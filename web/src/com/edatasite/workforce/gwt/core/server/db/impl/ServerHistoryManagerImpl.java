package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsServerHistory;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.ServerHistoryManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 24.03.11
 * Time: 20:30
 * To change this template use File | Settings | File Templates.
 */

@Repository("serverHistoryManager")
public class ServerHistoryManagerImpl extends BaseManager<EdsServerHistory> implements ServerHistoryManager {

    public ServerHistoryManagerImpl() {
        super(EdsServerHistory.class);
    }

    public EdsServerHistory getLastServerHistory() {
        return (EdsServerHistory) findSingle("select log from EdsServerHistory log order by log.objectID desc");
    }

    public List<EdsServerHistory> list(ListingFilterParameter filterParameter) {
        StringBuilder sqlQuery = new StringBuilder("select rec from EdsServerHistory rec where rec.downTimeTo is not null ");
        final String direction = filterParameter.isAscending() ? " ASC " : " DESC ";
        final String orderBy = filterParameter.getSortField();
        if (orderBy != null) {
            sqlQuery.append(" ORDER BY ").append(orderBy).append(direction);
        } else {
            sqlQuery.append(" ORDER BY rec.objectID ").append(direction);
        }
        return find(sqlQuery.toString());
    }
}
