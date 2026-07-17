package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.SinksContainer;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.hrms.client.localization.HrmsMessages;
import com.edatasite.workforce.gwt.hrms.client.rpc.GroupGoalITem;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.ArrayList;
//GROUP GOALS
public class GroupGoalListView extends BaseListView implements CommandConstants, Constants {
    private static final HrmsMessages hrmsMessages = HrmsMessages.App.get();
    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private final HrmsServiceAsync hrmsService = HrmsService.App.get();
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();

    private ListingPanel<GroupGoalITem> list;

    public GroupGoalListView() {
        super("groupgoals");
        setDescription(property.getPlural(hrmsStrings.groupGoals()));
        setAddNew("groupgoal|add/add/");
    }


    @Override
    protected Widget onInitialize() {
        list = new GuideListingPanel(ListPanelType.GroupGoalListPanel, getColumn(), getListProvider(), getListDesign());
        list.setExcelListener(clickEvent -> {
            String excelURL;
            excelURL = CommandConstants.COMMON_URL + "/downloadGroupGoalListExcel";
            ListingFilterParameter filterParametrs = list.getFilterParametrs();
            filterParametrs.setPropertyCode(getPropertyCode());
            filterParametrs.setAllGoals(true);
            list.callListExcel(excelURL, filterParametrs);
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_GROUP_GOAL_ADD, GroupGoalListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_GROUP_GOAL_REMOVED, GroupGoalListView.this, (sender, args) -> list.reloadPage());
        add(list);
        return null;
    }

    private ListingRequestProvider<GroupGoalITem> getListProvider() {
        return (filterParametrs, callback) -> {
            hrmsService.getGroupGoalList(filterParametrs, new AsyncCallback<ListResult<GroupGoalITem>>() {
                @Override
                public void onFailure(Throwable throwable) {
                    callback.onFailure(throwable);
                }

                @Override
                public void onSuccess(ListResult<GroupGoalITem> goalList) {
                    callback.onSuccess(goalList);
                }
            });
        };
    }

    private ColumnDefinitionConfig[] getColumn() {

        ArrayList<ColumnDefinitionConfig> columnConfigs = new ArrayList<>();

        ColumnDefinitionConfig columnConfig = new ColumnDefinitionConfig<GroupGoalITem, Anchor>(wfmStrings.action(), Constants.LISTING_ACTION.COLUMN_CODE, Constants.LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(GroupGoalITem item) {
                return getActions(item);
            }
        };
        columnConfig.setColumnSortable(false);
        columnConfig.setMinimumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columnConfig.setMaximumColumnWidth(Constants.LISTING_ACTION.COLUMN_WIDTH);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<GroupGoalITem, SimpleLink>(wfmStrings.employee(), GroupGoalITem.GROUP_GOAL_EMPLOYEE, 140) {
            @Override
            public SimpleLink getCellValue(GroupGoalITem item) {
                return getLink(item.getEmployee().getName(), "groupgoal|summary/" + item.getObjectId(), item.getEmployee().getName());
            }

        };
        columnConfig.setMinimumColumnWidth(70);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<GroupGoalITem, String>(wfmStrings.approver(), GroupGoalITem.GROUP_GOAL_APPROVER, 100) {
            @Override
            public String getCellValue(GroupGoalITem item) {

                return item.getApprover().getName();
            }

        };
        columnConfig.setMinimumColumnWidth(70);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<GroupGoalITem, String>(wfmStrings.status(), GroupGoalITem.GROUP_GOAL_STATUS, 100) {
            @Override
            public String getCellValue(GroupGoalITem item) {

                return item.getStatus().getName();
            }

        };
        columnConfig.setMinimumColumnWidth(70);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<GroupGoalITem, String>(wfmStrings.validityPeriod(), GroupGoalITem.VALIDITY_PERIOD, 100) {
            @Override
            public String getCellValue(GroupGoalITem item) {

                return item.getValidityPeriod() != null ? item.getValidityPeriod().getName() : "";
            }

        };
        columnConfig.setMinimumColumnWidth(70);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<GroupGoalITem, String>(wfmStrings.startDate(), GroupGoalITem.FROM_DATE, 100) {
            @Override
            public String getCellValue(GroupGoalITem item) {

                return item.getFromDate() != null ? DateUtils.format(item.getFromDate()) : "";
            }

        };
        columnConfig.setMinimumColumnWidth(70);
        columnConfigs.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<GroupGoalITem, String>(wfmStrings.endDate(), GroupGoalITem.TO_DATE, 100) {
            @Override
            public String getCellValue(GroupGoalITem item) {

                return item.getToDate() != null ? DateUtils.format(item.getToDate()) : "";
            }

        };
        columnConfig.setMinimumColumnWidth(70);
        columnConfigs.add(columnConfig);

        return columnConfigs.toArray(new ColumnDefinitionConfig[]{});
    }

    private Anchor getActions(final GroupGoalITem item) {
        int actionItemCount = 0;
        MenuBar menuBar = new MenuBar(true);

        if (Utils.hasPermission(PermissionConstants.HRMS_GROUP_PERSONAL_GOAL_SUMMARY)) {
            MenuPopItem summary = new MenuPopItem(wfmStrings.summaryView(), "icon-personal-goal-small");
            summary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("groupgoal|summary/" + item.getObjectId(), item.getEmployee().getName()));
            actionItemCount++;
            menuBar.addItem(summary);
            summary.getElement().setId(PermissionConstants.HRMS_GROUP_PERSONAL_GOAL_SUMMARY);
        }

        if (Utils.hasPermission(PermissionConstants.HRMS_EDIT_GROUP_PERSONAL_GOAL)) {
            MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
            edit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("groupgoal|add/add/" + item.getObjectId(), item.getEmployee().getName()));
            actionItemCount++;
            menuBar.addItem(edit);
            edit.getElement().setId(PermissionConstants.HRMS_EDIT_GROUP_PERSONAL_GOAL);
        }

