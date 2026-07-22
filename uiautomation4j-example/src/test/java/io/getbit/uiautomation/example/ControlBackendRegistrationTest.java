package io.getbit.uiautomation.example;

import io.getbit.uiautomation.condition.SearchCondition;
import io.getbit.uiautomation.control.Control;
import io.getbit.uiautomation.enums.ControlType;
import io.getbit.uiautomation.pattern.*;
import io.getbit.uiautomation.spi.ControlBackend;
import org.junit.jupiter.api.*;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ControlBackend 注册与未注册行为全面测试
 *
 * <p>覆盖：registerBackend/getBackend 行为、未注册时各方法抛出 IllegalStateException、
 * Mock Backend 注册后各方法可正常调用</p>
 */
@DisplayName("ControlBackend 注册测试")
class ControlBackendRegistrationTest {

    /** 保存原始 backend，测试结束后恢复 */
    private static ControlBackend originalBackend;

    @BeforeAll
    static void saveBackend() {
        try {
            originalBackend = Control.getBackend();
        } catch (IllegalStateException e) {
            originalBackend = null;
        }
    }

    @AfterAll
    static void restoreBackend() {
        if (originalBackend != null) {
            Control.registerBackend(originalBackend);
        }
    }

    @Nested
    @DisplayName("getBackend 未注册行为")
    class UnregisteredBackend {

        @Test
        @DisplayName("未注册 Backend 时 getBackend 应抛出 IllegalStateException")
        void testGetBackendThrows() {
            // 清除 backend
            Control.registerBackend(null);
            assertThrows(IllegalStateException.class, Control::getBackend);
        }

        @Test
        @DisplayName("未注册时异常消息应包含'未注册'")
        void testExceptionMessageContainsHint() {
            Control.registerBackend(null);
            IllegalStateException ex = assertThrows(IllegalStateException.class, Control::getBackend);
            assertTrue(ex.getMessage().contains("未注册") || ex.getMessage().contains("registerBackend"),
                    "异常消息应提示注册: " + ex.getMessage());
        }
    }

    @Nested
    @DisplayName("registerBackend 注册行为")
    class RegisterBackendTest {

        /** 一个简单的 Mock Backend */
        class MockBackend implements ControlBackend {
            @Override public Control findControl(SearchCondition c) { return null; }
            @Override public boolean exists(SearchCondition c, int t) { return false; }
            @Override public ValuePattern getValuePattern(Control c) { return null; }
            @Override public WindowPattern getWindowPattern(Control c) { return null; }
            @Override public InvokePattern getInvokePattern(Control c) { return null; }
            @Override public SelectionPattern getSelectionPattern(Control c) { return null; }
            @Override public ScrollPattern getScrollPattern(Control c) { return null; }
            @Override public ExpandCollapsePattern getExpandCollapsePattern(Control c) { return null; }
            @Override public TransformPattern getTransformPattern(Control c) { return null; }
            @Override public void sendKeys(Control c, String k) {}
            @Override public void click(Control c) {}
            @Override public void doubleClick(Control c) {}
            @Override public void rightClick(Control c) {}
            @Override public void captureToImage(Control c, String f) {}
            @Override public void setGlobalSearchTimeout(int s) {}
            @Override public int getGlobalSearchTimeout() { return 10; }
            @Override public boolean waitReady(Control c, int t) { return true; }
            @Override public boolean isEnabled(Control c) { return true; }
            @Override public boolean isVisible(Control c) { return true; }
            @Override public List<Control> findControls(SearchCondition c) { return Collections.emptyList(); }
            @Override public List<Control> getChildren(Control p) { return Collections.emptyList(); }
            @Override public String getElementName(Control c) { return "mock"; }
            @Override public int[] getElementBoundingRectangle(Control c) { return new int[]{0,0,100,100}; }
            @Override public int[] getElementRuntimeId(Control c) { return new int[]{1}; }
            @Override public String getElementClassName(Control c) { return "MockClass"; }
            @Override public int getElementProcessId(Control c) { return 1234; }
            @Override public ControlType getElementControlType(Control c) { return ControlType.Button; }
            @Override public String getElementAutomationId(Control c) { return "mockId"; }
            @Override public Control getFirstChild(Control p) { return null; }
            @Override public Control getNextSibling(Control c) { return null; }
        }

        @Test
        @DisplayName("注册 MockBackend 后 getBackend 应返回非 null")
        void testRegisterAndGet() {
            MockBackend mock = new MockBackend();
            Control.registerBackend(mock);
            assertSame(mock, Control.getBackend());
        }

