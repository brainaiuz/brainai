package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: Khasan
 * Date: 09.04.14
 * Time: 19:49
 * To change this template use File | Settings | File Templates.
 */
public interface ClockWidgetService extends RemoteService {

    Integer startTimer(ClockItem item);

    ClockItem getClockItem(Integer busObjectId, Integer type, DateNonConvertable clientsCurrentDate);

    Integer[] applyTime(ClockItem item);

    void stopTimer(ClockItem item);

    ClockItem getProjectTasks(Integer projectID, Integer type, DateNonConvertable clientsCurrentDate);

    ClockItem getHistoryClockItem();

    ClockItem getHistoryMultiClockItem(Integer type);

    boolean getPMNumberingSettings();

    ArrayList<ClockItem> getCurrentUserEntriesByDay(DateNonConvertable date);

    class App {
        public static ClockWidgetServiceAsync get() {
            ServiceDefTarget target = GWT.create(CoreGenericService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/clockWidget");
            return (ClockWidgetServiceAsync) target;
        }
    }
}
