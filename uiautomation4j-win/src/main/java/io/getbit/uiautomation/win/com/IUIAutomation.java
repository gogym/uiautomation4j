package io.getbit.uiautomation.win.com;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.ptr.PointerByReference;

/**
 * IUIAutomation COM 接口封装
 *
 * <p>对应 Windows SDK 中的 IUIAutomation 接口，是 UIAutomation 的入口接口。</p>
 * 参考文档: https://learn.microsoft.com/en-us/windows/win32/api/uiautomationclient/nn-uiautomationclient-iuiautomation
 *
 * <pre>
 * CLSID_CUIAutomation: {ff48dba4-60ef-4201-aa87-54103eef594e}
 * IID_IUIAutomation:   {30c25b32-5606-446a-b2ca-1be52e3c8a9f}
 *
 * Vtable 布局（继承自 IUnknown）:
 *   [0] QueryInterface(REFIID riid, void** ppvObject) -> HRESULT
 *   [1] AddRef() -> ULONG
 *   [2] Release() -> ULONG
 *   --- IUIAutomation 方法 ---
 *   [3]  CompareElements(IUIAutomationElement* el1, IUIAutomationElement* el2, BOOL* areSame) -> HRESULT
 *   [4]  CompareRuntimeIds(SAFEARRAY(int) rid1, SAFEARRAY(int) rid2, BOOL* areSame) -> HRESULT
 *   [5]  GetRootElement(IUIAutomationElement** ppRoot) -> HRESULT
 *   [6]  GetFocusedElement(IUIAutomationElement** ppFocused) -> HRESULT
 *   [7]  GetRootElementBuildCache(...) -> HRESULT
 *   [8]  GetFocusedElementBuildCache(...) -> HRESULT
 *   [9]  CreateTreeWalker(IUIAutomationCondition* pCondition, IUIAutomationTreeWalker** ppWalker) -> HRESULT
 *   [10] get_CachedTreeWalker (property)
 *   [11] get_ElementFromHandle(...) 
 *   [12] get_ElementFromPoint(...)
 *   [13] get_ElementFromHandleBuildCache(...)
 *   [14] get_ElementFromPointBuildCache(...)
 *   [15] get_FocusedElementBuildCache(...)
 *   [16] AddAutomationEventHandler(...)
 *   [17] RemoveAutomationEventHandler(...)
 *   [18] CreatePropertyCondition(PROPERTYID propertyId, VARIANT value, IUIAutomationCondition** ppCondition) -> HRESULT
 *   [19] CreateTrueCondition(IUIAutomationCondition** ppCondition) -> HRESULT
 *   [20] CreateFalseCondition(IUIAutomationCondition** ppCondition) -> HRESULT
 *   [21] CreateAndCondition(...)
 *   [22] CreateOrCondition(...)
 * </pre>
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
     * <pre>
     * COM 签名: HRESULT GetRootElement(IUIAutomationElement** ppRoot);
     * vtable index: 5
     * </pre>
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
     * 获取当前拥有键盘焦点的元素
     * <pre>
     * COM 签名: HRESULT GetFocusedElement(IUIAutomationElement** ppFocused);
     * vtable index: 6
     * </pre>
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
     * <pre>
     * COM 签名: HRESULT CreatePropertyCondition(
     *     [in] PROPERTYID propertyId,
     *     [in] VARIANT value,
     *     [out, retval] IUIAutomationCondition** ppCondition
     * );
     * vtable index: 18
     * </pre>
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
     * 创建 True 条件（匹配所有元素）
     * <pre>
     * COM 签名: HRESULT CreateTrueCondition(IUIAutomationCondition** ppCondition);
     * vtable index: 19
     * </pre>
     */
    public IUIAutomationCondition createTrueCondition() {
        PointerByReference ppCondition = new PointerByReference();
        invokeVtable(19, new Object[]{ppCondition});
        return new IUIAutomationCondition(ppCondition.getValue());
    }

    /**
     * 创建 TreeWalker（用于遍历 UI 自动化元素树）
     * <pre>
     * COM 签名: HRESULT CreateTreeWalker(
     *     [in] IUIAutomationCondition* pCondition,
     *     [out, retval] IUIAutomationTreeWalker** ppWalker
     * );
     * vtable index: 9
     * </pre>
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

    /**
     * 创建 And 条件（组合多个条件）
     * <pre>
     * COM 签名: HRESULT CreateAndCondition(
     *     [in] IUIAutomationCondition* pCondition1,
     *     [in] IUIAutomationCondition* pCondition2,
     *     [out, retval] IUIAutomationCondition** ppCondition
     * );
     * vtable index: 21
     * </pre>
     */
    public IUIAutomationCondition createAndCondition(IUIAutomationCondition condition1,
                                                      IUIAutomationCondition condition2) {
        PointerByReference ppCondition = new PointerByReference();
        invokeVtable(21, new Object[]{condition1.getPointer(), condition2.getPointer(), ppCondition});
        Pointer condPtr = ppCondition.getValue();
        if (condPtr == null || condPtr == Pointer.NULL) {
            return null;
        }
        return new IUIAutomationCondition(condPtr);
    }
}
