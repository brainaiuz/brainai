package com.edatasite.workforce.rest.v3.release10.accounting;

import com.antkorwin.xsync.XSync;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsQuote;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.ApproverManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CurrencyManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.QuoteManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.invoice.client.rpc.BatchPaymentResult;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.QuantityItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.ReceivePaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.enums.StockOutFlow;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.QuoteService;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceAPIService;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.edatasite.workforce.gwt.invoice.server.app.QuoteServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.enums.InvoiceStatusEnum;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.BaseInvoiceDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.InvoiceDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.InvoicePaymentDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.LineItemDto;
import com.edatasite.workforce.rest.v3.release10.accounting.request.UpdateStatusRequest;
import com.edatasite.workforce.rest.v3.release10.accounting.utils.InvoiceDtoUtils;
import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.PAID;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.REVERSED;

/**
 * Created by Normurod Buriev.
 * Date: 1/4/2021 12:48 AM
 */
public abstract class AbstractInvoiceQuoteApiResource implements ApiConstants {

    protected InvoiceDtoUtils dtoUtils;
    protected AccountingManager accountingManager;
    protected ReferenceManager referenceManager;
    protected FinancialSettingsManager financialSettingsManager;
    protected CrmServiceLocal crmServiceLocal;
    protected CrmAccountManager crmAccountManager;
    protected QuoteServiceLocal quoteServiceLocal;
    protected QuoteService quoteService;
    protected InvoiceServiceLocal invoiceServiceLocal;
    protected InvoiceAPIService invoiceAPIService;
    protected QuoteManager quoteManager;
    protected InvoiceManager invoiceManager;
    protected CurrencyManager currencyManager;
    protected XSync<String> stringXSync;
    private CurrencyService currencyService;
    protected ItemManager itemManager;
    protected ApproverManager approverManager;
    protected AllInOneService allInOneService;
    @Autowired
    @Qualifier("accountingLocalizer")
    protected WfmMessageSource wfmMessageSource;

    protected abstract boolean isCustomer();

    protected abstract EdsInvoice getInvoice(UpdateStatusRequest request);

    protected abstract EdsQuote getOrder(UpdateStatusRequest request);

    protected abstract void changeInvoiceStatus(Integer objectId, String statusCode);

    protected abstract void changeOrderStatus(Integer objectId, String statusCode);

