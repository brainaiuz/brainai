package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsVat;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.customfields.EdsBankTransferItemCustomFields;
import com.edatasite.workforce.gwt.accounting.client.rpc.BillableExpenseItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransactionItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
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

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 27.08.2010
 * Time: 16:34:51
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "spendreceivemoneyitem")
public class EdsBankTransferItem extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Type(type = "text")
    private String description;
    private String reference;
    @Column(precision = 25, scale = 5)
    private BigDecimal quantity;
    @Column(precision = 25, scale = 5)
    private BigDecimal unitPrice;
    @Column(precision = 25, scale = 5)
    private BigDecimal amount;
    @Column(precision = 25, scale = 5)
    private BigDecimal debit;
    @Column(precision = 25, scale = 5)
    private BigDecimal credit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountid")
    private EdsAccount account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departmentid")
    private EdsDepartment department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taxid")
    private EdsVat tax;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "banktransferid")
    private EdsBankTransfer bankTransfer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_or_supplier_id")
    private EdsCrmAccount clientOrSupplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeid")
    private EdsEmployee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "projectid")
    private EdsProject project;

    @Column(name = "taxAmount", precision = 25, scale = 5)
    private BigDecimal taxAmount;

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

    @OneToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private EdsBankTransferItemCustomFields bankTransferItemCustomFields;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public void setQuantity(BigDecimal quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getAmountInBase() {
        return amount.divide(getMoneyTransfer().getExchangeRate(), ServerUtils.getSystemCalculationScale(), BigDecimal.ROUND_HALF_UP);
    }

    public EdsAccount getAccount() {
        return account;
    }

    public void setAccount(EdsAccount account) {
        this.account = account;
    }

    public EdsDepartment getDepartment() {
        return department;
    }

    public void setDepartment(EdsDepartment department) {
        this.department = department;
    }

    public EdsVat getTax() {
        return tax;
    }

    public void setTax(EdsVat tax) {
        this.tax = tax;
    }

    public EdsBankTransfer getMoneyTransfer() {
        return bankTransfer;
    }

    public void setMoneyTransfer(EdsBankTransfer bankTransfer) {
        this.bankTransfer = bankTransfer;
    }

    public EdsCrmAccount getClientOrSupplier() {
        return clientOrSupplier;
    }

    public void setClientOrSupplier(EdsCrmAccount clientOrSupplier) {
        this.clientOrSupplier = clientOrSupplier;
    }

    public EdsProject getProject() {
        return project;
    }

    public void setProject(EdsProject project) {
        this.project = project;
    }

    public BigDecimal getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(BigDecimal taxAmount) {
        this.taxAmount = taxAmount;
    }

    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public EdsEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(EdsEmployee employee) {
        this.employee = employee;
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

    public EdsBankTransferItemCustomFields getBankTransferItemCustomFields() {
        return bankTransferItemCustomFields;
    }

    public void setBankTransferItemCustomFields(EdsBankTransferItemCustomFields bankTransferItemCustomFields) {
        this.bankTransferItemCustomFields = bankTransferItemCustomFields;
    }

    public NewManualTransactionItem createTransferItem() {
        NewManualTransactionItem itemData = new NewManualTransactionItem();
        itemData.setObjectId(getObjectID());
        itemData.setAccountItem(account.createAccountItem());
        if (tax != null) {
            itemData.setTaxItem(tax.createTaxItem());
        }
        itemData.setDebit(getDebit());
        itemData.setCredit(getCredit());
        itemData.setDescription(description);
        itemData.setReference(reference);
        itemData.setQuantity(quantity);
        itemData.setUnitPrice(unitPrice);
        if (amount != null) {
            itemData.setAmount(amount);
        }
        itemData.setTaxAmount(taxAmount);
        if (getDepartment() != null) {
            itemData.setDepartment(getDepartment().getAsSelectItem());
        }
        if (getClientOrSupplier() != null) {
            itemData.setCustomerOrSupplier(getClientOrSupplier().getAsSelectItem());
        }
        if (getEmployee() != null) {
            itemData.setEmployee(getEmployee().getAsSelectItem());
        }
        if (getProject() != null) {
            itemData.setProject(new SelectItem(getProject().getObjectID(),
                                               getProject().getNumber() + " -> " + getProject().getName()));
            if (getProject().getParent() != null) {
                itemData.setParentProject(new SelectItem(getProject().getParent().getObjectID(),
                                                         getProject().getParent().getNumber() +
                                                         " -> " +
                                                         getProject().getParent().getName()));
            }
        }
        if (getClient() != null) {
            itemData.setClient(getClient().getAsSelectItem());
        }
        final EdsInvoice invoice = this.getInvoice();

        if (invoice != null && !invoice.isDeleted()) {
            itemData.setInvoiceId(invoice.getObjectID());
        }
        return itemData;
    }

    public BillableExpenseItem createBillableExpenseItem(boolean...forEdit) {
        BillableExpenseItem beItem = new BillableExpenseItem();
        beItem.setObjectID(getObjectID());
        beItem.setNumber(bankTransfer.getNumber());
        beItem.setType(BillableExpenseItem.BANK_TRANSFER_AS_EXPENSE);
        beItem.setDescription(getDescription());
        beItem.setInvoiceId(bankTransfer.getObjectID());
        beItem.setBankTransferType(bankTransfer.getTransferType());

        if (client != null) {
            beItem.setClient(client.getAsSelectItem());
        }
        if (getAccount() != null) {
            beItem.setAccount(getAccount().getAsSelectItem());
        }

        BigDecimal total = getAmount() != null ? getAmount() : BigDecimal.ZERO;
        total = total.add(getTaxAmount() != null ? getTaxAmount() : BigDecimal.ZERO);

        beItem.setAmountInCurrency(total);
        beItem.setAmountInBase(total.divide(bankTransfer.getExchangeRate(), ServerUtils.getSystemCalculationScale(), BigDecimal.ROUND_HALF_UP));
        beItem.setCurrencyID(bankTransfer.getCurrency() != null ? bankTransfer.getCurrency().getObjectID() : null);

        beItem.setMarkupAmount(getMarkupAmount());
        beItem.setMarkupAmountInBase(getMarkupAmount() != null ? getMarkupAmount().divide(bankTransfer.getExchangeRate(), ServerUtils.getSystemCalculationScale(), BigDecimal.ROUND_HALF_UP) : null);

        beItem.setDate(bankTransfer.getDate());

        if (forEdit != null && forEdit.length > 0) {

            if (getMarkupAccount() != null) {
                beItem.setMarkupAccount(getMarkupAccount().getAsSelectItem());
            }
            if (getMarkupTax() != null) {
                beItem.setMarkupTax(getMarkupTax().createTaxItem());
            }
            beItem.setMarkupTaxAmount(getMarkupTaxAmount());
            beItem.setMarkupTaxAmountInBase(getMarkupTaxAmount() != null ? getMarkupTaxAmount().divide(bankTransfer.getExchangeRate(), ServerUtils.getSystemCalculationScale(), BigDecimal.ROUND_HALF_UP) : null);
            beItem.setSelected(forEdit[0]);
        }
        return beItem;
    }
}
