package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsTaskReminder;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder;
import com.edatasite.workforce.gwt.core.server.db.TaskReminderManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 05.10.11
 * Time: 10:01
 * To change this template use File | Settings | File Templates.
 */

@Repository("taskReminderManager")
public class TaskReminderManagerImpl extends BaseManager<EdsTaskReminder> implements TaskReminderManager {

    public TaskReminderManagerImpl() {
        super(EdsTaskReminder.class);
    }

    public ArrayList<CalendarEventReminder> getReminders(Integer taskID) {
        return (ArrayList<CalendarEventReminder>) find("select new com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder(rem.reminderType, rem.minutes) from EdsTaskReminder rem where rem.task.objectID=?", taskID);
    }

    public void deleteTaskReminders(Integer taskID) {
        update("delete from EdsTaskReminder rem where rem.task.objectID = ?", taskID);
    }
}
