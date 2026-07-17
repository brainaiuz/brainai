package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsExpense;
import com.edatasite.workforce.core.domain.EdsExpensePayment;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsVat;
import com.edatasite.workforce.core.domain.customfields.EdsAccountCustomFields;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountItemWithBudgetDate;
import com.edatasite.workforce.gwt.core.client.rpc.FromToDate;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrChartOfAccountRepresenter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrEmployeeRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.SolrUtils;
import com.edatasite.workforce.gwt.core.server.domain.ObjectHistory;
import org.apache.solr.common.SolrInputDocument;
import org.hibernate.annotations.Where;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.Column;
import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 16.02.2009
 * Time: 18:25:43
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "account")
@Inheritance(strategy = InheritanceType.JOINED)
public class EdsAccount extends EdsObject implements ObjectHistory {

    public static final int ACCOUNTS_RECEIVABLE = 100;
    public static final int ACCOUNTS_PAYABLE = 2100;
    public static final int UNPAID_EXPENSE_CLAIMS = 2108;
    public static final int HISTORICAL_ADJUSTMENT = 2240;
    public static final int EXCHANGE_VARIANCE = 7906;
    public static final int OPENING_BALANCE = 3201;
    public static final int PENDING_GOODS_RECEIVED_NOTES = 2500;
    public static final int PENDING_GOODS_DELIVERED_NOTES = 1249;
    public static final int DEPRECIATION_EXPENSE = 8000;
    public static final int ACCUMULATED_DEPRECIATION = 1050;
    public static final int LANDED_COST = 1100;
    public static final int PREPAYMENT = 103;
    public static final int RETAINED_EARNINGS = 3020;
    public static final int DISTRIBUTION_CARRIAGE = 4905;
    public static final int GAIN_ON_DISPOSAL = 4997;
    public static final int LOSS_ON_DISPOSAL = 9997;
    public static final int CARGO_EXPENSE = 6499;
    public static final int VAT_PAYABLE = 2202;
    public static final int VAT_OUTPUT = 2398;
    public static final int VAT_INPUT = 1248;
    public static final int SALES_DISCOUNT = 4010;
    public static final int PURCHASE_DISCOUNT = 5010;
    public static final int BANK = 999999;
    public static final int INCOME_TAX_PAYABLE = 2302;
    public static final int SOCIAL_SECURITY_COSTS = 9995;
    public static final int SALARIES_PAYABLE = 9996;
    public static final int EMPLOYERS_PENSIONS = 7007;
    public static final int PENSION_FUND = 2230;
    public static final byte DEBIT = 1;
    public static final byte CREDIT = -1;
    public static final int UNEARNED_REVENUE = 2021;
    public static final int PREPAID_EXPANSES = 1011;
    public static final int DEFERRED_REVENUE = 2150;
    public static final int DEFERRED_EXPENSE = 1180;
    private static final Logger log = LoggerFactory.getLogger(EdsAccount.class);
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;
    private Integer key;
    private Integer groupKey;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountTypeId", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private EdsAccountType accountType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taxid")
    private EdsVat tax;
    private String codeString;
    private String accountCode;
    private String name;
    private Boolean showInExpense = false;
    private Boolean enablePayments = false;
    @Column(name = "description", length = 10000)
    private String description;
    @Column(name = "balance", precision = 25, scale = 5)
    private BigDecimal balance = BigDecimal.ZERO;
    @Column(name = "foreignbalance", precision = 25, scale = 5)
    private BigDecimal foreignBalance = BigDecimal.ZERO;
    @Column(name = "quickbook_account_id")
    private String qbAccountID;
    @Column(name = "saasu_guid")
    private String saasuGUID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creatorid")
    private EdsUser creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "updaterId")
    private EdsUser updater;

    @Column(name = "creationTime")
    private Date creationTime;
    @Column(name = "lastUpdatedDate")
    private Date lastUpdatedDate;
    @Column(name = "sasuuLastUpdatedTime")
    private Date sasuuLastUpdatedTime;
    @Column(name = "saasuLastUpdatedUid")
    private String saasuLastUpdatedUid;
    @Column(name = "deleted")
    private Boolean deleted = false;
    @Column(name = "active")
    private Boolean active = true;
    private Boolean balanceCalculated = false;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parentid")
    private EdsAccount parent;
    private Boolean isDefaultAccount = false;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "parent")
    @Where(clause = "deleted = 'false' or deleted is null")
    private List<EdsAccount> childList;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "baseaccountid")
    private EdsAccount baseAccount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currencyid", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private EdsCurrency currency;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountid")
    private List<EdsTransactionItem> itemsInAccount = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private List<EdsInvoiceItem> invoiceItemsInAccount = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId")
    private List<EdsExpense> expenseItemsInAccount = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountid")
    private List<EdsConversionBalanceItem> conversionBalanceItemsInAccount = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY)
    @Where(clause = "deleted = 'false' or deleted is null")
    @JoinColumn(name = "accountid")
    private List<EdsItem> productsInAccount = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountid")
    private List<EdsItem> productsWithDeletesInAccount = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "cogs_accountid")
    private List<EdsItem> productsInCogsAccount = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_accountid")
    private List<EdsItem> productsInAssetAccount = new ArrayList<>();
    @OneToOne
    @JoinColumn(name = "customfieldsid")
    private EdsAccountCustomFields customFields;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private List<EdsQuoteItem> quoteItemsInAccount = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId")
    @Where(clause = "deleted = 'false' or deleted is null")
    private List<EdsInvoicePayment> invoicePaymentsInAccount = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId")
    private List<EdsInvoicePayment> invoicePaymentsWithDeletesInAccount = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId")
    @Where(clause = "deleted = 'false' or deleted is null")
    private List<EdsExpensePayment> expensePaymentsInAccount = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountId")
    private List<EdsExpensePayment> expensePaymentsWithDeletesInAccount = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountid")
    private List<EdsAccountBudget> budgetsInAccount = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountid")
    @Where(clause = "deleted = 'false' or deleted is null")
    private List<EdsFixedAsset> assetsInAccount = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountid")
    private List<EdsFixedAsset> assetsWithDeletesInAccount = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "financedbyid")
    @Where(clause = "deleted = 'false' or deleted is null")
    private List<EdsFixedAsset> financedAssetsInAccount = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "financedbyid")
    private List<EdsFixedAsset> financedAssetsWithDeletesInAccount = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "markupAccountId")
    private List<EdsInvoice> invoicesWithBillExp = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountid")
    private List<EdsManualJournalItem> manualJournalItems = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "accountID")
    private List<EdsExpenseReport> expenseReportList = new ArrayList<>();
    private Boolean foreignAccount = false;
    @Column(name = "quickbook_edit_sequence")
    private String quickbookEditSequence;
    @Column(name = "isShow", columnDefinition = "boolean default false")
    private Boolean isShow = false;
    @Transient
    private BigDecimal treeBalance = BigDecimal.ZERO;
    @Transient
    private BigDecimal treeBalanceExport = BigDecimal.ZERO;
    @Transient
    private Boolean isBankAccountActive;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Boolean getBankAccountActive() {
        return isBankAccountActive != null && isBankAccountActive;
    }

    public void setBankAccountActive(Boolean isBankAccountActive) {
        this.isBankAccountActive = isBankAccountActive;
    }

    public EdsAccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(EdsAccountType accountType) {
        this.accountType = accountType;
    }

    public EdsVat getTax() {
        return tax;
    }

    public void setTax(EdsVat tax) {
        this.tax = tax;
    }

    public String getCodeString() {
        return codeString;
    }

    public void setCodeString(String codeString) {
        this.codeString = codeString;
    }

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public TotalDebitCredit getDebitCreditTotal(Date from, Date to) {
        TotalDebitCredit tDC = new TotalDebitCredit();
        tDC.setAccountTypeCode(accountType.getCode());
        for (EdsTransactionItem item : getItemsInAccount()) {
            if (item.getTransaction() != null && !item.getTransaction().isDeleted()) {
                Date journalDate = item.getTransaction().getJournalDate();
                if (journalDate == null) {
                    continue;
                }
                if ((journalDate.after(from) && journalDate.before(to)) || journalDate.getTime() == from.getTime()) {
                    tDC.setContainsTransaction(true);
                    if (item.getCredit() != null) {
                        tDC.credit = tDC.credit.add(item.getCredit());
                    }
                    if (item.getDebit() != null) {
                        tDC.debit = tDC.debit.add(item.getDebit());
                    }
                }
            }
        }
        return tDC;
    }

    public TotalDebitCredit getDebitCreditTotal(Date from, Date to, Map<String, BigDecimal> exchangeRateMap, boolean compCurParentCompCurEquals) {
        TotalDebitCredit tDC = new TotalDebitCredit();
        tDC.setAccountTypeCode(accountType.getCode());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (EdsTransactionItem item : getItemsInAccount()) {
            if (item.getTransaction() != null && !item.getTransaction().isDeleted()) {
                Date journalDate = item.getTransaction().getJournalDate();
                if (journalDate == null) {
                    continue;
                }
                if ((journalDate.after(from) && journalDate.before(to)) || journalDate.getTime() == from.getTime()) {
                    String journalDateString = dateFormat.format(journalDate);
                    if (item.isInterCompanyTransaction()) {
                        if (item.getCredit() != null) {
                            if (!compCurParentCompCurEquals && exchangeRateMap.containsKey(journalDateString) && exchangeRateMap.get(journalDateString) != null && BigDecimal.ZERO.compareTo(exchangeRateMap.get(journalDateString)) != 0) {
                                tDC.interCompanyCredit = tDC.interCompanyCredit.add(item.getCredit().divide(exchangeRateMap.get(journalDateString), 10, RoundingMode.HALF_UP));
                            } else {
                                tDC.interCompanyCredit = tDC.interCompanyCredit.add(item.getCredit());
                            }
                        }
                        if (item.getDebit() != null) {
                            if (!compCurParentCompCurEquals && exchangeRateMap.containsKey(journalDateString) && exchangeRateMap.get(journalDateString) != null && BigDecimal.ZERO.compareTo(exchangeRateMap.get(journalDateString)) != 0) {
                                tDC.interCompanyDebit = tDC.interCompanyDebit.add(item.getDebit().divide(exchangeRateMap.get(journalDateString), 10, RoundingMode.HALF_UP));
                            } else {
                                tDC.interCompanyDebit = tDC.interCompanyDebit.add(item.getDebit());
                            }
                        }
                    } else {
                        if (item.getCredit() != null) {
                            if (!compCurParentCompCurEquals && exchangeRateMap.containsKey(journalDateString) && exchangeRateMap.get(journalDateString) != null && BigDecimal.ZERO.compareTo(exchangeRateMap.get(journalDateString)) != 0) {
                                tDC.credit = tDC.credit.add(item.getCredit().divide(exchangeRateMap.get(journalDateString), 10, RoundingMode.HALF_UP));
                            } else {
                                tDC.credit = tDC.credit.add(item.getCredit());
                            }
                        }
                        if (item.getDebit() != null) {
                            if (!compCurParentCompCurEquals && exchangeRateMap.containsKey(journalDateString) && exchangeRateMap.get(journalDateString) != null && BigDecimal.ZERO.compareTo(exchangeRateMap.get(journalDateString)) != 0) {
                                tDC.debit = tDC.debit.add(item.getDebit().divide(exchangeRateMap.get(journalDateString), 10, RoundingMode.HALF_UP));
                            } else {
                                tDC.debit = tDC.debit.add(item.getDebit());
                            }
                        }
                    }
                }
            }
            log.info("CONSOLIDATION_TRANSACTIONITEMID:{}", item.getObjectID());
        }
        log.info("CONSOLIDATION_DEBIT:{}", tDC.debit);
        log.info("CONSOLIDATION_CREDIT:{}", tDC.credit);
        log.info("CONSOLIDATION__INTERDEBIT:{}", tDC.interCompanyDebit);
        log.info("CONSOLIDATION__INTERCREDIT:{}", tDC.interCompanyCredit);
        return tDC;
    }

    public TotalDebitCredit getDebitCreditTotal(Date to, Map<String, BigDecimal> exchangeRateMap, boolean compCurParentCompCurEquals) {
        TotalDebitCredit tDC = new TotalDebitCredit();
        tDC.setAccountTypeCode(accountType.getCode());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (EdsTransactionItem item : getItemsInAccount()) {
            if (item.getTransaction() != null && !item.getTransaction().isDeleted()) {
                Date journalDate = item.getTransaction().getJournalDate();
                if (journalDate == null) {
                    continue;
                }
                if (journalDate.before(to)) {
                    String journalDateString = dateFormat.format(journalDate);
                    if (item.isInterCompanyTransaction()) {
                        if (item.getCredit() != null) {
                            if (!compCurParentCompCurEquals && exchangeRateMap.containsKey(journalDateString) && exchangeRateMap.get(journalDateString) != null && BigDecimal.ZERO.compareTo(exchangeRateMap.get(journalDateString)) != 0) {
                                tDC.interCompanyCredit = tDC.interCompanyCredit.add(item.getCredit().divide(exchangeRateMap.get(journalDateString), 10, RoundingMode.HALF_UP));
                            } else {
                                tDC.interCompanyCredit = tDC.interCompanyCredit.add(item.getCredit());
                            }
                        }
                        if (item.getDebit() != null) {
                            if (!compCurParentCompCurEquals && exchangeRateMap.containsKey(journalDateString) && exchangeRateMap.get(journalDateString) != null && BigDecimal.ZERO.compareTo(exchangeRateMap.get(journalDateString)) != 0) {
                                tDC.interCompanyDebit = tDC.interCompanyDebit.add(item.getDebit().divide(exchangeRateMap.get(journalDateString), 10, RoundingMode.HALF_UP));
                            } else {
                                tDC.interCompanyDebit = tDC.interCompanyDebit.add(item.getDebit());
                            }
                        }
                    } else {
                        if (item.getCredit() != null) {
                            if (!compCurParentCompCurEquals && exchangeRateMap.containsKey(journalDateString) && exchangeRateMap.get(journalDateString) != null && BigDecimal.ZERO.compareTo(exchangeRateMap.get(journalDateString)) != 0) {
                                tDC.credit = tDC.credit.add(item.getCredit().divide(exchangeRateMap.get(journalDateString), 10, RoundingMode.HALF_UP));
                            } else {
                                tDC.credit = tDC.credit.add(item.getCredit());
                            }
                        }
                        if (item.getDebit() != null) {
                            if (!compCurParentCompCurEquals && exchangeRateMap.containsKey(journalDateString) && exchangeRateMap.get(journalDateString) != null && BigDecimal.ZERO.compareTo(exchangeRateMap.get(journalDateString)) != 0) {
                                tDC.debit = tDC.debit.add(item.getDebit().divide(exchangeRateMap.get(journalDateString), 10, RoundingMode.HALF_UP));
                            } else {
                                tDC.debit = tDC.debit.add(item.getDebit());
                            }
                        }
                    }
                }
            }
        }
        return tDC;
    }

    public TotalDebitCredit getPNLConsolidationDebitCreditTotal(Date from, Date to, Map<String, BigDecimal> exchangeRateMap, boolean compCurParentCompCurEquals) {
        TotalDebitCredit tDC = new TotalDebitCredit();
        tDC.setAccountTypeCode(accountType.getCode());
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (EdsTransactionItem item : getItemsInAccount()) {
            if (item.getTransaction() != null && !item.getTransaction().isDeleted()) {
                Date journalDate = item.getTransaction().getJournalDate();
                if (journalDate == null) {
                    continue;
                }
                if ((journalDate.after(from) && journalDate.before(to)) || journalDate.getTime() == from.getTime()) {
                    tDC.setContainsTransaction(true);
                    String journalDateString = dateFormat.format(journalDate);
                    if (item.isPNLInterCompanyTransaction()) {
                        if (item.getCredit() != null) {
                            if (!compCurParentCompCurEquals && exchangeRateMap.containsKey(journalDateString) && exchangeRateMap.get(journalDateString) != null && BigDecimal.ZERO.compareTo(exchangeRateMap.get(journalDateString)) != 0) {
                                tDC.interCompanyCredit = tDC.interCompanyCredit.add(item.getCredit().divide(exchangeRateMap.get(journalDateString), 10, RoundingMode.HALF_UP));
                            } else {
                                tDC.interCompanyCredit = tDC.interCompanyCredit.add(item.getCredit());
                            }
                        }
                        if (item.getDebit() != null) {
                            if (!compCurParentCompCurEquals && exchangeRateMap.containsKey(journalDateString) && exchangeRateMap.get(journalDateString) != null && BigDecimal.ZERO.compareTo(exchangeRateMap.get(journalDateString)) != 0) {
                                tDC.interCompanyDebit = tDC.interCompanyDebit.add(item.getDebit().divide(exchangeRateMap.get(journalDateString), 10, RoundingMode.HALF_UP));
                            } else {
                                tDC.interCompanyDebit = tDC.interCompanyDebit.add(item.getDebit());
                            }
                        }
                        tDC.setContainsInterCompany(true);
                    } else {
                        if (item.getCredit() != null) {
                            if (!compCurParentCompCurEquals && exchangeRateMap.containsKey(journalDateString) && exchangeRateMap.get(journalDateString) != null && BigDecimal.ZERO.compareTo(exchangeRateMap.get(journalDateString)) != 0) {
                                tDC.credit = tDC.credit.add(item.getCredit().divide(exchangeRateMap.get(journalDateString), 10, RoundingMode.HALF_UP));
                            } else {
                                tDC.credit = tDC.credit.add(item.getCredit());
                            }
                        }
                        if (item.getDebit() != null) {
                            if (!compCurParentCompCurEquals && exchangeRateMap.containsKey(journalDateString) && exchangeRateMap.get(journalDateString) != null && BigDecimal.ZERO.compareTo(exchangeRateMap.get(journalDateString)) != 0) {
                                tDC.debit = tDC.debit.add(item.getDebit().divide(exchangeRateMap.get(journalDateString), 10, RoundingMode.HALF_UP));
                            } else {
                                tDC.debit = tDC.debit.add(item.getDebit());
                            }
                        }
                    }
                }
            }
        }
        return tDC;
    }

    public Integer getKey() {
        return key;
    }

    public void setKey(Integer key) {
        this.key = key;
    }

    public Integer getGroupKey() {
        return groupKey;
    }

    public void setGroupKey(Integer groupKey) {
        this.groupKey = groupKey;
    }

    public List<EdsTransactionItem> getItemsInAccount() {
        return itemsInAccount;
    }

    public void setItemsInAccount(List<EdsTransactionItem> itemsInAccount) {
        this.itemsInAccount = itemsInAccount;
    }

    public boolean getShowInExpense() {
        return showInExpense != null ? showInExpense : false;
    }

    public void setShowInExpense(Boolean showInExpense) {
        this.showInExpense = showInExpense;
    }

    public List<EdsInvoiceItem> getInvoiceItemsInAccount() {
        return invoiceItemsInAccount;
    }

    public void setInvoiceItemsInAccount(List<EdsInvoiceItem> invoiceItemsInAccount) {
        this.invoiceItemsInAccount = invoiceItemsInAccount;
    }

    public List<EdsExpense> getExpenseItemsInAccount() {
        return expenseItemsInAccount;
    }

    public void setExpenseItemsInAccount(List<EdsExpense> expenseItemsInAccount) {
        this.expenseItemsInAccount = expenseItemsInAccount;
    }

    public List<EdsConversionBalanceItem> getConversionBalanceItemsInAccount() {
        return conversionBalanceItemsInAccount;
    }

    public void setConversionBalanceItemsInAccount(List<EdsConversionBalanceItem> conversionBalanceItemsInAccount) {
        this.conversionBalanceItemsInAccount = conversionBalanceItemsInAccount;
    }

    public boolean getEnablePayments() {
        return enablePayments != null && enablePayments;
    }

    public void setEnablePayments(Boolean enablePayments) {
        this.enablePayments = enablePayments;
    }

    public List<EdsItem> getProductsInAccount() {
        return productsInAccount;
    }

    public void setProductsInAccount(List<EdsItem> productsInAccount) {
        this.productsInAccount = productsInAccount;
    }

    public List<EdsItem> getProductsInCogsAccount() {
        return productsInCogsAccount;
    }

    public void setProductsInCogsAccount(List<EdsItem> productsInCogsAccount) {
        this.productsInCogsAccount = productsInCogsAccount;
    }

    public List<EdsItem> getProductsInAssetAccount() {
        return productsInAssetAccount;
    }

    public void setProductsInAssetAccount(List<EdsItem> productsInAssetAccount) {
        this.productsInAssetAccount = productsInAssetAccount;
    }

    public List<EdsQuoteItem> getQuoteItemsInAccount() {
        return quoteItemsInAccount;
    }

    public void setQuoteItemsInAccount(List<EdsQuoteItem> quoteItemsInAccount) {
        this.quoteItemsInAccount = quoteItemsInAccount;
    }

    public List<EdsInvoicePayment> getInvoicePaymentsInAccount() {
        return invoicePaymentsInAccount;
    }

    public void setInvoicePaymentsInAccount(List<EdsInvoicePayment> invoicePaymentsInAccount) {
        this.invoicePaymentsInAccount = invoicePaymentsInAccount;
    }

    public List<EdsExpensePayment> getExpensePaymentsInAccount() {
        return expensePaymentsInAccount;
    }

    public void setExpensePaymentsInAccount(List<EdsExpensePayment> expensePaymentsInAccount) {
        this.expensePaymentsInAccount = expensePaymentsInAccount;
    }

    public List<EdsAccountBudget> getBudgetsInAccount() {
        return budgetsInAccount;
    }

    public void setBudgetsInAccount(List<EdsAccountBudget> budgetsInAccount) {
        this.budgetsInAccount = budgetsInAccount;
    }

    public boolean isUsedInSystem() {
        return isUsedInSystem(false);
    }

    public boolean isUsedInSystem(boolean withoutOpeningBalance) {
        return isUsedInItems(withoutOpeningBalance)
                || !conversionBalanceItemsInAccount.isEmpty()
                || !productsInAccount.isEmpty()
                || !invoicePaymentsInAccount.isEmpty()
                || !expensePaymentsInAccount.isEmpty()
                || !budgetsInAccount.isEmpty()
                || !assetsInAccount.isEmpty()
                || !financedAssetsInAccount.isEmpty();
    }

    private boolean isUsedInItems(boolean withoutOpeningBalance) {

        //check in transaction items
        if (getItemsInAccount() != null && !getItemsInAccount().isEmpty()) {
            for (EdsTransactionItem item : getItemsInAccount()) {
                if (item.getTransaction() != null && !item.getTransaction().isDeleted()
                        && !(withoutOpeningBalance && Constants.BANK_OPENING_BALANCE_TRANSACTION.equals(item.getTransaction().getKeyType()))) {
                    return true;
                }
            }
        }

        //check in quote items
        if (getQuoteItemsInAccount() != null && !getQuoteItemsInAccount().isEmpty()) {
            for (EdsQuoteItem quoteItem : getQuoteItemsInAccount()) {
                if (quoteItem.getQuote() != null && !quoteItem.getQuote().isDeleted()) {
                    return true;
                }
            }
        }

        //check in invoice items
        if (getInvoiceItemsInAccount() != null && !getInvoiceItemsInAccount().isEmpty()) {
            for (EdsInvoiceItem invoiceItem : getInvoiceItemsInAccount()) {
                if (invoiceItem.getInvoice() != null && !invoiceItem.getInvoice().isDeleted()) {
                    return true;
                }
            }
        }

        //check in expense items
        if (getExpenseItemsInAccount() != null && !getExpenseItemsInAccount().isEmpty()) {
            for (EdsExpense expense : getExpenseItemsInAccount()) {
                if (expense.getReport() != null && !expense.getReport().getDeleted()) {
                    return true;
                }
            }
        }
        return false;
    }


    public String getQBAccountID() {
        return qbAccountID;
    }

    public void setQBAccountID(String qbAccountID) {
        this.qbAccountID = qbAccountID;
    }

    public BigDecimal getTotalDebit() {
        BigDecimal totalDebit = BigDecimal.ZERO;
        for (EdsTransactionItem i : itemsInAccount) {
            if (i.getDebit() != null) {
                totalDebit = totalDebit.add(i.getDebit());
            }
        }
        return totalDebit;
    }

    public BigDecimal getTotalCredit() {
        BigDecimal totalCredit = BigDecimal.ZERO;
        for (EdsTransactionItem i : itemsInAccount) {
            if (i.getCredit() != null) {
                totalCredit = totalCredit.add(i.getCredit());
            }
        }
        return totalCredit;
    }


    public BigDecimal getTreeBalance() {
        treeBalance = treeBalance.add(getBalance());
        if (getChildList() != null && !getChildList().isEmpty()) {
            for (EdsAccount subAccount : getChildList()) {
                treeBalance = treeBalance.add(subAccount.getTreeBalance());
            }
        }
        BigDecimal balance = treeBalance;
        treeBalance = BigDecimal.ZERO;
        return balance;
    }

    public void setTreeBalance(BigDecimal treeBalance) {
        this.treeBalance = treeBalance;
    }

    /**
     * this is for export excel/pdf
     *
     * @return tree balance
     */

    public BigDecimal getTreeBalanceExport() {
        treeBalanceExport = treeBalanceExport.add(getBalance());
        if (getChildList() != null && !getChildList().isEmpty()) {
            for (EdsAccount subAccount : getChildList()) {
                treeBalanceExport = treeBalanceExport.add(subAccount.getBalance());
            }
        }
        return treeBalanceExport;
    }

    public BigDecimal getBalance() {
        return balance != null ? balance : BigDecimal.ZERO;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getForeignBalance() {
        return foreignBalance != null ? foreignBalance : BigDecimal.ZERO;
    }

    public void setForeignBalance(BigDecimal foreignBalance) {
        this.foreignBalance = foreignBalance;
    }

    public List<EdsInvoice> getInvoicesWithBillExp() {
        return invoicesWithBillExp;
    }

    public void setInvoicesWithBillExp(List<EdsInvoice> invoicesWithBillExp) {
        this.invoicesWithBillExp = invoicesWithBillExp;
    }

    public List<EdsManualJournalItem> getManualJournalItems() {
        return manualJournalItems;
    }

    public void setManualJournalItems(List<EdsManualJournalItem> manualJournalItems) {
        this.manualJournalItems = manualJournalItems;
    }

    public String getSaasuGUID() {
        return saasuGUID;
    }

    public void setSaasuGUID(String saasuGUID) {
        this.saasuGUID = saasuGUID;
    }

    public Date getLastUpdatedDate() {
        return lastUpdatedDate;
    }

    public void setLastUpdatedDate(Date lastUpdatedDate) {
        this.lastUpdatedDate = lastUpdatedDate;
    }

    public Date getSasuuLastUpdatedTime() {
        return sasuuLastUpdatedTime;
    }

    public void setSasuuLastUpdatedTime(Date sasuuLastUpdatedTime) {
        this.sasuuLastUpdatedTime = sasuuLastUpdatedTime;
    }

    public String getSaasuLastUpdatedUid() {
        return saasuLastUpdatedUid;
    }

    public void setSaasuLastUpdatedUid(String saasuLastUpdatedUid) {
        this.saasuLastUpdatedUid = saasuLastUpdatedUid;
    }

    public Boolean isDeleted() {
        return deleted == null ? Boolean.FALSE : deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public String getQuickbookEditSequence() {
        return quickbookEditSequence;
    }

    public void setQuickbookEditSequence(String quickbookEditSequence) {
        this.quickbookEditSequence = quickbookEditSequence;
    }

    public AccountItem createAccountItem() {
        return new AccountItem(getObjectID(),
                getAccountCode(), getName(),
                getAccountType().getObjectID(),
                getAccountType().getCode(),
                getAccountType().getCategory(),
                getKey() != null ? getKey() : getGroupKey(),
                (getCurrency() != null ? getCurrency().getObjectID() : null),
                (getCurrency() != null ? getCurrency().getName() : null));
    }

    public AccountItemWithBudgetDate createAccountItemWithBudgetDate() {
        return new AccountItemWithBudgetDate(getObjectID(), getAccountCode(), getAccountCode() != null ? getAccountCode() + "->" + getName() : getName(),
                getAccountType().getObjectID(), getAccountType().getCode(),
                getAccountType().getCategory(),
                getKey() != null ? getKey() : getGroupKey(),
                (getCurrency() != null ? getCurrency().getObjectID() : null),
                (getCurrency() != null ? getCurrency().getName() : null));
    }

    public AccountItemWithBudgetDate createPNLAccountItem(FromToDate main, FromToDate[] compareTo) {
        AccountItemWithBudgetDate rowData = new AccountItemWithBudgetDate(getObjectID(),
                getAccountCode(), getName(), getAccountType().getObjectID(), getAccountType().getCode(),
                getAccountType().getCategory(),
                getKey() != null ? getKey() : getGroupKey(),
                (getCurrency() != null ? getCurrency().getObjectID() : null),
                (getCurrency() != null ? getCurrency().getName() : null));
        rowData.setRowCells(AccountItemWithBudgetDate.createAndSetRowCellsData(main, compareTo));
        return rowData;
    }

    public EdsAccount getParent() {
        return parent;
    }

    public void setParent(EdsAccount parent) {
        this.parent = parent;
        setGroupKey(getParent() != null ? getParent().getKey() : null);
    }

    public EdsAccount getAccountRootParent() {
        if (getParent() != null) {
            return getParent().getAccountRootParent();
        }
        return this;
    }

    public boolean getIsDefaultAccount() {
        return isDefaultAccount != null && isDefaultAccount;
    }

    public void setIsDefaultAccount(Boolean isDefaultAccount) {
        this.isDefaultAccount = isDefaultAccount;
    }

    public List<EdsAccount> getChildList() {
        return childList;
    }

    public void setChildList(List<EdsAccount> childList) {
        this.childList = childList;
    }

    public EdsAccount getBaseAccount() {
        return baseAccount;
    }

    public void setBaseAccount(EdsAccount baseAccount) {
        this.baseAccount = baseAccount;
    }

    public EdsCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(EdsCurrency currency) {
        this.currency = currency;
    }

    public boolean isForeignAccount() {
        return foreignAccount != null && foreignAccount;
    }

    public void setForeignAccount(Boolean foreignAccount) {
        this.foreignAccount = foreignAccount;
    }

//    public Boolean isBalanceCalculated() {
//        return balanceCalculated != null ? balanceCalculated : false;
//    }

//    public void setBalanceCalculated(Boolean balanceCalculated) {
//        this.balanceCalculated = balanceCalculated;
//    }

    public boolean isShow() {
        return isShow != null && isShow;
    }

    public void setShow(Boolean isShow) {
        this.isShow = isShow;
    }

    public Integer findGroupKey() {
        if (getParent() != null) {
            return getParent().findGroupKey();
        } else {
            return getKey();
        }
    }

    public EdsAccount createMultiCurrencyAccount(EdsCurrency currency, EdsUser user) {
        EdsAccount account = new EdsAccount();
        account.setName(getName() + " - " + currency.getName());
        account.setAccountType(getAccountType());
        account.setKey(getKey());
        account.setBalance(BigDecimal.ZERO);
        account.setBaseAccount(this);
        account.setForeignAccount(true);
        account.setCurrency(currency);
        account.setShowInExpense(getShowInExpense());
        account.setActive(isActive());
        account.setEnablePayments(getEnablePayments());
        account.setCreationTime(new Date());
        account.setLastUpdatedDate(new Date());
        if (user != null) {
            account.setCreator(user);
            account.setUpdater(user);
        }
        return account;
    }

//    public BigDecimal calculateAccountBalance() {
//        BigDecimal balance = BigDecimal.ZERO;
//        String category = getAccountType() != null ? getAccountType().getCategory() : "";
//        List<EdsTransactionItem> tItems = getItemsInAccount();
//        for (EdsTransactionItem ti : tItems) {
//            if (ti.getTransaction() != null && !ti.getTransaction().isDeleted()) {
//                if (ti.getDebit() != null) {
//                    balance = (EdsAccountType.ASSETS.equals(category) || EdsAccountType.EXPENSES.equals(category)) ? balance.add(ti.getDebit()) : balance.subtract(ti.getDebit());
//                } else if (ti.getCredit() != null) {
//                    balance = (EdsAccountType.LIABILITIES.equals(category) || EdsAccountType.EQUITY.equals(category) || EdsAccountType.REVENUE.equals(category)) ? balance.add(ti.getCredit()) : balance.subtract(ti.getCredit());
//                }
//            }
//        }
//        return balance;
//    }

//    public BigDecimal calculateAccountForeignBalance() {
//        BigDecimal balance = BigDecimal.ZERO;
//        String category = getAccountType() != null ? getAccountType().getCategory() : "";
//        List<EdsTransactionItem> tItems = getItemsInAccount();
//        for (EdsTransactionItem ti : tItems) {
//            if (ti.getTransaction() != null && !ti.getTransaction().isDeleted()) {
//                if (ti.getForeignDebit() != null) {
//                    balance = (EdsAccountType.ASSETS.equals(category) || EdsAccountType.EXPENSES.equals(category)) ? balance.add(ti.getForeignDebit()) : balance.subtract(ti.getForeignDebit());
//                } else if (ti.getForeignCredit() != null) {
//                    balance = (EdsAccountType.LIABILITIES.equals(category) || EdsAccountType.EQUITY.equals(category) || EdsAccountType.REVENUE.equals(category)) ? balance.add(ti.getForeignCredit()) : balance.subtract(ti.getForeignCredit());
//                }
//            }
//        }
//        return balance;
//    }

    public List<Integer> getChildIDs(List<Integer> childIDs) {
        childIDs.add(getObjectID());
        List<EdsAccount> childList = getChildList();
        for (EdsAccount chld : childList) {
            chld.getChildIDs(childIDs);
        }
        return childIDs;
    }

    public SolrInputDocument indexToSolr(Integer companyID) {
        SolrInputDocument doc = new SolrInputDocument();
        String compositID = companyID + "_" + getObjectID();
        doc.addField(SolrChartOfAccountRepresenter.FIELD_COMPOSITE_ID, compositID);
        doc.addField(SolrChartOfAccountRepresenter.FIELD_COMPANY_ID, companyID);
        doc.addField(SolrChartOfAccountRepresenter.FIELD_ACCOUNT_ID, getObjectID());
        doc.addField(SolrChartOfAccountRepresenter.BANK_ACCOUNT_ACTIVE, getBankAccountActive());
        doc.addField(SolrChartOfAccountRepresenter.FIELD_CODE, getAccountCode());
        doc.addField(SolrChartOfAccountRepresenter.FIELD_NAME, getName());
        doc.addField(SolrChartOfAccountRepresenter.FIELD_LAST_UPDATED_DATE, getLastUpdatedDate());
        doc.addField(SolrChartOfAccountRepresenter.FIELD_SHOW_IN_EXPENSE, getShowInExpense());
        doc.addField(SolrChartOfAccountRepresenter.FIELD_ACTIVE, isActive());
        doc.addField(SolrChartOfAccountRepresenter.FIELD_ENABLE_PAYMENTS, getEnablePayments());
        doc.addField(SolrChartOfAccountRepresenter.FIELD_KEY, getKey());

        if (getParent() != null) {
            doc.addField(SolrChartOfAccountRepresenter.FIELD_PARENT_ID, getParent().getObjectID());
            doc.addField(SolrChartOfAccountRepresenter.FIELD_PARENT_NAME, getParent().getName());
            doc.addField(SolrChartOfAccountRepresenter.FIELD_PARENT_ID_NAME, getParent().getObjectID() + SolrEmployeeRepresenter.SPLIT + getParent().getName());
        }

        if (getCurrency() != null) {
            doc.addField(SolrChartOfAccountRepresenter.FIELD_CURRENCY_ID, getCurrency().getObjectID());
            doc.addField(SolrChartOfAccountRepresenter.FIELD_CURRENCY_NAME, getCurrency().getName());
            doc.addField(SolrChartOfAccountRepresenter.FIELD_CURRENCY_ID_NAME, getCurrency().getObjectID() + SolrEmployeeRepresenter.SPLIT + getCurrency().getName());
        }

        if (getAccountType() != null) {
            doc.addField(SolrChartOfAccountRepresenter.FIELD_TYPE_ID, getAccountType().getObjectID());
            doc.addField(SolrChartOfAccountRepresenter.FIELD_TYPE_NAME, getAccountType().getName());
            doc.addField(SolrChartOfAccountRepresenter.FIELD_TYPE_CODE, getAccountType().getCode());
            doc.addField(SolrChartOfAccountRepresenter.FIELD_TYPE_ID_NAME, getAccountType().getObjectID() + SolrEmployeeRepresenter.SPLIT + getAccountType().getName());
            doc.addField(SolrChartOfAccountRepresenter.FIELD_TYPE_CATEGORY, getAccountType().getCategory());
        }

        return doc;
    }

    public AccountItem getSolrRPC() {
        AccountItem accountItem = new AccountItem();

        accountItem.setId(getObjectID());
        accountItem.setCode(getCodeString());
        accountItem.setAccountKey(getKey());
        accountItem.setName(getName());
        accountItem.setActive(isActive());
        accountItem.setLastUpdatedDate(getLastUpdatedDate());
        if (getParent() != null) {
            accountItem.setParent(getParent().getAsSelectItem());
        }
        if (getCurrency() != null) {
            accountItem.setCurrency(getCurrency().getAsSelectItem());
        }
        if (getAccountType() != null) {
            accountItem.setAccountType(new SelectItem(getAccountType().getObjectID(), getAccountType().getName(), getAccountType().getCode(), getAccountType().getCategory()));
        }
        accountItem.setBankAccountActive(getBankAccountActive());

        return accountItem;
    }

    public EdsAccountCustomFields getCustomFields() {
        return customFields;
    }

    public void setCustomFields(EdsAccountCustomFields customFields) {
        this.customFields = customFields;
    }

    @Override
    public void setLastUpdateTime(Date value) {
        this.lastUpdatedDate = value;
    }

    @Override
    public void setUpdater(EdsUser user) {
        this.updater = user;
    }

    @Override
    public void setCreationTime(Date value) {
        this.creationTime = value;
    }

    @Override
    public void setCreator(EdsUser user) {
        this.creator = user;
    }

    public Boolean isActive() {
        return active != null && active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}
