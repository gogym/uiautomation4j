package com.gettyio.uiautomation.control;

import com.gettyio.uiautomation.condition.SearchCondition;
import com.gettyio.uiautomation.enums.ControlType;

/**
 * 列表项控件
 *
 * <p>对应 UIAutomation 的 ListItem 控件类型 (ControlType=50007)。
 * 通常作为 {@link ListControl} 的子元素出现。</p>
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
