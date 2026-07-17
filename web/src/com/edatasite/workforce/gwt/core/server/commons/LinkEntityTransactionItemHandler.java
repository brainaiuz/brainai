package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.workforce.core.config.datasource.TenantContextHolder;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsStockAdjustment;
import com.edatasite.workforce.core.domain.EdsStockTransfer;
import com.edatasite.workforce.core.domain.accounting.EdsBankTransfer;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsInvoicePayment;
import com.edatasite.workforce.core.domain.accounting.EdsManualJournal;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransaction;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransactionItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.accounting.server.app.ManualEntryServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.ExpenseReportManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.InvoicePaymentManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.StockAdjustmentManager;
import com.edatasite.workforce.gwt.core.server.db.StockTransferManager;
import com.edatasite.workforce.gwt.core.server.db.TransactionManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ItemStockManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ManualJournalManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.SpendReceiveMoneyManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

/**
 * Created by Shohruh on 01 Apr 2017.
 */
public class LinkEntityTransactionItemHandler implements HttpRequestHandler,Constants {

    private static Logger log = LoggerFactory.getLogger(LinkEntityTransactionItemHandler.class);
    @Autowired
    private AccountingServiceLocal accountingService;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private QuoteManager quoteManager;
    @Autowired
    private InvoicePaymentManager invoicePaymentManager;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private ExpenseReportManager expenseReportManager;
    @Autowired
    private SpendReceiveMoneyManager spendReceiveMoneyManager;
    @Autowired
    private ManualJournalManager manualJournalManager;
    @Autowired
    private StockAdjustmentManager stockAdjustmentManager;
    @Autowired
    private StockTransferManager stockTransferManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private TransactionManager transactionManager;
    @Autowired
    private ItemStockManager itemStockManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private ManualEntryServiceLocal manualEntryServiceLocal;

    /**
     * @param company_id
     * @param db_type - database type: FREE, PAID
     * @param entity_id - id of the updating entity
     * @param entity_type - type of the entity: SALES_INV, CREDIT_NOTE, PURCHASE_INV, DEBIT_NOTE, PURCHASE_ORDER, BANK_TRANSFER, EXPENSE, PREPAYMENT, MANUAL, STOCK_ADJUSTMENT, STOCK_TRANSFER
     * */
    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String companyID = request.getParameter("company_id");
        String dbType = request.getParameter("db_type");
        String entityIdString = request.getParameter("entity_id");
        String entityType = request.getParameter("entity_type");

        if (StringUtil.isEmpty(companyID) || StringUtil.isEmpty(dbType)) {
            response.getWriter().write("Company id and db type cannot be empty");
            return;
        }
        if (!TenantContextHolder.FREE_DB.equals(dbType) && !TenantContextHolder.PAID_DB.equals(dbType)) {
            response.getWriter().write("Invalid db type parameter");
            return;
        }
        if (StringUtil.isEmpty(entityIdString)) {
            response.getWriter().write("Entity id should be provided");
            return;
        }
        if (StringUtil.isEmpty(entityType)) {
            response.getWriter().write("Entity type is null");
            return;
        }

        Integer entityID = null;
        try {
            entityID = Integer.valueOf(entityIdString);
        } catch (NumberFormatException e) {
            response.getWriter().write("Number format exception " + e.getMessage());
            return;
        }

        ServerSecurityContext.getInstance().setDatabase(dbType);
        if (!companyManager.schemaExists(companyID)) {
            response.getWriter().write("Enter valid company");
            return;
        }
        SecurityContext.getInstance().setCompanyId(companyID);

        String uuid = UUID.randomUUID().toString();
        long start = System.currentTimeMillis();

        try {
            updateDirtyCompanyData(companyID, entityType, entityID);
        } catch (Exception e) {
            response.getWriter().write(e.getMessage());
            return;
        }