    List<BatchPaymentResult> makePayments(NewInvoice invoice, List<InvoicePaymentDto> payments) throws RestException {
        List<BatchPaymentResult> results = new ArrayList<>();
        CurrencyItem baseCurrency = invoiceServiceLocal.getBaseCurrency();

        for (InvoicePaymentDto payment : payments) {
            BigDecimal exchangeRate = BigDecimal.ONE;
            PaymentData paymentData = new PaymentData();
            initialize_payment_item_details:
            {
                paymentData.setInvoiceID(invoice.getID());
                paymentData.setPaymentAmount(payment.getAmount());
                paymentData.setDate(new DateNonConvertable(payment.getDate()));

                EdsAccount paymentAccount = null;
                if (payment.getAccount().getId() != null) {
                    paymentAccount = accountingManager.get(payment.getAccount().getId());
                } else if (payment.getAccount().getCode() != null) {
                    paymentAccount = accountingManager.getAccountByCode(payment.getAccount().getCode());
                }
                if (paymentAccount != null) {
                    paymentData.setPaymentAccount(paymentAccount.getAsSelectItem());
                } else {
                    throw new RestException(GENERAL_ERROR_MESSAGE, "Provided payment account code/id is not valid!", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }

                if (payment.getCurrency() != null && !"".equals(payment.getCurrency())) {
                    EdsCurrency currency = currencyManager.getCurrency(payment.getCurrency());
                    if (currency != null) {
                        paymentData.setCurrency(currency.getAsSelectItem());
                    } else {
                        paymentData.setCurrency(new SelectItem(baseCurrency.getId(), baseCurrency.getName()));
                    }
                } else {
                    paymentData.setCurrency(new SelectItem(baseCurrency.getId(), baseCurrency.getName()));
                }
                paymentData.setReferenceNumber(payment.getReference());

                if (payment.getExchangeRate() == null) {
                    CurrencyListItem currencyLayerItem = currencyService.getCurrencyRateByDate(paymentData.getCurrency().getId(), paymentData.getDate());
                    exchangeRate = BigDecimal.valueOf(currencyLayerItem.getExchangeRate());
                }
                paymentData.setExchangeRate(payment.getExchangeRate() != null ? payment.getExchangeRate() : exchangeRate);
                paymentData.setType(invoice.getType());
                paymentData.setTotal(invoice.getTotalInInvoiceCurrency());
                paymentData.setCrmAccount(new SelectItem(invoice.getClientID()));
            }

            initiazlize_batch_payment:
            {
                ReceivePaymentData receivePaymentData = new ReceivePaymentData();
                receivePaymentData.setBatchPayment(true);
                receivePaymentData.setCrmAccount(paymentData.getCrmAccount());
                receivePaymentData.setAccount(paymentData.getPaymentAccount());
                receivePaymentData.setExRate(paymentData.getExchangeRate());
                receivePaymentData.setCurrency(new CurrencyItem(paymentData.getCurrency().getId(), null, null));
                receivePaymentData.setReference(paymentData.getReferenceNumber());
                receivePaymentData.setDate(paymentData.getDate());
                receivePaymentData.setTotalAmount(paymentData.getPaymentAmount());
                receivePaymentData.setPayments(new PaymentData[]{paymentData});
                receivePaymentData.setType(paymentData.getType());
                receivePaymentData.setPaymentTarget(AccountingConstants.PAYMENT_TARGET_INVOICE);
                BatchPaymentResult r = invoiceServiceLocal.saveReceivePaymentData(receivePaymentData, Constants.RECEIVABLE.equals(paymentData.getType()));
                results.add(r);
            }
        }
        return results;
    }

    void deleteInvoicePayments(Integer invoiceId) {

    }

    void changeInvoiceStatus(UpdateStatusRequest request) throws RestException {
        if (request == null) {
            throw new RestException(IN_VALID_DATA, "Request body cannot be empty!", SERVER_ERROR, HttpStatus.BAD_REQUEST);
        }
        if (request.getObjectId() == null && StringUtils.isBlank(request.getNumber())) {
            throw new RestException(IN_VALID_DATA, "Invoice Id or Number is required!", SERVER_ERROR, HttpStatus.BAD_REQUEST);
        }
        String status = InvoiceStatusEnum.getStatus(request.getStatus());
        if (StringUtils.isBlank(status)) {
            throw new RestException(IN_VALID_DATA, "Status is not valid! Status must be one of " + StringUtils.join(InvoiceStatusEnum.values(), "/"), SERVER_ERROR, HttpStatus.BAD_REQUEST);
        }
        EdsInvoice invoice = getInvoice(request);

        if (invoice == null) {
            throw new RestException(IN_VALID_DATA, "Invoice is not found!", SERVER_ERROR, HttpStatus.NOT_FOUND);
        }

        if (InvoiceStatusEnum.PAID.getStatus().equals(status)) {
            if (InvoiceStatusEnum.DRAFT.getStatus().equals(invoice.getStatus().getCode())) {
                changeInvoiceStatus(invoice.getObjectID(), InvoiceStatusEnum.APPROVE.getStatus());
            }
            InvoicePaymentDto paymentDto = new InvoicePaymentDto();
            paymentDto.setReference(invoice.getNumber());
            paymentDto.setAmount(invoice.getDueAmount());
            paymentDto.setCurrency(invoice.getCurrency().getName());
            paymentDto.setExchangeRate(invoice.getExchangeRate());
            paymentDto.setDate(new Date());

            if (invoice instanceof EdsSaleInvoice && ((EdsSaleInvoice) invoice).getBankAccount() != null) {
                EdsAccount account = ((EdsSaleInvoice) invoice).getBankAccount().getAccount();
                paymentDto.setAccount(new IdCode(account.getObjectID(), account.getAccountCode()));
            } else if (invoice.getClientOrSupplier().getBankAccount() != null) {
                EdsAccount account = invoice.getClientOrSupplier().getBankAccount().getAccount();
                paymentDto.setAccount(new IdCode(account.getObjectID(), account.getAccountCode()));
            } else {
                ListingFilterParameter fp = new ListingFilterParameter();
                fp.setLookUp(true);
                fp.setCurrencyID(invoice.getCurrency().getObjectID());
                List<EdsAccount> accountList = accountingManager.getAccountsForPayment(fp);

                if (!org.springframework.util.CollectionUtils.isEmpty(accountList)) {
                    EdsAccount account = accountList.get(0);
                    paymentDto.setAccount(new IdCode(account.getObjectID(), account.getAccountCode()));
                }
            }
            makePayments(EdsInvoice.getInvoiceData(invoice), List.of(paymentDto));
        } else {
            changeInvoiceStatus(invoice.getObjectID(), status);
        }
    }

    void changeOrderStatus(UpdateStatusRequest request) throws RestException {
        if (request == null) {
            throw new RestException(IN_VALID_DATA, "Request body cannot be empty!", SERVER_ERROR, HttpStatus.BAD_REQUEST);
        }
        if (request.getObjectId() == null && StringUtils.isBlank(request.getNumber()) && StringUtils.isBlank(request.getObjectKey())) {
            throw new RestException(IN_VALID_DATA, "Invoice Id or Number is required!", SERVER_ERROR, HttpStatus.BAD_REQUEST);
        }
        String status = InvoiceStatusEnum.getStatus(request.getStatus());
        if (StringUtils.isBlank(status)) {
            throw new RestException(IN_VALID_DATA, "Status is not valid! Status must be one of " + StringUtils.join(InvoiceStatusEnum.values(), "/"), SERVER_ERROR, HttpStatus.BAD_REQUEST);
        }
        EdsQuote order = getOrder(request);

        if (order == null) {
            throw new RestException(IN_VALID_DATA, "Order is not found!", SERVER_ERROR, HttpStatus.NOT_FOUND);
        }
        changeOrderStatus(order.getObjectID(), request.getStatus());
    }

    EdsInvoice validateInvoiceForExistence(InvoiceDto dto, String transactionType) throws RestException {
        EdsInvoice edsInvoice = null;
        if (dto.isExistingObject()) {
            if (StringUtils.isNotBlank(dto.getObjectKey())) {
                edsInvoice = invoiceManager.getByObjectKey(dto.getObjectKey());
            }
            if (edsInvoice == null && dto.getId() != null) {
                edsInvoice = invoiceManager.get(dto.getId());
            }
            if (edsInvoice == null && StringUtils.isNotBlank(dto.getNumber())) {
                if (SALES_INVOICE.equals(transactionType)) {
                    edsInvoice = invoiceManager.getSaleInvoiceByNumber(dto.getNumber(), null).stream().findAny().orElse(null);
                } else {
                    edsInvoice = invoiceManager.getPurchaseInvoiceByNumber(dto.getNumber(), null, null).stream().findAny().orElse(null);
                }
            }
            if (edsInvoice == null) {
                throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Invoice with the given objectKey/Id or Number not found", ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST);
            }
            String statusCode = edsInvoice.getStatus().getCode();
            if (/*!CollectionUtils.isEmpty(edsInvoice.getPayments()) || */Arrays.asList(REVERSED, PAID).contains(statusCode)) {
                throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "You cannot modify the invoice", ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST);
            }
        }
        return edsInvoice;
    }

