package com.workforcetrack.mobile.services;

import com.edatasite.workforce.gwt.core.client.Exceptions.IncorrectPasswordException;
import com.edatasite.workforce.gwt.core.client.Exceptions.UserNotFoundException;
import com.edatasite.workforce.rest.base.exception.UserNotActivatedException;
import com.workforcetrack.mobile.rpc.login.MCompanyList;
import com.workforcetrack.mobile.rpc.login.MUserCompanyDTO;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 5/24/11
 * Time: 4:36 PM
 * To change this template use File | Settings | File Templates.
 */
public interface LoginWebService {

    MCompanyList getUserCompanies(String userName, String password, String userAgent, String hostUrl) throws UserNotFoundException,
            IncorrectPasswordException;

    MCompanyList getLoginedUserCompanies(String userName, String hostUrl) throws UserNotFoundException,
            IncorrectPasswordException;

    MUserCompanyDTO lightLogin(String userName, String password, String userAgent, Integer companyID, String hostUrl) throws UserNotFoundException,
            IncorrectPasswordException, UserNotActivatedException;


    MUserCompanyDTO mobileLogin(String userName, String password, String userAgent, Integer companyID, String hostUrl) throws UserNotFoundException,
            IncorrectPasswordException, UserNotActivatedException;

    MUserCompanyDTO mobileLogin(String userName, String password, String userAgent, Integer companyID, String deviceToken, String deviceType, String hostUrl) throws UserNotFoundException,
            IncorrectPasswordException, UserNotActivatedException;

    String getSessionID(Integer companyID);

    MUserCompanyDTO getMainParams();

    MUserCompanyDTO switchCompany(Integer companyId, String hostUrl, String userAgent);

    void setUserDeviceTypeAndToken(Integer userID, String deviceType, String deviceToken);

    void updateUserLastAccessTime();


}
