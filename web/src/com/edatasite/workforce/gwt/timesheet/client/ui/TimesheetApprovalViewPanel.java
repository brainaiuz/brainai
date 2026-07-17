package com.edatasite.workforce.gwt.timesheet.client.ui;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.FooteredView;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.interfaces.Colapse;
import com.edatasite.workforce.gwt.core.client.interfaces.FittedContent;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.CallbackSynchronizer;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.LoadingPanel;
import com.edatasite.workforce.gwt.core.client.ui.SortableTable.SortableTable;
import com.edatasite.workforce.gwt.core.client.ui.TextArea2;
import com.edatasite.workforce.gwt.core.client.ui.components.KpiCheckBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBox;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxItem;
import com.edatasite.workforce.gwt.core.client.ui.components.groupBox.GBoxRow;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.Action;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.IconEnum;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.KpiModal;
import com.edatasite.workforce.gwt.core.client.ui.dialogBox.WfmMessageBox;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.notifications.Info;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.IFooteredView;
import com.edatasite.workforce.gwt.core.client.ui.viewFooter.ViewFooter;
import com.edatasite.workforce.gwt.core.client.ui.wfmButton.buttonUtils.WfmButton2;
import com.edatasite.workforce.gwt.project.client.localization.ProjectStrings;
import com.edatasite.workforce.gwt.timesheet.client.TimesheetConstants;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimeSheetApprovalSingleItem;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimeSheetApprovalSingleItemsList;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetService;
import com.edatasite.workforce.gwt.timesheet.client.rpc.TimesheetServiceAsync;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import gwt.material.design.client.ui.MaterialPanel;
import gwt.material.design.client.ui.html.Div;
import gwt.material.design.client.ui.html.Span;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: May 1, 2009
 * Time: 3:47:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class TimesheetApprovalViewPanel extends FooteredView implements FittedContent, Constants, Colapse, Comparator {
    private static final ProjectStrings projectStrings = ProjectStrings.App.get();
    private static final TimesheetServiceAsync timesheetService = TimesheetService.App.get();
    private SortableTable sortableTable;
    private Map<Integer, TimeSheetApprovalSingleItem> timeSheetTimeEntries;
    private WfmButton2 proceed;
    private final Integer objectId;
    private final CallbackSynchronizer callbacksynchronizer = new CallbackSynchronizer();
    private int columnOffset = 0;
    private final String debugID = "gwt-debug_timesheet_";
    private final boolean isShowHourTypeDropDown = "true".equals(Utils.userSettings.get(SHOW_HOUR_TYPE_DROPDOWN));

    public TimesheetApprovalViewPanel(Integer id) {
        super("approve");
        setDescription(property.getPlural(projectStrings.approveAllTimesheets()));
        this.objectId = id;
    }

    protected Widget onInitialize() {
        if (isShowHourTypeDropDown) {
            columnOffset = 1;
        }
        timeSheetTimeEntries = new HashMap<>();
        refreshTable(objectId);
        proceed = new WfmButton2(wfmStrings.proceed(), WfmButton2.BTN_PRIMARY);
        proceed.ensureDebugId("proceed_button");
        proceed.getElement().getStyle().setMargin(10, Style.Unit.PX);
        proceed.addClickHandler(sender -> proceedI(objectId));
        return null;
    }

    private boolean validate() {
        int errors = 0;
        for (int i = 2; i < sortableTable.getRowCount(); i++) {
            KpiCheckBox approve = ((KpiCheckBox) sortableTable.getWidget(i, 6 + columnOffset));
            KpiCheckBox reject = ((KpiCheckBox) sortableTable.getWidget(i, 7 + columnOffset));
            if (!approve.getValue() && !reject.getValue()) {
                errors++;
            }
            if (!validCheckBox(approve, reject)) {
                errors++;
            }
        }
        if (errors > 0) {
            Info.show(projectStrings.plApproveOrRejectAllItems(), Info.Type.WARNING);
            return false;
        }
        return true;
    }

    private boolean validCheckBox(final KpiCheckBox approve, final KpiCheckBox reject) {
        if (!approve.getValue() && !reject.getValue()) {
            approve.addStyleName(ERROR_FORM_STYLE);
            reject.addStyleName(ERROR_FORM_STYLE);

            approve.addClickHandler(event -> {
                if (approve.getValue()) {
                    approve.removeStyleName(ERROR_FORM_STYLE);
                    reject.removeStyleName(ERROR_FORM_STYLE);
                }
            });
            reject.addClickHandler(event -> {
                if (reject.getValue()) {
                    approve.removeStyleName(ERROR_FORM_STYLE);
                    reject.removeStyleName(ERROR_FORM_STYLE);
                }
            });
            return false;
        }
        return true;
    }

    private void refreshTable(Integer objectId) {
        LoadingPanel.loading(true);
        timesheetService.getTimeSheetApprovalSingleListItems(objectId, callbacksynchronizer.registerCallback(new AbstractAsyncCallback<TimeSheetApprovalSingleItemsList>() {
            public void success(final TimeSheetApprovalSingleItemsList appSingleItemList) {
                Set<TimeSheetApprovalSingleItem> treeSet = new TreeSet(TimesheetApprovalViewPanel.this);
                treeSet.addAll(Arrays.asList(appSingleItemList.getItems()));
                boolean editable = appSingleItemList.getPermission() == EDIT;
                sortableTable = new SortableTable();
                sortableTable.removeAllRows();
//                sortableTable.getElement().setAttribute("style","border-bottom: 0.4px solid #DDDFE3");
                sortableTable.getElement().addClassName("timesheet-approval-table table table_listing  file--TimesheetApprovalVIewPanel");
                String projectName = "";
                int j = 2;
                int total = 0;
                for (final TimeSheetApprovalSingleItem singleItem : treeSet) {
                    sortableTable.getCellFormatter().setWordWrap(0, 0, true);

                    sortableTable.addColumnHeader(wfmStrings.taskName(), 0, null);
//                    sortableTable.getRowFormatter().getElement(1).setClassName("timesheet-approval-table__firstRow");
//                    sortableTable.getRowFormatter().getElement(2).setClassName("timesheet-approval-table__heading");
                    sortableTable.getRowFormatter().setStyleName(0, "timesheet-approval-table__firstRow  file--TimesheetApprovalVIewPanel");
                    sortableTable.getRowFormatter().setStyleName(1, "timesheet-approval-table__heading theader  file--TimesheetApprovalVIewPanel");
//                    sortableTable.getFlexCellFormatter().setStyleName(1, 0, "my-tbl-col tbl-hdr-gray");
                    sortableTable.setValue(j, 0, singleItem.getTaskName() != null ? singleItem.getTaskName() : "&nbsp;");

                    sortableTable.addColumnHeader(wfmStrings.description(), 1, null);
//                    sortableTable.getFlexCellFormatter().setStyleName(1, 1, "my-tbl-col tbl-hdr-gray");
                    sortableTable.setValue(j, 1, singleItem.getDescription() != null ? singleItem.getDescription() : "&nbsp;");

                    sortableTable.addColumnHeader(wfmStrings.comments(), 2, null);
//                    sortableTable.getFlexCellFormatter().setStyleName(1, 2, "my-tbl-col tbl-hdr-gray");
                    sortableTable.setValue(j, 2, singleItem.getComment() != null ? singleItem.getComment() : new HTML("&nbsp;").getHTML());

                    sortableTable.addColumnHeader(wfmStrings.date(), 3, null);
//                    sortableTable.getFlexCellFormatter().setStyleName(1, 3, "my-tbl-col tbl-hdr-gray");
                    sortableTable.setValue(j, 3, singleItem.getDate() != null ? DateUtils.format(singleItem.getDate().getNonConvertedDate()) : "&nbsp;");

                    sortableTable.addColumnHeader(wfmStrings.estimatedTime(), 4, null);
//                    sortableTable.getFlexCellFormatter().setStyleName(1, 4, "my-tbl-col tbl-hdr-gray");
                    sortableTable.setValue(j, 4, singleItem.getEstimatedTime());

                    sortableTable.addColumnHeader(TimesheetConstants.APPROVED.equals(appSingleItemList.getStatusCode()) ? wfmStrings.approved() : wfmStrings.waitingForApproval(), 5, null);
//                    sortableTable.getFlexCellFormatter().setStyleName(1, 5, "my-tbl-col tbl-hdr-gray");
                    sortableTable.setValue(j, 5, TimesheetConstants.APPROVED.equals(appSingleItemList.getStatusCode()) ? singleItem.getApprovedHours() : singleItem.getTimeSpent());

                    //sortableTable.addColumnHeader(timesheetStrings.waitingForApproval(), 6, null);
                    //sortableTable.getFlexCellFormatter().setStyleName(1, 6, "my-tbl-col tbl-hdr-gray");
                    //sortableTable.setValue(j, 6, singleItem.getTimeSpent());

                    if (isShowHourTypeDropDown) {
                        sortableTable.addColumnHeader(projectStrings.hourType(), 6, null);
//                        sortableTable.getFlexCellFormatter().setStyleName(1, 6, "my-tbl-col tbl-hdr-gray");
                        sortableTable.setValue(j, 6, singleItem.getHourType());
                    }
                    projectName = singleItem.getProjectName();

                    if (editable) {
                        final KpiCheckBox approve = new KpiCheckBox();
                        approve.setStyleName(debugID.concat("approve"));
                        final KpiCheckBox reject = new KpiCheckBox();
                        reject.setStyleName(debugID.concat("reject"));

                        approve.setName("action");
                        reject.setName("action");
                        if (singleItem.isApproved()) {
                            approve.setValue(singleItem.isApproved());
                        }
                        if (singleItem.isRejected()) {
                            reject.setValue(singleItem.isRejected());
                        }
                        approve.addClickHandler(clickEvent -> {
                            reject.setValue(false);
                            if (approve.getValue() && appSingleItemList.isTimesheetApprovalCommentRequired()) {
                                showApproveCommentPopup(singleItem, approve);
                            }
                        });
                        reject.addClickHandler(clickEvent -> {
                            approve.setValue(false);
                            if (appSingleItemList.isTimesheetApprovalCommentRequired()) {
                                showRejectCommentPopup(singleItem, reject);
                            }
                        });
//                        sortableTable.getFlexCellFormatter().setStyleName(1, 6 + columnOffset, "my-tbl-col tbl-hdr-gray");
                        sortableTable.setWidget(j, 6 + columnOffset, approve);
//                        sortableTable.getFlexCellFormatter().setStyleName(1, 7 + columnOffset, "my-tbl-col tbl-hdr-gray");
                        sortableTable.setWidget(j, 7 + columnOffset, reject);

                    }
                    total += singleItem.getTimeSpentInt();
                    timeSheetTimeEntries.put(j, singleItem);
                    j++;
                }

                if (editable) {
                    KpiCheckBox approvalAll = new KpiCheckBox("&nbsp;" + wfmStrings.approveAll(), true);
                    approvalAll.ensureDebugId(debugID + "approveAll");
                    approvalAll.addStyleName("timesheet_approve_all  file--TimesheetApprovalVIewPanel");

                    KpiCheckBox rejectAll = new KpiCheckBox("&nbsp;" + wfmStrings.rejectAll(), true);
                    rejectAll.ensureDebugId(debugID + "rejectAll");
                    rejectAll.addStyleName("timesheet_reject_all  file--TimesheetApprovalVIewPanel");
                    approvalAll.addClickHandler(clickEvent -> {
                        if (approvalAll.getValue() && appSingleItemList.isTimesheetApprovalCommentRequired()) {
                            showApproveCommentPopup(null, approvalAll);
                        }
                        rejectAll.setValue(false);
                        for (int ii = 2; ii < sortableTable.getRowCount(); ii++) {
                            KpiCheckBox approve_checkBox = (KpiCheckBox) sortableTable.getWidget(ii, 6 + columnOffset);
                            KpiCheckBox reject_checkBox = (KpiCheckBox) sortableTable.getWidget(ii, 7 + columnOffset);
                            reject_checkBox.setValue(false);
                            approve_checkBox.setValue(approvalAll.getValue());
                        }
                    });
                    rejectAll.addClickHandler(clickEvent -> {
                        approvalAll.setValue(false);
                        if (rejectAll.getValue() && appSingleItemList.isTimesheetApprovalCommentRequired()) {
                            showRejectCommentPopup(null, rejectAll);
                        }
                        for (int ii = 2; ii < sortableTable.getRowCount(); ii++) {
                            KpiCheckBox approve_checkBox = (KpiCheckBox) (sortableTable.getWidget(ii, 6 + columnOffset));
                            KpiCheckBox reject_checkBox = (KpiCheckBox) (sortableTable.getWidget(ii, 7 + columnOffset));
                            approve_checkBox.setValue(false);
                            reject_checkBox.setValue(rejectAll.getValue());
                        }
                    });
                    sortableTable.addColumnHeader("", treeSet.size() > 0 ? 6 + columnOffset : 0, approvalAll);
                    sortableTable.addColumnHeader("", treeSet.size() > 0 ? 7 + columnOffset : 1, rejectAll);
                }

//                sortableTable.setStyleName("rprt-rslt-te");
//                sortableTable.setWidget(0, 0, generateFields(editable, projectName, appSingleItemList));
//                sortableTable.getFlexCellFormatter().setWidth(0, 0, "100%");
//                sortableTable.getFlexCellFormatter().setColSpan(0, 0, editable ? 8 + columnOffset : 6 + columnOffset);

                MaterialPanel mainPanel = new MaterialPanel("timesheet-approval-new file--TimesheetApprovalVIewPanel");
                MaterialPanel tablePanel = new MaterialPanel("timesheet-approval-new__table  file--TimesheetApprovalVIewPanel");
                MaterialPanel footerPanel = new MaterialPanel("timesheet-approval-new__footer  file--TimesheetApprovalVIewPanel");

                Div employeeProjectTotalHours = new Div();
                HTML html = new HTML();
                String string = "EMPLOYEE NAME - " + appSingleItemList.getEmployeeName() + ", &emsp;";
                       string+="PROJECT № - " + projectName + "&emsp;";
                string += projectStrings.hoursSpentForAll() + " (" + wfmStrings.total() + ": " + Utils.formatMinutes(total) + ")";
                html.setHTML("<p>"+string+"</p>");
                html.getElement().getStyle().setLineHeight(40, Style.Unit.PX);
                html.getElement().setClassName("timesheet-approval-statusBar  file--TimesheetApprovalVIewPanel");

                employeeProjectTotalHours.add(html);

                tablePanel.add(sortableTable);
                tablePanel.add(employeeProjectTotalHours);

                mainPanel.add(tablePanel);
                mainPanel.add(createFooter());

                add(mainPanel);

                LoadingPanel.loading(false);
            }
        }));
    }

    private ViewFooter createFooter() {
        return new ViewFooter(new IFooteredView() {
            @Override
            public List<Widget> getFooterLeftSideWidgets() {
                return null;
            }

            @Override
            public List<Widget> getFooterRightSideWidgets() {
                return TimesheetApprovalViewPanel.this.getFooterRightSideWidgets();
            }
        });
    }

    private List<Widget> getFooterRightSideWidgets() {
        List<Widget> result = new ArrayList<>();
        Div proceedWrapper = new Div();
        proceedWrapper.add(proceed);
        result.add(proceedWrapper);
        return result;
    }

    private MaterialPanel generateFields(boolean editable, String projectName, TimeSheetApprovalSingleItemsList item) {
        MaterialPanel sectionBoxPanel = new MaterialPanel("section-box box-bg--1");
        MaterialPanel sectionBoxContentPanel = new MaterialPanel("section-box__content");
        GBox gBox = new GBox();
        GBoxRow groupBoxRow = new GBoxRow();

        GBoxItem employeeItem = new GBoxItem(wfmStrings.employee(), new Span(item.getEmployeeName()));
        employeeItem.setStyleNoBorder(true);
        groupBoxRow.add(employeeItem);

        GBoxItem projectItem = new GBoxItem(wfmStrings.projectName(), new Span(projectName));
        projectItem.setStyleNoBorder(true);
        groupBoxRow.add(projectItem);

        gBox.add(groupBoxRow);

        sectionBoxContentPanel.add(gBox);
        sectionBoxPanel.add(sectionBoxContentPanel);

        return sectionBoxPanel;
    }

    private void proceedI(Integer id) {
        proceed.setEnabled(false);
        if (!validate()) {
            proceed.setEnabled(true);
            return;
        }
        LoadingPanel.loading(true);
        TimeSheetApprovalSingleItemsList itemsList = new TimeSheetApprovalSingleItemsList();
        for (int i = 2; i < sortableTable.getRowCount(); i++) {
            if (sortableTable.getCellCount(i) > 6 + columnOffset) {
                if (((KpiCheckBox) sortableTable.getWidget(i, 6 + columnOffset)).getValue()) {
                    timeSheetTimeEntries.get(i).setApproved(true);
                } else if (((KpiCheckBox) sortableTable.getWidget(i, 7 + columnOffset)).getValue()) {
                    timeSheetTimeEntries.get(i).setApproved(false);
                }
            }
        }
        itemsList.setItems(timeSheetTimeEntries.values().toArray(new TimeSheetApprovalSingleItem[]{}));
        itemsList.setId(id);
        timesheetService.saveTimeSheetApprovalSessionListItem(itemsList, new AbstractAsyncCallback<Boolean>() {
            @Override
            public void failure(Throwable throwable) {
                proceed.setEnabled(true);
                LoadingPanel.loading(false);
                Info.show(wfmStrings.sorrySomethingWentWrong(), Info.Type.WARNING);
            }

            @Override
            public void success(Boolean aVoid) {
                proceed.setEnabled(true);
                LoadingPanel.loading(false);
                WfmMessageBox wfmMessageBox = new WfmMessageBox(IconEnum.CONFIRM, Action.OK);
                wfmMessageBox.setTitle(wfmStrings.information());
                wfmMessageBox.setMessage(Utils.textFormat(wfmStrings.messSuccessfullyUpdated(), wfmStrings.approvalProcess()));
                wfmMessageBox.open();
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_TIMESHEET_APPROVAL, aVoid, TimesheetApprovalViewPanel.this);
                WfmUiEventsBus.fireWfmUiEvent(WfmUiEventType.ON_NOTIFICATION_MSG_CHANGE_ENTITY, null, null);
                closeTab();
            }
        });
    }

    @Override
    public int compare(Object o1, Object o2) {
        TimeSheetApprovalSingleItem item1 = (TimeSheetApprovalSingleItem) o1;
        TimeSheetApprovalSingleItem item2 = (TimeSheetApprovalSingleItem) o2;
        if (item1.getDate().getNonConvertedDate().before(item2.getDate().getNonConvertedDate())) {
            return -1;
        } else {
            return 1;
        }
    }

    @Override
    public String getIconStyle() {
        return null;
    }

    private void showApproveCommentPopup(final TimeSheetApprovalSingleItem singleItem, final KpiCheckBox approve) {
        final KpiModal popup = new KpiModal();
        popup.setCloseButton(true);
        popup.setWidth(400);
        final TextArea2 commentArea = new TextArea2();
        commentArea.getTextArea().setVisibleLines(5);
        commentArea.setWidth("100%");
        VerticalPanel commentPanelDiv = new VerticalPanel();
        commentArea.getTextArea().addKeyPressHandler(event -> {
            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                if (singleItem != null) {
                    singleItem.setManagerApproveComment(commentArea.getText());
                } else { // this need for batch rejecting timesheet entries with same rejection comment
                    for (TimeSheetApprovalSingleItem item : timeSheetTimeEntries.values()) {
                        item.setManagerApproveComment(commentArea.getText());
                    }
                }
                popup.close();
            }
            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
                popup.close();
            }
        });

        commentPanelDiv.add(new Label(projectStrings.approvalComment()));
        commentPanelDiv.add(commentArea);
        WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveButton.addClickHandler(clickEvent -> {
            if (singleItem != null) {
                singleItem.setManagerApproveComment(commentArea.getText());
            } else { // this need for batch rejecting timesheet entries with same rejection comment
                for (TimeSheetApprovalSingleItem item : timeSheetTimeEntries.values()) {
                    item.setManagerApproveComment(commentArea.getText());
                }
            }
            popup.close();
        });
        popup.add(commentPanelDiv);
        popup.addButton(saveButton);
        popup.open();
    }

    private void showRejectCommentPopup(final TimeSheetApprovalSingleItem singleItem, final KpiCheckBox reject) {
        final KpiModal popup = new KpiModal();
        popup.setCloseButton(true);
        popup.setWidth(400);
        final TextArea2 commentArea = new TextArea2();
        commentArea.getTextArea().setVisibleLines(5);
        commentArea.setWidth("100%");
        VerticalPanel commentPanelDiv = new VerticalPanel();
        commentArea.getTextArea().addKeyPressHandler(event -> {
            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
                if (singleItem != null) {
                    singleItem.setManagerApproveComment(commentArea.getText());
                } else { // this need for batch rejecting timesheet entries with same rejection comment
                    for (TimeSheetApprovalSingleItem item : timeSheetTimeEntries.values()) {
                        item.setManagerApproveComment(commentArea.getText());
                    }
                }
                popup.close();
            }
            if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
                popup.close();
            }
        });

        commentPanelDiv.add(new Label(wfmStrings.rejectionReason()));
        commentPanelDiv.add(commentArea);
        WfmButton2 saveButton = new WfmButton2(wfmStrings.save(), WfmButton2.BTN_PRIMARY);
        saveButton.addClickHandler(clickEvent -> {
            if (singleItem != null) {
                singleItem.setManagerApproveComment(commentArea.getText());
            } else { // this need for batch rejecting timesheet entries with same rejection comment
                for (TimeSheetApprovalSingleItem item : timeSheetTimeEntries.values()) {
                    item.setManagerApproveComment(commentArea.getText());
                }
            }
            popup.close();
        });
        popup.add(commentPanelDiv);
        popup.addButton(saveButton);
        popup.open();
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
    public String getPropertyCode() {
        return TIMESHEET_APPROVAL_LIST;
    }
}
