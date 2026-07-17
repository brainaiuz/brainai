package com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateService;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Attendee;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.CalendarEventReminder;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DatePicker;
import com.edatasite.workforce.gwt.core.client.ui.KpiEditor;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiTimePicker;
import com.edatasite.workforce.gwt.core.client.ui.components.RecurringWidget;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.FormGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumn;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GColumnEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.form.GRow;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.svgIcon.SvgEnum;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.ContactLookUp;
import com.edatasite.workforce.gwt.core.client.ui.lookup.MultiSelectEmployeeLookUp;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableNewUI;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinks;
import com.edatasite.workforce.gwt.core.client.ui.tagging.HasLinksInterface;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.FooterInformer;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.googlecalendar.client.rpc.GoogleCalendarService;
import com.edatasite.workforce.gwt.googlecalendar.client.rpc.GoogleCalendarServiceAsync;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.DateUtils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Author: Azazello
 * Date: 5/24/2018
 * Time: 11:32 AM
 */
public class EditEventView extends CustomForm2 implements HasLinksInterface, FormHasCustomFieldInterface, Constants, Colapse {
    private static final GoogleCalendarServiceAsync calendarService = GoogleCalendarService.App.get();
    private int activityType;
    private Integer objectID,relationID;
    private String relationType,relationName;
    private boolean isCopy;
    private Appointment item;
    private final String test_code_ID_name = "edit_activity_";
    private DataListBox template;
    private MaterialPanel cloneWidget,callTypePanel,whenPanel;
    private KpiEditor description;
    protected GeneralFileUpload attachment = null;
    private KpiRadioButton currentCall, completedCall,scheduleCall, inbound,missed,outbound;
    private TextBox subject, durationMin,durationSec;
    private DatePicker startDate,endDate,startDateClone, endDateClone;
    private KpiTimePicker fromClone, toClone, from, to;
    private KpiSwitcher allDay, allDayClone,enableEmailReminder, clone;
    private MultiSelectEmployeeLookUp assignee;
    private MultiTableNewUI reminders,guestTable;
    private RecurringWidget recurringWidget;
    private FormGroup remindersWidget;
    private WfmButton2 save;
    private FooterInformer link;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    AtomicBoolean firstClick = new AtomicBoolean(true);
    ArrayList<RelationItem> relationItems = new ArrayList<>();


    public EditEventView(Integer eventID, int activityType) {
        super("editEvent", Property.get(Constants.EVENT_LIST, wfmStrings.editEvent(), wfmStrings.event()));
        this.objectID = eventID;
        this.activityType = activityType;
    }

    public EditEventView(Integer eventID, int activityType, boolean isCopy) {
        super("test", "test");
        this.objectID = eventID;
        this.activityType = activityType;
        this.isCopy = isCopy;
    }

    public EditEventView(Integer activityType, Integer relationID, String relationType) {
        super("addEvent", wfmStrings.addMess() + " " + wfmStrings.event());
        this.activityType = activityType;
        this.relationID = relationID;
        this.relationType = relationType;
        getRelationName(relationID, relationType);
    }

    public EditEventView(Integer activityType) {
        super("addEvent", wfmStrings.addMess() + " " + wfmStrings.event());
        this.activityType = activityType;
    }

    protected Widget onInitialize() {
        super.onInitialize();
        return null;
    }