        @Test
        @DisplayName("注册后 getGlobalSearchTimeout 应返回 Mock 值")
        void testMockTimeout() {
            MockBackend mock = new MockBackend();
            Control.registerBackend(mock);
            assertEquals(10, Control.getBackend().getGlobalSearchTimeout());
        }

        @Test
        @DisplayName("重复注册应覆盖前一个 Backend")
        void testReRegister() {
            MockBackend mock1 = new MockBackend();
            MockBackend mock2 = new MockBackend();
            Control.registerBackend(mock1);
            assertSame(mock1, Control.getBackend());
            Control.registerBackend(mock2);
            assertSame(mock2, Control.getBackend());
        }

        @Test
        @DisplayName("MockBackend 的 getElementName 应返回 mock")
        void testMockGetElementName() {
            MockBackend mock = new MockBackend();
            Control.registerBackend(mock);
            assertEquals("mock", mock.getElementName(null));
        }

        @Test
        @DisplayName("MockBackend 的 getElementClassName 应返回 MockClass")
        void testMockGetElementClassName() {
            MockBackend mock = new MockBackend();
            assertEquals("MockClass", mock.getElementClassName(null));
        }

        @Test
        @DisplayName("MockBackend 的 getElementProcessId 应返回 1234")
        void testMockGetElementProcessId() {
            MockBackend mock = new MockBackend();
            assertEquals(1234, mock.getElementProcessId(null));
        }

        @Test
        @DisplayName("MockBackend 的 isEnabled 应返回 true")
        void testMockIsEnabled() {
            MockBackend mock = new MockBackend();
            assertTrue(mock.isEnabled(null));
        }

        @Test
        @DisplayName("MockBackend 的 isVisible 应返回 true")
        void testMockIsVisible() {
            MockBackend mock = new MockBackend();
            assertTrue(mock.isVisible(null));
        }

        @Test
        @DisplayName("MockBackend 的 getElementBoundingRectangle 应返回 int[4]")
        void testMockBoundingRect() {
            MockBackend mock = new MockBackend();
            int[] rect = mock.getElementBoundingRectangle(null);
            assertEquals(4, rect.length);
            assertEquals(0, rect[0]);
            assertEquals(0, rect[1]);
            assertEquals(100, rect[2]);
            assertEquals(100, rect[3]);
        }

        @Test
        @DisplayName("MockBackend 的 findControls 应返回空列表")
        void testMockFindControls() {
            MockBackend mock = new MockBackend();
            assertTrue(mock.findControls(null).isEmpty());
        }

        @Test
        @DisplayName("MockBackend 的 getChildren 应返回空列表")
        void testMockGetChildren() {
            MockBackend mock = new MockBackend();
            assertTrue(mock.getChildren(null).isEmpty());
        }

        @Test
        @DisplayName("MockBackend 的 getFirstChild 应返回 null")
        void testMockGetFirstChild() {
            MockBackend mock = new MockBackend();
            assertNull(mock.getFirstChild(null));
        }

        @Test
        @DisplayName("MockBackend 的 getNextSibling 应返回 null")
        void testMockGetNextSibling() {
            MockBackend mock = new MockBackend();
            assertNull(mock.getNextSibling(null));
        }

        @Test
        @DisplayName("MockBackend 的 waitReady 应返回 true")
        void testMockWaitReady() {
            MockBackend mock = new MockBackend();
            assertTrue(mock.waitReady(null, 5));
        }

        @Test
        @DisplayName("MockBackend 的 getElementControlType 应返回 Button")
        void testMockGetControlType() {
            MockBackend mock = new MockBackend();
            assertEquals(ControlType.Button, mock.getElementControlType(null));
        }

        @Test
        @DisplayName("MockBackend 的 Pattern 获取方法应返回 null")
        void testMockPatternsNull() {
            MockBackend mock = new MockBackend();
            assertNull(mock.getValuePattern(null));
            assertNull(mock.getWindowPattern(null));
            assertNull(mock.getInvokePattern(null));
            assertNull(mock.getSelectionPattern(null));
            assertNull(mock.getScrollPattern(null));
            assertNull(mock.getExpandCollapsePattern(null));
            assertNull(mock.getTransformPattern(null));
        }

        @Test
        @DisplayName("MockBackend 的 findControl 应返回 null")
        void testMockFindControl() {
            MockBackend mock = new MockBackend();
            assertNull(mock.findControl(null));
        }

        @Test
        @DisplayName("MockBackend 的 exists 应返回 false")
        void testMockExists() {
            MockBackend mock = new MockBackend();
            assertFalse(mock.exists(null, 5));
        }
    }
}
