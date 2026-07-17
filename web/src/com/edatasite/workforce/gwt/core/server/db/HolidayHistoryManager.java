package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsHolidayHistory;

import java.util.List;

/**
 * User: ASUS
 * Date: 26.02.2016 12:05
 */
public interface HolidayHistoryManager extends Manager<EdsHolidayHistory> {
    List<EdsHolidayHistory> historyList(Integer holidayId);
}
