package com.edatasite.workforce.gwt.core.client.ui.dynamicTable;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgIcon;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.headers.AbstractHeader;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.AddListener;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.AddNewListener;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.DynamicTableListener;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.validation.InputValidator;
import com.edatasite.workforce.gwt.core.client.ui.entryPoints.carcass.MainLayout;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasHorizontalAlignment.HorizontalAlignmentConstant;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment.VerticalAlignmentConstant;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Table designed for Expenses and Invoicing sections.
 * Has special interface for adding rows manually and programmatically.
 * Has validation interface.
 * If needed values in the columns can be dynamically calculated.</p>
 * <p/>
 * <p>You may define your own header style by inherit AbstractDynamicTable class
 * and implementing getHeader() function.</p>
 * <p/>
 * <p>You may define your own border style by inherit AbstractDynamicTable class
 * and implementing getBorderStyle() function.</p>
 * <p/>
 * <p>Styles used from Core.css:</p>
 * <p/>
 * <p>.default-color{ <br />
 * background-color:white; <br />
 * }</p>
 * <p/>
 * <p>.description-default-color{ <br />
 * background-color:white; <br />
 * border:1px solid #819cb9; <br />
 * overflow:auto; <br />
 * }</p>
 * <p/>
 * <p>.input-default-color{ <br />
 * background-color:white; <br />
 * border:1px solid #819cb9; <br />
 * }</p>
 * <p/>
 * <p>#top{ <br />
 * height:25px; <br />
 * background-repeat:repeat-x; <br />
 * font-weight:bold; <br />
 * color:#1f4f8f; <br />
 * }</p>
 * <p/>
 * <p>.widget-notValid{ <br />
 * border: 2px solid red; <br />
 * }</p>
 */
public abstract class AbstractDynamicTable extends FlexTable implements Constants {
    /**
     * Width of first column, where buttons are replaced
     * *Minimum height of rows. You can't set smaller height.
     */
    private static final String MIN_HEIGHT = "40px";


    /**
     * Some rows should use constant alignment (Header for example)
     */
    private static final HorizontalAlignmentConstant CENTER_ALIGNMENT = HasHorizontalAlignment.ALIGN_CENTER;
    private static final VerticalAlignmentConstant TOP_ALIGNMENT = HasVerticalAlignment.ALIGN_TOP;
    private static final VerticalAlignmentConstant MIDDLE_ALIGNMENT = HasVerticalAlignment.ALIGN_MIDDLE;

    /**
     * For calculate total logic. We need to format output result in the proper way.
     */
    private static final NumberFormat numberFormat = NumberFormat.getFormat(",##0.00");

    private final List rows;
    /**
     * Rows kept here. We use this list to let client get needed column.
     */
    private final List index;
    /**
     * Index of row. Used for delete mechanism.
     */
    private DynamicTableColumn[] dynamicTableColumn;
    /**
     * Column names stored here.
     */
    private AddListener addListener;
    /**
     * Listener to handle plus clicked event.
     */
    private AddNewListener addNewListener;


    /**
     * Listener to get runtime calculated values.
     */

    private String height;
    /**
     * Client may set rows height. (exclude header row)
     */
    /**
     * Show button panel with plus-minus buttons or not. Client may manipulate with this value.
     */
    private boolean showButtons = true;

    private boolean isAdmin = true;

//    private String buttonsWidth = "40px";

    /**
     * Client may set aligment inside the rows. (exclude header row)
     */
    private HorizontalAlignmentConstant horizontalAlignment = CENTER_ALIGNMENT;
    private VerticalAlignmentConstant verticalAlignment = MIDDLE_ALIGNMENT;

    private Map cellWidgets;

    /**
     * notValid Widget
     */
    private Widget widget;

    private boolean forceWidthInPixel = false;
    private int containerWidth = 0;

    /**
     * @param dynamicTableColumn columns names and width should be here.
     */
    public AbstractDynamicTable(DynamicTableColumn[] dynamicTableColumn) {

        this(dynamicTableColumn, true);
    }


