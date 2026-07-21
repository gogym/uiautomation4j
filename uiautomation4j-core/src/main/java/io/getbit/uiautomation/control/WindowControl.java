package io.getbit.uiautomation.control;

import io.getbit.uiautomation.condition.SearchCondition;
import io.getbit.uiautomation.enums.ControlType;

/**
 * 窗口控件
 *
 * <p>对应 UIAutomation 的 Window 控件类型 (ControlType=50032)。
 * 支持 {@link WindowPattern} 提供的窗口操作（关闭、最大化、最小化、置顶等）。</p>
 *
 * <p>使用示例：</p>
 * <pre>
 * // 通过 Builder 查找窗口
 * WindowControl notepad = Control.window().className("Notepad").findWindow();
 * // 窗口操作
 * notepad.maximize();
 * notepad.close();
 * </pre>
 */
public class WindowControl extends Control {

    /**
     * 默认构造 - 用于内部创建
     */
    public WindowControl() {
        super();
    }

    /**
     * 根据搜索条件构造
     *
     * @param searchCondition 搜索条件
     */
    public WindowControl(SearchCondition searchCondition) {
        super(searchCondition);
    }

    @Override
    public ControlType getControlType() {
        return ControlType.Window;
    }

    /**
     * 设置窗口置顶
     * <p>通过 {@link WindowPattern#setTopmost(boolean)} 实现</p>
     *
     * @param topmost true 表示始终置顶
     */
    public void setTopmost(boolean topmost) {
        getWindowPattern().setTopmost(topmost);
    }

    /**
     * 关闭窗口
     * <p>通过 {@link WindowPattern#close()} 实现，
     * 对应 COM 方法: {@code IUIAutomationWindowPattern::Close()}</p>
     */
    public void close() {
        getWindowPattern().close();
    }

    /**
     * 最大化窗口
     * <p>通过 {@link WindowPattern#maximize()} 实现，
     * 对应 COM 方法: {@code SetWindowVisualState(WindowVisualState_Maximized)}</p>
     */
    public void maximize() {
        getWindowPattern().maximize();
    }

    /**
     * 最小化窗口
     * <p>通过 {@link WindowPattern#minimize()} 实现，
     * 对应 COM 方法: {@code SetWindowVisualState(WindowVisualState_Minimized)}</p>
     */
    public void minimize() {
        getWindowPattern().minimize();
    }
}
