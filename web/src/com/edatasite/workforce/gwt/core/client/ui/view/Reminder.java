package com.edatasite.workforce.gwt.core.client.ui.view;

import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableNewUI;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class Reminder extends MultiTableNewUI implements Constants {
    public static final String REMINDER_TYPE = "REMINDER_TYPE";
    public static final String REMINDER_TIME = "REMINDER_TIME";
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    boolean hasGoogleAccount = false;
    String width = "";

    public Reminder(boolean hasGoogleAccount) {
        this(hasGoogleAccount, "");
    }

    public Reminder(boolean hasGoogleAccount, String width) {
        super(new MultiTableWidgets() {
                  @Override
                  public WidgetsMap getWidgetsMaps() {
                      return getWidgetsMap(null, width, hasGoogleAccount);
                  }

                  @Override
                  public boolean isFilled() {
                      return false;
                  }
              },
                false);
        this.hasGoogleAccount = hasGoogleAccount;
        this.width = width;
        getElement().setId("debug_reminder");
    }

    public static Map<Integer, String> getReminderTimesMap() {
        HashMap<Integer, String> map = new HashMap<>();
        int k = 5;
        for (int i = 0; i < 10; i++) {
            map.put(k, " " + k + " " + wfmStrings.minutes());
            k += 5;
        }
        map.put(60, " " + wfmStrings.oneHour());
        map.put(60 * 2, " " + wfmStrings.twoHours());
        map.put(60 * 3, " " + wfmStrings.threeHours());
        map.put(60 * 12, " " + wfmStrings.twelveHours());
        map.put(60 * 24, " " + wfmStrings.oneDay());
        map.put(60 * 24 * 2, " " + wfmStrings.twoDays());
        map.put(60 * 24 * 7, " " + wfmStrings.oneWeek());
        map.put(60 * 24 * 15, " " + wfmStrings.fifteenDays());
        map.put(60 * 24 * 30, " " + wfmStrings.oneMonth());
        map.put(60 * 24 * 45, " " + wfmStrings.fortyFiveDays());
        map.put(60 * 24 * 60, " " + wfmStrings.twoMonths());
        return map;
    }

    public static Map<Integer, String> getReminderTypesMap() {
        HashMap<Integer, String> map = new HashMap<>();
        map.put(E_MAIL, wfmStrings.email());
        return map;
    }

    public static SelectItem[] getMapAsSelectItem(Map<Integer, String> map, boolean hasGoogleAccount) {
        ArrayList<SelectItem> result = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : map.entrySet()) {
            SelectItem selectItem = new SelectItem(entry.getKey(), entry.getValue());
            if (!hasGoogleAccount) {
                if (map.containsKey(55)) {
                    selectItem.setSelected(true);
                }
            }
            if (entry.getKey() == 60 * 24 * 30) {
                selectItem.setDescription("monthly");
            }
            result.add(selectItem);
        }
        return result.toArray(new SelectItem[]{});
    }

    private static WidgetsMap getWidgetsMap(CalendarEventReminder eventReminder, String width, boolean hasGoogleAccount) {
        WidgetsMap widgetsMap = new WidgetsMap();
        DataListBox reminderType = new DataListBox();
        if (width != null) {
            reminderType.setWidth(width);
        } else {
            reminderType.setWidth("150px");
        }
        reminderType.setItems(getMapAsSelectItem(getReminderTypesMap(), false));
        DataListBox reminderTime = new DataListBox();
        if (width != null) {
            reminderTime.setWidth(width);
        }
//        reminderTime.getElement().getStyle().setMarginLeft(2d, Style.Unit.PX);
        reminderTime.setItems(getMapAsSelectItem(getReminderTimesMap(), hasGoogleAccount));
        widgetsMap.add(REMINDER_TYPE, reminderType);
        widgetsMap.add(REMINDER_TIME, reminderTime);
        if (eventReminder != null && eventReminder.getValue() != null) {
            reminderType.setSelected(eventReminder.getValue());
        }
        if (eventReminder != null && eventReminder.getReminderTimes() != null) {
            reminderTime.setSelected(eventReminder.getReminderTimes());
        }
        return widgetsMap;
    }

    public ArrayList<CalendarEventReminder> getReminderDatas() {
        ArrayList<CalendarEventReminder> list = new ArrayList<>();
        for (Map<String, Widget> widgets : getWidgets()) {
            DataListBox reminderType = (DataListBox) widgets.get(REMINDER_TYPE);
            DataListBox reminderTime = (DataListBox) widgets.get(REMINDER_TIME);
            CalendarEventReminder cer = new CalendarEventReminder();
            if (reminderType.isSomethingSelected()) {
                cer.setValue(reminderType.getSelectedItem().getId());
            }
            if (reminderTime.isSomethingSelected()) {
                cer.setReminderTimes(reminderTime.getSelectedId());
            }
            if (reminderType.isSomethingSelected() || reminderTime.isSomethingSelected()) {
                list.add(cer);
            }
        }

        return list;
    }

    public void setReminderDatas(List<CalendarEventReminder> reminders) {
        clear();
        if (reminders != null && reminders.size() > 0) {
            for (CalendarEventReminder eventReminder : reminders) {
                addWidgets(getWidgetsMap(eventReminder, width, hasGoogleAccount));
            }
        } else {
            addWidgets(getWidgetsMap(null, width, hasGoogleAccount));
        }
    }

    public boolean validateDueReminder() {
        int errors = 0;

        for (Map<String, Widget> widgets : getWidgets()) {
            DataListBox reminderType = (DataListBox) widgets.get(REMINDER_TYPE);
            DataListBox reminderTime = (DataListBox) widgets.get(REMINDER_TIME);
            reminderType.removeStyleName("x-form-invalid");
            reminderTime.removeStyleName("x-form-invalid");

            if (reminderType.getSelectedItem() != null && reminderTime.getSelectedItem() == null) {
                reminderTime.addStyleName("x-form-invalid");
                errors++;
            }
            if (reminderType.getSelectedItem() == null && reminderTime.getSelectedItem() != null) {
                reminderType.addStyleName("x-form-invalid");
                errors++;
            }
            if (reminderType.getSelectedItem() == null && reminderTime.getSelectedItem() == null) {
                reminderTime.addStyleName("x-form-invalid");
                errors++;
            }
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }
}
