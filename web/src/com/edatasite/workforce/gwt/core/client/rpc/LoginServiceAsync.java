package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.Exceptions.IncorrectPasswordException;
import com.edatasite.workforce.gwt.core.client.Exceptions.UserNotFoundException;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by IntelliJ IDEA.
 * User: iskan
 * Date: Dec 25, 2007
 * Time: 4:00:45 PM
 * To change this template use File | Settings | File Templates.
 */

public interface LoginServiceAsync {

    void hideLandingPage(boolean checked, String viewName, AsyncCallback async);

    void getCompanyLogoURL1(AsyncCallback<String> async);

    void getCompanyLogoURL(String logoType, AsyncCallback<String> async);

    void updateAccount(AccountItem account, AsyncCallback<String[]> async);

    void getAccount(AsyncCallback<AccountItem> async);

    void getAuthSubURL(String baseURL, String authType, AsyncCallback<String> async);

    void resetCompanyOrPdfLogo(String logoType, AsyncCallback<Boolean> callback);

    void getRolesAsIntegersString(AsyncCallback<String> async);

    void isValid_User_For_Google_Gocs(AsyncCallback<Boolean> callback);

    void setTimeZone(String sessionId, String sessionTrackId, Integer i, AsyncCallback<Void> asyncCallback);

    void getUserSettings(AsyncCallback<ArrayList<KeyValueStruct>> asyncCallback);

    void login(String userName, String password, String userAgent, Integer companyID, String IPAddress, AsyncCallback<String> async) throws UserNotFoundException,
            IncorrectPasswordException;

    void getCompanyLogo(String logoType, AsyncCallback<SelectItem> async);

    void getMoreMenuSettings(String actionName, AsyncCallback<String> requestADemo);

    void isValidUserOfficeAndGoogle(String storageType, AsyncCallback<ArrayList<Boolean>> callback);

    void getUserCompanyList(AsyncCallback<LinkedHashMap<String, ArrayList<UserCompanyDTO>>> callback);
}
