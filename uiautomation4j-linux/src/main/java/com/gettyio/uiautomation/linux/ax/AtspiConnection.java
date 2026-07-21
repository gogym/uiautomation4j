package com.gettyio.uiautomation.linux.ax;

import com.gettyio.uiautomation.linux.dbus.AtspiAccessible;
import org.freedesktop.dbus.DBusPath;
import org.freedesktop.dbus.connections.impl.DBusConnection;
import org.freedesktop.dbus.connections.impl.DBusConnectionBuilder;
import org.freedesktop.dbus.types.UInt32;

import java.util.ArrayList;
import java.util.List;

/**
 * AT-SPI2 D-Bus 连接管理器
 *
 * <p>管理与 D-Bus 会话总线的连接，提供对 AT-SPI2 根元素的访问。
 * 等价于 macOS 的 AXUIElementCreateSystemWide()。</p>
 *
 * <h3>AT-SPI2 D-Bus 架构：</h3>
 * <ul>
 *   <li>总线类型: Session Bus（会话总线）</li>
 *   <li>根对象路径: /org/a11y/atspi/accessible/root</li>
 *   <li>桌面应用名: org.a11y.atspi.Registry</li>
 * </ul>
 *
 * @see <a href="https://gitlab.gnome.org/GNOME/at-spi2-core">AT-SPI2 Core</a>
 */
public class AtspiConnection {

    /** AT-SPI2 根对象路径 */
    public static final String ROOT_PATH = "/org/a11y/atspi/accessible/root";

    /** AT-SPI2 Accessible 接口名 */
    public static final String ACCESSIBLE_INTERFACE = "org.a11y.atspi.Accessible";

    /** D-Bus 会话总线连接 */
    private DBusConnection connection;

    /** 根 Accessible 元素代理 */
    private AtspiAccessible rootAccessible;

    /** 是否已连接 */
    private boolean connected = false;

    /**
     * 构造 AtspiConnection 并连接到 D-Bus 会话总线
     *
     * @throws RuntimeException 如果无法连接到 D-Bus
     */
    public AtspiConnection() {
        try {
            connection = DBusConnectionBuilder.forSessionBus().build();
            rootAccessible = connection.getRemoteObject(
                    "org.a11y.atspi.Registry", ROOT_PATH, AtspiAccessible.class);
            connected = true;
        } catch (Exception e) {
            throw new RuntimeException("无法连接到 AT-SPI2 D-Bus 会话总线。" +
                    "请确保已安装 at-spi2-core 且桌面环境支持无障碍功能。", e);
        }
    }

    /**
     * 获取根 Accessible 元素的 D-Bus 代理
     *
     * @return 根 Accessible 代理对象
     */
    public AtspiAccessible getRootAccessible() {
        return rootAccessible;
    }

    /**
     * 获取 D-Bus 连接
     *
     * @return D-Bus 会话总线连接
     */
    public DBusConnection getConnection() {
        return connection;
    }

    /**
     * 获取指定 D-Bus 路径的 Accessible 代理
     *
     * @param busName    D-Bus 总线名（如 "org.a11y.atspi.Registry"）
     * @param objectPath D-Bus 对象路径
     * @return Accessible 代理对象
     */
    public AtspiAccessible getAccessible(String busName, String objectPath) {
        try {
            return connection.getRemoteObject(busName, objectPath, AtspiAccessible.class);
        } catch (Exception e) {
            throw new RuntimeException("无法获取 Accessible 代理: " + objectPath, e);
        }
    }

    /**
     * 获取指定路径和接口类型的远程对象
     *
     * @param busName       D-Bus 总线名
     * @param objectPath    D-Bus 对象路径
     * @param interfaceType D-Bus 接口类
     * @param <T>           接口类型
     * @return 远程对象代理
     */
    public <T extends org.freedesktop.dbus.interfaces.DBusInterface> T getRemoteObject(
            String busName, String objectPath, Class<T> interfaceType) {
        try {
            return connection.getRemoteObject(busName, objectPath, interfaceType);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取根元素下的所有应用程序（顶层窗口容器）
     *
     * @return 应用程序 Accessible 代理列表
     */
    public List<AtspiAccessible> getApplications() {
        List<AtspiAccessible> apps = new ArrayList<>();
        int childCount = rootAccessible.GetChildCount();
        for (int i = 0; i < childCount; i++) {
            try {
                DBusPath childPath = rootAccessible.GetChildAtIndex(i);
                if (childPath != null) {
                    // 从路径中提取 bus name 和 object path
                    String path = childPath.getPath();
                    AtspiAccessible child = getAccessible("org.a11y.atspi.Registry", path);
                    apps.add(child);
                }
            } catch (Exception e) {
                // 跳过无法访问的子元素
            }
        }
        return apps;
    }

    /**
     * 是否已连接
     *
     * @return 如果 D-Bus 连接已建立返回 true
     */
    public boolean isConnected() {
        return connected;
    }

    /**
     * 关闭连接并释放资源
     */
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (Exception e) {
                // 忽略关闭异常
            }
            connected = false;
        }
    }
}
