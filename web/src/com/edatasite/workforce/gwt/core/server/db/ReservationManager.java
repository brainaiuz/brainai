package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsReservation;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sanjar
 * Date: Feb 1, 2011
 * Time: 3:55:45 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ReservationManager  extends Manager<EdsReservation> {
    List<EdsReservation> getByFilters(ListingFilterParameter filterParametrs);
}
