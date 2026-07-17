package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsBookingItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.BookingItemManager;
import com.edatasite.workforce.gwt.project.client.rpc.BookingItemsItem;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: developer
 * Date: 5/21/12
 * Time: 12:55 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("bookingItemManager")
public class BookingItemManagerImpl extends BaseManager<EdsBookingItem> implements BookingItemManager {

	public BookingItemManagerImpl(){
		super(EdsBookingItem.class);
	}

	@Override
	public List<EdsBookingItem> getBookingItemById(Integer itemId) {
		return find("select item from EdsBookingItem item where item.objectID=?", itemId);
	}
	@Override
	public List<EdsBookingItem> getBookingItemList() {
		return find("select item from EdsBookingItem item order by id");
	}

    public List<EdsBookingItem> getBookingItemList(ListingFilterParameter fp) {
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT bk  FROM EdsBookingItem bk ");
        sql.append("left join bk.category c ");
        sql.append("left join bk.location l ");
        sql.append("WHERE bk.deleted <> true");
        //searching
        if (fp.getSearchKey() != null && !fp.getSearchKey().isEmpty()) {
            sql.append(" AND (");
            sql.append(" LOWER(bk.itemNumber) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR LOWER(bk.name) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR LOWER(bk.description) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR LOWER(c.name) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(")");
        }
        sql.append(" ORDER BY ");
        //sorting
        if (fp.getSortField() != null && !"".equals(fp.getSortField())) {
            if (BookingItemsItem.ITEM_NUMBER.equals(fp.getSortField())) {
                sql.append("bk.itemNumber");
            } else if (BookingItemsItem.ITEM_NAME.equals(fp.getSortField())) {
                sql.append("bk.name");
            } else if (BookingItemsItem.CATEGORY.equals(fp.getSortField())) {
                sql.append("c.name");
            }
            if (fp.getSortDir() != null) {
                if (Integer.valueOf(1).equals(fp.getSortDir())) {
                    sql.append(" ASC");
                } else {
                    sql.append(" DESC");
                }
            } else {
                sql.append(" DESC");
            }
        } else {
            sql.append("bk.objectID DESC");
        }
        return findInterval(sql.toString(),fp.getStart(),fp.getLimit());
    }

    public Integer getBookingItemTotalCount(ListingFilterParameter fp){
        if (fp == null) {
            fp = new ListingFilterParameter();
        }
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT count(bk.objectID) FROM EdsBookingItem bk ");
        sql.append("left join bk.category c ");
        sql.append("left join bk.location l ");
        sql.append("WHERE bk.deleted <> true ");
        //searching
        if (fp.getSearchKey() != null && !"".equals(fp.getSearchKey())) {
            sql.append(" AND (");
            sql.append(" LOWER(bk.itemNumber) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR LOWER(bk.name) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR LOWER(bk.description) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(" OR LOWER(c.name) LIKE '").append(fp.getSqlSearchKey()).append("' ");
            sql.append(")");
        }
        Long count = (Long) findSingle(sql.toString());
        return count != null ? count.intValue() : 0;
    }

	@Override
	public Integer getBookingItemLastIntNumber() {
		return (Integer) findSingle("select item.intNumber from EdsBookingItem item where  item.intNumber is not null order by item.intNumber desc");
	}

	@Override
	public List<EdsBookingItem> getBookingItemListByCategoryId(Integer categoryId) {
        return find("select item from EdsBookingItem item where item.deleted <> true and item.category.objectID=?", categoryId);
	}
}
