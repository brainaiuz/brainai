package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.RolePermissionHistoryItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;


public class RolePermissionHistoryListView extends BaseListView implements Constants {
    private ListingPanel<RolePermissionHistoryItem> list;

    public RolePermissionHistoryListView() {
        super(PERMISSION_HISTORY_LIST, wfmStrings.pemissionLogs());
    }

    @Override
    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.RolePermissionHistoryListView, getColumns(), getRequestProvider(), getPanelDesigner());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_PERMISSION_HISTORY_LIST_RELOAD, RolePermissionHistoryListView.this, (sender, args) -> list.reloadPage());
        add(list);
        return null;
    }

    public CustomColumnDefinitionConfig[] getColumns() {
        ArrayList<CustomColumnDefinitionConfig> columns = new ArrayList<>();
        CustomColumnDefinitionConfig column;

        //Permission  Name
        column = new ColumnDefinitionConfig<RolePermissionHistoryItem, String>(wfmStrings.permission(), RolePermissionHistoryItem.PERMISSION_NAME, 80) {
            @Override
            public String getCellValue(RolePermissionHistoryItem rowValue) {
                return rowValue.getPermissionName() != null ? rowValue.getPermissionName() : wfmStrings.notAvailable();
            }
        };
        column.setMinimumColumnWidth(80);
        columns.add(column);

        //Role  Name
        column = new ColumnDefinitionConfig<RolePermissionHistoryItem, String>(wfmStrings.role(), RolePermissionHistoryItem.ROLE_NAME, 80) {
            @Override
            public String getCellValue(RolePermissionHistoryItem rowValue) {
                return rowValue.getRoleName() != null ? rowValue.getRoleName() : wfmStrings.notAvailable();
            }
        };
        column.setMinimumColumnWidth(80);
        columns.add(column);

        //Module  Name
        column = new ColumnDefinitionConfig<RolePermissionHistoryItem, String>(wfmStrings.section(), RolePermissionHistoryItem.MODULE_NAME, 80) {
            @Override
            public String getCellValue(RolePermissionHistoryItem rowValue) {
                return rowValue.getModuleName() != null ? rowValue.getModuleName() : wfmStrings.notAvailable();
            }
        };
        column.setMinimumColumnWidth(80);
        columns.add(column);
        //From
        column = new ColumnDefinitionConfig<RolePermissionHistoryItem, String>(wfmStrings.oldValue(), RolePermissionHistoryItem.FROM, 80) {
            @Override
            public String getCellValue(RolePermissionHistoryItem rowValue) {
                return PermissionConstants.ALLOW.equals(rowValue.getOldValue()) ? wfmStrings.yes() : wfmStrings.no();
            }
        };
        column.setMinimumColumnWidth(80);
        columns.add(column);
        //To
        column = new ColumnDefinitionConfig<RolePermissionHistoryItem, String>(wfmStrings.newValue(), RolePermissionHistoryItem.TO, 80) {
            @Override
            public String getCellValue(RolePermissionHistoryItem rowValue) {
                return PermissionConstants.ALLOW.equals(rowValue.getNewValue()) ? wfmStrings.yes() : wfmStrings.no();
            }
        };
        column.setMinimumColumnWidth(80);
        columns.add(column);

        //User
        column = new ColumnDefinitionConfig<RolePermissionHistoryItem, String>(wfmStrings.modifiedBy(), RolePermissionHistoryItem.MODIFIED_BY, 130) {
            @Override
            public String getCellValue(RolePermissionHistoryItem rowValue) {
                return rowValue.getUserName() != null ? rowValue.getUserName() : wfmStrings.notAvailable();
            }
        };
        column.setMinimumColumnWidth(80);
        columns.add(column);

        //Date
        column = new ColumnDefinitionConfig<RolePermissionHistoryItem, String>(wfmStrings.modifiedDate(), RolePermissionHistoryItem.MODIFIED_DATE, 80) {
            @Override
            public String getCellValue(RolePermissionHistoryItem rowValue) {
                return rowValue.getUpdatedDate() != null ? DateUtils.formatInternal(rowValue.getUpdatedDate()) : wfmStrings.notAvailable();
            }
        };
        column.setMinimumColumnWidth(80);
        columns.add(column);

        return columns.toArray(new CustomColumnDefinitionConfig[]{});
    }

    private ListingPanelDesign getPanelDesigner() {
        return new ListingPanelDesign() {
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
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmStrings.noUpdatesText());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private ListingRequestProvider<RolePermissionHistoryItem> getRequestProvider() {
        return (listingFilterParameter, listingCallback) -> {
            ProfileService.App.get().getPermissionLogHistoryList(listingFilterParameter, new AsyncCallback<ListResult<RolePermissionHistoryItem>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    GWT.log(throwable.getMessage());
                }

                @Override
                public void onSuccess(ListResult<RolePermissionHistoryItem> result) {
                    LoadingPanel.loading(false);
                    listingCallback.onSuccess(result);
                }
            });
        };
    }

    @Override
    public String getIconStyle() {
        return "employee employee-list";
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