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
 * <p><b>线程安全：</b>所有 COM 调用都通过 {@link StaTaskExecutor} 委托到专用 STA 线程执行，
 * 确保 UIA COM 对象的线程亲和性得到满足，避免 {@code RPC_E_WRONG_THREAD} 错误。</p>
 *
 * <p>主要职责：</p>
 * <ul>
 *   <li>通过 STA 线程初始化 COM 并创建 {@link IUIAutomation} 实例</li>
 *   <li>实现控件搜索逻辑（基于 TreeScope + 属性条件）</li>
 *   <li>实现控件操作（click/doubleClick/rightClick 通过鼠标模拟）</li>
 *   <li>实现 Pattern 获取（通过 {@code GetCurrentPatternAs} COM 方法）</li>
 *   <li>实现智能等待（waitReady/exists）</li>
 * </ul>
 */
public class WinControlBackend implements ControlBackend {

    /** STA 线程执行器，所有 COM 调用都通过此执行器委托到 STA 线程 */
    private final StaTaskExecutor executor;

    /** IUIAutomation COM 接口实例，仅在 STA 线程中使用 */
    private IUIAutomation automation;
    /** TrueCondition，匹配所有元素，用于 TreeWalker 遍历，仅在 STA 线程中使用 */
    private IUIAutomationCondition trueCondition;
    /** 全局搜索超时时间（秒），默认 10 秒 */
    private int globalSearchTimeout = 10;

    /**
     * 构造方法 - 通过 STA 执行器初始化 COM 并创建 IUIAutomation 实例
     *
     * <p>所有 COM 初始化操作都在 STA 线程中执行，确保 COM 对象的线程亲和性。</p>
     *
     * @param executor STA 线程执行器（必须已启动）
     */
    public WinControlBackend(StaTaskExecutor executor) {
        this.executor = executor;
        // 在 STA 线程中初始化 COM 对象
        executor.submit(() -> {
            this.automation = IUIAutomation.create();
            this.trueCondition = automation.createTrueCondition();
        });
    }

    /**
     * 根据搜索条件查找控件
     *
     * @param condition 搜索条件
     * @return 找到的控件
     * @throws ControlNotFoundException 如果未找到
     */
    @Override
    public Control findControl(SearchCondition condition) {
        Pointer elementPtr = executor.submit(() -> searchElementOnSta(condition));
        if (elementPtr == null) {
            throw new ControlNotFoundException("未找到控件: " + condition);
        }
        IUIAutomationElement element = new IUIAutomationElement(elementPtr);
        Control control = WinControlFactory.createControl(element, condition);
        control.setNativeElement(element);
        control.setElementFound(true);
        return control;
    }