        if (Utils.hasPermission(PermissionConstants.HRMS_GROUP_PERSONAL_GOAL_REMOVE)) {
            MenuPopItem removeItem = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
            removeItem.setCommand(() -> {
                final WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                wfmMessageBox.setTitle(wfmStrings.warning());
                wfmMessageBox.setMessage(wfmStrings.sureYouWantToDelete() );
                wfmMessageBox.addCloseHandler(new CloseHandler() {
                    @Override
                    public void onSubmit() {
                        LoadingPanel.loading(true);
                        hrmsService.deleteGroupGoals(item.getObjectId(), new AbstractAsyncCallback<Void>() {
                            @Override
                            public void failure(Throwable throwable) {
                                LoadingPanel.loading(false);
                                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                            }

                            @Override
                            public void success(Void result) {
                                LoadingPanel.loading(false);
                                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_GROUP_GOAL_REMOVED, result, GroupGoalListView.this);
                                Info.show(hrmsStrings.yourGoalHasBeenDeleted(), Info.Type.INFO);
                                list.reloadPage();
                            }
                        });
                    }
                });

                wfmMessageBox.open();
            });
            actionItemCount++;
            menuBar.addItem(removeItem);
            removeItem.getElement().setId(PermissionConstants.HRMS_GROUP_PERSONAL_GOAL_REMOVE);
        }

        ToolItem toolItem = new ToolItem(actionItemCount);
        toolItem.setWidget(menuBar);
        return toolItem.getAction();
    }

    private GuideListingPanelDesign getListDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_GROUP_PERSONAL_GOALS) ? GroupGoalListView.this::addNewLink : null;
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
            public ActionButton initTopToolBarNew() {
                if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_GROUP_PERSONAL_GOALS)) {
                    ActionButton add = getAddNewButton();
                    add.addClickHandler(event -> addNewLink());
                    add.getElement().setId("add_personal_goal");
                    return add;

                }
                return null;

            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null, true);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                String linkName = "groupgoal|add/add/";
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(hrmsMessages.currentlyThereAreNo(hrmsStrings.groupGoal()) + " ");
                if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_GROUP_PERSONAL_GOALS)) {
                    message.setTextBeforeLink(hrmsMessages.youCanStartRegistering(hrmsStrings.groupGoal()));
                    message.setHref(linkName);
                }
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return true;

            }

            @Override
            public boolean isShowCustomiseButton() {
                return Utils.hasPermission(PermissionConstants.HRMS_GROUP_GOAL_CUSTOMIZE_BUTTON);
            }

        };
    }

    private SinksContainer addNewLink() {
        return SinksContainerFactory.entryPoint.onHistoryChanged("groupgoal|add/add/");
    }

    public String getIconStyle() {
        return "hrms personal-goal";
    }

    @Override
    public void asyncOnInitialize(final AsyncCallback<Widget> callback) {
        GWT.runAsync(new RunAsyncCallback() {

            @Override
            public void onFailure(Throwable caught) {
                callback.onFailure(caught);
            }

            @Override
            public void onSuccess() {
                callback.onSuccess(onInitialize());
            }
        });
    }

    public String getPropertyCode() {
        return GROUP_GOAL;
    }
}
