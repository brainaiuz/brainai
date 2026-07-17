package com.edatasite.workforce.gwt.core.client.ui.table;

import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.MaterialPanel;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: May 19, 2010
 * Time: 4:15:57 PM
 * To change this template use File | Settings | File Templates.
 */
public class Table extends Composite {

    /**
     * Styles which are stored in Core.cache.css;
     */
    private final String blueHeader = "table-blue-header";
    private final String borderStyle = "tree-border-style";
    private final String blueBorder = "blue-border";
    private final String cellSettings1 = "cell-settings-1";
    private String whiteSpace = " whiteSpace";
    private String cellSettings;

    private final int height = 20;

    private TableColumn[] columns;

    private MaterialPanel panel = new MaterialPanel();
    private FlexTable header = new FlexTable();
    private ScrollPanel footer = new ScrollPanel();
    private KpiCheckBox checkbox = new KpiCheckBox();
    private FlexTable table = new FlexTable();
    private HTMLTable.CellFormatter cellFormatter = table.getCellFormatter();

    private ArrayList<TableItem> items = new ArrayList<>();
    private boolean showCheckBox = true;

    public Table(TableColumn[] columns) {
        this.columns = columns;

        onRender();
    }

    public Table(TableColumn[] columns, boolean showCheckBox) {
        this.columns = columns;
        this.showCheckBox = showCheckBox;
        onRender();
    }

    /**
     * ex. new Table(bla-bla).setWordWrap(true)
     *
     * @param wordWrap sets Whitspace for cellText
     * @return instance which is being created
     *         by default white-space:nowrap
     */
    public Table setWordWrap(boolean wordWrap) {
        whiteSpace = wordWrap ? whiteSpace : "";
        return this;
    }

    public void add(TableItem[] items) {
        for (TableItem item : items) {
            insert(getItemCount(), item);
        }
        resize();
    }

    public void addItem(TableItem item) {
        insert(getItemCount(), item);
        resize();
    }

    public void clear() {
        table.clear();
        table.removeAllRows();
        items.clear();
        checkbox.setEnabled(false);
    }

    public TableItem getItem(int index) {
        return items.get(index);
    }

    public int getItemCount() {
        return items.size();
    }

    public void removeItem(TableItem item) {
        if (items.indexOf(item) != -1) {
            table.removeRow(items.indexOf(item));
            items.remove(item);
        }
    }

    public void setColumnText(int column, String text) {
        header.setHTML(0, column, "<b>" + text + "</b>");
    }

    public void setHeight(int height) {
        footer.setHeight(height + "px");
    }

    public void setSize(int width, int height) {
        footer.setSize(width + "px", height + "px");
    }

    public void setWidth(int width) {
        footer.setWidth(width + "px");
    }

    protected void insert(int index, TableItem item) {
        items.add(index, item);

        int column = 0, row = table.getRowCount();
//        cellFormatter.setWidth(row, column, "30px");
        if (showCheckBox) {
            if (!checkbox.isEnabled()) {
                checkbox.setEnabled(true);
            }

            table.setWidget(row, column, item.getCheckbox());
            cellFormatter.setAlignment(row, column, HasAlignment.ALIGN_CENTER, HasAlignment.ALIGN_MIDDLE);
            column++;
        } else {
            cellFormatter.setAlignment(row, column, HasAlignment.ALIGN_LEFT, HasAlignment.ALIGN_MIDDLE);
        }

        for (TableItemValue value : item.getValues()) {
//            if (showCheckBox) {
//                cellFormatter.setWidth(row, column, columns[column - 1].getWidth() + "px");
//            } else {
//                cellFormatter.setWidth(row, column, columns[column].getWidth() + "px");
//            }
            cellFormatter.setHeight(row, column, (height + 3) + "px");
            cellFormatter.setStyleName(row, column, cellSettings1 + " " + blueBorder + whiteSpace);
            Widget widget;
            if(value != null && value.getValue() != null){
                if (value.getValue() instanceof Widget) {
                    widget = (Widget) value.getValue();
                } else {
                    widget = new HTML(value.getValue().toString());
                }
                table.setWidget(row, column, widget);
            }
            column++;
        }
    }

    private void buildHeader() {
        HTMLTable.CellFormatter cellFormatter = header.getCellFormatter();

        int col = 0;
//        cellFormatter.setWidth(0, col, "30px");
        cellFormatter.setAlignment(0, col, HasAlignment.ALIGN_CENTER, HasAlignment.ALIGN_MIDDLE);
        if (showCheckBox) {
            header.setWidget(0, col, checkbox);
//            cellFormatter.addStyleName(0, col, blueHeader);
            col++;

            checkbox.setEnabled(false);
            checkbox.addValueChangeHandler(event -> {
                for (TableItem item : items) {
                    item.getCheckbox().setValue(event.getValue());
                }
            });
        } else {
//            cellFormatter.addStyleName(0, col, blueHeader);
        }

        for (TableColumn column : columns) {
            header.setHTML(0, col, "<span>" + column.getText() + "</span>");

            cellFormatter.setHeight(0, col, (height + 5) + "px");
            cellFormatter.setAlignment(0, col, HasAlignment.ALIGN_CENTER, HasAlignment.ALIGN_MIDDLE);
//            cellFormatter.addStyleName(0, col, blueHeader);
            col++;
        }
    }

    private void onRender() {
        panel.add(header);
        panel.add(footer);

        footer.add(table);
        footer.setStyleName("selectPanelWidget__table-body");
        header.setWidth("100%");
        table.setWidth("100%");
        header.setCellPadding(0);
        header.setCellSpacing(0);
        header.setStyleName("selectPanelWidget__table-header");

        table.setCellPadding(0);
        table.setCellSpacing(0);
        table.setBorderWidth(1);
        table.setStyleName("cellBasedWidget-mod");

        buildHeader();
        initWidget(panel);
    }

    private void resize() {
        DeferredCommand.addCommand(() -> {
            HTMLTable.CellFormatter headerFormatter = header.getCellFormatter();
            for (int column = 0; column < columns.length; column++) {
                int width = cellFormatter.getElement(0, column).getOffsetWidth() - 1;//Because it has border that its width 1px;
                if (column == columns.length - 1) {
                    int scrollWidth = TableUtils.getScrollBarWidth(true);
                    if (scrollWidth < 0) {
                        scrollWidth = 0;
                    }
                    width += scrollWidth;
                }
                headerFormatter.setWidth(0, column, width + "px");
            }
        });
    }

    public String getCellSettings() {
        return cellSettings;
    }

    public void setCellSettings(String cellSettings) {
        this.cellSettings = cellSettings;
    }

    public void setMainWidth(String widht) {
        panel.setWidth(widht);
    }
}
