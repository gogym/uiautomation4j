package com.gettyio.uiautomation.control;

import com.gettyio.uiautomation.condition.SearchCondition;
import com.gettyio.uiautomation.enums.ControlType;

/**
 * 菜单栏控件
 */
public class MenuBarControl extends Control {

    public MenuBarControl() {
        super();
    }

    public MenuBarControl(SearchCondition searchCondition) {
        super(searchCondition);
    }

    @Override
    public ControlType getControlType() {
        return ControlType.MenuBar;
    }
}
