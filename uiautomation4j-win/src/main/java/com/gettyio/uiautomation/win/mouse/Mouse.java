package com.gettyio.uiautomation.win.mouse;

import com.sun.jna.Native;

/**
 * 鼠标操作工具类 - 通过 Win32 mouse_event API 实现
 *
 * <p>提供基本的鼠标操作：
 * <ul>
 *   <li>{@link #moveTo(int, int)} - 移动光标到指定位置</li>
 *   <li>{@link #click(int, int)} - 左键单击</li>
 *   <li>{@link #doubleClick(int, int)} - 左键双击</li>
 *   <li>{@link #rightClick(int, int)} - 右键单击</li>
 * </ul>
 *
 * <p>所有操作均基于屏幕绝对坐标（像素），原点 (0,0) 在屏幕左上角。
 *
 * <p>使用示例：
 * <pre>
 * // 点击坐标 (100, 200) 位置
 * Mouse.click(100, 200);
 *
 * // 右键点击
 * Mouse.rightClick(300, 400);
 * </pre>
 *
 * @see <a href="https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-mouse_event">mouse_event API</a>
 */
public class Mouse {

    /**
     * 自定义 JNA 接口，用于调用 JNA 内置 User32 中不存在的函数
     *
     * <p>包含两个本地 API：
     * <ul>
     *   <li>{@code mouse_event} - 模拟鼠标移动和按键事件</li>
     *   <li>{@code SetCursorPos} - 设置光标位置</li>
     * </ul>
     */
    private interface User32Ext extends com.sun.jna.Library {
        /**
         * 模拟鼠标事件（移动、按键、滚轮）
         *
         * @param dwFlags    事件标志（如 MOUSEEVENTF_LEFTDOWN、MOUSEEVENTF_LEFTUP 等）
         * @param dx         X 坐标或相对移动量
         * @param dy         Y 坐标或相对移动量
         * @param dwData     滚轮数据（仅滚轮事件使用）
         * @param dwExtraInfo 附加信息（通常为 0）
         */
        void mouse_event(int dwFlags, int dx, int dy, int dwData, int dwExtraInfo);

        /**
         * 将光标移动到屏幕指定位置
         *
         * @param x 屏幕 X 坐标（像素）
         * @param y 屏幕 Y 坐标（像素）
         * @return 成功返回 true
         */
        boolean SetCursorPos(int x, int y);
    }

    /** JNA 动态加载的 user32.dll 扩展接口实例 */
    private static final User32Ext USER32EXT = Native.load("user32", User32Ext.class);

    // ==================== 鼠标事件标志常量 ====================

    /** 鼠标左键按下事件标志 (0x0002) */
    private static final int MOUSEEVENTF_LEFTDOWN = 0x0002;
    /** 鼠标左键释放事件标志 (0x0004) */
    private static final int MOUSEEVENTF_LEFTUP = 0x0004;
    /** 鼠标右键按下事件标志 (0x0008) */
    private static final int MOUSEEVENTF_RIGHTDOWN = 0x0008;
    /** 鼠标右键释放事件标志 (0x0010) */
    private static final int MOUSEEVENTF_RIGHTUP = 0x0010;

    /**
     * 左键单击指定坐标
     * <p>先移动光标到目标位置，再模拟一次左键按下+释放
     *
     * @param x 屏幕 X 坐标（像素）
     * @param y 屏幕 Y 坐标（像素）
     */
    public static void click(int x, int y) {
        moveTo(x, y);
        USER32EXT.mouse_event(MOUSEEVENTF_LEFTDOWN, 0, 0, 0, 0);
        USER32EXT.mouse_event(MOUSEEVENTF_LEFTUP, 0, 0, 0, 0);
    }

    /**
     * 左键双击指定坐标
     * <p>先移动光标到目标位置，再模拟两次左键按下+释放
     *
     * @param x 屏幕 X 坐标（像素）
     * @param y 屏幕 Y 坐标（像素）
     */
    public static void doubleClick(int x, int y) {
        moveTo(x, y);
        USER32EXT.mouse_event(MOUSEEVENTF_LEFTDOWN, 0, 0, 0, 0);
        USER32EXT.mouse_event(MOUSEEVENTF_LEFTUP, 0, 0, 0, 0);
        USER32EXT.mouse_event(MOUSEEVENTF_LEFTDOWN, 0, 0, 0, 0);
        USER32EXT.mouse_event(MOUSEEVENTF_LEFTUP, 0, 0, 0, 0);
    }

    /**
     * 右键单击指定坐标
     * <p>先移动光标到目标位置，再模拟一次右键按下+释放
     *
     * @param x 屏幕 X 坐标（像素）
     * @param y 屏幕 Y 坐标（像素）
     */
    public static void rightClick(int x, int y) {
        moveTo(x, y);
        USER32EXT.mouse_event(MOUSEEVENTF_RIGHTDOWN, 0, 0, 0, 0);
        USER32EXT.mouse_event(MOUSEEVENTF_RIGHTUP, 0, 0, 0, 0);
    }

    /**
     * 移动光标到指定屏幕坐标
     * <p>调用 Win32 SetCursorPos API 设置光标绝对位置
     *
     * @param x 屏幕 X 坐标（像素，原点在屏幕左上角）
     * @param y 屏幕 Y 坐标（像素，原点在屏幕左上角）
     */
    public static void moveTo(int x, int y) {
        USER32EXT.SetCursorPos(x, y);
    }
}
