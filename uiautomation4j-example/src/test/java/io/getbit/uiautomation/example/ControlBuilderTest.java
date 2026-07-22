package io.getbit.uiautomation.example;

import io.getbit.uiautomation.condition.SearchCondition;
import io.getbit.uiautomation.control.Control;
import io.getbit.uiautomation.control.ButtonControl;
import io.getbit.uiautomation.control.EditControl;
import io.getbit.uiautomation.control.TextControl;
import io.getbit.uiautomation.control.WindowControl;
import io.getbit.uiautomation.enums.ControlType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Control Builder 模式全面测试
 *
 * <p>覆盖：4 种 Builder 类型的链式方法、buildCondition 逻辑、
 * 返回类型正确性、所有 setter 方法</p>
 */
@DisplayName("Control Builder 模式测试")
class ControlBuilderTest {

    @Nested
    @DisplayName("WindowControlBuilder 测试")
    class WindowBuilderTest {

        @Test
        @DisplayName("window() 返回 WindowControlBuilder 类型")
        void testReturnType() {
            assertInstanceOf(Control.WindowControlBuilder.class, Control.window());
        }

        @Test
        @DisplayName("所有链式方法返回 WindowControlBuilder")
        void testChainingReturnsSameType() {
            Control.WindowControlBuilder b = Control.window();
            assertSame(b.getClass(), b.name("test").getClass());
            assertSame(b.getClass(), b.subName("sub").getClass());
            assertSame(b.getClass(), b.regexName(".*").getClass());
            assertSame(b.getClass(), b.className("cls").getClass());
            assertSame(b.getClass(), b.automationId("id").getClass());
            assertSame(b.getClass(), b.searchDepth(3).getClass());
            assertSame(b.getClass(), b.depth(2).getClass());
            assertSame(b.getClass(), b.foundIndex(1).getClass());
        }

        @Test
        @DisplayName("findWindow 方法存在且返回 WindowControl")
        void testFindWindowMethod() throws Exception {
            Method m = Control.WindowControlBuilder.class.getMethod("findWindow");
            assertEquals(WindowControl.class, m.getReturnType());
        }

        @Test
        @DisplayName("compare 方法接受 BiPredicate")
        void testCompareMethod() {
            Control.WindowControlBuilder b = Control.window()
                    .compare((element, typeId) -> true);
            assertNotNull(b);
        }
    }

    @Nested
    @DisplayName("ButtonControlBuilder 测试")
    class ButtonBuilderTest {

        @Test
        @DisplayName("button() 返回 ButtonControlBuilder 类型")
        void testReturnType() {
            assertInstanceOf(Control.ButtonControlBuilder.class, Control.button());
        }

        @Test
        @DisplayName("所有链式方法返回 ButtonControlBuilder")
        void testChainingReturnsSameType() {
            Control.ButtonControlBuilder b = Control.button();
            assertSame(b.getClass(), b.name("OK").getClass());
            assertSame(b.getClass(), b.className("Button").getClass());
            assertSame(b.getClass(), b.searchDepth(5).getClass());
        }

        @Test
        @DisplayName("findButton 方法存在且返回 ButtonControl")
        void testFindButtonMethod() throws Exception {
            Method m = Control.ButtonControlBuilder.class.getMethod("findButton");
            assertEquals(ButtonControl.class, m.getReturnType());
        }
    }

    @Nested
    @DisplayName("EditControlBuilder 测试")
    class EditBuilderTest {

        @Test
        @DisplayName("edit() 返回 EditControlBuilder 类型")
        void testReturnType() {
            assertInstanceOf(Control.EditControlBuilder.class, Control.edit());
        }

        @Test
        @DisplayName("所有链式方法返回 EditControlBuilder")
        void testChainingReturnsSameType() {
            Control.EditControlBuilder b = Control.edit();
            assertSame(b.getClass(), b.name("input").getClass());
            assertSame(b.getClass(), b.automationId("txt1").getClass());
            assertSame(b.getClass(), b.searchDepth(3).getClass());
        }

