package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.CompanyList;
import com.edatasite.workforce.gwt.backend.client.rpc.CompanyListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 15.08.2009
 * Time: 17:12:41
 * To change this template use File | Settings | File Templates.
 */
public class BackendReturningViewExcelHandler extends BaseExcelHandler {

    private BackendService backendService;
    private static final Logger log = LoggerFactory.getLogger(BackendReturningViewExcelHandler.class);

    /* protected Object getDataClass(HttpServletRequest request) {
        return new ListingFilterParameter();
    }*/

    @Override
    protected void setFileName() {
        filename = "Returning Users";
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        CompanyList companyList = backendService.getCompanies(true, filterParametrs);
        List<CompanyListItem> companies = companyList.getList();
        ExcelData[] cellDatas;
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        header.remove(CompanyListItem.ACTION);
        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(CompanyListItem.COMPANY_ID, "Assignee");
        mapColumnHeader.put(CompanyListItem.COMPANY_NAME, "Company Name");
        mapColumnHeader.put(CompanyListItem.PHONE, "Phone");
        mapColumnHeader.put(CompanyListItem.LAST_ACCESS_DATE, "Last Access");
        mapColumnHeader.put(CompanyListItem.REGISTRATION_DATE, "Registration Date");
        mapColumnHeader.put(CompanyListItem.ACCESS_COUNT, "Access Count");
        mapColumnHeader.put(CompanyListItem.EMPLOYEES, "Employees");
        mapColumnHeader.put(CompanyListItem.PROJECT_COUNT, "Projects");
        mapColumnHeader.put(CompanyListItem.TASK_COUNT, "Tasks");
        mapColumnHeader.put(CompanyListItem.DEPARTMENTCOUNT, "Departments");
        mapColumnHeader.put(CompanyListItem.SIGNED_UP_FROM, "Signed Page");
        mapColumnHeader.put(CompanyListItem.APPRAISALSCOUNT, "Appraisals");
        mapColumnHeader.put(CompanyListItem.INVOICE_COUNT, "Invoces");
        try {
            List<ExcelData[]> list = new LinkedList<>();
            cellDatas = new ExcelData[header.size()];

            for (int i = 0; i < header.size(); i++) {
                cellDatas[i] = new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, header.get(i).equals(CompanyListItem.COMPANY_NAME) || header.get(i).equals(CompanyListItem.PHONE) ? 50 : 20, false, header.get(i).equals(CompanyListItem.COMPANY_NAME) || header.get(i).equals(CompanyListItem.PHONE) || header.get(i).equals(CompanyListItem.LAST_ACCESS_DATE), ExcelData.NO_BORDER, ExcelData.HEADER);
            }

            list.add(cellDatas);
            for (CompanyListItem solutionItem : companies) {
                String temp = "";
                cellDatas = new ExcelData[header.size()];
                for (int j = 0; j < header.size(); j++) {
                    temp = "";
                    if (CompanyListItem.COMPANY_ID.equals(header.get(j))) {
                        temp = solutionItem.getCompanyID() == null ? "" : solutionItem.getCompanyID().toString();
                    } else if (CompanyListItem.COMPANY_NAME.equals(header.get(j))) {
                        temp = solutionItem.getCompanyName() == null ? "" : solutionItem.getCompanyName();
                    } else if (CompanyListItem.PHONE.equals(header.get(j))) {
                        temp = solutionItem.getPhone() == null ? "" : solutionItem.getPhone();
                    } else if (CompanyListItem.LAST_ACCESS_DATE.equals(header.get(j))) {
                        temp = solutionItem.getLastAccessDate() == null ? "" : solutionItem.getLastAccessDate().toString();
                    } else if (CompanyListItem.REGISTRATION_DATE.equals(header.get(j))) {
                        temp = solutionItem.getRegistrationDate() == null ? "" : solutionItem.getRegistrationDate().toString();
                    } else if (CompanyListItem.ACCESS_COUNT.equals(header.get(j))) {
                        temp = solutionItem.getAccessCount() == null ? "" : solutionItem.getAccessCount();
                    } else if (CompanyListItem.EMPLOYEES.equals(header.get(j))) {
                        temp = solutionItem.getEmployeeCount() == null ? "" : solutionItem.getEmployeeCount();
                    } else if (CompanyListItem.PROJECT_COUNT.equals(header.get(j))) {
                        temp = solutionItem.getProjectCount() == null ? "" : solutionItem.getProjectCount();
                    } else if (CompanyListItem.TASK_COUNT.equals(header.get(j))) {
                        temp = solutionItem.getTaskCount() == null ? "" : solutionItem.getTaskCount();
                    } else if (CompanyListItem.DEPARTMENTCOUNT.equals(header.get(j))) {
                        temp = solutionItem.getDepartmentCount() == null ? "" : solutionItem.getDepartmentCount();
                    } else if (CompanyListItem.SIGNED_UP_FROM.equals(header.get(j))) {
                        temp = solutionItem.getSignedUpPage() == null ? "" : solutionItem.getSignedUpPage();
                    } else if (CompanyListItem.APPRAISALSCOUNT.equals(header.get(j))) {
                        temp = solutionItem.getAppraisalsCount() == null ? "" : solutionItem.getAppraisalsCount();
                    } else if (CompanyListItem.INVOICE_COUNT.equals(header.get(j))) {
                        temp = solutionItem.getInvoiceCount() == null ? "" : solutionItem.getInvoiceCount();
                    }
                    cellDatas[j] = new ExcelData(temp, ExcelData.STRING, header.get(j).equals(CompanyListItem.COMPANY_NAME) || header.get(j).equals(CompanyListItem.PHONE) ? 50 : 20, false, !header.get(j).equals(CompanyListItem.LAST_ACCESS_DATE), ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                list.add(cellDatas);
            }

            return new WorkBook(list).getWorkBook(filename, 0, 0, 0, header.size());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot ganarate returning users excel report, exception: " + e);
        }
        return null;
    }

    public void setBackendService(BackendService backendService) {
        this.backendService = backendService;
    }
}
