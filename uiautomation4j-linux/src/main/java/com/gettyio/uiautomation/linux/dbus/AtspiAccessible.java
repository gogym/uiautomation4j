package com.gettyio.uiautomation.linux.dbus;

import org.freedesktop.dbus.DBusPath;
import org.freedesktop.dbus.interfaces.DBusInterface;
import org.freedesktop.dbus.types.UInt32;
import org.freedesktop.dbus.types.Variant;

import java.util.List;
import java.util.Map;

/**
 * AT-SPI2 Accessible 接口 - D-Bus 绑定
 *
 * <p>对应 D-Bus 接口 {@code org.a11y.atspi.Accessible}，是 AT-SPI2 无障碍框架的核心接口。
 * 每个可访问的 UI 元素都实现此接口，提供属性查询和子元素遍历能力。</p>
 *
 * <p>等价于 Windows 的 {@code IUIAutomationElement} 和 macOS 的 {@code AXUIElement}。</p>
 *
 * <h3>D-Bus 接口信息：</h3>
 * <ul>
 *   <li>接口名: org.a11y.atspi.Accessible</li>
 *   <li>对象路径: /org/a11y/atspi/accessible/...</li>
 *   <li>总线类型: 直连（peer-to-peer）或会话总线</li>
 * </ul>
 *
 * @see <a href="https://gitlab.gnome.org/GNOME/at-spi2-core/blob/main/xml/Accessible.xml">AT-SPI2 Accessible XML</a>
 */
public interface AtspiAccessible extends DBusInterface {

    /**
     * 获取子元素
     * <p>D-Bus 方法: GetChildAtIndex(i) → o</p>
     *
     * @param index 子元素索引（从 0 开始）
     * @return 子元素的 D-Bus 对象路径
     */
    DBusPath GetChildAtIndex(int index);

    /**
     * 获取子元素数量
     * <p>D-Bus 属性: ChildCount → i</p>
     *
     * @return 子元素数量
     */
    int GetChildCount();

    /**
     * 获取元素名称
     * <p>D-Bus 属性: Name → s</p>
     *
     * @return 元素名称（如按钮文字、窗口标题等）
     */
    String GetName();

    /**
     * 获取元素角色
     * <p>D-Bus 属性: Role → u</p>
     * <p>角色值对应 AT-SPI2 的 Role 枚举（如 43=ROLE_PUSH_BUTTON）</p>
     *
     * @return 角色整数值
     */
    UInt32 GetRole();

    /**
     * 获取角色本地化名称
     * <p>D-Bus 方法: GetRoleName() → s</p>
     *
     * @return 角色的本地化名称（如 "push button"、"frame"）
     */
    String GetRoleName();

    /**
     * 获取元素描述
     * <p>D-Bus 属性: Description → s</p>
     *
     * @return 元素描述文本
     */
    String GetDescription();

    /**
     * 获取父元素
     * <p>D-Bus 属性: Parent → o</p>
     *
     * @return 父元素的 D-Bus 对象路径
     */
    DBusPath GetParent();

    /**
     * 获取元素状态集合
     * <p>D-Bus 方法: GetState() → au</p>
     * <p>状态是位标志数组，每个 UInt32 包含 32 个状态位。</p>
     *
     * @return 状态数组
     */
    UInt32[] GetState();

    /**
     * 获取元素支持的接口列表
     * <p>D-Bus 属性: Interfaces → as</p>
     *
     * @return 接口名称列表（如 "org.a11y.atspi.Action"）
     */
    List<String> GetInterfaces();

    /**
     * 获取元素属性映射
     * <p>D-Bus 方法: GetAttributes() → a{ss}</p>
     *
     * @return 属性键值对
     */
    Map<String, String> GetAttributes();

    /**
     * 获取指定索引处的子元素（带接口过滤）
     * <p>D-Bus 方法: GetChildAtPoint(i,i,s) → o</p>
     *
     * @param x         X 坐标
     * @param y         Y 坐标
     * @param sortOrder 排序方式（"READ" 或 "FLOWS"）
     * @return 该坐标处的子元素路径
     */
    DBusPath GetChildAtPoint(int x, int y, String sortOrder);
}
