package com.edatasite.workforce.rest.v2.release10.documents;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.documents.EdsDocumentPermission;
import com.edatasite.workforce.core.domain.documents.EdsFileHeader;
import com.edatasite.workforce.core.domain.documents.EdsFolder;
import com.edatasite.workforce.core.domain.rbac.EdsGroup;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMembersViewItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ListUtils;
import com.edatasite.workforce.gwt.core.server.app.LoginServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.documents.FileHeaderManager;
import com.edatasite.workforce.gwt.core.server.db.documents.FolderManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.GroupManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.documents.FolderRbacManager;
import com.edatasite.workforce.gwt.documents.client.exceptions.DuplicateNameException;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.exceptions.QuotaExceededException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.OtherUserResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.OthersResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.PermissionHolder;
import com.edatasite.workforce.gwt.documents.client.rest.resource.SharedResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.SystemResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.UserResource;
import com.edatasite.workforce.gwt.googledocuments.client.rpc.GoogleDocumentsService;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.base.CategoryTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseListData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.ResponseResultListData;
import com.edatasite.workforce.rest.v2.release10.core.to.base.SelectItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.documents.CheckAccessRequest;
import com.edatasite.workforce.rest.v2.release10.core.to.documents.CopyMoveFolderTO;
import com.edatasite.workforce.rest.v2.release10.core.to.documents.DocumentHistoryLogTO;
import com.edatasite.workforce.rest.v2.release10.core.to.documents.DocumentSectionPermissionTO;
import com.edatasite.workforce.rest.v2.release10.core.to.documents.FileDetailTO;
import com.edatasite.workforce.rest.v2.release10.core.to.documents.FileMoveCopyTO;
import com.edatasite.workforce.rest.v2.release10.core.to.documents.FileTO;
import com.edatasite.workforce.rest.v2.release10.core.to.documents.FileUpdateTO;
import com.edatasite.workforce.rest.v2.release10.core.to.documents.FileUploadTO;
import com.edatasite.workforce.rest.v2.release10.core.to.documents.FolderAddTO;
import com.edatasite.workforce.rest.v2.release10.core.to.documents.FolderIdTO;
import com.edatasite.workforce.rest.v2.release10.core.to.documents.FolderRequestListTO;
import com.edatasite.workforce.rest.v2.release10.core.to.documents.FolderUpdateTO;
import com.edatasite.workforce.rest.v2.release10.core.to.documents.OwnerTO;
import com.edatasite.workforce.rest.v2.release10.core.to.documents.ParentFolderTO;
import com.edatasite.workforce.rest.v2.release10.core.to.documents.PermissionHolderRequestTO;
import com.edatasite.workforce.rest.v2.release10.core.to.documents.PermissionHolderTO;
import com.edatasite.workforce.rest.v2.release10.core.to.documents.PermissionUpdateTO;
import com.edatasite.workforce.rest.v2.release10.core.to.documents.RemoveFilePermissionTO;
import com.edatasite.workforce.rest.v2.release10.core.to.documents.RemoveFolderPermissionTO;
import com.edatasite.workforce.rest.v2.release10.core.to.documents.RootFileTO;
import com.edatasite.workforce.rest.v2.release10.core.to.documents.RootFolderRequestTO;
import com.edatasite.workforce.rest.v2.release10.core.to.documents.StorageInfoTO;
import com.edatasite.workforce.rest.v2.release10.core.to.documents.UserGroupsResultTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.EmployeeTO;
import com.edatasite.workforce.rest.v2.release10.enums.DocumentsUserTypeEnum;
import com.edatasite.workforce.rest.v2.release10.enums.RootFolderTypeEnum;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Anvar Akramov on 10/27/2017.
 */
