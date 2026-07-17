package com.edatasite.workforce.gwt.core.client.ui.table;

import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: May 19, 2010
 * Time: 5:25:59 PM
 * To change this template use File | Settings | File Templates.
 */
public class TableItem<D> {

    private D data;
    private KpiCheckBox checkbox = new KpiCheckBox();

    private TableItemValue[] values;

    public TableItem(TableItemValue[] values) {
        this.values = values;
    }

    public KpiCheckBox getCheckbox() {
        return checkbox;
    }

    public D getData() {
        return data;
    }

    public TableItemValue[] getValues() {
        return values;
    }

    public boolean isChecked() {
        return checkbox.getValue();
    }

    public void setData(D data) {
        this.data = data;
    }
}