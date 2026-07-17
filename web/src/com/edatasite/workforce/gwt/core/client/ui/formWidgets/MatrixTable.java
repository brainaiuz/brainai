package com.edatasite.workforce.gwt.core.client.ui.formWidgets;

import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.*;

import java.util.HashMap;
import java.util.Map;

/**
 * User: hayot.rahimov@gmail.com
 * Date: 4/21/11
 * Time: 4:27 PM
 */
public class MatrixTable extends FlexTable {
    private int numberOfColumns = 4;
    private Map<Object, Object> valuesMap = new HashMap<>();
    private int lastRow = 0;
    private int lastColumn = 0;
    private boolean enabled;
    private static int fieldId = 0;

    public MatrixTable(int numberOfColumns) {
        super();
        this.numberOfColumns = numberOfColumns;
    }

    public MatrixTable() {
        super();
    }

    public void add(Object obj, Widget widget) {
        if (widget != null) {
            this.setWidget(getRow(), lastColumn++, widget);
            valuesMap.put(obj, widget);
        }
    }

    private int getRow() {
        if (numberOfColumns - lastColumn == 0) {
            lastColumn = 0;
            lastRow++;
        }
        return lastRow;
    }

    public Map<Object, Object> getValuesMap() {
        return valuesMap;
    }

    @Override
    public void clear() {
        super.clear();
        valuesMap.clear();
        lastRow = 0;
        lastColumn = 0;
    }

    @Override
    public void removeAllRows() {
        super.removeAllRows();
        valuesMap.clear();
        lastRow = 0;
        lastColumn = 0;
    }

    public void addItems(Map items, boolean... clearBeforeAdding) {
        if (clearBeforeAdding != null && clearBeforeAdding.length > 0 && clearBeforeAdding[0]) {
            clear();
        }
        if (items != null && items.size() > 0) {
            for (Map.Entry<Object, Widget> entry : ((Map<Object, Widget>) items).entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    add(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    public void addItems(Map items, HashMap<String, Boolean> disabledItems, boolean... clearBeforeAdding) {
        if (clearBeforeAdding != null && clearBeforeAdding.length > 0 && clearBeforeAdding[0]) {
            clear();
        }
        if (items != null && items.size() > 0) {
            for (Map.Entry<Object, Widget> entry : ((Map<Object, Widget>) items).entrySet()) {
                if (entry.getKey() != null && entry.getValue() != null) {
                    String code = ((ReferenceItem) entry.getKey()).getCode();
                    if (disabledItems != null && disabledItems.size() > 0 && disabledItems.containsKey(code)) {
                        if (entry.getValue() instanceof KpiCheckBox) {
                            KpiCheckBox ch = (KpiCheckBox) entry.getValue();
                            ch.setEnabled(false);
                            entry.setValue(ch);
                        }
                    }
                    add(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    public void setEnabled(boolean enable) {
        for (Map.Entry entry : valuesMap.entrySet()) {
            if (entry.getValue() instanceof FocusWidget) {
                final FocusWidget focusableWidget = (FocusWidget) entry.getValue();
                focusableWidget.setEnabled(enable);
            }
        }
    }

    /**
     * this method will unselects or clears written values in (Widgets)
     * checkbox ...
     * (the method is working for checkbox only if you want to add some widgets please write down your own code to implement for other widgets)
     */
    public void clearSelected() {
        for (Map.Entry entry : valuesMap.entrySet()) {
            if (entry.getValue() instanceof RadioButton) {
                final RadioButton checkBox = (RadioButton) entry.getValue();
                checkBox.setValue(Boolean.FALSE);
            } else if (entry.getValue() instanceof KpiCheckBox) {
                final KpiCheckBox checkBox = (KpiCheckBox) entry.getValue();
                checkBox.setValue(Boolean.FALSE);
            }
            if (entry.getKey() instanceof SelectItem) {
                final SelectItem selectItem = (SelectItem) entry.getKey();
                selectItem.setSelected(false);

            }
        }
    }

    public void addBlurHandler(BlurHandler blurHandler) {
        for (Map.Entry entry : valuesMap.entrySet()) {
            if (entry.getValue() instanceof RadioButton) {
                final RadioButton checkBox = (RadioButton) entry.getValue();
                checkBox.addBlurHandler(blurHandler);
            }
            if (entry.getValue() instanceof KpiCheckBox) {
                final KpiCheckBox checkBox = (KpiCheckBox) entry.getValue();
                checkBox.addBlurHandler(blurHandler);
            }
        }
    }

    public void addValueChangeHandler(ValueChangeHandler handler) {
        for (Map.Entry entry : valuesMap.entrySet()) {
            if (entry.getValue() instanceof RadioButton) {
                final RadioButton checkBox = (RadioButton) entry.getValue();
                checkBox.addValueChangeHandler(handler);
            }
            if (entry.getValue() instanceof KpiCheckBox) {
                final KpiCheckBox checkBox = (KpiCheckBox) entry.getValue();
                checkBox.addValueChangeHandler(handler);
            } else if (entry.getValue() instanceof CheckBox) {
                final CheckBox checkBox = (CheckBox) entry.getValue();
                checkBox.addValueChangeHandler(handler);
            }
        }
    }

    public int getLastColumn() {
        return lastColumn;
    }

    public int getLastRow() {
        return lastRow;
    }
}