package com.edatasite.workforce.gwt.core.client.ui;
//This list for all Goals List - All in One

import com.edatasite.workforce.gwt.core.client.*;
import com.edatasite.workforce.gwt.core.client.localization.HrmsStrings;
import com.edatasite.workforce.gwt.core.client.localization.WfmMessages;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.GoalItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CellChange;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
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

import java.util.ArrayList;

//This list for all Goals List - All in One

public class GoalsListView extends BaseListView implements CommandConstants, Constants {
    String iconStyle = null;
    private String type;
    private static final WfmMessages wfmMessages = WfmMessages.App.get(); //need to review all messages
    private static final HrmsStrings hrmsStrings = HrmsStrings.App.get();
    private ListingPanel<GoalItem> list;
    private Integer relatedProjectID;
    private final AllInOneServiceAsync allInOneService = AllInOneService.App.get();
    private boolean isProjectGoal, isPersonGoal, isDepartmentGoal, isBusinessGoal = false;

    public GoalsListView(String view, Integer projectID) {
        super(view);
        if (PROJECT_GOAL.equals(view)) {
            this.setDescription(Property.getPluralWithObjectCode(Constants.PROJECT_GOAL, hrmsStrings.projectgoal()));
            this.isProjectGoal = true;
            this.type = PROJECT_GOAL;
            this.relatedProjectID = projectID;
            if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_PROJECT_GOALS)) {
                setAddNew("projectgoal|add/add//" + type);
            }
        } else if (PERSONAL_GOAL.equals(view)) {
            this.setDescription(Property.getPluralWithObjectCode(Constants.PERSONAL_GOAL, hrmsStrings.personalGoals()));
            this.type = PERSONAL_GOAL;
            this.isPersonGoal = true;
            this.relatedProjectID = projectID;
            if (Utils.hasPermission(PermissionConstants.HRMS_GOAL_ADD_FROM_PROJECT)) {
                setAddNew("goal|add/add//" + type);
            }
        }
        this.isDepartmentGoal = false;
        this.isBusinessGoal = false;
    }

    public GoalsListView() {

        super("persongoals");
        setDescription(Property.getPluralWithObjectCode(Constants.PERSONAL_GOAL, hrmsStrings.personalGoals()));
        this.type = PERSONAL_GOAL;
        isPersonGoal = true;

        if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_PERSONAL_GOALS)) {
            setAddNew("goal|add/add//" + type);
        }
    }

    public GoalsListView(String view) {
        super(view);

        isDepartmentGoal = false;
        isProjectGoal = false;
        isBusinessGoal = false;

        if (DEPARTMENT_GOAL.equals(view)) {
            this.type = DEPARTMENT_GOAL;
            isDepartmentGoal = true;

            this.setDescription(hrmsStrings.departmentGoals());

            if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_DEPARTMENT_GOALS)) {
                setAddNew("departmentgoal|add/add//" + type);
            }

        } else if (PROJECT_GOAL.equals(view)) {
            this.type = PROJECT_GOAL;
            isProjectGoal = true;

            this.setDescription(hrmsStrings.projectGoals());

            if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_PROJECT_GOALS)) {
                setAddNew("projectgoal|add/add//" + type);
            }

        } else if (BUSINESS_GOAL.equals(view)) {
            this.type = BUSINESS_GOAL;
            isBusinessGoal = true;

            this.setDescription(hrmsStrings.businessGoals());

            if (Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_BUSINESS_GOALS)) {
                setAddNew("busingoal|add/add//" + type);
            }
        }
    }

    @Override
    protected Widget onInitialize() {
        list = new GuideListingPanel(isPersonGoal ? ListPanelType.GoalListPanel : isDepartmentGoal ? ListPanelType.DepartmentGoalListPanel : isProjectGoal ? ListPanelType.ProjectGoalListPanel : ListPanelType.BusinessGoalListPanel, getColumn(), getListProvider(), getListDesign());
        list.setExcelListener(clickEvent -> {
            String excelURL;
            if (isProjectGoal) {
                excelURL = CommandConstants.COMMON_URL + "/download" + type + "ListExcel";
            } else {
                excelURL = CommandConstants.COMMON_URL + "/download" + type + "goalListExcel";
            }
            ListingFilterParameter filterParametrs = list.getFilterParametrs();
            filterParametrs.setPropertyCode(getPropertyCode());
            list.callListExcel(excelURL, filterParametrs);
        });
        list.setCustomFieldsEditCellSaveChanges((CellChange<GoalItem>) (rowValue, columnCodeName) -> saveGoalEditCellValue(rowValue, columnCodeName));
        list.setPDFListener(clickEvent -> {

            String pdfURL;

            pdfURL = CommandConstants.PDF_URL + "/" + "goalListPDFHandler";
            ListingFilterParameter filterParametrs = list.getFilterParametrs();
            filterParametrs.setPropertyCode(getPropertyCode());
            if (filterParametrs == null) {
                filterParametrs = new ListingFilterParameter();
            }
            if (type != null && !"".equals(type)) {
                filterParametrs.setStatusValues(type);
            }
            filterParametrs.setAllGoals(true);
            list.callListPDF(pdfURL, filterParametrs);
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_GOAL_ADD, GoalsListView.this, (sender, args) -> list.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_GOAL_DELETE, GoalsListView.this, (sender, args) -> list.reloadPage());
        add(list);
        return null;
    }

    private ListingRequestProvider<GoalItem> getListProvider() {
        return (filterParametrs, callback) -> {
            if (isPersonGoal) {
                if (relatedProjectID != null) {
                    filterParametrs.setProjectId(relatedProjectID);
                }
                allInOneService.getPersonalGoalList(filterParametrs, new AsyncCallback<ListResult<GoalItem>>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        callback.onFailure(throwable);
                    }

                    @Override
                    public void onSuccess(ListResult<GoalItem> goalList) {
                        callback.onSuccess(goalList);
                    }
                });
            } else if (isDepartmentGoal) {
                allInOneService.getDepartmentGoalList(filterParametrs, new AsyncCallback<ListResult<GoalItem>>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        callback.onFailure(throwable);
                    }

                    @Override
                    public void onSuccess(ListResult<GoalItem> goalList) {
                        callback.onSuccess(goalList);
                    }
                });

            } else if (isBusinessGoal) {
                allInOneService.getBusinGoalList(filterParametrs, new AsyncCallback<ListResult<GoalItem>>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        callback.onFailure(throwable);
                    }

                    @Override
                    public void onSuccess(ListResult<GoalItem> goalList) {
                        callback.onSuccess(goalList);
                    }
                });

            } else {
                if (relatedProjectID != null) {
                    filterParametrs.setProjectId(relatedProjectID);
                }
                loadGoalList(filterParametrs, callback, null);
            }
        };
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        loadGoalList(new ListingFilterParameter(), null, container);
    }

    private void loadGoalList(ListingFilterParameter filterParameter, ListingCallback callback, Span container) {
        if (relatedProjectID != null) {
            filterParameter.setProjectId(relatedProjectID);
        }
        allInOneService.getRelatedGoalList(filterParameter, type, new AsyncCallback<ListResult<GoalItem>>() {
            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
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

    private ColumnDefinitionConfig[] getColumn() {

        //DEFAULT COLUMNS FOR ALL GOAL LISTS

        ArrayList<ColumnDefinitionConfig> columnDefinitionConfigs = new ArrayList<>();
        ColumnDefinitionConfig columns;
        //Action
        columns = new ColumnDefinitionConfig<GoalItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, 50) {
            @Override
            public Anchor getCellValue(GoalItem item) {
                return getGoalActions(item);
            }
        };
        columns.setColumnSortable(false);
        columns.setMinimumColumnWidth(50);
        columns.setMaximumColumnWidth(50);
        columnDefinitionConfigs.add(columns);

        //Number
        columns = new ColumnDefinitionConfig<GoalItem, SimpleLink>(wfmStrings.number(), GoalItem.GOAL_NUMBER, 60) {
            @Override
            public SimpleLink getCellValue(GoalItem item) {
                if (Utils.hasPermission(summaryPermission())) {
                    return getLink(item.getGoalNumber() != null ? item.getGoalNumber().getFirstNumberString() : "", "goal|summary/" + item.getObjectId() + "/" + type, item.getTitle());
                }
                return getLink(item.getGoalNumber() != null ? item.getGoalNumber().getFirstNumberString() : "", null);
            }

        };
        columns.setColumnSortable(true);
        columns.setMinimumColumnWidth(60);
        columns.setMaximumColumnWidth(100);
        columnDefinitionConfigs.add(columns);

        //Title
        columns = new ColumnDefinitionConfig<GoalItem, SimpleLink>(wfmStrings.title(), GoalItem.GOAL_LIST_TITLE, 130) {
            @Override
            public SimpleLink getCellValue(GoalItem item) {
                if (Utils.hasPermission(summaryPermission())) {
                    return getLink(item.getTitle(), "goal|summary/" + item.getObjectId() + "/" + type, item.getTitle());
                }
                return getLink(item.getTitle(), null);
            }

        };
        columns.setColumnSortable(true);
        columns.setMaximumColumnWidth(160);
        columns.setMinimumColumnWidth(70);
        columnDefinitionConfigs.add(columns);

        //From Date
        columns = new ColumnDefinitionConfig<GoalItem, String>(wfmStrings.startDate(), GoalItem.GOAL_LIST_FROM_DATE, 60) {
            @Override
            public String getCellValue(GoalItem item) {
                if (item.getFromDate() != null) {
                    return DateUtils.format(item.getFromDate()) + Utils.getHijriDate(item.getFromDate().getNonConvertedDate());
                }
                return "";
            }
        };
        columns.setHorizontalAlignment(HasAlignment.ALIGN_LEFT);
        columns.setColumnSortable(true);
        columns.setMinimumColumnWidth(60);
        columns.setMaximumColumnWidth(60);
        columnDefinitionConfigs.add(columns);

        //END DATE
        columns = new ColumnDefinitionConfig<GoalItem, String>(wfmStrings.endDate(), GoalItem.GOAL_LIST_TO_DATE, 60) {
            @Override
            public String getCellValue(GoalItem item) {
                if (item.getToDate() != null) {
                    return DateUtils.format(item.getToDate()) + Utils.getHijriDate(item.getToDate().getNonConvertedDate());
                }
                return "";
            }
        };
        columns.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columns.setColumnSortable(true);
        columns.setMinimumColumnWidth(60);
        columns.setMaximumColumnWidth(60);
        columnDefinitionConfigs.add(columns);

        //STATUS
        if (!isDepartmentGoal) {
            columns = new ColumnDefinitionConfig<GoalItem, String>(wfmStrings.status(), GoalItem.GOAL_STATUS, 70) {
                @Override
                public String getCellValue(GoalItem item) {
                    return item.getStatus();
                }
            };
            columns.setMinimumColumnWidth(70);
            columns.setMaximumColumnWidth(80);
            columns.setColumnSortable(true);
            columns.setShow(true);
            columnDefinitionConfigs.add(columns);
        }


        if (isBusinessGoal) {

            //Project
            columns = new ColumnDefinitionConfig<GoalItem, String>(hrmsStrings.companyGoal(), GoalItem.COMPANY_GOAL_LIST_TITLE, 70) {
                @Override
                public String getCellValue(GoalItem item) {
                    return item.getCompanyGoal();
                }
            };
            columns.setMinimumColumnWidth(70);
            columns.setMaximumColumnWidth(100);
            columns.setColumnSortable(true);
            columns.setShow(false);
            columnDefinitionConfigs.add(columns);

        } else if (isDepartmentGoal) {

            columns = new ColumnDefinitionConfig<GoalItem, String>(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.location()), GoalItem.GOAL_LIST_LOCATION, 80) {
                @Override
                public String getCellValue(GoalItem item) {
                    return item.getLocation();
                }
            };
            columns.setMinimumColumnWidth(70);
            columns.setMaximumColumnWidth(100);
            columns.setColumnSortable(true);
            columns.setShow(true);
            columnDefinitionConfigs.add(columns);


            columns = new ColumnDefinitionConfig<GoalItem, String>(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), GoalItem.GOAL_LIST_DEPARTMENT, 80) {
                @Override
                public String getCellValue(GoalItem item) {
                    return item.getDepartment();
                }
            };
            columns.setMinimumColumnWidth(70);
            columns.setMaximumColumnWidth(100);
            columns.setColumnSortable(true);
            columns.setShow(true);
            columnDefinitionConfigs.add(columns);

            //Manager
            columns = new ColumnDefinitionConfig<GoalItem, String>(wfmStrings.manager(), GoalItem.GOAL_LIST_RESOVER, 100) {
                @Override
                public String getCellValue(GoalItem item) {
                    return item.getResolver();
                }
            };
            columns.setMinimumColumnWidth(70);
            columns.setColumnSortable(true);
            columns.setShow(false);
            columnDefinitionConfigs.add(columns);


        } else if (isPersonGoal) {

            //project goal
            columns = new ColumnDefinitionConfig<GoalItem, String>(hrmsStrings.projectgoal(), GoalItem.PROJECT_GOAL, 100) {
                @Override
                public String getCellValue(GoalItem item) {
                    return item.getProjectGoalTitle();
                }

            };
            columns.setColumnSortable(true);
            columns.setMinimumColumnWidth(70);
            columns.setMaximumColumnWidth(160);
            columns.setShow(false);
            columnDefinitionConfigs.add(columns);


            //Assigned to
            columns = new ColumnDefinitionConfig<GoalItem, String>(wfmStrings.assignee(), GoalItem.GOAL_LIST_ASSIGN, 100) {
                @Override
                public String getCellValue(GoalItem item) {

                    if (item.getGoalAssignedTo() == null || "".equals(item.getGoalAssignedTo()) || "N/A".equals(item.getGoalAssignedTo())) {
                        item.setGoalAssignedTo(item.getCreatorName() != null ? item.getCreatorName() : "");
                    }

                    return item.getGoalAssignedTo();
                }
            };
            columns.setMinimumColumnWidth(70);
            columns.setMaximumColumnWidth(160);
            columns.setColumnSortable(true);
            columns.setShow(true);
            columnDefinitionConfigs.add(columns);

        }


        return columnDefinitionConfigs.toArray(new ColumnDefinitionConfig[0]);
    }

    private String summaryPermission() {
        String permission = null;
        if (isPersonGoal) {
            permission = PermissionConstants.HRMS_PERSONAL_GOAL_SUMMARY;
        } else if (isDepartmentGoal) {
            permission = PermissionConstants.HRMS_DEPARTMENT_GOAL_SUMMARY;
        } else if (isProjectGoal) {
            permission = PermissionConstants.HRMS_PROJECT_GOAL_SUMMARY;
        } else if (isBusinessGoal) {
            permission = PermissionConstants.HRMS_BUSINESS_GOAL_SUMMARY;
        }
        return permission;
    }

    private Anchor getGoalActions(final GoalItem item) {
        String goalSummaryPermission = PermissionConstants.HRMS_PERSONAL_GOAL_SUMMARY;
        String editGoalPermission = PermissionConstants.HRMS_EDIT_PERSONAL_GOAL;
        String removeGoalPermission = PermissionConstants.HRMS_PERSONAL_GOAL_REMOVE;
        if (isDepartmentGoal) {
            goalSummaryPermission = PermissionConstants.HRMS_DEPARTMENT_GOAL_SUMMARY;
            editGoalPermission = PermissionConstants.HRMS_EDIT_DEPARTMENT_GOAL;
            removeGoalPermission = PermissionConstants.HRMS_DEPARTMENT_GOAL_REMOVE;
        } else if (isProjectGoal) {
            goalSummaryPermission = PermissionConstants.HRMS_PROJECT_GOAL_SUMMARY;
            editGoalPermission = PermissionConstants.HRMS_EDIT_PROJECT_GOAL;
            removeGoalPermission = PermissionConstants.HRMS_PROJECT_GOAL_REMOVE;
        } else if (isBusinessGoal) {
            goalSummaryPermission = PermissionConstants.HRMS_BUSINESS_GOAL_SUMMARY;
            editGoalPermission = PermissionConstants.HRMS_EDIT_BUSINESS_GOAL;
            removeGoalPermission = PermissionConstants.HRMS_BUSINESS_GOAL_REMOVE;
        }
        int actionItemCount = 0;
        MenuBar menuBar = new MenuBar(true);

        if (Utils.hasPermission(goalSummaryPermission)) {
            MenuPopItem goalSummary = new MenuPopItem(wfmStrings.summaryView(), iconStyle);
            goalSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("goal|summary/" + item.getObjectId() + "/" + type, item.getTitle()));
            actionItemCount++;
            menuBar.addItem(goalSummary);
            goalSummary.getElement().setId(goalSummaryPermission);
        }

        MenuPopItem goalEdit = new MenuPopItem(wfmStrings.edit(), "icon-edit");
        goalEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("goaledit|editgoal/" + item.getObjectId() + "/" + type, item.getTitle()));
        if (isPersonGoal) {
            if ((item.getCreatorId() != null && Utils.getUserID().equals(item.getCreatorId())) || Utils.hasRoles(Constants.ADMIN, Constants.DR, Constants.HR)) {
                actionItemCount++;
                menuBar.addItem(goalEdit);
            }
        }

        if (isDepartmentGoal && Utils.hasPermission(editGoalPermission)) {
            actionItemCount++;
            menuBar.addItem(goalEdit);
        }
        if (isProjectGoal && Utils.hasPermission(editGoalPermission)) {
            actionItemCount++;
            menuBar.addItem(goalEdit);
        }
        if (isBusinessGoal && Utils.hasPermission(editGoalPermission)) {
            actionItemCount++;
            menuBar.addItem(goalEdit);
        }
        goalEdit.getElement().setId(editGoalPermission);


        MenuPopItem removeItem = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
        removeItem.setCommand(() -> {
            final WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
            wfmMessageBox.setTitle(wfmStrings.warning());
            wfmMessageBox.setMessage(wfmStrings.sureYouWantToDelete());
            wfmMessageBox.addCloseHandler(new CloseHandler() {
                @Override
                public void onSubmit() {
                    LoadingPanel.loading(true);
                    allInOneService.deleteGoal(item.getObjectId(), type, new AbstractAsyncCallback<Void>() {
                        @Override
                        public void failure(Throwable throwable) {
                            LoadingPanel.loading(false);
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        }

                        @Override
                        public void success(Void result) {
                            LoadingPanel.loading(false);
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_GOAL_DELETE, result, GoalsListView.this);
                            Info.show(property.getSingular(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.goal()), Info.Type.INFO);
                            list.reloadPage();
                        }
                    });
                }
            });

            wfmMessageBox.open();
        });

        // we are changing logics in deparmtent goal, apprasials currently not requrered
        if (!isDepartmentGoal) {
            MenuPopItem apprasilsItem = new MenuPopItem(wfmStrings.appraisal(), "icon-edit");
            apprasilsItem.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("initiate|add/add/" + item.getAssigneeId()));
            menuBar.addItem(apprasilsItem);
        }

        if (isPersonGoal) {
            if (item.getCreatorId() != null && Utils.getUserID().equals(item.getCreatorId())) {
                if (Utils.hasPermission(removeGoalPermission)) {
                    actionItemCount++;
                    menuBar.addItem(removeItem);
                }
            }
        }
        if (isDepartmentGoal && Utils.hasPermission(removeGoalPermission)) {
            actionItemCount++;
            menuBar.addItem(removeItem);
        }
        if (isProjectGoal) {
            if (Utils.hasPermission(removeGoalPermission)) {
                actionItemCount++;
                menuBar.addItem(removeItem);
            }
        }
        if (isBusinessGoal && Utils.hasPermission(removeGoalPermission)) {
            actionItemCount++;
            menuBar.addItem(removeItem);
        }
        removeItem.getElement().setId(removeGoalPermission);

        ToolItem toolItem = new ToolItem(actionItemCount);
        toolItem.setWidget(menuBar);
        return toolItem.getAction();
    }

    private GuideListingPanelDesign getListDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                if (isPersonGoal && Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_PERSONAL_GOALS)) {
                    return () -> addNewPersonGoal();
                } else if (isDepartmentGoal && Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_DEPARTMENT_GOALS)) {
                    return () -> addNewDepartmentGoal();
                } else if (isProjectGoal && Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_PROJECT_GOALS)) {
                    return () -> addNewProjectGoal();
                } else if (isBusinessGoal && Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_BUSINESS_GOALS)) {
                    return () -> addNewBusineesGoal();
                }
                return null;
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
                if (isPersonGoal && Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_PERSONAL_GOALS)) {
                    ActionButton addGoal = getAddNewButton();
                    addGoal.addClickHandler(event -> addNewPersonGoal());
                    addGoal.getElement().setId("add_personal_goal");
                    return addGoal;

                }

                if (isDepartmentGoal && Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_DEPARTMENT_GOALS)) {
                    ActionButton addDepartament = getAddNewButton();
                    addDepartament.addClickHandler(event -> addNewDepartmentGoal());
                    addDepartament.getElement().setId("add_departament_goal");
                    return addDepartament;
                }

                if (isProjectGoal && Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_PROJECT_GOALS)/* && (Utils.hasRole(DR) || Utils.hasRole(ADMIN) || Utils.hasRole(PM) || Utils.hasRole(ADMIN_LOCATION) || Utils.hasRole(HR))*/) {
                    ActionButton addProject = getAddNewButton();
                    addProject.addClickHandler(event -> addNewProjectGoal());
                    addProject.getElement().setId("add_project_goal");
                    return addProject;
                }
                if (isBusinessGoal && Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_BUSINESS_GOALS)) {
                    ActionButton addBusinGoal = getAddNewButton();
                    addBusinGoal.addClickHandler(event -> addNewBusineesGoal());
                    addBusinGoal.getElement().setId("add_businGoal");
                    return addBusinGoal;
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
                String linkName = "";
                String goalType = "";
                String addNewGoal = "";
                String permissionType = "";
                if (isPersonGoal) {
                    linkName = "goal|add/add//" + type;
                    goalType = wfmStrings.typePersonalGoal();
                    addNewGoal = hrmsStrings.personalGoals().toLowerCase();
                    permissionType = PermissionConstants.HRMS_ADD_NEW_PERSONAL_GOALS;
                } else if (isDepartmentGoal) {
                    linkName = "departmentgoal|add/add//" + type;
                    goalType = Property.get(Constants.DEPARTMENT_LIST, hrmsStrings.departmentGoals(), wfmStrings.department());
                    addNewGoal = Property.get(Constants.DEPARTMENT_LIST, hrmsStrings.departmentGoal(), wfmStrings.department().toLowerCase());
                    permissionType = PermissionConstants.HRMS_ADD_NEW_DEPARTMENT_GOALS;
                } else if (isProjectGoal) {
                    linkName = "projectgoal|add/add//" + type;
                    goalType = Property.get(Constants.PROJECT_GOAL, hrmsStrings.projectgoal());
                    addNewGoal = Property.get(Constants.PROJECT_GOAL, hrmsStrings.projectgoal());
                    permissionType = PermissionConstants.HRMS_ADD_NEW_PROJECT_GOALS;
                } else if (isBusinessGoal) {
                    linkName = "busingoal|add/add//" + type;
                    goalType = hrmsStrings.businessGoals();
                    addNewGoal = hrmsStrings.businessGoals().toLowerCase();
                    permissionType = PermissionConstants.HRMS_ADD_NEW_BUSINESS_GOALS;
                }

                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmMessages.currentlyThereAreNo(goalType) + " ");
                if (PERSONAL_GOAL.equals(type) || Utils.hasPermission(permissionType)) {
                    message.setTextBeforeLink(wfmMessages.youCanStartRegistering(addNewGoal));

                    message.setHref(linkName);
                }
                emptyDataTable.initEmptyDataTable(message);
            }

