package com.edatasite.workforce.gwt.core.server.office365.controllers;

import com.edatasite.workforce.core.domain.EdsRecurrence;
import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;
import com.edatasite.workforce.gwt.core.client.rpc.UserCompanyDTO;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.controllers.login.BaseLoginController;
import com.edatasite.workforce.gwt.core.server.db.RecurrenceManager;
import com.edatasite.workforce.gwt.core.server.office365.constants.Office365Constants;
import com.edatasite.workforce.gwt.core.server.office365.managers.Office365AuthManager;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365AccessTokenDTO;
import com.edatasite.workforce.gwt.core.server.office365.resources.Office365User;
import com.edatasite.workforce.gwt.core.server.office365.services.Office365AuthService;
import com.edatasite.workforce.gwt.core.server.office365.services.Office365ContactService;
import com.edatasite.workforce.gwt.core.server.office365.utils.Office365Utils;
import com.edatasite.workforce.gwt.office365.client.rpc.Office365CalendarService;
import com.edatasite.workforce.gwt.profile.client.rpc.IntegrationItem;
import com.edatasite.workforce.gwt.profile.client.rpc.ProfileService;
import com.edatasite.workforce.utils.EdsContextParams;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant.DAILY_PATTERN_OPTION_INTERVAL;
import static com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant.NO_END_DATE;
import static com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant.RECURRENCE_TYPE_MINUTELY;
import static com.edatasite.workforce.gwt.core.client.ui.SchedulerConstant.SYNCHRONIZE_OFFICE_CALENDAR;

/**
 * Created by umakarimov on 9/30/15.
 */
@Controller
@RequestMapping(value = Office365Constants.AUTH_PAGE)
public class OfficeAuth365Controller extends BaseLoginController implements Office365Constants {

    @Autowired
    private Office365AuthManager office365AuthManager;
    @Autowired
    private Office365AuthService office365AuthService;
    @Autowired
    private Office365CalendarService calendarService;
    @Autowired
    private Office365ContactService office365ContactService;
    @Autowired
    private ProfileService profileService;
    @Autowired
    private RecurrenceManager recurrenceManager;

