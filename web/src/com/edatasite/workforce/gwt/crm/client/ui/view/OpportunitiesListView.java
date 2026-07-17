package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductService;
import com.edatasite.workforce.gwt.accounting.client.rpc.product.ProductServiceAsync;
import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.core.client.*;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.enums.ReferenceParentEnum;
import com.edatasite.workforce.gwt.core.client.localization.Reference;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterCutomField;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.kanbanItemSettings.KanbanItemColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.kanbanItemSettings.KanbanItemSettingEnum;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrOpportunityRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.ImportFileActionLink;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.landing.HelpPanelGenerator;
import com.edatasite.workforce.gwt.core.client.ui.listTable.ImportFilePopUp;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.DateTimePickerCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.DropDownCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.TextBoxCellEditor;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.CustomColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.shortcut.ShortcutItem;
import com.edatasite.workforce.gwt.core.client.ui.view.OpportunityPercentageStageModal;
import com.edatasite.workforce.gwt.crm.client.localization.CrmMessages;
import com.edatasite.workforce.gwt.crm.client.localization.CrmStrings;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMServiceAsync;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunitiesList;
import com.edatasite.workforce.gwt.crm.client.rpc.OpportunityListItem;
import com.edatasite.workforce.gwt.crm.client.ui.PhonePopup;
import com.edatasite.workforce.gwt.crm.client.ui.view.kanban.OpportunityMaterialCard;
import com.edatasite.workforce.gwt.crm.client.ui.view.quickadd.CrmQuickAdd;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.ActivityQuickAddForm;
import com.edatasite.workforce.gwt.materialkanban.client.KanbanBoard;
import com.edatasite.workforce.gwt.materialkanban.client.KanbanBoardDesign;
import com.edatasite.workforce.gwt.materialkanban.client.KanbanDataLoader;
import com.edatasite.workforce.gwt.materialkanban.client.KanbanDataRenderer;
import com.edatasite.workforce.gwt.materialkanban.client.rpc.KanbanService;
import com.edatasite.workforce.gwt.materialkanban.client.rpc.KanbanServiceAsync;
import com.edatasite.workforce.gwt.task.client.ui.quickadd.TaskQuickAddView;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.gen2.table.client.InlineCellEditor;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLabel;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Icon;
import gwt.material.design.client.ui.html.Span;

import java.util.*;

import static com.edatasite.workforce.gwt.core.client.rpc.RelationItem.*;

public class OpportunitiesListView extends BaseListView implements Constants, PermissionConstants {
    private static final Reference reference = Reference.App.get();
    private static final CrmStrings crmStrings = CrmStrings.App.get();
    private static final CrmMessages crmMessages = CrmMessages.App.get();
    private static final CRMServiceAsync crmService = CRMService.App.get();
    private static final ProductServiceAsync productService = ProductService.App.get();
    protected KanbanServiceAsync kanbanService = KanbanService.App.get();

    private ListingPanel<OpportunityListItem> listPanel;
    protected HashSet<OpportunityListItem> selectedItems = new HashSet();
    private OpportunityListItem currentItem = null;
    private static final NumberFormat numberFormat = NumberFormat.getFormat(",##0.00");
    private final Integer relationID;
    private String relationName;
    private CampaignPopup campaignPopup;
    private AssigneePopup assigneePopup;
    private ContextMenu actions;
    private ContextMenu actionsEmpty;
    private OpportunityListItem defaultOne;
    private final String relationType;
    private final boolean isClientView = Utils.hasRoles(CLIENT) && !Utils.hasCrmRole();
    private final boolean addPermission = Utils.hasPermission(CRM_ADD_NEW_OPPORTUNITIES);
    private final boolean quickAddPermission = Utils.hasPermission(CRM_QUICK_ADD_NEW_OPPORTUNITIES);
    private final boolean toSQ = Utils.hasPermission(CONVERT_OPPORTUNITY_TO_SQ);
    private final boolean toSO = Utils.hasPermission(CONVERT_OPPORTUNITY_TO_SO);
    private final boolean toRFQ = Utils.hasPermission(CONVERT_OPPORTUNITY_TO_RFQ);
    private final boolean toPROJECT = Utils.hasPermission(CONVERT_OPPORTUNITY_TO_PROJECT);
    private final boolean toPO = Utils.hasPermission(CONVERT_OPPORTUNITY_TO_PO);
    private final boolean toSI = Utils.hasPermission(CONVERT_OPPORTUNITY_TO_SI);
    private final boolean toPI = Utils.hasPermission(CONVERT_OPPORTUNITY_TO_PI);
    private final boolean toSMS = Utils.hasPermission(CRM_OPPORTUNITY_SEND_SMS);

    private boolean isFromContact = false;

    public OpportunitiesListView(Integer relationID, String relationType) {
        super(OPPORTUNITY_LIST);
        setDescription(property.getPlural(wfmStrings.opportunities()));
        this.relationID = relationID;
        this.relationType = relationType;
        if (quickAddPermission) {
            setAddNew(() -> new CrmQuickAdd(LayoutRPC.OPPORTUNITY_FORM, RelationItem.newEventRelation(relationType, relationID, relationName)));
        }
    }

    public OpportunitiesListView(Integer relationID, String relationType, boolean isFromContact) {
        this(relationID, relationType);
        this.isFromContact = isFromContact;
    }

    public FlowPanel getHelpContainer() {
        return HelpPanelGenerator.getHelpPanel(CRM_CONTEXT, CRM_OPPORTUNITIES_LIST);
    }

    private final ImportFilePopUp imp = new ImportFilePopUp(ImportTypeEnum.OPPORTUNITY, null);

    private void getRelationName() {
        if (relationID != null && relationType != null) {
            AllInOneService.App.get().getRelationName(relationID, relationType, new AbstractAsyncCallback<String>() {
                @Override
                public void onSuccess(String result) {
                    relationName = result;
                }
            });
        }
    }

