package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsExpensePayment;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsStockAdjustment;
import com.edatasite.workforce.core.domain.EdsStockTransfer;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsAccountType;
import com.edatasite.workforce.core.domain.accounting.EdsBankCheck;
import com.edatasite.workforce.core.domain.accounting.EdsBankCheckTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsBankTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsBankTransfer;
import com.edatasite.workforce.core.domain.accounting.EdsBankTransferTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsBatchPayment;
import com.edatasite.workforce.core.domain.accounting.EdsBillOfEntryTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsCusSuppPaymentTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsCustomerTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsDeferredTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsDepreciation;
import com.edatasite.workforce.core.domain.accounting.EdsDepreciationTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsDisposalTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsExpensePaymentTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsExpenseTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsFixedAssetTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsGoodsDeliveredTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsGoodsReceivedTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsIncomeTaxTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsInventoryTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsInvoicePayment;
import com.edatasite.workforce.core.domain.accounting.EdsInvoicePaymentTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsInvoiceTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsManualJournal;
import com.edatasite.workforce.core.domain.accounting.EdsManualTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsOverPayment;
import com.edatasite.workforce.core.domain.accounting.EdsOverPaymentTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsPaymentRefund;
import com.edatasite.workforce.core.domain.accounting.EdsPaymentRefundTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseOrder;
import com.edatasite.workforce.core.domain.accounting.EdsShippingData;
import com.edatasite.workforce.core.domain.accounting.EdsStockAdjustmentTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsStockTransferTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsSupplierTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsTransactionItem;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.enums.DeferredTransactionType;
import com.edatasite.workforce.core.domain.payrolluk.EdsCashAdvance;
import com.edatasite.workforce.core.domain.payrolluk.EdsCashAdvancePayTransaction;
import com.edatasite.workforce.core.domain.payrolluk.EdsCashAdvanceTransaction;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayrollPaymentTransaction;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayrunPaymentTransaction;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipPayments;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTableItem;
import com.edatasite.workforce.core.domain.payrolluk.EdsPayslipTransaction;
import com.edatasite.workforce.core.domain.payrolluk.EdsSinglePayrunTransaction;
import com.edatasite.workforce.gwt.accounting.client.rpc.FindMatchFilterData;
import com.edatasite.workforce.gwt.accounting.client.rpc.ListingResult;
import com.edatasite.workforce.gwt.accounting.client.rpc.Transaction;
import com.edatasite.workforce.gwt.accounting.client.rpc.TransactionItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.client.client.rpc.CrmAccountBalanceItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.ExpensePaymentManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.InvoicePaymentManager;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.OverPaymentManager;
import com.edatasite.workforce.gwt.core.server.db.TransactionManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.BatchPaymentManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.PayslipTableItemManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.common.collect.Lists;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 21.02.2009
 * Time: 15:18:44
 * To change this template use File | Settings | File Templates.
 */
@SuppressWarnings({"unchecked"})
@Repository("accountTransactionManager")
public class TransactionManagerImpl extends BaseManager<EdsTransaction> implements TransactionManager, Constants, AccountingConstants {

    @Autowired
    private JdbcSpringManager jdbcSpringManager;

    @Autowired
    private AccountingManager accountingManager;

    @Autowired
    private CrmAccountManager crmAccountManager;

    @Autowired
    private BatchPaymentManager batchPaymentManager;

    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private InvoicePaymentManager invoicePaymentManager;
    @Autowired
    private ExpensePaymentManager expensePaymentManager;
    @Autowired
    private OverPaymentManager overPaymentManager;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private PayslipTableItemManager payslipTableItemManager;

    public TransactionManagerImpl() {
        super(EdsTransaction.class);
    }

    @Override
    public void create(EdsTransaction transaction) {
        super.create(transaction);
        setChangedAccountsForRecalculate(transaction);
    }

    @Override
    public void update(EdsTransaction transaction) {
        super.update(transaction);
        setChangedAccountsForRecalculate(transaction);
    }

    public ListingResult<Transaction> listingResult(Date from, Date to, String orderBy, Integer journalId, String departmentAndTreeChildIDs, ListingFilterParameter listingFilterParameter) {
        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        Integer calculationScale = fs.getAccountingCalculationScale();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd_MM_yyyy");
        String transactionLink;
        boolean isBlank;
        String order = "";

        if (orderBy.equals(JOURNAL_ID)) {
            order = "t.journalId";
        } else if (orderBy.equals(JOURNAL_DATE)) {
            order = "t.journalDate";
        }
        List<EdsTransaction> transactionsList;
        Long count;
        if (listingFilterParameter.getObjectId() != null && "REFUND".equals(listingFilterParameter.getFormtype())) {
            List<Integer> journalIds = Lists.newArrayList();

            List<EdsInvoicePayment> paymentIds = invoicePaymentManager.getRefundItems(listingFilterParameter.getObjectId());
            for (EdsInvoicePayment payment : paymentIds) {
                EdsInvoicePaymentTransaction transaction = getTransactionByPayment(payment);
                if (transaction != null) {
                    journalIds.add(transaction.getJournalId());
                }
            }
            StringBuilder sql = new StringBuilder("select t from EdsTransaction t ");
            sql.append(" where t.deleted <>true and t.journalId in (" + ServerUtils.getAsCommoDelimited(journalIds, "0", ",") + ") ");

            transactionsList = find(sql.toString());
            count = Long.valueOf(transactionsList.size());

        } else if (listingFilterParameter.getObjectId() != null && "GROUP_PAYRUN".equals(listingFilterParameter.getFormtype())) {
            List<Integer> journalIds = Lists.newArrayList();

            List<EdsPayslipTableItem> tableItems = payslipTableItemManager.getPayslipTableItemsByTableID(listingFilterParameter.getObjectId());
            for (EdsPayslipTableItem item : tableItems) {
                EdsSinglePayrunTransaction transaction = getTransactionByPayrun(item.getObjectID());
                if (transaction != null) {
                    journalIds.add(transaction.getJournalId());
                }
            }
            StringBuilder sql = new StringBuilder("select t from EdsTransaction t ");
            sql.append(" where t.deleted <>true and t.journalId in (" + ServerUtils.getAsCommoDelimited(journalIds, "0", ",") + ") ");

            transactionsList = find(sql.toString());
            count = Long.valueOf(transactionsList.size());
        } else if (listingFilterParameter.getObjectId() != null) {
            List<Integer> journalIds = Lists.newArrayList();
            EdsOverPayment edsOverPayment = overPaymentManager.getOverPaymentByBatchPayment(listingFilterParameter.getObjectId());

            List<EdsInvoicePayment> paymentIds = invoicePaymentManager.getBatchPaymentItems(listingFilterParameter.getObjectId());
            for (EdsInvoicePayment payment : paymentIds) {
                EdsInvoicePaymentTransaction transaction = getTransactionByPayment(payment);
                if (transaction != null) {
                    journalIds.add(transaction.getJournalId());
                }
            }
            List<EdsExpensePayment> expensePayments = expensePaymentManager.findAllByBatchPaymentId(listingFilterParameter.getObjectId());
            for (EdsExpensePayment edsExpensePayment : expensePayments) {
                EdsExpensePaymentTransaction transaction = getTransactionByExpensePayment(edsExpensePayment);
                if (transaction != null) {
                    journalIds.add(transaction.getJournalId());
                }
            }
            StringBuilder sql = new StringBuilder("select t from EdsTransaction t ");
            sql.append(" where t.journalId in (" + ServerUtils.getAsCommoDelimited(journalIds, "0", ",") + ") ");
            if (edsOverPayment != null) {
                sql.append(" or t.overPayment.objectID= " + edsOverPayment.getObjectID() + " ");
            }
            transactionsList = find(sql.toString());
            count = Long.valueOf(transactionsList.size());

        } else if (journalId != null) {
            transactionsList = findNative("select trt.* from " + getCompanyId() + ".transaction trt left join " + getCompanyId() + ".transaction t on trt.reversalid=t.id where trt.journalid = " + journalId + " or t.journalid = " + journalId + " order by " + order, EdsTransaction.class);
            count = Long.valueOf(transactionsList.size());
        } else if (listingFilterParameter != null && listingFilterParameter.getDepartmentId() != null) {
            transactionsList = findInterval("select distinct t from EdsTransaction t left join fetch t.transactionItems ti where  "
                            + ServerUtils.checkForDeleted("t.deleted") + " and ti.department.objectID in (" + departmentAndTreeChildIDs + ") and t.journalDate between ? and ? order by " + order + " desc",
                    listingFilterParameter.getStart(), listingFilterParameter.getLimit(), from, to
            );
            count = (Long) findSingle("select COUNT(DISTINCT t.id) from EdsTransaction t left join t.transactionItems ti where "
                    + ServerUtils.checkForDeleted("t.deleted") + " and ti.department.objectID in (" + departmentAndTreeChildIDs + ") and t.journalDate between ? and ?", from, to);
        } else if (listingFilterParameter != null && listingFilterParameter.getAccountID() != null) {
            transactionsList = findInterval("select distinct t from EdsTransaction t left join t.transactionItems ti where  "
                            + ServerUtils.checkForDeleted("t.deleted") + " and ti.account.objectID = " + listingFilterParameter.getAccountID() + " and t.journalDate between ? and ? order by " + order + " desc",
                    listingFilterParameter.getStart(), listingFilterParameter.getLimit(), from, to
            );
            count = (Long) findSingle("select COUNT(DISTINCT t.id) from EdsTransaction t left join t.transactionItems ti where "
                    + ServerUtils.checkForDeleted("t.deleted") + " and ti.account.objectID =" + listingFilterParameter.getAccountID() + " and t.journalDate between ? and ?", from, to);
        } else {
            transactionsList = findInterval("select distinct t from EdsTransaction t left join  t.transactionItems ti left join ti.department where  "
                            + ServerUtils.checkForDeleted("t.deleted") + " and t.journalDate between ? and ? order by " + order + " desc",
                    listingFilterParameter.getStart(), listingFilterParameter.getLimit(), from, to
            );
            count = (Long) findSingle("select COUNT(DISTINCT t) from EdsTransaction t where "
                    + ServerUtils.checkForDeleted("t.deleted") + " and t.journalDate between ? and ?", from, to);
        }

        ListingResult<Transaction> result = new ListingResult<>();
        if (count != null) {
            result.setTotal(count.intValue());
        }
        Transaction[] transactionsArray = new Transaction[transactionsList.size()];
        int i = 0;
        for (EdsTransaction transaction : transactionsList) {
            isBlank = false;
            Transaction trans = transactionsArray[i] = new Transaction();
            trans.setJournalId(transaction.getJournalId());
            trans.setJournalDate(transaction.getJournalDate() != null
                    ? new DateNonConvertable(transaction.getJournalDate())
                    : null);
            trans.setPostedDate(transaction.getPostedDate() != null
                    ? new DateNonConvertable(transaction.getPostedDate())
                    : null);
            trans.setJournalName(transaction.getName());
            trans.setPostedBy(transaction.getPostedBy() != null ? transaction.getPostedBy().getName() : null);
            trans.setTransactionType(transaction.getKeyType());
            if (transaction.getReversalTransaction() != null) {
                trans.setReversedJournalId(transaction.getReversalTransaction().getJournalId());
            }
            Set<EdsTransactionItem> items = transaction.getTransactionItems();
            TransactionItem[] tItems = new TransactionItem[items.size()];
            int j = 0;
            BigDecimal totalCredit = ZERO;
            BigDecimal totalDebit = ZERO;
            for (EdsTransactionItem item : items) {
                TransactionItem tItem = tItems[j] = new TransactionItem();
                if (item.getAccount() != null) {
                    tItem.setAccountCode(item.getAccount().getAccountCode()/*getCode()*/);
                    tItem.setAccountName(item.getAccount().getName());
                }
                if (item.getCredit() != null) {
                    BigDecimal taxAmount = item.getTax() != null
                            ? item.getCredit().multiply(item.getTax().getTaxRateAsBigDecimal()).divide(HUNDRED, calculationScale, BigDecimal.ROUND_HALF_UP)
                            : ZERO;
                    BigDecimal itemAmount = item.getCredit().add(taxAmount);
                    totalCredit = totalCredit.add(itemAmount);
                    tItem.setCredit(itemAmount);
                }
                if (item.getDebit() != null) {
                    BigDecimal taxAmount = item.getTax() != null
                            ? item.getDebit().multiply(item.getTax().getTaxRateAsBigDecimal()).divide(HUNDRED, calculationScale, BigDecimal.ROUND_HALF_UP)
                            : ZERO;
                    BigDecimal itemAmount = item.getDebit().add(taxAmount);
                    totalDebit = totalDebit.add(itemAmount);
                    tItem.setDebit(itemAmount);
                }
                if (item.getDepartment() != null) {
                    tItem.setDepartment(item.getDepartment().getName());
                }
                j++;
            }
            trans.setTransactionItems(tItems);
            trans.setTotalCredit(totalCredit);
            trans.setTotalDebit(totalDebit);
            if (totalCredit.compareTo(totalDebit) != 0) {
                messageManager.sendIncorrectReportBalanceEmail(transaction.getJournalId());
            }
            transactionLink = "";
            if (INVOICE_TRANSACTION.equals(transaction.getKeyType())) {
                EdsInvoiceTransaction invoiceTransaction = (EdsInvoiceTransaction) transaction;
                trans.setJournalName(trans.getJournalName() + " | " + invoiceTransaction.getInvoice().getNumber());
                if (transaction.getKeyId() != null) {
                    if (RECEIVABLE.equals(invoiceTransaction.getInvoice().getType())) {
                        transactionLink = (invoiceTransaction.getInvoice().isCreditNote()
                                ? "receivablecreditnote/"
                                : "saleinvoice/") + transaction.getKeyId();
                    } else {
                        transactionLink = (invoiceTransaction.getInvoice().isCreditNote()
                                ? "payablecreditnote/"
                                : "purchaseinvoice/") + transaction.getKeyId();
                    }
                }
            } else if (OVER_PAYMENT_TRANSACTION.equals(transaction.getKeyType())) {
                EdsOverPaymentTransaction overPaymentTransaction = (EdsOverPaymentTransaction) transaction;
                EdsBatchPayment edsBatchPayment = overPaymentTransaction.getOverPayment().getBatchPayment();
                trans.setJournalName(trans.getJournalName() + " | " + edsBatchPayment.getNumber());
                transactionLink = "receivepayment|summary/" + edsBatchPayment.getObjectID() + "/" + edsBatchPayment.getType();
            } else if (GOODS_RECEIVED_TRANSACTION.equals(transaction.getKeyType())) {
                if (transaction instanceof EdsGoodsReceivedTransaction) {
                    if (((EdsGoodsReceivedTransaction) transaction).getPurchaseOrder() != null) {
                        trans.setJournalName(trans.getJournalName() + " | " + ((EdsGoodsReceivedTransaction) transaction).getPurchaseOrder().getNumber());
                        transactionLink = "purchaseorder|summary/" + transaction.getKeyId();
                    } else if (((EdsGoodsReceivedTransaction) transaction).getShippingData() != null) {
                        trans.setJournalName(trans.getJournalName() + " | " + ((EdsGoodsReceivedTransaction) transaction).getShippingData().getNumber());
                        transactionLink = "grn|summary/" + transaction.getKeyId();
                    }
                }

            } else if (GOODS_DELIVERED_TRANSACTION.equals(transaction.getKeyType())) {
                if (((EdsGoodsDeliveredTransaction) transaction).getSaleOrder() != null) {
                    trans.setJournalName(trans.getJournalName() + " | " + ((EdsGoodsDeliveredTransaction) transaction).getSaleOrder().getNumber());
                    transactionLink = "saleorder|summary/" + transaction.getKeyId();
                } else if (((EdsGoodsDeliveredTransaction) transaction).getShippingData() != null) {
                    trans.setJournalName(trans.getJournalName() + " | " + ((EdsGoodsDeliveredTransaction) transaction).getShippingData().getNumber());
                    transactionLink = "gdn|summary/" + transaction.getKeyId();
                }
            } else if (INVOICEPAYMENT_TRANSACTION.equals(transaction.getKeyType())) {
                EdsInvoicePaymentTransaction paymentTransaction = (EdsInvoicePaymentTransaction) transaction;
                EdsInvoicePayment item = paymentTransaction.getInvoicePayment();
                if (item.getBatchPaymentID() != null) {
                    EdsBatchPayment batchPayment = batchPaymentManager.get(item.getBatchPaymentID());
                    trans.setJournalName(trans.getJournalName() + " | " + batchPayment.getNumber());
                    transactionLink = "receivepayment|summary/" + item.getBatchPaymentID() + "/" + (paymentTransaction.getClient() != null
                            ? Constants.RECEIVABLE
                            : Constants.PAYABLE);
                } else if (item.getCreditNote() != null) {
                    trans.setJournalName(trans.getJournalName() + " | " + item.getCreditNote().getNumber());
                    if (item.getType() != null && RECEIVABLE_PREPAYMENT.equals(item.getType())) {
                        transactionLink = "invoicepayment|paymentView/" + transaction.getKeyId() + "/prepayment";
                    } else if (item.getType() != null && PAYABLE_SUPPLIER_CREDIT.equals(item.getType())) {
                        transactionLink = "invoicepayment|paymentView/" + transaction.getKeyId() + "/supplierCredit";
                    } else {
                        transactionLink = "invoicepayment|paymentView/" + transaction.getKeyId() + "/cashRefund";
                    }
                } else if (item.getPaymentRefundID() != null) {

                    if (RECEIVABLE_PREPAYMENT_REFUND.equals(item.getType())) {
                        transactionLink = "customerRefund|summary/" + item.getPaymentRefundID();
                    } else if (PAYABLE_PREPAYMENT_REFUND.equals(item.getType())) {
                        transactionLink = "supplierRefund|summary/" + item.getPaymentRefundID();
                    } else {
                        transactionLink = "invoicepayment|paymentView/" + item.getPaymentRefundID();
                    }
                } else {
                    transactionLink = "invoicepayment|paymentView/" + transaction.getKeyId();
                }
            } else if (PAYMENTREFUND_CLOSED_TRANSACTION.equals(transaction.getKeyType())) {
                EdsPaymentRefundTransaction paymentTransaction = (EdsPaymentRefundTransaction) transaction;
                EdsPaymentRefund item = paymentTransaction.getPaymentRefund();
                if (item != null) {
                    if (RECEIVABLE.equals(item.getType())) {
                        transactionLink = "customerRefund|summary/" + item.getObjectID();
                    } else if (PAYABLE.equals(item.getType())) {
                        transactionLink = "supplierRefund|summary/" + item.getObjectID();
                    }
                }
            } else if (INVENTORY_TRANSACTION.equals(transaction.getKeyType())) {
                EdsInventoryTransaction inventoryTransaction = (EdsInventoryTransaction) transaction;
                if (inventoryTransaction != null && inventoryTransaction.getInventory().getProductNumber() != null) {
                    trans.setJournalName(trans.getJournalName() + " | " + inventoryTransaction.getInventory().getProductNumber());
                }
                transactionLink = "product|summary/" + transaction.getKeyId();
            } else if (MANUAL_TRANSACTION.equals(transaction.getKeyType())) {
                transactionLink = "manual|summary/" + transaction.getKeyId();
            } else if (EXPENSE_TRANSACTION.equals(transaction.getKeyType())) {
                EdsExpenseTransaction expenseTransaction = (EdsExpenseTransaction) transaction;
                if (expenseTransaction.getExpenseReport() != null) {
                    trans.setJournalName(trans.getJournalName() + " | " + expenseTransaction.getExpenseReport().getNumber());
                }
                if (expenseTransaction.getExpenseReport() != null && expenseTransaction.getExpenseReport().getStatus() != null && EXPENSE_PAID.equals(expenseTransaction.getExpenseReport().getStatus())) {
                    transactionLink = "expensepayment|summary/" + transaction.getKeyId();
                } else {
                    transactionLink = "expenseReports|previewReport/" + transaction.getKeyId() + "/" + Constants.EXPENSE_VIEW + "/" + PermissionConstants.ACCOUNTING_CONTEXT;
                }
            } else if (EXPENSEPAYMENT_TRANSACTION.equals(transaction.getKeyType())) {
                EdsExpensePaymentTransaction expensePaymentTransaction = (EdsExpensePaymentTransaction) transaction;
                if (expensePaymentTransaction != null && expensePaymentTransaction.getExpensePayment() != null && expensePaymentTransaction.getExpensePayment().getExpenseReport() != null) {
                    trans.setJournalName(trans.getJournalName() + " | " + expensePaymentTransaction.getExpensePayment().getExpenseReport().getNumber());
                }
                transactionLink = "expensepayment|summary/" + transaction.getKeyId();
            } else if (FIXED_ASSET_TRANSACTION.equals(transaction.getKeyType())) {
                EdsFixedAssetTransaction fixedAssetTransaction = (EdsFixedAssetTransaction) transaction;
                if (fixedAssetTransaction != null && fixedAssetTransaction.getFixedAsset() != null) {
                    trans.setJournalName(trans.getJournalName() + " | " + fixedAssetTransaction.getFixedAsset().getCode());
                }
                transactionLink = "fixedasset|summary/" + transaction.getKeyId();
            } else if (DISPOSAL_TRANSACTION.equals(transaction.getKeyType())) {
                EdsDisposalTransaction fixedAssetTransaction = (EdsDisposalTransaction) transaction;
                if (fixedAssetTransaction != null && fixedAssetTransaction.getFixedAsset() != null) {
                    trans.setJournalName(trans.getJournalName() + " | " + fixedAssetTransaction.getFixedAsset().getCode());
                }
                transactionLink = "fixedasset|summary/" + transaction.getKeyId();
            } else if (CUSTOMER_SUPPLIER_PAYMENT_TRANSACTION.equals(transaction.getKeyType())) {
                transactionLink = "transactionItemView|summary/" + transaction.getObjectID();
            } else if (BANK_CHECK_TRANSACTION.equals(transaction.getKeyType())) {
                transactionLink = "check|summary/" + transaction.getKeyId();
            } else if (BANK_TRANSFER_TRANSACTION.equals(transaction.getKeyType())) {
                EdsBankTransferTransaction bankTransferTransaction = (EdsBankTransferTransaction) transaction;
                trans.setJournalName(trans.getJournalName() + " | " + bankTransferTransaction.getBankTransfer().getNumber());
                String type = "";
                if (RECEIVE_MONEY.equals(bankTransferTransaction.getBankTransfer().getTransferType())) {
                    type = RECEIVE_MONEY_STR;
                } else if (SPEND_MONEY.equals(bankTransferTransaction.getBankTransfer().getTransferType())) {
                    type = SPEND_MONEY_STR;
                } else if (CASH_RECEIPT.equals(bankTransferTransaction.getBankTransfer().getTransferType())) {
                    type = CASH_RECEIPT_STR;
                } else if (CASH_PAYMENT.equals(bankTransferTransaction.getBankTransfer().getTransferType())) {
                    type = CASH_PAYMENT_STR;
                }
                transactionLink = "spendreceivemoney|summary/" + transaction.getKeyId() + "/" + type;
            } else if (BANK_MONEY_TRANSFER_TRANSACTION.equals(transaction.getKeyType())) {
                transactionLink = "transfer|summary/" + transaction.getKeyId() + "/" + VIEW_FORM;
            } else if (ADJUSTMENT_TRANSACTION.equals(transaction.getKeyType())) {
                EdsStockAdjustmentTransaction stockAdjustmentTransaction = (EdsStockAdjustmentTransaction) transaction;
                if (stockAdjustmentTransaction != null && stockAdjustmentTransaction.getAdjustment() != null) {
                    trans.setJournalName(trans.getJournalName() + " | " + stockAdjustmentTransaction.getAdjustment().getNumber());
                }
                transactionLink = "stockadjustment|summary/" + transaction.getKeyId();
            } else if (STOCK_TRANSFER_TRANSACTION.equals(transaction.getKeyType())) {
                transactionLink = "stocktransfer|summary/" + transaction.getKeyId();
            } else if (RETAINED_EARNINGS_TRANSACTION.equals(transaction.getKeyType())) {
                transactionLink = "clickedreport|newprofitLoss/" + transaction.getKeyId() + "/accountTransaction" + "/" + dateFormat.format(from) + "/" + dateFormat.format(to);
            } else if (CASH_ADVANCE_TRANSACTION.equals(transaction.getKeyType())) {
                EdsCashAdvanceTransaction cashAdvanceTransaction = (EdsCashAdvanceTransaction) transaction;
                EdsCashAdvance cashAdvance = cashAdvanceTransaction.getCashAdvance();
                trans.setJournalName(trans.getJournalName() + " | " + cashAdvance.getNumber());
                transactionLink = EdsContextParams.getHost() + "/Payroll.html#cashAdvance|summary/view/" + transaction.getKeyId() + "/" + cashAdvanceTransaction.getCashAdvance().getStatus().getName();
                isBlank = true;
            } else if (SINGLE_PAYRUN_TRANSACTION.equals(transaction.getKeyType())) {
                EdsSinglePayrunTransaction edsSinglePayrun = (EdsSinglePayrunTransaction) transaction;
                transactionLink = EdsContextParams.getHost() + "/Payroll.html#singlePayrun|viewPayslip/" + transaction.getKeyId();
                isBlank = true;
            } else if (DEFERRED_TRANSACTION.equals(transaction.getKeyType())) {
                EdsDeferredTransaction edsDeferredTransaction = (EdsDeferredTransaction) transaction;
                if (DeferredTransactionType.EXPENSE.equals(edsDeferredTransaction.getDeferredType())) {
                    transactionLink = "expenseReports|previewReport/" + transaction.getKeyId() + "/" + Constants.EXPENSE_VIEW + "/" + PermissionConstants.ACCOUNTING_CONTEXT;
                } else if (DeferredTransactionType.SALE_INVOICE.equals(edsDeferredTransaction.getDeferredType())) {
                    transactionLink = "saleinvoice/" + transaction.getKeyId();
                } else {
                    transactionLink = "purchaseinvoice/" + transaction.getKeyId();
                }
            }
            trans.setTransactionLink(transactionLink);
            trans.setBlank(isBlank);
            i++;
        }

        result.setList(transactionsArray);

        return result;
    }

