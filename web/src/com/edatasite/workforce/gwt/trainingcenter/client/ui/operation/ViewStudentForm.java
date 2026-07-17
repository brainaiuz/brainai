package com.edatasite.workforce.gwt.trainingcenter.client.ui.operation;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.ui.AddressWidget;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ProfileImage;
import com.edatasite.workforce.gwt.core.client.ui.components.tooltip.KpiToolTip;
import com.edatasite.workforce.gwt.core.client.ui.customfields.FormHasCustomField;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.ActivityQuickAddForm;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * User: Ilhombek
 * Date: 7/18/12
 * Time: 6:32 PM
 */
public class ViewStudentForm extends AddStudentView {

    private final Integer objectID;
    FormHasCustomField customFieldUtil;
    private HTML studentNumber, cardN, cardType, customer, dateOfBirth, firstName, lastName, gender, nationality, compnayEmployeeNumber, departmentCode, safetyPP, refIndeNumber, studentStatus;
    private FlowPanel emailPanel, phonePanel;
    private LinkedHashMap<String, FormProperty> formPropertyMap;

    public ViewStudentForm(Integer objectID) {
        super("summary", wfmStrings.summaryView(), objectID);
        this.objectID = objectID;
    }

    @Override
    protected Widget onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties(ViewName.Students, getFormID(),new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                super.failure(throwable);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                super.success(result);
                getCustomFieldUtil().setCompanyCustomFieldItems(result.getCompanyCustomFieldItems());
                formPropertyMap = result.getFormPropertyMap();
                ViewStudentForm.super.onInitialize();
            }
        });
        return null;
    }

    @Override
    protected void registerFields() {

        profilePicture = new ProfileImage(objectID, LayoutRPC.STUDENT_FORM);

        String student_summary_view_ = "student_summary_view_";
        //customer
        customer = new HTML();
        customer.addStyleName(DEFAULT_WIDTH);
        customer.ensureDebugId(student_summary_view_ + "customer");
        //first name
        firstName = new HTML();
        firstName.addStyleName(DEFAULT_WIDTH);
        firstName.ensureDebugId(student_summary_view_ + "first_name");
        //last name
        lastName = new HTML();
        lastName.addStyleName(DEFAULT_WIDTH);
        lastName.ensureDebugId(student_summary_view_ + "last_name");

        gender = new HTML();
        gender.addStyleName(DEFAULT_WIDTH);
        gender.ensureDebugId(student_summary_view_ + "gender");

        nationality = new HTML();
        nationality.addStyleName(DEFAULT_WIDTH);
        nationality.ensureDebugId(student_summary_view_ + "nationality");

        //student e-mail
        emailPanel = new FlowPanel();
        emailPanel.addStyleName(DEFAULT_WIDTH);
        emailPanel.ensureDebugId(student_summary_view_ + "email");
        //phone
        phonePanel = new FlowPanel();
//        phonePanel.addStyleName(DEFAULT_WIDTH);
        phonePanel.ensureDebugId(student_summary_view_ + "phone");
        //safetyPP number
        safetyPP = new HTML();
        safetyPP.addStyleName(DEFAULT_WIDTH);
        safetyPP.ensureDebugId(student_summary_view_ + "safety_PP_number");
        //Company Employee number
        compnayEmployeeNumber = new HTML();
        compnayEmployeeNumber.addStyleName(DEFAULT_WIDTH);
        compnayEmployeeNumber.ensureDebugId(student_summary_view_ + "company_Employee_number");
        //Department code
        departmentCode = new HTML();
        departmentCode.addStyleName(DEFAULT_WIDTH);
        departmentCode.ensureDebugId(student_summary_view_ + "department_code");

        refIndeNumber = new HTML();
        refIndeNumber.addStyleName(DEFAULT_WIDTH);
        refIndeNumber.ensureDebugId(student_summary_view_ + "refIndNumber");

        //student status
        studentStatus = new HTML();
        studentStatus.addStyleName(DEFAULT_WIDTH);
        studentStatus.ensureDebugId(student_summary_view_ + "student_status");
        //date of birth
        dateOfBirth = new HTML();
        dateOfBirth.addStyleName(DEFAULT_WIDTH);
        dateOfBirth.ensureDebugId(student_summary_view_ + "date_of_birth");
        //card type
        cardType = new HTML();
        cardType.addStyleName(DEFAULT_WIDTH);
        cardType.ensureDebugId(student_summary_view_ + "card_type");
        //card number
        cardN = new HTML();
        cardN.addStyleName(DEFAULT_WIDTH);
        cardN.ensureDebugId(student_summary_view_ + "cardN_number");

        //card number
        studentNumber = new HTML();
        studentNumber.addStyleName(DEFAULT_WIDTH);
        studentNumber.ensureDebugId(student_summary_view_ + "_number");


        //address
        addressWidget = new AddressWidget(address, false, (objectID != null ? objectID.toString() : "add"), true, false);
        addressWidget.ensureDebugId(student_summary_view_ + "address_information");
//        addressWidget.addStyleName(DEFAULT_WIDTH);

        //parent 1
        addTitleField(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_DETAILS, wfmStrings.studentDetails());
        //children 1.1
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.CUSTOMER) != null) {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.CUSTOMER, customer, getTitle(formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.CUSTOMER).isChanged() ? formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.CUSTOMER).getTitle() : wfmStrings.customer(), formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.CUSTOMER).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.CUSTOMER).isInformation());
            if (formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.CUSTOMER).isInformation()) {
                new KpiToolTip(customer, formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.CUSTOMER).getInformationText());
            }
        } else {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.CUSTOMER, customer, getTitle(wfmStrings.customer(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.FIRST_NAME) != null) {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.FIRST_NAME, firstName, getTitle(formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.FIRST_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.FIRST_NAME).getTitle() : wfmStrings.firstName(), formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.FIRST_NAME).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.FIRST_NAME).isInformation());
            if (formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.FIRST_NAME).isInformation()) {
                new KpiToolTip(firstName, formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.FIRST_NAME).getInformationText());
            }
        } else {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.FIRST_NAME, firstName, getTitle(wfmStrings.firstName(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.LAST_NAME) != null) {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.LAST_NAME, lastName, getTitle(formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.LAST_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.LAST_NAME).getTitle() : wfmStrings.lastName(), formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.LAST_NAME).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.LAST_NAME).isInformation());
            if (formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.LAST_NAME).isInformation()) {
                new KpiToolTip(lastName, formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.LAST_NAME).getInformationText());
            }
        } else {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.LAST_NAME, lastName, getTitle(wfmStrings.lastName(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.E_MAIL) != null) {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.E_MAIL, emailPanel, getTitle(formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.E_MAIL).isChanged() ? formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.E_MAIL).getTitle() : wfmStrings.email(), formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.E_MAIL).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.E_MAIL).isInformation());
            if (formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.E_MAIL).isInformation()) {
                new KpiToolTip(emailPanel, formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.E_MAIL).getInformationText());
            }
        } else {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.E_MAIL, emailPanel, getTitle(wfmStrings.email(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.PHONE) != null) {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.PHONE, phonePanel, getTitle(formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.PHONE).isChanged() ? formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.PHONE).getTitle() : wfmStrings.phone(), formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.PHONE).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.PHONE).isInformation());
            if (formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.PHONE).isInformation()) {
                new KpiToolTip(phonePanel, formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.PHONE).getInformationText());
            }
        } else {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.PHONE, phonePanel, getTitle(wfmStrings.phone(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.COMPANY_EMPLOYEE_NUMBER) != null) {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.COMPANY_EMPLOYEE_NUMBER, compnayEmployeeNumber, getTitle(formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.COMPANY_EMPLOYEE_NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.COMPANY_EMPLOYEE_NUMBER).getTitle() : wfmStrings.companyEmployeeNumber(), formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.COMPANY_EMPLOYEE_NUMBER).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.COMPANY_EMPLOYEE_NUMBER).isInformation());
            if (formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.COMPANY_EMPLOYEE_NUMBER).isInformation()) {
                new KpiToolTip(compnayEmployeeNumber, formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.COMPANY_EMPLOYEE_NUMBER).getInformationText());
            }
        } else {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.COMPANY_EMPLOYEE_NUMBER, compnayEmployeeNumber, getTitle(wfmStrings.companyEmployeeNumber(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_DEPARTMENT_CODE) != null) {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_DEPARTMENT_CODE, departmentCode, getTitle(formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_DEPARTMENT_CODE).isChanged() ? formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_DEPARTMENT_CODE).getTitle() : wfmStrings.studentDepartmentCode(), formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_DEPARTMENT_CODE).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_DEPARTMENT_CODE).isInformation());
            if (formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_DEPARTMENT_CODE).isInformation()) {
                new KpiToolTip(departmentCode, formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_DEPARTMENT_CODE).getInformationText());
            }
        } else {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_DEPARTMENT_CODE, departmentCode, getTitle(wfmStrings.studentDepartmentCode(), false));
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.SAFETY_PP) != null) {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.SAFETY_PP, safetyPP, getTitle(formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.SAFETY_PP).isChanged() ? formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.SAFETY_PP).getTitle() : tcStrings.residenceNumber(), formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.SAFETY_PP).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.SAFETY_PP).isInformation());
            if (formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.SAFETY_PP).isInformation()) {
                new KpiToolTip(safetyPP, formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.SAFETY_PP).getInformationText());
            }
        } else {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_DEPARTMENT_CODE, safetyPP, getTitle(tcStrings.residenceNumber(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.REF_IND_NUMBER) != null) {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.REF_IND_NUMBER, refIndeNumber, getTitle(formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.REF_IND_NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.REF_IND_NUMBER).getTitle() : tcStrings.refIndNumber(), formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.REF_IND_NUMBER).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.REF_IND_NUMBER).isInformation());
            if (formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.REF_IND_NUMBER).isInformation()) {
                new KpiToolTip(refIndeNumber, formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.REF_IND_NUMBER).getInformationText());
            }
        } else {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.REF_IND_NUMBER, refIndeNumber, getTitle(tcStrings.refIndNumber(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_STATUS) != null) {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_STATUS, studentStatus, getTitle(formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_STATUS).isChanged() ? formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_STATUS).getTitle() : wfmStrings.status(), formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_STATUS).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_STATUS).isInformation());
            if (formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_STATUS).isInformation()) {
                new KpiToolTip(studentStatus, formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_STATUS).getInformationText());
            }
        } else {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_STATUS, studentStatus, getTitle(wfmStrings.status(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_GENDER) != null) {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_GENDER, gender, getTitle(formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_GENDER).isChanged() ? formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_GENDER).getTitle() : wfmStrings.gender(), formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_GENDER).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_GENDER).isInformation());
            if (formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_GENDER).isInformation()) {
                new KpiToolTip(gender, formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_GENDER).getInformationText());
            }
        } else {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_GENDER, gender, getTitle(wfmStrings.gender(), false));
        }

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.NATIONALITY) != null) {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.NATIONALITY, nationality, getTitle(formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.NATIONALITY).isChanged() ? formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.NATIONALITY).getTitle() : wfmStrings.nationality(), formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.NATIONALITY).isRequired()),false,
                    formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.NATIONALITY).isInformation());
            if (formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.NATIONALITY).isInformation()) {
                new KpiToolTip(nationality, formPropertyMap.get(CustomFormConstants.TRAINING_CENTER.STUDENT.NATIONALITY).getInformationText());
            }
        } else {
            addField(CustomFormConstants.TRAINING_CENTER.STUDENT.NATIONALITY, nationality, getTitle(wfmStrings.nationality(), false));
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
//        addField(CustomFormConstants.TRAINING_CENTER.STUDENT.NUMBER, studentNumber, getTitle(wfmStrings.number()));
//        addField(CustomFormConstants.TRAINING_CENTER.STUDENT.CUSTOMER, customer, getTitle(Property.get(Constants.CLIENT_LIST, wfmStrings.customer())));
//        addField(CustomFormConstants.TRAINING_CENTER.STUDENT.FIRST_NAME, firstName, getTitle(wfmStrings.firstName()));
//        addField(CustomFormConstants.TRAINING_CENTER.STUDENT.LAST_NAME, lastName, getTitle(wfmStrings.lastName()));
//        addField(CustomFormConstants.TRAINING_CENTER.STUDENT.E_MAIL, emailPanel, null);
//        addField(CustomFormConstants.TRAINING_CENTER.STUDENT.PHONE, phonePanel, null);
//        //children 1.2
//        addField(CustomFormConstants.TRAINING_CENTER.STUDENT.COMPANY_EMPLOYEE_NUMBER, compnayEmployeeNumber, getTitle(wfmStrings.companyEmployeeNumber()));
//        addField(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_DEPARTMENT_CODE, departmentCode, getTitle(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.number(), wfmStrings.department())));
//        addField(CustomFormConstants.TRAINING_CENTER.STUDENT.SAFETY_PP, safetyPP, getTitle(tcStrings.residenceNumber()));
//        addField(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_STATUS, studentStatus, getTitle(wfmStrings.student() + " " + wfmStrings.status()));
//        addField(CustomFormConstants.TRAINING_CENTER.STUDENT.DATE_OF_BIRTH, dateOfBirth, getTitle(wfmStrings.dateOfBirth()));
        addField(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_UPLOAD_IMAGE, profilePicture, null);

        //parent 2
        addTitleField(CustomFormConstants.TRAINING_CENTER.STUDENT.ADDRESS_INFORMATION, wfmStrings.addressInformation());
        //children 2.1
        addField(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_ADDRESS, addressWidget, null);
//        addField(CustomFormConstants.TRAINING_CENTER.STUDENT.STUDENT_GENDER, gender, getTitle(wfmStrings.gender()));
//        addField(CustomFormConstants.TRAINING_CENTER.STUDENT.NATIONALITY, nationality, getTitle(wfmStrings.nationality()));
//        addField(CustomFormConstants.TRAINING_CENTER.STUDENT.REF_IND_NUMBER, refIndeNumber, getTitle(tcStrings.refIndNumber()));

        customFieldUtil.drawCustomFields(this, objectID, true);
        show();
    }

    @Override
    public FormHasCustomField getCustomFieldUtil() {
        if (this.customFieldUtil == null) {
            this.customFieldUtil = new FormHasCustomField();
        }
        return this.customFieldUtil;
    }


    @Override
    protected void getDataToFillFields() {
        super.getDataToFillFields();
    }

    @Override
    protected void fillFormWithData() {
        //customer
        customer.setHTML(item.getCustomerName() != null ? item.getCustomerName() : "");
        //firstName
        firstName.setHTML(item.getFirstName() != null ? item.getFirstName() : "");
        //lastName
        lastName.setHTML(item.getLastName() != null ? item.getLastName() : "");
        //email
        setMultiTableItems(Constants.CONTACT_EMAILS);
        //phone
        setMultiTableItems(Constants.CONTACT_PHONES);
        //safety PP Number
        safetyPP.setHTML(item.getSafetyPPNumber() != null ? item.getSafetyPPNumber() : "");
        compnayEmployeeNumber.setHTML(item.getCompEmpNum() != null ? item.getCompEmpNum() : "");
        departmentCode.setHTML(item.getDepartmentCode() != null ? item.getDepartmentCode() : "");
        //student status
        studentStatus.setHTML(item.isActive() ? wfmStrings.active() : wfmStrings.pending());
        //date of birth
        dateOfBirth.setHTML(item.getBirthDate() != null ? DateUtils.format(item.getBirthDate().getNonConvertedDate()) : "");
        //card type
        cardType.setHTML(item.getCardTypeName() != null ? item.getCardTypeName() : "");
        //card number
        cardN.setHTML(item.getCardNumber() != null ? item.getCardNumber() : "");
        //number
        studentNumber.setHTML(item.getNumber() != null ? item.getNumber() : "");
        customFieldUtil.fillCustomFieldsWithData(item.getCustomFields(),true);
    
        gender.setHTML(item.getGender());
        nationality.setHTML(item.getNationality());
        refIndeNumber.setHTML(item.getRefIndNumber());

        if (item.getAddresses() != null && item.getAddresses().size() > 0) {
            address = item.getAddresses().get(0);
        }
        addressWidget.setCountriesStates(item.getCountries(), item.getStates());
        addressWidget.setAddress(address);

        commonService.getStudentImageURL(objectID, new AbstractAsyncCallback<String>() {
            public void success(String result) {
                profilePicture.initialize(result, item.getFirstName(), item.getLastName(), true);
                GWT.log("imageURL ; " + result);
            }
        });


    }


    @Override
    protected void addButtons() {
        WfmButton2 editButton = new WfmButton2(wfmStrings.edit(), WfmButton2.BTN_PRIMARY);
        editButton.addClickHandler(event -> {
            //register edit student logic
            SinksContainerFactory.entryPoint.onHistoryChanged(TC_STUDENTS + "|editStudent/" + objectID);
        });
        addButton(editButton);
    }

    @Override
    protected void setMultiTableItems(int param) {
        Map<Integer, ArrayList<String>> itemParamsAsMap = ContactListItem.getItemParamsAsMap(item, param);
        if (itemParamsAsMap != null && itemParamsAsMap.size() > 0) {
            for (Map.Entry<Integer, ArrayList<String>> entry : itemParamsAsMap.entrySet()) {
                int relation = entry.getKey();
                for (String value : entry.getValue()) {
                    if (value != null && !"".equals(value.trim())) {
                        value = value.replace("|", "-");
                        getKeyValueElement(value, relation, param);
                    }
                }
            }
        }
    }

    private void getKeyValueElement(final String value, int relation, int param) {
        FlowPanel f = new FlowPanel();
        f.setStyleName("row");
        HTML valueF = new HTML(value);
        valueF.setStyleName("field");
        HTML relationS = null;
        switch (param) {
            case Constants.CONTACT_EMAILS:
                switch (relation) {
                    case Constants.G_HOME:
                        relationS = new HTML(getTitle(wfmStrings.homeEmail()));
                        break;
                    case Constants.G_WORK:
                        relationS = new HTML(getTitle(wfmStrings.workEmail()));
                        break;
                    case Constants.G_OTHER:
                        relationS = new HTML(getTitle(wfmStrings.otherEmail()));
                        break;
                }
                valueF.setHTML("<a href=\"javascript:\">" + value + "</a>");
                //valueF.addClickHandler(event -> new ComposeView(value));
                valueF.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("emailcompose|add/add/" + value));
                f.add(relationS);
                f.add(valueF);
                emailPanel.add(f);
                break;
            case Constants.CONTACT_PHONES:
                switch (relation) {
                    case Constants.G_HOME:
                        relationS = new HTML(getTitle(wfmStrings.homePhone()));
                        break;
                    case Constants.G_WORK:
                        relationS = new HTML(getTitle(wfmStrings.workPhone()));
                        break;
                    case Constants.G_OTHER:
                        relationS = new HTML(getTitle(wfmStrings.otherPhone()));
                        break;
                    case Constants.G_MOBILE:
                        relationS = new HTML(getTitle(wfmStrings.mobile()));
                        valueF.setHTML("<a href=\"javascript:\">" + value + "</a>");
                        valueF.addClickHandler(event -> new ActivityQuickAddForm(Appointment.SMS, item, "", RelationItem.newEventRelation(RelationItem.TYPE_STUDENT, item.getObjectId(), item.getName())));
                        break;
                    case Constants.G_HOME_FAX:
                        relationS = new HTML(getTitle(wfmStrings.homeFax()));
                        break;
                    case Constants.G_WORK_FAX:
                        relationS = new HTML(getTitle(wfmStrings.workFax()));
                        break;
                    case Constants.G_PAGER:
                        relationS = new HTML(getTitle(wfmStrings.pager()));
                        break;
                    case Constants.G_EXTENSION:
                        relationS = new HTML(getTitle(wfmStrings.extension()));
                        break;
                }
                f.add(relationS);
                f.add(valueF);
                phonePanel.add(f);
                break;
        }
    }

    @Override
    protected String getFormID() {
        return super.getFormID();
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.VIEW;
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