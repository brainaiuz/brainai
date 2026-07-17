package com.edatasite.workforce.gwt.core.server.servlets.pdf.template;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.*;

//import com.lowagie.text.pdf.draw.LineSeparator;

/**
 * User: Dilshod
 * This is Pdf generate event class
 * This is class set in page Footer and Header
 */
public class ITextPdfTemplateEvent extends PdfPageEventHelper {

    private PdfPTable header;
    private PdfPTable footer;
    private boolean onEveryPage = true;
    private boolean isShownPaging = true;
    private boolean isPagingOnTop = false;
    private boolean customFooter = false;

    public ITextPdfTemplateEvent(PdfPTable header, PdfPTable footer, boolean onEveryPage, boolean isShownPaging, boolean isPagingOnTop, boolean customFooter) {
        this.header = header;
        this.footer = footer;
        this.onEveryPage = onEveryPage;
        this.isShownPaging = isShownPaging;
        this.isPagingOnTop = isPagingOnTop;
        this.customFooter = customFooter;
    }

    @Override
    public void onStartPage(PdfWriter writer, Document document) {
        PdfContentByte directContent = writer.getDirectContent();
        if (writer != null && document != null) {
            if (header != null && (writer.getCurrentPageNumber() == 1 || onEveryPage)) {
                if (!document.isOpen()) {
                    document.open();
                }
                if (customFooter) {
                    header.writeSelectedRows(0, -1, 0, document.top() + document.topMargin() - 20, directContent);
                } else {
                    header.writeSelectedRows(0, -1, 20, document.top() + document.topMargin() - 25, directContent);
                }
//                directContent.moveTo(document.leftMargin(), document.top() + document.topMargin() - header.getTotalHeight());
//                directContent.lineTo(document.getPageSize().getWidth()-document.rightMargin(), document.top() + document.topMargin() - header.getTotalHeight());
//                directContent.fillStroke();

                /*LineSeparator lineSeparator = new LineSeparator(0.5f, 100, Color.BLACK, LineSeparator.ALIGN_CENTER, 20);*/
            }
        }
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        if (writer != null && document != null) {
            if (footer != null && onEveryPage) {
                PdfContentByte directContent = writer.getDirectContent();
                if (customFooter)
                    if (document.bottom() == 50f)
                        footer.writeSelectedRows(0, -1, 0, document.bottom() + 25, directContent);
                    else
                        footer.writeSelectedRows(0, -1, 0, document.bottom() - 25, directContent);
                else {
                    footer.writeSelectedRows(0, -1, 20, document.bottom() - 5, directContent);
                }
                if (isShownPaging && isPagingOnTop) {
                    String pageNumber = "Page " + writer.getCurrentPageNumber() + " of " + writer.getPageNumber();
                    PdfPTable pageCounterTable = new PdfPTable(1);
                    pageCounterTable.setTotalWidth(150);
                    pageCounterTable.getDefaultCell().setBorder(0);
                    pageCounterTable.setHorizontalAlignment(PdfPTable.ALIGN_RIGHT);
                    pageCounterTable.getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                    pageCounterTable.addCell(new Phrase(pageNumber, FontFactory.getFont(String.valueOf(ITextFontTypeEnum.DEJAVUSANS), BaseFont.IDENTITY_H, 8)));
                    int height = 5;

                    pageCounterTable.writeSelectedRows(0, -1, document.right() - 150, document.top() + 100, directContent);
                }
            }
        }
    }

    @Override
    public void onOpenDocument(PdfWriter writer, Document document) {
        super.onOpenDocument(writer, document);
    }


    @Override
    public void onCloseDocument(PdfWriter writer, Document document) {
        if (writer != null && document != null) {
            if (footer != null && !onEveryPage) {
                PdfContentByte directContent = writer.getDirectContent();
                footer.writeSelectedRows(0, -1, 20, document.bottom() - 5, directContent);
                if (isShownPaging && !isPagingOnTop) {
                    Phrase pageCounter = new Phrase("Page " + writer.getCurrentPageNumber(), new Font(Font.HELVETICA, 10, Font.BOLD, Color.BLACK));
                    PdfPTable pageCounterTable = new PdfPTable(1);
                    pageCounterTable.setTotalWidth(150);
                    pageCounterTable.getDefaultCell().setBorder(0);
                    pageCounterTable.setHorizontalAlignment(PdfPTable.ALIGN_RIGHT);
                    pageCounterTable.getDefaultCell().setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
                    pageCounterTable.addCell(pageCounter);
                    pageCounterTable.writeSelectedRows(0, -1, document.right() - 200, document.bottom() - 5, directContent);
                }
            }
        }
    }

    public PdfPTable getFooter() {
        return footer;
    }
}
