package com.edatasite.workforce.gwt.core.server.controllers;

import com.edatasite.workforce.core.domain.EdsHostBasedSetting;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.BrowserSupportUtils;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.utils.EdsContextParams;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 10.04.2009
 * Time: 18:21:24
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class IndexController {
    private ModelAndView view;

    @RequestMapping(value = {"/", "/index.html"})
    public ModelAndView index() {
        return new ModelAndView("redirect:/auth/login.html");
    }

    @RequestMapping(value = {"/login/bestSignin.html", "/login.html", "/userLogin.html", "/robots.txt"})
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {

        if (request.getRequestURI() != null && !"".equals(request.getRequestURI()) && request.getRequestURI().toLowerCase().contains("bestsignin")) {
            return view = new ModelAndView("bestSignin");
        }
        if (request.getRequestURI() != null && !"".equals(request.getRequestURI()) && request.getRequestURI().toLowerCase().contains("robots.txt")) {
            InputStream stream = request.getServletContext().getResourceAsStream("robots.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(stream));

            StringBuilder sb = new StringBuilder();
            try {
                String line = br.readLine();
                while (line != null) {
                    sb.append(line);
                    sb.append("<br/>");
                    line = br.readLine();
                }
            } finally {
                br.close();
            }
            request.setAttribute("stream", sb.toString());

            return view = new ModelAndView("robots");
        }
        String bestSignin = request.getParameter("BEST_SIGNIN");

        if (bestSignin != null && bestSignin.equals("BEST_SIGNIN")) {
            return view = new ModelAndView("bestSignin");
        }

        ServerUtils.removeCookie(Constants.LAST_REQUEST_TIME, response);
        ServerUtils.fillHostParameters(request);
        String userAgent = request.getHeader("user-agent");

        String remoteHost = request.getServerName();
        EdsHostBasedSetting hostSetting = EdsContextParams.getHostSetting(remoteHost);
        view = new ModelAndView();
        if (hostSetting != null) {
            view.addObject("logoEnable", hostSetting.getLogoEnable());
            view.addObject("logoUrl", hostSetting.getLogoUrl());
            view.addObject("descriptionEnable", hostSetting.getDescriptionEnable());
            view.addObject("description", hostSetting.getDescription());
            view.addObject("faviconEnable", hostSetting.getFaviconEnable());
            view.addObject("faviconUrl", hostSetting.getFaviconUrl());
            view.addObject("socialLoginEnable", hostSetting.getSocialLoginEnable());
            view.addObject("forgotPasswordEnable", hostSetting.getForgotPasswordEnable());
            view.addObject("signUpEnable", hostSetting.getSignUpEnable());
        }
        if (Utils.isNewKpi(request)) {
            view.setViewName("loginNewKpi");
            return view;
        }
        if (Utils.isOrient(request)) {
            view.setViewName("../templates/templateOrient");
            return view;
        }
        if (Utils.isUpshot(request)) {
            view.setViewName("upshotIndex");
            return view;
        }
        if (Utils.isTexnopark(request)) {
            view.setViewName("../templates/templateArtel");
            return view;
        }
        if (Utils.isArtel(request)) {
            view.setViewName("../templates/templateArtelGroup");
            return view;
        }
        if (Utils.isYuborUz(request)) {
            view.setViewName("../templates/templateYuborUz");
            return view;
        }
        if (Utils.isBat(request)) {
            view.setViewName("../templates/templateNamangan");
            return view;
        }
        if (Utils.isActivira(request)) {
            view.setViewName("../templates/templateGh");
            return view;
        }
        if (Utils.isAkfa(request)) {
            view.setViewName("../templates/templateAkfaLighting");
            return view;
        }
        if (Utils.isCar24(request)) {
            view.setViewName("../templates/templateCitynet");
            return view;
        }
        if (Utils.isUzAuto(request)) {
            view.setViewName("../templates/templateUzAuto");
            return view;
        }
        if (Utils.isMerit(request)) {
            view.setViewName("../templates/templateMerit");
            return view;
        }
        if (Utils.isGtl(request)) {
            view.setViewName("../templates/templateGtl");
            return view;
        }
        if (Utils.isNestle(request)) {
            return view = new ModelAndView("../templates/templateNestle");
        }

        if (Utils.isFolioOne(request)) {
            return view = new ModelAndView("../templates/templateFolioone");
        }

        if (Utils.isGym(request) || Utils.isPraaktis(request)) {
            return view = new ModelAndView("../templates/templateGym");
        }
        if (Utils.isBrain(request)) {
            return view = new ModelAndView("../templates/templateBrainUz");
        }
        if (Utils.isHotel(request)) {
            return view = new ModelAndView("../templates/templateHotel");
        }
        if (Utils.isHotel(request)) {
            return view = new ModelAndView("../templates/templateHotel");
        }
        if (Utils.isTenderGPT(request)) {
            return view = new ModelAndView("../templates/tendergpt");
        }
        if (Utils.isCspace(request)) {
            return view = new ModelAndView("../templates/templateCspace");
        }
        if (Utils.isTextileFinds(request)) {
            return view = new ModelAndView("../templates/templateTextileFinds");
        }
        if (Utils.isAgroBank(request)) {
            return view = new ModelAndView("../templates/templateAgroBank");
        }
        if (Utils.isZeta(request)) {
            return view = new ModelAndView("../templates/templateZpw");
        }
        if (Utils.isTextileFinds(request)) {
            return view = new ModelAndView("../templates/templateTextileFinds");
        }

        String tgId = request.getParameter(Constants.TG_ID);
        String tgChatName = request.getParameter(Constants.TG_CHAT_NAME);

        if (!StringUtils.isEmpty(tgId)) {
            ServerUtils.removeCookie(Constants.TG_ID, response);
            Cookie cookie = new Cookie(Constants.TG_ID, tgId);
            cookie.setPath("/");
            response.addCookie(cookie);

            if (!StringUtils.isEmpty(tgChatName)) {
                ServerUtils.removeCookie(Constants.TG_CHAT_NAME, response);
                Cookie nameCookie = new Cookie(Constants.TG_CHAT_NAME, tgChatName);
                nameCookie.setPath("/");
                response.addCookie(nameCookie);
            }
        }


        if (StringUtils.isNotBlank(EdsContextParams.getOauth2ConsumerKey())) {
            request.setAttribute(Constants.GOOGLE_CLIENT_ID, EdsContextParams.getOauth2ConsumerKey());
        } else {
            request.setAttribute(Constants.GOOGLE_CLIENT_ID, "508805038834-snuqpahkvtkvjlfudk611plkbn9c25hm.apps.googleusercontent.com");
        }
        request.setAttribute(Constants.BROWSER_SUPPORT, BrowserSupportUtils.checkBrowserVersion(userAgent));
        view.setViewName("index");
        return view;
    }

    public ModelAndView getView() {
        return view;
    }

    @RequestMapping(value = "/success.html")
    public ModelAndView success(HttpServletRequest request) {
        ServerUtils.fillHostParameters(request);
        return new ModelAndView("success");
    }

    @RequestMapping(value = "/cancel.html")
    public ModelAndView cancel(HttpServletRequest request) {
        ServerUtils.fillHostParameters(request);
        return new ModelAndView("cancel");
    }

    @RequestMapping(value = "/sessionTimeOut.html", method = RequestMethod.GET)
    public ModelAndView sessionTimeOut(HttpServletRequest request) throws Exception {
        ServerUtils.fillHostParameters(request);
        return new ModelAndView();
    }

    @RequestMapping(value = "/successfullyRemovedFromMailingList.html")
    public ModelAndView successfullyRemovedFromMailingList() throws Exception {
        return new ModelAndView("successfullyRemovedFromMailingList");
    }

    @RequestMapping(value = "/loadingPage.html")
    public ModelAndView handleRequest(HttpServletRequest request) {
        ServerUtils.fillHostParameters(request);

        return new ModelAndView("loadingPage");
    }
}
