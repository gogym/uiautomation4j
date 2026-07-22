package io.getbit.uiautomation.win.com;

import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.ptr.PointerByReference;

/**
 * IUIAutomationElement COM 接口封装
 *
 * <p>对应 Windows SDK 中的 IUIAutomationElement 接口。
 * 参考文档: https://learn.microsoft.com/en-us/windows/win32/api/uiautomationclient/nn-uiautomationclient-iuiautomationelement</p>
 *
 * <pre>
 * CLSID_CUIAutomation: {ff48dba4-60ef-4201-aa87-54103eef594e}
 * IID_IUIAutomationElement: {d22108aa-8ac5-49a5-837b-569c8a9c5f38}
 *
 * Vtable 布局（继承自 IUnknown）:
 *   [0] QueryInterface(REFIID riid, void** ppvObject) -> HRESULT
 *   [1] AddRef() -> ULONG
 *   [2] Release() -> ULONG
 *   --- IUIAutomationElement 方法 ---
 *   [3]  SetFocus() -> HRESULT
 *   [4]  GetRuntimeId(SAFEARRAY(int)* pRuntimeId) -> HRESULT
 *   [5]  FindFirst(TreeScope scope, IUIAutomationCondition* pCondition, IUIAutomationElement** ppFound) -> HRESULT
 *   [6]  FindAll(TreeScope scope, IUIAutomationCondition* pCondition, IUIAutomationElementArray** ppFound) -> HRESULT
 *   [7]  FindFirstBuildCache(...) -> HRESULT
 *   [8]  FindAllBuildCache(...) -> HRESULT
 *   [9]  BuildUpdatedCache(...) -> HRESULT
 *   [10] GetCurrentPropertyValue(UIA_PropertyId propertyId, BOOL ignoreDefaultValue, VARIANT* pValue) -> HRESULT
 *   [11] GetCurrentPropertyValueEx(...) -> HRESULT
 *   [12] GetCachedPropertyValue(...) -> HRESULT
 *   [13] GetCurrentPatternAs(UIA_PatternId patternId, REFIID riid, void** ppPatternObject) -> HRESULT  (deprecated)
 *   [14] GetCurrentPattern(...)  (deprecated)
 *   [15] GetCurrentPatternAs(...)  (deprecated)
 *   [16] GetCurrentPattern(...)  (deprecated)
 *   [17] GetCurrentPatternAs(UIA_PatternId patternId, REFIID riid, IUnknown** ppPatternObject) -> HRESULT
 *   [18-21] GetCurrentPattern 系列 (deprecated)
 *   --- 属性 get 方法 ---
 *   [22] get_CurrentName(BSTR* pName) -> HRESULT
 *   [23] get_CurrentClassName(BSTR* pClassName) -> HRESULT
 *   [24] get_CurrentControlType(UIA_ControlTypeIds* pControlType) -> HRESULT
 *   [25] get_CurrentAutomationId(BSTR* pAutomationId) -> HRESULT
 *   [26] get_CurrentProcessId(int* pProcessId) -> HRESULT
 *   [27] get_CurrentIsEnabled(BOOL* pIsEnabled) -> HRESULT
 *   [28] get_CurrentIsOffscreen(BOOL* pIsOffscreen) -> HRESULT
 *   [29] get_CurrentHasKeyboardFocus(BOOL* pHasKeyboardFocus) -> HRESULT
 *   [30] get_CurrentIsKeyboardFocusable(BOOL* pIsKeyboardFocusable) -> HRESULT
 *   [31] get_CurrentIsPassword(BOOL* pIsPassword) -> HRESULT
 *   [32] get_CurrentIsContentElement(BOOL* pIsContentElement) -> HRESULT
 *   [33] get_CurrentIsControlElement(BOOL* pIsControlElement) -> HRESULT
 *   [34] get_CurrentLocalizedControlType(BSTR* pLocalizedControlType) -> HRESULT
 *   [35] get_CurrentItemType(BSTR* pItemType) -> HRESULT
 *   [36] get_CurrentIsServerSideProvider(BOOL* pIsServerSideProvider) -> HRESULT
 *   [37] get_CurrentNativeWindowHandle(UIA_HWND* pNativeWindowHandle) -> HRESULT
 *   [38] get_CurrentProviderDescription(BSTR* pProviderDescription) -> HRESULT
 *   [39] get_CurrentFrameworkId(BSTR* pFrameworkId) -> HRESULT
 *   [40] get_CurrentIsRequiredForForm(BOOL* pIsRequiredForForm) -> HRESULT
 *   [41] get_CurrentItemStatus(BSTR* pItemStatus) -> HRESULT
 *   [42] get_CurrentBoundingRectangle(RECT* pRect) -> HRESULT
 *   [43] (reserved / get_CurrentBoundingRectangle 在某些版本中索引不同)
 *   [44] get_CurrentIsOffscreen (alternate)
 *   [45] GetClickablePoint(double* pX, double* pY, BOOL* pGotIt) -> HRESULT
 *   [46] get_CurrentIsKeyboardFocusable (alternate)
 *   [47] Navigate(NavigateDirection direction, IUIAutomationElement** ppFound) -> HRESULT
 *   [48] GetRuntimeId (alternate)
 *   [49] get_CurrentAriaRole(...)
 *   [50] get_CurrentAriaProperties(...)
 * </pre>
 *
 * <p>注意: vtable 索引可能因 Windows SDK 版本不同而略有差异。
 * 本实现基于 Windows 10/11 SDK 的标准 vtable 布局。</p>
 */
