package io.getbit.uiautomation.win;

import io.getbit.uiautomation.condition.SearchCondition;
import io.getbit.uiautomation.control.*;
import io.getbit.uiautomation.enums.ControlType;
import io.getbit.uiautomation.win.com.IUIAutomationElement;

/**
 * 控件工厂
 *
 * <p>根据 COM Element 的 ControlType 创建对应的 Java Control 子类。
 * 这是 core 模块与 win 模块之间的桥接点：
 * core 定义抽象的 Control 类，win 通过此工厂创建具体实例。</p>
 *
 * <p>映射关系：</p>
 * <ul>
 *   <li>Window(50032) → {@link WindowControl}</li>
 *   <li>Button(50000) → {@link ButtonControl}</li>
 *   <li>Edit(50004) → {@link EditControl}</li>
 *   <li>Text(50024) → {@link TextControl}</li>
 *   <li>List(50008) → {@link ListControl}</li>
 *   <li>ListItem(50007) → {@link ListItemControl}</li>
 *   <li>MenuItem(50011) → {@link MenuItemControl}</li>
 *   <li>CheckBox(50002) → {@link CheckBoxControl}</li>
 *   <li>Pane(50033) → {@link PaneControl}（默认回退类型）</li>
 *   <li>MenuBar(50010) → {@link MenuBarControl}</li>
 *   <li>TitleBar(50037) → {@link TitleBarControl}</li>
 *   <li>ScrollBar(50014) → {@link ScrollBarControl}</li>
 *   <li>Tab(50018) → {@link TabControl}</li>
 *   <li>Tree(50023) → {@link TreeControl}</li>
 *   <li>TreeItem(50028) → {@link TreeItemControl}</li>
 * </ul>
 */
public class WinControlFactory {

    /**
     * 根据 COM Element 创建对应的 Control 子类
     * <p>通过 {@code IUIAutomationElement::get_CurrentControlType} 获取控件类型 ID，
     * 转换为 {@link ControlType} 枚举后创建对应的 Control 子类。</p>
     * <p>未知类型默认创建 {@link PaneControl}。</p>
     *
     * @param element   COM Element
     * @param condition 搜索条件
     * @return 对应的 Control 子类实例
     */
    public static Control createControl(IUIAutomationElement element, SearchCondition condition) {
        // 将 COM 返回的 int 类型 ControlType 转换为 Java 枚举
        int controlTypeId = element.getControlType();
        ControlType type;
        try {
            type = ControlType.fromId(controlTypeId);
        } catch (IllegalArgumentException e) {
            // 未知类型，使用 PaneControl 作为默认回退
            return new PaneControl(condition);
        }

        switch (type) {
            case Window:
                return new WindowControl(condition);
            case Button:
                return new ButtonControl(condition);
            case Edit:
                return new EditControl(condition);
            case Text:
                return new TextControl(condition);
            case List:
                return new ListControl(condition);
            case ListItem:
                return new ListItemControl(condition);
            case MenuItem:
                return new MenuItemControl(condition);
            case CheckBox:
                return new CheckBoxControl(condition);
            case Pane:
                return new PaneControl(condition);
            case MenuBar:
                return new MenuBarControl(condition);
            case TitleBar:
                return new TitleBarControl(condition);
            case ScrollBar:
                return new ScrollBarControl(condition);
            case Tab:
                return new TabControl(condition);
            case Tree:
                return new TreeControl(condition);
            case TreeItem:
                return new TreeItemControl(condition);
            default:
                return new PaneControl(condition);
        }
    }
}
