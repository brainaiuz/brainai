package com.edatasite.workforce.rest.v2.release10.accounting;

import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsBaseSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.AddressManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.SaveResult;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.edatasite.workforce.gwt.invoice.server.app.QuoteServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.PaymentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.AddressTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.CurrencyTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.InvoiceItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.InvoiceListItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.InvoiceSalesAccountTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.InvoiceStatusTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.ItemTO;
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
@Tag(name = "Purchase Invoices", description = "Purchase Invoice API")
@RestController()
@RequestMapping(value = "/2", headers = {ApiConstants.X_AUTH, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiPurchaseInvoiceControllerV2 extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiPurchaseInvoiceControllerV2.class);

    @Autowired
    private InvoiceServiceLocal invoiceServiceLocal;
    @Autowired
    private AddressManager addressManager;
    @Autowired
    private CurrencyServiceLocal currencyServiceLocal;
    @Autowired
    private AccountingManager accountingManager;
    @Autowired
    private QuoteServiceLocal quoteServiceLocal;
    @Autowired
    private InvoiceManager invoiceManager;


    @Operation(summary = "Get Purchase Invoice List", description = "Retrieves Purchase Invoice List by search_text")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have Purchase Invoice list "),
            @ApiResponse(responseCode = "400", description = "Start point and limit is required"),
            @ApiResponse(responseCode = "422", description = "Start point and limit can not be zero at the same time"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/purchase_invoice/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
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

        ListResult<NewInvoice> purchaseInvoiceList;
        try {
            purchaseInvoiceList = invoiceServiceLocal.getPurchaseInvoiceDataFromSolr(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ArrayList<InvoiceListItemTO> resultList = new ArrayList<>();
        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        for (NewInvoice item : purchaseInvoiceList.getList()) {
            InvoiceListItemTO purshaseInvoice = new InvoiceListItemTO();
            purshaseInvoice.setId(item.getID());
            purshaseInvoice.setInvoice_number(item.getInvoiceNumber());
            purshaseInvoice.setInvoice_date(longDateTimezoneFormat.format(item.getInvoiceDate().getNonConvertedDate()));
            purshaseInvoice.setInvoice_status(new InvoiceStatusTO(item.getStatus(), item.getStatusCode()));
            purshaseInvoice.setInvoice_total(item.getTotal());

            CurrencyTO currency = new CurrencyTO();
            currency.setCurrency_id(item.getCurrencyID());
            currency.setCurrency_name(item.getCurrencyName());

            purshaseInvoice.setInvoice_currency(currency);

            resultList.add(purshaseInvoice);
        }
        ResponseResultListData<InvoiceListItemTO> resultListData = new ResponseResultListData<>();
        resultListData.setList(resultList);
        resultListData.setTotal(purchaseInvoiceList.getTotal());
        return successResponse(resultListData);

    }

    @Operation(summary = "Add New Purchase Invoice", description = "Adds new Purchase Invoice")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with error code "),
            @ApiResponse(responseCode = "404", description = "Invoice data is not found"),
            @ApiResponse(responseCode = "400", description = "Status is required or Invoice date is required or due date is required"),
            @ApiResponse(responseCode = "422", description = "Invalid Purchase Invoice status or Invalid invoice date format"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/purchase_invoice/create", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object createPurchaseInvoice(@RequestBody PurchaseOrderTO purchaseOrderTO) throws RestException {

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

        NewInvoice purchaseInvoice = convertPurchaseInvoice(purchaseOrderTO);

        //Create Purchase Invoice
        SaveResult saveResult = invoiceServiceLocal.savePurchaseInvoice(purchaseInvoice);

        if (saveResult.isInvoiceExist()) {
            throw new RestException("Invoice number is already used", "Invoice number " + purchaseOrderTO.getInvoice_number() + " is already used", CONFLICT, HttpStatus.CONFLICT);
        }
        return successResponse(new ResponseData());
    }

    @Operation(summary = "Add New Credit Note (Refund)", description = "Add New Credit Note (Refund)")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with error code "),
            @ApiResponse(responseCode = "404", description = "Invoice data is not found"),
            @ApiResponse(responseCode = "400", description = "Status is required or Invoice date is required or due date is required"),
            @ApiResponse(responseCode = "422", description = "Invalid Purchase Invoice status or Invalid invoice date format"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/purchase_invoice/credit_note", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object createCreditNote(@RequestBody PurchaseOrderTO purchaseOrderTO) throws RestException {

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

        NewInvoice purchaseInvoice = convertPurchaseInvoice(purchaseOrderTO);

        //Create Purchase Invoice
        SaveResult saveResult = invoiceServiceLocal.saveCreditNote(purchaseInvoice);

        if (saveResult.isInvoiceExist()) {
            throw new RestException("Invoice number is already used", "Invoice number " + purchaseOrderTO.getInvoice_number() + " is already used", CONFLICT, HttpStatus.CONFLICT);
        }
        return successResponse(new ResponseData());
    }

    @Operation(summary = "Update Purchase Invoice", description = "Update Purchase Invoice")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with error code "),
            @ApiResponse(responseCode = "404", description = "Invoice data is not found"),
            @ApiResponse(responseCode = "400", description = "Status is required or Invoice date is required or due date is required"),
            @ApiResponse(responseCode = "422", description = "Invalid Purchase Invoice status or Invalid invoice date format"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/purchase_invoice/update", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object updatePurchaseInvoice(@RequestBody PurchaseOrderTO purchaseOrderTO) throws RestException {

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

        NewInvoice purchaseInvoice = convertPurchaseInvoice(purchaseOrderTO);
        purchaseInvoice.setID(purchaseOrderTO.getId());

        //Create Purchase Invoice
        SaveResult saveResult = invoiceServiceLocal.savePurchaseInvoice(purchaseInvoice);

        if (saveResult.isInvoiceExist()) {
            throw new RestException("Invoice number is already used", "Invoice number " + purchaseOrderTO.getInvoice_number() + " is already used", CONFLICT, HttpStatus.CONFLICT);
        }
        return successResponse(new ResponseData());
    }

    @Operation(summary = "Get Purchase Invoice", description = "Get Purchase Invoice")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have Purchase Invoice details "),
            @ApiResponse(responseCode = "404", description = "Invoice data is not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/purchase_invoice/{id}", method = RequestMethod.GET)
    public Object getPurchaseInvoice(@PathVariable(value = "id") Integer id) throws RestException {

        //Start Validation
        if (id == null || id <= 0) {
            throw new RestException("id is required", "id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        NewInvoice purchaseInvoice = invoiceServiceLocal.getInvoiceSummaryData(id);

        if (purchaseInvoice != null) {

            PurchaseOrderTO result = convertPurchaseInvoice(purchaseInvoice);
            return successResponse(result);

        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, GENERAL_ERROR_MESSAGE, SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Operation(summary = "Delete Purchase Invoice", description = "Delete Purchase Invoice")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true if its deleted "),
            @ApiResponse(responseCode = "404", description = "Purchase Invoice not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/purchase_invoice/delete/{id}", method = RequestMethod.DELETE, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object deletePurchaseInvoice(@PathVariable(value = "id") Integer id) throws RestException {

        //Start Validation
        if (id == null || id <= 0) {
            throw new RestException("id is required", "id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        quoteServiceLocal.deleteQuote(id, Constants.PURCHASE_INVOICE);

        return successResponse(new ResponseData());

    }

    @Operation(summary = "Receive Purchase Invoice Payment", description = "Receive Purchase Invoice Payment")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false "),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/purchase_invoice/payment/{invoice_id}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object receivePayment(@PathVariable("invoice_id") Integer saleInvoiceId, @RequestBody PaymentTO paymentTO) throws RestException {


        if (saleInvoiceId == null || saleInvoiceId == 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "invoice_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        EdsInvoice invoice = invoiceManager.get(saleInvoiceId);

        if (!(invoice instanceof EdsBaseSaleInvoice)) {
            throw new RestException("Invoice not found", "Invoice not found", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        /*ReceivePaymentData receivePaymentData = new ReceivePaymentData();
        SelectItem crmAccount = null;
        if (((EdsBaseSaleInvoice) invoice).getClient() != null ) {
            EdsCrmAccount edsCrmAccount = crmAccountManager.get(((EdsBaseSaleInvoice) invoice).getClient().getObjectID());
            if (edsCrmAccount != null) {
                crmAccount = edsCrmAccount.getAsSelectItem();
            }
        }
        receivePaymentData.setCrmAccount(crmAccount);
        receivePaymentData.setCurrency(invoice.getCurrency() != null ? currencyManager.get(invoice.getCurrency().getObjectID()).createCurrencyItem() : null);
        receivePaymentData.setExRate(invoice.getExchangeRate());
        receivePaymentData.setReference(((EdsBaseSaleInvoice) invoice).getQuoteNumber());
        receivePaymentData.setDate(new DateNonConvertable(invoice.getInvoiceDate()));
        receivePaymentData.setTotalAmount(invoice.getTotal());
        if(invoice.getRelatedProject()!=null) {
            receivePaymentData.setProject(new SelectItem(invoice.getRelatedProject().getObjectID()));
        }
//        receivePaymentData.setPdfTemplateID(invoice.getPdfTemplateID());
        receivePaymentData.setPaymentTarget("INVOICE");
        receivePaymentData.setType(Constants.RECEIVABLE);
*/

        PaymentData[] paymentData = new PaymentData[1];
        paymentData[0] = new PaymentData();

        EdsAccount paymentAccount = accountingManager.get(paymentTO.getPayment_account_id());
        if (paymentAccount != null) {
            paymentData[0].setPaymentAccount(paymentAccount.getAsSelectItem());
        }

        paymentData[0].setReferenceNumber(paymentTO.getReference());
        paymentData[0].setInvoiceID(saleInvoiceId);
        paymentData[0].setPaymentAmount(paymentTO.getPaid_amount());
        paymentData[0].setExchangeRate(invoice.getExchangeRate());
        paymentData[0].setOpeningBalance(false);
        paymentData[0].setManualJournal(false);
        paymentData[0].setDate(new DateNonConvertable(ServerUtils.parseDate(paymentTO.getPaid_date(), "dd-MM-yyyy'T'hh:mm:ssZ")));
        paymentData[0].setPaymentTypeID(paymentTO.getPayment_method_id());
        paymentData[0].setType(Constants.PAYABLE);

//        receivePaymentData.setPayments(paymentData);

        Integer paymentId = invoiceServiceLocal.savePayment(paymentData[0]);
        if (paymentId != null && paymentId > 0) {
            return successResponse(new ResponseData());
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, GENERAL_ERROR_MESSAGE, INVALID, HttpStatus.CONFLICT);
        }
    }

    private NewInvoice convertPurchaseInvoice(PurchaseOrderTO purchaseOrderTO) throws RestException {

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        NewInvoice purchaseInvoice = new NewInvoice();
        purchaseInvoice.setType(Constants.PAYABLE);
        purchaseInvoice.setStatusCode(InvoiceStatusEnum.getStatus(purchaseOrderTO.getStatus().getStatus_code()));
        purchaseInvoice.setForceSave(true);
        purchaseInvoice.setFromApi(true);
        if (StringUtils.isNotBlank(purchaseOrderTO.getInvoice_number())) {
            purchaseInvoice.setInvoiceNumber(purchaseOrderTO.getInvoice_number());
        } else {
            purchaseInvoice.setNumberData(invoiceServiceLocal.getPurchaseInvoiceNumber(false));
            purchaseInvoice.setInvoiceNumber(purchaseInvoice.getNumberData().getInvoiceNumber());
        }

        purchaseInvoice.setReference(purchaseOrderTO.getReference());
        purchaseInvoice.setIntroduction(purchaseOrderTO.getIntroduction());
        purchaseInvoice.setPaymentTerms(purchaseOrderTO.getPayment_terms());
        purchaseInvoice.setShippingTerms(purchaseOrderTO.getShipping_terms());
        purchaseInvoice.setExchageRate(purchaseOrderTO.getExchange_rate());
        purchaseInvoice.setComissionAmount(purchaseOrderTO.getComission());
        purchaseInvoice.setTotalInInvoiceCurrency(purchaseOrderTO.getTotal_in_purchase_currency());
        purchaseInvoice.setTotalDiscount(purchaseOrderTO.getTotal_discount());
        purchaseInvoice.setShippingPrice(purchaseOrderTO.getShipping_price());
        purchaseInvoice.setTotalTaxes(purchaseOrderTO.getTotal_taxes_in_base_currency());
        purchaseInvoice.setTaxCalculationType(purchaseOrderTO.getTax_calculation_type());
        purchaseInvoice.setTotalInInvoiceCurrency(purchaseOrderTO.getTotal_in_purchase_currency());

        if (purchaseOrderTO.getPrice_level() != null) {
            purchaseInvoice.setPriceLevel(new SelectItem(purchaseOrderTO.getPrice_level().getId(), purchaseOrderTO.getPrice_level().getName()));
        }
        if (purchaseOrderTO.getShipping_method() != null) {
            purchaseInvoice.setShippingMethodID(purchaseOrderTO.getShipping_method().getId());
        }
        if (purchaseOrderTO.getRelated_project() != null) {
            purchaseInvoice.setRelatedProjectID(purchaseOrderTO.getRelated_project().getId());
            purchaseInvoice.setRelatedProjectName(purchaseOrderTO.getRelated_project().getName());
        }


        if (purchaseOrderTO.getCurrency() == null || purchaseOrderTO.getCurrency().getCurrency_id() == null) {
            purchaseInvoice.setCurrencyID(currencyServiceLocal.getBaseCurrency().getId());
        } else {
            purchaseInvoice.setCurrencyID(purchaseOrderTO.getCurrency().getCurrency_id());
        }

        try {
            purchaseInvoice.setInvoiceDate(new DateNonConvertable(ServerUtils.getStartDate(longDateTimezoneFormat.parse(purchaseOrderTO.getInvoice_date()))));
            purchaseInvoice.setDueDate(new DateNonConvertable(ServerUtils.getEndDate(longDateTimezoneFormat.parse(purchaseOrderTO.getDue_date()))));
        } catch (ParseException e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (purchaseOrderTO.getSupplier() != null) {

            purchaseInvoice.setClientID(purchaseOrderTO.getSupplier().getSupplier_id());

            List<EdsAddress> addressList = addressManager.getAddressesByEntityIdAndType(purchaseOrderTO.getSupplier().getSupplier_id(), EdsAddress.BILLING_ADDRESS, EdsAddress.ENTITY_TYPE_CRM_ACCOUNT);

            if (purchaseOrderTO.getSupplier().getBill_to_address() == null || purchaseOrderTO.getSupplier().getBill_to_address().getAddress_id() == null || purchaseOrderTO.getSupplier().getBill_to_address().getAddress_id() <= 0) {
                Optional<EdsAddress> optional = addressList.stream().filter(EdsAddress::isPrimary).findFirst();
                if (optional.isPresent()) {
                    purchaseInvoice.setBillAddressID(optional.get().getObjectID());
                } else {
                    throw new RestException("Invoice customer primary bill to address is not found",
                            "Invoice customer primary bill to address is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
                }
            } else {
                Optional<EdsAddress> optional = addressList.stream().filter(billToAddressId -> billToAddressId.getObjectID().equals(purchaseOrderTO.getSupplier().getBill_to_address().getAddress_id())).findFirst();
                if (optional.isPresent()) {
                    purchaseInvoice.setBillAddressID(purchaseOrderTO.getSupplier().getBill_to_address().getAddress_id());
                } else {
                    throw new RestException("Invoice customer primary bill to address with " + purchaseOrderTO.getSupplier().getBill_to_address().getAddress_id() + " is not found"
                            , "Invoice customer primary bill to address with " + purchaseOrderTO.getSupplier().getBill_to_address().getAddress_id() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
                }

            }
            if (purchaseOrderTO.getSupplier().getShip_to_address() != null) {
                purchaseInvoice.setMailAddressID(purchaseOrderTO.getSupplier().getShip_to_address().getAddress_id());
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
        purchaseInvoice.setItems(invoiceItems.toArray(new NewInvoiceItem[]{}));

        if (purchaseOrderTO.getTotal_in_base_currency() == null) {
            purchaseInvoice.setTotal(invoiceTotal);
            purchaseInvoice.setSubtotal(invoiceTotal);
        } else {
            purchaseInvoice.setTotal(purchaseOrderTO.getTotal_in_base_currency());
            purchaseInvoice.setSubtotal(purchaseOrderTO.getSubtotal());
        }
        return purchaseInvoice;
    }

    private PurchaseOrderTO convertPurchaseInvoice(NewInvoice purchaseInvoice) throws RestException {

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        PurchaseOrderTO purchaseOrderTO = new PurchaseOrderTO();
        purchaseOrderTO.setId(purchaseInvoice.getID());
        purchaseOrderTO.setStatus(new InvoiceStatusTO(purchaseInvoice.getStatusCode(), purchaseInvoice.getStatusCode()));
        purchaseOrderTO.setInvoice_number(purchaseInvoice.getInvoiceNumber());
        purchaseOrderTO.setReference(purchaseInvoice.getReference());
        purchaseOrderTO.setIntroduction(purchaseInvoice.getIntroduction());
        purchaseOrderTO.setPayment_terms(purchaseInvoice.getPaymentTerms());
        purchaseOrderTO.setShipping_terms(purchaseInvoice.getShippingTerms());
        purchaseOrderTO.setExchange_rate(purchaseInvoice.getExchageRate());
        purchaseOrderTO.setComission(purchaseInvoice.getComissionAmount());
        purchaseOrderTO.setTotal_in_purchase_currency(purchaseInvoice.getTotalInInvoiceCurrency());
        purchaseOrderTO.setTotal_discount(purchaseInvoice.getTotalDiscount());
        purchaseOrderTO.setShipping_price(purchaseInvoice.getShippingPrice());
        purchaseOrderTO.setTotal_taxes_in_base_currency(purchaseInvoice.getTotalTaxes());
        purchaseOrderTO.setTax_calculation_type(purchaseInvoice.getTaxCalculationType());
        purchaseOrderTO.setTotal_in_purchase_currency(purchaseInvoice.getTotalInInvoiceCurrency());

        if (purchaseInvoice.getPriceLevel() != null) {
            purchaseOrderTO.setPrice_level(new IdNameTO(purchaseInvoice.getPriceLevel().getId(), purchaseInvoice.getPriceLevel().getName()));
        }
        if (purchaseInvoice.getShippingMethodID() != null) {
            purchaseOrderTO.setShipping_method(new IdNameTO(purchaseInvoice.getShippingMethodID(), purchaseInvoice.getShippingMethodName()));
        }
        if (purchaseInvoice.getRelatedProjectID() != null) {
            purchaseOrderTO.setRelated_project(new IdNameTO(purchaseInvoice.getRelatedProjectID(), purchaseInvoice.getRelatedProjectName()));
        }

        if (purchaseInvoice.getCurrencyID() != null) {
            purchaseOrderTO.setCurrency(new CurrencyTO(purchaseInvoice.getCurrencyID(), purchaseInvoice.getCurrencyName()));
        }

        try {
            if (purchaseInvoice.getInvoiceDate() != null) {
                purchaseOrderTO.setInvoice_date(longDateTimezoneFormat.format(purchaseInvoice.getInvoiceDate().getDate()));
            }
            if (purchaseInvoice.getDueDate() != null) {
                purchaseOrderTO.setDue_date(longDateTimezoneFormat.format(purchaseInvoice.getDueDate().getDate()));
            }
        } catch (Exception e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (purchaseInvoice.getClientID() != null) {

            SupplierItemTO supplierItemTO = new SupplierItemTO();
            supplierItemTO.setSupplier_id(purchaseInvoice.getClientID());
            supplierItemTO.setSupplier_name(purchaseInvoice.getClientName());

            if (purchaseInvoice.getBillAddressID() != null) {
                supplierItemTO.setBill_to_address(new AddressTO(purchaseInvoice.getBillAddressID(), "BILLING_ADDRESS"));
            }
            if (purchaseInvoice.getMailAddressID() != null) {
                supplierItemTO.setShip_to_address(new AddressTO(purchaseInvoice.getMailAddressID(), "SHIPPING_ADDRESS"));
            }
            purchaseOrderTO.setSupplier(supplierItemTO);
        }

        ArrayList<InvoiceItemTO> invoiceItems = new ArrayList<>();

        if (purchaseOrderTO.getTotal_in_base_currency() == null) {
            for (NewInvoiceItem invoiceItem : purchaseInvoice.getItems()) {
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
        purchaseOrderTO.setTotal_in_base_currency(purchaseInvoice.getTotal());
        purchaseOrderTO.setSubtotal(purchaseInvoice.getSubtotal());

        return purchaseOrderTO;
    }

}
