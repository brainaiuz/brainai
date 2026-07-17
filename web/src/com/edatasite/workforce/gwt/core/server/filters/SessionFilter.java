package com.edatasite.workforce.gwt.core.server.filters;

import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SessionService;
import com.edatasite.workforce.gwt.core.server.rpc.AuthDetails;
import com.edatasite.workforce.gwt.core.server.rpc.AuthInfoItem;
import com.edatasite.workforce.utils.redis.RedisClient;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.gwtwidgets.server.spring.ServletUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

public class SessionFilter implements Filter, Constants {

    final Logger log = LoggerFactory.getLogger(SessionFilter.class);

    GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    SessionService sessionService;

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        ServletUtils.setResponse(response);
        Date start = new Date();

        try {
            if (!(request.getRequestURI().contains("worldPayNotification") || request.getRequestURI().contains("paypalNotification")
                    || request.getRequestURI().contains("PaypalNotification")
                    || request.getRequestURI().contains("mastercardPaymentValidate") || request.getRequestURI().contains("mastercardPaymentNotification")
                    || request.getRequestURI().contains("payWithElavon") || request.getRequestURI().contains("payWithElavonSC")
                    || request.getRequestURI().contains("invoiceClientApproveNotification") || request.getRequestURI().contains("magentoEventNotification"))
                    && !request.getRequestURI().contains("GoogleCheckoutNotification")) {
                String sessionId = "";

                //We have three ways of automatic login, below is list of those ways in priority order:
                // #1 - We must automatically login based on found cookie
                // #2 - We must check request parameter called SESSION_ID
                // #3 - Switch btw DocumentSign app and KPI (same way we will use to switch btw  Old-UI and New-UI) (token...&expire...&hash)


                String tokenParam = request.getParameter("token");
                boolean switchLayout = "true".equals(request.getParameter("switchlayout"));

                if (switchLayout && StringUtils.isNotBlank(tokenParam)) {
                    try {

                        if (StringUtils.isNotBlank(RedisClient.getKey(tokenParam))) {
                            String crossLayoutSessionId = RedisClient.getKey(tokenParam);
                            ServerSecurityContext.getInstance().setSessionId(crossLayoutSessionId);

                            if (StringUtils.isNotBlank(ServerSecurityContext.getInstance().getCompanyId())
                                    && ServerSecurityContext.getInstance().getUser() != null) {

                                //# generate session based on cross layout session
                                {
                                    Integer companyId = Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId());
                                    Integer userId = ((EdsUser) ServerSecurityContext.getInstance().getUser()).getObjectID();
                                    String dataBaseName = getGlobalAuthJdbcSpringManager().getUserDatabaseName(userId, companyId);

                                    //Create new Session
                                    sessionId = getSessionService().obtainSessionAndRegisterInSystem(request, response, new AuthDetails(companyId, userId, dataBaseName));

                                    //if sessionid is empty that means couldnt validate user with passed params
                                    if (StringUtils.isBlank(sessionId)) {
                                        cleanSessionCookie(request, response);
                                        filterChain.doFilter(servletRequest, servletResponse);
                                        return;
                                    }
                                }

                                //## re-init auth data for the new session ID, and define that the user has multi company or not
                                {
                                    RedisClient.setKey(sessionId, RedisClient.getKey(crossLayoutSessionId));
                                    AuthInfoItem authInfoItem = RedisClient.getKey(sessionId, AuthInfoItem.class);

                                    Cookie sessionCookie = new Cookie(IS_MULTI_COMPANY, authInfoItem.isMultiCompany() ? "true" : null);
                                    sessionCookie.setPath("/");
                                    response.addCookie(sessionCookie);
                                }

                                //## clearing prev source tokens
                                {
                                    //Delete session from request
                                    getSessionService().expireCurrentSession(crossLayoutSessionId);
                                    RedisClient.removeKey(crossLayoutSessionId);
                                    RedisClient.removeKey(tokenParam);
                                }
                            }
                        }
                    } catch (Exception e) {
                        log.error("", e);
                        //Clean SESSION_ID
                        cleanSessionCookie(request, response);
                    }

                    if(StringUtils.isNotBlank(sessionId)) {
                        ServerSecurityContext.getInstance().setSessionId(sessionId);
                        Cookie sessionCookie = new Cookie(SESSION_ID_COOKIE, sessionId);
                        sessionCookie.setPath("/");
                        response.addCookie(sessionCookie);
                        response.sendRedirect(request.getContextPath() + request.getRequestURI());
                        return;
                    } else {
                        //if sessionid is empty that means couldnt validate user with passed params
                        cleanSessionCookie(request, response);
                    }
                } else {
                    //#1 - We must automatically login based on found cookie
                    //Searching SESSION_ID from cookies
                    sessionId = ServerUtils.setUserSessionid(request);

                    //If sessionId not found in cookies then look at request param called SESSION_ID
                    // #2 - We must check request parameter called SESSION_ID
                    if (StringUtils.isBlank(sessionId) && request.getParameter(SESSION_ID_COOKIE) != null) {
                        sessionId = request.getParameter(SESSION_ID_COOKIE);
                        ServerSecurityContext.getInstance().setSessionId(sessionId);
                    }
                }

                filterChain.doFilter(servletRequest, servletResponse);
            } else {
                filterChain.doFilter(servletRequest, servletResponse);
            }

        } finally {
            Date end = new Date();
            long elapsedTime = end.getTime() - start.getTime();
            log.trace("->->Request = [ " + request.getRequestURI() + " ] Elapsed time to proceed this request = " + elapsedTime + "ms::" + ServerUtils.getCookiesAsStringForLog(request));
        }
    }

    @Override
    public void destroy() {

    }

    public void init(FilterConfig filterConfig) throws ServletException {

    }

    private void cleanSessionCookie(HttpServletRequest request, HttpServletResponse response) {
        if(request.getCookies()!=null) {
            for(int i = 0; i<request.getCookies().length;i++) {
                if(request.getCookies()[i].getName().equalsIgnoreCase(SESSION_ID_COOKIE)) {
                    request.getCookies()[i].setValue("");
                }
            }
        }
        Cookie sessionCookie = new Cookie(SESSION_ID_COOKIE, "");
        response.addCookie(sessionCookie);
        ServerSecurityContext.getInstance().setSessionId(null);
    }

    private SessionService getSessionService() {
        if (sessionService == null) {
            sessionService = ApplicationContextProvider.applicationContext.getBean(SessionService.class);
        }
        return sessionService;
    }

    private GlobalAuthJdbcSpringManager getGlobalAuthJdbcSpringManager() {
        return globalAuthJdbcSpringManager = globalAuthJdbcSpringManager == null
                ? ApplicationContextProvider.applicationContext.getBean(GlobalAuthJdbcSpringManager.class)
                : globalAuthJdbcSpringManager;
    }
}