    /**
     * 检查控件是否存在（带重试）
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
                Pointer elementPtr = executor.submit(() -> searchElementOnSta(condition));
                if (elementPtr != null) {
                    // 在 STA 线程中释放元素
                    executor.submit(() -> new IUIAutomationElement(elementPtr).release());
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
        return executor.submit(() -> {
            IUIAutomationElement element = getStaElement(control);
            Pointer patternPtr = element.getCurrentPatternAs(
                    IUIAutomationValuePattern.PATTERN_ID, IUIAutomationValuePattern.IID);
            if (patternPtr == null || patternPtr == Pointer.NULL) {
                throw new AutomationException("控件不支持 ValuePattern");
            }
            IUIAutomationValuePattern comPattern = new IUIAutomationValuePattern(patternPtr);
            return new WinValuePattern(comPattern);
        });
    }

    @Override
    public WindowPattern getWindowPattern(Control control) {
        return executor.submit(() -> {
            IUIAutomationElement element = getStaElement(control);
            Pointer patternPtr = element.getCurrentPatternAs(
                    IUIAutomationWindowPattern.PATTERN_ID, IUIAutomationWindowPattern.IID);
            if (patternPtr == null || patternPtr == Pointer.NULL) {
                throw new AutomationException("控件不支持 WindowPattern");
            }
            IUIAutomationWindowPattern comPattern = new IUIAutomationWindowPattern(patternPtr);
            long hwnd = element.getNativeWindowHandle();
            return new WinWindowPattern(comPattern, hwnd);
        });
    }

    @Override
    public InvokePattern getInvokePattern(Control control) {
        return executor.submit(() -> {
            IUIAutomationElement element = getStaElement(control);
            Pointer patternPtr = element.getCurrentPatternAs(
                    IUIAutomationInvokePattern.PATTERN_ID, IUIAutomationInvokePattern.IID);
            if (patternPtr == null || patternPtr == Pointer.NULL) {
                throw new AutomationException("控件不支持 InvokePattern");
            }
            IUIAutomationInvokePattern comPattern = new IUIAutomationInvokePattern(patternPtr);
            return new WinInvokePattern(comPattern);
        });
    }

    @Override
    public SelectionPattern getSelectionPattern(Control control) {
        return executor.submit(() -> {
            IUIAutomationElement element = getStaElement(control);
            Pointer patternPtr = element.getCurrentPatternAs(10001,
                    "{5ed5202e-b2ac-47a6-b638-4b0bf140d78e}");
            if (patternPtr == null || patternPtr == Pointer.NULL) {
                throw new AutomationException("控件不支持 SelectionPattern");
            }
            return new WinSelectionPattern(patternPtr);
        });
    }

    @Override
    public ScrollPattern getScrollPattern(Control control) {
        return executor.submit(() -> {
            IUIAutomationElement element = getStaElement(control);
            Pointer patternPtr = element.getCurrentPatternAs(10004,
                    "{b4b191ce-3975-4e7d-a50d-13b1004d8c80}");
            if (patternPtr == null || patternPtr == Pointer.NULL) {
                throw new AutomationException("控件不支持 ScrollPattern");
            }
            return new WinScrollPattern(patternPtr);
        });
    }

    @Override
    public ExpandCollapsePattern getExpandCollapsePattern(Control control) {
        return executor.submit(() -> {
            IUIAutomationElement element = getStaElement(control);
            Pointer patternPtr = element.getCurrentPatternAs(10005,
                    "{61927d36-3bea-4a29-8bd5-cc18ac6c0f0e}");
            if (patternPtr == null || patternPtr == Pointer.NULL) {
                throw new AutomationException("控件不支持 ExpandCollapsePattern");
            }
            return new WinExpandCollapsePattern(patternPtr);
        });
    }

    @Override
    public TransformPattern getTransformPattern(Control control) {
        return executor.submit(() -> {
            IUIAutomationElement element = getStaElement(control);
            Pointer patternPtr = element.getCurrentPatternAs(10016,
                    "{a9b55844-a55d-4ef0-926d-569c16ff89bb}");
            if (patternPtr == null || patternPtr == Pointer.NULL) {
                throw new AutomationException("控件不支持 TransformPattern");
            }
            return new WinTransformPattern(patternPtr);
        });
    }

    @Override
    public void sendKeys(Control control, String keys) {
        click(control);
        Keyboard.sendKeys(keys);
    }

    @Override
    public void click(Control control) {
        int[] center = executor.submit(() -> {
            IUIAutomationElement element = getStaElement(control);
            int[] rect = element.getBoundingRectangle();
            return new int[]{(rect[0] + rect[2]) / 2, (rect[1] + rect[3]) / 2};
        });
        Mouse.click(center[0], center[1]);
    }

    @Override
    public void doubleClick(Control control) {
        int[] center = executor.submit(() -> {
            IUIAutomationElement element = getStaElement(control);
            int[] rect = element.getBoundingRectangle();
            return new int[]{(rect[0] + rect[2]) / 2, (rect[1] + rect[3]) / 2};
        });
        Mouse.doubleClick(center[0], center[1]);
    }

    @Override
    public void rightClick(Control control) {
        int[] center = executor.submit(() -> {
            IUIAutomationElement element = getStaElement(control);
            int[] rect = element.getBoundingRectangle();
            return new int[]{(rect[0] + rect[2]) / 2, (rect[1] + rect[3]) / 2};
        });
        Mouse.rightClick(center[0], center[1]);
    }

    @Override
    public void captureToImage(Control control, String filePath) {
        int[] rect = executor.submit(() -> {
            IUIAutomationElement element = getStaElement(control);
            return element.getBoundingRectangle();
        });
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
                Boolean ready = executor.submit(() -> {
                    Pointer elementPtr = searchElementOnSta(control.getSearchCondition());
                    if (elementPtr != null) {
                        IUIAutomationElement element = new IUIAutomationElement(elementPtr);
                        boolean enabled = element.isEnabled();
                        boolean visible = !element.isOffscreen();
                        element.release();
                        return enabled && visible;
                    }
                    return false;
                });
                if (ready) {
                    return true;
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
        return executor.submit(() -> {
            IUIAutomationElement element = getStaElement(control);
            return element.isEnabled();
        });
    }

    @Override
    public boolean isVisible(Control control) {
        return executor.submit(() -> {
            IUIAutomationElement element = getStaElement(control);
            return !element.isOffscreen();
        });
    }

    // ==================== 批量查找与树遍历 ====================

    @Override
    public List<Control> findControls(SearchCondition condition) {
        return executor.submit(() -> {
            List<Control> results = new ArrayList<>();
            IUIAutomationElement root;
            if (condition.getSearchFrom() != null) {
                // 内部调用 findControlOnSta，避免死锁
                Pointer parentPtr = findControlOnSta(condition.getSearchFrom());
                root = new IUIAutomationElement(parentPtr);
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
        });
    }

    @Override
    public List<Control> getChildren(Control parent) {
        return executor.submit(() -> {
            List<Control> children = new ArrayList<>();
            IUIAutomationElement element = getStaElement(parent);
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
        });
    }

    @Override
    public Control getFirstChild(Control parent) {
        return executor.submit(() -> {
            IUIAutomationElement element = getStaElement(parent);
            IUIAutomationElement firstChild = element.getFirstChild();
            if (firstChild == null) return null;
            SearchCondition childCond = SearchCondition.builder().build();
            Control control = WinControlFactory.createControl(firstChild, childCond);
            control.setNativeElement(firstChild);
            control.setElementFound(true);
            return control;
        });
    }

    @Override
    public Control getNextSibling(Control control) {
        return executor.submit(() -> {
            IUIAutomationElement element = getStaElement(control);
            IUIAutomationElement nextSibling = element.getNextSibling();
            if (nextSibling == null) return null;
            SearchCondition siblingCond = SearchCondition.builder().build();
            Control siblingControl = WinControlFactory.createControl(nextSibling, siblingCond);
            siblingControl.setNativeElement(nextSibling);
            siblingControl.setElementFound(true);
            return siblingControl;
        });
    }

    // ==================== 原生属性直接访问 ====================

    @Override
    public String getElementName(Control control) {
        return executor.submit(() -> {
            IUIAutomationElement element = getStaElement(control);
            return element.getName();
        });
    }

    @Override
    public int[] getElementBoundingRectangle(Control control) {
        return executor.submit(() -> {
            IUIAutomationElement element = getStaElement(control);
            return element.getBoundingRectangle();
        });
    }

    @Override
    public int[] getElementRuntimeId(Control control) {
        return executor.submit(() -> {
            IUIAutomationElement element = getStaElement(control);
            return element.getRuntimeId();
        });
    }

    @Override
    public ControlType getElementControlType(Control control) {
        return executor.submit(() -> {
            IUIAutomationElement element = getStaElement(control);
            int typeId = element.getControlType();
            try {
                return ControlType.fromId(typeId);
            } catch (IllegalArgumentException e) {
                return ControlType.Pane; // 未知类型回退
            }
        });
    }

    @Override
    public String getElementClassName(Control control) {
        return executor.submit(() -> {
            IUIAutomationElement element = getStaElement(control);
            String className = element.getClassName();
            return className != null ? className : "";
        });
    }

    @Override
    public String getElementAutomationId(Control control) {
        return executor.submit(() -> {
            IUIAutomationElement element = getStaElement(control);
            String id = element.getAutomationId();
            return id != null ? id : "";
        });
    }

    @Override
    public int getElementProcessId(Control control) {
        return executor.submit(() -> {
            IUIAutomationElement element = getStaElement(control);
            return element.getProcessId();
        });
    }

    // ==================== STA 线程内部方法（仅在 STA 线程中调用） ====================

    /**
     * 获取控件的 COM Element（STA 线程内部方法）
     *
     * <p>从 Control 中取出 nativeElement 的 Pointer，在 STA 线程中重建
     * {@link IUIAutomationElement} 包装对象。如果 nativeElement 不是
     * {@link IUIAutomationElement}，则重新搜索。</p>
     *
     * <p><b>注意：</b>此方法必须在 STA 线程中调用，不可从外部直接调用。</p>
     *
     * @param control 控件
     * @return STA 线程中的 COM Element
     * @throws ControlNotFoundException 如果找不到
     */
    private IUIAutomationElement getStaElement(Control control) {
        Object nativeElement = control.getNativeElement();
        if (nativeElement instanceof IUIAutomationElement) {
            return (IUIAutomationElement) nativeElement;
        }
        // 如果 nativeElement 不是 IUIAutomationElement，重新搜索
        Pointer elementPtr = searchElementOnSta(control.getSearchCondition());
        if (elementPtr == null) {
            throw new ControlNotFoundException("控件不存在: " + control.getSearchCondition());
        }
        IUIAutomationElement element = new IUIAutomationElement(elementPtr);
        control.setNativeElement(element);
        return element;
    }

