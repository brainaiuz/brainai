package com.edatasite.workforce.rest.v1.release10.accounting;

import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.accounting.EdsBaseSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsDiscount;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseInvoice;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.accounting.server.app.ProductServiceLocal;
import com.edatasite.workforce.gwt.client.client.rpc.ClientServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.InvoiceTermsItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.EmailTemplateServiceLocal;
import com.edatasite.workforce.gwt.core.server.db.AddressManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.SaveResult;
import com.edatasite.workforce.gwt.invoice.client.rpc.ShippingMethod;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceCircularResolver;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.edatasite.workforce.gwt.invoice.server.app.QuoteServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.helpers.MListingFilterParameter;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.edatasite.workforce.rest.base.to.AddressTO;
import com.edatasite.workforce.rest.base.to.CustomFieldTO;
import com.edatasite.workforce.rest.base.to.InvoiceTO;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.base.to.SelectItemTO;
import com.edatasite.workforce.rest.base.to.ShippingMethodTO;
import com.edatasite.workforce.rest.v1.release10.core.BaseApiControllerV1;
import com.google.common.collect.Lists;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Umidbek.
 */
@Tag(name = "Sales", description = "Sales API")
@RestController
@RequestMapping(value = "/sales", headers = {ApiConstants.SESSION_ID, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiSalesControllerV1 extends BaseApiControllerV1 implements ApiConstants {

    @Autowired
    private InvoiceCircularResolver invoiceCircularResolver;
    @Autowired
    private InvoiceServiceLocal invoiceServiceLocal;
    @Autowired
    private ClientServiceLocal clientServiceLocal;
    @Autowired
    private QuoteServiceLocal quoteServiceLocal;
    @Autowired
    private AccountingServiceLocal accountingServiceLocal;
    @Autowired
    private ProductServiceLocal productServiceLocal;
    @Autowired
    private ItemManager itemManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private EmailTemplateServiceLocal emailTemplateServiceLocal;
    @Autowired
    private AddressManager addressManager;


    @RequestMapping(value = "/{type}/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getList(@PathVariable(value = "type") String type,
                          @RequestBody MListingFilterParameter mFilterParameter) {
        ListingFilterParameter filterParameter = mFilterParameter.convertToFilterParameters();
        ListResult<NewInvoice> salesList = new ListResult<>();
        switch (type) {
            case SALES_INVOICE -> salesList = invoiceCircularResolver.getSaleInvoiceData(filterParameter);
            case PURCHASE_INVOICE -> salesList = invoiceServiceLocal.getPurchaseInvoiceDataFromSolr(filterParameter);
            case PURCHASE_ORDER -> salesList = quoteServiceLocal.getPurchaseOrderData(filterParameter);
            case CREDIT_NOTE -> {
                filterParameter.setCreditNote(true);
                salesList = invoiceCircularResolver.getSaleInvoiceData(filterParameter);
            }
            case SALES_ORDER -> salesList = invoiceCircularResolver.getSaleOrderData(filterParameter);
            case SALES_QUOTE -> salesList = invoiceCircularResolver.getSaleQuoteData(filterParameter);
        }
        ArrayList<InvoiceTO> result = new ArrayList<>();
        for (NewInvoice invoice : salesList.getList()) {
            result.add(new InvoiceTO(invoice));
        }
        if (result.size() > 0) {
            return successResponse(new ListResultTO<>(salesList.getTotal(), result));
        } else {
            return this.infoResponse(ERROR_RESOURCE_NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{type}/{id}", method = RequestMethod.GET)
    public Object get(@PathVariable(value = "type") String type,
                      @PathVariable(value = "id") Integer id) {
        return switch (type) {
            case SALES_INVOICE ->
                    successResponse(new InvoiceTO(invoiceServiceLocal.getInvoiceSummaryData(id), SALES_INVOICE));
            case PURCHASE_INVOICE ->
                    successResponse(new InvoiceTO(invoiceServiceLocal.getInvoiceSummaryData(id), PURCHASE_INVOICE));
            case PURCHASE_ORDER ->
                    successResponse(new InvoiceTO(quoteServiceLocal.getQuoteSummaryData(id), PURCHASE_ORDER));
            case CREDIT_NOTE ->
                    successResponse(new InvoiceTO(invoiceServiceLocal.getCreditNoteSummaryData(id), CREDIT_NOTE));
            case SALES_ORDER, SALES_QUOTE ->
                    successResponse(new InvoiceTO(quoteServiceLocal.getQuoteSummaryData(id), SALES_QUOTE));
            default -> this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
        };
    }

    @RequestMapping(value = "/{type}/getByNumber", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getByNumber(@PathVariable(value = "type") String type,
                              @RequestBody MListingFilterParameter mFilterParameter) {
        String invoiceType = null;
        if (SALES_INVOICE.equals(type)) {
            invoiceType = Constants.INVOICE;
        } else if (CREDIT_NOTE.equals(type)) {
            invoiceType = Constants.CREDIT_NOTE;
        } else if (PURCHASE_INVOICE.equals(type)) {
            invoiceType = Constants.PURCHASE_INVOICE;
        } else if (SALES_QUOTE.equals(type)) {
            invoiceType = Constants.SALE_QUOTE;
        } else if (SALES_ORDER.equals(type)) {
            invoiceType = Constants.SALE_ORDER;
        } else if (PURCHASE_ORDER.equals(type)) {
            invoiceType = Constants.PURCHASE_ORDER;
        }
        if (StringUtils.isBlank(mFilterParameter.getNumber())) {
            return errorResponse("Number field is required");
        }
        ListResult<NewInvoice> salesList = invoiceServiceLocal.getInvoiceByNumber(mFilterParameter.getNumber(), invoiceType);

        ArrayList<InvoiceTO> result = new ArrayList<>();
        for (NewInvoice invoice : salesList.getList()) {
            result.add(new InvoiceTO(invoice, type));
        }
        if (result.size() > 0) {
            return successResponse(new ListResultTO<>(salesList.getTotal(), result));
        } else {
            return this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
    }

    /*@RequestMapping(value = "/{type}/{id}", method = RequestMethod.DELETE)
    public Object delete(@PathVariable(value = "type") String type,
                         @PathVariable(value = "id") Integer id) {
        try {
            if (SALES_INVOICE.equals(type) || CREDIT_NOTE.equals(type) || PURCHASE_INVOICE.equals(type)) {
                type = SALES_INVOICE.equals(type) || CREDIT_NOTE.equals(type) ? Constants.SALE_INVOICE : Constants.PURCHASE_INVOICE;
                invoiceServiceLocal.deleteInvoice(id, type);
            } else if (SALES_ORDER.equals(type) || SALES_QUOTE.equals(type) || PURCHASE_ORDER.equals(type)) {
                type = PURCHASE_ORDER.equals(type) ? Constants.PURCHASE_ORDER : Constants.SALE_QUOTE;
                quoteServiceLocal.deleteQuote(id, type);
            } else {
                return this.errorResponse(ERROR_RESOURCE_NOT_FOUND);
            }
            return successResponse(SUCCESS_DELETE);
        } catch (Exception e) {
            e.printStackTrace();
            return errorResponse(ERROR_FAIL_DELETE);
        }
    }*/

    @RequestMapping(value = "/{type}/{status}", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Object add(@PathVariable(value = "type") String type,
                      @PathVariable(value = "status") String status,
                      @RequestBody InvoiceTO invoiceTO) {
        SaveResult saveResult = new SaveResult();
        NewInvoice invoice = invoiceTO.wrap(invoiceTO);
        invoice.setStatusCode(status.toUpperCase());
        invoice.setFromApi(true);
        if (SALES_INVOICE.equals(type)) {
            invoice.setType(Constants.RECEIVABLE);
            if (invoiceTO.getCurrency() == null || invoiceTO.getCurrency().getId() == null || invoiceTO.getCurrency().getId() == 0) {
                errorResponse("Currency Field is required");
            }
            invoice.setInvoiceType(1);
            if (invoice.getTaxCalculationType() == null) {
                invoice.setTaxCalculationType(0);
            }
            if (invoice.getID() == null || invoice.getID() == 0) {
                invoice.setNumberData(invoiceServiceLocal.getSaleInvoiceNumber());
                invoice.setInvoiceNumber(invoice.getNumberData().getInvoiceNumber());
                saveResult = invoiceServiceLocal.saveSaleInvoice(invoice);
            } else {
                saveResult = invoiceServiceLocal.updateSaleInvoice(invoice);
            }
        } else if (CREDIT_NOTE.equals(type)) {
            if (invoiceTO.getCustomer() != null) {
                invoice.setType(Constants.RECEIVABLE);
            } else if (invoiceTO.getSupplier() != null) {
                invoice.setType(Constants.PAYABLE);
            } else {
                return errorResponse(ERROR_RESOURCE_NOT_FOUND);
            }
            if (invoice.getID() == null) {
                invoice.setNumberData(invoiceServiceLocal.getCreditNoteNumber());
                invoice.setInvoiceNumber(invoice.getNumberData().getInvoiceNumber());
                saveResult = invoiceServiceLocal.saveCreditNote(invoice);
            } else {
                saveResult = invoiceServiceLocal.updateCreditNote(invoice);
            }
        } else if (SALES_QUOTE.equals(type) || SALES_ORDER.equals(type)) {
            type = SALES_QUOTE.equals(type) ? Constants.SALE_QUOTE : Constants.SALE_ORDER;
            if (invoiceTO.getId() == null) {
                invoice.setNumberData(invoiceCircularResolver.getQuoteOrderNumberData(type));
                invoice.setInvoiceNumber(invoice.getNumberData().getInvoiceNumber());
                if (SALES_ORDER.equals(type)) {
                    invoice.setSalesOrder(true);
                }
                saveResult = quoteServiceLocal.saveSaleQuote(invoice);
            } else {
                saveResult = quoteServiceLocal.updateSaleQuote(invoice);
            }
        } else if (PURCHASE_ORDER.equals(type)) {
            type = Constants.PURCHASE_ORDER;
            if (invoiceTO.getId() == null) {
                invoice.setNumberData(invoiceCircularResolver.getQuoteOrderNumberData(type));
                invoice.setInvoiceNumber(invoice.getNumberData().getInvoiceNumber());
                saveResult = quoteServiceLocal.savePurchaseOrder(invoice);
            } else {
                boolean checkForUnallocatedExpenses = Constants.REVERSED.equalsIgnoreCase(invoice.getStatusCode());
                saveResult = quoteServiceLocal.updatePurchaseOrder(invoice, checkForUnallocatedExpenses);
            }
        }

        if (saveResult.isInvoiceExist()) {
            return infoResponse("Number already exists.");
        } else if (saveResult.getExceededCreditLimit()) {
            return infoResponse("You cannot create more than Credit Limit");
        } else if (saveResult.isPaymentExist()) {
            return infoResponse("Please note that you cannot edit which has payment");
        } else {
            return successResponse(SUCCESS_SAVE, new SelectItemTO(saveResult.getId(), saveResult.getNumber()));
        }
    }

    @RequestMapping(value = "/{type}/{id}/{status}", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public Object update(@PathVariable(value = "type") String type,
                         @PathVariable(value = "id") Integer id,
                         @PathVariable(value = "status") String status,
                         @RequestBody InvoiceTO invoiceTO) {

        invoiceTO.setId(id);
        return add(status, type, invoiceTO);
    }

    @RequestMapping(value = "/{type}/items", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getItems(@PathVariable(value = "type") String type,
                           @RequestBody MListingFilterParameter mFilterParameter) {
        ListingFilterParameter filterParameter = mFilterParameter.convertToFilterParameters();
        filterParameter.setLookUp(true);
        if (SALES_INVOICE.equals(type) || CREDIT_NOTE.equals(type) || SALES_QUOTE.equals(type) || SALES_ORDER.equals(type)) {
            filterParameter.setInvoiceType(Constants.RECEIVABLE);
        } else if (PURCHASE_ORDER.equals(type) || PURCHASE_INVOICE.equals(type)) {
            filterParameter.setInvoiceType(Constants.PAYABLE);
        }
        return successResponse(WrapUtils.wrapSelectItemList(productServiceLocal.getCompanyProductsByType(filterParameter)));
    }

    @RequestMapping(value = "/{itemId}/warehouses", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getWarehouses(@RequestBody MListingFilterParameter mListingFilterParameter) {
        ListingFilterParameter filterParameter = mListingFilterParameter.convertToFilterParameters();
        filterParameter.setLookUp(true);
        return successResponse(WrapUtils.wrapSelectItemList(accountingServiceLocal.getWarehousesForLookUp(filterParameter)));
    }

    @RequestMapping(value = "/taxRates", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getTaxRates(@RequestBody MListingFilterParameter mListingFilterParameter) {
        ListingFilterParameter filterParameter = mListingFilterParameter.convertToFilterParameters();
        filterParameter.setLookUp(true);
        return successResponse(WrapUtils.wrapSelectItemList(accountingServiceLocal.getWarehousesForLookUp(filterParameter)));
    }

    @RequestMapping(value = "/{type}/salesAccounts", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getSalesAccounts(@PathVariable(value = "type") String type,
                                   @RequestBody MListingFilterParameter mListingFilterParameter) {
        ListingFilterParameter filterParameter = mListingFilterParameter.convertToFilterParameters();
        if (SALES_INVOICE.equals(type) || CREDIT_NOTE.equals(type) || SALES_QUOTE.equals(type) || SALES_ORDER.equals(type)) {
            filterParameter.setInvoiceType(Constants.RECEIVABLE);
        } else if (PURCHASE_ORDER.equals(type) || PURCHASE_INVOICE.equals(type)) {
            filterParameter.setInvoiceType(Constants.PAYABLE);
        }
        filterParameter.setLookUp(true);

        ArrayList<String> types = null;
        if (!"".equals(filterParameter.getInvoiceType())) {
            types = Lists.newArrayList(filterParameter.getInvoiceType());
        }
        return successResponse(WrapUtils.wrapSelectItemList(accountingServiceLocal.getAccountsForInvoice(filterParameter, types)));
    }

    @RequestMapping(value = "/{itemId}/discounts", method = RequestMethod.GET)
    public Object getDiscounts(@PathVariable(value = "itemId") Integer itemId) {
        ArrayList<SelectItemTO> discounts = new ArrayList<>();
        discounts.add(new SelectItemTO(Constants.ONE_OFF_DISCOUNT, "Percentage"));
        discounts.add(new SelectItemTO(Constants.ONE_OFF_FIXED_AMOUNT, "Fixed Amount"));
        if (itemId != null) {
            EdsItem edsItem = itemManager.get(itemId);
            if (edsItem != null && edsItem.getDiscounts() != null) {
                for (EdsDiscount discount : edsItem.getDiscounts()) {
                    discounts.add(new SelectItemTO(discount.getObjectID(), discount.getName(), discount.getCode(), (discount.getFixedAmount() != null ? String.valueOf(discount.getFixedAmount()) : String.valueOf(discount.getPercentage()))));
                }
            }
        }
        return successResponse(discounts);
    }

    @RequestMapping(value = "/shippingMethods", method = RequestMethod.GET)
    public Object getShippingMethods() {
        ShippingMethod[] shippingMethods = invoiceServiceLocal.getShippinhMethodsForLookUp(null);
        ArrayList<ShippingMethodTO> result = new ArrayList<>();
        for (ShippingMethod shippingMethod : shippingMethods) {
            result.add(new ShippingMethodTO(shippingMethod));
        }
        return successResponse(result);
    }

    @RequestMapping(value = "/terms", method = RequestMethod.GET)
    public Object getTerms() {
        InvoiceTermsItem[] invoiceTermsItems = clientServiceLocal.getInvoiceTermsForLookUp(new ListingFilterParameter());
        ArrayList<SelectItemTO> result = new ArrayList<>();
        for (InvoiceTermsItem item : invoiceTermsItems) {
            result.add(new SelectItemTO(item.getId(), item.getName(), String.valueOf(item.getDays()), item.getDescription()));
        }
        return successResponse(result);
    }

    @RequestMapping(value = "/taxTypes", method = RequestMethod.GET)
    public Object getTaxTypes() {
        ArrayList<SelectItemTO> taxTypes = new ArrayList<>();
        taxTypes.add(new SelectItemTO(0, "No Tax"));
        taxTypes.add(new SelectItemTO(1, "Tax Inclusive"));
        taxTypes.add(new SelectItemTO(2, "Tax Exclusive"));
        return successResponse(taxTypes);
    }

    @RequestMapping(value = "/salesTypes", method = RequestMethod.GET)
    public Object getSalesTypes() {
        ArrayList<String> salesTypes = new ArrayList<>();
        salesTypes.add(SALES_INVOICE);
        salesTypes.add(PURCHASE_INVOICE);
        salesTypes.add(PURCHASE_ORDER);
        salesTypes.add(SALES_ORDER);
        salesTypes.add(SALES_QUOTE);
        salesTypes.add(CREDIT_NOTE);
        return successResponse(salesTypes);
    }

    @RequestMapping(value = "/invoiceTypes", method = RequestMethod.GET)
    public Object getInvoiceTypes() {
        ArrayList<SelectItemTO> invoiceTypes = new ArrayList<>();
        invoiceTypes.add(new SelectItemTO(AccountingConstants.PRODUCT_INVOICE_TYPE, "Product Invoice"));
        invoiceTypes.add(new SelectItemTO(AccountingConstants.SERVICE_INVOICE_TYPE, "Service Invoice"));
        return successResponse(invoiceTypes);
    }

    @RequestMapping(value = "/bankAccounts", method = RequestMethod.GET)
    public Object getBankAccounts() {
        return successResponse(WrapUtils.wrapCheckListItemTOs(accountingServiceLocal.getBankAccountItems()));
    }

    @RequestMapping(value = "/{accountType}/accounts", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getAccounts(@PathVariable(value = "accountType") String accountType,
                              @RequestBody MListingFilterParameter mListingFilterParameter) {
        ListingFilterParameter filterParameter = mListingFilterParameter.convertToFilterParameters();
        filterParameter.setAccountType(accountType);
        ArrayList<AccountItem> accountItems = accountingServiceLocal.getAccountsReceivablePayable(filterParameter);
        ArrayList<SelectItemTO> result = new ArrayList<>();
        for (AccountItem item : accountItems) {
            result.add(new SelectItemTO(item.getId(), item.getName(), item.getAccountTypeCode(), item.getDescription()));
        }

        return successResponse(result);
    }

    @RequestMapping(value = "/{type}/statuses", method = RequestMethod.GET)
    public Object getStatuses() {
        return successResponse(WrapUtils.wrapSelectItemTOs(referenceManager.getAsSelectItems(Constants.INVOICE_STATUS)));
    }

    @RequestMapping(value = "/{type}/{number}/status", method = RequestMethod.GET)
    public Object getStatus(@PathVariable(value = "type") String type,
                            @PathVariable(value = "number") String number) {
        if (SALES_INVOICE.equals(type) || CREDIT_NOTE.equals(type)) {
            List<EdsBaseSaleInvoice> saleInvoices = invoiceManager.getSaleInvoiceByNumber(number, null);
            for (EdsBaseSaleInvoice invoice : saleInvoices) {
                if (invoice.getStatus() != null) {
                    return successResponse(new SelectItemTO(invoice.getStatus().getObjectID(), invoice.getStatus().getName(), invoice.getStatus().getCode(), invoice.getStatus().getDescription()));
                }
            }
        } else if (PURCHASE_INVOICE.equals(type)) {
            List<EdsPurchaseInvoice> purchaseInvoices = invoiceManager.getPurchaseInvoiceByNumber(number, null, null);
            for (EdsPurchaseInvoice invoice : purchaseInvoices) {
                if (invoice.getStatus() != null) {
                    return successResponse(new SelectItemTO(invoice.getStatus().getObjectID(), invoice.getStatus().getName(), invoice.getStatus().getCode(), invoice.getStatus().getDescription()));
                }
            }
        }
        return errorResponse();
    }

    @RequestMapping(value = "/{type}/customFields/{number}", method = RequestMethod.GET)
    public Object getCustomFields(@PathVariable(value = "type") String type, @PathVariable(value = "number") String number) {
        ArrayList<CompanyCustomFieldItem> customFieldItems = new ArrayList<>();
        if (SALES_INVOICE.equals(type)) {
            customFieldItems = invoiceServiceLocal.getSalesInvoiceCustomFieldsByNumber(number);
        }
        if (customFieldItems == null || customFieldItems.isEmpty()) {
            return this.successResponse(ERROR_RESOURCE_NOT_FOUND);
        }
        return successResponse(customFieldItems);
    }

    @RequestMapping(value = "/{type}/customFields", method = RequestMethod.GET)
    public Object getSalesCustomFields(@PathVariable(value = "type") String relationType) {
        ViewName viewName = null;
        if (SALES_INVOICE.equals(relationType) || CREDIT_NOTE.equals(relationType)) {
            viewName = ViewName.SaleInvoice;
        } else if (PURCHASE_INVOICE.equals(relationType)) {
            viewName = ViewName.PurchaseInvoice;
        } else if (SALES_QUOTE.equals(relationType) || SALES_ORDER.equals(relationType)) {
            viewName = ViewName.SaleQuote;
        } else if (PURCHASE_ORDER.equals(relationType)) {
            viewName = ViewName.PurchaseOrder;
        } else if (SALES_INVOICE_ITEM.equals(relationType)) {
            viewName = ViewName.SaleInvoiceItem;
        } else if (PURCHASE_INVOICE_ITEM.equals(relationType)) {
            viewName = ViewName.PurchaseInvoiceItem;
        } else if (PURCHASE_ORDER_ITEM.equals(relationType)) {
            viewName = ViewName.PurchaseOrderItem;
        } else if (SALES_QUOTE_ITEM.equals(relationType)) {
            viewName = ViewName.SaleQuoteItem;
        } else if (SALES_ORDER_ITEM.equals(relationType)) {
            viewName = ViewName.SaleOrderItem;
        }
        if (viewName == null)
            return errorResponse(ERROR_INVALID_QUERY_PARAM_LIST);

        List<CompanyCustomFieldItem> customFields = commonServiceLocal.getCompanyCustomFields(viewName);
        ArrayList<CustomFieldTO> result = new ArrayList<>();
        for (CompanyCustomFieldItem customField : customFields) {
            result.add(new CustomFieldTO(customField));
        }
        if (result.size() > 0) {
            return successResponse(ERROR_RESOURCE_NOT_FOUND, result);
        } else {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{type}/{id}/customFields", method = RequestMethod.GET)
    public Object getCustomFieldsBySalesId(@PathVariable(value = "type") String relationType,
                                           @PathVariable(value = "id") Integer entityId) {
        List<CompanyCustomFieldItem> invoiceCustomFields = new ArrayList<>();
        if (SALES_INVOICE.equals(relationType) || CREDIT_NOTE.equals(relationType) || PURCHASE_INVOICE.equals(relationType)) {
            ViewName viewName = PURCHASE_INVOICE.equals(relationType) ? ViewName.PurchaseInvoice : ViewName.SaleInvoice;
            invoiceCustomFields = invoiceServiceLocal.getInvoiceCustomFields(entityId, viewName);
        } else if (SALES_QUOTE.equals(relationType) || SALES_ORDER.equals(relationType) || PURCHASE_ORDER.equals(relationType)) {
            ViewName viewName = PURCHASE_ORDER.equals(relationType) ? ViewName.PurchaseOrder : ViewName.SaleQuote;
            invoiceCustomFields = quoteServiceLocal.getQuoteCustomFields(entityId, viewName);
        }

        ArrayList<CustomFieldTO> result = new ArrayList<>();
        for (CompanyCustomFieldItem customField : invoiceCustomFields) {
            result.add(new CustomFieldTO(customField));
        }
        if (result.size() > 0) {
            return successResponse(ERROR_RESOURCE_NOT_FOUND, result);
        } else {
            return errorResponse(ERROR_RESOURCE_NOT_FOUND);
        }
    }

    @RequestMapping(value = "/{type}/{id}/customFields", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object saveCustomField(@PathVariable(value = "type") String relationType,
                                  @PathVariable(value = "id") Integer entityId,
                                  @RequestBody ArrayList<CustomFieldTO> customFieldTOs) {

        try {
            List<CompanyCustomFieldItem> customFields = new ArrayList<>();
            for (CustomFieldTO customFieldTO : customFieldTOs) {
                CompanyCustomFieldItem customField = customFieldTO.convertToCustomField();
                customFields.add(customField);
            }
            if (SALES_INVOICE.equals(relationType) || CREDIT_NOTE.equals(relationType) || PURCHASE_INVOICE.equals(relationType)) {
                invoiceServiceLocal.createInvoiceCustomFields(entityId, customFields);
            } else if (SALES_QUOTE.equals(relationType) || SALES_ORDER.equals(relationType) || PURCHASE_ORDER.equals(relationType)) {
                quoteServiceLocal.createQuoteCustomFields(entityId, customFields);
            }
            return successResponse(SUCCESS_SAVE);
        } catch (Exception e) {
            return errorResponse(ERROR_FAILED_SAVE);
        }

    }

    @RequestMapping(value = "/{type}/emailTemplates", method = RequestMethod.GET)
    public Object getEmailTemplates(@PathVariable(value = "type") String relationType) {
        if (SALES_ORDER.equals(relationType)) {
            return successResponse(WrapUtils.wrapSelectItemList(emailTemplateServiceLocal.getEmailTemplates(Constants.SALES_ORDER_CATEGORY)));
        }

        return errorResponse();
    }

    @RequestMapping(value = "/{type}/{clientId}/addresses", method = RequestMethod.GET)
    public Object getBillShipToAddress(@PathVariable(value = "clientId") Integer clientId) {

        HashMap<String, ArrayList<AddressTO>> addressMap = new HashMap<>();

        List<EdsAddress> billAddresses = addressManager.getAddressesByEntityIdAndType(clientId, EdsAddress.BILLING_ADDRESS, EdsAddress.ENTITY_TYPE_CRM_ACCOUNT);
        List<EdsAddress> mailAddresses = addressManager.getAddressesByEntityIdAndType(clientId, EdsAddress.MAILING_ADDRESS, EdsAddress.ENTITY_TYPE_CRM_ACCOUNT);

        if (billAddresses != null && billAddresses.size() > 0) {
            ArrayList<AddressTO> billingAddressList = new ArrayList<>();
            for (EdsAddress address : billAddresses) {
                billingAddressList.add(new AddressTO(address));
            }
            addressMap.put("billToAddress", billingAddressList);
        }

        if (mailAddresses != null && mailAddresses.size() > 0) {
            ArrayList<AddressTO> mailingAddressList = new ArrayList<>();
            for (EdsAddress address : mailAddresses) {
                mailingAddressList.add(new AddressTO(address));
            }
            addressMap.put("shipToAddress", mailingAddressList);
        }
        if (addressMap.isEmpty()) {
            return errorResponse();
        }
        return successResponse(addressMap);

    }
}
