package com.gettyio.uiautomation.condition;

import com.gettyio.uiautomation.enums.ControlType;

import java.util.function.BiPredicate;

/**
 * 搜索条件 - 基于 Builder 模式构建
 *
 * <p>用于描述控件搜索的各种过滤条件，支持链式调用。搜索条件最终由
 * {@link com.gettyio.uiautomation.spi.ControlBackend} 实现类转换为
 * COM 层的 {@code IUIAutomationCondition} 并执行搜索。</p>
 *
 * <p>条件匹配优先级（后端实现参考）：</p>
 * <ol>
 *   <li>精确名称 {@code name} → COM 属性条件（最高效）</li>
 *   <li>类名 {@code className} → COM 属性条件</li>
 *   <li>AutomationId {@code automationId} → COM 属性条件</li>
 *   <li>控件类型 {@code controlType} → COM 属性条件</li>
 *   <li>模糊名称 {@code subName} / 正则名称 {@code regexName} → Java 层二次过滤</li>
 *   <li>自定义比较器 {@code compare} → Java 层二次过滤</li>
 * </ol>
 *
 * <p>使用示例：</p>
 * <pre>
 * // 精确查找记事本窗口
 * SearchCondition condition = SearchCondition.builder()
 *     .name("Notepad")
 *     .className("Notepad")
 *     .controlType(ControlType.Window)
 *     .searchDepth(1)
 *     .build();
 *
 * // 模糊匹配 + 从父控件开始搜索
 * SearchCondition child = SearchCondition.builder()
 *     .subName("Save")
 *     .searchFrom(parentCondition)
 *     .build();
 * </pre>
 *
 * @see com.gettyio.uiautomation.control.Control
 * @see com.gettyio.uiautomation.enums.ControlType
 */
public class SearchCondition {

    // ==================== 搜索条件字段 ====================

    /** 精确名称，对应 UIA_NamePropertyId (30005)，完全匹配 */
    private String name;
    /** 模糊名称（子串匹配），在 Java 层进行二次过滤 */
    private String subName;
    /** 正则表达式名称，在 Java 层通过 {@code Pattern.matches()} 进行匹配 */
    private String regexName;
    /** 控件类名，对应 UIA_ClassNamePropertyId (30012)，如 "Button"、"Edit" */
    private String className;
    /** AutomationId，对应 UIA_AutomationIdPropertyId (30011)，控件唯一标识 */
    private String automationId;
    /** 控件类型，对应 UIA_ControlTypePropertyId (30003) */
    private ControlType controlType;
    /**
     * 元素在 UI 树中的深度（从搜索起点算起），
     * null 表示不限制深度。用于 TreeWalker 深度遍历。
     */
    private Integer depth;
    /** 搜索深度上限，默认 {@code Integer.MAX_VALUE}（无限制） */
    private int searchDepth = Integer.MAX_VALUE;
    /** 当匹配到多个控件时，取第几个（从 1 开始），默认取第 1 个 */
    private int foundIndex = 1;
    /** 搜索重试间隔（毫秒），默认 100ms，用于 {@code exists()} 轮询 */
    private long searchInterval = 100;
    /**
     * 自定义比较器，参数为 (nativeElement, controlType)。
     * 返回 true 表示该元素匹配条件。用于无法用属性表达的复杂匹配逻辑。
     */
    private BiPredicate<Object, Integer> compare;
    /**
     * 搜索起点条件，指定从哪个父控件开始搜索。
     * null 表示从桌面根元素开始搜索。
     */
    private SearchCondition searchFrom;

    /**
     * 私有构造方法，只能通过 {@link Builder#build()} 创建实例
     */
    private SearchCondition() {
    }

    /**
     * 创建 Builder 实例
     *
     * @return 新的 Builder 对象，用于链式构建搜索条件
     */
    public static Builder builder() {
        return new Builder();
    }

    // ==================== Getters ====================

