package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.core.domain.EdsUserEmailSettings;
import com.edatasite.workforce.gwt.core.server.rpc.AuthDetails;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 1/25/11
 * Time: 8:38 PM
 * To change this template use File | Settings | File Templates.
 */
public interface SessionService {

    String obtainSession(AuthDetails authDetails);

    String obtainSessionAndRegisterInSystem(HttpServletRequest request, HttpServletResponse response, AuthDetails authDetails) throws IOException;

    void expireCurrentSession(String sessionId);

    void expireMobileUserSessionsAcrossCompanies(String username);

    EdsUserEmailSettings getUserSettings();
}
