package com.edatasite.workforce.rest.v2.release10.accounting;

import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsInvoicingSettings;
import com.edatasite.workforce.core.domain.EdsItem;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsVat;
import com.edatasite.workforce.core.domain.accounting.EdsAccount;
import com.edatasite.workforce.core.domain.accounting.EdsBankAccount;
import com.edatasite.workforce.core.domain.accounting.EdsBaseSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsInvoicePayment;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsShippingMethod;
import com.edatasite.workforce.core.domain.accounting.EdsWarehouse;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.documents.EdsFolder;
import com.edatasite.workforce.gwt.accounting.client.rpc.ProductSelectItem;
import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.accounting.server.app.ProductServiceLocal;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyServiceLocal;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AccountingManager;
import com.edatasite.workforce.gwt.core.server.db.AddressManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CrmContactManager;
import com.edatasite.workforce.gwt.core.server.db.CurrencyManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.InvoicingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.ItemManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.VatManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.ShippingMethodManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.WarehouseManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FolderManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.crm.server.app.CrmServiceLocal;
import com.edatasite.workforce.gwt.documents.client.gwtupload.UUID;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.server.GwtUploadServlet;
import com.edatasite.workforce.gwt.documents.server.app.DocumentsServiceLocal;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.Params;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.ReceivePaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.SaveResult;
import com.edatasite.workforce.gwt.invoice.client.rpc.TotalTaxItem;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceCircularResolver;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.to.ShippingMethodTO;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.PaymentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.AccountTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.AddressTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.CurrencyTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.InvoiceItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.InvoiceListItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.InvoiceStatusTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.ItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.SalesInvoiceAddResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.SalesInvoiceAddTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.SupplierTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.WarehouseTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.ZapierInvoiceItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.ZapierShopifyInvoiceItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.ZapierShopifyOrderTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.ZapierShopifyTaxItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.product.CustomerTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.RequestListSearchData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseListData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseResultListData;
import com.edatasite.workforce.rest.v2.release10.core.to.documents.FileTO;
import com.edatasite.workforce.rest.v2.release10.enums.InvoiceStatusEnum;
import com.edatasite.workforce.rest.v2.release10.enums.TaxTypeEnum;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.utils.EdsContextParams;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants.COPY_INVOICE_TO_CREDITNOTE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.APPROVE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.ONE_OFF_FIXED_AMOUNT;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.PAID;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.RECEIVABLE;

/**
 * Created by Dilsh0d on 11/2/2017.
 */
