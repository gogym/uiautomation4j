package com.gettyio.uiautomation.linux.pattern;

import com.gettyio.uiautomation.linux.ax.AtspiConstants;
import com.gettyio.uiautomation.linux.ax.AtspiElement;
import com.gettyio.uiautomation.pattern.SelectionPattern;

import java.util.ArrayList;
import java.util.List;

/**
 * SelectionPattern 的 Linux 实现
 *
 * <p>通过遍历子元素并检查 STATE_SELECTED 状态获取选中项。
 * 等价于 Windows 的 IUIAutomationSelectionPattern。</p>
 */
public class LinuxSelectionPattern implements SelectionPattern {

    /** 底层 AtspiElement 引用 */
    private final AtspiElement element;

    /**
     * 构造 LinuxSelectionPattern
     *
     * @param element AtspiElement 元素
     */
    public LinuxSelectionPattern(AtspiElement element) {
        this.element = element;
    }

    /**
     * 获取当前选中的元素列表
     * <p>遍历子元素，检查每个子元素是否具有 STATE_SELECTED 状态</p>
     *
     * @return 选中元素列表
     */
    @Override
    public List<Object> getSelection() {
        List<Object> selected = new ArrayList<>();
        List<AtspiElement> children = element.getChildren();
        for (AtspiElement child : children) {
            if (child.hasState(AtspiConstants.STATE_SELECTED)) {
                selected.add(child);
            }
        }
        return selected;
    }

    /**
     * 判断是否支持多选
     * <p>通过检查元素是否具有 STATE_MULTISELECTABLE 状态判断</p>
     *
     * @return 如果支持多选返回 true
     */
    @Override
    public boolean isMultiSelect() {
        return element.hasState(AtspiConstants.STATE_MULTISELECTABLE);
    }
}
