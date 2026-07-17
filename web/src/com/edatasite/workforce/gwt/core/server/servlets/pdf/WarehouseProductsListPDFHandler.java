package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductLocationItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.WarehouseItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WarehouseProductsListPDFHandler  extends AbstractITextPostPdfHandler{
    private AccountingService accountingService;

    @Override
    protected String getTableName(Object dataClass) {
        return commonLocalizer.localize(PdfLocalizationName.productsOrServices);
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    public ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ListingFilterParameter filterParametrs = (ListingFilterParameter) dataClass;
        filterParametrs.setLimit(1000);
        EdsUser user = uploadManager.getUser();
        EdsCompanySettings companySettings = user.getCompany().getCompanySettings();
        ListResult<ProductLocationItem> listResult = accountingService.getWarehouseProductsList(filterParametrs);

        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        DecimalFormat priceScaleFormat = getPriceScaleNumberFormat(fs);

        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        List<CellData> header2 = new ArrayList<>();

        header.remove(WarehouseItem.ACTION);
        ITextTableList tableList = new ITextTableList(header.size());
        pdfData.setListTable(tableList);

        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(ProductLocationItem.NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.number), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductLocationItem.NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.name), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductLocationItem.QTY, new CellData(commonLocalizer.localize(PdfLocalizationName.qty), Element.ALIGN_RIGHT));
        mapColumnHeader.put(ProductLocationItem.MINQTY, new CellData(commonLocalizer.localize(PdfLocalizationName.minReorderQuantity), Element.ALIGN_RIGHT));
        mapColumnHeader.put(ProductLocationItem.AVERAGE_COST, new CellData(commonLocalizer.localize(PdfLocalizationName.averageUnitPrice), Element.ALIGN_RIGHT));
        mapColumnHeader.put(ProductLocationItem.TOTAL, new CellData(commonLocalizer.localize(PdfLocalizationName.total), Element.ALIGN_RIGHT));

        for (String aHeader : header) {
            header2.add(mapColumnHeader.get(aHeader));
        }
        tableList.addPdfTableHeader(header2.toArray(new CellData[]{}));

        List<ProductLocationItem> productList = listResult.getList();
        for (ProductLocationItem item : productList) {
            String[] temp = new String[header.size()];
            List<CellData> cell = new ArrayList<>();
            for (int j = 0; j < header.size(); j++) {
                if (ProductLocationItem.NUMBER.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(item.getProduct_number());
                    cell.add(header.indexOf(ProductLocationItem.NUMBER), new CellData(temp[j], Element.ALIGN_LEFT));
                }
                if (ProductLocationItem.NAME.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(item.getProductName());
                    cell.add(header.indexOf(ProductLocationItem.NAME), new CellData(temp[j], Element.ALIGN_LEFT));
                }
                if (ProductLocationItem.QTY.equals(header.get(j))) {
                    temp[j] = item.getQty() != null ? priceScaleFormat.format(item.getQty()) : priceScaleFormat.format(BigDecimal.ZERO);
                    cell.add(header.indexOf(ProductLocationItem.QTY), new CellData(temp[j], Element.ALIGN_RIGHT));
                }
                if (ProductLocationItem.MINQTY.equals(header.get(j))) {
                    temp[j] = item.getMinReorderQty() != null ? priceScaleFormat.format(item.getMinReorderQty()) : priceScaleFormat.format(BigDecimal.ZERO);
                    cell.add(header.indexOf(ProductLocationItem.MINQTY), new CellData(temp[j], Element.ALIGN_RIGHT));
                }
                if (ProductLocationItem.AVERAGE_COST.equals(header.get(j))) {
                    temp[j] = item.getAverageCost() != null ? priceScaleFormat.format(item.getAverageCost()) : priceScaleFormat.format(BigDecimal.ZERO);
                    cell.add(header.indexOf(ProductLocationItem.AVERAGE_COST), new CellData(temp[j], Element.ALIGN_RIGHT));
                }
                if (ProductLocationItem.TOTAL.equals(header.get(j))) {
                    temp[j] = item.getTotal() != null ? priceScaleFormat.format(item.getTotal()) : priceScaleFormat.format(BigDecimal.ZERO);
                    cell.add(header.indexOf(ProductLocationItem.TOTAL), new CellData(temp[j], Element.ALIGN_RIGHT));
                }
            }
            tableList.addPdfTableRows(cell.toArray(new CellData[header.size()]));
        }

        pdfData.setListTable(tableList);
        return pdfData;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_WareHousesList_" + dateFormat(new Date()));
    }

    public void setAccountingService(AccountingService accountingService) {
        this.accountingService = accountingService;
    }
}
