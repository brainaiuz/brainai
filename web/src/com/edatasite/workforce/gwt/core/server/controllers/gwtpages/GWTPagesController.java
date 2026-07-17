package com.edatasite.workforce.gwt.core.server.controllers.gwtpages;

import com.edatasite.shared.components.EncryptionHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Jonibek
 * Date: May 8, 2009
 * Time: 6:39:32 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class GWTPagesController extends BaseGWTPagesController {

    @RequestMapping(value = {"/GoogleDocuments.html", "/ProjectManagement.html", "/MessageCenter.html", "/Project.html", "/Expenses.html", "/Development.html",
            "/Settings.html", "/Accounting.html", "/GoogleCalendar.html"
            , "/Crm.html", "/TrainingCenter.html"
            , "/Hrms.html", "/Sticky.html", "/Documents.html", "/TaskGadgetContainer.html", "/ReportingSystem.html"
            , "/Reporting.html", "/EventManagement.html", "/Reporting.html", "/Logistics.html"}, method = RequestMethod.GET)
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return super.handleRequest(request, response);
    }

    public ModelAndView doHandleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String uri = request.getRequestURI();

        if ((uri.contains("Reporting.html") || uri.contains("ReportingSystem.html"))
                && null != request.getParameter("key") && !"".equals(request.getParameter("key"))) {
            response.addCookie(new Cookie("urlParams", EncryptionHelper.decryptURL(request.getParameter("key"))));
        }


        String page = uri.substring(uri.lastIndexOf("/") + 1, uri.lastIndexOf("."));
        request.setAttribute(ACTIVE_MENU, page);
        page = Character.toLowerCase(page.charAt(0)) + page.substring(1);
        page = "/gwt-pages/" + page;

        return new ModelAndView(page);
    }

}
