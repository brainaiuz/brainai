package com.edatasite.shared.poiutils;

import com.edatasite.workforce.gwt.core.server.commons.ExcelData;
import org.apache.poi.hssf.usermodel.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Dmitriy
 * Date: Dec 25, 2008
 */
public class WfmExcelSheet {

    /**
     * Creates Excel WorkBook with single Sheet
     */

    private HSSFWorkbook wb;
    private HSSFSheet sheet;
    private HSSFCell cell;
    private HSSFFont font;
    private HSSFCellStyle style;
    private int currentRowPos = 0;


    public WfmExcelSheet(String sheetName) {
        this(sheetName, 0);
    }

    public WfmExcelSheet(String sheetName, int firstRowPos) {
        wb = new HSSFWorkbook();
        sheet = wb.createSheet(sheetName);
        this.currentRowPos = firstRowPos;
        font = wb.createFont();
        style = wb.createCellStyle();
    }

    public void addRow(ExcelData[] fields) {
        addRow(fields, 0, 0);
    }

    public void addRow(ExcelData[] fields, int cellnum, int from) {
        HSSFRow r = sheet.createRow(currentRowPos++);
        for (int i = from; i < fields.length; i++) {
            cell = r.createCell((short) (cellnum + i));
            if (fields[i].getValue() != null) {
                if (fields[i].getDataType() == ExcelData.STRING) {
                    cell.setCellValue((String) fields[i].getValue());
                } else if (fields[i].getDataType() == ExcelData.INTEGER) {
                    cell.setCellValue((Integer) fields[i].getValue());
                } else if (fields[i].getDataType() == ExcelData.DATE) {
                    setDateWithFormate((Date) fields[i].getValue(), "yyyy-MM-dd");
                } else if (fields[i].getDataType() == ExcelData.DOUBLE) {
                    cell.setCellValue((Double) fields[i].getValue());
                } else if (fields[i].getDataType() == ExcelData.BIG_DECIMAL) {
                    cell.setCellValue(Double.valueOf(String.valueOf(fields[i].getValue())));
                } else if (fields[i].getDataType() == ExcelData.BOOLEAN) {
                    cell.setCellValue((Boolean) fields[i].getValue());
                }
                if (currentRowPos == 0 || currentRowPos == 1) {
                    if (fields[i].getCellSize() > 0) {
                        sheet.setColumnWidth((short) (cellnum + i), (short) fields[i].getCellSize());
                    }
                    if (fields[i].isAutSize()) {
                        sheet.autoSizeColumn((short) (cellnum + i));
                    }
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
            cell.setCellStyle(style);
        }
    }

    public HSSFWorkbook getResults() {
        return wb;
    }

    private void setDateWithFormate(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        cell.setCellValue(dateFormat.format(date));
    }


}
