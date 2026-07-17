package com.edatasite.workforce.gwt.wfmTimer.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.ClockItem;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Aug 11, 2010
 * Time: 7:15:29 PM
 * To change this template use File | Settings | File Templates.
 */

public interface ClockService extends RemoteService {

    void startTimer(ClockItem item);

    ClockItem getClockItem(Integer busObjectId, Integer type, DateNonConvertable clientsCurrentDate);

    ListResult<ClockItem> getCaseClocks(Integer busObjectId, Integer type);

    Integer[] applyTime(ClockItem item);

    void stopTimer(ClockItem item);

    ClockItem getProjectTasks(Integer projectID, Integer type, DateNonConvertable clientsCurrentDate);

    class App {
        public static ClockServiceAsync get() {
            ServiceDefTarget target = GWT.create(ClockService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/clock");
            return (ClockServiceAsync) target;
        }
    }
}
