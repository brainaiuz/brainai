package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.CompanyListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: xushnud
 * Date: Apr 15, 2011
 * Time: 11:34:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class BackendViewListExcelHandler extends BaseExcelHandler {

    @Autowired
    private BackendService backendService;

    private static final Logger log = LoggerFactory.getLogger(BackendViewListExcelHandler.class);

    protected HSSFWorkbook getWorkBook(Object object) {

        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        EdsCompanySettings companySettings = userManager.getUser().getCompany().getCompanySettings();
        String formatStr = (companySettings != null && companySettings.getShortDateFormat() != null) ? companySettings.getShortDateFormat() : "dd-MMM-yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(formatStr);
        if (companySettings != null && companySettings.getExcelLimit() != null && !"".equals(companySettings.getExcelLimit())) {
            filterParametrs.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
        } else {
            filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        }
        filterParametrs.setFromExcelPDF(true);
        ListResult<CompanyListItem> companyList = backendService.getCompanyStatisticList(filterParametrs);
        List<CompanyListItem> companies = companyList.getList();
        ExcelData[] cellDatas;
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        header.remove(CompanyListItem.ACTION);
        Map<String, ExcelData> mapColumnHeader = new HashMap<>();

        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);

            List<ExcelData[]> list = new LinkedList<>();
            mapColumnHeader.put(CompanyListItem.COMPANY_ID, new ExcelData("Company ID", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.COMPANY_NAME, new ExcelData("Company Name", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.COMPANY_STATUS, new ExcelData("Status", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.REGISTRATION_DATE, new ExcelData("Registration Date", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.ACCESS_COUNT, new ExcelData("Access Count", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.FIRST_ACCESS_DATE, new ExcelData("First Access Date", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.LAST_ACCESS_DATE, new ExcelData("Last Access Date", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.SIGNED_UP_FROM, new ExcelData("Signed Up From", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.SUBSCRIPTION_TYPE, new ExcelData("Is Paid", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.PAYMENT_STATUS, new ExcelData("Payment Status", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.PERIOD_ACCESS, new ExcelData("Period Access", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.USER_COUNT, new ExcelData("User Count", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.ACTIVE_USERS_COUNT, new ExcelData("Active Users Count", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.PROJECT_COUNT, new ExcelData("Project Count", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.TASK_COUNT, new ExcelData("Task Count", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.TIMESHEET_COUNT, new ExcelData("TimeSheet Count", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.CLIENT_COUNT, new ExcelData("Client Count", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.SUPPLIER_COUNT, new ExcelData("Supplier Count", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.LEAD_COUNT, new ExcelData("Lead Count", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.CONTACT_COUNT, new ExcelData("Contact Count", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.CRM_TASK_COUNT, new ExcelData("CRM Task Count", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.EVENT_COUNT, new ExcelData("Event Count", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.CASE_COUNT, new ExcelData("Case Count", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.INVOICE_COUNT, new ExcelData("Invoice Count", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.EXPENSE_COUNT, new ExcelData("Expense Count", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.PRODUCT_COUNT, new ExcelData("Product Count", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.FOLDER_COUNT, new ExcelData("Folder Count", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.FILE_COUNT, new ExcelData("File Count", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.ADMIN_EMAIL, new ExcelData("Admin Email", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.COUNTRY, new ExcelData("Country", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.EXPIRATION_DATE, new ExcelData("Expiry Date", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.HOST_NAME, new ExcelData("Host Name", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.ADMIN_NAME, new ExcelData("Admin Name", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.AFFILIATE, new ExcelData("Affiliate", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.COMPAING, new ExcelData("Campaign", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.SOURCE, new ExcelData("Source", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.MEDIUM, new ExcelData("Medium", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.REDIRECTED, new ExcelData("Redirected", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.REFERRER, new ExcelData("Referrer", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.GCLID, new ExcelData("Gclid", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.NO_ACCESS_USER_COUNT_SUBS, new ExcelData("No access Users (Subscription)", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.NO_ACCESS_USER_COUNT, new ExcelData("No access Users (Actual)", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.ACTIVE_USERS_COUNT_SUBS, new ExcelData("Active Users (Subscription)", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.ESS_USERS_COUNT_SUBS, new ExcelData("ESS Users (Subscription)", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.ESS_USERS_COUNT_ACTUAL, new ExcelData("ESS Users (Actual)", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(CompanyListItem.ADMIN_PHONE, new ExcelData("Admin Phone", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

            List<ExcelData> excelDataList = new ArrayList<>();
            for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                if (mapColumnHeader.containsKey(panelTools.getColumnCodeName().get(i))) {
                    excelDataList.add(mapColumnHeader.get(panelTools.getColumnCodeName().get(i)));
                }
            }

            cellDatas = new ExcelData[excelDataList.size()];
            excelDataList.toArray(cellDatas);
            list.add(cellDatas);

            for (CompanyListItem company : companies) {
                Map<String, ExcelData> mapColumn = new HashMap<>();
                if (panelTools.getColumnCodeName().contains(CompanyListItem.COMPANY_ID)) {
                    String companyId = company.getCompanyID() != null ? company.getCompanyID().toString() : "";
                    mapColumn.put(CompanyListItem.COMPANY_ID, new ExcelData(companyId, ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.COMPANY_NAME)) {
                    mapColumn.put(CompanyListItem.COMPANY_NAME, new ExcelData(escapeHtml(company.getCompanyName()), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.COMPANY_STATUS)) {
                    String status = company.isActive() ? "Active" : "Inactive";
                    mapColumn.put(CompanyListItem.COMPANY_STATUS, new ExcelData(escapeHtml(status), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.REGISTRATION_DATE)) {
                    String registrationDate = company.getRegistrationDate() != null ? dateFormat.format(company.getRegistrationDate()) : "";
                    mapColumn.put(CompanyListItem.REGISTRATION_DATE, new ExcelData(registrationDate, ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.ACCESS_COUNT)) {
                    mapColumn.put(CompanyListItem.ACCESS_COUNT, new ExcelData(escapeHtml(company.getAccessCount()), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.FIRST_ACCESS_DATE)) {
                    String firstAccessDate = company.getFirstAccessDate() != null ? dateFormat.format(company.getFirstAccessDate()) : "";
                    mapColumn.put(CompanyListItem.FIRST_ACCESS_DATE, new ExcelData(firstAccessDate, ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.LAST_ACCESS_DATE)) {
                    String lastAccessDate = company.getLastAccessDate() != null ? dateFormat.format(company.getLastAccessDate()) : "";
                    mapColumn.put(CompanyListItem.LAST_ACCESS_DATE, new ExcelData(lastAccessDate, ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.SIGNED_UP_FROM)) {
                    mapColumn.put(CompanyListItem.SIGNED_UP_FROM, new ExcelData(escapeHtml(company.getCompanySignedUpFrom()), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.SUBSCRIPTION_TYPE)) {
                    String isPaid = "f".equals(company.getUsagePlanPaymentType()) ? "No" : "Yes";
                    mapColumn.put(CompanyListItem.SUBSCRIPTION_TYPE, new ExcelData(escapeHtml(isPaid), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.PAYMENT_STATUS)) {
                    mapColumn.put(CompanyListItem.PAYMENT_STATUS, new ExcelData(escapeHtml(company.getUsagePlanPaymentStatus()), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.PERIOD_ACCESS)) {
                    mapColumn.put(CompanyListItem.PERIOD_ACCESS, new ExcelData(escapeHtml(String.valueOf(company.getPeriodAccess())), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.USER_COUNT)) {
                    mapColumn.put(CompanyListItem.USER_COUNT, new ExcelData(escapeHtml(String.valueOf(company.getUserCount())), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.ACTIVE_USERS_COUNT)) {
                    mapColumn.put(CompanyListItem.ACTIVE_USERS_COUNT, new ExcelData(escapeHtml(company.getActiveUsersCount()), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.PROJECT_COUNT)) {
                    mapColumn.put(CompanyListItem.PROJECT_COUNT, new ExcelData(escapeHtml(company.getProjectCount()), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.TASK_COUNT)) {
                    mapColumn.put(CompanyListItem.TASK_COUNT, new ExcelData(escapeHtml(company.getTaskCount()), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.TIMESHEET_COUNT)) {
                    mapColumn.put(CompanyListItem.TIMESHEET_COUNT, new ExcelData(escapeHtml(String.valueOf(company.getTimesheetCount())), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.CLIENT_COUNT)) {
                    mapColumn.put(CompanyListItem.CLIENT_COUNT, new ExcelData(escapeHtml(company.getClientsCount()), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.SUPPLIER_COUNT)) {
                    mapColumn.put(CompanyListItem.SUPPLIER_COUNT, new ExcelData(escapeHtml(String.valueOf(company.getSupplierCount())), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.LEAD_COUNT)) {
                    mapColumn.put(CompanyListItem.LEAD_COUNT, new ExcelData(escapeHtml(String.valueOf(company.getLeadCount())), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.CONTACT_COUNT)) {
                    mapColumn.put(CompanyListItem.CONTACT_COUNT, new ExcelData(escapeHtml(String.valueOf(company.getContactCount())), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.CRM_TASK_COUNT)) {
                    mapColumn.put(CompanyListItem.CRM_TASK_COUNT, new ExcelData(escapeHtml(String.valueOf(company.getCrmtaskCount())), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.EVENT_COUNT)) {
                    mapColumn.put(CompanyListItem.EVENT_COUNT, new ExcelData(escapeHtml(String.valueOf(company.getEventCount())), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.CASE_COUNT)) {
                    mapColumn.put(CompanyListItem.CASE_COUNT, new ExcelData(escapeHtml(String.valueOf(company.getCaseCount())), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.INVOICE_COUNT)) {
                    mapColumn.put(CompanyListItem.INVOICE_COUNT, new ExcelData(escapeHtml(company.getInvoiceCount()), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.EXPENSE_COUNT)) {
                    mapColumn.put(CompanyListItem.EXPENSE_COUNT, new ExcelData(escapeHtml(String.valueOf(company.getExpenseCount())), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.PRODUCT_COUNT)) {
                    mapColumn.put(CompanyListItem.PRODUCT_COUNT, new ExcelData(escapeHtml(String.valueOf(company.getProductCount())), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.FOLDER_COUNT)) {
                    mapColumn.put(CompanyListItem.FOLDER_COUNT, new ExcelData(escapeHtml(String.valueOf(company.getFolderCount())), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.FILE_COUNT)) {
                    mapColumn.put(CompanyListItem.FILE_COUNT, new ExcelData(escapeHtml(String.valueOf(company.getFileCount())), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.ADMIN_EMAIL)) {
                    mapColumn.put(CompanyListItem.ADMIN_EMAIL, new ExcelData(escapeHtml(company.getAdminEmail()), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.COUNTRY)) {
                    mapColumn.put(CompanyListItem.COUNTRY, new ExcelData(escapeHtml(company.getCountry()), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.EXPIRATION_DATE)) {
                    String expireDate = company.getUsagPlanEndDate() != null ? ServerUtils.shortDateFormat(company.getUsagPlanEndDate(), userManager.getUser(), true) : "";
                    mapColumn.put(CompanyListItem.EXPIRATION_DATE, new ExcelData(expireDate, ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.HOST_NAME)) {
                    mapColumn.put(CompanyListItem.HOST_NAME, new ExcelData(escapeHtml(company.getHostName()), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.ADMIN_NAME)) {
                    mapColumn.put(CompanyListItem.ADMIN_NAME, new ExcelData(escapeHtml(company.getAdminName()), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.AFFILIATE)) {
                    mapColumn.put(CompanyListItem.AFFILIATE, new ExcelData(escapeHtml(company.getAffiliate()), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.COMPAING)) {
                    mapColumn.put(CompanyListItem.COMPAING, new ExcelData(escapeHtml(company.getCompaing()), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.SOURCE)) {
                    mapColumn.put(CompanyListItem.SOURCE, new ExcelData(escapeHtml(company.getSource()), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.MEDIUM)) {
                    mapColumn.put(CompanyListItem.MEDIUM, new ExcelData(escapeHtml(company.getMedium()), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.REDIRECTED)) {
                    mapColumn.put(CompanyListItem.REDIRECTED, new ExcelData(escapeHtml(company.getRedirected()), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.REFERRER)) {
                    mapColumn.put(CompanyListItem.REFERRER, new ExcelData(escapeHtml(company.getReferrer()), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.GCLID)) {
                    mapColumn.put(CompanyListItem.GCLID, new ExcelData(escapeHtml(company.getGclid()), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.NO_ACCESS_USER_COUNT_SUBS)) {
                    mapColumn.put(CompanyListItem.NO_ACCESS_USER_COUNT_SUBS, new ExcelData(escapeHtml(String.valueOf(company.getPlannedNoAccessUsers())), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.NO_ACCESS_USER_COUNT)) {
                    mapColumn.put(CompanyListItem.NO_ACCESS_USER_COUNT, new ExcelData(escapeHtml(String.valueOf(company.getNoAccessUserCount())), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.ACTIVE_USERS_COUNT_SUBS)) {
                    mapColumn.put(CompanyListItem.ACTIVE_USERS_COUNT_SUBS, new ExcelData(escapeHtml(company.getPlannedActiveUsers()), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.ESS_USERS_COUNT_SUBS)) {
                    mapColumn.put(CompanyListItem.ESS_USERS_COUNT_SUBS, new ExcelData(escapeHtml(company.getPlannedEssUsers()), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.ESS_USERS_COUNT_ACTUAL)) {
                    mapColumn.put(CompanyListItem.ESS_USERS_COUNT_ACTUAL, new ExcelData(escapeHtml(String.valueOf(company.getEssUsersCount())), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(CompanyListItem.ADMIN_PHONE)) {
                    mapColumn.put(CompanyListItem.ADMIN_PHONE, new ExcelData(escapeHtml(String.valueOf(company.getAdminPhone())), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                excelDataList = new ArrayList<>();
                for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                    if (mapColumn.containsKey(panelTools.getColumnCodeName().get(i))) {
                        excelDataList.add(mapColumn.get(panelTools.getColumnCodeName().get(i)));
                    }
                }
                cellDatas = new ExcelData[excelDataList.size()];
                excelDataList.toArray(cellDatas);
                list.add(cellDatas);
            }
            workBook.setList(list);

            return workBook.getWorkBook(filename, 0, 0, 0, header.size());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate solution list excel report, exception: " + e);
        }
        return null;
    }

    private String escapeHtml(String value) {
        if (ServerUtils.isNullOrEmpty(value)) {
            return "—";
        }
        if ("null".equals(value)) {
            return "—";
        }
        return value
                .replace("\u001F", "")
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    @Override
    protected void setFileName() {
        filename = "Company Statistic";
    }
}
