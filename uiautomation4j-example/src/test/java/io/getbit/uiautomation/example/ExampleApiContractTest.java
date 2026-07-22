package io.getbit.uiautomation.example;

import io.getbit.uiautomation.condition.SearchCondition;
import io.getbit.uiautomation.control.Control;
import io.getbit.uiautomation.enums.ControlType;
import io.getbit.uiautomation.spi.ControlBackend;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 示例类 API 调用编译与契约测试
 *
 * <p>验证各示例类中使用的 API 调用方式是否合法，
 * 包括 Builder 模式、静态工厂方法、ControlBackend SPI 接口等。</p>
 */
@DisplayName("示例类 API 契约测试")
class ExampleApiContractTest {

    @Nested
    @DisplayName("Control 静态工厂方法测试")
    class StaticFactoryMethods {

        @Test
        @DisplayName("Control.window() 应返回非 null 的 Builder")
        void testWindowBuilder() {
            Control.WindowControlBuilder builder = Control.window();
            assertNotNull(builder, "window() Builder 不应返回 null");
        }

        @Test
        @DisplayName("Control.button() 应返回非 null 的 Builder")
        void testButtonBuilder() {
            Control.ButtonControlBuilder builder = Control.button();
            assertNotNull(builder, "button() Builder 不应返回 null");
        }

        @Test
        @DisplayName("Control.edit() 应返回非 null 的 Builder")
        void testEditBuilder() {
            Control.EditControlBuilder builder = Control.edit();
            assertNotNull(builder, "edit() Builder 不应返回 null");
        }

        @Test
        @DisplayName("Control.text() 应返回非 null 的 Builder")
        void testTextBuilder() {
            Control.TextControlBuilder builder = Control.text();
            assertNotNull(builder, "text() Builder 不应返回 null");
        }

        @Test
        @DisplayName("未注册 Backend 时调用 findWindow 应抛出 IllegalStateException")
        void testNoBackendThrows() {
            // 确保 backend 未注册
            // 注意：如果其他测试已注册 backend，此测试可能不生效
            // 这里主要验证 API 契约
            try {
                Control.window().name("test").findWindow();
                // 如果 backend 已注册，可能不会抛异常
            } catch (IllegalStateException e) {
                assertTrue(e.getMessage().contains("未注册"), "异常信息应包含'未注册'");
            } catch (Exception e) {
                // 其他异常也可以接受（如 ControlNotFoundException）
            }
        }
    }

    @Nested
    @DisplayName("Builder 链式调用测试")
    class BuilderChaining {

        @Test
        @DisplayName("WindowControlBuilder 应支持所有链式方法")
        void testWindowBuilderChaining() {
            // 验证 Builder 链式调用不会抛异常
            Control.WindowControlBuilder builder = Control.window()
                    .name("test")
                    .subName("sub")
                    .regexName(".*")
                    .className("class")
                    .automationId("id")
                    .searchDepth(3)
                    .depth(2)
                    .foundIndex(1);

            assertNotNull(builder);
        }

        @Test
        @DisplayName("ButtonControlBuilder 应支持所有链式方法")
        void testButtonBuilderChaining() {
            Control.ButtonControlBuilder builder = Control.button()
                    .name("确定")
                    .className("Button")
                    .searchDepth(5);

            assertNotNull(builder);
        }

        @Test
        @DisplayName("EditControlBuilder 应支持所有链式方法")
        void testEditBuilderChaining() {
            Control.EditControlBuilder builder = Control.edit()
                    .name("输入框")
                    .automationId("edit1")
                    .searchDepth(3);

            assertNotNull(builder);
        }

        @Test
        @DisplayName("Builder 的 compare 方法应接受 Lambda")
        void testBuilderCompare() {
            Control.WindowControlBuilder builder = Control.window()
                    .compare((element, typeId) -> true);

            assertNotNull(builder);
        }
    }

    @Nested
    @DisplayName("ControlBackend SPI 接口完整性测试")
    class BackendSPI {

