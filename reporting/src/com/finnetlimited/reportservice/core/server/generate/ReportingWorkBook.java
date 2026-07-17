package com.finnetlimited.reportservice.core.server.generate;

import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.commons.ExcelData;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Footer;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ReportingWorkBook {

    private HSSFWorkbook wb;
    private HSSFSheet sheet;
    private CellStyle cellStyle;
    private CreationHelper createHelper;
    private HashMap<String, HSSFCellStyle> styles;
    private final DateFormat shortDateFormatter = new SimpleDateFormat("yyyy-MM-dd");
    private final DateFormat longDateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private boolean autoSized = false;
    private Integer rowIndex = 0;
    private TimeZone userTimeZone;

    private int sheetIndex = 1;
    private final int MAX_ROWS_PER_SHEET = 64000;
    private String baseSheetName;
    private final List<ExcelData[]> headerRows = new ArrayList<>();

    public HSSFWorkbook getWorkBook(String sheetName, TimeZone userTimeZone) {
        this.userTimeZone = userTimeZone;
        this.baseSheetName = sheetName;

        if (wb == null) {
            wb = new HSSFWorkbook();
        }
        createNewSheet();

        cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        createHelper = wb.getCreationHelper();

        createDefaultStyles();
        autoSized = true;
        return wb;
    }

    private void createNewSheet() {
        String newSheetName = sheetIndex == 1 ? baseSheetName : baseSheetName + "_" + sheetIndex;
        sheet = wb.createSheet(newSheetName);
        wb.setActiveSheet(wb.getSheetIndex(sheet));

        sheet.getPrintSetup().setLandscape(true);
        Footer footer = sheet.getFooter();
        footer.setRight("Page " + HeaderFooter.page() + " of " + HeaderFooter.numPages());

        rowIndex = 0;
        sheetIndex++;

        if (!headerRows.isEmpty()) {
            for (ExcelData[] headerRow : headerRows) {
                createRow(headerRow, rowIndex++);
            }
        }
    }

    public void registerHeaderRow(ExcelData[] headerRow) {
        headerRows.add(headerRow);
    }

    public void addRow(ExcelData[] fields) {
        if (rowIndex >= MAX_ROWS_PER_SHEET) {
            createNewSheet();
        }
        createRow(fields, rowIndex++);
    }

    private void createRow(ExcelData[] data, int rowIndex) {
        HSSFRow row = sheet.createRow(rowIndex);

        for (int i = 0; i < data.length; i++) {

            HSSFCell cell = row.createCell(i);
            if (!autoSized) {
                sheet.setColumnWidth(i, 20 * 255);
            }

            if (data[i] != null && data[i].getValue() != null) {
                switch (data[i].getDataType()) {
                    case ExcelData.STRING -> {
                        if (data[i].getValue().toString().length() > 32767) {
                            data[i].setValue(data[i].getValue().toString().substring(0, 32766));
                        }
                        cell.setCellValue(data[i].getValue().toString());
                        cell.setCellStyle(styles.get("normal"));
                    }
                    case ExcelData.INTEGER -> {
                        try {
                            cell.setCellValue((Integer) data[i].getValue());
                        } catch (Exception e) {
                            cell.setCellValue("" + data[i].getValue());
                        }
                        cell.setCellStyle(styles.get("number"));
                    }
                    case ExcelData.DOUBLE -> {
                        try {
                            cell.setCellValue((Double) data[i].getValue());
                        } catch (Exception e) {
                            cell.setCellValue("" + data[i].getValue());
                        }
                        cell.setCellStyle(styles.get("normal"));
                    }
                    case ExcelData.NUMBER_FORMAT_0_00 -> {
                        try {
                            cell.setCellValue((Double) data[i].getValue());
                        } catch (Exception e) {
                            cell.setCellValue("" + data[i].getValue());
                        }
                        cell.setCellStyle(styles.get("money"));
                    }
                    case ExcelData.NUMBER_FORMAT_PERCENTAGE -> {
                        try {
                            cell.setCellValue((Double) data[i].getValue());
                        } catch (Exception e) {
                            cell.setCellValue("" + data[i].getValue());
                        }
                        cell.setCellStyle(styles.get("percent"));
                    }
                    case ExcelData.BIG_DECIMAL -> {
                        try {
                            cell.setCellValue(Double.valueOf(String.valueOf(data[i].getValue())));
                        } catch (Exception e) {
                            cell.setCellValue("" + data[i].getValue());
                        }
                        cell.setCellStyle(styles.get("money"));
                    }
                    case ExcelData.BOOLEAN -> {
                        try {
                            cell.setCellValue((Boolean) data[i].getValue());
                        } catch (Exception e) {
                            cell.setCellValue("" + data[i].getValue());
                        }
                        cell.setCellStyle(styles.get("normal"));
                    }
                    case ExcelData.TIME_FORMAT_Short_time_HH_mm -> {
                        try {
                            setTimeWithFormat(data[i].getValue().toString(), cell);
                        } catch (Exception e) {
                            cell.setCellValue("" + data[i].getValue());
                            cell.setCellStyle(styles.get("time"));
                        }
                    }
                    case ExcelData.DATE -> {
                        Date date;
                        try {
                            String dtFormat = "dd/MM/yyyy";
                            if (data[i].getCustomDateFormat() != null && !data[i].getCustomDateFormat().isEmpty()) {
                                dtFormat = data[i].getCustomDateFormat();
                            }
                            date = shortDateFormatter.parse(data[i].getValue().toString());
                            String reportShortDateFormat = new SimpleDateFormat(dtFormat).format(date);
                            Date convertToDate = new SimpleDateFormat(dtFormat).parse(reportShortDateFormat);

                            cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(dtFormat));

                            cell.setCellValue(convertToDate);
                            cell.setCellStyle(cellStyle);
                        } catch (ParseException e2) {
                            try {
                                date = new Date(Date.parse(data[i].getValue().toString()));
                                cell.setCellValue(date);
                                cell.setCellStyle(styles.get("shortdate"));
                            } catch (Exception e) {
                                cell.setCellValue(data[i].getValue().toString());
                                cell.setCellStyle(styles.get("shortdate"));
                            }
                        }
                    }
                    case ExcelData.DATE_LONG -> {
                        try {
                            String dtFormat = "dd/MM/yyyy HH:mm";
                            if (data[i].getCustomDateFormat() != null && !data[i].getCustomDateFormat().isEmpty()) {
                                dtFormat = data[i].getCustomDateFormat();
                            }

                            Date datelong = longDateFormatter.parse(data[i].getValue().toString());

                            String reportLongDateFormat = "";
                            if (userTimeZone != null) {
                                reportLongDateFormat = new SimpleDateFormat(dtFormat).format(ServerUtils.convertServerDateToUserDate(datelong, userTimeZone));
                            } else {
                                reportLongDateFormat = new SimpleDateFormat(dtFormat).format(datelong);
                            }
                            Date convertToDate = new SimpleDateFormat(dtFormat).parse(reportLongDateFormat);

                            cellStyle.setDataFormat(createHelper.createDataFormat().getFormat(dtFormat));

                            cell.setCellValue(convertToDate);
                            cell.setCellStyle(cellStyle);
                        } catch (ParseException e) {
                            try {
                                Date datelong = new Date(Date.parse(data[i].getValue().toString()));
                                cell.setCellValue(datelong);
                                cell.setCellStyle(styles.get("longdate"));
                            } catch (Exception e1) {
                                cell.setCellValue(data[i].getValue().toString());
                                cell.setCellStyle(styles.get("longdate"));
                            }
                        }
                    }
                }
                if (data[i].getDataPosiontionInPage() == ExcelData.HEADER) {
                    sheet.setColumnWidth(i, data[i].getCellSize());
                    switch (data[i].getDataType()) {
                        case ExcelData.INTEGER, ExcelData.NUMBER_FORMAT_0_00, ExcelData.BIG_DECIMAL, ExcelData.DOUBLE, ExcelData.CURRENCY, ExcelData.NUMBER_FORMAT_PERCENTAGE -> {
                            cell.setCellStyle(styles.get("headerRight"));
                        }
                        case ExcelData.TIME_FORMAT_Short_time_HH_mm -> {
                            cell.setCellStyle(styles.get("headerCenter"));
                        }
                        default -> {
                            cell.setCellStyle(styles.get("headerLeft"));
                        }
                    }

                } else if (data[i].getDataPosiontionInPage() == ExcelData.HEADER3) {
                    if (data[i].getFontSize() > 0) {
                        cell.setCellStyle(styles.get("TITLE"));
                    } else {
                        cell.setCellStyle(styles.get("title"));
                    }
                }

                if (data[i].isMerged()) {
                    data[i].addMerging(sheet,
                            row.getRowNum() + data[i].getFromRow(),
                            data[i].getFromCell(),
                            row.getRowNum() + data[i].getToRow(),
                            data[i].getToCell());
                }
            }
            if (data[i] != null && data[i].getBgcolor() != 0) {
                HSSFCellStyle customCellStyle = styles.get("customCellStyle");
                customCellStyle.cloneStyleFrom(cell.getCellStyle());
                customCellStyle.setFillForegroundColor(data[i].getBgcolor());
                cell.setCellStyle(customCellStyle);
            }
        }

    }

    private void createDefaultStyles() {
        styles = new HashMap<>();
        HSSFCellStyle cellStyle = wb.createCellStyle();

//        cellStyle.setFillForegroundColor(HSSFColor.WHITE.index);
//        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        HSSFFont headerFont = wb.createFont();
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cellStyle.setFont(headerFont);
        styles.put("headerLeft", cellStyle);

        cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        headerFont = wb.createFont();
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cellStyle.setFont(headerFont);
        styles.put("headerCenter", cellStyle);

        cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        headerFont = wb.createFont();
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cellStyle.setFont(headerFont);
        styles.put("headerRight", cellStyle);

        cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        headerFont = wb.createFont();
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        cellStyle.setFont(headerFont);
        styles.put("title", cellStyle);

        cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        headerFont = wb.createFont();
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerFont.setFontHeightInPoints((short) 12);
        cellStyle.setFont(headerFont);
        styles.put("TITLE", cellStyle);

        cellStyle = wb.createCellStyle();
        styles.put("normal", cellStyle);

        cellStyle = wb.createCellStyle();
        cellStyle.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat("#,##0_ ;[Red]-#,##0\\ "));
        cellStyle.setWrapText(false);
        cellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        styles.put("number", cellStyle);

        cellStyle = wb.createCellStyle();
        cellStyle.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat("#,##0.00\\ _ ;[Red](#,##0.00)\\ _ "));
        cellStyle.setWrapText(false);
        cellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        styles.put("money", cellStyle);

        cellStyle = wb.createCellStyle();
        cellStyle.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat("dd/mm/yyyy hh:mm"));
        cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        styles.put("longdate", cellStyle);

        cellStyle = wb.createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat("m/d/yy"));
        cellStyle.setAlignment(CellStyle.ALIGN_LEFT);
        styles.put("shortdate", cellStyle);

        cellStyle = wb.createCellStyle();
        cellStyle.setWrapText(true);
        cellStyle.setDataFormat(wb.createDataFormat().getFormat("[h]:mm"));
        cellStyle.setAlignment(CellStyle.ALIGN_CENTER);
        styles.put("time", cellStyle);

        cellStyle = wb.createCellStyle();
        cellStyle.setDataFormat(wb.getCreationHelper().createDataFormat().getFormat("0.00%"));
        cellStyle.setAlignment(CellStyle.ALIGN_RIGHT);
        styles.put("percent", cellStyle);

        HSSFCellStyle customCellStyle = wb.createCellStyle();
        customCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        styles.put("customCellStyle", cellStyle);

    }

    private void setTimeWithFormat(String time, HSSFCell cell) {//
        if (time != null && !time.isEmpty()) {
            try {
                String[] tString = time.split(":");
                double timeDouble = Double.valueOf(tString[0]) + ((1d / 60d) * Double.valueOf(tString[1]));
                cell.setCellValue(timeDouble / 24d);
            } catch (Exception e) {
                cell.setCellValue(time);
            }
        } else {
            cell.setCellValue(time);
        }
        cell.setCellStyle(styles.get("time"));
    }

}
