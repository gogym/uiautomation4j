package com.gettyio.uiautomation.control;

import com.gettyio.uiautomation.condition.SearchCondition;
import com.gettyio.uiautomation.enums.ControlType;

/**
 * 标题栏控件
 */
public class TitleBarControl extends Control {

    public TitleBarControl() {
        super();
    }

    public TitleBarControl(SearchCondition searchCondition) {
        super(searchCondition);
    }

    @Override
    public ControlType getControlType() {
        return ControlType.TitleBar;
    }
}
