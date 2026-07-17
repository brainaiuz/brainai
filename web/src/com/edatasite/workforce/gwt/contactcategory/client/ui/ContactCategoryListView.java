package com.edatasite.workforce.gwt.contactcategory.client.ui;

import com.edatasite.workforce.gwt.contact.client.ui.AddContactView;
import com.edatasite.workforce.gwt.contactcategory.client.rpc.ContactCategoryListItem;
import com.edatasite.workforce.gwt.contactcategory.client.rpc.ContactCategoryService;
import com.edatasite.workforce.gwt.contactcategory.client.rpc.ContactCategoryServiceAsync;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DataListBox;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.crm.client.localization.CrmMessages;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

/**
 * Created by Dilshod Madrahimov on 5/10/18.
 */
public class ContactCategoryListView extends BaseListView implements Constants {

    private static final CrmStrings crmStrings = CrmStrings.App.get();
    private static final CrmMessages crmMessages = CrmMessages.App.get();
    private static final ContactCategoryServiceAsync contactCategoryService = ContactCategoryService.App.get();

    private ListingPanel<ContactCategoryListItem> list;

    public ContactCategoryListView() {
        super("contactCategoryList", crmStrings.contactCategories());
    }

    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.ContactCategoryListPanel, getColumnConfigs(), getListData(), getDisagn());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CONTACT_CATEGORY_ADD, ContactCategoryListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CONTACT_CATEGORY_DELETE, ContactCategoryListView.this, (sender, args) -> list.reloadPage());
        add(list);
        return null;
    }

    private ColumnDefinitionConfig[] getColumnConfigs() {
        ArrayList<ColumnDefinitionConfig> columns = new ArrayList<>();

        ColumnDefinitionConfig column = new ColumnDefinitionConfig<ContactCategoryListItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(ContactCategoryListItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                if (Utils.hasPermission(PermissionConstants.CRM_CONTACT_CATEGORY_EDIT)) {
                    MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                    edit.getElement().setId("Contact_category_edit_button");
                    edit.setCommand(() -> editContactCategory(item.getObjectID()));
                    actionItemCount++;
                    menuBar.addItem(edit);
                }

                if (Utils.hasPermission(PermissionConstants.CRM_CONTACT_CATEGORY_DELETE) &&
                        item.getOwner() != null && !item.isSystemCategory() && item.getOwner() != null && Utils.getUserID().equals(item.getOwner().getObjectId())) {
                    MenuPopItem deleteItem = new MenuPopItem(wfmStrings.delete(), "icon-remove");
                    deleteItem.getElement().setId("Contact_category_delete_button");
                    deleteItem.setCommand(() -> removeContactCategory(item));
                    actionItemCount++;
                    menuBar.addItem(deleteItem);
                }

                if (Utils.hasPermission(PermissionConstants.CRM_CONTACT_CATEGORY_SHARE)) {
                    MenuPopItem share = new MenuPopItem(wfmStrings.share());
                    share.setCommand(() -> sharing(item.getObjectID()));
                    actionItemCount++;
                    menuBar.addItem(share);
                }

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };

        column.setColumnSortable(false);
        column.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);

        columns.add(column);

        //Name
        column = new ColumnDefinitionConfig<ContactCategoryListItem, String>(wfmStrings.name(), ContactCategoryListItem.NAME, 80) {
            @Override
            public String getCellValue(ContactCategoryListItem item) {
                return item.getName();
            }
        };
        column.setMinimumColumnWidth(80);
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns.add(column);

        //Parent
        column = new ColumnDefinitionConfig<ContactCategoryListItem, String>(wfmStrings.parent(), ContactCategoryListItem.PARENT_NAME, 80) {
            @Override
            public String getCellValue(ContactCategoryListItem item) {
                return item.getParent() != null ? item.getParent().getName() : "";
            }
        };
        column.setMinimumColumnWidth(80);
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns.add(column);

        //Parent
        column = new ColumnDefinitionConfig<ContactCategoryListItem, String>(wfmStrings.description(), ContactCategoryListItem.DESCRIPTION, 100) {
            @Override
            public String getCellValue(ContactCategoryListItem item) {
                return item.getDescription() != null ? item.getDescription() : "";
            }
        };
        column.setMinimumColumnWidth(100);
        column.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns.add(column);


        return columns.toArray(new ColumnDefinitionConfig[columns.size()]);
    }


    private ListingPanelDesign getDisagn() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (Utils.hasPermission(PermissionConstants.CRM_CONTACT_CATEGORY_ADD)) {
                    ActionButton addNew = getAddNewButton();
                    addNew.addClickHandler(event -> addContactCategory());
                    return addNew;
                }
                return null;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                emptyDataTable.initEmptyDataTable(new DefaultNoItemsMessage(wfmMessages.currentlyThereAreNotCategories()));
            }
        };
    }


    private ListingRequestProvider<ContactCategoryListItem> getListData() {
        return (filterParameter, callback) -> contactCategoryService.getContactCategoryList(filterParameter, new AbstractAsyncCallback<ListResult<ContactCategoryListItem>>() {
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess(ListResult<ContactCategoryListItem> result) {
                callback.onSuccess(result);
            }
        });
    }

    public String getIconStyle() {
        return null;
    }

    private void removeContactCategory(ContactCategoryListItem item) {
        Integer categoryID = item.getObjectID();
        WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
        messageBox.setTitle(wfmStrings.confirmation());
        messageBox.setMessage(crmMessages.messageText(item.getName()));
        messageBox.setWidth(500);

        KpiCheckBox deleteWithChildren = new KpiCheckBox("");
        DataListBox categoryListBox = new DataListBox();
        categoryListBox.setWithoutNullLabel(true);
        categoryListBox.setWidth(MIN_DEFAULT_WIDTH);
        contactCategoryService.getContactCategories(new AbstractAsyncCallback<ArrayList<ContactCategoryListItem>>() {
            @Override
            public void failure(Throwable throwable) {
                GWT.log(throwable.getMessage());
            }

            @Override
            public void success(ArrayList<ContactCategoryListItem> categories) {
                categoryListBox.setItems(AddContactView.removeSystemsCategories(ContactCategoryListItem.getAsTreeSelectItem(categories, item)));
            }
        });

        FlexTable container = new FlexTable();
        HTML listBoxLabel = new HTML(crmMessages.listBoxLabelText(item.getName()));
        HTML checkBoxLabel = new HTML(crmStrings.deleteSubCategories());

        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                LoadingPanel.loading(false);
                Integer selectedCategoryID = categoryListBox.getSelectedId(true);
                if (selectedCategoryID == null) {
                    LoadingPanel.loading(true);
                    Info.show(crmStrings.pleaseSelectCategoryToMoveThe() + ".", Info.Type.WARNING);
                } else {
                    contactCategoryService.deleteContactCategory(categoryID, selectedCategoryID, deleteWithChildren.getValue(), new AbstractAsyncCallback<Boolean>() {
                        @Override
                        public void failure(Throwable throwable) {
                            LoadingPanel.loading(false);
                            Info.show(wfmStrings.errorOccurredWhileDeleting(), Info.Type.WARNING);
                        }

                        @Override
                        public void success(Boolean result) {
                            LoadingPanel.loading(false);
                            Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.category()), Info.Type.INFO);
                            messageBox.close();
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CONTACT_CATEGORY_DELETE, result, ContactCategoryListView.this);
                        }
                    });
                }
            }
        });

        container.setWidget(0, 0, listBoxLabel);
        container.setWidget(0, 1, categoryListBox);
        container.setWidget(1, 0, checkBoxLabel);
        container.setWidget(1, 1, deleteWithChildren);

        messageBox.add(container);
        messageBox.center();
    }

    private void editContactCategory(Integer objectID) {
        new ContactCategoryPropertiesDialog(objectID, null);
    }

    private void addContactCategory() {
        new ContactCategoryPropertiesDialog(null, null);
    }

    private void sharing(Integer objectID) {
        new ContactCategoryPropertiesDialog(objectID, null, true);
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

}
