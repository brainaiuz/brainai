package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.contact.client.rpc.DependentItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.ReferenceItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.CountryLookUp;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.PhoneNumber;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.core.client.ui.upload.GeneralFileUpload;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * User: unni
 * Date: Oct 21, 2009
 * Time: 9:59:33 PM
 */
public class AddDependentView extends CustomForm2 implements Constants, Colapse {

    protected static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    protected DependentItem item;

    private TextBox address;
    private TextBox address_b;
    private TextBox city;
    private CountryLookUp country;
    private FormHasCustomField customFieldUtil;
    private TextBox firstName;
    private TextBox lastName;
    private TextBox middleName;
    private DataListBox relationship;
    private PhoneNumber phone1;
    private GeneralFileUpload fileUpload;
    private SelectItem[] relationships;

    private Integer int_employeeID;
    private Integer int_objectID;

    private ArrayList<ReferenceItem> references;
    private String successMessage = Utils.textFormat(wfmStrings.messSuccessfullyAdded(), hrmsStrings.dependant());
    private String test_code_ID_name = "add_dependent_view_";
    private LinkedHashMap<String, FormProperty> formProperty;
    private boolean isFromCandidate = false;

    private Integer candidateId;


    public AddDependentView(Integer int_objectID) {
        super("adddependent", hrmsStrings.addDependant());
        if (int_objectID != null) {
            this.int_objectID = int_objectID;
            setDescription(hrmsStrings.editDependent());
        }
    }

    public AddDependentView(Integer candidateId,boolean isFromCandidate) {
        super("adddependent", hrmsStrings.addDependant());
        this.candidateId = candidateId;
        this.isFromCandidate = isFromCandidate;
    }

    public AddDependentView(Integer int_objectID, Integer int_employeeID) {
        super("adddependent", hrmsStrings.addDependant());
        if (int_objectID != null) {
            this.int_objectID = int_objectID;
            setDescription(hrmsStrings.editDependent());
        }
        if (int_employeeID != null) {
            this.int_employeeID = int_employeeID;
        }
    }

    public AddDependentView(String name, String description, String test_code_ID_name, Integer int_objectID) {
        super(name, description);
        this.test_code_ID_name = test_code_ID_name;
        this.int_objectID = int_objectID;
    }

    public AddDependentView(String name, String description, String test_code_ID_name, Integer int_objectID,boolean isFromCandidate) {
        super(name, description);
        this.test_code_ID_name = test_code_ID_name;
        this.int_objectID = int_objectID;
        this.isFromCandidate = isFromCandidate;
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    @Override
    public String getIconStyle() {
        return "hrms add-edit-depent";
    }

    @Override
    protected void initPredefinedValues() {
        addPredefinedValues(CustomFormConstants.RELATIONSHIP, relationship.getItems());
    }

    @Override
    protected void addButtons() {
        if (int_objectID == null) {
            MaterialLink save = new MaterialLink(wfmStrings.save());
            MaterialSplitButton splitButton = new MaterialSplitButton(save);
            save.addClickHandler(event -> save(true));
            save.ensureDebugId(test_code_ID_name + "save_and_close_button");

            MaterialLink saveAndNewButton = new MaterialLink(wfmStrings.saveAndNew());
            saveAndNewButton.addClickHandler(event -> save(false));
            saveAndNewButton.ensureDebugId(test_code_ID_name + "save_and_new_button");

            splitButton.addItem(saveAndNewButton);
            addButton(splitButton);

        } else {
            WfmButton2 updateButton = addButton(wfmStrings.update(), event -> {
                //update logic
                save(true);
            });
            updateButton.ensureDebugId(test_code_ID_name + "update_button");
        }
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        Integer depId = null;
        if (!isFromCandidate) {
            depId = int_objectID;
        }
        HrmsService.App.get().editDependent(depId, new AbstractAsyncCallback<DependentItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(final DependentItem o) {
                LoadingPanel.loading(false);
                item = o;
                fillFormWithData();
                initPredefinedValues();
                if (int_objectID == null) {
                    setDefaultValuesByFormProperty();
                }
            }
        });
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.DEPENDENT_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return null;
    }