    /**
     * @param dynamicTableColumn columns names and width should be here.
     * @param showButtons        Show button panel with plus-minus buttons or not.
     *                           By default buttons are visible.
     */
    public AbstractDynamicTable(DynamicTableColumn[] dynamicTableColumn, boolean showButtons) {

        super();
        this.showButtons = showButtons;
        this.dynamicTableColumn = dynamicTableColumn;
        rows = new ArrayList();
        index = new ArrayList();
        init(true);
    }

    public AbstractDynamicTable() {
        super();
        this.showButtons = false;
        rows = new ArrayList();
        index = new ArrayList();
        init(false);
    }

    public AbstractDynamicTable(DynamicTableColumn[] dynamicTableColumn, boolean showButtons, boolean isAdmin) {

        super();
        this.showButtons = showButtons;
        this.dynamicTableColumn = dynamicTableColumn;
        this.isAdmin = isAdmin;
        rows = new ArrayList();
        index = new ArrayList();
        init(true);
    }


    /**
     * Creates new row with defined widgets in it.
     * If row index is bigger than table size it throws IndexOutOfBoundsException.
     * If item size is not equal to columns count it throws IllegalArgumentException.
     *
     * @param rowId    index of creating row. Begins from 0.
     * @param objectId id of items. Used to save and edit.
     * @param item     widgets array. Must be equal to columns number.
     */
    public void insertRow(int rowId, Integer objectId, Widget[] item) {

        lengthValidation(item);
        indexBoundaryValidation(rowId);

        rows.add(rowId, new DynamicTableItem(objectId, dynamicTableColumn, item));
        drawRow(rowId, item);
    }


    /**
     * Creates new row with defined widgets in it.
     * If row index is bigger than table size it throws IndexOutOfBoundsException.
     * If item size is not equal to columns count it throws IllegalArgumentException.
     * Object Id of items will be set to null.
     *
     * @param rowId index of creating row. Begins from 0.
     * @param item  widgets array. Must be equal to columns number.
     */
    public void insertRow(int rowId, Widget[] item) {
        insertRow(rowId, null, item);
    }


    /**
     * Add row to the end of the table
     *
     * @param objectId id of items. Used to save and edit.
     * @param item     widgets array. Must be equal to columns number.
     */
    public void addRow(Integer objectId, Widget[] item) {
        int rowId = rows.size();
        insertRow(rowId, objectId, item);
    }


    /**
     * Add row to the end of the table
     * Object Id of items will be set to null.
     *
     * @param item widgets array. Must be equal to columns number.
     */
    public void addRow(Widget[] item) {
        int rowId = rows.size();
        insertRow(rowId, null, item);
    }


    /**
     * Returns WfmtableItem object for specified row.
     * You can get from WfmTableIte all widgets or some concrete cell.
     *
     * @param rowId index of the row. Begins from 0.
     * @return WfmTableItem object.
     * @throws IllegalArgumentException if column with such name doesn't exist.
     */
    public DynamicTableItem getItem(int rowId) throws IllegalArgumentException {

        indexBoundaryValidation(rowId);

        DynamicTableItem dynamicTableItem = (DynamicTableItem) rows.get(rowId);

        if (dynamicTableItem == null) {
            throw new IllegalArgumentException("There is no column with such name.");
        }

        return dynamicTableItem;

    }


    /**
     * Return number of rows exclude tables header.
     *
     * @return number of rows.
     */
    public int getRowNumber() {
        return rows != null ? rows.size() : 0;
    }


    /**
     * Sets the object's rows height. This height does not include decorations such as
     * border, margin, and padding.
     *
     * @param height the object's new height, in pixels.
     * @throws IllegalArgumentException
     */
    public void setHeight(String height) throws IllegalArgumentException {

        //if height size not in pixels throw exception.
        if (!height.contains("px")) {
            throw new IllegalArgumentException("Height should be in pixels.");
        }
        this.height = height;
    }


    /**
     * Horizontal alignment of the table rows.
     *
     * @param horizontalAlignment for example HasHorizontalAlignment.ALIGN_CENTER.
     */
    public void setHorizontalAlignment(HorizontalAlignmentConstant horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }


