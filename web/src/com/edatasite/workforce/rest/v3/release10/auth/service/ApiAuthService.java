package com.edatasite.workforce.rest.v3.release10.auth.service;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.domain.EdsActivationLink;
import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsUsagePlan;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.enums.DeviceTypeEnum;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.core.client.rpc.ActivateAccount;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyData;
import com.edatasite.workforce.gwt.core.client.rpc.UserCompanyDTO;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.LoginServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SessionService;
import com.edatasite.workforce.gwt.core.server.controllers.login.BaseLoginController;
import com.edatasite.workforce.gwt.core.server.db.ActivationLinkManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.UsagePlanManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.rabbitmq.service.RabbitMQService;
import com.edatasite.workforce.gwt.core.server.rpc.AuthDetails;
import com.edatasite.workforce.gwt.core.server.rpc.AuthInfoItem;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanItem;
import com.edatasite.workforce.gwt.myaccount.server.app.MyAccountServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.to.auth.TokenTO;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.auth.dto.ActivateReponse;
import com.edatasite.workforce.rest.v3.release10.auth.dto.CompanyTO;
import com.edatasite.workforce.rest.v3.release10.auth.dto.LoginDTO;
import com.edatasite.workforce.utils.EdsContextParams;
import com.edatasite.workforce.utils.redis.RedisClient;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.gwtwidgets.server.spring.ServletUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.EMPLOYEE_STATUS_ACTIVE;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.EXPIRED;
import static com.edatasite.workforce.rest.base.helpers.ApiConstants.INCORRECT_USERNAME_OR_PASSWORD;

@Service
public class ApiAuthService {
    private static final Logger log = LoggerFactory.getLogger(ApiAuthService.class);

    private final GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    private final CompanyManager companyManager;
    private final UserManager userManager;
    private final LoginServiceLocal loginServiceLocal;
    private final UsagePlanManager usagePlanManager;
    private final SessionService sessionService;
    private final WfmMessageSource commonLocalizer;
    private final MyAccountServiceLocal myAccountServiceLocal;
    private final ActivationLinkManager activationLinkManager;
    private final RabbitMQService rabbitMQService;

    public ApiAuthService(GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager,
                          CompanyManager companyManager,
                          UserManager userManager,
                          LoginServiceLocal loginServiceLocal,
                          UsagePlanManager usagePlanManager,
                          SessionService sessionService,
                          MyAccountServiceLocal myAccountServiceLocal,
                          ActivationLinkManager activationLinkManager,
                          RabbitMQService rabbitMQService,
                          @Qualifier("commonLocalizer") WfmMessageSource commonLocalizer
    ) {
        this.globalAuthJdbcSpringManager = globalAuthJdbcSpringManager;
        this.companyManager = companyManager;
        this.userManager = userManager;
        this.loginServiceLocal = loginServiceLocal;
        this.usagePlanManager = usagePlanManager;
        this.sessionService = sessionService;
        this.commonLocalizer = commonLocalizer;
        this.myAccountServiceLocal = myAccountServiceLocal;
        this.activationLinkManager = activationLinkManager;
        this.rabbitMQService = rabbitMQService;
    }

    public TokenTO login(LoginDTO request, String serverName) throws RestException {
        List<UserCompanyDTO> companies = getCompanyDTOList(request, serverName);
        if (companies.isEmpty()) {
            throw new RestException("Incorrect login or password", "Incorrect login or password", INCORRECT_USERNAME_OR_PASSWORD, HttpStatus.UNAUTHORIZED);
        }
        List<CompanyTO> companiesDto = companies.stream()
                .map(c -> new CompanyTO(c, obtainSession(c)))
                .toList();
        companies.forEach(c -> loginServiceLocal.setUserDeviceTypeAndToken(c.getUserID(), DeviceTypeEnum.IPhone.toString(), request.getDeviceToken()));
        for (UserCompanyDTO companyDTO : companies) {
            String session = obtainSession(companyDTO);
            EdsUser newUser = userManager.get(companyDTO.getUserID());
            return new TokenTO(companyDTO.getUserID(), session, List.of(), newUser.getUUID(), companiesDto);
        }

        throw new RestException("Free trial ended", "Free trial ended", EXPIRED, HttpStatus.UPGRADE_REQUIRED);
    }

