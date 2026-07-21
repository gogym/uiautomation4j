package com.gettyio.uiautomation.pattern;

/**
 * TransformPattern - 用于移动、调整大小、旋转操作
 * 支持控件：Window、自定义控件等
 */
public interface TransformPattern extends Pattern {

    /**
     * 移动控件到指定位置
     *
     * @param x X 坐标
     * @param y Y 坐标
     */
    void move(int x, int y);

    /**
     * 调整控件大小
     *
     * @param width  宽度
     * @param height 高度
     */
    void resize(int width, int height);

    /**
     * 旋转控件
     *
     * @param degrees 旋转角度
     */
    void rotate(double degrees);

    /**
     * 是否可以移动
     */
    boolean canMove();

    /**
     * 是否可以调整大小
     */
    boolean canResize();

    /**
     * 是否可以旋转
     */
    boolean canRotate();
}
