package io.getbit.uiautomation.mac.mouse;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;

/**
 * 鼠标操作工具类 - 通过 macOS CGEvent API 实现
 *
 * <p>提供基本的鼠标操作：
 * <ul>
 *   <li>{@link #moveTo(int, int)} - 移动光标到指定位置</li>
 *   <li>{@link #click(int, int)} - 左键单击</li>
 *   <li>{@link #doubleClick(int, int)} - 左键双击</li>
 *   <li>{@link #rightClick(int, int)} - 右键单击</li>
 * </ul>
 *
 * <p>所有操作均基于屏幕绝对坐标（像素）。
 * macOS 坐标系原点在屏幕左下角（与 Windows 左上角不同），
 * 但 CGEvent 的鼠标事件使用统一的屏幕坐标。</p>
 *
 * @see <a href="https://developer.apple.com/documentation/coregraphics/cgevent">CGEvent</a>
 */
public class Mouse {

    /**
     * CoreGraphics 框架 JNA 绑定（鼠标事件相关）
     */
    interface CoreGraphics extends Library {
        CoreGraphics INSTANCE = Native.load("CoreGraphics", CoreGraphics.class);

        /**
         * 创建鼠标事件
         *
         * @param source      事件源（null 使用默认）
         * @param mouseType   鼠标事件类型
         * @param mousePosition 鼠标位置（CGPoint，double[2]）
         * @param mouseButton 鼠标按钮（0=左, 1=右, 2=中）
         * @return CGEventRef 指针
         */
        Pointer CGEventCreateMouseEvent(Pointer source, int mouseType,
                                         double[] mousePosition, int mouseButton);

        /**
         * 创建滚动事件
         *
         * @param source      事件源
         * @param scrollDeltaY 垂直滚动量
         * @param scrollDeltaX 水平滚动量
         * @param scrollDeltaZ Z 轴滚动量（通常为 0）
         * @return CGEventRef 指针
         */
        Pointer CGEventCreateScrollWheelEvent(Pointer source, int scrollType,
                                               int scrollDeltaY, int scrollDeltaX,
                                               int scrollDeltaZ);

        /**
         * 发送 CGEvent
         *
         * @param tap   事件 tap 位置
         * @param event CGEventRef 指针
         */
        void CGEventPost(int tap, Pointer event);

        /** 释放 CGEvent */
        void CFRelease(Pointer cf);

        /**
         * 获取鼠标当前位置
         *
         * @param eventSource 事件源
         * @return CGPoint（double[2]）
         */
        // CGEvent 没有直接的 GetMousePosition，需要通过其他方式获取
    }

    /** kCGHIDEventTap - 系统级事件注入点 */
    private static final int kCGHIDEventTap = 0;

    // ==================== 鼠标事件类型常量 ====================

    /** 鼠标按下事件 */
    private static final int kCGEventLeftMouseDown = 1;
    /** 鼠标释放事件 */
    private static final int kCGEventLeftMouseUp = 2;
    /** 鼠标移动事件 */
    private static final int kCGEventMouseMoved = 5;
    /** 右键按下事件 */
    private static final int kCGEventRightMouseDown = 3;
    /** 右键释放事件 */
    private static final int kCGEventRightMouseUp = 4;
    /** 滚动事件类型 */
    private static final int kCGScrollEventUnitPixel = 0;

    /** 鼠标左键 */
    private static final int kCGMouseButtonLeft = 0;
    /** 鼠标右键 */
    private static final int kCGMouseButtonRight = 1;

    /**
     * 移动鼠标到指定屏幕坐标
     *
     * @param x 屏幕 X 坐标
     * @param y 屏幕 Y 坐标
     */
    public static void moveTo(int x, int y) {
        double[] position = {x, y};
        Pointer event = CoreGraphics.INSTANCE.CGEventCreateMouseEvent(
                null, kCGEventMouseMoved, position, kCGMouseButtonLeft);
        if (event != null) {
            CoreGraphics.INSTANCE.CGEventPost(kCGHIDEventTap, event);
            CoreGraphics.INSTANCE.CFRelease(event);
        }
    }

    /**
     * 在指定坐标执行左键单击
     * <p>依次发送鼠标按下和释放事件</p>
     *
     * @param x 屏幕 X 坐标
     * @param y 屏幕 Y 坐标
     */
    public static void click(int x, int y) {
        double[] position = {x, y};
        // 鼠标按下
        Pointer downEvent = CoreGraphics.INSTANCE.CGEventCreateMouseEvent(
                null, kCGEventLeftMouseDown, position, kCGMouseButtonLeft);
        if (downEvent != null) {
            CoreGraphics.INSTANCE.CGEventPost(kCGHIDEventTap, downEvent);
            CoreGraphics.INSTANCE.CFRelease(downEvent);
        }
        // 鼠标释放
        Pointer upEvent = CoreGraphics.INSTANCE.CGEventCreateMouseEvent(
                null, kCGEventLeftMouseUp, position, kCGMouseButtonLeft);
        if (upEvent != null) {
            CoreGraphics.INSTANCE.CGEventPost(kCGHIDEventTap, upEvent);
            CoreGraphics.INSTANCE.CFRelease(upEvent);
        }
    }

    /**
     * 在指定坐标执行左键双击
     * <p>连续执行两次 {@link #click(int, int)}</p>
     *
     * @param x 屏幕 X 坐标
     * @param y 屏幕 Y 坐标
     */
    public static void doubleClick(int x, int y) {
        click(x, y);
        try {
            Thread.sleep(50); // 双击间隔
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
        double[] position = {x, y};
        // 右键按下
        Pointer downEvent = CoreGraphics.INSTANCE.CGEventCreateMouseEvent(
                null, kCGEventRightMouseDown, position, kCGMouseButtonRight);
        if (downEvent != null) {
            CoreGraphics.INSTANCE.CGEventPost(kCGHIDEventTap, downEvent);
            CoreGraphics.INSTANCE.CFRelease(downEvent);
        }
        // 右键释放
        Pointer upEvent = CoreGraphics.INSTANCE.CGEventCreateMouseEvent(
                null, kCGEventRightMouseUp, position, kCGMouseButtonRight);
        if (upEvent != null) {
            CoreGraphics.INSTANCE.CGEventPost(kCGHIDEventTap, upEvent);
            CoreGraphics.INSTANCE.CFRelease(upEvent);
        }
    }

    /**
     * 执行鼠标滚轮滚动
     *
     * @param scrollAmount 滚动量（正=向下, 负=向上）
     */
    public static void scroll(int scrollAmount) {
        Pointer event = CoreGraphics.INSTANCE.CGEventCreateScrollWheelEvent(
                null, kCGScrollEventUnitPixel, scrollAmount, 0, 0);
        if (event != null) {
            CoreGraphics.INSTANCE.CGEventPost(kCGHIDEventTap, event);
            CoreGraphics.INSTANCE.CFRelease(event);
        }
    }

    private Mouse() {
        // 工具类禁止实例化
    }
}
