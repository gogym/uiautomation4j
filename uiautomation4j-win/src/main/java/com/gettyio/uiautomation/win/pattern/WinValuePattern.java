package com.gettyio.uiautomation.win.pattern;

import com.gettyio.uiautomation.pattern.ValuePattern;
import com.gettyio.uiautomation.win.com.IUIAutomationValuePattern;

/**
 * ValuePattern 的 Windows 实现
 */
public class WinValuePattern implements ValuePattern {

    private final IUIAutomationValuePattern comPattern;

    public WinValuePattern(IUIAutomationValuePattern comPattern) {
        this.comPattern = comPattern;
    }

    @Override
    public String getValue() {
        return comPattern.getCurrentValue();
    }

    @Override
    public void setValue(String value) {
        comPattern.setValue(value);
    }

    @Override
    public boolean isReadOnly() {
        return comPattern.isReadOnly();
    }
}
