package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.myaccount.client.rpc.MyAccountService;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanItem;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Rinat
 * Date: 15.09.2011
 * Time: 23:15:16
 * To change this template use File | Settings | File Templates.
 */

public class UsageHistoryListExcelHandler extends BaseExcelHandler {
    private static Logger log = LoggerFactory.getLogger(UsageHistoryListExcelHandler.class.getName());
    @Autowired
    MyAccountService myaccountService;
    @Autowired
    UserManager userManager;

    @Override
    protected void setFileName() {
        filename = "Usage History";
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        String shortDateFormat = "MM/dd/yyyy";
        EdsCompanySettings companySettings = userManager.getUser().getCompany().getCompanySettings();
        if (companySettings != null) {
            shortDateFormat = companySettings.getShortDateFormat();
        }
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        ListResult<UsagePlanItem> usagePlanList = myaccountService.getUsagePlans(filterParametrs);
        Map<String, ExcelData> mapColumnHeader = new HashMap<>();
        ExcelData[] cellDatas;

        try {
            List<ExcelData[]> list = new LinkedList<>();
            mapColumnHeader.put(UsagePlanItem.MODULES, new ExcelData("Apps", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(UsagePlanItem.START_DATE, new ExcelData("Start Date", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(UsagePlanItem.END_DATE, new ExcelData("End Date", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(UsagePlanItem.STATUS, new ExcelData("Status", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(UsagePlanItem.USERS, new ExcelData("Full Users", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(UsagePlanItem.ESS_USERS, new ExcelData("ESS Users", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(UsagePlanItem.NOACCESS_USERS, new ExcelData("Non Users", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

            List<ExcelData> excelDataList = new ArrayList<>();
            for (String columnName : panelTools.getColumnCodeName()) {
                if (mapColumnHeader.containsKey(columnName)) {
                    excelDataList.add(mapColumnHeader.get(columnName));
                }
            }
            cellDatas = new ExcelData[excelDataList.size()];
            excelDataList.toArray(cellDatas);
            list.add(cellDatas);

            for (UsagePlanItem item : usagePlanList.getList()) {
                Map<String, ExcelData> mapColumns = new HashMap<>();

                /*if (panelTools.getColumnCodeName().contains(UsagePlanItem.NAME)) {
                    mapColumns.put(UsagePlanItem.NAME, new ExcelData(item.getCompName() != null ? item.getCompName() : "N/A", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }*/
                if (panelTools.getColumnCodeName().contains(UsagePlanItem.MODULES)) {
                    final StringBuilder columnValue = new StringBuilder();
                    if (item.isAccountsModule()) {
                        columnValue.append("Accounts");
                    }
                    if (item.isSalesModule()) {
                        if (columnValue.length() > 0) {
                            columnValue.append(", ");
                        }
                        columnValue.append("Sales");
                    }
                    if (item.isHumansModule()) {
                        if (columnValue.length() > 0) {
                            columnValue.append(", ");
                        }
                        columnValue.append("Humans");
                    }
                    if (item.isProjectModule()) {
                        if (columnValue.length() > 0) {
                            columnValue.append(", ");
                        }
                        columnValue.append("Projects");
                    }

                    if (item.isPayrollModule()) {
                        if (columnValue.length() > 0) {
                            columnValue.append(", ");
                        }
                        columnValue.append("Payroll");
                    }
                    mapColumns.put(UsagePlanItem.MODULES, new ExcelData(columnValue.toString(), ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(UsagePlanItem.START_DATE)) {
                    mapColumns.put(UsagePlanItem.START_DATE, new ExcelData(item.getStartDate() != null ? dateFormat(item.getStartDate()) : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(UsagePlanItem.END_DATE)) {
                    mapColumns.put(UsagePlanItem.END_DATE, new ExcelData(item.getEndDate() != null ? dateFormat(item.getEndDate()) : "", ExcelData.STRING, 35, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(UsagePlanItem.STATUS)) {
                    StringBuilder status = new StringBuilder(item.getStatus() != null ? item.getStatus() : "");
                    status.append(" ("+item.getPeriodType()+")");
                    mapColumns.put(UsagePlanItem.STATUS, new ExcelData(status.toString(), ExcelData.STRING, 35, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(UsagePlanItem.USERS)) {
                    mapColumns.put(UsagePlanItem.USERS, new ExcelData(item.getUserCount() != null ? String.valueOf(item.getUserCount()) : "0", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(UsagePlanItem.ESS_USERS)) {
                    mapColumns.put(UsagePlanItem.ESS_USERS, new ExcelData(item.getEssUserCount() != null ? String.valueOf(item.getEssUserCount()) : "0", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(UsagePlanItem.NOACCESS_USERS)) {
                    mapColumns.put(UsagePlanItem.NOACCESS_USERS, new ExcelData(item.getNonAccessUserCount() != null ? String.valueOf(item.getNonAccessUserCount()) : "0", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
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
            WorkBook workBook = new WorkBook(list, true, 0, 1, 0, 1);
            return workBook.getWorkBook(filename, 0, 0, 0, 6);
        } catch (Exception e) {
            log.error("Cannot generate Usage Plans list excel report, exception: ", e);
        }
        return null;
    }
}