package com.edatasite.workforce.gwt.crm.client.ui.view.quickadd;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCfAndPropertyItems;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.form.CustomFormConstants;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Label;

import java.util.LinkedHashMap;
import java.util.List;

public class CandidateQuickValidate extends KpiSideNavBox {
    interface CandidateQuickValidateUiBinder extends UiBinder<Widget, CandidateQuickValidate> {}
    private static CandidateQuickValidateUiBinder uiBinder = GWT.create(CandidateQuickValidateUiBinder.class);

    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();


    @UiField
    HTMLPanel container;
    @UiField
    Label codeLabel;
    @UiField
    TextBox codeField;
    @UiField
    Label passportNumberLabel;
    @UiField
    TextBox passportNumberField;
    @UiField
    Label firstNameLabel;
    @UiField
    Label lastNameLabel;
    @UiField
    TextBox firstNameField;
    @UiField
    TextBox lastNameField;
    @UiField
    HTMLPanel employeesBox;

    private ContactListItem item = new ContactListItem();
    protected static Property property;
    private boolean isEmployeeCodeExist = false;
    private boolean isPassportExist = false;
    private boolean isNameExist = false;
    private boolean isFromPlacement =  false;
    protected ExtendedCommand command;
    public LinkedHashMap<String, FormProperty> formPropertyMap;


    public CandidateQuickValidate() {
        super(WIDE_FORM_WIDTH);
        property = new Property();
        uiBinder.createAndBindUi(this);
        initWidgets();
    }

    public CandidateQuickValidate(boolean isFromPlacement) {
        super(WIDE_FORM_WIDTH);
        property = new Property();
        this.isFromPlacement = isFromPlacement;
        onInitialize();
        uiBinder.createAndBindUi(this);
    }

