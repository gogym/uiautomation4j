package io.getbit.uiautomation.example;

import io.getbit.uiautomation.condition.SearchCondition;
import io.getbit.uiautomation.control.*;
import io.getbit.uiautomation.enums.ControlType;
import io.getbit.uiautomation.exception.AutomationException;
import io.getbit.uiautomation.pattern.*;
import io.getbit.uiautomation.spi.ControlBackend;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Control 基类方法行为测试（使用 MockBackend）
 *
 * <p>覆盖：属性获取方法、搜索方法、操作方法、Pattern 获取、
 * 状态检查、ensureElement 逻辑、toString</p>
 */
@DisplayName("Control 基类行为测试")
class ControlBehaviorTest {

    /** 记录调用次数的 MockBackend */
    static class TrackingBackend implements ControlBackend {
        int findControlCalls = 0;
        int clickCalls = 0;
        int doubleClickCalls = 0;
        int rightClickCalls = 0;
        int sendKeysCalls = 0;
        int captureCalls = 0;
        int waitReadyCalls = 0;
        boolean waitReadyResult = true;
        boolean isEnabledResult = true;
        boolean isVisibleResult = true;
        String elementName = "TestElement";
        String className = "TestClass";
        int processId = 9999;
        ControlType controlType = ControlType.Button;
        int[] boundingRect = {10, 20, 200, 100};
        int[] runtimeId = {1, 2, 3};

        @Override
        public Control findControl(SearchCondition c) {
            findControlCalls++;
            ButtonControl found = new ButtonControl(c);
            found.setNativeElement(new Object());
            found.setElementFound(true);
            return found;
        }
        @Override public boolean exists(SearchCondition c, int t) { return true; }
        @Override public ValuePattern getValuePattern(Control c) { return null; }
        @Override public WindowPattern getWindowPattern(Control c) { return null; }
        @Override public InvokePattern getInvokePattern(Control c) { return null; }
        @Override public SelectionPattern getSelectionPattern(Control c) { return null; }
        @Override public ScrollPattern getScrollPattern(Control c) { return null; }
        @Override public ExpandCollapsePattern getExpandCollapsePattern(Control c) { return null; }
        @Override public TransformPattern getTransformPattern(Control c) { return null; }
        @Override public void sendKeys(Control c, String k) { sendKeysCalls++; }
        @Override public void click(Control c) { clickCalls++; }
        @Override public void doubleClick(Control c) { doubleClickCalls++; }
        @Override public void rightClick(Control c) { rightClickCalls++; }
        @Override public void captureToImage(Control c, String f) { captureCalls++; }
        @Override public void setGlobalSearchTimeout(int s) {}
        @Override public int getGlobalSearchTimeout() { return 10; }
        @Override public boolean waitReady(Control c, int t) { waitReadyCalls++; return waitReadyResult; }
        @Override public boolean isEnabled(Control c) { return isEnabledResult; }
        @Override public boolean isVisible(Control c) { return isVisibleResult; }
        @Override public List<Control> findControls(SearchCondition c) { return Collections.emptyList(); }
        @Override public List<Control> getChildren(Control p) { return Collections.emptyList(); }
        @Override public String getElementName(Control c) { return elementName; }
        @Override public int[] getElementBoundingRectangle(Control c) { return boundingRect; }
        @Override public int[] getElementRuntimeId(Control c) { return runtimeId; }
        @Override public String getElementClassName(Control c) { return className; }
        @Override public int getElementProcessId(Control c) { return processId; }
        @Override public ControlType getElementControlType(Control c) { return controlType; }
        @Override public String getElementAutomationId(Control c) { return "testAutoId"; }
        @Override public Control getFirstChild(Control p) { return null; }
        @Override public Control getNextSibling(Control c) { return null; }
    }

    private static TrackingBackend mock;
    private static ControlBackend savedBackend;

    @BeforeAll
    static void setup() {
        try { savedBackend = Control.getBackend(); } catch (Exception e) { savedBackend = null; }
        mock = new TrackingBackend();
        Control.registerBackend(mock);
    }

    @AfterAll
    static void teardown() {
        if (savedBackend != null) Control.registerBackend(savedBackend);
    }

    @BeforeEach
    void resetTracking() {
        mock.findControlCalls = 0;
        mock.clickCalls = 0;
        mock.doubleClickCalls = 0;
        mock.rightClickCalls = 0;
        mock.sendKeysCalls = 0;
        mock.captureCalls = 0;
        mock.waitReadyCalls = 0;
        mock.isEnabledResult = true;
        mock.isVisibleResult = true;
        mock.waitReadyResult = true;
    }

