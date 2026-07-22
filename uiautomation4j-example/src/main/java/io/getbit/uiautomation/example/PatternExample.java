package io.getbit.uiautomation.example;

import io.getbit.uiautomation.condition.SearchCondition;
import io.getbit.uiautomation.control.Control;
import io.getbit.uiautomation.control.EditControl;
import io.getbit.uiautomation.control.WindowControl;
import io.getbit.uiautomation.enums.ControlType;
import io.getbit.uiautomation.pattern.*;

/**
 * Pattern 使用示例
 *
 * <p>演示 UIAutomation 各种 Pattern 的使用方法：</p>
 * <ul>
 *   <li>{@link ValuePattern} - 读写控件值</li>
 *   <li>{@link InvokePattern} - 触发控件操作</li>
 *   <li>{@link WindowPattern} - 窗口操作</li>
 *   <li>{@link SelectionPattern} - 选择项获取</li>
 *   <li>{@link ScrollPattern} - 滚动操作</li>
 *   <li>{@link ExpandCollapsePattern} - 展开/折叠</li>
 *   <li>{@link TransformPattern} - 移动/调整大小/旋转</li>
 * </ul>
 *
 * <h3>Pattern 概念：</h3>
 * <p>Pattern 是 UIAutomation 的核心概念，代表控件支持的一组交互能力。
 * 不同控件类型支持不同的 Pattern 组合。例如：</p>
 * <ul>
 *   <li>Button → InvokePattern</li>
 *   <li>Edit → ValuePattern</li>
 *   <li>Window → WindowPattern + TransformPattern</li>
 *   <li>CheckBox → 同时支持 ValuePattern 和 InvokePattern</li>
 *   <li>List → SelectionPattern + ScrollPattern</li>
 *   <li>TreeItem → ExpandCollapsePattern</li>
 * </ul>
 */
public class PatternExample {

    /**
     * 运行 Pattern 示例
     */
    public static void run() {
        System.out.println("========== Pattern 使用示例 ==========");

        // 示例1：ValuePattern - 读写控件值
        valuePatternDemo();

        // 示例2：InvokePattern - 触发操作
        invokePatternDemo();

        // 示例3：WindowPattern - 窗口操作
        windowPatternDemo();

        // 示例4：控件基本操作（点击、输入）
        controlOperationsDemo();

        // 示例5：SelectionPattern - 选择项获取
        selectionPatternDemo();

        // 示例6：ScrollPattern - 滚动操作
        scrollPatternDemo();

        // 示例7：ExpandCollapsePattern - 展开/折叠
        expandCollapsePatternDemo();

        // 示例8：TransformPattern - 移动/调整大小
        transformPatternDemo();

        System.out.println("========== Pattern 使用示例结束 ==========\n");
    }

    /**
     * 示例1：ValuePattern - 读写控件值
     * <p>适用于 Edit、Text、ProgressBar 等支持值读写的控件</p>
     */
    private static void valuePatternDemo() {
        System.out.println("[示例1] ValuePattern - 读写控件值");
        try {
            // 查找编辑框
            EditControl edit = new EditControl(
                    SearchCondition.builder()
                            .controlType(ControlType.Edit)
                            .build()
            );

            // 获取 ValuePattern
            ValuePattern valuePattern = edit.getValuePattern();

            // 读取当前值
            String currentValue = valuePattern.getValue();
            System.out.println("  当前值: " + currentValue);

            // 检查是否只读
            boolean readOnly = valuePattern.isReadOnly();
            System.out.println("  是否只读: " + readOnly);

            // 设置新值
            if (!readOnly) {
                valuePattern.setValue("Hello UIAutomation!");
                System.out.println("  已设置新值");
            }
        } catch (Exception e) {
            System.out.println("  ValuePattern 操作失败: " + e.getMessage());
        }
    }

    /**
     * 示例2：InvokePattern - 触发操作
     * <p>适用于 Button、MenuItem 等支持调用的控件</p>
     */
    private static void invokePatternDemo() {
        System.out.println("[示例2] InvokePattern - 触发操作");
        try {
            // 查找按钮
            SearchCondition buttonCondition = SearchCondition.builder()
                    .name("确定")
                    .controlType(ControlType.Button)
                    .build();

            Control button = Control.getBackend().findControl(buttonCondition);

            // 获取 InvokePattern 并调用
            InvokePattern invokePattern = button.getInvokePattern();
            System.out.println("  准备触发按钮操作...");
            invokePattern.invoke();
            System.out.println("  按钮操作已触发");
        } catch (Exception e) {
            System.out.println("  InvokePattern 操作失败: " + e.getMessage());
        }
    }

