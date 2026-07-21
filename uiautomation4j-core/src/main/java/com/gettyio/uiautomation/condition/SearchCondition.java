package com.gettyio.uiautomation.condition;

import com.gettyio.uiautomation.enums.ControlType;

import java.util.function.BiPredicate;

/**
 * 搜索条件 - Builder 模式
 * 用于构建控件搜索条件，支持链式调用
 *
 * <pre>
 * SearchCondition condition = SearchCondition.builder()
 *     .name("Notepad")
 *     .className("Notepad")
 *     .searchDepth(1)
 *     .controlType(ControlType.Window)
 *     .build();
 * </pre>
 */
public class SearchCondition {

    private String name;
    private String subName;
    private String regexName;
    private String className;
    private String automationId;
    private ControlType controlType;
    private Integer depth;
    private int searchDepth = Integer.MAX_VALUE;
    private int foundIndex = 1;
    private long searchInterval = 100; // ms
    private BiPredicate<Object, Integer> compare;
    private SearchCondition searchFrom;

    private SearchCondition() {
    }

    public static Builder builder() {
        return new Builder();
    }

    // Getters
    public String getName() { return name; }
    public String getSubName() { return subName; }
    public String getRegexName() { return regexName; }
    public String getClassName() { return className; }
    public String getAutomationId() { return automationId; }
    public ControlType getControlType() { return controlType; }
    public Integer getDepth() { return depth; }
    public int getSearchDepth() { return searchDepth; }
    public int getFoundIndex() { return foundIndex; }
    public long getSearchInterval() { return searchInterval; }
    public BiPredicate<Object, Integer> getCompare() { return compare; }
    public SearchCondition getSearchFrom() { return searchFrom; }

    public boolean hasName() { return name != null; }
    public boolean hasSubName() { return subName != null; }
    public boolean hasRegexName() { return regexName != null; }
    public boolean hasClassName() { return className != null; }
    public boolean hasAutomationId() { return automationId != null; }
    public boolean hasControlType() { return controlType != null; }
    public boolean hasDepth() { return depth != null; }
    public boolean hasCompare() { return compare != null; }

    /**
     * Builder 内部类
     */
    public static class Builder {
        private final SearchCondition condition = new SearchCondition();

        public Builder name(String name) {
            condition.name = name;
            return this;
        }

        public Builder subName(String subName) {
            condition.subName = subName;
            return this;
        }

        public Builder regexName(String regexName) {
            condition.regexName = regexName;
            return this;
        }

        public Builder className(String className) {
            condition.className = className;
            return this;
        }

        public Builder automationId(String automationId) {
            condition.automationId = automationId;
            return this;
        }

        public Builder controlType(ControlType controlType) {
            condition.controlType = controlType;
            return this;
        }

        public Builder depth(int depth) {
            condition.depth = depth;
            return this;
        }

        public Builder searchDepth(int searchDepth) {
            condition.searchDepth = searchDepth;
            return this;
        }

        public Builder foundIndex(int foundIndex) {
            condition.foundIndex = foundIndex;
            return this;
        }

        public Builder searchInterval(long intervalMs) {
            condition.searchInterval = intervalMs;
            return this;
        }

        public Builder compare(BiPredicate<Object, Integer> compare) {
            condition.compare = compare;
            return this;
        }

        public Builder searchFrom(SearchCondition searchFrom) {
            condition.searchFrom = searchFrom;
            return this;
        }

        public SearchCondition build() {
            return condition;
        }
    }

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
