package com.gettyio.uiautomation.enums;

/**
 * UI Automation 控件类型枚举
 * 对应 Windows UIAutomation 的 ControlType
 */
public enum ControlType {

    AppBar(50040),
    Button(50000),
    Calendar(50001),
    CheckBox(50002),
    ComboBox(50003),
    Custom(50025),
    DataGrid(50026),
    DataItem(50027),
    Document(50030),
    Edit(50004),
    Group(50020),
    Header(50034),
    HeaderItem(50035),
    Hyperlink(50005),
    Image(50006),
    List(50008),
    ListItem(50007),
    Menu(50009),
    MenuBar(50010),
    MenuItem(50011),
    Pane(50033),
    ProgressBar(50012),
    RadioButton(50013),
    ScrollBar(50014),
    Separator(50038),
    Slider(50015),
    Spinner(50016),
    SplitButton(50031),
    StatusBar(50017),
    Tab(50018),
    TabItem(50019),
    Text(50024),
    Thumb(50029),
    TitleBar(50037),
    ToolBar(50021),
    ToolTip(50022),
    Tree(50023),
    TreeItem(50028),
    Window(50032);

    private final int id;

    ControlType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public static ControlType fromId(int id) {
        for (ControlType type : values()) {
            if (type.id == id) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown ControlType id: " + id);
    }
}