    /**
     * 示例3：WindowPattern - 窗口操作
     * <p>提供窗口关闭、最大化、最小化、恢复、置顶等操作</p>
     */
    private static void windowPatternDemo() {
        System.out.println("[示例3] WindowPattern - 窗口操作");
        try {
            // 查找窗口
            WindowControl window = Control.window()
                    .className("Notepad")
                    .findWindow();

            // 获取 WindowPattern
            WindowPattern windowPattern = window.getWindowPattern();

            // 检查是否可以关闭
            boolean canClose = windowPattern.canClose();
            System.out.println("  是否可关闭: " + canClose);

            // 检查是否置顶
            boolean isTopmost = windowPattern.isTopmost();
            System.out.println("  是否置顶: " + isTopmost);

            // 获取窗口状态（0=正常, 1=最大化, 2=最小化）
            int visualState = windowPattern.getVisualState();
            String stateName;
            switch (visualState) {
                case 0: stateName = "正常"; break;
                case 1: stateName = "最大化"; break;
                case 2: stateName = "最小化"; break;
                default: stateName = "未知"; break;
            }
            System.out.println("  窗口状态: " + stateName);

            // 设置窗口置顶
            windowPattern.setTopmost(true);
            System.out.println("  已设置窗口置顶");

            // 取消置顶
            windowPattern.setTopmost(false);
            System.out.println("  已取消窗口置顶");
        } catch (Exception e) {
            System.out.println("  WindowPattern 操作失败: " + e.getMessage());
        }
    }

    /**
     * 示例4：控件基本操作（点击、输入）
     * <p>演示 {@link Control} 提供的便捷方法</p>
     */
    private static void controlOperationsDemo() {
        System.out.println("[示例4] 控件基本操作（点击、输入）");
        try {
            // 查找编辑框并输入文本
            EditControl edit = new EditControl(
                    SearchCondition.builder()
                            .controlType(ControlType.Edit)
                            .build()
            );

            // 点击编辑框（自动等待就绪）
            edit.click();
            System.out.println("  已点击编辑框");

            // 发送按键输入
            edit.sendKeys("Hello, World!");
            System.out.println("  已输入文本");

            // 查找按钮并点击
            SearchCondition buttonCondition = SearchCondition.builder()
                    .name("确定")
                    .controlType(ControlType.Button)
                    .build();

            Control button = Control.getBackend().findControl(buttonCondition);

            // 单击
            button.click();
            System.out.println("  已点击按钮");

            // 双击（如果需要）
            // button.doubleClick();

            // 右键点击（如果需要）
            // button.rightClick();
        } catch (Exception e) {
            System.out.println("  控件操作失败: " + e.getMessage());
        }
    }

    /**
     * 示例5：SelectionPattern - 选择项获取
     * <p>适用于 List、ComboBox、Tab 等包含可选子项的容器控件</p>
     */
    private static void selectionPatternDemo() {
        System.out.println("[示例5] SelectionPattern - 选择项获取");
        try {
            // 查找列表控件
            SearchCondition listCondition = SearchCondition.builder()
                    .controlType(ControlType.List)
                    .build();

            Control list = Control.getBackend().findControl(listCondition);

            // 获取 SelectionPattern
            SelectionPattern selectionPattern = list.getSelectionPattern();

            // 获取当前选中项
            java.util.List<Object> selection = selectionPattern.getSelection();
            System.out.println("  当前选中项数量: " + selection.size());
            for (Object item : selection) {
                System.out.println("    选中项: " + item);
            }

            // 检查是否支持多选
            boolean multiSelect = selectionPattern.isMultiSelect();
            System.out.println("  是否支持多选: " + multiSelect);
        } catch (Exception e) {
            System.out.println("  SelectionPattern 操作失败: " + e.getMessage());
        }
    }

