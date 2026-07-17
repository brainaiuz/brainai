package com.edatasite.workforce.gwt.core.server.db.impl.documents;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.documents.EdsFileHeader;
import com.edatasite.workforce.core.domain.documents.EdsFolder;
import com.edatasite.workforce.core.domain.rbac.EdsTrusteeType;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.server.db.documents.FolderManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.documents.FolderRbacManager;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FolderResource;
import com.edatasite.workforce.gwt.documents.client.rest.resource.PermissionHolder;
import com.google.common.base.Stopwatch;
import org.apache.commons.lang.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 15.05.2010
 * Time: 13:30:38
 * To change this template use File | Settings | File Templates.
 */
@Repository
public class FolderManagerImpl extends BaseManager<EdsFolder> implements FolderManager {

    @Autowired
    private FolderRbacManager folderRbacManager;

    public FolderManagerImpl() {
        super(EdsFolder.class);
    }

    @Override
    public List<EdsFolder> list(Integer companyId) {
        return find("select f from EdsFolder f where f.deleted is not true order by f.auditInfo.modificationDate desc");
    }

    @Override
    public EdsFolder getRootFolder(Integer userId) {
        Map params = new HashMap();
        params.put("ownerId", userId);
        params.put("type", EdsFolder.CUSTOM);
        return (EdsFolder) findSingleByNamedParams("select f from EdsFolder f where f.folderType<>25 and f.owner.objectID=:ownerId and f.type=:type and f.parent is null", params);
    }

    @Override
    public boolean existsFile(Integer parentId, String name) {
        Map params = new HashMap();
        params.put("parentId", parentId);
        params.put("name", name.toLowerCase());

        EdsFileHeader fh = (EdsFileHeader) findSingleByNamedParams("select f from EdsFileHeader f " +
                "where f.folder.objectID=:parentId and lower(f.name)=:name", params);
        return fh != null;

    }

    @Override
    public boolean existsFileOtherThisFileID(Integer parentId, String name, Integer thisFileId){
        Map params = new HashMap();
        params.put("parentId", parentId);
        params.put("name", name.toLowerCase());
        params.put("thisFileId", thisFileId);
        EdsFileHeader fh = (EdsFileHeader) findSingleByNamedParams("select f from EdsFileHeader f " +
                "where f.folder.objectID=:parentId and lower(f.name)=:name and f.objectID<>:thisFileId", params);
        return fh != null;
    }

    @Override
    public boolean existsFolder(Integer parentId, String name) {
        Map params = new HashMap();
        params.put("parentId", parentId);
        params.put("name", name.toLowerCase());
        EdsFolder f = (EdsFolder) findSingleByNamedParams("select f from EdsFolder f " +
                "where f.parent.objectID=:parentId and lower(f.name)=:name and f.deleted is not true", params);
        return f != null;
    }

    @Override
    public List<EdsFolder> getFoldersPermittedForGroup(Integer userId, Integer groupId) {
        Map params = new HashMap();
        params.put("userId", userId);
        params.put("groupId", groupId);
        return findByNamedParams("select distinct f.folder from EdsFolderRbac f " +
                "where f.file=false and f.folder.owner.objectID=:userId and f.folder.deleted = false and f.group.objectID=:groupId ", params);
    }

    @Override
    public List<EdsFolder> getChildsByParentId(Integer parentId) {
        Map params = new HashMap();
        params.put("parentId", parentId);
        return findByNamedParams("select f from EdsFolder f where f.parent.objectID=:parentId and f.deleted=false", params);

    }

    @Override
    public List<EdsFolder> getSharedRootFolders(Integer userId) {
        Map params = new HashMap();
        params.put("userId", userId);
        List<EdsFolder> folders = findByNamedParams("select distinct f.folder from EdsFolderRbac f " +
                "where f.file=false and f.folder.owner.objectID=:userId and f.folder.deleted=false " +
                "and (f.group.objectID is not null or f.user.objectID != f.folder.owner.objectID) ", params);
        List<EdsFolder> result = new ArrayList<>();
        for (EdsFolder f : folders) {
            if (f.getType() != EdsFolder.TEMP) {
                if (!folders.contains(f.getParent())) {
                    result.add(f);
                }
            }
        }
        return result;
    }

