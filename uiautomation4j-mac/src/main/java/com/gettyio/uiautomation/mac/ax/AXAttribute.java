package com.gettyio.uiautomation.mac.ax;

/**
 * macOS Accessibility 属性名常量
 *
 * <p>定义 AXUIElement 可查询的属性名（对应 Windows UIAutomation 的 PropertyId）。
 * 这些常量用于 {@link AXUIElement#getAttribute(String)} 等方法的参数。</p>
 *
 * <h3>与 Windows UIA PropertyId 的对应关系：</h3>
 * <table>
 *   <tr><th>macOS AX 属性</th><th>Windows UIA PropertyId</th></tr>
 *   <tr><td>kAXRoleAttribute</td><td>UIA_ControlTypePropertyId (30003)</td></tr>
 *   <tr><td>kAXTitleAttribute</td><td>UIA_NamePropertyId (30005)</td></tr>
 *   <tr><td>kAXValueAttribute</td><td>UIA_ValueValuePropertyId (30045)</td></tr>
 *   <tr><td>kAXEnabledAttribute</td><td>UIA_IsEnabledPropertyId (30010)</td></tr>
 *   <tr><td>kAXPositionAttribute</td><td>UIA_BoundingRectanglePropertyId (30001)</td></tr>
 *   <tr><td>kAXChildrenAttribute</td><td>通过 TreeWalker 遍历</td></tr>
 * </table>
 *
 * @see <a href="https://developer.apple.com/documentation/applicationservices/axuielement/attribute_names">Attribute Names</a>
 */
public final class AXAttribute {

    // ==================== 基本标识属性 ====================

    /** 控件角色（类似 Windows ControlType）- CFString */
    public static final String ROLE = "AXRole";

    /** 控件子角色 - CFString */
    public static final String SUBROLE = "AXSubrole";

    /** 标题/名称（类似 Windows Name 属性）- CFString */
    public static final String TITLE = "AXTitle";

    /** 描述信息 - CFString */
    public static final String DESCRIPTION = "AXDescription";

    /** 帮助文本 - CFString */
    public static final String HELP = "AXHelp";

    /** 唯一标识符 - CFString */
    public static final String IDENTIFIER = "AXIdentifier";

    // ==================== 值属性 ====================

    /** 当前值（文本字段、滑块等）- CFTypeRef（类型取决于控件） */
    public static final String VALUE = "AXValue";

    /** 值描述（如滑块的当前值文本）- CFString */
    public static final String VALUE_DESCRIPTION = "AXValueDescription";

    /** 最小值（滑块、进度条等）- CFNumber */
    public static final String MIN_VALUE = "AXMinValue";

    /** 最大值 - CFNumber */
    public static final String MAX_VALUE = "AXMaxValue";

    /** 值增量（步进值）- CFNumber */
    public static final String VALUE_INCREMENT = "AXValueIncrement";

    // ==================== 状态属性 ====================

    /** 是否启用 - CFBoolean */
    public static final String ENABLED = "AXEnabled";

    /** 是否有焦点 - CFBoolean */
    public static final String FOCUSED = "AXFocused";

    /** 是否可见（macOS 无此属性，需通过位置计算） */
    // 注意：macOS AX API 没有直接的 AXVisible 属性

    // ==================== 几何属性 ====================

    /** 屏幕位置（AXValue → CGPoint {x, y}） */
    public static final String POSITION = "AXPosition";

    /** 尺寸（AXValue → CGSize {width, height}） */
    public static final String SIZE = "AXSize";

    // ==================== 树结构属性 ====================

    /** 子元素列表 - CFArray of AXUIElement */
    public static final String CHILDREN = "AXChildren";

    /** 子元素数量 - CFNumber */
    public static final String CHILDREN_COUNT = "AXChildrenInNavigationOrder";

    /** 父元素 - AXUIElement */
    public static final String PARENT = "AXParent";

    /** 窗口元素 - AXUIElement */
    public static final String WINDOW = "AXWindow";

    /** 顶层 UI 元素 - AXUIElement */
    public static final String TOP_LEVEL_UI_ELEMENT = "AXTopLevelUIElement";

    // ==================== 窗口属性 ====================

    /** 窗口是否最小化 - CFBoolean */
    public static final String MINIMIZED = "AXMinimized";

    /** 窗口是否全屏 - CFBoolean */
    public static final String FULL_SCREEN = "AXFullScreen";

    /** 窗口的主按钮（关闭/最小化/缩放按钮）- AXUIElement */
    public static final String MAIN = "AXMain";

    // ==================== 表格/列表属性 ====================

    /** 可见行 - CFArray */
    public static final String VISIBLE_ROWS = "AXVisibleRows";

    /** 可见列 - CFArray */
    public static final String VISIBLE_COLUMNS = "AXVisibleColumns";

    /** 选中行 - CFArray */
    public static final String SELECTED_ROWS = "AXSelectedRows";

    /** 选中单元格 - CFArray */
    public static final String SELECTED_CELLS = "AXSelectedCells";

    // ==================== 文本属性 ====================

    /** 选中文本 - CFString */
    public static final String SELECTED_TEXT = "AXSelectedText";

    /** 选中文本范围 - CFRange (通过 AXValue) */
    public static final String SELECTED_TEXT_RANGE = "AXSelectedTextRange";

    /** 可见文本范围 - CFRange */
    public static final String VISIBLE_TEXT_RANGE = "AXVisibleCharacterRange";

    /** 文本行数 - CFNumber */
    public static final String NUMBER_OF_CHARACTERS = "AXNumberOfCharacters";

    // ==================== 菜单属性 ====================

    /** 菜单项 - CFArray */
    public static final String MENU_ITEMS = "AXMenuItemCmdChar";

    /** 菜单命令键修饰符 - CFNumber */
    public static final String MENU_ITEM_CMD_MODIFIERS = "AXMenuItemCmdModifiers";

    // ==================== 滚动属性 ====================

    /** 水平滚动条 - AXUIElement */
    public static final String HORIZONTAL_SCROLL_BAR = "AXHorizontalScrollBar";

    /** 垂直滚动条 - AXUIElement */
    public static final String VERTICAL_SCROLL_BAR = "AXVerticalScrollBar";

    /** 可见内容 - CFArray */
    public static final String VISIBLE_CONTENT = "AXVisibleContent";

    // ==================== 进程信息 ====================

    /** 进程 ID - CFNumber */
    public static final String PROCESS_ID = "AXProcessIdentifier";

    /** 应用是否前台 - CFBoolean */
    public static final String FRONTMOST = "AXFrontmost";

    /** 应用 PID - CFNumber */
    public static final String PID = "AXPID";

    private AXAttribute() {
        // 常量类禁止实例化
    }
}
