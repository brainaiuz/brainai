package com.edatasite.workforce.gwt.core.client.ui.SortableTable;

import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: Jamshid
 * Date: 28.07.2010
 */
public class SortableTable extends FlexTable implements Sortable, ClickHandler {

    // Holds the current column being sorted
    private int sortColIndex = -1;

    // Holds the current direction of sort: Asc/ Desc
    private int sortDirection = -1;

    // The default image to show acending order arrow
    private Icon sortAscIcon;

    //The default image to show descending order arrow
    private Icon sortDescIcon;

    // The default image to show the blank image
    // This is needed to paint the columns other than
    // the one which is being sorted.
    // Should be same length and width as the asc/ desc
    // images.
    private String blankImage = "mainStyles/blank.png";

    // Holds the data rows of the table
    // This is a list of RowData Object
    private List tableRows = new ArrayList();

    // Holds the data for the column headers
    private List<String> tableHeader = new ArrayList<>();

    private Widget widget = null;

    /*
    * Default Constructor
    *
    * Calls the super class constructor
    * and adds a TableListener object
    */

    public SortableTable() {
        super();
        this.addClickHandler(this);
        this.sinkEvents(Event.ONMOUSEOVER | Event.ONMOUSEOUT);
//        this].addTableListener(this);
    }

    /*
    * addColumnHeader
    *
    * Adds the Column Header to the table
    * Uses the rowIndex 0 to add the header names.
    * Renders the name and the asc/desc/blank gif
    * to the column
    *
    * @param columnName (String)
    * @param columnIndex (int)
    */

    public void addColumnHeader(String name, int index, Widget widget) {
        tableHeader.add(index, name);
        this.widget = widget;
        this.renderTableHeader(name, index, widget);
    }

    /*
    * setValue
    *
    * Sets the values in specifed row/column
    * Expects a Comparable Object for sorting
    *
    * @param rowIndex (int)
    * @param columnIndex (int)
    * @param Value (Comparable)
    */

    public void setValue(int rowIndex, int colIndex, Comparable value) {
        // The rowIndex should begin with 2 as rowIndex 1 is for the Header
        // Any row with index == 1 will not be displayed.
        if (rowIndex == 0) {
            return;
        }

        if ((rowIndex - 2) >= this.tableRows.size() || null == tableRows.get(rowIndex - 2)) {
            tableRows.add(rowIndex - 2, new RowData());
        }

        RowData rowData = (RowData) this.tableRows.get(rowIndex - 2);
        rowData.addColumnValue(colIndex, value);
        this.setHTML(rowIndex, colIndex, "" + String.valueOf(value) + "");
    }

    /*
    * sort
    *
    * Implementation of Sortable Interface, this
    * method describes how to sort the specified
    * column. It checks the current sort direction
    * and flips it
    *
    * @param columnIndex (int)
    */

    public void sort(int columnIndex) {
        Collections.sort(this.tableRows);
        if (this.sortColIndex != columnIndex) {
            // New Column Header clicked
            // Reset the sortDirection to ASC
            this.sortDirection = Sortable.SORT_ASC;
        } else {
            // Same Column Header clicked
            // Reverse the sortDirection
            this.sortDirection = (this.sortDirection == Sortable.SORT_ASC) ? Sortable.SORT_DESC : Sortable.SORT_ASC;
        }
        this.sortColIndex = columnIndex;
    }

    /*
    * onCellClicked
    *
    * Implementation of Table Listener Interface, this
    * method describes what to do when a cell is clicked
    * It checks for the header row and calls the sort
    * method to sort the table
    *
    * @param sender (SourcesTableEvents)
    * @param rowIndex (int)
    * @param colIndex (int)
    */

    @Override
    public void onClick(ClickEvent clickEvent) {
        Cell cell = this.getCellForEvent(clickEvent);
        int row = cell != null ? cell.getRowIndex() : 0;

        if (row != 1) {
            return;
        }
        int col = cell.getCellIndex();

        sortRow(col);
    }

    public void sortRow(int col) {
//        this.setSortColIndex(col);
//        this.sort(col);
//        this.drawTable();
    }
    /*
    * Highlights selected row
    */

    @Override
    public void onBrowserEvent(Event event) {
        super.onBrowserEvent(event);
        Element td = getEventTargetCell(event);
        if (td == null) {
            return;
        }
        Element tr = DOM.getParent(td);
        switch (DOM.eventGetType(event)) {
            case Event.ONMOUSEOVER: {
                tr.addClassName("my-tbl-item-sel");
                break;
            }
            case Event.ONMOUSEOUT: {
                tr.removeClassName("my-tbl-item-sel");
                break;
            }
        }
    }
    /*
    * getSortAscImage
    * Getter for Sort Ascending Image
    * @return String
    */

