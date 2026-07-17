package com.edatasite.workforce.gwt.importfile.client.rpc;

import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * User: Faxriddin Taslimov Date: Aug 17, 2010
 */
public class ImportFile implements IsSerializable {
    public static final String DELIMITR_BETWEEN_REPRESENTATION_ID = "--:--";
    private static final String DELIMITR_BETWEEN_COLUMNS = "--;--";
    private static final String DELIMITR_BETWEEN_EXTRA_REPRESENTATION_ID_2 = "--#--";
    public static final String SKIP = "SKIP";
    public static final String MERGE = "MERGE";
    public static final String CLONE = "CLONE";

    private Integer objectID;
    private Integer fileID;
    private Date conversionDate;
    private HashMap<Integer, Integer> columns;
    private HashMap<Integer, String> extraColumns;
    private String categoryColumns;
    private char defaultSeparator;
    private boolean hasHeader = true;
    private Integer userID;
    private Integer csvColumns;
    private Integer importedColumns;
    private String duplicateAction = SKIP;

    private Integer paymentID;
    private Integer budgetID;
    private String nextSteps;
    private String viewType;
    private ImportTypeEnum type;
    private Integer mailingListId;
    private Integer newColumns;
    private Integer overwrittenColumns;
    private Integer skippedColumns;
    private Integer ignoredColumns;
    private Integer clonedColumns;
    private SelectItem[] dynamicColumns;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getFileID() {
        return fileID;
    }

    public void setFileID(Integer fileID) {
        this.fileID = fileID;
    }

    public HashMap<Integer, Integer> getColumns() {
        return columns;
    }

    public void setColumns(HashMap<Integer, Integer> columns) {
        this.columns = columns;
    }

    public void setColumns(String columns) {
        if (columns != null && !"".equals(columns)) {
            for (String column : columns.split(DELIMITR_BETWEEN_COLUMNS)) {
                if (this.columns == null) {
                    this.columns = new HashMap<>();
                }
                if (column != null && !"".equals(column)) {
                    this.columns.put(Integer.parseInt(column.split(DELIMITR_BETWEEN_REPRESENTATION_ID)[0]), Integer.parseInt(column.split(DELIMITR_BETWEEN_REPRESENTATION_ID)[1]));
                }
            }
        }
    }

    public HashMap<Integer, String> getExtraColumns() {
        return extraColumns;
    }

    public HashMap<Integer, ArrayList<Integer>> getExtraColumnsAsMap(Integer column) {
        HashMap<Integer, ArrayList<Integer>> items = null;
        if (extraColumns != null && extraColumns.containsKey(column)) {
            String str;
            str = extraColumns.get(column);
            if (str != null && !"".equals(str)) {
                items = new HashMap<>();
                String[] integers = str.split(DELIMITR_BETWEEN_EXTRA_REPRESENTATION_ID_2);
                ArrayList<Integer> columns;
                for (String integer1 : integers) {
                    if (integer1 != null && !"".equals(integer1)) {
                        Integer key = null;
                        columns = new ArrayList<>();
                        for (String integer : integer1.split(DELIMITR_BETWEEN_REPRESENTATION_ID)) {
                            if (integer != null && !"".equals(integer)) {
                                if (key != null) {
                                    columns.add(Integer.parseInt(integer));
                                } else {
                                    key = Integer.parseInt(integer);
                                }
                            } else {
                                columns.add(-1);
                            }
                        }
                        items.put(key, columns);
                    }
                }
            }
        }
        return items;
    }

    public HashMap<Integer, HashMap<String, ArrayList<Integer>>> getExtraColumnsAsMapList(Integer column) {
        HashMap<Integer, HashMap<String, ArrayList<Integer>>> items = null;
        if (extraColumns != null && extraColumns.containsKey(column)) {
            String str;
            str = extraColumns.get(column);
            if (str != null && !"".equals(str)) {
                items = new HashMap<>();
                String[] integers = str.split(DELIMITR_BETWEEN_EXTRA_REPRESENTATION_ID_2);
                ArrayList<Integer> columns;
                for (String integer1 : integers) {
                    if (integer1 != null && !"".equals(integer1)) {
                        Integer key = null;
                        columns = new ArrayList<>();
                        for (String integer : integer1.split(DELIMITR_BETWEEN_REPRESENTATION_ID)) {
                            if (integer != null && !"".equals(integer)) {
                                if (key != null) {
                                    columns.add(Integer.parseInt(integer));
                                } else {
                                    key = Integer.parseInt(integer);
                                }
                            } else {
                                columns.add(-1);
                            }
                        }
                        if (items.containsKey(key)) {
                            items.get(key).put(integer1, columns);
                        } else {
                            HashMap<String, ArrayList<Integer>> map = new HashMap<>();
                            map.put(integer1, columns);
                            items.put(key, map);
                        }
                    }
                }
            }
        }
        return items;
    }

