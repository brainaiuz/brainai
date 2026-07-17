package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.google.gwt.gen2.table.override.client.FlexTable;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.ListBox;

import java.util.Date;

/**
 * User: abdullo_
 * Date: Aug 17, 2010
 * Time: 12:10:10 PM
 */
public class DateTimePicker extends Composite {
    //    private final NumberFormat numberFormat = NumberFormat.getFormat("00");
    final DateTimeFormat timeFormat = DateUtils.getTimeFormatInternal()/*getFormatInternal().getShortTimeFormat()*/;
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    public KpiDatePicker startDate;
    public KpiDatePicker dueDate;
    public StartEndTime startTime;
    public StartEndTime endTime;
    public KpiCheckBox allDay;
    private boolean withQuarterTime;
    private boolean withTimeSlotTime;
    private boolean showOnlyDatePicker;
    private boolean autoValue = true;

    public DateTimePicker() {
        initComponents(null);
    }

    public DateTimePicker(String dateTimeFormat) {
        initComponents(dateTimeFormat);
    }

    public DateTimePicker(final Boolean onlyADate, boolean withTimeSlotTime) {
        this.withTimeSlotTime = withTimeSlotTime;
        if (onlyADate) {
            initADate();
        } else {
            initComponents(null);
        }
    }

    public DateTimePicker(final Boolean onlyADate, boolean withTimeSlotTime, boolean showOnlyDatePicker) {
        this.withTimeSlotTime = withTimeSlotTime;
        this.showOnlyDatePicker = showOnlyDatePicker;
        if (onlyADate) {
            initADate();
        } else {
            initComponents(null);
        }
    }

