package com.edatasite.workforce.gwt.core.server.servlets.pdf.template;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 21-Jun-2010
 * Time: 18:12:15
 */
public enum ITextPdfViewTypeEnum {
    LISTTABLE(0),
    SUMMARYVIEW(1),
    BASEINVOICE(2),
    CUSTOMVIEW(3);

    ITextPdfViewTypeEnum(int type) {
        this.type = type;
    }

    private int type;

    public int getType() {
        return type;
    }
}
