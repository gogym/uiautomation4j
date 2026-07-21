package io.getbit.uiautomation.mac.pattern;

import io.getbit.uiautomation.mac.ax.AXAction;
import io.getbit.uiautomation.mac.ax.AXUIElement;
import io.getbit.uiautomation.pattern.InvokePattern;

/**
 * InvokePattern 的 macOS 实现
 *
 * <p>通过执行 AXPress 或 AXConfirm 动作实现"调用"操作。
 * 等价于 Windows 的 IUIAutomationInvokePattern::Invoke。</p>
 *
 * <p>macOS 中按钮等控件支持 AXPress 动作，
 * 复选框等支持 AXConfirm 动作。本实现优先尝试 AXPress，
 * 如果失败则尝试 AXConfirm。</p>
 */
public class MacInvokePattern implements InvokePattern {

    /** 底层 AXUIElement 引用 */
    private final AXUIElement element;

    /**
     * 构造 MacInvokePattern
     *
     * @param element AXUIElement 元素
     */
    public MacInvokePattern(AXUIElement element) {
        this.element = element;
    }

    /**
     * 执行调用操作
     * <p>优先执行 AXPress（按钮类控件），
     * 如果失败则尝试 AXConfirm（确认类控件）。</p>
     */
    @Override
    public void invoke() {
        boolean success = element.performAction(AXAction.PRESS);
        if (!success) {
            // 回退：尝试 AXConfirm
            element.performAction(AXAction.CONFIRM);
        }
    }
}
