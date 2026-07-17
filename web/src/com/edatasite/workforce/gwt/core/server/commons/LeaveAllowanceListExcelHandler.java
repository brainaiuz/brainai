package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.leaveRequest.LaborPeriodRequest;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LeaveAllowanceListExcelHandler extends BaseExcelHandler implements Constants {

    private static final Logger log = LoggerFactory.getLogger(LeaveAllowanceListExcelHandler.class);
    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private HrmsService hrmsService;
    @Autowired
    private PropertManager propertManager;
    @Autowired
    private GenericSettingsManager genericSettings;
    private String sheetName;
    public boolean isLeaveByPeriodEnabled() {
        return genericSettings.isSettingsEnabled(GenericSettingsEnum.LEAVE_BY_PERIOD_OPTION);
    }

    @Override
    protected void setFileName() {
        this.filename = "Leave_Allowances";
    }

    private ListPanelToolRpc getpanelToolColumnCodes() {
        ListPanelToolRpc panelTools = new ListPanelToolRpc();
        ArrayList<String> columnCodes = panelTools.getColumnCodeName();
        if (columnCodes == null) {
            columnCodes = new ArrayList<>();
        }
        columnCodes.add(EmployeeListItem.EMPLOYEE_NUMBER);
        columnCodes.add(EmployeeListItem.EMPLOYEE_NAME);
        columnCodes.add(EmployeeListItem.DEPARTMENT);
        columnCodes.add(LaborPeriodRequest.LABOR_PERIOD);
        columnCodes.add(LaborPeriodRequest.ALLOWANCE);
        if (isLeaveByPeriodEnabled()) {
            columnCodes.add(LaborPeriodRequest.TAKEN);
            columnCodes.add(LaborPeriodRequest.ADJUSTED);
            columnCodes.add(LaborPeriodRequest.LEFT_DAYS);
        }
        panelTools.setColumnCodeName(columnCodes);
        return panelTools;
    }

    protected HSSFWorkbook getWorkBook(Object ob) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) ob;
        EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
        sheetName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.leaveAllowance);
        filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        EdsUser user = userManager.getUser();
        ListResult<EmployeeListItem> employeeListItemListResult = loadList(filterParametrs);
        ListPanelToolRpc panelTools = getpanelToolColumnCodes();
        ExcelData[] cellDatas;
        EdsCompany edsCompany = userManager.getUser().getCompany();
        Map<String, ExcelData> mapColumnHeader = new HashMap<>();

        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);
            List<ExcelData[]> list = new LinkedList<>();

            mapColumnHeader.put(EmployeeListItem.EMPLOYEE_NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.employeeCode), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(EmployeeListItem.EMPLOYEE_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.fullName), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(EmployeeListItem.DEPARTMENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.department), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(LaborPeriodRequest.LABOR_PERIOD, new ExcelData(commonLocalizer.localize(PdfLocalizationName.period), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(LaborPeriodRequest.ALLOWANCE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.allowance), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            if (isLeaveByPeriodEnabled()) {
                mapColumnHeader.put(LaborPeriodRequest.TAKEN, new ExcelData(commonLocalizer.localize(PdfLocalizationName.takenDays), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
                mapColumnHeader.put(LaborPeriodRequest.ADJUSTED, new ExcelData(commonLocalizer.localize(PdfLocalizationName.adjusted), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
                mapColumnHeader.put(LaborPeriodRequest.LEFT_DAYS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.leftLeaveDays), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            }
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), edsCompany.getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), sheetName, workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " Xolatiga ko'ra" : excelReferenceMessageSource.localize("EPAsOf", " As Of") + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            List<ExcelData> excelDataList = new ArrayList<>();
            for (String columnName : panelTools.getColumnCodeName()) {
                if (mapColumnHeader.containsKey(columnName)) {
                    excelDataList.add(mapColumnHeader.get(columnName));
                }
            }
            cellDatas = new ExcelData[excelDataList.size()];
            excelDataList.toArray(cellDatas);
            list.add(cellDatas);

            for (EmployeeListItem item : employeeListItemListResult.getList()) {

                ArrayList<LaborPeriodRequest> periodList = isLeaveByPeriodEnabled() ? hrmsService.getPeriodList(item.getObjectID()) : hrmsService.getLeaveAllowances(item.getObjectID());

                for (LaborPeriodRequest period : periodList) {
                    Map<String, ExcelData> mapColumns = new HashMap<>();

                    if (panelTools.getColumnCodeName().contains(EmployeeListItem.EMPLOYEE_NUMBER)) {
                        mapColumns.put(EmployeeListItem.EMPLOYEE_NUMBER, new ExcelData(item.getEmployeeNumber(), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }

                    if (panelTools.getColumnCodeName().contains(EmployeeListItem.EMPLOYEE_NAME)) {
                        mapColumns.put(EmployeeListItem.EMPLOYEE_NAME, new ExcelData(item.getFullName(), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }

                    if (panelTools.getColumnCodeName().contains(EmployeeListItem.DEPARTMENT)) {
                        mapColumns.put(EmployeeListItem.DEPARTMENT, new ExcelData(item.getDepartment(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }

                    if (panelTools.getColumnCodeName().contains(LaborPeriodRequest.LABOR_PERIOD)) {
                        mapColumns.put(LaborPeriodRequest.LABOR_PERIOD, new ExcelData(period.getLaborPeriod(), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }

                    if (panelTools.getColumnCodeName().contains(LaborPeriodRequest.ALLOWANCE)) {
                        mapColumns.put(LaborPeriodRequest.ALLOWANCE, new ExcelData(period.getAllowance(), ExcelData.DOUBLE, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }

                    if (isLeaveByPeriodEnabled()) {
                        if (panelTools.getColumnCodeName().contains(LaborPeriodRequest.TAKEN)) {
                            mapColumns.put(LaborPeriodRequest.TAKEN, new ExcelData(period.getApprovedTakenDays(), ExcelData.DOUBLE, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                        }

                        if (panelTools.getColumnCodeName().contains(LaborPeriodRequest.ADJUSTED)) {
                            mapColumns.put(LaborPeriodRequest.ADJUSTED, new ExcelData(period.getOutOfSystemDays(), ExcelData.DOUBLE, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                        }
                        if (panelTools.getColumnCodeName().contains(LaborPeriodRequest.LEFT_DAYS)) {
                            mapColumns.put(LaborPeriodRequest.LEFT_DAYS, new ExcelData((period.getAllowance() - (period.getApprovedTakenDays() + period.getOutOfSystemDays())), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                        }
                    }
                    excelDataList = new ArrayList<>();
                    for (String columnName : panelTools.getColumnCodeName()) {
                        if (mapColumns.containsKey(columnName)) {
                            excelDataList.add(mapColumns.get(columnName));
                        }
                    }
                    cellDatas = new ExcelData[excelDataList.size()];
                    excelDataList.toArray(cellDatas);
                    list.add(cellDatas);
                }
            }
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, mapColumnHeader.size());

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate " + "Annual Allowance excel report, exception: " + e);
        }
        return null;
    }

    private ListResult<EmployeeListItem> loadList(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        fp.setAllEmployees(true);
        fp.setBriefly(true);
        return employeeService.getEmployeeList(fp);
    }
}