    private void onInitialize() {
        CommonService.App.get().getCompanyCustomFieldsAndFormProperties( ViewName.Candidate,  LayoutRPC.CANDIDATE_FORM, new AbstractAsyncCallback<CompanyCfAndPropertyItems>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(true);
            }

            @Override
            public void success(CompanyCfAndPropertyItems result) {
                if (result != null) {
                    formPropertyMap = result.getFormPropertyMap();
                }
                initWidgets();
            }
        });
    }


    private void initWidgets() {
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NUMBER) != null) {
            codeLabel.setText(formPropertyMap.get(CustomFormConstants.NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.NUMBER).getTitle() : wfmStrings.number());
        } else {
            codeLabel.setText(wfmStrings.number());
        }
        codeField.addChangeHandler(event -> {
            ProfileItem profileItem = new ProfileItem();
            profileItem.setEmpCode(codeField.getValue());
            profileItem.setFirstName(firstNameField.getValue());
            profileItem.setLastName(lastNameField.getValue());
            profileItem.setPassportNumber(passportNumberField.getValue());
            EmployeeService.App.get().getEmployeesByData(profileItem, new AsyncCallback<List<ProfileItem>>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(List<ProfileItem> profileItems) {
                    employeesBox.clear();
                    if (profileItems != null && !profileItems.isEmpty()) {
                        isEmployeeCodeExist = true;
                        for (ProfileItem profileItem : profileItems) {
                            MaterialLink employeeLink = new MaterialLink();
                            employeeLink.setText(wfmStrings.employee()+": "+ profileItem.getEmpCode() + " -> " + profileItem.getName() );
                            employeeLink.setHref("Hrms.html#employeeProfile%7CemployeeProfileView/" + profileItem.getObjectId());
                            employeeLink.setFontSize(16, Style.Unit.PX);
                            employeeLink.getSpan().setStyle("color: red !important;");
                            employeesBox.add(new HTMLPanel("<br/>"));
                            employeesBox.add(employeeLink);
                        }
                        Info.warn(wfmStrings.empWithThisNumberExists());
                    }
                }
            });
        });

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.PASSPORT_NUMBER) != null) {
            passportNumberLabel.setText(formPropertyMap.get(CustomFormConstants.PASSPORT_NUMBER).isChanged() ? formPropertyMap.get(CustomFormConstants.PASSPORT_NUMBER).getTitle() : wfmStrings.passportNumber());
        } else {
            passportNumberLabel.setText(wfmStrings.passportNumber());
        }
        passportNumberField.addChangeHandler(event -> {
            ProfileItem profileItem = new ProfileItem();
            profileItem.setEmpCode(codeField.getValue());
            profileItem.setFirstName(firstNameField.getValue());
            profileItem.setLastName(lastNameField.getValue());
            profileItem.setPassportNumber(passportNumberField.getValue());
            EmployeeService.App.get().getEmployeesByData(profileItem, new AsyncCallback<List<ProfileItem>>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(List<ProfileItem> profileItems) {
                    employeesBox.clear();
                    if (profileItems != null && !profileItems.isEmpty()) {
                        isEmployeeCodeExist = true;
                        for (ProfileItem profileItem : profileItems) {
                            MaterialLink employeeLink = new MaterialLink();
                            employeeLink.setText(wfmStrings.employee()+": "+ profileItem.getEmpCode() + " -> " + profileItem.getName() );
                            employeeLink.setHref("Hrms.html#employeeProfile%7CemployeeProfileView/" + profileItem.getObjectId());
                            employeeLink.setFontSize(16, Style.Unit.PX);
                            employeeLink.getSpan().setStyle("color: red !important;");
                            employeesBox.add(new HTMLPanel("<br/>"));
                            employeesBox.add(employeeLink);
                        }
                        Info.warn(wfmStrings.empWithThisNumberExists());
                    }
                }
            });
        });

        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.FIRST_NAME) != null) {
            firstNameLabel.setText(formPropertyMap.get(CustomFormConstants.FIRST_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.FIRST_NAME).getTitle() : wfmStrings.firstName());
        } else {
            firstNameLabel.setText(wfmStrings.firstName());
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.LAST_NAME) != null) {
            lastNameLabel.setText(formPropertyMap.get(CustomFormConstants.LAST_NAME).isChanged() ? formPropertyMap.get(CustomFormConstants.LAST_NAME).getTitle() : wfmStrings.lastName());
        } else {
            lastNameLabel.setText(wfmStrings.lastName());
        }

        firstNameField.addChangeHandler(event -> {
            ProfileItem profileItem = new ProfileItem();
            profileItem.setEmpCode(codeField.getValue());
            profileItem.setFirstName(firstNameField.getValue());
            profileItem.setLastName(lastNameField.getValue());
            profileItem.setPassportNumber(passportNumberField.getValue());
            EmployeeService.App.get().getEmployeesByData(profileItem, new AsyncCallback<List<ProfileItem>>() {
                @Override
                public void onFailure(Throwable throwable) {

                }

                @Override
                public void onSuccess(List<ProfileItem> profileItems) {
                    employeesBox.clear();
                    if (profileItems != null && !profileItems.isEmpty()) {
                        isEmployeeCodeExist = true;
                        for (ProfileItem profileItem : profileItems) {
                            MaterialLink employeeLink = new MaterialLink();
                            employeeLink.setText(wfmStrings.employee()+": "+ profileItem.getEmpCode() + " -> " + profileItem.getName() );
                            employeeLink.setHref("Hrms.html#employeeProfile%7CemployeeProfileView/" + profileItem.getObjectId());
                            employeeLink.setFontSize(16, Style.Unit.PX);
                            employeeLink.getSpan().setStyle("color: red !important;");
                            employeesBox.add(new HTMLPanel("<br/>"));
                            employeesBox.add(employeeLink);
                        }
                        Info.warn(wfmStrings.empWithThisNumberExists());
                    }
                }
            });
        });

        lastNameField.addValueChangeHandler(event -> {
            ProfileItem profileItem = new ProfileItem();
            profileItem.setEmpCode(codeField.getValue());
            profileItem.setFirstName(firstNameField.getValue());
            profileItem.setLastName(lastNameField.getValue());
            profileItem.setPassportNumber(passportNumberField.getValue());
            EmployeeService.App.get().getEmployeesByData(profileItem, new AsyncCallback<List<ProfileItem>>() {
                    @Override
                    public void onFailure(Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(List<ProfileItem> profileItems) {
                        employeesBox.clear();
                        if (profileItems != null && !profileItems.isEmpty()) {
                            isEmployeeCodeExist = true;
                            for (ProfileItem profileItem : profileItems) {
                                MaterialLink employeeLink = new MaterialLink();
                                employeeLink.setText(wfmStrings.employee()+": "+ profileItem.getEmpCode() + " -> " + profileItem.getName() );
                                employeeLink.setHref("Hrms.html#employeeProfile%7CemployeeProfileView/" + profileItem.getObjectId());
                                employeeLink.setFontSize(16, Style.Unit.PX);
                                employeeLink.getSpan().setStyle("color: red !important;");
                                employeesBox.add(new HTMLPanel("<br/>"));
                                employeesBox.add(employeeLink);
                            }
                            Info.warn(wfmStrings.empWithThisNumberExists());
                        }
                    }
                });
        });


        Heading header = new Heading(HeadingSize.H1);
        header.setText(hrmsStrings.addCandidate());

        WfmButton2 saveBtn = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        WfmButton2 closeBtn = new WfmButton2(wfmStrings.close(), WfmButton2.BTN_PRIMARY);


        saveBtn.addClickHandler(event -> {
            if (isEmployeeCodeExist || isPassportExist || isNameExist) {
                final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                messageBox.setTitle(wfmStrings.warning());
                String message = "";
                if (isEmployeeCodeExist) {
                    message += wfmStrings.employeeCodeExistWarning()+"<br/>";
                }
                if (isPassportExist) {
                    message += wfmStrings.employeePassportNumberExistWarning() +"<br/>";
                }
                if (isNameExist) {
                    message += wfmStrings.employeeNameAlreadyExistWarning()+"<br/>";
                }
                message += wfmStrings.youWantToContinue();
                messageBox.setMessage(message);
                messageBox.open();
                messageBox.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        if (validate()) {
                            save();
                        } else {
                            saveBtn.setEnabled(true);
                        }
                    }
                });
            } else {
                if (validate()) {
                    save();
                } else {
                    saveBtn.setEnabled(true);
                }
            }
        });
        closeBtn.addClickHandler(event -> {
            clear();
            close();
        });

        addBody(container);
        addHeader(header);
        addFooter(saveBtn);
        addFooter(closeBtn);
    }

    public void save() {
        LoadingPanel.loading(true, container);
        setValuesToRPC();
        ContactService.App.get().saveCandidate(item, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false, container);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Integer result) {
                LoadingPanel.loading(false, container);
                if (isFromPlacement) {
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CANDIDATE_ADD, result, CandidateQuickValidate.this);
                }
                if (result != null && result > 0) {
                    close();
                    Info.show(property.getPlural(wfmStrings.messSuccessfullyAdded(), wfmStrings.candidate()), Info.Type.INFO);
                } else if (result != null && result.intValue() != Constants.ANTIBOT_ERROR) {
                    String fullName = firstNameField.getText() + " " + firstNameField.getText();
                    String messageParam = (result != null && result == -1) ? fullName : item.getPrimaryEmail();

                    Info.show(wfmMessages.duplicateDetectedMessage(wfmStrings.candidate(), messageParam), Info.Type.WARNING);
                }
            }
        });
    }

    private void setValuesToRPC() {
        NumberData numberData = new NumberData();
        numberData.setNumberString(codeField.getText());
        item.setNumberData(numberData);
        item.setFirstName(firstNameField.getText());
        item.setLastName(lastNameField.getText());
        item.setPassportNumber(passportNumberField.getText());
    }


    public boolean validate() {
        int errors = 0;

        if (firstNameField.getText() == null || "".equals(firstNameField.getText())) {
            firstNameField.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (lastNameField.getText() == null || "".equals(lastNameField.getText())) {
            lastNameField.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (codeField.getText() == null || "".equals(codeField.getText())) {
            codeField.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (formPropertyMap != null && formPropertyMap.get(CustomFormConstants.NUMBER) != null && formPropertyMap.get(CustomFormConstants.NUMBER).getMinChar() != null && !formPropertyMap.get(CustomFormConstants.NUMBER).getMinChar().equals("")) {
           if (codeField.getText().trim().length() != Integer.parseInt(formPropertyMap.get(CustomFormConstants.NUMBER).getMinChar())) {
               Info.warn(wfmMessages.allowedCharLimit(formPropertyMap.get(CustomFormConstants.NUMBER).isChanged() ?
                       formPropertyMap.get(CustomFormConstants.NUMBER).getTitle() : wfmStrings.number(), formPropertyMap.get(CustomFormConstants.NUMBER).getMinChar()));
           }
        }
        if (passportNumberField.getText() == null || "".equals(passportNumberField.getText())) {
            passportNumberField.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (passportNumberField.getText() == null || "".equals(passportNumberField.getText())) {
            passportNumberField.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }

        if (errors > 0) {
            Info.warn(wfmStrings.sureEnteredAllData(), Info.Position.TOP_RIGHT);
            return false;
        }
        return true;
    }

    public void setCommand(ExtendedCommand command) {
        this.command = command;
    }
}
