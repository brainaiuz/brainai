package com.edatasite.workforce.gwt.core.client.ui.listpanel;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.google.gwt.gen2.table.client.TableModel;
import com.google.gwt.gen2.table.client.TableModelHelper;


/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 24-Aug-2010
 * Time: 18:52:06
 */
public class ListingCallback<T> {

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
    public void onFailure(Throwable throwable) {

    }

    // This methods write overrides
    public void onSuccess(ListResult<T> data) {

    }
}
