package com.edatasite.workforce.gwt.crm.client.ui;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactService;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.form.CustomForm;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTable;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableColumn;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.DynamicTableItem;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.AddListener;
import com.edatasite.workforce.gwt.core.client.ui.dynamicTable.listeners.ColumnStatements;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.splitButton.MaterialSplitButton;
import com.edatasite.workforce.gwt.crm.client.localization.CrmMessages;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialLink;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: Jan 12, 2008
 * Time: 7:51:15 PM To
 * change this template use File | Settings | File Templates.
 */
public class AddMultiLeadView extends CustomForm implements CommandConstants, Constants, Colapse {
    private static final CrmStrings crmStrings = CrmStrings.App.get();
    private static final CrmMessages crmMessages = CrmMessages.App.get();
    private final boolean isLead;
    private Integer crmAccountID;
    private String crmAccountName;

    private Integer relationID;
    private String relationType;

    public Integer objectId;
    public ContactListItem item;
    private DataListBox assignee;
    private ArrayList<DataListBox> assigneeList;
    private TextBox firstName;
    private TextBox lastName;
    private TextBox company;
    private TextBox email;
    private TextBox phone;
    private boolean saveAndClose = false;

    private static final int COLUMNS_COUNT = 6;
    private DynamicTable dynamicTable;
    private HorizontalPanel dynaPanel;

    public AddMultiLeadView(boolean isLead, String name, String description) {
        super(name);
        setDescription(description);
        this.isLead = isLead;
        this.objectId = null;
    }

    public AddMultiLeadView(boolean isLead, String name, String description, Integer crmAccountID) {
        this(isLead, name, description);
        this.crmAccountID = crmAccountID;
    }

    public AddMultiLeadView(boolean isLead, String name, String description, Integer relationID, String relationType) {
        this(isLead, name, description);
        this.relationID = relationID;
        this.relationType = relationType;
        if (RelationItem.TYPE_CRM_ACCOUNT.equals(relationType)) {
            this.crmAccountID = relationID;
        }
    }

    public String getIconStyle() {
        return null;
    }


