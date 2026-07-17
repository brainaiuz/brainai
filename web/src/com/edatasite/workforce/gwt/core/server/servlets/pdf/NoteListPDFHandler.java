package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.rpc.PerformanceNoteItem;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * User: Ilhombek
 * Date: 12.08.2009
 * Time: 11:08:14
 */
public class NoteListPDFHandler extends AbstractITextPostPdfHandler {

    @Autowired
    private HrmsService hrmsService;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
        String tableName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.perfomanceNoteListTableName);
        EdsUser user = uploadManager.getUser();

        ListResult<PerformanceNoteItem> performanceNoteList = hrmsService.getPerformanceNoteList(filterParametrs);
        List<PerformanceNoteItem> performanceNoteItems = performanceNoteList.getList();

        pdfData.setTableName(commonLocalizer.localizeWithParam(PdfLocalizationName.perfomanceNoteListTableName, user.getFullName()));

        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();

        List<String> header = panelTools.getColumnCodeName();
        List<String> header2 = new ArrayList<>();

        header.remove(PerformanceNoteItem.ACTION);
        ITextTableList tableList = new ITextTableList(header.size());
        pdfData.setListTable(tableList);
        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(PerformanceNoteItem.NAME, commonLocalizer.localize(PdfLocalizationName.name));
        mapColumnHeader.put(PerformanceNoteItem.RELATED_TO, commonLocalizer.localize(PdfLocalizationName.relatedTo));
        mapColumnHeader.put(PerformanceNoteItem.PERIOD, commonLocalizer.localize(PdfLocalizationName.period));
        mapColumnHeader.put(PerformanceNoteItem.STATUS, commonLocalizer.localize(PdfLocalizationName.status));
        mapColumnHeader.put(PerformanceNoteItem.REPORTED_BY, commonLocalizer.localize(PdfLocalizationName.reportedBy));
        mapColumnHeader.put(PerformanceNoteItem.RESOLVER, commonLocalizer.localize(PdfLocalizationName.resolverOwner));
        for (String aHeader : header) {
            header2.add(mapColumnHeader.get(aHeader));
        }

        tableList.addPdfTableHeader(header2.toArray(new String[]{}));

        for (PerformanceNoteItem performanceNote : performanceNoteItems) {
            List<String> cell = new ArrayList<>();
            for (int ii = 0; ii < header.size(); ii++) {
                if (PerformanceNoteItem.NAME.equals(header.get(ii))) {
                    cell.add(header.indexOf(PerformanceNoteItem.NAME), (performanceNote.getName() != null ? performanceNote.getName() : ""));
                }
                if (PerformanceNoteItem.RELATED_TO.equals(header.get(ii))) {
                    cell.add(header.indexOf(PerformanceNoteItem.RELATED_TO), (performanceNote.getRelatedToName() != null ? performanceNote.getRelatedToName() : ""));
                }
                if (performanceNote.getStartDate() != null && PerformanceNoteItem.PERIOD.equals(header.get(ii)) && performanceNote.getEndDate() != null) {
                    cell.add(header.indexOf(PerformanceNoteItem.PERIOD), dateFormat(performanceNote.getStartDate().getNonConvertedDate(), true) + " - " + dateFormat(performanceNote.getEndDate().getNonConvertedDate(), true));
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
            tableList.addPdfTableRows(cell.toArray(new String[]{}));
        }

        pdfData.setListTable(tableList);
        return pdfData;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_PerformanceNoteList_" + dateFormat(new Date()));
    }
}
