package io.getbit.uiautomation.win.pattern;

import io.getbit.uiautomation.pattern.ValuePattern;
import io.getbit.uiautomation.win.com.IUIAutomationValuePattern;

/**
 * ValuePattern 的 Windows 实现
 *
 * <p>将 core 模块的 {@link ValuePattern} 接口桥接到 Windows COM 层的
 * {@link IUIAutomationValuePattern}。通过 {@code WinControlBackend.getValuePattern()}
 * 获取，内部调用 COM vtable 方法。</p>
 */
public class WinValuePattern implements ValuePattern {

    /** 底层 COM ValuePattern 对象 */
    private final IUIAutomationValuePattern comPattern;

    /**
     * 构造方法
     *
     * @param comPattern 底层 COM ValuePattern 对象
     */
    public WinValuePattern(IUIAutomationValuePattern comPattern) {
        this.comPattern = comPattern;
    }

    @Override
    public String getValue() {
        return comPattern.getCurrentValue();
    }

    @Override
    public void setValue(String value) {
        comPattern.setValue(value);
    }

    @Override
    public boolean isReadOnly() {
        return comPattern.isReadOnly();
    }
}
