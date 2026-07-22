package io.getbit.uiautomation.win;

import io.getbit.uiautomation.condition.SearchCondition;
import io.getbit.uiautomation.control.Control;
import io.getbit.uiautomation.enums.ControlType;
import io.getbit.uiautomation.exception.AutomationException;
import io.getbit.uiautomation.exception.ControlNotFoundException;
import io.getbit.uiautomation.pattern.*;
import io.getbit.uiautomation.spi.ControlBackend;
import io.getbit.uiautomation.win.com.*;
import io.getbit.uiautomation.win.keyboard.Keyboard;
import io.getbit.uiautomation.win.mouse.Mouse;
import io.getbit.uiautomation.win.pattern.*;
import io.getbit.uiautomation.win.screenshot.Screenshot;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.ptr.PointerByReference;

import java.util.ArrayList;
import java.util.List;

/**
 * Windows 平台的 ControlBackend 实现
 *
 * <p>通过 JNA + COM 调用 Windows UIAutomation API，实现 core 模块定义的
 * {@link io.getbit.uiautomation.spi.ControlBackend} SPI 接口。</p>
 *
 * <p>主要职责：</p>
 * <ul>
 *   <li>初始化 COM 和创建 {@link IUIAutomation} 实例</li>
 *   <li>实现控件搜索逻辑（基于 TreeScope + 属性条件）</li>
 *   <li>实现控件操作（click/doubleClick/rightClick 通过鼠标模拟）</li>
 *   <li>实现 Pattern 获取（通过 {@code GetCurrentPatternAs} COM 方法）</li>
 *   <li>实现智能等待（waitReady/exists）</li>
 * </ul>
 *
 * <p>搜索策略：</p>
 * <ol>
 *   <li>确定搜索起点（桌面根元素或父控件）</li>
 *   <li>构建 COM 属性条件（优先使用 Name/ClassName/AutomationId/ControlType）</li>
 *   <li>调用 {@code IUIAutomationElement::FindFirst} 执行搜索</li>
 *   <li>如果指定了深度限制，使用 {@link IUIAutomationTreeWalker} 进行深度遍历</li>
 *   <li>对于 subName/regexName 等复杂条件，在 Java 层进行二次过滤</li>
 * </ol>
 */
public class WinControlBackend implements ControlBackend {

    /** IUIAutomation COM 接口实例，用于创建条件和遍历元素树 */
    private IUIAutomation automation;
    /** TrueCondition，匹配所有元素，用于 TreeWalker 遍历 */
    private IUIAutomationCondition trueCondition;
    /** 全局搜索超时时间（秒），默认 10 秒 */
    private int globalSearchTimeout = 10;

    /**
     * 构造方法 - 初始化 COM 并创建 IUIAutomation 实例
     * <p>调用顺序: {@code CoInitializeEx} → {@code CoCreateInstance(CUIAutomation)}</p>
     */
    public WinControlBackend() {
        Win32Util.initCOM();
        this.automation = IUIAutomation.create();
        this.trueCondition = automation.createTrueCondition();
    }

    /**
     * 根据搜索条件查找控件
     * <p>搜索流程：searchElement → WinControlFactory.createControl → 返回对应 Control 子类</p>
     *
     * @param condition 搜索条件
     * @return 找到的控件
     * @throws io.getbit.uiautomation.exception.ControlNotFoundException 如果未找到
     */
    @Override
    public Control findControl(SearchCondition condition) {
        IUIAutomationElement element = searchElement(condition);
        if (element == null) {
            throw new ControlNotFoundException("未找到控件: " + condition);
        }
        Control control = WinControlFactory.createControl(element, condition);
        control.setNativeElement(element);
        control.setElementFound(true);
        return control;
    }

