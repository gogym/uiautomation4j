package com.gettyio.uiautomation.control;

import com.gettyio.uiautomation.condition.SearchCondition;
import com.gettyio.uiautomation.enums.ControlType;
import com.gettyio.uiautomation.pattern.*;
import com.gettyio.uiautomation.spi.ControlBackend;

/**
 * 控件基类
 * 所有 UIAutomation 控件的抽象基类
 */
public abstract class Control {

    protected static ControlBackend backend;

    protected SearchCondition searchCondition;
    protected Object nativeElement; // 底层 COM Element 引用
    protected boolean elementFound = false;

    protected Control() {
    }

    protected Control(SearchCondition searchCondition) {
        this.searchCondition = searchCondition;
    }

    // ==================== 供 Backend 使用的访问方法 ====================

    public Object getNativeElement() { return nativeElement; }
    public void setNativeElement(Object element) { this.nativeElement = element; }
    public boolean isElementFound() { return elementFound; }
    public void setElementFound(boolean found) { this.elementFound = found; }
    public SearchCondition getSearchCondition() { return searchCondition; }

    /**
     * 注册后端实现（由 win 模块初始化时调用）
     */
    public static void registerBackend(ControlBackend controlBackend) {
        backend = controlBackend;
    }

    /**
     * 获取已注册的后端
     */
    public static ControlBackend getBackend() {
        if (backend == null) {
            throw new IllegalStateException("ControlBackend 未注册，请先调用 Control.registerBackend()");
        }
        return backend;
    }

    // ==================== 属性获取 ====================

    /**
     * 获取控件名称
     */
    public String getName() {
        ensureElement();
        return getBackend().findControl(searchCondition).getName();
    }

    /**
     * 获取控件类名
     */
    public String getClassName() {
        ensureElement();
        return getBackend().findControl(searchCondition).getClassName();
    }

    /**
     * 获取 AutomationId
     */
    public String getAutomationId() {
        ensureElement();
        return getBackend().findControl(searchCondition).getAutomationId();
    }

    /**
     * 获取控件类型
     */
    public abstract ControlType getControlType();

    /**
     * 获取进程 ID
     */
    public int getProcessId() {
        ensureElement();
        return getBackend().findControl(searchCondition).getProcessId();
    }

    // ==================== 搜索子控件 ====================

    /**
     * 根据条件搜索子控件
     */
    public Control findControl(SearchCondition condition) {
        SearchCondition merged = mergeWithParent(condition);
        return getBackend().findControl(merged);
    }

    /**
     * 查找子窗口
     */
    public WindowControl findWindow() {
        return findWindow(SearchCondition.builder().build());
    }

    public WindowControl findWindow(SearchCondition condition) {
        SearchCondition merged = mergeWithParent(condition);
        merged = overrideControlType(merged, ControlType.Window);
        Control c = getBackend().findControl(merged);
        return (WindowControl) c;
    }

    /**
     * 查找子按钮
     */
    public ButtonControl findButton() {
        return findButton(SearchCondition.builder().build());
    }

    public ButtonControl findButton(SearchCondition condition) {
        SearchCondition merged = mergeWithParent(condition);
        merged = overrideControlType(merged, ControlType.Button);
        Control c = getBackend().findControl(merged);
        return (ButtonControl) c;
    }

    /**
     * 查找子编辑框
     */
    public EditControl findEdit() {
        return findEdit(SearchCondition.builder().build());
    }

    public EditControl findEdit(SearchCondition condition) {
        SearchCondition merged = mergeWithParent(condition);
        merged = overrideControlType(merged, ControlType.Edit);
        Control c = getBackend().findControl(merged);
        return (EditControl) c;
    }

    /**
     * 查找子文本控件
     */
    public TextControl findText() {
        return findText(SearchCondition.builder().build());
    }

