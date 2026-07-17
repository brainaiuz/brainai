package com.edatasite.workforce.gwt.core.server.office365.managers;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.UserCompanyDTO;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365AccessTokenDTO;

import java.util.List;

/**
 * Created by umakarimov on 9/21/15.
 */
public interface Office365AuthManager {
    List<UserCompanyDTO> getAuthInfoByObjectId(String objectId, final String domainName);

    Office365AccessTokenDTO getAccessToken(Integer authId, Integer companyId, String storageType);

    boolean hasAccessToken(Integer authId, Integer companyId, String storageType);

    Office365AccessTokenDTO saveAccessToken(Office365AccessTokenDTO token, String storageType);

    void sendValidationEmail(String email, Integer authTokenId, String domainName, Integer companyId);

    void deleteOfficeTokens(String storageType);

    Office365AccessTokenDTO getAuthTokenFromSecretCode(String secretCode);

    void assignOfficeUser(Integer userId, Integer companyId, String objectId);

    UserCompanyDTO getUserCompany();

    UserCompanyDTO getUserCompany(EdsUser user);

    UserCompanyDTO getUserCompany(EdsUser user, Integer companyID);
}
