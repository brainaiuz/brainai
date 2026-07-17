package com.edatasite.workforce.gwt.core.server.servlets.pdf;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsProductPicture;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceLocaleManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ProductPictureManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.ITextImageProperty;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: Apr 8, 2010
 * Time: 8:44:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductsServicesListPDFHandler extends AbstractITextPostPdfHandler {

    @Autowired
    private ItemManager itemManager;
    @Autowired
    private ProductPictureManager productPictureManager;
    @Autowired
    @Qualifier("accountingLocalizer")
    protected WfmMessageSource accountingLocalizer;
    @Autowired
    private ProductService productService;
    @Autowired
    private ReferenceLocaleManager referenceLocaleManager;

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsUser user = uploadManager.getUser();
        EdsCompanySettings companySettings = user.getCompany().getCompanySettings();
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        DecimalFormat priceScaleFormat = getPriceScaleNumberFormat(fs);


        if (companySettings.getPdfLimit() != null && !"".equals(companySettings.getPdfLimit()) && !"null".equals(companySettings.getPdfLimit())) {
            fp.setLimit(Integer.parseInt(companySettings.getPdfLimit()));
        } else {
            fp.setLimit(MAX_PDF_OR_EXCEL_LIMIT);
        }

        ListResult<ProductItem> productList = productService.getProductsListFromSolr(fp);
        List<ProductItem> productItems = productList.getList();
        ListPanelToolRpc panelTools = fp.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        List<CellData> header2 = new ArrayList<>();
        header.remove(ProductItem.ACTION);

        ITextTableList tableList = new ITextTableList(header.size());
        pdfData.setListTable(tableList);
        Map<String, CellData> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(ProductItem.NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.name), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.DISCRIPTION, new CellData(accountingLocalizer.localize(PdfLocalizationName.description), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.COST_PRICE, new CellData(accountingLocalizer.localize("purchasePrice"), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.COST_PRICE + "_", new CellData(accountingLocalizer.localizeAccounting("costPrice"), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.STATUS, new CellData(accountingLocalizer.localize(PdfLocalizationName.status), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.TYPE, new CellData(accountingLocalizer.localize(PdfLocalizationName.type), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.INVENTORY_TYPE, new CellData(commonLocalizer.localize(PdfLocalizationName.inventoryType), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.ACCOUND, new CellData(accountingLocalizer.localize("salesAccount"), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.TAX_RATE, new CellData(accountingLocalizer.localize(PdfLocalizationName.taxRate), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.PRODUCT_NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.number), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.SELING_PRICE, new CellData(accountingLocalizer.localize("sellingPrice"), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.ITEMS_IN_STOCK, new CellData(commonLocalizer.localize(PdfLocalizationName.onHand), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.SALE_ORDER_QTY, new CellData(accountingLocalizer.localize(PdfLocalizationName.onSaleOrder), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.AVAILABLE_STOCK, new CellData(accountingLocalizer.localize(PdfLocalizationName.availableStock, "Available Stock"), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.COGS_ACCOUND, new CellData(commonLocalizer.localize("purchaseAccount"), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.INCOME_ACCOUND, new CellData(accountingLocalizer.localize(PdfLocalizationName.incomeAccount), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.ASSET_ACCOUND, new CellData(accountingLocalizer.localize(PdfLocalizationName.assetAccount), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.ON_PURCHASE_ORDER, new CellData(accountingLocalizer.localize(PdfLocalizationName.onPurchaseOrder), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.AVERAGE_COST + "_", new CellData(accountingLocalizer.localize("averageCost"), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.Vendor, new CellData(accountingLocalizer.localize(PdfLocalizationName.supplier), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.Category, new CellData(accountingLocalizer.localize(PdfLocalizationName.category), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.STOREFRONT, new CellData(commonLocalizer.localize(PdfLocalizationName.storefront), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.UNIT_MEASUREMENT_NAME, new CellData(commonLocalizer.localize(PdfLocalizationName.unitMeasurement), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.PART_NUMBER, new CellData(commonLocalizer.localize(PdfLocalizationName.partNumber), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.PICTURE, new CellData(commonLocalizer.localize(PdfLocalizationName.picture), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.BARCODE, new CellData(commonLocalizer.localize(PdfLocalizationName.barcode), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.BRAND, new CellData(accountingLocalizer.localize(PdfLocalizationName.brand, "Brand"), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.CREATOR, new CellData(commonLocalizer.localize(PdfLocalizationName.createdBy), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.UPDATER, new CellData(commonLocalizer.localize(PdfLocalizationName.modifiedBy), Element.ALIGN_LEFT));
        mapColumnHeader.put(ProductItem.UPDATED_DATE, new CellData(commonLocalizer.localize(PdfLocalizationName.modifiedDate), Element.ALIGN_LEFT));


        CustomFieldsUtils.setCustomFieldsPdfHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);

        for (String aHeader : header) {
            header2.add(mapColumnHeader.get(aHeader));
        }

        tableList.addPdfTableHeader(header2.toArray(new CellData[]{}));

        for (ProductItem product : productItems) {
            String[] temp = new String[header.size()];
            Integer[] tempAligment = new Integer[header.size()];
            String availableStock = "0.00";
            LinkedList<CellData> rowList = new LinkedList<>();

            BigDecimal onHand = product.getItemsInStock().compareTo(BigDecimal.ZERO) < 0 ? new BigDecimal(-1).multiply(product.getItemsInStock()) : product.getItemsInStock();
            BigDecimal order = product.getOnSaleOrderQty();

            if (onHand.compareTo(order) > 0) {
                availableStock = getMoneyFormat(onHand.subtract(order));
            }
            for (int j = 0; j < header.size(); j++) {
                tempAligment[j] = Element.ALIGN_LEFT;
                switch (header.get(j)) {
                    case ProductItem.NAME -> {
                        temp[j] = getResultOrLongDash(product.getName());
                        rowList.add(new CellData(temp[j], tempAligment[j]));
                    }
                    case ProductItem.DISCRIPTION -> {
                        temp[j] = getResultOrLongDash(product.getDescription());
                        rowList.add(new CellData(temp[j], tempAligment[j]));
                    }
                    case ProductItem.STATUS -> {
                        temp[j] = getResultOrLongDash(product.isActive() ? accountingLocalizer.localize(PdfLocalizationName.active) : accountingLocalizer.localize(PdfLocalizationName.deactivate));
                        rowList.add(new CellData(temp[j], tempAligment[j]));
                    }
                    case ProductItem.COST_PRICE -> {
                        temp[j] = product.getCostPrice() != null ? priceScaleFormat.format(product.getCostPrice()) : "0.00";
                        tempAligment[j] = Element.ALIGN_LEFT;
                        rowList.add(new CellData(temp[j], tempAligment[j]));
                    }
                    case (ProductItem.COST_PRICE + "_") -> {
                        for (String key : product.getMultiPrices().keySet()) {
                            if (key.contains(PAYABLE)) {
                                String currency = key.substring(key.lastIndexOf(PAYABLE) + PAYABLE.length());
                                temp[j] = product.getMultiPrices().get(key) != null ? priceScaleFormat.format(product.getMultiPrices().get(key)) + " " + currency : "0.00";
                            }
                        }
                        tempAligment[j] = Element.ALIGN_LEFT;
                        rowList.add(new CellData(temp[j], tempAligment[j]));
                    }
                    case (ProductItem.SELING_PRICE + "_") -> {
                        for (String key : product.getMultiPrices().keySet()) {
                            if (key.contains(RECEIVABLE)) {
                                String currency = key.substring(key.lastIndexOf(RECEIVABLE) + RECEIVABLE.length());
                                temp[j] = product.getMultiPrices().get(key) != null ? priceScaleFormat.format(product.getMultiPrices().get(key)) + " " + currency : "0.00";
                            }
                        }
                        tempAligment[j] = Element.ALIGN_LEFT;
                        rowList.add(new CellData(temp[j], tempAligment[j]));
                    }
                    case ProductItem.TYPE -> {
                        temp[j] = getResultOrLongDash(product.getTypeName());
                        rowList.add(new CellData(temp[j], tempAligment[j]));
                    }
                    case ProductItem.INVENTORY_TYPE -> {
                        temp[j] = product.getParentId() != null ? "Variant" : "Product";
                        rowList.add(new CellData(temp[j], tempAligment[j]));
                    }
                    case ProductItem.ACCOUND -> {
                        temp[j] = getResultOrLongDash(product.getAccount());
                        rowList.add(new CellData(temp[j], tempAligment[j]));
                    }
                    case ProductItem.TAX_RATE -> {
                        temp[j] = getResultOrLongDash(product.getTaxRate());
                        tempAligment[j] = Element.ALIGN_LEFT;
                        rowList.add(new CellData(temp[j], tempAligment[j]));
                    }
                    case ProductItem.PRODUCT_NUMBER -> {
                        temp[j] = getResultOrLongDash(product.getProductNumber());
                        rowList.add(new CellData(temp[j], tempAligment[j]));
                    }
                    case ProductItem.SELING_PRICE -> {
                        temp[j] = product.getUnitpPrice() != null ? priceScaleFormat.format(product.getUnitpPrice()) : "0.00";
                        tempAligment[j] = Element.ALIGN_LEFT;
                        rowList.add(new CellData(temp[j], tempAligment[j]));
                    }
                    case ProductItem.ITEMS_IN_STOCK -> {
                        temp[j] = product.getItemsInStock().compareTo(BigDecimal.ZERO) < 0 ? "(" + getMoneyFormat(new BigDecimal(-1).multiply(product.getItemsInStock())) + ")" : getMoneyFormat(product.getItemsInStock());
                        tempAligment[j] = Element.ALIGN_LEFT;
                        rowList.add(new CellData(temp[j], tempAligment[j]));
                    }
                    case ProductItem.SALE_ORDER_QTY -> {
                        temp[j] = getMoneyFormat(product.getOnSaleOrderQty());
                        tempAligment[j] = Element.ALIGN_LEFT;
                        rowList.add(new CellData(temp[j], tempAligment[j]));
                    }
                    case ProductItem.AVAILABLE_STOCK -> {
                        tempAligment[j] = Element.ALIGN_LEFT;
                        temp[j] = availableStock;
                        rowList.add(new CellData(temp[j], tempAligment[j]));
                    }
                    case ProductItem.COGS_ACCOUND -> {
                        temp[j] = getResultOrLongDash(product.getCogsAccount());
                        rowList.add(new CellData(temp[j], tempAligment[j]));
                    }
                    case ProductItem.INCOME_ACCOUND -> {
                        temp[j] = getResultOrLongDash(product.getAccount());
                        rowList.add(new CellData(temp[j], tempAligment[j]));
                    }
                    case ProductItem.ASSET_ACCOUND -> {
                        temp[j] = getResultOrLongDash(product.getAssetAccount());
                        rowList.add(new CellData(temp[j], tempAligment[j]));
                    }
                    case ProductItem.Vendor -> {
                        temp[j] = getResultOrLongDash(product.getVendor());
                        rowList.add(new CellData(temp[j], tempAligment[j]));
                    }
                    case ProductItem.ON_PURCHASE_ORDER -> {
                        temp[j] = getMoneyFormat(product.getOnPurchaseOrder() != null ? product.getOnPurchaseOrder() : BigDecimal.ZERO);
                        tempAligment[j] = Element.ALIGN_LEFT;
                        rowList.add(new CellData(temp[j], tempAligment[j]));
                    }
                    case (ProductItem.AVERAGE_COST + "_") -> {
                        if (product.getAverageCost() != null) {
                            temp[j] = getMoneyFormat(product.getAverageCost());
                        } else {
                            temp[j] = "0.00";
                        }
                        tempAligment[j] = Element.ALIGN_LEFT;
                        rowList.add(new CellData(temp[j], tempAligment[j]));
                    }
                    case ProductItem.Category -> {
                        temp[j] = getResultOrLongDash(product.getCategory());
                        rowList.add(new CellData(temp[j], tempAligment[j]));
                    }
                    case ProductItem.STOREFRONT -> {
                        temp[j] = product.isStorefrontEnable() != null && product.isStorefrontEnable() ? commonLocalizer.localize(PdfLocalizationName.yes) : commonLocalizer.localize(PdfLocalizationName.no);
                        rowList.add(new CellData(temp[j], tempAligment[j]));
                    }
                    case ProductItem.PICTURE -> {
                        EdsProductPicture picture = productPictureManager.getProductDefaultPicture(itemManager.get(product.getObjectId()));
                        if (picture != null && picture.isDefaultPicture()) {
                            String pictureUrl = uploadManager.getFileURL(picture);
                            if (!StringUtil.isEmpty(pictureUrl)) {
                                CellData imageCell = new CellData(ITextTableList.CELL_IMAGE);
                                imageCell.setLink(pictureUrl);

                                ITextImageProperty imageProperty = new ITextImageProperty();
                                imageProperty.setWidth(30);
                                imageProperty.setHeight(30);
                                imageCell.setImageProperty(imageProperty);
                                imageCell.setAlignment(Element.ALIGN_LEFT);
                                rowList.add(imageCell);
                            } else {
                                temp[j] = "—";
                                rowList.add(new CellData(temp[j]));
                            }
                        } else {
                            temp[j] = "—";
                            rowList.add(new CellData(temp[j]));
                        }
                    }
                    case ProductItem.UNIT_MEASUREMENT_NAME -> {
                        temp[j] = getResultOrLongDash(product.getUnitMeasurementName());
                        rowList.add(new CellData(temp[j], tempAligment[j]));
                    }
                    case ProductItem.PART_NUMBER -> {
                        temp[j] = getResultOrLongDash(product.getPartNumber());
                        rowList.add(new CellData(temp[j], tempAligment[j]));
                    }
                    case ProductItem.BARCODE -> {
                        temp[j] = getResultOrLongDash(product.getBarCodeString());
                        rowList.add(new CellData(temp[j], tempAligment[j]));
                    }
                    case ProductItem.BRAND -> {
                        temp[j] = getResultOrLongDash(product.getBrand());
                        rowList.add(new CellData(temp[j], tempAligment[j]));
                    }
                    case ProductItem.CREATOR -> {
                        temp[j] = getResultOrLongDash(product.getCreator() != null ? product.getCreator().getName() : "");
                        rowList.add(new CellData(temp[j], tempAligment[j]));
                    }
                    case ProductItem.UPDATER -> {
                        temp[j] = getResultOrLongDash(product.getUpdater() != null ? product.getUpdater().getName() : "");
                        rowList.add(new CellData(temp[j], tempAligment[j]));
                    }
                    case ProductItem.UPDATED_DATE -> {
                        String updatedDate = product.getUpdatedDate() != null ? product.getUpdatedDate().toString() : "";
                        temp[j] = getResultOrLongDash(ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(updatedDate) : updatedDate);
                        rowList.add(new CellData(temp[j], tempAligment[j]));
                    }
                    default -> {
                        if (product.getCustomFieldMap().get(header.get(j)) instanceof Date) {
                            temp[j] = dateFormat((Date) product.getCustomFieldMap().get(header.get(j)));
                        } else
                            temp[j] = product.getCustomFieldMap().get(header.get(j)) != null ? product.getCustomFieldMap().get(header.get(j)).toString() : "—";
                        rowList.add(new CellData(temp[j], tempAligment[j]));
                    }
                }
            }
            tableList.addPdfTableRows(rowList.toArray(new CellData[]{}));
        }

        pdfData.setListTable(tableList);


        return pdfData;
    }

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) {
        return null;
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        if (fp.getPropertyCode().equals("inventoryitems")) {
            return property != null ? property.getPlural() : pdfWfmMessageSource.localize("inventoryItems");
        } else if (fp.getPropertyCode().equals("productsOrServices")) {
            return property != null ? property.getPlural() : pdfWfmMessageSource.localize("productsOrServices");
        }
        return null;
    }

    @Override
    protected boolean isListingPDF() {
        return true;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        String filename = "ProductsServicesList";
        if (fp.getViewType() != null && !"".equals(fp.getViewType()) && ViewName.InventoryItemsView.name().equals(fp.getViewType())) {
            filename = "InventoryItems";
        }
        setFileName(user.getFirstName() + "_" + user.getLastName() + "_" + filename + "_" + dateFormat(new Date()));
    }

}