public class IUIAutomationElement extends COMObject {

    /** IID_IUIAutomationElement: {d22108aa-8ac5-49a5-837b-569c8a9c5f38} */
    public static final String IID = "{d22108aa-8ac5-49a5-837b-569c8a9c5f38}";

    public IUIAutomationElement(Pointer pointer) {
        super(pointer);
    }

    // ==================== 基本操作 ====================

    /**
     * 设置焦点到当前元素
     * <pre>
     * COM 签名: HRESULT SetFocus();
     * vtable index: 3
     * </pre>
     */
    public void setFocus() {
        invokeVtable(3);
    }

    /**
     * 获取元素的 Runtime ID
     * <pre>
     * COM 签名: HRESULT GetRuntimeId(SAFEARRAY(int)* pRuntimeId);
     * vtable index: 4
     * </pre>
     *
     * <p>通过解析 COM 返回的 SAFEARRAY(int) 结构获取 Runtime ID。
     * 调用完成后会自动释放 SAFEARRAY 内存。</p>
     *
     * @return Runtime ID 数组，如果不可用则返回空数组
     */
    public int[] getRuntimeId() {
        // GetRuntimeId 通过输出参数返回 SAFEARRAY(int) 指针
        Pointer[] pSafeArray = new Pointer[1];
        invokeVtable(4, new Object[]{pSafeArray});
        Pointer safeArrayPtr = pSafeArray[0];
        if (safeArrayPtr == null || safeArrayPtr == Pointer.NULL) {
            return new int[0];
        }
        try {
            // 解析 SAFEARRAY 结构，提取 int 数组
            return Win32Util.safeArrayToIntArray(safeArrayPtr);
        } finally {
            // 释放 COM 分配的 SAFEARRAY 内存
            Win32Util.safeArrayDestroy(safeArrayPtr);
        }
    }

    // ==================== 搜索方法 ====================

    /**
     * 查找第一个匹配条件的子元素
     * <pre>
     * COM 签名: HRESULT FindFirst(
     *     [in] TreeScope scope,
     *     [in] IUIAutomationCondition* pCondition,
     *     [out, retval] IUIAutomationElement** ppFound
     * );
     * vtable index: 5
     * </pre>
     *
     * @param scope     搜索范围 (TreeScope_Element=0, TreeScope_Children=1, TreeScope_Descendants=2, TreeScope_Subtree=4)
     * @param condition 搜索条件
     * @return 找到的元素，未找到返回 null
     */
    public IUIAutomationElement findFirst(int scope, IUIAutomationCondition condition) {
        PointerByReference ppFound = new PointerByReference();
        invokeVtable(5, new Object[]{scope, condition.getPointer(), ppFound});
        Pointer foundPtr = ppFound.getValue();
        if (foundPtr == null || foundPtr == Pointer.NULL) {
            return null;
        }
        return new IUIAutomationElement(foundPtr);
    }

