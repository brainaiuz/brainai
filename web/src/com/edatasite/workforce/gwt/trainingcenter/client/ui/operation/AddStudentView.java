package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.ui.AddressWidget;
import com.edatasite.workforce.gwt.contact.client.ui.DOBWidget;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm2;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.localization.CoreMessages;
import com.edatasite.workforce.gwt.core.client.reference.AddressReference;
import com.edatasite.workforce.gwt.core.client.reference.PhoneReference;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CommonServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.Errors;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.ProfileImage;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiRadioButton;
import com.edatasite.workforce.gwt.core.client.ui.components.form.AdvancedInputGroup;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.PhoneNumber;
import com.edatasite.workforce.gwt.core.client.ui.lookup.CrmAccountLookUp;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableNewUI;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.MultiTableWidgets;
import com.edatasite.workforce.gwt.core.client.ui.multiwidget.WidgetsMap;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.trainingcenter.client.TCConstants;
import com.edatasite.workforce.gwt.trainingcenter.client.localization.TCStrings;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.TCService;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.student.StudentItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * User: Ilhombek
 * Date: 7/18/12
 * Time: 5:06 PM
 */
public class AddStudentView extends CustomForm2 implements TCConstants, Constants, CommandConstants, Colapse {

    public static final TCStrings tcStrings = TCStrings.App.get();

    public static final CoreMessages coreMessages = CoreMessages.App.get();
    protected final CommonServiceAsync commonService = CommonService.App.get();
    public StudentItem item;
    public String imageURL;

    public static final String PARAM_TEXT_BOX = "PARAM_TEXT_BOX";
    private static final String RELATION_LIST_BOX = "RELATION_LIST_BOX";
    private static final String PRIMARY_RADIO_BUTTON = "PRIMARY_RADIO_BUTTON";

    public AddressWidget addressWidget;
    public Address address;
    private TextBox cardN;
    private DataListBox cardType;
    private CrmAccountLookUp customer;
    private DOBWidget dateOfBirth;
    private MultiTableNewUI emailT;
    private TextBox firstName;
    private TextBox lastName;
    private MultiTableNewUI phoneNumberT;
    private TextBox safetyPP;
    private TextBox companyEmployeeNumber;
    private TextBox departmentCode;
    private DataListBox studentStatus;
    private RadioButton male;
    private RadioButton female;
    private FlexTable genderTable;
    public ProfileImage profilePicture;
    private TextBox txtRefIndNumber;
    private TextBox txtNationality;
    private LinkedHashMap<String, FormProperty> formPropertyMap;
    private HTML errorMessage;

    private Integer objectID;

    /**
     * Constructor
     *
     * @param objectID - student ID
     */
    public AddStudentView(Integer objectID) {
        super("addStudent", (objectID == null ? tcStrings.addStudentOnly() : tcStrings.editStudentOnly()));
        this.objectID = objectID;
    }

    /**
     * Constructor
     *
     * @param name        - view name
     * @param description - view description
     * @param objectID    - student ID
     */
    public AddStudentView(String name, String description, Integer objectID) {
        super(name, description);
        this.objectID = objectID;
    }
    FormHasCustomField customFieldUtil;

    @Override
    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.Students,getFormID(), new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                formPropertyMap = result.getFormPropertyMap();
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                AddStudentView.super.onInitialize();
            }
        });
        return null;
    }

    @Override
    protected void registerFields() {

        profilePicture = new ProfileImage(objectID, LayoutRPC.STUDENT_FORM);

        String student_add_edit_view = "student_add_edit_view_";
        //customer
        customer = new CrmAccountLookUp(CrmAccountLookUp.CUSTOMER, true);
        customer.addStyleName(DEFAULT_WIDTH);
        customer.ensureDebugId(student_add_edit_view + "customer");
        //add customer link
        SimpleLink addCustomerLink = new SimpleLink(wfmStrings.add(), "client|addaccount/add");
        addCustomerLink.addStyleName("addField");
        addCustomerLink.ensureDebugId(student_add_edit_view + "add_customer_link");
        new KpiToolTip(addCustomerLink, Property.get(Constants.CLIENT_LIST, wfmStrings.customer()));

        firstName = new TextBox();
        firstName.addStyleName(DEFAULT_WIDTH);
        firstName.ensureDebugId(student_add_edit_view + "first_name");
        //last name
        lastName = new TextBox();
        lastName.addStyleName(DEFAULT_WIDTH);
        lastName.ensureDebugId(student_add_edit_view + "last_name");
        //student e-mail
        emailT = new MultiTableNewUI(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getEmailWidgets(null, null, null);
            }

            @Override
            public boolean isFilled() {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }
        }, false);

        emailT.addStyleName(DEFAULT_WIDTH);
        emailT.ensureDebugId(student_add_edit_view + "email");
        //phone
        phoneNumberT = new MultiTableNewUI(new MultiTableWidgets() {
            @Override
            public WidgetsMap getWidgetsMaps() {
                return getPhoneWidgets(null, null, null);
            }

            @Override
            public boolean isFilled() {
                return false;  //To change body of implemented methods use File | Settings | File Templates.
            }
        }, false);
        phoneNumberT.addStyleName(DEFAULT_WIDTH);
        phoneNumberT.ensureDebugId(student_add_edit_view + "phone");
        //safetyPP number
        safetyPP = new TextBox();
        safetyPP.addStyleName(DEFAULT_WIDTH);
        safetyPP.ensureDebugId(student_add_edit_view + "safety_PP_number");
        //company Employee Number
        companyEmployeeNumber = new TextBox();
        companyEmployeeNumber.addStyleName(DEFAULT_WIDTH);
        companyEmployeeNumber.ensureDebugId(student_add_edit_view + "company_Employee_Number");

        //company Employee Number
        departmentCode = new TextBox();
        departmentCode.addStyleName(DEFAULT_WIDTH);
        departmentCode.ensureDebugId(student_add_edit_view + "department_number");
        //date of birth
        dateOfBirth = new DOBWidget();
