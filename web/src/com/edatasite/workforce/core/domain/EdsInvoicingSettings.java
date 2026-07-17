package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
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

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 07.11.2007
 * Time: 12:09:12
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "invoicingSettings")
public class EdsInvoicingSettings extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;
    private Boolean shownEmployeeFooter = true;
    private Integer defaultPaymentAccountId;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    private Integer paymentDue;
    private Integer salesQuoteDue;
    private Integer purchaseOrderDue;

    private Integer bankAccountId;

    private Integer taxCalculationType;

    private String invoiceNumberingFormat;
    private String salesQuoteNumberingFormat;
    private String salesOrderNumberingFormat;
    private String purchaseOrderNumberingFormat;
    private String piNumberingFormat;
    private String cnNumberingFormat;
    private String dnNumberingFormat;

    private String payPalAccount;

    @Column(columnDefinition = "boolean default true")
    private Boolean isConvertInvoiceBtnShow;

    @Column(columnDefinition = "boolean default false")
    private Boolean isPurchaseInvoiceNumberingShow = false;
    private Boolean salesQuoteProgressInvoicing;
    private Boolean expandProductGroup;


    private Integer invoiceType;// Product Invoice = 0, Service Invoice = 1
    private Integer dueDateType;// Due Date = 0, Terms = 1
    private Integer convertPurchaseInvoiceDateType;// PO Date = 0, Receive Date = 1

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paypalpaymentaccount")
    private EdsAccount payPalPaymentAccount;

    @JoinColumn(name = "merchant_id")
    private String merchantId;

    //Mastercard Merchant parameters
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "googlecheckoutpaymentaccount")
    private EdsAccount googleCheckoutPaymentAccount;


    //Mastercard Merchant parameters
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mastercardpaymentaccount")
    private EdsAccount masterCardPaymentAccount;

    private String masterCardMerchandID;
    private String masterCardAccessCode;
    private String masterCardSecretKey;

    //Elavon Merchant parameters
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "elavonpaymentaccount")
    private EdsAccount elavonPaymentAccount;

    private String elavonMerchandID;
    private String elavonUserID;
    private String elavonPIN;

    @Column(name = "addressTemplate")
    @Type(type = "text")
    private String addressTemplate;

    private Integer invoiceLogoWidth;    //For PDF
    private Integer invoiceLogoHeight;   //For PDF
    private Boolean showPDFPoweredBy = true;    //For PDF
    private Boolean showPDFPaging;       //For PDF
    private Integer invoiceStampWidth;    //For PDF
    private Integer invoiceStampHeight;   //For PDF

    private Boolean numberingRestartEnabled = false;
    private Integer numberingRestartDate;
    private Integer numberingRestartMonth;

    private String pdfNamingFormat;
    private String pdfNamingPrefix;

    private String salesReceiptPdfNamingFormat;
    private String salesReceiptPdfNamingPrefix;

    private String salesOrderPdfNamingFormat;
    private String salesOrderPdfNamingPrefix;

    private String purchaseOrderPdfNamingFormat;
    private String purchaseOrderPdfNamingPrefix;

    private String customerPdfNamingFormat;
    private String customerPdfNamingPrefix;

    private String supplierPdfNamingFormat;
    private String supplierPdfNamingPrefix;

    private String invoiceCreditNoteNumberingFormat;

    private Integer quoteConvertToInvoiceCustomType;
    private String payMeMerchantId;
    private String paymeServiceId;
    private BigDecimal paymeServiceFee;
    private String clickMerchantId;
    private String clickServiceId;
    private String revolutEmail;
    private String revolutSecretApiKey;

    @Column(columnDefinition = " boolean DEFAULT false")
    private boolean salesQuoteTermCopyToSalesInvoice = false;

    @Column(columnDefinition = " boolean DEFAULT false")
    private boolean salesQuoteTermCopyToSalesOrder = false;

    @Column(columnDefinition = " boolean DEFAULT false")
    private boolean salesOrderTermCopyToSalesInvoice = false;

    @Column(name = "stripe_user_id")
    private String stripeUserId;
    @Column(name = "stripe_auth_resp")
    @Type(type = "text")
    private String stripeAuthResp;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stripepaymentaccount_id")
    private EdsAccount stripePaymentAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "defaccountso_id")
    private EdsAccount defAccountSO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "defaccountsq_id")
    private EdsAccount defAccountSQ;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "defaccountsi_id")
    private EdsAccount defAccountSI;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "defaccountpo_id")
    private EdsAccount defAccountPO;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "defaccountpi_id")
    private EdsAccount defAccountPI;

    @JoinColumn(name = "defdiscountso_id")
    private Integer defDiscountSO;

    @JoinColumn(name = "defdiscountsq_id")
    private Integer defDiscountSQ;

    @JoinColumn(name = "defdiscountsi_id")
    private Integer defDiscountSI;

    @JoinColumn(name = "defdiscountpo_id")
    private Integer defDiscountPO;

    @JoinColumn(name = "defdiscountpi_id")
    private Integer defDiscountPI;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paymepaymentaccount_id")
    private EdsAccount paymePaymentAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clickpaymentaccount_id")
    private EdsAccount clickPaymentAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "revolutPaymentAccount_id")
    private EdsAccount revolutPaymentAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "revolut_expense_account_id")
    private EdsAccount revolutExpenseAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payme_expense_account_id")
    private EdsAccount paymeExpenseAccount;


    public Integer getPaymentDue() {
        return paymentDue;
    }

    public void setPaymentDue(Integer paymentDue) {
        this.paymentDue = paymentDue;
    }

    public Integer getSalesQuoteDue() {
        return salesQuoteDue;
    }

    public void setSalesQuoteDue(Integer salesQuoteDue) {
        this.salesQuoteDue = salesQuoteDue;
    }

    public Integer getPurchaseOrderDue() {
        return purchaseOrderDue;
    }

    public void setPurchaseOrderDue(Integer purchaseOrderDue) {
        this.purchaseOrderDue = purchaseOrderDue;
    }

    public Integer getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(Integer bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public Integer getTaxCalculationType() {
        return taxCalculationType;
    }

    public void setTaxCalculationType(Integer taxCalculationType) {
        this.taxCalculationType = taxCalculationType;
    }

    public String getInvoiceNumberingFormat() {
        return invoiceNumberingFormat;
    }

    private Boolean copySQIntroduction;
    private Boolean copySOIntroduction;

    public void setInvoiceNumberingFormat(String invoiceNumberingFormat) {
        this.invoiceNumberingFormat = invoiceNumberingFormat;
    }

    public String getSalesQuoteNumberingFormat() {
        return salesQuoteNumberingFormat;
    }

    public void setSalesQuoteNumberingFormat(String salesQuoteNumberingFormat) {
        this.salesQuoteNumberingFormat = salesQuoteNumberingFormat;
    }

    public String getSalesOrderNumberingFormat() {
        return salesOrderNumberingFormat;
    }

    public void setSalesOrderNumberingFormat(String salesOrderNumberingFormat) {
        this.salesOrderNumberingFormat = salesOrderNumberingFormat;
    }

    public String getPurchaseOrderNumberingFormat() {
        return purchaseOrderNumberingFormat;
    }

    public void setPurchaseOrderNumberingFormat(String purchaseOrderNumberingFormat) {
        this.purchaseOrderNumberingFormat = purchaseOrderNumberingFormat;
    }

    public String getPiNumberingFormat() {
        return piNumberingFormat;
    }

    public void setPiNumberingFormat(String piNumberingFormat) {
        this.piNumberingFormat = piNumberingFormat;
    }

    public String getCnNumberingFormat() {
        return cnNumberingFormat;
    }

    public void setCnNumberingFormat(String cnNumberingFormat) {
        this.cnNumberingFormat = cnNumberingFormat;
    }

    public String getDnNumberingFormat() {
        return dnNumberingFormat;
    }

    public void setDnNumberingFormat(String dnNumberingFormat) {
        this.dnNumberingFormat = dnNumberingFormat;
    }

    public String getPayPalAccount() {
        return payPalAccount;
    }

    public void setPayPalAccount(String payPalAccount) {
        this.payPalAccount = payPalAccount;
    }

    public EdsAccount getPayPalPaymentAccount() {
        return payPalPaymentAccount;
    }

    public void setPayPalPaymentAccount(EdsAccount payPalPaymentAccount) {
        this.payPalPaymentAccount = payPalPaymentAccount;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public EdsAccount getGoogleCheckoutPaymentAccount() {
        return googleCheckoutPaymentAccount;
    }

    public void setGoogleCheckoutPaymentAccount(EdsAccount googleCheckoutPaymentAccount) {
        this.googleCheckoutPaymentAccount = googleCheckoutPaymentAccount;
    }

    public String getAddressTemplate() {
        return addressTemplate;
    }

    public void setAddressTemplate(String addressTemplate) {
        this.addressTemplate = addressTemplate;
    }

    public Integer getInvoiceLogoWidth() {
        return invoiceLogoWidth;
    }

    public void setInvoiceLogoWidth(Integer invoiceLogoWidth) {
        this.invoiceLogoWidth = invoiceLogoWidth;
    }

    public Integer getInvoiceLogoHeight() {
        return invoiceLogoHeight;
    }

    public void setInvoiceLogoHeight(Integer invoiceLogoHeight) {
        this.invoiceLogoHeight = invoiceLogoHeight;
    }

    public Boolean isShowPDFPoweredBy() {
        return showPDFPoweredBy != null && showPDFPoweredBy;
    }

    public void setShowPDFPoweredBy(Boolean showPDFPoweredBy) {
        this.showPDFPoweredBy = showPDFPoweredBy;
    }

    public Boolean isShowPDFPaging() {
        return showPDFPaging;
    }

    public void setShowPDFPaging(Boolean showPDFPaging) {
        this.showPDFPaging = showPDFPaging;
    }

    public Boolean isNumberingRestartEnabled() {
        return numberingRestartEnabled != null ? numberingRestartEnabled : false;
    }

    public void setNumberingRestartEnabled(Boolean numberingRestartEnabled) {
        this.numberingRestartEnabled = numberingRestartEnabled;
    }

    public Integer getNumberingRestartDate() {
        return numberingRestartDate;
    }

    public void setNumberingRestartDate(Integer numberingRestartDate) {
        this.numberingRestartDate = numberingRestartDate;
    }

    public Integer getNumberingRestartMonth() {
        return numberingRestartMonth;
    }

    public void setNumberingRestartMonth(Integer numberingRestartMonth) {
        this.numberingRestartMonth = numberingRestartMonth;
    }

    public String getPdfNamingFormat() {
        return pdfNamingFormat;
    }

    public void setPdfNamingFormat(String pdfNamingFormat) {
        this.pdfNamingFormat = pdfNamingFormat;
    }

    public String getPdfNamingPrefix() {
        return pdfNamingPrefix;
    }

    public void setPdfNamingPrefix(String pdfNamingPrefix) {
        this.pdfNamingPrefix = pdfNamingPrefix;
    }

    public String getSalesReceiptPdfNamingFormat() {
        return salesReceiptPdfNamingFormat;
    }

    public void setSalesReceiptPdfNamingFormat(String salesReceiptPdfNamingFormat) {
        this.salesReceiptPdfNamingFormat = salesReceiptPdfNamingFormat;
    }

    public String getSalesReceiptPdfNamingPrefix() {
        return salesReceiptPdfNamingPrefix;
    }

    public void setSalesReceiptPdfNamingPrefix(String salesReceiptPdfNamingPrefix) {
        this.salesReceiptPdfNamingPrefix = salesReceiptPdfNamingPrefix;
    }

    public Integer getQuoteConvertToInvoiceCustomType() {
        return quoteConvertToInvoiceCustomType;
    }

    public void setQuoteConvertToInvoiceCustomType(Integer quoteConvertToInvoiceCustomType) {
        this.quoteConvertToInvoiceCustomType = quoteConvertToInvoiceCustomType;
    }

    public Integer getInvoiceStampWidth() {
        return invoiceStampWidth;
    }

    public void setInvoiceStampWidth(Integer invoiceStampWidth) {
        this.invoiceStampWidth = invoiceStampWidth;
    }

    public Integer getInvoiceStampHeight() {
        return invoiceStampHeight;
    }

    public void setInvoiceStampHeight(Integer invoiceStampHeight) {
        this.invoiceStampHeight = invoiceStampHeight;
    }

    public EdsAccount getMasterCardPaymentAccount() {
        return masterCardPaymentAccount;
    }

    public void setMasterCardPaymentAccount(EdsAccount masterCardPaymentAccount) {
        this.masterCardPaymentAccount = masterCardPaymentAccount;
    }

    public String getMasterCardMerchandID() {
        return masterCardMerchandID;
    }

    public void setMasterCardMerchandID(String masterCardMerchandID) {
        this.masterCardMerchandID = masterCardMerchandID;
    }

    public String getMasterCardAccessCode() {
        return masterCardAccessCode;
    }

    public void setMasterCardAccessCode(String masterCardAccessCode) {
        this.masterCardAccessCode = masterCardAccessCode;
    }

    public String getMasterCardSecretKey() {
        return masterCardSecretKey;
    }

    public void setMasterCardSecretKey(String masterCardSecretKey) {
        this.masterCardSecretKey = masterCardSecretKey;
    }

    public EdsAccount getElavonPaymentAccount() {
        return elavonPaymentAccount;
    }

    public void setElavonPaymentAccount(EdsAccount elavonPaymentAccount) {
        this.elavonPaymentAccount = elavonPaymentAccount;
    }

    public String getElavonMerchandID() {
        return elavonMerchandID;
    }

    public void setElavonMerchandID(String elavonMerchandID) {
        this.elavonMerchandID = elavonMerchandID;
    }

    public String getElavonUserID() {
        return elavonUserID;
    }

    public void setElavonUserID(String elavonUserID) {
        this.elavonUserID = elavonUserID;
    }

    public String getElavonPIN() {
        return elavonPIN;
    }

    public void setElavonPIN(String elavonPIN) {
        this.elavonPIN = elavonPIN;
    }

    public Boolean isSalesQuoteProgressInvoicing() {
        return salesQuoteProgressInvoicing != null && salesQuoteProgressInvoicing;
    }

    public void setSalesQuoteProgressInvoicing(Boolean salesQuoteProgressInvoicing) {
        this.salesQuoteProgressInvoicing = salesQuoteProgressInvoicing;
    }

    public Boolean isExpandProductGroup() {
        return expandProductGroup != null && expandProductGroup;
    }

    public void setExpandProductGroup(Boolean expandProductGroup) {
        this.expandProductGroup = expandProductGroup;
    }

    public Boolean isConvertInvoiceBtnShow() {
        return isConvertInvoiceBtnShow == null || isConvertInvoiceBtnShow;
    }

    public void setConvertInvoiceBtnShow(Boolean isConvertInvoiceBtnShow) {
        this.isConvertInvoiceBtnShow = isConvertInvoiceBtnShow;
    }

    public Boolean getIsPurchaseInvoiceNumberingShow() {
        return isPurchaseInvoiceNumberingShow != null && isPurchaseInvoiceNumberingShow;
    }

    public void setIsPurchaseInvoiceNumberingShow(Boolean isPurchaseInvoiceNumberingShow) {
        this.isPurchaseInvoiceNumberingShow = isPurchaseInvoiceNumberingShow;
    }

    public Integer getInvoiceType() {
        return invoiceType != null ? invoiceType : 0;
    }

    public void setInvoiceType(Integer invoiceType) {
        this.invoiceType = invoiceType;
    }

    public Integer getDueDateType() {
        return dueDateType != null ? dueDateType : 0;
    }

    public void setDueDateType(Integer dueDateType) {
        this.dueDateType = dueDateType;
    }

    public Integer getConvertPurchaseInvoiceDateType() {
        return convertPurchaseInvoiceDateType != null ? convertPurchaseInvoiceDateType : 0;
    }

    public void setConvertPurchaseInvoiceDateType(Integer convertPurchaseInvoiceDateType) {
        this.convertPurchaseInvoiceDateType = convertPurchaseInvoiceDateType;
    }

    public boolean isSalesQuoteTermCopyToSalesInvoice() {
        return salesQuoteTermCopyToSalesInvoice;
    }

    public void setSalesQuoteTermCopyToSalesInvoice(boolean salesQuoteTermCopyToSalesInvoice) {
        this.salesQuoteTermCopyToSalesInvoice = salesQuoteTermCopyToSalesInvoice;
    }

    public boolean isSalesQuoteTermCopyToSalesOrder() {
        return salesQuoteTermCopyToSalesOrder;
    }

    public void setSalesQuoteTermCopyToSalesOrder(boolean salesQuoteTermCopyToSalesOrder) {
        this.salesQuoteTermCopyToSalesOrder = salesQuoteTermCopyToSalesOrder;
    }

    public boolean isSalesOrderTermCopyToSalesInvoice() {
        return salesOrderTermCopyToSalesInvoice;
    }

    public void setSalesOrderTermCopyToSalesInvoice(boolean salesOrderTermCopyToSalesInvoice) {
        this.salesOrderTermCopyToSalesInvoice = salesOrderTermCopyToSalesInvoice;
    }

    public String getCustomerPdfNamingFormat() {
        return customerPdfNamingFormat;
    }

    public void setCustomerPdfNamingFormat(String customerPdfNamingFormat) {
        this.customerPdfNamingFormat = customerPdfNamingFormat;
    }

    public String getCustomerPdfNamingPrefix() {
        return customerPdfNamingPrefix;
    }

    public void setCustomerPdfNamingPrefix(String customerPdfNamingPrefix) {
        this.customerPdfNamingPrefix = customerPdfNamingPrefix;
    }

    public String getSupplierPdfNamingFormat() {
        return supplierPdfNamingFormat;
    }

    public void setSupplierPdfNamingFormat(String supplierPdfNamingFormat) {
        this.supplierPdfNamingFormat = supplierPdfNamingFormat;
    }

    public String getSupplierPdfNamingPrefix() {
        return supplierPdfNamingPrefix;
    }

    public void setSupplierPdfNamingPrefix(String supplierPdfNamingPrefix) {
        this.supplierPdfNamingPrefix = supplierPdfNamingPrefix;
    }

    public void setShownEmployeeFooter(boolean shownEmployeeFooter) {
        this.shownEmployeeFooter = shownEmployeeFooter;
    }

    public Boolean isShownEmployeeFooter() {
        return shownEmployeeFooter;
    }

    public Boolean getCopySQIntroduction() {
        return copySQIntroduction != null && copySQIntroduction;
    }

    public void setCopySQIntroduction(Boolean copySQIntroduction) {
        this.copySQIntroduction = copySQIntroduction;
    }

    public Boolean getCopySOIntroduction() {
        return copySOIntroduction != null && copySOIntroduction;
    }

    public void setCopySOIntroduction(Boolean copySOIntroduction) {
        this.copySOIntroduction = copySOIntroduction;
    }

    public String getInvoiceCreditNoteNumberingFormat() {
        return invoiceCreditNoteNumberingFormat;
    }

    public void setInvoiceCreditNoteNumberingFormat(String invoiceCreditNoteNumberingFormat) {
        this.invoiceCreditNoteNumberingFormat = invoiceCreditNoteNumberingFormat;
    }

    public void setDefaultPaymentAccountId(Integer defaultPaymentAccountId) {
        this.defaultPaymentAccountId = defaultPaymentAccountId;
    }

    public Integer getDefaultPaymentAccountId() {
        return defaultPaymentAccountId;
    }

    public String getStripeUserId() {
        return stripeUserId;
    }

    public void setStripeUserId(String stripeUserId) {
        this.stripeUserId = stripeUserId;
    }

    public String getStripeAuthResp() {
        return stripeAuthResp;
    }

    public void setStripeAuthResp(String stripeAuthResp) {
        this.stripeAuthResp = stripeAuthResp;
    }

    public EdsAccount getStripePaymentAccount() {
        return stripePaymentAccount;
    }

    public void setStripePaymentAccount(EdsAccount stripePaymentAccount) {
        this.stripePaymentAccount = stripePaymentAccount;
    }

    public EdsAccount getDefAccountSO() {
        return this.defAccountSO;
    }

    public void setDefAccountSO(final EdsAccount defAccountSO) {
        this.defAccountSO = defAccountSO;
    }

    public EdsAccount getDefAccountSQ() {
        return this.defAccountSQ;
    }

    public void setDefAccountSQ(final EdsAccount defAccountSQ) {
        this.defAccountSQ = defAccountSQ;
    }

    public EdsAccount getDefAccountSI() {
        return this.defAccountSI;
    }

    public void setDefAccountSI(final EdsAccount defAccountSI) {
        this.defAccountSI = defAccountSI;
    }

    public EdsAccount getDefAccountPO() {
        return this.defAccountPO;
    }

    public void setDefAccountPO(final EdsAccount defAccountPO) {
        this.defAccountPO = defAccountPO;
    }

    public EdsAccount getDefAccountPI() {
        return this.defAccountPI;
    }

    public void setDefAccountPI(final EdsAccount defAccountPI) {
        this.defAccountPI = defAccountPI;
    }

    public String getSalesOrderPdfNamingFormat() {
        return salesOrderPdfNamingFormat;
    }

    public void setSalesOrderPdfNamingFormat(String salesOrderPdfNamingFormat) {
        this.salesOrderPdfNamingFormat = salesOrderPdfNamingFormat;
    }

    public String getSalesOrderPdfNamingPrefix() {
        return salesOrderPdfNamingPrefix;
    }

    public void setSalesOrderPdfNamingPrefix(String salesOrderPdfNamingPrefix) {
        this.salesOrderPdfNamingPrefix = salesOrderPdfNamingPrefix;
    }

    public String getPurchaseOrderPdfNamingFormat() {
        return purchaseOrderPdfNamingFormat;
    }

    public void setPurchaseOrderPdfNamingFormat(String purchaseOrderPdfNamingFormat) {
        this.purchaseOrderPdfNamingFormat = purchaseOrderPdfNamingFormat;
    }

    public String getPurchaseOrderPdfNamingPrefix() {
        return purchaseOrderPdfNamingPrefix;
    }

    public void setPurchaseOrderPdfNamingPrefix(String purchaseOrderPdfNamingPrefix) {
        this.purchaseOrderPdfNamingPrefix = purchaseOrderPdfNamingPrefix;
    }

    public String getPayMeMerchantId() {
        return payMeMerchantId;
    }

    public void setPayMeMerchantId(String payMeMerchantId) {
        this.payMeMerchantId = payMeMerchantId;
    }

    public EdsAccount getPaymePaymentAccount() {
        return paymePaymentAccount;
    }

    public void setPaymePaymentAccount(EdsAccount paymePaymentAccount) {
        this.paymePaymentAccount = paymePaymentAccount;
    }

    public EdsAccount getClickPaymentAccount() {
        return clickPaymentAccount;
    }

    public void setClickPaymentAccount(EdsAccount clickPaymentAccount) {
        this.clickPaymentAccount = clickPaymentAccount;
    }

    public String getClickMerchantId() {
        return clickMerchantId;
    }

    public void setClickMerchantId(String clickMerchantId) {
        this.clickMerchantId = clickMerchantId;
    }

    public String getClickServiceId() {
        return clickServiceId;
    }

    public void setClickServiceId(String clickServiceId) {
        this.clickServiceId = clickServiceId;
    }

    public String getPaymeServiceId() {
        return paymeServiceId;
    }

    public void setPaymeServiceId(String paymeServiceId) {
        this.paymeServiceId = paymeServiceId;
    }

    public String getRevolutEmail() {
        return revolutEmail;
    }

    public void setRevolutEmail(String revolutEmail) {
        this.revolutEmail = revolutEmail;
    }

    public EdsAccount getRevolutPaymentAccount() {
        return revolutPaymentAccount;
    }

    public void setRevolutPaymentAccount(EdsAccount revolutPaymentAccount) {
        this.revolutPaymentAccount = revolutPaymentAccount;
    }

    public String getRevolutSecretApiKey() {
        return revolutSecretApiKey;
    }

    public void setRevolutSecretApiKey(String revolutSecretApiKey) {
        this.revolutSecretApiKey = revolutSecretApiKey;
    }

    public EdsAccount getRevolutExpenseAccount() {
        return revolutExpenseAccount;
    }

    public void setRevolutExpenseAccount(EdsAccount revolutExpenseAccount) {
        this.revolutExpenseAccount = revolutExpenseAccount;
    }

    public Integer getDefDiscountSO() {
        return defDiscountSO;
    }

    public void setDefDiscountSO(Integer defDiscountSO) {
        this.defDiscountSO = defDiscountSO;
    }

    public Integer getDefDiscountSQ() {
        return defDiscountSQ;
    }

    public void setDefDiscountSQ(Integer defDiscountSQ) {
        this.defDiscountSQ = defDiscountSQ;
    }

    public Integer getDefDiscountSI() {
        return defDiscountSI;
    }

    public void setDefDiscountSI(Integer defDiscountSI) {
        this.defDiscountSI = defDiscountSI;
    }

    public Integer getDefDiscountPO() {
        return defDiscountPO;
    }

    public void setDefDiscountPO(Integer defDiscountPO) {
        this.defDiscountPO = defDiscountPO;
    }

    public Integer getDefDiscountPI() {
        return defDiscountPI;
    }

    public void setDefDiscountPI(Integer defDiscountPI) {
        this.defDiscountPI = defDiscountPI;
    }

    public EdsAccount getPaymeExpenseAccount() {
        return paymeExpenseAccount;
    }

    public void setPaymeExpenseAccount(EdsAccount paymeExpenseAccount) {
        this.paymeExpenseAccount = paymeExpenseAccount;
    }

    public BigDecimal getPaymeServiceFee() {
        return paymeServiceFee;
    }

    public void setPaymeServiceFee(BigDecimal paymeServiceFee) {
        this.paymeServiceFee = paymeServiceFee;
    }
}
