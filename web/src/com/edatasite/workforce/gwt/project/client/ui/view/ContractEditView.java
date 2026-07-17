package com.edatasite.workforce.gwt.project.client.ui.view;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectPosition;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.view.Reminder;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.project.client.rpc.EditContract;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectServiceAsync;
import com.edatasite.workforce.gwt.project.client.ui.PmClientsLookUp;
import com.edatasite.workforce.gwt.project.client.ui.view.projectposition.ProjectPositionWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;

public class ContractEditView extends CustomForm2 implements Constants, Colapse {

    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private static final ProjectServiceAsync projectService = ProjectService.App.get();


    private TextBox number;
    private KpiCheckBox accomodation;
    private KpiCheckBox food;
    private FlexTable checkboxes;

    private PmClientsLookUp client;
    private HTML registrationDate;

    private final Integer contractID;
    private EditContract contract;

    private FlowPanel pnlEmployeeAssignmentContainer;

    private ProjectPositionWidget projectPositionWidget;

    private NoteWidget noteWidget;

    private GeneralFileUpload fileUpload;
    private Reminder reminder;

    private DatePicker startDatePicker;
    private DatePicker dueDatePicker;

    private FormHasCustomField customFieldUtil = null;

    private final String editContract = "edit_contract_";

    public ContractEditView(Integer contractID) {
        super("edit", wfmStrings.edit());
        this.contractID = contractID;
    }

    @Override
    public String getIconStyle() {
        return "bgMark project-edit";
    }


    public String getDescription() {
        return wfmStrings.edit();
    }

    private void getEditContract() {
        LoadingPanel.loading(true);
        projectService.getContractForEdit(contractID, new AbstractAsyncCallback<EditContract>() {
            @Override
            public void success(EditContract object) {
                LoadingPanel.loading(false);
                contract = object;
                number.setText(contract.getNumber());
                accomodation.setValue(contract.getIsAccomodation());
                food.setValue(contract.getIsFoot());
                pnlEmployeeAssignmentContainer.clear();
                projectPositionWidget.setValues(contract.getProjectPositions());
                pnlEmployeeAssignmentContainer.add(projectPositionWidget);
                if (contract.getCreationTime() != null) {
                    registrationDate.setHTML(DateUtils.format(contract.getCreationTime()));
                }

                if (contract.getClientId() != null) {
                    client.addItem(new SelectItem(contract.getClientId(), contract.getClientName()));
                }
                if (contract.getStartDate() != null) {
                    startDatePicker.setDate(contract.getStartDate().getNonConvertedDate());
                }
                if (contract.getDueDate() != null) {
                    dueDatePicker.setDate(contract.getDueDate().getNonConvertedDate());
                }
                reminder.setReminderDatas(object.getReminder());
                getCustomFieldUtil().fillCustomFieldsWithData(object.getCustomFieldItems());
                defferedLoading();
            }
        });

    }

    private void initialize() {
        super.onInitialize();
    }

    protected void registerFields() {

        number = new TextBox();
        number.addStyleName(DEFAULT_WIDTH);

        checkboxes = new FlexTable();
        accomodation = new KpiCheckBox(wfmStrings.accomodation());
        food = new KpiCheckBox(wfmStrings.food());
        checkboxes.setWidget(0, 0, accomodation);
        checkboxes.setWidget(1, 0, food);

        client = new PmClientsLookUp();
        client.showClearButton();
        client.addStyleName(DEFAULT_WIDTH);

        registrationDate = new HTML();
        registrationDate.ensureDebugId(editContract + "registrationDate");

        number.ensureDebugId(editContract + "name");
        client.ensureDebugId(editContract + "client");

        pnlEmployeeAssignmentContainer = new FlowPanel();
        projectPositionWidget = new ProjectPositionWidget(contractID, true);

        noteWidget = new NoteWidget(contractID, RelationItem.TYPE_CONTRACT);
        //attachments
        fileUpload = new GeneralFileUpload(F_CONTRACT, contractID, contractID);
        fileUpload.ensureDebugId("contract_attachments");

        startDatePicker = new DatePicker(true);
        startDatePicker.addStyleName(DEFAULT_WIDTH);
        startDatePicker.ensureDebugId(editContract + "startDatePicker");

        dueDatePicker = new DatePicker(true);
        dueDatePicker.addStyleName(DEFAULT_WIDTH);
        dueDatePicker.ensureDebugId(editContract + "startDatePicker");

        addFields();
    }

