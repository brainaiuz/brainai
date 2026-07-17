package com.edatasite.workforce.rest.v3.release10.settings.payment.payme.util;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsInvoicingSettings;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.InvoicingSettingsManager;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.ReceivePaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;


@Service
public class PaymentUtil {
    @Autowired
    private InvoiceService invoiceService;
    @Autowired
    private InvoicingSettingsManager invoicingSettingsManager;

    public void receivePaymentData(EdsInvoice invoice) {
        ServerSecurityContext.getInstance().setStaticUserID(invoice.getCreator().getObjectID());
        EdsCompany company = new EdsCompany(Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId()));
        EdsInvoicingSettings invSettings = invoicingSettingsManager.getInvoiceSettings(company);
        BigDecimal feeAmountPercent = BigDecimal.valueOf(2);
        if (invSettings.getPaymeServiceFee() != null) {
            feeAmountPercent = invSettings.getPaymeServiceFee();
        }
        NewInvoice newInvoice = invoiceService.getInvoiceSummaryData(invoice.getObjectID());
        BigDecimal amount = newInvoice.getAmount();
        BigDecimal feeValue = amount.multiply(feeAmountPercent).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        ReceivePaymentData receivePaymentData = new ReceivePaymentData();
        receivePaymentData.setBatchPayment(true);
        receivePaymentData.setCrmAccount(newInvoice.getTypeItem());
        receivePaymentData.setAccount(getBankAccount(Constants.PAYMENT_TYPES.PAYME_BANK,company));
        receivePaymentData.setExRate(newInvoice.getExchageRate());
        receivePaymentData.setCurrency(new CurrencyItem(newInvoice.getCurrencyID(), null, null));
        receivePaymentData.setReference(newInvoice.getInvoiceNumber());
        receivePaymentData.setDate(new DateNonConvertable());
        receivePaymentData.setTotalAmount(newInvoice.getAmount().subtract(feeValue));
        receivePaymentData.setValidateReferences(false);
        receivePaymentData.setType(Constants.RECEIVABLE);
        receivePaymentData.setPaymentTarget(AccountingConstants.PAYMENT_TARGET_INVOICE);
        receivePaymentData.setPayments(new PaymentData[]{getPaymentData(newInvoice, receivePaymentData.getAccount(), newInvoice.getAmount().subtract(feeValue))});

        invoiceService.saveReceivePaymentData(receivePaymentData, true);

        if (feeValue.compareTo(BigDecimal.ZERO) > 0) {
            ReceivePaymentData expenseData = new ReceivePaymentData();
            expenseData.setBatchPayment(true);
            expenseData.setCrmAccount(newInvoice.getTypeItem());
            expenseData.setAccount(getBankAccount(Constants.PAYMENT_TYPES.PAYME_EXPENSE, company));
            expenseData.setExRate(newInvoice.getExchageRate());
            expenseData.setCurrency(new CurrencyItem(newInvoice.getCurrencyID(), null, null));
            expenseData.setReference(newInvoice.getInvoiceNumber());
            expenseData.setDate(new DateNonConvertable());
            expenseData.setTotalAmount(feeValue);
            expenseData.setValidateReferences(false);
            expenseData.setType(Constants.RECEIVABLE);
            expenseData.setPaymentTarget(AccountingConstants.PAYMENT_TARGET_INVOICE);
            expenseData.setPayments(new PaymentData[]{getPaymentData(newInvoice, expenseData.getAccount(), feeValue)});

            invoiceService.saveReceivePaymentData(expenseData, true);
        }
    }

    PaymentData getPaymentData(NewInvoice invoice, SelectItem account, BigDecimal amount) {
        PaymentData paymentData = new PaymentData();
        paymentData.setInvoiceID(invoice.getID());
        paymentData.setPaymentAmount(amount != null ? amount : invoice.getDueAmount());
        paymentData.setDate(new DateNonConvertable());
        paymentData.setPaymentAccount(account);
        paymentData.setReferenceNumber(invoice.getInvoiceNumber());
        paymentData.setCurrency(new SelectItem(invoice.getCurrencyID()));
        paymentData.setExchangeRate(invoice.getExchageRate());
        paymentData.setType(invoice.getType());
        paymentData.setTotal(invoice.getTotalInInvoiceCurrency());
        paymentData.setCrmAccount(new SelectItem(invoice.getClientID(), invoice.getClientName()));
        return paymentData;
    }

    SelectItem getBankAccount(String type, EdsCompany company) {
        EdsInvoicingSettings edsInvoicingSettings = invoicingSettingsManager.getInvoiceSettings(company);
        if (Constants.PAYMENT_TYPES.PAYME_BANK.equals(type)) {
            if (StringUtils.isBlank(edsInvoicingSettings.getPayMeMerchantId())
                    || StringUtils.isBlank(edsInvoicingSettings.getPaymeServiceId())) {
                throw new RuntimeException("Payme me is not setup");
            }
            return edsInvoicingSettings.getPaymePaymentAccount().getAsSelectItem();
        } else if (Constants.PAYMENT_TYPES.PAYME_EXPENSE.equals(type)) {
            if (StringUtils.isBlank(edsInvoicingSettings.getPayMeMerchantId())
                    || StringUtils.isBlank(edsInvoicingSettings.getPaymeServiceId())) {
                throw new RuntimeException("Payme me is not setup");
            }
            return edsInvoicingSettings.getPaymeExpenseAccount().getAsSelectItem();
        }
        return null;
    }
}
