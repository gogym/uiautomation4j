package com.gettyio.uiautomation.control;

import com.gettyio.uiautomation.condition.SearchCondition;
import com.gettyio.uiautomation.enums.ControlType;

/**
 * 复选框控件
 */
public class CheckBoxControl extends Control {

    public CheckBoxControl() {
        super();
    }

    public CheckBoxControl(SearchCondition searchCondition) {
        super(searchCondition);
    }

    @Override
    public ControlType getControlType() {
        return ControlType.CheckBox;
    }
}
