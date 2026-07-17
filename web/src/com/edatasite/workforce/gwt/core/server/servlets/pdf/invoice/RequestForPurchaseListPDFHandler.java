package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.invoice.client.rpc.RFPData;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Omonullo on 6/4/2017.
 */
public class RequestForPurchaseListPDFHandler extends AbstractITextPostPdfHandler {

    @Autowired
    QuoteService quoteService;

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        if (fp.getPropertyCode().equals("requestforpurchase")) {
            return property != null ? property.getPlural() : pdfWfmMessageSource.localize("rfp");
        } else {
            return property != null ? property.getPlural() : pdfWfmMessageSource.localize("requestForQuote");
        }
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    public ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        filterParametrs.setAllByFilter(true);
        filterParametrs.setForExportOnly(true);
        filterParametrs.setStart(0);
        EdsUser user = uploadManager.getUser();
        EdsCompany edsCompany = user.getCompany();
        EdsCompanySettings companySettings = edsCompany.getCompanySettings();
        if (companySettings.getPdfLimit() != null && !"".equals(companySettings.getPdfLimit())) {
            filterParametrs.setLimit(Integer.parseInt(companySettings.getPdfLimit()));
        } else {
            filterParametrs.setLimit(LIMIT_PDF_ROWS);
        }

        ListResult<RFPData> rfplist = quoteService.getRFPList(filterParametrs);
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<RFPData> rfpItems = rfplist.getList();
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        Map<String, CellData> columnHeaderMap = new HashMap<>();
        columnHeaderMap.put(RFPData.CREATOR, new CellData(commonLocalizer.localize(PdfLocalizationName.employee), Element.ALIGN_LEFT));
        columnHeaderMap.put(RFPData.DUE_DATE, new CellData(accountingLocalizer.localize(PdfLocalizationName.dueDate), Element.ALIGN_LEFT));
        columnHeaderMap.put(RFPData.MANAGER, new CellData(commonLocalizer.localize(PdfLocalizationName.currentApprover), Element.ALIGN_LEFT));
        columnHeaderMap.put(RFPData.NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.number), Element.ALIGN_LEFT));
        columnHeaderMap.put(RFPData.RELATED_PROJECT, new CellData(accountingLocalizer.localize(PdfLocalizationName.project), Element.ALIGN_LEFT));
        columnHeaderMap.put(RFPData.LOCATION, new CellData(propertManager.findByCode(Constants.LOCATION_PROPERTY_OBJECTNAME) != null ? propertManager.findByCode("LocListView").getSingular() : commonLocalizer.localize(PdfLocalizationName.location), Element.ALIGN_LEFT));
        columnHeaderMap.put(RFPData.STATUS, new CellData(accountingLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        columnHeaderMap.put(RFPData.CREATED_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.createdDate), Element.ALIGN_LEFT));
        columnHeaderMap.put(RFPData.CUSTOMER, new CellData(commonLocalizer.localize(PdfLocalizationName.customer), Element.ALIGN_LEFT));
        List<CellData> header = new ArrayList<>();
        header.add(new CellData(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.number), Element.ALIGN_LEFT));
        for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
            if (columnHeaderMap.containsKey(panelTools.getColumnCodeName().get(i))) {
                header.add(columnHeaderMap.get(panelTools.getColumnCodeName().get(i)));
            }
        }
        ITextTableList tableList = new ITextTableList(header.size());
        pdfData.setListTable(tableList);
        tableList.addPdfTableHeader(header.toArray(new CellData[]{}));
        int count = 1;
        for (RFPData item : rfpItems) {
            Map<String, String> columnMap = new HashMap<>();
            String counter = String.valueOf(count);
            if (panelTools.getColumnCodeName().contains(RFPData.CREATOR)) {
                SelectItem creator = item.getCreator();
                columnMap.put(RFPData.CREATOR, creator != null ? getResultOrLongDash(creator.getName()) : "—");
            }
            if (panelTools.getColumnCodeName().contains(RFPData.DUE_DATE)) {
                columnMap.put(RFPData.DUE_DATE, item.getDueDate() != null ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(item.getDueDate(), edsCompany)) : ServerUtils.shortDateFormat(item.getDueDate(), edsCompany)) : "—");
            }
            if (panelTools.getColumnCodeName().contains(RFPData.MANAGER)) {
                SelectItem manager = item.getCurrentApprover();
                columnMap.put(RFPData.MANAGER, manager != null ? getResultOrLongDash(manager.getName()) : "—");
            }
            if (panelTools.getColumnCodeName().contains(RFPData.NUMBER)) {
                NumberData numberData = item.getNumberData();
                columnMap.put(RFPData.NUMBER, numberData != null ? getResultOrLongDash(numberData.getNumberString()) : "—");
            }
            if (panelTools.getColumnCodeName().contains(RFPData.RELATED_PROJECT)) {
                SelectItem relatedProject = item.getRelatedProject();
                columnMap.put(RFPData.RELATED_PROJECT, relatedProject != null ? getResultOrLongDash(relatedProject.getName()) : "—");
            }
            if (panelTools.getColumnCodeName().contains(RFPData.CREATED_DATE)) {
                columnMap.put(RFPData.CREATED_DATE, item.getCreatedDate() != null ? (ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(item.getCreatedDate(), edsCompany)) : ServerUtils.shortDateFormat(item.getCreatedDate(), edsCompany)) : "—");
            }
            if (panelTools.getColumnCodeName().contains(RFPData.CUSTOMER)) {
                columnMap.put(RFPData.CUSTOMER, item.getCustomer() != null ? getResultOrLongDash(item.getCustomer().getName()) : "—");
            }

            if (panelTools.getColumnCodeName().contains(RFPData.STATUS)) {
                String status = "";
                if (DRAFT.equals(item.getStatus())) {
                    status = accountingLocalizer.localize(PdfLocalizationName.draft);
                } else if (SUBMITTED_TO_MANAGER.equals(item.getStatus())) {
                    status = accountingLocalizer.localize(PdfLocalizationName.submittedToManager);
                } else if (APPROVE.equals(item.getStatus())) {
                    status = accountingLocalizer.localize(PdfLocalizationName.approved);
                } else if (REJECT.equals(item.getStatus())) {
                    status = commonLocalizer.localize(PdfLocalizationName.rejected);
                } else if (CONVERTED.equals(item.getStatus())) {
                    status = accountingLocalizer.localize(PdfLocalizationName.converted);
                } else {
                    status = item.getStatus();
                }
                columnMap.put(RFPData.STATUS, getResultOrLongDash(status));
            }
            List column = new ArrayList<String>();
            column.add(counter);
            for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                if (columnMap.containsKey(panelTools.getColumnCodeName().get(i))) {
                    column.add(columnMap.get(panelTools.getColumnCodeName().get(i)));
                }
            }
            String[] colArray = new String[column.size()];
            column.toArray(colArray);
            tableList.addPdfTableRows(colArray);
            count++;
        }

        return pdfData;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user != null ? user.getFirstName() + "_" + user.getLastName() + "_RFPList_" + dateFormat(new Date()) : "RFP");
    }
}
