package com.edatasite.workforce.gwt.timesheet.client.ui;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.DropDownCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.timesheet.client.rpc.SuggestionResponseDTO;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetDataItem;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetService;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetServiceAsync;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetSettings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;

public class TimesheetEditPanel extends Composite implements HasValue<TimesheetDataItem>, Constants {
    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final TimesheetServiceAsync timesheetService = TimesheetService.App.get();

    private TimesheetSettings timesheetSettings;
    private TimesheetDataItem dataItem;

    private TextArea2 commentText = new TextArea2(1000);
    private final MaterialPanel mainPanel = new MaterialPanel("timesheetEditCellPopup__cover");
    private final Label errorMessage = new Label();
    private TextBox textBox = new TextBox();
    private PopupPanel popup;
    private final DropDownCellEditor hourTypes;
    private int itemMinutes;
    private int oldMinutes;
    private Command closeCommand;
    private final WfmButton2 aiSuggest;

    public TimesheetEditPanel() {
        textBox = new TextBox();
        textBox.getElement().setAttribute("placeholder", "00:00 " + wfmStrings.pleaseSetTime());
        textBox.addKeyDownHandler(event -> {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                apply();
            }
        });
        textBox.addStyleName("timesheetEditCellPopup__time");
        mainPanel.add(textBox);

