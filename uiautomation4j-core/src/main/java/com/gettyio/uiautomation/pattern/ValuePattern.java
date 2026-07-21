package com.gettyio.uiautomation.pattern;

/**
 * ValuePattern - 用于获取或设置控件的值
 * 支持控件：Edit、Text、ProgressBar、Slider 等
 */
public interface ValuePattern extends Pattern {

    /**
     * 获取控件的值
     */
    String getValue();

    /**
     * 设置控件的值
     */
    void setValue(String value);

    /**
     * 控件值是否只读
     */
    boolean isReadOnly();
}
