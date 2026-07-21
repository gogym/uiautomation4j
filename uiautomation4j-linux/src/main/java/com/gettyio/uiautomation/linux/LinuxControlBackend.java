package com.gettyio.uiautomation.linux;

import com.gettyio.uiautomation.condition.SearchCondition;
import com.gettyio.uiautomation.control.Control;
import com.gettyio.uiautomation.enums.ControlType;
import com.gettyio.uiautomation.exception.AutomationException;
import com.gettyio.uiautomation.exception.ControlNotFoundException;
import com.gettyio.uiautomation.linux.ax.AtspiConnection;
import com.gettyio.uiautomation.linux.ax.AtspiElement;
import com.gettyio.uiautomation.linux.pattern.*;
import com.gettyio.uiautomation.pattern.*;
import com.gettyio.uiautomation.spi.ControlBackend;

import java.util.List;

/**
 * Linux 平台的 ControlBackend SPI 实现
 *
 * <p>通过 dbus-java 调用 AT-SPI2 无障碍接口实现控件搜索和操作。
 * 等价于 Windows 平台的 {@code WinControlBackend} 和 macOS 平台的 {@code MacControlBackend}。</p>
 *
 * <h3>搜索策略：</h3>
 * <ol>
 *   <li>从根元素（AT-SPI2 desktop）开始遍历 UI 树</li>
 *   <li>优先使用 AT-SPI Role 过滤（映射为 ControlType）</li>
 *   <li>使用 Name/Description 进行名称匹配</li>
 *   <li>支持深度限制（searchDepth）</li>
 *   <li>Java 层二次过滤：subName（子串匹配）、regexName（正则匹配）、compare（自定义比较器）</li>
 * </ol>
 *
 * @see com.gettyio.uiautomation.mac.MacControlBackend
 */
public class LinuxControlBackend implements ControlBackend {

    /** AT-SPI2 D-Bus 连接 */
    private AtspiConnection connection;

    /** 全局搜索超时（秒） */
    private int globalSearchTimeout = 10;

    /**
     * 构造 LinuxControlBackend
     * <p>建立 D-Bus 连接并获取根元素</p>
     */
    public LinuxControlBackend() {
        this.connection = new AtspiConnection();
    }

    /**
     * 获取 D-Bus 连接（供 Pattern 实现使用）
     *
     * @return AT-SPI2 连接
     */
    public AtspiConnection getConnection() {
        return connection;
    }

    /**
     * 根据搜索条件查找控件
     *
     * @param condition 搜索条件
     * @return 找到的控件
     * @throws ControlNotFoundException 如果找不到匹配的控件
     */
    @Override
    public Control findControl(SearchCondition condition) {
        AtspiElement startElement = resolveStartElement(condition);
        int maxDepth = condition.getSearchDepth() > 0 ? condition.getSearchDepth() : Integer.MAX_VALUE;

        AtspiElement found = searchElement(startElement, condition, 0, maxDepth, new int[]{0});
        if (found == null) {
            throw new ControlNotFoundException(condition.toString());
        }

        return LinuxControlFactory.createControl(found, condition);
    }

