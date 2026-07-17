package com.edatasite.workforce.gwt.core.server.db.rbac.documents;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.documents.EdsDocumentPermission;
import com.edatasite.workforce.core.domain.documents.EdsFileHeader;
import com.edatasite.workforce.core.domain.documents.EdsFolder;
import com.edatasite.workforce.core.domain.rbac.EdsGroup;
import com.edatasite.workforce.core.domain.rbac.EdsRelationship;
import com.edatasite.workforce.core.domain.rbac.documents.EdsFolderRbac;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.Manager;

import java.util.List;
import java.util.Map;

/**
 * User: Sherali
 * Date: 29.05.2010
 * Time: 12:55:27
 */
public interface FolderRbacManager extends Manager<EdsFolderRbac> {

    void addRbacEntries(EdsFolder folder);

    List<EdsFolderRbac> getFolderRbacEntries(Integer folderId);

    void indexFile(EdsFileHeader file);

    void indexFiles(List<EdsFileHeader> files);

    void addFileRbacEntries(EdsFileHeader fileHeader);

    List<EdsFolderRbac> getFileRbacEntries(Integer fileId);

    void removeIndexFileEntries(Integer fileId);

    void removeFileFromSolr(Integer fileId);

    List<EdsFolderRbac> getFileEntryForUser(EdsFileHeader file, EdsUser user);

    List<EdsFolderRbac> getFolderEntryForUser(EdsFolder folder, EdsUser user);

    EdsDocumentPermission getFolderEntryForUser2(EdsFolder folder, EdsUser user);

    void removeFolderEntries(Integer folderId);

    EdsDocumentPermission getFilePermissionForUser(EdsFileHeader file, EdsUser user);

    EdsDocumentPermission getFolderPermissionForUser(EdsFolder folder, EdsUser user);

    EdsFolderRbac createCustomFolderGroupRelationEntry(EdsFolder folder, EdsGroup group, EdsDocumentPermission permission, EdsRelationship relationship, int entryType);

    EdsFolderRbac createCustomFileGroupRelationEntry(EdsFileHeader fileHeader, EdsGroup group, EdsDocumentPermission permission, EdsRelationship relationship, int entryType);

    void removeFolderCustomEntries(EdsFolder folder);

    void removeFileCustomEntries(EdsFileHeader fileHeader);

    List<EdsFolderRbac> getFolderRbacEntries(Integer folderId, int entryType);

    List<EdsFolderRbac> getFolderRbacEntries(Integer folderId, String relationship);

    List<EdsFolderRbac> getFileRbacEntries(Integer fileId, int entryType);

    EdsFolderRbac createCustomFolderUserRelationEntry(EdsFolder folder, EdsUser user, EdsDocumentPermission permission, EdsRelationship relationship, int entryType);

    EdsFolderRbac createCustomFileUserRelationEntry(EdsFileHeader fileHeader, EdsUser user, EdsDocumentPermission permission, EdsRelationship relationship, int entryType);

    void removeGroupEntries(Integer groupId);

    void removeUserEntries(Integer userId);

    EdsFolderRbac getFolderRbacEntryForGroup(Integer folderId, Integer groupId);

    EdsFolderRbac getFolderRbacEntryForUser(Integer folderId, Integer userId);

    EdsFolderRbac getFileRbacEntryForGroup(Integer fileId, Integer groupId);

    EdsFolderRbac getFileRbacEntryForUser(Integer fileId, Integer userId);

    void removeFileEntries(Integer objectId);

    void removeFileEntriesIdsIn(String ids, Integer companyID);

    void removeFolderRbacList(List<Integer> folderRbacList);

    Map<EdsFolder, EdsDocumentPermission> getFoldersPermissionEntriesForUser(ListingFilterParameter fp, List<EdsFolder> folders, EdsUser user, boolean deleted);

    void bulkDeleteFileRbacEntriesForFolder(Integer folderId, boolean isSubFolder);

    void bulkInsertFileRbacEntriesForFolder(Integer folderId, Integer userId, Integer groupId,
                                            Integer permissionId, String relationship, int relationRank, int entryType,
                                            int trusteeType, boolean skipOwner);
}
