package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.GoalItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
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


public class DepartmentGoalListExcelHandler extends BaseExcelHandler {
    private static final Logger log = LoggerFactory.getLogger(DepartmentGoalListExcelHandler.class);
  //  HrmsStrings hrmsStrings= HrmsStrings.App.get();
    @Autowired
    private HrmsService hrmsService;
    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;
    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }

    @Autowired
    private UserManager userManager;
    @Autowired
    private PropertManager propertManager;
    @Override
    protected void setFileName(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
        filename = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.departmentGoals); ;
    }
    @Override
    protected void setFileName() {

    }
    @Override
    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        EdsUser user = userManager.getUser();
        EdsCompany edsCompany = user.getCompany();
        filterParametrs.setAllGoals(true);
        ListResult<GoalItem> goalList = hrmsService.getDepartmentGoalList(filterParametrs);//crmService.getContactList(filterParametrs, config);
        ExcelData[] cellDatas;
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        header.remove(GoalItem.ACTION);

        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);

            List<ExcelData[]> list = new LinkedList<>();

            Map<String, ExcelData> mapColumnHeader = new HashMap<>();

            //number
            mapColumnHeader.put(GoalItem.GOAL_NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.number), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

            //Title
            mapColumnHeader.put(GoalItem.GOAL_LIST_TITLE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.title), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

            //Start Date
            mapColumnHeader.put(GoalItem.GOAL_LIST_FROM_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.startDate), ExcelData.STRING, 12, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

            //End Date
            mapColumnHeader.put(GoalItem.GOAL_LIST_TO_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.endDate), ExcelData.STRING, 12, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

            //Manager
            mapColumnHeader.put(GoalItem.GOAL_LIST_RESOVER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.manager), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

            //Department goal
            mapColumnHeader.put(GoalItem.GOAL_LIST_DEPARTMENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.departmentGoal), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

            //Status
            mapColumnHeader.put(GoalItem.GOAL_STATUS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.status), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

            //Company Goal
            mapColumnHeader.put(GoalItem.COMPANY_GOAL_LIST_TITLE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.companyGoal), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

            //Custom Fields
            CustomFieldsUtils.setCustomFieldsExcelHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader); //mapping custom fields

            // Set excel header
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), edsCompany.getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), filename, workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " Xolatiga ko'ra" : excelReferenceMessageSource.localize("EPAsOf", " As Of") + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            List<ExcelData> excelDataList = new ArrayList<>();
            for (String columnName : panelTools.getColumnCodeName()) {
                if (mapColumnHeader.containsKey(columnName)) {
                    excelDataList.add(mapColumnHeader.get(columnName));
                }
            }
            cellDatas = new ExcelData[excelDataList.size()];
            excelDataList.toArray(cellDatas);
            list.add(cellDatas);

            for (GoalItem item : goalList.getList()) {
                Map<String, ExcelData> mapColumns = new HashMap<>();

                //Number
                if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_NUMBER)) {
                    mapColumns.put(GoalItem.GOAL_NUMBER, new ExcelData(item.getGoalNumber() != null ? item.getGoalNumber().getNumberString() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                //Goal Title
                if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_LIST_TITLE)) {
                    mapColumns.put(GoalItem.GOAL_LIST_TITLE, new ExcelData(item.getTitle() != null ? item.getTitle() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                //Start Date
                if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_LIST_FROM_DATE)) {
                    mapColumns.put(GoalItem.GOAL_LIST_FROM_DATE, new ExcelData(item.getFromDate() != null ? dateFormat(item.getFromDate().getNonConvertedDate(), true) : "", ExcelData.STRING, 12, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                //End Date
                if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_LIST_TO_DATE)) {
                    mapColumns.put(GoalItem.GOAL_LIST_TO_DATE, new ExcelData(item.getToDate() != null ? dateFormat(item.getToDate().getNonConvertedDate(), true) : "", ExcelData.STRING, 12, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                //Manager
                if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_LIST_RESOVER)) {
                    mapColumns.put(GoalItem.GOAL_LIST_RESOVER, new ExcelData(item.getResolver() != null ? item.getResolver() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                //Department
                if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_LIST_DEPARTMENT)) {
                    mapColumns.put(GoalItem.GOAL_LIST_DEPARTMENT, new ExcelData(item.getDepartment() != null ? item.getDepartment() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                //Status
                if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_STATUS)) {
                    mapColumns.put(GoalItem.GOAL_STATUS, new ExcelData(item.getStatus() != null ? item.getStatus() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                //Company goal
                if (panelTools.getColumnCodeName().contains(GoalItem.COMPANY_GOAL_LIST_TITLE)) {
                    mapColumns.put(GoalItem.COMPANY_GOAL_LIST_TITLE, new ExcelData(item.getCompanyGoal() != null ? item.getCompanyGoal() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
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
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, 6);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate department goals list excel file, due to exception: " + e);
        }
        return null;
    }
}