    /**
     * 示例6：ScrollPattern - 滚动操作
     * <p>适用于 ScrollBar、List、DataGrid 等可滚动控件</p>
     */
    private static void scrollPatternDemo() {
        System.out.println("[示例6] ScrollPattern - 滚动操作");
        try {
            // 查找可滚动控件（如列表）
            SearchCondition listCondition = SearchCondition.builder()
                    .controlType(ControlType.List)
                    .build();

            Control list = Control.getBackend().findControl(listCondition);

            // 获取 ScrollPattern
            ScrollPattern scrollPattern = list.getScrollPattern();

            // 检查滚动方向支持
            boolean hScrollable = scrollPattern.isHorizontallyScrollable();
            boolean vScrollable = scrollPattern.isVerticallyScrollable();
            System.out.println("  水平可滚动: " + hScrollable);
            System.out.println("  垂直可滚动: " + vScrollable);

            // 获取当前滚动位置百分比
            double hPercent = scrollPattern.getHorizontalScrollPercent();
            double vPercent = scrollPattern.getVerticalScrollPercent();
            System.out.println("  水平滚动位置: " + hPercent + "%");
            System.out.println("  垂直滚动位置: " + vPercent + "%");

            // 向下滚动一小段
            if (vScrollable) {
                scrollPattern.scroll(
                        ScrollPattern.SCROLL_AMOUNT_NO_AMOUNT, ScrollPattern.SCROLL_DIRECTION_DOWN,
                        0, 1.0);
                System.out.println("  已向下滚动一小段");
            }

            // 设置滚动位置到顶部
            scrollPattern.setScrollPercent(0.0, 0.0);
            System.out.println("  已滚动到顶部");
        } catch (Exception e) {
            System.out.println("  ScrollPattern 操作失败: " + e.getMessage());
        }
    }

    /**
     * 示例7：ExpandCollapsePattern - 展开/折叠
     * <p>适用于 TreeItem、ComboBox、MenuItem 等可展开控件</p>
     */
    private static void expandCollapsePatternDemo() {
        System.out.println("[示例7] ExpandCollapsePattern - 展开/折叠");
        try {
            // 查找树形项
            SearchCondition treeItemCondition = SearchCondition.builder()
                    .controlType(ControlType.TreeItem)
                    .build();

            Control treeItem = Control.getBackend().findControl(treeItemCondition);

            // 获取 ExpandCollapsePattern
            ExpandCollapsePattern ecPattern = treeItem.getExpandCollapsePattern();

            // 获取当前状态
            int state = ecPattern.getExpandCollapseState();
            String stateName;
            switch (state) {
                case ExpandCollapsePattern.STATE_COLLAPSED:
                    stateName = "已折叠"; break;
                case ExpandCollapsePattern.STATE_EXPANDED:
                    stateName = "已展开"; break;
                case ExpandCollapsePattern.STATE_PARTIALLY_EXPANDED:
                    stateName = "部分展开"; break;
                case ExpandCollapsePattern.STATE_LEAF_NODE:
                    stateName = "叶子节点"; break;
                default:
                    stateName = "未知"; break;
            }
            System.out.println("  当前状态: " + stateName);

            // 如果已折叠，则展开
            if (state == ExpandCollapsePattern.STATE_COLLAPSED) {
                ecPattern.expand();
                System.out.println("  已展开");
            } else if (state == ExpandCollapsePattern.STATE_EXPANDED) {
                ecPattern.collapse();
                System.out.println("  已折叠");
            }
        } catch (Exception e) {
            System.out.println("  ExpandCollapsePattern 操作失败: " + e.getMessage());
        }
    }

    /**
     * 示例8：TransformPattern - 移动/调整大小
     * <p>适用于 Window 等支持位置变换的控件</p>
     * <p>注意：Linux AT-SPI2 不直接支持 TransformPattern 操作</p>
     */
    private static void transformPatternDemo() {
        System.out.println("[示例8] TransformPattern - 移动/调整大小");
        try {
            // 查找窗口
            WindowControl window = Control.window()
                    .searchDepth(1)
                    .findWindow();

            // 获取 TransformPattern
            TransformPattern transformPattern = window.getTransformPattern();

            // 检查支持的操作
            boolean canMove = transformPattern.canMove();
            boolean canResize = transformPattern.canResize();
            boolean canRotate = transformPattern.canRotate();
            System.out.println("  可移动: " + canMove);
            System.out.println("  可调整大小: " + canResize);
            System.out.println("  可旋转: " + canRotate);

            // 如果可以调整大小，尝试调整
            if (canResize) {
                transformPattern.resize(800, 600);
                System.out.println("  已调整窗口大小为 800x600");
            }

            // 如果可以移动，尝试移动
            if (canMove) {
                transformPattern.move(100, 100);
                System.out.println("  已移动窗口到 (100, 100)");
            }
        } catch (UnsupportedOperationException e) {
            System.out.println("  当前平台不支持 TransformPattern 操作: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("  TransformPattern 操作失败: " + e.getMessage());
        }
    }
}
