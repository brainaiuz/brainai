package com.edatasite.workforce.gwt.core.server.controllers;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.UiSettings;
import com.edatasite.workforce.gwt.core.server.app.LoginServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CurrencyManager;
import com.edatasite.workforce.gwt.core.server.rpc.UserSignUPSessionID;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.gwt.submodule.paymentdeduction.client.SettingsData;
import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Stream;

@Controller
public class GettingStartedController {

    @Autowired
    @Qualifier("loginService")
    protected LoginServiceLocal loginServiceLocal;
    @Autowired
    private CurrencyManager currencyManager;
    @Autowired
    private ProfileService profileService;

    @RequestMapping(value = {"/gettingStarted.html"}, method = RequestMethod.GET)
    public ModelAndView gettingStarted(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return new ModelAndView("gettingStarted");
    }

    @RequestMapping(value = {"/gettingStarted.html"}, method = RequestMethod.POST)
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServerUtils.setUserSessionid(request);//if sessionID exists in cookies
        UserSignUPSessionID signedUser = loginServiceLocal.getSignedUser();
        ModelAndView view = new ModelAndView("companydata");
        view.addObject("modules", request.getParameterValues("modules"));
        view.addObject("name", getCompanyName(signedUser.getCompanyName()));
        view.addObject("languages", UiSettings.LANGUAGES_FOR_SIGNUP);
        view.addObject("locale", signedUser.getLocaleString());

        Stream.of(UiSettings.LANGUAGES_FOR_SIGNUP)
                .filter(l -> {
                    String localeCode = signedUser.getLocaleString().split("[_]")[0];
                    return localeCode.equalsIgnoreCase(l.getDescription());
                }).findFirst()
                .ifPresent(l -> view.addObject("userLanguage", l.getName()));

        SettingsData companySettings = profileService.getCompanySettings(false);
        view.addObject("countries", profileService.getCountries());
        view.addObject("companyCountryId", companySettings.getCountryID());
        view.addObject("timeZoneId", companySettings.getTimeZoneID());

        if (companySettings.getCountryID() != null) {
            SelectItem[] timezones = profileService.getMultipleCountryTimezones(Lists.newArrayList(companySettings.getCountryID()));
            view.addObject("timezones", timezones);
        }
        return view;
    }

    private String getCompanyName(String companyName) {
        if (companyName.startsWith("DEMO_")) {
            return "";
        } else {
            return companyName;
        }
    }
}
