package com.gettyio.uiautomation.example;

import com.gettyio.uiautomation.condition.SearchCondition;
import com.gettyio.uiautomation.control.Control;
import com.gettyio.uiautomation.control.ButtonControl;
import com.gettyio.uiautomation.control.EditControl;
import com.gettyio.uiautomation.control.WindowControl;
import com.gettyio.uiautomation.enums.ControlType;

/**
 * 控件搜索条件示例
 *
 * <p>演示 {@link SearchCondition} 的各种用法，包括：</p>
 * <ul>
 *   <li>精确名称匹配</li>
 *   <li>模糊名称匹配（子串）</li>
 *   <li>正则表达式匹配</li>
 *   <li>控件类型过滤</li>
 *   <li>搜索深度限制</li>
 *   <li>从指定父控件搜索</li>
 *   <li>自定义比较器</li>
 *   <li>链式查找子控件</li>
 * </ul>
 */
public class ControlSearchExample {

    /**
     * 运行控件搜索示例
     */
    public static void run() {
        System.out.println("========== 控件搜索条件示例 ==========\n");

        // 示例1：精确名称匹配
        exactNameMatch();

        // 示例2：模糊名称匹配
        subNameMatch();

        // 示例3：正则表达式匹配
        regexNameMatch();

        // 示例4：控件类型过滤
        controlTypeFilter();

        // 示例5：搜索深度限制
        searchDepthLimit();

        // 示例6：从父控件搜索
        searchFromParent();

        // 示例7：自定义比较器
        customComparator();

        // 示例8：链式查找子控件
        chainedChildSearch();

        System.out.println("========== 控件搜索条件示例结束 ==========\n");
    }

    /**
     * 示例1：精确名称匹配
     * <p>使用 {@code name()} 进行完全匹配，效率最高</p>
     */
    private static void exactNameMatch() {
        System.out.println("[示例1] 精确名称匹配");
        try {
            // 查找名称完全匹配的按钮
            ButtonControl button = new ButtonControl(
                    SearchCondition.builder()
                            .name("确定")
                            .controlType(ControlType.Button)
                            .build()
            );
            System.out.println("  找到按钮: " + button.getName());
        } catch (Exception e) {
            System.out.println("  未找到匹配控件: " + e.getMessage());
        }
    }

    /**
     * 示例2：模糊名称匹配（子串）
     * <p>使用 {@code subName()} 进行子串匹配，在 Java 层二次过滤</p>
     */
    private static void subNameMatch() {
        System.out.println("[示例2] 模糊名称匹配（子串）");
        try {
            // 查找名称中包含"保存"的按钮
            SearchCondition condition = SearchCondition.builder()
                    .subName("保存")
                    .controlType(ControlType.Button)
                    .build();

            Control button = Control.getBackend().findControl(condition);
            System.out.println("  找到按钮: " + button.getName());
        } catch (Exception e) {
            System.out.println("  未找到匹配控件: " + e.getMessage());
        }
    }

    /**
     * 示例3：正则表达式匹配
     * <p>使用 {@code regexName()} 进行正则匹配</p>
     */
    private static void regexNameMatch() {
        System.out.println("[示例3] 正则表达式匹配");
        try {
            // 查找名称匹配正则的窗口（如 "文档1 - Word"、"文档2 - Word" 等）
            SearchCondition condition = SearchCondition.builder()
                    .regexName(".*- Word$")
                    .controlType(ControlType.Window)
                    .build();

            WindowControl window = new WindowControl(condition);
            System.out.println("  找到窗口: " + window.getName());
        } catch (Exception e) {
            System.out.println("  未找到匹配控件: " + e.getMessage());
        }
    }

    /**
     * 示例4：控件类型过滤
     * <p>使用 {@code controlType()} 限定搜索范围</p>
     */
    private static void controlTypeFilter() {
        System.out.println("[示例4] 控件类型过滤");
        try {
            // 查找所有编辑框
            EditControl edit = new EditControl(
                    SearchCondition.builder()
                            .controlType(ControlType.Edit)
                            .searchDepth(5)
                            .build()
            );
            System.out.println("  找到编辑框: " + edit.getName());
        } catch (Exception e) {
            System.out.println("  未找到匹配控件: " + e.getMessage());
        }
    }

    /**
     * 示例5：搜索深度限制
     * <p>使用 {@code searchDepth()} 限制搜索深度，提高效率</p>
     */
    private static void searchDepthLimit() {
        System.out.println("[示例5] 搜索深度限制");
        try {
            // 只在深度为2的范围内搜索
            SearchCondition condition = SearchCondition.builder()
                    .controlType(ControlType.Button)
                    .searchDepth(2)
                    .build();

            Control button = Control.getBackend().findControl(condition);
            System.out.println("  找到按钮: " + button.getName());
        } catch (Exception e) {
            System.out.println("  未找到匹配控件: " + e.getMessage());
        }
    }

    /**
     * 示例6：从指定父控件搜索
     * <p>使用 {@code searchFrom()} 指定搜索起点</p>
     */
    private static void searchFromParent() {
        System.out.println("[示例6] 从父控件搜索");
        try {
            // 先找到父窗口
            SearchCondition parentCondition = SearchCondition.builder()
                    .className("Notepad")
                    .controlType(ControlType.Window)
                    .build();

            // 在记事本窗口内搜索按钮
            SearchCondition childCondition = SearchCondition.builder()
                    .name("确定")
                    .controlType(ControlType.Button)
                    .searchFrom(parentCondition)
                    .build();

            Control button = Control.getBackend().findControl(childCondition);
            System.out.println("  找到按钮: " + button.getName());
        } catch (Exception e) {
            System.out.println("  未找到匹配控件: " + e.getMessage());
        }
    }

    /**
     * 示例7：自定义比较器
     * <p>使用 {@code compare()} 提供自定义匹配逻辑</p>
     */
    private static void customComparator() {
        System.out.println("[示例7] 自定义比较器");
        try {
            // 使用自定义比较器查找进程ID大于0的控件
            SearchCondition condition = SearchCondition.builder()
                    .controlType(ControlType.Window)
                    .compare((element, typeId) -> {
                        // 这里可以添加任意自定义逻辑
                        // element 是底层平台元素引用，typeId 是控件类型ID
                        return true; // 示例：匹配所有
                    })
                    .build();

            Control window = Control.getBackend().findControl(condition);
            System.out.println("  找到控件: " + window.getName());
        } catch (Exception e) {
            System.out.println("  未找到匹配控件: " + e.getMessage());
        }
    }

    /**
     * 示例8：链式查找子控件
     * <p>使用 {@link Control#child(String)} 等方法进行链式调用</p>
     */
    private static void chainedChildSearch() {
        System.out.println("[示例8] 链式查找子控件");
        try {
            // 先找到窗口，再链式查找子控件
            WindowControl window = Control.window()
                    .className("Notepad")
                    .findWindow();

            // 通过名称查找子按钮
            Control okButton = window.child("确定");
            System.out.println("  找到子按钮: " + okButton.getName());

            // 通过名称和类型查找
            Control saveButton = window.child("保存", ControlType.Button);
            System.out.println("  找到保存按钮: " + saveButton.getName());
        } catch (Exception e) {
            System.out.println("  未找到匹配控件: " + e.getMessage());
        }
    }
}
