package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation.scheduledcourse;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateTimePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.client.ui.StartEndTime;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmInputBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.view.ReminderView;
import com.edatasite.workforce.gwt.trainingcenter.client.TCConstants;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.CourseItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse.ScheduledCourseItem;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.scheduledcourse.TimeSlotItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * User: Normurod
 * Date: 7/21/12
 * Time: 2:18 PM
 */
public class CourseScheduledView extends CustomForm2 implements Constants, TCConstants, Colapse {

    private static final TCStrings tcStrings = TCStrings.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    final DateTimeFormat timeFormat = DateUtils.getTimeFormatInternal()/*DateTimeFormat.getFormat("HH:mm")*/;

    private DataListBox dwCourse;
    private DataListBox dwLanguage;
    private KpiCheckBox cbEnableOvertime;
    private DateTimePicker dateTime;
    private DataListBox dwLocation;
    private DataListBox dwInstructor;
    private DataListBox dwAssessor;
    private TextBox txtNumberOfSeats;
    private TextArea2 txtDuration;
    private LinkedHashMap<String, FormProperty> formPropertyMap;

    private FlowPanel pnlCourseRequirementContainer;
    private CourseRequirements courseRequirements;

    private Integer objectID;
    private ScheduledCourseItem scheduledCourseItem;
    private Integer weekStart;
    private ReminderView reminderView;
    private KpiCheckBox enableEmailReminder;
    private VerticalPanel recurringPanel;

    public CourseScheduledView(String name, String description) {
        super(name, description);
    }

    public CourseScheduledView(Integer objectID) {
        super("edit", tcStrings.editScheduledCourse());
        this.objectID = objectID;
    }

    FormHasCustomField customFieldUtil;

