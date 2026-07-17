package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsAttendanceTerminal;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.website.AttendanceTerminal;
import com.edatasite.workforce.gwt.core.server.db.AttendanceTerminalManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AttendanceTerminalListPDFHandler extends AbstractITextPostPdfHandler {

    @Autowired
    private AttendanceTerminalManager attendanceTerminalManager;

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName("AttendanceTerminalList_" + dateFormat(new Date()));
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        List<EdsAttendanceTerminal> terminals = attendanceTerminalManager.getAll();
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();

        Map<String, CellData> columnHeaderMap = new HashMap<>();
        columnHeaderMap.put(AttendanceTerminal.DEVICE_ID, new CellData(commonLocalizer.localize(PdfLocalizationName.id), Element.ALIGN_LEFT));
        columnHeaderMap.put(AttendanceTerminal.BRANCH_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.branch), Element.ALIGN_LEFT));
        columnHeaderMap.put(AttendanceTerminal.DYNAMIC_STATUS, new CellData(availabilityLocalizer.localize("dynamicType"), Element.ALIGN_LEFT));

        List<CellData> header = panelTools.getColumnCodeName().stream()
                .filter(columnHeaderMap::containsKey)
                .map(columnHeaderMap::get)
                .toList();
        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));

        Integer[] alignments = header.stream().map(CellData::getAlignment).toArray(Integer[]::new);

        for (EdsAttendanceTerminal terminal : terminals) {
            Map<String, String> rowData = new HashMap<>();
            rowData.put(AttendanceTerminal.DEVICE_ID, getResultOrLongDash(terminal.getCompanyUniqueID()));
            rowData.put(AttendanceTerminal.BRANCH_NAME, getResultOrLongDash(terminal.getCompanyBranchName()));
            String dynamicVal = terminal.getDynamicStatus() != null
                    ? (terminal.getDynamicStatus() ? commonLocalizer.localize(PdfLocalizationName.yes) : commonLocalizer.localize(PdfLocalizationName.no))
                    : "—";
            rowData.put(AttendanceTerminal.DYNAMIC_STATUS, dynamicVal);

            String[] rowValues = panelTools.getColumnCodeName().stream()
                    .filter(rowData::containsKey)
                    .map(rowData::get)
                    .toArray(String[]::new);
            tableList.addPdfTableRows(alignments, rowValues);
        }

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setListTable(tableList);
        return pdfData;
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected String getTableName(Object dataClass) {
        return availabilityLocalizer.localize("attendanceTerminals");
    }
}