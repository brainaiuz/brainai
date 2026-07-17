package com.edatasite.workforce.gwt.profile.client.ui.view.workflow;

import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelField;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.profile.client.localization.ProfileMessages;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileServiceAsync;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowEmployee;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Azazello on 4/25/16.
 */
public class AddEditWorkflowEmployee extends CustomForm implements CustomFormConstants, Constants {
    protected static final ProfileServiceAsync profileService = ProfileService.App.get();
    protected static final SettingStrings settingsStrings = SettingStrings.App.get();
    protected static final ProfileMessages profileMessages = ProfileMessages.App.get();
    private static final String DELIMETR = ";;";
    private final String test_code_ID_name = "add_edit_workflow_employee_view_";
    protected Integer objectID;
    protected Integer workflowID;
    protected WorkflowEmployee item;
    //fields
    private DataListBox firstName;
    private DataListBox lastName;
    private DataListBox basicSalary;
    //personal identity information
    private DataListBox passportNumber;
    private DataListBox passportIssueBy;
    private DataListBox passportIssueDate;
    private DataListBox passportExpiryDate;
    private DataListBox insuranceNumber;
    private DataListBox insuranceExpiryDate;
    private DataListBox visaNumber;
    private DataListBox visaIssueDate;
    private DataListBox visaExpirationDate;
    //bank account information
    private DataListBox bankName;
    private DataListBox accountName;
    private DataListBox accountNumber;
    private DataListBox bankAddress;
    private DataListBox swiftCode;
    private DataListBox sortCode;
    private DataListBox ibanCode;
    private DataListBox agentID;
    //CF
    public List<CompanyCustomFieldItem> companyCustomFieldItems;
    public DataListBox[] tbValues;

    public AddEditWorkflowEmployee(Integer objectID, Integer workflowID) {
        super("addWorkflowEmployee", objectID == null ? settingsStrings.addWorkflowEmployee() : settingsStrings.editWorkflowEmployee());
        this.objectID = objectID;
        this.workflowID = workflowID;
    }

    @Override
    protected Widget onInitialize() {
        super.onInitialize();
        loadPage();
        return null;
    }

    private void loadPage() {
        LoadingPanel.loading(true);
        CommonService.App.get().getCompanyCustomFields(ViewName.Employee, new AbstractAsyncCallback<ArrayList<CompanyCustomFieldItem>>() {
            public void failure(Throwable throwable) {
            }

            public void success(ArrayList<CompanyCustomFieldItem> result) {
                if (result != null) {
                    companyCustomFieldItems = result;
                    initialize();
                }
            }
        });
    }

