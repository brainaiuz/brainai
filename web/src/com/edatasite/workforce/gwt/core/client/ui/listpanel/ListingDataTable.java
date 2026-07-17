package com.edatasite.workforce.gwt.core.client.ui.listpanel;

import com.google.gwt.gen2.table.client.FixedWidthGrid;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.TableListener;


/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 26-Nov-2010
 * Time: 20:25:23
 */
public class ListingDataTable extends FixedWidthGrid {

    private ListingPagingScrollTable parentTable;

    public ListingDataTable() {
        super();
        sinkEvents(Event.ONDBLCLICK);
    }

    @Override
    public void onBrowserEvent(Event event) {
        switch (event.getTypeInt()) {
            case Event.ONCLICK:
                if (getParentTable() != null) {
                    Element clicked = DOM.eventGetTarget(event);
                    if (clicked != null && clicked.getClassName().contains(ListingPanel.PENCIL_CSS)) {
                        Element rowElem = null, cellElem = null;
                        // Get the target row
                        cellElem = getEventTargetCell(event);
                        if (cellElem == null) {
                            return;
                        }
                        rowElem = DOM.getParent(cellElem);
                        int rowIndex = getRowIndex(rowElem);
                        int cellIndex = getCellIndex(rowElem, cellElem);
                        DOM.eventPreventDefault(event);
                        parentTable.editCell(rowIndex, cellIndex);
                    }
                }
                break;
            case Event.ONDBLCLICK:
                if (getParentTable() != null) {
                    Element rowElem = null, cellElem = null;
                    // Get the target row
                    cellElem = getEventTargetCell(event);
                    if (cellElem == null) {
                        return;
                    }
                    rowElem = DOM.getParent(cellElem);
                    int rowIndex = getRowIndex(rowElem);
                    int cellIndex = getCellIndex(rowElem, cellElem);
                    DOM.eventPreventDefault(event);
                    parentTable.editCell(rowIndex, cellIndex);
                }
            case Event.ONMOUSEOVER:
                if (getParentTable() != null) {
                    Element rowElem = null, cellElem = null;
                    // Get the target row
                    cellElem = getEventTargetCell(event);
                    if (cellElem == null) {
                        return;
                    }
                    rowElem = DOM.getParent(cellElem);
                    int rowIndex = getRowIndex(rowElem);
                    int cellIndex = getCellIndex(rowElem, cellElem);
                    DOM.eventPreventDefault(event);
                    parentTable.showPopup(rowIndex, cellIndex, event);
                }
            default:
                super.onBrowserEvent(event);
        }
    }

    @Override
    public void addTableListener(TableListener listener) {
        // This is method body empty because not working double cliked in cell
    }

    public ListingPagingScrollTable getParentTable() {
        return parentTable;
    }

    public void setParentTable(ListingPagingScrollTable parentTable) {
        this.parentTable = parentTable;
    }
}
