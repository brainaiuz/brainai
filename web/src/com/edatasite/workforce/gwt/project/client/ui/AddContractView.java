package com.edatasite.workforce.gwt.project.client.ui;

import com.edatasite.workforce.gwt.client.client.ui.view.quickadd.CusSuppQuickAddView;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.form.Localize;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CrmAccountLookUp;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.view.Reminder;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.project.client.rpc.ContractSingleItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectServiceAsync;
import com.edatasite.workforce.gwt.project.client.ui.view.projectposition.ProjectPositionWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.util.ArrayList;

/**
 * User: Faxriddin Taslimov Date: 28.08.2015
 */
public class AddContractView extends CustomForm2 implements CommandConstants, Constants, Colapse {

    public AddContractView() {
        super("addcontract", wfmStrings.addContract());
    }

    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private final ProjectServiceAsync projectService = ProjectService.App.get();
    private TextBox number;
    private KpiCheckBox accomodation;
    private KpiCheckBox food;
    private FlexTable checkboxes;
    private CrmAccountLookUp client;
    private FlowPanel pnlEmployeeAssignmentContainer;
    private WfmButton2 saveCloseButton;
    private ProjectPositionWidget projectPositionWidget;
    private Anchor addNewClient;
    private NoteWidget noteWidget;
    private GeneralFileUpload fileUpload;

    private FormHasCustomField customFieldUtil;

    private Reminder reminder;
    private DatePicker startDatePicker;
    private DatePicker dueDatePicker;


    private final String addContract = "add_contract_";

    private void initInternal() {
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

        client = new CrmAccountLookUp(CrmAccountLookUp.CUSTOMER, true);
        client.showClearButton();
        client.addStyleName(DEFAULT_WIDTH);
        client.ensureDebugId(addContract + "client");
//        client.getElement().getStyle().setPaddingBottom(5, Style.Unit.PX); //https://prnt.sc/tlb6jb

        pnlEmployeeAssignmentContainer = new FlowPanel();

        addNewClient = new Anchor(projectStrings.addNewClient());
        addNewClient.ensureDebugId(addContract + "addNewClient");
        addNewClient.addStyleName("btn-flat AddContractView"); //https://prnt.sc/ra73ak
        addNewClient.addClickHandler(widget -> new CusSuppQuickAddView(CrmAccountItem.CUSTOMER, null));

        projectPositionWidget = new ProjectPositionWidget(null, true);
        projectPositionWidget.getPnlContainer().addStyleName("scroll-box--x");
        pnlEmployeeAssignmentContainer.clear();
        pnlEmployeeAssignmentContainer.add(projectPositionWidget);
        number.ensureDebugId(addContract + "contractNumber");
        projectPositionWidget.ensureDebugId(addContract + "contractPositionWidget");

        startDatePicker = new DatePicker(true);
        startDatePicker.addStyleName(DEFAULT_WIDTH);
        startDatePicker.ensureDebugId(addContract + "startDate");

        dueDatePicker = new DatePicker(true);
        dueDatePicker.addStyleName(DEFAULT_WIDTH);
        dueDatePicker.ensureDebugId(addContract + "endDate");

        registrationEventBus();

        noteWidget = new NoteWidget(null, RelationItem.TYPE_CONTRACT);

        //attachments
        fileUpload = new GeneralFileUpload(F_CONTRACT, null, null);
        fileUpload.ensureDebugId("contract_attachments");
        addFields();
    }

