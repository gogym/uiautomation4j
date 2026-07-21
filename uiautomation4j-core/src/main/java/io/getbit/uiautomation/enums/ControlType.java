package io.getbit.uiautomation.enums;

/**
 * UI Automation 控件类型枚举（跨平台统一）
 *
 * <p>统一管理 Windows 和 macOS 两个平台的控件类型。
 * 每个枚举值标注了所属平台（{@link Platform}），便于在跨平台代码中判断是否可用。</p>
 *
 * <h3>平台 ID 范围约定：</h3>
 * <ul>
 *   <li>Windows UIA ControlType ID：50000 - 50040</li>
 *   <li>macOS AXRole 映射 ID：60000 - 60099</li>
 *   <li>Linux AT-SPI Role 映射 ID：70000 - 70099</li>
 *   <li>跨平台通用类型：使用 Windows ID 作为基准（各平台通过角色映射到相同枚举值）</li>
 * </ul>
 *
 * <h3>跨平台使用约定：</h3>
 * <ul>
 *   <li>{@code ControlType.Button} 在 Windows 对应 UIA_ButtonControlTypeId (50000)，
 *       在 macOS 对应 AXRole = "AXButton"</li>
 *   <li>{@code ControlType.Window} 在 Windows 对应 UIA_WindowControlTypeId (50032)，
 *       在 macOS 对应 AXRole = "AXWindow"</li>
 *   <li>仅标记为 {@link Platform#MAC} 的类型在 Windows 上不可用，反之亦然</li>
 * </ul>
 *
 * @see Platform
 */
public enum ControlType {

    // ==================== 跨平台通用类型（Windows + macOS） ====================

    /** 按钮控件 - Win: ButtonControl (InvokePattern) / Mac: AXButton */
    Button(50000, Platform.CROSS_PLATFORM),
    /** 复选框控件 - Win: CheckBoxControl / Mac: AXCheckBox */
    CheckBox(50002, Platform.CROSS_PLATFORM),
    /** 组合框/下拉列表 - Win: ComboBox / Mac: AXPopUpButton (映射) */
    ComboBox(50003, Platform.CROSS_PLATFORM),
    /** 编辑框/文本输入 - Win: EditControl (ValuePattern) / Mac: AXTextField, AXTextArea */
    Edit(50004, Platform.CROSS_PLATFORM),
    /** 超链接 - Win: Hyperlink / Mac: AXLink */
    Hyperlink(50005, Platform.CROSS_PLATFORM),
    /** 图片 - Win: Image / Mac: AXImage */
    Image(50006, Platform.CROSS_PLATFORM),
    /** 列表控件 - Win: ListControl (SelectionPattern) / Mac: AXList, AXTable */
    List(50008, Platform.CROSS_PLATFORM),
    /** 列表项 - Win: ListItemControl / Mac: AXRow, AXCell */
    ListItem(50007, Platform.CROSS_PLATFORM),
    /** 菜单 - Win: Menu / Mac: AXMenu */
    Menu(50009, Platform.CROSS_PLATFORM),
    /** 菜单栏 - Win: MenuBarControl / Mac: AXMenuBar */
    MenuBar(50010, Platform.CROSS_PLATFORM),
    /** 菜单项 - Win: MenuItemControl / Mac: AXMenuItem */
    MenuItem(50011, Platform.CROSS_PLATFORM),
    /** 单选按钮 - Win: RadioButton / Mac: AXRadioButton */
    RadioButton(50013, Platform.CROSS_PLATFORM),
    /** 滚动条 - Win: ScrollBarControl (ScrollPattern) / Mac: AXScrollBar */
    ScrollBar(50014, Platform.CROSS_PLATFORM),
    /** 滑块 - Win: Slider / Mac: AXSlider */
    Slider(50015, Platform.CROSS_PLATFORM),
    /** 状态栏 - Win: StatusBar / Mac: AXStaticText (部分映射) */
    StatusBar(50017, Platform.CROSS_PLATFORM),
    /** Tab 页签 - Win: TabControl / Mac: AXTabGroup */
    Tab(50018, Platform.CROSS_PLATFORM),
    /** Tab 页签项 - Win: TabItem / Mac: AXTabGroup 的子元素 */
    TabItem(50019, Platform.CROSS_PLATFORM),
    /** 文本控件（只读） - Win: TextControl / Mac: AXStaticText */
    Text(50024, Platform.CROSS_PLATFORM),
    /** 树形控件 - Win: TreeControl / Mac: AXOutline */
    Tree(50023, Platform.CROSS_PLATFORM),
    /** 树形项 - Win: TreeItemControl (ExpandCollapsePattern) / Mac: AXCell, AXRow */
    TreeItem(50028, Platform.CROSS_PLATFORM),
    /** 窗口控件 - Win: WindowControl (WindowPattern) / Mac: AXWindow */
    Window(50032, Platform.CROSS_PLATFORM),
    /** 面板/容器 - Win: PaneControl / Mac: AXGroup */
    Pane(50033, Platform.CROSS_PLATFORM),
    /** 自定义控件 - Win: Custom / Mac: 无法识别标准类型时使用 */
    Custom(50025, Platform.CROSS_PLATFORM),
    /** 文档控件 - Win: Document / Mac: AXTextArea (部分映射) */
    Document(50030, Platform.CROSS_PLATFORM),
    /** 分组控件 - Win: Group / Mac: AXGroup (部分映射) */
    Group(50020, Platform.CROSS_PLATFORM),
    /** 进度条 - Win: ProgressBar / Mac: AXProgressIndicator */
    ProgressBar(50012, Platform.CROSS_PLATFORM),
    /** 工具栏 - Win: ToolBar / Mac: AXToolbar */
    ToolBar(50021, Platform.CROSS_PLATFORM),
    /** 分隔符 - Win: Separator / Mac: AXSplitter */
    Separator(50038, Platform.CROSS_PLATFORM),

