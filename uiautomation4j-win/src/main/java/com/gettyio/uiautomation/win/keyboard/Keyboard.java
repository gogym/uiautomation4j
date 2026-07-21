package com.gettyio.uiautomation.win.keyboard;

import com.sun.jna.Native;

/**
 * 键盘操作工具类 - 通过 Win32 keybd_event API 实现
 *
 * <p>提供两类键盘操作：
 * <ul>
 *   <li>普通字符输入：通过 {@link #sendKeys(String)} 方法，支持普通字符和特殊键</li>
 *   <li>底层按键模拟：通过 {@link #keyDown(int)} / {@link #keyUp(int)} 模拟按键按下/释放</li>
 * </ul>
 *
 * <p>特殊键语法：使用花括号包裹键名，如 {@code "{Enter}"}、{@code "{Ctrl}"}、{@code "{F1}"}。
 * 普通字符会先通过 {@code VkKeyScanW} 查询对应的虚拟键码和修饰符，再模拟按键。
 *
 * <p>使用示例：
 * <pre>
 * // 输入普通文本
 * Keyboard.sendKeys("Hello World");
 *
 * // 输入特殊键
 * Keyboard.sendKeys("{Ctrl}a{Ctrl}");  // Ctrl+A 全选
 * Keyboard.sendKeys("{Enter}");         // 回车
 * Keyboard.sendKeys("{Tab}");           // Tab 切换焦点
 * </pre>
 *
 * @see <a href="https://docs.microsoft.com/en-us/windows/win32/api/winuser/nf-winuser-keybd_event">keybd_event API</a>
 */
public class Keyboard {

    /**
     * 自定义 JNA 接口，用于调用 JNA 内置 User32 中不存在的函数
     *
     * <p>包含两个本地 API：
     * <ul>
     *   <li>{@code keybd_event} - 模拟键盘按键事件（按下/释放）</li>
     *   <li>{@code VkKeyScanW} - 将字符转换为虚拟键码和修饰符状态</li>
     * </ul>
     */
    private interface User32Ext extends com.sun.jna.Library {
        /**
         * 模拟键盘按键事件
         *
         * @param bVk        虚拟键码（Virtual-Key Code）
         * @param bScan      硬件扫描码（通常为 0，由系统自动映射）
         * @param dwFlags    事件标志：0=按下，KEYEVENTF_KEYUP(0x0002)=释放
         * @param dwExtraInfo 附加信息（通常为 0）
         */
        void keybd_event(byte bVk, byte bScan, int dwFlags, int dwExtraInfo);

        /**
         * 将 Unicode 字符转换为虚拟键码和 Shift 状态
         *
         * @param ch 要转换的 Unicode 字符
         * @return 低字节为虚拟键码，高字节为修饰符标志（1=Shift, 2=Ctrl, 4=Alt）
         */
        short VkKeyScanW(char ch);
    }

    /** JNA 动态加载的 user32.dll 扩展接口实例 */
    private static final User32Ext USER32EXT = Native.load("user32", User32Ext.class);

    // ==================== 虚拟键码常量 ====================
    // 参考: https://docs.microsoft.com/en-us/windows/win32/inputdev/virtual-key-codes

