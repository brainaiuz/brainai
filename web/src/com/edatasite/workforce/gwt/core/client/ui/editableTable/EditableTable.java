package com.edatasite.workforce.gwt.core.client.ui.editableTable;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmWindow;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.CustomCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LinkableCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LinkedLinkableCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.cell.LookUpCell;
import com.edatasite.workforce.gwt.core.client.ui.editableTable.listeners.EditableTableListener;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.invoice.client.ui.SmartProductLookUp;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.jquery.client.api.JQuery;
import org.gwt.advanced.client.datamodel.Editable;
import org.gwt.advanced.client.datamodel.EditableGridDataModel;
import org.gwt.advanced.client.ui.EditCellListener;
import org.gwt.advanced.client.ui.widget.EditableGrid;
import org.gwt.advanced.client.ui.widget.GridPanel;
import org.gwt.advanced.client.ui.widget.cell.AbstractCell;
import org.gwt.advanced.client.ui.widget.cell.GridCell;
import org.gwt.advanced.client.ui.widget.cell.ImageCell;
import org.gwt.advanced.client.ui.widget.cell.LabelCell;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 2/10/12
 * Time: 7:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class EditableTable extends Composite {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private static final NumberFormat numberFormat = NumberFormat.getFormat(",##0.00");

    /**
     * Grid panel container of showRemoveCell table
     */
    private GridPanel gridPanel;

    /**
     * grid is Editable Cell table
     */
    private EditableGrid grid;

    /**
     * Editable Cell table column config
     */
    private ColumnConfig[] columns;

    /**
     * Editable cell table listener {ADD ROW, REMOVE ROW}
     */
    private EditableTableListener listener;

    private Map<String, Integer> columnsMap;

    private final HashMap valid;
    private final HashMap itemValid;

    private final List<String> requiredFields;

    public Integer isValidRows = 0;

    private boolean showRemoveCell = true;
    private boolean showAddCell = false;
    private boolean disableRemoveCell = false;
    private boolean addOnClick = true;
    private int ACTION_COLUMN_WIDTH = 35;
    private double containerWidth = 0;
    private boolean forceWidthInPixel = false;

    private boolean removeAllRows;
    private String formtType;
    private Command removeRowListener;
    private boolean draggable;
    private WfmButton2 runWebhooks;

    public EditableTable(ColumnConfig[] columns) {
        this(columns, true);
    }

    public EditableTable(ColumnConfig[] columns, boolean showRemoveCell) {
        this(columns, showRemoveCell, false);
    }

    public EditableTable(ColumnConfig[] columns, boolean showRemoveCell, boolean showAddCell) {
        this(columns, showRemoveCell, showAddCell, true);
    }

    public EditableTable(ColumnConfig[] columns, boolean showRemoveCell, boolean showAddCell, boolean addOnClick) {

        this.columns = columns;
        this.showRemoveCell = showRemoveCell;
        this.showAddCell = showAddCell;
        this.addOnClick = addOnClick;
        creatColumnsMap();
        valid = new HashMap();
        itemValid = new HashMap();
        requiredFields = new ArrayList<>();
        //initialize widget content
        onInitialize();
    }


    /**
     * Add row to showRemoveCell table
     *
     * @param data
     */
    public void addRow(Object[] data) {
        addRow(null, data);
    }

    boolean draggableProcessing;
    public void addRow(Integer position, Object[] data) {
        if (data == null) {
            data = new Object[grid.getHeaders().length];
        } else {
            Object[] temp = new Object[data.length + 1];
            temp[0] = "";
            System.arraycopy(data, 0, temp, 1, data.length);
            data = temp;
        }

        if (showRemoveCell || showAddCell) {
            data = generateRowDataWithRemoveOption(data);
        }

        Editable dataModel = grid.getModel();

        Object[] rowData = data;

        if (position == null) {
            position = grid.getModelRow(grid.getRowCount());
            dataModel.addRow(position, rowData);
        } else {
            dataModel.updateRow(position, rowData);
        }

        int columnCount = columns.length;
        if (!showRemoveCell && !showAddCell) {
            columnCount--;
        }
        for (int index = 0; index <= columnCount; index++) {

            if (index != columns.length) {
                if (!columns[index].isPixel() && columns[index].isForceWidthInPercent()) {
                    grid.getCellFormatter().getElement(position, index).getStyle().setWidth(columns[index].getWidth(), Style.Unit.PCT);
                } else if (columns[index].isPixel() || forceWidthInPixel) {
                    grid.getCellFormatter().getElement(position, index).getStyle().setWidth(columns[index].getWidth(), Style.Unit.PX);
                } else {
                    grid.getCellFormatter().getElement(position, index).getStyle().setWidth(columns[index].getWidth() * 100 / containerWidth, Style.Unit.PCT);
                }
                grid.getCellFormatter().getElement(position, index).setAttribute("column-reference", columns[index].getTitle().toLowerCase().replace(" ", "_"));

                String customStyle = columns[index].getCustomStyleName();
                if (customStyle != null && !customStyle.isEmpty()) {
                    grid.getCellFormatter().getElement(position, index).addClassName(customStyle);
                }
            } else {
                grid.getCellFormatter().getElement(position, index).getStyle().setWidth(ACTION_COLUMN_WIDTH, Style.Unit.PX);
            }
            gridPanel.setSortableColumn(index, false);
        }
        if (draggable) {
            Timer delayRunner = new Timer() {
                @Override
                public void run() {
                    if (draggableProcessing) {
                        return;
                    }
                    draggableProcessing = true;
                    makeDraggable(getElement());
                    draggableProcessing = false;
                }
            };
            delayRunner.schedule(100);
        }

        grid.setCurrentRow(position);
    }

    private native void makeDraggable(Element element) /*-{
        $wnd.advancedGrid_bind_dnd_events(element);
    }-*/;

    private native void reIndexDraggableItems(Element element) /*-{
        $wnd.advancedGrid_reindex_items(element);
    }-*/;

    public void setDraggable(boolean draggable) {
        this.draggable = draggable;
        if (draggable) {
            addStyleName("advanced-GridPanel--draggable");
        } else {
            removeStyleName("advanced-GridPanel--draggable");
        }
    }

    /**
     * Add multi row to showRemoveCell table
     *
     * @param data
     */
    public void addRows(Object[][] data) {
        if (data != null) {
            for (Object[] aData : data) {
                addRow(aData);
            }
        }
    }


    protected void onInitialize() {
        gridPanel = new GridPanel();

        int sizeColumn = showRemoveCell || showAddCell ? columns.length + 1 : columns.length;
        ACTION_COLUMN_WIDTH = showRemoveCell && showAddCell ? ACTION_COLUMN_WIDTH * 2 : ACTION_COLUMN_WIDTH;

        //showRemoveCell table header titles list
        String[] headers = new String[sizeColumn];

        //showRemoveCell table cell type classes
        Class[] columnWidgetClasses = new Class[sizeColumn];

        for (int index = 0; index < sizeColumn; index++) {
            boolean isRemoveColumn = (showRemoveCell || showAddCell) && index == columns.length;
            headers[index] = !isRemoveColumn ? columns[index].getTitle() : "";
            columnWidgetClasses[index] = !isRemoveColumn ? columns[index].getCell() : ImageCell.class;

            if (index != columns.length && columns[index].isRequired()) {
                requiredFields.add(columns[index].getName());
            }
        }


        gridPanel.createEditableGrid(headers, columnWidgetClasses, null);
        EditableTableEventManager eventManager = new EditableTableEventManager(gridPanel, showRemoveCell || showAddCell, addOnClick);
        gridPanel.setGridEventManager(eventManager);
        grid = gridPanel.getGrid();
        grid.setGridCellfactory(new EditableTableCellFactory(grid, columns));
        grid.setModel(new EditableGridDataModel(null));


        //this params contains sum of all columns width
        containerWidth = 0;
        for (int index = 0; index < columns.length; index++) {
            containerWidth += columns[index].getWidth();
        }
        containerWidth += ACTION_COLUMN_WIDTH;

        if (containerWidth > MainLayout.get().getMainContent().getElement().getClientWidth()) {
            forceWidthInPixel = true;
        }

        //set column size of showRemoveCell cell table
        for (int index = 0; index <= columns.length; index++) {

            if (index != columns.length) {
                if (!columns[index].isPixel() && columns[index].isForceWidthInPercent()) {
                    grid.setColumnWidth(index, columns[index].getWidth(), false);
                } else if (columns[index].isPixel() || forceWidthInPixel) {
                    grid.setColumnWidth(index, columns[index].getWidth(), true);
                } else {
                    grid.setColumnWidth(index, columns[index].getWidth() * 100 / containerWidth);
                }
            } else {
                grid.setColumnWidth(index, ACTION_COLUMN_WIDTH, true);
            }

            gridPanel.setSortableColumn(index, false);
        }
        grid.getHeaderTable().addStyleName("dnd-item-table__heading");
        grid.getBodyTable().addStyleName("dnd-item-table file--EditableTable");

        gridPanel.display();
        gridPanel.setResizable(false);
        gridPanel.remove(gridPanel.getTopPanel());
        gridPanel.getBottomPanel().clear();

        initializeListeners();

        runWebhooks = new WfmButton2(wfmStrings.update());
        runWebhooks.setVisible(false);
        gridPanel.getBottomPanel().add(runWebhooks);

        initWidget(gridPanel);
    }

    private void initializeListeners() {
        grid.addEditCellListener(new EditCellListener() {
            @Override
            public boolean onStartEdit(GridCell cell) {
                int rowCount = grid.getRowCount();

                if ((grid.getCurrentRow() == rowCount - 1) && showRemoveCell && addOnClick) {
                    listener.addRow();
                }
                return true;
            }

            @Override
            public boolean onFinishEdit(GridCell cell, Object newValue) {
                return true;
            }
        });
    }

    @Override
    protected void onAttach() {
        super.onAttach();
        JQuery.$(getElement()).on("dragEnd", (e, parent) -> {

            Element parent_ = (Element) parent;
            ArrayList<Object[]> list = new ArrayList<>();

            // parent child counts is line of rowitems, parent(table) > getChild(tr) > getChild(td)
            for (int i = 0; i < parent_.getChildCount(); i++) {
                for (int j = 0; j < parent_.getChild(i).getChildCount(); j++) {
                    // first column has to be drag drop, so we will take it.
                    if (((Element) parent_.getChild(i).getChild(j).cast()).hasAttribute("dragIndex")) {
                        int temp = Integer.parseInt(((Element) parent_.getChild(i).getChild(j).cast()).getAttribute("dragIndex"));
                        Object[] data = grid.getModel().getRowData(temp);
                        Object[] newData = new Object[data.length - 2];
                        System.arraycopy(data, 1, newData, 0, data.length - 2);
                        list.add(newData);
                        break;
                    }
                }

            }

            grid.getModel().removeAll();
            grid.getModel().clearRemovedRows();

            for (Object[] data : list) addRow(data);

            e.stopImmediatePropagation();
            e.stopPropagation();
            e.preventDefault();

            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_DRAG_END, null, EditableTable.this);
            return null;
        });
    }

    private Object[] generateRowDataWithRemoveOption(Object[] rowData) {
        Object[] data = new Object[rowData.length + 1];
        int index = 0;
        boolean disableDelete = false;
        String deleteMessage = null;

        for (Object item : rowData) {
            if (item instanceof SmartProductLookUp && (((SmartProductLookUp) item).isSoldOut() || ((SmartProductLookUp) item).getUsedInGrn() != null)) {
                disableDelete = true;
                if (((SmartProductLookUp) item).getUsedInGrn() != null) {
                    deleteMessage = wfmStrings.cannotRemoveItemUsedInGrn();
                }
            } else if (disableRemoveCell) {
                disableDelete = true;
                if (Constants.SALE_QUOTE.equals(formtType)) {
                    deleteMessage = accountingStrings.cannotDeleteItemSalesOrderUsedInShipment();
                } else if (Constants.SALE_INVOICE.equals(formtType)) {
                    deleteMessage = accountingStrings.cannotMakeChangesConvertedInvoice();
                } else if (Constants.PURCHASE_ORDER.equals(formtType)) {
                    deleteMessage = accountingStrings.purchaseOrderCanNotBeChangedBecouseConverted();
                } else {
                    deleteMessage = wfmStrings.youCannotDeleteThisItem();
                }
            }
            data[index++] = item;
        }
        data[index] = createImageCell(disableDelete, deleteMessage);

        return data;
    }

    private Widget createImageCell(boolean disableDelete, String deleteMessage) {
        HorizontalPanel panel = new HorizontalPanel();
        panel.setStyleName("editable-cell__actions");
        panel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);

        Icon addIcon = new Icon();
        addIcon.addStyleName("btn--icon");
        addIcon.setTitle(wfmStrings.insertRowBelow());
        SvgIcon plusIcon = new SvgIcon(SvgEnum.plus);
        addIcon.add(plusIcon);
        addIcon.addClickHandler(event -> {
            Timer timer = new Timer() {
                @Override
                public void run() {
                    int rowCount = grid.getRowCount();
                    int currentRow = grid.getCurrentRow();

                    ArrayList<Object[]> list = new ArrayList<>();
                    for (int i = rowCount - 1; i > currentRow; i--) {
                        Object[] data = grid.getModel().getRowData(i);
                        list.add(copyOf(data, data.length - 2));
                    }

                    for (int i = rowCount - 1; i > currentRow; i--) {
                        grid.getModel().removeRow(i);
                    }

                    grid.getModel().clearRemovedRows();
                    listener.addRow();

                    Collections.reverse(list);
                    for (Object[] data : list) {
                        addRow(data);
                    }
                }
            };
            timer.schedule(100);
        });

        Icon removeIcon = new Icon();
        removeIcon.setStyleName(disableDelete ? " disabled" : "");
        removeIcon.addStyleName("btn--icon");
        SvgIcon trashIcon = new SvgIcon(SvgEnum.trash2);
        removeIcon.add(trashIcon);
        removeIcon.setTitle(wfmStrings.removeTableRow());
        if (!disableDelete) {
            removeIcon.addClickHandler(event -> {
                if (removeRowListener != null) {
                    Timer timer = new Timer() {

                        @Override
                        public void run() {
                            removeRowListener.execute();
                        }
                    };
                    timer.schedule(100);
                } else {
                    Timer timer = new Timer() {
                        @Override
                        public void run() {
                            if (grid.getRowCount() > 1 || isRemoveAllRows()) {
                                int gridRow = grid.getCurrentRow();
                                grid.getModel().removeRow(gridRow);
                                if (listener != null) {
                                    listener.removeRow();
                                }
                                reIndexDraggableItems(getElement());
                            } else {
                                WfmWindow.alert(wfmStrings.youCanNotRemoveOneLineItem());
                            }
                        }
                    };
                    timer.schedule(100);
                }
            });
        } else if (deleteMessage != null) {
            removeIcon.addClickHandler(event -> {
                Info.warn(deleteMessage);
            });
        }
        if (showAddCell) {
            panel.add(addIcon);
        }
        if (showRemoveCell) {
            panel.add(removeIcon);
        }
        return panel;
    }

    private Object[] copyOf(Object[] old, int newLength) {
        Object[] data = new Object[newLength];
        System.arraycopy(old, 1, data, 0, newLength);
        return data;
    }

    private void creatColumnsMap() {
        ColumnConfig firstEpmtyColumn = new ColumnConfig(LabelCell.class, "first_epmty_column", 15);
        firstEpmtyColumn.setPixel(true);
        ColumnConfig[] tempArrs = new ColumnConfig[columns.length + 1];
        tempArrs[0] = firstEpmtyColumn;
        System.arraycopy(columns, 0, tempArrs, 1, columns.length);
        columns = tempArrs;

        int i = 0;
        columnsMap = new LinkedHashMap<>();
        for (ColumnConfig columnConfig : columns) {
            columnsMap.put(columnConfig.getName(), i);
            i++;
        }
    }

    public void removeAllRows() {
        while (grid.getRowCount() > 0) {
            grid.getModel().removeRow(0);
        }
    }


    /**
     * Sets columns status to not valid.
     *
     * @param rowId
     * @param columnKey
     */
    public void notValid(int rowId, String columnKey) {
        Integer columnID = getColumnId(columnKey);
        if (columnID != null) {
            Widget widget = grid.getWidget(rowId, columnID);
            if (widget != null) {
                if (widget instanceof CustomCell) {
                    if (((CustomCell) widget).getCustomWidget() instanceof TextBox) {
                        ((CustomCell) widget).getCustomWidget().addStyleName("x-form-invalid");
                    } else if (((CustomCell) widget).getCustomWidget() instanceof DataListBox) {
                        ((DataListBox) ((CustomCell) widget).getCustomWidget()).addStyleNameToInput("x-form-invalid");
                    }
                } else if (widget instanceof LookUpCell) {
                    ((LookUpCell) widget).getLookUp().getSuggestBox().addStyleName("x-form-invalid");
                } else {
                    widget.setStyleName("x-form-invalid");
                }
                ((AbstractCell) widget).displayActive(true);
            }
        }
    }

    /**
     * Checks current column valid or not.
     *
     * @param columnKey
     */
    public boolean isValid(String columnKey) {
        Boolean isValid = (Boolean) valid.get(columnKey);
        return isValid != null ? isValid : true;
    }

    public void setColumnValid(String columnKey) {
        valid.put(columnKey, false);
    }


    /**
     * Resets all validation statuses.
     * All columns are valid now.
     *
     * @param rowId
     */
    public void resetValidation(Integer rowId) {

        for (Map.Entry<String, Integer> entry : columnsMap.entrySet()) {
            Widget widget = grid.getWidget(rowId, getColumnId(entry.getKey()));
            widget.removeStyleName("x-form-invalid");
        }

        valid.clear();

    }


    /**
     * @return calculation result
     */
    public double calculateTotal(String columnKey) {

        double total = 0d;
        for (int i = 0; i < grid.getRowCount(); i++) {
            HasText label = (HasText) getColumnById(i, columnKey);
            double value = numberFormat.parse(label.getText());
            total += value;
        }
        return total;
    }

    /**
     * Set current row status (valid or not).
     *
     * @param rowId
     * @param isValid
     */
    public void setItemValid(Integer rowId, Boolean isValid) {
        itemValid.put(rowId, isValid);
    }

    /**
     * Checks current row valid or not.
     *
     * @param rowId
     */

    public Boolean isItemValid(Integer rowId) {
        Boolean isValid = (Boolean) itemValid.get(rowId);
        return isValid == null ? false : isValid;
    }

    public Boolean validateFields(Integer rowId) {
        if (valid.size() == 0) {
            return true;
        } else if (valid.size() < requiredFields.size()) {
            for (String column : requiredFields) {
                if (valid.get(column) != null) {
                    notValidFields(rowId);
                    break;
                }
            }
        }
        return false;
    }

    public void notValidFields(Integer rowId) {
        for (String requiredField : requiredFields) {

            if (!isValid(requiredField)) {
                notValid(rowId, requiredField);
            }
        }
    }

    public Integer getColumnId(String key) {
        return columnsMap.get(key);
    }

    public Widget getColumnById(Integer row, String key) {
        Widget result = null;
        if (columnsMap.containsKey(key)) {
            Widget widget = grid.getWidget(row, getColumnId(key));
            if (widget instanceof LookUpCell) {
                result = ((LookUpCell) widget).getLookUp();
            } else if (widget instanceof CustomCell) {
                result = ((CustomCell) widget).getCustomWidget();
            } else if (widget instanceof LinkableCell) {
                result = ((LinkableCell) widget).getCustomWidget();
            } else if (widget instanceof LinkedLinkableCell) {
                result = ((LinkedLinkableCell) widget).getCustomWidget();
            } else {
                result = widget;
            }
        }
        return result;
    }

    public Widget getColumnCellWidgetById(Integer rowId, String key) {
        Widget result = null;
        if (columnsMap.containsKey(key)) {
            result = grid.getWidget(rowId, getColumnId(key));
        }
        return result;
    }

    public void refreshCustomCellDisplayValue(Integer rowId, String key) {
        CustomCell customCell = (CustomCell) getColumnCellWidgetById(rowId, key);
        customCell.InActive();
    }

    public void incValidRow() {
        isValidRows++;
    }

    public Integer getRequiredFieldCount() {
        return requiredFields.size();
    }

    public EditableTableListener getListener() {
        return listener;
    }

    public void setListener(EditableTableListener listener) {
        this.listener = listener;
        if (gridPanel.getGridEventManager() != null) {
            ((EditableTableEventManager) gridPanel.getGridEventManager()).setListener(listener);
        }
    }

    public EditableGrid getGrid() {
        return grid;
    }

    public GridPanel getGridPanel() {
        return gridPanel;
    }

    public Integer getValidRows() {
        return isValidRows;
    }

    public void setValidRows(Integer validRows) {
        isValidRows = validRows;
    }

    public int getRowCount() {
        return grid.getRowCount();
    }

    public Command getRemoveRowListener() {
        return removeRowListener;
    }

    public void setRemoveRowListener(Command removeRowListener) {
        this.removeRowListener = removeRowListener;
    }

    public boolean isRemoveAllRows() {
        return removeAllRows;
    }

    public void setRemoveAllRows(boolean removeAllRows) {
        this.removeAllRows = removeAllRows;
    }

    public void setShowAddCell(boolean showAddCell) {
        this.showAddCell = showAddCell;
    }

    public void setShowRemoveCell(boolean showRemoveCell) {
        this.showRemoveCell = showRemoveCell;
    }

    public ColumnConfig[] getColumns() {
        return columns;
    }

    public void shiftRows(int from, int n) {
        int rowCount = grid.getRowCount();

        ArrayList<Object[]> list = new ArrayList<>();
        for (int i = rowCount - 1; i > from; i--) {
            Object[] data = grid.getModel().getRowData(i);
            list.add(copyOf(data, data.length - 2));
        }

        for (int i = rowCount - 1; i > from; i--) {
            grid.getModel().removeRow(i);
        }

        grid.getModel().clearRemovedRows();
        for (int i = 0; i < n; i++) {
            listener.addRow();
        }

        Collections.reverse(list);
        list.forEach(this::addRow);
    }

    public List<String> getRequiredFields() {
        return requiredFields;
    }

    public WfmButton2 getRunWebhooks() {
        return runWebhooks;
    }

    public void setDisableRemoveCell(boolean disableRemoveCell) {
        this.disableRemoveCell = disableRemoveCell;
    }

    public boolean isDisableRemoveCell() {
        return disableRemoveCell;
    }

    public void setFormtType(String formtType) {
        this.formtType = formtType;
    }

}
