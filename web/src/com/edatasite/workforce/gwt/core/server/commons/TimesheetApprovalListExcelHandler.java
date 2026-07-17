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
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimeSheetApprovalListItem;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * User: Ilhombek
 * Date: 29.08.2009
 * Time: 19:23:15
 */
public class TimesheetApprovalListExcelHandler extends BaseExcelHandler {

    private static final Logger log = LoggerFactory.getLogger(TimesheetApprovalListExcelHandler.class);

    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;
    @Autowired
    private TimesheetService timesheetService;

    @Autowired
    private PropertManager propertManager;
    @Autowired
    private UserManager userManager;

    @Override
    protected void setFileName() {
        filename = "Timesheet Approval List";
    }

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParameters = (ListingFilterParameter) object;
        EdsProperty property = propertManager.findByCode(filterParameters.getPropertyCode());
        String sheetName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.timesheetApproval);
        filterParameters.setLimit(1000);
        ListResult<TimeSheetApprovalListItem> positionList = timesheetService.getTimeSheetApprovalSessionList(filterParameters);
        EdsUser user = userManager.getUser();
        EdsCompany edsCompany = user.getCompany();
        List<TimeSheetApprovalListItem> solutionListItems = positionList.getList();
        ExcelData[] cellDatas;
        ListPanelToolRpc panelTools = filterParameters.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        if (header.contains("action")) {
            header.remove("action");
        } else {
            header.remove("action");
        }
        header.remove(TimeSheetApprovalListItem.ACTION);
        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(TimeSheetApprovalListItem.EMPLOYEENAME, commonLocalizer.localize(PdfLocalizationName.employee));
        mapColumnHeader.put(TimeSheetApprovalListItem.STATUS, commonLocalizer.localize(PdfLocalizationName.status));
        mapColumnHeader.put(TimeSheetApprovalListItem.PROJECTNAME, commonLocalizer.localize(PdfLocalizationName.project));
        mapColumnHeader.put(TimeSheetApprovalListItem.FROMDATE, commonLocalizer.localize(PdfLocalizationName.period));
        mapColumnHeader.put(TimeSheetApprovalListItem.APPROVER, commonLocalizer.localize(PdfLocalizationName.approvers));
        mapColumnHeader.put(TimeSheetApprovalListItem.SUBMITTED_DATE, commonLocalizer.localize(PdfLocalizationName.submittedDate));
        mapColumnHeader.put(TimeSheetApprovalListItem.APPROVAL_DATE, commonLocalizer.localize(PdfLocalizationName.approvedDate));
        mapColumnHeader.put(TimeSheetApprovalListItem.TIMESPENT, commonLocalizer.localize(PdfLocalizationName.timeSpentOnly));
        mapColumnHeader.put(TimeSheetApprovalListItem.APPROVED, commonLocalizer.localize(PdfLocalizationName.approved));
        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);

            List<ExcelData[]> list = new LinkedList<>();
            cellDatas = new ExcelData[header.size()];

            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), edsCompany.getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), sheetName, workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertDateFormatFromEngToUzb(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " Xolatiga ko'ra" : commonLocalizer.localize(PdfLocalizationName.asOF) + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            for (int i = 0; i < header.size(); i++) {
                cellDatas[i] = new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, header.get(i).equals(TimeSheetApprovalListItem.EMPLOYEENAME) || header.get(i).equals(TimeSheetApprovalListItem.STATUS) ? 50 : 20, false, header.get(i).equals(TimeSheetApprovalListItem.PROJECTNAME) || header.get(i).equals(TimeSheetApprovalListItem.FROMDATE) || header.get(i).equals(TimeSheetApprovalListItem.ENDDATE), ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            list.add(cellDatas);
            for (TimeSheetApprovalListItem timeSheetApprovals : solutionListItems) {
                String temp;
                cellDatas = new ExcelData[header.size()];
                for (int j = 0; j < header.size(); j++) {
                    temp = "";
                    if (TimeSheetApprovalListItem.EMPLOYEENAME.equals(header.get(j))) {
                        temp = timeSheetApprovals.getEmployeeName() == null ? "" : timeSheetApprovals.getEmployeeName();
                    } else if (TimeSheetApprovalListItem.STATUS.equals(header.get(j))) {
                        temp = timeSheetApprovals.getStatus() == null ? "" : timeSheetApprovals.getStatus();
                    } else if (TimeSheetApprovalListItem.PROJECTNAME.equals(header.get(j))) {
                        temp = timeSheetApprovals.getProjectName() == null ? "" : timeSheetApprovals.getProjectName();
                    } else if (TimeSheetApprovalListItem.FROMDATE.equals(header.get(j))) {
                        if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                            temp = (timeSheetApprovals.getFromDate() != null && timeSheetApprovals.getEndDate() != null) ?
                                    (ServerUtils.convertToUzbDateFormat(dateFormat(timeSheetApprovals.getFromDate().getNonConvertedDate(), true)) + "-" + ServerUtils.convertToUzbDateFormat(dateFormat(timeSheetApprovals.getEndDate().getNonConvertedDate(), true))) : "";
                        } else {
                            temp = (timeSheetApprovals.getFromDate() != null && timeSheetApprovals.getEndDate() != null) ?
                                    (dateFormat(timeSheetApprovals.getFromDate().getNonConvertedDate(), true) + "-" + dateFormat(timeSheetApprovals.getEndDate().getNonConvertedDate(), true)) : "";
                        }
                    } else if (TimeSheetApprovalListItem.APPROVER.equals(header.get(j))) {
                        temp = timeSheetApprovals.getApprover() == null ? "" : timeSheetApprovals.getApprover();
                    } else if (TimeSheetApprovalListItem.SUBMITTED_DATE.equals(header.get(j))) {
                        if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                            temp = timeSheetApprovals.getSubmittedDate() == null ? "" : ServerUtils.convertToUzbDateFormat(dateFormat(timeSheetApprovals.getSubmittedDate().getNonConvertedDate(), true));
                        } else {
                            temp = timeSheetApprovals.getSubmittedDate() == null ? "" : dateFormat(timeSheetApprovals.getSubmittedDate().getNonConvertedDate(), true);

                        }
                    } else if (TimeSheetApprovalListItem.APPROVAL_DATE.equals(header.get(j))) {
                        if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                            temp = timeSheetApprovals.getApprovalDate() == null ? "" : ServerUtils.convertToUzbDateFormat(dateFormat(timeSheetApprovals.getApprovalDate().getNonConvertedDate(), true));
                        } else {
                            temp = timeSheetApprovals.getApprovalDate() == null ? "" : dateFormat(timeSheetApprovals.getApprovalDate().getNonConvertedDate(), true);
                        }
                    } else if (TimeSheetApprovalListItem.TIMESPENT.equals(header.get(j))) {
                        temp = timeSheetApprovals.getTimeSpent() == null ? "" : timeSheetApprovals.getTimeSpent();
                    } else if (TimeSheetApprovalListItem.APPROVED.equals(header.get(j))) {
                        temp = timeSheetApprovals.getApprovedHours() == null ? "" : timeSheetApprovals.getApprovedHours();
                    }
                    cellDatas[j] = new ExcelData(temp, ExcelData.STRING, header.get(j).equals(TimeSheetApprovalListItem.EMPLOYEENAME) || header.get(j).equals(TimeSheetApprovalListItem.STATUS) ? 50 : 20, false, !header.get(j).equals(TimeSheetApprovalListItem.PROJECTNAME), ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                list.add(cellDatas);
            }
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, header.size());

        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate timeSheet approval list excel report, exception: " + e);
        }
        return null;
    }

    public void setExcelReferenceMessageSource(WfmResourceBundleMessageSource excelReferenceMessageSource) {
        this.excelReferenceMessageSource = excelReferenceMessageSource;
    }
}