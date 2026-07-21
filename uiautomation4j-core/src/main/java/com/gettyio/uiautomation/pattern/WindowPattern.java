package com.gettyio.uiautomation.pattern;

/**
 * WindowPattern - 用于窗口级操作
 *
 * <p>对应 UIAutomation 的 {@code IUIAutomationWindowPattern} (PatternId: 10009)。
 * 参考文档: https://learn.microsoft.com/en-us/windows/win32/api/uiautomationclient/nn-uiautomationclient-iuiautomationwindowpattern</p>
 *
 * <p>支持控件：Window</p>
 *
 * <p>提供窗口关闭、最大化、最小化、恢复、置顶等操作。</p>
 */
public interface WindowPattern extends Pattern {

    /**
     * 关闭窗口
     * <p>对应 COM 方法: {@code IUIAutomationWindowPattern::Close()}</p>
     */
    void close();

    /**
     * 是否可以关闭
     * <p>对应 COM 属性: {@code get_CurrentCanClose}</p>
     *
     * @return true 表示窗口可以被关闭
     */
    boolean canClose();

    /**
     * 最大化窗口
     * <p>通过 {@code SetWindowVisualState(WindowVisualState_Maximized)} 实现</p>
     */
    void maximize();

    /**
     * 最小化窗口
     * <p>通过 {@code SetWindowVisualState(WindowVisualState_Minimized)} 实现</p>
     */
    void minimize();

    /**
     * 恢复窗口（从最大化/最小化状态恢复为正常状态）
     * <p>通过 {@code SetWindowVisualState(WindowVisualState_Normal)} 实现</p>
     */
    void restore();

    /**
     * 窗口是否置顶
     * <p>对应 COM 属性: {@code get_CurrentIsTopmost(BOOL* pIsTopmost)}</p>
     *
     * @return true 表示窗口始终置顶
     */
    boolean isTopmost();

    /**
     * 设置窗口置顶
     * <p>注意: UIAutomation 没有直接设置 Topmost 的 API，
     * 需要通过 Win32 API {@code SetWindowPos} 实现</p>
     *
     * @param topmost true 表示置顶
     */
    void setTopmost(boolean topmost);

    /**
     * 获取窗口视觉状态
     * <p>对应 COM 属性: {@code get_CurrentWindowVisualState(WindowVisualState* pState)}</p>
     * <ul>
     *   <li>0 = Normal（正常）</li>
     *   <li>1 = Maximized（最大化）</li>
     *   <li>2 = Minimized（最小化）</li>
     * </ul>
     *
     * @return 窗口视觉状态值
     */
    int getVisualState();
}
