package io.getbit.uiautomation.win.pattern;

import io.getbit.uiautomation.pattern.TransformPattern;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinNT;
import io.getbit.uiautomation.win.com.COMObject;

/**
 * TransformPattern 的 Windows 实现
 *
 * <p>将 core 模块的 {@link TransformPattern} 接口桥接到 Windows COM 层。
 * 使用内部类 {@code TransformComObject} 继承 {@link COMObject} 以访问 protected 的
 * {@code invokeVtable} 方法。</p>
 *
 * <pre>
 * IID_IUIAutomationTransformPattern: {a9b55844-a55d-4ef0-926d-569c16ff89bb}
 * Pattern ID: 10016 (UIA_TransformPatternId)
 *
 * Vtable:
 *   [3] Move(double x, double y)
 *   [4] Resize(double width, double height)
 *   [5] Rotate(double degrees)
 *   [6] get_CurrentCanMove(BOOL* pCanMove)
 *   [7] get_CurrentCanResize(BOOL* pCanResize)
 *   [8] get_CurrentCanRotate(BOOL* pCanRotate)
 * </pre>
 */
public class WinTransformPattern implements TransformPattern {

    /** IID_IUIAutomationTransformPattern */
    private static final String IID = "{a9b55844-a55d-4ef0-926d-569c16ff89bb}";
    /** Pattern ID: UIA_TransformPatternId = 10016 */
    private static final int PATTERN_ID = 10016;

    /** 底层 COM TransformPattern 对象（通过内部类包装） */
    private final TransformComObject comPattern;

    public WinTransformPattern(Pointer patternPointer) {
        this.comPattern = new TransformComObject(patternPointer);
    }

    @Override
    public void move(int x, int y) {
        comPattern.callVtable(3, new Object[]{(double) x, (double) y});
    }

    @Override
    public void resize(int width, int height) {
        comPattern.callVtable(4, new Object[]{(double) width, (double) height});
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

    /**
     * 内部 COM 包装类
     * <p>继承 {@link COMObject} 以访问 protected 的 {@code invokeVtable} 方法。</p>
     */
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
