package com.edatasite.workforce.gwt.dashboard.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface DashboardServiceAsync {

    void lastEnteredDate(AsyncCallback<Void> async);

    void lastEnteredDate(Integer yearDifference, AsyncCallback<Void> async);

    void lastEnteredDate(Integer yearDifference, Integer companyID, AsyncCallback<Void> async);

}