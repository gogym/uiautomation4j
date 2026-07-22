package io.getbit.uiautomation.win;

import io.getbit.uiautomation.control.Control;

/**
 * Windows UIAutomation 入口类
 * 初始化 Windows 后端并注册到 Control 框架
 *
 * <pre>
 * // 使用示例：
 * WinAutomation.init();
 *
 * // 查找记事本窗口
 * WindowControl notepad = Control.window()
 *     .searchDepth(1)
 *     .className("Notepad")
 *     .findWindow();
 *
 * // 操作编辑框
 * notepad.findEdit().sendKeys("Hello World");
 * </pre>
 */
public class WinAutomation {

    /** 标记是否已初始化，防止重复初始化 */
    private static boolean initialized = false;

    /**
     * 初始化 Windows UIAutomation 后端
     * <p>创建 {@link WinControlBackend} 实例并通过 SPI 机制注册到 core 模块的 {@link Control} 框架。
     * 调用此方法后，即可使用 Control 的各种搜索方法。
     * <p>该方法使用 synchronized 保证线程安全，且只初始化一次。
     */
    public static synchronized void init() {
        if (!initialized) {
            WinControlBackend backend = new WinControlBackend();
            Control.registerBackend(backend);
            initialized = true;
        }
    }

    /**
     * 设置全局搜索超时时间（秒）
     *
     * @param seconds 超时秒数
     */
    public static void setGlobalSearchTimeout(int seconds) {
        ensureInitialized();
        Control.getBackend().setGlobalSearchTimeout(seconds);
    }

    /**
     * 释放 COM 资源并重置初始化状态
     * <p>调用 {@link io.getbit.uiautomation.win.com.Win32Util#uninitCOM()} 释放
     * IUIAutomation COM 对象，并将 initialized 标志重置为 false。
     */
    public static synchronized void shutdown() {
        if (initialized) {
            io.getbit.uiautomation.win.com.Win32Util.uninitCOM();
            Control.registerBackend(null);
            initialized = false;
        }
    }

    /**
     * 检查是否已初始化，未初始化则抛出异常
     *
     * @throws IllegalStateException 如果未调用 {@link #init()}
     */
    private static void ensureInitialized() {
        if (!initialized) {
            throw new IllegalStateException("WinAutomation 未初始化，请先调用 WinAutomation.init()");
        }
    }
}
