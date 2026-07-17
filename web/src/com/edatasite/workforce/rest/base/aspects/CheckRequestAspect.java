/*
package com.edatasite.workforce.rest.base.aspects;


import com.edatasite.workforce.core.domain.EdsCompanySystemSettings;
import com.edatasite.workforce.core.domain.EdsUserSession;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.CompanySystemSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.UserSessionManager;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v1.release10.core.BaseApiControllerV1;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

*/
/**
 * User: Dilsh0d Madrahimov
 * Date: 02.05.12
 * Time: 15:29
 *//*

@Aspect
public class CheckRequestAspect extends BaseApiControllerV1 {

    @Autowired
    private HttpServletRequest request;
    @Autowired
    private UserSessionManager userSessionManager;
    @Autowired
    private CompanySystemSettingsManager companySystemSettingsManager;

    @Around("@annotation(com.edatasite.workforce.rest.base.aspects.CheckRequest) && @annotation(checkRequest)")
    public Object invoke(ProceedingJoinPoint pjp, CheckRequest checkRequest) throws Throwable {
        Object result = null;
        if (checkRequest.checkSession()) {
            String sessionId = request.getHeader(ApiConstants.SESSION_ID);
            if (sessionId == null || "".equals(sessionId.trim())) {
                return this.errorResponse("Session is empty!", null, HttpServletResponse.SC_UNAUTHORIZED);
            }
            if (!sessionId.matches(Constants.SESSION_REGEX)) {
                return this.errorResponse("Session wrong!", null, HttpServletResponse.SC_UNAUTHORIZED);
            }

            ServerSecurityContext.getInstance().setSessionId(sessionId);
            EdsUserSession edsUserSession = userSessionManager.getUserSession(sessionId);
            if (edsUserSession == null) {
                return this.errorResponse("Session wrong!", null, HttpServletResponse.SC_UNAUTHORIZED);
            }

            // Getting sessionLength from company setting, or setting default sessionID live time
            long lastRequestTime = edsUserSession.getLastAccessTime().getTime();
            int sessionLength = ApiConstants.SESSION_LIVE_TIME_15_MINUTE;
            Integer companyID = userSessionManager.getUser().getCompany().getObjectID();
            EdsCompanySystemSettings settings = companySystemSettingsManager.findByCompanyID(companyID);
            if (settings != null && settings.getSessionLength() != null) {
                try {
                    sessionLength = Integer.valueOf(settings.getSessionLength());
                } catch (NumberFormatException ex) {
                    ex.printStackTrace();
                    //throw ApiConstants.RUNTIME_EXCEPTION_BASE;
                }
            }
            // Verifying for expiration & set lastAccessTime
            if (System.currentTimeMillis() < lastRequestTime + sessionLength * ApiConstants.MILLISECONDS_IN_HOUR) {
                edsUserSession.setLastAccessTime(new Date(System.currentTimeMillis()));
            } else {
                return this.errorResponse(REQUEST_USER_SESSION_EXPIRED, null, HttpServletResponse.SC_UNAUTHORIZED);
            }
        }

        result = pjp.proceed();
        return result;
    }


}
*/
