package io.getbit.uiautomation.control;

import io.getbit.uiautomation.condition.SearchCondition;
import io.getbit.uiautomation.enums.ControlType;

/**
 * Tree 控件
 *
 * <p>对应 UIAutomation 的 Tree 控件类型 (ControlType=50023)。
 * 包含多个 {@link TreeItemControl} 子元素，支持 {@link SelectionPattern} 和 {@link ScrollPattern}。</p>
 */
public class TreeControl extends Control {

    public TreeControl() {
        super();
    }

    public TreeControl(SearchCondition searchCondition) {
        super(searchCondition);
    }

    @Override
    public ControlType getControlType() {
        return ControlType.Tree;
    }
}
