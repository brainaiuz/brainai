package com.edatasite.workforce.gwt.core.server.controllers;

import com.edatasite.shared.db.EdsDbException;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.core.client.rpc.UserCompanyDTO;
import com.edatasite.workforce.gwt.core.client.rpc.website.CompanyDomain;
import com.edatasite.workforce.gwt.core.server.app.LoginServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.Utils;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * User: Sherali
 * Date: 14.10.2008
 * Time: 13:10:17
 */
@Controller
public class ForgotPasswordController {
    @Qualifier("loginService")
    @Autowired
    private LoginServiceLocal loginServiceLocal;

    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    private CompanyManager companyManager;

    private MessageSource messageSource;

    @Autowired
    @Qualifier(value = "messageSource")
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @RequestMapping(value = "/forgotPassword.html", method = RequestMethod.GET)
    public ModelAndView forgotPassword(HttpServletRequest request) {
        ServerUtils.fillHostParameters(request);
        if (request.getHeader("host").contains("cooconnect")) {
            return new ModelAndView("cooForgotPassword");
        } else if (request.getHeader("host").contains("atmonitor")) {
            return new ModelAndView("atmForgotPassword");
        } else if (request.getHeader("host").contains("vaival")) {
            return new ModelAndView("vaivalForgotPassword");
        } else if (request.getHeader("host").contains("victechweb")) {
            return new ModelAndView("victechWebForgotPassword");
        } else if (request.getHeader("host").contains("logotime")) {
            return new ModelAndView("logotimeForgotPassword");
        } else if (request.getHeader("host").contains("spritelab")) {
            return new ModelAndView("spritelabForgotPassword");
        } else if (request.getHeader("host").contains("icomtech")) {
            return new ModelAndView("icomtechForgotPassword");
        } else if (request.getHeader("host").contains("kmrsi")) {
            return new ModelAndView("kmrsiForgotPassword");
        } else if (request.getHeader("host").contains("postroomservices")) {
            return new ModelAndView("postRoomForgotPassword");
        } else if (request.getHeader("host").contains("stefano")) {
            return new ModelAndView("stefanoForgotPassword");
        } else if (Utils.isNewKpi(request)) {
            return new ModelAndView("newKpiForgotPassword");
        }else if (Utils.isPraaktis(request) || Utils.isGym(request)) {
            return new ModelAndView("praaktisGoForgotPassword");
        } else if (Utils.isCspace(request)) {
            return new ModelAndView("cspaceForgotPassword");
        } else if (Utils.isBrain(request)) {
            return new ModelAndView("brainForgotPassword");
        } else {
            return new ModelAndView("forgotPassword");
        }
    }