    /**
     * 查找所有匹配条件的子元素
     * <pre>
     * COM 签名: HRESULT FindAll(
     *     [in] TreeScope scope,
     *     [in] IUIAutomationCondition* pCondition,
     *     [out, retval] IUIAutomationElementArray** ppFound
     * );
     * vtable index: 6
     * </pre>
     *
     * @param scope     搜索范围
     * @param condition 搜索条件
     * @return 找到的元素数组 COM 接口，未找到返回 null
     */
    public Pointer findAll(int scope, IUIAutomationCondition condition) {
        PointerByReference ppFound = new PointerByReference();
        invokeVtable(6, new Object[]{scope, condition.getPointer(), ppFound});
        Pointer foundPtr = ppFound.getValue();
        if (foundPtr == null || foundPtr == Pointer.NULL) {
            return null;
        }
        return foundPtr;
    }

    // ==================== 属性值获取 ====================

    /**
     * 获取当前属性值（通用方法）
     * <pre>
     * COM 签名: HRESULT GetCurrentPropertyValue(
     *     [in] PROPERTYID propertyId,
     *     [in] BOOL ignoreDefaultValue,
     *     [out, retval] VARIANT* pValue
     * );
     * vtable index: 10
     * </pre>
     *
     * @param propertyId UIA 属性 ID（如 UIA_NamePropertyId=30005）
     * @return 属性值的 VARIANT 形式
     */
    public Variant.VARIANT getCurrentPropertyValue(int propertyId) {
        Variant.VARIANT.ByReference pValue = new Variant.VARIANT.ByReference();
        invokeVtable(10, new Object[]{propertyId, 0, pValue});
        return pValue;
    }

    /**
     * 获取当前 Pattern 对象（类型安全版本）
     * <pre>
     * COM 签名: HRESULT GetCurrentPatternAs(
     *     [in] PATTERNID patternId,
     *     [in] REFIID riid,
     *     [out, retval] IUnknown** ppPatternObject
     * );
     * vtable index: 17
     * </pre>
     *
     * @param patternId Pattern ID（如 UIA_InvokePatternId=10000）
     * @param iid       接口的 IID
     * @return Pattern 接口的 COM Pointer
     */
    public Pointer getCurrentPatternAs(int patternId, String iid) {
        Guid.GUID guid = Win32Util.createGUID(iid);
        PointerByReference ppPattern = new PointerByReference();
        invokeVtable(17, new Object[]{patternId, guid, ppPattern});
        return ppPattern.getValue();
    }

    // ==================== 字符串属性 ====================

    /**
     * 获取元素的 Name 属性
     * <pre>
     * COM 签名: HRESULT get_CurrentName(BSTR* pName);
     * vtable index: 22
     * 对应 UIA 属性: UIA_NamePropertyId (30005)
     * </pre>
     *
     * @return 元素名称，可能为 null
     */
    public String getName() {
        PointerByReference ppName = new PointerByReference();
        invokeVtable(22, new Object[]{ppName});
        Pointer bstr = ppName.getValue();
        if (bstr == null || bstr == Pointer.NULL) {
            return null;
        }
        String name = Win32Util.bstrToString(bstr);
        Win32Util.freeBstr(bstr);
        return name;
    }

    /**
     * 获取元素的 ClassName 属性
     * <pre>
     * COM 签名: HRESULT get_CurrentClassName(BSTR* pClassName);
     * vtable index: 23
     * 对应 UIA 属性: UIA_ClassNamePropertyId (30012)
     * </pre>
     *
     * @return 类名，可能为 null
     */
    public String getClassName() {
        PointerByReference ppClassName = new PointerByReference();
        invokeVtable(23, new Object[]{ppClassName});
        Pointer bstr = ppClassName.getValue();
        if (bstr == null || bstr == Pointer.NULL) {
            return null;
        }
        String className = Win32Util.bstrToString(bstr);
        Win32Util.freeBstr(bstr);
        return className;
    }

    /**
     * 获取元素的控件类型
     * <pre>
     * COM 签名: HRESULT get_CurrentControlType(UIA_ControlTypeIds* pControlType);
     * vtable index: 24
     * 对应 UIA 属性: UIA_ControlTypePropertyId (30003)
     * </pre>
     *
     * @return 控件类型 ID（对应 ControlType 枚举值）
     */
    public int getControlType() {
        int[] pControlType = new int[1];
        invokeVtable(24, new Object[]{pControlType});
        return pControlType[0];
    }

