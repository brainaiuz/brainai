package com.edatasite.workforce.gwt.core.server.db.documents;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.documents.EdsFolder;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.server.db.Manager;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 15.05.2010
 * Time: 13:29:32
 * To change this template use File | Settings | File Templates.
 */
public interface FolderManager extends Manager<EdsFolder> {
    List<EdsFolder> list(Integer companyId);

    EdsFolder getRootFolder(Integer userId);

    boolean existsFolder(Integer parentId, String name);

    boolean existsFile(Integer parentId, String name);

    boolean existsFileOtherThisFileID(Integer parentId, String name, Integer fileID);

    List<EdsFolder> getFoldersPermittedForGroup(Integer userId, Integer groupId);

    List<EdsFolder> getSharedRootFolders(Integer userId);

    List<EdsFolder> getChildsByParentId(Integer parentId);

    List<EdsFolder> getDeletedRootFolders(Integer userId) throws ObjectNotFoundException;

    List<EdsUser> getUsersSharingFoldersForUser(EdsUser user);

    List<EdsFolder> getSharedRootFolders(Integer userId, Integer callingUserId);

    List<FolderResource> getSharedFolders(Integer userId);

    List<FolderResource> getSharedFolders(Integer ownerId, Integer userId);

    EdsFolder getFolder(Integer objectID, String destName);

    EdsFolder getSystemFolder(Integer companyId);

    EdsFolder getPublicFolder(Integer companyId);

    void deleteFolderIndex(Integer folder);

    void indexFolder(EdsFolder folder, boolean isUpdateExistingOne);

    void indexFolders(List<EdsFolder> folders, boolean isUpdateExistingOne);

    EdsFolder getProjectFolder(Integer projectId);

    EdsFolder getTaskFolder(Integer projectId);

    EdsFolder getPMIssueFolder(Integer projectId);

    boolean isSharedFolder(Integer userId, Integer folderId);

    boolean isSharedFolderForOtherUser(Integer userId, Integer folderId);

    EdsFolder getProjectRootFolder(EdsCompany company);

    EdsFolder getExpensePaymentRootFolder(EdsCompany company);

    EdsFolder getMassMailingFolder(EdsCompany company);

    EdsFolder getCustomFieldRootFolder(EdsCompany company);

    /**
     * Return a folder with the provided folderType, entityId and company.
     *
     * @param folderType (F_DEFAULT-0,F_PROJECT_ROOT = 1,F_PROJECT-2,F_TASK-3,
     *                   F_PR_ISSUE = 4,F_PA_ROOT = 5,F_PA = 6,F_360 = 7,F_PA_ISSUE = 8,
     *                   F_AF_ROOT = 9,F_SALE_INV = 10,F_PUR_INV = 11,F_EXP = 12,F_AF_ISSUE = 13,
     *                   F_CRM_ROOT = 14,F_CRM_CONTACT = 15,F_LEAD = 16,F_CASE = 17,F_CLIENT = 18 ...)
     * @param entityId   (null,projectId,taskId,...)
     * @return the folder
     */
    EdsFolder getFolder(int folderType, Integer entityId);

    /**
     * Return a temp folder with the provided company.
     *
     * @param companyId EdsCompany
     * @return the temp folder
     */
    EdsFolder getTempFolder(Integer companyId);

    EdsFolder getFolderByFolderType(int folderType);

    List<EdsFolder> getFoldersByFolderType(int folderType, EdsCompany company);

    List<EdsFolder> getFolderNames(int type, Integer userId);

    List<EdsFolder> getCompanyFolderListForSolr(SolrReindexRpc solrReindex, Integer start, Integer limit);

    List<EdsFolder> getFoldersByIds(List<Integer> subIds);

    EdsFolder getCustomFieldFolder(Integer objectID);

    void createS3Key(String key);

    List<String> getS3Keys(String code);

    void setFolderHasChild(boolean hasChild, Integer folderId);

}