//        dateOfBirth.setWidth("57px", "100px", "70px");
//        dateOfBirth.setSpacing(5);
        dateOfBirth.ensureDebugId(student_add_edit_view + "date_of_birth");
        //card type
        cardType = new DataListBox();
        cardType.addStyleName(DEFAULT_WIDTH);
        cardType.ensureDebugId(student_add_edit_view + "card_type");
        //card number
        cardN = new TextBox();
        cardN.addStyleName(DEFAULT_WIDTH);
        Validation.addNumericKeyboardListener(cardN);
        cardN.ensureDebugId(student_add_edit_view + "cardN_number");
        //active status
        studentStatus = new DataListBox();
        studentStatus.addStyleName(DEFAULT_WIDTH);
        studentStatus.setNullLabel(wfmStrings.active());
        studentStatus.setItems(new SelectItem[]{
                new SelectItem(1, wfmStrings.pending())
        });
        studentStatus.setSelected(1);
        studentStatus.ensureDebugId(student_add_edit_view + "student_status");

        //gender
        male = new KpiRadioButton("gender", wfmStrings.male());
        male.setValue(true);
        male.ensureDebugId(student_add_edit_view + "gender_male");
        //gender: female
        female = new KpiRadioButton("gender", wfmStrings.female());
        female.ensureDebugId(student_add_edit_view + "gender_female");

        genderTable = new FlexTable();
        genderTable.setCellSpacing(3);
        genderTable.setWidget(0, 0, male);
        genderTable.setWidget(0, 1, female);
        //address
        addressWidget = new AddressWidget(address, false, (objectID != null ? objectID.toString() : "add"), false, false);
        addressWidget.ensureDebugId(student_add_edit_view + "address_information");

        txtRefIndNumber = new TextBox();
        txtRefIndNumber.addStyleName(DEFAULT_WIDTH);
        txtRefIndNumber.ensureDebugId(student_add_edit_view + "refIndNumber");

        txtNationality = new TextBox();
        txtNationality.addStyleName(DEFAULT_WIDTH);
        txtNationality.ensureDebugId(student_add_edit_view + "nationality");

        //parent 1
        addTitleField(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_DETAILS, wfmStrings.studentDetails());
        AdvancedInputGroup customerWidget = new AdvancedInputGroup(null, customer, addCustomerLink, true, true);
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.CUSTOMER) != null) {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.CUSTOMER, customerWidget, getTitle(formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.CUSTOMER).isChanged() ? formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.CUSTOMER).getTitle() : wfmStrings.customer(), formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.CUSTOMER).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.CUSTOMER).isInformation());
            customerWidget.setEnabled(!formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.CUSTOMER).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.CUSTOMER).isInformation()) {
                new KpiToolTip(customerWidget, formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.CUSTOMER).getInformationText());
            }
        } else {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.CUSTOMER, customerWidget, getTitle(wfmStrings.customer(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.FIRST_NAME) != null) {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.FIRST_NAME, firstName, getTitle(formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.FIRST_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.FIRST_NAME).getTitle() : wfmStrings.firstName(), formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.FIRST_NAME).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.FIRST_NAME).isInformation());
            firstName.setEnabled(!formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.FIRST_NAME).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.FIRST_NAME).isInformation()) {
                new KpiToolTip(firstName, formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.FIRST_NAME).getInformationText());
            }
        } else {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.FIRST_NAME, firstName, getTitle(wfmStrings.firstName(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.LAST_NAME) != null) {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.LAST_NAME, lastName, getTitle(formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.LAST_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.LAST_NAME).getTitle() : wfmStrings.lastName(), formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.LAST_NAME).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.LAST_NAME).isInformation());
            lastName.setEnabled(!formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.LAST_NAME).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.LAST_NAME).isInformation()) {
                new KpiToolTip(lastName, formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.LAST_NAME).getInformationText());
            }
        } else {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.LAST_NAME, lastName, getTitle(wfmStrings.lastName(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.E_MAIL) != null) {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.E_MAIL, emailT, getTitle(formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.E_MAIL).isChanged() ? formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.E_MAIL).getTitle() : wfmStrings.email(), formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.E_MAIL).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.E_MAIL).isInformation());
            if (formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.E_MAIL).isInformation()) {
                new KpiToolTip(emailT, formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.E_MAIL).getInformationText());
            }
        } else {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.E_MAIL, emailT, getTitle(wfmStrings.email(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.PHONE) != null) {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.PHONE, phoneNumberT, getTitle(formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.PHONE).isChanged() ? formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.PHONE).getTitle() : wfmStrings.phone(), formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.PHONE).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.PHONE).isInformation());
            if (formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.PHONE).isInformation()) {
                new KpiToolTip(phoneNumberT, formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.PHONE).getInformationText());
            }
        } else {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.PHONE, phoneNumberT, getTitle(wfmStrings.phone(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.COMPANY_EMPLOYEE_NUMBER) != null) {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.COMPANY_EMPLOYEE_NUMBER, companyEmployeeNumber, getTitle(formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.COMPANY_EMPLOYEE_NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.COMPANY_EMPLOYEE_NUMBER).getTitle() : wfmStrings.companyEmployeeNumber(), formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.COMPANY_EMPLOYEE_NUMBER).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.COMPANY_EMPLOYEE_NUMBER).isInformation());
            companyEmployeeNumber.setEnabled(!formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.COMPANY_EMPLOYEE_NUMBER).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.COMPANY_EMPLOYEE_NUMBER).isInformation()) {
                new KpiToolTip(companyEmployeeNumber, formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.COMPANY_EMPLOYEE_NUMBER).getInformationText());
            }
        } else {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.COMPANY_EMPLOYEE_NUMBER, companyEmployeeNumber, getTitle(wfmStrings.companyEmployeeNumber(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_DEPARTMENT_CODE) != null) {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_DEPARTMENT_CODE, departmentCode, getTitle(formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_DEPARTMENT_CODE).isChanged() ? formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_DEPARTMENT_CODE).getTitle() : wfmStrings.studentDepartmentCode(), formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_DEPARTMENT_CODE).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_DEPARTMENT_CODE).isInformation());
            departmentCode.setEnabled(!formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_DEPARTMENT_CODE).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_DEPARTMENT_CODE).isInformation()) {
                new KpiToolTip(departmentCode, formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_DEPARTMENT_CODE).getInformationText());
            }
        } else {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_DEPARTMENT_CODE, departmentCode, getTitle(wfmStrings.studentDepartmentCode(), false));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.SAFETY_PP) != null) {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.SAFETY_PP, safetyPP, getTitle(formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.SAFETY_PP).isChanged() ? formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.SAFETY_PP).getTitle() : tcStrings.residenceNumber(), formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.SAFETY_PP).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.SAFETY_PP).isInformation());
            safetyPP.setEnabled(!formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.SAFETY_PP).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.SAFETY_PP).isInformation()) {
                new KpiToolTip(safetyPP, formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.SAFETY_PP).getInformationText());
            }
        } else {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_DEPARTMENT_CODE, safetyPP, getTitle(tcStrings.residenceNumber(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.REF_IND_NUMBER) != null) {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.REF_IND_NUMBER, txtRefIndNumber, getTitle(formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.REF_IND_NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.REF_IND_NUMBER).getTitle() : tcStrings.refIndNumber(), formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.REF_IND_NUMBER).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.REF_IND_NUMBER).isInformation());
            txtRefIndNumber.setEnabled(!formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.REF_IND_NUMBER).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.REF_IND_NUMBER).isInformation()) {
                new KpiToolTip(txtRefIndNumber, formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.REF_IND_NUMBER).getInformationText());
            }
        } else {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.REF_IND_NUMBER, txtRefIndNumber, getTitle(tcStrings.refIndNumber(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_STATUS) != null) {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_STATUS, studentStatus, getTitle(formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_STATUS).isChanged() ? formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_STATUS).getTitle() : wfmStrings.status(), formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_STATUS).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_STATUS).isInformation());
            studentStatus.setEnabled(!formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_STATUS).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_STATUS).isInformation()) {
                new KpiToolTip(studentStatus, formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_STATUS).getInformationText());
            }
        } else {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_STATUS, studentStatus, getTitle(wfmStrings.status(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_GENDER) != null) {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_GENDER, genderTable, getTitle(formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_GENDER).isChanged() ? formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_GENDER).getTitle() : wfmStrings.gender(), formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_GENDER).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_GENDER).isInformation());
            if (formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_GENDER).isInformation()) {
                new KpiToolTip(genderTable, formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_GENDER).getInformationText());
            }
        } else {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_GENDER, genderTable, getTitle(wfmStrings.gender(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.NATIONALITY) != null) {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.NATIONALITY, txtNationality, getTitle(formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.NATIONALITY).isChanged() ? formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.NATIONALITY).getTitle() : wfmStrings.nationality(), formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.NATIONALITY).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.NATIONALITY).isInformation());
            txtNationality.setEnabled(!formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.NATIONALITY).isDisabled());
            if (formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.NATIONALITY).isInformation()) {
                new KpiToolTip(txtNationality, formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.NATIONALITY).getInformationText());
            }
        } else {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.NATIONALITY, txtNationality, getTitle(wfmStrings.nationality(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.DATE_OF_BIRTH) != null) {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.DATE_OF_BIRTH, dateOfBirth, getTitle(formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.DATE_OF_BIRTH).isChanged() ? formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.DATE_OF_BIRTH).getTitle() : wfmStrings.dateOfBirth(), formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.DATE_OF_BIRTH).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.DATE_OF_BIRTH).isInformation());
            if (formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.NATIONALITY).isInformation()) {
                new KpiToolTip(dateOfBirth, formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.DATE_OF_BIRTH).getInformationText());
            }
        } else {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.DATE_OF_BIRTH, dateOfBirth, getTitle(wfmStrings.dateOfBirth(), false));
        }
