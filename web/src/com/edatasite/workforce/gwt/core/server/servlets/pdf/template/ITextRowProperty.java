package com.edatasite.workforce.gwt.core.server.servlets.pdf.template;

/**
 * Created by IntelliJ IDEA.
 * User: Sanjar
 * Date: Mar 29, 2011
 * Time: 2:58:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class ITextRowProperty {
    private String backgroundColor;
    private Integer colspan;
    private Integer rowspan;

    public ITextRowProperty() {
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public Integer getColspan() {
        return colspan;
    }

    public void setColspan(Integer colspan) {
        this.colspan = colspan;
    }

    public Integer getRowspan() {
        return rowspan;
    }

    public void setRowspan(Integer rowspan) {
        this.rowspan = rowspan;
    }
}
