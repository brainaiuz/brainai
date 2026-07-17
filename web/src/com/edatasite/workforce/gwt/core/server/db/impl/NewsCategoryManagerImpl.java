package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsNewsCategory;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.NewsCategoryManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Oct 23, 2009
 * Time: 5:31:08 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("newsCategoryManager")
public class NewsCategoryManagerImpl extends BaseManager<EdsNewsCategory> implements NewsCategoryManager {
    public NewsCategoryManagerImpl() {
        super(EdsNewsCategory.class);
    }

    public List<EdsNewsCategory> getCategories() {
        return find("from EdsNewsCategory WHERE deleted = false order by name asc");
    }

    private void getSqlWhereList(StringBuilder sql, ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        sql.append(" FROM EdsNewsCategory c WHERE c.deleted = false ");

        if (fp.getSearchKey() != null && !fp.getSearchKey().isEmpty()) {
            sql.append(" AND lower(c.name) like '%" + fp.getSearchKey() + "%' ");
        }

    }

    @Override
    public List<EdsNewsCategory> list(ListingFilterParameter fp) {

        StringBuilder sql = new StringBuilder();

        sql.append("SELECT c ");
        getSqlWhereList(sql, fp);
        if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
            sql.append(" ORDER BY ");
            if ("name".equals(fp.getSortField())) {
                sql.append(" c.name ");
            } else {
                sql.append(" c.name ");
            }

            if (fp.isAscending()) {
                sql.append(" ASC ");
            } else {
                sql.append(" DESC ");
            }
        } else {
            sql.append("ORDER BY c.objectID DESC ");
        }
        return findInterval(sql.toString(), fp.getStart(), fp.getLimit());

    }

    @Override
    public Integer getListCount(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(c.objectID)");
        getSqlWhereList(sql, fp);
        Long result = (Long) findSingle(sql.toString());
        if (result != null) {
            return result.intValue();
        }
        return 0;
    }

    @Override
    public EdsNewsCategory getByName(String name) {
        return (EdsNewsCategory)findSingle("SELECT nc FROM EdsNewsCategory nc WHERE nc.name like '%" + name.trim() + "%'");
    }
}