    /** Backspace 键 (0x08) */
    private static final int VK_BACK = 0x08;
    /** Tab 键 (0x09) */
    private static final int VK_TAB = 0x09;
    /** Enter/Return 键 (0x0D) */
    private static final int VK_RETURN = 0x0D;
    /** Shift 键 (0x10) */
    private static final int VK_SHIFT = 0x10;
    /** Ctrl 键 (0x11) */
    private static final int VK_CONTROL = 0x11;
    /** Alt 键 (0x12)，即 VK_MENU */
    private static final int VK_MENU = 0x12;
    /** Esc 键 (0x1B) */
    private static final int VK_ESCAPE = 0x1B;
    /** 空格键 (0x20) */
    private static final int VK_SPACE = 0x20;
    /** 左方向键 (0x25) */
    private static final int VK_LEFT = 0x25;
    /** 上方向键 (0x26) */
    private static final int VK_UP = 0x26;
    /** 右方向键 (0x27) */
    private static final int VK_RIGHT = 0x27;
    /** 下方向键 (0x28) */
    private static final int VK_DOWN = 0x28;
    /** Delete 键 (0x2E) */
    private static final int VK_DELETE = 0x2E;
    /** Page Up 键 (0x21) */
    private static final int VK_PRIOR = 0x21;
    /** Page Down 键 (0x22) */
    private static final int VK_NEXT = 0x22;
    /** End 键 (0x23) */
    private static final int VK_END = 0x23;
    /** Home 键 (0x24) */
    private static final int VK_HOME = 0x24;
    /** F1 功能键 (0x70) */
    private static final int VK_F1 = 0x70;
    /** F2 功能键 (0x71) */
    private static final int VK_F2 = 0x71;
    /** F3 功能键 (0x72) */
    private static final int VK_F3 = 0x72;
    /** F4 功能键 (0x73) */
    private static final int VK_F4 = 0x73;
    /** F5 功能键 (0x74) */
    private static final int VK_F5 = 0x74;
    /** F6 功能键 (0x75) */
    private static final int VK_F6 = 0x75;
    /** F7 功能键 (0x76) */
    private static final int VK_F7 = 0x76;
    /** F8 功能键 (0x77) */
    private static final int VK_F8 = 0x77;
    /** F9 功能键 (0x78) */
    private static final int VK_F9 = 0x78;
    /** F10 功能键 (0x79) */
    private static final int VK_F10 = 0x79;
    /** F11 功能键 (0x7A) */
    private static final int VK_F11 = 0x7A;
    /** F12 功能键 (0x7B) */
    private static final int VK_F12 = 0x7B;
    /** keybd_event 标志：释放按键（与按下对应） */
    private static final int KEYEVENTF_KEYUP = 0x0002;

    /**
     * 发送键盘输入字符串
     *
     * <p>解析规则：
     * <ul>
     *   <li>普通字符：逐个通过 {@link #sendChar(char)} 发送</li>
     *   <li>{@code {xxx}} 花括号包裹的内容：作为特殊键名，通过 {@link #sendSpecialKey(String)} 发送</li>
     * </ul>
     *
     * <p>示例：
     * <pre>
     * sendKeys("Hello");           // 逐字符输入 "Hello"
     * sendKeys("{Enter}");          // 按下回车键
     * sendKeys("{Ctrl}c{Ctrl}");    // Ctrl+C 复制（注意：此处仅模拟单次按键）
     * </pre>
     *
     * @param keys 要输入的字符串，支持 {@code {KeyName}} 特殊键语法
     * @throws IllegalArgumentException 如果存在未匹配的 '{' 或未知的特殊键名
     */
    public static void sendKeys(String keys) {
        if (keys == null || keys.isEmpty()) return;
        int i = 0;
        while (i < keys.length()) {
            char c = keys.charAt(i);
            if (c == '{') {
                // 解析花括号内的特殊键名
                int end = keys.indexOf('}', i);
                if (end == -1) throw new IllegalArgumentException("未匹配的 '{': " + keys);
                sendSpecialKey(keys.substring(i + 1, end));
                i = end + 1;
            } else {
                // 普通字符直接发送
                sendChar(c);
                i++;
            }
        }
    }

