package io.getbit.uiautomation.mac.ax;

/**
 * macOS Accessibility 动作名常量
 *
 * <p>定义 AXUIElement 可执行的动作名（对应 Windows UIAutomation 的 Pattern 操作）。
 * 这些常量用于 {@link AXUIElement#performAction(String)} 方法的参数。</p>
 *
 * <h3>与 Windows Pattern 操作的对应关系：</h3>
 * <table>
 *   <tr><th>macOS AX 动作</th><th>Windows Pattern 操作</th></tr>
 *   <tr><td>kAXPressAction</td><td>InvokePattern::Invoke</td></tr>
 *   <tr><td>kAXConfirmAction</td><td>InvokePattern::Invoke（确认类控件）</td></tr>
 *   <tr><td>kAXCancelAction</td><td>WindowPattern::Close（取消/关闭）</td></tr>
 *   <tr><td>kAXIncrementAction</td><td>手动设置 Value 增加</td></tr>
 *   <tr><td>kAXDecrementAction</td><td>手动设置 Value 减少</td></tr>
 *   <tr><td>kAXShowMenuAction</td><td>右键点击（上下文菜单）</td></tr>
 * </table>
 *
 * @see <a href="https://developer.apple.com/documentation/applicationservices/axuielement/action_names">Action Names</a>
 */
public final class AXAction {

    // ==================== 基本操作 ====================

    /** 按下操作 - 等价于 Windows InvokePattern::Invoke */
    public static final String PRESS = "AXPress";

    /** 确认操作 - 用于对话框确认按钮等 */
    public static final String CONFIRM = "AXConfirm";

    /** 取消操作 - 用于对话框取消按钮 */
    public static final String CANCEL = "AXCancel";

    /** 显示上下文菜单 - 等价于 Windows 右键点击 */
    public static final String SHOW_MENU = "AXShowMenu";

    // ==================== 值操作 ====================

    /** 增加值 - 用于滑块、微调器等 */
    public static final String INCREMENT = "AXIncrement";

    /** 减少值 - 用于滑块、微调器等 */
    public static final String DECREMENT = "AXDecrement";

    // ==================== 窗口操作 ====================

    /** 关闭窗口 - 等价于 Windows WindowPattern::Close */
    public static final String CLOSE = "AXClose";

    /** 最大化窗口 */
    public static final String RAISE = "AXRaise";

    // ==================== 滚动操作 ====================

    /** 向上滚动 */
    public static final String SCROLL_UP = "AXScrollUp";

    /** 向下滚动 */
    public static final String SCROLL_DOWN = "AXScrollDown";

    /** 向左滚动 */
    public static final String SCROLL_LEFT = "AXScrollLeft";

    /** 向右滚动 */
    public static final String SCROLL_RIGHT = "AXScrollRight";

    /** 滚动到可见 */
    public static final String SCROLL_TO_VISIBLE = "AXScrollToVisible";

    // ==================== 焦点操作 ====================

    /** 设置焦点 */
    public static final String FOCUS = "AXFocus";

    // ==================== 展开/折叠 ====================

    /** 展开（树节点、下拉列表等）- 等价于 Windows ExpandCollapsePattern::Expand */
    public static final String EXPAND = "AXExpand";

    /** 折叠 - 等价于 Windows ExpandCollapsePattern::Collapse */
    public static final String COLLAPSE = "AXCollapse";

    private AXAction() {
        // 常量类禁止实例化
    }
}
