package com.edatasite.workforce.gwt.task.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;

import java.util.ArrayList;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 02-Sep-2010
 * Time: 21:27:49
 */
public class TaskList extends ListResult<TaskListItem> {

    private SelectItem[] items;
    private Boolean atLeastOneTimerIsRunning;

    public TaskList() {

    }

    public TaskList(ArrayList<TaskListItem> list, Integer total) {
        super(list, total);
    }

    public TaskList(ArrayList<TaskListItem> listItems, int totalCount, SelectItem[] items, Boolean atLeastOneTimerIsRunning) {
        super(listItems, totalCount);
        this.items = items;
        this.atLeastOneTimerIsRunning = atLeastOneTimerIsRunning;
    }

    public SelectItem[] getItems() {
        return items;
    }

    public void setItems(SelectItem[] items) {
        this.items = items;
    }

    public ArrayList<Integer> getObjectIDs() {
        ArrayList<Integer> ids = new ArrayList<>();
        if (getList() != null && getList().size() > 0) {
            for (TaskListItem item : getList()) {
                ids.add(item.getObjectID());
            }
        }
        return ids;
    }

    public Boolean getAtLeastOneTimerIsRunning() {
        return atLeastOneTimerIsRunning;
    }

    public void setAtLeastOneTimerIsRunning(Boolean atLeastOneTimerIsRunning) {
        this.atLeastOneTimerIsRunning = atLeastOneTimerIsRunning;
    }
}
