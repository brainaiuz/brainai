package com.edatasite.workforce.gwt.documents.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TeamEmployees;
import com.edatasite.workforce.gwt.core.client.rpc.TreeSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
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
import com.edatasite.workforce.gwt.documents.client.rest.resource.TrashResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.UserResource;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * User: Sherali
 * Date: 14.05.2010
 * Time: 15:34:29
 */
public interface DocumentsService extends RemoteService {

    ArrayList<FolderResource> getFolders(Integer folderId) throws ObjectNotFoundException;

    LinkedList<SelectItem> getLeftFolders(boolean isClient) throws ObjectNotFoundException, InsufficientPermissionsException;

    FolderResource getSubFolders(Integer folderId) throws ObjectNotFoundException, InsufficientPermissionsException;

    HashMap<String, FolderResource> getAllMainFolders();

    FolderResource createFolder(Integer parentId, String name) throws DuplicateNameException, ObjectNotFoundException, InsufficientPermissionsException;

    UserResource getCurrentUserResource();

    HashSet<PermissionHolder> getFolderPermissions(Integer folderId) throws ObjectNotFoundException, InsufficientPermissionsException;

    void deleteFolder(final Integer folderId) throws InsufficientPermissionsException, ObjectNotFoundException;

    void deleteFile(final Integer fileId) throws ObjectNotFoundException, InsufficientPermissionsException;

    void deleteFile(final Integer fileId, Integer folderId, Integer folderType) throws ObjectNotFoundException, InsufficientPermissionsException;

    ListResult<FileResource> listFile(ListingFilterParameter fp) throws ObjectNotFoundException;

    ListResult<FileResource> getDocumentList(ListingFilterParameter fp) throws ObjectNotFoundException;

    ArrayList<FileResource> getCompanyAndEmployeeDocumentExpiryList(ListingFilterParameter fp);

    ListResult<FileResource> getCompanyAndEmployeeDocumentExpiryListNew(ListingFilterParameter fp);

    ListResult<FileResource> listFilesAndFolders(ListingFilterParameter fp);

    void addUserToGroup(Integer groupId, Integer userToAddId) throws ObjectNotFoundException, DuplicateNameException, InsufficientPermissionsException;

    void deleteGroup(Integer objectId) throws ObjectNotFoundException, InsufficientPermissionsException;

    void removeMemberFromGroup(Integer groupId, Integer memberId) throws ObjectNotFoundException, InsufficientPermissionsException;

    ArrayList<TeamEmployees> getCompanyEmployeesWithTeams(boolean includeUser);

    void addUsersToGroup(Integer groupId, ArrayList<Integer> userIds) throws InsufficientPermissionsException;

    SharedResource getSharedFolder() throws ObjectNotFoundException;

    void updateFolder(FolderResource oldFolder, ArrayList<PermissionHolder> perms) throws InsufficientPermissionsException, ObjectNotFoundException, DuplicateNameException;

    TrashResource getTrashedFolder() throws ObjectNotFoundException;

    OthersResource getOthersShared() throws ObjectNotFoundException;

    OtherUserResource getOtherUserResource(Integer userId) throws ObjectNotFoundException;

    void emptyTrash() throws ObjectNotFoundException, InsufficientPermissionsException;

    void moveFolderToTrash(Integer folderId) throws ObjectNotFoundException, InsufficientPermissionsException;

    void moveFileToTrash(Integer fileId) throws ObjectNotFoundException, InsufficientPermissionsException;

    void batchDeleteFiles(ArrayList<Integer> fileIds) throws ObjectNotFoundException, InsufficientPermissionsException;

    void indexFiles(ArrayList<Integer> fileIds);

    void copyFolder(Integer folderId, Integer destId) throws ObjectNotFoundException,
            DuplicateNameException, InsufficientPermissionsException, QuotaExceededException;

    void moveFolder(Integer folderId, Integer objectId) throws InsufficientPermissionsException, DuplicateNameException;

    void copyFile(Integer fileId, Integer folderId, Integer entityID) throws QuotaExceededException, ObjectNotFoundException, InsufficientPermissionsException, DuplicateNameException;

    void moveFile(Integer fileId, Integer folderId) throws ObjectNotFoundException, InsufficientPermissionsException;

    void updateFile(Integer fileId, String name, Boolean readForAll, ArrayList<PermissionHolder> permissions)
            throws DuplicateNameException, ObjectNotFoundException, InsufficientPermissionsException;

    void removeFileFromTrash(Integer fileId) throws ObjectNotFoundException, InsufficientPermissionsException;

    void removeFolderFromTrash(Integer folderId) throws ObjectNotFoundException, InsufficientPermissionsException;

    SystemResource getSystemFolder() throws ObjectNotFoundException, InsufficientPermissionsException;

    void indexFolder(Integer integer, boolean isUpdateExistingOne);

    ArrayList<FolderResource> getSystemSubFolders(Integer parentId);

    SelectItem[] getTasks(Integer projectId);

    SelectItem[] getIssues(Integer projectId);

    SelectItem[] getEmployees();

    SelectItem[] getVacancyOrCandidateOrPlacement(String vacancyOrCandidateOrPlacement);

    void executeSolrQuery(String text, String core);

    Integer getCompanyFileUploadMaxSize();

    Double getCompanyFileUploadUsedStorage();

    FolderResource getFolderResource(int folderType, Integer entityID);

    FolderResource getFolderResource(Integer folderID);

    ArrayList<FileResource> getFileResources(int folderType, Integer folderId, Integer entityId);

    Integer getRootFolderID();

    FolderResource getRootFolderResource();

    Integer getFolderID(int folderType, Integer entityId);

    void indexFolders(Integer companyId);

    void indexFiles(Integer companyId);

    FolderResource[] getUsersAllGoogleDocumentsAndFolders(String storageType);

    ArrayList<TreeSelectItem> getUsersAllSubFoldersInKpiRoot(String root, String driveType);

    FolderResource[] getGoogleSubFolders(String parentId, String storageType);

    ArrayList<FileResource> getGoogleFiles(String folderId, String storageType);

    ListResult<FileResource> listFilesAndFoldersForPopup(ListingFilterParameter fp);

    ArrayList<FileResource> searchDocument(Integer folderID, String documentName);

    ArrayList<FileResource> uploadAllFiles(ArrayList<FileResource> files, ArrayList<FileResource> kpiFiles, FolderResource folder, String description);

    String copyUploadDocumentSize(Integer companyID);

    Boolean isIndexedUploadDocument();

    void saveFileDescription(Integer fileBodyId, String description);

    HashMap<Integer, ArrayList<SelectItem>> getDocumentTypes(String typeCode);

    void updateFiles(ArrayList<FileResource> items, Integer entityID, String typeCode);

    FolderResource getPublicFolder() throws ObjectNotFoundException, InsufficientPermissionsException;

    LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getEmployeeDocumentsWithTreeInfo(ListingFilterParameter filterParameter, ArrayList<Integer> employeeDocuments) throws ObjectNotFoundException;

    HashMap<String, Boolean> getEnableUploadTypes();

    String getFileLink(Integer fileBodyID);

    Double[] getStorageSize();

    ArrayList<FileResource> saveXhrFile(ArrayList<FileResource> files, FolderResource folder, String description);
    /**
     * Utility/Convenience class.
     * Use DocumentsService.App.getInstance() to access static instance of DocumentsServiceAsync
     */
    class App {
        public static DocumentsServiceAsync get() {
            ServiceDefTarget target = GWT.create(DocumentsService.class);
            target.setServiceEntryPoint("/rpc/documents");
            return (DocumentsServiceAsync) target;
        }
    }
}