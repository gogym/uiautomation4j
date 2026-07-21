package com.gettyio.uiautomation.linux.ax;

import com.gettyio.uiautomation.linux.dbus.*;
import org.freedesktop.dbus.DBusPath;
import org.freedesktop.dbus.types.UInt32;

import java.util.ArrayList;
import java.util.List;

/**
 * AT-SPI2 元素包装类
 *
 * <p>封装 D-Bus 代理对象，提供对 AT-SPI2 无障碍元素的高层访问。
 * 等价于 macOS 的 AXUIElement 和 Windows 的 IUIAutomationElement。</p>
 *
 * <p>每个 AtspiElement 持有一个 D-Bus 对象路径和对应的 Accessible 代理，
 * 并可以按需获取 Action、Component、Text、Value 等附加接口代理。</p>
 *
 * @see AtspiConnection
 */
public class AtspiElement {

    /** D-Bus 连接管理器 */
    private final AtspiConnection connection;

    /** D-Bus 对象路径 */
    private final String objectPath;

    /** D-Bus 总线名（通常是应用名） */
    private final String busName;

    /** Accessible 接口代理 */
    private final AtspiAccessible accessible;

    /** 缓存的角色值 */
    private int cachedRole = -1;

    /** 缓存的名称 */
    private String cachedName;

    /**
     * 构造 AtspiElement
     *
     * @param connection D-Bus 连接管理器
     * @param busName    D-Bus 总线名
     * @param objectPath D-Bus 对象路径
     */
    public AtspiElement(AtspiConnection connection, String busName, String objectPath) {
        this.connection = connection;
        this.busName = busName;
        this.objectPath = objectPath;
        this.accessible = connection.getAccessible(busName, objectPath);
    }

    /**
     * 从已有的 Accessible 代理构造
     *
     * @param connection D-Bus 连接管理器
     * @param busName    D-Bus 总线名
     * @param accessible Accessible 代理
     */
    public AtspiElement(AtspiConnection connection, String busName, AtspiAccessible accessible) {
        this.connection = connection;
        this.busName = busName;
        this.accessible = accessible;
        this.objectPath = accessible.getObjectPath();
    }

    /**
     * 获取元素名称
     *
     * @return 元素名称（如按钮文字、窗口标题）
     */
    public String getName() {
        if (cachedName == null) {
            try {
                cachedName = accessible.GetName();
            } catch (Exception e) {
                cachedName = "";
            }
        }
        return cachedName;
    }

    /**
     * 获取元素角色值
     *
     * @return AT-SPI2 Role 整数值
     */
    public int getRole() {
        if (cachedRole < 0) {
            try {
                UInt32 role = accessible.GetRole();
                cachedRole = role.intValue();
            } catch (Exception e) {
                cachedRole = AtspiConstants.ROLE_UNKNOWN;
            }
        }
        return cachedRole;
    }

    /**
     * 获取角色名称
     *
     * @return 角色的本地化名称（如 "push button"）
     */
    public String getRoleName() {
        try {
            return accessible.GetRoleName();
        } catch (Exception e) {
            return "unknown";
        }
    }

