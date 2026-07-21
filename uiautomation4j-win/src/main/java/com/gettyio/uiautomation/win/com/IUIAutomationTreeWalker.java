package com.gettyio.uiautomation.win.com;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * IUIAutomationTreeWalker COM 接口封装
 * IID_IUIAutomationTreeWalker: {4042c624-389c-4afc-a630-9df854a541fc}
 *
 * IUIAutomationTreeWalker vtable (after IUnknown 0-2):
 *   3: GetRootElementElement
 *   4: GetParentElement
 *   5: GetFirstChildElement
 *   6: GetLastChildElement
 *   7: GetNextSiblingElement
 *   8: GetPreviousSiblingElement
 *   ...
 */
public class IUIAutomationTreeWalker extends COMObject {

    public static final String IID = "{4042c624-389c-4afc-a630-9df854a541fc}";

    public IUIAutomationTreeWalker(Pointer pointer) {
        super(pointer);
    }

    /**
     * 获取第一个子元素
     * vtable index 5: GetFirstChildElement(pElement, ppFirst)
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
     * vtable index 7: GetNextSiblingElement(pElement, ppNext)
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
     * vtable index 4: GetParentElement(pElement, ppParent)
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