    /**
     * 在 STA 线程中查找控件，返回原始 Pointer（STA 线程内部方法）
     *
     * @param condition 搜索条件
     * @return COM Element 的 Pointer，未找到返回 null
     */
    private Pointer findControlOnSta(SearchCondition condition) {
        Pointer elementPtr = searchElementOnSta(condition);
        if (elementPtr == null) {
            throw new ControlNotFoundException("未找到控件: " + condition);
        }
        return elementPtr;
    }

    /**
     * 根据搜索条件查找元素（STA 线程内部方法）
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
     * @return 找到的元素的 Pointer，未找到返回 null
     */
    private Pointer searchElementOnSta(SearchCondition condition) {
        // 确定搜索起点
        IUIAutomationElement root;
        if (condition.getSearchFrom() != null) {
            Pointer parentPtr = findControlOnSta(condition.getSearchFrom());
            root = new IUIAutomationElement(parentPtr);
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
            Pointer foundPtr = searchWithDepthLimitOnSta(root, condition, comCondition, 0);
            return foundPtr;
        }

        return found != null ? found.getPointer() : null;
    }

    /**
     * 深度限制搜索（STA 线程内部方法）
     *
     * <p>使用 TreeWalker 遍历 UI 树，仅搜索到指定深度。
     * 对每个元素调用 {@link #matchesCondition} 进行二次过滤。</p>
     *
     * @param parent       父元素
     * @param condition    搜索条件
     * @param comCondition COM 条件
     * @param currentDepth 当前深度
     * @return 匹配的元素 Pointer，未找到返回 null
     */
    private Pointer searchWithDepthLimitOnSta(IUIAutomationElement parent,
                                               SearchCondition condition,
                                               IUIAutomationCondition comCondition,
                                               int currentDepth) {
        int maxDepth = condition.getDepth();
        if (currentDepth > maxDepth) {
            return null;
        }

        // 先检查当前元素是否匹配
        if (matchesCondition(parent, condition)) {
            return parent.getPointer();
        }

        // 递归搜索子元素
        IUIAutomationTreeWalker walker = automation.createTreeWalker(trueCondition);
        IUIAutomationElement child = walker.getFirstChildElement(parent);
        while (child != null) {
            Pointer result = searchWithDepthLimitOnSta(child, condition, comCondition, currentDepth + 1);
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
     * 检查元素是否匹配搜索条件（Java 层二次过滤，STA 线程内部方法）
     *
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
     * 构建 COM 搜索条件（STA 线程内部方法）
     *
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
}
