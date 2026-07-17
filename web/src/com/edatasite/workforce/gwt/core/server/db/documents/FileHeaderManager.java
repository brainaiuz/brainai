package com.edatasite.workforce.gwt.core.server.db.documents;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.documents.EdsFileHeader;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.server.db.Manager;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 15.05.2010
 * Time: 13:29:32
 * To change this template use File | Settings | File Templates.
 */
public interface FileHeaderManager extends Manager<EdsFileHeader> {

    /**
     * Returns a list of files contained in the folder specified by its id, CAUTION: it does not return files marked as deleted
     *
     * @param folderId
     * @param user
     * @param ignoreDeleted
     * @return List<EdsFileHeader>
     */
    List<EdsFileHeader> getFiles(Integer folderId, EdsUser user, boolean ignoreDeleted);

    /**
     * Retrieve the file with the supplied name that is contained
     * in a folder with the specified ID.
     *
     * @param folderId the ID of the parent folder
     * @param name     the name of the file
     * @return the file found
     */
    EdsFileHeader getFile(Integer folderId, String name);

    /**
     * @param fileType F_CASH_ADVANCE,F_PROJECT
     * @param name     the name of the file
     * @return
     */
    EdsFileHeader getFileByFileTypeFileName(Integer fileType, String name);

    /**
     * Returns a list of all shared files of a user, not contained in a shared folder.
     *
     * @param user EdsUser
     * @return the list of shared files
     */
    List<EdsFileHeader> getSharedFilesNotInSharedFolders(EdsUser user);

    /**
     * Returns a list of deleted files of user specified by userId
     *
     * @param user
     * @return List<FileHeader>
     */
    List<EdsFileHeader> getDeletedFiles(ListingFilterParameter filterParameter, EdsUser user);

    /**
     * Returns a list of users sharing files to specified user
     *
     * @param user the  of the EdsUser
     * @return the list of users sharing files to selected user
     * @throws ObjectNotFoundException if the user cannot be found
     */
    List<EdsUser> getUsersSharingFilesForUser(EdsUser user);

    /**
     * Returns a list of All Shared files of a user not contained in a shared folder that calling user has permissions.
     *
     * @param owner         EdsUser
     * @param callingUserId
     * @return the list of shared files
     * @throws ObjectNotFoundException if the user cannot be found
     */
    List<EdsFileHeader> getSharedFiles(EdsUser ownerId, Integer callingUserId) throws ObjectNotFoundException;

    List<EdsFileHeader> list(ListingFilterParameter fp);

    /**
     * Returns a list of All files of a company.
     *
     * @param company EdsComapny
     * @return the list of company files
     */
    List<EdsFileHeader> getCompanyFilelist(EdsCompany company);

    /**
     * returns limited files for company starting from startat
     *
     *
     * @param solrReindex
     * @param start
     * @param limit
     * @return
     */
    List<EdsFileHeader> getCompanyFileForSolr(SolrReindexRpc solrReindex, Integer start, Integer limit);

    List<EdsFileHeader> getFileIdsIn(String ids);

    List<Integer> getEntityIDsByFileType(int fCase);

    List<EdsFileHeader> searchFiles(Integer folderId, String name);

    List<EdsFileHeader> getEmployeeDocuments(Integer employeeID, int fType);

    EdsFileHeader getCurrentBody(Integer uploadID);

    List<EdsFileHeader> getEmployeeDocuments(List<Integer> availableItems);
}