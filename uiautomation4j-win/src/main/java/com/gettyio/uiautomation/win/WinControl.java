package com.gettyio.uiautomation.win;

import com.gettyio.uiautomation.win.com.IUIAutomationElement;

/**
 * Windows COM Element 的 Java 包装类
 * 提供对底层 IUIAutomationElement 的类型安全访问
 */
public class WinControl {

    private final IUIAutomationElement element;

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
     */
    public String getName() {
        return element.getName();
    }

    /**
     * 获取控件类名
     */
    public String getClassName() {
        return element.getClassName();
    }

    /**
     * 获取 AutomationId
     */
    public String getAutomationId() {
        return element.getAutomationId();
    }

    /**
     * 获取控件类型 ID
     */
    public int getControlType() {
        return element.getControlType();
    }

    /**
     * 获取进程 ID
     */
    public int getProcessId() {
        return element.getProcessId();
    }

    /**
     * 获取控件边界矩形 [left, top, right, bottom]
     */
    public int[] getBoundingRectangle() {
        return element.getBoundingRectangle();
    }

    /**
     * 设置焦点
     */
    public void setFocus() {
        element.setFocus();
    }

    /**
     * 释放 COM 资源
     */
    public void release() {
        element.release();
    }
}
