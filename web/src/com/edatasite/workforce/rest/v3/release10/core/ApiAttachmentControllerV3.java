package com.edatasite.workforce.rest.v3.release10.core;

import com.edatasite.workforce.core.domain.EdsCompanyCustomFieldsSettings;
import com.edatasite.workforce.core.domain.documents.EdsFileHeader;
import com.edatasite.workforce.core.domain.documents.EdsFolder;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.db.CompanyCustomFieldsManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FileHeaderManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FolderManager;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.server.app.DocumentsServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.base.to.AttachmentTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.to.ResultTO;
import com.edatasite.workforce.rest.v3.release10.crm.service.ApiContactService;
import com.edatasite.workforce.rest.v3.release10.pm.service.ApiProjectService;
import com.edatasite.workforce.rest.v3.release10.pm.service.ApiTaskService;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.F_CUSTOM_FIELD_ITEM;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.GENERAL_ERROR_MESSAGE;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.REQUIRED;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.SERVER_ERROR;

@Tag(name = "Attachment", description = "Attachment API")
@RestController
@RequestMapping(value = "/attachment", headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
public class ApiAttachmentControllerV3 {
    private static final Logger log = LoggerFactory.getLogger(ApiAttachmentControllerV3.class);

    private final DocumentsServiceLocal documentsServiceLocal;
    private final CommonService commonService;
    private final FileHeaderManager fileHeaderManager;
    private final FolderManager folderManager;
    private final ApiTaskService apiTaskService;
    private final CompanyCustomFieldsManager companyCustomFieldsManager;
    private final ApiProjectService apiProjectService;
    private final ApiContactService apiContactService;

    @Autowired
    public ApiAttachmentControllerV3(DocumentsServiceLocal documentsServiceLocal, CommonService commonService, FileHeaderManager fileHeaderManager, FolderManager folderManager, ApiTaskService apiTaskService, CompanyCustomFieldsManager companyCustomFieldsManager, ApiProjectService apiProjectService, ApiContactService apiContactService) {
        this.documentsServiceLocal = documentsServiceLocal;
        this.commonService = commonService;
        this.fileHeaderManager = fileHeaderManager;
        this.folderManager = folderManager;
        this.apiTaskService = apiTaskService;
        this.companyCustomFieldsManager = companyCustomFieldsManager;
        this.apiProjectService = apiProjectService;
        this.apiContactService = apiContactService;
    }

    @Operation(summary = "Upload attachment")
    @RequestMapping(value = "/upload", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResultTO<String> uploadAttachment(@RequestParam("file") MultipartFile file,
                                             @RequestParam(value = "itemId", required = false) Integer itemId) throws RestException {
        try {
            if (file == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Attachment is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            FolderResource folderResource = commonService.getTempFolderByCompanyID(null, null);
            FileResource fileResource = documentsServiceLocal.saveDocumentFile(file, folderResource.getObjectId(), folderResource.getFileType(), itemId, "");
            return ResultTO.success(fileResource.getFileName());
        } catch (Throwable throwable) {
            throw new RestException(GENERAL_ERROR_MESSAGE, throwable.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Upload attachment")
    @PostMapping(path = "/custom", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResultTO<AttachmentTO> uploadAttachment(@RequestParam("file") MultipartFile file,
                                                   @RequestParam("viewName") ViewName viewName,
                                                   @RequestParam("itemId") Integer itemId,
                                                   @RequestParam("alias") String alias) throws RestException {
        log.info("REST request to uplaod filter to: {}", itemId);
        try {
            if (file == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Attachment is required", REQUIRED, HttpStatus.BAD_REQUEST);
            }
            EdsCompanyCustomFieldsSettings customFields = companyCustomFieldsManager.getByAliasName(viewName.name(), alias);
            FileResource fileResource = documentsServiceLocal.saveDocumentFile(file, null, F_CUSTOM_FIELD_ITEM, customFields.getObjectID(), "");
            if (ViewName.Task.equals(viewName)) {
                apiTaskService.saveTaskCustomField(itemId, alias, fileResource);
            } else if (ViewName.Project.equals(viewName)) {
                apiProjectService.saveProjectCustomField(itemId, alias, fileResource);
            } else if (ViewName.Contact.equals(viewName) || ViewName.Lead.equals(viewName)) {
                apiContactService.saveContactCustomField(itemId, alias, fileResource, viewName);
            }
            return ResultTO.success(new AttachmentTO(fileResource));
        } catch (Throwable throwable) {
            throw new RestException(GENERAL_ERROR_MESSAGE, throwable.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @Operation(summary = "Delete attachment")
    @RequestMapping(method = RequestMethod.DELETE)
    public ResultTO<?> deleteAttachment(@RequestParam("name") String filename,
                                        @RequestParam("folderType") int folderType,
                                        @RequestParam(value = "itemId", required = false) Integer entityID) throws ObjectNotFoundException, InsufficientPermissionsException {
        FolderResource folderResource = commonService.getTempFolderByCompanyID(null, null);
        EdsFolder folder = folderManager.getFolder(folderType, entityID);

        EdsFileHeader fileHeader = fileHeaderManager.getFile(folderResource.getObjectId(), filename);
        if (fileHeader == null) {
            fileHeader = fileHeaderManager.getFile(folder.getObjectID(), filename);
        }
        documentsServiceLocal.deleteFile(fileHeader.getObjectID());
        return ResultTO.success();
    }

    @Operation(summary = "Delete custom attachment")
    @RequestMapping(path = "/custom", method = RequestMethod.DELETE)
    public ResultTO<?> deleteCustomAttachment(@RequestParam("name") String filename,
                                              @RequestParam("viewName") ViewName viewName,
                                              @RequestParam("alias") String alias) throws ObjectNotFoundException, InsufficientPermissionsException {
        EdsCompanyCustomFieldsSettings customFields = companyCustomFieldsManager.getByAliasName(viewName.name(), alias);
        FolderResource folderResource = commonService.getTempFolderByCompanyID(null, null);
        EdsFolder folder = folderManager.getFolder(F_CUSTOM_FIELD_ITEM, customFields.getObjectID());

        EdsFileHeader fileHeader = fileHeaderManager.getFile(folderResource.getObjectId(), filename);
        if (fileHeader == null) {
            fileHeader = fileHeaderManager.getFile(folder.getObjectID(), filename);
        }
        documentsServiceLocal.deleteFile(fileHeader.getObjectID());
        return ResultTO.success();
    }

    @Operation(summary = "Create system folders for company which is not system folders created.")
    @RequestMapping(value = "/createSystemFolders/{dataBase}/{companyId}", method = RequestMethod.POST)
    @Transactional
    public void createSystemFoldersTest(@PathVariable("dataBase") String dataBase, @PathVariable("companyId") Integer companyId) {
        if (Objects.isNull(companyId) || dataBase == null || dataBase.isEmpty())
            return;

        if (!Constants.DATABASE_FREE.equals(dataBase) && !Constants.DATABASE_PAID.equals(dataBase))
            return;
        String database_old = ServerSecurityContext.getInstance().getDatabase();

        ServerSecurityContext.getInstance().setDatabase(dataBase);

        if (folderManager.getSystemFolder(companyId) != null)
            return;

        try {
            documentsServiceLocal.createSystemFolders(companyId);
        } catch (Exception e) {
            log.error("Create System folders error:{}", e.getMessage());
        }
        ServerSecurityContext.getInstance().setDatabase(database_old);
    }
}
