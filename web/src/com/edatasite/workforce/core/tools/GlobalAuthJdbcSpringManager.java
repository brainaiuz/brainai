package com.edatasite.workforce.core.tools;

import com.edatasite.workforce.core.domain.EdsSubscriptionHistory;
import com.edatasite.workforce.core.domain.EdsUsagePlan;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.MassMailParams;
import com.edatasite.workforce.gwt.backend.client.rpc.AccountManagementListItem;
import com.edatasite.workforce.gwt.core.client.enums.UITypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.ApiAccessToken;
import com.edatasite.workforce.gwt.core.client.rpc.TelegramSettingsItem;
import com.edatasite.workforce.gwt.core.client.rpc.UserCompanyDTO;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.website.CompanyDomain;
import com.edatasite.workforce.gwt.core.server.controllers.login.marketplace.DomainInfo;
import com.edatasite.workforce.gwt.pricing.client.UserRateItem;
import com.edatasite.workforce.gwt.profile.client.rpc.ConsolidationCompanyList;
import com.edatasite.workforce.rest.v3.release10.core.to.GoogleAuthTO;

import java.math.BigDecimal;
import java.util.*;

/**
 * User: Sherali
 * Date: 4/15/11
 * Time: 12:25 PM
 */
public interface GlobalAuthJdbcSpringManager {
    UsagePlan getUsagePlane(Integer subsId, String customType);

    DomainInfo findByGoogleAppDomain(final String domainName, final String googleAppDomain, final String email);

    CompanyDomain findByGoogleAppDomain(final String domain);

    String getUserDatabaseName(final Integer userid, final Integer companyid);

    String getCompanyDatabaseName(final Integer companyid);

    String getCompanyServiceId(final Integer companyid);

    String getClassicUIHost(Integer companyId);

    String getLightUIHost(Integer companyId);

    CompanyDomain getCompanyDomain(final String domain);

    LinkedHashMap<String, String> getCompanyUniqueKeyListMap(Integer companyID);

    CompanyDomain getCompanyDomainByWN(final String number);

    CompanyDomain getCompanyIdByUniqueKey(String number);

    void insertEdsUsagePlanList(List<EdsUsagePlan> edsUsagePlanList);

    void insertEdsSubscriptionHistoryList(List<EdsSubscriptionHistory> edsSubscriptionHistoryList);

    String insertCompanyDeviceIdByCompanyId(Integer companyId, CompanyDomain companyDomain);

    Integer saveFingerprintSetup(CompanyDomain domain);

    void deleteCompanyDomainByCompanyId(Integer companyId);

    List<UserCompanyDTO> getUserCompanyByEmail(String domainName, String email);

    List<Integer> getUserCompanyIdsByEmailAndUserId(String email, Integer userId);

    void registerCompanyToLoginDispatcherDummy(final List<CompanyidDomain> companyList);

    void updateCompanyDomainInfo(Integer companyid, String domain);

    void registerCompanyHost(Integer companyId, String host);

    void registerCompanyService(Integer companyId, String serviceId);

    void registerCompanyToLoginDispatcher(List<Integer> companyList);

    AccountManagementListItem getValidUser(AccountManagementListItem account);

//    void changeAccountPassword(Integer authid, String password);

    void changeAccountUserName(Integer userCompanyId, String userName);

    void changeExistingACcountCompany(Integer existingAuthId, Integer authId, Integer companyId, Integer oldAuthId);

    void changeMaintenanceStatus(Integer companyID);

    void deleteCompanyUser(Integer userId, Integer companyId);

    void deleteCompanyFromGlobalAuth(Integer companyID);

    List<Map<String, Object>> getCompanyAuthList(Integer companyId);

    boolean getAuthInfo(String domain, String username, String password);

    List<UserCompanyDTO> getAuthInfoByUsernameAndPassword(String domainName, String login, String password);

    boolean isSuperPassword(final String password, final String domainname);

    ApiAccessToken getApiAccessToken(String accessToken);

    ApiAccessToken getApiAccessTokenByID(Integer id);

