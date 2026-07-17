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
import com.edatasite.workforce.gwt.hrms.client.rpc.GroupPlacementItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
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

import static com.edatasite.workforce.gwt.core.client.ui.Constants.GROUP_PLACEMENT_APPROVED;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.GROUP_PLACEMENT_DRAFT;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.GROUP_PLACEMENT_REJECTED;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.GROUP_PLACEMENT_SUBMITTED;

public class GroupPlacementListExcelHandler extends BaseExcelHandler {
    private static final Logger log = LoggerFactory.getLogger(VacancyListExcelHandler.class.getName());

    @Autowired
    private HrmsService hrmsService;

    @Autowired
    private PropertManager propertManager;

    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;

    protected HSSFWorkbook getWorkBook(Object dataClass) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        EdsUser user = userManager.getUser();
        ListResult<GroupPlacementItem> placementItemListResult = hrmsService.getGroupPlacementList(filterParametrs);

        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        ExcelData[] cellDatas;
        EdsCompany edsCompany = getUser().getCompany();
        Map<String, ExcelData> mapColumnHeader = new HashMap<>();
        String sheetName;
        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);
            EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
            sheetName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.vacanciesOnly);
            List<ExcelData[]> list = new LinkedList<>();

            mapColumnHeader.put(GroupPlacementItem.NUMBER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.number), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(GroupPlacementItem.STATUS, new ExcelData(commonLocalizer.localize(PdfLocalizationName.status), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(GroupPlacementItem.APPROVER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.approver), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(GroupPlacementItem.DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.date), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(GroupPlacementItem.CREATOR, new ExcelData(commonLocalizer.localize(PdfLocalizationName.creator), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(GroupPlacementItem.CREATED_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.createdDate), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(GroupPlacementItem.UPDATER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.modifiedBy), ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(GroupPlacementItem.UPDATED_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.modifiedDate), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));


            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), edsCompany.getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), commonLocalizer.localize(PdfLocalizationName.placement), workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " Xolatiga ko'ra" : " " + excelReferenceMessageSource.localize("EPAsOf", " As Of") + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            List<ExcelData> excelDataList = new ArrayList<>();
            for (String columnName : panelTools.getColumnCodeName()) {
                if (mapColumnHeader.containsKey(columnName)) {
                    excelDataList.add(mapColumnHeader.get(columnName));
                }
            }
            cellDatas = new ExcelData[excelDataList.size()];
            excelDataList.toArray(cellDatas);
            list.add(cellDatas);

            for (GroupPlacementItem item : placementItemListResult.getList()) {
                Map<String, ExcelData> mapColumns = new HashMap<>();

                if (panelTools.getColumnCodeName().contains(GroupPlacementItem.NUMBER)) {
                    mapColumns.put(GroupPlacementItem.NUMBER, new ExcelData(item.getPlacementCode(), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(GroupPlacementItem.STATUS)) {
                    String status = " ";
                    if (item.getOverallStatus() != null && item.getOverallStatus().getCode() != null) {
                        switch (item.getOverallStatus().getCode()) {
                            case GROUP_PLACEMENT_APPROVED -> status = "approved";
                            case GROUP_PLACEMENT_REJECTED -> status = "rejected";
                            case GROUP_PLACEMENT_SUBMITTED -> status = "waitingForApproval";
                            case GROUP_PLACEMENT_DRAFT -> status = "draft";
                        }
                    }
                    mapColumns.put(GroupPlacementItem.STATUS, new ExcelData(commonLocalizer.localize(status), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(GroupPlacementItem.APPROVER)) {
                    mapColumns.put(GroupPlacementItem.APPROVER, new ExcelData(item.getApproverEmployee() != null ? item.getApproverEmployee().getName() : "N/A", ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(GroupPlacementItem.DATE)) {
                    String format = ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(item.getDate()), user))
                            : ServerUtils.shortDateFormat(user.getUserDate(item.getDate()), user);
                    mapColumns.put(GroupPlacementItem.DATE, new ExcelData(format, ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(GroupPlacementItem.CREATOR)) {
                    mapColumns.put(GroupPlacementItem.CREATOR, new ExcelData(item.getCreator() != null ? item.getCreator().getName() : "N/A", ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(GroupPlacementItem.CREATED_DATE)) {
                    String format = ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(item.getCreatedDate().getDate()), user))
                            : ServerUtils.shortDateFormat(user.getUserDate(item.getCreatedDate().getDate()), user);
                    mapColumns.put(GroupPlacementItem.CREATED_DATE, new ExcelData(format, ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(GroupPlacementItem.UPDATER)) {
                    mapColumns.put(GroupPlacementItem.UPDATER, new ExcelData(item.getUpdater() != null ? item.getUpdater().getName() : "N/A", ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(GroupPlacementItem.UPDATED_DATE)) {
                    String format = ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(item.getUpdatedDate().getDate()), user))
                            : ServerUtils.shortDateFormat(user.getUserDate(item.getUpdatedDate().getDate()), user);
                    mapColumns.put(GroupPlacementItem.UPDATED_DATE, new ExcelData(format, ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
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
            log.error("Cannot generate " + "Group Placement list excel report, exception: " + e);
        }
        return null;
    }

    protected EdsUser getUser() {
        return userManager.getUser();
    }

    @Override
    protected void setFileName() {
        filename = "";
        EdsUser user = getUser();
        filename = user.getFirstName() + "_" + user.getLastName() + "_GroupPlacementList_" + dateFormat(user.getUserDate());
        filename = filename.replace("/", "_");
    }
}