    @GetMapping
    public void authPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.sendRedirect(office365AuthService.createAccountLoginUrl(request, response));
    }

    @GetMapping(value = AUTH_LINK_PAGE)
    public void authLinkPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String websiteUrl = request.getParameter("website_url");
        ServerUtils.setUserSessionid(request);
        response.sendRedirect(office365AuthService.createAccountLinkUrl(request, response, websiteUrl));
    }

    @GetMapping(value = AUTH_VERIFY_PAGE)
    public void authVerifyPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        ServerUtils.setUserSessionid(request);
        String siteUrl = null;

        for (Cookie cookie : request.getCookies()) {
            if (Office365Constants.WEBSITE_URL_COOKIE.equals(cookie.getName())) {
                siteUrl = cookie.getValue();
                cookie.setValue("");
                break;
            }
        }
        String action = office365AuthService.getActionFromState(request, response);

        if (action == null) {
            response.sendRedirect(Office365Utils.getHostUrl(request));
            return /*this.redirectWithError("Session expired, Please try again")*/;
        }

        Office365AccessTokenDTO token = office365AuthService.acquireAccessToken(Office365Utils.getHostUrl(request), request.getParameter("code"), siteUrl);

        if (token == null) {
            response.sendRedirect(Office365Utils.getHostUrl(request));
            return /*this.redirectWithError("We couldn't fetch your information from Microsoft 365.")*/;
        }

        Office365User me = office365AuthService.getMe(token);

        token.setObjectId(me.getObjectId());

        if (office365AuthService.isLinkAction(action)) {
            this.linkAccount(me, token);

            String redirectUrl = null;
            Cookie[] cookies = request.getCookies();
            for (Cookie cookie : cookies) {
                if (OFFICE_365_DATA_COKIE.equals(cookie.getName()) && OFFICE_365_EVENTS.equals(cookie.getValue())) {
                    redirectUrl = Office365Utils.getHostUrl(request) + URLDecoder.decode("/Crm.html#calendar|add/view/2", StandardCharsets.UTF_8);
                    cookie.setValue("");
                    break;
                }
                if (OFFICE_365_DRIVE_COKIE.equals(cookie.getName()) && !cookie.getValue().isEmpty()) {
                    redirectUrl = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                    cookie.setValue("");
                    break;
                }
                if (OFFICE_365_CONTACT_COKIE.equals(cookie.getName()) && !cookie.getValue().isEmpty()) {
                    redirectUrl = URLDecoder.decode(cookie.getValue(), StandardCharsets.UTF_8);
                    cookie.setValue("");
                    break;
                }
            }
            if (redirectUrl != null) {
                try {
                    response.sendRedirect(redirectUrl);
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        this.createAccount(me, token, request, response);
    }

    public ModelAndView linkAccount(Office365User me, Office365AccessTokenDTO token) {
        UserCompanyDTO userCompany = office365AuthManager.getUserCompany();

        if (userCompany == null) {
            return this.redirectWithError("Please sign in");
        }

        token.setUserId(userCompany.getAuthId());
        token.setCompanyId(userCompany.getCompanyID());
        token = office365AuthManager.saveAccessToken(token, token.issharepoint() ? Constants.OFFICE_365_SHARE_POINT : Constants.OFFICE_365);

        office365AuthManager.assignOfficeUser(token.getUserId(), token.getCompanyId(), token.getObjectId());

        if (!token.issharepoint()) {
            calendarService.saveToken(token);
            office365ContactService.createContactDetails(me.getObjectId(), token);
            IntegrationItem integration = profileService.getIntegrationItem();
            EdsRecurrence recurrence = recurrenceManager.get(integration.getObjectID());
            if (recurrence == null) {
                RecurrenceJobItem item = new RecurrenceJobItem();
                item.setObjectId(integration.getRecurrenceItem().getObjectId());
                item.setJobType(integration.getRecurrenceItem().getJobType());
                item.setEnabled(true);
                item.setType(RECURRENCE_TYPE_MINUTELY);
                item.setBusObjectParams("Synchronize with Office365 Calendar");
                item.setJobType(SYNCHRONIZE_OFFICE_CALENDAR);
                item.setStartDate(new Date());
                item.setEndType(NO_END_DATE);
                item.setDailyPatternOptions(DAILY_PATTERN_OPTION_INTERVAL);
                profileService.saveRecurrenceJob(item);
            }
        }

        return new ModelAndView("redirect:/");
    }

    public ModelAndView createAccount(Office365User me, Office365AccessTokenDTO token, HttpServletRequest request, HttpServletResponse response) throws IOException {
        ModelAndView signInView = this.signIn(me.getObjectId(), request, response);

        if (signInView != null) {
            response.sendRedirect(Office365Utils.getHostUrl(request));
            return signInView;
        }

        if (me.getOtherMails().isEmpty()) {
            response.sendRedirect(Office365Utils.getHostUrl(request));
            return this.redirectWithError("You did't registered any email in your Microsoft 365 Account.");
        }

        String userEmail = null;

        Integer userId = null;
        Integer companyId = null;

        for (String email : me.getOtherMails()) {
            List<UserCompanyDTO> companies = globalAuthJdbcSpringManager.getAuthInfoByUsername(
                    request.getServerName(), email
            );

            if (companies != null && !companies.isEmpty()) {
                UserCompanyDTO company = companies.get(0);

                userEmail = email;
                userId = company.getAuthId();
                companyId = company.getCompanyID();
                break;
            }
        }

        if (userEmail == null) {
            response.sendRedirect(Office365Utils.getHostUrl(request));
            return this.redirectWithError("You did't registered your " + EdsContextParams.getProductName() + " email in your Microsoft 365 Account.");
        }


        token.setUserId(userId);
        token.setCompanyId(companyId);

        token = office365AuthManager.saveAccessToken(token, Constants.OFFICE_365);

        office365AuthManager.sendValidationEmail(
                userEmail, token.getId(), Office365Utils.getHostUrl(request), companyId
        );

        String redirectUrl = null;

        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (OFFICE_365_DATA_COKIE.equals(cookie.getName()) && OFFICE_365_EVENTS.equals(cookie.getValue())) {
                redirectUrl = "/Crm.html#calendar|add/view/2";
            }
        }
        if (redirectUrl != null) {
            try {
                response.sendRedirect(Office365Utils.getHostUrl(request) + URLDecoder.decode(redirectUrl, StandardCharsets.UTF_8));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return this.redirectWithError("We sent you an email with a link in it. Please click that link to verificate that email address.");
    }

    @GetMapping(value = AUTH_EMAIL_VERIFY_PAGE)
    public ModelAndView emailVerifyPage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String secretCode = request.getParameter("s");

        if (StringUtils.isEmpty(secretCode)) {
            return this.redirectWithError("Secret code is empty");
        }

        Office365AccessTokenDTO token = office365AuthManager.getAuthTokenFromSecretCode(secretCode);

        if (token == null) {
            return this.redirectWithError("Secret code is expired, please try again");
        }

        office365AuthManager.assignOfficeUser(token.getUserId(), token.getCompanyId(), token.getObjectId());

        token = office365AuthService.assureAccessToken(Office365Utils.getHostUrl(request), token, Constants.OFFICE_365);

        if (token != null) {
            return this.signIn(token.getObjectId(), request, response);
        } else {
            return null;
        }
    }

    public ModelAndView signIn(String objectId, HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<UserCompanyDTO> companies = office365AuthManager.getAuthInfoByObjectId(objectId, request.getServerName());

        if (companies.size() == 1) {
            return this.signInUserToCompany(companies.get(0), response, request, null);
        } else if (companies.size() > 1) {
            return this.forwardToCompanyChooseForm(FROM_FEDERATED_LOGIN, null, companies, request);
        }

        return null;
    }

    public ModelAndView redirectWithError(String error) {
        return new ModelAndView("redirect:/?error=" + error);
    }
}
