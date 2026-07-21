package com.gettyio.uiautomation.win.com;

import com.sun.jna.Pointer;

/**
 * IUIAutomationCondition COM 接口封装
 *
 * <p>对应 Windows SDK 中的 IUIAutomationCondition 接口。
 * 参考文档: https://learn.microsoft.com/en-us/windows/win32/api/uiautomationclient/nn-uiautomationclient-iuiautomationcondition</p>
 *
 * <pre>
 * IID_IUIAutomationCondition: {352ffba8-0973-437c-a61f-f64cafd81df9}
 *
 * IUIAutomationCondition 继承自 IUnknown，没有额外方法。
 * 仅作为条件对象的类型标记，实际使用的子类包括：
 *   - IUIAutomationPropertyCondition（属性条件）
 *   - IUIAutomationAndCondition（与条件）
 *   - IUIAutomationOrCondition（或条件）
 *   - IUIAutomationNotCondition（非条件）
 *   - IUIAutomationTrueCondition（匹配所有元素）
 *   - IUIAutomationFalseCondition（不匹配任何元素）
 * </pre>
 */
public class IUIAutomationCondition extends COMObject {

    public static final String IID = "{352ffba8-0973-437c-a61f-f64cafd81df9}";

    public IUIAutomationCondition(Pointer pointer) {
        super(pointer);
    }
}