    /** @return 精确名称条件，未设置返回 null */
    public String getName() { return name; }
    /** @return 模糊名称（子串）条件，未设置返回 null */
    public String getSubName() { return subName; }
    /** @return 正则表达式名称条件，未设置返回 null */
    public String getRegexName() { return regexName; }
    /** @return 控件类名条件，未设置返回 null */
    public String getClassName() { return className; }
    /** @return AutomationId 条件，未设置返回 null */
    public String getAutomationId() { return automationId; }
    /** @return 控件类型条件，未设置返回 null */
    public ControlType getControlType() { return controlType; }
    /** @return UI 树深度条件，未设置返回 null */
    public Integer getDepth() { return depth; }
    /** @return 搜索深度上限 */
    public int getSearchDepth() { return searchDepth; }
    /** @return 匹配到多个控件时的取值索引（从 1 开始） */
    public int getFoundIndex() { return foundIndex; }
    /** @return 搜索重试间隔（毫秒） */
    public long getSearchInterval() { return searchInterval; }
    /** @return 自定义比较器，未设置返回 null */
    public BiPredicate<Object, Integer> getCompare() { return compare; }
    /** @return 搜索起点条件，未设置返回 null（表示从桌面根元素搜索） */
    public SearchCondition getSearchFrom() { return searchFrom; }

    // ==================== 条件判断方法 ====================

    /** @return 是否设置了精确名称条件 */
    public boolean hasName() { return name != null; }
    /** @return 是否设置了模糊名称条件 */
    public boolean hasSubName() { return subName != null; }
    /** @return 是否设置了正则名称条件 */
    public boolean hasRegexName() { return regexName != null; }
    /** @return 是否设置了类名条件 */
    public boolean hasClassName() { return className != null; }
    /** @return 是否设置了 AutomationId 条件 */
    public boolean hasAutomationId() { return automationId != null; }
    /** @return 是否设置了控件类型条件 */
    public boolean hasControlType() { return controlType != null; }
    /** @return 是否设置了 UI 树深度条件 */
    public boolean hasDepth() { return depth != null; }
    /** @return 是否设置了自定义比较器 */
    public boolean hasCompare() { return compare != null; }

    /**
     * Builder 内部类 - 用于链式构建 {@link SearchCondition}
     *
     * <p>所有 setter 方法均返回 {@code this}，支持链式调用。
     * 最终通过 {@link #build()} 生成不可变的 SearchCondition 实例。</p>
     */
    public static class Builder {
        /** 正在构建的搜索条件实例 */
        private final SearchCondition condition = new SearchCondition();

        /**
         * 设置精确名称（完全匹配）
         * <p>对应 UIA_NamePropertyId (30005)
         *
         * @param name 控件的精确名称
         * @return this（链式调用）
         */
        public Builder name(String name) {
            condition.name = name;
            return this;
        }

        /**
         * 设置模糊名称（子串匹配）
         * <p>在 Java 层通过 {@code name.contains(subName)} 进行二次过滤
         *
         * @param subName 名称中包含的子串
         * @return this（链式调用）
         */
        public Builder subName(String subName) {
            condition.subName = subName;
            return this;
        }

        /**
         * 设置正则表达式名称
         * <p>在 Java 层通过 {@code Pattern.matches(regexName, name)} 进行匹配
         *
         * @param regexName 名称的正则表达式
         * @return this（链式调用）
         */
        public Builder regexName(String regexName) {
            condition.regexName = regexName;
            return this;
        }

        /**
         * 设置控件类名
         * <p>对应 UIA_ClassNamePropertyId (30012)
         *
         * @param className 控件类名（如 "Button"、"Edit"、"Notepad"）
         * @return this（链式调用）
         */
        public Builder className(String className) {
            condition.className = className;
            return this;
        }

        /**
         * 设置 AutomationId
         * <p>对应 UIA_AutomationIdPropertyId (30011)，是控件的唯一标识符
         *
         * @param automationId 控件的 AutomationId
         * @return this（链式调用）
         */
        public Builder automationId(String automationId) {
            condition.automationId = automationId;
            return this;
        }

