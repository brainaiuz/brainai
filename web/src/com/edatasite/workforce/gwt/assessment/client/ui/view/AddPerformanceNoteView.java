package com.edatasite.workforce.gwt.assessment.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DateTimePicker;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.KpiDatePicker;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.form.InputGroup;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.lookup.EmployeeLookUpWithCode;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.rpc.PerformanceNoteItem;
import com.edatasite.workforce.gwt.issue.client.rpc.IssueService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;

import java.util.Date;
import java.util.LinkedHashMap;

/**
 * User: Sherzod
 * Date: May 12, 2009
 * Time: 8:03:26 PM
 */
public class AddPerformanceNoteView extends CustomForm2 implements Constants, Colapse {

    protected static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    protected PerformanceNoteItem performance_note_item;
    protected String info_error_message = wfmStrings.errorOccurredSavingChanges();
    protected String info_success_message = Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.note());

    protected DateTimePicker dateTime;
    protected TextArea2 performance_note_description;
    protected TextBox performance_note_name;
    protected KpiRadioButton performance_note_visibility_private;
    protected KpiRadioButton performance_note_visibility_public;
    protected EmployeeLookUpWithCode performance_note_related_employee;
    protected EmployeeLookUpWithCode performance_note_reported_by;
    protected DataListBox performance_note_resolver;
    protected DataListBox performance_note_status;
    protected DataListBox priorities;

    private Integer int_employeeID;
    protected Integer int_objectID;
    private String test_code_ID_name = "add_performance_note_view_";
    protected LinkedHashMap<String, FormProperty> formPropertyMap;
    private FormHasCustomField customFieldUtil;
    protected boolean isIncindent;


    public AddPerformanceNoteView() {
        super("performancenote", hrmsStrings.addNewNote());
    }

    public AddPerformanceNoteView(String name, String description, String test_code_ID_name, Integer int_objectID) {
        this(name, description, test_code_ID_name, int_objectID, null);
    }

    public AddPerformanceNoteView(String name, String description, String test_code_ID_name, Integer int_objectID, Integer int_employeeID) {
        super(name, description);
        this.test_code_ID_name = test_code_ID_name;
        this.int_objectID = int_objectID;
        this.int_employeeID = int_employeeID;
    }

    @Override
    public String getIconStyle() {
        return "hrms hrms-edit";
    }

    @Override
    protected void addButtons() {
        if (int_objectID == null) {
            //save & close logic
            MaterialLink save = new MaterialLink(wfmStrings.save());
            MaterialSplitButton splitButton = new MaterialSplitButton(save);
            save.addClickHandler(event -> save(true));
            save.ensureDebugId(test_code_ID_name + "save_and_close_button");

            //save & new logic
            MaterialLink saveAndNewButton = new MaterialLink(wfmStrings.saveAndNew());
            saveAndNewButton.addClickHandler(event -> save(false));
            saveAndNewButton.ensureDebugId(test_code_ID_name + "save_and_new_button");

            splitButton.addItem(saveAndNewButton);
            addButton(splitButton);
        } else {
            //update logic
            WfmButton2 updateButton = addButton(wfmStrings.update(), WfmButton2.BTN_PRIMARY, event -> {
                save(true);
            });
            updateButton.ensureDebugId(test_code_ID_name + "update_button");
        }
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        HrmsService.App.get().getPerformanceNote(int_objectID, new AbstractAsyncCallback<PerformanceNoteItem>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(PerformanceNoteItem result) {
                LoadingPanel.loading(false);
                performance_note_item = result;
                if (int_objectID == null) {
                    performance_note_item.setIncident(isIncident());
                    setDefaultValues();
                }
                fillFormWithData();
            }
        });
    }

    protected void setDefaultValuesByFormProperty() {
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NAME) != null && formPropertyMap.get(CustomFormConstants.NAME).getDefaultValue() != null) {
            performance_note_name.setText(formPropertyMap.get(CustomFormConstants.NAME).getDefaultValue());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION) != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION).getDefaultValue() != null) {
            performance_note_description.setText(formPropertyMap.get(CustomFormConstants.DESCRIPTION).getDefaultValue());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.RELATED_EMPLOYEES) != null && formPropertyMap.get(CustomFormConstants.RELATED_EMPLOYEES).getDefaultValue() != null) {
            performance_note_related_employee.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.RELATED_EMPLOYEES).getSelectedId(), formPropertyMap.get(CustomFormConstants.RELATED_EMPLOYEES).getDefaultValue()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null && formPropertyMap.get(CustomFormConstants.STATUS).getDefaultValue() != null) {
            performance_note_status.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.STATUS).getSelectedId(), formPropertyMap.get(CustomFormConstants.STATUS).getDefaultValue()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PRIORITY) != null && formPropertyMap.get(CustomFormConstants.PRIORITY).getDefaultValue() != null) {
            priorities.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.PRIORITY).getSelectedId(), formPropertyMap.get(CustomFormConstants.PRIORITY).getDefaultValue()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.REPORTED_BY) != null && formPropertyMap.get(CustomFormConstants.REPORTED_BY).getDefaultValue() != null) {
            performance_note_reported_by.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.REPORTED_BY).getSelectedId(), formPropertyMap.get(CustomFormConstants.REPORTED_BY).getDefaultValue()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.RESOLVER) != null && formPropertyMap.get(CustomFormConstants.RESOLVER).getDefaultValue() != null) {
            performance_note_resolver.setSelected(new SelectItem(formPropertyMap.get(CustomFormConstants.RESOLVER).getSelectedId(), formPropertyMap.get(CustomFormConstants.RESOLVER).getDefaultValue()));
        }

    }

    @Override
    protected String getFormID() {
        return LayoutRPC.PERFORMANCE_NOTE_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(isIncindent ? ViewName.Incident : ViewName.PerformanceNote, isIncindent ? LayoutRPC.INCIDENT_FORM : LayoutRPC.PERFORMANCE_NOTE_FORM, new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                GWT.log("property" + result.getFormPropertyMap());
                formPropertyMap = result.getFormPropertyMap();
                AddPerformanceNoteView.super.onInitialize();
            }
        });

        return null;
    }

    @Override
    protected void registerFields() {
        initialize();
        GWT.log(" registerfield1" + formPropertyMap.keySet());
        initializeForms();
    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    protected void fillFormWithData() {
        //performance note name
        if (performance_note_item.getName() != null) {
            performance_note_name.setText(performance_note_item.getName());
        }
        //performance note description
        if (performance_note_item.getDescription() != null) {
            performance_note_description.setText(performance_note_item.getDescription());
        }
        //register performance note related/resolver employees
        if (int_objectID == null) {
            if (int_employeeID != null) {
                performance_note_related_employee.setSelected(int_employeeID);
            } else if (performance_note_item.getCurrentUserID() != null) {
                performance_note_related_employee.selectCurrentUser();
            }
        }
        if (performance_note_item.getRelatedToID() != null) {
            performance_note_related_employee.setSelected(performance_note_item.getRelatedToID());
        }
        //performance note period type From
        if (performance_note_item.getStartDate() != null) {
            dateTime.getStartDatePicker().setDate(performance_note_item.getStartDate().getDate());
        }
        //performance note period type To
        if (performance_note_item.getEndDate() != null) {
            dateTime.getDueDatePicker().setDate(performance_note_item.getEndDate().getDate());
        }
        //register performance note resolver
        registerPerformanceNoteResolver();
        //performance note reported by
        if (performance_note_item.getReportedByID() != null) {
            performance_note_reported_by.setSelected(performance_note_item.getReportedByID());
        }
        //performance note status
        performance_note_status.setItems(performance_note_item.getStatuses());
        if (performance_note_status.getItems() != null) {
            for (SelectItem item : performance_note_status.getItems()) {
                if (item.getName().trim().equals(wfmStrings.New())) {
                    performance_note_status.setSelected(item.getId());
                    break;
                }
            }
        }
        if (performance_note_item.getStatusID() != null) {
            performance_note_status.setSelected(performance_note_item.getStatusID());
        }

        priorities.setItems(performance_note_item.getPriorities());
        if (performance_note_item.getPriorityID() != null) {
            priorities.setSelected(performance_note_item.getPriorityID());
        }
        //performance note show to type
        if (int_objectID == null) {
            performance_note_visibility_private.setValue(true);
        } else {
            if (performance_note_item.isPublic()) {
                performance_note_visibility_public.setValue(true);
            } else {
                performance_note_visibility_private.setValue(true);
            }
        }
        if (int_objectID == null) {
            setDefaultValuesByFormProperty();
        }
    }

    protected void initialize() {
        //performance note name
        performance_note_name = new TextBox();
        performance_note_name.ensureDebugId(test_code_ID_name + "name");
        //performance note description
        performance_note_description = new TextArea2(3000);
        performance_note_description.ensureDebugId(test_code_ID_name + "description");
        //performance note related to employee
        performance_note_related_employee = new EmployeeLookUpWithCode();
        performance_note_related_employee.addStyleName(DEFAULT_WIDTH);
        performance_note_related_employee.ensureDebugId(test_code_ID_name + "related_employee");
        //performance note related employee listener
        performance_note_related_employee.addValueChangeHandler(sender -> registerPerformanceNoteResolver());
        //performance note visibility show to type (private)
        performance_note_visibility_private = new KpiRadioButton("showNoteType", hrmsStrings.getPropertyPrivate(), true);
        performance_note_visibility_private.ensureDebugId(test_code_ID_name + "show_to_type_private");
        //performance note visibility show to type (public)
        performance_note_visibility_public = new KpiRadioButton("showNoteType", wfmStrings.pub(), true);
        performance_note_visibility_public.getElement().getStyle().setMarginLeft(10, Style.Unit.PX);
        performance_note_visibility_public.ensureDebugId(test_code_ID_name + "show_to_type_public");
        //performance note period date From/To
        dateTime = new DateTimePicker();
        dateTime.setAllDay(true);
        dateTime.setStartDate(DateUtil.resetTime(new Date()));
        dateTime.setDueDate(DateUtil.getDayLastTime(new Date()));
        dateTime.getStartTime().setVisible(false);
        dateTime.getEndTime().setVisible(false);
        dateTime.getStartDatePicker().addValueChangeHandler(event -> dateTime.getStartDatePicker().removeStyleName(ERROR_FORM_STYLE));
        dateTime.getStartDatePicker().ensureDebugId(test_code_ID_name + "period_from");
        dateTime.getDueDatePicker().ensureDebugId(test_code_ID_name + "period_to");
        //performance note status
        performance_note_status = new DataListBox();
        performance_note_status.setAllowFirstItem(true);
        performance_note_status.ensureDebugId(test_code_ID_name + "status");

        priorities = new DataListBox();
        priorities.setAllowFirstItem(true);
        priorities.ensureDebugId(test_code_ID_name + "priorities");
        //performance note reporter
        performance_note_reported_by = new EmployeeLookUpWithCode();
        performance_note_reported_by.addStyleName(DEFAULT_WIDTH);
        performance_note_reported_by.ensureDebugId(test_code_ID_name + "reporter");
        //performance note resolver/owner
        performance_note_resolver = new DataListBox();
        performance_note_resolver.setAllowFirstItem(true);
        performance_note_resolver.setEnabled(false);
        performance_note_resolver.ensureDebugId(test_code_ID_name + "resolver");
    }

    protected void initializeForms() {
        KpiDatePicker startDatePicker = dateTime.getStartDatePicker();
        KpiDatePicker dueDatePicker = dateTime.getDueDatePicker();

        addTitleField(CustomFormConstants.BASIC_INFORMATION, wfmStrings.basicDetails());
        addField(CustomFormConstants.NAME, performance_note_name, getTitle(hrmsStrings.noteName(), true));
        addField(CustomFormConstants.DESCRIPTION, performance_note_description, wfmStrings.description());
        addField(CustomFormConstants.RELATED_EMPLOYEES, performance_note_related_employee, getTitle(wfmStrings.relatedEmployee(), true));
        addField(CustomFormConstants.VISIBILITY, new InputGroup(performance_note_visibility_private, performance_note_visibility_public), getTitle(wfmStrings.visibility(), true));
        addField(CustomFormConstants.PERIOD, new InputGroup(startDatePicker, dueDatePicker), getTitle(hrmsStrings.notePeriod(), true));
        addField(CustomFormConstants.STATUS, performance_note_status, getTitle(hrmsStrings.noteStatus(), true));
        addField(CustomFormConstants.REPORTED_BY, performance_note_reported_by, getTitle(wfmStrings.reportedBy()));
        addField(CustomFormConstants.RESOLVER, performance_note_resolver, getTitle(wfmStrings.resolverOwner()));

        show();
    }

    protected void addCustomFields() {
        getCustomFieldUtil().drawCustomFields(this, int_objectID);
    }

    protected boolean isIncident() {
        return false;
    }

    protected void save(final boolean closeTabT) {
        enableButton(false);
        if (!validate()) {
            enableButton(true);
            return;
        }
        setValues();
        //register save logic
        LoadingPanel.loading(true);
        HrmsService.App.get().savePerformanceNote(performance_note_item, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                enableButton(true);
                Info.show(info_error_message, Info.Type.INFO);
            }

            @Override
            public void success(Integer result) {
                LoadingPanel.loading(false);
                enableButton(true);
                Info.show(info_success_message, Info.Type.INFO);
                onShellOk(closeTabT);
                if (performance_note_item.isIncident()) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_INCIDENT_ADD, result, AddPerformanceNoteView.this);
                } else {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PERFORMANCE_NOTE_ADD, result, AddPerformanceNoteView.this);
                }
            }
        });
    }

    protected void setValues() {
        //performance note ID
        performance_note_item.setObjectID(int_objectID);
        //performance note name
        performance_note_item.setName(performance_note_name.getText());
        //performance note description
        if (performance_note_description.getText() != null) {
            performance_note_item.setDescription(performance_note_description.getText());
        }
        //performance note related to employee ID
        performance_note_item.setRelatedToID(performance_note_related_employee.getSelectedItem() != null ? performance_note_related_employee.getSelectedItem().getId() : null);
        //performance note period -> From
        performance_note_item.setStartDate(new DateNonConvertable(dateTime.getStartDate()));
        //performance note period -> To
        performance_note_item.setEndDate(new DateNonConvertable(dateTime.getDueDate()));
        if (dateTime.isAllDay()) {
            Date dueDate = (Date) dateTime.getDueDate().clone();
            performance_note_item.setEndDate(new DateNonConvertable(DateUtil.getDayLastTime(dueDate)));
        }
        //performance note visibility (Public or Private)
        performance_note_item.setPublic(performance_note_visibility_public.getValue());
        //performance note status
        if (performance_note_status.isSomethingSelected()) {
            performance_note_item.setStatusID(performance_note_status.getSelectedItem().getId());
        }
        if (priorities.isSomethingSelected()) {
            performance_note_item.setPriorityID(priorities.getSelectedItem().getId());
        }
        //performance note reported by ID
        performance_note_item.setReportedByID(performance_note_reported_by.getSelectedItem() != null ? performance_note_reported_by.getSelectedItem().getId() : null);
        //performance note resolver ID
        if (performance_note_resolver.isSomethingSelected()) {
            performance_note_item.setResolverID(performance_note_resolver.getSelectedItem().getId());
        }
    }

    private void onShellOk(boolean closeTabT) {
        if (closeTabT) {
            closeTab();
            if (int_objectID != null) {
                if (performance_note_item.isIncident()) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("incident|summary/" + int_objectID, performance_note_item.getName());
                } else {
                    SinksContainerFactory.entryPoint.onHistoryChanged("performancenote|summary/" + int_objectID, performance_note_item.getName());
                }
            }
        } else {
            reInit();
        }
    }

    private void registerPerformanceNoteResolver() {
        SelectItem sItem = performance_note_related_employee.getSelectedItem();
        if (sItem != null && sItem.getId() != null && sItem.getId() != 0) {
            IssueService.App.get().getResolversRelatedTo(EMPLOYEE_ISSUE, sItem.getId(), new AbstractAsyncCallback<SelectItem[]>() {
                @Override
                public void failure(Throwable caught) {
                }

                @Override
                public void success(SelectItem[] items) {
                    performance_note_resolver.setItems(items);
                    performance_note_resolver.setEnabled(true);
                    if (performance_note_item.getResolverID() != null) {
                        performance_note_resolver.setSelected(performance_note_item.getResolverID());
                    }
                }
            });
        } else {
            performance_note_resolver.clear();
            performance_note_resolver.setEnabled(false);
        }
    }

    private void reInit() {

        initForm();
        GWT.log("registerfield2" + formPropertyMap.keySet());
        registerFields();

    }

    private boolean validate() {
        clearErrorStyle();
        int errors = 0;


        if (dateTime.getStartDatePicker().getDate() != null && dateTime.getDueDatePicker().getDate() != null) {
            errors += markAsError(CustomFormConstants.PERIOD, dateTime.getStartDatePicker(), !Validation.validateDateOrder(dateTime.getStartDatePicker().getDate(), dateTime.getDueDatePicker().getDate(), null, dateTime.isAllDay()));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NAME) != null && formPropertyMap.get(CustomFormConstants.NAME).isRequired()) {
            errors += markAsError(CustomFormConstants.FIRST_NAME, performance_note_name, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.NAME).isChanged() ?
                    formPropertyMap.get(CustomFormConstants.NAME).getTitle() : wfmStrings.name(), performance_note_name, formPropertyMap.get(CustomFormConstants.NAME).getMinChar()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION) != null && formPropertyMap.get(CustomFormConstants.DESCRIPTION).isRequired()) {
            errors += markAsError(CustomFormConstants.DESCRIPTION, performance_note_description, !Validation.validateTextBoxRequiredAndCharLimit(formPropertyMap.get(CustomFormConstants.DESCRIPTION).isChanged() ? formPropertyMap.get(CustomFormConstants.DESCRIPTION).getTitle() : wfmStrings.description(), performance_note_description.getTextArea(), formPropertyMap.get(CustomFormConstants.DESCRIPTION).getMinChar()));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.RELATED_EMPLOYEES) != null && formPropertyMap.get(CustomFormConstants.RELATED_EMPLOYEES).isRequired()) {
            errors += markAsError(CustomFormConstants.RELATED_EMPLOYEES, performance_note_related_employee, !Validation.validateLookUpRequired(performance_note_related_employee));
        }
