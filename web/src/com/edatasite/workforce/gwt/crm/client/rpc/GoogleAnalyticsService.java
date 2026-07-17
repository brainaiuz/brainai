package com.edatasite.workforce.gwt.crm.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Jan 11, 2011
 * Time: 10:53:13 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GoogleAnalyticsService extends RemoteService {

    String getUserTeamName();
    boolean validateCurrentUser();
    void saveToken(String token) throws Exception;
    void deleteGoogleAnalyticsToken();

    class App {
        public static GoogleAnalyticsServiceAsync get() {
            ServiceDefTarget target = GWT.create(GoogleAnalyticsService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/googleanalytics");
            return (GoogleAnalyticsServiceAsync) target;
        }
    }
}