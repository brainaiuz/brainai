package com.edatasite.workforce.gwt.core.server.app.hmrc.service.impl;

import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.core.client.rpc.ApiAccessToken;
import com.edatasite.workforce.gwt.core.client.rpc.ApprovalListResult;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.AllInOneServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.NotificationMsgServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.hmrc.service.UserService;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.enums.NotificationTypeEnum;
import com.edatasite.workforce.rest.v2.release10.core.to.auth.PhoneTO;
import com.edatasite.workforce.rest.v2.release10.core.to.auth.RoleTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.SelectItemTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.userprofile.SubscriptionInfo;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.userprofile.UserTO;
import com.edatasite.workforce.rest.v2.release10.enums.UserTypeEnum;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.core.to.IdName;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.LanguagesDto;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static com.edatasite.workforce.rest.base.helpers.ApiConstants.*;

@Service("userService")
public class UserServiceImpl implements UserService {
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final SimpleDateFormat zonedDatetimeFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);
    @Autowired
    private UserManager userManager;
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    private AllInOneServiceLocal allInOneServiceLocal;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private NotificationMsgServiceLocal notificationMsgServiceLocal;
    @Autowired
    private UsagePlanManager usagePlanManager;
    @Autowired
    private CountryManager countryManager;
    @Autowired
    private CompanySystemSettingsManager companySystemSettingsManager;
    @Autowired
    private SpokenLanguagesManager spokenLanguagesManager;

    @Override
    public UserTO getUserProfile(String accessToken) throws RestException {
        return getUserProfile(accessToken, null);
    }

    private String resolveUserType(EdsUser currentUser, ApiAccessToken apiAccessToken) {
        if (!StringUtils.isNotBlank(apiAccessToken.getModuleCode())
                || !apiAccessToken.getModuleCode().equals(PermissionConstants.HRMS_MODULE)) {
            return null;
        }
        ApprovalListResult approvalListResult = allInOneServiceLocal.getApprovers(RelationItem.TYPE_LEAVE_REQUEST, null, true, currentUser.getObjectID(), false);
        for (ApproverItem approverItem : approvalListResult.getList()) {
            if (approverItem.getRoles() != null
                    && approverItem.getRoles().stream()
                    .map(SelectItem::getDescription)
                    .anyMatch(currentUser::hasRole)) {
                return UserTypeEnum.MANAGER.getType();
            }
            if (approverItem.getEmployees() != null
                    && approverItem.getEmployees().stream()
                    .map(SelectItem::getId)
                    .anyMatch(e -> currentUser.getObjectID().equals(e))) {
                return UserTypeEnum.MANAGER.getType();
            }
        }
        return UserTypeEnum.USER.getType();
    }

    private PhoneTO resolvePhoneNumber(EdsEmployeeProfile profile) {
        if (profile == null) return null;
        try {
            EdsCrmContact contact = profile.getContact();
            if (contact == null || !StringUtils.isNotBlank(contact.getPrimaryPhone())) {
                return null;
            }
            List<EdsCountry> countryList = countryManager.list();
            for (EdsCountry country : countryList) {
                if (StringUtils.isNotBlank(country.getTelCode()) && contact.getPrimaryPhone().startsWith(country.getTelCode())) {
                    String telCode = country.getTelCode();
                    String phone = contact.getPrimaryPhone().substring(telCode.length());
                    return new PhoneTO(telCode, phone);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    private SubscriptionInfo resolveSubscription(EdsCompany company) {
        EdsUsagePlan usagePlan = usagePlanManager.getCurrentUsagePlan(company);

        if (usagePlan == null) {
            usagePlan = usagePlanManager.getLastUsagePlan(company.getObjectID());
        }

        if (usagePlan == null) {
            return null;
        }
        SubscriptionInfo subscriptionInfo = new SubscriptionInfo();

        if (usagePlan.getPeriodType() != null) {
            if (EdsUsagePlan.FREE_TRIAL.equalsIgnoreCase(usagePlan.getPeriodType().getCode())) {
                subscriptionInfo.setSubscription_type("TRIAL");
            } else {
                subscriptionInfo.setSubscription_type("REGULAR");
            }
        }
        if (usagePlan.getEndDate() != null) {
            subscriptionInfo.setExpiration_date(zonedDatetimeFormat.format(usagePlan.getEndDate()));

            Calendar cal1 = new GregorianCalendar();
            cal1.setTime(new Date());
            cal1.set(Calendar.HOUR, 0);
            cal1.set(Calendar.MINUTE, 0);
            cal1.set(Calendar.SECOND, 0);
            cal1.set(Calendar.MILLISECOND, 0);

            Calendar cal2 = new GregorianCalendar();
            cal2.setTime(usagePlan.getEndDate());
            cal2.set(Calendar.HOUR, 0);
            cal2.set(Calendar.MINUTE, 0);
            cal2.set(Calendar.SECOND, 0);
            cal2.set(Calendar.MILLISECOND, 0);
            int daysLeft = (int) ((cal2.getTimeInMillis() - cal1.getTimeInMillis()) / (1000 * 60 * 60 * 24));

            subscriptionInfo.setIs_active(daysLeft > 0);
            subscriptionInfo.setDays_left(Math.max(daysLeft, 0));
        }
        return subscriptionInfo;
    }

    protected static ArrayList<NotificationTypeEnum> getEntityTypes(String moduleCode) {
        if (StringUtils.isBlank(moduleCode) || PermissionConstants.ALL.equals(moduleCode) || PermissionConstants.DOCUMENTS_CONTEXT.equals(moduleCode)) {
            return null;
        }
        ArrayList<NotificationTypeEnum> entityList = new ArrayList<>();
        if (PermissionConstants.HRMS_MODULE.equals(moduleCode)) {
            entityList.add(NotificationTypeEnum.BenefitRequest);
            entityList.add(NotificationTypeEnum.LeaveRequests);
            entityList.add(NotificationTypeEnum.TimeSheetDueReminder);
            entityList.add(NotificationTypeEnum.TaskAssignee);
            entityList.add(NotificationTypeEnum.TaskDueReminder);
            entityList.add(NotificationTypeEnum.ExpenseClaim);
            entityList.add(NotificationTypeEnum.MeetingMinutesNotification);
            entityList.add(NotificationTypeEnum.ProjectDueReminder);
            entityList.add(NotificationTypeEnum.WorkstreamDueReminder);
            entityList.add(NotificationTypeEnum.Employee);
            entityList.add(NotificationTypeEnum.ProjectApproval);
            entityList.add(NotificationTypeEnum.Candidate);
            entityList.add(NotificationTypeEnum.CashAdvance);
            entityList.add(NotificationTypeEnum.OnboardingStep);
            entityList.add(NotificationTypeEnum.TimeSheetApproval);
            entityList.add(NotificationTypeEnum.GoogleCalendarEvent);
            entityList.add(NotificationTypeEnum.InvoiceDueReminder);
            return entityList;
        }
        if (PermissionConstants.CRM_MODULE.equals(moduleCode)) {
            entityList.add(NotificationTypeEnum.CRMCase);
            entityList.add(NotificationTypeEnum.LeadAssignee);
            entityList.add(NotificationTypeEnum.SaleInvoiceApproval);
            entityList.add(NotificationTypeEnum.CRMContact);
            entityList.add(NotificationTypeEnum.PurchaseOrder);
            entityList.add(NotificationTypeEnum.CrmOpportunity);
            entityList.add(NotificationTypeEnum.SalesQuote);
            return entityList;
        }

        return null;
    }
    @Override
    public UserTO getUserProfile(String accessToken, Integer id) throws RestException {
        EdsUser user;
        try {
            user = id != null ? userManager.get(id) : userManager.getUser();
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RestException("Session expired", "Session expired", EXPIRED, HttpStatus.UNAUTHORIZED);
        }

        if (user == null) {
            throw new RestException("Session expired", "Session expired", EXPIRED, HttpStatus.UNAUTHORIZED);
        }
        ApiAccessToken apiAccessToken = globalAuthJdbcSpringManager.getApiAccessToken(accessToken);
        try {
            EdsCompany company = user.getCompany();

            UserTO userTO = new UserTO();
            userTO.setId(user.getObjectID());
            userTO.setEmail(user.getEmail());
            userTO.setUser_name(user.getName());
            userTO.setFirstName(user.getFirstName());
            userTO.setMiddleName(user.getMiddleName());
            userTO.setLastName(user.getLastName());
            userTO.setCompany_name(company.getName());
            if (user.getLocation() != null) {
                userTO.setLocationId(user.getLocation().getObjectID());
            }
            if (user.isEmployee() && user.getEmployee().getBirthDay() != null) {
                userTO.setBirthDate(new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE).format(user.getEmployee().getBirthDay()));
            }
            if (user.getRoles() != null && !user.getRoles().isEmpty()) {
                userTO.setRoles(user.getRoles().stream().map(role -> new RoleTO(role.getObjectID(), role.getCode(), role.getName(), role.getSystem())).collect(Collectors.toList()));
            }
            if (user.isEmployee() && user.getEmployee().getPosition() != null) {
                EdsPosition position = user.getEmployee().getPosition();
                userTO.setPosition(new SelectItemTO(position.getObjectID(), position.getName(), position.getCode()));
            }
            userTO.setCompanyId(company.getObjectID());
            String userType = resolveUserType(user, apiAccessToken);
            userTO.setUser_type(userType);

            if (user.getPhoto() != null) {
                userTO.setAvatar_image(commonServiceLocal.getImageUrl(user.getPhoto().getObjectID()));
            } else {
                userTO.setAvatar_image(Constants.DEFAULT_USER_PROFILE_PHOTO);
            }

            ArrayList<NotificationTypeEnum> entityTypes = getEntityTypes(apiAccessToken.getModuleCode());
            userTO.setUnread_notifications(notificationMsgServiceLocal.getNewNotifications(entityTypes));

            SubscriptionInfo subscriptionInfo = resolveSubscription(company);
            userTO.setSubscription_info(subscriptionInfo);

            PhoneTO phoneTO = resolvePhoneNumber(user.getEmployee().getProfile());
            userTO.setPhone(phoneTO);
            userTO.setLang(ServerUtils.getUserLocale().getLanguage());
            Optional.ofNullable(user.getAccountStatus())
                    .map(EdsReference::getCode)
                    .ifPresent(as -> userTO.addProperty("accountStatusCode", as));
            Optional.of(company.getTestCompany())
                    .filter(Boolean::booleanValue)
                    .ifPresent(tc -> userTO.addProperty("testCompany", tc));
            userTO.setCountryZoneId(company.getCountryZone().getObjectID());
            EdsCompanySystemSettings companySystemSettings = companySystemSettingsManager.findByCompanyID(company.getObjectID());
            if (companySystemSettings != null) {
                userTO.setNameOrder(companySystemSettings.getNameOrder());
            }
            userTO.setOverallDatePickerWeekStart(companySystemSettings.getOverallDatePickerWeekStart());
            EdsCompanySettings companySettings = company.getCompanySettings();
            if (companySettings != null) {
                userTO.setShortDateFormat(companySettings.getShortDateFormat());
                userTO.setLongDateFormat(companySettings.getLongDateFormat());
            }
            ArrayList<EdsSpokenLanguages> spokenLanguages = spokenLanguagesManager.getListByRelation(user.getObjectID(), EdsSpokenLanguages.TYPE_EMPLOYEE);
            ArrayList<LanguagesDto> languages = new ArrayList<>();
            for (EdsSpokenLanguages spokenLanguage : spokenLanguages) {
                LanguagesDto languagesDto = new LanguagesDto();
                if (spokenLanguage.getLanguage() != null) {
                    languagesDto.setLanguage(new IdName(spokenLanguage.getLanguage().getObjectID(), spokenLanguage.getLanguage().getName()));
                }
                if (spokenLanguage.getLevel() != null) {
                    languagesDto.setLevel(new IdName(spokenLanguage.getLevel().getObjectID(), spokenLanguage.getLevel().getName()));
                }
                languages.add(languagesDto);
            }
            userTO.setLanguages(languages);

            return userTO;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
