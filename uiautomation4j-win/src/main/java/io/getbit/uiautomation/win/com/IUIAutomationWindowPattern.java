package io.getbit.uiautomation.win.com;

import com.sun.jna.Pointer;

/**
 * IUIAutomationWindowPattern COM 接口封装
 *
 * <p>对应 Windows SDK 中的 IUIAutomationWindowPattern 接口，用于窗口级操作。
 * 参考文档: https://learn.microsoft.com/en-us/windows/win32/api/uiautomationclient/nn-uiautomationclient-iuiautomationwindowpattern</p>
 *
 * <pre>
 * IID_IUIAutomationWindowPattern: {0faef453-9208-46ef-b328-22af1da5e3a6}
 * Pattern ID: 10009 (UIA_WindowPatternId)
 *
 * Vtable 布局（继承自 IUnknown）:
 *   [0] QueryInterface
 *   [1] AddRef
 *   [2] Release
 *   [3] Close() -> HRESULT
 *   [4] get_CurrentCanMaximize(BOOL* pVal) -> HRESULT
 *   [5] get_CurrentCanMinimize(BOOL* pVal) -> HRESULT
 *   [6] get_CurrentIsModal(BOOL* pVal) -> HRESULT
 *   [7] get_CurrentIsTopmost(BOOL* pVal) -> HRESULT
 *   [8] get_CurrentWindowVisualState(WindowVisualState* pVal) -> HRESULT
 *   [9] SetWindowVisualState(WindowVisualState state) -> HRESULT
 *   [10] WaitForInputIdle(int milliseconds, BOOL* pSuccess) -> HRESULT
 * </pre>
 */
public class IUIAutomationWindowPattern extends COMObject {

    public static final String IID = "{0faef453-9208-46ef-b328-22af1da5e3a6}";
    /** Pattern ID: UIA_WindowPatternId = 10009 */
    public static final int PATTERN_ID = 10009;

    /** 窗口视觉状态：正常 */
    public static final int WindowVisualState_Normal = 0;
    /** 窗口视觉状态：最大化 */
    public static final int WindowVisualState_Maximized = 1;
    /** 窗口视觉状态：最小化 */
    public static final int WindowVisualState_Minimized = 2;

    public IUIAutomationWindowPattern(Pointer pointer) {
        super(pointer);
    }

    /**
     * 关闭窗口
     * <pre>
     * COM 签名: HRESULT Close();
     * vtable index: 3
     * </pre>
     */
    public void close() {
        invokeVtable(3);
    }

    /**
     * 是否可以最大化
     * <pre>
     * COM 签名: HRESULT get_CurrentCanMaximize(BOOL* pVal);
     * vtable index: 4
     * </pre>
     *
     * @return true 表示窗口可以最大化
     */
    public boolean canMaximize() {
        int[] pVal = new int[1];
        invokeVtable(4, new Object[]{pVal});
        return pVal[0] != 0;
    }

    /**
     * 是否可以最小化
     * <pre>
     * COM 签名: HRESULT get_CurrentCanMinimize(BOOL* pVal);
     * vtable index: 5
     * </pre>
     *
     * @return true 表示窗口可以最小化
     */
    public boolean canMinimize() {
        int[] pVal = new int[1];
        invokeVtable(5, new Object[]{pVal});
        return pVal[0] != 0;
    }

    /**
     * 是否置顶
     * <pre>
     * COM 签名: HRESULT get_CurrentIsTopmost(BOOL* pVal);
     * vtable index: 7
     * </pre>
     *
     * @return true 表示窗口始终置顶
     */
    public boolean isTopmost() {
        int[] pVal = new int[1];
        invokeVtable(7, new Object[]{pVal});
        return pVal[0] != 0;
    }

    /**
     * 获取窗口视觉状态
     * <pre>
     * COM 签名: HRESULT get_CurrentWindowVisualState(WindowVisualState* pVal);
     * vtable index: 8
     * </pre>
     *
     * @return 视觉状态值 (0=Normal, 1=Maximized, 2=Minimized)
     */
    public int getVisualState() {
        int[] pVal = new int[1];
        invokeVtable(8, new Object[]{pVal});
        return pVal[0];
    }

    /**
     * 设置窗口视觉状态
     * <pre>
     * COM 签名: HRESULT SetWindowVisualState(WindowVisualState state);
     * vtable index: 9
     * </pre>
     *
     * @param state 视觉状态值 (0=Normal, 1=Maximized, 2=Minimized)
     */
    public void setWindowVisualState(int state) {
        invokeVtable(9, new Object[]{state});
    }
}
