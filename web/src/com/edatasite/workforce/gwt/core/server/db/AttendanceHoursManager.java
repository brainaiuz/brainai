package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsAttendanceHour;
import com.edatasite.workforce.core.domain.EdsShift;
import com.edatasite.workforce.gwt.core.client.enums.AttendanceHoursType;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

public interface AttendanceHoursManager extends Manager<EdsAttendanceHour> {
    void insertEmployeeHours(Integer employeeId, Date start, Date end, EdsShift shift, Integer timeslotId);

    void deleteEqualsStartDate(Integer employeeId, Date date, AttendanceHoursType type);

    void deleteByShiftId(Integer shiftId);

    Map<Date, BigDecimal> getAttendanceHours(ListingFilterParameter fp);
}
