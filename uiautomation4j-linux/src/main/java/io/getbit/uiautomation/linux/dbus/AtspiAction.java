package io.getbit.uiautomation.linux.dbus;

import org.freedesktop.dbus.interfaces.DBusInterface;

/**
 * AT-SPI2 Action 接口 - D-Bus 绑定
 *
 * <p>对应 D-Bus 接口 {@code org.a11y.atspi.Action}，提供对 UI 元素可执行动作的访问。
 * 每个可操作的 UI 元素（如按钮、菜单项）都实现此接口。</p>
 *
 * <p>等价于 Windows 的 {@code IUIAutomationInvokePattern} 和 macOS 的 AXAction。</p>
 *
 * <h3>D-Bus 接口信息：</h3>
 * <ul>
 *   <li>接口名: org.a11y.atspi.Action</li>
 *   <li>常见动作: "click"、"press"、"release"、"focus" 等</li>
 * </ul>
 */
public interface AtspiAction extends DBusInterface {

    /**
     * 获取可用动作数量
     * <p>D-Bus 方法: GetNActions() → i</p>
     *
     * @return 动作数量
     */
    int GetNActions();

    /**
     * 执行指定动作
     * <p>D-Bus 方法: DoAction(i,s) → b</p>
     *
     * @param index 动作索引（从 0 开始）
     * @return 是否执行成功
     */
    boolean DoAction(int index);

    /**
     * 获取动作名称
     * <p>D-Bus 方法: GetName(i) → s</p>
     *
     * @param index 动作索引
     * @return 动作名称（如 "click"、"press"）
     */
    String GetName(int index);

    /**
     * 获取动作描述
     * <p>D-Bus 方法: GetDescription(i) → s</p>
     *
     * @param index 动作索引
     * @return 动作描述
     */
    String GetDescription(int index);

    /**
     * 获取动作的键盘快捷键
     * <p>D-Bus 方法: GetKeyBinding(i) → s</p>
     *
     * @param index 动作索引
     * @return 快捷键字符串（如 "&lt;Alt&gt;F4"）
     */
    String GetKeyBinding(int index);
}
