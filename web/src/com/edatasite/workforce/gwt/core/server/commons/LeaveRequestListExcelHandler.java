package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.LeaveRequestLisItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class LeaveRequestListExcelHandler extends BaseExcelHandler implements Constants {

    @Autowired
    private AvailabilityService availabilityService;

    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;
    @Autowired
    private PropertManager propertManager;
    private String sheetName;

    private static final Logger log = LoggerFactory.getLogger(TimeSlotListExcelHandler.class);

    @Override
    protected void setFileName() {
        this.filename = "LeaveRequests";
    }

    protected HSSFWorkbook getWorkBook(Object ob) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) ob;
        EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
        sheetName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.leaveRequests);
        filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        EdsUser user = userManager.getUser();
        ListResult<LeaveRequestLisItem> leaveRequestListResult = availabilityService.getLeaveRequestList(filterParametrs);
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        ExcelData[] cellDatas;
        EdsCompany edsCompany = userManager.getUser().getCompany();
        Map<String, ExcelData> mapColumnHeader = new HashMap<>();

        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);
            List<ExcelData[]> list = new LinkedList<>();

            mapColumnHeader.put(LeaveRequestLisItem.CODE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.number), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(LeaveRequestLisItem.EMPLOYEE_NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.employee), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(LeaveRequestLisItem.REASON, new ExcelData(commonLocalizer.localize(PdfLocalizationName.reason), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(LeaveRequestLisItem.FROM_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.from), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(LeaveRequestLisItem.TO_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.to), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(LeaveRequestLisItem.STATUS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.status), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(LeaveRequestLisItem.APPROVER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.approver), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(LeaveRequestLisItem.CREATED_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.createdDate), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(LeaveRequestLisItem.REGISTERED_BY, new ExcelData(commonLocalizer.localize("registeredBy"), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(LeaveRequestLisItem.LEAVE_DAYS, new ExcelData(commonLocalizer.localize("leaveDays"), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(LeaveRequestLisItem.TYPE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.type), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(LeaveRequestLisItem.DESCRIPTION, new ExcelData(commonLocalizer.localize(PdfLocalizationName.description), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(LeaveRequestLisItem.DEPARTMENT, new ExcelData(commonLocalizer.localize(PdfLocalizationName.department), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(LeaveRequestLisItem.POSITION, new ExcelData(commonLocalizer.localize(PdfLocalizationName.position), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));

            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), edsCompany.getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), sheetName, workBook.getSheet(), 1));
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

            for (LeaveRequestLisItem item : leaveRequestListResult.getList()) {
                Map<String, ExcelData> mapColumns = new HashMap<>();

                if (panelTools.getColumnCodeName().contains(LeaveRequestLisItem.CODE)) {
                    mapColumns.put(LeaveRequestLisItem.CODE, new ExcelData(item.getLeaveRequestCode(), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                if (panelTools.getColumnCodeName().contains(LeaveRequestLisItem.EMPLOYEE_NAME)) {
                    mapColumns.put(LeaveRequestLisItem.EMPLOYEE_NAME, new ExcelData(item.getEmployeeName(), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                if (panelTools.getColumnCodeName().contains(LeaveRequestLisItem.REASON)) {
                    mapColumns.put(LeaveRequestLisItem.REASON, new ExcelData(item.getReason(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                if (panelTools.getColumnCodeName().contains(LeaveRequestLisItem.FROM_DATE)) {
                    String startDate = "--";
                    if (item.getStartDate() != null) {
                        if (item.isAllDay()) {
                            startDate = ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat(item.getStartDate().getNonConvertedDate())) : dateFormat(item.getStartDate().getNonConvertedDate());
                        } else {
                            startDate = ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(longDateFormat(item.getStartDate().getNonConvertedDate(), true)) : longDateFormat(item.getStartDate().getNonConvertedDate(), true);
                        }
                    }
                    mapColumns.put(LeaveRequestLisItem.FROM_DATE, new ExcelData(startDate, ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                if (panelTools.getColumnCodeName().contains(LeaveRequestLisItem.TO_DATE)) {
                    String enddate = "--";
                    if (item.getEndDate() != null) {
                        if (item.isAllDay()) {
                            enddate = ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat(item.getEndDate().getNonConvertedDate())) : dateFormat(item.getEndDate().getNonConvertedDate());
                        } else {
                            enddate = ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(longDateFormat(item.getEndDate().getNonConvertedDate(), true)) : longDateFormat(item.getEndDate().getNonConvertedDate(), true);
                        }
                    }
                    mapColumns.put(LeaveRequestLisItem.TO_DATE, new ExcelData(enddate, ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                if (panelTools.getColumnCodeName().contains(LeaveRequestLisItem.STATUS)) {
                    mapColumns.put(LeaveRequestLisItem.STATUS, new ExcelData(item.getStatus(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                if (panelTools.getColumnCodeName().contains(LeaveRequestLisItem.APPROVER)) {
                    mapColumns.put(LeaveRequestLisItem.APPROVER, new ExcelData(item.getApproverName(), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(LeaveRequestLisItem.CREATED_DATE)) {
                    mapColumns.put(LeaveRequestLisItem.CREATED_DATE, new ExcelData(item.getCreatedDate() != null ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(longDateFormat(item.getCreatedDate())) : longDateFormat(item.getCreatedDate())) : "", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

                if (panelTools.getColumnCodeName().contains(LeaveRequestLisItem.REGISTERED_BY)) {
                    mapColumns.put(LeaveRequestLisItem.REGISTERED_BY, new ExcelData(item.getCreator(), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(LeaveRequestLisItem.LEAVE_DAYS)) {
                    mapColumns.put(LeaveRequestLisItem.LEAVE_DAYS, new ExcelData(item.getLeaveDays(), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(LeaveRequestLisItem.TYPE)) {
                    mapColumns.put(LeaveRequestLisItem.TYPE, new ExcelData(item.getType(), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(LeaveRequestLisItem.DESCRIPTION)) {
                    mapColumns.put(LeaveRequestLisItem.DESCRIPTION, new ExcelData(item.getDescription(), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(LeaveRequestLisItem.DEPARTMENT)) {
                    mapColumns.put(LeaveRequestLisItem.DEPARTMENT, new ExcelData(item.getDepartment(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(LeaveRequestLisItem.POSITION)) {
                    mapColumns.put(LeaveRequestLisItem.POSITION, new ExcelData(item.getPosition(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }

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
            return workBook.getWorkBook(filename, 0, 0, 0, mapColumnHeader.size());


        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate " + "LR list excel report, exception: " + e);
        }
        return null;
    }
}