    /**
     * 检查控件是否存在
     *
     * @param condition      搜索条件
     * @param maxWaitSeconds 最大等待秒数
     * @return 如果找到返回 true
     */
    @Override
    public boolean exists(SearchCondition condition, int maxWaitSeconds) {
        long deadline = System.currentTimeMillis() + maxWaitSeconds * 1000L;
        while (System.currentTimeMillis() < deadline) {
            try {
                AtspiElement startElement = resolveStartElement(condition);
                int maxDepth = condition.getSearchDepth() > 0 ? condition.getSearchDepth() : Integer.MAX_VALUE;
                AtspiElement found = searchElement(startElement, condition, 0, maxDepth, new int[]{0});
                if (found != null) {
                    return true;
                }
            } catch (Exception e) {
                // 搜索失败，继续重试
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        return false;
    }

    // ==================== Pattern 获取 ====================

    @Override
    public ValuePattern getValuePattern(Control control) {
        AtspiElement element = getAtspiElement(control);
        return new LinuxValuePattern(element);
    }

    @Override
    public WindowPattern getWindowPattern(Control control) {
        AtspiElement element = getAtspiElement(control);
        return new LinuxWindowPattern(element);
    }

    @Override
    public InvokePattern getInvokePattern(Control control) {
        AtspiElement element = getAtspiElement(control);
        return new LinuxInvokePattern(element);
    }

    @Override
    public SelectionPattern getSelectionPattern(Control control) {
        AtspiElement element = getAtspiElement(control);
        return new LinuxSelectionPattern(element);
    }

    @Override
    public ScrollPattern getScrollPattern(Control control) {
        AtspiElement element = getAtspiElement(control);
        return new LinuxScrollPattern(element);
    }

    @Override
    public ExpandCollapsePattern getExpandCollapsePattern(Control control) {
        AtspiElement element = getAtspiElement(control);
        return new LinuxExpandCollapsePattern(element);
    }

    @Override
    public TransformPattern getTransformPattern(Control control) {
        AtspiElement element = getAtspiElement(control);
        return new LinuxTransformPattern(element);
    }

    // ==================== 操作 ====================

    /**
     * 发送按键
     * <p>先聚焦元素，然后通过 XTest 模拟键盘输入</p>
     */
    @Override
    public void sendKeys(Control control, String keys) {
        AtspiElement element = getAtspiElement(control);
        element.grabFocus();
        com.gettyio.uiautomation.linux.keyboard.Keyboard.sendKeys(keys);
    }

    /**
     * 点击控件
     * <p>获取控件中心坐标，通过 XTest 模拟鼠标点击</p>
     */
    @Override
    public void click(Control control) {
        AtspiElement element = getAtspiElement(control);
        int[] rect = element.getBoundingRectangle();
        int centerX = rect[0] + rect[2] / 2;
        int centerY = rect[1] + rect[3] / 2;
        com.gettyio.uiautomation.linux.mouse.Mouse.click(centerX, centerY);
    }

    @Override
    public void doubleClick(Control control) {
        AtspiElement element = getAtspiElement(control);
        int[] rect = element.getBoundingRectangle();
        int centerX = rect[0] + rect[2] / 2;
        int centerY = rect[1] + rect[3] / 2;
        com.gettyio.uiautomation.linux.mouse.Mouse.doubleClick(centerX, centerY);
    }

    @Override
    public void rightClick(Control control) {
        AtspiElement element = getAtspiElement(control);
        int[] rect = element.getBoundingRectangle();
        int centerX = rect[0] + rect[2] / 2;
        int centerY = rect[1] + rect[3] / 2;
        com.gettyio.uiautomation.linux.mouse.Mouse.rightClick(centerX, centerY);
    }

    @Override
    public void captureToImage(Control control, String filePath) {
        AtspiElement element = getAtspiElement(control);
        int[] rect = element.getBoundingRectangle();
        com.gettyio.uiautomation.linux.screenshot.Screenshot.captureRegion(
                filePath, rect[0], rect[1], rect[2], rect[3]);
    }

    // ==================== 超时设置 ====================

    @Override
    public void setGlobalSearchTimeout(int seconds) {
        this.globalSearchTimeout = seconds;
    }

    @Override
    public int getGlobalSearchTimeout() {
        return globalSearchTimeout;
    }

    // ==================== 状态检查 ====================

    @Override
    public boolean waitReady(Control control, int maxWaitSeconds) {
        long deadline = System.currentTimeMillis() + maxWaitSeconds * 1000L;
        while (System.currentTimeMillis() < deadline) {
            try {
                if (isEnabled(control) && isVisible(control)) {
                    return true;
                }
            } catch (Exception e) {
                // 忽略异常，继续等待
            }
            try {
                Thread.sleep(300);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        return false;
    }

    @Override
    public boolean isEnabled(Control control) {
        AtspiElement element = getAtspiElement(control);
        return element.isEnabled();
    }

    @Override
    public boolean isVisible(Control control) {
        AtspiElement element = getAtspiElement(control);
        return element.isVisible();
    }

    // ==================== 内部搜索方法 ====================

    /**
     * 确定搜索起点元素
     *
     * @param condition 搜索条件
     * @return 起点 AtspiElement
     */
    private AtspiElement resolveStartElement(SearchCondition condition) {
        if (condition.getSearchFrom() != null) {
            Control parentControl = findControl(condition.getSearchFrom());
            return getAtspiElement(parentControl);
        }
        // 返回根元素
        return new AtspiElement(connection, "org.a11y.atspi.Registry",
                AtspiConnection.ROOT_PATH);
    }

    /**
     * 深度优先搜索 UI 树
     *
     * @param element    当前遍历的元素
     * @param condition  搜索条件
     * @param depth      当前深度
     * @param maxDepth   最大深度
     * @param foundIndex 已匹配计数
     * @return 匹配的 AtspiElement，如果未找到返回 null
     */
    private AtspiElement searchElement(AtspiElement element, SearchCondition condition,
                                        int depth, int maxDepth, int[] foundIndex) {
        if (depth > maxDepth) {
            return null;
        }

        // 检查当前元素是否匹配
        if (matchesCondition(element, condition)) {
            if (condition.getFoundIndex() <= 0 || foundIndex[0] == condition.getFoundIndex()) {
                return element;
            }
            foundIndex[0]++;
        }

        // 递归搜索子元素
        List<AtspiElement> children = element.getChildren();
        for (AtspiElement child : children) {
            AtspiElement found = searchElement(child, condition, depth + 1, maxDepth, foundIndex);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    /**
     * 检查元素是否匹配搜索条件
     *
     * @param element   AtspiElement
     * @param condition 搜索条件
     * @return 是否匹配
     */
    private boolean matchesCondition(AtspiElement element, SearchCondition condition) {
        // 1. 控件类型匹配
        if (condition.getControlType() != null) {
            int role = element.getRole();
            ControlType actualType = ControlType.fromAtspiRole(role);
            if (actualType != condition.getControlType()) {
                return false;
            }
        }

        // 2. 精确名称匹配
        if (condition.getName() != null) {
            String name = element.getName();
            if (!condition.getName().equals(name)) {
                return false;
            }
        }

        // 3. 子串名称匹配
        if (condition.getSubName() != null) {
            String name = element.getName();
            if (name == null || !name.contains(condition.getSubName())) {
                return false;
            }
        }

        // 4. 正则名称匹配
        if (condition.getRegexName() != null) {
            String name = element.getName();
            if (name == null || !name.matches(condition.getRegexName())) {
                return false;
            }
        }

        // 5. AutomationId 匹配（Linux 使用 AT-SPI 属性）
        if (condition.getAutomationId() != null) {
            // AT-SPI2 没有直接的 AutomationId，尝试通过属性匹配
            // 暂时跳过此条件
        }

        // 6. 自定义比较器
        if (condition.getCompare() != null) {
            if (!condition.getCompare().test(element, element.getRole())) {
                return false;
            }
        }

        return true;
    }

    /**
     * 从 Control 对象提取 AtspiElement
     *
     * @param control Control 对象
     * @return AtspiElement
     */
    private AtspiElement getAtspiElement(Control control) {
        Object nativeElement = control.getNativeElement();
        if (nativeElement instanceof AtspiElement) {
            return (AtspiElement) nativeElement;
        }
        throw new AutomationException("控件未绑定 Linux AtspiElement: " + control.getSearchCondition());
    }
}
