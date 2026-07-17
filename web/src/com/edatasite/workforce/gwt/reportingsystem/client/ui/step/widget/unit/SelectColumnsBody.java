package com.edatasite.workforce.gwt.reportingsystem.client.ui.step.widget.unit;

import com.edatasite.workforce.gwt.reportingsystem.client.rpc.ColumnRpc;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.TableSectionElement;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ComplexPanel;

import java.util.ArrayList;

/**
 * Created by Virus on 8/27/14.
 */
public class SelectColumnsBody extends ComplexPanel {

	int widgetCount = 0;
	Element lastRow = null;
	private ArrayList<SelectColumnItem> columns = new ArrayList<>();

	public SelectColumnsBody(TableSectionElement tableContainer) {
		this.tableContainer = tableContainer;
	}

	TableSectionElement tableContainer;

	public SelectColumnItem addField(ColumnRpc columnRpc) {
		if (widgetCount++ % 3 < 1) {
			lastRow = DOM.createElement("tr");
			tableContainer.appendChild(lastRow);
		}
		SelectColumnItem columnItem = new SelectColumnItem(columnRpc);
		add(columnItem, lastRow);
		columns.add(columnItem);
		return columnItem;
	}

	public ArrayList<ColumnRpc> selectedColumns() {
		ArrayList<ColumnRpc> list = new ArrayList<>();
        for (SelectColumnItem column : columns) {
            ColumnRpc rpc = column.getColumnRpc();
            if (rpc.isChecked()) {
                list.add(rpc);
            }
        }
		return list;
	}

}
