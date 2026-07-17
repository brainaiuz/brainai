package com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 22-Jun-2010
 * Time: 16:56:18
 * <p/>
 * This is all view summary pdf template repository
 */
public class ITextSummaryView {

    private List<RowData> tables;

    public List<RowData> getTables() {
        if (tables == null) {
            tables = new ArrayList<>();
        }
        return tables;
    }

    public void addTable(ITextTableList... tableList) {
        if (tableList != null) {
            getTables().add(new RowData(tableList));
        }
    }
    public void addTable(ITextTableList[] tableList, float[] widthPercentages) {
        if (tableList != null) {
            getTables().add(new RowData(tableList, widthPercentages));
        }
    }

    public void setFontName(String fontName) {
        if (tables != null && tables.size() > 0) {
            for (RowData tableArray : tables) {
                if (tableArray != null && tableArray.getTables().length > 0) {
                    for (ITextTableList table : tableArray.getTables()) {
                        if (table != null)
                            table.setFontName(fontName);
                    }
                }
            }
        }
    }

    public static class RowData {
        private ITextTableList[] tables;
        private float[] widthPercentages;

        public RowData(ITextTableList[] tables) {
            this.tables = tables;
        }

        public RowData(ITextTableList[] tables, float[] widthPercentages) {
            this.tables = tables;
            this.widthPercentages = widthPercentages;
        }

        public ITextTableList[] getTables() {
            return tables;
        }

        public float[] getWidthPercentages() {
            return widthPercentages;
        }
    }
}
