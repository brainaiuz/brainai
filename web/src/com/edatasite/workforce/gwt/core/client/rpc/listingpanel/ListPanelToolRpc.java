package com.edatasite.workforce.gwt.core.client.rpc.listingpanel;

import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ColumnTool;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelGuideSettingsRPC;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;


/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 18-Aug-2010
 * Time: 14:41:51
 */
public class ListPanelToolRpc implements IsSerializable {

    private boolean applySettingsToAll;
    private ArrayList<String> columnCodeName;
    private LinkedHashMap<String, ColumnTool> colunmsTool;
    private ArrayList<CompanyCustomFieldItem> listViewCustomFields;
    private int pageSize = 20;
    private String sortBy;//Returns the name of the column.
    private Integer typeId;
    private ListPanelType type;
    private String formID;
    private boolean showPopup;
    private String sortByType;
    private Integer stepID;
    private String viewstate;
    private ListPanelGuideSettingsRPC guideSettings;

    public boolean isShowPopup() {
        return showPopup;
    }

    public void setShowPopup(boolean showPopup) {
        this.showPopup = showPopup;
    }

    public boolean isAppliedSettingsToAll() {
        return applySettingsToAll;
    }

    public void setApplySettingsToAll(boolean applySettingsToAll) {
        this.applySettingsToAll = applySettingsToAll;
    }

    public ArrayList<String> getColumnCodeName() {
        return columnCodeName;
    }

    public void setColumnCodeName(ArrayList<String> columnCodeName) {
        this.columnCodeName = columnCodeName;
    }

    public LinkedHashMap<String, ColumnTool> getColunmsTool() {
        return colunmsTool;
    }

    public void setColunmsTool(LinkedHashMap<String, ColumnTool> colunmsTool) {
        this.colunmsTool = colunmsTool;
    }

    public ArrayList<CompanyCustomFieldItem> getListViewCustomFields() {

        if (listViewCustomFields == null) {
            listViewCustomFields = new ArrayList<>();
        }
        return listViewCustomFields;
    }

    public void setListViewCustomFields(ArrayList<CompanyCustomFieldItem> listViewCustomFields) {
        this.listViewCustomFields = listViewCustomFields;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public ListPanelType getType() {
        if (type != null) {
            type.setId(typeId);
        }
        return type;
    }

    public void setType(ListPanelType type) {
        this.type = type;
    }

    public void removeColumnTools(String columnCode) {
        colunmsTool.remove(columnCode);
        columnCodeName.remove(columnCode);
    }

    public void addColumnTool(String columnCodeName, ColumnTool columnTool) {
        if (!getColumnCodeName().contains(columnCodeName)) {
            getColumnCodeName().add(columnCodeName);
            getColunmsTool().putIfAbsent(columnCodeName, columnTool);
        }
    }

    public boolean isCustomFieldsShown() {
        if (columnCodeName != null && columnCodeName.size() > 0) {
            return columnCodeName.stream()
                    .anyMatch(
                            s -> (s.startsWith("string_value") || s.startsWith("date_value") || s.startsWith("double_value"))
                    );
        }
        return false;
    }

    public void clearColors() {
        if (colunmsTool != null && colunmsTool.size() > 0) {
            for (Map.Entry<String, ColumnTool> entry : colunmsTool.entrySet()) {
                if (entry.getValue().hasColor()) {
                    entry.getValue().clearColors();
                }
            }
        }
    }

    public boolean isApplySettingsToAll() {
        return applySettingsToAll;
    }

    public static ListPanelToolRpc createIntance() {
        return new ListPanelToolRpc();
    }

    public String getSortByType() {
        return sortByType;
    }

    public void setSortByType(String sortByType) {
        this.sortByType = sortByType;
    }

    public Integer getStepID() {
        return stepID;
    }

    public void setStepID(Integer stepID) {
        this.stepID = stepID;
    }

    public String getViewstate() {
        return viewstate;
    }

    public void setViewstate(String viewstate) {
        this.viewstate = viewstate;
    }

    public ListPanelGuideSettingsRPC getGuideSettings() {
        return guideSettings;
    }

    public void setGuideSettings(ListPanelGuideSettingsRPC guideSettings) {
        this.guideSettings = guideSettings;
    }

    public String getFormID() {
        return this.formID;
    }

    public void setFormID(final String formID) {
        this.formID = formID;
    }
}
