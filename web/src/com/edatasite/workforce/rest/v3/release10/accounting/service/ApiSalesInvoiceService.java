package com.edatasite.workforce.rest.v3.release10.accounting.service;

import com.edatasite.workforce.core.domain.CrmAccountInvoiceTO;
import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.accounting.EdsFaiEvents;
import com.edatasite.workforce.core.domain.accounting.EdsInvoice;
import com.edatasite.workforce.core.domain.accounting.EdsInvoiceItem;
import com.edatasite.workforce.core.domain.accounting.EdsSaleInvoice;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.documents.EdsFolder;
import com.edatasite.workforce.core.solr.component.SaleInvoiceSolrComponent;
import com.edatasite.workforce.gwt.accounting.client.rpc.NewProduct;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.ApprovalListResult;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItemMini;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.ClientContactManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceItemManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.accounting.FaiEventsManager;
import com.edatasite.workforce.gwt.core.server.db.documents.AttachmentUtilsManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FolderManager;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.server.app.DocumentsServiceLocal;
import com.edatasite.workforce.gwt.invoice.client.rpc.BatchPaymentResult;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoice;
import com.edatasite.workforce.gwt.invoice.client.rpc.NewInvoiceItem;
import com.edatasite.workforce.gwt.invoice.client.rpc.PaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.ReceivePaymentData;
import com.edatasite.workforce.gwt.invoice.client.rpc.SaveResult;
import com.edatasite.workforce.gwt.invoice.client.rpc.service.InvoiceService;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.edatasite.workforce.rest.v2.release10.core.to.base.IdNameTO;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.ApproverTO;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.IdCustomFieldsDTO;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.fai.FaiWebhookPayload;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.fai.FaiWebhookRequest;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.fai.FaiZatcaResponse;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.sales.invoice.SalesInvoiceDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.sales.invoice.SalesInvoicePaymentDto;
import com.edatasite.workforce.rest.v3.release10.core.to.CustomFieldDto;
import com.edatasite.workforce.rest.v3.release10.core.to.IdCode;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.text.WordUtils;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.F_SALE_INV;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.RECEIVABLE;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.SALE_INVOICE;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.NOT_FOUND;

@Service
public class ApiSalesInvoiceService {
    private static final Logger log = LoggerFactory.getLogger(ApiSalesInvoiceService.class);

    private final InvoiceServiceLocal invoiceServiceLocal;
    private final CommonService commonService;
    private final InvoiceManager invoiceManager;
    private final InvoiceService invoiceService;
    private final AllInOneService allInOneService;
    private final ProductService productService;
    private final InvoiceItemManager invoiceItemManager;
    private final ClientContactManager clientContactManager;
    private final SaleInvoiceSolrComponent saleInvoiceSolrComponent;
    private final FolderManager folderManager;
    private final DocumentsServiceLocal documentsServiceLocal;
    private final AttachmentUtilsManager attachmentUtilsManager;
    private final FaiEventsManager faiEventsManager;

    @Autowired
    public ApiSalesInvoiceService(InvoiceService invoiceService,
                                  InvoiceServiceLocal invoiceServiceLocal,
                                  CommonService commonService,
                                  InvoiceManager invoiceManager,
                                  InvoiceService invoiceService1,
                                  AllInOneService allInOneService,
                                  ProductService productService,
                                  InvoiceItemManager invoiceItemManager,
                                  ClientContactManager clientContactManager,
                                  SaleInvoiceSolrComponent saleInvoiceSolrComponent,
                                  FolderManager folderManager,
                                  DocumentsServiceLocal documentsServiceLocal,
                                  AttachmentUtilsManager attachmentUtilsManager,
                                  FaiEventsManager faiEventsManager) {
        this.invoiceServiceLocal = invoiceServiceLocal;
        this.commonService = commonService;
        this.invoiceManager = invoiceManager;
        this.invoiceService = invoiceService1;
        this.allInOneService = allInOneService;
        this.productService = productService;
        this.invoiceItemManager = invoiceItemManager;
        this.clientContactManager = clientContactManager;
        this.saleInvoiceSolrComponent = saleInvoiceSolrComponent;
        this.folderManager = folderManager;
        this.documentsServiceLocal = documentsServiceLocal;
        this.attachmentUtilsManager = attachmentUtilsManager;
        this.faiEventsManager = faiEventsManager;
    }

