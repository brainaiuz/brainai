package com.edatasite.workforce.gwt.core.server.filters;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Sherali on 1/18/2016.
 * Project web
 */
public class CharacterEncodingFilter extends org.springframework.web.filter.CharacterEncodingFilter {

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String exclude = ".*\\.(jpg|jpeg|png|gif|css|js|cache.html|nocache.js|svg).*";
        String url = request.getRequestURI().toLowerCase();

        if (url.matches(exclude)) {
            filterChain.doFilter(request, response);
        } else {
            super.doFilterInternal(request, response, filterChain);
        }
    }
}