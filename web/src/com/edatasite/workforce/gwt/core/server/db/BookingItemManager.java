package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsBookingItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: developer
 * Date: 5/21/12
 * Time: 12:52 PM
 * To change this template use File | Settings | File Templates.
 */
public interface BookingItemManager extends Manager<EdsBookingItem> {

	List<EdsBookingItem> getBookingItemById(Integer itemId);

	List<EdsBookingItem> getBookingItemList();

	List<EdsBookingItem> getBookingItemListByCategoryId(Integer categoryId);

	Integer getBookingItemLastIntNumber();

    List<EdsBookingItem> getBookingItemList(ListingFilterParameter fp);

    Integer getBookingItemTotalCount(ListingFilterParameter fp);
}
