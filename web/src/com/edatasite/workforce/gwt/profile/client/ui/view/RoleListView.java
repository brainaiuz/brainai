package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RoleListItem;
import com.edatasite.workforce.gwt.core.client.rpc.RolePermissionService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

/**
 * Created by IntelliJ IDEA.
 * User: Fatkhulla
 * Date: 16.05.12
 * Time: 18:17
 * To change this template use File | Settings | File Templates.
 */
public class RoleListView extends BaseListView {

    private ListingPanel list;
    private int actionItemCount;
    private final SettingStrings settingsStrings = SettingStrings.App.get();

    public RoleListView() {
        super("role", WfmStrings.App.get().roles());

    }

    protected Widget onInitialize() {
        list = new ListingPanel(ListPanelType.RoleListPanel, getColumns(), getListProvider(), getListDesign());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ROLE_ADD, RoleListView.this, (sender, args) -> list.reloadPage());
        add(list);
        return null;
    }

    private ListingRequestProvider<RoleListItem> getListProvider() {
        return (filterParametrs, listingCallback) -> RolePermissionService.App.get().getRoleList(filterParametrs, new AsyncCallback<ListResult<RoleListItem>>() {
            public void onFailure(Throwable caught) {
                listingCallback.onFailure(caught);
            }

            @Override
            public void onSuccess(ListResult<RoleListItem> backendListListResult) {
                listingCallback.onSuccess(backendListListResult);
            }

        });
    }

    private ListingPanelDesign getListDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public ActionButton initTopToolBarNew() {

                ActionButton addNew = getAddNewButton();
                addNew.addClickHandler(e -> new AddRoleView(null));
                return addNew;
            }


            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;  //To change body of implemented methods use File | Settings | File Templates.
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                //To change body of implemented methods use File | Settings | File Templates.
            }


        };
    }

    private CustomColumnDefinitionConfig[] getColumns() {
        ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[4];
        columns[0] = new ColumnDefinitionConfig<RoleListItem, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final RoleListItem rowValue) {
                actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                MenuPopItem roleStatusChange = new MenuPopItem(rowValue.isActive() ? wfmStrings.deactivate() : wfmStrings.activate(), "icon-active");
                roleStatusChange.ensureDebugId("role_status");
                roleStatusChange.setCommand(() -> {
                    LoadingPanel.loading(true);
                    RolePermissionService.App.get().changeRoleStatus(rowValue.getObjectID(), new AbstractAsyncCallback<Void>() {
                        public void failure(Throwable throwable) {
                            LoadingPanel.loading(false);
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        }

                        public void success(Void result) {
                            LoadingPanel.loading(false);
                            Info.show(settingsStrings.yourRoleHasBeen() + " " + wfmStrings.successfully() + " " + (rowValue.isActive() ? wfmStrings.deactivated() : wfmStrings.activated()), Info.Type.INFO);
                            list.reloadPage();
                        }
                    });
                });
                if (!rowValue.getCode().equalsIgnoreCase(EdsRole.ADMIN_CODE)) {
                    actionItemCount++;
                    menuBar.addItem(roleStatusChange);
                }
                if (!rowValue.getSystem()) {
                    MenuPopItem roleEdit = new MenuPopItem(wfmStrings.edit(), "icon-task-small");
                    roleEdit.ensureDebugId("edit");
                    roleEdit.setScheduledCommand(() -> new AddRoleView(rowValue.getObjectID()));
                    if (!rowValue.getCode().equalsIgnoreCase(EdsRole.ADMIN_CODE)) {
                        actionItemCount++;
                        menuBar.addItem(roleEdit);
                    }
                    MenuPopItem roleDelete = new MenuPopItem(wfmStrings.delete(), "icon-task-small");
                    roleDelete.ensureDebugId("delete");
                    roleDelete.setScheduledCommand(() -> {
                        final WfmMessageBox message = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                        message.setTitle(wfmStrings.warning());
                        message.setMessage(wfmStrings.sureYouWantToDelete());
                        message.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                LoadingPanel.loading(true);
                                RolePermissionService.App.get().deleteRole(rowValue.getObjectID(), new AbstractAsyncCallback<Boolean>() {
                                    public void failure(Throwable throwable) {
                                        LoadingPanel.loading(false);
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    public void success(Boolean result) {
                                        LoadingPanel.loading(false);
                                        Info.show(wfmStrings.messSuccessfulyyDeleted(), Info.Type.INFO);
                                        list.reloadPage();

                                    }
                                });
                            }
                        });
                        message.open();
                    });
                    if (!rowValue.getCode().equalsIgnoreCase(EdsRole.ADMIN_CODE)) {
                        actionItemCount++;
                        menuBar.addItem(roleDelete);
                    }
                }
                if (!rowValue.getCode().equalsIgnoreCase(EdsRole.ADMIN_CODE)) {
                    ToolItem toolItem = new ToolItem(actionItemCount);
                    toolItem.setWidget(menuBar);
                    return toolItem.getAction();
                }
                return null;
            }
        };
        columns[0].setColumnSortable(false);
        columns[0].setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[0].setColumnSortable(false);

        columns[1] = new ColumnDefinitionConfig<RoleListItem, String>(wfmStrings.name(), RoleListItem.NAME, 80) {

            @Override
            public String getCellValue(RoleListItem roleListItem) {
                return roleListItem.getName();  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        columns[1].setMinimumColumnWidth(50);
        columns[1].setMaximumColumnWidth(80);

        columns[2] = new ColumnDefinitionConfig<RoleListItem, String>(wfmStrings.active(), RoleListItem.ACTIVE, 80) {

            @Override
            public String getCellValue(RoleListItem roleListItem) {
                return roleListItem.isActive() ? wfmStrings.yes() : wfmStrings.no();  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        columns[2].setMinimumColumnWidth(80);
        columns[2].setMaximumColumnWidth(100);

        columns[3] = new ColumnDefinitionConfig<RoleListItem, String>(settingsStrings.isSystem(), RoleListItem.IS_SYSTEM, 90) {

            @Override
            public String getCellValue(RoleListItem bugListItem) {
                return bugListItem.getSystem() ? wfmStrings.yes() : wfmStrings.no();  //To change body of implemented methods use File | Settings | File Templates.
            }
        };
        columns[3].setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columns[3].setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);

        return columns;
    }

    @Override
    public String getIconStyle() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
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

}