        /**
         * 设置控件类型
         * <p>对应 UIA_ControlTypePropertyId (30003)
         *
         * @param controlType 控件类型枚举值
         * @return this（链式调用）
         * @see com.gettyio.uiautomation.enums.ControlType
         */
        public Builder controlType(ControlType controlType) {
            condition.controlType = controlType;
            return this;
        }

        /**
         * 设置 UI 树中的元素深度
         * <p>指定从搜索起点开始，在第几层查找目标元素。
         * 例如 depth=1 表示直接子元素。
         *
         * @param depth 元素深度（从 1 开始）
         * @return this（链式调用）
         */
        public Builder depth(int depth) {
            condition.depth = depth;
            return this;
        }

        /**
         * 设置搜索深度上限
         * <p>限制搜索的最大深度，避免在过深的 UI 树中搜索。
         * 默认 {@code Integer.MAX_VALUE}（无限制）。
         *
         * @param searchDepth 最大搜索深度
         * @return this（链式调用）
         */
        public Builder searchDepth(int searchDepth) {
            condition.searchDepth = searchDepth;
            return this;
        }

        /**
         * 设置匹配索引
         * <p>当搜索到多个匹配元素时，取第几个（从 1 开始）。
         * 默认 1，即取第一个匹配的元素。
         *
         * @param foundIndex 匹配索引（从 1 开始）
         * @return this（链式调用）
         */
        public Builder foundIndex(int foundIndex) {
            condition.foundIndex = foundIndex;
            return this;
        }

        /**
         * 设置搜索重试间隔
         * <p>用于 {@code exists()} 方法轮询时的等待间隔。
         * 默认 100ms。
         *
         * @param intervalMs 重试间隔（毫秒）
         * @return this（链式调用）
         */
        public Builder searchInterval(long intervalMs) {
            condition.searchInterval = intervalMs;
            return this;
        }

        /**
         * 设置自定义比较器
         * <p>当内置属性条件无法满足需求时，提供自定义匹配逻辑。
         * 比较器参数为 {@code (nativeElement, controlTypeId)}，返回 true 表示匹配。
         *
         * @param compare 自定义匹配逻辑
         * @return this（链式调用）
         */
        public Builder compare(BiPredicate<Object, Integer> compare) {
            condition.compare = compare;
            return this;
        }

        /**
         * 设置搜索起点
         * <p>指定从哪个父控件开始搜索，而非从桌面根元素开始。
         * 适用于在已知父控件范围内查找子控件的场景。
         *
         * @param searchFrom 父控件的搜索条件
         * @return this（链式调用）
         */
        public Builder searchFrom(SearchCondition searchFrom) {
            condition.searchFrom = searchFrom;
            return this;
        }

        /**
         * 构建搜索条件
         * <p>返回当前 Builder 中设置的 SearchCondition 实例。
         * 注意：返回的是 Builder 内部对象的引用，非深拷贝。
         *
         * @return 构建完成的 SearchCondition
         */
        public SearchCondition build() {
            return condition;
        }
    }

    /**
     * 将搜索条件转换为可读的字符串表示
     * <p>仅输出已设置的非默认条件字段，用于日志记录和调试。
     * 格式示例：{@code SearchCondition{name='Notepad', className='Notepad'}}
     *
     * @return 条件描述字符串
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("SearchCondition{");
        if (name != null) sb.append("name='").append(name).append("', ");
        if (subName != null) sb.append("subName='").append(subName).append("', ");
        if (regexName != null) sb.append("regexName='").append(regexName).append("', ");
        if (className != null) sb.append("className='").append(className).append("', ");
        if (automationId != null) sb.append("automationId='").append(automationId).append("', ");
        if (controlType != null) sb.append("controlType=").append(controlType).append(", ");
        if (depth != null) sb.append("depth=").append(depth).append(", ");
        if (searchDepth != Integer.MAX_VALUE) sb.append("searchDepth=").append(searchDepth).append(", ");
        if (foundIndex != 1) sb.append("foundIndex=").append(foundIndex).append(", ");
        sb.append("}");
        return sb.toString();
    }
}