    // ==================== Windows 专用类型 ====================

    /** 应用栏控件（仅 Windows） */
    AppBar(50040, Platform.WINDOWS),
    /** 日历控件（仅 Windows） */
    Calendar(50001, Platform.WINDOWS),
    /** 数据表格控件（仅 Windows） */
    DataGrid(50026, Platform.WINDOWS),
    /** 数据表格项（仅 Windows） */
    DataItem(50027, Platform.WINDOWS),
    /** 标题控件（仅 Windows） */
    Header(50034, Platform.WINDOWS),
    /** 标题项控件（仅 Windows） */
    HeaderItem(50035, Platform.WINDOWS),
    /** 面板控件（仅 Windows） */
    // Pane 已在跨平台中定义
    /** 微调按钮（仅 Windows） */
    Spinner(50016, Platform.WINDOWS),
    /** 拆分按钮（仅 Windows） */
    SplitButton(50031, Platform.WINDOWS),
    /** 滑块拖块（仅 Windows） */
    Thumb(50029, Platform.WINDOWS),
    /** 标题栏（仅 Windows） */
    TitleBar(50037, Platform.WINDOWS),
    /** 工具提示（仅 Windows） */
    ToolTip(50022, Platform.WINDOWS),

    // ==================== macOS 专用类型 ====================

    /** 颜色选择器（仅 macOS） - AXRole: AXColorWell */
    ColorWell(60000, Platform.MAC),
    /** 抽屉控件（仅 macOS） - AXRole: AXDrawer */
    Drawer(60001, Platform.MAC),
    /** 模态表单（仅 macOS） - AXRole: AXSheet */
    Sheet(60002, Platform.MAC),
    /** 拖拽手柄（仅 macOS） - AXRole: AXHandle */
    Handle(60003, Platform.MAC),
    /** 增减器/微调器（仅 macOS） - AXRole: AXIncrementor */
    Incrementor(60004, Platform.MAC),
    /** 浏览器/列视图（仅 macOS） - AXRole: AXBrowser */
    Browser(60005, Platform.MAC),
    /** 列控件（仅 macOS） - AXRole: AXColumn */
    Column(60006, Platform.MAC),
    /** 标尺控件（仅 macOS） - AXRole: AXRuler */
    Ruler(60007, Platform.MAC),
    /** 分割视图（仅 macOS） - AXRole: AXSplitGroup */
    SplitGroup(60008, Platform.MAC),
    /** 文本区域（仅 macOS） - AXRole: AXTextArea（多行文本） */
    TextArea(60009, Platform.MAC),
    /** 文本字段（仅 macOS） - AXRole: AXTextField（单行文本） */
    TextField(60010, Platform.MAC),
    /** 弹出按钮（仅 macOS） - AXRole: AXPopUpButton */
    PopUpButton(60011, Platform.MAC),
    /** 应用级 UI 元素（仅 macOS） - AXRole: AXApplication */
    Application(60012, Platform.MAC),

