package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Jun 3, 2009
 * Time: 1:52:36 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "financialsettings")
public class EdsFinancialSettings extends EdsObject {

    public static final String TAX_PERIOD = "TAX_PERIOD";
    public static final String UAE_TAX_PERIOD = "_UAE_TAX_PERIOD";
    public static final String TAX_BASIS = "TAX_BASIS";
    public static final int SYSTEM_CALCULATION_SCALE = 5;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    @ForeignKey(name = "none")
    private EdsCurrency currency;

    @Column(name = "financialyearend")
    private Date financialYearEnd;

    @Column(name = "productprice")
    private Integer productPriceScale;

    @Column(name = "productquantity")
    private Integer productQuantity;

    @Column(name = "rounding_exch_rate")
    private Integer roundingExchRate;

    private Integer calculationScale;

    private Integer taxRateScale;

    private Integer discountScale;

    private Integer progressInvoicingAmountScale;

    @Column(name = "vat_registered", columnDefinition = "boolean default false")
    private boolean vatRegistered;

    private String taxIdNumber;

    private String taxIdDisplayNumber;

    private String tinNumber;

    private String tinDisplayName;

    @Column(name = "vat_registered_on")
    private Date vatRegisteredOn;

    @Column(name = "tax_generation_date")
    private Date taxGenerationDate;

    @Column(name = "enable_contract_outsite", columnDefinition = "boolean default false")
    private boolean enableContractOutsite;

    private String customVatName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taxPeriod")
    @ForeignKey(name = "none")
    private EdsReference taxPeriod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taxBasis")
    @ForeignKey(name = "none")
    private EdsReference taxBasis;

    @Column(name = "conversionDate")
    private Date conversionDate;

    @Column(name = "blockBeforeDate")
    private Date blockBeforeDate;

    private Boolean defaultTaxesCreated;

    private String flatRateReturn;

    private Boolean showVatNumberInInvoices;

    private Boolean showVatNumberInListings;

    private Boolean showVatReturnReport;

    private Boolean enablePostedDateTransaction;

    private Boolean updateCostPriceOnPurhcase;

    @Column(name = "enable_inventory_transaction")
    private Boolean enableIT = true; //enable Inventory Transaction

    @Column(name = "automaticexrateupdate")
    private Boolean automaticExRateUpdate = true;

    @Deprecated
    @Column(name = "enable_multiple_currency")
    private Boolean enableMultipleCurrency;

    @Column(name = "enable_multiple_sales_price")
    private Boolean enableMultipleSalesPrice;

    @Column(name = "enable_discount_account")
    private Boolean enableDiscountAccount;

    @Column(name = "enable_landed_cost")
    private Boolean enableLandedCost;

    @Column(name = "enable_multi_warehouse")
    private Boolean enableMultiWarehouse;

    @Column(name = "enable_accounting_department_relation")
    private Boolean enableAccountingDepartmentRelation;

