package io.getbit.uiautomation.control;

import io.getbit.uiautomation.condition.SearchCondition;
import io.getbit.uiautomation.enums.ControlType;

/**
 * 按钮控件
 *
 * <p>对应 UIAutomation 的 Button 控件类型 (ControlType=50000)。
 * 支持 {@link InvokePattern}，可通过 {@code getInvokePattern().invoke()} 触发按钮操作。</p>
 *
 * <p>使用示例：</p>
 * <pre>
 * ButtonControl okBtn = Control.button().name("确定").findButton();
 * okBtn.click();
 * // 或通过 InvokePattern 触发
 * okBtn.getInvokePattern().invoke();
 * </pre>
 */
public class ButtonControl extends Control {

    public ButtonControl() {
        super();
    }

    public ButtonControl(SearchCondition searchCondition) {
        super(searchCondition);
    }

}
