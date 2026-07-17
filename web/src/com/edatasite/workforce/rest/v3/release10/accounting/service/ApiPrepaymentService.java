package com.edatasite.workforce.rest.v3.release10.accounting.service;

import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsRentalOrder;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsInvoicePayment;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseOrder;
import com.edatasite.workforce.core.domain.accounting.EdsSaleQuote;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyListItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.view.BankTransferNumberData;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CurrencyManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.InvoicePaymentManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteManager;
import com.edatasite.workforce.gwt.core.server.db.RentalOrderManager;
import com.edatasite.workforce.gwt.invoice.server.app.PrepaymentServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.PrepaymentDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Date;
import java.util.Optional;

import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.PRE_PAYMENT_OPEN_STATUS;
import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.RECEIVABLE_PREPAYMENT;

@Service
public class ApiPrepaymentService implements Constants, ApiConstants {
    private static final Logger log = LoggerFactory.getLogger(ApiPrepaymentService.class);

    private final CrmAccountManager crmAccountManager;
    private final AccountingManager accountingManager;
    private final PrepaymentServiceLocal prepaymentService;
    private final InvoicePaymentManager invoicePaymentManager;
    private final InvoiceManager invoiceManager;
    private final QuoteManager quoteManager;
    private final RentalOrderManager rentalOrderManager;
    private final CurrencyManager currencyManager;
    private final CurrencyServiceLocal currencyService;
    private final FinancialSettingsManager financialSettingsManager;

    public ApiPrepaymentService(CrmAccountManager crmAccountManager, AccountingManager accountingManager, PrepaymentServiceLocal prepaymentService, InvoicePaymentManager invoicePaymentManager, InvoiceManager invoiceManager, QuoteManager quoteManager, RentalOrderManager rentalOrderManager, CurrencyManager currencyManager, CurrencyServiceLocal currencyService, FinancialSettingsManager financialSettingsManager) {
        this.crmAccountManager = crmAccountManager;
        this.accountingManager = accountingManager;
        this.prepaymentService = prepaymentService;
        this.invoicePaymentManager = invoicePaymentManager;
        this.invoiceManager = invoiceManager;
        this.quoteManager = quoteManager;
        this.rentalOrderManager = rentalOrderManager;
        this.currencyManager = currencyManager;
        this.currencyService = currencyService;
        this.financialSettingsManager = financialSettingsManager;
    }

    @Transactional(readOnly = true)
    public PrepaymentDto getById(Integer id) {
        return null;
    }

    @Transactional(rollbackFor = RestException.class)
    public Integer save(PrepaymentDto req) throws RestException {
        boolean isReceivable = RECEIVABLE_PREPAYMENT.equals(req.getType());

        if (req.getId() != null) {
            throw new RestException(IN_VALID_DATA, "Prepayment id is specified", ApiConstants.INVALID, HttpStatus.NOT_FOUND);
        }
        EdsCrmAccount crmAccount = crmAccountManager.get(req.getCrmAccountId());
        if (crmAccount == null || crmAccount.isDeleted()) {
            throw new RestException(IN_VALID_DATA, "Customer/Supplier does not exist with given ID", ApiConstants.INVALID, HttpStatus.NOT_FOUND);
        }
        EdsAccount account = accountingManager.get(req.getPaymentAccountId());
        if (account == null || account.isDeleted()) {
            throw new RestException(IN_VALID_DATA, "Payment Account does not exist with given ID", ApiConstants.INVALID, HttpStatus.NOT_FOUND);
        }
        if (req.getPaymentAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new RestException(IN_VALID_DATA, "Payment Amount must be greater than zero", ApiConstants.INVALID, HttpStatus.BAD_REQUEST);
        }

        EdsInvoicePayment prePayment = new EdsInvoicePayment();
        prePayment.setCrmAccount(crmAccount);
        prePayment.setAccount(account);
        prePayment.setAmount(req.getPaymentAmount());
//        prePayment.setReference("FROM API"); //todo
        EdsCurrency currency = currencyManager.getCurrency("UZS"); // todo
        prePayment.setCurrencyID(currency != null ? currency.getObjectID() : null);
        prePayment.setType(req.getType());
        prePayment.setPaymentStatus(PRE_PAYMENT_OPEN_STATUS);

        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        CurrencyListItem currencyItem = currencyService.getCurrencyRateByDate(currency.getObjectID(), new DateNonConvertable(new Date()));
        BigDecimal bankCurrencyExchangeRate = currencyItem != null
                ? BigDecimal.valueOf(currencyItem.getExchangeRate()).setScale(fs.getExchangeRateScale(), RoundingMode.HALF_UP)
                : BigDecimal.ONE;

        prePayment.setExchangeRate(bankCurrencyExchangeRate);
        prePayment.setPaymentDate(new Date());
        prePayment.setAmountInInvoiceCurrency(req.getPaymentAmount().multiply(bankCurrencyExchangeRate));

        Optional.ofNullable(req.getInvoiceId()).ifPresent(invId -> {
            EdsInvoice invoice = invoiceManager.get(invId);
            if (invoice != null && !invoice.isDeleted()) {
                prePayment.setInvoice(invoice);
            }
        });

        Optional.ofNullable(req.getQuoteId()).ifPresent(quoteId -> {
            EdsSaleQuote saleQuote = quoteManager.getSaleQuote(quoteId);
            if (saleQuote != null && !saleQuote.isDeleted()) {
                prePayment.setSaleQuote(saleQuote);
            }
        });

        Optional.ofNullable(req.getPurchaseOrderId()).ifPresent(orderId -> {
            EdsPurchaseOrder purchaseOrder = quoteManager.getPurchaseOrderByID(orderId);
            if (purchaseOrder != null && !purchaseOrder.isDeleted()) {
                prePayment.setPurchaseOrder(purchaseOrder);
            }
        });

        Optional.ofNullable(req.getRentalOrderId()).ifPresent(rentOrderId -> {
            EdsRentalOrder rentalOrder = rentalOrderManager.get(rentOrderId);
            if (rentalOrder != null && !rentalOrder.isDeleted()) {
                prePayment.setRentalOrder(rentalOrder);
            }
        });

        BankTransferNumberData numberData = prepaymentService.generatePrepaymentNumber(isReceivable ? "PREPAYMENT" : "SUPPLIER_CREDIT");
        prePayment.setNumber(numberData.getTransferNumber());
        prePayment.setNumberInt(Integer.parseInt(numberData.getFourDigitNumber()));
        invoicePaymentManager.createOrUpdate(prePayment);

        return prePayment.getObjectID();
    }
}
