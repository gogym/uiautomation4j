package com.gettyio.uiautomation.win;

import com.gettyio.uiautomation.condition.SearchCondition;
import com.gettyio.uiautomation.control.*;
import com.gettyio.uiautomation.enums.ControlType;
import com.gettyio.uiautomation.win.com.IUIAutomationElement;

/**
 * 控件工厂
 * 根据 COM Element 的 ControlType 创建对应的 Java Control 子类
 */
public class WinControlFactory {

    /**
     * 根据 COM Element 创建对应的 Control 子类
     *
     * @param element   COM Element
     * @param condition 搜索条件
     * @return 对应的 Control 子类实例
     */
    public static Control createControl(IUIAutomationElement element, SearchCondition condition) {
        int controlTypeId = element.getControlType();
        ControlType type;
        try {
            type = ControlType.fromId(controlTypeId);
        } catch (IllegalArgumentException e) {
            // 未知类型，使用 PaneControl 作为默认
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