    @Override
    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.ScheduledCourse,getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
                CourseScheduledView.super.onInitialize();
            }
        });
        return null;
    }

    @Override
    protected void registerFields() {
        drawForm();
    }

    private void drawForm() {
        weekStart = Integer.valueOf(Utils.userSettings.get(OVERALL_DATE_PICKER_WEEK_START)) - 1;
        String scheduledCourse_add_edit_view = "scheduledcourse_add_edit_view_";

        dwLocation = new DataListBox();
        dwLocation.addStyleName(DEFAULT_WIDTH);
        dwLocation.ensureDebugId(scheduledCourse_add_edit_view + "location");
        dwLocation.addValueChangeHandler(event -> {
            if (dwLocation.getSelectedId() != null && dwLocation.getSelectedId() > 0) {
                LoadingPanel.loading(true);
                TCService.App.get().getTimeSlotItem(dwLocation.getSelectedId(), new AsyncCallback<HashMap<Integer, TimeSlotItem>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        LoadingPanel.loading(false);
                        caught.printStackTrace();
                    }

                    @Override
                    public void onSuccess(HashMap<Integer, TimeSlotItem> result) {
                        LoadingPanel.loading(false);
                        scheduledCourseItem.setTimeSlotItems(result);
                        setDefaultStartTime();

                        checkAvailability();
                    }
                });
            }
        });

        recurringPanel = new VerticalPanel();
        enableEmailReminder = new KpiCheckBox();
        enableEmailReminder.ensureDebugId("Schedule_recurring_checkbox");
        recurringPanel.add(enableEmailReminder);
        enableEmailReminder.addValueChangeHandler(booleanValueChangeEvent -> {
            if (enableEmailReminder.getValue()) {
                reminderView = new ReminderView(SchedulerConstant.RECURRING_COURSE_SCHEDULE);
                reminderView.setStyleName("reccurence-view");
                reminderView.getElement().getStyle().setLeft(-87, com.google.gwt.dom.client.Style.Unit.PX);
                reminderView.getElement().getStyle().setTop(10, com.google.gwt.dom.client.Style.Unit.PX);

                reminderView.drawForm(null);
                reminderView.setStartDate(dateTime.getStartDate());
                reminderView.setStart(dateTime.getStartDate());
                reminderView.hideNeverRadioButton();
                recurringPanel.add(reminderView);
                recurringPanel.addStyleName("recurringPanel");
            } else {
                recurringPanel.remove(reminderView);
                reminderView = null;
            }
        });

        dwCourse = new DataListBox();
        dwCourse.addStyleName(DEFAULT_WIDTH);
        dwCourse.ensureDebugId(scheduledCourse_add_edit_view + "course");
        dwCourse.addValueChangeHandler(event -> {
            courseChangeEvent();
        });

        dwLanguage = new DataListBox();
        dwLanguage.addStyleName(DEFAULT_WIDTH);
        dwLanguage.ensureDebugId(scheduledCourse_add_edit_view + "language");
        dwLanguage.addValueChangeHandler(event -> langaugeChangeEvent());

        cbEnableOvertime = new KpiCheckBox();
        cbEnableOvertime.addValueChangeHandler(valueChangeEvent -> {
            if (isAvailableDateForSchedule(dateTime.getStartDate())) {
                checkAvailability();
            } else {
                setDefaultStartTime();
                Info.show(tcStrings.availableDateForSchedule(), Info.Type.WARNING);
            }
        });

        dateTime = new DateTimePicker();
        dateTime.setAllDay(false);
        dateTime.getStartTime().setVisible(true);
        dateTime.getStartDatePicker().addValueChangeHandler(dateValueChangeEvent -> {
            if (isAvailableDateForSchedule(dateTime.getStartDate())) {
                checkAvailability();
            } else {
                setDefaultStartTime();
                Info.show(tcStrings.availableDateForSchedule(), Info.Type.WARNING);
            }
        });
        dateTime.startTime.getListBox().addChangeHandler(event -> {
            if (isAvailableDateForSchedule(dateTime.getStartDate())) {
                checkAvailability();
            } else {
                setDefaultStartTime();
                Info.show(tcStrings.availableDateForSchedule(), Info.Type.WARNING);
            }
        });

        dateTime.startDate.ensureDebugId(scheduledCourse_add_edit_view + "_startDate");
        dateTime.startTime.ensureDebugId(scheduledCourse_add_edit_view + "_startTime");

        dwInstructor = new DataListBox();
        dwInstructor.addStyleName(DEFAULT_WIDTH);
        dwInstructor.ensureDebugId(scheduledCourse_add_edit_view + "instructor");
        dwInstructor.addValueChangeHandler(changeEvent -> {
            if (dwInstructor.getSelectedItem() != null && dwInstructor.getSelectedItem().getCategory() != null && dwInstructor.getSelectedItem().getCategory().equals(NOT_AVAILABLE)) {
                WfmInputBox wfmInputBox = new WfmInputBox(IconEnum.WARN, Action.OkCancel, tcStrings.trainerAlreadyBooked(), new String[]{"yes", "no"}, new AbstractAsyncCallback<String>() {
                    @Override
                    public void onSuccess(String result) {
                        if (result != null && result.equalsIgnoreCase("yes")) {

                        } else {
                            if (dwInstructor.getPreviousSelectedItem() != null) {
                                dwInstructor.setSelected(dwInstructor.getPreviousSelectedItem());
                            } else {
                                dwInstructor.clearSelected();
                            }
                        }
                    }
                });
                wfmInputBox.setTitle(wfmStrings.confirmation());
                wfmInputBox.setWidth("320px");
                wfmInputBox.center();
            }
        });


        dwAssessor = new DataListBox();
        dwAssessor.addStyleName(DEFAULT_WIDTH);
        dwAssessor.ensureDebugId(scheduledCourse_add_edit_view + "assessor");

        txtDuration = new TextArea2(TextArea2.AREA_LENGTH_1);
        txtDuration.setSize("250px", "100px");
        txtDuration.setEnabled(false);
        txtDuration.hideCharacterLimitPanel();

        txtNumberOfSeats = new TextBox();
        txtNumberOfSeats.addStyleName(DEFAULT_WIDTH);
        txtNumberOfSeats.ensureDebugId(scheduledCourse_add_edit_view + "numberofseats");
        Validation.addNumericKeyboardListener(txtNumberOfSeats);

        pnlCourseRequirementContainer = new FlowPanel();

        addTitleField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.SCHEDULED_COURSE_DETAILS, tcStrings.scheduledCourseDetails());
        if (formPropertyMap != null && formPropertyMap.get("location") != null) {
            addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.LOCATION, dwLocation, getTitle(formPropertyMap.get("location").isChanged() ? formPropertyMap.get("location").getTitle() : wfmStrings.location(), formPropertyMap.get("location").isRequired()),false,
                    formPropertyMap.get("location").isInformation());
            dwLocation.setEnabled(!formPropertyMap.get("location").isDisabled());
            if (formPropertyMap.get("location").isInformation()) {
                new KpiToolTip(dwLocation, formPropertyMap.get("location").getInformationText());
            }
        } else {
            addField("location", dwLocation, getTitle(wfmStrings.location(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get("course") != null) {
            addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.COURSE, dwCourse, getTitle(formPropertyMap.get("course").isChanged() ? formPropertyMap.get("course").getTitle() : wfmStrings.courses(), formPropertyMap.get("course").isRequired()),false,
                    formPropertyMap.get("course").isInformation());
            dwCourse.setEnabled(!formPropertyMap.get("course").isDisabled());
            if (formPropertyMap.get("course").isInformation()) {
                new KpiToolTip(dwCourse, formPropertyMap.get("course").getInformationText());
            }
        } else {
            addField("course", dwCourse, getTitle(wfmStrings.courses(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get("language") != null) {
            addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.LANGUAGE, dwLanguage, getTitle(formPropertyMap.get("language").isChanged() ? formPropertyMap.get("language").getTitle() : wfmStrings.language(), formPropertyMap.get("language").isRequired()),false,
                    formPropertyMap.get("language").isInformation());
            dwLanguage.setEnabled(!formPropertyMap.get("language").isDisabled());
            if (formPropertyMap.get("language").isInformation()) {
                new KpiToolTip(dwLanguage, formPropertyMap.get("language").getInformationText());
            }
        } else {
            addField("language", dwLanguage, getTitle(wfmStrings.language(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get("overtime") != null) {
            addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.OVERTIME, cbEnableOvertime, getTitle(formPropertyMap.get("overtime").isChanged() ? formPropertyMap.get("overtime").getTitle() : wfmStrings.allowOvertime(), formPropertyMap.get("overtime").isRequired()),false,
                    formPropertyMap.get("overtime").isInformation());
            cbEnableOvertime.setEnabled(!formPropertyMap.get("overtime").isDisabled());
            if (formPropertyMap.get("overtime").isInformation()) {
                new KpiToolTip(cbEnableOvertime, formPropertyMap.get("overtime").getInformationText());
            }
        } else {
            addField("overtime", cbEnableOvertime, getTitle(wfmStrings.allowOvertime(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get("instructor") != null) {
            addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.INSTRUCTOR, dwInstructor, getTitle(formPropertyMap.get("instructor").isChanged() ? formPropertyMap.get("instructor").getTitle() : wfmStrings.instructor(), formPropertyMap.get("instructor").isRequired()),false,
                    formPropertyMap.get("instructor").isInformation());
            dwInstructor.setEnabled(!formPropertyMap.get("instructor").isDisabled());
            if (formPropertyMap.get("instructor").isInformation()) {
                new KpiToolTip(dwInstructor, formPropertyMap.get("instructor").getInformationText());
            }
        } else {
            addField("instructor", dwInstructor, getTitle(wfmStrings.instructor(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get("assessor") != null) {
            addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.ASSESSOR, dwAssessor, getTitle(formPropertyMap.get("assessor").isChanged() ? formPropertyMap.get("assessor").getTitle() : wfmStrings.assessor(), formPropertyMap.get("assessor").isRequired()),false,
                    formPropertyMap.get("assessor").isInformation());
            dwAssessor.setEnabled(!formPropertyMap.get("assessor").isDisabled());
            if (formPropertyMap.get("assessor").isInformation()) {
                new KpiToolTip(dwAssessor, formPropertyMap.get("assessor").getInformationText());
            }
        } else {
            addField("assessor", dwAssessor, getTitle(wfmStrings.assessor(), false));
        }
//        if (formPropertyMap != null && formPropertyMap.get("startDate") != null) {
//            addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.START_DATE, Utils.getInHorizontalPanel(0, 0, true, dateTime.startDate, dateTime.startTime), getTitle(formPropertyMap.get("startDate").isChanged() ? formPropertyMap.get("startDate").getTitle() : wfmStrings.startDate(), formPropertyMap.get("startDate").isRequired()), false,
//                    formPropertyMap.get("startDate").isInformation());
//            Utils.getInHorizontalPanel(0, 0, true, dateTime.startDate, dateTime.startTime).setVisible(true);
//            if (formPropertyMap.get("startDate").isInformation()) {
//                new KpiToolTip(Utils.getInHorizontalPanel(0, 0, true, dateTime.startDate, dateTime.startTime), formPropertyMap.get("startDate").getInformationText());
//            }
//        } else {
//            addField("startDate", Utils.getInHorizontalPanel(0, 0, true, dateTime.startDate, dateTime.startTime), getTitle(wfmStrings.start(), false));
//        }

        if (formPropertyMap != null && formPropertyMap.get("duration") != null) {
            addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.DURATION, txtDuration, getTitle(formPropertyMap.get("duration").isChanged() ? formPropertyMap.get("duration").getTitle() : wfmStrings.duration(), formPropertyMap.get("duration").isRequired()), false,
                    formPropertyMap.get("duration").isInformation());
            txtDuration.setEnabled(!formPropertyMap.get("duration").isDisabled());
            if (formPropertyMap.get("duration").isInformation()) {
                new KpiToolTip(txtDuration, formPropertyMap.get("duration").getInformationText());
            }
        } else {
            addField("duration", txtDuration, getTitle(wfmStrings.duration(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get("numberOfSeats") != null) {
            addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.NUMBER_OF_SEATS, txtNumberOfSeats, getTitle(formPropertyMap.get("numberOfSeats").isChanged() ? formPropertyMap.get("numberOfSeats").getTitle() : wfmStrings.numberOfSeats(), formPropertyMap.get("numberOfSeats").isRequired()), false,
                    formPropertyMap.get("numberOfSeats").isInformation());
            txtNumberOfSeats.setEnabled(!formPropertyMap.get("numberOfSeats").isDisabled());
            if (formPropertyMap.get("numberOfSeats").isInformation()) {
                new KpiToolTip(txtNumberOfSeats, formPropertyMap.get("numberOfSeats").getInformationText());
            }
        } else {
            addField("numberOfSeats", txtNumberOfSeats, getTitle(wfmStrings.numberOfSeats(), false));
        }

//        if (formPropertyMap != null && formPropertyMap.get("courserequirements") != null) {
//            addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.COURSEREQUIREMENTS, courseRequirements, getTitle(formPropertyMap.get("courserequirements").isChanged() ? formPropertyMap.get("courserequirements").getTitle() : wfmStrings.courseRequirements(), formPropertyMap.get("courserequirements").isRequired()), false,
//                    formPropertyMap.get("courserequirements").isInformation());
//            courseRequirements.setVisible(formPropertyMap.get("courserequirements").isDisabled());
//            if (formPropertyMap.get("courserequirements").isInformation()) {
//                new KpiToolTip(courseRequirements, formPropertyMap.get("courserequirements").getInformationText());
//            }
//        } else {
//            addField("numberOfSeats", courseRequirements, getTitle(wfmStrings.courseRequirements(), false));
//        }

//        addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.LOCATION, dwLocation, getTitle(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), true));
//        addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.COURSE, dwCourse, getTitle(wfmStrings.course(), true));
//        addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.LANGUAGE, dwLanguage, getTitle(wfmStrings.language(), true));
//        addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.OVERTIME, cbEnableOvertime, getTitle(wfmStrings.allowOvertime(), false));
//        addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.INSTRUCTOR, dwInstructor, getTitle(wfmStrings.instructor(), true));
//        addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.ASSESSOR, dwAssessor, getTitle(wfmStrings.assessor(), false));

        addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.START_DATE, Utils.getInHorizontalPanel(0, 0, true, dateTime.startDate, dateTime.startTime), getTitle(wfmStrings.startDate(), true));
//        addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.DURATION, txtDuration, null);
//        addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.NUMBER_OF_SEATS, txtNumberOfSeats, getTitle(wfmStrings.numberOfSeats(), true));
        addField(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.COURSEREQUIREMENTS, pnlCourseRequirementContainer, null);

        addTitleField(CustomFormConstants.TASK.ADVANCED_OPTIONS, wfmStrings.advancedOptions());

        if (formPropertyMap != null && formPropertyMap.get("RECURRENING") != null) {
            addField("RECURRENING", recurringPanel, getTitle(formPropertyMap.get("RECURRENING").isChanged() ? formPropertyMap.get("RECURRENING").getTitle() : wfmStrings.recurring(), formPropertyMap.get("RECURRENING").isRequired()));
        } else {
            addField("RECURRENING", recurringPanel, getTitle(wfmStrings.recurring()));
        }

        getCustomFieldUtil().drawCustomFields(this, objectID, false);
        show();
    }

    private void langaugeChangeEvent() {
        if (dwLanguage.getSelectedId() != null) {
            checkAvailability();
        } else {
            courseChangeEvent();
        }
    }

    private void courseChangeEvent() {
        if (dwCourse.getSelectedId() != null) {
            LoadingPanel.loading(true);
            TCService.App.get().getCourseItem(dwCourse.getSelectedId(), new AsyncCallback<CourseItem>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                    caught.printStackTrace();
                }

                @Override
                public void onSuccess(CourseItem result) {
                    LoadingPanel.loading(false);

                    dwInstructor.setItems(result.getInstructors().toArray(new SelectItem[]{}));
                    scheduledCourseItem.setDuration(result.getDuration());
                    checkAvailability();
                }
            });
        }
    }


    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        TCService.App.get().getCourseSchedule(objectID, false, new AsyncCallback<ScheduledCourseItem>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                GWT.log(caught.getMessage());
            }

            @Override
            public void onSuccess(ScheduledCourseItem result) {
                LoadingPanel.loading(false);
                scheduledCourseItem = result;
                fillFormWithData();
                checkAvailability();
            }
        });
    }

    protected void fillFormWithData() {
        dwLocation.setItems(scheduledCourseItem.getLocations());
        dwCourse.setItems(scheduledCourseItem.getCourses());
        dwLanguage.setItems(scheduledCourseItem.getLanguages());
        dwAssessor.setItems(scheduledCourseItem.getAssessors());

        dwLocation.setSelected(scheduledCourseItem.getLocationID());
        dwLanguage.setSelected(scheduledCourseItem.getLanguageID());
        txtNumberOfSeats.setValue(scheduledCourseItem.getNumberOfSeats() != null ? String.valueOf(scheduledCourseItem.getNumberOfSeats()) : null);
        dwAssessor.setSelected(scheduledCourseItem.getAssessorID());
        cbEnableOvertime.setValue(scheduledCourseItem.isEnabledOvertime());

        if (scheduledCourseItem.getStartDate() != null) {
            dateTime.getStartDatePicker().setDate(scheduledCourseItem.getStartDate());
            dateTime.setStartTime(new StartEndTime(timeFormat.format(scheduledCourseItem.getStartDate())).time);
        } else {
            setDefaultStartTime();
        }

        if (scheduledCourseItem.getCourseID() != null) {
            dwCourse.setSelected(scheduledCourseItem.getCourseID());
            dwCourse.fireEvent(new OurChangeEvent());
        }
        getCustomFieldUtil().fillCustomFieldsWithData(scheduledCourseItem.getCustomFieldItems());
    }


    @Override
    protected void addButtons() {
        addButton(wfmStrings.save(), event -> save());
    }

    private void save() {
        enableButton(false);
        if (!validate()) {
            enableButton(true);
            return;
        }

        scheduledCourseItem = getObjectData();

        LoadingPanel.loading(true);
        TCService.App.get().saveCourseSchedule(scheduledCourseItem, new AsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                GWT.log(caught.getMessage());
                enableButton(true);
            }

            @Override
            public void onSuccess(Integer result) {
                LoadingPanel.loading(false);
                if (result != null && result == -1) {
                    String courseName = (dwCourse.getSelectedItem() != null && dwCourse.getSelectedItem().getName() != null) ? dwCourse.getSelectedItem().getName() : "";
                    String locationName = (dwLocation.getSelectedItem() != null && dwLocation.getSelectedItem().getName() != null) ? dwLocation.getSelectedItem().getName() : "";
                    Info.warn("Sorry, can't save the schedule. Please set price for " + courseName + " course for " + locationName + " location");
                    enableButton(true);
                } else {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.changes()), Info.Type.INFO);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SCHEDULED_COURSE_SAVED, null, null);
                    closeTab();
                }
            }
        });
    }

    private ScheduledCourseItem getObjectData() {
        if (scheduledCourseItem == null) {
            scheduledCourseItem = new ScheduledCourseItem();
        }

        if (enableEmailReminder.getValue()) {
            if (reminderView != null) {
                RecurrenceJobItem recurrenceJobItem = reminderView.getData();
                if (recurrenceJobItem != null) {
                    recurrenceJobItem.setStartDate(dateTime.getStartDate());
                    if (recurrenceJobItem.getType().equals(SchedulerConstant.RECURRENCE_TYPE_MONTHLY)) {
                        recurrenceJobItem.setMonthlyOrYearlyDay(dateTime.getStartDate().getDate());
                        recurrenceJobItem.setMonthlyOrYearlyPatternOption(SchedulerConstant.MONTHLY_OR_YEARLY_PATTERN_CUSTOM);
                    }
                    scheduledCourseItem.setRecurrenceJobItem(recurrenceJobItem);
                }
            }
        }

        scheduledCourseItem.setLocationID(dwLocation.getSelectedId());
        scheduledCourseItem.setCourseID(dwCourse.getSelectedId());
        scheduledCourseItem.setLanguageID(dwLanguage.getSelectedId());
        scheduledCourseItem.setInstructorID(dwInstructor.getSelectedId());
        scheduledCourseItem.setAssessorID(dwAssessor.getSelectedId());
        scheduledCourseItem.setStartDate(dateTime.getStartDate());
        scheduledCourseItem.setNumberOfSeats(Integer.valueOf(txtNumberOfSeats.getValue()));
        scheduledCourseItem.setReservations(courseRequirements.getObjectData());
        scheduledCourseItem.setEnableOvertime(cbEnableOvertime.getValue());
        scheduledCourseItem.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());
        return scheduledCourseItem;
    }

    @Override
    protected void initPredefinedValues() {

    }

    /**
     * Check date availability for schedule
     *
     * @param date
     * @return
     */
    private boolean isAvailableDateForSchedule(Date date) {
        return isAvailableDateForSchedule(date, false);
    }

    private boolean isAvailableDateForSchedule(Date date, boolean isNew) {
        TimeSlotItem timeSlotItem = scheduledCourseItem.getTimeSlotItems().get(date.getDay());
        if (cbEnableOvertime.getValue() && weekStart != null && (timeSlotItem.getStartTime() == 0 || timeSlotItem.getEndTime() == 0)) {
            timeSlotItem = scheduledCourseItem.getTimeSlotItems().get(weekStart);
        }

        if (timeSlotItem.getStartTime() == 0 && timeSlotItem.getEndTime() == 0) {
            return false;
        }

        Date dayStart = (Date) date.clone();
        dayStart.setHours(0);
        dayStart.setMinutes(0);
        dayStart.setSeconds(0);

        int timeInDay = Long.valueOf((date.getTime() - dayStart.getTime()) / 60000).intValue();

        return isNew || timeInDay >= timeSlotItem.getStartTime()
                && timeInDay <= timeSlotItem.getEndTime();
    }

    /**
     * Check availability item for course schedule
     */
    private void checkAvailability() {
        scheduledCourseItem.setStartDate(dateTime.getStartDate());

        if (dwLocation.getSelectedId() != null && dwCourse.getSelectedId() != null && dwLanguage.getSelectedId() != null) {
            scheduledCourseItem.setLocationID(dwLocation.getSelectedId());
            scheduledCourseItem.setCourseID(dwCourse.getSelectedId());
            scheduledCourseItem.setLanguageID(dwLanguage.getSelectedId());
            scheduledCourseItem.setEndDate(getCalculatedEndDate(scheduledCourseItem));

            LoadingPanel.loading(true);
            TCService.App.get().getAvailabilityData(scheduledCourseItem, new AsyncCallback<ScheduledCourseItem>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                    caught.printStackTrace();
                }

                @Override
                public void onSuccess(ScheduledCourseItem result) {
                    LoadingPanel.loading(false);
                    scheduledCourseItem = result;
                    initializeInternalInformation();
                }
            });
        }
    }

    private void initializeInternalInformation() {
        initDurationInfo();

        dwInstructor.clear();
        dwInstructor.setItems(scheduledCourseItem.getInstructors());
        dwInstructor.setSelected(scheduledCourseItem.getInstructorID());
        if (scheduledCourseItem.isStudentAttended() && scheduledCourseItem.getInstructorID() != null) {
            dwInstructor.setEnabled(false);
        }

        initCourseRequirements();
    }

    /**
     * Initialize Schedule course duration information for view
     */
    private void initDurationInfo() {
        String durationInfo = "Schedule start time: " + DateUtils.formatInternal(dateTime.getStartDate()) + "; \n" +
                "Schedule duration: " + scheduledCourseItem.getDuration() + " hour(s); \n" +
                "Schedule end time: " + DateUtils.formatInternal(scheduledCourseItem.getEndDate()) + "; \n";
        txtDuration.setText(durationInfo);
    }

    /**
     * get calculated end date for check availability schedule items
     *
     * @return
     */
    private Date getCalculatedEndDate(ScheduledCourseItem scheduledCourseItem) {
        Date startDate = (Date) dateTime.getStartDate().clone();
        startDate.setHours(0);
        startDate.setMinutes(0);
        startDate.setSeconds(0);

        Long startTime = (dateTime.getStartDate().getTime() - startDate.getTime()) / 60000;
        Integer scStartTime = startTime.intValue();
        Integer cDuration = scheduledCourseItem.getDuration() * 60;

        Date date = (Date) startDate.clone();
        int duration = 0;
        int scheduleDay = 0;
        do {
            TimeSlotItem timeSlotItem = scheduledCourseItem.getTimeSlotItems().get(date.getDay());

            if (cbEnableOvertime.getValue() && weekStart != null && (timeSlotItem.getStartTime() == 0 || timeSlotItem.getEndTime() == 0)) {
                timeSlotItem = scheduledCourseItem.getTimeSlotItems().get(weekStart);
            }

            if (timeSlotItem.getStartTime() != 0 && timeSlotItem.getEndTime() != 0) {
                int totalDurationInDay = 0; //total duration in day
                Integer scTime = scStartTime; //scheduled course start time

                //calculate lunch time from time slot
                int lunchTime = timeSlotItem.getLunchEnd() - timeSlotItem.getLunchStart();

                //calculate break time from time slot
                int breakTime = timeSlotItem.getCoffeeEnd() - timeSlotItem.getCoffeeStart();

                //calculate duration hour(s) in day
                if (scTime != 0) {
                    duration = scTime + cDuration;

                    //lunch time applying to schedule duration
                    if (scTime <= timeSlotItem.getLunchStart() && duration > timeSlotItem.getLunchStart()) {
                        totalDurationInDay += lunchTime;
                    }

                    //break time applying to schedule duration
                    if (scTime <= timeSlotItem.getCoffeeStart() && (duration + lunchTime) > timeSlotItem.getCoffeeStart()) {
                        totalDurationInDay += breakTime;
                    }

                    totalDurationInDay += duration;

                    scTime = 0;
                } else {
                    duration = timeSlotItem.getStartTime() + cDuration;

                    totalDurationInDay = duration + lunchTime + breakTime;
                }

                //split day(s) of course schedule duration
                if (timeSlotItem.getEndTime() < totalDurationInDay) {
                    cDuration = totalDurationInDay - timeSlotItem.getEndTime();
                    date.setDate(date.getDate() + 1);
                } else {
                    //apply lunch time to duration in day
                    if (scStartTime <= timeSlotItem.getLunchStart() && duration > timeSlotItem.getLunchStart()) {
                        duration += lunchTime;
                    }

                    //apply break time to duration in day
                    if (scStartTime <= timeSlotItem.getCoffeeStart() && (duration + lunchTime) > timeSlotItem.getCoffeeStart()) {
                        duration += breakTime;
                    }

                    date.setMinutes(duration);
                    cDuration = 0;
                }

                scheduleDay++;
            } else {
                date.setDate(date.getDate() + 1);
            }
        } while (cDuration > 0);

        scheduledCourseItem.setScheduleDuration(scheduleDay);
        return date;
    }

    private void initCourseRequirements() {
        courseRequirements = new CourseRequirements(scheduledCourseItem);
        pnlCourseRequirementContainer.clear();
        pnlCourseRequirementContainer.add(courseRequirements);
    }

    private void setDefaultStartTime() {
        Date _cschStartDate = new Date();
        while (!isAvailableDateForSchedule(_cschStartDate, true)) {
            _cschStartDate.setDate(_cschStartDate.getDate() + 1);
        }
        dateTime.getStartDatePicker().setDate(_cschStartDate);
        Date startDate = (Date) dateTime.getStartDatePicker().getDate().clone();
        startDate.setHours(0);
        startDate.setMinutes(scheduledCourseItem.getTimeSlotItems().get(startDate.getDay()).getStartTime());
        startDate.setSeconds(0);
        dateTime.setStartTime(new StartEndTime(timeFormat.format(startDate)).time);
    }

    @Override
    public String getIconStyle() {
        return "bgMark scheduled-course-edit-icon";
    }

    private boolean validate() {
        int errors = 0;
        clearErrorStyle();



        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.NUMBER_OF_SEATS) != null && formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.NUMBER_OF_SEATS).isRequired()) {
            errors += markAsError(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.NUMBER_OF_SEATS, txtNumberOfSeats, !Validation.validateTextBoxRequired(txtNumberOfSeats));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.ASSESSOR) != null && formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.ASSESSOR).isRequired()) {
            errors += markAsError(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.ASSESSOR, dwAssessor, !Validation.validateListBoxRequired(dwAssessor, new HTML(), null));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.DURATION) != null && formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.DURATION).isRequired()) {
            errors += markAsError(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.DURATION, txtDuration, !Validation.validateTextAreaRequired(txtDuration));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.START_DATE) != null && formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.START_DATE).isRequired()) {
            errors += markAsError(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.START_DATE, dateTime, dateTime == null);
        }

        errors += markAsError(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.LOCATION, dwLocation, !Validation.validateListBoxRequired(dwLocation, new HTML(), null));
        errors += markAsError(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.COURSE, dwCourse, !Validation.validateListBoxRequired(dwCourse, new HTML(), null));
        errors += markAsError(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.LANGUAGE, dwLanguage, !Validation.validateListBoxRequired(dwLanguage, new HTML(), null));
        errors += markAsError(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.INSTRUCTOR, dwInstructor, !Validation.validateListBoxRequired(dwInstructor, new HTML(), null));
//        errors += markAsError(CustomFormConstants.TRAINING_CENTER.SCHEDULED_COURSE.NUMBER_OF_SEATS, txtNumberOfSeats, !Validation.validateTextBoxRequired(txtNumberOfSeats));
        errors += getCustomFieldUtil().validateCustomFields();

        if (courseRequirements != null && !courseRequirements.validation()) {
            errors++;
        }

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.SCHEDULED_COURSE_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }


    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            @Override
            public void onFailure(Throwable reason) {
                callback.onFailure(reason);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    class OurChangeEvent extends ChangeEvent {
    }
}
