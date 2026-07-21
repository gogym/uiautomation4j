package com.gettyio.uiautomation.linux;

import com.gettyio.uiautomation.control.Control;

/**
 * Linux UIAutomation 后端初始化入口
 *
 * <p>等价于 Windows 平台的 {@code WinAutomation} 和 macOS 平台的 {@code MacAutomation}。
 * 调用 {@link #init()} 后，即可使用 Control 的各种搜索方法在 Linux 上查找和操作 UI 元素。</p>
 *
 * <h3>使用示例：</h3>
 * <pre>
 * // 初始化 Linux 后端
 * LinuxAutomation.init();
 *
 * // 搜索窗口
 * WindowControl window = Control.window().name("Firefox").findWindow();
 *
 * // 搜索按钮并点击
 * window.child("Back").click();
 *
 * // 使用完毕后释放资源
 * LinuxAutomation.shutdown();
 * </pre>
 *
 * <h3>前置条件：</h3>
 * <ul>
 *   <li>系统需要安装 at-spi2-core（大多数 Linux 桌面发行版已预装）</li>
 *   <li>需要运行图形桌面环境（GNOME、KDE 等）</li>
 *   <li>某些应用可能需要启用 ATK/AT-SPI 支持</li>
 * </ul>
 *
 * @see com.gettyio.uiautomation.mac.MacAutomation
 */
public class LinuxAutomation {

    /** 标记是否已初始化，防止重复初始化 */
    private static boolean initialized = false;

    /** LinuxControlBackend 实例 */
    private static LinuxControlBackend backend;

    /**
     * 初始化 Linux UIAutomation 后端
     *
     * <p>创建 {@link LinuxControlBackend} 实例并通过 SPI 机制注册到 core 模块的 {@link Control} 框架。
     * 调用此方法后，即可使用 Control 的各种搜索方法。</p>
     *
     * @throws IllegalStateException 如果无法连接到 AT-SPI2
     */
    public static synchronized void init() {
        if (initialized) {
            return;
        }

        // 创建后端并注册
        backend = new LinuxControlBackend();
        Control.registerBackend(backend);
        initialized = true;
    }

    /**
     * 释放资源并重置初始化状态
     *
     * <p>关闭 D-Bus 连接，将 initialized 标志重置为 false。
     * 调用后可以再次调用 {@link #init()} 重新初始化。</p>
     */
    public static synchronized void shutdown() {
        if (!initialized) {
            return;
        }
        if (backend != null && backend.getConnection() != null) {
            backend.getConnection().close();
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
     * 检查 AT-SPI2 是否可用
     *
     * <p>尝试连接 D-Bus 会话总线来验证 AT-SPI2 服务是否可用。</p>
     *
     * @return 如果 AT-SPI2 可用返回 true
     */
    public static boolean isAvailable() {
        try {
            LinuxControlBackend testBackend = new LinuxControlBackend();
            testBackend.getConnection().close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private LinuxAutomation() {
        // 工具类禁止实例化
    }
}
