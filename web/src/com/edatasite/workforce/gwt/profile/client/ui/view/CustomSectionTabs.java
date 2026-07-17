package com.edatasite.workforce.gwt.profile.client.ui.view;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.PermissionItem;
import com.edatasite.workforce.gwt.core.client.rpc.RoleListItem;
import com.edatasite.workforce.gwt.core.client.rpc.RolePermissionService;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.cell.CheckBoxCell;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.customtabbar.CustomTabWidget;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.*;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.profile.client.localization.SettingStrings;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Fatkhulla
 * Date: 24.05.12
 * Time: 17:00
 * To change this template use File | Settings | File Templates.
 */
public class CustomSectionTabs extends CustomTabWidget implements Constants, IFooteredView {

    public static final ProvidesKey<PermissionItem> KEY_PROVIDER = item -> item == null ? null : item.getObjectId();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final SettingStrings settingsStrings = SettingStrings.App.get();
    protected ListDataProvider<PermissionItem> dataProvider = null;
    private final List<RoleListItem> moduleRoleList;
    private List<RoleListItem> roleList;
    private final String sectionContext;
    private ScrolledGrid<PermissionItem> cellTable = null;
    private final FlexTable flexTable;
    private final KpiModal modal;
    private List<Widget> leftWidgets = new ArrayList<>();
    private List<Widget> rightWidgets = new ArrayList<>();

    public CustomSectionTabs(String context, String tabName, List<RoleListItem> moduleRoleList) {
        super(tabName);
        this.moduleRoleList = moduleRoleList;
        this.sectionContext = context;

        modal = new KpiModal();
        modal.setTitle(settingsStrings.availableRoles());
        flexTable = new FlexTable();
        modal.add(flexTable);

        WfmButton2 save = new WfmButton2(wfmStrings.save());
        save.addClickHandler(click -> saveRoleSettings());
        WfmButton2 cancel = new WfmButton2(wfmStrings.cancel());
        cancel.addClickHandler(click -> modal.close());
        modal.addButton(cancel);
        modal.addButton(save);
    }

    @Override
    public void initData() {

    }

    @Override
    public void viewShow() {
        dataProvider = new ListDataProvider<>();
        cellTable = new ScrolledGrid<>(5000, KEY_PROVIDER);
        cellTable.setWidth("100%");

        int tableHeight = Utils.getViewportSize().height - 230;
        cellTable.setHeight(tableHeight + "px");

        cellTable.addStyleName("cellBasedWidget-mod");
        cellTable.setEmptyTableWidget(DefaultNoItemsMessage.getNoItemsMessage(wfmStrings.dataLoading(), "", null));

        addDataDisplay(cellTable);
        initsializationStructure();
        loading();

        FlowPanel mainContainer = new FlowPanel();
        mainContainer.setWidth("100%");
        mainContainer.getElement().getStyle().setProperty("display", "flex");
        mainContainer.getElement().getStyle().setProperty("flexDirection", "column");
        mainContainer.getElement().getStyle().setProperty("justifyContent", "spaceBetween");

        int mainDockHeight = Utils.getViewportSize().height - 160;
        mainContainer.setHeight(mainDockHeight + "px");

        mainContainer.add(cellTable);

        ViewFooter footer = new ViewFooter(this);
        mainContainer.add(footer);

        this.clear();
        super.add(mainContainer);

        Scheduler.get().scheduleDeferred(() -> {
            if (cellTable != null) {
                cellTable.onResize();
                cellTable.redraw();
            }
        });
    }

    private void loading() {
        LoadingPanel.loading(true);
        ListingFilterParameter parameter = new ListingFilterParameter();
        parameter.setSection(sectionContext);
        parameter.setAllByFilter(Utils.isSuperUser());
        if (PermissionConstants.REPORTING.equals(sectionContext)) {
            RolePermissionService.App.get().getReportingPermissionList(parameter, new AsyncCallback<ArrayList<PermissionItem>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(ArrayList<PermissionItem> newList) {

                    PermissionItem[] list = new PermissionItem[newList.size()];
                    int h = 0;
                    for (PermissionItem item : newList) {
                        for (RoleListItem roleItem:moduleRoleList) {
                            item.setRole(roleItem.getObjectID(), item.hasRole(roleItem.getObjectID()));
                        }
                        list[h++] = item;
                    }

                    List<PermissionItem> provider = dataProvider.getList();
                    provider.clear();
                    Collections.addAll(provider, list);

                    LoadingPanel.loading(false);
                }
            });
            return;
        }

