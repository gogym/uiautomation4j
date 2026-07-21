package com.gettyio.uiautomation.pattern;

/**
 * WindowPattern - 用于窗口级操作
 * 支持控件：Window
 */
public interface WindowPattern extends Pattern {

    /**
     * 关闭窗口
     */
    void close();

    /**
     * 是否可以关闭
     */
    boolean canClose();

    /**
     * 最大化窗口
     */
    void maximize();

    /**
     * 最小化窗口
     */
    void minimize();

    /**
     * 恢复窗口
     */
    void restore();

    /**
     * 窗口是否置顶
     */
    boolean isTopmost();

    /**
     * 设置窗口置顶
     */
    void setTopmost(boolean topmost);

    /**
     * 获取窗口视觉状态（Normal, Minimized, Maximized）
     */
    int getVisualState();
}
