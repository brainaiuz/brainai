package com.edatasite.workforce.rest.v1.release10.accounting;

import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsInvoicePayment;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.CurrencyManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.InvoicePaymentManager;
import com.edatasite.workforce.gwt.invoice.client.rpc.BatchPaymentResult;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.ReceivePaymentData;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.MListingFilterParameter;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.edatasite.workforce.rest.base.to.PaymentTO;
import com.edatasite.workforce.rest.base.to.SelectItemTO;
import com.edatasite.workforce.rest.v1.release10.core.BaseApiControllerV1;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dilsh0d Madrahimov on 10.03.2017.
 */
@Tag(name = "Payment", description = "Payment API")
@RestController
@RequestMapping(value = "/payment", headers = {ApiConstants.SESSION_ID, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiPaymentControllerV1 extends BaseApiControllerV1 implements ApiConstants {

    @Autowired
    private AccountingServiceLocal accountingServiceLocal;
    @Autowired
    private InvoiceServiceLocal invoiceServiceLocal;
    @Autowired
    private InvoicePaymentManager invoicePaymentManager;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private CurrencyManager currencyManager;


    @RequestMapping(value = "/{relationType}/{relationId}/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getList(@PathVariable(value = "relationType") String relationType,
                          @PathVariable(value = "relationId") Integer relationId,
                          @RequestBody MListingFilterParameter mListingFilterParameter) {

        switch (relationType) {
            case SALES_INVOICE, CREDIT_NOTE, PURCHASE_INVOICE -> {
                EdsInvoice edsInvoice = invoiceManager.get(relationId);
                if (edsInvoice == null) {
                    return errorResponse(HttpServletResponse.SC_NOT_FOUND);
                }
                ArrayList<PaymentTO> payments = new ArrayList<>();
                List<EdsInvoicePayment> edsInvoicePayments = CREDIT_NOTE.equals(relationType) ? edsInvoice.getRefunds() : edsInvoice.getPayments();
                for (EdsInvoicePayment invoicePayment : edsInvoicePayments) {
                    PaymentTO paymentTO = new PaymentTO();
                    paymentTO.setId(invoicePayment.getObjectID());
                    paymentTO.setNumber(invoicePayment.getNumber());
                    paymentTO.setDate(WrapUtils.dateToLong(invoicePayment.getPaymentDate()));
                    paymentTO.setExchangeRate(invoicePayment.getExchangeRate());
                    paymentTO.setReference(invoicePayment.getReference());
                    paymentTO.setAmount(invoicePayment.getAmount());
                    if (invoicePayment.getCrmAccount() != null) {
                        paymentTO.setCrmAccount(new SelectItemTO(invoicePayment.getCrmAccount().getObjectID(), invoicePayment.getCrmAccount().getName()));
                    }
                    if (invoicePayment.getAccount() != null) {
                        paymentTO.setAccount(new SelectItemTO(invoicePayment.getAccount().getObjectID(), invoicePayment.getAccount().getName()));
                    }
                    if (invoicePayment.getCurrencyID() != null) {
                        EdsCurrency currency = currencyManager.get(invoicePayment.getCurrencyID());
                        if (currency != null) {
                            paymentTO.setCurrency(new SelectItemTO(currency.getObjectID(), currency.getName(), currency.getSymbol(), ""));
                        }
                    }
                    payments.add(paymentTO);
                }
                return successResponse(payments);
            }
            default -> {
                return errorResponse(ERROR_RESOURCE_NOT_FOUND);
            }
        }
    }

    @RequestMapping(value = "/{relationType}/{relationId}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object add(@PathVariable(value = "relationType") String relationType,
                      @PathVariable(value = "relationId") Integer relationId,
                      @RequestBody PaymentTO paymentTO) {
        switch (relationType) {
            case SALES_INVOICE, CREDIT_NOTE, PURCHASE_INVOICE -> {
                EdsInvoice edsInvoice = invoiceManager.get(relationId);
                if (edsInvoice == null || edsInvoice.getClientOrSupplier() == null) {
                    return errorResponse(ERROR_RESOURCE_NOT_FOUND);
                }
                boolean isValid = paymentTO.getAmount().compareTo(edsInvoice.getDueAmount()) > 0;
                PaymentData paymentData = new PaymentData();
                paymentData.setInvoiceID(relationId);
                paymentData.setPaymentAmount(paymentTO.getAmount());
                paymentData.setDate(new DateNonConvertable(WrapUtils.longToDate(paymentTO.getDate())));
                if (paymentTO.getAccount() != null) {
                    paymentData.setPaymentAccount(paymentTO.getAccount().wrap(paymentTO.getAccount()));
                }
                paymentData.setReferenceNumber(paymentTO.getReference());
                if (paymentTO.getCurrency() != null) {
                    paymentData.setCurrency(paymentTO.getCurrency().wrap(paymentTO.getCurrency()));
                }
                paymentData.setExchangeRate(paymentTO.getExchangeRate());
                paymentData.setType(edsInvoice.getType());
                paymentData.setTotal(edsInvoice.getTotalInInvoiceCurrency());
                PaymentData prePaymentData = new PaymentData();
                if (isValid) {
                    prePaymentData.setInvoiceID(relationId);
                    prePaymentData.setCrmAccount(edsInvoice.getClientOrSupplier().getAsSelectItem());
                    prePaymentData.setPaymentAmount(paymentTO.getAmount());
                    prePaymentData.setDate(new DateNonConvertable(WrapUtils.longToDate(paymentTO.getDate())));
                    if (paymentTO.getAccount() != null) {
                        prePaymentData.setPaymentAccount(paymentTO.getAccount().wrap(paymentTO.getAccount()));
                    }
                    prePaymentData.setReferenceNumber(paymentTO.getReference());
                    if (SALES_INVOICE.equals(relationType)) {
                        prePaymentData.setType(AccountingConstants.RECEIVABLE_PREPAYMENT);
                    } else {
                        prePaymentData.setType(AccountingConstants.PAYABLE_SUPPLIER_CREDIT);
                    }
                    if (paymentTO.getCurrency() != null) {
                        prePaymentData.setCurrency(paymentTO.getCurrency().wrap(paymentTO.getCurrency()));
                    }
                    prePaymentData.setExchangeRate(paymentTO.getExchangeRate());
                }
                if (CREDIT_NOTE.equals(relationType)) {
                    try {
                        invoiceServiceLocal.saveCreditNoteRefund(paymentData);
                        return successResponse(SUCCESS_SAVE);
                    } catch (Exception e) {
                        e.printStackTrace();
                        return errorResponse(ERROR_FAILED_SAVE);
                    }
                } else {
                    ReceivePaymentData receivePaymentData = new ReceivePaymentData();
                    receivePaymentData.setBatchPayment(true);
                    receivePaymentData.setCrmAccount(edsInvoice.getClientOrSupplier().getAsSelectItem());
                    if (paymentTO.getAccount() != null) {
                        receivePaymentData.setAccount(paymentTO.getAccount().wrap(paymentTO.getAccount()));
                    }
                    receivePaymentData.setExRate(paymentTO.getExchangeRate());
                    if (paymentTO.getCurrency() != null) {
                        receivePaymentData.setCurrency(new CurrencyItem(paymentTO.getCurrency().getId(), paymentTO.getCurrency().getName()));
                    }
                    receivePaymentData.setReference(paymentTO.getReference());
                    receivePaymentData.setDate(new DateNonConvertable(WrapUtils.longToDate(paymentTO.getDate())));
                    receivePaymentData.setTotalAmount(paymentTO.getAmount());
                    if (isValid) {
                        receivePaymentData.setPayments(new PaymentData[]{paymentData, prePaymentData});
                    } else {
                        receivePaymentData.setPayments(new PaymentData[]{paymentData});
                    }
                    receivePaymentData.setType(SALES_INVOICE.equals(relationType) ? Constants.RECEIVABLE : Constants.PAYABLE);
                    receivePaymentData.setPaymentTarget(AccountingConstants.PAYMENT_TARGET_INVOICE);

                    try {
                        BatchPaymentResult paymentResult = invoiceServiceLocal.saveReceivePaymentData(receivePaymentData, SALES_INVOICE.equals(relationType));
                        if (paymentResult != null && paymentResult.getResult() > 0) {
                            return successResponse(SUCCESS_SAVE, paymentResult);
                        } else {
                            return errorResponse("Number exists");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        return errorResponse(ERROR_FAIL_DELETE);
                    }
                }
            }
        }
        return errorResponse(HttpServletResponse.SC_NOT_FOUND);
    }

    @RequestMapping(value = "/{relationType}/{relationId}/{id}", method = RequestMethod.GET)
    public Object get(@PathVariable(value = "relationType") String relationType,
                      @PathVariable(value = "relationId") Integer relationId,
                      @PathVariable(value = "id") Integer id) {

        switch (relationType) {
            case SALES_INVOICE, CREDIT_NOTE, PURCHASE_INVOICE -> {
                EdsInvoicePayment invoicePayment = invoicePaymentManager.get(id);
                if (invoicePayment == null) {
                    return errorResponse(ERROR_RESOURCE_NOT_FOUND);
                }
                PaymentTO paymentTO = new PaymentTO();
                paymentTO.setId(invoicePayment.getObjectID());
                paymentTO.setNumber(invoicePayment.getNumber());
                paymentTO.setReference(invoicePayment.getReference());
                if (invoicePayment.getPaymentDate() != null) {
                    paymentTO.setDate(WrapUtils.dateToLong(invoicePayment.getPaymentDate()));
                }
                if (invoicePayment.getAccount() != null) {
                    paymentTO.setAccount(new SelectItemTO(invoicePayment.getAccount().getObjectID(), invoicePayment.getAccount().getName()));
                }
                if (invoicePayment.getCrmAccount() != null) {
                    paymentTO.setCrmAccount(new SelectItemTO(invoicePayment.getCrmAccount().getObjectID(), invoicePayment.getCrmAccount().getName()));
                }
                paymentTO.setAmount(invoicePayment.getAmount());
                paymentTO.setExchangeRate(invoicePayment.getExchangeRate());
                if (invoicePayment.getCurrencyID() != null) {
                    EdsCurrency currency = currencyManager.get(invoicePayment.getCurrencyID());
                    paymentTO.setCurrency(currency != null ? new SelectItemTO(currency.getObjectID(), currency.getName(), currency.getSymbol(), "") : null);
                }
                return successResponse(paymentTO);
            }
        }
        return errorResponse(ERROR_RESOURCE_NOT_FOUND);
    }

    @RequestMapping(value = "/{relationType}/{relationId}/{id}", method = RequestMethod.DELETE)
    public Object delete(@PathVariable(value = "relationType") String relationType,
                         @PathVariable(value = "relationId") Integer relationId,
                         @PathVariable(value = "id") Integer id) {
        switch (relationType) {
            case SALES_INVOICE, CREDIT_NOTE, PURCHASE_INVOICE -> {
                EdsInvoicePayment invoicePayment = invoicePaymentManager.get(id);
                if (invoicePayment == null || invoicePayment.getBatchPaymentID() == null) {
                    return errorResponse(ERROR_RESOURCE_NOT_FOUND);
                }
                try {
                    invoiceServiceLocal.deleteBatchPayment(invoicePayment.getBatchPaymentID());
                    return successResponse(SUCCESS_DELETE);
                } catch (Exception e) {
                    e.printStackTrace();
                    return errorResponse(ERROR_FAIL_DELETE);
                }
            }
        }

        return errorResponse(HttpServletResponse.SC_NOT_FOUND);

    }

    @RequestMapping(value = "/{type}/accounts", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getAccounts(@PathVariable(value = "type") String type,
                              @RequestBody MListingFilterParameter mListingFilterParameter) {
        ListingFilterParameter filterParameter = mListingFilterParameter.convertToFilterParameters();
        filterParameter.setLookUp(true);
        filterParameter.setWithCode(true);
        return successResponse(WrapUtils.wrapSelectItemTOs(accountingServiceLocal.getAccountsForPaymentPost(filterParameter)));
    }

}