    @Override
    public List<EdsFolder> getDeletedRootFolders(Integer userId) throws ObjectNotFoundException {
        if (userId == null) {
            throw new ObjectNotFoundException("No User specified");
        }
        Map params = new HashMap();
        params.put("userId", userId);
        return findByNamedParams("select f from EdsFolder f where f.owner.objectID=:userId and " +
                "f.deleted=true and f.parent.deleted=false", params);
    }

    @Override
    public List<EdsUser> getUsersSharingFoldersForUser(EdsUser user) {
        Map params = new HashMap();
        params.put("userId", user.getObjectID());
        params.put("type", EdsFolder.CUSTOM);
        params.put("trusteeType", EdsTrusteeType.USER);
        return findByNamedParams("select distinct f.folder.owner from EdsFolderRbac f " +
                "where f.file=false and f.folder.owner.objectID !=:userId and f.folder.deleted=false and f.folder.type=:type " +
                "and (f.user.objectID=:userId or f.group.objectID in (select distinct gg.objectID from EdsGroup gg join " +
                "gg.members memb where memb.objectID in (select distinct t.objectID from EdsTrustee t where t.trusteeID=:userId and t.type.objectID=:trusteeType))) ", params);
    }

    @Override
    public List<EdsFolder> getSharedRootFolders(Integer userId, Integer callingUserId) {
        Stopwatch watch = Stopwatch.createStarted();
        StringBuilder sql = new StringBuilder("SELECT distinct f.*, 0 as clazz_ FROM ").append(getCompanyId()).append(".folderrbac rbac ");
        sql
                .append("JOIN ").append(getCompanyId()).append(".folder f on f.id = rbac.folder_id \n")
                .append("JOIN ").append(getCompanyId()).append(".folderpermission perm on perm.id = rbac.documentPermissionId \n")
                .append("LEFT JOIN ").append(getCompanyId()).append(".trusteegroup tgroup on tgroup.id = rbac.groupid \n")
                .append("LEFT JOIN ").append(getCompanyId()).append(".trusteegroup_trustee tgm on tgm.trusteegroup_id = tgroup.id \n")
                .append("LEFT JOIN ").append(getCompanyId()).append(".trustee on trustee.id = tgm.members_id \n");
        sql
                .append("WHERE f.owner_id = " + userId + " AND f.deleted is not true \n")
                .append("AND f.type = " + EdsFolder.CUSTOM + " AND rbac.file is false AND perm.read is true \n")
                .append("AND (")
                .append("rbac.userid = " + callingUserId + " OR (trustee.trusteetype = " + EdsTrusteeType.USER + " AND trustee.trusteeID = " + callingUserId + ")")
                .append(" ) \n");
        List<EdsFolder> folders = findNative(sql.toString(), EdsFolder.class);
        List<EdsFolder> result = new ArrayList<>();
        for (EdsFolder f : folders) {
            if (!folders.contains(f.getParent())) {
                result.add(f);
            }
        }
        watch.elapsed(TimeUnit.MILLISECONDS);
        System.out.println("Shared Root Folders loading time " + watch);
        return result;
    }

    @Override
    public List<FolderResource> getSharedFolders(Integer userId) {
        return getSharedFolders(null, userId);
    }

