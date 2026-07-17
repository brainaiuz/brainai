package com.edatasite.workforce.rest.v3.release10.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsPurchaseOrder;
import com.edatasite.workforce.core.domain.accounting.EdsQuote;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.MessageCommand;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.invoice.client.rpc.BatchPaymentResult;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.SaveResult;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.to.ListResultTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.BaseInvoiceDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.InvoiceDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.InvoicePaymentDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.OrderDto;
import com.edatasite.workforce.rest.v3.release10.accounting.request.UpdateStatusRequest;
import com.edatasite.workforce.rest.v3.release10.core.helper.ListingFilterHelperV3;
import com.edatasite.workforce.rest.v3.release10.core.to.ListParamsDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.ws.rs.QueryParam;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.CONVERTED;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.INVOICED;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.PARTIAL_INVOICED;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.PARTIAL_RECEIVED;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.PARTIAL_SHIPPED;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.RECEIVED;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SHIPPED;

/**
 * Created by Normurod Buriev.
 * Date: 12/21/2020 6:40 AM
 */
@Tag(name = "Purchases Api Resource", description = "Here is a purchase api resouce that contains Purchase Invoice and Purchase Order")
@RestController
@RequestMapping(value = "/purchase", headers = {ApiConstants.X_AUTH, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class PurchasesApiResource extends AbstractInvoiceQuoteApiResource {

    @RequestMapping(value = "/order/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<ListResultTO<OrderDto>> orderList(@RequestBody ListParamsDTO params) {
        ListingFilterParameter fp = ListingFilterHelperV3.createListingFilter(params, ListPanelType.PurchaseOrderListPanel);
        configureDateFiltersWithFacet(fp);

        return ResponseEntity.ok(invoiceAPIService.getPurchaseOrderList(fp));
    }

    @RequestMapping(value = "/invoice/list", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<ListResultTO<InvoiceDto>> invoiceList(@RequestBody ListParamsDTO params) {
        ListingFilterParameter fp = ListingFilterHelperV3.createListingFilter(params, ListPanelType.PurchaseInvoicePanel);
        configureDateFiltersWithFacet(fp);

        return ResponseEntity.ok(invoiceAPIService.getPurchaseInvoiceList(fp));
    }

    @RequestMapping(value = "/order", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<?> createOrder(@RequestBody @Valid OrderDto dto) throws RestException {
        SaveResult result = saveOrder(dto, true);
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/order", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<?> updateOrder(@RequestBody @Valid OrderDto dto) throws RestException {
        SaveResult result = saveOrder(dto, false);
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/invoice", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<?> createInvoice(@Valid @RequestBody InvoiceDto dto) throws RestException {
        SaveResult result = saveInvoice(dto, true);
        return new ResponseEntity<SaveResult>(result, HttpStatus.OK);
    }

    @RequestMapping(value = "/invoice", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<?> updateInvoice(@Valid @RequestBody InvoiceDto dto) throws RestException {
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

    @RequestMapping(value = "/order", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteOrder(@QueryParam("id") Integer id, @QueryParam("number") String number) {
        if (id == null && StringUtils.isBlank(number)) {
            return ResponseEntity.badRequest().body("Order Id or Number for deleting process must be provided!");
        }

        if (StringUtils.isNotBlank(number)) {
            //here assumed that order number is a unique
            List<EdsPurchaseOrder> orders = quoteManager.getPurchaseOrderByNumber(number, null);

            if (!orders.isEmpty()) {
                id = orders.get(0).getObjectID();
            }
        }
        id = id == null ? 0 : id;

        TestRPC response = quoteServiceLocal.deleteQuote(id, Constants.PURCHASE_ORDER);

        if (MessageCommand.hasConvertedItems.equals(response.getMessageCommand())) {
            return ResponseEntity.badRequest().body("You cannot deleted PO, couse it has converted items!");
        } else if (MessageCommand.hasOutTransactions.equals(response.getMessageCommand())) {
            return ResponseEntity.badRequest().body("You cannot deleted PO, couse it has out transactions!");
        } else if (MessageCommand.hasShippingData.equals(response.getMessageCommand())) {
            return ResponseEntity.badRequest().body("You cannot deleted PO, couse it has shipping data!");
        }
        return ResponseEntity.ok().body("Successfully deleted!");
    }

    @RequestMapping(value = "/invoice", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteInvoice(@QueryParam("id") Integer id, @QueryParam("number") String number) {
        if (id == null && StringUtils.isBlank(number)) {
            return ResponseEntity.badRequest().body("Invoice Id or Number for deleting process must be provided!");
        }

        if (StringUtils.isNotBlank(number)) {
            //here assumed that invoice number is a unique
            List<EdsPurchaseInvoice> invoices = invoiceManager.getPurchaseInvoiceByNumber(number, null, null);

            if (!invoices.isEmpty()) {
                id = invoices.get(0).getObjectID();
            }
        }
        id = id == null ? 0 : id;

        Integer responseCode = invoiceServiceLocal.deleteInvoice(id, Constants.SALE_INVOICE);

        if (responseCode.intValue() == -2) {
            return ResponseEntity.badRequest().body("The Debit note has an allocated invoice, you cannot deleted is item!");
        } else if (responseCode.intValue() == -3) {
            return ResponseEntity.badRequest().body("The invoice is attended to VAT Return Report, you cannot delete is item!");
        }
        return ResponseEntity.ok().body("Successfully deleted!");
    }

    @Override
    protected EdsInvoice getInvoice(UpdateStatusRequest request) {
        EdsPurchaseInvoice invoice = null;
        if (request.getObjectId() != null) {
            invoice = invoiceManager.getPurchaseInvoice(request.getObjectId());
        }
        if (invoice == null && StringUtils.isNotBlank(request.getNumber())) {
            List<EdsPurchaseInvoice> list = invoiceManager.getPurchaseInvoiceByNumber(request.getNumber(), null, null);
            if (!org.springframework.util.CollectionUtils.isEmpty(list)) {
                invoice = list.get(0);
            }
        }
        return invoice;
    }

    @Override
    protected EdsQuote getOrder(UpdateStatusRequest request) {
        EdsQuote order = null;
        if (request.getObjectId() != null) {
            order = quoteManager.getPurchaseOrderByID(request.getObjectId());
        }
        if (order == null && StringUtils.isNotBlank(request.getNumber())) {
            List<EdsPurchaseOrder> list = quoteManager.getPurchaseOrderByNumber(request.getNumber(), null);
            if (!org.springframework.util.CollectionUtils.isEmpty(list)) {
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
        invoiceServiceLocal.changePurchaseInvoiceStatus(objectId, statusCode);
    }

    @Override
    protected void changeOrderStatus(Integer objectId, String statusCode) {
        quoteService.changeQuoteStatus(objectId, statusCode, null, false);
    }

    @Override
    protected boolean isCustomer() {
        return false;
    }

    EdsQuote validateOrderForExistence(BaseInvoiceDto dto, boolean isNew) throws RestException {
        EdsQuote order = null;
        if (dto.isExistingObject()) {
            if (StringUtils.isNotBlank(dto.getObjectKey())) {
                order = quoteManager.getByObjectKey(dto.getObjectKey());
            }
            if (order == null && dto.getId() != null) {
                order = quoteManager.get(dto.getId());
            }
            if (order == null && StringUtils.isNotBlank(dto.getNumber())) {
                order = quoteManager.getPurchaseOrderByNumber(dto.getNumber(), null).stream().findAny().orElse(null);
            }
            if (isNew) {
                if (order != null) {
                    throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Purchase Order with the given objectKey/Id or Number already exists", ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST);
                }
            } else {
                if (order == null) {
                    throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "Purchase Order with the given objectKey/Id or Number not found", ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST);
                }
                String statusCode = order.getStatus().getCode();
                if (Arrays.asList(INVOICED, PARTIAL_INVOICED, CONVERTED, PARTIAL_SHIPPED, SHIPPED, PARTIAL_RECEIVED, RECEIVED).contains(statusCode)) {
                    throw new RestException(ApiConstants.GENERAL_ERROR_MESSAGE, "You cannot modify the Purchase Order", ApiConstants.NOT_FOUND, HttpStatus.BAD_REQUEST);
                }
            }
        }
        return order;
    }

    SaveResult saveOrder(OrderDto dto, boolean isNew) throws RestException {
        if (isNew) {
            validateOrderForExistence(dto, true);
        }
        EdsQuote edsOrder = isNew ? null : validateOrderForExistence(dto, false);
        /**
         * Basic validation process flow
         */
        validate(dto, PURCHASE_ORDER);

        /**
         * Validate a custom for exist,
         * if not create a new one in synchronously
         */
        validateCustomerSupplierExist(dto);

        NewInvoice order = dtoUtils.wrapToModel(dto, PURCHASE_ORDER);
        order.setID(edsOrder != null ? edsOrder.getObjectID() : null);
        SaveResult result = order.getID() == null ? quoteServiceLocal.savePurchaseOrder(order) : quoteServiceLocal.updatePurchaseOrder(order, false);

        if (result.isInvoiceExist()) {
            throw new RestException(IN_VALID_DATA, "Purchase Order already exist with this number: " + dto.getNumber(), SERVER_ERROR, HttpStatus.BAD_REQUEST);
        }
        return result;
    }

    SaveResult saveInvoice(InvoiceDto dto, boolean isNew) throws RestException {
        EdsInvoice edsInvoice = isNew ? null : validateInvoiceForExistence(dto, PURCHASE_INVOICE);
        /**
         * Basic validation process flow
         */
        validate(dto, PURCHASE_INVOICE);

        /**
         * Validate a custom for exist,
         * if not create a new one in synchronously
         */
        validateCustomerSupplierExist(dto);

        NewInvoice invoice = dtoUtils.wrapToInvoiceModel(dto, PURCHASE_INVOICE);
        invoice.setID(edsInvoice != null ? edsInvoice.getObjectID() : null);

        if (!CollectionUtils.isEmpty(dto.getPayments())) {
            validatePayments(invoice, dto.getPayments());
        }
        SaveResult result = invoice.getID() == null ? invoiceServiceLocal.savePurchaseInvoice(invoice) : invoiceServiceLocal.updatePurchaseInvoice(invoice);

        if (!CollectionUtils.isEmpty(dto.getPayments())) {
            invoice.setID(result.getId());
            makePayments(invoice, dto.getPayments());
        }
        return result;
    }
}
