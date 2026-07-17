package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsPickList;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.accounting.PickListManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Apr 24, 2010
 * Time: 5:55:50 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("pickListManager")
public class PickListManagerImpl extends BaseManager<EdsPickList> implements PickListManager {
    public PickListManagerImpl() {
        super(EdsPickList.class);
    }

    @Override
    public EdsPickList getPickListBySaleQuoteID(Integer saleQuoteID) {
        if (saleQuoteID == null) {
            return  null;
        }
        final String sql = "SELECT pl FROM EdsPickList pl " +
                           "    WHERE pl.saleQuote.objectID = :saleQuoteId";
        final List<EdsPickList> result = this.slaveEntityManager.createQuery(sql, EdsPickList.class)
                                                           .setMaxResults(1)
                                                           .setParameter("saleQuoteId", saleQuoteID)
                                                           .getResultList();

        return result.isEmpty() ? null : result.get(0);
    }

    public List<EdsPickList> list(ListingFilterParameter fp) {
        StringBuffer sql = new StringBuffer();
        sql.append("select pl ");
        getSqlFromWhereList(fp, sql);
        if (fp != null && fp.getSortField() != null) {
            String ascOrDesc = fp.getSortDir() == 2 ? " desc" : "";
            if ("client".equals(fp.getSortField())) {
                sql.append(" order by sq.client.name " + ascOrDesc);
            } else if ("dueDate".equals(fp.getSortField())) {
                sql.append(" order by sq.dueDate " + ascOrDesc);
            } else if ("discount".equals(fp.getSortField())) {
                sql.append(" order by sq.discount " + ascOrDesc);
            } else if ("total".equals(fp.getSortField())) {
                sql.append(" order by sq.total " + ascOrDesc);
            } else if ("shipDate".equals(fp.getSortField())) {
                sql.append(" order by pl.shipDate " + ascOrDesc);
            } else if ("expectedDate".equals(fp.getSortField())) {
                sql.append(" order by pl.expectedDate " + ascOrDesc);
            } else if ("status".equals(fp.getSortField())) {
                sql.append(" order by st.name " + ascOrDesc);
            } else {
                sql.append(" order by pl.objectID desc");
            }
        } else {
            sql.append(" order by pl.objectID desc");
        }
         return findInterval(sql.toString(), fp.getStart(), fp.getLimit());
    }

    private void getSqlFromWhereList(ListingFilterParameter fp, StringBuffer sql) {
        sql.append(" from EdsPickList pl left join pl.saleQuote sq left join pl.status st where 1=1");
        if (fp != null && fp.getSqlSearchKey() != null) {
            sql.append(" and ( lower(sq.client.name) like '" + fp.getSqlSearchKey() + "' " +
                    " or lower(st.name) like '" + fp.getSqlSearchKey() + "')");
        }
    }

    @Override
    public void deletePickListAndItemsByQuote(Integer saleQuoteID) {
        List<EdsPickList> pickLists = find("select pl from EdsPickList pl where pl.saleQuote.objectID = ?", saleQuoteID);
        for(EdsPickList pl : pickLists){
            update("delete from EdsPickListItem pli where pli.pickList = ?", pl);
            delete(pl);
        }
    }

    @Override
    public Integer listCount(ListingFilterParameter fp) {
        StringBuffer sql = new StringBuffer();
        sql.append("select count(pl) ");
        getSqlFromWhereList(fp, sql);
        return Integer.parseInt(findSingle(sql.toString()).toString());
    }

    @Override
    public List<EdsPickList> getPickListBySaleQuoteIDs(String ids) {
        return find("SELECT pl FROM EdsPickList pl WHERE pl.saleQuote.objectID in (" + ids + ")");
    }
}