//        addField(CustomFormConstants.TRAINING_CENTER.STUDENT.CUSTOMER, new AdvancedInputGroup(null, customer, addCustomerLink, true, true), getTitle(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()), true));
//        addField(CustomFormConstants.TRAINING_CENTER.STUDENT.FIRST_NAME, firstName, getTitle(wfmStrings.firstName(), true));
//        addField(CustomFormConstants.TRAINING_CENTER.STUDENT.LAST_NAME, lastName, getTitle(wfmStrings.lastName(), true));
//        addField(CustomFormConstants.TRAINING_CENTER.STUDENT.E_MAIL, emailT, getTitle(wfmStrings.email(), true));
//        addField(CustomFormConstants.TRAINING_CENTER.STUDENT.PHONE, phoneNumberT, getTitle(wfmStrings.phone(), false));
//        addField(CustomFormConstants.TRAINING_CENTER.STUDENT.COMPANY_EMPLOYEE_NUMBER, companyEmployeeNumber, getTitle(wfmStrings.companyEmployeeNumber(), true));
//        addField(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_DEPARTMENT_CODE, departmentCode, getTitle(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.number(), wfmStrings.department()), false));
//        addField(CustomFormConstants.TRAINING_CENTER.STUDENT.SAFETY_PP, safetyPP, getTitle(tcStrings.residenceNumber(), true));
//        addField(CustomFormConstants.TRAINING_CENTER.STUDENT.REF_IND_NUMBER, txtRefIndNumber, getTitle(tcStrings.refIndNumber(), false));
//        addField(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_STATUS, studentStatus, getTitle(wfmStrings.student() + " " + wfmStrings.status()));
//        addField(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_GENDER, genderTable, getTitle(wfmStrings.gender()));
//        addField(CustomFormConstants.TRAINING_CENTER.STUDENT.NATIONALITY, txtNationality, getTitle(wfmStrings.nationality(), false));
//        addField(CustomFormConstants.TRAINING_CENTER.STUDENT.DATE_OF_BIRTH, dateOfBirth, getTitle(wfmStrings.dateOfBirth(), /*true*/false));
        if (objectID != null) {
        addField(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_UPLOAD_IMAGE, profilePicture, null, true);
        }
        getCustomFieldUtil().drawCustomFields(this, objectID, false);


        //parent 2
        addTitleField(CustomFormConstants.TRAINING_CENTER.STUDENT.ADDRESS_INFORMATION, wfmStrings.addressInformation());
        //children 2.1
        addField(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_ADDRESS, addressWidget, null);
        show();
    }

    @Override
    protected void initPredefinedValues() {

    }



    @Override
    protected void addButtons() {
        addButton(wfmStrings.save(), event -> save(false));
        if (objectID == null) {
            addButton(wfmStrings.saveAndNew(), event -> save(true));
        }
    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        TCService.App.get().getStudentItem(objectID, new AbstractAsyncCallback<StudentItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);

            }

            @Override
            public void success(final StudentItem result) {
                Scheduler.get().scheduleDeferred(() -> {
                    LoadingPanel.loading(false);
                    item = result;
                    fillFormWithData();
                });
            }
        });

    }

    protected void fillFormWithData() {
        if (item.getCustomerID() != null && item.getCustomerName() != null) {
            customer.setSelected(item.getCustomerID(), item.getCustomerName());
        }

        if (item.getCardNumber() != null) {
            cardN.setText(item.getCardNumber());
        }
        cardType.setItems(item.getCardTypes());
        if (item.getCardTypeID() != null) {
            cardType.setSelected(item.getCardTypeID());
        }
        if (item.getBirthDate() != null) {
            dateOfBirth.setSelected(item.getBirthDate().getNonConvertedDate().getDate(), item.getBirthDate().getNonConvertedDate().getMonth(), item.getBirthDate().getNonConvertedDate().getYear());
        } else {
            dateOfBirth.setSelected(0);
        }
        setEmailDetailsWidgets();
        setPhoneNumbersWidgets();
        if (item.getFirstName() != null) {
            firstName.setText(item.getFirstName());
        }
        if (item.getLastName() != null) {
            lastName.setText(item.getLastName());
        }
        if (item.getSafetyPPNumber() != null) {
            safetyPP.setText(item.getSafetyPPNumber());
        }
        if (item.getCompEmpNum() != null) {
            companyEmployeeNumber.setText(item.getCompEmpNum());
        }
        if (item.getDepartmentCode() != null) {
            departmentCode.setText(item.getDepartmentCode());
        }
        if (item.getAddresses() != null && item.getAddresses().size() > 0) {
            address = item.getAddresses().get(0);
        }
        addressWidget.setCountriesStates(item.getCountries(), item.getStates());
        if (address != null) {
            addressWidget.setAddress(address);
        }
        if (item.isActive()) {
            studentStatus.setSelectedNullLabel();
        }
        if (item.getGender() != null) {
            if (item.getGender().equals(wfmStrings.male())) {
                male.setValue(true);
            } else {
                female.setValue(true);
            }
        }
        getCustomFieldUtil().fillCustomFieldsWithData(item.getCustomFields());

        txtRefIndNumber.setText(item.getRefIndNumber());
        txtNationality.setText(item.getNationality());
        commonService.getStudentImageURL(objectID, new AbstractAsyncCallback<String>() {
            public void success(String result) {
                profilePicture.initialize(result, item.getFirstName(), item.getLastName(), true);
                GWT.log("imageURL ; " + result);
            }
        });

    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (customFieldUtil == null) {
            customFieldUtil = new FormHasCustomField();
        }
        return customFieldUtil;
    }

    private static SelectItem[] getAddress() {
        SelectItem[] items = new SelectItem[3];
        items[0] = new SelectItem(AddressReference.HOME.getId(), wfmStrings.contacthome());
        items[1] = new SelectItem(AddressReference.WORK.getId(), wfmStrings.contactwork());
        items[2] = new SelectItem(AddressReference.OTHER.getId(), wfmStrings.other());
        return items;
    }

    private WidgetsMap getEmailWidgets(String name, Integer id, String primaryEmail) {
        WidgetsMap widgetsMap = new WidgetsMap();
        final TextBox email = new TextBox();
        email.setPlaceHolder(wfmStrings.email());
        email.addStyleName("email");
        email.getElement().setId("email");
        if (name != null) {
            email.setText(name);
        }
        email.addClickHandler(event -> {
            if (wfmStrings.email().equals(email.getText())) {
                email.setText("");
                email.setStyleName("");
            }
        });
        email.addFocusHandler(focusEvent -> {
            if (wfmStrings.email().equals(email.getText())) {
                email.setText("");
                email.setStyleName("");
            }
        });
        email.addBlurHandler(blurEvent -> {
            if (email.getText() != null && !"".equals(email.getText()) && !wfmStrings.email().equals(email.getText())) {
                email.setText(email.getText().toLowerCase().trim());
            }
        });
        email.addStyleName(DEFAULT_WIDTH);
        DataListBox emailLocation = new DataListBox();
        emailLocation.setWidth("100px");
        emailLocation.setWithoutNullLabel(true);
        emailLocation.setItems(getAddress());
        if (id != null) {
            emailLocation.setSelected(id);
        } else {
            emailLocation.setSelected(AddressReference.WORK.getId());
        }
        RadioButton primary = new KpiRadioButton("primaryEmail");
        new KpiToolTip(primary, wfmStrings.primary());
        primary.addStyleName("multiwidget-sub");
        primary.getElement().setId("email_primary");
        if (primaryEmail != null && name != null && !"".equals(name.trim()) && !"".equals(primaryEmail.trim()) && primaryEmail.trim().equalsIgnoreCase(name.trim())) {
            primary.setValue(true);
        } else if (name == null && id == null && emailT == null) {
            primary.setValue(true);
        }
        widgetsMap.addToLeft(PRIMARY_RADIO_BUTTON, primary);
        widgetsMap.add(RELATION_LIST_BOX, emailLocation);
        widgetsMap.add(PARAM_TEXT_BOX, email);
        return widgetsMap;
    }

    public static SelectItem[] getPhoneNumber() {
        SelectItem[] items = new SelectItem[5];
        items[1] = new SelectItem(PhoneReference.WORK.getId(), wfmStrings.contactwork());
        items[0] = new SelectItem(PhoneReference.HOME.getId(), wfmStrings.contacthome());
        items[2] = new SelectItem(PhoneReference.MOBILE.getId(), wfmStrings.mobile());
//        items[3] = new SelectItem(PhoneReference.HOMEFAX.getId(), wfmStrings.contactfax());
//        items[4] = new SelectItem(PhoneReference.WORKFAX.getId(), wfmStrings.contactworkfax());
//        items[3] = new SelectItem(PhoneReference.PAGER.getId(), wfmStrings.contactpager());
        items[3] = new SelectItem(PhoneReference.OTHER.getId(), wfmStrings.other());
        items[4] = new SelectItem(PhoneReference.EXTENSION.getId(), wfmStrings.extension());
        return items;
    }

    private WidgetsMap getPhoneWidgets(String name, Integer id, String primaryPhone) {
        WidgetsMap widgetsMap = new WidgetsMap();
        final PhoneNumber phone = new PhoneNumber("");

        if (name != null) {
            phone.setData(name);
        }
        final DataListBox phoneLocation = new DataListBox();
        phoneLocation.setWidth("100px");
        phoneLocation.setWithoutNullLabel(true);
        phoneLocation.setItems(getPhoneNumber());
        if (id != null) {
            phoneLocation.setSelected(id);
        } else {
            phoneLocation.setSelected(PhoneReference.WORK.getId());
        }

        phoneLocation.addValueChangeHandler(event -> {
            phone.onlyExternal(phoneLocation.getSelectedId() != null && phoneLocation.getSelectedId().equals(PhoneReference.EXTENSION.getId()));
        });
        RadioButton primary = new KpiRadioButton("primaryPhone");
        new KpiToolTip(primary, wfmStrings.primary());
        primary.addStyleName("multiwidget-sub");
        primary.getElement().setId("email_primary");
        primary.addStyleName("email_primary");
        if (primaryPhone != null && name != null && !"".equals(name.trim()) && !"".equals(primaryPhone.trim()) && primaryPhone.trim().equalsIgnoreCase(name.trim())) {
            primary.setValue(true);
        } else if (name == null && id == null && phoneNumberT == null) {
            primary.setValue(true);
        }
        widgetsMap.addToLeft(PRIMARY_RADIO_BUTTON, primary);
        widgetsMap.add(RELATION_LIST_BOX, phoneLocation);
        widgetsMap.addWidgets(phone.getPhoneFeild());
        widgetsMap.addWidgetToMap(MultiTableNewUI.PHONE_NUMBER, phone);

        if (id != null && id.equals(PhoneReference.EXTENSION.getId())) {
            phone.onlyExternal(true);
        }
        return widgetsMap;
    }

    private void save(final boolean saveAndNew) {
        enableButton(false);
        if (validate()) {
            enableButton(true);
            return;
        }
        setStudentValues();
        //register save student logic
        LoadingPanel.loading(true);
        TCService.App.get().isExistStudentWithResidenceNumber(objectID, item.getSafetyPPNumber(), item.getCustomerID(), new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                caught.printStackTrace();
            }

            @Override
            public void onSuccess(Boolean isExistStudent) {
                if (!isExistStudent) {
                    TCService.App.get().saveStudent(item, new AbstractAsyncCallback<Integer>() {
                        @Override
                        public void failure(Throwable throwable) {
                            LoadingPanel.loading(false);
                            enableButton(true);
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        }

                        @Override
                        public void success(Integer result) {
                            LoadingPanel.loading(false);
                            enableButton(true);
                            if (result != null) {
                                if (Errors.STUDENT_WITH_THIS_EMAIL_ALREADY_EXISTS == result) {
                                    Info.show("Sorry, student with this email already exists!", Info.Type.WARNING);
                                } else {
                                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullySaved(), wfmStrings.changes()), Info.Type.INFO);
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_STUDENT_ADD_EDIT, result, AddStudentView.this);
                                    onShellOk(saveAndNew);
                                }
                            }
                        }
                    });
                } else {
                    LoadingPanel.loading(false);
                    enableButton(true);
                    Info.show("Sorry, student with this Residence# already exists. Try another Residence# !", Info.Type.WARNING);
                }
            }
        });
    }

    protected void onShellOk(boolean saveAndNew) {
        if (saveAndNew) {
            objectID = null;
            initForm();
        } else {
            closeTab();
            if (objectID != null) {
                SinksContainerFactory.entryPoint.onHistoryChanged(TC_STUDENTS + "|summary/" + objectID);
            }
        }
    }

    private void setEmailDetailsWidgets() {
        if (!item.isItemParamsEmpty(Constants.CONTACT_EMAILS)) {
            emailT.clear();
        }
        setMultiTableItems(Constants.CONTACT_EMAILS);
    }

    private void setPhoneNumbersWidgets() {
        if (!item.isItemParamsEmpty(Constants.CONTACT_PHONES)) {
            phoneNumberT.clear();
        }
        setMultiTableItems(Constants.CONTACT_PHONES);
    }

    protected void setMultiTableItems(int param) {
        Map<Integer, ArrayList<String>> itemParamsAsMap = ContactListItem.getItemParamsAsMap(item, param);
        if (itemParamsAsMap != null && itemParamsAsMap.size() > 0) {
            for (Map.Entry<Integer, ArrayList<String>> entry : itemParamsAsMap.entrySet()) {
                int relation = entry.getKey();
                for (String value : entry.getValue()) {
                    if (value != null && !"".equals(value.trim())) {
                        switch (param) {
                            case Constants.CONTACT_EMAILS:
                                emailT.addWidgets(getEmailWidgets(value, relation, item.getPrimaryEmail()));
                                break;
                            case Constants.CONTACT_PHONES:
                                phoneNumberT.addWidgets(getPhoneWidgets(value, relation, item.getPrimaryPhone()));
                                break;
                        }
                    }
                }
            }
        }
    }

    private void setStudentEmail() {
        item.setEmails();
        for (Map<String, Widget> emailRow : emailT.getWidgets()) {
            TextBox value = (TextBox) emailRow.get(PARAM_TEXT_BOX);
            if (!isEmpty(value.getText()) && !wfmStrings.email().equals(value.getText()) && Utils.validateEmail(value.getText(), false)) {
                DataListBox emailType = (DataListBox) emailRow.get(RELATION_LIST_BOX);
                RadioButton primary = (RadioButton) emailRow.get(PRIMARY_RADIO_BUTTON);
                item.addParam(Constants.CONTACT_EMAILS, emailType.getSelectedId(), value.getText());
                if (primary.getValue() != null && primary.getValue()) {
                    item.setPrimaryEmail(value.getText());
                }
                if (Constants.G_WORK == emailType.getSelectedId(true) && item.getCrmAccount().isNew()) {
                    item.getCrmAccount().setEmail(value.getText());
                }
            }
        }
    }

    private void setStudentPhoneNumber() {
        item.setPhones();
        for (Map<String, Widget> widgetsMap : phoneNumberT.getWidgets()) {
            PhoneNumber phoneNumber = (PhoneNumber) widgetsMap.get(MultiTableNewUI.PHONE_NUMBER);
            DataListBox phoneType = (DataListBox) widgetsMap.get(RELATION_LIST_BOX);
            RadioButton primary = (RadioButton) widgetsMap.get(PRIMARY_RADIO_BUTTON);
            boolean isPrimary = primary.getValue();
            if ("".equals(phoneNumber.toString()) || item.getAllPhones().contains(phoneNumber.toString())) {
                continue;
            }
            item.addParam(Constants.CONTACT_PHONES, phoneType.getSelectedId(), phoneNumber.toString());
            if (isPrimary) {
                item.setPrimaryPhone(phoneNumber.toString());
            }
            if (item.getCrmAccount().isNew()) {
                if (Constants.G_WORK == phoneType.getSelectedId(true)) {
                    item.getCrmAccount().setPhone(phoneNumber.toString());
                }
                if (Constants.G_WORK_FAX == phoneType.getSelectedId(true)) {
                    item.getCrmAccount().setFax(phoneNumber.toString());
                }
            }
        }
    }

    private void setStudentValues() {

        item.setObjectId(objectID);
        if (customer.getSelectedItemID() != null) {
            item.setCustomerID(customer.getSelectedItemID());
        }
        if (firstName.getText() != null && !"".equals(firstName.getText())) {
            item.setFirstName(firstName.getText());
        }
        if (lastName.getText() != null && !"".equals(lastName.getText())) {
            item.setLastName(lastName.getText());
        }

        setStudentEmail();
        setStudentPhoneNumber();
        if (dateOfBirth.getConvertableDOBDate() != null) {
            item.setBirthDate(dateOfBirth.getConvertableDOBDate());
        }
        if (safetyPP.getText() != null && !"".equals(safetyPP.getText())) {
            item.setSafetyPPNumber(safetyPP.getText());
        }
        if (companyEmployeeNumber.getText() != null && !"".equals(companyEmployeeNumber.getText())) {
            item.setCompEmpNum(companyEmployeeNumber.getText());
        }
        if (departmentCode.getText() != null && !"".equals(departmentCode.getText())) {
            item.setDepartmentCode(departmentCode.getText());
        }
        if (cardType.getSelectedItem() != null) {
            item.setCardTypeID(cardType.getSelectedItem().getId());
        }
        if (cardN.getText() != null && !"".equals(cardN.getText())) {
            item.setCardNumber(cardN.getText());
        }

        item.setAddresses(new ArrayList<>());
        if (addressWidget.isNotEmpty()) {
            item.getAddresses().add(addressWidget.getAddress());
        }
        if (!studentStatus.isSomethingSelected()) {
            item.setActive(true);
        }

        if (male.getValue()) {
            item.setGender(wfmStrings.male());
        }
        //gender: female
        if (female.getValue()) {
            item.setGender(wfmStrings.female());
        }

        item.setRefIndNumber(txtRefIndNumber.getText());
        item.setNationality(txtNationality.getText());
        item.setCustomFields(getCustomFieldUtil().getCustomFieldsValue());

    }

    private boolean validate() {
        int errors = 0;
        clearErrorStyle();

        errors += markAsError(CustomFormConstants.TRAINING_CENTER.STUDENT.CUSTOMER, customer, !Validation.validateLookUpRequired(customer));
        errors += markAsError(CustomFormConstants.TRAINING_CENTER.STUDENT.FIRST_NAME, firstName, !Validation.validateTextBoxRequired(firstName));
        errors += markAsError(CustomFormConstants.TRAINING_CENTER.STUDENT.LAST_NAME, lastName, !Validation.validateTextBoxRequired(lastName));
        if (!validateEmails()) {
            errors++;
        }
        errors += getCustomFieldUtil().validateCustomFields();
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.SAFETY_PP) != null && formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.SAFETY_PP).isRequired()) {
            errors += markAsError(CustomFormConstants.TRAINING_CENTER.STUDENT.SAFETY_PP, safetyPP, !Validation.validateTextBoxRequired(safetyPP));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.COMPANY_EMPLOYEE_NUMBER) != null && formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.COMPANY_EMPLOYEE_NUMBER).isRequired()) {
            errors += markAsError(CustomFormConstants.TRAINING_CENTER.STUDENT.COMPANY_EMPLOYEE_NUMBER, companyEmployeeNumber, !Validation.validateTextBoxRequired(companyEmployeeNumber));
        }



