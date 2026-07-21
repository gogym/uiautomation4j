package io.getbit.uiautomation.mac;

import io.getbit.uiautomation.condition.SearchCondition;
import io.getbit.uiautomation.control.Control;
import io.getbit.uiautomation.enums.ControlType;
import io.getbit.uiautomation.exception.AutomationException;
import io.getbit.uiautomation.exception.ControlNotFoundException;
import io.getbit.uiautomation.mac.ax.*;
import io.getbit.uiautomation.mac.pattern.*;
import io.getbit.uiautomation.pattern.*;
import io.getbit.uiautomation.spi.ControlBackend;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.PointerByReference;

import java.util.List;

/**
 * macOS 平台的 ControlBackend SPI 实现
 *
 * <p>通过 JNA 调用 macOS Accessibility API (AXUIElement) 实现控件搜索和操作。
 * 等价于 Windows 平台的 {@code WinControlBackend}。</p>
 *
 * <h3>搜索策略（与 Windows 类似）：</h3>
 * <ol>
 *   <li>从根元素（SystemWide 或 searchFrom 指定的元素）开始遍历</li>
 *   <li>优先使用 AXRole 过滤（等价于 Windows ControlType）</li>
 *   <li>使用 AXTitle/AXDescription 进行名称匹配</li>
 *   <li>支持深度限制（searchDepth）</li>
 *   <li>Java 层二次过滤：subName（子串匹配）、regexName（正则匹配）、compare（自定义比较器）</li>
 * </ol>
 *
 * <h3>与 Windows 的差异：</h3>
 * <ul>
 *   <li>macOS 没有 AutomationId，使用 AXIdentifier 替代（部分控件不支持）</li>
 *   <li>macOS 坐标系原点在屏幕左下角（Windows 在左上角）</li>
 *   <li>macOS 需要辅助功能授权（系统设置 → 隐私与安全 → 辅助功能）</li>
 *   <li>macOS 没有 className 概念，使用 AXRole + AXSubrole 替代</li>
 * </ul>
 *
 * @see io.getbit.uiautomation.win.WinControlBackend
 */
public class MacControlBackend implements ControlBackend {

    /** 系统级根元素（桌面），等价于 Windows 的 IUIAutomation::GetRootElement */
    private AXUIElement systemWideElement;

    /** 全局搜索超时（秒） */
    private int globalSearchTimeout = 10;

    /**
     * 构造 MacControlBackend
     * <p>创建系统级 AXUIElement 作为搜索根节点</p>
     */
    public MacControlBackend() {
        Pointer systemWideRef = ApplicationServices.INSTANCE.AXUIElementCreateSystemWide();
        this.systemWideElement = new AXUIElement(systemWideRef, true);
    }

