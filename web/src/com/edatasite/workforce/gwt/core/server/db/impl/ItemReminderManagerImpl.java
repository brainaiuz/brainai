package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsItemReminder;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder;
import com.edatasite.workforce.gwt.core.server.db.ItemReminderManager;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Fatxulla
 * Date: 05.10.11
 * Time: 10:01
 * To change this template use File | Settings | File Templates.
 */

@Repository("itemReminderManager")
public class ItemReminderManagerImpl extends BaseManager<EdsItemReminder> implements ItemReminderManager {

    public ItemReminderManagerImpl() {
        super(EdsItemReminder.class);
    }

    public ArrayList<CalendarEventReminder> getReminders(Integer itemID, Integer itemType) {
        return (ArrayList<CalendarEventReminder>) find("select new com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder(rem.reminderType, rem.minutes) from EdsItemReminder rem where rem.item=? and rem.itemType=?", itemID, itemType);
    }

    public void deleteItemReminders(Integer itemID, Integer itemType) {
        update("delete from EdsItemReminder rem where rem.item = ? and  rem.itemType = ?", itemID, itemType);
    }
}
