package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsPriceLevel;
import com.edatasite.workforce.gwt.accounting.client.rpc.enums.PriceLevelOperationTypeEnum;
import com.edatasite.workforce.gwt.accounting.client.rpc.priceLevel.PriceLevelItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.accounting.PriceLevelManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Jan 24, 2011
 * Time: 6:59:15 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("priceLevelManager")
public class PriceLevelManagerImpl extends BaseManager<EdsPriceLevel> implements PriceLevelManager, AccountingConstants {

    public PriceLevelManagerImpl() {
        super(EdsPriceLevel.class);
    }

    public String getListQuery(ListingFilterParameter filterParameter, boolean isCounter){
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ").append(isCounter ? "COUNT(DISTINCT pl.objectID)" : " DISTINCT pl ").append("FROM EdsPriceLevel pl ");
        getPermissionQuery(filterParameter, sql, true);
        sql.append(" AND pl.deleted = false ");

        if (!filterParameter.isFromListing()) {
            sql.append(" and pl.operationType = '").append(filterParameter.isSpecialOffer() ? PriceLevelOperationTypeEnum.FOR_SUPPLIER.name() : PriceLevelOperationTypeEnum.FOR_CLIENT.name()).append("' ");
        }
        if (filterParameter.getCurrencyID() != null){
            sql.append(" AND pl.currency.objectID = " + filterParameter.getCurrencyID()+" ");
        }
        if(filterParameter.getType() != null){
            sql.append(" and pl.type = ").append(filterParameter.getType().toString());
        }
        if(filterParameter.isCorporate()){
            sql.append(" and pl.type <> 3 ");
        }
        if (filterParameter.getSqlSearchKey() != null) {
            sql.append(" AND lower(pl.name) like '" + filterParameter.getSqlSearchKey() + "' ");
        }
        if(!isCounter){
            if (filterParameter.getSortField() != null && !"".equals(filterParameter.getSortField())) {
                if (PriceLevelItem.NAME.equals(filterParameter.getSortField())) {
                    sql.append("ORDER BY pl.name ");
                } else if (PriceLevelItem.TYPE.equals(filterParameter.getSortField())) {
                    sql.append("ORDER BY pl.type ");
                } else if (PriceLevelItem.PLCASE.equals(filterParameter.getSortField())) {
                    sql.append("ORDER BY pl.plCase ");
                }
                if (!filterParameter.isAscending()) {
                    sql.append(" DESC ");
                }
            } else {
                sql.append("ORDER BY pl.objectID DESC ");
            }
        } else {
            sql.append("GROUP BY pl.objectID");
        }
        System.out.println(sql);
        return sql.toString();
    }

    private void getPermissionQuery(ListingFilterParameter filterParameter, StringBuilder sql, boolean is_hql) {
        //start of object permission check* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
        sql.append("LEFT join pl.roles role ");
        sql.append(" WHERE ");
        if(filterParameter.isShowHidden()){
            sql.append(" 1=1 ");
        } else {
            sql.append(" (");
            sql.append(" pl.denied=false");
            sql.append(" or pl.objectCreatorId = ").append(getUser().getObjectID());
            sql.append(" or role.objectID in (").append(getUser().getRolesAsIntegersString()).append(")");
            sql.append(" ) ");
        }
        //end of object permission check* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
    }

    @Override
    public Integer listCount(ListingFilterParameter fp) {
        Object result = findSingle(getListQuery(fp, true));
        return result == null ? 0 : Integer.valueOf(result.toString());
    }


    @Override
    public List<EdsPriceLevel> list(ListingFilterParameter fp) {
        return findInterval(getListQuery(fp, false), fp.getStart(), fp.getLimit());
    }

    @Override
    public void deletePriceLevel(Integer objectID) {
        update("UPDATE EdsPriceLevel pl SET pl.deleted = true WHERE pl.objectID = ?", objectID);
    }

    @Override
    public List<EdsPriceLevel> getPriceLevelsByIds(String Ids) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setShowHidden(true);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT distinct pl FROM EdsPriceLevel pl ");
        getPermissionQuery(fp, sql, true);
        sql.append(" AND ").append(ServerUtils.checkForDeleted("pl.deleted"));
        sql.append(" AND ").append(" and pl.objectID IN (").append(Ids).append(")");

        return (List<EdsPriceLevel>) find(sql.toString());
    }

    @Override
    public List<EdsPriceLevel> getPriceLevelsByNotExportedToQB(Integer limit) {
        StringBuilder sql = new StringBuilder();
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setShowHidden(true);
        sql.append("SELECT distinct pl FROM EdsPriceLevel pl ");
        getPermissionQuery(fp, sql, true);
        sql.append(" AND ").append(ServerUtils.checkForDeleted("pl.deleted"));
        sql.append(" AND pl.quickbookPriceLevelID is null order by pl.objectID ASC");
        return (List<EdsPriceLevel>) findLimited(sql.toString(), limit);
    }

    @Override
    public EdsPriceLevel getPriceLevelByName(String name) {
        return (EdsPriceLevel) findSingle("select pl from EdsPriceLevel pl where pl.name=?", name);
    }

    @Override
    public List<EdsPriceLevel> getPriceLevels(Integer currenceId, Integer clientId, boolean showHiddens) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setShowHidden(showHiddens);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT distinct pl FROM EdsPriceLevel pl left join pl.clients c ");
        getPermissionQuery(fp, sql, true);
        sql.append(" AND ").append(ServerUtils.checkForDeleted("pl.deleted"));
        sql.append(currenceId != null ? " AND pl.currency.objectID=? " : "").append(" AND c.objectID=?");
        return (List<EdsPriceLevel>) (currenceId != null ? find(sql.toString(), currenceId, clientId) : find(sql.toString(), clientId));
    }

    @Override
    public boolean isPricelLevelNameExists(String name, Integer id) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setShowHidden(true);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT distinct pl FROM EdsPriceLevel pl left join pl.clients c ");
        getPermissionQuery(fp, sql, true);
        sql.append(" AND ").append(ServerUtils.checkForDeleted("pl.deleted"));
        sql.append(" AND pl.name=?").append(id!=null ? " AND pl.objectID != ?" : "");
        String q = sql.toString();
        return (id != null ? find(q, name.trim(), id) : find(q, name.trim())).size() > 0;
    }

    @Override
    public List<EdsPriceLevel> getPriceLevelsByClientType(Integer clientTypeID, Integer currencyID, boolean showHiddens) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setShowHidden(showHiddens);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT distinct pl FROM EdsPriceLevel pl ");
        sql.append(" LEFT JOIN pl.clientTypes c_types ");
        getPermissionQuery(fp, sql, true);
        sql.append(" AND ").append(ServerUtils.checkForDeleted("pl.deleted"));
        if(currencyID != null){
            sql.append(" AND pl.currency.objectID = ").append(currencyID);
        }
        if(clientTypeID != null){
            sql.append(" AND c_types.objectID=").append(clientTypeID);
        }
        System.out.println(sql);
        return find(sql.toString());
//        StringBuilder sql = new StringBuilder();
//        sql.append("select pl.* from ").append(getCompanyId()).append(".client_type_price_level ctpl ");
//        sql.append("left join ").append(getCompanyId()).append(".price_level pl  on pl.id=ctpl.price_level_id ");
//        sql.append("where ").append(ServerUtils.checkForDeleted("pl.deleted")).append(" and ctpl.client_type_id=").append(clientTypeID);
//        if(currencyID != null){
//            sql.append(" and pl.currencyid=").append(currencyID);
//        }
//        return findNative(sql.toString(), EdsPriceLevel.class);
    }
}
