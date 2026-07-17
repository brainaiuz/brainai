package com.edatasite.workforce.rest.v3.release10.crm.service;

import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.core.client.rpc.UserCompanyDTO;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SessionService;
import com.edatasite.workforce.gwt.core.server.rpc.AuthDetails;
import com.edatasite.workforce.rest.v3.release10.auth.service.ApiAuthService;
import com.edatasite.workforce.rest.v3.release10.crm.dto.GymCrmAccountTO;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ApiGymCrmAccountService {
    private final GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    private final ApiAuthService apiAuthService;
    private final SessionService sessionService;

    public ApiGymCrmAccountService(GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager, ApiAuthService apiAuthService, SessionService sessionService) {
        this.globalAuthJdbcSpringManager = globalAuthJdbcSpringManager;
        this.apiAuthService = apiAuthService;
        this.sessionService = sessionService;
    }

    public GymCrmAccountTO gymCrmAccount(HttpServletRequest request, HttpServletResponse response, Integer crmAccountId, Integer contactId, String email, Integer studentId) throws IOException {
        String currentSessionId = ServerSecurityContext.getInstance().getSessionId();
        String companyId = ServerSecurityContext.getInstance().getCompanyId();
        String database = ServerSecurityContext.getInstance().getDatabase();
        List<UserCompanyDTO> authInfoByUsername = globalAuthJdbcSpringManager.getAuthInfoByUsername(request.getServerName(), email);
        authInfoByUsername = apiAuthService.filterUserCompanyDTOList(authInfoByUsername);
        Optional<UserCompanyDTO> userCompany = authInfoByUsername.stream()
                .filter(e -> companyId == null || companyId.equals(e.getCompanyID().toString()))
                .findFirst();
        if (userCompany.isPresent()) {
            String sessionId = sessionService.obtainSessionAndRegisterInSystem(request, response, fillAuthDetails(request, userCompany.get()));
            return new GymCrmAccountTO(crmAccountId, contactId, sessionId, studentId);
        }
        if (currentSessionId == null) {
            throw new RuntimeException("Incorrect login or password");
        }
        ServerSecurityContext.getInstance().setSessionId(currentSessionId);
        ServerSecurityContext.getInstance().setCompanyId(companyId);
        ServerSecurityContext.getInstance().setDatabase(database);
        return new GymCrmAccountTO(crmAccountId, contactId, null,studentId);
    }

    private AuthDetails fillAuthDetails(HttpServletRequest request, UserCompanyDTO userDetails) {
        AuthDetails authDetails = new AuthDetails();
        authDetails.setCompanyID(userDetails.getCompanyID());
        authDetails.setDatabase(userDetails.getClusterDbName());
        authDetails.setUserID(userDetails.getUserID());
        authDetails.setUserAgent("restapi");
        authDetails.setIpAddress(ServerUtils.obtainClientIP(request));
        return authDetails;
    }

}
