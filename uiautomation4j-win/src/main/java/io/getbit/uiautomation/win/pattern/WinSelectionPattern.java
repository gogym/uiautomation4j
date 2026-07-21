package io.getbit.uiautomation.win.pattern;

import io.getbit.uiautomation.pattern.SelectionPattern;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.PointerByReference;
import io.getbit.uiautomation.win.com.COMObject;
import io.getbit.uiautomation.win.com.IUIAutomationElement;
import io.getbit.uiautomation.win.com.IUIAutomationElementArray;

import java.util.ArrayList;
import java.util.List;

/**
 * SelectionPattern 的 Windows 实现
 *
 * <p>将 core 模块的 {@link SelectionPattern} 接口桥接到 Windows COM 层。
 * 使用内部类 {@code SelectionComObject} 继承 {@link COMObject} 以访问 protected 的
 * {@code invokeVtable} 方法，解决跨包访问问题。</p>
 *
 * <p>对应 COM 接口: IUIAutomationSelectionPattern (PatternId: 10001)
 * Vtable:
 *   [3] get_CurrentSelection(IUIAutomationElementArray** ppSelected)
 *   [4] get_CurrentCanSelectMultiple(BOOL* pCanSelectMultiple)
 * </p>
 */
public class WinSelectionPattern implements SelectionPattern {

    /** 内部 COM 包装对象，用于调用 SelectionPattern 的 vtable 方法 */
    private final SelectionComObject comPattern;

    /**
     * 构造 WinSelectionPattern
     *
     * @param patternPointer IUIAutomationSelectionPattern COM 接口指针
     */
    public WinSelectionPattern(Pointer patternPointer) {
        this.comPattern = new SelectionComObject(patternPointer);
    }

    /**
     * 获取当前选中的元素集合
     * <p>调用 vtable[3]: GetSelection(IUIAutomationElementArray** ppElements)
     * <p>返回值为 IUIAutomationElementArray，需要遍历转换为 Java List
     *
     * @return 选中元素的列表，每个元素为 IUIAutomationElement 包装对象
     */
    @Override
    public List<Object> getSelection() {
        PointerByReference ppArray = new PointerByReference();
        // 调用 COM GetSelection 方法，获取元素数组指针
        comPattern.callVtable(3, new Object[]{ppArray});
        Pointer arrayPtr = ppArray.getValue();
        List<Object> result = new ArrayList<>();
        if (arrayPtr != null && arrayPtr != Pointer.NULL) {
            // 将 COM 数组包装为 IUIAutomationElementArray 并逐个获取元素
            IUIAutomationElementArray array = new IUIAutomationElementArray(arrayPtr);
            int len = array.getLength();
            for (int i = 0; i < len; i++) {
                result.add(array.getElement(i));
            }
        }
        return result;
    }

    /**
     * 判断是否支持多选
     * <p>调用 vtable[4]: get_CurrentCanSelectMultiple(BOOL* pCanSelectMultiple)
     *
     * @return 如果支持多选返回 true
     */
    @Override
    public boolean isMultiSelect() {
        int[] pVal = new int[1];
        comPattern.callVtable(4, new Object[]{pVal});
        return pVal[0] != 0;
    }

    /**
     * 内部 COM 包装类
     * <p>继承 COMObject，通过 {@code callVtable()} 公开方法调用 vtable 方法。
     * 使用 static 内部类避免外部直接访问 COMObject 的 protected 方法。
     */
    private static class SelectionComObject extends COMObject {
        /**
         * 构造 SelectionComObject
         *
         * @param pointer COM 接口指针
         */
        SelectionComObject(Pointer pointer) {
            super(pointer);
        }

        public WinNT.HRESULT callVtable(int idx, Object[] args) {
            return invokeVtable(idx, args);
        }
    }
}
