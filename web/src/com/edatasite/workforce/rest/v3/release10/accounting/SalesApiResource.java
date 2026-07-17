package com.edatasite.workforce.rest.v3.release10.accounting;

import com.edatasite.workforce.core.domain.accounting.*;
import com.edatasite.workforce.core.domain.approving.EdsApprover;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.InvoiceTermsItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.MessageCommand;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.invoice.client.rpc.*;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.base.to.PaymentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.IdNameTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.*;
import com.edatasite.workforce.rest.v3.release10.accounting.request.UpdateStatusRequest;
import com.edatasite.workforce.rest.v3.release10.core.helper.ListingFilterHelperV3;
import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.edatasite.workforce.rest.v3.release10.core.to.ListParamsDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import com.google.gwt.thirdparty.guava.common.base.Supplier;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.ws.rs.QueryParam;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.*;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.*;

@Tag(name = "Sales Api Resource", description = "Here is a sales api resouce that contains Sales Invoice, Sales Quote and Sales Order as well")
@RestController
@RequestMapping(value = "/sales", headers = {ApiConstants.X_AUTH, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class SalesApiResource extends AbstractInvoiceQuoteApiResource {

    private static final Logger log = LoggerFactory.getLogger(SalesApiResource.class);

    @RequestMapping(value = "/quote/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<ListResultTO<OrderDto>> quoteList(@RequestBody ListParamsDTO params) {
        ListingFilterParameter fp = ListingFilterHelperV3.createListingFilter(params, ListPanelType.SaleQuoteListPanel);
        configureDateFiltersWithFacet(fp);

        return ResponseEntity.ok(invoiceAPIService.getSaleQuoteList(fp));
    }

    @RequestMapping(value = "/quote", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<?> createQuote(@RequestBody @Valid OrderDto dto) throws RestException {
        SaveResult result = saveQuote(dto, SALES_QUOTE, true);
        return new ResponseEntity<SaveResult>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/quote", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<?> updateQuote(@RequestBody @Valid OrderDto dto) throws RestException {
        SaveResult result = saveQuote(dto, SALES_QUOTE, false);
        return new ResponseEntity<SaveResult>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/order/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<ListResultTO<OrderDto>> orderList(@RequestBody ListParamsDTO params) {
        ListingFilterParameter fp = ListingFilterHelperV3.createListingFilter(params, ListPanelType.SaleOrderListPanel);
        configureDateFiltersWithFacet(fp);

        return ResponseEntity.ok(invoiceAPIService.getSaleOrderList(fp));
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<?> createOrder(@RequestBody @Valid OrderDto dto) throws RestException {
        SaveResult result = saveQuote(dto, SALES_ORDER, true);
        return new ResponseEntity<SaveResult>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/order", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<?> updateOrder(@RequestBody @Valid OrderDto dto) throws RestException {
        SaveResult result = saveQuote(dto, SALES_ORDER, false);
        return new ResponseEntity<SaveResult>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/invoice/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<ListResultTO<InvoiceDto>> invoiceList(@RequestBody ListParamsDTO params) {
        ListingFilterParameter fp = ListingFilterHelperV3.createListingFilter(params, ListPanelType.SaleInvoiceListPanel);
        configureDateFiltersWithFacet(fp);

        return ResponseEntity.ok(invoiceAPIService.getSaleInvoiceList(fp));
    }

    @RequestMapping(value = "/invoice/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<InvoiceDto> getInvoice(@PathVariable Integer id) throws RestException, JsonProcessingException {
        NewInvoice invoice = invoiceServiceLocal.getInvoiceSummaryData(id);
        if (invoice == null || invoice.getID() == null) {
            throw new RestException(IN_VALID_DATA, "Invoice Not Found!", SERVER_ERROR, HttpStatus.NOT_FOUND);
        }
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(toInvoiceDto(invoice));
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(mapper.readValue(json, InvoiceDto.class));
    }

    @RequestMapping(value = "/invoice", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<?> createInvoice(@RequestBody @Valid InvoiceDto dto) throws RestException {
        SaveResult result = saveInvoice(dto, true);
        return new ResponseEntity<SaveResult>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/invoice", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<?> updateInvoice(@RequestBody @Valid InvoiceDto dto) throws RestException {
        SaveResult result = saveInvoice(dto, false);
        return new ResponseEntity<SaveResult>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/invoice/{invoiceId}/payment", method = RequestMethod.PATCH, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<?> makeInvoicePayment(@PathVariable("invoiceId") Integer invoiceId, @Valid @RequestBody InvoicePaymentDto dto) throws RestException {
        NewInvoice invoice = invoiceServiceLocal.getInvoice(invoiceId);
        validatePayments(invoice, Collections.singletonList(dto));
        List<BatchPaymentResult> results = makePayments(invoice, Collections.singletonList(dto));
        return new ResponseEntity<>(results.get(0), HttpStatus.OK);
    }

    @RequestMapping(value = "/invoice", method = RequestMethod.PATCH, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<?> patchInvoice(@RequestBody @Valid InvoiceFieldsUpdaterDto dto) throws RestException {
        if (dto == null) {
            throw new RestException(IN_VALID_DATA, "Request body cannot be empty!", SERVER_ERROR, HttpStatus.BAD_REQUEST);
        }
        if (dto.getId() == null) {
            throw new RestException(IN_VALID_DATA, "Invoice Id is required!", SERVER_ERROR, HttpStatus.BAD_REQUEST);
        }
        invoiceServiceLocal.updateInvoiceFields(dto);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @RequestMapping(value = "/invoice/status/update", method = RequestMethod.PATCH, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<?> updateInvoiceStatus(@RequestBody UpdateStatusRequest request) throws RestException {
        changeInvoiceStatus(request);
        return ResponseEntity.ok("Successfully changed!");
    }

    @RequestMapping(value = "/order/status/update", method = RequestMethod.PATCH, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<?> updateOrderStatus(@RequestBody UpdateStatusRequest request) throws RestException {
        changeOrderStatus(request);
        return ResponseEntity.ok("Successfully changed!");
    }

    @RequestMapping(value = "/quote/status/update", method = RequestMethod.PATCH, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<?> updateQuoteStatus(@RequestBody UpdateStatusRequest request) throws RestException {
        changeOrderStatus(request);
        return ResponseEntity.ok("Successfully changed!");
    }

    @RequestMapping(value = "/order", method = RequestMethod.DELETE, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<?> deleteOrder(@QueryParam("id") Integer id, @QueryParam("number") String number) {
        return deleteQuote(id, number);
    }

    @RequestMapping(value = "/terms", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ListResult<InvoiceTermsItem> getTerms() {
        return invoiceServiceLocal.getInvoiceTermsList(new ListingFilterParameter());
    }

    @RequestMapping(value = "/quote", method = RequestMethod.DELETE, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<?> deleteQuote(@QueryParam("id") Integer id, @QueryParam("number") String number) {
        if (id == null && StringUtils.isBlank(number)) {
            return ResponseEntity.badRequest().body("Order Id or Number for deleting process must be provided!");
        }

        if (StringUtils.isNotBlank(number)) {
            //here assumed that quote number is a unique
            List<EdsSaleQuote> quotes = quoteManager.getQuoteByNumber(number);

            if (!quotes.isEmpty()) {
                id = quotes.get(0).getObjectID();
            }
        }
        id = id == null ? 0 : id;

        TestRPC result = quoteServiceLocal.deleteQuote(id, Constants.SALE_QUOTE);
        if (MessageCommand.hasConvertedItems.equals(result.getMessageCommand())) {
            return ResponseEntity.badRequest().body("Order were converted to Invoice, You cannot deleted a invoiced Order!");
        } else if (MessageCommand.hasShippingData.equals(result.getMessageCommand())) {
            return ResponseEntity.badRequest().body("The Order has GRNs, You cannot deleted a delivered order!");
        }
        return ResponseEntity.ok().body("Successfully deleted!");
    }

    @Operation(summary = "Delete Invoice Payment Item")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "Success"))
    @RequestMapping(path = "/invoice/{invoicePaymentItemId}", method = RequestMethod.DELETE)
    public Object deleteInvoicePayment(@PathVariable Integer invoicePaymentItemId) {

        log.info("Deleting Sales Invoice Payment item id - {}", invoicePaymentItemId);
        invoiceServiceLocal.deleteBatchPayment(invoicePaymentItemId);
        return ResultTO.success();
    }

    @RequestMapping(value = "/invoice", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteInvoice(@QueryParam("id") Integer id, @QueryParam("number") String number) {
        if (id == null && StringUtils.isBlank(number)) {
            return ResponseEntity.badRequest().body("Invoice Id or Number for deleting process must be provided!");
        }

        if (StringUtils.isNotBlank(number)) {
            //here assumed that invoice number is a unique
            List<EdsBaseSaleInvoice> invoices = invoiceManager.getSaleInvoiceByNumber(number, null);

            if (!invoices.isEmpty()) {
                id = invoices.get(0).getObjectID();
            }
        }
        id = id == null ? 0 : id;

        Integer responseCode = invoiceServiceLocal.deleteInvoice(id, Constants.SALE_INVOICE);

        if (responseCode.intValue() == -2) {
            return ResponseEntity.badRequest().body("The Credit note has an allocated invoice, you cannot deleted is item!");
        } else if (responseCode.intValue() == -3) {
            return ResponseEntity.badRequest().body("The invoice is attended to VAT Return Report, you cannot delete is item!");
        }
        return ResponseEntity.ok().body("Successfully deleted!");
    }

    @Operation(summary = "Sales Chart of Accounts", description = "Retrieve all Chart of Accounts for sales based on provided search key")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of account_id,  account_name, and code as well"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/chart-of-accounts", method = RequestMethod.GET)
    public ResponseEntity<List<ItemDto>> getAccounts() throws RestException {

        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setAccountTypes(Lists.newArrayList(Constants.REVENUE, Constants.EQUITY, Constants.LIABILITIES));

        List<EdsAccount> accounts;
        try {
            accounts = accountingManager.getAccountsForInvoice(filterParameter, false);
        } catch (Exception e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ArrayList<ItemDto> result = new ArrayList<>();
        accounts.forEach(account -> result.add(new ItemDto(account.getObjectID(), account.getName(), account.getAccountCode())));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @Override
    protected EdsInvoice getInvoice(UpdateStatusRequest request) {
        EdsSaleInvoice invoice = null;
        if (request.getObjectId() != null) {
            invoice = invoiceManager.getSaleInvoice(request.getObjectId());
        }
        if (invoice == null && StringUtils.isNotBlank(request.getNumber())) {
            List<EdsBaseSaleInvoice> list = invoiceManager.getSaleInvoiceByNumber(request.getNumber(), null);
            if (!CollectionUtils.isEmpty(list)) {
                invoice = (EdsSaleInvoice) list.get(0);
            }
        }
        return invoice;
    }

    @Override
    protected EdsQuote getOrder(UpdateStatusRequest request) {
        EdsQuote order = null;
        if (request.getObjectId() != null) {
            order = quoteManager.getSaleQuote(request.getObjectId());
        }
        if (order == null && StringUtils.isNotBlank(request.getNumber())) {
            List<EdsSaleQuote> list = quoteManager.getQuoteByNumber(request.getNumber());
            if (!CollectionUtils.isEmpty(list)) {
                order = list.get(0);
            }
        }
        if (order == null && StringUtils.isNotBlank(request.getObjectKey())) {
            order = quoteManager.getByObjectKey(request.getObjectKey());
        }
        return order;
    }

    @Override
    protected void changeInvoiceStatus(Integer objectId, String statusCode) {
        invoiceServiceLocal.changeInvoiceStatus(objectId, statusCode);
    }

    @Override
    protected void changeOrderStatus(Integer objectId, String statusCode) {
        quoteService.changeQuoteStatus(objectId, statusCode, null, false);
    }

    @Override
    protected boolean isCustomer() {
        return true;
    }

    protected InvoiceDto toInvoiceDto(NewInvoice invoice) {
        if (invoice == null) return null;

        InvoiceDto dto = new InvoiceDto();
        dto.setId(invoice.getID());
        dto.setCustomer(toItemDto(invoice.getClientID(), invoice.getClientName()));
        dto.setDate(invoice.getInvoiceDate() != null ? invoice.getInvoiceDate().getDate() : null);
        dto.setDueDate(invoice.getDueDate() != null ? invoice.getDueDate().getDate() : null);

        dto.setReference(invoice.getReference());
        dto.setNumber(invoice.getInvoiceNumber());
        dto.setStatus(invoice.getStatus());
        dto.setCurrencyCode(invoice.getCurrencySymbol());
        dto.setTaxCalcType(invoice.getTaxCalculationType() != null ? invoice.getTaxCalculationType().toString() : null);
        dto.setTotal(invoice.getTotal());
        dto.setSubTotal(invoice.getSubtotal());
        dto.setDiscountTotal(invoice.getTotalDiscount());
        dto.setTotalInInvoiceCurrency(invoice.getTotalInInvoiceCurrency());
        dto.setTaxTotal(invoice.getTotalTaxes());
        dto.setExchangeRate(invoice.getExchageRate());
        dto.setPaidAmount(invoice.getPaidAmount());
        dto.setDueAmount(getDueAmount(invoice));
        dto.setProject(toIdCode(invoice.getRelatedProjectID(), invoice.getProjectName()));
        dto.setCurrentApprover(toItemDtoFromObj(invoice.getCurrentApproverSelectItem()));
        dto.addProperty("currencyId", invoice.getCurrencyID());
        dto.addProperty("currencyName", invoice.getCurrencyName());
        dto.setItems(toInvoiceItemDto(invoice.getItems()));
        Map<Integer, Supplier<String>> taxNameMap = Map.of(
                AccountingConstants.NO_TAX_CALCULATION, () -> wfmMessageSource.localize("noTax"),
                AccountingConstants.TAX_CALCULATION_INCLUSIVE, () -> wfmMessageSource.localize("taxInclusive"),
                AccountingConstants.TAX_CALCULATION_EXCLUSIVE, () -> wfmMessageSource.localize("taxExclusive")
        );
        Integer taxType = invoice.getTaxCalculationType();
        if (taxNameMap.containsKey(taxType)) {
            IdNameTO taxCalc = new IdNameTO(taxType, taxNameMap.get(taxType).get());
            dto.setTaxCalcTypeItem(taxCalc);
        }

        return dto;
    }

    private BigDecimal getDueAmount(NewInvoice invoice) {
        if (invoice.getTotalInInvoiceCurrency() == null) {
            return BigDecimal.ZERO;
        }
        return invoice.getTotalInInvoiceCurrency()
                .subtract(invoice.getPaidAmount() == null ? BigDecimal.ZERO : invoice.getPaidAmount())
                .setScale(invoice.getCalcScale() != null ? invoice.getCalcScale() : ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
    }

    private List<LineItemDto> toInvoiceItemDto(NewInvoiceItem[] items) {
        List<LineItemDto> lineItemDtos = new ArrayList<>();
        if (items == null) return lineItemDtos;

        for (NewInvoiceItem item : items) {
            if (item == null) continue;

            LineItemDto lineItemDto = new LineItemDto();
            lineItemDto.setProduct(toItemDto(item.getItemID(), item.getItemName()));
            lineItemDto.setDescription(item.getDescription());
            lineItemDto.setAccount(toItemDtoFromObj(item.getSalesAccount()));
            lineItemDto.setQuantity(item.getQuantity());
            lineItemDto.setTaxItem(toItemDtoFromObj(item.getTaxItem()));
            lineItemDto.setUnitMeasurement(toIdCodeFromObj(item.getMeasurement()));
            lineItemDto.setUnitPrice(item.getUnitPrice());
            lineItemDto.setDiscount(item.getDiscountPercent());
            lineItemDto.setDiscountAmount(item.getDiscountAmount());
            lineItemDto.setDepartment(toItemDtoFromObj(item.getDepartmentItem()));
            if (item.getWarehouse() != null) {
                lineItemDto.setWarehouseId(item.getWarehouse().getId());
                lineItemDto.addProperty("warehouseName", item.getWarehouse().getName());
            }
            lineItemDto.addProperty("netAmount", item.getNet());

            lineItemDtos.add(lineItemDto);
        }
        return lineItemDtos;
    }

    private ItemDto toItemDto(Integer id, String name) {
        return (id != null || name != null) ? new ItemDto(id, name) : null;
    }

    private ItemDto toItemDtoFromObj(SelectItem obj) {
        return obj != null ? new ItemDto(obj.getId(), obj.getName()) : null;
    }

    private IdCode toIdCode(Integer id, String code) {
        return (id != null || code != null) ? new IdCode(id, code) : null;
    }

    private IdCode toIdCodeFromObj(SelectItem obj) {
        return obj != null ? new IdCode(obj.getId(), obj.getName()) : null;
    }

    EdsQuote validateQuoteForExistence(OrderDto dto, String type, boolean isNew) throws RestException {
        EdsQuote edsQuote = null;
        if (dto.isExistingObject()) {
            if (StringUtils.isNotBlank(dto.getObjectKey())) {
                edsQuote = quoteManager.getByObjectKey(dto.getObjectKey());
            }
            if (edsQuote == null && dto.getId() != null) {
                edsQuote = quoteManager.get(dto.getId());
            }
            if (edsQuote == null && StringUtils.isNotBlank(dto.getNumber())) {
                edsQuote = quoteManager.getQuoteByNumber(dto.getNumber()).stream().findAny().orElse(null);
            }
            if (isNew) {
                if (edsQuote != null) {
                    throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, MessageFormat.format("{0} with the given objectKey/Id or Number already exists", SALES_QUOTE.equals(type) ? "Quote" : "Order"), ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST);
                }
            } else {
                if (edsQuote == null) {
                    throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, MessageFormat.format("{0} with the given objectKey/Id or Number not found", SALES_QUOTE.equals(type) ? "Quote" : "Order"), ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST);
                }
                String statusCode = edsQuote.getStatus().getCode();
                if (Arrays.asList(INVOICED, PARTIAL_INVOICED, CONVERTED, PARTIAL_SHIPPED, SHIPPED).contains(statusCode)) {
                    throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, MessageFormat.format("You cannot modify the {0}, cause of this quote is {1}", SALES_QUOTE.equals(type) ? "quote" : "order", statusCode), ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST);
                }
            }
        }
        return edsQuote;
    }

    SaveResult saveInvoice(InvoiceDto dto, boolean isNew) throws RestException {
        EdsInvoice edsInvoice = isNew ? null : validateInvoiceForExistence(dto, SALES_INVOICE);
        /**
         * Basic validation process flow
         */
        validate(dto, SALES_INVOICE);

        /**
         * Validate a customer for exist,
         * if not create a new one in synchronously
         */
        validateCustomerSupplierExist(dto);

        NewInvoice invoice = dtoUtils.wrapToInvoiceModel(dto, dto.isCreditNote() ? CREDIT_NOTE : SALES_INVOICE);


        if (!dto.isCreditNote()) {
            /**
             * Validate Stock Existence
             * We check stock availability after wrapping it to transfer object as we dont always have product id in line items
             */
            validateStockAvailability(invoice);
        }
        invoice.setID(edsInvoice != null ? edsInvoice.getObjectID() : null);

        if (!CollectionUtils.isEmpty(dto.getPayments())) {
            validatePayments(invoice, dto.getPayments());
        }
        SaveResult result;
        if (dto.isCreditNote()) {
            if (dto.getCreditedInvoice() != null) {
                EdsInvoice creditedInvoice = null;
                IdCode creditedInvoiceItem = dto.getCreditedInvoice();
                if (creditedInvoiceItem.getId() != null) {
                    creditedInvoice = invoiceManager.get(creditedInvoiceItem.getId());
                }
                if (creditedInvoice == null && StringUtils.isNotBlank(creditedInvoiceItem.getCode())) {
                    List<EdsBaseSaleInvoice> invoices = invoiceManager.getSaleInvoiceByNumber(creditedInvoiceItem.getCode(), null);
                    if (invoices != null && !invoices.isEmpty()) {
                        creditedInvoice = invoices.get(0);
                    }
                }
                if (creditedInvoice == null && StringUtils.isNotBlank(creditedInvoiceItem.getObjectKey())) {
                    creditedInvoice = invoiceManager.getByObjectKey(creditedInvoiceItem.getObjectKey());
                }
                invoice.setCreditedInvoiceID(creditedInvoice != null ? creditedInvoice.getObjectID() : null);
            }
            result = invoice.getID() == null ? invoiceServiceLocal.saveCreditNote(invoice) : invoiceServiceLocal.updateCreditNote(invoice);
        } else {
            result = invoice.getID() == null ? invoiceServiceLocal.saveSaleInvoice(invoice) : invoiceServiceLocal.updateSaleInvoice(invoice);
        }
        if (result.isInvoiceExist()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, MessageFormat.format("Sales Invoice with number {0} already exists", invoice.getInvoiceNumber()), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (!CollectionUtils.isEmpty(dto.getPayments())) {
            invoice.setID(result.getId());
            if (edsInvoice != null && !CollectionUtils.isEmpty(edsInvoice.getPayments())) {
                edsInvoice.getPayments().stream().filter(payment -> payment.getHistoricalParent() == null).forEach(payment -> invoiceServiceLocal.deletePayment(payment.getObjectID()));
            }
            makePayments(invoice, dto.getPayments());
        }
        return result;
    }


    SaveResult saveQuote(OrderDto dto, String type, boolean isNew) throws RestException {
        if (isNew) {
            validateQuoteForExistence(dto, type, true);
        }
        EdsQuote edsQuote = isNew ? null : validateQuoteForExistence(dto, SALES_QUOTE, false);

        /**
         * Basic validation process flow
         */
        validate(dto, type);

        /**
         * Validate a custom for exist,
         * if not create a new one in synchronously
         */
        validateCustomerSupplierExist(dto);

        NewInvoice quote = dtoUtils.wrapToModel(dto, type);
        quote.setID(edsQuote != null ? edsQuote.getObjectID() : null);
        SaveResult result = quote.getID() == null ? quoteServiceLocal.saveSaleQuote(quote) : quoteServiceLocal.updateSaleQuote(quote);
        return result;
    }

    @RequestMapping(value = "/numberdata", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<String> getNumberData() {
        return ResultTO.success(dtoUtils.getAutoGeneratedNumber(SALES_INVOICE).getInvoiceNumber());
    }

    @RequestMapping(value = "/managers", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResultTO<List<SelectItem>> getManagers(@RequestParam(required = false) String searchKey) {
        List<EdsApprover> approvers = approverManager.list("saleinvoice", null);
        if (approvers != null && !approvers.isEmpty()) {
            ListingFilterParameter filterParameter = new ListingFilterParameter();
            filterParameter.setApproverID(approvers.get(0).getObjectID());
            filterParameter.setListEmployees(true);
            filterParameter.setLookUp(true);
            filterParameter.setSearchKey(searchKey != null ? searchKey : null);
            filterParameter.setListCandidates(false);
            filterParameter.setResignedEmployeesIncluded(false);
            filterParameter.setShowDepartment(false);
            filterParameter.setCRM(false);
            filterParameter.setWithCode(false);
            filterParameter.setLimit(20);
            return ResultTO.success(Arrays.asList(allInOneService.getEmployeesAsSelectItem(new ListLoadConfig(), filterParameter)));
        }
        return null;
    }

    @RequestMapping(value = "payments/{relationId}/list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultTO<List<PaymentTO>> getPayments(@PathVariable(value = "relationId") Integer relationId) throws RestException {
        log.info("REST request to get payments by invoiceId: {}", relationId);
        NewInvoice invoice = invoiceServiceLocal.getInvoice(relationId);
        if (invoice == null) {
            throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Invoice doesn't exist", ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST);
        }
        if (invoice.getPaymentItems() == null) {
            return ResultTO.success(List.of());
        }
        List<PaymentTO> payments = new ArrayList<>();
        for (PaymentItem item : invoice.getPaymentItems()) {
            PaymentTO to = new PaymentTO();
            to.setId(item.getObjectId());
            to.setBatchPaymentId(item.getBatchPaymentID());
            to.setDate(item.getDate().getDateLong());
            to.setNumber(item.getNumber());
            to.setAmount(item.getAmount());
            to.setReference(item.getReference());
            to.setExchangeRate(item.getExchangeRate());
            payments.add(to);
        }
        return ResultTO.success(payments);
    }
}
