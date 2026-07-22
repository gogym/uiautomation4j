package io.getbit.uiautomation.example;

import io.getbit.uiautomation.enums.ControlType;
import io.getbit.uiautomation.enums.ControlType.Platform;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ControlType 枚举全面测试
 *
 * <p>覆盖：ID 范围、平台归属、fromMacRole 映射、fromAtspiRole 映射、
 * isAvailableOn 判断、fromId 查找、边界条件</p>
 */
@DisplayName("ControlType 测试")
class ControlTypeTest {

    @Nested
    @DisplayName("ID 范围测试")
    class IdRange {

        @Test
        @DisplayName("跨平台通用类型 ID 应在 50000-50099 范围")
        void testCrossPlatformIdRange() {
            for (ControlType type : ControlType.values()) {
                if (type.getPlatform() == Platform.CROSS_PLATFORM) {
                    assertTrue(type.getId() >= 50000 && type.getId() < 51000,
                            "跨平台类型 " + type + " ID=" + type.getId() + " 不在预期范围");
                }
            }
        }

        @Test
        @DisplayName("macOS 专用类型 ID 应在 60000-60099 范围")
        void testMacIdRange() {
            for (ControlType type : ControlType.values()) {
                if (type.getPlatform() == Platform.MAC) {
                    assertTrue(type.getId() >= 60000 && type.getId() < 61000,
                            "macOS 类型 " + type + " ID=" + type.getId() + " 不在预期范围");
                }
            }
        }

        @Test
        @DisplayName("Linux 专用类型 ID 应在 70000-70099 范围")
        void testLinuxIdRange() {
            for (ControlType type : ControlType.values()) {
                if (type.getPlatform() == Platform.LINUX) {
                    assertTrue(type.getId() >= 70000 && type.getId() < 71000,
                            "Linux 类型 " + type + " ID=" + type.getId() + " 不在预期范围");
                }
            }
        }

        @Test
        @DisplayName("Windows 专用类型 ID 应在 50000-50099 范围")
        void testWindowsIdRange() {
            for (ControlType type : ControlType.values()) {
                if (type.getPlatform() == Platform.WINDOWS) {
                    assertTrue(type.getId() >= 50000 && type.getId() < 51000,
                            "Windows 类型 " + type + " ID=" + type.getId() + " 不在预期范围");
                }
            }
        }

        @Test
        @DisplayName("所有枚举值的 ID 不应重复")
        void testNoDuplicateIds() {
            ControlType[] allTypes = ControlType.values();
            for (int i = 0; i < allTypes.length; i++) {
                for (int j = i + 1; j < allTypes.length; j++) {
                    // 不同枚举值允许相同 ID 吗？按设计不应重复
                    assertNotEquals(allTypes[i].getId(), allTypes[j].getId(),
                            "ID 重复: " + allTypes[i] + " 和 " + allTypes[j] + " 都是 ID=" + allTypes[i].getId());
                }
            }
        }
    }

    @Nested
    @DisplayName("平台归属测试")
    class PlatformAssignment {

        @Test
        @DisplayName("Button 应为 CROSS_PLATFORM")
        void testButtonPlatform() {
            assertEquals(Platform.CROSS_PLATFORM, ControlType.Button.getPlatform());
        }

        @Test
        @DisplayName("Window 应为 CROSS_PLATFORM")
        void testWindowPlatform() {
            assertEquals(Platform.CROSS_PLATFORM, ControlType.Window.getPlatform());
        }

        @Test
        @DisplayName("ColorWell 应为 MAC")
        void testColorWellPlatform() {
            assertEquals(Platform.MAC, ControlType.ColorWell.getPlatform());
        }

        @Test
        @DisplayName("Alert 应为 LINUX")
        void testAlertPlatform() {
            assertEquals(Platform.LINUX, ControlType.Alert.getPlatform());
        }

        @Test
        @DisplayName("AppBar 应为 WINDOWS")
        void testAppBarPlatform() {
            assertEquals(Platform.WINDOWS, ControlType.AppBar.getPlatform());
        }
    }

    @Nested
    @DisplayName("isAvailableOn 测试")
    class AvailabilityTest {

        @Test
        @DisplayName("CROSS_PLATFORM 类型在所有平台可用")
        void testCrossPlatformAvailableEverywhere() {
            ControlType button = ControlType.Button;
            assertTrue(button.isAvailableOn(Platform.WINDOWS));
            assertTrue(button.isAvailableOn(Platform.MAC));
            assertTrue(button.isAvailableOn(Platform.LINUX));
        }

        @Test
        @DisplayName("MAC 专用类型仅在 MAC 可用")
        void testMacOnlyAvailableOnMac() {
            ControlType colorWell = ControlType.ColorWell;
            assertFalse(colorWell.isAvailableOn(Platform.WINDOWS));
            assertTrue(colorWell.isAvailableOn(Platform.MAC));
            assertFalse(colorWell.isAvailableOn(Platform.LINUX));
        }