    protected void initialize() {
        firstName = new DataListBox();
        firstName.ensureDebugId("first_name");
        lastName = new DataListBox();
        lastName.ensureDebugId("last_name");
        basicSalary = new DataListBox();
        basicSalary.ensureDebugId("basicSalary");
        //personal identity
        passportNumber = new DataListBox();
        passportNumber.ensureDebugId("passportNumber");
        passportIssueBy = new DataListBox();
        passportIssueBy.ensureDebugId("passportIssueBy");
        passportIssueDate = new DataListBox();
        passportIssueDate.ensureDebugId("passportIssueDate");
        passportExpiryDate = new DataListBox();
        passportExpiryDate.ensureDebugId("passportExpiryDate");
        insuranceNumber = new DataListBox();
        insuranceNumber.ensureDebugId("insuranceNumber");
        insuranceExpiryDate = new DataListBox();
        insuranceExpiryDate.ensureDebugId("insuranceExpiryDate");
        visaNumber = new DataListBox();
        visaNumber.ensureDebugId("visaNumber");
        visaIssueDate = new DataListBox();
        visaIssueDate.ensureDebugId("visaIssueDate");
        visaExpirationDate = new DataListBox();
        visaExpirationDate.ensureDebugId("visaExpirationDate");
        //bankaccount
        bankName = new DataListBox();
        bankName.ensureDebugId("bankName");
        accountName = new DataListBox();
        accountName.ensureDebugId("accountName");
        accountNumber = new DataListBox();
        accountNumber.ensureDebugId("accountNumber");
        bankAddress = new DataListBox();
        bankAddress.ensureDebugId("bankAddress");
        swiftCode = new DataListBox();
        swiftCode.ensureDebugId("swiftCode");
        sortCode = new DataListBox();
        sortCode.ensureDebugId("sortCode");
        ibanCode = new DataListBox();
        ibanCode.ensureDebugId("ibanCode");
        agentID = new DataListBox();
        agentID.ensureDebugId("agentID");
        firstName.addStyleName(DEFAULT_WIDTH);
        lastName.addStyleName(DEFAULT_WIDTH);
        basicSalary.addStyleName(DEFAULT_WIDTH);
        passportNumber.addStyleName(DEFAULT_WIDTH);
        passportIssueBy.addStyleName(DEFAULT_WIDTH);
        passportIssueDate.addStyleName(DEFAULT_WIDTH);
        passportExpiryDate.addStyleName(DEFAULT_WIDTH);
        insuranceNumber.addStyleName(DEFAULT_WIDTH);
        insuranceExpiryDate.addStyleName(DEFAULT_WIDTH);
        visaNumber.addStyleName(DEFAULT_WIDTH);
        visaIssueDate.addStyleName(DEFAULT_WIDTH);
        visaExpirationDate.addStyleName(DEFAULT_WIDTH);
        bankName.addStyleName(DEFAULT_WIDTH);
        accountName.addStyleName(DEFAULT_WIDTH);
        accountNumber.addStyleName(DEFAULT_WIDTH);
        bankAddress.addStyleName(DEFAULT_WIDTH);
        swiftCode.addStyleName(DEFAULT_WIDTH);
        sortCode.addStyleName(DEFAULT_WIDTH);
        ibanCode.addStyleName(DEFAULT_WIDTH);
        agentID.addStyleName(DEFAULT_WIDTH);
        drawForm();
    }

    protected void drawForm() {
        addTitleField(EMPLOYEE_INFORMATION, getTitle(wfmStrings.employeeInformation()));
        addTitleField(CustomFormConstants.PERSONAL_IDENTITY_INFORMATION, getTitle(wfmStrings.personalIdentityInformation()));
        addTitleField(CustomFormConstants.BANK_ACCOUNT_INFORMATION, getTitle(Property.get(Constants.BANKACCOUNT, wfmStrings.bankAccountInformation(), wfmStrings.bankAccount())));
        addField(CustomFormConstants.FIRST_NAME, firstName, getTitle(wfmStrings.firstName()));
        addField(CustomFormConstants.LAST_NAME, lastName, getTitle(wfmStrings.lastName()));
        addField(CustomFormConstants.SALARY_AMOUNT, basicSalary, getTitle(wfmStrings.basicSalary()));
        addField(CustomFormConstants.PASSPORT_NUMBER, passportNumber, getTitle(wfmStrings.passportNumber()));
        addField(CustomFormConstants.PASSPORT_ISSUE, passportIssueBy, getTitle(wfmStrings.passportIssueBy()));
        addField(CustomFormConstants.PASSPORT_ISSUE_DATE, passportIssueDate, getTitle(wfmStrings.passportIssueDate()));
        addField(CustomFormConstants.PASSPORT_EXPIRY_DATE, passportExpiryDate, getTitle(wfmStrings.passportExpireDate()));
        addField(CustomFormConstants.INSURANCE_NUMBER, insuranceNumber, getTitle(wfmStrings.insuranseNumber()));
        addField(CustomFormConstants.INSURANCE_EXPIRY_DATE, insuranceExpiryDate, getTitle(wfmStrings.insuranceExpiryDate()));
        addField(CustomFormConstants.VISA_NUMBER, visaNumber, getTitle(wfmStrings.visaNumber()));
        addField(CustomFormConstants.VISA_ISSUE_DATE, visaIssueDate, getTitle(wfmStrings.visaIssueDate()));
        addField(CustomFormConstants.VISA_EXPIRATION_DATE, visaExpirationDate, getTitle(wfmStrings.visaExpirationDate()));
        addField(CustomFormConstants.BANK_NAME, bankName, getTitle(wfmStrings.bankName()));
        addField(CustomFormConstants.ACCOUNT_NAME, accountName, getTitle(wfmStrings.accountName()));
        addField(CustomFormConstants.ACCOUNT_NUMBER, accountNumber, getTitle(wfmStrings.accountNumber()));
        addField(CustomFormConstants.BANK_ADDRESS, bankAddress, getTitle(wfmStrings.bankAddress()));
        addField(CustomFormConstants.SWIFT_CODE, swiftCode, getTitle(wfmStrings.swiftCode()));
        addField(CustomFormConstants.SORT_CODE, sortCode, getTitle(wfmStrings.sortCode()));
        addField(CustomFormConstants.IBAN_CODE, ibanCode, getTitle(wfmStrings.ibanCode()));
        addField(CustomFormConstants.AGENT_ID, agentID, getTitle(wfmStrings.agentID()));
        if (companyCustomFieldItems != null && companyCustomFieldItems.size() > 0) {
            addTitleField(ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());
            tbValues = new DataListBox[companyCustomFieldItems.size()];
            for (int i = 0; i < companyCustomFieldItems.size(); i++) {
                tbValues[i] = new DataListBox();
                tbValues[i].setLayoutData(companyCustomFieldItems.get(i).getColumnCode());
                tbValues[i].addStyleName(DEFAULT_WIDTH);
                addField("string_value" + (i + 1), tbValues[i], companyCustomFieldItems.get(i).getFieldName());
            }
        }
        show();
    }