        log.info("UUID[" + uuid + "] cID[" + companyID + "] db[" + dbType + "] enity_type[" + entityType + "] Link Entity with Transaction item FINISHED. time[" + (System.currentTimeMillis() - start) + "]");
        response.getWriter().write("Succes. process time: " + (System.currentTimeMillis() - start) + "ms");
    }

    @Transactional
    public void updateDirtyCompanyData(String companyID, String entityType, Integer entityId) throws Exception{
        ServerSecurityContext.getInstance().setStaticUserID(userManager.getAdmin(Integer.valueOf(companyID)).getObjectID());

        switch (entityType) {
            case "SALES_INV" -> {
                EdsSaleInvoice saleInvoice = invoiceManager.getSaleInvoice(entityId);
                if (saleInvoice == null) {
                    throw new Exception("Sales Invoice with this number does not exists id:" + entityId);
                }
                String status = saleInvoice.getStatus() != null ? saleInvoice.getStatus().getCode() : null;
                if (APPROVE.equals(status) || OVER_DUE.equals(status) || PAID.equals(status) || OPEN.equals(status)) {
                    if (saleInvoice.getCreator() != null) {
                        ServerSecurityContext.getInstance().setStaticUserID(saleInvoice.getCreator().getObjectID());
                    }
                    try {
                        accountingService.createTransactionsForInvoice(saleInvoice, saleInvoice.getCreator());

                        itemStockManager.flushAndClear();
                        transactionManager.flushAndClear();
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                    log.info("Created Transaction for Sales Invoice: companyID=[" + companyID + "] invoiceID=[" + saleInvoice.getObjectID() + "]");
                }
            }
            case "CREDIT_NOTE" -> {
                EdsInvoice creditNote = invoiceManager.get(entityId);
                if (creditNote == null || !creditNote.isCreditNote()) {
                    throw new Exception("Credit Note with this number does not exists id:" + entityId);
                }
                String status = creditNote.getStatus() != null ? creditNote.getStatus().getCode() : null;
                if (APPROVE.equals(status) || OPEN.equals(status)) {
                    if (creditNote.getCreator() != null) {
                        ServerSecurityContext.getInstance().setStaticUserID(creditNote.getCreator().getObjectID());
                    }
                    try {
                        Integer creditNoteInvoiceID = creditNote.getCreditNoteInvoice() != null ? creditNote.getCreditNoteInvoice().getObjectID() : null;
                        accountingService.createTransactionsForCreditNote(creditNote, creditNoteInvoiceID);

                        itemStockManager.flushAndClear();
                        transactionManager.flushAndClear();
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                    log.info("Created Transaction for Credit Note: companyID=[" + companyID + "] creditNoteID=[" + creditNote.getObjectID() + "]");
                }
            }
            case "PURCHASE_INV" -> {
                EdsPurchaseInvoice purchaseInvoice = invoiceManager.getPurchaseInvoice(entityId);
                if (purchaseInvoice == null) {
                    throw new Exception("Purchase Invoice with this number does not exists id:" + entityId);
                }
                String status = purchaseInvoice.getStatus() != null ? purchaseInvoice.getStatus().getCode() : null;
                if (APPROVE.equals(status) || OVER_DUE.equals(status) || PAID.equals(status) || OPEN.equals(status)) {
                    if (purchaseInvoice.getCreator() != null) {
                        ServerSecurityContext.getInstance().setStaticUserID(purchaseInvoice.getCreator().getObjectID());
                    }
                    try {
                        accountingService.createTransactionsForInvoice(purchaseInvoice, purchaseInvoice.getCreator());

                        itemStockManager.flushAndClear();
                        transactionManager.flushAndClear();
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                    log.info("Created Transaction for Purchase Invoice: companyID=[" + companyID + "] invoiceID=[" + purchaseInvoice.getObjectID() + "]");
                }
            }
            case "DEBIT_NOTE" -> {
                EdsInvoice debitNote = invoiceManager.get(entityId);
                if (debitNote == null || !debitNote.isCreditNote()) {
                    throw new Exception("Debit Note with this number does not exists id:" + entityId);
                }
                String status = debitNote.getStatus() != null ? debitNote.getStatus().getCode() : null;
                if (APPROVE.equals(status) || OPEN.equals(status)) {
                    if (debitNote.getCreator() != null) {
                        ServerSecurityContext.getInstance().setStaticUserID(debitNote.getCreator().getObjectID());
                    }
                    try {
                        Integer creditNoteInvoiceID = debitNote.getCreditNoteInvoice() != null ? debitNote.getCreditNoteInvoice().getObjectID() : null;
                        accountingService.createTransactionsForCreditNote(debitNote, creditNoteInvoiceID);

                        itemStockManager.flushAndClear();
                        transactionManager.flushAndClear();
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                    log.info("Created Transaction for Debit Note: companyID=[" + companyID + "] invoiceID=[" + debitNote.getObjectID() + "]");
                }
            } /*case "PURCHASE_ORDER": {
                //"'" + PARTIAL_RECEIVED + "', '" + RECEIVED + "', '" + INVOICED + "', '" + CONVERTED + "', '" + INVOICE_STATUS_CLOSED + "'";
                EdsPurchaseOrder purchaseOrder = quoteManager.getPurchaseOrderByID(entityId);
                if (purchaseOrder == null) {
                    throw new Exception("Purchase Order with this number does not exists id:" + entityId);
                }
                String status = purchaseOrder.getStatus() != null ? purchaseOrder.getStatus().getCode() : null;
                if (!purchaseOrder.isFixedAssetRelated() && (APPROVE.equals(status))) {
                    if (purchaseOrder.getCreator() != null) {
                        ServerSecurityContext.getInstance().setStaticUserID(purchaseOrder.getCreator().getObjectID());
                    }
                    try {
                        List<EdsGoodsReceivedTransaction> transactions = transactionManager.getTransactionByPurchaseOrder(purchaseOrder);
                        for (EdsGoodsReceivedTransaction transaction : transactions) {
                            transactionManager.setChangedAccountsForRecalculate(transaction.getObjectID());
                            transaction.setDeleted(true);
                            transactionManager.update(transaction);
                            itemStockManager.deleteItemStocksByTransaction(transaction.getObjectID());
                        }
                        accountingService.createTransactionsForGoodsReceived(purchaseOrder, null);
                        transactionManager.flushAndClear();
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                    log.info("Created Transaction for Purchase Order: companyID=[" + companyID + "] orderID=[" + purchaseOrder.getObjectID() + "]");
                }
                break;
            }*/
            case "BANK_TRANSFER" -> {
                EdsBankTransfer bankTransfer = spendReceiveMoneyManager.get(entityId);
                if (bankTransfer == null) {
                    throw new Exception("Bank Transfer with this number does not exists id:" + entityId);
                }
                if (!bankTransfer.isPostDatedTransaction()) {
                    if (bankTransfer.getCreator() != null) {
                        ServerSecurityContext.getInstance().setStaticUserID(bankTransfer.getCreator().getObjectID());
                    }
                    try {
                        ListingFilterParameter fp = new ListingFilterParameter();
                        fp.setObjectId(bankTransfer.getObjectID());
                        fp.setAccountID(bankTransfer.getBankAccount() != null ? bankTransfer.getBankAccount().getObjectID() : bankTransfer.getCashAccount().getObjectID());
                        fp.setType(bankTransfer.getTransferType());

                        //Just a silly logic
                        boolean debit = AccountingConstants.RECEIVE_MONEY.equals(bankTransfer.getTransferType()) || AccountingConstants.CASH_RECEIPT.equals(bankTransfer.getTransferType());
                        NewManualTransaction spendReceiveMoney = accountingService.getBankTransferData(fp);

                        for (NewManualTransactionItem item : spendReceiveMoney.getItems()) {
                            if (debit) {
                                item.setCredit(item.getAmount());
                            } else {
                                item.setDebit(item.getAmount());
                            }
                        }
                        if (spendReceiveMoney.getVatTransactionItems() != null) {
                            for (NewManualTransactionItem vatItem : spendReceiveMoney.getVatTransactionItems()) {
                                if (debit) {
                                    vatItem.setCredit(vatItem.getAmount());
                                } else {
                                    vatItem.setDebit(vatItem.getAmount());
                                }
                            }
                        }
                        //I know I'm genius!

                        accountingService.spendOrReceiveMoney(spendReceiveMoney);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                    log.info("Created Transaction for Bank Transfer: companyID=[" + companyID + "] transferID=[" + bankTransfer.getObjectID() + "]");
                }
            }
            case "EXPENSE" -> {
                EdsExpenseReport report = expenseReportManager.get(entityId);
                if (report == null) {
                    throw new Exception("Expense Report with this number does not exists id:" + entityId);
                }
                String status = report.getStatus() != null ? report.getStatus().getCode() : null;
                if (EXPENSE_APPROVED.equals(status)) {
                    if (report.getCreator() != null) {
                        ServerSecurityContext.getInstance().setStaticUserID(report.getCreator().getObjectID());
                    }
                    try {
                        accountingService.createOrUpdateTransactionForExpense(report);
                        transactionManager.flushAndClear();
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                    log.info("Created Transaction for Expense: companyID=[" + companyID + "] expenseID=[" + report.getObjectID() + "]");
                }
            }
            case "PREPAYMENT" -> {
                EdsInvoicePayment prePayment = invoicePaymentManager.get(entityId);
                if (prePayment == null) {
                    throw new Exception("Prepayment/Supplier Credit with this number does not exists id:" + entityId);
                }
                if (!(prePayment.getStatus() != null && EdsInvoicePayment.POST_DATED.equals(prePayment.getStatus().getCode()))) {
                    if (prePayment.getUser() != null) {
                        ServerSecurityContext.getInstance().setStaticUserID(prePayment.getUser().getObjectID());
                    }
                    try {
                        accountingService.createTransactionForPayment(prePayment);
                        transactionManager.flushAndClear();
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                    log.info("Created Transaction for PrePayment: companyID=[" + companyID + "] prePaymentID=[" + prePayment.getObjectID() + "]");
                }
            }
            case "MANUAL" -> {
                EdsManualJournal manualJournal = manualJournalManager.get(entityId);
                if (manualJournal == null) {
                    throw new Exception("Manual Journal with this number does not exists id:" + entityId);
                }
                if (!manualJournal.isRecurringTemplate()) {
                    if (manualJournal.getSender() != null) {
                        ServerSecurityContext.getInstance().setStaticUserID(manualJournal.getSender().getObjectID());
                    }
                    try {
                        NewManualTransaction manualTransaction = manualEntryServiceLocal.getManualJournal(manualJournal.getObjectID());
                        manualTransaction.setRecurrenceJobItem(null);
                        manualEntryServiceLocal.saveManualJournal(manualTransaction);
                        transactionManager.flushAndClear();
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                    log.info("Created Transaction for Manual Journal: companyID=[" + companyID + "] manualJournalID=[" + manualJournal.getObjectID() + "]");
                }
            }
            case "STOCK_ADJUSTMENT" -> {
                EdsStockAdjustment stockAdjustment = stockAdjustmentManager.get(entityId);
                if (stockAdjustment == null) {
                    throw new Exception("Stock Adjustment with this number does not exists id:" + entityId);
                }
                try {
                    accountingService.createTransactionsForStockAdjustment(stockAdjustment);
                    transactionManager.flushAndClear();
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
                log.info("Created Transaction for Stock Adjustment: companyID=[" + companyID + "] stockAdjustmentID=[" + entityId + "]");
            }
            case "STOCK_TRANSFER" -> {
                EdsStockTransfer stockTransfer = stockTransferManager.get(entityId);
                if (stockTransfer == null) {
                    throw new Exception("Stock Transfer with this number does not exists id:" + entityId);
                }
                try {
                    accountingService.createTransactionForStockTransfer(stockTransfer);
                    transactionManager.flushAndClear();
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
                log.info("Created Transaction for Stock Adjustment: companyID=[" + companyID + "] stockAdjustmentID=[" + entityId + "]");
            }
        }
        ServerSecurityContext.getInstance().setStaticUserID(null);
        ServerSecurityContext.getInstance().removeCompanyId();
    }
}