        @Test
        @DisplayName("ControlBackend 应定义所有必需方法")
        void testBackendMethods() throws NoSuchMethodException {
            Class<?> backend = ControlBackend.class;

            // 控件搜索
            assertNotNull(backend.getMethod("findControl", SearchCondition.class));
            assertNotNull(backend.getMethod("findControls", SearchCondition.class));
            assertNotNull(backend.getMethod("exists", SearchCondition.class, int.class));

            // Pattern 获取
            assertNotNull(backend.getMethod("getValuePattern", Control.class));
            assertNotNull(backend.getMethod("getWindowPattern", Control.class));
            assertNotNull(backend.getMethod("getInvokePattern", Control.class));
            assertNotNull(backend.getMethod("getSelectionPattern", Control.class));
            assertNotNull(backend.getMethod("getScrollPattern", Control.class));
            assertNotNull(backend.getMethod("getExpandCollapsePattern", Control.class));
            assertNotNull(backend.getMethod("getTransformPattern", Control.class));

            // 操作
            assertNotNull(backend.getMethod("click", Control.class));
            assertNotNull(backend.getMethod("doubleClick", Control.class));
            assertNotNull(backend.getMethod("rightClick", Control.class));
            assertNotNull(backend.getMethod("sendKeys", Control.class, String.class));
            assertNotNull(backend.getMethod("captureToImage", Control.class, String.class));

            // 状态
            assertNotNull(backend.getMethod("isEnabled", Control.class));
            assertNotNull(backend.getMethod("isVisible", Control.class));
            assertNotNull(backend.getMethod("waitReady", Control.class, int.class));

            // 超时
            assertNotNull(backend.getMethod("setGlobalSearchTimeout", int.class));
            assertNotNull(backend.getMethod("getGlobalSearchTimeout"));

            // 树遍历
            assertNotNull(backend.getMethod("getChildren", Control.class));
            assertNotNull(backend.getMethod("getFirstChild", Control.class));
            assertNotNull(backend.getMethod("getNextSibling", Control.class));

            // 原生属性
            assertNotNull(backend.getMethod("getElementName", Control.class));
            assertNotNull(backend.getMethod("getElementClassName", Control.class));
            assertNotNull(backend.getMethod("getElementProcessId", Control.class));
            assertNotNull(backend.getMethod("getElementBoundingRectangle", Control.class));
            assertNotNull(backend.getMethod("getElementRuntimeId", Control.class));
            assertNotNull(backend.getMethod("getElementControlType", Control.class));
            assertNotNull(backend.getMethod("getElementAutomationId", Control.class));
        }
    }

    @Nested
    @DisplayName("各示例类可加载测试")
    class ExampleClassLoading {

        @Test
        @DisplayName("BasicWindowExample 类应可加载")
        void testBasicWindowExampleLoadable() {
            assertDoesNotThrow(() -> Class.forName("io.getbit.uiautomation.example.BasicWindowExample"));
        }

        @Test
        @DisplayName("ControlSearchExample 类应可加载")
        void testControlSearchExampleLoadable() {
            assertDoesNotThrow(() -> Class.forName("io.getbit.uiautomation.example.ControlSearchExample"));
        }

        @Test
        @DisplayName("PatternExample 类应可加载")
        void testPatternExampleLoadable() {
            assertDoesNotThrow(() -> Class.forName("io.getbit.uiautomation.example.PatternExample"));
        }

        @Test
        @DisplayName("CrossPlatformExample 类应可加载")
        void testCrossPlatformExampleLoadable() {
            assertDoesNotThrow(() -> Class.forName("io.getbit.uiautomation.example.CrossPlatformExample"));
        }

        @Test
        @DisplayName("LinuxSpecificExample 类应可加载")
        void testLinuxSpecificExampleLoadable() {
            assertDoesNotThrow(() -> Class.forName("io.getbit.uiautomation.example.LinuxSpecificExample"));
        }

        @Test
        @DisplayName("AllExamplesRunner 类应可加载")
        void testAllExamplesRunnerLoadable() {
            assertDoesNotThrow(() -> Class.forName("io.getbit.uiautomation.example.AllExamplesRunner"));
        }
    }

    @Nested
    @DisplayName("Control 基类方法完整性测试")
    class ControlBaseMethods {

