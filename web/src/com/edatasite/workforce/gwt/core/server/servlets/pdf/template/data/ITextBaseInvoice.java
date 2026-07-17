package com.edatasite.workforce.gwt.core.server.servlets.pdf.template.data;

import com.edatasite.workforce.gwt.core.client.rpc.CustomTableRpc;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_en;
import com.edatasite.workforce.gwt.core.server.utils.NumberToWord_ru;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 24-Jun-2010
 * Time: 15:47:21
 * <p/>
 * Account Invoice,Quote ... template repository class
 */
public class ITextBaseInvoice {

    private Integer objectId;
    private String currency;
    private String taxCalcType;
    private String exchangeRate;
    private String currencyName;
    private String clientCode;
    private Integer totalDay;
    private List<String> googleData;
    private List<String> paypallData;
    private Map<String,String> stripeData;
    private List<String> masterCardData;
    private List<String> elavonData;
    private Map<String, String> payMeData;
    private Map<String, ArrayList<CustomTableRpc>> tableItems;
    private final NumberToWord numberToWordConverter = new NumberToWord_en();
    private String clientApproveData;
    private String numberAndDatesTableName;// Number and Dates table name
    private String productTableName;// Product Table Name
    private String expenseTableName;// Product Table Name
    private String invoiceTotalTableName;// Invoice Total Table Name
    private String termsConditionsName;// Terms and Conditions Name
    private ITextTableList introduction;// Sales Quote Introduction
    private ITextTableList poDataTable;
    private ITextTableList termsConditions; // Terms Conditions Table
    private ITextTableList bank; // Bank table
    private ITextTableList account; // Account table
    private ITextTableList footerData;
    private List<String> notes;
    private String packingSlipTitle;
    private Boolean isPackingSlip;
    private Boolean isLetter;
    private ITextTableList productTable;// table product items
    private ITextTableList expenseTable;// table product items
    private ITextTableList numberAndDatesTable;// invoice,quote,.. numbers date
    private Map<String, String> clientSupplierData;// Client/Supplier Name, Contact and address data
    private Map<String, String> purchaseClientData;//Purchase Invoice/Order Client Name and address data
    private ITextTableList invoiceTotalTable;// totals
    private CustomisedITextTable customProductTable;// table product items
    private List<CustomisedITextTable> customProductTableList;// table product items
    private CustomisedITextTable customExpenseTable;// table product items
    private CustomisedITextTable customNumberAndDatesTable;// invoice,quote,.. numbers date
    private CustomisedITextTable customBillToAddress;// Bill  address
    private CustomisedITextTable customPrimaryContactAddress;// Primary contact address
    private CustomisedITextTable customProductAssemblyItemsTable;
    private CustomisedITextTable customProductKitItemsTable;
    private CustomisedITextTable customTotalTable;// totals
    private CustomisedITextTable customBankTable;// Bank Data
    private CustomisedITextTable customAccountTable;// Account Data
    private CustomisedITextTable consignTable;
    private CustomisedITextTable customFooterData;
    private CustomisedITextTable customTermsConditions;
    private CustomisedITextTable customIntroduction;
    private CustomisedITextTable customPOTable;
    private CustomisedITextTable customClientSupplierEntityCustomFieldTable;//Entity Drop Down Custom Field
    private CustomisedITextTable customEmployeeEntityCustomFieldTable;//Entity Drop Down Custom Field
    private CustomisedITextTable customClientOrSupplierTypeTable;//Customer or Supplier types
    private CustomisedITextTable customGroupTaxRateTable;
    private CustomisedITextTable customDueAmountTable;
    private CustomisedITextTable customPrepaymentTable;
    private CustomisedITextTable customEmployeeTable;
    private List<CustomisedProductCategoriesITextTable> customProductCategoriesITextTables;
    private List<CustomisedProductCategoriesITextTable> customItemCustomFieldITextTables;
    private CustomisedITextTable customProductSerialTable;
    private CustomisedITextTable customProductArticleTable;
    private CustomisedITextTable customLandedCostTable;
    private CustomisedITextTable customOutstandingInvoiceTable;
    private CustomisedITextTable customGroupItemNameAndUnitPriceTable;
    private CustomisedITextTable customGroupItemNameAndTaxRateTable;
    private CustomisedITextTable customGroupItemNameTable;
    private CustomisedITextTable customGroupItemNameForProductTableData;
    private CustomisedITextTable customDetailedItemsFromOrderBaseInvoice;
    private CustomisedITextTable customTotalForDetailedItemsFromOrderBaseInvoice;
    private CustomisedITextTable paymentHistoryTable;
    private CustomisedITextTable customMultiQuoteAndGdnConverToInvoiceProductTableData;// table product items
    private CustomisedITextTable customInvoicedItemTable;
    private String fontName;
    private Map<String, String> clickData;
    private Map<String, String> revolutData;
    private CustomisedITextTable customApproverTable;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getTaxCalcType() {
        return taxCalcType;
    }

