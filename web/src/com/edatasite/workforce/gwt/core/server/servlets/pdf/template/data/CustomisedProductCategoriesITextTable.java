package com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data;

import com.edatasite.workforce.gwt.core.server.servlets.pdf.PDFConstants;

import java.util.*;

/**
 * User: Abror Abdukadirov
 * Date: 14.11.2016 15:46
 */
public class CustomisedProductCategoriesITextTable implements PDFConstants {
    private Map<String, String> rows = new HashMap<>();
    private CustomisedITextTable table;
    private CustomisedITextTable innerTable;

    public Map<String, String> getRows() {
        return rows;
    }

    public void setRows(Map<String, String> rows) {
        this.rows = rows;
    }

    public CustomisedITextTable getTable() {
        return table;
    }

    public void setTable(CustomisedITextTable table) {
        this.table = table;
    }

    public CustomisedITextTable getInnerTable() {
        return innerTable;
    }

    public void setInnerTable(CustomisedITextTable innerTable) {
        this.innerTable = innerTable;
    }
}
