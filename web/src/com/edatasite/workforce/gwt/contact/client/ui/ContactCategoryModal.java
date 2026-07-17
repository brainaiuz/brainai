package com.edatasite.workforce.gwt.contact.client.ui;

import com.edatasite.workforce.gwt.contactcategory.client.rpc.ContactCategoryListItem;
import com.edatasite.workforce.gwt.contactcategory.client.rpc.ContactCategoryService;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.google.gwt.user.client.ui.TextBox;

import java.util.ArrayList;

public class ContactCategoryModal extends KpiModal {
    private DataListBox parent;
    private TextBox name;
    private TextArea2 description;
    private WfmButton2 save;

    public ContactCategoryModal() {
        super();
        setTitle(wfmStrings.addCategory());
        setWidth(350);
        initialize();
        getParents();
    }

    private void initialize() {
        parent = new DataListBox();
        parent.ensureDebugId("parent");

        name = new TextBox();
        name.ensureDebugId("name");

        description = new TextArea2(wfmStrings.description());
        description.setWidth("100%");
        description.setHeight(100);
        description.ensureDebugId("description");

        addWidget(parent, wfmStrings.parent());
        addWidget(name, wfmStrings.name());
        addWidget(description, null);

        addButton(new WfmButton2(wfmStrings.cancel(), clickEvent -> close()));
        save = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY, clickEvent -> save());
        addButton(save);

        open();
    }

    private void getParents() {
        LoadingPanel.loading(true, ContactCategoryModal.this);
        ContactCategoryService.App.get().getContactCategories(new AbstractAsyncCallback<ArrayList<ContactCategoryListItem>>() {
            @Override
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false, ContactCategoryModal.this);
            }

            @Override
            public void onSuccess(ArrayList<ContactCategoryListItem> result) {
                LoadingPanel.loading(false, ContactCategoryModal.this);
                if (result != null && result.size() > 0) {
                    parent.setItems(AddContactView.removeSystemsCategories(ContactCategoryListItem.getAsTreeSelectItem(result)));
                }
            }
        });
    }

    private void save() {
        if (!Validation.validateTextBoxRequired(name)) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return;
        }
        ContactCategoryListItem item = new ContactCategoryListItem();
        item.setName(name.getText());
        item.setParentID(parent.getSelectedId());
        item.setDescription(description.getText());
        save.setEnabled(false);
        LoadingPanel.loading(true, ContactCategoryModal.this);
        ContactCategoryService.App.get().saveContactCategory(item, new AbstractAsyncCallback<ContactCategoryListItem>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false, ContactCategoryModal.this);
                save.setEnabled(true);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(ContactCategoryListItem o) {
                LoadingPanel.loading(false, ContactCategoryModal.this);
                close();
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullyAdded(), wfmStrings.newCategory()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CONTACT_CATEGORY_ADD, item, ContactCategoryModal.this);
            }
        });
    }
}