    @Nested
    @DisplayName("属性获取方法测试")
    class PropertyMethods {

        @Test
        @DisplayName("getName 应调用 backend.getElementName")
        void testGetName() {
            mock.elementName = "MyButton";
            ButtonControl btn = new ButtonControl(SearchCondition.builder().name("test").build());
            btn.setElementFound(true);
            btn.setNativeElement(new Object());
            assertEquals("MyButton", btn.getName());
        }

        @Test
        @DisplayName("getClassName 应调用 backend.getElementClassName")
        void testGetClassName() {
            mock.className = "NSButton";
            ButtonControl btn = new ButtonControl(SearchCondition.builder().name("test").build());
            btn.setElementFound(true);
            btn.setNativeElement(new Object());
            assertEquals("NSButton", btn.getClassName());
        }

        @Test
        @DisplayName("getProcessId 应调用 backend.getElementProcessId")
        void testGetProcessId() {
            mock.processId = 12345;
            ButtonControl btn = new ButtonControl(SearchCondition.builder().name("test").build());
            btn.setElementFound(true);
            btn.setNativeElement(new Object());
            assertEquals(12345, btn.getProcessId());
        }

        @Test
        @DisplayName("getBoundingRectangle 应返回 int[4]")
        void testGetBoundingRectangle() {
            mock.boundingRect = new int[]{5, 10, 300, 200};
            ButtonControl btn = new ButtonControl(SearchCondition.builder().name("test").build());
            btn.setElementFound(true);
            btn.setNativeElement(new Object());
            int[] rect = btn.getBoundingRectangle();
            assertEquals(4, rect.length);
            assertEquals(5, rect[0]);
            assertEquals(10, rect[1]);
            assertEquals(300, rect[2]);
            assertEquals(200, rect[3]);
        }

        @Test
        @DisplayName("getRuntimeId 应返回 backend 提供的数组")
        void testGetRuntimeId() {
            mock.runtimeId = new int[]{7, 8, 9};
            ButtonControl btn = new ButtonControl(SearchCondition.builder().name("test").build());
            btn.setElementFound(true);
            btn.setNativeElement(new Object());
            int[] rid = btn.getRuntimeId();
            assertArrayEquals(new int[]{7, 8, 9}, rid);
        }

        @Test
        @DisplayName("getControlType 应调用 backend.getElementControlType")
        void testGetControlType() {
            mock.controlType = ControlType.Window;
            WindowControl win = new WindowControl(SearchCondition.builder().controlType(ControlType.Window).build());
            win.setElementFound(true);
            win.setNativeElement(new Object());
            assertEquals(ControlType.Window, win.getControlType());
        }

        @Test
        @DisplayName("getAutomationId 应调用 backend.getElementAutomationId")
        void testGetAutomationIdDefault() {
            ButtonControl btn = new ButtonControl(SearchCondition.builder().name("test").build());
            btn.setElementFound(true);
            btn.setNativeElement(new Object());
            assertEquals("testAutoId", btn.getAutomationId());
        }
    }

    @Nested
    @DisplayName("ensureElement 逻辑测试")
    class EnsureElementTest {

        @Test
        @DisplayName("elementFound=false 时应自动调用 findControl")
        void testAutoFind() {
            mock.findControlCalls = 0;
            ButtonControl btn = new ButtonControl(SearchCondition.builder().name("auto").build());
            assertFalse(btn.isElementFound());
            btn.getName(); // 触发 ensureElement
            assertTrue(btn.isElementFound());
            assertEquals(1, mock.findControlCalls);
        }

        @Test
        @DisplayName("elementFound=true 时不应再调用 findControl")
        void testNoRefind() {
            mock.findControlCalls = 0;
            ButtonControl btn = new ButtonControl(SearchCondition.builder().name("cached").build());
            btn.setElementFound(true);
            btn.setNativeElement(new Object());
            btn.getName();
            assertEquals(0, mock.findControlCalls);
        }

        @Test
        @DisplayName("多次属性访问在 elementFound=true 时不重复搜索")
        void testMultipleAccessNoRefind() {
            mock.findControlCalls = 0;
            ButtonControl btn = new ButtonControl(SearchCondition.builder().name("multi").build());
            btn.setElementFound(true);
            btn.setNativeElement(new Object());
            btn.getName();
            btn.getClassName();
            btn.getProcessId();
            assertEquals(0, mock.findControlCalls);
        }
    }

