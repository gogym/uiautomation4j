package io.getbit.uiautomation.control;

import io.getbit.uiautomation.condition.SearchCondition;
import io.getbit.uiautomation.enums.ControlType;

/**
 * TreeItem 控件
 *
 * <p>对应 UIAutomation 的 TreeItem 控件类型 (ControlType=50028)。
 * 支持 {@link ExpandCollapsePattern} 展开/折叠子节点，
 * 支持 {@link SelectionPattern} 选中状态。</p>
 */
public class TreeItemControl extends Control {

    public TreeItemControl() {
        super();
    }

    public TreeItemControl(SearchCondition searchCondition) {
        super(searchCondition);
    }

    @Override
    public ControlType getControlType() {
        return ControlType.TreeItem;
    }
}
