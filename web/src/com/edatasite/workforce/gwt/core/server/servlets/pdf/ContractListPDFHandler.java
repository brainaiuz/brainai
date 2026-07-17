package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.project.client.rpc.ContractListItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
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

/**
 * Created by Muhammad on 20.02.2016.
 */
public class ContractListPDFHandler extends AbstractITextPostPdfHandler {

    private ProjectService projectService;

    @Override
    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_ContractList_" + dateFormat(new Date()));
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        EdsUser user = uploadManager.getUser();
        ListingFilterParameter filterParameters = (ListingFilterParameter) dataClass;
        filterParameters.setAllByFilter(false);
        ListLoadConfig config = new ListLoadConfig();

        config.setSortField(filterParameters.getSortField());
        EdsCompanySettings companySettings = company.getCompanySettings();
        filterParameters.setLimit(StringUtils.isNotEmpty(companySettings.getPdfLimit()) ? Integer.parseInt(companySettings.getPdfLimit()) : LIMIT_PDF_ROWS);
        config.setLimit(StringUtils.isNotEmpty(companySettings.getPdfLimit()) ? Integer.parseInt(companySettings.getPdfLimit()) : LIMIT_PDF_ROWS);

        ListResult<ContractListItem> contractList = projectService.getContractList(filterParameters);
        ListPanelToolRpc panelTools = filterParameters.getListPanelTool();

        Map<String, CellData> columnHeaderMap = new HashMap<>();
        columnHeaderMap.put(ContractListItem.NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.number), Element.ALIGN_LEFT));
        columnHeaderMap.put(ContractListItem.CLIENT, new CellData(commonLocalizer.localize(PdfLocalizationName.customer), Element.ALIGN_LEFT));
        columnHeaderMap.put(ContractListItem.PROJECT, new CellData(commonLocalizer.localize(PdfLocalizationName.project), Element.ALIGN_LEFT));
        columnHeaderMap.put(ContractListItem.LAST_NOTE_COMMENT, new CellData(commonLocalizer.localize(PdfLocalizationName.notes), Element.ALIGN_LEFT));
        columnHeaderMap.put(ContractListItem.CONTRACT_START_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.contractStart), Element.ALIGN_LEFT));
        columnHeaderMap.put(ContractListItem.CONTRACT_END_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.contractEnd), Element.ALIGN_LEFT));
        columnHeaderMap.put(ContractListItem.CONTRACT_REGISTRATION_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.dateOfRegistration), Element.ALIGN_LEFT));

        List<CellData> header = panelTools.getColumnCodeName().stream()
                .filter(columnCode -> columnHeaderMap.containsKey(columnCode))
                .map(columnCode -> columnHeaderMap.get(columnCode))
                .collect(Collectors.toList());
        ITextTableList tableList = new ITextTableList(header.size());
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));

        for (ContractListItem contract : contractList.getList()) {
            Map<String, String> columnMap = new HashMap<>();
            if (panelTools.getColumnCodeName().contains(ContractListItem.NUMBER)) {
                columnMap.put(ContractListItem.NUMBER, getResultOrLongDash(contract.getNumber()));
            }
            if (panelTools.getColumnCodeName().contains(ContractListItem.CLIENT)) {
                columnMap.put(ContractListItem.CLIENT, getResultOrLongDash(contract.getClient()));
            }
            if (panelTools.getColumnCodeName().contains(ContractListItem.PROJECT)) {
                columnMap.put(ContractListItem.PROJECT, getResultOrLongDash(contract.getProject()));
            }
            if (panelTools.getColumnCodeName().contains(ContractListItem.LAST_NOTE_COMMENT)) {
                columnMap.put(ContractListItem.LAST_NOTE_COMMENT, getResultOrLongDash(contract.getLastNoteComment()));
            }
            if (panelTools.getColumnCodeName().contains(ContractListItem.CONTRACT_START_DATE)) {
                columnMap.put(ContractListItem.CONTRACT_START_DATE, contract.getContractBeginDate() != null ? dateFormat(user.getUserDate(contract.getContractBeginDate().getNonConvertedDate()), true) : "—");
            }
            if (panelTools.getColumnCodeName().contains(ContractListItem.CONTRACT_END_DATE)) {
                columnMap.put(ContractListItem.CONTRACT_END_DATE, contract.getContractEndDate() != null ? dateFormat(user.getUserDate(contract.getContractEndDate().getNonConvertedDate()), true) : "—");
            }
            if (panelTools.getColumnCodeName().contains(ContractListItem.CONTRACT_REGISTRATION_DATE)) {
                columnMap.put(ContractListItem.CONTRACT_REGISTRATION_DATE, contract.getCreationTime() != null ? getCompanyShortDateFormat(company).format(contract.getCreationTime().getNonConvertedDate()) : "—");
            }
            List<String> columns = panelTools.getColumnCodeName().stream()
                    .filter(columnCode -> columnMap.containsKey(columnCode))
                    .map(columnCode -> columnMap.get(columnCode))
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
        return property != null ? property.getPlural() : pdfWfmMessageSource.localize("contracts");
    }

    public void setProjectService(ProjectService projectService) {
        this.projectService = projectService;
    }
}
