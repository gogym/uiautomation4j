package com.gettyio.uiautomation.example;

import com.gettyio.uiautomation.linux.LinuxAutomation;
import com.gettyio.uiautomation.mac.MacAutomation;
import com.gettyio.uiautomation.win.WinAutomation;

/**
 * 所有示例的统一入口
 *
 * <p>运行此类可执行所有示例，演示 uiautomation4j 的核心功能。</p>
 *
 * <h3>运行方式：</h3>
 * <pre>
 * // 直接运行 main 方法
 * java -cp uiautomation4j-example.jar com.gettyio.uiautomation.example.AllExamplesRunner
 * </pre>
 *
 * <h3>示例列表：</h3>
 * <ul>
 *   <li>{@link BasicWindowExample} - 窗口基本操作</li>
 *   <li>{@link ControlSearchExample} - 控件搜索条件</li>
 *   <li>{@link PatternExample} - Pattern 使用</li>
 *   <li>{@link CrossPlatformExample} - 跨平台使用</li>
 *   <li>{@link LinuxSpecificExample} - Linux 平台专属功能（仅在 Linux 上运行）</li>
 * </ul>
 *
 * <h3>前置条件：</h3>
 * <ul>
 *   <li>Windows: 无特殊要求</li>
 *   <li>macOS: 需要授予"辅助功能"权限（系统设置 → 隐私与安全 → 辅助功能）</li>
 *   <li>Linux: 需要安装 at-spi2-core（大多数桌面发行版已预装），并运行图形桌面环境</li>
 * </ul>
 */
public class AllExamplesRunner {

    /**
     * 主入口
     *
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║           uiautomation4j 使用示例和测试用例               ║");
        System.out.println("║       跨平台 UI 自动化框架 (Windows + macOS + Linux)      ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝\n");

        // 打印环境信息
        printEnvironmentInfo();

        // 初始化平台后端
        initPlatform();

        try {
            // 运行所有示例
            System.out.println("\n");
            BasicWindowExample.run();

            System.out.println("\n");
            ControlSearchExample.run();

            System.out.println("\n");
            PatternExample.run();

            System.out.println("\n");
            CrossPlatformExample.run();

            // Linux 平台专属示例
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("linux") || os.contains("nux") || os.contains("nix")) {
                System.out.println("\n");
                LinuxSpecificExample.run();
            }

        } finally {
            // 释放资源
            shutdownPlatform();
        }

        System.out.println("\n╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║                    所有示例执行完成                       ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
    }

    /**
     * 打印环境信息
     */
    private static void printEnvironmentInfo() {
        System.out.println("【环境信息】");
        System.out.println("  操作系统: " + System.getProperty("os.name"));
        System.out.println("  系统版本: " + System.getProperty("os.version"));
        System.out.println("  系统架构: " + System.getProperty("os.arch"));
        System.out.println("  Java版本: " + System.getProperty("java.version"));
        System.out.println("  工作目录: " + System.getProperty("user.dir"));
        System.out.println();
    }

    /**
     * 初始化平台后端
     * <p>根据操作系统自动选择 Windows、macOS 或 Linux 后端</p>
     */
    private static void initPlatform() {
        String os = System.getProperty("os.name").toLowerCase();
        System.out.println("【初始化平台后端】");

        if (os.contains("win")) {
            System.out.println("  检测到 Windows 平台，初始化 JNA + COM 后端...");
            WinAutomation.init();
            System.out.println("  ✓ Windows 后端初始化成功");
        } else if (os.contains("mac")) {
            System.out.println("  检测到 macOS 平台，初始化 JNA + Accessibility API 后端...");
            MacAutomation.init();
            System.out.println("  ✓ macOS 后端初始化成功");

            // 检查辅助功能权限
            boolean trusted = MacAutomation.isTrusted();
            if (trusted) {
                System.out.println("  ✓ 辅助功能权限已授权");
            } else {
                System.out.println("  ✗ 辅助功能权限未授权！");
                System.out.println("    请在 系统设置 → 隐私与安全 → 辅助功能 中添加并授权此应用");
            }
        } else if (os.contains("linux") || os.contains("nux") || os.contains("nix")) {
            System.out.println("  检测到 Linux 平台，初始化 AT-SPI2 + D-Bus 后端...");

            // 检查 AT-SPI2 是否可用
            boolean available = LinuxAutomation.isAvailable();
            if (available) {
                LinuxAutomation.init();
                System.out.println("  ✓ Linux 后端初始化成功（AT-SPI2 可用）");
            } else {
                System.out.println("  ✗ AT-SPI2 不可用！");
                System.out.println("    请确保已安装 at-spi2-core 并运行图形桌面环境");
                System.exit(1);
            }
        } else {
            System.out.println("  ✗ 不支持的操作系统: " + os);
            System.out.println("    仅支持 Windows、macOS 和 Linux");
            System.exit(1);
        }
    }

    /**
     * 释放平台资源
     */
    private static void shutdownPlatform() {
        String os = System.getProperty("os.name").toLowerCase();
        System.out.println("\n【释放资源】");

        if (os.contains("win")) {
            WinAutomation.shutdown();
            System.out.println("  ✓ Windows 后端已释放");
        } else if (os.contains("mac")) {
            MacAutomation.shutdown();
            System.out.println("  ✓ macOS 后端已释放");
        } else if (os.contains("linux") || os.contains("nux") || os.contains("nix")) {
            LinuxAutomation.shutdown();
            System.out.println("  ✓ Linux 后端已释放");
        }
    }
}
