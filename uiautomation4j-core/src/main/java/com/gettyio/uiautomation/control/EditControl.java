package com.gettyio.uiautomation.control;

import com.gettyio.uiautomation.condition.SearchCondition;
import com.gettyio.uiautomation.enums.ControlType;
import com.gettyio.uiautomation.pattern.ValuePattern;

/**
 * 编辑框控件
 *
 * <p>对应 UIAutomation 的 Edit 控件类型 (ControlType=50004)。
 * 支持 {@link ValuePattern}，可通过 {@code getValue()}/{@code setValue()} 获取/设置文本内容。</p>
 *
 * <p>使用示例：</p>
 * <pre>
 * EditControl edit = Control.edit().automationId("txtInput").findEdit();
 * edit.sendKeys("Hello World");
 * // 或通过 ValuePattern
 * edit.setValue("Hello");
 * String text = edit.getValue();
 * </pre>
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
     * <p>通过 {@link ValuePattern#getValue()} 实现，
     * 对应 COM 方法: {@code IUIAutomationValuePattern::get_CurrentValue}</p>
     *
     * @return 编辑框当前文本内容
     */
    public String getValue() {
        return getValuePattern().getValue();
    }

    /**
     * 设置编辑框的值
     * <p>通过 {@link ValuePattern#setValue(String)} 实现，
     * 对应 COM 方法: {@code IUIAutomationValuePattern::SetValue}</p>
     *
     * @param value 要设置的文本内容
     */
    public void setValue(String value) {
        getValuePattern().setValue(value);
    }
}