    /**
     * Vertical alignment of the table rows.
     */
    public void setVerticalAlignment(VerticalAlignmentConstant verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }


    /**
     * You may define your own RowListener implementation.
     * RowListener method plusClicked(int rowId) called each time plus button is clicked.
     *
     * @param addListener
     */
    public void addListener(AddListener addListener) {
        this.addListener = addListener;
    }


    public void addNewListener(AddNewListener addNewListener) {
        this.addNewListener = addNewListener;
    }

    /**
     * Change style of inner widget to .widget-notValid.
     * (red borders)
     *
     * @param rowId
     * @param columnId
     */
    public void notValid(int rowId, String columnId) {
        DynamicTableItem tableItem = getItem(rowId);
        widget = tableItem.getColumnById(columnId);
        widget.addStyleName(ERROR_FORM_STYLE);
        tableItem.notValid(columnId);
    }

    /**
     * Change style of inner widget to .widget-notValid.
     * (red borders)
     * (and title message)
     *
     * @param rowId
     * @param columnId
     * @param titleMessage
     */
    public void notValid(int rowId, String columnId, String titleMessage) {
        notValid(rowId, columnId);
        if (widget != null) {
            widget.setTitle(titleMessage);
        }
    }


    /**
     * Removes .widget-notvalid style from all tables widgets.
     */
    public void resetValidation() {
        for (int rowId = 0; rowId < getRowNumber(); rowId++) {

            DynamicTableItem tableItem = getItem(rowId);
            tableItem.resetValidation();
            Widget[] widget = tableItem.getWidgets();

            for (Widget aWidget : widget) {
                aWidget.removeStyleName(ERROR_FORM_STYLE);
                if (aWidget.getTitle() != null) {
                    aWidget.setTitle("");
                }
            }
        }
    }


    /**
     * Addinf listener to the table.
     * Returns cell validation or info message and it's status.
     *
     * @param dynamicTableListener Listener for handling validation and information messages.
     * @throws NullPointerException if DynamicTableListener is null.
     */
    public void addDynamicTableListener(final DynamicTableListener dynamicTableListener) throws NullPointerException {
        if (dynamicTableListener == null) {
            throw new NullPointerException("Listener can not be null.");
        }
        super.addTableListener((sender, row, cell) -> {

            //If table has add/remove buttons (we shouldn't count them, it's not real columns)
            int columnId = showButtons ? cell - 1 : cell;

            //some unreal values for the table
            if (columnId < 0 || columnId > dynamicTableColumn.length) {
                return;
            }

            //Take cell data
            DynamicTableItem tableItem = getItem(row - 1);
            DynamicTableColumn column = dynamicTableColumn[columnId];

            //If not valid, then return VALIDATION status (4) and validation message.
            if (!tableItem.isValid(columnId)) {
                if (column.hasValidationMessage()) {
                    dynamicTableListener.onCellClicked(column.getValidationMessage(), VALIDATION);
                }
            } else {
                //if cell is valid and has information message then return this message with INFO(0) status.
                if (column.hasInfoMessage()) {
                    dynamicTableListener.onCellClicked(column.getInfoMessage(), INFO);
                }
            }
        });
    }

    public void clear() {
        int rowNumber = this.getRowNumber();

        for (int i = 1; i <= rowNumber; i++) {
            super.removeRow(this.getRowNumber());
            rows.remove(rows.size() - 1);
//            if(index.size() > 0){
//                index.remove(index.size() - 1);
//            }
        }
    }

    /**
     * Method should return concrete header widget.
     * Header must inherit AbstractHeader class.
     *
     * @return header concrete class.
     */
    protected abstract AbstractHeader getHeader();

    /**
     * You should write css style of table borders.
     *
     * @return styleName css style name
     */
    protected abstract String getBorderStyle();


    /**
     * Initialization logic and drawing header.
     */
    private void init(boolean drawHeader) {
        super.setCellSpacing(0);
        super.setCellPadding(0);
        super.setBorderWidth(1);

        String borderStyle = getBorderStyle();
        if (borderStyle != null) {
            super.addStyleName(borderStyle);
        }

        cellWidgets = new HashMap();

        if (drawHeader) {
            drawHeaderRow();
        }
    }


