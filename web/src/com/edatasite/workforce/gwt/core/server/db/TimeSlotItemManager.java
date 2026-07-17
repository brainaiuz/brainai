package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsTimeSlot;
import com.edatasite.workforce.core.domain.EdsTimeSlotItem;

import java.util.List;

public interface TimeSlotItemManager extends Manager<EdsTimeSlotItem> {

    List<EdsTimeSlotItem> getTimeSlotItems(EdsTimeSlot timeSlot);

    EdsTimeSlotItem getTimeSlotItemByEmployeeIDWithSelectedDay(Integer employeeID, Integer selectedDayINT, Integer companyDefaultTimeSlotID);

    void deleteExceptionalCaseTimeSlotItems(Integer timeSlotID);

    Integer getStartMinutesByDay(int day, Integer timeSlotID);

    EdsTimeSlotItem getTimeSlotItemByDay(int day, Integer timeSlotID);
}