    public void updateInvoiceCustomFields(IdCustomFieldsDTO request) {
        EdsInvoice edsInvoice = invoiceManager.get(request.getId());
        if (edsInvoice == null) return;
        List<CompanyCustomFieldItem> customFields = CustomFieldsUtils.convertCustomFields(request.getCustomFields(), commonService.getCompanyCustomFields(ViewName.SaleInvoice), edsInvoice.getCustomFields());
        invoiceServiceLocal.createInvoiceCustomFields(request.getId(), customFields);
    }

    public Integer createInvoice(SalesInvoiceDto request) {
        NewInvoice newInvoice = toNewInvoice(request);
        newInvoice.setNumberData(invoiceServiceLocal.getSaleInvoiceNumber());
        newInvoice.setInvoiceNumber(newInvoice.getNumberData().getInvoiceNumber());
        SaveResult saveResult = invoiceService.saveSaleInvoice(newInvoice);
        return saveResult.getId();
    }


    private NewInvoice toNewInvoice(SalesInvoiceDto request) {
        NewInvoice newInvoice = new NewInvoice();
        if (CollectionUtils.isNotEmpty(request.getApprovers())) {
            ArrayList<ApproverItemMini> approvers = request.getApprovers().stream()
                    .map(this::getChosenApprovers)
                    .collect(Collectors.toCollection(ArrayList::new));
            newInvoice.setApprovers(approvers);
        }
        newInvoice.setClientID(request.getClientId());
        Optional.ofNullable(request.getInvoiceDate())
                .map(DateNonConvertable::new)
                .ifPresent(newInvoice::setInvoiceDate);
        Optional.ofNullable(request.getDueDate())
                .map(DateNonConvertable::new)
                .ifPresent(newInvoice::setDueDate);
        newInvoice.setDescription(request.getDescription());
        if (CollectionUtils.isNotEmpty(request.getProductIds())) {
            NewInvoiceItem[] items = request.getProductIds().stream()
                    .map(productService::getProduct)
                    .map(this::toNewInvoiceItem)
                    .toArray(NewInvoiceItem[]::new);
            newInvoice.setItems(items);
        }
        Arrays.stream(newInvoice.getItems())
                .map(NewInvoiceItem::getTotalAmount)
                .reduce(BigDecimal::add)
                .ifPresent(t -> {
                    newInvoice.setTotal(t);
                    newInvoice.setSubtotal(t);
                });
        newInvoice.setExchageRate(BigDecimal.ONE);
        newInvoice.setStatusID(314);
        newInvoice.setStatusCode("APPROVE");
        newInvoice.setType(request.getType());
        Optional.ofNullable(request.getCurrency())
                .map(IdNameTO::getId)
                .ifPresent(newInvoice::setCurrencyID);
        return newInvoice;
    }

    private NewInvoiceItem toNewInvoiceItem(NewProduct product) {
        NewInvoiceItem item = new NewInvoiceItem();
        item.setItemID(product.getObjectId());
        item.setQuantity(BigDecimal.ONE);
        item.setDescription(product.getDescription());
        item.setProductType(product.getType());
        item.setUnitPrice(product.getSellingPrice());
        item.setTotalAmount(item.getQuantity().multiply(item.getUnitPrice()));
        item.setNet(product.getUnitPrice());
        return item;
    }

    public ApproverItemMini getChosenApprovers(ApproverTO approverItem) {
        ApproverItemMini item = new ApproverItem();
        if (approverItem.getAppproveStatusId() != null) {
            item.setAppproveStatusId(approverItem.getAppproveStatusId());
        }
        if (approverItem.getRejectStatusId() != null) {
            item.setRejectStatusId(approverItem.getRejectStatusId());
        }
        item.setApproverOrder(approverItem.getApproverOrder());
        item.setClonedFrom(approverItem.getClonedFrom());
        item.setExactEmployee(approverItem.getExactEmployee());
        return item;
    }

    public SalesInvoiceDto getInvoiceById(Integer id) {
        NewInvoice invoice = invoiceService.getInvoice(id);
        return toSalesInvoiceDto(invoice, invoiceItemManager.getByInvoiceIds(List.of(invoice.getID())));
    }