    void validateCustomerSupplierExist(BaseInvoiceDto dto) {
        EdsCrmAccount crmAccount = null;
        ItemDto customerDto = dto.getCustomer();
        if (customerDto.getId() != null) {
            crmAccount = crmAccountManager.get(customerDto.getId());
        } else if (StringUtils.isNotBlank(customerDto.getCode())) {
            crmAccount = crmAccountManager.getCrmAccountByNumber(customerDto.getCode());
        } else if (StringUtils.isNotBlank(customerDto.getName())) {
            crmAccount = crmAccountManager.getCrmAccountByName(customerDto.getName());
        }
        if (crmAccount == null) {
            createCrmAccountSynchronously(dto.getCustomer());
        }
    }

    void validate(BaseInvoiceDto dto, String transactionType) throws RestException {
        if (dto.getCustomer().getId() == null
                && StringUtils.isBlank(dto.getCustomer().getCode())
                && StringUtils.isBlank(dto.getCustomer().getName())) {
            throw new RestException(IN_VALID_DATA, (isCustomer() ? "Customer" : "Supplier") + " is required", ApiConstants.REQUIRED, HttpStatus.BAD_REQUEST);
        }
        for (LineItemDto itemDto : dto.getItems()) {
            if (itemDto.getQuantity().compareTo(BigDecimal.ZERO) <= 0) {
                throw new RestException(IN_VALID_DATA, "Item Quantity must be positive.", ApiConstants.REQUIRED, HttpStatus.BAD_REQUEST);
            }
        }
    }

