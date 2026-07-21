package com.gettyio.uiautomation.exception;

/**
 * 控件未找到异常
 */
public class ControlNotFoundException extends AutomationException {

    public ControlNotFoundException(String message) {
        super(message);
    }

    public ControlNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
