package com.edatasite.workforce.gwt.core.client.ui.editableTable;

import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.*;
import org.gwt.advanced.client.ui.widget.EditableGrid;
import org.gwt.advanced.client.ui.widget.cell.DefaultGridCellFactory;
import org.gwt.advanced.client.ui.widget.cell.GridCell;
import org.gwt.advanced.client.ui.widget.cell.HeaderCell;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 2/12/12
 * Time: 2:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class EditableTableCellFactory extends DefaultGridCellFactory {

    private ColumnConfig[] columnConfigs;

    /**
     * Creates an instance of the factory.
     *
     * @param grid is a grid instance.
     */
    public EditableTableCellFactory(EditableGrid grid) {
        super(grid);
    }

    public EditableTableCellFactory(EditableGrid grid, ColumnConfig[] columnConfigs) {
        super(grid);
        this.columnConfigs = columnConfigs;
    }

    @Override
    public GridCell create(int row, int column, Object data) {
        GridCell result;
        Class columnType = getGrid().getColumnWidgetClasses()[getGrid().getModelColumn(column)];
        //   String columnName = getGrid().getHeaders()[column];
        if (LookUpCell.class.equals(columnType)) {
            if (columnConfigs != null && column < columnConfigs.length) {
                result = new LookUpCell(columnConfigs[column].getCustomStyleName());
            } else {
                result = new LookUpCell(null);
            }
        } else if (CustomCell.class.equals(columnType)) {
            if (columnConfigs != null && column < columnConfigs.length) {
                result = new CustomCell(columnConfigs[column].getCustomStyleName());
            } else {
                result = new CustomCell(null);
            }
        } else if (LinkableCell.class.equals(columnType)) {
            if (columnConfigs != null && column < columnConfigs.length) {
                result = new LinkableCell(columnConfigs[column].getCustomStyleName());
            } else {
                result = new LinkableCell(null);
            }
        } else if (LinkedLinkableCell.class.equals(columnType)) {
            if (columnConfigs != null && column < columnConfigs.length) {
                result = new LinkedLinkableCell(columnConfigs[column].getCustomStyleName());
            } else {
                result = new LinkedLinkableCell(null);
            }
        } else {
            return super.create(row, column, data);
        }

        prepareCell(result, row, column, data);

        return result;
    }


    public HeaderCell create(int column, String header) {
        HeaderCell cell = new CustomHeaderCell();
        if (columnConfigs != null && column < columnConfigs.length) {
            String style = columnConfigs[column].getCustomStyleName();
            if (style != null) {
                ((CustomHeaderCell) cell).addStyleName(style);
            }
        }

        cell.setSortable(false);
        cell.setAscending(false);
        cell.setSorted(false);

        prepareCell(cell, 0, column, header);

        return cell;
    }
}
