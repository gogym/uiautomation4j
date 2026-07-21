package com.gettyio.uiautomation.control;

import com.gettyio.uiautomation.condition.SearchCondition;
import com.gettyio.uiautomation.enums.ControlType;

/**
 * 窗口控件
 */
public class WindowControl extends Control {

    public WindowControl() {
        super();
    }

    public WindowControl(SearchCondition searchCondition) {
        super(searchCondition);
    }

    @Override
    public ControlType getControlType() {
        return ControlType.Window;
    }

    /**
     * 设置窗口置顶
     */
    public void setTopmost(boolean topmost) {
        getWindowPattern().setTopmost(topmost);
    }

    /**
     * 关闭窗口
     */
    public void close() {
        getWindowPattern().close();
    }

    /**
     * 最大化窗口
     */
    public void maximize() {
        getWindowPattern().maximize();
    }

    /**
     * 最小化窗口
     */
    public void minimize() {
        getWindowPattern().minimize();
    }
}
