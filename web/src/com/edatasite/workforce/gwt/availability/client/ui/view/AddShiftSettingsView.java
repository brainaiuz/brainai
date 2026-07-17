package com.edatasite.workforce.gwt.availability.client.ui.view;

import com.edatasite.workforce.gwt.availability.client.localization.AvailabilityMessages;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceLocale;
import com.edatasite.workforce.gwt.core.client.rpc.ShiftSettingsItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.savepanel.ColorWidget;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.AddEditLocaleView;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;


public class AddShiftSettingsView extends CustomForm implements Constants, Colapse, BlurHandler, KeyUpHandler {

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private static final AvailabilityMessages availabilityMessages = AvailabilityMessages.App.get();

    private TextArea2 description;
    private final Integer objectId;
    private TextBox name;
    private AddEditLocaleView localeView;
    private ReferenceLocale localeItem;
    private TextBox shortName;
    private TextBox interval;
    private ColorWidget colorWidget;
    private ShiftSettingsItem shiftItem;
    private TextBox[] times;

    private final FlexTable ft = new FlexTable();
    private final FlexTable includedDays = new FlexTable();


    private final String test_code_ID_name;

    public AddShiftSettingsView(Integer objectId) {
        super("edit", hrmsStrings.editShiftSettings());
        this.objectId = objectId;
        this.test_code_ID_name = "edit_shift_view_";
    }

