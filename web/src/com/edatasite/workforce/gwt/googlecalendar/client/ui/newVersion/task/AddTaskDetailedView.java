package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.task;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.IdTime;
import com.edatasite.workforce.gwt.core.client.rpc.ProjectItem;
import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.ProjectEmployeesAvailabilityCheck;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.selectPanel.SelectPanel;
import com.edatasite.workforce.gwt.core.client.ui.table.TableColumn;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.NTreeSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.treeselect.TreeSelect;
import com.edatasite.workforce.gwt.core.client.ui.view.Reminder;
import com.edatasite.workforce.gwt.core.client.ui.view.ReminderView;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.googlecalendar.client.rpc.GoogleCalendarService;
import com.edatasite.workforce.gwt.googlecalendar.client.rpc.GoogleCalendarServiceAsync;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.SaveAppointmentHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: abdullo_
 * Date: Aug 4, 2010
 * Time: 4:45:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class AddTaskDetailedView {

    private final GoogleCalendarServiceAsync calendarService = GoogleCalendarService.App.get();
    private final DateTimeFormat timeFormat = DateTimeFormat.getShortTimeFormat();

    private final WfmStrings wfmStrings = WfmStrings.App.get();
    private final CommonServiceAsync commonService = CommonService.App.get();
    private WfmForm.Field tNameField;
    private WfmForm.Field projectField;
    private WfmForm.Field priorityField;
    private WfmForm.Field dateField;
    private KpiModal shell;
    private WfmForm pnlMain;
    private TextBox taskName;
    private When txtWhen;
    private DataListBox project;
    private DataListBox priority;
    private Reminder reminder;
    private WfmButton2 btnSave;
    private SelectPanel assigneePanel;
    private Appointment appointment;
    private final TableColumn[] columns = new TableColumn[2];

    private SaveAppointmentHandler handler;
    private ReminderView reminderView;
    private KpiCheckBox enableEmailReminder;
    private VerticalPanel recurringPanel;

    public AddTaskDetailedView(Appointment appointment) {
        if (appointment.getStartDate() == null) {
            appointment.setStartDate(new Date());
            appointment.setEndDate(DateUtil.addMinutes(appointment.getStartDate(), 30));
        }
        this.appointment = appointment;
        initComponents();

        if (appointment.getSubject() != null) {
            setTaskDetails();
        }
    }

    private void initComponents() {
        final String defaultWidth = "400px";

        shell = new KpiModal();
        shell.addCloseHandler(popupPanelCloseEvent -> {
            if (reminderView != null) {
                enableEmailReminder.setValue(false);
                recurringPanel.remove(reminderView);
                reminderView = null;
            }
        });

        //taskName
        taskName = new TextBox();
        taskName.setWidth(defaultWidth);
        //--------
        //assignee to Task
        columns[0] = new TableColumn(wfmStrings.employee(), wfmStrings.employee());
        columns[1] = new TableColumn(wfmStrings.delete(), wfmStrings.action(), 15);
        assigneePanel = new SelectPanel(columns);
        assigneePanel.setTableWidth(200);
        assigneePanel.setTreePanelWidth(220);
        assigneePanel.getTreeSelect().setEmployeeAvailabilityCheckHandler(new ProjectEmployeesAvailabilityCheck() {
            @Override
            public void onOnlyAvailableRadioButtonClick() {
                reloadOnlyAvailableAssignees();
            }

            public void onAllRadioButtonClick() {
                //it's OK do nothing
            }

            @Override
            public void onOnlyAvailableClickedSetStartAndEndDate() {
                //it's OK do nothing
            }

        });
        assigneePanel.getTreeSelect().setAllEmployeesHandler(new ProjectEmployeesAvailabilityCheck() {
            @Override
            public void onOnlyAvailableRadioButtonClick() {
                //it's OK do nothing
            }

            public void onAllRadioButtonClick() {
                reloadAssignees();
            }

            @Override
            public void onOnlyAvailableClickedSetStartAndEndDate() {
                //it's OK do nothing
            }
        });
        //txtWhen
        txtWhen = new When();
        //--------
        //We need 4 handlers to reloadOnlyAvailableEmployees in case the value changes in either StartDate, EndDate, StartTime and/or EndTime

        txtWhen.getStartDatePicker().addChangeHandler(dateValueChangeEvent -> reloadOnlyAvailableAssigneesOnDemand());

        txtWhen.getEndDatePicker().addChangeHandler(dateValueChangeEvent -> reloadOnlyAvailableAssigneesOnDemand());

        txtWhen.getStartTime().getListBox().addChangeHandler(event -> {
            txtWhen.setStartTime(txtWhen.getStartTime().getListBox().getItemText(txtWhen.getStartTime().getListBox().getSelectedIndex()));
            reloadOnlyAvailableAssigneesOnDemand();
        });

        txtWhen.getEndTime().getListBox().addChangeHandler(event -> {
            txtWhen.setEndTime(txtWhen.getEndTime().getListBox().getItemText(txtWhen.getEndTime().getListBox().getSelectedIndex()));
            reloadOnlyAvailableAssigneesOnDemand();
        });

        //txtWhere
        project = new DataListBox();
        project.setAllowFirstItem(true);
        project.setWidth("300px");
        //--------

        //priority
        priority = new DataListBox();
        priority.setWidth("300px");
        //--------

        enableEmailReminder = new KpiCheckBox("&nbsp;", true);

        recurringPanel = new VerticalPanel();
        recurringPanel.add(enableEmailReminder);
        enableEmailReminder.addValueChangeHandler(booleanValueChangeEvent -> {
            if (enableEmailReminder.getValue()) {
                reminderView = new ReminderView(SchedulerConstant.RECURRING_TASK_FORM);
                reminderView.setStyleName("reccurence-view");
                reminderView.drawForm(appointment.getRecurrenceJobItem());
                reminderView.setStartDate(txtWhen.getStartDate());
                reminderView.setStart(txtWhen.getStartDate());
                reminderView.hideNeverRadioButton();
                recurringPanel.add(reminderView);
                shell.center();
            } else {
                recurringPanel.remove(reminderView);
                reminderView = null;
            }
        });

        if (appointment != null && appointment.getRecurrenceJobItem() != null) {
            enableEmailReminder.setValue(appointment.getRecurrenceJobItem().isEnabled(), true);
            reminderView.hideNeverRadioButton();
            shell.center();
        } else {
            enableEmailReminder.setValue(false, true);
        }

        //pnlMian
        final String styleCls = "";
        pnlMain = new WfmForm();
        tNameField = pnlMain.addField(wfmStrings.taskName(), taskName, true);
        dateField = pnlMain.addField(wfmStrings.startDate(), txtWhen, true);
        projectField = pnlMain.addField(wfmStrings.project(), project, true);
        priorityField = pnlMain.addField(wfmStrings.priority(), priority, true);
        pnlMain.addField("<span class=" + styleCls + ">" + wfmStrings.recurring() + "</span>", recurringPanel);


        calendarService.getReminders(appointment.getObjectID(), false, new AbstractAsyncCallback<ArrayList<CalendarEventReminder>>() {
            public void failure(Throwable caught) {
            }

            public void success(ArrayList<CalendarEventReminder> result) {
                reminder.setReminderDatas(result);
            }
        });
        reminder = new Reminder(false);
        pnlMain.addField(wfmStrings.reminders(), reminder);
        assigneePanel.setHeight(240);

        //btnSave
        btnSave = new WfmButton2(wfmStrings.save(), sender -> save());

        final WfmButton2 btnCancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT, sender -> shell.close());

        //pnlButtons
        pnlMain.addButton(btnSave);
        pnlMain.addButton(btnCancel);

        final HorizontalPanel panel = new HorizontalPanel();
        panel.setSpacing(5);
        panel.add(pnlMain);
        panel.add(assigneePanel);


        initProjects();

        project.addValueChangeHandler(changeEvent -> reloadAssignees());
        //Priority
        priority.setVisibleItemCount(1);
        calendarService.getPriorities(new AbstractAsyncCallback<SelectItem[]>() {
            public void success(final SelectItem[] object) {
                DeferredCommand.addCommand(() -> {
                    priority.setItems(object);
                    if (appointment != null && appointment.getPriorityID() != null) {
                        priority.setSelected(appointment.getPriorityID());
                    } else if (object != null) {
                        for (SelectItem anObject : object) {
                            if (anObject.getName().trim().equals(wfmStrings.medium())) {
                                priority.setSelected(anObject.getId());
                            }
                        }
                    }
                });
            }
        });


        shell.add(panel);
        shell.setTitle(wfmStrings.createTask());
        shell.setWidth("1000px");
        shell.open();
    }

    private void setTaskDetails() {
        txtWhen.allDay.setValue(appointment.isAllDay(), true);
        taskName.setText(appointment.getSubject());
        txtWhen.setStartDate(appointment.getStartDate());
        txtWhen.setEndDate(appointment.getEndDate());

        if (appointment.getObjectID() != null) {
            btnSave.setText(wfmStrings.update());
            shell.setTitle(wfmStrings.updateExistingTask());
            if (appointment.getProjectEmployees() != null && appointment.getProjectEmployees().length > 0) {
                checkAssignees();
            } else {
                calendarService.getTaskAssignees(appointment.getObjectID(), new AbstractAsyncCallback<ArrayList<IdTime>>() {
                    @Override
                    public void failure(Throwable caught) {
                        //To change body of implemented methods use File | Settings | File Templates.
                    }

                    @Override
                    public void success(ArrayList<IdTime> result) {
                        if (result != null && result.size() > 0) {
                            appointment.setProjectEmployees(result.toArray(new IdTime[]{}));
                        }
                        checkAssignees();
                    }
                });
            }
        }
    }

    /**
     * When shell is opened the next calling this method allows fast loading of it,
     * during opening it clears all filled fields and puts null to appointment's id
     * value, because otherwise it wouldn't create new appointment, it will update it instead.
     */
    public void clearAndShow() {
        appointment = new Appointment();
        appointment.setObjectID(null);
        if (!assigneePanel.getTreeSelect().isAllEmployeesRadioButtonChecked()) {
            assigneePanel.getTreeSelect().setAllEmployeesRadioButtonChecked();
        }
        final Date today = new Date();
        taskName.setText("");
        project.clearSelected();
        project.setSelectedItem(null);
        txtWhen.setStartDate(today);
        txtWhen.setEndDate(today);
        priority.clearSelected();
        assigneePanel.clearTreeView();
        shell.open();
    }

    private void save() {
        if (!validate()) {
            return;
        }

        if (handler != null) {
            saveOrUpdate();
        } else {
            /**
             * This is the only exception, because out of calendar view we are also saving appointment,
             * therefore we cannot bind it to the calendar view. In all other cases the logic was centralized.
             */
            appointment = getTaskData();
            calendarService.saveCalendarTask(appointment, new AbstractAsyncCallback<SelectItem>() {
                public void success(SelectItem event) {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullyAdded(), wfmStrings.task()), Info.Type.INFO);
//                  if (appointment.getObjectID() == null) {
//                      appointment.setObjectID(event.getId());
//                  }
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CALENDAR_EVENT_ADD, event.getId(), shell);
                }
            });

        }

        shell.close();
    }

    private void saveOrUpdate() {
        if (appointment.getObjectID() != null) {
            final RecurrenceJobItem oldRecurrenceJobItem = appointment.getRecurrenceJobItem();
            appointment = getTaskData();
            if (oldRecurrenceJobItem != null && appointment.getRecurrenceJobItem() != null) {
                appointment.setAction(Constants.EDIT_THIS_INSTANCE);     // quick fix for editing recurring task
                handler.onSaveOrUpdateTask(appointment);
//                ShortAppointmentView.updateOrDeleteEventItem(appointment, "edit", handler, null);
            } else {
                appointment.setAction(Constants.EDIT_THIS_INSTANCE);
                handler.onSaveOrUpdateTask(appointment);
            }
            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CALENDAR_EVENT_ADD, this.appointment.getObjectID(), shell);
        } else {
            appointment = getTaskData().clone();
            appointment.setInstancesCount(10);
            handler.onSaveOrUpdateTask(appointment);
        }
    }

    public void onSaveTask(SaveAppointmentHandler handler) {
        this.handler = handler;
    }

    private boolean validate() {
        int errors = 0;
        pnlMain.cleanupErrors();
        if (!Validation.validateTextBoxRequired(taskName, tNameField)) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(project, projectField, "Please choose Project")) {
            errors++;
        }
        if (!Validation.validateListBoxRequired(priority, priorityField, "Please choose Priority")) {
            errors++;
        }
        if (enableEmailReminder.getValue()) {
            errors += (reminderView != null && !reminderView.validate()) ? 1 : 0;
        }
