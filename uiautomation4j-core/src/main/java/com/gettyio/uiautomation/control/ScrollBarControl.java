package com.gettyio.uiautomation.control;

import com.gettyio.uiautomation.condition.SearchCondition;
import com.gettyio.uiautomation.enums.ControlType;

/**
 * 滚动条控件
 *
 * <p>对应 UIAutomation 的 ScrollBar 控件类型 (ControlType=50014)。
 * 支持 {@link ScrollPattern} 进行滚动操作。</p>
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
