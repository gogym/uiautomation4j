package io.getbit.uiautomation.control;

import io.getbit.uiautomation.condition.SearchCondition;
import io.getbit.uiautomation.enums.ControlType;

/**
 * 文本控件
 *
 * <p>对应 UIAutomation 的 Text 控件类型 (ControlType=50024)。
 * 通常用于显示只读文本标签，支持 {@link ValuePattern} 获取文本内容。</p>
 */
public class TextControl extends Control {

    public TextControl() {
        super();
    }

    public TextControl(SearchCondition searchCondition) {
        super(searchCondition);
    }

}
