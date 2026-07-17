package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsPaymentBase;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRentalOrder;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.customfields.EdsPrepaymentCustomFields;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.domain.ObjectHistory;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentItem;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "invoicePayments")
public class EdsInvoicePayment extends EdsPaymentBase implements ObjectHistory {

    public static final String INVOICEPAYMENT_STATUS = "INVOICEPAYMENT_STATUS";
    public static final String REVERSED = "IS_REVERSED";
    public static final String POST_DATED = "POST_DATED"; //that means if you add pre-payment for future date then,
    // transaction will run at that date not, creation/updated date.

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoiceId")
    private EdsInvoice invoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creditNoteId")
    private EdsInvoice creditNote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "statusId")
    private EdsReference status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crmAccountID")
    private EdsCrmAccount crmAccount; //Client or Supplier

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "saleQuoteId")
    private EdsSaleQuote saleQuote;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "saleInvoiceId")
    private EdsSaleInvoice saleInvoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rentalOrderId")
    private EdsRentalOrder rentalOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchaseOrderId")
    private EdsPurchaseOrder purchaseOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectid")
    private EdsProject project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departmentid")
    private EdsDepartment department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receivablePayableID")
    private EdsAccount receivablePayable;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bankFeeID")
    private EdsAccount bankFee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectBalanceID")
    private EdsProjectPrepaymentBalance projectPrepaymentBalance;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vatEFileID")
    @ForeignKey(name = "none")
    private EdsVatEFiling vatEFile;

    @Type(type = "text")
    @Column(name = "note")
    private String note;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fromInvoiceId")
    private EdsInvoice fromInvoice;

    @Column(name = "calc_scale")
    private Integer calcScale;


    @Column(precision = 25, scale = 5)
    private BigDecimal baseAmount;

    @Column(precision = 25, scale = 5)
    private BigDecimal amountInInvoiceCurrency;

    @Column(precision = 25, scale = 5)
    private BigDecimal closedAmount;

    private String type;//RECEIVABLE, PAYABLE, RECEIVABLE_PREPAYMENT, PAYABLE_SUPPLIER_CREDIT, RECEIVABLE_PREPAYMENT_SHARE, PAYABLE_SUPPLIER_CREDIT_SHARE, VATRETURN_PAYMENT_RECEIVABLE, , VATRETURN_PAYMENT_PAYABLE

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "historicalparent")
    private EdsInvoicePayment historicalParent;

    @Type(type = "text")
    private String gatewayReturnedURL;

    private Integer manualJournalID;

    private Integer bankTransferID;

    private Integer bankFeeTransferID;

    private Integer batchPaymentID;

    private Integer paymentRefundID;

    private Integer expenseID;

    private Integer underPaymentID;

    private BigDecimal underPaymentTaxRate;

    private BigDecimal underPaymentTaxAmount;

    private String paymentStatus;

    private String bankFeeType;

    @Column(precision = 25, scale = 5)
    private BigDecimal bankFeeValue;

    private String number;
    private Integer numberInt;

    private Date creationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "appliedPaymentId")
    private EdsInvoicePayment appliedPayment;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private EdsPrepaymentCustomFields prepaymentCustomFields;

    public String getInOutType() {
        if (Constants.RECEIVABLE.equals(type) || AccountingConstants.RECEIVABLE_PREPAYMENT.equals(type)
                || AccountingConstants.RECEIVABLE_PREPAYMENT_SHARE.equals(type) || AccountingConstants.VATRETURN_PAYMENT_RECEIVABLE.equals(type)
                || AccountingConstants.RECEIVABLE_CRM_ACCOUNT_CREDIT.equals(type) || AccountingConstants.RECEIVABLE_MANUAL_CREDIT.equals(type)
                || AccountingConstants.RECEIVABLE_BANKTRANSFER_CREDIT.equals(type)
                || AccountingConstants.PAYABLE_PREPAYMENT_REFUND.equals(type)
                || AccountingConstants.PAYABLE_DEBIT_REFUND.equals(type)) {

            return Constants.RECEIVABLE;
        } else if (Constants.PAYABLE.equals(type) || AccountingConstants.PAYABLE_SUPPLIER_CREDIT.equals(type) || AccountingConstants.PAYABLE_SUPPLIER_CREDIT_SHARE.equals(type)
                || AccountingConstants.VATRETURN_PAYMENT_PAYABLE.equals(type) || AccountingConstants.PAYABLE_BANK_CHECK_SHARE.equals(type)
                || AccountingConstants.PAYABLE_CRM_ACCOUNT_CREDIT.equals(type) || AccountingConstants.PAYABLE_MANUAL_CREDIT.equals(type)
                || AccountingConstants.PAYABLE_BANKTRANSFER_CREDIT.equals(type)
                || AccountingConstants.RECEIVABLE_PREPAYMENT_REFUND.equals(type)
                || AccountingConstants.RECEIVABLE_CREDIT_REFUND.equals(type)) {
            return Constants.PAYABLE;
        }
        return type;
    }

    public Integer getSystemAccountKeyByType() {
        if (Constants.RECEIVABLE.equals(type) || AccountingConstants.RECEIVABLE_PREPAYMENT.equals(type)
                || AccountingConstants.RECEIVABLE_PREPAYMENT_SHARE.equals(type) || AccountingConstants.RECEIVABLE_CRM_ACCOUNT_CREDIT.equals(type)
                || AccountingConstants.RECEIVABLE_MANUAL_CREDIT.equals(type) || AccountingConstants.RECEIVABLE_BANKTRANSFER_CREDIT.equals(type)
                || AccountingConstants.RECEIVABLE_PREPAYMENT_REFUND.equals(type) || AccountingConstants.RECEIVABLE_CREDIT_REFUND.equals(type)) {

            return EdsAccount.ACCOUNTS_RECEIVABLE;
        } else if (Constants.PAYABLE.equals(type) || AccountingConstants.PAYABLE_SUPPLIER_CREDIT.equals(type)
                || AccountingConstants.PAYABLE_SUPPLIER_CREDIT_SHARE.equals(type) || AccountingConstants.PAYABLE_BANK_CHECK_SHARE.equals(type)
                || AccountingConstants.PAYABLE_CRM_ACCOUNT_CREDIT.equals(type) || AccountingConstants.PAYABLE_MANUAL_CREDIT.equals(type) || AccountingConstants.PAYABLE_BANKTRANSFER_CREDIT.equals(type)
                || AccountingConstants.PAYABLE_PREPAYMENT_REFUND.equals(type) || AccountingConstants.PAYABLE_DEBIT_REFUND.equals(type)) {
            return EdsAccount.ACCOUNTS_PAYABLE;
        } else if (AccountingConstants.VATRETURN_PAYMENT_RECEIVABLE.equals(type)) {
            return EdsAccount.VAT_PAYABLE;
        } else if (AccountingConstants.VATRETURN_PAYMENT_PAYABLE.equals(type)) {
            return EdsAccount.VAT_PAYABLE;
        }
        return null;
    }

    public Integer getCalcScale() {
        return calcScale;
    }

    public void setCalcScale(Integer calcScale) {
        this.calcScale = calcScale;
    }

    public PaymentItem getPaymentAsRPC() {
        PaymentItem pItem = new PaymentItem(getUser() != null ? getUser().getFullName() : "");
        pItem.setObjectId(getObjectID());
        pItem.setBatchPaymentID(getBatchPaymentID());
        pItem.setDate(new DateNonConvertable(getPaymentDate()));
        pItem.setReference(getReference() != null ? getReference() : getAppliedPayment() != null ? getAppliedPayment().getReference() : null);
        pItem.setPaidTo(getAccount() != null ? getAccount().getName() : null);
        pItem.setPaidToID(getAccount() != null ? getAccount().getObjectID() : null);

        BigDecimal fullPayment = BigDecimal.ZERO;

        int calcScale = getCalcScale() != null ? getCalcScale() : ServerUtils.getSystemCalculationScale();

        if (invoice != null && invoice.getCurrency().getObjectID().equals(getCurrencyID())) {
            fullPayment = fullPayment.add(getAmountInInvoiceCurrency() != null ? getAmountInInvoiceCurrency() : getAmount()).setScale(calcScale, RoundingMode.HALF_UP);
        } else {
            fullPayment = fullPayment.add((getAmountInInvoiceCurrency() != null ? getAmountInInvoiceCurrency() : getAmount())
                    .divide(getExchangeRate(), calcScale, RoundingMode.HALF_UP));
        }
        pItem.setCalcScale(calcScale);
        pItem.setAmount(fullPayment);

        pItem.setType(getType());
//        pItem.setNote(getNote());
        if ((AccountingConstants.RECEIVABLE_PREPAYMENT_SHARE.equals(getType()) || AccountingConstants.RECEIVABLE_PREPAYMENT_REFUND.equals(getType())) && getAppliedPayment() != null) {
            pItem.setAppliedPaymentID(getAppliedPayment().getObjectID());
        } else if (AccountingConstants.RECEIVABLE_MANUAL_CREDIT.equals(getType())) {
            pItem.setAppliedPaymentID(getManualJournalID());
        }

        if (getCreditNote() != null) {
            pItem.setCrmAccount(getCreditNote().getClientOrSupplier().getAsSelectItem());
        } else if (getInvoice() != null) {
            pItem.setCrmAccount(getInvoice().getClientOrSupplier().getAsSelectItem());
        } else if (getCrmAccount() != null) {
            pItem.setCrmAccount(getCrmAccount().getAsSelectItem());
        }

        if (getInvoice() != null) {
            pItem.setInvoice(new SelectItem(getInvoice().getObjectID(), getInvoice().getNumber()));
        }
        if (getCreditNote() != null) {
            pItem.setCreditNote(new SelectItem(getCreditNote().getObjectID(), getCreditNote().getNumber()));
        }
        if (getProject() != null) {
            pItem.setProject(getProject().getAsSelectItem());
        }
        if (getDepartment() != null) {
            pItem.setDepartment(getDepartment().getAsSelectItem());
        }

        return pItem;
    }

    public void setInvoice(EdsInvoice invoice) {
        this.invoice = invoice;
    }

    public EdsInvoice getInvoice() {
        return invoice;
    }

    public EdsInvoice getCreditNote() {
        return creditNote;
    }

    public void setCreditNote(EdsInvoice creditNote) {
        this.creditNote = creditNote;
    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        this.status = status;
    }

    public EdsCrmAccount getCrmAccount() {
        return crmAccount;
    }

    public void setCrmAccount(EdsCrmAccount crmAccount) {
        this.crmAccount = crmAccount;
    }

    public EdsSaleQuote getSaleQuote() {
        return saleQuote;
    }

    public void setSaleQuote(EdsSaleQuote saleQuote) {
        this.saleQuote = saleQuote;
    }

    public EdsSaleInvoice getSaleInvoice() {
        return saleInvoice;
    }

    public void setSaleInvoice(EdsSaleInvoice saleInvoice) {
        this.saleInvoice = saleInvoice;
    }

    public EdsRentalOrder getRentalOrder() {
        return rentalOrder;
    }

    public void setRentalOrder(EdsRentalOrder rentalOrder) {
        this.rentalOrder = rentalOrder;
    }

    public EdsPurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public void setPurchaseOrder(EdsPurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public EdsProjectPrepaymentBalance getProjectPrepaymentBalance() {
        return projectPrepaymentBalance;
    }

    public void setProjectPrepaymentBalance(EdsProjectPrepaymentBalance projectPrepaymentBalance) {
        this.projectPrepaymentBalance = projectPrepaymentBalance;
    }

    public EdsVatEFiling getVatEFile() {
        return vatEFile;
    }

    public void setVatEFile(EdsVatEFiling vatEFile) {
        this.vatEFile = vatEFile;
    }

    public BigDecimal getBaseAmount() {
        return baseAmount;
    }

    public void setBaseAmount(BigDecimal baseAmount) {
        this.baseAmount = baseAmount;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isReversed() {
        return status != null && REVERSED.equals(status.getCode());
    }

    public EdsInvoicePayment getHistoricalParent() {
        return historicalParent;
    }

    public void setHistoricalParent(EdsInvoicePayment historicalParent) {
        this.historicalParent = historicalParent;
    }

    public String getGatewayReturnedURL() {
        return gatewayReturnedURL;
    }

    public void setGatewayReturnedURL(String gatewayReturnedURL) {
        this.gatewayReturnedURL = gatewayReturnedURL;
    }

    public Integer getManualJournalID() {
        return manualJournalID;
    }

    public void setManualJournalID(Integer manualJournalID) {
        this.manualJournalID = manualJournalID;
    }

    public Integer getBatchPaymentID() {
        return batchPaymentID;
    }

    public void setBatchPaymentID(Integer batchPaymentID) {
        this.batchPaymentID = batchPaymentID;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getBankFeeType() {
        return bankFeeType;
    }

    public void setBankFeeType(String bankFeeType) {
        this.bankFeeType = bankFeeType;
    }

    public BigDecimal getBankFeeValue() {
        return bankFeeValue;
    }

    public void setBankFeeValue(BigDecimal bankFeeValue) {
        this.bankFeeValue = bankFeeValue;
    }

    public EdsInvoicePayment getAppliedPayment() {
        return appliedPayment;
    }

    public void setAppliedPayment(EdsInvoicePayment appliedPayment) {
        this.appliedPayment = appliedPayment;
    }

    public Integer getBankTransferID() {
        return bankTransferID;
    }

    public void setBankTransferID(Integer bankTransferID) {
        this.bankTransferID = bankTransferID;
    }

    public Integer getBankFeeTransferID() {
        return bankFeeTransferID;
    }

    public void setBankFeeTransferID(Integer bankFeeTransferID) {
        this.bankFeeTransferID = bankFeeTransferID;
    }

    public BigDecimal getAmountInInvoiceCurrency() {
        return amountInInvoiceCurrency;
    }

    public void setAmountInInvoiceCurrency(BigDecimal amountInInvoiceCurrency) {
        this.amountInInvoiceCurrency = amountInInvoiceCurrency;
    }

    public BigDecimal getClosedAmount() {
        return this.closedAmount;
    }

    public void setClosedAmount(final BigDecimal closedAmount) {
        this.closedAmount = closedAmount;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getNumberInt() {
        return numberInt;
    }

    public void setNumberInt(Integer numberInt) {
        this.numberInt = numberInt;
    }

    public EdsProject getProject() {
        return project;
    }

    public void setProject(EdsProject project) {
        this.project = project;
    }

    public EdsDepartment getDepartment() {
        return department;
    }

    public void setDepartment(EdsDepartment department) {
        this.department = department;
    }

    public EdsInvoice getFromInvoice() {
        return fromInvoice;
    }

    public void setFromInvoice(EdsInvoice fromInvoice) {
        this.fromInvoice = fromInvoice;
    }

    public EdsAccount getReceivablePayable() {
        return receivablePayable;
    }

    public void setReceivablePayable(EdsAccount receivablePayable) {
        this.receivablePayable = receivablePayable;
    }

    public EdsAccount getBankFee() {
        return bankFee;
    }

    public void setBankFee(EdsAccount bankFee) {
        this.bankFee = bankFee;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Integer getUnderPaymentID() {
        return underPaymentID;
    }

    public void setUnderPaymentID(Integer underPaymentID) {
        this.underPaymentID = underPaymentID;
    }

    public BigDecimal getUnderPaymentTaxRate() {
        return underPaymentTaxRate;
    }

    public void setUnderPaymentTaxRate(BigDecimal underPaymentTaxRate) {
        this.underPaymentTaxRate = underPaymentTaxRate;
    }

    public BigDecimal getUnderPaymentTaxAmount() {
        return underPaymentTaxAmount;
    }

    public void setUnderPaymentTaxAmount(BigDecimal underPaymentTaxAmount) {
        this.underPaymentTaxAmount = underPaymentTaxAmount;
    }

    @Override
    public void setLastUpdateTime(Date value) {

    }

    @Override
    public void setUpdater(EdsUser user) {

    }

    @Override
    public void setCreationTime(Date value) {
        this.creationDate = value;
    }

    @Override
    public void setCreator(EdsUser value) {

    }

    public Integer getPaymentRefundID() {
        return this.paymentRefundID;
    }

    public void setPaymentRefundID(final Integer paymentRefundID) {
        this.paymentRefundID = paymentRefundID;
    }


    public Integer getExpenseID() {
        return this.expenseID;
    }

    public void setExpenseID(final Integer expenseID) {
        this.expenseID = expenseID;
    }

    public EdsPrepaymentCustomFields getPrepaymentCustomFields() {
        return this.prepaymentCustomFields;
    }

    public void setPrepaymentCustomFields(final EdsPrepaymentCustomFields prepaymentCustomFields) {
        this.prepaymentCustomFields = prepaymentCustomFields;
    }
}
