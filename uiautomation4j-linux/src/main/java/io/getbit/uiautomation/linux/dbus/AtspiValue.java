package io.getbit.uiautomation.linux.dbus;

import org.freedesktop.dbus.interfaces.DBusInterface;
import org.freedesktop.dbus.types.Variant;

/**
 * AT-SPI2 Value 接口 - D-Bus 绑定
 *
 * <p>对应 D-Bus 接口 {@code org.a11y.atspi.Value}，提供数值型控件的值访问。
 * 适用于滑块、进度条、微调按钮等具有数值属性的 UI 元素。</p>
 *
 * <p>等价于 Windows 的 {@code IUIAutomationValuePattern} 和 macOS 的 AXValue（数值类型）。</p>
 *
 * <h3>D-Bus 接口信息：</h3>
 * <ul>
 *   <li>接口名: org.a11y.atspi.Value</li>
 *   <li>支持控件: Slider、ProgressBar、SpinButton 等</li>
 * </ul>
 */
public interface AtspiValue extends DBusInterface {

    /**
     * 获取当前值
     * <p>D-Bus 属性: CurrentValue → d</p>
     *
     * @return 当前数值
     */
    double GetCurrentValue();

    /**
     * 设置当前值
     * <p>D-Bus 属性: CurrentValue (writable) → d</p>
     *
     * @param value 新数值
     * @return 是否设置成功
     */
    boolean SetCurrentValue(double value);

    /**
     * 获取最小值
     * <p>D-Bus 属性: MinimumValue → d</p>
     *
     * @return 最小值
     */
    double GetMinimumValue();

    /**
     * 获取最大值
     * <p>D-Bus 属性: MaximumValue → d</p>
     *
     * @return 最大值
     */
    double GetMaximumValue();

    /**
     * 获取最小增量
     * <p>D-Bus 属性: MinimumIncrement → d</p>
     *
     * @return 最小增量步长
     */
    double GetMinimumIncrement();
}
