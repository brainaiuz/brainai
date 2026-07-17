package com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data;

import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Mar 23, 2011
 * Time: 2:49:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class CustomisedITextTable implements PDFConstants {

    private String name;
    private String nameUz;
    private List<String> columnOrder = new LinkedList<>();
    private Map<String, String> header = new LinkedHashMap<>();
    private LinkedHashMap<String, HashMap<String, String>> rows = new LinkedHashMap<>();
    private LinkedHashMap<String, LinkedHashMap<String, HashMap<String, String>>> childRows = new LinkedHashMap<>();
    private LinkedHashMap<String, ITextTreeObject> treeRows = new LinkedHashMap<>();
    private LinkedHashMap<String, LinkedList<String>> totalRows = new LinkedHashMap<>();
    private Map<String, LinkedHashMap<String, Map<String, String>>> customFields;
    private CustomisedHierarchyProductsTable hierarchyTable;
    private List<Map<String, List<CellData>>> rowsList;


    public CustomisedITextTable() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getNameUz() {
        return nameUz;
    }

    public void setNameUz(String nameUz) {
        this.nameUz = nameUz;
    }

    public Map<String, String> getHeader() {
        return header;
    }

    public void setHeader(Map<String, String> header) {
        this.header = header;
    }

    public LinkedHashMap<String, HashMap<String, String>> getRows() {
        if (rows == null) {
            return new LinkedHashMap<>();
        }
        return rows;
    }

    public List<String> getColumnOrder() {
        return columnOrder;
    }

    public void setColumnOrder(List<String> columnOrder) {
        this.columnOrder = columnOrder;
    }

    public LinkedHashMap<String, LinkedList<String>> getTotalRows() {
        if (totalRows == null) {
            return new LinkedHashMap<>();
        }
        return totalRows;
    }

    public void setTotalRows(LinkedHashMap<String, LinkedList<String>> totalRows) {
        this.totalRows = totalRows;
    }

    public CustomisedHierarchyProductsTable getHierarchyTable() {
        if (hierarchyTable == null) {
            hierarchyTable = new CustomisedHierarchyProductsTable();
        }
        return hierarchyTable;
    }

    public void setHierarchyTable(CustomisedHierarchyProductsTable hierarchy) {
        this.hierarchyTable = hierarchy;
    }

    public void setRows(LinkedHashMap<String, HashMap<String, String>> rows) {
        this.rows = rows;
    }

    public Map<String, LinkedHashMap<String, Map<String, String>>> getCustomFields() {
        return customFields;
    }

    public void setCustomFields(Map<String, LinkedHashMap<String, Map<String, String>>> customFields) {
        this.customFields = customFields;
    }

    public void addColumnOrder(String... columnCodes) {
        columnOrder.addAll(Arrays.asList(columnCodes));
    }

    public void addColumnOrder(String columnCode) {
        columnOrder.add(columnCode);
    }

    public void addColumn(String code, String label) {
        columnOrder.add(code);
        header.put(code, label);
    }

    public boolean containsColumn(String code) {
        return columnOrder.contains(code);
    }

    public void addHeaderColumns(String... columnLabels) {
        for (int i = 0; i < columnLabels.length; i++) {
            header.put(columnOrder.get(i), columnLabels[i]);
        }
    }

    public void addRow(String... values) {
        LinkedHashMap<String, String> row = new LinkedHashMap<>();
        for (int i = 0; i < values.length; i++) {
            row.put(columnOrder.get(i), values[i]);
        }
        rows.put(String.valueOf(rows.size()), row);
    }

    public void addRowWithCode(String code, String... values) {
        LinkedHashMap<String, String> row = new LinkedHashMap<>();
        for (int i = 0; i < values.length; i++) {
            row.put(columnOrder.get(i), values[i]);
        }
        rows.put(code, row);
    }

    public void addRowWithOrder(String code, String... values) {
        LinkedHashMap<String, String> row = new LinkedHashMap<>();
        for (int i = 0; i < values.length; i++) {
            row.put(i + "", values[i]);
        }
        rows.put(code, row);
    }

    public ITextTreeObject addTreeRows(String code, ITextTreeObject parent, String... values) {
        ITextTreeObject treeObject = new ITextTreeObject();
        treeObject.setKey(code);
        LinkedHashMap<String, String> value = new LinkedHashMap<>();
        for (int i = 0; i < values.length; i++) {
            value.put(i + "", values[i]);
        }
        treeObject.setValue(value);

        if (parent != null) {
            LinkedList<ITextTreeObject> childs = parent.getChilds();
            childs.add(treeObject);
            parent.setChilds(childs);
        } else {
            treeRows.put(code, treeObject);
        }
        return treeObject;
    }

    public void addChildRows(LinkedHashMap<String, HashMap<String, String>> rows) {
        childRows.put(String.valueOf(childRows.size()), rows);
    }

    public LinkedHashMap<String, ITextTreeObject> getTreeRows() {
        if (treeRows == null) {
            treeRows = new LinkedHashMap<>();
        }
        return treeRows;
    }

    public void setTreeRows(LinkedHashMap<String, ITextTreeObject> treeRows) {
        this.treeRows = treeRows;
    }

    public void addTotalRow(String code, LinkedList<String> values) {
        totalRows.put(code, values);
    }

    public ITextTreeObject getTreeObject() {
        return new ITextTreeObject();
    }

    public LinkedHashMap<String, LinkedHashMap<String, HashMap<String, String>>> getChildRows() {
        return childRows;
    }

    public void setChildRows(LinkedHashMap<String, LinkedHashMap<String, HashMap<String, String>>> childRows) {
        this.childRows = childRows;
    }

    public List<Map<String, List<CellData>>> getRowsList() {
        return rowsList;
    }

    public void setRowsList(List<Map<String, List<CellData>>> rowsList) {
        this.rowsList = rowsList;
    }
}
