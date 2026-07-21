package com.gettyio.uiautomation.win.pattern;

import com.gettyio.uiautomation.pattern.WindowPattern;
import com.gettyio.uiautomation.win.com.IUIAutomationWindowPattern;

/**
 * WindowPattern 的 Windows 实现
 */
public class WinWindowPattern implements WindowPattern {

    private final IUIAutomationWindowPattern comPattern;

    public WinWindowPattern(IUIAutomationWindowPattern comPattern) {
        this.comPattern = comPattern;
    }

    @Override
    public void close() {
        comPattern.close();
    }

    @Override
    public boolean canClose() {
        return true; // WindowPattern 本身就意味着可以关闭
    }

    @Override
    public void maximize() {
        comPattern.setWindowVisualState(IUIAutomationWindowPattern.WindowVisualState_Maximized);
    }

    @Override
    public void minimize() {
        comPattern.setWindowVisualState(IUIAutomationWindowPattern.WindowVisualState_Minimized);
    }

    @Override
    public void restore() {
        comPattern.setWindowVisualState(IUIAutomationWindowPattern.WindowVisualState_Normal);
    }

    @Override
    public boolean isTopmost() {
        return comPattern.isTopmost();
    }

    @Override
    public void setTopmost(boolean topmost) {
        // UIAutomation 没有直接设置 Topmost 的 API
        // 需要通过 Win32 API SetWindowPos 实现
        // 此处留作后续通过 User32 实现
    }

    @Override
    public int getVisualState() {
        return comPattern.getVisualState();
    }
}
