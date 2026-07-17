package com.edatasite.workforce.gwt.crm.client.rpc;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: Jan 11, 2011
 * Time: 10:55:42 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GoogleAnalyticsServiceAsync {

    void getUserTeamName(AsyncCallback<String> callback);

    void validateCurrentUser(AsyncCallback<Boolean> callback);

    void saveToken(String token, AsyncCallback callback);

    void deleteGoogleAnalyticsToken(AsyncCallback async);
}