    public void initComponents(String dateTimeFormat) {
        //startDate
        startDate = new KpiDatePicker();
        if (dateTimeFormat != null && !"".equals(dateTimeFormat)) {
            startDate.setDateTimeFormat(DateTimeFormat.getFormat(dateTimeFormat));
        }
        setStartDatePicker(startDate);
        startDate.addValueChangeHandler(dateValueChangeEvent -> {
            if (getDueDatePicker().getDate() == null && autoValue) {
                setDueDate(new Date());
            }
            if (getDueDate() != null && getStartDate().after(getDueDate())) {
                Date dueDate = DateUtil.addMinutes(getStartDate(), 30);
                setDueDate(dueDate);
                if (!isAllDay() || !withTimeSlotTime) {
                    ListBox startBox = startTime.getListBox();
                    int startIndex = startTime.getItemIndex(startTime.getText());
                    String endTimeText = startIndex < startBox.getItemCount() - (isWithQuarterTime() ? 2 : 4) ? startBox.getItemText(startIndex + (isWithQuarterTime() ? 1 : 4)) : startBox.getItemText(startIndex);
                    if (endTime.getItemIndex(endTimeText) != -1) {
                        endTime.getListBox().setSelectedIndex(endTime.getItemIndex(endTimeText));
                    }
                    endTime.setText(endTimeText);
                }
            }
        });

        //startTime
        startTime = new StartEndTime(timeFormat.format(new Date()));
        startTime.setWidth("100px");
        startTime.setText(withTimeSlotTime ? Utils.getDefaultCurrentUserTimeSlotStartTIME() : "00:00");
        startTime.setVisible(false);
        startTime.getListBox().addChangeHandler(event -> {
            ListBox listBox = startTime.getListBox();
            startTime.setText(startTime.getListBox().getItemText(listBox.getSelectedIndex()));

            int itemCount = listBox.getItemCount();
            if (getStartDate().after(getDueDate()) || getStartDate().equals(getDueDate())) {
                setDueDate(DateUtil.addMinutes(getStartDate(), isWithQuarterTime() ? 30 : 60));
                String endTimeText = listBox.getSelectedIndex() < itemCount - (isWithQuarterTime() ? 2 : 4) ? listBox.getItemText(listBox.getSelectedIndex() + (isWithQuarterTime() ? 2 : 4)) : listBox.getItemText(listBox.getSelectedIndex() + (isWithQuarterTime() ? 2 : 4) - itemCount);
                if (endTime.getItemIndex(endTimeText) != -1) {
                    endTime.getListBox().setSelectedIndex(endTime.getItemIndex(endTimeText));
                }
                endTime.setText(endTimeText);
            }
        });

        //endDate
        dueDate = new KpiDatePicker();
        if (dateTimeFormat != null && !"".equals(dateTimeFormat)) {
            dueDate.setDateTimeFormat(DateTimeFormat.getFormat(dateTimeFormat));
        }
        setDueDatePicker(dueDate);
        dueDate.addValueChangeHandler(dateValueChangeEvent -> {
            if (startDate.getDate() == null && autoValue) {
                setStartDate(new Date());
            }
            if (getDueDate().before(getStartDate()) && autoValue) {
                setStartDate(getDueDate());
                if (!isAllDay() || !withTimeSlotTime) {
                    ListBox startBox = startTime.getListBox();
                    int startIndex = startTime.getItemIndex(startTime.getText());
                    String endTimeText = startIndex < startBox.getItemCount() - (isWithQuarterTime() ? 2 : 4) ? startBox.getItemText(startIndex + (isWithQuarterTime() ? 1 : 4)) : startBox.getItemText(startIndex);
                    if (endTime.getItemIndex(endTimeText) != -1) {
                        endTime.getListBox().setSelectedIndex(endTime.getItemIndex(endTimeText));
                    }
                    endTime.setText(endTimeText);
                }
            }
        });

        //endTime
        endTime = new StartEndTime(timeFormat.format(new Date()));
        endTime.setWidth("100px");
        endTime.setText(withTimeSlotTime ? Utils.getDefaultCurrentUserTimeSlotEndTIME() : "00:00");
        endTime.setVisible(false);
        endTime.getListBox().addChangeHandler(event -> {
            ListBox listBox = startTime.getListBox();
            ListBox endListBox = endTime.getListBox();
            endTime.setText(endTime.getListBox().getItemText(endListBox.getSelectedIndex()));

            int itemCount = listBox.getItemCount();
            if (getStartDate().after(getDueDate()) || getStartDate().equals(getDueDate())) {
                setStartDate(DateUtil.addMinutes(getDueDate(), isWithQuarterTime() ? -30 : -60));
                String startTimeText = endTime.getListBox().getSelectedIndex() < (isWithQuarterTime() ? 2 : 4) ? listBox.getItemText(itemCount - ((isWithQuarterTime() ? 2 : 4) - endTime.getListBox().getSelectedIndex())) : listBox.getItemText(endTime.getListBox().getSelectedIndex() - (isWithQuarterTime() ? 2 : 4));
                if (startTime.getItemIndex(startTimeText) != -1) {
                    startTime.getListBox().setSelectedIndex(startTime.getItemIndex(startTimeText));
                }
                startTime.setText(startTimeText);
            }
        });

        //all Day
        allDay = new KpiCheckBox(" " + wfmStrings.allDay());
        allDay.getElement().setAttribute("style", "margin-left:5px");
        allDay.setValue(true);
        allDay.addValueChangeHandler(valueChangeEvent -> {
            if (valueChangeEvent.getValue()) {
                //withTimeSlotTime -> set default start date time (default -> employee timeSlot start time)
                //set default start date time (default zero)
                String startTimeTextTZero = withTimeSlotTime ? Utils.getDefaultCurrentUserTimeSlotStartTIME() : "00:00";
                startTime.setText(startTimeTextTZero);
                startTime.setVisible(false);
                if (startTime.getItemIndex(startTimeTextTZero) != -1) {
                    startTime.getListBox().setSelectedIndex(startTime.getItemIndex(startTimeTextTZero));
                }
                //withTimeSlotTime - > set default end date time (default -> employee timeSlot end time)
                //set default end date time (default zero)
                String endTimeTextTZero = withTimeSlotTime ? Utils.getDefaultCurrentUserTimeSlotEndTIME() : "00:00";
                endTime.setText(endTimeTextTZero);
                endTime.setVisible(false);
                if (endTime.getItemIndex(endTimeTextTZero) != -1) {
                    endTime.getListBox().setSelectedIndex(endTime.getItemIndex(endTimeTextTZero));
                }
            } else {
                //set default start date time (default current time (with zero second))
                String startTimeTextT = timeFormat.format(DateUtil.getDateWithZeroMinutes(new Date()));
                startTime.setText(startTimeTextT);
                startTime.setVisible(true);
                if (startTime.getItemIndex(startTimeTextT) != -1) {
                    startTime.getListBox().setSelectedIndex(startTime.getItemIndex(startTimeTextT));
                }
                //set default start date time (default current time + 1 (with zero second))
                String endTimeTextT = timeFormat.format(DateUtil.getDateWithZeroMinutes(DateUtil.addHours(new Date(), 1)));
                endTime.setText(endTimeTextT);
                endTime.setVisible(true);
                if (endTime.getItemIndex(endTimeTextT) != -1) {
                    endTime.getListBox().setSelectedIndex(endTime.getItemIndex(endTimeTextT));
                }
            }
        });
//        allDay.setValue(true, true);

    }

