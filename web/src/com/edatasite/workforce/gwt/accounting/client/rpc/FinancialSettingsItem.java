package com.edatasite.workforce.gwt.accounting.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Jun 3, 2009
 * Time: 5:02:26 PM
 * To change this template use File | Settings | File Templates.
 */
public class FinancialSettingsItem implements IsSerializable {
    private Integer objectId;

    private Integer currencyId;
    private String currencySymbol;
    private String currencyName;
    private DateNonConvertable finYearEnd;
    private Date conversionDate;
    private DateNonConvertable closeBeforeDate;
    private String taxIdNumber;
    private String taxIdDisplayNumber;
    private Integer taxPeriodId;
    private Integer taxBasisId;
    private String flatRate;
    private Boolean vatRegistered;
    private Boolean showVatNumberInInvoices;
    private DateNonConvertable vatRegisteredOn;
    private DateNonConvertable taxGenerationDate;
    private boolean enableContractOutsite;
//    private Boolean enableMultipleCurrency;
    private Boolean automaticExRateUpdate;
    private Boolean enablePostDatedTransactions;
    private Boolean enableDoubleMessage;
    private Boolean enableMultiSalesPrice;
    private Boolean enableDiscountAccount;
    private Boolean enableLandedCost;
    private Boolean enableMultiWarehouse;
    private Boolean enableDeferredTransaction;
    private Boolean enableAccountingDepartmentRelation;
    private CurrencyItem[] currencies;
    private SelectItem[] taxPeriods;
    private SelectItem[] taxBasises;
    private String agedFilterInterval;
    private String agedPastDueDays;
    private String vatAccountingBasis;
    private Boolean enableVatIossOrMoss;
    private Boolean enableReverseCharge;
    private String tinNumber;
    private String tinDisplayName;  /// these fields tinNumber,tinDisplayName,csrStatus for saudia companies configuer Zatca csr
    private String csrStatus;
    private String otpNumber;

    private boolean isUKCompany;

    private boolean enableIT = true; //enable Inventory Transaction;

    private String localeCode;

    private Boolean vatReturnReportVisibility;

    private Integer productPrice;
    private Integer productQuantity;
    private Integer roundingExchRate;
    private Integer calculationScale;
    private boolean showCustomTaxInListing;
    private String customVatName;

    private Integer incomeTaxPeriodID;
    private BigDecimal incomeTaxRate;
    private SelectItem incomeTaxAccount;
    private boolean hasTransaction;
    private Integer defaultWarehouse;
    private boolean isFromGettingStarted;
//    private boolean enableMultipleCurrencyForBankAccounts;
    private boolean restrictCreatingOrUpdatingInvoices;
    private BigDecimal royality;
    private BigDecimal minRoyality;
    private BigDecimal marketing;
    private BigDecimal minMarketing;

    private Boolean updateCostPriceOnPurhcase;
    private Boolean enableDepreciationDatePeriod;
    private Boolean enableBatchTrackingItems;

    private Boolean submitVatManually;

    private Boolean isAgent;

    private String agentNumber;