    // ==================== Linux 专用类型（AT-SPI2 Role） ====================

    /** 警告框（仅 Linux） - AT-SPI Role: ROLE_ALERT */
    Alert(70000, Platform.LINUX),
    /** 对话框（仅 Linux） - AT-SPI Role: ROLE_DIALOG / ROLE_FRAME */
    Dialog(70001, Platform.LINUX),
    /** 填充器/空白容器（仅 Linux） - AT-SPI Role: ROLE_FILLER */
    Filler(70002, Platform.LINUX),
    /** 图标控件（仅 Linux） - AT-SPI Role: ROLE_ICON */
    Icon(70003, Platform.LINUX),
    /** 标签页面板（仅 Linux） - AT-SPI Role: ROLE_PAGE_TAB */
    PageTab(70004, Platform.LINUX),
    /** 标签页内容区（仅 Linux） - AT-SPI Role: ROLE_PAGE_TAB_LIST */
    PageTabList(70005, Platform.LINUX),
    /** 弹出菜单（仅 Linux） - AT-SPI Role: ROLE_POPUP_MENU */
    PopupMenu(70006, Platform.LINUX),
    /** 表格控件（仅 Linux） - AT-SPI Role: ROLE_TABLE */
    Table(70007, Platform.LINUX),
    /** 表格单元格（仅 Linux） - AT-SPI Role: ROLE_TABLE_CELL */
    TableCell(70008, Platform.LINUX),
    /** 表格列头（仅 Linux） - AT-SPI Role: ROLE_TABLE_COLUMN_HEADER */
    TableColumnHeader(70009, Platform.LINUX),
    /** 表格行头（仅 Linux） - AT-SPI Role: ROLE_TABLE_ROW_HEADER */
    TableRowHeader(70010, Platform.LINUX);


    /** 控件类型 ID（Windows: 50000+, macOS: 60000+） */
    private final int id;

    /** 所属平台 */
    private final Platform platform;

    /**
     * 构造方法
     *
     * @param id       控件类型 ID
     * @param platform 所属平台
     */
    ControlType(int id, Platform platform) {
        this.id = id;
        this.platform = platform;
    }

    /**
     * 获取控件类型 ID
     *
     * @return 控件类型 ID
     */
    public int getId() {
        return id;
    }

    /**
     * 获取所属平台
     *
     * @return 平台标识
     */
    public Platform getPlatform() {
        return platform;
    }

    /**
     * 判断当前类型是否在指定平台上可用
     *
     * @param targetPlatform 目标平台
     * @return 如果可用返回 true
     */
    public boolean isAvailableOn(Platform targetPlatform) {
        return this.platform == Platform.CROSS_PLATFORM || this.platform == targetPlatform;
    }

