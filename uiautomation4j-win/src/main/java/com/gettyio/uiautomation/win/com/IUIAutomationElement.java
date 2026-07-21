package com.gettyio.uiautomation.win.com;

import com.gettyio.uiautomation.exception.AutomationException;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.Variant;
import com.sun.jna.ptr.PointerByReference;

/**
 * IUIAutomationElement COM 接口封装
 * IID_IUIAutomationElement: {d22108aa-8ac5-49a5-837b-37bbb3d7591e}
 *
 * IUIAutomationElement vtable (after IUnknown 0-2):
 *   3: SetFocus
 *   4: GetRuntimeId
 *   5: FindFirst (scope, condition, ppFound)
 *   6: FindAll (scope, condition, ppFound)
 *   7: FindFirstBuildCache
 *   8: FindAllBuildCache
 *   9: BuildUpdatedCache
 *   10: GetCurrentPropertyValue (propertyId, ignoreDefaultValue, pValue)
 *   11: GetCurrentPropertyValueEx
 *   12: GetCachedPropertyValue
 *   ...
 *   17: GetCurrentPatternAs (patternId, riid, pPatternObject)
 *   ...
 *   22: get_Name (property)
 *   23: get_ClassName (property)
 *   24: get_ControlType (property)
 *   ...
 */
public class IUIAutomationElement extends COMObject {

    public static final String IID = "{d22108aa-8ac5-49a5-837b-37bbb3d7591e}";

    public IUIAutomationElement(Pointer pointer) {
        super(pointer);
    }

    /**
     * 设置焦点
     * vtable index 3: SetFocus
     */
    public void setFocus() {
        invokeVtable(3);
    }

    /**
     * 查找第一个匹配的子元素
     * vtable index 5: FindFirst(scope, condition, ppFound)
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
     * 获取当前属性值
     * vtable index 10: GetCurrentPropertyValue(propertyId, ignoreDefaultValue, pValue)
     */
    public Variant.VARIANT getCurrentPropertyValue(int propertyId) {
        Variant.VARIANT.ByReference pValue = new Variant.VARIANT.ByReference();
        invokeVtable(10, new Object[]{propertyId, 0, pValue});
        return pValue;
    }

    /**
     * 获取 Name 属性
     * vtable index 22: get_Name(pName)
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
     * 获取 ClassName 属性
     * vtable index 23: get_ClassName(pClassName)
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
     * 获取 ControlType 属性
     * vtable index 24: get_ControlType(pControlType)
     */
    public int getControlType() {
        int[] pControlType = new int[1];
        invokeVtable(24, new Object[]{pControlType});
        return pControlType[0];
    }

    /**
     * 获取 AutomationId 属性
     * vtable index 25: get_AutomationId(pAutomationId)
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
     * 获取 ProcessId 属性
     * vtable index 26: get_ProcessId(pProcessId)
     */
    public int getProcessId() {
        int[] pProcessId = new int[1];
        invokeVtable(26, new Object[]{pProcessId});
        return pProcessId[0];
    }

    /**
     * 获取当前 Pattern 对象
     * vtable index 17: GetCurrentPatternAs(patternId, riid, pPatternObject)
     */
    public Pointer getCurrentPatternAs(int patternId, String iid) {
        Guid.GUID guid = Win32Util.createGUID(iid);
        PointerByReference ppPattern = new PointerByReference();
        invokeVtable(17, new Object[]{patternId, guid, ppPattern});
        return ppPattern.getValue();
    }

    /**
     * 获取元素的边界矩形
     * vtable index 43: get_BoundingRectangle(pRect)
     */
    public int[] getBoundingRectangle() {
        // RECT = {left, top, right, bottom}
        int[] rect = new int[4];
        invokeVtable(43, new Object[]{rect});
        return rect;
    }
}
