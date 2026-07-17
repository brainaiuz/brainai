package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: Apr 8, 2010
 * Time: 7:12:11 PM
 * To change this template use File | Settings | File Templates.
 */
public class ProductsServicesListExcelHandler extends BaseExcelHandler implements Constants {

    private static final Logger log = LoggerFactory.getLogger(ProductsServicesListExcelHandler.class);

    @Autowired
    @Qualifier("accountingLocalizer")
    protected WfmMessageSource accountingLocalizer;
    @Autowired
    private ProductService productService;
    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;

    @Autowired
    @Qualifier("commonLocalizer")
    protected WfmMessageSource commonLocalizer;
    @Override
    protected void setFileName() {
        filename = "Products_Services";
    }

    @Override
    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter fp = (ListingFilterParameter) object;
        EdsUser user = userManager.getUser();
        EdsCompany edsCompany = user.getCompany();
        EdsCompanySettings companySettings = user.getCompany().getCompanySettings();

        if (companySettings.getExcelLimit() != null && !"".equals(companySettings.getExcelLimit()) && !"null".equals(companySettings.getExcelLimit())) {
            fp.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
        } else {
            fp.setLimit(LIMIT_EXCEL_ROW);
        }

        if (ViewName.InventoryItemsView.name().equals(fp.getViewType())) {
            fp.setProductType(1);
        } else if (ViewName.AssemblyItemsView.name().equals(fp.getViewType())) {
            fp.setProductType(4);
        }

