package com.edatasite.workforce.gwt.crm.client.ui.view;

import com.edatasite.workforce.gwt.core.client.*;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.AllInOneService;
import com.edatasite.workforce.gwt.core.client.rpc.RbacService;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFieldConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterCutomField;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrEventRepresenter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.crm.CrmConstants;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.CloseHandler;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.DatePeriodFacetContent;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetContentType;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.landing.HelpPanelGenerator;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.cellwidgets.DateTimePickerCellEditor;
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
import com.edatasite.workforce.gwt.crm.client.rpc.EventItem;
import com.edatasite.workforce.gwt.googlecalendar.client.ui.newVersion.appointment.ActivityQuickAddForm;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.gen2.table.client.InlineCellEditor;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.MaterialDropDown;
import gwt.material.design.client.ui.MaterialLink;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.util.*;

import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.CRM_ADD_NEW_ACTIVITY_LOG_A_CALL;
import static com.edatasite.workforce.gwt.core.client.ui.PermissionConstants.HRMS_ADD_NEW_ACTIVITY_LOG_A_CALL;

/**
 * Created by IntelliJ IDEA.
 * User: Rinat
 * Date: 19.08.11
 * Time: 15:59
 * To change this template use File | Settings | File Templates.
 */

public class EventListView extends BaseListView implements Constants {
    private static final CrmStrings crmStrings = CrmStrings.App.get();
    private static final CrmMessages crmMessages = CrmMessages.App.get();
    private final CRMServiceAsync crmService = CRMService.App.get();
    protected HashSet selectedItems = new HashSet();
    private final String activityListViewID = "activities_list_view_";
    private ListingPanel<EventItem> listPanel;
    private int totalCount;
    private Integer relationID;
    private String relationType;
    private Integer accountRelationID;
    private String accountRelationType;
    private String accountRelationName;
    private String relationName;
    private final Integer eventType;
    private KpiCheckBox todayBox;
    private ActionButton actionButton;
    private final boolean summaryPermission = Utils.hasPermission(Utils.isHRMS() ? PermissionConstants.HRMS_SUMMARY_ACTIVITY : PermissionConstants.CRM_ACTIVITY_SUMMARY);


    public EventListView(Integer eventType) {
        super(EVENT_LIST);
        setDescription(eventType != null && eventType == Appointment.INTERVIEW ? wfmStrings.interview() : property.getPlural(wfmStrings.activities()));
        this.eventType = eventType;
        if (Utils.hasPermission(Utils.isHRMS() ? PermissionConstants.HRMS_ADD_NEW_ACTIVITY_EVENT : PermissionConstants.CRM_ADD_NEW_ACTIVITY_EVENT)) {
            setAddNew(() -> new ActivityQuickAddForm(Utils.isHRMS() ? Appointment.INTERVIEW : Appointment.EVENT, RelationItem.newEventRelation(relationType, relationID, relationName)));
        } else if (Utils.hasPermission(Utils.isHRMS() ? PermissionConstants.HRMS_FULL_ADD_NEW_ACTIVITY_EVENT : PermissionConstants.CRM_FULL_ADD_NEW_ACTIVITY_EVENT)) {
            setAddNew(() -> SinksContainerFactory.entryPoint.onHistoryChanged("event|add/add/" + (Utils.isHRMS() ? Appointment.INTERVIEW : Appointment.EVENT)));
        }
    }

    public EventListView(Integer eventType, Integer relationID, String relationType) {
        this(eventType);
        this.relationID = relationID;
        this.relationType = relationType;
        if (Utils.hasPermission(Utils.isHRMS() ? PermissionConstants.HRMS_ADD_NEW_ACTIVITY_EVENT : PermissionConstants.CRM_ADD_NEW_ACTIVITY_EVENT)) {
            setAddNew(() -> new ActivityQuickAddForm(Utils.isHRMS() ? Appointment.INTERVIEW : Appointment.EVENT, RelationItem.newEventRelation(relationType, relationID, relationName)));
        } else if (Utils.hasPermission(Utils.isHRMS() ? PermissionConstants.HRMS_FULL_ADD_NEW_ACTIVITY_EVENT : PermissionConstants.CRM_FULL_ADD_NEW_ACTIVITY_EVENT)) {
            setAddNew(() -> SinksContainerFactory.entryPoint.onHistoryChanged("event|add/add/" + (Utils.isHRMS() ? Appointment.INTERVIEW : Appointment.EVENT) + "/" + relationID + "/" + relationType));
        }
    }

    public EventListView(Integer eventType, Integer relationID, String relationType, Integer accountRelationID, String accountRelationType) {
        this(eventType);
        this.relationID = relationID;
        this.relationType = relationType;
        this.accountRelationID = accountRelationID;
        this.accountRelationType = accountRelationType;
        if (Utils.hasPermission(Utils.isHRMS() ? PermissionConstants.HRMS_ADD_NEW_ACTIVITY_EVENT : PermissionConstants.CRM_ADD_NEW_ACTIVITY_EVENT)) {
            setAddNew(() -> new ActivityQuickAddForm(Utils.isHRMS() ? Appointment.INTERVIEW : Appointment.EVENT, RelationItem.newEventRelation(relationType, relationID, relationName), RelationItem.newEventRelation(accountRelationType, accountRelationID, accountRelationName)));
        } else if (Utils.hasPermission(Utils.isHRMS() ? PermissionConstants.HRMS_FULL_ADD_NEW_ACTIVITY_EVENT : PermissionConstants.CRM_FULL_ADD_NEW_ACTIVITY_EVENT)) {
            setAddNew(() -> SinksContainerFactory.entryPoint.onHistoryChanged("event|add/add/" + (Utils.isHRMS() ? Appointment.INTERVIEW : Appointment.EVENT) + "/" + relationID + "/" + relationType));
        }
    }

