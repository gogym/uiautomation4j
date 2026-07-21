package io.getbit.uiautomation.mac.pattern;

import io.getbit.uiautomation.mac.ax.AXAttribute;
import io.getbit.uiautomation.mac.ax.AXUIElement;
import io.getbit.uiautomation.mac.ax.CFUtil;
import io.getbit.uiautomation.pattern.SelectionPattern;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

import java.util.ArrayList;
import java.util.List;

/**
 * SelectionPattern 的 macOS 实现
 *
 * <p>通过 AXSelectedRows/AXSelectedChildren 属性获取选中项。
 * 等价于 Windows 的 IUIAutomationSelectionPattern。</p>
 */
public class MacSelectionPattern implements SelectionPattern {

    /** 底层 AXUIElement 引用 */
    private final AXUIElement element;

    /**
     * 构造 MacSelectionPattern
     *
     * @param element AXUIElement 元素
     */
    public MacSelectionPattern(AXUIElement element) {
        this.element = element;
    }

    /**
     * 获取当前选中的元素列表
     * <p>查询 AXSelectedRows 或 AXSelectedChildren 属性</p>
     *
     * @return 选中元素列表
     */
    @Override
    public List<Object> getSelection() {
        List<Object> result = new ArrayList<>();
        // 尝试获取选中行
        Pointer selected = element.getAttribute(AXAttribute.SELECTED_ROWS);
        if (selected != null && selected != Pointer.NULL) {
            try {
                long count = CFUtil.getArrayCount(selected);
                for (int i = 0; i < count; i++) {
                    Pointer item = CFUtil.getArrayValue(selected, i);
                    if (item != null && item != Pointer.NULL) {
                        result.add(new AXUIElement(item, false));
                    }
                }
            } finally {
                CFUtil.release(selected);
            }
        }
        return result;
    }

    /**
     * 判断是否支持多选
     * <p>macOS 没有直接的多选属性，通过检查 AXRole 推断：
     * AXList/AXTable 通常支持多选</p>
     *
     * @return 如果可能支持多选返回 true
     */
    @Override
    public boolean isMultiSelect() {
        String role = element.getRole();
        return "AXList".equals(role) || "AXTable".equals(role) || "AXOutline".equals(role);
    }
}