    private Boolean enableDoubleMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "incomeTaxPeriod")
    @ForeignKey(name = "none")
    private EdsReference incomeTaxPeriod;

    private BigDecimal incomeTaxRate;
    private Integer incomeTaxAccount;

    @Column(precision = 25, scale = 5)
    private BigDecimal royality;
    @Column(precision = 25, scale = 5)
    private BigDecimal minRoyality;
    @Column(precision = 25, scale = 5)
    private BigDecimal marketing;
    @Column(precision = 25, scale = 5)
    private BigDecimal minMarketing;

    @Column(name = "reports_old_version", columnDefinition = "boolean default false")
    private boolean reportsOldVerion;


    @Column(name = "includeCurrentYearEariningInRetained", columnDefinition = "boolean default false")
    private boolean includeCurrentYearEariningInRetained;

    @Column(name = "generateNewNumber", columnDefinition = "boolean default false")
    private boolean generateNewNumber;

    @Column(name = "balancesheetSettingsJSONData")
    @Type(type = "text")
    private String balancesheetSettingsJSONData;

    @Column(name = "enableDepreciationDatePeriod")
    private Boolean enableDepreciationDatePeriod = false;

    @Column(name = "enableBatchTrackingItems")
    private Boolean enableBatchTrackingItems = false;

    @Column(name = "restrictCreatingOrUpdatingInvoices")
    private Boolean restrictCreatingOrUpdatingInvoices = false;

    @Column(name = "costOfGoodsCalculationType")
    private String costOfGoodsCalculationType;

    @Column(name = "agedFilterInterval")
    private String agedFilterInterval;

    @Column(name = "agedPastDueDays")
    private String agedPastDueDays;

    @Column(name = "enableDeferredTransaction", columnDefinition = "boolean default false")
    private boolean enableDeferredTransaction;
    @Column
    private String otpNumber;   /// it is need to be integer type of value

    @Column(name = "vatAccountingBasis")
    private String vatAccountingBasis;

    @Column(name = "enableReverseCharge")
    private Boolean enableReverseCharge;

    @Column(name = "submitVatManually", columnDefinition = "boolean default true")
    private boolean submitVatManually;

    private Boolean isAgent;

    private String agentNumber;

    @Column(name = "hmrcAuthorized", columnDefinition = "boolean default false")
    private boolean hmrcAuthorized;

    @Column(name = "hideOpenInvoicesGainLossAmount", columnDefinition = "boolean default false")
    private Boolean hideOpenInvoicesGainLossAmount;

    @Column(name = "mandatory_project_for_expense_claims", columnDefinition = "boolean default false")
    private Boolean mandatoryProjectForExpenseClaims = false;

    @Column(name = "mandatory_project_for_purchase_orders", columnDefinition = "boolean default false")
    private Boolean mandatoryProjectForPurchaseOrders = false;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(EdsCurrency currency) {
        this.currency = currency;
    }

    public Date getFinancialYearEnd() {
        return financialYearEnd;
    }

    public void setFinancialYearEnd(Date financialYearEnd) {
        this.financialYearEnd = financialYearEnd;
    }

    public Integer getProductPriceScale() {
        return productPriceScale;
    }

    public void setProductPriceScale(Integer productPrice) {
        this.productPriceScale = productPrice;
    }

    public Integer getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(Integer productQuantity) {
        this.productQuantity = productQuantity;
    }

    public Integer getCalculationScale() {
        return calculationScale;
    }

    public void setCalculationScale(Integer calculationScale) {
        this.calculationScale = calculationScale;
    }

    public Integer getAccountingCalculationScale() {
        return (calculationScale != null && calculationScale > 0) ? calculationScale : 2;
    }

    public Integer getAccountingQtyCalculationScale() {
        return getProductQuantity() != null ? getProductQuantity() : 2;
    }

    public Integer getAccountingUnitPriceCalculationScale() {
        if (getProductPriceScale() != null) {
            return getProductPriceScale();
        }
        if (calculationScale != null && calculationScale > 0) {
            return calculationScale;
        }
        return 4;
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

    public EdsReference getTaxPeriod() {
        return taxPeriod;
    }

    public void setTaxPeriod(EdsReference taxPeriod) {
        this.taxPeriod = taxPeriod;
    }

    public EdsReference getTaxBasis() {
        return taxBasis;
    }

    public void setTaxBasis(EdsReference taxBasis) {
        this.taxBasis = taxBasis;
    }

    public Date getConversionDate() {
        return conversionDate;
    }

    public void setConversionDate(Date conversionDate) {
        this.conversionDate = conversionDate;
    }

    public Date getBlockBeforeDate() {
        return blockBeforeDate;
    }

    public void setBlockBeforeDate(Date blockBeforeDate) {
        this.blockBeforeDate = blockBeforeDate;
    }

    public Boolean getDefaultTaxesCreated() {
        return defaultTaxesCreated;
    }

    public void setDefaultTaxesCreated(Boolean defaultTaxesCreated) {
        this.defaultTaxesCreated = defaultTaxesCreated;
    }

    public String getFlatRateReturn() {
        return flatRateReturn;
    }

    public void setFlatRateReturn(String flatRateReturn) {
        this.flatRateReturn = flatRateReturn;
    }

    public boolean isFlatRate() {
        return AccountingConstants.YES.equals(flatRateReturn);
    }

    public boolean isShowVatNumberInInvoices() {
        return showVatNumberInInvoices != null ? showVatNumberInInvoices : false;
    }

    public void setShowVatNumberInInvoices(Boolean showVatNumberInInvoices) {
        this.showVatNumberInInvoices = showVatNumberInInvoices;
    }

    public Boolean enableIT() {
        return enableIT != null ? enableIT : true;
    }

    public void setEnableIT(Boolean enableIT) {
        this.enableIT = enableIT;
    }

    public Boolean isAutomaticExRateUpdate() {
        return automaticExRateUpdate != null ? automaticExRateUpdate : false;
    }

    public Boolean isExRateHistoryEnabled() {
        return !isAutomaticExRateUpdate() && (enableMultipleCurrency != null && enableMultipleCurrency);
    }

    public void setAutomaticExRateUpdate(Boolean automaticExRateUpdate) {
        this.automaticExRateUpdate = automaticExRateUpdate;
    }

    /*public Boolean getEnableMultipleCurrency() {
        return Boolean.TRUE;//enableMultipleCurrency; TODO We'll remove this option later
    }

    public void setEnableMultipleCurrency(Boolean enableMultipleCurrency) {
        this.enableMultipleCurrency = enableMultipleCurrency;
    }*/

    public Boolean getEnableDoubleMessage() {
        return enableDoubleMessage;
    }

    public void setEnableDoubleMessage(Boolean enableDoubleMessage) {
        this.enableDoubleMessage = enableDoubleMessage;
    }

    public Boolean isEnableMultipleSalesPrice() {
        return enableMultipleSalesPrice != null ? enableMultipleSalesPrice : Boolean.FALSE;
    }

    public void setEnableMultipleSalesPrice(Boolean enableMultipleSalesPrice) {
        this.enableMultipleSalesPrice = enableMultipleSalesPrice;
    }

    public Boolean getEnableDiscountAccount() {
        return enableDiscountAccount != null ? enableDiscountAccount : Boolean.FALSE;
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

    public Boolean getEnableAccountingDepartmentRelation() {
        return enableAccountingDepartmentRelation != null ? enableAccountingDepartmentRelation : Boolean.FALSE;
    }

    public void setEnableAccountingDepartmentRelation(Boolean enableAccountingDepartmentRelation) {
        this.enableAccountingDepartmentRelation = enableAccountingDepartmentRelation;
    }

    public Integer getRoundingExchRate() {
        return roundingExchRate;
    }

    public void setRoundingExchRate(Integer roundingExchRate) {
        this.roundingExchRate = roundingExchRate;
    }

    public Integer getExchangeRateScale() {
        return getRoundingExchRate() != null ? getRoundingExchRate() : 4;
    }

    public Boolean isShowVatNumberInListings() {
        return showVatNumberInListings != null ? showVatNumberInListings : false;
    }

    public void setShowVatNumberInListings(Boolean showVatNumberInListings) {
        this.showVatNumberInListings = showVatNumberInListings;
    }

    public Boolean isShowVatReturnReport() {
        return showVatReturnReport == null ? false : showVatReturnReport;
    }

    public void setShowVatReturnReport(Boolean showVatReturnReport) {
        this.showVatReturnReport = showVatReturnReport;
    }

    public Boolean isEnablePostedDateTransaction() {
        return enablePostedDateTransaction != null ? enablePostedDateTransaction : Boolean.FALSE;
    }

    public void setEnablePostedDateTransaction(Boolean enablePostedDateTransaction) {
        this.enablePostedDateTransaction = enablePostedDateTransaction;
    }

    public String getCustomVatName() {
        return customVatName;
    }

    public void setCustomVatName(String customVatName) {
        this.customVatName = customVatName;
    }

    public EdsReference getIncomeTaxPeriod() {
        return incomeTaxPeriod;
    }

    public void setIncomeTaxPeriod(EdsReference incomeTaxPeriod) {
        this.incomeTaxPeriod = incomeTaxPeriod;
    }

    public BigDecimal getIncomeTaxRate() {
        return incomeTaxRate;
    }

    public void setIncomeTaxRate(BigDecimal incomeTaxRate) {
        this.incomeTaxRate = incomeTaxRate;
    }

    public Integer getIncomeTaxAccount() {
        return incomeTaxAccount;
    }

    public void setIncomeTaxAccount(Integer incomeTaxAccount) {
        this.incomeTaxAccount = incomeTaxAccount;
    }

    public Integer getTaxRateScale() {
        return taxRateScale != null && taxRateScale > 0 ? taxRateScale : 2;
    }

    public void setTaxRateScale(Integer taxRateScale) {
        this.taxRateScale = taxRateScale;
    }

    public Integer getDiscountScale() {
        return discountScale != null ? discountScale : 2;
    }

    public void setDiscountScale(Integer discountScale) {
        this.discountScale = discountScale;
    }

    public Integer getProgressInvoicingAmountScale() {
        return this.progressInvoicingAmountScale;
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

    public boolean isIncludeCurrentYearEariningInRetained() {
        return includeCurrentYearEariningInRetained;
    }

    public void setIncludeCurrentYearEariningInRetained(boolean includeCurrentYearEariningInRetained) {
        this.includeCurrentYearEariningInRetained = includeCurrentYearEariningInRetained;
    }

    public String getBalancesheetSettingsJSONData() {
        return balancesheetSettingsJSONData;
    }

    public void setBalancesheetSettingsJSONData(String balancesheetSettingsJSONData) {
        this.balancesheetSettingsJSONData = balancesheetSettingsJSONData;
    }

    public Boolean getEnableDepreciationDatePeriod() {
        return enableDepreciationDatePeriod;
    }

    public void setEnableDepreciationDatePeriod(Boolean enableDepreciationDatePeriod) {
        this.enableDepreciationDatePeriod = enableDepreciationDatePeriod;
    }

    public Boolean getEnableBatchTrackingItems() {
        return enableBatchTrackingItems;
    }

    public void setEnableBatchTrackingItems(Boolean enableBatchTrackingItems) {
        this.enableBatchTrackingItems = enableBatchTrackingItems;
    }

    public Boolean getRestrictCreatingOrUpdatingInvoices() {
        return restrictCreatingOrUpdatingInvoices != null && restrictCreatingOrUpdatingInvoices;
    }

    public void setRestrictCreatingOrUpdatingInvoices(Boolean restrictCreatingOrUpdatingInvoices) {
        this.restrictCreatingOrUpdatingInvoices = restrictCreatingOrUpdatingInvoices;
    }

    public boolean isGenerateNewNumber() {
        return generateNewNumber;
    }

    public void setGenerateNewNumber(boolean generateNewNumber) {
        this.generateNewNumber = generateNewNumber;
    }

    public String getCostOfGoodsCalculationType() {
        return costOfGoodsCalculationType;
    }

    public void setCostOfGoodsCalculationType(String costOfGoodsCalculationType) {
        this.costOfGoodsCalculationType = costOfGoodsCalculationType;
    }

    public boolean isVatRegistered() {
        return vatRegistered;
    }

    public void setVatRegistered(boolean vatRegistered) {
        this.vatRegistered = vatRegistered;
    }

    public Date getVatRegisteredOn() {
        return vatRegisteredOn;
    }

    public void setVatRegisteredOn(Date vatRegisteredOn) {
        this.vatRegisteredOn = vatRegisteredOn;
    }

    public Date getTaxGenerationDate() {
        return taxGenerationDate;
    }

    public void setTaxGenerationDate(Date taxGenerationDate) {
        this.taxGenerationDate = taxGenerationDate;
    }

    public boolean isEnableContractOutsite() {
        return enableContractOutsite;
    }

    public void setEnableContractOutsite(boolean enableContractOutsite) {
        this.enableContractOutsite = enableContractOutsite;
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

    public boolean isEnableDeferredTransaction() {
        return enableDeferredTransaction;
    }

    public void setEnableDeferredTransaction(boolean enableDeferredTransaction) {
        this.enableDeferredTransaction = enableDeferredTransaction;
    }

    public String getVatAccountingBasis() {
        return vatAccountingBasis;
    }

    public void setVatAccountingBasis(String vatAccountingBasis) {
        this.vatAccountingBasis = vatAccountingBasis;
    }

    public Boolean getEnableReverseCharge() {
        return enableReverseCharge;
    }

    public void setEnableReverseCharge(Boolean enableReverseCharge) {
        this.enableReverseCharge = enableReverseCharge;
    }

    public boolean isSubmitVatManually() {
        return submitVatManually;
    }

    public void setSubmitVatManually(boolean submitVatManually) {
        this.submitVatManually = submitVatManually;
    }

    public Boolean isAgent() {
        return Boolean.TRUE.equals(isAgent);
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

    public boolean isHmrcAuthorized() {
        return hmrcAuthorized;
    }

    public void setHmrcAuthorized(boolean hmrcAuthorized) {
        this.hmrcAuthorized = hmrcAuthorized;
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

    public String getOtpNumber() {
        return otpNumber;
    }

    public void setOtpNumber(String otpNumber) {
        this.otpNumber = otpNumber;
    }

    public Boolean isHideOpenInvoicesGainLossAmount() {
        return hideOpenInvoicesGainLossAmount != null ? hideOpenInvoicesGainLossAmount : false;
    }

    public void setHideOpenInvoicesGainLossAmount(Boolean hideOpenInvoicesGainLossAmount) {
        this.hideOpenInvoicesGainLossAmount = hideOpenInvoicesGainLossAmount;
    }

    public Boolean getMandatoryProjectForExpenseClaims() {
        return mandatoryProjectForExpenseClaims != null && mandatoryProjectForExpenseClaims;
    }

    public void setMandatoryProjectForExpenseClaims(Boolean mandatoryProjectForExpenseClaims) {
        this.mandatoryProjectForExpenseClaims = mandatoryProjectForExpenseClaims;
    }

    public Boolean getMandatoryProjectForPurchaseOrders() {
        return mandatoryProjectForPurchaseOrders != null && mandatoryProjectForPurchaseOrders;
    }

    public void setMandatoryProjectForPurchaseOrders(Boolean mandatoryProjectForPurchaseOrders) {
        this.mandatoryProjectForPurchaseOrders = mandatoryProjectForPurchaseOrders;
    }

    public boolean isReportsOldVerion() {
        return reportsOldVerion;
    }

    public void setReportsOldVerion(boolean reportsOldVerion) {
        this.reportsOldVerion = reportsOldVerion;
    }
}
