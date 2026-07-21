package com.gettyio.uiautomation.pattern;

/**
 * ScrollPattern - 用于滚动操作
 * 支持控件：ScrollBars、List、DataGrid 等
 */
public interface ScrollPattern extends Pattern {

    int SCROLL_DIRECTION_LEFT = 0;
    int SCROLL_DIRECTION_RIGHT = 1;
    int SCROLL_DIRECTION_UP = 2;
    int SCROLL_DIRECTION_DOWN = 3;
    int SCROLL_DIRECTION_NO_AMOUNT = 0;
    int SCROLL_DIRECTION_SMALL_DECREMENT = 1;
    int SCROLL_DIRECTION_SMALL_INCREMENT = 2;
    int SCROLL_DIRECTION_LARGE_DECREMENT = 3;
    int SCROLL_DIRECTION_LARGE_INCREMENT = 4;

    /**
     * 滚动
     *
     * @param horizontalDirection 水平方向
     * @param verticalDirection   垂直方向
     * @param horizontalAmount    水平滚动量
     * @param verticalAmount      垂直滚动量
     */
    void scroll(int horizontalDirection, int verticalDirection,
                double horizontalAmount, double verticalAmount);

    /**
     * 设置滚动位置百分比
     *
     * @param horizontalPercent 水平百分比 (0.0 - 100.0)
     * @param verticalPercent   垂直百分比 (0.0 - 100.0)
     */
    void setScrollPercent(double horizontalPercent, double verticalPercent);

    /**
     * 获取水平滚动百分比
     */
    double getHorizontalScrollPercent();

    /**
     * 获取垂直滚动百分比
     */
    double getVerticalScrollPercent();

    /**
     * 是否可水平滚动
     */
    boolean isHorizontallyScrollable();

    /**
     * 是否可垂直滚动
     */
    boolean isVerticallyScrollable();
}
