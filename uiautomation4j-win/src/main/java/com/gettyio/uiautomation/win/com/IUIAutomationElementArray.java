package com.gettyio.uiautomation.win.com;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * IUIAutomationElementArray COM 接口封装
 *
 * <p>对应 Windows SDK 中的 IUIAutomationElementArray 接口，表示元素数组。
 * 参考文档: https://learn.microsoft.com/en-us/windows/win32/api/uiautomationclient/nn-uiautomationclient-iuiautomationelementarray</p>
 *
 * <pre>
 * IID_IUIAutomationElementArray: {14314595-b4bc-405e-9883-717d98f27670}
 *
 * Vtable 布局（继承自 IUnknown）:
 *   [0] QueryInterface
 *   [1] AddRef
 *   [2] Release
 *   [3] get_Length(int* pLength) -> HRESULT
 *   [4] GetElement(int index, IUIAutomationElement** ppElement) -> HRESULT
 * </pre>
 *
 * <p>通过 {@link IUIAutomationElement#findAll(int, IUIAutomationCondition)} 获取。</p>
 */
public class IUIAutomationElementArray extends COMObject {

    public static final String IID = "{14314595-b4bc-405e-9883-717d98f27670}";

    public IUIAutomationElementArray(Pointer pointer) {
        super(pointer);
    }

    /**
     * 获取数组长度
     * <pre>
     * COM 签名: HRESULT get_Length(int* pLength);
     * vtable index: 3
     * </pre>
     *
     * @return 数组中元素的数量
     */
    public int getLength() {
        int[] pLength = new int[1];
        invokeVtable(3, new Object[]{pLength});
        return pLength[0];
    }

    /**
     * 获取指定索引的元素
     * <pre>
     * COM 签名: HRESULT GetElement(int index, IUIAutomationElement** ppElement);
     * vtable index: 4
     * </pre>
     *
     * @param index 元素索引（从 0 开始）
     * @return 指定索引的元素，如果索引无效返回 null
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
