package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsReservation;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.db.ReservationManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Sanjar
 * Date: Feb 1, 2011
 * Time: 3:56:44 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("reservationManager")
public class ReservationManagerImpl extends BaseManager<EdsReservation> implements ReservationManager {
    public ReservationManagerImpl() {
        super(EdsReservation.class);
    }

    public List<EdsReservation> getByFilters(ListingFilterParameter fp) {
        boolean hasLimit = (fp.getLimit() != null && fp.getLimit() > 0);
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT w ");
        sql.append("FROM EdsReservation w WHERE w.deleted <> true ");
        sql.append("ORDER BY w.objectID DESC");

        int start = 0;
        if (fp.getStart() != null && fp.getStart() > 0) {
            start = fp.getStart();
        }
        if (hasLimit) {
            return findInterval(sql.toString(), start, fp.getLimit(), null);
        } else {
            return find(sql.toString(), start, null, null);
        }
    }
}
