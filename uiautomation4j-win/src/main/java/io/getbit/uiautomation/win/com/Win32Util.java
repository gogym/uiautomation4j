package io.getbit.uiautomation.win.com;

import io.getbit.uiautomation.exception.AutomationException;
import com.sun.jna.Function;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.Ole32;
import com.sun.jna.platform.win32.OaIdl;
import com.sun.jna.platform.win32.OleAuto;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;

import java.nio.charset.StandardCharsets;

/**
 * Win32 / COM 工具类
 *
 * <p>提供 COM 初始化、BSTR 字符串操作、GUID 创建、COM 实例创建等工具方法。
 * 这些方法封装了 Windows COM API 的底层调用，供其他 COM 包装类使用。</p>
 *
 * <p>主要功能：</p>
 * <ul>
 *   <li>{@link #initCOM()} / {@link #uninitCOM()} - COM 库初始化/反初始化</li>
 *   <li>{@link #createInstance(String, String)} - 通过 CoCreateInstance 创建 COM 对象</li>
 *   <li>{@link #bstrToString(Pointer)} / {@link #stringToBstr(String)} - BSTR 与 Java String 互转</li>
 *   <li>{@link #createGUID(String)} - 创建 GUID 结构</li>
 * </ul>
 */
public class Win32Util {

    /** 每个线程的 COM 是否已初始化标志（COM 初始化是每线程的） */
    private static final ThreadLocal<Boolean> comInitialized = ThreadLocal.withInitial(() -> false);
    /** 每个线程中是否由本类调用了 CoInitializeEx（用于 uninitCOM 时判断是否需要反初始化） */
    private static final ThreadLocal<Boolean> comInitializedByUs = ThreadLocal.withInitial(() -> false);

    /**
     * 初始化 COM 库（每线程独立初始化，STA 模式）
     * <p>COM 库的初始化是每线程的，每个使用 COM 的线程都必须独立调用
     * {@code CoInitializeEx}。此方法使用 {@link ThreadLocal} 跟踪每个线程的初始化状态。</p>
     * <p>始终使用 {@code COINIT_APARTMENTTHREADED}（STA 模式）初始化，
     * 因为 UIA COM 对象要求 STA 线程。所有 UIA 操作应通过 {@link StaTaskExecutor}
     * 在专用 STA 线程中执行。</p>
     */
    public static void initCOM() {
        if (!comInitialized.get()) {
            WinNT.HRESULT hr = Ole32.INSTANCE.CoInitializeEx(null, Ole32.COINIT_APARTMENTTHREADED);
            if (hr.intValue() == 0 || hr.intValue() == 1) {
                // S_OK=0: 首次初始化成功
                // S_FALSE=1: 已以相同模式初始化过，需要匹配调用 CoUninitialize
                comInitialized.set(true);
                comInitializedByUs.set(true);
            } else {
                throw new AutomationException("COM 初始化失败(STA): 0x" + Integer.toHexString(hr.intValue()));
            }
        }
    }

    /**
     * 释放 COM 库
     * <p>调用 {@code CoUninitialize()} 释放 COM 资源。
     * 应在程序退出时调用。</p>
     */
    public static void uninitCOM() {
        if (comInitialized.get()) {
            if (comInitializedByUs.get()) {
                Ole32.INSTANCE.CoUninitialize();
            }
            comInitialized.set(false);
            comInitializedByUs.set(false);
        }
    }

    /**
     * 创建 GUID 结构
     * <p>调用 {@code IIDFromString} 将字符串格式的 GUID 转换为 {@link Guid.GUID} 结构。</p>
     *
     * @param guidStr GUID 字符串，格式如 "{ff48dba4-60ef-4201-aa87-54103eef594e}"
     * @return GUID 结构
     * @throws AutomationException 如果 GUID 格式无效
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
     * <p>调用 {@code CoCreateInstance} 创建 COM 对象。
     * 这是创建 IUIAutomation 等 COM 接口的入口方法。</p>
     *
     * @param clsidStr CLSID 字符串 (如 "{ff48dba4-60ef-4201-aa87-54103eef594e}")
     * @param iidStr   IID 字符串 (如 "{30cbe57d-d9d0-452a-ab13-7ac5ac4825ee}")
     * @return COM 对象指针
     * @throws AutomationException 如果创建失败
     */
    public static Pointer createInstance(String clsidStr, String iidStr) {
        initCOM();

        Guid.GUID clsid = createGUID(clsidStr);
        Guid.GUID iid = createGUID(iidStr);

        // 直接调用 CoCreateInstance 创建 COM 实例
        PointerByReference ppv = new PointerByReference();
        Function coCreateInstance = Function.getFunction("ole32", "CoCreateInstance");
        int hr = coCreateInstance.invokeInt(new Object[]{
                clsid.getPointer(),          // rclsid
                null,                        // pUnkOuter
                0x1 | 0x4 | 0x10,           // dwClsContext = CLSCTX_ALL
                iid.getPointer(),            // riid
                ppv                          // ppv
        });
        if (hr != 0) {
            throw new AutomationException("CoCreateInstance 失败: 0x" +
                    Integer.toHexString(hr) + " CLSID=" + clsidStr);
        }
        return ppv.getValue();
    }

