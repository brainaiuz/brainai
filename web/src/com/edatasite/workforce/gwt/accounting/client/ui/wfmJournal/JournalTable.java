package com.edatasite.workforce.gwt.accounting.client.ui.wfmJournal;

import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 23.02.2009
 * Time: 19:30:58
 * To change this template use File | Settings | File Templates.
 */
public class JournalTable extends Composite {

    private List items;

    private DockPanel panel;
    private FlexTable extraHeader;
    private FlexTable table;
    private HTMLTable.RowFormatter rowFormatter;
    private HTMLTable.CellFormatter cellFormatter;

    private String cellStyle1 = "cell-settings-1";
    private String cellStyle2 = "cell-settings-2";
    private String cellStyle3 = "cell-settings-3";
    private String cellBorderStyle = "cell-border-style";

    private JournalTableColumn[] columns;
    private String bookmark;

    private int rows = 0;

    private boolean isGroupBookmarkExists;
    private boolean enablePager = false;

    /**
     * Creates journal table instance.
     *
     * @param columns
     */
    public JournalTable(JournalTableColumn[] columns) {
        this.columns = columns;

        initialize();
        drawHeader();
    }

    /**
     * Initializing local variables.
     */
    private void initialize() {
        items = new ArrayList();

        panel = new DockPanel();

        extraHeader = new FlexTable();

        table = new FlexTable();
        table.setCellPadding(0);
        table.setCellSpacing(0);
        table.setBorderWidth(1);
        table.setStyleName("budget-sheet-table table");
        for (int i = 0; i < columns.length; i++) {
            if (columns[i].getWidthPercentage() > 0) {
                table.getColumnFormatter().setWidth(i, columns[i].getWidthPercentage() + "%");
            }
        }

        rowFormatter = table.getRowFormatter();
        cellFormatter = table.getCellFormatter();

        initWidget(panel);
    }

    /**
     * Draws a header to the table and fills it with headerData.
     *
     * @param headerData
     */
    public void setAdditionalHeader(Object[] headerData) {
        HTMLTable.CellFormatter cellFrmtr = getExtraHeaderCellFormatter();

        for (int i = 0; i < headerData.length; i++) {
            if (headerData[i] instanceof Widget) {
                extraHeader.setWidget(0, i, (Widget) headerData[i]);
            } else {
                String data = String.valueOf(headerData[i]);
                extraHeader.setHTML(0, i, data != null && !data.equals("") ? data : "&nbsp;");
            }
            cellFrmtr.setStyleName(0, i, cellStyle1);
        }

        panel.add(extraHeader, DockPanel.NORTH);
    }

    /**
     * Draws a header to the table.
     *
     * @param text1 will be put most left side.
     * @param text2 will be put most right side.
     */
    public void setAdditionalHeader(String text1, String text2) {
        HTMLTable.CellFormatter cellFrmtr = getExtraHeaderCellFormatter();

        extraHeader.setHTML(0, 0, text1);
        cellFrmtr.setHorizontalAlignment(0, 0, DockPanel.ALIGN_LEFT);
        cellFrmtr.setStyleName(0, 0, cellStyle1);

        extraHeader.setHTML(0, 1, text2);
        cellFrmtr.setHorizontalAlignment(0, 1, DockPanel.ALIGN_RIGHT);
        cellFrmtr.setStyleName(0, 1, "header-cell-settings");

        panel.add(extraHeader, DockPanel.NORTH);
        //changes second header style
        setSecondHeaderStyle();
    }

    private void setSecondHeaderStyle() {
        for (int i = 0; i < columns.length; i++) {
            cellFormatter.setStyleName(0, i, "my-tbl-col");
        }
    }

    /**
     * Returns CellFormatter instance for drawing additional header.
     *
     * @return
     */
    private HTMLTable.CellFormatter getExtraHeaderCellFormatter() {
        if (extraHeader.getRowCount() != 0) {
            extraHeader.removeRow(0);
        }
        extraHeader.setWidth("100%");
        extraHeader.setStyleName("blue-header");
        return extraHeader.getCellFormatter();
    }

