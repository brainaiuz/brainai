package com.edatasite.workforce.rest.v3.release10.accounting;

import com.edatasite.workforce.core.solr.component.SaleInvoiceSolrComponent;
import com.edatasite.workforce.gwt.accounting.client.rpc.AccountingService;
import com.edatasite.workforce.gwt.accounting.client.rpc.FinancialSettingsItem;
import com.edatasite.workforce.gwt.core.client.rpc.CurrencyService;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FolderManager;
import com.edatasite.workforce.gwt.documents.server.app.DocumentsServiceLocal;
import com.edatasite.workforce.gwt.invoice.client.rpc.BatchPaymentResult;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.FinancialSettingsDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.IdCustomFieldsDTO;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.fai.FaiWebhookRequest;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.sales.invoice.SalesInvoiceDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.sales.invoice.SalesInvoicePaymentDto;
import com.edatasite.workforce.rest.v3.release10.accounting.service.ApiSalesInvoiceService;
import com.edatasite.workforce.rest.v3.release10.core.helper.ListingFilterHelperV3;
import com.edatasite.workforce.rest.v3.release10.core.to.ListParamsDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Tag(name = "Sales Invoice")
@RestController
@RequestMapping(path = "/sales-invoice")
public class ApiSalesInvoiceControllerV3 {
    private static final Logger log = LoggerFactory.getLogger(ApiSalesInvoiceControllerV3.class);

    private final ApiSalesInvoiceService apiSalesInvoiceService;
    private final AccountingService accountingService;
    private final CurrencyService currecyService;
    private final InvoiceManager invoiceManager;
    private final AttachmentUtilsManager attachmentUtilsManager;
    private final FolderManager folderManager;
    private final DocumentsServiceLocal documentsServiceLocal;
    private final ObjectMapper objectMapper;
    private final InvoiceService invoiceService;
    private final SaleInvoiceSolrComponent saleInvoiceSolrComponent;

    @Autowired
    public ApiSalesInvoiceControllerV3(ApiSalesInvoiceService apiSalesInvoiceService,
                                       AccountingService accountingService,
                                       CurrencyService currecyService,
                                       InvoiceManager invoiceManager,
                                       AttachmentUtilsManager attachmentUtilsManager,
                                       FolderManager folderManager,
                                       DocumentsServiceLocal documentsServiceLocal,
                                       ObjectMapper objectMapper,
                                       InvoiceService invoiceService,
                                       SaleInvoiceSolrComponent saleInvoiceSolrComponent) {
        this.apiSalesInvoiceService = apiSalesInvoiceService;
        this.accountingService = accountingService;
        this.currecyService = currecyService;
        this.invoiceManager = invoiceManager;
        this.attachmentUtilsManager = attachmentUtilsManager;
        this.folderManager = folderManager;
        this.documentsServiceLocal = documentsServiceLocal;
        this.objectMapper = objectMapper;
        this.invoiceService = invoiceService;
        this.saleInvoiceSolrComponent = saleInvoiceSolrComponent;
    }

    @PatchMapping(path = "/custom-fields", consumes = MediaType.APPLICATION_JSON_VALUE, headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
    public ResultTO<?> update(@Valid @RequestBody IdCustomFieldsDTO request) {
        apiSalesInvoiceService.updateInvoiceCustomFields(request);
        return ResultTO.success();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
    public ResultTO<Integer> create(@RequestBody SalesInvoiceDto request) {
        Integer invoice = apiSalesInvoiceService.createInvoice(request);
        return ResultTO.success(invoice);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE, headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
    public ResultTO<SalesInvoiceDto> read(@PathVariable("id") Integer id) {
        SalesInvoiceDto invoice = apiSalesInvoiceService.getInvoiceById(id);
        return ResultTO.success(invoice);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
    public ResultTO<Boolean> update(@PathVariable("id") Integer id, @Valid @RequestBody SalesInvoiceDto request) {
        apiSalesInvoiceService.updateInvoice(id, request);
        return ResultTO.success(true);
    }

    @PostMapping(path = "/list", produces = MediaType.APPLICATION_JSON_VALUE, headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
    public ResultTO<List<SalesInvoiceDto>> getAllInvoices(@RequestBody ListParamsDTO listingParams,
                                                          @RequestParam(required = false) Boolean includeProduct) {
        ListingFilterParameter fp = ListingFilterHelperV3.createListingFilter(listingParams, ListPanelType.SaleInvoiceListPanel);
        List<SalesInvoiceDto> invoices = apiSalesInvoiceService.getAllInvoices(fp, includeProduct);
        return ResultTO.success(invoices);
    }

    @GetMapping(path = "/approvers", produces = MediaType.APPLICATION_JSON_VALUE, headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
    public ResultTO<List<ApproverItemMini>> getApprovers(@RequestParam Integer objectId) {
        List<ApproverItemMini> approvers = apiSalesInvoiceService.getApprover(null);
        return ResultTO.success(approvers);
    }
//
//    @DeleteMapping(path = "/{id}")
//    public ResultTO<Void> delete(@PathVariable("id") Integer id) {
//        apiSalesInvoiceService.deleteInvoice(id);
//        return ResultTO.success();
//    }

    @PostMapping(path = "/pay", produces = MediaType.APPLICATION_JSON_VALUE, headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
    public ResultTO<BatchPaymentResult> paySalesInvoice(@RequestBody SalesInvoicePaymentDto request) {
        log.info("REST request to pay sales invoice: {}", request);
        var batchPaymentResult = apiSalesInvoiceService.paySalesInvoice(request);
        return ResultTO.success(batchPaymentResult);
    }

    @GetMapping(path = "/active", produces = MediaType.APPLICATION_JSON_VALUE, headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
    public ResultTO<List<SalesInvoiceDto>> getActiveInvoice(@RequestParam(required = false) Integer crmAccountId) {
        log.info("REST request to get active invoice");
        var activeInvoice = apiSalesInvoiceService.getActiveInvoice(crmAccountId);
        return ResultTO.success(activeInvoice);
    }

    @PostMapping(path = "/financial-settings", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE, headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH})
    public ResultTO<Boolean> changeCurrency(@RequestBody FinancialSettingsDto request) {
        FinancialSettingsItem settings = accountingService.getCompanyFinancialSettings();
        boolean existsCurrency = Optional.ofNullable(request.getCurrencyId())
                .map(currecyService::getCurrency)
                .isPresent();
        if (existsCurrency && !settings.isHasTransaction()) {
            settings.setCurrencyId(request.getCurrencyId());
        }
        accountingService.saveFinancialSettings(settings);
        return ResultTO.success(true);
    }

    @PostMapping(path = "/fai-event", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> handleFaiEvent(@RequestParam("companyId") Integer companyId,
                                            @RequestParam("database") String database,
                                            @RequestBody String requestJson) throws JsonProcessingException {
        log.info("REST request to handle FAI event: {}", requestJson);
        ServerSecurityContext.getInstance().setCompanyId(companyId);
        ServerSecurityContext.getInstance().setDatabase(database);
        FaiWebhookRequest request = objectMapper.readValue(requestJson, FaiWebhookRequest.class);
        return apiSalesInvoiceService.updateInvoiceZatcaStatus(request,companyId,database);
    }
}
