package com.gettyio.uiautomation.pattern;

/**
 * ScrollPattern - 用于滚动操作
 *
 * <p>对应 UIAutomation 的 {@code IUIAutomationScrollPattern} (PatternId: 10004)。
 * 参考文档: https://learn.microsoft.com/en-us/windows/win32/api/uiautomationclient/nn-uiautomationclient-iuiautomationscrollpattern</p>
 *
 * <p>支持控件：ScrollBars、List、DataGrid 等</p>
 *
 * <p>滚动方向常量对应 UIAutomation 的 {@code ScrollAmount} 枚举：</p>
 * <ul>
 *   <li>{@code SCROLL_DIRECTION_NO_AMOUNT (0)} - 不滚动</li>
 *   <li>{@code SCROLL_DIRECTION_SMALL_DECREMENT (1)} - 小幅减少（如点击箭头）</li>
 *   <li>{@code SCROLL_DIRECTION_SMALL_INCREMENT (2)} - 小幅增加</li>
 *   <li>{@code SCROLL_DIRECTION_LARGE_DECREMENT (3)} - 大幅减少（如点击滚动条空白区）</li>
 *   <li>{@code SCROLL_DIRECTION_LARGE_INCREMENT (4)} - 大幅增加</li>
 * </ul>
 */
public interface ScrollPattern extends Pattern {

    /** 滚动方向：向左 */
    int SCROLL_DIRECTION_LEFT = 0;
    /** 滚动方向：向右 */
    int SCROLL_DIRECTION_RIGHT = 1;
    /** 滚动方向：向上 */
    int SCROLL_DIRECTION_UP = 2;
    /** 滚动方向：向下 */
    int SCROLL_DIRECTION_DOWN = 3;
    /** 滚动量：不滚动 */
    int SCROLL_DIRECTION_NO_AMOUNT = 0;
    /** 滚动量：小幅减少（如点击箭头按钮） */
    int SCROLL_DIRECTION_SMALL_DECREMENT = 1;
    /** 滚动量：小幅增加 */
    int SCROLL_DIRECTION_SMALL_INCREMENT = 2;
    /** 滚动量：大幅减少（如点击滚动条空白区域） */
    int SCROLL_DIRECTION_LARGE_DECREMENT = 3;
    /** 滚动量：大幅增加 */
    int SCROLL_DIRECTION_LARGE_INCREMENT = 4;

    /**
     * 滚动
     * <p>对应 COM 方法: {@code IUIAutomationScrollPattern::Scroll(ScrollAmount, ScrollAmount)}</p>
     * <p>参数使用 ScrollAmount 枚举值（如 SMALL_DECREMENT=1）。</p>
     *
     * @param horizontalDirection 水平滚动量枚举值
     * @param verticalDirection   垂直滚动量枚举值
     * @param horizontalAmount    水平滚动量（当使用百分比时）
     * @param verticalAmount      垂直滚动量（当使用百分比时）
     */
    void scroll(int horizontalDirection, int verticalDirection,
                double horizontalAmount, double verticalAmount);

    /**
     * 设置滚动位置百分比
     * <p>对应 COM 方法: {@code IUIAutomationScrollPattern::SetScrollPercent(double, double)}</p>
     *
     * @param horizontalPercent 水平百分比 (0.0 - 100.0)
     * @param verticalPercent   垂直百分比 (0.0 - 100.0)
     */
    void setScrollPercent(double horizontalPercent, double verticalPercent);

    /**
     * 获取水平滚动百分比
     * <p>对应 COM 属性: {@code get_CurrentHorizontalScrollPercent(double* pPercent)}</p>
     *
     * @return 水平滚动百分比 (0.0 - 100.0)
     */
    double getHorizontalScrollPercent();

    /**
     * 获取垂直滚动百分比
     * <p>对应 COM 属性: {@code get_CurrentVerticalScrollPercent(double* pPercent)}</p>
     *
     * @return 垂直滚动百分比 (0.0 - 100.0)
     */
    double getVerticalScrollPercent();

    /**
     * 是否可水平滚动
     * <p>对应 COM 属性: {@code get_CurrentHorizontallyScrollable(BOOL* pScrollable)}</p>
     *
     * @return true 表示支持水平滚动
     */
    boolean isHorizontallyScrollable();

    /**
     * 是否可垂直滚动
     * <p>对应 COM 属性: {@code get_CurrentVerticallyScrollable(BOOL* pScrollable)}</p>
     *
     * @return true 表示支持垂直滚动
     */
    boolean isVerticallyScrollable();
}
