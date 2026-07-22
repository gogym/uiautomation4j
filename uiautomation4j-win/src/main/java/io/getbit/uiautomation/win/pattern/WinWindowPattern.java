package io.getbit.uiautomation.win.pattern;

import io.getbit.uiautomation.pattern.WindowPattern;
import io.getbit.uiautomation.win.com.IUIAutomationWindowPattern;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef;

/**
 * WindowPattern 的 Windows 实现
 *
 * <p>将 core 模块的 {@link WindowPattern} 接口桥接到 Windows COM 层的
 * {@link IUIAutomationWindowPattern}。提供窗口关闭、最大化、最小化、恢复、置顶等操作。</p>
 *
 * <p>其中 {@code setTopmost()} 通过 Win32 API {@code SetWindowPos} 实现，
 * 因为 UIAutomation 的 IUIAutomationWindowPattern 只支持读取 IsTopmost 属性，
 * 不支持设置置顶状态。</p>
 */
public class WinWindowPattern implements WindowPattern {

    /** HWND_TOPMOST - 将窗口置于所有非顶级窗口之上 */
    private static final long HWND_TOPMOST = -1L;
    /** HWND_NOTOPMOST - 将窗口置于所有非顶级窗口之下 */
    private static final long HWND_NOTOPMOST = -2L;
    /** SWP_NOMOVE - 保持当前位置不变 */
    private static final int SWP_NOMOVE = 0x0002;
    /** SWP_NOSIZE - 保持当前大小不变 */
    private static final int SWP_NOSIZE = 0x0001;
    /** SWP_NOACTIVATE - 不激活窗口 */
    private static final int SWP_NOACTIVATE = 0x0010;

    /** 底层 COM WindowPattern 对象 */
    private final IUIAutomationWindowPattern comPattern;
    /** 窗口原生句柄（HWND），用于 SetWindowPos 调用 */
    private final long hwnd;

    /**
     * 构造 WinWindowPattern
     *
     * @param comPattern IUIAutomationWindowPattern COM 包装对象
     * @param hwnd       窗口原生句柄（通过 IUIAutomationElement.getNativeWindowHandle() 获取）
     */
    public WinWindowPattern(IUIAutomationWindowPattern comPattern, long hwnd) {
        this.comPattern = comPattern;
        this.hwnd = hwnd;
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
     * <p>通过 Win32 API {@code SetWindowPos} 实现。UIAutomation 的
     * IUIAutomationWindowPattern 只提供只读的 IsTopmost 属性，无法设置置顶。
     * 因此这里使用窗口原生句柄（HWND）调用 {@code SetWindowPos}，
     * 配合 {@code HWND_TOPMOST} / {@code HWND_NOTOPMOST} 标志完成置顶/取消置顶。</p>
     *
     * @param topmost 是否置顶
     */
    @Override
    public void setTopmost(boolean topmost) {
        if (hwnd == 0) {
            return;
        }
        WinDef.HWND hWnd = new WinDef.HWND(Pointer.createConstant(hwnd));
        WinDef.HWND hWndInsertAfter = new WinDef.HWND(
                Pointer.createConstant(topmost ? HWND_TOPMOST : HWND_NOTOPMOST));
        User32.INSTANCE.SetWindowPos(hWnd, hWndInsertAfter,
                0, 0, 0, 0,
                SWP_NOMOVE | SWP_NOSIZE | SWP_NOACTIVATE);
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
