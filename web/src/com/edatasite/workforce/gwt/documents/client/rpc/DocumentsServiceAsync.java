package com.edatasite.workforce.gwt.documents.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.TeamEmployees;
import com.edatasite.workforce.gwt.core.client.rpc.TreeSelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.OtherUserResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.OthersResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.PermissionHolder;
import com.edatasite.workforce.gwt.documents.client.rest.resource.SharedResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.SystemResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.TrashResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.UserResource;
import com.google.gwt.user.client.rpc.AsyncCallback;

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
public interface DocumentsServiceAsync {

    void getFolders(Integer folderId, AsyncCallback<ArrayList<FolderResource>> callback);

    void getLeftFolders(boolean isClient, AsyncCallback<LinkedList<SelectItem>> callback) throws InsufficientPermissionsException;

    void getSubFolders(Integer folderId, AsyncCallback<FolderResource> callback);

    void getAllMainFolders(AsyncCallback<HashMap<String, FolderResource>> callback);

    void createFolder(Integer parentId, String name, AsyncCallback<FolderResource> callback);

    void getCurrentUserResource(AsyncCallback<UserResource> callback);

    void getFolderPermissions(Integer folderId, AsyncCallback<HashSet<PermissionHolder>> callback);

    void deleteFolder(final Integer folderId, AsyncCallback callback);

    void deleteFile(final Integer fileId, AsyncCallback callback);

    void deleteFile(final Integer fileId, Integer folderId, Integer folderType, AsyncCallback callback);

    void listFile(ListingFilterParameter fp, AsyncCallback<ListResult<FileResource>> callback);

    void getDocumentList(ListingFilterParameter fp, AsyncCallback<ListResult<FileResource>> callback);

    void getCompanyAndEmployeeDocumentExpiryList(ListingFilterParameter fp, AsyncCallback<ArrayList<FileResource>> callback);

    void getCompanyAndEmployeeDocumentExpiryListNew(ListingFilterParameter fp, AsyncCallback<ListResult<FileResource>> callback);

    void listFilesAndFolders(ListingFilterParameter fp, AsyncCallback<ListResult<FileResource>> callback);

    void addUserToGroup(Integer groupId, Integer userToAddId, AsyncCallback callback);

    void deleteGroup(Integer objectId, AsyncCallback callback);

    void removeMemberFromGroup(Integer groupId, Integer memberId, AsyncCallback callback);

    void getCompanyEmployeesWithTeams(boolean includeActiveUser, AsyncCallback<ArrayList<TeamEmployees>> callback);

    void addUsersToGroup(Integer groupId, ArrayList<Integer> emIdList, AsyncCallback callback);

    void getSharedFolder(AsyncCallback<SharedResource> callback);

    void updateFolder(FolderResource oldFolder, ArrayList<PermissionHolder> perms, AsyncCallback callback);

    void getTrashedFolder(AsyncCallback<TrashResource> callback);

    void getOthersShared(AsyncCallback<OthersResource> callback);

    void getOtherUserResource(Integer userId, AsyncCallback<OtherUserResource> callback);

    void emptyTrash(AsyncCallback callback);

    void moveFolderToTrash(Integer folderId, AsyncCallback callback);

    void moveFileToTrash(Integer fileId, AsyncCallback callback);

    void batchDeleteFiles(ArrayList<Integer> fileIds, AsyncCallback callback);

    void indexFiles(ArrayList<Integer> fileIds, AsyncCallback callback);

    void copyFolder(Integer folderId, Integer destId, AsyncCallback callback);

    void moveFolder(Integer folderId, Integer objectId, AsyncCallback callback);

    void copyFile(Integer fileId, Integer folderId, Integer entityID, AsyncCallback callback);

    void moveFile(Integer fileId, Integer folderId, AsyncCallback callback);

    void updateFile(Integer fileId, String name, Boolean readForAll, ArrayList<PermissionHolder> permissions, AsyncCallback callback);

    void removeFileFromTrash(Integer fileId, AsyncCallback callback);

    void removeFolderFromTrash(Integer folderId, AsyncCallback callback);

    void getSystemFolder(AsyncCallback<SystemResource> callback);

    void indexFolder(Integer integer, boolean isUpdateExistingOne, AsyncCallback callback);

    void getSystemSubFolders(Integer parentId, AsyncCallback<ArrayList<FolderResource>> callback);

    void getTasks(Integer projectId, AsyncCallback<SelectItem[]> callback);

    void getIssues(Integer projectId, AsyncCallback<SelectItem[]> callback);

    void getEmployees(AsyncCallback<SelectItem[]> callback);

    void getVacancyOrCandidateOrPlacement(String vacancyOrCandidateOrPlacement, AsyncCallback<SelectItem[]> callback);

    void executeSolrQuery(String text, String core, AsyncCallback callback);

    void getCompanyFileUploadMaxSize(AsyncCallback<Integer> callback);

    void getCompanyFileUploadUsedStorage(AsyncCallback<Double> callback);

    void getFolderResource(int folderType, Integer entityID, AsyncCallback<FolderResource> callback);

    void getFolderResource(Integer folderID, AsyncCallback<FolderResource> callback);

    void getFolderID(int folderType, Integer entityId, AsyncCallback<Integer> callback);

    void getFileResources(int folderType, Integer folderId, Integer entityId, AsyncCallback<ArrayList<FileResource>> callback);

    void getRootFolderID(AsyncCallback<Integer> async);

    void getRootFolderResource(AsyncCallback<FolderResource> callback);

    void indexFolders(Integer companyId, AsyncCallback<Void> async);

    void indexFiles(Integer companyId, AsyncCallback<Void> async);

    void getUsersAllGoogleDocumentsAndFolders(String storageType, AsyncCallback<FolderResource[]> callback);

    void getUsersAllSubFoldersInKpiRoot(String root, String driveType, AsyncCallback<ArrayList<TreeSelectItem>> callback);

    void getGoogleSubFolders(String parentId, String storageType, AsyncCallback<FolderResource[]> callback);

    void getGoogleFiles(String folderId, String storageType, AsyncCallback<ArrayList<FileResource>> callback);

    void listFilesAndFoldersForPopup(ListingFilterParameter fp, AsyncCallback<ListResult<FileResource>> callback);

    void searchDocument(Integer folderID, String documentName, AsyncCallback<ArrayList<FileResource>> callback);

    void uploadAllFiles(ArrayList<FileResource> files, ArrayList<FileResource> kpiFiles, FolderResource folder, String description, AsyncCallback<ArrayList<FileResource>> callback);

    void copyUploadDocumentSize(Integer companyID, AsyncCallback<String> callback);

    void isIndexedUploadDocument(AsyncCallback<Boolean> callback);

    void saveFileDescription(Integer fileBodyId, String description, AsyncCallback<Void> callback);

    void getDocumentTypes(String typeCode, AsyncCallback<HashMap<Integer, ArrayList<SelectItem>>> async);

    void updateFiles(ArrayList<FileResource> items, Integer entityID, String typeCode, AsyncCallback callback);

    void getPublicFolder(AsyncCallback<FolderResource> myPublic);

    void getEmployeeDocumentsWithTreeInfo(ListingFilterParameter filterParametrs, ArrayList<Integer> employeeDocuments, AsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>> callback);

    void getEnableUploadTypes(AsyncCallback<HashMap<String, Boolean>> callback);

    void getFileLink(Integer fileBodyID, AsyncCallback<String> callback);
    void getStorageSize (AsyncCallback<Double[]> callback);

    void saveXhrFile(ArrayList<FileResource> files, FolderResource folder, String description, AsyncCallback<ArrayList<FileResource>> async);

}