    private List<UserCompanyDTO> getCompanyDTOList(LoginDTO request, String serverName) {
        String sessionId = SecurityContext.getInstance().getSessionId();
        List<UserCompanyDTO> companies;

        if (StringUtil.isEmpty(request.getPassword())) {
            companies = globalAuthJdbcSpringManager.getAuthInfoByUsername(serverName, request.getEmail());
        } else {
            companies = globalAuthJdbcSpringManager.getAuthInfoByUsernameAndPassword(serverName, request.getEmail(), request.getPassword());
        }

        ArrayList<UserCompanyDTO> result = new ArrayList<>();

        for (UserCompanyDTO userCompanyDTO : companies) {
            ServerSecurityContext.getInstance().setCompanyId(userCompanyDTO.getCompanyID());
            ServerSecurityContext.getInstance().setDatabase(userCompanyDTO.getClusterDbName());

            userCompanyDTO = validateCompany(userCompanyDTO, serverName);
            if (userCompanyDTO == null) {
                continue;
            }
            String logo = loginServiceLocal.getCompanyLogoURL(null);
            if (logo == null) {
                logo = Constants.COMPANY_NO_LOGO;
            }
            userCompanyDTO.setLogo(logo);

            setCompanyStatusCode(userCompanyDTO);
            result.add(userCompanyDTO);
        }

        ServerSecurityContext.getInstance().setSessionId(sessionId);

        return result;
    }

    private UserCompanyDTO validateCompany(UserCompanyDTO userCompanyDTO, String serverName) {
        EdsCompany company = companyManager.get(userCompanyDTO.getCompanyID());

        if (company == null) {
            return null;
        }

        if (serverName != null
                && ("gym.kpi.com".equals(serverName) || serverName.endsWith("praaktisgo.com"))
                && !"gym".equalsIgnoreCase(company.getOrgType())) {
            return null;
        }

        EdsUser user;

        try {
            user = userManager.get(userCompanyDTO.getUserID());
        } catch (Exception ignored) {
            return null;
        }

        if (user == null || user.getDeleted() || Constants.USER_TYPE_BMT_RESPONDENT.equals(user.getUserType())) {
            return null;
        }

        if (user instanceof EdsClientContact && (user.getClientContact().getAccess() == null || !user.getClientContact().getAccess())) {
            return null;
        }

        String status = userManager.getUserStatus(user.getObjectID());

        if (!EMPLOYEE_STATUS_ACTIVE.equals(status)) {
            return null;
        }
        userCompanyDTO.setCompanyName(company.getName());

        return userCompanyDTO;
    }

    private void setCompanyStatusCode(UserCompanyDTO companyDTO) {
        EdsCompany company = companyManager.get(companyDTO.getCompanyID());
        if (company == null) {
            return;
        }
        EdsUsagePlan usagePlan = usagePlanManager.getCurrentUsagePlan(company);
        UsagePlanItem usagePlanItem = loginServiceLocal.getUsagePlanItem(usagePlan, company.getObjectID());

        if (usagePlan != null && usagePlan.getPaid()) {
            companyDTO.setStatus(ApiConstants.ACTIVE_COMPANY);
        } else if ((usagePlanItem.isCurrSub() && (usagePlanItem.isFree() || usagePlanItem.isPaid()))) {
            companyDTO.setStatus(ApiConstants.FREE_COMPANY);
        }
        if (!(usagePlanItem.isCurrSub() && (usagePlanItem.isFree() || usagePlanItem.isPaid()))) {
            companyDTO.setStatus(ApiConstants.EXPIRED_COMPANY);
        }
    }

