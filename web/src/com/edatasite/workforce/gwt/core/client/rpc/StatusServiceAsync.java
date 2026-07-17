package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.employee.GoogleMarketPlaceUser;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface StatusServiceAsync {

    void setUserStatus(String changeStatusCode, boolean timeSpentRequared, boolean logout, AsyncCallback<String> callback);

    void setUserStatus(Integer employeeId, String changeStatusCode, boolean timeSpentRequared, AsyncCallback<String> callback);

    void getGoogleMarketPlaceUsersFirstTime(AsyncCallback<GoogleMarketPlaceUser> callback);

    void saveEmployees(GoogleMarketPlaceUser employees, boolean showPopup, AsyncCallback<Integer[]> callback);

    void getGoogleMarketPlaceUsers(AsyncCallback<GoogleMarketPlaceUser> callback);

    void getLatestServerUploadVersion(AsyncCallback<String> callback);

    void getIncomingCallerID(AsyncCallback<SwitchvoxContactItem> callback);

    void refreshSession(String sessionId, String password, AsyncCallback<Boolean> callback);


    void getDateAndSetEndDate(DateNonConvertable nonConvertable, AsyncCallback<Void> callback);

    void insertUserRequest(String methodname, AsyncCallback<Void> callback);
}