    public TextControl findText(SearchCondition condition) {
        SearchCondition merged = mergeWithParent(condition);
        merged = overrideControlType(merged, ControlType.Text);
        Control c = getBackend().findControl(merged);
        return (TextControl) c;
    }

    /**
     * 查找子列表
     */
    public ListControl findList() {
        return findList(SearchCondition.builder().build());
    }

    public ListControl findList(SearchCondition condition) {
        SearchCondition merged = mergeWithParent(condition);
        merged = overrideControlType(merged, ControlType.List);
        Control c = getBackend().findControl(merged);
        return (ListControl) c;
    }

    /**
     * 查找子列表项
     */
    public ListItemControl findListItem() {
        return findListItem(SearchCondition.builder().build());
    }

    public ListItemControl findListItem(SearchCondition condition) {
        SearchCondition merged = mergeWithParent(condition);
        merged = overrideControlType(merged, ControlType.ListItem);
        Control c = getBackend().findControl(merged);
        return (ListItemControl) c;
    }

    /**
     * 查找子菜单项
     */
    public MenuItemControl findMenuItem() {
        return findMenuItem(SearchCondition.builder().build());
    }

    public MenuItemControl findMenuItem(SearchCondition condition) {
        SearchCondition merged = mergeWithParent(condition);
        merged = overrideControlType(merged, ControlType.MenuItem);
        Control c = getBackend().findControl(merged);
        return (MenuItemControl) c;
    }

    /**
     * 查找子复选框
     */
    public CheckBoxControl findCheckBox() {
        return findCheckBox(SearchCondition.builder().build());
    }

    public CheckBoxControl findCheckBox(SearchCondition condition) {
        SearchCondition merged = mergeWithParent(condition);
        merged = overrideControlType(merged, ControlType.CheckBox);
        Control c = getBackend().findControl(merged);
        return (CheckBoxControl) c;
    }

    /**
     * 查找子面板
     */
    public PaneControl findPane() {
        return findPane(SearchCondition.builder().build());
    }

    public PaneControl findPane(SearchCondition condition) {
        SearchCondition merged = mergeWithParent(condition);
        merged = overrideControlType(merged, ControlType.Pane);
        Control c = getBackend().findControl(merged);
        return (PaneControl) c;
    }

    /**
     * 查找子菜单栏
     */
    public MenuBarControl findMenuBar() {
        return findMenuBar(SearchCondition.builder().build());
    }

    public MenuBarControl findMenuBar(SearchCondition condition) {
        SearchCondition merged = mergeWithParent(condition);
        merged = overrideControlType(merged, ControlType.MenuBar);
        Control c = getBackend().findControl(merged);
        return (MenuBarControl) c;
    }

    /**
     * 查找子标题栏
     */
    public TitleBarControl findTitleBar() {
        return findTitleBar(SearchCondition.builder().build());
    }

    public TitleBarControl findTitleBar(SearchCondition condition) {
        SearchCondition merged = mergeWithParent(condition);
        merged = overrideControlType(merged, ControlType.TitleBar);
        Control c = getBackend().findControl(merged);
        return (TitleBarControl) c;
    }

    /**
     * 查找子滚动条
     */
    public ScrollBarControl findScrollBar() {
        return findScrollBar(SearchCondition.builder().build());
    }

    public ScrollBarControl findScrollBar(SearchCondition condition) {
        SearchCondition merged = mergeWithParent(condition);
        merged = overrideControlType(merged, ControlType.ScrollBar);
        Control c = getBackend().findControl(merged);
        return (ScrollBarControl) c;
    }

    /**
     * 查找子 Tab
     */
    public TabControl findTab() {
        return findTab(SearchCondition.builder().build());
    }

    public TabControl findTab(SearchCondition condition) {
        SearchCondition merged = mergeWithParent(condition);
        merged = overrideControlType(merged, ControlType.Tab);
        Control c = getBackend().findControl(merged);
        return (TabControl) c;
    }

