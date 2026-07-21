package com.gettyio.uiautomation.win.pattern;

import com.gettyio.uiautomation.pattern.ExpandCollapsePattern;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinNT;
import com.gettyio.uiautomation.win.com.COMObject;

/**
 * ExpandCollapsePattern 的 Windows 实现
 * IID_IUIAutomationExpandCollapsePattern: {61927d36-3bea-4a29-8bd5-cc18ac6c0f0e}
 *
 * vtable (after IUnknown 0-2):
 *   3: Expand
 *   4: Collapse
 *   5: get_CurrentExpandCollapseState
 */
public class WinExpandCollapsePattern implements ExpandCollapsePattern {

    private static final String IID = "{61927d36-3bea-4a29-8bd5-cc18ac6c0f0e}";
    private static final int PATTERN_ID = 10005; // UIA_ExpandCollapsePatternId

    private final ExpandCollapseComObject comPattern;

    public WinExpandCollapsePattern(Pointer patternPointer) {
        this.comPattern = new ExpandCollapseComObject(patternPointer);
    }

    @Override
    public void expand() {
        comPattern.callVtable(3);
    }

    @Override
    public void collapse() {
        comPattern.callVtable(4);
    }

    @Override
    public int getExpandCollapseState() {
        int[] pVal = new int[1];
        comPattern.callVtable(5, new Object[]{pVal});
        return pVal[0];
    }

    private static class ExpandCollapseComObject extends COMObject {
        ExpandCollapseComObject(Pointer pointer) {
            super(pointer);
        }

        public WinNT.HRESULT callVtable(int idx, Object[] args) {
            return invokeVtable(idx, args);
        }

        public WinNT.HRESULT callVtable(int idx) {
            return invokeVtable(idx);
        }
    }
}