    /**
     * User Event Bus
     */
    private void registrationEventBus() {

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CLIENT_ADD, AddContractView.this, (sender, args) -> {
            if (args != null) {
                if (args instanceof Integer) {
                    initClients((Integer) args);
                } else {
                    initClients(((CrmAccountItem) args).getObjectId());
                }
            }
        });
    }

    private void addFields() {
        //set titles
        addTitleField(DETAILS, wfmStrings.contractDetails());
        addTitleField(CustomFormConstants.PROJECT.INVOLVED_EMPLOYEES, wfmStrings.requirements());
        addTitleField(CONTRACT.NOTE, wfmStrings.notes());

        //set fields
        addField(CustomFormConstants.NUMBER, number, getTitle(wfmStrings.number(), true));
        addField(CustomFormConstants.NAME, checkboxes, getTitle(wfmStrings.allowancebytheclient()));

        FlexTable vPanel = new FlexTable();
        vPanel.setWidget(0, 0, client);
        if (Utils.hasRole(PM) || Utils.hasRole(TL) || Utils.hasRole(DR) || Utils.hasRole(ADMIN)) {
            vPanel.setWidget(1, 0, addNewClient);
            vPanel.getFlexCellFormatter().setHorizontalAlignment(1, 0, HasHorizontalAlignment.ALIGN_RIGHT);
        }
        addField(CustomFormConstants.PROJECT.CLIENT, vPanel, getTitle(Property.get(Constants.CLIENT_LIST, wfmStrings.customer())));
        addField(CustomFormConstants.PROJECT.INVOLVED_EMPLOYEE, pnlEmployeeAssignmentContainer, null);
        addField(CustomFormConstants.CONTRACT.NOTE, noteWidget, wfmStrings.notes(), true);
        addField(CONTRACT.START_DATE, startDatePicker, wfmStrings.startDate());
        addField(CONTRACT.END_DATE, dueDatePicker, wfmStrings.dueDate());
        addField(ATTACHMENTS, fileUpload, getTitle(wfmStrings.attachments(), false), true);
        setDefaultValues();
        //reminder widgets
        reminder = new Reminder(false, "10.5em");
        addField(CustomFormConstants.PROJECT.DUE_DATE_REMINDER, reminder, getTitle(wfmStrings.duedatereminder()));
        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
        getCustomFieldUtil().drawCustomFields(this, null);
    }

    private void initClients(final Integer clientId) {
        projectService.getClients(new AbstractAsyncCallback<SelectItem[]>() {
            public void success(final SelectItem[] object) {
                DeferredCommand.addCommand(() -> {
                    client.setItems("", object);
                    if (clientId != null) {
                        client.setSelected(client.getSelectedItem(clientId));
                    }
                });
            }
        });
    }

    private boolean validate() {
        clearErrorStyle();
        int errors = 0;
        errors += super.customValidate();

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

        errors += markAsError(CustomFormConstants.NAME, number, number.getText() == null || "".equals(number.getText()));

        errors += getCustomFieldUtil().validateCustomFields();

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private void save() {
        if (!validate()) {
            return;
        }
        LoadingPanel.loading(true);
        saveContract();
    }

    private void saveContract() {
        ContractSingleItem newContract = new ContractSingleItem();
        newContract.setNumber(number.getText());
        newContract.setAccomudation(accomodation.getValue());
        newContract.setFood(food.getValue());
        newContract.setProjectPositions(projectPositionWidget.getProjectPositions());
        if (client.getSelectedItem() != null) {
            newContract.setClientId(client.getSelectedItem().getId());
        }
        newContract.setNotes(noteWidget.getNewNotesToSave());
        newContract.setAttachments(fileUpload.getAttachedFiles());

        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_CONTRACT_CUSTOM_FIELD_SET_STATIC_TEXT)) {

            ArrayList<CompanyCustomFieldItem> cfList = getCustomFieldUtil().getCustomFieldsValue();

            // 1. Customer Category topiladi
            CompanyCustomFieldItem customerCategory = cfList.stream()
                    .filter(cf -> "Customer Category".equals(cf.getAliasName()))
                    .findFirst()
                    .orElse(null);

            if (customerCategory != null) {

                String category = customerCategory.getFieldStringValue();
                double total = 0.0;

                // 2. Category bo‘yicha qiymat aniqlanadi
                switch (category) {
                    case "A": total = 4.0; break;
                    case "B": total = 3.0; break;
                    case "C": total = 2.0; break;
                }

                double remaining = total;
                double used = 0.0;

                // 3. Custom fieldlarga qiymatlarni set qilish
                for (CompanyCustomFieldItem cf : cfList) {
                    String alias = cf.getAliasName();

                    if ("Visit Credit Total".equals(alias)) {
                        cf.setFieldStringValue(total);
                    } else if ("Visit Credit Remaining".equals(alias)) {
                        cf.setFieldStringValue(remaining);
                    } else if ("Visit Credit Used".equals(alias)) {
                        cf.setFieldStringValue(used);
                    }
                }
            }

            newContract.setCustomFieldItems(cfList);

        } else {
            newContract.setCustomFieldItems(getCustomFieldUtil().getCustomFieldsValue());
        }

        LoadingPanel.loading(true);
        saveCloseButton.setEnabled(false);
        newContract.setReminder(reminder.getReminderDatas());
        if (startDatePicker.getDate() != null) {
            newContract.setStartDate(new DateNonConvertable(DateUtil.resetTime(startDatePicker.getDate())));
        }
        if (dueDatePicker.getDate() != null) {
            newContract.setDueDate(new DateNonConvertable(DateUtil.getDayLastTime(dueDatePicker.getDate())));
        }

        projectService.saveContract(newContract, new AbstractAsyncCallback<Integer>() {

            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                saveCloseButton.setEnabled(true);
                WfmMessageBox messageBox = new WfmMessageBox(IconEnum.ERROR, Action.OK, "", null);
                messageBox.setTitle(wfmStrings.error());
                messageBox.open();
            }

            public void success(final Integer result) {
                LoadingPanel.loading(false);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CONTRACT_ADD, result, AddContractView.this);
                Info.show(wfmStrings.messSuccessfullyAdded(), Info.Type.INFO);
                saveCloseButton.setEnabled(true);
                shellOk();
            }

        });
    }

    private void shellOk() {
        closeTab();
    }

    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFields(ViewName.Contract, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
            @Override
            public void failure(Throwable throwable) {
                initInternal();
            }

            @Override
            public void success(ArrayList<CompanyCustomFieldItem> result) {
                if (result != null) {
                    getCustomFieldUtil().setCompanyCustomFieldItems(result);
                }
                initInternal();
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
        return LayoutRPC.ADD;
    }

    @Override
    protected void getDataToFillFields() {
    }

    @Override
    protected void initPredefinedValues() {
    }

    @Override
    protected void addButtons() {
        saveCloseButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveCloseButton.ensureDebugId(addContract + "saveCloseButton");
        saveCloseButton.addClickHandler(sender -> save());
        addButton(saveCloseButton);

//        WfmButton2 cancel = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_DEFAULT);
//        cancel.ensureDebugId(addContract + "cancelButton");
//        cancel.addClickHandler(clickEvent -> closeTab());
//        addButton(cancel);
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    public String getIconStyle() {
        return null;
    }

    @Override
    public String getFieldLabel(String fieldID) {
        return Localize.getInstance().localizeByFieldID(getFormID(), fieldID);
    }


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
