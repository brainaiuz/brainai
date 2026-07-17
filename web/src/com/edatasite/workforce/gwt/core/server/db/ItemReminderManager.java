package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsItemReminder;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Fatxulla
 * Date: 05.10.11
 * Time: 10:00
 * To change this template use File | Settings | File Templates.
 */

public interface ItemReminderManager extends Manager<EdsItemReminder> {

    ArrayList<CalendarEventReminder> getReminders(Integer itemID, Integer itemType);
    void deleteItemReminders(Integer itemID, Integer itemType);
}
