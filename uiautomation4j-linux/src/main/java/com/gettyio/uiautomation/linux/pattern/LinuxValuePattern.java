package com.gettyio.uiautomation.linux.pattern;

import com.gettyio.uiautomation.linux.ax.AtspiElement;
import com.gettyio.uiautomation.linux.dbus.AtspiText;
import com.gettyio.uiautomation.linux.dbus.AtspiValue;
import com.gettyio.uiautomation.pattern.ValuePattern;

/**
 * ValuePattern 的 Linux 实现
 *
 * <p>通过 AT-SPI2 Value 接口读写控件的数值，或通过 Text 接口读写文本值。
 * 等价于 Windows 的 IUIAutomationValuePattern。</p>
 *
 * <p>支持两种模式：
 * <ul>
 *   <li>数值控件（Slider、ProgressBar 等）：通过 AtspiValue 接口</li>
 *   <li>文本控件（Edit、Label 等）：通过 AtspiText 接口</li>
 * </ul>
 */
public class LinuxValuePattern implements ValuePattern {

    /** 底层 AtspiElement 引用 */
    private final AtspiElement element;

    /**
     * 构造 LinuxValuePattern
     *
     * @param element AtspiElement 元素
     */
    public LinuxValuePattern(AtspiElement element) {
        this.element = element;
    }

    /**
     * 获取控件的当前值
     * <p>优先尝试 AtspiValue 接口，如果不可用则尝试 AtspiText 接口</p>
     *
     * @return 控件当前值
     */
    @Override
    public String getValue() {
        // 先尝试 Value 接口（数值型控件）
        AtspiValue value = element.getValue();
        if (value != null) {
            try {
                double current = value.GetCurrentValue();
                return String.valueOf(current);
            } catch (Exception e) {
                // Value 接口不可用，尝试 Text 接口
            }
        }

        // 尝试 Text 接口（文本型控件）
        AtspiText text = element.getText();
        if (text != null) {
            try {
                return text.GetText(0, -1);
            } catch (Exception e) {
                // Text 接口也不可用
            }
        }

        // 回退到元素名称
        return element.getName();
    }

    /**
     * 设置控件的值
     * <p>优先尝试 AtspiValue 接口，如果不可用则尝试 AtspiText 接口</p>
     *
     * @param value 要设置的值
     */
    @Override
    public void setValue(String value) {
        // 先尝试 Value 接口（数值型控件）
        AtspiValue valueIface = element.getValue();
        if (valueIface != null) {
            try {
                double numValue = Double.parseDouble(value);
                valueIface.SetCurrentValue(numValue);
                return;
            } catch (NumberFormatException e) {
                // 不是数值，尝试 Text 接口
            } catch (Exception e) {
                // Value 设置失败
            }
        }

        // 尝试 Text 接口（文本型控件）
        AtspiText text = element.getText();
        if (text != null) {
            try {
                text.SetText(value);
                return;
            } catch (Exception e) {
                // Text 设置失败
            }
        }

        throw new UnsupportedOperationException("控件不支持 setValue 操作: " + element);
    }

    /**
     * 检查控件值是否只读
     * <p>通过检查元素状态判断是否可编辑</p>
     *
     * @return 如果只读返回 true
     */
    @Override
    public boolean isReadOnly() {
        // 检查是否有 editable 状态
        return !element.hasState(com.gettyio.uiautomation.linux.ax.AtspiConstants.STATE_EDITABLE);
    }
}