        RolePermissionService.App.get().getPermissionListByContext(parameter, new AsyncCallback<ListResult<PermissionItem>>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(ListResult<PermissionItem> permissionItems) {
                List<PermissionItem> list = dataProvider.getList();
                list.clear();
                Collections.addAll(list, permissionItems.getList().toArray(new PermissionItem[]{}));
                LoadingPanel.loading(false);
            }
        });
    }

    public void refresh() {
        loading();
    }

    public void addDataDisplay(HasData<PermissionItem> display) {
        dataProvider.addDataDisplay(display);
    }

    private void initsializationStructure() {
        int recursionDepth = PermissionConstants.REPORTING.equals(sectionContext) ? 3 : 2;
        Column<PermissionItem, SafeHtml> firstNameColumn = new Column<PermissionItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(PermissionItem permissionItem) {
                SafeHtmlBuilder htmlBuilder = new SafeHtmlBuilder();
                htmlBuilder.appendHtmlConstant(permissionItem.getRightChar());
//                if (permissionItem.getObjectId() == -1) return null;
                if (permissionItem.getRightChar() != null) {
                    if (permissionItem.getRightCharCount() <= recursionDepth) {
                        htmlBuilder.appendHtmlConstant("<b>");
                        htmlBuilder.appendEscaped(permissionItem.getName() == null ? wfmStrings.selectAll() : permissionItem.getName());
                        htmlBuilder.appendHtmlConstant("</b>");

                    } else {
                        htmlBuilder.appendEscaped(permissionItem.getName());       ////////     permission name
                    }
                } else {
                    htmlBuilder.appendEscaped(permissionItem.getName());
                }
                return htmlBuilder.toSafeHtml();
            }
        };
        cellTable.addColumn(firstNameColumn, "");
        cellTable.setColumnWidth(firstNameColumn, 250, Style.Unit.PX);

        for (int i = 0; i < moduleRoleList.size(); i++) {
            final RoleListItem roleItem = moduleRoleList.get(i);
            Column<PermissionItem, Boolean> check = new Column<PermissionItem, Boolean>(new CheckBoxCell()) {
                @Override
                public Boolean getValue(PermissionItem permissionItem) {
                    return permissionItem.hasRole(roleItem.getObjectID());
                }
            };
            final SafeHtmlBuilder sb = new SafeHtmlBuilder();
            sb.appendHtmlConstant("<a style=\"color:black;\" title=\"" + roleItem.getName() + "\">" + roleItem.getName() + "</a>");

            cellTable.addColumn(check, sb.toSafeHtml());
            cellTable.setColumnWidth(check, 50, Style.Unit.PX);


            check.setFieldUpdater((_index, permissionItem, value) -> {
                LoadingPanel.loading(true);

                if (permissionItem.getName() == null) {
                    RolePermissionService.App.get().saveRolePermissions(roleItem.getObjectID(), value, sectionContext, new AsyncCallback<Boolean>() {
                        @Override
                        public void onFailure(Throwable throwable) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void onSuccess(Boolean aBoolean) {
                            LoadingPanel.loading(false);
                            if (PermissionConstants.REPORTING.equals(sectionContext)) {
                                refresh();
                            }
                            List<PermissionItem> provider = dataProvider.getList();
                            PermissionItem[] list = new PermissionItem[provider.size()];

                            int h = 0;
                            for (PermissionItem item : provider) {
                                item.setRole(roleItem.getObjectID(), value);
                                list[h++] = item;
                            }
                            provider.clear();
                            Collections.addAll(provider, list);

                            dataProvider.refresh();
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PERMISSION_HISTORY_LIST_RELOAD, null, CustomSectionTabs.this);
                        }
                    });
                } else {
                    // if clicked select all button need write logic it
                    if (PermissionConstants.REPORTING.equals(sectionContext)) {
//                        List<PermissionItem> provider = dataProvider.getList();
                            RolePermissionService.App.get().updateReportingPermission(roleItem, value, permissionItem, new AsyncCallback<ArrayList<PermissionItem>>() {
                                @Override
                                public void onFailure(Throwable throwable) {
                                    String fail = "Method Failed";
                                    LoadingPanel.loading(false);
                                }
                                @Override
                                public void onSuccess(ArrayList<PermissionItem> newList) {
                                    LoadingPanel.loading(false);
                                    PermissionItem[] list = new PermissionItem[newList.size()];

                                    int h = 0;
                                    for (PermissionItem item : newList) {
                                        item.setRole(roleItem.getObjectID(), item.hasRole(roleItem.getObjectID()));
                                        if (item.hasRole(roleItem.getObjectID())) {
                                            GWT.log(item.getName() + " -:- " + roleItem.getName());
                                        }
                                        list[h++] = item;
                                    }
                                    dataProvider = new ListDataProvider<>();
                                    Collections.addAll(dataProvider.getList(), list);
                                    dataProvider.refresh();
                                    addDataDisplay(cellTable);
                                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PERMISSION_HISTORY_LIST_RELOAD, null, CustomSectionTabs.this);
                                }
                            });
                    } else {
                        RolePermissionService.App.get().saveRolePermission(sectionContext, permissionItem.getObjectId() + "_" + roleItem.getObjectID(), value, new AsyncCallback<Boolean>() {
                            @Override
                            public void onFailure(Throwable throwable) {
                                LoadingPanel.loading(false);
                            }

                            @Override
                            public void onSuccess(Boolean result) {
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PERMISSION_HISTORY_LIST_RELOAD, null, CustomSectionTabs.this);
                                LoadingPanel.loading(false);
                            }
                        });
                    }
                }
            });
        }
    }

    private void saveRoleSettings() {
        moduleRoleList.clear();

        HashMap<String, String> map = new HashMap<>();
        int count = 0;
        for (int i = 0; i < flexTable.getRowCount(); i++) {
            for (int j = 0; j < flexTable.getRowFormatter().getElement(i).getChildCount(); j++) {
                Widget widget = flexTable.getWidget(i, j);
                if (widget instanceof KpiCheckBox) {
                    KpiCheckBox checkBox2 = (KpiCheckBox) widget;
                    RoleListItem roleListItem = (RoleListItem) checkBox2.getLayoutData();

                    roleListItem.getModuleCode().remove(sectionContext);
                    if (checkBox2.getValue()) {
                        count++;
                        if (count > 5) {
                            Info.warn(wfmStrings.youCannotSelectMoreThanItem());
                            return;
                        }
                        moduleRoleList.add(roleListItem);
                        roleListItem.getModuleCode().add(sectionContext);
                    }
                    map.put(roleListItem.getCode(), String.join(",", roleListItem.getModuleCode()));
                }
            }
        }

        LoadingPanel.loading(true);
        RolePermissionService.App.get().saveRoleSettings(map, new AsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable throwable) {
                LoadingPanel.loading(false);
            }

            @Override
            public void onSuccess(Boolean b) {
                modal.close();
                clear();
                LoadingPanel.loading(false);
                viewShow();
            }
        });
    }

    public void setRoleList(List<RoleListItem> roleList) {
        this.roleList = roleList;
    }

    @Override
    public List<Widget> getFooterLeftSideWidgets() {
        leftWidgets.clear();

        return leftWidgets;
    }

    @Override
    public List<Widget> getFooterRightSideWidgets() {
        rightWidgets.clear();

        WfmButton2 selectRolesBtn = new WfmButton2(wfmStrings.selectRoles(), BTN_PRIMARY);
        selectRolesBtn.addClickHandler(event -> {
            handleShowRolesAction();
        });

        WfmButton2 resetBtn = new WfmButton2(wfmStrings.resetPermission(), BTN_REJECT);
        resetBtn.addClickHandler(event -> {
            handleResetAction();
        });

        rightWidgets.add(resetBtn);
        rightWidgets.add(selectRolesBtn);

        return rightWidgets;
    }

    private void handleResetAction() {
        final WfmMessageBox message = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
        message.setTitle(wfmStrings.warning());
        message.setMessage(settingsStrings.areYouSureDeletePermissions());
        message.open();
        message.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                LoadingPanel.loading(true);
                RolePermissionService.App.get().resetRolePermissions(sectionContext, new AsyncCallback<Boolean>() {
                    @Override
                    public void onFailure(Throwable throwable) { LoadingPanel.loading(false); }
                    @Override
                    public void onSuccess(Boolean b) {
                        clear();
                        LoadingPanel.loading(false);
                        viewShow();
                    }
                });
            }
        });
    }

    private void handleShowRolesAction() {
        flexTable.clear();
        if (roleList != null && !roleList.isEmpty()) {
            int row = 0;
            int i = 0;
            int s = roleList.size();

            for (RoleListItem listItem : roleList) {
                KpiCheckBox checkBox = new KpiCheckBox();
                checkBox.setText(listItem.getName());
                checkBox.setLayoutData(listItem);

                checkBox.setValue(moduleRoleList.contains(listItem));
                flexTable.setWidget(row, i, checkBox);
                if (s % 3 == 0) {
                    row++;
                    if (row == s / 3) {
                        row = 0;
                        i++;
                    }
                } else {
                    row++;
                    if (row == s / 3 + 1) {
                        row = 0;
                        i++;
                    }
                }
            }
        }
        modal.open();
    }
}
