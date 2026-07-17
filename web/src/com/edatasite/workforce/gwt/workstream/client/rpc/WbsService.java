package com.edatasite.workforce.gwt.workstream.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.project.WbsItem;
import com.edatasite.workforce.gwt.workstream.client.ui.WbscopyItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;

public interface WbsService extends RemoteService {

    WbsItem[] getItems(ListingFilterParameter filterParameter);

    WbsItem[] getSubItems(ListingFilterParameter filterParameter);

    WbsItem[] getFirstLevelWorkstreams(Integer projectId);

    WbsItem getFirstLevelWorkstreams(Integer projectId, Integer workStreamID);

    WbsItem[] getSubWorkStreams(Integer workStreamId);

    ArrayList<WbsItem> getWorkStreamList(Integer projectID);

    WbsItem getSelectCompilitedStatus(Integer projectID);

    void copyWorkstreamToOtherProject(WbscopyItem item);

    class App {
        public static WbsServiceAsync get() {
            ServiceDefTarget target = GWT.create(WbsService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/wbs");
            return (WbsServiceAsync) target;
        }
    }

}