    @Nested
    @DisplayName("操作方法测试")
    class OperationMethods {

        @Test
        @DisplayName("click 应调用 backend.click")
        void testClick() {
            ButtonControl btn = new ButtonControl(SearchCondition.builder().name("click").build());
            btn.setElementFound(true);
            btn.setNativeElement(new Object());
            btn.click();
            assertEquals(1, mock.clickCalls);
        }

        @Test
        @DisplayName("doubleClick 应调用 backend.doubleClick")
        void testDoubleClick() {
            ButtonControl btn = new ButtonControl(SearchCondition.builder().name("dbl").build());
            btn.setElementFound(true);
            btn.setNativeElement(new Object());
            btn.doubleClick();
            assertEquals(1, mock.doubleClickCalls);
        }

        @Test
        @DisplayName("rightClick 应调用 backend.rightClick")
        void testRightClick() {
            ButtonControl btn = new ButtonControl(SearchCondition.builder().name("rc").build());
            btn.setElementFound(true);
            btn.setNativeElement(new Object());
            btn.rightClick();
            assertEquals(1, mock.rightClickCalls);
        }

        @Test
        @DisplayName("sendKeys 应调用 backend.sendKeys")
        void testSendKeys() {
            ButtonControl btn = new ButtonControl(SearchCondition.builder().name("keys").build());
            btn.setElementFound(true);
            btn.setNativeElement(new Object());
            btn.sendKeys("Hello");
            assertEquals(1, mock.sendKeysCalls);
        }

        @Test
        @DisplayName("captureToImage 应调用 backend.captureToImage")
        void testCapture() {
            ButtonControl btn = new ButtonControl(SearchCondition.builder().name("cap").build());
            btn.setElementFound(true);
            btn.setNativeElement(new Object());
            btn.captureToImage("/tmp/test.png");
            assertEquals(1, mock.captureCalls);
        }
    }

    @Nested
    @DisplayName("状态检查方法测试")
    class StateMethods {

        @Test
        @DisplayName("isEnabled 应返回 backend 结果")
        void testIsEnabled() {
            mock.isEnabledResult = true;
            ButtonControl btn = new ButtonControl(SearchCondition.builder().name("en").build());
            btn.setElementFound(true);
            btn.setNativeElement(new Object());
            assertTrue(btn.isEnabled());

            mock.isEnabledResult = false;
            assertFalse(btn.isEnabled());
        }

        @Test
        @DisplayName("isVisible 应返回 backend 结果")
        void testIsVisible() {
            mock.isVisibleResult = true;
            ButtonControl btn = new ButtonControl(SearchCondition.builder().name("vis").build());
            btn.setElementFound(true);
            btn.setNativeElement(new Object());
            assertTrue(btn.isVisible());

            mock.isVisibleResult = false;
            assertFalse(btn.isVisible());
        }

        @Test
        @DisplayName("exists 应调用 backend.exists")
        void testExists() {
            ButtonControl btn = new ButtonControl(SearchCondition.builder().name("ex").build());
            assertTrue(btn.exists(5));
        }

        @Test
        @DisplayName("exists() 无参版本使用默认超时")
        void testExistsDefault() {
            ButtonControl btn = new ButtonControl(SearchCondition.builder().name("ex2").build());
            assertTrue(btn.exists());
        }
    }

    @Nested
    @DisplayName("waitReady 测试")
    class WaitReadyTest {

        @Test
        @DisplayName("waitReady 成功时不抛异常")
        void testWaitReadySuccess() {
            mock.waitReadyResult = true;
            ButtonControl btn = new ButtonControl(SearchCondition.builder().name("wr").build());
            assertDoesNotThrow(() -> btn.waitReady(5));
        }

        @Test
        @DisplayName("waitReady 超时时抛出 AutomationException")
        void testWaitReadyTimeout() {
            mock.waitReadyResult = false;
            ButtonControl btn = new ButtonControl(SearchCondition.builder().name("wrt").build());
            assertThrows(AutomationException.class, () -> btn.waitReady(3));
        }

        @Test
        @DisplayName("waitReady 成功后重置 elementFound")
        void testWaitReadyResetsState() {
            mock.waitReadyResult = true;
            ButtonControl btn = new ButtonControl(SearchCondition.builder().name("rst").build());
            btn.setElementFound(true);
            btn.setNativeElement(new Object());
            assertTrue(btn.isElementFound());
            btn.waitReady(5);
            assertFalse(btn.isElementFound());
            assertNull(btn.getNativeElement());
        }
    }

