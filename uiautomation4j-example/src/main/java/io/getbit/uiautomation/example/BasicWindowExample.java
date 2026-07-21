package io.getbit.uiautomation.example;

import io.getbit.uiautomation.condition.SearchCondition;
import io.getbit.uiautomation.control.Control;
import io.getbit.uiautomation.control.WindowControl;
import io.getbit.uiautomation.enums.ControlType;

/**
 * 窗口基本操作示例
 *
 * <p>演示如何查找窗口、获取窗口属性、执行窗口操作（最大化、最小化、关闭等）。</p>
 *
 * <h3>核心概念：</h3>
 * <ul>
 *   <li>通过 {@link Control#window()} Builder 构建搜索条件</li>
 *   <li>通过 {@link WindowControl} 执行窗口操作</li>
 *   <li>通过 {@link SearchCondition} 精确定位目标窗口</li>
 * </ul>
 *
 * <h3>跨平台说明：</h3>
 * <p>同样的代码在 Windows 上通过 UIAutomation COM 接口操作，
 * 在 macOS 上通过 Accessibility API (AXUIElement) 操作，
 * 在 Linux 上通过 AT-SPI2 D-Bus 接口操作。</p>
 */
public class BasicWindowExample {

    /**
     * 运行窗口基本操作示例
     */
    public static void run() {
        System.out.println("========== 窗口基本操作示例 ==========");

        // 示例1：通过窗口类名查找窗口
        findWindowByClassName();

        // 示例2：通过窗口名称查找
        findWindowByName();

        // 示例3：窗口属性获取
        getWindowProperties();

        // 示例4：窗口操作（最大化、最小化、恢复）
        windowOperations();

        // 示例5：控件存在性检查
        checkControlExists();

        // 示例6：智能等待控件就绪
        waitControlReady();

        System.out.println("========== 窗口基本操作示例结束 ==========\n");
    }

    /**
     * 示例1：通过窗口类名查找窗口
     * <p>Windows: 记事本的类名为 "Notepad"
     * macOS: 文本编辑的 AXRole 为 "AXWindow"
     * Linux: 通过 AT-SPI2 Role 查找，如 GTK 窗口</p>
     */
    private static void findWindowByClassName() {
        System.out.println("[示例1] 通过类名查找窗口");

        String os = System.getProperty("os.name").toLowerCase();

        try {
            String className;
            if (os.contains("win")) {
                className = "Notepad";
            } else if (os.contains("mac")) {
                className = "AXWindow";
            } else {
                // Linux: 尝试查找 GNOME 应用窗口
                className = "GtkWindow";
            }

            WindowControl window = Control.window()
                    .className(className)
                    .findWindow();
            System.out.println("  找到窗口: " + window.getName());
        } catch (Exception e) {
            System.out.println("  未找到目标窗口（可能未运行）: " + e.getMessage());
        }
    }

    /**
     * 示例2：通过窗口名称查找
     * <p>根据平台不同查找不同的典型应用窗口</p>
     */
    private static void findWindowByName() {
        System.out.println("[示例2] 通过名称查找窗口");

        String os = System.getProperty("os.name").toLowerCase();

        try {
            String windowName;
            if (os.contains("win")) {
                windowName = "无标题 - 记事本";
            } else if (os.contains("mac")) {
                windowName = "Finder";
            } else {
                // Linux: 尝试查找常见应用
                windowName = "Files";
            }

            WindowControl window = Control.window()
                    .name(windowName)
                    .findWindow();
            System.out.println("  找到窗口: " + window.getName());
            System.out.println("  窗口类名: " + window.getClassName());
        } catch (Exception e) {
            System.out.println("  未找到目标窗口: " + e.getMessage());
        }

        // 尝试模糊匹配
        try {
            WindowControl window = Control.window()
                    .subName("Terminal")
                    .findWindow();
            System.out.println("  模糊匹配找到: " + window.getName());
        } catch (Exception e) {
            System.out.println("  未找到包含 'Terminal' 的窗口");
        }
    }

    /**
     * 示例3：获取窗口属性
     */
    private static void getWindowProperties() {
        System.out.println("[示例3] 获取窗口属性");
        try {
            // 使用 SearchCondition 构建复杂查询
            SearchCondition condition = SearchCondition.builder()
                    .controlType(ControlType.Window)
                    .searchDepth(1)
                    .build();

            WindowControl window = new WindowControl(condition);
            System.out.println("  控件类型: " + window.getControlType());
            System.out.println("  进程ID: " + window.getProcessId());
            System.out.println("  是否可见: " + window.isVisible());
            System.out.println("  是否启用: " + window.isEnabled());
            System.out.println("  窗口名称: " + window.getName());
        } catch (Exception e) {
            System.out.println("  获取属性失败: " + e.getMessage());
        }
    }

    /**
     * 示例4：窗口操作
     * <p>演示最大化、最小化、恢复操作</p>
     */
    private static void windowOperations() {
        System.out.println("[示例4] 窗口操作（最大化/最小化/恢复）");
        try {
            // 查找第一个可见窗口
            WindowControl window = Control.window()
                    .searchDepth(1)
                    .findWindow();

            System.out.println("  目标窗口: " + window.getName());

            // 最大化
            System.out.println("  执行最大化...");
            window.maximize();
            Thread.sleep(1000);

            // 最小化
            System.out.println("  执行最小化...");
            window.minimize();
            Thread.sleep(1000);

            // 恢复
            System.out.println("  恢复窗口...");
            window.getWindowPattern().restore();
            Thread.sleep(1000);

            System.out.println("  窗口操作完成");
        } catch (Exception e) {
            System.out.println("  窗口操作失败（可能未运行）: " + e.getMessage());
        }
    }

    /**
     * 示例5：控件存在性检查
     * <p>演示 {@code exists()} 方法的使用</p>
     */
    private static void checkControlExists() {
        System.out.println("[示例5] 控件存在性检查");

        // 检查窗口是否存在（短超时）
        SearchCondition condition = SearchCondition.builder()
                .controlType(ControlType.Window)
                .searchDepth(1)
                .build();

        WindowControl window = new WindowControl(condition);
        boolean exists = window.exists(2);
        System.out.println("  窗口是否存在(2秒超时): " + exists);

        // 检查不存在的控件
        SearchCondition notExistCondition = SearchCondition.builder()
                .name("这个窗口不存在_12345")
                .controlType(ControlType.Window)
                .build();

        WindowControl notExistWindow = new WindowControl(notExistCondition);
        boolean notExists = notExistWindow.exists(1);
        System.out.println("  不存在的窗口检查(1秒超时): " + notExists);
    }

    /**
     * 示例6：智能等待控件就绪
     * <p>演示 {@code waitReady()} 方法的使用</p>
     */
    private static void waitControlReady() {
        System.out.println("[示例6] 智能等待控件就绪");
        try {
            // 查找窗口并等待就绪
            SearchCondition condition = SearchCondition.builder()
                    .controlType(ControlType.Window)
                    .searchDepth(1)
                    .build();

            WindowControl window = new WindowControl(condition);

            // 等待控件就绪（最多 5 秒）
            window.waitReady(5);
            System.out.println("  窗口已就绪: " + window.getName());
            System.out.println("  可见: " + window.isVisible());
            System.out.println("  启用: " + window.isEnabled());

        } catch (Exception e) {
            System.out.println("  等待超时或失败: " + e.getMessage());
        }
    }
}
