package com.edatasite.workforce.gwt.core.server.controllers;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.workforce.core.domain.EdsActivationLink;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.core.client.rpc.ActivateAccount;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyData;
import com.edatasite.workforce.gwt.core.client.rpc.LoginService;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.LoginServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.SessionService;
import com.edatasite.workforce.gwt.core.server.db.ActivationLinkManager;
import com.edatasite.workforce.gwt.core.server.rabbitmq.service.RabbitMQService;
import com.edatasite.workforce.gwt.core.server.rpc.AuthDetails;
import com.edatasite.workforce.gwt.core.server.rpc.AuthInfoItem;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.utils.EdsContextParams;
import com.edatasite.workforce.utils.redis.RedisClient;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * User: Sherali
 * Date: 5/25/11
 * Time: 2:50 PM
 */
@Controller
public class AccountController implements Constants {

    @Qualifier("loginService")
    @Autowired
    private LoginService loginService;
    @Autowired
    private SessionService sessionService;
    @Autowired
    @Qualifier("loginService")
    private LoginServiceLocal loginServiceLocal;
    @Autowired
    private ActivationLinkManager activationLinkManager;
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    private RabbitMQService rabbitMQService;

    @RequestMapping(value = "/account", method = RequestMethod.GET)
    public ModelAndView account(HttpServletRequest request,
                                HttpServletResponse response,
                                @RequestParam(value = "name", required = false) String name,
                                @RequestParam(value = "pass", required = false) String pass,
                                @RequestParam("uid") String uid,
                                @RequestParam("cid") String cid,
                                @RequestParam(value = "chpass", defaultValue = "false") String changePass,
                                @RequestParam(value = "adPassEnabled", defaultValue = "false") String adPassEnabled) {
        ServerUtils.fillHostParameters(request);
        if (StringUtils.isEmpty(cid) || StringUtils.isEmpty(uid)) {
            return new ModelAndView("redirect:NoUserFound.html");
        }

        System.out.println("Enter companyID:=" + cid);
        System.out.println("Enter userId:=" + uid);

        String companyID = cid;
        String userId = uid;

        while (companyID.contains("%") || userId.contains("%")) {
            companyID = EncryptionHelper.decodeURL(companyID);
            userId = EncryptionHelper.decodeURL(userId);
        }
/*System.out.println("companyid=" + EncryptionHelper.encodeURL(EncryptionHelper.encrypt("200783")));
System.out.println("userid=" + EncryptionHelper.encodeURL(EncryptionHelper.encrypt("51574")));
System.out.println("duserid=" + EncryptionHelper.encodeURL(EncryptionHelper.encrypt("1")));*/
        companyID = EncryptionHelper.decrypt(companyID);
        userId = EncryptionHelper.decrypt(userId);
//        http://localhost:8080/account?cid=G1jkwEFqviA%3D&uid=jgyayOHxzmQ%3D
//        http://localhost:8080/account?cid=G1jkwEFqviA%3D&uid=RzmFEdQD%2Fdk%3D

        System.out.println("companyID:=" + companyID);
        System.out.println("userId:=" + userId);

        if (StringUtils.isEmpty(companyID) || StringUtils.isEmpty(userId)) {
            throw new RuntimeException("Probably hack attempt");
        }
        Integer cID = Integer.valueOf(companyID);
        Integer uID = Integer.valueOf(userId);

        String dataBaseName = globalAuthJdbcSpringManager.getUserDatabaseName(uID, cID);

        if (dataBaseName == null && StringUtils.isEmpty(dataBaseName)) {
            return new ModelAndView("redirect:NoUserFound.html");
        }

        AuthDetails authDetails = new AuthDetails(cID, uID, dataBaseName);
        /////////////Setting companyID to thread local variable//////////////

        SecurityContext.getInstance().setDatabase(dataBaseName);
        SecurityContext.getInstance().setCompanyId(companyID);
        ActivateAccount employee = loginServiceLocal.getActiveAccount(authDetails.getUserID(), authDetails.getCompanyID());
        if (employee == null) {//if user not found show an error
            return new ModelAndView("redirect:NoUserFound.html");
        }
        if (employee.isActive() && !Boolean.valueOf(changePass)) {//if user already active redirect to the login page
            if (request.getServerName().contains("localhost")) {
                try {
                    response.sendRedirect("http://" + request.getServerName() + ":" + request.getServerPort());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    response.sendRedirect("http://" + request.getServerName());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return new ModelAndView("redirect:index.html");
        }

        try {
            ServerSecurityContext.getInstance().setCompanyId(authDetails.getCompanyID());
            String sessionId = sessionService.obtainSession(authDetails);
            Cookie sessionCookie = new Cookie(SESSION_ID_COOKIE, sessionId);
            //We are making SESSION_ID cookie visible for all multisubdomains
            /*if(!"localhost".equalsIgnoreCase(request.getServerName())) {
                sessionCookie.setDomain("." + request.getServerName());
            }*/
            response.addCookie(sessionCookie);
            ModelAndView model = new ModelAndView("redirect:/auth/changePassword.html");
            model.addObject("adPassEnabled", adPassEnabled);
            return model;
        } catch (Exception ex) {
            return new ModelAndView("redirect:BadUrlError.html");
        }
    }

    @RequestMapping(value = {"/account/activation", "/signup/activation"}, method = RequestMethod.GET)
    public ModelAndView activate(HttpServletRequest request,
                                 HttpServletResponse response,
                                 @RequestParam(value = "key") String key,
                                 @RequestParam(value = "chpass", defaultValue = "false") String changePass,
                                 @RequestParam(value = "adPassEnabled", defaultValue = "false") String adPassEnabled) {
        ServerUtils.fillHostParameters(request);


        if (StringUtils.isEmpty(key)) {
            System.out.println("==========================ACTIVATION KEY IS EMPTY=");
            return new ModelAndView("redirect:/NoUserFound.html");
        }
        String keyQueryParams = EncryptionHelper.decrypt(key);

        if (StringUtils.isEmpty(keyQueryParams)) {
            System.out.println("==========================ACTIVATION: CANNOT DECRYPT KEY");
            return new ModelAndView("redirect:/NoUserFound.html");
        }
        Map<String, String> paramsMap = splitQuery(keyQueryParams);

        if (StringUtils.isEmpty(paramsMap.get("company_id"))) {
            System.out.println("==========================ACTIVATION: COMPANY ID NOT FOUND");
            return new ModelAndView("redirect:/NoUserFound.html");
        }
        String db = StringUtils.isNotBlank(paramsMap.get("company_id")) ? globalAuthJdbcSpringManager.getCompanyDatabaseName(Integer.valueOf(paramsMap.get("company_id").trim())) : Constants.DATABASE_FREE;
        ServerSecurityContext.getInstance().setDatabase(db);

        EdsActivationLink activationLink = this.activationLinkManager.getByKey(key);

        if (activationLink == null || activationLink.getCompanyId() == null || activationLink.getUserId() == null) {
            System.out.println("==========================ACTIVATION NOT FOUND WITH KEY: " + key);
            return new ModelAndView("redirect:/NoUserFound.html");
        }
        String dataBaseName = globalAuthJdbcSpringManager.getUserDatabaseName(activationLink.getUserId(), activationLink.getCompanyId());

        if (dataBaseName == null && StringUtils.isEmpty(dataBaseName)) {
            System.out.println("==========================ACTIVATION NOT FOUND DATABASE:");
            System.out.println("KEY: " + key);
            System.out.println("COMPANY_ID: " + activationLink.getCompanyId());
            System.out.println("USER_ID: " + activationLink.getUserId() + "=");

            return new ModelAndView("redirect:NoUserFound.html");
        }
        loginServiceLocal.deleteActivationLink(activationLink.getObjectID());
        AuthDetails authDetails = new AuthDetails(activationLink.getCompanyId(), activationLink.getUserId(), dataBaseName);
        SecurityContext.getInstance().setDatabase(dataBaseName);
        SecurityContext.getInstance().setCompanyId(activationLink.getCompanyId());
        ActivateAccount employee = loginServiceLocal.getActiveAccount(authDetails.getUserID(), authDetails.getCompanyID());

        if (employee == null) {//if user not found show an error
            System.out.println("==========================ACTIVATION NOT FOUND USER:");
            System.out.println("KEY: " + key);
            System.out.println("DATABASE: " + dataBaseName);
            System.out.println("COMPANY_ID: " + activationLink.getCompanyId());
            System.out.println("USER_ID: " + activationLink.getUserId() + "=");
            return new ModelAndView("redirect:NoUserFound.html");            
        }
        if (employee.isActive() && !Boolean.valueOf(changePass)) {
            try {
                if (request.getServerName().contains("localhost")) {
                    response.sendRedirect("http://" + request.getServerName() + ":" + request.getServerPort());
                } else {
                    response.sendRedirect("http://" + request.getServerName());
                }
            } catch (Exception ignored) {
            }
            return new ModelAndView("redirect:index.html");
        }
        try {
            ServerSecurityContext.getInstance().setCompanyId(authDetails.getCompanyID());
            String sessionId = sessionService.obtainSession(authDetails);

            ServerUtils.removeCookie(SESSION_ID, response);

            Cookie sessionCookie = new Cookie(SESSION_ID_COOKIE, sessionId);
            sessionCookie.setPath("/");
            response.addCookie(sessionCookie);



            //Activate Lead Company

            Integer leadSignUpCompany = EdsContextParams.getLeadSignUpCompany();
            if (leadSignUpCompany != null) {
                CompanyData cData = new CompanyData();
                cData.setCompanyId(authDetails.getCompanyID());
                cData.setRating("Activated");
                rabbitMQService.sendCompanySettingsUpdate(cData, leadSignUpCompany);
            }
            //End Of Activate Lead Company

            //## set auth info to REDIS
            RedisClient.setKey(sessionId, new AuthInfoItem().buildForBasicLogin(employee.getLogin(), null), AuthInfoItem.class);
            ModelAndView model = new ModelAndView("redirect:/auth/changePassword.html");
            model.addObject("adPassEnabled", adPassEnabled);
            return model;
        } catch (Exception ex) {
            return new ModelAndView("redirect:BadUrlError.html");
        }
    }

    public Map<String, String> splitQuery(String queryParams) {
        try {
            if (StringUtils.isEmpty(queryParams)) {
                return Collections.emptyMap();
            }
            return Arrays.stream(queryParams.split("&"))
                    .map(this::splitQueryParameter).collect(Collectors.toMap(AbstractMap.SimpleImmutableEntry::getKey, AbstractMap.SimpleImmutableEntry::getValue));
        } catch (Exception e) {
            System.out.println(queryParams);
            e.printStackTrace();
            return new LinkedHashMap<>();
        }
    }

    public AbstractMap.SimpleImmutableEntry<String, String> splitQueryParameter(String it) {
        final int idx = it.indexOf("=");
        final String key = idx > 0 ? it.substring(0, idx) : it;
        final String value = idx > 0 && it.length() > idx + 1 ? it.substring(idx + 1) : null;
        return new AbstractMap.SimpleImmutableEntry<>(key, value);
    }

}
