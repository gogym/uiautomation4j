package io.getbit.uiautomation.example;

import io.getbit.uiautomation.condition.SearchCondition;
import io.getbit.uiautomation.enums.ControlType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * SearchCondition Builder 模式全面测试
 *
 * <p>覆盖：默认值、各字段设置、链式调用、toString、边界条件</p>
 */
@DisplayName("SearchCondition 测试")
class SearchConditionTest {

    @Nested
    @DisplayName("默认值测试")
    class DefaultValues {

        @Test
        @DisplayName("空 Builder 构建的默认值应正确")
        void testDefaultValues() {
            SearchCondition condition = SearchCondition.builder().build();

            assertNull(condition.getName(), "name 默认应为 null");
            assertNull(condition.getSubName(), "subName 默认应为 null");
            assertNull(condition.getRegexName(), "regexName 默认应为 null");
            assertNull(condition.getClassName(), "className 默认应为 null");
            assertNull(condition.getAutomationId(), "automationId 默认应为 null");
            assertNull(condition.getControlType(), "controlType 默认应为 null");
            assertNull(condition.getDepth(), "depth 默认应为 null");
            assertEquals(Integer.MAX_VALUE, condition.getSearchDepth(), "searchDepth 默认应为 MAX_VALUE");
            assertEquals(1, condition.getFoundIndex(), "foundIndex 默认应为 1");
            assertEquals(100, condition.getSearchInterval(), "searchInterval 默认应为 100ms");
            assertNull(condition.getCompare(), "compare 默认应为 null");
            assertNull(condition.getSearchFrom(), "searchFrom 默认应为 null");
        }

        @Test
        @DisplayName("has* 方法在默认值下应返回 false")
        void testHasMethodsDefault() {
            SearchCondition condition = SearchCondition.builder().build();

            assertFalse(condition.hasName());
            assertFalse(condition.hasSubName());
            assertFalse(condition.hasRegexName());
            assertFalse(condition.hasClassName());
            assertFalse(condition.hasAutomationId());
            assertFalse(condition.hasControlType());
            assertFalse(condition.hasDepth());
            assertFalse(condition.hasCompare());
        }
    }

    @Nested
    @DisplayName("字段设置测试")
    class FieldSetting {

        @Test
        @DisplayName("设置 name 后 hasName 应返回 true")
        void testSetName() {
            SearchCondition condition = SearchCondition.builder()
                    .name("测试窗口")
                    .build();

            assertTrue(condition.hasName());
            assertEquals("测试窗口", condition.getName());
        }

        @Test
        @DisplayName("设置 subName 后 hasSubName 应返回 true")
        void testSetSubName() {
            SearchCondition condition = SearchCondition.builder()
                    .subName("记事")
                    .build();

            assertTrue(condition.hasSubName());
            assertEquals("记事", condition.getSubName());
        }

        @Test
        @DisplayName("设置 regexName 后 hasRegexName 应返回 true")
        void testSetRegexName() {
            SearchCondition condition = SearchCondition.builder()
                    .regexName(".*Word$")
                    .build();

            assertTrue(condition.hasRegexName());
            assertEquals(".*Word$", condition.getRegexName());
        }

        @Test
        @DisplayName("设置 className 后 hasClassName 应返回 true")
        void testSetClassName() {
            SearchCondition condition = SearchCondition.builder()
                    .className("Notepad")
                    .build();

            assertTrue(condition.hasClassName());
            assertEquals("Notepad", condition.getClassName());
        }

        @Test
        @DisplayName("设置 automationId 后 hasAutomationId 应返回 true")
        void testSetAutomationId() {
            SearchCondition condition = SearchCondition.builder()
                    .automationId("btn_ok")
                    .build();

            assertTrue(condition.hasAutomationId());
            assertEquals("btn_ok", condition.getAutomationId());
        }

        @Test
        @DisplayName("设置 controlType 后 hasControlType 应返回 true")
        void testSetControlType() {
            SearchCondition condition = SearchCondition.builder()
                    .controlType(ControlType.Button)
                    .build();

            assertTrue(condition.hasControlType());
            assertEquals(ControlType.Button, condition.getControlType());
        }

        @Test
        @DisplayName("设置 depth 后 hasDepth 应返回 true")
        void testSetDepth() {
            SearchCondition condition = SearchCondition.builder()
                    .depth(3)
                    .build();

            assertTrue(condition.hasDepth());
            assertEquals(3, condition.getDepth());
        }

        @Test
        @DisplayName("设置 searchDepth 应覆盖默认值")
        void testSetSearchDepth() {
            SearchCondition condition = SearchCondition.builder()
                    .searchDepth(5)
                    .build();

            assertEquals(5, condition.getSearchDepth());
        }

        @Test
        @DisplayName("设置 foundIndex 应覆盖默认值")
        void testSetFoundIndex() {
            SearchCondition condition = SearchCondition.builder()
                    .foundIndex(3)
                    .build();

            assertEquals(3, condition.getFoundIndex());
        }

