package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: Apr 8, 2010
 * Time: 8:44:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductsServicesStockListPDFHandler extends AbstractITextPostPdfHandler {

    private ProductService productService;

    @Override
    protected String getTableName(Object dataClass) {
        return pdfWfmMessageSource.localize("productOrServicesStockList");
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        ListResult<ProductItem> productList = productService.getStockProductsList(fp);
        List<ProductItem> productItems = productList.getList();
        ListPanelToolRpc panelTools = fp.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        List<CellData> header2 = new ArrayList<>();
        header.remove(ProductItem.ACTION);
        ITextTableList tableList = new ITextTableList(header.size());
        pdfData.setListTable(tableList);
        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(ProductItem.PRODUCT_NUMBER, new CellData(accountingLocalizer.localizeAccounting(PdfLocalizationName.number), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.NAME, new CellData(accountingLocalizer.localizeAccounting(PdfLocalizationName.name), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.DISCRIPTION, new CellData(accountingLocalizer.localizeAccounting(PdfLocalizationName.description), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.COST_PRICE, new CellData(accountingLocalizer.localizeAccounting("costPrice"), Element.ALIGN_RIGHT));
        mapColumnHeader.put(ProductItem.SELING_PRICE, new CellData(accountingLocalizer.localizeAccounting(PdfLocalizationName.salePrice), Element.ALIGN_RIGHT));
        mapColumnHeader.put(ProductItem.TYPE, new CellData(accountingLocalizer.localizeAccounting(PdfLocalizationName.type), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.INVENTORY_TYPE, new CellData("inventoryType", Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.INCOME_ACCOUND, new CellData(accountingLocalizer.localizeAccounting(PdfLocalizationName.incomeAccount), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.COGS_ACCOUND, new CellData(accountingLocalizer.localizeAccounting(PdfLocalizationName.cogsAccount), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.ASSET_ACCOUND, new CellData(accountingLocalizer.localizeAccounting(PdfLocalizationName.assetAccount), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.ITEMS_IN_STOCK, new CellData(commonLocalizer.localize(PdfLocalizationName.onHand), Element.ALIGN_RIGHT));
        mapColumnHeader.put(ProductItem.TAX_RATE, new CellData(accountingLocalizer.localizeAccounting(PdfLocalizationName.taxRate), Element.ALIGN_RIGHT));
        mapColumnHeader.put(ProductItem.OPENING_BALANCE, new CellData(commonLocalizer.localize(PdfLocalizationName.openingBalance), Element.ALIGN_RIGHT));
        mapColumnHeader.put(ProductItem.AS_OF, new CellData(accountingLocalizer.localizeAccounting(PdfLocalizationName.asOF), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.SCUNUMBER, new CellData(accountingLocalizer.localize("EPMinReOrdPoint"), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.WEREHOUSE, new CellData(accountingLocalizer.localize(PdfLocalizationName.warehouse), Element.ALIGN_LEFT));
//        mapColumnHeader.put(ProductItem.LOCATION, "Location");
        for (String aHeader : header) {
            header2.add(mapColumnHeader.get(aHeader));
        }

        tableList.addPdfTableHeader(header2.toArray(new String[]{}));

        for (ProductItem item : productItems) {
            String[] temp = new String[header.size()];
            List<CellData> cell = new ArrayList<>();
            for (int j = 0; j < header.size(); j++) {
                if (ProductItem.PRODUCT_NUMBER.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(item.getProductNumber());
                    cell.add(header.indexOf(ProductItem.PRODUCT_NUMBER), new CellData(temp[j], Element.ALIGN_LEFT));
                } else if (ProductItem.NAME.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(item.getName());
                    cell.add(header.indexOf(ProductItem.NAME), new CellData(temp[j], Element.ALIGN_LEFT));
                } else if (ProductItem.DISCRIPTION.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(item.getDescription());
                    cell.add(header.indexOf(ProductItem.DISCRIPTION), new CellData(temp[j], Element.ALIGN_LEFT));
                } else if (ProductItem.COST_PRICE.equals(header.get(j))) {
                    temp[j] = item.getCostPrice() != null ? getMoneyFormat(item.getCostPrice()) : "—";
                    cell.add(header.indexOf(ProductItem.COST_PRICE), new CellData(temp[j], Element.ALIGN_RIGHT));
                } else if (ProductItem.SELING_PRICE.equals(header.get(j))) {
                    temp[j] = item.getUnitpPrice() != null ? getMoneyFormat(item.getUnitpPrice()) : "—";
                    cell.add(header.indexOf(ProductItem.SELING_PRICE), new CellData(temp[j], Element.ALIGN_RIGHT));
                } else if (ProductItem.TYPE.equals(header.get(j))) {
                    temp[j] = item.getType() != null ? item.getType().toString() : "—";
                    cell.add(header.indexOf(ProductItem.TYPE), new CellData(temp[j], Element.ALIGN_LEFT));
                } else if (ProductItem.INVENTORY_TYPE.equals(header.get(j))) {
                    temp[j] = item.getParentId() != null ? "Variant" : "Product";
                    cell.add(header.indexOf(ProductItem.INVENTORY_TYPE), new CellData(temp[j], Element.ALIGN_LEFT));
                } else if (ProductItem.INCOME_ACCOUND.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(item.getAccount());
                    cell.add(header.indexOf(ProductItem.INCOME_ACCOUND), new CellData(temp[j], Element.ALIGN_LEFT));
                } else if (ProductItem.COGS_ACCOUND.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(item.getCogsAccount());
                    cell.add(header.indexOf(ProductItem.COGS_ACCOUND), new CellData(temp[j], Element.ALIGN_LEFT));
                } else if (ProductItem.ASSET_ACCOUND.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(item.getAssetAccount());
                    cell.add(header.indexOf(ProductItem.ASSET_ACCOUND), new CellData(temp[j], Element.ALIGN_LEFT));
                } else if (ProductItem.ITEMS_IN_STOCK.equals(header.get(j))) {
                    temp[j] = item.getItemsInStock() != null ? getMoneyFormat(item.getItemsInStock()) : "—";
                    cell.add(header.indexOf(ProductItem.ITEMS_IN_STOCK), new CellData(temp[j], Element.ALIGN_RIGHT));
                } else if (ProductItem.TAX_RATE.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(item.getTaxRate());
                    cell.add(header.indexOf(ProductItem.TAX_RATE), new CellData(temp[j], Element.ALIGN_RIGHT));
                } else if (ProductItem.OPENING_BALANCE.equals(header.get(j))) {
                    temp[j] = item.getTotalValue() != null ? getMoneyFormat(item.getTotalValue()) : "—";
                    cell.add(header.indexOf(ProductItem.OPENING_BALANCE), new CellData(temp[j], Element.ALIGN_RIGHT));
                } else if (ProductItem.AS_OF.equals(header.get(j))) {
                    temp[j] = item.getAsOf() != null ? ServerUtils.shortDateFormat(item.getAsOf().getNonConvertedDate(), company) : "—";
                    cell.add(header.indexOf(ProductItem.AS_OF), new CellData(temp[j], Element.ALIGN_LEFT));
                } else if (ProductItem.SCUNUMBER.equals(header.get(j))) {
                    temp[j] = item.getMinReorderPoint() != null ? item.getMinReorderPoint().toString() : "—";
                    cell.add(header.indexOf(ProductItem.SCUNUMBER), new CellData(temp[j], Element.ALIGN_LEFT));
                } else if (ProductItem.WEREHOUSE.equals(header.get(j))) {
                    temp[j] = getResultOrLongDash(item.getWarehouseName());
                    cell.add(header.indexOf(ProductItem.WEREHOUSE), new CellData(temp[j], Element.ALIGN_LEFT));
                } /*else if (ProductItem.LOCATION.equals(header.get(j))) {
                    temp[j] = item.getLocationName() != null ? item.getLocationName() : "—";
                }*/
            }
            tableList.addPdfTableRows(cell.toArray(new CellData[header.size()]));
        }
        pdfData.setListTable(tableList);

        return pdfData;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_StockList_" + dateFormat(new Date()));
    }

    public void setProductService(ProductService productService) {
        this.productService = productService;
    }
}