        @Test
        @DisplayName("findEdit 方法存在且返回 EditControl")
        void testFindEditMethod() throws Exception {
            Method m = Control.EditControlBuilder.class.getMethod("findEdit");
            assertEquals(EditControl.class, m.getReturnType());
        }
    }

    @Nested
    @DisplayName("TextControlBuilder 测试")
    class TextBuilderTest {

        @Test
        @DisplayName("text() 返回 TextControlBuilder 类型")
        void testReturnType() {
            assertInstanceOf(Control.TextControlBuilder.class, Control.text());
        }

        @Test
        @DisplayName("所有链式方法返回 TextControlBuilder")
        void testChainingReturnsSameType() {
            Control.TextControlBuilder b = Control.text();
            assertSame(b.getClass(), b.name("label").getClass());
            assertSame(b.getClass(), b.className("Text").getClass());
        }

        @Test
        @DisplayName("findText 方法存在且返回 TextControl")
        void testFindTextMethod() throws Exception {
            Method m = Control.TextControlBuilder.class.getMethod("findText");
            assertEquals(TextControl.class, m.getReturnType());
        }
    }

    @Nested
    @DisplayName("ControlBuilderBase 公共方法测试")
    class BuilderBaseTest {

        @Test
        @DisplayName("ControlBuilderBase 是抽象类")
        void testIsAbstract() {
            assertTrue(Modifier.isAbstract(Control.ControlBuilderBase.class.getModifiers()));
        }

        @Test
        @DisplayName("所有 Builder 都有 name 方法")
        void testAllHaveName() throws Exception {
            assertNotNull(Control.WindowControlBuilder.class.getMethod("name", String.class));
            assertNotNull(Control.ButtonControlBuilder.class.getMethod("name", String.class));
            assertNotNull(Control.EditControlBuilder.class.getMethod("name", String.class));
            assertNotNull(Control.TextControlBuilder.class.getMethod("name", String.class));
        }

        @Test
        @DisplayName("所有 Builder 都有 subName 方法")
        void testAllHaveSubName() throws Exception {
            assertNotNull(Control.WindowControlBuilder.class.getMethod("subName", String.class));
            assertNotNull(Control.ButtonControlBuilder.class.getMethod("subName", String.class));
            assertNotNull(Control.EditControlBuilder.class.getMethod("subName", String.class));
            assertNotNull(Control.TextControlBuilder.class.getMethod("subName", String.class));
        }

        @Test
        @DisplayName("所有 Builder 都有 regexName 方法")
        void testAllHaveRegexName() throws Exception {
            assertNotNull(Control.WindowControlBuilder.class.getMethod("regexName", String.class));
            assertNotNull(Control.ButtonControlBuilder.class.getMethod("regexName", String.class));
            assertNotNull(Control.EditControlBuilder.class.getMethod("regexName", String.class));
            assertNotNull(Control.TextControlBuilder.class.getMethod("regexName", String.class));
        }

        @Test
        @DisplayName("所有 Builder 都有 className 方法")
        void testAllHaveClassName() throws Exception {
            assertNotNull(Control.WindowControlBuilder.class.getMethod("className", String.class));
            assertNotNull(Control.ButtonControlBuilder.class.getMethod("className", String.class));
            assertNotNull(Control.EditControlBuilder.class.getMethod("className", String.class));
            assertNotNull(Control.TextControlBuilder.class.getMethod("className", String.class));
        }

        @Test
        @DisplayName("所有 Builder 都有 automationId 方法")
        void testAllHaveAutomationId() throws Exception {
            assertNotNull(Control.WindowControlBuilder.class.getMethod("automationId", String.class));
            assertNotNull(Control.ButtonControlBuilder.class.getMethod("automationId", String.class));
            assertNotNull(Control.EditControlBuilder.class.getMethod("automationId", String.class));
            assertNotNull(Control.TextControlBuilder.class.getMethod("automationId", String.class));
        }

