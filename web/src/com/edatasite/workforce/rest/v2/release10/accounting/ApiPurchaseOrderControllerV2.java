package com.edatasite.workforce.rest.v2.release10.accounting;

import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AddressManager;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.SaveResult;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceCircularResolver;
import com.edatasite.workforce.gwt.invoice.server.app.QuoteServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.AddressTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.CurrencyTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.InvoiceItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.InvoiceListItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.InvoiceSalesAccountTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.InvoiceStatusTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.ItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.PurchaseOrderAddResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.SupplierItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.WarehouseTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.order.PurchaseOrderTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.IdNameTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.RequestListSearchData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseResultListData;
import com.edatasite.workforce.rest.v2.release10.enums.InvoiceStatusEnum;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by Dilsh0d on 11/2/2017.
 */
@Tag(name = "Purchase Order", description = "Purchase Order API")
@RestController()
@RequestMapping(value = "/2", headers = {ApiConstants.X_AUTH, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiPurchaseOrderControllerV2 extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiPurchaseOrderControllerV2.class);

    @Autowired
    private AddressManager addressManager;
    @Autowired
    private InvoiceCircularResolver invoiceCircularResolver;
    @Autowired
    private CurrencyServiceLocal currencyServiceLocal;
    @Autowired
    private QuoteServiceLocal quoteServiceLocal;


    @Operation(summary = "Get Purchase Order List", description = "Retrieves Purchase Order List by search_text")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have Purchase Order list "),
            @ApiResponse(responseCode = "400", description = "Start point and limit is required"),
            @ApiResponse(responseCode = "422", description = "Start point and limit can not be zero at the same time"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/purchase_order/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getList(@RequestBody RequestListSearchData requestListSearchData) throws RestException {

        if (requestListSearchData.getStart() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Start point is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (requestListSearchData.getLimit() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Limit is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (requestListSearchData.getStart().equals(requestListSearchData.getLimit()) && requestListSearchData.getLimit() == 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Start point and limit can not be zero at the same time", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setStart(requestListSearchData.getStart());
        filterParameter.setLimit(requestListSearchData.getLimit());
        filterParameter.setSearchKey(requestListSearchData.getSearch_text());

        ListResult<NewInvoice> purchaseOrderList;
        try {
            purchaseOrderList = quoteServiceLocal.getPurchaseOrderData(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ArrayList<InvoiceListItemTO> resultList = new ArrayList<>();
        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        for (NewInvoice item : purchaseOrderList.getList()) {
            InvoiceListItemTO purshaseOrder = new InvoiceListItemTO();
            purshaseOrder.setId(item.getID());
            purshaseOrder.setInvoice_number(item.getInvoiceNumber());
            purshaseOrder.setInvoice_date(longDateTimezoneFormat.format(item.getInvoiceDate().getNonConvertedDate()));
            purshaseOrder.setInvoice_status(new InvoiceStatusTO(item.getStatus(), item.getStatusCode()));
            purshaseOrder.setInvoice_total(item.getTotal());

            CurrencyTO currency = new CurrencyTO();
            currency.setCurrency_id(item.getCurrencyID());
            currency.setCurrency_name(item.getCurrencyName());

            purshaseOrder.setInvoice_currency(currency);

            resultList.add(purshaseOrder);
        }
        ResponseResultListData<InvoiceListItemTO> resultListData = new ResponseResultListData<>();
        resultListData.setList(resultList);
        resultListData.setTotal(purchaseOrderList.getTotal());
        return successResponse(resultListData);

    }

    @Operation(summary = "Add New Purchase Order", description = "Adds new Purchase Order")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with error code "),
            @ApiResponse(responseCode = "404", description = "Invoice data is not found"),
            @ApiResponse(responseCode = "400", description = "Status is required or Invoice date is required or due date is required"),
            @ApiResponse(responseCode = "422", description = "Invalid Purchase Order status or Invalid invoice date format"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/purchase_order/create", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object createPurchaseOrder(@RequestBody PurchaseOrderTO purchaseOrderTO) throws RestException {

        //Start Validation
        if (purchaseOrderTO.getStatus() == null || StringUtils.isBlank(purchaseOrderTO.getStatus().getStatus_code())) {
            throw new RestException("status is required", "status is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (InvoiceStatusEnum.getStatus(purchaseOrderTO.getStatus().getStatus_code()) == null) {
            throw new RestException("Invalid status", "Invalid invoice status " + purchaseOrderTO.getStatus().getStatus_code(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (StringUtils.isBlank(purchaseOrderTO.getInvoice_date())) {
            throw new RestException("date not provided", "Invoice date is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(purchaseOrderTO.getDue_date())) {
            throw new RestException("due date is not provided", "Invoice due date is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        try {
            longDateTimezoneFormat.parse(purchaseOrderTO.getInvoice_date());
        } catch (ParseException e) {
            log.error("", e);
            throw new RestException("Invalid invoice date format", "Invalid date format. Acceptable format for invoice is " + longDateTimezoneFormat.toPattern(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        try {
            longDateTimezoneFormat.parse(purchaseOrderTO.getDue_date());
        } catch (ParseException e) {
            log.error("", e);
            throw new RestException("Invalid due date format", "Invalid date format. Acceptable format for invoice is " + longDateTimezoneFormat.toPattern(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (purchaseOrderTO.getSupplier() == null || purchaseOrderTO.getSupplier().getSupplier_id() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Invoice customer is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (purchaseOrderTO.getItems() == null || purchaseOrderTO.getItems().isEmpty()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Invoice item is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        for (InvoiceItemTO invoiceItem : purchaseOrderTO.getItems()) {
            if (invoiceItem.getItem() == null || invoiceItem.getItem().getItem_id() == null) {
                throw new RestException("Invoice item not provided", "Invoice item is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }

            if (invoiceItem.getItem_quantity() == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Invoice item quantity is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            if (BigDecimal.ZERO.compareTo(invoiceItem.getItem_quantity()) == 0) {
                throw new RestException("Invalid invoice item quantity", "Invoice item quantity can not be zero", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (invoiceItem.getItem_price() == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Invoice item price is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            if (BigDecimal.ZERO.compareTo(invoiceItem.getItem_price()) == 0) {
                throw new RestException("Invalid invoice item price", "Invoice invoice item price can not be zero", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            /*if (invoiceItem.getItem_sales_account() == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Invoice item sales account is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            if (invoiceItem.getItem_sales_account().getSales_account_id() == null || invoiceItem.getItem_sales_account().getSales_account_id() > 0) {
                throw new RestException("Invoice item sales account is required", "Invoice item sales account is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            if (invoiceItem.getItem_warehouse() == null || invoiceItem.getItem_warehouse().getWarehouse_id() == null) {
                throw new RestException("Invoice item warehouse is required", "Invoice item warehouse is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }*/

        }
        //End Of Validation

        NewInvoice purchaseOrder = convertPurchaseOrder(purchaseOrderTO);

        //Create Purchase Order
        SaveResult saveResult = quoteServiceLocal.savePurchaseOrder(purchaseOrder);

        PurchaseOrderAddResultTO addResultTO = new PurchaseOrderAddResultTO();
        addResultTO.setId(saveResult.getId());
        addResultTO.setNumber(saveResult.getNumber());

        if (saveResult.isInvoiceExist()) {
            throw new RestException("Order number is already used", "Invoice number " + purchaseOrderTO.getInvoice_number() + " is already used", CONFLICT, HttpStatus.CONFLICT);
        }
        return successResponse(addResultTO);
    }

    @Operation(summary = "Update Purchase Order", description = "Update Purchase Order")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with error code "),
            @ApiResponse(responseCode = "404", description = "Invoice data is not found"),
            @ApiResponse(responseCode = "400", description = "Status is required or Invoice date is required or due date is required"),
            @ApiResponse(responseCode = "422", description = "Invalid Purchase Order status or Invalid invoice date format"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/purchase_order/update", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object updatePurchaseOrder(@RequestBody PurchaseOrderTO purchaseOrderTO) throws RestException {

        //Start Validation
        if (purchaseOrderTO.getId() == null || purchaseOrderTO.getId() <= 0) {
            throw new RestException("Purchase order id is invalid", "Purchase order id is invalid", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (purchaseOrderTO.getStatus() == null || StringUtils.isBlank(purchaseOrderTO.getStatus().getStatus_code())) {
            throw new RestException("status is required", "status is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (InvoiceStatusEnum.getStatus(purchaseOrderTO.getStatus().getStatus_code()) == null) {
            throw new RestException("Invalid status", "Invalid invoice status " + purchaseOrderTO.getStatus().getStatus_code(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (StringUtils.isBlank(purchaseOrderTO.getInvoice_date())) {
            throw new RestException("date not provided", "Invoice date is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(purchaseOrderTO.getDue_date())) {
            throw new RestException("due date is not provided", "Invoice due date is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        try {
            longDateTimezoneFormat.parse(purchaseOrderTO.getInvoice_date());
        } catch (ParseException e) {
            log.error("", e);
            throw new RestException("Invalid invoice date format", "Invalid date format. Acceptable format for invoice is " + longDateTimezoneFormat.toPattern(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        try {
            longDateTimezoneFormat.parse(purchaseOrderTO.getDue_date());
        } catch (ParseException e) {
            log.error("", e);
            throw new RestException("Invalid due date format", "Invalid date format. Acceptable format for invoice is " + longDateTimezoneFormat.toPattern(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (purchaseOrderTO.getSupplier() == null || purchaseOrderTO.getSupplier().getSupplier_id() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Invoice customer is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (purchaseOrderTO.getItems() == null || purchaseOrderTO.getItems().isEmpty()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Invoice item is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        for (InvoiceItemTO invoiceItem : purchaseOrderTO.getItems()) {
            if (invoiceItem.getItem() == null || invoiceItem.getItem().getItem_id() == null) {
                throw new RestException("Invoice item not provided", "Invoice item is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }

            if (invoiceItem.getItem_quantity() == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Invoice item quantity is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            if (BigDecimal.ZERO.compareTo(invoiceItem.getItem_quantity()) == 0) {
                throw new RestException("Invalid invoice item quantity", "Invoice item quantity can not be zero", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (invoiceItem.getItem_price() == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Invoice item price is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            if (BigDecimal.ZERO.compareTo(invoiceItem.getItem_price()) == 0) {
                throw new RestException("Invalid invoice item price", "Invoice invoice item price can not be zero", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            /*if (invoiceItem.getItem_sales_account() == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Invoice item sales account is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            if (invoiceItem.getItem_sales_account().getSales_account_id() == null || invoiceItem.getItem_sales_account().getSales_account_id() > 0) {
                throw new RestException("Invoice item sales account is required", "Invoice item sales account is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            if (invoiceItem.getItem_warehouse() == null || invoiceItem.getItem_warehouse().getWarehouse_id() == null) {
                throw new RestException("Invoice item warehouse is required", "Invoice item warehouse is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }*/

        }
        //End Of Validation

        NewInvoice purchaseOrder = convertPurchaseOrder(purchaseOrderTO);
        purchaseOrder.setID(purchaseOrderTO.getId());

        //Create Purchase Order
        SaveResult saveResult = quoteServiceLocal.savePurchaseOrder(purchaseOrder);

        if (saveResult.isInvoiceExist()) {
            throw new RestException("Order number is already used", "Invoice number " + purchaseOrderTO.getInvoice_number() + " is already used", CONFLICT, HttpStatus.CONFLICT);
        }
        return successResponse(new ResponseData());
    }

    @Operation(summary = "Get Purchase Order", description = "Get Purchase Order")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have Purchase Order details "),
            @ApiResponse(responseCode = "404", description = "Invoice data is not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/purchase_order/{id}", method = RequestMethod.GET)
    public Object getPurchaseOrder(@PathVariable(value = "id") Integer id) throws RestException {

        //Start Validation
        if (id == null || id <= 0) {
            throw new RestException("id is required", "id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        NewInvoice purchaseOrder = quoteServiceLocal.getQuoteSummaryData(id);

        if (purchaseOrder != null) {

            PurchaseOrderTO result = convertPurchaseOrder(purchaseOrder);
            return successResponse(result);

        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, GENERAL_ERROR_MESSAGE, SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Operation(summary = "Delete Purchase Order", description = "Delete Purchase Order")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true if its deleted "),
            @ApiResponse(responseCode = "404", description = "Purchase Order not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/purchase_order/delete/{id}", method = RequestMethod.DELETE, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object deletePurchaseOrder(@PathVariable(value = "id") Integer id) throws RestException {

        //Start Validation
        if (id == null || id <= 0) {
            throw new RestException("id is required", "id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        quoteServiceLocal.deleteQuote(id, Constants.PURCHASE_ORDER);

        return successResponse(new ResponseData());

    }

    private NewInvoice convertPurchaseOrder(PurchaseOrderTO purchaseOrderTO) throws RestException {

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        NewInvoice purchaseOrder = new NewInvoice();
        purchaseOrder.setType(Constants.PAYABLE);
        purchaseOrder.setStatusCode(InvoiceStatusEnum.getStatus(purchaseOrderTO.getStatus().getStatus_code()));
        purchaseOrder.setForceSave(true);
        purchaseOrder.setFromApi(true);
        if (StringUtils.isNotBlank(purchaseOrderTO.getInvoice_number())) {
            purchaseOrder.setInvoiceNumber(purchaseOrderTO.getInvoice_number());
        } else {
            purchaseOrder.setNumberData(invoiceCircularResolver.getQuoteOrderNumberData(Constants.PURCHASE_ORDER));
            purchaseOrder.setInvoiceNumber(purchaseOrder.getNumberData().getInvoiceNumber());
        }

        purchaseOrder.setReference(purchaseOrderTO.getReference());
        purchaseOrder.setIntroduction(purchaseOrderTO.getIntroduction());
        purchaseOrder.setPaymentTerms(purchaseOrderTO.getPayment_terms());
        purchaseOrder.setShippingTerms(purchaseOrderTO.getShipping_terms());
        purchaseOrder.setExchageRate(purchaseOrderTO.getExchange_rate());
        purchaseOrder.setComissionAmount(purchaseOrderTO.getComission());
        purchaseOrder.setTotalInInvoiceCurrency(purchaseOrderTO.getTotal_in_purchase_currency());
        purchaseOrder.setTotalDiscount(purchaseOrderTO.getTotal_discount());
        purchaseOrder.setShippingPrice(purchaseOrderTO.getShipping_price());
        purchaseOrder.setTotalTaxes(purchaseOrderTO.getTotal_taxes_in_base_currency());
        purchaseOrder.setTaxCalculationType(purchaseOrderTO.getTax_calculation_type());

        if (purchaseOrderTO.getPrice_level() != null) {
            purchaseOrder.setPriceLevel(new SelectItem(purchaseOrderTO.getPrice_level().getId(), purchaseOrderTO.getPrice_level().getName()));
        }
        if (purchaseOrderTO.getShipping_method() != null) {
            purchaseOrder.setShippingMethodID(purchaseOrderTO.getShipping_method().getId());
        }
        if (purchaseOrderTO.getRelated_project() != null) {
            purchaseOrder.setRelatedProjectID(purchaseOrderTO.getRelated_project().getId());
            purchaseOrder.setRelatedProjectName(purchaseOrderTO.getRelated_project().getName());
        }


        if (purchaseOrderTO.getCurrency() == null || purchaseOrderTO.getCurrency().getCurrency_id() == null) {
            purchaseOrder.setCurrencyID(currencyServiceLocal.getBaseCurrency().getId());
        } else {
            purchaseOrder.setCurrencyID(purchaseOrderTO.getCurrency().getCurrency_id());
        }

        try {
            purchaseOrder.setInvoiceDate(new DateNonConvertable(ServerUtils.getStartDate(longDateTimezoneFormat.parse(purchaseOrderTO.getInvoice_date()))));
            purchaseOrder.setDueDate(new DateNonConvertable(ServerUtils.getEndDate(longDateTimezoneFormat.parse(purchaseOrderTO.getDue_date()))));
        } catch (ParseException e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (purchaseOrderTO.getSupplier() != null) {

            purchaseOrder.setClientID(purchaseOrderTO.getSupplier().getSupplier_id());

            List<EdsAddress> addressList = addressManager.getAddressesByEntityIdAndType(purchaseOrderTO.getSupplier().getSupplier_id(), EdsAddress.BILLING_ADDRESS, EdsAddress.ENTITY_TYPE_CRM_ACCOUNT);

            if (purchaseOrderTO.getSupplier().getBill_to_address() == null || purchaseOrderTO.getSupplier().getBill_to_address().getAddress_id() == null || purchaseOrderTO.getSupplier().getBill_to_address().getAddress_id() <= 0) {
                try {
                    Optional<EdsAddress> optional = addressList.stream().filter(EdsAddress::isPrimary).findFirst();
                    if (optional.isPresent()) {
                        purchaseOrder.setBillAddressID(optional.get().getObjectID());
                    } else {
                        throw new RestException(GENERAL_ERROR_MESSAGE, "Invoice customer primary bill to address is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
                    }
                } catch (Exception e) {
                    log.error("", e);
                    throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
                }
            } else {
                Optional<EdsAddress> optional = addressList.stream().filter(billToAddressId -> billToAddressId.getObjectID().equals(purchaseOrderTO.getSupplier().getBill_to_address().getAddress_id())).findFirst();
                if (optional.isPresent()) {
                    purchaseOrder.setBillAddressID(purchaseOrderTO.getSupplier().getBill_to_address().getAddress_id());
                } else {
                    throw new RestException(GENERAL_ERROR_MESSAGE, "Invoice customer primary bill to address with " + purchaseOrderTO.getSupplier().getBill_to_address().getAddress_id() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
                }

            }
            if (purchaseOrderTO.getSupplier().getShip_to_address() != null) {
                purchaseOrder.setMailAddressID(purchaseOrderTO.getSupplier().getShip_to_address().getAddress_id());
            }
        }


        ArrayList<NewInvoiceItem> invoiceItems = new ArrayList<>();
        BigDecimal invoiceTotal = BigDecimal.ZERO;

        if (purchaseOrderTO.getTotal_in_base_currency() == null) {
            for (InvoiceItemTO invoiceItem : purchaseOrderTO.getItems()) {
                NewInvoiceItem item = new NewInvoiceItem();
                item.setItemID(invoiceItem.getItem().getItem_id());
                item.setItemName(invoiceItem.getItem().getItem_name());
                item.setAccountID(invoiceItem.getItem_sales_account().getSales_account_id());
                item.setQuantity(invoiceItem.getItem_quantity());
                item.setUnitPrice(invoiceItem.getItem_price());
                item.setNet(invoiceItem.getItem_net_amount() == null ? (invoiceItem.getItem_quantity().multiply(invoiceItem.getItem_price())) : invoiceItem.getItem_net_amount());
                item.setWarehouse(new SelectItem(invoiceItem.getItem_warehouse().getWarehouse_id()));
                invoiceTotal = invoiceTotal.add(invoiceItem.getItem_quantity().multiply(invoiceItem.getItem_price()));
            }
        } else {
            for (InvoiceItemTO invoiceItem : purchaseOrderTO.getItems()) {
                NewInvoiceItem item = new NewInvoiceItem();
                if (invoiceItem.getItem() != null) {
                    item.setItemID(invoiceItem.getItem().getItem_id());
                    item.setItemName(invoiceItem.getItem().getItem_name());
                }

                if (invoiceItem.getItem_sales_account() != null) {
                    item.setAccountID(invoiceItem.getItem_sales_account().getSales_account_id());
                }
                item.setQuantity(invoiceItem.getItem_quantity());
                item.setUnitPrice(invoiceItem.getItem_price());
                item.setNet(invoiceItem.getItem_net_amount());
                if (invoiceItem.getItem_warehouse() != null) {
                    item.setWarehouse(new SelectItem(invoiceItem.getItem_warehouse().getWarehouse_id()));
                }
                invoiceItems.add(item);
            }
        }
        purchaseOrder.setItems(invoiceItems.toArray(new NewInvoiceItem[]{}));

        if (purchaseOrderTO.getTotal_in_base_currency() == null) {
            purchaseOrder.setTotal(invoiceTotal);
            purchaseOrder.setSubtotal(invoiceTotal);
        } else {
            purchaseOrder.setTotal(purchaseOrderTO.getTotal_in_base_currency());
            purchaseOrder.setSubtotal(purchaseOrderTO.getSubtotal());
        }
        return purchaseOrder;
    }

    private PurchaseOrderTO convertPurchaseOrder(NewInvoice purchaseOrder) throws RestException {

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        PurchaseOrderTO purchaseOrderTO = new PurchaseOrderTO();
        purchaseOrderTO.setId(purchaseOrder.getID());
        purchaseOrderTO.setStatus(new InvoiceStatusTO(purchaseOrder.getStatusCode(), purchaseOrder.getStatusCode()));
        purchaseOrderTO.setInvoice_number(purchaseOrder.getInvoiceNumber());
        purchaseOrderTO.setReference(purchaseOrder.getReference());
        purchaseOrderTO.setIntroduction(purchaseOrder.getIntroduction());
        purchaseOrderTO.setPayment_terms(purchaseOrder.getPaymentTerms());
        purchaseOrderTO.setShipping_terms(purchaseOrder.getShippingTerms());
        purchaseOrderTO.setExchange_rate(purchaseOrder.getExchageRate());
        purchaseOrderTO.setComission(purchaseOrder.getComissionAmount());
        purchaseOrderTO.setTotal_in_purchase_currency(purchaseOrder.getTotalInInvoiceCurrency());
        purchaseOrderTO.setTotal_discount(purchaseOrder.getTotalDiscount());
        purchaseOrderTO.setShipping_price(purchaseOrder.getShippingPrice());
        purchaseOrderTO.setTotal_taxes_in_base_currency(purchaseOrder.getTotalTaxes());
        purchaseOrderTO.setTax_calculation_type(purchaseOrder.getTaxCalculationType());
        purchaseOrderTO.setTotal_in_purchase_currency(purchaseOrder.getTotalInInvoiceCurrency());

        if (purchaseOrder.getPriceLevel() != null) {
            purchaseOrderTO.setPrice_level(new IdNameTO(purchaseOrder.getPriceLevel().getId(), purchaseOrder.getPriceLevel().getName()));
        }
        if (purchaseOrder.getShippingMethodID() != null) {
            purchaseOrderTO.setShipping_method(new IdNameTO(purchaseOrder.getShippingMethodID(), purchaseOrder.getShippingMethodName()));
        }
        if (purchaseOrder.getRelatedProjectID() != null) {
            purchaseOrderTO.setRelated_project(new IdNameTO(purchaseOrder.getRelatedProjectID(), purchaseOrder.getRelatedProjectName()));
        }

        if (purchaseOrder.getCurrencyID() != null) {
            purchaseOrderTO.setCurrency(new CurrencyTO(purchaseOrder.getCurrencyID(), purchaseOrder.getCurrencyName()));
        }

        try {
            if (purchaseOrder.getInvoiceDate() != null) {
                purchaseOrderTO.setInvoice_date(longDateTimezoneFormat.format(purchaseOrder.getInvoiceDate().getDate()));
            }
            if (purchaseOrder.getDueDate() != null) {
                purchaseOrderTO.setDue_date(longDateTimezoneFormat.format(purchaseOrder.getDueDate().getDate()));
            }
        } catch (Exception e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (purchaseOrder.getClientID() != null) {

            SupplierItemTO supplierItemTO = new SupplierItemTO();
            supplierItemTO.setSupplier_id(purchaseOrder.getClientID());
            supplierItemTO.setSupplier_name(purchaseOrder.getClientName());

            if (purchaseOrder.getBillAddressID() != null) {
                supplierItemTO.setBill_to_address(new AddressTO(purchaseOrder.getBillAddressID(), "BILLING_ADDRESS"));
            }
            if (purchaseOrder.getMailAddressID() != null) {
                supplierItemTO.setShip_to_address(new AddressTO(purchaseOrder.getMailAddressID(), "SHIPPING_ADDRESS"));
            }
            purchaseOrderTO.setSupplier(supplierItemTO);
        }

        ArrayList<InvoiceItemTO> invoiceItems = new ArrayList<>();

        if (purchaseOrderTO.getTotal_in_base_currency() == null) {
            for (NewInvoiceItem invoiceItem : purchaseOrder.getItems()) {
                InvoiceItemTO item = new InvoiceItemTO();

                ItemTO itemTO = new ItemTO();
                itemTO.setItem_id(invoiceItem.getItemID());
                itemTO.setItem_name(invoiceItem.getItemName());
                itemTO.setItem_number(invoiceItem.getItemNumber());
                item.setItem(itemTO);

                InvoiceSalesAccountTO salesAccountTO = new InvoiceSalesAccountTO();
                salesAccountTO.setSales_account_id(invoiceItem.getAccountID());

                item.setItem_sales_account(salesAccountTO);
                item.setItem_quantity(invoiceItem.getQuantity());
                item.setItem_price(invoiceItem.getUnitPrice());
                item.setItem_net_amount(invoiceItem.getNet());
                if (invoiceItem.getWarehouse() != null) {
                    item.setItem_warehouse(new WarehouseTO(invoiceItem.getWarehouse().getId(), invoiceItem.getWarehouse().getName()));
                }
                invoiceItems.add(item);
            }
        }
        purchaseOrderTO.setItems(invoiceItems);
        purchaseOrderTO.setTotal_in_base_currency(purchaseOrder.getTotal());
        purchaseOrderTO.setSubtotal(purchaseOrder.getSubtotal());

        return purchaseOrderTO;
    }

}
