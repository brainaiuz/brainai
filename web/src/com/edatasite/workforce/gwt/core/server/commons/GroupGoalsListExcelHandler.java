package com.edatasite.workforce.gwt.core.server.commons;
//Group Goal List export to XLS

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
import com.edatasite.workforce.gwt.hrms.client.rpc.GroupGoalITem;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.meetingMinutes.client.rpc.MeetingMinutesItem;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GroupGoalsListExcelHandler extends BaseExcelHandler {
    @Autowired
    private PropertManager propertManager;
    @Autowired
    private HrmsService hrmsService;
    private static final Logger log = LoggerFactory.getLogger(CrmAccountsExcelHandler.class);

    protected HSSFWorkbook getWorkBook(Object object) {

        EdsUser user = userManager.getUser();
        EdsCompany edsCompany = user.getCompany();

        ListingFilterParameter filterParameter = (ListingFilterParameter) object;
        filterParameter.setLimit(LIMIT_EXCEL_ROW);

        ListResult<GroupGoalITem> goalList = hrmsService.getGroupGoalList(filterParameter);
        List<GroupGoalITem> goalListItems = goalList.getList();

        ExcelData[] cellDatas;
        ListPanelToolRpc panelToolRpc = filterParameter.getListPanelTool();


        List<String> header = panelToolRpc.getColumnCodeName();

        header.remove(GroupGoalITem.ACTION);

        try {

            WorkBook xlsFile = new WorkBook(true, 0, 1, 0, 1);
            xlsFile.setSheetName(filename);

            Map<String, String> mapColumnHeader = new HashMap<>();
            mapColumnHeader.put(GroupGoalITem.GROUP_GOAL_APPROVER, commonLocalizer.localize(PdfLocalizationName.approver));
            mapColumnHeader.put(GroupGoalITem.GROUP_GOAL_EMPLOYEE, commonLocalizer.localize(PdfLocalizationName.employee));
            mapColumnHeader.put(GroupGoalITem.GROUP_GOAL_STATUS, commonLocalizer.localize(PdfLocalizationName.status));
            mapColumnHeader.put(GroupGoalITem.TO_DATE, commonLocalizer.localize(PdfLocalizationName.endDate));
            mapColumnHeader.put(GroupGoalITem.VALIDITY_PERIOD, hrmsLocalizer.localize(PdfLocalizationName.validityPeriod));
            mapColumnHeader.put(GroupGoalITem.FROM_DATE, commonLocalizer.localize(PdfLocalizationName.startDate));

            List<ExcelData[]> list = new LinkedList<>();
            list.add(generateOneRowWithValue(panelToolRpc.getColumnCodeName().size(), edsCompany.getName(), xlsFile.getSheet(), 0));
            list.add(generateOneRowWithValue(panelToolRpc.getColumnCodeName().size(), xlsFile.getSheet().getSheetName() ,xlsFile.getSheet(), 1));
            list.add(generateOneRowWithValue(panelToolRpc.getColumnCodeName().size(), ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " Xolatiga ko'ra" : commonLocalizer.localize(PdfLocalizationName.asOF) + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), xlsFile.getSheet(), 2));

            cellDatas = new ExcelData[header.size()];

            for (int i = 0; i < header.size(); i++) {
                cellDatas[i] = new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }


            for (int i = 0; i < header.size(); i++) {
                cellDatas[i] = new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            list.add(cellDatas);

            for (GroupGoalITem goal : goalListItems) {
                String temp = "";
                cellDatas = new ExcelData[header.size()];

                for (int j = 0; j < header.size(); j++) {
                    if (GroupGoalITem.GROUP_GOAL_APPROVER.equals(header.get(j))) {
                        temp = goal.getApprover().getName();
                    } else if (GroupGoalITem.GROUP_GOAL_STATUS.equals(header.get(j))) {
                        temp = goal.getStatus().getName();
                    } else if (GroupGoalITem.GROUP_GOAL_EMPLOYEE.equals(header.get(j))) {
                        temp = goal.getEmployee().getName();
                    } else if (GroupGoalITem.FROM_DATE.equals(header.get(j))) {
                        temp = dateFormat(user.getUserDate(goal.getFromDate().getNonConvertedDate()), true);
                    } else if (GroupGoalITem.TO_DATE.equals(header.get(j))) {
                        temp = dateFormat(user.getUserDate(goal.getToDate().getNonConvertedDate()), true);
                    } else if (GroupGoalITem.VALIDITY_PERIOD.equals(header.get(j))) {
                        temp = goal.getValidityPeriod().getName();
                    }
                    cellDatas[j] = new ExcelData(temp, ExcelData.STRING, 20, false, !header.get(j).equals(MeetingMinutesItem.TYPE), ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                list.add(cellDatas);
            }

            xlsFile.setList(list);
            return xlsFile.getWorkBook(filename, 0, 0, 0, header.size());

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate group goals list excel file, due to exception: " + e);
        }
        return null;

    }


    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }


    @Override //default method
    protected void setFileName() {   }
    @Override //set filename dynamic-localized
    protected void setFileName(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
        filename = property != null ? property.getPlural() : hrmsLocalizer.localize(PdfLocalizationName.groupGoals);
    }
}
