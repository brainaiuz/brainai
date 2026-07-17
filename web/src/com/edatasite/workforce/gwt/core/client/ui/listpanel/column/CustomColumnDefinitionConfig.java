package com.edatasite.workforce.gwt.core.client.ui.listpanel.column;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ColumnColor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.DropDownCellEditor;
import com.google.gwt.gen2.table.client.AbstractColumnDefinition;
import com.google.gwt.gen2.table.client.CellEditor;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: Sep 20, 2010
 * Time: 5:54:20 PM
 * To change this template use File | Settings | File Templates.
 */

/**
 * Current class generalizes table column. It defines row and
 * column related  types. In  some cases we needed  to change
 * column name when some actions have been fired and for this
 * case we need own type to change it as we want. For further
 * information please refer to Ruslan Muhammadov or
 * see com.edatasite.workforce.gwt.messagecenter.client.view.MessageCenterContentView.
 *
 * @param <T> the type of the row value
 * @param <E> the data type of the column
 * @param <C> the type of the column value
 */
public abstract class CustomColumnDefinitionConfig<T, E, C> extends AbstractColumnDefinition<T, E> {

    private final String codeName;
    private String linkUrl;
    private final C columnName;
    private String footerName;
    private boolean isHtml = false;
    private boolean isShow = true;
    private CellChange cellChangesSave;
    private LinkedHashMap<String, String> styleAttributeMap;
    private HasHorizontalAlignment.HorizontalAlignmentConstant horizontalAlign;
    private boolean hasEditor = true;
    private boolean showPopup = false;
    private boolean isClickable;
    private boolean hasLink = false;
    private DataType dataType = DataType.String;
    private final HashMap<String, ColumnColor> colors = new HashMap<>();
    private String referenceCode;

    public CustomColumnDefinitionConfig(C columnName, String codeName, int width) {
        this.columnName = columnName;
        this.codeName = codeName;
        setPreferredColumnWidth(width);
        setHeader(0, columnName);
        setHeaderCount(1);
        setColumnSortable(true);
    }

    public CustomColumnDefinitionConfig(C columnName, String codeName, int width, DataType dataType) {
        this(columnName, codeName, width);
        this.dataType = dataType;
    }

    public CustomColumnDefinitionConfig(C columnName, String codeName, int width, DataType dataType, String referenceCode) {
        this(columnName, codeName, width);
        this.dataType = dataType;
        this.referenceCode = referenceCode;
    }

    public CustomColumnDefinitionConfig(C columnName, String codeName, int width, boolean hasLink) {
        this.columnName = columnName;
        this.codeName = codeName;
        this.hasLink = hasLink;
        setPreferredColumnWidth(width);
        setHeader(0, columnName);
        setHeaderCount(1);
        setColumnSortable(true);
    }

    public void addColor(ColumnColor color) {
        colors.put(color.getCondition(), color);
    }

    public HashMap<String, ColumnColor> getColors() {
        return colors;
    }

    public String getCodeName() {
        return codeName;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public C getColumnName() {
        return columnName;
    }

    public boolean isShow() {
        return isShow;
    }

    public void setShow(boolean show) {
        isShow = show;
    }

    public boolean isHtml() {
        return isHtml;
    }

    public void setHtml(boolean html) {
        isHtml = html;
    }

    public void setCellChangesSave(CellChange cellChangesSave) {
        this.cellChangesSave = cellChangesSave;
    }

    public HasHorizontalAlignment.HorizontalAlignmentConstant getHorizontalAlign() {
        return horizontalAlign;
    }

    public void setHorizontalAlignment(HasHorizontalAlignment.HorizontalAlignmentConstant horizontalAlign) {
        this.horizontalAlign = horizontalAlign;
    }

    public LinkedHashMap<String, String> getStyleAttributeMap() {
        if (styleAttributeMap == null) {
            styleAttributeMap = new LinkedHashMap<>();
        }
        return styleAttributeMap;
    }

    public void addStyleAttribute(String atribute, String value) {
        getStyleAttributeMap().put(atribute, value);
    }

    public void saveCellValue(T rowValue) {
        if (cellChangesSave != null) {
            cellChangesSave.saveCell(rowValue, getCodeName());
        }
    }

    public void setLinkValue(String linkUrl) {
        if (linkUrl != null) {
            this.linkUrl = linkUrl;
        }
    }

    @Override
    public void setCellValue(T rowValue, E cellValue) {
        hasEditor = false;
    }

    public boolean hasEditor() {
        return hasEditor;
    }

    public static LinkedHashMap<String, CustomColumnDefinitionConfig> getEditableColumns(List<CustomColumnDefinitionConfig> columns) {
        LinkedHashMap<String, CustomColumnDefinitionConfig> map = new LinkedHashMap<>();
        if (columns != null && columns.size() > 0) {
            for (CustomColumnDefinitionConfig column : columns) {
                if (column.getCodeName() != null && column.hasEditor()) {
                    map.put(column.getCodeName(), column);
                }
            }
        }
        return map;
    }

    public void setItems(SelectItem[] selectItems) {
        CellEditor<E> cellEditor = getCellEditor();
        if (cellEditor instanceof DropDownCellEditor) {
            ((DropDownCellEditor) cellEditor).setItems(selectItems);
        }
    }

    public boolean isShowPopup() {
        return showPopup;
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public void setShowPopup(boolean showPopup) {
        this.showPopup = showPopup;
    }

    public String getFooterName() {
        return footerName;
    }

    public void setFooterName(String footerName) {
        this.footerName = footerName;
    }


    public boolean isHasLink() {
        return hasLink;
    }

    public void setHasLink(boolean hasLink) {
        this.hasLink = hasLink;
    }

    public boolean isClickable() {
        return isClickable;
    }

    public void setIsClickable(boolean isClickable) {
        this.isClickable = isClickable;
    }

    public DataType getDataType() {
        return dataType;
    }

    public enum DataType {
        BigDecimal,
        Integer,
        String,
        Widget
    }

}