    private SalesInvoiceDto toSalesInvoiceDto(NewInvoice invoice, List<EdsInvoiceItem> invoiceItems) {
        SalesInvoiceDto dto = new SalesInvoiceDto();
        if (invoice.getCalcScale() == null) {
            invoice.setCalcScale(ServerUtils.getSystemCalculationScale());
        }
        dto.setId(invoice.getID());
        Optional.ofNullable(invoice.getCurrentApproverSelectItem())
                .map(SelectItem::getId)
                .map(List::of)
                .ifPresent(dto::setApproverIds);
        dto.setClientId(invoice.getClientID());
        IdCode client = new IdCode(invoice.getClientID(), invoice.getClientNumber());
        client.addProperty("name", invoice.getClientName());
        dto.setClient(client);
        dto.setInvoiceDate(invoice.getInvoiceDate().getDate());
        Optional.ofNullable(invoice.getDueDate())
                .map(DateNonConvertable::getDate)
                .ifPresent(dto::setDueDate);
        dto.setTotal(invoice.getTotal());
        dto.setDescription(invoice.getDescription());
        dto.setSubtotal(invoice.getSubtotal());
        dto.setNumberString(invoice.getInvoiceNumber());
        Map<Integer, NewProduct> productMap = new HashMap<>();
        for (EdsInvoiceItem invoiceItem : invoiceItems) {
            NewProduct productEditData = productService.getProductEditData(invoiceItem.getItem().getObjectID(), false);
            productMap.put(productEditData.getObjectId(), productEditData);
        }
        var itemsTo = toItemsTo(invoiceItems, productMap);
        dto.setItems(itemsTo);
        IdCode status = new IdCode(invoice.getStatusID(), invoice.getStatusCode());
        status.addProperty("name", invoice.getStatus());
        dto.setStatus(status);
        dto.setDueAmount(getDueAmount(invoice));
        dto.setPaidAmount(invoice.getPaidAmount());
        var currency = new IdNameTO(invoice.getCurrencyID(), invoice.getCurrencyName());
        Optional.ofNullable(invoice.getCurrencySymbol())
                .ifPresent(symbol -> currency.addProperty("symbol", symbol));
        dto.setCurrency(currency);
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

    private List<IdNameTO> toItemsTo(List<EdsInvoiceItem> invoiceItems, Map<Integer, NewProduct> productMap) {
        return invoiceItems.stream()
                .map(i -> {
                    var item = i.getItem();
                    var idNameTO = new IdNameTO(item.getObjectID(), item.getName());
                    idNameTO.addProperty("description", item.getDescription());
                    Optional.ofNullable(item.getCategory())
                            .map(c -> new IdNameTO(c.getObjectID(), c.getName()))
                            .ifPresent(c -> idNameTO.addProperty("category", c));
                    var newProduct = productMap.get(item.getObjectID());
                    if (newProduct != null && newProduct.getProductCustomFieldItems() != null) {
                        List<CustomFieldDto> customFields = newProduct.getProductCustomFieldItems().stream()
                                .map(CustomFieldsUtils::getCustomFieldDto)
                                .filter(cf -> cf.getValue() != null)
                                .toList();
                        idNameTO.addProperty("customFields", customFields);
                    }
                    return idNameTO;
                })
                .toList();
    }

    public void updateInvoice(Integer id, SalesInvoiceDto request) {
        NewInvoice newInvoice = toNewInvoice(request);
        newInvoice.setID(id);
        invoiceService.updateSaleInvoice(newInvoice);
    }

    public List<SalesInvoiceDto> getAllInvoices(ListingFilterParameter fp, Boolean includeProduct) {
        ArrayList<NewInvoice> invoices = invoiceService.getSaleInvoiceData(fp).getList();
        List<Integer> invoiceIds = invoices.stream().map(NewInvoice::getID).toList();
        if (!Boolean.TRUE.equals(includeProduct)) {
            return invoices.stream().map(inv -> toSalesInvoiceDto(inv, invoiceItemManager.getByInvoiceIds(List.of(inv.getID())))).toList();
        }
        Map<Integer, List<EdsInvoiceItem>> invoiceItems = invoiceItemManager.getByInvoiceIds(invoiceIds).stream()
                .filter(i -> i.getInvoice() != null)
                .collect(Collectors.groupingBy(edsInvoiceItem -> edsInvoiceItem.getInvoice().getObjectID()));
        return invoices.stream().map(invoice -> toSalesInvoiceDto(invoice, invoiceItems.getOrDefault(invoice.getID(), List.of()))).toList();
    }

    public List<ApproverItemMini> getApprover(Integer objectId) {
        ApprovalListResult approvers = allInOneService.getApprovers(SALE_INVOICE,
                objectId,
                false,
                ServerSecurityContext.getInstance().getStaticUserID(),
                false,
                true,
                new ListingFilterParameter());
        ArrayList<ApproverItem> list = approvers.getList();
        return list.stream().map(this::toMini).toList();
    }

    private ApproverItemMini toMini(ApproverItem approverItem) {
        ApproverItemMini approverItemMini = new ApproverItemMini();
        approverItemMini.setObjectID(approverItem.getObjectID());
        approverItemMini.setClonedFrom(approverItem.getClonedFrom());
        approverItemMini.setFromBackupEmployeeDate(approverItem.getFromBackupEmployeeDate());
        approverItemMini.setDueBackupEmployeeDate(approverItem.getDueBackupEmployeeDate());
        approverItemMini.setAppproveStatusId(approverItem.getAppproveStatusId());
        approverItemMini.setExactEmployee(approverItem.getExactEmployee());
        return approverItemMini;
    }

    public BatchPaymentResult paySalesInvoice(SalesInvoicePaymentDto request) {
        var invoiceData = invoiceService.getInvoiceSummaryData(request.getInvoiceId());
        PaymentData paymentData = new PaymentData();
        paymentData.setPaymentAccount(new SelectItem(request.getPaidToId()));
        paymentData.setExchangeRate(Optional.ofNullable(invoiceData.getExchageRate()).orElse(BigDecimal.ONE));
        paymentData.setReferenceNumber(invoiceData.getNumberData().getInvoiceNumber());
        paymentData.setDate(new DateNonConvertable(request.getPaidDate()));
        paymentData.setPaymentAmount(request.getPaidAmount());
        paymentData.setInvoiceID(request.getInvoiceId());
        CurrencyItem currency = new CurrencyItem(invoiceData.getCurrencyID(), null, null);
        paymentData.setCurrency(currency);

        ReceivePaymentData receivePaymentData = new ReceivePaymentData();
        receivePaymentData.setBatchPayment(true);

        receivePaymentData.setCrmAccount(invoiceData.getTypeItem());
        receivePaymentData.setAccount(paymentData.getPaymentAccount());
        receivePaymentData.setExRate(paymentData.getExchangeRate());
        receivePaymentData.setCurrency(currency);
        receivePaymentData.setReference(paymentData.getReferenceNumber());
        receivePaymentData.setDate(paymentData.getDate());
        receivePaymentData.setTotalAmount(paymentData.getPaymentAmount());

        receivePaymentData.setPayments(new PaymentData[]{paymentData});
        receivePaymentData.setValidateReferences(false);
        receivePaymentData.setType(RECEIVABLE);
        receivePaymentData.setPaymentTarget(AccountingConstants.PAYMENT_TARGET_INVOICE);
        return invoiceService.saveReceivePaymentData(receivePaymentData, true);
    }

    public List<SalesInvoiceDto> getActiveInvoice(Integer crmAccountId) {
        if (crmAccountId == null) {
            EdsUser user = invoiceManager.getUser();
            EdsClientContact edsClientContact = clientContactManager.get(user.getObjectID());
            EdsCrmContact crmContact = edsClientContact.getCrmContact();
            EdsCrmAccount crmAccount = crmContact.getCrmAccount();
            crmAccountId = crmAccount.getObjectID();
        }
        var activeInvoices = invoiceManager.getPrioritizedInvoices(List.of(crmAccountId));
        return activeInvoices.stream()
                .map(CrmAccountInvoiceTO::getInvoiceId)
                .map(this::getInvoiceById)
                .toList();
    }

    @Transactional
    public ResponseEntity<Void> updateInvoiceZatcaStatus(FaiWebhookRequest request, Integer companyId, String database) {
        FaiWebhookPayload payload = request.getPayload();
        String integrationId = payload.getId();
        EdsInvoice edsInvoice = invoiceManager.getByIntegrationId(integrationId);
        if (edsInvoice == null) {
            EdsFaiEvents faiEvents = new EdsFaiEvents();
            faiEvents.setCompanyId(companyId);
            faiEvents.setDatabase(database);
            faiEvents.setIntegrationId(integrationId);
            faiEvents.setZatca_status(payload.getZatca_status());
            faiEvents.setStatus(String.valueOf(NOT_FOUND));
            faiEventsManager.create(faiEvents);
            log.error("Invoice with integration id {} not found", integrationId);
            return ResponseEntity.notFound().build();
        }
        if (payload.getZatca_response() != null) {
            writeNotes(payload.getZatca_response(), edsInvoice.getObjectID());
        }
        if (payload.isZatcaStatusFailed()) {
            edsInvoice.setReportedDate(new Date());
        }
        ServerSecurityContext.getInstance().setStaticUserID(edsInvoice.getCreator().getObjectID());
        edsInvoice.setZatcaStatus(WordUtils.capitalizeFully(payload.getZatca_status().replace("_", " ")));
        invoiceManager.update(edsInvoice);
        try {
            saleInvoiceSolrComponent.index((EdsSaleInvoice) edsInvoice);
        } catch (IOException | SolrServerException | InterruptedException e) {
            log.error(e.getMessage());
        }
        EdsFolder salesInvoiceFolder = folderManager.getFolderByFolderType(Constants.F_SALE_INV);
        FolderResource folderResource = documentsServiceLocal.getFolderResource(salesInvoiceFolder.getObjectID());

        if (payload.isZatcaStatusSuccess()) {
            try {
                FileItem fileItemEn = getFileItem(payload.getPdfLinkEn(), folderResource, edsInvoice.getObjectID());
                FileItem fileItemAr = getFileItem(payload.getPdfLinkAr(), folderResource, edsInvoice.getObjectID());
                List<FileItem> fileItems = new ArrayList<>(List.of(fileItemEn, fileItemAr));
                if (payload.getZatca_xml_file() != null) {
                    fileItems.add(getFileItem(payload.getZatca_xml_file(), folderResource, edsInvoice.getObjectID(), "ZATCA_Signed_Invoice.xml"));
                }
                attachmentUtilsManager.saveAttachments(F_SALE_INV, edsInvoice.getObjectID(), edsInvoice.getObjectID(), fileItems.toArray(new FileItem[0]));
            } catch (Exception e) {
                log.error("Failed to download file", e);
            }
        }
        return ResponseEntity.ok().build();
    }

    private void writeNotes(FaiZatcaResponse response, Integer invoiceId) {
        Optional.ofNullable(response.getInfo()).orElse(List.of())
                .forEach(i -> writeNote(invoiceId, "Zatca info: " + i.getMessage()));
        Optional.ofNullable(response.getWarnings()).orElse(List.of())
                .forEach(w -> writeNote(invoiceId, "Zatca warning: " + w.getMessage()));
        Optional.ofNullable(response.getErrors()).orElse(List.of())
                .forEach(e -> writeNote(invoiceId, "Zatca error"));
    }

    private void writeNote(Integer invoiceId, String comment) {
        HistoryListItem note = new HistoryListItem();
        note.setEmployee(Constants.defaultSupportName);
        note.setEventDate(new Date());
        note.setComment(comment);
        invoiceService.createInvoiceNoteAndHistory(invoiceId, "saleinvoice", note, true);
    }

    private FileItem getFileItem(String url, FolderResource folder, Integer objectId) throws IOException {
        return getFileItem(url, folder, objectId, null);
    }

    private FileItem getFileItem(String url, FolderResource folder, Integer objectId, String fileName) throws IOException {
        MultipartFile file = downloadFile(url);
        if (fileName != null) {
            file = new MockMultipartFile(fileName, fileName, file.getContentType(), file.getInputStream());
        }
        FileResource savedFile = documentsServiceLocal.saveDocumentFile(file, folder.getObjectId(), folder.getFileType(), objectId, null);
        FileItem fileItem = new FileItem();
        fileItem.setId(savedFile.getObjectId());
        fileItem.setFileName(savedFile.getFileName());
        return fileItem;
    }

    public MultipartFile downloadFile(String fileUrl) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.ALL));

        ResponseEntity<byte[]> response = restTemplate.exchange(fileUrl, HttpMethod.GET, new HttpEntity<>(headers), byte[].class);

        if (response.getBody() == null) {
            throw new IOException("Failed to download file: Empty response body");
        }

        String contentType = response.getHeaders().getContentType() != null
                ? response.getHeaders().getContentType().toString()
                : "application/octet-stream";

        String filename = extractFilename(response.getHeaders());

        return new MockMultipartFile(filename, filename, contentType, new ByteArrayInputStream(response.getBody()));
    }

    private static String extractFilename(HttpHeaders headers) {
        String contentDisposition = headers.getFirst(HttpHeaders.CONTENT_DISPOSITION);
        return (contentDisposition != null) ? extractFilenameFromHeader(contentDisposition) : "invoice.pdf";
    }

    private static String extractFilenameFromHeader(String contentDisposition) {
        Matcher matcher = Pattern.compile("filename=\"([^\"]+)\"").matcher(contentDisposition);
        return matcher.find() ? matcher.group(1) : "invoice.pdf";
    }
}
