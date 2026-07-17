package com.edatasite.workforce.gwt.client.client.rpc.supplier;

import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.ListData;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Apr 11, 2009
 * Time: 8:11:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class SupplierList extends ListResult<CrmAccountItem> {

    public SupplierList() {
    }

    public SupplierList(ArrayList<CrmAccountItem> suppliers, Integer total) {
        super(suppliers, total);
    }

    public ListData getListData() {
        return new ListData(getList().toArray(new CrmAccountItem[]{}), getTotal());
    }

//    private SupplierListItem[] results;
//    private SupplierSingleItem[] items;
//    private int totalCount;
//    private String params;
//
//    public SupplierList() {
//    }
//
//    public SupplierList(SupplierListItem[] results, int totalCount) {
//        this.results = results;
//        this.totalCount = totalCount;
//    }
//
//    public SupplierListItem[] getResults() {
//        return results;
//    }
//
//    public SupplierSingleItem[] getItems() {
//        return items;
//    }
//
//    public void setItems(SupplierSingleItem[] items) {
//        this.items = items;
//    }
//
//    public void setResults(SupplierListItem[] results) {
//        this.results = results;
//    }
//
//    public int getTotalCount() {
//        return totalCount;
//    }
//
//    public void setTotalCount(int totalCount) {
//        this.totalCount = totalCount;
//    }
//
//    public ListData getListData() {
//        return new ListData(results, totalCount);
//    }
//
//    public ListData getListItemsData() {
//        return new ListData(items, totalCount);
//    }
//
//    public String getParams() {
//        return params;
//    }
//
//    public void setParams(String params) {
//        this.params = params;
//    }
}
