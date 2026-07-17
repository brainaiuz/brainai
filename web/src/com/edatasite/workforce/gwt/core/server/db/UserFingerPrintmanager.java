package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsUserFingerPrintDevice;
import com.edatasite.workforce.core.domain.EdsUsersFingerPrint;
import com.edatasite.workforce.core.domain.GymInOutTO;
import com.edatasite.workforce.core.domain.StaffInOut;
import com.edatasite.workforce.gwt.availability.client.rpc.AttendanceMarkListItem;
import com.edatasite.workforce.gwt.availability.client.rpc.InOutPairDto;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.rest.v3.release10.core.to.InOutReportTO;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * User: Djuraev  * Date: 4/12/14
 */
public interface UserFingerPrintmanager extends Manager<EdsUsersFingerPrint> {

    EdsUsersFingerPrint getLatestTimeEntry(String fingerprintId, Date checkTime, Boolean isNull);

    EdsUsersFingerPrint getLatestTimeEntry(String fingerprintId, Date checkTime, String deviceUUID);

    void deleteEqualsStartDate(Integer employeeID, Date startDate);

    void deleteByShiftId(Integer shiftId);

    void deleteRecordBetweenDates(Integer employeeID, Date startDate, Date endDate);

    DateNonConvertable getFingerPrint(ListingFilterParameter fp);

    List<StaffInOut> getEmployeeTimeTrackInPeriod(Integer employeeID, Date startDate, Date endDate, boolean fingerprintIsCustom);

    List<InOutReportTO> getEmployeeAttendanceReport(Integer employeeID, Date startDate, Date endDate);

    Long countAttendanceDays(Integer employeeId, Date startDate, Date endDate);

    void insertTimeTrack(Integer objectID, Integer employeeID, EdsUserFingerPrintDevice userFingerPrintDevice, Date startTime, Date endTime, Integer statusID, boolean isCustomFingerPrint);

    void insertTimeTrack(Integer objectID, Integer employeeID, EdsUserFingerPrintDevice userFingerPrintDevice, Date startTime, Date endTime, Integer statusID, boolean isCustomFingerPrint, Integer shiftId, Integer timeSlotId, boolean isOvertime);

    List<GymInOutTO> getUserGymInOutRecords(Integer userId);

    List<InOutPairDto> getUserInOutPairs(Integer userId, Date date);

    Integer[] getLateAndEarlyCount( Date startDate,Date endDate,Integer userId);

    Map<Integer, Integer[]> getLateAndEarlyCountBatch(Date startDate, Date endDate, Set<Integer> employeeIds);

    Map<Integer, Integer> getUserDailySummaryCountBatch(Date fromDate, Date toDate, Set<Integer> employeeIds);

    List<InOutPairDto> getUserInOutPairs(Integer userId, Date fromDate, Date toDate);

    List<InOutPairDto> getUserInOutPairsForApi(Integer userId, Date fromDate, Date toDate);

    List<InOutPairDto> getUserDailySummary(Integer userId, Date fromDate, Date toDate);

    ListResult<AttendanceMarkListItem> getAttendanceMarkList(ListingFilterParameter fp);

    List<AttendanceMarkListItem> getAttendanceMarkListForExport(ListingFilterParameter fp);

    void approveAttendanceMarks(List<Integer> fingerprintIds);
}
