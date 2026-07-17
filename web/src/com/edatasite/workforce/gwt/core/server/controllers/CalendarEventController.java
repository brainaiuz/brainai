package com.edatasite.workforce.gwt.core.server.controllers;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.shared.components.SessionCryptor;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.googlecalendar.server.app.GoogleCalendarServiceLocal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: Jan 29, 2011
 * Time: 5:29:39 PM
 * To change this template use File | Settings | File Templates.
 */

@Controller
public class CalendarEventController {

    @Qualifier("googleCalendarService")
    @Autowired
    private GoogleCalendarServiceLocal googleCalendarServiceLocal;
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;

    @RequestMapping(value = "/eventGuest.html")
    public ModelAndView handleRequestInternal(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServerUtils.fillHostParameters(request);
        String dataBaseName = request.getParameter("dtype") != null && !"null".equals(request.getParameter("dtype")) ? EncryptionHelper.decrypt(request.getParameter("dtype")) : null;
        String eventId = SessionCryptor.decrypt(request.getParameter("id"));
        String companyID = SessionCryptor.decrypt(request.getParameter("cid"));
        String email = request.getParameter("email");
        String answer = request.getParameter("answer");
        if (dataBaseName == null || "".equals(dataBaseName) || "null".equals(dataBaseName)) {
            dataBaseName = globalAuthJdbcSpringManager.getCompanyClusterType(Integer.valueOf(companyID));
        }
        SecurityContext.getInstance().setDatabase(dataBaseName);
        googleCalendarServiceLocal.updateEventGuestStatus(Integer.parseInt(companyID), Integer.parseInt(eventId), email, answer);
        return new ModelAndView("eventGuestsResponse");
    }
}