    /**
     * 获取元素的 AutomationId 属性
     * <pre>
     * COM 签名: HRESULT get_CurrentAutomationId(BSTR* pAutomationId);
     * vtable index: 25
     * 对应 UIA 属性: UIA_AutomationIdPropertyId (30011)
     * </pre>
     *
     * @return AutomationId，可能为 null
     */
    public String getAutomationId() {
        PointerByReference ppId = new PointerByReference();
        invokeVtable(25, new Object[]{ppId});
        Pointer bstr = ppId.getValue();
        if (bstr == null || bstr == Pointer.NULL) {
            return null;
        }
        String id = Win32Util.bstrToString(bstr);
        Win32Util.freeBstr(bstr);
        return id;
    }

    /**
     * 获取元素所属进程的 ID
     * <pre>
     * COM 签名: HRESULT get_CurrentProcessId(int* pProcessId);
     * vtable index: 26
     * 对应 UIA 属性: UIA_ProcessIdPropertyId (30002)
     * </pre>
     *
     * @return 进程 ID
     */
    public int getProcessId() {
        int[] pProcessId = new int[1];
        invokeVtable(26, new Object[]{pProcessId});
        return pProcessId[0];
    }

    // ==================== 布尔属性 ====================

    /**
     * 获取元素是否启用（Enabled）
     * <pre>
     * COM 签名: HRESULT get_CurrentIsEnabled(BOOL* pIsEnabled);
     * vtable index: 27
     * 对应 UIA 属性: UIA_IsEnabledPropertyId (30010)
     * </pre>
     *
     * @return true 表示元素已启用
     */
    public boolean isEnabled() {
        int[] pIsEnabled = new int[1];
        invokeVtable(27, new Object[]{pIsEnabled});
        return pIsEnabled[0] != 0;
    }

    /**
     * 获取元素是否在屏幕外（Offscreen）
     * <pre>
     * COM 签名: HRESULT get_CurrentIsOffscreen(BOOL* pIsOffscreen);
     * vtable index: 28
     * 对应 UIA 属性: UIA_IsOffscreenPropertyId (30022)
     * </pre>
     *
     * @return true 表示元素在屏幕外（不可见）
     */
    public boolean isOffscreen() {
        int[] pIsOffscreen = new int[1];
        invokeVtable(28, new Object[]{pIsOffscreen});
        return pIsOffscreen[0] != 0;
    }

    /**
     * 获取元素是否有键盘焦点
     * <pre>
     * COM 签名: HRESULT get_CurrentHasKeyboardFocus(BOOL* pHasKeyboardFocus);
     * vtable index: 29
     * 对应 UIA 属性: UIA_HasKeyboardFocusPropertyId (30013)
     * </pre>
     *
     * @return true 表示元素当前拥有键盘焦点
     */
    public boolean hasKeyboardFocus() {
        int[] pHasFocus = new int[1];
        invokeVtable(29, new Object[]{pHasFocus});
        return pHasFocus[0] != 0;
    }

    /**
     * 获取元素是否可以接受键盘焦点
     * <pre>
     * COM 签名: HRESULT get_CurrentIsKeyboardFocusable(BOOL* pIsKeyboardFocusable);
     * vtable index: 30
     * 对应 UIA 属性: UIA_IsKeyboardFocusablePropertyId (30014)
     * </pre>
     *
     * @return true 表示元素可以接受键盘焦点
     */
    public boolean isKeyboardFocusable() {
        int[] pIsFocusable = new int[1];
        invokeVtable(30, new Object[]{pIsFocusable});
        return pIsFocusable[0] != 0;
    }

    /**
     * 获取元素是否为密码控件
     * <pre>
     * COM 签名: HRESULT get_CurrentIsPassword(BOOL* pIsPassword);
     * vtable index: 31
     * 对应 UIA 属性: UIA_IsPasswordPropertyId (30019)
     * </pre>
     *
     * @return true 表示元素是密码控件
     */
    public boolean isPassword() {
        int[] pIsPassword = new int[1];
        invokeVtable(31, new Object[]{pIsPassword});
        return pIsPassword[0] != 0;
    }

