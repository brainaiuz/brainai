package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.core.domain.EdsCompanySystemSettings;
import com.edatasite.workforce.core.domain.EdsUserEmailSettings;
import com.edatasite.workforce.core.domain.EdsUserSession;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.core.client.rpc.StatusService;
import com.edatasite.workforce.gwt.core.client.rpc.UserCompanyDTO;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.db.CompanySystemSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.UserEmailSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.UserSessionManager;
import com.edatasite.workforce.gwt.core.server.rpc.AuthDetails;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Date;
import java.util.stream.Collectors;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 1/25/11
 * Time: 8:38 PM
 * To change this template use File | Settings | File Templates.
 */
@Transactional
@Service("sessionService")
public class SessionServiceImpl implements SessionService, Constants {
    private static final Logger log = LoggerFactory.getLogger(SessionServiceImpl.class);

    @Autowired
    private UserManager userManager;
    @Autowired
    private UserEmailSettingsManager userEmailSettingsManager;
    @Autowired
    private UserSessionManager userSessionManager;
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    private StatusService statusService;
    @Autowired
    private StatusServiceLocal statusServiceLocal;
    @Autowired
    protected CompanySystemSettingsManager companySystemSettingsManager;

    /**
     * Creates the session
     *
     * @param authDetails session creation details
     * @return created sessionID
     */
    @Transactional
    public String obtainSession(AuthDetails authDetails) {
        EdsUserSession session = new EdsUserSession();
        session.setUserId(authDetails.getUserID());
        session.setLastAccessTime(new Date());
        session.setSigniinDate(new Date());
        session.setUserAgent(authDetails.getUserAgent());
        session.setIPAddress(authDetails.getIpAddress());
        session.setSuperUser(authDetails.isSuperUser());
        session.setOpenIDUser(authDetails.isOpenIDSignIn());

        // Generate a secure random session ID component
        SecureRandom random = new SecureRandom();
        byte[] randomBytes = new byte[16]; // 128 bits of randomness
        random.nextBytes(randomBytes);
        String randomPart = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);

        // Construct the session ID
        String sessionID = authDetails.getDatabase() + "$" + authDetails.getCompanyID() + "$" + randomPart;
        session.setSessionID(sessionID);

        // Persist the session
        userSessionManager.create(session);

        // Set security context
        ServerSecurityContext.getInstance().setSessionId(session.getSessionID());
        ServerSecurityContext.getInstance().setSuperUser(session.isSuperUser());

//        String key = CacheConstants.SUPER_USER + "_" + session.getSessionID();
//        RedisClient.setKey(key, session.isSuperUser(), Boolean.class);

        return session.getSessionID();
    }

    @Transactional
    public String obtainSessionAndRegisterInSystem(HttpServletRequest request, HttpServletResponse response, AuthDetails authDetails) throws IOException {
        String sessionID;

        if (StringUtils.isEmpty(authDetails.getSessionID())) {
            ServerSecurityContext.getInstance().setCompanyId(authDetails.getCompanyID());

            //Check for IP address restriction
            if (!authDetails.isSuperUser() && !ServerUtils.hasPermission(PermissionConstants.ACCESS_FOR_IP, userManager.get(authDetails.getUserID()))) {
                if (request != null && response != null && !isIPValid(request, authDetails.getCompanyID())) {
                    String redirectUrl;
                    if (request.getServerName().contains("localhost")) {
                        redirectUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + "/invalidIP.html";
                    } else {
                        redirectUrl = "http://" + request.getServerName() + "/invalidIP.html";
                    }

                    request.setAttribute("INVALID_IP", redirectUrl);
//                    response.sendRedirect(redirectUrl);

                    return null;
                }
            }
            sessionID = obtainSession(authDetails); //for login
        } else {
            sessionID = authDetails.getSessionID();//for sign up
        }

        if (request != null && !authDetails.isSuperUser()) {
            statusServiceLocal.proceedUserAbsentStatus(AVAILABLE, authDetails.getUserID());//userAutoIn
        }
        return sessionID;
    }

    @Override
    @Transactional
    public void expireCurrentSession(String sessionId) {
        EdsUserSession session = userSessionManager.getUserSession(sessionId);

        if (session != null) {
            session.setExpired(true);
            userSessionManager.createOrUpdate(session);
        }
    }

    @Override
    @Transactional
    public void expireMobileUserSessionsAcrossCompanies(String username) {
        var companies = globalAuthJdbcSpringManager.getAuthInfoByUsername(EdsContextParams.getHostname(), username);
        if (companies == null || companies.isEmpty()) {
            return;
        }
        var schemaUserMap = companies.stream()
                .collect(Collectors.toMap(
                        UserCompanyDTO::getCompanyID,
                        UserCompanyDTO::getUserID,
                        (existing, duplicate) -> existing
                ));
        userSessionManager.expireMobileUserSessions(companies);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public EdsUserEmailSettings getUserSettings() {
        return userEmailSettingsManager.getUserSettings(userManager.getUser());
    }

    private boolean isIPValid(HttpServletRequest request, Integer companyID) {
        String clientIPAdderss = request.getHeader("X-FORWARDED-FOR"); //Load balancer sets this header client's IP
        log.info("X-FORWARDED-FOR: " + clientIPAdderss);
        if (clientIPAdderss == null) {//For local or aws there is no load balancer therefore it is null
            clientIPAdderss = request.getRemoteAddr();
        } else {
            clientIPAdderss = clientIPAdderss.split(",")[0];//Cut client ip from proxy ips
        }
        log.info("CLIENT  IP: " + clientIPAdderss);
        if (clientIPAdderss != null && !clientIPAdderss.contains(".")) {
            return true;//IPv6 check. If client IP is in IPv6 then skip the check
        }

        EdsCompanySystemSettings companySystemSettings = companySystemSettingsManager.findByCompanyID(companyID);
        if (companySystemSettings.getIpRanges() != null && !companySystemSettings.getIpRanges().equals("")) {
            String ips = companySystemSettings.getIpRanges();
            String[] ipRangeArray = ips.split("\\|");
            for (String ipRange : ipRangeArray) {
                String[] ipArray = ipRange.split(";");
                if (ipArray.length == 1) {
                    if (ipArray[0].equals(clientIPAdderss)) {
                        return true;
                    }
                } else if (checkInRange(clientIPAdderss, ipArray[0], ipArray[1])) {
                    return true;
                }
            }
            return false;
        }

        return true;
    }

    private boolean checkInRange(String clientIPAdderss, String fromIP, String toIP) {
        if (fromIP == null || fromIP.equals("")) {
            return true;
        }
        String[] clientIP = clientIPAdderss.split("\\.");
        String[] lowerIP = fromIP.split("\\.");
        String[] higherIP = toIP.split("\\.");
        boolean inRange = true;
        for (int i = 0; i < 4; i++) {
            Integer clientIPOctet = Integer.valueOf(clientIP[i]);
            Integer lowerIPOctet = Integer.valueOf(lowerIP[i]);
            Integer higherIPOctet = Integer.valueOf(higherIP[i]);
            if (clientIPOctet < lowerIPOctet || clientIPOctet > higherIPOctet) {
                inRange = false;
            }
        }
        return inRange;
    }
}
