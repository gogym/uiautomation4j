package io.getbit.uiautomation.win.com;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * IUIAutomationValuePattern COM 接口封装
 *
 * <p>对应 Windows SDK 中的 IUIAutomationValuePattern 接口，用于获取/设置控件的值。
 * 参考文档: https://learn.microsoft.com/en-us/windows/win32/api/uiautomationclient/nn-uiautomationclient-iuiautomationvaluepattern</p>
 *
 * <pre>
 * IID_IUIAutomationValuePattern: {a94cd8b1-0844-4cd6-9d2d-640537ab39e9}
 * Pattern ID: 10002 (UIA_ValuePatternId)
 *
 * Vtable 布局（继承自 IUnknown）:
 *   [0] QueryInterface
 *   [1] AddRef
 *   [2] Release
 *   [3] SetValue(BSTR szValue) -> HRESULT
 *   [4] get_CurrentValue(BSTR* pValue) -> HRESULT
 *   [5] get_CurrentIsReadOnly(BOOL* pIsReadOnly) -> HRESULT
 * </pre>
 */
public class IUIAutomationValuePattern extends COMObject {

    public static final String IID = "{a94cd8b1-0844-4cd6-9d2d-640537ab39e9}";
    /** Pattern ID: UIA_ValuePatternId = 10002 */
    public static final int PATTERN_ID = 10002;

    public IUIAutomationValuePattern(Pointer pointer) {
        super(pointer);
    }

    /**
     * 设置值
     * <pre>
     * COM 签名: HRESULT SetValue(BSTR szValue);
     * vtable index: 3
     * </pre>
     *
     * @param value 要设置的值
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
     * <pre>
     * COM 签名: HRESULT get_CurrentValue(BSTR* pValue);
     * vtable index: 4
     * </pre>
     *
     * @return 当前值，可能为 null
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
     * <pre>
     * COM 签名: HRESULT get_CurrentIsReadOnly(BOOL* pIsReadOnly);
     * vtable index: 5
     * </pre>
     *
     * @return true 表示值只读
     */
    public boolean isReadOnly() {
        int[] pIsReadOnly = new int[1];
        invokeVtable(5, new Object[]{pIsReadOnly});
        return pIsReadOnly[0] != 0;
    }
}
