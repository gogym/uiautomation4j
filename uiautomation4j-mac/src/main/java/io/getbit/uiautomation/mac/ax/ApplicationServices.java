package io.getbit.uiautomation.mac.ax;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

/**
 * macOS ApplicationServices 框架 JNA 绑定
 *
 * <p>提供对 macOS Accessibility API (AXUIElement) 的底层函数调用。
 * 这些函数是 macOS UI 自动化的核心，等价于 Windows 的 IUIAutomation COM 接口。</p>
 *
 * <h3>核心函数分组：</h3>
 * <h4>1. 元素创建（入口点）</h4>
 * <ul>
 *   <li>{@link #AXUIElementCreateSystemWide} - 创建系统级根元素（等价于 Windows 的桌面根元素）</li>
 *   <li>{@link #AXUIElementCreateApplication} - 通过 PID 创建指定应用的根元素</li>
 * </ul>
 *
 * <h4>2. 属性访问</h4>
 * <ul>
 *   <li>{@link #AXUIElementCopyAttributeValue} - 获取属性值（Copy 语义，需释放）</li>
 *   <li>{@link #AXUIElementSetAttributeValue} - 设置属性值（用于窗口操作等）</li>
 *   <li>{@link #AXUIElementCopyAttributeNames} - 获取元素支持的所有属性名</li>
 * </ul>
 *
 * <h4>3. 动作执行</h4>
 * <ul>
 *   <li>{@link #AXUIElementPerformAction} - 执行动作（如 Press、Confirm、Increment）</li>
 *   <li>{@link #AXUIElementCopyActionNames} - 获取元素支持的所有动作名</li>
 * </ul>
 *
 * <h4>4. 多属性批量访问</h4>
 * <ul>
 *   <li>{@link #AXUIElementCopyAttributeValues} - 批量获取多个属性值</li>
 * </ul>
 *
 * <h4>5. 进程级操作</h4>
 * <ul>
 *   <li>{@link #AXIsProcessTrusted} - 检查当前进程是否被授权辅助功能</li>
 *   <li>{@link #AXUIElementCopyElementAtPosition} - 获取屏幕指定坐标处的元素</li>
 * </ul>
 *
 * <p>所有 AX 函数返回 {@code AXError} 错误码（0 表示成功）。</p>
 *
 * @see <a href="https://developer.apple.com/documentation/applicationservices/axuielement_h">AXUIElement Reference</a>
 */
public interface ApplicationServices extends Library {

    /**
     * JNA 单例实例
     * <p>加载 macOS ApplicationServices.framework</p>
     */
    ApplicationServices INSTANCE = Native.load("ApplicationServices", ApplicationServices.class);

    // ==================== AXUIElement 创建（入口点） ====================

    /**
     * 创建系统级 AXUIElement（根元素）
     * <p>返回的 AXUIElement 代表整个桌面，是所有窗口和应用的祖先。
     * 等价于 Windows UIAutomation 中的 {@code IUIAutomation::GetRootElement}。</p>
     *
     * <p>返回值通过 out 参数返回，调用者负责 {@code CFRelease}。</p>
     *
     * @return AXUIElementRef 指针（系统级根元素）
     */
    Pointer AXUIElementCreateSystemWide();

    /**
     * 通过进程 PID 创建应用级 AXUIElement
     * <p>返回的 AXUIElement 代表指定应用的根节点，可以遍历其子窗口和控件。
     * 等价于 Windows 中通过 PID 查找顶层窗口。</p>
     *
     * @param pid 目标应用的进程 ID
     * @return AXUIElementRef 指针（应用级根元素），调用者负责 CFRelease
     */
    Pointer AXUIElementCreateApplication(int pid);

    // ==================== AXUIElement 属性访问 ====================

