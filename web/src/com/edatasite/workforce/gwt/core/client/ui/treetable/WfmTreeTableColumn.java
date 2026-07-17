package com.edatasite.workforce.gwt.core.client.ui.treetable;

import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.SortCommand;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 17-Jul-2010
 * Time: 05:41:07
 */

public class WfmTreeTableColumn {

    private int width = 20;
    private String columnName;
    private String columnCode;
    private String columnStyleName;
    private WfmTreeTableCellWidget treeTableCellWidget;
    private SortCommand sortCommand;
    private HasHorizontalAlignment.HorizontalAlignmentConstant alignment = HorizontalPanel.ALIGN_LEFT;

    public WfmTreeTableColumn(String columnName, WfmTreeTableCellWidget treeTableCellWidget) {
        this.columnName = columnName;
        this.treeTableCellWidget = treeTableCellWidget;
    }

    public WfmTreeTableColumn(String columnName, int width, WfmTreeTableCellWidget treeTableCellWidget) {
        this.width = width;
        this.columnName = columnName;
        this.treeTableCellWidget = treeTableCellWidget;
    }

    public WfmTreeTableColumn(String columnName, int width, WfmTreeTableCellWidget treeTableCellWidget, SortCommand sortCommand) {
        this.width = width;
        this.columnName = columnName;
        this.treeTableCellWidget = treeTableCellWidget;
        this.sortCommand = sortCommand;
    }

    public WfmTreeTableColumn(String columnName, String columnCode, int width, WfmTreeTableCellWidget treeTableCellWidget) {
        this.width = width;
        this.columnName = columnName;
        this.columnCode = columnCode;
        this.treeTableCellWidget = treeTableCellWidget;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public WfmTreeTableCellWidget getTreeTableCellWidget() {
        return treeTableCellWidget;
    }

    public HasHorizontalAlignment.HorizontalAlignmentConstant getAlignment() {
        return alignment;
    }

    public void setAlignment(HasHorizontalAlignment.HorizontalAlignmentConstant alignment) {
        this.alignment = alignment;
    }

    public String getColumnCode() {
        return columnCode;
    }

    public void setColumnCode(String columnCode) {
        this.columnCode = columnCode;
    }

    public String getColumnStyleName() {
        return columnStyleName;
    }

    public void setColumnStyleName(String columnStyleName) {
        this.columnStyleName = columnStyleName;
    }

    public SortCommand getSortCommand() {
        return sortCommand;
    }

}
