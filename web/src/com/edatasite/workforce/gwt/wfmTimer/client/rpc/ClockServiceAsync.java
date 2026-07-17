package com.edatasite.workforce.gwt.wfmTimer.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.ClockItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Aug 11, 2010
 * Time: 7:15:29 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ClockServiceAsync {

    void startTimer(ClockItem item, AsyncCallback<Void> callback);

    void getClockItem(Integer busObjectId, Integer type, DateNonConvertable clientsCurrentDate, AsyncCallback<ClockItem> callback);

    void getCaseClocks(Integer busObjectId, Integer type, AsyncCallback<ListResult<ClockItem>> calback);

    void applyTime(ClockItem item, AsyncCallback<Integer[]> callback);

    void stopTimer(ClockItem item, AsyncCallback<Void> callback);

    void getProjectTasks(Integer projectID, Integer type, DateNonConvertable clientsCurrentDate, AsyncCallback<ClockItem> callback);
}
