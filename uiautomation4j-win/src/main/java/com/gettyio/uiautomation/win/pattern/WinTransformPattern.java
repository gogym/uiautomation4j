package com.gettyio.uiautomation.win.pattern;

import com.gettyio.uiautomation.pattern.TransformPattern;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinNT;
import com.gettyio.uiautomation.win.com.COMObject;

/**
 * TransformPattern 的 Windows 实现
 * IID_IUIAutomationTransformPattern: {a9b55844-a55d-4ef0-926d-569c16ff89bb}
 *
 * vtable (after IUnknown 0-2):
 *   3: Move(x, y)
 *   4: Resize(width, height)
 *   5: Rotate(degrees)
 *   6: get_CurrentCanMove
 *   7: get_CurrentCanResize
 *   8: get_CurrentCanRotate
 */
public class WinTransformPattern implements TransformPattern {

    private static final String IID = "{a9b55844-a55d-4ef0-926d-569c16ff89bb}";
    private static final int PATTERN_ID = 10016; // UIA_TransformPatternId

    private final TransformComObject comPattern;

    public WinTransformPattern(Pointer patternPointer) {
        this.comPattern = new TransformComObject(patternPointer);
    }

    @Override
    public void move(int x, int y) {
        comPattern.callVtable(3, new Object[]{x, y});
    }

    @Override
    public void resize(int width, int height) {
        comPattern.callVtable(4, new Object[]{width, height});
    }

    @Override
    public void rotate(double degrees) {
        comPattern.callVtable(5, new Object[]{degrees});
    }

    @Override
    public boolean canMove() {
        int[] pVal = new int[1];
        comPattern.callVtable(6, new Object[]{pVal});
        return pVal[0] != 0;
    }

    @Override
    public boolean canResize() {
        int[] pVal = new int[1];
        comPattern.callVtable(7, new Object[]{pVal});
        return pVal[0] != 0;
    }

    @Override
    public boolean canRotate() {
        int[] pVal = new int[1];
        comPattern.callVtable(8, new Object[]{pVal});
        return pVal[0] != 0;
    }

    private static class TransformComObject extends COMObject {
        TransformComObject(Pointer pointer) {
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
