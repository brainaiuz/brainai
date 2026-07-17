package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;

import java.io.IOException;

public class BuildAssemblyViewPdfHandler extends AbstractITextPostPdfHandler implements PDFConstants {

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {

    }
}
