package io.getbit.uiautomation.pattern;

/**
 * InvokePattern - 用于调用控件的操作
 *
 * <p>对应 UIAutomation 的 {@code IUIAutomationInvokePattern} (PatternId: 10000)。
 * 参考文档: https://learn.microsoft.com/en-us/windows/win32/api/uiautomationclient/nn-uiautomationclient-iuiautomationinvokepattern</p>
 *
 * <p>支持控件：Button、MenuItem、Hyperlink 等</p>
 *
 * <p>Invoke 操作等价于用户点击该控件，与 {@code Control.click()} 的区别是：
 * Invoke 通过 COM 接口直接触发控件的默认操作，而 click 通过鼠标模拟点击。</p>
 */
public interface InvokePattern extends Pattern {

    /**
     * 调用控件的操作（如点击按钮）
     * <p>对应 COM 方法: {@code IUIAutomationInvokePattern::Invoke()}</p>
     * <p>此方法会触发控件的默认操作，等价于用户点击该控件。</p>
     */
    void invoke();
}
