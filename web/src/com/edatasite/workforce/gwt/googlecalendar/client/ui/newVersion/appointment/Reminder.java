package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment;

import com.edatasite.workforce.gwt.core.client.WordUtils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;

public class Reminder extends Composite implements Constants {
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final DataListBox reminderTime;
    private final DataListBox reminderType;

    public Reminder() {

        reminderTime = new DataListBox();
        reminderTime.setIdAttribute("reminder-time");
        int k = 5;
        for (int i = 0; i < 10; i++) {
            reminderTime.addListItem(new SelectItem(k, " " + k + " " + wfmStrings.minutes()));
            k += 5;
        }
        reminderTime.addListItem(new SelectItem(60, " " + wfmStrings.oneHour()));
        reminderTime.addListItem(new SelectItem(60 * 2, " " + wfmStrings.twoHours()));
        reminderTime.addListItem(new SelectItem(60 * 3, " " + wfmStrings.threeHours()));
        reminderTime.addListItem(new SelectItem(60 * 12, " " + wfmStrings.twelveHours()));
        reminderTime.addListItem(new SelectItem(60 * 24, " " + wfmStrings.oneDay()));
        reminderTime.addListItem(new SelectItem(60 * 24 * 2, " " + wfmStrings.twoDays()));
        reminderTime.addListItem(new SelectItem(60 * 24 * 7, " " + wfmStrings.oneWeek()));
        reminderTime.setSelected(new SelectItem(30, " " + 30 + " " + wfmStrings.minutes()));

        reminderType = new DataListBox();
        reminderType.addListItem(new SelectItem(PUSH_NOTIFICATION, wfmStrings.pushNotification()));
        reminderType.addListItem(new SelectItem(E_MAIL, wfmStrings.email()));
        reminderType.addListItem(new SelectItem(SMS, wfmStrings.sms()));
        reminderType.setSelected(PUSH_NOTIFICATION);
        reminderType.setIdAttribute("reminder-type");

        InputGroup timeWrapper = new InputGroup();
        Div appendDiv = new Div("input-group-append");
        Label prependedText = new Label(WordUtils.uncapitalize(wfmStrings.before()));
        prependedText.addStyleName("input-group-text");
        appendDiv.add(prependedText);

        timeWrapper.add(reminderTime);
        timeWrapper.add(appendDiv);

        InputGroup typeWrapper = new InputGroup();
        Div prependDiv = new Div("input-group-prepend");
        Label labelBy = new Label(wfmStrings.by());
        labelBy.addStyleName("input-group-text");
        prependDiv.add(labelBy);
        typeWrapper.add(prependDiv);
        typeWrapper.add(reminderType);

        MaterialPanel panel = new MaterialPanel();
        panel.addStyleName("input-group");
        panel.add(timeWrapper);
        panel.add(typeWrapper);

        initWidget(panel);
    }

    public CalendarEventReminder getReminderData() {
        CalendarEventReminder cer = new CalendarEventReminder();
        if (reminderType.isSomethingSelected()) {
            cer.setValue(reminderType.getSelectedItem().getId());
        }
        if (reminderTime.isSomethingSelected()) {
            cer.setReminderTimes(reminderTime.getValue(reminderTime.getSelectedIndex()).getId());
        }
        return cer;
    }

    public void setReminderData(CalendarEventReminder reminder) {
        reminderType.setSelected(reminder.getValue());
        reminderTime.setSelected(reminder.getReminderTimes());
    }

    public void setEnabled(boolean enabled) {
        reminderType.setEnabled(enabled);
        reminderTime.setEnabled(enabled);
    }
}
