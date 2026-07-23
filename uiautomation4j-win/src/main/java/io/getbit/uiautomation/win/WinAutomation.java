package io.getbit.uiautomation.win;

import io.getbit.uiautomation.control.Control;
import io.getbit.uiautomation.win.com.StaTaskExecutor;

/**
 * Windows UIAutomation 入口类
 *
 * <p>初始化 Windows 后端并注册到 Control 框架。</p>
 *
 * <p><b>线程安全：</b>本类内部维护一个专用 STA 线程执行器（{@link StaTaskExecutor}），
 * 所有 UIA COM 操作都委托到该 STA 线程执行，确保 COM 对象的线程亲和性。</p>
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
 *
 * // 程序退出时释放资源
 * WinAutomation.shutdown();
 * </pre>
 */
public class WinAutomation {

    /** 标记是否已初始化，防止重复初始化 */
    private static boolean initialized = false;

    /** STA 线程执行器，所有 UIA COM 操作都通过此执行器在专用 STA 线程中执行 */
    private static StaTaskExecutor staExecutor;

    /**
     * 初始化 Windows UIAutomation 后端
     *
     * <p>创建并启动 {@link StaTaskExecutor}（专用 STA 线程），然后在该线程中
     * 初始化 COM 并创建 {@link WinControlBackend} 实例，最后通过 SPI 机制注册到
     * core 模块的 {@link Control} 框架。</p>
     *
     * <p>调用此方法后，即可使用 Control 的各种搜索方法。
     * 该方法使用 synchronized 保证线程安全，且只初始化一次。</p>
     */
    public static synchronized void init() {
        if (!initialized) {
            staExecutor = new StaTaskExecutor();
            staExecutor.start();
            WinControlBackend backend = new WinControlBackend(staExecutor);
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
     *
     * <p>关闭 STA 线程执行器（内部会调用 {@code CoUninitialize()} 释放 COM 对象），
     * 并将 initialized 标志重置为 false。</p>
     */
    public static synchronized void shutdown() {
        if (initialized) {
            Control.registerBackend(null);
            if (staExecutor != null) {
                staExecutor.shutdown();
                staExecutor = null;
            }
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