    /**
     * 查找子 Tree
     */
    public TreeControl findTree() {
        return findTree(SearchCondition.builder().build());
    }

    public TreeControl findTree(SearchCondition condition) {
        SearchCondition merged = mergeWithParent(condition);
        merged = overrideControlType(merged, ControlType.Tree);
        Control c = getBackend().findControl(merged);
        return (TreeControl) c;
    }

    /**
     * 查找子 TreeItem
     */
    public TreeItemControl findTreeItem() {
        return findTreeItem(SearchCondition.builder().build());
    }

    public TreeItemControl findTreeItem(SearchCondition condition) {
        SearchCondition merged = mergeWithParent(condition);
        merged = overrideControlType(merged, ControlType.TreeItem);
        Control c = getBackend().findControl(merged);
        return (TreeItemControl) c;
    }

    // ==================== 操作 ====================

    /**
     * 点击控件
     */
    public void click() {
        ensureElement();
        getBackend().click(this);
    }

    /**
     * 双击控件
     */
    public void doubleClick() {
        ensureElement();
        getBackend().doubleClick(this);
    }

    /**
     * 右键点击控件
     */
    public void rightClick() {
        ensureElement();
        getBackend().rightClick(this);
    }

    /**
     * 发送按键
     */
    public void sendKeys(String keys) {
        ensureElement();
        getBackend().sendKeys(this, keys);
    }

    /**
     * 截图并保存到文件
     */
    public void captureToImage(String filePath) {
        ensureElement();
        getBackend().captureToImage(this, filePath);
    }

    // ==================== Pattern 获取 ====================

    public ValuePattern getValuePattern() {
        ensureElement();
        return getBackend().getValuePattern(this);
    }

    public WindowPattern getWindowPattern() {
        ensureElement();
        return getBackend().getWindowPattern(this);
    }

    public InvokePattern getInvokePattern() {
        ensureElement();
        return getBackend().getInvokePattern(this);
    }

    public SelectionPattern getSelectionPattern() {
        ensureElement();
        return getBackend().getSelectionPattern(this);
    }

    public ScrollPattern getScrollPattern() {
        ensureElement();
        return getBackend().getScrollPattern(this);
    }

    public ExpandCollapsePattern getExpandCollapsePattern() {
        ensureElement();
        return getBackend().getExpandCollapsePattern(this);
    }

    public TransformPattern getTransformPattern() {
        ensureElement();
        return getBackend().getTransformPattern(this);
    }

    // ==================== 状态检查 ====================

    /**
     * 检查控件是否存在
     *
     * @param maxWaitSeconds 最大等待秒数
     * @return 是否存在
     */
    public boolean exists(int maxWaitSeconds) {
        return getBackend().exists(searchCondition, maxWaitSeconds);
    }

    /**
     * 检查控件是否存在（使用默认超时）
     */
    public boolean exists() {
        return getBackend().exists(searchCondition, getBackend().getGlobalSearchTimeout());
    }

    /**
     * 重新查找控件
     */
    public void refind() {
        elementFound = false;
        nativeElement = null;
        ensureElement();
    }

    // ==================== 静态工厂方法（Builder 入口） ====================

    /**
     * 创建窗口搜索 Builder
     */
    public static WindowControlBuilder window() {
        return new WindowControlBuilder();
    }

    /**
     * 创建按钮搜索 Builder
     */
    public static ButtonControlBuilder button() {
        return new ButtonControlBuilder();
    }

    /**
     * 创建编辑框搜索 Builder
     */
    public static EditControlBuilder edit() {
        return new EditControlBuilder();
    }

    /**
     * 创建文本搜索 Builder
     */
    public static TextControlBuilder text() {
        return new TextControlBuilder();
    }

    // ==================== 内部方法 ====================

    protected void ensureElement() {
        if (!elementFound) {
            Control found = getBackend().findControl(searchCondition);
            this.nativeElement = found.nativeElement;
            this.elementFound = true;
        }
    }