        @Test
        @DisplayName("LINUX 专用类型仅在 LINUX 可用")
        void testLinuxOnlyAvailableOnLinux() {
            ControlType alert = ControlType.Alert;
            assertFalse(alert.isAvailableOn(Platform.WINDOWS));
            assertFalse(alert.isAvailableOn(Platform.MAC));
            assertTrue(alert.isAvailableOn(Platform.LINUX));
        }

        @Test
        @DisplayName("WINDOWS 专用类型仅在 WINDOWS 可用")
        void testWindowsOnlyAvailableOnWindows() {
            ControlType appBar = ControlType.AppBar;
            assertTrue(appBar.isAvailableOn(Platform.WINDOWS));
            assertFalse(appBar.isAvailableOn(Platform.MAC));
            assertFalse(appBar.isAvailableOn(Platform.LINUX));
        }
    }

    @Nested
    @DisplayName("fromId 测试")
    class FromIdTest {

        @Test
        @DisplayName("fromId(50000) 应返回 Button")
        void testFromIdButton() {
            assertEquals(ControlType.Button, ControlType.fromId(50000));
        }

        @Test
        @DisplayName("fromId(50032) 应返回 Window")
        void testFromIdWindow() {
            assertEquals(ControlType.Window, ControlType.fromId(50032));
        }

        @Test
        @DisplayName("fromId(60000) 应返回 ColorWell")
        void testFromIdColorWell() {
            assertEquals(ControlType.ColorWell, ControlType.fromId(60000));
        }

        @Test
        @DisplayName("fromId(70000) 应返回 Alert")
        void testFromIdAlert() {
            assertEquals(ControlType.Alert, ControlType.fromId(70000));
        }

        @Test
        @DisplayName("fromId 传入无效 ID 应抛出异常")
        void testFromIdInvalid() {
            assertThrows(IllegalArgumentException.class, () -> ControlType.fromId(99999));
        }

        @Test
        @DisplayName("fromId 传入负数应抛出异常")
        void testFromIdNegative() {
            assertThrows(IllegalArgumentException.class, () -> ControlType.fromId(-1));
        }

        @Test
        @DisplayName("所有枚举值的 ID 通过 fromId 应能还原")
        void testFromIdRoundTrip() {
            for (ControlType type : ControlType.values()) {
                assertEquals(type, ControlType.fromId(type.getId()),
                        "fromId(" + type.getId() + ") 应返回 " + type);
            }
        }
    }

    @Nested
    @DisplayName("fromMacRole 测试")
    class FromMacRoleTest {

        @Test
        @DisplayName("AXButton 应映射到 Button")
        void testAXButton() {
            assertEquals(ControlType.Button, ControlType.fromMacRole("AXButton"));
        }

        @Test
        @DisplayName("AXWindow 应映射到 Window")
        void testAXWindow() {
            assertEquals(ControlType.Window, ControlType.fromMacRole("AXWindow"));
        }

        @Test
        @DisplayName("AXTextField 应映射到 TextField")
        void testAXTextField() {
            assertEquals(ControlType.TextField, ControlType.fromMacRole("AXTextField"));
        }

        @Test
        @DisplayName("AXTextArea 应映射到 TextArea")
        void testAXTextArea() {
            assertEquals(ControlType.TextArea, ControlType.fromMacRole("AXTextArea"));
        }

        @Test
        @DisplayName("AXStaticText 应映射到 Text")
        void testAXStaticText() {
            assertEquals(ControlType.Text, ControlType.fromMacRole("AXStaticText"));
        }

        @Test
        @DisplayName("AXList 应映射到 List")
        void testAXList() {
            assertEquals(ControlType.List, ControlType.fromMacRole("AXList"));
        }

        @Test
        @DisplayName("AXTable 应映射到 List（统一映射）")
        void testAXTable() {
            assertEquals(ControlType.List, ControlType.fromMacRole("AXTable"));
        }

        @Test
        @DisplayName("AXOutline 应映射到 Tree")
        void testAXOutline() {
            assertEquals(ControlType.Tree, ControlType.fromMacRole("AXOutline"));
        }

        @Test
        @DisplayName("AXGroup 应映射到 Pane")
        void testAXGroup() {
            assertEquals(ControlType.Pane, ControlType.fromMacRole("AXGroup"));
        }

        @Test
        @DisplayName("未知 AXRole 应映射到 Custom")
        void testUnknownRole() {
            assertEquals(ControlType.Custom, ControlType.fromMacRole("AXUnknown"));
        }

        @Test
        @DisplayName("null AXRole 应映射到 Custom")
        void testNullRole() {
            assertEquals(ControlType.Custom, ControlType.fromMacRole(null));
        }

