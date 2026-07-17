package com.edatasite.workforce.gwt.core.client.ui.listpanel;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.ui.treetable.TreeItem;
import com.google.gwt.gen2.table.client.TableModel;
import com.google.gwt.gen2.table.client.TableModelHelper;

/**
 * Created by IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 7/31/12
 * Time: 5:09 AM
 * To change this template use File | Settings | File Templates.
 */
public class TreeListingCallBack<T> {

    private TableModel.Callback<T> callback;
    private TableModelHelper.Request request;

    public TableModel.Callback<T> getCallback() {
        return callback;
    }

    public void setCallback(TableModel.Callback<T> callback) {
        this.callback = callback;
    }

    public TableModelHelper.Request getRequest() {
        return request;
    }

    public void setRequest(TableModelHelper.Request request) {
        this.request = request;
    }

    // This methods write overrides
    public void onFailure(Throwable throwable, TreeItem item) {

    }

    // This methods write overrides
    public void onSuccess(ListResult<T> data, TreeItem item) {

    }
}