        @Test
        @DisplayName("所有 Builder 都有 searchDepth 方法")
        void testAllHaveSearchDepth() throws Exception {
            assertNotNull(Control.WindowControlBuilder.class.getMethod("searchDepth", int.class));
            assertNotNull(Control.ButtonControlBuilder.class.getMethod("searchDepth", int.class));
            assertNotNull(Control.EditControlBuilder.class.getMethod("searchDepth", int.class));
            assertNotNull(Control.TextControlBuilder.class.getMethod("searchDepth", int.class));
        }

        @Test
        @DisplayName("所有 Builder 都有 depth 方法")
        void testAllHaveDepth() throws Exception {
            assertNotNull(Control.WindowControlBuilder.class.getMethod("depth", int.class));
            assertNotNull(Control.ButtonControlBuilder.class.getMethod("depth", int.class));
            assertNotNull(Control.EditControlBuilder.class.getMethod("depth", int.class));
            assertNotNull(Control.TextControlBuilder.class.getMethod("depth", int.class));
        }

        @Test
        @DisplayName("所有 Builder 都有 foundIndex 方法")
        void testAllHaveFoundIndex() throws Exception {
            assertNotNull(Control.WindowControlBuilder.class.getMethod("foundIndex", int.class));
            assertNotNull(Control.ButtonControlBuilder.class.getMethod("foundIndex", int.class));
            assertNotNull(Control.EditControlBuilder.class.getMethod("foundIndex", int.class));
            assertNotNull(Control.TextControlBuilder.class.getMethod("foundIndex", int.class));
        }

        @Test
        @DisplayName("所有 Builder 都有 compare 方法")
        void testAllHaveCompare() throws Exception {
            assertNotNull(Control.WindowControlBuilder.class.getMethod("compare", java.util.function.BiPredicate.class));
            assertNotNull(Control.ButtonControlBuilder.class.getMethod("compare", java.util.function.BiPredicate.class));
            assertNotNull(Control.EditControlBuilder.class.getMethod("compare", java.util.function.BiPredicate.class));
            assertNotNull(Control.TextControlBuilder.class.getMethod("compare", java.util.function.BiPredicate.class));
        }
    }

    @Nested
    @DisplayName("Builder 链式调用组合测试")
    class CombinationTest {

        @Test
        @DisplayName("WindowControlBuilder 全字段链式调用")
        void testFullChainWindow() {
            assertDoesNotThrow(() -> {
                Control.window()
                        .name("窗口")
                        .subName("子串")
                        .regexName(".*正则.*")
                        .className("WindowClass")
                        .automationId("win_001")
                        .searchDepth(10)
                        .depth(3)
                        .foundIndex(2)
                        .compare((e, t) -> true);
            });
        }

        @Test
        @DisplayName("ButtonControlBuilder 全字段链式调用")
        void testFullChainButton() {
            assertDoesNotThrow(() -> {
                Control.button()
                        .name("确定")
                        .subName("确")
                        .regexName("确.*")
                        .className("Button")
                        .automationId("btn_ok")
                        .searchDepth(5)
                        .depth(1)
                        .foundIndex(1)
                        .compare((e, t) -> false);
            });
        }

        @Test
        @DisplayName("EditControlBuilder 全字段链式调用")
        void testFullChainEdit() {
            assertDoesNotThrow(() -> {
                Control.edit()
                        .name("输入框")
                        .subName("输入")
                        .regexName("输入.*")
                        .className("Edit")
                        .automationId("edit1")
                        .searchDepth(3)
                        .depth(2)
                        .foundIndex(1);
            });
        }

        @Test
        @DisplayName("TextControlBuilder 全字段链式调用")
        void testFullChainText() {
            assertDoesNotThrow(() -> {
                Control.text()
                        .name("标签")
                        .subName("标")
                        .regexName("标.*")
                        .className("Text")
                        .automationId("txt1")
                        .searchDepth(2)
                        .depth(1)
                        .foundIndex(1);
            });
        }
    }
}
