package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsAdjustmentItem;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsVat;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 19.02.2009
 * Time: 13:17:30
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "transactionItem")
public class EdsTransactionItem extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;


    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    @Column(precision = 25, scale = 5)
    private BigDecimal debit;
    @Column(precision = 25, scale = 5)
    private BigDecimal credit;

    //For multicurrency enabled companies only
    @Column(precision = 25, scale = 5)
    private BigDecimal foreignDebit;
    @Column(precision = 25, scale = 5)
    private BigDecimal foreignCredit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transactionid")
    @ForeignKey(name = "none")
    private EdsTransaction transaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountid")
    @ForeignKey(name = "none")
    private EdsAccount account;

    //Manual Journal related
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taxid")
    @ForeignKey(name = "none")
    private EdsVat tax;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crmaccount_id")
    @ForeignKey(name = "none")
    private EdsCrmAccount crmAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id")
    @ForeignKey(name = "none")
    private EdsDepartment department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    @ForeignKey(name = "none")
    private EdsProject project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_adjustment_item_id")
    @ForeignKey(name = "none")
    private EdsAdjustmentItem stockAdjustmentItem;

    //itemId contains {invoice_item_id, order_item_id, mj_item_id, bt_item_id,...}
    private Integer itemId;

    @Column(name = "inventory_id")
    private Integer inventoryId;

    @Column(name = "is_cogs_item", columnDefinition = "boolean default false")
    private boolean isCogsItem;

    //Manual Journal related
    @Type(type = "text")
    private String description;

    private String type;

    private String reconcileStatus;

    public EdsTransaction getTransaction() {
        return transaction;
    }

    public void setTransaction(EdsTransaction transaction) {
        this.transaction = transaction;
    }

    public EdsAccount getAccount() {
        return account;
    }

    public void setAccount(EdsAccount account) {
        this.account = account;
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

    public EdsVat getTax() {
        return tax;
    }

    public void setTax(EdsVat tax) {
        this.tax = tax;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public EdsCrmAccount getCrmAccount() {
        return crmAccount;
    }

    public void setCrmAccount(EdsCrmAccount crmAccount) {
        this.crmAccount = crmAccount;
    }

    public EdsDepartment getDepartment() {
        return department;
    }

    public void setDepartment(EdsDepartment department) {
        this.department = department;
    }

    public BigDecimal getForeignDebit() {
        return foreignDebit;
    }

    public void setForeignDebit(BigDecimal foreignDebit) {
        this.foreignDebit = foreignDebit;
    }

    public BigDecimal getForeignCredit() {
        return foreignCredit;
    }

    public void setForeignCredit(BigDecimal foreignCredit) {
        this.foreignCredit = foreignCredit;
    }

    public EdsProject getProject() {
        return project;
    }

    public void setProject(EdsProject project) {
        this.project = project;
    }

    public EdsAdjustmentItem getStockAdjustmentItem() {
        return stockAdjustmentItem;
    }

    public void setStockAdjustmentItem(EdsAdjustmentItem stockAdjustmentItem) {
        this.stockAdjustmentItem = stockAdjustmentItem;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    public boolean isCogsItem() {
        return isCogsItem;
    }

    public void setCogsItem(boolean cogsItem) {
        isCogsItem = cogsItem;
    }

    public String getReconcileStatus() {
        return reconcileStatus;
    }

    public void setReconcileStatus(String reconcileStatus) {
        this.reconcileStatus = reconcileStatus;
    }

    public BigDecimal getBalance(boolean receivable) {
        if (receivable) {
            return (debit != null ? debit : BigDecimal.ZERO).subtract(credit != null ? credit : BigDecimal.ZERO);
        } else {
            return (credit != null ? credit : BigDecimal.ZERO).subtract(debit != null ? debit : BigDecimal.ZERO);
        }
    }

    public BigDecimal getForeignBalance(boolean receivable) {
        if (receivable) {
            return (foreignDebit != null ? foreignDebit : BigDecimal.ZERO).subtract(foreignCredit != null ? foreignCredit : BigDecimal.ZERO);
        } else {
            return (foreignCredit != null ? foreignCredit : BigDecimal.ZERO).subtract(foreignDebit != null ? foreignDebit : BigDecimal.ZERO);
        }
    }

    public boolean isInterCompanyTransaction() {
        return account.getKey() != null && (account.getKey().equals(EdsAccount.ACCOUNTS_RECEIVABLE) || account.getKey().equals(EdsAccount.ACCOUNTS_PAYABLE))
                && ((transaction.getClient() != null && transaction.getClient().getSubsidiary() != null) || (transaction.getSupplier() != null && transaction.getSupplier().getSubsidiary() != null)
                || (getCrmAccount() != null && getCrmAccount().getSubsidiary() != null));
    }

    public boolean isPNLInterCompanyTransaction() {
        return (transaction.getClient() != null && transaction.getClient().getSubsidiary() != null) || (transaction.getSupplier() != null && transaction.getSupplier().getSubsidiary() != null)
                || (getCrmAccount() != null && getCrmAccount().getSubsidiary() != null);
    }
}
