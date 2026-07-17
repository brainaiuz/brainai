package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsVat;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.accounting.client.rpc.BillableExpenseItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
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
import java.util.Optional;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/15/12
 * Time: 5:24 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "bankcheckitem")
public class EdsBankCheckItem extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bankcheckid")
    private EdsBankCheck bankCheck;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountid")
    private EdsAccount account;

    @Column(precision = 25, scale = 5)
    private BigDecimal amount;

    private BigDecimal usedAsPayment;

    @Type(type = "text")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crmaccountid")
    private EdsCrmAccount crmAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectId")
    private EdsProject project;

    @Column(name = "quickbook_check_itemId")
    private String quickbookCheckID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clientId")
    private EdsCrmAccount client;

    @Column(name = "markupAmount", precision = 25, scale = 5)
    private BigDecimal markupAmount;

    @Column(name = "markupTaxAmount", precision = 25, scale = 5)
    private BigDecimal markupTaxAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "markuptaxId")
    private EdsVat markupTax;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "markupAccountId")
    private EdsAccount markupAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoiceId")
    private EdsInvoice invoice;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsBankCheck getBankCheck() {
        return bankCheck;
    }

    public void setBankCheck(EdsBankCheck bankCheck) {
        this.bankCheck = bankCheck;
    }

    public EdsAccount getAccount() {
        return account;
    }

    public void setAccount(EdsAccount account) {
        this.account = account;
    }

    public BigDecimal getAmountInBase() {
        return amount.divide(Optional.ofNullable(getBankCheck().getExchangeRate()).orElse(BigDecimal.ONE), ServerUtils.getSystemCalculationScale(), BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getUsedAsPayment() {
        return usedAsPayment != null ? usedAsPayment : BigDecimal.ZERO;
    }

    public void setUsedAsPayment(BigDecimal usedAsPayment) {
        this.usedAsPayment = usedAsPayment;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EdsCrmAccount getCrmAccount() {
        return crmAccount;
    }

    public void setCrmAccount(EdsCrmAccount crmAccount) {
        this.crmAccount = crmAccount;
    }

    public EdsProject getProject() {
        return project;
    }

    public void setProject(EdsProject project) {
        this.project = project;
    }

    public BigDecimal getBalance() {
        return amount.subtract(usedAsPayment != null ? usedAsPayment : BigDecimal.ZERO);
    }

    public String getQuickbookCheckID() {
        return quickbookCheckID;
    }

    public void setQuickbookCheckID(String quickbookCheckID) {
        this.quickbookCheckID = quickbookCheckID;
    }

    public EdsCrmAccount getClient() {
        return client;
    }

    public void setClient(EdsCrmAccount client) {
        this.client = client;
    }

    public BigDecimal getMarkupAmount() {
        return markupAmount;
    }

    public void setMarkupAmount(BigDecimal markupAmount) {
        this.markupAmount = markupAmount;
    }

    public BigDecimal getMarkupTaxAmount() {
        return markupTaxAmount;
    }

    public void setMarkupTaxAmount(BigDecimal markupTaxAmount) {
        this.markupTaxAmount = markupTaxAmount;
    }

    public EdsVat getMarkupTax() {
        return markupTax;
    }

    public void setMarkupTax(EdsVat markupTax) {
        this.markupTax = markupTax;
    }

    public EdsAccount getMarkupAccount() {
        return markupAccount;
    }

    public void setMarkupAccount(EdsAccount markupAccount) {
        this.markupAccount = markupAccount;
    }

    public EdsInvoice getInvoice() {
        return invoice;
    }

    public void setInvoice(EdsInvoice invoice) {
        this.invoice = invoice;
    }

    public BillableExpenseItem createBillableExpenseItem(boolean... forEdit) {
        BillableExpenseItem beItem = new BillableExpenseItem();
        beItem.setObjectID(getObjectID());
        beItem.setNumber(bankCheck.getNumber());
        beItem.setType(BillableExpenseItem.CHECK_AS_EXPENSE);
        beItem.setDescription(getDescription());
        beItem.setInvoiceId(bankCheck.getObjectID());

        if (client != null) {
            beItem.setClient(client.getAsSelectItem());
        }
        if (getAccount() != null) {
            beItem.setAccount(getAccount().getAsSelectItem());
        }

        beItem.setAmountInCurrency(getAmount());
        beItem.setAmountInBase(getAmount().divide(Optional.ofNullable(bankCheck.getExchangeRate()).orElse(BigDecimal.ONE), ServerUtils.getSystemCalculationScale(), BigDecimal.ROUND_HALF_UP));

        beItem.setMarkupAmount(getMarkupAmount());
        beItem.setMarkupAmountInBase(Optional.ofNullable(getMarkupAmount()).orElse(BigDecimal.ZERO)
                .divide(Optional.ofNullable(bankCheck.getExchangeRate()).orElse(BigDecimal.ONE), ServerUtils.getSystemCalculationScale(), BigDecimal.ROUND_HALF_UP));

        beItem.setDate(bankCheck.getDate());

        if (forEdit != null && forEdit.length > 0) {

            if (getMarkupAccount() != null) {
                beItem.setMarkupAccount(getMarkupAccount().getAsSelectItem());
            }
            if (getMarkupTax() != null) {
                beItem.setMarkupTax(getMarkupTax().createTaxItem());
            }
            beItem.setMarkupTaxAmount(getMarkupTaxAmount());
            beItem.setMarkupTaxAmountInBase(Optional.ofNullable(getMarkupTaxAmount()).orElse(BigDecimal.ZERO)
                    .divide(Optional.ofNullable(bankCheck.getExchangeRate()).orElse(BigDecimal.ONE), ServerUtils.getSystemCalculationScale(), BigDecimal.ROUND_HALF_UP));
            beItem.setSelected(forEdit[0]);
        }

        return beItem;
    }
}