//            public void initDataEmptyTable(HTML emptyTable) {
//                emptyTable.setText("");
//                emptyTable.setStyleName("drawColumns");
//                emptyTable.addClickHandler(clickEvent -> {
//                });
//
//            }

            @Override
            public boolean isEditCustomFieldCell() {
                String editGoalPermission = PermissionConstants.HRMS_PERSONAL_GOAL_SUMMARY;
                if (isDepartmentGoal) {
                    editGoalPermission = PermissionConstants.HRMS_EDIT_DEPARTMENT_GOAL;
                } else if (isProjectGoal) {
                    editGoalPermission = PermissionConstants.HRMS_EDIT_PROJECT_GOAL;
                } else if (isBusinessGoal) {
                    editGoalPermission = PermissionConstants.HRMS_EDIT_BUSINESS_GOAL;
                }
                return (Utils.adminOrDirector() || Utils.hasRole(HR) || Utils.hasPermission(editGoalPermission));

            }

            @Override
            public boolean isShowCustomiseButton() {
                String permission = "";
                if (isPersonGoal) {
                    permission = PermissionConstants.HRMS_PERSONAL_GOAL_CUSTOMIZE_BUTTON;
                } else if (isDepartmentGoal) {
                    permission = PermissionConstants.HRMS_DEPARTMENT_GOAL_CUSTOMIZE_BUTTON;
                } else if (isProjectGoal) {
                    permission = PermissionConstants.HRMS_PROJECT_GOAL_CUSTOMIZE_BUTTON;
                } else if (isBusinessGoal) {
                    permission = PermissionConstants.HRMS_BUSINESS_GOAL_CUSTOMIZE_BUTTON;
                }
                return Utils.hasPermission(permission);
            }
        };
    }

    private SinksContainer addNewBusineesGoal() {
        return SinksContainerFactory.entryPoint.onHistoryChanged("busingoal|add/add//" + type);
    }

    private SinksContainer addNewProjectGoal() {
        if (relatedProjectID != null) {
            return SinksContainerFactory.entryPoint.onHistoryChanged("projectgoal|add/add/true/" + relatedProjectID + "/" + type);
        } else {
            return SinksContainerFactory.entryPoint.onHistoryChanged("projectgoal|add/add//" + type);
        }
    }

    private SinksContainer addNewDepartmentGoal() {
        return SinksContainerFactory.entryPoint.onHistoryChanged("departmentgoal|add/add//" + type);
    }

    private SinksContainer addNewPersonGoal() {
        return SinksContainerFactory.entryPoint.onHistoryChanged("goal|add/add//" + type);
    }

    public String getIconStyle() {
        if (isPersonGoal) {
            return "hrms personal-goal";
        }
        if (isDepartmentGoal) {
            return "hrms department-goal";
        }
        if (isProjectGoal) {
            return "hrms project-goal";
        }
        if (isBusinessGoal) {
            return "hrms business-goal";
        }
        return null;
    }

    private void saveGoalEditCellValue(GoalItem rowValue, String columnCodeName) {
        allInOneService.saveGoalEditCellValue(rowValue, columnCodeName, new AbstractAsyncCallback<Void>() {
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
        return isPersonGoal ? PERSONAL_GOAL + GOAL : isDepartmentGoal ? DEPARTMENT_GOAL  : isProjectGoal ? PROJECT_GOAL_LIST_VIEW : BUSINESS_GOAL + GOAL;
    }

}
