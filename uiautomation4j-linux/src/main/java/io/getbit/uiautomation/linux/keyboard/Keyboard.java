package io.getbit.uiautomation.linux.keyboard;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * 键盘操作工具类 - 通过 Linux XTest API 实现
 *
 * <p>提供键盘输入模拟，支持普通字符和特殊键。
 * 特殊键语法：{@code "{Enter}"}、{@code "{Ctrl}c"} 等。</p>
 *
 * <p>依赖 X11 XTest 扩展，需要安装 libxtst6。</p>
 *
 * @see <a href="https://www.x.org/releases/current/doc/libXtst/xtestlib.html">XTest Extension</a>
 */
public class Keyboard {

    /**
     * X11 XTest 扩展 JNA 绑定
     */
    interface XTest extends Library {
        XTest INSTANCE = Native.load("Xtst", XTest.class);

        /**
         * 模拟按键事件
         *
         * @param display  Display 指针
         * @param keycode  键码
         * @param isPress  true=按下, false=释放
         * @param delay    延迟（毫秒）
         * @return 是否成功
         */
        int XTestFakeKeyEvent(long display, int keycode, boolean isPress, long delay);

        /**
         * 打开 Display
         *
         * @param displayName Display 名称（null 使用默认）
         * @return Display 指针
         */
        long XOpenDisplay(String displayName);

        /**
         * 关闭 Display
         *
         * @param display Display 指针
         */
        void XCloseDisplay(long display);

        /**
         * 将 keysym 转换为 keycode
         *
         * @param display Display 指针
         * @param keysym  keysym 值
         * @return keycode
         */
        int XKeysymToKeycode(long display, long keysym);

        /**
         * 刷新输出缓冲区
         *
         * @param display Display 指针
         */
        void XFlush(long display);
    }

    // ==================== X11 Keysym 常量 ====================

    /** XK_Return */
    public static final long XK_RETURN = 0xFF0D;
    /** XK_Tab */
    public static final long XK_TAB = 0xFF09;
    /** XK_space */
    public static final long XK_SPACE = 0x0020;
    /** XK_BackSpace */
    public static final long XK_BACKSPACE = 0xFF08;
    /** XK_Escape */
    public static final long XK_ESCAPE = 0xFF1B;
    /** XK_Control_L */
    public static final long XK_CONTROL_L = 0xFFE3;
    /** XK_Shift_L */
    public static final long XK_SHIFT_L = 0xFFE1;
    /** XK_Alt_L */
    public static final long XK_ALT_L = 0xFFE9;
    /** XK_Super_L */
    public static final long XK_SUPER_L = 0xFFEB;
    /** XK_Left */
    public static final long XK_LEFT = 0xFF51;
    /** XK_Up */
    public static final long XK_UP = 0xFF52;
    /** XK_Right */
    public static final long XK_RIGHT = 0xFF53;
    /** XK_Down */
    public static final long XK_DOWN = 0xFF54;
    /** XK_Home */
    public static final long XK_HOME = 0xFF50;
    /** XK_End */
    public static final long XK_END = 0xFF57;
    /** XK_Page_Up */
    public static final long XK_PAGE_UP = 0xFF55;
    /** XK_Page_Down */
    public static final long XK_PAGE_DOWN = 0xFF56;
    /** XK_Delete */
    public static final long XK_DELETE = 0xFFFF;
    /** XK_Insert */
    public static final long XK_INSERT = 0xFF63;
    /** XK_Caps_Lock */
    public static final long XK_CAPS_LOCK = 0xFFE5;

    /**
     * 发送按键序列
     *
     * @param keys 按键序列字符串
     */
    public static void sendKeys(String keys) {
        if (keys == null || keys.isEmpty()) return;

        long display = XTest.INSTANCE.XOpenDisplay(null);
        if (display == 0) {
            throw new RuntimeException("无法打开 X11 Display，请确保已设置 DISPLAY 环境变量");
        }

        try {
            int i = 0;
            while (i < keys.length()) {
                if (keys.charAt(i) == '{') {
                    int end = keys.indexOf('}', i);
                    if (end == -1) {
                        sendChar(display, keys.charAt(i));
                        i++;
                        continue;
                    }
                    String keyName = keys.substring(i + 1, end);
                    sendSpecialKey(display, keyName);
                    i = end + 1;
                } else {
                    sendChar(display, keys.charAt(i));
                    i++;
                }
            }
            XTest.INSTANCE.XFlush(display);
        } finally {
            XTest.INSTANCE.XCloseDisplay(display);
        }
    }