    /**
     * Draws header using overriden AbstractHeader widget.
     * Child class should determinate concrete Header widget.
     *
     * @throws IllegalStateException
     */
    private void drawHeaderRow() throws IllegalStateException {
        AbstractHeader header = getHeader();

        if (header == null) {
            throw new IllegalStateException("You should define concrete Header widget.");
        }

        int column = 0; //column index. Counts from zero. This user defined columns.
        int order = 0;  //column order. Differs from columns size because of buttons panel.
        containerWidth = 0; //this params contains sum of all columns width

        if (showButtons) {
            drawHeaderColumn(column, header, "", null, true);
            order++;
            containerWidth += 40;
        }

        /**
         * Define the column will use as pixel or percentage
         */
        {
            for (int index = 0; index < dynamicTableColumn.length; index++) {
                containerWidth += dynamicTableColumn[index].getColumnWidth() != null ? dynamicTableColumn[index].getColumnWidth() : 0;
            }
            if (containerWidth > MainLayout.get().getMainContent().getElement().getClientWidth()) {
                forceWidthInPixel = true;
            }
        }

        while (column < dynamicTableColumn.length) {
            header = getHeader();

            if (dynamicTableColumn[column].getSortCommand() != null) {
                header.setSortCommand(dynamicTableColumn[column].getSortCommand());
            }
            header.setHeaderText(dynamicTableColumn[column].getColumnName());
            String width = null, style = null;

            if (dynamicTableColumn[column].getColumnWidth() != null) {

                if (!dynamicTableColumn[column].isPixel() && dynamicTableColumn[column].isForceWidthInPercent()) {
                    width = dynamicTableColumn[column].getColumnWidth() + "%";
                } else if (dynamicTableColumn[column].isPixel() || forceWidthInPixel) {
                    width = dynamicTableColumn[column].getColumnWidth() + "px";
                } else {
                    width = (dynamicTableColumn[column].getColumnWidth() * 100 / containerWidth) + "%";
                }
                style = dynamicTableColumn[column].getStyle();
            }
            boolean isVisible = dynamicTableColumn[column].isVisible();
            boolean saveWhiteSpace = dynamicTableColumn[column].isSaveWhiteSpace();
            if (saveWhiteSpace) {
                drawHeaderColumnWithWhiteSpace(order, header, width, style, isVisible, saveWhiteSpace);
            } else {
                drawHeaderColumn(order, header, width, style, isVisible);
            }

            if (dynamicTableColumn[column].getColspan() != null) {
                getFlexCellFormatter().setColSpan(0, order, dynamicTableColumn[column].getColspan());
            }
            column++;
            order++;
        }
    }

    public void drawHeaderRow(DynamicTableColumn[] columns) {
        this.dynamicTableColumn = columns;
        drawHeaderRow();
    }


    /**
     * Draws row. For drawing it calls functions which drawing separate cells.
     *
     * @param rowId
     * @param item  array of widgets that we put in table cells. Number of items should be equal to columns count.
     */
    private void drawRow(int rowId, Widget[] item) {
        rowId = super.insertRow(rowId + 1);

        int column = 0; //column index. Counts from zero. This user defined columns.
        int order = 0;  //column order. Differs from columns size because of buttons panel.
        if (showButtons) {
            addButtonPanel(rowId, column);
            order++;
        }

        while (column < dynamicTableColumn.length) {
            Widget widget = item[column];

            //if field calculatable
            calculatable(column, widget);
            String width;

            if (!dynamicTableColumn[column].isPixel() && dynamicTableColumn[column].isForceWidthInPercent()) {
                width = dynamicTableColumn[column].getColumnWidth() + "%";
            } else if (dynamicTableColumn[column].isPixel() || forceWidthInPixel) {
                width = dynamicTableColumn[column].getColumnWidth() + "px";
            } else {
                width = (dynamicTableColumn[column].getColumnWidth() * 100 / containerWidth) + "%";
            }
            //String width = dynamicTableColumn[column].getColumnWidth() + "px";
            String style = dynamicTableColumn[column].getStyle();
            boolean visible = dynamicTableColumn[column].isVisible();
            boolean saveWhiteSpace = dynamicTableColumn[column].isSaveWhiteSpace();
            if (saveWhiteSpace) {
                drawColumnWithWhiteSpace(rowId, order, style, item[column], width, visible, saveWhiteSpace);
            } else {
                if (item[column] != null) {
                    drawColumn(rowId, order, style, item[column], width, visible);
                }
            }
            column++;
            order++;
        }
    }

