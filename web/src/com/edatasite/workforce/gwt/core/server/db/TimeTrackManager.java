package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsTimeTrack;
import com.edatasite.workforce.core.domain.StaffInOut;

import java.util.Date;
import java.util.List;

public interface TimeTrackManager extends Manager<EdsTimeTrack> {

    EdsTimeTrack getLatestTimeEntry(Integer employeeID);

    List<EdsTimeTrack> getTimeEntriesByToday(Integer employee, String today);

    List getMonthTimeTrack(Integer teamId, Integer locationId, Date startDate, Date endDate, boolean withLunchTime);

    void deleteEqualsStartDate(Integer employeeId, Date startDate);

    void deleteByShiftId(Integer shiftId);

    List<StaffInOut> getEmployeeTimeTrackInPeriod(Integer employeeId, Date startDate, Date endDate);

    void insertTimeTrack(Integer companyID, Integer employeeID, Date startDate, Integer statusID, boolean isFingerPrintUser);

    void insertTimeTrack(Integer companyID, Integer employeeID, Date startDate, Date endDate, Integer statusID, boolean isFingerPrintUser);

    void insertTimeTrack(Integer companyID, Integer employeeID, Date startDate, Date endDate, Integer statusID, String location);

    List<EdsTimeTrack> getUserAvailableTimeTrackInPeriod(Integer userId, Date startDate, Date endDate);

}
