package io.getbit.uiautomation.linux.mouse;

import com.sun.jna.Library;
import com.sun.jna.Native;

/**
 * 鼠标操作工具类 - 通过 Linux XTest API 实现
 *
 * <p>提供基本鼠标操作：
 * <ul>
 *   <li>{@link #moveTo(int, int)} - 移动光标</li>
 *   <li>{@link #click(int, int)} - 左键单击</li>
 *   <li>{@link #doubleClick(int, int)} - 左键双击</li>
 *   <li>{@link #rightClick(int, int)} - 右键单击</li>
 *   <li>{@link #scroll(int)} - 滚轮滚动</li>
 * </ul>
 *
 * <p>依赖 X11 XTest 扩展，需要安装 libxtst6。</p>
 */
public class Mouse {

    /**
     * X11 XTest 扩展 JNA 绑定
     */
    interface XTest extends Library {
        XTest INSTANCE = Native.load("Xtst", XTest.class);

        int XTestFakeButtonEvent(long display, int button, boolean isPress, long delay);

        int XTestFakeMotionEvent(long display, int screen, int x, int y, long delay);

        int XTestFakeRelativeMotionEvent(long display, int dx, int dy, long delay);

        long XOpenDisplay(String displayName);

        void XCloseDisplay(long display);

        void XFlush(long display);

        int XDefaultScreen(long display);
    }

    // 鼠标按钮常量
    private static final int BUTTON_LEFT = 1;
    private static final int BUTTON_MIDDLE = 2;
    private static final int BUTTON_RIGHT = 3;
    private static final int BUTTON_SCROLL_UP = 4;
    private static final int BUTTON_SCROLL_DOWN = 5;

    /**
     * 移动鼠标到指定屏幕坐标
     *
     * @param x 屏幕 X 坐标
     * @param y 屏幕 Y 坐标
     */
    public static void moveTo(int x, int y) {
        long display = XTest.INSTANCE.XOpenDisplay(null);
        if (display == 0) {
            throw new RuntimeException("无法打开 X11 Display");
        }
        try {
            int screen = XTest.INSTANCE.XDefaultScreen(display);
            XTest.INSTANCE.XTestFakeMotionEvent(display, screen, x, y, 0);
            XTest.INSTANCE.XFlush(display);
        } finally {
            XTest.INSTANCE.XCloseDisplay(display);
        }
    }

    /**
     * 在指定坐标执行左键单击
     *
     * @param x 屏幕 X 坐标
     * @param y 屏幕 Y 坐标
     */
    public static void click(int x, int y) {
        long display = XTest.INSTANCE.XOpenDisplay(null);
        if (display == 0) {
            throw new RuntimeException("无法打开 X11 Display");
        }
        try {
            int screen = XTest.INSTANCE.XDefaultScreen(display);
            XTest.INSTANCE.XTestFakeMotionEvent(display, screen, x, y, 0);
            XTest.INSTANCE.XTestFakeButtonEvent(display, BUTTON_LEFT, true, 0);
            XTest.INSTANCE.XTestFakeButtonEvent(display, BUTTON_LEFT, false, 0);
            XTest.INSTANCE.XFlush(display);
        } finally {
            XTest.INSTANCE.XCloseDisplay(display);
        }
    }

    /**
     * 在指定坐标执行左键双击
     *
     * @param x 屏幕 X 坐标
     * @param y 屏幕 Y 坐标
     */
    public static void doubleClick(int x, int y) {
        click(x, y);
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        click(x, y);
    }

    /**
     * 在指定坐标执行右键单击
     *
     * @param x 屏幕 X 坐标
     * @param y 屏幕 Y 坐标
     */
    public static void rightClick(int x, int y) {
        long display = XTest.INSTANCE.XOpenDisplay(null);
        if (display == 0) {
            throw new RuntimeException("无法打开 X11 Display");
        }
        try {
            int screen = XTest.INSTANCE.XDefaultScreen(display);
            XTest.INSTANCE.XTestFakeMotionEvent(display, screen, x, y, 0);
            XTest.INSTANCE.XTestFakeButtonEvent(display, BUTTON_RIGHT, true, 0);
            XTest.INSTANCE.XTestFakeButtonEvent(display, BUTTON_RIGHT, false, 0);
            XTest.INSTANCE.XFlush(display);
        } finally {
            XTest.INSTANCE.XCloseDisplay(display);
        }
    }

    /**
     * 执行鼠标滚轮滚动
     *
     * @param scrollAmount 滚动量（正=向下, 负=向上）
     */
    public static void scroll(int scrollAmount) {
        long display = XTest.INSTANCE.XOpenDisplay(null);
        if (display == 0) {
            throw new RuntimeException("无法打开 X11 Display");
        }
        try {
            int button = scrollAmount > 0 ? BUTTON_SCROLL_DOWN : BUTTON_SCROLL_UP;
            int count = Math.abs(scrollAmount);
            for (int i = 0; i < count; i++) {
                XTest.INSTANCE.XTestFakeButtonEvent(display, button, true, 0);
                XTest.INSTANCE.XTestFakeButtonEvent(display, button, false, 0);
            }
            XTest.INSTANCE.XFlush(display);
        } finally {
            XTest.INSTANCE.XCloseDisplay(display);
        }
    }

    private Mouse() {
        // 工具类禁止实例化
    }
}
