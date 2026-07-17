package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 23.09.2009
 * Time: 18:59:25
 * To change this template use File | Settings | File Templates.
 */
public class AttendanceReportImportExcelHandler extends ExcelHandler {

    @Autowired
    private EmployeeManager employeeManager;

    protected HSSFWorkbook getWorkBook(HttpServletRequest request) {
        HSSFWorkbook workBook;
        HSSFSheet sheet;
        String comapnyName;
        String month;
        String year;
        int maxMonthDay;
        String teamIds = null;
        Integer locationID = null;
        Integer projectID = null;
        year = request.getParameter("year");
        month = request.getParameter("month");
        teamIds = (request.getParameter("teamIds") != null && !"null".equals(request.getParameter("teamID"))) ? request.getParameter("teamID") : null;
        locationID = (request.getParameter("locationID") != null && !"null".equals(request.getParameter("locationID"))) ? Integer.parseInt(request.getParameter("locationID")) : null;
        projectID = (request.getParameter("projectID") != null && !"null".equals(request.getParameter("projectID"))) ? Integer.parseInt(request.getParameter("projectID")) : null;

        if (year != null && month != null && !year.equals("") && !month.equals("")) {
            maxMonthDay = getDaysInMonth(Integer.parseInt(year) + 1900, Integer.parseInt(month) - 1);
        } else {
            Date date = new Date();
            year = String.valueOf(date.getYear());
            month = String.valueOf(date.getMonth());
            maxMonthDay = getDaysInMonth(Integer.parseInt(year) + 1900, Integer.parseInt(month) - 1);
        }

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setViewAsId(EdsRole.ADMIN);
        fp.setDepartmentIds(teamIds);
        fp.setLocationId(locationID);
        fp.setProjectId(projectID);
        Date date = new Date();
        date.setYear(Integer.parseInt(request.getParameter("year")));
        date.setMonth(Integer.parseInt(request.getParameter("month")) - 1);

        fp.setStartDate(ServerUtils.getMonthStartDate(date));
        fp.setEndDate(ServerUtils.getMonthEndDate(date));
        fp.setThisMonthEmployees(true);
        comapnyName = employeeManager.getUser().getCompany().getName();
        List<EdsEmployee> epmlooyess = employeeManager.list(fp);
        List<ExcelData[]> list = getAttendanceReportTitle(maxMonthDay);
        try {
//            int empID = 1;
            for (EdsEmployee employee : epmlooyess) {
                ExcelData[] cellDatas = new ExcelData[2 + (maxMonthDay * 2)];

                cellDatas[0] = new ExcelData(employee.getProfile() != null ? String.valueOf(employee.getProfile().getEmployeeCode()) : "", ExcelData.STRING, 10, true, true, ExcelData.NO_BORDER, ExcelData.HEADER3);
                cellDatas[0].setStyle(true);
                cellDatas[0].setFontSize((short) 10);
                cellDatas[0].setFontColor(HSSFColor.GREEN.index);
                cellDatas[0].setBgcolor(HSSFColor.YELLOW.index);

                cellDatas[1] = new ExcelData(employee.getName(), ExcelData.STRING, 35, true, true, ExcelData.NO_BORDER, ExcelData.HEADER);
                cellDatas[1].setStyle(true);
                cellDatas[1].setFontSize((short) 10);
                cellDatas[1].setFontColor(HSSFColor.GREEN.index);
                cellDatas[1].setBgcolor(HSSFColor.YELLOW.index);

                for (int i = 2; i < 2 + (maxMonthDay * 2); i++) {
                    if (i % 2 == 0) {
                        cellDatas[i] = new ExcelData("00:00", ExcelData.STRING, 10, true, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        cellDatas[i].setStyle(true);
                        cellDatas[i].setFontSize((short) 8);
                        cellDatas[i].setFontColor(HSSFColor.BLACK.index);
                        cellDatas[i].setBgcolor(HSSFColor.BLUE.index);
                    } else {
                        cellDatas[i] = new ExcelData("00:00", ExcelData.STRING, 10, true, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                        cellDatas[i].setStyle(true);
                        cellDatas[i].setFontSize((short) 8);
                        cellDatas[i].setFontColor(HSSFColor.BLACK.index);
                        cellDatas[i].setBgcolor(HSSFColor.ORANGE.index);
                    }
                }
                list.add(cellDatas);
//                empID++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        workBook = new WorkBook(list, true, 5, 2).getWorkBook(filename, 0, 0, 0, list.size());
        sheet = workBook.getSheetAt(0);
        sheet.addMergedRegion(new Region(0, (short) 0, 2, (short) (1 + (maxMonthDay * 2))));

        HSSFCell cell = sheet.getRow(0).getCell((short) 0);
        cell.getCellStyle().setFillForegroundColor(HSSFColor.WHITE.index);
        cell.getCellStyle().setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        cell.getCellStyle().setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);

        cell.setCellValue("  " + comapnyName + " " + commonLocalizer.localize(PdfLocalizationName.employeeHoursReport));

        workBook.getFontAt(cell.getCellStyle().getFontIndex()).setFontHeightInPoints((short) 20);
        workBook.getFontAt(cell.getCellStyle().getFontIndex()).setColor(HSSFColor.GREEN.index);

        setCustomColors(workBook);
        mergedDateColumn(sheet, workBook, year, month, maxMonthDay);

        return workBook;
    }

    @Override
    public void setFileName(String name) {
        filename = "AttendanceReportImport";
    }

    private List<ExcelData[]> getAttendanceReportTitle(int maxMonthDay) {
        List<ExcelData[]> list = new LinkedList<>();
        try {
            ExcelData[] cellDatas = new ExcelData[2 + (maxMonthDay * 2)];
            for (int i = 0; i < 2 + (maxMonthDay * 2); i++) {
                cellDatas[i] = new ExcelData("", ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER2);
            }

            list.add(cellDatas);
            list.add(cellDatas);
            list.add(cellDatas);

            cellDatas = new ExcelData[2 + (maxMonthDay * 2)];
            cellDatas[0] = new ExcelData("N#", ExcelData.STRING, 8, true, true, ExcelData.NO_BORDER, ExcelData.HEADER2);
            cellDatas[0].setStyle(true);
            cellDatas[0].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
            cellDatas[0].setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            cellDatas[0].setFontSize((short) 8);
            cellDatas[0].setBgcolor(HSSFColor.RED.index);
            cellDatas[0].setFontColor(HSSFColor.WHITE.index);

            cellDatas[1] = new ExcelData(commonLocalizer.localize(PdfLocalizationName.employee), ExcelData.STRING, 35, true, true, ExcelData.NO_BORDER, ExcelData.HEADER2);
            cellDatas[1].setStyle(true);
            cellDatas[1].setHorizontalAlignment(HSSFCellStyle.ALIGN_LEFT);
            cellDatas[1].setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
            cellDatas[1].setFontSize((short) 11);
            cellDatas[1].setBgcolor(HSSFColor.RED.index);
            cellDatas[1].setFontColor(HSSFColor.WHITE.index);

            for (int i = 2; i < 2 + (maxMonthDay * 2); i++) {
                cellDatas[i] = new ExcelData("", ExcelData.STRING, 10, true, true, ExcelData.NO_BORDER, ExcelData.HEADER2);
                cellDatas[i].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                cellDatas[i].setStyle(true);
                cellDatas[i].setFontSize(10);
                cellDatas[i].setBgcolor(HSSFColor.RED.index);
                cellDatas[i].setFontColor(HSSFColor.WHITE.index);
            }

            list.add(cellDatas);
            cellDatas = new ExcelData[2 + (maxMonthDay * 2)];

            cellDatas[0] = new ExcelData("", ExcelData.STRING, 8, true, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            cellDatas[0].setMerged(true);
            cellDatas[0].setFromRow(-1);
            cellDatas[0].setToRow(0);
            cellDatas[0].setFromCell(0);
            cellDatas[0].setToCell(1);

            cellDatas[1] = new ExcelData("", ExcelData.STRING, 35, true, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
            cellDatas[1].setMerged(true);
            cellDatas[1].setFromRow(-1);
            cellDatas[1].setToRow(0);
            cellDatas[1].setFromCell(1);
            cellDatas[1].setToCell(2);

            for (int i = 2; i < 2 + (maxMonthDay * 2); i++) {
                if (i % 2 == 0) {
                    cellDatas[i] = new ExcelData("Came", ExcelData.STRING, 10, true, true, ExcelData.NO_BORDER, ExcelData.HEADER);
                    cellDatas[i].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                    cellDatas[i].setStyle(true);
                    cellDatas[i].setFontSize((short) 8);
                    cellDatas[i].setFontColor(HSSFColor.GREEN.index);
                    cellDatas[i].setBgcolor(HSSFColor.YELLOW.index);

                } else {
                    cellDatas[i] = new ExcelData("Left", ExcelData.STRING, 10, true, true, ExcelData.NO_BORDER, ExcelData.HEADER);
                    cellDatas[i].setHorizontalAlignment(HSSFCellStyle.ALIGN_CENTER);
                    cellDatas[i].setStyle(true);
                    cellDatas[i].setFontSize((short) 8);
                    cellDatas[i].setFontColor(HSSFColor.GREEN.index);
                    cellDatas[i].setBgcolor(HSSFColor.YELLOW.index);
                }
            }
            list.add(cellDatas);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    private void setCustomColors(HSSFWorkbook workBook) {
        HSSFPalette palette = workBook.getCustomPalette();

        palette.setColorAtIndex(HSSFColor.RED.index, (byte) 79, (byte) 129, (byte) 189);// RED(79,129,189) - label and date background
        palette.setColorAtIndex(HSSFColor.YELLOW.index, (byte) 219, (byte) 229, (byte) 241);//RED(219,229,241) - employee Name and came,left background
        palette.setColorAtIndex(HSSFColor.GREEN.index, (byte) 23, (byte) 55, (byte) 93);//RED(23,55,93) - labels - color
        palette.setColorAtIndex(HSSFColor.BLUE.index, (byte) 252, (byte) 213, (byte) 180);//RED(252,213,180) - came backgroud color
        palette.setColorAtIndex(HSSFColor.ORANGE.index, (byte) 234, (byte) 241, (byte) 221);//RED(234,241,221) - left backgroud color

    }


    private void mergedDateColumn(HSSFSheet sheet, HSSFWorkbook workBook, String year, String month, int maxMonthDay) {
        sheet = workBook.getSheetAt(0);
        HSSFRow row = sheet.getRow(3);
        int k = 1;
        for (int i = 2; i < (2 + maxMonthDay * 2); i += 2) {
            try {
                Date date = new Date(Integer.parseInt(year), Integer.parseInt(month) - 1, k++, 0, 0, 0);
                sheet.addMergedRegion(new Region(3, (short) i, 3, (short) (i + 1)));
                HSSFCell cell = row.getCell((short) i);
                cell.setCellValue(new SimpleDateFormat("dd.MM.yyyy").format(date));//not changed this format, because, used date parsed
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*this method get in year and month  month numbers*/

    private int getDaysInMonth(int year, int month) {
        switch (month) {
            case 1 -> {
                if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0) {
                    return 29; // leap year
                } else {
                    return 28;
                }
            }
            case 3 -> {
                return 30;
            }
            case 5 -> {
                return 30;
            }
            case 8 -> {
                return 30;
            }
            case 10 -> {
                return 30;
            }
            default -> {
                return 31;
            }
        }
    }


}
