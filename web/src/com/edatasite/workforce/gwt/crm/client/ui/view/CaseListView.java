package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.core.client.BaseListView;
import com.edatasite.workforce.gwt.core.client.CRMUtils;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Property;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.ReferenceParentEnum;
import com.edatasite.workforce.gwt.core.client.localization.Reference;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneServiceAsync;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.ConvertItem;
import com.edatasite.workforce.gwt.core.client.rpc.FormProperty;
import com.edatasite.workforce.gwt.core.client.rpc.LocalizationType;
import com.edatasite.workforce.gwt.core.client.rpc.PropertyItem;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.kanbanItemSettings.KanbanItemColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.kanbanItemSettings.KanbanItemSettingEnum;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrCaseRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.CompanyConstants;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.ContextMenu;
import com.edatasite.workforce.gwt.core.client.ui.DefaultNoItemsMessage;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.SimpleLink;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmWindow;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.DatePeriodFacetContent;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.landing.HelpPanelGenerator;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.ColumnColor;
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
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.DropDownCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.crm.client.localization.CrmMessages;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMServiceAsync;
import com.edatasite.workforce.gwt.crm.client.rpc.CaseList;
import com.edatasite.workforce.gwt.crm.client.ui.view.kanban.CaseMaterialCard;
import com.edatasite.workforce.gwt.crm.client.ui.view.quickadd.CrmQuickAdd;
import com.edatasite.workforce.gwt.crmcase.client.rpc.CaseItem;
import com.edatasite.workforce.gwt.materialkanban.client.KanbanBoard;
import com.edatasite.workforce.gwt.materialkanban.client.KanbanBoardDesign;
import com.edatasite.workforce.gwt.materialkanban.client.KanbanDataLoader;
import com.edatasite.workforce.gwt.materialkanban.client.KanbanDataRenderer;
import com.edatasite.workforce.gwt.materialkanban.client.rpc.KanbanService;
import com.edatasite.workforce.gwt.materialkanban.client.rpc.KanbanServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.gen2.table.client.InlineCellEditor;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 07-Jul-2009
 * Time: 18:11:32
 * To change this template use File | Settings | File Templates.
 */
public class CaseListView extends BaseListView implements Constants {
    private static final Reference reference = Reference.App.get();
    private static final CrmStrings crmStrings = CrmStrings.App.get();
    private static final CrmMessages crmMessages = CrmMessages.App.get();
    private final CRMServiceAsync crmService = CRMService.App.get();
    protected KanbanServiceAsync kanbanService = KanbanService.App.get();
    private static final AllInOneServiceAsync allInOneService = AllInOneService.App.get();
    private ListingPanel listPanel;
    protected HashSet<CaseItem> selectedItems = new HashSet<>();
    protected ContextMenu actions;
    private ContextMenu emptyActions = null;
    private AssigneePopup assigneePopup;
    private KpiCheckBox hasAttachment;
    private KpiCheckBox inTrach;
    private boolean checkedTrash = false;
    private Integer relationID;
    private String relationType;
    private String relationName;
    private CaseItem currentItem = null;
    private Integer webFormId;
    private final boolean isClientView = Utils.hasRoles(CLIENT) && !Utils.hasCrmRole();
    private LinkedHashMap<String, FormProperty> formProperty;

    public CaseListView() {
        super(CASE_LIST);
        setDescription(property.getPlural(wfmStrings.cases()));
        if ((Utils.hasPermission(PermissionConstants.CRM_CASE_QUICK_ADD) && Utils.hasPermission(PermissionConstants.ADD_NEW_CASE)) || Utils.hasPermission(PermissionConstants.ADD_NEW_CASE)) {
            setAddNew(() -> goTo("case|add/add/"));
        } else if (Utils.hasPermission(PermissionConstants.CRM_CASE_QUICK_ADD)) {
            setAddNew(() -> new CrmQuickAdd(LayoutRPC.CASE_FORM, RelationItem.newEventRelation(relationType, relationID, relationName)));
        }
    }

    public CaseListView(Integer webFormId) {
        super(CASE_LIST);
        setDescription(property.getPlural(crmStrings.submittedPlusElement(), wfmStrings.cases()));
        this.webFormId = webFormId;
        if (Utils.hasPermission(PermissionConstants.ADD_NEW_CASE)) {
            setAddNew(() -> new CrmQuickAdd(LayoutRPC.CASE_FORM, RelationItem.newEventRelation(relationType, relationID, relationName)));
        }
    }

    public CaseListView(Integer relationID, String relationType) {
        this();
        this.relationID = relationID;
        this.relationType = relationType;
        if (Utils.hasPermission(PermissionConstants.ADD_NEW_CASE)) {
            setAddNew(() -> new CrmQuickAdd(LayoutRPC.CASE_FORM, RelationItem.newEventRelation(relationType, relationID, relationName)));
        }
    }

    protected Widget onInitialize() {
        CommonService.App.get().getFormProperty(LayoutRPC.CASE_FORM, new AbstractAsyncCallback<LinkedHashMap<String, FormProperty>>() {
            @Override
            public void failure(Throwable throwable) {
                initilazation();
            }

            @Override
            public void success(LinkedHashMap<String, FormProperty> result) {
                formProperty = result;
                initilazation();
            }
        });
        return null;
    }

