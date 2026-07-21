package com.gettyio.uiautomation.mac;

import com.gettyio.uiautomation.condition.SearchCondition;
import com.gettyio.uiautomation.control.*;
import com.gettyio.uiautomation.enums.ControlType;
import com.gettyio.uiautomation.mac.ax.AXUIElement;

/**
 * macOS 平台的控件工厂
 *
 * <p>根据 AXRole 将 AXUIElement 包装为对应的 Control 子类。
 * 等价于 Windows 平台的 {@code WinControlFactory}。</p>
 *
 * <h3>AXRole → Control 映射表：</h3>
 * <table>
 *   <tr><th>AXRole</th><th>ControlType</th><th>Control 子类</th></tr>
 *   <tr><td>AXWindow</td><td>Window</td><td>WindowControl</td></tr>
 *   <tr><td>AXButton</td><td>Button</td><td>ButtonControl</td></tr>
 *   <tr><td>AXTextField</td><td>TextField</td><td>EditControl</td></tr>
 *   <tr><td>AXStaticText</td><td>Text</td><td>TextControl</td></tr>
 *   <tr><td>AXCheckBox</td><td>CheckBox</td><td>CheckBoxControl</td></tr>
 *   <tr><td>AXList/AXTable</td><td>List</td><td>ListControl</td></tr>
 *   <tr><td>AXRow/AXCell</td><td>ListItem</td><td>ListItemControl</td></tr>
 *   <tr><td>AXGroup</td><td>Pane</td><td>PaneControl</td></tr>
 *   <tr><td>AXMenuBar</td><td>MenuBar</td><td>MenuBarControl</td></tr>
 *   <tr><td>AXMenuItem</td><td>MenuItem</td><td>MenuItemControl</td></tr>
 *   <tr><td>AXScrollBar</td><td>ScrollBar</td><td>ScrollBarControl</td></tr>
 *   <tr><td>AXTabGroup</td><td>Tab</td><td>TabControl</td></tr>
 *   <tr><td>AXOutline</td><td>Tree</td><td>TreeControl</td></tr>
 * </table>
 *
 * @see com.gettyio.uiautomation.win.WinControlFactory
 */
public class MacControlFactory {

    /**
     * 根据 AXUIElement 的角色创建对应的 Control 子类
     *
     * <p>流程：
     * <ol>
     *   <li>从 AXUIElement 获取 AXRole</li>
     *   <li>通过 {@link ControlType#fromMacRole(String)} 映射为 ControlType</li>
     *   <li>根据 ControlType 创建对应的 Control 子类</li>
     *   <li>设置 nativeElement 和 elementFound 标志</li>
     * </ol>
     *
     * @param element        AXUIElement 包装对象
     * @param searchCondition 搜索条件（用于 Control 的后续搜索）
     * @return 对应的 Control 子类实例
     */
    public static Control createControl(AXUIElement element, SearchCondition searchCondition) {
        String role = element.getRole();
        ControlType controlType = ControlType.fromMacRole(role);

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
            case TextField:
            case TextArea:
                return new EditControl(searchCondition);
            case Text:
                return new TextControl(searchCondition);
            case CheckBox:
                return new CheckBoxControl(searchCondition);
            case List:
                return new ListControl(searchCondition);
            case ListItem:
                return new ListItemControl(searchCondition);
            case Pane:
            case Group:
                return new PaneControl(searchCondition);
            case MenuBar:
                return new MenuBarControl(searchCondition);
            case MenuItem:
                return new MenuItemControl(searchCondition);
            case ScrollBar:
                return new ScrollBarControl(searchCondition);
            case Tab:
                return new TabControl(searchCondition);
            case Tree:
                return new TreeControl(searchCondition);
            case TreeItem:
                return new TreeItemControl(searchCondition);
            case PopUpButton:
                // macOS 特有：弹出按钮，映射为 ButtonControl
                return new ButtonControl(searchCondition);
            default:
                // 未知类型使用 PaneControl 作为通用容器
                return new PaneControl(searchCondition);
        }
    }

    private MacControlFactory() {
        // 工具类禁止实例化
    }
}
