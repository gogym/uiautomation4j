package io.getbit.uiautomation.example;

import io.getbit.uiautomation.exception.AutomationException;
import io.getbit.uiautomation.exception.ControlNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 异常类全面测试
 *
 * <p>覆盖：异常层次结构、构造方法、消息传递、cause 链、
 * 作为 RuntimeException 的特性、多态行为</p>
 */
@DisplayName("异常类测试")
class ExceptionTest {

    @Nested
    @DisplayName("AutomationException 基础测试")
    class AutomationExceptionBasic {

        @Test
        @DisplayName("AutomationException 继承自 RuntimeException")
        void testInheritance() {
            AutomationException ex = new AutomationException("test");
            assertInstanceOf(RuntimeException.class, ex);
        }

        @Test
        @DisplayName("单参数构造方法正确设置消息")
        void testMessageConstructor() {
            AutomationException ex = new AutomationException("操作失败");
            assertEquals("操作失败", ex.getMessage());
            assertNull(ex.getCause());
        }

        @Test
        @DisplayName("双参数构造方法正确设置消息和 cause")
        void testMessageAndCauseConstructor() {
            Throwable cause = new RuntimeException("原始错误");
            AutomationException ex = new AutomationException("包装错误", cause);
            assertEquals("包装错误", ex.getMessage());
            assertSame(cause, ex.getCause());
            assertEquals("原始错误", ex.getCause().getMessage());
        }

        @Test
        @DisplayName("null 消息不抛异常")
        void testNullMessage() {
            AutomationException ex = new AutomationException(null);
            assertNull(ex.getMessage());
        }

        @Test
        @DisplayName("空字符串消息")
        void testEmptyMessage() {
            AutomationException ex = new AutomationException("");
            assertEquals("", ex.getMessage());
        }

        @Test
        @DisplayName("可以正常抛出和捕获")
        void testThrowAndCatch() {
            assertThrows(AutomationException.class, () -> {
                throw new AutomationException("测试异常");
            });
        }

        @Test
        @DisplayName("可以作为 RuntimeException 捕获")
        void testCatchAsRuntimeException() {
            try {
                throw new AutomationException("运行时异常");
            } catch (RuntimeException e) {
                assertEquals("运行时异常", e.getMessage());
            }
        }

        @Test
        @DisplayName("stack trace 不为空")
        void testStackTrace() {
            AutomationException ex = new AutomationException("trace test");
            assertTrue(ex.getStackTrace().length > 0);
        }
    }

    @Nested
    @DisplayName("ControlNotFoundException 测试")
    class ControlNotFoundExceptionTest {

        @Test
        @DisplayName("ControlNotFoundException 继承自 AutomationException")
        void testInheritance() {
            ControlNotFoundException ex = new ControlNotFoundException("未找到");
            assertInstanceOf(AutomationException.class, ex);
            assertInstanceOf(RuntimeException.class, ex);
        }

        @Test
        @DisplayName("单参数构造方法正确设置消息")
        void testMessageConstructor() {
            ControlNotFoundException ex = new ControlNotFoundException("控件未找到: name='OK'");
            assertEquals("控件未找到: name='OK'", ex.getMessage());
            assertNull(ex.getCause());
        }

        @Test
        @DisplayName("双参数构造方法正确设置消息和 cause")
        void testMessageAndCauseConstructor() {
            Throwable cause = new IllegalStateException("元素无效");
            ControlNotFoundException ex = new ControlNotFoundException("搜索失败", cause);
            assertEquals("搜索失败", ex.getMessage());
            assertSame(cause, ex.getCause());
        }

        @Test
        @DisplayName("可以作为 AutomationException 捕获（多态）")
        void testCatchAsAutomationException() {
            try {
                throw new ControlNotFoundException("未找到控件");
            } catch (AutomationException e) {
                assertEquals("未找到控件", e.getMessage());
            }
        }

        @Test
        @DisplayName("可以作为 RuntimeException 捕获（多态）")
        void testCatchAsRuntimeException() {
            try {
                throw new ControlNotFoundException("未找到控件");
            } catch (RuntimeException e) {
                assertEquals("未找到控件", e.getMessage());
            }
        }

        @Test
        @DisplayName("cause 链可以传递")
        void testCauseChain() {
            Exception root = new Exception("根因");
            AutomationException mid = new AutomationException("中间层", root);
            ControlNotFoundException top = new ControlNotFoundException("顶层", mid);

            assertSame(mid, top.getCause());
            assertSame(root, top.getCause().getCause());
            assertEquals("根因", top.getCause().getCause().getMessage());
        }

        @Test
        @DisplayName("null 消息不抛异常")
        void testNullMessage() {
            ControlNotFoundException ex = new ControlNotFoundException(null);
            assertNull(ex.getMessage());
        }
    }

    @Nested
    @DisplayName("异常类关系验证")
    class ExceptionHierarchy {

        @Test
        @DisplayName("ControlNotFoundException 是 AutomationException 的子类")
        void testSubclassRelationship() {
            assertTrue(AutomationException.class.isAssignableFrom(ControlNotFoundException.class));
        }

        @Test
        @DisplayName("AutomationException 是 RuntimeException 的子类")
        void testRuntimeExceptionSubclass() {
            assertTrue(RuntimeException.class.isAssignableFrom(AutomationException.class));
        }

        @Test
        @DisplayName("AutomationException 是 Exception 的子类")
        void testExceptionSubclass() {
            assertTrue(Exception.class.isAssignableFrom(AutomationException.class));
        }

        @Test
        @DisplayName("AutomationException 是 Throwable 的子类")
        void testThrowableSubclass() {
            assertTrue(Throwable.class.isAssignableFrom(AutomationException.class));
        }

        @Test
        @DisplayName("ControlNotFoundException 不是 Error 的子类")
        void testNotError() {
            assertFalse(Error.class.isAssignableFrom(ControlNotFoundException.class));
        }

        @Test
        @DisplayName("instanceof 检查正确")
        void testInstanceof() {
            ControlNotFoundException cnf = new ControlNotFoundException("test");
            assertTrue(cnf instanceof ControlNotFoundException);
            assertTrue(cnf instanceof AutomationException);
            assertTrue(cnf instanceof RuntimeException);
            assertTrue(cnf instanceof Exception);
            assertTrue(cnf instanceof Throwable);
            // cnf 不可能是 Error（编译时已知类型不兼容）
        }
    }
}