    void validatePayments(NewInvoice invoice, List<InvoicePaymentDto> payments) throws RestException {
        BigDecimal totalPayment = BigDecimal.ZERO;
        for (InvoicePaymentDto payment : payments) {
            Date invDate = DateUtil.resetTime((Date) invoice.getInvoiceDate().getNonConvertedDate().clone());
            Date paymentDate = DateUtil.resetTime((Date) payment.getDate().clone());
            if (invDate.after(paymentDate)) {
                throw new RestException(IN_VALID_DATA, "Payment date cannot be before than invoice date", ApiConstants.REQUIRED, HttpStatus.BAD_REQUEST);
            }
            if (payment.getAccount().getId() != null) {
                Optional.ofNullable(accountingManager.get(payment.getAccount().getId()))
                        .orElseThrow(() -> new RestException(IN_VALID_DATA, "Payment account not found by ID: " + payment.getAccount().getId(), ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST));
            } else if (payment.getAccount().getCode() != null) {
                Optional.ofNullable(accountingManager.getAccountByCode(payment.getAccount().getCode()))
                        .orElseThrow(() -> new RestException(IN_VALID_DATA, "Payment account not found by Code: " + payment.getAccount().getCode(), ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST));
            }
            totalPayment = totalPayment.add(payment.getAmount());
        }

        if (totalPayment.compareTo(invoice.getDueAmount()) > 0) {
            throw new RestException(IN_VALID_DATA, "Payment amount cannot be more than invoice due amount", ApiConstants.REQUIRED, HttpStatus.BAD_REQUEST);
        }
    }

    void createCrmAccountSynchronously(ItemDto customer) {
        String content = StringUtils.isNotBlank(customer.getCode()) ? customer.getCode().trim().toLowerCase() + "-" : "";
        content += StringUtils.isNotBlank(customer.getName()) ? customer.getName().trim().toLowerCase() : "";
        stringXSync.execute(getSyncronizedKey(content), () -> {
            try {
                createCrmAccount(customer);
            } catch (RestException e) {
                e.printStackTrace();
            }
        });
    }

    void createCrmAccount(ItemDto crmAccount) throws RestException {
        CrmAccountItem crmAccountItem = new CrmAccountItem();
        crmAccountItem.setName(crmAccount.getName());
        crmAccountItem.setNumber(crmAccount.getCode());

        EdsReference accountType = referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, isCustomer() ? EdsCrmAccount.CUSTOMER : EdsCrmAccount.SUPPLIER);
        SelectItem accountTypeItem = accountType.getAsSelectItem();
        accountTypeItem.setSelected(true);
        crmAccountItem.setAccountTypes(new SelectItem[]{
                accountTypeItem
        });

        EdsFinancialSettings fs = financialSettingsManager.getFinancialSettings();
        crmAccountItem.setCurrencyId(fs.getCurrency() != null ? fs.getCurrency().getObjectID() : null);
        Integer result = crmServiceLocal.saveAccount(crmAccountItem, isCustomer() ? CrmAccountItem.CUSTOMER : CrmAccountItem.SUPPLIER, null, false, false, false, true);

        if (result < 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Error handled when creating a " + (isCustomer() ? "customer" : "supplier"), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    String getSyncronizedKey(String content) {
        return (isCustomer() ? "CREATE_CUSTOMER_" : "CREATE_SUPPLIER_") + ServerSecurityContext.getInstance().getCompanyId() + "_" + content;
    }

    void configureDateFiltersWithFacet(ListingFilterParameter fp) {
        if (fp.getFacetFilter() != null && fp.getFacetFilter().getStartDate() != null && fp.getFacetFilter().getEndDate() != null) {
            fp.setStartDate(fp.getFacetFilter().getStartDate());
            fp.setEndDate(fp.getFacetFilter().getEndDate());
        }
        /*fp.setStartDateNC(fp.getStartDate() != null ? Utils.getStartDateNCForFilter(fp.getStartDate()) : null);
        fp.setEndDateNC(fp.getEndDate() != null ? Utils.getEndDateNCForFilter(fp.getEndDate()) : null);*/
    }