    private SearchCondition mergeWithParent(SearchCondition child) {
        if (this.searchCondition == null) {
            return child;
        }
        // 如果有 searchFrom 未设置，则设置为当前控件
        if (child.getSearchFrom() == null) {
            return SearchCondition.builder()
                    .name(child.getName())
                    .subName(child.getSubName())
                    .regexName(child.getRegexName())
                    .className(child.getClassName())
                    .automationId(child.getAutomationId())
                    .controlType(child.getControlType())
                    .depth(child.getDepth())
                    .searchDepth(child.getSearchDepth())
                    .foundIndex(child.getFoundIndex())
                    .searchInterval(child.getSearchInterval())
                    .compare(child.getCompare())
                    .searchFrom(this.searchCondition)
                    .build();
        }
        return child;
    }

    private SearchCondition overrideControlType(SearchCondition condition, ControlType type) {
        return SearchCondition.builder()
                .name(condition.getName())
                .subName(condition.getSubName())
                .regexName(condition.getRegexName())
                .className(condition.getClassName())
                .automationId(condition.getAutomationId())
                .controlType(type)
                .depth(condition.getDepth())
                .searchDepth(condition.getSearchDepth())
                .foundIndex(condition.getFoundIndex())
                .searchInterval(condition.getSearchInterval())
                .compare(condition.getCompare())
                .searchFrom(condition.getSearchFrom())
                .build();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" +
                "searchCondition=" + searchCondition +
                ", elementFound=" + elementFound +
                '}';
    }

    // ==================== Builder 内部类 ====================

    public static class WindowControlBuilder extends ControlBuilderBase<WindowControlBuilder> {
        public WindowControl findWindow() {
            SearchCondition cond = buildCondition(ControlType.Window);
            Control c = getBackend().findControl(cond);
            return (WindowControl) c;
        }
    }

    public static class ButtonControlBuilder extends ControlBuilderBase<ButtonControlBuilder> {
        public ButtonControl findButton() {
            SearchCondition cond = buildCondition(ControlType.Button);
            Control c = getBackend().findControl(cond);
            return (ButtonControl) c;
        }
    }

    public static class EditControlBuilder extends ControlBuilderBase<EditControlBuilder> {
        public EditControl findEdit() {
            SearchCondition cond = buildCondition(ControlType.Edit);
            Control c = getBackend().findControl(cond);
            return (EditControl) c;
        }
    }

    public static class TextControlBuilder extends ControlBuilderBase<TextControlBuilder> {
        public TextControl findText() {
            SearchCondition cond = buildCondition(ControlType.Text);
            Control c = getBackend().findControl(cond);
            return (TextControl) c;
        }
    }

    public static abstract class ControlBuilderBase<T extends ControlBuilderBase<T>> {
        protected final SearchCondition.Builder builder = SearchCondition.builder();

        @SuppressWarnings("unchecked")
        protected T self() {
            return (T) this;
        }

        public T searchDepth(int depth) {
            builder.searchDepth(depth);
            return self();
        }

        public T name(String name) {
            builder.name(name);
            return self();
        }

        public T subName(String subName) {
            builder.subName(subName);
            return self();
        }

        public T regexName(String regexName) {
            builder.regexName(regexName);
            return self();
        }

        public T className(String className) {
            builder.className(className);
            return self();
        }

        public T automationId(String automationId) {
            builder.automationId(automationId);
            return self();
        }

        public T depth(int depth) {
            builder.depth(depth);
            return self();
        }

        public T foundIndex(int foundIndex) {
            builder.foundIndex(foundIndex);
            return self();
        }

        public T compare(java.util.function.BiPredicate<Object, Integer> compare) {
            builder.compare(compare);
            return self();
        }

        protected SearchCondition buildCondition(ControlType type) {
            builder.controlType(type);
            return builder.build();
        }
    }
}