    /**
     * 将 BSTR (Pointer) 转换为 Java String
     * <p>BSTR 是 COM 中的宽字符串类型，使用 UTF-16LE 编码。
     * 通过 {@code SysStringLen} 获取长度，然后读取内存。</p>
     *
     * @param bstr BSTR 指针
     * @return Java String，如果 bstr 为 null 则返回 null
     */
    public static String bstrToString(Pointer bstr) {
        if (bstr == null || bstr == Pointer.NULL) {
            return null;
        }
        WTypes.BSTR bstrObj = new WTypes.BSTR(bstr);
        int len = OleAuto.INSTANCE.SysStringLen(bstrObj);
        if (len == 0) {
            return "";
        }
        // 使用已知字符长度精确读取（每个 UTF-16 字符占 2 字节）
        int byteLen = len * 2;
        byte[] bytes = bstr.getByteArray(0, byteLen);
        return new String(bytes, StandardCharsets.UTF_16LE);
    }

    /**
     * 将 Java String 转换为 BSTR
     * <p>调用 {@code SysAllocString} 分配 BSTR 内存。
     * 使用后必须调用 {@link #freeBstr(Pointer)} 释放内存，防止内存泄漏。</p>
     *
     * @param str Java 字符串
     * @return BSTR 指针
     */
    public static Pointer stringToBstr(String str) {
        if (str == null) {
            return Pointer.NULL;
        }
        WTypes.BSTR bstr = OleAuto.INSTANCE.SysAllocString(str);
        return bstr.getPointer();
    }

    /**
     * 释放 BSTR 内存
     * <p>调用 {@code SysFreeString} 释放由 {@link #stringToBstr(String)} 分配的 BSTR。
     * 必须与 stringToBstr 配对使用，否则会导致内存泄漏。</p>
     *
     * @param bstr BSTR 指针
     */
    public static void freeBstr(Pointer bstr) {
        if (bstr != null && bstr != Pointer.NULL) {
            WTypes.BSTR bstrObj = new WTypes.BSTR(bstr);
            OleAuto.INSTANCE.SysFreeString(bstrObj);
        }
    }

    /**
     * 将 SAFEARRAY(int) 解析为 Java int 数组
     * <p>直接从 SAFEARRAY 结构体内存布局中读取数据，不依赖额外的 OLE API 调用。
     * 适用于一维 SAFEARRAY of VT_I4（int）类型。支持 32 位和 64 位系统。</p>
     *
     * <p>SAFEARRAY 内存布局（64 位系统）：</p>
     * <pre>
     * offset  0: USHORT cDims        (2 bytes) - 维数
     * offset  2: USHORT fFeatures     (2 bytes) - 特性标志
     * offset  4: ULONG  cbElements    (4 bytes) - 每个元素的字节数
     * offset  8: ULONG  cLocks        (4 bytes) - 锁定计数
     * offset 16: PVOID  pvData        (8 bytes) - 指向实际数据的指针
     * offset 24: ULONG  cElements     (4 bytes) - 第一维元素个数
     * offset 28: LONG   lLbound       (4 bytes) - 第一维下界
     * </pre>
     *
     * <p>SAFEARRAY 内存布局（32 位系统）：</p>
     * <pre>
     * offset  0: USHORT cDims        (2 bytes)
     * offset  2: USHORT fFeatures     (2 bytes)
     * offset  4: ULONG  cbElements    (4 bytes)
     * offset  8: ULONG  cLocks        (4 bytes)
     * offset 12: PVOID  pvData        (4 bytes)
     * offset 16: ULONG  cElements     (4 bytes)
     * offset 20: LONG   lLbound       (4 bytes)
     * </pre>
     *
     * @param safeArrayPtr SAFEARRAY 指针（由 COM 方法返回）
     * @return Java int 数组，如果指针无效则返回空数组
     */
    public static int[] safeArrayToIntArray(Pointer safeArrayPtr) {
        if (safeArrayPtr == null || safeArrayPtr == Pointer.NULL) {
            return new int[0];
        }
        // 根据指针大小判断 32/64 位，动态计算 SAFEARRAY 字段偏移
        boolean is64bit = Native.POINTER_SIZE == 8;
        int pvDataOffset = is64bit ? 16 : 12;
        int cElementsOffset = is64bit ? 24 : 16;

        // 读取元素个数
        int cElements = safeArrayPtr.getInt(cElementsOffset);
        if (cElements <= 0) {
            return new int[0];
        }
        // 读取数据指针
        Pointer pvData = safeArrayPtr.getPointer(pvDataOffset);
        if (pvData == null || pvData == Pointer.NULL) {
            return new int[0];
        }
        // 一次性读取所有 int 值
        return pvData.getIntArray(0, cElements);
    }

    /**
     * 释放 SAFEARRAY 内存
     * <p>调用 {@code SafeArrayDestroy} 释放由 COM API（如 {@code GetRuntimeId}）分配的 SAFEARRAY。
     * 必须与返回 SAFEARRAY 的 COM 方法配对使用，防止内存泄漏。</p>
     *
     * @param safeArrayPtr SAFEARRAY 指针
     */
    public static void safeArrayDestroy(Pointer safeArrayPtr) {
        if (safeArrayPtr != null && safeArrayPtr != Pointer.NULL) {
            OleAuto.INSTANCE.SafeArrayDestroy(new OaIdl.SAFEARRAY(safeArrayPtr));
        }
    }
}
