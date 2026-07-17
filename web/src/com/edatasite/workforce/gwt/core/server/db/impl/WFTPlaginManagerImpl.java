package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsWFTPlagin;
import com.edatasite.workforce.gwt.backend.client.rpc.WFTPlaginListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.WFTPlaginManager;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Ilhombek
 * Date: 11.08.2010
 * Time: 17:20:50
 * To change this template use File | Settings | File Templates.
 */
@Repository("wftPlaginManager")
public class WFTPlaginManagerImpl extends BaseManager<EdsWFTPlagin> implements WFTPlaginManager {
    public WFTPlaginManagerImpl() {
        super(EdsWFTPlagin.class);
    }

    public List<EdsWFTPlagin> getLastVersionPlagin(ListingFilterParameter fp) {
        if (fp.getSortField() == null) {
            fp.setSortField(WFTPlaginListItem.OBJECT_ID);
        }
        StringBuilder sql = new StringBuilder();
        sql.append(" select pl ");
        sql.append(" from EdsWFTPlagin pl ");
        if (fp.getSqlSearchKey() != null) {
            sql.append(" where pl." + WFTPlaginListItem.UPDATER_NAME + ".email like '" + fp.getSqlSearchKey() + "' ");
        }
        sql.append(" order by  pl." + fp.getSortField() + " " + (fp.isAscending() ? " ASC " : "DESC"));
        return findInterval(sql.toString(), fp.getStart(), fp.getLimit());
    }

    public EdsWFTPlagin getPlugin(String plugin) {
        Map params = new HashMap();
        params.put("plugin", plugin);
        return (EdsWFTPlagin) findSingleByNamedParams("select pl from EdsWFTPlagin pl where pl.plagin=:plugin", params);
    }
}
