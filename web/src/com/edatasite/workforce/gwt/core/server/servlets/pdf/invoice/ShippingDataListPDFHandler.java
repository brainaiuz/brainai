package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.WordUtils;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingData;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ShippingDataListPDFHandler extends AbstractITextPostPdfHandler {

    @Autowired
    QuoteService quoteService;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        EdsUser user = uploadManager.getUser();
        String shortDateFormat = "MMM dd, yyyy";
        ListingFilterParameter filterParameter = (ListingFilterParameter) dataClass;
        filterParameter.setLimit(LIMIT_PDF_ROWS);
        filterParameter.setFromExcelPDF(true);
        EdsCompanySettings companySettings = user.getCompany().getCompanySettings();
        if (companySettings != null) {
            shortDateFormat = companySettings.getShortDateFormat();
            if (StringUtils.isNotBlank(companySettings.getPdfLimit())) {
                filterParameter.setLimit(Integer.parseInt(companySettings.getPdfLimit()));
            }
        }

        ITextGenericPdfData pdfData = new ITextGenericPdfData();

        ListPanelToolRpc panelTools = filterParameter.getListPanelTool();
        List<String> headerColumns = panelTools.getColumnCodeName();
        ArrayList<CellData> headerData = new ArrayList<>();
        headerColumns.remove("action");

        ITextTableList tableList = new ITextTableList(headerColumns.size());
        pdfData.setListTable(tableList);

        HashMap<String, CellData> columnHeadersMap = new HashMap<>();

        columnHeadersMap.put(ShippingData.ORDER_NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.orderNumber), Element.ALIGN_LEFT));
        columnHeadersMap.put(ShippingData.NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.number), Element.ALIGN_LEFT));
        columnHeadersMap.put(ShippingData.STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        columnHeadersMap.put(ShippingData.INVOICE_NUMBER, new CellData(accountingLocalizer.localize(PdfLocalizationName.invoiceNumber), Element.ALIGN_LEFT));
        columnHeadersMap.put(ShippingData.INVOICE_STATUS, new CellData(commonLocalizer.localize(PdfLocalizationName.invoiceStatus), Element.ALIGN_LEFT));
        columnHeadersMap.put(ShippingData.SUPPLIER, new CellData(commonLocalizer.localize(PdfLocalizationName.supplier), Element.ALIGN_LEFT));
        columnHeadersMap.put(ShippingData.DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.shipDate), Element.ALIGN_LEFT));
        columnHeadersMap.put(ShippingData.CREATOR, new CellData(commonLocalizer.localize(PdfLocalizationName.createdBy), Element.ALIGN_LEFT));

        for (String column : headerColumns) {
            headerData.add(columnHeadersMap.get(column));
        }
        tableList.addPdfTableHeader(headerData.toArray(new CellData[]{}));

        ListResult<ShippingData> result = quoteService.getShippingDataForListing(filterParameter);
        if (result != null) {
            for (ShippingData shippingData : result.getList()) {
                Map<String, CellData> columnMap = new HashMap<>();
                columnMap.put(ShippingData.ORDER_NUMBER, shippingData.getOrderNumber() != null ? new CellData(shippingData.getOrderNumber()) : new CellData(commonLocalizer.localize(PdfLocalizationName.na)));
                columnMap.put(ShippingData.NUMBER, shippingData.getNumber() != null ? new CellData(shippingData.getNumber()) : new CellData(commonLocalizer.localize(PdfLocalizationName.na)));
                columnMap.put(ShippingData.STATUS, shippingData.getStatus() != null ? new CellData(WordUtils.uppercaseFirstLetterOnly(shippingData.getStatus().name())) : new CellData(commonLocalizer.localize(PdfLocalizationName.na)));
                columnMap.put(ShippingData.INVOICE_NUMBER, shippingData.getInvoiceNumber() != null ? new CellData(shippingData.getInvoiceNumber()) : new CellData(commonLocalizer.localize(PdfLocalizationName.na)));
                columnMap.put(ShippingData.INVOICE_STATUS, shippingData.getInvoiceStatus() != null ? new CellData(shippingData.getInvoiceStatus()) : new CellData(commonLocalizer.localize(PdfLocalizationName.na)));
                columnMap.put(ShippingData.SUPPLIER, shippingData.getClientName() != null ? new CellData(shippingData.getClientName()) : new CellData(commonLocalizer.localize(PdfLocalizationName.na)));
                columnMap.put(ShippingData.DATE, new CellData(ServerUtils.dateFormat(shippingData.getShippingDate() != null ? shippingData.getShippingDate().getNonConvertedDate() : null, shortDateFormat)));
                columnMap.put(ShippingData.CREATOR, shippingData.getCreatorName() != null ? new CellData(shippingData.getCreatorName()) : new CellData(commonLocalizer.localize(PdfLocalizationName.na)));
                List<CellData> columns = panelTools.getColumnCodeName().stream()
                        .filter(columnCode -> columnMap.containsKey(columnCode))
                        .map(columnCode -> columnMap.get(columnCode))
                        .collect(Collectors.toList());
                tableList.addPdfTableRows(columns.toArray(new CellData[]{}));
            }
        }
        ITextGenericPdfData pdfData2 = new ITextGenericPdfData();
        pdfData2.setListTable(tableList);
        return pdfData2;
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        if (fp.getPropertyCode().equals("goodsdeliverednotes")) {
            return property != null ? property.getPlural() : pdfWfmMessageSource.localize("goodsDeliveredNotes");
        } else if (fp.getPropertyCode().equals("goodsreceivednotes")) {
            return property != null ? property.getPlural() : pdfWfmMessageSource.localize("goodsReceivedNotes");
        }
        return null;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        ListingFilterParameter filterParameter = (ListingFilterParameter) dataClass;
        String fileName = "Shipping_data";
        if (filterParameter != null) {
            fileName = filterParameter.isGdn() ? "Goods_Delivered_Note" : "Goods_Received_Note";
        }
        fileName += "_" + dateFormat(user.getUserDate());
        setFileName(fileName);
    }
}
