package com.gettyio.uiautomation.control;

import com.gettyio.uiautomation.condition.SearchCondition;
import com.gettyio.uiautomation.enums.ControlType;

/**
 * TreeItem 控件
 */
public class TreeItemControl extends Control {

    public TreeItemControl() {
        super();
    }

    public TreeItemControl(SearchCondition searchCondition) {
        super(searchCondition);
    }

    @Override
    public ControlType getControlType() {
        return ControlType.TreeItem;
    }
}
