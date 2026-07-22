package io.getbit.uiautomation.control;

import io.getbit.uiautomation.condition.SearchCondition;
import io.getbit.uiautomation.enums.ControlType;

/**
 * 标题栏控件
 *
 * <p>对应 UIAutomation 的 TitleBar 控件类型 (ControlType=50037)。
 * 通常作为 {@link WindowControl} 的子元素出现。</p>
 */
public class TitleBarControl extends Control {

    public TitleBarControl() {
        super();
    }

    public TitleBarControl(SearchCondition searchCondition) {
        super(searchCondition);
    }

}
