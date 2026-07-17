package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
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
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.team.client.rpc.DepartmentService;
import com.edatasite.workforce.gwt.team.client.rpc.TeamListItem;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DepartmentListPDFHandler extends AbstractITextPostPdfHandler {

    private DepartmentService departmentService;

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_DepartmentList_" + dateFormat(new Date()));
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        filterParametrs.setAllByFilter(false);
        EdsCompanySettings companySettings = company.getCompanySettings();
        filterParametrs.setLimit(StringUtils.isNotEmpty(companySettings.getPdfLimit()) ? Integer.parseInt(companySettings.getPdfLimit()) : LIMIT_PDF_ROWS);

        ListResult<TeamListItem> teamListItemListResult = departmentService.getTeams(filterParametrs);
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();

        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(TeamListItem.CODE, new CellData(commonLocalizer.localize(PdfLocalizationName.number), Element.ALIGN_LEFT));
        mapColumnHeader.put(TeamListItem.NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.name), Element.ALIGN_LEFT));
        mapColumnHeader.put(TeamListItem.PARENT_DEPARTMENT, new CellData(commonLocalizer.localize(PdfLocalizationName.parentDepartmentForPdfHeader), Element.ALIGN_LEFT));
        mapColumnHeader.put(TeamListItem.LEADER_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.leader), Element.ALIGN_LEFT));
        mapColumnHeader.put(TeamListItem.HEADCOUNT, new CellData(commonLocalizer.localize(PdfLocalizationName.headCount), Element.ALIGN_LEFT));
        mapColumnHeader.put(TeamListItem.STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        mapColumnHeader.put(TeamListItem.START_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.startDateField), Element.ALIGN_LEFT));

        CustomFieldsUtils.setCustomFieldsPdfHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);
        List<CellData> header = panelTools.getColumnCodeName().stream()
                .filter(columnCode -> mapColumnHeader.containsKey(columnCode))
                .map(columnCode -> mapColumnHeader.get(columnCode))
                .collect(Collectors.toList());
        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));

        for (TeamListItem item : teamListItemListResult.getList()) {
            Map<String, CellData> mapColumns = new HashMap<>();
            if (panelTools.getColumnCodeName().contains(TeamListItem.CODE)) {
                mapColumns.put(TeamListItem.CODE, new CellData(item.getDepartmentCode() != null ? item.getDepartmentCode() : ""));
            }
            if (panelTools.getColumnCodeName().contains(TeamListItem.NAME)) {
                mapColumns.put(TeamListItem.NAME, new CellData(item.getName() != null ? item.getName() : ""));
            }
            if (panelTools.getColumnCodeName().contains(TeamListItem.PARENT_DEPARTMENT)) {
                mapColumns.put(TeamListItem.PARENT_DEPARTMENT, new CellData(item.getParentDepartment() != null ? item.getParentDepartment().getName() : "N/A"));
            }
            if (panelTools.getColumnCodeName().contains(TeamListItem.LEADER_NAME)) {
                mapColumns.put(TeamListItem.LEADER_NAME, new CellData(item.getLeader() != null ? item.getLeader() : ""));
            }
            if (panelTools.getColumnCodeName().contains(TeamListItem.HEADCOUNT)) {
                mapColumns.put(TeamListItem.HEADCOUNT, new CellData(item.getHeadCount() != null ? item.getHeadCount() : ""));
            }
            if (panelTools.getColumnCodeName().contains(TeamListItem.STATUS)) {
                mapColumns.put(TeamListItem.STATUS, new CellData(item.isActive() != null ? item.isActive() ? commonLocalizer.localize(PdfLocalizationName.active) : commonLocalizer.localize(PdfLocalizationName.inactive) : ""));
            }
            if (panelTools.getColumnCodeName().contains(TeamListItem.START_DATE)) {
                mapColumns.put(TeamListItem.START_DATE, new CellData(item.getStartDate() != null ? ServerUtils.convertDateFormatFromEngToUzb(dateFormat(item.getStartDate())) : ""));
            }

            CustomFieldsUtils.setCustomFieldsPdfTableRows(panelTools.getListViewCustomFields(), mapColumns, panelTools.getColumnCodeName(), item, company);
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
        return commonLocalizer.localize(PdfLocalizationName.departmentListTableName);
    }

    public void setDepartmentService(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }
}