    @RequestMapping(value = "/forgotPassword.html", method = RequestMethod.POST)
    public ModelAndView forgotPassword(HttpServletRequest request,
                                       HttpServletResponse response) throws Exception {
        ServerUtils.fillHostParameters(request);
        String email = ServletRequestUtils.getStringParameter(request, "email");
        String reEmail = email.replace(" ", "");
        String defaultLocale = request.getAttribute("defaultLocale").toString();
        String language = request.getParameter("language");
        Locale locale = new Locale(defaultLocale);
        String required = messageSource.getMessage("email_required", null, "Email address is required", locale);
        String notFound = messageSource.getMessage("email_notfound", null, "Email address not found!", locale);
        String error = messageSource.getMessage("error", null, "Error", locale);

        if (!EMAIL_PATTERN.matcher(reEmail).matches()) {
            if (request.getHeader("host").contains("cooconnect")) {
                return new ModelAndView("cooForgotPassword", "message", "Email address is required");
            } else if (request.getHeader("host").contains("atmonitor")) {
                return new ModelAndView("atmForgotPassword", "message", "Email address is required");
            } else if (request.getHeader("host").contains("vaival")) {
                return new ModelAndView("vaivalForgotPassword", "message", "Email address is required");
            } else if (request.getHeader("host").contains("victechweb")) {
                return new ModelAndView("victechWebForgotPassword", "message", "Email address is required");
            } else if (request.getHeader("host").contains("logotime")) {
                return new ModelAndView("logotimeForgotPassword", "message", "Email address is required");
            } else if (request.getHeader("host").contains("spritelab")) {
                return new ModelAndView("spritelabForgotPassword", "message", "Email address is required");
            } else if (request.getHeader("host").contains("icomtech")) {
                return new ModelAndView("icomtechForgotPassword", "message", "Email addres is required");
            } else if (request.getHeader("host").contains("kmrsi")) {
                return new ModelAndView("kmrsiForgotPassword", "message", "Email address is required");
            } else if (request.getHeader("host").contains("postroomservices")) {
                return new ModelAndView("postRoomForgotPassword", "message", "<div style=\"color:red;font-size:10px;margin-top:-14px;\"><b>Email address is required</b></div>");
            } else if (request.getHeader("host").contains("stefano")) {
                return new ModelAndView("stefanoForgotPassword", "message", "Email address is required");
            } else if (Utils.isNewKpi(request)){
                return new ModelAndView("newKpiForgotPassword", "message", "Email address is required");
            }  else if (Utils.isCspace(request)) {
                return new ModelAndView("cspaceForgotPassword", "message", "Email address is required");
            }  else if (Utils.isBrain(request)) {
                return new ModelAndView("brainForgotPassword", "message", "Email address is required");
            } else {
                return new ModelAndView("forgotPassword", "message", required);
            }
        } else {
            boolean isSend = sendForgotPasswordNotification(request.getServerName(), reEmail);
            if (!isSend) {
                if (request.getHeader("host").contains("cooconnect")) {
                    return new ModelAndView("cooForgotPassword", "message", "Email address not found!");
                } else if (request.getHeader("host").contains("atmonitor")) {
                    return new ModelAndView("atmForgotPassword", "message", "Email address not found!");
                } else if (request.getHeader("host").contains("vaival")) {
                    return new ModelAndView("vaivalForgotPassword", "message", "Email address not found!");
                } else if (request.getHeader("host").contains("vaival")) {
                    return new ModelAndView("victechWebForgotPassword", "message", "Email address not found!");
                } else if (request.getHeader("host").contains("logotime")) {
                    return new ModelAndView("logotimeForgotPassword", "message", "Email address not found!");
                } else if (request.getHeader("host").contains("spritelab")) {
                    return new ModelAndView("spritelabForgotPassword", "message", "Email address not found!");
                } else if (request.getHeader("host").contains("icomtech")) {
                    return new ModelAndView("icomtechForgotPassword", "message", "Email addres not found!");
                } else if (request.getHeader("host").contains("kmrsi")) {
                    return new ModelAndView("kmrsiForgotPassword", "message", "Email address not found!");
                } else if (request.getHeader("host").contains("postroomservices")) {
                    return new ModelAndView("postRoomForgotPassword", "message", "<div style=\"color:red;font-size:10px;margin-top:-14px;\"><b>Email address not found!</b></div>");
                } else if (request.getHeader("host").contains("stefano")) {
                    return new ModelAndView("stefanoForgotPassword", "message", "Email address is required");
                } else if (Utils.isNewKpi(request)){
                    return new ModelAndView("newKpiForgotPassword", "message", "Email address not found!");
                } else if (Utils.isCspace(request)) {
                    return new ModelAndView("cspaceForgotPassword", "message", "Email address not found!");
                } else if (Utils.isBrain(request)) {
                    return new ModelAndView("brainForgotPassword", "errorMessageKey", "not_found");
                } else {
                    return new ModelAndView("forgotPassword", "message", notFound);
                }
            }
            if (request.getHeader("host").contains("cooconnect")) {
                return new ModelAndView("successPassword", "email", "Error");
            } else if (request.getHeader("host").contains("atmonitor")) {
                return new ModelAndView("atmPasswordSuccess", "email", "Error");
            } else if (request.getHeader("host").contains("vaival")) {
                return new ModelAndView("vaivalPasswordSuccess", "email", "Error");
            } else if (request.getHeader("host").contains("victechweb")) {
                return new ModelAndView("victechWebPasswordSuccess", "email", "Error");
            } else if (request.getHeader("host").contains("logotime")) {
                return new ModelAndView("logotimePasswordSuccess", "email", "Error");
            } else if (request.getHeader("host").contains("spritelab")) {
                return new ModelAndView("spritelabPasswordSuccess", "email", "Error");
            } else if (request.getHeader("host").contains("icomtech")) {
                return new ModelAndView("icomtechForgotPassword", "email", "Error");
            } else if (request.getHeader("host").contains("kmrsi")) {
                return new ModelAndView("kmrsiForgotPassword", "email", "Error");
            } else if (request.getHeader("host").contains("postroomservices")) {
                return new ModelAndView("postRoomForgotPassword", "message", "<div style=\"font-size:10px;margin-top:-14px;\"><b>Password Reminder Success</b></div>");
            } else if (request.getHeader("host").contains("stefano")) {
                return new ModelAndView("stefanoForgotPassword", "message", "Error");
            } else if (Utils.isNewKpi(request)){
                return new ModelAndView("newKpiPassSuccess");
            } else if (Utils.isCspace(request)) {
                return new ModelAndView("cspaceForgotPassword", "message", "Error");
            } else if (Utils.isBrain(request)) {
                ModelAndView mav = new ModelAndView("brainForgotPasswordSuccess");
                mav.addObject("language", language);
                return new ModelAndView("brainForgotPasswordSuccess");
            } else {
                return new ModelAndView("passSuccess", "email", error);
            }
        }
    }

    public boolean sendForgotPasswordNotification(String domainName, String email) throws EdsDbException {
        List<UserCompanyDTO> users = globalAuthJdbcSpringManager.getUserCompanyByEmail(domainName, email);
        for (UserCompanyDTO userCompany : users) {
            ServerSecurityContext.getInstance().setDatabase(userCompany.getClusterDbName());
            ServerSecurityContext.getInstance().setCompanyId(userCompany.getCompanyID());
            if (companyManager.schemaExists(String.valueOf(userCompany.getCompanyID()))) {//check for deleted schema
                Map<Boolean, CompanyDomain> isKpi = new HashMap<>();
                isKpi.put(true, null);
                boolean isSend = loginServiceLocal.sendForgotPasswordNotification(userCompany.getUserID(), isKpi);
                if (isSend) {
                    ServerSecurityContext.getInstance().setDatabase("");
                    ServerSecurityContext.getInstance().removeCompanyId();
                    return true;
                }
            }
        }
        ServerSecurityContext.getInstance().setDatabase("");
        ServerSecurityContext.getInstance().removeCompanyId();
        return false;
    }

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^\\w+([_.-]\\w+)*@(\\w+([_.-]\\w+)*)");
}