        @Test
        @DisplayName("设置 searchInterval 应覆盖默认值")
        void testSetSearchInterval() {
            SearchCondition condition = SearchCondition.builder()
                    .searchInterval(500)
                    .build();

            assertEquals(500, condition.getSearchInterval());
        }

        @Test
        @DisplayName("设置 compare 后 hasCompare 应返回 true")
        void testSetCompare() {
            SearchCondition condition = SearchCondition.builder()
                    .compare((element, typeId) -> true)
                    .build();

            assertTrue(condition.hasCompare());
            assertNotNull(condition.getCompare());
        }

        @Test
        @DisplayName("设置 searchFrom 应正确引用父条件")
        void testSetSearchFrom() {
            SearchCondition parent = SearchCondition.builder()
                    .name("父窗口")
                    .controlType(ControlType.Window)
                    .build();

            SearchCondition child = SearchCondition.builder()
                    .name("子按钮")
                    .searchFrom(parent)
                    .build();

            assertNotNull(child.getSearchFrom());
            assertEquals("父窗口", child.getSearchFrom().getName());
        }
    }

    @Nested
    @DisplayName("链式调用测试")
    class ChainedCalls {

        @Test
        @DisplayName("所有字段链式设置应全部生效")
        void testAllFieldsChained() {
            SearchCondition condition = SearchCondition.builder()
                    .name("确定")
                    .className("Button")
                    .automationId("btn_ok")
                    .controlType(ControlType.Button)
                    .searchDepth(3)
                    .foundIndex(2)
                    .searchInterval(200)
                    .build();

            assertEquals("确定", condition.getName());
            assertEquals("Button", condition.getClassName());
            assertEquals("btn_ok", condition.getAutomationId());
            assertEquals(ControlType.Button, condition.getControlType());
            assertEquals(3, condition.getSearchDepth());
            assertEquals(2, condition.getFoundIndex());
            assertEquals(200, condition.getSearchInterval());
        }
    }

    @Nested
    @DisplayName("toString 测试")
    class ToStringTest {

        @Test
        @DisplayName("空条件的 toString 应只包含类名")
        void testEmptyConditionToString() {
            SearchCondition condition = SearchCondition.builder().build();
            String str = condition.toString();

            assertTrue(str.startsWith("SearchCondition{"));
            assertTrue(str.endsWith("}"));
            // 空条件不应包含 name= 等内容
            assertFalse(str.contains("name="));
        }

        @Test
        @DisplayName("有条件字段的 toString 应包含字段信息")
        void testPopulatedConditionToString() {
            SearchCondition condition = SearchCondition.builder()
                    .name("确定")
                    .controlType(ControlType.Button)
                    .searchDepth(3)
                    .build();

            String str = condition.toString();
            assertTrue(str.contains("name='确定'"));
            assertTrue(str.contains("controlType=Button"));
            assertTrue(str.contains("searchDepth=3"));
        }

        @Test
        @DisplayName("默认值字段不应出现在 toString 中")
        void testDefaultFieldsNotInToString() {
            SearchCondition condition = SearchCondition.builder()
                    .name("test")
                    .build();

            String str = condition.toString();
            // foundIndex 默认 1 不应出现
            assertFalse(str.contains("foundIndex="));
            // searchDepth 默认 MAX_VALUE 不应出现
            assertFalse(str.contains("searchDepth="));
        }
    }

    @Nested
    @DisplayName("边界条件测试")
    class BoundaryTests {

        @Test
        @DisplayName("name 设置为 null 应等同未设置")
        void testNameSetNull() {
            SearchCondition condition = SearchCondition.builder()
                    .name(null)
                    .build();

            assertFalse(condition.hasName());
            assertNull(condition.getName());
        }

        @Test
        @DisplayName("空字符串 name 应被视为已设置")
        void testEmptyName() {
            SearchCondition condition = SearchCondition.builder()
                    .name("")
                    .build();

            assertTrue(condition.hasName());
            assertEquals("", condition.getName());
        }

        @Test
        @DisplayName("foundIndex 设置为 0 的边界行为")
        void testFoundIndexZero() {
            SearchCondition condition = SearchCondition.builder()
                    .foundIndex(0)
                    .build();

            assertEquals(0, condition.getFoundIndex());
        }

        @Test
        @DisplayName("searchDepth 设置为 0 的边界行为")
        void testSearchDepthZero() {
            SearchCondition condition = SearchCondition.builder()
                    .searchDepth(0)
                    .build();

            assertEquals(0, condition.getSearchDepth());
        }

        @Test
        @DisplayName("searchDepth 设置为 1 表示只搜索直接子元素")
        void testSearchDepthOne() {
            SearchCondition condition = SearchCondition.builder()
                    .searchDepth(1)
                    .build();

            assertEquals(1, condition.getSearchDepth());
        }
    }
}
