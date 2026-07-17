package com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data;

import com.lowagie.text.pdf.PdfPTable;

/**
 * Created by IntelliJ IDEA.
 * User: Fatkhulla
 * Date: 9/13/12
 * Time: 10:59 AM
 * To change this template use File | Settings | File Templates.
 */
public class ITextCustomView {
    private PdfPTable customTable;


    public PdfPTable getCustomTable() {
        return customTable;
    }

    public void setCustomTable(PdfPTable customTable) {
        this.customTable = customTable;
    }
}
