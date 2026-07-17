package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.shared.poiutils.WorkBook;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsWarehouse;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.InventoryStockData;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.InventoryStockValuation;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.InventoryStockValuationItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.WarehouseManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmResourceBundleMessageSource;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Atabek Boboyev
 * Date: 19.03.12
 * Time: 12:56
 * To change this template use File | Settings | File Templates.
 */
public class StockValuationExcelHandler extends BaseExcelHandler implements Constants {
    @Autowired
    private ProductService productService;
    @Autowired
    private WarehouseManager warehouseManager;
    @Autowired
    private PropertManager propertManager;
    private String sheetName;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    @Qualifier("pdfWfmMessageSource")
    protected WfmResourceBundleMessageSource excelReferenceMessageSource;
    @Autowired
    private UploadManager uploadManager;

    private final int iCellSize = 15;
    private DecimalFormat priceScaleNumberFormat;

    public void setExcelReferenceMessageSource(WfmResourceBundleMessageSource excelReferenceMessageSource) {
        this.excelReferenceMessageSource = excelReferenceMessageSource;
    }

    @Override
    protected void setFileName() {
        filename = "Stock_Valuation_" + dateFormat(uploadManager.getUser().getUserDate());
    }

    @Override
    protected HSSFWorkbook getWorkBook(Object object) {
        EdsUser user = uploadManager.getUser();
        EdsCompany company = user.getCompany();
        ListingFilterParameter fp = (ListingFilterParameter) object;
        EdsProperty property = propertManager.findByCode(fp.getPropertyCode());
        sheetName = property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.stockValuation);
        Date fromDate = parseFilterParameterDate(fp.getStartDateNC());
        Date toDate = parseFilterParameterDate(fp.getEndDateNC());
        fp.setFromExcelPDF(true);
        EdsCompanySettings companySettings = user.getCompany().getCompanySettings();
        fp.setStart(0); // stock valuation always downloaded fully
        if (companySettings.getExcelLimit() != null && !"".equals(companySettings.getExcelLimit())) {
            fp.setLimit(Integer.parseInt(companySettings.getExcelLimit()));
        } else {
            fp.setLimit(MAX_PDF_OR_EXCEL_LIMIT);
        }

        InventoryStockData stockData = productService.getStockValuations(fp, (fromDate != null ? new DateNonConvertable(fromDate) : null), (toDate != null ? new DateNonConvertable(toDate) : null));
        InventoryStockValuation[] stockValuations = stockData.getStockValuations();
        List<ExcelData[]> list = new LinkedList<>();
        List<ExcelData[]> list2 = new LinkedList<>();

        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();

        String currencySymbol = fs.getCurrency().getSymbol();
        String currencyCode = fs.getCurrency().getName();
        priceScaleNumberFormat = getPriceScaleNumberFormat(fs);

        currencySymbol = currencySymbol != null ? currencySymbol : "";

        list.add(new ExcelData[]{
                new ExcelData("", ExcelData.STRING, 31, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL)
        });
        list2.add(new ExcelData[]{
                new ExcelData("", ExcelData.STRING, 31, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL)
        });

        ExcelData titleData = ExcelData.getReportNameData(sheetName, 31, 9);

        ExcelData companyData = ExcelData.getReportNameChildData(user.getCompany().getName(), 31, 9);

