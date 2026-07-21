package com.gettyio.uiautomation.pattern;

/**
 * ValuePattern - 用于获取或设置控件的值
 *
 * <p>对应 UIAutomation 的 {@code IUIAutomationValuePattern} (PatternId: 10002)。
 * 参考文档: https://learn.microsoft.com/en-us/windows/win32/api/uiautomationclient/nn-uiautomationclient-iuiautomationvaluepattern</p>
 *
 * <p>支持控件：Edit、Text、ProgressBar、Slider 等</p>
 *
 * <p>使用示例：</p>
 * <pre>
 * // 获取编辑框的值
 * String value = editControl.getValuePattern().getValue();
 * // 设置编辑框的值
 * editControl.getValuePattern().setValue("Hello");
 * </pre>
 */
public interface ValuePattern extends Pattern {

    /**
     * 获取控件的值
     * <p>对应 COM 方法: {@code IUIAutomationValuePattern::get_CurrentValue(BSTR* pValue)}</p>
     *
     * @return 控件当前值
     */
    String getValue();

    /**
     * 设置控件的值
     * <p>对应 COM 方法: {@code IUIAutomationValuePattern::SetValue(BSTR szValue)}</p>
     *
     * @param value 要设置的值
     */
    void setValue(String value);

    /**
     * 控件值是否只读
     * <p>对应 COM 方法: {@code IUIAutomationValuePattern::get_CurrentIsReadOnly(BOOL* pIsReadOnly)}</p>
     *
     * @return true 表示值只读，不可修改
     */
    boolean isReadOnly();
}
