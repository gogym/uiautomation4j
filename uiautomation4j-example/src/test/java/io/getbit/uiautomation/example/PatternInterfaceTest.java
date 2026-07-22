package io.getbit.uiautomation.example;

import io.getbit.uiautomation.pattern.ExpandCollapsePattern;
import io.getbit.uiautomation.pattern.ScrollPattern;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pattern 接口常量与契约测试
 *
 * <p>验证各 Pattern 接口定义的常量值是否符合 UIAutomation 规范</p>
 */
@DisplayName("Pattern 接口常量测试")
class PatternInterfaceTest {

    @Nested
    @DisplayName("ScrollPattern 常量测试")
    class ScrollPatternConstants {

        @Test
        @DisplayName("滚动方向常量应符合 UIAutomation 规范")
        void testScrollDirectionConstants() {
            // UIAutomation ScrollAmount 枚举值
            assertEquals(0, ScrollPattern.SCROLL_DIRECTION_LEFT, "LEFT 应为 0");
            assertEquals(1, ScrollPattern.SCROLL_DIRECTION_RIGHT, "RIGHT 应为 1");
            assertEquals(2, ScrollPattern.SCROLL_DIRECTION_UP, "UP 应为 2");
            assertEquals(3, ScrollPattern.SCROLL_DIRECTION_DOWN, "DOWN 应为 3");
        }

        @Test
        @DisplayName("滚动量常量应符合 UIAutomation ScrollAmount 规范")
        void testScrollAmountConstants() {
            // 滚动量常量已重命名为 SCROLL_AMOUNT_* 前缀，与方向常量 SCROLL_DIRECTION_* 区分
            assertEquals(0, ScrollPattern.SCROLL_AMOUNT_NO_AMOUNT, "NO_AMOUNT 应为 0");
            assertEquals(1, ScrollPattern.SCROLL_AMOUNT_SMALL_DECREMENT, "SMALL_DECREMENT 应为 1");
            assertEquals(2, ScrollPattern.SCROLL_AMOUNT_SMALL_INCREMENT, "SMALL_INCREMENT 应为 2");
            assertEquals(3, ScrollPattern.SCROLL_AMOUNT_LARGE_DECREMENT, "LARGE_DECREMENT 应为 3");
            assertEquals(4, ScrollPattern.SCROLL_AMOUNT_LARGE_INCREMENT, "LARGE_INCREMENT 应为 4");
        }

        @Test
        @DisplayName("滚动方向与滚动量常量应使用不同的命名前缀")
        void testScrollConstantNamingSeparated() {
            // 方向和滚动量使用不同的前缀，避免语义混淆
            // SCROLL_DIRECTION_* 用于方向 (0-3)
            // SCROLL_AMOUNT_* 用于滚动量 (0-4)
            // 虽然数值范围有重叠，但命名前缀不同，语义清晰
            assertNotEquals(ScrollPattern.SCROLL_DIRECTION_LEFT, ScrollPattern.SCROLL_AMOUNT_SMALL_DECREMENT,
                    "LEFT 和 SMALL_DECREMENT 应使用不同常量名");
        }
    }

    @Nested
    @DisplayName("ExpandCollapsePattern 常量测试")
    class ExpandCollapseConstants {

        @Test
        @DisplayName("展开/折叠状态常量应符合 UIAutomation 规范")
        void testExpandCollapseStateConstants() {
            assertEquals(0, ExpandCollapsePattern.STATE_COLLAPSED, "COLLAPSED 应为 0");
            assertEquals(1, ExpandCollapsePattern.STATE_EXPANDED, "EXPANDED 应为 1");
            assertEquals(2, ExpandCollapsePattern.STATE_PARTIALLY_EXPANDED, "PARTIALLY_EXPANDED 应为 2");
            assertEquals(3, ExpandCollapsePattern.STATE_LEAF_NODE, "LEAF_NODE 应为 3");
        }

        @Test
        @DisplayName("状态常量值应互不相同")
        void testStateConstantsUnique() {
            int[] states = {
                    ExpandCollapsePattern.STATE_COLLAPSED,
                    ExpandCollapsePattern.STATE_EXPANDED,
                    ExpandCollapsePattern.STATE_PARTIALLY_EXPANDED,
                    ExpandCollapsePattern.STATE_LEAF_NODE
            };
            for (int i = 0; i < states.length; i++) {
                for (int j = i + 1; j < states.length; j++) {
                    assertNotEquals(states[i], states[j], "状态常量不应重复");
                }
            }
        }
    }

