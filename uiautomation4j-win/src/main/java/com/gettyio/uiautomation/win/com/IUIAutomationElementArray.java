package com.gettyio.uiautomation.win.com;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * IUIAutomationElementArray COM 接口封装
 * IID_IUIAutomationElementArray: {14314595-b4bc-405e-9883-717d98f27670}
 *
 * IUIAutomationElementArray vtable (after IUnknown 0-2):
 *   3: get_Length(pLength)
 *   4: GetElement(index, ppElement)
 */
public class IUIAutomationElementArray extends COMObject {

    public static final String IID = "{14314595-b4bc-405e-9883-717d98f27670}";

    public IUIAutomationElementArray(Pointer pointer) {
        super(pointer);
    }

    /**
     * 获取数组长度
     * vtable index 3: get_Length(pLength)
     */
    public int getLength() {
        int[] pLength = new int[1];
        invokeVtable(3, new Object[]{pLength});
        return pLength[0];
    }

    /**
     * 获取指定索引的元素
     * vtable index 4: GetElement(index, ppElement)
     */
    public IUIAutomationElement getElement(int index) {
        PointerByReference ppElement = new PointerByReference();
        invokeVtable(4, new Object[]{index, ppElement});
        Pointer elementPtr = ppElement.getValue();
        if (elementPtr == null || elementPtr == Pointer.NULL) {
            return null;
        }
        return new IUIAutomationElement(elementPtr);
    }
}
