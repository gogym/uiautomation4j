package com.gettyio.uiautomation.control;

import com.gettyio.uiautomation.condition.SearchCondition;
import com.gettyio.uiautomation.enums.ControlType;
import com.gettyio.uiautomation.pattern.ValuePattern;

/**
 * 编辑框控件
 */
public class EditControl extends Control {

    public EditControl() {
        super();
    }

    public EditControl(SearchCondition searchCondition) {
        super(searchCondition);
    }

    @Override
    public ControlType getControlType() {
        return ControlType.Edit;
    }

    /**
     * 获取编辑框的值
     */
    public String getValue() {
        return getValuePattern().getValue();
    }

    /**
     * 设置编辑框的值
     */
    public void setValue(String value) {
        getValuePattern().setValue(value);
    }
}