    public String getIconStyle() {
        return "availability add-shift";
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.save(), BTN_PRIMARY, null, (test_code_ID_name + "add_shift_button"), clickEvent -> save());
    }

    private void drawShift(FlexTable table, TextBox[] week) {
        table.clear();
        table.setHTML(2, 0, "<b class=customTitle>" + hrmsStrings.shiftSettings() + ":</b>");
        table.setHTML(3, 0, "<b class=customTitle>" + wfmStrings.interval() + ":</b>");
        table.setHTML(4, 0, "");
        table.setHTML(5, 0, "");
        table.setHTML(6, 0, "");
        table.setHTML(7, 0, "");
        table.setHTML(8, 0, "");
        for (int i = 0; i < 6; i++) {
            table.setWidget(2, i + 1, week[i]);
            week[i].addKeyUpHandler(this);
        }
        week[0].setText("09:30");
        week[1].setText("18:00");
        week[2].setText("00:00");
        week[3].setText("00:00");
        week[4].setText("00:00");
        week[5].setText("00:00");

        table.setWidget(3, 1, interval);
        setTableFeatures(ft);
    }


    private void drawIncludedDays(FlexTable table) {
        table.clear();

        table.setHTML(1, 0, "<b class=customTitle>"+wfmStrings.monday()+"</b>");
        CheckBox mondayBox = new CheckBox();
        table.setWidget(1,1,mondayBox);

        table.setWidget(2,0,new HTML("<b class=customTitle>"+wfmStrings.tuesday()+"</b>"));
        CheckBox tuesdayBox = new CheckBox();
        table.setWidget(2,1,tuesdayBox);


        table.setWidget(3,0,new HTML("<b class=customTitle>"+wfmStrings.wednesday()+"</b>"));
        CheckBox wednesdayBox = new CheckBox();
        table.setWidget(3,1,wednesdayBox);

        table.setWidget(4,0,new HTML("<b class=customTitle>"+wfmStrings.thursday()+"</b>"));
        CheckBox thursdayBox = new CheckBox();
        table.setWidget(4,1,thursdayBox);

        table.setWidget(5,0,new HTML("<b class=customTitle>"+wfmStrings.friday()+"</b>"));
        CheckBox fridayBox = new CheckBox();
        table.setWidget(5,1,fridayBox);

        table.setWidget(6,0,new HTML("<b class=customTitle>"+wfmStrings.saturday()+"</b>"));
        CheckBox saturdayBox = new CheckBox();
        table.setWidget(6,1,saturdayBox);

        table.setWidget(7,0,new HTML("<b class=customTitle>"+wfmStrings.sunday()+"</b>"));
        CheckBox sundayBox = new CheckBox();
        table.setWidget(7,1,sundayBox);


        setIncludedDaysHeader(includedDays);
    }


    @Override
    protected void getDataToFillFields() {
        if (objectId != null) {
            LoadingPanel.loading(true);
            AvailabilityService.App.get().getShiftSettings(objectId, new AbstractAsyncCallback<ShiftSettingsItem>() {
                @Override
                public void success(ShiftSettingsItem item) {
                    LoadingPanel.loading(false);
                    shiftItem = item;
                    localeItem = item.getReferenceLocale() != null ? item.getReferenceLocale() : new ReferenceLocale();
                    fillFormWithData(item);
                }
            });
        }
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.SHIFT_SETTINGS_FORM;
    }

    @Override
    protected String getFormType() {
        return objectId == null ? LayoutRPC.ADD : LayoutRPC.EDIT;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        initialize();
        return null;
    }

    private void setWeekTime(TextBox[] week, ShiftSettingsItem item) {
        week[0].setText(getShift(item.getTimes()[0] / 60) + ":" + getShift(item.getTimes()[0] % 60));
        week[1].setText(getShift(item.getTimes()[1] / 60) + ":" + getShift(item.getTimes()[1] % 60));
        week[2].setText(getShift(item.getLunchTimes()[0] / 60) + ":" + getShift(item.getLunchTimes()[0] % 60));
        week[3].setText(getShift(item.getLunchTimes()[1] / 60) + ":" + getShift(item.getLunchTimes()[1] % 60));
        week[4].setText(getShift(item.getCoffeeTimes()[0] / 60) + ":" + getShift(item.getCoffeeTimes()[0] % 60));
        week[5].setText(getShift(item.getCoffeeTimes()[1] / 60) + ":" + getShift(item.getCoffeeTimes()[1] % 60));
    }

    private void setExcludedDays(ShiftSettingsItem item) {
        if (item.getExcludedDays() == null || item.getExcludedDays().isEmpty()) {
            return;
        }
        String[] split = item.getExcludedDays().split(",");
        for (String s : split) {
            CheckBox dayBox = new CheckBox();
            dayBox.setValue(true);
            includedDays.setWidget(Integer.parseInt(s) + 1, 1, dayBox);
        }
    }

    private void fillFormWithData(ShiftSettingsItem item) {
        //shift name
        if (item.getName() != null) {
            name.setText(item.getName());
        }
        //description
        if (item.getDescription() != null) {
            description.setText(item.getDescription());
        }
        interval.setText(item.getInterval());
        setWeekTime(times, item);
        shortName.setText(item.getShortName());
        setExcludedDays(item);
        if (item.getHexColor() != null && item.getHexColor().

                length() > 0)
            colorWidget.setColor(item.getHexColor());
    }

    private void initialize() {
        name = new TextBox();
        name.setName("name");
        name.addStyleName(DEFAULT_WIDTH);
        name.ensureDebugId(test_code_ID_name + "name");
        localeItem = new ReferenceLocale();

        WfmButton2 locale = new WfmButton2(wfmStrings.vacancyLocale());
        locale.setStyleName("font-style: italic;", true);
        locale.addClickHandler(event -> {
            if (localeView == null) {
                localeView = new AddEditLocaleView(name.getText(), localeItem);
            } else {
                localeView.setLocaleItem(localeItem);
                localeView.setNameValue(name.getText());
                localeView.showView();
            }
        });
        FlexTable localedNameBox = new FlexTable();
        localedNameBox.setWidget(0, 0, name);
        localedNameBox.getColumnFormatter().setWidth(0, "85%");
        localedNameBox.setWidget(0, 1, locale);
        localedNameBox.getColumnFormatter().setWidth(1, "15%");

        shortName = new TextBox();
        shortName.setName("name");
        shortName.setMaxLength(3);
        shortName.addStyleName(DEFAULT_WIDTH);
        shortName.ensureDebugId(test_code_ID_name + "shortName");

        interval = new TextBox();
        interval.setWidth("80px");
        interval.addKeyUpHandler(event1 -> {
            Validation.numberValidation(interval);
            Validation.addNumericKeyboardListener(interval);
        });

        colorWidget = new ColorWidget();
        colorWidget.setWidth("295px");

        description = new TextArea2(wfmStrings.description());
        description.addStyleName(MAX_DEFAULT_WIDTH);
        description.ensureDebugId(test_code_ID_name + "description");

        times = new TextBox[6];
        for (int i = 0; i < 6; i++) {
            times[i] = new TextBox();
            times[i].setWidth("80px");
        }
        drawShift(ft, times);
        drawIncludedDays(includedDays);

        addTitleField(CustomFormConstants.DETAILS, hrmsStrings.timeSlotDetails());
        addField(CustomFormConstants.NAME, localedNameBox, getTitle(wfmStrings.name(), true));
        addField(CustomFormConstants.DESCRIPTION, description, null);

        addField(CustomFormConstants.SHORT_NAME, shortName, getTitle(wfmStrings.shortName()));
        addField(COLOR_PICKER, colorWidget, getTitle(wfmStrings.color()));

        addField(CustomFormConstants.TIME_ENTRY_FOR_EACH, new HTML(wfmStrings.shift()), null);
        addField(CustomFormConstants.TIME_ENTRY, ft, null);


        addTitleField("INCLUDED_DAYS_TITLE","Included Days");
        addField("INCLUDED_DAYS_LABEL", new HTML(wfmStrings.days()), null);
        addField("INCLUDED_DAYS", includedDays, null);


        show();
    }

    private void setTableFeatures(FlexTable table) {
        table.setCellPadding(15);
        table.setCellSpacing(15);
        //title 1
        table.setHTML(0, 0, "");
        table.setWidget(0, 1, new HTML("<b class=customTitle align=right style=\"margin-left: 3px\">" + wfmStrings.contactwork() + " " + wfmStrings.time().toLowerCase() + "</b>"));
        table.setWidget(0, 3, new HTML("<b class=customTitle align=right style=\"margin-left: 3px\">" + hrmsStrings.lunch() + " " + wfmStrings.time().toLowerCase() + "</b>"));
        table.setWidget(0, 5, new HTML("<b class=customTitle align=right style=\"white-space: nowrap; margin-left: 3px\">" + hrmsStrings.coffee() + " " + hrmsStrings.getPropertyBreak().toLowerCase() + "</b>"));
        table.setHTML(0, 7, "");
        //title 2
        table.setHTML(1, 0, "");
        table.setWidget(1, 1, new HTML("<div style=\"margin-left: 3px\">" + wfmStrings.start() + "</div>"));
        table.setWidget(1, 2, new HTML("<div style=\"margin-left: 3px\">" + wfmStrings.end() + "</div>"));
        table.setWidget(1, 3, new HTML("<div style=\"margin-left: 3px\">" + wfmStrings.start() + "</div>"));
        table.setWidget(1, 4, new HTML("<div style=\"margin-left: 3px\">" + wfmStrings.end() + "</div>"));
        table.setWidget(1, 5, new HTML("<div style=\"margin-left: 3px\">" + wfmStrings.start() + "</div>"));
        table.setWidget(1, 6, new HTML("<div style=\"margin-left: 3px\">" + wfmStrings.end() + "</div>"));

        //cell formatter to table
        HTMLTable.CellFormatter cellFormatter = table.getCellFormatter();
        cellFormatter.setWidth(0, 0, "8%");
        cellFormatter.setWidth(0, 1, "2%");
        cellFormatter.setWidth(0, 2, "10%");
        cellFormatter.setWidth(0, 3, "2%");
        cellFormatter.setWidth(0, 4, "10%");
        cellFormatter.setWidth(0, 5, "2%");
        cellFormatter.setWidth(0, 6, "15%");
        cellFormatter.setWidth(0, 7, "2%");
        cellFormatter.setWidth(0, 8, "40%");
        cellFormatter.setHorizontalAlignment(2, 8, HasHorizontalAlignment.ALIGN_LEFT);
    }

    private void setIncludedDaysHeader(FlexTable table) {
        table.setCellPadding(15);
        table.setCellSpacing(15);
        table.setHTML(0, 0, "");
        table.setWidget(0, 1, new HTML("<b class=customTitle align=right>" +"Included Days" +"</b>"));

        HTMLTable.CellFormatter cellFormatter = table.getCellFormatter();
        cellFormatter.setWidth(0, 0, "5%");
        cellFormatter.setWidth(0, 1, "10%");

        cellFormatter.setHorizontalAlignment(2, 8, HasHorizontalAlignment.ALIGN_LEFT);

    }

    private String getShift(int time) {
        if (String.valueOf(time).length() == 1) {
            return "0" + time;
        }
        if (String.valueOf(time).length() == 0) {
            return "00";
        }
        return String.valueOf(time);
    }

    private void save() {
        enableButton(false);
        if (!Validation.validateTextBoxRequired(name)) {
            enableButton(true);
            return;
        }
        setValues();
        LoadingPanel.loading(true);
        AvailabilityService.App.get().saveShiftSettings(shiftItem, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                enableButton(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Void object) {
                LoadingPanel.loading(false);
                enableButton(true);
                Info.show(property != null ? property.getPlural(wfmStrings.messSuccessfullyAdded(), wfmStrings.timeslot()) : wfmStrings.messSuccessfullyAdded(), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(objectId == null ? WfmUiEventType.ON_SHIFT_SETTINGS_ADD : WfmUiEventType.ON_SHIFT_SETTINGS_EDIT, object, AddShiftSettingsView.this);
                closeTab();
            }
        });
    }

    private void setValues() {
        shiftItem = new ShiftSettingsItem();
        if (objectId != null) {
            shiftItem.setId(objectId);
        }
        shiftItem.setName(name.getText());
        shiftItem.setShortName(shortName.getText());
        shiftItem.setDescription(description.getText());
        shiftItem.setInterval(interval.getValue());

        localeItem = localeView != null ? localeView.getLocaleItem() : new ReferenceLocale();
        shiftItem.setReferenceLocale(localeItem);
        shiftItem.setHexColor(colorWidget.getColor());

        shiftItem.setTimes(new int[]{convertToInteger(times[0].getText()), convertToInteger(times[1].getText())});
        shiftItem.setLunchTimes(new int[]{convertToInteger(times[2].getText()), convertToInteger(times[3].getText())});
        shiftItem.setCoffeeTimes(new int[]{convertToInteger(times[4].getText()), convertToInteger(times[5].getText())});

        CheckBox monday = (CheckBox) includedDays.getWidget(1, 1);
        CheckBox tuesday = (CheckBox) includedDays.getWidget(2, 1);
        CheckBox wensday = (CheckBox) includedDays.getWidget(3, 1);
        CheckBox thursday = (CheckBox) includedDays.getWidget(4, 1);
        CheckBox friday = (CheckBox) includedDays.getWidget(5, 1);
        CheckBox saturday = (CheckBox) includedDays.getWidget(6, 1);
        CheckBox sunday = (CheckBox) includedDays.getWidget(7, 1);

        String includedDay = "";
        if (monday.getValue()) {
            includedDay += ","+ WEEK_DAYS.MONDAY;
        }
        if (tuesday.getValue()) {
            includedDay += ","+ WEEK_DAYS.TUESDAY;
        }
        if (wensday.getValue()) {
            includedDay += ","+ WEEK_DAYS.WEDNESDAY;
        }
        if (thursday.getValue()) {
            includedDay += ","+ WEEK_DAYS.THURSDAY;
        }
        if (friday.getValue()) {
            includedDay += ","+ WEEK_DAYS.FRIDAY;
        }
        if (saturday.getValue()) {
            includedDay += ","+ WEEK_DAYS.SATURDAY;
        }
        if (sunday.getValue()) {
            includedDay += ","+ WEEK_DAYS.SUNDAY;
        }
        if (includedDay.startsWith(",")) {
            includedDay = includedDay.substring(1);
        }
        shiftItem.setExcludedDays(includedDay);
    }

    private int convertToInteger(String s) {
        String[] time = s.split(":");
        int hours = 0;
        int minutes = 0;
        try {
            hours = Integer.parseInt(time[0]);
            minutes = Integer.parseInt(time[1]);
        } catch (NumberFormatException e) {

            e.printStackTrace();
        }
        return (hours * 60 + minutes);
    }

    @Override
    public void onBlur(BlurEvent event) {
        Object sender = event.getSource();
        /**/
        if (((TextBox) sender).getText().length() == 1) {
            ((TextBox) sender).setText("0" + ((TextBox) sender).getText() + ":00");
        }
        if (((TextBox) sender).getText().length() == 3) {
            ((TextBox) sender).setText(((TextBox) sender).getText() + "00");
        }
        if (((TextBox) sender).getText().length() == 4) {
            ((TextBox) sender).setText(((TextBox) sender).getText() + "0");
        }
        if (((TextBox) sender).getText().length() > 5) {
            ((TextBox) sender).setText(((TextBox) sender).getText().substring(0, 5));
        }
        String[] times = ((TextBox) sender).getText().split(":");
        try {
            int hours = Integer.parseInt(times[0]);
            int minutes = Integer.parseInt(times[1]);
            if (hours > 23) {
                Info.show(hrmsStrings.hoursExeedsLimit(), Info.Type.WARNING);
                ((TextBox) sender).setText("0");
                ((TextBox) sender).setFocus(true);
                return;
            }
            if (minutes > 59) {
                Info.show(hrmsStrings.minutesExeedsLimit(), Info.Type.WARNING);
                ((TextBox) sender).setText("0");
                ((TextBox) sender).setFocus(true);
            }
        } catch (NumberFormatException e) {

            if (!("".equals(((TextBox) sender).getText()))) {
                Info.show(availabilityMessages.timeNotValid(((TextBox) sender).getText()), Info.Type.WARNING);
                ((TextBox) sender).setText("00:00");
                ((TextBox) sender).setFocus(true);
            } else {
                ((TextBox) sender).setText("00:00");
            }
        }
    }

    @Override
    public void onKeyUp(KeyUpEvent event) {
        Object sender = event.getSource();
        if (((TextBox) sender).getText().length() == 2) {
            ((TextBox) sender).setText(((TextBox) sender).getText() + ":");
        }
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }
}