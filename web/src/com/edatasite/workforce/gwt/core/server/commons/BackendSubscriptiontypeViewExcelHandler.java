package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.SubscriptionList;
import com.edatasite.workforce.gwt.backend.client.rpc.SubscriptionListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 15.08.2009
 * Time: 17:28:02
 * To change this template use File | Settings | File Templates.
 */
public class BackendSubscriptiontypeViewExcelHandler extends BaseExcelHandler {

    private BackendService backendService;
    private static final Logger log = LoggerFactory.getLogger(BackendSubscriptiontypeViewExcelHandler.class);

   /* protected Object getDataClass(HttpServletRequest request) {
        return new ListingFilterParameter();
    }*/

    @Override
    protected void setFileName() {
        filename = "Subscription Types";
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        ListLoadConfig config = new ListLoadConfig();
        Integer start = filterParametrs.getStart() == null ? 0 : filterParametrs.getStart();
        Integer sortDir = filterParametrs.getSortDir() == null ? 0 : filterParametrs.getSortDir();
        Integer limit = filterParametrs.getLimit() == null ? 0 : filterParametrs.getLimit();
        config.setStart(start);
        config.setLimit(limit);
        config.setSortField(filterParametrs.getSortField());
        config.setSortDir(sortDir);
        SubscriptionList companyList = backendService.getSubscriptiontype(config);
        SubscriptionListItem[] companies = companyList.getResults();
        ExcelData[] cellDatas;
        try {
            List<ExcelData[]> list = new LinkedList<>();
            cellDatas = new ExcelData[]{
                    new ExcelData("Name", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Task Limit", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Project Limit", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Employee Limit", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Department Limit", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Attachments File Size Per User Limit", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Attachments Size Per Company", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Appraisals Limit", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Appraisals 360 Limit", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Invoce Limit", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER)
            };
            list.add(cellDatas);
            for (SubscriptionListItem item : companies) {
                String name = "";
                Integer taskLimit = 0;
                Integer projectLimit = 0;
                Integer employeeLimit = 0;
                Integer departmentLimit = 0;
                Integer attachlentLimitPerUser = 0;
                Integer attachmentLimitPerCompany = 0;
                Integer appraisalsLimit = 0;
                Integer appraisals360Limit = 0;
                Integer invoceLimit = 0;
                if (item.getName() != null) {
                    name = item.getName();
                }
                if (item.getTaskLimit() != null) {
                    taskLimit = item.getTaskLimit();
                }
                if (item.getProjectLimit() != null) {
                    projectLimit = item.getProjectLimit();
                }
                if (item.getEmployeeLimit() != null) {
                    employeeLimit = item.getEmployeeLimit();
                }
                if (item.getDepartmentLimit() != null) {
                    departmentLimit = item.getDepartmentLimit();
                }
                if (item.getAttachmentsFileSizePerUserLimit() != null) {
                    attachlentLimitPerUser = item.getAttachmentsFileSizePerUserLimit();
                }
                if (item.getAttachmentsSizePerCompany() != null) {
                    attachmentLimitPerCompany = item.getAttachmentsSizePerCompany();
                }
                if (item.getAppraisalsLimit() != null) {
                    appraisalsLimit = item.getAppraisalsLimit();
                }
                if (item.getAppraisals360Limit() != null) {
                    appraisals360Limit = item.getAppraisals360Limit();
                }
                if (item.getInvoiceLimit() != null) {
                    invoceLimit = item.getInvoiceLimit();
                }
                cellDatas = new ExcelData[]{
                        new ExcelData(name, ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                        new ExcelData(taskLimit, ExcelData.INTEGER, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                        new ExcelData(projectLimit, ExcelData.INTEGER, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                        new ExcelData(employeeLimit, ExcelData.INTEGER, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                        new ExcelData(departmentLimit, ExcelData.INTEGER, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                        new ExcelData(attachlentLimitPerUser, ExcelData.INTEGER, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                        new ExcelData(attachmentLimitPerCompany, ExcelData.INTEGER, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                        new ExcelData(appraisalsLimit, ExcelData.INTEGER, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                        new ExcelData(appraisals360Limit, ExcelData.INTEGER, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                        new ExcelData(invoceLimit, ExcelData.INTEGER, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL)
                };
                list.add(cellDatas);
            }
            return new WorkBook(list).getWorkBook(filename, 0, 0, 0, 6);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot ganarate subscription types excel report, exception: " + e);
        }
        return null;
    }

    public void setBackendService(BackendService backendService) {
        this.backendService = backendService;
    }
}