    public Integer getCompanyLastTransactionOrderID() {
        Integer id = (Integer) findSingle("select max(t.journalId) from EdsTransaction t ");
        if (id == null) {
            return 0;
        } else {
            return id;
        }
    }

    public Integer getCompanyLastTransactionOrderID(EdsCompany company) {
        Integer id = (Integer) findSingle("select max(t.journalId) from EdsTransaction t ");
        if (id == null) {
            return 0;
        } else {
            return id;
        }
    }

    public List<Transaction> listAttendedInAccountInPeriod2(ListingFilterParameter fp,
                                                            String departmentAndTreeChildIDs,
                                                            boolean isCacheOnly) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        StringBuilder sql = new StringBuilder();

        sql.append("SELECT temp.transactionid, temp.journalid, temp.reference, temp.invoiceOrPaymentType, temp.number, temp.invoicePaymentNumber, temp.clientName, temp.supplierName, ")
                .append("temp.creditNote, temp.description, temp.spendReceiveMoneyType, temp.spendReceiveMoneyNumber, temp.spendReceiveMoneyNarration, temp.manualJournalNumber,")
                .append("temp.narration, temp.checkNumber, temp.expenseNumber, temp.expenseTitle, temp.stockAdjustmentNumber, temp.productNumber, temp.fixedAssetNumber, temp.fixedAssetName, ")
                .append("temp.totalCredit, temp.totalDebit, temp.cashAdvanceNumber, temp.cashAdvanceStatus, temp.name, temp.reversedJournalId, temp.reconcileStatus ")
                .append(" FROM (\n SELECT t.id as transactionid, t.journalid, ")
                .append("    coalesce(ip.reference, (CASE WHEN i.reference is not null THEN i.reference ELSE (CASE WHEN t.dtype = 'EdsBankCheckTransaction' THEN\n")
                .append("                                   (CASE WHEN t.reference is not null THEN t.reference || ', Pay To: '||bch.payTo ELSE 'Pay To: '||bch.payTo END)\n")
                .append("                                         WHEN t.dtype = 'EdsBankTransferTransaction' THEN COALESCE(spr.reference,coalesce(ti.description,t.reference)) \n")
                .append("                                         ELSE t.reference \n")
                .append("                                    END) END) \n")
                .append("               ) reference,\n")
                .append("    coalesce(i.type, ip.type) invoiceOrPaymentType, \n")
                .append("    coalesce(coalesce(i.number, quo.number), q1.number) number, \n")
                .append("    ip.number invoicePaymentNumber, \n")
                .append("    client.name clientName, \n")
                .append("    supplier.name supplierName, \n")
                .append("    coalesce(i.iscreditnote, (CASE WHEN ip.invoiceid is null and ip.creditnoteid is not null THEN true ELSE false END)) as creditNote, \n")
                .append("    coalesce(bt.description, (CASE WHEN t.dtype = 'EdsExpenseTransaction' OR t.dtype = 'EdsManualTransaction' THEN ti.description ELSE '' END)) description, \n")
                .append("    bt.transferType spendReceiveMoneyType,bt.number spendReceiveMoneyNumber,bt.name spendReceiveMoneyNarration, \n")
                .append("    mj.number manualJournalNumber, mj.narration narration, \n")
                .append("    bch.number checkNumber, \n")
                .append("    expr.number expenseNumber,expr.title expenseTitle, \n")
                .append("    adj.number stockAdjustmentNumber, \n")
                .append("    it.product_number productNumber, \n")
                .append("    fa.code fixedAssetNumber,fa.name fixedAssetName, \n")
                .append("    (CASE WHEN a.foreignAccount is true ").append(" and ").append(!fp.isShowInBase()).append(" is true ").append(" THEN ti.foreignCredit ELSE ti.credit END) as totalCredit, \n")
                .append("    (CASE WHEN a.foreignAccount is true ").append(" and ").append(!fp.isShowInBase()).append(" is true ").append("THEN ti.foreignDebit ELSE ti.debit END) as totalDebit, \n")
                .append("     cashAdv.number cashAdvanceNumber, \n")
                .append("     cashAdvRef.name cashAdvanceStatus, \n")
                .append("     t.journaldate as journaldate, \n")
                .append("     t.name AS name,\n")
                .append("     t.reversalid AS reversedJournalId,\n")
                .append("     ti.reconcileStatus AS reconcileStatus\n")
                .append("FROM ").append(getCompanyId()).append(".transactionitem ti \n")
                .append("INNER JOIN ").append(getCompanyId()).append(".account a on a.id = ti.accountid \n")
                .append("INNER JOIN ").append(getPublic()).append(".accounttype act on act.id = a.accountTypeId \n")
                .append("INNER JOIN ").append(getCompanyId()).append(".transaction t on t.id = ti.transactionid \n")
                .append("LEFT JOIN ").append(getCompanyId()).append(".invoicepayments ip on ip.id = t.invoicepaymentid \n")
                .append("LEFT JOIN ").append(getCompanyId()).append(".invoice i on i.id = t.invoiceid \n")
                .append("LEFT JOIN ").append(getCompanyId()).append(".purchaseorder po on po.id = t.purchaseorder_id \n")
                .append("LEFT JOIN ").append(getCompanyId()).append(".shipping_data shd on shd.id = t.shippingDataId \n")
                .append("LEFT JOIN ").append(getCompanyId()).append(".quote q1 on q1.id = shd.quoteId \n")
                .append("LEFT JOIN ").append(getCompanyId()).append(".quote quo on quo.id = po.id \n")
                .append("LEFT JOIN ").append(getCompanyId()).append(".bankcheck bch on bch.id = t.bankcheckid \n")
                .append("LEFT JOIN ").append(getCompanyId()).append(".manualjournal mj on mj.id = t.manualjournalid \n")
                .append("LEFT JOIN ").append(getCompanyId()).append(".fixedasset fa on fa.id = t.fixedassetid \n")
                .append("LEFT JOIN ").append(getCompanyId()).append(".expenseReport expr on expr.id = t.expenseReportid \n")
                .append("LEFT JOIN ").append(getCompanyId()).append(".expensePayments expay on t.expensePaymentId=expay.id \n")
                .append("LEFT JOIN ").append(getCompanyId()).append(".expensereport payexp on expay.expenseReportId=payexp.id \n")
                .append("LEFT JOIN ").append(getCompanyId()).append(".crmAccount client on client.id = t.clientid \n")
                .append("LEFT JOIN ").append(getCompanyId()).append(".stock_adjustment adj on adj.id = t.adjustment_id \n")
//                .append("LEFT JOIN ").append(getCompanyId()).append(".adjustment_item adi on adj.id = adi.adjustment_id \n")
                .append("LEFT JOIN ").append(getCompanyId()).append(".cashAdvance cashAdv on cashAdv.id = t.cashadvance_id \n")
                .append("LEFT JOIN ").append(getCompanyId()).append(".Reference cashAdvRef on cashAdvRef.id = cashAdv.overallstatus \n")
                .append("LEFT JOIN ").append(getCompanyId()).append(".item it on it.id = t.inventory_id \n")
                .append("LEFT JOIN ").append(getCompanyId()).append(".crmAccount supplier on supplier.id = t.supplierid \n")
                .append("LEFT JOIN ").append(getCompanyId()).append(".adjustment_item adjit on ti.stock_adjustment_item_id=adjit.id \n")
                .append("LEFT JOIN ").append(getCompanyId()).append(".spendreceivemoney spr on t.banktransferid=spr.id \n")
                .append("left join ").append(getCompanyId()).append(".quoteitem poitem on t.purchaseorder_id is not null and ti.itemid=poitem.id \n")
                .append("left join ").append(getCompanyId()).append(".quoteitem soitem on t.saleorder_id is not null and ti.itemid=soitem.id \n")
                .append("left join ").append(getCompanyId()).append(".invoiceitem invitem on t.invoiceid is not null and ti.itemid=invitem.id \n")
                .append("left join ").append(getCompanyId()).append(".shipping_data_items shi on t.shippingDataId is not null and ti.itemid = shi.id \n")
                .append("left join ").append(getCompanyId()).append(".quoteitem shitem on shi.quoteItemId = shitem.id \n")
                .append("left join ").append(getCompanyId()).append(".adjustment_item adjitem on t.stockTransferId is not null and ti.itemid=adjitem.id \n")
                .append("left join ").append(getCompanyId()).append(".payslipTableItem pti on t.payrun_id is not null and t.payrun_id=pti.id ")
                .append("left join ").append(getCompanyId()).append(".payslipTable pt on pti.payslipTable_id is not null and pti.payslipTable_id=pt.id ");

        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
            sql.append("left join ").append(getCompanyId()).append(".project prj on coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(ti.project_id,expr.projectId),payexp.projectId),spr.projectid),adjit.projectid),adjitem.projectid),mj.projectid),poitem.project_id),soitem.project_id),invitem.project_id),shitem.project_id) = prj.id \n");
        } else {
            sql.append("left join ").append(getCompanyId()).append(".project prj on coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(ti.project_id,expr.projectId),payexp.projectId),spr.projectid),adjit.projectid),adjitem.projectid),mj.projectid),i.relatedproject_id),quo.relatedproject_id) = prj.id \n");
        }

        sql.append("LEFT JOIN (select srm.id,srm.name,srm.transferType,srm.number,srm.projectid, array_to_string(array_agg(srmi.description),',') description \n")
                .append("from ").append(getCompanyId()).append(".spendreceivemoney srm \n")
                .append("inner join ").append(getCompanyId()).append(".spendreceivemoneyitem srmi on srmi.banktransferid = srm.id \n")
                .append("where srm.deleted is not true \n")
                .append("group by srm.id,srm.name, srm.transferType, srm.number,srm.projectid ) bt on spr.id =bt.id \n")
                .append("WHERE t.deleted is not true and ti.accountid = ").append(fp.getAccountID()).append("\n");
        if (fp.getDepartmentId() != null) {
            sql.append(" and (");
            sql.append("ti.department_id in (").append(departmentAndTreeChildIDs).append(") \n");
            sql.append("or adjit.departmentid in (").append(departmentAndTreeChildIDs).append(") \n");
            sql.append("or adjitem.departmentid in (").append(departmentAndTreeChildIDs).append(") \n");
            sql.append("or (t.invoiceid is not null and invitem.departmentid in (").append(departmentAndTreeChildIDs).append(")) \n");
            sql.append("or (t.purchaseorder_id is not null and poitem.departmentid in (").append(departmentAndTreeChildIDs).append(")) \n");
            sql.append("or (t.saleorder_id is not null and soitem.departmentid in (").append(departmentAndTreeChildIDs).append(")) \n");
            sql.append("or (t.shippingDataId is not null and shitem.departmentid in (").append(departmentAndTreeChildIDs).append(")) \n");
            sql.append(") \n");
        }
        if (fp.getProjectId() != null) {
            sql.append(" AND (");
            sql.append("ti.project_id = ").append(fp.getProjectId()).append(" \n");
            sql.append("or (t.expensereportid is not null and expr.projectId=").append(fp.getProjectId()).append(") \n");
            sql.append("or (t.expensePaymentId is not null and payexp.projectId=").append(fp.getProjectId()).append(") \n");
            sql.append("or (t.banktransferid is not null and bt.projectid=").append(fp.getProjectId()).append(") \n");
            sql.append("or (t.adjustment_id is not null and adjit.projectid=").append(fp.getProjectId()).append(") \n");
            sql.append("or (t.stockTransferId is not null and adjitem.projectid=").append(fp.getProjectId()).append(") \n");
            sql.append("or (pt.projectid=").append(fp.getProjectId()).append(") \n");
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MANUAL_JOURNAL_PM_TO_HEAD_ENABLED)) {
                sql.append("or (t.manualjournalid is not null and mj.projectid=").append(fp.getProjectId()).append(") \n");
            }
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
                sql.append("or (t.purchaseorder_id is not null and poitem.project_id=").append(fp.getProjectId()).append(") \n");
                sql.append("or (t.saleorder_id is not null and soitem.project_id=").append(fp.getProjectId()).append(") \n");
                sql.append("or (t.invoiceid is not null and invitem.project_id=").append(fp.getProjectId()).append(") \n");
                sql.append("or (t.shippingDataId is not null and shitem.project_id=").append(fp.getProjectId()).append(") \n");
            } else {
                sql.append("or (t.invoiceid is not null and i.relatedproject_id=").append(fp.getProjectId()).append(") \n");
                sql.append("or (t.purchaseorder_id is not null and quo.relatedproject_id=").append(fp.getProjectId()).append(") \n");
            }
            sql.append(") \n");
        } else if (!ServerUtils.hasPermission(PermissionConstants.PM_SEE_ALL_PROJECTS) && genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MANUAL_JOURNAL_PM_TO_HEAD_ENABLED)) {
            sql.append(" and prj.managerid = ").append(((EdsUser) ServerSecurityContext.getInstance().getUser()).getObjectID()).append(" ");
        }
        if (isCacheOnly) {
            sql.append(" and t.dtype in ('EdsCusSuppPaymentTransaction','EdsExpensePaymentTransaction','EdsInvoicePaymentTransaction','EdsOverPaymentTransaction','EdsCashAdvancePayTransaction','EdsManualTransaction') \n");
            sql.append(" and (act.code='" + EdsAccountType.BANK + "' or a.enablePayments=true) \n");
        }

        if (fp.getStartDate() != null && fp.getEndDate() != null) {
            sql.append("AND t.journaldate between '").append(fp.getStartDate()).append("' and '").append(fp.getEndDate()).append("' \n");
        }
        if (fp.getAccountTransactionStatus() != null) {
            if (fp.getAccountTransactionStatus().equals(UNRECONCILED)) {
                sql.append(" and (ti.reconcileStatus = '").append(fp.getAccountTransactionStatus()).append("'");
                sql.append(" or ti.reconcileStatus is null )");
            } else {
                sql.append(" and ti.reconcileStatus = '").append(fp.getAccountTransactionStatus()).append("'");
            }
        }
        if (fp.getStatusCodes() != null && fp.getStatusCodes().length > 0) {
            if (fp.getStatusCodes().length == 2) {
                sql.append(" and (ti.reconcileStatus = '").append(fp.getStatusCodes()[0]).append("' or ti.reconcileStatus = '").append(fp.getStatusCodes()[1]).append("')");
            }
        }
        if (!StringUtils.isEmpty(fp.getSqlSearchKey())) {
            sql.append(" and ( lower(t.name) like '").append(fp.getSqlSearchKey()).append("'");
            sql.append(" or lower(t.reference) like '").append(fp.getSqlSearchKey()).append("')\n");
        }

        sql.append(") temp \n");

        if (!StringUtils.isEmpty(fp.getSortField())) {
            if (fp.getSortField().equals(AccountingConstants.BANK_TRANSFER_DESCRIPTION_COLUMN)) {
                sql.append(" ORDER BY temp.description ");
            } else if (fp.getSortField().equals("icon") || fp.getSortField().equals(STATUS_COLUMN)) {
                sql.append(" ORDER BY temp.transactionid ");
            } else if (fp.getSortField().equals(DATE_COLUMN)) {
                sql.append(" ORDER BY temp.journaldate::timestamp ");
            } else if (fp.getSortField().equals(DESCRIPTION_COLUMN)) {
                sql.append(" ORDER BY temp.name ");
            } else if (fp.getSortField().equals(SPENT_COLUMN)) {
                sql.append(" ORDER BY temp.totalCredit ");
            } else if (fp.getSortField().equals(RECEIVED_COLUMN)) {
                sql.append(" ORDER BY temp.totalDebit ");
            } else if (fp.getSortField().equals(NUMBER_COLUMN)) {
                sql.append(" ORDER BY temp.number ");
            }

            if (fp.getSortDir() != null) {
                if (fp.getSortDir() == 1) {
                    sql.append(" DESC ");
                } else if (fp.getSortDir() == 2) {
                    sql.append(" ASC ");
                }
            }
        } else {
            sql.append(" ORDER BY temp.number DESC \n");
        }

        return jdbcSpringManager.getSimpleJdbcTemplate().query(sql.toString(), BeanPropertyRowMapper.newInstance(Transaction.class));
    }

    private String getSqlBodyListAttendedInAccountInPeriod(ListingFilterParameter fp, String sql, String departmentAndTreeChildIDs) {
        Integer accountId = null;
        Date from = null;
        Date toGiven = null;
        if (fp != null) {
            accountId = fp.getAccountID();
            from = fp.getStartDate();
            toGiven = fp.getEndDate();
        }
        boolean dateInterval = from != null && toGiven != null;
        sql += "FROM " + getCompanyId() + ".transactionitem ti \n";
        sql += "JOIN " + getCompanyId() + ".transaction tr ON tr.id = ti.transactionid \n";
        sql += "JOIN " + getCompanyId() + ".account a ON a.id = ti.accountid \n";
        sql += "INNER JOIN " + getPublic() + ".accounttype at ON at.id = a.accounttypeid \n";
        sql += "left join " + getCompanyId() + ".invoice i on tr.invoiceid=i.id \n";
        sql += "left join " + getCompanyId() + ".manualjournal mj on tr.manualjournalid=mj.id \n";
        sql += "left join " + getCompanyId() + ".expensereport expr on tr.expensereportid=expr.id \n";
        sql += "left join " + getCompanyId() + ".expensePayments expay on tr.expensePaymentId=expay.id \n";
        sql += "left join " + getCompanyId() + ".expensereport payexp on expay.expenseReportId=payexp.id \n";
        sql += "left join " + getCompanyId() + ".quote quo on tr.purchaseorder_id=quo.id \n";
        sql += "left join " + getCompanyId() + ".spendreceivemoney spr on tr.banktransferid=spr.id \n";
        sql += "left join " + getCompanyId() + ".adjustment_item adjit on ti.stock_adjustment_item_id=adjit.id \n";
        sql += "left join " + getCompanyId() + ".quoteitem poitem on tr.purchaseorder_id is not null and ti.itemid=poitem.id \n";
        sql += "left join " + getCompanyId() + ".quoteitem soitem on tr.saleorder_id is not null and ti.itemid=soitem.id \n";
        sql += "left join " + getCompanyId() + ".invoiceitem invitem on tr.invoiceid is not null and ti.itemid=invitem.id \n";
        sql += "left join " + getCompanyId() + ".shipping_data_items shi on tr.shippingDataId is not null and ti.itemid = shi.id \n";
        sql += "left join " + getCompanyId() + ".quoteitem shitem on shi.quoteItemId = shitem.id \n";
        sql += "left join " + getCompanyId() + ".adjustment_item adjitem on tr.stockTransferId is not null and ti.itemid=adjitem.id \n";
        sql += "left join " + getCompanyId() + ".payslipTableItem pti on tr.payrun_id is not null and tr.payrun_id=pti.id \n";
        sql += "left join " + getCompanyId() + ".payslipTable pt on pti.payslipTable_id is not null and pti.payslipTable_id=pt.id \n";
//        if (fp != null && fp.getDepartmentId() != null) {
//            sql += "left join " + getCompanyId() + ".stock_adjustment adj on adj.id = tr.adjustment_id \n";
//            sql += "left join " + getCompanyId() + ".adjustment_item adi on adj.id = adi.adjustment_id \n";
//        }
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
            sql += "left join " + getCompanyId() + ".project prj on coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(ti.project_id,expr.projectId),payexp.projectId),spr.projectid),adjit.projectid),adjitem.projectid),mj.projectid),poitem.project_id),soitem.project_id),invitem.project_id),shitem.project_id) = prj.id \n";
        } else {
            sql += "left join " + getCompanyId() + ".project prj on coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(ti.project_id,expr.projectId),payexp.projectId),spr.projectid),adjit.projectid),adjitem.projectid),mj.projectid),i.relatedproject_id),quo.relatedproject_id) = prj.id \n";
        }

        sql += " where tr.deleted is not true  and ti.accountid=" + accountId + " \n";
        if (dateInterval) {
            sql += " and (date(tr.journalDate) between date('" + from + "') and date('" + toGiven + "')) \n";
        } else if (from != null) {
            sql += " and date(tr.journalDate) < date('" + from + "') \n";
        } else if (toGiven != null) {
            sql += " and date(tr.journalDate) <= date('" + toGiven + "') \n";
        }
        if (fp != null && fp.getAccountTransactionStatus() != null) {
            if (fp.getAccountTransactionStatus().equals(UNRECONCILED)) {
                sql += " and (ti.reconcileStatus = '" + fp.getAccountTransactionStatus() + "' or ti.reconcileStatus is null ) \n";
            } else {
                sql += " and ti.reconcileStatus = '" + fp.getAccountTransactionStatus() + "' \n";
            }
        }
        if (fp != null && fp.getDepartmentId() != null) {
            sql += " and (";
            sql += "ti.department_id in (" + departmentAndTreeChildIDs + ") \n";
            sql += "or adjit.departmentid in (" + departmentAndTreeChildIDs + ") \n";
            sql += "or adjitem.departmentid in (" + departmentAndTreeChildIDs + ") \n";
            sql += "or (tr.invoiceid is not null and invitem.departmentid in (" + departmentAndTreeChildIDs + ")) \n";
            sql += "or (tr.purchaseorder_id is not null and poitem.departmentid in (" + departmentAndTreeChildIDs + ")) \n";
            sql += "or (tr.saleorder_id is not null and soitem.departmentid in (" + departmentAndTreeChildIDs + ")) \n";
            sql += "or (tr.shippingDataId is not null and shitem.departmentid in (" + departmentAndTreeChildIDs + ")) \n";
            sql += ") \n";
        }
        if (fp != null && fp.getProjectId() != null) {
            sql += " AND (";
            sql += "ti.project_id = " + fp.getProjectId() + " \n";
            sql += "or (tr.expensereportid is not null and expr.projectId=" + fp.getProjectId() + ") \n";
            sql += "or (tr.expensePaymentId is not null and payexp.projectId=" + fp.getProjectId() + ") \n";
            sql += "or (tr.banktransferid is not null and spr.projectid=" + fp.getProjectId() + ") \n";
            sql += "or (tr.adjustment_id is not null and adjit.projectid=" + fp.getProjectId() + ") \n";
            sql += "or (tr.stockTransferId is not null and adjitem.projectid=" + fp.getProjectId() + ")  \n";
            sql += "or (pt.projectid=" + fp.getProjectId() + ") \n";
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MANUAL_JOURNAL_PM_TO_HEAD_ENABLED)) {
                sql += "or (tr.manualjournalid is not null and mj.projectid=" + fp.getProjectId() + ")  \n";
            }
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
                sql += "or (tr.purchaseorder_id is not null and poitem.project_id=" + fp.getProjectId() + ")  \n";
                sql += "or (tr.saleorder_id is not null and soitem.project_id=" + fp.getProjectId() + ")  \n";
                sql += "or (tr.invoiceid is not null and invitem.project_id=" + fp.getProjectId() + ")  \n";
                sql += "or (tr.shippingDataId is not null and shitem.project_id=" + fp.getProjectId() + ")  \n";
            } else {
                sql += "or (tr.invoiceid is not null and i.relatedproject_id=" + fp.getProjectId() + ") \n";
                sql += "or (tr.purchaseorder_id is not null and quo.relatedproject_id=" + fp.getProjectId() + ") \n";
            }
            sql += ") \n";
        } else if (!ServerUtils.hasPermission(PermissionConstants.PM_SEE_ALL_PROJECTS) && genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MANUAL_JOURNAL_PM_TO_HEAD_ENABLED)) {
            sql += " and prj.managerid = " + ((EdsUser) ServerSecurityContext.getInstance().getUser()).getObjectID() + " ";
        }
        if (fp != null && fp.getStatusCodes() != null && fp.getStatusCodes().length > 0 && fp.getStatusCodes().length == 2) {
            sql += " and (ti.reconcileStatus = '" + fp.getStatusCodes()[0] + "' or ti.reconcileStatus = '" + fp.getStatusCodes()[1] + "') \n";
        }

        if (fp != null && fp.getSqlSearchKey() != null && !fp.getSqlSearchKey().equals("")) {
            sql += " and ( lower(tr.name) like '" + fp.getSqlSearchKey() + "' or lower(tr.reference) like '" + fp.getSqlSearchKey() + "') \n";
        }
        return sql;
    }

    public void deleteBankTransactionByAccountId(Integer accountId) {
        updateNative("update " + getCompanyId() + ".transaction set deleted = 't' where dtype='EdsBankTransaction' and bankaccountid=" + accountId);
    }

    private String getSqlBodylistCashOnlyInAccountInPeriod(ListingFilterParameter fp, String sql, String departmentAndTreeChildIDs) {
        Integer accountId = null;
        Date from = null;
        Date toGiven = null;
        String searchKey = null;
        if (fp != null) {
            accountId = fp.getAccountID();
            from = fp.getStartDate();
            toGiven = fp.getEndDate();
            searchKey = fp.getSqlSearchKey();
        }
        sql += "FROM " + getCompanyId() + ".transactionitem ti \n";
        sql += "JOIN " + getCompanyId() + ".transaction tr ON tr.id = ti.transactionid \n";
        sql += "JOIN " + getCompanyId() + ".account a ON a.id = ti.accountid \n";
        sql += "INNER JOIN " + getPublic() + ".accounttype at ON at.id = a.accounttypeid \n";
        sql += "left join " + getCompanyId() + ".invoice i on tr.invoiceid=i.id  \n";
        sql += "left join " + getCompanyId() + ".manualjournal mj on tr.manualjournalid=mj.id  \n";
        sql += "left join " + getCompanyId() + ".expensereport expr on tr.expensereportid=expr.id  \n";
        sql += "left join " + getCompanyId() + ".expensePayments expay on tr.expensePaymentId=expay.id  \n";
        sql += "left join " + getCompanyId() + ".expensereport payexp on expay.expenseReportId=payexp.id  \n";
        sql += "left join " + getCompanyId() + ".quote quo on tr.purchaseorder_id=quo.id  \n";
        sql += "left join " + getCompanyId() + ".spendreceivemoney spr on tr.banktransferid=spr.id  \n";
        sql += "left join " + getCompanyId() + ".adjustment_item adjit on ti.stock_adjustment_item_id=adjit.id  \n";
        sql += "left join " + getCompanyId() + ".quoteitem poitem on tr.purchaseorder_id is not null and ti.itemid=poitem.id \n";
        sql += "left join " + getCompanyId() + ".quoteitem soitem on tr.saleorder_id is not null and ti.itemid=soitem.id \n";
        sql += "left join " + getCompanyId() + ".invoiceitem invitem on tr.invoiceid is not null and ti.itemid=invitem.id \n";
        sql += "left join " + getCompanyId() + ".shipping_data_items shi on tr.shippingDataId is not null and ti.itemid = shi.id \n";
        sql += "left join " + getCompanyId() + ".quoteitem shitem on shi.quoteItemId = shitem.id \n";
        sql += "left join " + getCompanyId() + ".adjustment_item adjitem on tr.stockTransferId is not null and ti.itemid=adjitem.id \n";
        sql += "left join " + getCompanyId() + ".payslipTableItem pti on tr.payrun_id is not null and tr.payrun_id=pti.id \n";
        sql += "left join " + getCompanyId() + ".payslipTable pt on pti.payslipTable_id is not null and pti.payslipTable_id=pt.id \n";
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
            sql += "left join " + getCompanyId() + ".project prj on coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(ti.project_id,expr.projectId),payexp.projectId),spr.projectid),adjit.projectid),adjitem.projectid),mj.projectid),poitem.project_id),soitem.project_id),invitem.project_id),shitem.project_id) = prj.id \n";
        } else {
            sql += "left join " + getCompanyId() + ".project prj on coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(ti.project_id,expr.projectId),payexp.projectId),spr.projectid),adjit.projectid),adjitem.projectid),mj.projectid),i.relatedproject_id),quo.relatedproject_id) = prj.id \n";
        }
        sql += "where tr.deleted is not true  \n";
        sql += "and tr.dtype in ('EdsCusSuppPaymentTransaction','EdsExpensePaymentTransaction','EdsInvoicePaymentTransaction','EdsOverPaymentTransaction','EdsCashAdvancePayTransaction','EdsManualTransaction') \n";
        sql += "and (at.code='" + EdsAccountType.BANK + "' or a.enablePayments=true) \n";

        if (from != null && toGiven != null) {
            sql += " and (date(tr.journalDate) between date('" + from + "') and date('" + toGiven + "'))  \n";
        } else if (from != null) {
            sql += " and date(tr.journalDate) < date('" + from + "')  \n";
        } else if (toGiven != null) {
            sql += " and date(tr.journalDate) <= date('" + toGiven + "')  \n";
        }
        if (fp != null && fp.getDepartmentId() != null) {
            sql += " and (";
            sql += "ti.department_id in (" + departmentAndTreeChildIDs + ") \n";
            sql += "or adjit.departmentid in (" + departmentAndTreeChildIDs + ") \n";
            sql += "or adjitem.departmentid in (" + departmentAndTreeChildIDs + ") \n";
            sql += "or (tr.invoiceid is not null and invitem.departmentid in (" + departmentAndTreeChildIDs + ")) \n";
            sql += "or (tr.purchaseorder_id is not null and poitem.departmentid in (" + departmentAndTreeChildIDs + ")) \n";
            sql += "or (tr.saleorder_id is not null and soitem.departmentid in (" + departmentAndTreeChildIDs + ")) \n";
            sql += "or (tr.shippingDataId is not null and shitem.departmentid in (" + departmentAndTreeChildIDs + ")) \n";
            sql += ") \n";
        }
        if (fp != null && fp.getProjectId() != null) {
            sql += " AND (";
            sql += "ti.project_id = " + fp.getProjectId() + " \n";
            sql += "or (tr.expensereportid is not null and expr.projectId=" + fp.getProjectId() + ")  \n";
            sql += "or (tr.expensePaymentId is not null and payexp.projectId=" + fp.getProjectId() + ")  \n";
            sql += "or (tr.banktransferid is not null and spr.projectid=" + fp.getProjectId() + ")  \n";
            sql += "or (tr.adjustment_id is not null and adjit.projectid=" + fp.getProjectId() + ")  \n";
            sql += "or (tr.stockTransferId is not null and adjitem.projectid=" + fp.getProjectId() + ")  \n";
            sql += "or (pt.projectid=" + fp.getProjectId() + ") \n";
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MANUAL_JOURNAL_PM_TO_HEAD_ENABLED)) {
                sql += "or (tr.manualjournalid is not null and mj.projectid=" + fp.getProjectId() + ")  \n";
            }
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
                sql += "or (tr.purchaseorder_id is not null and poitem.project_id=" + fp.getProjectId() + ")  \n";
                sql += "or (tr.saleorder_id is not null and soitem.project_id=" + fp.getProjectId() + ")  \n";
                sql += "or (tr.invoiceid is not null and invitem.project_id=" + fp.getProjectId() + ")  \n";
                sql += "or (tr.shippingDataId is not null and shitem.project_id=" + fp.getProjectId() + ")  \n";
            } else {
                sql += "or (tr.invoiceid is not null and i.relatedproject_id=" + fp.getProjectId() + ")  \n";
                sql += "or (tr.purchaseorder_id is not null and quo.relatedproject_id=" + fp.getProjectId() + ")  \n";
            }
            sql += ")  \n";
        } else if (!ServerUtils.hasPermission(PermissionConstants.PM_SEE_ALL_PROJECTS) && genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MANUAL_JOURNAL_PM_TO_HEAD_ENABLED)) {
            sql += " and prj.managerid = " + ((EdsUser) ServerSecurityContext.getInstance().getUser()).getObjectID() + " ";
        }
        if (searchKey != null && !Objects.equals(searchKey, "")) {
            sql += " and ( lower(tr.name) like '" + searchKey + "' or lower(tr.reference) like '" + searchKey + "') \n";
        }

        sql += " and ti.accountid=" + accountId;
        return sql;
    }

    public EdsInvoiceTransaction getTransactionByInvoice(EdsInvoice invoice) {
        return getTransactionByInvoice(invoice, false);
    }

    @Override
    public EdsInvoiceTransaction getTransactionByInvoice(EdsInvoice invoice, boolean evenDeleted) {
        return (EdsInvoiceTransaction) findSingle("from EdsInvoiceTransaction it where " + (!evenDeleted
                ? "(it.deleted is null or it.deleted = false ) and"
                : "") + " it.invoice = ? and it.reversalTransaction is null", invoice);
    }

    @Override
    public EdsInvoicePaymentTransaction getTransactionByPayment(EdsInvoicePayment invoicePayment) {
        return (EdsInvoicePaymentTransaction) findSingle("from EdsInvoicePaymentTransaction ipt where (ipt.deleted is null or ipt.deleted = false ) and ipt.invoicePayment = ? and ipt.reversalTransaction is null", invoicePayment);
    }

    @Override
    public EdsOverPaymentTransaction getTransactionByOverPayment(EdsOverPayment overPayment) {
        return (EdsOverPaymentTransaction) findSingle("from EdsOverPaymentTransaction oupt where (oupt.deleted is null or oupt.deleted = false ) and oupt.overPayment = ? ", overPayment);
    }

    @Override
    public void deleteTransactionByRefund(EdsPaymentRefund paymentRefund) {
        update("update EdsPaymentRefundTransaction pft set deleted=true where pft.paymentRefund.objectID = ?", paymentRefund.getObjectID());
    }

    @Override
    public EdsPaymentRefundTransaction getTransactionByRefund(EdsPaymentRefund paymentRefund) {
        return (EdsPaymentRefundTransaction) findSingle("from EdsPaymentRefundTransaction oupt where (oupt.deleted is null or oupt.deleted = false ) and oupt.paymentRefund = ? ", paymentRefund);
    }

    @Override
    public void deleteOverPaymentTransaction(EdsOverPayment overPayment) {
        update("update EdsOverPaymentTransaction oup set deleted=true where oup.overPayment.objectID = ?", overPayment.getObjectID());
    }

    @Override
    public EdsInventoryTransaction getTransactionByInventory(EdsItem inventory) {
        return (EdsInventoryTransaction) findSingle("from EdsInventoryTransaction it where (it.deleted is null or it.deleted is false) and it.inventory = ?", inventory);
    }

    @Override
    public EdsStockAdjustmentTransaction getTransactionByStockAdjustment(EdsStockAdjustment stockAdjustment) {
        return (EdsStockAdjustmentTransaction) findSingle("from EdsStockAdjustmentTransaction sat where sat.adjustment = ?", stockAdjustment);
    }

    @Override
    public EdsStockTransferTransaction getTransactionByStockTransfer(EdsStockTransfer stockTransfer) {
        return (EdsStockTransferTransaction) findSingle("from EdsStockTransferTransaction stt where stt.stockTransfer = ?", stockTransfer);
    }

    public EdsExpenseTransaction getTransactionByExpense(EdsExpenseReport expenseReport) {
        return (EdsExpenseTransaction) findSingle("from EdsExpenseTransaction et where et.expenseReport = ?", expenseReport);
    }

    @Override
    public EdsExpensePaymentTransaction getTransactionByExpensePayment(EdsExpensePayment expensePayment) {
        return (EdsExpensePaymentTransaction) findSingle("from EdsExpensePaymentTransaction ept where ept.expensePayment =?", expensePayment);
    }

    public void deleteTransactionItems(Integer transactionId) {
        //clear old transaction value from account/client/supplier balances
        //setChangedAccountsForRecalculate(transactionId);
        update("delete from EdsTransactionItem t where t.transaction.objectID= '" + transactionId + "' or t.transaction.objectID is null ");

    }

    @Override
    public void deleteCOGSTransactionItems(Integer transactionId, Integer inventoryId, Integer transactionItemId) {
        update("delete from EdsTransactionItem t where t.isCogsItem = true and t.transaction.objectID = " + transactionId + " and t.inventoryId = " + inventoryId + " and (t.itemId is null or t.itemId = " + transactionItemId + ")");
    }

    @Override
    public void deleteStockTransferTransactionItems(Integer transactionId, Integer inventoryId, Integer transactionItemId) {
        update("delete from EdsTransactionItem t where t.isCogsItem <> true and t.transaction.objectID = " + transactionId + " and t.inventoryId = " + inventoryId + " and (t.stockAdjustmentItem.objectID is null or t.stockAdjustmentItem.objectID = " + transactionItemId + ")");
    }

    @Override
    public BigDecimal getCOGSAmountOfStockTransferItem(Integer transactionId, Integer inventoryId, Integer transactionItemId) {
        return (BigDecimal) findSingle("select sum(coalesce(ti.debit,0.0)) from EdsTransactionItem ti where ti.isCogsItem <> true and ti.transaction.objectID = " + transactionId + " and ti.inventoryId = " + inventoryId + " and ti.stockAdjustmentItem.objectID = " + transactionItemId);
    }

    public void deleteInvoicePaymentTransaction(EdsInvoicePayment invoicePayment) {
        EdsInvoicePaymentTransaction transaction = getTransactionByPayment(invoicePayment);
        setChangedAccountsForRecalculate(transaction);
        update("update EdsInvoicePaymentTransaction ipt set deleted=true where ipt.invoicePayment.objectID = ?", invoicePayment.getObjectID());
    }

    public void deleteExpenseReportTransaction(EdsExpenseReport expenseReport) {
        EdsExpenseTransaction transaction = getTransactionByExpense(expenseReport);
        setChangedAccountsForRecalculate(transaction);
        // This query will delete both the original and reversed transactions.
        update("update EdsExpenseTransaction ext set deleted=true where ext.expenseReport.objectID = ?", expenseReport.getObjectID());
    }

    public void deleteExpensePaymentTransaction(EdsExpensePayment expensePayment) {
        EdsExpensePaymentTransaction transaction = getTransactionByExpensePayment(expensePayment);
        setChangedAccountsForRecalculate(transaction);
        // This query will delete both the original and reversed payment transactions.
        update("update EdsExpensePaymentTransaction ept set deleted=true where ept.expensePayment.objectID = ?", expensePayment.getObjectID());
    }

    public void deleteAdditionalPaymentTransaction(Integer paymentDeductionId) {
        update("update EdsAdditionalPaymentTransaction apt set deleted=true where apt.paymentDeduction.objectID = ?", paymentDeductionId);
    }

    public List<EdsTransactionItem> getPaymentTransactionsByReconsileStatus(FindMatchFilterData filterData) {
        boolean isMultiCurrency = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MULTICURRENCY_ENABLED);
        Map<String, Object> map = new HashMap<>();
        map.put("accountID", filterData.getGlAccountID());
        StringBuilder sb = new StringBuilder();
        sb.append("from EdsTransactionItem ti left join fetch ti.transaction where ti.account.objectID=:accountID and ").append(ServerUtils.checkForDeleted("ti.transaction.deleted"));
        if (filterData.isReconsiled()) {
            sb.append(" and ti.reconcileStatus = '" + RECONCILED + "'");
        } else {
            sb.append(" and (ti.reconcileStatus is null or ti.reconcileStatus != '" + RECONCILED + "')");
        }
        if (filterData.getSearchKey() != null && !filterData.getSearchKey().trim().isEmpty()) {
            String preparedSearchKey = (filterData.getSearchKey().trim() + " ").replace("'", "''").replace(" ", "%").toLowerCase();
            sb.append(" and lower(ti.transaction.name) like '").append(preparedSearchKey).append("' ");
        }
        if (filterData.getTransactionAmount() != null) {
            sb.append(" and (");
            if (filterData.isDebitCredit()) {
                sb.append(" ti.debit =:amount");
            } else {
                sb.append(" ti.credit =:amount");
            }
            if (isMultiCurrency) {
                sb.append(" or ");
                if (filterData.isDebitCredit()) {
                    sb.append(" ti.foreignDebit =:amount");
                } else {
                    sb.append(" ti.foreignCredit =:amount");
                }
            }
            sb.append(" ) ");
            map.put("amount", filterData.getTransactionAmount());
        } else if (filterData.getCredit() != null) {
            sb.append(" and ( ");
            sb.append(" ti.credit =:credit");
            if (isMultiCurrency) {
                sb.append(" or ti.foreignCredit =:credit");
            }
            sb.append(" ) ");
            map.put("credit", filterData.getCredit());
        } else if (filterData.getDebit() != null) {
            sb.append(" and ( ");
            sb.append(" ti.debit =:debit");
            if (isMultiCurrency) {
                sb.append(" or ti.foreignDebit =:debit");
            }
            sb.append(" ) ");
            map.put("debit", filterData.getDebit());
        } else if (filterData.getStartAmount() != null || filterData.getEndAmount() != null) {
            if (filterData.isDebitCredit()) {
                if (filterData.getStartAmount() != null) {
                    sb.append(" and ( ");
                    sb.append(" ti.debit >= :startAmount");
                    if (isMultiCurrency) {
                        sb.append(" or ti.foreignDebit >= :startAmount");
                    }
                    sb.append(" ) ");
                    map.put("startAmount", filterData.getStartAmount());
                }
                if (filterData.getEndAmount() != null) {
                    sb.append(" and ( ");
                    sb.append(" ti.debit <= :endAmount");
                    if (isMultiCurrency) {
                        sb.append(" or ti.foreignDebit <= :endAmount");
                    }
                    sb.append(" ) ");
                    map.put("endAmount", filterData.getEndAmount());
                }
            } else {
                if (filterData.getStartAmount() != null) {
                    sb.append(" and ( ");
                    sb.append(" ti.credit >= :startAmount");
                    if (isMultiCurrency) {
                        sb.append(" or ti.foreignCredit >= :startAmount");
                    }
                    sb.append(" ) ");
                    map.put("startAmount", filterData.getStartAmount());
                }
                if (filterData.getEndAmount() != null) {
                    sb.append(" and ( ");
                    sb.append(" ti.credit <= :endAmount");
                    if (isMultiCurrency) {
                        sb.append(" or ti.foreignCredit <= :endAmount");
                    }
                    sb.append(" ) ");
                    map.put("endAmount", filterData.getEndAmount());
                }
            }
        }
        if (filterData.getStartDate() != null || filterData.getEndDate() != null) {
            if (filterData.getStartDate() != null) {
                sb.append(" and ti.transaction.journalDate >= :startDate ");
                map.put("startDate", filterData.getStartDate().getNonConvertedDate());
            }
            if (filterData.getEndDate() != null) {
                sb.append(" and ti.transaction.journalDate <= :endDate ");
                map.put("endDate", filterData.getEndDate().getNonConvertedDate());
            }
        }
        sb.append(" order by ti.transaction.journalDate desc ");
        return (List<EdsTransactionItem>) findByNamedParams(sb.toString(), map);
    }

    public List<EdsTransactionItem> getTransactionItemsByAccountAndDate(Integer accountID, Date from, Date to, boolean debit) {
        Map<String, Object> map = new HashMap<>();
        map.put("accountID", accountID);
        map.put("from", from);
        map.put("to", to);
        String sb = ("select ti from EdsTransactionItem ti left join fetch ti.transaction t left join t.bankStatementItem bsi where ti.account.objectID=:accountID " +
                " and (ti.reconcileStatus is null or ti.reconcileStatus = '" + UNRECONCILED + "')") +
                " and t.journalDate between :from and :to and " + (debit
                ? "ti.debit is not null"
                : "ti.credit is not null") +
                " and (bsi.uploadedFileDeleted is null or bsi.uploadedFileDeleted=false)" +
                " and " + ServerUtils.checkForDeleted("ti.transaction.deleted");
        return (List<EdsTransactionItem>) findByNamedParams(sb, map);
    }

    @Override
    public EdsPayslipTransaction getTransactionByPayslip(Integer payslipId) {
        return (EdsPayslipTransaction) findSingle("select pt from EdsPayslipTransaction pt where pt.payslip.objectID = ?", payslipId);
    }

    @Override
    public EdsSinglePayrunTransaction getTransactionByPayrun(Integer payrunID) {
        return (EdsSinglePayrunTransaction) findSingle("select pt from EdsSinglePayrunTransaction pt where pt.payrun.objectID = ?", payrunID);
    }

    @Override
    public EdsCashAdvanceTransaction getTransactionByCashAdvance(Integer cashAdvanceID) {
        return (EdsCashAdvanceTransaction) findSingle("select cat from EdsCashAdvanceTransaction cat where cat.cashAdvance.objectID = ? and (cat.deleted is null or cat.deleted <> true)", cashAdvanceID);
    }

    @Override
    public EdsPayrunPaymentTransaction getTransactionByPayrunPayment(Integer paymentItemID) {
        return (EdsPayrunPaymentTransaction) findSingle("select ppt from EdsPayrunPaymentTransaction ppt where ppt.paymentItem.objectID = ?", paymentItemID);
    }

    @Override
    public EdsPayrollPaymentTransaction getTransactionByPayrollPayment(Integer paymentItemID) {
        return (EdsPayrollPaymentTransaction) findSingle("select ppt from EdsPayrollPaymentTransaction ppt where ppt.paymentItem.objectID = ?", paymentItemID);
    }

    @Override
    public BigDecimal getCashAdvanceTransctionPaidAmount(ListingFilterParameter lfp) {
        String sql = "SELECT sum(ti.debit " +
                "FROM " + getCompanyId() + ".transactionitem ti " +
                "INNER JOIN " + getCompanyId() + ".transaction t ON ti.transactionid = t.id " +
                "WHERE (t.deleted IS NULL OR t.deleted <> TRUE) AND t.cashadvance_id = ?";
        return (BigDecimal) findNativeSingle(sql, lfp.getObjectId());
    }

    @Override
    public EdsCustomerTransaction getCustomerOpeningBalanceTransaction(Integer clientID) {
        return (EdsCustomerTransaction) findSingle("select ct from EdsCustomerTransaction ct where ct.client.objectID = ? and ct.deleted=false", clientID);
    }

    @Override
    public EdsSupplierTransaction getSupplierOpeningBalanceTransaction(Integer supplierID) {
        return (EdsSupplierTransaction) findSingle("select st from EdsSupplierTransaction st where st.supplier.objectID = ? and st.deleted=false", supplierID);
    }

    @Override
    public EdsFixedAssetTransaction getFixedAssetTransaction(Integer fixedAssetID) {
        return (EdsFixedAssetTransaction) findSingle("select fat from EdsFixedAssetTransaction fat where fat.fixedAsset.objectID = ? order by fat.objectID", fixedAssetID);
    }

    @Override
    public EdsDisposalTransaction getFixedAssetDisposalTransaction(Integer fixedAssetID) {
        return (EdsDisposalTransaction) findSingle("select fdt from EdsDisposalTransaction fdt " +
                "where " + ServerUtils.checkForDeleted("fdt.deleted") +
                "and fdt.reversalTransaction is null and fdt.fixedAsset.objectID = ? order by fdt.objectID desc", fixedAssetID);
    }

    @Override
    public List<Object[]> getBalanceDataByDateAndAccountCategory(String category, Date fromDate, Date toDate, String departmentAndTreeChildIDs, Integer projectID) {
        //karochche
        String sql = "select" +
                " sum(CASE WHEN ti.accountid is not null and a.foreignAccount = true THEN ti.foreignDebit ELSE ti.debit END) totalDebit," +
                " sum(CASE WHEN ti.accountid is not null and a.foreignAccount = true THEN ti.foreignCredit ELSE ti.credit END) totalcredit," +
                " a.id accountid, a.name accountname ";
        sql += "FROM " + getCompanyId() + ".transactionitem ti \n";
        sql += "JOIN " + getCompanyId() + ".transaction tr ON tr.id = ti.transactionid \n";
        sql += "JOIN " + getCompanyId() + ".account a ON a.id = ti.accountid \n";
        sql += "left JOIN " + getPublic() + ".accounttype at ON at.id = a.accounttypeid \n";
        sql += "left join " + getCompanyId() + ".invoice i on tr.invoiceid=i.id  \n";
        sql += "left join " + getCompanyId() + ".manualjournal mj on tr.manualjournalid=mj.id  \n";
        sql += "left join " + getCompanyId() + ".expensereport expr on tr.expensereportid=expr.id  \n";
        sql += "left join " + getCompanyId() + ".expensePayments expay on tr.expensePaymentId=expay.id  \n";
        sql += "left join " + getCompanyId() + ".expensereport payexp on expay.expenseReportId=payexp.id  \n";
        sql += "left join " + getCompanyId() + ".quote quo on tr.purchaseorder_id=quo.id  \n";
        sql += "left join " + getCompanyId() + ".spendreceivemoney spr on tr.banktransferid=spr.id  \n";
        sql += "left join " + getCompanyId() + ".adjustment_item adjit on ti.stock_adjustment_item_id=adjit.id  \n";
        sql += "left join " + getCompanyId() + ".quoteitem poitem on tr.purchaseorder_id is not null and ti.itemid=poitem.id \n";
        sql += "left join " + getCompanyId() + ".quoteitem soitem on tr.saleorder_id is not null and ti.itemid=soitem.id \n";
        sql += "left join " + getCompanyId() + ".invoiceitem invitem on tr.invoiceid is not null and ti.itemid=invitem.id \n";
        sql += "left join " + getCompanyId() + ".shipping_data_items shi on tr.shippingDataId is not null and ti.itemid = shi.id \n";
        sql += "left join " + getCompanyId() + ".quoteitem shitem on shi.quoteItemId = shitem.id \n";
        sql += "left join " + getCompanyId() + ".adjustment_item adjitem on tr.stockTransferId is not null and ti.itemid=adjitem.id \n";
        sql += "left join " + getCompanyId() + ".payslipTableItem pti on tr.payrun_id is not null and tr.payrun_id=pti.id \n";
        sql += "left join " + getCompanyId() + ".payslipTable pt on pti.payslipTable_id is not null and pti.payslipTable_id=pt.id \n";
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
            sql += "left join " + getCompanyId() + ".project prj on coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(ti.project_id,expr.projectId),payexp.projectId),spr.projectid),adjit.projectid),adjitem.projectid),mj.projectid),poitem.project_id),soitem.project_id),invitem.project_id),shitem.project_id) = prj.id \n";
        } else {
            sql += "left join " + getCompanyId() + ".project prj on coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(ti.project_id,expr.projectId),payexp.projectId),spr.projectid),adjit.projectid),adjitem.projectid),mj.projectid),i.relatedproject_id),quo.relatedproject_id) = prj.id \n";
        }
        sql += "where tr.deleted is not true  and at.category = ? and tr.journalDate between ? and ? ";
        if (departmentAndTreeChildIDs != null && departmentAndTreeChildIDs.trim().length() > 0) {
            sql += " and (";
            sql += "ti.department_id in (" + departmentAndTreeChildIDs + ") \n";
            sql += "or adjit.departmentid in (" + departmentAndTreeChildIDs + ") \n";
            sql += "or adjitem.departmentid in (" + departmentAndTreeChildIDs + ") \n";
            sql += "or (tr.invoiceid is not null and invitem.departmentid in (" + departmentAndTreeChildIDs + ")) \n";
            sql += "or (tr.purchaseorder_id is not null and poitem.departmentid in (" + departmentAndTreeChildIDs + ")) \n";
            sql += "or (tr.saleorder_id is not null and soitem.departmentid in (" + departmentAndTreeChildIDs + ")) \n";
            sql += "or (tr.shippingDataId is not null and shitem.departmentid in (" + departmentAndTreeChildIDs + ")) \n";
            sql += ") \n";
        }
        if (projectID != null) {
            sql += " AND (";
            sql += "ti.project_id = " + projectID + " \n";
            sql += "or (tr.expensereportid is not null and expr.projectId=" + projectID + ") \n";
            sql += "or (tr.expensePaymentId is not null and payexp.projectId=" + projectID + ") \n";
            sql += "or (tr.banktransferid is not null and spr.projectid=" + projectID + ") \n";
            sql += "or (tr.adjustment_id is not null and adjit.projectid=" + projectID + ") \n";
            sql += "or (tr.stockTransferId is not null and adjitem.projectid=" + projectID + ")  \n";
            sql += "or (pt.projectid=" + projectID + ") \n";
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MANUAL_JOURNAL_PM_TO_HEAD_ENABLED)) {
                sql += "or (tr.manualjournalid is not null and mj.projectid=" + projectID + ")  \n";
            }
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
                sql += "or (tr.purchaseorder_id is not null and poitem.project_id=" + projectID + ")  \n";
                sql += "or (tr.saleorder_id is not null and soitem.project_id=" + projectID + ")  \n";
                sql += "or (tr.invoiceid is not null and invitem.project_id=" + projectID + ")  \n";
                sql += "or (tr.shippingDataId is not null and shitem.project_id=" + projectID + ")  \n";
            } else {
                sql += "or (tr.invoiceid is not null and i.relatedproject_id=" + projectID + ") \n";
                sql += "or (tr.purchaseorder_id is not null and quo.relatedproject_id=" + projectID + ") \n";
            }
            sql += ") \n";
        } else if (!ServerUtils.hasPermission(PermissionConstants.PM_SEE_ALL_PROJECTS) && genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MANUAL_JOURNAL_PM_TO_HEAD_ENABLED)) {
            sql += " and prj.managerid = " + ((EdsUser) ServerSecurityContext.getInstance().getUser()).getObjectID();
        }
        sql += "  group by a.id, a.name";
        return findNative(sql, category, fromDate, toDate);
    }


    public Object[] getBalanceDataByDateAndAccountCategoryByDepartment(String category, Date fromDate, Date toDate, String departmentAndTreeChildIDs, Integer projectID) {
        StringBuilder sql = new StringBuilder();
        sql.append("select sum(CASE WHEN ti.accountid is not null and at.code = 'BANK' and a.foreignAccount = true THEN ti.foreignDebit ELSE ti.debit END) debit, \n");
        sql.append("sum(CASE WHEN ti.accountid is not null and at.code = 'BANK' and a.foreignAccount = true THEN ti.foreignCredit ELSE ti.credit END) credit \n");
        getAllJoinedTransaction(category, fromDate, toDate, departmentAndTreeChildIDs, projectID, sql);
        return (Object[]) findNativeSingle(sql.toString());

//        TODO eski holati shu pastdagidek bulgan man tepada native ga utkazdim
//        return (Object[]) findSingle("select" +
//                " sum(CASE WHEN ti.account is not null and ti.account.accountType.code = 'BANK' and ti.account.foreignAccount = true THEN ti.foreignDebit ELSE ti.debit END)," +
//                " sum(CASE WHEN ti.account is not null and ti.account.accountType.code = 'BANK' and ti.account.foreignAccount = true THEN ti.foreignCredit ELSE ti.credit END)" +
//                " from EdsTransactionItem ti where  " + ServerUtils.checkForDeleted("ti.transaction.deleted") + " and ti.account.accountType.category = ? " +
//                " and ti.transaction.journalDate between ? and ? " +
//                ((departmentAndTreeChildIDs != null && departmentAndTreeChildIDs.trim().length() > 0) ? " and ti.department.objectID in (" + departmentAndTreeChildIDs + ") " : " "), category, fromDate, toDate);
    }

    private void getAllJoinedTransaction(String category, Date fromDate, Date toDate, String departmentAndTreeChildIDs, Integer projectID, StringBuilder sql) {
        sql.append(" FROM ").append(getCompanyId()).append(".transactionitem ti \n");
        sql.append("JOIN ").append(getCompanyId()).append(".transaction t ON t.id = ti.transactionid \n");
        sql.append("JOIN ").append(getCompanyId()).append(".account a ON a.id = ti.accountid \n");
        sql.append("INNER JOIN ").append(getPublic()).append(".accounttype at ON at.id = a.accounttypeid \n");

        sql.append("left join ").append(getCompanyId()).append(".invoice i on t.invoiceid=i.id \n");
        sql.append("left join ").append(getCompanyId()).append(".manualjournal mj on t.manualjournalid=mj.id \n");
        sql.append("left join ").append(getCompanyId()).append(".expensereport expr on t.expensereportid=expr.id \n");
        sql.append("left join ").append(getCompanyId()).append(".expensePayments expay on t.expensePaymentId=expay.id  \n");
        sql.append("left join ").append(getCompanyId()).append(".expensereport payexp on expay.expenseReportId=payexp.id \n");
        sql.append("left join ").append(getCompanyId()).append(".quote quo on t.purchaseorder_id=quo.id \n");
        sql.append("left join ").append(getCompanyId()).append(".spendreceivemoney spr on t.banktransferid=spr.id \n");
        sql.append("left join ").append(getCompanyId()).append(".adjustment_item adjit on ti.stock_adjustment_item_id=adjit.id \n");
        sql.append("left join ").append(getCompanyId()).append(".quoteitem poitem on t.purchaseorder_id is not null and ti.itemid=poitem.id \n");
        sql.append("left join ").append(getCompanyId()).append(".quoteitem soitem on t.saleorder_id is not null and ti.itemid=soitem.id \n");
        sql.append("left join ").append(getCompanyId()).append(".invoiceitem invitem on t.invoiceid is not null and ti.itemid=invitem.id \n");
        sql.append("left join ").append(getCompanyId()).append(".shipping_data_items shi on t.shippingDataId is not null and ti.itemid = shi.id  \n");
        sql.append("left join ").append(getCompanyId()).append(".quoteitem shitem on shi.quoteItemId = shitem.id  \n");
        sql.append("left join ").append(getCompanyId()).append(".adjustment_item adjitem on t.stockTransferId is not null and ti.itemid=adjitem.id \n");
        sql.append("left join ").append(getCompanyId()).append(".payslipTableItem pti on t.payrun_id is not null and t.payrun_id=pti.id \n");
        sql.append("left join ").append(getCompanyId()).append(".payslipTable pt on pti.payslipTable_id is not null and pti.payslipTable_id=pt.id \n");
        if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
            sql.append(" left join ").append(getCompanyId()).append(".project prj on coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(ti.project_id,expr.projectId),payexp.projectId),spr.projectid),adjit.projectid),adjitem.projectid),mj.projectid),poitem.project_id),soitem.project_id),invitem.project_id),shitem.project_id) = prj.id \n");
        } else {
            sql.append(" left join ").append(getCompanyId()).append(".project prj on coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(coalesce(ti.project_id,expr.projectId),payexp.projectId),spr.projectid),adjit.projectid),adjitem.projectid),mj.projectid),i.relatedproject_id),quo.relatedproject_id) = prj.id \n");
        }


        sql.append("WHERE t.deleted is not true \n");
        sql.append(" and t.journalDate between '").append(fromDate).append("' and '").append(toDate).append("' \n");
        sql.append(" and at.category ='").append(category).append("' \n");
        if (departmentAndTreeChildIDs != null && departmentAndTreeChildIDs.trim().length() > 0) {
            sql.append(" and (");
            sql.append("ti.department_id in (").append(departmentAndTreeChildIDs).append(") \n");
            sql.append("or adjit.departmentid in (").append(departmentAndTreeChildIDs).append(") \n");
            sql.append("or adjitem.departmentid in (").append(departmentAndTreeChildIDs).append(") \n");
            sql.append("or (t.invoiceid is not null and invitem.departmentid in (").append(departmentAndTreeChildIDs).append(")) \n");
            sql.append("or (t.purchaseorder_id is not null and poitem.departmentid in (").append(departmentAndTreeChildIDs).append(")) \n");
            sql.append("or (t.saleorder_id is not null and soitem.departmentid in (").append(departmentAndTreeChildIDs).append(")) \n");
            sql.append("or (t.shippingDataId is not null and shitem.departmentid in (").append(departmentAndTreeChildIDs).append(")) \n");
            sql.append(") \n");
        }
        if (projectID != null) {
            sql.append(" AND (");
            sql.append("ti.project_id = ").append(projectID).append(" \n");
            sql.append("or (t.expensereportid is not null and expr.projectId=").append(projectID).append(") \n");
            sql.append("or (t.expensePaymentId is not null and payexp.projectId=").append(projectID).append(") \n");
            sql.append("or (t.banktransferid is not null and spr.projectid=").append(projectID).append(") \n");
            sql.append("or (t.adjustment_id is not null and adjit.projectid=").append(projectID).append(") \n");
            sql.append("or (t.stockTransferId is not null and adjitem.projectid=").append(projectID).append(") \n");
            sql.append("or (pt.projectid=").append(projectID).append(") \n");
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MANUAL_JOURNAL_PM_TO_HEAD_ENABLED)) {
                sql.append("or (t.manualjournalid is not null and mj.projectid=").append(projectID).append(") \n");
            }
            if (genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.PROJECT_IN_LINE_ITEM_ENABLE)) {
                sql.append("or (t.purchaseorder_id is not null and poitem.project_id=").append(projectID).append(") \n");
                sql.append("or (t.saleorder_id is not null and soitem.project_id=").append(projectID).append(") \n");
                sql.append("or (t.invoiceid is not null and invitem.project_id=").append(projectID).append(") \n");
                sql.append("or (t.shippingDataId is not null and shitem.project_id=").append(projectID).append(") \n");
            } else {
                sql.append("or (t.invoiceid is not null and i.relatedproject_id=").append(projectID).append(") \n");
                sql.append("or (t.purchaseorder_id is not null and quo.relatedproject_id=").append(projectID).append(") \n");
            }
            sql.append(") ");
        } else if (!ServerUtils.hasPermission(PermissionConstants.PM_SEE_ALL_PROJECTS) && genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MANUAL_JOURNAL_PM_TO_HEAD_ENABLED)) {
            sql.append(" and prj.managerid = ").append(((EdsUser) ServerSecurityContext.getInstance().getUser()).getObjectID());
        }
    }

    @Override
    public List<EdsAccount> getBalanceSubsidiariesDataByDateAndAccountCategory(String category, Date fromDate, Date toDate, String departmentAndTreeChildIDs, Integer projectID) {

        StringBuilder sql = new StringBuilder();
        sql.append("select a.*, 0 as _clazz \n");
        getAllJoinedTransaction(category, fromDate, toDate, departmentAndTreeChildIDs, projectID, sql);
        sql.append(" group by a.id \n");
        return find(sql.toString(), EdsAccount.class);

//       TODO eski holati shu pastdagidek bulgan man tepada native ga utkazdim
//        return find("select distinct ti.account from EdsTransactionItem ti where "
//                + ServerUtils.checkForDeleted("ti.transaction.deleted")
//                + ((departmentAndTreeChildIDs != null && departmentAndTreeChildIDs.trim().length() > 0) ? " and ti.department.objectID in (" + departmentAndTreeChildIDs + ") " : " ")
//                + " and ti.account.accountType.category = ? and ti.transaction.journalDate between ? and ? ", category, fromDate, toDate);
    }

    @Override
    public BigDecimal[] getListCashOnlyInAccountInPeriodCount(ListingFilterParameter fp, String departmentAndTreeChildIDs) {
        String sql = "select count(ti.id),  \n" +
                "sum(CASE WHEN a.id is not null and at.id is not null and a.foreignAccount = true THEN ti.foreignDebit ELSE ti.debit END) debit,  \n" +
                "sum(CASE WHEN a.id is not null and at.id is not null and a.foreignAccount = true THEN ti.foreignCredit ELSE ti.credit END) credit \n";

        sql = getSqlBodylistCashOnlyInAccountInPeriod(fp, sql, departmentAndTreeChildIDs);
        Object[] resultCashOnly = (Object[]) findNativeSingle(sql);
        BigDecimal[] totalsCashOnly = new BigDecimal[3];
        if (resultCashOnly != null) {
            Long count = resultCashOnly[0] != null ? ((BigInteger) resultCashOnly[0]).longValue() : 0;
            BigDecimal totalDebit = resultCashOnly[1] != null ? (BigDecimal) resultCashOnly[1] : BigDecimal.ZERO;
            BigDecimal totalCredit = resultCashOnly[2] != null ? (BigDecimal) resultCashOnly[2] : BigDecimal.ZERO;

            totalsCashOnly[0] = new BigDecimal(count);
            totalsCashOnly[1] = totalDebit;
            totalsCashOnly[2] = totalCredit;
        }
        return totalsCashOnly;
    }

    @Override
    public BigDecimal[] getListAttendedInAccountInPeriodCount(ListingFilterParameter fp, String departmentAndTreeChildIDs) {
        String sql = "select count(ti.id), " +
                "sum(CASE WHEN a.id is not null and a.foreignAccount = true " + (" and " + !fp.isShowInBase() + " = true ") + " THEN ti.foreignDebit ELSE ti.debit END) debit,  \n" +
                "sum(CASE WHEN a.id is not null and a.foreignAccount = true " + (" and " + !fp.isShowInBase() + " = true ") + " THEN ti.foreignCredit ELSE ti.credit END) credit \n";
        sql = getSqlBodyListAttendedInAccountInPeriod(fp, sql, departmentAndTreeChildIDs);
        Object[] result = (Object[]) findNativeSingle(sql);
        BigDecimal[] totals = new BigDecimal[3];
        if (result != null) {
            Long count = result[0] != null ? ((BigInteger) result[0]).longValue() : 0;
            BigDecimal totalDebit = result[1] != null ? (BigDecimal) result[1] : BigDecimal.ZERO;
            BigDecimal totalCredit = result[2] != null ? (BigDecimal) result[2] : BigDecimal.ZERO;

            totals[0] = new BigDecimal(count);
            totals[1] = totalDebit;
            totals[2] = totalCredit;
        }
        return totals;
    }

    @Override
    public void deleteInventoryTransaction(Integer productID) {
        List<EdsInventoryTransaction> transactions = getInventoryTransactions(productID);
        for (EdsInventoryTransaction transaction : transactions) {
            setChangedAccountsForRecalculate(transaction);
            if (transaction != null) {
                transaction.setDeleted(true);
                update(transaction);
            }
        }
    }

    @Override
    public void deleteTransaction(Integer transactionID) {
        EdsTransaction edsTransaction = get(transactionID);
        if (edsTransaction != null) {
            edsTransaction.setDeleted(true);
            update(edsTransaction);
        }
    }

    @Override
    public void setChangedAccountsForRecalculate(Integer transactionID) {
        setChangedAccountsForRecalculate(get(transactionID));
    }

    private void setChangedAccountsForRecalculate(EdsTransaction transaction) {
        if (true) {
            return;
        }
        if (transaction != null) {
            if (transaction.getClient() != null) {
                EdsCrmAccount crmAccount = transaction.getClient();
//                crmAccount.setBalanceCalculated(false);
                crmAccountManager.update(crmAccount);
            }
            if (transaction.getSupplier() != null) {
                EdsCrmAccount crmAccount = transaction.getSupplier();
//                crmAccount.setBalanceCalculated(false);
                crmAccountManager.update(crmAccount);
            }
            Set<EdsTransactionItem> transactionItems = transaction.getTransactionItems();
            if (transactionItems != null && transactionItems.size() > 0) {
                for (EdsTransactionItem ti : transactionItems) {
                    if (ti.getAccount() != null) {
                        EdsAccount account = ti.getAccount();
//                        account.setBalanceCalculated(false);
                        accountingManager.update(account);
                    }
                    if (ti.getCrmAccount() != null) {
                        EdsCrmAccount crmAccount = ti.getCrmAccount();
//                        crmAccount.setBalanceCalculated(false);
                        crmAccountManager.update(crmAccount);
                    }
                }
            }
        }
    }

    public List<Integer> getCrmAccountUsedCurrencies(ArrayList<Integer> crmAccountIDs, String crmAccountType) {
        String schema = ServerSecurityContext.getInstance().getCompanyId();
        StringBuilder sql = new StringBuilder();
        sql.append("select distinct coalesce(mj.currencyid, coalesce(bt.currencyid,a.currencyid)) currencyid ");
        sql.append(" from \"" + schema + "\".transaction t \n");
        sql.append(" inner join \"" + schema + "\".transactionitem ti on ti.transactionid=t.id \n");
        sql.append(" inner join \"" + schema + "\".account a on a.id=ti.accountid \n");
        sql.append(" left join \"" + schema + "\".manualjournal mj on mj.id = t.manualjournalid \n");
        sql.append(" left join \"" + schema + "\".spendreceivemoney bt on bt.id = t.banktransferid \n");
        sql.append(" where t.deleted<>true ");
        if (CrmAccountItem.CUSTOMER.equals(crmAccountType)) {
            sql.append(" and (a.key in (" + EdsAccount.ACCOUNTS_RECEIVABLE + "," + EdsAccount.UNEARNED_REVENUE + ") or a.groupKey = " + EdsAccount.ACCOUNTS_RECEIVABLE + ") ");
            sql.append(" and (t.clientid in (" + ServerUtils.getAsCommoDelimited(crmAccountIDs, "0", ",") + ") or ti.crmaccount_id in (" + ServerUtils.getAsCommoDelimited(crmAccountIDs, "0", ",") + ")) ");
        } else if (CrmAccountItem.SUPPLIER.equals(crmAccountType)) {
            sql.append(" and (a.key = " + EdsAccount.ACCOUNTS_PAYABLE + " or a.groupKey = " + EdsAccount.ACCOUNTS_PAYABLE + ") ");
            sql.append(" and (t.supplierid in (" + ServerUtils.getAsCommoDelimited(crmAccountIDs, "0", ",") + ") or ti.crmaccount_id in (" + ServerUtils.getAsCommoDelimited(crmAccountIDs, "0", ",") + ")) ");
        }
        return findNative(sql.toString());
    }

    public LinkedHashMap<BigDecimal, BigDecimal> getCrmAccountEarlyBalance(ArrayList<Integer> crmAccountIDs, String from, String crmAccountType, Integer currencyID, boolean isBaseCurrency) {
        boolean isMultiCurrencyCrmAccountBalance = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MULTIPLE_CURRENCY_CRM_ACCOUNT_BALANCE);

        String schema = ServerSecurityContext.getInstance().getCompanyId();
        StringBuilder queryBuilder = new StringBuilder();
        if (CrmAccountItem.CUSTOMER.equals(crmAccountType)) {
            if (isBaseCurrency) {
                queryBuilder.append("select sum(r.amount) amountinbase, sum(r.amount) amount from (select SUM(COALESCE(ti.debit, 0) - COALESCE(ti.credit, 0)) as amount ");
            } else {
                if (isMultiCurrencyCrmAccountBalance) {
                    queryBuilder.append("select sum(r.amountinbase) amountinbase, sum(r.amount) amount from ( select sum(COALESCE(ti.debit, 0)-COALESCE(ti.credit, 0)) amountinbase, SUM( \n")
                            .append("(case when t.manualjournalid is null is not null and mj.currencyid != a.currencyid then (mj.exchangerate * COALESCE(ti.debit, 0))\n" +
                                    "\t when t.banktransferid is not null and bt.currencyid != a.currencyid then (bt.exchangerate * COALESCE(ti.debit, 0)) else COALESCE(ti.foreigndebit, 0) end) \n")
                            .append(" - \n")
                            .append("(case when t.manualjournalid is not null and mj.currencyid != a.currencyid then (mj.exchangerate * COALESCE(ti.credit, 0))\n" +
                                    "\t when t.banktransferid is not null and bt.currencyid != a.currencyid then (bt.exchangerate * COALESCE(ti.credit, 0)) else COALESCE(ti.foreigncredit, 0) end)) as amount \n");
                } else {
                    queryBuilder.append("select sum(r.amount) amount from (select SUM(COALESCE(ti.foreigndebit, 0) - COALESCE(ti.foreigncredit, 0)) as amount ");
                }

            }

            queryBuilder.append(" from \"").append(schema).append("\".transaction t")
                    .append(" inner join \"").append(schema).append("\".transactionitem ti on ti.transactionid=t.id")
                    .append(" inner join \"").append(schema).append("\".account a on a.id=ti.accountid")
                    .append(" left join \"").append(schema).append("\".invoice i on i.id=t.invoiceid")
                    .append(" left join \"").append(schema).append("\".invoicepayments p on p.id=t.invoicepaymentid")
                    .append(" left join \"").append(schema).append("\".invoice pi on pi.id=p.invoiceId")
                    .append(" left join \"").append(schema).append("\".invoice cn on cn.id=p.creditNoteId")
                    .append(" left join \"").append(schema).append("\".fixedasset fa on fa.id = t.fixedassetid ")
                    .append(" left join \"").append(schema).append("\".manualjournal mj on mj.id = t.manualjournalid ")
                    .append(" left join \"").append(schema).append("\".spendreceivemoney bt on bt.id = t.banktransferid ")
                    .append(" where t.deleted<>true and (a.key in (").append(EdsAccount.ACCOUNTS_RECEIVABLE).append(",").append(EdsAccount.UNEARNED_REVENUE).append(") or a.groupKey = " + EdsAccount.ACCOUNTS_RECEIVABLE + ") ");

            if (currencyID != null) {
                if (isMultiCurrencyCrmAccountBalance) {
                    if (isBaseCurrency) {
                        queryBuilder.append(" and (a.currencyid is null or a.currencyid=").append(currencyID).append(") and (t.manualjournalid is null or mj.currencyid = ").append(currencyID).append(") and (t.banktransferid is null or bt.currencyid = ").append(currencyID).append(") \n");
                    } else {
                        queryBuilder.append(" and (a.currencyid = ").append(currencyID).append(" or mj.currencyid = ").append(currencyID).append(" or bt.currencyid = ").append(currencyID).append(") \n");
                    }
                } else {
                    queryBuilder.append(isBaseCurrency
                            ? " and (a.currencyid is null or a.currencyid=" + currencyID + ")"
                            : " and a.currencyid=" + currencyID);
                }
            }

            queryBuilder.append(" and t.journaldate<'").append(from).append("'")
                    .append(" and (t.clientid in (").append(ServerUtils.getAsCommoDelimited(crmAccountIDs, "0")).append(") or ti.crmaccount_id in (").append(ServerUtils.getAsCommoDelimited(crmAccountIDs, "0")).append(")) ")
                    .append(" group by t.id, t.reversalid, t.dtype, t.journaldate, t.journalid, p.reference, i.iscreditnote, i.id, i.number, cn.isCreditNote, p.id, pi.number, p.type")
                    .append(" order by t.journalid) as r");

        } else if (CrmAccountItem.SUPPLIER.equals(crmAccountType)) {
            if (isBaseCurrency) {
                queryBuilder.append("select sum(r.amount) amountinbase, sum(r.amount) amount from (select SUM(COALESCE(ti.credit, 0) - COALESCE(ti.debit, 0)) as amount ");
            } else {
                if (isMultiCurrencyCrmAccountBalance) {
                    queryBuilder.append("select sum(r.amountinbase) amountinbase, sum(r.amount) amount from ( select sum(COALESCE(ti.credit, 0)-COALESCE(ti.debit, 0)) amountinbase,SUM( \n")
                            .append("(case when t.manualjournalid is not null and mj.currencyid != a.currencyid then (mj.exchangerate * COALESCE(ti.credit, 0))\n" +
                                    "\t when t.banktransferid is not null and bt.currencyid != a.currencyid then (bt.exchangerate * COALESCE(ti.credit, 0)) else COALESCE(ti.foreigncredit, 0) end) \n")
                            .append(" - \n")
                            .append("(case when t.manualjournalid is not null and mj.currencyid != a.currencyid then (mj.exchangerate * COALESCE(ti.debit, 0))\n" +
                                    "\t when t.banktransferid is not null and bt.currencyid != a.currencyid then (bt.exchangerate * COALESCE(ti.debit, 0)) else COALESCE(ti.foreigndebit, 0) end)) as amount \n");
                } else {
                    queryBuilder.append("select sum(r.amount) amount from (select SUM(COALESCE(ti.foreigncredit, 0) - COALESCE(ti.foreigndebit, 0)) as amount ");
                }

            }

            queryBuilder.append(" from \"").append(schema).append("\".transaction t")
                    .append(" inner join \"").append(schema).append("\".transactionitem ti on ti.transactionid=t.id")
                    .append(" inner join \"").append(schema).append("\".account a on a.id=ti.accountid")
                    .append(" left join \"").append(schema).append("\".invoice i on i.id=t.invoiceid ")
                    .append(" left join \"").append(schema).append("\".invoicepayments p on p.id=t.invoicepaymentid ")
                    .append(" left join \"").append(schema).append("\".invoice pi on pi.id=p.invoiceId")
                    .append(" left join \"").append(schema).append("\".invoice cn on cn.id=p.creditNoteId")
                    .append(" left join \"").append(schema).append("\".fixedasset fa on fa.id = t.fixedassetid ")
                    .append(" left join \"").append(schema).append("\".manualjournal mj on mj.id = t.manualjournalid ")
                    .append(" left join \"").append(schema).append("\".spendreceivemoney bt on bt.id = t.banktransferid ")
                    .append(" where t.deleted<>true and (a.key in (").append(EdsAccount.ACCOUNTS_PAYABLE).append(",").append(EdsAccount.PREPAID_EXPANSES).append(") or a.groupKey = " + EdsAccount.ACCOUNTS_PAYABLE + ") ");

            if (currencyID != null) {
                if (isMultiCurrencyCrmAccountBalance) {
                    if (isBaseCurrency) {
                        queryBuilder.append(" and (a.currencyid is null or a.currencyid=").append(currencyID).append(") and (t.manualjournalid is null or mj.currencyid = ").append(currencyID).append(") and (t.banktransferid is null or bt.currencyid = ").append(currencyID).append(") \n");
                    } else {
                        queryBuilder.append(" and (a.currencyid = ").append(currencyID).append(" or mj.currencyid = ").append(currencyID).append(" or bt.currencyid = ").append(currencyID).append(") \n");
                    }
                } else {
                    queryBuilder.append(isBaseCurrency
                            ? " and (a.currencyid is null or a.currencyid=" + currencyID + ")"
                            : " and a.currencyid=" + currencyID);
                }

            }

            queryBuilder.append(" and t.journaldate<'").append(from).append("'")
                    .append(" and (t.supplierid in (").append(ServerUtils.getAsCommoDelimited(crmAccountIDs, "0")).append(") or ti.crmaccount_id in (").append(ServerUtils.getAsCommoDelimited(crmAccountIDs, "0")).append(")) ")
                    .append(" group by t.id, t.reversalid, t.dtype, t.journaldate, t.journalid, p.reference, i.iscreditnote, i.id, i.number, cn.isCreditNote, p.id, pi.number, p.type")
                    .append(" order by t.journalid) as r");
        }
        Object[] result = (Object[]) findNativeSingle(queryBuilder.toString());

        BigDecimal key = result[0] == null ? BigDecimal.ZERO : (BigDecimal) result[0];
        BigDecimal value = result[1] == null ? BigDecimal.ZERO : (BigDecimal) result[1];
        return new LinkedHashMap<>(Map.of(key, value));
    }

    @Override
    public BigDecimal getCrmAccountPrevPageBalance(ArrayList<Integer> crmAccountIDs, String from, String to, String crmAccountType, Integer currencyID, boolean isBaseCurrency, ListingFilterParameter fp) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("select sum(t.amount) from (")
                .append(getCrmAccountBalanceQuery(crmAccountIDs, from, to, crmAccountType, currencyID, isBaseCurrency));

        if (fp.getLimit() != 0) {
            queryBuilder.append(" OFFSET ").append(fp.getStart()).append(" LIMIT ").append(fp.getLimit());
        }
        queryBuilder.append(") t");

        Object result = findNativeSingle(queryBuilder.toString());
        if (result == null) {
            return new BigDecimal(0);
        }
        return (BigDecimal) result;
    }

    public List<CrmAccountBalanceItem> getCrmAccountBalance(ArrayList<Integer> crmAccountIDs, String from, String to, String crmAccountType, Integer currencyID, boolean isBaseCurrency, ListingFilterParameter fp, BigDecimal exchangeRate) {
        StringBuilder queryBuilder = new StringBuilder();

        queryBuilder.append(getCrmAccountBalanceQuery(crmAccountIDs, from, to, crmAccountType, currencyID, isBaseCurrency));

        if (fp.getLimit() != 0) {
            queryBuilder.append(" OFFSET ").append(fp.getStart()).append(" LIMIT ").append(fp.getLimit());
        }

        List<CrmAccountBalanceItem> items = new LinkedList<>(jdbcSpringManager.getSimpleJdbcTemplate().query(queryBuilder.toString(), BeanPropertyRowMapper.newInstance(CrmAccountBalanceItem.class)));
        for (CrmAccountBalanceItem cabi : items) {
            if (cabi.getDate() != null) {
                cabi.setDate_nc(new DateNonConvertable(cabi.getDate()));
            }
            if (exchangeRate != null) {
                if (cabi.getAmount() != null) {
                    cabi.setAmount(cabi.getAmount().multiply(exchangeRate.setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP)));
                }
                if (cabi.getDebit() != null) {
                    cabi.setDebit(cabi.getDebit().multiply(exchangeRate.setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP)));
                }
                if (cabi.getCredit() != null) {
                    cabi.setCredit(cabi.getCredit().multiply(exchangeRate.setScale(ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP)));
                }
            }
        }

        return items;
    }

    @Override
    public Integer getCrmAccountBalanceCount(ArrayList<Integer> crmAccountIDs, String from, String to, String crmAccountType, Integer currencyID, boolean isBaseCurrency) {
        String queryBuilder = "select count(t.objectID) from (" +
                getCrmAccountBalanceQuery(crmAccountIDs, from, to, crmAccountType, currencyID, isBaseCurrency) +
                ") t";

        Object result = findNativeSingle(queryBuilder);
        if (result == null) {
            return 0;
        }
        return ((BigInteger) result).intValue();
    }

    private String getCrmAccountBalanceQuery(ArrayList<Integer> crmAccountIDs, String from, String to, String crmAccountType, Integer currencyID, boolean isBaseCurrency) {
        boolean isMultiCurrencyCrmAccountBalance = genericSettingsManager.isSettingsEnabled(GenericSettingsEnum.MULTIPLE_CURRENCY_CRM_ACCOUNT_BALANCE);
        String schema = ServerSecurityContext.getInstance().getCompanyId();

        StringBuilder queryBuilder = new StringBuilder();

        if (CrmAccountItem.CUSTOMER.equals(crmAccountType)) {
            queryBuilder.append("select t.id as objectID, t.reversalid as reversalID," +
                            " (case when t.dtype = 'EdsFixedAssetTransaction' then 'EdsInvoiceTransaction' else t.dtype end) as transactionType," +
                            " t.journalid, t.journaldate as date, coalesce(mj.narration, t.name) as narration, coalesce(t.manualjournalid, cp.manualjournalid) as manualJournalId, ")
                    .append(" COALESCE((case when t.dtype = 'EdsInvoiceTransaction' then i.reference else p.reference||','||coalesce(pi.number,cn.number) end), t.reference) as reference, i.duedate as dueDate, " +
                            "CAST(null as integer)  batchPaymentId, coalesce(pi.number,cn.number) as paymentNumber, i.isCreditNote as creditNote," +
                            "(case when t.dtype = 'EdsFixedAssetTransaction' then finv.id else i.id end) as invoiceID," +
                            "(case when t.dtype = 'EdsFixedAssetTransaction' then finv.number " +
                            "when t.dtype = 'EdsExpenseTransaction' then expr.number " +
                            "else i.number end) number," +
                            " cn.isCreditNote as refund, p.id as paymentID, pi.number as paymentInvoiceNumber, ")
                    .append(" p.type as paymentType, ")
                    .append(" (case when t.dtype = 'EdsBankTransferTransaction' then max(t.banktransferid) else null end) as itemId, ")
                    .append(" coalesce(ca.name, '') as clientSupplierName, ");

            if (isBaseCurrency) {
                queryBuilder.append("SUM(COALESCE(ti.debit, 0) - COALESCE(ti.credit, 0)) as amountInBase,SUM(COALESCE(ti.debit, 0) - COALESCE(ti.credit, 0)) as amount, SUM(COALESCE(ti.debit, 0)) as debit, SUM(COALESCE(ti.credit, 0)) as credit ");
            } else {
                if (isMultiCurrencyCrmAccountBalance) {
                    queryBuilder.append("SUM(COALESCE(ti.debit, 0) - COALESCE(ti.credit, 0)) as amountInBase, SUM( \n")
                            .append("(case when t.manualjournalid is not null and mj2.currencyid != a.currencyid then (mj2.exchangerate * COALESCE(ti.debit, 0))\n" +
                                    "\t when t.banktransferid is not null and bt.currencyid != a.currencyid then (bt.exchangerate * COALESCE(ti.debit, 0)) else COALESCE(ti.foreigndebit, 0) end) \n")
                            .append(" - \n")
                            .append("(case when t.manualjournalid is not null and mj2.currencyid != a.currencyid then (mj2.exchangerate * COALESCE(ti.credit, 0))\n" +
                                    "\t when t.banktransferid is not null and bt.currencyid != a.currencyid then (bt.exchangerate * COALESCE(ti.credit, 0)) else COALESCE(ti.foreigncredit, 0) end)) as amount, \n")
                            .append("SUM(case when t.manualjournalid is not null and mj2.currencyid != a.currencyid then (mj2.exchangerate * COALESCE(ti.debit, 0))\n" +
                                    "\t when t.banktransferid is not null and bt.currencyid != a.currencyid then (bt.exchangerate * COALESCE(ti.debit, 0)) else COALESCE(ti.foreigndebit, 0) end) as debit, \n")
                            .append("SUM(case when t.manualjournalid is not null and mj2.currencyid != a.currencyid then (mj2.exchangerate * COALESCE(ti.credit, 0))\n" +
                                    "\t when bt.id is not null and bt.currencyid != a.currencyid then (bt.exchangerate * COALESCE(ti.credit, 0)) else COALESCE(ti.foreigncredit, 0) end) as credit \n");
                } else {
                    queryBuilder.append("SUM(COALESCE(ti.foreigndebit, 0) - COALESCE(ti.foreigncredit, 0)) as amount, SUM(COALESCE(ti.foreigndebit, 0)) as debit, SUM(COALESCE(ti.foreigncredit, 0)) as credit ");
                }

            }

            queryBuilder.append(" from \"").append(schema).append("\".transaction t")
                    .append(" inner join \"").append(schema).append("\".transactionitem ti on ti.transactionid=t.id")
                    .append(" inner join \"").append(schema).append("\".account a on a.id=ti.accountid")
                    .append(" left join \"").append(schema).append("\".invoice i on i.id=t.invoiceid and i.deleted is not true ")
                    .append(" left join \"").append(schema).append("\".invoicepayments p on p.id=t.invoicepaymentid")
                    .append(" left join \"").append(schema).append("\".invoice pi on pi.id=p.invoiceId")
                    .append(" left join \"").append(schema).append("\".invoice cn on cn.id=p.creditNoteId")
                    .append(" left join \"").append(schema).append("\".fixedasset fa on fa.id = t.fixedassetid ")
                    .append(" left join \"").append(schema).append("\".expensereport expr on expr.id=t.expensereportid  ")
                    .append(" left join \"").append(schema).append("\".invoice finv on finv.id = fa.salesinvoiceid and finv.deleted is not true ")
                    .append(" left join \"").append(schema).append("\".customerpayment cp on cp.id = t.customerSupplierPaymentID ")
                    .append(" left join \"").append(schema).append("\".manualjournal mj on mj.id = cp.manualjournalid ")
                    .append(" left join \"").append(schema).append("\".manualjournal mj2 on mj2.id = t.manualjournalid ")
                    .append(" left join \"").append(schema).append("\".spendreceivemoney bt on bt.id = t.banktransferid ")
                    .append(" left join \"").append(schema).append("\".crmaccount ca on (ca.id = t.clientid or ca.id = ti.crmaccount_id) ")
                    .append(" left join \"").append(schema).append("\".expensePayments ep on ep.id = t.expensePaymentId ").append("\n")
                    .append(" left join \"").append(schema).append("\".expenseReport pexp on pexp.id = ep.expenseReportId ").append("\n");
//                        .append(" left join \"").append(schema).append("\".batchpayment sn on sn.id = p.batchPaymentID ");

            createWhereForCustomerBalance(crmAccountIDs, from, to, currencyID, isBaseCurrency, isMultiCurrencyCrmAccountBalance, queryBuilder, false);

            queryBuilder.append(" and p.batchPaymentID is null ");

            queryBuilder.append(" group by t.id, t.reversalid,expr.number, t.dtype, t.journaldate, t.journalid, t.manualjournalid, " +
                            " cp.manualjournalid, mj.narration, t.name,  p.reference,i.iscreditnote, i.id, finv.id, i.number, " +
                            " finv.number, cn.isCreditNote, p.id, pi.number, cn.number, p.type, ca.name")
                    .append(" having  SUM(COALESCE(ti.debit, 0)) != SUM(COALESCE(ti.credit, 0)) ");

            queryBuilder.append(" UNION ALL ");

            queryBuilder.append("select sn.id as objectID, CAST(null as integer) as reversalID, 'EdsInvoicePaymentTransaction' as transactionType," +
                            " sn.id , t.journaldate as date, '' as narration, CAST(null as integer) as manualJournalId, ")
                    .append(" sn.reference as reference,  cn.duedate as dueDate, sn.id batchPaymentId, " +
                            "sn.number||' <br> ('||array_to_string(array_agg(coalesce(pi.number,cn.number)),', ')||')' as paymentNumber, " +
                            "false as creditNote,CAST(null as integer) as invoiceID,sn.number number,false as refund, sn.id as paymentID, max('') as paymentInvoiceNumber, ")
                    .append(" sn.type as paymentType,CAST(null as integer) as itemId,  coalesce(ca.name, '') as clientSupplierName, ");

            if (isBaseCurrency) {
                queryBuilder.append("SUM(COALESCE(ti.debit, 0) - COALESCE(ti.credit, 0)) as amountInBase, SUM(COALESCE(ti.debit, 0) - COALESCE(ti.credit, 0)) as amount, SUM(COALESCE(ti.debit, 0)) as debit, SUM(COALESCE(ti.credit, 0)) as credit ");
            } else {
                queryBuilder.append("SUM(COALESCE(ti.debit, 0) - COALESCE(ti.credit, 0)) as amountInBase, SUM(COALESCE(ti.foreigndebit, 0) - COALESCE(ti.foreigncredit, 0)) as amount, SUM(COALESCE(ti.foreigndebit, 0)) as debit, SUM(COALESCE(ti.foreigncredit, 0)) as credit ");

            }

            queryBuilder.append(" from \"").append(schema).append("\".transaction t")
                    .append(" join \"").append(schema).append("\".transactionitem ti on ti.transactionid=t.id")
                    .append(" join \"").append(schema).append("\".account a on a.id=ti.accountid")
                    .append(" join \"").append(schema).append("\".invoicepayments p on p.id=t.invoicepaymentid")
                    .append(" join \"").append(schema).append("\".batchpayment sn on sn.id = p.batchPaymentID ")
                    .append(" left join \"").append(schema).append("\".invoice pi on pi.id=p.invoiceId")
                    .append(" left join \"").append(schema).append("\".invoice cn on cn.id=p.creditNoteId")
                    .append(" left join \"").append(schema).append("\".crmaccount ca on (ca.id = t.clientid or ca.id = ti.crmaccount_id) ");

            createWhereForCustomerBalance(crmAccountIDs, from, to, currencyID, isBaseCurrency, isMultiCurrencyCrmAccountBalance, queryBuilder, true);

            queryBuilder.append(" group by sn.id, t.journaldate, sn.reference, cn.duedate, sn.number, ca.name")
                    .append(" having  SUM(COALESCE(ti.debit, 0)) != SUM(COALESCE(ti.credit, 0)) ")
                    .append(" order by date, paymentNumber, number");


        } else if (CrmAccountItem.SUPPLIER.equals(crmAccountType)) {
            queryBuilder.append("select t.id as objectID, t.reversalid as reversalID," +
                            " (case when t.dtype = 'EdsFixedAssetTransaction' then 'EdsInvoiceTransaction' else t.dtype end) as transactionType," +
                            " t.journalid, t.journaldate as date, coalesce(mj.narration, t.name) as narration, coalesce(t.manualjournalid, cp.manualjournalid) as manualJournalId, ")
                    .append("coalesce((case " +
                            "when t.dtype = 'EdsFixedAssetTransaction' then finv.reference||','||finv.number " +
                            "when t.dtype = 'EdsBankTransferTransaction' then t.reference " +
                            "when t.dtype = 'EdsExpenseTransaction' then expr.title" +
                            " else i.reference||','||i.number end), t.reference) " +
                            "as reference, i.duedate as dueDate, " +
                            "cast(null as integer) batchPaymentId, coalesce(pi.number,cn.number) as paymentNumber, i.isCreditNote as creditNote," +
                            "(case when t.dtype = 'EdsFixedAssetTransaction' then finv.id else i.id end) as invoiceID," +
                            "(case when t.dtype = 'EdsFixedAssetTransaction' then finv.number " +
                            "when t.dtype = 'EdsExpenseTransaction' then expr.number " +
                            "else i.number end) number," +
                            " cn.isCreditNote as refund, p.id as paymentID, pi.number as paymentInvoiceNumber, ")
                    .append(" p.type as paymentType, ")
                    .append(" (case when t.dtype = 'EdsBankTransferTransaction' then max(t.banktransferid) " +
                            "  when t.dtype = 'EdsExpenseTransaction' then max(t.expenseReportid) " +
                            "  when t.dtype = 'EdsExpensePaymentTransaction' then max(t.expensePaymentId) else null end) as itemId, ")
                    .append(" coalesce(ca.name, '') as clientSupplierName, ");

            if (isBaseCurrency) {
                queryBuilder.append("SUM(COALESCE(ti.credit, 0) - COALESCE(ti.debit, 0)) as amountInBase, SUM(COALESCE(ti.credit, 0) - COALESCE(ti.debit, 0)) as amount, SUM(COALESCE(ti.debit, 0)) as debit, SUM(COALESCE(ti.credit, 0)) as credit ");
            } else {
                if (isMultiCurrencyCrmAccountBalance) {
                    queryBuilder.append("SUM(COALESCE(ti.credit, 0) - COALESCE(ti.debit, 0)) as amountInBase, SUM( \n")
                            .append("(case when t.manualjournalid is not null and mj2.currencyid != a.currencyid then (mj2.exchangerate * COALESCE(ti.credit, 0))\n" +
                                    "\t when t.banktransferid is not null and bt.currencyid != a.currencyid then (bt.exchangerate * COALESCE(ti.credit, 0)) else COALESCE(ti.foreigncredit, 0) end) \n")
                            .append(" - \n")
                            .append("(case when t.manualjournalid is not null and mj2.currencyid != a.currencyid then (mj2.exchangerate * COALESCE(ti.debit, 0))\n" +
                                    "\t when t.banktransferid is not null and bt.currencyid != a.currencyid then (bt.exchangerate * COALESCE(ti.debit, 0)) else COALESCE(ti.foreigndebit, 0) end)) as amount, \n")
                            .append("SUM(case when t.manualjournalid is not null and mj2.currencyid != a.currencyid then (mj2.exchangerate * COALESCE(ti.debit, 0))\n" +
                                    "\t when t.banktransferid is not null and bt.currencyid != a.currencyid then (bt.exchangerate * COALESCE(ti.debit, 0)) else COALESCE(ti.foreigndebit, 0) end) as debit, \n")
                            .append("SUM(case when t.manualjournalid is not null and mj2.currencyid != a.currencyid then (mj2.exchangerate * COALESCE(ti.credit, 0))\n" +
                                    "\t when bt.id is not null and bt.currencyid != a.currencyid then (bt.exchangerate * COALESCE(ti.credit, 0)) else COALESCE(ti.foreigncredit, 0) end) as credit \n");
                } else {
                    queryBuilder.append("SUM(COALESCE(ti.foreigncredit, 0) - COALESCE(ti.foreigndebit, 0)) as amount, SUM(ti.foreigndebit) as debit, SUM(ti.foreigncredit) as credit ");
                }
            }

            queryBuilder.append(" from \"").append(schema).append("\".transaction t")
                    .append(" inner join \"").append(schema).append("\".transactionitem ti on ti.transactionid=t.id")
                    .append(" inner join \"").append(schema).append("\".account a on a.id=ti.accountid")
                    .append(" left join \"").append(schema).append("\".invoice i on i.id=t.invoiceid and i.deleted is not true ")
                    .append(" left join \"").append(schema).append("\".invoicepayments p on p.id=t.invoicepaymentid ")
                    .append(" left join \"").append(schema).append("\".invoice pi on pi.id=p.invoiceId")
                    .append(" left join \"").append(schema).append("\".invoice cn on cn.id=p.creditNoteId")
                    .append(" left join \"").append(schema).append("\".fixedasset fa on fa.id = t.fixedassetid ")
                    .append(" left join \"").append(schema).append("\".expensereport expr on expr.id=t.expensereportid   ")
                    .append(" left join \"").append(schema).append("\".invoice finv on finv.id = fa.purchaseinvoiceid and finv.deleted is not true  ")
                    .append(" left join \"").append(schema).append("\".customerpayment cp on cp.id = t.customerSupplierPaymentID ")
                    .append(" left join \"").append(schema).append("\".manualjournal mj on mj.id = cp.manualjournalid ")
                    .append(" left join \"").append(schema).append("\".manualjournal mj2 on mj2.id = t.manualjournalid ")
                    .append(" left join \"").append(schema).append("\".spendreceivemoney bt on bt.id = t.banktransferid ")
                    .append(" left join \"").append(schema).append("\".crmaccount ca on (ca.id = t.supplierid or ca.id = ti.crmaccount_id) ")
                    .append(" left join \"").append(schema).append("\".expensePayments ep on ep.id = t.expensePaymentId ").append("\n")
                    .append(" left join \"").append(schema).append("\".expenseReport pexp on pexp.id = ep.expenseReportId ").append("\n");
//                        .append(" left join \"").append(schema).append("\".batchpayment sn on sn.id = p.batchPaymentID ");

            createWhereForSupplierBalance(crmAccountIDs, from, to, currencyID, isBaseCurrency, isMultiCurrencyCrmAccountBalance, queryBuilder, false);

            queryBuilder.append(" and p.batchPaymentID is null ");

            queryBuilder.append(" group by t.id, t.reversalid, expr.number, t.dtype, t.journaldate, t.journalid, t.manualjournalid, cp.manualjournalid, " +
                            "mj.narration, t.name,  p.reference, i.iscreditnote, i.id, finv.id, i.number, finv.number, cn.isCreditNote," +
                            " p.id, pi.number,cn.number, p.type, ca.name, expr.title")
                    .append(" having  SUM(COALESCE(ti.debit, 0)) != SUM(COALESCE(ti.credit, 0)) ");

            queryBuilder.append(" UNION ALL ");

            queryBuilder.append("select sn.id as objectID, cast(null as integer) as reversalID,'EdsInvoicePaymentTransaction' as transactionType," +
                            " sn.id journalid, sn.date as date, '' as narration, cast(null as integer) as manualJournalId, ")
                    .append(" sn.reference as reference,  cn.duedate as dueDate, sn.id batchPaymentId, " +
                            "sn.number||' <br> ('||array_to_string(array_agg(coalesce(pi.number,cn.number)),', ')||')' as paymentNumber," +
                            " false as creditNote,cast(null as integer) as invoiceID," +
                            "sn.number number,false as refund, CAST(null as integer) as paymentID, '' as paymentInvoiceNumber, ")
                    .append(" sn.type as paymentType, CAST(null as integer) as itemId, coalesce(ca.name, '') as clientSupplierName, ");

            if (isBaseCurrency) {
                queryBuilder.append("SUM(COALESCE(ti.credit, 0) - COALESCE(ti.debit, 0)) as amountInBase, SUM(COALESCE(ti.credit, 0) - COALESCE(ti.debit, 0)) as amount, SUM(COALESCE(ti.debit, 0)) as debit, SUM(COALESCE(ti.credit, 0)) as credit ");
            } else {
                queryBuilder.append("SUM(COALESCE(ti.credit, 0) - COALESCE(ti.debit, 0)) as amountInBase, SUM(COALESCE(ti.foreigncredit, 0) - COALESCE(ti.foreigndebit, 0)) as amount, SUM(ti.foreigndebit) as debit, SUM(ti.foreigncredit) as credit ");
            }

            queryBuilder.append(" from \"").append(schema).append("\".transaction t")
                    .append(" join \"").append(schema).append("\".transactionitem ti on ti.transactionid=t.id")
                    .append(" join \"").append(schema).append("\".account a on a.id=ti.accountid")
                    .append(" join \"").append(schema).append("\".invoicepayments p on p.id=t.invoicepaymentid ")
                    .append(" join \"").append(schema).append("\".batchpayment sn on sn.id = p.batchPaymentID ")
                    .append(" left join \"").append(schema).append("\".invoice pi on pi.id=p.invoiceId")
                    .append(" left join \"").append(schema).append("\".invoice cn on cn.id=p.creditNoteId")
                    .append(" left join \"").append(schema).append("\".crmaccount ca on (ca.id = t.supplierid or ca.id = ti.crmaccount_id) ");


            createWhereForSupplierBalance(crmAccountIDs, from, to, currencyID, isBaseCurrency, isMultiCurrencyCrmAccountBalance, queryBuilder, true);

            queryBuilder.append(" group by sn.id, sn.date,  cn.duedate, sn.number, sn.type, ca.name")
                    .append(" having  SUM(COALESCE(ti.debit, 0)) != SUM(COALESCE(ti.credit, 0)) ");

            queryBuilder.append(" order by date, paymentNumber, number");

        }

        return queryBuilder.toString();
    }

    private void createWhereForSupplierBalance(ArrayList<Integer> crmAccountIDs, String from, String to, Integer currencyID, boolean isBaseCurrency, boolean isMultiCurrencyCrmAccountBalance, StringBuilder queryBuilder, boolean fromBatchPaymentQuery) {
        queryBuilder.append(" where t.deleted<>true and (a.key in (").append(EdsAccount.ACCOUNTS_PAYABLE).append(",").append(EdsAccount.PREPAID_EXPANSES).append(")").append(" or a.groupKey in ( " + EdsAccount.ACCOUNTS_PAYABLE + "," + EdsAccount.PREPAID_EXPANSES + ") ) ");
        if (!fromBatchPaymentQuery) {
            queryBuilder.append(" and (t.expensereportid is null or  expr.isCompanyExpense is true) ");
            queryBuilder.append(" and (t.expensePaymentId is null or  pexp.isCompanyExpense is true) ");
        }

        if (currencyID != null) {
            if (isMultiCurrencyCrmAccountBalance) {
                if (isBaseCurrency) {
                    queryBuilder.append(" and (a.currencyid is null or a.currencyid=").append(currencyID).append(")");
                    if (!fromBatchPaymentQuery) {
                        queryBuilder.append(" and (t.invoiceid is null or i.currency_id = ").append(currencyID).append(") and (t.manualjournalid is null or mj2.currencyid = ").append(currencyID).append(") and (t.banktransferid is null or bt.currencyid = ").append(currencyID).append(") \n");
                    }
                } else {
                    queryBuilder.append(" and (a.currencyid = ").append(currencyID);
                    if (!fromBatchPaymentQuery) {
                        queryBuilder.append(" or i.currency_id = ").append(currencyID).append(" or mj2.currencyid = ").append(currencyID).append(" or bt.currencyid = ").append(currencyID);
                    }
                    queryBuilder.append(") \n");
                }
            } else {
                queryBuilder.append(isBaseCurrency
                        ? " and (a.currencyid is null or a.currencyid=" + currencyID + ")"
                        : " and a.currencyid=" + currencyID);
            }
        }

        queryBuilder.append(" and (t.journaldate between  '").append(from).append("' and '").append(to).append("')")
                .append(" and (t.supplierid in (").append(ServerUtils.getAsCommoDelimited(crmAccountIDs, "0")).append(")")
                .append(" or ti.crmaccount_id in (").append(ServerUtils.getAsCommoDelimited(crmAccountIDs, "0")).append(")) ");

        if (!fromBatchPaymentQuery) {
            //expense is CompanyExpense
            queryBuilder.append(" and (t.expenseReportid is null or expr.isCompanyExpense is true) ").append("\n");
            //expense payment is CompanyExpense Payment
            queryBuilder.append(" and (t.expensePaymentId is null or pexp.isCompanyExpense is true) \n");
        }
    }

    private void createWhereForCustomerBalance(ArrayList<Integer> crmAccountIDs, String from, String to, Integer currencyID, boolean isBaseCurrency, boolean isMultiCurrencyCrmAccountBalance, StringBuilder queryBuilder, boolean fromBatchPaymentQuery) {
        queryBuilder.append(" where t.deleted is not true and (a.key in (").append(EdsAccount.ACCOUNTS_RECEIVABLE).append(",").append(EdsAccount.UNEARNED_REVENUE).append(") or a.groupKey in ( " + EdsAccount.ACCOUNTS_RECEIVABLE + "," + EdsAccount.UNEARNED_REVENUE + ") ) ");

        if (currencyID != null) {
            if (isMultiCurrencyCrmAccountBalance) {
                if (isBaseCurrency) {
                    queryBuilder.append(" and (a.currencyid is null or a.currencyid=").append(currencyID).append(")");
                    if (!fromBatchPaymentQuery) {
                        queryBuilder.append(" and (t.invoiceid is null or i.currency_id = ").append(currencyID).append(") and (t.manualjournalid is null or mj2.currencyid = ").append(currencyID).append(") and (t.banktransferid is null or bt.currencyid = ").append(currencyID).append(") \n");
                    }
                } else {
                    queryBuilder.append(" and (a.currencyid = ").append(currencyID);
                    if (!fromBatchPaymentQuery) {
                        queryBuilder.append(" or i.currency_id = ").append(currencyID).append(" or mj2.currencyid = ").append(currencyID).append(" or bt.currencyid = ").append(currencyID);
                    }
                    queryBuilder.append(") \n");
                }
            } else {
                queryBuilder.append(isBaseCurrency
                        ? " and (a.currencyid is null or a.currencyid=" + currencyID + ")"
                        : " and a.currencyid=" + currencyID);
            }
        }

        queryBuilder.append(" and (t.journaldate between  '").append(from).append("' and '").append(to).append("')")
                .append(" and (t.clientid in (").append(ServerUtils.getAsCommoDelimited(crmAccountIDs, "0")).append(") ")
                .append(" or ti.crmaccount_id in (").append(ServerUtils.getAsCommoDelimited(crmAccountIDs, "0")).append(")) ");

        if (!fromBatchPaymentQuery) {
            //expense is CompanyExpense
            queryBuilder.append(" and (t.expenseReportid is null or expr.isCompanyExpense is true) ").append("\n");
            //expense payment is CompanyExpense Payment
            queryBuilder.append(" and (t.expensePaymentId is null or pexp.isCompanyExpense is true) \n");
        }
    }

    @Override
    public EdsBankCheckTransaction getBankCheckTransaction(EdsBankCheck bankCheck) {
        return (EdsBankCheckTransaction) findSingle("select bct from EdsBankCheckTransaction bct where bct.bankCheck = ?", bankCheck);
    }

    @Override
    @Deprecated
    public List<EdsGoodsReceivedTransaction> getTransactionByPurchaseOrder(EdsPurchaseOrder purchaseOrder) {
        return (List<EdsGoodsReceivedTransaction>) find("select tr from EdsGoodsReceivedTransaction tr where tr.purchaseOrder = ? order by tr.journalDate", purchaseOrder);
    }

    @Override
    public List<Integer> getTransactionIdsByShippingData(EdsShippingData shippingData) {
        if (shippingData == null || shippingData.getQuote() == null) {
            return Collections.emptyList();
        }
        final String sql = "select tr.objectID from EdsGoodsReceivedTransaction tr " +
                "    where (tr.deleted is null or tr.deleted <> true)" +
                "        and (" +
                "                (tr.shippingData = :shippingData and tr.purchaseOrder is null)" +
                "                or (tr.shippingData is null and tr.purchaseOrder.objectID=:purchaseOrderId)" +
                "            )";

        return this.slaveEntityManager.createQuery(sql, Integer.class)
                .setParameter("shippingData", shippingData)
                .setParameter("purchaseOrderId", shippingData.getQuote().getObjectID())
                .getResultList();
    }

    @Override
    public List<Integer> getTransactionIdsByShippings(List<Integer> shippingIds) {

        if (shippingIds == null || shippingIds.isEmpty()) {
            return Collections.emptyList();
        }

        StringBuilder sql = new StringBuilder("SELECT t.objectID FROM EdsGoodsDeliveredTransaction t join t.shippingData shd \n");
        sql.append(" WHERE (t.deleted is null or t.deleted <> true) and shd.objectID in :shippingIds");

        return slaveEntityManager.createQuery(sql.toString(), Integer.class)
                .setParameter("shippingIds", shippingIds)
                .getResultList();
    }

    @Override
    public BigDecimal getGrnVATTotal(List<Integer> grnIds) {

        if (grnIds == null || grnIds.isEmpty()) {
            return BigDecimal.ZERO;
        }
        StringBuilder sql = new StringBuilder("SELECT SUM(coalesce(ti.debit, 0)) - SUM(coalesce(ti.credit, 0)) FROM ").append(getCompanyId()).append(".transactionitem ti \n")
                .append("JOIN ").append(getCompanyId()).append(".transaction t on t.id = ti.transactionid \n")
                .append("JOIN ").append(getCompanyId()).append(".account a on a.id = ti.accountid \n")
                .append("WHERE t.deleted is not true AND t.shippingDataId in :grnIds \n")
                .append("AND a.key = :vatKey");
        BigDecimal grnVATTotal = (BigDecimal) slaveEntityManager.createNativeQuery(sql.toString())
                .setParameter("grnIds", grnIds)
                .setParameter("vatKey", accountingManager.getVatAccountKey(PAYABLE))
                .getSingleResult();


        return grnVATTotal != null ? grnVATTotal : BigDecimal.ZERO;
    }

    @Override
    public List<Integer> getTransactionIdsByAdjustments(List<Integer> adjustmentIds) {

        if (adjustmentIds == null || adjustmentIds.isEmpty()) {
            return Collections.emptyList();
        }

        StringBuilder sql = new StringBuilder("SELECT t.objectID FROM EdsStockAdjustmentTransaction t join t.adjustment adj \n");
        sql.append(" WHERE (t.deleted is null or t.deleted <> true) and adj.objectID in :adjustmentIds");

        return slaveEntityManager.createQuery(sql.toString(), Integer.class)
                .setParameter("adjustmentIds", adjustmentIds)
                .getResultList();
    }

    @Override
    public List<EdsGoodsReceivedTransaction> getTransactionsByShippingData(EdsShippingData shippingData) {
        return getTransactionsByShippingData(shippingData, false);
    }

    @Override
    public List<EdsGoodsReceivedTransaction> getTransactionsByShippingData(EdsShippingData shippingData, boolean evenDeleted) {
        if (shippingData == null || shippingData.getQuote() == null) {
            return Collections.emptyList();
        }
        LinkedHashMap<String, EdsShippingData> map = new LinkedHashMap<>();
        map.put("shippingData", shippingData);
        final String sql = "select tr from EdsGoodsReceivedTransaction tr " +
                "    where " + (!evenDeleted ? "(tr.deleted is null or tr.deleted <> true) and " : "") +
                "         tr.shippingData = :shippingData " +
                "         order by objectID desc ";

        return findByNamedParams(sql, map);
    }

    @Override
    public List<EdsGoodsReceivedTransaction> getGoodsReceivedTransactionByShippingData(EdsShippingData shippingData) {
        final String sql = "select tr from EdsGoodsReceivedTransaction tr " +
                "    where (tr.deleted is null or tr.deleted <> true) and (tr.shippingData = :shippingData )";

        return this.slaveEntityManager.createQuery(sql, EdsGoodsReceivedTransaction.class)
                .setParameter("shippingData", shippingData)
                .getResultList();
    }

    @Override
    public List<EdsBankTransferTransaction> getBankTransferTransaction(EdsBankTransfer bankTransfer) {
        final String sql = "select tr from EdsBankTransferTransaction tr " +
                "    where (tr.deleted is null or tr.deleted <> true) and (tr.bankTransfer = :bankTransfer )";

        return this.slaveEntityManager.createQuery(sql, EdsBankTransferTransaction.class)
                .setParameter("bankTransfer", bankTransfer)
                .getResultList();
    }

    @Override
    public List<EdsGoodsDeliveredTransaction> getGoodsDeliverdTransactionsByShippingData(EdsShippingData shippingData) {
        return getGoodsDeliverdTransactionsByShippingData(shippingData, false);
    }

    @Override
    public List<EdsGoodsDeliveredTransaction> getGoodsDeliverdTransactionByShippingData(EdsShippingData shippingData) {
        String sql = "select tr from EdsGoodsDeliveredTransaction tr" +
                "  where (tr.deleted is null or tr.deleted <> true) and (tr.shippingData = :shippingData)";

        return this.slaveEntityManager.createQuery(sql, EdsGoodsDeliveredTransaction.class)
                .setParameter("shippingData", shippingData)
                .getResultList();
    }

    @Override
    public List<EdsGoodsDeliveredTransaction> getGoodsDeliverdTransactionsByShippingData(EdsShippingData shippingData, boolean evenDeleted) {
        if (shippingData == null || shippingData.getQuote() == null) {
            return Collections.emptyList();
        }
        final String sql = "select tr from EdsGoodsDeliveredTransaction tr " +
                "    where " + (!evenDeleted ? "(tr.deleted is null or tr.deleted <> true) and " : "") +
                "        tr.shippingData = :shippingData " +
                "        order by objectID desc ";

        return this.slaveEntityManager.createQuery(sql, EdsGoodsDeliveredTransaction.class)
                .setParameter("shippingData", shippingData)
                .getResultList();
    }

    @Override
    public List<EdsGoodsReceivedTransaction> getTransactionsyPurchaseOrderId(Integer purchaseOrderId) {
        if (purchaseOrderId == null) {
            return Collections.emptyList();
        }
        final String sql = "SELECT tr.* FROM " + getCompanyId() + ".transaction tr " +
                "    LEFT JOIN " + getCompanyId() + ".shipping_data sd on tr.shippingDataId = sd.id " +
                "    WHERE (sd.quoteId = :purchaseOrderId and tr.purchaseorder_id is null) " +
                "        and (tr.deleted is null or tr.deleted <> true )" +
                "        or (tr.shippingDataId is null and tr.purchaseorder_id = :purchaseOrderId)";
        return this.slaveEntityManager.createNativeQuery(sql, EdsGoodsReceivedTransaction.class)
                .setParameter("purchaseOrderId", purchaseOrderId)
                .getResultList();
    }

    @Override
    public List<Integer> getTransactionIdsByPurchaseOrderId(Integer purchaseOrderId) {
        if (purchaseOrderId == null) {
            return Collections.emptyList();
        }
        final String sql = "SELECT tr.id FROM " + getCompanyId() + ".transaction tr " +
                "    LEFT JOIN " + getCompanyId() + ".shipping_data sd on tr.shippingDataId = sd.id " +
                "    WHERE (sd.quoteId = :purchaseOrderId and tr.purchaseorder_id is null) " +
                "        and (tr.deleted is null or tr.deleted <> true )" +
                "        or (tr.shippingDataId is null and tr.purchaseorder_id = :purchaseOrderId)";
        return this.slaveEntityManager.createNativeQuery(sql)
                .setParameter("purchaseOrderId", purchaseOrderId)
                .getResultList();
    }

    @Override
    public List<EdsGoodsDeliveredTransaction> getTransactionBySaleOrderId(Integer saleOrderId) {
        if (saleOrderId == null) {
            return Collections.emptyList();
        }
        final String sql = "select tr.* from " + getCompanyId() + ".transaction tr " +
                "    left JOIN " + getCompanyId() + ".shipping_data sd on tr.shippingDataId = sd.id " +
                "    WHERE tr.dtype = :dType" +
                "        and (tr.deleted is null or tr.deleted <> true )" +
                "        and ((sd.quoteId = :saleOrderId and tr.saleorder_id is null) " +
                "            or (tr.shippingDataId is null and tr.saleorder_id = :saleOrderId))";
        return this.slaveEntityManager.createNativeQuery(sql, EdsGoodsDeliveredTransaction.class)
                .setParameter("saleOrderId", saleOrderId)
                .setParameter("dType", EdsGoodsDeliveredTransaction.class.getSimpleName())
                .getResultList();
    }

    @Override
    public EdsDepreciationTransaction getDepreciationTransaction(EdsDepreciation depreciation) {
        return (EdsDepreciationTransaction) findSingle("select tr from EdsDepreciationTransaction tr where tr.depreciation = ?", depreciation);
    }

    @Override
    public EdsManualTransaction getTransactionByManualJournal(EdsManualJournal manualJournal) {
        return (EdsManualTransaction) findSingle("select mt from EdsManualTransaction mt where mt.manualJournal = ? and mt.reversalTransaction is null and mt.deleted<>true", manualJournal);
    }

    @Override
    public List<EdsManualTransaction> getTransactionsByManualJournal(EdsManualJournal manualJournal) {
        if (manualJournal.getObjectID() == null) {
            return Collections.emptyList();
        }
        final String sql = "select tr.* from " + getCompanyId() + ".transaction tr " +
                "    WHERE (tr.deleted is null or tr.deleted <> true ) " +
                " and tr.manualjournalid = :mt " +
                " union " +
                " select rt.* from " + getCompanyId() + ".transaction mt " +
                " left join " + getCompanyId() + ".transaction rt on mt.id = rt.reversalid " +
                " WHERE (mt.deleted is null or mt.deleted <> true ) " +
                " and mt.manualjournalid = :mt ";

        return this.slaveEntityManager.createNativeQuery(sql, EdsManualTransaction.class)
                .setParameter("mt", manualJournal.getObjectID())
                .getResultList();
    }

    @Override
    public BigDecimal getCompanyIncome(ListingFilterParameter filterParameter) {
        Integer companyID = filterParameter.getCompanyID();

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT coalesce(sum(ti.credit),0) - coalesce(sum(ti.debit),0) FROM ").append(companyID).append(".transactionitem ti ");
        sql.append(" INNER JOIN ").append(companyID).append(".transaction t on t.id=ti.transactionid");
        sql.append(" INNER JOIN ").append(companyID).append(".account acc on acc.id=ti.accountid");
        sql.append(" LEFT JOIN ").append(getPublic()).append(".accounttype acct on acct.id=acc.accountTypeId");
        sql.append(" WHERE ").append(ServerUtils.checkForDeleted("t.deleted"));
        sql.append(" AND acct.category in ('" + EdsAccountType.REVENUE + "')");
        if (filterParameter.getStartDate() != null && filterParameter.getEndDate() != null) {
            sql.append(" AND t.journaldate between ? and ?");
            return (BigDecimal) findNative(sql.toString(), filterParameter.getStartDate(), filterParameter.getEndDate());
        } else {
            return (BigDecimal) findNative(sql.toString());
        }
    }

    @Override
    public EdsCusSuppPaymentTransaction getTransaction(Integer transactionID) {
        return (EdsCusSuppPaymentTransaction) findSingle("select tr from EdsCusSuppPaymentTransaction tr where tr.objectID = ?", transactionID);
    }

    public Number getTransactionCount() {
        return (Number) findSingle("select count(*) from EdsTransaction where " + ServerUtils.checkForDeleted("deleted"));
    }

    @Override
    public List<EdsInventoryTransaction> getInventoryTransactions(Integer productID) {
        return find("select it from EdsInventoryTransaction it where it.inventory.objectID = ? and " + ServerUtils.checkForDeleted("deleted"), productID);
    }

    @Override
    public EdsBankTransaction getBankTransactionByMoneyTransfer(Integer bankMoneyTransferID) {
        return (EdsBankTransaction) findSingle("select tr from EdsBankTransaction tr where tr.bankMoneyTransfer.objectID = ?", bankMoneyTransferID);
    }

    @Override
    public EdsBillOfEntryTransaction getBillOfEntryTransaction(Integer billOfEntryID) {
        return (EdsBillOfEntryTransaction) findSingle("select tr from EdsBillOfEntryTransaction tr where tr.billOfEntryId = ?", billOfEntryID);
    }

    @Override
    public EdsBankTransaction getBankAccountOpeningBalanceTransaction(Integer bankAccountID) {
        return (EdsBankTransaction) findSingle("select tr from EdsBankTransaction tr where tr.bankAccount.objectID = ?", bankAccountID);
    }

    @Override
    public void deleteIncomeTaxTransactionsByPeriod(Date startDate, Date endDate) {
        List<EdsIncomeTaxTransaction> transactionList = find("select itt from EdsIncomeTaxTransaction itt where "
                + ServerUtils.checkForDeleted("itt.deleted") + " and itt.journalDate between ? and ?", startDate, endDate);
        for (EdsIncomeTaxTransaction itt : transactionList) {
            itt.setDeleted(true);
            update(itt);
        }
    }

    @Override
    public BigDecimal getPNLProfitForIncomeTax(Date startDate, Date endDate) {
        BigDecimal revenueTotal = (BigDecimal) findSingle("select SUM(COALESCE(ti.credit, 0) - COALESCE(ti.debit, 0)) from EdsTransactionItem ti where "
                + ServerUtils.checkForDeleted("ti.transaction.deleted")
                + " and ti.account.accountType.category=? and ti.transaction.journalDate between ? and ?", EdsAccountType.REVENUE, startDate, endDate);
        BigDecimal expenseTotal = (BigDecimal) findSingle("select SUM(COALESCE(ti.debit, 0) - COALESCE(ti.credit, 0)) from EdsTransactionItem ti where "
                + ServerUtils.checkForDeleted("ti.transaction.deleted")
                + " and ti.account.accountType.category=? and ti.transaction.journalDate between ? and ?", EdsAccountType.EXPENSES, startDate, endDate);

        return (revenueTotal != null ? revenueTotal : BigDecimal.ZERO).subtract(expenseTotal != null
                ? expenseTotal
                : BigDecimal.ZERO);
    }

    public Long getTransactionItemCount(ListingFilterParameter filterParameter) {
        StringBuilder sql = new StringBuilder("select COUNT(distinct t.id) from EdsTransaction t left join t.transactionItems ti where ");
        sql.append(ServerUtils.checkForDeleted("t.deleted"));
        if (filterParameter.getJournalID() != null) {
            sql.append(" and t.journalId = ").append(filterParameter.getJournalID());
            return (Long) findSingle(sql.toString());
        }
        if (filterParameter.getDepartmentId() != null) {
            sql.append(" and ti.department.objectID = ").append(filterParameter.getDepartmentId());
        }
        sql.append(" and t.journalDate between ? and ?");
        return (Long) findSingle(sql.toString(), filterParameter.getStartDate(), filterParameter.getEndDate());
    }

    @Override
    public BigDecimal getBankAccountLastExchangeRate(Integer accountID) {
        return getBankAccountLastExchangeRate(accountID, null);
    }

    @Override
    public BigDecimal getBankAccountLastExchangeRate(Integer accountID, Date date) {
        EdsTransactionItem item = (EdsTransactionItem) findSingle("SELECT ti FROM EdsTransactionItem ti left outer join ti.transaction t WHERE (t.deleted is null or t.deleted is false) " +
                "and ti.account.objectID = " + accountID + (date != null
                ? " and t.journalDate <= '" + date + "'"
                : "") + " ORDER BY t.journalDate DESC");

        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        Integer exchangeScale = fs.getExchangeRateScale();
        if (exchangeScale == null || exchangeScale == 0) {
            exchangeScale = 15;
        }

        if (item != null) {
            if (item.getForeignDebit() != null && item.getForeignDebit().compareTo(BigDecimal.ZERO) > 0) {
                return item.getForeignDebit().divide(item.getDebit(), exchangeScale, BigDecimal.ROUND_HALF_UP);
            }

            if (item.getForeignCredit() != null && item.getForeignCredit().compareTo(BigDecimal.ZERO) > 0) {
                return item.getForeignCredit().divide(item.getCredit(), exchangeScale, BigDecimal.ROUND_HALF_UP);
            }
        }
        return BigDecimal.ONE;
    }

    private EdsCusSuppPaymentTransaction getCustomerSupplierPaymentTransaction(Integer customerSupplierPaymentID) {
        return (EdsCusSuppPaymentTransaction) findSingle("from EdsCusSuppPaymentTransaction t where t.customerSupplierPayment.objectID = ?", customerSupplierPaymentID);
    }

    @Override
    public void deleteCustomerSupplierPaymentTransaction(Integer customerSupplierPaymentID) {
        EdsCusSuppPaymentTransaction transaction = getCustomerSupplierPaymentTransaction(customerSupplierPaymentID);
        setChangedAccountsForRecalculate(transaction);
        update("update EdsCusSuppPaymentTransaction t set deleted=true where t.customerSupplierPayment.objectID = ?", customerSupplierPaymentID);
    }

    public EdsTransaction getTransactionByBankStatementItemID(Integer bankStatementItemID) {
        return (EdsTransaction) findNativeSingle("select * from " + getCompanyId() + ".transaction where deleted is not true and dtype='EdsBankTransferTransaction' and bankstatementitemid=" + bankStatementItemID, EdsTransaction.class);
    }

    public EdsTransaction getTransactionByJournalID(Integer journalId) {
        return (EdsTransaction) findNativeSingle("select * from " + getCompanyId() + ".transaction where deleted is not true and journalId=" + journalId, EdsTransaction.class);
    }

    @Override
    public EdsTransactionItem getPaymentTransactionByStatementItem(FindMatchFilterData filterData) {
        Map<String, Object> map = new HashMap<>();
        map.put("accountID", filterData.getGlAccountID());
        StringBuilder sql = new StringBuilder();
        map.put("bankStatementItemID", filterData.getBankStatementItemID());
        sql.append("from EdsTransactionItem ti left join fetch ti.transaction where ti.account.objectID=:accountID and ").append(ServerUtils.checkForDeleted("ti.transaction.deleted"));
        sql.append(" and ti.reconcileStatus = '" + RECONCILED + "'");
        sql.append(" and ti.transaction.bankStatementItem.objectID =:bankStatementItemID");
        List<EdsTransactionItem> transactionItems = (List<EdsTransactionItem>) findByNamedParams(sql.toString(), map);

        EdsTransaction transaction = null;
        EdsTransactionItem transactionItem = null;
        StringBuilder sb = new StringBuilder();
        BigDecimal totalDebit = ZERO;
        BigDecimal totalCredit = ZERO;
        BigDecimal totalForeignDebit = ZERO;
        BigDecimal totalForeignCredit = ZERO;
        if (transactionItems != null && transactionItems.size() > 0) {
            for (EdsTransactionItem item : transactionItems) {
                transactionItem = item;
                transaction = item.getTransaction();
                sb.append(transaction.getName());
                sb.append(" ; ");
                totalDebit = totalDebit.add(item.getDebit() != null ? item.getDebit() : ZERO);
                totalCredit = totalCredit.add(item.getCredit() != null ? item.getCredit() : ZERO);
                totalForeignDebit = totalForeignDebit.add(item.getForeignDebit() != null
                        ? item.getForeignDebit()
                        : ZERO);
                totalForeignCredit = totalForeignCredit.add(item.getForeignCredit() != null
                        ? item.getForeignCredit()
                        : ZERO);
            }
            transactionItem.setDebit(totalDebit);
            transactionItem.setCredit(totalCredit);
            transactionItem.setForeignDebit(totalForeignDebit);
            transactionItem.setForeignCredit(totalForeignCredit);
            transaction.setName(sb.toString());
            transactionItem.setTransaction(transaction);
            return transactionItem;
        }
        return null;
    }

    @Override
    public void mergeOldCrmAccountToNewOne(List<Integer> oldAccountIDs, Integer newAccountID) {
        //merge clients
        updateNative("UPDATE " + getCompanyId() + ".transaction SET clientid = " + newAccountID + " WHERE clientid in (" + ServerUtils.getAsCommoDelimited(oldAccountIDs, "0", ",") + ")");
        //merge supplier
        updateNative("UPDATE " + getCompanyId() + ".transaction SET supplierid = " + newAccountID + " WHERE supplierid in (" + ServerUtils.getAsCommoDelimited(oldAccountIDs, "0", ",") + ")");
        //merge client/supplier
        updateNative("UPDATE " + getCompanyId() + ".transactionitem SET crmaccount_id = " + newAccountID + " WHERE crmaccount_id in (" + ServerUtils.getAsCommoDelimited(oldAccountIDs, "0", ",") + ")");
    }

    @Override
    public ArrayList<SelectItem> getTransactionJournals(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("select t.journalId as id, t.journalId as name from ").append(getCompanyId()).append(".transaction t where ").append(ServerUtils.checkForDeleted("t.deleted"));
        if (fp.getSqlSearchKey() != null && !fp.getSqlSearchKey().isEmpty()) {
            sql.append(" and CAST(t.journalId AS TEXT) like '").append(fp.getSqlSearchKey()).append("'");
        }
        return (ArrayList<SelectItem>) jdbcSpringManager.getSimpleJdbcTemplate().query(sql.toString(), BeanPropertyRowMapper.newInstance(SelectItem.class));
    }

    @Override
    public EdsDepreciationTransaction getTransactionByDepreciation(EdsDepreciation depreciation) {
        return (EdsDepreciationTransaction) findSingle("select dt from EdsDepreciationTransaction dt where (dt.deleted is null or dt.deleted = false ) and dt.depreciation = ?", depreciation);
    }

    @Override
    public EdsCashAdvancePayTransaction getCashAdvancePayTransactionByPayslipPayment(EdsPayslipPayments payslipPayment) {
        return (EdsCashAdvancePayTransaction) findSingle("select capt from EdsCashAdvancePayTransaction capt where (capt.deleted is null or capt.deleted = false ) and capt.cashAdvancePayment = ?", payslipPayment);
    }

    @Override
    public EdsTransaction getTransactionByVatReturnId(Integer vatReturnId) {
        return (EdsTransaction) findNativeSingle("select * from " + getCompanyId() + ".transaction where deleted is not true and dtype='EdsVatReturnTransaction' and vatreturn_id=" + vatReturnId, EdsTransaction.class);
    }

    @Override
    public EdsTransaction getTransactionByReversedId(Integer reversedId) {
        return (EdsTransaction) findNativeSingle("select * from " + getCompanyId() + ".transaction where deleted is not true  and reversalid=" + reversedId, EdsTransaction.class);
    }

    @Override
    public EdsTransaction getTransactionByVatAdjustmentId(Integer vatAdjustmentId) {
        return (EdsTransaction) findNativeSingle("select * from " + getCompanyId() + ".transaction where deleted is not true and dtype='EdsVatAdjustmentTransaction' and vatadjustment_id=" + vatAdjustmentId, EdsTransaction.class);
    }

    @Override
    public void deleteDeferredTransaction(DeferredTransactionType type, Integer objectId) {
        update("UPDATE EdsDeferredTransaction SET deleted = true WHERE deferredType = ? AND deferredObjectId = ?", type, objectId);
    }

    @Override
    public void deleteMJTransactionItemsByIds(String ids) {
        updateNative("delete from " + getCompanyId() + ".transactionItem where id in (" + ids + ")");
    }

    @Override
    public List<Object[]> getIncorrectInventoryTransactions() {
        return findNative("with itemdata as (select transactionid, sum(credit) credit , sum(debit) debit from " + getCompanyId() +
                ".transactionitem group by transactionid) select t.id, i.credit, i.debit from  " + getCompanyId() + ".transaction t " +
                " left join itemdata i on t.id = i.transactionid where i.credit != i.debit and t.dtype = 'EdsInventoryTransaction'");
    }

    @Override
    public void deleteTransactionsByPayslip(Integer payslipId) {
        update("update EdsPayslipTransaction pt set pt.deleted = true where pt.payslip.objectID = ?", payslipId);
    }

    @Override
    public void deleteTransactionsByPayslipTable(Integer payslipId) {
        update("update EdsPayslipTableTransaction pt set pt.deleted = true where pt.payslipTable.objectID = ?", payslipId);
    }

    @Override
    public void deleteTransactionsByPayrun(Integer payrunId) {
        update("update EdsSinglePayrunTransaction pt set pt.deleted = true where pt.payrun.objectID = ?", payrunId);
    }

    @Override
    public void deleteTransactionsByPayrunPayment(Integer paymentItemId) {
        update("update EdsPayrunPaymentTransaction ppt set ppt.deleted = true where ppt.paymentItem.objectID = ?", paymentItemId);
    }

    @Override
    public void deleteTransactionsByPayrollPayment(Integer paymentItemId) {
        update("update EdsPayrollPaymentTransaction ppt set ppt.deleted = true where ppt.paymentItem.objectID = ?", paymentItemId);
    }
}
