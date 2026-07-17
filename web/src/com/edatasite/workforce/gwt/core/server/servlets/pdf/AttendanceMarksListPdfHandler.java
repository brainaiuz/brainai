package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.availability.client.rpc.AttendanceMarkListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.UserFingerPrintmanager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AttendanceMarksListPdfHandler extends AbstractITextPostPdfHandler {

    @Autowired
    private UserFingerPrintmanager userFingerPrintmanager;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsCompanySettings companySettings = company.getCompanySettings();
        fp.setLimit(StringUtils.isNotEmpty(companySettings.getPdfLimit())
                ? Integer.parseInt(companySettings.getPdfLimit())
                : LIMIT_PDF_ROWS);

        List<AttendanceMarkListItem> listResult = userFingerPrintmanager.getAttendanceMarkListForExport(fp);
        ListPanelToolRpc panelTools = fp.getListPanelTool();

        // LinkedHashMap preserves the display order defined here
        Map<String, CellData> columnHeaders = new LinkedHashMap<>();
        columnHeaders.put(AttendanceMarkListItem.EMPLOYEE_CODE, new CellData(commonLocalizer.localize("employeeCode"), Element.ALIGN_LEFT));
        columnHeaders.put(AttendanceMarkListItem.EMPLOYEE_NAME, new CellData(commonLocalizer.localize("employee"), Element.ALIGN_LEFT));
        columnHeaders.put(AttendanceMarkListItem.DATE, new CellData(commonLocalizer.localize("date"), Element.ALIGN_LEFT));
        columnHeaders.put(AttendanceMarkListItem.LOCATION, new CellData(commonLocalizer.localize("location"), Element.ALIGN_LEFT));
        columnHeaders.put(AttendanceMarkListItem.DEPARTMENT, new CellData(commonLocalizer.localize("department"), Element.ALIGN_LEFT));
        columnHeaders.put(AttendanceMarkListItem.POSITION, new CellData(commonLocalizer.localize("position"), Element.ALIGN_LEFT));
        columnHeaders.put(AttendanceMarkListItem.TIMESLOT, new CellData(commonLocalizer.localize("timeslot"), Element.ALIGN_LEFT));
        columnHeaders.put(AttendanceMarkListItem.SUPERVISOR, new CellData(commonLocalizer.localize("supervisor"), Element.ALIGN_LEFT));
        columnHeaders.put(AttendanceMarkListItem.DEVICE_ID, new CellData(commonLocalizer.localize("deviceId"), Element.ALIGN_LEFT));
        columnHeaders.put(AttendanceMarkListItem.TERMINAL, new CellData(commonLocalizer.localize("terminal"), Element.ALIGN_LEFT));
        columnHeaders.put(AttendanceMarkListItem.EMPLOYEE_STATUS, new CellData(commonLocalizer.localize("status"), Element.ALIGN_LEFT));
        columnHeaders.put(AttendanceMarkListItem.ROLE, new CellData(commonLocalizer.localize("role"), Element.ALIGN_LEFT));
        columnHeaders.put(AttendanceMarkListItem.VALIDATED, new CellData(commonLocalizer.localize("inOut"), Element.ALIGN_LEFT));
        columnHeaders.put(AttendanceMarkListItem.SOURCE, new CellData(commonLocalizer.localize("source"), Element.ALIGN_LEFT));
        columnHeaders.put(AttendanceMarkListItem.IS_AUTO, new CellData(commonLocalizer.localize("isAuto"), Element.ALIGN_LEFT));
        columnHeaders.put(AttendanceMarkListItem.CREATED_DATE, new CellData(commonLocalizer.localize("createdDate"), Element.ALIGN_LEFT));
        columnHeaders.put(AttendanceMarkListItem.NOTE, new CellData(commonLocalizer.localize("note"), Element.ALIGN_LEFT));
        columnHeaders.put(AttendanceMarkListItem.CREATED_BY_NAME, new CellData(commonLocalizer.localize("createdBy"), Element.ALIGN_LEFT));
        columnHeaders.put(AttendanceMarkListItem.UPDATED_AT, new CellData(commonLocalizer.localize("modifiedDate"), Element.ALIGN_LEFT));
        columnHeaders.put(AttendanceMarkListItem.UPDATED_BY_NAME, new CellData(commonLocalizer.localize("modifiedBy"), Element.ALIGN_LEFT));
        columnHeaders.put(AttendanceMarkListItem.MAP, new CellData(commonLocalizer.localize("location"), Element.ALIGN_LEFT));
        columnHeaders.put(AttendanceMarkListItem.PROFILE_PICTURE_URL, new CellData(commonLocalizer.localize("profilePhoto"), Element.ALIGN_LEFT));
        columnHeaders.put("pictureUrl", new CellData(commonLocalizer.localize("photo"), Element.ALIGN_LEFT));

        List<String> activeColumns = panelTools.getColumnCodeName();

        List<CellData> header = activeColumns.stream()
                .filter(columnHeaders::containsKey)
                .map(columnHeaders::get)
                .toList();

        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[0]));

        // Build alignment array from the header CellData so row cells inherit the same alignment
        Integer[] alignments = header.stream()
                .map(CellData::getAlignment)
                .toArray(Integer[]::new);

        for (AttendanceMarkListItem item : listResult) {
            Map<String, String> rowData = buildRowData(item);
            String[] rowValues = activeColumns.stream()
                    .filter(rowData::containsKey)
                    .map(rowData::get)
                    .toArray(String[]::new);
            tableList.addPdfTableRows(alignments, rowValues);
        }

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        pdfData.setListTable(tableList);
        return pdfData;
    }

    private Map<String, String> buildRowData(AttendanceMarkListItem item) {
        Map<String, String> row = new LinkedHashMap<>();
        row.put(AttendanceMarkListItem.EMPLOYEE_CODE, getResultOrLongDash(item.getEmployeeCode()));
        row.put(AttendanceMarkListItem.EMPLOYEE_NAME, getResultOrLongDash(item.getEmployeeName()));
        row.put(AttendanceMarkListItem.DATE, item.getDate() != null ? longDateFormat(item.getDate().getDate(), true) : "—");
        row.put(AttendanceMarkListItem.LOCATION, getResultOrLongDash(item.getLocation()));
        row.put(AttendanceMarkListItem.DEPARTMENT, getResultOrLongDash(item.getDepartment()));
        row.put(AttendanceMarkListItem.POSITION, getResultOrLongDash(item.getPosition()));
        row.put(AttendanceMarkListItem.TIMESLOT, getResultOrLongDash(item.getTimeslotName()));
        row.put(AttendanceMarkListItem.SUPERVISOR, getResultOrLongDash(item.getSupervisor()));
        row.put(AttendanceMarkListItem.DEVICE_ID, getResultOrLongDash(item.getDeviceId()));
        row.put(AttendanceMarkListItem.TERMINAL, getResultOrLongDash(item.getTerminal()));
        row.put(AttendanceMarkListItem.EMPLOYEE_STATUS, getResultOrLongDash(item.getEmployeeStatus()));
        row.put(AttendanceMarkListItem.ROLE, getResultOrLongDash(item.getRole()));
        row.put(AttendanceMarkListItem.VALIDATED, getResultOrLongDash(item.getValidated()));
        row.put(AttendanceMarkListItem.SOURCE, getResultOrLongDash(formatSource(item.getSource())));
        row.put(AttendanceMarkListItem.IS_AUTO, Boolean.TRUE.equals(item.getIsAuto()) ? commonLocalizer.localize("yes") : commonLocalizer.localize("no"));
        row.put(AttendanceMarkListItem.CREATED_DATE, item.getCreatedDate() != null ? longDateFormat(item.getCreatedDate().getDate(), true) : "—");
        row.put(AttendanceMarkListItem.NOTE, getResultOrLongDash(item.getNote()));
        row.put(AttendanceMarkListItem.CREATED_BY_NAME, getResultOrLongDash(item.getCreatedByName()));
        row.put(AttendanceMarkListItem.UPDATED_AT, item.getUpdatedAt() != null ? longDateFormat(item.getUpdatedAt().getDate(), true) : "—");
        row.put(AttendanceMarkListItem.UPDATED_BY_NAME, getResultOrLongDash(item.getUpdatedByName()));
        row.put(AttendanceMarkListItem.MAP, (item.getLatitude() != null && item.getLongitude() != null)
                ? item.getLatitude() + ", " + item.getLongitude() : "—");
        row.put(AttendanceMarkListItem.PROFILE_PICTURE_URL, "");
        row.put("pictureUrl", "");
        return row;
    }

    private String formatSource(String source) {
        if (source == null) return null;
        return switch (source) {
            case "FACE_ID" -> "Face ID";
            case "MOBILE" -> "Mobile";
            case "WEB" -> "Web";
            case "UNKNOWN" -> "Unknown";
            default -> source;
        };
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected String getTableName(Object dataClass) {
        return commonLocalizer.localize("attendanceMarks");
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_AttendanceMarksList_" + dateFormat(new Date()));
    }
}
