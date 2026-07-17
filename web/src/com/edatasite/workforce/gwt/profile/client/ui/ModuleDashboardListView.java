package com.edatasite.workforce.gwt.profile.client.ui;

import com.edatasite.workforce.gwt.accounting.client.localization.AccountingStrings;
import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.ModuleEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.ModuleDashboardService;
import com.edatasite.workforce.gwt.core.client.rpc.ModuleDashboardServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.ModuleDashboardListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.edatasite.workforce.gwt.profile.client.ui.quickadd.ModuleDashboardQuickAddView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;

import java.util.ArrayList;

/**
 * User: Abror Abdukadirov
 * Date: 10.04.2018 14:55
 */
public class ModuleDashboardListView extends BaseListView {

    private final ModuleDashboardServiceAsync moduleDashboardServiceAsync = ModuleDashboardService.App.get();
    private static final AccountingStrings accountingStrings = AccountingStrings.App.get();
    private ListingPanel<ModuleDashboardListItem> list;
    private final SettingStrings settingsStrings = SettingStrings.App.get();

    public ModuleDashboardListView() {
        super("moduleDashboard", accountingStrings.dashboardsList());
    }

    @Override
    protected Widget onInitialize() {
        list = new ListingPanel<>(ListPanelType.ModuleDashboardPanel, getColumnConfigs(), getRequestProvider(), getDesign());

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_MODULE_DASHBOARD_ADD, ModuleDashboardListView.this, (sender, args) -> list.reloadPage());

