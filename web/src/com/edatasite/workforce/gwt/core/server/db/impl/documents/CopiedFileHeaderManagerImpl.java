package com.edatasite.workforce.gwt.core.server.db.impl.documents;

import com.edatasite.workforce.core.domain.documents.EdsCopiedFileHeader;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.documents.CopiedFileHeaderManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by : Faxriddin Taslimov  * Date: 30.09.2015
 */
@Repository("copiedFileHeaderManager")
public class CopiedFileHeaderManagerImpl extends BaseManager<EdsCopiedFileHeader> implements CopiedFileHeaderManager {

    public CopiedFileHeaderManagerImpl() {
        super(EdsCopiedFileHeader.class);
    }

    @Override
    public List<EdsCopiedFileHeader> list(ListingFilterParameter fp) {
        return (List<EdsCopiedFileHeader>) find("select f from EdsCopiedFileHeader f where f.deleted=false and f.folder.deleted=false order by f.objectID desc");
    }

    @Override
    public EdsCopiedFileHeader getFile(Integer folderId, String name) {
        Map params = new HashMap();
        params.put("parentId", folderId);
        params.put("name", name);
        return (EdsCopiedFileHeader) findSingleByNamedParams("select f from EdsCopiedFileHeader f where f.folder.objectID=:parentId and f.name=:name and f.deleted<>true ", params);
    }

    @Override
    public boolean existsFile(Integer parentId, String name) {
        Map params = new HashMap();
        params.put("parentId", parentId);
        params.put("name", name.toLowerCase());

        EdsCopiedFileHeader fh = (EdsCopiedFileHeader) findSingleByNamedParams("select f from EdsCopiedFileHeader f " +
                "where f.folder.objectID=:parentId and lower(f.name)=:name", params);
        return fh != null;
    }

    @Override
    public List<Integer> getFileHeaders(ListingFilterParameter filter) {
        return find("select f.fileHeaderId from EdsCopiedFileHeader f where f.entityId =? and f.folder.objectID=? and f.deleted<>true", filter.getCrmEntityId(), filter.getFolderId());
    }

    @Override
    public EdsCopiedFileHeader getCopiedFile(Integer fileHeaderId, Integer folderId) {
        Map params = new HashMap();
        params.put("parentId", folderId);
        params.put("fileheader", fileHeaderId);
        return (EdsCopiedFileHeader) findSingleByNamedParams("select f from EdsCopiedFileHeader f where f.folder.objectID=:parentId and f.fileHeaderId=:fileheader and f.deleted<>true", params);
    }

    @Override
    public List<EdsCopiedFileHeader> getCopiedFile(Integer fileHeaderId) {
        Map params = new HashMap();
        params.put("fileheader", fileHeaderId);
        return (List<EdsCopiedFileHeader>) findByNamedParams("select f from EdsCopiedFileHeader f where f.fileHeaderId=:fileheader and f.deleted<>true", params);
    }

    @Override
    public List<EdsCopiedFileHeader> getListByFolderIdAndEntityId(ListingFilterParameter fp) {
        if (fp.getFolderId() == null || fp.getEntityID() == null) {
            return Lists.newArrayList();
        }
        String sqlQuery = "select cf from EdsCopiedFileHeader cf " +
                          " where cf.deleted = false " +
                          "     and cf.folder.objectID =:folderId " +
                          "     and cf.entityId =:entityId ";

        return slaveEntityManager.createQuery(sqlQuery, EdsCopiedFileHeader.class)
                            .setParameter("folderId", fp.getFolderId())
                            .setParameter("entityId", fp.getEntityID())
                            .setFirstResult(fp.getStart())
                            .setMaxResults(fp.getLimit() > 0 ? fp.getLimit() : 100)
                            .getResultList();
    }
}