    @Override
    public List<FolderResource> getSharedFolders(Integer ownerId, Integer userId) {
        StringBuilder sql = new StringBuilder("SELECT f.id, bool_or(perm.read) p_read, bool_or(perm.write) p_write, bool_or(perm.delete) p_delete, bool_or(perm.modifyACL) p_modifyACL ")
                .append(" FROM ").append(getCompanyId()).append(".folderrbac rbac ");
        sql
                .append("JOIN ").append(getCompanyId()).append(".folder f on f.id = rbac.folder_id \n")
                .append("JOIN ").append(getCompanyId()).append(".folderpermission perm on perm.id = rbac.documentPermissionId \n")
                .append("LEFT JOIN ").append(getCompanyId()).append(".trusteegroup tgroup on tgroup.id = rbac.groupid \n")
                .append("LEFT JOIN ").append(getCompanyId()).append(".trusteegroup_trustee tgm on tgm.trusteegroup_id = tgroup.id \n")
                .append("LEFT JOIN ").append(getCompanyId()).append(".trustee on trustee.id = tgm.members_id \n");
        sql
                .append("WHERE f.deleted is not true " + (ownerId != null ? " AND f.owner_id = " + ownerId : " AND f.owner_id != " + userId) + " \n")
                .append("AND f.type = " + EdsFolder.CUSTOM + " AND rbac.file is false AND perm.read is true \n")
                .append("AND (")
                .append("rbac.userid = " + userId + " OR (trustee.trusteetype = " + EdsTrusteeType.USER + " AND trustee.trusteeID = " + userId + ")")
                .append(" ) \n");
        sql.append("GROUP BY f.id \n");

        List<Object[]> list = findNative(sql.toString());
        List<Integer> folderIds = new ArrayList<>();

        Map<Integer, PermissionHolder> permissionMap = list.stream()
                .peek(objects -> folderIds.add((Integer) objects[0]))
                .collect(Collectors.toMap(objects -> (Integer) objects[0],
                        objects -> {
                            PermissionHolder p = new PermissionHolder();
                            p.setRead((Boolean) objects[1]);
                            p.setWrite((Boolean) objects[2]);
                            p.setDelete((Boolean) objects[3]);
                            p.setModifyACL((Boolean) objects[4]);
                            return p;
                        }));
        List<EdsFolder> folders = getFoldersByIds(folderIds);
        return folders.stream()
                .map(folder -> folder.getDTO())
                .peek(resource -> resource.setPermission(permissionMap.get(resource.getObjectId())))
                .collect(Collectors.toList());
    }

    public boolean isSharedFolder(Integer userId, Integer folderId) {
        return !isSharedFolderForOtherUser(userId, folderId);
    }

    public boolean isSharedFolderForOtherUser(Integer userId, Integer folderId) {
        Map params = new HashMap();
        params.put("userId", userId);
        params.put("folderId", folderId);
        params.put("trusteeType", EdsTrusteeType.USER);
        List<EdsFolder> folders = findByNamedParams("select distinct f.folder from EdsFolderRbac f " +
                "where f.file=false and f.folder.objectID = :folderId " +
                " and (f.user.objectID=:userId or f.group.objectID in " +
                "(select distinct gg.objectID from EdsGroup gg join gg.members memb where memb.objectID in " +
                "(select distinct t.objectID from EdsTrustee t where t.trusteeID=:userId and t.type.objectID=:trusteeType)))", params);
        return ArrayUtils.isEmpty(folders.toArray());
    }

    @Override
    public EdsFolder getFolder(Integer parentId, String name) {
        Map params = new HashMap();
        params.put("parentId", parentId);
        params.put("name", name);
        return (EdsFolder) findSingleByNamedParams("select f from EdsFolder f where f.parent.objectID=:parentId and f.name=:name", params);
    }

    @Override
    public EdsFolder getSystemFolder(Integer companyId) {
        Map params = new HashMap();
        params.put("type", EdsFolder.SYSTEM_BUILTIN);
        params.put("folderType", EdsFolder.F_DEFAULT);
        return (EdsFolder) findSingleByNamedParams("select f from EdsFolder f where f.type=:type and f.folderType=:folderType and f.parent is null", params);
    }

    @Override
    public EdsFolder getPublicFolder(Integer companyId) {
        Map params = new HashMap();
        params.put("type", EdsFolder.SYSTEM_BUILTIN);
        params.put("folderType", EdsFolder.F_COMPANY_PUBLIC_ROOT);
        return (EdsFolder) findSingleByNamedParams("select f from EdsFolder f where f.type=:type and f.folderType=:folderType and f.parent is null", params);
    }

    @Override
    public EdsFolder getProjectRootFolder(EdsCompany company) {
        Map params = new HashMap();
        params.put("type", EdsFolder.SYSTEM_BUILTIN);
        params.put("folderType", EdsFolder.F_PROJECT_ROOT);
        return (EdsFolder) findSingleByNamedParams("select f from EdsFolder f where f.type=:type and f.folderType=:folderType and f.parent is not null", params);
    }

