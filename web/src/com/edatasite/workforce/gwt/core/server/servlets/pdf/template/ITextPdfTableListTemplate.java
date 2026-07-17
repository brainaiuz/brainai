package com.edatasite.workforce.gwt.core.server.servlets.pdf.template;

import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 21-Jun-2010
 * <p/>
 * This is class Generic Pdf Table List class
 */
public class ITextPdfTableListTemplate extends PdfPTable implements ITextPdfTemplate {
    public static String ELEMENT_STYLE = "<span style=\"font-size:8pt;font-family:lighter Geneva,Arial,Helvetica,sans-serif;\">";
    private int numColumns = 0;
    private ITextTableList dataListIText;

    public ITextPdfTableListTemplate(ITextTableList dataListIText) {
        super(dataListIText.getNumColumns());
        this.numColumns = dataListIText.getNumColumns();
        this.dataListIText = dataListIText;
        intialization();
    }

    /**
     * Initilazation default values
     */
    private void intialization() {
        this.setSplitLate(false);
        this.setWidthPercentage(100);// width set in percent
        this.getDefaultCell().setBorderWidth(0.5f);
        this.getDefaultCell().setPadding(3);
        this.getDefaultCell().setHorizontalAlignment(Element.ALIGN_LEFT);
        this.setRunDirection(PdfWriter.RUN_DIRECTION_LTR);
        this.setHorizontalAlignment(Element.ALIGN_LEFT);
    }

    /**
     * This method generate pdf by GenericPdfDataList object in data
     *
     * @return create pdf table
     * @throws DocumentException
     */
    public Element generatePdf(Document doc) throws DocumentException, IOException {

        String fontName = dataListIText.getFontName() != null ? dataListIText.getFontName() : ITextFontTypeEnum.ARIAL.getName();

        if (dataListIText.getTotalWidth() != null) {
            this.setTotalWidth(dataListIText.getTotalWidth());
            this.setLockedWidth(true);
        } else {
            this.setTotalWidth(doc.getPageSize().getWidth() - doc.leftMargin() - doc.rightMargin());
            this.setLockedWidth(true);
        }
        if (dataListIText.getColWidthPercentage() != null && dataListIText.getColWidthPercentage().length == numColumns) {
            this.setWidths(dataListIText.getColWidthPercentage());
        }
        if (dataListIText.getTableAlignment() != null) {
            this.setHorizontalAlignment(dataListIText.getTableAlignment());
        }
        if (dataListIText.getBorderWidth() != -1) {
            this.getDefaultCell().setBorderWidth(dataListIText.getBorderWidth());
        }
        if (dataListIText.getCellPadding() != -1) {
            this.getDefaultCell().setPadding(dataListIText.getCellPadding());
        }
        if (dataListIText.getBeforSpacing() != -1) {
            this.setSpacingBefore(dataListIText.getBeforSpacing());
        }
        if (dataListIText.getAfterSpacing() != -1) {
            this.setSpacingAfter(dataListIText.getAfterSpacing());
        }
        if (dataListIText.getName() != null && !"".equals(dataListIText.getName())) {
            PdfPCell thema = new PdfPCell(new Phrase(dataListIText.getName(), FontFactory.getFont(fontName, BaseFont.IDENTITY_H, 10, Font.BOLD)));
            thema.setPadding(10);
            thema.setBorder(0);
            thema.setColspan(numColumns);
            thema.setHorizontalAlignment(Element.ALIGN_LEFT);
            this.addCell(thema);
        }

        if (dataListIText.getNamePositionCenter() != null && !"".equals(dataListIText.getNamePositionCenter())) {
            PdfPCell thema = new PdfPCell(new Phrase(dataListIText.getNamePositionCenter(), FontFactory.getFont(fontName, BaseFont.IDENTITY_H, 10, Font.BOLD)));
            thema.setPadding(5);
            thema.setBorder(0);
            thema.setColspan(numColumns);
            thema.setHorizontalAlignment(Element.ALIGN_CENTER);
            this.addCell(thema);
        }

        if (dataListIText.getNamePositionRight() != null && !"".equals(dataListIText.getNamePositionRight())) {
            PdfPCell thema = new PdfPCell(new Phrase(dataListIText.getNamePositionRight(), FontFactory.getFont(fontName, BaseFont.IDENTITY_H, 10, Font.BOLD)));
            thema.setPadding(10);
            thema.setBorder(0);
            thema.setColspan(numColumns);
            thema.setHorizontalAlignment(Element.ALIGN_RIGHT);
            this.addCell(thema);
        }

        if (ITextPdfHeaderPosition.FIRST_HORIZONTAL_ROW.equals(dataListIText.getHeaderPosition())) {
            addTableHeades(dataListIText.getTableHeader(), fontName);
        }
        addTableRows(dataListIText.getTableRows(), fontName);
        addTableFooter(dataListIText.getTableFooter(), fontName);
        return this;
    }

    /**
     * Add to Table header
     *
     * @param columnHeaders
     */
    private void addTableHeades(List<CellData> columnHeaders, String fontName) {
        if (columnHeaders.size() != 0) {
            Font font8Bold = FontFactory.getFont(fontName, BaseFont.IDENTITY_H, 8, Font.BOLD);
            for (CellData header : columnHeaders) {
                PdfPCell cell = new PdfPCell(new Phrase(header.getText(), header.getFont() != null ? header.getFont() : font8Bold));
                cell.setHorizontalAlignment(header.getAlignment() != null ? header.getAlignment() : Element.ALIGN_CENTER);
                cell.setBackgroundColor(header.getBgColor() != null ? header.getBgColor() : Color.LIGHT_GRAY);
                cell.setBorderWidth(dataListIText.getBorderWidth() != -1 ? dataListIText.getBorderWidth() : 0.5f);
                cell.setPadding(dataListIText.getCellPadding() != -1 ? dataListIText.getCellPadding() : 3);
                if (header.getColspan() != null) {
                    cell.setColspan(2);
                }
                if (header.getBorderColor() != null) {
                    cell.setBorderColor(header.getBorderColor());
                }
                if (header.getBorder() != null) {
                    cell.setBorder(header.getBorder());
                }
                this.addCell(cell);
            }
            this.setHeaderRows(this.size());
        }
    }

