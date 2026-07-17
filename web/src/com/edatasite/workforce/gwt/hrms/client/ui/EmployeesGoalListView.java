package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.GoalItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
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
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ExportImportOption;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.GuideListingPanelDesign;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListPanelType;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingCallback;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingEmptyDataInitializer;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingFacetFilter;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingPanel;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ListingRequestProvider;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HasAlignment;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.html.Span;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: Nov 12, 2009
 * Time: 11:53:20 AM
 * To change this template use File | Settings | File Templates.
 */
public class EmployeesGoalListView extends BaseListView implements CommandConstants, Constants {

    private static final WfmStrings wfmStrings = WfmStrings.App.get();
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private final HrmsServiceAsync hrmsService = HrmsService.App.get();
    private String type;
    private Integer employeeId;
    private Integer relatedProjectGoalID;
    private boolean isAllByProjectGoal = false;

    public EmployeesGoalListView(Integer employeeId) {
        super("employeegoal");
        setDescription(property.getPlural(hrmsStrings.personalGoals()));
        this.type = PERSONAL_GOAL;
        this.employeeId = employeeId;
        if (Utils.hasPermission(PermissionConstants.HRMS_GOAL_ADD_FROM_PROJECT)) {
            setAddNew("goal|add/add//" + PERSONAL_GOAL);
        }
    }

    public EmployeesGoalListView(Integer employeeId, Integer relatedProjectGoalID, Boolean isAllByProjectGoal) {
        super("employeegoal");
        setDescription(property.getPlural(hrmsStrings.personalGoals()));
        this.employeeId = employeeId;
        this.type = PERSONAL_GOAL;
        this.relatedProjectGoalID = relatedProjectGoalID;
        this.isAllByProjectGoal = isAllByProjectGoal;
        if (Utils.hasPermission(PermissionConstants.HRMS_GOAL_ADD_FROM_PROJECT)) {
            setAddNew("goal|add/add//" + PERSONAL_GOAL);
        }
    }

    private ListingPanel<GoalItem> list;