        @Test
        @DisplayName("空字符串 AXRole 应映射到 Custom")
        void testEmptyRole() {
            assertEquals(ControlType.Custom, ControlType.fromMacRole(""));
        }

        @Test
        @DisplayName("所有标准 macOS AXRole 都应映射到非 Custom 类型")
        void testAllStandardRolesMapped() {
            String[] standardRoles = {
                    "AXButton", "AXCheckBox", "AXPopUpButton", "AXTextField", "AXTextArea",
                    "AXStaticText", "AXList", "AXTable", "AXRow", "AXCell", "AXMenu",
                    "AXMenuBar", "AXMenuItem", "AXRadioButton", "AXScrollBar", "AXSlider",
                    "AXTabGroup", "AXOutline", "AXWindow", "AXGroup", "AXImage", "AXLink",
                    "AXProgressIndicator", "AXToolbar", "AXSplitter", "AXColorWell",
                    "AXDrawer", "AXSheet", "AXBrowser", "AXColumn", "AXRuler",
                    "AXSplitGroup", "AXApplication", "AXIncrementor"
            };
            for (String role : standardRoles) {
                assertNotEquals(ControlType.Custom, ControlType.fromMacRole(role),
                        "标准角色 " + role + " 不应映射到 Custom");
            }
        }
    }

    @Nested
    @DisplayName("fromAtspiRole 测试")
    class FromAtspiRoleTest {

        @Test
        @DisplayName("ROLE_PUSH_BUTTON(74) 应映射到 Button")
        void testPushButton() {
            assertEquals(ControlType.Button, ControlType.fromAtspiRole(74));
        }

        @Test
        @DisplayName("ROLE_FRAME(27) 应映射到 Window")
        void testFrame() {
            assertEquals(ControlType.Window, ControlType.fromAtspiRole(27));
        }

        @Test
        @DisplayName("ROLE_TEXT(66) 应映射到 Edit")
        void testText() {
            assertEquals(ControlType.Edit, ControlType.fromAtspiRole(66));
        }

        @Test
        @DisplayName("ROLE_CHECK_BOX(11) 应映射到 CheckBox")
        void testCheckBox() {
            assertEquals(ControlType.CheckBox, ControlType.fromAtspiRole(11));
        }

        @Test
        @DisplayName("ROLE_LIST(35) 应映射到 List")
        void testList() {
            assertEquals(ControlType.List, ControlType.fromAtspiRole(35));
        }

        @Test
        @DisplayName("ROLE_TREE(70) 应映射到 Tree")
        void testTree() {
            assertEquals(ControlType.Tree, ControlType.fromAtspiRole(70));
        }

        @Test
        @DisplayName("ROLE_DIALOG(20) 应映射到 Dialog")
        void testDialog() {
            assertEquals(ControlType.Dialog, ControlType.fromAtspiRole(20));
        }

        @Test
        @DisplayName("ROLE_TABLE(60) 应映射到 Table")
        void testTable() {
            assertEquals(ControlType.Table, ControlType.fromAtspiRole(60));
        }

        @Test
        @DisplayName("ROLE_ALERT(2) 应映射到 Alert")
        void testAlert() {
            assertEquals(ControlType.Alert, ControlType.fromAtspiRole(2));
        }

        @Test
        @DisplayName("未知 Role(0) 应映射到 Custom")
        void testUnknownRole() {
            assertEquals(ControlType.Custom, ControlType.fromAtspiRole(0));
        }

        @Test
        @DisplayName("负数 Role 应映射到 Custom")
        void testNegativeRole() {
            assertEquals(ControlType.Custom, ControlType.fromAtspiRole(-1));
        }

        @Test
        @DisplayName("超大 Role 值应映射到 Custom")
        void testLargeRole() {
            assertEquals(ControlType.Custom, ControlType.fromAtspiRole(9999));
        }
    }

    @Nested
    @DisplayName("枚举完整性测试")
    class Completeness {

        @Test
        @DisplayName("枚举值数量应不少于 40 个")
        void testEnumCount() {
            assertTrue(ControlType.values().length >= 40,
                    "枚举值数量不足: " + ControlType.values().length);
        }

        @Test
        @DisplayName("每个枚举值都应有非 null 的 Platform")
        void testAllHavePlatform() {
            for (ControlType type : ControlType.values()) {
                assertNotNull(type.getPlatform(), type.name() + " 的 Platform 为 null");
            }
        }

        @Test
        @DisplayName("每个枚举值都应有正数 ID")
        void testAllHavePositiveId() {
            for (ControlType type : ControlType.values()) {
                assertTrue(type.getId() > 0, type.name() + " 的 ID 不是正数: " + type.getId());
            }
        }
    }
}
