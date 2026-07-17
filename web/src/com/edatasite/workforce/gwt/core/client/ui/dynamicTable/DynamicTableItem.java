package com.edatasite.workforce.gwt.core.client.ui.dynamicTable;

import com.edatasite.workforce.gwt.core.client.ui.LinkedMap;
import com.google.gwt.user.client.ui.Widget;

import java.util.HashMap;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 23.10.2008
 * Time: 14:46:46
 * To change this template use File | Settings | File Templates.
 */
public class DynamicTableItem {

    private Integer objectId;
    private Integer entityID;
    private Widget[] widgets;
    private DynamicTableColumn[] dynamicTableColumn;
    private LinkedMap columns;
    private HashMap valid;


    public DynamicTableItem(Integer objectId, DynamicTableColumn[] dynamicTableColumn, Widget[] item) {

        this.objectId = objectId;
        this.widgets = item;
        this.dynamicTableColumn = dynamicTableColumn;
        init();
    }


    /**
     * Returns widget by column name in which widget was replaced.
     *
     * @param columnName
     * @return widget
     */
    public Widget getColumnById(String columnId) {

        return (Widget) columns.get(columnId);
    }


    /**
     * Returns objectId of item
     *
     * @return objectID
     */
    public Integer getObjectId() {
        return objectId;
    }


    /**
     * Sets items objectId.
     *
     * @return objectID
     */
    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }


    /**
     * Returns all widgets which were replaced in this row.
     *
     * @return widgets
     */
    public Widget[] getWidgets() {
        return widgets;
    }


    /**
     * Returns number of column with such Id.
     *
     * @param columnId
     * @return
     */
    public int getColumnNumber(String columnId) {
        return columns.indexOf(columnId);
    }


    /**
     * Checks current column valid or not.
     *
     * @param columnId
     * @return
     */
    public boolean isValid(String columnId) {

        return (Boolean) valid.get(columnId);
    }


    /**
     * Checks current column valid or not.
     *
     * @param cell
     */
    public boolean isValid(int cell) {

        Object key = columns.getKeyByIndex(cell);
        return (Boolean) valid.get(key);
    }


    /**
     * Sets columns status to not valid.
     *
     * @param columnId
     */
    public void notValid(String columnId) {
        valid.put(columnId, Boolean.FALSE);
    }


    /**
     * Resets all validation statuses.
     * All columns are valid now.
     */
    public void resetValidation() {

        for (Object o : valid.keySet()) {
            valid.put(o, Boolean.TRUE);
        }
    }


    /**
     * Initialization logic.
     * Widgets array kept in special list from which we can take definite item by column name.
     *
     * @param item
     */
    private void init() {

        columns = new LinkedMap();
        valid = new HashMap();
        for (int i = 0; i < widgets.length; i++) {
            String key = dynamicTableColumn[i].getColunmId();
            //we can't add identical key's to map, so - throw exception
            if (columns.contains(key)) {
                throw new IllegalArgumentException("You may not define columns with identical id. Column's Id = " + key);
            }
            columns.add(key, widgets[i]);
            valid.put(key, Boolean.TRUE);
        }
    }


    public LinkedMap getColumns() {
        return columns;
    }
    public Integer getColumnWidth(String columnID) {
        return dynamicTableColumn[columns.indexOf(columnID)].getColumnWidth();
    }

    public Integer getEntityID() {
        return entityID;
    }

    public void setEntityID(Integer entityID) {
        this.entityID = entityID;
    }
}
