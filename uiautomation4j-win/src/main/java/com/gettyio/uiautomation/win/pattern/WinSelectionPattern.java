package com.gettyio.uiautomation.win.pattern;

import com.gettyio.uiautomation.pattern.SelectionPattern;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import com.gettyio.uiautomation.win.com.COMObject;
import com.gettyio.uiautomation.win.com.IUIAutomationElement;
import com.gettyio.uiautomation.win.com.IUIAutomationElementArray;

import java.util.ArrayList;
import java.util.List;

/**
 * SelectionPattern 的 Windows 实现
 */
public class WinSelectionPattern implements SelectionPattern {

    private final SelectionComObject comPattern;

    public WinSelectionPattern(Pointer patternPointer) {
        this.comPattern = new SelectionComObject(patternPointer);
    }

    @Override
    public List<Object> getSelection() {
        PointerByReference ppArray = new PointerByReference();
        comPattern.callVtable(3, new Object[]{ppArray});
        Pointer arrayPtr = ppArray.getValue();
        List<Object> result = new ArrayList<>();
        if (arrayPtr != null && arrayPtr != Pointer.NULL) {
            IUIAutomationElementArray array = new IUIAutomationElementArray(arrayPtr);
            int len = array.getLength();
            for (int i = 0; i < len; i++) {
                result.add(array.getElement(i));
            }
        }
        return result;
    }

    @Override
    public boolean isMultiSelect() {
        int[] pVal = new int[1];
        comPattern.callVtable(4, new Object[]{pVal});
        return pVal[0] != 0;
    }

    private static class SelectionComObject extends COMObject {
        SelectionComObject(Pointer pointer) {
            super(pointer);
        }

        public WinNT.HRESULT callVtable(int idx, Object[] args) {
            return invokeVtable(idx, args);
        }
    }
}