    private void drawColumnWithWhiteSpace(int rowId, int columnId, String style, Widget widget, String width, boolean visible, boolean saveWhiteSpace) {
        setColumnWithWhiteSpace(rowId, columnId, widget, width, visible, saveWhiteSpace, style);
    }


    /**
     * If field marked as calculatable,
     * we try to add validation listener.
     *
     * @param column
     * @param widget
     */
    private void calculatable(int column, Widget widget) {
        //marked as calculatable.
        if (dynamicTableColumn[column].isCheckInput()) {
            //we must be sure that widget is TextBox.
            if (widget instanceof TextBox) {
                TextBox textBox = (TextBox) widget;
                //class used to validate and format entered value.
                new InputValidator(textBox);

            }
        }
    }


    /**
     * Draws cell. Client may change height and alignment.
     *
     * @param rowId
     * @param columnId
     * @param widget
     * @param width
     */
    private void drawColumn(int rowId, int columnId, String style, Widget widget, String width, boolean visible) {
//        String height = calculateHeight();
//        setColumnHeight(rowId, columnId, height);
        setColumn(rowId, columnId, widget, width, visible, style);
    }


    /**
     * Draws header cell. Constant alignment, constant height. (Height equals to Header widgets height.)
     *
     * @param columnId
     * @param widget
     */
    private void drawHeaderColumn(int columnId, Widget widget, String width, String style, boolean isVisble) {
        setColumn(0, columnId, widget, width, isVisble, style);
    }


    private void drawHeaderColumnWithWhiteSpace(int columnId, Widget widget, String width, String style, boolean isVisble, boolean saveWhiteSpace) {
        setColumn(0, columnId, widget, width, isVisble, style);
        if (saveWhiteSpace) {
            super.getFlexCellFormatter().getElement(0, columnId).getFirstChildElement().setPropertyString("style", "white-space: pre;");
        }
    }


    /**
     * Draws button panel.
     * Panel uses constant alignment, client may set height.
     *
     * @param rowId
     * @param columnId
     * @param widget
     */
    private void drawCustomColumn(int rowId, int columnId, Widget widget) {
//        String height = calculateHeight();
        setColumnHeight(rowId, columnId, height);
//        setAlignment(rowId, columnId, CENTER_ALIGNMENT, MIDDLE_ALIGNMENT);
        setColumn(rowId, columnId, widget, null, true, "dynamictable__actions");
    }


    /**
     * Deletes pointed row.
     * Function will not delete last row. (If table has only one row).
     *
     * @param rowId
     */
    public void deleteRow(int rowId, boolean forcedDeletion) {

        if (rows.size() > 1 || forcedDeletion) {
            rows.remove(rowId - 1);
            if (index.size() > 0) {
                index.remove(rowId - 1);
            }
            super.removeRow(rowId);
        } else {
            showAlert();
        }
    }

    public void deleteAnyRow(int rowId) {
        if (rows.size() > 0) {
            rows.remove(rowId - 1);
            if (index.size() > 0) {
                index.remove(rowId - 1);
            }
            super.removeRow(rowId);
        } else {
            showAlert();
        }
    }

    public void removeItems() {
        int length = getRowNumber();
        for (int i = length - 1; i >= 0; i--) {
            rows.remove(i);
            super.removeRow(i + 1);
        }
    }


    /**
     * Sets cells height.
     *
     * @param rowId
     * @param columnId
     * @param height
     */
    private void setColumnHeight(int rowId, int columnId, String height) {

        super.getFlexCellFormatter().setHeight(rowId, columnId, height);
    }


