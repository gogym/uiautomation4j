package com.gettyio.uiautomation.mac.ax;

import com.sun.jna.Pointer;
import com.sun.jna.platform.mac.CoreFoundation.CFStringRef;
import com.sun.jna.ptr.PointerByReference;

import java.util.ArrayList;
import java.util.List;

/**
 * macOS AXUIElement 的 Java 包装类
 *
 * <p>AXUIElement 是 macOS Accessibility API 的核心类型，代表一个 UI 元素。
 * 本类封装了对 AXUIElementRef 指针的操作，提供类型安全的属性访问和动作执行。</p>
 *
 * <p>等价于 Windows 的 {@code IUIAutomationElement} COM 接口。</p>
 *
 * <h3>核心能力：</h3>
 * <ul>
 *   <li>属性访问：通过 {@link #getAttribute(String)} 获取任意属性</li>
 *   <li>属性设置：通过 {@link #setAttribute(String, Pointer)} 修改属性</li>
 *   <li>动作执行：通过 {@link #performAction(String)} 执行操作</li>
 *   <li>子元素遍历：通过 {@link #getChildren()} 获取子元素列表</li>
 *   <li>几何信息：通过 {@link #getBoundingRectangle()} 获取边界矩形</li>
 * </ul>
 *
 * <h3>内存管理：</h3>
 * <p>实现 {@link AutoCloseable}，支持 try-with-resources 自动释放底层 AXUIElementRef。
 * 通过 Copy 语义获取的 AXUIElement 必须释放，通过 Get 语义获取的不需要释放。</p>
 *
 * @see <a href="https://developer.apple.com/documentation/applicationservices/axuielement">AXUIElement</a>
 */
public class AXUIElement implements AutoCloseable {

    /** 底层 AXUIElementRef 指针 */
    private Pointer elementRef;

    /** 标记是否需要释放（Copy 语义获取的需要释放） */
    private boolean ownsRef;

    /**
     * 构造 AXUIElement 包装
     *
     * @param elementRef AXUIElementRef 指针
     * @param ownsRef    是否拥有引用（true 表示 close() 时会 CFRelease）
     */
    public AXUIElement(Pointer elementRef, boolean ownsRef) {
        this.elementRef = elementRef;
        this.ownsRef = ownsRef;
    }

    /**
     * 获取底层 AXUIElementRef 指针
     *
     * @return AXUIElementRef 指针
     */
    public Pointer getRef() {
        return elementRef;
    }

    /**
     * 获取字符串属性
     *
     * @param attributeName 属性名（如 "AXRole", "AXTitle"）
     * @return 属性值字符串，如果属性不存在返回 null
     */
    public String getStringAttribute(String attributeName) {
        CFStringRef attrName = CFUtil.createCFString(attributeName);
        try {
            PointerByReference valueRef = new PointerByReference();
            int err = ApplicationServices.INSTANCE.AXUIElementCopyAttributeValue(
                    elementRef, attrName.getPointer(), valueRef);
            if (err != ApplicationServices.kAXErrorSuccess) {
                return null;
            }
            Pointer value = valueRef.getValue();
            try {
                return CFUtil.pointerToJavaString(value);
            } finally {
                CFUtil.release(value);
            }
        } finally {
            CFUtil.release(attrName);
        }
    }

    /**
     * 获取布尔属性
     *
     * @param attributeName 属性名（如 "AXEnabled", "AXMinimized"）
     * @return 属性值，如果属性不存在返回 false
     */
    public boolean getBooleanAttribute(String attributeName) {
        CFStringRef attrName = CFUtil.createCFString(attributeName);
        try {
            PointerByReference valueRef = new PointerByReference();
            int err = ApplicationServices.INSTANCE.AXUIElementCopyAttributeValue(
                    elementRef, attrName.getPointer(), valueRef);
            if (err != ApplicationServices.kAXErrorSuccess) {
                return false;
            }
            Pointer value = valueRef.getValue();
            try {
                return CFUtil.isCFBooleanTrue(value);
            } finally {
                CFUtil.release(value);
            }
        } finally {
            CFUtil.release(attrName);
        }
    }

