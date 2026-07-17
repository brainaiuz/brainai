package com.edatasite.workforce.gwt.core.server.office365.services;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.UserCompanyDTO;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365AccessTokenDTO;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by umakarimov on 9/30/15.
 */
public interface Office365AuthService {
    boolean isLinkAction(String action);

    boolean isLoginAction(String action);

    String createAccountLinkUrl(HttpServletRequest request, HttpServletResponse response, String websiteUrl);

    String createAccountLoginUrl(HttpServletRequest request, HttpServletResponse response);

    String getActionFromState(HttpServletRequest request, HttpServletResponse response);

    Office365AccessTokenDTO acquireAccessToken(String hostUrl, String code, String site_url) throws IOException;

    Office365AccessTokenDTO renewExpiringAccessToken(String hostUrl, Office365AccessTokenDTO token);

    Office365AccessTokenDTO assureAccessToken(String hostUrl, Office365AccessTokenDTO token, String storagType);

    Office365User getMe(Office365AccessTokenDTO token);

    Office365AccessTokenDTO getUserAccessToken(String storageType);

    Office365AccessTokenDTO getUserAccessToken(String hostUrl, String storageType);

    Office365AccessTokenDTO getUserAccessToken(String hostUrl, EdsUser user, String storageType);

    Office365AccessTokenDTO getUserAccessToken(String hostUrl, UserCompanyDTO userCompany, String storageType);

    boolean isUserLinked(String storageType);

    boolean isUserLinked(EdsUser user, String storageType);

    boolean isUserLinked(EdsUser user, Integer companyID, String storageType);

    boolean isUserLinked(UserCompanyDTO userCompany, String storageType);

    void deleteOfficeCalendar(EdsEmployee employee, boolean delete, String storageType);
}
