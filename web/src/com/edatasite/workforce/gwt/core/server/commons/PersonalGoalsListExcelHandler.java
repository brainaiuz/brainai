package com.edatasite.workforce.gwt.core.server.commons;
//Export Personal Goals
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import org.springframework.context.support.WfmResourceBundleMessageSource;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.LoggerFactory; //logs manager
import org.slf4j.Logger; //logs
import javax.servlet.http.HttpServletRequest; //HttpServletRequest
import java.util.*; //all utils
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.GoalItem;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.shared.poiutils.WorkBook;

//personal goals list export to XLS
public class PersonalGoalsListExcelHandler extends BaseExcelHandler {

    private static final Logger log = LoggerFactory.getLogger(CrmAccountsExcelHandler.class);

    @Autowired
    private HrmsService hrmsService;
    @Autowired
    private PropertManager propertManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;

    public PersonalGoalsListExcelHandler() {
    }

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }




    @Override
    protected void setFileName(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
        filename = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.personalGoals);
    }

    @Override
    protected void setFileName() {

    }

    @Override
    protected HSSFWorkbook getWorkBook(Object object) {

        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        filterParametrs.setAllGoals(true);

        ListResult<GoalItem> goalList = hrmsService.getPersonalGoalList(filterParametrs);
        EdsCompany edsCompany = userManager.getUser().getCompany();

        ExcelData[] cellDatas;
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();

        header.remove(GoalItem.ACTION);

        try {

            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);

            workBook.setSheetName(filename);

            Map<String, ExcelData> mapColumnHeader = new HashMap<>();

            mapColumnHeader.put(GoalItem.GOAL_NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.number), ExcelData.STRING, 15, true, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(GoalItem.GOAL_LIST_TITLE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.title), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(GoalItem.PROJECT_GOAL, new ExcelData(hrmsLocalizer.localize(PdfLocalizationName.projectGoal), ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(GoalItem.GOAL_LIST_TO_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.endDate), ExcelData.STRING, 15, true, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(GoalItem.GOAL_LIST_FROM_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.startDate), ExcelData.STRING, 15, true, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(GoalItem.GOAL_STATUS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.status), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(GoalItem.GOAL_LIST_ASSIGN, new ExcelData(commonLocalizer.localize(PdfLocalizationName.assignee), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

            //custom fields map
            CustomFieldsUtils.setCustomFieldsExcelHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);

            List<ExcelData> excelDataList = new ArrayList<>();

            for (String columnName : panelTools.getColumnCodeName()) {
                if (mapColumnHeader.containsKey(columnName)) {
                    excelDataList.add(mapColumnHeader.get(columnName));
                }
            }

            List<ExcelData[]> list = new LinkedList<>();
            cellDatas = new ExcelData[excelDataList.size()];

            EdsUser user = userManager.getUser();
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size() + 1, edsCompany.getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size() + 1, filename, workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size() + 1, ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " Xolatiga ko'ra" : excelReferenceMessageSource.localize("EPAsOf", " As Of") + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            excelDataList.toArray(cellDatas);
            list.add(cellDatas);


            for (GoalItem item : goalList.getList()) {
                Map<String, ExcelData> mapColumns = new HashMap<>();


                //Goal Number
                if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_NUMBER)) {
                    mapColumns.put(GoalItem.GOAL_NUMBER, new ExcelData(item.getGoalNumber() != null ? item.getGoalNumber().getNumberString() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                //Goal Title
                if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_LIST_TITLE)) {
                    mapColumns.put(GoalItem.GOAL_LIST_TITLE, new ExcelData(item.getTitle() != null ? item.getTitle() : "", ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                //Assign
                if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_LIST_ASSIGN)) {
                    mapColumns.put(GoalItem.GOAL_LIST_ASSIGN, new ExcelData(item.getGoalAssignedTo() != null ? item.getGoalAssignedTo() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                //Project Goal
                if (panelTools.getColumnCodeName().contains(GoalItem.PROJECT_GOAL)) {
                    mapColumns.put(GoalItem.PROJECT_GOAL, new ExcelData(item.getProjectGoalTitle() != null ? item.getProjectGoalTitle() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }


              //Start Date
               if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_LIST_FROM_DATE)) {
                    mapColumns.put(GoalItem.GOAL_LIST_FROM_DATE, new ExcelData(item.getFromDate() != null ? ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat(item.getFromDate().getNonConvertedDate(), true)) : dateFormat(item.getFromDate().getNonConvertedDate(), true) : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

               //End Date
                if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_LIST_TO_DATE)) {
                    mapColumns.put(GoalItem.GOAL_LIST_TO_DATE, new ExcelData(item.getToDate() != null ? ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat(item.getToDate().getNonConvertedDate(), true)) : dateFormat(item.getToDate().getNonConvertedDate(), true) : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                //Status
                if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_STATUS)) {
                    mapColumns.put(GoalItem.GOAL_STATUS, new ExcelData(item.getStatus() != null ? item.getStatus() : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
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
            log.error("Cannot generate personal goal list excel report, due to exception: " + e);
        }

        return null;
    }


}
