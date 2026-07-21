package io.getbit.uiautomation.example;

import io.getbit.uiautomation.condition.SearchCondition;
import io.getbit.uiautomation.control.Control;
import io.getbit.uiautomation.control.EditControl;
import io.getbit.uiautomation.control.WindowControl;
import io.getbit.uiautomation.enums.ControlType;
import io.getbit.uiautomation.linux.LinuxAutomation;
import io.getbit.uiautomation.pattern.ValuePattern;
import io.getbit.uiautomation.pattern.WindowPattern;

/**
 * Linux 平台专属功能示例
 *
 * <p>演示 Linux 平台上 AT-SPI2 + D-Bus 后端的特有功能和使用方式。</p>
 *
 * <h3>技术架构：</h3>
 * <pre>
 * ┌─────────────────────────────────────┐
 * │       uiautomation4j-core           │
 * │    (Control, SearchCondition)       │
 * └──────────────┬──────────────────────┘
 *                │ SPI (ControlBackend)
 *                ▼
 * ┌─────────────────────────────────────┐
 * │       uiautomation4j-linux          │
 * │  LinuxControlBackend                │
 * │    ├── AtspiConnection (D-Bus)      │
 * │    ├── AtspiElement (元素封装)       │
 * │    ├── LinuxControlFactory          │
 * │    ├── Keyboard/Mouse (XTest)       │
 * │    └── Screenshot (AWT Robot)       │
 * └─────────────────────────────────────┘
 * </pre>
 *
 * <h3>前置条件：</h3>
 * <ul>
 *   <li>安装 at-spi2-core：{@code sudo apt install at-spi2-core}（Ubuntu/Debian）</li>
 *   <li>运行图形桌面环境（GNOME、KDE、XFCE 等）</li>
 *   <li>某些 GTK3 应用需要设置环境变量：{@code GTK_MODULES=gail:atk-bridge}</li>
 * </ul>
 *
 * <h3>AT-SPI2 基本概念：</h3>
 * <ul>
 *   <li>AT-SPI2 (Assistive Technology Service Provider Interface) 是 Linux 标准无障碍框架</li>
 *   <li>通过 D-Bus IPC 暴露 UI 元素树，接口包括 Accessible/Action/Component/Text/Value</li>
 *   <li>每个 UI 元素有 Role（角色）、Name（名称）、State（状态集）等属性</li>
 *   <li>Role 值映射：ROLE_PUSH_BUTTON=74, ROLE_FRAME=27, ROLE_TEXT=66 等</li>
 * </ul>
 */
public class LinuxSpecificExample {

    /**
     * 运行 Linux 平台专属示例
     */
    public static void run() {
        System.out.println("========== Linux 平台专属示例 ==========");

        // 示例1：检查 AT-SPI2 可用性
        checkAtspiAvailability();

        // 示例2：Linux 窗口查找与操作
        linuxWindowOperations();

        // 示例3：Linux AT-SPI2 Role 映射
        atspiRoleMapping();

        // 示例4：Linux 控件搜索（GTK 应用）
        gtkAppControlSearch();

        // 示例5：Linux 文本值读取
        linuxValuePatternUsage();

        // 示例6：Linux 截图功能
        linuxScreenshot();

        System.out.println("========== Linux 平台专属示例结束 ==========\n");
    }

    /**
     * 示例1：检查 AT-SPI2 可用性
     * <p>验证 D-Bus 会话总线连接和 AT-SPI2 服务状态</p>
     */
    private static void checkAtspiAvailability() {
        System.out.println("[示例1] 检查 AT-SPI2 可用性");

        // 检查是否已初始化
        boolean initialized = LinuxAutomation.isInitialized();
        System.out.println("  Linux 后端已初始化: " + initialized);

        // 检查 AT-SPI2 是否可用
        boolean available = LinuxAutomation.isAvailable();
        System.out.println("  AT-SPI2 可用: " + available);

        if (!available) {
            System.out.println("  提示: 请确保已安装 at-spi2-core 并运行图形桌面");
            System.out.println("  Ubuntu/Debian: sudo apt install at-spi2-core");
            System.out.println("  Fedora: sudo dnf install at-spi2-core");
            return;
        }

        // 显示 D-Bus 环境信息
        String dbusSession = System.getenv("DBUS_SESSION_BUS_ADDRESS");
        System.out.println("  D-Bus 会话地址: " + (dbusSession != null ? dbusSession : "未设置（使用默认）"));

        String display = System.getenv("DISPLAY");
        System.out.println("  X Display: " + (display != null ? display : "未设置"));

        String waylandDisplay = System.getenv("WAYLAND_DISPLAY");
        System.out.println("  Wayland Display: " + (waylandDisplay != null ? waylandDisplay : "未使用"));
    }

