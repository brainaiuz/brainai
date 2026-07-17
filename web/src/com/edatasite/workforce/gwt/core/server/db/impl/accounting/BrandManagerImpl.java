package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsBrand;
import com.edatasite.workforce.gwt.accounting.client.rpc.BrandItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.accounting.BrandManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Jun 17, 2010
 * Time: 2:37:35 AM
 * To change this template use File | Settings | File Templates.
 */
@Repository("brandManager")
public class BrandManagerImpl extends BaseManager<EdsBrand> implements BrandManager {

    public BrandManagerImpl() {
        super(EdsBrand.class);
    }

    public List<EdsBrand> getBrandList(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT b FROM EdsBrand b left join b.parentBrand pb WHERE " + ServerUtils.checkForDeleted("b.deleted"));
        getSqlWhereBrands(fp, sql);
        if (fp != null && fp.getSortField() != null) {
            if ("name".equals(fp.getSortField())) {
                sql.append(" ORDER BY b.name " + (fp.getSortDir() == 2 ? "desc" : ""));
            } else if ("description".equals(fp.getSortField())) {
                sql.append(" ORDER BY b.description " + (fp.getSortDir() == 2 ? "desc" : ""));
            } else if ("parent".equals(fp.getSortField())) {
                sql.append(" ORDER BY pb.name " + (fp.getSortDir() == 2 ? "desc" : ""));
            } else {
                sql.append(" ORDER BY b.objectID desc");
            }
        } else {
            sql.append(" ORDER BY b.objectID desc");
        }
        if (fp != null && fp.getStart() != null && fp.getLimit() != null) {
            return (List<EdsBrand>) findInterval(sql.toString(), fp.getStart(), fp.getLimit());
        }
        return find(sql.toString());
    }

    @Override
    public int getBrandListCount(ListingFilterParameter fp) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(b.objectID) FROM EdsBrand b left join b.parentBrand pb WHERE " + ServerUtils.checkForDeleted("b.deleted"));
        getSqlWhereBrands(fp, sql);
        return ((Long) findSingle(sql.toString())).intValue();
    }

    public StringBuilder getSqlWhereBrands(ListingFilterParameter fp, StringBuilder sql) {
        if (fp != null && fp.getSqlSearchKey() != null) {
            sql.append(" and (lower(b.name) like '" + fp.getSqlSearchKey() + "' or ");
            sql.append(" lower(b.description) like '" + fp.getSqlSearchKey() + "' or ");
            sql.append(" lower (pb.name) like '" + fp.getSqlSearchKey() + "') ");
        }
        return sql;
    }

    /**
     * returns true if amount greater than zero
     *
     * @param brandItem
     * @return
     */

    @Override
    public Boolean checkIfBrandExists(BrandItem brandItem) {
        if (brandItem.getId() == null) {
            return ((Long) findSingle("select count(objectID) from EdsBrand b where " + ServerUtils.checkForDeleted("b.deleted") + " and upper(b.name)=?", brandItem.getName().toUpperCase())) > 0;
        } else {
            return ((Long) findSingle("select count(objectID) from EdsBrand b where " + ServerUtils.checkForDeleted("b.deleted") + " and upper(b.name)=? and b.objectID != ?", brandItem.getName().toUpperCase(), brandItem.getId())) > 0;
        }
    }

    @Override
    public EdsBrand getBrandByName(String brandName) {
        return (EdsBrand) findSingle("select b from EdsBrand b where " + ServerUtils.checkForDeleted("b.deleted") + " and lower(trim(b.name))=?", brandName.toLowerCase());
    }

}
