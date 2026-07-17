package com.edatasite.workforce.gwt.timesheet.client.ui;

import com.edatasite.workforce.gwt.core.client.*;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetContentConfigure;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.*;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.facetfilter.FacetCallbackProvider;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.core.client.ui.landing.HelpPanelGenerator;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.*;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.actionsmenu.ListingActionMenu;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.column.ColumnDefinitionConfig;
import com.edatasite.workforce.gwt.core.client.ui.listpanel.filter.ListingChooseFilter;
import com.edatasite.workforce.gwt.core.client.ui.menu.ActionButton;
import com.edatasite.workforce.gwt.core.client.ui.menu.MenuPopItem;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimeSheetApprovalListItem;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetService;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.gen2.table.client.SelectionGrid;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import gwt.material.design.client.ui.MaterialDropDown;

import java.util.ArrayList;
import java.util.Set;

/**
 * User: Hasan
 * Date: Sen 6, 2011
 * Time: 3:20:45 PM
 */
public class TimesheetApprovalListView extends BaseListView implements Constants {

    
    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private static final TimesheetServiceAsync timesheetService = TimesheetService.App.get();

    private ListingPanel<TimeSheetApprovalListItem> listingTable;
    protected ContextMenu actions;
    private Set<TimeSheetApprovalListItem> selectedRows;

    public TimesheetApprovalListView() {
        super(TIMESHEET_APPROVAL_LIST);
        setDescription(property.getPlural(wfmStrings.timesheetApproval()));
    }

    @Override
    public String getIconStyle() {
        return "bgMark icon-timesheet-approval-new";
    }

    @Override
    public FlowPanel getHelpContainer() {
        if (helpPanel == null) {
            helpPanel = HelpPanelGenerator.getHelpPanel(PermissionConstants.PM_CONTEXT, PermissionConstants.PM_TIMESHEET_APPROVAL);
        }
        return helpPanel;
    }

