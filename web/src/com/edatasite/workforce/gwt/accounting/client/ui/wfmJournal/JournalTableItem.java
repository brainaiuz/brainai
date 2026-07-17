package com.edatasite.workforce.gwt.accounting.client.ui.wfmJournal;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 23.02.2009
 * Time: 19:32:19
 * To change this template use File | Settings | File Templates.
 */
public class JournalTableItem {

    private Object[] values;

    private Integer[] colspans;
    private String backgroundColor;
    private String rowStyleName;

    public JournalTableItem(Object[] values) {
        this.values = values;
    }

    public Object[] getValues() {
        return values;
    }

    public Integer[] getColspans() {
        return colspans;
    }

    public void setColspans(Integer[] colspans) {
        this.colspans = colspans;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getRowStyleName() {
        return rowStyleName;
    }

    public void setRowStyleName(String rowStyleName) {
        this.rowStyleName = rowStyleName;
    }
}
