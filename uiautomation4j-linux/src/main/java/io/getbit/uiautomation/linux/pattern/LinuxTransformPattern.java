package io.getbit.uiautomation.linux.pattern;

import io.getbit.uiautomation.linux.ax.AtspiConstants;
import io.getbit.uiautomation.linux.ax.AtspiElement;
import io.getbit.uiautomation.pattern.TransformPattern;

/**
 * TransformPattern 的 Linux 实现
 *
 * <p>AT-SPI2 对窗口移动和调整大小的支持有限。
 * 此实现通过 Component 接口获取几何信息，但移动/调整操作依赖窗口管理器的支持。</p>
 *
 * <p>注意：AT-SPI2 不直接支持窗口旋转操作。</p>
 */
public class LinuxTransformPattern implements TransformPattern {

    /** 底层 AtspiElement 引用 */
    private final AtspiElement element;

    /**
     * 构造 LinuxTransformPattern
     *
     * @param element AtspiElement 元素
     */
    public LinuxTransformPattern(AtspiElement element) {
        this.element = element;
    }

    /**
     * 移动元素到指定位置
     * <p>AT-SPI2 不直接支持移动操作，需要通过窗口管理器 API 实现</p>
     *
     * @param x 目标 X 坐标
     * @param y 目标 Y 坐标
     */
    @Override
    public void move(int x, int y) {
        // AT-SPI2 不直接支持移动窗口
        // 需要通过 X11/Wayland API 实现
        throw new UnsupportedOperationException(
                "AT-SPI2 不支持直接移动窗口，需要通过 X11/Wayland API 实现");
    }

    /**
     * 调整元素大小
     * <p>AT-SPI2 不直接支持调整大小操作</p>
     *
     * @param width  新宽度
     * @param height 新高度
     */
    @Override
    public void resize(int width, int height) {
        // AT-SPI2 不直接支持调整窗口大小
        throw new UnsupportedOperationException(
                "AT-SPI2 不支持直接调整窗口大小，需要通过 X11/Wayland API 实现");
    }

    /**
     * 旋转元素
     * <p>Linux 桌面不支持窗口旋转操作</p>
     *
     * @param degrees 旋转角度
     */
    @Override
    public void rotate(double degrees) {
        throw new UnsupportedOperationException("Linux 不支持窗口旋转操作");
    }

    /**
     * 是否可以移动
     * <p>窗口元素通常可以移动</p>
     *
     * @return 如果元素是窗口类型返回 true
     */
    @Override
    public boolean canMove() {
        int role = element.getRole();
        return role == AtspiConstants.ROLE_FRAME || role == AtspiConstants.ROLE_DIALOG;
    }

    /**
     * 是否可以调整大小
     * <p>窗口元素通常可以调整大小</p>
     *
     * @return 如果元素是窗口类型返回 true
     */
    @Override
    public boolean canResize() {
        int role = element.getRole();
        return role == AtspiConstants.ROLE_FRAME || role == AtspiConstants.ROLE_DIALOG;
    }

    /**
     * 是否可以旋转
     * <p>Linux 不支持旋转</p>
     *
     * @return 始终返回 false
     */
    @Override
    public boolean canRotate() {
        return false;
    }
}