    @Override
    public EdsFolder getExpensePaymentRootFolder(EdsCompany company) {
        Map params = new HashMap();
        params.put("type", EdsFolder.SYSTEM_BUILTIN);
        params.put("folderType", EdsFolder.F_EXP_PAYMENT);
        return (EdsFolder) findSingleByNamedParams("select f from EdsFolder f where f.type=:type and f.folderType=:folderType and f.parent is not null", params);
    }

    @Override
    public EdsFolder getMassMailingFolder(EdsCompany company) {
        Map params = new HashMap();
        params.put("type", EdsFolder.SYSTEM_BUILTIN);
        params.put("folderType", EdsFolder.F_MASS_MAILING);
        return (EdsFolder) findSingleByNamedParams("select f from EdsFolder f where f.type=:type and f.folderType=:folderType and f.parent is not null", params);
    }

    @Override
    public EdsFolder getCustomFieldRootFolder(EdsCompany company) {
        Map params = new HashMap();
        params.put("type", EdsFolder.SYSTEM_BUILTIN);
        params.put("folderType", EdsFolder.F_CUSTOM_FIELD_ROOT);
        return (EdsFolder) findSingleByNamedParams("select f from EdsFolder f where f.type=:type and f.folderType=:folderType and f.parent is not null", params);
    }

    @Override
    public EdsFolder getFolder(int folderType, Integer entityID) {
        Map params = new HashMap();
        params.put("folderType", folderType);
        if (entityID == null) {
            return (EdsFolder) findSingleByNamedParams("select f from EdsFolder f where f.folderType=:folderType and f.parent is not null", params);
        }
        params.put("entityId", entityID);
        return (EdsFolder) findSingleByNamedParams("select f from EdsFolder f where f.entityId=:entityId and f.folderType=:folderType and f.parent is not null", params);
    }

    @Override
    public EdsFolder getFolderByFolderType(int folderType) {
        Map params = new HashMap();
        params.put("folderType", folderType);
        return (EdsFolder) findSingleByNamedParams("select f from EdsFolder f where f.folderType=:folderType and f.parent is not null", params);
    }

    @Override
    public List<EdsFolder> getFoldersByFolderType(int folderType, EdsCompany company) {
        final Map params = new HashMap();
        params.put("folderType", folderType);
        return findByNamedParams("select f.objectID,f.name from EdsFolder f where f.folderType=:folderType", params);
    }

    public List<EdsFolder> getFolderNames(int type, Integer userId) {
        return find("select f.objectID,f.name from EdsFolder f where f.folderType=? and f.owner.objectID=?", type, userId);
    }

    @Override
    public void deleteFolderIndex(Integer folderId) {
        folderRbacManager.removeFolderEntries(folderId);
        /*try {
            solrManager.removeFolder(folderId);
        } catch (SolrServerException e) {
           baseEventPostProcessor.registerEvent(FolderCustomEventListenerImpl.TYPE,FolderCustomEventListenerImpl.EVENT_DELETE,get(folderId),getUser());
        } catch (IOException e) {
           baseEventPostProcessor.registerEvent(FolderCustomEventListenerImpl.TYPE,FolderCustomEventListenerImpl.EVENT_DELETE,get(folderId),getUser());
        }*/
    }

    @Override
    public void indexFolder(EdsFolder folder, boolean isUpdateExistingOne) {
        folderRbacManager.addRbacEntries(folder);
        folderRbacManager.flushAndClear();
        /*try {
            solrManager.addFolderToIndex(folder, folder.getCompany());
            //We are registering solrEvents to rollback solr transaction in case of Database transaction failure ONLY on CREATION NEW ONE
            if(!isUpdateExistingOne){
                SolrEvent sEvent = solrTransactionManager.registerEvent(SolrEvent.FOLDER_ADD,folder,folder.getCompany());
            }

        } catch (SolrServerException e) {
            baseEventPostProcessor.registerEvent(FolderCustomEventListenerImpl.TYPE,FolderCustomEventListenerImpl.EVENT_ADD,folder,getUser());
        } catch (IOException e) {
            baseEventPostProcessor.registerEvent(FolderCustomEventListenerImpl.TYPE,FolderCustomEventListenerImpl.EVENT_ADD,folder,getUser());
        }*/
    }

