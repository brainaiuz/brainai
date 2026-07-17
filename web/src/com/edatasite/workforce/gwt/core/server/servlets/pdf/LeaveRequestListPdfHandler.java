package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.client.rpc.LeaveRequestLisItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LeaveRequestListPdfHandler extends AbstractITextPostPdfHandler {

    @Autowired
    private AvailabilityService availabilityService;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        EdsCompanySettings companySettings = company.getCompanySettings();
        filterParametrs.setLimit(StringUtils.isNotEmpty(companySettings.getPdfLimit()) ? Integer.parseInt(companySettings.getPdfLimit()) : LIMIT_PDF_ROWS);

        ListResult<LeaveRequestLisItem> listResult = availabilityService.getLeaveRequestList(filterParametrs);
        List<LeaveRequestLisItem> leaveRequests = listResult.getList();

        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(LeaveRequestLisItem.CODE, new CellData(commonLocalizer.localize(PdfLocalizationName.number), Element.ALIGN_LEFT));
        mapColumnHeader.put(LeaveRequestLisItem.EMPLOYEE_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.employee), Element.ALIGN_LEFT));
        mapColumnHeader.put(LeaveRequestLisItem.REASON, new CellData(commonLocalizer.localize(PdfLocalizationName.reason), Element.ALIGN_LEFT));
        mapColumnHeader.put(LeaveRequestLisItem.FROM_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.from), Element.ALIGN_LEFT));
        mapColumnHeader.put(LeaveRequestLisItem.TO_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.to), Element.ALIGN_LEFT));
        mapColumnHeader.put(LeaveRequestLisItem.STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        mapColumnHeader.put(LeaveRequestLisItem.APPROVER, new CellData(commonLocalizer.localize(PdfLocalizationName.approver), Element.ALIGN_LEFT));
        mapColumnHeader.put(LeaveRequestLisItem.CREATED_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.createdDate), Element.ALIGN_LEFT));
        mapColumnHeader.put(LeaveRequestLisItem.REGISTERED_BY, new CellData(commonLocalizer.localize("registeredBy"), Element.ALIGN_LEFT));
        mapColumnHeader.put(LeaveRequestLisItem.LEAVE_DAYS, new CellData(commonLocalizer.localize("leaveDays"), Element.ALIGN_LEFT));
        mapColumnHeader.put(LeaveRequestLisItem.TYPE, new CellData(commonLocalizer.localize(PdfLocalizationName.type), Element.ALIGN_LEFT));
        mapColumnHeader.put(LeaveRequestLisItem.DESCRIPTION, new CellData(commonLocalizer.localize(PdfLocalizationName.description), Element.ALIGN_LEFT));
        mapColumnHeader.put(LeaveRequestLisItem.DEPARTMENT, new CellData(commonLocalizer.localize(PdfLocalizationName.department), Element.ALIGN_LEFT));
        mapColumnHeader.put(LeaveRequestLisItem.POSITION, new CellData(commonLocalizer.localize(PdfLocalizationName.position), Element.ALIGN_LEFT));

        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<CellData> header = panelTools.getColumnCodeName().stream()
                .filter(mapColumnHeader::containsKey)
                .map(mapColumnHeader::get)
                .collect(Collectors.toList());
        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));

        for (LeaveRequestLisItem item : leaveRequests) {
            Map<String, String> columnMap = new HashMap<>();
            if (panelTools.getColumnCodeName().contains(LeaveRequestLisItem.CODE)) {
                columnMap.put(LeaveRequestLisItem.CODE, getResultOrLongDash(item.getLeaveRequestCode()));
            }
            if (panelTools.getColumnCodeName().contains(LeaveRequestLisItem.EMPLOYEE_NAME)) {
                columnMap.put(LeaveRequestLisItem.EMPLOYEE_NAME, getResultOrLongDash(item.getEmployeeName()));
            }
            if (panelTools.getColumnCodeName().contains(LeaveRequestLisItem.REASON)) {
                columnMap.put(LeaveRequestLisItem.REASON, getResultOrLongDash(item.getReason()));
            }
            if (panelTools.getColumnCodeName().contains(LeaveRequestLisItem.FROM_DATE)) {
                String startDate = "--";
                if (item.getEndDate() != null) {
                    if (item.isAllDay()) {
                        startDate = ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat(item.getStartDate().getNonConvertedDate())) : dateFormat(item.getStartDate().getNonConvertedDate());
                    } else {
                        startDate = ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(longDateFormat(item.getStartDate().getNonConvertedDate(), true)) : longDateFormat(item.getStartDate().getNonConvertedDate(), true);
                    }
                }
                columnMap.put(LeaveRequestLisItem.FROM_DATE, startDate);
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
                columnMap.put(LeaveRequestLisItem.TO_DATE, enddate);
            }
            if (panelTools.getColumnCodeName().contains(LeaveRequestLisItem.STATUS)) {
                columnMap.put(LeaveRequestLisItem.STATUS, item.getStatus());
            }
            if (panelTools.getColumnCodeName().contains(LeaveRequestLisItem.APPROVER)) {
                columnMap.put(LeaveRequestLisItem.APPROVER, item.getApproverName());
            }
            if (panelTools.getColumnCodeName().contains(LeaveRequestLisItem.CREATED_DATE)) {
                columnMap.put(LeaveRequestLisItem.CREATED_DATE, item.getCreatedDate() != null ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(longDateFormat(item.getCreatedDate())) : longDateFormat(item.getCreatedDate())) : "—");
            }
            if (panelTools.getColumnCodeName().contains(LeaveRequestLisItem.REGISTERED_BY)) {
                columnMap.put(LeaveRequestLisItem.REGISTERED_BY, getResultOrLongDash(item.getCreator()));
            }
            if (panelTools.getColumnCodeName().contains(LeaveRequestLisItem.LEAVE_DAYS)) {
                columnMap.put(LeaveRequestLisItem.LEAVE_DAYS, item.getLeaveDays());
            }
            if (panelTools.getColumnCodeName().contains(LeaveRequestLisItem.TYPE)) {
                columnMap.put(LeaveRequestLisItem.TYPE, item.getType());
            }
            if (panelTools.getColumnCodeName().contains(LeaveRequestLisItem.DESCRIPTION)) {
                columnMap.put(LeaveRequestLisItem.DESCRIPTION, item.getDescription());
            }
            if (panelTools.getColumnCodeName().contains(LeaveRequestLisItem.DEPARTMENT)) {
                columnMap.put(LeaveRequestLisItem.DEPARTMENT, item.getDepartment());
            }
            if (panelTools.getColumnCodeName().contains(LeaveRequestLisItem.POSITION)) {
                columnMap.put(LeaveRequestLisItem.POSITION, item.getPosition());
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
        return property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.leaveRequests);

    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_LeaveRequestList_" + dateFormat(new Date()));
    }
}
