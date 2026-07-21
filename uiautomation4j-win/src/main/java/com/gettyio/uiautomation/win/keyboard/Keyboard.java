package com.gettyio.uiautomation.win.keyboard;

import com.sun.jna.Native;

/**
 * 键盘操作工具类 - 通过 Win32 keybd_event API 实现
 */
public class Keyboard {

    /**
     * 自定义 JNA 接口，用于调用 JNA User32 中不存在的函数
     */
    private interface User32Ext extends com.sun.jna.Library {
        void keybd_event(byte bVk, byte bScan, int dwFlags, int dwExtraInfo);
        short VkKeyScanW(char ch);
    }

    private static final User32Ext USER32EXT = Native.load("user32", User32Ext.class);

    private static final int VK_BACK = 0x08;
    private static final int VK_TAB = 0x09;
    private static final int VK_RETURN = 0x0D;
    private static final int VK_SHIFT = 0x10;
    private static final int VK_CONTROL = 0x11;
    private static final int VK_MENU = 0x12;
    private static final int VK_ESCAPE = 0x1B;
    private static final int VK_SPACE = 0x20;
    private static final int VK_LEFT = 0x25;
    private static final int VK_UP = 0x26;
    private static final int VK_RIGHT = 0x27;
    private static final int VK_DOWN = 0x28;
    private static final int VK_DELETE = 0x2E;
    private static final int VK_PRIOR = 0x21;
    private static final int VK_NEXT = 0x22;
    private static final int VK_END = 0x23;
    private static final int VK_HOME = 0x24;
    private static final int VK_F1 = 0x70;
    private static final int VK_F2 = 0x71;
    private static final int VK_F3 = 0x72;
    private static final int VK_F4 = 0x73;
    private static final int VK_F5 = 0x74;
    private static final int VK_F6 = 0x75;
    private static final int VK_F7 = 0x76;
    private static final int VK_F8 = 0x77;
    private static final int VK_F9 = 0x78;
    private static final int VK_F10 = 0x79;
    private static final int VK_F11 = 0x7A;
    private static final int VK_F12 = 0x7B;
    private static final int KEYEVENTF_KEYUP = 0x0002;

    public static void sendKeys(String keys) {
        if (keys == null || keys.isEmpty()) return;
        int i = 0;
        while (i < keys.length()) {
            char c = keys.charAt(i);
            if (c == '{') {
                int end = keys.indexOf('}', i);
                if (end == -1) throw new IllegalArgumentException("未匹配的 '{': " + keys);
                sendSpecialKey(keys.substring(i + 1, end));
                i = end + 1;
            } else {
                sendChar(c);
                i++;
            }
        }
    }

    private static void sendSpecialKey(String key) {
        switch (key.toLowerCase()) {
            case "ctrl": case "control": pressKey(VK_CONTROL); break;
            case "alt": pressKey(VK_MENU); break;
            case "shift": pressKey(VK_SHIFT); break;
            case "enter": case "return": pressKey(VK_RETURN); break;
            case "tab": pressKey(VK_TAB); break;
            case "esc": case "escape": pressKey(VK_ESCAPE); break;
            case "del": case "delete": pressKey(VK_DELETE); break;
            case "backspace": case "bs": pressKey(VK_BACK); break;
            case "space": pressKey(VK_SPACE); break;
            case "up": pressKey(VK_UP); break;
            case "down": pressKey(VK_DOWN); break;
            case "left": pressKey(VK_LEFT); break;
            case "right": pressKey(VK_RIGHT); break;
            case "home": pressKey(VK_HOME); break;
            case "end": pressKey(VK_END); break;
            case "pageup": pressKey(VK_PRIOR); break;
            case "pagedown": pressKey(VK_NEXT); break;
            case "f1": pressKey(VK_F1); break; case "f2": pressKey(VK_F2); break;
            case "f3": pressKey(VK_F3); break; case "f4": pressKey(VK_F4); break;
            case "f5": pressKey(VK_F5); break; case "f6": pressKey(VK_F6); break;
            case "f7": pressKey(VK_F7); break; case "f8": pressKey(VK_F8); break;
            case "f9": pressKey(VK_F9); break; case "f10": pressKey(VK_F10); break;
            case "f11": pressKey(VK_F11); break; case "f12": pressKey(VK_F12); break;
            default: throw new IllegalArgumentException("未知特殊键: " + key);
        }
    }

    private static void pressKey(int vkCode) {
        keyDown(vkCode);
        keyUp(vkCode);
    }

    private static void keyDown(int vkCode) {
        USER32EXT.keybd_event((byte) vkCode, (byte) 0, 0, 0);
    }

    private static void keyUp(int vkCode) {
        USER32EXT.keybd_event((byte) vkCode, (byte) 0, KEYEVENTF_KEYUP, 0);
    }

    private static void sendChar(char c) {
        short vkScan = USER32EXT.VkKeyScanW(c);
        int vk = vkScan & 0xFF;
        int modifiers = (vkScan >> 8) & 0xFF;
        if ((modifiers & 1) != 0) keyDown(VK_SHIFT);
        if ((modifiers & 2) != 0) keyDown(VK_CONTROL);
        if ((modifiers & 4) != 0) keyDown(VK_MENU);
        pressKey(vk);
        if ((modifiers & 4) != 0) keyUp(VK_MENU);
        if ((modifiers & 2) != 0) keyUp(VK_CONTROL);
        if ((modifiers & 1) != 0) keyUp(VK_SHIFT);
    }
}