    /**
     * Adds a item to the journal table.
     *
     * @param item the item to be added
     */
    public void add(JournalTableItem item) {
        insert(item, getItemsCount(), cellStyle1, cellBorderStyle);
    }

    /**
     * Adds items to the journal table.
     *
     * @param items the items to be added
     */
    public void addItems(JournalTableItem[] items) {
        for (JournalTableItem item : items) {
            add(item);
        }
    }

    /**
     * Returns the item at the given index.
     *
     * @param index the index of the item to return
     * @return the item at the given index or <code>null</code>
     */
    public Object getItem(int index) {
        return items.get(index);
    }

    /**
     * Returns the number of items contained in the journal table.
     *
     * @return the number of items
     */
    public int getItemsCount() {
        return items.size();
    }

    /**
     * Returns the journal table's items.
     *
     * @return the table items
     */
    public List getItems() {
        return items;
    }

    /**
     * Puts new bookmark text into the new row.
     *
     * @param bookmark
     */
    public void setBookMark(String bookmark) {
        this.bookmark = bookmark;

        table.setHTML(rows, 0, "<span style='font-size:16px;'><b>" + bookmark + "</b></span>");
        cellFormatter.setHeight(rows, 0, "40px");
        cellFormatter.setStyleName(rows, 0, cellStyle1);
        rows++;
        items.add(getItemsCount(), bookmark);

        cellStyle1 = cellStyle2;
        cellStyle2 = cellStyle3;
    }

    public void drawGroupBookMark(JournalTableItem item) {
        for (int i = 0; i < item.getValues().length; i++) {
            table.setWidget(rows, i, (Widget) item.getValues()[i]);
            cellFormatter.setStyleName(rows, i, cellStyle1);
            cellFormatter.addStyleName(rows, i, cellBorderStyle);
            cellFormatter.addStyleName(rows, i, cellBorderStyle);
            if (item.getColspans() != null && item.getColspans()[i] != null) {
                table.getFlexCellFormatter().setColSpan(rows, i, item.getColspans()[i]);
            }
            if (item.getBackgroundColor() != null) {
                table.getFlexCellFormatter().getElement(rows, i).getStyle().setBackgroundColor(item.getBackgroundColor());
            }
            if (columns[i].getHorizontalAlignment() != null) {
                cellFormatter.setHorizontalAlignment(rows, i, columns[i].getHorizontalAlignment());
            }
            if (item.getRowStyleName() != null) {
                table.getRowFormatter().setStyleName(rows, item.getRowStyleName());
            }
        }
        rows++;
        items.add(getItemsCount(), item);
        isGroupBookmarkExists = true;
    }

    /**
     * Draws items with their groups.
     *
     * @param groupName
     * @param tableItems
     */
    public void setGroup(String groupName, JournalTableItem[] tableItems) {
//        for (int i = 0; i < columns.length; i++) {
        table.setHTML(rows, 0, "<b>" + groupName + "</b>");
        cellFormatter.setStyleName(rows, 0, isGroupBookmarkExists ? cellStyle2 : cellStyle1);
        cellFormatter.addStyleName(rows, 0, cellBorderStyle);
        table.getFlexCellFormatter().setColSpan(rows, 0, columns.length);
//        }
        rows++;
        items.add(getItemsCount(), groupName);

        if (tableItems != null) {
            for (JournalTableItem tableItem : tableItems) {
                insert(tableItem, getItemsCount(), isGroupBookmarkExists ? cellStyle3 : cellStyle2, cellBorderStyle);
            }
        }
        /*drawEmptyRow();*/
    }

    public void setGroup(FlexTable groupName, JournalTableItem[] tableItems) {
//        for (int i = 0; i < columns.length; i++) {
        table.setWidget(rows, 0, groupName);
        cellFormatter.setStyleName(rows, 0, isGroupBookmarkExists ? cellStyle2 : cellStyle1);
        cellFormatter.addStyleName(rows, 0, cellBorderStyle);
        table.getFlexCellFormatter().setColSpan(rows, 0, columns.length);
//        }
        rows++;
        items.add(getItemsCount(), groupName);

        if (tableItems != null) {
            for (JournalTableItem tableItem : tableItems) {
                insert(tableItem, getItemsCount(), isGroupBookmarkExists ? cellStyle3 : cellStyle2, cellBorderStyle);
            }
        }
        /*drawEmptyRow();*/
    }

