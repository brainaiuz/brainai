package com.edatasite.workforce.gwt.crm.client.ui.view.quickadd;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.client.ui.DOBWidget;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.reference.PhoneReference;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.Numbering;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.PhoneNumber;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Label;
import gwt.material.design.client.ui.html.Span;

/**
 * User: Abror Abdukadirov
 * Date: 06.01.2018 12:13
 */
public class CandidateQuickAddForm extends Composite {
    interface CandidateQuickAddFormUiBinder extends UiBinder<Widget, CandidateQuickAddForm> {
    }

    private static CandidateQuickAddFormUiBinder ourUiBinder = GWT.create(CandidateQuickAddFormUiBinder.class);

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    
    public static final WfmMessages wfmMessages = WfmMessages.App.get();
    protected static Property property;

    @UiField
    HTMLPanel panel;
    @UiField
    Span collapsibleHeader;
    @UiField
    Label numberLabel;
    @UiField
    HTMLPanel numberDiv;
    @UiField
    Label ownerLabel;
    @UiField
    DataListBox owner;
    @UiField
    Label firstNameLabel;
    @UiField
    TextBox firstName;
    @UiField
    Label lastNameLabel;
    @UiField
    TextBox lastName;
    @UiField
    Label dateOfBithLabel;
    @UiField
    HTMLPanel dateOfBithDiv;
    @UiField
    Label phoneLabel;
    @UiField
    HTMLPanel phoneDiv;
    @UiField
    Label emailLabel;
    @UiField
    TextBox email;
    @UiField
    Label statusLabel;
    @UiField
    DataListBox status;

    private DOBWidget dateOfBirthWidget;
    private Numbering number;
    private PhoneNumber phone;

    private ExtendedCommand command;
    private ContactListItem item;

    private String debugId = "candidate_quick_add_";

    public CandidateQuickAddForm() {
        initWidget(ourUiBinder.createAndBindUi(this));

        initForm();
    }

    private void initForm() {
        collapsibleHeader.setText(wfmStrings.candidateInformation());
        numberLabel.setText(wfmStrings.number());
        ownerLabel.setText(wfmStrings.owner());
        firstNameLabel.setText(wfmStrings.firstName());
        lastNameLabel.setText(wfmStrings.lastName());
        dateOfBithLabel.setText(wfmStrings.dateOfBirth());
        phoneLabel.setText(wfmStrings.phone());
        emailLabel.setText(wfmStrings.email());
        statusLabel.setText(wfmStrings.status());

        number = new Numbering();
        number.ensureDebugId(this.debugId + "numbering");
        numberDiv.add(number);

        owner.ensureDebugId(this.debugId + "owner");

        firstName.ensureDebugId(this.debugId + "firstname");

        lastName.ensureDebugId(this.debugId + "lastname");

        dateOfBirthWidget = new DOBWidget();
        dateOfBirthWidget.ensureDebugId(this.debugId + "dateOfBirth");
        dateOfBithDiv.add(dateOfBirthWidget);

        phone = new PhoneNumber("");
        phone.ensureDebugId(this.debugId + "phone");
        phoneDiv.add(phone.getPhoneFeild());

        email.ensureDebugId(this.debugId + "email");

        status.ensureDebugId(this.debugId + "status");
    }

    public void getCandidateQuickData() {
        LoadingPanel.loading(true, panel);
        CRMService.App.get().getCandidateQuickData(new AbstractAsyncCallback<ContactListItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false, panel);
            }

            @Override
            public void success(ContactListItem result) {
                LoadingPanel.loading(false, panel);
                item = result;
                fillFields();
            }
        });
    }

    private void fillFields() {
        owner.setItems(item.getLeadAssignees());
        if (item.getOwnerId() != null) {
            owner.setSelected(item.getOwnerId());
        }
        number.setNumberData(item.getNumberData());
        status.setItems(item.getCandidateStatuses());
    }

    public boolean validate() {
        int errors = 0;

        if (firstName.getText() == null || "".equals(firstName.getText())) {
            firstName.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (email.getText() != null && !"".equals(email.getText()) && !Utils.validateEmail(email.getText(), false)) {
            email.addStyleName(Constants.ERROR_FORM_STYLE);
            errors++;
        }
        if (errors > 0) {
            Info.warn(wfmStrings.sureEnteredAllData(), Info.Position.TOP_RIGHT);
            return false;
        }
        return true;
    }

    public void save() {
        LoadingPanel.loading(true, panel);
        item.setCheckForDuplicates(true);
        setValuesToRPC();
        ContactService.App.get().saveCandidate(item, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false, panel);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Integer result) {
                LoadingPanel.loading(false, panel);

                if (result != null && result > 0) {
                    Info.show(property.getPlural(wfmStrings.messSuccessfullyAdded(), wfmStrings.candidate()), Info.Type.INFO);

                    if (command != null) {
                        command.execute(result);
                    }
                } else if (result != null && result.intValue() != Constants.ANTIBOT_ERROR) {
                    String fullName = firstName.getText() + " " + lastName.getText();
                    String messageParam = (result != null && result == -1) ? fullName : item.getPrimaryEmail();

                    Info.show(wfmMessages.duplicateDetectedMessage(wfmStrings.candidate(), messageParam), Info.Type.WARNING);
                }
            }
        });
    }

    private void setValuesToRPC() {
        item.setNumberData(number.getNumberData(false));

        if (owner.getSelectedId() != null) {
            item.setOwner(owner.getSelectedItem(true).getName());
            item.setOwnerId(owner.getSelectedId(true));
        }
        item.setFirstName(firstName.getText());

        item.setLastName(lastName.getText());

        if (dateOfBirthWidget.getConvertableDOBDate() != null) {
            item.setBirthDate(dateOfBirthWidget.getConvertableDOBDate());
        }
        item.setPhones();
        item.addParam(Constants.CONTACT_PHONES, PhoneReference.WORK.getId(), phone.toString());
        item.setPrimaryPhone(phone.toString());

        item.setEmails();
        item.addParam(Constants.CONTACT_EMAILS, PhoneReference.WORK.getId(), email.getText());
        item.setPrimaryEmail(email.getText());

        item.setCandidateStatus(status.getSelectedItem());
    }

    public void clearForm() {
        owner.clear();
        firstName.setText("");
        lastName.setText("");
        dateOfBirthWidget.setSelected(0);
        phone.clearPhoneData();
        email.setText("");
        status.clear();

        firstName.removeStyleName(Constants.ERROR_FORM_STYLE);
        email.removeStyleName(Constants.ERROR_FORM_STYLE);
    }


    public void setCommand(ExtendedCommand command) {
        this.command = command;
    }
}