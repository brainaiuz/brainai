package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.LeaveBalanceReport;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.stream.Collectors;

public class AnnualBalanceReportPdfHandler extends AbstractITextPostPdfHandler {

    @Autowired
    private AvailabilityService availabilityService;
    private Integer year = Calendar.getInstance().get(Calendar.YEAR);

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;

        year = filterParametrs.getYear();

        EdsCompanySettings companySettings = company.getCompanySettings();
        filterParametrs.setLimit(StringUtils.isNotEmpty(companySettings.getPdfLimit()) ? Integer.parseInt(companySettings.getPdfLimit()) : LIMIT_PDF_ROWS);

        ListResult<LeaveBalanceReport> reportDataList = availabilityService.getEmployeeLeaveBalanceReport(filterParametrs);
        List<LeaveBalanceReport> leaveRequests = reportDataList.getList();

        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(LeaveBalanceReport.EMPLOYEE_NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.number), Element.ALIGN_LEFT));
        mapColumnHeader.put(LeaveBalanceReport.EMPLOYEE_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.employee), Element.ALIGN_LEFT));
        mapColumnHeader.put(LeaveBalanceReport.HIRE_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.hireDateField), Element.ALIGN_LEFT));
        mapColumnHeader.put(LeaveBalanceReport.RESIGN_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.resignationDate), Element.ALIGN_LEFT));
        mapColumnHeader.put(LeaveBalanceReport.STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        mapColumnHeader.put(LeaveBalanceReport.DEPARTMENT, new CellData(commonLocalizer.localize(PdfLocalizationName.department), Element.ALIGN_LEFT));
        mapColumnHeader.put(LeaveBalanceReport.LEAVE_ALLOWANCE_DAYS, new CellData(commonLocalizer.localize(PdfLocalizationName.leaveAllowanceDays), Element.ALIGN_LEFT));
        mapColumnHeader.put(LeaveBalanceReport.TAKEN_DAYS, new CellData(commonLocalizer.localize(PdfLocalizationName.takenDays), Element.ALIGN_RIGHT));
        mapColumnHeader.put(LeaveBalanceReport.CURRENT_BALANCE, new CellData(commonLocalizer.localize("balance"), Element.ALIGN_RIGHT));
        mapColumnHeader.put(LeaveBalanceReport.OPENING_BALANCE, new CellData(commonLocalizer.localize(PdfLocalizationName.openingBalanceDays), Element.ALIGN_RIGHT));

        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<CellData> header = panelTools.getColumnCodeName().stream()
                .filter(mapColumnHeader::containsKey)
                .map(mapColumnHeader::get)
                .collect(Collectors.toList());
        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));

        for (LeaveBalanceReport item : leaveRequests) {
            Map<String, String> columnMap = new HashMap<>();
            if (panelTools.getColumnCodeName().contains(LeaveBalanceReport.EMPLOYEE_NAME)) {
                columnMap.put(LeaveBalanceReport.EMPLOYEE_NAME, getResultOrLongDash(item.getEmployeeName()));
            }
            if (panelTools.getColumnCodeName().contains(LeaveBalanceReport.EMPLOYEE_NUMBER)) {
                columnMap.put(LeaveBalanceReport.EMPLOYEE_NUMBER, getResultOrLongDash(item.getEmployeeNumber()));
            }
            if (panelTools.getColumnCodeName().contains(LeaveBalanceReport.HIRE_DATE)) {
                columnMap.put(LeaveBalanceReport.HIRE_DATE, item.getHireDate() != null ? dateFormat(item.getHireDate().getNonConvertedDate()) : "");
            }
            if (panelTools.getColumnCodeName().contains(LeaveBalanceReport.RESIGN_DATE)) {
                columnMap.put(LeaveBalanceReport.RESIGN_DATE, item.getResignDate() != null ? dateFormat(item.getResignDate().getNonConvertedDate()) : "");
            }

            if (panelTools.getColumnCodeName().contains(LeaveBalanceReport.STATUS)) {
                columnMap.put(LeaveBalanceReport.STATUS, item.getStatus());
            }
            if (panelTools.getColumnCodeName().contains(LeaveBalanceReport.DEPARTMENT)) {
                columnMap.put(LeaveBalanceReport.DEPARTMENT, item.getDepartment());
            }
            if (panelTools.getColumnCodeName().contains(LeaveBalanceReport.LEAVE_ALLOWANCE_DAYS)) {
                columnMap.put(LeaveBalanceReport.LEAVE_ALLOWANCE_DAYS, getMoneyFormat(item.getLeaveAllowanceDays()));
            }
            if (panelTools.getColumnCodeName().contains(LeaveBalanceReport.TAKEN_DAYS)) {
                columnMap.put(LeaveBalanceReport.TAKEN_DAYS, getMoneyFormat(item.getTakenDays()));
            }
            if (panelTools.getColumnCodeName().contains(LeaveBalanceReport.CURRENT_BALANCE)) {
                columnMap.put(LeaveBalanceReport.CURRENT_BALANCE, getMoneyFormat(item.getCurrentBalance()));
            }
            if (panelTools.getColumnCodeName().contains(LeaveBalanceReport.OPENING_BALANCE)) {
                columnMap.put(LeaveBalanceReport.OPENING_BALANCE, String.valueOf(item.getOpeningBalance() == null ? "" : String.valueOf(item.getOpeningBalance())));
            }

            List<String> columns = panelTools.getColumnCodeName().stream()
                    .filter(columnMap::containsKey)
                    .map(columnMap::get)
                    .collect(Collectors.toList());
            tableList.addPdfTableRows(columns.toArray(new String[]{}));
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
        return property != null ? property.getPlural() : pdfWfmMessageSource.localize("annualLeaveBalance");
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_AnnualBalanceReport_" + dateFormat(new Date()));
    }
}
