package io.getbit.uiautomation.exception;

/**
 * 控件未找到异常
 *
 * <p>当通过 {@link io.getbit.uiautomation.spi.ControlBackend#findControl} 搜索控件失败时抛出。
 * 通常在以下情况发生：</p>
 * <ul>
 *   <li>搜索条件不匹配任何元素</li>
 *   <li>元素在超时时间内未出现</li>
 *   <li>搜索起点元素无效</li>
 * </ul>
 *
 * <p>异常信息中会包含搜索条件的描述，便于调试。</p>
 */
public class ControlNotFoundException extends AutomationException {

    /**
     * 构造异常
     *
     * @param message 异常描述信息（通常包含搜索条件）
     */
    public ControlNotFoundException(String message) {
        super(message);
    }

    /**
     * 构造异常（包含原始异常）
     *
     * @param message 异常描述信息
     * @param cause   原始异常
     */
    public ControlNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