    protected void registerFields() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(Appointment.CALL_LOG == activityType ? ViewName.LogACall : ViewName.Activity, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
                initializeForms();
            }
        });
        if (RelationItem.TYPE_EMPLOYEE.equals(relationType)) {
            EmailTemplateService.App.get().getMessageCenterEmailTemplates(new ArrayList<>(Collections.singleton(getModuleCode())), new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void failure(Throwable throwable) {

                }

                @Override
                public void onSuccess(SelectItem[] templates) {
                    template.setItems(templates);
                }

            });
        }
    }

    private String getModuleCode() {
        return "ET_EMPLOYEE_MODULE";
    }

    private void initializeForms() {

        String subjText = "";
        if (this.activityType == Appointment.INTERVIEW && relationName != null) {
            subjText = wfmStrings.interviewWith() + " - " + relationName;
        }
        subject = new TextBox();
        subject.setText(subjText);
        subject.ensureDebugId(this.test_code_ID_name + "subject");

        template = new DataListBox();
        template.ensureDebugId(this.test_code_ID_name + "template");
        template.addValueChangeHandler(changeEvent -> setSubjectAndDescription(template.getSelectedId()));

        attachment = new GeneralFileUpload(Constants.F_EVENT, objectID, objectID);
        attachment.setWidth("100%");
        //when
        whenPanel = new MaterialPanel();

        startDate = new DatePicker();
        startDate.setDate(new Date());
        startDate.ensureDebugId(this.test_code_ID_name + "startDate");
        startDate.addChangeHandler(changeEvent -> endDate.setDate(startDate.getDate()));

        endDate = new DatePicker();
        endDate.setDate(new Date());
        endDate.ensureDebugId(this.test_code_ID_name + "endDate");

        from = new KpiTimePicker(true);
        from.setMarginTop(0);
        from.setValue(new int[]{new Date().getHours(), new Date().getMinutes()});
        from.setWidth("25%");
        from.setStyleName("form-control timepicker");
        from.ensureDebugId(this.test_code_ID_name + "from");
        from.setChangeCommand(() -> {
            if (from.getValue() != null) {
                int[] fromtime = from.getValue();
                int hour = fromtime[0];
                int minutes = fromtime[1];
                to.setValue(getToTime(hour, minutes));
            }
        });

        to = new KpiTimePicker(true);
        to.setMarginTop(0);
        int hour = new Date().getHours();
        int minutes = new Date().getMinutes();
        if (minutes < 30) {
            minutes += 30;
        } else {
            minutes = 30 - (60 - minutes);
            hour++;
        }
        to.setValue(new int[]{hour, minutes});
        to.setWidth("15%");
        to.setStyleName("form-control timepicker");
        to.ensureDebugId(this.test_code_ID_name + "to");

        allDay = new KpiSwitcher();
        allDay.setTooltip(wfmStrings.allDay());
        allDay.addValueChangeHandler(event -> fillWhenPanel(event.getValue()));
        allDay.setValue(true, true);
        allDay.ensureDebugId(this.test_code_ID_name + "allDay");
        if (objectID == null) {
            allDay.setValue(false, true);
            whenPanel.clear();
            Div toHTML = InputGroup.wrapIntoGroupContent(InputGroup.wrapIntoGroupText(new HTML(wfmStrings.to())));
            whenPanel.add(new AdvancedInputGroup(null, new InputGroup(startDate, from, toHTML, endDate, to), InputGroup.wrapIntoGroupText(allDay), false, false));
        }


        description = new KpiEditor(true);
        description.ensureDebugId(this.test_code_ID_name + "description");

        assignee = new MultiSelectEmployeeLookUp();
        assignee.getFilterParametrs().setHRMS(true);
        assignee.selectCurrentUser();
        assignee.ensureDebugId(this.test_code_ID_name + "assignee");

        guestTable = new MultiTableNewUI(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getGuestWidget(null);
            }

            @Override
            public boolean isFilled() {
                return false;
            }
        }, false);
        guestTable.ensureDebugId(this.test_code_ID_name + "guestTable");

        enableEmailReminder = new KpiSwitcher();
        enableEmailReminder.addValueChangeHandler(booleanValueChangeEvent -> recurringWidget.setVisible(booleanValueChangeEvent.getValue()));
        enableEmailReminder.ensureDebugId(this.test_code_ID_name + "enableEmailReminder");

        recurringWidget = new RecurringWidget(SchedulerConstant.RECURRING_EVENT_FORM);
        recurringWidget.getElement().getStyle().setPadding(0, Style.Unit.PX);
        recurringWidget.getElement().getStyle().setMarginTop(15, Style.Unit.PX);
        recurringWidget.ensureDebugId(this.test_code_ID_name + "recurringWidget");
        MaterialPanel recurrencePanel = new MaterialPanel();
        recurrencePanel.add(enableEmailReminder);
        recurrencePanel.add(recurringWidget);

        reminders = new MultiTableNewUI(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getReminderWidgets(null);
            }

            @Override
            public boolean isFilled() {
                return false;
            }
        });
        reminders.ensureDebugId(this.test_code_ID_name + "reminders");
        remindersWidget = new FormGroup(wfmStrings.reminders(), reminders, false);

        if (this.activityType == Appointment.CALL_LOG) {
            initCallTypePanel();
        }

        addTitleField(EVENT_INFORMATION, Property.get(Constants.EVENT_LIST, wfmStrings.basicDetails(), wfmStrings.event()));
        addTitleField(CALL_INFORMATION, wfmStrings.logaCallInformation());

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUBJECT) != null) {
            addField(CustomFormConstants.SUBJECT, subject, getTitle(formPropertyMap.get(CustomFormConstants.SUBJECT).isChanged() ? formPropertyMap.get(CustomFormConstants.SUBJECT).getTitle() : wfmStrings.subject(), formPropertyMap.get(CustomFormConstants.SUBJECT).isRequired()),false, formPropertyMap.get(CustomFormConstants.SUBJECT).isInformation());
            subject.setEnabled(!formPropertyMap.get(CustomFormConstants.SUBJECT).isDisabled());

            if (formPropertyMap.get(CustomFormConstants.SUBJECT).isInformation()) {
                new KpiToolTip(subject, formPropertyMap.get(CustomFormConstants.SUBJECT).getInformationText());
            }

        } else {
            addField(CustomFormConstants.SUBJECT, subject, getTitle(wfmStrings.subject(), true));
        }
        if (RelationItem.TYPE_EMPLOYEE.equals(relationType) && objectID == null) {
            if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CUSTOM_HTML_TEMPLATE) != null) {
                addField(CustomFormConstants.CUSTOM_HTML_TEMPLATE, template, getTitle(formPropertyMap.get(CustomFormConstants.CUSTOM_HTML_TEMPLATE).isChanged() ? formPropertyMap.get(CustomFormConstants.CUSTOM_HTML_TEMPLATE).getTitle() : wfmStrings.template(), formPropertyMap.get(CustomFormConstants.CUSTOM_HTML_TEMPLATE).isRequired()),false, formPropertyMap.get(CustomFormConstants.CUSTOM_HTML_TEMPLATE).isInformation());
                template.setEnabled(!formPropertyMap.get(CustomFormConstants.CUSTOM_HTML_TEMPLATE).isDisabled());
            } else {
                addField(CustomFormConstants.CUSTOM_HTML_TEMPLATE, template, getTitle(wfmStrings.template(), false));
            }
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.WHEN) != null) {
            addField(CustomFormConstants.WHEN, whenPanel, getTitle(formPropertyMap.get(CustomFormConstants.WHEN).isChanged() ? formPropertyMap.get(CustomFormConstants.WHEN).getTitle() : wfmStrings.when(), formPropertyMap.get(CustomFormConstants.WHEN).isRequired()),false, formPropertyMap.get(CustomFormConstants.WHEN).isInformation());
            whenPanel.setEnabled(!formPropertyMap.get(CustomFormConstants.WHEN).isDisabled());

            if (formPropertyMap.get(CustomFormConstants.WHEN).isInformation()) {
                new KpiToolTip(whenPanel, formPropertyMap.get(CustomFormConstants.WHEN).getInformationText());
            }

        } else {
            addField(CustomFormConstants.WHEN, whenPanel, getTitle(wfmStrings.when(), true));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION) != null) {
            addField(CustomFormConstants.DESCRIPTION, description, getTitle(formPropertyMap.get(CustomFormConstants.DESCRIPTION).isChanged() ? formPropertyMap.get(CustomFormConstants.DESCRIPTION).getTitle() : wfmStrings.description(), formPropertyMap.get(CustomFormConstants.DESCRIPTION).isRequired()),false, formPropertyMap.get(CustomFormConstants.DESCRIPTION).isInformation());
            description.setEnabled(!formPropertyMap.get(CustomFormConstants.DESCRIPTION).isDisabled());

            if (formPropertyMap.get(CustomFormConstants.DESCRIPTION).isInformation()) {
                new KpiToolTip(description, formPropertyMap.get(CustomFormConstants.DESCRIPTION).getInformationText());
            }
        } else {
            addField(CustomFormConstants.DESCRIPTION, description, getTitle(wfmStrings.description()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SHARED_WITH) != null) {
            addField(CustomFormConstants.SHARED_WITH, assignee, getTitle(formPropertyMap.get(CustomFormConstants.SHARED_WITH).isChanged() ? formPropertyMap.get(CustomFormConstants.SHARED_WITH).getTitle() : wfmStrings.share(), formPropertyMap.get(CustomFormConstants.SHARED_WITH).isRequired()),false, formPropertyMap.get(CustomFormConstants.SHARED_WITH).isInformation());
            assignee.setEnabled(!formPropertyMap.get(CustomFormConstants.SHARED_WITH).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.SHARED_WITH).isInformation()) {
                new KpiToolTip(assignee, formPropertyMap.get(CustomFormConstants.SHARED_WITH).getInformationText());
            }
        } else {
            addField(CustomFormConstants.SHARED_WITH, assignee, getTitle(wfmStrings.share(), true));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.GUESTS) != null) {
            addField(CustomFormConstants.GUESTS, guestTable, getTitle(formPropertyMap.get(CustomFormConstants.GUESTS).isChanged() ? formPropertyMap.get(CustomFormConstants.GUESTS).getTitle() : wfmStrings.guests(), formPropertyMap.get(CustomFormConstants.GUESTS).isRequired()),false, formPropertyMap.get(CustomFormConstants.GUESTS).isInformation());
            if (formPropertyMap.get(CustomFormConstants.GUESTS).isInformation()) {
                new KpiToolTip(guestTable, formPropertyMap.get(CustomFormConstants.GUESTS).getInformationText());

            }
        } else {
            addField(CustomFormConstants.GUESTS, guestTable, getTitle(wfmStrings.guests()));
        }

        addTitleField(ADVANCED, wfmStrings.advanced());

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.RECURRING_WIDGET) != null) {
            addField(CustomFormConstants.RECURRING_WIDGET, recurrencePanel, getTitle(formPropertyMap.get(CustomFormConstants.RECURRING_WIDGET).isChanged() ? formPropertyMap.get(CustomFormConstants.RECURRING_WIDGET).getTitle() : wfmStrings.recurrence(), formPropertyMap.get(CustomFormConstants.RECURRING_WIDGET).isRequired()),false, formPropertyMap.get(CustomFormConstants.RECURRING_WIDGET).isInformation());
            recurrencePanel.setEnabled(!formPropertyMap.get(CustomFormConstants.RECURRING_WIDGET).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.RECURRING_WIDGET).isInformation()) {
                new KpiToolTip(recurrencePanel, formPropertyMap.get(CustomFormConstants.RECURRING_WIDGET).getInformationText());
            }
        } else {
            addField(CustomFormConstants.RECURRING_WIDGET, recurrencePanel, getTitle(wfmStrings.recurrence()));
        }

        addField(CustomFormConstants.ENABLE_REMINDER, remindersWidget, null);
        addField(ATTACHMENTS, attachment, null, true);
        addTitleField(ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        getCustomFieldUtil().drawCustomFields(this, objectID);
        if (objectID == null) {
            setDefaultValues();
            setDefaultValuesByFormProperty();
        }

        show();
    }

    @Override
    protected void initPredefinedValues() {

    }

    private void initCallTypePanel() {
        callTypePanel = new MaterialPanel();
        inbound = new KpiRadioButton("callType", wfmStrings.inbound());
        inbound.ensureDebugId(this.test_code_ID_name + "inbound");
        outbound = new KpiRadioButton("callType", wfmStrings.outbound());
        outbound.getElement().getStyle().setMarginLeft(15, Style.Unit.PX);
        outbound.ensureDebugId(this.test_code_ID_name + "outbound");
        outbound.setValue(true);
        missed = new KpiRadioButton("callType", wfmStrings.missed());
        missed.getElement().getStyle().setMarginLeft(30, Style.Unit.PX);
        missed.ensureDebugId(this.test_code_ID_name + "missed");
        callTypePanel.add(inbound);
        callTypePanel.add(outbound);
        callTypePanel.add(missed);
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CALL_TYPE) != null) {
            addField(CustomFormConstants.CALL_TYPE, callTypePanel, getTitle(formPropertyMap.get(CustomFormConstants.CALL_TYPE).isChanged() ? formPropertyMap.get(CustomFormConstants.CALL_TYPE).getTitle() : wfmStrings.callType(), formPropertyMap.get(CustomFormConstants.CALL_TYPE).isRequired()));
        } else {
            addField(CustomFormConstants.CALL_TYPE, callTypePanel, getTitle(wfmStrings.callType()));
        }


        MaterialPanel callDetailsPanel = new MaterialPanel();

        currentCall = new KpiRadioButton("callDetails", wfmStrings.current());
        currentCall.ensureDebugId(this.test_code_ID_name + "currentCall");
        currentCall.setValue(false);
        currentCall.setEnabled(false);

        completedCall = new KpiRadioButton("callDetails", wfmStrings.completed());
        completedCall.ensureDebugId(this.test_code_ID_name + "completedCall");
        completedCall.setValue(true);

        scheduleCall = new KpiRadioButton("callDetails", wfmStrings.schedule());
        scheduleCall.ensureDebugId(this.test_code_ID_name + "scheduleCall");

        GRow callRow = new GRow();
        callRow.add(new GColumn(GColumnEnum.COL_4, currentCall));
        callRow.add(new GColumn(GColumnEnum.COL_4, completedCall));
        callRow.add(new GColumn(GColumnEnum.COL_4, scheduleCall));
        callDetailsPanel.add(callRow);
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CALL_DETAILS) != null) {
            addField(CustomFormConstants.CALL_DETAILS, callDetailsPanel, getTitle(formPropertyMap.get(CustomFormConstants.CALL_DETAILS).isChanged() ? formPropertyMap.get(CustomFormConstants.CALL_DETAILS).getTitle() : wfmStrings.callDetails(), formPropertyMap.get(CustomFormConstants.CALL_DETAILS).isRequired()));
        } else {
            addField(CustomFormConstants.CALL_DETAILS, callDetailsPanel, getTitle(wfmStrings.callDetails()));
        }

        durationMin = new TextBox();
        durationMin.setPlaceHolder(wfmStrings.minutes());
        validateMinute(durationMin);

        durationSec = new TextBox();
        durationSec.setText("0");
        durationSec.setPlaceHolder(wfmStrings.seconds());
        validateMinute(durationSec);

        if (objectID == null) {

            MaterialPanel clonePanel = new MaterialPanel();
            clone = new KpiSwitcher();
            clone.addValueChangeHandler(booleanValueChangeEvent -> {
                cloneWidget.setVisible(booleanValueChangeEvent.getValue());
            });
            cloneWidget = new MaterialPanel();
            cloneWidget.getElement().getStyle().setPadding(0, Style.Unit.PX);
            cloneWidget.getElement().getStyle().setMarginTop(15, Style.Unit.PX);
            cloneWidget.setVisible(false);
            initCloneTimeWidgets();
            clonePanel.add(clone);
            clonePanel.add(cloneWidget);

            if (formPropertyMap != null && formPropertyMap.get("CLONE") != null) {
                addField(CustomFormConstants.CALL_CLONE, clonePanel, getTitle(formPropertyMap.get("CLONE").isChanged() ? formPropertyMap.get("CLONE").getTitle() : wfmStrings.clonE(), formPropertyMap.get("CLONE").isRequired()));
            } else {
                addField(CustomFormConstants.CALL_CLONE, clonePanel, getTitle(wfmStrings.clonE()));
            }


            if (completedCall.getValue()) {
                inbound.setEnabled(true);
                outbound.setEnabled(true);
                missed.setEnabled(true);
                outbound.setValue(true);
                remindersWidget.setVisible(false);
                remindersWidget.setLabel(null);
                currentCall.setEnabled(false);
                whenPanel.clear();
                whenPanel.add(new GRow(new GColumn(GColumnEnum.COL_8, new InputGroup(startDate, from)), new GColumn(GColumnEnum.COL_2, durationMin), new GColumn(GColumnEnum.COL_2, durationSec)));
            }


            completedCall.addValueChangeHandler(valueChangeEvent -> {
                if (completedCall.getValue()) {
                    inbound.setEnabled(true);
                    outbound.setEnabled(true);
                    missed.setEnabled(true);
                    currentCall.setEnabled(false);
                    callTypePanel.clear();
                    callTypePanel.add(inbound);
                    callTypePanel.add(outbound);
                    callTypePanel.add(missed);
                    remindersWidget.setVisible(false);
                    remindersWidget.setLabel(null);
                    whenPanel.clear();
                    whenPanel.add(new GRow(new GColumn(GColumnEnum.COL_8, new InputGroup(startDate, from)), new GColumn(GColumnEnum.COL_2, durationMin), new GColumn(GColumnEnum.COL_2, durationSec)));
                }
            });

            scheduleCall.addValueChangeHandler(valueChangeEvent -> {
                if (scheduleCall.getValue()) {
                    inbound.setEnabled(false);
                    missed.setEnabled(false);
                    outbound.setEnabled(false);
                    currentCall.setEnabled(false);
                    outbound.setValue(true);
                    callTypePanel.clear();
                    callTypePanel.add(inbound);
                    callTypePanel.add(outbound);
                    callTypePanel.add(missed);
                    remindersWidget.setVisible(true);
                    remindersWidget.setLabel(wfmStrings.reminders());
                    whenPanel.clear();
                    whenPanel.add(new GRow(new GColumn(GColumnEnum.COL_6, new InputGroup(startDate, from))));
                }
            });


        }


    }

    private void fillWhenPanel(boolean isAllDay) {
        whenPanel.clear();
        Div toHTML = InputGroup.wrapIntoGroupContent(InputGroup.wrapIntoGroupText(new HTML(wfmStrings.to())));
        toHTML.addStyleName("width-auto");
        if (isAllDay) {
            whenPanel.add(new AdvancedInputGroup(null, new InputGroup(startDate, toHTML, endDate), InputGroup.wrapIntoGroupText(allDay), false, false));
        } else {
            whenPanel.add(new AdvancedInputGroup(null, new InputGroup(startDate, from, toHTML, endDate, to), InputGroup.wrapIntoGroupText(allDay), false, false));
        }
    }

    private WidgetsMap getGuestWidget(SelectItem id) {
        WidgetsMap widgetsMap = new WidgetsMap();
        ContactLookUp relationTextBox = new ContactLookUp(BY_BOTH);
        if (id != null) {
            relationTextBox.setSelected(id);
        }
        widgetsMap.addToCenter(MultiTableNewUI.LOOK_UP_BOX, relationTextBox);
        return widgetsMap;
    }

    private WidgetsMap getReminderWidgets(CalendarEventReminder data) {
        WidgetsMap widgetsMap = new WidgetsMap();
        if (data == null) {
            data = new CalendarEventReminder();
        }
        Reminder reminder = new Reminder();
        reminder.setReminderData(data);
        widgetsMap.addToCenter("reminder", reminder);
        return widgetsMap;
    }

    @Override
    protected void addButtons() {
        Div a = new Div();
        link = new FooterInformer(SvgEnum.link, wfmStrings.links(), null);
        footer.addToLeftSide(link);

        relationItems = new ArrayList<>();
        if (relationID != null && relationType != null) {
            relationItems.add(RelationItem.newEventRelation(relationType, relationID, relationName));
        }

        link.addClickHandler(event -> {
            if (firstClick.get()) {
                getLinkingUtil().getAddLinkSideNavBox();
                if (objectID == null) {
                    getLinkingUtil().getAddLinkSideNavBox().setSelectedRelations(relationItems, true);
                } else {
                    getLinkingUtil().getAddLinkSideNavBox().setSelectedRelations(item.getRelations(), false);
                }
                firstClick.set(false);
            } else {
                getLinkingUtil().getAddLinkSideNavBox().show();
            }
        });

        if (relationID != null && relationType != null) {
            link.setBadgeCount(1);
        }

        save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save());
        if (objectID != null && this.activityType == Appointment.CALL_LOG) {
            save.setVisible(false);
        }

        Div saveWrapper = new Div();
        saveWrapper.add(save);
        a.add(saveWrapper);

        addButton(a);
    }

    @Override
    protected void getDataToFillFields() {
        if (objectID != null) {
            LoadingPanel.loading(true);
            GoogleCalendarService.App.get().getAppointment(objectID, isCopy, new AbstractAsyncCallback<Appointment>() {
                @Override
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(Appointment result) {
                    LoadingPanel.loading(false);
                    item = result;
                    if (activityType != Appointment.CALL_LOG && item.getActivityType() == Appointment.CALL_LOG) {
                        activityType = item.getActivityType();
                        initCallTypePanel();
                    }
                    enableOrDisableFields((item.getAsteriskid() == null || item.getAsteriskid().trim().isEmpty()) && (item.getTwilioCallSID() == null || item.getTwilioCallSID().trim().isEmpty()));
                    fillFields();

                }
            });
            calendarService.getReminders(objectID, true, new AbstractAsyncCallback<ArrayList<CalendarEventReminder>>() {
                @Override
                public void failure(Throwable caught) {
                }

                @Override
                public void success(ArrayList<CalendarEventReminder> result) {
                    if (result != null && !result.isEmpty()) {
                        reminders.clear();
                        for (CalendarEventReminder aResult : result) {
                            reminders.addWidgets(getReminderWidgets(aResult));
                        }
                    }
                }
            });

        }
    }

    private void fillFields() {
        subject.setText(item.getSubject());
        template.setSelected(item.getTemplate());
        allDay.setValue(item.isAllDay(), true);
        if (item.getStartDate() != null) {
            startDate.setDate(item.getStartDate());
            from.setValue(new int[]{item.getStartDate().getHours(), item.getStartDate().getMinutes()});
        }
        if (item.getEndDate() != null) {
            endDate.setDate(item.getEndDate());
            to.setValue(new int[]{item.getEndDate().getHours(), item.getEndDate().getMinutes()});
        }
        description.setData(item.getDescription());
        if (item.getAttendees() != null && item.getAttendees().size() > 0) {
            ArrayList<SelectItem> items = new ArrayList<>();
            item.getAttendees().forEach(attendee -> items.add(new SelectItem(attendee.getID(), attendee.getName())));
            assignee.setSelectedItems(items);
        } else {
            assignee.selectCurrentUser();
        }
        if (item.getGuests() != null && item.getGuests().size() > 0) {
            guestTable.clear();

            for (SelectItem guest : item.getGuests()) {
                guestTable.addWidgets(getGuestWidget(guest));
            }
        }
        if (item.getRecurrenceJobItem() != null) {
            enableEmailReminder.setValue(true, true);
            recurringWidget.setData(item.getRecurrenceJobItem());
        } else {
            recurringWidget.setVisible(false);
        }
        if (item.getReminder() != null && item.getReminder().size() > 0) {
            reminders.clear();
            for (CalendarEventReminder reminder : item.getReminder()) {
                reminders.addWidgets(getReminderWidgets(reminder));
            }
        }


        link.setBadgeCount(item.getRelations().size());

        getCustomFieldUtil().fillCustomFieldsWithData(item.getCustomFieldItems());

        if (activityType == Appointment.CALL_LOG) {
            inbound.setValue(item.isInboundCall());
            outbound.setValue(item.isOutboundCall());
            missed.setValue(item.isMissedCall());
            inbound.setEnabled(false);
            outbound.setEnabled(false);
            missed.setEnabled(false);
            subject.setEnabled(false);
            template.setEnabled(false);

            currentCall.setValue(item.isCurrentCall());
            completedCall.setValue(item.isComplatedCall());
            scheduleCall.setValue(item.isScheduleCall());

            currentCall.setEnabled(false);
            completedCall.setEnabled(false);
            scheduleCall.setEnabled(false);

            durationMin.setText(item.getCallDuration() > 0 ? (item.getCallDuration() / 60) + "" : null);
            durationSec.setText(item.getCallDuration() > 0 ? (item.getCallDuration() % 60) + "" : null);

            startDate.setEnabled(false);
            from.setEnabled(false);
            endDate.setEnabled(false);
            to.setEnabled(false);
            allDay.setEnabled(false);
            durationMin.setEnabled(false);
            durationSec.setEnabled(false);

            if (currentCall.getValue()) {
                outbound.setValue(true);
                save.setVisible(false);
                remindersWidget.setVisible(false);
                remindersWidget.setLabel(null);
                whenPanel.clear();
                whenPanel.add(new GRow(new GColumn(GColumnEnum.COL_6, new InputGroup(startDate, from))));
            }
            if (completedCall.getValue()) {
                durationMin.setEnabled(true);
                durationSec.setEnabled(true);
                startDate.setEnabled(true);
                remindersWidget.setVisible(false);
                remindersWidget.setLabel(null);
                whenPanel.clear();
                whenPanel.add(new GRow(new GColumn(GColumnEnum.COL_8, new InputGroup(startDate, from)), new GColumn(GColumnEnum.COL_2, durationMin), new GColumn(GColumnEnum.COL_2, durationSec)));
                save.setVisible(true);
            }
            if (scheduleCall.getValue()) {
                startDate.setEnabled(true);
                from.setEnabled(true);
                save.setVisible(true);
                whenPanel.clear();
                whenPanel.add(new GRow(new GColumn(GColumnEnum.COL_6, new InputGroup(startDate, from))));
            }
            if (!currentCall.getValue() && !completedCall.getValue() && !scheduleCall.getValue()) {
                save.setVisible(true);
            }
        }
    }

    private void initCloneTimeWidgets() {
        startDateClone = new DatePicker();
        startDateClone.setDate(new Date());
        startDateClone.ensureDebugId(this.test_code_ID_name + "startDateClone");
        startDateClone.addChangeHandler(changeEvent -> {
            if (startDateClone.getDate().after(endDateClone.getDate())) {
                endDateClone.setDate(startDateClone.getDate());
            }
        });
        endDateClone = new DatePicker();
        endDateClone.setDate(new Date());
        endDateClone.ensureDebugId(this.test_code_ID_name + "endDateClone");
        fromClone = new KpiTimePicker(true);
        fromClone.setMarginTop(0);
        fromClone.setValue(KpiTimePicker.getHoursAndMinutes(new Date()));
        fromClone.setPaddingLeft(8);
        fromClone.setWidth("25%");
        fromClone.setStyleName("timepicker input-group-content");
        fromClone.ensureDebugId(this.test_code_ID_name + "fromClone");
        fromClone.setChangeCommand(() -> {
            if (fromClone.getValue() != null) {
                int[] fromtime = fromClone.getValue();
                int hour = fromtime[0];
                int minutes = fromtime[1];
                toClone.setValue(getToTime(hour, minutes));
            }
        });
        toClone = new KpiTimePicker(true);
        toClone.setMarginTop(0);
        int hour = new Date().getHours();
        int minutes = new Date().getMinutes();
        toClone.setValue(getToTime(hour, minutes));
        toClone.setWidth("25%");
        toClone.setPaddingLeft(8);
        toClone.setStyleName("timepicker input-group-content");
        toClone.ensureDebugId(this.test_code_ID_name + "toClone");
        allDayClone = new KpiSwitcher();
        allDayClone.setTooltip(wfmStrings.allDay());
        allDayClone.addValueChangeHandler(event -> drawClonePanel(event.getValue()));
        allDayClone.setValue(false, true);
        allDayClone.ensureDebugId(this.test_code_ID_name + "allDayClone");
        drawClonePanel(false);
    }

    private int[] getToTime(int hour, int minutes) {
        int[] result = new int[2];
        if (minutes < 30) {
            minutes += 30;
        } else {
            minutes = 30 - (60 - minutes);
            hour++;
        }
        result[0] = hour;
        result[1] = minutes;
        return result;
    }

    private void drawClonePanel(boolean isAllDay) {
        cloneWidget.clear();
        if (this.activityType != Appointment.CALL_LOG) {
            if (isAllDay) {
                cloneWidget.add(new GRow(new GColumn(GColumnEnum.COL_5, startDateClone),
                        new GColumn(GColumnEnum.COL_5, endDateClone),
                        new GColumn(GColumnEnum.COL_2, allDayClone)));
            } else {
                cloneWidget.add(new GRow(new GColumn(GColumnEnum.COL_5, new InputGroup(startDateClone, fromClone)),
                        new GColumn(GColumnEnum.COL_5, new InputGroup(endDateClone, toClone)),
                        new GColumn(GColumnEnum.COL_2, allDayClone)));
            }
        } else {
            cloneWidget.add(new GRow(new GColumn(GColumnEnum.COL_6, new InputGroup(startDateClone, fromClone))));
        }
    }

    private void enableOrDisableFields(boolean enabled) {
        subject.setEnabled(enabled);
        template.setEnabled(enabled);
        startDate.setEnabled(enabled);
        endDate.setEnabled(enabled);
//        assignee.setEnabled(enabled);
        enableEmailReminder.setEnabled(enabled);
        recurringWidget.setEnabled(enabled);
        link.setEnabled(enabled);
        if (activityType == Appointment.CALL_LOG) {
            inbound.setEnabled(enabled);
            outbound.setEnabled(enabled);
            missed.setEnabled(enabled);
            currentCall.setEnabled(enabled);
            completedCall.setEnabled(enabled);
            scheduleCall.setEnabled(enabled);
            durationMin.setEnabled(enabled);
            durationSec.setEnabled(enabled);
        }
        from.setEnabled(enabled);
        to.setEnabled(enabled);
        allDay.setEnabled(enabled);
        guestTable.setViewMode(!enabled);
        reminders.setViewMode(!enabled);
        for (HashMap<String, Widget> widgetMap : reminders.getWidgets()) {
            Reminder reminder_ = (Reminder) widgetMap.get("reminder");
            reminder_.setEnabled(enabled);
        }
        for (Map<String, Widget> emailRow : guestTable.getWidgets()) {
            ContactLookUp value = (ContactLookUp) emailRow.get(MultiTableNewUI.LOOK_UP_BOX);
            value.setEnabled(enabled);
        }
    }

    public boolean validate() {
        clearErrorStyle();
        int errors = customValidate();
        boolean dateValid = true;
        boolean scheduleCallValid = true;
        boolean complatedCallValid = true;
        startDate.removeStyleName(Constants.ERROR_FORM_STYLE);
        endDate.removeStyleName(Constants.ERROR_FORM_STYLE);
        from.removeStyleName(Constants.ERROR_FORM_STYLE);
        to.removeStyleName(Constants.ERROR_FORM_STYLE);

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUBJECT) != null) {
            if (formPropertyMap.get(CustomFormConstants.SUBJECT).isRequired()) {
                errors += markAsError(CustomFormConstants.SUBJECT, subject, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.SUBJECT).isChanged() ?
                        formPropertyMap.get(CustomFormConstants.SUBJECT).getTitle() : wfmStrings.subject(), subject, formPropertyMap.get(CustomFormConstants.SUBJECT).getMinChar()));
            }
        } else {
            errors += markAsError(CustomFormConstants.SUBJECT, subject, Utils.isNullOrEmpty(subject.getText()));
        }

        if (this.activityType != Appointment.CALL_LOG) {
            if (allDay.getValue()) {
                if (endDate.getDate().before(startDate.getDate()) && !DateUtils.areOnTheSameDay(startDate.getDate(), endDate.getDate())) {
                    startDate.addStyleName(Constants.ERROR_FORM_STYLE);
                    endDate.addStyleName(Constants.ERROR_FORM_STYLE);
                    dateValid = false;
                }
            } else {
                if (DateUtils.areOnTheSameDay(startDate.getDate(), endDate.getDate())) {
                    Integer fromHour = from.getValue()[0];
                    Integer fromMinute = from.getValue()[1];
                    Integer toHour = to.getValue()[0];
                    Integer toMinute = to.getValue()[1];
                    if ((fromHour.equals(toHour) && fromMinute >= toMinute) || fromHour > toHour) {
                        from.addStyleName(Constants.ERROR_FORM_STYLE);
                        to.addStyleName(Constants.ERROR_FORM_STYLE);
                        dateValid = false;
                    }
                } else if (endDate.getDate().before(startDate.getDate())) {
                    startDate.addStyleName(Constants.ERROR_FORM_STYLE);
                    endDate.addStyleName(Constants.ERROR_FORM_STYLE);
                    dateValid = false;
                }
            }
        } else {
            durationMin.removeStyleName(Constants.ERROR_FORM_STYLE);
            durationSec.removeStyleName(Constants.ERROR_FORM_STYLE);

            if (scheduleCall != null && scheduleCall.getValue()) {
                if (objectID == null) {
                    firstClick.set(false);
                }
                boolean scheduleCallRelationValid = false;
                if (startDate.getDate().before(new Date())) {
                    startDate.addStyleName(Constants.ERROR_FORM_STYLE);
                    from.addStyleName(Constants.ERROR_FORM_STYLE);
                    scheduleCallValid = false;
                }

                if (firstClick.get()) {
                    for (RelationItem relationItem : item.getRelations()) {
                        if (relationItem != null && (RelationItem.TYPE_LEAD.equals(relationItem.getToType()) || RelationItem.TYPE_CONTACT.equals(relationItem.getToType()))) {
                            scheduleCallRelationValid = true;
                            break;
                        }
                    }
                } else {
                    for (RelationItem relationItem : getLinkingUtil().getAddLinkSideNavBox().getSelectedRelations()) {
                        if ((relationItem != null && (RelationItem.TYPE_LEAD.equals(relationItem.getToType()) || RelationItem.TYPE_CONTACT.equals(relationItem.getToType()))) ||
                                (getLinkingUtil().getAddLinkSideNavBox().getSelectedRelations().size() != 0 && (RelationItem.TYPE_LEAD.equals(relationItem.getToType()) || RelationItem.TYPE_CONTACT.equals(relationItem.getToType())))) {
                            scheduleCallRelationValid = true;
                        }
                    }
                }
                if (!scheduleCallRelationValid) {
                    if (firstClick.get()) {
                        getLinkingUtil().getAddLinkSideNavBox();
                        getLinkingUtil().getAddLinkSideNavBox().setSelectedRelations(item.getRelations(), false);
                        firstClick.set(false);
                    } else {
                        getLinkingUtil().getAddLinkSideNavBox().show();
                    }
                    Info.warn(wfmStrings.scheduleCallRelationValid());
                    return false;
                }
            }
            if (completedCall != null && completedCall.getValue()) {
                if (startDate.getDate().after(new Date())) {
                    startDate.addStyleName(Constants.ERROR_FORM_STYLE);
                    from.addStyleName(Constants.ERROR_FORM_STYLE);
                    complatedCallValid = false;
                }
                if (!Validation.validateTextBoxRequired(durationMin)) {
                    errors++;
                }
            }
            if (objectID == null) {
                if (clone.getValue()) {
                    startDateClone.removeStyleName(Constants.ERROR_FORM_STYLE);
                    endDateClone.removeStyleName(Constants.ERROR_FORM_STYLE);
                    fromClone.removeStyleName(Constants.ERROR_FORM_STYLE);
                    toClone.removeStyleName(Constants.ERROR_FORM_STYLE);
                    if (allDayClone.getValue()) {
                        if (endDateClone.getDate().before(startDateClone.getDate()) && !DateUtils.areOnTheSameDay(startDateClone.getDate(), endDateClone.getDate())) {
                            startDateClone.addStyleName(Constants.ERROR_FORM_STYLE);
                            endDateClone.addStyleName(Constants.ERROR_FORM_STYLE);
                            dateValid = false;
                        }
                    } else {
                        if (DateUtils.areOnTheSameDay(startDateClone.getDate(), endDateClone.getDate())) {
                            Integer fromHour = fromClone.getValue()[0];
                            Integer fromMinute = fromClone.getValue()[1];
                            Integer toHour = toClone.getValue()[0];
                            Integer toMinute = toClone.getValue()[1];
                            if ((fromHour.equals(toHour) && fromMinute >= toMinute) || fromHour > toHour) {
                                fromClone.addStyleName(Constants.ERROR_FORM_STYLE);
                                toClone.addStyleName(Constants.ERROR_FORM_STYLE);
                                dateValid = false;
                            }
                        } else if (endDateClone.getDate().before(startDateClone.getDate())) {
                            startDateClone.addStyleName(Constants.ERROR_FORM_STYLE);
                            endDateClone.addStyleName(Constants.ERROR_FORM_STYLE);
                            dateValid = false;
                        }
                    }
                }
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION) != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION).isRequired()) {
            errors += markAsError(CustomFormConstants.DESCRIPTION, description, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.DESCRIPTION).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.DESCRIPTION).getTitle() : wfmStrings.description(), subject, formPropertyMap.get(CustomFormConstants.DESCRIPTION).getMinChar()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SHARED_WITH) != null) {
            if (formPropertyMap.get(CustomFormConstants.SHARED_WITH).isRequired()) {
                errors += markAsError(CustomFormConstants.SHARED_WITH, assignee, assignee.getSelectedItems() == null || assignee.getSelectedItems().size() == 0);
            }
        } else {
            errors += markAsError(CustomFormConstants.SHARED_WITH, assignee, assignee.getSelectedItems() == null || assignee.getSelectedItems().size() == 0);
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CUSTOM_HTML_TEMPLATE) != null) {
            if (formPropertyMap.get(CustomFormConstants.CUSTOM_HTML_TEMPLATE).isRequired()) {
                errors += markAsError(CustomFormConstants.CUSTOM_HTML_TEMPLATE, template, template.getSelectedItem() == null);
            }
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.GUESTS) != null && formPropertyMap.get(CustomFormConstants.GUESTS).isRequired()) {

            if (guestTable.getWidgets() != null && guestTable.getWidgets().get(0).size() > 0) {
                ContactLookUp value = (ContactLookUp) guestTable.getWidgets().get(0).get(MultiTableNewUI.LOOK_UP_BOX);
                if (value.getSelectedItem() != null && !Validation.validateLookUpRequired(value)) {
                    errors++;
                }
            }
        } else {
            if (guestTable.getWidgets().size() > 0) {
                for (Map<String, Widget> emailRow : guestTable.getWidgets()) {
                    ContactLookUp value = (ContactLookUp) emailRow.get(MultiTableNewUI.LOOK_UP_BOX);
                    if (value.getSelectedItem() != null && !Validation.validateLookUpRequired(value)) {
                        errors++;
                    }
                }
            }
        }
        if (enableEmailReminder.getValue() && !recurringWidget.validate()) {
            errors++;
        }
        boolean candidateNotAdded = false;
        if (activityType == Appointment.INTERVIEW) {
            if (objectID == null) {
                firstClick.set(false);
            }
            if (objectID != null && firstClick.get() && (item.getRelations() == null || (item.getRelations() != null && item.getRelations().size() == 0))) {
                candidateNotAdded = true;
            } else if (!firstClick.get() && (getLinkingUtil().getAddLinkSideNavBox().getSelectedRelations() == null || (getLinkingUtil().getAddLinkSideNavBox().getSelectedRelations() != null && getLinkingUtil().getAddLinkSideNavBox().getSelectedRelations().size() == 0))) {
                candidateNotAdded = true;
            } else if (objectID == null && firstClick.get()) {
                candidateNotAdded = relationItems.isEmpty();
            } else if (objectID == null) {
                ArrayList<Integer> ids = RelationItem.getRelatedIDs(getLinkingUtil().getAddLinkSideNavBox().getSelectedRelations(), RelationItem.TYPE_CANDIDATE);
                if (ids == null || ids.size() == 0) {
                    candidateNotAdded = true;
                }
            }
            if (!candidateNotAdded) {
                errors++;
                if (getLinkingUtil().getAddLink() != null) {
                    getLinkingUtil().getAddLink().addStyleName(Constants.ERROR_FORM_STYLE);
                }
            }
        }
        errors += getCustomFieldUtil().validateCustomFields();
        if (errors > 0) {
//            if (candidateNotAdded) {
//            Info.warn(wfmMessages.pleaseSelect(wfmStrings.sureEnteredAllData()));
//            } else {
            Info.warn(wfmStrings.sureEnteredAllData());
            return false;
        } else if (!dateValid) {
            Info.warn(wfmStrings.pleaseChooseValidDate());
            return false;
        } else if (!complatedCallValid) {
            Info.warn(wfmStrings.callStartTimeShould());
            return false;
        } else if (!scheduleCallValid) {
            Info.warn(wfmStrings.scheduleCallValid());
            return false;
        }
        return true;
    }

    private void setValues() {
        Date start = startDate.getDate();
        Date end = endDate.getDate();

        item = new Appointment();
        item.setActivityType(activityType);
        item.setSubject(subject.getText());
        item.setTemplateID(template.getSelectedId());
        item.setCopy(isCopy);
        item.setObjectID(objectID);

        if (this.activityType == Appointment.CALL_LOG) {
            item.setCurrentCall(currentCall.getValue());
            item.setComplatedCall(completedCall.getValue());
            item.setScheduleCall(scheduleCall.getValue());
            item.setInboundCall(inbound.getValue());
            item.setOutboundCall(outbound.getValue());
            item.setMissedCall(missed.getValue());
            if (completedCall != null && completedCall.getValue()) {
                start = new Date(startDate.getDate().getYear(), startDate.getDate().getMonth(), startDate.getDate().getDate(),
                        from.getValue()[0],
                        from.getValue()[1], 0);
                long duration = 0;
                if (durationMin.getText().trim().length() > 0) {
                    duration = duration + Long.valueOf(durationMin.getText()) * 60;
                }
                if (durationSec.getText().trim().length() > 0) {
                    duration = duration + Long.valueOf(durationSec.getText());
                }
                item.setCallDuration(duration);
            } else if (scheduleCall.getValue()) {
                start = new Date(startDate.getDate().getYear(), startDate.getDate().getMonth(), startDate.getDate().getDate(),
                        from.getValue()[0],
                        from.getValue()[1], 0);
                end = start;
            }
        } else {
            if (!allDay.getValue()) {
                start = new Date(startDate.getDate().getYear(), startDate.getDate().getMonth(), startDate.getDate().getDate(),
                        allDay.getValue() ? 0 : from.getValue()[0],
                        allDay.getValue() ? 0 : from.getValue()[1], 0);

                end = new Date(endDate.getDate().getYear(), endDate.getDate().getMonth(), endDate.getDate().getDate(),
                        allDay.getValue() ? 59 : to.getValue()[0],
                        allDay.getValue() ? 59 : to.getValue()[1], 59);
            }
        }

        item.setAttachments(attachment.getAttachedFiles());

        item.setAllDay(allDay.getValue());
        item.setStartDate(start);
        item.setEndDate(end);
        item.setDescription(description.getData());
        ArrayList<Attendee> attendees = new ArrayList<>();
        assignee.getSelectedItems().forEach(attendee -> attendees.add(new Attendee(attendee.getId(), true)));
        item.setAttendees(attendees);
        for (Map<String, Widget> emailRow : guestTable.getWidgets()) {
            ContactLookUp value = (ContactLookUp) emailRow.get(MultiTableNewUI.LOOK_UP_BOX);
//            item.getGuests().clear();
            if (value.getSelectedItem() != null && !wfmStrings.email().equals(value.getSelectedItem().getName())) {
                item.getGuests().add(new SelectItem(null, value.getSelectedItem() != null ? value.getSelectedItem().getName() : null));
            }
        }
        item.setSendEmailNotification(item.getGuests() != null && item.getGuests().size() > 0);

        item.setRecurrenceJobItem(null);
        if (enableEmailReminder.getValue()) {
            item.setRecurrenceJobItem(recurringWidget.getData());
        }
        if (firstClick.get()) {
            if (relationID != null && relationType != null) {
                item.setRelations(relationItems);
            } else {
                item.setRelations(item.getRelations());
            }
        } else {
            item.setRelations(getLinkingUtil().getAddLinkSideNavBox().getSelectedRelations());
        }

        item.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());
        item.setRegisterNestedWorkflowEvents(false);
        ArrayList<CalendarEventReminder> eventReminders = new ArrayList<>();
        boolean isReminderData = false;
        for (HashMap<String, Widget> widgetMap : reminders.getWidgets()) {
            Reminder reminder_ = (Reminder) widgetMap.get("reminder");
            if (reminder_.getReminderData().getValue() != null && reminder_.getReminderData().getReminderTimes() != null) {
                eventReminders.add(reminder_.getReminderData());
                isReminderData = true;
            }
        }
        if (isReminderData) {
            item.setReminder(eventReminders);
        }
        item.setCreatedFrom(Utils.isHRMS() ? Appointment.FROM_HRMS : Appointment.FROM_CRM);
    }

    private void save() {
        save.setEnabled(false);
        if (!validate()) {
            save.setEnabled(true);
            return;
        }
        setValues();
        if (handler != null) {
            checkForHolidayAndSave();
        } else {
            ArrayList<Attendee> attendeeList = new ArrayList<>();
            Attendee attendee = new Attendee();
            attendee.setID(Utils.getUserID());
            attendeeList.add(attendee);
            Date start = new Date(startDate.getDate().getYear(), startDate.getDate().getMonth(), startDate.getDate().getDate(), from.getValue()[0], from.getValue()[1]);
            Date end = new Date(startDate.getDate().getYear(), startDate.getDate().getMonth(), startDate.getDate().getDate(), to.getValue()[0], to.getValue()[1]);
            LoadingPanel.loading(true, panel);
            calendarService.isAssigneeOnHoliday(attendeeList, start, end, false, new AbstractAsyncCallback<String>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false, panel);
                    save.setEnabled(true);
                }

                public void success(String result) {
                    LoadingPanel.loading(false, panel);
                    save.setEnabled(true);
                    if (!Utils.isNullOrEmpty(result)) {
                        WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.CONFIRM, Action.YesNo, wfmStrings.youHaveHolidayOnDate(), new com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler() {
                            @Override
                            public void onSubmit() {
                                saveCalendarEvent();
                            }
                        });
                        wfmMessageBox.setTitle(wfmStrings.confirmation());
                        wfmMessageBox.open();
                    } else {
                        saveCalendarEvent();
                    }
                }
            });
        }
    }

    private void checkForHolidayAndSave() {
        Date startDate = item.getStartDate();
        Date endDate = item.getEndDate();
        LoadingPanel.loading(true, panel);
        if (!item.isMissedCall()) {
            calendarService.isAssigneeOnHoliday(item.getAttendees(), startDate, endDate, false, new AbstractAsyncCallback<String>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false, panel);
                }

                public void success(String result) {
                    LoadingPanel.loading(false, panel);
                    if (!Utils.isNullOrEmpty(result)) {
                        WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.CONFIRM, Action.YesNo, wfmStrings.followingEmployees() + result + " " + wfmStrings.haveHolidayOnDateUpdate(), new com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler() {
                            @Override
                            public void onSubmit() {
                                saveCalendarEvent();
                            }
                        });
                        wfmMessageBox.setTitle(wfmStrings.confirmation());
                        wfmMessageBox.open();
                    } else {
                        saveCalendarEvent();
                    }
                }
            });
        } else {
            saveCalendarEvent();
        }
    }

    private void saveCalendarEvent() {
        LoadingPanel.loading(true, panel);
        calendarService.saveCalendarEvent(null, item, false, new AbstractAsyncCallback<SelectItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false, panel);
            }

            public void success(SelectItem event) {
                LoadingPanel.loading(false, panel);
                closeTab();
                Info.show(Property.get(Constants.EVENT_LIST, wfmStrings.messSuccessfullyUpdated(), wfmStrings.event()));
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CALENDAR_EVENT_ADD, event != null ? event.getId() : null, EditEventView.this);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CRM_EVENT_ADD_EDIT, event != null ? event.getId() : null, EditEventView.this);
            }
        });
    }

    @Override
    protected String getFormID() {
        return Appointment.CALL_LOG == activityType ? LayoutRPC.LOGACALL_FORM : LayoutRPC.ACTIVITY_FORM;
    }

    @Override
    protected String getFormType() {
        return objectID != null ? LayoutRPC.EDIT : LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    HasLinks linkingUtil;

    @Override
    public HasLinks getLinkingUtil() {
        if (linkingUtil == null) {
            linkingUtil = new HasLinks(EditEventView.this) {
                @Override
                protected boolean isActionEditing() {
                    return true;
                }

                @Override
                public Integer getRelationID() {
                    return objectID != null ? objectID : null;
                }

                @Override
                public String getRelationType() {
                    return RelationItem.TYPE_EVENT;
                }

                @Override
                public String getRelationName() {
                    return item != null ? item.getSubject() : null;
                }

                @Override
                public boolean hasNoSummaryTab() {
                    return true;
                }
            };
        }
        return linkingUtil;
    }

    FormHasCustomField customFieldUtil;

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    private void validateMinute(TextBox textBox) {
        textBox.addKeyPressHandler(event -> {
            char key = event.getCharCode();
            if (Utils.isArabicLanguage()) {
                return;
            }

            if (key == (char) 0) {
                return;
            }

            if (!Character.isDigit(key) && key != (char) KeyCodes.KEY_DELETE
                    && key != (char) KeyCodes.KEY_BACKSPACE && key != (char) KeyCodes.KEY_BACKSPACE
                    && key != (char) KeyCodes.KEY_LEFT && key != (char) KeyCodes.KEY_RIGHT
                    && key != (char) KeyCodes.KEY_HOME && key != (char) KeyCodes.KEY_END
                    && key != (char) KeyCodes.KEY_ENTER && key != (char) KeyCodes.KEY_DOWN
                    && key != (char) KeyCodes.KEY_UP && key != (char) KeyCodes.KEY_TAB) {
                ((TextBox) event.getSource()).cancelKey();
            }
            if (textBox.getText() != null && textBox.getText().indexOf('.') != -1 && key == '.') {
                ((TextBox) event.getSource()).cancelKey();
            }
            if (textBox.getText() != null && key == '\'') {
                ((TextBox) event.getSource()).cancelKey();
            }
            if (key == '.') {
                ((TextBox) event.getSource()).cancelKey();
            }

            if (Character.isDigit(key)) {
                boolean isTrue = Integer.valueOf(textBox.getValue() + key).compareTo(59) <= 0;
                if (!isTrue) {
                    ((TextBox) event.getSource()).cancelKey();
                }
            }
        });
    }

    private void setDefaultValuesByFormProperty() {
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SUBJECT) != null && formPropertyMap.get(CustomFormConstants.SUBJECT).getDefaultValue() != null && formPropertyMap.get(CustomFormConstants.SUBJECT).getDefaultValue().length() > 0) {
            subject.setText(formPropertyMap.get(CustomFormConstants.SUBJECT).getDefaultValue());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.CUSTOM_HTML_TEMPLATE) != null && formPropertyMap.get(CustomFormConstants.CUSTOM_HTML_TEMPLATE).getDefaultValue() != null && formPropertyMap.get(CustomFormConstants.CUSTOM_HTML_TEMPLATE).getSelectedId() != null) {
            template.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.CUSTOM_HTML_TEMPLATE).getSelectedId(), formPropertyMap.get(CustomFormConstants.CUSTOM_HTML_TEMPLATE).getDefaultValue()));
            setSubjectAndDescription(formPropertyMap.get(CustomFormConstants.CUSTOM_HTML_TEMPLATE).getSelectedId());
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION) != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION).getDefaultValue() != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION).getDefaultValue().length() > 0) {
            description.setData(formPropertyMap.get(CustomFormConstants.DESCRIPTION).getDefaultValue());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.SHARED_WITH).getDefaultValue() != null && formPropertyMap.get(CustomFormConstants.SHARED_WITH).getSelectedId() != null) {
            assignee.setSelectedItems(new SelectItem(formPropertyMap.get(CustomFormConstants.SHARED_WITH).getSelectedId(), formPropertyMap.get(CustomFormConstants.SHARED_WITH).getDefaultValue()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.GUESTS) != null && formPropertyMap.get(CustomFormConstants.GUESTS).getDefaultValue() != null && guestTable.getWidgets() != null && guestTable.getWidgets().get(0) != null) {
            ContactLookUp value = (ContactLookUp) guestTable.getWidgets().get(0).get(MultiTableNewUI.LOOK_UP_BOX);
            if (value != null) {
                value.setSelected(formPropertyMap.get(CustomFormConstants.GUESTS).getDefaultValue());
            }

        }
    }

    private void setSubjectAndDescription(Integer templateID) {
        if (templateID != null) {
            LoadingPanel.loading(true);
            EmployeeListItem item = new EmployeeListItem();
            item.setObjectID(relationID);
            CRMService.App.get().generateEmployeeEventTemplate(templateID, item, new AbstractAsyncCallback<LinkedHashMap<String, String>>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(LinkedHashMap<String, String> subjectWithContent) {
                    LoadingPanel.loading(false);
                    for (String key : subjectWithContent.keySet()) {
                        subject.setText(key);
                        description.setData(subjectWithContent.get(key));
                    }
                }
            });
        }
    }

    private void getRelationName(final Integer relationID, final String relType) {
        AllInOneService.App.get().getRelationName(relationID, relType, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    relationName = result;
                }
            }
        });
    }
}
