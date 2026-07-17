package com.edatasite.workforce.gwt.core.client.ui.listpanel;

import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.google.gwt.gen2.table.client.*;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 26-Nov-2010
 * Time: 22:08:27
 */
public class ListingPagingScrollTable<T> extends PagingScrollTable<T> {

    private KpiCheckBox headerCheckBox;

    public ListingPagingScrollTable(TableModel tableModel, TableDefinition tableDefinition) {
        super(tableModel, tableDefinition);
    }

    public ListingPagingScrollTable(TableModel tableModel, FixedWidthGrid dataTable, FixedWidthFlexTable headerTable, TableDefinition tableDefinition) {
        super(tableModel, dataTable, headerTable, tableDefinition);
    }

    public ListingPagingScrollTable(TableModel tableModel, FixedWidthGrid dataTable, FixedWidthFlexTable headerTable, TableDefinition tableDefinition, ScrollTableImages images) {
        super(tableModel, dataTable, headerTable, tableDefinition, images);
    }

    @Override
    public void editCell(int row, int column) {
        super.editCell(row, column);
    }

    @Override
    public void showPopup(int row, int column, Event event) {
        super.showPopup(row, column, event);
    }

    /**
     * It may return null value when there is no support of
     * checkboxing in table. Hint: SelectionPolicy.CHECKBOX
     *
     * @return
     */
    public KpiCheckBox getHeaderCheckBox() {
        return headerCheckBox;
    }

    /**
     * Returns true when all items in the table have
     * been checked all items and vice versa.
     *
     * @return
     */
    public boolean hasCheckedAllTableItems() {
        return headerCheckBox != null && headerCheckBox.getValue();

    }

    @Override
    protected Widget getSelectAllWidget() {
        Widget widget = super.getSelectAllWidget();

        if (widget != null && widget instanceof KpiCheckBox) {
            KpiCheckBox checkbox = new KpiCheckBox();
            checkbox.addClickHandler(event -> {
                KpiCheckBox box = (KpiCheckBox) event.getSource();

                if (box.getValue()) {
                    getDataTable().selectAllRows();

                    if(allRowSelectedCommand != null) {
                        allRowSelectedCommand.execute();
                    }
                } else {
                    getDataTable().deselectAllRows();
                }
            });
            widget = headerCheckBox = checkbox;
        }

        return widget;
    }

    private Command allRowSelectedCommand;

    public void setAllRowSelectedCommand(Command allRowSelectedCommand) {
        this.allRowSelectedCommand = allRowSelectedCommand;
    }

    public Command getAllRowSelectedCommand() {
        return allRowSelectedCommand;
    }

    public void setPopupWidgets(ArrayList<ArrayList<Widget>> widgets) {
        super.setPopupWidgets(widgets);
    }

    /**
     * @return the wrapper element around the data table
     */
    @Override
    protected Element getDataWrapper() {
        return super.getDataWrapper();
    }
}
