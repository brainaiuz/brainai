package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Khasan
 * Date: 09.04.14
 * Time: 19:51
 * To change this template use File | Settings | File Templates.
 */
public interface ClockWidgetServiceAsync {

    void startTimer(ClockItem item, AsyncCallback<Integer> callback);

    void getClockItem(Integer busObjectId, Integer type, DateNonConvertable clientsCurrentDate, AsyncCallback<ClockItem> callback);

    void stopTimer(ClockItem item, AsyncCallback<Void> callback);

    void applyTime(ClockItem item, AsyncCallback<Integer[]> callback);

    void getHistoryClockItem(AsyncCallback<ClockItem> asyncCallback);

    void getProjectTasks(Integer projectID, Integer type, DateNonConvertable clientsCurrentDate, AsyncCallback<ClockItem> callback);

    void getHistoryMultiClockItem(Integer newType, AsyncCallback<ClockItem> callback);

    void getPMNumberingSettings(AsyncCallback<Boolean> callback);

    void getCurrentUserEntriesByDay(DateNonConvertable date, AsyncCallback<ArrayList<ClockItem>> callback);

}
