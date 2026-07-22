package io.getbit.uiautomation.spi;

import io.getbit.uiautomation.condition.SearchCondition;
import io.getbit.uiautomation.control.Control;
import io.getbit.uiautomation.enums.ControlType;
import io.getbit.uiautomation.pattern.*;

import java.util.List;

/**
 * 控件后端 SPI 接口
 * 由具体平台实现（如 Windows 通过 JNA+COM 实现）
 */
public interface ControlBackend {

    /**
     * 根据搜索条件查找控件
     *
     * @param condition 搜索条件
     * @return 找到的控件，找不到抛出 ControlNotFoundException
     */
    Control findControl(SearchCondition condition);

    /**
     * 检查控件是否存在
     *
     * @param condition          搜索条件
     * @param maxWaitSeconds     最大等待秒数
     * @return 是否存在
     */
    boolean exists(SearchCondition condition, int maxWaitSeconds);

    /**
     * 获取控件的 ValuePattern
     */
    ValuePattern getValuePattern(Control control);

    /**
     * 获取控件的 WindowPattern
     */
    WindowPattern getWindowPattern(Control control);

    /**
     * 获取控件的 InvokePattern
     */
    InvokePattern getInvokePattern(Control control);

    /**
     * 获取控件的 SelectionPattern
     */
    SelectionPattern getSelectionPattern(Control control);

    /**
     * 获取控件的 ScrollPattern
     */
    ScrollPattern getScrollPattern(Control control);

    /**
     * 获取控件的 ExpandCollapsePattern
     */
    ExpandCollapsePattern getExpandCollapsePattern(Control control);

    /**
     * 获取控件的 TransformPattern
     */
    TransformPattern getTransformPattern(Control control);

    /**
     * 发送按键
     */
    void sendKeys(Control control, String keys);

    /**
     * 点击控件
     */
    void click(Control control);

    /**
     * 双击控件
     */
    void doubleClick(Control control);

    /**
     * 右键点击控件
     */
    void rightClick(Control control);

    /**
     * 截图并保存到文件
     */
    void captureToImage(Control control, String filePath);

    /**
     * 设置全局搜索超时（秒）
     */
    void setGlobalSearchTimeout(int seconds);

    /**
     * 获取全局搜索超时（秒）
     */
    int getGlobalSearchTimeout();

    /**
     * 等待控件就绪（可见且可交互）
     *
     * @param control      控件
     * @param maxWaitSeconds 最大等待秒数
     * @return true 表示控件已就绪
     */
    boolean waitReady(Control control, int maxWaitSeconds);

    /**
     * 检查控件是否启用
     *
     * @param control 控件
     * @return 是否启用
     */
    boolean isEnabled(Control control);

    /**
     * 检查控件是否可见（不在屏幕外）
     *
     * @param control 控件
     * @return 是否可见
     */
    boolean isVisible(Control control);

    // ==================== 批量查找与树遍历 ====================

    /**
     * 查找所有匹配条件的控件
     *
     * @param condition 搜索条件
     * @return 所有匹配的控件列表，如果没有匹配返回空列表
     */
    List<Control> findControls(SearchCondition condition);

    /**
     * 获取指定控件的所有直接子控件
     *
     * @param parent 父控件
     * @return 子控件列表，如果没有子控件返回空列表
     */
    List<Control> getChildren(Control parent);

    // ==================== 原生属性直接访问 ====================

    /**
     * 直接从 native element 获取控件名称
     *
     * @param control 控件
     * @return 控件名称
     */
    String getElementName(Control control);

    /**
     * 直接从 native element 获取控件边界矩形
     *
     * @param control 控件
     * @return int[4] = {x, y, width, height}（Windows 为 {left, top, right, bottom}）
     */
    int[] getElementBoundingRectangle(Control control);

    /**
     * 直接从 native element 获取运行时 ID
     *
     * @param control 控件
     * @return 运行时 ID 数组，如果不可用返回空数组
     */
    int[] getElementRuntimeId(Control control);

    /**
     * 直接从 native element 获取控件类名
     *
     * @param control 控件
     * @return 控件类名（Windows 为 ClassName，macOS 为 AXRole+AXSubrole，Linux 为 RoleName）
     */
    String getElementClassName(Control control);

    /**
     * 直接从 native element 获取控件所属进程 ID
     *
     * @param control 控件
     * @return 进程 ID
     */
    int getElementProcessId(Control control);

    /**
     * 直接从 native element 获取控件实际类型
     *
     * @param control 控件
     * @return 控件类型枚举
     */
    ControlType getElementControlType(Control control);

    /**
     * 获取第一个子控件
     *
     * @param parent 父控件
     * @return 第一个子控件，如果没有返回 null
     */
    Control getFirstChild(Control parent);

    /**
     * 获取下一个兄弟控件
     *
     * @param control 当前控件
     * @return 下一个兄弟控件，如果没有返回 null
     */
    Control getNextSibling(Control control);
}
