package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.BugsPerEmployeesListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 17.08.2009
 * Time: 15:45:30
 * To change this template use File | Settings | File Templates.
 */
public class BugListSummaryViewExcelHandler extends BaseExcelHandler {

    private BackendService backendService;
    private static final Logger log = LoggerFactory.getLogger(BugListSummaryViewExcelHandler.class);

  /*  protected Object getDataClass(HttpServletRequest request) {
        return new ListingFilterParameter();
    }*/

    @Override
    protected void setFileName() {
        filename = "Summary by Employee";
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        Integer start = filterParametrs.getStart() == null ? 0 : filterParametrs.getStart();
        Integer sortDir = filterParametrs.getSortDir() == null ? 0 : filterParametrs.getSortDir();
        Integer limit = filterParametrs.getLimit() == null ? 0 : filterParametrs.getLimit();

        filterParametrs.setLimit(1000);
        ListResult<BugsPerEmployeesListItem> bugsPerEmployeesList = backendService.getBugsPerEmployees(filterParametrs);
        List<BugsPerEmployeesListItem> bugsPerEmployeesListItems = bugsPerEmployeesList.getList();
        ExcelData[] cellDatas;
        try {
            List<ExcelData[]> list = new LinkedList<>();
            cellDatas = new ExcelData[]{
                    new ExcelData("Employee", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("New", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Resolved", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Under Investigation", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("In Progress", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Ignored", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Done", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Total", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER)
            };
            list.add(cellDatas);
            for (BugsPerEmployeesListItem item : bugsPerEmployeesListItems) {
                String employee = "";
                Integer newStatus = 0;
                Integer resolved = 0;
                Integer underInvestigation = 0;
                Integer inProgress = 0;
                Integer ignored = 0;
                Integer done = 0;
                Integer total = 0;
                if (item.getEmployee() != null) {
                    employee = item.getEmployee();
                }
                if (item.getStatusNew() != null) {
                    newStatus = item.getStatusNew();
                }
                if (item.getResolved() != null) {
                    resolved = item.getResolved();
                }
                if (item.getUnderInvest() != null) {
                    underInvestigation = item.getUnderInvest();
                }
                if (item.getInProgress() != null) {
                    inProgress = item.getInProgress();
                }
                if (item.getIgnored() != null) {
                    ignored = item.getIgnored();
                }
                if (item.getDone() != null) {
                    done = item.getDone();
                }
                if (item.getTotal() != null) {
                    total = item.getTotal();
                }
                cellDatas = new ExcelData[]{
                        new ExcelData(employee, ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                        new ExcelData(newStatus, ExcelData.INTEGER, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                        new ExcelData(resolved, ExcelData.INTEGER, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                        new ExcelData(underInvestigation, ExcelData.INTEGER, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                        new ExcelData(inProgress, ExcelData.INTEGER, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                        new ExcelData(ignored, ExcelData.INTEGER, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                        new ExcelData(done, ExcelData.INTEGER, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                        new ExcelData(total, ExcelData.INTEGER, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL)
                };
                list.add(cellDatas);
            }
            return new WorkBook(list).getWorkBook(filename, 0, 0, 0, 6);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate summary by employee excel report, exception: " + e);
        }
        return null;
    }

    public void setBackendService(BackendService backendService) {
        this.backendService = backendService;
    }
}
