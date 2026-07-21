package io.getbit.uiautomation.linux.pattern;

import io.getbit.uiautomation.linux.ax.AtspiConstants;
import io.getbit.uiautomation.linux.ax.AtspiElement;
import io.getbit.uiautomation.linux.dbus.AtspiAction;
import io.getbit.uiautomation.pattern.ScrollPattern;

/**
 * ScrollPattern 的 Linux 实现
 *
 * <p>通过 AT-SPI2 Action 接口执行滚动动作。
 * 等价于 Windows 的 IUIAutomationScrollPattern。</p>
 *
 * <p>AT-SPI2 滚动通过 Action 接口执行 "scroll" 相关动作，
 * 或通过子元素 ScrollBar 进行操作。</p>
 */
public class LinuxScrollPattern implements ScrollPattern {

    /** 底层 AtspiElement 引用 */
    private final AtspiElement element;

    /**
     * 构造 LinuxScrollPattern
     *
     * @param element AtspiElement 元素
     */
    public LinuxScrollPattern(AtspiElement element) {
        this.element = element;
    }

    /**
     * 滚动指定方向和量
     *
     * @param horizontalDirection 水平滚动方向
     * @param verticalDirection   垂直滚动方向
     * @param horizontalAmount    水平滚动量
     * @param verticalAmount      垂直滚动量
     */
    @Override
    public void scroll(int horizontalDirection, int verticalDirection,
                       double horizontalAmount, double verticalAmount) {
        if (verticalDirection < 0) {
            executeAction("scroll-up");
        } else if (verticalDirection > 0) {
            executeAction("scroll-down");
        }
        if (horizontalDirection < 0) {
            executeAction("scroll-left");
        } else if (horizontalDirection > 0) {
            executeAction("scroll-right");
        }
    }

    /**
     * 设置滚动百分比
     * <p>AT-SPI2 不支持直接设置滚动百分比</p>
     *
     * @param horizontalPercent 水平百分比
     * @param verticalPercent   垂直百分比
     */
    @Override
    public void setScrollPercent(double horizontalPercent, double verticalPercent) {
        // AT-SPI2 不支持直接设置滚动百分比
        // 尝试执行 scrollToVisible 动作
        executeAction("scroll-to-visible");
    }

    /**
     * 获取水平滚动百分比
     * <p>AT-SPI2 没有直接的滚动百分比属性</p>
     *
     * @return 始终返回 0
     */
    @Override
    public double getHorizontalScrollPercent() {
        return 0;
    }

    /**
     * 获取垂直滚动百分比
     * <p>AT-SPI2 没有直接的滚动百分比属性</p>
     *
     * @return 始终返回 0
     */
    @Override
    public double getVerticalScrollPercent() {
        return 0;
    }

    /**
     * 是否可水平滚动
     *
     * @return 如果元素支持水平滚动返回 true
     */
    @Override
    public boolean isHorizontallyScrollable() {
        return element.hasState(AtspiConstants.STATE_HORIZONTALLY_SCROLLABLE);
    }

    /**
     * 是否可垂直滚动
     *
     * @return 如果元素支持垂直滚动返回 true
     */
    @Override
    public boolean isVerticallyScrollable() {
        return element.hasState(AtspiConstants.STATE_VERTICALLY_SCROLLABLE);
    }

    /**
     * 执行指定名称的动作
     *
     * @param actionName 动作名称
     */
    private void executeAction(String actionName) {
        AtspiAction action = element.getAction();
        if (action != null) {
            try {
                int nActions = action.GetNActions();
                for (int i = 0; i < nActions; i++) {
                    String name = action.GetName(i);
                    if (actionName.equalsIgnoreCase(name)) {
                        action.DoAction(i);
                        return;
                    }
                }
            } catch (Exception e) {
                // 动作执行失败
            }
        }
    }
}
