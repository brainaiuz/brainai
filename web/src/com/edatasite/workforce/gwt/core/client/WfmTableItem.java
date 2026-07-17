package com.edatasite.workforce.gwt.core.client;

import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.Widget;

public class WfmTableItem extends Composite {

    private FlexTable table;
    private CellFormatter cellFormatter;
    private Object[] values;
    private SelectItem selectItem;
    private FileItem fileItem;
    private Widget widget;
    private Widget widget2;

    public WfmTableItem(Object[] values) {
        this.values = values;
        initializeObject();
    }

    public WfmTableItem(SelectItem selectItem, Widget widget) {
        this.selectItem = selectItem;
        this.widget = widget;
        initSelectItem();
    }

    public WfmTableItem(FileItem fileItem, Widget widget1, Widget widget2) {
        this.fileItem = fileItem;
        this.widget = widget1;
        this.widget2 = widget2;
        initFileItem();
    }

    public WfmTableItem(FileItem fileItem, Widget widget1, Widget widget2, boolean description) {
        this.fileItem = fileItem;
        this.widget = widget1;
        this.widget2 = widget2;
        if (description) {
            initFileItem();
        } else {
            initCuttedFileItem();
        }
    }

    private void initializeObject() {
        initilaize();
    }

    private void initilaize() {
        table = new FlexTable();
        initWidget(table);
    }

    private void initSelectItem() {
        initilaize();
        cellFormatter = table.getCellFormatter();
        cellFormatter.setWidth(0, 0, "85%");
        cellFormatter.setWidth(0, 1, "15%");
        table.setHTML(0, 0, selectItem.getName());
        table.setWidget(0, 1, widget);
    }

    private void initFileItem() {
        initilaize();
        table.setWidth("100%");
        cellFormatter = table.getCellFormatter();
        cellFormatter.setWidth(0, 0, "25%");
        cellFormatter.setWidth(0, 1, "15%");
        cellFormatter.setWidth(0, 2, "14%");
        cellFormatter.setWidth(0, 3, "11%");
        cellFormatter.setWidth(0, 4, "35%");
        table.setHTML(0, 0, fileItem.getFileName());
        table.setHTML(0, 1, initSize(fileItem));
        table.setWidget(0, 2, widget);
        table.setWidget(0, 3, widget2);
        table.setHTML(0, 4, fileItem.getDescription());
    }

    private void initCuttedFileItem() {
        initilaize();
        table.setWidth("100%");
        cellFormatter = table.getCellFormatter();
        cellFormatter.setWidth(0, 0, "40%");
        cellFormatter.setWidth(0, 1, "15%");
        cellFormatter.setWidth(0, 2, "14%");
        cellFormatter.setWidth(0, 3, "11%");
        table.setHTML(0, 0, fileItem.getFileName());
        table.setHTML(0, 1, initSize(fileItem));
        table.setWidget(0, 2, widget);
        table.setWidget(0, 3, widget2);
    }

    private String initSize(FileItem item) {
        String fileSize;
        int kb = 1024;
        int mb = kb * 1024;
        if (item.getSize() != null) {
            if (item.getSize().intValue() > kb && item.getSize().intValue() < mb) {
                fileSize = item.getSize().intValue() / kb + "." + String.valueOf(item.getSize().intValue() % kb).substring(0, 1) + " KB";
            } else if (fileItem.getSize().intValue() > mb) {
                fileSize = item.getSize().intValue() / mb + "." + String.valueOf(item.getSize().intValue() % mb).substring(0, 1) + " MB";
            } else {
                fileSize = item.getSize().intValue() + " B";
            }
        } else {
            fileSize = "0 B";
        }
        return fileSize;
    }

    public void deleteRemoveLink() {
        table.remove(widget);
        table.remove(widget2);
        table.setWidget(0, 3, widget);
    }

    public Object[] getValues() {
        return values;
    }

    public Object getValue(int index) {
        return values[index];
    }

    public SelectItem getSelectItem() {
        return selectItem;
    }

    public void setSelectItem(SelectItem selectItem) {
        this.selectItem = selectItem;
    }

    public Widget getWidget() {
        return widget;
    }

    public void setWidget(Widget widget) {
        this.widget = widget;
    }

    public FileItem getFileItem() {
        return fileItem;
    }

    public void setFileItem(FileItem fileItem) {
        this.fileItem = fileItem;
    }
}
