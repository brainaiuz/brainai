package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.shared.db.EdsDbException;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUsagePlan;
import com.edatasite.workforce.gwt.core.client.Exceptions.IncorrectPasswordException;
import com.edatasite.workforce.gwt.core.client.Exceptions.UserNotFoundException;
import com.edatasite.workforce.gwt.core.client.rpc.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.ActivateAccount;
import com.edatasite.workforce.gwt.core.client.rpc.UserCompanyDTO;
import com.edatasite.workforce.gwt.core.client.rpc.website.CompanyDomain;
import com.edatasite.workforce.gwt.core.server.rpc.LoggingInUser;
import com.edatasite.workforce.gwt.core.server.rpc.ShadowAccount;
import com.edatasite.workforce.gwt.core.server.rpc.UserSignUPSessionID;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanItem;
import com.edatasite.workforce.gwt.signup.client.rpc.ActivationLink;
import com.workforcetrack.mobile.rpc.login.MUserCompanyDTO;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 25.07.2009
 * Time: 20:22:25
 * To change this template use File | Settings | File Templates.
 */
public interface LoginServiceLocal {

    MUserCompanyDTO getMobileUserSettings(MUserCompanyDTO mUserCompanyDTO);

    void registrGoogleServices(String userEmail, String accessToken);

    MUserCompanyDTO getUserInfo(MUserCompanyDTO mUserCompanyDTO, boolean isForMobile);

    String getCompanyCurrencySymbol();

    boolean sendForgotPasswordNotification(Integer userId, Map<Boolean, CompanyDomain> isKpi) throws EdsDbException;

    String getCompanyLogoURL(EdsCompany company);

    ShadowAccount getShadowAccount(Integer id);

    String loginShadow(String userName, Integer companyId) throws UserNotFoundException, IncorrectPasswordException;

    LoggingInUser getLoggingUser(Integer userId);

    Boolean isActiveAccount(Integer id, Integer companyid);

    ActivateAccount getActiveAccount(Integer id, Integer companyid);

    String[] getAdmin();

    String[] updateAccount(AccountItem account);

    AccountItem getAccount();

    Boolean logout();

    UserSignUPSessionID getSignedUser();

    Integer updateUserSessionTrack(String sessionID, String section, String params);

    String loginWithEmail(String email, String userAgent, String hostUrl);

    List<UserCompanyDTO> filterUserCompanyDTOList(List<UserCompanyDTO> urlList);

    UserCompanyDTO filterUserCompanyDTOList(UserCompanyDTO urlList);

    Boolean isValid_User_For_Google_Gocs();

    ActivationLink getActivationLink(String key);

    void deleteActivationLink(Integer id);

    UsagePlanItem getUsagePlanItem(EdsUsagePlan usagePlan, Integer companyId);

    void setUserDeviceTypeAndToken(Integer userID, String deviceType, String deviceToken);

}