    @Override
    protected Widget onInitialize() {
        listingTable = new ListingPanel<>(ListPanelType.TimeSheetApprovalPanel, getColumnConfigs(), getListingRequestProvider(), getListingPanelDesign(), SelectionGrid.SelectionPolicy.CHECKBOX);
        listingTable.setExcelListener(clickEvent -> {
            ListingFilterParameter filterParameter = listingTable.getFilterParametrs();
            String excelURL = CommandConstants.COMMON_URL + "/downloadTimesheetApprovalListExcel";
            filterParameter.setPropertyCode(getPropertyCode());
            listingTable.callListExcel(excelURL, filterParameter);

        });

        listingTable.setPDFListener(clickEvent -> {
            String pdfURL = CommandConstants.PDF_URL + "/timesheetApprovalListPDFHandler";
            ListingFilterParameter filterParametrs = listingTable.getFilterParametrs();
            filterParametrs.setPropertyCode(getPropertyCode());
            listingTable.callListPDF(pdfURL, filterParametrs);
        });
        listingTable.addSelectionRowHandler(selected -> selectedRows = selected);
        add(listingTable);
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TIMESHEET_SUBMIT_FOR_APPROVAL, TimesheetApprovalListView.this, (sender, args) -> listingTable.reloadPage());
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TIMESHEET_APPROVAL, TimesheetApprovalListView.this, (sender, args) -> listingTable.reloadPage());
        listingTable.reloadPage();
        return null;
    }

    private ListingPanelDesign getListingPanelDesign() {
        return new ListingPanelDesign() {
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
                        return ListingChooseFilter.TIMESHEET_APPROVAL_LIST;
                    }

                    @Override
                    public ArrayList<String> getCustomFacetFilterFields() {
                        ArrayList<String> fields = new ArrayList<>(4);
                        fields.add(ListingChooseFilter.RELATED_PROJECT);
                        fields.add(ListingChooseFilter.EMPLOYEES);
                        fields.add(ListingChooseFilter.TIMESHEET_APPROVERS);
                        fields.add(ListingChooseFilter.TIMESHEET_APPROVE_STATUS);
                        return fields;
                    }

                    @Override
                    public ViewName getView() {
                        return ViewName.TimeSheetApproval;
                    }
                };
            }

            @Override
            public ActionButton initTopToolBarMore() {
                final ActionButton more = new ActionButton(ActionButton.getMoreString(), ActionButton.Type.TOOLMENU);
                more.ensureDebugId("Timsheet_approval_list_more_button");
                more.addDomHandler(event -> {
                    MenuBar menu = getActionsForSelections();
                    menu.setAutoOpen(true);
                    more.setMenu(menu);
                }, MouseOverEvent.getType());
                return more;
            }

            @Override
            public ActionButton initTopToolBarNew() {
                final ActionButton approve = getAddNewButton();
                approve.ensureDebugId("timesheet_approveAll_button");
                approve.addClickHandler(event -> {
                    SinksContainerFactory.entryPoint.onHistoryChanged("timesheet|edit/1");
                    listingTable.reloadPage();
                });
                return approve;
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
                DefaultNoItemsMessage message = new DefaultNoItemsMessage(wfmStrings.thereAreNoTimeEntriesForApprovalYet());
                emptyDataTable.initEmptyDataTable(message);
            }
        };
    }

    private MenuBar getActionsForSelections() {
            if ((selectedRows != null && selectedRows.size() > 0)) {
                actions = new ContextMenu();
                //Batch Approve
                final KpiModal approvePopup = new KpiModal();
                approvePopup.setCloseButton(true);
                approvePopup.setWidth(400);
                final TextArea2 approvalCommentArea = new TextArea2();
                approvalCommentArea.getTextArea().setVisibleLines(5);
                approvalCommentArea.setWidth("100%");
                VerticalPanel approvalCommentPanelDiv = new VerticalPanel();
                approvalCommentPanelDiv.add(new Label(projectStrings.approvalComment()));
                approvalCommentPanelDiv.add(approvalCommentArea);
                WfmButton2 approveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
                approveButton.addClickHandler(clickEvent -> {
                    ArrayList<Integer> itemIds = new ArrayList<>();
                    for (TimeSheetApprovalListItem selectedRow : selectedRows) {
                        if (!TimeSheetApprovalListItem.APPROVEDS.equals(selectedRow.getStatusCode()))
                            itemIds.add(selectedRow.getId());
                    }
                    sendToBatchUpdate(itemIds, approvalCommentArea.getText(), true);
                    approvePopup.close();
                });
                approvePopup.add(approvalCommentPanelDiv);
                approvePopup.addButton(approveButton);

                MenuItem approve = new MenuItem("<span>" + wfmStrings.approveAll() + "</span>", true, (Command) () -> {
                    Command closeCommand = () -> {
                        approvePopup.close();
                    };
                    approvePopup.open();
                });
                actions.getMenuBar().addItem(approve);

                //Batch Reject
                final KpiModal rejectPopup = new KpiModal();
                rejectPopup.setCloseButton(true);
                rejectPopup.setWidth(400);
                final TextArea2 rejectionCommentArea = new TextArea2();
                rejectionCommentArea.getTextArea().setVisibleLines(5);
                rejectionCommentArea.setWidth("100%");
                VerticalPanel rejectionPanelDiv = new VerticalPanel();
                rejectionPanelDiv.add(new Label(wfmStrings.rejectionReason()));
                rejectionPanelDiv.add(rejectionCommentArea);
                WfmButton2 rejectButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
                rejectButton.addClickHandler(clickEvent -> {
                    ArrayList<Integer> itemIds = new ArrayList<>();
                    for (TimeSheetApprovalListItem selectedRow : selectedRows) {
                        if (!wfmStrings.rejected().equals(selectedRow.getStatus()))
                            itemIds.add(selectedRow.getId());
                    }
                    sendToBatchUpdate(itemIds, rejectionCommentArea.getText(),false);
                    rejectPopup.close();
                    listingTable.reloadPage();
                });
                rejectPopup.add(rejectionPanelDiv);
                rejectPopup.addButton(rejectButton);

                MenuItem reject = new MenuItem("<span>" + wfmStrings.rejectAll() + "</span>", true, (Command) () -> {
                    Command closeCommand = () -> {
                        rejectPopup.close();
                    };
                    rejectPopup.open();
                });
                actions.getMenuBar().addItem(reject);

                return actions.getMenuBar();
            }
        return null;
    }

    private void sendToBatchUpdate(ArrayList<Integer> itemIds, String comment, boolean isApproved) {
        timesheetService.timesheetBatchApproveOrReject(itemIds, comment,isApproved, new AbstractAsyncCallback<Void>() {
            public void failure(Throwable caught) {
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            public void success(Void result) {
                LoadingPanel.loading(false);
                Info.show(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.approvalProcess()), Info.Type.INFO);
                listingTable.reloadPage();
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TIMESHEET_APPROVAL, result,TimesheetApprovalListView.this);
            }
        });
    }

    private ListingRequestProvider<TimeSheetApprovalListItem> getListingRequestProvider() {
        return (filterParametrs, callback) -> TimesheetService.App.get().getTimeSheetApprovalSessionList(filterParametrs, new AsyncCallback<ListResult<TimeSheetApprovalListItem>>() {
            @Override
            public void onFailure(Throwable throwable) {
                callback.onFailure(throwable);
            }

            @Override
            public void onSuccess(ListResult<TimeSheetApprovalListItem> timeSheetApprovalListItemListResult) {
                callback.onSuccess(timeSheetApprovalListItemListResult);
            }
        });
    }

    private ColumnDefinitionConfig[] getColumnConfigs() {
        ColumnDefinitionConfig[] columnConfigs = new ColumnDefinitionConfig[10];
        //action
        columnConfigs[0] = new ColumnDefinitionConfig<TimeSheetApprovalListItem, Anchor>(wfmStrings.action(), LISTING_ACTION.COLUMN_CODE, LISTING_ACTION.COLUMN_WIDTH) {
            @Override
            public Anchor getCellValue(final TimeSheetApprovalListItem rowValue) {
                int actionItemCount = 0;
                MenuBar menuBar = new MenuBar(true);
                MenuPopItem approve = new MenuPopItem(wfmStrings.review(), "icon-timesheet-approval");
                approve.setCommand(() -> SinksContainerFactory.entryPoint.onHistoryChanged("timesheetapproval|approve/" + rowValue.getId(), rowValue.getEmployeeName(), rowValue.getEmployeeName()));
                menuBar.addItem(approve);

                final com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem toolItem = new com.edatasite.workforce.gwt.core.client.ui.menu.ToolItem(actionItemCount);
                toolItem.setWidget(menuBar);
                return toolItem.getAction();
            }
        };
        columnConfigs[0].setMinimumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfigs[0].setMaximumColumnWidth(LISTING_ACTION.COLUMN_WIDTH);
        columnConfigs[0].setColumnSortable(false);
        //employee name
        columnConfigs[1] = new ColumnDefinitionConfig<TimeSheetApprovalListItem, SimpleLink>(wfmStrings.employee(), TimeSheetApprovalListItem.EMPLOYEENAME, 150) {
            @Override
            public SimpleLink getCellValue(TimeSheetApprovalListItem rowValue) {
                return getLink(rowValue.getEmployeeName(), "timesheetapproval|approve/" + rowValue.getId(), rowValue.getEmployeeName(), rowValue.getEmployeeName());
            }
        };
        columnConfigs[1].setMinimumColumnWidth(100);
        //period
        columnConfigs[2] = new ColumnDefinitionConfig<TimeSheetApprovalListItem, String>(wfmStrings.period(), TimeSheetApprovalListItem.FROMDATE, 150) {
            @Override
            public String getCellValue(TimeSheetApprovalListItem timeSheetApprovalListItem) {
                return DateUtils.format(timeSheetApprovalListItem.getFromDate().getNonConvertedDate()) + " - " + DateUtils.format(timeSheetApprovalListItem.getEndDate().getNonConvertedDate());
            }
        };
        columnConfigs[2].setMinimumColumnWidth(100);
        //timeSpent
        columnConfigs[3] = new ColumnDefinitionConfig<TimeSheetApprovalListItem, String>(wfmStrings.timeSpentOnly(), TimeSheetApprovalListItem.TIMESPENT, 60) {
            @Override
            public String getCellValue(TimeSheetApprovalListItem timeSheetApprovalListItem) {
                return timeSheetApprovalListItem.getTimeSpent();
            }
        };
        columnConfigs[3].setMinimumColumnWidth(40);
        columnConfigs[3].setColumnSortable(false);
        //approved hours
        columnConfigs[4] = new ColumnDefinitionConfig<TimeSheetApprovalListItem, String>(wfmStrings.approved(), TimeSheetApprovalListItem.APPROVED, 60) {
            @Override
            public String getCellValue(TimeSheetApprovalListItem timeSheetApprovalListItem) {
                return timeSheetApprovalListItem.getApprovedHours();
            }
        };
        columnConfigs[4].setMinimumColumnWidth(40);
        columnConfigs[4].setColumnSortable(false);
        //approver(s)
        columnConfigs[5] = new ColumnDefinitionConfig<TimeSheetApprovalListItem, String>(wfmStrings.approvers(), TimeSheetApprovalListItem.APPROVER, 150) {
            @Override
            public String getCellValue(TimeSheetApprovalListItem timeSheetApprovalListItem) {
                return timeSheetApprovalListItem.getApprover();
            }
        };
        columnConfigs[5].setMinimumColumnWidth(100);
        columnConfigs[5].setColumnSortable(false);
        //project name
        columnConfigs[6] = new ColumnDefinitionConfig<TimeSheetApprovalListItem, String>(Property.get(Constants.PROJECT, wfmStrings.project()), TimeSheetApprovalListItem.PROJECTNAME, 110) {
            @Override
            public String getCellValue(TimeSheetApprovalListItem timeSheetApprovalListItem) {
                return timeSheetApprovalListItem.getProjectName();
            }
        };
        columnConfigs[6].setMinimumColumnWidth(100);
        columnConfigs[7] = new ColumnDefinitionConfig<TimeSheetApprovalListItem, String>(wfmStrings.status(), TimeSheetApprovalListItem.STATUS, 150) {
            @Override
            public String getCellValue(TimeSheetApprovalListItem item) {
                String status;
                if (TimeSheetApprovalListItem.WAITING.equals(item.getStatusCode())) {
                    status = wfmStrings.waitingForApproval();
                } else if (TimeSheetApprovalListItem.APPROVEDS.equals(item.getStatusCode())) {
                    status = wfmStrings.approved();
                } else if (wfmStrings.rejected().equals(item.getStatus())) {
                    status = wfmStrings.rejected();
                } else {
                    status = item.getStatus();
                }
                return status;
            }
        };
        columnConfigs[7].setMinimumColumnWidth(130);
        //submitted date
        columnConfigs[8] = new ColumnDefinitionConfig<TimeSheetApprovalListItem, String>(wfmStrings.submittedDate(), TimeSheetApprovalListItem.SUBMITTED_DATE, 90) {
            @Override
            public String getCellValue(TimeSheetApprovalListItem item) {
                return ((item.getSubmittedDate() != null && item.getSubmittedDate().getNonConvertedDate() != null) ?
                        DateUtils.format(item.getSubmittedDate().getNonConvertedDate()) : "");
            }
        };
        columnConfigs[8].setMinimumColumnWidth(70);
        columnConfigs[8].setColumnSortable(true);
        //approval date
        columnConfigs[9] = new ColumnDefinitionConfig<TimeSheetApprovalListItem, String>(wfmStrings.approvedDate(), TimeSheetApprovalListItem.APPROVAL_DATE, 90) {
            @Override
            public String getCellValue(TimeSheetApprovalListItem item) {
                return ((item.getApprovalDate() != null && item.getApprovalDate().getNonConvertedDate() != null) ?
                        DateUtils.format(item.getApprovalDate().getNonConvertedDate()) : "");
            }
        };
        columnConfigs[9].setMinimumColumnWidth(70);
        columnConfigs[9].setColumnSortable(true);
        return columnConfigs;
    }

    public String getTimeSpentHM(int timeSpent) {
        String timeSpentHM;
        if (timeSpent == 0) {
            return timeSpentHM = "00:00";
        }
        timeSpentHM = "";
        if (timeSpent / 60 < 10) {
            timeSpentHM = "0";
        }
        timeSpentHM = timeSpentHM + timeSpent / 60;
        timeSpentHM = timeSpentHM + ":";
        if (timeSpent % 60 < 10) {
            timeSpentHM = timeSpentHM + "0";
        }
        timeSpentHM = timeSpentHM + timeSpent % 60;
        return timeSpentHM;
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
    public String getPropertyCode() {
        return TIMESHEET_APPROVAL_LIST;
    }
}