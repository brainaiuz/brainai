package com.finnetlimited.reportservice.core.client.ui.table;

import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;

/**
 * Created by IntelliJ IDEA.
 * User: nodir
 * Date: 05.08.2010
 * Time: 21:23:02
 * To change this template use File | Settings | File Templates.
 */
public final class TableItem<D> {

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
