package com.edatasite.workforce.gwt.dashboard.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public interface DashboardService extends RemoteService {

    void lastEnteredDate();

    void lastEnteredDate(Integer yearDifference);

    void lastEnteredDate(Integer yearDifference, Integer companyID);

    class App {
        public static DashboardServiceAsync get() {
            ServiceDefTarget target = GWT.create(DashboardService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/dashboard");
            return (DashboardServiceAsync) target;
        }
    }
}
