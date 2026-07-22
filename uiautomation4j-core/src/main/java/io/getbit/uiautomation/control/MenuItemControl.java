package io.getbit.uiautomation.control;

import io.getbit.uiautomation.condition.SearchCondition;
import io.getbit.uiautomation.enums.ControlType;

/**
 * 菜单项控件
 *
 * <p>对应 UIAutomation 的 MenuItem 控件类型 (ControlType=50011)。
 * 支持 {@link InvokePattern} 触发菜单项操作。</p>
 */
public class MenuItemControl extends Control {

    public MenuItemControl() {
        super();
    }

    public MenuItemControl(SearchCondition searchCondition) {
        super(searchCondition);
    }

}