    /**
     * 示例2：Linux 窗口查找与操作
     * <p>演示在 Linux 上查找和操作常见桌面应用窗口</p>
     */
    private static void linuxWindowOperations() {
        System.out.println("[示例2] Linux 窗口查找与操作");

        // 查找桌面第一个可见窗口
        try {
            SearchCondition condition = SearchCondition.builder()
                    .controlType(ControlType.Window)
                    .searchDepth(1)
                    .build();

            WindowControl window = new WindowControl(condition);
            System.out.println("  找到窗口: " + window.getName());
            System.out.println("  控件类型: " + window.getControlType());
            System.out.println("  是否可见: " + window.isVisible());
            System.out.println("  是否启用: " + window.isEnabled());

            // 获取窗口 Pattern
            WindowPattern windowPattern = window.getWindowPattern();
            int visualState = windowPattern.getVisualState();
            String stateName;
            switch (visualState) {
                case 0: stateName = "正常"; break;
                case 1: stateName = "最大化"; break;
                case 2: stateName = "最小化"; break;
                default: stateName = "未知"; break;
            }
            System.out.println("  窗口状态: " + stateName);

        } catch (Exception e) {
            System.out.println("  窗口查找失败: " + e.getMessage());
        }

        // 尝试查找常见 Linux 应用
        System.out.println("  尝试查找常见应用:");
        String[] commonApps = {"Firefox", "Chrome", "Files", "Terminal", "gedit", "Text Editor"};
        for (String appName : commonApps) {
            try {
                WindowControl window = Control.window()
                        .subName(appName)
                        .findWindow();
                System.out.println("    ✓ 找到: " + window.getName());
            } catch (Exception e) {
                System.out.println("    ✗ 未找到: " + appName);
            }
        }
    }

    /**
     * 示例3：Linux AT-SPI2 Role 映射
     * <p>展示 AT-SPI2 Role 整数值如何映射到统一的 ControlType</p>
     */
    private static void atspiRoleMapping() {
        System.out.println("[示例3] AT-SPI2 Role 映射");

        System.out.println("  常用 Role 映射:");
        System.out.println("    ROLE_INVALID(0)          → " + ControlType.fromAtspiRole(0));
        System.out.println("    ROLE_ALERT(2)            → " + ControlType.fromAtspiRole(2));
        System.out.println("    ROLE_DIALOG(20)          → " + ControlType.fromAtspiRole(20));
        System.out.println("    ROLE_FRAME(27)           → " + ControlType.fromAtspiRole(27));
        System.out.println("    ROLE_LABEL(33)           → " + ControlType.fromAtspiRole(33));
        System.out.println("    ROLE_LIST(35)            → " + ControlType.fromAtspiRole(35));
        System.out.println("    ROLE_LIST_ITEM(36)       → " + ControlType.fromAtspiRole(36));
        System.out.println("    ROLE_MENU(37)            → " + ControlType.fromAtspiRole(37));
        System.out.println("    ROLE_MENU_BAR(38)        → " + ControlType.fromAtspiRole(38));
        System.out.println("    ROLE_MENU_ITEM(39)       → " + ControlType.fromAtspiRole(39));
        System.out.println("    ROLE_PANEL(44)           → " + ControlType.fromAtspiRole(44));
        System.out.println("    ROLE_PROGRESS_BAR(47)    → " + ControlType.fromAtspiRole(47));
        System.out.println("    ROLE_PUSH_BUTTON(74)     → " + ControlType.fromAtspiRole(74));
        System.out.println("    ROLE_CHECK_BOX(11)       → " + ControlType.fromAtspiRole(11));
        System.out.println("    ROLE_RADIO_BUTTON(49)    → " + ControlType.fromAtspiRole(49));
        System.out.println("    ROLE_COMBO_BOX(15)       → " + ControlType.fromAtspiRole(15));
        System.out.println("    ROLE_TEXT(66)            → " + ControlType.fromAtspiRole(66));
        System.out.println("    ROLE_TREE(70)            → " + ControlType.fromAtspiRole(70));
        System.out.println("    ROLE_TABLE(60)           → " + ControlType.fromAtspiRole(60));
        System.out.println("    ROLE_SCROLL_BAR(53)      → " + ControlType.fromAtspiRole(53));
        System.out.println("    ROLE_SLIDER(56)          → " + ControlType.fromAtspiRole(56));
        System.out.println("    ROLE_LINK(79)            → " + ControlType.fromAtspiRole(79));
        System.out.println("    ROLE_IMAGE(31)           → " + ControlType.fromAtspiRole(31));
        System.out.println("    ROLE_TOOL_BAR(68)        → " + ControlType.fromAtspiRole(68));

        System.out.println("  Linux 专用类型:");
        System.out.println("    Alert:           " + ControlType.Alert + " (ID=" + ControlType.Alert.getId() + ")");
        System.out.println("    Dialog:          " + ControlType.Dialog + " (ID=" + ControlType.Dialog.getId() + ")");
        System.out.println("    PageTab:         " + ControlType.PageTab + " (ID=" + ControlType.PageTab.getId() + ")");
        System.out.println("    PopupMenu:       " + ControlType.PopupMenu + " (ID=" + ControlType.PopupMenu.getId() + ")");
        System.out.println("    Table:           " + ControlType.Table + " (ID=" + ControlType.Table.getId() + ")");
        System.out.println("    TableCell:       " + ControlType.TableCell + " (ID=" + ControlType.TableCell.getId() + ")");
    }