    @Override
    protected void addButtons() {
        addButton(objectID == null ? wfmStrings.save() : wfmStrings.update(), null, (test_code_ID_name + "save_and_close_button"), event -> save());
    }

    private void save() {
        getValues();
        LoadingPanel.loading(true);
        profileService.saveWorkflowEmployee(item, new AbstractAsyncCallback<Void>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Void result) {
                LoadingPanel.loading(false);
                closeTab();
                Info.show(profileMessages.workflowEmployeeSavSuc());
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_WORKFLOW_EMPLOYEE_MODIFICATION, item, AddEditWorkflowEmployee.this);
            }
        });
    }

    private void getValues() {
        item = item == null ? new WorkflowEmployee() : item;
        String values = "";
        if(firstName.getSelectedItem() != null){
            values += CustomFormConstants.FIRST_NAME + DELIMETR + firstName.getSelectedItem().getReferenceCode() + DELIMETR;
        }
        if(lastName.getSelectedItem() != null){
            values += CustomFormConstants.LAST_NAME + DELIMETR + lastName.getSelectedItem().getReferenceCode() + DELIMETR;
        }
        if(basicSalary.getSelectedItem() != null){
            values += CustomFormConstants.SALARY_AMOUNT + DELIMETR + basicSalary.getSelectedItem().getReferenceCode() + DELIMETR;
        }
        if(passportNumber.getSelectedItem() != null){
            values += CustomFormConstants.PASSPORT_NUMBER + DELIMETR + passportNumber.getSelectedItem().getReferenceCode() + DELIMETR;
        }
        if(passportIssueBy.getSelectedItem() != null){
            values += CustomFormConstants.PASSPORT_ISSUE + DELIMETR + passportIssueBy.getSelectedItem().getReferenceCode() + DELIMETR;
        }
        if(passportIssueDate.getSelectedItem() != null){
            values += CustomFormConstants.PASSPORT_ISSUE_DATE + DELIMETR + passportIssueDate.getSelectedItem().getReferenceCode() + DELIMETR;
        }
        if(passportExpiryDate.getSelectedItem() != null){
            values += CustomFormConstants.PASSPORT_EXPIRY_DATE + DELIMETR + passportExpiryDate.getSelectedItem().getReferenceCode() + DELIMETR;
        }
        if(insuranceNumber.getSelectedItem() != null){
            values += CustomFormConstants.INSURANCE_NUMBER + DELIMETR + insuranceNumber.getSelectedItem().getReferenceCode() + DELIMETR;
        }
        if(insuranceExpiryDate.getSelectedItem() != null){
            values += CustomFormConstants.INSURANCE_EXPIRY_DATE + DELIMETR + insuranceExpiryDate.getSelectedItem().getReferenceCode() + DELIMETR;
        }
        if(visaNumber.getSelectedItem() != null){
            values += CustomFormConstants.VISA_NUMBER + DELIMETR + visaNumber.getSelectedItem().getReferenceCode() + DELIMETR;
        }
        if(visaIssueDate.getSelectedItem() != null){
            values += CustomFormConstants.VISA_ISSUE_DATE + DELIMETR + visaIssueDate.getSelectedItem().getReferenceCode() + DELIMETR;
        }
        if(visaExpirationDate.getSelectedItem() != null){
            values += CustomFormConstants.VISA_EXPIRATION_DATE + DELIMETR + visaExpirationDate.getSelectedItem().getReferenceCode() + DELIMETR;
        }
        if(bankName.getSelectedItem() != null){
            values += CustomFormConstants.BANK_NAME + DELIMETR + bankName.getSelectedItem().getReferenceCode() + DELIMETR;
        }
        if(accountName.getSelectedItem() != null){
            values += CustomFormConstants.ACCOUNT_NAME + DELIMETR + accountName.getSelectedItem().getReferenceCode() + DELIMETR;
        }
        if(accountNumber.getSelectedItem() != null){
            values += CustomFormConstants.ACCOUNT_NUMBER + DELIMETR + accountNumber.getSelectedItem().getReferenceCode() + DELIMETR;
        }
        if(bankAddress.getSelectedItem() != null){
            values += CustomFormConstants.BANK_ADDRESS + DELIMETR + bankAddress.getSelectedItem().getReferenceCode() + DELIMETR;
        }
        if(swiftCode.getSelectedItem() != null){
            values += CustomFormConstants.SWIFT_CODE + DELIMETR + swiftCode.getSelectedItem().getReferenceCode() + DELIMETR;
        }
        if(sortCode.getSelectedItem() != null){
            values += CustomFormConstants.SORT_CODE + DELIMETR + sortCode.getSelectedItem().getReferenceCode() + DELIMETR;
        }
        if(ibanCode.getSelectedItem() != null){
            values += CustomFormConstants.IBAN_CODE + DELIMETR + ibanCode.getSelectedItem().getReferenceCode() + DELIMETR;
        }
        if(agentID.getSelectedItem() != null){
            values += CustomFormConstants.AGENT_ID + DELIMETR + agentID.getSelectedItem().getReferenceCode() + DELIMETR;
        }
        if (tbValues != null && tbValues.length > 0) {
            StringBuilder valuesBuilder = new StringBuilder(values);
            for (DataListBox box : tbValues) {
                if(box.getSelectedItem() != null){
                    valuesBuilder.append(box.getLayoutData()).append(DELIMETR).append(box.getSelectedItem().getReferenceCode()).append(DELIMETR);
                }
            }
            values = valuesBuilder.toString();
        }
        values = values.length() > 0 ? values.substring(0, values.length() - 2) : null;
        item.setValuesAsString(values);
    }

    @Override
    protected void getDataToFillFields() {
        profileService.getWorkflowEmployee(objectID, workflowID, new AbstractAsyncCallback<WorkflowEmployee>() {
            public void failure(Throwable d) {
                LoadingPanel.loading(false);
                closeTab();
            }

            public void success(final WorkflowEmployee result) {
                DeferredCommand.addCommand(() -> {
                    LoadingPanel.loading(false);
                    item = result;
                    setItems(getColumnsAsReferenceItem(item.getFields()));
                    if (objectID != null) {
                        setValues(item.getValues());
                    }
                });
            }
        });
    }

    private SelectItem[] getColumnsAsReferenceItem(ArrayList<ModelField> fields) {
        ArrayList<SelectItem> result = new ArrayList<>();
        if (fields.size() > 0) {
            for (ModelField entry : fields) {
                String localized = getLocalizer().localizeByFieldID(entry.getForm_ID(), entry.getField_ID());
                String name = localized != null ? localized : (entry.getField_ID().contains("string_value") || entry.getField_ID().contains("double_value") || entry.getField_ID().contains("date_value") ? entry.getLabel() : entry.getField_ID());
                String description = entry.getField_ID();
                result.add(new SelectItem(entry.getObjectID(), name, description));
            }
        }
        result.sort((item1, item2) -> item1.getName().compareTo(item2.getName()));
        return result.toArray(new SelectItem[]{});
    }

    private void setItems(SelectItem[] fields) {
        firstName.setItems(fields, objectID == null ? wfmStrings.firstName() : wfmStrings.pleaseSelect());
        lastName.setItems(fields, objectID == null ? wfmStrings.lastName() : wfmStrings.pleaseSelect());
        basicSalary.setItems(fields, objectID == null ? wfmStrings.basicSalary() : wfmStrings.pleaseSelect());
        passportNumber.setItems(fields, objectID == null ? wfmStrings.passportNumber() : wfmStrings.pleaseSelect());
        passportIssueBy.setItems(fields, objectID == null ? wfmStrings.passportIssueBy() : wfmStrings.pleaseSelect());
        passportIssueDate.setItems(fields, objectID == null ? wfmStrings.passportIssueDate() : wfmStrings.pleaseSelect());
        passportExpiryDate.setItems(fields, objectID == null ? wfmStrings.passportExpireDate() : wfmStrings.pleaseSelect());
        insuranceNumber.setItems(fields, objectID == null ? wfmStrings.insuranseNumber() : wfmStrings.pleaseSelect());
        insuranceExpiryDate.setItems(fields, objectID == null ? wfmStrings.insuranceExpiryDate() : wfmStrings.pleaseSelect());
        visaNumber.setItems(fields, objectID == null ? wfmStrings.visaNumber() : wfmStrings.pleaseSelect());
        visaIssueDate.setItems(fields, objectID == null ? wfmStrings.visaIssueDate() : wfmStrings.pleaseSelect());
        visaExpirationDate.setItems(fields, objectID == null ? wfmStrings.visaExpirationDate() : wfmStrings.pleaseSelect());
        bankName.setItems(fields, objectID == null ? wfmStrings.bankName() : wfmStrings.pleaseSelect());
        accountName.setItems(fields, objectID == null ? wfmStrings.accountName() : wfmStrings.pleaseSelect());
        accountNumber.setItems(fields, objectID == null ? wfmStrings.accountNumber() : wfmStrings.pleaseSelect());
        bankAddress.setItems(fields, objectID == null ? wfmStrings.bankAddress() : wfmStrings.pleaseSelect());
        swiftCode.setItems(fields, objectID == null ? wfmStrings.swiftCode() : wfmStrings.pleaseSelect());
        sortCode.setItems(fields, objectID == null ? wfmStrings.sortCode() : wfmStrings.pleaseSelect());
        ibanCode.setItems(fields, objectID == null ? wfmStrings.ibanCode() : wfmStrings.pleaseSelect());
        agentID.setItems(fields, objectID == null ? wfmStrings.agentID() : wfmStrings.pleaseSelect());
        if (tbValues != null && tbValues.length > 0) {
            int i = 0;
            for (DataListBox box : tbValues) {
                String title = companyCustomFieldItems.size() > i && companyCustomFieldItems.get(i) != null ? companyCustomFieldItems.get(i).getFieldName() : null;
                box.setItems(fields, objectID == null ? title : wfmStrings.pleaseSelect());
                i++;
            }
        }
    }

    private void setValues(HashMap<String, String> values) {
        if(values != null){
            if(values.containsKey(CustomFormConstants.FIRST_NAME)){
                firstName.setSelectedByCode(values.get(CustomFormConstants.FIRST_NAME));
            }
            if(values.containsKey(CustomFormConstants.LAST_NAME)){
                lastName.setSelectedByCode(values.get(CustomFormConstants.LAST_NAME));
            }
            if(values.containsKey(CustomFormConstants.SALARY_AMOUNT)){
                basicSalary.setSelectedByCode(values.get(CustomFormConstants.SALARY_AMOUNT));
            }
            if(values.containsKey(CustomFormConstants.PASSPORT_NUMBER)){
                passportNumber.setSelectedByCode(values.get(CustomFormConstants.PASSPORT_NUMBER));
            }
            if(values.containsKey(CustomFormConstants.PASSPORT_ISSUE)){
                passportIssueBy.setSelectedByCode(values.get(CustomFormConstants.PASSPORT_ISSUE));
            }
            if(values.containsKey(CustomFormConstants.PASSPORT_ISSUE_DATE)){
                passportIssueDate.setSelectedByCode(values.get(CustomFormConstants.PASSPORT_ISSUE_DATE));
            }
            if(values.containsKey(CustomFormConstants.PASSPORT_EXPIRY_DATE)){
                passportExpiryDate.setSelectedByCode(values.get(CustomFormConstants.PASSPORT_EXPIRY_DATE));
            }
            if(values.containsKey(CustomFormConstants.INSURANCE_NUMBER)){
                insuranceNumber.setSelectedByCode(values.get(CustomFormConstants.INSURANCE_NUMBER));
            }
            if(values.containsKey(CustomFormConstants.INSURANCE_EXPIRY_DATE)){
                insuranceExpiryDate.setSelectedByCode(values.get(CustomFormConstants.INSURANCE_EXPIRY_DATE));
            }
            if(values.containsKey(CustomFormConstants.VISA_NUMBER)){
                visaNumber.setSelectedByCode(values.get(CustomFormConstants.VISA_NUMBER));
            }
            if(values.containsKey(CustomFormConstants.VISA_ISSUE_DATE)){
                visaIssueDate.setSelectedByCode(values.get(CustomFormConstants.VISA_ISSUE_DATE));
            }
            if(values.containsKey(CustomFormConstants.VISA_EXPIRATION_DATE)){
                visaExpirationDate.setSelectedByCode(values.get(CustomFormConstants.VISA_EXPIRATION_DATE));
            }
            if(values.containsKey(CustomFormConstants.BANK_NAME)){
                bankName.setSelectedByCode(values.get(CustomFormConstants.BANK_NAME));
            }
            if(values.containsKey(CustomFormConstants.ACCOUNT_NAME)){
                accountName.setSelectedByCode(values.get(CustomFormConstants.ACCOUNT_NAME));
            }
            if(values.containsKey(CustomFormConstants.ACCOUNT_NUMBER)){
                accountNumber.setSelectedByCode(values.get(CustomFormConstants.ACCOUNT_NUMBER));
            }
            if(values.containsKey(CustomFormConstants.BANK_ADDRESS)){
                bankAddress.setSelectedByCode(values.get(CustomFormConstants.BANK_ADDRESS));
            }
            if(values.containsKey(CustomFormConstants.SWIFT_CODE)){
                swiftCode.setSelectedByCode(values.get(CustomFormConstants.SWIFT_CODE));
            }
            if(values.containsKey(CustomFormConstants.SORT_CODE)){
                sortCode.setSelectedByCode(values.get(CustomFormConstants.SORT_CODE));
            }
            if(values.containsKey(CustomFormConstants.IBAN_CODE)){
                ibanCode.setSelectedByCode(values.get(CustomFormConstants.IBAN_CODE));
            }
            if(values.containsKey(CustomFormConstants.AGENT_ID)){
                agentID.setSelectedByCode(values.get(CustomFormConstants.AGENT_ID));
            }
            if (tbValues != null) {
                for (DataListBox box : tbValues) {
                    String code = String.valueOf(box.getLayoutData());
                    if(values.containsKey(code)){
                        box.setSelectedByCode(values.get(code));
                    }
                }
            }
        }
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.WORKFLOW_EMPLOYEE_FORM;
    }

    @Override
    protected String getFormType() {
        return objectID == null ? LayoutRPC.ADD : LayoutRPC.EDIT;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    @Override
    public String getIconStyle() {
        return null;
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
