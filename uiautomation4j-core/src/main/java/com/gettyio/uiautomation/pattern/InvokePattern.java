package com.gettyio.uiautomation.pattern;

/**
 * InvokePattern - 用于调用控件的操作
 * 支持控件：Button、MenuItem、Hyperlink 等
 */
public interface InvokePattern extends Pattern {

    /**
     * 调用控件的操作（如点击按钮）
     */
    void invoke();
}