    /**
     * 示例4：Linux 控件搜索（GTK 应用）
     * <p>演示在 Linux 上搜索 GTK 应用的控件</p>
     */
    private static void gtkAppControlSearch() {
        System.out.println("[示例4] Linux 控件搜索");

        try {
            // 查找所有顶层窗口
            System.out.println("  搜索顶层窗口...");
            SearchCondition windowCondition = SearchCondition.builder()
                    .controlType(ControlType.Window)
                    .searchDepth(1)
                    .build();

            Control window = Control.getBackend().findControl(windowCondition);
            System.out.println("    窗口: " + window.getName());

            // 在窗口内搜索按钮
            try {
                SearchCondition buttonCondition = SearchCondition.builder()
                        .controlType(ControlType.Button)
                        .searchFrom(windowCondition)
                        .searchDepth(5)
                        .build();

                Control button = Control.getBackend().findControl(buttonCondition);
                System.out.println("    按钮: " + button.getName());
            } catch (Exception e) {
                System.out.println("    未找到按钮: " + e.getMessage());
            }

            // 在窗口内搜索文本标签
            try {
                SearchCondition textCondition = SearchCondition.builder()
                        .controlType(ControlType.Text)
                        .searchFrom(windowCondition)
                        .searchDepth(5)
                        .build();

                Control text = Control.getBackend().findControl(textCondition);
                System.out.println("    文本: " + text.getName());
            } catch (Exception e) {
                System.out.println("    未找到文本控件: " + e.getMessage());
            }

        } catch (Exception e) {
            System.out.println("  控件搜索失败: " + e.getMessage());
        }
    }

    /**
     * 示例5：Linux 文本值读取
     * <p>演示通过 ValuePattern 读取 Linux 控件的文本内容</p>
     */
    private static void linuxValuePatternUsage() {
        System.out.println("[示例5] Linux 文本值读取");

        try {
            // 查找编辑框（如 gedit 的文本区域）
            EditControl edit = new EditControl(
                    SearchCondition.builder()
                            .controlType(ControlType.Edit)
                            .build()
            );

            ValuePattern valuePattern = edit.getValuePattern();
            String value = valuePattern.getValue();
            System.out.println("  编辑框值: " + (value != null && !value.isEmpty() ? value : "(空)"));
            System.out.println("  是否只读: " + valuePattern.isReadOnly());

        } catch (Exception e) {
            System.out.println("  未找到编辑框: " + e.getMessage());
        }

        // 尝试读取文本标签的值
        try {
            SearchCondition textCondition = SearchCondition.builder()
                    .controlType(ControlType.Text)
                    .build();

            Control textControl = Control.getBackend().findControl(textCondition);
            ValuePattern valuePattern = textControl.getValuePattern();
            String value = valuePattern.getValue();
            System.out.println("  文本控件值: " + (value != null && !value.isEmpty() ? value : "(空)"));

        } catch (Exception e) {
            System.out.println("  未找到文本控件: " + e.getMessage());
        }
    }

    /**
     * 示例6：Linux 截图功能
     * <p>演示在 Linux 上使用 AWT Robot 进行截图</p>
     */
    private static void linuxScreenshot() {
        System.out.println("[示例6] Linux 截图功能");

        try {
            // 查找窗口并截图
            WindowControl window = Control.window()
                    .searchDepth(1)
                    .findWindow();

            String screenshotPath = "/tmp/uiautomation4j_linux_screenshot.png";
            window.captureToImage(screenshotPath);
            System.out.println("  窗口截图已保存: " + screenshotPath);

        } catch (Exception e) {
            System.out.println("  截图失败: " + e.getMessage());
            System.out.println("  提示: 确保 X11/Wayland 环境支持 AWT Robot");
        }
    }
}
