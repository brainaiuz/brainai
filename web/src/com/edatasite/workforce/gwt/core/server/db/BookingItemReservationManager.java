package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsBookingItem;
import com.edatasite.workforce.core.domain.EdsBookingItemReservation;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: developer
 * Date: 5/21/12
 * Time: 12:52 PM
 * To change this template use File | Settings | File Templates.
 */
public interface BookingItemReservationManager extends Manager<EdsBookingItemReservation> {

	List<EdsBookingItemReservation> getBookingItemReservationByid(Integer reservationItemId);

	List<EdsBookingItemReservation> getReservationByBookingid(Integer ItemId);

	List<EdsBookingItemReservation> getValidationReservation(Integer ItemId, Date fromDate, Date toDate);

    List<EdsBookingItem> getAvailableBookingItems(ListingFilterParameter fp);

	List<EdsBookingItemReservation> getReservationStatus(Integer ItemId);

	void deleteReservation(EdsBookingItemReservation reservation);

}
