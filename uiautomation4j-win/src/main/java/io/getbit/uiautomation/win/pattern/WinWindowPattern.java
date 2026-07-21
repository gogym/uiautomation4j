package io.getbit.uiautomation.win.pattern;

import io.getbit.uiautomation.pattern.WindowPattern;
import io.getbit.uiautomation.win.com.IUIAutomationWindowPattern;

/**
 * WindowPattern 的 Windows 实现
 *
 * <p>将 core 模块的 {@link WindowPattern} 接口桥接到 Windows COM 层的
 * {@link IUIAutomationWindowPattern}。提供窗口关闭、最大化、最小化、恢复等操作。</p>
 *
 * <p>注意: {@code setTopmost()} 暂未实现，因为 UIAutomation 没有直接设置 Topmost 的 API，
 * 需要通过 Win32 API {@code SetWindowPos} 实现。</p>
 */
public class WinWindowPattern implements WindowPattern {

    /** 底层 COM WindowPattern 对象 */
    private final IUIAutomationWindowPattern comPattern;

    /**
     * 构造 WinWindowPattern
     *
     * @param comPattern IUIAutomationWindowPattern COM 包装对象
     */
    public WinWindowPattern(IUIAutomationWindowPattern comPattern) {
        this.comPattern = comPattern;
    }

    /**
     * 关闭窗口
     * <p>调用 IUIAutomationWindowPattern::Close()，相当于点击窗口关闭按钮
     */
    @Override
    public void close() {
        comPattern.close();
    }

    /**
     * 判断窗口是否可关闭
     * <p>WindowPattern 本身就意味着窗口支持关闭操作，因此始终返回 true
     *
     * @return 始终返回 true
     */
    @Override
    public boolean canClose() {
        return true; // WindowPattern 本身就意味着可以关闭
    }

    /**
     * 最大化窗口
     * <p>设置窗口视觉状态为 WindowVisualState_Maximized
     */
    @Override
    public void maximize() {
        comPattern.setWindowVisualState(IUIAutomationWindowPattern.WindowVisualState_Maximized);
    }

    /**
     * 最小化窗口
     * <p>设置窗口视觉状态为 WindowVisualState_Minimized
     */
    @Override
    public void minimize() {
        comPattern.setWindowVisualState(IUIAutomationWindowPattern.WindowVisualState_Minimized);
    }

    /**
     * 恢复窗口到正常大小
     * <p>设置窗口视觉状态为 WindowVisualState_Normal
     */
    @Override
    public void restore() {
        comPattern.setWindowVisualState(IUIAutomationWindowPattern.WindowVisualState_Normal);
    }

    /**
     * 判断窗口是否置顶
     * <p>查询 UIA_IsWindowTopmost 属性
     *
     * @return 如果窗口置顶返回 true
     */
    @Override
    public boolean isTopmost() {
        return comPattern.isTopmost();
    }

    /**
     * 设置窗口置顶状态
     * <p>注意：UIAutomation 没有直接设置 Topmost 的 API，
     * 需要通过 Win32 API {@code SetWindowPos} 配合 {@code HWND_TOPMOST} 实现。
     * 当前版本暂未实现，留作后续扩展。
     *
     * @param topmost 是否置顶
     */
    @Override
    public void setTopmost(boolean topmost) {
        // UIAutomation 没有直接设置 Topmost 的 API
        // 需要通过 Win32 API SetWindowPos 实现
        // 此处留作后续通过 User32 实现
    }

    /**
     * 获取窗口当前视觉状态
     * <p>返回值对应 WindowVisualState 常量：
     * <ul>
     *   <li>0 - Normal（正常）</li>
     *   <li>1 - Maximized（最大化）</li>
     *   <li>2 - Minimized（最小化）</li>
     * </ul>
     *
     * @return 窗口视觉状态常量
     */
    @Override
    public int getVisualState() {
        return comPattern.getVisualState();
    }
}
