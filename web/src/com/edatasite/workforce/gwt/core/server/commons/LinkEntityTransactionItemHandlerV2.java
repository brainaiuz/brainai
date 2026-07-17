package com.edatasite.workforce.gwt.core.server.commons;

import com.edatasite.workforce.core.domain.EdsAssemblyItemBuildHistory;
import com.edatasite.workforce.core.domain.EdsExpenseReport;
import com.edatasite.workforce.core.domain.EdsStockAdjustment;
import com.edatasite.workforce.core.domain.EdsStockTransfer;
import com.edatasite.workforce.core.domain.accounting.EdsBaseSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsGoodsDeliveredTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsInventoryTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsInvoicePayment;
import com.edatasite.workforce.core.domain.accounting.EdsInvoiceTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsManualJournal;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseOrder;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.accounting.EdsShippingData;
import com.edatasite.workforce.core.domain.accounting.EdsStockAdjustmentTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsStockTransferTransaction;
import com.edatasite.workforce.core.domain.accounting.EdsTransaction;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewManualTransaction;
import com.edatasite.workforce.gwt.accounting.client.rpc.PrePaymentListItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.AssemblyItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.accounting.server.app.BuildAssemblyServiceLocal;
import com.edatasite.workforce.gwt.accounting.server.app.ManualEntryServiceLocal;
import com.edatasite.workforce.gwt.backend.server.app.BackendServiceLocal;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.ExpenseReportManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.InvoicePaymentManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.ShippingDataManager;
import com.edatasite.workforce.gwt.core.server.db.StockAdjustmentManager;
import com.edatasite.workforce.gwt.core.server.db.StockTransferManager;
import com.edatasite.workforce.gwt.core.server.db.TransactionManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ItemStockManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ManualJournalManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.SpendReceiveMoneyManager;
import com.edatasite.workforce.gwt.invoice.client.rpc.QuantityItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingData;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.HttpRequestHandler;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class LinkEntityTransactionItemHandlerV2 implements HttpRequestHandler, Constants {

    private static final String FROM_DATE = "FROM_DATE";
    private static final String TO_DATE = "TO_DATE";

    private static Logger log = LoggerFactory.getLogger(LinkEntityTransactionItemHandler.class);
    @Autowired
    private AccountingServiceLocal accountingService;
    @Autowired
    private BuildAssemblyServiceLocal buildAssemblyServiceLocal;
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
    @Autowired
    private ShippingDataManager shippingDataManager;
    @Autowired
    private BackendServiceLocal backendServiceLocal;

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("Link Entity with Transaction item STARTED");
        String companyID = request.getParameter(COMPANY_ID);//0=ALL, company_id
        String type = request.getParameter(ENTITY_TYPE);//SALES_INV/CREDIT_NOTE, PURCHASE_INV/CREDIT_NOTE, PURCHASE_ORDER, BANK_TRANSFER, EXPENSE, PREPAYMENT, MANUAL, STOCK
        String fromDateString = request.getParameter(FROM_DATE);//from date
        String toDateString = request.getParameter(TO_DATE);//to date
        Date fromDate = null, toDate = null;
        try {
            fromDate = StringUtils.isNotBlank(fromDateString) ? dateFormat.parse(fromDateString) : null;
            toDate = StringUtils.isNotBlank(toDateString) ? dateFormat.parse(toDateString) : null;
        } catch (ParseException e) {
            log.info(e.getMessage());
        }

        if (Integer.valueOf(companyID) == 0) {
            List<String> schemas = companyManager.getExistingSchemas();
            for (String schema : schemas) {
                updateDirtyCompanyData(schema, type, fromDate, toDate, response);
            }
        } else {
            updateDirtyCompanyData(companyID, type, fromDate, toDate, response);
        }
        log.info("Link Entity with Transaction item FINISHED");
    }

    @Transactional
    public void updateDirtyCompanyData(String companyID, String type, Date fromDate, Date toDate, HttpServletResponse response) {
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();

        ListingFilterParameter fp = new ListingFilterParameter();

        Date conversionDate = financialSettings.getConversionDate();
        Date blockBeforeDate = financialSettings.getBlockBeforeDate();

        if (fromDate != null) {
            if (blockBeforeDate != null && fromDate.compareTo(blockBeforeDate) < 0) {
                try {
                    PrintWriter writer = response.getWriter();
                    writer.println("From date cannot be less that Transaction Blocked Date, blocked date: " + dateFormat.format(blockBeforeDate));
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    return;
                }
            } else if (fromDate.compareTo(conversionDate) < 0) {
                try {
                    PrintWriter writer = response.getWriter();
                    writer.println("From date cannot be less that Conversion date, conversion date: " + dateFormat.format(conversionDate));
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    return;
                }
            } else {
                fp.setStartDate(fromDate);
            }

        } else if (blockBeforeDate != null) {
            fp.setStartDate(blockBeforeDate);
        } else {
            fp.setStartDate(conversionDate);
        }

        if (toDate != null) {
            fp.setEndDate(toDate);
        }
        ServerSecurityContext.getInstance().setStaticUserID(userManager.getAdmin(Integer.valueOf(companyID)).getObjectID());
        switch (type) {
            case SALES_INV -> {
                fp.setSortField("date");
                List<EdsBaseSaleInvoice> saleInvoices = invoiceManager.getSaleInvoiceList(fp);
                for (EdsBaseSaleInvoice saleInvoice : saleInvoices) {
                    if (saleInvoice.getStatus().equals(referenceManager.findReference(INVOICE_STATUS, APPROVE)) || saleInvoice.getStatus().equals(referenceManager.findReference(INVOICE_STATUS, OVER_DUE))
                            || saleInvoice.getStatus().equals(referenceManager.findReference(INVOICE_STATUS, PAID)) || saleInvoice.getStatus().equals(referenceManager.findReference(INVOICE_STATUS, OPEN))) {
                        if (saleInvoice.getCreator() != null) {
                            ServerSecurityContext.getInstance().setStaticUserID(saleInvoice.getCreator().getObjectID());
                        }
                        try {
                            if (saleInvoice.isCreditNote()) {
                                Integer creditNoteInvoiceID = saleInvoice.getCreditNoteInvoice() != null ? saleInvoice.getCreditNoteInvoice().getObjectID() : null;
                                accountingService.createTransactionsForCreditNote(saleInvoice, creditNoteInvoiceID);
                            } else {
                                accountingService.createTransactionsForInvoice(saleInvoice, saleInvoice.getCreator());
                            }
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        }
                        log.info("Created Transaction for Sales Invoice: companyID=[" + companyID + "] invoiceID=[" + saleInvoice.getObjectID() + "]");
                    }
                }
            }
            case GDN -> {
                fp.setSortField(ShippingData.DATE);
                fp.setSortDir(2);
                fp.setIsGdn(Boolean.TRUE);
                List<EdsShippingData> gdnList = shippingDataManager.getList(fp);
                int counter = 0;
                for (EdsShippingData gdn : gdnList) {
                    try {
                        if (gdn.getCreator() != null) {
                            ServerSecurityContext.getInstance().setStaticUserID(gdn.getCreator().getObjectID());
                        }
                        accountingService.deleteGoodsDeliveryTransactions(gdn);
                        EdsSaleQuote saleQuote = quoteManager.getSaleQuote(gdn.getQuote().getObjectID());
                        accountingService.createTransactionForGoodsDelivered(saleQuote, gdn);
                        log.info("(" + (++counter) + ")" + " Created Transaction for GDN: companyID=[" + companyID + "] gdnID=[" + gdn.getObjectID() + "]");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            case PURCHASE_INV -> {
                fp.setSortField(AccountingConstants.INVOICE_DATE_COLUMN);
                fp.setSortDir(2);
                List<EdsPurchaseInvoice> purchaseInvoices = invoiceManager.getPurchaseInvoiceList(fp, false);
                for (EdsPurchaseInvoice purchaseInvoice : purchaseInvoices) {
                    if (purchaseInvoice.getStatus().equals(referenceManager.findReference(INVOICE_STATUS, APPROVE)) || purchaseInvoice.getStatus().equals(referenceManager.findReference(INVOICE_STATUS, OVER_DUE))
                            || purchaseInvoice.getStatus().equals(referenceManager.findReference(INVOICE_STATUS, PAID)) || purchaseInvoice.getStatus().equals(referenceManager.findReference(INVOICE_STATUS, OPEN))) {
                        if (purchaseInvoice.getCreator() != null) {
                            ServerSecurityContext.getInstance().setStaticUserID(purchaseInvoice.getCreator().getObjectID());
                        }
                        try {
                            if (purchaseInvoice.isCreditNote()) {
                                Integer creditNoteInvoiceID = purchaseInvoice.getCreditNoteInvoice() != null ? purchaseInvoice.getCreditNoteInvoice().getObjectID() : null;
                                accountingService.createTransactionsForCreditNote(purchaseInvoice, creditNoteInvoiceID);
                            } else {
                                accountingService.createTransactionsForInvoice(purchaseInvoice, purchaseInvoice.getCreator());
                            }
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        }
                        log.info("Created Transaction for Purchase Invoice: companyID=[" + companyID + "] invoiceID=[" + purchaseInvoice.getObjectID() + "]");
                    }
                }
            }
            case GRN -> {
                fp.setSortField(ShippingData.DATE);
                fp.setSortDir(2);
                fp.setIsGdn(Boolean.FALSE);
                List<EdsShippingData> grnList = shippingDataManager.getList(fp);
                for (EdsShippingData grn : grnList) {
                    try {
                        if (grn.getCreator() != null) {
                            ServerSecurityContext.getInstance().setStaticUserID(grn.getCreator().getObjectID());
                        }
                        EdsPurchaseOrder purchaseOrder = quoteManager.getPurchaseOrderByID(grn.getQuote().getObjectID());
                        accountingService.deleteGoodsReceivedTransaction(grn);
                        accountingService.createTransactionsForGoodsReceived(purchaseOrder, grn, null);
                        log.info("Created Transaction for GRN: companyID=[" + companyID + "] grnID=[" + grn.getObjectID() + "]");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            case EXPENSE -> {
                List<EdsExpenseReport> reports = expenseReportManager.getCompanyReports(EdsExpenseReport.EXPENSE_APPROVED, fp);
                reports.addAll(expenseReportManager.getCompanyReports(EXPENSE_PAID, fp));
                for (EdsExpenseReport report : reports) {
                    if (report.getCreator() != null) {
                        ServerSecurityContext.getInstance().setStaticUserID(report.getCreator().getObjectID());
                    }
                    try {
                        accountingService.createOrUpdateTransactionForExpense(report);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                    log.info("Created Transaction for Expense: companyID=[" + companyID + "] expenseID=[" + report.getObjectID() + "]");
                }
            }
            case PREPAYMENT -> {
                fp.setViewType(AccountingConstants.RECEIVABLE_PREPAYMENT);
                ListResult<PrePaymentListItem> prePayments = invoicePaymentManager.getPrePayments(fp);
                ArrayList<PrePaymentListItem> payments = new ArrayList<>(prePayments.getList());
                fp.setViewType(AccountingConstants.PAYABLE_SUPPLIER_CREDIT);
                ListResult<PrePaymentListItem> supplierCredits = invoicePaymentManager.getPrePayments(fp);
                payments.addAll(supplierCredits.getList());
                for (PrePaymentListItem payment : payments) {
                    EdsInvoicePayment prePayment = invoicePaymentManager.get(payment.getObjectID());
                    if (!(prePayment.getStatus() != null && EdsInvoicePayment.POST_DATED.equals(prePayment.getStatus().getCode()))) {
                        if (prePayment.getUser() != null) {
                            ServerSecurityContext.getInstance().setStaticUserID(prePayment.getUser().getObjectID());
                        }
                        try {
                            accountingService.createTransactionForPayment(prePayment);
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        }
                        log.info("Created Transaction for PrePayment: companyID=[" + companyID + "] prePaymentID=[" + prePayment.getObjectID() + "]");
                    }
                }
            }
            case MANUAL_ENTRY -> {
                List<EdsManualJournal> manualJournals = manualJournalManager.getManualJournals(fp);
                for (EdsManualJournal manualJournal : manualJournals) {
                    if (!manualJournal.isRecurringTemplate()) {
                        if (manualJournal.getSender() != null) {
                            ServerSecurityContext.getInstance().setStaticUserID(manualJournal.getSender().getObjectID());
                        }
                        try {
                            NewManualTransaction manualTransaction = manualEntryServiceLocal.getManualJournal(manualJournal.getObjectID());
                            manualTransaction.setRecurrenceJobItem(null);
                            manualEntryServiceLocal.saveManualJournal(manualTransaction);
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        }
                        log.info("Created Transaction for Manual Journal: companyID=[" + companyID + "] manualJournalID=[" + manualJournal.getObjectID() + "]");
                    }
                }
            }
            case STOCK_ADJUSTMENT -> {
                fp.setSortField("date");
                fp.setSortDir(2);
                List<EdsStockAdjustment> stockAdjustments = stockAdjustmentManager.getList(fp);
                for (EdsStockAdjustment item : stockAdjustments) {
                    try {
                        EdsStockAdjustment stockAdjustment = item;
                        accountingService.createTransactionsForStockAdjustment(stockAdjustment);
                    } catch (Exception e) {
                        log.error(e.getMessage());
                    }
                    log.info("Created Transaction for Stock Adjustment: companyID=[" + companyID + "] stockAdjustmentID=[" + item.getObjectID() + "]");
                }
            }
            case STOCK_TRANSFER -> {
                fp.setSortField("date");
                List<EdsStockTransfer> stockTransfers = stockTransferManager.getList(fp);
                if (!CollectionUtils.isEmpty(stockTransfers)) {
                    stockTransfers.forEach(st -> {
                        if (st.getCreator() != null) {
                            ServerSecurityContext.getInstance().setStaticUserID(st.getCreator().getObjectID());
                        }
                        accountingService.createTransactionForStockTransfer(st);
                        log.info("Created Transaction for Stock Transfer: companyID=[" + companyID + "] stockTransferID=[" + st.getObjectID() + "]");
                    });
                }
            }
            case OUT_TRANSACTION -> {
                if (StringUtils.isBlank(companyID)) {
                    return;
                }
//                backendServiceLocal.enableDisableGenericSettings(Integer.valueOf(companyID), GenericSettingsEnum.DISABLE_FIFO, true);
                String qyrCompanyID = "\"" + companyID + "\"";
                StringBuilder sql = new StringBuilder("select t.transaction_id from ").append(qyrCompanyID).append(".dump_transaction t order by journaldate, transaction_id\n");
                EntityManager em = quoteManager.getJpaTemplate().getHibernateEntityManager();
                List<Integer> transactionIds = null;
                int start = 0;
                int limit = 100;
                do {
                    System.out.println("Start: " + start + ", Limit: " + limit);
                    Query query = em.createNativeQuery(sql.toString());
                    query.setFirstResult(start);
                    query.setMaxResults(limit);
                    transactionIds = query.getResultList();

                    for (Integer transactionId : transactionIds) {
                        try {
                            EdsTransaction transaction = transactionManager.get(transactionId);

                            if (transaction instanceof EdsInventoryTransaction) {
                                EdsAssemblyItemBuildHistory history = em.createQuery("from EdsAssemblyItemBuildHistory where transactionID = :transactionId", EdsAssemblyItemBuildHistory.class)
                                        .setParameter("transactionId", transactionId).getResultList().get(0);

                                AssemblyItem assemblyItem = new AssemblyItem();
                                assemblyItem.setProductId(history.getAssemblyItemID());
                                assemblyItem.setQuantity(history.getQty());
                                assemblyItem.setWarehouseId(history.getWarehouseID());
                                assemblyItem.setDate(new DateNonConvertable(transaction.getJournalDate()));
                                List<QuantityItem> items = new ArrayList<>();
                                List<Object[]> asbItems = em.createNativeQuery("select item_id, sum(quantity) qty from " + qyrCompanyID + ".item_stock where transactionid = " + transactionId + "\n" +
                                        "and transaction_code = 'OUT'\n" +
                                        "group by item_id").getResultList();

                                for (Object[] objs : asbItems) {
                                    QuantityItem qitem = new QuantityItem();
                                    qitem.setId((Integer) objs[0]);
                                    qitem.setQuantity((BigDecimal) objs[1]);
                                    qitem.setWarehouseID(assemblyItem.getWarehouseId());
                                    items.add(qitem);
                                }

                                if (CollectionUtils.isEmpty(items)) {
                                    continue;
                                }
                                assemblyItem.setItems(items.toArray(new QuantityItem[]{}));
                                buildAssemblyServiceLocal.reBuildAssemblyItem(assemblyItem, transactionId);
                                log.info(transaction.getJournalDate() + " Created Transaction for Assemply Build: companyID=[" + companyID + "] assemplyID=[" + history.getAssemblyItemID() + "]");
                            } else if (transaction instanceof EdsGoodsDeliveredTransaction) {
                                EdsGoodsDeliveredTransaction gdnTranasction = (EdsGoodsDeliveredTransaction) transaction;
                                EdsShippingData gdn = gdnTranasction.getShippingData();
                                if (gdn.getCreator() != null) {
                                    ServerSecurityContext.getInstance().setStaticUserID(gdn.getCreator().getObjectID());
                                }
                                accountingService.deleteGoodsDeliveryTransactions(gdn);
                                EdsSaleQuote saleQuote = quoteManager.getSaleQuote(gdn.getQuote().getObjectID());
                                accountingService.createTransactionForGoodsDelivered(saleQuote, gdn);
                                log.info(transaction.getJournalDate() + " Created Transaction for GDN: companyID=[" + companyID + "] gdnID=[" + gdn.getObjectID() + "]");
                            } else if (transaction instanceof EdsStockTransferTransaction) {
                                EdsStockTransferTransaction stTransaction = (EdsStockTransferTransaction) transaction;
                                EdsStockTransfer st = stTransaction.getStockTransfer();

                                if (st.getCreator() != null) {
                                    ServerSecurityContext.getInstance().setStaticUserID(st.getCreator().getObjectID());
                                }
                                accountingService.createTransactionForStockTransfer(st);
                                log.info(transaction.getJournalDate() + " Created Transaction for Stock Transfer: companyID=[" + companyID + "] stockTransferID=[" + st.getObjectID() + "]");
                            } else if (transaction instanceof EdsStockAdjustmentTransaction) {
                                EdsStockAdjustmentTransaction saTransaction = (EdsStockAdjustmentTransaction) transaction;
                                EdsStockAdjustment stockAdjustment = saTransaction.getAdjustment();
                                accountingService.createTransactionsForStockAdjustment(stockAdjustment);
                                log.info(transaction.getJournalDate() + " Created Transaction for Stock Adjustment: companyID=[" + companyID + "] stockAdjustmentID=[" + stockAdjustment.getObjectID() + "]");
                            } else if (transaction instanceof EdsInvoiceTransaction) {
                                EdsInvoiceTransaction invTransaction = (EdsInvoiceTransaction) transaction;
                                EdsInvoice invoice = invTransaction.getInvoice();

                                if (Constants.PAYABLE.equals(invoice.getType())) {
                                    invoice = invoiceManager.getPurchaseInvoice(invoice.getObjectID());
                                } else {
                                    invoice = invoiceManager.getSaleInvoice(invoice.getObjectID());
                                }
                                if (invoice.getCreator() != null) {
                                    ServerSecurityContext.getInstance().setStaticUserID(invoice.getCreator().getObjectID());
                                }
                                if (invoice.isCreditNote()) {
                                    Integer creditNoteInvoiceID = invoice.getCreditNoteInvoice() != null ? invoice.getCreditNoteInvoice().getObjectID() : null;
                                    accountingService.createTransactionsForCreditNote(invoice, creditNoteInvoiceID);
                                } else {
                                    accountingService.createTransactionsForInvoice(invoice, invoice.getCreator());
                                }
                                log.info(transaction.getJournalDate() + " Created Transaction for Sales Invoice: companyID=[" + companyID + "] invoiceID=[" + invoice.getObjectID() + "]");
                            }
                        } catch (Exception e) {
                            log.error(e.getMessage());
                        }
                    }
                    start += limit;
                } while (transactionIds.size() == limit);
                em.close();
            }
        }
        try {
            PrintWriter writer = response.getWriter();
            writer.println("Process successfully Completed! PROCESS_TYPE:" + type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        backendServiceLocal.enableDisableGenericSettings(Integer.valueOf(companyID), GenericSettingsEnum.DISABLE_FIFO, false);
        ServerSecurityContext.getInstance().setStaticUserID(null);
        ServerSecurityContext.getInstance().removeCompanyId();
    }

    final String SALES_INV = "SALES_INV";
    final String GDN = "GDN";
    final String PURCHASE_INV = "PURCHASE_INV";
    final String GRN = "GRN";
    final String EXPENSE = "EXPENSE";
    final String PREPAYMENT = "PREPAYMENT";
    final String MANUAL_ENTRY = "MANUAL_ENTRY";
    final String STOCK_ADJUSTMENT = "STOCK_ADJUSTMENT";
    final String STOCK_TRANSFER = "STOCK_TRANSFER";
    final String OUT_TRANSACTION = "OUT_TRANSACTION";
}
