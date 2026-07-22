package io.getbit.uiautomation.example;

import io.getbit.uiautomation.condition.SearchCondition;
import io.getbit.uiautomation.control.*;
import io.getbit.uiautomation.enums.ControlType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.function.Executable;

/**
 * 控件层次结构全面测试
 *
 * <p>覆盖：所有 15 种控件类型的继承关系、构造方法、
 * 特有方法、Control 基类方法继承</p>
 */
@DisplayName("控件层次结构测试")
class ControlHierarchyTest {

    @Nested
    @DisplayName("所有控件类型继承 Control")
    class InheritanceTest {

        @Test
        @DisplayName("ButtonControl 继承 Control")
        void testButtonInheritance() {
            assertTrue(Control.class.isAssignableFrom(ButtonControl.class));
        }

        @Test
        @DisplayName("CheckBoxControl 继承 Control")
        void testCheckBoxInheritance() {
            assertTrue(Control.class.isAssignableFrom(CheckBoxControl.class));
        }

        @Test
        @DisplayName("EditControl 继承 Control")
        void testEditInheritance() {
            assertTrue(Control.class.isAssignableFrom(EditControl.class));
        }

        @Test
        @DisplayName("ListControl 继承 Control")
        void testListInheritance() {
            assertTrue(Control.class.isAssignableFrom(ListControl.class));
        }

        @Test
        @DisplayName("ListItemControl 继承 Control")
        void testListItemInheritance() {
            assertTrue(Control.class.isAssignableFrom(ListItemControl.class));
        }

        @Test
        @DisplayName("MenuBarControl 继承 Control")
        void testMenuBarInheritance() {
            assertTrue(Control.class.isAssignableFrom(MenuBarControl.class));
        }

        @Test
        @DisplayName("MenuItemControl 继承 Control")
        void testMenuItemInheritance() {
            assertTrue(Control.class.isAssignableFrom(MenuItemControl.class));
        }

        @Test
        @DisplayName("PaneControl 继承 Control")
        void testPaneInheritance() {
            assertTrue(Control.class.isAssignableFrom(PaneControl.class));
        }

        @Test
        @DisplayName("ScrollBarControl 继承 Control")
        void testScrollBarInheritance() {
            assertTrue(Control.class.isAssignableFrom(ScrollBarControl.class));
        }

        @Test
        @DisplayName("TabControl 继承 Control")
        void testTabInheritance() {
            assertTrue(Control.class.isAssignableFrom(TabControl.class));
        }

        @Test
        @DisplayName("TextControl 继承 Control")
        void testTextInheritance() {
            assertTrue(Control.class.isAssignableFrom(TextControl.class));
        }

        @Test
        @DisplayName("TitleBarControl 继承 Control")
        void testTitleBarInheritance() {
            assertTrue(Control.class.isAssignableFrom(TitleBarControl.class));
        }

        @Test
        @DisplayName("TreeControl 继承 Control")
        void testTreeInheritance() {
            assertTrue(Control.class.isAssignableFrom(TreeControl.class));
        }

        @Test
        @DisplayName("TreeItemControl 继承 Control")
        void testTreeItemInheritance() {
            assertTrue(Control.class.isAssignableFrom(TreeItemControl.class));
        }

        @Test
        @DisplayName("WindowControl 继承 Control")
        void testWindowInheritance() {
            assertTrue(Control.class.isAssignableFrom(WindowControl.class));
        }

        @Test
        @DisplayName("Control 是抽象类")
        void testControlIsAbstract() {
            assertTrue(Modifier.isAbstract(Control.class.getModifiers()));
        }
    }

    @Nested
    @DisplayName("所有控件有无参构造方法")
    class ConstructorTest {

        @Test
        @DisplayName("ButtonControl 有无参和 SearchCondition 构造方法")
        void testButtonConstructors() throws Exception {
            assertNotNull(ButtonControl.class.getDeclaredConstructor());
            assertNotNull(ButtonControl.class.getDeclaredConstructor(SearchCondition.class));
        }

        @Test
        @DisplayName("CheckBoxControl 有无参和 SearchCondition 构造方法")
        void testCheckBoxConstructors() throws Exception {
            assertNotNull(CheckBoxControl.class.getDeclaredConstructor());
            assertNotNull(CheckBoxControl.class.getDeclaredConstructor(SearchCondition.class));
        }

        @Test
        @DisplayName("EditControl 有无参和 SearchCondition 构造方法")
        void testEditConstructors() throws Exception {
            assertNotNull(EditControl.class.getDeclaredConstructor());
            assertNotNull(EditControl.class.getDeclaredConstructor(SearchCondition.class));
        }

        @Test
        @DisplayName("ListControl 有无参和 SearchCondition 构造方法")
        void testListConstructors() throws Exception {
            assertNotNull(ListControl.class.getDeclaredConstructor());
            assertNotNull(ListControl.class.getDeclaredConstructor(SearchCondition.class));
        }

