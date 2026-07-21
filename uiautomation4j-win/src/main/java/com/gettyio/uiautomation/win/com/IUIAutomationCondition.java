package com.gettyio.uiautomation.win.com;

import com.sun.jna.Pointer;

/**
 * IUIAutomationCondition COM 接口封装
 * IID_IUIAutomationCondition: {352ffba8-0973-437c-a61f-f64cafd81df9}
 *
 * IUIAutomationCondition 继承自 IUnknown，没有额外方法
 * 仅作为条件对象的类型标记
 */
public class IUIAutomationCondition extends COMObject {

    public static final String IID = "{352ffba8-0973-437c-a61f-f64cafd81df9}";

    public IUIAutomationCondition(Pointer pointer) {
        super(pointer);
    }
}