    private void defferedLoading() {
        if (contract.getClientId() != null) {
            projectService.getClient(contract.getClientId(), new AbstractAsyncCallback<SelectItem>() {
                @Override
                public void failure(Throwable caught) {

                }

                @Override
                public void success(SelectItem result) {
                    if (result != null) {
                        client.addItem(result);
                    }
                }
            });
        }
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    private void addFields() {
        addTitleField(CustomFormConstants.DETAILS, wfmStrings.contract() + " " + wfmStrings.details());
        addField(CustomFormConstants.NUMBER, number, getTitle( wfmStrings.number(), true));
        addField(CustomFormConstants.NAME, checkboxes, getTitle(wfmStrings.allowancebytheclient()));

        addTitleField(CustomFormConstants.PROJECT.INVOLVED_EMPLOYEES, wfmStrings.requirements());

        addField(CustomFormConstants.PROJECT.CLIENT, client, getTitle(Property.get(Constants.CLIENT_LIST, wfmStrings.customer())));

        addField(CustomFormConstants.REGISTRATION_DATE, registrationDate, getTitle(wfmStrings.dateOfRegistration()));

        addTitleField(CONTRACT.NOTE, wfmStrings.notes());
        addField(CustomFormConstants.CONTRACT.NOTE, noteWidget, wfmStrings.notes(), true);
        addField(ATTACHMENTS, fileUpload, getTitle(wfmStrings.attachments(), false), true);

        addField(CONTRACT.START_DATE, startDatePicker, wfmStrings.startDate());
        addField(CONTRACT.END_DATE, dueDatePicker, wfmStrings.dueDate());

        reminder = new Reminder(false, "10.5em");
        addField(CustomFormConstants.PROJECT.DUE_DATE_REMINDER, reminder, getTitle(wfmStrings.duedatereminder(), false));

        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());

        addField(CustomFormConstants.PROJECT.INVOLVED_EMPLOYEE, pnlEmployeeAssignmentContainer, null);

        getCustomFieldUtil().drawCustomFields(this, contractID);

        show();

    }

    private boolean validate() {
        int errors = 0;

        errors = super.customValidate();

        if (!reminder.validateDueReminder()) {
            return false;
        }

        if (!reminder.getReminderDatas().isEmpty()) {
            if (!Validation.validateDate(dueDatePicker)) {
                return false;
            }
            if (startDatePicker.getDate() != null && dueDatePicker.getDate() != null && startDatePicker.getDate().after(dueDatePicker.getDate())) {
                Info.show(projectStrings.contractEndDateCanNotBeEarlier(), Info.Type.WARNING);
                return false;
            }
        }

        errors += markAsError(CustomFormConstants.NUMBER, number, number.getText() == null || "".equals(number.getText()));
        errors += getCustomFieldUtil().validateCustomFields();
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }


    private void updateContract() {
        if (!validate()) {
            return;
        }
        enableButton(false);
        contract.setNumber(number.getText());
        contract.setIsAccomodation(accomodation.getValue());
        contract.setIsFoot(food.getValue());
        if (client.getSelectedItem() != null) {
            contract.setClientId(client.getSelectedItem().getId());
        } else {
            contract.setClientId(null);
        }
        contract.setProjectPositions(projectPositionWidget.getProjectPositions());
        if (startDatePicker.getDate() != null) {
            contract.setStartDate(new DateNonConvertable(DateUtil.resetTime(startDatePicker.getDate())));
        }
        if (dueDatePicker.getDate() != null) {
            contract.setDueDate(new DateNonConvertable(DateUtil.getDayLastTime(dueDatePicker.getDate())));
        }

        LoadingPanel.loading(true);

        contract.setReminder(reminder.getReminderDatas());
        contract.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());
        projectService.updateContract(contract, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable caught) {
                enableButton(true);
                LoadingPanel.loading(false);
                try {
                    throw caught;
                } catch (NumberExistingException ex) {
                    WfmMessageBox messageBox = new WfmMessageBox(IconEnum.ERROR, Action.OK, ex.getDetailedMessage());
                    messageBox.setTitle(wfmStrings.error());
                    messageBox.open();
                } catch (Throwable ex) {
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                }
            }

