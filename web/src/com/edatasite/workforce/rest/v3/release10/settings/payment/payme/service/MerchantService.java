package com.edatasite.workforce.rest.v3.release10.settings.payment.payme.service;

import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.settings.payment.payme.EdsPaymeTransaction;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.customfields.ItemCFManager;
import com.edatasite.workforce.gwt.core.server.db.settings.payment.payme.PaymeOrderManager;
import com.edatasite.workforce.gwt.core.server.db.settings.payment.payme.PaymeTransactionManager;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.base.PaymeError;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.base.PaymeResponse;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.base.Transactions;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.request.CancelTransaction;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.request.CheckPerformTransaction;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.request.CheckTransaction;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.request.CreateTransaction;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.request.GetStatement;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.request.PerformTransaction;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.result.CancelTransactionResult;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.result.CheckPerformTransactionResult;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.result.CheckTransactionResult;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.result.GetStatementResult;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.dto.result.PerformTransactionResult;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.enums.OrderCancelReason;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.enums.TransactionState;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.util.MerchantUtil;
import com.edatasite.workforce.rest.v3.release10.settings.payment.payme.util.PaymentUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: Sharof Mukhtorov
 * Date: 07.05.2025
 * Time: 12:05:01
 */
@Service
public class MerchantService implements Constants {
    private static final Long time_expired = 43_200_000L;
    @Autowired
    private PaymeOrderManager paymeOrderManager;
    @Autowired
    private PaymeTransactionManager paymeTransactionManager;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private InvoiceService invoiceService;

    private final MerchantUtil merchantUtil;
    private final PaymentUtil paymentUtil;
    private final ItemCFManager itemCFManager;

    public MerchantService(MerchantUtil merchantUtil, PaymentUtil paymentUtil, ItemCFManager itemCFManager
    ) {
        this.merchantUtil = merchantUtil;
        this.paymentUtil = paymentUtil;
        this.itemCFManager = itemCFManager;
    }

    @Transactional
    public PaymeResponse handleCheckPerformTransaction(Long id, CheckPerformTransaction checkPerformTransaction) {
        Integer invoiceObjectId = checkPerformTransaction.getAccount().getInvoiceId() != null ?
                checkPerformTransaction.getAccount().getInvoiceId().intValue() : null;
        EdsInvoice invoice = invoiceManager.getSaleInvoice(invoiceObjectId);

        if (invoice == null || invoice.getObjectID() == null) {
            Map<String, String> message = PaymeError.message("Buyurtma topilmadi", "Order not found", "Order not found");
            return PaymeResponse.error(id, PaymeError.error(-31050, message, "orderId"));
        }

        if (Constants.PAID.equals(invoice.getStatus().getCode())
                || Constants.DRAFT.equals(invoice.getStatus().getCode())
                || Constants.REJECTED.equals(invoice.getStatus().getCode())) {
            Map<String, String> message = PaymeError.message("To'lov qilishni imkoni yoq", "Order cannot be charged", "Order cannot be charged");
            return PaymeResponse.error(id, PaymeError.error(-31050, message, "invoiceId"));
        }

        BigDecimal requestedAmount = BigDecimal.valueOf(checkPerformTransaction.getAmount()).divide(BigDecimal.valueOf(100));
        if (invoice.getDueAmount().compareTo(requestedAmount) != 0) {
            Map<String, String> message = PaymeError.message("To'lov miqdori mos kelmadi", "Order amount is invalid", "Order amount is invalid");
            return PaymeResponse.error(id, PaymeError.error(-31001, message, "amount"));
        }

        EdsPaymeTransaction transaction = paymeTransactionManager.findByInvoice_Id(invoiceObjectId);

        if (transaction != null) {
            Map<String, String> message = PaymeError.message("To'lov qilishni imkoni yoq", "Order cannot be charged", "Order cannot be charged");
            return PaymeResponse.error(id, PaymeError.error(-31099, message, "invoice_id"));
        }
//        if (invoice.getInvoiceItems() != null
//                && !invoice.getInvoiceItems().isEmpty()
//                && invoice.getInvoiceItems().get(0) != null
//                && invoice.getInvoiceItems().get(0).getItem() != null) {
//            itemCFManager.getCustomFieldValue(invoice.getInvoiceItems().get(0).getObjectID(), "SPIC");
//        }
        return PaymeResponse.ok(id, merchantUtil.getCheckPerformTransactionResult(true, invoice));
    }

