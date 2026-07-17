package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.LeaveBalanceReport;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AnnualBalanceReportExcelHandler extends BaseExcelHandler implements Constants {
    private static final Logger log = LoggerFactory.getLogger(AnnualBalanceReportExcelHandler.class);
    private Integer year = Calendar.getInstance().get(Calendar.YEAR);

    @Autowired
    private AvailabilityService availabilityService;

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        filterParametrs.setLimit(LIMIT_EXCEL_ROW);

        year = filterParametrs.getYear();

        EdsUser user = userManager.getUser();
        filterParametrs.setDate(filterParametrs.getDueDate());
        ListResult<LeaveBalanceReport> reportDataList = availabilityService.getEmployeeLeaveBalanceReport(filterParametrs);

        ExcelData[] cellDatas;
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        Map<String, String> mapColumnHeader = new HashMap<>();

        mapColumnHeader.put(LeaveBalanceReport.EMPLOYEE_NUMBER, commonLocalizer.localize(PdfLocalizationName.number));
        mapColumnHeader.put(LeaveBalanceReport.EMPLOYEE_NAME, commonLocalizer.localize(PdfLocalizationName.employee));
        mapColumnHeader.put(LeaveBalanceReport.HIRE_DATE, commonLocalizer.localize(PdfLocalizationName.hireDateField));
        mapColumnHeader.put(LeaveBalanceReport.RESIGN_DATE, commonLocalizer.localize(PdfLocalizationName.resignationDate));
        mapColumnHeader.put(LeaveBalanceReport.STATUS, commonLocalizer.localize(PdfLocalizationName.status));
        mapColumnHeader.put(LeaveBalanceReport.DEPARTMENT, commonLocalizer.localize(PdfLocalizationName.department));
        mapColumnHeader.put(LeaveBalanceReport.LEAVE_ALLOWANCE_DAYS, commonLocalizer.localize(PdfLocalizationName.leaveAllowanceDays));
        mapColumnHeader.put(LeaveBalanceReport.TAKEN_DAYS, commonLocalizer.localize(PdfLocalizationName.takenDays));
        mapColumnHeader.put(LeaveBalanceReport.CURRENT_BALANCE, commonLocalizer.localize(PdfLocalizationName.balance));
        mapColumnHeader.put(LeaveBalanceReport.OPENING_BALANCE, commonLocalizer.localize(PdfLocalizationName.openingBalanceDays));


        try {
            List<ExcelData[]> list = new LinkedList<>();
            cellDatas = new ExcelData[header.size()];

            for (int i = 0; i < header.size(); i++) {
                cellDatas[i] = new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, 18, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }

            list.add(cellDatas);

            for (LeaveBalanceReport item : reportDataList.getList()) {
                List<String> cell = new ArrayList<>();
                for (int ii = 0; ii < header.size(); ii++) {
                    if (LeaveBalanceReport.EMPLOYEE_NUMBER.equals(header.get(ii))) {
                        cell.add(header.indexOf(LeaveBalanceReport.EMPLOYEE_NUMBER), (item.getEmployeeNumber() != null ? item.getEmployeeNumber() : commonLocalizer.localize(PdfLocalizationName.na)));
                    }
                    if (LeaveBalanceReport.EMPLOYEE_NAME.equals(header.get(ii))) {
                        cell.add(header.indexOf(LeaveBalanceReport.EMPLOYEE_NAME), (item.getEmployeeName() != null ? item.getEmployeeName() : commonLocalizer.localize(PdfLocalizationName.na)));
                    }

                    if (LeaveBalanceReport.HIRE_DATE.equals(header.get(ii))) {
                        cell.add(header.indexOf(LeaveBalanceReport.HIRE_DATE), item.getHireDate() != null ? ServerUtils.shortDateFormat(item.getHireDate().getNonConvertedDate(), user) : "");

                    }
                    if (LeaveBalanceReport.RESIGN_DATE.equals(header.get(ii))) {
                        cell.add(header.indexOf(LeaveBalanceReport.RESIGN_DATE), item.getResignDate() != null ? ServerUtils.shortDateFormat(item.getResignDate().getNonConvertedDate(), user) : "");

                    }
                    if (LeaveBalanceReport.STATUS.equals(header.get(ii))) {
                        cell.add(header.indexOf(LeaveBalanceReport.STATUS), item.getStatus());

                    }
                    if (LeaveBalanceReport.DEPARTMENT.equals(header.get(ii))) {
                        cell.add(header.indexOf(LeaveBalanceReport.DEPARTMENT), item.getDepartment());

                    }
                    if (LeaveBalanceReport.LEAVE_ALLOWANCE_DAYS.equals(header.get(ii))) {
                        cell.add(header.indexOf(LeaveBalanceReport.LEAVE_ALLOWANCE_DAYS), getMoneyFormat(item.getLeaveAllowanceDays()));

                    }
                    if (LeaveBalanceReport.TAKEN_DAYS.equals(header.get(ii))) {
                        cell.add(header.indexOf(LeaveBalanceReport.TAKEN_DAYS), getMoneyFormat(item.getTakenDays()));

                    }
                    if (LeaveBalanceReport.CURRENT_BALANCE.equals(header.get(ii))) {
                        cell.add(header.indexOf(LeaveBalanceReport.CURRENT_BALANCE), getMoneyFormat(item.getCurrentBalance()));
                    }
                    if (LeaveBalanceReport.OPENING_BALANCE.equals(header.get(ii))) {
                        cell.add(header.indexOf(LeaveBalanceReport.OPENING_BALANCE), item.getOpeningBalance() != null ? item.getOpeningBalance().toString() : " ");
                    }
                }
                cellDatas = new ExcelData[header.size()];
                for (int k = 0; k < header.size(); k++) {
                    String columnCode = header.get(k);
                    int dataType = ExcelData.STRING;
                    cellDatas[k] = new ExcelData(cell.get(k), dataType, 18, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                list.add(cellDatas);
            }

            return new WorkBook(list).getWorkBook(filename, 0, 0, 0, 6);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate Annual Allowance Balance Report list excel report, exception: " + e);
        }
        return null;
    }

    @Override
    protected void setFileName() {
        filename = "Annual_Balance_Report_List_" + year;
    }
}