    @Nested
    @DisplayName("refind 测试")
    class RefindTest {

        @Test
        @DisplayName("refind 应重置 elementFound 并重新查找")
        void testRefind() {
            mock.findControlCalls = 0;
            ButtonControl btn = new ButtonControl(SearchCondition.builder().name("rf").build());
            btn.setElementFound(true);
            btn.setNativeElement(new Object());
            assertTrue(btn.isElementFound());

            btn.refind();
            assertTrue(btn.isElementFound()); // refind 后重新找到了
            assertEquals(1, mock.findControlCalls);
        }
    }

    @Nested
    @DisplayName("子控件搜索方法测试")
    class ChildSearchMethods {

        @Test
        @DisplayName("getChildren 应调用 backend.getChildren")
        void testGetChildren() {
            ButtonControl btn = new ButtonControl(SearchCondition.builder().name("ch").build());
            btn.setElementFound(true);
            btn.setNativeElement(new Object());
            List<Control> children = btn.getChildren();
            assertNotNull(children);
        }

        @Test
        @DisplayName("getFirstChild 应调用 backend.getFirstChild")
        void testGetFirstChild() {
            ButtonControl btn = new ButtonControl(SearchCondition.builder().name("fc").build());
            btn.setElementFound(true);
            btn.setNativeElement(new Object());
            // mock 返回 null
            assertNull(btn.getFirstChild());
        }

        @Test
        @DisplayName("getNextSibling 应调用 backend.getNextSibling")
        void testGetNextSibling() {
            ButtonControl btn = new ButtonControl(SearchCondition.builder().name("ns").build());
            btn.setElementFound(true);
            btn.setNativeElement(new Object());
            assertNull(btn.getNextSibling());
        }
    }

    @Nested
    @DisplayName("Pattern 获取方法测试")
    class PatternMethods {

        @Test
        @DisplayName("getValuePattern 应调用 backend.getValuePattern")
        void testGetValuePattern() {
            ButtonControl btn = new ButtonControl(SearchCondition.builder().name("vp").build());
            btn.setElementFound(true);
            btn.setNativeElement(new Object());
            // mock 返回 null
            assertNull(btn.getValuePattern());
        }

        @Test
        @DisplayName("getWindowPattern 应调用 backend.getWindowPattern")
        void testGetWindowPattern() {
            WindowControl win = new WindowControl(SearchCondition.builder().controlType(ControlType.Window).build());
            win.setElementFound(true);
            win.setNativeElement(new Object());
            assertNull(win.getWindowPattern());
        }

        @Test
        @DisplayName("所有 Pattern 获取方法不抛异常")
        void testAllPatternMethodsNoException() {
            ButtonControl btn = new ButtonControl(SearchCondition.builder().name("pat").build());
            btn.setElementFound(true);
            btn.setNativeElement(new Object());
            assertDoesNotThrow(btn::getValuePattern);
            assertDoesNotThrow(btn::getWindowPattern);
            assertDoesNotThrow(btn::getInvokePattern);
            assertDoesNotThrow(btn::getSelectionPattern);
            assertDoesNotThrow(btn::getScrollPattern);
            assertDoesNotThrow(btn::getExpandCollapsePattern);
            assertDoesNotThrow(btn::getTransformPattern);
        }
    }

    @Nested
    @DisplayName("toString 测试")
    class ToStringTest {

        @Test
        @DisplayName("未找到元素时 toString 应包含 searchCondition")
        void testToStringBeforeFind() {
            ButtonControl btn = new ButtonControl(SearchCondition.builder().name("tst").build());
            String str = btn.toString();
            assertTrue(str.contains("ButtonControl"));
            assertTrue(str.contains("elementFound=false"));
        }

        @Test
        @DisplayName("已找到元素时 toString 应包含 name 和 controlType")
        void testToStringAfterFind() {
            mock.elementName = "FoundBtn";
            mock.controlType = ControlType.Button;
            ButtonControl btn = new ButtonControl(SearchCondition.builder().name("found").build());
            btn.setElementFound(true);
            btn.setNativeElement(new Object());
            String str = btn.toString();
            assertTrue(str.contains("ButtonControl"));
            assertTrue(str.contains("FoundBtn"));
            assertTrue(str.contains("Button"));
        }
    }
}
