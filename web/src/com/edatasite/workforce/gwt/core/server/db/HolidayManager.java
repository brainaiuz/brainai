package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsHoliday;
import com.edatasite.workforce.core.domain.EdsLocation;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public interface HolidayManager extends Manager<EdsHoliday> {

    List<EdsHoliday> list(ListingFilterParameter fp, boolean isTotal);

    List<EdsHoliday> getCalendarHolidays(EdsLocation location, Date start, Date end);

    List<EdsHoliday> getHolidays(EdsLocation location, ListingFilterParameter fp);

    ArrayList<EdsHoliday> getHolidaysByRecurrenceID(Integer recurrenceID);

    List<EdsHoliday> getHolidaysByDatesAndLocation(Date start, Date end, EdsLocation location);

    void deleteHoliday(Integer holidayID);

    List<EdsHoliday> getLocationHolidays(Date start, EdsLocation location);

    void removeRecurrenceIDFromRecurringHoliday(Integer recurrenceID);
}
