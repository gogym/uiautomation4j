package com.gettyio.uiautomation.control;

import com.gettyio.uiautomation.condition.SearchCondition;
import com.gettyio.uiautomation.enums.ControlType;

/**
 * 菜单栏控件
 *
 * <p>对应 UIAutomation 的 MenuBar 控件类型 (ControlType=50010)。
 * 通常包含多个 {@link MenuItemControl} 子元素。</p>
 */
public class MenuBarControl extends Control {

    public MenuBarControl() {
        super();
    }

    public MenuBarControl(SearchCondition searchCondition) {
        super(searchCondition);
    }

    @Override
    public ControlType getControlType() {
        return ControlType.MenuBar;
    }
}
