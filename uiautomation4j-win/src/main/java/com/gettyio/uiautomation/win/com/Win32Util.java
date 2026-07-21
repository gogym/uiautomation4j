package com.gettyio.uiautomation.win.com;

import com.gettyio.uiautomation.exception.AutomationException;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;

/**
 * Win32 / COM 工具类
 * 提供 COM 初始化、BSTR 操作、GUID 创建等工具方法
 */
public class Win32Util {

    private static boolean comInitialized = false;
    private static final int CLSCTX_ALL = 0x17; // CLSCTX_INPROC_SERVER | CLSCTX_LOCAL_SERVER | CLSCTX_REMOTE_SERVER

    /**
     * 初始化 COM 库
     */
    public static synchronized void initCOM() {
        if (!comInitialized) {
            WinNT.HRESULT hr = Ole32.INSTANCE.CoInitializeEx(null, Ole32.COINIT_APARTMENTTHREADED);
            if (hr.intValue() != 0 && hr.intValue() != 1) { // S_OK=0, S_FALSE=1
                // RPC_E_CHANGED_MODE = 0x80010106 - 已经以不同模式初始化
                if (hr.intValue() != (int) 0x80010106L) {
                    throw new AutomationException("COM 初始化失败: " + hr.intValue());
                }
            }
            comInitialized = true;
        }
    }

    /**
     * 释放 COM 库
     */
    public static synchronized void uninitCOM() {
        if (comInitialized) {
            Ole32.INSTANCE.CoUninitialize();
            comInitialized = false;
        }
    }

    /**
     * 创建 GUID 结构
     */
    public static Guid.GUID createGUID(String guidStr) {
        Guid.GUID guid = new Guid.GUID();
        WinNT.HRESULT hr = Ole32.INSTANCE.IIDFromString(guidStr, guid);
        if (hr.intValue() != 0) {
            throw new AutomationException("无法解析 GUID: " + guidStr);
        }
        return guid;
    }

    /**
     * 通过 CLSID 创建 COM 实例
     *
     * @param clsidStr CLSID 字符串 (如 "{ff48dba4-60ef-4201-aa87-54103eef594e}")
     * @param iidStr   IID 字符串
     * @return COM 对象指针
     */
    public static Pointer createInstance(String clsidStr, String iidStr) {
        initCOM();

        Guid.GUID clsid = createGUID(clsidStr);
        Guid.GUID iid = createGUID(iidStr);
        PointerByReference ppv = new PointerByReference();

        WinNT.HRESULT hr = Ole32.INSTANCE.CoCreateInstance(
                clsid, null, CLSCTX_ALL, iid, ppv);

        if (hr.intValue() != 0) {
            throw new AutomationException("CoCreateInstance 失败: 0x" +
                    Integer.toHexString(hr.intValue()) + " CLSID=" + clsidStr);
        }

        return ppv.getValue();
    }

    /**
     * 将 BSTR (Pointer) 转换为 Java String
     */
    public static String bstrToString(Pointer bstr) {
        if (bstr == null || bstr == Pointer.NULL) {
            return null;
        }
        // 将 Pointer 包装为 WTypes.BSTR
        WTypes.BSTR bstrObj = new WTypes.BSTR(bstr);
        int len = OleAuto.INSTANCE.SysStringLen(bstrObj);
        if (len == 0) {
            return "";
        }
        return bstr.getString(0, "UTF-16LE");
    }

    /**
     * 将 Java String 转换为 BSTR
     */
    public static Pointer stringToBstr(String str) {
        if (str == null) {
            return Pointer.NULL;
        }
        WTypes.BSTR bstr = OleAuto.INSTANCE.SysAllocString(str);
        return bstr.getPointer();
    }

    /**
     * 释放 BSTR
     */
    public static void freeBstr(Pointer bstr) {
        if (bstr != null && bstr != Pointer.NULL) {
            WTypes.BSTR bstrObj = new WTypes.BSTR(bstr);
            OleAuto.INSTANCE.SysFreeString(bstrObj);
        }
    }
}
