package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsBookingItem;
import com.edatasite.workforce.core.domain.EdsBookingItemReservation;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.BookingItemReservationManager;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: developer
 * Date: 5/21/12
 * Time: 12:55 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("bookingItemReservationManager")
public class BookingItemReservationManagerImpl extends BaseManager<EdsBookingItemReservation> implements BookingItemReservationManager {

	public BookingItemReservationManagerImpl() {
		super(EdsBookingItemReservation.class);
	}

	@Override
	public List<EdsBookingItemReservation> getBookingItemReservationByid(Integer reservationItemId) {

		return find("select reservationItem from EdsBookingItemReservation reservationItem where reservationItem.objectID=? AND (reservationItem.deleted is null OR reservationItem.deleted is not true)", reservationItemId);
	}

	@Override
	public List<EdsBookingItemReservation> getReservationByBookingid(Integer ItemId) {
		return find("select reservationItem from EdsBookingItemReservation reservationItem where (reservationItem.deleted is null OR reservationItem.deleted is not true) AND reservationItem.bookingItem.objectID=?", ItemId);
	}

	@Override
	public List<EdsBookingItemReservation> getValidationReservation(Integer ItemId, Date fromDate, Date toDate) {
		return find("select reservationItem from EdsBookingItemReservation reservationItem where (reservationItem.deleted is null OR reservationItem.deleted is not true) AND reservationItem.bookingItem.objectID=? and (? <= reservationItem.to and ? >= reservationItem.from)", ItemId, fromDate, toDate);
	}

    @Override
    public List<EdsBookingItem> getAvailableBookingItems(ListingFilterParameter fp) {
        Map<String, Object> map = new HashMap<>();
        map.put("startDate", fp.getStartDate());
        map.put("endDate", fp.getEndDate());
        map.put("locationID", fp.getLocationId());

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT bi FROM EdsBookingItem bi ");
        sql.append("WHERE bi.objectID NOT IN (SELECT br.bookingItem.objectID FROM EdsBookingItemReservation br WHERE " +
                "((:startDate between br.from and  br.to) or (:endDate between br.from and  br.to) or (br.from between :startDate and :endDate) or (br.to between :startDate and :endDate)) ");

        if (fp.getReservationIds() != null && !fp.getReservationIds().isEmpty()) {
            sql.append(" AND br.objectID NOT IN (").append(fp.getReservationIds()).append(") ");
        }

        sql.append(") ");

        if (fp.getCategoryID() != null) {
            sql.append(" AND bi.category.objectID = ").append(fp.getCategoryID());
        }

        if (fp.getItemId() != null) {
            sql.append(" AND bi.objectID = ").append(fp.getItemId());
        }

        sql.append(" AND bi.location.objectID = :locationID");
        return findByNamedParams(sql.toString(), map);
    }

    @Override
	public List<EdsBookingItemReservation> getReservationStatus(Integer ItemId) {
		return find("select reservationItem from EdsBookingItemReservation reservationItem where (reservationItem.deleted is null OR reservationItem.deleted is not true) AND reservationItem.bookingItem.objectID=? and (now() between reservationItem.from and reservationItem.to)", ItemId);
	}

	public void deleteReservation(EdsBookingItemReservation reservation) {
		update("update EdsBookingItemReservation br set br.deleted=true where br=?", reservation);
	}
}
