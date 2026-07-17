/*
package com.finnetlimited.reportservice.core.server.generate;

import com.finnetlimited.reportservice.core.client.gwtrpc.ReportGenerateTableRpc;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Rectangle;
import com.lowagie.text.html.simpleparser.HTMLWorker;
import com.lowagie.text.html.simpleparser.StyleSheet;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPageEventHelper;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java.util.List;

*/
/**
 * User: ${Dilsh0d}
 * Date: 14-Apr-2010
 * Time: 13:03:36
 *//*

public final class GenerateReportToPdf extends PdfPageEventHelper {

    private static Font font14 = FontFactory.getFont("Tahoma,Arial,sans-serif", 14f, Font.BOLD, Color.black);
    private static Font font11 = FontFactory.getFont("Tahoma,Arial,sans-serif", 11f, Font.NORMAL, Color.black);

    private int width = 0;
    private PdfPTable table;
    private Document document;
    private OutputStream stream;
    private ReportGenerateTableRpc reportData;

    public GenerateReportToPdf(ReportGenerateTableRpc reportData, OutputStream stream) {
        this.stream = stream;
        this.reportData = reportData;
    }

    public void generateToPdf() {
        List<Integer> numbers = new ArrayList<Integer>();
        numbers.add(25);
        width += 30;
        for (int s = 0; s < reportData.getTitleRows().size(); s++) {
            width += reportData.getTitleRows().get(s).getTitle().length();
            numbers.add(reportData.getTitleRows().get(s).getTitle().length() * 7);
        }
        width *= 7;
        width += 12;
        if (width < 700) {
            document = new Document(PageSize.A4, 10, 10, 10, 10);
        } else {
            Rectangle rec = new Rectangle(width, (int) (width * 0.75));
            document = new Document(rec, 10, 10, 10, 10);
        }
        try {
            PdfWriter writer = PdfWriter.getInstance(document, stream);
            writer.setPageEvent(this);

            document.open();

            document.addTitle("Dynamic Reporting Service");
            document.addAuthor("Dilshod.T dilshod.toj@gmail.com");
            document.addSubject("ReportService convert to pdf");

            int[] colWidth = new int[numbers.size()];
//            for (int i = 0; i < colWidth.length; i++)
//                colWidth[i] = numbers.get(i);

            table = new PdfPTable(colWidth.length);
//            table.setWidths(colWidth);
            table.setWidthPercentage(100);
            table.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.getDefaultCell().setBorderColor(new Color(0xff9ABAE9));

            */
/* tabular report title *//*

            PdfPCell cell = new PdfPCell(new Phrase("#", font14));
            cell.setBorderWidth(1);
            cell.setPaddingLeft(5);
            cell.setBorderColor(new Color(0xff9ABAE9));
            cell.setBackgroundColor(new Color(0xffff9a00));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell);
            for (int s = 0; s < reportData.getTitleRows().size(); s++) {
                cell = new PdfPCell(new Phrase(reportData.getTitleRows().get(s).getTitle(), font14));
                cell.setBorderWidth(1);
                cell.setPaddingLeft(5);
                cell.setBorderColor(new Color(0xff9ABAE9));
                cell.setBackgroundColor(new Color(0xffff9a00));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell);
            }
            */
/* calc tabular report *//*

            for (int k = 0; k < reportData.getTotalPosition().size(); k++) {
                Paragraph p = getParseHtml("<b>" + reportData.getTotalPosition().get(k).getTotalType() + "</b>");
                p.setFont(font11);
                cell = new PdfPCell(p);
                cell.setBorderWidth(1);
                cell.setPaddingLeft(5);
                cell.setBorderColor(new Color(0xff9ABAE9));
                cell.setBackgroundColor(new Color(0xfffac743));
                table.addCell(cell);
                for (int s = 0; s < reportData.getTotalPosition().get(k).getColumnData().size(); s++) {
                    p = getParseHtml("<b>" + reportData.getTotalPosition().get(k).getColumnData().get(s) + "</b>");
                    p.setFont(font11);
                    cell = new PdfPCell(p);
                    cell.setBorderWidth(1);
                    cell.setPaddingLeft(5);
                    cell.setBorderColor(new Color(0xff9ABAE9));
                    cell.setBackgroundColor(new Color(0xfffac743));
                    table.addCell(cell);
                }
            }

            table.setHeaderRows(1);
            for (int s = 0; s < reportData.getRows().size(); s++) {
                if (reportData.getRows().get(s).getTotalType() != null || reportData.getReport().getIsDetailed()) {
                    Phrase phrase = new Phrase((s + reportData.getNowPosition()) + "", font11);
                    cell = new PdfPCell(phrase);
                    cell.setBorderWidth(1);
                    cell.setPaddingLeft(5);
                    cell.setBorderColor(new Color(0xff9ABAE9));
                    table.addCell(cell);
                    if (reportData.getRows().get(s).getColspan() != 0) {
                        cell = new PdfPCell();
                        cell.setBorderWidth(1);
                        cell.setPaddingLeft(5);
                        cell.setBorderColor(new Color(0xff9ABAE9));
                        cell.setBackgroundColor(Color.white);
                        cell.setColspan(reportData.getRows().get(s).getColspan());
                        table.addCell(cell);
                    }
                    for (int k = reportData.getRows().get(s).getColspan(); k < reportData.getRows().get(s).getColumnData().size(); k++) {
                        if (reportData.getRows().get(s).getTotalType() != null) {
                            Paragraph p = getParseHtml(reportData.getRows().get(s).getColumnData().get(k));
                            p.setFont(font11);
                            cell = new PdfPCell(p);
                            cell.setBorderWidth(1);
                            cell.setPaddingLeft(5);
                            cell.setBorderColor(new Color(0xff9ABAE9));
                            cell.setBackgroundColor(new Color(0xfffac743));
                            table.addCell(cell);
                        } else {
                            Paragraph p = getParseHtml(reportData.getRows().get(s).getColumnData().get(k));
                            p.setFont(font11);
                            table.addCell(p);
                        }
                    }
                }
            }
            document.add(table);
            document.close();
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Paragraph getParseHtml(String s) throws IOException {
        Paragraph paragraph = new Paragraph();
        if (s == null) {
            return paragraph;
        }
        ArrayList objects = HTMLWorker.parseToList(new StringReader(s), new StyleSheet());
        for (int k = 0; k < objects.size(); ++k) {
            paragraph.add((Element) objects.get(k));
        }
        return paragraph;
    }

    @Override
    public void onStartPage(PdfWriter pdfWriter, Document document) {
        super.onStartPage(pdfWriter, document);
        Rectangle rec = new Rectangle(5, 5, document.getPageSize().getWidth() - 5, document.getPageSize().getHeight() - 5);
        rec.setBorder(15);
        rec.setBorderWidth(1);
        rec.setBorderColor(new Color(50, 102, 157));
        try {
            document.add(rec);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }
}
*/
