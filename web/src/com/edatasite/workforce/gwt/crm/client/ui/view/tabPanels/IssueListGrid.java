package com.edatasite.workforce.gwt.crm.client.ui.view.tabPanels;

import com.edatasite.workforce.gwt.contact.client.ui.AbstractDataGrid;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.localization.WfmStrings;
import com.edatasite.workforce.gwt.core.client.rpc.AbstractAsyncCallback;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.edatasite.workforce.gwt.task.client.rpc.TaskTimeEntriesItem;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.user.cellview.client.Column;

/**
 * User: Fatxulla
 * Date: Feb 21, 2014
 * Time: 4:49:32 PM
 */
public class IssueListGrid extends AbstractDataGrid<TaskTimeEntriesItem> implements Constants {

    protected static final WfmStrings wfmStrings = WfmStrings.App.get();
    protected Integer entityID;

    public IssueListGrid(Integer entityID) {
        super();
        this.entityID = entityID;
        initialize();
    }


    @Override
    protected void addColums() {
        //owner
        Column<TaskTimeEntriesItem, String> owner = new Column<TaskTimeEntriesItem, String>(new TextCell()) {
            @Override
            public String getValue(final TaskTimeEntriesItem item) {
                return item.getEmloyee() != null ? item.getEmloyee() : "";
            }
        };
        addColumn(owner, wfmStrings.employee());
        setColumnWidth(owner, 40, com.google.gwt.dom.client.Style.Unit.PCT);

        //subject
        Column<TaskTimeEntriesItem, String> subject = new Column<TaskTimeEntriesItem, String>(new TextCell()) {
            @Override
            public String getValue(final TaskTimeEntriesItem item) {
                return item.getComment() != null ? item.getComment() : "";
            }
        };
        addColumn(subject, wfmStrings.comment());
        setColumnWidth(subject, 40, com.google.gwt.dom.client.Style.Unit.PCT);

        //time spend
        Column<TaskTimeEntriesItem, String> spend = new Column<TaskTimeEntriesItem, String>(new TextCell()) {
            @Override
            public String getValue(final TaskTimeEntriesItem item) {
                return item.getTimeSpent() != null ? Utils.formatMinutes(item.getTimeSpent()) : "";
            }
        };
        addColumn(spend, wfmStrings.timeSpentOnly());
        setColumnWidth(spend, 40, com.google.gwt.dom.client.Style.Unit.PCT);

        //date
        Column<TaskTimeEntriesItem, String> date = new Column<TaskTimeEntriesItem, String>(new TextCell()) {
            @Override
            public String getValue(final TaskTimeEntriesItem item) {
                return  item.getDate() != null ? DateUtils.format(item.getDate().getNonConvertedDate()) : "";
            }
        };
        addColumn(subject, wfmStrings.date());
        setColumnWidth(subject, 40, com.google.gwt.dom.client.Style.Unit.PCT);


    }

    @Override
    public void refresher() {
        TaskService.App.get().getTaskTimeEntries(entityID, new AbstractAsyncCallback<TaskTimeEntriesItem[]>() {
            @Override
            public void failure(Throwable throwable) {
            }

            @Override
            public void success(TaskTimeEntriesItem[] result) {
                Scheduler.get().scheduleDeferred(() -> {
                    supplyProvider(result);
                    reDrawItems();
                });
            }
        });
    }
}