    /**
     * 获取 AXUIElement 的单个属性值
     * <p>核心函数，等价于 Windows 的 {@code IUIAutomationElement::get_CurrentXXX}。
     * 通过属性名（CFString）查询属性值，返回值为 CFTypeRef（可能是 CFString、CFArray、
     * CFBoolean、AXValue 等类型）。</p>
     *
     * <p><b>Copy 语义：</b>返回的 {@code *value} 需要调用 {@code CFRelease} 释放。</p>
     *
     * <p>常用属性名（定义在 {@link AXAttribute}）：
     * <ul>
     *   <li>{@code kAXRoleAttribute} - 控件角色（类似 Windows ControlType）</li>
     *   <li>{@code kAXTitleAttribute} - 标题/名称</li>
     *   <li>{@code kAXValueAttribute} - 当前值</li>
     *   <li>{@code kAXChildrenAttribute} - 子元素数组</li>
     *   <li>{@code kAXPositionAttribute} - 屏幕坐标（AXValue → CGPoint）</li>
     *   <li>{@code kAXSizeAttribute} - 尺寸（AXValue → CGSize）</li>
     * </ul>
     *
     * @param element    AXUIElementRef 指针
     * @param attribute  属性名（CFStringRef）
     * @param value      [out] 属性值（CFTypeRef*），调用者负责释放
     * @return AXError 错误码（0 = 成功）
     */
    int AXUIElementCopyAttributeValue(Pointer element, Pointer attribute, PointerByReference value);

    /**
     * 设置 AXUIElement 的属性值
     * <p>用于执行窗口操作（如设置 {@code kAXMinimizedAttribute} = true 最小化窗口）。
     * 等价于 Windows 的 WindowPattern::SetWindowVisualState。</p>
     *
     * @param element    AXUIElementRef 指针
     * @param attribute  属性名（CFStringRef）
     * @param value      新值（CFTypeRef）
     * @return AXError 错误码（0 = 成功）
     */
    int AXUIElementSetAttributeValue(Pointer element, Pointer attribute, Pointer value);

    /**
     * 获取 AXUIElement 支持的所有属性名列表
     * <p>返回 CFArrayRef，包含所有可查询的属性名（CFString）。
     * 用于调试和动态发现元素能力。</p>
     *
     * @param element      AXUIElementRef 指针
     * @param attributeNames [out] 属性名数组（CFArrayRef*），调用者负责释放
     * @return AXError 错误码（0 = 成功）
     */
    int AXUIElementCopyAttributeNames(Pointer element, PointerByReference attributeNames);

    /**
     * 批量获取 AXUIElement 的多个属性值
     *
     * @param element    AXUIElementRef 指针
     * @param attributes 属性名数组（CFArrayRef）
     * @param index      起始索引
     * @param maxValues  最大获取数量
     * @param values     [out] 属性值数组（CFArrayRef*），调用者负责释放
     * @return AXError 错误码（0 = 成功）
     */
    int AXUIElementCopyAttributeValues(Pointer element, Pointer attributes,
                                       long index, long maxValues, PointerByReference values);

    // ==================== AXUIElement 动作执行 ====================

    /**
     * 在 AXUIElement 上执行指定动作
     * <p>等价于 Windows 的 Pattern 操作（InvokePattern::Invoke、WindowPattern::Close 等）。
     * macOS 的动作是字符串名称，常用动作定义在 {@link AXAction}。</p>
     *
     * <p>常用动作：
     * <ul>
     *   <li>{@code kAXPressAction} - 按下按钮（等价于 Windows InvokePattern::Invoke）</li>
     *   <li>{@code kAXConfirmAction} - 确认操作</li>
     *   <li>{@code kAXCancelAction} - 取消操作</li>
     *   <li>{@code kAXIncrementAction} - 增加值（等价于 Windows Spinner 操作）</li>
     *   <li>{@code kAXDecrementAction} - 减少值</li>
     * </ul>
     *
     * @param element AXUIElementRef 指针
     * @param action  动作名（CFStringRef）
     * @return AXError 错误码（0 = 成功）
     */
    int AXUIElementPerformAction(Pointer element, Pointer action);

    /**
     * 获取 AXUIElement 支持的所有动作名列表
     * <p>返回 CFArrayRef，包含所有可执行的动作名（CFString）。
     * 用于调试和动态发现元素能力。</p>
     *
     * @param element   AXUIElementRef 指针
     * @param actionNames [out] 动作名数组（CFArrayRef*），调用者负责释放
     * @return AXError 错误码（0 = 成功）
     */
    int AXUIElementCopyActionNames(Pointer element, PointerByReference actionNames);

    // ==================== 进程级操作 ====================