        commentText = new TextArea2(1000);
        commentText.setPlaceHolder(wfmStrings.comment());
        commentText.addStyleName("timesheetEditCellPopup__note");
        commentText.setHeight(135);
        mainPanel.add(commentText);
        hourTypes = new DropDownCellEditor(null, false) {
            @Override
            protected Object getValue() {
                return getListBox().getSelectedItem();
            }

            @Override
            protected void setValue(Object cellValue) {
                getListBox().setItems(new SelectItem[0]);

            }
        };
        hourTypes.getListBox().addKeyDownHandler(event -> {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                apply();
            }
        });
        hourTypes.addStyleName("timesheetEditCellPopup__hourType");
        if ("true".equals(Utils.userSettings.get(SHOW_HOUR_TYPE_DROPDOWN))) {
            mainPanel.add(hourTypes);
        }
        if (Utils.hasGenericAccess(GenericSettingsEnum.TIMESHEET_EXPENSE_ENABLED)) {
            Anchor addExpenseLink = new Anchor(wfmStrings.addMess());
            addExpenseLink.addClickHandler(event -> {
                String url = GWT.getHostPageBaseURL() + "Accounting.html#expenseReports|add/add/TIMESHEET/"
                        + dataItem.getProjectID() + "/" + DateTimeFormat.getFormat("dd_MM_yyyy").format(dataItem.getDate());
                Window.open(url, "_blank", "");
            });
            mainPanel.add(addExpenseLink);
        }

        MaterialPanel buttonGroup = new MaterialPanel("accept-cancel-group btns-group");
        WfmButton2 save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        save.addClickHandler(event -> {
            apply();
        });

        aiSuggest = new WfmButton2(wfmStrings.aiHelper(), WfmButton2.BTN_SUCCESS);
        aiSuggest.setVisible(false);
        aiSuggest.addClickHandler(event -> {
            aiSuggest.setEnabled(false);
            aiSuggest.addStyleName("loading");
            fetchSuggestion(Utils.getUserID(), dataItem.getTaskID());
        });

        WfmButton2 cancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);
        cancel.addClickHandler(event -> {
            if (closeCommand != null) {
                closeCommand.execute();
            }
        });

        buttonGroup.add(save);
        buttonGroup.add(cancel);
        buttonGroup.add(aiSuggest);
        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_AI_TIMESHEET_COMMENT)) {
            aiSuggest.setVisible(true);
        }else {
            mainPanel.setWidth("270px");
        }

        errorMessage.addStyleName("timesheetEditCellPopup__errorMessage");
        mainPanel.add(errorMessage);
        mainPanel.add(buttonGroup);

        initWidget(mainPanel);
    }
    private void fetchSuggestion(Integer userId, Integer taskId) {
        timesheetService.getTimesheetCommentSuggestion(userId, taskId, new AbstractAsyncCallback<ArrayList<SuggestionResponseDTO>>() {
            @Override
            public void onFailure(Throwable caught) {
                resetButtonState(aiSuggest);
            }

            @Override
            public void onSuccess(ArrayList<SuggestionResponseDTO> suggestions) {
                resetButtonState(aiSuggest);
                if (suggestions.isEmpty()) {
                    Info.show(wfmStrings.suggestionNotAviable());
                } else {
                    showSuggestionPopup(suggestions);
                }
            }
        });
    }

    private void showSuggestionPopup(ArrayList<SuggestionResponseDTO> suggestions) {
        // Create the popup
        popup = new PopupPanel(true);
        popup.setStyleName("aiHintPopup");
        popup.getElement().getStyle().setWidth(242, Style.Unit.PX);
        popup.getElement().getStyle().setHeight(mainPanel.getOffsetHeight() + 30, Style.Unit.PX);
        popup.getElement().getStyle().setMarginTop(-15, Style.Unit.PX);


        // Build the content
        Div content = new Div();

        for (SuggestionResponseDTO suggestion : suggestions) {
            Div row = new Div();
            row.setStyleName("aiHint__section");

            Span time = new Span(suggestion.getTime() != null ? Utils.formatMinutes(suggestion.getTime()) : "00:00");
            time.addStyleName("aiHint__time");
            Span description = new Span(suggestion.getDescription() != null ? suggestion.getDescription() : "Could not find a description");
            description.addStyleName("aiHint__desc");

            Div itemContent = new Div("aiHint");
            itemContent.addClickHandler(event -> {
                commentText.setText(suggestion.getDescription());
                if (suggestion.getTime() != null) {
                    textBox.setText(Utils.formatMinutes(suggestion.getTime()));
                }
                popup.hide();
            });
            itemContent.add(time);
            itemContent.add(description);
            itemContent.add(description);

            row.add(itemContent);
            content.add(row);
        }

        // Wrap content in a ScrollPanel
        Div scrollPanel = new Div("aiHintPopup__scroll");
        scrollPanel.add(content);
//        scrollPanel.setWidth("280px");  // Slightly less than popup width to account for padding
//        scrollPanel.setHeight("100px"); // Fixed height to trigger scrolling

        // Set the ScrollPanel as the popup's widget
        popup.setWidget(scrollPanel);

        // Position the popup
        int modalLeft = mainPanel.getAbsoluteLeft();
        int modalTop = mainPanel.getAbsoluteTop();
        int modalWidth = mainPanel.getOffsetWidth();

        int popupLeft = modalLeft + modalWidth + 25;
        int popupTop = modalTop;

        int windowWidth = Window.getClientWidth();
        int windowHeight = Window.getClientHeight();

        if (popupLeft + 300 > windowWidth) {
            popupLeft = modalLeft - 300 - 25;
        }
        if (popupTop + 200 > windowHeight) {
            popupTop = windowHeight - 200 - 25;
        }
        if (popupTop < 0) popupTop = 0;
        if (popupLeft < 0) popupLeft = 0;

        popup.setPopupPosition(popupLeft, popupTop);
        popup.show();
    }

    private void resetButtonState(WfmButton2 button) {
        button.setEnabled(true);
        button.removeStyleName("loading");
    }

    private boolean apply() {
        itemMinutes = 0;
        if (!validate()) {
            errorMessage.setText(wfmStrings.pleaseFillInCommentBox());
            commentText.getTextArea().addStyleName(Constants.ERROR_FORM_STYLE);
            return false;
        }
        try {
            if (!textBox.getText().equals("")) {
                int lastMinutes = Utils.parseMinutes(textBox.getText());
                if (lastMinutes < 0 || !Utils.correctFormat) {
                    errorPopup(errorMessage);
                    return false;
                }
                DateTimeFormat format = DateTimeFormat.getFormat(Utils.getShortDateFormat());
                if ("true".equals(Utils.userSettings.get(VALIDATE_TASK_START))) {
                    if ((dataItem.getDate().getYear() == dataItem.getTaskStart().getYear() && dataItem.getDate().getMonth() == dataItem.getTaskStart().getMonth() && dataItem.getDate().getDate() < dataItem.getTaskStart().getDate()) ||
                            (dataItem.getDate().getYear() == dataItem.getTaskStart().getYear() && dataItem.getDate().getMonth() < dataItem.getTaskStart().getMonth()) ||
                            dataItem.getDate().getYear() < dataItem.getTaskStart().getYear()) {
                        Info.get().show(wfmStrings.taskStartDateValidationMessage() + format.format(dataItem.getTaskStart()), Info.Type.WARNING);
                        return false;
                    }
                }
                if ("true".equals(Utils.userSettings.get(VALIDATE_TASK_END))) {
                    if ((dataItem.getDate().getYear() == dataItem.getTaskEnd().getYear() && dataItem.getDate().getMonth() == dataItem.getTaskEnd().getMonth() && dataItem.getDate().getDate() > dataItem.getTaskEnd().getDate()) ||
                            (dataItem.getDate().getYear() == dataItem.getTaskEnd().getYear() && dataItem.getDate().getMonth() > dataItem.getTaskEnd().getMonth()) ||
                            dataItem.getDate().getYear() > dataItem.getTaskEnd().getYear()) {
                        Info.get().show(wfmStrings.taskEndDateValidationMessage() + format.format(dataItem.getTaskEnd()), Info.Type.WARNING);
                        return false;
                    }
                }
                if ("true".equals(Utils.userSettings.get(VALIDATE_HOLIDAY)) && dataItem.isHoliday()) {
                    Info.get().show(wfmStrings.holidayValidationMessage(), Info.Type.WARNING);
                    return false;
                }
                //check for the weekend, weekend is when the timeslot equals to ZERO
                if ("true".equals(Utils.userSettings.get(VALIDATE_DAY_OFF)) && dataItem.isDayOff()) {
                    Info.get().show(wfmStrings.dayOffValidationMessage(), Info.Type.WARNING);
                    return false;
                }
                if ("true".equals(Utils.userSettings.get(VALIDATE_lEAVE_REQUEST)) && (dataItem.getLeaveRequestMinutes() > 0)) {
                    if (dataItem.getTimeslotMinutes() - dataItem.getLeaveRequestMinutes() == 0) {
                        Info.get().show(wfmStrings.dailyLeaveRequestValidationMessage(), Info.Type.WARNING);
                        return false;
                    }
                    int maxMinutesAllowed = 0;
                    if ("true".equals(Utils.userSettings.get(VALIDATE_MAXIMUM_HOURS)) && "false".equals(Utils.userSettings.get(VALIDATE_TIMESLOT))) {
                        if (Utils.userSettings.get(MAXIMUM_HOURS) != null && !Utils.userSettings.get(MAXIMUM_HOURS).equals("")) {
                            maxMinutesAllowed = Integer.valueOf(Utils.userSettings.get(MAXIMUM_HOURS)) * 60;
                        }
                    } else {
                        maxMinutesAllowed = dataItem.getTimeslotMinutes();
                    }

                    if (maxMinutesAllowed - dataItem.getLeaveRequestMinutes() - (dataItem.getTimesheetMinutes() - oldMinutes) < lastMinutes) {
                        Info.get().show(wfmStrings.hourlyLeaveRequestValidationMessage() + Utils.formatMinutes(maxMinutesAllowed - dataItem.getLeaveRequestMinutes()) + wfmStrings.hours(), Info.Type.WARNING);
                        return false;
                    }
                }
                if ("true".equals(Utils.userSettings.get(VALIDATE_MAXIMUM_HOURS))) {
                    if ("true".equals(Utils.userSettings.get(VALIDATE_TIMESLOT))) {
                        if ((dataItem.getTimeslotMinutes() - (dataItem.getTimesheetMinutes() - oldMinutes) < lastMinutes)) {
                            Info.get().show(wfmStrings.timeslotValidationMessage() + Utils.formatMinutes(dataItem.getTimeslotMinutes()) + wfmStrings.hours(), Info.Type.WARNING);
                            return false;
                        }
                    } else {
                        int maxHoursAllowed = 0;
                        if (Utils.userSettings.get(MAXIMUM_HOURS) != null && !Utils.userSettings.get(MAXIMUM_HOURS).equals("")) {
                            maxHoursAllowed = Integer.valueOf(Utils.userSettings.get(MAXIMUM_HOURS));
                        }
                        if (((maxHoursAllowed * 60) - (dataItem.getTimesheetMinutes() - oldMinutes) < lastMinutes)) {
                            Info.get().show(wfmStrings.timeslotValidationMessage() + maxHoursAllowed + wfmStrings.hours(), Info.Type.WARNING);
                            return false;
                        }
                    }
                }

                if (dataItem.getTaskTransfer().getEstimatedTime() > 0 && Utils.timesheetEstimateExceedsValidation()) {//validate more than estimate
                    if (lastMinutes - oldMinutes + dataItem.getTaskTransfer().getTotalMinutes() > dataItem.getTaskTransfer().getEstimatedTime()) {
                        Info.get().show(projectStrings.estTimeValidation(), Info.Type.WARNING);
                        return false;
                    }
                }


                itemMinutes = lastMinutes - oldMinutes;
                dataItem.setComment(commentText.getText());
                dataItem.setHourTypeID(0);
                if (hourTypes.getListBox() != null && hourTypes.getListBox().getSelectedItem() != null && hourTypes.getListBox().getSelectedItem().getId() != null) {
                    dataItem.setHourTypeID(hourTypes.getListBox().getSelectedItem().getId());
                }
                //dataItem.setHourTypeID(hourTypes.getListBox() == null ? 0 : hourTypes.getListBox().getSelectedItem().getId());
                dataItem.setMinutes(lastMinutes);
                dataItem.setDifference(itemMinutes);
                dataItem.setTimesheetMinutes(dataItem.getTimesheetMinutes() + itemMinutes);
                dataItem.setStatus(TIMESHEET_ENTRY_APPLYING_UPDATE);
                setValue(dataItem, true);
            }
        } catch (NumberFormatException | StringIndexOutOfBoundsException exc) {
            errorPopup(errorMessage);
            return false;
        }

        return true;
    }

    private boolean validate() {
        if (timesheetSettings.isTimesheetCommentRequired()) {
            if (commentText.getText() == null || commentText.getText().isEmpty()) {
                return false;
            }
            return !commentText.getText().trim().isEmpty();
        }
        return true;
    }

    private void errorPopup(Label errorMessage) {
        errorMessage.setText(wfmStrings.timeFormats());
    }

    @Override
    public TimesheetDataItem getValue() {
        return dataItem;
    }

    public void setValue(int column, TimesheetDataItem value) {
        setValue(value, false);
    }

    @Override
    public void setValue(TimesheetDataItem value) {
        setValue(value, false);
    }

    @Override
    public void setValue(TimesheetDataItem value, boolean fireEvents) {
        this.dataItem = value;
        errorMessage.setText("");
        commentText.setText(value.getComment() != null ? value.getComment() : "");
        if ("true".equals(Utils.userSettings.get(SHOW_HOUR_TYPE_DROPDOWN))) {
            hourTypes.setItems(value.getHourTypes());
            if (value.getHourTypeID() == 0) {
                hourTypes.getListBox().setSelectedNullLabel();
            } else {
                hourTypes.getListBox().setSelected(value.getHourTypeID());
            }
        }

        oldMinutes = value.getMinutes();
        if (value.getMinutes() != 0) {
            textBox.setText(Utils.formatMinutes(value.getMinutes()));
        } else {
            textBox.setText("");
        }
        if (fireEvents) {
//            TaskDatabase.get().setTotal(itemMinutes);
            ValueChangeEvent.fire(this, value);
        }
        textBox.setFocus(true);
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<TimesheetDataItem> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    public TimesheetSettings getTimesheetSettings() {
        return timesheetSettings;
    }

    public void setTimesheetSettings(TimesheetSettings timesheetSettings) {
        this.timesheetSettings = timesheetSettings;
    }

    public void setFocused(boolean b) {
        Utils.setFocus(textBox.getElement(), true);
    }

    public void setCloseHandler(Command closeHandler) {
        this.closeCommand = closeHandler;
    }
}