    public void setTaxCalcType(String taxCalcType) {
        this.taxCalcType = taxCalcType;
    }

    public List<String> getGoogleData() {
        return googleData;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getClientCode() {
        return clientCode;
    }

    public void setClientCode(String clientCode) {
        this.clientCode = clientCode;
    }

    public Integer getTotalDay() {
        return totalDay;
    }

    public void setTotalDay(Integer totalDay) {
        this.totalDay = totalDay;
    }

    public void setGoogleData(List<String> googleData) {
        this.googleData = googleData;
    }

    public List<String> getPaypallData() {
        return paypallData;
    }

    public void setPaypallData(List<String> paypallData) {
        this.paypallData = paypallData;
    }

    public Map<String,String> getStripeData() {
        return stripeData;
    }

    public void setStripeData(Map<String,String> stripeData) {
        this.stripeData = stripeData;
    }

    public List<String> getMasterCardData() {
        return masterCardData;
    }

    public void setMasterCardData(List<String> masterCardData) {
        this.masterCardData = masterCardData;
    }

    public List<String> getElavonData() {
        return elavonData;
    }

    public void setElavonData(List<String> elavonData) {
        this.elavonData = elavonData;
    }

    public String getClientApproveData() {
        return clientApproveData;
    }

    public void setClientApproveData(String clientApproveData) {
        this.clientApproveData = clientApproveData;
    }

    public String getNumberAndDatesTableName() {
        return numberAndDatesTableName;
    }

    public void setNumberAndDatesTableName(String numberAndDatesTableName) {
        this.numberAndDatesTableName = numberAndDatesTableName;
    }

    public String getProductTableName() {
        return productTableName;
    }

    public void setProductTableName(String productTableName) {
        this.productTableName = productTableName;
    }

    public String getExpenseTableName() {
        return expenseTableName;
    }

    public void setExpenseTableName(String expenseTableName) {
        this.expenseTableName = expenseTableName;
    }

    public String getInvoiceTotalTableName() {
        return invoiceTotalTableName;
    }

    public void setInvoiceTotalTableName(String invoiceTotalTableName) {
        this.invoiceTotalTableName = invoiceTotalTableName;
    }

    public String getTermsConditionsName() {
        return termsConditionsName;
    }

    public void setTermsConditionsName(String termsConditionsName) {
        this.termsConditionsName = termsConditionsName;
    }

    public Map<String, String> getClientSupplierData() {
        return clientSupplierData;
    }

    public void setClientSupplierData(Map<String, String> clientSupplierData) {
        this.clientSupplierData = clientSupplierData;
    }

    public Map<String, String> getPurchaseClientData() {
        return purchaseClientData;
    }

    public void setPurchaseClientData(Map<String, String> purchaseClientData) {
        this.purchaseClientData = purchaseClientData;
    }

    public ITextTableList getNumberAndDatesTable() {
        return numberAndDatesTable;
    }

    public void setNumberAndDatesTable(ITextTableList numberAndDatesTable) {
        this.numberAndDatesTable = numberAndDatesTable;
    }

    public ITextTableList getPoDataTable() {
        return poDataTable;
    }

    public void setPoDataTable(ITextTableList poDataTable) {
        this.poDataTable = poDataTable;
    }

    public ITextTableList getProductTable() {
        return productTable;
    }

    public void setProductTable(ITextTableList productTable) {
        this.productTable = productTable;
    }

    public ITextTableList getExpenseTable() {
        return expenseTable;
    }

    public void setExpenseTable(ITextTableList expenseTable) {
        this.expenseTable = expenseTable;
    }

    public ITextTableList getInvoiceTotalTable() {
        return invoiceTotalTable;
    }

    public void setInvoiceTotalTable(ITextTableList invoiceTotalTable) {
        this.invoiceTotalTable = invoiceTotalTable;
    }

    public ITextTableList getTermsConditions() {
        return termsConditions;
    }

    public void setTermsConditions(ITextTableList termsConditions) {
        this.termsConditions = termsConditions;
    }

    public ITextTableList getBank() {
        return bank;
    }

    public void setBank(ITextTableList bank) {
        this.bank = bank;
    }

    public ITextTableList getAccount() {
        return account;
    }

    public void setAccount(ITextTableList account) {
        this.account = account;
    }

    public ITextTableList getFooterData() {
        return footerData;
    }

    public void setFooterData(ITextTableList footerData) {
        this.footerData = footerData;
    }

    public ITextTableList getIntroduction() {
        return introduction;
    }

    public void setIntroduction(ITextTableList introduction) {
        this.introduction = introduction;
    }

    public List<String> getNotes() {
        return notes;
    }

    public void setNotes(List<String> notes) {
        this.notes = notes;
    }

    public String getPackingSlipTitle() {
        return packingSlipTitle;
    }

    public void setPackingSlipTitle(String packingSlipTitle) {
        this.packingSlipTitle = packingSlipTitle;
    }

    public Boolean isPackingSlip() {
        return isPackingSlip != null ? isPackingSlip : false;
    }

    public void setPackingSlip(Boolean packingSlip) {
        isPackingSlip = packingSlip;
    }

    public Boolean isLetter() {
        return isLetter != null ? isLetter : false;
    }

    public void setLetter(Boolean letter) {
        isLetter = letter;
    }

    public CustomisedITextTable getCustomProductTable() {
        return customProductTable;
    }

    public void setCustomProductTable(CustomisedITextTable customProductTable) {
        this.customProductTable = customProductTable;
    }

    public CustomisedITextTable getCustomExpenseTable() {
        return customExpenseTable;
    }

    public void setCustomExpenseTable(CustomisedITextTable customExpenseTable) {
        this.customExpenseTable = customExpenseTable;
    }

    public CustomisedITextTable getCustomBillToAddress() {
        return customBillToAddress;
    }

    public void setCustomBillToAddress(CustomisedITextTable customBillToAddress) {
        this.customBillToAddress = customBillToAddress;
    }

    public CustomisedITextTable getCustomPrimaryContactAddress() {
        return customPrimaryContactAddress;
    }

    public void setCustomPrimaryContactAddress(CustomisedITextTable customPrimaryContactAddress) {
        this.customPrimaryContactAddress = customPrimaryContactAddress;
    }

    public CustomisedITextTable getCustomProductAssemblyItemsTable() {
        return customProductAssemblyItemsTable;
    }

    public void setCustomProductAssemblyItemsTable(CustomisedITextTable customProductAssemblyItemsTable) {
        this.customProductAssemblyItemsTable = customProductAssemblyItemsTable;
    }

    public CustomisedITextTable getCustomProductKitItemsTable() {
        return customProductKitItemsTable;
    }

    public void setCustomProductKitItemsTable(CustomisedITextTable customProductKitItemsTable) {
        this.customProductKitItemsTable = customProductKitItemsTable;
    }

    public CustomisedITextTable getCustomNumberAndDatesTable() {
        return customNumberAndDatesTable;
    }

    public void setCustomNumberAndDatesTable(CustomisedITextTable customNumberAndDatesTable) {
        this.customNumberAndDatesTable = customNumberAndDatesTable;
    }

    public CustomisedITextTable getCustomTotalTable() {
        return customTotalTable;
    }

    public void setCustomTotalTable(CustomisedITextTable customTotalTable) {
        this.customTotalTable = customTotalTable;
    }

    public CustomisedITextTable getCustomBankTable() {
        return customBankTable;
    }

    public void setCustomBankTable(CustomisedITextTable customBankTable) {
        this.customBankTable = customBankTable;
    }

    public CustomisedITextTable getCustomAccountTable() {
        return customAccountTable;
    }

    public void setCustomAccountTable(CustomisedITextTable customAccountTable) {
        this.customAccountTable = customAccountTable;
    }

    public CustomisedITextTable getConsignTable() {
        return consignTable;
    }

    public void setConsignTable(CustomisedITextTable consignTable) {
        this.consignTable = consignTable;
    }

    public CustomisedITextTable getCustomFooterData() {
        return customFooterData;
    }

    public void setCustomFooterData(CustomisedITextTable customFooterData) {
        this.customFooterData = customFooterData;
    }

    public CustomisedITextTable getCustomTermsConditions() {
        return customTermsConditions;
    }

    public void setCustomTermsConditions(CustomisedITextTable customTermsConditions) {
        this.customTermsConditions = customTermsConditions;
    }

    public CustomisedITextTable getCustomIntroduction() {
        return customIntroduction;
    }

    public void setCustomIntroduction(CustomisedITextTable customIntroduction) {
        this.customIntroduction = customIntroduction;
    }

    public CustomisedITextTable getCustomPOTable() {
        return customPOTable;
    }

    public void setCustomPOTable(CustomisedITextTable customPOTable) {
        this.customPOTable = customPOTable;
    }

    public CustomisedITextTable getPaymentHistoryTable() {
        return paymentHistoryTable;
    }

    public void setPaymentHistoryTable(CustomisedITextTable paymentHistoryTable) {
        this.paymentHistoryTable = paymentHistoryTable;
    }

    public String getFontName() {
        return fontName;
    }

    public String getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(String exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public CustomisedITextTable getCustomClientSupplierEntityCustomFieldTable() {
        return customClientSupplierEntityCustomFieldTable;
    }

    public void setCustomClientSupplierEntityCustomFieldTable(CustomisedITextTable customClientSupplierEntityCustomFieldTable) {
        this.customClientSupplierEntityCustomFieldTable = customClientSupplierEntityCustomFieldTable;
    }

    public CustomisedITextTable getCustomEmployeeEntityCustomFieldTable() {
        return customEmployeeEntityCustomFieldTable;
    }

    public void setCustomEmployeeEntityCustomFieldTable(CustomisedITextTable customEmployeeEntityCustomFieldTable) {
        this.customEmployeeEntityCustomFieldTable = customEmployeeEntityCustomFieldTable;
    }

    public CustomisedITextTable getCustomClientOrSupplierTypeTable() {
        return customClientOrSupplierTypeTable;
    }

    public void setCustomClientOrSupplierTypeTable(CustomisedITextTable customClientOrSupplierTypeTable) {
        this.customClientOrSupplierTypeTable = customClientOrSupplierTypeTable;
    }

    public CustomisedITextTable getCustomGroupTaxRateTable() {
        return customGroupTaxRateTable;
    }

    public void setCustomGroupTaxRateTable(CustomisedITextTable customGroupTaxRateTable) {
        this.customGroupTaxRateTable = customGroupTaxRateTable;
    }

    public List<CustomisedProductCategoriesITextTable> getCustomProductCategoriesITextTables() {
        return customProductCategoriesITextTables;
    }

    public void setCustomProductCategoriesITextTables(List<CustomisedProductCategoriesITextTable> customProductCategoriesITextTables) {
        this.customProductCategoriesITextTables = customProductCategoriesITextTables;
    }

    public List<CustomisedProductCategoriesITextTable> getCustomItemCustomFieldITextTables() {
        return customItemCustomFieldITextTables;
    }

    public void setCustomItemCustomFieldITextTables(List<CustomisedProductCategoriesITextTable> customItemCustomFieldITextTables) {
        this.customItemCustomFieldITextTables = customItemCustomFieldITextTables;
    }

    public CustomisedITextTable getCustomDueAmountTable() {
        return customDueAmountTable;
    }

    public void setCustomDueAmountTable(CustomisedITextTable customDueAmountTable) {
        this.customDueAmountTable = customDueAmountTable;
    }

    public CustomisedITextTable getCustomPrepaymentTable() {
        return customPrepaymentTable;
    }

    public void setCustomPrepaymentTable(CustomisedITextTable customPrepaymentTable) {
        this.customPrepaymentTable = customPrepaymentTable;
    }

    public CustomisedITextTable getCustomEmployeeTable() {
        return customEmployeeTable;
    }

    public void setCustomEmployeeTable(CustomisedITextTable customEmployeeTable) {
        this.customEmployeeTable = customEmployeeTable;
    }

    public CustomisedITextTable getCustomProductSerialTable() {
        return customProductSerialTable;
    }

    public void setCustomProductSerialTable(CustomisedITextTable customProductSerialTable) {
        this.customProductSerialTable = customProductSerialTable;
    }

    public CustomisedITextTable getCustomProductArticleTable() {
        return customProductArticleTable;
    }

    public void setCustomProductArticleTable(CustomisedITextTable customProductArticleTable) {
        this.customProductArticleTable = customProductArticleTable;
    }

    public CustomisedITextTable getCustomLandedCostTable() {
        return customLandedCostTable;
    }

    public void setCustomLandedCostTable(CustomisedITextTable customLandedCostTable) {
        this.customLandedCostTable = customLandedCostTable;
    }

    public CustomisedITextTable getCustomOutstandingInvoiceTable() {
        return customOutstandingInvoiceTable;
    }

    public void setCustomOutstandingInvoiceTable(CustomisedITextTable customOutstandingInvoiceTable) {
        this.customOutstandingInvoiceTable = customOutstandingInvoiceTable;
    }

    public CustomisedITextTable getCustomGroupItemNameAndUnitPriceTable() {
        return customGroupItemNameAndUnitPriceTable;
    }

    public void setCustomGroupItemNameAndUnitPriceTable(CustomisedITextTable customGroupItemNameAndUnitPriceTable) {
        this.customGroupItemNameAndUnitPriceTable = customGroupItemNameAndUnitPriceTable;
    }

    public CustomisedITextTable getCustomGroupItemNameAndTaxRateTable() {
        return customGroupItemNameAndTaxRateTable;
    }

    public void setCustomGroupItemNameAndTaxRateTable(CustomisedITextTable customGroupItemNameAndTaxRateTable) {
        this.customGroupItemNameAndTaxRateTable = customGroupItemNameAndTaxRateTable;
    }

    public CustomisedITextTable getCustomGroupItemNameTable() {
        return customGroupItemNameTable;
    }

    public void setCustomGroupItemNameTable(CustomisedITextTable customGroupItemNameTable) {
        this.customGroupItemNameTable = customGroupItemNameTable;
    }

    public CustomisedITextTable getCustomGroupItemNameForProductTableData() {
        return customGroupItemNameForProductTableData;
    }

    public void setCustomGroupItemNameForProductTableData(CustomisedITextTable customGroupItemNameForProductTableData) {
        this.customGroupItemNameForProductTableData = customGroupItemNameForProductTableData;
    }

    public CustomisedITextTable getCustomDetailedItemsFromOrderBaseInvoice() {
        return customDetailedItemsFromOrderBaseInvoice;
    }

    public void setCustomDetailedItemsFromOrderBaseInvoice(CustomisedITextTable customDetailedItemsFromOrderBaseInvoice) {
        this.customDetailedItemsFromOrderBaseInvoice = customDetailedItemsFromOrderBaseInvoice;
    }

    public CustomisedITextTable getCustomTotalForDetailedItemsFromOrderBaseInvoice() {
        return customTotalForDetailedItemsFromOrderBaseInvoice;
    }

    public void setCustomTotalForDetailedItemsFromOrderBaseInvoice(CustomisedITextTable customTotalForDetailedItemsFromOrderBaseInvoice) {
        this.customTotalForDetailedItemsFromOrderBaseInvoice = customTotalForDetailedItemsFromOrderBaseInvoice;
    }

    public CustomisedITextTable getCustomMultiQuoteAndGdnConverToInvoiceProductTableData() {
        return customMultiQuoteAndGdnConverToInvoiceProductTableData;
    }

    public void setCustomMultiQuoteAndGdnConverToInvoiceProductTableData(CustomisedITextTable customMultiQuoteAndGdnConverToInvoiceProductTableData) {
        this.customMultiQuoteAndGdnConverToInvoiceProductTableData = customMultiQuoteAndGdnConverToInvoiceProductTableData;
    }

    public CustomisedITextTable getCustomInvoicedItemTable() {
        return customInvoicedItemTable;
    }

    public void setCustomInvoicedItemTable(CustomisedITextTable customInvoicedItemTable) {
        this.customInvoicedItemTable = customInvoicedItemTable;
    }

    public List<CustomisedITextTable> getCustomProductTableList() {
        return customProductTableList;
    }

    public void setCustomProductTableList(List<CustomisedITextTable> customProductTableList) {
        this.customProductTableList = customProductTableList;
    }

    public Map<String, String> getPayMeData() {
        return payMeData;
    }

    public Map<String, ArrayList<CustomTableRpc>> getTableItems() {
        return tableItems;
    }

    public Map<String, ArrayList<CustomTableRpc>> setTableItems(Map<String, ArrayList<CustomTableRpc>> tableItems) {
        return this.tableItems = tableItems;
    }
    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public void setPayMeData(Map<String, String> payMeData) {
        this.payMeData = payMeData;
    }

    public Map<String, String> getClickData() {
        return clickData;
    }

    public void setClickData(Map<String, String> clickData) {
        this.clickData = clickData;
    }

    public CustomisedITextTable getCustomApproverTable() {
        return customApproverTable;
    }

    public void setCustomApproverTable(CustomisedITextTable customApproverTable) {
        this.customApproverTable = customApproverTable;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
        if (introduction != null) {
            introduction.setFontName(fontName);
        }
        if (poDataTable != null) {
            poDataTable.setFontName(fontName);
        }
        if (termsConditions != null) {
            termsConditions.setFontName(fontName);
        }
        if (bank != null) {
            bank.setFontName(fontName);
        }
        if (account != null) {
            account.setFontName(fontName);
        }
        if (footerData != null) {
            footerData.setFontName(fontName);
        }
        if (productTable != null) {
            productTable.setFontName(fontName);
        }
        if (expenseTable != null) {
            expenseTable.setFontName(fontName);
        }
        if (numberAndDatesTable != null) {
            numberAndDatesTable.setFontName(fontName);
        }
        if (invoiceTotalTable != null) {
            invoiceTotalTable.setFontName(fontName);
        }
    }

    public Float getValueAsFloat(String in) {
        float f = 0;
        try {
            f = Float.parseFloat(in.replaceAll("[,]", ""));
        } catch (Exception e) {
            return 0.0f;
        }
        return f;
    }

    public String getAsFormatted(Object obj) {
        String pattern = ",##0.00";
        DecimalFormat decimalFormat = new DecimalFormat(pattern);
        return decimalFormat.format(obj);
    }

    public String getNumberInWordsAll(Object obj) {
        String word = "";
        if (obj != null && !obj.equals("") && !obj.equals("N/A")) {
            word = numberToWordConverter.convert(new BigDecimal(String.valueOf(obj)));
        }
        return !"".equals(word) ? WordUtils.capitalizeFully(word) : "";
    }

    public String getNumberInWords(Object obj) {
        String word = "";
        if (obj != null && !obj.equals("") && !obj.equals("N/A")) {
            word = numberToWordConverter.toWord(new BigDecimal(String.valueOf(obj)));
        }
        return !"".equals(word) ? WordUtils.capitalizeFully(word) : "";
    }

    public String getNumberInWordsAllRu(Object object) {
        if (object == null || object.equals("") || object.equals("N/A") || object.equals(0.0)) {
            return "";
        }
        NumberToWord numberToWordConverterRu = new NumberToWord_ru();
        String objToString = object.toString().replace(",", "");
        String numberToWord = numberToWordConverterRu.convert(new BigDecimal(objToString));

        return !StringUtils.isEmpty(numberToWord) ? WordUtils.capitalizeFully(numberToWord) : "";
    }

    public Map<String, String> getRevolutData() {
        return revolutData;
    }

    public void setRevolutData(Map<String, String> revolutData) {
        this.revolutData = revolutData;
    }
}
