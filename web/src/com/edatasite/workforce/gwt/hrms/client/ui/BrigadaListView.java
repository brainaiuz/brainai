package com.edatasite.workforce.gwt.hrms.client.ui;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ContextMenu;
import com.edatasite.workforce.gwt.core.client.ui.DateFormatException;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiSideNavBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.landing.HelpPanelGenerator;
import com.edatasite.workforce.gwt.core.client.ui.listTable.ImportFilePopUp;
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
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.DateTimePickerCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.DropDownCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CellChange;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsService;
import com.edatasite.workforce.gwt.hrms.client.rpc.HrmsServiceAsync;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectListItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


public class BrigadaListView extends BaseListView implements Constants {

    private static final NumberFormat numberFormat = NumberFormat.getFormat(",##0.00");
    private final HrmsServiceAsync brigadaService = HrmsService.App.get();
    public KpiSideNavBox quickAddBox;
    protected ContextMenu actions;
    protected ActionButton actionButton;
    protected ContextMenu actionsEmpty;
    private ListingPanel<ProjectListItem> listingTable;
    private final HorizontalPanel toolPanel = new HorizontalPanel();
    private Integer parentProjectId;
    private ImportFilePopUp uploadPopup;
    private HashSet<ProjectListItem> selectedRows;
    private String relationType;
    private Integer relationID;
    private int actionItemCount;
    private boolean isClientView;
    private final String from = "PM";

