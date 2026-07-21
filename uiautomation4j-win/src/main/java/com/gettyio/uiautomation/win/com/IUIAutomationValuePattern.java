package com.gettyio.uiautomation.win.com;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * IUIAutomationValuePattern COM 接口封装
 * IID_IUIAutomationValuePattern: {a94cd8b1-0844-4cd6-9d2d-640537ab39e9}
 *
 * IUIAutomationValuePattern vtable (after IUnknown 0-2):
 *   3: SetValue(szValue)
 *   4: get_CurrentValue(pValue)
 *   5: get_CurrentIsReadOnly(pIsReadOnly)
 */
public class IUIAutomationValuePattern extends COMObject {

    public static final String IID = "{a94cd8b1-0844-4cd6-9d2d-640537ab39e9}";
    public static final int PATTERN_ID = 10002; // UIA_ValuePatternId

    public IUIAutomationValuePattern(Pointer pointer) {
        super(pointer);
    }

    /**
     * 设置值
     * vtable index 3: SetValue(szValue)
     */
    public void setValue(String value) {
        Pointer bstr = Win32Util.stringToBstr(value);
        try {
            invokeVtable(3, new Object[]{bstr});
        } finally {
            Win32Util.freeBstr(bstr);
        }
    }

    /**
     * 获取当前值
     * vtable index 4: get_CurrentValue(pValue)
     */
    public String getCurrentValue() {
        PointerByReference ppValue = new PointerByReference();
        invokeVtable(4, new Object[]{ppValue});
        Pointer bstr = ppValue.getValue();
        if (bstr == null || bstr == Pointer.NULL) {
            return null;
        }
        String value = Win32Util.bstrToString(bstr);
        Win32Util.freeBstr(bstr);
        return value;
    }

    /**
     * 是否只读
     * vtable index 5: get_CurrentIsReadOnly(pIsReadOnly)
     */
    public boolean isReadOnly() {
        int[] pIsReadOnly = new int[1];
        invokeVtable(5, new Object[]{pIsReadOnly});
        return pIsReadOnly[0] != 0;
    }
}