        ListResult<ProductItem> productList = productService.getProductsListFromSolr(fp);
        List<ProductItem> productItems = productList.getList();//266 products
        ExcelData[] cellDatas;
        ListPanelToolRpc panelTools = fp.getListPanelTool();
        Map<String, ExcelData> mapColumnHeader = new LinkedHashMap<>();
        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);
            List<ExcelData[]> list = new LinkedList<>();

            mapColumnHeader.put(ProductItem.NAME, new ExcelData(commonLocalizer.localize(PdfLocalizationName.name), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ProductItem.STATUS, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.status), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ProductItem.DISCRIPTION, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.description), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ProductItem.TYPE, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.type), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ProductItem.INVENTORY_TYPE, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.inventoryType), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ProductItem.ACCOUND, new ExcelData(accountingLocalizer.localize("salesAccount"), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ProductItem.TAX_RATE, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.taxRate), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ProductItem.PRODUCT_NUMBER, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.number), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ProductItem.SELING_PRICE, new ExcelData(accountingLocalizer.localize("sellingPrice"), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT));
            mapColumnHeader.put(ProductItem.COST_PRICE, new ExcelData(accountingLocalizer.localize("purchasePrice"), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT));
            mapColumnHeader.put(ProductItem.ITEMS_IN_STOCK, new ExcelData(commonLocalizer.localize(PdfLocalizationName.onHand), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT));
            mapColumnHeader.put(ProductItem.SALE_ORDER_QTY, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.onSaleOrder), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT));
            mapColumnHeader.put(ProductItem.AVAILABLE_STOCK, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.availableStock, "Available Stock"), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT));
            mapColumnHeader.put(ProductItem.COGS_ACCOUND, new ExcelData(accountingLocalizer.localize("purchaseAccount").replace("<br>", ""), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ProductItem.INCOME_ACCOUND, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.incomeAccount), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ProductItem.ASSET_ACCOUND, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.assetAccount), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ProductItem.ON_PURCHASE_ORDER, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.onPurchaseOrder), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT));
            mapColumnHeader.put(ProductItem.AVERAGE_COST + "_", new ExcelData(accountingLocalizer.localize("averageCost"), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ProductItem.Vendor, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.supplier, "Supplier"), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ProductItem.Category, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.category, "Category"), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ProductItem.UNIT_MEASUREMENT_NAME, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.unitMeasurementName, "Unit Measurement"), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ProductItem.BARCODE, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.barcode), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ProductItem.PART_NUMBER, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.partNumber, "Part Number"), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ProductItem.BRAND, new ExcelData(accountingLocalizer.localize(PdfLocalizationName.brand, "Brand"), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ProductItem.CREATOR, new ExcelData(commonLocalizer.localize(PdfLocalizationName.createdBy, "Created By"), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ProductItem.UPDATER, new ExcelData(commonLocalizer.localize(PdfLocalizationName.modifiedBy, "Updated By"), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            mapColumnHeader.put(ProductItem.UPDATED_DATE, new ExcelData(commonLocalizer.localize(PdfLocalizationName.modifiedDate, "Updated Date"), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER));
            CustomFieldsUtils.setCustomFieldsExcelHeaderMap(panelTools.getListViewCustomFields(), mapColumnHeader);

            List<ExcelData> excelDataList = new ArrayList<>();
            for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                if (mapColumnHeader.containsKey(panelTools.getColumnCodeName().get(i))) {
                    excelDataList.add(mapColumnHeader.get(panelTools.getColumnCodeName().get(i)));
                }
            }

            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), edsCompany.getName(), workBook.getSheet(), 0));
            if (ViewName.InventoryItemsView.name().equals(fp.getViewType())) {
                list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), commonLocalizer.localize(PdfLocalizationName.inventoryItems),
                        workBook.getSheet(), 1));
            } else if (ViewName.AssemblyItemsView.name().equals(fp.getViewType())) {
                list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), commonLocalizer.localize(PdfLocalizationName.assemblyItems),
                        workBook.getSheet(), 1));
            } else {
                list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), commonLocalizer.localize(PdfLocalizationName.productsOrServices),
                        workBook.getSheet(), 1));
            }
            list.add(generateOneRowWithValue(panelTools.getColumnCodeName().size(), ServerUtils.getUserLocale().getLanguage().equals("uz") ? ServerUtils.convertToUzbDateFormat(ServerUtils.shortDateFormat(user.getUserDate(new Date()), user)) + " Xolatiga ko'ra" : excelReferenceMessageSource.localize("EPAsOf", " As Of") + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            cellDatas = new ExcelData[excelDataList.size()];
            excelDataList.toArray(cellDatas);
            list.add(cellDatas);

            for (ProductItem product : productItems) {
                BigDecimal availableStock = BigDecimal.ZERO;
                BigDecimal onHand = product.getItemsInStock().compareTo(BigDecimal.ZERO) < 0 ? new BigDecimal(-1).multiply(product.getItemsInStock()) : product.getItemsInStock();
                BigDecimal order = product.getOnSaleOrderQty();

                if (onHand.compareTo(order) > 0) {
                    availableStock = onHand.subtract(order);
                }
                BigDecimal saleOrderQty = product.getItemsInStock().compareTo(BigDecimal.ZERO) < 0 ? new BigDecimal(-1).multiply(product.getItemsInStock()) : product.getItemsInStock();

                Map<String, ExcelData> mapColumn = new HashMap<>();
                if (panelTools.getColumnCodeName().contains(ProductItem.NAME)) {
                    mapColumn.put(ProductItem.NAME, new ExcelData(escapeHtml(product.getName()), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProductItem.STATUS)) {
                    mapColumn.put(ProductItem.STATUS, new ExcelData(escapeHtml(product.isActive() ? accountingLocalizer.localize(PdfLocalizationName.active) : accountingLocalizer.localize(PdfLocalizationName.deactivate)), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProductItem.DISCRIPTION)) {
                    mapColumn.put(ProductItem.DISCRIPTION, new ExcelData(escapeHtml(product.getDescription()), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProductItem.TYPE)) {
                    mapColumn.put(ProductItem.TYPE, new ExcelData(escapeHtml(product.getTypeName()), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProductItem.INVENTORY_TYPE)) {
                    mapColumn.put(ProductItem.INVENTORY_TYPE, new ExcelData(product.getParentId() == null ? "Product" : "Variant", ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProductItem.ACCOUND)) {
                    mapColumn.put(ProductItem.ACCOUND, new ExcelData(escapeHtml(product.getAccount()), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProductItem.TAX_RATE)) {
                    mapColumn.put(ProductItem.TAX_RATE, new ExcelData(escapeHtml(product.getTaxRate()), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProductItem.PRODUCT_NUMBER)) {
                    mapColumn.put(ProductItem.PRODUCT_NUMBER, new ExcelData(escapeHtml(product.getProductNumber()), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProductItem.SELING_PRICE)) {
                    mapColumn.put(ProductItem.SELING_PRICE, new ExcelData(product.getUnitpPrice() != null ? product.getUnitpPrice() : BigDecimal.ZERO, ExcelData.CURRENCY, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProductItem.COST_PRICE)) {
                    mapColumn.put(ProductItem.COST_PRICE, new ExcelData(product.getCostPrice() != null ? product.getCostPrice() : BigDecimal.ZERO, ExcelData.CURRENCY, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProductItem.ITEMS_IN_STOCK)) {
                    mapColumn.put(ProductItem.ITEMS_IN_STOCK, new ExcelData(saleOrderQty, ExcelData.CURRENCY, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProductItem.SALE_ORDER_QTY)) {
                    mapColumn.put(ProductItem.SALE_ORDER_QTY, new ExcelData(product.getOnSaleOrderQty() != null ? product.getOnSaleOrderQty() : BigDecimal.ZERO, ExcelData.CURRENCY, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProductItem.AVAILABLE_STOCK)) {
                    mapColumn.put(ProductItem.AVAILABLE_STOCK, new ExcelData(availableStock, ExcelData.CURRENCY, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProductItem.COGS_ACCOUND)) {
                    mapColumn.put(ProductItem.COGS_ACCOUND, new ExcelData(escapeHtml(product.getCogsAccount()), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProductItem.INCOME_ACCOUND)) {
                    mapColumn.put(ProductItem.INCOME_ACCOUND, new ExcelData(escapeHtml(product.getAccount()), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProductItem.ASSET_ACCOUND)) {
                    mapColumn.put(ProductItem.ASSET_ACCOUND, new ExcelData(escapeHtml(product.getAssetAccount()), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProductItem.ON_PURCHASE_ORDER)) {
                    mapColumn.put(ProductItem.ON_PURCHASE_ORDER, new ExcelData(product.getOnPurchaseOrder() != null ? product.getOnPurchaseOrder() : BigDecimal.ZERO, ExcelData.CURRENCY, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProductItem.AVERAGE_COST + "_")) {
                    mapColumn.put(ProductItem.AVERAGE_COST + "_", new ExcelData(product.getAverageCost() != null ? product.getAverageCost() : BigDecimal.ZERO, ExcelData.CURRENCY, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProductItem.Vendor)) {
                    mapColumn.put(ProductItem.Vendor, new ExcelData(escapeHtml(product.getVendor() != null ? product.getVendor() : ""), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProductItem.Category)) {
                    mapColumn.put(ProductItem.Category, new ExcelData(escapeHtml(product.getCategory()), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProductItem.UNIT_MEASUREMENT_NAME)) {
                    mapColumn.put(ProductItem.UNIT_MEASUREMENT_NAME, new ExcelData(escapeHtml(product.getUnitMeasurementName()), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProductItem.BARCODE)) {
                    mapColumn.put(ProductItem.BARCODE, new ExcelData(escapeHtml(product.getBarCodeString()), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProductItem.PART_NUMBER)) {
                    mapColumn.put(ProductItem.PART_NUMBER, new ExcelData(escapeHtml(product.getPartNumber()), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProductItem.AVERAGE_COST)) {
                    mapColumn.put(ProductItem.AVERAGE_COST, new ExcelData(product.getAverageCost() != null ? product.getAverageCost() : BigDecimal.ZERO, ExcelData.CURRENCY, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProductItem.BRAND)) {
                    mapColumn.put(ProductItem.BRAND, new ExcelData(escapeHtml(product.getBrand()), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProductItem.CREATOR)) {
                    mapColumn.put(ProductItem.CREATOR, new ExcelData(product.getCreator() != null ? escapeHtml(product.getCreator().getName()) : "", ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProductItem.UPDATER)) {
                    mapColumn.put(ProductItem.UPDATER, new ExcelData(product.getUpdater() != null ? escapeHtml(product.getUpdater().getName()) : "", ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                if (panelTools.getColumnCodeName().contains(ProductItem.UPDATED_DATE)) {
                    mapColumn.put(ProductItem.UPDATED_DATE, new ExcelData(escapeHtml(ServerUtils.shortDateFormat(product.getUpdatedDate(), user)), ExcelData.STRING, 25, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL));
                }
                CustomFieldsUtils.setCustomFieldsExcelTableRows(panelTools.getListViewCustomFields(), mapColumn, panelTools.getColumnCodeName(), product, edsCompany);
                excelDataList = new ArrayList<>();
                for (int i = 0; i < panelTools.getColumnCodeName().size(); i++) {
                    if (mapColumn.containsKey(panelTools.getColumnCodeName().get(i))) {
                        excelDataList.add(mapColumn.get(panelTools.getColumnCodeName().get(i)));
                    }
                }
                cellDatas = new ExcelData[excelDataList.size()];
                excelDataList.toArray(cellDatas);
                list.add(cellDatas);
            }
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, 6);
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Cannot generate products/services list excel report, exception: " + ex.getMessage());
        }
        return null;
    }

    private ExcelData getRightValue(String value, int cellSize) {
        ExcelData excelData = new ExcelData(value, ExcelData.STRING, cellSize, true, false, ExcelData.NO_BORDER, ExcelData.HEADER);
        excelData.setHorizontalAlignment(HSSFCellStyle.ALIGN_RIGHT);
        return excelData;
    }

    public String escapeHtml(String value) {
        if (ServerUtils.isNullOrEmpty(value)) {
            return "";
        }
        return value;
    }

    @Override
    protected void setFileName(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        filename = "Products Services";
        if (fp.getViewType() != null && !"".equals(fp.getViewType()) && ViewName.InventoryItemsView.name().equals(fp.getViewType())) {
            filename = "Inventory Items";
        } else if (fp.getViewType() != null && !"".equals(fp.getViewType()) && ViewName.AssemblyItemsView.name().equals(fp.getViewType())) {
            filename = "Assembly Items";
        }
    }
}
