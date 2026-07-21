package com.gettyio.uiautomation.linux.ax;

/**
 * AT-SPI2 常量定义
 *
 * <p>包含 AT-SPI2 无障碍框架中使用的各种常量值，如 Role、State 等。</p>
 *
 * @see <a href="https://gitlab.gnome.org/GNOME/at-spi2-core">AT-SPI2 Core</a>
 */
public class AtspiConstants {

    // ==================== AT-SPI2 Role 值 ====================

    public static final int ROLE_INVALID = 0;
    public static final int ROLE_ACCELERATOR_LABEL = 1;
    public static final int ROLE_ALERT = 2;
    public static final int ROLE_ANIMATION = 3;
    public static final int ROLE_ARROW = 4;
    public static final int ROLE_CALENDAR = 5;
    public static final int ROLE_CANVAS = 6;
    public static final int ROLE_CHECK_BOX = 11;
    public static final int ROLE_CHECK_MENU_ITEM = 12;
    public static final int ROLE_COLOR_CHOOSER = 13;
    public static final int ROLE_COLUMN_HEADER = 14;
    public static final int ROLE_COMBO_BOX = 15;
    public static final int ROLE_DATE_EDITOR = 16;
    public static final int ROLE_DESKTOP_ICON = 17;
    public static final int ROLE_DESKTOP_FRAME = 18;
    public static final int ROLE_DIAL = 19;
    public static final int ROLE_DIALOG = 20;
    public static final int ROLE_DIRECTORY_PANE = 21;
    public static final int ROLE_DRAWING_AREA = 22;
    public static final int ROLE_FILE_CHOOSER = 23;
    public static final int ROLE_FILLER = 24;
    public static final int ROLE_FONT_CHOOSER = 26;
    public static final int ROLE_FRAME = 27;
    public static final int ROLE_GLASS_PANE = 28;
    public static final int ROLE_HTML_CONTAINER = 29;
    public static final int ROLE_ICON = 30;
    public static final int ROLE_IMAGE = 31;
    public static final int ROLE_INTERNAL_FRAME = 32;
    public static final int ROLE_LABEL = 33;
    public static final int ROLE_LAYERED_PANE = 34;
    public static final int ROLE_LIST = 35;
    public static final int ROLE_LIST_ITEM = 36;
    public static final int ROLE_MENU = 37;
    public static final int ROLE_MENU_BAR = 38;
    public static final int ROLE_MENU_ITEM = 39;
    public static final int ROLE_OPTION_PANE = 41;
    public static final int ROLE_PAGE_TAB = 42;
    public static final int ROLE_PAGE_TAB_LIST = 43;
    public static final int ROLE_PANEL = 44;
    public static final int ROLE_PASSWORD_TEXT = 45;
    public static final int ROLE_POPUP_MENU = 46;
    public static final int ROLE_PROGRESS_BAR = 47;
    public static final int ROLE_PUSH_BUTTON = 74;
    public static final int ROLE_RADIO_BUTTON = 49;
    public static final int ROLE_RADIO_MENU_ITEM = 50;
    public static final int ROLE_ROOT_PANE = 51;
    public static final int ROLE_ROW_HEADER = 52;
    public static final int ROLE_SCROLL_BAR = 53;
    public static final int ROLE_SCROLL_PANE = 54;
    public static final int ROLE_SEPARATOR = 55;
    public static final int ROLE_SLIDER = 56;
    public static final int ROLE_SPIN_BUTTON = 57;
    public static final int ROLE_SPLIT_PANE = 58;
    public static final int ROLE_STATUS_BAR = 59;
    public static final int ROLE_TABLE = 60;
    public static final int ROLE_TABLE_CELL = 61;
    public static final int ROLE_TABLE_COLUMN_HEADER = 62;
    public static final int ROLE_TABLE_ROW_HEADER = 63;
    public static final int ROLE_TERMINAL = 65;
    public static final int ROLE_TEXT = 66;
    public static final int ROLE_TOGGLE_BUTTON = 67;
    public static final int ROLE_TOOL_BAR = 68;
    public static final int ROLE_TOOL_TIP = 69;
    public static final int ROLE_TREE = 70;
    public static final int ROLE_TREE_TABLE = 71;
    public static final int ROLE_UNKNOWN = 72;
    public static final int ROLE_WINDOW = 74;
    public static final int ROLE_LINK = 79;

    // ==================== AT-SPI2 State 值 ====================

    public static final int STATE_ACTIVE = 1;
    public static final int STATE_ARMED = 2;
    public static final int STATE_BUSY = 3;
    public static final int STATE_CHECKED = 4;
    public static final int STATE_DEFUNCT = 5;
    public static final int STATE_EDITABLE = 6;
    public static final int STATE_ENABLED = 7;
    public static final int STATE_EXPANDABLE = 8;
    public static final int STATE_EXPANDED = 9;
    public static final int STATE_FOCUSABLE = 10;
    public static final int STATE_FOCUSED = 11;
    public static final int STATE_HAS_TOOLTIP = 12;
    public static final int STATE_HORIZONTAL = 13;
    public static final int STATE_ICONIFIED = 14;
    public static final int STATE_MODAL = 15;
    public static final int STATE_MULTI_LINE = 16;
    public static final int STATE_MULTISELECTABLE = 17;
    public static final int STATE_OPAQUE = 18;
    public static final int STATE_PRESSED = 19;
    public static final int STATE_RESIZABLE = 20;
    public static final int STATE_SELECTABLE = 21;
    public static final int STATE_SELECTED = 22;
    public static final int STATE_SENSITIVE = 23;
    public static final int STATE_SHOWING = 24;
    public static final int STATE_SINGLE_LINE = 25;
    public static final int STATE_STALE = 26;
    public static final int STATE_TRANSIENT = 27;
    public static final int STATE_VERTICAL = 28;
    public static final int STATE_VISIBLE = 29;
    public static final int STATE_VERTICALLY_SCROLLABLE = 30;
    public static final int STATE_HORIZONTALLY_SCROLLABLE = 31;
    /** 最大化状态（非标准，某些工具集使用） */
    public static final int STATE_MAXIMIZED = 32;

    private AtspiConstants() {
        // 工具类禁止实例化
    }
}