    private Widget initilazation() {
        assigneePopup = new AssigneePopup(RelationItem.TYPE_CASE);
        assigneePopup.setListRefresh(this::refresh);
        listPanel = new GuideListingPanel(ListPanelType.CaseListPanel, getColumns(), getRequestProvider(),
                getListingPanelDesign(), SelectionGrid.SelectionPolicy.CHECKBOX);
        listPanel.setCustomFieldsEditCellSaveChanges((rowValue, columnCodeName) -> saveCaseEditCellValue((CaseItem) rowValue, columnCodeName));
        listPanel.setPDFListener(clickEvent -> {
            if (listPanel.getItemCount() > 1000) {
                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
            }
            String pdfURL = CommandConstants.PDF_URL + "/caseListPDFHandler";
            ListingFilterParameter fp = listPanel.getFilterParametrs();
            fp.setPropertyCode(getPropertyCode());
            if (fp == null) {
                fp = new ListingFilterParameter();
            }
            fp.setHasOnlyClientAccess(isClientView);
            listPanel.callListPDF(pdfURL, fp);
        });
        listPanel.getPdfVersion().ensureDebugId("crmCasePDF");
        listPanel.setExcelListener(clickEvent -> {
            if (listPanel.getItemCount() > 1000) {
                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
            }
            String excelURL = CommandConstants.COMMON_URL + "/downloadCasesListExcel";
            ListingFilterParameter fp = listPanel.getFilterParametrs();
            fp.setPropertyCode(getPropertyCode());
            if (fp == null) {
                fp = new ListingFilterParameter();
            }
            fp.setHasOnlyClientAccess(isClientView);
            listPanel.callListExcel(excelURL, fp);
        });
        listPanel.getXlsVersion().ensureDebugId("crmCaseExcel");
        listPanel.addSelectionRowHandler(selectedRows -> {
            selectedItems = (HashSet<CaseItem>) selectedRows;
            actions = null;
        });
        getRelationName(relationID, relationType);
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CASE_ADD, CaseListView.this, (sender, args) -> {
            refresh();
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CASE_REPLY_TO_REPORTER, CaseListView.this, (sender, args) -> refresh());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CASE_FORWARDED, CaseListView.this, (sender, args) -> refresh());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CASE_DELETE, CaseListView.this, (sender, args) -> refresh());
        add(listPanel);
        KanbanBoard<CaseItem> kanbanBoard = new KanbanBoard<CaseItem>(ListPanelType.CaseKanbanPanel, getKanbanDataLoader(), getKanbanBoardDesign()) {
            @Override
            public Widget getColumnAddButton(SelectItem columnMetadata) {
                if (Utils.hasPermission(PermissionConstants.ADD_NEW_CASE)) {
                    MaterialLink addCaseLink = new MaterialLink();
                    addCaseLink.setStyleName("wg_canban__add-card");
                    Icon plus = new Icon();
                    plus.setStyleName("ficon--plus");
                    addCaseLink.add(plus);
                    addCaseLink.addClickHandler((ClickEvent click) -> new CrmQuickAdd(LayoutRPC.CASE_FORM, columnMetadata.getId(),
                            RelationItem.newEventRelation(relationType, relationID, relationName)));
                    return addCaseLink;
                }
                return super.getColumnAddButton(columnMetadata);
            }
        };
        kanbanBoard.setKanbanItemSettingsType(KanbanItemSettingEnum.CASE_ITEM_SETTINGS);
        listPanel.setKanbanBoardView(kanbanBoard);
        return null;
    }

    private void refresh() {
        if (listPanel.isListingPage()) {
            listPanel.reloadPage();
        } else {
            listPanel.requestKanbanData();
        }
    }

    public FlowPanel getHelpContainer() {
        return HelpPanelGenerator.getHelpPanel(PermissionConstants.CRM_CONTEXT, PermissionConstants.CRM_CASES_LIST);
    }

    private ColumnDefinitionConfig[] getColumns() {
        final ArrayList<CustomColumnDefinitionConfig> columns = new ArrayList<>();

        ColumnDefinitionConfig column = new ColumnDefinitionConfig<CaseItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final CaseItem item) {
                int actionItemCount = 0;
                final MenuBar menuBar = new MenuBar(true);
                menuBar.setAutoOpen(true);

                /*
                 * Summery Menu Item
                 * */
                final MenuPopItem caseSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-case-view-small", () -> SinksContainerFactory.entryPoint.onHistoryChanged("case|summary/" + item.getObjectId() + "/" + item.getTrackerID(), item.getCaseNumber(), item.getSubject()));
                caseSummary.ensureDebugId("caseView");
                actionItemCount++;
                menuBar.addItem(caseSummary);
                if (Utils.hasPermission(PermissionConstants.CRM_EDIT_CASE)) {
                    MenuPopItem caseEdit = new MenuPopItem(wfmStrings.edit(), "icon-edit", () -> SinksContainerFactory.entryPoint.onHistoryChanged("case|add/add/" + item.getObjectId(), item.getCaseNumber(), item.getSubject()));
                    caseEdit.ensureDebugId("editCase");
                    actionItemCount++;
                    menuBar.addItem(caseEdit);
                }
                if (Utils.hasPermission(PermissionConstants.CRM_COPY_CASE)) {
                    MenuPopItem copyCase = new MenuPopItem(wfmStrings.copy(), "icon-copy", () -> SinksContainerFactory.entryPoint.onHistoryChanged("case|add/add/" + item.getObjectId() + "/" + COPY));
                    copyCase.ensureDebugId("crmCaseCopy");
                    actionItemCount++;
                    menuBar.addItem(copyCase);
                }
                if (!(isClientView || checkedTrash)) {
                    if (Utils.hasPermission(PermissionConstants.CRM_CHANGE_STATUS_CASE, PermissionConstants.CRM_EDIT_CASE)) {
                        final MenuPopItem changeStatus = new MenuPopItem(wfmStrings.changeStatus(), "icon-change-status");
                        changeStatus.ensureDebugId("changeStatus");

                        final MenuBar statuses = new MenuBar(true);
                        statuses.setAutoOpen(true);
                        if (item.getStatusItems() != null) {
                            for (final SelectItem status : item.getStatusItems()) {
                                if (item.getStatus() == null || !status.getId().equals(item.getStatus().getObjectID())) {
                                    final MenuPopItem mItem = new MenuPopItem(status.getName());
                                    mItem.ensureDebugId("status_item_" + status.getName());
                                    mItem.setCommand(() -> {
                                        mItem.closeAll(menuBar);

                                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.CONFIRM, Action.YesNo, true);
                                        messageBox.setTitle(wfmStrings.confirm());
                                        messageBox.setMessage(crmStrings.areYouWantToChangeStatus() + "&nbsp <font color='#15428B'><b> \"" + status.getName() + "\"</b></font> ?");
                                        messageBox.addCloseHandler(new CloseHandler() {
                                            @Override
                                            public void onSubmit() {
                                                if (status.isSelected()) {
                                                    new CaseStatusModal(item.getObjectId(), status.getId(), false);
                                                } else {
                                                    changeCaseStatus(item.getObjectId(), status.getId());
                                                }
                                            }
                                        });
                                        messageBox.open();
                                    });
                                    statuses.addItem(mItem);
                                }
                            }
                        }
                        changeStatus.setSubMenu(statuses);
                        actionItemCount++;
                        menuBar.addItem(changeStatus);
                    }
                    if (Utils.hasPermission(PermissionConstants.CRM_CHANGE_ASSIGNEE_CASE, PermissionConstants.CRM_EDIT_CASE)) {
                        MenuPopItem changeAssignee = new MenuPopItem(crmStrings.changeAssignee(), "icon-change-status", () -> {
                            assigneePopup.getItemIDs().clear();
                            assigneePopup.addItemID(item.getObjectId());
                            assigneePopup.open();
                        });
                        changeAssignee.ensureDebugId("changeAssignee");
                        actionItemCount++;
                        menuBar.addItem(changeAssignee);
                    }
                    if (Utils.hasPermission(PermissionConstants.CRM_TASKS_ADD)) {
                        MenuPopItem convertToTask = new MenuPopItem(Property.get(Constants.TASK, wfmStrings.addMess(), wfmStrings.task()), "icon-addtask-small");
                        convertToTask.ensureDebugId("createTask");
                        convertToTask.setCommand(() -> goTo("task|add/add/" + CrmConstants.CRM_TASK + "/" + item.getObjectId() + "/" + RelationItem.TYPE_CASE
                                + "/" + item.getSubject().replace("\\/", "\\ ") + "/" + item.getSubject().replaceAll("\\/", "\\ ")
                                + "/" + CONVERT_TO_TASK_FROM_CASE));
                        actionItemCount++;
                        menuBar.addItem(convertToTask);
                    }
                }
                if (Utils.hasPermission(PermissionConstants.CRM_CLOSE_CASE)) {
                    MenuPopItem caseClose = new MenuPopItem(property.getSingular(crmStrings.closeCase(), wfmStrings.caseID()), "icon-case-small", () -> new CloseCaseView(item, () -> refresh()));
                    caseClose.ensureDebugId("closeCase");
                    actionItemCount++;
                    menuBar.addItem(caseClose);
                }

                PropertyItem propertyItem = Utils.getProperTy(Constants.CASE_LIST);
                if (propertyItem != null && propertyItem.getConvertItems() != null && propertyItem.getConvertItems().length > 0) {
                    MenuPopItem convertMenuPopItem = new MenuPopItem(wfmStrings.convert(), "icon-add-green");

                    MenuBar convertMenu = new MenuBar(true);
                    convertMenu.setAutoOpen(true);
                    int convertItems = 0;
                    for (ConvertItem convertItem : propertyItem.getConvertItems()) {
                        if (convertItem != null) {
                            convertItems = getConvertItems(item, menuBar, convertMenu, convertItems, convertItem);
                        }
                    }

                    if (convertItems > 0) {
                        convertMenuPopItem.setSubMenu(convertMenu);
                        actionItemCount++;
                        menuBar.addItem(convertMenuPopItem);
                    }
                }

                if (Utils.hasPermission(PermissionConstants.CRM_REMOVE_CASE)) {
                    MenuPopItem removeCase = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile", () -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                CRMService.App.get().deleteCase(item.getObjectId(), new AbstractAsyncCallback() {
                                    @Override
                                    public void failure(Throwable caught) {
                                        Info.show(wfmStrings.error());
                                    }

                                    @Override
                                    public void success(Object result) {
                                        WfmWindow.confirm(property.getSingular(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.caseID()));
                                        refresh();
                                    }
                                });
                            }
                        });
                        messageBox.open();
                    });
                    removeCase.ensureDebugId("deleteCase");
                    actionItemCount++;
                    menuBar.addItem(removeCase);
                }

                /*final MenuPopItem timer = new MenuPopItem(wfmStrings.wfmTimer(), item.isTimerIsStarted() ? "icon-clock-active" : "icon-clock", () -> {
//                    SinksContainerFactory.entryPoint.onHistoryChanged("caseTimer|summary/" + item.getObjectId().toString())
                    MainLayout.get().getRightBar().setTimerData(item.getObjectId(), CRM_CASE, null);
                });
                timer.ensureDebugId("wfmTimer");
                actionItemCount++;
                menuBar.addItem(timer);*/

                final ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);

                return toolItem.getAction();
            }
        };
        column.setColumnSortable(false);
        column.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        column.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns.add(column);
        //Number
        column = new ColumnDefinitionConfig<CaseItem, SimpleLink>(formProperty != null && formProperty.get("CASE_ID") != null && formProperty.get("CASE_ID").isChanged() ? formProperty.get("CASE_ID").getTitle() : property.getSingular(wfmStrings.caseID(), wfmStrings.caseID()), CaseItem.CASE_ID, 100) {
            @Override
            public SimpleLink getCellValue(CaseItem item) {
                return getLink(item.getCaseNumber(), "case|summary/" + item.getObjectId() + "/" + item.getTrackerID(), item.getCaseNumber(), item.getSubject());
            }
        };
        column.setShow(CaseItem.shownColumns.contains(CaseItem.CASE_ID));
        column.setMinimumColumnWidth(60);
        columns.add(column);
        //Subject
        column = new ColumnDefinitionConfig<CaseItem, SimpleLink>(formProperty != null && formProperty.get("SUBJECT") != null && formProperty.get("SUBJECT").isChanged() ? formProperty.get("SUBJECT").getTitle() : wfmStrings.subject(), CaseItem.SUBJECT, 150) {
            @Override
            public SimpleLink getCellValue(CaseItem item) {
                return getLink(item.getSubject(), "case|summary/" + item.getObjectId() + "/" + item.getTrackerID(), item.getCaseNumber(), item.getSubject());
            }
        };
        column.setShow(CaseItem.shownColumns.contains(CaseItem.SUBJECT));
        column.setMinimumColumnWidth(130);
        columns.add(column);
        //Reporter
        column = new ColumnDefinitionConfig<CaseItem, String>(formProperty != null && formProperty.get("REPORTED_BY") != null && formProperty.get("REPORTED_BY").isChanged() ? formProperty.get("REPORTED_BY").getTitle() : wfmStrings.reportedBy(), CaseItem.REPORTED_BY, 130) {
            @Override
            public String getCellValue(CaseItem item) {
                return item.getReportedBy();
            }
        };
        column.setShow(CaseItem.shownColumns.contains(CaseItem.REPORTED_BY));
        column.setMinimumColumnWidth(90);
        columns.add(column);
        //Created
        column = new ColumnDefinitionConfig<CaseItem, String>(wfmStrings.createdDate(), CaseItem.CREATED_DATE, 110) {
            @Override
            public String getCellValue(CaseItem item) {
                return CRMUtils.refactor(item.getCreatedDate(), true);
            }
        };
        column.setShow(CaseItem.shownColumns.contains(CaseItem.CREATED_DATE));
        column.setMinimumColumnWidth(110);
        columns.add(column);
        //Priority
        column = new ColumnDefinitionConfig<CaseItem, SelectItem>(formProperty != null && formProperty.get("PRIORITY") != null && formProperty.get("PRIORITY").isChanged() ? formProperty.get("PRIORITY").getTitle() : wfmStrings.priority(), CaseItem.PRIORITY, 75) {
            @Override
            public SelectItem getCellValue(CaseItem item) {
                currentItem = item;
                return new SelectItem(item.getPriorityId(), item.getPriority());
            }

            @Override
            public void setCellValue(CaseItem rowValue, SelectItem cellValue) {
                rowValue.setPriorityId(cellValue != null ? cellValue.getId() : null);
                rowValue.setPriority(cellValue != null ? cellValue.getName() : null);
                saveCellValue(rowValue);
            }
        };
        column.setShow(CaseItem.shownColumns.contains(CaseItem.PRIORITY));
        column.setMinimumColumnWidth(40);
        column.setColumnSortable(false);
        columns.add(column);
        //Origin
        column = new ColumnDefinitionConfig<CaseItem, String>(formProperty != null && formProperty.get("CASE_ORIGIN") != null && formProperty.get("CASE_ORIGIN").isChanged() ? formProperty.get("CASE_ORIGIN").getTitle() : wfmStrings.caseOrigin(), CaseItem.ORIGIN, 75) {
            @Override
            public String getCellValue(CaseItem item) {
                return item.getCaseOrigin();
            }
        };
        column.setShow(CaseItem.shownColumns.contains(CaseItem.ORIGIN));
        column.setMinimumColumnWidth(40);
        columns.add(column);
        if (!isClientView) {
            //Assignee
            column = new ColumnDefinitionConfig<CaseItem, String>(formProperty != null && formProperty.get("ASSIGNEE") != null && formProperty.get("ASSIGNEE").isChanged() ? formProperty.get("ASSIGNEE").getTitle() : wfmStrings.assignedTo(), CaseItem.ASSIGNED_TO, 130) {
                @Override
                public String getCellValue(CaseItem item) {
                    return item.getCaseAssigneeName() == null ? item.getDepartment() : item.getCaseAssigneeName();
                }
            };
            column.setShow(CaseItem.shownColumns.contains(CaseItem.ASSIGNED_TO));
            column.setMinimumColumnWidth(80);
            columns.add(column);
        }
        //Status
        column = new ColumnDefinitionConfig<CaseItem, SelectItem>(formProperty != null && formProperty.get("STATUS") != null && formProperty.get("STATUS").isChanged() ? formProperty.get("STATUS").getTitle() : wfmStrings.status(), CaseItem.STATUS, 100) {
            @Override
            public SelectItem getCellValue(CaseItem item) {
                currentItem = item;
                return item.getStatus();
            }

            @Override
            public void setCellValue(CaseItem rowValue, SelectItem cellValue) {
                rowValue = currentItem;
                saveCellValue(rowValue);
            }
        };
        column.addColor(new ColumnColor(reference.WAITING_FOR_REPLY(), "r", "2BBF57"));
        column.addColor(new ColumnColor(reference.REPLIED(), "r", "007DE7"));
        column.addColor(new ColumnColor(wfmStrings.New(), "c", "DC0C0C"));
        column.setShow(CaseItem.shownColumns.contains(CaseItem.STATUS));
        column.setMinimumColumnWidth(80);
        columns.add(column);
        //Type
        column = new ColumnDefinitionConfig<CaseItem, SelectItem>(formProperty != null && formProperty.get("TYPE") != null && formProperty.get("TYPE").isChanged() ? formProperty.get("TYPE").getTitle() : wfmStrings.type(), CaseItem.CASE_TYPE, 75) {
            @Override
            public SelectItem getCellValue(CaseItem item) {
                currentItem = item;
                return new SelectItem(item.getTypeId(), item.getType());
            }

            @Override
            public void setCellValue(CaseItem rowValue, SelectItem cellValue) {
                rowValue.setTypeId(cellValue != null ? cellValue.getId() : null);
                rowValue.setType(cellValue != null ? cellValue.getName() : null);
                saveCellValue(rowValue);
            }
        };
        column.setShow(CaseItem.shownColumns.contains(CaseItem.CASE_TYPE));
        column.setMinimumColumnWidth(40);
        column.setColumnSortable(false);
        columns.add(column);
        //Updated
        column = new ColumnDefinitionConfig<CaseItem, String>(wfmStrings.modifiedDate(), CaseItem.LAST_UPDATED_DATE, 130) {
            @Override
            public String getCellValue(CaseItem item) {
                return CRMUtils.refactor(item.getLastUpdatedDate(), true);
            }
        };
        column.setShow(CaseItem.shownColumns.contains(CaseItem.LAST_UPDATED_DATE));
        column.setMinimumColumnWidth(110);
        columns.add(column);
        if (!isClientView) {
            //Resolver
            column = new ColumnDefinitionConfig<CaseItem, String>(formProperty != null && formProperty.get("RESOLVER") != null && formProperty.get("RESOLVER").isChanged() ? formProperty.get("RESOLVER").getTitle() : wfmStrings.resolver(), CaseItem.RESOLVER, 130) {
                @Override
                public String getCellValue(CaseItem item) {
                    return item.getResolverName() == null ? item.getDepartment() : item.getResolverName();
                }
            };
            column.setShow(CaseItem.shownColumns.contains(CaseItem.RESOLVER));
            column.setMinimumColumnWidth(80);
            columns.add(column);
        }
        //Reason
        column = new ColumnDefinitionConfig<CaseItem, SelectItem>(formProperty != null && formProperty.get("CASE_REASON") != null && formProperty.get("CASE_REASON").isChanged() ? formProperty.get("CASE_REASON").getTitle() : wfmStrings.reason(), CaseItem.CASE_REASON, 60) {
            @Override
            public SelectItem getCellValue(CaseItem item) {
                currentItem = item;
                return new SelectItem(item.getCaseReasonId(), item.getCaseReason());
            }

            @Override
            public void setCellValue(CaseItem rowValue, SelectItem cellValue) {
                rowValue.setCaseReasonId(cellValue != null ? cellValue.getId() : null);
                rowValue.setCaseReason(cellValue != null ? cellValue.getName() : null);
                saveCellValue(rowValue);
            }
        };
        column.setShow(CaseItem.shownColumns.contains(CaseItem.CASE_REASON));
        column.setMinimumColumnWidth(30);
        column.setColumnSortable(false);
        columns.add(column);
        //Attachment
        column = new ColumnDefinitionConfig<CaseItem, Widget>(wfmStrings.attachment(), CaseItem.CASE_ATTACHMENT, 60) {
            @Override
            public Widget getCellValue(CaseItem item) {

                Icon icon = new Icon();
                icon.addStyleName("ficon--upload");

                return item.hasAttachments() ? icon : new HTML("");
            }
        };
        column.setShow(CaseItem.shownColumns.contains(CaseItem.CASE_ATTACHMENT));
        column.setColumnSortable(false);
        column.setMinimumColumnWidth(45);
        column.setMaximumColumnWidth(45);
        columns.add(column);

        //Reported By Company Name
        column = new ColumnDefinitionConfig<CaseItem, String>(wfmStrings.reportedByCompanyName(), CaseItem.REPORTED_BY_COMPANY_NAME, 130) {
            @Override
            public String getCellValue(CaseItem item) {
                return item.getReportedByCompanyName();
            }
        };
        column.setShow(CaseItem.shownColumns.contains(CaseItem.REPORTED_BY_COMPANY_NAME));
        column.setMinimumColumnWidth(90);
        columns.add(column);
        //Task Status
        column = new ColumnDefinitionConfig<CaseItem, String>(
                wfmStrings.task() + " " + wfmStrings.status(),
                CaseItem.TASK_STATUS,
                100) {

            @Override
            public String getCellValue(CaseItem item) {
                if (item.getTasks() != null && !item.getTasks().isEmpty() && item.getTasks().get(0).getStatus() != null) {
                    return item.getTasks().get(0).getStatus().getName();
                }
                return "";
            }
        };

        column.setShow(CaseItem.shownColumns.contains(CaseItem.TASK_STATUS));
        column.setMinimumColumnWidth(90);
        columns.add(column);

        //Task Number
        column = new ColumnDefinitionConfig<CaseItem, SimpleLink>(wfmStrings.task() + " " + wfmStrings.number() , CaseItem.TASK_NUMBER, 100) {
            @Override
            public SimpleLink getCellValue(CaseItem item) {
                if (item.getTasks() != null && !item.getTasks().isEmpty()) {
                    return getLink(item.getTasks().get(0).getNumber(), "task|summary/" + item.getTasks().get(0).getObjectID(), item.getTasks().get(0).getNumber());
                }
                return new SimpleLink("");
            }
        };
        column.setShow(CaseItem.shownColumns.contains(CaseItem.TASK_NUMBER));
        column.setMinimumColumnWidth(90);
        columns.add(column);


        if (Utils.hasGenericAccess(GenericSettingsEnum.ENABLE_PRODUCT_DETAILS_TO_CRM)) {
            //Brand
            column = new ColumnDefinitionConfig<CaseItem, String>(wfmStrings.brand(), CaseItem.BRAND, 130) {
                @Override
                public String getCellValue(CaseItem item) {
                    return item.getBrand() != null ? item.getBrand().getName() : "";
                }
            };
            column.setShow(CaseItem.shownColumns.contains(CaseItem.BRAND));
            column.setMinimumColumnWidth(90);
            columns.add(column);

            //Product Category
            column = new ColumnDefinitionConfig<CaseItem, String>(wfmStrings.product() + " " + wfmStrings.category(), CaseItem.PRODUCT_CATEGORY, 130) {
                @Override
                public String getCellValue(CaseItem item) {
                    return item.getProductCategory() != null ? item.getProductCategory().getName() : "";
                }
            };
            column.setShow(CaseItem.shownColumns.contains(CaseItem.PRODUCT_CATEGORY));
            column.setMinimumColumnWidth(90);
            columns.add(column);

            //Product
            column = new ColumnDefinitionConfig<CaseItem, String>(wfmStrings.product(), CaseItem.PRODUCT, 130) {
                @Override
                public String getCellValue(CaseItem item) {
                    return item.getProduct() != null ? item.getProduct().getName() : "";
                }
            };
            column.setShow(CaseItem.shownColumns.contains(CaseItem.PRODUCT));
            column.setMinimumColumnWidth(90);
            columns.add(column);
        }

        if (Utils.hasPermission(PermissionConstants.CRM_EDIT_CASE)) {
            initCellEdit(columns);
        }
        return columns.toArray(new ColumnDefinitionConfig[]{});
    }

    private void changeCaseStatus(Integer caseID, Integer statusId) {
        LoadingPanel.loading(true);
        crmService.updateCaseStatus(caseID, statusId, null, new AbstractAsyncCallback<Void>() {
            @Override
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
            }

            @Override
            public void success(Void result) {
                LoadingPanel.loading(false);
                Info.show(property.getSingular(wfmStrings.messSuccessfullyUpdated(), wfmStrings.caseID()));
                refresh();
            }
        });
    }

    private void initCellEdit(ArrayList<CustomColumnDefinitionConfig> columns) {
        LinkedHashMap<String, CustomColumnDefinitionConfig> mapOfColumns = CustomColumnDefinitionConfig.getEditableColumns(columns);
        if (mapOfColumns.size() > 0) {
            for (final Map.Entry<String, CustomColumnDefinitionConfig> entry : mapOfColumns.entrySet()) {
                InlineCellEditor widget = null;
                CustomColumnDefinitionConfig column = entry.getValue();
                if (CaseItem.PRIORITY.equals(entry.getKey()) || CaseItem.CASE_TYPE.equals(entry.getKey())
                        || CaseItem.CASE_REASON.equals(entry.getKey()) || CaseItem.INTERNAL_STATUS.equals(entry.getKey())) {
                    widget = new DropDownCellEditor<SelectItem>() {
                        @Override
                        protected SelectItem getValue() {
                            return getListBox().getSelectedItem();
                        }

                        @Override
                        protected void setValue(SelectItem cellValue) {
                            getListBox().setAllowFirstItem(true);
                            if (getListBox().getItems() == null || getListBox().getItems().length < 1 && currentItem != null) {
                                if (CaseItem.PRIORITY.equals(entry.getKey())) {
                                    getListBox().setItems(currentItem.getPriorities());
                                } else if (CaseItem.CASE_REASON.equals(entry.getKey())) {
                                    getListBox().setItems(currentItem.getCaseReasons());
                                } else if (CaseItem.CASE_TYPE.equals(entry.getKey())) {
                                    getListBox().setItems(currentItem.getTypes());
                                } else if (CaseItem.INTERNAL_STATUS.equals(entry.getKey())) {
                                    getListBox().setItems(currentItem.getInternalStatusItems());
                                }
                            }
                            getListBox().setSelectedIndex(0);
                            if (cellValue == null || cellValue.getId() == null) {
                                if (cellValue != null && cellValue.getName() != null) {
                                    getListBox().setSelectedByValue(cellValue.getName());
                                } else {
                                    getListBox().setSelectedNullLabel();
                                }
                            } else {
                                getListBox().setSelected(cellValue.getId());
                            }
                        }
                    };
                } else if (CaseItem.STATUS.equals(entry.getKey())) {
                    if (!isClientView) {
                        widget = new DropDownCellEditor<SelectItem>() {
                            @Override
                            protected SelectItem getValue() {
                                SelectItem item = getListBox().getSelectedItem();
                                currentItem.setStatus(item);
                                currentItem.setStatusCode(item.getCode());
                                return item;
                            }

                            @Override
                            protected void setValue(SelectItem cellValue) {
                                getListBox().setWithoutNullLabel(true);
                                if (getListBox().getItems() == null || getListBox().getItems().length < 1) {
                                    getListBox().setItems(currentItem.getStatusItems());
                                }
                                getListBox().setSelectedIndex(0);
                                if (cellValue == null || cellValue.getId() == null) {
                                    if (cellValue != null && cellValue.getName() != null) {
                                        getListBox().setSelectedByValue(cellValue.getName());
                                    } else {
                                        getListBox().setSelectedNullLabel();
                                    }
                                } else {
                                    getListBox().setSelected(cellValue.getId());
                                }
                            }
                        };
                    }
                }
                if (widget != null) {
                    column.setCellEditor(widget);
                    column.setCellChangesSave((rowValue, columnCodeName) -> {
                        refresh = false;
                        saveCaseEditCellValue((CaseItem) rowValue, columnCodeName);
                    });
                }
            }
        }
    }

    private boolean refresh = true;

    private void saveCaseEditCellValue(CaseItem rowValue, String columnCodeName) {
        Integer id = null;
        if (CaseItem.CASE_REASON.equals(columnCodeName)) {
            id = rowValue.getCaseReasonId();
        } else if (CaseItem.PRIORITY.equals(columnCodeName)) {
            id = rowValue.getPriorityId();
        } else if (CaseItem.STATUS.equals(columnCodeName)) {
            id = rowValue.getStatus().getId();
        } else if (CaseItem.CASE_TYPE.equals(columnCodeName)) {
            id = rowValue.getTypeId();
        } else if (CaseItem.INTERNAL_STATUS.equals(columnCodeName)) {
            id = rowValue.getInternalStatusId();
        }
        if (id == null) {
            LoadingPanel.loading(true);
            crmService.saveCaseEditCellValue(rowValue, columnCodeName, new AbstractAsyncCallback<Void>() {
                @Override
                public void onFailure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void onSuccess(Void result) {
                    LoadingPanel.loading(false);
                }
            });
        } else if (columnCodeName != null) {
            if (CaseItem.STATUS.equals(columnCodeName)) {
                final Integer statusID = id;
                if (rowValue.getStatus().isSelected()) {
                    new CaseStatusModal(rowValue.getObjectId(), statusID, false);
                } else {
                    changeCaseStatus(rowValue.getObjectId(), statusID);
                }
            } else {
                final ArrayList<Integer> ids = new ArrayList<>();
                ids.add(rowValue.getObjectId());
                LoadingPanel.loading(true);
                crmService.updateCases(id, ids, columnCodeName, new AbstractAsyncCallback<Boolean>() {
                    @Override
                    public void failure(Throwable caught) {
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void success(Boolean result) {
                        LoadingPanel.loading(false);
                        if (refresh) {
                            refresh();
                        }
                        refresh = true;
                    }
                });
            }
        }
    }

    private void getRelationName(final Integer relationID, final String relType) {
        allInOneService.getRelationName(relationID, relType, new AsyncCallback<String>() {
            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(String result) {
                relationName = result;
            }
        });
    }

    private GuideListingPanelDesign getListingPanelDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                if (Utils.hasPermission(PermissionConstants.ADD_NEW_CASE)) {
                    if (relationID != null && relationType != null) {
                        return () -> goTo("case|add/add/RELATION/" + relationType + "/" + relationID);
                    } else {
                        return () -> goTo("case|add/add/");
                    }
                } else if (Utils.hasPermission(PermissionConstants.CRM_CASE_QUICK_ADD)) {
                    return (() -> new CrmQuickAdd(LayoutRPC.CASE_FORM, RelationItem.newEventRelation(relationType, relationID, relationName)));
                }
                return null;
            }

            @Override
            public Command getUploadButtonCommand() {
                return null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return isClientView ? null : new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return (data, callback) -> RbacService.App.get().getCRMFacetFilterData(CrmConstants.CRM_CASE, data, new AbstractAsyncCallback<FacetFilterRpc>() {
                            @Override
                            public void failure(Throwable throwable) {
                                callback.onFailure(throwable);
                            }

                            @Override
                            public void success(FacetFilterRpc newCaseData) {
                                callback.onSuccess(newCaseData);
                            }
                        });
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return getCaseContentConfigure();
                    }

                    @Override
                    public DatePeriodFacetContent getPeriodDateContent() {
                        return getDatePeriodFacetContent();
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (Utils.hasPermission(PermissionConstants.ADD_NEW_CASE) || Utils.hasPermission(PermissionConstants.CRM_CASE_QUICK_ADD)) {
                    ActionButton newItem = getAddNewButton(ActionButton.Type.TOOLMENU);
                    MenuBar menu = new MenuBar(true);

                    if (Utils.hasPermission(PermissionConstants.ADD_NEW_CASE)) {
                        MenuPopItem addNew = new MenuPopItem(Property.get(Constants.CASE_LIST, wfmStrings.crmCase()));
                        addNew.ensureDebugId("crmAddNewCase");
                        if (relationID != null && relationType != null) {
                            addNew.setCommand(() -> goTo("case|add/add/RELATION/" + relationType + "/" + relationID));
                        } else {
                            addNew.setCommand(() -> goTo("case|add/add/"));
                        }
                        menu.addItem(addNew);
                    }

                    if (Utils.hasPermission(PermissionConstants.CRM_CASE_QUICK_ADD)) {
                        MenuPopItem quickAddCase = new MenuPopItem(wfmStrings.quickAdd());
                        quickAddCase.ensureDebugId("crmAddNewCaseQuickAdd");
                        quickAddCase.setCommand(() -> new CrmQuickAdd(LayoutRPC.CASE_FORM, RelationItem.newEventRelation(relationType, relationID, relationName)));

                        menu.addItem(quickAddCase);
                    }

                    newItem.setMenu(menu);
                    return newItem;
                }
                return null;
            }

            @Override
            public ActionButton initTopToolBarMore() {
                final ActionButton more = new ActionButton(ActionButton.getMoreString(), "", ActionButton.Type.TOOLMENU);
                more.ensureDebugId("crmCaseMore");
                more.addClickHandler(clickEvent -> {
                    MenuBar menu = getActionsForSelections();
                    menu.setAutoOpen(true);
                    more.setMenu(menu);
                    menu.setLayoutData(more);
                });

                return more;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown
                    menuContainer) {
                exportOption.initExport(null, Utils.hasPermission(PermissionConstants.CRM_CASES_EXPORT));
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(property.getPlural(crmStrings.messCurrentlyCases(), wfmStrings.cases()));
                if (Utils.hasPermission(PermissionConstants.ADD_NEW_CASE)) {
                    message.setHref(clickEvent -> new CrmQuickAdd(LayoutRPC.CASE_FORM, RelationItem.newEventRelation(relationType, relationID, relationName)));
                    message.setTextBeforeLink(property.getPlural(crmStrings.messAddingCasesClicking(), wfmStrings.cases()));
                }
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return !Utils.hasOnlyRole(CLIENT) && Utils.hasPermission(PermissionConstants.CRM_EDIT_CASE);
            }
        }

                ;
    }

    private KanbanDataLoader<CaseItem> getKanbanDataLoader() {
        return new KanbanDataLoader<CaseItem>() {
            @Override
            public void loadData(ListingFilterParameter filterParametrs, KanbanDataRenderer dataRenderer) {
                LoadingPanel.loading(true);
                filterParametrs.setWebFormID(webFormId);
                if (relationID != null && relationType != null) {
                    filterParametrs.setRelationID(relationID);
                    filterParametrs.setRelationType(relationType);
                }
                if (relationID != null && relationType != null) {
                    switch (relationType) {
                        case RelationItem.TYPE_CRM_ACCOUNT:
                            filterParametrs.setAccountID(relationID);
                            break;
                        case RelationItem.TYPE_OPPORTUNITY:
                            filterParametrs.setOpportunityID(relationID);
                            break;
                        case RelationItem.TYPE_CONTACT:
                            filterParametrs.setCrmContactId(relationID);
                            break;
                        case RelationItem.TYPE_LEAD:
                            filterParametrs.setLeadID(relationID);
                            break;
                    }
                }
                filterParametrs.setHasOnlyClientAccess(isClientView);
                crmService.getNewKanbanCases(filterParametrs, dataRenderer.getColumnMetadata(), new AbstractAsyncCallback<ListResult<CaseItem>>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void onSuccess(ListResult<CaseItem> result) {
                        dataRenderer.setResults(result);
                        LoadingPanel.loading(false);
                    }
                });
            }

            @Override
            public void onDropKanbanItem(Object sourceColumnLayoutData, Object targetColumnLayoutData, Object caseItem,
                                         Integer widgetIndex, Object prevItem, Object afterItem, KanbanBoard kanbanBoard,
                                         KanbanBoard.OnDropCard onDropCard) {

                if (((SelectItem) targetColumnLayoutData).isSelected()) {
                    new CaseStatusModal((SelectItem) targetColumnLayoutData, (Integer) caseItem, widgetIndex, (Integer) prevItem, (Integer) afterItem, onDropCard);
                } else {
                    changeKanbanCaseStatus((SelectItem) targetColumnLayoutData, (Integer) caseItem, widgetIndex, (Integer) prevItem, (Integer) afterItem, onDropCard);
                }
            }
        };
    }

    private void changeKanbanCaseStatus(SelectItem targetColumnLayoutData, Integer caseItem, Integer
            widgetIndex, Integer prevItem, Integer afterItem, KanbanBoard.OnDropCard onDropCard) {
        crmService.changeCaseKanbanOrder(targetColumnLayoutData, caseItem, widgetIndex,
                prevItem, afterItem, new AsyncCallback<Integer>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        if (onDropCard != null) {
                            onDropCard.onDropCard();
                        }
                    }
                });
    }

    private KanbanBoardDesign<CaseItem> getKanbanBoardDesign() {
        return new KanbanBoardDesign<CaseItem>() {
            @Override
            public Widget getBoardItem(CaseItem kanbanItem, KanbanBoard kanbanBoard, Object... obj) {
                MaterialPanel p = new MaterialPanel();
                if (obj != null && obj.length > 0 && (obj[0] instanceof HashMap)) {
                    HashMap<String, KanbanItemColumnConfigs> strMap = (HashMap) obj[0];
                    p.add(new CaseMaterialCard(kanbanItem, kanbanBoard, strMap));
                } else {
                    p.add(new CaseMaterialCard(kanbanItem, kanbanBoard));
                }
                p.setLayoutData(kanbanItem.getObjectId());
                return p;
            }

            @Override
            public void loadDefaultColumns(AbstractAsyncCallback callback) {
                LoadingPanel.loading(true);
                kanbanService.getKanbanDefaultColumns(ReferenceParentEnum._CASE_STATUS, new AsyncCallback<ArrayList<SelectItem>>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        LoadingPanel.loading(false);
                        callback.failure(throwable);
                    }

                    @Override
                    public void onSuccess(ArrayList<SelectItem> selectItems) {
                        LoadingPanel.loading(false);
                        callback.success(selectItems);
                    }
                });
            }

            @Override
            public boolean canDnD(CaseItem kanbanItem) {
                return !(isClientView || checkedTrash) && Utils.hasPermission(PermissionConstants.CRM_CHANGE_STATUS_CASE, PermissionConstants.CRM_EDIT_CASE);
            }
        };
    }

    private FacetContentConfigure getCaseContentConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(4, wfmStrings.filter());

        contentConfigure.addContentConfigure(FacetContentType.CaseFacetFilter.getContentCode()[3], wfmStrings.status(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrCaseRepresenter.STATUS_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrCaseRepresenter.STATUS_ID_CODE_NAME;
            }

            @Override
            public LocalizationType getLocalizationType() {
                return LocalizationType.REFERENCE;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.CaseFacetFilter.getContentCode()[0], wfmStrings.reportedBy(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrCaseRepresenter.REPORTED_BY;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrCaseRepresenter.REPORTED_BY;
            }

            @Override
            public boolean isConditionItemId() {
                return false;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.CaseFacetFilter.getContentCode()[1], wfmStrings.type(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrCaseRepresenter.CASE_TYPE_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrCaseRepresenter.CASE_TYPE_ID_CODE_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }

            @Override
            public LocalizationType getLocalizationType() {
                return LocalizationType.REFERENCE;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.CaseFacetFilter.getContentCode()[2], wfmStrings.priority(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrCaseRepresenter.PRIORITY_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrCaseRepresenter.PRIORITY_ID_CODE_NAME;
            }

            @Override
            public LocalizationType getLocalizationType() {
                return LocalizationType.REFERENCE;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.CaseFacetFilter.getContentCode()[4], crmStrings.origin(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrCaseRepresenter.CASE_ORIGIN_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrCaseRepresenter.CASE_ORIGIN_ID_CODE_NAME;
            }

            @Override
            public LocalizationType getLocalizationType() {
                return LocalizationType.REFERENCE;
            }

        });
        contentConfigure.addContentConfigure(FacetContentType.CaseFacetFilter.getContentCode()[5], wfmStrings.assignees(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrCaseRepresenter.CASE_ASSIGNEE_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrCaseRepresenter.CASE_ASSIGNEE_ID_NAME;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.CaseFacetFilter.getContentCode()[6], Property.get(Constants.DEPARTMENT_LIST, wfmStrings.department()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrCaseRepresenter.CASE_DEPARTMENT_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrCaseRepresenter.CASE_DEPARTMENT_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.CaseFacetFilter.getContentCode()[7], wfmStrings.resolver(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrCaseRepresenter.RESOLVER_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrCaseRepresenter.RESOLVER_ID_NAME;
            }
        });
        if (CompanyConstants.C24899.equals(Utils.getEncryptedCompanyID()) || !isClientView) {
            contentConfigure.addContentConfigure(FacetContentType.CaseFacetFilter.getContentCode()[8], Property.get(Constants.Contacts, wfmStrings.relatedToEvent(), wfmStrings.contact()), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrCaseRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_CONTACT;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrCaseRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_CONTACT;
                }

                @Override
                public boolean isShowFacetConttentFilter() {
                    return false;
                }
            });
        }
        if (!isClientView) {
            contentConfigure.addContentConfigure(FacetContentType.CaseFacetFilter.getContentCode()[9], wfmStrings.relatedCrmAccount(), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrCaseRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_CRM_ACCOUNT;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrCaseRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_CRM_ACCOUNT;
                }

                @Override
                public boolean isShowFacetConttentFilter() {
                    return false;
                }
            });
            contentConfigure.addContentConfigure(FacetContentType.CaseFacetFilter.getContentCode()[10], Property.get(Constants.LEADS, wfmStrings.relatedToLead(), wfmStrings.lead()), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrCaseRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_LEAD;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrCaseRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_LEAD;
                }

                @Override
                public boolean isShowFacetConttentFilter() {
                    return false;
                }
            });
            contentConfigure.addContentConfigure(FacetContentType.CaseFacetFilter.getContentCode()[11], Property.get(Constants.Opportunities, wfmStrings.relatedToOpportunity(), wfmStrings.opportunity()), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrCaseRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_OPPORTUNITY;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrCaseRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_OPPORTUNITY;
                }

                @Override
                public boolean isShowFacetConttentFilter() {
                    return false;
                }
            });
            contentConfigure.addContentConfigure(FacetContentType.CaseFacetFilter.getContentCode()[12], Property.get(Constants.TASK, wfmStrings.relatedToTask(), wfmStrings.task()), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrCaseRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_TASK;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrCaseRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_TASK;
                }

                @Override
                public boolean isShowFacetConttentFilter() {
                    return false;
                }
            });
            contentConfigure.addContentConfigure(FacetContentType.CaseFacetFilter.getContentCode()[13], Property.get(Constants.PROJECT, wfmStrings.relatedToProject(), wfmStrings.project()), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrCaseRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_PROJECT;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrCaseRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_PROJECT;
                }

                @Override
                public boolean isShowFacetConttentFilter() {
                    return false;
                }
            });
            contentConfigure.addContentConfigure(FacetContentType.CaseFacetFilter.getContentCode()[14], Property.get(Constants.EVENT_LIST, wfmStrings.relatedEvent(), wfmStrings.event()), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrCaseRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_EVENT;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrCaseRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_EVENT;
                }

                @Override
                public boolean isShowFacetConttentFilter() {
                    return false;
                }
            });
            contentConfigure.addContentConfigure(FacetContentType.CaseFacetFilter.getContentCode()[15], Property.get(Constants.ISSUE, wfmStrings.relatedIssue(), wfmStrings.issue()), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrCaseRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_ISSUE;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrCaseRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_ISSUE;
                }

                @Override
                public boolean isShowFacetConttentFilter() {
                    return false;
                }
            });
            contentConfigure.addContentConfigure(FacetContentType.CaseFacetFilter.getContentCode()[16], wfmStrings.relatedEmployee(), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrCaseRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_EMPLOYEE;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrCaseRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_EMPLOYEE;
                }

                @Override
                public boolean isShowFacetConttentFilter() {
                    return false;
                }
            });
            contentConfigure.addContentConfigure(FacetContentType.CaseFacetFilter.getContentCode()[18], Property.get(Constants.CLIENT_LIST, wfmStrings.relatedClient()), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrCaseRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_CLIENT;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrCaseRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_CLIENT;
                }

                @Override
                public boolean isShowFacetConttentFilter() {
                    return false;
                }
            });
            contentConfigure.addContentConfigure(FacetContentType.CaseFacetFilter.getContentCode()[19], Property.get(Constants.SUPPLIER_LIST, wfmStrings.relatedSupplier()), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrCaseRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_SUPPLIER;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrCaseRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_SUPPLIER;
                }

                @Override
                public boolean isShowFacetConttentFilter() {
                    return false;
                }
            });
            contentConfigure.addContentConfigure(FacetContentType.CaseFacetFilter.getContentCode()[21], wfmStrings.internalStatus(), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrCaseRepresenter.INTERNAL_STATUS_ID;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrCaseRepresenter.INTERNAL_STATUS_ID_NAME;
                }

                @Override
                public boolean isShowFacetConttentFilter() {
                    return false;
                }
            });
        }
        if (CompanyConstants.C24899.equals(Utils.getEncryptedCompanyID()) || !isClientView) {
            contentConfigure.addContentConfigure(FacetContentType.CaseFacetFilter.getContentCode()[20], crmStrings.relatedToQuote(), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrCaseRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_SALEQUOTE;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrCaseRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_SALEQUOTE;
                }

                @Override
                public boolean isShowFacetConttentFilter() {
                    return false;
                }
            });
        }
        contentConfigure.addContentConfigureDateListBox(SolrCaseRepresenter.LAST_UPDATE_DATE, "Last Update");
        contentConfigure.addContentConfigureDateListBox(SolrCaseRepresenter.CREATE_DATE, "Received Date");
        return contentConfigure;
    }

    private DatePeriodFacetContent getDatePeriodFacetContent() {
        return new DatePeriodFacetContent() {
            @Override
            public void getDateFacetContent(FlexTable datePeriod) {
                checkedTrash = false;
                hasAttachment = new KpiCheckBox("<span class='customTitle' style='padding-left: 8px;'>" + crmStrings.hasAttachment() + "</span>", true);
                hasAttachment.addClickHandler(clickEvent -> {
                    listPanel.getFacetPopup().getFacetFilterRpc().setCustomDataPut(CaseItem.CASE_ATTACHMENT, hasAttachment.getValue().toString());
                    listPanel.refreshFacetFilter();
                });
                inTrach = new KpiCheckBox("<span class='customTitle' style='padding-left: 8px;'>" + crmStrings.inTrash() + "</span>", true);
                inTrach.addClickHandler(clickEvent -> {
                    checkedTrash = inTrach.getValue();
                    listPanel.getFacetPopup().getFacetFilterRpc().setCustomDataPut(CaseItem.IN_TRASH, inTrach.getValue().toString());
                    listPanel.refreshFacetFilter();
                });
                datePeriod.setCellSpacing(5);
                int row = datePeriod.getRowCount();
                datePeriod.setWidget(row, 0, inTrach);
                datePeriod.getFlexCellFormatter().setColSpan(row++, 0, 2);
                datePeriod.setWidget(row, 0, hasAttachment);
                datePeriod.getFlexCellFormatter().setColSpan(row++, 0, 2);
            }

            @Override
            public void refreshFacetFilter(FacetFilterRpc data) {
                if (data.getCustomData().containsKey(CaseItem.CASE_ATTACHMENT)) {
                    hasAttachment.setValue(Boolean.valueOf(data.getCustomData().get(CaseItem.CASE_ATTACHMENT)), true);
                }
                if (data.getCustomData().containsKey(CaseItem.IN_TRASH)) {
                    inTrach.setValue(Boolean.valueOf(data.getCustomData().get(CaseItem.IN_TRASH)), true);
                }
            }

            @Override
            public void reset() {
                hasAttachment.setValue(false);
                inTrach.setValue(false);
            }
        };
    }

    private ListingRequestProvider<CaseItem> getRequestProvider() {
        return (filterParametrs, caseItemListingCallback) -> initCaseList(filterParametrs, caseItemListingCallback, null);
    }

    private void initCaseList(ListingFilterParameter filterParametrs, ListingCallback<CaseItem> callback, Span
            container) {
        filterParametrs.setWebFormID(webFormId);
        if (relationID != null && relationType != null) {
            filterParametrs.setRelationID(relationID);
            filterParametrs.setRelationType(relationType);
        }
        if (relationID != null && relationType != null) {
            switch (relationType) {
                case RelationItem.TYPE_CRM_ACCOUNT:
                    filterParametrs.setAccountID(relationID);
                    break;
                case RelationItem.TYPE_OPPORTUNITY:
                    filterParametrs.setOpportunityID(relationID);
                    break;
                case RelationItem.TYPE_CONTACT:
                    filterParametrs.setCrmContactId(relationID);
                    break;
                case RelationItem.TYPE_LEAD:
                    filterParametrs.setLeadID(relationID);
                    break;
            }
        }
        filterParametrs.setHasOnlyClientAccess(isClientView);
        crmService.getCases(filterParametrs, new AbstractAsyncCallback<CaseList>() {
            @Override
            public void failure(Throwable throwable) {
                if (callback != null) {
                    callback.onFailure(throwable);
                }
            }

            @Override
            public void success(CaseList caseList) {
                if (callback != null) {
                    if (caseList.isTrash()) {
                        checkedTrash = caseList.isTrash();
                    }
                    callback.onSuccess(caseList);
                }
                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (caseList.getTotal() != null && caseList.getTotal() > 0) {
                        statisticShortcut.setText(countFormat(caseList.getTotal()));
                        statisticShortcut.setClass("tab-label");
                    } else {
                        statisticShortcut.setText("");
                        statisticShortcut.removeStyleName("tab-label");
                    }
                }
            }
        });
    }

    public String getIconStyle() {
        return "casesList cases-list";
    }

    private MenuBar getActionsForSelections() {
        if (!(listPanel.getPagingScrollTable().getSelectedRowValues() == null || listPanel.getPagingScrollTable().getSelectedRowValues().size() < 1)) {
            if (actions == null) {
                actions = new ContextMenu();
                actions.getMenuBar().setAutoOpen(false);
                actions.getMenuBar().addStyleName("my-menu");
                String ID = "";
                if (!isClientView) {
                    if (Utils.hasPermission(PermissionConstants.CRM_EDIT_CASE, PermissionConstants.CRM_CHANGE_STATUS_CASE)) {
                        MenuBar statuses = new MenuBar(true);
                        statuses.addStyleName("my-menu");
                        for (final SelectItem status : ((CaseItem) listPanel.getDefaultOne()).getStatusItems()) {
                            ID = status.getCode() != null ? status.getCode().toLowerCase() : status.getName().toLowerCase().replace(" ", "");
                            statuses.addItem("<span id=" + ID + " style='padding-left:20px'>" + status.getName() + "</span>", true, (Command) () -> updateCases(null, status.getId(), CaseItem.STATUS));
                        }
                        actions.addMenuItemWithMenuBar(wfmStrings.changeStatus(), null, true, statuses);
                    }
                    if (Utils.hasPermission(PermissionConstants.CRM_EDIT_CASE, PermissionConstants.CRM_CHANGE_PRIORITY_CASE)) {
                        MenuBar changePriority = new MenuBar(true);
                        changePriority.setAutoOpen(true);
                        changePriority.addStyleName("my-menu");
                        for (final SelectItem apriority : ((CaseItem) listPanel.getDefaultOne()).getPriorities()) {
                            ID = apriority.getCode() != null ? apriority.getCode().toLowerCase() : apriority.getName().toLowerCase().replace(" ", "");
                            changePriority.addItem("<span id=" + ID + " style='padding-left:20px'>" + apriority.getName() + "</span>", true, (Command) () -> updateCases(null, apriority.getId(), CaseItem.PRIORITY));
                        }
                        actions.addMenuItemWithMenuBar(crmStrings.changePriority(), null, true, changePriority);
                    }
                    if (Utils.hasPermission(PermissionConstants.CRM_EDIT_CASE, PermissionConstants.CRM_CHANGE_ASSIGNEE_CASE)) {
                        actions.addMenuItem(crmStrings.changeAssignee(), null, true, () -> {
                            actions.hide();
                            assigneePopup.getItemIDs().clear();
                            if (selectedItems.size() > 0) {
                                for (CaseItem item : selectedItems) {
                                    assigneePopup.getItemIDs().add(item.getObjectId());
                                }
                                assigneePopup.open();
                            } else {
                                listPanel.showSelectOneMessage();
                            }
                        });
                    }
                }
                if (Utils.hasPermission(PermissionConstants.CRM_REMOVE_CASE)) {
                    actions.addMenuItem(wfmStrings.delete(), null, true, () -> deleteSelection());
                }
            }
            return actions.getMenuBar();
        } else {
            if (emptyActions == null) {
                emptyActions = new ContextMenu();
                emptyActions.getMenuBar().setAutoOpen(false);
                emptyActions.addMenuItem(wfmStrings.selectAnyItemToActivateBatchActions(), null, true, null);
            }
            return emptyActions.getMenuBar();
        }

    }

    private void updateCases(Integer caseID, Integer typeID, String type) {
        if (type != null && (selectedItems.size() > 0 || caseID != null)) {
            final ArrayList<Integer> ids = new ArrayList<>();
            if (caseID == null) {
                for (CaseItem aCase : selectedItems) {
                    ids.add(aCase.getObjectId());
                }
            } else {
                ids.add(caseID);
            }
            LoadingPanel.loading(true);
            crmService.updateCases(typeID, ids, type, new AbstractAsyncCallback<Boolean>() {
                @Override
                public void failure(Throwable caught) {
                    LoadingPanel.loading(false);
                }

                @Override
                public void success(Boolean result) {
                    LoadingPanel.loading(false);
                    refresh();
                }
            });
        }
    }

    protected void deleteSelection() {
        if (selectedItems.size() == 0) {
            Info.show(wfmMessages.pleaseSelectOneRow("Case"), Info.Type.WARNING);
        } else {
            showDeleteMessage();
        }
    }

    private void showDeleteMessage() {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.WARN, Action.YesNo, true);
        messageBox.setTitle(wfmStrings.warning());
        CaseItem item = selectedItems.iterator().next();
        String message = wfmStrings.areYouSureYouWantToDeleteTheSelectedRecords();
        messageBox.setMessage(message);
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                final java.util.ArrayList<Integer> ids = CaseItem.getIDsOnly(selectedItems);
                if (ids.size() > 0) {
                    LoadingPanel.loading(true);
                    crmService.deleteCase(ids, new AbstractAsyncCallback<Void>() {
                        @Override
                        public void failure(Throwable caught) {
                            LoadingPanel.loading(false);
                        }

                        @Override
                        public void success(Void result) {
                            LoadingPanel.loading(false);
                            refresh();
                            Info.show(ids.size() > 1 ? Utils.textFormat(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.cases()) : property.getSingular(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.caseID()), Info.Type.INFO);
                        }
                    });
                }
            }
        });
        messageBox.open();
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

    @Override
    public boolean isCollapse() {
        return !Utils.hasOnlyRole(CLIENT);
    }

    @Override
    public void initStatistics(Integer parentId, Span container) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setLimit(1);
        initCaseList(fp, null, container);
    }

    public void restoreState() {
        if (listPanel == null) {
            return;
        }
        if (listPanel.isListingPage()) {
            super.restoreState();
        } else {
            listPanel.getKanbanBoardView().resetScrollPositions();
        }
    }

    @Override
    public String getPropertyCode() {
        return CASE_LIST;
    }

    private int getConvertItems(CaseItem rowValue, MenuBar menuBar, MenuBar convertMenu,
                                int convertItems, ConvertItem convertItem) {
        if (convertItem.getCode().contains("_FORM") && Utils.hasPermission(convertItem.getCode() + "_ADD_" + Utils.getCompanyID())) {
            final MenuPopItem convertToCF = new MenuPopItem(convertItem.getName(), "icon-send-sales-invoice");
            convertToCF.setCommand(() -> {
                convertToCF.closeAll(menuBar);
                SinksContainerFactory.entryPoint.onHistoryChanged(Constants.ITEM_LIST + "|add/add/" + convertItem.getEntityId() + "/" + convertItem.getCode() + "/CONVERT/" + RelationItem.TYPE_CASE + "/" + rowValue.getObjectId());
            });
            convertToCF.ensureDebugId("convert_to_" + convertItem.getName());
            convertMenu.addItem(convertToCF);
            convertItems++;
        }

        return convertItems;
    }
}
