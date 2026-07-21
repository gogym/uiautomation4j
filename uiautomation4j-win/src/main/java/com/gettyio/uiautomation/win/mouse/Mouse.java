package com.gettyio.uiautomation.win.mouse;

import com.sun.jna.Native;

/**
 * 鼠标操作工具类 - 通过 Win32 mouse_event API 实现
 */
public class Mouse {

    /**
     * 自定义 JNA 接口，用于调用 JNA User32 中不存在的函数
     */
    private interface User32Ext extends com.sun.jna.Library {
        void mouse_event(int dwFlags, int dx, int dy, int dwData, int dwExtraInfo);
        boolean SetCursorPos(int x, int y);
    }

    private static final User32Ext USER32EXT = Native.load("user32", User32Ext.class);

    private static final int MOUSEEVENTF_LEFTDOWN = 0x0002;
    private static final int MOUSEEVENTF_LEFTUP = 0x0004;
    private static final int MOUSEEVENTF_RIGHTDOWN = 0x0008;
    private static final int MOUSEEVENTF_RIGHTUP = 0x0010;

    public static void click(int x, int y) {
        moveTo(x, y);
        USER32EXT.mouse_event(MOUSEEVENTF_LEFTDOWN, 0, 0, 0, 0);
        USER32EXT.mouse_event(MOUSEEVENTF_LEFTUP, 0, 0, 0, 0);
    }

    public static void doubleClick(int x, int y) {
        moveTo(x, y);
        USER32EXT.mouse_event(MOUSEEVENTF_LEFTDOWN, 0, 0, 0, 0);
        USER32EXT.mouse_event(MOUSEEVENTF_LEFTUP, 0, 0, 0, 0);
        USER32EXT.mouse_event(MOUSEEVENTF_LEFTDOWN, 0, 0, 0, 0);
        USER32EXT.mouse_event(MOUSEEVENTF_LEFTUP, 0, 0, 0, 0);
    }

    public static void rightClick(int x, int y) {
        moveTo(x, y);
        USER32EXT.mouse_event(MOUSEEVENTF_RIGHTDOWN, 0, 0, 0, 0);
        USER32EXT.mouse_event(MOUSEEVENTF_RIGHTUP, 0, 0, 0, 0);
    }

    public static void moveTo(int x, int y) {
        USER32EXT.SetCursorPos(x, y);
    }
}
