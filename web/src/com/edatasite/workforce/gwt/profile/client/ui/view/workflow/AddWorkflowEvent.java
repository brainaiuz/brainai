package com.edatasite.workforce.gwt.profile.client.ui.view.workflow;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelField;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelForm;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowRule;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomFieldInterface;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.googlecalendar.client.rpc.GoogleCalendarService;
import com.edatasite.workforce.gwt.googlecalendar.client.rpc.GoogleCalendarServiceAsync;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileServiceAsync;
import com.edatasite.workforce.gwt.profile.client.ui.view.WorkflowDateSelecter;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialPanel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Hayot on 5/20/2014.
 */
public class AddWorkflowEvent extends CustomForm2 implements Constants, FormHasCustomFieldInterface, Colapse {
    private final SettingStrings settingsStrings = SettingStrings.App.get();
    private final GoogleCalendarServiceAsync calendarService = GoogleCalendarService.App.get();
    protected static final ProfileServiceAsync profileService = ProfileService.App.get();
    private Appointment appointment;
    private int activityType;
    private final LinkedHashMap<String, ModelField> fields = new LinkedHashMap<>();

    private final Integer objectID;
    private TextBox what;
    private WorkflowDateSelecter when;
    private KpiSwitcher workflowTimeBasedAction;
    private WorkflowDateSelecter workflowTimeBasedActionDate;
    private TextBox where;
    private TextArea description;
    private DataListBox callType;
    private final Integer workflowID;
    private boolean isCallLog;
    private MaterialPanel descriptionPanel;
    private DataListBox fieldCodes;
    private HTML attribute;

    public AddWorkflowEvent(Integer objectID, Integer workflowID) {
        super("workflowEvent", Property.get(Constants.EVENT_LIST, wfmStrings.workflowEvent(), wfmStrings.event()));
        this.objectID = objectID;
        this.workflowID = workflowID;
        this.appointment = new Appointment(DateUtil.getDateTime());
        this.activityType = Appointment.EVENT;
    }

    public AddWorkflowEvent(Integer id, Integer workflowID, boolean isCallLog) {
        this(id, workflowID);
        this.isCallLog = isCallLog;
        this.activityType = isCallLog ? Appointment.CALL_LOG : Appointment.EVENT;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        return null;
    }

    protected void registerFields() {
        what = new TextBox();
        when = new WorkflowDateSelecter(true, false);
        workflowTimeBasedAction = new KpiSwitcher();
        workflowTimeBasedAction.getElement().getStyle().setMarginTop(5, Style.Unit.PX);
        workflowTimeBasedActionDate = new WorkflowDateSelecter(false, true);
        workflowTimeBasedActionDate.setVisible(false);
        workflowTimeBasedAction.addValueChangeHandler(booleanValueChangeEvent -> workflowTimeBasedActionDate.setVisible(booleanValueChangeEvent.getValue()));

        where = new TextBox();
        callType = new DataListBox();
        SelectItem inbound = new SelectItem(0, wfmStrings.inbound());
        SelectItem outbound = new SelectItem(1, wfmStrings.outbound());
        SelectItem[] items = new SelectItem[2];
        items[0] = inbound;
        items[1] = outbound;
        callType.setItems(items);
        callType.setSelected(-1);

        drawDescriptionPanel();

        addField(CustomFormConstants.SUBJECT, what);
        addField(CustomFormConstants.WHEN, when);
        addTitleField(WORKFLOW_TIME_BASED_HEADER, wfmStrings.timeBasedAction());
        addField(WORKFLOW_TIME_BASED, new InputGroup(workflowTimeBasedAction, workflowTimeBasedActionDate));
        addField(CustomFormConstants.WHERE, where);
        addField(CustomFormConstants.CALL_TYPE, callType);
        addField(CustomFormConstants.DESCRIPTION, descriptionPanel);
        show();
    }

