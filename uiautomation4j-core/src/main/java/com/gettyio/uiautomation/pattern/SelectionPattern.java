package com.gettyio.uiautomation.pattern;

import java.util.List;

/**
 * SelectionPattern - 用于获取容器中的选中项
 *
 * <p>对应 UIAutomation 的 {@code IUIAutomationSelectionPattern} (PatternId: 10001)。
 * 参考文档: https://learn.microsoft.com/en-us/windows/win32/api/uiautomationclient/nn-uiautomationclient-iuiautomationselectionpattern</p>
 *
 * <p>支持控件：List、ComboBox、Tab 等</p>
 */
public interface SelectionPattern extends Pattern {

    /**
     * 获取当前选中的控件列表
     * <p>对应 COM 方法: {@code IUIAutomationSelectionPattern::get_CurrentSelection(IUIAutomationElementArray** ppSelected)}</p>
     *
     * @return 当前选中的元素列表（每个元素为 COM 接口包装对象）
     */
    List<Object> getSelection();

    /**
     * 是否支持多选
     * <p>对应 COM 属性: {@code get_CurrentCanSelectMultiple(BOOL* pCanSelectMultiple)}</p>
     *
     * @return true 表示容器支持多选
     */
    boolean isMultiSelect();
}
