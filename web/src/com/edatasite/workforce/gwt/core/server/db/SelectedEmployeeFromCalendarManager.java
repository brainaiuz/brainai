package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsSelectedEmployeeFromCalendar;
import com.edatasite.workforce.core.domain.EdsUser;

import java.util.List;

/**
 * Created by KHasan on 12.11.15.
 */
public interface SelectedEmployeeFromCalendarManager extends Manager<EdsSelectedEmployeeFromCalendar> {

    List<EdsSelectedEmployeeFromCalendar> getByUser(EdsUser user);

    void deletedOldSelected(EdsUser user);
}
