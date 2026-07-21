package io.getbit.uiautomation.linux;

import io.getbit.uiautomation.condition.SearchCondition;
import io.getbit.uiautomation.control.*;
import io.getbit.uiautomation.enums.ControlType;
import io.getbit.uiautomation.linux.ax.AtspiElement;

/**
 * Linux 平台的控件工厂
 *
 * <p>根据 AT-SPI2 Role 将 AtspiElement 包装为对应的 Control 子类。
 * 等价于 macOS 平台的 {@code MacControlFactory}。</p>
 *
 * <h3>AT-SPI Role → Control 映射：</h3>
 * <table>
 *   <tr><th>AT-SPI Role</th><th>ControlType</th><th>Control 子类</th></tr>
 *   <tr><td>ROLE_FRAME (27)</td><td>Window</td><td>WindowControl</td></tr>
 *   <tr><td>ROLE_PUSH_BUTTON (74)</td><td>Button</td><td>ButtonControl</td></tr>
 *   <tr><td>ROLE_TEXT (66)</td><td>Edit</td><td>EditControl</td></tr>
 *   <tr><td>ROLE_LABEL (33)</td><td>Text</td><td>TextControl</td></tr>
 *   <tr><td>ROLE_CHECK_BOX (11)</td><td>CheckBox</td><td>CheckBoxControl</td></tr>
 *   <tr><td>ROLE_LIST (35)</td><td>List</td><td>ListControl</td></tr>
 *   <tr><td>ROLE_MENU_BAR (38)</td><td>MenuBar</td><td>MenuBarControl</td></tr>
 *   <tr><td>ROLE_MENU_ITEM (39)</td><td>MenuItem</td><td>MenuItemControl</td></tr>
 * </table>
 */
public class LinuxControlFactory {

    /**
     * 根据 AtspiElement 的角色创建对应的 Control 子类
     *
     * @param element         AtspiElement 包装对象
     * @param searchCondition 搜索条件
     * @return 对应的 Control 子类实例
     */
    public static Control createControl(AtspiElement element, SearchCondition searchCondition) {
        int role = element.getRole();
        ControlType controlType = ControlType.fromAtspiRole(role);

        Control control = createControlByType(controlType, searchCondition);
        control.setNativeElement(element);
        control.setElementFound(true);
        return control;
    }

    /**
     * 根据 ControlType 创建对应的 Control 子类
     *
     * @param type            控件类型
     * @param searchCondition 搜索条件
     * @return Control 子类实例
     */
    private static Control createControlByType(ControlType type, SearchCondition searchCondition) {
        switch (type) {
            case Window:
                return new WindowControl(searchCondition);
            case Button:
                return new ButtonControl(searchCondition);
            case Edit:
            case ComboBox:
                return new EditControl(searchCondition);
            case Text:
                return new TextControl(searchCondition);
            case CheckBox:
            case RadioButton:
                return new CheckBoxControl(searchCondition);
            case List:
            case Table:
                return new ListControl(searchCondition);
            case ListItem:
            case TableCell:
                return new ListItemControl(searchCondition);
            case Menu:
            case PopupMenu:
                return new MenuItemControl(searchCondition);
            case MenuBar:
                return new MenuBarControl(searchCondition);
            case MenuItem:
                return new MenuItemControl(searchCondition);
            case ScrollBar:
                return new ScrollBarControl(searchCondition);
            case Tab:
            case TabItem:
                return new TabControl(searchCondition);
            case Tree:
                return new TreeControl(searchCondition);
            case TreeItem:
                return new TreeItemControl(searchCondition);
            case Pane:
            case Group:
            case Dialog:
                return new PaneControl(searchCondition);
            case Hyperlink:
            case Image:
            case Icon:
            default:
                return new PaneControl(searchCondition);
        }
    }

    private LinuxControlFactory() {
        // 工具类禁止实例化
    }
}
