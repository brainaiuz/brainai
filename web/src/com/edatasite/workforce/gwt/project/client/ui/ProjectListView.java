package com.edatasite.workforce.gwt.project.client.ui;

import com.edatasite.workforce.gwt.core.client.*;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterCutomField;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrProjectListRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.ImportFileActionLink;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetFilterPopup;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.landing.HelpPanelGenerator;
import com.edatasite.workforce.gwt.core.client.ui.listTable.ImportFilePopUp;
import com.edatasite.workforce.gwt.core.client.ui.listTable.ImportMPPFilePopup;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.DateTimePickerCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.DropDownCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CellChange;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.shortcut.ShortcutItem;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectListItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectService;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectServiceAsync;
import com.edatasite.workforce.gwt.project.client.ui.quickadd.ProjectQuickAddForm;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.constants.HeadingSize;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Heading;
import gwt.material.design.client.ui.html.ListItem;
import gwt.material.design.client.ui.html.Span;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Created by IntelliJ IDEA. User: Anvarbek Date: 15.01.2008 Time: 12:33:26 To
 * change this template use File | Settings | File Templates.
 */

public class ProjectListView extends BaseListView implements Constants {

    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private static final NumberFormat numberFormat = NumberFormat.getFormat(",##0.00");
    private final ProjectServiceAsync projectService = ProjectService.App.get();
    public KpiSideNavBox quickAddBox;
    protected ContextMenu actions;
    protected ActionButton actionButton;
    protected ContextMenu actionsEmpty;
    private ListingPanel<ProjectListItem> listingTable;
    private FacetFilterPopup projectFacetPopup;
    private Integer parentProjectId;
    private final HashMap<Integer, HashSet<String>> permissionMap = new HashMap<>();
    private final HorizontalPanel toolPanel = new HorizontalPanel();
    private ImportFilePopUp uploadPopup;
    private HashSet<ProjectListItem> selectedRows;
    private String relationType;
    private Integer relationID;
    private int actionItemCount;
    private boolean isClientView;
    private String from = "PM";

    public ProjectListView() {
        super(PROJECT_LIST);
        setDescription(property.getPlural(wfmStrings.projects()));
        if (hasPermissionToAdd()) {
            setAddNew(new Command() {
                @Override
                public void execute() {
                    if (quickAddBox == null) {
                        initQuickAddView();
                    }
                    quickAddBox.show();
                }
            });
        }
    }

    public ProjectListView(Integer parentProjectId) {
        super(SUB_PROJECT_LIST);
        setDescription(Property.getPluralWithObjectCode(SUB_PROJECT_LIST, projectStrings.subprojects()));
        this.parentProjectId = parentProjectId;
    }

    public ProjectListView(String relationType, Integer relationID, boolean isClientView) {
        this();
        this.relationType = relationType;
        this.relationID = relationID;
        this.isClientView = isClientView;
    }

    public ProjectListView(String relationType, Integer relationID, String from) {
        this();
        setDescription(Property.getPluralWithObjectCode(PROJECT, wfmStrings.projectsList()));
        this.relationType = relationType;
        this.relationID = relationID;
        this.from = from;
    }

    public FlowPanel getHelpContainer() {
        if (helpPanel == null) {
            helpPanel = HelpPanelGenerator.getHelpPanel(PermissionConstants.PM_CONTEXT, PermissionConstants.PM_PROJECT_LIST);
        }
        return helpPanel;
    }

    @Override
    public String getIconStyle() {
        return "bgMark icon-project";
    }

    public ListingFilterParameter getFiterParametrs() {
        return null;
    }

    protected Widget onInitialize() {
        listingTable = new GuideListingPanel(ListPanelType.ProjectListPanel, getColumnConfigs(), getListingRequestProvider(), getListingPanelDesign(), SelectionGrid.SelectionPolicy.CHECKBOX, -1, Utils.hasGenericAccess(GenericSettingsEnum.PROJECT_COST_IN_LISTING_ENABLED), null, null);
        listingTable.setCustomFieldsEditCellSaveChanges((rowValue, columnCodeName) -> saveProjectEditCellValue((ProjectListItem) rowValue, columnCodeName));

        final boolean hasPermission = Utils.hasPermission(PermissionConstants.PM_PROJECT_LIST_PDF_EXCEL_EXPORT);
        listingTable.setExcelListener(clickEvent -> {
            if (hasPermission) {
                String excelURL = CommandConstants.COMMON_URL + "/downloadProjectListExcel";
                ListingFilterParameter filterParametrs = listingTable.getFilterParametrs();
                if (parentProjectId != null) {
                    filterParametrs.setPropertyCode(getPropertyCode());
                    filterParametrs.setProjectId(parentProjectId);
                }
                if (getFiterParametrs() != null && getFiterParametrs().getDepartmentId() != null) {
                    filterParametrs.setDepartmentId((getFiterParametrs().getDepartmentId()));
                }
                listingTable.callListExcel(excelURL, filterParametrs);
            } else {
                LoadingPanel.loading(false);
                Info.show("You don't have enough permissions", Info.Type.WARNING);
            }
        });
        listingTable.setPDFListener(clickEvent -> {
            if (hasPermission) {
                String pdfURL = CommandConstants.PDF_URL + "/projectListPDFHandler";
                ListingFilterParameter filterParametrs = listingTable.getFilterParametrs();
                if (parentProjectId != null) {
                    filterParametrs.setProjectId(parentProjectId);
                }
                if (getFiterParametrs() != null && getFiterParametrs().getDepartmentId() != null) {
                    filterParametrs.setDepartmentId((getFiterParametrs().getDepartmentId()));
                }
                listingTable.callListPDF(pdfURL, filterParametrs);
            } else {
                LoadingPanel.loading(false);
                Info.show("You don't have enough permissions", Info.Type.WARNING);
            }
        });
        add(listingTable);
        add(toolPanel);
        // sub project or project event registrations
        registrationEvents();
        listingTable.addSelectionRowHandler(selected -> selectedRows = selected);
        initQuickAddView();
        return null;
    }

