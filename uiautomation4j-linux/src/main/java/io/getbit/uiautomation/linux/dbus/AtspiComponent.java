package io.getbit.uiautomation.linux.dbus;

import org.freedesktop.dbus.interfaces.DBusInterface;
import org.freedesktop.dbus.types.UInt32;

/**
 * AT-SPI2 Component 接口 - D-Bus 绑定
 *
 * <p>对应 D-Bus 接口 {@code org.a11y.atspi.Component}，提供 UI 元素的几何信息
 * （位置、大小）和交互操作（点击、聚焦）。</p>
 *
 * <p>等价于 Windows 的 {@code IUIAutomationElement::CurrentBoundingRectangle} 和
 * macOS 的 {@code AXUIElement::getBoundingRectangle()}。</p>
 *
 * <h3>D-Bus 接口信息：</h3>
 * <ul>
 *   <li>接口名: org.a11y.atspi.Component</li>
 *   <li>坐标系: 屏幕绝对坐标（左上角为原点）</li>
 * </ul>
 */
public interface AtspiComponent extends DBusInterface {

    /**
     * 获取元素在屏幕上的位置
     * <p>D-Bus 方法: GetPosition(u) → (ii)</p>
     *
     * @param coordType 坐标类型（0=屏幕坐标, 1=窗口坐标）
     * @return int[2] = {x, y}
     */
    int[] GetPosition(UInt32 coordType);

    /**
     * 获取元素的尺寸
     * <p>D-Bus 方法: GetSize(u) → (ii)</p>
     *
     * @param coordType 坐标类型（0=屏幕坐标, 1=窗口坐标）
     * @return int[2] = {width, height}
     */
    int[] GetSize(UInt32 coordType);

    /**
     * 在指定坐标执行鼠标点击
     * <p>D-Bus 方法: Click(i,i,u) → b</p>
     *
     * @param x         X 坐标
     * @param y         Y 坐标
     * @param mouseButton 鼠标按钮（1=左键, 2=中键, 3=右键）
     * @return 是否成功
     */
    boolean Click(int x, int y, UInt32 mouseButton);

    /**
     * 将焦点设置到此元素
     * <p>D-Bus 方法: GrabFocus() → b</p>
     *
     * @return 是否成功
     */
    boolean GrabFocus();

    /**
     * 检查元素是否可见
     * <p>D-Bus 属性: Extents → (iiii)</p>
     *
     * @return 如果元素在屏幕可见区域内返回 true
     */
    boolean Contains(int x, int y, UInt32 coordType);

    /**
     * 获取元素在指定坐标处的子元素
     * <p>D-Bus 方法: GetAccessibleAtPoint(i,i,u) → o</p>
     *
     * @param x         X 坐标
     * @param y         Y 坐标
     * @param coordType 坐标类型
     * @return 该坐标处的可访问元素路径
     */
    org.freedesktop.dbus.DBusPath GetAccessibleAtPoint(int x, int y, UInt32 coordType);
}