    public void setExtraColumns(String extraColumns) {
        if (extraColumns != null && !"".equals(extraColumns)) {
            for (String column : extraColumns.split(DELIMITR_BETWEEN_COLUMNS)) {
                if (this.extraColumns == null) {
                    this.extraColumns = new HashMap<>();
                }
                if (column != null && !"".equals(column)) {
                    Integer columnRepresentation = Integer.parseInt(column.split(DELIMITR_BETWEEN_REPRESENTATION_ID)[0]);
                    StringBuilder value = new StringBuilder();
                    boolean escapeFirst = false;
                    for (String val : column.split(DELIMITR_BETWEEN_REPRESENTATION_ID)) {
                        if (escapeFirst) {
                            value.append(value.toString().equals("") ? "" : DELIMITR_BETWEEN_REPRESENTATION_ID).append(val);
                        }
                        escapeFirst = true;
                    }
                    this.extraColumns.put(columnRepresentation, value.toString());
                }
            }
        }
    }


    public void addColumn(Integer columnRepresentation, Integer columnID) {
        if (columns == null) {
            columns = new HashMap<>();
        }
        if (columnRepresentation != null && columnID != null) {
            columns.put(columnRepresentation, columnID);
        }
    }

    public void addExtraColumn(boolean forAddress, Integer columnRepresentation, Integer columnID, String... values) {
        if (extraColumns == null) {
            extraColumns = new HashMap<>();
        }
        if (columnRepresentation != null) {
            StringBuilder mapValue = new StringBuilder("" + (columnID == null ? "" : columnID.toString()));
            if (values != null && values.length > 0) {
                for (String value : values) {
                    if (forAddress || (!"".equals(value) && value != null)) {
                        mapValue.append(DELIMITR_BETWEEN_REPRESENTATION_ID).append(forAddress && (value == null || "".equals(value)) ? "-1" : value);
                    }
                }
            }
            if (extraColumns.containsKey(columnRepresentation)) {
                extraColumns.put(columnRepresentation, extraColumns.get(columnRepresentation) + DELIMITR_BETWEEN_EXTRA_REPRESENTATION_ID_2 + mapValue);
            } else {
                extraColumns.put(columnRepresentation, mapValue.toString());
            }
        }
    }

    public Integer getColumnID(Integer fieldRepresentation) {
        if (columns != null) {
            return columns.get(fieldRepresentation) != null ? columns.get(fieldRepresentation) : -1;
        }
        return -1;
    }

    public Integer getExtraColumnID(String extraColumn) {
        String id = extraColumn.split(DELIMITR_BETWEEN_REPRESENTATION_ID).length > 0 ? extraColumn.split(DELIMITR_BETWEEN_REPRESENTATION_ID)[0] : null;
        if (id != null && !"".equals(id) && id.matches(Constants.REGEX_INTEGER)) {
            return Integer.parseInt(id);
        }
        return -1;
    }

    public String[] getExtraColumnValues(String extraColumn) {
        return extraColumn.split(DELIMITR_BETWEEN_REPRESENTATION_ID);
    }

    public char getDefaultSeparator() {
        return defaultSeparator;
    }

    public void setDefaultSeparator(char defaultSeparator) {
        this.defaultSeparator = defaultSeparator;
    }

    public boolean isHasHeader() {
        return hasHeader;
    }

