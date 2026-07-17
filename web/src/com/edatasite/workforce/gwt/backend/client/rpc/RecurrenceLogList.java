package com.edatasite.workforce.gwt.backend.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 30.03.11
 * Time: 18:51
 * To change this template use File | Settings | File Templates.
 */
public class RecurrenceLogList extends ListResult<RecurrenceLogItem> {

    private int totalCount;
    private SelectItem[] items;

    public RecurrenceLogList() {

    }

    public RecurrenceLogList(ArrayList<RecurrenceLogItem> recurrenceLogItems, int totalCount) {
        super(recurrenceLogItems, totalCount);
        this.items = items;
    }

    public SelectItem[] getItems() {
        return items;
    }

    public void setItems(SelectItem[] items) {
        this.items = items;
    }
}
