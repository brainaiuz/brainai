package com.edatasite.shared.poiutils;

import com.edatasite.workforce.gwt.core.server.commons.ExcelData;
import org.apache.poi.hssf.usermodel.HeaderFooter;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class WorkBook {
    HSSFWorkbook wb;
    HSSFSheet sheet;
    HSSFRow row;
    HSSFCell cell;
    List<ExcelData[]> list;
    HSSFFont font;
    HSSFCellStyle style;
    DataFormat format;
    HashMap<Integer, HSSFFont> fonts;
    HashMap<Integer, HSSFCellStyle> styles;
    boolean freezePane = true;
    int colSplit, rowSplit, leftmostColumn, topRow;
    short headerRowHeight, contentRowHeight;
    Drawing drawing;

    public WorkBook(boolean freezePane, int colSplit, int rowSplit, int leftmostColumn, int topRow) {
        this.colSplit = colSplit;
        this.rowSplit = rowSplit;
        this.leftmostColumn = leftmostColumn;
        this.topRow = topRow;
        this.freezePane = freezePane;
    }

    public WorkBook(List<ExcelData[]> list) {
        this.list = list;
        this.freezePane = false;
    }

    public WorkBook(List<ExcelData[]> list, boolean freezePane, int rowSplit, int colSplit) {
        this.list = list;
        this.colSplit = colSplit;
        this.rowSplit = rowSplit;
        this.leftmostColumn = colSplit;
        this.topRow = rowSplit;
        this.freezePane = freezePane;
    }

    public WorkBook(List<ExcelData[]> list, boolean freezePane, int rowSplit, int colSplit, HashMap<Integer, HSSFCellStyle> styles) {
        this.list = list;
        this.colSplit = colSplit;
        this.rowSplit = rowSplit;
        this.leftmostColumn = colSplit;
        this.topRow = rowSplit;
        this.freezePane = true;
        this.styles = styles;
    }

    public WorkBook(List<ExcelData[]> list, boolean freezePane, int colSplit, int rowSplit, int leftmostColumn, int topRow) {
        this.list = list;
        this.colSplit = colSplit;
        this.rowSplit = rowSplit;
        this.leftmostColumn = leftmostColumn;
        this.topRow = topRow;
        this.freezePane = freezePane;
    }

    public void setList(List<ExcelData[]> list) {
        this.list = list;
    }

    public void setSheetName(String name) {
        if (wb == null) {
            wb = new HSSFWorkbook();
        }
        sheet = wb.createSheet(name);
    }

    public HSSFSheet getSheet() {
        return sheet;
    }

    public HSSFWorkbook getWorkBook(String sheetname, int startrownum, int startcellnum, int fromListIndex, int toListIndex) {
        if (wb == null) {
            wb = new HSSFWorkbook();
        }
        if (wb.getSheet(sheetname) == null) {
            sheet = wb.createSheet(sheetname);
            if (freezePane) {
                sheet.createFreezePane(colSplit, rowSplit, leftmostColumn, topRow);
            }
        }


        fonts = new HashMap<>();
        styles = new HashMap<>();
        format = wb.createDataFormat();
        drawing = sheet.createDrawingPatriarch();
        boolean autoSizeColumn = true;
        for (ExcelData[] fields : list) {
            setCell(sheet.createRow(startrownum++), startcellnum, fields, fromListIndex, toListIndex, autoSizeColumn);
            autoSizeColumn = false;
        }

        sheet.getPrintSetup().setLandscape(true);
        Footer footer = sheet.getFooter();
        footer.setRight("Page " + HeaderFooter.page() + " of " + HeaderFooter.numPages());

        return wb;
    }

    public void setCell(HSSFRow r, int cellnum, ExcelData[] fields, int from, int to, boolean autoSizeColumn) {

        for (int i = from; i < fields.length; i++) {
            if (fields[i] != null) {

                Integer sumInteger = fields[i].getDataPosiontionInPage() * 2 + fields[i].getFontSize() * 4 + (fields[i].isBold() ? 1 : 0) + (fields[i].isGroupCellBorder() ? 1 : 0);
                Integer stayleInteger = fields[i].getDataType() + fields[i].getDataPosiontionInPage() * 2 + fields[i].getFontSize() * 4 + fields[i].getBgcolor() * 8 + (fields[i].isCenterAligned() ? 1 : 0) * 16 + (fields[i].isBold() ? 1 : 0) * 32 + (fields[i].isGroupCellBorder() ? 1 : 0) * 64;

                if (fonts.containsKey(sumInteger)) {
                    font = fonts.get(sumInteger);
                } else {
                    font = wb.createFont();
                }

                if (styles.containsKey(stayleInteger)) {
                    style = styles.get(stayleInteger);
                } else {
                    style = wb.createCellStyle();
                }

                if (fields[i].getCellIndex() == null) {
                    cell = r.createCell(cellnum + i);
                } else {
                    cell = r.createCell(fields[i].getCellIndex());
                }
                if (fields[i].getValue() != null) {
                    if (fields[i].getDataType() == ExcelData.STRING) {
                        cell.setCellValue(fields[i].getValue() != null ? fields[i].getValue().toString() : "");
                    } else if (fields[i].getDataType() == ExcelData.INTEGER) {
                        cell.setCellValue((Integer) fields[i].getValue());
                        style.setDataFormat((short) 1);
                    } else if (fields[i].getDataType() == ExcelData.DATE) {
                        setDateWithFormate((Date) fields[i].getValue(), "yyyy-MM-dd");
                    } else if (fields[i].getDataType() == ExcelData.DOUBLE) {
                        cell.setCellValue((Double) fields[i].getValue());
                    } else if (fields[i].getDataType() == ExcelData.NUMBER_FORMAT_0_00) {
                        cell.setCellValue(Double.valueOf(String.valueOf(fields[i].getValue())));
                        style.setDataFormat((short) 4);
                    } else if (fields[i].getDataType() == ExcelData.NUMBER_FORMAT_PERCENTAGE) {
                        cell.setCellValue((Double) fields[i].getValue());
                        style.setDataFormat(format.getFormat("#0.00%"));
                    } else if (fields[i].getDataType() == ExcelData.BIG_DECIMAL) {
                        cell.setCellValue(Double.valueOf(String.valueOf(fields[i].getValue()).replace(",", "")));//bu yerda Srting number ",##0.00" kelib qolsa numberFormatException tashlayotgan edi
                    } else if (fields[i].getDataType() == ExcelData.BOOLEAN) {
                        cell.setCellValue((Boolean) fields[i].getValue());
                    } else if (fields[i].getDataType() == ExcelData.TIME_FORMAT_Short_time_HH_mm) {
                        setTimeWithFormat(fields[i].getValue().toString());
                    } else if (fields[i].getDataType() == ExcelData.CURRENCY) {
                        cell.setCellValue(Double.valueOf(String.valueOf(fields[i].getValue())));
                        style.setDataFormat(format.getFormat("#,##0.00"));
                    }
                    if (fields[i].getCommentString() != null) {
                        ClientAnchor anchor = wb.getCreationHelper().createClientAnchor();
                        anchor.setCol1(cell.getColumnIndex());
                        anchor.setCol2(cell.getColumnIndex() + 3);
                        anchor.setRow1(cell.getRowIndex());
                        anchor.setRow2(cell.getRowIndex() + 3);
                        Comment comment = drawing.createCellComment(anchor);
                        comment.setString(wb.getCreationHelper().createRichTextString(fields[i].getCommentString()));
                        cell.setCellComment(comment);
                    }
                    if (fields[i].getCellSize() > 0) {
                        sheet.setColumnWidth((cellnum + i), fields[i].getCellSize());
                    } else if (fields[i].getColumnSize() > 0) {
                        sheet.setColumnWidth((cellnum + i), fields[i].getColumnSize().intValue());
                    }
                } else {
                    if (fields[i].getDataType() == ExcelData.CURRENCY) {
                        style.setDataFormat(format.getFormat("#,##0.00"));
                    }
                }
                if (fields[i].isMerged()) {
                    fields[i].addMerging(sheet,
                            r.getRowNum() + fields[i].getFromRow(),
                            fields[i].getFromCell(),
                            r.getRowNum() + fields[i].getToRow(),
                            fields[i].getToCell());
                }


                fields[i].setStyle(style, font);
                fonts.put(sumInteger, font);
                styles.put(stayleInteger, style);
                if (fields[i].getDataType() == ExcelData.TIME_FORMAT_Short_time_HH_mm) {
                    setTimeWithFormat(fields[i].getValue().toString());
                } else if (fields[i].getDataType() == ExcelData.CURRENCY) {
                    double value = fields[i].getValue() != null ? Double.valueOf(String.valueOf(fields[i].getValue())) : 0d;
                    cell.setCellValue(value);
                    style.setDataFormat(format.getFormat("#,##0.00"));
                }
                cell.setCellStyle(style);
            }
        }
    }

    public void setWorkBook(HSSFWorkbook wb) {
        this.wb = wb;
    }

    public void setDateWithFormate(Date date, String format) {
        cell.setCellValue(date);
        style.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat("yyyy-MM-dd"));
    }

    public void setTimeWithFormat(String time) {//
        if (time != null && !time.isEmpty()) {
            try {
                String[] tString = time.split(":");
                double timeDouble = Double.valueOf(tString[0]) + ((1d / 60d) * Double.valueOf(tString[1]));
                cell.setCellValue(timeDouble / 24d);
                style.setDataFormat(wb.createDataFormat().getFormat("[h]:mm"));

            } catch (Exception e) {
                cell.setCellValue(time);
            }
        } else {
            cell.setCellValue(time);
        }
    }

    public void drowGroupCellBorlder(int fromRow, int toRow, int fromCell, int toCell) {
        for (int i = fromRow; i <= toRow; i++) {
            for (int j = fromCell; j <= toCell; j++) {
                row = sheet.getRow(i);
                cell = row.getCell(j);
                if (cell != null) {
                    setBorder(cell.getCellStyle());
                } else {
                    style = wb.createCellStyle();
                    cell = row.createCell(j);
                    setBorder(style);
                    cell.setCellStyle(style);
                }
            }
        }
    }

    public void drawChart(byte[] chart) {
        HSSFPatriarch patriarch = sheet.createDrawingPatriarch();

        HSSFClientAnchor anchor;
        anchor = new HSSFClientAnchor(0, 0, 0, 255, (short) 1, 1, (short) 14, 40);
        anchor.setAnchorType(2);
        HSSFPicture picture = patriarch.createPicture(anchor, wb.addPicture(chart, HSSFWorkbook.PICTURE_TYPE_PNG));
        picture.setNoFill(true);
    }

    private void setBorder(HSSFCellStyle style) {
        style.setBorderTop(HSSFCellStyle.BORDER_THIN);
        style.setTopBorderColor(ExcelData.BLACK);

        style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        style.setLeftBorderColor(ExcelData.BLACK);

        style.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        style.setBottomBorderColor(ExcelData.BLACK);

        style.setBorderRight(HSSFCellStyle.BORDER_THIN);
        style.setRightBorderColor(ExcelData.BLACK);
    }

}
