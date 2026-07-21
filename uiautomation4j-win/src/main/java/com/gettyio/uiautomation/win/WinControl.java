package com.gettyio.uiautomation.win;

import com.gettyio.uiautomation.win.com.IUIAutomationElement;

/**
 * Windows COM Element 的 Java 包装类
 * 提供对底层 IUIAutomationElement 的类型安全访问
 *
 * <p>WinControl 是 UI 元素在 Java 层的表示，封装了对 COM 元素对象的常用属性访问。
 * 每个 WinControl 实例对应一个 IUIAutomationElement COM 对象。
 *
 * <p>主要功能：
 * <ul>
 *   <li>获取元素属性：名称、类名、AutomationId、控件类型、进程 ID</li>
 *   <li>获取元素位置：边界矩形（BoundingRectangle）</li>
 *   <li>元素操作：设置焦点、释放 COM 资源</li>
 * </ul>
 *
 * <p>使用示例：
 * <pre>
 * WinControl ctrl = new WinControl(element);
 * String name = ctrl.getName();         // 获取控件名称
 * int[] rect = ctrl.getBoundingRectangle(); // 获取边界矩形
 * ctrl.setFocus();                       // 设置焦点
 * ctrl.release();                        // 释放 COM 资源
 * </pre>
 */
public class WinControl {

    /** 底层 COM 元素对象，提供对 IUIAutomationElement 接口的访问 */
    private final IUIAutomationElement element;

    /**
     * 构造 WinControl，包装指定的 COM 元素对象
     *
     * @param element IUIAutomationElement COM 对象，不能为 null
     * @throws IllegalArgumentException 如果 element 为 null
     */
    public WinControl(IUIAutomationElement element) {
        if (element == null) {
            throw new IllegalArgumentException("IUIAutomationElement 不能为空");
        }
        this.element = element;
    }

    /**
     * 获取底层 COM Element
     */
    public IUIAutomationElement getElement() {
        return element;
    }

    /**
     * 获取控件名称
     * <p>对应 UIA_NamePropertyId (30005)
     *
     * @return 控件名称字符串
     */
    public String getName() {
        return element.getName();
    }

    /**
     * 获取控件类名
     * <p>对应 UIA_ClassNamePropertyId (30012)，如 "Button"、"Edit"、"Notepad" 等
     *
     * @return 控件类名字符串
     */
    public String getClassName() {
        return element.getClassName();
    }

    /**
     * 获取 AutomationId
     * <p>对应 UIA_AutomationIdPropertyId (30011)，是控件的唯一标识符
     *
     * @return AutomationId 字符串
     */
    public String getAutomationId() {
        return element.getAutomationId();
    }

    /**
     * 获取控件类型 ID
     * <p>对应 UIA_ControlTypePropertyId (30003)，值对应 {@link com.gettyio.uiautomation.enums.ControlType} 中的 ID
     *
     * @return 控件类型 ID（如 50000=Button, 50003=Edit 等）
     */
    public int getControlType() {
        return element.getControlType();
    }

    /**
     * 获取控件所属进程的 ID
     * <p>对应 UIA_ProcessIdPropertyId (30002)
     *
     * @return 进程 ID（PID）
     */
    public int getProcessId() {
        return element.getProcessId();
    }

    /**
     * 获取控件边界矩形
     * <p>对应 UIA_BoundingRectanglePropertyId (30001)
     *
     * @return 包含 4 个元素的数组：[left, top, right, bottom]，单位为屏幕像素
     */
    public int[] getBoundingRectangle() {
        return element.getBoundingRectangle();
    }

    /**
     * 设置控件焦点
     * <p>调用 IUIAutomationElement::SetFocus()，使控件获得键盘焦点
     */
    public void setFocus() {
        element.setFocus();
    }

    /**
     * 释放底层 COM 资源
     * <p>调用 IUIAutomationElement 的 Release 方法，减少 COM 引用计数。
     * 释放后不应再使用此对象。
     */
    public void release() {
        element.release();
    }
}
