package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsSavedAssemblyItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.SavedAssemblyItemManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("savedAssemblyItemManager")
public class SavedAssemblyItemManagerImp extends BaseManager<EdsSavedAssemblyItem> implements SavedAssemblyItemManager {

    public SavedAssemblyItemManagerImp() {
        super(EdsSavedAssemblyItem.class);
    }

    @Override
    public Integer getAssemblyLastIntNumber() {
        return (Integer) findSingle("select bce.intNumber from EdsSavedAssemblyItem bce where (bce.deleted = false or bce.deleted is null) and bce.intNumber is not null order by bce.intNumber desc");
    }

    @Override
    public List<EdsSavedAssemblyItem> getList(ListingFilterParameter fp) {
        StringBuffer sql = new StringBuffer();
        sql.append("select distinct a from EdsSavedAssemblyItem a where (a.deleted = false or a.deleted is null) order by a.objectID desc");
        return findInterval(sql.toString(), fp.getStart(), fp.getLimit());
    }

    @Override
    public Integer getTotalCount(ListingFilterParameter fp) {
        StringBuffer sql = new StringBuffer();
        sql.append("select count (*) from ").append(getCompanyId());
        sql.append(".savedAssemblyItem where deleted is not true");
        return Integer.parseInt(findNativeSingle(sql.toString()).toString());
    }

    @Override
    public Boolean isSavedAssemblyItemExist(String code, Integer objectID) {
        StringBuffer sql = new StringBuffer("select eds from EdsSavedAssemblyItem eds");
        sql.append(" where eds.deleted is not true and eds.assemblyItemCode = ?").append(objectID != null ? " and eds.objectID is not ?" : "");
        if (objectID != null) {
            return (EdsSavedAssemblyItem) findSingle(sql.toString(), code, objectID) != null;
        } else {
            return (EdsSavedAssemblyItem) findSingle(sql.toString(), code) != null;
        }
    }
}
