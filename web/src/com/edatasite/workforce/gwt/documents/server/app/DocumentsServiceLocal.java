package com.edatasite.workforce.gwt.documents.server.app;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.documents.EdsFileHeader;
import com.edatasite.workforce.core.domain.documents.EdsFolder;
import com.edatasite.workforce.gwt.core.client.rpc.DocumentsSearchItem;
import com.edatasite.workforce.gwt.core.client.rpc.SearchResultItemList;
import com.edatasite.workforce.gwt.core.client.rpc.TeamEmployees;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.documents.client.exceptions.DuplicateNameException;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.exceptions.QuotaExceededException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 18.05.2010
 * Time: 19:12:15
 * To change this template use File | Settings | File Templates.
 */
public interface DocumentsServiceLocal {

    FileResource createFile(DocumentItem file, String type, int fileType, Integer entityId) throws DuplicateNameException, ObjectNotFoundException, InsufficientPermissionsException, QuotaExceededException;

    FileResource createFile(DocumentItem file, String type, int fileType, Integer entityId, Integer userId) throws DuplicateNameException, ObjectNotFoundException, InsufficientPermissionsException, QuotaExceededException;

    FileResource createFile(DocumentItem file) throws DuplicateNameException, ObjectNotFoundException, InsufficientPermissionsException, QuotaExceededException;

    void createFile(List<DocumentItem> files, String type, int fileType, Integer entityId, String sourceBucket, String destinationBucket) throws DuplicateNameException, ObjectNotFoundException, InsufficientPermissionsException, QuotaExceededException;

    void deleteFile(final Integer fileId) throws ObjectNotFoundException, InsufficientPermissionsException;

    DocumentItem getFile(Integer fileId);

    List<FolderResource> getFolders() throws ObjectNotFoundException;

    FolderResource getSubFolders(Integer folderId) throws ObjectNotFoundException, InsufficientPermissionsException;

    FolderResource createFolder(Integer parentId, String name) throws DuplicateNameException, ObjectNotFoundException, InsufficientPermissionsException;

    UserResource getCurrentUserResource();

    HashSet<PermissionHolder> getFolderPermissions(Integer folderId) throws ObjectNotFoundException, InsufficientPermissionsException;

    HashSet<PermissionHolder> getFilePermissions(Integer fileId);

    void setFilePermissions(EdsFileHeader file, List<PermissionHolder> permissions, boolean isSubFolder) throws ObjectNotFoundException, InsufficientPermissionsException;

    void setFolderPermissions(EdsUser user, EdsFolder folder, List<PermissionHolder> permissions, boolean isSubFolder) throws ObjectNotFoundException, InsufficientPermissionsException;

    void deleteFolder(final Integer folderId) throws InsufficientPermissionsException, ObjectNotFoundException;

    ListResult<FileResource> listFile(ListingFilterParameter fp) throws ObjectNotFoundException;

    void deleteFiles(List<Integer> fileIds) throws ObjectNotFoundException, InsufficientPermissionsException;

    ArrayList<TeamEmployees> getCompanyEmployeesWithTeams(boolean includeActiveUser);

    SharedResource getSharedFolder() throws ObjectNotFoundException;

    TrashResource getTrashedFolder() throws ObjectNotFoundException;

    OthersResource getOthersShared() throws ObjectNotFoundException;

    void emptyTrash() throws ObjectNotFoundException, InsufficientPermissionsException;

    void moveFolderToTrash(Integer folderId) throws ObjectNotFoundException, InsufficientPermissionsException;

    void moveFileToTrash(Integer fileId) throws ObjectNotFoundException, InsufficientPermissionsException;

    void copyFolder(Integer folderId, Integer destId) throws ObjectNotFoundException,
            DuplicateNameException, InsufficientPermissionsException, QuotaExceededException;

    void moveFolder(Integer folderId, Integer objectId) throws InsufficientPermissionsException, DuplicateNameException;

    void copyFile(Integer fileId, Integer folderId, Integer entityID) throws QuotaExceededException, ObjectNotFoundException, InsufficientPermissionsException, DuplicateNameException;

    void moveFile(Integer fileId, Integer foldeId) throws ObjectNotFoundException, InsufficientPermissionsException;

    void removeFileFromTrash(Integer fileId) throws ObjectNotFoundException, InsufficientPermissionsException;

    void removeFolderFromTrash(Integer folderId) throws ObjectNotFoundException, InsufficientPermissionsException;

    SystemResource getSystemFolder() throws ObjectNotFoundException, InsufficientPermissionsException;

    ArrayList<FolderResource> getSystemSubFolders(Integer parentId);

    void createProjectFolder(Integer projectId);

    void createExpensePaymentFolder(Integer projectId);

    void createMailMessageFolder(Integer mailMessageID);

    void createCustomFieldFolder(Integer customFieldId);

    void createSystemFolders(Integer companyId);

    Integer indexCompanyFolders(SolrReindexRpc solrReindex, Integer start, Integer limit);

    void indexCompanySystemFolders(SolrReindexRpc solrReindex);

    void removeDocumentEntries(Integer userId);

    void reIndexProjectDocument(Integer projectId);

    void reIndexTaskDocument(Integer taskId);

    Integer getCompanyFileUploadMaxSize(Integer... companyIDs);

    SearchResultItemList getSearchResult(DocumentsSearchItem parameters);

    FolderResource getFolderResource(int folderType, Integer entityID);

    void copyCaseAttachments(EdsCompany company);

    ArrayList<FileResource> uploadAllFiles(ArrayList<FileResource> files, FolderResource folder, String description);

    ArrayList<FileResource> getFileResources(int folderType, Integer folderId, Integer entityId);

    void saveEmployeeProfilePicture(Integer companyID, Integer employeeID);

    FolderResource getPublicFolder() throws ObjectNotFoundException, InsufficientPermissionsException;

    ArrayList<FolderResource> getFolders(Integer folderId) throws ObjectNotFoundException;

    ListResult<FileResource> listFilesAndFolders(ListingFilterParameter fp);

    FolderResource getFolderResource(Integer folderID);

    FileResource getFileResource(Integer fileId);

    FileResource getFileResourceByFileTypeAndName(Integer fileType, String fileName);

    void updateFolder(FolderResource oldFolder, ArrayList<PermissionHolder> perms) throws InsufficientPermissionsException, ObjectNotFoundException, DuplicateNameException;

    FolderResource getFolderResource(Integer folderId, EdsUser user) throws InsufficientPermissionsException;

    FileResource saveDocumentFile(MultipartFile multipartFile, String uploadType,
                                  Integer folderID, Integer folderType, Integer entityID,
                                  String description);

    FileResource saveDocumentFile(MultipartFile multipartFile, Integer folderID,
                                  Integer folderType, Integer entityID,
                                  String description);

    FolderResource getTempFolderByCompany(Integer companyID);

    Double[] getStorageSize();
    void updateFile(Integer fileId, String name, Boolean readForAll, ArrayList<PermissionHolder> permissions)
            throws DuplicateNameException, ObjectNotFoundException, InsufficientPermissionsException;

}
