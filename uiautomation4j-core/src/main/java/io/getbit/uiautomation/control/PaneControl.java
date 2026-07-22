package io.getbit.uiautomation.control;

import io.getbit.uiautomation.condition.SearchCondition;
import io.getbit.uiautomation.enums.ControlType;

/**
 * 面板控件
 *
 * <p>对应 UIAutomation 的 Pane 控件类型 (ControlType=50033)。
 * 通用容器控件，常用于分组其他控件。未知控件类型也会默认使用 PaneControl。</p>
 */
public class PaneControl extends Control {

    public PaneControl() {
        super();
    }

    public PaneControl(SearchCondition searchCondition) {
        super(searchCondition);
    }

}
