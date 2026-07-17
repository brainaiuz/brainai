package com.finnetlimited.reportservice.core.client.gwtrpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: nodir
 * Date: 05.08.2010
 * Time: 16:18:54
 * To change this template use File | Settings | File Templates.
 */
public interface StatusServiceAsync {
    void getGoogleMarketPlaceUsersFirstTime(AsyncCallback<GoogleMarketPlaceUser> callback);

    void getGoogleMarketPlaceUsers(AsyncCallback<GoogleMarketPlaceUser> callback);

    void saveEmployees(GoogleMarketPlaceUser employees, boolean showPopup, AsyncCallback<Integer[]> callback);

    void getCountries(AsyncCallback<ArrayList<SelectItem>> callback);
}
