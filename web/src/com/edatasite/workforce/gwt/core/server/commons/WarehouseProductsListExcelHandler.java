package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductLocationItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListPanelToolRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class WarehouseProductsListExcelHandler extends BaseExcelHandler {
    private static final Logger log = LoggerFactory.getLogger(WarehouseListExcelHandler.class);

    @Autowired
    private AccountingService accountingService;

    @Autowired
    private PropertManager propertManager;

    @Autowired
    @Qualifier("allReferenceWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;



    @Override
    protected HSSFWorkbook getWorkBook(Object object) {

        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        EdsUser user = userManager.getUser();
        EdsCompanySettings companySettings = user.getCompany().getCompanySettings();
        if (companySettings.getExcelLimit() != null && !"".equals(companySettings.getExcelLimit().trim()) && !"null".equals(companySettings.getExcelLimit().trim())) {
            filterParametrs.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
        } else {
            filterParametrs.setLimit(LIMIT_EXCEL_ROW);
        }
        ListResult<ProductLocationItem> listResult = accountingService.getWarehouseProductsList(filterParametrs);
        List<ProductLocationItem> productList = listResult.getList();
        ListPanelToolRpc panelTools = filterParametrs.getListPanelTool();
        List<String> header = panelTools.getColumnCodeName();
        if (header.contains("Action")) {
            header.remove("Action");
        } else {
            header.remove("action");
        }
        header.remove(ProductLocationItem.ACTION);

        Map<String, String> mapColumnHeader = new HashMap<>();
        mapColumnHeader.put(ProductLocationItem.NUMBER, commonLocalizer.localize(PdfLocalizationName.number));
        mapColumnHeader.put(ProductLocationItem.NAME, commonLocalizer.localize(PdfLocalizationName.name));
        mapColumnHeader.put(ProductLocationItem.QTY, commonLocalizer.localize(PdfLocalizationName.qty));
        mapColumnHeader.put(ProductLocationItem.MINQTY, commonLocalizer.localize(PdfLocalizationName.minReorderQuantity));
        mapColumnHeader.put(ProductLocationItem.AVERAGE_COST, commonLocalizer.localize(PdfLocalizationName.averageUnitPrice));
        mapColumnHeader.put(ProductLocationItem.TOTAL, commonLocalizer.localize(PdfLocalizationName.total));

        try {
            WorkBook workBook = new WorkBook(true, 0, 1, 0, 1);
            workBook.setSheetName(filename);

            List<ExcelData[]> list = new LinkedList<>();
            ExcelData[] cellExcelHeaders = new ExcelData[header.size()];
            ExcelData[] cellExcelDatas;

            list.add(generateOneRowWithValue(header.size() + 1, user.getCompany().getName(), workBook.getSheet(), 0));
            list.add(generateOneRowWithValue(header.size() + 1, commonLocalizer.localize(PdfLocalizationName.productsOrServices), workBook.getSheet(), 1));
            list.add(generateOneRowWithValue(header.size() + 1, excelReferenceMessageSource.localize("EPAsOf", " As Of") + "  " + ServerUtils.shortDateFormat(user.getUserDate(new Date()), user), workBook.getSheet(), 2));

            for (int i = 0; i < header.size(); i++) {
                cellExcelHeaders[i] = new ExcelData(mapColumnHeader.get(header.get(i)), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            }
            list.add(cellExcelHeaders);

            for (ProductLocationItem item : productList) {
                cellExcelDatas = new ExcelData[header.size()];
                for (int j = 0; j < header.size(); j++) {
                    if (ProductLocationItem.NUMBER.equals(header.get(j))) {
                        cellExcelDatas[j] = new ExcelData(item.getProduct_number(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    } else if (ProductLocationItem.NAME.equals(header.get(j))) {
                        cellExcelDatas[j] = new ExcelData(item.getProductName(), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    } else if (ProductLocationItem.QTY.equals(header.get(j))) {
                        cellExcelDatas[j] = new ExcelData(item.getQty() != null ? item.getQty() : BigDecimal.ZERO, ExcelData.BIG_DECIMAL, 50, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    } else if (ProductLocationItem.MINQTY.equals(header.get(j))) {
                        cellExcelDatas[j] = new ExcelData(item.getMinReorderQty() != null ? item.getMinReorderQty() : BigDecimal.ZERO, ExcelData.BIG_DECIMAL, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    } else if (ProductLocationItem.AVERAGE_COST.equals(header.get(j))) {
                        cellExcelDatas[j] = new ExcelData(item.getAverageCost() != null ? item.getAverageCost() : BigDecimal.ZERO, ExcelData.BIG_DECIMAL, 20, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    } else if (ProductLocationItem.TOTAL.equals(header.get(j))) {
                        cellExcelDatas[j] = new ExcelData(item.getTotal() != null ? item.getTotal() : BigDecimal.ZERO, ExcelData.BIG_DECIMAL, 30, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);
                    }
                }
                list.add(cellExcelDatas);
            }
            workBook.setList(list);
            return workBook.getWorkBook(filename, 0, 0, 0, header.size());
        } catch (Exception e) {
            e.printStackTrace();
            log.error("Warehouse Products list excel export issue, exception: " + e);
        }

        return null;
    }

//    @Override
//    protected void setFileName() {
//        filename = "Warehouse Products List";
//    }

    @Override
    protected void setFileName(Object object) {
        ListingFilterParameter filterParametrs = (ListingFilterParameter) object;
        EdsProperty property = propertManager.findByCode(filterParametrs.getPropertyCode());
        filename = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.product);
    }

    @Override
    protected void setFileName() {

    }

}
