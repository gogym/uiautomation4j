package io.getbit.uiautomation.linux.dbus;

import org.freedesktop.dbus.interfaces.DBusInterface;

/**
 * AT-SPI2 Text 接口 - D-Bus 绑定
 *
 * <p>对应 D-Bus 接口 {@code org.a11y.atspi.Text}，提供文本内容的读取和编辑能力。
 * 适用于文本标签、编辑框、文档等包含文本的 UI 元素。</p>
 *
 * <p>等价于 Windows 的 {@code IUIAutomationTextPattern} 和 macOS 的 AXValue（文本类型）。</p>
 *
 * <h3>D-Bus 接口信息：</h3>
 * <ul>
 *   <li>接口名: org.a11y.atspi.Text</li>
 *   <li>支持控件: Label、Text Entry、Document 等</li>
 * </ul>
 */
public interface AtspiText extends DBusInterface {

    /**
     * 获取指定范围的文本
     * <p>D-Bus 方法: GetText(ii) → s</p>
     *
     * @param startOffset 起始偏移量（从 0 开始）
     * @param endOffset   结束偏移量（-1 表示到末尾）
     * @return 文本内容
     */
    String GetText(int startOffset, int endOffset);

    /**
     * 设置文本内容
     * <p>D-Bus 方法: SetText(s) → b</p>
     *
     * @param text 新文本内容
     * @return 是否设置成功
     */
    boolean SetText(String text);

    /**
     * 获取文本总长度
     * <p>D-Bus 属性: CharacterCount → i</p>
     *
     * @return 字符数
     */
    int GetCharacterCount();

    /**
     * 获取光标位置
     * <p>D-Bus 属性: CaretOffset → i</p>
     *
     * @return 光标偏移量
     */
    int GetCaretOffset();

    /**
     * 在指定范围插入文本
     * <p>D-Bus 方法: InsertText(i,s,i) → b</p>
     *
     * @param position 插入位置
     * @param text     要插入的文本
     * @param length   文本长度（-1 表示自动计算）
     * @return 是否成功
     */
    boolean InsertText(int position, String text, int length);

    /**
     * 删除指定范围的文本
     * <p>D-Bus 方法: DeleteText(ii) → b</p>
     *
     * @param startOffset 起始偏移
     * @param endOffset   结束偏移
     * @return 是否成功
     */
    boolean DeleteText(int startOffset, int endOffset);
}
