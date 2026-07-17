package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.ProfileManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class AsteriskEmployeeListExcelHandler extends BaseExcelHandler {

    private static final Logger log = LoggerFactory.getLogger(AsteriskEmployeeListExcelHandler.class);
    @Autowired
    private ProfileService profileService;
    @Autowired
    private ProfileManager profileManager;
    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }

    @Override
    protected void setFileName() {
        filename = "AsteriskEmployeeList";
    }

    public void setExcelReferenceMessageSource(WfmResourceBundleMessageSource wfmResourceBundleMessageSource) {
        this.excelReferenceMessageSource = wfmResourceBundleMessageSource;
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter listingFilterParameter = (ListingFilterParameter) object;
        String shortDateFormat = "MM/dd/yyyy";
        EdsUser user = profileManager.getUser();
        EdsCompany company = user.getCompany();
        EdsCompanySettings companySettings = company.getCompanySettings();
        if (companySettings != null) {
            shortDateFormat = companySettings.getShortDateFormat();
        }
        long timeStarted = System.currentTimeMillis();
        if (companySettings.getExcelLimit() != null && !"".equals(companySettings.getExcelLimit())) {
            listingFilterParameter.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
        } else {
            listingFilterParameter.setLimit(LIMIT_EXCEL_ROW);
        }

        int start = -200;
        int limit = 200;
        int totalLength = 1;
        ListResult<EmployeeListItem> employees = null;
        while (totalLength > (start += limit)) {
            listingFilterParameter.setStart(start);
            listingFilterParameter.setLimit(200);
            employees = profileService.getAsteriskEmployeeList(listingFilterParameter.getRelationID(), listingFilterParameter);
            totalLength = employees.getTotal();
        }
        ListPanelToolRpc toolRpc = listingFilterParameter.getListPanelTool();

        ExcelData[] cellDatas;
        Map<String, ExcelData> mapColumnHeader = new HashMap<>();
        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);
            List<ExcelData[]> list = new LinkedList<>();

            mapColumnHeader.put(EmployeeListItem.EMPLOYEE_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.employee), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(EmployeeListItem.STATUS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.status), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(EmployeeListItem.USERNAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.internalNumber), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.HEADER));

            CustomFieldsUtils.setCustomFieldsExcelHeaderMap(toolRpc.getListViewCustomFields(), mapColumnHeader);
            list.add(generateOneRowWithValue(toolRpc.getColumnCodeName().size(), company.getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(toolRpc.getColumnCodeName().size(), "Asterisk employee", workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(toolRpc.getColumnCodeName().size(), excelReferenceMessageSource.localize("EPAsOf", " As Of") + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            List<ExcelData> excelDataList = new ArrayList<>();
            for (int i = 0; i < toolRpc.getColumnCodeName().size(); i++) {
                if (mapColumnHeader.containsKey(toolRpc.getColumnCodeName().get(i))) {
                    excelDataList.add(getExcelDataHeader(mapColumnHeader.get(toolRpc.getColumnCodeName().get(i))));
                }
            }
            cellDatas = new ExcelData[excelDataList.size()];
            excelDataList.toArray(cellDatas);
            list.add(cellDatas);

            for (EmployeeListItem item : employees.getList()) {
                Map<String, ExcelData> mapColumn = new HashMap<>();
                if (toolRpc.getColumnCodeName().contains(EmployeeListItem.EMPLOYEE_NAME)) {
                    mapColumn.put(EmployeeListItem.EMPLOYEE_NAME, new ExcelData(item.getFullName(), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (toolRpc.getColumnCodeName().contains(EmployeeListItem.STATUS)) {
                    mapColumn.put(EmployeeListItem.STATUS, new ExcelData(item.getStatus(), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (toolRpc.getColumnCodeName().contains(EmployeeListItem.USERNAME)) {
                    mapColumn.put(EmployeeListItem.USERNAME, new ExcelData(item.getAsteriskUsername(), ExcelData.STRING, 30, true, false, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                CustomFieldsUtils.setCustomFieldsExcelTableRows(toolRpc.getListViewCustomFields(), mapColumn, toolRpc.getColumnCodeName(), item, company);
                excelDataList = new ArrayList<>();
                for (int i = 0; i < toolRpc.getColumnCodeName().size(); i++) {
                    if (mapColumn.containsKey(toolRpc.getColumnCodeName().get(i))) {
                        excelDataList.add(getExcelRows(mapColumn.get(toolRpc.getColumnCodeName().get(i))));
                    }
                }
                cellDatas = new ExcelData[excelDataList.size()];
                excelDataList.toArray(cellDatas);
                list.add(cellDatas);
            }
            System.out.println(excelReferenceMessageSource.localize("ProfilingExcelData", "Profiling excel Data generation, time spent:") + (System.currentTimeMillis() - timeStarted));
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, 7);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate asterisk employee list excel report, exception: " + e);
        }
        return null;
    }




}