    public Icon getSortAscIcon() {
        return sortAscIcon;
    }

    /*
    * setSortAscIcon
    *
    * Setter for Sort Ascending Icon
    *
    * @param relative path + image name (String)
    * e.g. images/asc.gif
    */

    public void setSortAscIcon(Icon sortAscIcon) {
        this.sortAscIcon = sortAscIcon;
    }

    /*
    * getSortDescIcon
    *
    * Getter for Sort Descending Icon
    *
    * @return String
    */

    public Icon getSortDescIcon() {
        return sortDescIcon;
    }

    /*
    * setSortDescIcon
    *
    * Setter for Sort Descending Image
    *
    * @param relative path + image name (String)
    * e.g. images/desc.gif
    */

    public void setSortDescIcon(Icon sortDescIcon) {
        this.sortDescIcon = sortDescIcon;
    }

    /*
    * getBlankImage
    *
    * Getter for blank Image
    *
    * @return String
    */

    public String getBlankImage() {
        return blankImage;
    }

    /*
    * setBlankImage
    *
    * Setter for the blank Image
    *
    * @param relative path + image name (String)
    * e.g. images/blank.gif
    */

    public void setBlankImage(String blankImage) {
        this.blankImage = blankImage;
    }

    /*
    * drawTable
    *
    * Renders the header as well as the body
    * of the table
    */

    protected void drawTable() {
        this.displayTableHeader();
        this.displayTableBody();
    }

    /*
    * displayTableHeader
    *
    * Renders only the table header
    */

    private void displayTableHeader() {
        int colIndex = 0;
        for (Object aTableHeader : this.tableHeader) {
            String colHeader = (String) aTableHeader;
            this.renderTableHeader(colHeader, colIndex++, widget);
        }
    }

    /*
    * displayTableBody
    *
    * Renders the body or the remaining rows of the table
    * except the header.
    * It checks the sort direction and displays the rows
    * accordingly
    */

    private void displayTableBody() {
        if (this.sortDirection == Sortable.SORT_ASC || this.sortDirection == -1) {
            // Ascending order and Default Display
            for (int rowIndex = 0; rowIndex < tableRows.size(); rowIndex++) {
                RowData columns = (RowData) tableRows.get(rowIndex);
                for (int colIndex = 0; colIndex < columns.getColumnValues().size(); colIndex++) {
                    Object value = columns.getColumnValue(colIndex);
                    if (null != value) {
                        this.setHTML(rowIndex + 2, colIndex, value.toString());
                    }
                }
            }
        } else {
            // Descending Order Display
            for (int rowIndex = tableRows.size() - 2, rowNum = 2; rowIndex >= 1; rowIndex--, rowNum++) {
                RowData columns = (RowData) tableRows.get(rowIndex);
                for (int colIndex = 0; colIndex < columns.getColumnValues().size(); colIndex++) {
                    Object value = columns.getColumnValue(colIndex);
                    if (null != value) {
                        this.setHTML(rowNum, colIndex, value.toString());
                    }
                }
            }
        }
    }

    /*
    * setSortColIndex
    *
    * Sets the current column index being sorted
    *
    * @param column index being sorted (int)
    */

    private void setSortColIndex(int sortIndex) {
        for (Object tableRow : tableRows) {
            RowData row = (RowData) tableRow;
            row.setSortColIndex(sortIndex);
        }
    }

    /*
    * renderTableHeader
    * Renders a particular column in the Table Header
    *
    * @param Column Name (String)
    * @param Column Index (int)
    */

    private void renderTableHeader(String name, int index, Widget widget) {
        Icon sortIcon = new Icon();
        if (this.sortColIndex == index) {
            if (this.sortDirection == Sortable.SORT_ASC) {
                sortIcon.setStyleName("ficon--keyboard-arrow-down");
            } else {
                sortIcon.setStyleName("ficon--keyboard-arrow-up");
            }
        }
        if (widget == null) {
            MaterialPanel panel = new MaterialPanel();
            panel.add(new Span(name));
            panel.add(sortIcon);
            this.setWidget(1, index, panel);
        } else {
            MaterialPanel panel = new MaterialPanel();
            panel.add(widget);
            panel.add(new Span(name));
            panel.add(sortIcon);
            this.setWidget(1, index, panel);
        }
    }
}
