package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingMethod;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Jun 26, 2010
 * Time: 4:04:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class ShippingMethodsListPDFHandler extends AbstractITextPostPdfHandler {
    @Autowired
    private InvoiceService invoiceService;

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected String getTableName(Object dataClass) {
        return pdfWfmMessageSource.localize(PdfLocalizationName.shippingMethodsList);
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    public ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;

        ListResult<ShippingMethod> shippinglist = invoiceService.getShippingMethodData(filterParametrs);
        List<ShippingMethod> positionItems = shippinglist.getList();
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        filterParametrs.setLimit(1000);

        List<String> header = panelTools.getColumnCodeName();
        List<CellData> header2 = new ArrayList<>();
        header.remove(ShippingMethod.ACTION);
        ITextTableList tableList = new ITextTableList(header.size());
        pdfData.setListTable(tableList);
        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(ShippingMethod.PRICE, new CellData(commonLocalizer.localize(PdfLocalizationName.price), Element.ALIGN_RIGHT));
        mapColumnHeader.put(ShippingMethod.DESCRIPTION, new CellData(commonLocalizer.localize(PdfLocalizationName.description), Element.ALIGN_LEFT));
        mapColumnHeader.put(ShippingMethod.NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.name), Element.ALIGN_LEFT));
        mapColumnHeader.put(ShippingMethod.TAXRATE, new CellData(commonLocalizer.localize(PdfLocalizationName.taxRate), Element.ALIGN_LEFT));
        for (String aHeader : header) {
            header2.add(mapColumnHeader.get(aHeader));
        }

        tableList.addPdfTableHeader(header2.toArray(new CellData[]{}));

        Integer calculationScale = getCalculationScale();
        for (ShippingMethod pickList : positionItems) {
            String[] temp = new String[header.size()];
            List<CellData> cell = new ArrayList<>();
            for (int j = 0; j < header.size(); j++) {
                if (ShippingMethod.PRICE.equals(header.get(j))) {
                    temp[j] = "" + (pickList.getPrice() != null ? pickList.getPrice() : BigDecimal.ZERO).setScale(calculationScale, BigDecimal.ROUND_HALF_UP);
                    cell.add(header.indexOf(ShippingMethod.PRICE), new CellData(temp[j], Element.ALIGN_RIGHT));
                } else if (ShippingMethod.DESCRIPTION.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(pickList.getDescription());
                    cell.add(header.indexOf(ShippingMethod.DESCRIPTION), new CellData(temp[j], Element.ALIGN_LEFT));
                } else if (ShippingMethod.NAME.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(pickList.getName());
                    cell.add(header.indexOf(ShippingMethod.NAME), new CellData(temp[j], Element.ALIGN_LEFT));
                } else if (ShippingMethod.TAXRATE.equals(header.get(j))) {
                    temp[j] = pickList.getTaxItem() != null && pickList.getTaxItem().getName() != null ? pickList.getTaxItem().getName() : "—";
                    cell.add(header.indexOf(ShippingMethod.TAXRATE), new CellData(temp[j], Element.ALIGN_LEFT));
                }
            }
            tableList.addPdfTableRows(cell.toArray(new CellData[header.size()]));
        }

        pdfData.setListTable(tableList);
        return pdfData;

    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFullName() + "_" + "Shipping_Methods_List" + dateFormat(new Date()));
    }

}