@Tag(name = "Documents", description = "Documents API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiDocumentsControllerV2 extends BaseApiControllerV2 {

    private static final Logger log = LoggerFactory.getLogger(ApiDocumentsControllerV2.class);
    private final SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);
    @Autowired
    private LoginServiceLocal loginServiceLocal;
    @Autowired
    private FolderManager folderManager;
    @Autowired
    private FileHeaderManager fileHeaderManager;
    @Autowired
    private FolderRbacManager folderRbacManager;
    @Autowired
    private GoogleDocumentsService googleDocumentsService;
    @Autowired
    private GroupManager groupManager;
    @Autowired
    private RbacService rbacService;

    private static Integer getPatentId(Integer parentID) {
        return parentID == null ? 0 : parentID;
    }

    @Operation(summary = "Get Root Types", description = """
            1) Retrieves the list of available folder root types\s

             2) The following errors may occur during root_type retrieval:\s

            Session may be expired - Error Code - 3003\s

             Invalid Access token may be provided - Error Code - 3006\s

             Root Types may not be found - Error Code - 3005

             Internal Server Error occurred - Error Code - 3008""")
    @RequestMapping(value = "/root_types", method = RequestMethod.GET)
    public Object getRootTypes() throws RestException {
        ArrayList<RootFileTO> rootTypes = new ArrayList<>();
        try {
            //system folder
            SystemResource systemResource = documentsServiceLocal.getSystemFolder();
            rootTypes.add(new RootFileTO(systemResource.getObjectId(), "System folder", RootFolderTypeEnum.SYSTEM_FOLDER.name()));

            //public folder
            FolderResource folderResource = documentsServiceLocal.getPublicFolder();
            rootTypes.add(new RootFileTO(folderResource.getObjectId(), "Public folder", RootFolderTypeEnum.PUBLIC_FOLDER.name()));
            //My folder
            ArrayList<FolderResource> folderResources = documentsServiceLocal.getFolders(null);
            rootTypes.add(new RootFileTO(folderResources.get(0).getObjectId(), "My folder", RootFolderTypeEnum.MY_FOLDER.name()));

            rootTypes.add(new RootFileTO("Shared with me", RootFolderTypeEnum.SHARED_WITH_ME.name()));
            rootTypes.add(new RootFileTO("Shared by me", RootFolderTypeEnum.SHARED_BY_ME.name()));
            rootTypes.add(new RootFileTO("Trash", RootFolderTypeEnum.TRASH.name()));
        } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return successResponse(new ResponseListData<>(rootTypes));
    }

    @Operation(summary = "Get Storage Size Info", description = "Storage info are displayed in GB")
    @RequestMapping(value = "/storage_info", method = RequestMethod.GET)
    public Object getStorageInfo() throws RestException {
        Double companyMaxStorage = 10d;
        Double companyUsedStorage = 0d;
        Double[] result;
        try {
            result = documentsServiceLocal.getStorageSize();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        companyMaxStorage = result[0];
        companyUsedStorage = result[1];

        StorageInfoTO storageInfo = new StorageInfoTO();
        storageInfo.setUsed(new BigDecimal(companyUsedStorage).setScale(2, RoundingMode.HALF_UP).doubleValue());
        storageInfo.setFree(new BigDecimal(companyMaxStorage - companyUsedStorage).setScale(2, RoundingMode.HALF_UP).doubleValue());

        return successResponse(storageInfo);
    }

    @Operation(summary = "Get Root Folder", description = """
            1) Requires root_type that should be one of the followings: SYSTEM_FOLDER, SHARED_WITH_ME, SHARED_BY_ME, MY_FOLDER, TRASH, PUBLIC_FOLDER.\s

             2) Returns list of files and folders with the provided root_type\s

            3) if response may be false due to the following errors:\s

             Session may be expired - Error Code - 3003\s

             Invalid Access token may be provided - Error Code - 3006\s

             Root Type may not be found - Error Code - 3005\s

             Internal Server Error occurred - Error Code - 3008\s

             Start point or Limit may be required - Error Code - 3007""")
    @RequestMapping(value = "/root_folder", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getRootFolder(@RequestBody RootFolderRequestTO rootFolderRequest) throws RestException, InsufficientPermissionsException, ObjectNotFoundException {
        if (StringUtils.isBlank(rootFolderRequest.getRoot_type())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Root type not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (rootFolderRequest.getStart() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Fetching folders and files start point required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (rootFolderRequest.getLimit() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Fetching folders and files limit required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setStart(rootFolderRequest.getStart());
        filterParameter.setLimit(rootFolderRequest.getLimit());
        filterParameter.setSearchKey(rootFolderRequest.getSearch_text());
        filterParameter.setFromMobile(true);

        if (RootFolderTypeEnum.SYSTEM_FOLDER.name().equalsIgnoreCase(rootFolderRequest.getRoot_type())) {
            return getSystemFolders(filterParameter);
        }
        if (RootFolderTypeEnum.PUBLIC_FOLDER.name().equalsIgnoreCase(rootFolderRequest.getRoot_type())) {
            return getPublicFolders(filterParameter);
        }
        if (RootFolderTypeEnum.SHARED_WITH_ME.name().equalsIgnoreCase(rootFolderRequest.getRoot_type())) {
            return getSharedWithMeFolders(filterParameter);
        }
        if (RootFolderTypeEnum.SHARED_BY_ME.name().equalsIgnoreCase(rootFolderRequest.getRoot_type())) {
            return getSharedByMeFolders(filterParameter);
        }
        if (RootFolderTypeEnum.MY_FOLDER.name().equalsIgnoreCase(rootFolderRequest.getRoot_type())) {
            return getMyFolders(filterParameter);
        }
        if (RootFolderTypeEnum.TRASH.name().equalsIgnoreCase(rootFolderRequest.getRoot_type())) {
            return getTrashFolders(filterParameter);
        }

        throw new RestException("Invalid root type: " + rootFolderRequest.getRoot_type(), "Invalid root type: " + rootFolderRequest.getRoot_type(), INVALID, HttpStatus.UNPROCESSABLE_ENTITY);

    }

    @Operation(summary = "Get Files", description = """
            1) Requires folder_id from which files will be retrieved, starting point and limit.\s

             2) The following possible errors may occur:\s

            Session may be expired - Error Code - 3003\s

             Invalid Access token may be provided - Error Code - 3006\s

            Internal Server Error occurred - Error Code - 3008\s

             Start point or Limit may be required - Error Code - 3007""")
    @RequestMapping(value = "/files", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getFiles(@RequestBody FolderRequestListTO requestListData) throws RestException, InsufficientPermissionsException, ObjectNotFoundException {
        if (requestListData.getStart() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Fetching files start point required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (requestListData.getLimit() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Fetching files limit required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setStart(requestListData.getStart());
        filterParameter.setLimit(requestListData.getLimit());
        filterParameter.setSearchKey(requestListData.getSearch_text());
        filterParameter.setFromMobile(true);
        if (requestListData.getFolder_id() != null && requestListData.getFolder_id() != 0) {
            filterParameter.setFolderId(requestListData.getFolder_id());
        } else {
            filterParameter.setAllFilesResource(true);
        }

        try {
            ListResult<FileResource> fileResourceListResult = documentsServiceLocal.listFilesAndFolders(filterParameter);
            ArrayList<FileTO> fileList = new ArrayList<>();

            for (FileResource fileResource : fileResourceListResult.getList()) {
                //Convert and add to list
                fileList.add(convert(fileResource));
            }

            return successResponse(new ResponseResultListData<>(fileList, fileResourceListResult.getTotal()));
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, (e.getMessage() != null ? e.getMessage() : e.toString()), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/history/logs", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getDocumentsHistoryLog(@RequestBody FolderRequestListTO requestListData) throws RestException, InsufficientPermissionsException, ObjectNotFoundException {
        if (requestListData.getFolder_id() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "folder_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (requestListData.getStart() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Fetching files start point required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (requestListData.getLimit() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Fetching files limit required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setStart(requestListData.getStart());
        filterParameter.setLimit(requestListData.getLimit());
        filterParameter.setSearchKey(requestListData.getSearch_text());
        filterParameter.setFromMobile(true);
        if (requestListData.getFolder_id() == 0) {
            filterParameter.setAllFilesResource(true);
            filterParameter.setFolderId(0);
        } else {
            filterParameter.setFolderId(requestListData.getFolder_id());
        }

        ListResult<FileResource> fileResourceListResult = null;
        try {
            fileResourceListResult = documentsServiceLocal.listFilesAndFolders(filterParameter);
        } catch (Exception e) {
            log.error("", e);
        }
        ArrayList<DocumentHistoryLogTO> historyLogs = new ArrayList<>();
        if (fileResourceListResult != null && fileResourceListResult.getList() != null) {
            fileResourceListResult.getList().forEach(fileResource -> {
                DocumentHistoryLogTO historyLog = new DocumentHistoryLogTO();
                historyLog.setId(fileResource.getObjectId());
                historyLog.setName(fileResource.getFileName());
                if (fileResource.getCreationDate() != null) {
                    historyLog.setCreated_date(longDateTimezoneFormat.format(fileResource.getCreationDate()));
                }
                if (fileResource.getModificationDate() != null) {
                    historyLog.setModified_date(longDateTimezoneFormat.format(fileResource.getModificationDate()));
                }
                historyLog.setIs_deleted(fileResource.isDeleted());
                historyLog.setIs_shared(fileResource.isShared());
                if (fileResource.isFolder()) {
                    historyLog.setIs_file(false);
                } else {
                    historyLog.setIs_file(true);
                }
                if (StringUtils.isNotBlank(fileResource.getCreatedBy())) {
                    historyLog.setCreated_by(fileResource.getCreatedBy());
                }
                if (fileResource.isFolder()) {
                    EdsFolder edsFolder = folderManager.get(fileResource.getObjectId());
                    if (edsFolder.getAuditInfo() != null && edsFolder.getAuditInfo().getModifiedBy() != null) {
                        EdsUser modifier = userManager.get(edsFolder.getAuditInfo().getModifiedBy().getObjectID());
                        if (modifier != null) {
                            historyLog.setModified_by(modifier.getFullName());
                        }
                    }
                } else {
                    EdsFileHeader edsFileHeader = fileHeaderManager.get(fileResource.getObjectId());
                    if (edsFileHeader.getAuditInfo() != null && edsFileHeader.getAuditInfo().getModifiedBy() != null) {
                        EdsUser modifier = userManager.get(edsFileHeader.getAuditInfo().getModifiedBy().getObjectID());
                        if (modifier != null) {
                            historyLog.setModified_by(modifier.getFullName());
                        }
                    }
                }
                historyLogs.add(historyLog);
            });
        }
        historyLogs.sort((o1, o2) -> {
            if (o1.getModified_date().equals(o2.getModified_date())) {
                return o2.getCreated_date().compareTo(o1.getCreated_date());
            }
            return o2.getModified_date().compareTo(o1.getModified_date());
        });
        return successResponse(new ResponseResultListData<>(historyLogs, fileResourceListResult != null ? fileResourceListResult.getTotal() : null));
    }

    @Operation(summary = "File upload", description = """
            1) Requires folder_id into which the file will uploaded.\s

            2) The following errors may occur during file upload process:\s

             Session may be expired - Error Code - 3003\s

             Invalid Access token may be provided - Error Code - 3006\s

            Invalid folder_id may be provided - Error Code - 3005\s

             Internal Server Error occurred - Error Code - 3008""")
    @RequestMapping(value = "/file_upload", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public Object fileUpload(@Parameter(description = "metadata") FileUploadTO metadata,
                             @RequestParam(value = "file") MultipartFile file) throws RestException {

        if (metadata.getFolder_id() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Folder is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (file == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "File is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        FolderResource folderResource = documentsServiceLocal.getFolderResource(metadata.getFolder_id());
        if (folderResource == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Folder with id " + metadata.getFolder_id() + " not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        FileResource result = documentsServiceLocal.saveDocumentFile(file, folderResource.getObjectId(), folderResource.getFileType(), null, metadata.getDescription());

        if (result != null) {
            return successResponse(convert(result));
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Couldn't save document.", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Save Google refresh token", description = "")
    @RequestMapping(value = "/save_google_refresh_token", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public Object saveGoogleRefreshToken(@RequestParam(value = "refresh_token") String refreshToken) throws RestException {
        if (StringUtils.isBlank(refreshToken)) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Refresh token is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        try {
            googleDocumentsService.saveToken(refreshToken);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return successResponse(new ResponseData());
    }

    @Operation(summary = "File upload to Google", description = """
            1) Requires folder_id into which the file will uploaded.\s

            2) The following errors may occur during file upload process:\s

             Session may be expired - Error Code - 3003\s

             Invalid Access token may be provided - Error Code - 3006\s

            Invalid folder_id may be provided - Error Code - 3005\s

             Internal Server Error occurred - Error Code - 3008\s

             User may not have permission to the google folder - Error Code - 3004""")
    @RequestMapping(value = "/file_upload_to_google", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public Object fileUploadToGoogle(@Parameter(description = "metadata") FileUploadTO metadata,
                                     @RequestParam(value = "file") MultipartFile file) throws RestException {

        if (metadata.getFolder_id() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Folder is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (file == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "File is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (!loginServiceLocal.isValid_User_For_Google_Gocs()) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "You need to authorize to your Google Documents and save a refresh token. Call to the '/save_google_refresh_token' method", ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }

        FolderResource folderResource = documentsServiceLocal.getFolderResource(metadata.getFolder_id());
        if (folderResource == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Folder with id " + metadata.getFolder_id() + " not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        FileResource result = documentsServiceLocal.saveDocumentFile(file, Constants.GOOGLE, folderResource.getObjectId(), folderResource.getFileType(), null, metadata.getDescription());

        if (result != null) {
            return successResponse(convert(result));
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Couldn't save document to Google.", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Get Folder", description = """
            1) Retrieves the folder which corresponds with the provided folder_id.\s

             2) The following error may occur during folder data retrieval:\s

            Session may be expired - Error Code - 3003\s

             Invalid Access token may be provided - Error Code - 3006\s

             Invalid folder_id may be provided - Error Code - 3005\s

            Internal Server Error occurred - Error Code - 3008\s

             User may not have permission to access the folder - Error Code - 3004""")
    @RequestMapping(value = "/folder/{id}", method = RequestMethod.GET)
    public Object getFolder(@PathVariable(value = "id") Integer id) throws RestException, InsufficientPermissionsException, ObjectNotFoundException {
        if (id == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Id field is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        EdsFolder edsFolder = folderManager.get(id);
        if (edsFolder == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Folder with id " + id + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        FolderResource folderResource = documentsServiceLocal.getFolderResource(id, folderManager.getUser());
        if (folderResource == null || folderResource.getObjectId() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Folder with id " + id + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        FileDetailTO file = new FileDetailTO();
        file.setFile_id(folderResource.getObjectId());
        file.setFile_name(folderResource.getName());
        file.setIs_folder(true);
        file.setFolder_id(getPatentId(folderResource.getParentId()));
        if (folderResource.getOwner() != null) {
            file.setOwner(new OwnerTO(folderResource.getOwner().getObjectId(), folderResource.getOwner().getName()));
        }
        if (folderResource.getCreationDate() != null) {
            file.setCreated_time(longDateTimezoneFormat.format(folderResource.getCreationDate()));
        }
        if (folderResource.getModificationDate() != null) {
            file.setUpdated_time(longDateTimezoneFormat.format(folderResource.getModificationDate()));
        }
        file.setIs_shared(folderResource.isShared());
        if (folderResource.getPermission() != null) {
            file.setCan_delete(folderResource.getPermission().isDelete());
            file.setCan_rename(folderResource.getPermission().isWrite());
            file.setCan_share(folderResource.getPermission().isModifyACL());
        }
        ArrayList<ParentFolderTO> parentFolders = new ArrayList<>();
        fillParentFolders(parentFolders, edsFolder);
        file.setParent_folders(parentFolders);
        ListingFilterParameter filterParameter = new ListingFilterParameter();
        filterParameter.setObjectId(folderResource.getObjectId());
        int file_size = 0;
        int sub_folder_size = 0;
        if (edsFolder.getFiles() != null) {
            file_size = edsFolder.getFiles().size();
            if (edsFolder.getSubfolders() != null) {
                sub_folder_size = edsFolder.getSubfolders().size();
            }
            file.setFile_count(Math.addExact(file_size, sub_folder_size));
        }

        return successResponse(file);
    }

    private void fillParentFolders(ArrayList<ParentFolderTO> parentFolders, EdsFolder edsFolder) {
        if (edsFolder.getParent() != null) {
            EdsFolder parent = edsFolder.getParent();
            parentFolders.add(new ParentFolderTO(parent.getObjectID(), parent.getName(), parent.getPathRaw()));
            if (parent.getParent() != null) {
                fillParentFolders(parentFolders, parent);
            }
        }
    }

    @Operation(summary = "Get File", description = """
            1) Retrieves the file which corresponds with the provided file_id.\s

             2) The following error may occur during file retrieval:\s

            Session may be expired - Error Code - 3003\s

             Invalid Access token may be provided - Error Code - 3006\s

             Invalid file_id may be provided - Error Code - 3005\s

            Internal Server Error occurred - Error Code - 3008\s

             User may not have permission to see the file - Error Code - 3004""")
    @RequestMapping(value = "/file/{id}", method = RequestMethod.GET)
    public Object getFile(@PathVariable(value = "id") Integer id) throws RestException, InsufficientPermissionsException, ObjectNotFoundException {
        if (id == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Id field required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        FileResource fileResource = documentsServiceLocal.getFileResource(id);
        if (fileResource != null) {
            return successResponse(convert(fileResource));
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "File with id " + id + " not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Create Folder", description = """
            1) Creates new folder inside the parent Folder which corresponds with the provided parent_id.\s

             2) The following errors may occur during folder creation:\s

            Session may be expired - Error Code - 3003\s

             Invalid Access token may be provided - Error Code - 3006\s

             Invalid parent_id may be provided - Error Code - 3005\s

            Internal Server Error occurred - Error Code - 3008\s

             User may not have permission to create the folder - Error Code - 3004""")
    @RequestMapping(value = "/create_folder", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object createFolder(@RequestBody FolderAddTO folderAdd) throws RestException {
        if (StringUtils.isBlank(folderAdd.getFolder_name())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Folder name is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (folderAdd.getParent_id() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "No parent specified", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        FolderResource folderResource;
        try {
            folderResource = documentsServiceLocal.createFolder(folderAdd.getParent_id(), folderAdd.getFolder_name());
            return successResponse(new FolderIdTO(folderResource.getObjectId()));
        } catch (InsufficientPermissionsException e) {
            log.error("", e);
            throw new RestException("You don't have the permissions to create this folder", "You don't have the permissions to create this folder", ACCESS_DENIED, HttpStatus.FORBIDDEN);
        } catch (ObjectNotFoundException e) {
            log.error("", e);
            throw new RestException("No parent specified", "No parent specified", NOT_FOUND, HttpStatus.NOT_FOUND);
        } catch (DuplicateNameException e) {
            log.error("", e);
            throw new RestException("A folder with this name " + folderAdd.getFolder_name() + " already exists at this level", "A folder with this name " + folderAdd.getFolder_name() + " already exists at this level", CONFLICT, HttpStatus.CONFLICT);
        }
    }

    @Operation(summary = "Update Folder", description = """
            1) Updates the folder which corresponds to the provided folder_id.\s

             2) The following errors may occur during folder update:\s

            Session may be expired - Error Code - 3003\s

             Invalid Access token may be provided - Error Code - 3006\s

             Invalid folder_id may be provided - Error Code - 3005\s

            Internal Server Error occurred - Error Code - 3008\s

             User may not have permission to update the folder - Error Code - 3004\s

             Provided folder name may already exist - Error Code - 3009""")
    @RequestMapping(value = "/update_folder", method = RequestMethod.PATCH, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object updateFolder(@RequestBody FolderUpdateTO updateFile) throws RestException {
        if (updateFile.getFolder_id() == null || updateFile.getFolder_id() == 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Id field is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(updateFile.getFolder_name())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Folder name is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        EdsFolder edsFolder = folderManager.get(updateFile.getFolder_id());
        if (edsFolder == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Folder with id " + updateFile.getFolder_id() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (EdsFolder.SYSTEM_BUILTIN == edsFolder.getType()) {
            throw new RestException("You can not modify root folder " + edsFolder.getName(), "You can not modify root folder " + edsFolder.getName(), ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }

        FolderResource folderResource = new FolderResource();
        folderResource.setObjectId(updateFile.getFolder_id());
        folderResource.setName(updateFile.getFolder_name());
        try {
            documentsServiceLocal.updateFolder(folderResource, null);
            return successResponse(new ResponseData());
        } catch (InsufficientPermissionsException e) {
            log.error("", e);
            throw new RestException("You don't have the permissions to modify this folder", "You don't have the permissions to modify this folder", ACCESS_DENIED, HttpStatus.FORBIDDEN);
        } catch (ObjectNotFoundException e) {
            log.error("", e);
            throw new RestException("No folder specified", "No folder specified", REQUIRED, HttpStatus.BAD_REQUEST);
        } catch (DuplicateNameException e) {
            log.error("", e);
            throw new RestException("A folder with the name '" + updateFile.getFolder_name() + "' already exists at this level", "A folder with the name '" + updateFile.getFolder_name() + "' already exists at this level", CONFLICT, HttpStatus.CONFLICT);
        }
    }

    @Operation(summary = "Delete File", description = """
            1) Moves the file with provided file_id to the trash.\s

             2) The following errors may occur during file deletion:\s

            Session may be expired - Error Code - 3003\s

             Invalid Access token may be provided - Error Code - 3006\s

             Invalid file_id may be provided - Error Code - 3005\s

            Internal Server Error occurred - Error Code - 3008\s

             User may not have permission to delete the file - Error Code - 3004""")
    @RequestMapping(value = "/delete_file/{file_id}", method = RequestMethod.DELETE)
    public Object deleteFile(@PathVariable(value = "file_id") Integer file_id) throws RestException {
        if (file_id == null || file_id == 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "File id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        EdsFileHeader edsFile = fileHeaderManager.get(file_id);
        if (edsFile == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "File with id " + file_id + " not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        try {
            documentsServiceLocal.moveFileToTrash(file_id);
            return successResponse(new ResponseData());
        } catch (InsufficientPermissionsException e) {
            log.error("", e);
            throw new RestException("You don't have the permissions to delete this file", "You don't have the permissions to delete this file", ACCESS_DENIED, HttpStatus.FORBIDDEN);
        } catch (ObjectNotFoundException e) {
            log.error("", e);
            throw new RestException("The specified file has no parent folder", "The specified file has no parent folder", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete File", description = """
            1) Deletes the file permanently with provided file_id.\s

             2) The following errors may occur during file deletion:\s

            Session may be expired - Error Code - 3003\s

             Invalid Access token may be provided - Error Code - 3006\s

             Invalid file_id may be provided - Error Code - 3005\s

            Internal Server Error occurred - Error Code - 3008\s

             User may not have permission to delete the file - Error Code - 3004""")
    @RequestMapping(value = "/delete_file_from_trash/{file_id}", method = RequestMethod.DELETE)
    public Object deleteFileFromTrash(@PathVariable(value = "file_id") Integer file_id) throws RestException {
        if (file_id == null || file_id == 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "File id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        EdsFileHeader edsFile = fileHeaderManager.get(file_id);
        if (edsFile == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "File with id " + file_id + " not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        try {
            documentsServiceLocal.deleteFile(file_id);
            return successResponse(new ResponseData());
        } catch (InsufficientPermissionsException e) {
            log.error("", e);
            throw new RestException("You don't have the permissions to delete this file", "You don't have the permissions to delete this file", ACCESS_DENIED, HttpStatus.FORBIDDEN);
        } catch (ObjectNotFoundException e) {
            log.error("", e);
            throw new RestException("The specified file has no parent folder", "The specified file has no parent folder", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/rename_file", method = RequestMethod.PATCH, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object renameFile(@RequestBody FileUpdateTO fileRename) throws RestException {
        if (fileRename.getFile_id() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "file_id field is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (fileRename.getFile_name() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "file_name field is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        EdsFileHeader edsFileHeader = fileHeaderManager.get(fileRename.getFile_id());
        if (edsFileHeader == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "File with id " + fileRename.getFile_id() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        try {
            documentsServiceLocal.updateFile(fileRename.getFile_id(), fileRename.getFile_name(), null, null);
            return successResponse(new ResponseData());
        } catch (InsufficientPermissionsException e) {
            log.error("", e);
            throw new RestException("You don't have the permissions to modify this file", "You don't have the permissions to modify this file", ACCESS_DENIED, HttpStatus.FORBIDDEN);
        } catch (ObjectNotFoundException e) {
            log.error("", e);
            throw new RestException("No file specified", "No file specified", REQUIRED, HttpStatus.BAD_REQUEST);
        } catch (DuplicateNameException e) {
            log.error("", e);
            throw new RestException("A file with the name " + fileRename.getFile_name() + " already exists at this level", "The file name already exists", CONFLICT, HttpStatus.CONFLICT);
        }
    }

    @RequestMapping(value = "/restore_file/{file_id}", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object restoreFile(@PathVariable(value = "file_id") Integer file_id) throws RestException {
        if (file_id == null || file_id == 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "file_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        EdsFileHeader fileHeader = fileHeaderManager.get(file_id);
        if (fileHeader == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "File with id " + file_id + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        try {
            documentsServiceLocal.removeFileFromTrash(file_id);
            return successResponse(new ResponseData());
        } catch (InsufficientPermissionsException e) {
            log.error("", e);
            throw new RestException("You don't have the permissions to restore this file", "You don't have the permissions to restore this file", ACCESS_DENIED, HttpStatus.FORBIDDEN);
        } catch (ObjectNotFoundException e) {
            log.error("", e);
            throw new RestException("The specified file has no parent folder", "The specified file has no parent folder", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/restore_folder/{folder_id}", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object restoreFolder(@PathVariable(value = "folder_id") Integer folder_id) throws RestException {
        if (folder_id == null || folder_id == 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "folder_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        EdsFolder edsFolder = folderManager.get(folder_id);
        if (edsFolder == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Folder with id " + folder_id + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        try {
            documentsServiceLocal.removeFolderFromTrash(folder_id);
            return successResponse(new ResponseData());
        } catch (InsufficientPermissionsException e) {
            log.error("", e);
            throw new RestException("You don't have the permissions to restore this folder", "You don't have the permissions to restore this folder", ACCESS_DENIED, HttpStatus.FORBIDDEN);
        } catch (ObjectNotFoundException e) {
            log.error("", e);
            throw new RestException("The specified file has no parent folder", "The specified file has no parent folder", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(value = "/clean_trash", method = RequestMethod.DELETE)
    public Object cleanTrashFolder() throws RestException {
        try {
            documentsServiceLocal.emptyTrash();
        } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return successResponse(new ResponseData());
    }

    @RequestMapping(value = "/copy_file", method = RequestMethod.PATCH, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object copyFile(@RequestBody FileMoveCopyTO copyFile) throws RestException {
        if (copyFile.getFile_id() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id field is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (copyFile.getFolder_id() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "folder_id field is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        EdsFileHeader edsFile = fileHeaderManager.get(copyFile.getFile_id());
        EdsFolder edsFolder = folderManager.get(copyFile.getFolder_id());
        if (edsFile == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "File with id " + copyFile.getFile_id() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (edsFolder == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Folder with id " + copyFile.getFolder_id() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        try {
            documentsServiceLocal.copyFile(copyFile.getFile_id(), copyFile.getFolder_id(), null);
            return successResponse(new ResponseData());
        } catch (InsufficientPermissionsException e) {
            log.error("", e);
            throw new RestException("You don't have the permissions to copy this file", "You don't have the permissions to copy this file", ACCESS_DENIED, HttpStatus.FORBIDDEN);
        } catch (ObjectNotFoundException e) {
            log.error("", e);
            throw new RestException("No file specified", "No file specified", REQUIRED, HttpStatus.BAD_REQUEST);
        } catch (DuplicateNameException e) {
            log.error("", e);
            throw new RestException("A file with the name already exists at this level", "The file with the same name already exists", CONFLICT, HttpStatus.CONFLICT);
        } catch (QuotaExceededException e) {
            throw new RestException("Quota limit has exceeded", "A quota limit has been exceeded", CONFLICT, HttpStatus.UPGRADE_REQUIRED);
        }
    }

    @RequestMapping(value = "/copy_folder", method = RequestMethod.PATCH, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object copyFolder(@RequestBody CopyMoveFolderTO copyFolder) throws RestException {
        if (copyFolder.getSelected_folder() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id field is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (copyFolder.getDestination_folder() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "folder_id field is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        EdsFolder selectedFolder = folderManager.get(copyFolder.getSelected_folder());
        EdsFolder destinationFolder = folderManager.get(copyFolder.getDestination_folder());

        if (selectedFolder == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Folder with id " + copyFolder.getSelected_folder() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (destinationFolder == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Parent Folder with id " + copyFolder.getDestination_folder() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        try {
            documentsServiceLocal.copyFolder(copyFolder.getSelected_folder(), copyFolder.getDestination_folder());
            return successResponse(new ResponseData());
        } catch (InsufficientPermissionsException e) {
            log.error("", e);
            throw new RestException("You don't have the permissions to copy this folder", "You don't have the permissions to copy this folder", ACCESS_DENIED, HttpStatus.FORBIDDEN);
        } catch (ObjectNotFoundException e) {
            log.error("", e);
            throw new RestException("No folder specified", "No folder specified", REQUIRED, HttpStatus.BAD_REQUEST);
        } catch (DuplicateNameException e) {
            log.error("", e);
            throw new RestException("A folder with the name already exists at this level", "The folder with the same name already exists", CONFLICT, HttpStatus.CONFLICT);
        } catch (QuotaExceededException e) {
            throw new RestException("Quota limit has exceeded", "A quota limit has been exceeded", CONFLICT, HttpStatus.UPGRADE_REQUIRED);
        }
    }

    @RequestMapping(value = "/move_file", method = RequestMethod.PATCH, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object moveFile(@RequestBody FileMoveCopyTO moveFile) throws RestException {
        if (moveFile.getFile_id() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id field is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (moveFile.getFolder_id() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "folder_id field is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        EdsFileHeader edsFile = fileHeaderManager.get(moveFile.getFile_id());
        EdsFolder edsFolder = folderManager.get(moveFile.getFolder_id());
        if (edsFile == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "File with id " + moveFile.getFile_id() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (edsFolder == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Folder with id " + moveFile.getFolder_id() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        try {
            documentsServiceLocal.moveFile(moveFile.getFile_id(), moveFile.getFolder_id());
            return successResponse(new ResponseData());
        } catch (InsufficientPermissionsException e) {
            log.error("", e);
            throw new RestException("You don't have the permissions to copy this file", "You don't have the permissions to copy this file", ACCESS_DENIED, HttpStatus.FORBIDDEN);
        } catch (ObjectNotFoundException e) {
            log.error("", e);
            throw new RestException("No file specified", "No file specified", REQUIRED, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/move_folder", method = RequestMethod.PATCH, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object moveFolder(@RequestBody CopyMoveFolderTO moveFolder) throws RestException {
        if (moveFolder.getSelected_folder() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "item_id field is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (moveFolder.getDestination_folder() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "folder_id field is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        EdsFolder selectedFolder = folderManager.get(moveFolder.getSelected_folder());
        EdsFolder destinationFolder = folderManager.get(moveFolder.getDestination_folder());

        if (selectedFolder == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Folder with id " + moveFolder.getSelected_folder() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (destinationFolder == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Parent Folder with id " + moveFolder.getDestination_folder() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        try {
            documentsServiceLocal.moveFolder(moveFolder.getSelected_folder(), moveFolder.getDestination_folder());
            return successResponse(new ResponseData());
        } catch (InsufficientPermissionsException e) {
            log.error("", e);
            throw new RestException("You don't have the permissions to copy this folder", "You don't have the permissions to copy this folder", ACCESS_DENIED, HttpStatus.FORBIDDEN);
        } catch (DuplicateNameException e) {
            log.error("", e);
            throw new RestException("A folder with the name already exists at this level", "The folder with the same name already exists", CONFLICT, HttpStatus.CONFLICT);
        }
    }

    @Operation(summary = "Delete Folder", description = """
            1) Moves the folder with provided folder_id to the TrashBin.\s

             2) The following errors may occur during folder deletion:\s

            Session may be expired - Error Code - 3003\s

             Invalid Access token may be provided - Error Code - 3006\s

             Invalid folder_id may be provided - Error Code - 3005\s

            Internal Server Error occurred - Error Code - 3008\s

             User may not have permission to delete the folder - Error Code - 3004""")
    @RequestMapping(value = "/delete_folder/{folder_id}", method = RequestMethod.DELETE)
    public Object deleteFolder(@PathVariable(value = "folder_id") Integer folder_id) throws RestException {
        if (folder_id == null || folder_id == 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Folder id field is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        EdsFolder edsFolder = folderManager.get(folder_id);
        if (edsFolder == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Folder with id " + folder_id + " not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (EdsFolder.SYSTEM_BUILTIN == edsFolder.getType()) {
            throw new RestException("You can not delete root folder " + edsFolder.getName(), "You can not delete root folder " + edsFolder.getName(), ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }
        try {
            documentsServiceLocal.moveFolderToTrash(folder_id);
            return successResponse(new ResponseData());
        } catch (InsufficientPermissionsException e) {
            log.error("", e);
            throw new RestException("You don't have the permissions to delete this folder", "You don't have the permissions to delete this folder", ACCESS_DENIED, HttpStatus.FORBIDDEN);
        } catch (ObjectNotFoundException e) {
            log.error("", e);
            throw new RestException("Deleting the root folder is not allowed", "Deleting the root folder is not allowed", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete Folder", description = """
            1) Deletes the folder with provided folder_id permanently.\s

             2) The following errors may occur during folder deletion:\s

            Session may be expired - Error Code - 3003\s

             Invalid Access token may be provided - Error Code - 3006\s

             Invalid folder_id may be provided - Error Code - 3005\s

            Internal Server Error occurred - Error Code - 3008\s

             User may not have permission to delete the folder - Error Code - 3004""")
    @RequestMapping(value = "/delete_folder_from_trash/{folder_id}", method = RequestMethod.DELETE)
    public Object deleteFolderFromTrash(@PathVariable(value = "folder_id") Integer folder_id) throws RestException {
        if (folder_id == null || folder_id == 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Folder id field is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        EdsFolder edsFolder = folderManager.get(folder_id);
        if (edsFolder == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Folder with id " + folder_id + " not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        if (EdsFolder.SYSTEM_BUILTIN == edsFolder.getType()) {
            throw new RestException("You can not delete root folder " + edsFolder.getName(), "You can not delete root folder " + edsFolder.getName(), ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }
        try {
            documentsServiceLocal.deleteFolder(folder_id);
            return successResponse(new ResponseData());
        } catch (InsufficientPermissionsException e) {
            log.error("", e);
            throw new RestException("You don't have the permissions to delete this folder", "You don't have the permissions to delete this folder", ACCESS_DENIED, HttpStatus.FORBIDDEN);
        } catch (ObjectNotFoundException e) {
            log.error("", e);
            throw new RestException("Deleting the root folder is not allowed", "Deleting the root folder is not allowed", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Get File Permissions", description = """
            1) Retrieves data list that gives information on who can implement modifications to specified file\s

            2) The following errors may occur during file permission data retrieval:\s

            Session may be expired - Error Code - 3003\s

             Invalid Access token may be provided - Error Code - 3006\s

             Invalid file_id may be provided - Error Code - 3005\s

            Internal Server Error occurred - Error Code - 3008""")
    @RequestMapping(value = "/file_permissions/{file_id}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getFilePermissions(@PathVariable(value = "file_id") Integer fileId) throws RestException, InsufficientPermissionsException, ObjectNotFoundException {
        if (fileId == null || fileId <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "file_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        try {
            //Getting File RBAC permissions
            HashSet<PermissionHolder> filePermissions = documentsServiceLocal.getFilePermissions(fileId);
            ArrayList<PermissionHolderTO> filePermissionsList = new ArrayList<>();

            for (PermissionHolder permissionHolder : filePermissions) {
                //Convert and add to list
                filePermissionsList.add(convert(permissionHolder));
            }

            return successResponse(new ResponseResultListData<>(filePermissionsList, filePermissionsList.size()));
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, (e.getMessage() != null ? e.getMessage() : e.toString()), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Remove File Permissions", description = "Removes permission holder based on the file_id and permission_holder_id \n" +
            "permission_holder_id can be retrieved from file permissions list. ")
    @RequestMapping(value = "/remove_file_permissions", method = RequestMethod.DELETE, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object removeFilePermissions(@RequestBody RemoveFilePermissionTO permHolder) throws RestException {
        if (permHolder.getFile_id() == null || permHolder.getFile_id() <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "file_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (permHolder.getPermission_holder_id() == null || permHolder.getPermission_holder_id() <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "permission_holder_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        HashSet<PermissionHolder> permissionHolders = documentsServiceLocal.getFilePermissions(permHolder.getFile_id());
        ArrayList<PermissionHolder> newPermissionHolders = new ArrayList<>();
        if (permissionHolders != null) {
            for (PermissionHolder permissionHolder : permissionHolders) {
                if (!permissionHolder.getObjectId().equals(permHolder.getPermission_holder_id())) {
                    newPermissionHolders.add(permissionHolder);
                }
            }
        }

        try {
            documentsServiceLocal.updateFile(permHolder.getFile_id(), null, false, newPermissionHolders);
            return successResponse(new ResponseData());
        } catch (ObjectNotFoundException e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, "File with provided file_id is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        } catch (DuplicateNameException | InsufficientPermissionsException e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, "You don't have necessary permission for this operation", ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }
    }

    @Operation(summary = "Get Folder Permissions", description = """
            1) Retrieves data list that gives information on who can implement modifications to specified folder\s

            2) The following errors may occur during folder permission data retrieval:\s

            Session may be expired - Error Code - 3003\s

             Invalid Access token may be provided - Error Code - 3006\s

             Invalid folder_id may be provided - Error Code - 3005\s

            Internal Server Error occurred - Error Code - 3008""")
    @RequestMapping(value = "/folder_permissions/{folder_id}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object getFolderPermissions(@PathVariable(value = "folder_id") Integer folderId) throws RestException, InsufficientPermissionsException, ObjectNotFoundException {
        if (folderId == null || folderId <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "folder_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        try {
            //Getting Folder RBAC permissions
            HashSet<PermissionHolder> folderPermissions = documentsServiceLocal.getFolderPermissions(folderId);

            ArrayList<PermissionHolderTO> folderPermissionsList = new ArrayList<>();

            for (PermissionHolder permissionHolder : folderPermissions) {
                //Convert and add to list
                folderPermissionsList.add(convert(permissionHolder));
            }
            return successResponse(new ResponseResultListData<>(folderPermissionsList, folderPermissionsList.size()));
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, (e.getMessage() != null ? e.getMessage() : e.toString()), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Remove Folder Permissions", description = "Removes permission holder based on the file_id and permission_holder_id \n" +
            "permission_holder_id can be retrieved from folder permissions list. ")
    @RequestMapping(value = "/remove_folder_permissions", method = RequestMethod.DELETE, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object removeFolderPermissions(@RequestBody RemoveFolderPermissionTO permHolder) throws RestException {

        if (permHolder.getFolder_id() == null || permHolder.getFolder_id() <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "folder_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        if (permHolder.getPermission_holder_id() == null || permHolder.getPermission_holder_id() <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "permission_holder_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        FolderResource folderResource = documentsServiceLocal.getFolderResource(permHolder.getFolder_id());
        if (folderResource == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Folder with id " + permHolder.getFolder_id() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        HashSet<PermissionHolder> permissionHolders;
        try {
            permissionHolders = documentsServiceLocal.getFolderPermissions(permHolder.getFolder_id());
        } catch (ObjectNotFoundException e) {
            log.error(e.getMessage());
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (InsufficientPermissionsException e) {
            log.error(e.getMessage());
            throw new RestException(GENERAL_ERROR_MESSAGE, "You don't have necessary permission for this operation", ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }

        ArrayList<PermissionHolder> newPermissionHolders = new ArrayList<>();
        if (permissionHolders != null) {
            for (PermissionHolder permissionHolder : permissionHolders) {
                if (!permissionHolder.getObjectId().equals(permHolder.getPermission_holder_id())) {
                    newPermissionHolders.add(permissionHolder);
                }
            }
        }

        try {
            documentsServiceLocal.updateFolder(folderResource, newPermissionHolders);
            return successResponse(new ResponseData());
        } catch (ObjectNotFoundException e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, "Folder with provided folder_id is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        } catch (DuplicateNameException | InsufficientPermissionsException e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, "You don't have necessary permission for this operation", ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }
    }

    @Operation(summary = "Set Folder Permissions", description = "object_type should be Group, Customer, User or Supplier")
    @RequestMapping(value = "/set_file_permissions", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object saveFilePermissions(@RequestBody PermissionHolderRequestTO request) throws RestException {
        if (request.getFile_id() == null || request.getFile_id() <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "file_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(request.getObject_type())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "object type is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (request.getObject_ids() == null || request.getObject_ids().size() <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "at least one object_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        EdsFileHeader file = fileHeaderManager.get(request.getFile_id());
        if (file == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Specified file is not found.", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        HashSet<PermissionHolder> existingPermissionHolders = documentsServiceLocal.getFilePermissions(request.getFile_id());

        if (DocumentsUserTypeEnum.Customer.toString().equals(request.getObject_type()) || DocumentsUserTypeEnum.User.toString().equals(request.getObject_type()) ||
                DocumentsUserTypeEnum.Supplier.toString().equals(request.getObject_type())) {
            for (Integer userId : request.getObject_ids()) {
                if (existingPermissionHolders != null) {
                    for (PermissionHolder existingPermissionHolder : existingPermissionHolders) {
                        if (existingPermissionHolder.getUser() != null && existingPermissionHolder.getUser().getObjectId().equals(userId)) {
                            throw new RestException(GENERAL_ERROR_MESSAGE, " The member has already had access to the resource", CONFLICT, HttpStatus.CONFLICT);
                        }
                    }

                }
                PermissionHolder permissionHolder = new PermissionHolder();
                EdsUser user = userManager.get(userId);
                UserResource userResource = new UserResource();
                userResource.setObjectId(user.getObjectID());
                userResource.setName(user.getName());
                permissionHolder.setUser(userResource);
                permissionHolder.setCanChange(request.isCan_change());
                permissionHolder.setDelete(request.isDelete());
                permissionHolder.setRead(request.isRead());
                permissionHolder.setWrite(request.isWrite());
                permissionHolder.setModifyACL(request.isModify_acl());

                if (existingPermissionHolders != null) {
                    existingPermissionHolders.add(permissionHolder);
                }
            }
        } else if (DocumentsUserTypeEnum.Group.toString().equals(request.getObject_type())) {
            for (Integer groupId : request.getObject_ids()) {
                if (existingPermissionHolders != null) {
                    for (PermissionHolder existingPermissionHolder : existingPermissionHolders) {
                        if (existingPermissionHolder != null && existingPermissionHolder.getGroup() != null && existingPermissionHolder.getGroup().getGroupID().equals(groupId)) {
                            throw new RestException(GENERAL_ERROR_MESSAGE, "Group has already had access to the resource", CONFLICT, HttpStatus.CONFLICT);
                        }
                    }

                }
                PermissionHolder permissionHolder = new PermissionHolder();
                EdsGroup edsGroup = groupManager.get(groupId);
                GroupMembersViewItem group = new GroupMembersViewItem();
                group.setGroupID(edsGroup.getObjectID());
                group.setGroupName(edsGroup.getName());
                permissionHolder.setGroup(group);
                permissionHolder.setCanChange(request.isCan_change());
                permissionHolder.setDelete(request.isDelete());
                permissionHolder.setRead(request.isRead());
                permissionHolder.setWrite(request.isWrite());
                permissionHolder.setModifyACL(request.isModify_acl());
                if (existingPermissionHolders != null) {
                    existingPermissionHolders.add(permissionHolder);
                }
            }
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "object type should be Group, Customer, User, Supplier", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        List<PermissionHolder> permissionHolderList = null;
        if (existingPermissionHolders != null) {
            permissionHolderList = new ArrayList<>(existingPermissionHolders);
        }
        try {
            documentsServiceLocal.setFilePermissions(file, permissionHolderList, false);
            return successResponse(new ResponseData());
        } catch (InsufficientPermissionsException e) {
            log.error("", e);
            throw new RestException("You don't have the permissions to change this folder", "You don't have the permissions to change this folder", ACCESS_DENIED, HttpStatus.FORBIDDEN);
        } catch (ObjectNotFoundException e) {
            log.error("", e);
            throw new RestException("No parent specified", "No parent specified", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

    }

    @Operation(summary = "Update File Permissions", description = "object_type should be Group, Customer, User or Supplier")
    @RequestMapping(value = "/update_file_permissions", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object updateFilePermissions(@RequestBody PermissionUpdateTO request) throws RestException {
        if (request.getFile_id() == null || request.getFile_id() <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "file_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (request.getObject_ids() == null || request.getObject_ids().size() <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "at least one object_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        EdsFileHeader file = fileHeaderManager.get(request.getFile_id());
        if (file == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Specified file is not found.", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        HashSet<PermissionHolder> existingPermissionHolders = documentsServiceLocal.getFilePermissions(request.getFile_id());
        for (Integer permissionId : request.getObject_ids()) {
            if (existingPermissionHolders != null) {
                for (PermissionHolder existingPermissionHolder : existingPermissionHolders) {
                    if (existingPermissionHolder != null && existingPermissionHolder.getObjectId().equals(permissionId)) {
                        existingPermissionHolder.setDelete(request.isDelete());
                        existingPermissionHolder.setRead(request.isRead());
                        existingPermissionHolder.setWrite(request.isWrite());
                        existingPermissionHolder.setModifyACL(request.isModify_acl());

                        existingPermissionHolders.add(existingPermissionHolder);
                    }
                }
            }
        }
        ArrayList<PermissionHolder> permissionHolderList = null;
        if (existingPermissionHolders != null) {
            permissionHolderList = new ArrayList<>(existingPermissionHolders);
        }
        try {
            documentsServiceLocal.updateFile(request.getFile_id(), null, false, permissionHolderList);
            return successResponse(new ResponseData());
        } catch (ObjectNotFoundException e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, "File with provided file_id is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        } catch (DuplicateNameException | InsufficientPermissionsException e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, "You don't have necessary permission for this operation", ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }

    }

    @Operation(summary = "Update Folder Permissions", description = "object_type should be Group, Customer, User or Supplier")
    @RequestMapping(value = "/update_folder_permissions", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object updateFolderPermissions(@RequestBody PermissionUpdateTO request) throws RestException {
        if (request.getFile_id() == null || request.getFile_id() <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "valid file_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (request.getObject_ids() == null || request.getObject_ids().size() <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "at least one object_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        FolderResource folderResource = documentsServiceLocal.getFolderResource(request.getFile_id());
        if (folderResource == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Folder with id " + request.getFile_id() + " is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        HashSet<PermissionHolder> existingPermissionHolders;
        try {
            existingPermissionHolders = documentsServiceLocal.getFolderPermissions(request.getFile_id());
        } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        for (Integer permissionId : request.getObject_ids()) {
            if (existingPermissionHolders != null) {
                for (PermissionHolder existingPermissionHolder : existingPermissionHolders) {
                    if (existingPermissionHolder != null && existingPermissionHolder.getObjectId().equals(permissionId)) {
                        existingPermissionHolder.setDelete(request.isDelete());
                        existingPermissionHolder.setRead(request.isRead());
                        existingPermissionHolder.setWrite(request.isWrite());
                        existingPermissionHolder.setModifyACL(request.isModify_acl());

                        existingPermissionHolders.add(existingPermissionHolder);
                    }
                }

            }
        }
        ArrayList<PermissionHolder> permissionHolderList = null;
        if (existingPermissionHolders != null) {
            permissionHolderList = new ArrayList<>(existingPermissionHolders);
        }
        try {
            documentsServiceLocal.updateFolder(folderResource, permissionHolderList);
            return successResponse(new ResponseData());
        } catch (ObjectNotFoundException e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, "File with provided file_id is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        } catch (DuplicateNameException | InsufficientPermissionsException e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, "You don't have necessary permission for this operation", ACCESS_DENIED, HttpStatus.FORBIDDEN);
        }

    }

    @Operation(summary = "Set Folder Permissions", description = "object_type should be Group, Customer, User or Supplier")
    @RequestMapping(value = "/set_folder_permissions", method = RequestMethod.PUT, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object saveFolderPermissions(@RequestBody PermissionHolderRequestTO request) throws RestException {
        if (request.getFile_id() == null || request.getFile_id() <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "file_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (StringUtils.isBlank(request.getObject_type())) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "object type is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (request.getObject_ids() == null || request.getObject_ids().size() <= 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "at least one object_id is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }

        EdsFolder folder = folderManager.get(request.getFile_id());
        if (folder == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Specified folder is not found.", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        HashSet<PermissionHolder> existingPermissionHolders;
        try {
            existingPermissionHolders = documentsServiceLocal.getFolderPermissions(request.getFile_id());
        } catch (ObjectNotFoundException | InsufficientPermissionsException e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        if (DocumentsUserTypeEnum.Customer.toString().equals(request.getObject_type()) || DocumentsUserTypeEnum.User.toString().equals(request.getObject_type()) ||
                DocumentsUserTypeEnum.Supplier.toString().equals(request.getObject_type())) {
            for (Integer userId : request.getObject_ids()) {
                if (existingPermissionHolders != null) {
                    for (PermissionHolder existingPermissionHolder : existingPermissionHolders) {
                        if (existingPermissionHolder.getUser() != null && existingPermissionHolder.getUser().getObjectId().equals(userId)) {
                            throw new RestException(GENERAL_ERROR_MESSAGE, " The member has already had access to the resource", CONFLICT, HttpStatus.CONFLICT);
                        }
                    }

                }
                PermissionHolder permissionHolder = new PermissionHolder();
                EdsUser user = userManager.get(userId);
                UserResource userResource = new UserResource();
                userResource.setObjectId(user.getObjectID());
                userResource.setName(user.getName());
                permissionHolder.setUser(userResource);
                permissionHolder.setCanChange(request.isCan_change());
                permissionHolder.setDelete(request.isDelete());
                permissionHolder.setRead(request.isRead());
                permissionHolder.setWrite(request.isWrite());
                permissionHolder.setModifyACL(request.isModify_acl());

                if (existingPermissionHolders != null) {
                    existingPermissionHolders.add(permissionHolder);
                }
            }
        } else if (DocumentsUserTypeEnum.Group.toString().equals(request.getObject_type())) {
            for (Integer groupId : request.getObject_ids()) {
                if (existingPermissionHolders != null) {
                    for (PermissionHolder existingPermissionHolder : existingPermissionHolders) {
                        if (existingPermissionHolder != null && existingPermissionHolder.getGroup() != null && existingPermissionHolder.getGroup().getGroupID().equals(groupId)) {
                            throw new RestException(GENERAL_ERROR_MESSAGE, "Group has already had access to the resource", CONFLICT, HttpStatus.CONFLICT);
                        }
                    }

                }
                PermissionHolder permissionHolder = new PermissionHolder();
                EdsGroup edsGroup = groupManager.get(groupId);
                GroupMembersViewItem group = new GroupMembersViewItem();
                group.setGroupID(edsGroup.getObjectID());
                group.setGroupName(edsGroup.getName());
                permissionHolder.setGroup(group);
                permissionHolder.setCanChange(request.isCan_change());
                permissionHolder.setDelete(request.isDelete());
                permissionHolder.setRead(request.isRead());
                permissionHolder.setWrite(request.isWrite());
                permissionHolder.setModifyACL(request.isModify_acl());
                if (existingPermissionHolders != null) {
                    existingPermissionHolders.add(permissionHolder);
                }
            }
        } else {
            throw new RestException(GENERAL_ERROR_MESSAGE, "object type should be Group, Customer, User, Supplier", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }
        List<PermissionHolder> permissionHolderList = null;
        if (existingPermissionHolders != null) {
            permissionHolderList = new ArrayList<>(existingPermissionHolders);
        }
        try {
            final EdsUser user = folderManager.getUser();
            documentsServiceLocal.setFolderPermissions(user, folder, permissionHolderList, false);
            return successResponse(new ResponseData());
        } catch (InsufficientPermissionsException e) {
            log.error("", e);
            throw new RestException("You don't have the permissions to change this folder", "You don't have the permissions to change this folder", ACCESS_DENIED, HttpStatus.FORBIDDEN);
        } catch (ObjectNotFoundException e) {
            log.error("", e);
            throw new RestException("No parent specified", "No parent specified", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

    }

    @RequestMapping(value = "/list_groups", method = RequestMethod.GET)
    public Object getGroupList() throws RestException {
        ArrayList<CategoryTO> userGroups = new ArrayList<>();
        ArrayList<GroupMembersViewItem> groupMembersViewItems;
        try {
            groupMembersViewItems = rbacService.getCompanyGroupsWithMembers();
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        if (groupMembersViewItems != null) {
            groupMembersViewItems.forEach(groupList -> {
                CategoryTO group = new CategoryTO();
                group.setId(groupList.getGroupID());
                group.setTitle(groupList.getGroupName());
                userGroups.add(group);
            });
        }
        return successResponse(new UserGroupsResultTO(userGroups));
    }

    @Operation(summary = "Checking access to file manager and docs")
    @RequestMapping(value = "/documents_menu_access", method = RequestMethod.GET)
    public Object getDocumentSectionAccess() throws RestException {
        ArrayList<DocumentSectionPermissionTO> permissions = new ArrayList<>();

        boolean hasMainMenuPermission = ServerUtils.hasPermission(PermissionConstants.DOCUMENTS_MAIN_MENU);
        boolean isClient = userManager.getUser().hasRole(Constants.CLIENT_CODE);

        DocumentSectionPermissionTO permission = new DocumentSectionPermissionTO();
        permission.setTitle("Documents");
        permission.setCode(RootFolderTypeEnum.DOCUMENT.name());
        permission.setHas_access(hasMainMenuPermission && !isClient);
        permissions.add(permission);

        permission = new DocumentSectionPermissionTO();
        permission.setTitle("My Folders");
        permission.setCode(RootFolderTypeEnum.MY_FOLDER.name());
        permission.setHas_access(hasMainMenuPermission && !isClient);
        permissions.add(permission);

        permission = new DocumentSectionPermissionTO();
        permission.setTitle("System Folder");
        permission.setCode(RootFolderTypeEnum.SYSTEM_FOLDER.name());
        permission.setHas_access(hasMainMenuPermission && !isClient);
        permissions.add(permission);

        permission = new DocumentSectionPermissionTO();
        permission.setTitle("Shared By Me");
        permission.setCode(RootFolderTypeEnum.SHARED_BY_ME.name());
        permission.setHas_access(hasMainMenuPermission && !isClient);
        permissions.add(permission);

        permission = new DocumentSectionPermissionTO();
        permission.setTitle("Shared With Me");
        permission.setCode(RootFolderTypeEnum.SHARED_WITH_ME.name());
        permission.setHas_access(hasMainMenuPermission);
        permissions.add(permission);


        return successResponse(new ResponseListData<>(permissions));
    }

    @Operation(summary = "Check Access", description = """
            1) Retrieves data list on who can have access to the specified files and folders\s

             2) The following errors may occur during access check\s

            Session may be expired - Error Code - 3003\s

             Invalid Access token may be provided - Error Code - 3006\s

             Specified files and folders may not be found - Error Code - 3005\s

            Internal Server Error occurred - Error Code - 3008""")
    @RequestMapping(value = "/check_access", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object checkAccess(@RequestBody CheckAccessRequest request) throws RestException {

        EdsUser user = fileHeaderManager.getUser();
        ArrayList<FileTO> fileList = new ArrayList<>();

        //Check File Access
        if (request.getFiles() != null) {
            for (Integer fileId : request.getFiles()) {
                EdsFileHeader file = fileHeaderManager.get(fileId);
                if (file != null) {
                    // Check permissions.
                    EdsDocumentPermission permission = folderRbacManager.getFilePermissionForUser(file, user);
                    if (permission != null) {
                        FileResource fileResource = documentsServiceLocal.getFileResource(fileId);
                        if (fileResource != null) {
                            fileList.add(convert(fileResource));
                        }
                    }
                }
            }
        }

        //Check Folder Access
        if (request.getFolders() != null) {
            for (Integer folderId : request.getFolders()) {
                EdsFolder folder = folderManager.get(folderId);
                if (folder != null) {
                    // Check permissions.
                    EdsDocumentPermission permission = folderRbacManager.getFolderPermissionForUser(folder, user);
                    if (permission != null) {
                        FolderResource folderResource = documentsServiceLocal.getFolderResource(folderId);
                        if (folderResource != null) {
                            fileList.add(convert(folderResource));
                        }
                    }
                }
            }
        }

        return successResponse(new ResponseResultListData<>(fileList, fileList.size()));
    }

    private FileTO convert(FileResource fileResource) {
        FileTO fileTO = new FileTO();
        fileTO.setFile_id(fileResource.getObjectId());
        fileTO.setFile_name(fileResource.getFileName());
        if (fileResource.getOwner() != null) {
            fileTO.setOwner(new OwnerTO(fileResource.getOwner().getObjectId(), fileResource.getOwner().getName()));
        }
        if (fileResource.isFolder()) {
            fileTO.setIs_folder(true);
            fileTO.setFolder_id(getPatentId(fileResource.getFolderResource().getParentId()));
            if (fileResource.getFolderResource() != null && fileResource.getFolderResource().getCreationDate() != null) {
                fileTO.setCreated_time(longDateTimezoneFormat.format(fileResource.getFolderResource().getCreationDate()));
            }
            if (fileResource.getFolderResource() != null && fileResource.getFolderResource().getModificationDate() != null) {
                fileTO.setUpdated_time(longDateTimezoneFormat.format(fileResource.getFolderResource().getModificationDate()));
            }
            if (fileResource.getFolderResource() != null) {
                EdsFolder edsFolder = folderManager.get(fileResource.getFolderResource().getObjectId());
                int file_size = 0;
                int sub_folder_size = 0;
                if (edsFolder != null && edsFolder.getFiles() != null) {
                    file_size = edsFolder.getFiles().size();
                    if (edsFolder.getSubfolders() != null) {
                        sub_folder_size = edsFolder.getSubfolders().size();
                    }
                    fileTO.setFile_count(Math.addExact(file_size, sub_folder_size));
                }
            }
            fileTO.setIs_shared(fileResource.isShared());
            if (fileResource.getFolderResource() != null && fileResource.getFolderResource().getPermission() != null) {
                fileTO.setCan_delete(fileResource.getFolderResource().getPermission().isDelete());
                fileTO.setCan_rename(fileResource.getFolderResource().getPermission().isWrite());
                fileTO.setCan_share(fileResource.getFolderResource().getPermission().isModifyACL());
            }
        } else {
            fileTO.setIs_folder(false);
            fileTO.setFolder_id(getPatentId(fileResource.getFolderId()));
            fileTO.setFile_size(fileResource.getContentLength());
            fileTO.setFile_content_type(fileResource.getContentType());
            fileTO.setFile_url(fileResource.getDownloadUrl());
            if (fileResource.getCreationDate() != null) {
                fileTO.setCreated_time(longDateTimezoneFormat.format(fileResource.getCreationDate()));
            }
            if (fileResource.getModificationDate() != null) {
                fileTO.setUpdated_time(longDateTimezoneFormat.format(fileResource.getModificationDate()));
            }
            fileTO.setIs_shared(fileResource.isShared());
            if (fileResource.getPermission() != null) {
                fileTO.setCan_delete(fileResource.getPermission().isDelete());
                fileTO.setCan_rename(fileResource.getPermission().isWrite());
                fileTO.setCan_share(fileResource.getPermission().isModifyACL());
            }
        }
        return fileTO;
    }

    private FileTO convert(FolderResource folderResource) {
        FileTO file = new FileTO();
        file.setFile_id(folderResource.getObjectId());
        file.setFile_name(folderResource.getName());
        file.setIs_folder(true);
        file.setFolder_id(getPatentId(folderResource.getParentId()));
        if (folderResource.getCreationDate() != null) {
            file.setCreated_time(longDateTimezoneFormat.format(folderResource.getCreationDate()));
        }
        if (folderResource.getModificationDate() != null) {
            file.setUpdated_time(longDateTimezoneFormat.format(folderResource.getModificationDate()));
        }
        if (folderResource.getOwner() != null) {
            file.setOwner(new OwnerTO(folderResource.getOwner().getObjectId(), folderResource.getOwner().getName()));
        }
        file.setIs_shared(folderResource.isShared());
        if (folderResource.getPermission() != null) {
            file.setCan_delete(folderResource.getPermission().isDelete());
            file.setCan_rename(folderResource.getPermission().isWrite());
            file.setCan_share(folderResource.getPermission().isModifyACL());
        }
        return file;
    }

    private PermissionHolderTO convert(PermissionHolder permissionHolder) {
        if (permissionHolder != null) {
            PermissionHolderTO permissionHolderTO = new PermissionHolderTO();
            permissionHolderTO.setId(permissionHolder.getObjectId());
            permissionHolderTO.setRelationship(permissionHolder.getRelationship());
            if (permissionHolder.getGroup() != null) {
                permissionHolderTO.setGroup(new SelectItemTO(permissionHolder.getGroup().getGroupID(),
                        permissionHolder.getGroup().getGroupName(), permissionHolder.getGroup().getGroupConstantName()));
            }
            if (permissionHolder.getUser() != null) {
                EmployeeTO userTO = new EmployeeTO();
                userTO.setId(permissionHolder.getUser().getObjectId());
                userTO.setName(permissionHolder.getUser().getFullName());
                permissionHolderTO.setUser(userTO);
            }
            permissionHolderTO.setCan_change(permissionHolder.isCanChange());
            permissionHolderTO.setDelete(permissionHolder.isDelete());
            permissionHolderTO.setRead(permissionHolder.isRead());
            permissionHolderTO.setWrite(permissionHolder.isWrite());
            permissionHolderTO.setModify_acl(permissionHolder.isModifyACL());
            return permissionHolderTO;
        } else {
            return null;
        }
    }

    private PermissionHolder convert(PermissionHolderTO permissionHolderTO) {
        if (permissionHolderTO != null) {
            PermissionHolder result = new PermissionHolder();
            result.setObjectId(permissionHolderTO.getId());
            result.setRelationship(permissionHolderTO.getRelationship());
            if (permissionHolderTO.getGroup() != null) {
                GroupMembersViewItem group = new GroupMembersViewItem();
                group.setGroupID(permissionHolderTO.getGroup().getId());
                result.setGroup(group);
            }
            if (permissionHolderTO.getUser() != null) {
                UserResource userResource = new UserResource();
                userResource.setObjectId(permissionHolderTO.getUser().getId());
                userResource.setName(permissionHolderTO.getUser().getName());
                result.setUser(userResource);
            }
            result.setCanChange(permissionHolderTO.isCan_change());
            result.setDelete(permissionHolderTO.isDelete());
            result.setRead(permissionHolderTO.isRead());
            result.setWrite(permissionHolderTO.isWrite());
            result.setModifyACL(permissionHolderTO.isModify_acl());
            return result;
        } else {
            return null;
        }
    }

    private Object getSystemFolders(ListingFilterParameter filterParameter) throws RestException, InsufficientPermissionsException, ObjectNotFoundException {
        SystemResource systemResource = documentsServiceLocal.getSystemFolder();
        if (systemResource == null || systemResource.getObjectId() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "System folders not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        ArrayList<FolderResource> folderResources = documentsServiceLocal.getSystemSubFolders(systemResource.getObjectId());

        if (StringUtils.isNotBlank(filterParameter.getSearchKey())) {
            folderResources = (ArrayList<FolderResource>) folderResources.stream()
                    .filter(item -> item.getName().toLowerCase().contains(filterParameter.getSearchKey().toLowerCase()))
                    .collect(Collectors.toList());
        }

        Integer total = folderResources.size();
        ArrayList<FolderResource> subList = ListUtils.getSublistSmart(folderResources, filterParameter.getStart(), filterParameter.getLimit());

        ArrayList<FileTO> fileList = new ArrayList<>();
        for (FolderResource systemFolderResource : subList) {
            //Convert and add to list
            fileList.add(convert(systemFolderResource));
        }

        return successResponse(new ResponseResultListData<>(fileList, total));
    }

    private Object getPublicFolders(ListingFilterParameter filterParameter) throws RestException, InsufficientPermissionsException, ObjectNotFoundException {
        FolderResource folderResource = documentsServiceLocal.getPublicFolder();
        if (folderResource == null || folderResource.getObjectId() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Public folders not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        filterParameter.setFolderId(folderResource.getObjectId());
        ListResult<FileResource> folderResources = documentsServiceLocal.listFilesAndFolders(filterParameter);
        ArrayList<FileTO> fileList = new ArrayList<>();
        for (FileResource fileResource : folderResources.getList()) {
            //Convert and add to list
            fileList.add(convert(fileResource));
        }

        return successResponse(new ResponseResultListData<>(fileList, folderResources.getTotal()));
    }

    private Object getSharedByMeFolders(ListingFilterParameter filterParameter) throws RestException, InsufficientPermissionsException, ObjectNotFoundException {
        SharedResource sharedResource = documentsServiceLocal.getSharedFolder();
        if (sharedResource == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Shared by me folders not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        ArrayList<FileTO> fileList = new ArrayList<>();
        ArrayList<FolderResource> subFolders = sharedResource.getSubFolders();
        if (StringUtils.isNotBlank(filterParameter.getSearchKey())) {
            subFolders = (ArrayList<FolderResource>) sharedResource.getSubFolders().stream()
                    .filter(item -> item.getName().toLowerCase().contains(filterParameter.getSearchKey().toLowerCase()))
                    .collect(Collectors.toList());
        }
        for (FolderResource sharedFolderResource : subFolders) {
            //Convert and add to list
            fileList.add(convert(sharedFolderResource));
        }

        Integer total = fileList.size();
        ArrayList<FileTO> resultList = ListUtils.getSublistSmart(fileList, filterParameter.getStart(), filterParameter.getLimit());

        return successResponse(new ResponseResultListData<>(resultList, total));
    }

    private Object getTrashFolders(ListingFilterParameter filterParameter) throws RestException, InsufficientPermissionsException, ObjectNotFoundException {
        filterParameter.setTrashResource(true);
        ListResult<FileResource> trashFileResources = documentsServiceLocal.listFilesAndFolders(filterParameter);
        ArrayList<FileTO> fileList = new ArrayList<>();
        for (FileResource fileResource : trashFileResources.getList()) {
            //Convert and add to list
            fileList.add(convert(fileResource));
        }
        return successResponse(new ResponseResultListData<>(fileList, trashFileResources.getTotal()));
    }

    private Object getSharedWithMeFolders(ListingFilterParameter filterParameter) throws RestException, InsufficientPermissionsException, ObjectNotFoundException {
        OthersResource othersResource = documentsServiceLocal.getOthersShared();
        if (othersResource == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Shared with me folders is not found", NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        ArrayList<FileTO> fileList = new ArrayList<>();
        for (OtherUserResource otherUserResource : othersResource.getOtherUsers()) {
            for (FolderResource folderRes : otherUserResource.getFolders()) {
                //Convert and add to list
                fileList.add(convert(folderRes));
            }
        }
        if (StringUtils.isNotBlank(filterParameter.getSearchKey())) {
            fileList = (ArrayList<FileTO>) fileList.stream()
                    .filter(item -> item.getFile_name().toLowerCase().contains(filterParameter.getSearchKey().toLowerCase()))
                    .collect(Collectors.toList());
        }
        Integer total = fileList.size();
        ArrayList<FileTO> resultList = ListUtils.getSublistSmart(fileList, filterParameter.getStart(), filterParameter.getLimit());
        return successResponse(new ResponseResultListData<>(resultList, total));
    }

    private Object getMyFolders(ListingFilterParameter filterParameter) throws RestException, InsufficientPermissionsException, ObjectNotFoundException {
        ArrayList<FolderResource> folderResources = documentsServiceLocal.getFolders(null);
        if (folderResources == null || folderResources.isEmpty() || folderResources.get(0).getObjectId() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "There is no my folder", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        ArrayList<FileTO> fileList = new ArrayList<>();
        filterParameter.setFolderId(folderResources.get(0).getObjectId());
        ListResult<FileResource> resourceListResult = documentsServiceLocal.listFilesAndFolders(filterParameter);
        for (FileResource fileResource : resourceListResult.getList()) {
            //Convert and add to list
            fileList.add(convert(fileResource));
        }

        return successResponse(new ResponseResultListData<>(fileList, resourceListResult.getTotal()));
    }

}