    protected Widget onInitialize() {
        list = new GuideListingPanel(ListPanelType.EmployeesGoalListPanel, drawColumns(), getProvider(), getDesigner());

        list.setExcelListener(clickEvent -> {
            String excelURL;
            excelURL = CommandConstants.COMMON_URL + "/downloademployeesgoalListExcel";
            ListingFilterParameter filterParametrs = list.getFilterParametrs();
            filterParametrs.setPropertyCode(getPropertyCode());
            filterParametrs.setAllGoals(false);
            list.callListExcel(excelURL, filterParametrs);
        });

        list.setPDFListener(clickEvent -> {
            String pdfURL;

            pdfURL = CommandConstants.PDF_URL + "/" + "goalListPDFHandler";
            ListingFilterParameter filterParametrs = list.getFilterParametrs();
            filterParametrs.setPropertyCode(getPropertyCode());
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            filterParametrs.setAllGoals(false);
            list.callListPDF(pdfURL, filterParametrs);
        });


        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_GOAL_ADD, EmployeesGoalListView.this, (sender, args) -> list.reloadPage());
        add(list);
        return null;
    }


    private CustomColumnDefinitionConfig[] drawColumns() {
        final ColumnDefinitionConfig[] columns = new ColumnDefinitionConfig[10];
        int index = 0;

        columns[index] = new ColumnDefinitionConfig<GoalItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final GoalItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                if (Utils.hasPermission(PermissionConstants.HRMS_PERSONAL_GOAL_SUMMARY)) {
                    MenuPopItem goalSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-company-goal-small");
                    goalSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("goal|summary/" + item.getObjectId() + "/" + type, item.getTitle()));
                    actionItemCount++;
                    menuBar.addItem(goalSummary);
                }


                if (Utils.hasPermission(PermissionConstants.HRMS_EDIT_PERSONAL_GOAL)) {
                    MenuPopItem goalEdit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
                    goalEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("goaledit|editgoal/" + item.getObjectId() + "/" + type, item.getTitle()));
                    actionItemCount++;
                    menuBar.addItem(goalEdit);
                }

                if (Utils.hasPermission(PermissionConstants.HRMS_PERSONAL_GOAL_REMOVE)) {
                    MenuPopItem removeItem = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    removeItem.setCommand(() -> {
                        final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        message.setTitle(wfmStrings.warning());
                        message.setMessage(wfmStrings.sureYouWantToDelete());
                        message.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                LoadingPanel.loading(true);
                                hrmsService.deleteGoal(item.getObjectId(), type, new AbstractAsyncCallback<Void>() {
                                    public void failure(Throwable throwable) {
                                        LoadingPanel.loading(false);
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    public void success(Void result) {
                                        LoadingPanel.loading(false);
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_GOAL_DELETE, result, EmployeesGoalListView.this);
                                        Info.show(hrmsStrings.yourGoalHasBeenDeleted(), Info.Type.INFO);
                                        list.reloadPage();
                                    }
                                });
                            }
                        });
                        message.open();
                    });
                    actionItemCount++;
                    menuBar.addItem(removeItem);
                }

                final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columns[index].setColumnSortable(false);
        columns[index].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns[index++].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);

        columns[index] = new ColumnDefinitionConfig<GoalItem, SimpleLink>(wfmStrings.code(), GoalItem.GOAL_NUMBER, 70) {
            @Override
            public SimpleLink getCellValue(GoalItem item) {
                if (Utils.hasPermission(PermissionConstants.HRMS_PERSONAL_GOAL_SUMMARY)) {
                    return getLink(item.getGoalNumber() != null ? item.getGoalNumber().getFirstNumberString() : "", "goal|summary/" + item.getObjectId() + "/" + type, item.getTitle());
                }
                return getLink(item.getTitle(), null);
            }

        };
        columns[index].setColumnSortable(true);
        columns[index++].setMinimumColumnWidth(70);

        columns[index] = new ColumnDefinitionConfig<Object, String>(wfmStrings.goalCategory(), GoalItem.EMPLOYEE_GOAL_LIST_GOAL_CATEGORY, 100) {
            @Override
            public String getCellValue(Object object) {
                GoalItem item = (GoalItem) object;
                return item.getGoalCategory();

            }

        };
        columns[index++].setMinimumColumnWidth(70);

        columns[index] = new ColumnDefinitionConfig<GoalItem, String>(wfmStrings.project(), GoalItem.PROJECT_GOAL, 100) {
            @Override
            public String getCellValue(GoalItem item) {
                return item.getProjectGoalTitle();
            }

        };
        columns[index++].setMinimumColumnWidth(70);

        columns[index] = new ColumnDefinitionConfig<Object, String>(wfmStrings.status(), GoalItem.EMPLOYEE_GOAL_LIST_STATUS, 80) {
            @Override
            public String getCellValue(Object object) {
                GoalItem item = (GoalItem) object;
                return item.getStatus();
            }
        };
        columns[index].setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columns[index++].setMinimumColumnWidth(70);
        columns[index] = new ColumnDefinitionConfig<Object, Widget>(wfmStrings.title(), GoalItem.EMPLOYEE_GOAL_LIST_TITLE, 120) {
            @Override
            public Widget getCellValue(Object object) {
                GoalItem item = (GoalItem) object;
                if ("Department".equals(item.getGoalCategory())) {
                    type = DEPARTMENT_GOAL;
                }

                if (Utils.hasPermission(PermissionConstants.HRMS_PERSONAL_GOAL_SUMMARY)) {
                    return getLink(item.getTitle(), "goal|summary/" + item.getObjectId() + "/" + type);
                }
                return getLink(item.getTitle(), null);
            }
        };
        columns[index++].setMinimumColumnWidth(70);
        columns[index] = new ColumnDefinitionConfig<Object, String>(wfmStrings.description(), GoalItem.EMPLOYEE_GOAL_LIST_DESCRIPTION, 120) {
            @Override
            public String getCellValue(Object object) {
                GoalItem item = (GoalItem) object;

                return item.getDescription();
            }
        };
        columns[index++].setMinimumColumnWidth(70);
        columns[index] = new ColumnDefinitionConfig<Object, String>(wfmStrings.score(), GoalItem.EMPLOYEE_GOAL_LIST_WEIGHT, 80) {
            @Override

            public String getCellValue(Object object) {
                GoalItem item = (GoalItem) object;
                return item.getWeight() + "";
            }
        };
        columns[index++].setMinimumColumnWidth(70);
        columns[index] = new ColumnDefinitionConfig<Object, String>(wfmStrings.actionSteps(), GoalItem.EMPLOYEE_GOAL_LIST_ACTIONSTEPS, 100) {

            @Override
            public String getCellValue(Object object) {
                GoalItem item = (GoalItem) object;
                return item.getActionSteps();
            }
        };
        columns[index++].setMinimumColumnWidth(70);
        columns[index] = new ColumnDefinitionConfig<Object, String>(wfmStrings.resolver(), GoalItem.EMPLOYEE_GOAL_LIST_RESOLVER, 120) {

            @Override
            public String getCellValue(Object object) {
                GoalItem item = (GoalItem) object;
                return item.getResolver();
            }
        };
        columns[index].setMinimumColumnWidth(70);
        columns[index++].setShow(false);
        return columns;
    }

    private ListingRequestProvider<GoalItem> getProvider() {
        return (filterParametrs, callback) -> {
            loadEmployeeGoals(filterParametrs, callback, null);
        };
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        loadEmployeeGoals(new ListingFilterParameter(), null, container);
    }

    private void loadEmployeeGoals(ListingFilterParameter filterParameter, ListingCallback callback, Span container) {
        if (filterParameter == null) {
            filterParameter = new ListingFilterParameter();
        }
        if (relatedProjectGoalID != null && isAllByProjectGoal) {
            filterParameter.setRelationID(relatedProjectGoalID);
            filterParameter.setAllByProjectGoal(isAllByProjectGoal);
        }
        filterParameter.setEmployeeId(employeeId);
        filterParameter.setAllGoals(true);
        HrmsService.App.get().getOwnGoalList(filterParameter, new AsyncCallback<ListResult<GoalItem>>() {
            public void onFailure(Throwable throwable) {
                if (callback != null) {
                    callback.onFailure(throwable);
                }
            }

            public void onSuccess(ListResult<GoalItem> goalList) {
                if (callback != null) {
                    callback.onSuccess(goalList);
                }

                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (goalList.getTotal() != null && goalList.getTotal() > 0) {
                        statisticShortcut.setText(countFormat(goalList.getTotal()));
                        statisticShortcut.setClass("tab-label");
                    } else {
                        statisticShortcut.setText("");
                        statisticShortcut.removeStyleName("tab-label");
                    }
                }
            }
        });
    }

    private GuideListingPanelDesign getDesigner() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return () -> {
                    if (isAllByProjectGoal && relatedProjectGoalID != null) {
                        if (Utils.hasPermission(PermissionConstants.HRMS_GOAL_ADD_FROM_PROJECT)) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("goal|add/add/true/" + relatedProjectGoalID + "/" + PERSONAL_GOAL);
                        } else {
                            Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                        }
                    } else {
                        SinksContainerFactory.entryPoint.onHistoryChanged("goal|add/add//" + PERSONAL_GOAL);
                    }
                };
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
                if (Utils.hasPermission(PermissionConstants.HRMS_GOAL_ADD_FROM_PROJECT)) {
                    ActionButton addNews = getAddNewButton();
                    addNews.addClickHandler(event -> {
                        if (isAllByProjectGoal && relatedProjectGoalID != null) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("goal|add/add/true/" + relatedProjectGoalID + "/" + PERSONAL_GOAL);
                        } else {
                            SinksContainerFactory.entryPoint.onHistoryChanged("goal|add/add//" + PERSONAL_GOAL);
                        }
                    });
                    return addNews;
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
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(hrmsStrings.currentlyPersonalProject());
                String permission = null;
                if ((employeeId == null && employeeId.equals(Utils.getUserID())) || type.equals(PERSONAL_GOAL)) {
                    permission = PermissionConstants.HRMS_GOAL_ADD_FROM_PROJECT;
                } else if (type.equals(PROJECT_GOAL)) {
                    permission = PermissionConstants.HRMS_ADD_NEW_PROJECT_GOALS;
                } else if (type.equals(BUSINESS_GOAL)) {
                    permission = PermissionConstants.HRMS_ADD_NEW_BUSINESS_GOALS;
                } else if (type.equals(DEPARTMENT_GOAL)) {
                    permission = PermissionConstants.HRMS_ADD_NEW_DEPARTMENT_GOALS;
                } else if (type.equals(COMPANY_GOAL)) {
                    permission = PermissionConstants.HRMS_ADD_NEW_COMPANY_GOALS;
                }
                if (Utils.hasPermission(permission)) {
                    message.setTextBeforeLink(hrmsStrings.youCanStartRegisteringPersonal());
                    message.setHref("goal|add/add//" + type);
                    emptyDataTable.initEmptyDataTable(message);
                }
            }
        };
    }

    public String getIconStyle() {
        return "hrms employees-goal-list";
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

    public String getPropertyCode() {
        return PERSONAL_GOAL + GOAL;
    }
}
