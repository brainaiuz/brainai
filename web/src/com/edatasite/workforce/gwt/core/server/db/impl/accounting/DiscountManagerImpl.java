package com.edatasite.workforce.gwt.core.server.db.impl.accounting;

import com.edatasite.workforce.core.domain.accounting.EdsDiscount;
import com.edatasite.workforce.gwt.accounting.client.rpc.discount.DiscountItem;
import com.edatasite.workforce.gwt.accounting.client.ui.AccountingConstants;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.accounting.DiscountManager;
import com.edatasite.workforce.gwt.core.server.db.impl.BaseManager;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: Dec 3, 2010
 * Time: 5:07:46 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("discountManager")
public class DiscountManagerImpl extends BaseManager<EdsDiscount> implements DiscountManager, AccountingConstants {

    public DiscountManagerImpl() {
        super(EdsDiscount.class);
    }

    @Override
    public List<EdsDiscount> list(ListingFilterParameter filterParametrs, ListLoadConfig config) {

        if (filterParametrs == null) {
            filterParametrs = new ListingFilterParameter();
        }

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT d FROM EdsDiscount d ");
        sql.append("WHERE d.deleted = false ");

        if (filterParametrs.getSqlSearchKey() != null && !filterParametrs.getSqlSearchKey().isEmpty()) {
            sql.append("AND (lower(d.name) like '" + filterParametrs.getSqlSearchKey() + "' OR lower(d.description) like '" + filterParametrs.getSqlSearchKey() + "') ");
        }

        if (filterParametrs.getSortField() != null) {
            String ascOrDesc = filterParametrs.getSortDir() == 2 ? "DESC" : "";
            if (CODE_COLUMN.equals(filterParametrs.getSortField())) {
                sql.append("ORDER BY d.code " + ascOrDesc);
            } else if (NAME_COLUMN.equals(filterParametrs.getSortField())) {
                sql.append("ORDER BY d.name " + ascOrDesc);
            } else if (TYPE_COLUMN.equals(filterParametrs.getSortField())) {
                sql.append("ORDER BY d.type " + (filterParametrs.getSortDir() != 2 ? "DESC" : ""));
            } else if (ACTIVE_COLUMN.equals(filterParametrs.getSortField())) {
                sql.append("ORDER BY d.isActive " + ascOrDesc);
            } else {
                sql.append("ORDER BY d.objectID DESC ");
            }
        } else {
            sql.append("ORDER BY d.objectID DESC ");
        }
        return findInterval(sql.toString(), filterParametrs.getStart(), filterParametrs.getLimit());
    }

    @Override
    public List<EdsDiscount> list(ListingFilterParameter filterParameter) {
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT d FROM EdsDiscount d ");
        sql.append("WHERE d.deleted = false ");
        if (filterParameter.getSqlSearchKey() != null) {
            sql.append("AND (lower(d.name) like '" + filterParameter.getSqlSearchKey() + "' OR ");
            sql.append("lower(d.code) like '" + filterParameter.getSqlSearchKey() + "' OR ");
            sql.append("lower(d.description) like '" + filterParameter.getSqlSearchKey() + "') ");
        }
        if (filterParameter.getSortField() != null && !"".equals(filterParameter.getSortField())) {
            if (DiscountItem.NAME_COLUMN.equals(filterParameter.getSortField())) {
                sql.append("ORDER BY d.name ");
            } else if (DiscountItem.CODE_COLUMN.equals(filterParameter.getSortField())) {
                sql.append("ORDER BY d.code ");
            } else if (DiscountItem.TYPE_COLUMN.equals(filterParameter.getSortField())) {
                sql.append("ORDER BY d.type ");
            } else if (AccountingConstants.ACTIVE_COLUMN.equals(filterParameter.getSortField())) {
                sql.append("order by d.isActive ");
            }
            if(!filterParameter.isAscending()){
                sql.append(" DESC ");
            }

        } else {
            sql.append("ORDER BY d.objectID DESC ");
        }

        return findInterval(sql.toString(),filterParameter.getStart(),filterParameter.getLimit());
    }

    @Override
    public EdsDiscount getDiscountByDiscountItem(DiscountItem discountItem) {
        List<EdsDiscount> discountList = find("select d from EdsDiscount d where d.name = ? and " + ServerUtils.checkForDeleted("d.deleted"), discountItem.getName());
        if (discountList != null && discountList.size() > 0) {
            for (EdsDiscount discount : discountList) {
                if (discount.getType().equals(discountItem.getType()) && Constants.SIMPLE_DISCOUNT.equals(discount.getType())) {
                    if (discountItem.getPercentage() != null && discountItem.getPercentage().compareTo(BigDecimal.ZERO) > 0 && discountItem.getPercentage().equals(discount.getPercentage())) {
                        return discount;
                    } else if (discountItem.getFixedAmount() != null && discountItem.getFixedAmount().compareTo(BigDecimal.ZERO) > 0 && discountItem.getFixedAmount().equals(discount.getFixedAmount())) {
                        return discount;
                    }
                }
            }
        }
        return null;
    }

	@Override
	public Integer listCount(ListingFilterParameter filterParametrs) {
		if (filterParametrs == null) {
			filterParametrs = new ListingFilterParameter();
		}

		StringBuilder sql = new StringBuilder();
		sql.append("SELECT count(d) FROM EdsDiscount d ");
		sql.append("WHERE d.deleted = false ");

		if (filterParametrs.getSqlSearchKey() != null && !filterParametrs.getSqlSearchKey().isEmpty()) {
			sql.append("AND (lower(d.name) like '" + filterParametrs.getSqlSearchKey() + "' OR lower(d.description) like '" + filterParametrs.getSqlSearchKey() + "') ");
		}

		return (Integer)findSingle(sql.toString());
	}
}
