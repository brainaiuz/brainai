package com.edatasite.workforce.gwt.core.server.db.impl.documents;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.documents.EdsFileHeader;
import com.edatasite.workforce.core.domain.documents.EdsFolder;
import com.edatasite.workforce.core.domain.rbac.EdsTrusteeType;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.server.db.documents.FileHeaderManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 15.05.2010
 * Time: 13:30:38
 * To change this template use File | Settings | File Templates.
 */
@Repository("fileHeaderManager")
public class FileHeaderManagerImpl extends BaseManager<EdsFileHeader> implements FileHeaderManager {

    public FileHeaderManagerImpl() {
        super(EdsFileHeader.class);
    }

    @Override
    public List<EdsFileHeader> list(ListingFilterParameter fp) {
        return (List<EdsFileHeader>) find("select f from EdsFileHeader f where f.deleted=false and f.folder.deleted=false order by f.objectID desc");
    }

    @Override
    public List<EdsFileHeader> getCompanyFilelist(EdsCompany company) {
        return (List<EdsFileHeader>) find("select f from EdsFileHeader f");
    }

    public List<EdsFileHeader> getCompanyFileForSolr(SolrReindexRpc solrReindex, Integer start, Integer limit) {
        StringBuilder fileSqlQuery = new StringBuilder("select t.* from " + getCompanyId() + ".fileheader t ");
        fileSqlQuery.append("where t.id>" + start);
        if (!solrReindex.isAllReindex() && solrReindex.getLastUpdateTime() != null) {
            fileSqlQuery.append(" and t.modificationDate>=").append("'").append(solrReindex.getLastUpdateTime()).append("'");
            if (solrReindex.getLastUpdateEndTime() != null) {
                fileSqlQuery.append(" and t.modificationDate<='").append(solrReindex.getLastUpdateEndTime()).append("'");
            }
        }
        fileSqlQuery.append(" order by t.id asc limit " + limit);
        return findNative(fileSqlQuery.toString(), EdsFileHeader.class);
    }

    @Override
    public List<EdsFileHeader> getFileIdsIn(String ids) {
        return (List<EdsFileHeader>) find("SELECT ta FROM EdsFileHeader ta WHERE ta.objectID IN (" + ids + ")");
    }

    @Override
    public List<Integer> getEntityIDsByFileType(int fCase) {
        return (List<Integer>) findNative("select distinct f.entityid from " + getCompanyId() + ".fileheader f where deleted is not true and filetype = " + fCase);
    }

    @Override
    public List<EdsFileHeader> getFiles(Integer folderId, EdsUser user, boolean ignoreDeleted) {
        Map params = new HashMap();
        params.put("folderId", folderId);
        String query;
        if (ignoreDeleted) {
            query = "select f from EdsFileHeader f where f.folder.objectID=:folderId  and f.deleted=false";
        } else {
            query = "select f from EdsFileHeader f where f.folder.objectID=:folderId";
        }
        return findByNamedParams(query, params);
    }

    @Override
    public EdsFileHeader getFile(Integer folderId, String name) {
        Map params = new HashMap();
        params.put("parentId", folderId);
        params.put("name", name);
        return (EdsFileHeader) findSingleByNamedParams("select f from EdsFileHeader f where f.folder.objectID=:parentId and f.name=:name", params);
    }


    @Override
    public EdsFileHeader getFileByFileTypeFileName(Integer fileType, String name) {
        Map params = new HashMap();
        params.put("fileType", fileType);
        params.put("name", name);
        return (EdsFileHeader) findSingleByNamedParams("select f from EdsFileHeader f where f.fileType=:fileType and f.name=:name", params);
    }

    @Override
    public List<EdsFileHeader> getSharedFilesNotInSharedFolders(EdsUser user) {
        Map params = new HashMap();
        params.put("userId", user.getObjectID());
        params.put("type", EdsFolder.CUSTOM);
        return findByNamedParams("select distinct f.fileHeader from EdsFolderRbac f " +
                "where f.fileHeader.owner.objectID=:userId and f.file=true and f.fileHeader.deleted=false and f.fileHeader.folder.type=:type " +
                "and (f.fileHeader.readForAll=true or f.group.objectID is not null or f.user.objectID != f.fileHeader.owner.objectID)" +
                " and f.fileHeader.folder.objectID not in (select distinct fo.folder.objectID from EdsFolderRbac fo " +
                "where fo.file=false and fo.folder.owner.objectID=:userId and fo.folder.deleted=false and " +
                "(fo.group.objectID is not null or fo.user.objectID != fo.folder.owner.objectID))", params);
    }

