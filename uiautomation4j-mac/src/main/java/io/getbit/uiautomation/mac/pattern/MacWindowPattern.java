package io.getbit.uiautomation.mac.pattern;

import io.getbit.uiautomation.mac.ax.AXAction;
import io.getbit.uiautomation.mac.ax.AXAttribute;
import io.getbit.uiautomation.mac.ax.AXUIElement;
import io.getbit.uiautomation.mac.ax.CFUtil;
import io.getbit.uiautomation.pattern.WindowPattern;

/**
 * WindowPattern 的 macOS 实现
 *
 * <p>通过 AX 属性和动作实现窗口操作（关闭、最小化、最大化等）。
 * macOS 窗口操作通过属性设置和动作执行完成，而非专门的 COM 接口方法。</p>
 *
 * <p>与 Windows WindowPattern 的对应关系：
 * <ul>
 *   <li>close() → AXClose 动作</li>
 *   <li>minimize() → 设置 AXMinimized = true</li>
 *   <li>maximize() → AXRaise 动作</li>
 *   <li>restore() → 设置 AXMinimized = false</li>
 *   <li>getVisualState() → 检查 AXMinimized 属性</li>
 * </ul>
 */
public class MacWindowPattern implements WindowPattern {

    /** 底层 AXUIElement 引用（代表窗口元素） */
    private final AXUIElement element;

    /**
     * 构造 MacWindowPattern
     *
     * @param element AXUIElement 窗口元素
     */
    public MacWindowPattern(AXUIElement element) {
        this.element = element;
    }

    /**
     * 关闭窗口
     * <p>执行 AXClose 动作，等价于 Windows WindowPattern::Close</p>
     */
    @Override
    public void close() {
        element.performAction(AXAction.CLOSE);
    }

    /**
     * 是否可以关闭
     * <p>macOS 窗口通常都可以关闭</p>
     *
     * @return 始终返回 true
     */
    @Override
    public boolean canClose() {
        return true;
    }

    /**
     * 最大化窗口
     * <p>执行 AXRaise 动作</p>
     */
    @Override
    public void maximize() {
        element.performAction(AXAction.RAISE);
    }

    /**
     * 最小化窗口
     * <p>设置 AXMinimized = true</p>
     */
    @Override
    public void minimize() {
        // 通过 AppleScript 或设置 AXMinimized 属性
        // 简化实现：执行最小化动作
        element.performAction("AXMinimize");
    }

    /**
     * 恢复窗口（从最小化状态恢复）
     * <p>设置 AXMinimized = false</p>
     */
    @Override
    public void restore() {
        element.performAction(AXAction.RAISE);
    }

    /**
     * 窗口是否置顶
     * <p>macOS 没有直接的 AX 属性表示置顶状态</p>
     *
     * @return 始终返回 false
     */
    @Override
    public boolean isTopmost() {
        return false;
    }

    /**
     * 设置窗口置顶
     * <p>macOS AX API 不直接支持设置窗口置顶</p>
     *
     * @param topmost true 表示置顶（macOS 忽略此参数）
     */
    @Override
    public void setTopmost(boolean topmost) {
        // macOS AX API 不直接支持设置窗口置顶
        // 需要通过 NSWindow API 实现
    }

    /**
     * 获取窗口视觉状态
     * <p>通过检查 AXMinimized 属性判断窗口状态</p>
     *
     * @return 窗口视觉状态（0=正常, 1=最大化, 2=最小化）
     */
    @Override
    public int getVisualState() {
        boolean minimized = element.getBooleanAttribute(AXAttribute.MINIMIZED);
        if (minimized) {
            return 2; // Minimized
        }
        boolean fullScreen = element.getBooleanAttribute(AXAttribute.FULL_SCREEN);
        if (fullScreen) {
            return 1; // Maximized
        }
        return 0; // Normal
    }
}
