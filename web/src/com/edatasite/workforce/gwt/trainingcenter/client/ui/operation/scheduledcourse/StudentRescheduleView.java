package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.scheduledcourse;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.WfmForm;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse.ScheduledCourseItem;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Created with IntelliJ IDEA.
 * User: Normurod
 * Date: 9/11/12
 * Time: 1:44 PM
 * To change this template use File | Settings | File Templates.
 */
public class StudentRescheduleView implements Constants {
    private static final TCStrings tcStrings = TCStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private WfmForm table;
    private WfmForm.Field scheduleDateField;

    private DataListBox dwSchedules;

    private WfmButton2 btnSave;
    private WfmButton2 btnCancel;

    private final DialogBox dialogBox;

    private VerticalPanel pnlContainer;

    private final Integer objectID; //scheduled course ID
    private final Integer studentID;

    public StudentRescheduleView(Integer objectID, Integer studentID) {
        this.objectID = objectID;
        this.studentID = studentID;

        dialogBox = new DialogBox();
        dialogBox.setText(tcStrings.studentReschedule());
        dialogBox.setStyleName("gwt-DialogBox workforce");

        initialize();
    }

    private void initialize() {
        pnlContainer = new VerticalPanel();
        pnlContainer.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        pnlContainer.setVerticalAlignment(HasAlignment.ALIGN_MIDDLE);
        pnlContainer.addStyleName("inner");
        pnlContainer.setSpacing(15);
        pnlContainer.setSize("100%", "80px");

        table = new WfmForm();

        dwSchedules = new DataListBox();
        dwSchedules.addStyleName(DEFAULT_WIDTH);

        scheduleDateField = table.addField(tcStrings.scheduledDates(), dwSchedules, true);
        pnlContainer.add(table);

        btnSave = new WfmButton2(wfmStrings.saveAndClose());
        btnSave.addClickHandler(event -> save());
        btnCancel = new WfmButton2(wfmStrings.close());
        btnCancel.addClickHandler(event -> dialogBox.hide());
        HorizontalPanel btnPanel = new HorizontalPanel();
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);
        pnlContainer.add(btnPanel);

        dialogBox.add(pnlContainer);
        dialogBox.show();

        loadData();
    }

    private void loadData() {
        LoadingPanel.loading(true);
        TCService.App.get().getAvailableScheduleCourseDates(objectID, new AsyncCallback<ScheduledCourseItem[]>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                caught.printStackTrace();
            }

            @Override
            public void onSuccess(ScheduledCourseItem[] result) {
                LoadingPanel.loading(false);
                dwSchedules.clear();
                if (result != null && result.length > 0) {
                   SelectItem[] schedules = new SelectItem[result.length];
                    for (int i = 0; i < result.length; i++) {
                        schedules[i] = new SelectItem();
                        schedules[i].setId(result[i].getObjectID());
                        schedules[i].setName(DateUtils.formatInternalShort(result[i].getStartDate()) + " (" + result[i].getLanguageName() + ")");
                    }

                    dwSchedules.setItems(schedules);
                }
            }
        });
    }

    private void save() {
        if (!validation()) {
            return;
        }

        LoadingPanel.loading(true);
        TCService.App.get().studentReschedule(studentID, objectID, dwSchedules.getSelectedId(), new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                caught.printStackTrace();
            }

            @Override
            public void onSuccess(Boolean result) {
                LoadingPanel.loading(false);
                if (result == null || !result) {
                    Info.show(tcStrings.studentRescheduleWarningInfo(), Info.Type.WARNING);
                } else {
                    dialogBox.hide();
                    Info.show(tcStrings.studentRescheduleInfo(), Info.Type.INFO);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_STUDENT_DELETE, result, dialogBox);
                }
            }
        });
    }

    private boolean validation() {
        int errors = 0;

        if (!Validation.validateListBoxRequired(dwSchedules, scheduleDateField, "This field is required.")) {
            errors++;
        }

        return errors == 0;
    }
}
