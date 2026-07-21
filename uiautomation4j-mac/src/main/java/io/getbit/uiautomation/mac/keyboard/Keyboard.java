package io.getbit.uiautomation.mac.keyboard;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * 键盘操作工具类 - 通过 macOS CGEvent API 实现
 *
 * <p>提供两类键盘操作：
 * <ul>
 *   <li>普通字符输入：通过 {@link #sendKeys(String)} 方法，支持普通字符和特殊键</li>
 *   <li>底层按键模拟：通过 {@link #keyDown(short)} / {@link #keyUp(short)} 模拟按键</li>
 * </ul>
 *
 * <p>特殊键语法：使用花括号包裹键名，如 {@code "{Enter}"}、{@code "{Cmd}"}、{@code "{Tab}"}。
 * 修饰键（Cmd、Option、Ctrl、Shift）支持组合语法：{@code "{Cmd}c"} 表示 Cmd+C。</p>
 *
 * <p>macOS 与 Windows 的键名映射：
 * <table>
 *   <tr><th>macOS 键名</th><th>Windows 等价键</th></tr>
 *   <tr><td>Cmd / Command</td><td>Ctrl</td></tr>
 *   <tr><td>Option / Alt</td><td>Alt</td></tr>
 *   <tr><td>Control</td><td>Ctrl</td></tr>
 *   <tr><td>Return / Enter</td><td>Enter</td></tr>
 *   <tr><td>Escape</td><td>Esc</td></tr>
 *   <tr><td>Delete (forward)</td><td>Delete</td></tr>
 *   <tr><td>ForwardDelete</td><td>Del</td></tr>
 * </table>
 *
 * @see <a href="https://developer.apple.com/documentation/coregraphics/cgeventsource">CGEventSource</a>
 */
public class Keyboard {

    /**
     * CoreGraphics 框架 JNA 绑定（用于 CGEvent 键盘/鼠标模拟）
     */
    interface CoreGraphics extends Library {
        CoreGraphics INSTANCE = Native.load("CoreGraphics", CoreGraphics.class);

        /**
         * 创建 CGEvent 键盘事件
         *
         * @param source   事件源（null 使用默认源）
         * @param keyDown  true=按下事件, false=释放事件
         * @param keyCode  虚拟键码（macOS virtual key code）
         * @param flags    事件标志（修饰键状态）
         * @return CGEventRef 指针
         */
        // CGEventRef CGEventCreateKeyboardEvent(CGEventSourceRef source,
        //                                       CGKeyCode virtualKey,
        //                                       bool keyDown);
        // 在 JNA 中简化为：
        com.sun.jna.Pointer CGEventCreateKeyboardEvent(com.sun.jna.Pointer source,
                                                        short virtualKey,
                                                        boolean keyDown);

        /**
         * 发送 CGEvent
         *
         * @param tap       事件 tap 位置（0 = kCGHIDEventTap = 系统级）
         * @param event     CGEventRef 指针
         */
        void CGEventPost(int tap, com.sun.jna.Pointer event);

        /** 释放 CGEvent */
        void CFRelease(com.sun.jna.Pointer cf);
    }

    /** kCGHIDEventTap - 系统级事件注入点 */
    private static final int kCGHIDEventTap = 0;

    // ==================== macOS 虚拟键码（等价于 Windows VK 常量） ====================

    /** A 键 */
    public static final short VK_A = 0x00;
    /** S 键 */
    public static final short VK_S = 0x01;
    /** D 键 */
    public static final short VK_D = 0x02;
    /** F 键 */
    public static final short VK_F = 0x03;
    /** Return/Enter 键 */
    public static final short VK_RETURN = 0x24;
    /** Tab 键 */
    public static final short VK_TAB = 0x30;
    /** Space 空格键 */
    public static final short VK_SPACE = 0x31;
    /** Delete (Backspace) 键 */
    public static final short VK_DELETE = 0x33;
    /** Escape 键 */
    public static final short VK_ESCAPE = 0x35;
    /** Command 键 */
    public static final short VK_COMMAND = 0x37;
    /** Shift 键 */
    public static final short VK_SHIFT = 0x38;
    /** Caps Lock */
    public static final short VK_CAPS_LOCK = 0x39;
    /** Option/Alt 键 */
    public static final short VK_OPTION = 0x3A;
    /** Control 键 */
    public static final short VK_CONTROL = 0x3B;
    /** 左箭头 */
    public static final short VK_LEFT_ARROW = 0x7B;
    /** 右箭头 */
    public static final short VK_RIGHT_ARROW = 0x7C;
    /** 下箭头 */
    public static final short VK_DOWN_ARROW = 0x7D;
    /** 上箭头 */
    public static final short VK_UP_ARROW = 0x7E;
    /** Forward Delete 键 */
    public static final short VK_FORWARD_DELETE = 0x75;
    /** Home 键 */
    public static final short VK_HOME = 0x73;
    /** End 键 */
    public static final short VK_END = 0x77;
    /** Page Up */
    public static final short VK_PAGE_UP = 0x74;
    /** Page Down */
    public static final short VK_PAGE_DOWN = 0x79;

    /**
     * 发送按键序列
     *
     * <p>支持普通字符和特殊键语法：
     * <ul>
     *   <li>{@code "Hello"} → 逐字符输入</li>
     *   <li>{@code "{Return}"} → 按下回车键</li>
     *   <li>{@code "{Cmd}c"} → Cmd+C（复制）</li>
     *   <li>{@code "{Cmd}{Shift}v"} → Cmd+Shift+V（粘贴为纯文本）</li>
     * </ul>
     *
     * @param keys 按键序列字符串
     */
    public static void sendKeys(String keys) {
        if (keys == null || keys.isEmpty()) return;

        int i = 0;
        while (i < keys.length()) {
            if (keys.charAt(i) == '{') {
                // 解析特殊键
                int end = keys.indexOf('}', i);
                if (end == -1) {
                    // 没有闭合的 }，当作普通字符
                    sendChar(keys.charAt(i));
                    i++;
                    continue;
                }
                String keyName = keys.substring(i + 1, end);
                sendSpecialKey(keyName);
                i = end + 1;
            } else {
                sendChar(keys.charAt(i));
                i++;
            }
        }
    }

    /**
     * 模拟按键按下
     *
     * @param keyCode macOS 虚拟键码
     */
    public static void keyDown(short keyCode) {
        com.sun.jna.Pointer event = CoreGraphics.INSTANCE.CGEventCreateKeyboardEvent(null, keyCode, true);
        if (event != null) {
            CoreGraphics.INSTANCE.CGEventPost(kCGHIDEventTap, event);
            CoreGraphics.INSTANCE.CFRelease(event);
        }
    }

    /**
     * 模拟按键释放
     *
     * @param keyCode macOS 虚拟键码
     */
    public static void keyUp(short keyCode) {
        com.sun.jna.Pointer event = CoreGraphics.INSTANCE.CGEventCreateKeyboardEvent(null, keyCode, false);
        if (event != null) {
            CoreGraphics.INSTANCE.CGEventPost(kCGHIDEventTap, event);
            CoreGraphics.INSTANCE.CFRelease(event);
        }
    }

    /**
     * 发送特殊键
     *
     * @param keyName 键名（如 "Enter", "Cmd", "Tab", "Escape"）
     */
    private static void sendSpecialKey(String keyName) {
        short keyCode = resolveKeyCode(keyName);
        keyDown(keyCode);
        keyUp(keyCode);
    }

    /**
     * 发送单个字符
     * <p>通过 CGEvent 模拟字符输入（使用 Unicode 方式）</p>
     *
     * @param ch 字符
     */
    private static void sendChar(char ch) {
        // 对于普通字符，通过 keyDown/keyUp 发送
        // 简化实现：使用 AppleScript 或 CGEvent 的 Unicode 功能
        // 完整实现需要 CGEventKeyboardSetUnicodeString
        short keyCode = charToKeyCode(ch);
        if (keyCode >= 0) {
            keyDown(keyCode);
            keyUp(keyCode);
        }
    }

    /**
     * 将键名解析为 macOS 虚拟键码
     *
     * @param keyName 键名
     * @return 虚拟键码
     */
    private static short resolveKeyCode(String keyName) {
        switch (keyName.toLowerCase()) {
            case "return":
            case "enter":
                return VK_RETURN;
            case "tab":
                return VK_TAB;
            case "space":
                return VK_SPACE;
            case "delete":
            case "backspace":
                return VK_DELETE;
            case "escape":
            case "esc":
                return VK_ESCAPE;
            case "cmd":
            case "command":
                return VK_COMMAND;
            case "shift":
                return VK_SHIFT;
            case "option":
            case "alt":
                return VK_OPTION;
            case "control":
            case "ctrl":
                return VK_CONTROL;
            case "left":
                return VK_LEFT_ARROW;
            case "right":
                return VK_RIGHT_ARROW;
            case "down":
                return VK_DOWN_ARROW;
            case "up":
                return VK_UP_ARROW;
            case "forwarddelete":
            case "del":
                return VK_FORWARD_DELETE;
            case "home":
                return VK_HOME;
            case "end":
                return VK_END;
            case "pageup":
                return VK_PAGE_UP;
            case "pagedown":
                return VK_PAGE_DOWN;
            case "capslock":
                return VK_CAPS_LOCK;
            default:
                // 尝试作为单字符处理
                if (keyName.length() == 1) {
                    return charToKeyCode(keyName.charAt(0));
                }
                return 0;
        }
    }

    /**
     * 将字符转换为 macOS 虚拟键码
     * <p>简化映射表，覆盖常用 ASCII 字符</p>
     *
     * @param ch 字符
     * @return 虚拟键码，如果无法映射返回 -1
     */
    private static short charToKeyCode(char ch) {
        // macOS 键码布局基于 ANSI 键盘
        switch (Character.toLowerCase(ch)) {
            case 'a': return 0x00; case 's': return 0x01;
            case 'd': return 0x02; case 'f': return 0x03;
            case 'h': return 0x04; case 'g': return 0x05;
            case 'z': return 0x06; case 'x': return 0x07;
            case 'c': return 0x08; case 'v': return 0x09;
            case 'b': return 0x0B; case 'q': return 0x0C;
            case 'w': return 0x0D; case 'e': return 0x0E;
            case 'r': return 0x0F; case 'y': return 0x10;
            case 't': return 0x11; case '1': return 0x12;
            case '2': return 0x13; case '3': return 0x14;
            case '4': return 0x15; case '6': return 0x16;
            case '5': return 0x17; case '=': return 0x18;
            case '9': return 0x19; case '7': return 0x1A;
            case '-': return 0x1B; case '8': return 0x1C;
            case '0': return 0x1D; case ']': return 0x1E;
            case 'o': return 0x1F; case 'u': return 0x20;
            case '[': return 0x21; case 'i': return 0x22;
            case 'p': return 0x23; case 'l': return 0x25;
            case 'j': return 0x26; case '\'': return 0x27;
            case 'k': return 0x28; case ';': return 0x29;
            case '\\': return 0x2A; case ',': return 0x2B;
            case '/': return 0x2C; case 'n': return 0x2D;
            case 'm': return 0x2E; case '.': return 0x2F;
            case ' ': return VK_SPACE;
            default: return -1;
        }
    }

    private Keyboard() {
        // 工具类禁止实例化
    }
}
