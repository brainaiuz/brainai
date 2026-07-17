package com.edatasite.workforce.rest.v2.release10.accounting;

import com.edatasite.workforce.core.domain.EdsImportFile;
import com.edatasite.workforce.core.domain.documents.EdsFolder;
import com.edatasite.workforce.gwt.core.client.enums.ImportStatusEnum;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.ImportFileManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FolderManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.ImportCustomEventListenerImpl;
import com.edatasite.workforce.gwt.documents.client.gwtupload.UUID;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.server.GwtUploadServlet;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.utils.EdsContextParams;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
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

/**
 * Created by Dilsh0d on 11/2/2017.
 */
@Tag(name = "Sales Order Batch", description = "Sales Order Batch API")
@RestController
@RequestMapping(headers = {ApiConstants.X_AUTH, ApiConstants.ACCESS_TOKEN},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiSalesOrderBatchControllerV2 extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiSalesOrderBatchControllerV2.class);

    @Autowired
    private ImportFileManager importFileManager;
    @Autowired
    private FolderManager folderManager;
    @Autowired
    private ServletContext servletContext;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;


    @Operation(summary = "Sales Order Batch", description = "Sales Order Batch Import")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/sales_order_batch", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Transactional
    public Object salesOrderBatch(@RequestParam(value = "file") MultipartFile multipartFile) throws RestException {

        if (multipartFile == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "File is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        EdsFolder salesOrderFolder = folderManager.getFolderByFolderType(Constants.F_SALE_QUOTE);
        if (salesOrderFolder == null || salesOrderFolder.getObjectID() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Sales order root folder is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        FolderResource folderResource = documentsServiceLocal.getFolderResource(salesOrderFolder.getObjectID());
        if (folderResource == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Sales order folder with id " + salesOrderFolder.getObjectID() + " not found", NOT_FOUND, HttpStatus.NOT_FOUND);
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

        ArrayList<FileResource> result = documentsServiceLocal.uploadAllFiles(files, folderResource, "Sales Order Batch Import");
        if (!result.isEmpty()) {
            FileResource f = result.get(0);

            EdsImportFile edsImportFile = new EdsImportFile();
            edsImportFile.setFileID(f.getObjectId());
            edsImportFile.setType(ImportTypeEnum.BATCH_SALES_ORDER);
            edsImportFile.setStatus(ImportStatusEnum.IN_PROCESS);
            edsImportFile.setDefaultSeparator(",");
            edsImportFile.setOwner(userManager.getUser());
            edsImportFile.setHasHeader(true);
            importFileManager.create(edsImportFile);
            baseEventPostProcessor.registerEvent(ImportCustomEventListenerImpl.TYPE, ImportCustomEventListenerImpl.EVENT_IMPORT_BATCH_SALES_ORDER, edsImportFile, userManager.getUser());
            return successResponse(new ResponseData());
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Error occurred while uploading sales order file", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