    private void drawDescriptionPanel() {
        descriptionPanel = new MaterialPanel();

        MaterialPanel firstRow = new MaterialPanel("grid-row");

        MaterialPanel dropdownPanel = new MaterialPanel("col-9");
        MaterialPanel attributePanel = new MaterialPanel("col-3");
        firstRow.add(dropdownPanel);
        firstRow.add(attributePanel);

        fieldCodes = new DataListBox();
        fieldCodes.addValueChangeHandler(changeEvent -> {
            if (changeEvent.getValue() != null && changeEvent.getValue().getDescription() != null) {
                attribute.setHTML(changeEvent.getValue().getDescription());
            } else {
                attribute.setHTML(wfmStrings.noAttributesSelected());
            }
        });
        dropdownPanel.add(fieldCodes);

        attribute = new HTML(wfmStrings.noAttributesSelected());
        attribute.getElement().getStyle().setMarginTop(8, Style.Unit.PX);
        attribute.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
        attributePanel.add(attribute);

        MaterialPanel secondRow = new MaterialPanel("grid-row");
        secondRow.getElement().getStyle().setMarginTop(10, Style.Unit.PX);
        MaterialPanel contentPanel = new MaterialPanel("col-12");
        secondRow.add(contentPanel);

        description = new TextArea();
        description.setHeight(isCallLog ? "275px" : "180px");
        contentPanel.add(description);

        descriptionPanel.add(firstRow);
        descriptionPanel.add(secondRow);
    }

    @Override
    protected void initPredefinedValues() {
        addPredefinedValues(WORKFLOW_.START_TIME, WorkflowDateSelecter.START_DATE_ITEMS);
        addPredefinedValues(WORKFLOW_.DUE_GRANULARITY, WorkflowDateSelecter.DUE_GRANULARITY_ITEMS);
    }

    @Override
    protected void addButtons() {
        addButton(wfmStrings.save(), WfmButton2.BTN_PRIMARY, event -> save());
    }

