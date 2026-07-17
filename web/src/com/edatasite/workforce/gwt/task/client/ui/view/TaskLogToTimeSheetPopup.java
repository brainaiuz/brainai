package com.edatasite.workforce.gwt.task.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.CoreMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.HMWidget;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.rpc.PMNumberingSettings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.edatasite.workforce.gwt.timesheet.client.rpc.SuggestionResponseDTO;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetDataItem;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetService;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetServiceAsync;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.PopupPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Khasan
 * Date: 20.05.14
 * Time: 17:40
 * To change this template use File | Settings | File Templates.
 */
public class TaskLogToTimeSheetPopup extends KpiModal implements Constants {

    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final CoreMessages coreMessages = CoreMessages.App.get();
    private final TimesheetServiceAsync timesheetService = TimesheetService.App.get();
    private final WfmForm form = new WfmForm(new String[]{"35%", "65%"});
    private final Integer taskId;
    private final Integer employeeId;
    private DatePicker dateTimePicker;
    //    private TextBox taskHours;
    private HMWidget timeSpent;
    private TextArea2 area;
    private WfmButton2 apply;
    private WfmButton2 close;
    private WfmButton2 aiSuggest;
    private TimesheetDataItem timesheetDataItem;
    private final Date selectedDate;
    private Command logTimeCmd;
    private final boolean editMode;
    private boolean isCommentMandatory = false;

    private int popupHeight;

    public TaskLogToTimeSheetPopup(Integer taskID) {
        this(taskID, null, null);
    }

    public TaskLogToTimeSheetPopup(Integer taskID, Date selectedDate, Integer employeeId) {
        this(taskID, selectedDate, employeeId, false);
    }

    public TaskLogToTimeSheetPopup(Integer taskID, Date selectedDate, Integer employeeId, boolean isEditMode) {
        this.taskId = taskID;
        this.employeeId = employeeId;
        this.selectedDate = selectedDate;
        this.editMode = isEditMode;
        init();
    }

private void init() {
    LoadingPanel.loading(true);
    dateTimePicker = new DatePicker(true);

    if (!editMode) {
        dateTimePicker.addChangeHandler(changeEvent -> getValidationDateData());
    }
    getWrapper().getParent().addStyleName("tmshtPopup");
    getTimeSheetSettings();

    TaskService.App.get().getTaskName(taskId, new AbstractAsyncCallback<String>() {
        @Override
        public void failure(Throwable throwable) {
            LoadingPanel.loading(false);
        }

        @Override
        public void success(String result) {
            showData(result);
            LoadingPanel.loading(false);
            getValidationDateData();

            Scheduler.get().scheduleFinally(() -> {
                Scheduler.get().scheduleDeferred(() -> {
                    popupHeight = getElement().getOffsetHeight();
                    setHeight(popupHeight + "px");
                });
            });
        }
    });
    setTitle(wfmStrings.logTime());
    open();
}

    private void showData(String result) {


        HTML taskName = new HTML("<b>" + result != null ? result : " " + "</b>");
        taskName.addStyleName("tmshtPopup-taskName");
        dateTimePicker.setDate(selectedDate != null ? selectedDate : new Date());

        /*taskHours = new TextBox();
        taskHours.setWidth("130px");*/
        timeSpent = new HMWidget();
        area = new TextArea2(1000, wfmStrings.comments());
//        area.getTextArea().setWidth("100%");

        apply = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        apply.ensureDebugId("log_to_timesheet_button");
        apply.addClickHandler(clickEvent -> {
            if (validation()) {
                apply.setEnabled(false);
                if (timesheetDataItem.getOldMinutes() > 0) {
                    final WfmMessageBox message = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                    //message.setSize(300, 150);
                    message.setTitle(wfmStrings.warning());
                    message.setMessage(projectStrings.youAlreadyHaveTimesheethours());
                    message.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            save();
                        }

                        @Override
                        public void onCancel() {
                            apply.setEnabled(true);
                        }
                    });
                    message.open();
                } else {
                    save();
                }
            }
        });

        close = new WfmButton2(wfmStrings.close());

        close.addClickHandler(clickEvent -> close());

        aiSuggest = new WfmButton2(wfmStrings.aiHelper(), WfmButton2.BTN_SUCCESS);

        aiSuggest.addClickHandler(clickEvent -> {
            aiSuggest.setEnabled(false);
            aiSuggest.addStyleName("loading");
            fetchSuggestion(Utils.getUserID(),taskId);
        });