    private static ArrayList<Integer> getIDsOnly(Set<EventItem> selectedItems) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (EventItem item : selectedItems) {
            ids.add(item.getObjectID());
        }
        return ids;
    }

    public FlowPanel getHelpContainer() {
        return HelpPanelGenerator.getHelpPanel(PermissionConstants.CRM_CONTEXT, PermissionConstants.CRM_ACTIVITIES_LIST);
    }

    @Override
    protected Widget onInitialize() {
        listPanel = new GuideListingPanel(ListPanelType.EventsListPanel, getColumnConfig(), getListProvider(), getListDesign(), SelectionGrid.SelectionPolicy.CHECKBOX);

        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CRM_EVENT_ADD_EDIT, EventListView.this, (sender, args) -> listPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_CRM_ACTIVITY_DELETED, EventListView.this, (sender, args) -> listPanel.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_ADD_RELATION, EventListView.this, (sender, args) -> listPanel.reloadPage());

        listPanel.addSelectionRowHandler(selectedRows -> {
            selectedItems = selectedRows;
            if (selectedItems.isEmpty()) {
                if (actionButton != null) {
                    actionButton.setVisible(false);
                }
            } else {
                if (actionButton != null) {
                    actionButton.setVisible(true);
                }
            }

        });

        listPanel.setCustomFieldsEditCellSaveChanges((rowValue, columnCodeName) -> saveEventsCellValue((EventItem) rowValue, columnCodeName));

        listPanel.setPDFListener(clickEvent -> {
            if (totalCount > 1000) {
                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
            }
            String pdfURL = CommandConstants.PDF_URL + "/downloadCrmEventPDF";
            ListingFilterParameter listingFilterParameter = listPanel.getFilterParametrs();
            listingFilterParameter.setPropertyCode(getPropertyCode());
            if (listingFilterParameter == null) {
                listingFilterParameter = new ListingFilterParameter();
            }
            listingFilterParameter.setFacetFilterJson(Utils.facetFilterRpcToJsonString(listingFilterParameter.getFacetFilter()));
            listingFilterParameter.setListPanelToolJson(Utils.listPanelToolRpcConvertJsonData(listingFilterParameter.getListPanelTool()));
            listPanel.callListPDF(pdfURL, listPanel.getFilterParametrs());
        });
        listPanel.setExcelListener(clickEvent -> {
            if (totalCount > 1000) {
                Window.alert(wfmStrings.CurrentlyLimitedContactExport());
            }
            String excelURL = CommandConstants.COMMON_URL + "/downloadCrmEventExcel";
            ListingFilterParameter listingFilterParameter = listPanel.getFilterParametrs();
            listingFilterParameter.setFacetFilterJson(Utils.facetFilterRpcToJsonString(listingFilterParameter.getFacetFilter()));
            listingFilterParameter.setListPanelToolJson(Utils.listPanelToolRpcConvertJsonData(listingFilterParameter.getListPanelTool()));
            listingFilterParameter.setPropertyCode(getPropertyCode());
            listPanel.callListExcel(excelURL, listPanel.getFilterParametrs());
        });
        getRelationName();
        getAccountRelationName();
        add(listPanel);
        return null;
    }

    private void getRelationName() {
        AllInOneService.App.get().getRelationName(relationID, relationType, new AbstractAsyncCallback<String>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(String result) {
                relationName = result;

                if (accountRelationID != null) {
                    getAccountRelationName();
                }
            }
        });
    }

    private void getAccountRelationName() {
        AllInOneService.App.get().getRelationName(accountRelationID, accountRelationType, new AbstractAsyncCallback<String>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(String result) {
                accountRelationName = result;
            }
        });
    }

    private CustomColumnDefinitionConfig[] getColumnConfig() {
        ArrayList<CustomColumnDefinitionConfig> columns = new ArrayList<>();

        ColumnDefinitionConfig columnConfig = new ColumnDefinitionConfig<EventItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final EventItem item) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);

                if (summaryPermission) {
                    MenuPopItem eventSummary = new MenuPopItem(wfmStrings.summaryView(), "icon-activity-small", () -> SinksContainerFactory.entryPoint.onHistoryChanged("event|summary/" + item.getObjectID(), item.getSubject()));
                    eventSummary.ensureDebugId(activityListViewID + "event_summary");
                    actionItemCount++;
                    menuBar.addItem(eventSummary);
                }
                if (Utils.hasPermission(Utils.isHRMS() ? PermissionConstants.HRMS_EDIT_ACTIVITY : PermissionConstants.CRM_EDIT_ACTIVITY) && Appointment.SMS != item.getActivityType()) {
                    MenuPopItem eventEdit = new MenuPopItem(wfmStrings.edit(), "icon-edit", () -> SinksContainerFactory.entryPoint.onHistoryChanged("event|add/add/" + item.getObjectID() + "/" + item.getActivityType(), item.getSubject()));
                    eventEdit.ensureDebugId(activityListViewID + "edit_event");
                    actionItemCount++;
                    menuBar.addItem(eventEdit);
                }
                if (Utils.hasPermission(PermissionConstants.CRM_COPY_ACTIVITY) && Appointment.SMS != item.getActivityType()) {
                    MenuPopItem eventcopy = new MenuPopItem(wfmStrings.copy(), "icon-edit", () -> SinksContainerFactory.entryPoint.onHistoryChanged("event|add/add/" + item.getObjectID() + "/" + item.getActivityType() + "/copyFromExistingData", item.getSubject()));
                    eventcopy.ensureDebugId(activityListViewID + "edit_event");
                    actionItemCount++;
                    menuBar.addItem(eventcopy);
                }
                if (Utils.hasPermission(Utils.isHRMS() ? PermissionConstants.HRMS_REMOVE_ACTIVITY : PermissionConstants.CRM_REMOVE_ACTIVITY)) {
                    MenuPopItem removeEvent = new MenuPopItem(wfmStrings.delete(), "removeItemStyle-profile", () -> {
                        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
                        messageBox.setTitle(wfmStrings.warning());
                        messageBox.setMessage(wfmStrings.sureYouWantToDelete());
                        messageBox.addCloseHandler(new CloseHandler() {
                            @Override
                            public void onSubmit() {
                                ArrayList<Integer> objectIDs = new ArrayList<>();
                                objectIDs.add(item.getObjectID());
                                LoadingPanel.loading(true);
                                CRMService.App.get().deleteEvent(objectIDs, new AbstractAsyncCallback<ArrayList<Integer>>() {
                                    @Override
                                    public void failure(Throwable caught) {
                                        LoadingPanel.loading(false);
                                    }

                                    @Override
                                    public void success(ArrayList<Integer> result) {
                                        LoadingPanel.loading(false);
                                        String messageT = (item.isCallLog() ? wfmMessages.yourSomethingHasBeenDeleted(wfmStrings.call().toLowerCase()) :
                                                item.isInterview() ? wfmMessages.yourSomethingHasBeenDeleted(wfmStrings.interview().toLowerCase()) :
                                                        Property.get(Constants.EVENT_LIST, wfmStrings.messSuccessfulyyDeleted(), wfmStrings.event()));
                                        Info.show(messageT, Info.Type.INFO);
                                        listPanel.reloadPage();
                                        WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_CRM_ACTIVITY_DELETED, result, EventListView.this);
                                    }
                                });
                            }
                        });
                        messageBox.open();
                    });
                    removeEvent.ensureDebugId(activityListViewID + "delete_event");
                    actionItemCount++;
                    menuBar.addItem(removeEvent);
                }

                ToolItem toolItem = new ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columnConfig.setColumnSortable(false);
        columnConfig.setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columns.add(columnConfig);

        //Candidate Relation
        columnConfig = new ColumnDefinitionConfig<EventItem, HTML>(wfmStrings.relatedTo() + " " + wfmStrings.candidate(), EventItem.CANDIDATE_RELATION, 100) {
            @Override
            public HTML getCellValue(EventItem rowValue) {
                if (rowValue.getCandidateRelation() != null && rowValue.getCandidateRelation().getToID() != null) {
                    return getLink(rowValue.getCandidateRelation().getToName(), "candidate|summary/" + rowValue.getCandidateRelation().getToID(), rowValue.getCandidateRelation().getToName(), rowValue.getCandidateRelation().getToName());
                }
                return new HTML(wfmStrings.notAvailable());
            }
        };
        columnConfig.setMinimumColumnWidth(50);
        columns.add(columnConfig);

        //Subject
        columnConfig = new ColumnDefinitionConfig<EventItem, Div>(wfmStrings.subject(), EventItem.SUBJECT, 250) {
            @Override
            public Div getCellValue(final EventItem item) {
                Div widget = new Div();
                String subject = item.getSubject();
                String[] subjects = subject.split("[()]");
                if (summaryPermission) {
                    widget.add(getLink(subjects[0], "event|summary/" + item.getObjectID() + (item.isCallLog() ? "/true" : ""), subjects[0]));
                } else {
                    widget.add(getLink(subjects[0], null));
                }
                if (subjects.length == 2 && Utils.hasPermission(Utils.isHRMS() ? HRMS_ADD_NEW_ACTIVITY_LOG_A_CALL : CRM_ADD_NEW_ACTIVITY_LOG_A_CALL)) {
                    MaterialLink callLink = new MaterialLink(" (" + subjects[1] + ")");
                    RelationItem existingRelation = item.getExistingRelation();
                    callLink.addClickHandler(event -> new ActivityQuickAddForm(Appointment.CALL_LOG, item.getPhoneNumber(), null, RelationItem.newEventRelation(existingRelation.getToType(), existingRelation.getToID(), existingRelation.getToName())));
                    widget.add(callLink);
                }
                return widget;
            }
        };
        columnConfig.setMinimumColumnWidth(100);
        columnConfig.setShow(false);
        columns.add(columnConfig);


        //Start Date
        columnConfig = new ColumnDefinitionConfig<EventItem, String>(wfmStrings.startDate(), EventItem.START_DATE, 80) {
            @Override
            public String getCellValue(EventItem item) {
                return DateUtils.formatInternal(item.getStartDate()) + Utils.getHijriDate(item.getStartDate());
            }

            @Override
            public void setCellValue(EventItem rowValue, String cellValue) {
                try {
                    if (cellValue != null && !"".equals(cellValue)) {
                        rowValue.setStartDate(DateUtils.parse(cellValue));
                        if (rowValue.getEndDate() != null)
                            if (!validate(rowValue.getStartDate(), rowValue.getEndDate()))
                                saveCellValue(rowValue);
                        if (rowValue.getEndDate() == null)
                            saveCellValue(rowValue);
                    }
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig.setMinimumColumnWidth(40);
        columns.add(columnConfig);
        //End Date
        columnConfig = new ColumnDefinitionConfig<EventItem, String>(wfmStrings.endDate(), EventItem.END_DATE, 80) {
            @Override
            public String getCellValue(EventItem item) {
                return DateUtils.formatInternal(item.getEndDate()) + Utils.getHijriDate(item.getEndDate());
            }

            @Override
            public void setCellValue(EventItem rowValue, String cellValue) {
                try {
                    if (cellValue != null && !"".equals(cellValue)) {
                        rowValue.setEndDate(DateUtils.parse(cellValue));
                        if (rowValue.getStartDate() != null)
                            if (!validate(rowValue.getStartDate(), rowValue.getEndDate()))
                                saveCellValue(rowValue);
                        if (rowValue.getEndDate() == null)
                            saveCellValue(rowValue);
                    }
                } catch (DateFormatException e) {
                    e.printStackTrace();
                }
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig.setMinimumColumnWidth(40);
        columns.add(columnConfig);


        //Created By
        columnConfig = new ColumnDefinitionConfig<EventItem, String>(wfmStrings.createdBy(), EventItem.CREATER, 120) {
            @Override
            public String getCellValue(EventItem rowValue) {
                return rowValue.getCreatedBy();
            }
        };
        columnConfig.setMinimumColumnWidth(80);
        columns.add(columnConfig);


        //Created Date
        columnConfig = new ColumnDefinitionConfig<EventItem, String>(wfmStrings.createdDate(), EventItem.CREATED_DATE, 100) {
            @Override
            public String getCellValue(EventItem rowValue) {
                return DateUtils.formatInternal(rowValue.getCreatedDate()) + Utils.getHijriDate(rowValue.getCreatedDate());
            }
        };
        columnConfig.setMinimumColumnWidth(50);
        columns.add(columnConfig);


        //Event Type
        columnConfig = new ColumnDefinitionConfig<EventItem, String>(wfmStrings.type(), EventItem.EVENT_TYPE, 170) {
            @Override
            public String getCellValue(EventItem item) {
                return Appointment.CALL_LOG == item.getActivityType() ? wfmStrings.call() : Appointment.INTERVIEW == item.getActivityType() ? wfmStrings.interview() : Appointment.SMS == item.getActivityType() ? wfmStrings.sms() : Property.get(Constants.EVENT_LIST, wfmStrings.event());
            }
        };
        columnConfig.setHorizontalAlignment(HasAlignment.ALIGN_CENTER);
        columnConfig.setMinimumColumnWidth(40);
        columnConfig.setColumnSortable(false);
        columnConfig.setShow(false);
        columns.add(columnConfig);

        if (!Utils.isHRMS()) {
            //Call Type
            columnConfig = new ColumnDefinitionConfig<EventItem, String>(wfmStrings.callType(), EventItem.CALL_TYPE, 60) {
                @Override
                public String getCellValue(EventItem item) {
                    if (item.isCallLog()) {
                        if (item.isMissedCall()) {
                            return wfmStrings.missed();
                        } else if (item.isInboundCall()) {
                            return wfmStrings.inbound();
                        } else /*if (item.isOutboundCall())*/ {
                            return wfmStrings.outbound();
                        }/* else {
                            return wfmStrings.na();
                        }*/
                    } else {
                        return wfmStrings.notAvailable();
                    }
                }
            };
            columnConfig.setMinimumColumnWidth(30);
            columnConfig.setColumnSortable(false);
            columnConfig.setShow(false);
            columns.add(columnConfig);
            //Assignees
            columnConfig = new ColumnDefinitionConfig<EventItem, String>(wfmStrings.assignees(), EventItem.ASSIGNEE, 100) {
                @Override
                public String getCellValue(EventItem item) {
                    return item.getSharedEmployeesString() != null && !"".equals(item.getSharedEmployeesString()) ? item.getSharedEmployeesString() : wfmStrings.notAvailable();
                }
            };
            columnConfig.setMinimumColumnWidth(50);
            columnConfig.setShow(false);
            columns.add(columnConfig);
        }

        //Modified By
        columnConfig = new ColumnDefinitionConfig<EventItem, String>(wfmStrings.modifiedBy(), EventItem.UPDATER, 120) {
            @Override
            public String getCellValue(EventItem rowValue) {
                return rowValue.getLastModifiedBy();
            }
        };
        columnConfig.setMinimumColumnWidth(80);
        columnConfig.setShow(false);
        columns.add(columnConfig);

        //Modified Date
        columnConfig = new ColumnDefinitionConfig<EventItem, String>(wfmStrings.modifiedDate(), EventItem.UPDATED_DATE, 100) {
            @Override
            public String getCellValue(EventItem rowValue) {

                return DateUtils.formatInternal(rowValue.getLastModifiedDate()) + Utils.getHijriDate(rowValue.getLastModifiedDate());
            }
        };
        columnConfig.setMinimumColumnWidth(50);
        columnConfig.setShow(false);
        columns.add(columnConfig);

        //Duration
        columnConfig = new ColumnDefinitionConfig<EventItem, String>(wfmStrings.duration(), EventItem.DURATION, 100) {
            @Override
            public String getCellValue(EventItem rowValue) {
                String duration = "";
                if (rowValue != null && rowValue.getCallDuration() > 0) {
                    if (rowValue.getCallDuration() / 60 < 10) {
                        duration += "0";
                    }
                    duration += (int) rowValue.getCallDuration() / 60 + ":";

                    if (rowValue.getCallDuration() % 60 < 10) {
                        duration += "0";
                    }
                    duration += rowValue.getCallDuration() % 60;
                } else {
                    duration += "0";
                }
                return duration;
            }
        };
        columnConfig.setMinimumColumnWidth(50);
        columnConfig.setShow(false);
        columns.add(columnConfig);

        //Lead Relation
        columnConfig = new ColumnDefinitionConfig<EventItem, HTML>(wfmStrings.relatedTo() + " " + wfmStrings.lead(), EventItem.LEAD_RELATION, 100) {
            @Override
            public HTML getCellValue(EventItem rowValue) {
                if (rowValue.getLeadRelation() != null && rowValue.getLeadRelation().getToID() != null) {
                    return getLink(rowValue.getLeadRelation().getToName(), "lead|summary/" + rowValue.getLeadRelation().getToID(), rowValue.getLeadRelation().getToName(), rowValue.getLeadRelation().getToName());
                }
                return new HTML(wfmStrings.notAvailable());
            }
        };
        columnConfig.setMinimumColumnWidth(50);
        columnConfig.setShow(false);
        columns.add(columnConfig);

        //Contact Relation
        columnConfig = new ColumnDefinitionConfig<EventItem, HTML>(wfmStrings.relatedTo() + " " + wfmStrings.contact(), EventItem.CONTACT_RELATION, 100) {
            @Override
            public HTML getCellValue(EventItem rowValue) {
                if (rowValue.getContactRelation() != null && rowValue.getContactRelation().getToID() != null) {
                    if (Utils.hasPermission(PermissionConstants.CRM_CONTACTS_SUMMARY)) {
                        return getLink(rowValue.getContactRelation().getToName(), "contact|summary/" + rowValue.getContactRelation().getToID(), rowValue.getContactRelation().getToName(), rowValue.getContactRelation().getToName());
                    } else {
                        return getLink(rowValue.getContactRelation().getToName(), null);
                    }
                }
                return new HTML(wfmStrings.notAvailable());
            }
        };
        columnConfig.setMinimumColumnWidth(50);
        columnConfig.setShow(false);
        columns.add(columnConfig);

        //Crm Account Relation
        columnConfig = new ColumnDefinitionConfig<EventItem, HTML>(wfmStrings.relatedTo() + " " + wfmStrings.crmAccount(), EventItem.CRM_ACCOUNT_RELATION, 100) {
            @Override
            public HTML getCellValue(EventItem rowValue) {
                if (rowValue.getCrmAccountRelation() != null && rowValue.getCrmAccountRelation().getToID() != null && Utils.hasPermission(PermissionConstants.CRM_ACCOUNTS_SUMMARY)) {
                    return getLink(rowValue.getCrmAccountRelation().getToName(), "account|summary/" + rowValue.getCrmAccountRelation().getToID(), rowValue.getCrmAccountRelation().getToName(), rowValue.getCrmAccountRelation().getToName());
                }
                return new HTML(wfmStrings.notAvailable());
            }
        };
        columnConfig.setMinimumColumnWidth(50);
        columnConfig.setShow(false);
        columns.add(columnConfig);

        //Employee Relation
        columnConfig = new ColumnDefinitionConfig<EventItem, HTML>(wfmStrings.relatedTo() + " " + wfmStrings.employee(), EventItem.EMPLOYEE_RELATION, 100) {
            @Override
            public HTML getCellValue(EventItem rowValue) {
                if (rowValue.getEmployeeRelation() != null && rowValue.getEmployeeRelation().getToID() != null) {
                    return getLink(rowValue.getEmployeeRelation().getToName(), "employeeProfile|employeeProfileView/" + rowValue.getEmployeeRelation().getToID(), rowValue.getEmployeeRelation().getToName(), rowValue.getEmployeeRelation().getToName());
                }
                return new HTML(wfmStrings.notAvailable());
            }
        };
        columnConfig.setMinimumColumnWidth(50);
        columnConfig.setShow(false);
        columns.add(columnConfig);

        if (Utils.hasPermission(PermissionConstants.CRM_EDIT_ACTIVITY)) {
            initCellEdit(CustomColumnDefinitionConfig.getEditableColumns(columns));
        }
        return columns.toArray(new CustomColumnDefinitionConfig[]{});
    }

    private void initCellEdit(Map<String, CustomColumnDefinitionConfig> columns) {
        for (final Map.Entry<String, CustomColumnDefinitionConfig> entry : columns.entrySet()) {
            InlineCellEditor widget = null;
            CustomColumnDefinitionConfig column = entry.getValue();
            if (EventItem.START_DATE.equals(entry.getKey()) || EventItem.END_DATE.equals(entry.getKey())) {
                widget = new DateTimePickerCellEditor<String>(true) {
                    @Override
                    protected String getValue() {
                        return DateUtils.format1(getDate());
                    }

                    @Override
                    protected void setValue(String cellValue) {
                        try {
                            if (cellValue != null && !"".equals(cellValue)) {
                                setDate(DateUtils.parse(cellValue), true);
                            }
                        } catch (DateFormatException e) {
                            e.printStackTrace();
                        }
                    }
                };
            }
            if (widget != null) {
                column.setCellEditor(widget);
                column.setCellChangesSave((rowValue, columnCodeName) -> saveEventsCellValue((EventItem) rowValue, columnCodeName));
            }
        }
    }

    private void saveEventsCellValue(EventItem rowValue, String columnCodeName) {
        if ((rowValue.getAsteriskid() == null || rowValue.getAsteriskid().trim().isEmpty()) && (rowValue.getTwilioCallSID() == null || rowValue.getTwilioCallSID().trim().isEmpty())) {
            CRMService.App.get().saveEventEditCellValue(rowValue, columnCodeName, new AbstractAsyncCallback<Void>() {
            });
        } else {
            Info.show(wfmMessages.youCanEditAsteriksOrTwilio(rowValue.getAsteriskid() != null && !rowValue.getAsteriskid().trim().isEmpty() ? "Asterisk" : "Twilio"), Info.Type.WARNING);
        }
    }

    private boolean validate(Date startDate, Date endDate) {
        if (!startDate.before(endDate)) {
            Info.show(wfmStrings.canNotBeEarlier(), Info.Type.WARNING);
            return true;
        }
        return false;
    }

    private GuideListingPanelDesign getListDesign() {
        return new GuideListingPanelDesign() {
            @Override
            public Command getAddNewItemCommand() {
                if (Utils.hasPermission(Utils.isHRMS() ? PermissionConstants.HRMS_ADD_NEW_ACTIVITY_EVENT : PermissionConstants.CRM_ADD_NEW_ACTIVITY_EVENT)) {
                    if (accountRelationID != null) {
                        return (() -> new ActivityQuickAddForm(Utils.isHRMS() ? Appointment.INTERVIEW : Appointment.EVENT, RelationItem.newEventRelation(relationType, relationID, relationName), RelationItem.newEventRelation(accountRelationType, accountRelationID, accountRelationName)));
                    } else {
                        return (() -> new ActivityQuickAddForm(Utils.isHRMS() ? Appointment.INTERVIEW : Appointment.EVENT, RelationItem.newEventRelation(relationType, relationID, relationName)));
                    }
                } else if (Utils.hasPermission(Utils.isHRMS() ? PermissionConstants.HRMS_FULL_ADD_NEW_ACTIVITY_EVENT : PermissionConstants.CRM_FULL_ADD_NEW_ACTIVITY_EVENT)) {
                    if (relationID != null && relationType != null) {
                        return (() -> SinksContainerFactory.entryPoint.onHistoryChanged("event|add/add/" + (Utils.isHRMS() ? Appointment.INTERVIEW : Appointment.EVENT) + "/" + relationID + "/" + relationType));
                    } else {
                        return (() -> SinksContainerFactory.entryPoint.onHistoryChanged("event|add/add/" + (Utils.isHRMS() ? Appointment.INTERVIEW : Appointment.EVENT)));
                    }
                }
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
                        return (data, callback) -> {
                            if (relationID != null) {
                                data.setCustomDataPut(FacetFilterCutomField.RELATION_ID, relationID.toString());
                            }
                            if (relationType != null) {
                                data.setCustomDataPut(FacetFilterCutomField.RELATION_TYPE, relationType);
                            }
                            Integer createdFrom = Utils.isHRMS() ? Appointment.FROM_HRMS : Appointment.FROM_CRM;
                            RbacService.App.get().getEventFacetFilterData(data, eventType, createdFrom, new AbstractAsyncCallback<FacetFilterRpc>() {
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

                    @Override
                    public DatePeriodFacetContent getPeriodDateContent() {
                        return getDatePeriodFacetContent();
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarNew() {
                if (Utils.isHRMS() ? Utils.hasPermission(PermissionConstants.HRMS_ADD_NEW_ACTIVITY_EVENT, PermissionConstants.HRMS_ADD_NEW_ACTIVITY_LOG_A_CALL, PermissionConstants.HRMS_FULL_ADD_NEW_ACTIVITY_EVENT, PermissionConstants.HRMS_FULL_ADD_NEW_ACTIVITY_LOG_A_CALL) :
                        Utils.hasPermission(PermissionConstants.CRM_ADD_NEW_ACTIVITY_EVENT, CRM_ADD_NEW_ACTIVITY_LOG_A_CALL, PermissionConstants.CRM_FULL_ADD_NEW_ACTIVITY_EVENT, PermissionConstants.CRM_FULL_ADD_NEW_ACTIVITY_LOG_A_CALL)) {
                    ActionButton addNew = getAddNewButton(ActionButton.Type.TOOLMENU);
                    addNew.ensureDebugId(activityListViewID + "add_new_menu_button");

                    MenuBar menuBar = new MenuBar(true);
                    menuBar.setAutoOpen(true);

                    if (Utils.hasPermission(Utils.isHRMS() ? PermissionConstants.HRMS_FULL_ADD_NEW_ACTIVITY_EVENT : PermissionConstants.CRM_FULL_ADD_NEW_ACTIVITY_EVENT)) {
                        MenuPopItem addButton = new MenuPopItem(Utils.isHRMS() ? wfmStrings.interview() : Property.get(Constants.EVENT_LIST, wfmStrings.event()), "icon-event-interview", () -> {
                            if (relationID != null && relationType != null) {
                                SinksContainerFactory.entryPoint.onHistoryChanged("event|add/add/" + (Utils.isHRMS() ? Appointment.INTERVIEW : Appointment.EVENT) + "/" + relationID + "/" + relationType);
                            } else {
                                SinksContainerFactory.entryPoint.onHistoryChanged("event|add/add/" + (Utils.isHRMS() ? Appointment.INTERVIEW : Appointment.EVENT));
                            }
                        });
                        menuBar.addItem(addButton);
                    }
                    if (Utils.hasPermission(Utils.isHRMS() ? PermissionConstants.HRMS_FULL_ADD_NEW_ACTIVITY_LOG_A_CALL : PermissionConstants.CRM_FULL_ADD_NEW_ACTIVITY_LOG_A_CALL)) {
                        MenuPopItem callAdd = new MenuPopItem(Property.get(Constants.LOGACALL, wfmStrings.logCall()), "icon-call-log", () -> {
                            if (relationID != null && relationType != null) {
                                SinksContainerFactory.entryPoint.onHistoryChanged("event|add/add/" + Appointment.CALL_LOG + "/" + relationID + "/" + relationType);
                            } else {
                                SinksContainerFactory.entryPoint.onHistoryChanged("event|add/add/" + Appointment.CALL_LOG);
                            }
                        });
                        menuBar.addItem(callAdd);
                    }
                    if (Utils.hasPermission(Utils.isHRMS() ? PermissionConstants.HRMS_ADD_NEW_ACTIVITY_EVENT : PermissionConstants.CRM_ADD_NEW_ACTIVITY_EVENT)) {
                        MenuPopItem add = new MenuPopItem(wfmStrings.quick() + " " + (Utils.isHRMS() ? wfmStrings.interview() : Property.get(Constants.EVENT_LIST, wfmStrings.event())), "icon-event-interview", () ->
                        {
                            if (accountRelationID != null) {
                                new ActivityQuickAddForm(Utils.isHRMS() ? Appointment.INTERVIEW : Appointment.EVENT, RelationItem.newEventRelation(relationType, relationID, relationName), RelationItem.newEventRelation(accountRelationType, accountRelationID, accountRelationName));
                            } else {
                                new ActivityQuickAddForm(Utils.isHRMS() ? Appointment.INTERVIEW : Appointment.EVENT, RelationItem.newEventRelation(relationType, relationID, relationName));
                            }
                        });
                        add.ensureDebugId(activityListViewID + (Utils.isHRMS() ? "add_interview" : "add_event"));
                        menuBar.addItem(add);
                    }
                    if (Utils.hasPermission(Utils.isHRMS() ? PermissionConstants.HRMS_ADD_NEW_ACTIVITY_LOG_A_CALL : CRM_ADD_NEW_ACTIVITY_LOG_A_CALL)) {
                        MenuPopItem addNewCallLog = new MenuPopItem(wfmStrings.quick() + " " + wfmStrings.call(), "icon-call-log", () -> {
                            if (accountRelationID != null) {
                                new ActivityQuickAddForm(Appointment.CALL_LOG, RelationItem.newEventRelation(relationType, relationID, relationName), RelationItem.newEventRelation(accountRelationType, accountRelationID, accountRelationName));
                            } else {
                                new ActivityQuickAddForm(Appointment.CALL_LOG, RelationItem.newEventRelation(relationType, relationID, relationName));
                            }
                        });
                        addNewCallLog.ensureDebugId(activityListViewID + "log_a_call");
                        menuBar.addItem(addNewCallLog);
                    }
                    addNew.setMenu(menuBar);
                    return addNew;
                }
                return null;
            }

            @Override
            public ActionButton initTopToolBarMore() {
                if (Utils.hasPermission(PermissionConstants.CRM_REMOVE_ACTIVITY)) {
                    actionButton = getRemoveMoreButton(clickEvent -> deleteSelection());
                    return actionButton;
                }
                return null;
            }

            @Override
            public void initImportExportToolBarWidgets(ExportImportOption exportOption, MaterialDropDown menuContainer) {
                exportOption.initExport(null);
                if (menuContainer != null && menuContainer.getParent() != null && menuContainer.getParent() instanceof MaterialLink) {
                    ((MaterialLink) menuContainer.getParent()).setTooltip(wfmStrings.export());
                }
            }

            @Override
            public ListingActionMenu initLeftTopActionMenu() {
                return null;
            }

            @Override
            public void initDataEmptyTable(ListingEmptyDataInitializer emptyDataTable) {
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(Property.getPluralWithObjectCodeWithReplace(Constants.EVENT_LIST, crmStrings.messCurrentlyActivities(), wfmStrings.activities()));
                message.setHref(clickEvent -> {
                    if (accountRelationID != null) {
                        new ActivityQuickAddForm(Appointment.EVENT, RelationItem.newEventRelation(relationType, relationID, relationName), RelationItem.newEventRelation(accountRelationType, accountRelationID, accountRelationName));
                    } else {
                        new ActivityQuickAddForm(Appointment.EVENT, RelationItem.newEventRelation(relationType, relationID, relationName));
                    }
                });
                message.setTextBeforeLink(Property.get(Constants.EVENT_LIST, crmStrings.pleaseAddNewEvent(), wfmStrings.event()));
                emptyDataTable.initEmptyDataTable(message);
            }

            @Override
            public boolean isEditCustomFieldCell() {
                return Utils.hasPermission(PermissionConstants.CRM_EDIT_ACTIVITY);
            }
        };
    }

    private FacetContentConfigure getFacetContentConfigure() {
        FacetContentConfigure contentConfigure = new FacetContentConfigure(3, wfmStrings.filter());

        contentConfigure.addContentConfigure(FacetContentType.EventFacetFilter.getContentCode()[0], wfmStrings.assignee(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrEventRepresenter.FIELD_SHARED_USER_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrEventRepresenter.FIELD_SHARED_USER_ID_NAME;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.EventFacetFilter.getContentCode()[1], Property.get(Constants.EVENT_LIST, wfmStrings.type(), wfmStrings.activity()), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrEventRepresenter.FIELD_ACTIVITY_TYPE_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrEventRepresenter.FIELD_ACTIVITY_TYPE_ID_NAME;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.EventFacetFilter.getContentCode()[15], crmStrings.relatedCandidate(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_CANDIDATE;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_CANDIDATE;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.EventFacetFilter.getContentCode()[16], wfmStrings.createdBy(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrEventRepresenter.FIELD_OWNER_ID;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrEventRepresenter.FIELD_OWNER_ID_NAME;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });

        contentConfigure.addContentConfigure(FacetContentType.EventFacetFilter.getContentCode()[2], wfmStrings.callType(), new FacetFieldConfigure() {
            @Override
            public String getSolrFieldCriteriaName() {
                return SolrEventRepresenter.FIELD_CALL_TYPE;
            }

            @Override
            public String getSolrFacetFieldName() {
                return SolrEventRepresenter.FIELD_CALL_TYPE;
            }

            @Override
            public boolean isConditionItemId() {
                return false;
            }

            @Override
            public boolean isShowFacetConttentFilter() {
                return false;
            }
        });

        if (!Utils.isHRMS()) {
            contentConfigure.addContentConfigure(FacetContentType.EventFacetFilter.getContentCode()[3], crmStrings.relatedContact(), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_CONTACT;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_CONTACT;
                }

                @Override
                public boolean isShowFacetConttentFilter() {
                    return false;
                }
            });
            contentConfigure.addContentConfigure(FacetContentType.EventFacetFilter.getContentCode()[4], wfmStrings.relatedCrmAccount(), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_CRM_ACCOUNT;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_CRM_ACCOUNT;
                }

                @Override
                public boolean isShowFacetConttentFilter() {
                    return false;
                }
            });
            contentConfigure.addContentConfigure(FacetContentType.EventFacetFilter.getContentCode()[5], Property.get(Constants.LEADS, wfmStrings.relatedRFQ(), wfmStrings.lead()), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_LEAD;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_LEAD;
                }

                @Override
                public boolean isShowFacetConttentFilter() {
                    return false;
                }
            });
            contentConfigure.addContentConfigure(FacetContentType.EventFacetFilter.getContentCode()[6], Property.get(Constants.CASE_LIST, crmStrings.relatedCase(), wfmStrings.caseID()), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_CASE;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_CASE;
                }

                @Override
                public boolean isShowFacetConttentFilter() {
                    return false;
                }
            });
            contentConfigure.addContentConfigure(FacetContentType.EventFacetFilter.getContentCode()[7], Property.get(Constants.Opportunities, wfmStrings.relatedRFQ(), wfmStrings.opportunity()), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_OPPORTUNITY;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_OPPORTUNITY;
                }

                @Override
                public boolean isShowFacetConttentFilter() {
                    return false;
                }
            });
            contentConfigure.addContentConfigure(FacetContentType.EventFacetFilter.getContentCode()[8], wfmStrings.relatedProject(), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_PROJECT;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_PROJECT;
                }

                @Override
                public boolean isShowFacetConttentFilter() {
                    return false;
                }
            });
            contentConfigure.addContentConfigure(FacetContentType.EventFacetFilter.getContentCode()[9], crmStrings.relatedTask(), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_TASK;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_TASK;
                }

                @Override
                public boolean isShowFacetConttentFilter() {
                    return false;
                }
            });
            //related issue
            contentConfigure.addContentConfigure(FacetContentType.EventFacetFilter.getContentCode()[10], Property.get(Constants.ISSUE, wfmStrings.relatedIssue(), wfmStrings.issue()), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_ISSUE;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_ISSUE;
                }

                @Override
                public boolean isShowFacetConttentFilter() {
                    return false;
                }
            });
            //related employee
            contentConfigure.addContentConfigure(FacetContentType.EventFacetFilter.getContentCode()[11], wfmStrings.relatedEmployee(), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_EMPLOYEE;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_EMPLOYEE;
                }

                @Override
                public boolean isShowFacetConttentFilter() {
                    return false;
                }
            });
            //related department
            contentConfigure.addContentConfigure(FacetContentType.EventFacetFilter.getContentCode()[12], Property.get(Constants.DEPARTMENT_LIST, wfmStrings.relatedDepartment(), wfmStrings.department()), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_DEPARTMENT;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_DEPARTMENT;
                }

                @Override
                public boolean isShowFacetConttentFilter() {
                    return false;
                }
            });
            //related client
            contentConfigure.addContentConfigure(FacetContentType.EventFacetFilter.getContentCode()[13], Property.get(Constants.CLIENT_LIST, wfmStrings.relatedClient()), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_CLIENT;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_CLIENT;
                }

                @Override
                public boolean isShowFacetConttentFilter() {
                    return false;
                }
            });
            //related supplier
            contentConfigure.addContentConfigure(FacetContentType.EventFacetFilter.getContentCode()[14], Property.get(Constants.SUPPLIER_LIST, wfmStrings.relatedSupplier()), new FacetFieldConfigure() {
                @Override
                public String getSolrFieldCriteriaName() {
                    return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID + RelationItem.TYPE_SUPPLIER;
                }

                @Override
                public String getSolrFacetFieldName() {
                    return SolrEventRepresenter.DYNAMIC_FIELD_RELATED_ID_NAME + RelationItem.TYPE_SUPPLIER;
                }

                @Override
                public boolean isShowFacetConttentFilter() {
                    return false;
                }
            });
        }

        return contentConfigure;
    }

    private ListingRequestProvider<EventItem> getListProvider() {
        return (filterParametrs, listingCallback) -> initEventList(filterParametrs, listingCallback, null);
    }

    private void initEventList(ListingFilterParameter filterParametrs, ListingCallback<EventItem> listingCallback, Span container) {
        filterParametrs = filterParametrs == null ? new ListingFilterParameter() : filterParametrs;
        filterParametrs.setRelationID(relationID);
        if (accountRelationType != null && CrmConstants.CRM_ACCOUNT.equals(relationType)) {
            ArrayList<String> relationTypes = new ArrayList<>();
            relationTypes.add(relationType);
            relationTypes.add(accountRelationType);
            relationTypes.add(CrmConstants.SUPPLIER);
            filterParametrs.setRelationTypes(relationTypes);
        }
        filterParametrs.setRelationType(relationType);
        filterParametrs.setEventType(eventType);
        filterParametrs.setCreatedFrom(Utils.isHRMS() ? Appointment.FROM_HRMS : Appointment.FROM_CRM);
        FacetFilterRpc facetFilterRpc = filterParametrs.getFacetFilter();
        if (facetFilterRpc != null && !facetFilterRpc.getCustomData().isEmpty()) {
            if (facetFilterRpc.getCustomData().containsKey(Appointment.TODAY)
                    && Boolean.valueOf(facetFilterRpc.getCustomData().get(Appointment.TODAY))) {
                facetFilterRpc.setStartDate(null);
                facetFilterRpc.setEndDate(null);
            }
        }
        crmService.getEventList(filterParametrs, new AbstractAsyncCallback<ListResult<EventItem>>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(ListResult<EventItem> result) {
                totalCount = result.getTotal();
                if (listingCallback != null) {
                    listingCallback.onSuccess(result);
                }
                statisticShortcut = statisticShortcut != null ? statisticShortcut : container;
                if (statisticShortcut != null) {
                    if (totalCount > 0) {
                        statisticShortcut.setText(countFormat(totalCount));
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
    public String getIconStyle() {
        return "event event-list";
    }

    protected void deleteSelection() {
        if (selectedItems.size() == 0) {
            String messageT = Utils.isHRMS() ? crmMessages.pleaseSelectOneRow(wfmStrings.call() + " or " + wfmStrings.interview()) : crmMessages.pleaseSelectOneRow(Property.get(Constants.EVENT_LIST, wfmStrings.event()));
            Info.show(messageT, Info.Type.WARNING);
        } else {
            showDeleteMessage();
        }
    }

    private void showDeleteMessage() {
        final WfmMessageBox messageBox = new WfmMessageBox(IconEnum.QUESTION, Action.YesNo, true);
        messageBox.setTitle(wfmStrings.warning());
        EventItem item = (EventItem) selectedItems.iterator().next();
        String message = wfmStrings.areYouSureYouWantToDeleteTheSelectedRecords();
        messageBox.setMessage(message);
        messageBox.addCloseHandler(new CloseHandler() {
            @Override
            public void onSubmit() {
                ArrayList<Integer> ids = getIDsOnly(selectedItems);
                if (ids.size() > 0) {
                    LoadingPanel.loading(true);
                    crmService.deleteEvent(ids, new AbstractAsyncCallback<ArrayList<Integer>>() {
                        @Override
                        public void failure(Throwable caught) {
                            LoadingPanel.loading(false);
                            Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
                        }

                        @Override
                        public void success(ArrayList<Integer> result) {
                            LoadingPanel.loading(false);
                            listPanel.reloadPage();
                            Info.show(Property.get(Constants.EVENT_LIST, wfmStrings.messSuccessfulyyDeleted(), wfmStrings.event()), Info.Type.INFO);
                        }
                    });
                }
            }
        });
        messageBox.open();
    }

    private DatePeriodFacetContent getDatePeriodFacetContent() {
        return new DatePeriodFacetContent() {

            @Override
            public void getDateFacetContent(FlexTable datePeriod) {
                todayBox = new KpiCheckBox("<span class='customTitle' style='padding-left: 8px;'>" + wfmStrings.today() + "</span>", true);
                todayBox.addClickHandler(clickEvent -> {
                    listPanel.getFacetPopup().getFacetFilterRpc().setCustomDataPut(Appointment.TODAY, todayBox.getValue().toString());
                    listPanel.refreshFacetFilter();
                });

                datePeriod.setCellSpacing(5);
                int row = datePeriod.getRowCount();
                datePeriod.setWidget(row, 0, todayBox);
                datePeriod.getFlexCellFormatter().setColSpan(row++, 0, 2);
            }

            @Override
            public void refreshFacetFilter(FacetFilterRpc data) {
                if (data.getCustomData().containsKey(Appointment.TODAY)) {
                    todayBox.setValue(Boolean.valueOf(data.getCustomData().get(Appointment.TODAY)), true);
                }
            }

            @Override
            public void reset() {
                todayBox.setValue(false);
            }
        };
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
            initEventList(fp, null, container);
            onInitialize();
            clear();
        }
    }

    @Override
    public String getPropertyCode() {
        return EVENT_LIST;
    }
}
