package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.rpc.PerformanceNoteItem;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * User: Admin
 * Date: 23.07.2009
 * Time: 16:18:58
 */
public class IncidentListExcelHandler extends BaseExcelHandler implements Constants {
    @Autowired
    private HrmsService hrmsService;
    @Autowired
    private UserManager userManager;
    @Autowired
    private PropertManager propertManager;
    private String sheetName;
    private static final Logger log = LoggerFactory.getLogger(IncidentListExcelHandler.class);
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter fp = (ListingFilterParameter) object;
        fp.setLimit(LIMIT_EXCEL_ROW);
        fp.setIncident(true);
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        sheetName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.incidents);
        ListResult<PerformanceNoteItem> incidentItems = hrmsService.getPerformanceNoteList(fp);
        List<PerformanceNoteItem> incidentListItems = incidentItems.getList();

        ExcelData[] cellDatas;
        EdsUser user = userManager.getUser();

        ListPanelToolRpc panelTools = fp.getListPanelTool();

        WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
        workBook.setSheetName(filename);
        Map<String, ExcelData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(PerformanceNoteItem.NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.name), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(PerformanceNoteItem.RELATED_TO, new ExcelData(commonLocalizer.localize(PdfLocalizationName.relatedTo), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(PerformanceNoteItem.PERIOD, new ExcelData(commonLocalizer.localize(PdfLocalizationName.period), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(PerformanceNoteItem.STATUS, new ExcelData(availabilityLocalizer.localize(PdfLocalizationName.incidentStatus), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(PerformanceNoteItem.REPORTED_BY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.reportedBy), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(PerformanceNoteItem.RESOLVER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.resolverOwner), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
        mapColumnHeader.put(PerformanceNoteItem.PRIORITY, new ExcelData(commonLocalizer.localize(PdfLocalizationName.priority), ExcelData.STRING, 15, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
        try {
            List<ExcelData[]> list = new LinkedList<>();
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size() + 1, user.getCompany().getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size() + 1, sheetName, workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size() + 1, ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertDateFormatFromEngToUzb(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " Xolatiga ko'ra" : commonLocalizer.localize(PdfLocalizationName.asOF) + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));
            List<ExcelData> excelDataList = new ArrayList<>();
            for (String columnName : panelTools.getColumnCodeName()) {
                if (mapColumnHeader.containsKey(columnName)) {
                    excelDataList.add(mapColumnHeader.get(columnName));
                }
            }
            cellDatas = new ExcelData[excelDataList.size()];
            excelDataList.toArray(cellDatas);
            list.add(cellDatas);

            Map<String, ExcelData> mapColumns = new HashMap<>();
            for (PerformanceNoteItem incident : incidentListItems) {
                mapColumns.clear();

                if (panelTools.getColumnCodeName().contains(PerformanceNoteItem.NAME)) {
                    mapColumns.put(PerformanceNoteItem.NAME, new ExcelData(incident.getName() != null ? incident.getName() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(PerformanceNoteItem.RELATED_TO)) {
                    mapColumns.put(PerformanceNoteItem.RELATED_TO, new ExcelData(incident.getRelatedToName() != null ? incident.getRelatedToName() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(PerformanceNoteItem.PERIOD)) {
                    String formatedDate = "";
                    if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                        if (incident.getStartDate() != null) {
                            formatedDate = ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(incident.getStartDate().getNonConvertedDate(), user));
                        }
                        if (incident.getEndDate() != null) {
                            formatedDate += " - " + ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(incident.getEndDate().getNonConvertedDate(), user));
                        }
                        mapColumns.put(PerformanceNoteItem.PERIOD, new ExcelData(formatedDate, ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    } else {
                        if (incident.getStartDate() != null) {
                            formatedDate = ServerUtils.shortDateFormat(incident.getStartDate().getNonConvertedDate(), user);
                        }
                        if (incident.getEndDate() != null) {
                            formatedDate += " - " + ServerUtils.shortDateFormat(incident.getEndDate().getNonConvertedDate(), user);
                        }
                        mapColumns.put(PerformanceNoteItem.PERIOD, new ExcelData(formatedDate, ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                    }
                }
                if (panelTools.getColumnCodeName().contains(PerformanceNoteItem.STATUS)) {
                    mapColumns.put(PerformanceNoteItem.STATUS, new ExcelData(incident.getStatusName() != null ? incident.getStatusName() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(PerformanceNoteItem.PRIORITY)) {
                    mapColumns.put(PerformanceNoteItem.PRIORITY, new ExcelData(incident.getPriorityName() != null ? incident.getPriorityName() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(PerformanceNoteItem.REPORTED_BY)) {
                    mapColumns.put(PerformanceNoteItem.REPORTED_BY, new ExcelData(incident.getReportedByName() != null ? incident.getReportedByName() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(PerformanceNoteItem.RESOLVER)) {
                    mapColumns.put(PerformanceNoteItem.RESOLVER, new ExcelData(incident.getResolverName() != null ? incident.getResolverName() : "", ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
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
            return workBook.getWorkBook(filename, 0, 0, 0, 6);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate incident list excel report, exception: " + e);
        }

        return null;
    }

    @Override
    protected void setFileName() {
        filename = "Incident List";
    }

    public void setExcelReferenceMessageSource(WfmResourceBundleMessageSource excelReferenceMessageSource) {
        this.excelReferenceMessageSource = excelReferenceMessageSource;
    }
}