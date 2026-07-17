package com.edatasite.workforce.rest.v2.release10.core;

import com.edatasite.workforce.gwt.core.server.actions.CreateDocumentCommand;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.servlets.WfmMultipartFile;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.rest.base.enums.FolderRelationTypeEnum;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.to.attachments.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseListData;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Dilsh0d Madrahimov on 2/21/2018.
 */

@Tag(name = "Attachments", description = "Attachments API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiAttachmentControllerV2 extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiAttachmentControllerV2.class);

    @Autowired
    private HrmsService hrmsService;

    @Operation(summary = "Get attachments for some entity", description = "Attachments linked to Lead, Opportunity and whatever you want")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/{main_entity_path}/{id}/attachments", method = RequestMethod.GET)
    public Object getAttachmentList(@PathVariable(value = "main_entity_path") String entityType,
                                    @PathVariable(value = "id") Integer entityId) throws RestException {

        if (StringUtils.isBlank(entityType)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "main_entity_path is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (entityId == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        Integer folderType = FolderRelationTypeEnum.getRelationType(entityType);
        Integer folderID = getFolderId(folderType, entityId);

        ArrayList<FileResource> fileResources;
        try {
            fileResources = documentsServiceLocal.getFileResources(folderType, folderID, entityId);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        ArrayList<AttachmentTO> attachmentList = new ArrayList<>();
        for (FileResource fileResource : fileResources) {
            AttachmentTO attachment = new AttachmentTO();
            attachment.setItem_id(fileResource.getObjectId());
            attachment.setFile_name(fileResource.getFileName());
            attachment.setLink(hrmsService.getImageUrl(fileResource.getBodyId()));
            attachment.setUpload_date(longDateTimezoneFormat.format(fileResource.getCreationDate()));
            attachment.setFile_size(ServerUtils.getSizeAsString(fileResource.getContentLength()));
            attachmentList.add(attachment);
        }
        return successResponse(new ResponseListData<>(attachmentList));
    }

    @Operation(summary = "Add new attachment for some entity", description = """
            Request to add attachment to Lead, Opportunity and whatever you want.
             It's multipart request. We will send attachment in part called attachment.
            In response we need to receive newly created attachment object.""")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/{main_entity_path}/{id}/attachments", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Object addAttachment(MultipartRequest multipartRequest,
                                @PathVariable(value = "main_entity_path") String entityType,
                                @PathVariable(value = "id") Integer entityId) throws RestException {

        if (StringUtils.isBlank(entityType)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "main_entity_path is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (entityId == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (multipartRequest == null || multipartRequest.getFileMap() == null || multipartRequest.getFileMap().isEmpty()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "attachment is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        Integer folderType = getFolderType(entityType);
        Integer folderId = getFolderId(folderType, entityId);

        FolderResource folderResource = documentsServiceLocal.getFolderResource(folderType, folderId);

        SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);

        AttachmentTO attachmentTO = null;
        for (MultipartFile file : multipartRequest.getFileMap().values()) {
            FileResource fileResource;
            try {
                fileResource = documentsServiceLocal.saveDocumentFile(file, folderResource.getObjectId(), folderResource.getFileType(), entityId, null);
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
            attachmentTO = new AttachmentTO();
            attachmentTO.setItem_id(fileResource.getObjectId());
            attachmentTO.setFile_name(fileResource.getFileName());
            attachmentTO.setLink(fileResource.getAmazonLink());
            attachmentTO.setUpload_date(longDateTimezoneFormat.format(fileResource.getCreationDate()));
            attachmentTO.setFile_size(ServerUtils.getSizeAsString(fileResource.getContentLength()));

        }
        return successResponse(attachmentTO);
    }

    @Operation(summary = "Delete attachment of some entity", description = "Request to delete attachment of Lead, Opportunity and whatever you want")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/{main_entity_path}/{id}/attachments", method = RequestMethod.DELETE, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object deleteAttachment(@PathVariable(value = "main_entity_path") String entityType,
                                   @PathVariable(value = "id") Integer entityId,
                                   @RequestBody AttachmentTO attachment) throws RestException {

        if (attachment == null || attachment.getItem_id() == null || attachment.getItem_id() <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(entityType)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "main_entity_path is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (entityId == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        try {
            documentsServiceLocal.deleteFile(attachment.getItem_id());
            return successResponse(new ResponseData());
        } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
            log.error(e.getMessage());
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Operation(summary = "Add new attachment for some entity", description = """
            Request to add attachment to Lead, Opportunity and whatever you want.
             It's multipart request. We will send attachment in part called attachment.
            In response we need to receive newly created attachment object.""")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "")})
    @RequestMapping(value = "/uploadAttachment")
    public Integer uploadInvoiceFile(@RequestParam("uploadFile") MultipartFile file) throws RestException {
        try {
            if (file == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Attachment is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            CreateDocumentCommand documentCommand = new CreateDocumentCommand();
            WfmMultipartFile multipartFile = new WfmMultipartFile("", file);
            documentCommand.addFile(multipartFile);
            String[] values = wfmCommandServiceLocal.createAttachmentHandler(documentCommand);
            if (values[0] == null || values[0].isEmpty()) {
                return 0;
            } else {
                return Integer.valueOf(values[0]);
            }
        } catch (Throwable throwable) {
            throw new RestException(GENERAL_ERROR_MESSAGE, throwable.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
