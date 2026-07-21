package com.gettyio.uiautomation.win.pattern;

import com.gettyio.uiautomation.pattern.ScrollPattern;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinNT;
import com.gettyio.uiautomation.win.com.COMObject;

/**
 * ScrollPattern 的 Windows 实现
 * IID_IUIAutomationScrollPattern: {b4b191ce-3975-4e7d-a50d-13b1004d8c80}
 *
 * vtable (after IUnknown 0-2):
 *   3: Scroll(horizontalAmount, verticalAmount)
 *   4: SetScrollPercent(horizontalPercent, verticalPercent)
 *   5: get_CurrentHorizontalScrollPercent
 *   6: get_CurrentVerticalScrollPercent
 *   7: get_CurrentHorizontalViewSize
 *   8: get_CurrentVerticalViewSize
 *   9: get_CurrentHorizontallyScrollable
 *   10: get_CurrentVerticallyScrollable
 */
public class WinScrollPattern implements ScrollPattern {

    private static final String IID = "{b4b191ce-3975-4e7d-a50d-13b1004d8c80}";
    private static final int PATTERN_ID = 10004; // UIA_ScrollPatternId

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
