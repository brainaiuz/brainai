package com.edatasite.workforce.gwt.core.server.servlets.pdf.crm;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.CampaignItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
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
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 13.08.2009
 * Time: 17:37:20
 * To change this template use File | Settings | File Templates.
 */
public class CampaignListPDFHandler extends AbstractITextPostPdfHandler {

    private CRMService crmService;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        EdsCompanySettings companySettings = company.getCompanySettings();
        filterParametrs.setLimit(StringUtils.isNotEmpty(companySettings.getPdfLimit()) ? Integer.parseInt(companySettings.getPdfLimit()) : LIMIT_PDF_ROWS);
        ListResult<CampaignItem> campaignList = crmService.getCampaigns(filterParametrs);

        HashMap<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(CampaignItem.OWNER, new CellData(commonLocalizer.localize(PdfLocalizationName.owner), Element.ALIGN_LEFT));
        mapColumnHeader.put(CampaignItem.NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.name), Element.ALIGN_LEFT));
        mapColumnHeader.put(CampaignItem.TYPE, new CellData(commonLocalizer.localize(PdfLocalizationName.type), Element.ALIGN_LEFT));
        mapColumnHeader.put(CampaignItem.STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        mapColumnHeader.put(CampaignItem.START_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.startDateField), Element.ALIGN_LEFT));
        mapColumnHeader.put(CampaignItem.END_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.endDateField), Element.ALIGN_LEFT));

        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<CellData> header = panelTools.getColumnCodeName().stream()
                .filter(columnCode -> mapColumnHeader.containsKey(columnCode))
                .map(columnCode -> mapColumnHeader.get(columnCode))
                .collect(Collectors.toList());
        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));

        for (CampaignItem items : campaignList.getList()) {
            Map<String, String> mapColumns = new HashMap<>();
            if (panelTools.getColumnCodeName().contains(CampaignItem.OWNER)) {
                mapColumns.put(CampaignItem.OWNER, items.getAssignee());
            }
            if (panelTools.getColumnCodeName().contains(CampaignItem.NAME)) {
                mapColumns.put(CampaignItem.NAME, items.getName());
            }
            if (panelTools.getColumnCodeName().contains(CampaignItem.TYPE)) {
                mapColumns.put(CampaignItem.TYPE, items.getType());
            }
            if (panelTools.getColumnCodeName().contains(CampaignItem.STATUS)) {
                mapColumns.put(CampaignItem.STATUS, items.getStatus());
            }
            if (panelTools.getColumnCodeName().contains(CampaignItem.START_DATE)) {
                if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                    mapColumns.put(CampaignItem.START_DATE, items.getStartDate() == null ? "" : ServerUtils.convertToUzbDateFormat(dateFormat(items.getStartDate())));
                } else {
                    mapColumns.put(CampaignItem.START_DATE, items.getStartDate() == null ? "" : dateFormat(items.getStartDate()));
                }
            }
            if (panelTools.getColumnCodeName().contains(CampaignItem.END_DATE)) {
                if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
                    mapColumns.put(CampaignItem.END_DATE, items.getStartDate() == null ? "" : ServerUtils.convertToUzbDateFormat(dateFormat(items.getEndDate())));
                } else {
                    mapColumns.put(CampaignItem.END_DATE, items.getEndDate() == null ? "" : dateFormat(items.getEndDate()));
                }

            }
            List<String> columns = panelTools.getColumnCodeName().stream()
                    .filter(columnCode -> mapColumns.containsKey(columnCode))
                    .map(columnCode -> mapColumns.get(columnCode))
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
        return property != null ? property.getPlural() : pdfWfmMessageSource.localize("campaigns");
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_CampaignList_" + dateFormat(new Date()));
    }

    public CRMService getCrmService() {
        return crmService;
    }

    public void setCrmService(CRMService crmService) {
        this.crmService = crmService;
    }
}
