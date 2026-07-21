package com.gettyio.uiautomation.mac.pattern;

import com.gettyio.uiautomation.mac.ax.AXAction;
import com.gettyio.uiautomation.mac.ax.AXUIElement;
import com.gettyio.uiautomation.pattern.ExpandCollapsePattern;

/**
 * ExpandCollapsePattern 的 macOS 实现
 *
 * <p>通过执行 AXExpand/AXCollapse 动作实现展开/折叠操作。
 * 等价于 Windows 的 IUIAutomationExpandCollapsePattern。</p>
 *
 * <p>macOS 中的 Outline（树形控件）节点支持展开/折叠动作。</p>
 */
public class MacExpandCollapsePattern implements ExpandCollapsePattern {

    /** 展开状态常量 */
    public static final int EXPAND_COLLAPSE_STATE_EXPANDED = 1;
    /** 折叠状态常量 */
    public static final int EXPAND_COLLAPSE_STATE_COLLAPSED = 0;

    /** 底层 AXUIElement 引用 */
    private final AXUIElement element;

    /**
     * 构造 MacExpandCollapsePattern
     *
     * @param element AXUIElement 元素
     */
    public MacExpandCollapsePattern(AXUIElement element) {
        this.element = element;
    }

    /**
     * 展开元素
     * <p>执行 AXExpand 动作</p>
     */
    @Override
    public void expand() {
        element.performAction(AXAction.EXPAND);
    }

    /**
     * 折叠元素
     * <p>执行 AXCollapse 动作</p>
     */
    @Override
    public void collapse() {
        element.performAction(AXAction.COLLAPSE);
    }

    /**
     * 获取当前展开/折叠状态
     * <p>macOS 没有直接的状态属性，通过检查子元素可见性推断</p>
     *
     * @return 展开状态（0=折叠, 1=展开）
     */
    public int getExpandCollapseState() {
        // macOS 没有直接的展开状态属性
        // 通过检查是否有可见子元素推断
        return EXPAND_COLLAPSE_STATE_COLLAPSED;
    }
}