    @Override
    public void indexFolders(List<EdsFolder> folders, boolean isUpdateExistingOne) {
        for (EdsFolder edsFolder : folders) {
            indexFolder(edsFolder, isUpdateExistingOne);
        }
    }

    @Override
    public EdsFolder getProjectFolder(Integer projectId) {
        Map params = new HashMap();
        params.put("entityId", projectId);
        params.put("folderType", EdsFolder.F_PROJECT);
        return (EdsFolder) findSingleByNamedParams("select f from EdsFolder f where f.folderType=:folderType and f.entityId=:entityId", params);
    }

    @Override
    public EdsFolder getTaskFolder(Integer projectId) {
        Map params = new HashMap();
        params.put("entityId", projectId);
        params.put("folderType", EdsFolder.F_TASK);
        return (EdsFolder) findSingleByNamedParams("select f from EdsFolder f where f.folderType=:folderType and f.entityId=:entityId", params);
    }

    @Override
    public EdsFolder getPMIssueFolder(Integer projectId) {
        Map params = new HashMap();
        params.put("entityId", projectId);
        params.put("folderType", EdsFolder.F_PR_ISSUE);
        return (EdsFolder) findSingleByNamedParams("select f from EdsFolder f where f.folderType=:folderType and f.entityId=:entityId", params);
    }

    @Override
    public EdsFolder getTempFolder(Integer companyId) {
        Map params = new HashMap();
        //params.put("companyId", companyId);
        params.put("type", EdsFolder.TEMP);
        return (EdsFolder) findSingleByNamedParams("select f from EdsFolder f where f.type=:type and f.parent is null", params);
    }

    @Override
    public List<EdsFolder> getCompanyFolderListForSolr(SolrReindexRpc solrReindex, Integer start, Integer limit) {
        StringBuilder folderSqlQuery = new StringBuilder("select f.* ,0 as clazz_ from " + getCompanyId() + ".folder f ");
        folderSqlQuery.append("where f.id>").append(start);
        if (!solrReindex.isAllReindex() && solrReindex.getLastUpdateTime() != null) {
            folderSqlQuery.append(" and f.modificationDate>=").append("'").append(solrReindex.getLastUpdateTime()).append("'");
        }
        folderSqlQuery.append(" order by f.id asc limit ").append(limit);
        return findNative(folderSqlQuery.toString(), EdsFolder.class);
    }

    @Override
    public List<EdsFolder> getFoldersByIds(List<Integer> subIds) {
        Map params = new HashMap();
        params.put("ids", subIds);
        return findByNamedParams("from EdsFolder f where f.objectID in(:ids)", params);
    }

    @Override
    public EdsFolder getCustomFieldFolder(Integer cusFieldID) {
        Map params = new HashMap();
        params.put("entityId", cusFieldID);
        params.put("folderType", EdsFolder.F_CUSTOM_FIELD_ITEM);
        return (EdsFolder) findSingleByNamedParams("select f from EdsFolder f where f.folderType=:folderType and f.entityId=:entityId", params);
    }

    @Override
    public void createS3Key(String key) {
        updateNative("insert into s3keys(key) values('"+key+"');");
    }

    @Override
    public List<String> getS3Keys(String code) {
        StringBuilder sql = new StringBuilder();
        sql.append("select key from s3keys where key like '%2015/"+code+".jpg' or key like '%2016/"+code+".jpg' or key like '%2015/"+code+" %' or key like '%2016/"+code+" %' or key like '%2015/"+code+"b.jpg' or key like '%2016/"+code+"b.jpg'");
        return (List<String>)findNative(sql.toString());
    }

    @Override
    public void setFolderHasChild(boolean hasChild, Integer folderId) {
        update("update EdsFolder set hasChild = " + (hasChild ? "true" : "false") + " where objectID = " + folderId);
    }

}