    /**
     * Add Table Rows
     *
     * @param rows
     */
    private void addTableRows(List<CellData[]> rows, String fontName) throws DocumentException, IOException {
        if (rows != null) {
            int step = 0;
            Font font8Normal = FontFactory.getFont(fontName, BaseFont.IDENTITY_H, 8, Font.NORMAL);
            Font font8Link = FontFactory.getFont(fontName, BaseFont.IDENTITY_H, 8, Font.NORMAL);
            font8Link.setColor(Color.BLUE);
            for (CellData[] row : rows) {
                int length_ = numColumns < row.length ? numColumns : row.length;
                if (ITextPdfHeaderPosition.FIRST_VERTICAL_COLUMN.equals(dataListIText.getHeaderPosition()) && dataListIText.getTableHeader().size() != 0) {
                    setVerticalHeader(dataListIText.getTableHeader().get(step++), fontName);
                }
                for (int i = 0; i < length_; i++) {// add to table row columns
                    PdfPCell cell = row[i].createPdfCell(font8Normal, font8Link);

                    if (row[i].getAlignment() != null) {
                        cell.setHorizontalAlignment(row[i].getAlignment());
                    }

                    if (dataListIText.getBorderWidth() != -1) {
                        cell.setBorderWidth(dataListIText.getBorderWidth());
                    } else {
                        cell.setBorderWidth(0.5f);
                    }

                    if (row[i].getBorderColor() != null) {
                        cell.setBorderColor(row[i].getBorderColor());
                    }
                    if (row[i].getBorder() != null) {
                        cell.setBorder(row[i].getBorder());
                    }
                    if (row[i].getColspan() != null) {
                        cell.setColspan(row[i].getColspan());
                    }
                    if (row[i].getBorderLeft() != null && row[i].getBorderLeft() > 0) {
                        cell.setBorderWidthLeft(row[i].getBorderLeft());
                    }
                    if (row[i].getBorderRight() != null && row[i].getBorderRight() > 0) {
                        cell.setBorderWidthRight(row[i].getBorderRight());
                    }
                    if (row[i].getBgColor() != null) {
                        cell.setBackgroundColor(row[i].getBgColor());
                    }
                    if (row[i].getPadding() != null) {
                        if (row[i].getPadding().getLeft() != null) {
                            cell.setPaddingLeft(row[i].getPadding().getLeft());
                        }
                        if (row[i].getPadding().getRight() != null) {
                            cell.setPaddingRight(row[i].getPadding().getRight());
                        }
                        if (row[i].getPadding().getTop() != null) {
                            cell.setPaddingTop(row[i].getPadding().getTop());
                        }
                        if (row[i].getPadding().getBottom() != null) {
                            cell.setPaddingBottom(row[i].getPadding().getBottom());
                        }
                    } else {
                        if (dataListIText.getCellPadding() != -1) {
                            cell.setPadding(dataListIText.getCellPadding());
                        } else {
                            cell.setPadding(3);
                        }
                    }
                    this.addCell(cell);
                }
                if (dataListIText.isLastCellAdd() && row.length < numColumns && !ITextPdfHeaderPosition.FIRST_VERTICAL_COLUMN.equals(dataListIText.getHeaderPosition())) {// if row.length < numColumns  [ rowlength . . . numcolumns ] put space string or span the cols...
                    PdfPCell cell = new PdfPCell();
                    cell.setColspan(numColumns - row.length);
                    this.addCell(cell);
                }
            }
        }
    }

    /**
     * Set First Column Table Header
     *
     * @param header
     */
    private void setVerticalHeader(CellData header, String fontName) {
        Font font8Bold = FontFactory.getFont(fontName, BaseFont.IDENTITY_H, 8, Font.BOLD);
        PdfPCell cell = new PdfPCell(new Phrase(header.getText(), font8Bold));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setBackgroundColor(Color.LIGHT_GRAY);
        if (dataListIText.getBorderWidth() != -1) {
            cell.setBorderWidth(dataListIText.getBorderWidth());
        } else {
            cell.setBorderWidth(0.5f);
        }
        if (dataListIText.getCellPadding() != -1) {
            cell.setPadding(dataListIText.getCellPadding());
        } else {
            cell.setPadding(3);
        }
        this.addCell(cell);
    }

    /**
     * Add Table Footer
     *
     * @param columnFooter
     */
    private void addTableFooter(List<String> columnFooter, String fontName) {
        if (columnFooter.size() != 0) {
            Font font8Bold = FontFactory.getFont(fontName, BaseFont.IDENTITY_H, 8, Font.BOLD);
            for (String header : columnFooter) {
                PdfPCell cell = new PdfPCell(new Phrase(header, font8Bold));
                cell.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell.setBackgroundColor(Color.LIGHT_GRAY);
                if (dataListIText.getBorderWidth() != -1) {
                    cell.setBorderWidth(dataListIText.getBorderWidth());
                } else {
                    cell.setBorderWidth(0.5f);
                }
                if (dataListIText.getCellPadding() != -1) {
                    cell.setPadding(dataListIText.getCellPadding());
                } else {
                    cell.setPadding(3);
                }
                this.addCell(cell);
            }
            this.setFooterRows(this.size());
        }
    }
}
