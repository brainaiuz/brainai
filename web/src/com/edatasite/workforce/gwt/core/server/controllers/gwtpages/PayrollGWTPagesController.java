package com.edatasite.workforce.gwt.core.server.controllers.gwtpages;

import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: admin
 * Date: Nov 13, 2009
 * Time: 12:28:45 PM
 */
@Controller
public class PayrollGWTPagesController extends BaseGWTPagesController {

    @RequestMapping(value = "/Payroll.html", method = RequestMethod.GET)
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return super.handleRequest(request, response);
    }

    public ModelAndView doHandleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServerUtils.fillHostParameters(request);
        String uri = request.getRequestURI();
        String page = uri.substring(uri.lastIndexOf("/") + 1, uri.lastIndexOf("."));
        request.setAttribute(ACTIVE_MENU, page);
        //Add base currency
        String symbol = loginServiceLocal.getCompanyCurrencySymbol();
        request.setAttribute(BASE_CURRENCY, symbol != null ? symbol : "");
        page = Character.toLowerCase(page.charAt(0)) + page.substring(1);
        page = "/gwt-pages/" + page;
        return new ModelAndView(page);
    }
}