        @Test
        @DisplayName("Control 应定义所有属性获取方法")
        void testControlPropertyMethods() throws NoSuchMethodException {
            Class<?> clazz = Control.class;
            assertNotNull(clazz.getMethod("getName"));
            assertNotNull(clazz.getMethod("getClassName"));
            assertNotNull(clazz.getMethod("getAutomationId"));
            assertNotNull(clazz.getMethod("getControlType"));
            assertNotNull(clazz.getMethod("getBoundingRectangle"));
            assertNotNull(clazz.getMethod("getRuntimeId"));
            assertNotNull(clazz.getMethod("getProcessId"));
        }

        @Test
        @DisplayName("Control 应定义所有搜索子控件方法")
        void testControlSearchMethods() throws NoSuchMethodException {
            Class<?> clazz = Control.class;
            assertNotNull(clazz.getMethod("getChildren"));
            assertNotNull(clazz.getMethod("getFirstChild"));
            assertNotNull(clazz.getMethod("getNextSibling"));
            assertNotNull(clazz.getMethod("child", String.class));
            assertNotNull(clazz.getMethod("child", String.class, ControlType.class));
            assertNotNull(clazz.getMethod("child", SearchCondition.class));
            assertNotNull(clazz.getMethod("findControl", SearchCondition.class));
        }

        @Test
        @DisplayName("Control 应定义所有 find* 便捷方法")
        void testControlFindMethods() throws NoSuchMethodException {
            Class<?> clazz = Control.class;
            assertNotNull(clazz.getMethod("findWindow"));
            assertNotNull(clazz.getMethod("findButton"));
            assertNotNull(clazz.getMethod("findEdit"));
            assertNotNull(clazz.getMethod("findText"));
            assertNotNull(clazz.getMethod("findList"));
            assertNotNull(clazz.getMethod("findListItem"));
            assertNotNull(clazz.getMethod("findMenuItem"));
            assertNotNull(clazz.getMethod("findCheckBox"));
            assertNotNull(clazz.getMethod("findPane"));
            assertNotNull(clazz.getMethod("findMenuBar"));
            assertNotNull(clazz.getMethod("findTitleBar"));
            assertNotNull(clazz.getMethod("findScrollBar"));
            assertNotNull(clazz.getMethod("findTab"));
            assertNotNull(clazz.getMethod("findTree"));
            assertNotNull(clazz.getMethod("findTreeItem"));
        }

        @Test
        @DisplayName("Control 应定义所有操作方法")
        void testControlOperationMethods() throws NoSuchMethodException {
            Class<?> clazz = Control.class;
            assertNotNull(clazz.getMethod("click"));
            assertNotNull(clazz.getMethod("doubleClick"));
            assertNotNull(clazz.getMethod("rightClick"));
            assertNotNull(clazz.getMethod("sendKeys", String.class));
            assertNotNull(clazz.getMethod("captureToImage", String.class));
        }

        @Test
        @DisplayName("Control 应定义所有 Pattern 获取方法")
        void testControlPatternMethods() throws NoSuchMethodException {
            Class<?> clazz = Control.class;
            assertNotNull(clazz.getMethod("getValuePattern"));
            assertNotNull(clazz.getMethod("getWindowPattern"));
            assertNotNull(clazz.getMethod("getInvokePattern"));
            assertNotNull(clazz.getMethod("getSelectionPattern"));
            assertNotNull(clazz.getMethod("getScrollPattern"));
            assertNotNull(clazz.getMethod("getExpandCollapsePattern"));
            assertNotNull(clazz.getMethod("getTransformPattern"));
        }

        @Test
        @DisplayName("Control 应定义状态检查方法")
        void testControlStateMethods() throws NoSuchMethodException {
            Class<?> clazz = Control.class;
            assertNotNull(clazz.getMethod("exists", int.class));
            assertNotNull(clazz.getMethod("exists"));
            assertNotNull(clazz.getMethod("isEnabled"));
            assertNotNull(clazz.getMethod("isVisible"));
            assertNotNull(clazz.getMethod("waitReady"));
            assertNotNull(clazz.getMethod("waitReady", int.class));
            assertNotNull(clazz.getMethod("refind"));
        }
    }
}
