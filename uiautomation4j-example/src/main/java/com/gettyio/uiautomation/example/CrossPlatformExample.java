package com.gettyio.uiautomation.example;

import com.gettyio.uiautomation.condition.SearchCondition;
import com.gettyio.uiautomation.control.Control;
import com.gettyio.uiautomation.control.WindowControl;
import com.gettyio.uiautomation.enums.ControlType;
import com.gettyio.uiautomation.linux.LinuxAutomation;
import com.gettyio.uiautomation.mac.MacAutomation;
import com.gettyio.uiautomation.win.WinAutomation;

/**
 * 跨平台使用示例
 *
 * <p>演示如何在不同平台上初始化后端并执行相同的 UI 自动化代码。
 * 框架通过 SPI 机制自动适配平台，用户代码无需修改。</p>
 *
 * <h3>跨平台架构：</h3>
 * <pre>
 * ┌─────────────────────────────────────────┐
 * │         uiautomation4j-core             │
 * │    (Control, SearchCondition, Pattern)  │
 * └────────────────┬────────────────────────┘
 *                  │ SPI (ControlBackend)
 *         ┌────────┼────────┐
 *         ▼        ▼        ▼
 * ┌──────────┐ ┌──────────┐ ┌──────────────┐
 * │ win 模块 │ │ mac 模块 │ │ linux 模块   │
 * │ JNA+COM  │ │ JNA+AX   │ │ AT-SPI2+D-Bus│
 * │(Windows) │ │(macOS)   │ │(Linux)       │
 * └──────────┘ └──────────┘ └──────────────┘
 * </pre>
 *
 * <h3>使用方式：</h3>
 * <p>只需在应用启动时调用对应平台的 init() 方法：</p>
 * <ul>
 *   <li>Windows: {@code WinAutomation.init()}</li>
 *   <li>macOS: {@code MacAutomation.init()}</li>
 *   <li>Linux: {@code LinuxAutomation.init()}</li>
 * </ul>
 * <p>之后所有 UI 操作代码完全一致。</p>
 */
public class CrossPlatformExample {

    /**
     * 运行跨平台示例
     */
    public static void run() {
        System.out.println("========== 跨平台使用示例 ==========");

        // 示例1：平台检测与初始化
        detectAndInitPlatform();

        // 示例2：跨平台统一的搜索代码
        crossPlatformSearch();

        // 示例3：平台特有的 ControlType
        platformSpecificControlTypes();

        // 示例4：平台 Role 映射演示
        platformRoleMapping();

        System.out.println("========== 跨平台使用示例结束 ==========\n");
    }

    /**
     * 示例1：平台检测与初始化
     * <p>根据操作系统自动选择对应的后端实现</p>
     */
    private static void detectAndInitPlatform() {
        System.out.println("[示例1] 平台检测与初始化");

        String os = System.getProperty("os.name").toLowerCase();
        System.out.println("  当前操作系统: " + System.getProperty("os.name"));

        if (os.contains("win")) {
            // Windows 平台
            System.out.println("  初始化 Windows 后端 (JNA + COM)...");
            WinAutomation.init();
            System.out.println("  Windows 后端初始化成功");
        } else if (os.contains("mac")) {
            // macOS 平台
            System.out.println("  初始化 macOS 后端 (JNA + Accessibility API)...");
            MacAutomation.init();
            System.out.println("  macOS 后端初始化成功");

            // 检查辅助功能权限
            boolean trusted = MacAutomation.isTrusted();
            System.out.println("  辅助功能权限: " + (trusted ? "已授权" : "未授权"));
        } else if (os.contains("linux") || os.contains("nux") || os.contains("nix")) {
            // Linux 平台
            System.out.println("  初始化 Linux 后端 (AT-SPI2 + D-Bus)...");
            boolean available = LinuxAutomation.isAvailable();
            System.out.println("  AT-SPI2 可用性: " + (available ? "可用" : "不可用"));
            if (available) {
                LinuxAutomation.init();
                System.out.println("  Linux 后端初始化成功");
            }
        } else {
            System.out.println("  不支持的操作系统: " + os);
            return;
        }

        System.out.println("  后端已注册: " + (Control.getBackend() != null));
    }

    /**
     * 示例2：跨平台统一的搜索代码
     * <p>以下代码在 Windows、macOS 和 Linux 上完全一致，无需修改</p>
     */
    private static void crossPlatformSearch() {
        System.out.println("[示例2] 跨平台统一的搜索代码");

        // 以下代码在所有平台上行为一致
        // Windows: 通过 IUIAutomation 查找
        // macOS: 通过 AXUIElement 查找
        // Linux: 通过 AT-SPI2 D-Bus 查找

        // 方式1：使用 Builder 模式
        System.out.println("  方式1: Builder 模式搜索窗口");
        try {
            // 尝试查找当前活跃窗口（跨平台通用）
            SearchCondition condition = SearchCondition.builder()
                    .controlType(ControlType.Window)
                    .searchDepth(1)
                    .build();

            WindowControl window = new WindowControl(condition);
            System.out.println("    找到窗口: " + window.getName());
            System.out.println("    控件类型: " + window.getControlType());
        } catch (Exception e) {
            System.out.println("    搜索失败: " + e.getMessage());
        }

        // 方式2：使用平台特定名称搜索
        System.out.println("  方式2: 平台特定名称搜索");
        String os = System.getProperty("os.name").toLowerCase();
        try {
            String windowName;
            if (os.contains("win")) {
                windowName = "无标题 - 记事本";
            } else if (os.contains("mac")) {
                windowName = "Finder";
            } else {
                windowName = "Files";  // GNOME Files
            }

            WindowControl window = Control.window()
                    .name(windowName)
                    .findWindow();
            System.out.println("    找到窗口: " + window.getName());
        } catch (Exception e) {
            System.out.println("    未找到目标窗口（可能未运行）");
        }
    }

