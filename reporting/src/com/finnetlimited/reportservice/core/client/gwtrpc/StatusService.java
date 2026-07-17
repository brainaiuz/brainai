package com.finnetlimited.reportservice.core.client.gwtrpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.finnetlimited.reportservice.core.client.exceptions.GoogleAppsException;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: nodir
 * Date: 05.08.2010
 * Time: 16:10:40
 * To change this template use File | Settings | File Templates.
 */
public interface StatusService extends RemoteService {
    GoogleMarketPlaceUser getGoogleMarketPlaceUsersFirstTime() throws GoogleAppsException;

    GoogleMarketPlaceUser getGoogleMarketPlaceUsers() throws GoogleAppsException;

    Integer[] saveEmployees(GoogleMarketPlaceUser employees, boolean showPopup);

    ArrayList<SelectItem> getCountries();

    class App {
        public static StatusServiceAsync get() {
            ServiceDefTarget target = GWT.create(StatusService.class);
            target.setServiceEntryPoint(GWT.getHostPageBaseURL() + "gwtrpc/statusService");
            return (StatusServiceAsync) target;
        }
    }
}
