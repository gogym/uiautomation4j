package com.gettyio.uiautomation.win.com;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.ptr.PointerByReference;

/**
 * IUIAutomation COM 接口封装
 * CLSID_CUIAutomation: {ff48dba4-60ef-4201-aa87-54103eef594e}
 * IID_IUIAutomation:   {30c25b32-5606-446a-b2ca-1be52e3c8a9f}
 *
 * IUIAutomation vtable (after IUnknown 0-2):
 *   3: CompareElements
 *   4: CompareRuntimeIds
 *   5: GetRootElement
 *   6: GetFocusedElement
 *   7: GetRootElementBuildCache
 *   8: GetFocusedElementBuildCache
 *   9: CreateTreeWalker
 *   10: get_CachedTreeWalker (property)
 *   ...
 *   18: CreatePropertyCondition
 *   19: CreateTrueCondition
 *   20: CreateFalseCondition
 *   ...
 */
public class IUIAutomation extends COMObject {

    public static final String CLSID = "{ff48dba4-60ef-4201-aa87-54103eef594e}";
    public static final String IID = "{30c25b32-5606-446a-b2ca-1be52e3c8a9f}";

    // Property IDs
    public static final int UIA_NamePropertyId = 30005;
    public static final int UIA_ClassNamePropertyId = 30012;
    public static final int UIA_AutomationIdPropertyId = 30011;
    public static final int UIA_ControlTypePropertyId = 30003;
    public static final int UIA_ProcessIdPropertyId = 30002;

    // TreeScope
    public static final int TreeScope_Element = 0;
    public static final int TreeScope_Children = 1;
    public static final int TreeScope_Descendants = 2;
    public static final int TreeScope_Subtree = 4;

    public IUIAutomation(Pointer pointer) {
        super(pointer);
    }

    /**
     * 创建 IUIAutomation 实例
     */
    public static IUIAutomation create() {
        Pointer ptr = Win32Util.createInstance(CLSID, IID);
        return new IUIAutomation(ptr);
    }

    /**
     * 获取根元素（桌面）
     * vtable index 5: GetRootElement
     */
    public IUIAutomationElement getRootElement() {
        PointerByReference ppElement = new PointerByReference();
        invokeVtable(5, new Object[]{ppElement});
        Pointer elementPtr = ppElement.getValue();
        if (elementPtr == null || elementPtr == Pointer.NULL) {
            return null;
        }
        return new IUIAutomationElement(elementPtr);
    }

    /**
     * 获取焦点元素
     * vtable index 6: GetFocusedElement
     */
    public IUIAutomationElement getFocusedElement() {
        PointerByReference ppElement = new PointerByReference();
        invokeVtable(6, new Object[]{ppElement});
        Pointer elementPtr = ppElement.getValue();
        if (elementPtr == null || elementPtr == Pointer.NULL) {
            return null;
        }
        return new IUIAutomationElement(elementPtr);
    }

    /**
     * 创建属性条件
     * vtable index 18: CreatePropertyCondition(propertyId, value, ppCondition)
     */
    public IUIAutomationCondition createPropertyCondition(int propertyId, Pointer value) {
        PointerByReference ppCondition = new PointerByReference();
        invokeVtable(18, new Object[]{propertyId, value, ppCondition});
        Pointer condPtr = ppCondition.getValue();
        if (condPtr == null || condPtr == Pointer.NULL) {
            return null;
        }
        return new IUIAutomationCondition(condPtr);
    }

    /**
     * 创建 True 条件
     * vtable index 19: CreateTrueCondition(ppCondition)
     */
    public IUIAutomationCondition createTrueCondition() {
        PointerByReference ppCondition = new PointerByReference();
        invokeVtable(19, new Object[]{ppCondition});
        return new IUIAutomationCondition(ppCondition.getValue());
    }

    /**
     * 创建 TreeWalker
     * vtable index 9: CreateTreeWalker(pCondition, ppWalker)
     */
    public IUIAutomationTreeWalker createTreeWalker(IUIAutomationCondition condition) {
        PointerByReference ppWalker = new PointerByReference();
        invokeVtable(9, new Object[]{condition.getPointer(), ppWalker});
        Pointer walkerPtr = ppWalker.getValue();
        if (walkerPtr == null || walkerPtr == Pointer.NULL) {
            return null;
        }
        return new IUIAutomationTreeWalker(walkerPtr);
    }
}
