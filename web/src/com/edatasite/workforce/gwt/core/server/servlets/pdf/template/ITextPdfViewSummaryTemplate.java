package com.edatasite.workforce.gwt.core.server.servlets.pdf.template;

import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextSummaryView;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.io.IOException;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 24-Jun-2010
 * Time: 19:37:03
 * All Summary View pdf generate Template
 */
public class ITextPdfViewSummaryTemplate extends PdfPTable implements ITextPdfTemplate {

    private ITextSummaryView summaryView;

    public ITextPdfViewSummaryTemplate(ITextSummaryView summaryView) {
        super(1);
        this.summaryView = summaryView;
        initialization();
    }

    /**
     * set Default properties
     */
    private void initialization() {
        this.setSplitLate(false);
        this.setWidthPercentage(100);
        this.getDefaultCell().setPadding(10);
        this.getDefaultCell().setBorder(0);
        this.getDefaultCell().setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
    }

    /**
     * @return
     * @throws DocumentException
     * @throws IOException
     */
    public Element generatePdf(Document doc) throws DocumentException, IOException {
        for (ITextSummaryView.RowData row : summaryView.getTables()) {
            ITextTableList[] tables = row.getTables();
            PdfPTable table = new PdfPTable(tables.length);
            table.setWidthPercentage(100);
            table.getDefaultCell().setBorderWidth(0);
            table.getDefaultCell().setVerticalAlignment(Element.ALIGN_TOP);
            table.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
            if (row.getWidthPercentages() != null) {
                table.setWidths(row.getWidthPercentages());
            }
            for (ITextTableList table1 : tables) {
                ITextPdfTableListTemplate tableTemplate = new ITextPdfTableListTemplate(table1);
                if (table1.getTableAlignment() != null) {
                    PdfPCell cell = new PdfPCell((PdfPTable) tableTemplate.generatePdf(doc));
                    cell.setBorderWidth(0);
                    cell.setHorizontalAlignment(table1.getTableAlignment());
                    table.addCell(cell);
                } else {
                    table.addCell((PdfPTable) tableTemplate.generatePdf(doc));
                }
            }
            this.addCell(table);
        }
        return this;
    }
}
