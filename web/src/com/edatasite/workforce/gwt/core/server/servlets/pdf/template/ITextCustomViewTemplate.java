package com.edatasite.workforce.gwt.core.server.servlets.pdf.template;

import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextCustomView;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Fatkhulla
 * Date: 9/13/12
 * Time: 11:05 AM
 * To change this template use File | Settings | File Templates.
 */
public class ITextCustomViewTemplate extends PdfPTable implements ITextPdfTemplate {

    private ITextCustomView customView;
    private Document document;

    public ITextCustomViewTemplate(ITextCustomView customView) {
        super(1);
        this.customView = customView;
    }

    private void initialization() {
        this.setWidthPercentage(100);
        this.setHorizontalAlignment(Element.ALIGN_LEFT);
        this.getDefaultCell().setBorder(0);
        this.getDefaultCell().setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
    }

    @Override
    public Element generatePdf(Document doc) throws DocumentException, IOException {
        doc.add(customView.getCustomTable());
        return this;
    }
}
