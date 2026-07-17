package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 29.11.2010
 * Time: 19:46:12
 * To change this template use File | Settings | File Templates.
 */
public class ProductsServicesStockListExcelHandler extends BaseExcelHandler {

    private static final Logger log = LoggerFactory.getLogger(ProductsServicesListExcelHandler.class);
    @Autowired
    private ProductService productService;
    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;

    @Override
    protected void setFileName() {
        filename = "Inventory Items";
    }

    @Override
    protected HSSFWorkbook getWorkBook(Object object) {
        ListingFilterParameter fp = (ListingFilterParameter) object;

        fp.setLimit(200);
        ListResult<ProductItem> productList = productService.getStockProductsList(fp);
        List<ProductItem> productItems = productList.getList();
        ExcelData[] cellDatas;
        ListPanelToolRpc panelTools = fp.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        header.remove(ProductItem.ACTION);
        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(ProductItem.PRODUCT_NUMBER, excelReferenceMessageSource.localizeAccounting("EPProductNumber", "Product Number"));
        mapColumnHeader.put(ProductItem.NAME, excelReferenceMessageSource.localizeAccounting("GeneralName2", "Name"));
        mapColumnHeader.put(ProductItem.DISCRIPTION, excelReferenceMessageSource.localizeAccounting("wokrspacedescriptionField", "Description"));
        mapColumnHeader.put(ProductItem.COST_PRICE, excelReferenceMessageSource.localizeAccounting("EPCostPrice", "Cost Price"));
        mapColumnHeader.put(ProductItem.SELING_PRICE, excelReferenceMessageSource.localizeAccounting("EPSelingPrice", "Selling Price"));
        mapColumnHeader.put(ProductItem.TYPE, excelReferenceMessageSource.localizeAccounting("EPType", "Type"));
        mapColumnHeader.put(ProductItem.INVENTORY_TYPE, "Inventory Type");
        mapColumnHeader.put(ProductItem.INCOME_ACCOUND, excelReferenceMessageSource.localizeAccounting("EPIncomeAccount", "Income Account"));
        mapColumnHeader.put(ProductItem.COGS_ACCOUND, excelReferenceMessageSource.localizeAccounting("EPCOGSAccount", "COGS Account"));
        mapColumnHeader.put(ProductItem.ASSET_ACCOUND, excelReferenceMessageSource.localizeAccounting("EPAssetAccount", "Asset Account"));
        mapColumnHeader.put(ProductItem.ITEMS_IN_STOCK, excelReferenceMessageSource.localizeAccounting("EPItemsinStock", "Items in Stock"));
        mapColumnHeader.put(ProductItem.TAX_RATE, excelReferenceMessageSource.localizeAccounting("EPTaxRate", "Tax Rate"));
        mapColumnHeader.put(ProductItem.OPENING_BALANCE, excelReferenceMessageSource.localizeAccounting("EPOpeningBalance", "Opening Balance"));
        mapColumnHeader.put(ProductItem.AS_OF, excelReferenceMessageSource.localizeAccounting("EPAsOf", "As Of"));
        mapColumnHeader.put(ProductItem.SCUNUMBER, excelReferenceMessageSource.localizeAccounting("EPMinReOrdPoint", "Min Re.Ord.Point"));
        mapColumnHeader.put(ProductItem.WEREHOUSE, excelReferenceMessageSource.localizeAccounting("EPWerehous", "Warehouse"));
//        mapColumnHeader.put(ProductItem.LOCATION, excelReferenceMessageSource.localizeAccounting("EPLocation", "Location"));
        try {
            List<ExcelData[]> list = new LinkedList<>();
            cellDatas = new ExcelData[header.size()];

            for (int i = 0; i < header.size(); i++) {
                cellDatas[i] = new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, header.get(i).equals(ProductItem.PRODUCT_NUMBER) || header.get(i).equals(ProductItem.NAME) ? 50 : 20, false, header.get(i).equals(ProductItem.PRODUCT_NUMBER) || header.get(i).equals(ProductItem.NAME) || header.get(i).equals(ProductItem.DISCRIPTION), ExcelData.NO_BORDER, ExcelData.HEADER);
            }

            list.add(cellDatas);

            for (ProductItem item : productItems) {
                String temp = "";
                cellDatas = new ExcelData[header.size()];
                for (int j = 0; j < header.size(); j++) {
                    temp = "";
                    if (ProductItem.PRODUCT_NUMBER.equals(header.get(j))) {
                        temp = item.getProductNumber() != null ? item.getProductNumber() : "";
                    } else if (ProductItem.NAME.equals(header.get(j))) {
                        temp = item.getName() != null ? item.getName() : "";
                    } else if (ProductItem.DISCRIPTION.equals(header.get(j))) {
                        temp = item.getDescription() != null ? item.getDescription() : "";
                    } else if (ProductItem.COST_PRICE.equals(header.get(j))) {
                        temp = item.getCostPrice() != null ? getMoneyFormat(item.getCostPrice()) : "";
                    } else if (ProductItem.SELING_PRICE.equals(header.get(j))) {
                        temp = item.getUnitpPrice() != null ? getMoneyFormat(item.getUnitpPrice()) : "";
                    } else if (ProductItem.TYPE.equals(header.get(j))) {
                        temp = item.getType() != null ? item.getType().toString() : "";
                    } else if (ProductItem.INVENTORY_TYPE.equals(header.get(j))) {
                        temp = item.getParentId() != null ? "Variant" : "Product";
                    } else if (ProductItem.INCOME_ACCOUND.equals(header.get(j))) {
                        temp = item.getAccount() != null ? item.getAccount() : "";
                    } else if (ProductItem.COGS_ACCOUND.equals(header.get(j))) {
                        temp = item.getCogsAccount() != null ? item.getCogsAccount() : "";
                    } else if (ProductItem.ASSET_ACCOUND.equals(header.get(j))) {
                        temp = item.getAssetAccount() != null ? item.getAssetAccount() : "";
                    } else if (ProductItem.ITEMS_IN_STOCK.equals(header.get(j))) {
                        temp = item.getItemsInStock() != null ? getMoneyFormat(item.getItemsInStock()) : "";
                    } else if (ProductItem.TAX_RATE.equals(header.get(j))) {
                        temp = item.getTaxRate() != null ? item.getTaxRate() : "";
                    } else if (ProductItem.OPENING_BALANCE.equals(header.get(j))) {
                        temp = item.getTotalValue() != null ? item.getTotalValue().toString() : "";
                    } else if (ProductItem.AS_OF.equals(header.get(j))) {
                        temp = item.getAsOf() != null ? item.getAsOf().toString() : "";
                    } else if (ProductItem.SCUNUMBER.equals(header.get(j))) {
                        temp = item.getMinReorderPoint() != null ? item.getMinReorderPoint().toString() : "";
                    } else if (ProductItem.WEREHOUSE.equals(header.get(j))) {
                        temp = item.getWarehouseName() != null ? item.getWarehouseName() : "";
                    } /*else if (ProductItem.LOCATION.equals(header.get(j))) {
                        temp = item.getLocationName() != null ? item.getLocationName() : "";
                    }*/
                    cellDatas[j] = new ExcelData(temp, ExcelData.STRING, header.get(j).equals(ProductItem.PRODUCT_NUMBER) || header.get(j).equals(ProductItem.NAME) ? 50 : 20, false, !header.get(j).equals(ProductItem.DISCRIPTION), ExcelData.NO_BORDER, ExcelData.NORMAL);
                }
                list.add(cellDatas);
            }
            return new WorkBook(list).getWorkBook(filename, 0, 0, 0, header.size());
        } catch (Exception ex) {
            ex.printStackTrace();
            log.error("Cannot generate products/services list excel report, exception: " + ex);
        }

        return null;
    }
}
