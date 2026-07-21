package io.getbit.uiautomation.pattern;

/**
 * Pattern 基础接口（标记接口）
 *
 * <p>所有 UIAutomation Pattern 接口的公共父接口。
 * UIAutomation 中的 Pattern 定义了控件的特定行为能力，
 * 不同类型的控件支持不同的 Pattern。</p>
 *
 * <p>例如：</p>
 * <ul>
 *   <li>{@link ValuePattern} - 获取/设置控件值（Edit、Text）</li>
 *   <li>{@link WindowPattern} - 窗口操作（Window）</li>
 *   <li>{@link InvokePattern} - 调用操作（Button、MenuItem）</li>
 *   <li>{@link SelectionPattern} - 选中项获取（List、ComboBox）</li>
 *   <li>{@link ScrollPattern} - 滚动操作（ScrollBar、List）</li>
 *   <li>{@link ExpandCollapsePattern} - 展开/折叠（TreeItem）</li>
 *   <li>{@link TransformPattern} - 移动/缩放/旋转（Window）</li>
 * </ul>
 *
 * <p>通过 {@code Control.getXxxPattern()} 方法获取控件支持的 Pattern。</p>
 */
public interface Pattern {
}
