package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;

import javax.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 18.02.2009
 * Time: 12:51:55
 * To change this template use File | Settings | File Templates.
 */
@Entity
@SequenceGenerator(name = "SEQ_STORE", schema = EdsScope.PRIVATE_SCHEMA, sequenceName = "transaction_id_man_seq", allocationSize = 1)
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "transaction")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class EdsTransaction extends EdsObject implements Constants {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_STORE")
    @Column(name = "id")
    private Integer objectID;

    //The id of the transaction in company
    private Integer journalId;

    private String name;

    private Date journalDate;
    private Date postedDate;

    private String reference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postedBy")
    private EdsUser postedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reversalid")
    private EdsTransaction reversalTransaction;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clientid")
    private EdsCrmAccount client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplierid")
    private EdsCrmAccount supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bankstatementitemid")
    private EdsBankStatementItem bankStatementItem;

    private Integer currencyID;

    @Column(precision = 25, scale = 15)
    private BigDecimal exchangeRate;

    @Column(name = "filed_vat_id")
    private Integer filedVatId;

    @Column(name = "deleted")
    private Boolean deleted = false;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "transactionid")
    private Set<EdsTransactionItem> transactionItems = new HashSet<>();

    public Set<EdsTransactionItem> getTransactionItems() {
        return transactionItems;
    }

    public void setTransactionItems(Set<EdsTransactionItem> transactionItems) {
        this.transactionItems = transactionItems;
    }

    public void addTransactionItem(EdsTransactionItem transactionItem) {
        transactionItem.setTransaction(this);
        transactionItems.add(transactionItem);
    }

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "transactionid")
    private Set<EdsItemStock> itemStocks = new HashSet<>();

    @Transient
    private HashMap<Integer, Integer> stockProductIdentifierList = new HashMap<>();

    public Set<EdsItemStock> getItemStocks() {
        return itemStocks;
    }

    public void setItemStocks(Set<EdsItemStock> itemStocks) {
        this.itemStocks = itemStocks;
    }

    public void addItemStock(EdsItemStock itemStock) {
        itemStock.setTransaction(this);
        itemStocks.add(itemStock);
    }

    public Date getJournalDate() {
        return journalDate;
    }

    public void setJournalDate(Date journalDate) {
        this.journalDate = journalDate;
    }

    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EdsUser getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(EdsUser postedBy) {
        this.postedBy = postedBy;
    }

    public Integer getJournalId() {
        return journalId;
    }

    public void setJournalId(Integer journalId) {
        this.journalId = journalId;
    }

    public EdsCompany getCompany() {
        return getPostedBy().getCompany();
    }

    public EdsTransaction getReversalTransaction() {
        return reversalTransaction;
    }

    public void setReversalTransaction(EdsTransaction reversalTransaction) {
        this.reversalTransaction = reversalTransaction;
    }

    public EdsCrmAccount getClient() {
        return client;
    }

    public void setClient(EdsCrmAccount client) {
        this.client = client;
    }

    public EdsCrmAccount getSupplier() {
        return supplier;
    }

    public void setSupplier(EdsCrmAccount supplier) {
        this.supplier = supplier;
    }

    @Transient
    public Integer getKeyId() {
        return null;
    }

    @Transient
    public String getKeyType() {
        return null;
    }

    public String getStatus() {
//           return status;
        return null;
    }

    public EdsBankStatementItem getBankStatementItem() {
        return bankStatementItem;
    }

    public void setBankStatementItem(EdsBankStatementItem bankStatementItem) {
        this.bankStatementItem = bankStatementItem;
    }

    public Integer getCurrencyID() {
        return currencyID;
    }

    public void setCurrencyID(Integer currencyID) {
        this.currencyID = currencyID;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }

    public Integer getFiledVatId() {
        return filedVatId;
    }

    public void setFiledVatId(Integer filedVatId) {
        this.filedVatId = filedVatId;
    }

    public Boolean isDeleted() {
        return deleted == null ? Boolean.FALSE : deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public HashMap<Integer, Integer> getStockProductIdentifierList() {
        return stockProductIdentifierList;
    }

    public Integer getStockProductIdentifier(Integer productID) {
        if (productID != null && getStockProductIdentifierList().containsKey(productID)) {
            return getStockProductIdentifierList().get(productID);
        }
        return 1;
    }

    @Deprecated
    //If you build transaction item list from scrach PLEASE DO NOT USE this method,
    //because the method uses getTransactionItems(), so this means it gets deleted transaction items
    //which is you deleted those item in the current @Transaction. Therefore BE CAREFUL use this one
    public BigDecimal getDebitCreditDiff(String type) {
        BigDecimal debitTotal = AccountingConstants.ZERO, creditTotal = AccountingConstants.ZERO;
        for (EdsTransactionItem i : getTransactionItems()) {
            if (i.getDebit() != null) {
                debitTotal = debitTotal.add(i.getDebit());
            }
            if (i.getCredit() != null) {
                creditTotal = creditTotal.add(i.getCredit());
            }
        }
        if (RECEIVABLE.equals(type)) {
            return debitTotal.subtract(creditTotal);
        } else if (PAYABLE.equals(type)) {
            return creditTotal.subtract(debitTotal);
        } else {
            return AccountingConstants.ZERO;
        }
    }

    public BigDecimal getDebitCreditDiff(String type, List<EdsTransactionItem> items) {
        BigDecimal debitTotal = AccountingConstants.ZERO, creditTotal = AccountingConstants.ZERO;
        for (EdsTransactionItem i : items) {
            if (i.getDebit() != null) {
                debitTotal = debitTotal.add(i.getDebit().setScale(5, RoundingMode.HALF_UP));
            }
            if (i.getCredit() != null) {
                creditTotal = creditTotal.add(i.getCredit().setScale(5, RoundingMode.HALF_UP));
            }
        }
        if (RECEIVABLE.equals(type)) {
            return debitTotal.subtract(creditTotal);
        } else if (PAYABLE.equals(type)) {
            return creditTotal.subtract(debitTotal);
        } else {
            return AccountingConstants.ZERO;
        }
    }
}