        @Test
        @DisplayName("ListItemControl 有无参和 SearchCondition 构造方法")
        void testListItemConstructors() throws Exception {
            assertNotNull(ListItemControl.class.getDeclaredConstructor());
            assertNotNull(ListItemControl.class.getDeclaredConstructor(SearchCondition.class));
        }

        @Test
        @DisplayName("MenuBarControl 有无参和 SearchCondition 构造方法")
        void testMenuBarConstructors() throws Exception {
            assertNotNull(MenuBarControl.class.getDeclaredConstructor());
            assertNotNull(MenuBarControl.class.getDeclaredConstructor(SearchCondition.class));
        }

        @Test
        @DisplayName("MenuItemControl 有无参和 SearchCondition 构造方法")
        void testMenuItemConstructors() throws Exception {
            assertNotNull(MenuItemControl.class.getDeclaredConstructor());
            assertNotNull(MenuItemControl.class.getDeclaredConstructor(SearchCondition.class));
        }

        @Test
        @DisplayName("PaneControl 有无参和 SearchCondition 构造方法")
        void testPaneConstructors() throws Exception {
            assertNotNull(PaneControl.class.getDeclaredConstructor());
            assertNotNull(PaneControl.class.getDeclaredConstructor(SearchCondition.class));
        }

        @Test
        @DisplayName("ScrollBarControl 有无参和 SearchCondition 构造方法")
        void testScrollBarConstructors() throws Exception {
            assertNotNull(ScrollBarControl.class.getDeclaredConstructor());
            assertNotNull(ScrollBarControl.class.getDeclaredConstructor(SearchCondition.class));
        }

        @Test
        @DisplayName("TabControl 有无参和 SearchCondition 构造方法")
        void testTabConstructors() throws Exception {
            assertNotNull(TabControl.class.getDeclaredConstructor());
            assertNotNull(TabControl.class.getDeclaredConstructor(SearchCondition.class));
        }

        @Test
        @DisplayName("TextControl 有无参和 SearchCondition 构造方法")
        void testTextConstructors() throws Exception {
            assertNotNull(TextControl.class.getDeclaredConstructor());
            assertNotNull(TextControl.class.getDeclaredConstructor(SearchCondition.class));
        }

        @Test
        @DisplayName("TitleBarControl 有无参和 SearchCondition 构造方法")
        void testTitleBarConstructors() throws Exception {
            assertNotNull(TitleBarControl.class.getDeclaredConstructor());
            assertNotNull(TitleBarControl.class.getDeclaredConstructor(SearchCondition.class));
        }

        @Test
        @DisplayName("TreeControl 有无参和 SearchCondition 构造方法")
        void testTreeConstructors() throws Exception {
            assertNotNull(TreeControl.class.getDeclaredConstructor());
            assertNotNull(TreeControl.class.getDeclaredConstructor(SearchCondition.class));
        }

        @Test
        @DisplayName("TreeItemControl 有无参和 SearchCondition 构造方法")
        void testTreeItemConstructors() throws Exception {
            assertNotNull(TreeItemControl.class.getDeclaredConstructor());
            assertNotNull(TreeItemControl.class.getDeclaredConstructor(SearchCondition.class));
        }

        @Test
        @DisplayName("WindowControl 有无参和 SearchCondition 构造方法")
        void testWindowConstructors() throws Exception {
            assertNotNull(WindowControl.class.getDeclaredConstructor());
            assertNotNull(WindowControl.class.getDeclaredConstructor(SearchCondition.class));
        }
    }

    @Nested
    @DisplayName("所有控件可通过无参构造实例化")
    class InstantiationTest {

        @Test
        @DisplayName("所有控件类型可实例化")
        void testAllControlsInstantiable() {
            assertDoesNotThrow((Executable) ButtonControl::new);
            assertDoesNotThrow((Executable) CheckBoxControl::new);
            assertDoesNotThrow((Executable) EditControl::new);
            assertDoesNotThrow((Executable) ListControl::new);
            assertDoesNotThrow((Executable) ListItemControl::new);
            assertDoesNotThrow((Executable) MenuBarControl::new);
            assertDoesNotThrow((Executable) MenuItemControl::new);
            assertDoesNotThrow((Executable) PaneControl::new);
            assertDoesNotThrow((Executable) ScrollBarControl::new);
            assertDoesNotThrow((Executable) TabControl::new);
            assertDoesNotThrow((Executable) TextControl::new);
            assertDoesNotThrow((Executable) TitleBarControl::new);
            assertDoesNotThrow((Executable) TreeControl::new);
            assertDoesNotThrow((Executable) TreeItemControl::new);
            assertDoesNotThrow((Executable) WindowControl::new);
        }

        @Test
        @DisplayName("通过 SearchCondition 构造的控件应保存条件引用")
        void testSearchConditionPreserved() {
            SearchCondition cond = SearchCondition.builder()
                    .name("test")
                    .controlType(ControlType.Button)
                    .build();
            ButtonControl btn = new ButtonControl(cond);
            assertSame(cond, btn.getSearchCondition());
        }

