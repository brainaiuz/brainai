package com.edatasite.workforce.gwt.core.server.servlets.pdf.invoice;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsProperty;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.InventoryStockData;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.InventoryStockValuation;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.InventoryStockValuationItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.AbstractITextPostPdfHandler;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.PdfReferenceCodeNameEnum;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CellData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.CustomisedITextTable;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextGenericPdfData;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.ITextTableList;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data.PdfParams;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.pdf.PdfWriter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 12/24/11
 * Time: 3:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class StockValuationPDFHandler extends AbstractITextPostPdfHandler {

    @Autowired
    private ProductService productService;

    @Override
    public ITextGenericPdfData buildPdfDocument(Object dataClass, Document document, PdfWriter writer) throws IOException {
        return null;
    }

    @Override
    protected ITextGenericPdfData buildPdfDocumentCustomise(Object dataClass, EdsCompany company, boolean hasPhantom) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        SimpleDateFormat format;
        if (company.getCompanySettings() != null && StringUtils.isNotEmpty(company.getCompanySettings().getShortDateFormat())) {
            format = new SimpleDateFormat(company.getCompanySettings().getShortDateFormat(), Locale.ENGLISH);
        } else {
            format = new SimpleDateFormat("MMM d yyyy", Locale.ENGLISH);
        }

        String currencySymbol = fs.getCurrency().getSymbol();
        String currencyCode = fs.getCurrency().getName();
        currencySymbol = currencySymbol != null ? currencySymbol : "";

        fp.setFromExcelPDF(true);

        Date fromDate = parseFilterParameterDate(fp.getStartDateNC());
        Date toDate = parseFilterParameterDate(fp.getEndDateNC());

        ITextGenericPdfData pdfData = new ITextGenericPdfData();
        if (ServerUtils.getUserLocale().getLanguage().equals("uz")) {
            pdfData.setCurrentDate(StringUtils.join(commonLocalizer.localize(PdfLocalizationName.from), " ", ServerUtils.convertToUzbDateFormat(format.format(fromDate)), " ",
                    commonLocalizer.localizeAccounting(PdfLocalizationName.to), " ", ServerUtils.convertToUzbDateFormat(format.format(toDate))));
        } else {
            pdfData.setCurrentDate(StringUtils.join(commonLocalizer.localize(PdfLocalizationName.from), " ", format.format(fromDate), " ",
                    commonLocalizer.localizeAccounting(PdfLocalizationName.to), " ", format.format(toDate)));
        }


        pdfData.setExtraData(StringUtils.join(commonLocalizer.localize(PdfLocalizationName.figuresIn), " ", currencySymbol, "(", currencyCode, ")"));

        ITextTableList table = new ITextTableList(10);
        table.addPdfTableHeader(new CellData(commonLocalizer.localize(PdfLocalizationName.type), Element.ALIGN_LEFT),
                new CellData(commonLocalizer.localize(PdfLocalizationName.entryDate), Element.ALIGN_LEFT),
                new CellData(commonLocalizer.localize(PdfLocalizationName.transactionDate), Element.ALIGN_LEFT),
                new CellData(commonLocalizer.localize(PdfLocalizationName.name), Element.ALIGN_LEFT),
                new CellData(commonLocalizer.localize(PdfLocalizationName.number), Element.ALIGN_RIGHT),
                new CellData(commonLocalizer.localize(PdfLocalizationName.qty), Element.ALIGN_RIGHT),
                new CellData(commonLocalizer.localize(PdfLocalizationName.transactionValue), Element.ALIGN_RIGHT),
                new CellData(commonLocalizer.localize(PdfLocalizationName.costPerQty), Element.ALIGN_RIGHT),
                new CellData(commonLocalizer.localize(PdfLocalizationName.quantityOnHand), Element.ALIGN_RIGHT),
                new CellData(commonLocalizer.localize(PdfLocalizationName.balanceValue), Element.ALIGN_RIGHT));
        pdfData.setListTable(table);

        DecimalFormat priceScaleNumberFormat = getPriceScaleNumberFormat(fs);
        InventoryStockData stockData = productService.getStockValuations(fp, (fromDate != null ? new DateNonConvertable(fromDate) : null), (toDate != null ? new DateNonConvertable(toDate) : null));

        HashMap<String, CustomisedITextTable> customData = new LinkedHashMap<>();
        customData.put("BEGINNING_BALANCE", getBeginningEndingBalance(stockData.getBeginningBalance(), true, priceScaleNumberFormat));
        if (stockData.getStockValuations() != null && stockData.getStockValuations().length > 0) {
            int i = 0;
            for (InventoryStockValuation stockValuation : stockData.getStockValuations()) {
                customData.put("" + i++, createGroup(stockValuation, format, priceScaleNumberFormat));
            }
        }
        customData.put("ENDING_BALANCE", getBeginningEndingBalance(stockData.getEndingBalance(), false, priceScaleNumberFormat));

        pdfData.setCustomData(customData);
        return pdfData;
    }

    private CustomisedITextTable getBeginningEndingBalance(BigDecimal beginningBalance, boolean beginning, DecimalFormat format) {
        CustomisedITextTable table = new CustomisedITextTable();
        table.setName(commonLocalizer.localize(beginning ? PdfLocalizationName.beginningBalance : PdfLocalizationName.endingBalance));
        table.addColumn("AMOUNT", getValueAsString(beginningBalance, format));
        return table;
    }

    private String getValueAsString(BigDecimal value, DecimalFormat priceScaleNumberFormat) {
        if (value.compareTo(BigDecimal.ZERO) >= 0) {
            return priceScaleNumberFormat.format(value);
        } else {
            return "(" + priceScaleNumberFormat.format(value.abs()) + ")";
        }
    }

    private CustomisedITextTable createGroup(InventoryStockValuation stockValuation, SimpleDateFormat dateFormat, DecimalFormat format) {
        CustomisedITextTable table = new CustomisedITextTable();
        table.setName(stockValuation.getProductCode() == null ? stockValuation.getName() : stockValuation.getProductCode() + "->" + stockValuation.getName());
        table.addColumnOrder("NAME", "ENTRY_DATE", "TRANSACTION_DATE", "CLIENT_NAME", "NUMBER", "QTY", "TRANSACTION_VALUE", "COST_PER_QTY", "QTY_ON_HAND", "BALANCE");
        //inventory stock beginning balance and quantity
        BigDecimal balanceValue = stockValuation.getBeginningBalance();
        BigDecimal qtyOnHand = stockValuation.getBeginningQty();

        table.addRow(commonLocalizer.localize(PdfLocalizationName.beginningBalance), "", "", "", "", "", "", "",
                getValueAsString(qtyOnHand, format), getValueAsString(balanceValue, format));

        String nameCell = "", entryDateCell, tranDateCell, clientOrSuplierNameCell, numberCell, qtyCell, tranValueCell, costPerQtyCell, qtyOnHandCell, balanceValueCell;

        InventoryStockValuationItem[] stockValuationItems = stockValuation.getStockValuationItems();
        if (stockValuationItems != null && stockValuationItems.length > 0) {
            for (InventoryStockValuationItem stockValuationItem : stockValuationItems) {
                Boolean isNegative = false;
                if (stockValuationItem.getTransactionType().equals(TT_STOCK_ADJUSTMENT)) {
                    nameCell = TT_STOCK_ADJUSTMENT_STR;
                } else if (stockValuationItem.getTransactionType().equals(TT_OPENING_BALANCE)) {
                    nameCell = TT_OPENING_BALANCE_STR;
                } else if (stockValuationItem.getTransactionType().equals(TT_BUILD_ASSEMBLY)) {
                    nameCell = TT_BUILD_ASSEMBLY_STR;
                } else if (stockValuationItem.getTransactionType().equals(TT_GOODS_RECEIVED)) {
                    nameCell = TT_GOODS_RECEIVED_STR;
                } else if (stockValuationItem.getTransactionType().equals(TT_GOODS_DELIVERED)) {
                    nameCell = TT_GOODS_DELIVERED_STR;
                } else if (stockValuationItem.getTransactionType().equals(TT_PURCHASE)) {
                    nameCell = accountingLocalizer.localizeAccounting("purchase");
                } else if (stockValuationItem.getTransactionType().equals(TT_INVOICE)) {
                    isNegative = true;
                    nameCell = accountingLocalizer.localizeAccounting("invoice");
                } else if (stockValuationItem.getTransactionType().equals(TT_CUSTOMER_CREDIT_NOTE)) {
                    nameCell = TT_CUSTOMER_CREDIT_NOTE_STR;
                } else if (stockValuationItem.getTransactionType().equals(TT_SUPPLIER_CREDIT_NOTE)) {
                    isNegative = true;
                    nameCell = TT_SUPPLIER_CREDIT_NOTE_STR;
                }
                entryDateCell = dateFormat.format(stockValuationItem.getEntryDate().getNonConvertedDate());
                tranDateCell = dateFormat.format(stockValuationItem.getTransactionDate().getNonConvertedDate());
                clientOrSuplierNameCell = stockValuationItem.getName() != null ? stockValuationItem.getName() : "-";
                numberCell = StringUtils.isNotBlank(stockValuationItem.getNumber()) ? stockValuationItem.getNumber() : "";
                if (isNegative || stockValuationItem.getQty().compareTo(BigDecimal.ZERO) < 0) {
                    qtyCell = "(" + getMoneyFormat(stockValuationItem.getQty().multiply(new BigDecimal(-1))) + ")";
                    tranValueCell = "(" + getMoneyFormat(stockValuationItem.getTransactionValue().multiply(new BigDecimal(-1))) + ")";
                } else {
                    qtyCell = "" + getMoneyFormat(stockValuationItem.getQty());
                    tranValueCell = "" + getMoneyFormat(stockValuationItem.getTransactionValue());
                }
//                StringBuilder sb = new StringBuilder();
//                for (StockItem stockItem : stockValuationItem.getQuantityPerPriceList()) {
//                    sb.append(getMoneyFormat(stockItem.getQuantity())).append(" × ").append(getMoneyFormat(stockItem.getPrice())).append("\n");
//                }
                costPerQtyCell = stockValuationItem.getQuantityPerPriceList();

                qtyOnHand = qtyOnHand.add(stockValuationItem.getQty());
                balanceValue = balanceValue.add(stockValuationItem.getTransactionValue());

                if (qtyOnHand.doubleValue() < 0) {
                    qtyOnHandCell = "(" + getMoneyFormat(qtyOnHand.multiply(new BigDecimal(-1))) + ")";
                } else {
                    qtyOnHandCell = "" + getMoneyFormat(qtyOnHand);
                }

                if (balanceValue.doubleValue() < 0) {
                    balanceValueCell = "(" + getMoneyFormat(balanceValue.multiply(new BigDecimal(-1))) + ")";
                } else {
                    balanceValueCell = "" + getMoneyFormat(balanceValue);
                }

                table.addRow(nameCell, entryDateCell, tranDateCell, clientOrSuplierNameCell, numberCell, qtyCell, tranValueCell, costPerQtyCell, qtyOnHandCell, balanceValueCell);
            }
        }

        table.addRow(commonLocalizer.localize(PdfLocalizationName.endingBalance), "", "", "", "", "", "", "",
                getValueAsString(qtyOnHand, format), getValueAsString(balanceValue, format));

        return table;
    }

    @Override
    protected String getTableName(Object dataClass) {
        ListingFilterParameter filterParameter = (ListingFilterParameter) dataClass;
        EdsProperty property = propertManager.findByCode(filterParameter.getPropertyCode());
        return property != null ? property.getPlural() : commonLocalizer.localize(PdfLocalizationName.stockValuation);
    }

    @Override
    protected PdfReferenceCodeNameEnum getPdfCodeName(Object dataClass) {
        return PdfReferenceCodeNameEnum.STOCK_VALUATION;
    }

    @Override
    protected void setFileName(EdsUser user, Object dataClass) {
        setFileName("Stock_Valuation_" + dateFormat(user.getUserDate()));
    }

    protected boolean prepareRequest(HttpServletRequest request) {
        return false;
    }

    @Override
    protected PdfParams.Orientation getOrientation(Object dataClass) {
        ListingFilterParameter fp = (ListingFilterParameter) dataClass;
        return fp.isLandscape() ? PdfParams.Orientation.landscape : null;
    }
}
