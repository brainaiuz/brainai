package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsTaskReminder;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 05.10.11
 * Time: 10:00
 * To change this template use File | Settings | File Templates.
 */

public interface TaskReminderManager extends Manager<EdsTaskReminder> {

    ArrayList<CalendarEventReminder> getReminders(Integer taskID);
    void deleteTaskReminders(Integer taskID);
}
