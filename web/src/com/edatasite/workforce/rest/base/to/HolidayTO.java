package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.gwt.availability.client.rpc.HolidayItem;

/**
 * Created by Dilshod Madrahimov.
 */
public class HolidayTO extends SelectItemTO {

    private Long fromDate;
    private Long toDate;

    public HolidayTO() {
    }

    public HolidayTO(HolidayItem item) {
        this.id = item.getObjectID();
        this.name = item.getName();
        this.description = item.getDescription();
        this.fromDate = item.getFrom().getDateLong();
        this.toDate = item.getTo().getDateLong();
    }

    public Long getFromDate() {
        return fromDate;
    }

    public Long getToDate() {
        return toDate;
    }
}
