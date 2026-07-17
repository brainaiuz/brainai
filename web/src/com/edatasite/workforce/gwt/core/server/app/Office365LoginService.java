package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.workforce.gwt.core.server.rpc.office365.MeUserResponseTO;
import com.edatasite.workforce.gwt.core.server.rpc.office365.TokenResponseTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * User: Murad Satimov
 * Date: 9/7/17 8:23 PM
 */
public interface Office365LoginService {

    String createLoginUrl(HttpServletRequest request, HttpServletResponse response);

    MeUserResponseTO getUser(HttpServletRequest request, HttpServletResponse response);

    MeUserResponseTO getUserByToken(TokenResponseTO tokenTO);
}