    /**
     * 获取原始 CF 类型属性值
     * <p>返回底层指针，调用者负责释放。</p>
     *
     * @param attributeName 属性名
     * @return CFTypeRef 指针，如果属性不存在返回 null
     */
    public Pointer getAttribute(String attributeName) {
        CFStringRef attrName = CFUtil.createCFString(attributeName);
        try {
            PointerByReference valueRef = new PointerByReference();
            int err = ApplicationServices.INSTANCE.AXUIElementCopyAttributeValue(
                    elementRef, attrName.getPointer(), valueRef);
            if (err != ApplicationServices.kAXErrorSuccess) {
                return null;
            }
            return valueRef.getValue();
        } finally {
            CFUtil.release(attrName);
        }
    }

    /**
     * 设置属性值
     *
     * @param attributeName 属性名
     * @param value         CFTypeRef 值指针
     * @return 是否设置成功
     */
    public boolean setAttribute(String attributeName, Pointer value) {
        CFStringRef attrName = CFUtil.createCFString(attributeName);
        try {
            int err = ApplicationServices.INSTANCE.AXUIElementSetAttributeValue(
                    elementRef, attrName.getPointer(), value);
            return err == ApplicationServices.kAXErrorSuccess;
        } finally {
            CFUtil.release(attrName);
        }
    }

    /**
     * 执行动作
     *
     * @param actionName 动作名（如 "AXPress", "AXConfirm"）
     * @return 是否执行成功
     * @see AXAction
     */
    public boolean performAction(String actionName) {
        CFStringRef action = CFUtil.createCFString(actionName);
        try {
            int err = ApplicationServices.INSTANCE.AXUIElementPerformAction(
                    elementRef, action.getPointer());
            return err == ApplicationServices.kAXErrorSuccess;
        } finally {
            CFUtil.release(action);
        }
    }

    /**
     * 获取子元素列表
     *
     * @return 子元素列表
     */
    public List<AXUIElement> getChildren() {
        List<AXUIElement> children = new ArrayList<>();
        Pointer childrenArray = getAttribute(AXAttribute.CHILDREN);
        if (childrenArray != null && childrenArray != Pointer.NULL) {
            try {
                long count = CFUtil.getArrayCount(childrenArray);
                for (int i = 0; i < count; i++) {
                    Pointer childPtr = CFUtil.getArrayValue(childrenArray, i);
                    if (childPtr != null && childPtr != Pointer.NULL) {
                        children.add(new AXUIElement(childPtr, false));
                    }
                }
            } finally {
                CFUtil.release(childrenArray);
            }
        }
        return children;
    }

    /**
     * 获取元素的边界矩形
     * <p>macOS 坐标系原点在屏幕左下角（与 Windows 左上角不同）。</p>
     *
     * @return int[4] = {x, y, width, height}
     */
    public int[] getBoundingRectangle() {
        Pointer position = getAttribute(AXAttribute.POSITION);
        double x = 0, y = 0, width = 0, height = 0;
        if (position != null && position != Pointer.NULL) {
            try {
                double[] point = new double[2];
                if (ApplicationServices.INSTANCE.AXValueGetValue(position, 1, point)) {
                    x = point[0];
                    y = point[1];
                }
            } finally {
                CFUtil.release(position);
            }
        }
        Pointer size = getAttribute(AXAttribute.SIZE);
        if (size != null && size != Pointer.NULL) {
            try {
                double[] sizeVal = new double[2];
                if (ApplicationServices.INSTANCE.AXValueGetValue(size, 2, sizeVal)) {
                    width = sizeVal[0];
                    height = sizeVal[1];
                }
            } finally {
                CFUtil.release(size);
            }
        }
        return new int[]{(int) x, (int) y, (int) width, (int) height};
    }

    /**
     * 获取元素的角色（AXRole）
     *
     * @return 角色字符串（如 "AXButton", "AXWindow"）
     */
    public String getRole() {
        return getStringAttribute(AXAttribute.ROLE);
    }

    /**
     * 获取元素的名称
     * <p>依次尝试 AXTitle、AXDescription、AXValue</p>
     *
     * @return 名称字符串
     */
    public String getName() {
        String title = getStringAttribute(AXAttribute.TITLE);
        if (title != null && !title.isEmpty()) return title;
        String desc = getStringAttribute(AXAttribute.DESCRIPTION);
        if (desc != null && !desc.isEmpty()) return desc;
        return getStringAttribute(AXAttribute.VALUE);
    }

    /**
     * 释放底层 AXUIElementRef
     */
    @Override
    public void close() {
        if (ownsRef && elementRef != null && elementRef != Pointer.NULL) {
            CFUtil.release(elementRef);
            elementRef = null;
        }
    }
}
