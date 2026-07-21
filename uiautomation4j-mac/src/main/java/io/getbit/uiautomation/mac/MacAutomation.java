package io.getbit.uiautomation.mac;

import io.getbit.uiautomation.control.Control;
import io.getbit.uiautomation.mac.ax.ApplicationServices;

/**
 * macOS UIAutomation 后端初始化入口
 *
 * <p>等价于 Windows 平台的 {@code WinAutomation}。调用 {@link #init()} 后，
 * 即可使用 Control 的各种搜索方法在 macOS 上查找和操作 UI 元素。</p>
 *
 * <h3>使用示例：</h3>
 * <pre>
 * // 初始化 macOS 后端
 * MacAutomation.init();
 *
 * // 搜索窗口
 * WindowControl window = Control.window().name("Safari").findWindow();
 *
 * // 搜索按钮并点击
 * window.child("Back").click();
 *
 * // 使用完毕后释放资源
 * MacAutomation.shutdown();
 * </pre>
 *
 * <h3>前置条件：</h3>
 * <ul>
 *   <li>应用必须获得"辅助功能"权限（系统设置 → 隐私与安全 → 辅助功能）</li>
 *   <li>通过 {@link ApplicationServices#AXIsProcessTrusted()} 检查权限状态</li>
 * </ul>
 *
 * @see io.getbit.uiautomation.win.WinAutomation
 */
public class MacAutomation {

    /** 标记是否已初始化，防止重复初始化 */
    private static boolean initialized = false;

    /** MacControlBackend 实例 */
    private static MacControlBackend backend;

    /**
     * 初始化 macOS UIAutomation 后端
     *
     * <p>创建 {@link MacControlBackend} 实例并通过 SPI 机制注册到 core 模块的 {@link Control} 框架。
     * 调用此方法后，即可使用 Control 的各种搜索方法。</p>
     *
     * <p>该方法会检查辅助功能权限，如果未授权会打印警告信息。</p>
     *
     * @throws IllegalStateException 如果辅助功能权限未授予
     */
    public static synchronized void init() {
        if (initialized) {
            return;
        }

        // 检查辅助功能权限
        boolean trusted = ApplicationServices.INSTANCE.AXIsProcessTrusted();
        if (!trusted) {
            System.err.println("[警告] 当前进程未获得辅助功能权限！");
            System.err.println("请在 系统设置 → 隐私与安全 → 辅助功能 中添加并授权此应用。");
            System.err.println("授权后可能需要重启应用。");
        }

        // 创建后端并注册
        backend = new MacControlBackend();
        Control.registerBackend(backend);
        initialized = true;
    }

    /**
     * 释放资源并重置初始化状态
     *
     * <p>释放系统级 AXUIElement，将 initialized 标志重置为 false。
     * 调用后可以再次调用 {@link #init()} 重新初始化。</p>
     */
    public static synchronized void shutdown() {
        if (!initialized) {
            return;
        }
        backend = null;
        initialized = false;
    }

    /**
     * 检查是否已初始化
     *
     * @return 如果已初始化返回 true
     */
    public static boolean isInitialized() {
        return initialized;
    }

    /**
     * 检查当前进程是否被授权辅助功能访问
     *
     * <p>macOS 要求用户在"系统设置 → 隐私与安全 → 辅助功能"中授权应用。
     * 如果未授权，所有 AX 调用都会返回权限错误。</p>
     *
     * @return 如果已授权返回 true
     */
    public static boolean isTrusted() {
        return ApplicationServices.INSTANCE.AXIsProcessTrusted();
    }

    private MacAutomation() {
        // 工具类禁止实例化
    }
}
