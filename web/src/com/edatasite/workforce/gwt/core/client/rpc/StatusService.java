package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.Exceptions.GoogleAppsException;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.employee.GoogleMarketPlaceUser;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public interface StatusService extends RemoteService {

    String setUserStatus(String changeStatusCode, boolean timeSpentRequared, boolean logout);

    String setUserStatus(Integer employeeId, String changeStatusCode, boolean timeSpentRequared);

    GoogleMarketPlaceUser getGoogleMarketPlaceUsersFirstTime() throws GoogleAppsException;

    Integer[] saveEmployees(GoogleMarketPlaceUser employees, boolean showPopup);

    GoogleMarketPlaceUser getGoogleMarketPlaceUsers() throws GoogleAppsException;

    String getLatestServerUploadVersion();

    SwitchvoxContactItem getIncomingCallerID();

    Boolean refreshSession(String sessionId, String password);


    void getDateAndSetEndDate(DateNonConvertable nonConvertable);

    void insertUserRequest(String methodName);

    class App {
        public static StatusServiceAsync get() {
            ServiceDefTarget target = GWT.create(CoreGenericService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/status");
            return (StatusServiceAsync) target;
        }
    }

}