//        form.setWidth("100%");
        form.addStyleName("tmshtPopup-form");
        form.addField(wfmStrings.taskName() + ":", taskName);
        form.addField(wfmStrings.date() + " :", dateTimePicker, true);
        form.addField(wfmStrings.hoursSpent(), timeSpent/*taskHours*/, true);
        form.addField(null, area);

        dateTimePicker.getElement().getStyle().clearDisplay();
        area.getElement().getStyle().clearDisplay();

        setWidth("410px");
//        setHeight("450px");

        // Todo: Stanislav need to remove scrollbar
//        getScrollPanel().addStyleName("css-no-scroll"); // add css
        add(form);
        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_AI_TIMESHEET_COMMENT)){
            addButton(aiSuggest);
        }
        addButton(close);
        addButton(apply);
    }

    private void fetchSuggestion(Integer userId, Integer taskId) {
        timesheetService.getTimesheetCommentSuggestion(userId, taskId, new AbstractAsyncCallback<ArrayList<SuggestionResponseDTO>>() {
            @Override
            public void onFailure(Throwable caught) {
                resetButtonState(aiSuggest);
                Info.warn(wfmStrings.sorrySomethingWentWrong() + ": " + caught.getMessage());
            }

            @Override
            public void onSuccess(ArrayList<SuggestionResponseDTO> suggestions) {
                resetButtonState(aiSuggest);
                if (suggestions.isEmpty()) {
                    Info.warn(wfmStrings.suggestionNotAviable());
                } else {
                    showSuggestionPopup(suggestions);
                }
            }
        });
    }

    private void showSuggestionPopup(ArrayList<SuggestionResponseDTO> suggestions) {
        // Create the popup
        PopupPanel popup = new PopupPanel(true); // Auto-hide on click outside
        popup.setStyleName("aiHintPopup");
        popup.getElement().getStyle().setWidth(242, Style.Unit.PX);
//        popup.setHeight("450px");



        // Build the content
        Div content = new Div();

        for (SuggestionResponseDTO suggestion : suggestions) {
            Div row = new Div("aiHint__section");
            Span time = new Span(suggestion.getTime() != null ? Utils.formatMinutes(suggestion.getTime()) : "00:00");
            time.addStyleName("aiHint__time");
            Span description = new Span(suggestion.getDescription() != null ? suggestion.getDescription() : "");
            description.addStyleName("aiHint__desc");

            Div itemContent = new Div("aiHint");
            itemContent.addClickHandler(event -> {
                area.setText(suggestion.getDescription());
                if (suggestion.getTime() != null) {
                    timeSpent.setValueAsMinutes(suggestion.getTime());
                }
                popup.hide();
            });
            itemContent.add(time);
            itemContent.add(description);

            row.add(itemContent);

            content.add(row);
        }

        // Wrap content in a ScrollPanel
        Div scrollPanel = new Div("aiHintPopup__scroll");
//        scrollPanel.setWidth("280px");  // Slightly less than popup width to account for padding
//        scrollPanel.setHeight("180px"); // Fixed height to trigger scrolling
        scrollPanel.add(content);

        // Set the ScrollPanel as the popup's widget
        popup.setWidget(scrollPanel);

        // Position the popup
        int buttonLeft = getWrapper().getAbsoluteLeft();
        int buttonTop = getWrapper().getAbsoluteTop();
        int buttonWidth = getWrapper().getOffsetWidth();

        int popupLeft = buttonLeft + buttonWidth + 10;
        int popupTop = buttonTop;

        int windowWidth = Window.getClientWidth();
        int windowHeight = Window.getClientHeight();

        if (popupLeft + 300 > windowWidth) {
            popupLeft = buttonLeft - 300 - 10;
        }
        if (popupTop + 200 > windowHeight) {
            popupTop = windowHeight - 200 - 10;
        }
        if (popupTop < 0) popupTop = 0;
        if (popupLeft < 0) popupLeft = 0;

        popup.setPopupPosition(popupLeft, popupTop);
        popup.show();

        // Set the height of the aiHintPopup
        Scheduler.get().scheduleFinally(() -> {
            Scheduler.get().scheduleDeferred(() -> {
//                int popupHeight = popup.getElement().getOffsetHeight();
                int offsetHeight = getWrapper().getOffsetHeight();
                popup.getElement().getStyle().setHeight(offsetHeight, Style.Unit.PX);
            });
        });
    }

    private void resetButtonState(WfmButton2 button) {
        button.setEnabled(true);
        button.removeStyleName("loading");
    }

    private void save() {
        apply.setEnabled(false);
        int minutes = timeSpent.getValueAsMinutes();//Utils.parseMinutes(taskHours.getText());
        if (timesheetDataItem == null) {
            timesheetDataItem = new TimesheetDataItem();
        }
        timesheetDataItem.setComment(area.getText());
        timesheetDataItem.setMinutes(minutes);
        timesheetDataItem.setStatus(TIMESHEET_ENTRY_APPLYING_UPDATE);
        timesheetDataItem.setDate(dateTimePicker.getDate());
        timesheetDataItem.setTaskID(taskId);
//        LoadingPanel.loading(true);
        close();
        TaskService.App.get().setTimeToTimesheet(timesheetDataItem, new AbstractAsyncCallback<Boolean>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                close();
            }

            @Override
            public void success(Boolean result) {
                LoadingPanel.loading(false);
                if (result) {
                    close();
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.hours()), Info.Type.INFO);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TIME_LOGGED, result, TaskLogToTimeSheetPopup.this);
                    if (logTimeCmd != null) {
                        logTimeCmd.execute();
                    }
                }
            }
        });

    }

    private void getValidationDateData() {
        LoadingPanel.loading(true);
        Date selectedDate = dateTimePicker != null ? dateTimePicker.getDate() : new Date();
        DateNonConvertable nonConvertable = new DateNonConvertable(selectedDate);
        TaskService.App.get().getValidationData(taskId, nonConvertable, employeeId, new AbstractAsyncCallback<TimesheetDataItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(TimesheetDataItem result) {
                LoadingPanel.loading(false);
                timesheetDataItem = result;
                area.setText(timesheetDataItem.getOldComment());
            }
        });
    }

    private void getTimeSheetSettings() {
        ProfileService.App.get().getPMNumberingSettings(new AbstractAsyncCallback<PMNumberingSettings>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(PMNumberingSettings pmNumberingSettings) {
                isCommentMandatory = pmNumberingSettings.getTimesheetCommentRequired();
            }
        });
    }

    private boolean validation() {
        int errors = 0;
        int lastMinutes = 0;
        try {
            if (timeSpent.getValueAsMinutes() == 0) {
                errorPopup(wfmStrings.pleaseEnterValue());
                errors++;
                return false;
            }
            if (isCommentMandatory && area != null && area.getText().isEmpty()) {
                errorPopup(wfmStrings.pleaseFillInCommentBox());
                errors++;
                return false;
            }

            DateTimeFormat format = DateTimeFormat.getFormat(Utils.getShortDateFormat());
            if ("true".equals(Utils.userSettings.get(VALIDATE_TASK_START))) {
                if ((timesheetDataItem.getDate().getYear() == timesheetDataItem.getTaskStart().getYear() && timesheetDataItem.getDate().getMonth() == timesheetDataItem.getTaskStart().getMonth() && timesheetDataItem.getDate().getDate() < timesheetDataItem.getTaskStart().getDate()) ||
                        (timesheetDataItem.getDate().getYear() == timesheetDataItem.getTaskStart().getYear() && timesheetDataItem.getDate().getMonth() < timesheetDataItem.getTaskStart().getMonth()) ||
                        timesheetDataItem.getDate().getYear() < timesheetDataItem.getTaskStart().getYear()) {
                    errorPopup(wfmStrings.taskStartDateValidationMessage() + format.format(timesheetDataItem.getTaskStart()));
                    errors++;
                    return false;
                }
            }
            if ("true".equals(Utils.userSettings.get(VALIDATE_TASK_END))) {
                if ((timesheetDataItem.getDate().getYear() == timesheetDataItem.getTaskEnd().getYear() && timesheetDataItem.getDate().getMonth() == timesheetDataItem.getTaskEnd().getMonth() && timesheetDataItem.getDate().getDate() > timesheetDataItem.getTaskEnd().getDate()) ||
                        (timesheetDataItem.getDate().getYear() == timesheetDataItem.getTaskEnd().getYear() && timesheetDataItem.getDate().getMonth() > timesheetDataItem.getTaskEnd().getMonth()) ||
                        timesheetDataItem.getDate().getYear() > timesheetDataItem.getTaskEnd().getYear()) {
                    errorPopup(wfmStrings.taskEndDateValidationMessage() + format.format(timesheetDataItem.getTaskEnd()));
                    errors++;
                    return false;
                }
            }
            if ("true".equals(Utils.userSettings.get(VALIDATE_HOLIDAY)) && timesheetDataItem.isHoliday()) {
                errorPopup(wfmStrings.holidayValidationMessage());
                errors++;
                return false;
            }
            //check for the weekend, weekend is when the timeslot equals to ZERO
            if ("true".equals(Utils.userSettings.get(VALIDATE_DAY_OFF)) && timesheetDataItem.isDayOff()) {
                errorPopup(wfmStrings.dayOffValidationMessage());
                return false;
            }
            if ("true".equals(Utils.userSettings.get(VALIDATE_lEAVE_REQUEST)) && (timesheetDataItem.getLeaveRequestMinutes() > 0)) {
                if (timesheetDataItem.getTimeslotMinutes() - timesheetDataItem.getLeaveRequestMinutes() == 0) {
                    errorPopup(wfmStrings.dailyLeaveRequestValidationMessage());
                    errors++;
                    return false;
                }
                int maxMinutesAllowed = 0;
                if ("true".equals(Utils.userSettings.get(VALIDATE_MAXIMUM_HOURS)) && "false".equals(Utils.userSettings.get(VALIDATE_TIMESLOT))) {
                    if (Utils.userSettings.get(MAXIMUM_HOURS) != null && !Utils.userSettings.get(MAXIMUM_HOURS).equals("")) {
                        maxMinutesAllowed = Integer.valueOf(Utils.userSettings.get(MAXIMUM_HOURS)) * 60;
                    }
                } else {
                    maxMinutesAllowed = timesheetDataItem.getTimeslotMinutes();
                }

                if (maxMinutesAllowed - timesheetDataItem.getLeaveRequestMinutes() - (timesheetDataItem.getTimesheetMinutes() - timesheetDataItem.getOldMinutes()) < lastMinutes) {
                    errorPopup(wfmStrings.hourlyLeaveRequestValidationMessage() + Utils.formatMinutes(maxMinutesAllowed - timesheetDataItem.getLeaveRequestMinutes()) + wfmStrings.hours());
                    errors++;
                    return false;
                }
            }
            if ("true".equals(Utils.userSettings.get(VALIDATE_MAXIMUM_HOURS))) {
                if ("true".equals(Utils.userSettings.get(VALIDATE_TIMESLOT))) {
                    if ((timesheetDataItem.getTimeslotMinutes() - (timesheetDataItem.getTimesheetMinutes() - timesheetDataItem.getOldMinutes()) < lastMinutes)) {
                        errorPopup(wfmStrings.timeslotValidationMessage() + Utils.formatMinutes(timesheetDataItem.getTimeslotMinutes()) + wfmStrings.hours());
                        errors++;
                        return false;
                    }
                } else {
                    int maxHoursAllowed = 0;
                    if (Utils.userSettings.get(MAXIMUM_HOURS) != null && !Utils.userSettings.get(MAXIMUM_HOURS).equals("")) {
                        maxHoursAllowed = Integer.valueOf(Utils.userSettings.get(MAXIMUM_HOURS));
                    }
                    if (((maxHoursAllowed * 60) - (timesheetDataItem.getTimesheetMinutes() - timesheetDataItem.getOldMinutes()) < lastMinutes)) {
                        errorPopup(wfmStrings.timeslotValidationMessage() + maxHoursAllowed + wfmStrings.hours());
                        errors++;
                        return false;
                    }
                }
            }
            if ("true".equals(Utils.userSettings.get(VALIDATE_PAST_TIMSHEET))) {
                int pastDays = Integer.parseInt(Utils.userSettings.get(PAST_TIMSHEET_DAYS));
                if (timesheetDataItem.getDate().getTime() < dateTimePicker.getDate().getTime()) {
                    if (pastDays < Math.floor((dateTimePicker.getDate().getTime() - timesheetDataItem.getDate().getTime()) / (double) (1000 * 60 * 60 * 24))) {
                        errorPopup(coreMessages.validationPast(Integer.toString(pastDays)));
                        errors++;
                        return false;
                    }
                }
            }

            if ("true".equals(Utils.userSettings.get(VALIDATE_FUTURE_TIMESHEET))) {
                Integer futureDays = Integer.valueOf(Utils.userSettings.get(FUTURE_TIMESHEET_DAYS));
                if (timesheetDataItem.getDate().getTime() > dateTimePicker.getDate().getTime()) {
                    if (futureDays < Math.ceil(((timesheetDataItem.getDate().getTime() - dateTimePicker.getDate().getTime()) / (double) (1000 * 60 * 60 * 24)))) {
                        Info.show(coreMessages.validationFuture(futureDays.toString()), Info.Type.WARNING);
                        errors++;
                        return false;
                    }
                }
            }

            if (timesheetDataItem.getTaskTransfer().getEstimatedTime() != null && timesheetDataItem.getTaskTransfer().getEstimatedTime() > 0 && Utils.timesheetEstimateExceedsValidation()) {
                if (lastMinutes - timesheetDataItem.getOldMinutes() + timesheetDataItem.getTaskTransfer().getTotalMinutes() > timesheetDataItem.getTaskTransfer().getEstimatedTime()) {
                    errorPopup(projectStrings.estTimeValidation());
                    errors++;
                    return false;
                }
            }
            if (!"true".equals(Utils.userSettings.get(ISAUTOMATICAPPROVAL)) && timesheetDataItem.getStatus() == TIMESHEET_ENTRY_APPROVED) {
                errorPopup(projectStrings.youAlreadyHaveApprovedHours());
                errors++;
                return false;
            } else if (!"true".equals(Utils.userSettings.get(ISAUTOMATICWAITINGFORAPPROVAL)) && timesheetDataItem.getStatus() == TIMESHEET_ENTRY_WAITING) {
                errorPopup(projectStrings.youAlreadyHaveSubmittedHours());
                errors++;
                return false;
            }

        } catch (NumberFormatException | StringIndexOutOfBoundsException exc) {
            errorPopup(wfmStrings.timeFormats());
            errors++;
            return false;
        }
        if (dateTimePicker.getDate() == null) {
            errorPopup(wfmStrings.pleaseEnterValue());
            errors++;
        }
        return errors <= 0;
    }

    private void errorPopup(String errorMessage) {
        Info.show(errorMessage, Info.Type.WARNING);
    }

    public void setLogTimeCmd(Command cmd) {
        this.logTimeCmd = cmd;
    }

    public DatePicker getDateTimePicker() {
        return dateTimePicker;
    }
}
