package com.edatasite.workforce.gwt.issue.client.ui;

import com.edatasite.workforce.gwt.core.client.*;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.landing.HelpPanelGenerator;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.filter.ListingChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.shortcut.ShortcutItem;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.issue.client.rpc.IssueListItem;
import com.edatasite.workforce.gwt.issue.client.rpc.IssueService;
import com.edatasite.workforce.gwt.issue.client.rpc.IssueServiceAsync;
import com.edatasite.workforce.gwt.issue.client.ui.quickadd.IssueQuickAddForm;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class IssueListView extends BaseListView implements Constants {
    public static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private static final IssueServiceAsync issueService = IssueService.App.get();

    private int actionItemCount = 0;
    private ListingPanel<IssueListItem> listPanel;
    protected HashSet selectedItems = new HashSet();
    protected ContextMenu actions;

    private String relationName;
    private Integer relationID;
    private String relationType;
    private final boolean hasDeleteAccess = Utils.hasPermission(PermissionConstants.PM_ISSUE_REMOVE);

    public IssueListView() {
        super(ISSUE_LIST);
        setDescription(property.getPlural(wfmStrings.issues()));
        if (hasPermissionToAdd()) {
            setAddNew(() -> getQuickAddView().show());
        }
    }

    private boolean hasPermissionToAdd() {
        return Utils.hasPermission(PermissionConstants.PM_ISSUE_ADD);
    }

    public IssueListView(Integer relationID, String relationType) {
        this();
        this.relationID = relationID;
        this.relationType = relationType;
    }

    @Override
    public String getIconStyle() {
        return "issues issue-list";
    }

    @Override
    public FlowPanel getHelpContainer() {
        if (helpPanel == null) {
            helpPanel = HelpPanelGenerator.getHelpPanel(PermissionConstants.PM_CONTEXT, PermissionConstants.PM_ISSUE_LIST);
        }
        return helpPanel;
    }

    @Override
    protected Widget onInitialize() {

        if (!Utils.hasRole(CLIENT)) {
            listPanel = new GuideListingPanel(ListPanelType.IssuesListPanel, getColumnConfig(), getListProvider(), getListDesign(),
                    SelectionGrid.SelectionPolicy.CHECKBOX);
        } else {
            listPanel = new GuideListingPanel(ListPanelType.IssuesListPanel, getColumnConfig(), getListProvider(), getListDesign());
        }
        listPanel.setCustomFieldsEditCellSaveChanges((rowValue, columnCodeName) -> saveIssueEditCellValue((IssueListItem) rowValue, columnCodeName));
        listPanel.setPDFListener(clickEvent -> {
            String pdfURL = CommandConstants.PDF_URL + "/issueListPDFHandler";
            ListingFilterParameter fp = listPanel.getFilterParametrs();
            fp.setPropertyCode(getPropertyCode());
            listPanel.callListPDF(pdfURL, fp);


        });
        listPanel.setExcelListener(clickEvent -> {
            String excelURL = CommandConstants.COMMON_URL + "/downloadIssueListExcel";
            ListingFilterParameter fp = listPanel.getFilterParametrs();
            fp.setPropertyCode("issue");
            listPanel.callListExcel(excelURL, fp);
        });
        listPanel.addSelectionRowHandler(selectedRows -> selectedItems = selectedRows);
        getRelationName();
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ISSUE_ADD, IssueListView.this, (sender, args) -> listPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ISSUE_DELETE, IssueListView.this, (sender, args) -> listPanel.reloadPage());
        add(listPanel);

        return null;
    }

    private KpiSideNavBox getQuickAddView() {
        final KpiSideNavBox quickAddBox = new KpiSideNavBox();
        setStyleName(quickAddBox.getElement(), "quick-add", true);

        IssueQuickAddForm quickAddForm = new IssueQuickAddForm();
        quickAddForm.setRelationId(relationID);
        quickAddForm.setRelationType(relationType);
        quickAddForm.setRelationName(relationName);

        Heading header = new Heading(HeadingSize.H1);
        header.setText(property.getSingular(wfmStrings.addNewIssue(), wfmStrings.issue()));

        WfmButton2 saveBtn = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);

        WfmButton2 cancelBtn = new WfmButton2(wfmStrings.cancel(), WfmButton2.BTN_RESET);


        saveBtn.addClickHandler(event -> {
            saveBtn.setEnabled(false);
            cancelBtn.setEnabled(false);
            if (quickAddForm.validate()) {
                quickAddForm.save();
            } else {
                saveBtn.setEnabled(true);
                cancelBtn.setEnabled(true);
            }
        });
        cancelBtn.addClickHandler(event -> {
            quickAddForm.clearForm();
            quickAddBox.hide();
        });
        quickAddForm.setCommand(new ExtendedCommand() {
            @Override
            public void execute(Integer id) {
                cancelBtn.setEnabled(true);
                saveBtn.setEnabled(true);
                if (id > 0) {
                    quickAddForm.clearForm();
                    quickAddBox.hide();

                    ShortcutItem shortcutItem = getContainer().getItemsByView().get(ISSUE_LIST);

                    if (relationID != null && shortcutItem != null) {
                        shortcutItem.getStatisticCommand().execute();
                    }
                }
            }
        });
        quickAddBox.addOpeningHandler(event -> quickAddForm.getIssueQuickData(RelationItem.TYPE_PROJECT.equals(relationType) ? relationID : null));

        quickAddBox.addHeader(header);
        quickAddBox.addBody(quickAddForm);
        quickAddBox.addFooter(saveBtn);
        cancelBtn.ensureDebugId("issue_quick_add_cancel");
        saveBtn.ensureDebugId("issue_quick_add_save");
        return quickAddBox;
    }

    private void deleteSelection() {
        if (selectedItems.size() == 0) {
            Info.show(wfmMessages.pleaseSelectOneRow(property.getSingular(wfmStrings.issue())), Info.Type.WARNING);
        } else {
            showDeleteMessage();
        }
    }

    private CustomColumnDefinitionConfig[] getColumnConfig() {
        ArrayList<CustomColumnDefinitionConfig> columns = new ArrayList<>();

        //issue action
        ColumnDefinitionConfig columnConfig = new ColumnDefinitionConfig<Object, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final Object object) {
                IssueListItem item = (IssueListItem) object;
                actionItemCount = 0;
                MenuBar actions = new MenuBar(true);
                //issue summary
                MenuPopItem issueSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-issue-small");
                issueSummary.setCommand(() -> {
                    LoadingPanel.loading(true);
                    issueService.checkAccess(item.getObjectID(), PermissionConstants.PM_ISSUE_LIST, new AbstractAsyncCallback<Boolean>() {
                        @Override
                        public void failure(Throwable throwable) {
                            LoadingPanel.loading(false);
                            Info.show(wfmMessages.youDontHaveEnoughPermissons(), Info.Type.WARNING);
                        }

                        @Override
                        public void success(Boolean result) {
                            if (result) {
                                SinksContainerFactory.entryPoint.onHistoryChanged("issue|summary/" + item.getObjectID(), item.getNumber(), item.getName());
                            } else {
                                LoadingPanel.loading(false);
                                Info.show(wfmMessages.youDontHaveEnoughPermissons(), Info.Type.WARNING);
                            }
                        }
                    });
                });
                actionItemCount++;
                actions.addItem(issueSummary);
                issueSummary.ensureDebugId("issueSummary");

                //issue edit
                if (Utils.hasPermission(PermissionConstants.PM_ISSUE_EDIT)) {
                    MenuPopItem editIssue = new MenuPopItem(wfmStrings.edit(), "icon-issue-edit-small");
                    editIssue.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("issue|edit/" + item.getObjectID(), item.getNumber(), item.getName()));
                    actionItemCount++;
                    actions.addItem(editIssue);
                    editIssue.ensureDebugId("issueEdit");
                }

                //timer
                if (((IssueListItem) object).isShowTimer()) {
                    MenuPopItem timer = new MenuPopItem(wfmStrings.timer(), item.isTimerIsStarted() ? "icon-clock-active" : "icon-clock");
                    timer.ensureDebugId("wfmTimer");
                    timer.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("issueTimer|summary/" + ((IssueListItem) object).getObjectID().toString() + "/" + ((IssueListItem) object).getProjectID().toString()));
                    actionItemCount++;
                    actions.addItem(timer);
                }

                //issue delete
                if (hasDeleteAccess || (item.getIssueCreatorID() != null && item.getIssueCreatorID().equals(Utils.getUserID()))) {
                    MenuPopItem removeIssue = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile file--IssueListView");
                    removeIssue.setCommand(() -> {
                        WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                LoadingPanel.loading(true);
                                issueService.deleteIssue(item.getObjectID(), new AbstractAsyncCallback<Boolean>() {
                                    @Override
                                    public void failure(Throwable caught) {
                                        LoadingPanel.loading(false);
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    @Override
                                    public void success(Boolean result) {
                                        LoadingPanel.loading(false);
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ISSUE_DELETE, result, IssueListView.this);
                                        Info.show(property.getSingular(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.issue()), Info.Type.INFO);
//                                        listPanel.reloadPage();
                                    }
                                });
                            }
                        });
                        messageBox.open();
                    });

                    actionItemCount++;
                    actions.addItem(removeIssue);
                }

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(actions);

                return toolItem.getAction();
            }
        };
        columnConfig.setColumnSortable(false);
        columnConfig.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfig.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns.add(columnConfig);
        //issue number
        columnConfig = new ColumnDefinitionConfig<IssueListItem, Widget>(wfmStrings.number(), IssueListItem.NUMBER, 50) {
            @Override
            public Widget getCellValue(IssueListItem rowValue) {
                Label label = new Label(rowValue.getNumber());
                label.setStyleName("uploadLinkStyle2");
                label.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("issue|summary/" + rowValue.getObjectID(), rowValue.getNumber(), rowValue.getName()));
                return label;
            }
        };
        columnConfig.setMinimumColumnWidth(50);
        columns.add(columnConfig);
        //issue name
        columnConfig = new ColumnDefinitionConfig<Object, SimpleLink>(wfmStrings.name(), IssueListItem.NAME, 140) {
            @Override
            public SimpleLink getCellValue(Object object) {
                final IssueListItem item = (IssueListItem) object;
                return new SimpleLink((item.getName() != null ? item.getName() : ""), ("issue|summary/" + item.getObjectID()), item.getName(), item.getNumber());
            }
        };
        columnConfig.setMinimumColumnWidth(60);
        columns.add(columnConfig);

        //issue period
        columnConfig = new ColumnDefinitionConfig<Object, String>(wfmStrings.period(), IssueListItem.PERIOD, 100) {
            @Override
            public String getCellValue(Object object) {
                IssueListItem item = (IssueListItem) object;
                return DateUtils.format(item.getStartDate()) + " - " + DateUtils.format(item.getEndDate());
            }
        };
        columnConfig.setMinimumColumnWidth(70);
        columns.add(columnConfig);

        //issue resolver/owner
        columnConfig = new ColumnDefinitionConfig<Object, String>(wfmStrings.resolverOwner(), IssueListItem.RESOLVER, 150) {
            @Override
            public String getCellValue(Object object) {
                IssueListItem item = (IssueListItem) object;
                return item.getResolver();
            }
        };
        columnConfig.setMinimumColumnWidth(70);
        columns.add(columnConfig);

        //issue priority
        columnConfig = new ColumnDefinitionConfig<Object, String>(wfmStrings.priority(), IssueListItem.PRIORITY, 70) {
            @Override
            public String getCellValue(Object object) {
                IssueListItem item = (IssueListItem) object;
                return item.getPriority() != null ? item.getPriority() : "";
            }
        };
        columnConfig.setMinimumColumnWidth(40);
        columnConfig.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns.add(columnConfig);
        //issue status
        columnConfig = new ColumnDefinitionConfig<Object, String>(property.getSingular(wfmStrings.status(), wfmStrings.issue()), IssueListItem.STATUS, 120) {
            @Override
            public String getCellValue(Object object) {
                IssueListItem item = (IssueListItem) object;
                return item.getStatus();
            }
        };
        columnConfig.setMinimumColumnWidth(70);
        columnConfig.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columns.add(columnConfig);

        //issue description
        columnConfig = new ColumnDefinitionConfig<IssueListItem, String>(wfmStrings.description(), IssueListItem.DESCRIPTION, 50) {
            @Override
            public String getCellValue(IssueListItem rowValue) {
                return rowValue.getDescription();
            }
        };
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(100);
        columns.add(columnConfig);

        //issue timeSheet
        columnConfig = new ColumnDefinitionConfig<Object, String>(Property.get(Constants.TIMESHEET, wfmStrings.timesheet()), IssueListItem.TIMESHEET, 70) {
            @Override
            public String getCellValue(Object object) {
                IssueListItem item = (IssueListItem) object;
                return item.isTimeSheetEnabled() ? wfmStrings.enabled() : wfmStrings.disabled();
            }
        };
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(40);
        columns.add(columnConfig);

        //
        //related to columns
        //related contact
        columnConfig = new ColumnDefinitionConfig<IssueListItem, String>(Property.get(Constants.Contacts, crmStrings.relatedContact(), wfmStrings.contact()), RelationItem.TYPE_CONTACT, 100) {
            @Override
            public String getCellValue(IssueListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_CONTACT);
            }
        };
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(95);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);
        //related lead
        columnConfig = new ColumnDefinitionConfig<IssueListItem, String>(Property.get(Constants.LEADS, wfmStrings.relatedLead(), wfmStrings.lead()), RelationItem.TYPE_LEAD, 100) {
            @Override
            public String getCellValue(IssueListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_LEAD);
            }
        };
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(95);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);
        //related crm account
        columnConfig = new ColumnDefinitionConfig<IssueListItem, String>(wfmStrings.relatedCrmAccount(), RelationItem.TYPE_CRM_ACCOUNT, 100) {
            @Override
            public String getCellValue(IssueListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_CRM_ACCOUNT);
            }
        };
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(95);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);
        //related case
        columnConfig = new ColumnDefinitionConfig<IssueListItem, String>(Property.get(Constants.CASE_LIST, crmStrings.relatedCase(), wfmStrings.crmCase()), RelationItem.TYPE_CASE, 100) {
            @Override
            public String getCellValue(IssueListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_CASE);
            }
        };
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(95);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);
        //related opportunity
        columnConfig = new ColumnDefinitionConfig<IssueListItem, String>(Property.get(Constants.Opportunities, wfmStrings.relatedToOpportunity(), wfmStrings.opportunity()), RelationItem.TYPE_OPPORTUNITY, 100) {
            @Override
            public String getCellValue(IssueListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_OPPORTUNITY);
            }
        };
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(95);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);
        //related event
        columnConfig = new ColumnDefinitionConfig<IssueListItem, String>(Property.get(Constants.EVENT_LIST, wfmStrings.relatedEvent(), wfmStrings.event()), RelationItem.TYPE_EVENT, 100) {
            @Override
            public String getCellValue(IssueListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_EVENT);
            }
        };
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(95);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);
        //related task
        columnConfig = new ColumnDefinitionConfig<IssueListItem, String>(Property.get(Constants.TASK, crmStrings.relatedTask(), wfmStrings.task()), RelationItem.TYPE_TASK, 100) {
            @Override
            public String getCellValue(IssueListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_TASK);
            }
        };
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(95);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);
        //related project
        columnConfig = new ColumnDefinitionConfig<IssueListItem, String>(Property.get(Constants.PROJECT, wfmStrings.relatedToProject(), wfmStrings.project()), RelationItem.TYPE_PROJECT, 100) {
            @Override
            public String getCellValue(IssueListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_PROJECT);
            }
        };
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(95);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);
        //related employee
        columnConfig = new ColumnDefinitionConfig<IssueListItem, String>(wfmStrings.relatedEmployee(), RelationItem.TYPE_EMPLOYEE, 100) {
            @Override
            public String getCellValue(IssueListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_EMPLOYEE);
            }
        };
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(95);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);
        //related department
        columnConfig = new ColumnDefinitionConfig<IssueListItem, String>(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.relatedDepartment(), wfmStrings.department()), RelationItem.TYPE_DEPARTMENT, 100) {
            @Override
            public String getCellValue(IssueListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_DEPARTMENT);
            }
        };
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(95);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);
        //related client
        columnConfig = new ColumnDefinitionConfig<IssueListItem, String>(Property.get(Constants.CLIENT_LIST, wfmStrings.relatedClient()), RelationItem.TYPE_CLIENT, 100) {
            @Override
            public String getCellValue(IssueListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_CLIENT);
            }
        };
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(95);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);
        //related supplier
        columnConfig = new ColumnDefinitionConfig<IssueListItem, String>(Property.get(Constants.SUPPLIER_LIST, wfmStrings.relatedSupplier()), RelationItem.TYPE_SUPPLIER, 100) {
            @Override
            public String getCellValue(IssueListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_SUPPLIER);
            }
        };
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(95);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);
        return columns.toArray(new CustomColumnDefinitionConfig[]{});
    }

    private ArrayList<Integer> getIDsOnly(Set<IssueListItem> selectedItems) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (IssueListItem item : selectedItems) {
            if (hasDeleteAccess || (item.getIssueCreatorID() != null && item.getIssueCreatorID().equals(Utils.getUserID()))) {
                ids.add(item.getObjectID());
            }
        }
        return ids;
    }

    private ListingRequestProvider<IssueListItem> getListProvider() {
        return (fp, listingCallback) -> {
            if (fp == null) {
                fp = new ListingFilterParameter();
            }
            initIssueList(fp, listingCallback, null);
        };
    }

    private void initIssueList(ListingFilterParameter fp, ListingCallback<IssueListItem> listingCallback, Span container) {
        if (relationID != null && relationType != null) {
            fp.setRelationID(relationID);
            fp.setRelationType(relationType);
        }
        issueService.getIssuesList(fp, new AsyncCallback<ListResult<IssueListItem>>() {
            @Override
            public void onFailure(Throwable throwable) {
                if (listingCallback != null) {
                    listingCallback.onFailure(throwable);
                }
            }

            @Override
            public void onSuccess(ListResult<IssueListItem> issueList) {
                if (listingCallback != null) {
                    listingCallback.onSuccess(issueList);
                }
                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (issueList.getTotal() != null && issueList.getTotal() > 0) {
                        statisticShortcut.setText(countFormat(issueList.getTotal()));
                        statisticShortcut.setClass("tab-label");
                    } else {
                        statisticShortcut.setText("");
                        statisticShortcut.removeStyleName("tab-label");
                    }
                }
            }
        });
    }

    private GuideListingPanelDesign getListDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return hasPermissionToAdd() ? IssueListView.this::addNewIssue : null;
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
                        if (Utils.hasRole(CLIENT)) {
                            return ListingChooseFilter.PROJECT + ListingChooseFilter.EMPLOYEE + ListingChooseFilter.ISSUE_STATUS + ListingChooseFilter.ISSUE_PRIORITY;
                        } else {
                            ListingChooseFilter.wCategory = false;
                            return ListingChooseFilter.ISSUE_LIST;
                        }
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (hasPermissionToAdd()) {
                    ActionButton newItem = getAddNewButton(ActionButton.Type.TOOLMENU);
                    MenuBar menu = new MenuBar(true);

                    MenuPopItem addNew = new MenuPopItem(property.getSingular(wfmStrings.issue()));
                    addNew.setCommand(() -> {
                        addNewIssue();
                    });
                    menu.addItem(addNew);
                    addNew.ensureDebugId("addNewIssue_id");

                    MenuPopItem quick = new MenuPopItem(wfmStrings.quickAdd());
                    quick.ensureDebugId("new_task");
                    quick.setCommand(() -> getQuickAddView().show());
                    menu.addItem(quick);

                    newItem.setMenu(menu);
                    newItem.getElement().setId("addNewIssueButton");
                    return newItem;
                }
                return null;
            }

            @Override
            public ActionButton initTopToolBarMore() {
                if (hasDeleteAccess) {
                    return getRemoveMoreButton(clickEvent -> deleteSelection());
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
            public boolean isShowCustomiseButton() {
                return Utils.hasPermission(PermissionConstants.PM_ISSUE_LIST_CUSTOMIZE_BUTTON);
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(property.getPlural(projectStrings.currentlyAnyIssues(), wfmStrings.issues()));
                if (Utils.hasPermission(PermissionConstants.PM_ISSUE_ADD)) {
                    message.setTextBeforeLink(property.getSingular(projectStrings.addIssueByClicking(), wfmStrings.issue()));
                    message.setHref(clickEvent -> getQuickAddView().show());
                }
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return Utils.hasPermission(PermissionConstants.PM_ISSUE_EDIT);
            }
        };
    }

    private void addNewIssue() {
        String historyToken;
        if (relationID != null && relationType != null) {
            historyToken = "issue|add/add/" + relationID + "/" + relationType + "/" + relationName;
        } else {
            historyToken = "issue|add/add";
        }
        SinksContainerFactory.entryPoint.onHistoryChanged(historyToken);
    }

    private void saveIssueEditCellValue(IssueListItem rowValue, String columnCodeName) {
        issueService.saveIssueEditCellValue(rowValue, columnCodeName, new AbstractAsyncCallback<Boolean>() {
            @Override
            public void onFailure(Throwable caught) {
                super.onFailure(caught);
            }

            @Override
            public void onSuccess(Boolean result) {
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_ISSUE_ADD, result, IssueListView.this);
            }
        });
    }

    private void getRelationName() {
        AllInOneService.App.get().getRelationName(relationID, relationType, new AbstractAsyncCallback<String>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(String result) {
                if (result != null) {
                    relationName = result;
                }
            }
        });
    }

    private void showDeleteMessage() {
        WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
        messageBox.setTitle(wfmStrings.warning());
        IssueListItem item = (IssueListItem) selectedItems.iterator().next();
        String message = wfmStrings.areYouSureYouWantToDeleteTheSelectedRecords();
        messageBox.setMessage(message);
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                ArrayList<Integer> ids = getIDsOnly(selectedItems);
                if (ids.size() > 0) {
                    LoadingPanel.loading(true);
                    issueService.deleteIssueMass(ids, new AsyncCallback<Boolean>() {
                        @Override
                        public void onFailure(Throwable caught) {
                            LoadingPanel.loading(false);
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        }

                        @Override
                        public void onSuccess(Boolean result) {
                            listPanel.reloadPage();
                            LoadingPanel.loading(false);
                            Info.show(property.getSingular(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.issue()), Info.Type.INFO);
                        }
                    });
                }
            }
        });
        messageBox.open();
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

    @Override
    public void initStatistics(Integer parentId, Span container) {
        ListingFilterParameter fp = new ListingFilterParameter();
        relationID = parentId;
        fp.setLimit(1);
        initIssueList(fp, null, container);
    }

    @Override
    public String getPropertyCode() {
        return Constants.ISSUE;
    }
}