    public BrigadaListView() {
        super(BRIGADA_LIST);
        setDescription(Property.getPluralWithObjectCode(Constants.BRIGADA_LIST, wfmStrings.brigadas()));
        if (hasPermissionToAdd()) {
            setAddNew(() -> SinksContainerFactory.entryPoint.onHistoryChanged("brigada|add/add"));
        }
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
        listingTable = new GuideListingPanel(ListPanelType.BrendsListPanel, getColumnConfigs(), getListingRequestProvider(), getListingPanelDesign(), SelectionGrid.SelectionPolicy.CHECKBOX, -1, Utils.hasGenericAccess(GenericSettingsEnum.PROJECT_COST_IN_LISTING_ENABLED), null, null);
        listingTable.setCustomFieldsEditCellSaveChanges((rowValue, columnCodeName) -> saveProjectEditCellValue((ProjectListItem) rowValue, columnCodeName));

        final boolean hasPermissionXls = Utils.hasPermission(PermissionConstants.HRMS_BRIGADA_EXCEL);
        final boolean hasPermissionPdf = Utils.hasPermission(PermissionConstants.HRMS_BRIGADA_PDF);
        listingTable.setExcelListener(clickEvent -> {
            if (hasPermissionXls) {
                String excelURL = CommandConstants.COMMON_URL + "/downloadBrigadaListExcel";
                ListingFilterParameter filterParametrs = listingTable.getFilterParametrs();

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
            if (hasPermissionPdf) {
                String pdfURL = CommandConstants.PDF_URL + "/brigadaViewPDFHandler";
                ListingFilterParameter filterParametrs = listingTable.getFilterParametrs();
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
        registrationEvents();
        listingTable.addSelectionRowHandler(selected -> selectedRows = selected);

        return null;
    }

    private void registrationEvents() {
        int ON_PROJECT_ADD = WfmUiEventType.ON_PROJECT_ADD, ON_PROJECT_MEMBER_ADD = WfmUiEventType.ON_PROJECT_MEMBER_ADD;
        int ON_PROJECT_EDIT = WfmUiEventType.ON_PROJECT_EDIT, ON_PROJECT_DELETE = WfmUiEventType.ON_PROJECT_DELETE;
        if (parentProjectId != null) {
            ON_PROJECT_ADD = WfmUiEventType.ON_SUB_PROJECT_ADD;
            ON_PROJECT_MEMBER_ADD = WfmUiEventType.ON_SUB_PROJECT_MEMBER_ADD;
            ON_PROJECT_EDIT = WfmUiEventType.ON_SUB_PROJECT_EDIT;
            ON_PROJECT_DELETE = WfmUiEventType.ON_SUB_PROJECT_DELETE;
        }
        WfmUiEventsBus.addWfmUiListener(ON_PROJECT_ADD, BrigadaListView.this, (sender, args) -> listingTable.reloadPage());

        WfmUiEventsBus.addWfmUiListener(ON_PROJECT_MEMBER_ADD, BrigadaListView.this, (sender, args) -> listingTable.reloadPage());

        WfmUiEventsBus.addWfmUiListener(ON_PROJECT_EDIT, BrigadaListView.this, (sender, args) -> listingTable.reloadPage());
        WfmUiEventsBus.addWfmUiListener(ON_PROJECT_DELETE, BrigadaListView.this, (sender, args) -> listingTable.reloadPage());
    }

    private ColumnDefinitionConfig[] getColumnConfigs() {
        List<ColumnDefinitionConfig> columnConfigs = new ArrayList<>();

        // Action
        ColumnDefinitionConfig column1 = new ColumnDefinitionConfig<ProjectListItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final ProjectListItem rowValue) {
                actionItemCount = 0;
                final MenuBar actions = new MenuBar(true);
                if (hasPermissionToSummary()) {
                    MenuPopItem projectSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-project-small");
                    projectSummary.ensureDebugId("brigadaSummary");
                    projectSummary.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("brigada|summary/" + rowValue.getObjectId()));
                    actionItemCount++;
                    actions.addItem(projectSummary);
                }

                if (hasPermissionToEdit()) {
                    final MenuPopItem projectEdit = new MenuPopItem(wfmStrings.edit(), "icon-project-edit-small");
                    projectEdit.ensureDebugId("editBrigada");
                    projectEdit.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("brigada|edit/" + rowValue.getObjectId() + "/" + parentProjectId, rowValue.getNumber(), rowValue.getName()));
                    actionItemCount++;
                    actions.addItem(projectEdit);
                }

                if (hasPermissionToDelete()) {
                    final MenuPopItem removeItem = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile");
                    removeItem.ensureDebugId("delete");
                    removeItem.setCommand(() -> {
                        deleteBrigada(rowValue.getObjectId());
                    });
                    actionItemCount++;
                    actions.addItem(removeItem);
                }

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(actions);
                return hasPermissionToEdit() || hasPermissionToDelete() || hasPermissionToSummary() ? toolItem.getAction() : null;
            }
        };
        column1.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column1.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column1.setColumnSortable(false);
        column1.setFooterName("");
        columnConfigs.add(column1);


        // Shift Number
        ColumnDefinitionConfig column2 = new ColumnDefinitionConfig<ProjectListItem, Widget>(wfmStrings.number(), ProjectListItem.NUMBER, 60) {
            @Override
            public Widget getCellValue(ProjectListItem item) {
                final boolean hasAccessToChange = !Utils.isLockCompletedProjecItems() || (Utils.isLockCompletedProjecItems() && !PS_CLOSED.equals(item.getStatusCode()));
                Label label = new Label(item.getNumber());
                label.setStyleName("uploadLinkStyle2");
                if (Utils.hasPermission(PermissionConstants.HRMS_BRIGADA_SUMMARY)) {
                    label.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("brigada|summary/" + item.getObjectId()));
                }
                if (Utils.isPM() || Utils.isHRMS()) {

                } else {
                    String moduleName = from.equals("HRMS") ? "Hrms.html" : "ProjectManagement.html";

                }
                return label;
            }
        };
        column2.setMinimumColumnWidth(60);
        column2.setFooterName("");
        column2.setShow(true);
        column2.setColumnSortable(true);
        columnConfigs.add(column2);

