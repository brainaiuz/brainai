package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


/**
 * User: Admin
 * Date: 23.07.2009
 * Time: 11:32:36
 */
public class NoteListExcelHandler extends BaseExcelHandler implements Constants {

    @Autowired
    private HrmsService hrmsService;
    @Autowired
    private UserManager userManager;

    private static final Logger log = LoggerFactory.getLogger(NoteListExcelHandler.class);
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;

    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        EdsUser user = userManager.getUser();
        ListResult<PerformanceNoteItem> performanceNoteList = hrmsService.getPerformanceNoteList(filterParametrs);
        ExcelData[] cellDatas;
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        header.remove(PerformanceNoteItem.ACTION);
        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(PerformanceNoteItem.NAME, commonLocalizer.localize(PdfLocalizationName.name));
        mapColumnHeader.put(PerformanceNoteItem.RELATED_TO, commonLocalizer.localize(PdfLocalizationName.relatedTo));
        mapColumnHeader.put(PerformanceNoteItem.PERIOD, commonLocalizer.localize(PdfLocalizationName.period));
        mapColumnHeader.put(PerformanceNoteItem.STATUS, commonLocalizer.localize(PdfLocalizationName.status));
        mapColumnHeader.put(PerformanceNoteItem.REPORTED_BY, commonLocalizer.localize(PdfLocalizationName.reportedBy));
        mapColumnHeader.put(PerformanceNoteItem.RESOLVER, commonLocalizer.localize(PdfLocalizationName.resolverOwner));

        try {
            List<ExcelData[]> list = new LinkedList<>();
            cellDatas = new ExcelData[header.size()];

            for (int i = 0; i < header.size(); i++) {
                cellDatas[i] = new ExcelData(mapColumnHeader.get(header.get(i)),
                        ExcelData.STRING, header.get(i).equals(PerformanceNoteItem.NAME) || header.get(i).equals(PerformanceNoteItem.RELATED_TO) ? 50 : 20, false,
                        header.get(i).equals(PerformanceNoteItem.NAME) || header.get(i).equals(PerformanceNoteItem.RELATED_TO) ||
                                header.get(i).equals(PerformanceNoteItem.PERIOD), ExcelData.NO_BORDER, ExcelData.HEADER);
            }

            list.add(cellDatas);

            for (PerformanceNoteItem performanceNote : performanceNoteList.getList()) {
                List<String> cell = new ArrayList<>();
                for (int ii = 0; ii < header.size(); ii++) {
                    if (PerformanceNoteItem.NAME.equals(header.get(ii))) {
                        cell.add(header.indexOf(PerformanceNoteItem.NAME), (performanceNote.getName() != null ? performanceNote.getName() : ""));
                    }
                    if (PerformanceNoteItem.RELATED_TO.equals(header.get(ii))) {
                        cell.add(header.indexOf(PerformanceNoteItem.RELATED_TO), (performanceNote.getRelatedToName() != null ? performanceNote.getRelatedToName() : ""));
                    }
                    if (PerformanceNoteItem.PERIOD.equals(header.get(ii))) {
                        if (performanceNote.getStartDate() != null && performanceNote.getEndDate() != null) {
                            cell.add(ServerUtils.shortDateFormat(performanceNote.getStartDate().getNonConvertedDate(), user) + " - " + ServerUtils.shortDateFormat(performanceNote.getEndDate().getNonConvertedDate(), user));
                        } else {
                            cell.add("");
                        }
                    }
                    if (PerformanceNoteItem.STATUS.equals(header.get(ii))) {
                        cell.add(header.indexOf(PerformanceNoteItem.STATUS), (performanceNote.getStatusName() != null ? performanceNote.getStatusName() : ""));
                    }
                    if (PerformanceNoteItem.REPORTED_BY.equals(header.get(ii))) {
                        cell.add(header.indexOf(PerformanceNoteItem.REPORTED_BY), (performanceNote.getReportedByName() != null ? performanceNote.getReportedByName() : ""));
                    }
                    if (PerformanceNoteItem.RESOLVER.equals(header.get(ii))) {
                        cell.add(header.indexOf(PerformanceNoteItem.RESOLVER), (performanceNote.getResolverName() != null ? performanceNote.getResolverName() : ""));
                    }
                }
                cellDatas = new ExcelData[header.size()];
                for (int k = 0; k < header.size(); k++) {
                    cellDatas[k] = new ExcelData(cell.get(k), ExcelData.STRING, header.get(k).equals(PerformanceNoteItem.NAME) || header.get(k).equals(PerformanceNoteItem.RELATED_TO) ? 50 : 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                list.add(cellDatas);
            }

            return new WorkBook(list).getWorkBook(filename, 0, 0, 0, 6);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Cannot generate performance note list excel report, exception: " + e);
        }
        return null;
    }

    @Override
    protected void setFileName() {
        filename = "Performance Note List";
    }

    public void setExcelReferenceMessageSource(WfmResourceBundleMessageSource excelReferenceMessageSource) {
        this.excelReferenceMessageSource = excelReferenceMessageSource;
    }
}