//        errors += markAsError(CustomFormConstants.TRAINING_CENTER.STUDENT.SAFETY_PP, safetyPP, !Validation.validateTextBoxRequired(safetyPP));
//        errors += markAsError(CustomFormConstants.TRAINING_CENTER.STUDENT.COMPANY_EMPLOYEE_NUMBER, companyEmployeeNumber, !Validation.validateTextBoxRequired(companyEmployeeNumber));
//        errors += markAsError(CustomFormConstants.TRAINING_CENTER.STUDENT.DATE_OF_BIRTH, dateOfBirth, dateOfBirth.box_validate());

        boolean ident = false;
        TextBox email;
        for (Map<String, Widget> emailRow : emailT.getWidgets()) {
            email = (TextBox) emailRow.get(PARAM_TEXT_BOX);
            if (Validation.validateTextBoxRequired(email) && Validation.validEmailFormat(email.getText(), false)) {
                ident = true;
                break;
            } else {
                errors += markAsError(email, true);
            }
        }
        if (!ident) {
            errors++;
        }

        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return true;
        }
        return false;
    }

    private boolean validateEmails() {
        boolean validEmails = true;
        for (Map<String, Widget> emailRow : emailT.getWidgets()) {
            final TextBox emailBox = (TextBox) emailRow.get(PARAM_TEXT_BOX);
            if (!isEmpty(emailBox.getText()) && !wfmStrings.email().equals(emailBox.getText()) && !Utils.validateEmail(emailBox.getText(), false)) {
                markAsError(emailBox, true);
                validEmails = false;
                Utils.scrollIntoView(emailBox.getElement());
            }
        }
        return validEmails;
    }



    @Override
    protected String getFormID() {
        return LayoutRPC.STUDENT_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    public String getIconStyle() {
        return null;
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
}