    private Boolean mandatoryProjectForExpenseClaims;
    private Boolean mandatoryProjectForPurchaseOrders;

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public Integer getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Integer currencyId) {
        this.currencyId = currencyId;
    }

    public DateNonConvertable getFinYearEnd() {
        return finYearEnd;
    }

    public void setFinYearEnd(DateNonConvertable finYearEnd) {
        this.finYearEnd = finYearEnd;
    }

    public Date getConversionDate() {
        return conversionDate;
    }

    public void setConversionDate(Date conversionDate) {
        this.conversionDate = conversionDate;
    }

    public DateNonConvertable getCloseBeforeDate() {
        return closeBeforeDate;
    }

    public void setCloseBeforeDate(DateNonConvertable closeBeforeDate) {
        this.closeBeforeDate = closeBeforeDate;
    }

    public Integer getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(Integer productPrice) {
        this.productPrice = productPrice;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getTaxIdNumber() {
        return taxIdNumber;
    }

    public void setTaxIdNumber(String taxIdNumber) {
        this.taxIdNumber = taxIdNumber;
    }

    public String getTaxIdDisplayNumber() {
        return taxIdDisplayNumber;
    }

    public void setTaxIdDisplayNumber(String taxIdDisplayNumber) {
        this.taxIdDisplayNumber = taxIdDisplayNumber;
    }

    public Integer getTaxPeriodId() {
        return taxPeriodId;
    }

    public void setTaxPeriodId(Integer taxPeriodId) {
        this.taxPeriodId = taxPeriodId;
    }

    public Integer getTaxBasisId() {
        return taxBasisId;
    }

    public void setTaxBasisId(Integer taxBasisId) {
        this.taxBasisId = taxBasisId;
    }

    public String getFlatRate() {
        return flatRate;
    }

    public void setFlatRate(String flatRate) {
        this.flatRate = flatRate;
    }

    public CurrencyItem[] getCurrencies() {
        return currencies;
    }

    public void setCurrencies(CurrencyItem[] currencies) {
        this.currencies = currencies;
    }

    public SelectItem[] getTaxPeriods() {
        return taxPeriods;
    }

    public void setTaxPeriods(SelectItem[] taxPeriods) {
        this.taxPeriods = taxPeriods;
    }

    public SelectItem[] getTaxBasises() {
        return taxBasises;
    }

    public void setTaxBasises(SelectItem[] taxBasises) {
        this.taxBasises = taxBasises;
    }

    public Boolean getVatRegistered() {
        return vatRegistered;
    }

    public void setVatRegistered(Boolean vatRegistered) {
        this.vatRegistered = vatRegistered;
    }

    public Boolean getShowVatNumberInInvoices() {
        return showVatNumberInInvoices;
    }

    public void setShowVatNumberInInvoices(Boolean showVatNumberInInvoices) {
        this.showVatNumberInInvoices = showVatNumberInInvoices;
    }

    public boolean isUKCompany() {
        return isUKCompany;
    }

    public void setUKCompany(boolean UKCompany) {
        isUKCompany = UKCompany;
    }

    public boolean enableIT() {
        return enableIT;
    }

    public void setEnableIT(boolean enableIT) {
        this.enableIT = enableIT;
    }

    /*public Boolean isEnableMultipleCurrency() {
        return enableMultipleCurrency != null ? enableMultipleCurrency : false;
    }

    public void setEnableMultipleCurrency(Boolean enableMultipleCurrency) {
        this.enableMultipleCurrency = enableMultipleCurrency;
    }*/

    public Boolean isAutomaticExRateUpdate() {
        return automaticExRateUpdate != null ? automaticExRateUpdate : false;
    }

    public void setAutomaticExRateUpdate(Boolean automaticExRateUpdate) {
        this.automaticExRateUpdate = automaticExRateUpdate;
    }

    public Boolean isEnablePostDatedTransactions() {
        return enablePostDatedTransactions != null ? enablePostDatedTransactions : Boolean.FALSE;
    }

    public void setEnablePostDatedTransactions(Boolean enablePostDatedTransactions) {
        this.enablePostDatedTransactions = enablePostDatedTransactions;
    }

    public Boolean getEnableDoubleMessage() {
        return enableDoubleMessage;
    }

    public void setEnableDoubleMessage(Boolean enableDoubleMessage) {
        this.enableDoubleMessage = enableDoubleMessage;
    }

    public Boolean isEnableMultiSalesPrice() {
        return enableMultiSalesPrice;
    }

    public void setEnableMultiSalesPrice(Boolean enableMultiSalesPrice) {
        this.enableMultiSalesPrice = enableMultiSalesPrice;
    }

    public Boolean getEnableDiscountAccount() {
        return enableDiscountAccount;
    }

    public void setEnableDiscountAccount(Boolean enableDiscountAccount) {
        this.enableDiscountAccount = enableDiscountAccount;
    }

    public Boolean getEnableLandedCost() {
        return enableLandedCost != null ? enableLandedCost : Boolean.FALSE;
    }

    public void setEnableLandedCost(Boolean enableLandedCost) {
        this.enableLandedCost = enableLandedCost;
    }

    public Boolean getEnableMultiWarehouse() {
        return enableMultiWarehouse != null ? enableMultiWarehouse : Boolean.FALSE;
    }

    public void setEnableMultiWarehouse(Boolean enableMultiWarehouse) {
        this.enableMultiWarehouse = enableMultiWarehouse;
    }

    public Boolean getEnableDeferredTransaction() {
        return enableDeferredTransaction != null ? enableDeferredTransaction : Boolean.FALSE;
    }

    public void setEnableDeferredTransaction(Boolean enableDeferredTransaction) {
        this.enableDeferredTransaction = enableDeferredTransaction;
    }


    public Boolean getEnableAccountingDepartmentRelation() {
        return enableAccountingDepartmentRelation != null ? enableAccountingDepartmentRelation : Boolean.FALSE;
    }

    public void setEnableAccountingDepartmentRelation(Boolean enableAccountingDepartmentRelation) {
        this.enableAccountingDepartmentRelation = enableAccountingDepartmentRelation;
    }

    public String getLocaleCode() {
        return localeCode;
    }

    public void setLocaleCode(String localeCode) {
        this.localeCode = localeCode;
    }

    public Boolean getVatReturnReportVisibility() {
        return vatReturnReportVisibility;
    }

    public void setVatReturnReportVisibility(Boolean vatReturnReportVisibility) {
        this.vatReturnReportVisibility = vatReturnReportVisibility;
    }

    public Integer getRoundingExchRate() {
        return roundingExchRate;
    }

    public void setRoundingExchRate(Integer roundingExchRate) {
        this.roundingExchRate = roundingExchRate;
    }

    public Integer getCalculationScale() {
        return calculationScale;
    }

    public void setCalculationScale(Integer calculationScale) {
        this.calculationScale = calculationScale;
    }

    public boolean isShowCustomTaxInListing() {
        return showCustomTaxInListing;
    }

    public void setShowCustomTaxInListing(boolean showCustomTaxInListing) {
        this.showCustomTaxInListing = showCustomTaxInListing;
    }

    public String getCustomVatName() {
        return customVatName;
    }

    public void setCustomVatName(String customVatName) {
        this.customVatName = customVatName;
    }

    public Integer getIncomeTaxPeriodID() {
        return incomeTaxPeriodID;
    }

    public void setIncomeTaxPeriodID(Integer incomeTaxPeriodID) {
        this.incomeTaxPeriodID = incomeTaxPeriodID;
    }

    public BigDecimal getIncomeTaxRate() {
        return incomeTaxRate;
    }

    public void setIncomeTaxRate(BigDecimal incomeTaxRate) {
        this.incomeTaxRate = incomeTaxRate;
    }

    public SelectItem getIncomeTaxAccount() {
        return incomeTaxAccount;
    }

    public void setIncomeTaxAccount(SelectItem incomeTaxAccount) {
        this.incomeTaxAccount = incomeTaxAccount;
    }

    public boolean isHasTransaction() {
        return hasTransaction;
    }

    public void setHasTransaction(boolean hasTransaction) {
        this.hasTransaction = hasTransaction;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public void setDefaultWarehouse(Integer defaultWarehouse) {
        this.defaultWarehouse = defaultWarehouse;
    }

    public Integer getDefaultWarehouse() {
        return defaultWarehouse;
    }

    public boolean isFromGettingStarted() {
        return isFromGettingStarted;
    }

    public void setIsFromGettingStarted(boolean isFromGettingStarted) {
        this.isFromGettingStarted = isFromGettingStarted;
    }

    /*public boolean isEnableMultipleCurrencyForBankAccounts() {
        return enableMultipleCurrencyForBankAccounts;
    }

    public void setEnableMultipleCurrencyForBankAccounts(boolean enableMultipleCurrencyForBankAccounts) {
        this.enableMultipleCurrencyForBankAccounts = enableMultipleCurrencyForBankAccounts;
    }*/

    public boolean isRestrictCreatingOrUpdatingInvoices() {
        return restrictCreatingOrUpdatingInvoices;
    }

    public void setRestrictCreatingOrUpdatingInvoices(boolean restrictCreatingOrUpdatingInvoices) {
        this.restrictCreatingOrUpdatingInvoices = restrictCreatingOrUpdatingInvoices;
    }

    public BigDecimal getRoyality() {
        return royality;
    }

    public void setRoyality(BigDecimal royality) {
        this.royality = royality;
    }

    public BigDecimal getMinRoyality() {
        return minRoyality;
    }

    public void setMinRoyality(BigDecimal minRoyality) {
        this.minRoyality = minRoyality;
    }

    public BigDecimal getMarketing() {
        return marketing;
    }

    public void setMarketing(BigDecimal marketing) {
        this.marketing = marketing;
    }

    public BigDecimal getMinMarketing() {
        return minMarketing;
    }

    public void setMinMarketing(BigDecimal minMarketing) {
        this.minMarketing = minMarketing;
    }

    public Boolean getUpdateCostPriceOnPurhcase() {
        return updateCostPriceOnPurhcase != null && updateCostPriceOnPurhcase;
    }

    public void setUpdateCostPriceOnPurhcase(Boolean updateCostPriceOnPurhcase) {
        this.updateCostPriceOnPurhcase = updateCostPriceOnPurhcase;
    }

    public Boolean getEnableDepreciationDatePeriod() {
        return enableDepreciationDatePeriod != null ? enableDepreciationDatePeriod : false;
    }

    public void setEnableDepreciationDatePeriod(Boolean enableDepreciationDatePeriod) {
        this.enableDepreciationDatePeriod = enableDepreciationDatePeriod;
    }

    public Boolean getEnableBatchTrackingItems() {
        return enableBatchTrackingItems != null ? enableBatchTrackingItems : false;
    }

    public void setEnableBatchTrackingItems(Boolean enableBatchTrackingItems) {
        this.enableBatchTrackingItems = enableBatchTrackingItems;
    }

    public boolean isEnableContractOutsite() {
        return enableContractOutsite;
    }

    public void setEnableContractOutsite(boolean enableContractOutsite) {
        this.enableContractOutsite = enableContractOutsite;
    }

    public DateNonConvertable getTaxGenerationDate() {
        return taxGenerationDate;
    }

    public void setTaxGenerationDate(DateNonConvertable taxGenerationDate) {
        this.taxGenerationDate = taxGenerationDate;
    }

    public DateNonConvertable getVatRegisteredOn() {
        return vatRegisteredOn;
    }

    public void setVatRegisteredOn(DateNonConvertable vatRegisteredOn) {
        this.vatRegisteredOn = vatRegisteredOn;
    }

    public String getAgedFilterInterval() {
        return agedFilterInterval;
    }

    public void setAgedFilterInterval(String agedFilterInterval) {
        this.agedFilterInterval = agedFilterInterval;
    }

    public String getAgedPastDueDays() {
        return agedPastDueDays;
    }

    public void setAgedPastDueDays(String agedPastDueDays) {
        this.agedPastDueDays = agedPastDueDays;
    }

    public String getVatAccountingBasis() {
        return vatAccountingBasis;
    }

    public void setVatAccountingBasis(String vatAccountingBasis) {
        this.vatAccountingBasis = vatAccountingBasis;
    }

    public Boolean getEnableVatIossOrMoss() {
        return enableVatIossOrMoss;
    }

    public void setEnableVatIossOrMoss(Boolean enableVatIossOrMoss) {
        this.enableVatIossOrMoss = enableVatIossOrMoss;
    }

    public Boolean getEnableReverseCharge() {
        return enableReverseCharge;
    }

    public void setEnableReverseCharge(Boolean enableReverseCharge) {
        this.enableReverseCharge = enableReverseCharge;
    }

    public Boolean getSubmitVatManually() {
        return submitVatManually;
    }

    public void setSubmitVatManually(Boolean submitVatManually) {
        this.submitVatManually = submitVatManually;
    }

    public Boolean getAgent() {
        return isAgent;
    }

    public void setAgent(Boolean agent) {
        isAgent = agent;
    }

    public String getAgentNumber() {
        return agentNumber;
    }

    public void setAgentNumber(String agentNumber) {
        this.agentNumber = agentNumber;
    }

    public String getTinNumber() {
        return tinNumber;
    }

    public void setTinNumber(String tinNumber) {
        this.tinNumber = tinNumber;
    }

    public String getTinDisplayName() {
        return tinDisplayName;
    }

    public void setTinDisplayName(String tinDisplayName) {
        this.tinDisplayName = tinDisplayName;
    }

    public String getCsrStatus() {
        return csrStatus;
    }

    public void setCsrStatus(String csrStatus) {
        this.csrStatus = csrStatus;
    }

    public String getOtpNumber() {
        return otpNumber;
    }

    public void setOtpNumber(String otpNumber) {
        this.otpNumber = otpNumber;
    }

    public Boolean getMandatoryProjectForExpenseClaims() {
        return mandatoryProjectForExpenseClaims != null ? mandatoryProjectForExpenseClaims : false;
    }

    public void setMandatoryProjectForExpenseClaims(Boolean mandatoryProjectForExpenseClaims) {
        this.mandatoryProjectForExpenseClaims = mandatoryProjectForExpenseClaims;
    }

    public Boolean getMandatoryProjectForPurchaseOrders() {
        return mandatoryProjectForPurchaseOrders != null ? mandatoryProjectForPurchaseOrders : false;
    }

    public void setMandatoryProjectForPurchaseOrders(Boolean mandatoryProjectForPurchaseOrders) {
        this.mandatoryProjectForPurchaseOrders = mandatoryProjectForPurchaseOrders;
    }
}
