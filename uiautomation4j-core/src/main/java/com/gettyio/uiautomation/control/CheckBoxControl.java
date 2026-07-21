package com.gettyio.uiautomation.control;

import com.gettyio.uiautomation.condition.SearchCondition;
import com.gettyio.uiautomation.enums.ControlType;

/**
 * 复选框控件
 *
 * <p>对应 UIAutomation 的 CheckBox 控件类型 (ControlType=50002)。
 * 支持 {@link InvokePattern} 切换选中状态。</p>
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
