package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsTimeSlot;
import com.edatasite.workforce.core.domain.EdsTimeSlotItem;
import com.edatasite.workforce.gwt.core.server.db.TimeSlotItemManager;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("timeSlotItemManager")
public class TimeSlotItemManagerImpl extends BaseManager<EdsTimeSlotItem> implements TimeSlotItemManager {

    public TimeSlotItemManagerImpl() {
        super(EdsTimeSlotItem.class);
    }

    public List<EdsTimeSlotItem> getTimeSlotItems(EdsTimeSlot timeSlot) {
        return find(
                "select tsi from EdsTimeSlotItem tsi where tsi.timeSlot =? and tsi.exceptionalDate is null order by tsi.day asc", timeSlot);
    }

    public EdsTimeSlotItem getTimeSlotItemByEmployeeIDWithSelectedDay(Integer employeeID, Integer selectedDayINT, Integer companyDefaultTimeSlotID) {
        return (EdsTimeSlotItem) findSingle("SELECT tsi FROM EdsTimeSlotItem tsi, EdsEmployee e " +
                "WHERE e.objectID=? AND tsi.day =? AND tsi.exceptionalDate is null AND " +
                "((e.timeSlot is not null AND e.timeSlot.objectID = tsi.timeSlot.objectID) OR " +
                "(e.timeSlot is null AND  tsi.timeSlot.objectID = ?)) " +
                "ORDER BY tsi.day ASC", employeeID, selectedDayINT, companyDefaultTimeSlotID);
    }

    public void deleteExceptionalCaseTimeSlotItems(Integer timeSlotID) {
        if (timeSlotID != null) {
            StringBuilder sql = new StringBuilder();
            sql.append("DELETE FROM ").append(getCompanyId()).append(".timeslotitem tsi \n");
            sql.append("WHERE tsi.exceptionalDate is not null ");
            sql.append("AND tsi.timeslotid = ").append(timeSlotID);
            updateNative(sql.toString());
        }
    }

    @Override
    public Integer getStartMinutesByDay(int day, Integer timeSlotID) {
        return (Integer) findSingle("SELECT ets.startTime FROM EdsTimeSlotItem ets WHERE ets.timeSlot.objectID=? AND ets.day=?", timeSlotID, day);
    }

    @Override
    public EdsTimeSlotItem getTimeSlotItemByDay(int day, Integer timeSlotID) {
        return (EdsTimeSlotItem) findSingle("select tsi from EdsTimeSlotItem tsi join tsi.timeSlot ts where ts.objectID = ? and tsi.day = ?", timeSlotID, day - 1);
    }
}