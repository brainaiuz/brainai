package com.edatasite.workforce.rest.v2.release10.accounting;

import com.edatasite.workforce.core.domain.EdsImportFile;
import com.edatasite.workforce.core.domain.documents.EdsFolder;
import com.edatasite.workforce.gwt.core.client.enums.ImportStatusEnum;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.ImportFileManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FolderManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.ImportCustomEventListenerImpl;
import com.edatasite.workforce.gwt.documents.client.gwtupload.UUID;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.server.GwtUploadServlet;
import com.edatasite.workforce.gwt.profile.client.rpc.ImportLogItem;
import com.edatasite.workforce.gwt.profile.server.app.ProfileServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.batchinvoice.FilterTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.batchinvoice.ImportLogTO;
import com.edatasite.workforce.rest.v2.release10.core.to.accounting.invoice.batchinvoice.PagedDTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.utils.EdsContextParams;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dilsh0d on 11/2/2017.
 */
@Tag(name = "Sales Invoice Batch", description = "Sales Invoice Batch API")
@RestController
@RequestMapping(headers = {ApiConstants.X_AUTH, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiSalesInvoiceBatchControllerV2 extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiSalesInvoiceBatchControllerV2.class);

    @Autowired
    private ImportFileManager importFileManager;
    @Autowired
    private FolderManager folderManager;
    @Autowired
    private ServletContext servletContext;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private ProfileServiceLocal profileServiceLocal;


    @Operation(summary = "Sales Invoice Batch", description = "Sales Invoice Batch Import")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/sales_invoice_batch", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Transactional
    public Object salesInvoiceBatch(@RequestParam(value = "file") MultipartFile multipartFile) throws RestException {

        if (multipartFile == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "File is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        EdsFolder salesInvoiceFolder = folderManager.getFolderByFolderType(Constants.F_SALE_INV);
        if (salesInvoiceFolder == null || salesInvoiceFolder.getObjectID() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Sales invoice root folder is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        FolderResource folderResource = documentsServiceLocal.getFolderResource(salesInvoiceFolder.getObjectID());
        if (folderResource == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Sales invoice folder with id " + salesInvoiceFolder.getObjectID() + " not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        String originalFileName = multipartFile.getOriginalFilename().replace("%20", " ");

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
            IOUtils.copy(multipartFile.getInputStream(), os);
            multipartFile.getInputStream().close();
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

        ArrayList<FileResource> result = documentsServiceLocal.uploadAllFiles(files, folderResource, "Invoice Batch Import");
        if (!result.isEmpty()) {
            FileResource f = result.get(0);

            EdsImportFile edsImportFile = new EdsImportFile();
            edsImportFile.setFileID(f.getObjectId());
            edsImportFile.setType(ImportTypeEnum.BATCH_SALES_INVOICE);
            edsImportFile.setStatus(ImportStatusEnum.IN_PROCESS);
            edsImportFile.setDefaultSeparator(",");
            edsImportFile.setOwner(userManager.getUser());
            edsImportFile.setHasHeader(true);
            importFileManager.create(edsImportFile);
            baseEventPostProcessor.registerEvent(ImportCustomEventListenerImpl.TYPE, ImportCustomEventListenerImpl.EVENT_IMPORT_BATCH_INVOICE_WITHOUT_PAYMENT, edsImportFile, userManager.getUser());
            return successResponse(new ResponseData());
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Error occurred while uploading invoice file", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Operation(summary = "Sales Invoice Payment Batch", description = "Sales Invoice Payment Batch Import")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/sales_invoice_payment_batch", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Transactional
    public Object salesInvoicePaymentBatch(@RequestParam(value = "file") MultipartFile multipartFile) throws RestException {

        if (multipartFile == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "File is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        EdsFolder salesInvoiceFolder = folderManager.getFolderByFolderType(Constants.F_SALE_INV);
        if (salesInvoiceFolder == null || salesInvoiceFolder.getObjectID() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Sales invoice payment root folder is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        FolderResource folderResource = documentsServiceLocal.getFolderResource(salesInvoiceFolder.getObjectID());
        if (folderResource == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Sales invoice payment folder with id " + salesInvoiceFolder.getObjectID() + " not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        String originalFileName = multipartFile.getOriginalFilename().replace("%20", " ");

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
            IOUtils.copy(multipartFile.getInputStream(), os);
            multipartFile.getInputStream().close();
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

        ArrayList<FileResource> result = documentsServiceLocal.uploadAllFiles(files, folderResource, "Invoice Payment Batch Import");
        if (!result.isEmpty()) {
            FileResource f = result.get(0);

            EdsImportFile edsImportFile = new EdsImportFile();
            edsImportFile.setFileID(f.getObjectId());
            edsImportFile.setType(ImportTypeEnum.BATCH_SALES_INVOICE_PAYMENT);
            edsImportFile.setStatus(ImportStatusEnum.IN_PROCESS);
            edsImportFile.setDefaultSeparator(",");
            edsImportFile.setOwner(userManager.getUser());
            edsImportFile.setHasHeader(true);
            importFileManager.create(edsImportFile);
            baseEventPostProcessor.registerEvent(ImportCustomEventListenerImpl.TYPE, ImportCustomEventListenerImpl.EVENT_IMPORT_BATCH_INVOICE_PAYMENT, edsImportFile, userManager.getUser());
            return successResponse(new ResponseData());
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Error occurred while uploading invoice payment file", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Import Logs", description = "Sales Invoice/Payment Import Logs")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/batch_import_logs", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Transactional
    public Object getImportLogs(@RequestBody FilterTO filterTO) throws RestException {
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setFromDate(filterTO.getFromDate().getTime());
        filterParameter.setToDate(filterTO.getToDate().getTime());
        filterParameter.setStart(filterTO.getPage());
        filterParameter.setLimit(filterTO.getSize());
        if (StringUtils.isNotBlank(filterTO.getEntity_type())) {
            if ("INVOICE".equals(filterTO.getEntity_type())) {
                filterParameter.setDataType(ImportTypeEnum.BATCH_SALES_INVOICE.name());
            } else if ("INVOICE_PAYMENT".equals(filterTO.getEntity_type())) {
                filterParameter.setDataType(ImportTypeEnum.BATCH_SALES_INVOICE_PAYMENT.name());
            } else if ("SALES_ORDER".equals(filterTO.getEntity_type())) {
                filterParameter.setDataType(ImportTypeEnum.BATCH_SALES_ORDER.name());
            }
        }
        if (StringUtils.isNotBlank(filterTO.getSorted_column())) {
            if ("import_date".equals(filterTO.getSorted_column())) {
                filterParameter.setSortField(ImportLogItem.DATE);
            } else if ("status".equals(filterTO.getSorted_column())) {
                filterParameter.setSortField(ImportLogItem.STATUS);
            } else {
                filterParameter.setSortField(ImportLogItem.DATE);
            }
            if ("ascending".equals(filterTO.getSort_type())) {
                filterParameter.setSortDir(Constants.ASC);
                filterParameter.setAscending(true);
            } else {
                filterParameter.setSortDir(Constants.DESC);
                filterParameter.setAscending(false);
            }
        }

        ListResult<ImportLogItem> listResult = profileServiceLocal.getImportLogs(filterParameter);
        List<ImportLogTO> items = new ArrayList<>();
        for (ImportLogItem logItem : listResult.getList()) {
            ImportLogTO item = new ImportLogTO();
            if (logItem.getStatus() != null) {
                item.setStatus(logItem.getStatus().getCode());
            }
            item.setImport_date(logItem.getDate());
            item.setRequested(logItem.getRequestedRows());
            item.setImported(logItem.getImportedRows());
            item.setRejected(logItem.getRejectedRows());
            if (logItem.getLogFile() != null) {
                item.setLog_file(logItem.getLogFile().getName());
                item.setLog_file_url(logItem.getLogFile().getDescription());
            }
            if (logItem.getImportFile() != null) {
                item.setImported_file(logItem.getImportFile().getName());
                item.setImported_file_url(logItem.getImportFile().getDescription());
            }

            items.add(item);
        }

        return successResponse(new PagedDTO<>(items, filterTO.getPage(), filterTO.getSize(), listResult.getTotal(), 0, false));
    }


}
