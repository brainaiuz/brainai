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

/**
 * Created by IntelliJ IDEA.
 * User: Farhod
 * Date: 19-Aug-2010
 * Time: 20:20:37
 * To change this template use File | Settings | File Templates.
 */
public class CompanyGoalListExcelHandler extends BaseExcelHandler {

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
        filename = "HRMS Company Goal";
    }

    @Override
    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        EdsUser user = userManager.getUser();
        EdsCompany edsCompany = user.getCompany();
        ListResult<GoalItem> goalList = hrmsService.getCompanyGoalList(filterParametrs);
        ExcelData[] cellDatas;
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        header.remove(GoalItem.ACTION);

        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);
            EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
            sheetName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.companyGoals);
            List<ExcelData[]> list = new LinkedList<>();
            Map<String, ExcelData> mapColumnHeader = new HashMap<>();
            mapColumnHeader.put(GoalItem.COMPANY_GOAL_LIST_TITLE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.title), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(GoalItem.COMPANY_GOAL_LIST_DESCRIPTION, new ExcelData(commonLocalizer.localize(PdfLocalizationName.description), ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(GoalItem.COMPANY_GOAL_LIST_OUTCOME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.outcome), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(GoalItem.COMPANY_GOAL_LIST_FROM_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.startDate), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(GoalItem.COMPANY_GOAL_LIST_TO_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.endDate), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(GoalItem.COMPANY_GOAL_LIST_STATUS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.status), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(GoalItem.GOAL_LIST_VALIDITY_PERIOD, new ExcelData(commonLocalizer.localize(PdfLocalizationName.validityPeriod), ExcelData.STRING, 15, true, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            CustomFieldsUtils.setCustomFieldsExcelHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);

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
                if (panelTools.getColumnCodeName().contains(GoalItem.COMPANY_GOAL_LIST_TITLE)) {
                    mapColumns.put(GoalItem.COMPANY_GOAL_LIST_TITLE, new ExcelData(item.getTitle() != null ? item.getTitle() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(GoalItem.COMPANY_GOAL_LIST_DESCRIPTION)) {
                    mapColumns.put(GoalItem.COMPANY_GOAL_LIST_DESCRIPTION, new ExcelData(item.getDescription() != null ? item.getDescription() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(GoalItem.COMPANY_GOAL_LIST_FROM_DATE)) {
                    mapColumns.put(GoalItem.COMPANY_GOAL_LIST_FROM_DATE, new ExcelData(item.getFromDate() != null ? dateFormat(item.getFromDate().getNonConvertedDate(), true) : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(GoalItem.COMPANY_GOAL_LIST_TO_DATE)) {
                    mapColumns.put(GoalItem.COMPANY_GOAL_LIST_TO_DATE, new ExcelData(item.getToDate() != null ? dateFormat(item.getToDate().getNonConvertedDate(), true) : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(GoalItem.COMPANY_GOAL_LIST_OUTCOME)) {
                    mapColumns.put(GoalItem.COMPANY_GOAL_LIST_OUTCOME, new ExcelData(item.getOutcome() != null ? item.getOutcome() : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(GoalItem.COMPANY_GOAL_LIST_STATUS)) {
                    mapColumns.put(GoalItem.COMPANY_GOAL_LIST_STATUS, new ExcelData(item.getStatus() != null ? item.getStatus() : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(GoalItem.GOAL_LIST_VALIDITY_PERIOD)) {
                    mapColumns.put(GoalItem.GOAL_LIST_VALIDITY_PERIOD, new ExcelData(item.getValidityPeriodItem() != null ? item.getValidityPeriodItem().getName() : "", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
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
            log.error("Cannot generate company goal list excel report, exception: " + e);
        }
        return null;
    }
}
