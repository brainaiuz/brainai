package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsSelectedEmployeeFromCalendar;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.server.db.SelectedEmployeeFromCalendarManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by KHasan on 12.11.15.
 */
@Repository("selectedEmployeeFromCalendarManager")
public class SelectedEmployeeFromCalendarManagerImpl extends BaseManager<EdsSelectedEmployeeFromCalendar> implements SelectedEmployeeFromCalendarManager {

    public SelectedEmployeeFromCalendarManagerImpl() {
        super(EdsSelectedEmployeeFromCalendar.class);
    }

    @Override
    public List<EdsSelectedEmployeeFromCalendar> getByUser(EdsUser user) {
        return find("select se from EdsSelectedEmployeeFromCalendar se where se.user=?", user);
    }

    @Override
    public void deletedOldSelected(EdsUser user) {
        update("delete from EdsSelectedEmployeeFromCalendar se where se.user.objectID=?", user.getObjectID());
    }
}
