package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsAssemblyItemBuildHistory;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AssemblyHistoryManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 6/5/13
 * Time: 2:47 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("assemblyHistoryManager")
public class AssemblyHistoryManagerImpl extends BaseManager<EdsAssemblyItemBuildHistory> implements AssemblyHistoryManager {

    public AssemblyHistoryManagerImpl() {
        super(EdsAssemblyItemBuildHistory.class);
    }

    @Override
    public List<EdsAssemblyItemBuildHistory> getAssemblyBuildHistory(Integer assemblyID) {
        return find("select h from EdsAssemblyItemBuildHistory h where " + ServerUtils.checkForDeleted("h.deleted") + " and h.assemblyItemID=?", assemblyID);
    }

    @Override
    public boolean isAssemblyBuilded(Integer objectID) {
        return find("select h.objectID from EdsAssemblyItemBuildHistory h where " + ServerUtils.checkForDeleted("h.deleted") + " and h.assemblyItemID=?", objectID).size() > 0;
    }

    @Override
    public List<EdsAssemblyItemBuildHistory> getList(ListingFilterParameter fp) {
        StringBuffer sql = new StringBuffer();
        sql.append("select distinct a from  EdsAssemblyItemBuildHistory a  where (a.deleted = false or a.deleted is null) order by a.objectID desc");

        return findInterval(sql.toString(), 0, 50);

    }
}
