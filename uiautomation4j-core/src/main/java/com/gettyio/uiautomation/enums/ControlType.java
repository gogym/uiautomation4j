package com.gettyio.uiautomation.enums;

/**
 * UI Automation 控件类型枚举
 *
 * <p>对应 Windows UIAutomation 的 {@code UIA_ControlTypeIds} 枚举。</p>
 * <p>参考文档: https://learn.microsoft.com/en-us/windows/win32/uiautomation/uiautomation-control-type-identifiers</p>
 *
 * <p>每个枚举值对应一个 UIA ControlType ID（整数），
 * 通过 {@code IUIAutomationElement::get_CurrentControlType} 获取元素的控件类型。</p>
 *
 * <p>在搜索控件时，通过 {@code SearchCondition.controlType()} 指定要搜索的控件类型，
 * 底层会将其转换为 {@code UIA_ControlTypePropertyId (30003)} 的属性条件。</p>
 */
public enum ControlType {

    /** 应用栏控件 */
    AppBar(50040),
    /** 按钮控件 - 对应 ButtonControl，支持 InvokePattern */
    Button(50000),
    /** 日历控件 */
    Calendar(50001),
    /** 复选框控件 - 对应 CheckBoxControl */
    CheckBox(50002),
    /** 组合框/下拉列表控件 */
    ComboBox(50003),
    /** 自定义控件（无法识别标准类型时使用） */
    Custom(50025),
    /** 数据表格控件 */
    DataGrid(50026),
    /** 数据表格项 */
    DataItem(50027),
    /** 文档控件 */
    Document(50030),
    /** 编辑框控件 - 对应 EditControl，支持 ValuePattern */
    Edit(50004),
    /** 分组控件 */
    Group(50020),
    /** 标题控件 */
    Header(50034),
    /** 标题项控件 */
    HeaderItem(50035),
    /** 超链接控件 */
    Hyperlink(50005),
    /** 图片控件 */
    Image(50006),
    /** 列表控件 - 对应 ListControl，支持 SelectionPattern */
    List(50008),
    /** 列表项控件 - 对应 ListItemControl */
    ListItem(50007),
    /** 弹出菜单控件 */
    Menu(50009),
    /** 菜单栏控件 - 对应 MenuBarControl */
    MenuBar(50010),
    /** 菜单项控件 - 对应 MenuItemControl */
    MenuItem(50011),
    /** 面板控件 - 通用容器，对应 PaneControl */
    Pane(50033),
    /** 进度条控件 */
    ProgressBar(50012),
    /** 单选按钮控件 */
    RadioButton(50013),
    /** 滚动条控件 - 对应 ScrollBarControl，支持 ScrollPattern */
    ScrollBar(50014),
    /** 分隔符控件 */
    Separator(50038),
    /** 滑块控件 */
    Slider(50015),
    /** 微调按钮控件 */
    Spinner(50016),
    /** 拆分按钮控件 */
    SplitButton(50031),
    /** 状态栏控件 */
    StatusBar(50017),
    /** Tab 页签控件 - 对应 TabControl */
    Tab(50018),
    /** Tab 页签项 */
    TabItem(50019),
    /** 文本控件 - 对应 TextControl，显示只读文本 */
    Text(50024),
    /** 滑块控件 */
    Thumb(50029),
    /** 标题栏控件 - 对应 TitleBarControl */
    TitleBar(50037),
    /** 工具栏控件 */
    ToolBar(50021),
    /** 工具提示控件 */
    ToolTip(50022),
    /** 树形控件 - 对应 TreeControl */
    Tree(50023),
    /** 树形项控件 - 对应 TreeItemControl，支持 ExpandCollapsePattern */
    TreeItem(50028),
    /** 窗口控件 - 对应 WindowControl，支持 WindowPattern */
    Window(50032);

    /** UIA ControlType ID，对应 UIA_ControlTypeIds 枚举值 */
    private final int id;

    /**
     * 构造方法
     *
     * @param id UIA ControlType ID（如 Button=50000, Window=50032）
     */
    ControlType(int id) {
        this.id = id;
    }

    /**
     * 获取 UIA ControlType ID
     * <p>用于与 {@code IUIAutomationElement::get_CurrentControlType} 返回值进行比较</p>
     *
     * @return 控件类型 ID
     */
    public int getId() {
        return id;
    }

    /**
     * 根据 ID 查找对应的 ControlType 枚举值
     * <p>在 {@link com.gettyio.uiautomation.win.WinControlFactory} 中使用，
     * 将从 COM 接口获取的 int 类型 ControlType 转换为 Java 枚举。</p>
     *
     * @param id UIA ControlType ID
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
}