    /**
     * 获取元素描述
     *
     * @return 元素描述文本
     */
    public String getDescription() {
        try {
            return accessible.GetDescription();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * 获取子元素数量
     *
     * @return 子元素数量
     */
    public int getChildCount() {
        try {
            return accessible.GetChildCount();
        } catch (Exception e) {
            return 0;
        }
    }

    /**
     * 获取所有子元素
     *
     * @return 子元素列表
     */
    public List<AtspiElement> getChildren() {
        List<AtspiElement> children = new ArrayList<>();
        int count = getChildCount();
        for (int i = 0; i < count; i++) {
            try {
                DBusPath childPath = accessible.GetChildAtIndex(i);
                if (childPath != null) {
                    String path = childPath.getPath();
                    AtspiElement child = new AtspiElement(connection, busName, path);
                    children.add(child);
                }
            } catch (Exception e) {
                // 跳过无法访问的子元素
            }
        }
        return children;
    }

    /**
     * 获取指定索引的子元素
     *
     * @param index 子元素索引
     * @return 子元素，如果索引无效返回 null
     */
    public AtspiElement getChildAtIndex(int index) {
        try {
            DBusPath childPath = accessible.GetChildAtIndex(index);
            if (childPath != null) {
                return new AtspiElement(connection, busName, childPath.getPath());
            }
        } catch (Exception e) {
            // 忽略
        }
        return null;
    }

    /**
     * 获取父元素
     *
     * @return 父元素，如果已是根元素返回 null
     */
    public AtspiElement getParent() {
        try {
            DBusPath parentPath = accessible.GetParent();
            if (parentPath != null && !parentPath.getPath().isEmpty()) {
                return new AtspiElement(connection, busName, parentPath.getPath());
            }
        } catch (Exception e) {
            // 忽略
        }
        return null;
    }

    /**
     * 获取元素支持的接口列表
     *
     * @return 接口名称列表
     */
    public List<String> getInterfaces() {
        try {
            return accessible.GetInterfaces();
        } catch (Exception e) {
            return new ArrayList<>();
        }
    }

    /**
     * 检查元素是否支持指定接口
     *
     * @param interfaceName 接口名（如 "org.a11y.atspi.Action"）
     * @return 是否支持
     */
    public boolean hasInterface(String interfaceName) {
        return getInterfaces().contains(interfaceName);
    }

    /**
     * 获取元素状态数组
     *
     * @return 状态位数组
     */
    public UInt32[] getState() {
        try {
            return accessible.GetState();
        } catch (Exception e) {
            return new UInt32[0];
        }
    }

    /**
     * 检查元素是否包含指定状态
     *
     * @param stateIndex 状态索引（如 AtspiConstants.STATE_ENABLED = 7）
     * @return 是否包含该状态
     */
    public boolean hasState(int stateIndex) {
        UInt32[] states = getState();
        if (states.length == 0) return false;
        // AT-SPI2 状态是位数组：stateIndex / 32 确定在哪个 UInt32，stateIndex % 32 确定位
        int arrayIndex = stateIndex / 32;
        int bitIndex = stateIndex % 32;
        if (arrayIndex >= states.length) return false;
        return (states[arrayIndex].intValue() & (1 << bitIndex)) != 0;
    }

    /**
     * 检查元素是否启用
     *
     * @return 如果元素已启用返回 true
     */
    public boolean isEnabled() {
        return hasState(AtspiConstants.STATE_ENABLED) && hasState(AtspiConstants.STATE_SENSITIVE);
    }

    /**
     * 检查元素是否可见
     *
     * @return 如果元素可见返回 true
     */
    public boolean isVisible() {
        return hasState(AtspiConstants.STATE_VISIBLE) || hasState(AtspiConstants.STATE_SHOWING);
    }

    /**
     * 检查元素是否聚焦
     *
     * @return 如果元素已聚焦返回 true
     */
    public boolean isFocused() {
        return hasState(AtspiConstants.STATE_FOCUSED);
    }

    /**
     * 获取元素的边界矩形
     *
     * @return int[4] = {x, y, width, height}，如果获取失败返回全零数组
     */
    public int[] getBoundingRectangle() {
        try {
            AtspiComponent component = getComponent();
            if (component != null) {
                UInt32 coordType = new UInt32(0); // 屏幕坐标
                int[] position = component.GetPosition(coordType);
                int[] size = component.GetSize(coordType);
                if (position != null && position.length >= 2 && size != null && size.length >= 2) {
                    return new int[]{position[0], position[1], size[0], size[1]};
                }
            }
        } catch (Exception e) {
            // 忽略
        }
        return new int[]{0, 0, 0, 0};
    }

    // ==================== 附加接口获取 ====================

    /**
     * 获取 Action 接口代理
     *
     * @return Action 代理，如果不支持返回 null
     */
    public AtspiAction getAction() {
        return connection.getRemoteObject(busName, objectPath, AtspiAction.class);
    }

    /**
     * 获取 Component 接口代理
     *
     * @return Component 代理，如果不支持返回 null
     */
    public AtspiComponent getComponent() {
        return connection.getRemoteObject(busName, objectPath, AtspiComponent.class);
    }

    /**
     * 获取 Text 接口代理
     *
     * @return Text 代理，如果不支持返回 null
     */
    public AtspiText getText() {
        return connection.getRemoteObject(busName, objectPath, AtspiText.class);
    }

    /**
     * 获取 Value 接口代理
     *
     * @return Value 代理，如果不支持返回 null
     */
    public AtspiValue getValue() {
        return connection.getRemoteObject(busName, objectPath, AtspiValue.class);
    }

    /**
     * 执行默认动作（通常是 "click"）
     *
     * @return 是否成功
     */
    public boolean doDefaultAction() {
        AtspiAction action = getAction();
        if (action != null) {
            try {
                int nActions = action.GetNActions();
                if (nActions > 0) {
                    return action.DoAction(0);
                }
            } catch (Exception e) {
                // 忽略
            }
        }
        return false;
    }

    /**
     * 将焦点设置到此元素
     *
     * @return 是否成功
     */
    public boolean grabFocus() {
        AtspiComponent component = getComponent();
        if (component != null) {
            try {
                return component.GrabFocus();
            } catch (Exception e) {
                // 忽略
            }
        }
        return false;
    }

    // ==================== Getters ====================

    /**
     * 获取 D-Bus 对象路径
     *
     * @return 对象路径
     */
    public String getObjectPath() {
        return objectPath;
    }

    /**
     * 获取 D-Bus 总线名
     *
     * @return 总线名
     */
    public String getBusName() {
        return busName;
    }

    /**
     * 获取 Accessible 代理
     *
     * @return Accessible 代理
     */
    public AtspiAccessible getAccessible() {
        return accessible;
    }

    /**
     * 获取 D-Bus 连接管理器
     *
     * @return 连接管理器
     */
    public AtspiConnection getConnection() {
        return connection;
    }

    @Override
    public String toString() {
        return "AtspiElement{name='" + getName() + "', role=" + getRoleName() +
                ", path='" + objectPath + "'}";
    }
}