        add(list);
        return null;
    }

    private CustomColumnDefinitionConfig[] getColumnConfigs() {
        ArrayList<CustomColumnDefinitionConfig> columns = new ArrayList<>();
        CustomColumnDefinitionConfig column;
        //Action
        column = new ColumnDefinitionConfig<ModuleDashboardListItem, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final ModuleDashboardListItem rowValue) {
                int count = 0;
                final MenuBar menuBar = new MenuBar(true);
                menuBar.setAutoOpen(true);
                //Summary
                MenuPopItem customize = new MenuPopItem(wfmStrings.customize());
                customize.ensureDebugId("Dashboard_customize");
                customize.setScheduledCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("moduleDashboard|summary/" + rowValue.getObjectId(), rowValue.getName()));
                count++;
                menuBar.addItem(customize);
                //Edit
                if (Utils.hasPermission(PermissionConstants.SETTINGS_DASHBOARD_EDIT)) {
                    MenuPopItem edit = new MenuPopItem(wfmStrings.edit());
                    edit.ensureDebugId("Dashboard_edit");
                    edit.setScheduledCommand(() -> new ModuleDashboardQuickAddView(rowValue.getObjectId()));
                    count++;
                    menuBar.addItem(edit);
                }
                //Localization
                MenuPopItem localization = new MenuPopItem(wfmStrings.localization());
                localization.ensureDebugId("Dashboard_localization");
                localization.setScheduledCommand(() -> new LocalizationCFModal(rowValue.getObjectId(), LocalizationTypeEnum.DASHBOARD).center());
                count++;
                menuBar.addItem(localization);

                //Delete
                if (!rowValue.isSystem() && Utils.hasPermission(PermissionConstants.SETTINGS_DASHBOARD_DELETE)) {
                    MenuPopItem delete = new MenuPopItem(wfmStrings.delete());
                    delete.ensureDebugId("Dashboard_delete");
                    delete.setScheduledCommand(() -> {
                        WfmMessageBox message = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                        message.setTitle(wfmStrings.warning());
                        message.setMessage(wfmStrings.sureYouWantToDelete());
                        message.addCloseHandler(event -> {
                            LoadingPanel.loading(true);
                            moduleDashboardServiceAsync.deleteModuleDashboardItem(rowValue.getObjectId(), new AbstractAsyncCallback<Integer>() {
                                @Override
                                public void onFailure(Throwable caught) {
                                    LoadingPanel.loading(false);
                                    Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                }

                                @Override
                                public void onSuccess(Integer result) {
                                    LoadingPanel.loading(false);

                                    if (result != null && result == 1) {
                                        Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.item()), Info.Type.INFO);
                                        list.reloadPage();
                                    } else {
                                        Info.show("Dashboard not found", Info.Type.WARNING);
                                    }
                                }
                            });
                        });
                        message.open();

                    });
                    count++;
                    menuBar.addItem(delete);
                }

                ToolItem toolItem = new ToolItem(count);
                toolItem.setWidget(menuBar);

                return toolItem.getAction();
            }
        };
        column.setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        column.setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        column.setColumnSortable(false);
        columns.add(column);

        column = new ColumnDefinitionConfig<ModuleDashboardListItem, SimpleLink>(wfmStrings.name(), ModuleDashboardListItem.DASHBOARD_NAME, 250) {
            @Override
            public SimpleLink getCellValue(ModuleDashboardListItem rowValue) {
                return getLink(rowValue.getName(), "moduleDashboard|summary/" + rowValue.getObjectId());
            }
        };
        column.setMinimumColumnWidth(200);
        columns.add(column);

        column = new ColumnDefinitionConfig<ModuleDashboardListItem, String>(wfmStrings.apps(), ModuleDashboardListItem.MODULE, 80) {
            @Override
            public String getCellValue(ModuleDashboardListItem rowValue) {
                return localizeModuleName(rowValue.getModule());
            }
        };
        column.setMinimumColumnWidth(60);
        columns.add(column);

        column = new ColumnDefinitionConfig<ModuleDashboardListItem, String>(wfmStrings.active(), ModuleDashboardListItem.IS_ACTIVE, 40) {
            @Override
            public String getCellValue(ModuleDashboardListItem rowValue) {
                return rowValue.isActive() ? wfmStrings.yes() : wfmStrings.no();
            }
        };
        column.setMinimumColumnWidth(20);
        columns.add(column);

        column = new ColumnDefinitionConfig<ModuleDashboardListItem, String>(wfmStrings.default2(), ModuleDashboardListItem.IS_DEFAULT, 40) {
            @Override
            public String getCellValue(ModuleDashboardListItem rowValue) {
                return rowValue.isDefault() ? wfmStrings.yes() : wfmStrings.no();
            }
        };
        column.setMinimumColumnWidth(20);
        columns.add(column);

        column = new ColumnDefinitionConfig<ModuleDashboardListItem, String>(settingsStrings.isSystem(), ModuleDashboardListItem.IS_SYSTEM, 40) {
            @Override
            public String getCellValue(ModuleDashboardListItem rowValue) {
                return rowValue.isSystem() ? wfmStrings.yes() : wfmStrings.no();
            }
        };
        column.setMinimumColumnWidth(20);
        columns.add(column);

        column = new ColumnDefinitionConfig<ModuleDashboardListItem, String>(wfmStrings.createdBy(), ModuleDashboardListItem.CREATOR, 90) {
            @Override
            public String getCellValue(ModuleDashboardListItem rowValue) {
                return rowValue.getCreator() != null ? rowValue.getCreator().getName() : "N/A";
            }
        };
        column.setMinimumColumnWidth(50);
        columns.add(column);

        column = new ColumnDefinitionConfig<ModuleDashboardListItem, String>(wfmStrings.createdDate(), ModuleDashboardListItem.CREATION_DATE, 80) {
            @Override
            public String getCellValue(ModuleDashboardListItem rowValue) {
                return DateUtils.format(rowValue.getCreationDate());
            }
        };
        column.setMinimumColumnWidth(50);
        columns.add(column);

        column = new ColumnDefinitionConfig<ModuleDashboardListItem, String>(wfmStrings.modifiedBy(), ModuleDashboardListItem.UPDATOR, 90) {
            @Override
            public String getCellValue(ModuleDashboardListItem rowValue) {
                return rowValue.getUpdator() != null ? rowValue.getUpdator().getName() : "N/A";
            }
        };
        column.setMinimumColumnWidth(50);
        columns.add(column);

        column = new ColumnDefinitionConfig<ModuleDashboardListItem, String>(wfmStrings.modifiedDate(), ModuleDashboardListItem.UPDATED_DATE, 80) {
            @Override
            public String getCellValue(ModuleDashboardListItem rowValue) {
                return DateUtils.format(rowValue.getUpdatedDate());
            }
        };
        column.setMinimumColumnWidth(50);
        columns.add(column);

        return columns.toArray(new CustomColumnDefinitionConfig[]{});
    }

    private ListingRequestProvider<ModuleDashboardListItem> getRequestProvider() {
        return new ListingRequestProvider<ModuleDashboardListItem>() {
            @Override
            public void getRequest(ListingFilterParameter fp, ListingCallback<ModuleDashboardListItem> callback) {
                moduleDashboardServiceAsync.getModuleDashboardList(fp, new AbstractAsyncCallback<ListResult<ModuleDashboardListItem>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        super.onFailure(caught);
                    }

                    @Override
                    public void onSuccess(ListResult<ModuleDashboardListItem> result) {
                        callback.onSuccess(result);
                    }
                });
            }
        };
    }

    private ListingPanelDesign getDesign() {
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
            public ActionButton initTopToolBarNew() {
                if (Utils.hasPermission(PermissionConstants.SETTINGS_DASHBOARD_ADD)) {
                    ActionButton newItem = getAddNewButton();
                    newItem.addClickHandler(event -> {
                        new ModuleDashboardQuickAddView(null);
                    });
                    return newItem;
                } else {
                    return null;
                }
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {

            }
        };
    }

    private String localizeModuleName(ModuleEnum moduleEnum) {
        if (moduleEnum != null) {
            switch (moduleEnum) {
                case PM:
                    return wfmStrings.projects();
                case HRMS:
                    return wfmStrings.hrms();
                case ACCOUNTING:
                    return wfmStrings.accounts();
                case CRM:
                    return wfmStrings.crm();
                case PAYROLL:
                    return wfmStrings.payroll();
                case MYWORKSPACE:
                    return wfmStrings.myWorkspace();

            }
        }
        return "N/A";
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    @Override
    public void asyncOnInitialize(AsyncCallback<Widget> callback) {
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