    /**
     * 发送特殊键（按下并释放）
     *
     * <p>支持的键名（不区分大小写）：
     * <ul>
     *   <li>修饰键：ctrl/control、alt、shift</li>
     *   <li>回车/换行：enter/return、tab、esc/escape</li>
     *   <li>编辑键：del/delete、backspace/bs、space</li>
     *   <li>方向键：up、down、left、right</li>
     *   <li>导航键：home、end、pageup、pagedown</li>
     *   <li>功能键：f1 ~ f12</li>
     * </ul>
     *
     * @param key 键名（不含花括号）
     * @throws IllegalArgumentException 如果键名不在支持列表中
     */
    private static void sendSpecialKey(String key) {
        switch (key.toLowerCase()) {
            // 修饰键
            case "ctrl": case "control": pressKey(VK_CONTROL); break;
            case "alt": pressKey(VK_MENU); break;
            case "shift": pressKey(VK_SHIFT); break;
            // 回车/换行/转义
            case "enter": case "return": pressKey(VK_RETURN); break;
            case "tab": pressKey(VK_TAB); break;
            case "esc": case "escape": pressKey(VK_ESCAPE); break;
            // 编辑相关
            case "del": case "delete": pressKey(VK_DELETE); break;
            case "backspace": case "bs": pressKey(VK_BACK); break;
            case "space": pressKey(VK_SPACE); break;
            // 方向键
            case "up": pressKey(VK_UP); break;
            case "down": pressKey(VK_DOWN); break;
            case "left": pressKey(VK_LEFT); break;
            case "right": pressKey(VK_RIGHT); break;
            // 导航键
            case "home": pressKey(VK_HOME); break;
            case "end": pressKey(VK_END); break;
            case "pageup": pressKey(VK_PRIOR); break;
            case "pagedown": pressKey(VK_NEXT); break;
            // 功能键 F1-F12
            case "f1": pressKey(VK_F1); break; case "f2": pressKey(VK_F2); break;
            case "f3": pressKey(VK_F3); break; case "f4": pressKey(VK_F4); break;
            case "f5": pressKey(VK_F5); break; case "f6": pressKey(VK_F6); break;
            case "f7": pressKey(VK_F7); break; case "f8": pressKey(VK_F8); break;
            case "f9": pressKey(VK_F9); break; case "f10": pressKey(VK_F10); break;
            case "f11": pressKey(VK_F11); break; case "f12": pressKey(VK_F12); break;
            default: throw new IllegalArgumentException("未知特殊键: " + key);
        }
    }

    /**
     * 模拟一次完整的按键操作（按下 + 释放）
     *
     * @param vkCode 虚拟键码
     */
    private static void pressKey(int vkCode) {
        keyDown(vkCode);
        keyUp(vkCode);
    }

    /**
     * 模拟按键按下事件
     * <p>调用 keybd_event，dwFlags=0 表示按下
     *
     * @param vkCode 虚拟键码
     */
    private static void keyDown(int vkCode) {
        USER32EXT.keybd_event((byte) vkCode, (byte) 0, 0, 0);
    }

    /**
     * 模拟按键释放事件
     * <p>调用 keybd_event，dwFlags=KEYEVENTF_KEYUP 表示释放
     *
     * @param vkCode 虚拟键码
     */
    private static void keyUp(int vkCode) {
        USER32EXT.keybd_event((byte) vkCode, (byte) 0, KEYEVENTF_KEYUP, 0);
    }

    /**
     * 发送单个 Unicode 字符
     *
     * <p>处理流程：
     * <ol>
     *   <li>调用 {@code VkKeyScanW} 将字符转换为虚拟键码 + 修饰符标志</li>
     *   <li>根据修饰符标志，先按下对应的修饰键（Shift/Ctrl/Alt）</li>
     *   <li>按下并释放目标键</li>
     *   <li>按相反顺序释放修饰键（保证状态正确恢复）</li>
     * </ol>
     *
     * <p>修饰符标志位含义：
     * <ul>
     *   <li>bit 0 (值=1)：需要 Shift</li>
     *   <li>bit 1 (值=2)：需要 Ctrl</li>
     *   <li>bit 2 (值=4)：需要 Alt</li>
     * </ul>
     *
     * @param c 要发送的 Unicode 字符
     */
    private static void sendChar(char c) {
        // VkKeyScanW 返回值：低字节=虚拟键码，高字节=修饰符标志
        short vkScan = USER32EXT.VkKeyScanW(c);
        int vk = vkScan & 0xFF;           // 提取虚拟键码
        int modifiers = (vkScan >> 8) & 0xFF; // 提取修饰符标志

        // 按下需要的修饰键（顺序：Shift -> Ctrl -> Alt）
        if ((modifiers & 1) != 0) keyDown(VK_SHIFT);
        if ((modifiers & 2) != 0) keyDown(VK_CONTROL);
        if ((modifiers & 4) != 0) keyDown(VK_MENU);

        // 按下并释放目标键
        pressKey(vk);

        // 释放修饰键（顺序与按下相反：Alt -> Ctrl -> Shift）
        if ((modifiers & 4) != 0) keyUp(VK_MENU);
        if ((modifiers & 2) != 0) keyUp(VK_CONTROL);
        if ((modifiers & 1) != 0) keyUp(VK_SHIFT);
    }
}