    void validateStockAvailability(NewInvoice dto) throws RestException {
        List<QuantityItem> quantityItems = Arrays.stream(dto.getItems())
                .filter(invItem -> invItem.getItemID() != null && invItem.getQuantity().compareTo(BigDecimal.ZERO) > 0)
                .map(invItem -> {
                    QuantityItem qtyItem = new QuantityItem();
                    qtyItem.setId(invItem.getItemID());
                    qtyItem.setQuantity(invItem.getQuantity());
                    qtyItem.setWarehouseID(invItem.getWarehouse() != null ? invItem.getWarehouse().getId() : null);
                    return qtyItem;
                }).toList();
        SelectItem[] stocks = invoiceServiceLocal.validateStockAvailability(quantityItems.toArray(new QuantityItem[]{}), dto.getID(), StockOutFlow.FROM_SALE_INVOICE, null);
        if (stocks.length > 0) {
            StringBuilder itemNames = new StringBuilder();
            StringBuilder bookingReservation = new StringBuilder();
            for (int i = 0; i < stocks.length; i++) {
                if (i != 0) {
                    itemNames.append(", ");
                }
                itemNames.append("\"").append(stocks[i].getName()).append("\"");
                if (stocks[i].getDescription() != null && stocks[i].getDescription().length() > 0) {
                    if (i != 0) {
                        bookingReservation.append(", ");
                    }
                    bookingReservation.append("\"(").append(stocks[i].getDescription()).append(")\"");
                }
            }
            if (bookingReservation.length() > 0) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Item " + itemNames + " is booked in " + bookingReservation + ". You cannot deliver booked product unless you cancel these bookings.", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            } else {
                throw new RestException(GENERAL_ERROR_MESSAGE, "You don’t have sufficient quantity of " + itemNames + " available to sell in your warehouse. Please adjust stock quantity of the inventory in order to proceed.", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
    }

    @Autowired
    public final void setDtoUtils(InvoiceDtoUtils dtoUtils) {
        this.dtoUtils = dtoUtils;
    }

    @Autowired
    public final void setAccountingManager(AccountingManager accountingManager) {
        this.accountingManager = accountingManager;
    }

    @Autowired
    public final void setReferenceManager(ReferenceManager referenceManager) {
        this.referenceManager = referenceManager;
    }

    @Autowired
    public final void setFinancialSettingsManager(FinancialSettingsManager financialSettingsManager) {
        this.financialSettingsManager = financialSettingsManager;
    }

    @Autowired
    public final void setCrmServiceLocal(CrmServiceLocal crmServiceLocal) {
        this.crmServiceLocal = crmServiceLocal;
    }

    @Autowired
    public final void setCrmAccountManager(CrmAccountManager crmAccountManager) {
        this.crmAccountManager = crmAccountManager;
    }

    @Autowired
    public final void setQuoteServiceLocal(QuoteServiceLocal quoteServiceLocal) {
        this.quoteServiceLocal = quoteServiceLocal;
    }

    @Autowired
    public final void setInvoiceServiceLocal(InvoiceServiceLocal invoiceServiceLocal) {
        this.invoiceServiceLocal = invoiceServiceLocal;
    }

    @Autowired
    public final void setInvoiceAPIService(InvoiceAPIService invoiceAPIService) {
        this.invoiceAPIService = invoiceAPIService;
    }

    @Autowired
    public final void setQuoteManager(QuoteManager quoteManager) {
        this.quoteManager = quoteManager;
    }

    @Autowired
    public final void setInvoiceManager(InvoiceManager invoiceManager) {
        this.invoiceManager = invoiceManager;
    }

    @Autowired
    public final void setCurrencyManager(CurrencyManager currencyManager) {
        this.currencyManager = currencyManager;
    }

    @Autowired
    public final void setStringXSync(XSync<String> stringXSync) {
        this.stringXSync = stringXSync;
    }

    @Autowired
    public void setCurrencyService(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    @Autowired
    public void setItemManager(ItemManager itemManager) {
        this.itemManager = itemManager;
    }

    @Autowired
    public void setQuoteService(QuoteService quoteService) {
        this.quoteService = quoteService;
    }
    @Autowired
    public void setApproverManager(ApproverManager approverManager) {
        this.approverManager = approverManager;
    }@Autowired
    public void setAllInOneService(AllInOneService allInOneService) {
        this.allInOneService = allInOneService;
    }
}
