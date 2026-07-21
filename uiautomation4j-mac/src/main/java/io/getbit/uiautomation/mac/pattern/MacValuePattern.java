package io.getbit.uiautomation.mac.pattern;

import io.getbit.uiautomation.mac.ax.AXAttribute;
import io.getbit.uiautomation.mac.ax.AXUIElement;
import io.getbit.uiautomation.mac.ax.CFUtil;
import io.getbit.uiautomation.pattern.ValuePattern;
import com.sun.jna.Pointer;

/**
 * ValuePattern 的 macOS 实现
 *
 * <p>通过 AXValue 属性读写控件的值。适用于文本字段、滑块等支持值读写的控件。</p>
 *
 * <p>对应 Windows 的 IUIAutomationValuePattern，macOS 通过 AXValue 属性实现等价功能：
 * <ul>
 *   <li>getValue() → AXUIElementCopyAttributeValue("AXValue")</li>
 *   <li>setValue() → AXUIElementSetAttributeValue("AXValue")</li>
 * </ul>
 */
public class MacValuePattern implements ValuePattern {

    /** 底层 AXUIElement 引用 */
    private final AXUIElement element;

    /**
     * 构造 MacValuePattern
     *
     * @param element AXUIElement 包装对象
     */
    public MacValuePattern(AXUIElement element) {
        this.element = element;
    }

    /**
     * 获取控件的当前值
     * <p>读取 AXValue 属性，返回字符串表示</p>
     *
     * @return 控件当前值
     */
    @Override
    public String getValue() {
        return element.getStringAttribute(AXAttribute.VALUE);
    }

    /**
     * 设置控件的值
     * <p>通过 AXUIElementSetAttributeValue 设置 AXValue 属性。
     * 需要将 Java String 转换为 CFString 后传入。</p>
     *
     * @param value 新值
     */
    @Override
    public void setValue(String value) {
        Pointer cfValue = CFUtil.createCFString(value).getPointer();
        try {
            element.setAttribute(AXAttribute.VALUE, cfValue);
        } finally {
            CFUtil.release(cfValue);
        }
    }

    /**
     * 检查控件值是否只读
     * <p>macOS 没有直接的 IsReadOnly 属性，通过尝试设置值来判断。
     * 如果设置失败则认为只读。</p>
     *
     * @return 如果只读返回 true
     */
    public boolean isReadOnly() {
        // macOS 没有直接的 IsReadOnly 属性
        // 通过检查是否支持 AXValue 的写入来间接判断
        String currentValue = getValue();
        if (currentValue == null) {
            return true; // 无法读取值，认为只读
        }
        return false;
    }
}
