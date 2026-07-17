package com.edatasite.workforce.gwt.core.server.servlets.pdf.crm;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.SolutionItem;
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
 * Time: 18:55:10
 * To change this template use File | Settings | File Templates.
 */
public class SolutionListPDFHandler extends AbstractITextPostPdfHandler {

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
        ListResult<SolutionItem> solutionList = crmService.getSolutionList(filterParametrs);
        List<SolutionItem> holListItems = solutionList.getList();

        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(SolutionItem.ASSIGNEE, new CellData(crmLocalizer.localize(PdfLocalizationName.leadAssignee), Element.ALIGN_LEFT));
        mapColumnHeader.put(SolutionItem.TITLE, new CellData(crmLocalizer.localize(PdfLocalizationName.solutionTitle), Element.ALIGN_LEFT));
        mapColumnHeader.put(SolutionItem.STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        mapColumnHeader.put(SolutionItem.QUESTION, new CellData(crmLocalizer.localize(PdfLocalizationName.question), Element.ALIGN_LEFT));
        mapColumnHeader.put(SolutionItem.ANSWER, new CellData(crmLocalizer.localize(PdfLocalizationName.answer), Element.ALIGN_LEFT));

        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<CellData> header = panelTools.getColumnCodeName().stream()
                .filter(columnCode -> mapColumnHeader.containsKey(columnCode))
                .map(columnCode -> mapColumnHeader.get(columnCode))
                .collect(Collectors.toList());
        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));

        for (SolutionItem solutions : holListItems) {
            Map<String, String> mapColumns = new HashMap<>();
            if (panelTools.getColumnCodeName().contains(SolutionItem.ASSIGNEE)) {
                mapColumns.put(SolutionItem.ASSIGNEE, solutions.getAssignee());
            }
            if (panelTools.getColumnCodeName().contains(SolutionItem.TITLE)) {
                mapColumns.put(SolutionItem.TITLE, solutions.getTitle());
            }
            if (panelTools.getColumnCodeName().contains(SolutionItem.STATUS)) {
                mapColumns.put(SolutionItem.STATUS, solutions.getStatus());
            }
            if (panelTools.getColumnCodeName().contains(SolutionItem.QUESTION)) {
                mapColumns.put(SolutionItem.QUESTION, solutions.getQuestion());
            }
            if (panelTools.getColumnCodeName().contains(SolutionItem.ANSWER)) {
                mapColumns.put(SolutionItem.ANSWER, solutions.getAnswer());
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
    protected String getTableName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        return property != null ? property.getPlural() : pdfWfmMessageSource.localize("solutions");
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_SolutionList_" + dateFormat(new Date()));
    }

    public CRMService getCrmService() {
        return crmService;
    }

    public void setCrmService(CRMService crmService) {
        this.crmService = crmService;
    }
}