    @Transactional
    public PaymeResponse handleCreateTransaction(Long id, CreateTransaction createTransaction) {
        EdsPaymeTransaction transactionByPaycom = paymeTransactionManager.findByPaycomId(createTransaction.getId());

        if (transactionByPaycom != null) {
            if (!TransactionState.STATE_IN_PROGRESS.equals(transactionByPaycom.getState())) {
                Map<String, String> message = PaymeError.message("Amaliyotni yakunlab bo'lmadi", "Unable to complete operation", "Unable to complete operation");
                return PaymeResponse.error(id, PaymeError.error(-31008, message, "transaction"));
            }
            if (System.currentTimeMillis() - transactionByPaycom.getPaycomTime() > time_expired) {
                transactionByPaycom.setReason(OrderCancelReason.TRANSACTION_TIMEOUT);
                transactionByPaycom.setState(TransactionState.STATE_CANCELED);
                paymeTransactionManager.update(transactionByPaycom);
                Map<String, String> message = PaymeError.message("Amaliyotni yakunlab bo'lmadi", "Unable to complete operation", "Unable to complete operation");
                return PaymeResponse.error(id, PaymeError.error(-31008, message, "transaction"));
            }
            return PaymeResponse.ok(id, merchantUtil.getCreateTransactionResult(transactionByPaycom));
        }

        PaymeResponse response = handleCheckPerformTransaction(id,
                new CheckPerformTransaction(createTransaction.getAmount(), createTransaction.getAccount()));
        CheckPerformTransactionResult result = (CheckPerformTransactionResult) response.getResult();

        if (result != null) {
            if (!result.isAllow()) {
                Map<String, String> message = PaymeError.message("Amaliyotni yakunlab bo'lmadi", "Unable to complete operation", "Unable to complete operation");
                return PaymeResponse.error(id, PaymeError.error(-31008, message, "transaction"));
            }
        }

        Integer invoiceObjectId = createTransaction.getAccount().getInvoiceId() != null ?
                Integer.valueOf(createTransaction.getAccount().getInvoiceId().intValue()) : null;
        EdsPaymeTransaction transaction = paymeTransactionManager.findByInvoice_Id(invoiceObjectId);

        if (transaction != null) {
            Map<String, String> message = PaymeError.message("Buyurtma to'lov jarayonida", "Payment is pending", "Payment is pending");
            return PaymeResponse.error(id, PaymeError.error(-31099, message, "transaction"));
        }

        EdsInvoice invoice = invoiceManager.getSaleInvoice(invoiceObjectId);

        EdsPaymeTransaction newTransaction = new EdsPaymeTransaction();
        newTransaction.setPaycomId(createTransaction.getId());
        newTransaction.setPaycomTime(createTransaction.getTime());
        newTransaction.setInvoice(invoice);
        paymeTransactionManager.create(newTransaction);

        return PaymeResponse.ok(id, merchantUtil.getCreateTransactionResult(newTransaction));
    }

    @Transactional
    public PaymeResponse handlePerformTransaction(Long id, PerformTransaction performTransaction) {
        EdsPaymeTransaction transaction = paymeTransactionManager.findByPaycomId(performTransaction.getId());

        if (transaction == null) {
            Map<String, String> message = PaymeError.message("Tranzaksiya mavjud emas", "Transaction is not found", "Transaction is not found");
            return PaymeResponse.error(id, PaymeError.error(-31003, message, "transaction"));
        }

        long currentTimeMillis = System.currentTimeMillis();

        if (TransactionState.STATE_IN_PROGRESS.equals(transaction.getState())) {
            if (currentTimeMillis - transaction.getPaycomTime() > time_expired) {
                transaction.setState(TransactionState.STATE_CANCELED);
                transaction.setReason(OrderCancelReason.TRANSACTION_TIMEOUT);
                transaction.setCancelTime(currentTimeMillis);
                paymeTransactionManager.update(transaction);

                Map<String, String> message = PaymeError.message("Amaliyotni yakunlab bo'lmadi", "Unable to complete operation", "Unable to complete operation");
                return PaymeResponse.error(id, PaymeError.error(-31008, message, "transaction"));
            }

            transaction.setState(TransactionState.STATE_DONE);
            transaction.setPerformTime(currentTimeMillis);

            EdsInvoice invoice = transaction.getInvoice();
            if (invoice != null) { // Invoice status changed to PAID
                paymentUtil.receivePaymentData(invoice);
            }

            paymeTransactionManager.update(transaction);

            return PaymeResponse.ok(id, new PerformTransactionResult(String.valueOf(transaction.getId()), transaction.getPerformTime(), transaction.getState().getCode()));
        } else if (TransactionState.STATE_DONE.equals(transaction.getState())) {
            return PaymeResponse.ok(id, new PerformTransactionResult(String.valueOf(transaction.getId()), transaction.getPerformTime(), transaction.getState().getCode()));
        }

        Map<String, String> message = PaymeError.message("Amaliyotni yakunlab bo'lmadi", "Unable to complete operation", "Unable to complete operation");
        return PaymeResponse.error(id, PaymeError.error(-31008, message, "transaction"));
    }


