package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSwitcher;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.crm.client.localization.CrmMessages;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.MailListItem;
import com.edatasite.workforce.gwt.crm.client.rpc.MassMailService;
import com.google.gwt.user.client.ui.TextBox;

/**
 * Created by: Azazello
 * Date: 1/25/2018
 * Time: 5:40 PM
 */
public class MailListPopup extends KpiModal {
    private static final CrmStrings crmStrings = CrmStrings.App.get();
    protected static final CrmMessages crmMessages = CrmMessages.App.get();
    private Integer object_id;
    private MailListItem item;
    private ContactListItem[] entities;
    private boolean isLead;
    private ListingFilterParameter filterParameter;
    private TextBox name;
    private TextArea2 description;
    private KpiSwitcher status;

    public MailListPopup() {
        super();
        setTitle(wfmStrings.createMailingList());
//        setSize("400px", "500px"); //https://prnt.sc/t38m4v

        initialize();
        getData();
    }

    public MailListPopup(Integer object_id, ContactListItem[] entities, boolean isLead, ListingFilterParameter filterParameter) {
        super();
        setTitle(object_id == null ? wfmStrings.createMailingList() : crmStrings.editMailList());
//        setSize("400px", "500px");
        this.object_id = object_id;
        this.entities = entities;
        this.isLead = isLead;
        this.filterParameter = filterParameter;
        initialize();
        getData();
    }

    private void initialize() {
        name = new TextBox();
        name.ensureDebugId("name");
        addWidget(name, wfmStrings.name());
        description = new TextArea2(2000, wfmStrings.description());
        description.setSize("100%", "100px");
        description.ensureDebugId("description");
        addWidget(description, null);
        status = new KpiSwitcher();
        status.ensureDebugId("status");
        status.setValue(true);
        addWidget(status, wfmStrings.active());
        WfmButton2 saveButton = new WfmButton2(object_id == null ? wfmStrings.save() : wfmStrings.update(), WfmButton2.BTN_PRIMARY, clickEvent -> save());
//        saveButton.ensureDebugId(object_id + "_mail_list_add_view");
        addButton(new WfmButton2(wfmStrings.cancel(), clickEvent -> close()));
        addButton(saveButton);
        open();
    }

    private void getData() {
        if (object_id != null) {
            LoadingPanel.loading(true);
            MassMailService.App.get().getMailList(object_id, new AbstractAsyncCallback<MailListItem>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(MailListItem result) {
                    LoadingPanel.loading(false);
                    item = result;
                    setValues();
                }
            });
        }
    }

    private void setValues() {
        name.setText(item.getName());
        description.setText(item.getDescription());
        status.setValue(item.isActive());
    }

    private void save() {
        if (!Validation.validateTextBoxRequired(name)) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return;
        }
        item = item == null ? new MailListItem() : item;
        item.setName(name.getText());
        item.setDescription(description.getText());
        item.setActive(status.getValue());
        item.setMembers(entities);
        LoadingPanel.loading(true);
        MassMailService.App.get().saveMailList(item, filterParameter, isLead, new AbstractAsyncCallback<Integer>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void onSuccess(Integer result) {
                LoadingPanel.loading(false);
                if (object_id == null) {
                    Info.show(crmMessages.mailingListCreating(), Info.Type.INFO);
                } else {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.mailingList()), Info.Type.INFO);
                }
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_MAIL_LIST_ADD, result, MailListPopup.this);
                close();
            }
        });
    }
}
