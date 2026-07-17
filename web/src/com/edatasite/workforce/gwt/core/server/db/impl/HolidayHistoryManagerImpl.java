package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsHolidayHistory;
import com.edatasite.workforce.gwt.core.server.db.HolidayHistoryManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: ASUS
 * Date: 26.02.2016 12:08
 */
@Repository("holidayHistoryManager")
public class HolidayHistoryManagerImpl extends BaseManager<EdsHolidayHistory> implements HolidayHistoryManager {
    public HolidayHistoryManagerImpl(){
        super(EdsHolidayHistory.class);
    }
    @Override
    public List<EdsHolidayHistory> historyList(Integer holidayId) {
        return find("select holidayHistr from EdsHolidayHistory holidayHistr where holidayHistr.holiday.objectID = ? order by holidayHistr.creationTime desc", holidayId);
    }
}
