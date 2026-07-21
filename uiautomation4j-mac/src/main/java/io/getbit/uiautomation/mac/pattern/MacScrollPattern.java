package io.getbit.uiautomation.mac.pattern;

import io.getbit.uiautomation.mac.ax.AXAction;
import io.getbit.uiautomation.mac.ax.AXUIElement;
import io.getbit.uiautomation.pattern.ScrollPattern;

/**
 * ScrollPattern 的 macOS 实现
 *
 * <p>通过执行 AXScrollUp/Down/Left/Right 动作实现滚动操作。
 * 等价于 Windows 的 IUIAutomationScrollPattern。</p>
 *
 * <p>macOS 的滚动通过动作执行，而非直接设置滚动百分比。
 * 因此 {@link #setScrollPercent} 在 macOS 上通过计算滚动方向后执行对应动作。</p>
 */
public class MacScrollPattern implements ScrollPattern {

    /** 底层 AXUIElement 引用 */
    private final AXUIElement element;

    /**
     * 构造 MacScrollPattern
     *
     * @param element AXUIElement 元素
     */
    public MacScrollPattern(AXUIElement element) {
        this.element = element;
    }

    /**
     * 滚动指定方向和量
     * <p>根据水平和垂直方向执行对应的 AX 滚动作</p>
     *
     * @param horizontalDirection 水平方向（0=无, 负=左, 正=右）
     * @param verticalDirection   垂直方向（0=无, 负=上, 正=下）
     * @param horizontalAmount    水平滚动量（macOS 忽略，按动作粒度滚动）
     * @param verticalAmount      垂直滚动量（macOS 忽略，按动作粒度滚动）
     */
    @Override
    public void scroll(int horizontalDirection, int verticalDirection,
                       double horizontalAmount, double verticalAmount) {
        if (verticalDirection < 0) {
            element.performAction(AXAction.SCROLL_UP);
        } else if (verticalDirection > 0) {
            element.performAction(AXAction.SCROLL_DOWN);
        }
        if (horizontalDirection < 0) {
            element.performAction(AXAction.SCROLL_LEFT);
        } else if (horizontalDirection > 0) {
            element.performAction(AXAction.SCROLL_RIGHT);
        }
    }

    /**
     * 设置滚动百分比
     * <p>macOS 不支持直接设置滚动百分比，通过 {@link #scrollToVisible} 替代</p>
     *
     * @param horizontalPercent 水平百分比（0.0 - 100.0）
     * @param verticalPercent   垂直百分比（0.0 - 100.0）
     */
    @Override
    public void setScrollPercent(double horizontalPercent, double verticalPercent) {
        // macOS 不支持直接设置百分比，使用 scrollToVisible 替代
        element.performAction(AXAction.SCROLL_TO_VISIBLE);
    }

    /**
     * 获取水平滚动百分比
     * <p>macOS 没有直接的滚动百分比属性，返回 0</p>
     *
     * @return 水平滚动百分比（macOS 始终返回 0）
     */
    @Override
    public double getHorizontalScrollPercent() {
        return 0; // macOS 不提供此信息
    }

    /**
     * 获取垂直滚动百分比
     * <p>macOS 没有直接的滚动百分比属性，返回 0</p>
     *
     * @return 垂直滚动百分比（macOS 始终返回 0）
     */
    @Override
    public double getVerticalScrollPercent() {
        return 0; // macOS 不提供此信息
    }

    /**
     * 是否可水平滚动
     *
     * @return 如果元素支持水平滚动返回 true
     */
    @Override
    public boolean isHorizontallyScrollable() {
        // macOS 没有直接属性，检查是否有水平滚动条子元素
        return false;
    }

    /**
     * 是否可垂直滚动
     *
     * @return 如果元素支持垂直滚动返回 true
     */
    @Override
    public boolean isVerticallyScrollable() {
        // 检查是否有垂直滚动条子元素
        return false;
    }
}
