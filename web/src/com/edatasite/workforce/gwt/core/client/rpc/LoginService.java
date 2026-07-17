package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.core.client.Exceptions.IncorrectPasswordException;
import com.edatasite.workforce.gwt.core.client.Exceptions.UserNotFoundException;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * Created by IntelliJ IDEA. User: iskan Date: Dec 25, 2007 Time: 4:00:15 PM To
 * change this template use File | Settings | File Templates.
 */


public interface LoginService extends RemoteService {

    String NAME = "loginService";

    void hideLandingPage(boolean checked, String viewName);

    String getCompanyLogoURL1();

    String getCompanyLogoURL(String logoType);

    SelectItem getCompanyLogo(String logoType);

    String getAuthSubURL(String baseURL, String authType);

    boolean resetCompanyOrPdfLogo(String logoType);

    String[] updateAccount(AccountItem account);

    AccountItem getAccount();

    String getRolesAsIntegersString();

    Boolean isValid_User_For_Google_Gocs();

    void setTimeZone(String sessionId, String moduleLoadTrackId, Integer i);

    //Do not delete (For mobile)

    ArrayList<KeyValueStruct> getUserSettings();

    String login(String userName, String password, String userAgent, Integer companyID, String IPAddress) throws UserNotFoundException, IncorrectPasswordException;

    String getMoreMenuSettings(String actionName);

    ArrayList<Boolean> isValidUserOfficeAndGoogle(String StorageType);

    LinkedHashMap<String, ArrayList<UserCompanyDTO>> getUserCompanyList();

    class App {
        public static LoginServiceAsync get() {
            ServiceDefTarget target = GWT.create(CoreGenericService.class);
            target.setServiceEntryPoint(Utils.getHostNameURL() + "rpc/login");
            return (LoginServiceAsync) target;
        }
    }

}
