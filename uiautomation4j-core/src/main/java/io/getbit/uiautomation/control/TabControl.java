package io.getbit.uiautomation.control;

import io.getbit.uiautomation.condition.SearchCondition;
import io.getbit.uiautomation.enums.ControlType;

/**
 * Tab 控件
 *
 * <p>对应 UIAutomation 的 Tab 控件类型 (ControlType=50018)。
 * 支持 {@link SelectionPattern} 获取当前选中的 TabItem，
 * 支持 {@link ScrollPattern}（当 Tab 项过多时）。</p>
 */
public class TabControl extends Control {

    public TabControl() {
        super();
    }

    public TabControl(SearchCondition searchCondition) {
        super(searchCondition);
    }

    @Override
    public ControlType getControlType() {
        return ControlType.Tab;
    }
}
