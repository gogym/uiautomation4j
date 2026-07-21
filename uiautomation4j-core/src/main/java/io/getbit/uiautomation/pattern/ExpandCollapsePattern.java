package io.getbit.uiautomation.pattern;

/**
 * ExpandCollapsePattern - 用于展开/折叠操作
 *
 * <p>对应 UIAutomation 的 {@code IUIAutomationExpandCollapsePattern} (PatternId: 10005)。
 * 参考文档: https://learn.microsoft.com/en-us/windows/win32/api/uiautomationclient/nn-uiautomationclient-iuiautomationexpandcollapsepattern</p>
 *
 * <p>支持控件：TreeItem、ComboBox、MenuItem 等</p>
 *
 * <p>状态常量对应 {@code ExpandCollapseState} 枚举：</p>
 * <ul>
 *   <li>{@code STATE_COLLAPSED (0)} - 已折叠</li>
 *   <li>{@code STATE_EXPANDED (1)} - 已展开</li>
 *   <li>{@code STATE_PARTIALLY_EXPANDED (2)} - 部分展开</li>
 *   <li>{@code STATE_LEAF_NODE (3)} - 叶子节点（不可展开）</li>
 * </ul>
 */
public interface ExpandCollapsePattern extends Pattern {

    /** 已折叠状态 */
    int STATE_COLLAPSED = 0;
    /** 已展开状态 */
    int STATE_EXPANDED = 1;
    /** 部分展开状态（如树节点仅部分子节点可见） */
    int STATE_PARTIALLY_EXPANDED = 2;
    /** 叶子节点（无子节点，不可展开） */
    int STATE_LEAF_NODE = 3;

    /**
     * 展开控件
     * <p>对应 COM 方法: {@code IUIAutomationExpandCollapsePattern::Expand()}</p>
     */
    void expand();

    /**
     * 折叠控件
     * <p>对应 COM 方法: {@code IUIAutomationExpandCollapsePattern::Collapse()}</p>
     */
    void collapse();

    /**
     * 获取当前展开/折叠状态
     * <p>对应 COM 属性: {@code get_CurrentExpandCollapseState(ExpandCollapseState* pState)}</p>
     *
     * @return 状态值，参见 STATE_ 常量
     */
    int getExpandCollapseState();
}