    private String obtainSession(UserCompanyDTO companyDTO) {
        AuthDetails authDetails = new AuthDetails();
        authDetails.setUserID(companyDTO.getUserID());
        authDetails.setDatabase(companyDTO.getClusterDbName());
        authDetails.setCompanyID(companyDTO.getCompanyID());
        authDetails.setUserAgent(ServerSecurityContext.getInstance().getSource());
        ServerSecurityContext.getInstance().setCompanyId(companyDTO.getCompanyID());
        ServerSecurityContext.getInstance().setDatabase(companyDTO.getClusterDbName());
        try {
            return sessionService.obtainSessionAndRegisterInSystem(null, null, authDetails);
        } catch (IOException e) {
            return null;
        }
    }

    public List<UserCompanyDTO> filterUserCompanyDTOList(List<UserCompanyDTO> urlList) {
        List<UserCompanyDTO> companyDTOList = new ArrayList<>();
        for (UserCompanyDTO userCompanyDTO : urlList) {
            ServerSecurityContext.getInstance().setCompanyId(userCompanyDTO.getCompanyID());
            ServerSecurityContext.getInstance().setDatabase(userCompanyDTO.getClusterDbName());
            EdsCompany company = companyManager.get(userCompanyDTO.getCompanyID());

            if (company == null || (company.isDeleted() != null && company.isDeleted())) {
                continue;
            }
            EdsUser user;
            try {
                user = userManager.get(userCompanyDTO.getUserID());
            } catch (Exception e) {
                continue;
            }
            if (user == null || user.getDeleted() || !EMPLOYEE_STATUS_ACTIVE.equals(userManager.getUserStatus(user.getObjectID()))) {
                continue;
            }
            if (Constants.USER_TYPE_BMT_RESPONDENT.equals(user.getUserType())) {
                continue;
            }
            if (user instanceof EdsClientContact && (user.getClientContact().getAccess() == null || (user.getClientContact().getAccess() != null && !user.getClientContact().getAccess()))) {
                continue;
            }
            userCompanyDTO.setActive(company.getActive());
            userCompanyDTO.setCompanyName(company.getName());
            userCompanyDTO.setFullName(user.getFullName());
            EdsUsagePlan usagePlan = usagePlanManager.getCurrentUsagePlan(company);
            if (usagePlan != null && usagePlan.getPaid()) {
                userCompanyDTO.setStatusName(commonLocalizer.localize("active"));
                userCompanyDTO.setStatus("active");
            } else {
                userCompanyDTO.setStatus("free");
                userCompanyDTO.setStatusName(commonLocalizer.localize("freeTrialButton"));
            }

            UsagePlanItem usagePlanItem = getUsagePlanItem(usagePlan);
            if (!(usagePlanItem.isCurrSub() && (usagePlanItem.isFree() || usagePlanItem.isPaid()))) {
                userCompanyDTO.setStatus("expired");
                userCompanyDTO.setStatusName(commonLocalizer.localize("expired"));
            }
            companyDTOList.add(userCompanyDTO);
        }
        return companyDTOList;
    }

    private UsagePlanItem getUsagePlanItem(EdsUsagePlan usagePlan) {
        UsagePlanItem result = new UsagePlanItem();
        if (usagePlan == null) {
            result.setFree(true);
            return result;
        }
        UsagePlanItem item = myAccountServiceLocal.getParametr(usagePlan);
        result.setFree(item.isFree());
        result.setPaid(usagePlan.getPaid());
        result.setCurrSub(true);
        return result;
    }

    public ActivateReponse activate(HttpServletRequest request, String key, boolean changePass) {
        Map<String, String> params = ServerUtils.extractParamsFromUrl(key);
        if (params.containsKey("key")) {
            return activate(request, key, changePass, params);
        }
        if (params.containsKey("uid") && params.containsKey("cid")) {
            return account(request, params.get("uid"), params.get("cid"));
        }
        return null;
    }