//        if (!Validation.validateIntegersEqualary(txtWhen.parseTimeInMinutes(txtWhen.startTime.getText()), txtWhen.parseTimeInMinutes(txtWhen.endTime.getText()), txtWhen.isAllDay(), dateField)) {
//            errors++;
//        }
        if (!Validation.validateDateOrder(txtWhen.getStartDate(), txtWhen.getEndDate(), null, txtWhen.isAllDay())) {
            dateField.setErrorMessage(wfmStrings.startTimeValidation(), "");
            return false;
        } else {
            dateField.setErrorMessage(null, "");
        }
        if (!Validation.validateSelectPanel(assigneePanel)) {
            errors++;
        }
        //validate so that recurrence end date is not before the first task's end date
        if (reminderView != null && !reminderView.validateRecurrenceEndDate(txtWhen.getEndDate(), wfmStrings.recurrentTaskEndDateValidation())) {
            return false;
        }
        //in case the recurrence is daily, validate
        if (reminderView != null && !reminderView.validateIntervalGreaterThanZero(wfmStrings.recurrenceIntervalShouldBeGreaterThanZero())) {
            return false;
        }
        if (errors > 0) {
            Info.show(wfmStrings.areYouSureThatEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private Appointment getTaskData() {
        final Date start = txtWhen.getStartDate();
        final Date end = txtWhen.getEndDate();

        appointment.setSubject(taskName.getText());
        appointment.setStartDate(start);
        appointment.setEndDate(end);
        appointment.setTaskCreator(Utils.getFullName());
        appointment.setAllDay(appointment.isAllDay());
        appointment.setMultiDay(appointment.isMultiDayAppointment());
        appointment.setProjectID(project.getSelectedItem().getId());
        appointment.setPriorityID(priority.getSelectedItem().getId());
        appointment.setProjectName(project.getSelectedItem().getName());
        appointment.setPriorityName(priority.getSelectedItem().getName());
        appointment.setAllDay(txtWhen.isAllDay());
        appointment.setInstancesCount(10);
        if (txtWhen.isAllDay()) {
            Date dueDate = (Date) end.clone();
            dueDate.setHours(23);
            dueDate.setMinutes(59);
            dueDate.setSeconds(59);
            appointment.setEndDate(dueDate);
        }
        appointment.setRecurrenceJobItem(null);
        if (enableEmailReminder.getValue()) {
            if (reminderView != null) {
                RecurrenceJobItem recurrenceJobItem = reminderView.getData();
                if (recurrenceJobItem != null) {
                    recurrenceJobItem.setStartDate(start);
                    if (recurrenceJobItem.getType().equals(SchedulerConstant.RECURRENCE_TYPE_MONTHLY)) {
                        recurrenceJobItem.setMonthlyOrYearlyDay(appointment.getStartDate().getDate());
                        recurrenceJobItem.setMonthlyOrYearlyPatternOption(SchedulerConstant.MONTHLY_OR_YEARLY_PATTERN_CUSTOM);
                        //recurrenceJobItem.setInterval(1);
                    }
                    Date normalizedEndDate = recurrenceJobItem.getEndDate();
                    if (normalizedEndDate != null) {
                        if (!txtWhen.isAllDay()) {
                            normalizedEndDate.setHours(txtWhen.getEndDate().getHours());
                            normalizedEndDate.setMinutes(txtWhen.getEndDate().getMinutes());
                            recurrenceJobItem.setEndDate(normalizedEndDate);
                        } else {
                            normalizedEndDate.setHours(23);
                            normalizedEndDate.setMinutes(59);
                            recurrenceJobItem.setEndDate(normalizedEndDate);
                        }
                    }
                    appointment.setRecurrenceJobItem(recurrenceJobItem);
                }
            }
        }
        if (assigneePanel.getSelectedItems().length != 0) {
            HashMap<Integer, IdTime> assignees = new HashMap<>();
            if (appointment.getProjectEmployees() != null) {
                appointment.getProjectEmployees();
                for (IdTime idTime : appointment.getProjectEmployees()) {
                    assignees.put(idTime.getId(), idTime);
                }
            }
            Integer[] selectedItems = assigneePanel.getSelectedItems();
            for (Integer id : selectedItems) {
                if (!assignees.containsKey(id)) {
                    assignees.put(id, new IdTime(id, 0, (float) 0));
                }
            }
            List<Integer> integers = Arrays.asList(selectedItems);
            List<Integer> integerSet = new ArrayList<>();
            integerSet.addAll(assignees.keySet());
            for (Integer id : integerSet) {
                if (!integers.contains(id)) {
                    assignees.remove(id);
                }
            }

            appointment.setProjectEmployees(assignees.values().toArray(new IdTime[]{}));
        }

        appointment.setReminder(reminder.getReminderDatas());

        return appointment;
    }

    private class When extends Composite {

        private DatePicker startDate;
        private DatePicker endDate;
        private StartEndTime startTime;
        private StartEndTime endTime;
        private KpiCheckBox allDay;

        public When() {
            initComponents();
        }

        private void initComponents() {
            //txtWhen
            startDate = new DatePicker();
            startDate.setWidth("75px");
            startDate.setDate(appointment.getStartDate());
            startDate.addChangeHandler(event -> {
                if (reminderView != null) {
                    reminderView.setStartDate(startDate.getDate());
                }
                if (startDate.getDate().after(endDate.getDate())) {
                    endDate.setDate(startDate.getDate());
                }
            });

            //endDate
            endDate = new DatePicker();
            endDate.setWidth("75px");
            endDate.setDate(appointment.getStartDate());

            //startTime
            startTime = new StartEndTime(timeFormat.format(appointment.getStartDate()));
            startTime.setWidth("55px");

            //endTime
            endTime = new StartEndTime(timeFormat.format(appointment.getEndDate()));
            endTime.setWidth("55px");

            startTime.onClick(() -> {
                Date testStart = getDateTime(startDate.getDate(), startTime.getText());
                Date testEnd = getDateTime(endDate.getDate(), endTime.getText());
                if (testStart.equals(testEnd) || testStart.after(testEnd)) {
                    Date end = DateUtil.addMinutes(testStart, 30);
                    endDate.setDate(end);
                    endTime.setText(timeFormat.format(end));
                }
            });
            endTime.onClick(() -> {
                Date testStart = getDateTime(startDate.getDate(), startTime.getText());
                Date testEnd = getDateTime(endDate.getDate(), endTime.getText());
                if (testEnd.equals(testStart) || testEnd.before(testStart)) {
                    Date start = DateUtil.addMinutes(testEnd, -30);
                    startDate.setDate(start);
                    startTime.setText(timeFormat.format(start));
                }
            });

            //all Day
            allDay = new KpiCheckBox(" " + wfmStrings.allDay());
            allDay.addValueChangeHandler(valueChangeEvent -> {
                if (txtWhen != null) {
                    reloadOnlyAvailableAssigneesOnDemand();
                }
                Date startDate = DateUtil.getDateTime();
                Date endDate = (Date) startDate.clone();
                endDate.setMinutes(startDate.getMinutes() + 30);
                if (allDay.getValue()) {
                    DateUtil.resetTime(startDate);
                    DateUtil.getDayLastTime(endDate);
                    startTime.setText(timeFormat.format(startDate));
                    endTime.setText(timeFormat.format(endDate));
                    startTime.setVisible(false);
                    endTime.setVisible(false);
                    if (reminderView != null) {
                        reminderView.setStartDate(startDate);
                    }
                } else {
                    if (appointment.getObjectID() != null) {
                        startTime.setText(timeFormat.format(appointment.getStartDate()));
                        if (appointment.getStartDate().equals(appointment.getEndDate())) {
                            final Date end = (Date) appointment.getStartDate().clone();
                            end.setMinutes(appointment.getStartDate().getMinutes() + 30);
                            endTime.setText(timeFormat.format(end));
                        } else {
                            if (appointment.isAllDay()) {
                                startTime.setText(timeFormat.format(startDate));
                                endTime.setText(timeFormat.format(endDate));
                            } else {
                                endTime.setText(timeFormat.format(appointment.getEndDate()));
                            }
                        }
                        startDate = appointment.getStartDate();
                    } else {
                        if ("12:00 AM".equals(timeFormat.format(appointment.getStartDate()))) {
                            startTime.setText(timeFormat.format(startDate));
                            endTime.setText(timeFormat.format(endDate));
                        } else {
                            startTime.setText(timeFormat.format(appointment.getStartDate()));
                            endTime.setText(timeFormat.format(appointment.getEndDate()));
                        }
                    }
                    startTime.setVisible(true);
                    endTime.setVisible(true);
                    if (reminderView != null) {
                        reminderView.setStartDate(startDate);
                    }

                }
            });
            allDay.setValue(true, true);

            final FlexTable table = new FlexTable();
            table.setWidth("100%");
            table.setWidget(0, 0, startDate);
            table.setWidget(0, 1, startTime);
            table.setWidget(0, 2, new HTML("<b class=customTitle>" + "&nbsp;&nbsp;" + wfmStrings.dueDate() + "</b>" + ("<font color='red'>*</font>:</b>")));
            table.setWidget(0, 3, endTime);
            table.setWidget(0, 4, endDate);
            table.setWidget(0, 5, allDay);

            initWidget(table);
        }

        public Date getStartDate() {
            return getDateTime(startDate.getDate(), startTime.getText());
        }

        public void setStartDate(Date date) {
            startDate.setDate(date);
        }

        public Date getEndDate() {
            return getDateTime(endDate.getDate(), endTime.getText());
        }

        public void setEndDate(Date date) {
            endDate.setDate(date);
            if (appointment.isAllDay()) {
                allDay.setValue(true, true);
            }
        }

        //we need to get the WfmDatePicker for startDate and endDate
        //startDate

        public DatePicker getStartDatePicker() {
            return startDate;
        }
        //endDate

        public DatePicker getEndDatePicker() {
            return endDate;
        }

        public void setStartTime(String startTime) {
            this.startTime.time = startTime;
            this.startTime.setValue(startTime);
        }

        public StartEndTime getStartTime() {
            return startTime;
        }

        public StartEndTime getEndTime() {
            return endTime;
        }

        public void setEndTime(String startTime) {
            this.endTime.time = startTime;
            this.endTime.setValue(startTime);
        }

        public boolean isAllDay() {
            return allDay.getValue();
        }

        private Date getDateTime(Date date, String time) {
            final int year = date.getYear();
            final int month = date.getMonth();
            final int day = date.getDate();
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

        private int parseTimeInMinutes(String time) {
            String[] timeHour = time.split(":");
            String[] timeMinute = timeHour[1].split("\\ ");
            int hour = Integer.parseInt(timeHour[0]);
            int minute = Integer.parseInt(timeMinute[0]);
            if (timeMinute.length == 2) {
                /*if ("PM".equals(timeMinute[1]) && timeHour[0].length() == 1 && "10".equals(timeHour[0]) && "11".equals(timeHour[0])) {*/
                if ("PM".equals(timeMinute[1]) && !"12".equals(timeHour[0])) {
                    hour += 12;
                } else if ("AM".equals(timeMinute[1]) && "12".equals(timeHour[0])) {
                    hour = 0;
                }
            }

            return hour * 60 + minute;
        }
    }

    private class StartEndTime extends TextBox {

        private PopupPanel popup;
        private ListBox listTime;
        private String time;
        private Command command;
        private HashMap<String, Integer> listItems;

        public StartEndTime(String time) {
            this.time = time;
            initComponents();
        }

        private void initComponents() {
            super.setText(time);
            super.setStyleName("input-default-color");

            listItems = new HashMap<>();

            //listTime
            listTime = new ListBox(true);
            listTime.setSize("75", "125");
            listTime.addClickHandler(event -> {
                setStyleName("input-default-color");
                popup.hide();
                setText(listTime.getItemText(listTime.getSelectedIndex()));

                if (command != null) {
                    command.execute();
                }
            });

            popup = new PopupPanel(true);
            popup.setWidget(listTime);

            popup.getElement().getStyle().setZIndex(7000);

            setWorkingTime();
            super.addValueChangeHandler(stringValueChangeEvent -> {
                String updateTime = stringValueChangeEvent.getValue();
                if (stringValueChangeEvent.getValue() != null && !stringValueChangeEvent.getValue().equals("")) {
                    String text = ((Utils.parseMinutes(updateTime).toString()));
                    if (text.equals("0")) {
                        setStyleName("gwt-TextBox x-form-invalid");
                        setText(time);
                    } else {
                        if (stringValueChangeEvent.getValue().length() >= 1 && stringValueChangeEvent.getValue().length() <= 5) {
                            setStyleName("input-default-color");
                            Integer h = Integer.valueOf(text) / 60;
                            Integer m = Integer.valueOf(text) - h * 60;
                            String hour = "";
                            String minut = "";
                            if (h < 24) {
                                setStyleName("input-default-color");
                                if (h < 10) {
                                    hour = "0" + h;
                                } else {
                                    hour = h.toString();
                                }
                                if (m == 0) {
                                    minut = "00";
                                } else if (m > 9) {
                                    minut = m.toString();
                                } else {
                                    minut = "0" + m;
                                }
                                String lastTime = hour + ":" + minut;
                                setText(lastTime);
                            } else {
                                setStyleName("gwt-TextBox x-form-invalid");
//                                    setText(time);
                            }
                        } else {
                            setStyleName("gwt-TextBox x-form-invalid");
//                                setText(time);
                        }
                    }
                }
            });
            super.addClickHandler(event -> {
                showPopup(((TextBox) event.getSource()).getOffsetHeight());
                int scrollHeight = listTime.getElement().getScrollHeight();
                listTime.getElement().setScrollTop(scrollHeight / listTime.getItemCount() * getItemIndex(getValue()));
            });
        }

        private void setWorkingTime() {
            //items of ListBox in Time
            int k = 0;
            for (int i = 0; i < 24; i++) {
                for (int j = 0; j <= 45; j += 15) {
                    final Date date = new Date();
                    date.setHours(i);
                    date.setMinutes(j);
                    listTime.addItem(timeFormat.format(date));
                    listItems.put(timeFormat.format(date), k++);
                }
            }
        }

        private void showPopup(final int textboxHeight) {
            popup.setPopupPositionAndShow((offsetWidth, offsetHeight) -> popup.setPopupPosition(getAbsoluteLeft(), getAbsoluteTop() + textboxHeight));
        }

        public void onClick(Command command) {
            this.command = command;
        }

        public int getItemIndex(String value) {
            if (listItems != null && listItems.containsKey(value)) {
                return listItems.get(value);
            }
            return -1;
        }

        public ListBox getListBox() {
            return listTime;
        }
    }

    private void initProjects() {
        TreeSelect.setTickAllVisible(false);
        commonService.getProjects(false, new AbstractAsyncCallback<ProjectItem[]>() {
            public void success(final ProjectItem[] object) {
                DeferredCommand.addCommand(() -> {
                    project.setItems(object);
                    project.setSelected(appointment != null && appointment.getProjectID() != null ? appointment.getProjectID() : 0);
                    reloadAssignees();
                });
            }
        });
    }

    private void reloadAssignees() {
        if (project.getSelectedItem() != null) {
            final Integer projectId = project.getSelectedItem().getId();
            if (projectId != 0) {
                if (!assigneePanel.getTreeSelect().isAllEmployeesRadioButtonChecked()) {
                    assigneePanel.getTreeSelect().setAllEmployeesRadioButtonChecked();
                }
                assigneePanel.clearTreeView();
                calendarService.getAssigneesWithPositions2(projectId, new AbstractAsyncCallback<LinkedList<WfmTreeItem>>() {
                    public void success(LinkedList<WfmTreeItem> results) {
                        TreeSelect.setTickAllVisible(results.size() != 0);
                        LinkedHashMap<WfmTreeItem, LinkedList<WfmTreeItem>> employees = new LinkedHashMap<>();

                        final WfmTreeItem projectItem = new WfmTreeItem(projectId, project.getSelectedItem().getName());

                        if (results.size() != 0) {
                            employees.put(projectItem, results);
                            assigneePanel.setTreeParentChildrenNodes(employees);
                            assigneePanel.expandTreeView();
                        }
                        assigneePanel.checkAllItems(false);
                        if (appointment.getProjectEmployees() != null && appointment.getProjectEmployees().length > 0) {
                            checkAssignees();
                        }
                    }
                });
            }
        }
    }

    private void reloadOnlyAvailableAssignees() {
        if (project.getSelectedItem() != null) {
            final Integer projectId = project.getSelectedItem().getId();
            if (projectId != null && projectId != 0) {
                assigneePanel.clearTreeView();
                calendarService.getOnlyAvailableAssigneesWithPosition1(projectId, txtWhen.getStartDate(), txtWhen.getEndDate(), new AbstractAsyncCallback<LinkedList<WfmTreeItem>>() {
                    public void success(LinkedList<WfmTreeItem> results) {
                        TreeSelect.setTickAllVisible(results.size() != 0);
                        final LinkedHashMap<WfmTreeItem, LinkedList<WfmTreeItem>> employees = new LinkedHashMap<>();

                        final WfmTreeItem projectItem = new WfmTreeItem(projectId, project.getSelectedItem().getName());

                        if (results.size() != 0) {
//                            taskService.getProjectTasks();
                            employees.put(projectItem, results);
                            assigneePanel.setTreeParentChildrenNodes(employees);
                            assigneePanel.expandTreeView();
                        }
                        if (appointment.getProjectEmployees() != null && appointment.getProjectEmployees().length > 0) {
                            checkAssignees();
                        }
                    }
                });
            }
        }
    }

    private void reloadOnlyAvailableAssigneesOnDemand() {
        if (txtWhen.getStartDate().getTime() < txtWhen.getEndDate().getTime()) {
            if (assigneePanel.getTreeSelect().isAvailableEmployeesRadioButtonChecked()) {
                reloadOnlyAvailableAssignees();
            }
        }

    }

    private void checkAssignees() {
        final WfmTreeItem[] members = new WfmTreeItem[appointment.getProjectEmployees().length];
        if (appointment.getProjectEmployees() != null && appointment.getProjectEmployees().length > 0) {
            boolean isChecked = false;
            for (int i = 0; i < assigneePanel.getTree().getItemCount(); i++) {
                final NTreeSelectItem parent = (NTreeSelectItem) assigneePanel.getTree().getItem(i);
                for (int j = 0; j < parent.getChildCount(); j++) {
                    NTreeSelectItem child = (NTreeSelectItem) parent.getChild(j);
                    for (IdTime involvedMember : appointment.getProjectEmployees()) {
                        if (child.getItem().getId().equals(involvedMember.getId())) {
                            child.setChecked(true);
                            assigneePanel.onTreeItemSelection(child, null);
                            isChecked = true;
                            break;
                        }
                    }
                }
                if (isChecked) {
                    break;
                }
            }
        }
    }
}