    protected Widget onInitialize() {
        super.onInitialize();
        if (crmAccountID != null) {
            LoadingPanel.loading(true);
            CRMService.App.get().getCrmAccountNameByID(crmAccountID, new AbstractAsyncCallback<String>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(String result) {
                    LoadingPanel.loading(false);
                    if (result != null) {
                        crmAccountName = result;
                    }
                    initialize();
                }
            });
        } else {
            initialize();
        }
        return this;
    }

    public void initialize() {
        initDynamicTable();
        addTitleField(CONTACT_INFORMATION, isLead ? property.getSingular(wfmStrings.basicDetails(), wfmStrings.lead()) : Property.get(Constants.Contacts, wfmStrings.contactInformation(), wfmStrings.contact()));
        addField(WEB_ADDRESS, dynaPanel);
        show();
    }

    @Override
    protected void addButtons() {
        MaterialLink save = new MaterialLink(wfmStrings.save());
        MaterialSplitButton splitButton = new MaterialSplitButton(save);
        save.addClickHandler(event -> {
            saveAndClose = true;
            save();
        });

        MaterialLink saveAdd = new MaterialLink(wfmStrings.saveAndNew());
        saveAdd.addClickHandler(event -> {
            saveAndClose = false;
            save();
        });
        splitButton.addItem(saveAdd);
        addButton(splitButton);

    }

    @Override
    protected void getDataToFillFields() {
        LoadingPanel.loading(true);
        CRMService.App.get().editLead(objectId, Utils.isWebForm() ? Utils.getWebFormID() : null, new AbstractAsyncCallback<ContactListItem>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            public void success(final ContactListItem contactItem) {
                Scheduler.get().scheduleDeferred(() -> {
                    LoadingPanel.loading(false);
                    item = contactItem;
                    if (item.getObjectId() == null) {
                        setDefaultValue();
                    }
                });
            }
        });
    }

    private void setDefaultValue() {
        for (DataListBox listBox : assigneeList) {
            listBox.setItems(item.getLeadAssignees());
            listBox.setSelectedByValue(item.getOwner());
        }
    }

    @Override
    protected String getFormID() {
        return LayoutRPC.MULTILEAD_OR_CONTACT_FORM;
    }

    @Override
    protected String getFormType() {
        return LayoutRPC.ADD;
    }

    @Override
    protected String getWikiCode() {
        return isLead ? PermissionConstants.ADD_NEW_LEAD : PermissionConstants.CRM_CONTACT_ADD;
    }

    private void initDynamicTable() {
        dynamicTable = new DynamicTable(getColumnArray());
        assigneeList = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            Widget[] widgets = getWidgetArray(null);
            dynamicTable.addRow(widgets);
        }
        dynamicTable.addListener(new AddListener() {
            public void plusClicked(int rowId) {
                Widget[] widgets = getWidgetArray(null);
                dynamicTable.insertRow(rowId + 1, widgets);
            }

            public void minusClicked(int rowId, Integer objectId) {
            }
        });
        dynaPanel = new HorizontalPanel();
        dynaPanel.add(dynamicTable);
    }

    private Widget[] getWidgetArray(Map.Entry<ContactListItem, Integer> entry) {
        Widget[] widgets = new Widget[COLUMNS_COUNT];

        assignee = new DataListBox();
        if (item != null) {
            assignee.setItems(item.getLeadAssignees());
            assignee.setSelectedByValue(item.getOwner());
        }
        assigneeList.add(assignee);
        firstName = new TextBox();
        lastName = new TextBox();
        company = new TextBox();
        if (crmAccountName != null) {
            company.setText(crmAccountName);
        }
        email = new TextBox();
        phone = new TextBox();
        Validation.addPhoneNumberKeyboardListener(phone);

        widgets[0] = assignee;
        widgets[1] = firstName;
        widgets[2] = lastName;
        widgets[3] = company;
        widgets[4] = email;
        widgets[5] = phone;

        if (entry != null && entry.getKey() != null && entry.getValue() != null) {
            assignee.setSelectedByValue(entry.getKey().getLeadAssignee());
            firstName.setText(entry.getKey().getFirstName());
            lastName.setText(entry.getKey().getLastName());
            company.setText(entry.getKey().getCrmAccount().getName());
            email.setText(entry.getKey().getPrimaryEmail());
            phone.setText(entry.getKey().getPrimaryPhone());
            if (entry.getValue() == -1) {
                firstName.setStyleName(ERROR_FORM_STYLE);
                lastName.setStyleName(ERROR_FORM_STYLE);
            } else if (entry.getValue() == -2) {
                email.setStyleName(ERROR_FORM_STYLE);
            }
        }
        return widgets;
    }

    private DynamicTableColumn[] getColumnArray() {
        DynamicTableColumn[] columns = new DynamicTableColumn[COLUMNS_COUNT];
        int countIndex = 0;

        columns[countIndex++] = new DynamicTableColumn((isLead) ? wfmStrings.assignee() : wfmStrings.owner(), (isLead) ? wfmStrings.assignee() : wfmStrings.owner(), new ColumnStatements(".", ""), 140);
        columns[countIndex++] = new DynamicTableColumn(wfmStrings.firstName(), wfmStrings.firstName(), new ColumnStatements(".", wfmStrings.pleaseEnterFirstName()), 100);
        columns[countIndex++] = new DynamicTableColumn(wfmStrings.lastName(), wfmStrings.lastName(), new ColumnStatements(".", wfmStrings.pleaseEnterLastName()), 100);
        columns[countIndex++] = new DynamicTableColumn(wfmStrings.company(), wfmStrings.company(), new ColumnStatements("", wfmStrings.enterCompanyName()), 140);
        columns[countIndex++] = new DynamicTableColumn(wfmStrings.email(), wfmStrings.email(), new ColumnStatements("", ""), 140);
        columns[countIndex] = new DynamicTableColumn(wfmStrings.phone(), wfmStrings.phone(), new ColumnStatements("", ""), 100);

        return columns;
    }


    private boolean validate() {
        int errors = 0;
        boolean hasLead = false;
        dynamicTable.resetValidation();
        for (int rowId = 0; rowId < dynamicTable.getRowNumber(); rowId++) {
            DynamicTableItem tableItem = dynamicTable.getItem(rowId);
            TextBox firstName = (TextBox) tableItem.getColumnById(wfmStrings.firstName());
            TextBox lastName = (TextBox) tableItem.getColumnById(wfmStrings.lastName());
            TextBox email = (TextBox) tableItem.getColumnById(wfmStrings.email());
            if (!Utils.isNullOrEmpty(firstName.getText()) || !Utils.isNullOrEmpty(lastName.getText()) || !Utils.isNullOrEmpty(email.getText())) {
                hasLead = true;
                if (Utils.isNullOrEmpty(firstName.getText())) {
                    dynamicTable.notValid(rowId, wfmStrings.firstName());
                    errors++;
                }
                if (Utils.isNullOrEmpty(lastName.getText())) {
                    dynamicTable.notValid(rowId, wfmStrings.lastName());
                    errors++;
                }
                if (!Utils.isNullOrEmpty(email.getText()) && !Validation.validEmailFormat(email.getText(), false)) {
                    dynamicTable.notValid(rowId, wfmStrings.email());
                    errors++;
                }
            }
        }
        if (errors > 0) {
            Info.warn(wfmStrings.sureEnteredAllData());
            return false;
        } else if (!hasLead) {
            Info.warn(crmStrings.pleaseEnterAtLeastOneEntry());
            return false;
        }
        return true;
    }

    public void save() {
        if (!validate()) {
            return;
        }
        ArrayList<ContactListItem> leads = new ArrayList<>();
        for (int i = 0; i < dynamicTable.getRowNumber(); i++) {
            DynamicTableItem tableItem = dynamicTable.getItem(i);
            ContactListItem item = getCrmEntity(tableItem);
            if (item != null) {
                leads.add(item);
            }
        }
        LoadingPanel.loading(true);
        ContactService.App.get().saveMultipleContacts(leads, true, new AbstractAsyncCallback<HashMap<ContactListItem, Integer>>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(property.getSingular(wfmStrings.messAddLeadError(), wfmStrings.lead()), Info.Type.WARNING);
            }

            public void success(HashMap<ContactListItem, Integer> result) {
                LoadingPanel.loading(false);
                WfmUiEventsBus.fireWfmUiEvent(isLead ? WfmUiEventType.ON_LEADS_ADD_EDIT : WfmUiEventType.ON_CONTACT_ADD, result, AddMultiLeadView.this);
                if (result.size() == 0) {
                    Info.show(property.getSingular(wfmStrings.messSuccessfullyAdded(), isLead ? wfmStrings.lead() : wfmStrings.contact()), Info.Type.INFO);
                    onShellOk();
                } else {
                    dynamicTable.removeItems();
                    Info.show(crmMessages.warningDuplicateDetected(isLead ? property.getSingular(wfmStrings.lead()) : Property.get(Constants.Contacts, wfmStrings.contact())), Info.Type.WARNING);
                    for (Map.Entry<ContactListItem, Integer> entry : result.entrySet()) {
                        dynamicTable.addRow(getWidgetArray(entry));
                    }
                }
            }
        });
    }

    private void onShellOk() {
        if (saveAndClose) {
            closeTab();
        } else {
            reinit();
        }
    }

    public void reinit() {
        saveAndClose = false;
        dynaPanel.clear();
        initDynamicTable();
    }

    public ContactListItem getCrmEntity(DynamicTableItem tableItem) {
        ContactListItem lead = new ContactListItem();
        lead.setContactType(ContactListItem.LEAD_CONTACT);
        if (relationID != null && relationType != null) {
            if (RelationItem.TYPE_CAMPAIGN.equals(relationType)) {
                lead.setCampaignId(relationID);
            }//set others
        }
        DataListBox assignee = (DataListBox) tableItem.getColumnById((isLead) ? wfmStrings.assignee() : wfmStrings.owner());
        TextBox firstName = (TextBox) tableItem.getColumnById(wfmStrings.firstName());
        TextBox lastName = (TextBox) tableItem.getColumnById(wfmStrings.lastName());
        TextBox company = (TextBox) tableItem.getColumnById(Property.get(Constants.CRM_ACCOUNT_LIST, wfmStrings.company()));
        TextBox email = (TextBox) tableItem.getColumnById(wfmStrings.email());
        TextBox phone = (TextBox) tableItem.getColumnById(wfmStrings.phone());
        if (!Utils.isNullOrEmpty(firstName.getText()) || !Utils.isNullOrEmpty(lastName.getText()) || !Utils.isNullOrEmpty(email.getText())) {
            if (isLead) {
                if (assignee.getSelectedItem() != null) {
                    lead.setLeadAssigneeID(assignee.getSelectedItem().getId());
                    lead.setLeadAssignee(assignee.getSelectedItem().getName());
                }
            } else {
                if (assignee.getSelectedItem() != null) {
                    lead.setOwnerId(assignee.getSelectedItem().getId());
                    lead.setOwner(assignee.getSelectedItem().getName());
                }
            }
            lead.setFirstName(firstName.getText());
            lead.setLastName(lastName.getText());
            lead.getCrmAccount().setName(company.getText());
            lead.getHomeEmail().add(email.getText());
            lead.setPrimaryEmail(email.getText());
            lead.getHomePhone().add(phone.getText());
            lead.setPrimaryPhone(phone.getText());
            lead.setContactType(isLead ? ContactListItem.LEAD_CONTACT : ContactListItem.CRM_CONTACT);
            lead.setCheckForDuplicates(true);
            lead.setAddresses(new ArrayList<>());
            if (item.getPrimaryAddress() != null) {
                lead.getAddresses().add(item.getPrimaryAddress());
                lead.setPrimaryAddress(item.getPrimaryAddress());
            }
            return lead;
        } else {
            return null;
        }
    }

    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    @Override
    public String getPropertyCode() {
        return Constants.LEADS;
    }
}