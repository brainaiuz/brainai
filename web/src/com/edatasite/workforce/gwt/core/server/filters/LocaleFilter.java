package com.edatasite.workforce.gwt.core.server.filters;

/**
 * Created by IntelliJ IDEA.
 * User: Sherali
 * Date: 6/3/11
 * Time: 7:40 PM
 * To change this template use File | Settings | File Templates.
 */

import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.utils.EdsContextParams;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.jstl.core.Config;
import java.io.IOException;
import java.util.Locale;

/**
 * Filter to wrap request with a request including user preferred locale.
 */
public class LocaleFilter extends OncePerRequestFilter {


    /**
     * This method looks for a "locale" request parameter. If it finds one, it sets it as the preferred locale
     * and also configures it to work with JSTL.
     *
     * @param request  the current request
     * @param response the current response
     * @param chain    the chain
     * @throws IOException      when something goes wrong
     * @throws ServletException when a communication failure happens
     */
    @SuppressWarnings("unchecked")
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                 FilterChain chain)
            throws IOException, ServletException {

        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Headers", "Cache-Control, origin, s, sessionid, sign, x-requested-with, content-type, x-auth, accessToken");
        response.setHeader("Access-Control-Allow-Methods", "PUT, PATCH, GET, POST, DELETE, OPTIONS");

        String exclude = ".*\\.(jpg|jpeg|png|gif|css|js|cache.html|nocache.js).*";
        String url = request.getRequestURI().toLowerCase();

        if (url.matches(exclude) || url.startsWith("/rpc")) {
            chain.doFilter(request, response);
            return;
        }
        String locale = request.getParameter("locale");
        ServerUtils.fillHostParameters(request);
        String domainLocale = EdsContextParams.getDefaultLocale(request.getServerName()) != null ? EdsContextParams.getDefaultLocale(request.getServerName()).getLanguage() : "en";
        Locale preferredLocale;
        HttpSession session = request.getSession(false);
        domainLocale = domainLocale != null ? domainLocale : "en";
        if (locale == null) {
            locale = domainLocale;
        }
        int indexOfUnderscore = locale.indexOf('_');
        int indexOfDash = locale.indexOf('-');

        if (indexOfUnderscore != -1) {
            preferredLocale = generateLocal(locale.substring(0, indexOfUnderscore), locale.substring(indexOfUnderscore + 1));
        } else if (indexOfDash != -1) {
            preferredLocale = generateLocal(locale.substring(0, indexOfDash), locale.substring(indexOfDash + 1));
        } else {
            preferredLocale = new Locale(locale);
        }
        request = new LocaleRequestWrapper(request, preferredLocale);
        LocaleContextHolder.setLocale(preferredLocale);

        if (session != null) {
            session.setAttribute(Constants.PREFERRED_LOCALE_KEY, preferredLocale);
            Config.set(session, Config.FMT_LOCALE, preferredLocale);
        }
        chain.doFilter(request, response);

        // Reset thread-bound LocaleContext.
        LocaleContextHolder.setLocaleContext(null);
    }

    private Locale generateLocal(String language, String country) {
        return new Locale(language, country);
    }
}