    @Override
    public List<EdsFileHeader> getDeletedFiles(ListingFilterParameter fp, EdsUser user) {
        StringBuilder sql = new StringBuilder();
        sql.append("select f from EdsFileHeader f where f.owner.objectID=").append(user.getObjectID());
        sql.append(" and f.deleted=true and f.folder.deleted=false");
        if (fp != null) {
            if (StringUtils.isNotBlank(fp.getSqlSearchKey())) {
                sql.append(" and (lower(f.name) like '" + fp.getSqlSearchKey() + "')");
            }
            return findInterval(sql.toString(), fp.getStart(), fp.getLimit());
        } else {
            return find(sql.toString());
        }
    }

    @Override
    public List<EdsUser> getUsersSharingFilesForUser(EdsUser user) {
        Map params = new HashMap();
        params.put("userId", user.getObjectID());
        params.put("type", EdsFolder.CUSTOM);
        params.put("trusteeType", EdsTrusteeType.USER);
        return findByNamedParams("select distinct f.fileHeader.owner from EdsFolderRbac f " +
                "where f.file=true and f.fileHeader.owner.objectID != :userId and f.fileHeader.deleted=false and f.fileHeader.folder.type=:type " +
                "and (f.user.objectID=:userId or f.group.objectID in (select distinct gg.objectID from EdsGroup gg join " +
                "gg.members memb where memb.objectID in (select distinct t.objectID from EdsTrustee t where t.trusteeID=:userId and t.type.objectID=:trusteeType))))", params);
    }

    @Override
    public List<EdsFileHeader> getSharedFiles(EdsUser user, Integer callingUserId) throws ObjectNotFoundException {
        if (user == null) {
            throw new ObjectNotFoundException("No user specified");
        }
        Map params = new HashMap();
        params.put("userId", user.getObjectID());
        params.put("cuserId", callingUserId);
        params.put("type", EdsFolder.CUSTOM);
        params.put("trusteeType", EdsTrusteeType.USER);
        return findByNamedParams("select distinct f .fileHeader from EdsFolderRbac f " +
                "where f.fileHeader.owner.objectID=:userId and f.file=true and f.fileHeader.deleted=false  and f.fileHeader.folder.type=:type " +
                "and f.documentPermission.read=true and (f.user.objectID=:cuserId or f.group.objectID in " +
                "(select distinct gg.objectID from EdsGroup gg join gg.members memb where memb.objectID in " +
                "(select distinct t.objectID from EdsTrustee t where t.trusteeID=:cuserId and t.type.objectID=:trusteeType))) and f.fileHeader.folder.objectID not in (select distinct fo.fileHeader.objectID " +
                "from EdsFolderRbac fo where fo.file=true and fo.folder.owner.objectID = :userId " +
                "and fo.folder.deleted=false and fo.documentPermission.read=true and (fo.user.objectID=:cuserId " +
                "or fo.group.objectID in (select distinct gg.objectID from EdsGroup gg join gg.members memb where " +
                "memb.objectID in (select distinct t.objectID from EdsTrustee t where t.trusteeID=:cuserId and t.type.objectID=:trusteeType))))", params);
    }

    public List<EdsFileHeader> searchFiles(Integer folderId, String name) {
        return find("select f from EdsFileHeader f where " + (folderId != null ? "f.folder.objectID="+folderId+" and " : "") + "lower(f.name) like lower('%" + name + "%')");
    }

    @Override
    public List<EdsFileHeader> getEmployeeDocuments(Integer employeeID, int ftype) {
        return find("select f from EdsFileHeader f where f.deleted is not true and f.folder.folderType=" + ftype + " and f.entityId=" + employeeID);
    }

    @Override
    public EdsFileHeader getCurrentBody(Integer uploadID) {
        return (EdsFileHeader) findSingle("select fh from EdsFileHeader fh where fh.currentBody.objectID = " + uploadID);

    }

    @Override
    public List<EdsFileHeader> getEmployeeDocuments(List<Integer> availableItems) {
        if (availableItems == null || availableItems.isEmpty()) return Collections.emptyList();
        return masterEntityManager.createQuery(
                        "select f from EdsFileHeader f " +
                                "where coalesce(f.deleted,false)=false and f.objectID in :ids",
                        EdsFileHeader.class
                )
                .setParameter("ids", availableItems)
                .getResultList();
    }
}