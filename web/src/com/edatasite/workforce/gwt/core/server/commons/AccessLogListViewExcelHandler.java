package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.gwt.backend.client.rpc.AccessLogListItem;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
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
 * Date: 18.08.2009
 * Time: 14:54:10
 * To change this template use File | Settings | File Templates.
 */
public class AccessLogListViewExcelHandler extends BaseExcelHandler {

    private BackendService backendService;
    private static final Logger log = LoggerFactory.getLogger(AccessLogListViewExcelHandler.class);

   /* protected Object getDataClass(HttpServletRequest request) {
        return new ListingFilterParameter();
    }
*/
    @Override
    protected void setFileName() {
        filename = "Access Log";
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter fp = (ListingFilterParameter) object;
        fp.setLimit(1000);
        ListResult<AccessLogListItem> accessLogList = backendService.getAccessLog(fp);
        List<AccessLogListItem> accessLogListItems = accessLogList.getList();
        ExcelData[] cellDatas;
         ListPanelToolRpc panelTools = fp.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(AccessLogListItem.COMPANY_ID, "Company ID");
        mapColumnHeader.put(AccessLogListItem.COMPANY_NAME, "Company Name");
        mapColumnHeader.put(AccessLogListItem.USERS, "User");
        mapColumnHeader.put(AccessLogListItem.EMAIL, "E-mail");
        mapColumnHeader.put(AccessLogListItem.DATE_ACCESSED, "Date Accessed");
        mapColumnHeader.put(AccessLogListItem.BROWSER_TYPE_VERSION, "Browser Type/Version");
        try {
             List<ExcelData[]> list = new LinkedList<>();
             cellDatas = new ExcelData[header.size()];

             for (int i = 0; i < header.size(); i++) {
                 cellDatas[i] = new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, header.get(i).equals(AccessLogListItem.COMPANY_ID) || header.get(i).equals(AccessLogListItem.COMPANY_NAME) ? 50 : 20, false, header.get(i).equals(AccessLogListItem.USERS) || header.get(i).equals(AccessLogListItem.EMAIL) || header.get(i).equals(AccessLogListItem.DATE_ACCESSED) || header.get(i).equals(AccessLogListItem.BROWSER_TYPE_VERSION), ExcelData.NO_BORDER, ExcelData.HEADER);
             }
             list.add(cellDatas);

            for (AccessLogListItem item : accessLogListItems) {
                String temp = "";
                cellDatas = new ExcelData[header.size()];
                for (int j = 0; j < header.size(); j++) {
                    temp = "";
                    if (AccessLogListItem.COMPANY_ID.equals(header.get(j))) {
                    temp = (item.getCompanyid() != null ? item.getCompanyid(): "").toString();
                } else if (AccessLogListItem.COMPANY_NAME.equals(header.get(j))) {
                    temp = item.getCompanyName()!= null ? item.getCompanyName():"";
                } else if (AccessLogListItem.USERS.equals(header.get(j))) {
                    temp = item.getUserName() != null ? item.getUserName():"";
                } else if (AccessLogListItem.EMAIL.equals(header.get(j))) {
                    temp = item.getEmail() != null ? item.getEmail():"";
                } else if (AccessLogListItem.DATE_ACCESSED.equals(header.get(j))) {
                    temp = (item.getLastAccessDate()!=null? item.getLastAccessDate():"").toString();
                } else if (AccessLogListItem.BROWSER_TYPE_VERSION.equals(header.get(j))) {
                    temp = item.getBrowserType()!=null? item.getBrowserType():"";
                }
                    cellDatas[j] = new ExcelData(temp, ExcelData.STRING, header.get(j).equals(AccessLogListItem.COMPANY_ID) || header.get(j).equals(AccessLogListItem.COMPANY_NAME) ? 50 : 20, false, header.get(j).equals(AccessLogListItem.USERS) || header.get(j).equals(AccessLogListItem.EMAIL) || header.get(j).equals(AccessLogListItem.DATE_ACCESSED) || header.get(j).equals(AccessLogListItem.BROWSER_TYPE_VERSION), ExcelData.NO_BORDER, ExcelData.NORMAL);

                }
                list.add(cellDatas);
            }
              return new WorkBook(list).getWorkBook(filename, 0, 0, 0, header.size());
         }
         catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot ganarate Access Log  excel report, exception: " + e);
        }
        return null;

    }

    public void setBackendService(BackendService backendService) {
        this.backendService = backendService;
    }
}
