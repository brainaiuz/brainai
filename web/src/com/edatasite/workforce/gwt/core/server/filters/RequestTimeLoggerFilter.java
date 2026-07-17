package com.edatasite.workforce.gwt.core.server.filters;


import com.edatasite.workforce.rest.base.aspects.RequestTimeLogger;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Sherali on 1/26/2016.
 * Project web
 */
public class RequestTimeLoggerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain chain) throws ServletException, IOException {
        RequestTimeLogger.begin(httpServletRequest);
        chain.doFilter(httpServletRequest, httpServletResponse);
        RequestTimeLogger.end();
    }
}