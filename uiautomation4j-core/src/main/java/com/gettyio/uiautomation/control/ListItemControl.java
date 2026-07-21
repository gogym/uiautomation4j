package com.gettyio.uiautomation.control;

import com.gettyio.uiautomation.condition.SearchCondition;
import com.gettyio.uiautomation.enums.ControlType;

/**
 * 列表项控件
 */
public class ListItemControl extends Control {

    public ListItemControl() {
        super();
    }

    public ListItemControl(SearchCondition searchCondition) {
        super(searchCondition);
    }

    @Override
    public ControlType getControlType() {
        return ControlType.ListItem;
    }
}