    /**
     * Sets cells vertical and horizontal alignment.
     *
     * @param rowId
     * @param columnId
     * @param horizontalAlignment
     * @param verticalAlignment
     */
    public void setAlignment(int rowId,
                             int columnId,
                             HorizontalAlignmentConstant horizontalAlignment,
                             VerticalAlignmentConstant verticalAlignment) {

        super.getFlexCellFormatter().setHorizontalAlignment(rowId, columnId, horizontalAlignment);
        super.getFlexCellFormatter().setVerticalAlignment(rowId, columnId, verticalAlignment);
    }


    /**
     * Inserts widget to mentioned cell.
     *
     * @param rowId
     * @param columnId
     * @param widget
     */
    private void setColumn(int rowId, int columnId, Widget widget, String width, boolean visible, String style) {
        if (width != null && width.contains("%") && !(widget instanceof AbstractHeader)) {
            widget.setWidth("100%");
            super.getFlexCellFormatter().setWidth(rowId, columnId, width);
        } else {
            super.getFlexCellFormatter().setWidth(rowId, columnId, null);
        }
        super.getFlexCellFormatter().setVisible(rowId, columnId, visible);
        super.setWidget(rowId, columnId, widget);
        if (style != null && !style.isEmpty()) {
            super.getFlexCellFormatter().addStyleName(rowId, columnId, style);
        }
        super.getFlexCellFormatter().setVerticalAlignment(rowId, columnId, verticalAlignment);
        cellWidgets.put(getCellID(rowId, columnId), getWidget(rowId, columnId));
    }


    /**
     * Inserts widget to mentioned cell.
     *
     * @param rowId
     * @param columnId
     * @param widget
     * @param saveWhiteSpace
     */
    private void setColumnWithWhiteSpace(int rowId, int columnId, Widget widget, String width, boolean visible, boolean saveWhiteSpace, String style) {
        if (width != null && width.contains("%") && !(widget instanceof AbstractHeader)) {
            widget.setWidth("100%");
            super.getFlexCellFormatter().setWidth(rowId, columnId, width);
        } else {
            super.getFlexCellFormatter().setWidth(rowId, columnId, null);
        }
        super.getFlexCellFormatter().setVisible(rowId, columnId, visible);
        super.setWidget(rowId, columnId, widget);
        if (style != null && !style.isEmpty()) {
            super.getFlexCellFormatter().addStyleName(rowId, columnId, style);
        }
        if (saveWhiteSpace) {
            super.getFlexCellFormatter().getElement(rowId, columnId).getFirstChildElement().getPropertyString("style").concat("white-space: pre;");
        }
        super.getFlexCellFormatter().setVerticalAlignment(rowId, columnId, verticalAlignment);
        cellWidgets.put(getCellID(rowId, columnId), getWidget(rowId, columnId));
    }


    /**
     * Returns table's cell position for writing to the map which named 'cellWidgets'.
     *
     * @param row
     * @param column
     * @return
     */
    private String getCellID(int row, int column) {
        return row + "_" + column;
    }


    /**
     * Button logic is placed here.
     * To draw button panel we call corresponding function.
     *
     * @param rowId
     */
    private void addButtonPanel(int rowId, int column) {

        final HorizontalPanel buttonPanel = new HorizontalPanel();
        buttonPanel.setSpacing(1);
        index.add(rowId - 1, buttonPanel);

        SvgIcon addIcon = new SvgIcon(SvgEnum.plus);
        /*addIcon.addClickHandler(sender -> {
            int id = index.indexOf(exportPanel);
            plusClickListener(id);
        });*/
        MaterialLink addRow = new MaterialLink();
        addRow.setClass("dynamictable__add--row dynamictable__action-item btn--icon");
        addRow.add(addIcon);
        addRow.addClickHandler(ch -> {
            int id = index.indexOf(buttonPanel);
            plusClickListener(id);
        });
        buttonPanel.add(addRow);

        SvgIcon removeIcon = new SvgIcon(SvgEnum.trash2);
        /*removeIcon.addClickHandler(sender -> {
            int id = index.indexOf(exportPanel);
            if (addNewListener != null) {
                minusNewClickListener(id);
            } else {
                minusClickListener(id);
            }
        });*/
        MaterialLink removeRow = new MaterialLink();
        removeRow.setClass("dynamictable__remove--row dynamictable__action-item btn--icon");
        removeRow.add(removeIcon);
        removeRow.addClickHandler(ch -> {
            int id = index.indexOf(buttonPanel);

            if (addNewListener != null) {
                minusNewClickListener(id);
            } else {
                minusClickListener(id);
            }
        });
        if (isAdmin) {
            buttonPanel.add(removeRow);
        }

        drawCustomColumn(rowId, column, buttonPanel);
    }


