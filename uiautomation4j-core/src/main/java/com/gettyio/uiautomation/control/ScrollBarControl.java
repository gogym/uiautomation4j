package com.gettyio.uiautomation.control;

import com.gettyio.uiautomation.condition.SearchCondition;
import com.gettyio.uiautomation.enums.ControlType;

/**
 * 滚动条控件
 */
public class ScrollBarControl extends Control {

    public ScrollBarControl() {
        super();
    }

    public ScrollBarControl(SearchCondition searchCondition) {
        super(searchCondition);
    }

    @Override
    public ControlType getControlType() {
        return ControlType.ScrollBar;
    }
}
