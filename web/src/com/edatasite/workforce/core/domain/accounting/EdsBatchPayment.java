package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsPaymentMethod;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.customfields.EdsInvoiceCustomFields;
import com.edatasite.workforce.core.domain.pdf.EdsCompanyPdfTemplate;
import com.edatasite.workforce.core.domain.workflow.EdsTraceable;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.server.domain.ObjectHistory;
import com.edatasite.workforce.gwt.invoice.client.rpc.ReceivePaymentData;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Sherzod on 7/4/2015.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "batchPayment")
public class EdsBatchPayment extends EdsTraceable implements ObjectHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    private String number;
    private Integer intNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crmAccountID")
    private EdsCrmAccount crmAccount; //Client or Supplier

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountID")
    private EdsAccount account; //Chart account

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currencyid")
    @ForeignKey(name = "none")
    private EdsCurrency currency;

    @Column(precision = 25, scale = 15)
    private BigDecimal exchangeRate;

    private String reference;
    private Date date;
    private Date lastUpdated;
    private Date creationDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorId")
    private EdsUser creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paymentMethod")
    private EdsPaymentMethod paymentMethod;

    @Column(precision = 20, scale = 4)
    private BigDecimal totalAmount;

    private String type;//RECEIVABLE, PAYABLE
    private String paymentTarget;//INVOICE or MANUAL_JOURNAL

    private Boolean deleted = false;
    private Boolean reversed = false;

    @Type(type = "text")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId")
    @ForeignKey(name = "none")
    private EdsProject project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departmentid")
    @ForeignKey(name = "none")
    private EdsDepartment department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pdfTemplateId")
    @ForeignKey(name = "none")
    private EdsCompanyPdfTemplate pdfTemplate;

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private EdsInvoiceCustomFields customFields;

    @Column(columnDefinition = "boolean default false")
    private Boolean includeSuAccountTransaction = Boolean.FALSE;

    private Integer emailTemplateID;
    private BigDecimal remainingBalance;

    private String changedType;
    private BigDecimal creditAmount;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getIntNumber() {
        return intNumber;
    }

    public void setIntNumber(Integer intNumber) {
        this.intNumber = intNumber;
    }

    public EdsCrmAccount getCrmAccount() {
        return crmAccount;
    }

    public void setCrmAccount(EdsCrmAccount crmAccount) {
        this.crmAccount = crmAccount;
    }

    public EdsAccount getAccount() {
        return account;
    }

    public void setAccount(EdsAccount account) {
        this.account = account;
    }

    public EdsCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(EdsCurrency currency) {
        this.currency = currency;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public EdsPaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(EdsPaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPaymentTarget() {
        return paymentTarget;
    }

    public void setPaymentTarget(String paymentTarget) {
        this.paymentTarget = paymentTarget;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
    public String getChangedType() {
        return changedType;
    }

    public void setChangedType(String changedType) {
        this.changedType = changedType;
        addChange("CHANGED_TYPE");
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public BigDecimal getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(BigDecimal remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public Boolean getReversed() {
        return reversed;
    }

    public void setReversed(Boolean reversed) {
        this.reversed = reversed;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EdsDepartment getDepartment() {
        return department;
    }

    public void setDepartment(EdsDepartment department) {
        this.department = department;
    }

    public EdsCompanyPdfTemplate getPdfTemplate() {
        return pdfTemplate;
    }

    public void setPdfTemplate(EdsCompanyPdfTemplate pdfTemplate) {
        this.pdfTemplate = pdfTemplate;
    }

    public EdsInvoiceCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsInvoiceCustomFields customFields) {
        this.customFields = customFields;
    }

    public EdsProject getProject() {
        return project;
    }

    public void setProject(EdsProject project) {
        this.project = project;
    }

    public Boolean getIncludeSuAccountTransaction() {
        return includeSuAccountTransaction;
    }

    public void setIncludeSuAccountTransaction(Boolean includeSuAccountTransaction) {
        this.includeSuAccountTransaction = includeSuAccountTransaction;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public EdsUser getCreator() {
        return creator;
    }

    public Integer getEmailTemplateID() {
        return emailTemplateID;
    }

    public void setEmailTemplateID(Integer emailTemplateID) {
        this.emailTemplateID = emailTemplateID;
    }

    public ReceivePaymentData getAsRPC() {
        ReceivePaymentData receivePaymentData = new ReceivePaymentData();
        receivePaymentData.setObjectID(getObjectID());
        receivePaymentData.setNumber(getNumber());
        receivePaymentData.setIntNumber(getIntNumber());
        EdsCrmAccount crmAccount = getCrmAccount();
        if (crmAccount != null) {
            receivePaymentData.setCrmAccount(crmAccount.getAsSelectItem());
            if (crmAccount.getBillingAddress() != null) {
                receivePaymentData.setCrmAccountBillAddressId(crmAccount.getBillingAddress(true).getObjectID());
            } else if (crmAccount.getBillingAddresses() != null && crmAccount.getBillingAddresses().size() > 0) {
                receivePaymentData.setCrmAccountBillAddressId(crmAccount.getBillingAddresses().get(0).getObjectID());
            }
        }
        receivePaymentData.setExRate(getExchangeRate());
        receivePaymentData.setCurrency(getCurrency().createCurrencyItem());
        receivePaymentData.setAccount(new SelectItem(getAccount().getObjectID(), getAccount().getName(), getAccount().getAccountCode()));
        receivePaymentData.setReference(getReference());
        receivePaymentData.setType(getType());
        receivePaymentData.setDate(new DateNonConvertable(getDate()));
        if (getPaymentMethod() != null) {
            receivePaymentData.setPaymentMethod(getPaymentMethod().getAsSelectItem());
        }
        receivePaymentData.setPaymentTarget(getPaymentTarget());
        receivePaymentData.setTotalAmount(getTotalAmount());
        receivePaymentData.setDescription(getDescription());
        receivePaymentData.setIncludeSubAccountTransaction(getIncludeSuAccountTransaction());
        receivePaymentData.setReversed(getReversed());

        if (getProject() != null) {
            receivePaymentData.setProject(new SelectItem(getProject().getObjectID(), getProject().getName(), getProject().getNumber()));
        }
        if (getDepartment() != null) {
            receivePaymentData.setDepartment(getDepartment().getAsSelectItem());
        }
        if (getPdfTemplate() != null) {
            receivePaymentData.setPdfTemplateID(getPdfTemplate().getObjectID());
        }
        return receivePaymentData;
    }

    @Override
    public void setLastUpdateTime(Date value) {
        lastUpdated = value;
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
        this.creator = value;
    }
}
