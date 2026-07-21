package com.gettyio.uiautomation.linux.pattern;

import com.gettyio.uiautomation.linux.ax.AtspiConstants;
import com.gettyio.uiautomation.linux.ax.AtspiElement;
import com.gettyio.uiautomation.linux.dbus.AtspiAction;
import com.gettyio.uiautomation.pattern.ExpandCollapsePattern;

/**
 * ExpandCollapsePattern 的 Linux 实现
 *
 * <p>通过 AT-SPI2 Action 接口执行展开/折叠动作。
 * 等价于 Windows 的 IUIAutomationExpandCollapsePattern。</p>
 *
 * <p>AT-SPI2 中树形节点等控件通过 Action 接口支持 "expand" 和 "collapse" 动作。</p>
 */
public class LinuxExpandCollapsePattern implements ExpandCollapsePattern {

    /** 底层 AtspiElement 引用 */
    private final AtspiElement element;

    /**
     * 构造 LinuxExpandCollapsePattern
     *
     * @param element AtspiElement 元素
     */
    public LinuxExpandCollapsePattern(AtspiElement element) {
        this.element = element;
    }

    /**
     * 展开元素
     * <p>执行 AT-SPI2 Action "expand" 或 "expand-or-contract"</p>
     */
    @Override
    public void expand() {
        // 先尝试 "expand" 动作
        if (!executeAction("expand")) {
            // 回退到 "expand-or-contract"（某些 GTK 控件使用此动作名）
            executeAction("expand-or-contract");
        }
    }

    /**
     * 折叠元素
     * <p>执行 AT-SPI2 Action "collapse" 或 "expand-or-contract"</p>
     */
    @Override
    public void collapse() {
        // 先尝试 "collapse" 动作
        if (!executeAction("collapse")) {
            // 回退到 "expand-or-contract"
            executeAction("expand-or-contract");
        }
    }

    /**
     * 获取当前展开/折叠状态
     * <p>通过检查 AT-SPI2 状态标志判断</p>
     *
     * @return 展开状态（0=折叠, 1=展开）
     */
    @Override
    public int getExpandCollapseState() {
        if (element.hasState(AtspiConstants.STATE_EXPANDED)) {
            return STATE_EXPANDED;
        }
        return STATE_COLLAPSED;
    }

    /**
     * 执行指定名称的动作
     *
     * @param actionName 动作名称
     * @return 是否成功执行
     */
    private boolean executeAction(String actionName) {
        AtspiAction action = element.getAction();
        if (action != null) {
            try {
                int nActions = action.GetNActions();
                for (int i = 0; i < nActions; i++) {
                    String name = action.GetName(i);
                    if (actionName.equalsIgnoreCase(name)) {
                        return action.DoAction(i);
                    }
                }
            } catch (Exception e) {
                // 动作执行失败
            }
        }
        return false;
    }
}
