package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.DepartmentGoalEmployeeHistoryItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.hrms.client.DepartmentGoalDataLogPopUp;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;

public class DepartmentGoalEmployeeMetricHistoryListView extends BaseListView implements CommandConstants, Constants {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();

    private final Integer departmentGoalId;
    private ListingPanel<DepartmentGoalEmployeeHistoryItem> list;

    public DepartmentGoalEmployeeMetricHistoryListView(Integer departmentGoalId) {
        super("employeeMetricsList");
        this.departmentGoalId = departmentGoalId;
        setDescription(wfmStrings.history());
    }

    @Override
    public String getIconStyle() {
        return "hrms department-goal";
    }

    @Override
    protected Widget onInitialize() {
        list = new GuideListingPanel(ListPanelType.EmployeeMetricsPanel, drawColumns(), getProvider(), getDesigner());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_DEPARTMETN_GOAL_METRIC_HISTORY_ADD, DepartmentGoalEmployeeMetricHistoryListView.this, (sender, args) -> list.reloadPage());
        add(list);
        return null;
    }

    private CustomColumnDefinitionConfig[] drawColumns() {
        final ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[5];

        // ACTIONS COLUMN
        columns[0] = new ColumnDefinitionConfig<DepartmentGoalEmployeeHistoryItem, Anchor>(
                wfmStrings.action(),
                LISTING_ACTION.COLUMN_CODE, 40) {

            @Override
            public Anchor getCellValue(final DepartmentGoalEmployeeHistoryItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                // EDIT
                MenuPopItem edit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                edit.setCommand(() -> new DepartmentGoalDataLogPopUp(item.getDepartmentGoalId(), item.getId(), null));
                actionItemCount++;
                menuBar.addItem(edit);

                // DELETE
                MenuPopItem delete = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                delete.setCommand(() -> {
                    final WfmMessageBox box = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                    box.setTitle(wfmStrings.warning());
                    box.setMessage(wfmStrings.sureYouWantToDelete());

                    box.addCloseHandler(new CloseHandler() {
                        @Override
                        public void onSubmit() {
                            LoadingPanel.loading(true);
                            HrmsService.App.get().deleteDepartmentGoalLogData(item.getId(),
                                    new AbstractAsyncCallback<Void>() {
                                        @Override
                                        public void failure(Throwable caught) {
                                            LoadingPanel.loading(false);
                                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                        }

                                        @Override
                                        public void success(Void result) {
                                            LoadingPanel.loading(false);
                                            Info.show(Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(),
                                                    wfmStrings.information()), Info.Type.INFO);
                                            list.reloadPage();
                                        }
                                    });
                        }
                    });
                    box.open();
                });

                actionItemCount++;
                menuBar.addItem(delete);

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns[0].setColumnSortable(false);
        columns[0].setMinimumColumnWidth(30);
        columns[0].setMaximumColumnWidth(50);

        // EMPLOYEE
        columns[1] = new ColumnDefinitionConfig<DepartmentGoalEmployeeHistoryItem, String>(
                wfmStrings.employee(), DepartmentGoalEmployeeHistoryItem.EMPLOYEE, 100) {
            @Override
            public String getCellValue(DepartmentGoalEmployeeHistoryItem item) {
                return item.getEmployee();
            }
        };

        columns[1].setMinimumColumnWidth(100);
        columns[1].setMaximumColumnWidth(120);

        // ACTUAL
        columns[2] = new ColumnDefinitionConfig<DepartmentGoalEmployeeHistoryItem, String>(
                wfmStrings.actual(), DepartmentGoalEmployeeHistoryItem.ACTUAL, 80) {
            @Override
            public String getCellValue(DepartmentGoalEmployeeHistoryItem item) {
                return String.valueOf(item.getActual());
            }
        };

        columns[2].setColumnSortable(false);
        columns[2].setMinimumColumnWidth(80);
        columns[2].setMaximumColumnWidth(100);

        // COMMENT
        columns[3] = new ColumnDefinitionConfig<DepartmentGoalEmployeeHistoryItem, String>(
                wfmStrings.comment(), DepartmentGoalEmployeeHistoryItem.COMMENT, 130) {
            @Override
            public String getCellValue(DepartmentGoalEmployeeHistoryItem item) {
                return item.getComment();
            }
        };
        columns[3].setColumnSortable(false);
        columns[3].setMinimumColumnWidth(130);
        columns[3].setMaximumColumnWidth(150);

        // DATE
        columns[4] = new ColumnDefinitionConfig<DepartmentGoalEmployeeHistoryItem, String>(
                wfmStrings.date(), DepartmentGoalEmployeeHistoryItem.DATE, 60) {
            @Override
            public String getCellValue(DepartmentGoalEmployeeHistoryItem item) {
                return item.getDate() != null ? DateUtils.format(item.getDate()) : "";
            }
        };
        columns[4].setMinimumColumnWidth(60);
        columns[4].setMaximumColumnWidth(80);

        // CREATION DATE
        columns[5] = new ColumnDefinitionConfig<DepartmentGoalEmployeeHistoryItem, String>(
                wfmStrings.createdDate(), DepartmentGoalEmployeeHistoryItem.CREATION_DATE, 60) {
            @Override
            public String getCellValue(DepartmentGoalEmployeeHistoryItem item) {
                return item.getCreationDate() != null ? DateUtils.format(item.getCreationDate()) : "";
            }
        };
        columns[5].setMinimumColumnWidth(50);
        columns[5].setMaximumColumnWidth(80);

        return columns;
    }

    private GuideListingPanelDesign getDesigner() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return null;
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return null;
                    }

                    @Override
                    public long initSimpleFilterType() {
                        return -1;
                    }

                    @Override
                    public ArrayList<String> getCustomFacetFilterFields() {
//                        ArrayList<String> fields = new ArrayList<>();
//                        fields.add(ListingChooseFilter.EMPLOYEES);
                        return null;
                    }

                    @Override
                    public ViewName getView() {
                        return ViewName.EmployeeMetrics;
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmStrings.currentlyYouDoNotHaveAnyData());
                emptyDataTable.initEmptyDataTable(message);
            }
        };

    }

    private ListingRequestProvider<DepartmentGoalEmployeeHistoryItem> getProvider() {
        return (fp, callback) -> loadEmployeeMetrics(fp, callback, null);
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        loadEmployeeMetrics(new ListingFilterParameter(), null, container);
    }

    private void loadEmployeeMetrics(ListingFilterParameter fp, ListingCallback callback, Span container) {
        if (fp == null) fp = new ListingFilterParameter();
        fp.setObjectId(departmentGoalId);
        HrmsService.App.get().getDepartmentGoalEmployeeMetricHistory(fp, new AbstractAsyncCallback<ListResult<DepartmentGoalEmployeeHistoryItem>>() {
            @Override
            public void failure(Throwable throwable) {
                if (callback != null) {
                    callback.onFailure(throwable);
                }
            }

            @Override
            public void success(ListResult<DepartmentGoalEmployeeHistoryItem> result) {
                if (callback != null) {
                    callback.onSuccess(result);
                }
                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (result.getTotal() != null && result.getTotal() > 0) {
                        statisticShortcut.setText(countFormat(result.getTotal()));
                        statisticShortcut.setClass("tab-label");
                    } else {
                        statisticShortcut.setText("");
                        statisticShortcut.removeStyleName("tab-label");
                    }
                }
            }
        });
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
        return "employeeMetricsList";
    }
}