@Tag(name = "Sales Invoice", description = "Sales Invoice API")
@RestController()
@RequestMapping(value = "/2", headers = {ApiConstants.X_AUTH, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiSalesInvoiceControllerV2 extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiSalesInvoiceControllerV2.class);

    @Autowired
    private InvoiceServiceLocal invoiceServiceLocal;
    @Autowired
    private CrmServiceLocal crmServiceLocal;
    @Autowired
    private ProductServiceLocal productServiceLocal;
    @Autowired
    private AddressManager addressManager;
    @Autowired
    private InvoiceCircularResolver invoiceCircularResolver;
    @Autowired
    private DocumentsServiceLocal documentsServiceLocal;
    @Autowired
    private ServletContext servletContext;
    @Autowired
    private FolderManager folderManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private AccountingServiceLocal accountingServiceLocal;
    @Autowired
    private CurrencyServiceLocal currencyServiceLocal;
    @Autowired
    private AccountingManager accountingManager;
    @Autowired
    private InvoiceManager invoiceManager;
    @Autowired
    private CurrencyManager currencyManager;
    @Autowired
    private ItemManager itemManager;
    @Autowired
    private CrmContactManager crmContactManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private WarehouseManager warehouseManager;
    @Autowired
    private VatManager vatManager;
    @Autowired
    private InvoicingSettingsManager invoicingSettingsManager;
    @Autowired
    private ShippingMethodManager shippingMethodManager;


    @Operation(summary = "Get Sales Invoice List", description = "Retrieves Sales Invoice List by search_text")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have Sales Invoice details "),
            @ApiResponse(responseCode = "400", description = "Start point and limit is required"),
            @ApiResponse(responseCode = "422", description = "Start point and limit can not be zero at the same time"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/sales_invoices", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
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
        filterParameter.setClientId(requestListSearchData.getEntity_id());

        ListResult<NewInvoice> salesList;
        try {
            salesList = invoiceCircularResolver.getSaleInvoiceData(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ArrayList<InvoiceListItemTO> resultList = new ArrayList<>();
        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        for (NewInvoice item : salesList.getList()) {
            InvoiceListItemTO salesInvoice = new InvoiceListItemTO();
            salesInvoice.setId(item.getID());
            salesInvoice.setInvoice_number(item.getInvoiceNumber());
            salesInvoice.setInvoice_date(longDateTimezoneFormat.format(item.getInvoiceDate().getNonConvertedDate()));
            salesInvoice.setInvoice_status(new InvoiceStatusTO(item.getStatus(), item.getStatusCode()));
            salesInvoice.setInvoice_total(item.getTotal());

            CurrencyTO currency = new CurrencyTO();
            currency.setCurrency_id(item.getCurrencyID());
            currency.setCurrency_name(item.getCurrencyName());

            salesInvoice.setInvoice_currency(currency);

            salesInvoice.setRelatedProject(item.getRelatedProjectName());

            resultList.add(salesInvoice);
        }
        ResponseResultListData<InvoiceListItemTO> resultListData = new ResponseResultListData<>();
        resultListData.setList(resultList);
        resultListData.setTotal(salesList.getTotal());
        return successResponse(resultListData);

    }


    @Operation(summary = "Get Sales Invoice List for Zapier", description = "Retrieves Sales Invoice List by search_text")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have Sales Invoice details "),
            @ApiResponse(responseCode = "400", description = "Start point and limit is required"),
            @ApiResponse(responseCode = "422", description = "Start point and limit can not be zero at the same time"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/sales_invoices_zapier", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getListForZapier(@RequestBody RequestListSearchData requestListSearchData) throws RestException {

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
        filterParameter.setClientId(requestListSearchData.getEntity_id());

        ListResult<NewInvoice> salesList;
        try {
            salesList = invoiceCircularResolver.getSaleInvoiceData(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ArrayList<ZapierInvoiceItemTO> resultList = new ArrayList<>();
        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        for (NewInvoice item : salesList.getList()) {
            ZapierInvoiceItemTO salesInvoice = new ZapierInvoiceItemTO();
            salesInvoice.setId(item.getID());
            salesInvoice.setInvoice_number(item.getInvoiceNumber());
            salesInvoice.setInvoice_date(longDateTimezoneFormat.format(item.getInvoiceDate().getNonConvertedDate()));
            salesInvoice.setInvoice_status(new InvoiceStatusTO(item.getStatus(), item.getStatusCode()));
            salesInvoice.setInvoice_total(item.getTotal());

            CurrencyTO currency = new CurrencyTO();
            currency.setCurrency_id(item.getCurrencyID());
            currency.setCurrency_name(item.getCurrencyName());

            salesInvoice.setInvoice_currency(currency);
            //Get Other details
            NewInvoice invoice = invoiceServiceLocal.getInvoiceSummaryData(item.getID());
            if (invoice != null) {
                salesInvoice.setReference(invoice.getReference());
                if (invoice.getDueDate() != null) {
                    salesInvoice.setDue_date(invoice.getDueDate().getDate());
                }
                salesInvoice.setSubtotal(invoice.getSubtotal());

                salesInvoice.setBase_total(invoice.getTotal());
                salesInvoice.setBase_subtotal(invoice.getSubtotal());
                salesInvoice.setDiscount_total(invoice.getTotalDiscount());
                salesInvoice.setBase_discount_total(invoice.getDiscountAmount());
                salesInvoice.setTax_total(invoice.getTotalTaxes());
                salesInvoice.setBase_tax_total(invoice.getTotalTaxes());
                salesInvoice.setDue_amount(invoice.getDueAmount());
                salesInvoice.setExchange_rate(invoice.getExchageRate());
                salesInvoice.setShipping_price(invoice.getShippingPrice());

                salesInvoice.setCustomer(new CustomerTO(invoice.getTypeItem().getId(), invoice.getTypeItem().getName(), invoice.getClientContactEmail()));
                salesInvoice.setBill_to_address(new com.edatasite.workforce.rest.base.to.AddressTO(invoice.getBillAddress()));
                salesInvoice.setShip_to_address(new com.edatasite.workforce.rest.base.to.AddressTO(invoice.getMailAddress()));
                salesInvoice.setIntroduction(invoice.getIntroduction());
//                com.edatasite.workforce.rest.base.to.SelectItemTO invoice_type;
                if (invoice.getShippingMethod() != null) {
                    ShippingMethodTO shippingMethodTO = new ShippingMethodTO();
                    shippingMethodTO.setName(invoice.getShippingMethodName());
                    salesInvoice.setShipping_method(shippingMethodTO);
                }
                /*com.edatasite.workforce.rest.base.to.SelectItemTO terms;
                com.edatasite.workforce.rest.base.to.SelectItemTO tax_type;
                com.edatasite.workforce.rest.base.to.SelectItemTO bank_account;
                com.edatasite.workforce.rest.base.to.SelectItemTO account;
                com.edatasite.workforce.rest.base.to.SelectItemTO email_template;*/
                ArrayList<InvoiceItemTO> items = new ArrayList<>();
                if (invoice.getItems() != null) {
                    for (NewInvoiceItem invoiceItem : invoice.getItems()) {
                        EdsItem product = itemManager.get(invoiceItem.getItemID());
                        if (product != null) {
                            InvoiceItemTO invoiceItemTO = new InvoiceItemTO();
                            ItemTO itemTO = new ItemTO(invoiceItem.getItemID(), invoiceItem.getItemName(), invoiceItem.getItemNumber(), ServerUtils.getProductTypeName(invoiceItem.getProductType()));
                            itemTO.setSku(product.getInternalSKUNumber());
                            itemTO.setQuantity(product.getQty());
                            itemTO.setDescription(product.getDescription());
                            if (product.getSuppliers() != null && !product.getSuppliers().isEmpty()) {
                                for (EdsCrmAccount supplier : product.getSuppliers()) {
                                    itemTO.setSupplier(new SupplierTO(supplier.getObjectID(), supplier.getName()));
                                    break;
                                }
                            }
                            if (product.getParent() != null) {
                                itemTO.setParent_item_name(product.getParent().getName());
                            }

                            invoiceItemTO.setItem(itemTO);
                            invoiceItemTO.setItem_quantity(invoiceItem.getQuantity());
                            invoiceItemTO.setItem_price(invoiceItem.getUnitPrice());
                            invoiceItemTO.setZapiervariantid(product.getZapiervariantid());
                            items.add(invoiceItemTO);
                        }
                    }
                }
                salesInvoice.setItems(items);
            }

            resultList.add(salesInvoice);
        }
        /*ResponseResultListData resultListData = new ResponseResultListData();
        resultListData.setList(resultList);
        resultListData.setTotal(salesList.getTotal());
        return successResponse(resultListData);*/
        return resultList;
    }

    @Operation(summary = "Add New Sales Invoice", description = "Adds new Sales Invoice")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with error code "),
            @ApiResponse(responseCode = "404", description = "Invoice data is not found"),
            @ApiResponse(responseCode = "400", description = "Invoice Status is required or Invoice date is required or Invoice due date is required"),
            @ApiResponse(responseCode = "422", description = "Invalid Sales Invoice status or Invalid invoice date format"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/sales_invoice", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object add(@RequestBody @Validated SalesInvoiceAddTO salesInvoice) throws RestException {
        if (salesInvoice == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Invoice data is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (salesInvoice.getInvoice_status() == null || StringUtils.isBlank(salesInvoice.getInvoice_status().getStatus_code())) {
            throw new RestException("Invoice status is required", "Invoice status is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (InvoiceStatusEnum.getStatus(salesInvoice.getInvoice_status().getStatus_code()) == null) {
            throw new RestException("Invalid invoice status", "Invalid invoice status " + salesInvoice.getInvoice_status().getStatus_code(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (StringUtils.isBlank(salesInvoice.getInvoice_date())) {
            throw new RestException("Invoice date not provided", "Invoice date is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(salesInvoice.getInvoice_due_date())) {
            throw new RestException("Invoice due date is not provided", "Invoice due date is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        try {
            longDateTimezoneFormat.parse(salesInvoice.getInvoice_date());
        } catch (ParseException e) {
            log.error("", e);
            throw new RestException("Invalid invoice date format", "Invalid invoice date format. Acceptable format for invoice is " + longDateTimezoneFormat.toPattern(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        try {
            longDateTimezoneFormat.parse(salesInvoice.getInvoice_due_date());
        } catch (ParseException e) {
            log.error("", e);
            throw new RestException("Invalid invoice due date format", "Invalid invoice date format. Acceptable format for invoice is " + longDateTimezoneFormat.toPattern(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (salesInvoice.getCustomer() == null || salesInvoice.getCustomer().getCustomer_id() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Invoice customer is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (salesInvoice.getInvoice_items() == null || salesInvoice.getInvoice_items().isEmpty()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Invoice item is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        for (InvoiceItemTO invoiceItem : salesInvoice.getInvoice_items()) {
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
            if (invoiceItem.getItem_sales_account() == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Invoice item sales account is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            if (invoiceItem.getItem_sales_account().getSales_account_id() == null || invoiceItem.getItem_sales_account().getSales_account_id() <= 0) {
                throw new RestException("Invoice item sales account is required", "Invoice item sales account is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            if (invoiceItem.getItem_warehouse() == null || invoiceItem.getItem_warehouse().getWarehouse_id() == null) {
                throw new RestException("Invoice item warehouse is required", "Invoice item warehouse is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }

        }

        if (salesInvoice.getPayments() != null) {
            int it = 0;
            for (PaymentTO paymentItem : salesInvoice.getPayments()) {
                it++;
                if (paymentItem.getPaid_amount() == null) {
                    throw new RestException("payment item paid amount is empty #" + it, "payment item paid amount is empty #" + it, REQUIRED, HttpStatus.BAD_REQUEST);
                }
                if (paymentItem.getPayment_account_id() == null) {
                    throw new RestException("payment item payment account is empty #" + it, "payment item payment account is empty #" + it, REQUIRED, HttpStatus.BAD_REQUEST);
                }
                if (StringUtils.isBlank(paymentItem.getPaid_date())) {
                    throw new RestException(" payment item paid date is empty #" + it, " payment item paid date is empty #" + it, REQUIRED, HttpStatus.BAD_REQUEST);
                } else if (ServerUtils.parseDate(paymentItem.getPaid_date(), "dd-MM-yyyy'T'hh:mm:ssZ") == null) {
                    throw new RestException("payment item paid date is not compatible with pattern (dd-MM-yyyy'T'hh:mm:ssZ) #" + it, "payment item paid date is not compatible with pattern (dd-MM-yyyy'T'hh:mm:ssZ) #" + it, REQUIRED, HttpStatus.BAD_REQUEST);
                }
            }
        }

        NewInvoice invoice = convertSalesInvoice(salesInvoice, longDateTimezoneFormat);

        if (invoice.getID() == null || invoice.getID() == 0) {
            invoice.setNumberData(invoiceServiceLocal.getSaleInvoiceNumber());
            invoice.setInvoiceNumber(invoice.getNumberData().getInvoiceNumber());
        }

        SaveResult saveResult = invoiceServiceLocal.saveSaleInvoice(invoice);
        if (saveResult.isInvoiceExist()) {
            throw new RestException("Invoice number is already used", "Invoice number " + salesInvoice.getInvoice_number() + " is already used", CONFLICT, HttpStatus.CONFLICT);
        }

        //Generate invoice payment
        SalesInvoiceAddResultTO salesInvoiceAddResultTO = new SalesInvoiceAddResultTO();
        salesInvoiceAddResultTO.setId(saveResult.getId());
        salesInvoiceAddResultTO.setInvoice_number(saveResult.getNumber());
        if (salesInvoice.getInvoice_status() != null && APPROVE.equalsIgnoreCase(salesInvoice.getInvoice_status().getStatus_code())
                && invoice.getTotal() != null && invoice.getTotal().compareTo(BigDecimal.ZERO) != 0) {
            EdsInvoicingSettings invSettings = invoicingSettingsManager.getInvoiceSettings(userManager.getUser().getCompany());
            EdsAccount defaultAccount = accountingManager.get(invSettings.getDefaultPaymentAccountId());


            ReceivePaymentData receivePaymentData = new ReceivePaymentData();
            receivePaymentData.setAccount(defaultAccount != null ? defaultAccount.getAsSelectItem() : null);
            receivePaymentData.setBatchPayment(true);
            SelectItem crmAccount = null;
            if (salesInvoice.getCustomer() != null && salesInvoice.getCustomer().getCustomer_id() != null) {
                EdsCrmAccount edsCrmAccount = crmAccountManager.get(salesInvoice.getCustomer().getCustomer_id());
                if (edsCrmAccount != null) {
                    crmAccount = edsCrmAccount.getAsSelectItem();
                }
            }
            receivePaymentData.setCrmAccount(crmAccount);
            receivePaymentData.setCurrency(invoice.getCurrencyID() != null ? currencyManager.get(invoice.getCurrencyID()).createCurrencyItem() : null);
            receivePaymentData.setExRate(invoice.getExchageRate());
            receivePaymentData.setReference(invoice.getInvoiceNumber());
            receivePaymentData.setDate(invoice.getInvoiceDate());
            receivePaymentData.setTotalAmount(invoice.getTotal());
            receivePaymentData.setProject(invoice.getRelatedProject());
            receivePaymentData.setPdfTemplateID(invoice.getPdfTemplateID());
            receivePaymentData.setPaymentTarget("INVOICE");
            receivePaymentData.setType(RECEIVABLE);

            ArrayList<PaymentData> payments = new ArrayList<>();
            if (salesInvoice.getPayments() != null && salesInvoice.getPayments().size() > 0) {

                final SelectItem defaultAcc = defaultAccount != null ? defaultAccount.getAsSelectItem() : null;

                salesInvoice.getPayments().forEach(p -> {
                    PaymentData paymentData = new PaymentData();

                    EdsAccount paymentAccount = accountingManager.get(p.getPayment_account_id());
                    if (paymentAccount != null) {
                        paymentData.setPaymentAccount(paymentAccount.getAsSelectItem());
                    } else {
                        paymentData.setPaymentAccount(defaultAcc);
                    }

                    paymentData.setReferenceNumber(p.getReference());
                    paymentData.setInvoiceID(saveResult.getId());
                    paymentData.setPaymentAmount(p.getPaid_amount());
                    paymentData.setExchangeRate(invoice.getExchageRate());
                    paymentData.setOpeningBalance(false);
                    paymentData.setManualJournal(false);
                    paymentData.setDate(new DateNonConvertable(ServerUtils.parseDate(p.getPaid_date(), "dd-MM-yyyy'T'hh:mm:ssZ")));
                    paymentData.setPaymentTypeID(p.getPayment_method_id());
                    payments.add(paymentData);

                    receivePaymentData.setAccount(paymentData.getPaymentAccount());
                });
            } else {
                PaymentData paymentData = new PaymentData();
                paymentData.setReferenceNumber(invoice.getInvoiceNumber());
                paymentData.setInvoiceID(saveResult.getId());
                paymentData.setPaymentAmount(salesInvoice.getInvoice_total());
                paymentData.setExchangeRate(invoice.getExchageRate());
                paymentData.setOpeningBalance(false);
                paymentData.setManualJournal(false);
                paymentData.setDate(invoice.getInvoiceDate());
                paymentData.setPaymentAccount(defaultAccount.getAsSelectItem());

                payments.add(paymentData);
            }
            receivePaymentData.setPayments(payments.toArray(new PaymentData[]{}));
            salesInvoiceAddResultTO.setBatchPaymentResult(invoiceServiceLocal.saveReceivePaymentData(receivePaymentData, true));
            return successResponse(salesInvoiceAddResultTO);
        }

        return successResponse(salesInvoiceAddResultTO);
    }

    @Operation(summary = "Add New Sales Invoice From Zapier", description = "Adds new Sales Invoice From Zapier")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with error code "),
            @ApiResponse(responseCode = "404", description = "Invoice data is not found"),
            @ApiResponse(responseCode = "400", description = "Invoice Status is required or Invoice date is required or Invoice due date is required"),
            @ApiResponse(responseCode = "422", description = "Invalid Sales Invoice status or Invalid invoice date format"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/sales_invoice_create_zapier", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object addOrderForZapier(HttpServletRequest request, @RequestBody String json) throws RestException {

        ZapierShopifyOrderTO shopifyOrderTO = null;

        try {
            json = json.replace("\"tax_line_items\":\"\",", "");
            json = json.replace("\"line_items\":\"\",", "");
            log.info("JSON: {}", json);
            shopifyOrderTO = new ObjectMapper().readValue(json, ZapierShopifyOrderTO.class);
        } catch (Exception e) {
            log.error("Error Occured: " + json);
            log.error("", e);
        }
        if (shopifyOrderTO == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Invoice data is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (StringUtils.isBlank(shopifyOrderTO.getStatus())) {
            throw new RestException("Invoice status is required", "Invoice status is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (StringUtils.isBlank(shopifyOrderTO.getInvoice_date())) {
            throw new RestException("Invoice date not provided", "Invoice date is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat("yyyy-MM-dd");

        try {
            //2019-07-23
            longDateTimezoneFormat.parse(shopifyOrderTO.getInvoice_date());
        } catch (ParseException e) {
            log.error("", e);
            throw new RestException("Invalid invoice date format", "Invalid invoice date format. Acceptable format for invoice is " + longDateTimezoneFormat.toPattern(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        for (ZapierShopifyInvoiceItemTO invoiceItem : shopifyOrderTO.getLine_items()) {
            if (StringUtils.isBlank(invoiceItem.getItem_name())) {
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
        }

        NewInvoice invoice = convertShopifyOrderToSalesInvoice(shopifyOrderTO, longDateTimezoneFormat);

        EdsSaleInvoice saleInvoice = invoiceManager.getSaleInvoiceByZapierOrderNumber(shopifyOrderTO.getOrder_number());
        if (saleInvoice != null) {
            if (saleInvoice.getStatus() != null && !PAID.equalsIgnoreCase(saleInvoice.getStatus().getCode())) {
                invoice.setID(saleInvoice.getObjectID());
                invoiceServiceLocal.updateSaleInvoice(invoice, false);
            }
            invoice.setID(saleInvoice.getObjectID());
            invoice.setInvoiceNumber(saleInvoice.getNumber());
        } else {
            SaveResult saveResult = invoiceServiceLocal.saveSaleInvoice(invoice, false);
            invoice.setID(saveResult.getId());
        }
        if ("refunded".equalsIgnoreCase(shopifyOrderTO.getStatus()) || "partially_refunded".equalsIgnoreCase(shopifyOrderTO.getStatus())) {
            NewInvoice creditNote = null;
            BigDecimal paymentTotal = BigDecimal.ZERO;

            if (saleInvoice != null && saleInvoice.getPayments() != null && !saleInvoice.getPayments().isEmpty()) {
                for (EdsInvoicePayment payment : saleInvoice.getPayments()) {
                    paymentTotal = paymentTotal.add(payment.getAmount());
                    invoiceServiceLocal.deletePayment(payment.getObjectID());
                }
            }

            if ("partially_refunded".equalsIgnoreCase(shopifyOrderTO.getStatus())) {
                paymentTotal = paymentTotal.subtract(invoice.getTotal());

                if (paymentTotal.compareTo(BigDecimal.ZERO) > 0) {
                    EdsInvoicingSettings invSettings = invoicingSettingsManager.getInvoiceSettings(userManager.getUser().getCompany());
                    EdsAccount defaultAccount = accountingManager.get(invSettings.getDefaultPaymentAccountId());
                    PaymentData invoicePayment = new PaymentData();
                    invoicePayment.setInvoiceID(invoice.getID());
                    invoicePayment.setPaymentAccount(defaultAccount.getAsSelectItem());
                    invoicePayment.setReferenceNumber(invoice.getReference());
                    invoicePayment.setDate(invoice.getInvoiceDate());
                    invoicePayment.setPaymentAmount(invoice.getTotal());
                    invoicePayment.setType(RECEIVABLE);
                    invoicePayment.setExchangeRate(new BigDecimal("1.00"));
                    invoicePayment.setValidateReference(false);
                    invoiceServiceLocal.savePayment(invoicePayment);
                }
                creditNote = invoice;
                {
                    creditNote.setShippingMethodID(null);
                    creditNote.setShippingPrice(null);
                    creditNote.setShippingMethod(null);
                }
                creditNote.setCreditedInvoiceID(invoice.getID());
                creditNote.setID(null);
                creditNote.setCreditNote(true);
                creditNote.setNumberData(invoiceServiceLocal.getCreditNoteNumber());
            } else {
                Params fp = new Params();
                fp.setExternalObjectID(invoice.getID());
                fp.setExternalFormID(COPY_INVOICE_TO_CREDITNOTE);
                fp.setType(RECEIVABLE);
                creditNote = invoiceServiceLocal.getAllCreditNoteData(fp);
                creditNote.setCreditedInvoiceID(invoice.getID());
            }
            creditNote.setInvoiceNumber(creditNote.getNumberData().getInvoiceNumber());
            creditNote.setStatusCode(APPROVE);
            invoiceServiceLocal.saveCreditNote(creditNote);
        } else if ("Paid".equalsIgnoreCase(shopifyOrderTO.getStatus())
                && invoice.getTotal() != null && invoice.getTotal().compareTo(BigDecimal.ZERO) != 0
                && (saleInvoice == null
                || (saleInvoice.getStatus() != null && !InvoiceStatusEnum.PAID.getStatus().equalsIgnoreCase(saleInvoice.getStatus().getCode())))) {

            EdsInvoicingSettings invSettings = invoicingSettingsManager.getInvoiceSettings(userManager.getUser().getCompany());
            EdsAccount defaultAccount = accountingManager.get(invSettings.getDefaultPaymentAccountId());
            if (defaultAccount != null) {
                PaymentData invoicePayment = new PaymentData();
                invoicePayment.setInvoiceID(invoice.getID());
                invoicePayment.setPaymentAccount(defaultAccount.getAsSelectItem());
                invoicePayment.setReferenceNumber(invoice.getReference());
                invoicePayment.setDate(invoice.getInvoiceDate());
                invoicePayment.setPaymentAmount(invoice.getTotal());
                invoicePayment.setType(RECEIVABLE);
                invoicePayment.setExchangeRate(new BigDecimal("1.00"));
                invoicePayment.setValidateReference(false);
                Integer result = invoiceServiceLocal.savePayment(invoicePayment);
                log.info("ZAPIER PAID ORDER PAYMENT {}", result);
            } else {
                log.error("Default account not found during the /sales_invoice_create_zapier");
            }
        } else if ("Canceled".equalsIgnoreCase(shopifyOrderTO.getStatus())) {
            invoiceServiceLocal.voidInvoice(invoice.getID(), invoice.getCancelDate() != null ? invoice.getCancelDate() : new DateNonConvertable());
        }
        /*if (saveResult.isInvoiceExist()) {
            throw new RestException("Invoice number is already used", "Invoice number " + shopifyOrderTO.getInvoice_number() + " is already used", CONFLICT, HttpStatus.CONFLICT);
        }*/
        return successResponse(new ResponseData());
    }

    private NewInvoice convertShopifyOrderToSalesInvoice(ZapierShopifyOrderTO shopifyOrderTO, SimpleDateFormat longDateTimezoneFormat) throws RestException {
        if (shopifyOrderTO != null) {
            NewInvoice invoice = new NewInvoice();
            invoice.setZapierordernumber(shopifyOrderTO.getOrder_number());

            /*if (StringUtils.isBlank(shopifyOrderTO.getCustomer_email())) {
                throw new RestException("Email address is required", "Email address is required", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
            }*/
            if (shopifyOrderTO.getOrder_number() == null || shopifyOrderTO.getOrder_number() <= 0) {
                throw new RestException("Order number is required", "Order number is required", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            Address billingAddress = new Address();
            billingAddress.setState(shopifyOrderTO.getBill_to_state_code());
            billingAddress.setCountry(shopifyOrderTO.getBill_to_country_code());
            billingAddress.setCountryCode(shopifyOrderTO.getBill_to_country_code());
            billingAddress.setZipCode(shopifyOrderTO.getBill_to_postcode());
            billingAddress.setCity(shopifyOrderTO.getBill_to_city());
            billingAddress.setAddress(shopifyOrderTO.getBill_to_address1());
            billingAddress.setAddressb(shopifyOrderTO.getBill_to_address2());
            billingAddress.setName("BILLING_ADDRESS");

            Address shippingAddress = new Address();
            shippingAddress.setState(shopifyOrderTO.getShip_to_state_code());
            shippingAddress.setCountry(shopifyOrderTO.getShip_to_country_code());
            shippingAddress.setCountryCode(shopifyOrderTO.getShip_to_country_code());
            shippingAddress.setZipCode(shopifyOrderTO.getShip_to_postcode());
            shippingAddress.setCity(shopifyOrderTO.getShip_to_city());
            shippingAddress.setAddress(shopifyOrderTO.getShip_to_address1());
            shippingAddress.setAddressb(shopifyOrderTO.getShip_to_address2());
            shippingAddress.setName("SHIPPING_ADDRESS");
            Integer savedAccountID = null;

            if (StringUtils.isNotBlank(shopifyOrderTO.getCustomer_firstname() + " " + shopifyOrderTO.getCustomer_lastname())
                    || StringUtils.isNotBlank(shopifyOrderTO.getCustomer_name())) {

                ContactListItem contact = new ContactListItem();
                contact.setContactType(CrmConstants.TYPE_CRM_CONTACT);

                Integer companyID = SecurityContext.getCompanyID();
                EdsCrmContact crmContact;
                if (StringUtils.isNotBlank(shopifyOrderTO.getCustomer_firstname() + " " + shopifyOrderTO.getCustomer_lastname())) {
                    crmContact = crmContactManager.getContactByEmail(shopifyOrderTO.getCustomer_email(), companyID);
                } else {
                    crmContact = crmContactManager.getContactByAccountName(shopifyOrderTO.getCustomer_name());
                }
                if (crmContact != null) {
                    //If contact exist
                    contact.setObjectId(crmContact.getObjectID());
                }


                ArrayList<Address> addresses = new ArrayList<>();
                addresses.add(shippingAddress);
                addresses.add(billingAddress);
                contact.setAddresses(addresses);

                //contact.setCheckForDuplicates(true);

                contact.setFirstName(shopifyOrderTO.getCustomer_firstname());
                contact.setLastName(shopifyOrderTO.getCustomer_lastname());
                contact.setContactName(shopifyOrderTO.getCustomer_name());

                if (StringUtils.isNotBlank(shopifyOrderTO.getCustomer_phone())) {
                    contact.setPrimaryPhone(shopifyOrderTO.getCustomer_phone());
                    HashMap<Integer, ArrayList<String>> phoneParam = new HashMap<>();
                    ArrayList<String> phones = new ArrayList<>();
                    phones.add(shopifyOrderTO.getCustomer_phone());
                    phoneParam.put(Constants.G_WORK, phones);
                    contact.setPhones(phoneParam);
                }

                if (StringUtils.isNotBlank(shopifyOrderTO.getCustomer_email())) {
                    if (!EMAIL_PATTERN.matcher(shopifyOrderTO.getCustomer_email()).matches()) {
                        throw new RestException("Invalid email address", "Invalid email address", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
                    }
                    contact.setPrimaryEmail(shopifyOrderTO.getCustomer_email());
                    HashMap<Integer, ArrayList<String>> emailParam = new HashMap<>();
                    ArrayList<String> emails = new ArrayList<>();
                    emails.add(shopifyOrderTO.getCustomer_email());
                    emailParam.put(Constants.G_WORK, emails);
                    contact.setEmails(emailParam);
                }
                ArrayList<ContactListItem> contacts = new ArrayList<>();
                contacts.add(contact);

                CrmAccountItem account = new CrmAccountItem();
                EdsCrmAccount edsCrmAccount;
                if (StringUtils.isNotBlank(shopifyOrderTO.getCustomer_firstname() + " " + shopifyOrderTO.getCustomer_lastname())) {
                    edsCrmAccount = crmAccountManager.getCrmAccountByName(shopifyOrderTO.getCustomer_firstname() + " " + shopifyOrderTO.getCustomer_lastname());
                    account.setName(shopifyOrderTO.getCustomer_firstname() + " " + shopifyOrderTO.getCustomer_lastname());
                } else {
                    edsCrmAccount = crmAccountManager.getCrmAccountByName(shopifyOrderTO.getCustomer_name());
                    account.setName(shopifyOrderTO.getCustomer_name());
                }
                if (edsCrmAccount != null) {
                    account.setObjectId(edsCrmAccount.getObjectID());
                    log.info("ZAPIER CRMACCOUNT FOUND {}", (shopifyOrderTO.getCustomer_firstname() + " " + shopifyOrderTO.getCustomer_lastname()));
                } else {
                    log.info("ZAPIER CRMACCOUNT NOT FOUND {}", (shopifyOrderTO.getCustomer_firstname() + " " + shopifyOrderTO.getCustomer_lastname()));
                }

                account.setMailAddresses(new Address[]{shippingAddress});
                account.setBillAddresses(new Address[]{billingAddress});
                if (StringUtils.isNotBlank(shopifyOrderTO.getCustomer_email())) {
                    account.setEmail(shopifyOrderTO.getCustomer_email());
                }
                if (StringUtils.isNotBlank(shopifyOrderTO.getCustomer_phone())) {
                    account.setPhone(shopifyOrderTO.getCustomer_phone());
                }

                ArrayList<SelectItem> accountTypes = new ArrayList<>();
                EdsReference customerType = referenceManager.findReference(EdsCrmAccount._CRM_ACCOUNT_TYPE, EdsCrmAccount.CUSTOMER);
                if (customerType != null) {
                    accountTypes.add(customerType.getAsSelectItem());
                }
                account.setAccountTypes(accountTypes.toArray(new SelectItem[0]));
                account.setContacts(contacts);

                //Save Company with Contact
                savedAccountID = crmServiceLocal.saveAccount(account, CrmAccountItem.CUSTOMER, null,
                        false, false, true, true, false);

            } else {
                //Default customer
                EdsCrmAccount edsCrmAccount = crmAccountManager.getCrmAccountByName("No Customer");
                if (edsCrmAccount != null) {
                    savedAccountID = edsCrmAccount.getObjectID();
                    log.info("ZAPIER CRMACCOUNT FOUND {}", "No Customer");
                } else {
                    log.info("ZAPIER CRMACCOUNT NOT FOUND {}", "No Customer");
                }
            }
            if (savedAccountID == null || savedAccountID <= 0) {
                throw new RestException("Error saving customer", "Error saving customer", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            invoice.setClientID(savedAccountID);

            List<EdsAddress> shippingAddressList = addressManager.getAddressesByEntityIdAndType(savedAccountID, EdsAddress.MAILING_ADDRESS, EdsAddress.ENTITY_TYPE_CRM_ACCOUNT);
            List<EdsAddress> billingAddressList = addressManager.getAddressesByEntityIdAndType(savedAccountID, EdsAddress.BILLING_ADDRESS, EdsAddress.ENTITY_TYPE_CRM_ACCOUNT);
            if (shippingAddressList != null && !shippingAddressList.isEmpty()) {
                invoice.setMailAddressID(shippingAddressList.get(0).getObjectID());
            }
            if (billingAddressList != null && !billingAddressList.isEmpty()) {
                invoice.setBillAddressID(billingAddressList.get(0).getObjectID());
            }

            if (StringUtils.isBlank(shopifyOrderTO.getCurrency_code())) {
                invoice.setCurrencyID(currencyServiceLocal.getBaseCurrency().getId());
            } else {
                EdsCurrency currency = currencyManager.getCurrency(shopifyOrderTO.getCurrency_code());
                if (currency != null) {
                    invoice.setCurrencyID(currency.getObjectID());
                }
            }
            invoice.setExchageRate(BigDecimal.ONE);
//            invoice.setNumberData(invoiceServiceLocal.getSaleInvoiceNumber());
//            invoice.setInvoiceNumber(invoice.getNumberData().getInvoiceNumber());
            invoice.setInvoiceNumber(shopifyOrderTO.getInvoice_number());
            invoice.setReference(shopifyOrderTO.getReference());
            invoice.setShippingPrice(shopifyOrderTO.getShipping_price());
            if (StringUtils.isNotBlank(shopifyOrderTO.getShipping_method())) {
                EdsShippingMethod shippingMethod = shippingMethodManager.getShippingMethodByName(shopifyOrderTO.getShipping_method());
                if (shippingMethod == null) {
                    shippingMethod = shippingMethodManager.getShippingMethodByName("CUSTOM SHIPPING");
                }
                if (shippingMethod != null) {
                    invoice.setShippingMethod(shippingMethod.getRPC());
                    invoice.setShippingMethodID(shippingMethod.getObjectID());
                }
            }
            try {
                invoice.setInvoiceDate(new DateNonConvertable(ServerUtils.getStartDate(longDateTimezoneFormat.parse(shopifyOrderTO.getInvoice_date()))));
                //same as invoice date
                invoice.setDueDate(new DateNonConvertable(ServerUtils.getEndDate(longDateTimezoneFormat.parse(shopifyOrderTO.getInvoice_date()))));
            } catch (ParseException e) {
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            try {
                if (StringUtils.isNotBlank(shopifyOrderTO.getCancel_at())) {
                    invoice.setCancelDate(new DateNonConvertable(ServerUtils.getStartDate(longDateTimezoneFormat.parse(shopifyOrderTO.getCancel_at()))));
                }
            } catch (ParseException e) {
                log.debug("cannot part cancelation date: {}", shopifyOrderTO.getCancel_at());
            }

            if (StringUtils.isNotBlank(shopifyOrderTO.getNotes())) {
                HistoryListItem notes = new HistoryListItem();
                notes.setComment(shopifyOrderTO.getNotes());
                invoice.setHistoryList(new HistoryListItem[]{notes});
            }


            //SET TAXes for reporting
            if (shopifyOrderTO.getTax_line_items() != null) {

                ArrayList<TotalTaxItem> totalTaxItems = new ArrayList<>();

                for (ZapierShopifyTaxItemTO taxItemTO : shopifyOrderTO.getTax_line_items()) {
                    if (StringUtils.isNotBlank(taxItemTO.getTax_name())) {
                        EdsVat tax = vatManager.getVatByName(taxItemTO.getTax_name());
                        if (tax != null) {
                            TotalTaxItem totalTaxItem = new TotalTaxItem();
                            totalTaxItem.setTaxAmount(taxItemTO.getTax_amount());
                            totalTaxItem.setTaxItem(tax.createTaxItem());
                            totalTaxItems.add(totalTaxItem);
                        } else {
                            log.info("ZAPIER TAX NOT FOUND \"{}\"", taxItemTO.getTax_name());
                        }
                    }
                }
                invoice.setTotalTaxItems(totalTaxItems.toArray(new TotalTaxItem[]{}));
                invoice.setTotalTaxes(shopifyOrderTO.getTax_total());
            }

            ArrayList<NewInvoiceItem> invoiceItems = new ArrayList<>();
            BigDecimal invoiceTotal = BigDecimal.ZERO;

            for (ZapierShopifyInvoiceItemTO zapierInvoiceItem : shopifyOrderTO.getLine_items()) {
                NewInvoiceItem invoiceItem = new NewInvoiceItem();
                EdsItem product = itemManager.getItemByName(zapierInvoiceItem.getItem_name());
                if (product != null) {
                    invoiceItem.setItemID(product.getObjectID());
                }
                invoiceItem.setItemName(zapierInvoiceItem.getItem_name());
                invoiceItem.setDescription(zapierInvoiceItem.getItem_description());
                //we must use default account
                //item.setEntityID(invoiceItem.getItem_sales_account().getSales_account_id());
                invoiceItem.setQuantity(zapierInvoiceItem.getItem_quantity());
                invoiceItem.setUnitPrice(zapierInvoiceItem.getItem_price());
                invoiceItem.setDiscountAmount(zapierInvoiceItem.getItem_discount_amount());

                //we must use default warehouse
                if (zapierInvoiceItem.getItem_warehouse() != null) {
                    invoiceItem.setWarehouse(new SelectItem(zapierInvoiceItem.getItem_warehouse().getWarehouse_id()));
                } else {
                    EdsWarehouse defaultWarehouse = warehouseManager.getDefaultWarehouse();
                    if (defaultWarehouse != null) {
                        invoiceItem.setWarehouse(defaultWarehouse.getAsSelectItem());
                    }
                }
                //Sum up total
                if (zapierInvoiceItem.getItem_quantity() != null && zapierInvoiceItem.getItem_price() != null) {
                    invoiceItem.setNet(zapierInvoiceItem.getItem_quantity().multiply(zapierInvoiceItem.getItem_price()));
                    invoiceTotal = invoiceTotal.add(invoiceItem.getNet());
                }
                AccountItem accountItem = invoiceServiceLocal.getDefaultAccountItem(null, RECEIVABLE);
                if (accountItem != null) {
                    invoiceItem.setAccountItem(accountItem);
                    invoiceItem.setAccountID(accountItem.getId());
                }
                invoiceItems.add(invoiceItem);
            }

            //calculate discounts and taxes
            final BigDecimal totalWithoutDiscount = invoiceTotal;
            invoiceItems.forEach(item -> {
                BigDecimal discountAmount = BigDecimal.ZERO;
                if (shopifyOrderTO.getDiscount_total() != null && shopifyOrderTO.getDiscount_total().doubleValue() > 0) {
                    BigDecimal discountPercent = (item.getQuantity().multiply(item.getUnitPrice())).multiply(new BigDecimal("100.00"))
                            .divide(totalWithoutDiscount, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                    discountAmount = shopifyOrderTO.getDiscount_total().divide(new BigDecimal("100.00"), RoundingMode.HALF_UP).multiply(discountPercent);
                    item.setDiscountAmount(discountAmount);
                }
                if (invoice.getTotalTaxItems() != null && invoice.getTotalTaxItems().length > 0) {
                    BigDecimal totalItemTaxAmount = BigDecimal.ZERO;
                    for (TotalTaxItem taxItem : invoice.getTotalTaxItems()) {
                        item.setTaxItem(taxItem.getTaxItem());
                        try {
                            BigDecimal itemTaxAmount = item.getUnitPrice().subtract(item.getDiscountAmount() != null ? item.getDiscountAmount() : BigDecimal.ZERO).
                                    multiply(taxItem.getTaxItem().getTaxPercent()).divide(
                                            new BigDecimal("100.00").add(taxItem.getTaxItem().getTaxPercent()),
                                            ServerUtils.getSystemCalculationScale(),
                                            RoundingMode.HALF_UP);
                            totalItemTaxAmount = totalItemTaxAmount.add(itemTaxAmount);
                        } catch (Exception e) {
                            log.error("", e);
                        }
                        item.setTaxItem(taxItem.getTaxItem());
                        item.setTaxAmount(totalItemTaxAmount);
                    }
                }
                //added this because pdf generation were failed after this
                item.setTotalAmount((item.getNet() != null ? item.getNet() : BigDecimal.ZERO).add(item.getTaxAmount() != null ? item.getTaxAmount() : BigDecimal.ZERO));

            });
            //end of calculate discounts and taxes


            invoice.setItems(invoiceItems.toArray(new NewInvoiceItem[]{}));

            invoice.setIntroduction(shopifyOrderTO.getIntroduction());
            //SET DISCOUNTS
            invoice.setTotalDiscount(shopifyOrderTO.getDiscount_total());
            invoice.setDiscountType(ONE_OFF_FIXED_AMOUNT);
            invoice.setDiscountAmount(shopifyOrderTO.getDiscount_total());


            //TotalTaxItem
            if (shopifyOrderTO.getTotal() == null) {
                invoice.setTotal(invoiceTotal);
                invoice.setSubtotal(invoiceTotal);
            } else {
                invoice.setTotal(shopifyOrderTO.getTotal());
                invoice.setSubtotal(shopifyOrderTO.getSubtotal());
            }
            invoice.setType(RECEIVABLE);

            //To create transactions
            invoice.setBookkeep(true);

            if (StringUtils.isBlank(shopifyOrderTO.getStatus())) {
                invoice.setStatusCode(InvoiceStatusEnum.DRAFT.getStatus());
            } else if ("Authorized".equalsIgnoreCase(shopifyOrderTO.getStatus())
                    || "PENDING".equalsIgnoreCase(shopifyOrderTO.getStatus())
                    || "Paid".equalsIgnoreCase(shopifyOrderTO.getStatus())
                    || "Partially Paid".equalsIgnoreCase(shopifyOrderTO.getStatus())
                    || "partially_refunded".equalsIgnoreCase(shopifyOrderTO.getStatus())) {
                invoice.setStatusCode(InvoiceStatusEnum.APPROVE.getStatus());
            } else if ("Refunded".equalsIgnoreCase(shopifyOrderTO.getStatus())) {
                invoice.setStatusCode(InvoiceStatusEnum.PAID.getStatus());
            } else if ("Voided".equalsIgnoreCase(shopifyOrderTO.getStatus()) || "Canceled".equalsIgnoreCase(shopifyOrderTO.getStatus()) || StringUtils.isBlank(shopifyOrderTO.getStatus())) {
                invoice.setStatusCode("REVERSED");
            }
            invoice.setForceSave(true);
            return invoice;

        }
        return null;
    }

    @Operation(summary = "Add New Credit Note (Refund)", description = "Add New Credit Note (Refund)")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false with error code "),
            @ApiResponse(responseCode = "404", description = "Invoice data is not found"),
            @ApiResponse(responseCode = "400", description = "Invoice Status is required or Invoice date is required or Invoice due date is required"),
            @ApiResponse(responseCode = "422", description = "Invalid Sales Invoice status or Invalid invoice date format"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/sales_invoice/credit_note", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object addCreditNote(@RequestBody SalesInvoiceAddTO creditNoteTO) throws RestException {

        if (creditNoteTO == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Invoice data is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (creditNoteTO.getInvoice_status() == null || StringUtils.isBlank(creditNoteTO.getInvoice_status().getStatus_code())) {
            throw new RestException("status is required", "status is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (InvoiceStatusEnum.getStatus(creditNoteTO.getInvoice_status().getStatus_code()) == null) {
            throw new RestException("Invalid status", "Invalid status " + creditNoteTO.getInvoice_status().getStatus_code(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        if (StringUtils.isBlank(creditNoteTO.getInvoice_date())) {
            throw new RestException("date not provided", "Invoice date is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(creditNoteTO.getInvoice_due_date())) {
            throw new RestException("due date is not provided", "Invoice due date is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        try {
            longDateTimezoneFormat.parse(creditNoteTO.getInvoice_date());
        } catch (ParseException e) {
            log.error("", e);
            throw new RestException("Invalid invoice date format", "Invalid invoice date format. Acceptable format for invoice is " + longDateTimezoneFormat.toPattern(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        try {
            longDateTimezoneFormat.parse(creditNoteTO.getInvoice_due_date());
        } catch (ParseException e) {
            log.error("", e);
            throw new RestException("Invalid invoice due date format", "Invalid invoice date format. Acceptable format for invoice is " + longDateTimezoneFormat.toPattern(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        if (creditNoteTO.getCustomer() == null || creditNoteTO.getCustomer().getCustomer_id() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Invoice customer is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (creditNoteTO.getInvoice_items() == null || creditNoteTO.getInvoice_items().isEmpty()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Invoice item is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        for (InvoiceItemTO invoiceItem : creditNoteTO.getInvoice_items()) {
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
            if (invoiceItem.getItem_sales_account() == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Invoice item sales account is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }
            if (invoiceItem.getItem_sales_account().getSales_account_id() == null || invoiceItem.getItem_sales_account().getSales_account_id() <= 0) {
                throw new RestException("Invoice item sales account is required", "Invoice item sales account is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            if (invoiceItem.getItem_warehouse() == null || invoiceItem.getItem_warehouse().getWarehouse_id() == null) {
                throw new RestException("Invoice item warehouse is required", "Invoice item warehouse is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }

        }

        NewInvoice creditNote = convertSalesInvoice(creditNoteTO, longDateTimezoneFormat);
        //Create CreditNote (Refund)
        SaveResult saveResult = invoiceServiceLocal.saveCreditNote(creditNote);

        if (saveResult.isInvoiceExist()) {
            throw new RestException("Invoice number is already used", "Invoice number " + creditNoteTO.getInvoice_number() + " is already used", CONFLICT, HttpStatus.CONFLICT);
        }
        return successResponse(new ResponseData());
    }

    private NewInvoice convertSalesInvoice(SalesInvoiceAddTO salesInvoice, SimpleDateFormat longDateTimezoneFormat) throws RestException {

        NewInvoice invoice = new NewInvoice();
        invoice.setBookkeep(true);
        invoice.setClientID(salesInvoice.getCustomer().getCustomer_id());
        List<EdsAddress> addressList = addressManager.getAddressesByEntityIdAndType(salesInvoice.getCustomer().getCustomer_id(), EdsAddress.BILLING_ADDRESS, EdsAddress.ENTITY_TYPE_CRM_ACCOUNT);
        if (salesInvoice.getCustomer().getBill_to_address() == null || salesInvoice.getCustomer().getBill_to_address().getAddress_id() == null || salesInvoice.getCustomer().getBill_to_address().getAddress_id() <= 0) {
            //try {
            Optional<EdsAddress> optional = addressList.stream().filter(EdsAddress::isPrimary).findFirst();
            if (optional.isPresent()) {
                invoice.setBillAddressID(optional.get().getObjectID());
            } else if (!addressList.isEmpty()) {
                invoice.setBillAddressID(addressList.get(0).getObjectID());
            }
        } else {
            Optional<EdsAddress> optional = addressList.stream().filter(billToAddressId -> billToAddressId.getObjectID().equals(salesInvoice.getCustomer().getBill_to_address().getAddress_id())).findFirst();
            if (optional.isPresent()) {
                invoice.setBillAddressID(salesInvoice.getCustomer().getBill_to_address().getAddress_id());
            } else {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Invoice customer primary bill to address with " + salesInvoice.getCustomer().getBill_to_address().getAddress_id() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }

        }
        if (salesInvoice.getCustomer().getShip_to_address() != null) {
            invoice.setMailAddressID(salesInvoice.getCustomer().getShip_to_address().getAddress_id());
        }
        if (salesInvoice.getInvoice_currency() == null || salesInvoice.getInvoice_currency().getCurrency_id() == null) {
            invoice.setCurrencyID(currencyServiceLocal.getBaseCurrency().getId());
        } else {
            invoice.setCurrencyID(salesInvoice.getInvoice_currency().getCurrency_id());
        }
        invoice.setExchageRate(salesInvoice.getExchange_rate());
        invoice.setInvoiceNumber(salesInvoice.getInvoice_number());
        invoice.setReference(salesInvoice.getReference());
        if (salesInvoice.getBank_account_id() != null) {
            SelectItem bankAccount = new SelectItem();
            bankAccount.setId(salesInvoice.getBank_account_id());
            invoice.setBankAccount(bankAccount);
        }
        try {
            invoice.setInvoiceDate(new DateNonConvertable(ServerUtils.getStartDate(longDateTimezoneFormat.parse(salesInvoice.getInvoice_date()))));
            invoice.setDueDate(new DateNonConvertable(ServerUtils.getEndDate(longDateTimezoneFormat.parse(salesInvoice.getInvoice_due_date()))));
        } catch (ParseException e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }


        //SET TAXes for reporting
        if (salesInvoice.getTax_line_items() != null) {

            ArrayList<TotalTaxItem> totalTaxItems = new ArrayList<>();

            for (ZapierShopifyTaxItemTO taxItemTO : salesInvoice.getTax_line_items()) {
                if (StringUtils.isNotBlank(taxItemTO.getTax_name())) {
                    EdsVat tax = vatManager.get(taxItemTO.getTax_id());
                    if (tax != null) {
                        TotalTaxItem totalTaxItem = new TotalTaxItem();
                        totalTaxItem.setTaxAmount(taxItemTO.getTax_amount());
                        totalTaxItem.setTaxItem(tax.createTaxItem());
                        totalTaxItems.add(totalTaxItem);
                    } else {
                        log.info("TAX NOT FOUND \"{}\"", taxItemTO.getTax_name());
                    }
                }
            }
            invoice.setTotalTaxItems(totalTaxItems.toArray(new TotalTaxItem[]{}));
            invoice.setTotalTaxes(salesInvoice.getTax_total());
        }


        ArrayList<NewInvoiceItem> invoiceItems = new ArrayList<>();

        BigDecimal invoiceTotal = BigDecimal.ZERO;
        if (salesInvoice.getInvoice_total() == null) {
            for (InvoiceItemTO invoiceItem : salesInvoice.getInvoice_items()) {
                NewInvoiceItem item = new NewInvoiceItem();
                item.setItemID(invoiceItem.getItem().getItem_id());
                item.setItemName(invoiceItem.getItem().getItem_name());
                item.setAccountID(invoiceItem.getItem_sales_account().getSales_account_id());
                item.setDescription(invoiceItem.getItem_description());
                item.setQuantity(invoiceItem.getItem_quantity());
                item.setUnitPrice(invoiceItem.getItem_price());
                item.setNet(invoiceItem.getItem_net_amount() == null ? (invoiceItem.getItem_quantity().multiply(invoiceItem.getItem_price())) : invoiceItem.getItem_net_amount());
                item.setWarehouse(new SelectItem(invoiceItem.getItem_warehouse().getWarehouse_id()));
                item.setBatchItems(invoiceItem.getBatchItems());
                if (invoiceItem.getTax_item() != null && invoiceItem.getTax_item().getTax_id() != null) {
                    EdsVat tax = vatManager.get(invoiceItem.getTax_item().getTax_id());
                    if (tax != null) {
                        item.setTaxItem(tax.createTaxItem());
                    }
                    item.setTaxAmount(invoiceItem.getTax_item().getTax_amount());
                }
                if (invoiceItem.getTotal_discount() != null && invoiceItem.getTotal_discount().compareTo(BigDecimal.ZERO) > 0) {
                    item.setDiscountItemStaticType(Constants.ONE_OFF_FIXED_AMOUNT);
                    item.setDiscountAmount(invoiceItem.getTotal_discount());
                }
                invoiceTotal = invoiceTotal.add(invoiceItem.getItem_quantity().multiply(invoiceItem.getItem_price()));
                invoiceItems.add(item);
            }
        } else {
            for (InvoiceItemTO invoiceItem : salesInvoice.getInvoice_items()) {
                NewInvoiceItem item = new NewInvoiceItem();
                item.setItemID(invoiceItem.getItem().getItem_id());
                item.setItemName(invoiceItem.getItem().getItem_name());
                item.setDescription(invoiceItem.getItem_description());
                item.setAccountID(invoiceItem.getItem_sales_account().getSales_account_id());
                item.setQuantity(invoiceItem.getItem_quantity());
                item.setUnitPrice(invoiceItem.getItem_price());
                item.setNet(invoiceItem.getItem_net_amount());
                item.setWarehouse(new SelectItem(invoiceItem.getItem_warehouse().getWarehouse_id()));
                item.setBatchItems(invoiceItem.getBatchItems());

                if (invoiceItem.getTotal_discount() != null && invoiceItem.getTotal_discount().compareTo(BigDecimal.ZERO) > 0) {
                    item.setDiscountItemStaticType(Constants.ONE_OFF_FIXED_AMOUNT);
                    item.setDiscountAmount(invoiceItem.getTotal_discount());
                }

                if (invoiceItem.getTax_item() != null && invoiceItem.getTax_item().getTax_id() != null) {
                    EdsVat tax = vatManager.get(invoiceItem.getTax_item().getTax_id());
                    if (tax != null) {
                        item.setTaxItem(tax.createTaxItem());
                    }
                    item.setTaxAmount(invoiceItem.getTax_item().getTax_amount());
                }
                invoiceItems.add(item);
            }
        }

        //calculate discounts and taxes
        final BigDecimal totalWithoutDiscount = invoiceTotal;
        invoiceItems.forEach(item -> {
            BigDecimal discountAmount = BigDecimal.ZERO;
            if (salesInvoice.getDiscount_total() != null && salesInvoice.getDiscount_total().doubleValue() > 0) {
                BigDecimal discountPercent = (item.getQuantity().multiply(item.getUnitPrice())).multiply(new BigDecimal("100.00"))
                        .divide(totalWithoutDiscount, ServerUtils.getSystemCalculationScale(), RoundingMode.HALF_UP);
                discountAmount = salesInvoice.getDiscount_total().divide(new BigDecimal("100.00"), RoundingMode.HALF_UP).multiply(discountPercent);
                item.setDiscountAmount(discountAmount);
            }

            //added this because pdf generation were failed after this
            item.setTotalAmount((item.getNet() != null ? item.getNet() : BigDecimal.ZERO).add(item.getTaxAmount() != null ? item.getTaxAmount() : BigDecimal.ZERO));

        });
        //end of calculate discounts and taxes

        invoice.setItems(invoiceItems.toArray(new NewInvoiceItem[]{}));

        //Start Of Custom Fields
        invoice.setCustomFieldItems(convertCustomFields(salesInvoice.getCustom_fields(), Collections.emptyMap()));
        //End Of Custom Fields


        invoice.setIntroduction(salesInvoice.getIntroduction());
        if (salesInvoice.getInvoice_total() == null) {
            invoice.setTotal(invoiceTotal);
            invoice.setSubtotal(invoiceTotal);
        } else {
            invoice.setTotal(salesInvoice.getInvoice_total());

            if (salesInvoice.getInvoice_subtotal() != null && salesInvoice.getInvoice_subtotal().compareTo(BigDecimal.ZERO) > 0) {
                invoice.setSubtotal(salesInvoice.getInvoice_subtotal());
            } else {
                invoice.setSubtotal(invoice.getTotalTaxes() != null ? salesInvoice.getInvoice_total().subtract(invoice.getTotalTaxes()) : salesInvoice.getInvoice_total());
            }
        }
        if (salesInvoice.getTax_calculation_type() != null && TaxTypeEnum.valueOf(salesInvoice.getTax_calculation_type()) != null) {
            invoice.setTaxCalculationType(TaxTypeEnum.valueOf(salesInvoice.getTax_calculation_type()).getId());
        }
        invoice.setType(RECEIVABLE);
        invoice.setStatusCode(InvoiceStatusEnum.getStatus(salesInvoice.getInvoice_status().getStatus_code()));
        invoice.setForceSave(true);
        return invoice;
    }

    private NewInvoice convertSalesInvoice(ZapierInvoiceItemTO salesInvoice, SimpleDateFormat longDateTimezoneFormat) throws RestException {

        NewInvoice invoice = new NewInvoice();
        /*invoice.setClientID(salesInvoice.getCustomer().getCustomer_id());
        List<EdsAddress> addressList = addressManager.getAddressesByEntityIdAndType(salesInvoice.getCustomer().getCustomer_id(), EdsAddress.BILLING_ADDRESS, EdsAddress.ENTITY_TYPE_CRM_ACCOUNT);
        if (salesInvoice.getCustomer().getBill_to_address() == null || salesInvoice.getCustomer().getBill_to_address().getAddress_id() == null || salesInvoice.getCustomer().getBill_to_address().getAddress_id() <= 0) {
            //try {
                Optional<EdsAddress> optional = addressList.stream().filter(EdsAddress::isPrimary).findFirst();
                if (optional.isPresent()) {
                    invoice.setBillAddressID(optional.get().getObjectID());
                } else {
                    throw new RestException(GENERAL_ERROR_MESSAGE, "Invoice customer primary bill to address is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
                }
//            } catch (Exception e) {
//                log.error("", e);
//                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
//            }
        } else {
            Optional<EdsAddress> optional = addressList.stream().filter(billToAddressId -> billToAddressId.getObjectID().equals(salesInvoice.getCustomer().getBill_to_address().getAddress_id())).findFirst();
            if (optional.isPresent()) {
                invoice.setBillAddressID(salesInvoice.getCustomer().getBill_to_address().getAddress_id());
            } else {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Invoice customer primary bill to address with " + salesInvoice.getCustomer().getBill_to_address().getAddress_id() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
            }

        }
        if (salesInvoice.getCustomer().getShip_to_address() != null) {
            invoice.setMailAddressID(salesInvoice.getCustomer().getShip_to_address().getAddress_id());
        }
        if (salesInvoice.getInvoice_currency() == null || salesInvoice.getInvoice_currency().getCurrency_id() == null) {
            invoice.setCurrencyID(currencyServiceLocal.getBaseCurrency().getId());
        } else {
            invoice.setCurrencyID(salesInvoice.getInvoice_currency().getCurrency_id());
        }
        invoice.setExchageRate(salesInvoice.getExchange_rate());
        invoice.setInvoiceNumber(salesInvoice.getInvoice_number());
        invoice.setReference(salesInvoice.getReference());
        try {
            invoice.setInvoiceDate(new DateNonConvertable(ServerUtils.getStartDate(longDateTimezoneFormat.parse(salesInvoice.getInvoice_date()))));
            invoice.setDueDate(new DateNonConvertable(ServerUtils.getEndDate(longDateTimezoneFormat.parse(salesInvoice.getInvoice_due_date()))));
        } catch (ParseException e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ArrayList<NewInvoiceItem> invoiceItems = new ArrayList<>();

        BigDecimal invoiceTotal = BigDecimal.ZERO;
        if (salesInvoice.getInvoice_total() == null) {
            for (InvoiceItemTO invoiceItem : salesInvoice.getInvoice_items()) {
                NewInvoiceItem item = new NewInvoiceItem();
                item.setItemID(invoiceItem.getProductItem().getItem_id());
                item.setItemName(invoiceItem.getProductItem().getItem_name());
                item.setEntityID(invoiceItem.getItem_sales_account().getSales_account_id());
                item.setQuantity(invoiceItem.getItem_quantity());
                item.setUnitPrice(invoiceItem.getItem_price());
                item.setNet(invoiceItem.getItem_net_amount() == null ? (invoiceItem.getItem_quantity().multiply(invoiceItem.getItem_price())) : invoiceItem.getItem_net_amount());
                item.setWarehouse(new SelectItem(invoiceItem.getItem_warehouse().getWarehouse_id()));
                invoiceTotal = invoiceTotal.add(invoiceItem.getItem_quantity().multiply(invoiceItem.getItem_price()));
                invoiceItems.add(item);
            }
        } else {
            for (InvoiceItemTO invoiceItem : salesInvoice.getInvoice_items()) {
                NewInvoiceItem item = new NewInvoiceItem();
                item.setItemID(invoiceItem.getProductItem().getItem_id());
                item.setItemName(invoiceItem.getProductItem().getItem_name());
                item.setEntityID(invoiceItem.getItem_sales_account().getSales_account_id());
                item.setQuantity(invoiceItem.getItem_quantity());
                item.setUnitPrice(invoiceItem.getItem_price());
                item.setNet(invoiceItem.getItem_net_amount());
                item.setWarehouse(new SelectItem(invoiceItem.getItem_warehouse().getWarehouse_id()));
                invoiceItems.add(item);
            }
        }
        invoice.setItems(invoiceItems.toArray(new NewInvoiceItem[]{}));

        invoice.setIntroduction(salesInvoice.getIntroduction());
        if (salesInvoice.getInvoice_total() == null) {
            invoice.setTotal(invoiceTotal);
            invoice.setSubtotal(invoiceTotal);
        } else {
            invoice.setTotal(salesInvoice.getInvoice_total());
            invoice.setSubtotal(salesInvoice.getInvoice_total());
        }
        invoice.setType(Constants.RECEIVABLE);
        invoice.setStatusCode(InvoiceStatusEnum.getStatus(salesInvoice.getInvoice_status().getStatus_code()));
        invoice.setForceSave(true);*/
        return invoice;
    }

    @Operation(summary = "Get Sales Invoice Customer List", description = "Retrieves All Available Sales Invoice Customers by search_text")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have customer_id and customer_name "),
            @ApiResponse(responseCode = "400", description = "Start point and limit is required"),
            @ApiResponse(responseCode = "422", description = "Start point and limit can not be zero at the same time"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/sales_invoice_customers", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getCustomers(@RequestBody RequestListSearchData requestListSearchData) throws RestException {
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
        filterParameter.setAccountType(CrmConstants.CUSTOMER);
        ArrayList<SelectItem> customers;
        try {
            customers = crmServiceLocal.getLookUpItems(filterParameter, CrmConstants.CRM_ACCOUNT_ID).getList();
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ArrayList<SupplierTO> result = new ArrayList<>();
        for (SelectItem item : customers) {
            SupplierTO customer = new SupplierTO();
            customer.setCustomer_id(item.getId());
            customer.setCustomer_name(item.getName());
            result.add(customer);
        }
        ResponseListData<SupplierTO> responseListData = new ResponseListData<>();
        responseListData.setList(result);
        return successResponse(responseListData);
    }

    @Operation(summary = "Get Customer Bill to Addresses List", description = "Retrieves customer's bill to addresses based on customer_id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have address_id and address_name "),
            @ApiResponse(responseCode = "400", description = "customer_id is not provided"),
            @ApiResponse(responseCode = "404", description = "Customer with provided customer_id is not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/{customer_id}/bill_to_addresses", method = RequestMethod.GET)
    public Object getCustomerBillToAddresses(@PathVariable(value = "customer_id") Integer customer_id) throws RestException {
        return getCustomerAddressList(customer_id, true);
    }

    @Operation(summary = "Get Customer Ship to Addresses List", description = "Retrieves customer's ship to addresses based on customer_id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have address_id and address_name "),
            @ApiResponse(responseCode = "400", description = "Customer id is required"),
            @ApiResponse(responseCode = "404", description = "Customer with provided customer_id is not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/{customer_id}/ship_to_addresses", method = RequestMethod.GET)
    public Object getCustomerShipToAddresses(@PathVariable(value = "customer_id") Integer customer_id) throws RestException {
        return getCustomerAddressList(customer_id, false);
    }

    private Object getCustomerAddressList(Integer customerID, boolean isBillToAddress) throws RestException {
        if (customerID == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Customer id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        EdsCrmAccount edsCrmAccount = crmAccountManager.get(customerID);
        if (edsCrmAccount == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Customer with id " + customerID + " not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        List<EdsAddress> addressList;
        try {
            addressList = addressManager.getAddressesByEntityIdAndType(customerID, isBillToAddress ? EdsAddress.BILLING_ADDRESS : EdsAddress.MAILING_ADDRESS, EdsAddress.ENTITY_TYPE_CRM_ACCOUNT);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ArrayList<AddressTO> resultList = new ArrayList<>();
        if (addressList != null && addressList.size() > 0) {
            for (EdsAddress address : addressList) {
                AddressTO addressTO = new AddressTO();
                addressTO.setAddress_id(address.getObjectID());
                addressTO.setAddress_name(address.getName());
                resultList.add(addressTO);
            }
        }

        ResponseListData<AddressTO> responseListData = new ResponseListData<>();
        responseListData.setList(resultList);
        return successResponse(responseListData);
    }

    @Operation(summary = "Get Sales Invoice Items", description = "Retrieves Sales Invoice Items based on provided text")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have item_id, item_name, item_name"),
            @ApiResponse(responseCode = "400", description = "Start point and limit is required"),
            @ApiResponse(responseCode = "422", description = "Start point and limit can not be zero at the same time")})
    @RequestMapping(value = "/sales_invoice_items", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getItems(@RequestBody RequestListSearchData requestListSearchData) throws RestException {
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
        filterParameter.setLookUp(true);
        filterParameter.setInvoiceType(RECEIVABLE);
        filterParameter.setStart(requestListSearchData.getStart());
        filterParameter.setLimit(requestListSearchData.getLimit());
        filterParameter.setSearchKey(requestListSearchData.getSearch_text());

        ProductSelectItem[] items = productServiceLocal.getCompanyProductsByType(filterParameter);
        ArrayList<ItemTO> resultList = new ArrayList<>();
        for (ProductSelectItem invoiceItem : items) {
            ItemTO item = new ItemTO();
            item.setItem_id(invoiceItem.getId());
            item.setItem_name(invoiceItem.getName());
            if (invoiceItem.getName() != null && invoiceItem.getName().contains("->")) {
                String[] s = invoiceItem.getName().split("->");
                if (s.length > 1) {
                    item.setItem_number(s[0].trim());
                    item.setItem_name(s[1].trim());
                } else {
                    item.setItem_name(s[0].trim());
                }
            }
            resultList.add(item);
        }

        ResponseListData<ItemTO> responseListData = new ResponseListData<>();
        responseListData.setList(resultList);
        return successResponse(responseListData);
    }

    @Operation(summary = "Sales Invoice Batch Import", description = "Imports Sales Import Batch to the current user ")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have imported file details "),
            @ApiResponse(responseCode = "400", description = "File required"),
            @ApiResponse(responseCode = "404", description = "Sales invoice root folder is not found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/sales_invoice_batch_import", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public Object fileUpload(@RequestParam(value = "file") MultipartFile file) throws RestException {
        if (file == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "File required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        EdsFolder salesInvoiceFolder = folderManager.getFolderByFolderType(Constants.F_SALE_INV);
        if (salesInvoiceFolder == null || salesInvoiceFolder.getObjectID() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Sales invoice root folder is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        FolderResource folderResource = documentsServiceLocal.getFolderResource(salesInvoiceFolder.getObjectID());
        if (folderResource == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Sales invoice folder with id " + salesInvoiceFolder.getObjectID() + " not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        String originalFileName = file.getOriginalFilename().replace("%20", " ");

        if (GwtUploadServlet.realPath == null) {
            GwtUploadServlet.realPath = servletContext.getRealPath("uploads") + "/";
        }

        String fileName = UUID.uuid() + "_upld_" + originalFileName;
        try {
            String filenameEncode = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
            String url = GwtUploadServlet.realPath + filenameEncode;
            File newFile = new File(url);
            newFile.getParentFile().mkdirs();
            FileOutputStream os = new FileOutputStream(newFile);
            IOUtils.copy(file.getInputStream(), os);
            file.getInputStream().close();
            os.flush();
            os.close();
        } catch (IOException e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ArrayList<FileResource> files = new ArrayList<>();
        FileResource fileResource = new FileResource();
        fileResource.setName(fileName);
        fileResource.setPath(GwtUploadServlet.realPath + fileName);
        fileResource.setUploadType(EdsContextParams.getUploadType());
        files.add(fileResource);


        ArrayList<FileResource> result = documentsServiceLocal.uploadAllFiles(files, folderResource, "");
        if (result.size() > 1) {
            FileResource f = result.get(1);
            FileTO fileTO = new FileTO();
            fileTO.setFile_id(f.getObjectId());
            fileTO.setFile_name(f.getFileName());
            fileTO.setFile_url(f.getDownloadUrl());
            fileTO.setFile_content_type(f.getContentType());
            fileTO.setFile_size(f.getContentLength());
            fileTO.setIs_folder(false);
            fileTO.setFolder_id(f.getFolderId());
            invoiceServiceLocal.batchImportInvoicesFromFile(f.getObjectId());
            return successResponse(fileTO);
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Error occurred while uploading file", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get Sales Invoice Accounts", description = "Retrieves Sales Invoice Accounts based on provided text")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of account_id and account_name"),
            @ApiResponse(responseCode = "400", description = "Start point and limit is required"),
            @ApiResponse(responseCode = "422", description = "Start point and limit can not be zero at the same time"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/sales_invoice_accounts", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getAccounts(@RequestBody RequestListSearchData requestListSearchData) throws RestException {
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
        filterParameter.setLookUp(true);
        ArrayList<String> accountTypes = new ArrayList<>();
        accountTypes.add(Constants.REVENUE);
        accountTypes.add(Constants.EQUITY);
        accountTypes.add(Constants.LIABILITIES);
        filterParameter.setAccountTypes(accountTypes);

        List<EdsAccount> accounts;
        try {
            accounts = accountingManager.getAccountsForInvoice(filterParameter, false);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ArrayList<AccountTO> result = new ArrayList<>();
        for (EdsAccount account : accounts) {
            AccountTO salesAccount = new AccountTO();
            salesAccount.setAccount_id(account.getObjectID());
            salesAccount.setAccount_name(account.getName());
            result.add(salesAccount);
        }

        ResponseListData<AccountTO> responseListData = new ResponseListData<>();
        responseListData.setList(result);
        return successResponse(responseListData);
    }

    @Operation(summary = "Get Bank Accounts", description = "Retrieves Bank Accounts based on provided text")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of account_id and account_name"),
            @ApiResponse(responseCode = "400", description = "Start point and limit is required"),
            @ApiResponse(responseCode = "422", description = "Start point and limit can not be zero at the same time"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/bank_accounts", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getBankAccounts(@RequestBody RequestListSearchData requestListSearchData) throws RestException {
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
        filterParameter.setLookUp(true);

        List<EdsBankAccount> bankAccounts;
        try {
            bankAccounts = accountingManager.getBankAccountList(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ArrayList<AccountTO> result = new ArrayList<>();
        for (EdsBankAccount bankAccount : bankAccounts) {
            AccountTO salesAccount = new AccountTO();
            salesAccount.setAccount_id(bankAccount.getAccount().getObjectID());
            salesAccount.setAccount_name(bankAccount.getAccount().getName());
            result.add(salesAccount);
        }

        ResponseListData<AccountTO> responseListData = new ResponseListData<>();
        responseListData.setList(result);
        return successResponse(responseListData);
    }

    @Operation(summary = "Get Sales Invoice Warehouses", description = "Retrieves all Sale Invoice Warehouses based on provided text")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have warehouse_id, warehouse_name"),
            @ApiResponse(responseCode = "400", description = "Start point and limit is required"),
            @ApiResponse(responseCode = "422", description = "Start point and limit can not be zero at the same time"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/sales_invoice_warehouses", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getWarehouses(@RequestBody RequestListSearchData requestListSearchData) throws RestException {
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

        SelectItem[] warehouseList;
        try {
            warehouseList = accountingServiceLocal.getWarehousesForLookUp(filterParameter);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ArrayList<WarehouseTO> result = new ArrayList<>();
        for (SelectItem item : warehouseList) {
            WarehouseTO warehouse = new WarehouseTO();
            warehouse.setWarehouse_id(item.getId());
            warehouse.setWarehouse_name(item.getName());
            result.add(warehouse);
        }

        ResponseListData<WarehouseTO> responseListData = new ResponseListData<>();
        responseListData.setList(result);
        return successResponse(responseListData);
    }

    @Operation(summary = "Receive Sales Invoice Payment", description = "Receive Sales Invoice Payment")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have true or false "),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")})
    @RequestMapping(value = "/sales_invoice/payment/{invoice_id}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
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
        paymentData[0].setType(RECEIVABLE);

//        receivePaymentData.setPayments(paymentData);

        Integer paymentId = invoiceServiceLocal.savePayment(paymentData[0]);
        if (paymentId != null && paymentId > 0) {
            return successResponse(new ResponseData());
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, GENERAL_ERROR_MESSAGE, INVALID, HttpStatus.CONFLICT);
        }
    }
}
