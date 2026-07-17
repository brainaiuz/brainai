package com.edatasite.workforce.gwt.crm.client.ui.view.quickadd;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.reference.PhoneReference;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.ExtendedCommand;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.formWidgets.PhoneNumber;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.html.Label;

/**
 * User: Abror Abdukadirov
 * Date: 19.12.2017 17:48
 */
public class ContactQuickAddForm extends CrmQuickAddForm {

    interface ContactQuickAddFormUiBinder extends UiBinder<Widget, ContactQuickAddForm> {
    }

    private static final ContactQuickAddFormUiBinder ourUiBinder = GWT.create(ContactQuickAddFormUiBinder.class);

    @UiField
    HTMLPanel panel;
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
    Label phoneLabel;
    @UiField
    HTMLPanel phoneDiv;
    @UiField
    Label emailLabel;
    @UiField
    TextBox email;
    @UiField
    HTMLPanel leadStatusDiv;
    @UiField
    Label leadStatusLabel;
    @UiField
    DataListBox leadStatus;

    private PhoneNumber phone;
    protected ExtendedCommand command;
    public ContactListItem item;
    public Integer defaultLeadStatusId;
    public Integer crmAccountId;

    protected int contactType;
    private final String debugId = "contact_quick_add_";

    public ContactQuickAddForm(int contactType, Integer crmAccountId, RelationItem... relationItems) {
        this.contactType = contactType;
        this.crmAccountId = crmAccountId;
        initWidget(ourUiBinder.createAndBindUi(this));
        setRelationItems(relationItems);
        initialize();
    }

    @Override
    protected void initialize() {
        ownerLabel.setText(wfmStrings.owner());
        firstNameLabel.setText(wfmStrings.firstName());
        lastNameLabel.setText(wfmStrings.lastName());
        phoneLabel.setText(wfmStrings.phone());
        emailLabel.setText(wfmStrings.email());
        if (ContactListItem.LEAD_CONTACT == contactType) {
            leadStatusLabel.setText(Property.get(Constants.LEADS, wfmStrings.status(), wfmStrings.lead()));
        } else {
            leadStatusDiv.setVisible(false);
        }
        owner.ensureDebugId(this.debugId + "owner");
        firstName.ensureDebugId(this.debugId + "firstname");
        lastName.ensureDebugId(this.debugId + "lastname");
        phone = new PhoneNumber("");
        phone.ensureDebugId(this.debugId + "phone");
        phoneDiv.add(phone.getPhoneFeild());
        email.ensureDebugId(this.debugId + "email");
        leadStatus.ensureDebugId(this.debugId + "leadStatus");
    }

    @Override
    public void getQuickData() {
        LoadingPanel.loading(true, panel);
        Integer crmAccountId = null;
        if (getRelations() != null && getRelations().size() > 0) {
            for (RelationItem relationItem : getRelations()) {
                if (RelationItem.TYPE_CRM_ACCOUNT.equalsIgnoreCase(relationItem.getToType())) {
                    crmAccountId = relationItem.getToID();
                }
            }
        }
        crmService.getContactQuickData(crmAccountId, contactType, new AbstractAsyncCallback<ContactListItem>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false, panel);
            }

