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
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;


public class ProjectGoalListExcelHandler extends BaseExcelHandler {

    private static final Logger log = LoggerFactory.getLogger(CrmAccountsExcelHandler.class);

    @Autowired
    private HrmsService hrmsService;
    @Autowired
    private UserManager userManager;
    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;
    @Autowired
    private PropertManager propertManager;
    private String sheetName;

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }
    @Override
    protected void setFileName() {
        filename = "Project Goals";
    }
    @Override
    protected HSSFWorkbook getWorkBook(Object object) {

        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        filterParametrs.setLimit(LIMIT_EXCEL_ROW);

        EdsUser user = userManager.getUser();
        EdsCompany edsCompany = user.getCompany();

        filterParametrs.setAllGoals(true);
        ListResult<GoalItem> goalList = hrmsService.getProjectGoalList(filterParametrs);
        ExcelData[] cellDatas;

        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        header.remove(GoalItem.ACTION);

        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);
            EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
            sheetName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.projectGoals);
            List<ExcelData[]> list = new LinkedList<>();

            Map<String, ExcelData> mapColumnHeader = new HashMap<>();

            //Goal Number
            mapColumnHeader.put(GoalItem.GOAL_NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.number), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

            //Goal Title
            mapColumnHeader.put(GoalItem.GOAL_LIST_TITLE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.title), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

            //Project Goal
            mapColumnHeader.put(GoalItem.PROJECT_GOAL, new ExcelData(commonLocalizer.localize(PdfLocalizationName.projectGoals), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

            //Description
            mapColumnHeader.put(GoalItem.GOAL_LIST_DESCRIPTION, new ExcelData(commonLocalizer.localize(PdfLocalizationName.description), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

            //Start Date
            mapColumnHeader.put(GoalItem.GOAL_LIST_FROM_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.startDate), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

            //End Date
            mapColumnHeader.put(GoalItem.GOAL_LIST_TO_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.endDate), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

            //Manager
            mapColumnHeader.put(GoalItem.GOAL_LIST_RESOVER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.manager), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

            //Validity period
            mapColumnHeader.put(GoalItem.GOAL_LIST_VALIDITY_PERIOD, new ExcelData(commonLocalizer.localize(PdfLocalizationName.validityPeriod), ExcelData.STRING, 15, true, true, ExcelData.NO_BORDER, ExcelData.HEADER));

            //Company Goal
            mapColumnHeader.put(GoalItem.GOAL_LIST_STRATEGIC, new ExcelData(commonLocalizer.localize(PdfLocalizationName.companyGoal), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

            //Weighted Score
            mapColumnHeader.put(GoalItem.GOAL_LIST_WEIGHT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.weight), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

            //Assigned
         // mapColumnHeader.put(GoalItem.GOAL_LIST_ASSIGN, new ExcelData(commonLocalizer.localize(PdfLocalizationName.assigneedTo), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

            CustomFieldsUtils.setCustomFieldsExcelHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);  //Additional Fields

            //data 1-3 rows in excel
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size() + 1, edsCompany.getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size() + 1, sheetName, workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size() + 1, ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " Xolatiga ko'ra" : excelReferenceMessageSource.localize("EPAsOf", " As Of") + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

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
                    mapColumns.put(GoalItem.GOAL_NUMBER, new ExcelData(item.getTitle() != null ? item.getGoalNumber().getNumberString() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                //Title
                if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_LIST_TITLE)) {
                    mapColumns.put(GoalItem.GOAL_LIST_TITLE, new ExcelData(item.getTitle() != null ? item.getTitle() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                //Project Goal
                if (panelTools.getColumnCodeName().contains(GoalItem.PROJECT_GOAL)) {
                    mapColumns.put(GoalItem.PROJECT_GOAL, new ExcelData(item.getProjectGoals() != null ? item.getProjectGoals() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                //Description
                if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_LIST_DESCRIPTION)) {
                    mapColumns.put(GoalItem.GOAL_LIST_DESCRIPTION, new ExcelData(item.getDescription() != null ? item.getDescription() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                //Start Date
                if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_LIST_FROM_DATE)) {
                    mapColumns.put(GoalItem.GOAL_LIST_FROM_DATE, new ExcelData(item.getFromDate() != null ? dateFormat(item.getFromDate().getNonConvertedDate(), true) : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                //End Date
                if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_LIST_TO_DATE)) {
                    mapColumns.put(GoalItem.GOAL_LIST_TO_DATE, new ExcelData(item.getToDate() != null ? dateFormat(item.getToDate().getNonConvertedDate(), true) : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                //Manager
                if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_LIST_RESOVER)) {
                    mapColumns.put(GoalItem.GOAL_LIST_RESOVER, new ExcelData(item.getResolver() != null ? item.getResolver() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                //Validity Period
                if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_LIST_VALIDITY_PERIOD)) {
                    mapColumns.put(GoalItem.GOAL_LIST_VALIDITY_PERIOD, new ExcelData(item.getValidityPeriodItem() != null ? item.getValidityPeriodItem().getName() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }


                //Company Goal
                if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_LIST_STRATEGIC)) {
                    mapColumns.put(GoalItem.GOAL_LIST_STRATEGIC, new ExcelData(item.getCompanyGoal() != null ? item.getCompanyGoal() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                //Project Goal
                if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_LIST_WEIGHT)) {
                    mapColumns.put(GoalItem.GOAL_LIST_WEIGHT, new ExcelData(item.getWeight() + "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

//                //Assigned
//                if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_LIST_ASSIGN)) {
//                    mapColumns.put(GoalItem.GOAL_LIST_ASSIGN, new ExcelData(item.getGoalAssignedTo() != null ? item.getGoalAssignedTo() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
//                }


                CustomFieldsUtils.setCustomFieldsExcelTableRows(panelTools.getListViewCustomFields(), mapColumns, panelTools.getColumnCodeName(), item, edsCompany); //Additional Fields, Custom fields

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
            log.error("Cannot generate project goals list, due to exception: " + e);
        }
        return null;
    }
}