    public ActivateReponse activate(HttpServletRequest request, String key, boolean changePass, Map<String, String> params) {
        ServerUtils.fillHostParameters(request);

        key = params.getOrDefault("key", key);
        changePass = (params.containsKey("chpass")) ? Boolean.parseBoolean(params.get("chpass")) : changePass;

        if (StringUtils.isEmpty(key)) {
            log.info("==========================ACTIVATION KEY IS EMPTY=");
            return null;
        }
        String keyQueryParams = EncryptionHelper.decrypt(key);

        if (StringUtils.isEmpty(keyQueryParams)) {
            log.info("==========================ACTIVATION: CANNOT DECRYPT KEY");
            return null;
        }
        Map<String, String> paramsMap = splitQuery(keyQueryParams);

        String companyIdStr = paramsMap.get("company_id");
        if (StringUtils.isEmpty(companyIdStr)) {
            log.info("==========================ACTIVATION: COMPANY ID NOT FOUND");
            return null;
        }
        int companyId = Integer.parseInt(companyIdStr.trim());
        String db = StringUtils.isBlank(companyIdStr)
                ? Constants.DATABASE_FREE
                : globalAuthJdbcSpringManager.getCompanyDatabaseName(companyId);
        ServerSecurityContext.getInstance().setDatabase(db);

        EdsActivationLink activationLink = activationLinkManager.getByKey(key);

        if (activationLink == null || activationLink.getCompanyId() == null || activationLink.getUserId() == null) {
            log.info("==========================ACTIVATION NOT FOUND WITH KEY: {}", key);
            return null;
        }
        EdsUser user = userManager.getUserByUserIdAndCompanyId(activationLink.getUserId(), companyId);
        String dataBaseName = globalAuthJdbcSpringManager.getUserDatabaseName(activationLink.getUserId(), activationLink.getCompanyId());

        if (dataBaseName == null || StringUtils.isEmpty(dataBaseName)) {
            log.info("==========================ACTIVATION NOT FOUND DATABASE:");
            log.info("KEY: {}", key);
            log.info("COMPANY_ID: {}", activationLink.getCompanyId());
            log.info("USER_ID: {}=", activationLink.getUserId());

            return null;
        }
        loginServiceLocal.deleteActivationLink(activationLink.getObjectID());
        AuthDetails authDetails = new AuthDetails(activationLink.getCompanyId(), activationLink.getUserId(), dataBaseName);
        SecurityContext.getInstance().setDatabase(dataBaseName);
        SecurityContext.getInstance().setCompanyId(activationLink.getCompanyId());
        ActivateAccount employee = loginServiceLocal.getActiveAccount(authDetails.getUserID(), authDetails.getCompanyID());

        if (employee == null) {//if user not found show an error
            log.info("==========================ACTIVATION NOT FOUND USER:");
            log.info("KEY: {}", key);
            log.info("DATABASE: {}", dataBaseName);
            log.info("COMPANY_ID: {}", activationLink.getCompanyId());
            log.info("USER_ID: {}=", activationLink.getUserId());
            return null;
        }
        if (employee.isActive() && !changePass) {
            try {
                String sessionId = sessionService.obtainSession(authDetails);
                return new ActivateReponse(sessionId, activationLink.getUserId(), activationLink.getLinkType().name(), user.getEmail());
            } catch (Exception ignored) {
            }
            return null;
        }
        try {
            ServerSecurityContext.getInstance().setCompanyId(authDetails.getCompanyID());
            String sessionId = sessionService.obtainSession(authDetails);


            Integer leadSignUpCompany = EdsContextParams.getLeadSignUpCompany();
            if (leadSignUpCompany != null) {
                CompanyData companyData = new CompanyData();
                companyData.setCompanyId(authDetails.getCompanyID());
                companyData.setRating("Activated");
                rabbitMQService.sendCompanySettingsUpdate(companyData, leadSignUpCompany);
            }
            RedisClient.setKey(sessionId, new AuthInfoItem().buildForBasicLogin(employee.getLogin(), null), AuthInfoItem.class);
            return new ActivateReponse(sessionId, activationLink.getUserId(), activationLink.getLinkType().name(), user.getEmail());
        } catch (Exception ex) {
            log.error(ex.getMessage(), ex);
        }
        return null;
    }

