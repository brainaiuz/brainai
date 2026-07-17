package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsTimeSlotHistory;
import com.edatasite.workforce.gwt.core.server.db.TimeSlotHistoryManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: ASUS
 * Date: 22.02.2016 18:47
 */
@Repository("timeSlotHistoryManager")
public class TimeSlotHistoryManagerImpl extends BaseManager<EdsTimeSlotHistory> implements TimeSlotHistoryManager {

    public TimeSlotHistoryManagerImpl() {
        super(EdsTimeSlotHistory.class);
    }

    @Override
    public List<EdsTimeSlotHistory> historyList(Integer timeSlotID, boolean isShift) {
        return find("select timeSlotHistr from EdsTimeSlotHistory timeSlotHistr where timeSlotHistr." + (isShift ? "shiftSettings" : "timeSlot") + ".objectID = ? order by timeSlotHistr.creationTime desc", timeSlotID);
    }
}