    /**
     * Plus button is clicked.
     * If client have setted RowListener we call plusClicked() function.
     * Otherwise simply do nothing.
     *
     * @param rowId index of row exclude header.
     */
    private void plusClickListener(int rowId) {

        if (addListener != null) {
            addListener.plusClicked(rowId);
        } else if (addNewListener != null) {
            addNewListener.plusClicked(rowId);
        }
    }


    /**
     * Minus button is clicked. We call deleting row function.
     *
     * @param rowId index of row.
     */
    public void minusClickListener(int rowId) {

        DynamicTableItem tableItem = getItem(rowId);
        Integer objectId = tableItem.getObjectId();
        deleteRow(rowId + 1, false);
        if (addListener != null) {
            addListener.minusClicked(rowId, objectId);
        }
    }


    public void minusNewClickListener(int rowId) {
        DynamicTableItem tableItem = getItem(rowId);
        Integer objectId = tableItem.getObjectId();
        if (addNewListener != null) {
            addNewListener.minusClicked(rowId, objectId);
        }

    }

    /**
     * Used to calculate height.
     * If client haven't set any height we use MIN_HEIGHT constant.
     * Otherwise we check if clients height is bigger than our rows MIN_HEIGHT.
     *
     * @return
     */
    private String calculateHeight() {

        if (height != null) {
//            int intHeight = Integer.valueOf(height.substring(0, height.indexOf("px"))).intValue();
//            int intMIN_HEIGHT = Integer.valueOf(MIN_HEIGHT.substring(0, MIN_HEIGHT.indexOf("px"))).intValue();

            return height;// = intHeight < intMIN_HEIGHT ? MIN_HEIGHT : height;
        }

        return MIN_HEIGHT;
    }


    /**
     * Number of widgets you inserted to table shouldn't be bigger than columns number.
     *
     * @param item your widgets array.
     * @throws IllegalArgumentException
     */
    private void lengthValidation(Widget[] item) throws IllegalArgumentException {

        if (item.length != dynamicTableColumn.length) {
            throw new IllegalArgumentException("Inserted widget count differs from table column count.");
        }
    }


    /**
     * Is there row with such index?
     *
     * @param rowId
     * @throws IndexOutOfBoundsException
     */
    private void indexBoundaryValidation(int rowId) throws IndexOutOfBoundsException {

        if (rowId > rows.size() || rowId < 0) {
            throw new IndexOutOfBoundsException("Impossible value for table row. Min row index = 0, max index = " + rows.size());
        }
    }


    /**
     * Shows alert if user tries to delete last table row.
     */
    private void showAlert() {
        Info.show(WfmStrings.App.get().youMustHaveAtLeast1LineItem() + ".", Info.Type.WARNING);
    }

    /**
     * @return calculation result
     */
    public double calculateTotal(String columnId) {

        double total = 0d;
        for (int i = 0; i < getRowNumber(); i++) {
            DynamicTableItem tableItem = getItem(i);
            HasText label = (HasText) tableItem.getColumnById(columnId);
            double value = numberFormat.parse(label.getText());
            total += value;
        }
        return total;
    }

    public void setShowButtons(boolean showButtons) {
        this.showButtons = showButtons;
    }

    public void deleteRow(int row) {
        deleteRow(row + 1, false);
    }

    public void setColspan(int row, int column, int colspan) {
        super.getFlexCellFormatter().setColSpan(row, column, colspan);
    }

    public FlexTable.FlexCellFormatter getFlexCellFormatter() {
        return super.getFlexCellFormatter();
    }
}