    public void initADate() {
        //startDate
        startDate = new KpiDatePicker();
//        startDate.setWidth("80px");
        //startTime
        startTime = new StartEndTime(timeFormat.format(new Date()));
        startTime.setWidth("60px");

        //all Day
        allDay = new KpiCheckBox(" " + wfmStrings.allDay());
        allDay.addValueChangeHandler(valueChangeEvent -> {
            if (valueChangeEvent.getValue()) {
                //withTimeSlotTime -> set default start date time (default -> employee timeSlot start time)
                //set default start date time (default zero)
                String startTimeTextTZero = withTimeSlotTime ? Utils.getDefaultCurrentUserTimeSlotStartTIME() : "00:00";
                startTime.setText(startTimeTextTZero);
                startTime.setVisible(false);
                if (startTime.getItemIndex(startTimeTextTZero) != -1) {
                    startTime.getListBox().setSelectedIndex(startTime.getItemIndex(startTimeTextTZero));
                }
            } else {
                //set default start date time (default current time (with zero second))
                String startTimeTextT = timeFormat.format(DateUtil.getDateWithZeroMinutes(new Date()));
                startTime.setText(startTimeTextT);
                startTime.setVisible(true);
                if (startTime.getItemIndex(startTimeTextT) != -1) {
                    startTime.getListBox().setSelectedIndex(startTime.getItemIndex(startTimeTextT));
                }
            }
        });
        allDay.setValue(true, true);

        FlexTable content = new FlexTable();
        content.setWidget(0, 0, startDate);
        if (!showOnlyDatePicker) {
            content.getFlexCellFormatter().setColSpan(0, 0, 2);
            content.setWidget(1, 0, startTime);
            content.setWidget(1, 1, allDay);
            content.getFlexCellFormatter().setVerticalAlignment(1, 1, HasVerticalAlignment.ALIGN_MIDDLE);
        }
        initWidget(content);
    }

    public KpiDatePicker getStartDatePicker() {
        return this.startDate;
    }

    private void setStartDatePicker(KpiDatePicker startDate) {
        this.startDate = startDate;
    }

    public KpiDatePicker getDueDatePicker() {
        return this.dueDate;
    }

    private void setDueDatePicker(KpiDatePicker dueDate) {
        this.dueDate = dueDate;
    }

    public StartEndTime getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime.time = startTime;
        this.startTime.setValue(startTime);
        this.startTime.getListBox().setSelectedIndex(this.startTime.getItemIndex(startTime));
    }

    public StartEndTime getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime.time = endTime;
        this.endTime.setValue(endTime);
        this.endTime.getListBox().setSelectedIndex(this.endTime.getItemIndex(endTime));
    }

    public Date getStartDate() {
        if (startDate.getDate() == null) {
            return null;
        }
        return getDateTime(startDate.getDate(), startTime.getText());
    }

    public void setStartDate(Date date) {
        startDate.setDate(date);
    }

    public Date getDueDate() {
        if (dueDate.getDate() == null) {
            return null;
        }
        return getDateTime(dueDate.getDate(), endTime.getText());//getDateTime(dueDate.getDate(), endTime.getListBox().getSelectedIndex() >= 0 ? endTime.getListBox().getItemText(endTime.getListBox().getSelectedIndex()) : endTime.getText());
    }

    public void setDueDate(Date date) {
        dueDate.setDate(date);
    }

    public boolean isAllDay() {
        return allDay.getValue();
    }

    @SuppressWarnings("deprecation")
    public static Date getDateTime(Date date, String time) {
        int year = date.getYear();
        int month = date.getMonth();
        int day = date.getDate();
        String[] timeHour = time.split(":");
        String[] timeMinute = timeHour[1].split("\\ ");
        int hour = Integer.parseInt(timeHour[0]);
        int minute = Integer.parseInt(timeMinute[0]);
        if (timeMinute.length == 2) {
            /*if ("PM".equals(timeMinute[1]) && (timeHour[0].length() == 1 || "10".equals(timeHour[0]) || "11".equals(timeHour[0]))) {*/
            if ("PM".equals(timeMinute[1]) && !"12".equals(timeHour[0])) {
                hour += 12;
            } else if ("AM".equals(timeMinute[1]) && "12".equals(timeHour[0])) {
                hour = 0;
            }
        }
        return new Date(year, month, day, hour, minute);
    }

    public void setDefaultValue() {
        startDate.setDefaultValue();
    }

    public void setAllDay(boolean show) {
        allDay.setValue(show, true);
    }

    public void setVisableAllCheck(boolean visable) {
        allDay.setVisible(visable);
    }

    public KpiCheckBox getAllDayCheckBox() {
        return allDay;
    }

    public boolean isWithQuarterTime() {
        return withQuarterTime;
    }

    public void setWithQuarterTime(boolean withQuarterTime) {
        this.withQuarterTime = withQuarterTime;
    }

    public void setEnabled(boolean enabled) {
        startDate.setEnabled(enabled);
        startTime.setEnabled(enabled);
        dueDate.setEnabled(enabled);
        endTime.setEnabled(enabled);
        allDay.setEnabled(enabled);
    }

    public void setAutoValue(boolean autoValue) {
        this.autoValue = autoValue;
    }
}