    public void setHasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
    }

    public Integer getUserID() {
        return userID;
    }

    public void setUserID(Integer userID) {
        this.userID = userID;
    }

    public Integer getCsvColumns() {
        return csvColumns;
    }

    public void setCsvColumns(Integer csvColumns) {
        this.csvColumns = csvColumns;
    }

    public Integer getImportedColumns() {
        return importedColumns != null ? importedColumns : 0;
    }

    public void setImportedColumns(Integer importedColumns) {
        this.importedColumns = importedColumns;
    }

    public String getDuplicateAction() {
        return duplicateAction;
    }

    public void setDuplicateAction(String duplicateAction) {
        this.duplicateAction = duplicateAction;
    }

    public boolean isDuplicateAction(String duplicateAction) {
        return this.duplicateAction.equals(duplicateAction);
    }

    public Integer getPaymentID() {
        return paymentID;
    }

    public void setPaymentID(Integer paymentID) {
        this.paymentID = paymentID;
    }

    public String getNextSteps() {
        return nextSteps;
    }

    public void setNextSteps(String nextSteps) {
        this.nextSteps = nextSteps;
    }

    public ImportTypeEnum getType() {
        return type;
    }

    public void setType(ImportTypeEnum type) {
        this.type = type;
    }

    public String getViewType() {
        return viewType;
    }

    public void setViewType(String viewType) {
        this.viewType = viewType;
    }

    public Integer getMailingListId() {
        return mailingListId;
    }

    public void setMailingListId(Integer mailingListId) {
        this.mailingListId = mailingListId;
    }

    public String getCategoryColumns() {
        return categoryColumns;
    }

    public void setCategoryColumns(String categoryColumns) {
        this.categoryColumns = categoryColumns;
    }

    public Integer getNewColumns() {
        return newColumns != null ? newColumns : 0;
    }

    public void setNewColumns(Integer newColumns) {
        this.newColumns = newColumns;
    }

    public Integer getOverwrittenColumns() {
        return overwrittenColumns != null ? overwrittenColumns : 0;
    }

    public void setOverwrittenColumns(Integer overwrittenColumns) {
        this.overwrittenColumns = overwrittenColumns;
    }

    public Integer getSkippedColumns() {
        return skippedColumns != null ? skippedColumns : 0;
    }

    public void setSkippedColumns(Integer skippedColumns) {
        this.skippedColumns = skippedColumns;
    }

    public Integer getIgnoredColumns() {
        return ignoredColumns != null ? ignoredColumns : 0;
    }

    public void setIgnoredColumns(Integer ignoredColumns) {
        this.ignoredColumns = ignoredColumns;
    }

    public Integer getClonedColumns() {
        return clonedColumns != null ? clonedColumns : 0;
    }

    public void setClonedColumns(Integer clonedColumns) {
        this.clonedColumns = clonedColumns;
    }

    public String getColumnsAsString() {
        StringBuilder columns_ = new StringBuilder();
        String delimitr = "";
        if (columns != null) {
            for (HashMap.Entry<Integer, Integer> entry : columns.entrySet()) {
                columns_.append(delimitr).append(entry.getKey().toString()).append(DELIMITR_BETWEEN_REPRESENTATION_ID).append(entry.getValue().toString());
                delimitr = DELIMITR_BETWEEN_COLUMNS;
            }
        }
        return columns_.toString().equals("") ? null : columns_.toString();
    }

    public String getExtraColumnsAsString() {
        StringBuilder extraColumns_ = new StringBuilder();
        String delimitr = "";
        if (extraColumns != null) {
            for (HashMap.Entry<Integer, String> entry : extraColumns.entrySet()) {
                if (entry.getValue() != null && !"".equals(entry.getValue())) {
                    extraColumns_.append(delimitr).append(entry.getKey().toString()).append(DELIMITR_BETWEEN_REPRESENTATION_ID).append(entry.getValue());
                    delimitr = DELIMITR_BETWEEN_COLUMNS;
                }
            }
        }
        return extraColumns_.toString().equals("") ? null : extraColumns_.toString();
    }

    public String getAsString(Integer id) {
        if (id == null) {
            return "";
        }
        return id.toString();
    }

    public boolean isSkip() {
        return SKIP.equals(duplicateAction);
    }

    public boolean isMerge() {
        return MERGE.equals(duplicateAction);
    }

    public boolean isClone() {
        return CLONE.equals(duplicateAction);
    }

    public String toJson(HashMap<Integer, Integer> map) {
        StringBuilder json = new StringBuilder("{");
        if (map != null && !map.isEmpty()) {
            int size = map.keySet().size();
            for (Integer key : map.keySet()) {
                json.append("\"").append(key).append("\":").append(map.get(key));
                size--;
                if (size != 0) {
                    json.append(",");
                }

            }
        }
        json.append("}");
        if ("{}".equals(json.toString())) {
            return null;
        }
        return json.toString();
    }

    public void setDynamicColumns(SelectItem[] dynamicColumns) {
        this.dynamicColumns = dynamicColumns;
    }

    public SelectItem[] getDynamicColumns() {
        return dynamicColumns;
    }

    public Date getConversionDate() {
        return this.conversionDate;
    }

    public void setConversionDate(final Date conversionDate) {
        this.conversionDate = conversionDate;
    }

    public Integer getBudgetID() {
        return this.budgetID;
    }

    public void setBudgetID(final Integer budgetID) {
        this.budgetID = budgetID;
    }
}
