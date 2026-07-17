package com.edatasite.workforce.gwt.contactcategory.client.ui;

import com.edatasite.workforce.gwt.contactcategory.client.rpc.ContactCategoryListItem;
import com.edatasite.workforce.gwt.contactcategory.client.rpc.ContactCategoryService;
import com.edatasite.workforce.gwt.contactcategory.client.rpc.ContactCategoryServiceAsync;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.Validation;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.rbac.GroupMembersViewItem;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.documents.client.PermissionsAddDialog;
import com.edatasite.workforce.gwt.documents.client.PermissionsList;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.html.Div;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Dilshod Madrahimov on 5/10/18.
 */

public class ContactCategoryPropertiesDialog extends KpiModal {

    private final ContactCategoryServiceAsync contactCategoryService = ContactCategoryService.App.get();
    private ArrayList<GroupMembersViewItem> groups = null;
    public Integer objectID;
    public Integer parentID;
    private ContactCategoryListItem item;
    private final DataListBox parentListBox;
    private final TextBox categoryNameTextBox;
    private final TextArea2 descriptionTextArea;

    private PermissionsList permList;

    /**
     * A flag that denotes whether the dialog will be used to create or modify a
     * contact Category.
     */
    private boolean create = false;

    private final TabPanel inner;
    private boolean openSharingPart = false;

