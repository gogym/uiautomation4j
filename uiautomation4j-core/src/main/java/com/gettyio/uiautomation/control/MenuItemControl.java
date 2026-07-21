package com.gettyio.uiautomation.control;

import com.gettyio.uiautomation.condition.SearchCondition;
import com.gettyio.uiautomation.enums.ControlType;

/**
 * 菜单项控件
 */
public class MenuItemControl extends Control {

    public MenuItemControl() {
        super();
    }

    public MenuItemControl(SearchCondition searchCondition) {
        super(searchCondition);
    }

    @Override
    public ControlType getControlType() {
        return ControlType.MenuItem;
    }
}
