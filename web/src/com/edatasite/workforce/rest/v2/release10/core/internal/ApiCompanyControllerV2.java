package com.edatasite.workforce.rest.v2.release10.core.internal;

import com.edatasite.shared.db.EdsDbException;
import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCompanySystemSettings;
import com.edatasite.workforce.core.domain.EdsUsagePlan;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.client.rpc.UserCompanyDTO;
import com.edatasite.workforce.gwt.core.client.rpc.website.CompanyDomain;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.LoginServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.CompanySystemSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.UsagePlanManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanItem;
import com.edatasite.workforce.gwt.myaccount.server.app.MyAccountServiceLocal;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;


@RestController
@RequestMapping("/internal")
public class ApiCompanyControllerV2 extends BaseApiControllerV2 {
    private static final Logger log = LoggerFactory.getLogger(ApiCompanyControllerV2.class);

    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private CompanySystemSettingsManager companySystemSettingsManager;
    @Autowired
    private UsagePlanManager usagePlanManager;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    private MyAccountServiceLocal myAccountServiceLocal;
    @Autowired
    @Qualifier("loginService")
    private LoginServiceLocal loginService;

    @RequestMapping(value = "/company_logo", method = RequestMethod.GET)
    public String getCompanyLogo(@RequestBody UserCompanyDTO userCompany) throws RestException {
        ServerSecurityContext.getInstance().setDatabase(userCompany.getClusterDbName());
        ServerSecurityContext.getInstance().setCompanyId(userCompany.getCompanyID());
        try {
            String logo = loginService.getCompanyLogoURL(null);
            if (logo == null) {
                logo = Constants.COMPANY_NO_LOGO;
            }
            return logo;
        } catch (Exception e) {
            e.printStackTrace();
        }

        throw new RestException(ERROR_MESSAGE, "Error occurred while sending email notification", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(value = "/forgot_password_notification", method = RequestMethod.POST)
    public Boolean forgotPasswordNotification(@RequestBody UserCompanyDTO userCompany) throws RestException {
        ServerSecurityContext.getInstance().setDatabase(userCompany.getClusterDbName());
        ServerSecurityContext.getInstance().setCompanyId(userCompany.getCompanyID());
        HashMap<Boolean, CompanyDomain> isKpi = new HashMap<>();
        isKpi.put(true, null);
        try {
            return loginService.sendForgotPasswordNotification(userCompany.getUserID(), isKpi);
        } catch (EdsDbException e) {
            e.printStackTrace();
        }

        throw new RestException(ERROR_MESSAGE, "Error occurred while sending email notification", SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @RequestMapping(value = "/filter_user_company", method = RequestMethod.POST)
    public UserCompanyDTO filterUserCompany(@RequestBody UserCompanyDTO userCompanyDTO) {
        ServerSecurityContext.getInstance().setCompanyId(userCompanyDTO.getCompanyID());
        ServerSecurityContext.getInstance().setDatabase(userCompanyDTO.getClusterDbName());
        EdsCompany company = companyManager.get(userCompanyDTO.getCompanyID());
        EdsUser user = null;

        if (company != null) {
            userCompanyDTO.setActive(company.getActive());
            try {
                user = userManager.get(userCompanyDTO.getUserID());
            } catch (Exception e) {
                //schema doesn't exist
            }
        }
        if (company != null && user != null && !user.getDeleted() && Constants.EMPLOYEE_STATUS_ACTIVE.equals(userManager.getUserStatus(user.getObjectID()))) {
            if (Constants.USER_TYPE_BMT_RESPONDENT.equals(user.getUserType())) { // is survey respondent
                return null;
            }
            if (user instanceof EdsClientContact) {
                if (user.getClientContact().getAccess() == null || (user.getClientContact().getAccess() != null && !user.getClientContact().getAccess())) {
                    log.debug("Client contact access is false for user " + user.getObjectID());
                    return null;
                }
            }
            userCompanyDTO.setClusterURL(ServerUtils.getWebURL(userCompanyDTO));
            userCompanyDTO.setCompanyName(company.getName());
            String logo = loginService.getCompanyLogoURL(null);
            if (logo == null) {
                logo = "/no-logo.gif";
            }
            userCompanyDTO.setLogo(logo);
            userCompanyDTO.setFullName(user.getFullName());
            EdsUsagePlan usagePlan = usagePlanManager.getCurrentUsagePlan(company);
            if (usagePlan != null && usagePlan.getPaid()) {
                userCompanyDTO.setStatusName(commonLocalizer.localize("active"));
                userCompanyDTO.setStatus("active");
            } else {
                userCompanyDTO.setStatus("free");
                userCompanyDTO.setStatusName(commonLocalizer.localize("freeTrial"));
            }

            UsagePlanItem usagePlanItem = getUsagePlanItem(usagePlan);
            if (!(usagePlanItem.isCurrSub() && (usagePlanItem.isFree() || usagePlanItem.isPaid()))) {
                userCompanyDTO.setStatus("expired");
                userCompanyDTO.setStatusName(commonLocalizer.localize("expired"));
            }
            EdsCompanySystemSettings companySystemSettings = companySystemSettingsManager.findByCompanyID(userCompanyDTO.getCompanyID());
            if (companySystemSettings != null) {
                userCompanyDTO.setPasswordExpirationDayCount(companySystemSettings.getPasswordExpirationDayCount());
            }
            if (company.isDeleted() == null || !company.isDeleted()) {
                return userCompanyDTO;
            }
        }
        return null;
    }

    private UsagePlanItem getUsagePlanItem(EdsUsagePlan usagePlan) {
        UsagePlanItem result = new UsagePlanItem();
        if (usagePlan != null) {
            UsagePlanItem item = myAccountServiceLocal.getParametr(usagePlan);
            result.setFree(item.isFree());
            result.setPaid(usagePlan.getPaid());
            result.setCurrSub(true);
        } else {
            result.setCurrSub(false);
            result.setPaid(false);
            result.setFree(true);
        }
        return result;
    }

}
