package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxList;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Aug 10, 2009
 * Time: 3:37:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class TaxRatesListPDFHandler extends AbstractITextPostPdfHandler {

    private InvoiceService invoiceService;
    private TaxList list;


    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected String getTableName(Object dataClass) {
        return accountingLocalizer.localize(PdfLocalizationName.taxRatesList).replaceFirst("[{][0][}]", "");
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    public ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;

        ListResult<TaxListItem> solutionList = invoiceService.getAccountingTaxList(filterParametrs).getTaxList();
        List<TaxListItem> holListItems = solutionList.getList();
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        filterParametrs.setLimit(1000);
        List<String> header = panelTools.getColumnCodeName();
        List<CellData> header2 = new ArrayList<>();
        header.remove(TaxListItem.ACTION);
        ITextTableList tableList = new ITextTableList(header.size());
        pdfData.setListTable(tableList);
        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(TaxListItem.NAME, new CellData(accountingLocalizer.localize(PdfLocalizationName.taxName), Element.ALIGN_LEFT));
        mapColumnHeader.put(TaxListItem.TAXRATE, new CellData(accountingLocalizer.localize(PdfLocalizationName.taxRate), Element.ALIGN_RIGHT));
        for (String aHeader : header) {
            header2.add(mapColumnHeader.get(aHeader));
        }

        tableList.addPdfTableHeader(header2.toArray(new CellData[]{}));

        Integer calculationScale = getCalculationScale();
        for (TaxListItem tax : holListItems) {
            String[] temp = new String[header.size()];
            List<CellData> cell = new ArrayList<>();
            for (int j = 0; j < header.size(); j++) {
                if (TaxListItem.NAME.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(tax.getName());
                    cell.add(header.indexOf(TaxListItem.NAME), new CellData(temp[j], Element.ALIGN_LEFT));
                } else if (TaxListItem.TAXRATE.equals(header.get(j))) {
                    temp[j] = "" + (tax.getPercent() != null ? tax.getPercent() : BigDecimal.ZERO).setScale(calculationScale, BigDecimal.ROUND_HALF_UP);
                    cell.add(header.indexOf(TaxListItem.TAXRATE), new CellData(temp[j], Element.ALIGN_RIGHT));
                }
            }
            tableList.addPdfTableRows(cell.toArray(new CellData[header.size()]));
        }

        pdfData.setListTable(tableList);
        return pdfData;
//        ITextGenericPdfData pdfData = new ITextGenericPdfData();
//        ITextTableList tableList = new ITextTableList(2);
//        pdfData.setListTable(tableList);
//        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
//        EdsUser user = (EdsUser) uploadManager.getUser();
//
//        ListLoadConfig config = new ListLoadConfig();
//        config.setStart(filterParametrs.getStart() != null ? filterParametrs.getStart() : 0);
////        config.setLimit(200);
//        if (filterParametrs.getSortField() != null) {
//            config.setSortField(filterParametrs.getSortField());
//        }
//        config.setSortDir(filterParametrs.getSortDir() != null ? filterParametrs.getSortDir() : 0);
//        pdfData.setTableName(user.getFullName() + "'s " + "Tax Rates List");
//        tableList.addPdfTableHeader("Name", "Tax Rate");
//        list = invoiceService.getCompanyTaxList(filterParametrs, config);
//        if (list != null && list.getTaxItems() != null && list.getTaxItems().length > 0) {
//            for (TaxItem item : list.getTaxItems()) {
//                String name = item.getName() != null ? item.getName() : "N/A";
//                String percent = String.valueOf(item.getTaxPercent());
//                tableList.addPdfTableRows(name, percent);
//            }
//        }
//        return pdfData;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_TaxRatesList_" + dateFormat(user.getUserDate()));
    }

    public void setInvoiceService(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }
}