            @Override
            public void success(Void result) {
                enableButton(true);
                LoadingPanel.loading(false);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CONTRACT_EDIT, contractID, ContractEditView.this);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.changes()), Info.Type.INFO);
                closeTab();
            }
        });
    }

    private String validatePositionEndDate() { // returning null means valid
        if (!reminder.getReminderDatas().isEmpty()) {
            if (projectPositionWidget.getProjectPositions().length > 0) {
                for (ProjectPosition projectPosition : projectPositionWidget.getProjectPositions()) {
                    if (projectPosition.getContractEnd() != null) {
                        return null;
                    }
                }
            } else {
                return projectStrings.selectContractPositionAsYouHavSetReminder();
            }
        } else {
            return null;
        }
        return projectStrings.setEndDateAtLeastForOneOfThePositions();
    }

    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFields(ViewName.Contract, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
            @Override
            public void failure(Throwable throwable) {
                Info.show(wfmStrings.errorOccurred());
                initialize();
            }

            @Override
            public void success(ArrayList<CompanyCustomFieldItem> result) {
                if (result != null) {
                    getCustomFieldUtil().setCompanyCustomFieldItems(result);
                }
                initialize();
            }
        });


        return null;
    }

    @Override
    protected String getWikiCode() {
        return PermissionConstants.PM_CONTRACT_ADD_EDIT;
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.CONTRACT_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.EDIT;
    }

    @Override
    protected void getDataToFillFields() {
        getEditContract();
    }

    @Override
    protected void addButtons() {
        WfmButton2 saveButton = new WfmButton2(wfmStrings.update(), WfmButton2.BTN_PRIMARY);
        saveButton.ensureDebugId(editContract + "saveButton");
        saveButton.addClickHandler(sender -> updateContract());
        addButton(saveButton);
//        WfmButton2 cancel = new WfmButton2(wfmStrings.cancel());
//        cancel.ensureDebugId(editContract + "cancelButton");
//        cancel.addClickHandler(clickEvent -> closeTab());
//        addButton(cancel);

    }

    @Override
    protected void initPredefinedValues() {

    }

    @Override
    public String getFieldLabel(String fieldID) {
        if (fieldID != null) {
            if (CustomFormConstants.DETAILS.equals(fieldID)) {
                return wfmStrings.contract() + " " + wfmStrings.details();
            } else if (CustomFormConstants.NUMBER.equals(fieldID)) {
                return wfmStrings.contract() + " " + wfmStrings.number();
            } else if (CustomFormConstants.NAME.equals(fieldID)) {
                return wfmStrings.allowancebytheclient();
            } else if (CustomFormConstants.PROJECT.CLIENT.equals(fieldID)) {
                return wfmStrings.customer();
            } else if (CustomFormConstants.TASK.PROJECT.equals(fieldID)) {
                return Property.get(Constants.PROJECT, wfmStrings.project());
            } else if (CustomFormConstants.PROJECT.INVOLVED_EMPLOYEES.equals(fieldID)) {
                return wfmStrings.requirements();
            } else if (CustomFormConstants.PROJECT.INVOLVED_EMPLOYEE.equals(fieldID)) {
                return wfmStrings.members();
            } else if (CustomFormConstants.CONTRACT.NOTE.equals(fieldID)) {
                return wfmStrings.notes();
            } else if (CustomFormConstants.PROJECT.DUE_DATE_REMINDER.equals(fieldID)) {
                return wfmStrings.duedatereminder();
            }
        }
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
}