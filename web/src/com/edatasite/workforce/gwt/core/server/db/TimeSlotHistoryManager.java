package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsTimeSlotHistory;

import java.util.List;

/**
 * User: ASUS
 * Date: 22.02.2016 18:42
 */
public interface TimeSlotHistoryManager extends Manager<EdsTimeSlotHistory> {
    List<EdsTimeSlotHistory> historyList(Integer timeSlotID, boolean isShift);
}
