package com.edatasite.workforce.gwt.core.client.ui.dynamicTable;

import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.ColumnStatements;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 18.10.2008
 * Time: 11:39:58
 * To change this template use File | Settings | File Templates.
 */
public class DynamicTableColumn {

    private String columnName;
    private String colunmId;
    private Integer columnWidth;
    private boolean forceWidthInPercent;
    private ColumnStatements columnStatements;
    private boolean checkInput;
    private Integer colspan;
    private boolean visible = true;
    private boolean saveWhiteSpace = false;
    private String style;
    private SortCommand sortCommand;

    /**
     * column has pixel or percentage
     */
    private boolean isPixel;


    public DynamicTableColumn(String columnName, String columnId, Integer columnWidth) {
        this(columnName, columnId, null, columnWidth, false, null, null);
    }

    public DynamicTableColumn(String columnName, String columnId, Integer columnWidth, String style) {
        this(columnName, columnId, null, columnWidth, false, style, null);
    }

    public DynamicTableColumn(String columnName, String columnId, Integer columnWidth, String style, SortCommand sortCommand) {
        this(columnName, columnId, null, columnWidth, false, style, sortCommand);
    }

    public DynamicTableColumn(String columnName, String columnId, ColumnStatements columnStatements, Integer columnWidth) {
        this(columnName, columnId, columnStatements, columnWidth, false, null, null);
    }

    public DynamicTableColumn(String columnName, String columnId, Integer columnWidth, boolean checkInput) {
        this(columnName, columnId, null, columnWidth, checkInput, null, null);
    }

    public DynamicTableColumn(String columnName, String columnId, Integer columnWidth, boolean checkInput, String style) {
        this(columnName, columnId, null, columnWidth, checkInput, style, null);
    }

    public DynamicTableColumn(String columnName, String columnId, Integer columnWidth, SortCommand sortCommand) {
        this(columnName, columnId, null, columnWidth, false, null, sortCommand);
    }

    public DynamicTableColumn(String columnName, String columnId, ColumnStatements columnStatements, Integer columnWidth, boolean checkInput) {
        this(columnName, columnId, columnStatements, columnWidth, checkInput, null, null);
    }


    public DynamicTableColumn(String columnName, String columnId, ColumnStatements columnStatements, Integer columnWidth, boolean checkInput, String style, SortCommand sortCommand) {
        this.columnName = columnName.toUpperCase();
        this.columnWidth = columnWidth;
        this.columnStatements = columnStatements;
        this.checkInput = checkInput;
        this.colunmId = columnId;
        this.style = style;
        this.sortCommand = sortCommand;
    }

    public boolean hasValidationMessage() {
        if (columnStatements != null) {

            return columnStatements.getNotValidMessage() != null;
        }
        return false;
    }

    public boolean hasInfoMessage() {
        if (columnStatements != null) {

            return columnStatements.getInfoMessage() != null;
        }
        return false;
    }

    public String getValidationMessage() {
        return columnStatements == null ? null : columnStatements.getNotValidMessage();
    }

    public String getInfoMessage() {
        return columnStatements == null ? null : columnStatements.getInfoMessage();
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Integer getColumnWidth() {
        return columnWidth;
    }

    public void setColumnWidth(Integer columnWidth) {
        this.columnWidth = columnWidth;
    }

    public boolean isCheckInput() {
        return checkInput;
    }

    public void setCheckInput(boolean checkInput) {
        this.checkInput = checkInput;
    }

    public String getColunmId() {
        return colunmId;
    }

    public void setColunmId(String colunmId) {
        this.colunmId = colunmId;
    }

    public ColumnStatements getColumnStatements() {
        return columnStatements;
    }

    public Integer getColspan() {
        return colspan;
    }

    public void setColspan(Integer colspan) {
        this.colspan = colspan;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public SortCommand getSortCommand() {
        return sortCommand;
    }

    public void setSortCommand(SortCommand sortCommand) {
        this.sortCommand = sortCommand;
    }

    public boolean isForceWidthInPercent() {
        return forceWidthInPercent;
    }

    public void setForceWidthInPercent(boolean forceWidthInPercent) {
        this.forceWidthInPercent = forceWidthInPercent;
    }

    public void setPixel(boolean pixel) {
        this.isPixel = pixel;
    }

    public boolean isPixel() {
        return isPixel;
    }


    public boolean isSaveWhiteSpace() {
        return saveWhiteSpace;
    }

    public void setSaveWhiteSpace(boolean saveWhiteSpace) {
        this.saveWhiteSpace = saveWhiteSpace;
    }
}