    /**
     * 获取元素是否为内容元素
     * <pre>
     * COM 签名: HRESULT get_CurrentIsContentElement(BOOL* pIsContentElement);
     * vtable index: 32
     * 对应 UIA 属性: UIA_IsContentElementPropertyId (30017)
     * </pre>
     *
     * @return true 表示元素是内容元素（在 UIA 树中对用户可见/有意义）
     */
    public boolean isContentElement() {
        int[] pIsContent = new int[1];
        invokeVtable(32, new Object[]{pIsContent});
        return pIsContent[0] != 0;
    }

    /**
     * 获取元素是否为控件元素
     * <pre>
     * COM 签名: HRESULT get_CurrentIsControlElement(BOOL* pIsControlElement);
     * vtable index: 33
     * 对应 UIA 属性: UIA_IsControlElementPropertyId (30016)
     * </pre>
     *
     * @return true 表示元素是控件元素
     */
    public boolean isControlElement() {
        int[] pIsControl = new int[1];
        invokeVtable(33, new Object[]{pIsControl});
        return pIsControl[0] != 0;
    }

    /**
     * 获取元素的本地化控件类型描述
     * <pre>
     * COM 签名: HRESULT get_CurrentLocalizedControlType(BSTR* pLocalizedControlType);
     * vtable index: 34
     * 对应 UIA 属性: UIA_LocalizedControlTypePropertyId (30004)
     * </pre>
     *
     * @return 本地化类型描述（如 "按钮"、"编辑"）
     */
    public String getLocalizedControlType() {
        PointerByReference ppValue = new PointerByReference();
        invokeVtable(34, new Object[]{ppValue});
        Pointer bstr = ppValue.getValue();
        if (bstr == null || bstr == Pointer.NULL) {
            return null;
        }
        String value = Win32Util.bstrToString(bstr);
        Win32Util.freeBstr(bstr);
        return value;
    }

    /**
     * 获取元素的 Item Type
     * <pre>
     * COM 签名: HRESULT get_CurrentItemType(BSTR* pItemType);
     * vtable index: 35
     * 对应 UIA 属性: UIA_ItemTypePropertyId (30021)
     * </pre>
     *
     * @return 项目类型描述
     */
    public String getItemType() {
        PointerByReference ppValue = new PointerByReference();
        invokeVtable(35, new Object[]{ppValue});
        Pointer bstr = ppValue.getValue();
        if (bstr == null || bstr == Pointer.NULL) {
            return null;
        }
        String value = Win32Util.bstrToString(bstr);
        Win32Util.freeBstr(bstr);
        return value;
    }

    /**
     * 获取元素是否为服务端提供者
     * <pre>
     * COM 签名: HRESULT get_CurrentIsServerSideProvider(BOOL* pIsServerSideProvider);
     * vtable index: 36
     * </pre>
     */
    public boolean isServerSideProvider() {
        int[] pValue = new int[1];
        invokeVtable(36, new Object[]{pValue});
        return pValue[0] != 0;
    }

    /**
     * 获取元素的原生窗口句柄
     * <pre>
     * COM 签名: HRESULT get_CurrentNativeWindowHandle(UIA_HWND* pNativeWindowHandle);
     * vtable index: 37
     * 对应 UIA 属性: UIA_NativeWindowHandlePropertyId (30020)
     * </pre>
     *
     * @return 窗口句柄值（HWND），无窗口返回 0
     */
    public long getNativeWindowHandle() {
        Pointer[] pHandle = new Pointer[1];
        invokeVtable(37, new Object[]{pHandle});
        return pHandle[0] != null ? Pointer.nativeValue(pHandle[0]) : 0;
    }

    /**
     * 获取元素的 Framework ID
     * <pre>
     * COM 签名: HRESULT get_CurrentFrameworkId(BSTR* pFrameworkId);
     * vtable index: 39
     * 对应 UIA 属性: UIA_FrameworkIdPropertyId (30024)
     * </pre>
     *
     * @return 框架 ID（如 "WPF"、"WinForm"、"Win32"）
     */
    public String getFrameworkId() {
        PointerByReference ppValue = new PointerByReference();
        invokeVtable(39, new Object[]{ppValue});
        Pointer bstr = ppValue.getValue();
        if (bstr == null || bstr == Pointer.NULL) {
            return null;
        }
        String value = Win32Util.bstrToString(bstr);
        Win32Util.freeBstr(bstr);
        return value;
    }

