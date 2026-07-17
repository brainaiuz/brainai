package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsVat;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.customfields.EdsManualJournalItemCustomFields;
import com.edatasite.workforce.gwt.accounting.client.rpc.BillableExpenseItem;
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
 * Date: 02.09.2010
 * Time: 9:55:30
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "manualjournalitem")
public class EdsManualJournalItem extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Type(type = "text")
    private String description;

    @Column(precision = 25, scale = 5)
    private BigDecimal debit;

    @Column(precision = 25, scale = 5)
    private BigDecimal credit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountid")
    private EdsAccount account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taxid")
    private EdsVat tax;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manualjournalid")
    private EdsManualJournal manualJournal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_or_supplier_id")
    private EdsCrmAccount clientOrSupplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeid")
    private EdsEmployee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    private EdsDepartment department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private EdsProject project;

    @ManyToOne(cascade={CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "transactionitemid")
    private EdsTransactionItem transactionItem;

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
    private EdsManualJournalItemCustomFields manualJournalItemCustomFields;

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

    public BigDecimal getDebit() {
        return debit;
    }

    public void setDebit(BigDecimal debit) {
        this.debit = debit;
    }

    public BigDecimal getDebitInBase() {
        if (debit == null) {
            return null;
        }
        return debit.divide(getManualTransfer().getExchangeRate(), 10, BigDecimal.ROUND_HALF_UP);
    }

    public BigDecimal getCredit() {
        return credit;
    }

    public void setCredit(BigDecimal credit) {
        this.credit = credit;
    }

    public BigDecimal getCreditInBase() {
        if (credit == null) {
            return null;
        }
        return credit.divide(getManualTransfer().getExchangeRate(), 10, BigDecimal.ROUND_HALF_UP);
    }

    public EdsAccount getAccount() {
        return account;
    }

    public void setAccount(EdsAccount account) {
        this.account = account;
    }

    public EdsVat getTax() {
        return tax;
    }

    public void setTax(EdsVat tax) {
        this.tax = tax;
    }

    public EdsManualJournal getManualTransfer() {
        return manualJournal;
    }

    public void setManualTransfer(EdsManualJournal manualJournal) {
        this.manualJournal = manualJournal;
    }

    public EdsCrmAccount getClientOrSupplier() {
        return clientOrSupplier;
    }

    public void setClientOrSupplier(EdsCrmAccount clientOrSupplier) {
        this.clientOrSupplier = clientOrSupplier;
    }

    public EdsDepartment getDepartment() {
        return department;
    }

    public void setDepartment(EdsDepartment department) {
        this.department = department;
    }

    public EdsProject getProject() {
        return project;
    }

    public void setProject(EdsProject project) {
        this.project = project;
    }

    public EdsTransactionItem getTransactionItem() {
        return transactionItem;
    }

    public void setTransactionItem(EdsTransactionItem transactionItem) {
        this.transactionItem = transactionItem;
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

    public EdsManualJournalItemCustomFields getManualJournalItemCustomFields() {
        return manualJournalItemCustomFields;
    }

    public void setManualJournalItemCustomFields(EdsManualJournalItemCustomFields manualJournalItemCustomFields) {
        this.manualJournalItemCustomFields = manualJournalItemCustomFields;
    }

    public BillableExpenseItem createBillableExpenseItem(boolean... forEdit) {
        BillableExpenseItem beItem = new BillableExpenseItem();
        beItem.setObjectID(getObjectID());
        beItem.setNumber(manualJournal.getNumber());
        beItem.setType(BillableExpenseItem.MANUAL_TRANSACTION_AS_EXPENSE);
        beItem.setDescription(getDescription());
        beItem.setInvoiceId(manualJournal.getObjectID());

        if (client != null) {
            beItem.setClient(client.getAsSelectItem());
        }
        if (getAccount() != null) {
            beItem.setAccount(getAccount().getAsSelectItem());
        }

        if (credit != null && credit.compareTo(BigDecimal.ZERO) != 0) {
            beItem.setAmountInCurrency(credit.multiply(new BigDecimal(-1)));
        } else {
            beItem.setAmountInCurrency(debit != null ? debit : BigDecimal.ZERO);
        }
        beItem.setAmountInBase(beItem.getAmountInCurrency().divide(manualJournal.getExchangeRate(), ServerUtils.getSystemCalculationScale(), BigDecimal.ROUND_HALF_UP));
        beItem.setCurrencyID(manualJournal.getCurrency() != null ? manualJournal.getCurrency().getObjectID() : null);

        beItem.setMarkupAmount(getMarkupAmount());
        beItem.setMarkupAmountInBase(getMarkupAmount() != null ? getMarkupAmount().divide(manualJournal.getExchangeRate(), ServerUtils.getSystemCalculationScale(), BigDecimal.ROUND_HALF_UP) : null);

        beItem.setDate(manualJournal.getDate());

        if (forEdit != null && forEdit.length > 0) {

            if (getMarkupAccount() != null) {
                beItem.setMarkupAccount(getMarkupAccount().getAsSelectItem());
            }
            if (getMarkupTax() != null) {
                beItem.setMarkupTax(getMarkupTax().createTaxItem());
            }
            beItem.setMarkupTaxAmount(getMarkupTaxAmount());
            beItem.setMarkupTaxAmountInBase(getMarkupTaxAmount() != null ? getMarkupTaxAmount().divide(manualJournal.getExchangeRate(), ServerUtils.getSystemCalculationScale(), BigDecimal.ROUND_HALF_UP) : null);
            beItem.setSelected(forEdit[0]);
        }

        return beItem;
    }
}
