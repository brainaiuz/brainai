package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsTimeSlot;
import com.edatasite.workforce.gwt.availability.client.rpc.FingerprintTimeDto;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface TimeSlotManager extends Manager<EdsTimeSlot> {

    List<EdsTimeSlot> getTimeslots(ListingFilterParameter fp);

    List<Object[]> getTimeslotsForListing(ListingFilterParameter fp);

    List<Object[]> getResult(Integer departmentFilter, Integer employeeFilter, Integer viewAsFilter, Date from, Date to);

    List<Object[]> getInUotReportCustom(Integer teamID, Integer locationID, Integer employeeID, Integer viewAsFilter, Date from, Date to, boolean withLunchTime, String employeeIds, String departmentIds);

    Map<Integer, Map<Integer, FingerprintTimeDto>> getFingerprintData(Integer departmentId, String employeeIds, Integer viewAsFilter, Date from, Date to);

    List<EdsTimeSlot> getTimeslots();

    SelectItem[] getTimeslotsAsSelectItem(ListingFilterParameter fp);

    EdsTimeSlot getTimeSlotByShortName(String shortName);
}