    public ActivateReponse account(HttpServletRequest request, String uid, String cid) {
        ServerUtils.fillHostParameters(request);
        if (StringUtils.isEmpty(cid) || StringUtils.isEmpty(uid)) {
            return null;
        }

        String companyID = cid;
        String userId = uid;

        while (companyID.contains("%") || userId.contains("%")) {
            companyID = EncryptionHelper.decodeURL(companyID);
            userId = EncryptionHelper.decodeURL(userId);
        }
        companyID = EncryptionHelper.decrypt(companyID);
        userId = EncryptionHelper.decrypt(userId);


        if (StringUtils.isEmpty(companyID) || StringUtils.isEmpty(userId)) {
            return null;
        }
        Integer cID = Integer.valueOf(companyID);
        Integer uID = Integer.valueOf(userId);

        String dataBaseName = globalAuthJdbcSpringManager.getUserDatabaseName(uID, cID);

        if (dataBaseName == null || StringUtils.isEmpty(dataBaseName)) {
            return null;
        }

        AuthDetails authDetails = new AuthDetails(cID, uID, dataBaseName);

        SecurityContext.getInstance().setDatabase(dataBaseName);
        SecurityContext.getInstance().setCompanyId(companyID);
        ActivateAccount employee = loginServiceLocal.getActiveAccount(authDetails.getUserID(), authDetails.getCompanyID());
        if (employee == null) {
            return null;
        }

        try {
            ServerSecurityContext.getInstance().setCompanyId(authDetails.getCompanyID());
            String sessionId = sessionService.obtainSession(authDetails);
            return new ActivateReponse(sessionId, authDetails.getUserID(), null, employee.getLogin());
        } catch (Exception ex) {
            return null;
        }
    }


    public Map<String, String> splitQuery(String queryParams) {
        if (StringUtils.isEmpty(queryParams)) {
            return Collections.emptyMap();
        }
        return Arrays.stream(queryParams.split("&"))
                .map(this::splitQueryParameter).collect(Collectors.toMap(AbstractMap.SimpleImmutableEntry::getKey, AbstractMap.SimpleImmutableEntry::getValue));
    }

    public AbstractMap.SimpleImmutableEntry<String, String> splitQueryParameter(String it) {
        int idx = it.indexOf("=");
        String key = idx > 0 ? it.substring(0, idx) : it;
        String value = idx > 0 && it.length() > idx + 1 ? it.substring(idx + 1) : null;
        return new AbstractMap.SimpleImmutableEntry<>(key, value);
    }

    public List<CompanyTO> getCompanies() {
        EdsUser user = userManager.getUser();
        if (user == null) return List.of();
        HttpServletRequest request = ServletUtils.getRequest();
        Integer companyID = Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId());
        AuthInfoItem authInfoItem = RedisClient.getKey(ServerSecurityContext.getInstance().getSessionId(), AuthInfoItem.class);
        List<UserCompanyDTO> ucd = null;
        if (authInfoItem == null) {
            String username = globalAuthJdbcSpringManager.getUsername(companyID, userManager.getUser().getObjectID());
            ucd = globalAuthJdbcSpringManager.getAuthInfoByUsername(request.getServerName(), username);
        } else if (BaseLoginController.FROM_BASIC_LOGIN.equals(authInfoItem.getAuthType())) {
            ucd = globalAuthJdbcSpringManager.getAuthInfoByUsernameAndPassword(request.getServerName(), authInfoItem.getUsername(), authInfoItem.getPassword());
        } else if (BaseLoginController.FROM_FEDERATED_LOGIN.equals(authInfoItem.getAuthType())) {
            if (StringUtils.isNotBlank(authInfoItem.getEmail())) {
                ucd = globalAuthJdbcSpringManager.getAuthInfoByUsername(request.getServerName(), authInfoItem.getEmail());
            }
        } else {
            ucd = List.of();
        }
        if (ucd == null) return List.of();
        return filterUserCompanyDTOList(ucd).stream()
                .map(CompanyTO::new)
                .toList();
    }

}
