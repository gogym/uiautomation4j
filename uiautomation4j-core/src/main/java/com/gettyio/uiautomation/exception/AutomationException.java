package com.gettyio.uiautomation.exception;

/**
 * UIAutomation 基础异常类
 */
public class AutomationException extends RuntimeException {

    public AutomationException(String message) {
        super(message);
    }

    public AutomationException(String message, Throwable cause) {
        super(message, cause);
    }
}