        ExcelData dateData;
        if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
            dateData = ExcelData.getReportNameChildData(commonLocalizer.localize(PdfLocalizationName.from) + " "
                    + ServerUtils.convertToUzbDateFormat(Utils.formatDate(fromDate, company)) + " " + commonLocalizer.localize(PdfLocalizationName.to) + " "
                    + ServerUtils.convertToUzbDateFormat(Utils.formatDate(toDate, company)), 31, 9);
        } else {
            dateData = ExcelData.getReportNameChildData(commonLocalizer.localize(PdfLocalizationName.from) + " "
                    + Utils.formatDate(fromDate, company) + " " + commonLocalizer.localize(PdfLocalizationName.to) + " "
                    + Utils.formatDate(toDate, company), 31, 9);
        }


        ExcelData currencyData = ExcelData.getReportNameChildData(accountingLocalizer.localize(PdfLocalizationName.figuresIn) + " " + currencySymbol + "(" + currencyCode + ")", 31, 9);

        list.add(new ExcelData[]{titleData});
        list.add(new ExcelData[]{companyData});
        list.add(new ExcelData[]{dateData});
        list.add(new ExcelData[]{currencyData});

        ExcelData emptyData = new ExcelData("", ExcelData.STRING, 31, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
        ExcelData[] cellEmptyHeader = new ExcelData[]{
                emptyData
        };
        list.add(cellEmptyHeader);
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        if (fs.getEnableMultiWarehouse() && fp.getWarehouseID() != null) {
            ExcelData warehouse;
            EdsWarehouse edswarehouse = warehouseManager.get(fp.getWarehouseID());
            warehouse = new ExcelData(excelReferenceMessageSource.localize(PdfLocalizationName.warehouse) + " " + edswarehouse.getName(), ExcelData.STRING, 50, false, true, ExcelData.NO_BORDER, ExcelData.HEADER);
            ExcelData[] warehouseItem = new ExcelData[]{warehouse};
            list.add(warehouseItem);
        }
        ExcelData[] cellHeader;

        ExcelData typeData = new ExcelData(accountingLocalizer.localize(PdfLocalizationName.type), ExcelData.STRING, 31, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT);
        ExcelData entryDateData = new ExcelData(accountingLocalizer.localize(PdfLocalizationName.entryDate), ExcelData.STRING, 12, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT);
        ExcelData transactionDateData = new ExcelData(accountingLocalizer.localize(PdfLocalizationName.transactionDate), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT);
        ExcelData nameData = new ExcelData(accountingLocalizer.localize(PdfLocalizationName.name), ExcelData.STRING, 31, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT);
        ExcelData noData = new ExcelData(accountingLocalizer.localize(PdfLocalizationName.number), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT);
        ExcelData qtyData = new ExcelData(accountingLocalizer.localize(PdfLocalizationName.qty), ExcelData.STRING, 10, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
        ExcelData transactionValueData = new ExcelData(accountingLocalizer.localize(PdfLocalizationName.transactionValue), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
        ExcelData costPerQtyData = new ExcelData(commonLocalizer.localize(PdfLocalizationName.costPerQty), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
        ExcelData quantityOnHandData = new ExcelData(excelReferenceMessageSource.localize(PdfLocalizationName.quantityOnHand), ExcelData.STRING, iCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
        ExcelData balanceValueData = new ExcelData(accountingLocalizer.localize(PdfLocalizationName.balanceValue), ExcelData.STRING, iCellSize, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
        typeData.setBold(true);
        entryDateData.setBold(true);
        transactionDateData.setBold(true);
        nameData.setBold(true);
        noData.setBold(true);
        cellHeader = new ExcelData[]{
                typeData, entryDateData, transactionDateData, nameData, noData, qtyData, transactionValueData, costPerQtyData, quantityOnHandData, balanceValueData
        };
        list.add(cellHeader);
        list2.add(cellHeader);
        drawLastEndingBalance(list, PdfLocalizationName.beginningBalance, stockData.getBeginningBalance());

        boolean isAllowableRange = true;
        if (stockValuations != null && stockValuations.length > 0) {
            for (InventoryStockValuation stockValuation : stockValuations) {
                isAllowableRange = isAllowableRange && list.size() + stockValuation.getStockValuationItems().length <= 65535;
                if (isAllowableRange) {
                    createGroup(stockValuation, list, company);
                    list.add(cellEmptyHeader);
                } else {
                    createGroup(stockValuation, list2, company);
                    list2.add(cellEmptyHeader);
                }
            }
        } else {
            list.add(emptyCellData());
        }

        drawLastEndingBalance(isAllowableRange ? list : list2, PdfLocalizationName.endingBalance, stockData.getEndingBalance());

        WorkBook wb = new WorkBook(list);

        HSSFWorkbook swb = wb.getWorkBook(filename, 0, 0, 0, 3);
        if (!isAllowableRange) {
            wb.setList(list2);
            swb = wb.getWorkBook(filename + "_part_2", 0, 0, 0, 3);
        }
        // Set the columns to repeat from column 0 to 2 on the first sheet
        swb.setRepeatingRowsAndColumns(0, 0, 8, 0, 5);

        return swb;

    }

    private boolean drawLastEndingBalance(List<ExcelData[]> list, String balanceString, BigDecimal endingBalance2) {
        return list.add(getBalanceBookMarkCell(commonLocalizer.localize(balanceString), endingBalance2));
    }

    private void createGroup(InventoryStockValuation stockValuation, List<ExcelData[]> activeList, EdsCompany company) {
        activeList.add(drawBookMark(stockValuation.getProductCode() + "->" + stockValuation.getName()));

        InventoryStockValuationItem[] stockValuationItems = stockValuation.getStockValuationItems();

        //inventory stock beginning balance and quantity
        BigDecimal balanceValue = stockValuation.getBeginningBalance();
        BigDecimal qtyOnHand = stockValuation.getBeginningQty();

        //draw beginning balance group book mark
        drawBeginningOrEndingBalance(activeList, accountingLocalizer.localize(PdfLocalizationName.beginningBalance), qtyOnHand, balanceValue);

        ExcelData nameCell, qtyOnHandCell, balanceValueCell, qtyCell, tranValueCell, costPerQtyCell, entryDateCell, tranDateCell, clientOrSupplierNameCell, numberCell;

        if (stockValuationItems != null && stockValuationItems.length > 0) {
            for (InventoryStockValuationItem stockValuationItem : stockValuationItems) {
                if (stockValuationItem.getTransactionType().equals(TT_STOCK_ADJUSTMENT)) {
                    nameCell = new ExcelData(TT_STOCK_ADJUSTMENT_STR, ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
                } else if (stockValuationItem.getTransactionType().equals(TT_OPENING_BALANCE)) {
                    nameCell = new ExcelData(TT_OPENING_BALANCE_STR, ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
                } else if (stockValuationItem.getTransactionType().equals(TT_BUILD_ASSEMBLY)) {
                    nameCell = new ExcelData(TT_BUILD_ASSEMBLY_STR, ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
                } else if (stockValuationItem.getTransactionType().equals(TT_GOODS_RECEIVED)) {
                    nameCell = new ExcelData(TT_GOODS_RECEIVED_STR, ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
                } else if (stockValuationItem.getTransactionType().equals(TT_GOODS_DELIVERED)) {
                    nameCell = new ExcelData(TT_GOODS_DELIVERED_STR, ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
                } else if (stockValuationItem.getTransactionType().equals(TT_PURCHASE)) {
                    nameCell = new ExcelData(accountingLocalizer.localize("purchase"), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
                } else if (stockValuationItem.getTransactionType().equals(TT_INVOICE)) {
                    nameCell = new ExcelData(accountingLocalizer.localize("invoiceStockValuation"), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
                } else if (stockValuationItem.getTransactionType().equals(TT_CUSTOMER_CREDIT_NOTE)) {
                    nameCell = new ExcelData(TT_CUSTOMER_CREDIT_NOTE_STR, ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
                } else if (stockValuationItem.getTransactionType().equals(TT_SUPPLIER_CREDIT_NOTE)) {
                    nameCell = new ExcelData(TT_SUPPLIER_CREDIT_NOTE_STR, ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
                } else {
                    nameCell = new ExcelData("", ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
                }
                entryDateCell = new ExcelData(Utils.formatDate(stockValuationItem.getEntryDate().getNonConvertedDate(), company), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
                tranDateCell = new ExcelData(Utils.formatDate(stockValuationItem.getTransactionDate().getNonConvertedDate(), company), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
                clientOrSupplierNameCell = new ExcelData(stockValuationItem.getName() != null ? stockValuationItem.getName() : "-", ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
                numberCell = new ExcelData(stockValuationItem.getNumber() != null ? stockValuationItem.getNumber() : "", ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
                qtyOnHand = qtyOnHand.add(stockValuationItem.getQty());
                balanceValue = balanceValue.add(stockValuationItem.getTransactionValue());

                qtyCell = new ExcelData(priceScaleNumberFormat.format(stockValuationItem.getQty().setScale(priceScaleNumberFormat.getMaximumFractionDigits(), RoundingMode.HALF_UP)), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
                tranValueCell = new ExcelData(priceScaleNumberFormat.format(stockValuationItem.getTransactionValue().setScale(priceScaleNumberFormat.getMaximumFractionDigits(), RoundingMode.HALF_UP)), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
                costPerQtyCell = new ExcelData(stockValuationItem.getQuantityPerPriceList().replace("<br>", "\n"), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
                qtyOnHandCell = new ExcelData(priceScaleNumberFormat.format(qtyOnHand.setScale(priceScaleNumberFormat.getMaximumFractionDigits(), RoundingMode.HALF_UP)), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
                balanceValueCell = new ExcelData(priceScaleNumberFormat.format(balanceValue.setScale(priceScaleNumberFormat.getMaximumFractionDigits(), RoundingMode.HALF_UP)), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_RIGHT);
                ExcelData[] cellBody = new ExcelData[]{
                        nameCell, entryDateCell, tranDateCell, clientOrSupplierNameCell, numberCell, qtyCell, tranValueCell, costPerQtyCell, qtyOnHandCell, balanceValueCell
                };
                activeList.add(cellBody);
            }
        }

        //draw beginning balance group book mark
        drawBeginningOrEndingBalance(activeList, commonLocalizer.localize(PdfLocalizationName.endingBalance), qtyOnHand, balanceValue);

    }

    private void drawBeginningOrEndingBalance(List<ExcelData[]> activeList, String name, BigDecimal qtyOnHand, BigDecimal balanceValue) {

        ExcelData balanceNameData = new ExcelData(name != null ? name : "", ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT);

        ExcelData emptyData = new ExcelData("", ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL_LEFT);
        ExcelData qtyOnHandData = new ExcelData(priceScaleNumberFormat.format(qtyOnHand.setScale(priceScaleNumberFormat.getMaximumFractionDigits(), BigDecimal.ROUND_HALF_UP)), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
        ExcelData balanceValueData = new ExcelData(priceScaleNumberFormat.format(balanceValue.setScale(priceScaleNumberFormat.getMaximumFractionDigits(), BigDecimal.ROUND_HALF_UP)), ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);

        ExcelData[] endingBalance = new ExcelData[]{
                balanceNameData, emptyData, emptyData, emptyData, emptyData, emptyData, emptyData, emptyData, qtyOnHandData, balanceValueData
        };
        activeList.add(endingBalance);
    }

    private ExcelData[] emptyCellData() {
        ExcelData emptyData = new ExcelData("", ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);

        return new ExcelData[]{
                emptyData, emptyData, emptyData, emptyData, emptyData, emptyData, emptyData, emptyData, emptyData, emptyData
        };
    }

    private ExcelData[] drawBookMark(String name) {
        ExcelData headerData = new ExcelData(name != null ? name : "", ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_LEFT);
        return new ExcelData[]{
                headerData
        };
    }

    private ExcelData[] getBalanceBookMarkCell(String name, BigDecimal balance) {
        BigDecimal balanceAmount = balance.setScale(priceScaleNumberFormat.getMaximumFractionDigits(), BigDecimal.ROUND_HALF_UP);

        ExcelData balanceTypeData = new ExcelData(name, ExcelData.STRING, 30, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
        ExcelData balanceAmountData = new ExcelData(priceScaleNumberFormat.format(balanceAmount), ExcelData.STRING, 20, false, true, ExcelData.NO_BORDER, ExcelData.HEADER_RIGHT);
        ExcelData emptyData = new ExcelData("", ExcelData.STRING, 0, false, true, ExcelData.NO_BORDER, ExcelData.NORMAL);

        return new ExcelData[]{
                balanceTypeData, emptyData, emptyData, emptyData, emptyData, emptyData, emptyData, emptyData, emptyData, balanceAmountData
        };
    }

}
