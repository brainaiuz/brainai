package com.edatasite.workforce.gwt.backend.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Nov 6, 2010
 * Time: 4:51:17 PM
 * To change this template use File | Settings | File Templates.
 */
public class SchemaList extends ListResult<SchemaListItem> {

    private SelectItem[] items;

    public SchemaList() {

    }

    public SchemaList(ArrayList<SchemaListItem> listItems, int totalCount, SelectItem[] items) {
        super(listItems, totalCount);
        this.items = items;
    }

    public SelectItem[] getItems() {
        return items;
    }

    public void setItems(SelectItem[] items) {
        this.items = items;
    }
}
