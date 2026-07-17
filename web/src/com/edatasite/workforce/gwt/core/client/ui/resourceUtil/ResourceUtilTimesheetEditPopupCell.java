package com.edatasite.workforce.gwt.core.client.ui.resourceUtil;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.resourceUtil.TaskItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetDataItem;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetService;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetServiceAsync;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Paragraph;

/**
 * Created by Farrukh on 19-Aug-17.
 */
public class ResourceUtilTimesheetEditPopupCell extends PopupPanel implements Constants {

    private final TimesheetServiceAsync timesheetService = TimesheetService.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final ProjectStrings projectStrings = ProjectStrings.App.get();

    private TimesheetDataItem dataItem;
    private ChangeDailyTimesheetListener changeDailyTimesheetListener;
    private final TaskItem taskItem;
    private final DateNonConvertable selectedDate;
    private final Element selectedElement;
    private final int oldMinutes;
    private int itemMinutes;
    private final Integer employeeid;

    private final MaterialPanel mainPanel = new MaterialPanel("timesheetEditCellPopup__cover");
    private TextArea2 commentText = new TextArea2(1000);
    private final Label errorMessage = new Label();
    private TextBox textBox = new TextBox();
    private DataListBox hourTypes;
    private Command closeCommand;

    public interface ChangeDailyTimesheetListener {
        void dailyTimesheetListener(int changedTimesheetTime);
    }

    public ResourceUtilTimesheetEditPopupCell(TaskItem taskItem, DateNonConvertable dateNonConvertable, Element selectedElement, int oldMinutes, Integer employeeid) {
        super(true, true);
        this.taskItem = taskItem;
        this.selectedDate = dateNonConvertable;
        this.selectedElement = selectedElement;
        this.oldMinutes = oldMinutes;
        this.employeeid = employeeid;
        getTimesheetComment();
        drawInitialize();
    }

    private void getTimesheetComment() {
        TimesheetService.App.get().getTimesheetComment(taskItem.getTask_id(), employeeid, selectedDate, new AbstractAsyncCallback<String>() {
            public void failure() {

            }
            public void success(final String comment) {
                commentText.setText(comment);
            }
        });
    }

    private void drawInitialize() {
        errorMessage.setText("");

        textBox = new TextBox();
        textBox.getElement().setAttribute("placeholder", "00:00 " + wfmStrings.pleaseSetTime());
        if (oldMinutes != 0) {
            textBox.setText(Utils.formatMinutes(oldMinutes));
        } else {
            textBox.setText("");
        }
        textBox.setFocus(true);
        mainPanel.add(new Paragraph(textBox));

        commentText = new TextArea2(1000);
        commentText.setPlaceHolder(wfmStrings.comment());
        mainPanel.add(commentText);

        hourTypes = new DataListBox();
        hourTypes.setAllowFirstItem(true);
        hourTypes.addKeyDownHandler(event -> {
            if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
                apply();
            }
        });
        if ("true".equals(Utils.userSettings.get(SHOW_HOUR_TYPE_DROPDOWN))) {
            mainPanel.add(hourTypes);
        }

        MaterialPanel buttonGroup = new MaterialPanel("accept-cancel-group");
        MaterialPanel save = new MaterialPanel("accept");
        save.addClickHandler(event -> apply());

        MaterialPanel cancel = new MaterialPanel("cancel");
        cancel.addClickHandler(event -> {
            if (closeCommand != null) {
                closeCommand.execute();
            }
        });
        buttonGroup.add(save);
        buttonGroup.add(cancel);

        errorMessage.addStyleName("timesheetEditCellPopup__errorMessage");
        mainPanel.add(new Paragraph(errorMessage));
        mainPanel.add(buttonGroup);

        add(mainPanel);

        show();
    }

    private boolean apply() {
        itemMinutes = 0;
        try {
            if (!textBox.getText().equals("")) {
                int lastMinutes = Utils.parseMinutes(textBox.getText());
                if (lastMinutes < 0 || !Utils.correctFormat) {
                    errorPopup(errorMessage);
                    return false;
                }
                itemMinutes = lastMinutes - oldMinutes;
                dataItem = new TimesheetDataItem();
                dataItem.setEmployeeID(employeeid);
                dataItem.setTaskID(taskItem.getTask_id());
                dataItem.setDateNonConvertable(selectedDate);
                if (lastMinutes == 0) {
                    dataItem.setComment("");
                    commentText.setText("");
                } else {
                    dataItem.setComment(commentText.getText());
                }
                dataItem.setHourTypeID(hourTypes.getSelectedItem() == null ? 0 : hourTypes.getSelectedItem().getId());
                dataItem.setDifference(itemMinutes);
                dataItem.setMinutes(lastMinutes);
                dataItem.setStatus(TIMESHEET_ENTRY_APPLYING_UPDATE);

                timesheetService.applyUpdates(dataItem, new AbstractAsyncCallback<Integer>() {
                    @Override
                    public void failure(Throwable throwable) {
                        errorPopup(errorMessage);
                    }

                    @Override
                    public void success(Integer result) {
                        hide();
                    }

                });
                //returns timesheet hour
                if (changeDailyTimesheetListener != null) {
                    if (oldMinutes != lastMinutes) {
                        changeDailyTimesheetListener.dailyTimesheetListener(lastMinutes);
                    }
                }
            }
        }  catch (NumberFormatException | StringIndexOutOfBoundsException exc) {
            errorPopup(errorMessage);
            return false;
        }

        return true;
    }

    public void show() {
        setPopupPosition(selectedElement.getAbsoluteLeft() - 10, selectedElement.getAbsoluteTop());
        super.show();
    }

    public void setChangeDailyTimesheetListener(ChangeDailyTimesheetListener changeDailyTimesheetListener) {
        this.changeDailyTimesheetListener = changeDailyTimesheetListener;
    }

    private void errorPopup(Label errorMessage) {
        errorMessage.setText(wfmStrings.timeFormats());
    }

    @Override
    protected void onPreviewNativeEvent(Event.NativePreviewEvent event) {
        super.onPreviewNativeEvent(event);
        if (Event.ONKEYUP == event.getTypeInt()) {
            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
                hide();
            }
            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                apply();
            }
        }
    }

    public void setCloseCommand(Command closeCommand) {
        this.closeCommand = closeCommand;
    }
}
