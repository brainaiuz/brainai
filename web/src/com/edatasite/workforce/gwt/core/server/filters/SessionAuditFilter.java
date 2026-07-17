package com.edatasite.workforce.gwt.core.server.filters;

import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.gwtwidgets.server.spring.ServletUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * SessionAuditFilter ensures session is set for requests that need it
 * and clears ServerSecurityContext after request is processed.
 */
public class SessionAuditFilter implements Filter {

    private static final String EXCLUDE_PATTERN = ".*\\.(jpg|jpeg|png|gif|css|js|cache.html|nocache.js).*";

    @Override
    public void init(FilterConfig filterConfig) {
        // no initialization required
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        // Skip static resources
        String url = request.getRequestURI().toLowerCase();
        if (url.matches(EXCLUDE_PATTERN)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Bind response for frameworks that use ServletUtils
        ServletUtils.setResponse(response);

        try {
            // Only set session for /googleData endpoints
            if (request.getRequestURI().contains("/googleData")) {
                String sessionId = ServerUtils.setUserSessionid(request);
                ServerSecurityContext.getInstance().setSessionId(sessionId);
            }

            // Proceed with the filter chain
            filterChain.doFilter(request, response);

        } finally {
            // Always clear ServerSecurityContext to prevent thread-local leaks
            ServerSecurityContext.getInstance().setSessionId(null);
        }
    }

    @Override
    public void destroy() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
