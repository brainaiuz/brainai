package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.BugList;
import com.edatasite.workforce.gwt.backend.client.rpc.BugListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Man
 * Date: 17.12.2008
 * Time: 15:45:28
 * To change this template use File | Settings | File Templates.
 */
public class BugListExcelHandler extends BaseExcelHandler {

    @Autowired
    private BackendService backendService;

    private static final Logger log = LoggerFactory.getLogger(BugListExcelHandler.class);

   /* protected Object getDataClass(HttpServletRequest request) {
        return new ListingFilterParameter();
    }*/

    @Override
    protected void setFileName() {
        filename = "Bug List";
    }

    protected HSSFWorkbook getWorkBook(Object object) {

        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        ListLoadConfig config = new ListLoadConfig();
        config.setStart(filterParametrs.getStart());
        config.setSortField(filterParametrs.getSortField());
        if (filterParametrs.getSortDir() != null) {
            config.setSortDir(filterParametrs.getSortDir());
        }
        BugList bugList = backendService.getBugLists(filterParametrs, config);
        BugListItem[] bugs = bugList.getBugListItems();

        ExcelData[] cellDatas;
        try {
            List<ExcelData[]> list = new LinkedList<>();
            cellDatas = new ExcelData[]{
                    new ExcelData("ID", ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Bug", ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("User", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Company", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Status", ExcelData.STRING, 10, false, false, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Priority", ExcelData.STRING, 10, false, false, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Created On", ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Created From", ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Last Update", ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Assignee", ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Comments", ExcelData.STRING, 50, true, false, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Browser", ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.HEADER)
            };
            list.add(cellDatas);
            for (BugListItem bug : bugs) {
                String bugName = " ";
                String user = " ";
                String status = " ";
                String priority = " ";
                if (bug.getBug() != null) {
                    bugName = bug.getBug();
                }
                if (bug.getUser() != null) {
                    user = bug.getUser();
                }
                if (bug.getStatus() != null) {
                    status = bug.getStatus();
                }
                if (bug.getPriority() != null) {
                    priority = bug.getPriority();
                }
                cellDatas = new ExcelData[]{
                        new ExcelData(bug.getBugId(), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                        new ExcelData(bugName, ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                        new ExcelData(user, ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                        new ExcelData(bug.getCompany(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                        new ExcelData(status, ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL),
                        new ExcelData(priority, ExcelData.STRING, 10, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL),
                        new ExcelData(bug.getCreationTime(), ExcelData.STRING, 20, false, false, ExcelData.NO_BORDER, ExcelData.NORMAL),
                        new ExcelData(bug.getCreatedFrom(), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL),
                        new ExcelData(bug.getUpdateTime(), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL),
                        new ExcelData(bug.getAssignee(), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL),
                        new ExcelData(bug.getComment(), ExcelData.STRING, 50, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL),
                        new ExcelData(bug.getBrowser(), ExcelData.STRING, 20, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL)
                };
                list.add(cellDatas);
            }
            return new WorkBook(list).getWorkBook(filename, 0, 0, 0, 6);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate bug list excel report, exception: " + e);
        }
        return null;
    }

}
