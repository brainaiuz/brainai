package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ContextMenu;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
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
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.MassMailService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created with IntelliJ IDEA.
 * User: Hasan Xo'janazarov
 * Date: 14.08.12
 * Time: 14:17
 * To change this template use File | Settings | File Templates.
 */
public class MailListMemberListView extends BaseListView implements Constants {
    private static final CrmStrings crmStrings = CrmStrings.App.get();
    private ListingPanel<SelectItem> listingTable;
    private HashSet<SelectItem> selectedItems = new HashSet<>();
    private final Integer mail_list_id;
    private final boolean showUnsubscribeds;
    protected ContextMenu actions;

    public MailListMemberListView(Integer mail_list_id, boolean showUnsubscribeds) {
        super(showUnsubscribeds ? "unsubscribedlist" : "memberlist", showUnsubscribeds ? crmStrings.unsubscribedUsers() : crmStrings.membersList());
        this.mail_list_id = mail_list_id;
        this.showUnsubscribeds = showUnsubscribeds;
    }

    @Override
    protected Widget onInitialize() {
        listingTable = new ListingPanel<>(ListPanelType.MailListMemberListPanel, getColumnConfigs(), getListingRequestProvider(), getListingPanelDesign(), SelectionGrid.SelectionPolicy.CHECKBOX);
        listingTable.addSelectionRowHandler(selectedRows -> selectedItems = selectedRows);
        add(listingTable);
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_MAILLIST_DELETED, MailListMemberListView.this, (sender, args) -> listingTable.reloadPage());
        return null;
    }

    private void removeContacts(ArrayList<Integer> contactIDs) {
        LoadingPanel.loading(true);
        MassMailService.App.get().unsubscribeFromMailList(mail_list_id, !showUnsubscribeds, contactIDs, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Void result) {
                LoadingPanel.loading(false);
                listingTable.reloadPage();
                Info.show(showUnsubscribeds ? crmStrings.userSuccessfullyMovedToMailingList() : Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.users()), Info.Type.INFO);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_MAILLIST_DELETED, result, MailListMemberListView.this);
            }
        });
    }


    private ColumnDefinitionConfig[] getColumnConfigs() {
        ArrayList<ColumnDefinitionConfig> columns = new ArrayList<>();
        //Name
        ColumnDefinitionConfig column = new ColumnDefinitionConfig<SelectItem, SimpleLink>(wfmStrings.name(), "name", 150) {
            @Override
            public SimpleLink getCellValue(SelectItem item) {
                return getLink(item.getName(), "contact|summary/" + item.getId());
            }
        };
        column.setMinimumColumnWidth(150);
        columns.add(column);
        //Email
        column = new ColumnDefinitionConfig<SelectItem, String>(wfmStrings.email(), "email", 150) {
            @Override
            public String getCellValue(SelectItem item) {
                return item.getDescription();
            }
        };
        column.setMinimumColumnWidth(150);
        columns.add(column);
        //Remove link
/*
        column = new ColumnDefinitionConfig<SelectItem, SimpleLink>(wfmStrings.action(), "action", 100) {
            @Override
            public SimpleLink getCellValue(final SelectItem item) {
                final SimpleLink removeLink = new SimpleLink(wfmStrings.delete());
                removeLink.addClickHandler(event -> {
                    WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, crmStrings.areYouSureYouWanttoRemovethisEmail());
                    wfmMessageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            ArrayList<Integer> ids = new ArrayList<>();
                            ids.add(item.getId());
                            removeContacts(ids);
                        }
                    });
                    wfmMessageBox.open();
                });
                return removeLink;
            }
        };
        column.setMinimumColumnWidth(50);
        column.setColumnSortable(false);
        columns.add(column);
*/
        return columns.toArray(new ColumnDefinitionConfig[columns.size()]);
    }

    private MenuBar getActionsForSelections() {
        if (actions == null) {
            actions = new ContextMenu();
            actions.getMenuBar()/*.setAutoOpen(true)*/;
            actions.addMenuItem(wfmStrings.delete(), true, () -> {
                actions.hide();
                if (selectedItems.size() > 0) {
                    WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, crmStrings.areYouSureYouWanttoRemovetheseEmails());
                    wfmMessageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            ArrayList<Integer> ids = new ArrayList<>();
                            selectedItems.forEach(item -> ids.add(item.getId()));
                            removeContacts(ids);
                        }
                    });
                    wfmMessageBox.open();
                }
            });
        }
        return actions.getMenuBar();
    }


    private ListingRequestProvider<SelectItem> getListingRequestProvider() {
        return (filterParametrs, callback) -> {
            filterParametrs = filterParametrs == null ? new ListingFilterParameter() : filterParametrs;
            filterParametrs.setShowUnsbcribeds(showUnsubscribeds);
            filterParametrs.setMailListID(mail_list_id);
            MassMailService.App.get().getMailListMembers(filterParametrs, new AbstractAsyncCallback<ListResult<SelectItem>>() {
                public void failure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                public void success(final ListResult<SelectItem> result) {
                    callback.onSuccess(result);
                }
            });
        };
    }

    private ListingPanelDesign getListingPanelDesign() {
        return new ListingPanelDesign() {

            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

//            @Override
//            public ActionButton initTopToolBarNew() {
//                ActionButton addNew = getAddNewButton();
//                addNew.ensureDebugId("button_add_new");
//                addNew.addClickHandler(clickEvent -> {
//
//                });
//                return addNew;
//            }
//
//            @Override
//            public ActionButton initTopToolBarMore() {
//                final ActionButton more = new ActionButton(ActionButton.getMoreString(), "", ActionButton.Type.TOOLMENU);
//                more.ensureDebugId("leadListMore");
//                more.addDomHandler(event -> {
//                    MenuBar menuBar = getActionsForSelections();
//                    menuBar.setAutoOpen(true);
//                    more.setMenu(menuBar);
//                    menuBar.setLayoutData(more);
//                }, MouseOverEvent.getType());
//                return more;
//            }
            @Override
            public ActionButton initTopToolBarMore() {
                    return getRemoveMoreButton(clickEvent -> deleteSelection());
            }


            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(crmStrings.thereAreNoAnyData());
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return true;
            }
        };
    }

    @Override
    public String getIconStyle() {
        return showUnsubscribeds ? "lead lead-list" : "crm mail-list";
    }

    @Override
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
    private void deleteSelection() {
        if (selectedItems.isEmpty()) {
            Info.show(wfmMessages.pleaseSelectOneRow("Mail List Member"), Info.Type.WARNING);
        } else {
            showDeleteMessage();
        }
    }
    private void showDeleteMessage() {
        WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, crmStrings.areYouSureYouWanttoRemovetheseEmails());
        wfmMessageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                ArrayList<Integer> ids = new ArrayList<>();
                selectedItems.forEach(item -> ids.add(item.getId()));
                removeContacts(ids);
            }
        });
        wfmMessageBox.open();
    }
}
