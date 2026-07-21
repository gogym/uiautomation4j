package com.gettyio.uiautomation.pattern;

import java.util.List;

/**
 * SelectionPattern - 用于获取容器中的选中项
 * 支持控件：List、ComboBox、Tab 等
 */
public interface SelectionPattern extends Pattern {

    /**
     * 获取当前选中的控件列表
     */
    List<Object> getSelection();

    /**
     * 是否支持多选
     */
    boolean isMultiSelect();
}
