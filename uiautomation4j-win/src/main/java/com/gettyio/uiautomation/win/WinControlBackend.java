package com.gettyio.uiautomation.win;

import com.gettyio.uiautomation.condition.SearchCondition;
import com.gettyio.uiautomation.control.Control;
import com.gettyio.uiautomation.enums.ControlType;
import com.gettyio.uiautomation.exception.AutomationException;
import com.gettyio.uiautomation.exception.ControlNotFoundException;
import com.gettyio.uiautomation.pattern.*;
import com.gettyio.uiautomation.spi.ControlBackend;
import com.gettyio.uiautomation.win.com.*;
import com.gettyio.uiautomation.win.keyboard.Keyboard;
import com.gettyio.uiautomation.win.mouse.Mouse;
import com.gettyio.uiautomation.win.pattern.*;
import com.gettyio.uiautomation.win.screenshot.Screenshot;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.ptr.PointerByReference;

/**
 * Windows 平台的 ControlBackend 实现
 * 通过 JNA + COM 调用 Windows UIAutomation API
 */
public class WinControlBackend implements ControlBackend {

    private IUIAutomation automation;
    private IUIAutomationCondition trueCondition;
    private int globalSearchTimeout = 10; // 默认 10 秒

    public WinControlBackend() {
        Win32Util.initCOM();
        this.automation = IUIAutomation.create();
        this.trueCondition = automation.createTrueCondition();
    }

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
        return new WinWindowPattern(comPattern);
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

    // ==================== 内部方法 ====================

    /**
     * 根据搜索条件查找元素
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
     */
    private IUIAutomationElement searchWithDepthLimit(IUIAutomationElement parent,
                                                       SearchCondition condition,
                                                       IUIAutomationCondition comCondition,
                                                       int currentDepth) {
        int maxDepth = condition.getSearchDepth();
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
     * 检查元素是否匹配条件
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
     */
    private IUIAutomationCondition buildComCondition(SearchCondition condition) {
        // 优先使用 Name 属性构建条件
        if (condition.hasName()) {
            Pointer bstr = Win32Util.stringToBstr(condition.getName());
            try {
                return automation.createPropertyCondition(
                        IUIAutomation.UIA_NamePropertyId, bstr);
            } finally {
                Win32Util.freeBstr(bstr);
            }
        }
        if (condition.hasClassName()) {
            Pointer bstr = Win32Util.stringToBstr(condition.getClassName());
            try {
                return automation.createPropertyCondition(
                        IUIAutomation.UIA_ClassNamePropertyId, bstr);
            } finally {
                Win32Util.freeBstr(bstr);
            }
        }
        if (condition.hasAutomationId()) {
            Pointer bstr = Win32Util.stringToBstr(condition.getAutomationId());
            try {
                return automation.createPropertyCondition(
                        IUIAutomation.UIA_AutomationIdPropertyId, bstr);
            } finally {
                Win32Util.freeBstr(bstr);
            }
        }
        if (condition.hasControlType()) {
            com.sun.jna.platform.win32.Variant.VARIANT.ByReference v =
                    new com.sun.jna.platform.win32.Variant.VARIANT.ByReference();
            v.setValue(com.sun.jna.platform.win32.Variant.VT_INT, condition.getControlType().getId());
            return automation.createPropertyCondition(
                    IUIAutomation.UIA_ControlTypePropertyId, v.getPointer());
        }
        return null;
    }

    /**
     * 获取控件的 COM Element
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
