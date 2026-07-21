package io.getbit.uiautomation.win.pattern;

import io.getbit.uiautomation.pattern.ScrollPattern;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinNT;
import io.getbit.uiautomation.win.com.COMObject;

/**
 * ScrollPattern 的 Windows 实现
 *
 * <p>将 core 模块的 {@link ScrollPattern} 接口桥接到 Windows COM 层。
 * 使用内部类 {@code ScrollComObject} 继承 {@link COMObject} 以访问 protected 的
 * {@code invokeVtable} 方法。</p>
 *
 * <pre>
 * IID_IUIAutomationScrollPattern: {b4b191ce-3975-4e7d-a50d-13b1004d8c80}
 * Pattern ID: 10004 (UIA_ScrollPatternId)
 *
 * Vtable:
 *   [3] Scroll(ScrollAmount horizontalAmount, ScrollAmount verticalAmount)
 *   [4] SetScrollPercent(double horizontalPercent, double verticalPercent)
 *   [5] get_CurrentHorizontalScrollPercent(double* pPercent)
 *   [6] get_CurrentVerticalScrollPercent(double* pPercent)
 *   [7] get_CurrentHorizontalViewSize(double* pSize)
 *   [8] get_CurrentVerticalViewSize(double* pSize)
 *   [9] get_CurrentHorizontallyScrollable(BOOL* pScrollable)
 *   [10] get_CurrentVerticallyScrollable(BOOL* pScrollable)
 * </pre>
 */
public class WinScrollPattern implements ScrollPattern {

    /** IID_IUIAutomationScrollPattern */
    private static final String IID = "{b4b191ce-3975-4e7d-a50d-13b1004d8c80}";
    /** Pattern ID: UIA_ScrollPatternId = 10004 */
    private static final int PATTERN_ID = 10004;

    /** 底层 COM ScrollPattern 对象（通过内部类包装） */
    private final ScrollComObject comPattern;

    public WinScrollPattern(Pointer patternPointer) {
        this.comPattern = new ScrollComObject(patternPointer);
    }

    @Override
    public void scroll(int horizontalDirection, int verticalDirection,
                       double horizontalAmount, double verticalAmount) {
        // ScrollAmount enum mapping
        comPattern.callVtable(3, new Object[]{horizontalDirection, verticalDirection});
    }

    @Override
    public void setScrollPercent(double horizontalPercent, double verticalPercent) {
        comPattern.callVtable(4, new Object[]{horizontalPercent, verticalPercent});
    }

    @Override
    public double getHorizontalScrollPercent() {
        double[] pVal = new double[1];
        comPattern.callVtable(5, new Object[]{pVal});
        return pVal[0];
    }

    @Override
    public double getVerticalScrollPercent() {
        double[] pVal = new double[1];
        comPattern.callVtable(6, new Object[]{pVal});
        return pVal[0];
    }

    @Override
    public boolean isHorizontallyScrollable() {
        int[] pVal = new int[1];
        comPattern.callVtable(9, new Object[]{pVal});
        return pVal[0] != 0;
    }

    @Override
    public boolean isVerticallyScrollable() {
        int[] pVal = new int[1];
        comPattern.callVtable(10, new Object[]{pVal});
        return pVal[0] != 0;
    }

    /**
     * 内部 COM 包装类
     * <p>继承 {@link COMObject} 以访问 protected 的 {@code invokeVtable} 方法。
     * 这是解决跨包访问 COM vtable 的设计模式。</p>
     */
    private static class ScrollComObject extends COMObject {
        ScrollComObject(Pointer pointer) {
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
