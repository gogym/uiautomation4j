package io.getbit.uiautomation.win.com;

import com.sun.jna.Pointer;

/**
 * IUIAutomationInvokePattern COM 接口封装
 *
 * <p>对应 Windows SDK 中的 IUIAutomationInvokePattern 接口，用于触发控件的默认操作。
 * 参考文档: https://learn.microsoft.com/en-us/windows/win32/api/uiautomationclient/nn-uiautomationclient-iuiautomationinvokepattern</p>
 *
 * <pre>
 * IID_IUIAutomationInvokePattern: {fb377fbe-8ea6-46d5-9c73-6499642d3059}
 * Pattern ID: 10000 (UIA_InvokePatternId)
 *
 * Vtable 布局（继承自 IUnknown）:
 *   [0] QueryInterface
 *   [1] AddRef
 *   [2] Release
 *   [3] Invoke() -> HRESULT
 * </pre>
 */
public class IUIAutomationInvokePattern extends COMObject {

    public static final String IID = "{fb377fbe-8ea6-46d5-9c73-6499642d3059}";
    /** Pattern ID: UIA_InvokePatternId = 10000 */
    public static final int PATTERN_ID = 10000;

    public IUIAutomationInvokePattern(Pointer pointer) {
        super(pointer);
    }

    /**
     * 调用操作
     * <pre>
     * COM 签名: HRESULT Invoke();
     * vtable index: 3
     * </pre>
     * <p>触发控件的默认操作，等价于用户点击该控件。</p>
     */
    public void invoke() {
        invokeVtable(3);
    }
}
