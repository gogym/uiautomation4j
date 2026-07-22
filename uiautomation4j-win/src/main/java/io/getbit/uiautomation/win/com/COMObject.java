package io.getbit.uiautomation.win.com;

import io.getbit.uiautomation.exception.AutomationException;
import com.sun.jna.Function;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Guid;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;

/**
 * COM 对象基础包装类
 * 提供 vtable 方法调用、引用计数管理等基础功能
 *
 * <p>实现 {@link AutoCloseable} 接口，支持 try-with-resources 自动释放 COM 资源：</p>
 * <pre>
 * try (IUIAutomationElement element = someElement.findFirst(scope, condition)) {
 *     // 使用 element
 * } // 自动调用 close() -> release()
 * </pre>
 */
public abstract class COMObject implements AutoCloseable {

    protected Pointer pointer; // IUnknown pointer

    protected COMObject() {
    }

    protected COMObject(Pointer pointer) {
        this.pointer = pointer;
    }

    /**
     * 获取底层 COM Pointer
     */
    public Pointer getPointer() {
        return pointer;
    }

    /**
     * 设置底层 COM Pointer
     */
    protected void setPointer(Pointer pointer) {
        this.pointer = pointer;
    }

    /**
     * 是否有效
     */
    public boolean isValid() {
        return pointer != null && pointer != Pointer.NULL;
    }

    /**
     * 调用 vtable 方法（无参数，返回 HRESULT）
     *
     * @param vtableIndex vtable 中的方法索引
     */
    protected WinNT.HRESULT invokeVtable(int vtableIndex) {
        return invokeVtable(vtableIndex, new Object[0]);
    }

    /**
     * 调用 vtable 方法
     *
     * @param vtableIndex vtable 中的方法索引
     * @param args        方法参数
     */
    protected WinNT.HRESULT invokeVtable(int vtableIndex, Object[] args) {
        if (!isValid()) {
            throw new AutomationException("COM 对象无效，无法调用 vtable 方法");
        }
        // 获取 vtable 指针（IUnknown 的第一个字段是指向 vtable 的指针）
        Pointer vtable = pointer.getPointer(0);
        // 获取具体方法指针
        Pointer methodPtr = vtable.getPointer((long) vtableIndex * Native.POINTER_SIZE);
        // 使用 Function 调用方法
        Function function = Function.getFunction(methodPtr, Function.ALT_CONVENTION);
        // 构建完整参数（第一个参数是 this 指针）
        Object[] fullArgs = new Object[args.length + 1];
        fullArgs[0] = pointer;
        System.arraycopy(args, 0, fullArgs, 1, args.length);
        // 调用并返回 HRESULT
        int hrValue = function.invokeInt(fullArgs);
        WinNT.HRESULT hr = new WinNT.HRESULT(hrValue);
        if (hrValue < 0) {
            throw new AutomationException("COM vtable 调用失败, vtableIndex=" + vtableIndex
                    + ", HRESULT=0x" + String.format("%08X", hrValue));
        }
        return hr;
    }

    /**
     * 调用 vtable 方法并返回指针结果
     */
    protected Pointer invokeVtablePointer(int vtableIndex, Object[] args) {
        if (!isValid()) {
            throw new AutomationException("COM 对象无效，无法调用 vtable 方法");
        }
        Pointer vtable = pointer.getPointer(0);
        Pointer methodPtr = vtable.getPointer((long) vtableIndex * Native.POINTER_SIZE);
        Function function = Function.getFunction(methodPtr, Function.ALT_CONVENTION);
        Object[] fullArgs = new Object[args.length + 1];
        fullArgs[0] = pointer;
        System.arraycopy(args, 0, fullArgs, 1, args.length);
        return (Pointer) function.invoke(Pointer.class, fullArgs);
    }

    /**
     * 调用 vtable 方法并返回 int 结果
     */
    protected int invokeVtableInt(int vtableIndex, Object[] args) {
        if (!isValid()) {
            throw new AutomationException("COM 对象无效，无法调用 vtable 方法");
        }
        Pointer vtable = pointer.getPointer(0);
        Pointer methodPtr = vtable.getPointer((long) vtableIndex * Native.POINTER_SIZE);
        Function function = Function.getFunction(methodPtr, Function.ALT_CONVENTION);
        Object[] fullArgs = new Object[args.length + 1];
        fullArgs[0] = pointer;
        System.arraycopy(args, 0, fullArgs, 1, args.length);
        return function.invokeInt(fullArgs);
    }

    /**
     * QueryInterface - 查询接口
     * <pre>
     * COM 签名: HRESULT QueryInterface(REFIID riid, void** ppvObject);
     * vtable index: 0
     * </pre>
     *
     * @param iid  接口 ID
     * @param ppv  输出参数
     * @return HRESULT
     */
    public WinNT.HRESULT queryInterface(Guid.GUID iid, PointerByReference ppv) {
        // IUnknown::QueryInterface is vtable index 0
        return invokeVtable(0, new Object[]{iid, ppv});
    }

    /**
     * AddRef - 增加引用计数
     * <pre>
     * COM 签名: ULONG AddRef();
     * vtable index: 1
     * </pre>
     */
    public int addRef() {
        // IUnknown::AddRef is vtable index 1
        return invokeVtableInt(1, new Object[0]);
    }

    /**
     * Release - 减少引用计数，当引用计数为 0 时 COM 对象被释放
     * <pre>
     * COM 签名: ULONG Release();
     * vtable index: 2
     * </pre>
     */
    public int release() {
        if (!isValid()) {
            return 0;
        }
        // IUnknown::Release is vtable index 2
        int refCount = invokeVtableInt(2, new Object[0]);
        if (refCount == 0) {
            pointer = null;
        }
        return refCount;
    }

    /**
     * 释放 COM 对象（等同于 release()）
     */
    public void dispose() {
        release();
    }

    /**
     * AutoCloseable 接口实现 - 支持 try-with-resources 自动释放
     * 调用 release() 减少 COM 引用计数，防止内存泄漏
     */
    @Override
    public void close() {
        release();
    }
}