    /**
     * 检查控件是否存在（带重试）
     * <p>在超时时间内循环尝试搜索，每次间隔由 condition.getSearchInterval() 控制。</p>
     *
     * @param condition      搜索条件
     * @param maxWaitSeconds 最大等待秒数
     * @return true 表示在超时时间内找到了控件
     */
    @Override
    public boolean exists(SearchCondition condition, int maxWaitSeconds) {
        long startTime = System.currentTimeMillis();
        long timeoutMs = maxWaitSeconds * 1000L;

        while (System.currentTimeMillis() - startTime < timeoutMs) {
            try {
                IUIAutomationElement element = searchElement(condition);
                if (element != null) {
                    element.release();
                    return true;
                }
            } catch (Exception e) {
                // 忽略异常，继续重试
            }
            try {
                Thread.sleep(condition.getSearchInterval());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return false;
    }

    @Override
    public ValuePattern getValuePattern(Control control) {
        IUIAutomationElement element = getElement(control);
        Pointer patternPtr = element.getCurrentPatternAs(
                IUIAutomationValuePattern.PATTERN_ID, IUIAutomationValuePattern.IID);
        if (patternPtr == null || patternPtr == Pointer.NULL) {
            throw new AutomationException("控件不支持 ValuePattern");
        }
        IUIAutomationValuePattern comPattern = new IUIAutomationValuePattern(patternPtr);
        return new WinValuePattern(comPattern);
    }

    @Override
    public WindowPattern getWindowPattern(Control control) {
        IUIAutomationElement element = getElement(control);
        Pointer patternPtr = element.getCurrentPatternAs(
                IUIAutomationWindowPattern.PATTERN_ID, IUIAutomationWindowPattern.IID);
        if (patternPtr == null || patternPtr == Pointer.NULL) {
            throw new AutomationException("控件不支持 WindowPattern");
        }
        IUIAutomationWindowPattern comPattern = new IUIAutomationWindowPattern(patternPtr);
        long hwnd = element.getNativeWindowHandle();
        return new WinWindowPattern(comPattern, hwnd);
    }

    @Override
    public InvokePattern getInvokePattern(Control control) {
        IUIAutomationElement element = getElement(control);
        Pointer patternPtr = element.getCurrentPatternAs(
                IUIAutomationInvokePattern.PATTERN_ID, IUIAutomationInvokePattern.IID);
        if (patternPtr == null || patternPtr == Pointer.NULL) {
            throw new AutomationException("控件不支持 InvokePattern");
        }
        IUIAutomationInvokePattern comPattern = new IUIAutomationInvokePattern(patternPtr);
        return new WinInvokePattern(comPattern);
    }

    @Override
    public SelectionPattern getSelectionPattern(Control control) {
        IUIAutomationElement element = getElement(control);
        Pointer patternPtr = element.getCurrentPatternAs(10001,
                "{5ed5202e-b2ac-47a6-b638-4b0bf140d78e}");
        if (patternPtr == null || patternPtr == Pointer.NULL) {
            throw new AutomationException("控件不支持 SelectionPattern");
        }
        return new WinSelectionPattern(patternPtr);
    }

    @Override
    public ScrollPattern getScrollPattern(Control control) {
        IUIAutomationElement element = getElement(control);
        Pointer patternPtr = element.getCurrentPatternAs(10004,
                "{b4b191ce-3975-4e7d-a50d-13b1004d8c80}");
        if (patternPtr == null || patternPtr == Pointer.NULL) {
            throw new AutomationException("控件不支持 ScrollPattern");
        }
        return new WinScrollPattern(patternPtr);
    }

    @Override
    public ExpandCollapsePattern getExpandCollapsePattern(Control control) {
        IUIAutomationElement element = getElement(control);
        Pointer patternPtr = element.getCurrentPatternAs(10005,
                "{61927d36-3bea-4a29-8bd5-cc18ac6c0f0e}");
        if (patternPtr == null || patternPtr == Pointer.NULL) {
            throw new AutomationException("控件不支持 ExpandCollapsePattern");
        }
        return new WinExpandCollapsePattern(patternPtr);
    }

    @Override
    public TransformPattern getTransformPattern(Control control) {
        IUIAutomationElement element = getElement(control);
        Pointer patternPtr = element.getCurrentPatternAs(10016,
                "{a9b55844-a55d-4ef0-926d-569c16ff89bb}");
        if (patternPtr == null || patternPtr == Pointer.NULL) {
            throw new AutomationException("控件不支持 TransformPattern");
        }
        return new WinTransformPattern(patternPtr);
    }

    @Override
    public void sendKeys(Control control, String keys) {
        click(control);
        Keyboard.sendKeys(keys);
    }

    @Override
    public void click(Control control) {
        IUIAutomationElement element = getElement(control);
        int[] rect = element.getBoundingRectangle();
        int centerX = (rect[0] + rect[2]) / 2;
        int centerY = (rect[1] + rect[3]) / 2;
        Mouse.click(centerX, centerY);
    }

    @Override
    public void doubleClick(Control control) {
        IUIAutomationElement element = getElement(control);
        int[] rect = element.getBoundingRectangle();
        int centerX = (rect[0] + rect[2]) / 2;
        int centerY = (rect[1] + rect[3]) / 2;
        Mouse.doubleClick(centerX, centerY);
    }

    @Override
    public void rightClick(Control control) {
        IUIAutomationElement element = getElement(control);
        int[] rect = element.getBoundingRectangle();
        int centerX = (rect[0] + rect[2]) / 2;
        int centerY = (rect[1] + rect[3]) / 2;
        Mouse.rightClick(centerX, centerY);
    }

    @Override
    public void captureToImage(Control control, String filePath) {
        IUIAutomationElement element = getElement(control);
        int[] rect = element.getBoundingRectangle();
        Screenshot.captureRegion(filePath, rect[0], rect[1],
                rect[2] - rect[0], rect[3] - rect[1]);
    }

    @Override
    public void setGlobalSearchTimeout(int seconds) {
        this.globalSearchTimeout = seconds;
    }

    @Override
    public int getGlobalSearchTimeout() {
        return globalSearchTimeout;
    }

    @Override
    public boolean waitReady(Control control, int maxWaitSeconds) {
        long startTime = System.currentTimeMillis();
        long timeoutMs = maxWaitSeconds * 1000L;

        while (System.currentTimeMillis() - startTime < timeoutMs) {
            try {
                IUIAutomationElement element = searchElement(control.getSearchCondition());
                if (element != null) {
                    boolean enabled = element.isEnabled();
                    boolean visible = !element.isOffscreen();
                    if (enabled && visible) {
                        element.release();
                        return true;
                    }
                    element.release();
                }
            } catch (Exception e) {
                // 忽略异常，继续重试
            }
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean isEnabled(Control control) {
        IUIAutomationElement element = getElement(control);
        return element.isEnabled();
    }

    @Override
    public boolean isVisible(Control control) {
        IUIAutomationElement element = getElement(control);
        return !element.isOffscreen();
    }

    // ==================== 批量查找与树遍历 ====================

    @Override
    public List<Control> findControls(SearchCondition condition) {
        List<Control> results = new ArrayList<>();
        IUIAutomationElement root;
        if (condition.getSearchFrom() != null) {
            Control parentControl = findControl(condition.getSearchFrom());
            root = (IUIAutomationElement) parentControl.getNativeElement();
        } else {
            root = automation.getRootElement();
        }
        if (root == null) return results;

        int scope = IUIAutomation.TreeScope_Subtree;
        if (condition.getSearchDepth() == 1) {
            scope = IUIAutomation.TreeScope_Children;
        }

        IUIAutomationCondition comCondition = buildComCondition(condition);
        if (comCondition == null) comCondition = trueCondition;

        Pointer arrayPtr = root.findAll(scope, comCondition);
        if (arrayPtr == null || arrayPtr == Pointer.NULL) return results;

        IUIAutomationElementArray elementArray = new IUIAutomationElementArray(arrayPtr);
        int length = elementArray.getLength();
        for (int i = 0; i < length; i++) {
            IUIAutomationElement elem = elementArray.getElement(i);
            if (elem != null && matchesCondition(elem, condition)) {
                Control control = WinControlFactory.createControl(elem, condition);
                control.setNativeElement(elem);
                control.setElementFound(true);
                results.add(control);
            }
        }
        return results;
    }

    @Override
    public List<Control> getChildren(Control parent) {
        List<Control> children = new ArrayList<>();
        IUIAutomationElement element = getElement(parent);
        IUIAutomationTreeWalker walker = automation.createTreeWalker(trueCondition);
        IUIAutomationElement child = walker.getFirstChildElement(element);
        while (child != null) {
            SearchCondition childCond = SearchCondition.builder().build();
            Control control = WinControlFactory.createControl(child, childCond);
            control.setNativeElement(child);
            control.setElementFound(true);
            children.add(control);
            child = walker.getNextSiblingElement(child);
        }
        walker.dispose();
        return children;
    }

    @Override
    public Control getFirstChild(Control parent) {
        IUIAutomationElement element = getElement(parent);
        IUIAutomationElement firstChild = element.getFirstChild();
        if (firstChild == null) return null;
        SearchCondition childCond = SearchCondition.builder().build();
        Control control = WinControlFactory.createControl(firstChild, childCond);
        control.setNativeElement(firstChild);
        control.setElementFound(true);
        return control;
    }

    @Override
    public Control getNextSibling(Control control) {
        IUIAutomationElement element = getElement(control);
        IUIAutomationElement nextSibling = element.getNextSibling();
        if (nextSibling == null) return null;
        SearchCondition siblingCond = SearchCondition.builder().build();
        Control siblingControl = WinControlFactory.createControl(nextSibling, siblingCond);
        siblingControl.setNativeElement(nextSibling);
        siblingControl.setElementFound(true);
        return siblingControl;
    }

    // ==================== 原生属性直接访问 ====================

    @Override
    public String getElementName(Control control) {
        IUIAutomationElement element = getElement(control);
        return element.getName();
    }

    @Override
    public int[] getElementBoundingRectangle(Control control) {
        IUIAutomationElement element = getElement(control);
        return element.getBoundingRectangle();
    }

    @Override
    public int[] getElementRuntimeId(Control control) {
        IUIAutomationElement element = getElement(control);
        return element.getRuntimeId();
    }

    @Override
    public ControlType getElementControlType(Control control) {
        IUIAutomationElement element = getElement(control);
        int typeId = element.getControlType();
        try {
            return ControlType.fromId(typeId);
        } catch (IllegalArgumentException e) {
            return ControlType.Pane; // 未知类型回退
        }
    }

    @Override
    public String getElementClassName(Control control) {
        IUIAutomationElement element = getElement(control);
        String className = element.getClassName();
        return className != null ? className : "";
    }

    @Override
    public String getElementAutomationId(Control control) {
        IUIAutomationElement element = getElement(control);
        String id = element.getAutomationId();
        return id != null ? id : "";
    }

    @Override
    public int getElementProcessId(Control control) {
        IUIAutomationElement element = getElement(control);
        return element.getProcessId();
    }

    // ==================== 内部方法 ====================

    /**
     * 根据搜索条件查找元素（内部方法）
     *
     * <p>搜索策略：</p>
     * <ol>
     *   <li>确定搜索起点（searchFrom 或桌面根元素）</li>
     *   <li>确定 TreeScope（Element/Children/Subtree）</li>
     *   <li>构建 COM 属性条件</li>
     *   <li>调用 FindFirst 搜索</li>
     *   <li>如果指定了深度限制，使用 TreeWalker 深度遍历</li>
     * </ol>
     *
     * @param condition 搜索条件
     * @return 找到的元素，未找到返回 null
     */
    private IUIAutomationElement searchElement(SearchCondition condition) {
        // 确定搜索起点
        IUIAutomationElement root;
        if (condition.getSearchFrom() != null) {
            // 从父控件开始搜索
            Control parentControl = findControl(condition.getSearchFrom());
            root = (IUIAutomationElement) parentControl.getNativeElement();
        } else {
            root = automation.getRootElement();
        }

        if (root == null) {
            return null;
        }

        // 确定搜索范围
        int scope;
        if (condition.getDepth() != null) {
            scope = IUIAutomation.TreeScope_Element;
        } else if (condition.getSearchDepth() == 1) {
            scope = IUIAutomation.TreeScope_Children;
        } else {
            scope = IUIAutomation.TreeScope_Subtree;
        }

        // 构建搜索条件
        IUIAutomationCondition comCondition = buildComCondition(condition);
        if (comCondition == null) {
            comCondition = trueCondition;
        }

        // 执行搜索
        IUIAutomationElement found = root.findFirst(scope, comCondition);

        // 如果指定了深度，使用 TreeWalker 进行深度限制搜索
        if (found == null && condition.getDepth() != null) {
            found = searchWithDepthLimit(root, condition, comCondition, 0);
        }

        return found;
    }

    /**
     * 深度限制搜索
     * <p>使用 TreeWalker 遍历 UI 树，仅搜索到指定深度。
     * 对每个元素调用 {@code matchesCondition} 进行二次过滤。</p>
     *
     * @param parent       父元素
     * @param condition    搜索条件
     * @param comCondition COM 条件（未使用，保留用于未来优化）
     * @param currentDepth 当前深度
     * @return 匹配的元素，未找到返回 null
     */
    private IUIAutomationElement searchWithDepthLimit(IUIAutomationElement parent,
                                                       SearchCondition condition,
                                                       IUIAutomationCondition comCondition,
                                                       int currentDepth) {
        int maxDepth = condition.getDepth();
        if (currentDepth > maxDepth) {
            return null;
        }

        // 先检查当前元素是否匹配
        if (matchesCondition(parent, condition)) {
            return parent;
        }

        // 递归搜索子元素
        IUIAutomationTreeWalker walker = automation.createTreeWalker(trueCondition);
        IUIAutomationElement child = walker.getFirstChildElement(parent);
        while (child != null) {
            IUIAutomationElement result = searchWithDepthLimit(child, condition, comCondition, currentDepth + 1);
            if (result != null) {
                walker.dispose();
                return result;
            }
            IUIAutomationElement next = walker.getNextSiblingElement(child);
            child.release();
            child = next;
        }
        walker.dispose();
        return null;
    }

    /**
     * 检查元素是否匹配搜索条件（Java 层二次过滤）
     * <p>COM 层只能按单一属性搜索，复杂条件（subName、regexName 等）
     * 需要在找到元素后在 Java 层进行二次验证。</p>
     *
     * @param element   要检查的元素
     * @param condition 搜索条件
     * @return true 表示元素匹配所有条件
     */
    private boolean matchesCondition(IUIAutomationElement element, SearchCondition condition) {
        if (condition.hasName()) {
            String name = element.getName();
            if (!condition.getName().equals(name)) {
                return false;
            }
        }
        if (condition.hasSubName()) {
            String name = element.getName();
            if (name == null || !name.contains(condition.getSubName())) {
                return false;
            }
        }
        if (condition.hasClassName()) {
            String className = element.getClassName();
            if (!condition.getClassName().equals(className)) {
                return false;
            }
        }
        if (condition.hasAutomationId()) {
            String autoId = element.getAutomationId();
            if (!condition.getAutomationId().equals(autoId)) {
                return false;
            }
        }
        if (condition.hasControlType()) {
            int type = element.getControlType();
            if (type != condition.getControlType().getId()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 构建 COM 搜索条件
     * <p>优先使用 Name 属性构建条件，因为 Name 是最常用的搜索条件。
     * 依次尝试: Name → ClassName → AutomationId → ControlType。</p>
     *
     * @param condition 搜索条件
     * @return COM 条件对象，如果没有可构建的条件则返回 null
     */
    private IUIAutomationCondition buildComCondition(SearchCondition condition) {
        java.util.List<IUIAutomationCondition> conditions = new java.util.ArrayList<>();

        if (condition.hasName()) {
            Pointer bstr = Win32Util.stringToBstr(condition.getName());
            try {
                conditions.add(automation.createPropertyCondition(
                        IUIAutomation.UIA_NamePropertyId, bstr));
            } finally {
                Win32Util.freeBstr(bstr);
            }
        }
        if (condition.hasClassName()) {
            Pointer bstr = Win32Util.stringToBstr(condition.getClassName());
            try {
                conditions.add(automation.createPropertyCondition(
                        IUIAutomation.UIA_ClassNamePropertyId, bstr));
            } finally {
                Win32Util.freeBstr(bstr);
            }
        }
        if (condition.hasAutomationId()) {
            Pointer bstr = Win32Util.stringToBstr(condition.getAutomationId());
            try {
                conditions.add(automation.createPropertyCondition(
                        IUIAutomation.UIA_AutomationIdPropertyId, bstr));
            } finally {
                Win32Util.freeBstr(bstr);
            }
        }
        if (condition.hasControlType()) {
            com.sun.jna.platform.win32.Variant.VARIANT.ByReference v =
                    new com.sun.jna.platform.win32.Variant.VARIANT.ByReference();
            v.setValue(com.sun.jna.platform.win32.Variant.VT_INT, condition.getControlType().getId());
            conditions.add(automation.createPropertyCondition(
                    IUIAutomation.UIA_ControlTypePropertyId, v.getPointer()));
        }

        if (conditions.isEmpty()) {
            return null;
        }

        IUIAutomationCondition result = conditions.get(0);
        for (int i = 1; i < conditions.size(); i++) {
            result = automation.createAndCondition(result, conditions.get(i));
        }
        return result;
    }

    /**
     * 获取控件的 COM Element
     * <p>如果控件的 nativeElement 已经是 IUIAutomationElement，直接返回；
     * 否则重新搜索并缓存结果。</p>
     *
     * @param control 控件
     * @return COM Element
     * @throws io.getbit.uiautomation.exception.ControlNotFoundException 如果找不到
     */
    private IUIAutomationElement getElement(Control control) {
        if (control.getNativeElement() instanceof IUIAutomationElement) {
            return (IUIAutomationElement) control.getNativeElement();
        }
        // 如果 nativeElement 不是 IUIAutomationElement，重新搜索
        IUIAutomationElement element = searchElement(control.getSearchCondition());
        if (element == null) {
            throw new ControlNotFoundException("控件不存在: " + control.getSearchCondition());
        }
        control.setNativeElement(element);
        return element;
    }
}
