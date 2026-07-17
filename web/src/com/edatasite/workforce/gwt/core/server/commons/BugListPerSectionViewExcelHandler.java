package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.backend.client.rpc.BugsPerEmployeesListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
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
 * Date: 17.08.2009
 * Time: 18:28:30
 * To change this template use File | Settings | File Templates.
 */
public class BugListPerSectionViewExcelHandler extends BaseExcelHandler {

    private BackendService backendService;
    private static final Logger log = LoggerFactory.getLogger(BugListPerSectionViewExcelHandler.class);

  /*  protected Object getDataClass(HttpServletRequest request) {
        return new ListingFilterParameter();
    }*/

    @Override
    protected void setFileName() {
        filename = "Summary by Section";
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter fp = (ListingFilterParameter) object;
        /*Integer start = filterParametrs.getStart() == null ? 0 : filterParametrs.getStart();
        Integer sortDir = filterParametrs.getSortDir() == null ? 0 : filterParametrs.getSortDir();
        Integer limit = filterParametrs.getLimit() == null ? 0 : filterParametrs.getLimit();
        config.setStart(start);*/
        fp.setLimit(1000);
        /*fp.setSortField(fp.getSortField());
        fp.setSortDir(sortDir);*/
        ListResult<BugsPerEmployeesListItem> bugsPerSectionList = backendService.getBugsPerSections(fp);
        List<BugsPerEmployeesListItem> bugsPerSectionListItems = bugsPerSectionList.getList();
        ExcelData[] cellDatas;
        ListPanelToolRpc panelTools = fp.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(BugsPerEmployeesListItem.SECTION, "Section");
        mapColumnHeader.put(BugsPerEmployeesListItem.STATUS_NEW, "New");
        mapColumnHeader.put(BugsPerEmployeesListItem.STATUS_RESOLVED, "Resolved");
        mapColumnHeader.put(BugsPerEmployeesListItem.STATUS_UNDER_INVESTIGATION, "Under Investigation");
        mapColumnHeader.put(BugsPerEmployeesListItem.STATUS_IN_PROGRESS, "In Progress");
        mapColumnHeader.put(BugsPerEmployeesListItem.STATUS_IGNORED, "Ignored");
        mapColumnHeader.put(BugsPerEmployeesListItem.STATUS_DONE, "Done");
        mapColumnHeader.put(BugsPerEmployeesListItem.TOTAL_BUG, "Total");

        try {
            List<ExcelData[]> list = new LinkedList<>();
            cellDatas = new ExcelData[header.size()];

            for (int i = 0; i < header.size(); i++) {
                cellDatas[i] = new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, header.get(i).equals(BugsPerEmployeesListItem.SECTION) || header.get(i).equals(BugsPerEmployeesListItem.STATUS_NEW) ? 50 : 20, false, header.get(i).equals(BugsPerEmployeesListItem.STATUS_RESOLVED) || header.get(i).equals(BugsPerEmployeesListItem.STATUS_UNDER_INVESTIGATION) || header.get(i).equals(BugsPerEmployeesListItem.STATUS_IN_PROGRESS) || header.get(i).equals(BugsPerEmployeesListItem.STATUS_IGNORED) || header.get(i).equals(BugsPerEmployeesListItem.STATUS_DONE) || header.get(i).equals(BugsPerEmployeesListItem.TOTAL_BUG), ExcelData.NO_BORDER, ExcelData.HEADER);
            }

            list.add(cellDatas);
            for (BugsPerEmployeesListItem item : bugsPerSectionListItems) {
                String temp = "";
                cellDatas = new ExcelData[header.size()];
                for (int j = 0; j < header.size(); j++) {
                    temp = "";
                    if (BugsPerEmployeesListItem.SECTION.equals(header.get(j))) {
                    temp = item.getSection() != null ? item.getSection(): "";
                } else if (BugsPerEmployeesListItem.STATUS_NEW.equals(header.get(j))) {
                    temp = (item.getStatusNew()!= null ? item.getStatusNew():Integer.valueOf(0)).toString();
                } else if (BugsPerEmployeesListItem.STATUS_RESOLVED.equals(header.get(j))) {
                    temp = (item.getResolved() != null ? item.getResolved():Integer.valueOf(0)).toString();
                } else if (BugsPerEmployeesListItem.STATUS_UNDER_INVESTIGATION.equals(header.get(j))) {
                    temp = (item.getUnderInvest() != null ? item.getUnderInvest():Integer.valueOf(0)).toString();
                } else if (BugsPerEmployeesListItem.STATUS_IN_PROGRESS.equals(header.get(j))) {
                    temp = (item.getInProgress()!=null? item.getInProgress():Integer.valueOf(0)).toString();
                } else if (BugsPerEmployeesListItem.STATUS_IGNORED.equals(header.get(j))) {
                    temp = (item.getIgnored()!=null? item.getIgnored():Integer.valueOf(0)).toString();
                } else if (BugsPerEmployeesListItem.STATUS_DONE.equals(header.get(j))) {
                        temp = (item.getDone() != null ? item.getDone() : Integer.valueOf(0)).toString();
                } else if (BugsPerEmployeesListItem.TOTAL_BUG.equals(header.get(j))) {
                        temp = (item.getTotal() != null ? item.getTotal() : Integer.valueOf(0)).toString();
                }
                cellDatas[j] = new ExcelData(temp, ExcelData.STRING, header.get(j).equals(BugsPerEmployeesListItem.SECTION) || header.get(j).equals(BugsPerEmployeesListItem.STATUS_NEW) ? 50 : 20, false, header.get(j).equals(BugsPerEmployeesListItem.STATUS_RESOLVED) || header.get(j).equals(BugsPerEmployeesListItem.STATUS_UNDER_INVESTIGATION) || header.get(j).equals(BugsPerEmployeesListItem.STATUS_IN_PROGRESS) || header.get(j).equals(BugsPerEmployeesListItem.STATUS_IGNORED) || header.get(j).equals(BugsPerEmployeesListItem.STATUS_DONE) || header.get(j).equals(BugsPerEmployeesListItem.TOTAL_BUG),ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                list.add(cellDatas);
            }
            return new WorkBook(list).getWorkBook(filename, 0, 0, 0, header.size());

        }
        catch (Exception ex) {
            ex.printStackTrace();
            log.error("Cannot generate Summary by Section excel report, exception: " + ex);
        }
        return null;
    }

    public void setBackendService(BackendService backendService) {
        this.backendService = backendService;
    }
}