    private void saveOpportunityEditCellValue(OpportunityListItem rowValue, final String columnCodeName) {
        crmService.saveOppotunityEditCellValue(rowValue, columnCodeName, new AbstractAsyncCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean result) {
                if (OpportunityListItem.STAGE.equals(columnCodeName)) {
                    Info.show(property.getPlural(wfmStrings.messSuccessfullyUpdated(), wfmStrings.opportunities()), Info.Type.INFO);
                    WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_OPPORTUNITY_LOAD_STAGE_HISTORY, true, OpportunitiesListView.this);
                    refresh();
                }
            }
        });
    }

    private CustomColumnDefinitionConfig[] getColumnConfig() {
        ArrayList<CustomColumnDefinitionConfig> columns = new ArrayList<>();
        ColumnDefinitionConfig columnConfig;

        columnConfig = new ColumnDefinitionConfig<OpportunityListItem, Widget>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Widget getCellValue(final OpportunityListItem item) {
                int actionItemCount = 0;
                final MenuBar menuBar = new MenuBar(true);
                menuBar.setAutoOpen(true);

                MenuPopItem opportunitySummary = new MenuPopItem(wfmStrings.summaryView(), "icon-opportunity-small",
                        () -> SinksContainerFactory.entryPoint.onHistoryChanged("opportunity|summary/" + item.getObjectId() + "/" + item.isConvertedLead() + "/" + item.getContactId() + "/" + item.getAccountId(), item.getNumberData() != null ? item.getNumberData().getNumberString() : item.getOpportunityName(), item.getOpportunityName()));

                opportunitySummary.ensureDebugId("opportunityView");
                actionItemCount++;
                menuBar.addItem(opportunitySummary);

                if (Utils.hasPermission(CRM_EDIT_OPPORTUNITIES) && item.isAllowEdit()) {
                    MenuPopItem opportunityEdit = new MenuPopItem(wfmStrings.edit(), "icon-edit", () -> SinksContainerFactory.entryPoint.onHistoryChanged("opportunity|edit/" + item.getObjectId(), item.getNumberData() != null ? item.getNumberData().getNumberString() : item.getOpportunityName(), item.getOpportunityName()));
                    opportunityEdit.ensureDebugId("editOpportunity");
                    actionItemCount++;
                    menuBar.addItem(opportunityEdit);
                }
                if (Utils.hasPermission(CRM_COPY_OPPORTUNITIES)) {
                    MenuPopItem copyOpportunity = new MenuPopItem(wfmStrings.copy(), "icon-copy", () -> SinksContainerFactory.entryPoint.onHistoryChanged("opportunity|add/add/" + item.getObjectId() + "/" + COPY));
                    copyOpportunity.ensureDebugId("copy_opportunity");
                    actionItemCount++;
                    menuBar.addItem(copyOpportunity);
                }

                if (Utils.hasPermission(CRM_ADD_OPPORTUNITY_NOTE, CRM_EDIT_OPPORTUNITIES)) {
                    MenuPopItem noteItem = new MenuPopItem(wfmStrings.addNote(), "icon-add-node", () -> new NotePopup(item.getObjectId(), RelationItem.TYPE_OPPORTUNITY));
                    noteItem.ensureDebugId("add_note");
                    actionItemCount++;
                    menuBar.addItem(noteItem);
                }

                if (Utils.hasPermission(CRM_OPPORTUNITY_CHANGE_STAGE, CRM_EDIT_OPPORTUNITIES)) {
                    MenuPopItem statusMenuPopItem = new MenuPopItem(wfmStrings.changeStage(), "icon-leads");
                    statusMenuPopItem.ensureDebugId("Change Stage");

                    /*
                     *  Append menu bar items
                     * */
                    MenuBar changeStage = new MenuBar(true);
                    changeStage.setAutoOpen(true);
                    if (listPanel.getDefaultOne().getStages() != null) {
                        for (final SelectItem stage : listPanel.getDefaultOne().getStages()) {
                            if (!stage.getId().equals(item.getStage().getId())) {
                                final MenuPopItem menuItem = new MenuPopItem(stage.getName());
                                menuItem.getElement().setId(stage.getId().toString());
                                menuItem.setCommand(() -> {
                                    menuItem.closeAll(menuBar);
                                    //change stage
                                    item.setStage(stage);
                                    //saving change stage
                                    if (stage.isDraggable() && item.isDraggable()) {
                                        if (Utils.isDoubleMessageEnable()) {
                                            WfmMessageBox changeStatusMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                                            changeStatusMessageBox.setMessage(crmMessages.doYouWantToChangeStatusTo(stage.getName()));
                                            changeStatusMessageBox.addCloseHandler(new CloseHandler() {
                                                @Override
                                                public void onSubmit() {
                                                    if ("0".equals(stage.getDescription()) || stage.isSelected()) {
                                                        new OpportunityPercentageStageModal(item, stage.isSelected(), "0".equals(stage.getDescription()));
                                                    } else {
                                                        saveOpportunityEditCellValue(item, OpportunityListItem.STAGE);
                                                    }
                                                }
                                            });
                                            changeStatusMessageBox.setTitle(wfmStrings.warning());
                                            changeStatusMessageBox.open();
                                        } else {
                                            if ("0".equals(stage.getDescription()) || stage.isSelected()) {
                                                new OpportunityPercentageStageModal(item, stage.isSelected(), "0".equals(stage.getDescription()));
                                            } else {
                                                saveOpportunityEditCellValue(item, OpportunityListItem.STAGE);
                                            }
                                        }
                                    } else {
                                        Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                                    }

                                });
                                changeStage.addItem(menuItem);
                            }
                        }
                    }
                    statusMenuPopItem.setSubMenu(changeStage);

                    actionItemCount++;
                    menuBar.addItem(statusMenuPopItem);
                }

                /*
                 *   Add Activity  Menu Item
                 * */

                if (Utils.hasPermission(CRM_TASKS_ADD, CRM_ADD_NEW_ACTIVITY_LOG_A_CALL, CRM_ADD_NEW_ACTIVITY_EVENT)) {
                    MenuPopItem addActivityMenuPopItem = new MenuPopItem(wfmStrings.add(), "icon-add-green");
                    addActivityMenuPopItem.ensureDebugId(wfmStrings.add());

                    /*
                     *  Append menu bar
                     * */
                    MenuBar addActivityMenu = new MenuBar(true);
                    addActivityMenu.setAutoOpen(true);

                    if (Utils.hasPermission(CRM_TASKS_ADD)) {
                        final MenuPopItem taskItem = new MenuPopItem(Property.get(Constants.TASK, wfmStrings.task(), wfmStrings.task()), "icon-add-task");
                        taskItem.ensureDebugId(Property.get(Constants.TASK, wfmStrings.addMess(), wfmStrings.task()));
                        taskItem.setCommand(() -> {
                            taskItem.closeAll(menuBar);
                            new TaskQuickAddView(RelationItem.newEventRelation(RelationItem.TYPE_OPPORTUNITY, item.getObjectId(), item.getOpportunityName()),
                                    RelationItem.newEventRelation(TYPE_CONTACT, item.getContactId(), item.getContact()),
                                    RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, item.getAccountId(), item.getAccount()));
                        });
                        addActivityMenu.addItem(taskItem);
                    }

                    /*
                     *  Append log menu item
                     * */
                    if (Utils.hasPermission(CRM_ADD_NEW_ACTIVITY_LOG_A_CALL)) {
                        final MenuPopItem logItem = new MenuPopItem(Property.get(Constants.LOGACALL, wfmStrings.logCall()), "icon-call");
                        logItem.ensureDebugId(Property.get(Constants.LOGACALL, wfmStrings.logCall()));
                        logItem.setCommand(() -> {
                            logItem.closeAll(menuBar);
                            new ActivityQuickAddForm(Appointment.CALL_LOG,
                                    RelationItem.newEventRelation(RelationItem.TYPE_OPPORTUNITY, item.getObjectId(), item.getOpportunityName()),
                                    RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, item.getAccountId(), item.getAccount()),
                                    RelationItem.newEventRelation(TYPE_CONTACT, item.getContactId(), item.getContact()));
                        });
                        addActivityMenu.addItem(logItem);
                    }

                    /*
                     *  Append schedule menu item
                     * */
                    if (Utils.hasPermission(CRM_ADD_NEW_ACTIVITY_EVENT)) {
                        final MenuPopItem scheduleItem = new MenuPopItem(Property.get(Constants.EVENT_LIST, crmStrings.scheduleEventMeeting()), "icon-schedile");
                        scheduleItem.ensureDebugId(Property.get(Constants.EVENT_LIST, crmStrings.scheduleEventMeeting()));
                        scheduleItem.setCommand(() -> {
                            scheduleItem.closeAll(menuBar);
                            new ActivityQuickAddForm(Appointment.EVENT, RelationItem.newEventRelation(RelationItem.TYPE_OPPORTUNITY, item.getObjectId(), item.getOpportunityName()), RelationItem.newEventRelation(RelationItem.TYPE_CRM_ACCOUNT, item.getAccountId(), item.getAccount()), RelationItem.newEventRelation(TYPE_CONTACT, item.getContactId(), item.getContact()));
                        });
                        addActivityMenu.addItem(scheduleItem);
                    }

                    addActivityMenuPopItem.setSubMenu(addActivityMenu);
                    actionItemCount++;
                    menuBar.addItem(addActivityMenuPopItem);
                }

                if (toSQ || toSO || toRFQ || toPROJECT || toPO || toPI || toSI) {
                    actionItemCount++;
                    menuBar.addItem(getConvertAction(item, menuBar));
                }

                PropertyItem propertyItem = Utils.getProperTy(Constants.Opportunities);
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


                if (Utils.hasPermission(CRM_REMOVE_OPPORTUNITIES)) {
                    MenuPopItem removeOpportunity = new MenuPopItem(wfmStrings.delete(), "icon-remove", () -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(wfmStrings.sureYouWantToDelete() );
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                ArrayList<Integer> objectIDs = new ArrayList<>();
                                objectIDs.add(item.getObjectId());
                                CRMService.App.get().deleteOpportunity(objectIDs, new AbstractAsyncCallback<ArrayList<Integer>>() {
                                    @Override
                                    public void failure(Throwable caught) {
                                    }

                                    @Override
                                    public void success(ArrayList<Integer> result) {
                                        Info.show(property.getSingular(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.opportunity()), Info.Type.INFO);
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_OPPORTUNITY_DELETED, result, OpportunitiesListView.this);
                                    }
                                });
                            }
                        });
                        messageBox.open();
                    });
                    removeOpportunity.ensureDebugId("delete");
                    actionItemCount++;
                    menuBar.addItem(removeOpportunity);
                }
                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columnConfig.setColumnSortable(false);
        columnConfig.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfig.setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns.add(columnConfig);
        //Number
        columnConfig = new ColumnDefinitionConfig<OpportunityListItem, Widget>(wfmStrings.number(), OpportunityListItem.NUMBER, 60) {
            @Override
            public Widget getCellValue(OpportunityListItem item) {
                Span label = new Span(item.getNumberData() != null ? item.getNumberData().getNumberString() : "");
                label.setStyleName("uploadLinkStyle2");
                label.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("opportunity|summary/" + item.getObjectId() + "/" + item.isConvertedLead() + "/" + item.getContactId() + "/" + item.getAccountId(), item.getNumberData() != null ? item.getNumberData().getNumberString() : item.getOpportunityName(), item.getOpportunityName()));
                return label;
            }
        };
        columnConfig.setShow(OpportunityListItem.defaultColumnNames.contains(OpportunityListItem.NUMBER));
        columnConfig.setMinimumColumnWidth(40);
        columns.add(columnConfig);
        //Name
        columnConfig = new ColumnDefinitionConfig<OpportunityListItem, Widget>(wfmStrings.name(), OpportunityListItem.OPPORTUNITY_NAME, 100) {
            @Override
            public Widget getCellValue(final OpportunityListItem item) {
                Span label = new Span(item.getOpportunityName() != null ? item.getOpportunityName() : "");
                label.setStyleName("uploadLinkStyle2");
                label.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("opportunity|summary/" + item.getObjectId() + "/" + item.isConvertedLead() + "/" + item.getContactId() + "/" + item.getAccountId(), item.getNumberData() != null ? item.getNumberData().getNumberString() : item.getOpportunityName(), item.getOpportunityName()));
                return label;
            }
        };
        columnConfig.setShow(OpportunityListItem.defaultColumnNames.contains(OpportunityListItem.OPPORTUNITY_NAME));
        columnConfig.setMinimumColumnWidth(40);
        columns.add(columnConfig);
        //Amount
        if (Utils.hasPermission(CRM_EDIT_OPPORTUNITIES)) {
            columnConfig = new ColumnDefinitionConfig<OpportunityListItem, Widget>(wfmStrings.amount(), OpportunityListItem.AMOUNT, 70) {
                @Override
                public Widget getCellValue(OpportunityListItem item) {
                    currentItem = item;
                    return getAmountWidget(item);
                }

                @Override
                public void setCellValue(OpportunityListItem rowValue, Widget value) {
                    if (!rowValue.isAmountWidgetDisable() && rowValue.isAllowEdit()) {
                        saveCellValue(rowValue);
                    } else {
                        Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                        refresh();
                    }
                }
            };
        } else {
            columnConfig = new ColumnDefinitionConfig<OpportunityListItem, String>(wfmStrings.amount(), OpportunityListItem.AMOUNT, 70) {
                @Override
                public String getCellValue(OpportunityListItem item) {
                    currentItem = item;
                    return numberFormat.format(item.getAmount() != null ? item.getAmount() : 0d);
                }
            };
        }
        columnConfig.setShow(OpportunityListItem.defaultColumnNames.contains(OpportunityListItem.AMOUNT));
        columnConfig.setMinimumColumnWidth(40);
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_RIGHT);
        columns.add(columnConfig);
        //Stage
        if (Utils.hasPermission(CRM_EDIT_OPPORTUNITIES)) {
            columnConfig = new ColumnDefinitionConfig<OpportunityListItem, SelectItem>(wfmStrings.stage(), OpportunityListItem.STAGE, 70) {
                @Override
                public SelectItem getCellValue(OpportunityListItem item) {
                    return item.getStage();
                }

                @Override
                public void setCellValue(OpportunityListItem rowValue, SelectItem cellValue) {
                    if (cellValue != null && cellValue.isDraggable() && rowValue.isDraggable()) {
                        rowValue.setStage(cellValue);
                        if (Utils.isDoubleMessageEnable()) {
                            WfmMessageBox changeStatusMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                            changeStatusMessageBox.setMessage(crmMessages.doYouWantToChangeStatusTo(cellValue.getName()));
                            changeStatusMessageBox.addCloseHandler(new CloseHandler() {
                                @Override
                                public void onSubmit() {
                                    saveCellValue(rowValue);
                                }

                                @Override
                                public void onCancel() {
                                    listPanel.reloadPage();
                                }
                            });
                            changeStatusMessageBox.setTitle(wfmStrings.warning());
                            changeStatusMessageBox.open();
                        } else {
                            saveCellValue(rowValue);
                        }
                    } else {
                        Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                    }
                }
            };
        } else {
            columnConfig = new ColumnDefinitionConfig<OpportunityListItem, String>(wfmStrings.stage(), OpportunityListItem.STAGE, 70) {
                @Override
                public String getCellValue(OpportunityListItem item) {
                    return item.getStage() != null ? item.getStage().getName() : "";
                }
            };
        }

        columnConfig.addColor(new ColumnColor(reference.QUALIFICATION(), "r", "2BBF57"));
        columnConfig.addColor(new ColumnColor(reference.CLOSED_WON(), "r", "007DE7"));
        columnConfig.addColor(new ColumnColor(reference.CLOSED_LOST(), "c", "DC0C0C"));
        columnConfig.setShow(OpportunityListItem.defaultColumnNames.contains(OpportunityListItem.STAGE));
        columnConfig.setMinimumColumnWidth(40);
        columns.add(columnConfig);
        //Closing Date
        if (Utils.hasPermission(CRM_EDIT_OPPORTUNITIES)) {
            columnConfig = new ColumnDefinitionConfig<OpportunityListItem, String>(wfmStrings.closeDate(), OpportunityListItem.CLOSING_DATE, 50) {
                @Override
                public String getCellValue(OpportunityListItem item) {
                    return DateUtils.format(item.getClosingDate());
                }

                @Override
                public void setCellValue(OpportunityListItem rowValue, String cellValue) {
                    try {
                        if (!rowValue.isCloseDateDisable() && rowValue.isAllowEdit()) {
                            rowValue.setClosingDate(DateUtils.parse(cellValue));
                            saveCellValue(rowValue);
                        } else {
                            Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                            refresh();
                        }
                    } catch (DateFormatException e) {
                        e.printStackTrace();
                    }
                }
            };
        } else {
            columnConfig = new ColumnDefinitionConfig<OpportunityListItem, String>(wfmStrings.closeDate(), OpportunityListItem.CLOSING_DATE, 50) {
                @Override
                public String getCellValue(OpportunityListItem item) {
                    return DateUtils.format(item.getClosingDate());
                }
            };
        }
        columnConfig.setShow(OpportunityListItem.defaultColumnNames.contains(OpportunityListItem.CLOSING_DATE));
        columnConfig.setMinimumColumnWidth(40);
        columns.add(columnConfig);
        //Account
        columnConfig = new ColumnDefinitionConfig<OpportunityListItem, HTML>(Property.get(Constants.CRM_ACCOUNT_LIST, wfmStrings.company()), OpportunityListItem.ACCOUNT_NAME, 80) {
            @Override
            public HTML getCellValue(OpportunityListItem item) {
                if (Utils.hasPermission(CRM_ACCOUNTS_SUMMARY)) {
                    return item.getAccountId() != null ? getLink(item.getAccount(), "account|summary/" + item.getAccountId(), item.getAccountNumber(), item.getAccount()) : null;
                } else {
                    return item.getAccountId() != null ? getLink(item.getAccount(), null) : null;
                }
            }
        };
        columnConfig.setShow(OpportunityListItem.defaultColumnNames.contains(OpportunityListItem.ACCOUNT_NAME));
        columnConfig.setMinimumColumnWidth(40);
        columns.add(columnConfig);
        //Assignee
        columnConfig = new ColumnDefinitionConfig<OpportunityListItem, String>(wfmStrings.assignee(), OpportunityListItem.ASSIGNEE_NAME, 100) {
            @Override
            public String getCellValue(OpportunityListItem item) {
                return item.getAssignee();
            }
        };
        columnConfig.setShow(OpportunityListItem.defaultColumnNames.contains(OpportunityListItem.ASSIGNEE_NAME));
        columnConfig.setMinimumColumnWidth(40);
        columns.add(columnConfig);
        //Country
        columnConfig = new ColumnDefinitionConfig<OpportunityListItem, String>(wfmStrings.country(), OpportunityListItem.COUNTRY_NAME, 80) {
            @Override
            public String getCellValue(OpportunityListItem item) {
                return null != item.getCountryName() ? item.getCountryName() : "";
            }
        };
        columnConfig.setShow(OpportunityListItem.defaultColumnNames.contains(OpportunityListItem.COUNTRY_NAME));
        columnConfig.setMinimumColumnWidth(40);
        columns.add(columnConfig);
        //Probability
        columnConfig = new ColumnDefinitionConfig<OpportunityListItem, String>(wfmStrings.probability(), OpportunityListItem.PROBABILITY, 80) {
            @Override
            public String getCellValue(OpportunityListItem item) {
                return item.getProbability() != null ? item.getProbability() + "%" : "";
            }
        };
        columnConfig.setShow(OpportunityListItem.defaultColumnNames.contains(OpportunityListItem.PROBABILITY));
        columnConfig.setMinimumColumnWidth(40);
        columns.add(columnConfig);
        //Converted
        columnConfig = new ColumnDefinitionConfig<OpportunityListItem, String>(Property.get(Constants.PROJECT, crmStrings.isConvertedToProject(), wfmStrings.project()), OpportunityListItem.ISCONVERTEDTOPROJECT, 80) {
            @Override
            public String getCellValue(OpportunityListItem item) {
                return item.isConvertedToProject() ? wfmStrings.yes() : wfmStrings.no();
            }
        };
        columnConfig.setShow(OpportunityListItem.defaultColumnNames.contains(OpportunityListItem.ISCONVERTEDTOPROJECT));
        columnConfig.setMinimumColumnWidth(40);
        columns.add(columnConfig);
        //Campaign
        columnConfig = new ColumnDefinitionConfig<OpportunityListItem, String>(wfmStrings.campaign(), OpportunityListItem.CAMPAIGN, 70) {
            @Override
            public String getCellValue(OpportunityListItem item) {
                return item.getCampaign();
            }
        };
        columnConfig.setShow(OpportunityListItem.defaultColumnNames.contains(OpportunityListItem.CAMPAIGN));
        columnConfig.setMinimumColumnWidth(40);
        columns.add(columnConfig);

        //related to columns
        columnConfig = new ColumnDefinitionConfig<OpportunityListItem, String>(Property.get(Constants.Contacts, wfmStrings.relatedToEvent(), wfmStrings.contact()), TYPE_CONTACT, 80) {
            @Override
            public String getCellValue(OpportunityListItem item) {
                return item.getRelationValueMap().get(TYPE_CONTACT);
            }
        };
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(60);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<OpportunityListItem, String>(Property.get(Constants.LEADS, wfmStrings.relatedToEvent(), wfmStrings.lead()), RelationItem.TYPE_LEAD, 80) {
            @Override
            public String getCellValue(OpportunityListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_LEAD);
            }
        };
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(60);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<OpportunityListItem, String>(wfmStrings.relatedCrmAccount(), RelationItem.TYPE_CRM_ACCOUNT, 80) {
            @Override
            public String getCellValue(OpportunityListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_CRM_ACCOUNT);
            }
        };
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(60);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<OpportunityListItem, String>(Property.get(Constants.CASE_LIST, wfmStrings.relatedToEvent(), wfmStrings.crmCase()), RelationItem.TYPE_CASE, 80) {
            @Override
            public String getCellValue(OpportunityListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_CASE);
            }
        };
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(60);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<OpportunityListItem, String>(Property.get(Constants.TASK, wfmStrings.relatedToEvent(), wfmStrings.task()), RelationItem.TYPE_TASK, 80) {
            @Override
            public String getCellValue(OpportunityListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_TASK);
            }
        };
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(60);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<OpportunityListItem, String>(Property.get(Constants.EVENT_LIST, wfmStrings.relatedEvent(), wfmStrings.event()), RelationItem.TYPE_EVENT, 80) {
            @Override
            public String getCellValue(OpportunityListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_EVENT);
            }
        };
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(60);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);

        columnConfig = new ColumnDefinitionConfig<OpportunityListItem, String>(Property.get(Constants.PROJECT, wfmStrings.relatedToEvent(), wfmStrings.project()), RelationItem.TYPE_PROJECT, 80) {
            @Override
            public String getCellValue(OpportunityListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_PROJECT);
            }
        };
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(60);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);

        //related issue
        columnConfig = new ColumnDefinitionConfig<OpportunityListItem, String>(Property.get(Constants.ISSUE, wfmStrings.relatedIssue(), wfmStrings.issue()), RelationItem.TYPE_ISSUE, 80) {
            @Override
            public String getCellValue(OpportunityListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_ISSUE);
            }
        };
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(60);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);

        //related employee
        columnConfig = new ColumnDefinitionConfig<OpportunityListItem, String>(wfmStrings.relatedEmployee(), RelationItem.TYPE_EMPLOYEE, 80) {
            @Override
            public String getCellValue(OpportunityListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_EMPLOYEE);
            }
        };
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(60);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);

        //related department
        columnConfig = new ColumnDefinitionConfig<OpportunityListItem, String>(Property.get(Constants.DEPARTMENT_LIST, wfmStrings.relatedDepartment(), wfmStrings.department()), RelationItem.TYPE_DEPARTMENT, 80) {
            @Override
            public String getCellValue(OpportunityListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_DEPARTMENT);
            }
        };
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(60);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);

        //related client
        columnConfig = new ColumnDefinitionConfig<OpportunityListItem, String>(Property.get(Constants.CLIENT_LIST, wfmStrings.relatedClient()), RelationItem.TYPE_CLIENT, 80) {
            @Override
            public String getCellValue(OpportunityListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_CLIENT);
            }
        };
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(60);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);

        //related supplier
        columnConfig = new ColumnDefinitionConfig<OpportunityListItem, String>(Property.get(Constants.SUPPLIER_LIST, wfmStrings.relatedSupplier()), RelationItem.TYPE_SUPPLIER, 80) {
            @Override
            public String getCellValue(OpportunityListItem item) {
                return item.getRelationValueMap().get(RelationItem.TYPE_SUPPLIER);
            }
        };
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(60);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);

        //Created
        columnConfig = new ColumnDefinitionConfig<OpportunityListItem, String>(wfmStrings.createdDate(), OpportunityListItem.CREATED_DATE, 50) {
            @Override
            public String getCellValue(OpportunityListItem item) {
                return DateUtils.formatInternal(item.getCreatedDate());
            }
        };
        columnConfig.setShow(OpportunityListItem.defaultColumnNames.contains(OpportunityListItem.CREATED_DATE));
        columnConfig.setMinimumColumnWidth(40);
        columns.add(columnConfig);
        //Updated
        columnConfig = new ColumnDefinitionConfig<OpportunityListItem, String>(wfmStrings.modifiedDate(), OpportunityListItem.UPDATED_DATE, 50) {
            @Override
            public String getCellValue(OpportunityListItem item) {
                return DateUtils.formatInternal(item.getUpdatedDate());
            }
        };
        columnConfig.setShow(OpportunityListItem.defaultColumnNames.contains(OpportunityListItem.UPDATED_DATE));
        columnConfig.setMinimumColumnWidth(40);
        columns.add(columnConfig);
        //Creator
        columnConfig = new ColumnDefinitionConfig<OpportunityListItem, HTML>(wfmStrings.createdBy(), OpportunityListItem.CREATOR_NAME, 100) {
            @Override
            public HTML getCellValue(OpportunityListItem item) {
                return getLink(item.getCreatorName(), "contact|summary/" + item.getCreatorID(), item.getCreatorName(), item.getCreatorName());
            }
        };
        columnConfig.setShow(OpportunityListItem.defaultColumnNames.contains(OpportunityListItem.CREATOR_NAME));
        columnConfig.setMinimumColumnWidth(80);
        columns.add(columnConfig);
        //Backup Assignee
        columnConfig = new ColumnDefinitionConfig<OpportunityListItem, String>(wfmStrings.backupAssignee(), OpportunityListItem.BACKUP_ASSIGNEE_NAME, 100) {
            @Override
            public String getCellValue(OpportunityListItem item) {
                return item.getBackupAssignee();
            }
        };
        columnConfig.setShow(OpportunityListItem.defaultColumnNames.contains(OpportunityListItem.BACKUP_ASSIGNEE_NAME));
        columnConfig.setMinimumColumnWidth(80);
        columns.add(columnConfig);
        //Contact
        columnConfig = new ColumnDefinitionConfig<OpportunityListItem, Widget>(Property.get(Constants.Contacts, wfmStrings.contact()), OpportunityListItem.OPPORTUNITY_CONTACT_NAME, 100) {
            @Override
            public Widget getCellValue(OpportunityListItem item) {
                Span label = new Span(item.getContact() != null ? item.getContact() : "");
                label.setStyleName("uploadLinkStyle2");
                label.addClickHandler(clickEvent -> SinksContainerFactory.entryPoint.onHistoryChanged("contact|summary/" + item.getContactId(), item.getContact(), item.getContact()));
                return label;
            }
        };
        columnConfig.setShow(OpportunityListItem.defaultColumnNames.contains(OpportunityListItem.OPPORTUNITY_CONTACT_NAME));
        columnConfig.setMinimumColumnWidth(80);
        columns.add(columnConfig);

        //Contact Phone
        columnConfig = new ColumnDefinitionConfig<OpportunityListItem, Div>(wfmStrings.phone(), OpportunityListItem.OPPORTUNITY_CONTACT_PHONE, 100) {
            @Override
            public Div getCellValue(OpportunityListItem item) {
                PhonePopup phonePopup = new PhonePopup(item.getContactPrimaryPhone(), TYPE_OPPORTUNITY, item.getObjectId(), item.getOpportunityName(), false, true, item, TYPE_CONTACT, null, item.getContactId());
                return phonePopup.getPhoneWidget();
            }
        };
        columnConfig.setShow(OpportunityListItem.defaultColumnNames.contains(OpportunityListItem.OPPORTUNITY_CONTACT_PHONE));
        columnConfig.setMinimumColumnWidth(80);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);
        //Contact Email
        columnConfig = new ColumnDefinitionConfig<OpportunityListItem, HTML>(wfmStrings.email(), OpportunityListItem.OPPORTUNITY_CONTACT_EMAIL, 100) {
            @Override
            public HTML getCellValue(OpportunityListItem item) {
                return getEmailLink(item);
            }
        };
        columnConfig.setShow(OpportunityListItem.defaultColumnNames.contains(OpportunityListItem.OPPORTUNITY_CONTACT_EMAIL));
        columnConfig.setMinimumColumnWidth(80);
        columnConfig.setColumnSortable(false);
        columns.add(columnConfig);
        if (Utils.hasPermission(CRM_EDIT_OPPORTUNITIES)) {
            //Lead Source
            columnConfig = new ColumnDefinitionConfig<OpportunityListItem, SelectItem>(wfmStrings.source(), OpportunityListItem.OPPORTUNITY_LEAD_SOURCE, 100) {
                @Override
                public SelectItem getCellValue(OpportunityListItem item) {

                    return new SelectItem(item.getLeadSourceId(), item.getLeadSource());
                }

                @Override
                public void setCellValue(OpportunityListItem rowValue, SelectItem cellValue) {
                    if (!rowValue.isLeadSourceDisable() && rowValue.isAllowEdit()) {
                        if (cellValue != null) {
                            rowValue.setLeadSource(cellValue.getId() != null && cellValue.getId() > 0 ? cellValue.getName() : null);
                            rowValue.setLeadSourceId(cellValue.getId());
                        } else {
                            rowValue.setLeadSource(null);
                            rowValue.setLeadSourceId(null);
                        }
                        saveCellValue(rowValue);
                    } else {
                        Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                        refresh();
                    }
                }
            };
        } else {
            columnConfig = new ColumnDefinitionConfig<OpportunityListItem, String>(wfmStrings.source(), OpportunityListItem.OPPORTUNITY_LEAD_SOURCE, 100) {
                @Override
                public String getCellValue(OpportunityListItem item) {
                    return item.getLeadSource();
                }
            };
        }
        columnConfig.setShow(OpportunityListItem.defaultColumnNames.contains(OpportunityListItem.OPPORTUNITY_LEAD_SOURCE));
        columnConfig.setMinimumColumnWidth(80);
        columns.add(columnConfig);

        //Attachment
        columnConfig = new ColumnDefinitionConfig<OpportunityListItem, Widget>(wfmStrings.attachment(), OpportunityListItem.OPPORTUNITY_ATTACHMENT, 60) {
            @Override
            public Widget getCellValue(OpportunityListItem item) {

                Icon icon = new Icon();
                icon.addStyleName("ficon--upload");

                return item.hasAttachments() ? icon : new HTML("");
            }
        };
        columnConfig.setShow(false);
        columnConfig.setMinimumColumnWidth(45);
        columns.add(columnConfig);

        if (Utils.hasPermission(CRM_EDIT_OPPORTUNITIES)) {
            initCellEdit(CustomColumnDefinitionConfig.getEditableColumns(columns));
        }
        return columns.toArray(new ColumnDefinitionConfig[]{});
    }

    public HTML getEmailLink(final OpportunityListItem rowValue) {
        SimpleLink sendEmailLink = new SimpleLink("");
        if (rowValue.getContactPrimaryEmail() != null && !"".equals(rowValue.getContactPrimaryEmail().trim())) {
            sendEmailLink = new SimpleLink(rowValue.getContactPrimaryEmail());
            sendEmailLink.addClickHandler(clickEvent -> {
                if (!rowValue.isContactEmailOptOut()) {
                    //new ComposeView(rowValue.getContactPrimaryEmail(), RelationItem.newEventRelation(RelationItem.TYPE_CONTACT, rowValue.getContactId(), rowValue.getContact()));
                    SinksContainerFactory.entryPoint.onHistoryChanged("emailcompose|add/add/" + rowValue.getContactPrimaryEmail() + "/" + TYPE_CONTACT + "/" + rowValue.getContactId() + "/" + rowValue.getContact() + "/" + RelationItem.TYPE_OPPORTUNITY + "/" + rowValue.getObjectId() + "/" + rowValue.getOpportunityName());
                } else {
                    WfmMessageBox messageBox = new WfmMessageBox(IconEnum.INFO, Action.OK, crmMessages.theEmailOutIsEnabled());
                    messageBox.setTitle(wfmStrings.information());
                    messageBox.open();
                }
            });
        }
        return sendEmailLink;
    }

    @Override
    protected Widget onInitialize() {
        campaignPopup = new CampaignPopup(RelationItem.TYPE_OPPORTUNITY);
        campaignPopup.setListRefresh(() -> refresh());

        assigneePopup = new AssigneePopup(TYPE_OPPORTUNITY);
        assigneePopup.setListRefresh(this::refresh);

        listPanel = new GuideListingPanel(ListPanelType.OpportunitiesListPanel, getColumnConfig(), getListProvider(),
                getListDesign(), isClientView ? null : SelectionGrid.SelectionPolicy.CHECKBOX);

        listPanel.setCustomFieldsEditCellSaveChanges((rowValue, columnCodeName) -> saveOpportunityEditCellValue((OpportunityListItem) rowValue, columnCodeName));
        listPanel.setExcelListener(clickEvent -> {
            if (listPanel.getItemCount() > 1000) {
                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
            }
            ListingFilterParameter listingFilterParameter = listPanel.getFilterParametrs();
            if (listingFilterParameter == null) {
                listingFilterParameter = new ListingFilterParameter();
            }
            listingFilterParameter.setFacetFilterJson(Utils.facetFilterRpcToJsonString(listingFilterParameter.getFacetFilter()));
            listingFilterParameter.setListPanelToolJson(Utils.listPanelToolRpcConvertJsonData(listingFilterParameter.getListPanelTool()));
            String excelURL = CommandConstants.COMMON_URL + "/downloadCrmOppotunitiesExcel";
            listingFilterParameter.setPropertyCode(getPropertyCode());
            listPanel.callListExcel(excelURL, listingFilterParameter);
        });
        listPanel.setPDFListener(clickEvent -> {
            if (listPanel.getItemCount() > 1000) {
                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
            }
            String pdfURL = CommandConstants.PDF_URL + "/opportunitiesListPDFHandler";
            ListingFilterParameter fp = listPanel.getFilterParametrs();
            fp.setPropertyCode(getPropertyCode());
            listPanel.callListPDF(pdfURL, fp);
        });
        getRelationName();
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_OPPORTUNITY_ADD_EDIT, OpportunitiesListView.this, (sender, args) -> {
            if (listPanel.isListingPage()) {
                listPanel.reloadPage();
            } else {
                listPanel.requestKanbanData();
            }
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_OPPORTUNITY_DELETED, OpportunitiesListView.this, (sender, args) -> {
            if (listPanel.isListingPage()) {
                listPanel.reloadPage();
            } else {
                listPanel.requestKanbanData();
            }
        });
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TENDER_CARD_ADD_EDIT, OpportunitiesListView.this, (sender, args) -> {
            if (listPanel.isListingPage()) {
                listPanel.reloadPage();
            } else {
                listPanel.requestKanbanData();
            }
        });
        listPanel.addSelectionRowHandler(selectedRows -> selectedItems = selectedRows);
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_OPPORTUNITY_LIST_RELOAD, OpportunitiesListView.this, (sender, args) -> {
            if (listPanel.isListingPage()) {
                listPanel.reloadPage();
            } else {
                listPanel.requestKanbanData();
            }
        });

        add(listPanel);

        KanbanBoard<OpportunityListItem> kanbanBoard = new KanbanBoard<OpportunityListItem>(ListPanelType.OpportunitiesKanbanPanel, getKanbanDataLoader(), getKanbanBoardDesign(), true) {
            @Override
            public MaterialPanel getColumnHeaderTotal(SelectItem columnMetadata) {
                MaterialPanel total = new MaterialPanel("wg_canban__column-body-top");

                if (columnMetadata.getDescription() != null) {
                    MaterialLabel probability = new MaterialLabel(columnMetadata.getDescription() + "%");
                    probability.setStyleName("wg_canban__probability-total");
                    total.add(probability);
                }

                MaterialLabel totalAmount = new MaterialLabel("0.00");
                totalAmount.setStyleName("wg_canban__oportunity-total");
                total.add(totalAmount);
                return total;
            }

            @Override
            public Widget getColumnAddButton(SelectItem columnMetadata) {
                if (addPermission) {
                    MaterialLink addOpportunityLink = new MaterialLink();
                    addOpportunityLink.setStyleName("wg_canban__add-card");
                    Icon plus = new Icon();
                    plus.setStyleName("ficon--plus");
                    addOpportunityLink.add(plus);
                    addOpportunityLink.addClickHandler(click -> SinksContainerFactory.entryPoint.onHistoryChanged("opportunity|add/add" + "/Kanban/" + columnMetadata.getId()));
                    return addOpportunityLink;
                } else if (quickAddPermission || addPermission) {
                    MaterialLink quicAddOpportunityLink = new MaterialLink();
                    quicAddOpportunityLink.setStyleName("wg_canban__add-card");
                    Icon plus = new Icon();
                    plus.setStyleName("ficon--plus");
                    quicAddOpportunityLink.add(plus);
                    quicAddOpportunityLink.addClickHandler((ClickEvent click) -> new CrmQuickAdd(LayoutRPC.OPPORTUNITY_FORM, columnMetadata.getId(),
                            RelationItem.newEventRelation(relationType, relationID, relationName)));
                    return quicAddOpportunityLink;
                }
                return super.getColumnAddButton(columnMetadata);
            }
        };
        kanbanBoard.setKanbanItemSettingsType(KanbanItemSettingEnum.OPPORTUNITY_ITEM_SETTINGS);
        if (!Utils.hasPermission(PermissionConstants.CRM_OPPORTUNITIES_LIST_CUSTOMIZE_BUTTON)){
            kanbanBoard.setDropable(false);
        }
        listPanel.setKanbanBoardView(kanbanBoard);

        return null;
    }

    private HorizontalPanel getAmountWidget(OpportunityListItem item) {
        HorizontalPanel panel = new HorizontalPanel();
//        panel.setWidth("85%");
        String currency = (!"".equals(item.getCurrency()) && item.getCurrency() != null) ? "  " + "(" + item.getCurrency() + ")" : "";
        Label label = new Label(numberFormat.format(item.getAmount() != null ? item.getAmount() : 0d) + currency);
        panel.add(label);
        panel.setCellHorizontalAlignment(label, HasHorizontalAlignment.ALIGN_RIGHT);
        panel.setCellVerticalAlignment(label, HasVerticalAlignment.ALIGN_MIDDLE);
        return panel;
    }

    private void initCellEdit(Map<String, CustomColumnDefinitionConfig> columns) {
        for (final Map.Entry<String, CustomColumnDefinitionConfig> entry : columns.entrySet()) {
            InlineCellEditor widget = null;
            CustomColumnDefinitionConfig column = entry.getValue();
            if (OpportunityListItem.OPPORTUNITY_LEAD_SOURCE.equals(entry.getKey())) {
                InlineCellEditor editor = new DropDownCellEditor<SelectItem>() {
                    @Override
                    protected SelectItem getValue() {
                        return getListBox().getSelectedItem();
                    }

                    @Override
                    protected void setValue(SelectItem cellValue) {
                        getListBox().setItems(defaultOne.getLeadSources());
                    }
                };
                widget = editor;
            } else if (OpportunityListItem.CLOSING_DATE.equals(entry.getKey())) {
                DateTimePickerCellEditor<String> startDateTimePickerCellEditor = new DateTimePickerCellEditor<String>() {
                    @Override
                    protected String getValue() {
                        return !getDateTimePicker().isAllDay() ? DateUtils.formatInternal1(getDate()) : DateUtils.format1(getDate());
                    }

                    @Override
                    protected void setValue(String cellValue) {
                        if (cellValue.contains("AM") || cellValue.contains("PM")) {
                            try {
                                setDate(DateUtils.parseLongFormat(cellValue), true);
                            } catch (DateFormatException e) {
                                e.printStackTrace();
                            }
                        } else {
                            try {
                                setDate(DateUtils.parse(cellValue), false);
                            } catch (DateFormatException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                };
                widget = startDateTimePickerCellEditor;
            } else if (OpportunityListItem.STAGE.equals(entry.getKey())) {
                final DropDownCellEditor<SelectItem> stageCellEditor = new DropDownCellEditor<SelectItem>() {
                    @Override
                    protected SelectItem getValue() {
                        return getListBox().getSelectedItem();
                    }

                    @Override
                    protected void setValue(SelectItem cellValue) {
                        getListBox().setItems(defaultOne.getStages());
                    }
                };
                stageCellEditor.getListBox().setWithoutNullLabel(true);
                widget = stageCellEditor;

                column.setCellEditor(widget);
                column.setCellChangesSave((rowValue, columnCodeName) -> {

                    if (((OpportunityListItem) rowValue).getStage() != null && ("0".equals(((OpportunityListItem) rowValue).getStage().getDescription()) || ((OpportunityListItem) rowValue).getStage().isSelected())) {
                        new OpportunityPercentageStageModal((OpportunityListItem) rowValue, ((OpportunityListItem) rowValue).getStage().isSelected(), "0".equals(((OpportunityListItem) rowValue).getStage().getDescription()));
                    } else {
                        saveOpportunityEditCellValue((OpportunityListItem) rowValue, columnCodeName);
                    }
                });

            } else if (OpportunityListItem.AMOUNT.equals(entry.getKey())) {
                TextBoxCellEditor<Object> amount = new TextBoxCellEditor<Object>() {
                    @Override
                    protected Object getValue() {
                        currentItem.setAmount(Double.parseDouble(getTextBox().getText()));
                        return getAmountWidget(currentItem);
                    }

                    @Override
                    protected void setValue(Object cellValue) {
                        getTextBox().setText(currentItem.getAmount() == null ? "" : String.valueOf(currentItem.getAmount()));
                    }
                };
                amount.addNumberValidation(false);
                widget = amount;
            }
            if (widget != null && !OpportunityListItem.STAGE.equals(entry.getKey())) {
                column.setCellEditor(widget);
                column.setCellChangesSave((rowValue, columnCodeName) -> saveOpportunityEditCellValue((OpportunityListItem) rowValue, columnCodeName));
            }
        }
    }

    private boolean isShowImport() {
        return Utils.hasPermission(CRM_OPPORTUNITIES_IMPORT_LIST);
    }

    private boolean isShowExport() {
        return Utils.hasPermission(CRM_OPPORTUNITIES_EXPORT_LIST);
    }

    private GuideListingPanelDesign getListDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                return () -> {
                    if (isFromContact) {
                        if (Utils.hasPermission(PermissionConstants.CRM_ADD_OPPORTUNITY_FROM_CONTACT)) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("opportunity|add/add" + "/" + RelationItem.TYPE_CAMPAIGN.equals(relationType) + "/" + relationID + "/" + relationName + "/" + FROM_CONTACT);
                        } else {
                            Info.warn(wfmStrings.youDontHavePermission());
                            return;
                        }
                    }
                    if (!addPermission) {
                        return;
                    }
                    if (RelationItem.TYPE_CRM_ACCOUNT.equals(relationType)) {
                        SinksContainerFactory.entryPoint.onHistoryChanged("opportunity|add/add//" + relationID + "/" + Utils.encrypt(relationName));
                    } else {
                        if (TYPE_CONTACT.equals(relationType)) {
                            SinksContainerFactory.entryPoint.onHistoryChanged("opportunity|add/add" + "/" + RelationItem.TYPE_CAMPAIGN.equals(relationType) + "/" + relationID + "/" + relationName + "/" + FROM_CONTACT);
                        } else {
                            SinksContainerFactory.entryPoint.onHistoryChanged("opportunity|add/add" + "/" + RelationItem.TYPE_CAMPAIGN.equals(relationType) + "/" + relationID + "/" + relationName);
                        }
                    }
                };
            }

            @Override
            public Command getUploadButtonCommand() {
                return isShowImport() ? () -> imp.open() : null;
            }

            @Override
            public ListingFacetFilter initFacetFilter() {
                return new ListingFacetFilter() {
                    @Override
                    public FacetCallbackProvider getFacetCallbackProvider() {
                        return (data, callback) -> {
                            if (relationID != null) {
                                data.setCustomDataPut(FacetFilterCutomField.RELATION_ID, relationID.toString());
                            }
                            if (relationType != null) {
                                data.setCustomDataPut(FacetFilterCutomField.RELATION_TYPE, relationType);
                            }
                            RbacService.App.get().getOpportunityFacetFilterData(data, new AbstractAsyncCallback<FacetFilterRpc>() {
                                public void failure(Throwable caught) {
                                    callback.onFailure(caught);
                                }

                                public void success(FacetFilterRpc data) {
                                    callback.onSuccess(data);
                                }
                            });
                        };
                    }

                    @Override
                    public FacetContentConfigure getFacetFilterContentconfigure() {
                        return getFacetContentConfigure();
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarMore() {
                final ActionButton more = new ActionButton(ActionButton.getMoreString(), "", ActionButton.Type.TOOLMENU);
                more.ensureDebugId("crm_opportunities_more_id");
                more.addDomHandler(mouseOverEvent -> {
                    MenuBar menuBar = getActionsForSelections();
                    menuBar.setAutoOpen(true);
                    more.setMenu(menuBar);
                    menuBar.setLayoutData(more);
                }, MouseOverEvent.getType());
                return more;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (isFromContact) {
                    if (Utils.hasPermission(PermissionConstants.CRM_ADD_OPPORTUNITY_FROM_CONTACT)) {
                        return addButton();
                    }
                }
                if (addPermission && quickAddPermission) {
                    return addButton();
                } else if (addPermission) {
                    return fullAddButton();
                } else if (quickAddPermission) {
                    return quickAddButton();
                }
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                imp.setSubmitCompleted(() -> {
                    if (imp.getObjectId() != null) {
                        goTo("importopportunity|add/add/" + imp.getObjectId());
                    }
                });

                if (isShowImport()) {
                    ImportFileActionLink link = new ImportFileActionLink();
                    link.addClickHandler(ch -> imp.open());
                    menuContainer.add(link);
                }
                exportOption.initExport(null, isShowExport());
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return Utils.hasPermission(CRM_EDIT_OPPORTUNITIES);
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(property.getPlural(crmStrings.messCurrentlyOpprtunities(), wfmStrings.opportunities()));
                if (quickAddPermission) {
                    message.setHref(clickEvent -> new CrmQuickAdd(LayoutRPC.OPPORTUNITY_FORM, RelationItem.newEventRelation(relationType, relationID, relationName)));
                    message.setTextBeforeLink(property.getSingular(crmStrings.messAddingOpportunityClicking(), wfmStrings.opportunity()));
                }
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isShowCustomiseButton() {
                return Utils.hasPermission(PermissionConstants.CRM_OPPORTUNITIES_LIST_CUSTOMIZE_BUTTON);
            }
        };
    }

    public ActionButton addButton() {

        ActionButton newItem = getAddNewButton(ActionButton.Type.TOOLMENU);
        MenuBar menu = new MenuBar(true);

        MenuPopItem addNew = new MenuPopItem(wfmStrings.opportunity());
        addNew.setCommand(() -> {
            if (RelationItem.TYPE_CRM_ACCOUNT.equals(relationType)) {
                SinksContainerFactory.entryPoint.onHistoryChanged("opportunity|add/add//" + relationID + "/" + Utils.encrypt(relationName));
            } else {
                if (TYPE_CONTACT.equals(relationType)) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("opportunity|add/add/" + null + "/" + null + "/" + RelationItem.TYPE_CONTACT + "/" + relationID + "/" + relationName, relationName, relationName);
                } else {
                    SinksContainerFactory.entryPoint.onHistoryChanged("opportunity|add/add" + "/" + RelationItem.TYPE_CAMPAIGN.equals(relationType) + "/" + relationID + "/" + relationName);
                }
            }
        });
        menu.addItem(addNew);

        if (!isFromContact) {
            MenuPopItem quick = new MenuPopItem(wfmStrings.quickAdd());
            quick.ensureDebugId("new_opportunity");
            quick.setCommand(() -> new CrmQuickAdd(LayoutRPC.OPPORTUNITY_FORM, RelationItem.newEventRelation(relationType, relationID, relationName)));
            menu.addItem(quick);
        }
        newItem.setMenu(menu);
        return newItem;
    }

    public ActionButton fullAddButton() {
        ActionButton fullAddButton = getAddNewButton(ActionButton.Type.TOOLMENU);
        fullAddButton.addClickHandler(clickEvent -> {
            if (RelationItem.TYPE_CRM_ACCOUNT.equals(relationType)) {
                SinksContainerFactory.entryPoint.onHistoryChanged("opportunity|add/add//" + relationID + "/" + Utils.encrypt(relationName));
            } else {
                if (TYPE_CONTACT.equals(relationType)) {
                    SinksContainerFactory.entryPoint.onHistoryChanged("opportunity|add/add/" + null + "/" + null + "/" + RelationItem.TYPE_CONTACT + "/" + relationID + "/" + relationName, relationName, relationName);
                } else {
                    SinksContainerFactory.entryPoint.onHistoryChanged("opportunity|add/add" + "/" + RelationItem.TYPE_CAMPAIGN.equals(relationType) + "/" + relationID + "/" + relationName);
                }
            }
        });
        return fullAddButton;
    }

    public ActionButton quickAddButton() {
        ActionButton quickAddButton = getAddNewButton(ActionButton.Type.TOOLMENU);
        quickAddButton.addClickHandler(clickEvent -> {
            new CrmQuickAdd(LayoutRPC.OPPORTUNITY_FORM, RelationItem.newEventRelation(relationType, relationID, relationName));
        });
        return quickAddButton;
    }

    public void refresh() {
        listPanel.reloadPage();
    }

    private FacetContentConfigure getFacetContentConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(4, wfmStrings.filter());
        contentConfigure.addContentConfigure(FacetContentType.OpportunityFacetFilter.getContentCode()[0], wfmStrings.stage(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrOpportunityRepresenter.FIELD_OPPORTUNITY_STAGE_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrOpportunityRepresenter.FIELD_OPPORTUNITY_STAGE_ID_CODE;
            }

            @Override
            public LocalizationType getLocalizationType() {
                return LocalizationType.REFERENCE;
            }
        });
        contentConfigure.addContentConfigure(FacetContentType.OpportunityFacetFilter.getContentCode()[1], wfmStrings.assignee(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrOpportunityRepresenter.FIELD_ASSIGNEE_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrOpportunityRepresenter.FIELD_ASSIGNEE_ID_NAME;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.OpportunityFacetFilter.getContentCode()[3], wfmStrings.country(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrOpportunityRepresenter.FIELD_CRM_ACCOUNT_COUNTRY_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrOpportunityRepresenter.FIELD_CRM_ACCOUNT_COUNTRY_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.OpportunityFacetFilter.getContentCode()[2], wfmStrings.crmAccount(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrOpportunityRepresenter.FIELD_CRM_ACCOUNT_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrOpportunityRepresenter.FIELD_CRM_ACCOUNT_ID_NAME;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.OpportunityFacetFilter.getContentCode()[5], wfmStrings.campaign(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrOpportunityRepresenter.FIELD_CAMPAIGN_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrOpportunityRepresenter.FIELD_CAMPAIGN_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });

        //creator
        contentConfigure.addContentConfigure(FacetContentType.OpportunityFacetFilter.getContentCode()[6], wfmStrings.createdBy(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrOpportunityRepresenter.FIELD_CREATOR_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrOpportunityRepresenter.FIELD_CREATOR_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        //backup assigne 
        contentConfigure.addContentConfigure(FacetContentType.OpportunityFacetFilter.getContentCode()[7], wfmStrings.backupAssignee(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrOpportunityRepresenter.FIELD_BACKUP_ASSIGNEE_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrOpportunityRepresenter.FIELD_BACKUP_ASSIGNEE_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        //Lead Source
        contentConfigure.addContentConfigure(FacetContentType.OpportunityFacetFilter.getContentCode()[8], wfmStrings.source(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrOpportunityRepresenter.FIELD_LEAD_SOURCE_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrOpportunityRepresenter.FIELD_LEAD_SOURCE_ID_NAME;
            }
        });
        //Project
        contentConfigure.addContentConfigure(FacetContentType.OpportunityFacetFilter.getContentCode()[9], Property.get(Constants.PROJECT, wfmStrings.project()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return Utils.isProjectInLineItemEnable() ? SolrOpportunityRepresenter.FIELD_MULTI_PROJECT_ID : SolrOpportunityRepresenter.FIELD_RELATED_PROJECT_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return Utils.isProjectInLineItemEnable() ? SolrOpportunityRepresenter.FIELD_MULTI_PROJECT_ID_NAME : SolrOpportunityRepresenter.FIELD_RELATED_PROJECT_ID_NAME;
            }
        });
        //Attachment
        contentConfigure.addContentConfigure(FacetContentType.OpportunityFacetFilter.getContentCode()[10], wfmStrings.attachment(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrOpportunityRepresenter.HAS_ATTACHMENT;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrOpportunityRepresenter.HAS_ATTACHMENT;
            }

            @Override
            public boolean isWithID() {
                return false;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });
        contentConfigure.addContentConfigureDateListBox(SolrOpportunityRepresenter.FIELD_CLOSING_DATE, wfmStrings.closeDate());
        contentConfigure.addContentConfigureDateListBox(SolrOpportunityRepresenter.FIELD_MODIFICATION_DATE, wfmStrings.modifiedDate());
        contentConfigure.addContentConfigureDateListBox(SolrOpportunityRepresenter.FIELD_CREATION_DATE, wfmStrings.createdDate());
        return contentConfigure;
    }

    private ListingRequestProvider<OpportunityListItem> getListProvider() {
        return (filterParametrs, listingCallback) -> {
            if (defaultOne == null) {
                crmService.getDefaultOne(new AsyncCallback<OpportunityListItem>() {
                    @Override
                    public void onFailure(Throwable throwable) {

                    }

                    @Override
                    public void onSuccess(OpportunityListItem opportunityListItem) {
                        defaultOne = opportunityListItem;
                    }
                });
            }
            initOpportunityList(filterParametrs, listingCallback, null);
        };
    }

    private void initOpportunityList(ListingFilterParameter filterParametrs, ListingCallback<OpportunityListItem> listingCallback, Span container) {
        filterParametrs = filterParametrs == null ? new ListingFilterParameter() : filterParametrs;
        if (TYPE_CONTACT.equals(relationType)) {
            filterParametrs.setContactID(relationID);
        } else if (RelationItem.TYPE_CRM_ACCOUNT.equals(relationType)) {
            filterParametrs.setCrmAccountId(relationID);
        } else if (RelationItem.TYPE_CAMPAIGN.equals(relationType)) {
            filterParametrs.setCampaignID(relationID);
        }
        filterParametrs.setHasOnlyClientAccess(isClientView);
        if (RelationItem.TYPE_PRODUCT_CATEGORY.equals(relationType)) {
            crmService.getOpportunityListByCategoryID(relationID, new AbstractAsyncCallback<ListResult<OpportunityListItem>>() {
                @Override
                public void failure(Throwable throwable) {
                    if (listingCallback != null) {
                        listingCallback.onFailure(throwable);
                    }
                }

                @Override
                public void success(ListResult<OpportunityListItem> result) {

                    if (listingCallback != null) {
                        listingCallback.onSuccess(result);
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
        }else if (RelationItem.TYPE_PRODUCT.equals(relationType)) {
            crmService.getOpportunityListByProductID(relationID, new AbstractAsyncCallback<ListResult<OpportunityListItem>>() {
                @Override
                public void failure(Throwable throwable) {
                    if (listingCallback != null) {
                        listingCallback.onFailure(throwable);
                    }
                }

                @Override
                public void success(ListResult<OpportunityListItem> result) {

                    if (listingCallback != null) {
                        listingCallback.onSuccess(result);
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
        }else {
            crmService.getOpportunityList(filterParametrs, new AbstractAsyncCallback<ListResult<OpportunityListItem>>() {
                @Override
                public void failure(Throwable throwable) {
                    if (listingCallback != null) {
                        listingCallback.onFailure(throwable);
                    }
                }

                @Override
                public void success(ListResult<OpportunityListItem> result) {
                    if (listingCallback != null) {
                        listingCallback.onSuccess(result);
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
    }

    @Override
    public String getIconStyle() {
        return "oport opportunities-list";
    }

    private void showDeleteMessage() {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
        messageBox.setTitle(wfmStrings.warning());
        OpportunityListItem item = selectedItems.iterator().next();

        String message = wfmStrings.areYouSureYouWantToDeleteTheSelectedRecords();
        messageBox.setMessage(message);
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                ArrayList<Integer> ids = new ArrayList<>();
                for (OpportunityListItem item : selectedItems) {
                    ids.add(item.getObjectId());
                }
                LoadingPanel.loading(true);
                crmService.deleteOpportunity(ids, new AbstractAsyncCallback<ArrayList<Integer>>() {
                    @Override
                    public void failure(Throwable caught) {
                        LoadingPanel.loading(false);
                        Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                    }

                    @Override
                    public void success(ArrayList<Integer> result) {
                        LoadingPanel.loading(false);
                        Info.show(property.getSingular(wfmStrings.messSuccessfulyyDeleted(), wfmStrings.opportunity()), Info.Type.INFO);
                        refresh();
                    }
                });
            }
        });
        messageBox.open();
    }

    public static ArrayList<Integer> getIDsOnly(Set<OpportunityListItem> selectedItems) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (OpportunityListItem item : selectedItems) {
            ids.add(item.getObjectId());
        }
        return ids;
    }

    private MenuPopItem getConvertAction(final OpportunityListItem item, MenuBar menuBar) {
        MenuPopItem convert = new MenuPopItem(wfmStrings.send(), "icon-convert-small");
        MenuBar subMenu = new MenuBar(true);
        subMenu.setAutoOpen(true);

        if (toSQ) {
            MenuPopItem toSalesQuote = new MenuPopItem(Property.getShortName(Constants.SALE_QUOTE, wfmStrings.salesQuote()), "");
            toSalesQuote.setCommand(() -> {
                if ("true".equals(Utils.userSettings.get(Constants.ACCOUNTING_IS_SETUP))) {
                    if (item.getAccountId() != null) {
                        SinksContainerFactory.entryPoint.onHistoryChanged(Constants.SALE_QUOTE + "|add/add/opportunity/" + item.getObjectId());
                    } else {
                        LoadingPanel.loading(true);
                        crmService.addAccountOrContactToOpportunity(item.getObjectId(), true, new AbstractAsyncCallback<OpportunityListItem>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                LoadingPanel.loading(false);
                            }

                            @Override
                            public void onSuccess(OpportunityListItem result) {
                                LoadingPanel.loading(false);
                                refresh();
                                SinksContainerFactory.entryPoint.onHistoryChanged(Constants.SALE_QUOTE + "|add/add/opportunity/" + item.getObjectId());
                            }
                        });
                    }
                } else {
                    Info.show(wfmStrings.accountingAndFinanceModuleNoSetupYet() + " " + Utils.getSupportEmail() + " " + wfmStrings.accountingAndFinanceModuleNoSetupYet1(), Info.Type.WARNING);
                }
                toSalesQuote.closeAll(menuBar);
            });
            subMenu.addItem(toSalesQuote);
        }
        if (toSO) {
            MenuPopItem toSalesOrder = new MenuPopItem(Property.getShortName(Constants.SALE_ORDER_CODE, wfmStrings.saleorder()), "icon-sales-quote");
            toSalesOrder.setCommand(() -> {
                if ("true".equals(Utils.userSettings.get(Constants.ACCOUNTING_IS_SETUP))) {
                    if (item.getAccountId() != null) {
                        SinksContainerFactory.entryPoint.onHistoryChanged(Constants.SALE_ORDER_CODE + "|add/add/opportunity/" + item.getObjectId());
                    } else {
                        LoadingPanel.loading(true);
                        crmService.addAccountOrContactToOpportunity(item.getObjectId(), true, new AbstractAsyncCallback<OpportunityListItem>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                LoadingPanel.loading(false);
                            }

                            @Override
                            public void onSuccess(OpportunityListItem result) {
                                LoadingPanel.loading(false);
                                refresh();
                                SinksContainerFactory.entryPoint.onHistoryChanged(Constants.SALE_ORDER_CODE + "|add/add/opportunity/" + item.getObjectId());
                            }
                        });
                    }
                } else {
                    Info.show(wfmStrings.accountingAndFinanceModuleNoSetupYet() + " " + Utils.getSupportEmail() + " " + wfmStrings.accountingAndFinanceModuleNoSetupYet1(), Info.Type.WARNING);
                }
                toSalesOrder.closeAll(menuBar);
            });
            subMenu.addItem(toSalesOrder);
        }

        if (toRFQ) {
            MenuPopItem toRfq = new MenuPopItem(Property.getShortName(REQUEST_FOR_QUOTE, wfmStrings.requestForQuote()), "icon-purchase-order");
            toRfq.setCommand(() -> {
                if ("true".equals(Utils.userSettings.get(Constants.ACCOUNTING_IS_SETUP))) {
                    if (item.getRFQId() != null) {
                        /*Window.open(GWT.getHostPageBaseURL() + "Accounting.html#requestforquote|summary/" + item.getRFQId(), "_blank", "");*/
                        SinksContainerFactory.entryPoint.onHistoryChanged("requestforquote|summary/" + item.getRFQId());
                    } else if (item.getAccountId() != null) {
                        String addRFQ = "requestforquote|add/add/opportunity/" + item.getObjectId();
                        SinksContainerFactory.entryPoint.onHistoryChanged(addRFQ);
                    } else {
                        LoadingPanel.loading(true);
                        crmService.addAccountOrContactToOpportunity(item.getObjectId(), true, new AbstractAsyncCallback<OpportunityListItem>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                LoadingPanel.loading(false);
                            }

                            @Override
                            public void onSuccess(OpportunityListItem result) {
                                LoadingPanel.loading(false);
                                refresh();
                                String addRFQ = "requestforquote|add/add/opportunity/" + item.getObjectId();
                                SinksContainerFactory.entryPoint.onHistoryChanged(addRFQ);
                            }
                        });
                    }
                } else {
                    Info.show(wfmStrings.accountingAndFinanceModuleNoSetupYet() + " " + Utils.getSupportEmail() + " " + wfmStrings.accountingAndFinanceModuleNoSetupYet1(), Info.Type.WARNING);
                }
                toRfq.closeAll(menuBar);
            });
            subMenu.addItem(toRfq);
        }
        if (toPO) {
            MenuPopItem toPurchaseOrder = new MenuPopItem(Property.getShortName(PURCHASE_ORDER, wfmStrings.purchaseorder()), "icon-purchase-order");
            toPurchaseOrder.setCommand(() -> {
                if ("true".equals(Utils.userSettings.get(Constants.ACCOUNTING_IS_SETUP))) {
                    if (item.getAccountId() != null) {
                        SinksContainerFactory.entryPoint.onHistoryChanged(Constants.PURCHASE_ORDER + "|add/add/opportunity/" + item.getObjectId());
                    } else {
                        LoadingPanel.loading(true);
                        crmService.addAccountOrContactToOpportunity(item.getObjectId(), false, new AbstractAsyncCallback<OpportunityListItem>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                LoadingPanel.loading(false);
                            }

                            @Override
                            public void onSuccess(OpportunityListItem result) {
                                LoadingPanel.loading(false);
                                refresh();
                                SinksContainerFactory.entryPoint.onHistoryChanged(Constants.PURCHASE_ORDER + "|add/add/opportunity/" + item.getObjectId());
                            }
                        });
                    }
                } else {
                    Info.show(wfmStrings.accountingAndFinanceModuleNoSetupYet() + " " + Utils.getSupportEmail() + " " + wfmStrings.accountingAndFinanceModuleNoSetupYet1(), Info.Type.WARNING);
                }
                toPurchaseOrder.closeAll(menuBar);
            });
            subMenu.addItem(toPurchaseOrder);
        }
        if (toSI) {
            MenuPopItem toSaleInvoice = new MenuPopItem(Property.getShortName(SALE_INVOICE, wfmStrings.salesInvoice()), "icon-sales-invoice");
            toSaleInvoice.setCommand(() -> {
                if ("true".equals(Utils.userSettings.get(Constants.ACCOUNTING_IS_SETUP))) {
                    if (item.getAccountId() != null) {
                        SinksContainerFactory.entryPoint.onHistoryChanged(Constants.SALE_INVOICE + "|add/add/opportunity/" + item.getObjectId());
                    } else {
                        LoadingPanel.loading(true);
                        crmService.addAccountOrContactToOpportunity(item.getObjectId(), false, new AbstractAsyncCallback<OpportunityListItem>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                LoadingPanel.loading(false);
                            }

                            @Override
                            public void onSuccess(OpportunityListItem result) {
                                LoadingPanel.loading(false);
                                refresh();
                                SinksContainerFactory.entryPoint.onHistoryChanged(Constants.SALE_INVOICE + "|add/add/opportunity/" + item.getObjectId());
                            }
                        });
                    }
                } else {
                    Info.show(wfmStrings.accountingAndFinanceModuleNoSetupYet() + " " + Utils.getSupportEmail() + " " + wfmStrings.accountingAndFinanceModuleNoSetupYet1(), Info.Type.WARNING);
                }
                toSaleInvoice.closeAll(menuBar);
            });
            subMenu.addItem(toSaleInvoice);
        }
        if (toPI) {
            MenuPopItem toPurchaseInvoice = new MenuPopItem(Property.getShortName(PURCHASE_INVOICE, wfmStrings.purchaseinvoice()), "icon-purchase-invoice");
            toPurchaseInvoice.setCommand(() -> {
                if ("true".equals(Utils.userSettings.get(Constants.ACCOUNTING_IS_SETUP))) {
                    if (item.getAccountId() != null) {
                        SinksContainerFactory.entryPoint.onHistoryChanged(Constants.PURCHASE_INVOICE + "|add/add/opportunity/" + item.getObjectId());
                    } else {
                        LoadingPanel.loading(true);
                        crmService.addAccountOrContactToOpportunity(item.getObjectId(), false, new AbstractAsyncCallback<OpportunityListItem>() {
                            @Override
                            public void onFailure(Throwable caught) {
                                LoadingPanel.loading(false);
                            }

                            @Override
                            public void onSuccess(OpportunityListItem result) {
                                LoadingPanel.loading(false);
                                refresh();
                                SinksContainerFactory.entryPoint.onHistoryChanged(Constants.PURCHASE_INVOICE + "|add/add/opportunity/" + item.getObjectId());
                            }
                        });
                    }
                } else {
                    Info.show(wfmStrings.accountingAndFinanceModuleNoSetupYet() + " " + Utils.getSupportEmail() + " " + wfmStrings.accountingAndFinanceModuleNoSetupYet1(), Info.Type.WARNING);
                }
                toPurchaseInvoice.closeAll(menuBar);
            });
            subMenu.addItem(toPurchaseInvoice);
        }
        if (toPROJECT && !item.isConvertedToProject()) {
            MenuPopItem toProject = new MenuPopItem(Property.getShortName(PROJECT, wfmStrings.project()), "convert-toproject");
            toProject.setCommand(() -> {
                if ("true".equals(Utils.userSettings.get(PM_IS_SETUP)) || Utils.hasPermission(PM_PROJECT_ADD)) {
                    if (item.getAccountId() != null) {
                        ViewOpportunityForm.goToConvert(item, OpportunitiesListView.this);
                    } else {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.information());
                        messageBox.setMessage(wfmMessages.convertToProjectRequiresAccount(item.getOpportunityName()));
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                crmService.addAccountOrContactToOpportunity(item.getObjectId(), true, new AbstractAsyncCallback<OpportunityListItem>() {
                                    @Override
                                    public void onFailure(Throwable caught) {
                                    }

                                    @Override
                                    public void onSuccess(OpportunityListItem result) {
                                        item.setCrmAccountItem(result.getCrmAccountItem());
                                        item.setContactId(result.getContactId());
                                        ViewOpportunityForm.goToConvert(item, OpportunitiesListView.this);
                                    }
                                });
                            }

                            @Override
                            public void onCancel() {
                                if (Utils.hasPermission(CRM_EDIT_OPPORTUNITIES)) {
                                    SinksContainerFactory.entryPoint.onHistoryChanged("opportunity|add/add/" + item.getObjectId() + "/REQUIRED");
                                }
                            }
                        });
                        messageBox.open();
                    }
                } else {
                    Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                }
                toProject.closeAll(menuBar);
            });
            subMenu.addItem(toProject);
        }

        if (toSMS) {
            MenuPopItem addSms = new MenuPopItem(wfmStrings.sms(), "icon-sms");
            addSms.ensureDebugId(wfmStrings.sendSms());
            addSms.setCommand(() -> {
                addSms.closeAll(menuBar);
                new ActivityQuickAddForm(Appointment.SMS, item.getContactPrimaryPhone(), new ProfileItem(), RelationItem.newEventRelation(TYPE_OPPORTUNITY, item.getObjectId(), item.getOpportunityName()), RelationItem.newEventRelation(TYPE_CONTACT, item.getContactId(), item.getContact()), RelationItem.newEventRelation(TYPE_CRM_ACCOUNT, item.getAccountId(), item.getAccount()));
            });
            subMenu.addItem(addSms);
        }

        convert.setSubMenu(subMenu);
        return convert;
    }

    private MenuBar getActionsForSelections() {
        if (selectedItems != null && selectedItems.size() > 0) {
            if (actions == null) {
                actions = new ContextMenu();
                actions.getMenuBar().setAutoOpen(false);
                //Change Campaign
                if (Utils.hasPermission(CHANGE_OPPORTUNITIES_CAMPAIGN) || Utils.hasPermission(CRM_EDIT_OPPORTUNITIES)) {
                    actions.addMenuItem(crmStrings.changeCampaign(), null, true, () -> {
                        actions.hide();
                        Set<OpportunityListItem> items = selectedItems;
                        for (OpportunityListItem item : items) {
                            campaignPopup.getItemIDs().add(item.getObjectId());
                        }
                        campaignPopup.open();
                    });
                }
                //Change Assignee
                if (Utils.hasPermission(CHANGE_OPPORTUNITY_ASSIGNEE)) {
                    actions.addMenuItem(crmStrings.changeAssignee(), null, true, () -> {
                        actions.hide();
                        Set<OpportunityListItem> items = selectedItems;
                        assigneePopup.getItemIDs().clear();
                        for (OpportunityListItem item : items) {
                            assigneePopup.addItemID(item.getObjectId());
                        }
                        assigneePopup.open();
                    });
                }
                //Delete
                if (Utils.hasPermission(CRM_REMOVE_OPPORTUNITIES)) {
                    actions.addMenuItem(wfmStrings.delete(), null, true, () -> {
                        actions.hide();
                        showDeleteMessage();
                    });
                }
            }
            return actions.getMenuBar();
        } else {
            if (actionsEmpty == null) {
                actionsEmpty = new ContextMenu();
                actionsEmpty.getMenuBar().setAutoOpen(false);
                actionsEmpty.addMenuItem(wfmStrings.selectAnyItemToActivateBatchActions(), null, true, null);
            }
            return actionsEmpty.getMenuBar();
        }
    }

    public Command getStatisticCommand() {
        ShortcutItem shortcutItem = getContainer().getItemsByView().get(Constants.TASK_LIST);
        if (shortcutItem != null && relationID != null) {
            return shortcutItem.getStatisticCommand();
        }
        return null;
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
    public void initStatistics(Integer parentId, Span container) {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setLimit(1);
        if (parentId != null) {
            initOpportunityList(fp, null, container);
            onInitialize();
            clear();
        }
    }

    private KanbanDataLoader<OpportunityListItem> getKanbanDataLoader() {
        return new KanbanDataLoader<OpportunityListItem>() {
            @Override
            public void loadData(ListingFilterParameter filterParameter, KanbanDataRenderer dataRenderer) {
                LoadingPanel.loading(true);

                //When you open contact related opportunities from contactsummary page
                if (TYPE_CONTACT.equals(relationType)) {
                    filterParameter.setContactID(relationID);
                } else if (RelationItem.TYPE_CRM_ACCOUNT.equals(relationType)) {
                    filterParameter.setCrmAccountId(relationID);
                } else if (RelationItem.TYPE_CAMPAIGN.equals(relationType)) {
                    filterParameter.setCampaignID(relationID);
                }
                filterParameter.setHasOnlyClientAccess(isClientView);

                crmService.getNewKanbanOpportunities(filterParameter, dataRenderer.getColumnMetadata(), new AbstractAsyncCallback<OpportunitiesList<OpportunityListItem>>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        LoadingPanel.loading(false);
                    }

                    @Override
                    public void onSuccess(OpportunitiesList<OpportunityListItem> result) {
                        dataRenderer.setResults(result);
                        dataRenderer.setTotalAmount(result.getTotalAmount() != null ? result.getTotalAmount() : 0d);
                        LoadingPanel.loading(false);
                    }
                });
            }

            @Override
            public void onDropKanbanItem(Object sourceColumnLayoutData, Object targetColumnLayoutData, Object opportunityListItem,
                                         Integer widgetIndex, Object prevItem, Object afterItem, KanbanBoard kanbanBoard,
                                         KanbanBoard.OnDropCard onDropCard) {

                Double[] d = (Double[]) opportunityListItem;
                Integer itemId = d[0].intValue();

                Double[] d2 = (Double[]) prevItem;
                Integer prevItemId = d2 != null && d2.length > 0 ? d2[0].intValue() : null;

                Double[] d3 = (Double[]) afterItem;
                Integer afterItemId = d3 != null && d3.length > 0 ? d3[0].intValue() : null;

                if (((SelectItem) targetColumnLayoutData).isDraggable() && ((SelectItem) sourceColumnLayoutData).isDraggable()) {
                    if (Utils.isDoubleMessageEnable() && targetColumnLayoutData != sourceColumnLayoutData) {
                        WfmMessageBox changeStatusMessageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        changeStatusMessageBox.setMessage(crmMessages.doYouWantToChangeStatusTo(((SelectItem) targetColumnLayoutData).getName()));
                        changeStatusMessageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                if (targetColumnLayoutData != null && ("0".equals(((SelectItem) targetColumnLayoutData).getDescription()) || ((SelectItem) targetColumnLayoutData).isSelected())) {
                                    new OpportunityPercentageStageModal((SelectItem) targetColumnLayoutData, itemId, widgetIndex, prevItemId, afterItemId, onDropCard);
                                } else {
                                    changeOpportunityStage((SelectItem) targetColumnLayoutData, itemId, widgetIndex, prevItemId, afterItemId, onDropCard);
                                }
                            }

                            @Override
                            public void onCancel() {
                                kanbanBoard.reloadColumn(((SelectItem) targetColumnLayoutData).getId());
                                kanbanBoard.reloadColumn(((SelectItem) sourceColumnLayoutData).getId());
                            }
                        });

                        changeStatusMessageBox.setTitle(wfmStrings.warning());
                        changeStatusMessageBox.open();
                    } else {
                        if (targetColumnLayoutData != null && ("0".equals(((SelectItem) targetColumnLayoutData).getDescription()) || ((SelectItem) targetColumnLayoutData).isSelected())) {
                            new OpportunityPercentageStageModal((SelectItem) targetColumnLayoutData, itemId, widgetIndex, prevItemId, afterItemId, onDropCard);
                        } else {
                            changeOpportunityStage((SelectItem) targetColumnLayoutData, itemId, widgetIndex, prevItemId, afterItemId, onDropCard);
                        }
                    }
                } else {
                    Info.show(wfmStrings.youDontHavePermission(), Info.Type.WARNING);
                    listPanel.requestKanbanData();
                }

            }

            @Override
            public Double getAmount(Number[] amount) {
                if (amount != null && amount.length > 1) {
                    return (Double) amount[1];
                }
                return 0d;
            }
        };
    }

    private void changeOpportunityStage(SelectItem targetColumnLayoutData, Integer itemId, Integer widgetIndex, Integer prevItemId, Integer afterItemId, KanbanBoard.OnDropCard onDropCard) {
        crmService.changeOpportunityKanbanOrder(targetColumnLayoutData, itemId, widgetIndex,
                prevItemId, afterItemId, new AsyncCallback<Integer>() {
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

    private KanbanBoardDesign<OpportunityListItem> getKanbanBoardDesign() {
        return new KanbanBoardDesign<OpportunityListItem>() {
            @Override
            public Widget getBoardItem(OpportunityListItem kanbanItem, KanbanBoard kanbanBoard, Object... obj) {
                //Return card item
                MaterialPanel p = new MaterialPanel();
                if (obj != null && obj.length > 0 && (obj[0] instanceof HashMap)) {
                    HashMap<String, KanbanItemColumnConfigs> strMap = (HashMap) obj[0];
                    p.add(new OpportunityMaterialCard(kanbanItem, strMap));
                } else {
                    p.add(new OpportunityMaterialCard(kanbanItem));
                }
                Double[] d = new Double[2];
                d[0] = Double.valueOf(kanbanItem.getObjectId());
                d[1] = kanbanItem.getAmountInBaseCurrency();
                p.setLayoutData(d);
                return p;
            }

            @Override
            public void loadDefaultColumns(AbstractAsyncCallback callback) {
                LoadingPanel.loading(true);
                kanbanService.getKanbanDefaultColumns(ReferenceParentEnum._OPPORTUNITY_STAGE, new AsyncCallback<ArrayList<SelectItem>>() {
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
            public boolean canDnD(OpportunityListItem kanbanItem) {
                return Utils.hasPermission(CRM_OPPORTUNITY_CHANGE_STAGE, CRM_EDIT_OPPORTUNITIES);
            }
        };
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
        return Constants.Opportunities;
    }

    private int getConvertItems(OpportunityListItem rowValue, MenuBar menuBar, MenuBar convertMenu, int convertItems, ConvertItem convertItem) {
        if (convertItem.getCode().contains("_FORM") && Utils.hasPermission(convertItem.getCode() + "_ADD_" + Utils.getCompanyID())) {
            final MenuPopItem convertToCF = new MenuPopItem(convertItem.getName(), "icon-send-sales-invoice");
            convertToCF.setCommand(() -> {
                convertToCF.closeAll(menuBar);
                SinksContainerFactory.entryPoint.onHistoryChanged(Constants.ITEM_LIST + "|add/add/" + convertItem.getEntityId() + "/" + convertItem.getCode() + "/CONVERT/" + RelationItem.TYPE_OPPORTUNITY + "/" + rowValue.getObjectId());
            });
            convertToCF.ensureDebugId("convert_to_" + convertItem.getName());
            convertMenu.addItem(convertToCF);
            convertItems++;
        }

        return convertItems;
    }
}