    @Transactional
    public PaymeResponse handleCancelTransaction(Long id, CancelTransaction cancelTransaction) {
        EdsPaymeTransaction transaction = paymeTransactionManager.findByPaycomId(cancelTransaction.getId());
        if (transaction == null) {
            Map<String, String> message = PaymeError.message("Tranzaksiya topilmadi", "Transaction not found", "Transaction not found");
            return PaymeResponse.error(id, PaymeError.error(-31003, message, "transaction"));
        }

        if (TransactionState.STATE_CANCELED.equals(transaction.getState()) ||
                TransactionState.STATE_POST_CANCELED.equals(transaction.getState())) {
            return PaymeResponse.ok(id, new CancelTransactionResult(transaction.getId().toString(), transaction.getCancelTime(), transaction.getState().getCode()));
        }

        if (TransactionState.STATE_IN_PROGRESS.equals(transaction.getState())) {
            transaction.setState(TransactionState.STATE_CANCELED);
            transaction.setCancelTime(System.currentTimeMillis());
            transaction.setReason(OrderCancelReason.fromCode(cancelTransaction.getReason()));
            paymeTransactionManager.update(transaction);

            return PaymeResponse.ok(id, new CancelTransactionResult(transaction.getId().toString(), transaction.getCancelTime(), transaction.getState().getCode()));
        }

        // If the transaction is not eligible for cancellation, implement the logic here.
        if (TransactionState.STATE_DONE.equals(transaction.getState())) {
            transaction.setState(TransactionState.STATE_POST_CANCELED);
            transaction.setCancelTime(System.currentTimeMillis());
            transaction.setReason(OrderCancelReason.fromCode(cancelTransaction.getReason()));
            paymeTransactionManager.update(transaction);

            return PaymeResponse.ok(id, new CancelTransactionResult(transaction.getId().toString(), transaction.getCancelTime(), transaction.getState().getCode()));
        }

        Map<String, String> message = PaymeError.message("Tranzaksiya holati noto‘g‘ri", "Invalid transaction state", "Invalid transaction state");
        return PaymeResponse.error(id, PaymeError.error(-31008, message, "transaction"));
    }

    @Transactional
    public PaymeResponse handleCheckTransaction(Long id, CheckTransaction checkTransaction) {
        EdsPaymeTransaction transaction = paymeTransactionManager.findByPaycomId(checkTransaction.getId());
        if (transaction == null) {
            Map<String, String> message = PaymeError.message("Tranzaksiya mavjud emas", "Transaction is not found", "Transaction is not found");
            return PaymeResponse.error(id, PaymeError.error(-31003, message, "transaction"));
        }
        CheckTransactionResult result = merchantUtil.getCheckTransactionResult(transaction);

        return PaymeResponse.ok(id, result);
    }

    @Transactional
    public PaymeResponse handleGetStatement(Long id, GetStatement getStatement) {
        List<EdsPaymeTransaction> transactions = paymeTransactionManager.findByPaycomTimeBetween(getStatement.getFrom(), getStatement.getTo());

        if (transactions.isEmpty()) {
            return PaymeResponse.ok(id, new Transactions(new ArrayList<>()));
        }

        List<GetStatementResult> collect = transactions.stream().map(merchantUtil::getStatementResult).collect(Collectors.toList());
        return PaymeResponse.ok(id, new Transactions(collect));
    }
}
