package io.getbit.uiautomation.mac.ax;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import com.sun.jna.platform.mac.CoreFoundation;
import com.sun.jna.platform.mac.CoreFoundation.*;
import com.sun.jna.ptr.PointerByReference;

/**
 * macOS CoreFoundation 类型操作工具类
 *
 * <p>封装 JNA 对 CoreFoundation 框架的调用，提供 CFString、CFArray、CFBoolean、
 * CFDictionary 等类型的创建、转换和释放操作。</p>
 *
 * <p>macOS Accessibility API 大量使用 CF 类型进行数据交换：
 * <ul>
 *   <li>CFStringRef → 字符串属性（名称、角色、值等）</li>
 *   <li>CFArrayRef → 子元素列表</li>
 *   <li>CFBooleanRef → 布尔属性（是否启用、是否可见等）</li>
 *   <li>CFDictionaryRef → 复合属性（位置、大小等）</li>
 *   <li>AXValueRef → 几何值（点、大小、矩形）</li>
 * </ul>
 *
 * <p>内存管理规则：通过 {@code Copy} 语义获取的 CF 对象需要调用 {@code CFRelease} 释放。
 * 本工具类的 {@code release()} 方法统一处理空指针和 NULL 指针检查。</p>
 *
 * @see <a href="https://developer.apple.com/documentation/corefoundation">CoreFoundation Framework</a>
 */
public final class CFUtil {

    /**
     * 将 Java String 转换为 CFStringRef
     * <p>调用 {@code CFStringCreateWithCharacters} 创建 CF 字符串，
     * 使用 UTF-16 编码（与 Java String 内部编码一致）。</p>
     *
     * @param str Java 字符串
     * @return CFStringRef（调用者负责释放）
     */
    public static CFStringRef createCFString(String str) {
        char[] chars = str.toCharArray();
        return CoreFoundation.INSTANCE.CFStringCreateWithCharacters(
                null, chars, new CFIndex(chars.length));
    }

    /**
     * 将 CFStringRef 转换为 Java String
     *
     * @param cfString CFStringRef
     * @return Java 字符串，如果为 null 返回 null
     */
    public static String toJavaString(CFStringRef cfString) {
        if (cfString == null) return null;
        long length = CoreFoundation.INSTANCE.CFStringGetLength(cfString).longValue();
        long maxSize = CoreFoundation.INSTANCE.CFStringGetMaximumSizeForEncoding(
                new CFIndex(length), CoreFoundation.kCFStringEncodingUTF8).longValue();
        Memory buffer = new Memory(maxSize + 1);
        byte result = CoreFoundation.INSTANCE.CFStringGetCString(
                cfString, buffer, new CFIndex(buffer.size()),
                CoreFoundation.kCFStringEncodingUTF8);
        if (result != 0) {
            return buffer.getString(0, "UTF-8");
        }
        return null;
    }

    /**
     * 将 Pointer 包装为 CFStringRef 并提取字符串
     *
     * @param ptr CFStringRef 底层指针
     * @return Java 字符串
     */
    public static String pointerToJavaString(Pointer ptr) {
        if (ptr == null || ptr == Pointer.NULL) return null;
        CFStringRef ref = new CFStringRef();
        ref.setPointer(ptr);
        return toJavaString(ref);
    }

    /**
     * 获取 CFArray 的元素个数
     *
     * @param cfArray CFArrayRef
     * @return 元素个数
     */
    public static long getArrayCount(CFArrayRef cfArray) {
        if (cfArray == null) return 0;
        return CoreFoundation.INSTANCE.CFArrayGetCount(cfArray).longValue();
    }

    /**
     * 获取 CFArray 指定索引的元素（返回 Pointer）
     *
     * @param cfArray CFArrayRef
     * @param index   索引
     * @return 元素指针
     */
    public static Pointer getArrayValue(CFArrayRef cfArray, long index) {
        return CoreFoundation.INSTANCE.CFArrayGetValueAtIndex(cfArray, new CFIndex(index));
    }

    /**
     * 获取 CFArray 的元素个数（Pointer 版本）
     *
     * @param cfArrayPtr CFArrayRef 底层指针
     * @return 元素个数
     */
    public static long getArrayCount(Pointer cfArrayPtr) {
        if (cfArrayPtr == null || cfArrayPtr == Pointer.NULL) return 0;
        CFArrayRef ref = new CFArrayRef();
        ref.setPointer(cfArrayPtr);
        return getArrayCount(ref);
    }

    /**
     * 获取 CFArray 指定索引的元素（Pointer 版本）
     *
     * @param cfArrayPtr CFArrayRef 底层指针
     * @param index      索引
     * @return 元素指针
     */
    public static Pointer getArrayValue(Pointer cfArrayPtr, long index) {
        CFArrayRef ref = new CFArrayRef();
        ref.setPointer(cfArrayPtr);
        return getArrayValue(ref, index);
    }

    /**
     * 判断 CFBoolean 是否为 true
     *
     * @param cfBooleanPtr CFBooleanRef 底层指针
     * @return 如果为 true 返回 true
     */
    public static boolean isCFBooleanTrue(Pointer cfBooleanPtr) {
        if (cfBooleanPtr == null || cfBooleanPtr == Pointer.NULL) return false;
        CFBooleanRef boolRef = new CFBooleanRef();
        boolRef.setPointer(cfBooleanPtr);
        return CoreFoundation.INSTANCE.CFBooleanGetValue(boolRef) != 0;
    }

    /**
     * 安全释放 CF 对象（CFTypeRef 版本）
     *
     * @param cfTypeRef CFTypeRef（可以为 null）
     */
    public static void release(CFTypeRef cfTypeRef) {
        if (cfTypeRef != null) {
            CoreFoundation.INSTANCE.CFRelease(cfTypeRef);
        }
    }

    /**
     * 安全释放 CF 对象（Pointer 版本）
     *
     * @param ptr CFTypeRef 底层指针
     */
    public static void release(Pointer ptr) {
        if (ptr != null && ptr != Pointer.NULL) {
            CFTypeRef ref = new CFTypeRef();
            ref.setPointer(ptr);
            CoreFoundation.INSTANCE.CFRelease(ref);
        }
    }

    /**
     * 从 CFNumber 提取 double 值
     *
     * @param cfNumberPtr CFNumberRef 底层指针
     * @return double 值
     */
    public static double toDouble(Pointer cfNumberPtr) {
        if (cfNumberPtr == null || cfNumberPtr == Pointer.NULL) return 0.0;
        double[] value = new double[1];
        ApplicationServices.INSTANCE.CFNumberGetValue(cfNumberPtr, 13, value);
        return value[0];
    }

    private CFUtil() {
        // 工具类禁止实例化
    }
}