    protected void fillFormWithData() {
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
        if (int_objectID != null) {
            successMessage = Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), hrmsStrings.dependant());
        }
        //dependent first name
        if (item.getFirstName() != null) {
            firstName.setText(item.getFirstName());
        }
        //dependent last name
        if (item.getLastName() != null) {
            lastName.setText(item.getLastName());
        }
        //dependent middle name
        if (item.getMiddleName() != null) {
            middleName.setText(item.getMiddleName());
        }
        //dependent relation ship
        if (item.getRelationship() != null) {
            relationship.setSelectedByDescription(item.getRelationship());
        } else {
            relationship.setSelectedNullLabel();
        }
        //dependent address
        if (item.getAddress() != null) {
            address.setText(item.getAddress());
        }
        //dependent address b
        if (item.getAddressb() != null) {
            address_b.setText(item.getAddressb());
        }
        //dependent city
        if (item.getCity() != null) {
            city.setText(item.getCity());
        }
        //dependent countries
        if (item.getCountry() != null) {
            country.setSelected(item.getCountryId(), item.getCountryName());
        }
        //dependent phone 1
        if (item.getPhone1() != null) {
            phone1.setData(item.getPhone1());
        }

        //dependent custom fields
        getCustomFieldUtil().fillCustomFieldsWithData(item.getCustomFields());
    }

    @Override
    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.Dependent, getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                AddDependentView.super.onInitialize();
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                if (result != null) {
                    getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                    formProperty = result.getFormPropertyMap();
                }
                AddDependentView.super.onInitialize();
            }
        });
        return null;
    }

    protected void registerFields() {

        //dependent first name
        firstName = new TextBox();
        firstName.addStyleName(DEFAULT_WIDTH);
        firstName.ensureDebugId(test_code_ID_name + "first_name");
        //dependent last name
        lastName = new TextBox();
        lastName.addStyleName(DEFAULT_WIDTH);
        lastName.ensureDebugId(test_code_ID_name + "last_name");
        //dependent middle name
        middleName = new TextBox();
        middleName.addStyleName(DEFAULT_WIDTH);
        middleName.ensureDebugId(test_code_ID_name + "middle_name");
        //dependent relation ship

        relationship = new DataListBox();
        relationship.addStyleName(DEFAULT_WIDTH);
        relationship.ensureDebugId(test_code_ID_name + "relationship");
        relationship.setSelectedNullLabel();
        getRelationship();
        //dependent address
        address = new TextBox();
        address.addStyleName(DEFAULT_WIDTH);
        address.ensureDebugId(test_code_ID_name + "address");
        //dependent address b
        address_b = new TextBox();
        address_b.addStyleName(DEFAULT_WIDTH);
        address_b.ensureDebugId(test_code_ID_name + "address_b");
        //dependent city
        city = new TextBox();
        city.addStyleName(DEFAULT_WIDTH);
        city.ensureDebugId(test_code_ID_name + "city");
        //dependent country
        country = new CountryLookUp();
        country.addStyleName(DEFAULT_WIDTH);
        country.ensureDebugId(test_code_ID_name + "country");
        //dependent phone 1
        phone1 = new PhoneNumber("");
        phone1.addStyleName(DEFAULT_WIDTH);
        phone1.ensureDebugId(test_code_ID_name + "phone_1");

        fileUpload = new GeneralFileUpload(F_DEPENDENTS, int_objectID, int_objectID);

        initializeForms();
    }

    private void getRelationship() {
        AllInOneService.App.get().getReferenceChildren("RELATIONSHIP", new AbstractAsyncCallback<ArrayList<ReferenceItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(ArrayList<ReferenceItem> result) {
                references = result;
                ArrayList<SelectItem> selectItems = new ArrayList<>();
                if (references != null && references.size() > 0) {
                    int index = 0;
                    for (ReferenceItem referenceItem : references) {
                        SelectItem selectItem = new SelectItem(0, referenceItem.getName(), referenceItem.getDescription());
                        selectItems.add(selectItem);
                    }
                    SelectItem[] selectItems1 = new SelectItem[selectItems.size()];
                    selectItems.toArray(selectItems1);
                    relationships = selectItems1;
                    relationship.setItems(relationships);
                }
            }
        });
    }

    protected void initializeForms() {
        addTitleField(CustomFormConstants.DETAILS, wfmStrings.details());
        if (formProperty != null && formProperty.get(CustomFormConstants.FIRST_NAME) != null) {
            addField(CustomFormConstants.FIRST_NAME, firstName, getTitle(formProperty.get(CustomFormConstants.FIRST_NAME).isChanged() ? formProperty.get(CustomFormConstants.FIRST_NAME).getTitle() : wfmStrings.firstName(),
                    formProperty.get(CustomFormConstants.FIRST_NAME).isRequired()),false,
                    formProperty.get(CustomFormConstants.FIRST_NAME).isInformation());
            firstName.setEnabled(!formProperty.get(CustomFormConstants.FIRST_NAME).isDisabled());
            if (formProperty.get(CustomFormConstants.FIRST_NAME).isInformation()){
                new KpiToolTip(firstName,formProperty.get(CustomFormConstants.FIRST_NAME).getInformationText());
            }
        } else {
            addField(CustomFormConstants.FIRST_NAME, firstName, getTitle(wfmStrings.firstName(), true));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.MIDDLE_NAME) != null) {
            addField(CustomFormConstants.MIDDLE_NAME, middleName, getTitle(formProperty.get(CustomFormConstants.MIDDLE_NAME).isChanged() ? formProperty.get(CustomFormConstants.MIDDLE_NAME).getTitle() : wfmStrings.middleName(),
                    formProperty.get(CustomFormConstants.MIDDLE_NAME).isRequired()),false,
                    formProperty.get(CustomFormConstants.MIDDLE_NAME).isInformation());
            middleName.setEnabled(!formProperty.get(CustomFormConstants.MIDDLE_NAME).isDisabled());
            if (formProperty.get(CustomFormConstants.MIDDLE_NAME).isInformation()){
                new KpiToolTip(middleName,formProperty.get(CustomFormConstants.MIDDLE_NAME).getInformationText());
            }
        } else {
            addField(CustomFormConstants.MIDDLE_NAME, middleName, getTitle(wfmStrings.middleName()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.LAST_NAME) != null) {
            addField(CustomFormConstants.LAST_NAME, lastName, getTitle(formProperty.get(CustomFormConstants.LAST_NAME).isChanged() ? formProperty.get(CustomFormConstants.LAST_NAME).getTitle() : wfmStrings.lastName(),
                    formProperty.get(CustomFormConstants.LAST_NAME).isRequired()),false,
                    formProperty.get(CustomFormConstants.LAST_NAME).isInformation());
            lastName.setEnabled(!formProperty.get(CustomFormConstants.LAST_NAME).isDisabled());
            if (formProperty.get(CustomFormConstants.LAST_NAME).isInformation()){
                new KpiToolTip(lastName,formProperty.get(CustomFormConstants.LAST_NAME).getInformationText());
            }
        } else {
            addField(CustomFormConstants.LAST_NAME, lastName, getTitle(wfmStrings.lastName(), true));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.RELATIONSHIP) != null) {
            addField(CustomFormConstants.RELATIONSHIP, relationship, getTitle(formProperty.get(CustomFormConstants.RELATIONSHIP).isChanged() ? formProperty.get(CustomFormConstants.RELATIONSHIP).getTitle() : wfmStrings.relationship(),
                    formProperty.get(CustomFormConstants.RELATIONSHIP).isRequired()),false,
                    formProperty.get(CustomFormConstants.RELATIONSHIP).isInformation());
            relationship.setEnabled(!formProperty.get(CustomFormConstants.LAST_NAME).isDisabled());
            if (formProperty.get(CustomFormConstants.RELATIONSHIP).isInformation()){
                new KpiToolTip(relationship,formProperty.get(CustomFormConstants.RELATIONSHIP).getInformationText());
            }
        } else {
            addField(CustomFormConstants.RELATIONSHIP, relationship, getTitle(wfmStrings.relationship()));
        }

        if (formProperty != null && formProperty.get(CustomFormConstants.ADDRESS) != null) {
            addField(CustomFormConstants.ADDRESS, address, getTitle(formProperty.get(CustomFormConstants.ADDRESS).isChanged() ? formProperty.get(CustomFormConstants.ADDRESS).getTitle() : wfmStrings.streetAddress1(),
                    formProperty.get(CustomFormConstants.ADDRESS).isRequired()),false,
                    formProperty.get(CustomFormConstants.ADDRESS).isInformation());
            address.setEnabled(!formProperty.get(CustomFormConstants.ADDRESS).isDisabled());
            if (formProperty.get(CustomFormConstants.ADDRESS).isInformation()){
                new KpiToolTip(address,formProperty.get(CustomFormConstants.ADDRESS).getInformationText());
            }
        } else {
            addField(CustomFormConstants.ADDRESS, address, getTitle(wfmStrings.streetAddress1()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.ADDRESS_2) != null) {
            addField(CustomFormConstants.ADDRESS_2, address_b, getTitle(formProperty.get(CustomFormConstants.ADDRESS_2).isChanged() ? formProperty.get(CustomFormConstants.ADDRESS_2).getTitle() : wfmStrings.streetAddress2(),
                    formProperty.get(CustomFormConstants.ADDRESS_2).isRequired()),false,
                    formProperty.get(CustomFormConstants.ADDRESS_2).isInformation());
            address_b.setEnabled(!formProperty.get(CustomFormConstants.ADDRESS_2).isDisabled());
            if (formProperty.get(CustomFormConstants.ADDRESS_2).isInformation()){
                new KpiToolTip(address_b,formProperty.get(CustomFormConstants.ADDRESS_2).getInformationText());
            }
        } else {
            addField(CustomFormConstants.ADDRESS_2, address_b, getTitle(wfmStrings.streetAddress2()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.CITY_) != null) {
            addField(CustomFormConstants.CITY_, city, getTitle(formProperty.get(CustomFormConstants.CITY_).isChanged() ? formProperty.get(CustomFormConstants.CITY_).getTitle() : wfmStrings.city(),
                    formProperty.get(CustomFormConstants.CITY_).isRequired()),false,
                    formProperty.get(CustomFormConstants.CITY_).isInformation());
            city.setEnabled(!formProperty.get(CustomFormConstants.CITY_).isDisabled());
            if (formProperty.get(CustomFormConstants.CITY_).isInformation()){
                new KpiToolTip(city,formProperty.get(CustomFormConstants.CITY_).getInformationText());
            }
        } else {
            addField(CustomFormConstants.CITY_, city, getTitle(wfmStrings.city()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.COUNTRY_) != null) {
            addField(CustomFormConstants.COUNTRY_, country, getTitle(formProperty.get(CustomFormConstants.COUNTRY_).isChanged() ? formProperty.get(CustomFormConstants.COUNTRY_).getTitle() : wfmStrings.country(),
                    formProperty.get(CustomFormConstants.COUNTRY_).isRequired()),false,
                    formProperty.get(CustomFormConstants.COUNTRY_).isInformation());
            country.setEnabled(!formProperty.get(CustomFormConstants.COUNTRY_).isDisabled());
            if (formProperty.get(CustomFormConstants.COUNTRY_).isInformation()){
                new KpiToolTip(country,formProperty.get(CustomFormConstants.COUNTRY_).getInformationText());
            }
        } else {
            addField(CustomFormConstants.COUNTRY_, country, getTitle(wfmStrings.country()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.PHONE) != null) {
            addField(CustomFormConstants.PHONE, phone1, getTitle(formProperty.get(CustomFormConstants.PHONE).isChanged() ? formProperty.get(CustomFormConstants.PHONE).getTitle() : wfmStrings.phone(),
                    formProperty.get(CustomFormConstants.PHONE).isRequired()),false,
                    formProperty.get(CustomFormConstants.PHONE).isInformation());
            phone1.getField().setEnabled(!formProperty.get(CustomFormConstants.PHONE).isDisabled());
            if (formProperty.get(CustomFormConstants.PHONE).isInformation()){
                new KpiToolTip(phone1,formProperty.get(CustomFormConstants.PHONE).getInformationText());
            }
        } else {
            addField(CustomFormConstants.PHONE, phone1.getField(), getTitle(wfmStrings.phone()));
        }
        addField(CustomFormConstants.ATTACHMENTS, fileUpload, wfmStrings.attachments());
        //custom fields -> 2
        getCustomFieldUtil().drawCustomFields(this, int_objectID);
        addTitleField(CustomFormConstants.ADDITIONAL_INFORMATION, wfmStrings.additionalInformation());

        show();
    }


    private void reInit() {
        registerFields();
        initForm();
    }

    protected void save(final boolean saveAndClose) {
        if (!validate()) {
            return;
        }
        setValues();

        LoadingPanel.loading(true);
        HrmsService.App.get().saveDependent(item, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(final Void o) {
                LoadingPanel.loading(false);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_DEPENDENT_ADD_EDIT, o, AddDependentView.this);
                Info.show(successMessage, Info.Type.INFO);
                onShellOk(saveAndClose);
            }
        });
    }

    private void onShellOk(boolean saveAndClose) {
        if (saveAndClose) {
            closeTab();
            if (int_objectID != null) {
                SinksContainerFactory.entryPoint.onHistoryChanged("dependent|summary/" + int_objectID, item.getFirstName(), item.getLastName());
            }
        } else {
            reInit();
        }
    }

    private void setValues() {
        //dependent ID
        item.setObjectId(int_objectID);
        //dependent employee ID
        item.setEmployeeId(int_employeeID);
        //dependent first name
        item.setFirstName(firstName.getText());
        //dependent last name
        item.setLastName(lastName.getText());
        //dependent middle name
        item.setMiddleName(middleName.getText());
        //dependent relationship
        if (relationship.getSelectedItem() != null) {
            item.setRelationship(relationship.getSelectedItem().getDescription());
        } else {
            item.setRelationship(null);
        }
        //dependent address 1
        item.setAddress(address.getText());
        //dependent address 2
        item.setAddressb(address_b.getText());
        //dependent city
        item.setCity(city.getText());
        //dependent phone 1
        item.setPhone1(phone1.toString());
        //dependent phone 2
        //dependent country ID
        if (country.getSelectedItem() != null) {
            item.setCountryId(country.getSelectedItem().getId());
        }
        //dependent custom fields
        item.setCustomFields(getCustomFieldUtil().getCustomFieldsValue());
        item.setAttachments(fileUpload.getAttachedFiles());
        item.setFromCandidate(isFromCandidate);
        item.setCandidateId(candidateId);
    }

    private boolean validate() {
        clearErrorStyle();
        int errors = 0;
        if (formProperty != null && formProperty.get(CustomFormConstants.FIRST_NAME) != null && formProperty.get(CustomFormConstants.FIRST_NAME).isRequired()) {
            errors += markAsError(CustomFormConstants.FIRST_NAME, firstName, !Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.FIRST_NAME).isChanged() ?
                    formProperty.get(CustomFormConstants.FIRST_NAME).getTitle() : wfmStrings.firstName(), firstName, formProperty.get(CustomFormConstants.FIRST_NAME).getMinChar()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.LAST_NAME) != null && formProperty.get(CustomFormConstants.LAST_NAME).isRequired()) {
            errors += markAsError(CustomFormConstants.LAST_NAME, lastName, !Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.LAST_NAME).isChanged() ?
                    formProperty.get(CustomFormConstants.LAST_NAME).getTitle() : wfmStrings.lastName(), lastName, formProperty.get(CustomFormConstants.LAST_NAME).getMinChar()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.MIDDLE_NAME) != null && formProperty.get(CustomFormConstants.MIDDLE_NAME).isRequired()) {
            errors += markAsError(CustomFormConstants.MIDDLE_NAME, middleName, !Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.MIDDLE_NAME).isChanged() ?
                    formProperty.get(CustomFormConstants.MIDDLE_NAME).getTitle() : wfmStrings.middleName(), middleName, formProperty.get(CustomFormConstants.MIDDLE_NAME).getMinChar()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.RELATIONSHIP) != null && formProperty.get(CustomFormConstants.RELATIONSHIP).isRequired()) {
            errors += markAsError(CustomFormConstants.RELATIONSHIP, relationship, !Validation.validateListBoxRequired(relationship));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.ADDRESS) != null && formProperty.get(CustomFormConstants.ADDRESS).isRequired()) {
            errors += markAsError(CustomFormConstants.ADDRESS, address, !Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.ADDRESS).isChanged() ?
                    formProperty.get(CustomFormConstants.ADDRESS).getTitle() : wfmStrings.address(), address, formProperty.get(CustomFormConstants.ADDRESS).getMinChar()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.ADDRESS_2) != null && formProperty.get(CustomFormConstants.ADDRESS_2).isRequired()) {
            errors += markAsError(CustomFormConstants.ADDRESS_2, address_b, !Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.ADDRESS_2).isChanged() ?
                    formProperty.get(CustomFormConstants.ADDRESS_2).getTitle() : wfmStrings.streetAddress2(), address_b, formProperty.get(CustomFormConstants.ADDRESS_2).getMinChar()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.CITY_) != null && formProperty.get(CustomFormConstants.CITY_).isRequired()) {
            errors += markAsError(CustomFormConstants.CITY_, city, !Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.CITY_).isChanged() ?
                    formProperty.get(CustomFormConstants.CITY_).getTitle() : wfmStrings.city(), city, formProperty.get(CustomFormConstants.CITY_).getMinChar()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.COUNTRY_) != null && formProperty.get(CustomFormConstants.COUNTRY_).isRequired()) {
            errors += markAsError(CustomFormConstants.COUNTRY_, country, !Validation.validateListBoxRequired(country, null, ""));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.PHONE) != null && formProperty.get(CustomFormConstants.PHONE).isRequired()) {
            errors += markAsError(CustomFormConstants.PHONE, phone1, !Validation.validateTextBoxRequiredAndCharLimit(formProperty.get(CustomFormConstants.PHONE).isChanged() ?
                    formProperty.get(CustomFormConstants.PHONE).getTitle() : wfmStrings.phone(), phone1.getPhoneFeild(), formProperty.get(CustomFormConstants.PHONE).getMinChar()));
        }
        errors += getCustomFieldUtil().validateCustomFields();

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    protected void setDefaultValuesByFormProperty() {
        if (formProperty != null && formProperty.get(CustomFormConstants.FIRST_NAME) != null && formProperty.get(CustomFormConstants.FIRST_NAME).getDefaultValue() != null) {
            firstName.setText(formProperty.get(CustomFormConstants.FIRST_NAME).getDefaultValue());
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.LAST_NAME) != null && formProperty.get(CustomFormConstants.LAST_NAME).getDefaultValue() != null) {
            lastName.setText(formProperty.get(CustomFormConstants.LAST_NAME).getDefaultValue());
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.MIDDLE_NAME) != null && formProperty.get(CustomFormConstants.MIDDLE_NAME).getDefaultValue() != null) {
            middleName.setText(formProperty.get(CustomFormConstants.MIDDLE_NAME).getDefaultValue());
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.RELATIONSHIP) != null && formProperty.get(CustomFormConstants.RELATIONSHIP).getDefaultValue() != null) {
            relationship.setSelected(new SelectItem(formProperty.get(CustomFormConstants.RELATIONSHIP).getSelectedId(), formProperty.get(CustomFormConstants.RELATIONSHIP).getDefaultValue()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.COUNTRY_) != null && formProperty.get(CustomFormConstants.COUNTRY_).getDefaultValue() != null) {
            country.setSelected(new SelectItem(formProperty.get(CustomFormConstants.COUNTRY_).getSelectedId(), formProperty.get(CustomFormConstants.COUNTRY_).getDefaultValue()));
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.ADDRESS) != null && formProperty.get(CustomFormConstants.ADDRESS).getDefaultValue() != null) {
            address.setText(formProperty.get(CustomFormConstants.ADDRESS).getDefaultValue());
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.ADDRESS_2) != null && formProperty.get(CustomFormConstants.ADDRESS_2).getDefaultValue() != null) {
            address_b.setText(formProperty.get(CustomFormConstants.ADDRESS_2).getDefaultValue());
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.CITY_) != null && formProperty.get(CustomFormConstants.CITY_).getDefaultValue() != null) {
            city.setText(formProperty.get(CustomFormConstants.CITY_).getDefaultValue());
        }
        if (formProperty != null && formProperty.get(CustomFormConstants.PHONE) != null && formProperty.get(CustomFormConstants.PHONE).getDefaultValue() != null) {
            phone1.setData(formProperty.get(CustomFormConstants.PHONE).getDefaultValue());
        }
    }

    @Override
    public String getFieldLabel(String fieldID) {
        if (fieldID != null) {
            if (CustomFormConstants.DETAILS.equals(fieldID)) {
                return wfmStrings.details();
            } else if (CustomFormConstants.FIRST_NAME.equals(fieldID)) {
                return wfmStrings.firstName();
            } else if (CustomFormConstants.MIDDLE_NAME.equals(fieldID)) {
                return wfmStrings.middleName();
            } else if (CustomFormConstants.LAST_NAME.equals(fieldID)) {
                return wfmStrings.lastName();
            } else if (CustomFormConstants.RELATIONSHIP.equals(fieldID)) {
                return wfmStrings.relationship();
            } else if (CustomFormConstants.ADDRESS.equals(fieldID)) {
                return wfmStrings.streetAddress1();
            } else if (CustomFormConstants.ADDRESS_2.equals(fieldID)) {
                return wfmStrings.streetAddress2();
            } else if (CustomFormConstants.CITY_.equals(fieldID)) {
                return wfmStrings.city();
            } else if (CustomFormConstants.TOWN_.equals(fieldID)) {
                return wfmStrings.city();
            } else if (CustomFormConstants.COUNTRY_.equals(fieldID)) {
                return wfmStrings.country();
            } else if (CustomFormConstants.PHONE.equals(fieldID)) {
                return wfmStrings.phone();
            } else if (CustomFormConstants.PHONE_2.equals(fieldID)) {
                return wfmStrings.phone()+" 2";
            } else if (CustomFormConstants.ATTACHMENTS.equals(fieldID)) {
                return wfmStrings.attachments();
            } else if (CustomFormConstants.ADDITIONAL_INFORMATION.equals(fieldID)) {
                return wfmStrings.additionalInformation();
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