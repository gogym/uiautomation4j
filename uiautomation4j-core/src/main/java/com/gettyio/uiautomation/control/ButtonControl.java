package com.gettyio.uiautomation.control;

import com.gettyio.uiautomation.condition.SearchCondition;
import com.gettyio.uiautomation.enums.ControlType;

/**
 * 按钮控件
 */
public class ButtonControl extends Control {

    public ButtonControl() {
        super();
    }

    public ButtonControl(SearchCondition searchCondition) {
        super(searchCondition);
    }

    @Override
    public ControlType getControlType() {
        return ControlType.Button;
    }
}
