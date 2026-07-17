package com.edatasite.workforce.gwt.workstream.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.project.WbsItem;
import com.edatasite.workforce.gwt.workstream.client.ui.WbscopyItem;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;

public interface WbsServiceAsync {

    void getItems(ListingFilterParameter filterParameter, AsyncCallback<WbsItem[]> callback);

    void getSubItems(ListingFilterParameter filterParameter, AsyncCallback<WbsItem[]> callback);

    void getFirstLevelWorkstreams(Integer projectId, AsyncCallback<WbsItem[]> callback);

    void getSubWorkStreams(Integer workStreamId, AsyncCallback<WbsItem[]> callback);

    void getFirstLevelWorkstreams(Integer projectId, Integer workStreamID, AsyncCallback<WbsItem> callback);

    void getWorkStreamList(Integer projectID, AsyncCallback<ArrayList<WbsItem>> async);

    void getSelectCompilitedStatus(Integer projectId, AsyncCallback<WbsItem> async);

    void copyWorkstreamToOtherProject(WbscopyItem item, AsyncCallback<Void> callback);


}