    /**
     * Inserts a item into the journal table.
     *
     * @param item  the item to insert
     * @param index the insert location
     */
    public void insert(JournalTableItem item, int index, String styleName, String styleName2) {
        for (int i = 0; i < item.getValues().length; i++) {
            if (item.getValues()[i] != null && item.getValues()[i] instanceof Widget) {
                table.setWidget(rows, i, (Widget) item.getValues()[i]);
            } else {
                String value = item.getValues()[i] != null ? String.valueOf(item.getValues()[i]) : " ";
                table.setHTML(rows, i, value);
                // table.setHTML(rows, i, !value.equals("null") && !value.equals("") ? value : "&nbsp;");
            }
            if (item.getColspans() != null && item.getColspans()[i] != null) {
                table.getFlexCellFormatter().setColSpan(rows, i, item.getColspans()[i]);
            }

            if (item.getColspans() == null) {
                cellFormatter.setWidth(rows, i, columns[i].getWidth() + "px");
            }

            cellFormatter.setStyleName(rows, i, i == 0 ? styleName : "cell-settings-1");
            cellFormatter.addStyleName(rows, i, styleName2);
            if (columns[i].getHorizontalAlignment() != null) {
                cellFormatter.setHorizontalAlignment(rows, i, columns[i].getHorizontalAlignment());
            }
        }
        rows++;
        items.add(index, item);
    }

    /**
     * Draws empty row to the table.
     */
    private void drawEmptyRow() {
        for (int i = 0; i < columns.length; i++) {
            table.setHTML(rows, i, "&nbsp;");
        }
        rows++;
        items.add(getItemsCount(), "&nbsp;");
    }

    /**
     * Journal Table consists of two headers,
     * this is the drawing of second header.
     */
    private void drawHeader() {
        for (int i = 0; i < columns.length; i++) {
            if (columns[i].getTextType() == JournalTableColumn.TYPE_WIDGET)
                table.setWidget(rows, i, (Widget) columns[i].getText());
            else
                table.setHTML(rows, i, columns[i].getText() != null ? "<span>" + columns[i].getText() + "</span>" : "&nbsp;");

            if (columns[i].getWidth() > 0) {
                cellFormatter.setWidth(rows, i, columns[i].getWidth() + "px");
            }
            cellFormatter.setStyleName(rows, i, "blue-header");
            cellFormatter.addStyleName(rows, i, cellStyle1);
            if (columns[i].getHorizontalAlignment() != null) {
                cellFormatter.setHorizontalAlignment(rows, i, columns[i].getHorizontalAlignment());
            }
        }
        table.getRowFormatter().setStyleName(rows, "budget-sheet-table__header fixed-content");
        rows++;
        panel.add(table, DockPanel.SOUTH);
    }

    /**
     * Draws total result of table data.
     *
     * @param item
     */
    public void drawTotal(JournalTableItem item) {
        if (bookmark == null) {
            insert(item, getItemsCount(), isGroupBookmarkExists ? cellStyle2 : cellStyle1, "total-cell-border-style");
        } else {
            insert(item, getItemsCount(), isGroupBookmarkExists ? cellStyle2 : cellStyle1, "total-cell-border-style-2");
        }
    }

    /**
     * Draws bookmark's total results.
     *
     * @param item
     */
    public void drawBookMarkTotal(JournalTableItem item) {
        insert(item, getItemsCount(), "cell-settings-1", "total-cell-border-style");
    }

    public FlexTable getTable() {
        return table;
    }

    public void setEnablePager(boolean enable) {
        this.enablePager = enable;
    }

    public boolean getEnablePager() {
        return this.enablePager;
    }
}
