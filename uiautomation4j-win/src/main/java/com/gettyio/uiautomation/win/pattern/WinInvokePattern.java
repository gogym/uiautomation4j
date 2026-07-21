package com.gettyio.uiautomation.win.pattern;

import com.gettyio.uiautomation.pattern.InvokePattern;
import com.gettyio.uiautomation.win.com.IUIAutomationInvokePattern;

/**
 * InvokePattern 的 Windows 实现
 *
 * <p>将 core 模块的 {@link InvokePattern} 接口桥接到 Windows COM 层的
 * {@link IUIAutomationInvokePattern}。通过 {@code invoke()} 触发控件的默认操作。</p>
 */
public class WinInvokePattern implements InvokePattern {

    /** 底层 COM InvokePattern 对象 */
    private final IUIAutomationInvokePattern comPattern;

    public WinInvokePattern(IUIAutomationInvokePattern comPattern) {
        this.comPattern = comPattern;
    }

    @Override
    public void invoke() {
        comPattern.invoke();
    }
}
