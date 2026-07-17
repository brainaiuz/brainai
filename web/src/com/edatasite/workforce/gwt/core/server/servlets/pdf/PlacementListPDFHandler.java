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
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.hrms.client.rpc.PlacementItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.RecruitmentService;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class PlacementListPDFHandler extends AbstractITextPostPdfHandler {

    private RecruitmentService recruitmentService;

    public void setRecruitmentService(RecruitmentService recruitmentService) {
        this.recruitmentService = recruitmentService;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        filterParametrs.setAllByFilter(false);
        EdsCompanySettings companySettings = company.getCompanySettings();
        filterParametrs.setLimit(StringUtils.isNotEmpty(companySettings.getPdfLimit()) ? Integer.parseInt(companySettings.getPdfLimit().equals("null") ? "0" : companySettings.getPdfLimit()) : LIMIT_PDF_ROWS);
        ListResult<PlacementItem> candidateListResult = recruitmentService.getPlacementList(filterParametrs);

        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(PlacementItem.PLACEMENT_CANDIDATE_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.candidate), Element.ALIGN_LEFT));
        mapColumnHeader.put(PlacementItem.PLACEMENT_POSITION_OFFERED, new CellData(commonLocalizer.localize(PdfLocalizationName.position), Element.ALIGN_LEFT));
        mapColumnHeader.put(PlacementItem.PLACEMENT_DATE_OFFERED, new CellData(commonLocalizer.localize(PdfLocalizationName.dateOffered), Element.ALIGN_LEFT));
        mapColumnHeader.put(PlacementItem.PLACEMENT_STATUS_OFFER, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        mapColumnHeader.put(PlacementItem.PLACEMENT_CODE, new CellData(commonLocalizer.localize(PdfLocalizationName.number), Element.ALIGN_LEFT));


        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        if (panelTools.isCustomFieldsShown()) {
            CustomFieldsUtils.setCustomFieldsPdfHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);
        }
        List<CellData> header = panelTools.getColumnCodeName().stream()
                .filter(mapColumnHeader::containsKey)
                .map(mapColumnHeader::get)
                .collect(Collectors.toList());

        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));

        for (PlacementItem item : candidateListResult.getList()) {
            Map<String, CellData> mapColumns = new HashMap<>();
            if (mapColumnHeader.containsKey(PlacementItem.PLACEMENT_CANDIDATE_NAME)) {
                mapColumns.put(PlacementItem.PLACEMENT_CANDIDATE_NAME, new CellData(getResultOrLongDash(item.getCandidateName()) == null ? "" : getResultOrLongDash(item.getCandidateName())));
            }
            if (mapColumnHeader.containsKey(PlacementItem.PLACEMENT_POSITION_OFFERED)) {
                mapColumns.put(PlacementItem.PLACEMENT_POSITION_OFFERED, new CellData(getResultOrLongDash(item.getPositionName()) == null ? "" : getResultOrLongDash(item.getPositionName())));
            }
            if (mapColumnHeader.containsKey(PlacementItem.PLACEMENT_DATE_OFFERED)) {
                mapColumns.put(PlacementItem.PLACEMENT_DATE_OFFERED, new CellData(item.getDateOffed() != null ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(dateFormat(item.getDateOffed())) : dateFormat(item.getDateOffed())) : "—"));
            }
            if (mapColumnHeader.containsKey(PlacementItem.PLACEMENT_STATUS_OFFER)) {
                mapColumns.put(PlacementItem.PLACEMENT_STATUS_OFFER, new CellData(getResultOrLongDash(item.getStatusName()) == null ? "" : getResultOrLongDash(item.getStatusName())));
            }
            if (mapColumnHeader.containsKey(PlacementItem.PLACEMENT_CODE)) {
                mapColumns.put(PlacementItem.PLACEMENT_CODE, new CellData(getResultOrLongDash(item.getPlacementCode()) == null ? "" : getResultOrLongDash(item.getPlacementCode())));
            }
            CustomFieldsUtils.setCustomFieldsPdfTableRows(panelTools.getListViewCustomFields(), mapColumns, panelTools.getColumnCodeName(), item, company);
            List<CellData> columns = panelTools.getColumnCodeName().stream()
                    .filter(mapColumns::containsKey)
                    .map(mapColumns::get)
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
        ListingFilterParameter filterParameter = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(filterParameter.getPropertyCode());
        return property != null ? property.getPlural() : pdfWfmMessageSource.localize("placementList");
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName("Placements");
    }
}
