package com.gettyio.uiautomation.pattern;

/**
 * ExpandCollapsePattern - 用于展开/折叠操作
 * 支持控件：TreeItem、ComboBox、MenuItem 等
 */
public interface ExpandCollapsePattern extends Pattern {

    int STATE_COLLAPSED = 0;
    int STATE_EXPANDED = 1;
    int STATE_PARTIALLY_EXPANDED = 2;
    int STATE_LEAF_NODE = 3;

    /**
     * 展开
     */
    void expand();

    /**
     * 折叠
     */
    void collapse();

    /**
     * 获取当前展开/折叠状态
     */
    int getExpandCollapseState();
}