            @Override
            public void success(ContactListItem result) {
                LoadingPanel.loading(false, panel);
                item = result;
                item.setCheckForDuplicates(true);
                fillFields();
            }
        });
    }

    private void fillFields() {
        owner.setItems(item.getLeadAssignees());
        if (item.getOwnerId() != null) {
            owner.setSelected(item.getOwnerId());
        }
        if (ContactListItem.LEAD_CONTACT == contactType) {
            leadStatus.setItems(item.getLeadStatuses());
            if (item.getLeadStatus(true).getId() != null) {
                leadStatus.setSelected(item.getLeadStatus(true));
            } else if (defaultLeadStatusId != null && defaultLeadStatusId > 0) {
                leadStatus.setSelected(defaultLeadStatusId);
            }
        }
    }

    public void setLeadStatus(Integer statusId) {
        if (ContactListItem.LEAD_CONTACT == contactType) {
            defaultLeadStatusId = statusId;
            leadStatus.setSelected(defaultLeadStatusId);
        }
    }

    public boolean validate() {
        int errors = 0;
        String message = wfmStrings.sureEnteredAllData();
//        email.removeStyleName(Constants.ERROR_FORM_STYLE);
        if (!Validation.validateTextBoxRequired(firstName, ContactListItem.LEAD_CONTACT == contactType)) {
            errors++;
            message = wfmStrings.sureEnteredAllData();
        }

        if (!Validation.validateEmailRequired(email)){
            errors++;
            message = wfmStrings.pleaseEnterValidEmail();
        }

        if (!Validation.validateTextBoxRequired(lastName, ContactListItem.LEAD_CONTACT == contactType)) {
            errors++;
            message = wfmStrings.sureEnteredAllData();
        }

        if (errors > 0) {
            Info.warn(message, Info.Position.TOP_RIGHT);
            return false;
        }
        return true;
    }

    public void save() {
        LoadingPanel.loading(true, panel);
        setValuesToRPC();
        ContactService.App.get().saveContact(item, null, true, new AbstractAsyncCallback<Integer>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false, panel);
                CrmQuickAdd.enableButtons(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Integer result) {
                LoadingPanel.loading(false, panel);
                CrmQuickAdd.enableButtons(true);
                if (result != null && result > 0) {
                    String leadSuccessAdd = Property.get(Constants.LEADS, wfmStrings.messSuccessfullyAdded(), wfmStrings.lead());
                    String contactSuccessAdd = Property.get(Constants.Contacts, wfmStrings.messSuccessfullyAdded(), wfmStrings.contact());
                    Info.show(ContactListItem.LEAD_CONTACT == contactType ? leadSuccessAdd : contactSuccessAdd, Info.Type.INFO);
                    if (command != null) {
                        command.execute(result);
                    }
                    item.setObjectId(result);
                    WfmUiEventsBus.fireWfmUiEvent(item.isLeadContact() ? WfmUiEventType.ON_LEADS_ADD_EDIT : WfmUiEventType.ON_CONTACT_ADD, item, ContactQuickAddForm.this);
                } else if (result != null && !Constants.ANTIBOT_ERROR.equals(result)) {
                    /*String message;
                    String messageParam = result == -1 ? firstName.getText().concat(" ").concat(lastName.getText()) : item.getPrimaryEmail();
                    if (ContactListItem.LEAD_CONTACT == contactType) {
                        message = wfmMessages.duplicateDetectedMessage(wfmStrings.lead(), messageParam);
                    } else {
                        message = wfmMessages.duplicateDetectedMessage(wfmStrings.contact(), messageParam);
                    }
                    if (result == -2) {
                        Validation.validateTextBoxRequired(email, false);
                    }
                    Info.show(message, Info.Type.WARNING);*/
                    showDuplicatePopup(result == -1);
                }
            }
        });
    }

    private void showDuplicatePopup(final boolean nameIsDuplicated) {
        String fullName = firstName.getText().concat(" ").concat(lastName.getText());
        IconEnum iconEnum = nameIsDuplicated ? IconEnum.QUESTION : IconEnum.WARN;
        Action action = nameIsDuplicated ? Action.YesNo : Action.OK;
        String message = wfmMessages.duplicateDetectedMessage(ContactListItem.LEAD_CONTACT == contactType ? Property.get(Constants.LEADS, wfmStrings.lead()) : Property.get(Constants.Contacts, wfmStrings.contact()), nameIsDuplicated ? fullName : item.getPrimaryEmail());
        String buttonText = nameIsDuplicated ? wfmStrings.continueAnyway() : wfmStrings.ok();
        WfmMessageBox messageBox = new WfmMessageBox(iconEnum, action, message, buttonText, wfmStrings.cancel(), new CloseHandler() {
            @Override
            public void onCancel() {
                if (!nameIsDuplicated) {
                    Validation.validateTextBoxRequired(email, false);
                }
                CrmQuickAdd.enableButtons(true);
            }

            @Override
            public void onSubmit() {
                if (!nameIsDuplicated) {
                    Validation.validateTextBoxRequired(email, false);
                    CrmQuickAdd.enableButtons(true);
                } else {
                    item.setCheckForDuplicates(false);
                    save();
                }
            }
        });
        messageBox.open();
    }

    private void setValuesToRPC() {
        item.setContactType(contactType);
        if (owner.getSelectedItem() != null) {
            if (ContactListItem.LEAD_CONTACT == contactType) {
                item.setLeadAssigneeID(owner.getSelectedId());
            } else {
                item.setOwnerId(owner.getSelectedId(true));
                item.setOwner(owner.getSelectedItem(true).getName());
            }
        }
        item.setFirstName(firstName.getText());
        item.setLastName(lastName.getText());

        item.setPhones();
        item.addParam(Constants.CONTACT_PHONES, PhoneReference.WORK.getId(), phone.toString());
        item.setPrimaryPhone(phone.toString());

        item.setEmails();
        item.addParam(Constants.CONTACT_EMAILS, PhoneReference.WORK.getId(), email.getText());
        item.setPrimaryEmail(email.getText());
        if (ContactListItem.LEAD_CONTACT == contactType) {
            item.setLeadStatus(leadStatus.getSelectedItem());
        }
        if (getRelations() != null && getRelations().size() > 0) {
            for (RelationItem relationItem : getRelations()) {
                if (RelationItem.TYPE_CRM_ACCOUNT.equalsIgnoreCase(relationItem.getToType())) {
                    if (item.getCrmAccount() == null) {
                        CrmAccountItem accountItem = new CrmAccountItem();
                        accountItem.setObjectId(relationItem.getToID());
                        accountItem.setName(relationItem.getToName());
                        item.setCrmAccount(accountItem);
                    }
                }
                if (RelationItem.TYPE_CAMPAIGN.equalsIgnoreCase(relationItem.getToType())) {
                    item.setCampaignId(relationItem.getToID());
                    item.setCampaign(relationItem.getToName());
                }

            }
        }
        item.setRelations(getRelations());
        if (crmAccountId != null) {
            if(crmAccountId != null){
                CRMService.App.get().getAccount(crmAccountId, null, new AsyncCallback<CrmAccountItem>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        Info.show("CRM Account missing", Info.Type.WARNING);
                    }

                    @Override
                    public void onSuccess(CrmAccountItem crmAccountItem) {
                        item.setCrmAccount(crmAccountItem);
                    }
                });
            }
        }
    }

    public void setCommand(ExtendedCommand command) {
        this.command = command;
    }

    @Override
    protected String getRelationType() {
        return ContactListItem.LEAD_CONTACT == contactType ? RelationItem.TYPE_LEAD : RelationItem.TYPE_CONTACT;
    }

    @Override
    protected String getRelationName() {
        return (firstName.getText() != null ? firstName.getText() : "") + (lastName.getText() != null ? lastName.getText() : "");
    }
}