package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.gwt.core.client.rpc.GoalItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.meetingMinutes.client.rpc.MeetingMinutesItem;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 23.07.2009
 * Time: 16:18:58
 * To change this template use File | Settings | File Templates.
 */
public class EmployeesGoalsListExcelHandler extends BaseExcelHandler{

    private static final Logger log = LoggerFactory.getLogger(CrmAccountsExcelHandler.class);

    @Autowired
    private HrmsService hrmsService;
    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;
    public static final String NAME_ = "name";
    public static final String RELATED_TO_ = "relatedTo";
    public static final String TIMESHEET_ = "timesheet";

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        filterParametrs.setAllGoals(true);
        ListResult<GoalItem> goalList = hrmsService.getOwnGoalList(filterParametrs);
        List<GoalItem> goalListItems = goalList.getList();
        ExcelData[] cellDatas;
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        header.remove("action");
        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(GoalItem.GOAL_NUMBER, commonLocalizer.localize(PdfLocalizationName.number));
        mapColumnHeader.put(GoalItem.EMPLOYEE_GOAL_LIST_GOAL_CATEGORY, commonLocalizer.localize(PdfLocalizationName.goalCategory));
        mapColumnHeader.put(GoalItem.PROJECT_GOAL, commonLocalizer.localize(PdfLocalizationName.project));
        mapColumnHeader.put(GoalItem.EMPLOYEE_GOAL_LIST_STATUS, commonLocalizer.localize(PdfLocalizationName.status));
        mapColumnHeader.put(GoalItem.EMPLOYEE_GOAL_LIST_TITLE, commonLocalizer.localize(PdfLocalizationName.title));
        mapColumnHeader.put(GoalItem.EMPLOYEE_GOAL_LIST_DESCRIPTION, commonLocalizer.localize(PdfLocalizationName.description));
        mapColumnHeader.put(GoalItem.EMPLOYEE_GOAL_LIST_WEIGHT, commonLocalizer.localize(PdfLocalizationName.score));
        mapColumnHeader.put(GoalItem.EMPLOYEE_GOAL_LIST_ACTIONSTEPS, commonLocalizer.localize(PdfLocalizationName.actionSteps));
        mapColumnHeader.put(GoalItem.EMPLOYEE_GOAL_LIST_RESOLVER, commonLocalizer.localize(PdfLocalizationName.resolver));
        
        try {
            List<ExcelData[]> list = new LinkedList<>();
            cellDatas = new ExcelData[header.size()];

            for (int i = 0; i < header.size(); i++) {
                cellDatas[i] = new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }

            list.add(cellDatas);

            for (GoalItem goal : goalListItems) {
                String temp = "";
                cellDatas = new ExcelData[header.size()];
                for (int ii = 0; ii < header.size(); ii++) {
                    if (GoalItem.GOAL_NUMBER.equals(header.get(ii))) {
                        temp = goal.getGoalNumber() != null ? goal.getGoalNumber().getNumberString() : "";
                    } else if (GoalItem.EMPLOYEE_GOAL_LIST_GOAL_CATEGORY.equals(header.get(ii))) {
                        temp = goal.getGoalCategory() != null ? goal.getGoalCategory() : "";
                    } else if (GoalItem.PROJECT_GOAL.equals(header.get(ii))) {
                        temp = goal.getProjectGoalTitle() != null ? goal.getProjectGoalTitle() : "";
                    }else if (GoalItem.EMPLOYEE_GOAL_LIST_STATUS.equals(header.get(ii))) {
                        temp = goal.getStatus() != null ? goal.getStatus() : "";
                    } else if (GoalItem.EMPLOYEE_GOAL_LIST_TITLE.equals(header.get(ii))) {
                        temp = goal.getTitle();
                    } else if (GoalItem.EMPLOYEE_GOAL_LIST_DESCRIPTION.equals(header.get(ii))) {
                        temp = goal.getDescription();
                    } else if (GoalItem.EMPLOYEE_GOAL_LIST_WEIGHT.equals(header.get(ii))) {
                        temp = goal.getWeight() + "";
                    } else if (GoalItem.EMPLOYEE_GOAL_LIST_ACTIONSTEPS.equals(header.get(ii))) {
                        temp = goal.getActionSteps() != null ? goal.getActionSteps() : "";
                    } else if (GoalItem.EMPLOYEE_GOAL_LIST_RESOLVER.equals(header.get(ii))) {
                        temp = goal.getResolver() != null ? goal.getResolver() : "";
                    }
                    cellDatas[ii] = new ExcelData(temp, ExcelData.STRING, 20, false, !header.get(ii).equals(MeetingMinutesItem.TYPE), ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                list.add(cellDatas);

            }
            return new WorkBook(list).getWorkBook(filename, 0, 0, 0, header.size());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot ganarate issue list excel report, exception: " + e);
        }

        return null;
    }

    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }
    @Override
    protected void setFileName() {
        filename = "Employees Goal";
    }
}