    private void initQuickAddView() {
        quickAddBox = new KpiSideNavBox();
        setStyleName(quickAddBox.getElement(), "quick-add", true);

        ProjectQuickAddForm quickAddForm = new ProjectQuickAddForm(relationType, relationID, parentProjectId);

        Heading header = new Heading(HeadingSize.H1);
        header.setText(property.getSingular(wfmStrings.addMess(), wfmStrings.project()));

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

                    ShortcutItem shortcutItem = getContainer().getItemsByView().get(PROJECT_LIST);

                    if ((relationID != null || parentProjectId != null) && shortcutItem != null) {
                        shortcutItem.getStatisticCommand().execute();
                    }
                }
            }
        });
        quickAddBox.addOpeningHandler(event -> quickAddForm.getProjectQuickData());

        quickAddBox.addHeader(header);
        quickAddBox.addBody(quickAddForm);
        quickAddBox.addFooter(saveBtn);
        saveBtn.getElement().setId("gwt_debug-project-saveButton");
        cancelBtn.ensureDebugId("project-cancelButton");
    }

    /**
     * <i>... This is method refistration project or sub project events ...</i>
     * <br/>
     * <i>... Write developer {Dilshod.T} ...</i>
     * <br/>
     * <i>... Created date {20:12 24/05/2011} ...</i>
     */
    private void registrationEvents() {
        int ON_PROJECT_ADD = WfmUiEventType.ON_PROJECT_ADD, ON_PROJECT_MEMBER_ADD = WfmUiEventType.ON_PROJECT_MEMBER_ADD;
        int ON_PROJECT_EDIT = WfmUiEventType.ON_PROJECT_EDIT, ON_PROJECT_DELETE = WfmUiEventType.ON_PROJECT_DELETE;
        if (parentProjectId != null) {
            ON_PROJECT_ADD = WfmUiEventType.ON_SUB_PROJECT_ADD;
            ON_PROJECT_MEMBER_ADD = WfmUiEventType.ON_SUB_PROJECT_MEMBER_ADD;
            ON_PROJECT_EDIT = WfmUiEventType.ON_SUB_PROJECT_EDIT;
            ON_PROJECT_DELETE = WfmUiEventType.ON_SUB_PROJECT_DELETE;
        }
        WfmUiEventsBus.addWfmUiListener(ON_PROJECT_ADD, ProjectListView.this, (sender, args) -> listingTable.reloadPage());

        WfmUiEventsBus.addWfmUiListener(ON_PROJECT_MEMBER_ADD, ProjectListView.this, (sender, args) -> listingTable.reloadPage());

        WfmUiEventsBus.addWfmUiListener(ON_PROJECT_EDIT, ProjectListView.this, (sender, args) -> listingTable.reloadPage());
        WfmUiEventsBus.addWfmUiListener(ON_PROJECT_DELETE, ProjectListView.this, (sender, args) -> listingTable.reloadPage());
    }

    private ColumnDefinitionConfig[] getColumnConfigs() {
        List<ColumnDefinitionConfig> columnConfigs = new ArrayList<>();

        boolean isInvoiceColumnEnabled = Utils.hasRole(Utils.DR) || Utils.hasRole(Utils.ADMIN) || Utils.hasRole(Utils.PM) || Utils.hasRole(Utils.TL);

        // Action
        if (Utils.isPM() || Utils.isHRMS()) {
            ColumnDefinitionConfig column1 = new ColumnDefinitionConfig<ProjectListItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
                @Override
                public Anchor getCellValue(final ProjectListItem rowValue) {
                    final boolean hasAccessToChange = !Utils.isLockCompletedProjecItems() || (Utils.isLockCompletedProjecItems() && !PS_CLOSED.equals(rowValue.getStatusCode()));
                    actionItemCount = 0;

                    final MenuBar actions = new MenuBar(true);

                    MenuPopItem projectSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-project-small");
                    projectSummary.ensureDebugId("projectSummary");

                    projectSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged(
                            "project|summary/" + rowValue.getObjectId() + "/" + parentProjectId + "/" + hasAccessToChange, rowValue.getNumber(), rowValue.getName()));
                    actionItemCount++;
                    actions.addItem(projectSummary);


                    final MenuPopItem projectEdit = new MenuPopItem(wfmStrings.edit(), "icon-project-edit-small");
                    projectEdit.ensureDebugId("editProject");
                    projectEdit.setVisible(false);
                    projectEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("project|edit/" + rowValue.getObjectId() + "/" + parentProjectId, rowValue.getNumber(), rowValue.getName()));
                    if (Utils.hasPermission(PermissionConstants.PM_PROJECT_EDIT) && (hasAccessToChange || Utils.hasRole(ADMIN))) {
                        actionItemCount++;
                        actions.addItem(projectEdit);
                    }

                    final MenuPopItem projectTasks = new MenuPopItem(Property.getPluralWithObjectCode(TASK, wfmStrings.tasks()), "icon-task-small");
                    projectTasks.ensureDebugId("projectTasks");
                    projectTasks.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("project|taskList/" + rowValue.getObjectId() + "/" + parentProjectId + "/" + hasAccessToChange, rowValue.getNumber(), rowValue.getName()));
                    actionItemCount++;
                    actions.addItem(projectTasks);

                    if (CompanyConstants.C8032.equals(Utils.getEncryptedCompanyID())) {
                        MenuPopItem projectTaskPDF = new MenuPopItem(Property.get(TASK, wfmStrings.exportAllTask(), wfmStrings.task()), "icon-document-pdf");
                        projectTaskPDF.ensureDebugId("Export All Task");
                        projectTaskPDF.setCommand(() -> {
                            final String pdfURL = CommandConstants.PDF_URL + "/taskViewPDFHandler";
                            final RequestObject requestObject = new RequestObject(rowValue.getObjectId());
                            final HashMap<String, String> parametrs = requestObject.getRequestParams();
                            parametrs.put("fromC8032", "");
                            Utils.sendPDFOrExcelRequest(toolPanel, pdfURL, parametrs, "_blank");
                        });

                        if (!Utils.hasRole(CLIENT)) {
                            actionItemCount++;
                            actions.addItem(projectTaskPDF);
                        }
                    }

                    final MenuPopItem removeItem = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    removeItem.ensureDebugId("delete");
                    removeItem.setCommand(() -> {
                        final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        message.setTitle(wfmStrings.warning());
                        message.setMessage(wfmStrings.sureYouWantToDelete());
                        message.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                LoadingPanel.loading(true);
                                projectService.deleteProject(rowValue.getObjectId(), new AbstractAsyncCallback<Void>() {
                                    public void failure(Throwable throwable) {
                                        LoadingPanel.loading(false);
                                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                                    }

                                    public void success(Void result) {
                                        LoadingPanel.loading(false);
                                        if (parentProjectId != null) {// sub project delete
                                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_SUB_PROJECT_DELETE, result, ProjectListView.this);
                                        } else {
                                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PROJECT_DELETE, result, ProjectListView.this);
                                        }
                                        Info.show(property.getSingular(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.project()), Info.Type.INFO);
                                    }
                                });
                            }
                        });
                        message.open();
                    });

                    if (hasAccessToChange && !rowValue.getObjectId().equals(rowValue.getDefaultProjectId()) && !rowValue.getObjectId().equals(rowValue.getCrmProjectId())) {
                        actionItemCount++;
                        actions.addItem(removeItem);
                    }

                    ToolItem toolItem = new ToolItem(actionItemCount);
                    toolItem.setWidget(actions);

                    Anchor anchor = toolItem.getAction();
                    anchor.addClickHandler(new ClickHandler() {
                        @Override
                        public void onClick(ClickEvent clickEvent) {
                            if (Utils.hasRole(ADMIN)) {
                                projectEdit.setVisible(true);
                                return;
                            }
                            projectEdit.setVisible(false);
                            projectTasks.setVisible(false);
                            removeItem.setVisible(false);
                            final Integer objectID = rowValue.getObjectId();
                            showContextMenu();
                        }

                        private void showContextMenu() {
                            projectEdit.setVisible(Utils.hasPermission(PermissionConstants.PM_PROJECT_EDIT));
                            projectTasks.setVisible(Utils.hasPermission(PermissionConstants.PM_TASKS_LIST));
                            removeItem.setVisible(Utils.hasPermission(PermissionConstants.PM_PROJECT_REMOVE) && !rowValue.getObjectId().equals(rowValue.getDefaultProjectId()) && !rowValue.getObjectId().equals(rowValue.getCrmProjectId()));
                        }
                    });
                    return anchor;
                }
            };
            column1.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
            column1.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
            column1.setColumnSortable(false);
            column1.setFooterName("");
            columnConfigs.add(column1);
        }

        // Project Number
        ColumnDefinitionConfig column2 = new ColumnDefinitionConfig<ProjectListItem, Widget>(wfmStrings.number(), ProjectListItem.NUMBER, 60) {
            @Override
            public Widget getCellValue(ProjectListItem item) {
                final boolean hasAccessToChange = !Utils.isLockCompletedProjecItems() || (Utils.isLockCompletedProjecItems() && !PS_CLOSED.equals(item.getStatusCode()));
                Label label = new Label(item.getNumber());
                label.setStyleName("uploadLinkStyle2");
                if (Utils.isPM() || Utils.isHRMS()) {
                    label.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("project|summary/" + item.getObjectId() + "/" + parentProjectId + "/" + hasAccessToChange, item.getNumber(), item.getName()));
                } else {
                    String moduleName = from.equals("HRMS") ? "Hrms.html" : "ProjectManagement.html";
                    label.addClickHandler(clickEvent -> Window.open(moduleName + "#project|summary/" + item.getObjectId() + "/" + parentProjectId + "/" + hasAccessToChange, "_blank", ""));
                }
                return label;
            }
        };
        column2.setMinimumColumnWidth(60);
        column2.setFooterName("");
        columnConfigs.add(column2);

        // Project Name
        ColumnDefinitionConfig column3 = new ColumnDefinitionConfig<ProjectListItem, Widget>(wfmStrings.name(), ProjectListItem.NAME, 140) {
            @Override
            public Widget getCellValue(ProjectListItem rowValue) {
                final boolean hasAccessToChange = !Utils.isLockCompletedProjecItems() || (Utils.isLockCompletedProjecItems() && !PS_CLOSED.equals(rowValue.getStatusCode()));
                Label label = new Label(rowValue.getName());
                label.setStyleName("uploadLinkStyle2");
                if (Utils.isPM() || Utils.isHRMS()) {
                    label.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("project|summary/" + rowValue.getObjectId() + "/" + parentProjectId + "/" + hasAccessToChange, rowValue.getNumber(), rowValue.getName()));
                } else {
                    String moduleName = from.equals("HRMS") ? "Hrms.html" : "ProjectManagement.html";
                    label.addClickHandler(clickEvent -> Window.open(moduleName + "#project|summary/" + rowValue.getObjectId() + "/" + parentProjectId + "/" + hasAccessToChange, "_blank", ""));
                }
                return label;
            }
        };
        column3.setMinimumColumnWidth(135);
        column3.setFooterName("");
        columnConfigs.add(column3);

        // Client Name
        ColumnDefinitionConfig column7 = new ColumnDefinitionConfig<ProjectListItem, String>(Property.get(Constants.CLIENT_LIST, wfmStrings.customer()), ProjectListItem.CLIENT, 120) {
            @Override
            public String getCellValue(ProjectListItem rowValue) {
                return rowValue.getClient();
            }
        };
        column7.setMinimumColumnWidth(115);
        column7.setFooterName("");
        columnConfigs.add(column7);

        // Start Date
        ColumnDefinitionConfig column15 = new ColumnDefinitionConfig<ProjectListItem, String>(wfmStrings.startDate(), ProjectListItem.START_DATE, 100) {
            @Override
            public String getCellValue(ProjectListItem rowValue) {
                return DateUtils.format(rowValue.getStartDate());
            }

            @Override
            public void setCellValue(ProjectListItem rowValue, String cellValue) {
                try {
                    rowValue.setStartDate(DateUtils.parse(cellValue));
                    saveCellValue(rowValue);
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }
        };
        column15.setMinimumColumnWidth(95);
        column15.setFooterName("");
        column15.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnConfigs.add(column15);

        // End Date
        ColumnDefinitionConfig column16 = new ColumnDefinitionConfig<ProjectListItem, String>(wfmStrings.endDate(), ProjectListItem.END_DATE, 100) {
            @Override
            public String getCellValue(ProjectListItem rowValue) {
                return DateUtils.format(rowValue.getEndDate());
            }

            @Override
            public void setCellValue(ProjectListItem rowValue, String cellValue) {
                try {
                    rowValue.setEndDate(DateUtils.parse(cellValue));
                    saveCellValue(rowValue);
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }
        };
        column16.setMinimumColumnWidth(95);
        column16.setFooterName("");
        column16.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnConfigs.add(column16);

        // Project Manager
        ColumnDefinitionConfig column5 = new ColumnDefinitionConfig<ProjectListItem, String>(wfmStrings.manager(), ProjectListItem.MANAGER, 120) {
            @Override
            public String getCellValue(ProjectListItem rowValue) {
                return rowValue.getManager();
            }

            @Override
            public void setCellValue(ProjectListItem rowValue, String cellValue) {
                rowValue.setManager(cellValue);
                saveCellValue(rowValue);
            }
        };
        column5.setMinimumColumnWidth(115);
        column5.setFooterName("");
        columnConfigs.add(column5);

        // Status
        ColumnDefinitionConfig column13 = new ColumnDefinitionConfig<ProjectListItem, String>(wfmStrings.status(), ProjectListItem.STATUS, 80) {
            @Override
            public String getCellValue(ProjectListItem rowValue) {
                return rowValue.getStatus();
            }

            @Override
            public void setCellValue(ProjectListItem rowValue, String cellValue) {
                rowValue.setStatus(cellValue);
                saveCellValue(rowValue);
            }
        };
        column13.setMinimumColumnWidth(75);
        column13.setFooterName("");
        column13.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnConfigs.add(column13);

        // Project Description
        ColumnDefinitionConfig column4 = new ColumnDefinitionConfig<ProjectListItem, String>(wfmStrings.description(), ProjectListItem.DESCRIPTION, 150) {
            @Override
            public String getCellValue(ProjectListItem rowValue) {
                return rowValue.getDescription();
            }
        };
        column4.setMinimumColumnWidth(145);
        column4.setShow(false);
        column4.setFooterName("");
        columnConfigs.add(column4);

        // Project Bacup Manager
        ColumnDefinitionConfig column6 = new ColumnDefinitionConfig<ProjectListItem, String>(wfmStrings.backupManagers(), ProjectListItem.BACKUP_MANAGER, 120) {
            @Override
            public String getCellValue(ProjectListItem rowValue) {
                return rowValue.getBackupManager();
            }
        };
        column6.setMinimumColumnWidth(115);
        column6.setShow(false);
        column6.setFooterName("");
        column6.setColumnSortable(false);
        columnConfigs.add(column6);

        // Time Spent
        ColumnDefinitionConfig column8 = new ColumnDefinitionConfig<ProjectListItem, String>(wfmStrings.actualTimeSpent(), ProjectListItem.ACTUAL_TIME_SPENT, 100) {
            @Override
            public String getCellValue(ProjectListItem rowValue) {
                return rowValue.getActualHoursSpent();
            }
        };
        column8.setMinimumColumnWidth(100);
        column8.setShow(false);
        column8.setColumnSortable(false);
        column8.setFooterName("");
        column8.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnConfigs.add(column8);

        // Time Spent
        ColumnDefinitionConfig column9 = new ColumnDefinitionConfig<ProjectListItem, String>(wfmStrings.timeSpentOnly(), ProjectListItem.HOURS_SPENT, 100) {
            @Override
            public String getCellValue(ProjectListItem rowValue) {
                return rowValue.getHoursSpent();
            }
        };
        column9.setMinimumColumnWidth(100);
        column9.setShow(false);
        column9.setFooterName("");
        column9.setColumnSortable(false);
        column9.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnConfigs.add(column9);

//        Estimated Time
        ColumnDefinitionConfig column10 = new ColumnDefinitionConfig<ProjectListItem, String>(wfmStrings.estimatedTime(), ProjectListItem.ESTIMATED_TIME, 110) {
            @Override
            public String getCellValue(ProjectListItem rowValue) {
                return rowValue.getEstimatedTime() != null ? formatIntToTime(rowValue.getEstimatedTime()) : "00:00";
            }
        };
        column10.setShow(false);
        column10.setMinimumColumnWidth(105);
        column10.setColumnSortable(false);
        column10.setFooterName("");
        column10.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnConfigs.add(column10);

        // Number of Tasks
        ColumnDefinitionConfig column11 = new ColumnDefinitionConfig<ProjectListItem, Long>(wfmStrings.noOfTask(), ProjectListItem.NUMBER_OF_TASKS, 80) {
            @Override
            public Long getCellValue(ProjectListItem rowValue) {
                return rowValue.getTaskCount();
            }
        };
        column11.setMinimumColumnWidth(75);
        column11.setShow(false);
        column11.setColumnSortable(false);
        column11.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        column11.setFooterName("");
        columnConfigs.add(column11);

        // Head Count
        ColumnDefinitionConfig column12 = new ColumnDefinitionConfig<ProjectListItem, Integer>(wfmStrings.headCount(), ProjectListItem.HEAD_COUNT, 80) {
            @Override
            public Integer getCellValue(ProjectListItem rowValue) {
                return rowValue.getHeadCount();
            }
        };
        column12.setShow(false);
        column12.setMinimumColumnWidth(75);
        column12.setColumnSortable(false);
        column12.setFooterName("");
        column12.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnConfigs.add(column12);

        // Percent Completed
        ColumnDefinitionConfig column14 = new ColumnDefinitionConfig<ProjectListItem, HTML>(wfmStrings.percent(), ProjectListItem.PERCENT_COMPLETED, 80) {
            @Override
            public HTML getCellValue(ProjectListItem rowValue) {
                String[] st = rowValue.getComplete().split("%");
                String[] rt = st[0].split("\\ ");
                String percent = rt[0];
                if (percent == null || "".equals(percent) || "null".equals(percent) || "0.0".equals(percent)) {
                    return new HTML("0.00%");
                } else {
                    BigDecimal bigDecimal = new BigDecimal(percent);
                    Double bigDoouble = Double.valueOf(percent);
                    if (bigDoouble > 100) {
                        return new HTML("<p style='color: red'>" + bigDecimal.setScale(2, RoundingMode.HALF_UP) + "% </p>");
                    } else {
                        return new HTML(bigDecimal.setScale(2, RoundingMode.HALF_UP) + "%");
                    }

                }
            }

            @Override
            public void setCellValue(ProjectListItem rowValue, HTML cellValue) {

                String[] value = cellValue.getText().split("%");
                if (Integer.valueOf(value[0]) > 100 && !Utils.hasGenericAccess(GenericSettingsEnum.PROJECT_PERCENT_OVER_HUNDRED)) {
                    cellValue = new HTML("100");
                } else {
                    cellValue = new HTML(value[0]);
                }
                rowValue.setComplete(cellValue.getText());
                saveCellValue(rowValue);
            }
        };
        column14.setMinimumColumnWidth(75);
        column14.setShow(false);
        column14.setFooterName("");
        column14.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnConfigs.add(column14);

        if (isInvoiceColumnEnabled) {
            // Invoices
            ColumnDefinitionConfig column17 = new ColumnDefinitionConfig<ProjectListItem, SimpleLink>(wfmStrings.invoices(), ProjectListItem.INVOICES, 110) {
                @Override
                public SimpleLink getCellValue(ProjectListItem rowValue) {
                    if (rowValue.getInvoiceNumber() != null) {
                        return new SimpleLink(rowValue.getInvoiceNumber(), "project|ProjectInvoice/" + rowValue.getObjectId(), rowValue.getName(), rowValue.getName());
                    }
                    return null;
                }
            };
            column17.setMinimumColumnWidth(110);
            column17.setShow(false);
            column17.setFooterName("");
            columnConfigs.add(column17);
        }

        if (CompanyConstants.C10520.equals(Utils.getEncryptedCompanyID()) || Utils.getHostURL().contains(HOST_AWS)) {
            // Location
            ColumnDefinitionConfig column18 = new ColumnDefinitionConfig<ProjectListItem, String>(Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), ProjectListItem.LOCATION, 100) {
                @Override
                public String getCellValue(ProjectListItem rowValue) {
                    return rowValue.getProjectLocation();
                }
            };
            column18.setMinimumColumnWidth(100);
            column18.setColumnSortable(false);
            column18.setShow(false);
            column18.setFooterName("");
            columnConfigs.add(column18);
        }

        //related to columns
        ColumnDefinitionConfig column19 = new ColumnDefinitionConfig<ProjectListItem, String>(Property.get(Constants.Contacts, crmStrings.relatedContact(), wfmStrings.contact()), RelationItem.TYPE_CONTACT, 100) {
            @Override
            public String getCellValue(ProjectListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_CONTACT);
            }
        };
        column19.setShow(false);
        column19.setMinimumColumnWidth(95);
        column19.setColumnSortable(false);
        column19.setFooterName("");
        columnConfigs.add(column19);

        ColumnDefinitionConfig column20 = new ColumnDefinitionConfig<ProjectListItem, String>(Property.get(Constants.LEADS, wfmStrings.relatedLead(), wfmStrings.lead()), RelationItem.TYPE_LEAD, 100) {
            @Override
            public String getCellValue(ProjectListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_LEAD);
            }
        };
        column20.setShow(false);
        column20.setMinimumColumnWidth(95);
        column20.setColumnSortable(false);
        column20.setFooterName("");
        columnConfigs.add(column20);

        ColumnDefinitionConfig column21 = new ColumnDefinitionConfig<ProjectListItem, String>(wfmStrings.relatedCrmAccount(), RelationItem.TYPE_CRM_ACCOUNT, 100) {
            @Override
            public String getCellValue(ProjectListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_CRM_ACCOUNT);
            }
        };
        column21.setShow(false);
        column21.setMinimumColumnWidth(95);
        column21.setColumnSortable(false);
        column21.setFooterName("");
        columnConfigs.add(column21);

        ColumnDefinitionConfig column22 = new ColumnDefinitionConfig<ProjectListItem, String>(Property.get(Constants.CASE_LIST, crmStrings.relatedCase(), wfmStrings.crmCase()), RelationItem.TYPE_CASE, 100) {
            @Override
            public String getCellValue(ProjectListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_CASE);
            }
        };
        column22.setShow(false);
        column22.setMinimumColumnWidth(95);
        column22.setColumnSortable(false);
        column22.setFooterName("");
        columnConfigs.add(column22);

        ColumnDefinitionConfig column23 = new ColumnDefinitionConfig<ProjectListItem, String>(Property.get(Constants.Opportunities, wfmStrings.relatedToOpportunity(), wfmStrings.opportunity()), RelationItem.TYPE_OPPORTUNITY, 100) {
            @Override
            public String getCellValue(ProjectListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_OPPORTUNITY);
            }
        };
        column23.setShow(false);
        column23.setMinimumColumnWidth(95);
        column23.setColumnSortable(false);
        column23.setFooterName("");
        columnConfigs.add(column23);

        ColumnDefinitionConfig column24 = new ColumnDefinitionConfig<ProjectListItem, String>(Property.get(Constants.EVENT_LIST, wfmStrings.relatedEvent(), wfmStrings.event()), RelationItem.TYPE_EVENT, 100) {
            @Override
            public String getCellValue(ProjectListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_EVENT);
            }
        };
        column24.setShow(false);
        column24.setMinimumColumnWidth(95);
        column24.setColumnSortable(false);
        column24.setFooterName("");
        columnConfigs.add(column24);

        ColumnDefinitionConfig column25 = new ColumnDefinitionConfig<ProjectListItem, String>(crmStrings.relatedTask(), RelationItem.TYPE_TASK, 100) {
            @Override
            public String getCellValue(ProjectListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_TASK);
            }
        };
        column25.setShow(false);
        column25.setMinimumColumnWidth(95);
        column25.setColumnSortable(false);
        column25.setFooterName("");
        columnConfigs.add(column25);

        //related issue
        ColumnDefinitionConfig column25_1 = new ColumnDefinitionConfig<ProjectListItem, String>(Property.get(Constants.ISSUE, wfmStrings.relatedIssue(), wfmStrings.issue()), RelationItem.TYPE_ISSUE, 100) {
            @Override
            public String getCellValue(ProjectListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_ISSUE);
            }
        };
        column25_1.setShow(false);
        column25_1.setMinimumColumnWidth(95);
        column25_1.setColumnSortable(false);
        column25_1.setFooterName("");
        columnConfigs.add(column25_1);
        //related employee
        ColumnDefinitionConfig column25_2 = new ColumnDefinitionConfig<ProjectListItem, String>(wfmStrings.relatedEmployee(), RelationItem.TYPE_EMPLOYEE, 100) {
            @Override
            public String getCellValue(ProjectListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_EMPLOYEE);
            }
        };
        column25_2.setShow(false);
        column25_2.setMinimumColumnWidth(95);
        column25_2.setColumnSortable(false);
        column25_2.setFooterName("");
        columnConfigs.add(column25_2);
        //related department
        ColumnDefinitionConfig column25_3 = new ColumnDefinitionConfig<ProjectListItem, String>(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.relatedDepartment(), wfmStrings.department()), RelationItem.TYPE_DEPARTMENT, 100) {
            @Override
            public String getCellValue(ProjectListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_DEPARTMENT);
            }
        };
        column25_3.setShow(false);
        column25_3.setMinimumColumnWidth(95);
        column25_3.setColumnSortable(false);
        column25_3.setFooterName("");
        columnConfigs.add(column25_3);
        //related client
        ColumnDefinitionConfig column25_4 = new ColumnDefinitionConfig<ProjectListItem, String>(Property.get(Constants.CLIENT_LIST, wfmStrings.relatedToClient()), ProjectListItem.PROJECT_RELATION_CLIENT, 100) {
            @Override
            public String getCellValue(ProjectListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_CLIENT);
            }
        };
        column25_4.setShow(false);
        column25_4.setMinimumColumnWidth(95);
        column25_4.setColumnSortable(false);
        column25_4.setFooterName("");
        columnConfigs.add(column25_4);
        //related supplier
        ColumnDefinitionConfig column25_5 = new ColumnDefinitionConfig<ProjectListItem, String>(Property.get(Constants.SUPPLIER_LIST, wfmStrings.relatedSupplier()), RelationItem.TYPE_SUPPLIER, 100) {
            @Override
            public String getCellValue(ProjectListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_SUPPLIER);
            }
        };
        column25_5.setShow(false);
        column25_5.setMinimumColumnWidth(95);
        column25_5.setColumnSortable(false);
        column25_5.setFooterName("");
        columnConfigs.add(column25_5);

        if (Utils.hasGenericAccess(GenericSettingsEnum.PROJECT_COST_IN_LISTING_ENABLED)) {
            ColumnDefinitionConfig column26 = new ColumnDefinitionConfig<ProjectListItem, String>("Planed Income", ProjectListItem.PLANED_INCOME, 90) {
                @Override
                public String getCellValue(ProjectListItem item) {
                    return item.getPlanedIncome() != null ? getTotalMoneyFormat(item.getPlanedIncome()) : "0.00";
                }
            };
            column26.setShow(false);
            column26.setMinimumColumnWidth(85);
            column26.setColumnSortable(false);
            column26.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
            columnConfigs.add(column26);

            ColumnDefinitionConfig column27 = new ColumnDefinitionConfig<ProjectListItem, String>("Income", ProjectListItem.INCOME, 90) {
                @Override
                public String getCellValue(ProjectListItem item) {
                    return item.getIncome() != null ? getTotalMoneyFormat(item.getIncome()) : "0.00";
                }
            };
            column27.setShow(false);
            column27.setMinimumColumnWidth(85);
            column27.setColumnSortable(false);
            column27.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
            columnConfigs.add(column27);

            ColumnDefinitionConfig column28 = new ColumnDefinitionConfig<ProjectListItem, String>("Planed Cost", ProjectListItem.PLANED_COST, 90) {
                @Override
                public String getCellValue(ProjectListItem item) {
                    return item.getPlanedCost() != null ? getTotalMoneyFormat(item.getPlanedCost()) : "0.00";
                }
            };
            column28.setShow(false);
            column28.setMinimumColumnWidth(85);
            column28.setColumnSortable(false);
            column28.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
            columnConfigs.add(column28);

            ColumnDefinitionConfig column29 = new ColumnDefinitionConfig<ProjectListItem, String>("Cost", ProjectListItem.COST, 90) {
                @Override
                public String getCellValue(ProjectListItem item) {
                    return item.getCost() != null ? getTotalMoneyFormat(item.getCost()) : "0.00";
                }
            };
            column29.setShow(false);
            column29.setMinimumColumnWidth(85);
            column29.setColumnSortable(false);
            column29.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
            columnConfigs.add(column29);

            ColumnDefinitionConfig column30 = new ColumnDefinitionConfig<ProjectListItem, String>("Planed Profit", ProjectListItem.PLANED_PROFIT, 90) {
                @Override
                public String getCellValue(ProjectListItem item) {
                    return item.getPlanedProfit() != null ? getTotalMoneyFormat(item.getPlanedProfit()) : "0.00";
                }
            };
            column30.setShow(false);
            column30.setMinimumColumnWidth(85);
            column30.setColumnSortable(false);
            column30.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
            columnConfigs.add(column30);

            ColumnDefinitionConfig column31 = new ColumnDefinitionConfig<ProjectListItem, String>("Profit", ProjectListItem.PROFIT, 90) {
                @Override
                public String getCellValue(ProjectListItem item) {
                    return item.getProfit() != null ? getTotalMoneyFormat(item.getProfit()) : "0.00";
                }
            };
            column31.setShow(false);
            column31.setMinimumColumnWidth(85);
            column31.setColumnSortable(false);
            column31.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
            columnConfigs.add(column31);

            ColumnDefinitionConfig column32 = new ColumnDefinitionConfig<ProjectListItem, String>("Difference", ProjectListItem.DIFFERENCE, 90) {
                @Override
                public String getCellValue(ProjectListItem item) {
                    return item.getDifference() != null ? getTotalMoneyFormat(item.getDifference()) + "%" : "0.00%";
                }
            };
            column32.setShow(false);
            column32.setMinimumColumnWidth(85);
            column32.setColumnSortable(false);
            columnConfigs.add(column32);
        }

        ColumnDefinitionConfig column33 = new ColumnDefinitionConfig<ProjectListItem, String>(wfmStrings.waitingForApproval(), ProjectListItem.WAITING_HOURS, 90) {
            @Override
            public String getCellValue(ProjectListItem item) {
                return item.getWaitingHours();
            }
        };
        column33.setShow(false);
        column33.setMinimumColumnWidth(85);
        column33.setColumnSortable(false);
        column33.setFooterName("");
        columnConfigs.add(column33);

        ColumnDefinitionConfig column34 = new ColumnDefinitionConfig<ProjectListItem, String>(projectStrings.rejectedHours(), ProjectListItem.REJECTED_HOURS, 90) {
            @Override
            public String getCellValue(ProjectListItem item) {
                return item.getRejectedHours();
            }
        };
        column34.setShow(false);
        column34.setMinimumColumnWidth(85);
        column34.setColumnSortable(false);
        column34.setFooterName("");
        columnConfigs.add(column34);

        ColumnDefinitionConfig column35 = new ColumnDefinitionConfig<ProjectListItem, String>(wfmStrings.createdBy(), ProjectListItem.CREATED_BY, 110) {
            @Override
            public String getCellValue(ProjectListItem item) {
                return item.getCreatedBy();
            }
        };
        column35.setShow(false);
        column35.setMinimumColumnWidth(105);
        column35.setColumnSortable(false);
        column35.setFooterName("");
        columnConfigs.add(column35);

        // Created date column

        ColumnDefinitionConfig CreatedDate = new ColumnDefinitionConfig<ProjectListItem, String>(wfmStrings.createdDate(), ProjectListItem.CREATED_DATE, 110) {
            @Override
            public String getCellValue(ProjectListItem item) {
                return DateUtils.formatInternal(item.getCreatedDate());
            }
        };
        CreatedDate.setShow(false);
        CreatedDate.setMinimumColumnWidth(105);
        CreatedDate.setColumnSortable(false);
        CreatedDate.setFooterName("");
        columnConfigs.add(CreatedDate);

        //Contract
        if (Utils.isEmployeeAssignmentEnable() && Utils.hasPermission(PermissionConstants.PM_CONTRACT_LIST)) {
            ColumnDefinitionConfig column36 = new ColumnDefinitionConfig<ProjectListItem, SimpleLink>(wfmStrings.contract(), ProjectListItem.CONTRACT, 110) {
                @Override
                public SimpleLink getCellValue(ProjectListItem item) {
                    if (item.getContractName() != null && !"".equals(item.getContractName())) {
                        return new SimpleLink(item.getContractName(), "contract|summary/" + item.getContractId(), item.getName(), item.getNumber());
                    }
                    return null;
                }
            };
            column36.setShow(false);
            column36.setMinimumColumnWidth(105);
            column36.setColumnSortable(false);
            column36.setFooterName("");
            columnConfigs.add(column36);
        }

        ColumnDefinitionConfig column37 = new ColumnDefinitionConfig<ProjectListItem, String>(wfmStrings.billable(), ProjectListItem.BILLABLE, 60) {
            @Override
            public String getCellValue(ProjectListItem item) {
                return item.getBillable() != null && item.getBillable() ? wfmStrings.yes() : wfmStrings.no();
            }
        };
        column37.setShow(false);
        column37.setMinimumColumnWidth(50);
        column37.setColumnSortable(false);
        column37.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnConfigs.add(column37);

        // Modified by
        ColumnDefinitionConfig column38 = new ColumnDefinitionConfig<ProjectListItem, String>(wfmStrings.modifiedBy(), ProjectListItem.MODIFIED_BY, 60) {
            @Override
            public String getCellValue(ProjectListItem item) {
                return item.getModifiedBy();
            }
        };

        column38.setShow(false);
        column38.setMinimumColumnWidth(50);
        column38.setColumnSortable(false);
        column38.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnConfigs.add(column38);


        // Modified date
        ColumnDefinitionConfig column39 = new ColumnDefinitionConfig<ProjectListItem, String>(wfmStrings.modifiedDate(), ProjectListItem.MODIFIED_DATE, 60) {
            @Override
            public String getCellValue(ProjectListItem item) {
                return DateUtils.formatInternal(item.getModifiedDate());
            }
        };

        column39.setShow(false);
        column39.setMinimumColumnWidth(105);
        column39.setColumnSortable(false);
        column39.setFooterName("");
        columnConfigs.add(column39);


        ColumnDefinitionConfig[] columnConfigArray = columnConfigs.toArray(new ColumnDefinitionConfig[]{});
        if (Utils.isPM()) {
            initCellEdit(columnConfigArray);
        }
        return columnConfigArray;
    }

    private GuideListingPanelDesign getListingPanelDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return hasPermissionToAdd() ? (Utils.isPM() ? ProjectListView.this::addNewItem : ProjectListView.this::opendQuickAddForm) : null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return hasPermissionToUpload() ? ProjectListView.this::openOploadPopup : null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return (data, callback) -> {
                            if (parentProjectId != null) {//  sub projects parent projectId
                                data.getCustomData().put(ProjectListItem.PROJECT_PARENT_ID, parentProjectId.toString());
                            }
                            if (relationID != null && !isClientView) {
                                data.setCustomDataPut(FacetFilterCutomField.RELATION_ID, relationID.toString());
                            }
                            if (isClientView) {
                                data.setCustomDataPut(FacetFilterCutomField.CLIENT_ID, relationID.toString());
                            }
                            if (relationType != null && !isClientView) {
                                data.setCustomDataPut(FacetFilterCutomField.RELATION_TYPE, relationType);
                            }
                            RbacService.App.get().getProjectFacetFilterData(data, new AbstractAsyncCallback<FacetFilterRpc>() {
                                @Override
                                public void failure(Throwable throwable) {
                                    callback.onFailure(throwable);
                                }

                                @Override
                                public void success(FacetFilterRpc facetFilterRpc) {
                                    callback.onSuccess(facetFilterRpc);
                                }
                            });
                        };
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return getProjectFilterContentConfigure();
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                return addProject();
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption option, MaterialDropDown menuContainer) {

                if (hasPermissionToUpload()) {

                    uploadPopup = new ImportFilePopUp(ImportTypeEnum.PROJECT, null);
                    uploadPopup.setSubmitCompleted(() -> {
                        if (uploadPopup.getObjectId() != null) {
                            goTo("importproject|add/add/" + uploadPopup.getObjectId());
                        }
                    });

                    ListItem li = new ListItem();

                    MaterialLink importItem = new MaterialLink(wfmStrings.importString());

                    MaterialDropDown items = new MaterialDropDown(importItem);
                    items.setHover(true);
                    items.setBelowOrigin(true);

                    if (Utils.hasPermission(PermissionConstants.PM_PROJECT_LIST_IMPORT_CSV_BUTTON)) {
                        ImportFileActionLink csvLink = new ImportFileActionLink();
                        csvLink.addClickHandler(ch -> openOploadPopup());
                        items.add(csvLink);
                    }

                    if (Utils.hasPermission(PermissionConstants.PM_PROJECT_LIST_IMPORT_MS_BUTTON)) {
                        ImportFileActionLink msProjectLink = new ImportFileActionLink("ficon--ms-project");
                        msProjectLink.setText("Ms Project file");
                        msProjectLink.addClickHandler(ch -> new ImportMPPFilePopup("/MSProjectUploadHandler"));
                        items.add(msProjectLink);
                    }
                    if (!items.getItems().isEmpty()) {
                        li.add(importItem);
                        li.add(items);
                        menuContainer.add(li);
                    }
                }

                option.initExport(null);
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(property.getPlural(wfmStrings.noProjectsTextAdmin(), wfmStrings.projects()));

                if (hasPermissionToAdd()) {
//                    if (parentProjectId != null) {
//                        message.setHref("project|add/add/" + parentProjectId);
                    message.setHref(clickEvent -> quickAddBox.show());
//                    } else {
//                        message.setHref("project|add/add");
//                    }
                    message.setTextBeforeLink(property.getPlural(wfmStrings.noProjectLink(), wfmStrings.projects()));
                }
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isShowCustomiseButton() {
                return Utils.hasPermission(PermissionConstants.PM_PROJECT_LIST_CUSTOMIZE_BUTTON);
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return Utils.hasPermission(PermissionConstants.PM_PROJECT_EDIT);
            }

            @Override
            public Integer getTypeParentId() {
                return parentProjectId;
            }

            @Override
            public ActionButton initTopToolBarMore() {
                if (Utils.hasPermission(PermissionConstants.PM_PROJECT_CHANGE_STATUS) && Utils.isPM()) {
                    final ActionButton more = new ActionButton(ActionButton.getMoreString(), ActionButton.Type.TOOLMENU);
                    more.ensureDebugId("projects_list_more_button");
                    more.addDomHandler(event -> {
                        MenuBar menu = getActionsForSelections();
                        menu.setAutoOpen(true);
                        more.setMenu(menu);
                    }, MouseOverEvent.getType());
                    return more;
                }
                return null;
            }
        };
    }

    private void openOploadPopup() {
        uploadPopup.open();
    }

    private boolean hasPermissionToUpload() {
        return Utils.hasPermission(PermissionConstants.PM_PROJECT_LIST_IMPORT_MS_BUTTON) && (Utils.isPM() || Utils.isHRMS());
    }

    private MenuBar getActionsForSelections() {
        if (selectedRows != null && selectedRows.size() > 0) {
            if (actions == null) {
                actions = new ContextMenu();
                actions.getMenuBar().setAutoOpen(true);
                //STATUS
                final MenuBar statuses = new MenuBar(true);
                statuses.setAutoOpen(true);
                statuses.addStyleName("my-menu");
                projectService.getProjectStatuses(new AbstractAsyncCallback<SelectItem[]>() {
                    public void success(final SelectItem[] result) {
                        int i = 0;
                        for (final SelectItem status : result) {
                            SafeHtml safeHtml = () -> "<span class='list-action-menu-icon'>" + status.getName() + "</span>";
                            MenuItem item = new MenuItem(safeHtml);
                            if (status.getDescription().equals(Constants.PS_COMPLETED)) {
                                item.ensureDebugId("changeStatus" + i++);
                                item.setCommand(() -> {

                                    final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                                    message.setTitle(wfmStrings.status());
                                    message.setMessage(projectStrings.projectStatusUpdate());
                                    message.addCloseHandler(new CloseHandler() {

                                        @Override
                                        public void onCancel() {
                                            updateStatus(selectedRows, status, false);
                                        }


                                        @Override
                                        public void onSubmit() {
                                            LoadingPanel.loading(true);
                                            updateStatus(selectedRows, status, true);
                                        }
                                    });
                                    message.open();
                                });
                            } else {
                                item.setCommand(() -> updateStatus(selectedRows, status, false));
                            }
                            statuses.addItem(item);
                        }
                    }
                });
                com.google.gwt.user.client.ui.MenuItem statusBar = new com.google.gwt.user.client.ui.MenuItem("<span class='list-action-menu-icon'>" + wfmStrings.changeStatus() + " </span>", true, statuses);
                statusBar.ensureDebugId("changeStatus");

                actions.getMenuBar().addItem(statusBar);

                MenuItem menuItem = new MenuItem("<span>" + wfmStrings.pdf() + "</span", true, (Command) () -> {
                    for (ProjectListItem project : selectedRows) {
                        Scheduler.get().scheduleFixedPeriod(() -> {
                            HashMap<String, String> parametersMap = new HashMap<>();
                            parametersMap.put("objectID", project.getObjectId().toString());
                            Utils.sendPDFOrExcelRequest(this, CommandConstants.PDF_URL + "/projectViewPDFHandler", parametersMap, "_blank");
                            return false;
                        }, 2000);
                    }
                });
                actions.getMenuBar().addItem(menuItem);

            }
            actions.getMenuBar().setAutoOpen(true);
            return actions.getMenuBar();
        } else {
            if (actionsEmpty == null) {
                actionsEmpty = new ContextMenu();
                actionsEmpty.getMenuBar().setAutoOpen(true);
                final com.google.gwt.user.client.ui.MenuItem alertItem = new com.google.gwt.user.client.ui.MenuItem("<span class='list-action-menu-icon'>" + wfmStrings.selectAnyItemToActivateBatchActions() + "</span>", true, (Command) () -> {

                });
                actionsEmpty.getMenuBar().addItem(alertItem);
            }
            actionsEmpty.getMenuBar().setAutoOpen(true);
            return actionsEmpty.getMenuBar();
        }
    }

    private void updateStatus(HashSet<ProjectListItem> projects, SelectItem status, boolean updateTasks) {
        LoadingPanel.loading(true);
        projectService.updateProjectStatus(projects, status, updateTasks, new AbstractAsyncCallback<Void>() {
            public void onFailure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void onSuccess(Void result) {
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PROJECT_EDIT, result, ProjectListView.this);
                listingTable.reloadPage();
                actions.getMenuBar().removeFromParent();
                LoadingPanel.loading(false);
                Info.show(property.getSingular(wfmStrings.messSuccessfullySaved(), wfmStrings.project()));
            }
        });
    }

    /**
     * <i>... Project Facet Filter Content Configure ...</i>
     * <br/>
     * <i>... Write by developer {Dilshod.T} ...</i>
     * <br/>
     * <i>... Created date {15:42 06/06/2011}  ...</i>
     *
     * @return
     */
    private FacetContentConfigure getProjectFilterContentConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(4, wfmStrings.filter());
        contentConfigure.addContentConfigure(FacetContentType.ProjectFacetFilter.getContentCode()[0], wfmStrings.manager(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrProjectListRepresenter.FIELD_PROJECT_MANAGER_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrProjectListRepresenter.FIELD_PROJECT_MANAGER_ID_NAME;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.ProjectFacetFilter.getContentCode()[17], wfmStrings.backupManagers(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrProjectListRepresenter.FIELD_PROJECT_BACKUP_MANAGER_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrProjectListRepresenter.FIELD_PROJECT_BACKUP_MANAGER_ID_NAME;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.ProjectFacetFilter.getContentCode()[1], Property.get(Constants.CLIENT_LIST, wfmStrings.customer()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_MULTI_CUSTOMER_TO_PROJECT) ? SolrProjectListRepresenter.FIELD_PROJECT_MULTI_CLIENT_ID : SolrProjectListRepresenter.FIELD_PROJECT_CLIENT_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_MULTI_CUSTOMER_TO_PROJECT) ? SolrProjectListRepresenter.FIELD_PROJECT_MULTI_CLIENT_ID_NAME : SolrProjectListRepresenter.FIELD_PROJECT_CLIENT_ID_NAME;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.ProjectFacetFilter.getContentCode()[2], wfmStrings.status(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrProjectListRepresenter.FIELD_PROJECT_STATUS_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrProjectListRepresenter.FIELD_PROJECT_STATUS_ID_CODE_NAME;
            }

            @Override
            public LocalizationType getLocalizationType() {
                return LocalizationType.REFERENCE;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.ProjectFacetFilter.getContentCode()[3], wfmStrings.assignees(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrProjectListRepresenter.FIELD_USER_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrProjectListRepresenter.FIELD_USER_ID_NAME;
            }
        });
        if (CompanyConstants.C1.equals(Utils.getEncryptedCompanyID()) ||
                CompanyConstants.C10520.equals(Utils.getEncryptedCompanyID()) || Utils.getHostURL().contains(HOST_AWS)) {
            contentConfigure.addContentConfigure(FacetContentType.ProjectFacetFilter.getContentCode()[4], Property.get(Constants.LOCATION_PROPERTY_OBJECTNAME, wfmStrings.location()), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrProjectListRepresenter.FIELD_PROJECT_LOCATION_ID;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrProjectListRepresenter.FIELD_PROJECT_LOCATION_ID_NAME;
                }
            });
        }

        contentConfigure.addContentConfigure(FacetContentType.ProjectFacetFilter.getContentCode()[5], Property.get(Constants.Contacts, crmStrings.relatedContact(), wfmStrings.contact()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_CONTACT;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_CONTACT;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.ProjectFacetFilter.getContentCode()[6], wfmStrings.relatedCrmAccount(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_CRM_ACCOUNT;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_CRM_ACCOUNT;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.ProjectFacetFilter.getContentCode()[7], Property.get(Constants.LEADS, wfmStrings.relatedLead(), wfmStrings.lead()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_LEAD;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_LEAD;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.ProjectFacetFilter.getContentCode()[8], Property.get(Constants.CASE_LIST, crmStrings.relatedCase(), wfmStrings.crmCase()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_CASE;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_CASE;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.ProjectFacetFilter.getContentCode()[9], Property.get(Constants.Opportunities, wfmStrings.relatedToOpportunity(), wfmStrings.opportunity()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_OPPORTUNITY;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_OPPORTUNITY;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.ProjectFacetFilter.getContentCode()[10], Property.get(Constants.EVENT_LIST, wfmStrings.relatedEvent(), wfmStrings.event()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_EVENT;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_EVENT;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.ProjectFacetFilter.getContentCode()[11], crmStrings.relatedTask(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_TASK;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_TASK;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        //related issue
        contentConfigure.addContentConfigure(FacetContentType.ProjectFacetFilter.getContentCode()[12], Property.get(Constants.ISSUE, wfmStrings.relatedIssue(), wfmStrings.issue()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_ISSUE;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_ISSUE;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        //related employee
        contentConfigure.addContentConfigure(FacetContentType.ProjectFacetFilter.getContentCode()[13], wfmStrings.relatedEmployee(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_EMPLOYEE;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_EMPLOYEE;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        //related department
        contentConfigure.addContentConfigure(FacetContentType.ProjectFacetFilter.getContentCode()[14], Property.get(Constants.DEPARTMENT_LIST, wfmStrings.relatedDepartment(), wfmStrings.department()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_DEPARTMENT;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_DEPARTMENT;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        //related client
        contentConfigure.addContentConfigure(FacetContentType.ProjectFacetFilter.getContentCode()[15], Property.get(Constants.CLIENT_LIST, wfmStrings.relatedClient()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_CLIENT;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_CLIENT;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        //related supplier
        contentConfigure.addContentConfigure(FacetContentType.ProjectFacetFilter.getContentCode()[16], Property.get(Constants.SUPPLIER_LIST, wfmStrings.relatedSupplier()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_SUPPLIER;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrProjectListRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_SUPPLIER;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        contentConfigure.addContentConfigureDateListBox(SolrProjectListRepresenter.FIELD_START_DATE, wfmStrings.startDate());
        contentConfigure.addContentConfigureDateListBox(SolrProjectListRepresenter.FIELD_DUE_DATE, wfmStrings.endDate());
        return contentConfigure;
    }

    private ActionButton addProject() {
        if (!hasPermissionToAdd()) {
            return null;
        }
        ActionButton newItem = getAddNewButton(ActionButton.Type.TOOLMENU);
        MenuBar menu = new MenuBar(true);

        MenuPopItem addNew = new MenuPopItem(property.getSingular(wfmStrings.project()));
        addNew.getElement().setId("gwt_debug-project--add");
        addNew.setCommand(() -> {
            addNewItem();
        });
        menu.addItem(addNew);

        MenuPopItem quick = new MenuPopItem(wfmStrings.quickAdd());
        quick.ensureDebugId("new_project");
        quick.setCommand(() -> quickAddBox.show());
        menu.addItem(quick);

        newItem.setMenu(menu);
        return newItem;
    }

    private void addNewItem() {
        if (parentProjectId != null) {
            SinksContainerFactory.entryPoint.onHistoryChanged("project|add/add/" + parentProjectId);
        } else {
            SinksContainerFactory.entryPoint.onHistoryChanged("project|add/add");
        }
    }

    private void opendQuickAddForm() {
        quickAddBox.show();
    }

    private boolean hasPermissionToAdd() {
        return Utils.hasPermission(PermissionConstants.PM_PROJECT_ADD);
//        && Utils.isPM() for accounting;
    }

    private ListingRequestProvider<ProjectListItem> getListingRequestProvider() {

        return (filterParametrs, callback) -> {
            if (parentProjectId != null) { //  sub projects parent projectId
                filterParametrs.setProjectId(parentProjectId);
            }
            if (RelationItem.TYPE_CRM_ACCOUNT.equals(relationType)) {
                filterParametrs.setClientId(relationID);
            }
            initProjectList(filterParametrs, callback, null);
        };
    }

    private void initProjectList(ListingFilterParameter filterParametrs, ListingCallback<ProjectListItem> callback, Span container) {
        projectService.getProjectList(filterParametrs, new AbstractAsyncCallback<ListResult<ProjectListItem>>() {
            public void failure(Throwable throwable) {
                if (callback != null) {
                    callback.onFailure(throwable);
                }
            }

            public void success(ListResult<ProjectListItem> projectList) {

                if (callback != null) {
                    if (Utils.hasGenericAccess(GenericSettingsEnum.PROJECT_COST_IN_LISTING_ENABLED)) {
                        listingTable.getFilterParametrs().setClearAndRecalculate(false);
                        listingTable.putFooterValue(ProjectListItem.PLANED_INCOME, "" + getTotalMoneyFormat(projectList.getObjectData().getPlanedIncome()));
                        listingTable.putFooterValue(ProjectListItem.INCOME, "" + getTotalMoneyFormat(projectList.getObjectData().getIncome()));

                        listingTable.putFooterValue(ProjectListItem.PLANED_COST, "" + getTotalMoneyFormat(projectList.getObjectData().getPlanedCost()));
                        listingTable.putFooterValue(ProjectListItem.COST, "" + getTotalMoneyFormat(projectList.getObjectData().getCost()));

                        listingTable.putFooterValue(ProjectListItem.PLANED_PROFIT, "" + getTotalMoneyFormat(projectList.getObjectData().getPlanedProfit()));
                        listingTable.putFooterValue(ProjectListItem.PROFIT, "" + getTotalMoneyFormat(projectList.getObjectData().getProfit()));

                        listingTable.putFooterValue(ProjectListItem.DIFFERENCE, "" + getTotalMoneyFormat(projectList.getObjectData().getDifference()) + "%");
                        callback.onSuccess(projectList);
                        listingTable.getPagingScrollTable().setFooterGenerated(true);
                        if (projectList.getList() == null || projectList.getList().size() <= 0) {
                            if (listingTable.getPagingScrollTable().getEmptyTableWidget().getParent() != null && listingTable.getPagingScrollTable().getOffsetHeight() > 58) {
                                listingTable.getPagingScrollTable().getEmptyTableWidget().getParent().setHeight((listingTable.getPagingScrollTable().getOffsetHeight() - 58) + "px");
                            }
                        }
//                        FlowPanel table = (FlowPanel) listingTable.getPagingScrollTable().getEmptyTableWidget().asWidget();
//                        if (table != null && HasVerticalAlignment.ALIGN_MIDDLE == table.getVerticalAlignment()) {
//                            table.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
//                        }
                    } else {
                        callback.onSuccess(projectList);
                    }
                }
                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (projectList.getTotal() != null && projectList.getTotal().intValue() > 0) {
                        statisticShortcut.setText(countFormat(projectList.getTotal()));
                        statisticShortcut.setClass("tab-label");
                    } else {
                        statisticShortcut.setText("");
                        statisticShortcut.removeStyleName("tab-label");
                    }
                }
            }
        });
    }

    private void initCellEdit(ColumnDefinitionConfig[] columnConfigs) {
        //
        // Project Status Cell Editor
        final DropDownCellEditor<String> statusCellEditor = new DropDownCellEditor<String>() {
            @Override
            protected String getValue() {
                return getListBox().getSelectedItem().getName();
            }

            @Override
            protected void setValue(String cellValue) {
                getListBox().setSelectedByValue(cellValue);
            }
        };
        statusCellEditor.getListBox().setWithoutNullLabel(true);
        columnConfigs[7].setCellEditor(statusCellEditor);
        columnConfigs[7].setCellChangesSave(new CellChange<ProjectListItem>() {
            @Override
            public void saveCell(ProjectListItem rowValue, String columnCodeName) {
                rowValue.setStatusId(statusCellEditor.getSelectItem().getId());
                rowValue.setStatusCode(statusCellEditor.getSelectItem().getDescription());
                saveProjectEditCellValue(rowValue, columnCodeName);
            }
        });

        projectService.getProjectStatuses(new AbstractAsyncCallback<SelectItem[]>() {
            public void success(SelectItem[] statuses) {
                statusCellEditor.getListBox().setItems(statuses);
            }
        });
        statusCellEditor.setVisible(Utils.hasPermission(PermissionConstants.PM_PROJECT_CHANGE_STATUS));

        // Project StartDate Cell Edit
        DateTimePickerCellEditor<String> startDateTimePickerCellEditor = new DateTimePickerCellEditor<String>() {
            @Override
            protected String getValue() {
                return DateUtils.format1(getDate());
            }

            @Override
            protected void setValue(String cellValue) {
                try {
                    Date date;
                    if (cellValue == null || "".equals(cellValue) || wfmStrings.notAvailable().equals(cellValue)) {
                        date = new Date();
                        setDefaultValue(true);
                    } else {
                        date = DateUtils.parse(cellValue);
                        setDefaultValue(false);
                    }
                    setDate(date, false);
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }
        };
        startDateTimePickerCellEditor.getDateTimePicker().getAllDayCheckBox().setVisible(false);
        columnConfigs[4].setCellEditor(startDateTimePickerCellEditor);
        columnConfigs[4].setCellChangesSave(new CellChange<ProjectListItem>() {
            @Override
            public void saveCell(ProjectListItem rowValue, String columnCodeName) {
                saveProjectEditCellValue(rowValue, columnCodeName);
            }
        });

        // Project EndDate Cell Edit
        DateTimePickerCellEditor<String> endDateTimePickerCellEditor = new DateTimePickerCellEditor<String>() {
            @Override
            protected String getValue() {
                return DateUtils.format1(getDate());
            }

            @Override
            protected void setValue(String cellValue) {
                try {
                    Date date;
                    if (cellValue == null || "".equals(cellValue) || "N/A".equals(cellValue)) {
                        date = new Date();
                        setDefaultValue(true);
                    } else {
                        date = DateUtils.parse(cellValue);
                        setDefaultValue(false);
                    }
                    setDate(date, false);
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }
        };
        endDateTimePickerCellEditor.getDateTimePicker().getAllDayCheckBox().setVisible(false);
        columnConfigs[5].setCellEditor(endDateTimePickerCellEditor);
        columnConfigs[5].setCellChangesSave(new CellChange<ProjectListItem>() {
            @Override
            public void saveCell(ProjectListItem rowValue, String columnCodeName) {
                saveProjectEditCellValue(rowValue, columnCodeName);
            }
        });
    }

    private void saveProjectEditCellValue(final ProjectListItem rowValue, final String columnCodeName) {
        boolean hasAccessToChange = !Utils.isLockCompletedProjecItems() || (Utils.isLockCompletedProjecItems() && !PS_CLOSED.equals(rowValue.getStatusCode()));

        if (!hasAccessToChange && !Utils.hasRole(Constants.ADMIN)) {
            Info.warn(wfmStrings.youDontHavePermission());
            return;
        }

        if (columnCodeName.equals(ProjectListItem.STATUS) && Constants.PS_COMPLETED.equals(rowValue.getStatusCode())) {
            final WfmMessageBox message = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
            message.setTitle(wfmStrings.status());
            message.setMessage(projectStrings.projectStatusUpdate());
            message.addCloseHandler(new CloseHandler() {

                @Override
                public void onCancel() {
                    projectService.saveProjectEditCellValue(rowValue, columnCodeName, false, new AbstractAsyncCallback<Void>() {
                        @Override
                        public void success(Void result) {
                            super.success(result);
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_DASHBOARD_PROJECT_REFRESH, result, ProjectListView.this);
                        }
                    });
                }


                @Override
                public void onSubmit() {
                    projectService.saveProjectEditCellValue(rowValue, columnCodeName, true, new AbstractAsyncCallback<Void>() {
                        @Override
                        public void success(Void result) {
                            super.success(result);
                            WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_DASHBOARD_PROJECT_REFRESH, result, ProjectListView.this);
                        }
                    });
                }
            });
            message.open();
        } else {
            projectService.saveProjectEditCellValue(rowValue, columnCodeName, false, new AbstractAsyncCallback<Void>() {
                @Override
                public void success(Void result) {
                    super.success(result);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_DASHBOARD_PROJECT_REFRESH, result, ProjectListView.this);
                }
            });
        }
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

    private String formatIntToTime(int totalActualTime) {
        int minute = totalActualTime % 60;
        int hour = (totalActualTime - minute) / 60;
        return "" + (hour < 10 ? "0" : "") + hour + ":" + (minute < 10 ? "0" : "") + minute;
    }

    public BigDecimal parseToBigDecimal(String text) {
        if (text != null && text.length() > 0) {
            if (text.equals("0.00")) {
                text = "0,00";
            }
            return BigDecimal.valueOf(numberFormat.parse(text));
        }
        return new BigDecimal("0.00");
    }

    public String getTotalMoneyFormat(BigDecimal bigDecimal) {

        return numberFormat.format(bigDecimal.setScale(2, RoundingMode.HALF_UP).doubleValue());
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        ListingFilterParameter fp = new ListingFilterParameter();
        if (RelationItem.TYPE_CRM_ACCOUNT.equals(relationType)) {
            fp.setClientId(relationID);
        } else {
            fp.setProjectId(parentProjectId);
        }
        fp.setLimit(1);
        initProjectList(fp, null, container);
    }

    @Override
    public String getPropertyCode() {
        return PROJECT;
    }
}
