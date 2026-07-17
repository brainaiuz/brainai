package com.edatasite.workforce.gwt.core.server.servlets.pdf;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 13.12.2008
 * Time: 19:23:29
 * To change this template use File | Settings | File Templates.
 */

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.lowagie.text.Document;
import com.lowagie.text.pdf.PdfWriter;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;


public class InvoiceItemListPDFHandler extends AbstractITextPostPdfHandler {

    private ItemManager itemManager;

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ITextTableList tableList = new ITextTableList(7);
        pdfData.setListTable(tableList);
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        EdsUser user = uploadManager.getUser();
        List<EdsItem> itemList = itemManager.getCompanyItemList(/*user.getCompany(), */filterParametrs);
        DecimalFormat numberFormat = new DecimalFormat("#,##0.00");
        tableList.addPdfTableHeader(pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.number),
                accountingLocalizer.localizeAccounting(PdfLocalizationName.productOrServiceName),
                commonLocalizer.localizeAccounting(PdfLocalizationName.description),
                commonLocalizer.localizeAccounting(PdfLocalizationName.type),
                pdfWfmMessageSource.localizeAccounting(PdfLocalizationName.quantity),
                accountingLocalizer.localizeAccounting(PdfLocalizationName.unitPrice),
                commonLocalizer.localize(PdfLocalizationName.cost),
                accountingLocalizer.localizeAccounting(PdfLocalizationName.vat));
        for (int i = 0; i < itemList.size(); i++) {
            EdsItem item = itemList.get(i);
            if (item != null) {
                String counter = String.valueOf(i + 1);
                String name = getResultOrLongDash(item.getName());
                String description = getResultOrLongDash(item.getDescription());
                String type = getResultOrLongDash(item.getTypeName());
                String quantity = item.getQty() != null ? item.getQty().toString() : "—";
                String uprice = item.getUnitPrice() != null ? item.getUnitPrice().toString() : "—";
                String ucost = item.getUnitCost() != null ? item.getUnitCost().toString() : "—";
                String vat = item.getVat() != null && item.getVat().getName() != null ? item.getVat().getName() : "—";
                tableList.addPdfTableRows(counter, name, description, type, quantity, uprice, ucost, vat);
            }
        }
        return pdfData;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected String getTableName(Object dataClass) {
        return accountingLocalizer.localizeAccounting(PdfLocalizationName.productsServicesList);
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_InvoiceItemList_" + dateFormat(new Date()));
    }

    public void setItemManager(ItemManager itemManager) {
        this.itemManager = itemManager;
    }
}