    /**
     * 获取元素的 Item Status 描述
     * <pre>
     * COM 签名: HRESULT get_CurrentItemStatus(BSTR* pItemStatus);
     * vtable index: 41
     * 对应 UIA 属性: UIA_ItemStatusPropertyId (30023)
     * </pre>
     *
     * @return 状态描述（如 "加载中..."）
     */
    public String getItemStatus() {
        PointerByReference ppValue = new PointerByReference();
        invokeVtable(41, new Object[]{ppValue});
        Pointer bstr = ppValue.getValue();
        if (bstr == null || bstr == Pointer.NULL) {
            return null;
        }
        String value = Win32Util.bstrToString(bstr);
        Win32Util.freeBstr(bstr);
        return value;
    }

    // ==================== 几何与导航 ====================

    /**
     * 获取元素的边界矩形
     * <pre>
     * COM 签名: HRESULT get_CurrentBoundingRectangle(RECT* pRect);
     * vtable index: 42 (部分版本为 43)
     * 对应 UIA 属性: UIA_BoundingRectanglePropertyId (30018)
     * RECT 结构: {left, top, right, bottom}
     * </pre>
     *
     * @return int[4] = {left, top, right, bottom}
     */
    public int[] getBoundingRectangle() {
        int[] rect = new int[4];
        invokeVtable(42, new Object[]{rect});
        return rect;
    }

    /**
     * 获取元素的可点击坐标点
     * <pre>
     * COM 签名: HRESULT GetClickablePoint(double* pX, double* pY, BOOL* pGotIt);
     * vtable index: 45
     * </pre>
     *
     * @return double[2] = {x, y}，如果无法获取可点击点则返回 null
     */
    public double[] getClickablePoint() {
        double[] pX = new double[1];
        double[] pY = new double[1];
        int[] pGotIt = new int[1];
        invokeVtable(45, new Object[]{pX, pY, pGotIt});
        if (pGotIt[0] == 0) {
            return null;
        }
        return new double[]{pX[0], pY[0]};
    }

    /**
     * 按方向导航到相邻元素
     * <pre>
     * COM 签名: HRESULT Navigate(
     *     [in] NavigateDirection direction,
     *     [out, retval] IUIAutomationElement** ppFound
     * );
     * vtable index: 47
     * NavigateDirection: Parent=0, NextSibling=1, PreviousSibling=2, FirstChild=3, LastChild=4
     * </pre>
     *
     * @param direction 导航方向
     * @return 目标元素，未找到返回 null
     */
    public IUIAutomationElement navigate(int direction) {
        PointerByReference ppFound = new PointerByReference();
        invokeVtable(47, new Object[]{direction, ppFound});
        Pointer foundPtr = ppFound.getValue();
        if (foundPtr == null || foundPtr == Pointer.NULL) {
            return null;
        }
        return new IUIAutomationElement(foundPtr);
    }

    /**
     * 获取父元素
     * <pre>
     * 等价于 Navigate(NavigateDirection_Parent=0, ppFound)
     * </pre>
     *
     * @return 父元素，未找到返回 null
     */
    public IUIAutomationElement getParent() {
        return navigate(0);
    }

    /**
     * 获取下一个兄弟元素
     * <pre>
     * 等价于 Navigate(NavigateDirection_NextSibling=1, ppFound)
     * </pre>
     *
     * @return 下一个兄弟元素，未找到返回 null
     */
    public IUIAutomationElement getNextSibling() {
        return navigate(1);
    }

    /**
     * 获取上一个兄弟元素
     * <pre>
     * 等价于 Navigate(NavigateDirection_PreviousSibling=2, ppFound)
     * </pre>
     *
     * @return 上一个兄弟元素，未找到返回 null
     */
    public IUIAutomationElement getPreviousSibling() {
        return navigate(2);
    }

    /**
     * 获取第一个子元素
     * <pre>
     * 等价于 Navigate(NavigateDirection_FirstChild=3, ppFound)
     * </pre>
     *
     * @return 第一个子元素，未找到返回 null
     */
    public IUIAutomationElement getFirstChild() {
        return navigate(3);
    }

    /**
     * 获取最后一个子元素
     * <pre>
     * 等价于 Navigate(NavigateDirection_LastChild=4, ppFound)
     * </pre>
     *
     * @return 最后一个子元素，未找到返回 null
     */
    public IUIAutomationElement getLastChild() {
        return navigate(4);
    }
}
