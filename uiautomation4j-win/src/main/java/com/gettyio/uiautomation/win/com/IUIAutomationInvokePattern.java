package com.gettyio.uiautomation.win.com;

import com.sun.jna.Pointer;

/**
 * IUIAutomationInvokePattern COM 接口封装
 * IID_IUIAutomationInvokePattern: {fb377fbe-8ea6-46d5-9c73-6499642d3059}
 *
 * IUIAutomationInvokePattern vtable (after IUnknown 0-2):
 *   3: Invoke
 */
public class IUIAutomationInvokePattern extends COMObject {

    public static final String IID = "{fb377fbe-8ea6-46d5-9c73-6499642d3059}";
    public static final int PATTERN_ID = 10000; // UIA_InvokePatternId

    public IUIAutomationInvokePattern(Pointer pointer) {
        super(pointer);
    }

    /**
     * 调用操作
     * vtable index 3: Invoke
     */
    public void invoke() {
        invokeVtable(3);
    }
}
