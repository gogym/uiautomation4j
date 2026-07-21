package io.getbit.uiautomation.exception;

/**
 * UIAutomation 基础异常类
 *
 * <p>所有 UI 自动化操作中抛出的运行时异常的基类。
 * 继承自 {@link RuntimeException}，不需要在方法签名中显式声明。</p>
 *
 * <p>常见场景：</p>
 * <ul>
 *   <li>COM 对象无效时调用 vtable 方法</li>
 *   <li>COM 初始化失败</li>
 *   <li>控件等待就绪超时</li>
 *   <li>GUID 解析失败</li>
 * </ul>
 *
 * @see ControlNotFoundException 控件未找到异常
 */
public class AutomationException extends RuntimeException {

    /**
     * 构造异常
     *
     * @param message 异常描述信息
     */
    public AutomationException(String message) {
        super(message);
    }

    /**
     * 构造异常（包含原始异常）
     *
     * @param message 异常描述信息
     * @param cause   原始异常
     */
    public AutomationException(String message, Throwable cause) {
        super(message, cause);
    }
}
