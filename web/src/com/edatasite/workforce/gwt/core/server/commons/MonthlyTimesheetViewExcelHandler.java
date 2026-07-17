package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetService;
import com.edatasite.workforce.gwt.timesheet.client.ui.view.MonthlyTimesheetItem;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Dilshod Madrahimov on 8/20/15 3:27 PM
 */
public class MonthlyTimesheetViewExcelHandler extends BaseExcelHandler {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private static final Logger log = LoggerFactory.getLogger(MonthlyTimesheetViewExcelHandler.class);
    private final String datePattern = "MMM, yyyy";

    @Autowired
    private TimesheetService timesheetService;

    @Override
    protected void setFileName() {
        filename = "Monthly Timesheet List";
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter fp = (ListingFilterParameter) object;
//        fp.setLandscape(Boolean.parseBoolean(request.getParameter("IS_LANDSCAPE")));
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        EdsUser user = userManager.getUser();

        Date startDate = null;
        try {
            startDate = dateFormat.parse(fp.getStartDateNC());
        } catch (ParseException e) {
            log.error("Generate " + filename + " excel date format parse exception: " + e);
            e.printStackTrace();
        }
        List<MonthlyTimesheetItem> timesheetItems = timesheetService.getMonthlyTimesheetData(new DateNonConvertable(startDate), fp.getProjectId(), fp.getEmployeeId(), fp.isShowYTD());

        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);

            ExcelData[] headers = new ExcelData[7];
            headers[0] = new ExcelData(commonLocalizer.localize(PdfLocalizationName.employeeCode), ExcelData.STRING, 40, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            headers[1] = new ExcelData(commonLocalizer.localize(PdfLocalizationName.employee), ExcelData.STRING, 40, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            headers[2] = new ExcelData(commonLocalizer.localize(PdfLocalizationName.workedHours), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            headers[3] = new ExcelData(commonLocalizer.localize(PdfLocalizationName.totalWorkedDays), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            headers[4] = new ExcelData(commonLocalizer.localize(PdfLocalizationName.overtimeHours), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            headers[5] = new ExcelData(commonLocalizer.localize(PdfLocalizationName.weekendOvertimeHours), ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            headers[6] = new ExcelData(commonLocalizer.localize(PdfLocalizationName.holidayOvertimeHours), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);

            List<ExcelData[]> list = new LinkedList<>();
            list.add(generateOneRowWithValue(headers.length, timesheetItems.size() > 0 ? timesheetItems.get(0).getProjectName() : "", workBook.getSheet(), 0));
//            list.add(generateOneRowWithValue(headers.length, ServerUtils.dateFormat(startDate, datePattern), workBook.getSheet(), 1));
            if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                list.add(generateOneRowWithValue(headers.length, ServerUtils.convertToUzbDateFormat(ServerUtils.dateFormat(startDate, datePattern)), workBook.getSheet(), 1));
            } else {
                list.add(generateOneRowWithValue(headers.length, ServerUtils.dateFormat(startDate, datePattern), workBook.getSheet(), 1));
            }

            list.add(headers);

            ExcelData[] dataList = null;
            for (MonthlyTimesheetItem item : timesheetItems) {
                dataList = new ExcelData[7];
                Integer indexOffString = item.getEmployeeName().indexOf(" - ");
                String employeeCode = "";
                String employeeName = item.getEmployeeName();
                if (indexOffString > 0 && item.getEmployeeName().length() > 0) {
                    employeeCode = item.getEmployeeName().substring(0, indexOffString);
                    employeeName = "";
                    if (indexOffString + 3 < item.getEmployeeName().length() - 1) {
                        employeeName = item.getEmployeeName().substring(indexOffString + 3, item.getEmployeeName().length() - 1);
                    }
                }
                dataList[0] = new ExcelData(employeeCode, ExcelData.STRING, 40, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                dataList[1] = new ExcelData(employeeName, ExcelData.STRING, 40, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                dataList[2] = new ExcelData(item.getWorkedHours() != null ? item.getWorkedHours() : 0, ExcelData.DOUBLE, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                dataList[3] = new ExcelData(item.getTotalWorkedDays() != null ? item.getTotalWorkedDays() : 0, ExcelData.DOUBLE, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                dataList[4] = new ExcelData(item.getOvertimeHours() != null ? item.getOvertimeHours() : 0, ExcelData.DOUBLE, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                dataList[5] = new ExcelData(item.getWeekendOvertimeHours() != null ? item.getWeekendOvertimeHours() : 0, ExcelData.DOUBLE, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                dataList[6] = new ExcelData(item.getHolidayOvertimeHours() != null ? item.getHolidayOvertimeHours() : 0, ExcelData.DOUBLE, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);

                list.add(dataList);
            }
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, headers.length);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Error occurred while generating Monthly Timesheet List Excel");
        }
        return null;
    }


}