    /**
     * 模拟按键按下
     *
     * @param keysym X11 keysym 值
     */
    public static void keyDown(long keysym) {
        long display = XTest.INSTANCE.XOpenDisplay(null);
        if (display != 0) {
            try {
                int keycode = XTest.INSTANCE.XKeysymToKeycode(display, keysym);
                XTest.INSTANCE.XTestFakeKeyEvent(display, keycode, true, 0);
                XTest.INSTANCE.XFlush(display);
            } finally {
                XTest.INSTANCE.XCloseDisplay(display);
            }
        }
    }

    /**
     * 模拟按键释放
     *
     * @param keysym X11 keysym 值
     */
    public static void keyUp(long keysym) {
        long display = XTest.INSTANCE.XOpenDisplay(null);
        if (display != 0) {
            try {
                int keycode = XTest.INSTANCE.XKeysymToKeycode(display, keysym);
                XTest.INSTANCE.XTestFakeKeyEvent(display, keycode, false, 0);
                XTest.INSTANCE.XFlush(display);
            } finally {
                XTest.INSTANCE.XCloseDisplay(display);
            }
        }
    }

    /**
     * 发送特殊键
     *
     * @param display Display 指针
     * @param keyName 键名
     */
    private static void sendSpecialKey(long display, String keyName) {
        long keysym = resolveKeysym(keyName);
        int keycode = XTest.INSTANCE.XKeysymToKeycode(display, keysym);
        if (keycode != 0) {
            XTest.INSTANCE.XTestFakeKeyEvent(display, keycode, true, 0);
            XTest.INSTANCE.XTestFakeKeyEvent(display, keycode, false, 0);
        }
    }

    /**
     * 发送单个字符
     *
     * @param display Display 指针
     * @param ch      字符
     */
    private static void sendChar(long display, char ch) {
        // 使用 keysym 直接映射字符
        long keysym = charToKeysym(ch);
        int keycode = XTest.INSTANCE.XKeysymToKeycode(display, keysym);
        if (keycode != 0) {
            XTest.INSTANCE.XTestFakeKeyEvent(display, keycode, true, 0);
            XTest.INSTANCE.XTestFakeKeyEvent(display, keycode, false, 0);
        }
    }

    /**
     * 将键名解析为 X11 keysym
     *
     * @param keyName 键名
     * @return keysym 值
     */
    private static long resolveKeysym(String keyName) {
        switch (keyName.toLowerCase()) {
            case "return":
            case "enter":
                return XK_RETURN;
            case "tab":
                return XK_TAB;
            case "space":
                return XK_SPACE;
            case "delete":
            case "backspace":
                return XK_BACKSPACE;
            case "escape":
            case "esc":
                return XK_ESCAPE;
            case "ctrl":
            case "control":
                return XK_CONTROL_L;
            case "shift":
                return XK_SHIFT_L;
            case "alt":
                return XK_ALT_L;
            case "cmd":
            case "super":
            case "win":
                return XK_SUPER_L;
            case "left":
                return XK_LEFT;
            case "right":
                return XK_RIGHT;
            case "down":
                return XK_DOWN;
            case "up":
                return XK_UP;
            case "home":
                return XK_HOME;
            case "end":
                return XK_END;
            case "pageup":
                return XK_PAGE_UP;
            case "pagedown":
                return XK_PAGE_DOWN;
            case "del":
            case "forwarddelete":
                return XK_DELETE;
            case "insert":
                return XK_INSERT;
            case "capslock":
                return XK_CAPS_LOCK;
            default:
                if (keyName.length() == 1) {
                    return charToKeysym(keyName.charAt(0));
                }
                return 0;
        }
    }

    /**
     * 将字符转换为 X11 keysym
     *
     * @param ch 字符
     * @return keysym 值
     */
    private static long charToKeysym(char ch) {
        // 对于 ASCII 字符，keysym 等于 Unicode 码点
        if (ch >= 0x20 && ch <= 0x7E) {
            return ch;
        }
        // 对于其他字符，使用 Unicode keysym 格式
        return 0x01000000 | ch;
    }

    private Keyboard() {
        // 工具类禁止实例化
    }
}
