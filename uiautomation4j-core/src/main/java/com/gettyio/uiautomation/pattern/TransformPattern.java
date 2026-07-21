package com.gettyio.uiautomation.pattern;

/**
 * TransformPattern - 用于移动、调整大小、旋转操作
 *
 * <p>对应 UIAutomation 的 {@code IUIAutomationTransformPattern} (PatternId: 10016)。
 * 参考文档: https://learn.microsoft.com/en-us/windows/win32/api/uiautomationclient/nn-uiautomationclient-iuiautomationtransformpattern</p>
 *
 * <p>支持控件：Window、自定义控件等</p>
 *
 * <p>注意: 某些控件可能不支持全部操作，通过 {@code canMove()}、{@code canResize()}、{@code canRotate()}
 * 检查控件支持哪些操作。</p>
 */
public interface TransformPattern extends Pattern {

    /**
     * 移动控件到指定位置
     * <p>对应 COM 方法: {@code IUIAutomationTransformPattern::Move(double x, double y)}</p>
     *
     * @param x X 坐标（屏幕坐标）
     * @param y Y 坐标（屏幕坐标）
     */
    void move(int x, int y);

    /**
     * 调整控件大小
     * <p>对应 COM 方法: {@code IUIAutomationTransformPattern::Resize(double width, double height)}</p>
     *
     * @param width  新宽度（像素）
     * @param height 新高度（像素）
     */
    void resize(int width, int height);

    /**
     * 旋转控件
     * <p>对应 COM 方法: {@code IUIAutomationTransformPattern::Rotate(double degrees)}</p>
     *
     * @param degrees 旋转角度（顺时针）
     */
    void rotate(double degrees);

    /**
     * 是否可以移动
     * <p>对应 COM 属性: {@code get_CurrentCanMove(BOOL* pCanMove)}</p>
     *
     * @return true 表示控件可以移动
     */
    boolean canMove();

    /**
     * 是否可以调整大小
     * <p>对应 COM 属性: {@code get_CurrentCanResize(BOOL* pCanResize)}</p>
     *
     * @return true 表示控件可以调整大小
     */
    boolean canResize();

    /**
     * 是否可以旋转
     * <p>对应 COM 属性: {@code get_CurrentCanRotate(BOOL* pCanRotate)}</p>
     *
     * @return true 表示控件可以旋转
     */
    boolean canRotate();
}