    /**
     * 检查当前进程是否被授权辅助功能访问
     * <p>macOS 要求用户在"系统设置 → 隐私与安全 → 辅助功能"中授权应用。
     * 如果未授权，所有 AX 调用都会返回权限错误。</p>
     *
     * @return 如果已授权返回 true
     */
    boolean AXIsProcessTrusted();

    /**
     * 获取屏幕指定坐标处的 UI 元素
     * <p>等价于 Windows 的 {@code IUIAutomation::ElementFromPoint}。
     * 用于实现"鼠标悬停检测"等功能。</p>
     *
     * @param x       屏幕 X 坐标
     * @param y       屏幕 Y 坐标
     * @param element [out] AXUIElementRef 指针，调用者负责释放
     * @return AXError 错误码（0 = 成功）
     */
    int AXUIElementCopyElementAtPosition(float x, float y, PointerByReference element);

    // ==================== AXValue 操作（几何类型） ====================

    /**
     * 从 AXValue 提取底层值到缓冲区
     * <p>AXValue 是 macOS 用于封装 CGPoint、CGSize、CGRect 等几何类型的容器。
     * 通过此函数可以将 AXValue 转换为 C 结构体（在 Java 中通过 double[] 接收）。</p>
     *
     * <p>AXValue 类型常量：
     * <ul>
     *   <li>kAXValueCGPointType (1) → CGPoint {x: double, y: double}</li>
     *   <li>kAXValueCGSizeType (2) → CGSize {width: double, height: double}</li>
     *   <li>kAXValueCGRectType (3) → CGRect {x: double, y: double, width: double, height: double}</li>
     * </ul>
     *
     * @param value AXValueRef 指针
     * @param type  值类型（1=CGPoint, 2=CGSize, 3=CGRect）
     * @param out   [out] 输出缓冲区（double[]）
     * @return 如果成功返回 true
     */
    boolean AXValueGetValue(Pointer value, int type, double[] out);

    // ==================== CoreFoundation 补充（CFNumber） ====================

    /**
     * 从 CFNumber 获取数值
     * <p>JNA 未内置此函数，通过 ApplicationServices 框架间接调用。</p>
     *
     * @param number CFNumberRef 指针
     * @param type   数值类型（13 = kCFNumberDoubleType）
     * @param value  [out] 输出缓冲区（double[1]）
     * @return 如果成功返回 true
     */
    boolean CFNumberGetValue(Pointer number, int type, double[] value);

    /**
     * 从 CFNumber 获取整数值
     * <p>JNA 未内置此函数，通过 ApplicationServices 框架间接调用。</p>
     *
     * @param number CFNumberRef 指针
     * @param type   数值类型（9 = kCFNumberIntType）
     * @param value  [out] 输出缓冲区（int[1]）
     * @return 如果成功返回 true
     */
    boolean CFNumberGetValue(Pointer number, int type, int[] value);

    // ==================== 辅助功能选项 ====================

    /**
     * 设置辅助功能选项（如启用全键盘访问）
     *
     * @param options CFDictionaryRef 选项字典
     * @return AXError 错误码
     */
    int AXUIElementCopyParameterizedAttributeValue(
            Pointer element, Pointer attribute, Pointer parameter, PointerByReference value);

    // ==================== AXError 错误码常量 ====================

    /** 操作成功 */
    int kAXErrorSuccess = 0;
    /** 参数无效 */
    int kAXErrorFailure = -25200;
    /** 参数无效 */
    int kAXErrorInvalidUIElement = -25201;
    /** 参数无效 */
    int kAXErrorInvalidUIElementObserver = -25202;
    /** 无法获取属性值 */
    int kAXErrorCannotComplete = -25204;
    /** 属性不存在 */
    int kAXErrorAttributeUnsupported = -25203;
    /** 动作不支持 */
    int kAXErrorActionUnsupported = -25205;
    /** 通知不支持 */
    int kAXErrorNotificationUnsupported = -25206;
    /** 未实现 */
    int kAXErrorNotImplementedized = -25207;
    /** 参数无效 */
    int kAXErrorInvalidParameter = -25208;
    /** 不在主线程 */
    int kAXErrorIllegalAction = -25210;
}
