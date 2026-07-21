package com.gettyio.uiautomation.win.pattern;

import com.gettyio.uiautomation.pattern.InvokePattern;
import com.gettyio.uiautomation.win.com.IUIAutomationInvokePattern;

/**
 * InvokePattern 的 Windows 实现
 */
public class WinInvokePattern implements InvokePattern {

    private final IUIAutomationInvokePattern comPattern;

    public WinInvokePattern(IUIAutomationInvokePattern comPattern) {
        this.comPattern = comPattern;
    }

    @Override
    public void invoke() {
        comPattern.invoke();
    }
}
