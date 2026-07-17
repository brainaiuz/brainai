package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.team.client.rpc.DepartmentService;
import com.edatasite.workforce.gwt.team.client.rpc.TeamListItem;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DepartmentListExcelHandler extends BaseExcelHandler {


    @Autowired
    private PropertManager propertManager;
    @Autowired
    private DepartmentService departmentService;


    @Override
    protected void setFileName(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
        filename = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.departments);   }

    private static final Logger log = LoggerFactory.getLogger(DepartmentListExcelHandler.class.getName());
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }

    @Override
    protected void setFileName() {

    }

    protected HSSFWorkbook getWorkBook(Object dataClass) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        filterParametrs.setLimit(20000);
        ListResult<TeamListItem> teamListItemListResult = departmentService.getTeams(filterParametrs);
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        ExcelData[] cellDatas;
        EdsUser user = getUser();
        EdsCompany edsCompany = user.getCompany();
        Map<String, ExcelData> mapColumnHeader = new HashMap<>();
        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);

            List<ExcelData[]> list = new LinkedList<>();
            mapColumnHeader.put(TeamListItem.CODE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.number), ExcelData.STRING, 15, true, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TeamListItem.NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.name), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TeamListItem.PARENT_DEPARTMENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.reportsTo), ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TeamListItem.LEADER_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.leader), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TeamListItem.HEADCOUNT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.headCount), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TeamListItem.START_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.startDateField), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TeamListItem.STATUS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.status), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TeamListItem.END_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.endDateField), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(TeamListItem.LOCATION_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.location), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), edsCompany.getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), commonLocalizer.localize(PdfLocalizationName.department), workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " Xolatiga ko'ra" : commonLocalizer.localize("asOF", "As of") + " " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            CustomFieldsUtils.setCustomFieldsExcelHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);

            List<ExcelData> excelDataList = new ArrayList<>();
            for (String columnName : panelTools.getColumnCodeName()) {
                if (mapColumnHeader.containsKey(columnName)) {
                    excelDataList.add(mapColumnHeader.get(columnName));
                }
            }
            cellDatas = new ExcelData[excelDataList.size()];
            excelDataList.toArray(cellDatas);
            list.add(cellDatas);

            for (TeamListItem item : teamListItemListResult.getList()) {
                Map<String, ExcelData> mapColumns = new HashMap<>();

                if (panelTools.getColumnCodeName().contains(TeamListItem.NAME)) {
                    mapColumns.put(TeamListItem.CODE, new ExcelData(item.getDepartmentCode() != null ? item.getDepartmentCode() : "", ExcelData.STRING, 15, true, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(TeamListItem.NAME)) {
                    mapColumns.put(TeamListItem.NAME, new ExcelData(item.getName() != null ? item.getName() : "", ExcelData.STRING, 35, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(TeamListItem.PARENT_DEPARTMENT)) {
                    mapColumns.put(TeamListItem.PARENT_DEPARTMENT, new ExcelData(item.getParentDepartment() != null ? item.getParentDepartment().getName() : "N/A", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(TeamListItem.LEADER_NAME)) {
                    mapColumns.put(TeamListItem.LEADER_NAME, new ExcelData(item.getLeader() != null ? item.getLeader() : "", ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(TeamListItem.HEADCOUNT)) {
                    mapColumns.put(TeamListItem.HEADCOUNT, new ExcelData(item.getHeadCount() != null ? item.getHeadCount() : "", ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(TeamListItem.STATUS)) {
                    mapColumns.put(TeamListItem.STATUS, new ExcelData(item.isActive() != null ? item.isActive() ? commonLocalizer.localize(PdfLocalizationName.active) : commonLocalizer.localize(PdfLocalizationName.inactive) : "", ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(TeamListItem.START_DATE)) {
                    mapColumns.put(TeamListItem.START_DATE, new ExcelData(item.getStartDate() != null ? dateFormat(item.getStartDate()) : "N/A", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(TeamListItem.END_DATE)) {
                    String endDate = ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat(item.getEndDate())) : dateFormat(item.getEndDate());
                    mapColumns.put(TeamListItem.END_DATE, new ExcelData(item.getEndDate() != null ? endDate : "N/A", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(TeamListItem.LOCATION_NAME)) {
                    mapColumns.put(TeamListItem.LOCATION_NAME, new ExcelData(item.getLocation() != null ? item.getLocation().getName() : "N/A", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                CustomFieldsUtils.setCustomFieldsExcelTableRows(panelTools.getListViewCustomFields(), mapColumns, panelTools.getColumnCodeName(), item, edsCompany);

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
            //WorkBook workBook = new WorkBook(list, true, 0, 1, 0, 1);
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, 6);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate " + "Department list excel report, exception: " + e);
        }
        return null;
    }

    protected EdsUser getUser() {
        return userManager.getUser();
    }

    public void setExcelReferenceMessageSource(WfmResourceBundleMessageSource excelReferenceMessageSource) {
        this.excelReferenceMessageSource = excelReferenceMessageSource;
    }
}
