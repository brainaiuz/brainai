package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.crm.client.rpc.ActivityItem;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: unni
 * Date: Aug 10, 2009
 * Time: 8:08:43 PM
 */
public class CrmActivitiesExcelHandler extends BaseExcelHandler {
    @Autowired
    @Qualifier("crmService")
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private UserManager userManager;
    private static final Logger log = LoggerFactory.getLogger(CrmActivitiesExcelHandler.class);

    /* protected Object getDataClass(HttpServletRequest request) {
        return new ListingFilterParameter();
    }*/

    @Override
    protected void setFileName() {
        filename = "Crm Activities";
    }

    protected HSSFWorkbook getWorkBook(Object object) {

        String shortDateFormat = "MM/dd/yyyy";
        EdsCompanySettings companySettings = userManager.getUser().getCompany().getCompanySettings();
        if (companySettings != null) {
            shortDateFormat = companySettings.getShortDateFormat();
        }

        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        filterParametrs = filterParametrs == null ? new ListingFilterParameter() : filterParametrs;
        if (companySettings.getExcelLimit() != null && !"".equals(companySettings.getExcelLimit())) {
            filterParametrs.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
        } else {
            filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        }
        ListLoadConfig config = new ListLoadConfig();
        config.setStart(0);
        config.setLimit(0);
        ListResult<ActivityItem> activityList = crmServiceLocal.getActivityList(filterParametrs);
        ExcelData[] cellDatas;
        try {
            List<ExcelData[]> list = new LinkedList<>();
            cellDatas = new ExcelData[]{
                    new ExcelData("Subject", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Activity Type", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Start Date", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Due Date", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Status", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER),
                    new ExcelData("Priority", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER)
            };
            list.add(cellDatas);
            for (ActivityItem item : activityList.getList()) {

                cellDatas = new ExcelData[]{
                        new ExcelData(item.getSubject(), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                        new ExcelData(item.getActivityType(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                        new ExcelData(ServerUtils.shortDateFormat(item.getStartDate(), userManager.getUser(), true), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                        new ExcelData(ServerUtils.shortDateFormat(item.getDueDate(), userManager.getUser(), true), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                        new ExcelData(item.getStatus(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL),
                        new ExcelData(item.getPriority(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL)
                };
                list.add(cellDatas);
            }
            return new WorkBook(list).getWorkBook(filename, 0, 0, 0, 6);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate Crm Activities list excel report, exception: " + e);
        }
        return null;
    }
}