    /**
     * 示例3：平台特有的 ControlType
     * <p>展示各平台专属控件类型和跨平台通用类型</p>
     */
    private static void platformSpecificControlTypes() {
        System.out.println("[示例3] 平台特有的 ControlType");

        // 跨平台通用类型
        System.out.println("  跨平台通用类型 (CROSS_PLATFORM):");
        System.out.println("    Window:  " + ControlType.Window.getPlatform());
        System.out.println("    Button:  " + ControlType.Button.getPlatform());
        System.out.println("    Edit:    " + ControlType.Edit.getPlatform());
        System.out.println("    Text:    " + ControlType.Text.getPlatform());
        System.out.println("    List:    " + ControlType.List.getPlatform());
        System.out.println("    Tree:    " + ControlType.Tree.getPlatform());

        // Windows 特有类型
        System.out.println("  Windows 特有类型 (WINDOWS):");
        System.out.println("    Spinner:  " + ControlType.Spinner.getPlatform());
        System.out.println("    AppBar:   " + ControlType.AppBar.getPlatform());
        System.out.println("    TitleBar: " + ControlType.TitleBar.getPlatform());

        // macOS 特有类型
        System.out.println("  macOS 特有类型 (MAC):");
        System.out.println("    ColorWell:    " + ControlType.ColorWell.getPlatform());
        System.out.println("    PopUpButton:  " + ControlType.PopUpButton.getPlatform());
        System.out.println("    TextArea:     " + ControlType.TextArea.getPlatform());
        System.out.println("    TextField:    " + ControlType.TextField.getPlatform());

        // Linux 特有类型
        System.out.println("  Linux 特有类型 (LINUX):");
        System.out.println("    Alert:           " + ControlType.Alert.getPlatform());
        System.out.println("    Dialog:          " + ControlType.Dialog.getPlatform());
        System.out.println("    PageTab:         " + ControlType.PageTab.getPlatform());
        System.out.println("    PopupMenu:       " + ControlType.PopupMenu.getPlatform());
        System.out.println("    Table:           " + ControlType.Table.getPlatform());
        System.out.println("    TableCell:       " + ControlType.TableCell.getPlatform());
        System.out.println("    TableColumnHeader: " + ControlType.TableColumnHeader.getPlatform());

        // 检查可用性
        System.out.println("  可用性检查:");
        System.out.println("    Button 在 Windows 可用: " + ControlType.Button.isAvailableOn(ControlType.Platform.WINDOWS));
        System.out.println("    Button 在 Linux 可用:   " + ControlType.Button.isAvailableOn(ControlType.Platform.LINUX));
        System.out.println("    ColorWell 在 Linux 可用: " + ControlType.ColorWell.isAvailableOn(ControlType.Platform.LINUX));
        System.out.println("    Alert 在 macOS 可用:    " + ControlType.Alert.isAvailableOn(ControlType.Platform.MAC));
    }

    /**
     * 示例4：平台 Role 映射演示
     * <p>展示各平台原生角色如何映射到统一的 ControlType</p>
     */
    private static void platformRoleMapping() {
        System.out.println("[示例4] 平台 Role 映射演示");

        // macOS AXRole → ControlType
        System.out.println("  macOS AXRole → ControlType:");
        System.out.println("    AXButton    → " + ControlType.fromMacRole("AXButton"));
        System.out.println("    AXWindow    → " + ControlType.fromMacRole("AXWindow"));
        System.out.println("    AXTextField → " + ControlType.fromMacRole("AXTextField"));
        System.out.println("    AXList      → " + ControlType.fromMacRole("AXList"));
        System.out.println("    AXOutline   → " + ControlType.fromMacRole("AXOutline"));
        System.out.println("    AXUnknown   → " + ControlType.fromMacRole("AXUnknown"));

        // Linux AT-SPI Role → ControlType
        System.out.println("  Linux AT-SPI Role → ControlType:");
        System.out.println("    ROLE_PUSH_BUTTON(74) → " + ControlType.fromAtspiRole(74));
        System.out.println("    ROLE_FRAME(27)       → " + ControlType.fromAtspiRole(27));
        System.out.println("    ROLE_TEXT(66)        → " + ControlType.fromAtspiRole(66));
        System.out.println("    ROLE_CHECK_BOX(11)   → " + ControlType.fromAtspiRole(11));
        System.out.println("    ROLE_LIST(35)        → " + ControlType.fromAtspiRole(35));
        System.out.println("    ROLE_TREE(70)        → " + ControlType.fromAtspiRole(70));
        System.out.println("    ROLE_DIALOG(20)      → " + ControlType.fromAtspiRole(20));
        System.out.println("    ROLE_TABLE(60)       → " + ControlType.fromAtspiRole(60));
        System.out.println("    ROLE_ALERT(2)        → " + ControlType.fromAtspiRole(2));
        System.out.println("    ROLE_UNKNOWN(0)      → " + ControlType.fromAtspiRole(0));
    }
}
