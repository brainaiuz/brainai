package com.edatasite.workforce.gwt.core.server.servlets.pdf.template;

import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;

import java.io.IOException;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 21-Jun-2010
 * Time: 18:10:47
 */
public class ITextTemplateFactory {

    public static Element getPdfElement(ITextGenericPdfData pdfData, Document doc) throws DocumentException, IOException {
        ITextPdfTemplate iTextPdfTemplate = null;
        if (ITextPdfViewTypeEnum.LISTTABLE.getType() == pdfData.getPdfViewType().getType()) {
            iTextPdfTemplate = new ITextPdfTableListTemplate(pdfData.getListTable());
        } else if (ITextPdfViewTypeEnum.SUMMARYVIEW.getType() == pdfData.getPdfViewType().getType()) {
            if (pdfData.getSummaryView() != null) {
                iTextPdfTemplate = new ITextPdfViewSummaryTemplate(pdfData.getSummaryView());
            } else {
                iTextPdfTemplate = new ITextPdfViewSummaryArrayTemplate(pdfData.getSummaryViewArray());
            }
        } else if (ITextPdfViewTypeEnum.BASEINVOICE.getType() == pdfData.getPdfViewType().getType()) {
            iTextPdfTemplate = new ITextPdfBaseInvoiceTemplate(pdfData.getBaseInvoice());
        } else if (ITextPdfViewTypeEnum.CUSTOMVIEW.getType() == pdfData.getPdfViewType().getType()) {
            iTextPdfTemplate = new ITextCustomViewTemplate(pdfData.getCustomView());
        }
        return iTextPdfTemplate.generatePdf(doc);
    }
}