    private void getEventData() {
        appointment.setActivityType(activityType);
        if (isCallLog) {
            appointment.setActivityType(Appointment.CALL_LOG);
            if (callType.getSelectedItem() != null) {
                appointment.setInboundCall(wfmStrings.inbound().equals(callType.getSelectedItem().getName()));
            }
        }
        appointment.setSubject(what.getText());
        appointment.setLocation(where.getText());
        appointment.setWorkflowStartDate(when.getWorkflowStartDate());
        appointment.setWorkflowStartDateAttributes(when.getWorkflowStartDateAttributes());
        appointment.setWorkflowDueDate(when.getWorkflowDueDateUnit());
        appointment.setWorkflowDueDateGranularity(when.getWorkflowDueDateGranularity());
        appointment.setWorkflowActionTimeBased(workflowTimeBasedAction.getValue());
        appointment.setWorkflowActionStartTime(workflowTimeBasedActionDate.getWorkflowStartDate());
        appointment.setWorkflowActionStartTimeUnit(workflowTimeBasedActionDate.getWorkflowDueDateUnit());
        appointment.setWorkflowActionStartTimeGranularity(workflowTimeBasedActionDate.getWorkflowDueDateGranularity());
        appointment.setAllDay(when.isAllDay());
        appointment.setDescription(description.getText());
        if (appointment.getCreatedBy() == null || "".equals(appointment.getCreatedBy())) {
            appointment.setCreatedBy(Utils.getFullName());
        }
        appointment.setMultiDay(appointment.isMultiDayAppointment());
        appointment.setStyle(isCallLog ? Appointment.AQUA : Appointment.BLUE);
        appointment.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());
        appointment.setAction(Appointment.ADD_NEW_EVENT);
        appointment.setWorkflowID(workflowID);
    }

    private void save() {
        if (!validate()) {
            Info.warn(wfmStrings.sureEnteredAllData());
            return;
        }
        getEventData();
        LoadingPanel.loading(true);
        enableButton(false);
        calendarService.saveCalendarEvent(null, appointment, false, new AbstractAsyncCallback<SelectItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                enableButton(true);
            }

            public void success(SelectItem event) {
                LoadingPanel.loading(false);
                Info.show(Property.get(Constants.EVENT_LIST, wfmStrings.messSuccessfullySaved(), wfmStrings.event()));
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CALENDAR_EVENT_ADD, event.getId(), AddWorkflowEvent.this);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CRM_EVENT_ADD_EDIT, event.getId(), AddWorkflowEvent.this);
                enableButton(true);
                closeTab();
            }
        });
    }

    private boolean validate() {
        int err = 0;
        when.removeStyleName(Constants.ERROR_FORM_STYLE);
        if (!Validation.validateTextBoxRequired(what)) {
            err++;
        }
        if (when.getWorkflowDueDateUnit() == -1 || when.getWorkflowDueDateGranularity() == null) {
            Info.warn(when.getWorkflowDueDateUnit() == -1 ? "Integers only allowed" : wfmStrings.pleaseEnterValue());
            when.addStyleName(Constants.ERROR_FORM_STYLE);
            err++;
        }
        if (when.getWorkflowStartDate().equals(WORKFLOW_START_TIME.BY_ATTRIBUTES) && (when.getWorkflowStartDateAttributes() == null || "".equals(when.getWorkflowStartDateAttributes()))) {
            Info.warn(wfmStrings.pleaseEnterValue());
            when.addStyleName(Constants.ERROR_FORM_STYLE);
            err++;
        }
        return err == 0;
    }

    @Override
    protected void getDataToFillFields() {
        if (objectID != null) {
            GoogleCalendarService.App.get().getAppointment(objectID, false, new AbstractAsyncCallback<Appointment>() {
                @Override
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(Appointment result) {
                    appointment = result;
                    activityType = appointment.getActivityType();
                    setAppointmentDetails();
                    initColumns(result.getWorkflowModule());
                    callType.setSelectedByValue(appointment.isInboundCall() ? wfmStrings.inbound() : wfmStrings.outbound());
                    getCustomFieldUtil().drawCustomFields(AddWorkflowEvent.this, objectID);
                    getCustomFieldUtil().fillCustomFieldsWithData(result.getCustomFieldItems());
                    LoadingPanel.loading(false);
                }
            });
        } else {
            profileService.getWorkflowRuleForEvent(workflowID, new AbstractAsyncCallback<WorkflowRule>() {
                @Override
                public void failure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(WorkflowRule result) {
                    if (result != null) {
                        initColumns(result.getModule());
                    }
                }
            });
            CommonService.App.get().getCompanyCustomFields(ViewName.Event, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
                @Override
                public void failure(Throwable throwable) {

                }

                @Override
                public void success(ArrayList<CompanyCustomFieldItem> result) {
                    if (result != null) {
                        getCustomFieldUtil().setCompanyCustomFieldItems(result);
                    }
                    getCustomFieldUtil().drawCustomFields(AddWorkflowEvent.this, objectID);
                }
            });

        }
    }

    private String getFormIDOfModule(String code) {
        if (code != null && !"".equals(code)) {
            if (WorkflowRule._WORKFLOW_MODULE_MANUAL_JOURNAL.equals(code)) {
                return LayoutRPC.MANUAL_JOURNAL_FORM;
            } else if (WorkflowRule._WORKFLOW_MODULE_TASK.equals(code)) {
                return LayoutRPC.TASK_MAX_FORM;
            }
            //dont' forget the code of the module reference must be build like this. "_WORKFLOW_MODULE_" + formID.replaceAll("_FORM", "");
            //for example :module for lead. "_WORKFLOW_MODULE_" + LEAD_FORM.replaceAll("_FORM", "") = "_WORKFLOW_MODULE_LEAD";
            //why we need formID? Because all fields for this form is in form(database.tablename = model).
            return code.replace("_WORKFLOW_MODULE_", "") + "_FORM";
        }
        return null;
    }

    private void initColumns(String code) {
        if (code != null) {
            service.getDefaultModelForm(getFormIDOfModule(code), new AsyncCallback<ModelForm>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(ModelForm modelForm) {
                    //show Only showInForm fields
                    fields.clear();
                    if (modelForm != null && modelForm.getFields().size() > 0) {
                        for (ModelField field : modelForm.getFields()) {
                            if (field.isEntityField()) {
                                fields.put(field.getField_ID(), field);
                            }
                        }
                    }
                    notifyAllFieldRelateds(modelForm.getAttributes());
                }
            });
        }
    }

    private void notifyAllFieldRelateds(SelectItem[] additionalAttributes) {
        SelectItem[] items = getColumnsAsReferenceItems(additionalAttributes);
        if (workflowTimeBasedActionDate != null) {
            workflowTimeBasedActionDate.setDateItems(getDateTypeColums());
        }
        fieldCodes.clear();
        fieldCodes.setItems(items);
    }

    private ArrayList<SelectItem> getDateTypeColums() {
        ArrayList<SelectItem> result = new ArrayList<>();
        int i = 4;
        if (fields != null && fields.size() > 0) {
            for (Map.Entry<String, ModelField> entry : fields.entrySet()) {
                if (entry.getValue().getType() != null && DATA_TYPE_DATE.equals(entry.getValue().getType())) {
                    String localized = getLocalizer().localizeByFieldID(entry.getValue().getForm_ID(), entry.getValue().getField_ID());
                    String name = localized != null ? localized : (entry.getValue().getField_ID().contains("string_value") || entry.getValue().getField_ID().contains("double_value") || entry.getValue().getField_ID().contains("date_value") ? entry.getValue().getLabel() : entry.getValue().getField_ID());
                    result.add(new SelectItem(i++, name, entry.getValue().getField_ID()));
                }
            }
        }
        result.sort(Comparator.comparing(SelectItem::getName));
        return result;
    }

    public SelectItem[] getColumnsAsReferenceItems(SelectItem[] additionalAttributes) {
        ArrayList<SelectItem> result = new ArrayList<>();
        if (fields != null && fields.size() > 0) {
            for (Map.Entry<String, ModelField> entry : fields.entrySet()) {
                String localized = getLocalizer().localizeByFieldID(entry.getValue().getForm_ID(), entry.getValue().getField_ID());
                String name = localized != null ? localized : (entry.getValue().getField_ID().contains("string_value") || entry.getValue().getField_ID().contains("double_value") || entry.getValue().getField_ID().contains("date_value") ? entry.getValue().getLabel() : entry.getValue().getField_ID());
                String description = entry.getValue().getField_ID() != null ? ("${" + entry.getValue().getField_ID().toLowerCase() + "}") : entry.getValue().getField_ID();
                result.add(new SelectItem(entry.getValue().getObjectID(), name, description));
            }
        }
        if (additionalAttributes != null && additionalAttributes.length > 0) {
            result.addAll(Arrays.asList(additionalAttributes));
        }
        result.sort(Comparator.comparing(SelectItem::getName));
        return result.toArray(new SelectItem[]{});
    }

    private void setAppointmentDetails() {
        if (appointment != null && appointment.getObjectID() != null) {
            what.setText(appointment.getSubject());
            callType.setSelectedByValue(appointment.isInboundCall() ? wfmStrings.inbound() : wfmStrings.outbound());
            when.setStartDate(appointment.getWorkflowStartDate());
            when.setStartDateAttributes(appointment.getWorkflowStartDateAttributes());
            when.setDueDate(appointment.getWorkflowDueDate());
            when.setDueDateGranularity(appointment.getWorkflowDueDateGranularity());
            if (appointment.isWorkflowActionTimeBased()) {
                workflowTimeBasedActionDate.setStartDate(appointment.getWorkflowActionStartTime());
                workflowTimeBasedActionDate.setDueDate(appointment.getWorkflowActionStartTimeUnit());
                workflowTimeBasedActionDate.setDueDateGranularity(appointment.getWorkflowActionStartTimeGranularity());
                workflowTimeBasedAction.setValue(true, true);
            }
            when.allDay.setValue(appointment.isAllDay(), true);
            where.setText(appointment.getLocation());
            description.setText(appointment.getDescription());
        }
    }

    @Override
    protected String getFormID() {
        return isCallLog ? LayoutRPC.WORKFLOW_CALL_LOG_FORM : LayoutRPC.WORKFLOW_EVENT_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;
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

    FormHasCustomField customFieldUtil;

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }
}