//        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.VISIBILITY) != null && formPropertyMap.get(CustomFormConstants.VISIBILITY).isRequired()) {
//            errors += markAsError(CustomFormConstants.VISIBILITY,new InputGroup(performance_note_visibility_private, performance_note_visibility_public), !Validation.validateRadioButtonRequired(performance_note_visibility_private));
//        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.STATUS) != null && formPropertyMap.get(CustomFormConstants.STATUS).isRequired()) {
            errors += markAsError(CustomFormConstants.STATUS, performance_note_status, !Validation.validateDataListBoxRequired(performance_note_status));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PRIORITY) != null && formPropertyMap.get(CustomFormConstants.PRIORITY).isRequired()) {
            errors += markAsError(CustomFormConstants.PRIORITY, priorities, !Validation.validateDataListBoxRequired(priorities));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.REPORTED_BY) != null && formPropertyMap.get(CustomFormConstants.REPORTED_BY).isRequired()) {
            errors += markAsError(CustomFormConstants.REPORTED_BY, performance_note_reported_by, !Validation.validateLookUpRequired(performance_note_reported_by));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.RESOLVER) != null && formPropertyMap.get(CustomFormConstants.RESOLVER).isRequired()) {
            errors += markAsError(CustomFormConstants.RESOLVER, performance_note_resolver, !Validation.validateDataListBoxRequired(performance_note_resolver));
        }
        errors += markAsError(CustomFormConstants.STATUS, performance_note_status, !Validation.validateListBoxRequired(performance_note_status, new HTML(), wfmStrings.pleaseSelectStatus()));
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
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