        // Shift Name
        ColumnDefinitionConfig column3 = new ColumnDefinitionConfig<ProjectListItem, Widget>(wfmStrings.name(), ProjectListItem.NAME, 140) {
            @Override
            public Widget getCellValue(ProjectListItem rowValue) {
                final boolean hasAccessToChange = !Utils.isLockCompletedProjecItems() || (Utils.isLockCompletedProjecItems() && !PS_CLOSED.equals(rowValue.getStatusCode()));
                Label label = new Label(rowValue.getName());
                label.setStyleName("uploadLinkStyle2");
//                if (Utils.isPM() || Utils.isHRMS()) {
                if (Utils.hasPermission(PermissionConstants.HRMS_BRIGADA_SUMMARY)) {
                    label.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("brigada|summary/" + rowValue.getObjectId()));
                }
                return label;
            }
        };
        column3.setMinimumColumnWidth(135);
        column3.setFooterName("");
        columnConfigs.add(column3);
        column3.setShow(true);


        // Shift Manager
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
        column5.setShow(true);
        columnConfigs.add(column5);

        // Shift Bacup Manager
        ColumnDefinitionConfig column6 = new ColumnDefinitionConfig<ProjectListItem, String>(wfmStrings.backupManagers(), ProjectListItem.BACKUP_MANAGER, 120) {
            @Override
            public String getCellValue(ProjectListItem rowValue) {
                return rowValue.getBackupManager();
            }
        };
        column6.setMinimumColumnWidth(115);
        column6.setShow(true);
        column6.setFooterName("");
        column6.setColumnSortable(false);
        columnConfigs.add(column6);


        // Head Count
        ColumnDefinitionConfig column7 = new ColumnDefinitionConfig<ProjectListItem, Integer>(wfmStrings.headCount(), ProjectListItem.HEAD_COUNT, 80) {
            @Override
            public Integer getCellValue(ProjectListItem rowValue) {
                return rowValue.getHeadCount();
            }
        };
        column7.setShow(true);
        column7.setMinimumColumnWidth(75);
        column7.setColumnSortable(false);
        column7.setFooterName("");
        column7.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnConfigs.add(column7);

        // Status
        ColumnDefinitionConfig column8 = new ColumnDefinitionConfig<ProjectListItem, String>(wfmStrings.status(), ProjectListItem.STATUS, 80) {
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
        column8.setMinimumColumnWidth(75);
        column8.setFooterName("");
        column8.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnConfigs.add(column8);


        // Creator
        ColumnDefinitionConfig column9 = new ColumnDefinitionConfig<ProjectListItem, String>(wfmStrings.createdBy(), ProjectListItem.CREATED_BY, 80) {
            @Override
            public String getCellValue(ProjectListItem rowValue) {
                return rowValue.getCreatedBy();
            }

            @Override
            public void setCellValue(ProjectListItem rowValue, String cellValue) {
                rowValue.setCreatedBy(cellValue);
                saveCellValue(rowValue);
            }
        };
        column9.setMinimumColumnWidth(75);
        column9.setFooterName("");
        column9.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnConfigs.add(column9);


        // createdDate
        ColumnDefinitionConfig column10 = new ColumnDefinitionConfig<ProjectListItem, String>(wfmStrings.createdDate(), ProjectListItem.CREATED_DATE, 80) {
            @Override
            public String getCellValue(ProjectListItem rowValue) {
                return DateUtils.formatInternal(rowValue.getCreatedDate());
            }
        };
        column10.setMinimumColumnWidth(75);
        column10.setFooterName("");
        column10.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnConfigs.add(column10);

        // modifiedBy
        ColumnDefinitionConfig column11 = new ColumnDefinitionConfig<ProjectListItem, String>(wfmStrings.modifiedBy(), ProjectListItem.MODIFIED_BY, 80) {
            @Override
            public String getCellValue(ProjectListItem rowValue) {
                return rowValue.getModifiedBy();
            }
        };
        column11.setMinimumColumnWidth(75);
        column11.setFooterName("");
        column11.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnConfigs.add(column11);

        // modified date
        ColumnDefinitionConfig column12 = new ColumnDefinitionConfig<ProjectListItem, String>(wfmStrings.modifiedDate(), ProjectListItem.MODIFIED_DATE, 80) {
            @Override
            public String getCellValue(ProjectListItem rowValue) {
                return DateUtils.formatInternal(rowValue.getModifiedDate());
            }
        };
        column12.setMinimumColumnWidth(75);
        column12.setFooterName("");
        column12.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        columnConfigs.add(column12);

        ColumnDefinitionConfig[] columnConfigArray = columnConfigs.toArray(new ColumnDefinitionConfig[]{});
        if (Utils.isPM()) {
            initCellEdit(columnConfigArray);
        }
        return columnConfigArray;
    }

    private void deleteBrigada(Integer id) {
        brigadaService.deleteBrigada(id, new AbstractAsyncCallback<Void>() {
            public void failure(Throwable throwable) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Void result) {
                LoadingPanel.loading(false);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_PROJECT_DELETE, result, BrigadaListView.this);
                Info.show(property.getSingular(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.project()), Info.Type.INFO);
            }
        });
    }

    private GuideListingPanelDesign getListingPanelDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return hasPermissionToAdd() ? BrigadaListView.this::addNewItem : BrigadaListView.this::opendQuickAddForm;
            }

            @Override
            public Command getUploadButtonCommand() {
                return hasPermissionToUpload() ? BrigadaListView.this::openOploadPopup : null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {  // remove this

                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return null;
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return null;
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (hasPermissionToAdd()) {
                    ActionButton addnew = getAddNewButton();
                    addnew.addClickHandler(event -> SinksContainerFactory.entryPoint.onHistoryChanged("brigada|add/add"));
                    return addnew;
                } else {
                    return null;
                }
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption option, MaterialDropDown menuContainer) {


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

    private boolean hasPermissionToAdd() {
        return Utils.hasPermission(PermissionConstants.HRMS_BRIGADA_ADD);
    }

    private boolean hasPermissionToEdit() {
        return Utils.hasPermission(PermissionConstants.HRMS_BRIGADA_EDIT);
    }

    private boolean hasPermissionToDelete() {
        return Utils.hasPermission(PermissionConstants.HRMS_BRIGADA_DELETE);
    }

    private boolean hasPermissionToSummary() {
        return Utils.hasPermission(PermissionConstants.HRMS_BRIGADA_SUMMARY);
    }

    private void openOploadPopup() {
        uploadPopup.open();
    }

    private boolean hasPermissionToUpload() {
        return Utils.hasPermission(PermissionConstants.PM_PROJECT_LIST_IMPORT_BUTTON) && Utils.isPM();
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

    private FacetContentConfigure getProjectFilterContentConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(4, wfmStrings.filter());

        return contentConfigure;
    }

    private ActionButton addProject() {
        if (!hasPermissionToAdd()) {
            return null;
        }
        ActionButton newItem = getAddNewButton(ActionButton.Type.TOOLMENU);
        MenuBar menu = new MenuBar(true);

        MenuPopItem addNew = new MenuPopItem(property.getSingular("brigada"));
        addNew.getElement().setId("gwt_debug-project--add");
        addNew.setCommand(() -> {
            addNewItem();
        });
        menu.addItem(addNew);

        MenuPopItem quick = new MenuPopItem(wfmStrings.quickAdd());
        quick.ensureDebugId("new_brigada");
        quick.setCommand(() -> quickAddBox.open());
//        menu.addItem(quick);

        newItem.setMenu(menu);
        return newItem;
    }

    private void addNewItem() {
        if (parentProjectId != null) {
            SinksContainerFactory.entryPoint.onHistoryChanged("brigada|add/add/" + parentProjectId);
        } else {
            SinksContainerFactory.entryPoint.onHistoryChanged("brigada|add/add");
        }
    }

    private void opendQuickAddForm() {
        quickAddBox.open();
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
        brigadaService.getBrigadaList(filterParametrs, new AbstractAsyncCallback<ListResult<ProjectListItem>>() {
            public void failure(Throwable throwable) {
                if (callback != null) {
                    callback.onFailure(throwable);
                }
            }

            public void success(ListResult<ProjectListItem> projectList) {

                if (callback != null) {
                    if (Utils.hasGenericAccess(GenericSettingsEnum.PROJECT_COST_IN_LISTING_ENABLED)) {
                        listingTable.getFilterParametrs().setClearAndRecalculate(false);

                        callback.onSuccess(projectList);
                        listingTable.getPagingScrollTable().setFooterGenerated(true);
                        if (projectList.getList() == null || projectList.getList().size() <= 0) {
                            if (listingTable.getPagingScrollTable().getEmptyTableWidget().getParent() != null && listingTable.getPagingScrollTable().getOffsetHeight() > 58) {
                                listingTable.getPagingScrollTable().getEmptyTableWidget().getParent().setHeight((listingTable.getPagingScrollTable().getOffsetHeight() - 58) + "px");
                            }
                        }

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
            //TODO BRIGADA EDIT
            @Override
            public void saveCell(ProjectListItem rowValue, String columnCodeName) {
                saveProjectEditCellValue(rowValue, columnCodeName);
            }
        });


        columnConfigs[5].setCellChangesSave(new CellChange<ProjectListItem>() {
            //TODO BRIGADA EDIT
            @Override
            public void saveCell(ProjectListItem rowValue, String columnCodeName) {
                saveProjectEditCellValue(rowValue, columnCodeName);
            }
        });
    }

    //TODO BRIGADA SAVE
    private void saveProjectEditCellValue(final ProjectListItem rowValue, final String columnCodeName) {
        boolean hasAccessToChange = !Utils.isLockCompletedProjecItems() || (Utils.isLockCompletedProjecItems() && !PS_CLOSED.equals(rowValue.getStatusCode()));

        if (!hasAccessToChange && !Utils.hasRole(Constants.ADMIN)) {
            Info.warn(wfmStrings.youDontHavePermission());
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


//    public BigDecimal parseToBigDecimal(String text) {
//        if (text != null && text.length() > 0) {
//            if (text.equals("0.00")) {
//                text = "0,00";
//            }
//            return new BigDecimal(numberFormat.parse(text));
//        }
//        return new BigDecimal("0.00");
//    }

    @Override
    public String getPropertyCode() {
        return BRIGADA;
    }
}