        @Test
        @DisplayName("新实例的 elementFound 应为 false")
        void testNewInstanceNotElementFound() {
            ButtonControl btn = new ButtonControl();
            assertFalse(btn.isElementFound());
        }

        @Test
        @DisplayName("新实例的 nativeElement 应为 null")
        void testNewInstanceNullElement() {
            ButtonControl btn = new ButtonControl();
            assertNull(btn.getNativeElement());
        }

        @Test
        @DisplayName("setNativeElement/getNativeElement 一致")
        void testSetGetNativeElement() {
            ButtonControl btn = new ButtonControl();
            Object mock = new Object();
            btn.setNativeElement(mock);
            assertSame(mock, btn.getNativeElement());
        }

        @Test
        @DisplayName("setElementFound/isElementFound 一致")
        void testSetGetElementFound() {
            ButtonControl btn = new ButtonControl();
            btn.setElementFound(true);
            assertTrue(btn.isElementFound());
            btn.setElementFound(false);
            assertFalse(btn.isElementFound());
        }
    }

    @Nested
    @DisplayName("控件特有方法测试")
    class SpecialMethodsTest {

        @Test
        @DisplayName("WindowControl 应有 close/maximize/minimize/restore/setTopmost 方法")
        void testWindowControlMethods() throws Exception {
            assertNotNull(WindowControl.class.getMethod("close"));
            assertNotNull(WindowControl.class.getMethod("maximize"));
            assertNotNull(WindowControl.class.getMethod("minimize"));
            assertNotNull(WindowControl.class.getMethod("restore"));
            assertNotNull(WindowControl.class.getMethod("setTopmost", boolean.class));
        }

        @Test
        @DisplayName("EditControl 应有 getValue/setValue 方法")
        void testEditControlMethods() throws Exception {
            assertNotNull(EditControl.class.getMethod("getValue"));
            assertNotNull(EditControl.class.getMethod("setValue", String.class));
        }

        @Test
        @DisplayName("WindowControl 方法数量为 5 个自有方法（不含继承）")
        void testWindowControlOwnMethodCount() {
            // WindowControl 自有方法: setTopmost, close, maximize, minimize, restore
            long ownMethods = 0;
            for (var m : WindowControl.class.getDeclaredMethods()) {
                if (Modifier.isPublic(m.getModifiers()) && !m.isSynthetic()) {
                    ownMethods++;
                }
            }
            assertEquals(5, ownMethods, "WindowControl 应有 5 个自有 public 方法");
        }

        @Test
        @DisplayName("EditControl 方法数量为 2 个自有方法（不含继承）")
        void testEditControlOwnMethodCount() {
            long ownMethods = 0;
            for (var m : EditControl.class.getDeclaredMethods()) {
                if (Modifier.isPublic(m.getModifiers()) && !m.isSynthetic()) {
                    ownMethods++;
                }
            }
            assertEquals(2, ownMethods, "EditControl 应有 2 个自有 public 方法");
        }

        @Test
        @DisplayName("ButtonControl 无自有方法（全部继承自 Control）")
        void testButtonControlNoOwnMethods() {
            long ownMethods = 0;
            for (var m : ButtonControl.class.getDeclaredMethods()) {
                if (Modifier.isPublic(m.getModifiers()) && !m.isSynthetic()) {
                    ownMethods++;
                }
            }
            assertEquals(0, ownMethods, "ButtonControl 不应有自有 public 方法");
        }
    }

    @Nested
    @DisplayName("控件类型数量完整性")
    class CompletenessTest {

        @Test
        @DisplayName("core 模块应有 15 种具体控件类型 + 1 个抽象基类")
        void testControlTypeCount() {
            Class<?>[] controlClasses = {
                    ButtonControl.class, CheckBoxControl.class, EditControl.class,
                    ListControl.class, ListItemControl.class, MenuBarControl.class,
                    MenuItemControl.class, PaneControl.class, ScrollBarControl.class,
                    TabControl.class, TextControl.class, TitleBarControl.class,
                    TreeControl.class, TreeItemControl.class, WindowControl.class
            };
            assertEquals(15, controlClasses.length, "应有 15 种具体控件类型");
            for (Class<?> clazz : controlClasses) {
                assertFalse(Modifier.isAbstract(clazz.getModifiers()),
                        clazz.getSimpleName() + " 不应是抽象类");
            }
        }

        @Test
        @DisplayName("所有控件类型都在 control 包下")
        void testAllInControlPackage() {
            Class<?>[] controlClasses = {
                    ButtonControl.class, CheckBoxControl.class, EditControl.class,
                    ListControl.class, ListItemControl.class, MenuBarControl.class,
                    MenuItemControl.class, PaneControl.class, ScrollBarControl.class,
                    TabControl.class, TextControl.class, TitleBarControl.class,
                    TreeControl.class, TreeItemControl.class, WindowControl.class
            };
            for (Class<?> clazz : controlClasses) {
                assertEquals("io.getbit.uiautomation.control", clazz.getPackage().getName(),
                        clazz.getSimpleName() + " 应在 control 包下");
            }
        }
    }
}
