package com.edatasite.workforce.rest.v2.release10.accounting;

import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
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
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.InvoiceCustomerTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.InvoiceItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.InvoiceSalesAccountTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.InvoiceStatusTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.ItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.WarehouseTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.order.SalesOrderListTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.order.SalesOrderTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.RequestListSearchData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseResultListData;
import com.edatasite.workforce.rest.v2.release10.enums.InvoiceStatusEnum;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.to.IdDTO;
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
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Created by Dilsh0d on 11/2/2017.
 */
@Tag(name = "Sales Order", description = "Sales Order API")
@RestController
@RequestMapping(headers = {ApiConstants.X_AUTH, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiSalesOrderControllerV2 extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiSalesOrderControllerV2.class);

    @Autowired
    private QuoteServiceLocal quoteServiceLocal;
    @Autowired
    private AddressManager addressManager;
    @Autowired
    private CurrencyServiceLocal currencyServiceLocal;
    @Autowired
    private InvoiceCircularResolver invoiceCircularResolver;


    @Operation(summary = "Get Sales Order", description = "Retrieves Sales Invoice ")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have Sales Orders details "),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/sales_order/{id}", method = RequestMethod.GET)
    public Object get(@PathVariable(value = "id") Integer id) throws RestException {

        if (id == null || id == 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        NewInvoice quoteSummary = quoteServiceLocal.getQuoteSummaryData(id);

        SalesOrderTO saleOrderTO = new SalesOrderTO();
        saleOrderTO.setId(quoteSummary.getID());
        saleOrderTO.setNumber(quoteSummary.getInvoiceNumber());
        saleOrderTO.setDate(longDateTimezoneFormat.format(quoteSummary.getInvoiceDate().getNonConvertedDate()));
        saleOrderTO.setDue_date(longDateTimezoneFormat.format(quoteSummary.getDueDate().getNonConvertedDate()));
        saleOrderTO.setStatus(new InvoiceStatusTO(quoteSummary.getStatus(), quoteSummary.getStatusCode()));
        saleOrderTO.setTotal(quoteSummary.getTotalInInvoiceCurrency());
        saleOrderTO.setCurrency(new CurrencyTO(quoteSummary.getCurrencyID(), quoteSummary.getCurrencyName()));
        saleOrderTO.setIntroduction(quoteSummary.getPaymentInstruction());
        saleOrderTO.setReference(quoteSummary.getReference());
        saleOrderTO.setTax_calculation_type(quoteSummary.getTaxCalculationType());
        saleOrderTO.setExchange_rate(quoteSummary.getExchageRate());
        if (quoteSummary.getRelatedProject() != null) {
            saleOrderTO.setRelatedProject(quoteSummary.getRelatedProject().getName());
        }
        InvoiceCustomerTO customerTO = new InvoiceCustomerTO();
        customerTO.setCustomer_id(quoteSummary.getClientID());
        customerTO.setCustomer_name(quoteSummary.getClientName());
        if (quoteSummary.getBillAddressID() != null) {
            EdsAddress address = addressManager.get(quoteSummary.getBillAddressID());
            AddressTO billToAddress = new AddressTO();
            billToAddress.setAddress_id(address.getObjectID());
            billToAddress.setAddress_name(address.getName());
            customerTO.setBill_to_address(billToAddress);
        }
        if (quoteSummary.getMailAddressID() != null) {
            EdsAddress address = addressManager.get(quoteSummary.getMailAddressID());
            AddressTO shipToAddress = new AddressTO();
            shipToAddress.setAddress_id(address.getObjectID());
            shipToAddress.setAddress_name(address.getName());
            customerTO.setShip_to_address(shipToAddress);
        }
        saleOrderTO.setCustomer(customerTO);


        ArrayList<InvoiceItemTO> items = new ArrayList<>();
        for (NewInvoiceItem item : quoteSummary.getItems()) {
            InvoiceItemTO salesQuoteitemTO = new InvoiceItemTO();
            ItemTO itemTO = new ItemTO(item.getItemID(), item.getItemName(), item.getItemNumber());
            itemTO.setDescription(item.getDescription());
            salesQuoteitemTO.setItem(itemTO);
            salesQuoteitemTO.setItem_quantity(item.getQuantity());
            salesQuoteitemTO.setItem_price(item.getUnitPrice());
            salesQuoteitemTO.setItem_net_amount(item.getNet());
            if (item.getSalesAccount() != null) {
                salesQuoteitemTO.setItem_sales_account(new InvoiceSalesAccountTO(item.getSalesAccount().getId(), item.getSalesAccount().getName()));
            }
            salesQuoteitemTO.setItem_description(item.getDescription());
            if (item.getWarehouse() != null) {
                salesQuoteitemTO.setItem_warehouse(new WarehouseTO(item.getWarehouse().getId(), item.getWarehouse().getName()));
            }
            items.add(salesQuoteitemTO);
        }
        saleOrderTO.setItems(items);

        return successResponse(saleOrderTO);

    }


    @Operation(summary = "Get Sales Orders List", description = "Retrieves Sales Order List")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have Sales Orders details "),
            @ApiResponse(responseCode = "400", description = "Start point and limit is required"),
            @ApiResponse(responseCode = "422", description = "Start point and limit can not be zero at the same time"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/sales_orders", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
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

        ListResult<NewInvoice> salesOrderList;
        try {
            salesOrderList = quoteServiceLocal.getSaleOrderData(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ArrayList<SalesOrderListTO> resultList = new ArrayList<>();
        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        for (NewInvoice item : salesOrderList.getList()) {
            SalesOrderListTO salesOrder = new SalesOrderListTO();
            salesOrder.setId(item.getID());
            salesOrder.setNumber(item.getInvoiceNumber());
            salesOrder.setPo_number(item.getPoNumber());
            salesOrder.setDate(longDateTimezoneFormat.format(item.getInvoiceDate().getNonConvertedDate()));
            salesOrder.setStatus(new InvoiceStatusTO(item.getStatus(), item.getStatusCode()));
            salesOrder.setTotal(item.getTotalInInvoiceCurrency());

            CurrencyTO currency = new CurrencyTO();
            currency.setCurrency_id(item.getCurrencyID());
            currency.setCurrency_name(item.getCurrencyName());

            salesOrder.setCurrency(currency);

            salesOrder.setRelatedProject(item.getRelatedProjectName());

            resultList.add(salesOrder);
        }
        ResponseResultListData<SalesOrderListTO> resultListData = new ResponseResultListData<>();
        resultListData.setList(resultList);
        resultListData.setTotal(salesOrderList.getTotal());
        return successResponse(resultListData);
    }

    @Operation(summary = "Add New Sales Order", description = "Add New Sales Order")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with error code "),
            @ApiResponse(responseCode = "404", description = "Order data is not found"),
            @ApiResponse(responseCode = "400", description = "Order Status is required or Order date is required or Order due date is required"),
            @ApiResponse(responseCode = "422", description = "Order status or Invalid Order date format"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/sales_order", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object add(@RequestBody SalesOrderTO salesOrderTO) throws RestException {

        if (salesOrderTO == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Order data is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (salesOrderTO.getStatus() == null || StringUtils.isBlank(salesOrderTO.getStatus().getStatus_code())) {
            throw new RestException("Order status is required", "Order status is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (InvoiceStatusEnum.getStatus(salesOrderTO.getStatus().getStatus_code()) == null) {
            throw new RestException("Invalid Order status", "Invalid Order status " + salesOrderTO.getStatus().getStatus_code(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (StringUtils.isBlank(salesOrderTO.getDate())) {
            throw new RestException("Order date not provided", "Order date is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(salesOrderTO.getDue_date())) {
            throw new RestException("Order due date is not provided", "Order due date is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        try {
            longDateTimezoneFormat.parse(salesOrderTO.getDate());
        } catch (ParseException e) {
            log.error("", e);
            throw new RestException("Invalid Order date format", "Invalid Order date format. Acceptable format for invoice is " + longDateTimezoneFormat.toPattern(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        try {
            longDateTimezoneFormat.parse(salesOrderTO.getDue_date());
        } catch (ParseException e) {
            log.error("", e);
            throw new RestException("Invalid Order due date format", "Invalid Order date format. Acceptable format for invoice is " + longDateTimezoneFormat.toPattern(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (salesOrderTO.getCustomer() == null || salesOrderTO.getCustomer().getCustomer_id() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Order customer is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (salesOrderTO.getItems() == null || salesOrderTO.getItems().isEmpty()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Order item is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        for (InvoiceItemTO invoiceItem : salesOrderTO.getItems()) {
            if (invoiceItem.getItem() == null || invoiceItem.getItem().getItem_id() == null) {
                throw new RestException("Order item not provided", "Order item is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            if (invoiceItem.getItem_quantity() == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Order item quantity is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            if (BigDecimal.ZERO.compareTo(invoiceItem.getItem_quantity()) == 0) {
                throw new RestException("Order invoice item quantity", "Order item quantity can not be zero", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (invoiceItem.getItem_price() == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Order item price is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            if (BigDecimal.ZERO.compareTo(invoiceItem.getItem_price()) == 0) {
                throw new RestException("Invalid Order item price", "Invoice Order item price can not be zero", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            if (invoiceItem.getItem_sales_account() == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Invoice item sales account is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            if (invoiceItem.getItem_sales_account().getSales_account_id() == null || invoiceItem.getItem_sales_account().getSales_account_id() <= 0) {
                throw new RestException("Order item sales account is required", "Order item sales account is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            if (invoiceItem.getItem_warehouse() == null || invoiceItem.getItem_warehouse().getWarehouse_id() == null) {
                throw new RestException("Order item warehouse is required", "Order item warehouse is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
        }

        NewInvoice salesOrder = new NewInvoice();
        salesOrder.setID(salesOrderTO.getId());
        salesOrder.setInvoiceNumber(salesOrderTO.getNumber());
        salesOrder.setBookkeep(true);
        salesOrder.setPoNumber(salesOrderTO.getPo_number());
        try {
            salesOrder.setInvoiceDate(new DateNonConvertable(ServerUtils.getStartDate(longDateTimezoneFormat.parse(salesOrderTO.getDate()))));
            salesOrder.setDueDate(new DateNonConvertable(ServerUtils.getEndDate(longDateTimezoneFormat.parse(salesOrderTO.getDue_date()))));
        } catch (ParseException e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        salesOrder.setClientID(salesOrderTO.getCustomer().getCustomer_id());
        salesOrder.setCurrencyID(salesOrderTO.getCurrency().getCurrency_id());
        salesOrder.setExchageRate(salesOrderTO.getExchange_rate());
        salesOrder.setReference(salesOrderTO.getReference());
        salesOrder.setIntroduction(salesOrderTO.getIntroduction());
        salesOrder.setType(Constants.RECEIVABLE);
        salesOrder.setStatusCode(Constants.SALE_ORDER);
        salesOrder.setSalesOrder(true);
        salesOrder.setTaxCalculationType(salesOrderTO.getTax_calculation_type());

        List<EdsAddress> addressList = addressManager.getAddressesByEntityIdAndType(salesOrderTO.getCustomer().getCustomer_id(), EdsAddress.BILLING_ADDRESS, EdsAddress.ENTITY_TYPE_CRM_ACCOUNT);
        if (salesOrderTO.getCustomer().getBill_to_address() == null || salesOrderTO.getCustomer().getBill_to_address().getAddress_id() == null || salesOrderTO.getCustomer().getBill_to_address().getAddress_id() <= 0) {
            Optional<EdsAddress> optional = addressList.stream().filter(EdsAddress::isPrimary).findFirst();
            if (optional.isPresent()) {
                salesOrder.setBillAddressID(optional.get().getObjectID());
            } else {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Order customer primary bill to address is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
        } else {
            Optional<EdsAddress> optional = addressList.stream().filter(billToAddressId -> billToAddressId.getObjectID().equals(salesOrderTO.getCustomer().getBill_to_address().getAddress_id())).findFirst();
            if (optional.isPresent()) {
                salesOrder.setBillAddressID(salesOrderTO.getCustomer().getBill_to_address().getAddress_id());
            } else {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Order customer primary bill to address with " + salesOrderTO.getCustomer().getBill_to_address().getAddress_id() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }

        }
        if (salesOrderTO.getCustomer().getShip_to_address() != null) {
            salesOrder.setMailAddressID(salesOrderTO.getCustomer().getShip_to_address().getAddress_id());
        }
        CurrencyItem baseCurrency = currencyServiceLocal.getBaseCurrency();
        if (salesOrderTO.getCurrency() == null || salesOrderTO.getCurrency().getCurrency_id() == null || baseCurrency.getId().equals(salesOrderTO.getCurrency().getCurrency_id())) {
            salesOrder.setCurrencyID(baseCurrency.getId());
            salesOrder.setBaseCurrency(baseCurrency);
            salesOrder.setExchageRate(BigDecimal.ONE);
        } else {
            salesOrder.setCurrencyID(salesOrderTO.getCurrency().getCurrency_id());
            if (salesOrderTO.getExchange_rate() == null) {
                salesOrder.setExchageRate(BigDecimal.valueOf(currencyServiceLocal.getCurrencyRateByDate(salesOrderTO.getCurrency().getCurrency_id(), salesOrder.getInvoiceDate()).getExchangeRate()));
            }
        }
        salesOrder.setCustomFieldItems(convertCustomFields(salesOrderTO.getCustom_fields(), Collections.emptyMap()));

        ArrayList<NewInvoiceItem> saleOrderItems = new ArrayList<>();

        BigDecimal salesOrderTotal = BigDecimal.ZERO;
        if (salesOrderTO.getTotal() == null) {
            for (InvoiceItemTO invoiceItem : salesOrderTO.getItems()) {
                NewInvoiceItem item = new NewInvoiceItem();
                item.setItemID(invoiceItem.getItem().getItem_id());
                item.setItemName(invoiceItem.getItem().getItem_name());
                item.setDescription(invoiceItem.getItem().getDescription());
                item.setAccountID(invoiceItem.getItem_sales_account().getSales_account_id());
                item.setQuantity(invoiceItem.getItem_quantity());
                item.setUnitPrice(invoiceItem.getItem_price());
                item.setNet(invoiceItem.getItem_net_amount() == null ? (invoiceItem.getItem_quantity().multiply(invoiceItem.getItem_price())) : invoiceItem.getItem_net_amount());
                if (invoiceItem.getItem_warehouse() != null) {
                    item.setWarehouse(new SelectItem(invoiceItem.getItem_warehouse().getWarehouse_id()));
                }
                if (invoiceItem.getTax_item() != null) {
                    item.setTaxItem(new TaxItem(invoiceItem.getTax_item().getTax_id(), invoiceItem.getTax_item().getTax_name(), invoiceItem.getTax_item().getTax_rate()));
                    item.setTaxAmount(invoiceItem.getTax_item().getTax_amount());
                }
                salesOrderTotal = salesOrderTotal.add(invoiceItem.getItem_quantity().multiply(invoiceItem.getItem_price()));
                saleOrderItems.add(item);
            }
        } else {
            for (InvoiceItemTO invoiceItem : salesOrderTO.getItems()) {
                NewInvoiceItem item = new NewInvoiceItem();
                item.setItemID(invoiceItem.getItem().getItem_id());
                item.setItemName(invoiceItem.getItem().getItem_name());
                item.setDescription(invoiceItem.getItem().getDescription());
                item.setAccountID(invoiceItem.getItem_sales_account().getSales_account_id());
                item.setQuantity(invoiceItem.getItem_quantity());
                item.setUnitPrice(invoiceItem.getItem_price());
                item.setNet(invoiceItem.getItem_net_amount());
                if (invoiceItem.getItem_warehouse() != null) {
                    item.setWarehouse(new SelectItem(invoiceItem.getItem_warehouse().getWarehouse_id()));
                }
                if (invoiceItem.getTax_item() != null) {
                    item.setTaxItem(new TaxItem(invoiceItem.getTax_item().getTax_id(), invoiceItem.getTax_item().getTax_name(), invoiceItem.getTax_item().getTax_rate()));
                    item.setTaxAmount(invoiceItem.getTax_item().getTax_amount());
                }
                saleOrderItems.add(item);
            }
        }
        salesOrder.setItems(saleOrderItems.toArray(new NewInvoiceItem[0]));

        if (salesOrderTO.getTotal() == null) {
            salesOrder.setTotalInInvoiceCurrency(salesOrderTotal);
            salesOrder.setSubtotal(salesOrderTotal);
            salesOrder.setTotal(salesOrderTotal);
        } else {
            salesOrder.setTotalInInvoiceCurrency(salesOrderTO.getTotal());
            salesOrder.setSubtotal(salesOrderTO.getTotal());
            salesOrder.setTotal(salesOrderTO.getTotal());
        }
        if (!baseCurrency.getId().equals(salesOrder.getCurrencyID())) {
            salesOrder.setTotal(salesOrder.getTotalInInvoiceCurrency().divide(salesOrderTO.getExchange_rate(), ServerUtils.getCalculationScale(), RoundingMode.HALF_UP));
        }

        SaveResult saveResult;
        if (salesOrderTO.getId() == null && StringUtils.isBlank(salesOrderTO.getNumber())) {
            salesOrder.setNumberData(invoiceCircularResolver.getQuoteOrderNumberData(Constants.SALE_ORDER));
            salesOrder.setInvoiceNumber(salesOrder.getNumberData().getInvoiceNumber());
            saveResult = quoteServiceLocal.saveSaleQuote(salesOrder);
        } else {
            saveResult = quoteServiceLocal.updateSaleQuote(salesOrder);
        }

        if (saveResult.isInvoiceExist()) {
            throw new RestException("Order number is already used", "Order number " + salesOrder.getInvoiceNumber() + " is already used", CONFLICT, HttpStatus.CONFLICT);
        }
        return successResponse(new IdDTO(saveResult.getId()));
    }

    @Operation(summary = "Update Sales Order", description = "Update Sales Order")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with error code "),
            @ApiResponse(responseCode = "404", description = "Order data is not found"),
            @ApiResponse(responseCode = "400", description = "Order id is required or status is required or date is required or due date is required"),
            @ApiResponse(responseCode = "422", description = "Order status or Invalid Order date format"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/sales_order/{id}",
            method = RequestMethod.PUT,
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object update(@PathVariable(value = "id") Integer id,
                         @RequestBody SalesOrderTO salesOrderTO) throws RestException {

        if (id == null || id == 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        salesOrderTO.setId(id);
        try {
            return add(salesOrderTO);
        } catch (RestException e) {
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Delete Sales Order", description = "Delete Sales Order")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with error code "),
            @ApiResponse(responseCode = "400", description = "Order id is required"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/sales_order/{id}", method = RequestMethod.DELETE)
    public Object delete(@PathVariable(value = "id") Integer id) throws RestException {

        if (id == null || id == 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        try {
            quoteServiceLocal.deleteQuote(id, PURCHASE_ORDER);
            return successResponse(new ResponseData());
        } catch (Exception e) {
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
