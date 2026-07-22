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
 * IID_IUIAutomation:   {30cbe57d-d9d0-452a-ab13-7ac5ac4825ee}
 *
 * Vtable 布局（继承自 IUnknown）:
 *   [0] QueryInterface(REFIID riid, void** ppvObject) -> HRESULT
 *   [1] AddRef() -> ULONG
 *   [2] Release() -> ULONG
 *   --- IUIAutomation 方法 ---
 *   [3]  CompareElements(IUIAutomationElement* el1, IUIAutomationElement* el2, BOOL* areSame) -> HRESULT
 *   [4]  CompareRuntimeIds(SAFEARRAY(int) rid1, SAFEARRAY(int) rid2, BOOL* areSame) -> HRESULT
 *   [5]  GetRootElement(IUIAutomationElement** ppRoot) -> HRESULT
 *   [6]  ElementFromHandle(VARIANT* hwnd, IUIAutomationElement** ppElement) -> HRESULT
 *   [7]  ElementFromPoint(POINT pt, IUIAutomationElement** ppElement) -> HRESULT
 *   [8]  GetFocusedElement(IUIAutomationElement** ppFocused) -> HRESULT
 *   [9]  GetRootElementBuildCache(...) -> HRESULT
 *   [10] ElementFromHandleBuildCache(...) -> HRESULT
 *   [11] ElementFromPointBuildCache(...) -> HRESULT
 *   [12] GetFocusedElementBuildCache(...) -> HRESULT
 *   [13] CreateTreeWalker(IUIAutomationCondition* pCondition, IUIAutomationTreeWalker** ppWalker) -> HRESULT
 *   [14] get_ControlViewWalker (property)
 *   [15] get_ContentViewWalker (property)
 *   [16] get_RawViewWalker (property)
 *   ...
 *   [20] CreatePropertyCondition(PROPERTYID propertyId, VARIANT value, IUIAutomationCondition** ppCondition) -> HRESULT
 *   [21] CreateTrueCondition(IUIAutomationCondition** ppCondition) -> HRESULT
 *   [22] CreateFalseCondition(IUIAutomationCondition** ppCondition) -> HRESULT
 *   [23] CreateAndCondition(...) -> HRESULT
 *   [24] CreateOrCondition(...) -> HRESULT
 * </pre>
 */
public class IUIAutomation extends COMObject {

    public static final String CLSID = "{ff48dba4-60ef-4201-aa87-54103eef594e}";
    public static final String IID = "{30cbe57d-d9d0-452a-ab13-7ac5ac4825ee}";

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
     * vtable index: 8
     * </pre>
     */
    public IUIAutomationElement getFocusedElement() {
        PointerByReference ppElement = new PointerByReference();
        invokeVtable(8, new Object[]{ppElement});
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
     * vtable index: 20
     * </pre>
     */
    public IUIAutomationCondition createPropertyCondition(int propertyId, Pointer value) {
        PointerByReference ppCondition = new PointerByReference();
        invokeVtable(20, new Object[]{propertyId, value, ppCondition});
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
     * vtable index: 21
     * </pre>
     */
    public IUIAutomationCondition createTrueCondition() {
        PointerByReference ppCondition = new PointerByReference();
        invokeVtable(21, new Object[]{ppCondition});
        return new IUIAutomationCondition(ppCondition.getValue());
    }

    /**
     * 创建 TreeWalker（用于遍历 UI 自动化元素树）
     * <pre>
     * COM 签名: HRESULT CreateTreeWalker(
     *     [in] IUIAutomationCondition* pCondition,
     *     [out, retval] IUIAutomationTreeWalker** ppWalker
     * );
     * vtable index: 13
     * </pre>
     */
    public IUIAutomationTreeWalker createTreeWalker(IUIAutomationCondition condition) {
        PointerByReference ppWalker = new PointerByReference();
        invokeVtable(13, new Object[]{condition.getPointer(), ppWalker});
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
     * vtable index: 23
     * </pre>
     */
    public IUIAutomationCondition createAndCondition(IUIAutomationCondition condition1,
                                                      IUIAutomationCondition condition2) {
        PointerByReference ppCondition = new PointerByReference();
        invokeVtable(23, new Object[]{condition1.getPointer(), condition2.getPointer(), ppCondition});
        Pointer condPtr = ppCondition.getValue();
        if (condPtr == null || condPtr == Pointer.NULL) {
            return null;
        }
        return new IUIAutomationCondition(condPtr);
    }
}
