package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.ReasonItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
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

import java.util.ArrayList;
import java.util.List;

/**
 * @author Hurshid on 12/15/2018
 */
public class LeaveReasonListView extends BaseListView {

    protected static final SettingStrings settingsStrings = SettingStrings.App.get();
    protected static final AllInOneServiceAsync allInOneService = AllInOneService.App.get();
    protected ListingPanel<ReasonItem> listing;

    public LeaveReasonListView() {
        super("leaveReasonList", wfmStrings.leaveReasons());
    }

    protected Widget onInitialize() {
        listing = new ListingPanel<>(ListPanelType.LeaveReasonListPanel, getColumns(), getListData(), getDesign());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_LEAVE_REASON_UPDATE, LeaveReasonListView.this, (sender, args) -> listing.reloadPage());
        add(listing);
        return null;
    }

    protected ColumnDefinitionConfig[] getColumns() {
        List<ColumnDefinitionConfig> columnsConfigList = new ArrayList<>();

        ColumnDefinitionConfig column = new ColumnDefinitionConfig<ReasonItem, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final ReasonItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                if (Utils.hasPermission(PermissionConstants.REFERENCE_EDIT)) {
                    MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                    edit.getElement().setId("edit");
                    edit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("leaveReason|edit/" + item.getId() + "/false", item.getName(), item.getName()));
                    actionItemCount++;
                    menuBar.addItem(edit);
                }

                if (Utils.hasPermission(PermissionConstants.REFERENCE_DELETE) && !item.isSystemReference()) {
                    MenuPopItem deletePage = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    deletePage.getElement().setId("Leave_reason_delete_button");
                    deletePage.setCommand(() -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.confirmation());
                        messageBox.setMessage(wfmMessages.sureYouWantToDelete(item.getName(), "?"));
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                LoadingPanel.loading(true);
                                allInOneService.deleteReason(item.getId(), new AbstractAsyncCallback<Void>() {
                                    @Override
                                    public void failure(Throwable throwable) {
                                        LoadingPanel.loading(false);
                                        Info.warn(wfmStrings.youCannotDeleteThisItem());
                                    }

                                    @Override
                                    public void success(Void result) {
                                        LoadingPanel.loading(false);
                                        listing.reloadPage();
                                    }
                                });
                            }
                        });
                        messageBox.open();
                    });
                    menuBar.addItem(deletePage);
                }
                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        column.setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        column.setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        column.setColumnSortable(false);
        columnsConfigList.add(column);

        column = new ColumnDefinitionConfig<ReasonItem, SimpleLink>(wfmStrings.name(), ReasonItem.NAME, 100) {
            @Override
            public SimpleLink getCellValue(ReasonItem item) {
                return getLink(item.getName(), "leaveReason|edit/" + item.getId() + "/false");
            }
        };
        column.setMinimumColumnWidth(50);
        columnsConfigList.add(column);

        column = new ColumnDefinitionConfig<ReasonItem, String>(wfmStrings.description(), ReasonItem.DESCRIPTION, 150) {
            @Override
            public String getCellValue(ReasonItem item) {
                return item.getDescription();
            }
        };
        column.setMinimumColumnWidth(100);
        columnsConfigList.add(column);

        column = new ColumnDefinitionConfig<ReasonItem, String>(wfmStrings.active(), "active", 50) {
            @Override
            public String getCellValue(ReasonItem item) {
                return item.isActive() ? wfmStrings.yes() : wfmStrings.no();
            }
        };
        column.setMinimumColumnWidth(30);
        column.setColumnSortable(false);
        columnsConfigList.add(column);

        column = new ColumnDefinitionConfig<ReasonItem, String>(wfmStrings.shortName(), "shortName", 50) {
            @Override
            public String getCellValue(ReasonItem item) {
                return item.getShortName() != null ? item.getShortName() : wfmStrings.notAvailable();
            }
        };
        column.setMinimumColumnWidth(30);
        columnsConfigList.add(column);

        column = new ColumnDefinitionConfig<ReasonItem, String>(settingsStrings.quickAddLR(), "attendanceLR", 50) {
            @Override
            public String getCellValue(ReasonItem item) {
                return item.isAttendanceLR() ? wfmStrings.yes() : wfmStrings.no();
            }
        };
        column.setMinimumColumnWidth(30);
        columnsConfigList.add(column);

        return columnsConfigList.toArray(new ColumnDefinitionConfig[]{});
    }

    protected ListingRequestProvider<ReasonItem> getListData() {
        return (filterParameter, callback) -> {
            allInOneService.getReasonItems(filterParameter, new AbstractAsyncCallback<ListResult<ReasonItem>>() {
                public void failure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                public void success(ListResult<ReasonItem> result) {
                    callback.onSuccess(result);
                }
            });
        };
    }

    protected ListingPanelDesign getDesign() {
        return new ListingPanelDesign() {
            @Override
            public ListingFacetFilter initFacetFilter() {
                return null;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (Utils.hasPermission(PermissionConstants.REFERENCE_ADD)) {
                    ActionButton newProjectItem = getAddNewButton();
                    newProjectItem.addClickHandler(event -> {
                        SinksContainerFactory.entryPoint.onHistoryChanged("leaveReason|leaveReasonadd/add");
                    });
                    return newProjectItem;
                } else {
                    return null;
                }
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
            }

            @Override
            public boolean isShowCustomiseButton() {
                return true;
            }

            @Override
            public boolean isShowResetButton() {
                return true;
            }
        };

    }

    @Override
    public String getIconStyle() {
        return null;
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
