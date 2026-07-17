package com.edatasite.workforce.gwt.crm.client.ui.view.widgets;

import com.edatasite.workforce.gwt.contact.client.ui.AbstractDataGrid;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.LogHistoryItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TaskLogHistoryGrid extends AbstractDataGrid<LogHistoryItem> implements Constants {

    private final Integer taskID;

    public TaskLogHistoryGrid(Integer taskID) {
        super();
        this.taskID = taskID;
        initialize();
    }

    @Override
    protected void addColums() {
        addColumn(new Column<LogHistoryItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(final LogHistoryItem item) {
                return () -> "<span>" + (item.getFieldName() != null ? item.getFieldName() : wfmStrings.notAvailable()) + "</span>";
            }
        }, wfmStrings.fieldName());

        addColumn(new Column<LogHistoryItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(final LogHistoryItem item) {
                return () -> "<span>" + (item.getOldValue() != null ? item.getOldValue() : wfmStrings.notAvailable()) + "</span>";
            }
        }, wfmStrings.oldValue());

        addColumn(new Column<LogHistoryItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(final LogHistoryItem item) {
                return () -> "<span>" + (item.getNewValue() != null ? item.getNewValue() : wfmStrings.notAvailable()) + "</span>";
            }
        }, wfmStrings.newValue());

        addColumn(new Column<LogHistoryItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(final LogHistoryItem item) {
                return () -> "<span>" + (item.getModifiedDate() != null ? DateUtils.formatInternal(item.getModifiedDate()) : wfmStrings.notAvailable()) + "</span>";
            }
        }, wfmStrings.modifiedDate());

        addColumn(new Column<LogHistoryItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(final LogHistoryItem item) {
                return () -> "<span>" + (item.getModifier() != null ? item.getModifier() : wfmStrings.notAvailable()) + "</span>";
            }
        }, wfmStrings.modifiedBy());
    }

    @Override
    public void refresher() {
        TaskService.App.get().getAllLogHistories(taskID, new AsyncCallback<LogHistoryItem[]>() {

            @Override
            public void onFailure(Throwable throwable) {

            }

            @Override
            public void onSuccess(LogHistoryItem[] result) {
                if (result.length > 0) {
                    supplyProvider(result);
                    reDrawItems();
                }
            }
        });
    }
}
