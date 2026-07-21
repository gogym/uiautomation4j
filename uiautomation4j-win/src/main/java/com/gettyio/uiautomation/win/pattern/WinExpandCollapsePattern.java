package com.gettyio.uiautomation.win.pattern;

import com.gettyio.uiautomation.pattern.ExpandCollapsePattern;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinNT;
import com.gettyio.uiautomation.win.com.COMObject;

/**
 * ExpandCollapsePattern 的 Windows 实现
 *
 * <p>将 core 模块的 {@link ExpandCollapsePattern} 接口桥接到 Windows COM 层。
 * 使用内部类 {@code ExpandCollapseComObject} 继承 {@link COMObject} 以访问 protected 的
 * {@code invokeVtable} 方法。</p>
 *
 * <pre>
 * IID_IUIAutomationExpandCollapsePattern: {61927d36-3bea-4a29-8bd5-cc18ac6c0f0e}
 * Pattern ID: 10005 (UIA_ExpandCollapsePatternId)
 *
 * Vtable:
 *   [3] Expand()
 *   [4] Collapse()
 *   [5] get_CurrentExpandCollapseState(ExpandCollapseState* pState)
 * </pre>
 */
public class WinExpandCollapsePattern implements ExpandCollapsePattern {

    /** IID_IUIAutomationExpandCollapsePattern */
    private static final String IID = "{61927d36-3bea-4a29-8bd5-cc18ac6c0f0e}";
    /** Pattern ID: UIA_ExpandCollapsePatternId = 10005 */
    private static final int PATTERN_ID = 10005;

    /** 底层 COM ExpandCollapsePattern 对象（通过内部类包装） */
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

    /**
     * 内部 COM 包装类
     * <p>继承 {@link COMObject} 以访问 protected 的 {@code invokeVtable} 方法。</p>
     */
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
