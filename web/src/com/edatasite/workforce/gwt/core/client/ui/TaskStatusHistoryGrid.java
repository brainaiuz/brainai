package com.edatasite.workforce.gwt.core.client.ui;

import com.edatasite.workforce.gwt.contact.client.ui.AbstractDataGrid;
import com.edatasite.workforce.gwt.core.client.DateUtils;
import com.edatasite.workforce.gwt.core.client.rpc.LogHistoryItem;
import com.edatasite.workforce.gwt.task.client.rpc.TaskService;
import com.google.gwt.cell.client.SafeHtmlCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class TaskStatusHistoryGrid extends AbstractDataGrid<LogHistoryItem> implements Constants {

    protected Integer objectId;

    public TaskStatusHistoryGrid(Integer objectId) {
        super();
        this.objectId = objectId;
        initialize();
    }

    @Override
    protected void addColums() {
        Column<LogHistoryItem, SafeHtml> status = new Column<LogHistoryItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(final LogHistoryItem item) {
                return () -> "<span>" + (item.getStatus() != null ? item.getStatus() : wfmStrings.notAvailable()) + "</span>";
            }
        };
        addColumn(status, wfmStrings.status());
        setColumnWidth(status, "400px");

        Column<LogHistoryItem, SafeHtml> comment = new Column<LogHistoryItem, SafeHtml>(new SafeHtmlCell()) {
            @Override
            public SafeHtml getValue(final LogHistoryItem item) {
                return () -> "<span>" + (item.getComment() != null ? item.getComment() : wfmStrings.notAvailable()) + "</span>";
            }
        };
        addColumn(comment, wfmStrings.comment());
        setColumnWidth(comment, "500px");

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
        TaskService.App.get().getAllStatusHistories(objectId, new AsyncCallback<LogHistoryItem[]>() {

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
