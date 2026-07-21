package com.gettyio.uiautomation.win.com;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * IUIAutomationTreeWalker COM 接口封装
 *
 * <p>对应 Windows SDK 中的 IUIAutomationTreeWalker 接口，用于遍历 UI 自动化元素树。
 * 参考文档: https://learn.microsoft.com/en-us/windows/win32/api/uiautomationclient/nn-uiautomationclient-iuiautomationtreewalker</p>
 *
 * <pre>
 * IID_IUIAutomationTreeWalker: {4042c624-389c-4afc-a630-9df854a541fc}
 *
 * Vtable 布局（继承自 IUnknown）:
 *   [0] QueryInterface
 *   [1] AddRef
 *   [2] Release
 *   [3] GetRootElementElement(IUIAutomationElement** ppRoot) -> HRESULT
 *   [4] GetParentElement(IUIAutomationElement* pElement, IUIAutomationElement** ppParent) -> HRESULT
 *   [5] GetFirstChildElement(IUIAutomationElement* pElement, IUIAutomationElement** ppFirst) -> HRESULT
 *   [6] GetLastChildElement(IUIAutomationElement* pElement, IUIAutomationElement** ppLast) -> HRESULT
 *   [7] GetNextSiblingElement(IUIAutomationElement* pElement, IUIAutomationElement** ppNext) -> HRESULT
 *   [8] GetPreviousSiblingElement(IUIAutomationElement* pElement, IUIAutomationElement** ppPrevious) -> HRESULT
 * </pre>
 *
 * <p>通过 {@link IUIAutomation#createTreeWalker(IUIAutomationCondition)} 创建。</p>
 */
public class IUIAutomationTreeWalker extends COMObject {

    public static final String IID = "{4042c624-389c-4afc-a630-9df854a541fc}";

    public IUIAutomationTreeWalker(Pointer pointer) {
        super(pointer);
    }

    /**
     * 获取第一个子元素
     * <pre>
     * COM 签名: HRESULT GetFirstChildElement(
     *     [in] IUIAutomationElement* pElement,
     *     [out, retval] IUIAutomationElement** ppFirst
     * );
     * vtable index: 5
     * </pre>
     *
     * @param parent 父元素
     * @return 第一个子元素，未找到返回 null
     */
    public IUIAutomationElement getFirstChildElement(IUIAutomationElement parent) {
        PointerByReference ppFirst = new PointerByReference();
        invokeVtable(5, new Object[]{parent.getPointer(), ppFirst});
        Pointer firstPtr = ppFirst.getValue();
        if (firstPtr == null || firstPtr == Pointer.NULL) {
            return null;
        }
        return new IUIAutomationElement(firstPtr);
    }

    /**
     * 获取下一个兄弟元素
     * <pre>
     * COM 签名: HRESULT GetNextSiblingElement(
     *     [in] IUIAutomationElement* pElement,
     *     [out, retval] IUIAutomationElement** ppNext
     * );
     * vtable index: 7
     * </pre>
     *
     * @param element 当前元素
     * @return 下一个兄弟元素，未找到返回 null
     */
    public IUIAutomationElement getNextSiblingElement(IUIAutomationElement element) {
        PointerByReference ppNext = new PointerByReference();
        invokeVtable(7, new Object[]{element.getPointer(), ppNext});
        Pointer nextPtr = ppNext.getValue();
        if (nextPtr == null || nextPtr == Pointer.NULL) {
            return null;
        }
        return new IUIAutomationElement(nextPtr);
    }

    /**
     * 获取父元素
     * <pre>
     * COM 签名: HRESULT GetParentElement(
     *     [in] IUIAutomationElement* pElement,
     *     [out, retval] IUIAutomationElement** ppParent
     * );
     * vtable index: 4
     * </pre>
     *
     * @param element 当前元素
     * @return 父元素，未找到返回 null
     */
    public IUIAutomationElement getParentElement(IUIAutomationElement element) {
        PointerByReference ppParent = new PointerByReference();
        invokeVtable(4, new Object[]{element.getPointer(), ppParent});
        Pointer parentPtr = ppParent.getValue();
        if (parentPtr == null || parentPtr == Pointer.NULL) {
            return null;
        }
        return new IUIAutomationElement(parentPtr);
    }
}
