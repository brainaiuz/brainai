package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimeSheetApprovalListItem;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetService;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * User: Ilhombek
 * Date: 30.08.2009
 * Time: 17:06:42
 */
public class TimesheetApprovalListPDFHandler extends AbstractITextPostPdfHandler {

    private TimesheetService timesheetService;

    public void setTimesheetService(TimesheetService timesheetService) {
        this.timesheetService = timesheetService;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_" + commonLocalizer.localize(PdfLocalizationName.timesheetApprovedListFileName) + "_" + dateFormat(new Date(), true));
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        EdsCompanySettings companySettings = company.getCompanySettings();
        filterParametrs.setLimit(StringUtils.isNotEmpty(companySettings.getPdfLimit()) ? Integer.parseInt(companySettings.getPdfLimit()) : LIMIT_PDF_ROWS);
        ListResult<TimeSheetApprovalListItem> positionList = timesheetService.getTimeSheetApprovalSessionList(filterParametrs);

        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(TimeSheetApprovalListItem.EMPLOYEENAME, new CellData(commonLocalizer.localize(PdfLocalizationName.employee), Element.ALIGN_LEFT));
        mapColumnHeader.put(TimeSheetApprovalListItem.STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        mapColumnHeader.put(TimeSheetApprovalListItem.PROJECTNAME, new CellData(commonLocalizer.localize(PdfLocalizationName.project), Element.ALIGN_LEFT));
        mapColumnHeader.put(TimeSheetApprovalListItem.FROMDATE, new CellData(commonLocalizer.localize(PdfLocalizationName.period), Element.ALIGN_LEFT));
        mapColumnHeader.put(TimeSheetApprovalListItem.APPROVER, new CellData(commonLocalizer.localize(PdfLocalizationName.approvers), Element.ALIGN_LEFT));
        mapColumnHeader.put(TimeSheetApprovalListItem.SUBMITTED_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.submittedDate), Element.ALIGN_LEFT));
        mapColumnHeader.put(TimeSheetApprovalListItem.APPROVAL_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.approvedDate), Element.ALIGN_LEFT));
        mapColumnHeader.put(TimeSheetApprovalListItem.TIMESPENT, new CellData(commonLocalizer.localize(PdfLocalizationName.timeSpentOnly), Element.ALIGN_LEFT));
        mapColumnHeader.put(TimeSheetApprovalListItem.APPROVED, new CellData(commonLocalizer.localize(PdfLocalizationName.approved), Element.ALIGN_LEFT));
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<CellData> header = panelTools.getColumnCodeName().stream()
                .filter(columnCode -> mapColumnHeader.containsKey(columnCode))
                .map(columnCode -> mapColumnHeader.get(columnCode))
                .collect(Collectors.toList());
        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));

        for (TimeSheetApprovalListItem positions : positionList.getList()) {
            Map<String, CellData> mapColumns = new HashMap<>();
            if (panelTools.getColumnCodeName().contains(TimeSheetApprovalListItem.EMPLOYEENAME)) {
                mapColumns.put(TimeSheetApprovalListItem.EMPLOYEENAME, new CellData(getResultOrLongDash(positions.getEmployeeName()), Element.ALIGN_LEFT));
            } 
            if (panelTools.getColumnCodeName().contains(TimeSheetApprovalListItem.STATUS)) {
                mapColumns.put(TimeSheetApprovalListItem.STATUS, new CellData(getResultOrLongDash(positions.getStatus()), Element.ALIGN_LEFT));
            } 
            if (panelTools.getColumnCodeName().contains(TimeSheetApprovalListItem.PROJECTNAME)) {
                mapColumns.put(TimeSheetApprovalListItem.PROJECTNAME, new CellData(getResultOrLongDash(positions.getProjectName()), Element.ALIGN_LEFT));
            } 
            if (panelTools.getColumnCodeName().contains(TimeSheetApprovalListItem.FROMDATE)) {
                if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                    mapColumns.put(TimeSheetApprovalListItem.FROMDATE, (positions.getFromDate() != null && positions.getEndDate() != null) ? new CellData((ServerUtils.convertToUzbDateFormat(dateFormat(positions.getFromDate().getNonConvertedDate(), true)) + "-" + ServerUtils.convertToUzbDateFormat(dateFormat(positions.getEndDate().getNonConvertedDate(), true))), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                } else {
                    mapColumns.put(TimeSheetApprovalListItem.FROMDATE, (positions.getFromDate() != null && positions.getEndDate() != null) ? new CellData((dateFormat(positions.getFromDate().getNonConvertedDate(), true) + "-" + dateFormat(positions.getEndDate().getNonConvertedDate(), true)), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                }
            } 
            if (panelTools.getColumnCodeName().contains(TimeSheetApprovalListItem.APPROVER)) {
                mapColumns.put(TimeSheetApprovalListItem.APPROVER, new CellData(getResultOrLongDash(positions.getApprover()), Element.ALIGN_LEFT));
            } 
            if (panelTools.getColumnCodeName().contains(TimeSheetApprovalListItem.SUBMITTED_DATE)) {
                if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                    mapColumns.put(TimeSheetApprovalListItem.SUBMITTED_DATE, positions.getSubmittedDate() != null ? new CellData(ServerUtils.convertToUzbDateFormat(dateFormat(positions.getSubmittedDate().getNonConvertedDate(), true)), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                } else {
                    mapColumns.put(TimeSheetApprovalListItem.SUBMITTED_DATE, positions.getSubmittedDate() != null ? new CellData(dateFormat(positions.getSubmittedDate().getNonConvertedDate(), true), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));

                }
            } 
            if (panelTools.getColumnCodeName().contains(TimeSheetApprovalListItem.APPROVAL_DATE)) {
                if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                    mapColumns.put(TimeSheetApprovalListItem.APPROVAL_DATE, positions.getApprovalDate() != null ? new CellData(ServerUtils.convertToUzbDateFormat(dateFormat(positions.getApprovalDate().getNonConvertedDate(), true)), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));
                } else {
                    mapColumns.put(TimeSheetApprovalListItem.APPROVAL_DATE, positions.getApprovalDate() != null ? new CellData(dateFormat(positions.getApprovalDate().getNonConvertedDate(), true), Element.ALIGN_LEFT) : new CellData("—", Element.ALIGN_LEFT));

                }
            } 
            if (panelTools.getColumnCodeName().contains(TimeSheetApprovalListItem.TIMESPENT)) {
                mapColumns.put(TimeSheetApprovalListItem.TIMESPENT, new CellData(getResultOrLongDash(positions.getTimeSpent()), Element.ALIGN_LEFT));
            } 
            if (panelTools.getColumnCodeName().contains(TimeSheetApprovalListItem.APPROVED)) {
                mapColumns.put(TimeSheetApprovalListItem.APPROVED, new CellData(getResultOrLongDash(positions.getApprovedHours()), Element.ALIGN_LEFT));
            }
            List<CellData> columns = panelTools.getColumnCodeName().stream()
                    .filter(columnCode -> mapColumns.containsKey(columnCode))
                    .map(columnCode -> mapColumns.get(columnCode))
                    .collect(Collectors.toList());
            tableList.addPdfTableRows(columns.toArray(new CellData[]{}));
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
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        return property != null ? property.getPlural() : pdfWfmMessageSource.localize("timesheetApproval");
    }
}