    /**
     * 根据搜索条件查找控件
     *
     * <p>搜索流程：
     * <ol>
     *   <li>确定搜索起点（searchFrom 或系统根元素）</li>
     *   <li>深度优先遍历 UI 树</li>
     *   <li>通过 AXRole 进行类型过滤</li>
     *   <li>通过 AXTitle/AXDescription 进行名称匹配</li>
     *   <li>Java 层二次过滤（subName、regexName、compare）</li>
     *   <li>包装为对应的 Control 子类返回</li>
     * </ol>
     *
     * @param condition 搜索条件
     * @return 找到的控件
     * @throws ControlNotFoundException 如果找不到匹配的控件
     */
    @Override
    public Control findControl(SearchCondition condition) {
        AXUIElement startElement = resolveStartElement(condition);
        int maxDepth = condition.getSearchDepth() > 0 ? condition.getSearchDepth() : Integer.MAX_VALUE;

        AXUIElement found = searchElement(startElement, condition, 0, maxDepth, new int[]{0});
        if (found == null) {
            throw new ControlNotFoundException(condition.toString());
        }

        return MacControlFactory.createControl(found, condition);
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
                AXUIElement startElement = resolveStartElement(condition);
                int maxDepth = condition.getSearchDepth() > 0 ? condition.getSearchDepth() : Integer.MAX_VALUE;
                AXUIElement found = searchElement(startElement, condition, 0, maxDepth, new int[]{0});
                if (found != null) {
                    found.close();
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
        AXUIElement element = getAXElement(control);
        return new MacValuePattern(element);
    }

    @Override
    public WindowPattern getWindowPattern(Control control) {
        AXUIElement element = getAXElement(control);
        return new MacWindowPattern(element);
    }

    @Override
    public InvokePattern getInvokePattern(Control control) {
        AXUIElement element = getAXElement(control);
        return new MacInvokePattern(element);
    }

    @Override
    public SelectionPattern getSelectionPattern(Control control) {
        AXUIElement element = getAXElement(control);
        return new MacSelectionPattern(element);
    }

    @Override
    public ScrollPattern getScrollPattern(Control control) {
        AXUIElement element = getAXElement(control);
        return new MacScrollPattern(element);
    }

    @Override
    public ExpandCollapsePattern getExpandCollapsePattern(Control control) {
        AXUIElement element = getAXElement(control);
        return new MacExpandCollapsePattern(element);
    }

    @Override
    public TransformPattern getTransformPattern(Control control) {
        AXUIElement element = getAXElement(control);
        return new MacTransformPattern(element);
    }

    // ==================== 操作 ====================

    /**
     * 发送按键
     * <p>通过 macOS CGEvent API 模拟键盘输入</p>
     */
    @Override
    public void sendKeys(Control control, String keys) {
        // 先设置焦点
        AXUIElement element = getAXElement(control);
        element.performAction(AXAction.FOCUS);
        // 通过 Keyboard 工具类发送按键
        io.getbit.uiautomation.mac.keyboard.Keyboard.sendKeys(keys);
    }

    /**
     * 点击控件
     * <p>获取控件中心坐标，通过 CGEvent 模拟鼠标点击</p>
     */
    @Override
    public void click(Control control) {
        AXUIElement element = getAXElement(control);
        int[] rect = element.getBoundingRectangle();
        int centerX = rect[0] + rect[2] / 2;
        int centerY = rect[1] + rect[3] / 2;
        io.getbit.uiautomation.mac.mouse.Mouse.click(centerX, centerY);
    }

    @Override
    public void doubleClick(Control control) {
        AXUIElement element = getAXElement(control);
        int[] rect = element.getBoundingRectangle();
        int centerX = rect[0] + rect[2] / 2;
        int centerY = rect[1] + rect[3] / 2;
        io.getbit.uiautomation.mac.mouse.Mouse.doubleClick(centerX, centerY);
    }

    @Override
    public void rightClick(Control control) {
        AXUIElement element = getAXElement(control);
        int[] rect = element.getBoundingRectangle();
        int centerX = rect[0] + rect[2] / 2;
        int centerY = rect[1] + rect[3] / 2;
        io.getbit.uiautomation.mac.mouse.Mouse.rightClick(centerX, centerY);
    }

    @Override
    public void captureToImage(Control control, String filePath) {
        AXUIElement element = getAXElement(control);
        int[] rect = element.getBoundingRectangle();
        io.getbit.uiautomation.mac.screenshot.Screenshot.captureRegion(
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
        AXUIElement element = getAXElement(control);
        return element.getBooleanAttribute(AXAttribute.ENABLED);
    }

    @Override
    public boolean isVisible(Control control) {
        // macOS 没有直接的 AXVisible 属性，通过边界矩形判断
        AXUIElement element = getAXElement(control);
        int[] rect = element.getBoundingRectangle();
        return rect[2] > 0 && rect[3] > 0; // 宽高大于 0 即认为可见
    }

    // ==================== 内部搜索方法 ====================

    /**
     * 确定搜索起点元素
     *
     * @param condition 搜索条件
     * @return 起点 AXUIElement
     */
    private AXUIElement resolveStartElement(SearchCondition condition) {
        if (condition.getSearchFrom() != null) {
            // 如果有 searchFrom，先找到父控件，然后从其 nativeElement 开始搜索
            Control parentControl = findControl(condition.getSearchFrom());
            return getAXElement(parentControl);
        }
        return systemWideElement;
    }

    /**
     * 深度优先搜索 UI 树
     *
     * <p>搜索策略：
     * <ol>
     *   <li>检查当前元素是否匹配条件</li>
     *   <li>如果未超过深度限制，递归搜索子元素</li>
     *   <li>匹配条件包括：role（类型）、name（名称）、subName（子串）、regexName（正则）</li>
     * </ol>
     *
     * @param element     当前遍历的元素
     * @param condition   搜索条件
     * @param depth       当前深度
     * @param maxDepth    最大深度
     * @param foundIndex  已匹配计数（用于 foundIndex 条件）
     * @return 匹配的 AXUIElement，如果未找到返回 null
     */
    private AXUIElement searchElement(AXUIElement element, SearchCondition condition,
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
        List<AXUIElement> children = element.getChildren();
        for (AXUIElement child : children) {
            AXUIElement found = searchElement(child, condition, depth + 1, maxDepth, foundIndex);
            if (found != null) {
                return found;
            }
        }
        return null;
    }

    /**
     * 检查元素是否匹配搜索条件
     *
     * <p>匹配规则：
     * <ul>
     *   <li>controlType → 比较 AXRole 映射后的 ControlType</li>
     *   <li>name → 精确匹配 AXTitle/AXDescription</li>
     *   <li>subName → 子串匹配（Java 层）</li>
     *   <li>regexName → 正则匹配（Java 层）</li>
     *   <li>className → macOS 不支持，跳过</li>
     *   <li>automationId → 匹配 AXIdentifier</li>
     * </ul>
     *
     * @param element   AXUIElement
     * @param condition 搜索条件
     * @return 是否匹配
     */
    private boolean matchesCondition(AXUIElement element, SearchCondition condition) {
        // 1. 控件类型匹配
        if (condition.getControlType() != null) {
            String role = element.getRole();
            ControlType actualType = ControlType.fromMacRole(role);
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

        // 3. 子串名称匹配（Java 层二次过滤）
        if (condition.getSubName() != null) {
            String name = element.getName();
            if (name == null || !name.contains(condition.getSubName())) {
                return false;
            }
        }

        // 4. 正则名称匹配（Java 层二次过滤）
        if (condition.getRegexName() != null) {
            String name = element.getName();
            if (name == null || !name.matches(condition.getRegexName())) {
                return false;
            }
        }

        // 5. AutomationId 匹配（macOS 使用 AXIdentifier）
        if (condition.getAutomationId() != null) {
            String identifier = element.getStringAttribute(AXAttribute.IDENTIFIER);
            if (!condition.getAutomationId().equals(identifier)) {
                return false;
            }
        }

        // 6. 自定义比较器（Java 层二次过滤）
        if (condition.getCompare() != null) {
            if (!condition.getCompare().test(element.getRef(), 0)) {
                return false;
            }
        }

        return true;
    }

    /**
     * 从 Control 对象提取 AXUIElement
     *
     * @param control Control 对象
     * @return AXUIElement
     */
    private AXUIElement getAXElement(Control control) {
        Object nativeElement = control.getNativeElement();
        if (nativeElement instanceof AXUIElement) {
            return (AXUIElement) nativeElement;
        }
        throw new AutomationException("控件未绑定 macOS AXUIElement: " + control.getSearchCondition());
    }
}