    /**
     * 根据 ID 查找对应的 ControlType 枚举值
     *
     * @param id 控件类型 ID
     * @return 对应的枚举值
     * @throws IllegalArgumentException 如果 ID 不在已知范围内
     */
    public static ControlType fromId(int id) {
        for (ControlType type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown ControlType id: " + id);
    }

    /**
     * 根据 macOS AXRole 字符串查找对应的 ControlType
     * <p>在 Mac 后端中使用，将 AXRole 映射到统一的 ControlType 枚举</p>
     *
     * @param axRole macOS AXRole 字符串（如 "AXButton", "AXWindow"）
     * @return 对应的 ControlType，如果找不到返回 {@link #Custom}
     */
    public static ControlType fromMacRole(String axRole) {
        if (axRole == null) return Custom;
        switch (axRole) {
            case "AXButton":            return Button;
            case "AXCheckBox":          return CheckBox;
            case "AXPopUpButton":       return PopUpButton;
            case "AXTextField":         return TextField;
            case "AXTextArea":          return TextArea;
            case "AXStaticText":        return Text;
            case "AXList":              return List;
            case "AXTable":             return List;
            case "AXRow":               return ListItem;
            case "AXCell":              return ListItem;
            case "AXMenu":              return Menu;
            case "AXMenuBar":           return MenuBar;
            case "AXMenuItem":          return MenuItem;
            case "AXRadioButton":       return RadioButton;
            case "AXScrollBar":         return ScrollBar;
            case "AXSlider":            return Slider;
            case "AXTabGroup":          return Tab;
            case "AXOutline":           return Tree;
            case "AXWindow":            return Window;
            case "AXGroup":             return Pane;
            case "AXImage":             return Image;
            case "AXLink":              return Hyperlink;
            case "AXProgressIndicator": return ProgressBar;
            case "AXToolbar":           return ToolBar;
            case "AXSplitter":          return Separator;
            case "AXColorWell":         return ColorWell;
            case "AXDrawer":            return Drawer;
            case "AXSheet":             return Sheet;
            case "AXBrowser":           return Browser;
            case "AXColumn":            return Column;
            case "AXRuler":             return Ruler;
            case "AXSplitGroup":        return SplitGroup;
            case "AXApplication":       return Application;
            case "AXIncrementor":       return Incrementor;
            default:                    return Custom;
        }
    }

    /**
     * 根据 Linux AT-SPI2 Role 值查找对应的 ControlType
     * <p>在 Linux 后端中使用，将 AT-SPI Role 整数映射到统一的 ControlType 枚举</p>
     *
     * @param atspiRole AT-SPI2 Role 整数值（如 74=ROLE_PUSH_BUTTON, 27=ROLE_FRAME）
     * @return 对应的 ControlType，如果找不到返回 {@link #Custom}
     * @see <a href="https://gitlab.gnome.org/GNOME/at-spi2-core/blob/main/xml/Role.xml">AT-SPI2 Role</a>
     */
    public static ControlType fromAtspiRole(int atspiRole) {
        switch (atspiRole) {
            case 74:  return Button;            // ROLE_PUSH_BUTTON
            case 11:  return CheckBox;          // ROLE_CHECK_BOX
            case 15:  return ComboBox;          // ROLE_COMBO_BOX
            case 66:  return Edit;              // ROLE_TEXT (editable)
            case 79:  return Hyperlink;         // ROLE_LINK
            case 31:  return Image;             // ROLE_IMAGE
            case 35:  return List;              // ROLE_LIST
            case 36:  return ListItem;          // ROLE_LIST_ITEM
            case 37:  return Menu;              // ROLE_MENU
            case 38:  return MenuBar;           // ROLE_MENU_BAR
            case 39:  return MenuItem;          // ROLE_MENU_ITEM
            case 49:  return RadioButton;       // ROLE_RADIO_BUTTON
            case 53:  return ScrollBar;         // ROLE_SCROLL_BAR
            case 56:  return Slider;            // ROLE_SLIDER
            case 59:  return StatusBar;         // ROLE_STATUS_BAR
            case 42:  return Tab;               // ROLE_PAGE_TAB
            case 43:  return TabItem;           // ROLE_PAGE_TAB_LIST (items)
            case 33:  return Text;              // ROLE_LABEL
            case 70:  return Tree;              // ROLE_TREE
            case 71:  return TreeItem;          // ROLE_TREE_TABLE
            case 27:  return Window;            // ROLE_FRAME
            case 20:  return Dialog;            // ROLE_DIALOG
            case 44:  return Pane;              // ROLE_PANEL
            case 22:  return Document;          // ROLE_DRAWING_AREA
            case 24:  return Group;             // ROLE_FILLER
            case 47:  return ProgressBar;       // ROLE_PROGRESS_BAR
            case 68:  return ToolBar;           // ROLE_TOOL_BAR
            case 55:  return Separator;         // ROLE_SEPARATOR
            case 2:   return Alert;             // ROLE_ALERT
            case 46:  return PopupMenu;         // ROLE_POPUP_MENU
            case 60:  return Table;             // ROLE_TABLE
            case 61:  return TableCell;         // ROLE_TABLE_CELL
            case 62:  return TableColumnHeader; // ROLE_TABLE_COLUMN_HEADER
            case 63:  return TableRowHeader;    // ROLE_TABLE_ROW_HEADER
            case 30:  return Icon;              // ROLE_ICON
            default:  return Custom;
        }
    }

    /**
     * 平台标识枚举
     * <p>用于标记控件类型所属的平台，支持跨平台代码判断</p>
     */
    public enum Platform {
        /** 跨平台通用类型（Windows + macOS + Linux 均有对应概念） */
        CROSS_PLATFORM,
        /** 仅 Windows 平台 */
        WINDOWS,
        /** 仅 macOS 平台 */
        MAC,
        /** 仅 Linux 平台（AT-SPI2） */
        LINUX
    }
}