    /**
     * The widget's constructor.
     *
     * @param objectID
     * @param parentID
     */
    public ContactCategoryPropertiesDialog(Integer objectID, Integer parentID, boolean... openSharingPart) {
        super();
        this.objectID = objectID;
        this.parentID = parentID;
        this.openSharingPart = openSharingPart != null && openSharingPart.length > 0 && openSharingPart[0];

        inner = new DecoratedTabPanel();
        categoryNameTextBox = new TextBox();
        categoryNameTextBox.ensureDebugId("category_name");

        parentListBox = new DataListBox();
        parentListBox.ensureDebugId("category_parent");

        descriptionTextArea = new TextArea2();
        descriptionTextArea.ensureDebugId("category_description");

        LoadingPanel.loading(true);
        contactCategoryService.editContactCategory(this.objectID, new AbstractAsyncCallback<ContactCategoryListItem>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            public void success(final ContactCategoryListItem o) {
                DeferredCommand.addCommand(() -> {

                    LoadingPanel.loading(false);
                    if (o != null && o.getObjectID() == null) {
                        create = true;
                    }
                    item = o;
                    initialize();
                    LoadingPanel.loading(false);
                });
            }

        });
    }

    private void initialize() {
        ArrayList<ContactCategoryListItem> items = null;
        if (item.getParents() != null) {
            items = new ArrayList(Arrays.asList(item.getParents()));
        }
        parentListBox.setItems(ContactCategoryListItem.getAsTreeSelectItem(items));
        if (parentID != null) {
            parentListBox.setSelected(parentID);
        }
        if (item.getParentID() != null) {
            parentListBox.setSelected(item.getParentID());
        }
        categoryNameTextBox.setText(item.getName());
        categoryNameTextBox.ensureDebugId("Category_Name");

        descriptionTextArea.setText(item.getDescription());
        descriptionTextArea.ensureDebugId("Additional_Information");

        groups = item.getGroups();

        permList = new PermissionsList(item.getPermissions(), item.getOwner(), null, false);

        VerticalPanel outer = new VerticalPanel();
        FocusPanel focusPanel = new FocusPanel(outer);

        VerticalPanel generalPanel = new VerticalPanel();
        HorizontalPanel permButtons = new HorizontalPanel();
        VerticalPanel permPanel = new VerticalPanel();

        FlexTable generalTable = new FlexTable();
        int row = 0;
        generalTable.setHTML(row, 0, wfmStrings.categoryName());
        generalTable.setWidget(row, 1, categoryNameTextBox);
        row++;

        generalTable.setHTML(row, 0, wfmStrings.parent());
        generalTable.setWidget(row, 1, create ? parentListBox : item.getParent() != null ? new HTML(item.getParent().getName()) : new HTML("--"));
        row++;

        if (item.getModificationDate() != null) {
            generalTable.setHTML(row, 0, wfmStrings.modifiedDate());
            generalTable.setWidget(row, 1, item.getModificationDate() != null ? new HTML(DateTimeFormat.getFormat(Utils.getShortDateFormat()).format(item.getModificationDate().getNonConvertedDate())) : new HTML("--"));
            row++;
        }

        generalTable.setHTML(row, 0, wfmStrings.createdBy());
        generalTable.setWidget(row, 1, item.getOwner() != null ? new HTML(item.getOwner().getName()) : new HTML("--"));
        row++;

        generalTable.setHTML(row, 0, wfmStrings.additionalInformation());
        generalTable.setWidget(row, 1, descriptionTextArea);

        generalTable.addStyleName("props-table");
        generalTable.getElement().addClassName("file--ContactCategoryPropertiesDialog");
        generalTable.getFlexCellFormatter().setStyleName(0, 0, "props-labels");
        generalTable.getFlexCellFormatter().setStyleName(1, 0, "props-labels");
        generalTable.getFlexCellFormatter().setStyleName(2, 0, "props-labels");
        generalTable.getFlexCellFormatter().setStyleName(3, 0, "props-labels");
        if (item.getModificationDate() != null) {
            generalTable.getFlexCellFormatter().setStyleName(4, 0, "props-labels");
        }
        generalPanel.setSpacing(4);
        generalPanel.add(generalTable);

        categoryNameTextBox.setText(create ? "" : item.getName());
        categoryNameTextBox.setMaxLength(255);
        descriptionTextArea.setText(create ? "" : item.getDescription());

        if (create) {
            setTitle(wfmStrings.addCategory());
        } else {
            setTitle("Category properties");
            parentListBox.setEnabled(false);
        }

        WfmButton2 ok = new WfmButton2((create ? wfmStrings.create() : wfmStrings.update()), WfmButton2.BTN_PRIMARY, event -> {
            if (!validateFolderName()) {
                return;
            }
            saveContactCategory();
        });
        ok.ensureDebugId("create_button");

        WfmButton2 cancel = new WfmButton2(wfmStrings.cancel(), event -> closeDialog());
        cancel.ensureDebugId("cancel_button");

        WfmButton2 add = new WfmButton2(wfmStrings.addGroup(), event -> {
            PermissionsAddDialog dlg = new PermissionsAddDialog(groups, permList, false);
            dlg.center();
        });
        add.ensureDebugId("add_group_button");
        permButtons.add(add);
        permButtons.setCellHorizontalAlignment(add, HasHorizontalAlignment.ALIGN_CENTER);

        WfmButton2 addUser = new WfmButton2(wfmStrings.addUser(), event -> {
            PermissionsAddDialog dlg = new PermissionsAddDialog(groups, permList, true);
            dlg.center();
        });
        addUser.ensureDebugId("add_user_button");
        permButtons.add(addUser);
        permButtons.setCellHorizontalAlignment(addUser, HasHorizontalAlignment.ALIGN_CENTER);

        permButtons.setCellHorizontalAlignment(cancel, HasHorizontalAlignment.ALIGN_CENTER);
        permButtons.setSpacing(8);
        permButtons.addStyleName("doc-TabPanelBottom");

        Div scrollPanel = new Div();
        scrollPanel.setStyleName("scroll-box");
        scrollPanel.add(permList);
        permPanel.add(scrollPanel);
        permPanel.add(permButtons);

        inner.setAnimationEnabled(true);
        inner.addStyleName("decorated-tab--panel");
        inner.add(generalPanel, wfmStrings.generals());
        if (!create && item.getCategoryType() != ContactCategoryListItem.PRIVATE_CONTACT_CATEGORY && Utils.hasPermission(PermissionConstants.CRM_CONTACT_CATEGORY_SHARE)) {
            inner.add(permPanel, wfmStrings.share());
        }

        this.selectTab(openSharingPart && !create ? 1 : 0);
        setWidth(550);
        outer.add(inner);
        outer.addStyleName("doc-TabPanelBottom");
        focusPanel.setFocus(true);

        add(outer);
        addButton(cancel);
        addButton(ok);
        addStyleName("no-border file--ContactCategoryPropertiesDialog");

        center();
    }

    @Override
    public void center() {
        super.center();
        categoryNameTextBox.setFocus(true);
    }


    /**
     * Enables IE selection prevention and hides the dialog
     * (we disable the prevention on creation of the dialog)
     */
    public void closeDialog() {
        close();
    }

    /**
     * Generate an RPC request to create a new contact Category or update .
     */
    private void saveContactCategory() {
        if (!validate()) {
            return;
        }
        setValues();
        LoadingPanel.loading(true);
        contactCategoryService.saveContactCategory(item, new AbstractAsyncCallback<ContactCategoryListItem>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(ContactCategoryListItem o) {
                LoadingPanel.loading(false);
                closeDialog();
                if (create) {
                    Info.show(Utils.textFormat(wfmStrings.messSuccessfullyAdded(), wfmStrings.newCategory()), Info.Type.INFO);
                } else {
                    Info.show(wfmStrings.messContactCategoryShared(), Info.Type.INFO);
                }
                if (o != null) {
                    item.setObjectID(o.getObjectID());
                }
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CONTACT_CATEGORY_ADD, item, ContactCategoryPropertiesDialog.this);
            }
        });
    }

    public void setValues() {
        if (item == null) {
            item = new ContactCategoryListItem();
        }
        if (objectID != null) {
            item.setObjectID(objectID);
        }
        item.setName(categoryNameTextBox.getText());
        if (create) {
            if (parentListBox.getSelectedId() != null && create) {
                item.setParentID(parentListBox.getSelectedId());
            }
        }
        item.setDescription(descriptionTextArea.getText());
        permList.updatePermissionsAccordingToInput();
        item.setPermissions(permList.getPermissions());
    }

    public boolean validate() {
        int errors = 0;
        if (!Validation.validateTextBoxRequired(categoryNameTextBox)) {
            errors++;
        }
        if (errors > 0) {
            Info.show(wfmStrings.sureEnteredAllData(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    public void selectTab(int tab) {
        if (tab == 0) {
            setTitle(objectID == null ? wfmStrings.addCategory() : "Edit Category");
        } else {
            setTitle("Category Properties");
            parentListBox.setEnabled(false);
        }
        inner.selectTab(tab);
    }

    private boolean validateFolderName() {
        if (!Utils.isValidFolderName(categoryNameTextBox.getText())) {
            Info.show(wfmStrings.youCantUseFolderName(), Info.Type.WARNING);
            return false;
        }
        return true;
    }
}