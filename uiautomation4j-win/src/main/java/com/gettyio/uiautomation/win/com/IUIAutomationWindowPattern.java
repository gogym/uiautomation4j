package com.gettyio.uiautomation.win.com;

import com.sun.jna.Pointer;

/**
 * IUIAutomationWindowPattern COM 接口封装
 * IID_IUIAutomationWindowPattern: {0faef453-9208-46ef-b328-22af1da5e3a6}
 *
 * IUIAutomationWindowPattern vtable (after IUnknown 0-2):
 *   3: Close
 *   4: get_CurrentCanMaximize
 *   5: get_CurrentCanMinimize
 *   6: get_CurrentIsModal
 *   7: get_CurrentIsTopmost
 *   8: get_CurrentWindowVisualState
 *   9: SetWindowVisualState(state)
 *   10: WaitForInputIdle(milliseconds, pSuccess)
 */
public class IUIAutomationWindowPattern extends COMObject {

    public static final String IID = "{0faef453-9208-46ef-b328-22af1da5e3a6}";
    public static final int PATTERN_ID = 10009; // UIA_WindowPatternId

    // WindowVisualState
    public static final int WindowVisualState_Normal = 0;
    public static final int WindowVisualState_Maximized = 1;
    public static final int WindowVisualState_Minimized = 2;

    public IUIAutomationWindowPattern(Pointer pointer) {
        super(pointer);
    }

    /**
     * 关闭窗口
     * vtable index 3: Close
     */
    public void close() {
        invokeVtable(3);
    }

    /**
     * 是否可以最大化
     * vtable index 4
     */
    public boolean canMaximize() {
        int[] pVal = new int[1];
        invokeVtable(4, new Object[]{pVal});
        return pVal[0] != 0;
    }

    /**
     * 是否可以最小化
     * vtable index 5
     */
    public boolean canMinimize() {
        int[] pVal = new int[1];
        invokeVtable(5, new Object[]{pVal});
        return pVal[0] != 0;
    }

    /**
     * 是否置顶
     * vtable index 7
     */
    public boolean isTopmost() {
        int[] pVal = new int[1];
        invokeVtable(7, new Object[]{pVal});
        return pVal[0] != 0;
    }

    /**
     * 获取窗口视觉状态
     * vtable index 8
     */
    public int getVisualState() {
        int[] pVal = new int[1];
        invokeVtable(8, new Object[]{pVal});
        return pVal[0];
    }

    /**
     * 设置窗口视觉状态
     * vtable index 9: SetWindowVisualState(state)
     */
    public void setWindowVisualState(int state) {
        invokeVtable(9, new Object[]{state});
    }
}