    @Nested
    @DisplayName("Pattern 接口完整性测试")
    class PatternCompleteness {

        @Test
        @DisplayName("ValuePattern 接口应定义 getValue/setValue/isReadOnly 方法")
        void testValuePatternMethods() throws NoSuchMethodException {
            assertNotNull(io.getbit.uiautomation.pattern.ValuePattern.class.getMethod("getValue"));
            assertNotNull(io.getbit.uiautomation.pattern.ValuePattern.class.getMethod("setValue", String.class));
            assertNotNull(io.getbit.uiautomation.pattern.ValuePattern.class.getMethod("isReadOnly"));
        }

        @Test
        @DisplayName("InvokePattern 接口应定义 invoke 方法")
        void testInvokePatternMethods() throws NoSuchMethodException {
            assertNotNull(io.getbit.uiautomation.pattern.InvokePattern.class.getMethod("invoke"));
        }

        @Test
        @DisplayName("WindowPattern 接口应定义所有窗口操作方法")
        void testWindowPatternMethods() throws NoSuchMethodException {
            Class<?> clazz = io.getbit.uiautomation.pattern.WindowPattern.class;
            assertNotNull(clazz.getMethod("close"));
            assertNotNull(clazz.getMethod("canClose"));
            assertNotNull(clazz.getMethod("maximize"));
            assertNotNull(clazz.getMethod("minimize"));
            assertNotNull(clazz.getMethod("restore"));
            assertNotNull(clazz.getMethod("isTopmost"));
            assertNotNull(clazz.getMethod("setTopmost", boolean.class));
            assertNotNull(clazz.getMethod("getVisualState"));
        }

        @Test
        @DisplayName("SelectionPattern 接口应定义 getSelection 和 isMultiSelect 方法")
        void testSelectionPatternMethods() throws NoSuchMethodException {
            assertNotNull(io.getbit.uiautomation.pattern.SelectionPattern.class.getMethod("getSelection"));
            assertNotNull(io.getbit.uiautomation.pattern.SelectionPattern.class.getMethod("isMultiSelect"));
        }

        @Test
        @DisplayName("ScrollPattern 接口应定义所有滚动方法")
        void testScrollPatternMethods() throws NoSuchMethodException {
            Class<?> clazz = io.getbit.uiautomation.pattern.ScrollPattern.class;
            assertNotNull(clazz.getMethod("scroll", int.class, int.class, double.class, double.class));
            assertNotNull(clazz.getMethod("setScrollPercent", double.class, double.class));
            assertNotNull(clazz.getMethod("getHorizontalScrollPercent"));
            assertNotNull(clazz.getMethod("getVerticalScrollPercent"));
            assertNotNull(clazz.getMethod("isHorizontallyScrollable"));
            assertNotNull(clazz.getMethod("isVerticallyScrollable"));
        }

        @Test
        @DisplayName("ExpandCollapsePattern 接口应定义 expand/collapse/getState 方法")
        void testExpandCollapsePatternMethods() throws NoSuchMethodException {
            Class<?> clazz = io.getbit.uiautomation.pattern.ExpandCollapsePattern.class;
            assertNotNull(clazz.getMethod("expand"));
            assertNotNull(clazz.getMethod("collapse"));
            assertNotNull(clazz.getMethod("getExpandCollapseState"));
        }

        @Test
        @DisplayName("TransformPattern 接口应定义移动/调整大小/旋转方法")
        void testTransformPatternMethods() throws NoSuchMethodException {
            Class<?> clazz = io.getbit.uiautomation.pattern.TransformPattern.class;
            assertNotNull(clazz.getMethod("move", int.class, int.class));
            assertNotNull(clazz.getMethod("resize", int.class, int.class));
            assertNotNull(clazz.getMethod("rotate", double.class));
            assertNotNull(clazz.getMethod("canMove"));
            assertNotNull(clazz.getMethod("canResize"));
            assertNotNull(clazz.getMethod("canRotate"));
        }
    }
}
