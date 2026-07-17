package com.edatasite.workforce.gwt.profile.client.ui.view.customfields.ui;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.rpc.EmailFilter;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;


/**
 * Created by IntelliJ IDEA.
 * User: Hayot
 * Date: 11/30/11
 * Time: 5:25 PM
 * To change this template use File | Settings | File Templates.
 */
public class EmailFilterListView extends BaseListView implements Constants {
    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    private static final AllInOneServiceAsync service = AllInOneService.App.get();
    private ListingPanel<EmailFilter> list;

    public EmailFilterListView() {
        super("EMAIL_FILTER_LIST", wfmStrings.emailFilters());
    }

    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.EmailFilterListPanel, getColumnConfigs(), getListData(), getDesign());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_FILTER_ADDED, EmailFilterListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_FILTER_DELETED, EmailFilterListView.this, (sender, args) -> list.reloadPage());
        add(list);
        return null;
    }

    private CustomColumnDefinitionConfig[] getColumnConfigs() {
        final ArrayList<CustomColumnDefinitionConfig> columns = new ArrayList<>();

        ColumnDefinitionConfig column = new ColumnDefinitionConfig<EmailFilter, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final EmailFilter item) {
                int actionItemCount = 0;

                MenuBar menuBar = new MenuBar(true);

                MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                edit.getElement().setId("Email_filter_edit_button");
                edit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("emailfilter|addemailfilter/" + item.getObjectID() + (item.isParent() ? "/parent" : ""), item.getName()));
                actionItemCount++;
                menuBar.addItem(edit);

                MenuPopItem deletePage = new MenuPopItem(wfmStrings.delete(), "icon-remove");
                deletePage.getElement().setId("Email_filter_delete_button");
                deletePage.setCommand(() -> {
                    final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    messageBox.setTitle(wfmStrings.warning());
                    messageBox.setMessage(wfmStrings.messAreDelete()
                            + item.getName() + " " + wfmStrings.filter());
                    messageBox.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            LoadingPanel.loading(true);
                            service.deleteEmailFilter(item.getObjectID(), new AbstractAsyncCallback() {
                                @Override
                                public void failure(Throwable caught) {
                                    LoadingPanel.loading(false);
                                }

                                @Override
                                public void success(Object result) {
                                    LoadingPanel.loading(false);
                                    list.reloadPage();
                                    Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.item()));
                                }
                            });
                        }
                    });
                    messageBox.open();
                });
                menuBar.addItem(deletePage);

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        column.setColumnSortable(false);
        column.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns.add(column);

        column = new ColumnDefinitionConfig<EmailFilter, SimpleLink>(wfmStrings.name(), EmailFilter.NAME, 100) {
            @Override
            public SimpleLink getCellValue(EmailFilter item) {
                return getLink(item.getName(), "emailfilter|addemailfilter/" + item.getObjectID() + (item.isParent() ? "/parent" : ""));
            }
        };
        column.setMinimumColumnWidth(40);
        columns.add(column);

        column = new ColumnDefinitionConfig<EmailFilter, String>(wfmStrings.parent(), EmailFilter.ISPARENT, 50) {
            @Override
            public String getCellValue(EmailFilter item) {
                return item.isParent() ? "" : item.getParent() != null ? item.getParent().getName() : "";
            }
        };
        column.setMinimumColumnWidth(40);
        columns.add(column);

        return columns.toArray(new CustomColumnDefinitionConfig[0]);
    }

    private ListingPanelDesign getDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                ActionButton addMenu = getAddNewButton(ActionButton.Type.TOOLMENU);
                MenuBar menuBar = new MenuBar(true);
                menuBar.setAutoOpen(true);

                if (Utils.hasPermission(PermissionConstants.ADD_NEW_LEAD)) {
                    MenuPopItem addLead = new MenuPopItem(wfmStrings.parentFilter());
                    addLead.ensureDebugId("lead_quick_add");
                    addLead.setScheduledCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("emailfilter|add/add//parent"));

                    MenuPopItem addMultipleLead = new MenuPopItem(wfmStrings.childFilter());
                    addMultipleLead.ensureDebugId("Crm_add_Multiple_Lead");
                    addMultipleLead.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("emailfilter|add/add"));

                    menuBar.addItem(addLead);
                    menuBar.addItem(addMultipleLead);
                }
                addMenu.setMenu(menuBar);
                return addMenu;
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(settingsStrings.currentlyNoFilters());
                message.setHref("emailfilter|add/add//parent");
                message.setTextBeforeLink(settingsStrings.addingFilterClicking());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private ListingRequestProvider<EmailFilter> getListData() {
        return (filterParametrs, callback) -> ProfileService.App.get().getEmailFilters(filterParametrs, new AbstractAsyncCallback<ListResult<EmailFilter>>() {
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess(ListResult<EmailFilter> result) {
                callback.onSuccess(result);
            }
        });
    }

    public String getIconStyle() {
        return "icon-settings-user-credentials";
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
