package com.edatasite.workforce.rest.v2.release10.core.to.base.pdf;

import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;

/**
 * Created by Anvar Akramov on 02/21/2018.
 */
public class ITextGenericDataTO extends ResponseData {

    private ITextGenericPdfData itextGenericPdfData;

    public ITextGenericDataTO() {
    }

    public ITextGenericDataTO(ITextGenericPdfData itextGenericPdfData) {
        this.itextGenericPdfData = itextGenericPdfData;
    }

    public ITextGenericPdfData getItextGenericPdfData() {
        return itextGenericPdfData;
    }

    public void setItextGenericPdfData(ITextGenericPdfData itextGenericPdfData) {
        this.itextGenericPdfData = itextGenericPdfData;
    }
}
