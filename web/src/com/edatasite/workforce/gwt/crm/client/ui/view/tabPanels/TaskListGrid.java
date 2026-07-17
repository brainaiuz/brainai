package com.edatasite.workforce.gwt.crm.client.ui.view.tabPanels;

import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.CompanyConstants;
import com.edatasite.workforce.gwt.core.client.ui.cell.SimpleLinkCell;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventType;
import com.edatasite.workforce.gwt.core.client.ui.eventHandler.WfmUiEventsBus;
import com.edatasite.workforce.gwt.core.client.ui.factory.SinksContainerFactory;
import com.edatasite.workforce.gwt.crm.client.rpc.ActivityItem;
import com.edatasite.workforce.gwt.crm.client.rpc.CRMService;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.Column;

/**
 * User: Hayot
 * Date: Apr 10, 2010
 * Time: 4:49:32 PM
 */
public class TaskListGrid extends CrmActivityGrid {

    public TaskListGrid(Integer entityID, String entityType) {
        super(entityID, entityType);
    }

    @Override
    protected void initUiEventListeners() {
        WfmUiEventsBus.addWfmUiListener(WfmUiEventType.ON_TASK_ADD, TaskListGrid.this, (sender, args) -> refresher());
    }

    @Override
    protected void addColums() {
        //subject
        Column<ActivityItem, String> subject = new Column<ActivityItem, String>(new SimpleLinkCell()) {
            @Override
            public String getValue(final ActivityItem item) {
                return item.getSubject();
            }
        };
        subject.setFieldUpdater((i, item, s) -> SinksContainerFactory.entryPoint.onHistoryChanged("task|summary/" + item.getTaskObjectId()));
        addColumn(subject, CompanyConstants.C24899.equals(Utils.getEncryptedCompanyID()) ? wfmStrings.category() : wfmStrings.subject());
        setColumnWidth(subject, 40, com.google.gwt.dom.client.Style.Unit.PCT);

        //assignees
        Column<ActivityItem, SafeHtml> assignees = new Column<ActivityItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(final ActivityItem item) {
                return () -> "<span>" + (item.getAssignee() != null ? item.getAssignee() : wfmStrings.notAvailable()) + "</span>";
            }
        };
        addColumn(assignees, wfmStrings.assignees());
        setColumnWidth(assignees, 30, com.google.gwt.dom.client.Style.Unit.PCT);

        //startDateField
        Column<ActivityItem, SafeHtml> startDateField = new Column<ActivityItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(final ActivityItem item) {
                return () -> "<span>" + (DateUtils.formatInternal(item.getStartDate())) + "</span>";
            }
        };
        addColumn(startDateField, wfmStrings.startDate());
        setColumnWidth(startDateField, 20, com.google.gwt.dom.client.Style.Unit.PCT);

        //timeSpentOnly
        Column<ActivityItem, SafeHtml> timeSpentOnly = new Column<ActivityItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(final ActivityItem item) {
                return () -> "<span>" + (Utils.formatMinutes(item.getTimeSpent())) + "</span>";
            }
        };
        addColumn(timeSpentOnly, wfmStrings.timeSpentOnly());
        setColumnWidth(timeSpentOnly, 10, com.google.gwt.dom.client.Style.Unit.PCT);
    }

    @Override
    public void refresher() {
        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setRelationID(entityID);
        fp.setRelationType(entityType);
        CRMService.App.get().getTaskList(fp, new AbstractAsyncCallback<ListResult<ActivityItem>>() {
            public void failure(Throwable caught) {
            }

            public void success(final ListResult<ActivityItem> activityList) {
                Scheduler.get().scheduleDeferred(() -> {
                    supplyProvider(activityList.getList().toArray(new ActivityItem[]{}));
                    reDrawItems();
                });
            }
        });
    }
}