package com.gettyio.uiautomation.win;

import com.gettyio.uiautomation.control.Control;

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

    private static boolean initialized = false;

    /**
     * 初始化 Windows UIAutomation 后端
     * 调用此方法后，即可使用 Control 的各种搜索方法
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
     * 释放 COM 资源
     */
    public static synchronized void shutdown() {
        if (initialized) {
            com.gettyio.uiautomation.win.com.Win32Util.uninitCOM();
            initialized = false;
        }
    }

    private static void ensureInitialized() {
        if (!initialized) {
            throw new IllegalStateException("WinAutomation 未初始化，请先调用 WinAutomation.init()");
        }
    }
}
