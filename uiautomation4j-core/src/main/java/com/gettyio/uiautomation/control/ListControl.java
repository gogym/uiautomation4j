package com.gettyio.uiautomation.control;

import com.gettyio.uiautomation.condition.SearchCondition;
import com.gettyio.uiautomation.enums.ControlType;

/**
 * 列表控件
 *
 * <p>对应 UIAutomation 的 List 控件类型 (ControlType=50008)。
 * 支持 {@link SelectionPattern} 获取选中项，支持 {@link ScrollPattern} 滚动操作。</p>
 */
public class ListControl extends Control {

    public ListControl() {
        super();
    }

    public ListControl(SearchCondition searchCondition) {
        super(searchCondition);
    }

    @Override
    public ControlType getControlType() {
        return ControlType.List;
    }
}
