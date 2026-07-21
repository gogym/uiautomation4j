package io.getbit.uiautomation.linux.pattern;

import io.getbit.uiautomation.linux.ax.AtspiConstants;
import io.getbit.uiautomation.linux.ax.AtspiElement;
import io.getbit.uiautomation.linux.dbus.AtspiAction;
import io.getbit.uiautomation.pattern.WindowPattern;

/**
 * WindowPattern 的 Linux 实现
 *
 * <p>通过 AT-SPI2 Action 接口和状态实现窗口操作。
 * 等价于 Windows 的 IUIAutomationWindowPattern。</p>
 *
 * <p>AT-SPI2 窗口操作通过 Action 接口执行：
 * <ul>
 *   <li>close → Action "close"</li>
 *   <li>maximize → Action "maximize"</li>
 *   <li>minimize → Action "minimize"</li>
 *   <li>restore → Action "lower" 或 "raise"</li>
 * </ul>
 */
public class LinuxWindowPattern implements WindowPattern {

    /** 底层 AtspiElement 引用 */
    private final AtspiElement element;

    /**
     * 构造 LinuxWindowPattern
     *
     * @param element AtspiElement 窗口元素
     */
    public LinuxWindowPattern(AtspiElement element) {
        this.element = element;
    }

    /**
     * 关闭窗口
     * <p>执行 AT-SPI2 Action "close"</p>
     */
    @Override
    public void close() {
        executeAction("close");
    }

    /**
     * 是否可以关闭
     *
     * @return 如果元素支持 close 动作返回 true
     */
    @Override
    public boolean canClose() {
        return hasAction("close");
    }

    /**
     * 最大化窗口
     * <p>执行 AT-SPI2 Action "maximize"</p>
     */
    @Override
    public void maximize() {
        executeAction("maximize");
    }

    /**
     * 最小化窗口
     * <p>执行 AT-SPI2 Action "minimize"</p>
     */
    @Override
    public void minimize() {
        executeAction("minimize");
    }

    /**
     * 恢复窗口
     * <p>执行 AT-SPI2 Action "raise"</p>
     */
    @Override
    public void restore() {
        executeAction("raise");
    }

    /**
     * 窗口是否置顶
     * <p>AT-SPI2 没有直接的置顶状态属性</p>
     *
     * @return 始终返回 false
     */
    @Override
    public boolean isTopmost() {
        return false;
    }

    /**
     * 设置窗口置顶
     * <p>AT-SPI2 不直接支持设置窗口置顶</p>
     *
     * @param topmost true 表示置顶
     */
    @Override
    public void setTopmost(boolean topmost) {
        // AT-SPI2 不直接支持设置窗口置顶
    }

    /**
     * 获取窗口视觉状态
     * <p>通过检查 AT-SPI2 状态标志判断窗口状态</p>
     *
     * @return 窗口视觉状态（0=正常, 1=最大化, 2=最小化）
     */
    @Override
    public int getVisualState() {
        // AT-SPI2 没有直接的窗口视觉状态属性
        // 通过检查状态标志推断
        if (element.hasState(AtspiConstants.STATE_ICONIFIED)) {
            return 2; // Minimized
        }
        if (element.hasState(AtspiConstants.STATE_MAXIMIZED)) {
            return 1; // Maximized
        }
        return 0; // Normal
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

    /**
     * 检查是否支持指定动作
     *
     * @param actionName 动作名称
     * @return 是否支持
     */
    private boolean hasAction(String actionName) {
        AtspiAction action = element.getAction();
        if (action != null) {
            try {
                int nActions = action.GetNActions();
                for (int i = 0; i < nActions; i++) {
                    String name = action.GetName(i);
                    if (actionName.equalsIgnoreCase(name)) {
                        return true;
                    }
                }
            } catch (Exception e) {
                // 查询失败
            }
        }
        return false;
    }
}
