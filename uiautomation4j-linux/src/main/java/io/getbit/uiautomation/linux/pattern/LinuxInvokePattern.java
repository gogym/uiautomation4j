package io.getbit.uiautomation.linux.pattern;

import io.getbit.uiautomation.linux.ax.AtspiElement;
import io.getbit.uiautomation.linux.dbus.AtspiAction;
import io.getbit.uiautomation.pattern.InvokePattern;

/**
 * InvokePattern 的 Linux 实现
 *
 * <p>通过 AT-SPI2 Action 接口执行 "click" 动作实现"调用"操作。
 * 等价于 Windows 的 IUIAutomationInvokePattern::Invoke。</p>
 */
public class LinuxInvokePattern implements InvokePattern {

    /** 底层 AtspiElement 引用 */
    private final AtspiElement element;

    /**
     * 构造 LinuxInvokePattern
     *
     * @param element AtspiElement 元素
     */
    public LinuxInvokePattern(AtspiElement element) {
        this.element = element;
    }

    /**
     * 执行调用操作
     * <p>通过 AT-SPI2 Action 接口执行第一个动作（通常是 "click"）</p>
     */
    @Override
    public void invoke() {
        element.doDefaultAction();
    }
}