    ListResult<ApiAccessToken> getApiAccessTokenList(ListingFilterParameter fp);

    Boolean saveAccessToken(ApiAccessToken accessToken);

    List<UserCompanyDTO> getAuthInfoByUsername(final String domainName, final String username);

    UserCompanyDTO getUserInfoByUserNamePasswordAndCompany(final String username, final String password, final Integer companyid);

    String getMaintenanceStatus(Integer companyID);

    Boolean isEnabledAdvancedPassword(String companyIDs);

    String getAllCompanyIdsByAuthId(Integer authID);

    BigDecimal getUserRatePerHOST(Integer userCount, String hostName);

    UserRateItem getUserDiscountPerHOST(String hostName, Integer userCount);

    Integer getMaxPayableUserCount(String hostName);

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    UserRateItem getUserCountsViaPrice(String hostName, String pricingPackageNAME, String modules, boolean pricingType);

    BigDecimal getUserRatePerPackage(/*Integer userCount, */String hostName, String pricingPackageName);

    BigDecimal getSupportPricePerPackage(String hostName, String supportPackageName);

    HashMap<String, Double> getSupportPackagePrices(String hostName);

    HashMap<String, UserRateItem> getPerPricingSupportUserPrices(String hostName, ArrayList<String> pricingPackageNames);

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    void updateClusterCompanyParent(Integer companyId, Integer parentId);

    List<ConsolidationCompanyList> getSubsidiariesCompany(Integer companyId);

    String getCompanyClusterType(Integer companyId);

    Integer getCompanyClusterId(Integer companyId);

    Map<String, Set<Integer>> getClusterCompanies();

    Map<Integer, String> getCompaniesClusterType(String companyIds);

    Map<Integer, String> getSubsidiariesCompanyClusterType(Integer parentCompanyId);

    void chageAccountPasswordByAuthId(AccountManagementListItem item, String pwd);

    void recordChangePassword(AccountManagementListItem item, String passwordEnc);

    List<String> getLastPasswordList(Integer userID);

    Vector<String> getAllDomains();

    Vector<String> getAllWhiteLabelDomains();

    String updateUserAuthDomainName(EdsUser user);

    UserCompanyDTO getUserInfoByUserNameAndCompany(final String domainName, final String username, final Integer companyid);

    void blockLogin(String login);

    boolean isLoginBlocked(String login);

    void updateUserEmail(EdsUser user);

    void patchForGA(String sql, final Object... values);

    ArrayList<CompanyDomain> getFingerprintSetup(Integer companyID);

    CompanyDomain getFingerprintSetupById(Integer id);

    MassMailParams getMassMailParamsByCompany(Integer companyID);

    String getUsername(Integer companyID, Integer userID);

    Boolean createPushNotificationToken(String domainName, String username, String firebasePushNotificationToken, String devicetype, String module);

    Boolean deletePushNotificationToken(String firebasePushNotificationToken, String device_os);

    List<String[]> getUserPushNotificationTokens(String username, String domainName, String modulecode);


    void setCurrentUIToUser(UITypeEnum ui, String username);

    void setDomainToUIAccess(String domain, String username);

    ListResult<TelegramSettingsItem> getTelegramSettingItems(ListingFilterParameter filterParameter);

    void saveTelegramSettingsItem(TelegramSettingsItem telegramSettingsItem);

    TelegramSettingsItem getTelegramSettingsItem(Integer id);

    Boolean deleteTelegramSettingsItem(Integer id);

    List<TelegramSettingsItem> getTelegramSettingsItemByToken(String token);

    TelegramSettingsItem getTelegramSettingsItemByTokenAndCompanyId(String token, Integer companyId);

    TelegramSettingsItem getTelegramSettingsItemByBotNameAndCompanyId(String botName, Integer companyId);

    void createOauthToken(GoogleAuthTO auth);

    boolean existsOAuthToken(String email, String userId);

    String updateSmsCode(int companyId, String phoneNumber);

    boolean verifySmsCode(String phonenumber, int code);

    void deleteFingerprintSetupById(Integer id);
}
