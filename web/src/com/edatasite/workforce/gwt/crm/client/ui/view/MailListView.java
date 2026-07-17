package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.landing.HelpPanelGenerator;
import com.edatasite.workforce.gwt.core.client.ui.listTable.ImportFilePopUp;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.MailListItem;
import com.edatasite.workforce.gwt.crm.client.rpc.MassMailService;
import com.edatasite.workforce.gwt.crm.client.rpc.MassMailServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by IntelliJ IDEA.
 * User: Aziz
 * Date: 25-Jan-2010
 * Time: 18:11:32
 * To change this template use File | Settings | File Templates.
 */
public class MailListView extends BaseListView implements Constants {
    private static final CrmStrings crmStrings = CrmStrings.App.get();
    private ListingPanel<MailListItem> list;
    protected ContextMenu actions;
    private HashSet<MailListItem> selectedItems = new HashSet<>();

    public MailListView() {
        super(MAIL_LIST, wfmStrings.mailingList());
        if (Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_MAILING_LIST) && Utils.hasPermission(PermissionConstants.CRM_E_MAIL_MARKETING_TAB)) {
            setAddNew(() -> new MailListPopup());
        }
    }

    protected Widget onInitialize() {
        list = new GuideListingPanel(ListPanelType.MailListPanel, getColumnConfigs(), getListData(), getDesign(), SelectionGrid.SelectionPolicy.CHECKBOX);
        list.addSelectionRowHandler(selectedRows -> selectedItems = selectedRows);
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_MAIL_LIST_EDIT, MailListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_MAIL_LIST_ADD, MailListView.this, (sender, args) -> list.reloadPage());
        add(list);
        return null;
    }

    public FlowPanel getHelpContainer() {
        return HelpPanelGenerator.getHelpPanel(PermissionConstants.CRM_CONTEXT, PermissionConstants.CRM_MAILING_LIST);
    }

    private ColumnDefinitionConfig[] getColumnConfigs() {
        final ArrayList<ColumnDefinitionConfig> columns = new ArrayList<>();
        ColumnDefinitionConfig column = new ColumnDefinitionConfig<MailListItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final MailListItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                MenuPopItem mailListView = new MenuPopItem(wfmStrings.summaryView(), "icon-view", () -> SinksContainerFactory.entryPoint.onHistoryChanged("maillist|summary/" + item.getObjectId(), item.getName()));
                mailListView.ensureDebugId("viewMailList");
                actionItemCount++;
                menuBar.addItem(mailListView);
                if (Utils.hasPermission(PermissionConstants.CRM_EDIT_MAILING_LIST)) {
                    MenuPopItem mailListEdit = new MenuPopItem(wfmStrings.edit(), "icon-edit", () -> new MailListPopup(item.getObjectId(), null, false, null));
                    mailListEdit.ensureDebugId(wfmStrings.edit());
                    actionItemCount++;
                    menuBar.addItem(mailListEdit);
                }
                if (Utils.hasPermission(PermissionConstants.CRM_CONTACTS_IMPORT)) {
                    MenuPopItem importContacts = new MenuPopItem(crmStrings.importContacts(), "import-mailingList", () -> {
                        final ImportFilePopUp importFile = new ImportFilePopUp(ImportTypeEnum.CONTACT, null);
                        importFile.ensureDebugId("imp");
                        importFile.open();
                        importFile.setSubmitCompleted(() -> {
                            if (importFile.getObjectId() != null) {
                                goTo("import|add/add/" + importFile.getObjectId() + "/" + item.getObjectId());
                            }
                        });
                    });
                    importContacts.ensureDebugId("imp");
                    actionItemCount++;
                    menuBar.addItem(importContacts);
                }
                if (Utils.hasPermission(PermissionConstants.CRM_REMOVE_MAILING_LIST)) {
                    MenuPopItem mailListRemove = new MenuPopItem(wfmStrings.delete(), "icon-remove", () -> {
                        WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        wfmMessageBox.setTitle(wfmStrings.warning());
                        wfmMessageBox.setMessage(wfmStrings.sureYouWantToDelete());
                        wfmMessageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                LoadingPanel.loading(true);
                                MassMailService.App.get().deleteMailList(item.getObjectId(), new AbstractAsyncCallback<Void>() {
                                    @Override
                                    public void failure(Throwable throwable) {
                                        LoadingPanel.loading(false);
                                    }

                                    @Override
                                    public void success(Void result) {
                                        LoadingPanel.loading(false);
                                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.mailList()), Info.Type.INFO);
                                        list.reloadPage();
                                    }
                                });
                            }
                        });
                        wfmMessageBox.open();
                    });
                    mailListRemove.ensureDebugId("removeMailingList");
                    actionItemCount++;
                    menuBar.addItem(mailListRemove);
                }
                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        column.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setColumnSortable(false);
        columns.add(column);
        //Name
        column = new ColumnDefinitionConfig<MailListItem, SimpleLink>(wfmStrings.name(), MailListItem.NAME, 150) {
            @Override
            public SimpleLink getCellValue(MailListItem item) {
                return getLink(item.getName(), "maillist|summary/" + item.getObjectId(), item.getName());
            }
        };
        column.setMinimumColumnWidth(40);
        columns.add(column);
        //Members Count
        column = new ColumnDefinitionConfig<MailListItem, Integer>(crmStrings.membersCount(), MailListItem.MEMBERS_COUNT, 70) {
            @Override
            public Integer getCellValue(MailListItem item) {
                return item.getMembersCount();
            }
        };
        column.setMinimumColumnWidth(40);
        columns.add(column);
        //Status
        column = new ColumnDefinitionConfig<MailListItem, String>(wfmStrings.status(), MailListItem.ACTIVE, 50) {
            @Override
            public String getCellValue(MailListItem item) {
                return item.isActive() ? wfmStrings.active() : wfmStrings.inactive();
            }
        };
        column.setMinimumColumnWidth(40);
        columns.add(column);
        //Created
        column = new ColumnDefinitionConfig<MailListItem, String>(wfmStrings.createdDate(), MailListItem.CREATION_TIME, 70) {
            @Override
            public String getCellValue(MailListItem item) {
                return item.getCreatedDate() != null ? DateUtils.formatInternal(item.getCreatedDate()) : wfmStrings.notAvailable();
            }
        };
        column.setMinimumColumnWidth(40);
        columns.add(column);

        return columns.toArray(new ColumnDefinitionConfig[]{});
    }

    private ListingRequestProvider<MailListItem> getListData() {
        return (filterParametrs, callback) -> {
            MassMailServiceAsync massMailService = MassMailService.App.get();
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            filterParametrs.setShowInListing(Utils.hasPermission(PermissionConstants.CRM_SEE_ALL_MAILLIST_LIST));
            massMailService.getMailLists(filterParametrs, new AsyncCallback<ListResult<MailListItem>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                @Override
                public void onSuccess(ListResult<MailListItem> mailListItemListResult) {
                    callback.onSuccess(mailListItemListResult);
                }
            });
        };
    }

    private GuideListingPanelDesign getDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                if (Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_MAILING_LIST) && Utils.hasPermission(PermissionConstants.CRM_E_MAIL_MARKETING_TAB)) {
                    return (() -> new MailListPopup());
                }
                return null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                emptyDataTable.initEmptyDataTable(new DefaultNoItemsMessage(crmStrings.messCurrentlyMailLists()));
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_MAILING_LIST) && Utils.hasPermission(PermissionConstants.CRM_E_MAIL_MARKETING_TAB)) {
                    ActionButton addNew = getAddNewButton();
                    addNew.ensureDebugId("button_add_new");
                    addNew.addClickHandler(clickEvent -> new MailListPopup());
                    return addNew;
                }
                return null;
            }

            @Override
            public ActionButton initTopToolBarMore() {
                final ActionButton more = new ActionButton(ActionButton.getMoreString(), "", ActionButton.Type.TOOLMENU);
                more.ensureDebugId("crmContactMore");
                more.addClickHandler(event -> {
                    MenuBar menuBar = getActionsForSelections();
                    menuBar.setAutoOpen(true);
                    more.setMenu(menuBar);
                });
                return more;
            }
        };
    }

    private MenuBar getActionsForSelections() {
        if (!(list.getPagingScrollTable().getSelectedRowValues() == null || list.getPagingScrollTable().getSelectedRowValues().size() < 1)) {
            if (actions == null) {
                actions = new ContextMenu();
                actions.getMenuBar().setAutoOpen(true);
                if (Utils.hasPermission(PermissionConstants.CRM_REMOVE_MAILING_LIST)) {
                    actions.addMenuItem(wfmStrings.delete(), true, () -> {
                        actions.hide();
                        deleteSelection();
                    });
                }
            }
        }
        actions.getMenuBar().setAutoOpen(true);
        return actions.getMenuBar();
    }

    protected void deleteSelection() {
        if (selectedItems.size() == 0) {
            Info.show(wfmMessages.pleaseSelectOneRow(property.getSingular(wfmStrings.mailingList())), Info.Type.WARNING);
        } else {
            deleteMailItems(new ArrayList<>(selectedItems));
        }
    }

    private void deleteMailItems(final ArrayList<MailListItem> items) {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
        messageBox.setTitle(wfmStrings.warning());
        String message = wfmStrings.areYouSureYouWantToDeleteTheSelectedRecords();
        messageBox.setMessage(message);
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                deleteMail(items);
            }
        });
        messageBox.open();
    }

    private void deleteMail(final ArrayList<MailListItem> items) {
        LoadingPanel.loading(true);
        MassMailService.App.get().deleteMails(getIDs(items), new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(Void result) {
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.mailList()), Info.Type.INFO);
                list.reloadPage();
            }
        });

    }

    protected ArrayList<Integer> getIDs(ArrayList<MailListItem> selectedMails) {
        ArrayList<Integer> ids = new ArrayList<>();
        if (selectedMails.size() > 0) {
            for (MailListItem item : selectedMails) {
                if (item != null && !ids.contains(item.getObjectId())) {
                    ids.add(item.getObjectId());
                }
            }
        }
        return ids;
    }

    public String getIconStyle() {
        return "crm mail-list";
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
        return MAIL_LIST;
    }
}