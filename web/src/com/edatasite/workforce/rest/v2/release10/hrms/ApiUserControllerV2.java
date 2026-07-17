package com.edatasite.workforce.rest.v2.release10.hrms;

import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.crm.contact.EdsCrmContact;
import com.edatasite.workforce.core.domain.payrolluk.EdsCompanyPayrollSettings;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.contact.client.rpc.ContactListItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.core.client.rpc.*;
import com.edatasite.workforce.gwt.core.client.rpc.approvers.ApproverItem;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.customfields.ViewName;
import com.edatasite.workforce.gwt.core.server.actions.CreateDocumentCommand;
import com.edatasite.workforce.gwt.core.server.app.*;
import com.edatasite.workforce.gwt.core.server.db.*;
import com.edatasite.workforce.gwt.core.server.db.payroll.CompanyPayrollSettingsManager;
import com.edatasite.workforce.gwt.core.server.servlets.WfmMultipartFile;
import com.edatasite.workforce.gwt.core.server.utils.CustomFieldsUtils;
import com.edatasite.workforce.gwt.employee.server.app.EmployeeServiceLocal;
import com.edatasite.workforce.gwt.hrms.server.app.HrmsServiceLocal;
import com.edatasite.workforce.gwt.task.server.app.TaskServiceLocal;
import com.edatasite.workforce.rest.base.helpers.ApiConstants;
import com.edatasite.workforce.rest.v2.release10.core.BaseApiControllerV2;
import com.edatasite.workforce.rest.v2.release10.core.to.auth.PhoneTO;
import com.edatasite.workforce.rest.v2.release10.core.to.auth.RoleTO;
import com.edatasite.workforce.rest.v2.release10.core.to.base.*;
import com.edatasite.workforce.rest.v2.release10.core.to.crm.AddressTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.*;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.userprofile.SubscriptionInfo;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.userprofile.UserDetailsTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.userprofile.UserTO;
import com.edatasite.workforce.rest.v2.release10.core.to.hrms.userprofile.UserUpdateTO;
import com.edatasite.workforce.rest.v2.release10.core.to.permission.PermissionListTO;
import com.edatasite.workforce.rest.v2.release10.core.to.permission.PermissionTO;
import com.edatasite.workforce.rest.v2.release10.enums.*;
import com.edatasite.workforce.rest.v2.release10.exp.RestException;
import com.edatasite.workforce.rest.v3.release10.hrms.dto.mygov.MyGovPassportResponseDto;
import com.edatasite.workforce.utils.EdsContextParams;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Dilsh0d on 9/25/2017.
 */
@Tag(name = "User", description = "User API")
@RestController
@RequestMapping(headers = {ApiConstants.ACCESS_TOKEN, ApiConstants.X_AUTH},
        produces = {MediaType.APPLICATION_JSON_UTF8_VALUE},
        consumes = {MediaType.ALL_VALUE})
public class ApiUserControllerV2 extends BaseApiControllerV2 implements ApiConstants {

    private static final Logger log = LoggerFactory.getLogger(ApiUserControllerV2.class);

    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private UsagePlanManager usagePlanManager;
    @Autowired
    private CountryManager countryManager;
    @Autowired
    private AllInOneServiceLocal allInOneServiceLocal;
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private EmployeeServiceLocal employeeServiceLocal;
    @Autowired
    private HrmsServiceLocal hrmsServiceLocal;
    @Autowired
    private UserEmailSettingsManager userEmailSettingsManager;
    @Autowired
    private NotificationMsgServiceLocal notificationMsgServiceLocal;
    /*@Autowired
    private CreateAttachmentHandler createAttachmentHandler;*/
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private TaskServiceLocal taskServiceLocal;
    @Autowired
    private CompanyPayrollSettingsManager companyPayrollSettingsManager;
    @Autowired
    private CrmContactManager candidateManager;

    @Operation(summary = "Get User Profile", description = "1) user_type (string) - The User type can be the followings: \n\n USER - is a regular user and cannot accept or reject applications from other users \n\n MANAGER (HR manager e.g) - can accept or reject applications from other users. It becomes active if the current user has APPROVE access to at least one of the existing application types ")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have user profile info")})
    @RequestMapping(value = "/user_profile", method = RequestMethod.GET)
    @Transactional
    public Object getUserProfile() throws RestException {
        return getUserProfile(null);
    }

    private Object getUserProfile(Integer userId) throws RestException {
        EdsUser currentUser;
        if (userId != null) {
            currentUser = userManager.get(userId);
        } else {
            try {
                currentUser = userManager.getUser();
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new RestException("Session expired", "Session expired", EXPIRED, HttpStatus.UNAUTHORIZED);
            }
        }
        if (currentUser == null) {
            throw new RestException("Session expired", "Session expired", EXPIRED, HttpStatus.UNAUTHORIZED);
        }
        String accessToken = httpServletRequest.getHeader(ApiConstants.ACCESS_TOKEN);
        ApiAccessToken apiAccessToken = globalAuthJdbcSpringManager.getApiAccessToken(accessToken);
        try {
            EdsCompany company = currentUser.getCompany();

            UserTO userTO = new UserTO();
            userTO.setId(currentUser.getObjectID());
            userTO.setEmail(currentUser.getEmail());
            userTO.setUser_name(currentUser.getName());
            userTO.setFirstName(currentUser.getFirstName());
            userTO.setMiddleName(currentUser.getMiddleName());
            userTO.setLastName(currentUser.getLastName());
            userTO.setCompany_name(company.getName());
            if (currentUser.isEmployee() && currentUser.getEmployee().getBirthDay() != null) {
                userTO.setBirthDate(new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE).format(currentUser.getEmployee().getBirthDay()));
            }
            if (currentUser.getRoles() != null && !currentUser.getRoles().isEmpty()) {
                userTO.setRoles(currentUser.getRoles().stream().map(role -> new RoleTO(role.getCode(), role.getName(), role.getSystem())).collect(Collectors.toList()));
            }
            if (currentUser.isEmployee() && currentUser.getEmployee().getPosition() != null) {
                EdsPosition position = currentUser.getEmployee().getPosition();
                userTO.setPosition(new SelectItemTO(position.getObjectID(), position.getName(), position.getCode()));
            }
            userTO.setCompanyId(company.getObjectID());
            if (StringUtils.isNotBlank(apiAccessToken.getModuleCode())
                    && apiAccessToken.getModuleCode().equals(PermissionConstants.HRMS_MODULE)) {
                ApprovalListResult approvalListResult = allInOneServiceLocal.getApprovers(RelationItem.TYPE_LEAVE_REQUEST, null, true, currentUser.getObjectID(), false);
                for (ApproverItem approverItem : approvalListResult.getList()) {
                    if (approverItem.getRoles() != null) {
                        for (SelectItem role : approverItem.getRoles()) {
                            if (currentUser.hasRole(role.getDescription())) {
                                userTO.setUser_type(UserTypeEnum.MANAGER.getType());
                                break;
                            }
                        }
                    }
                    if (approverItem.getEmployees() != null) {
                        for (SelectItem employee : approverItem.getEmployees()) {
                            if (currentUser.getObjectID().equals(employee.getId())) {
                                userTO.setUser_type(UserTypeEnum.MANAGER.getType());
                                break;
                            }
                        }
                    }
                }

                if (userTO.getUser_type() == null) {
                    userTO.setUser_type(UserTypeEnum.USER.getType());
                }
            }
            //Set User Photo URL
            if (currentUser.getPhoto() != null) {
                userTO.setAvatar_image(commonServiceLocal.getImageUrl(currentUser.getPhoto().getObjectID()));
            } else {
                userTO.setAvatar_image(Constants.DEFAULT_USER_PROFILE_PHOTO);
            }
            //Set Unread Notifications count
            userTO.setUnread_notifications(notificationMsgServiceLocal.getNewNotifications(ApiNotificationControllerV2.getEntityTypes(apiAccessToken.getModuleCode())));

            SimpleDateFormat longDateTimezoneFormat = new SimpleDateFormat(FORMAT_WITH_DATETIME_AND_TIMEZONE);
            //Set Trial Info
            /*TrialInfoTO trialInfoTO = new TrialInfoTO();
            trialInfoTO.setIs_active(false);
            if (freeUsage != null && usagePlan != null && usagePlan.getObjectID().equals(freeUsage.getObjectID())) {
                trialInfoTO.setTrial_end_date(longDateTimezoneFormat.format(usagePlan.getEndDate()));
                trialInfoTO.setIs_active(true);
            }
            userTO.setTrial_info(trialInfoTO);*/

            //Subscription Info
            EdsUsagePlan usagePlan = usagePlanManager.getCurrentUsagePlan(company);
            //EdsReference periodType = referenceManager.findReference(EdsUsagePlan._PERIOD_TYPE, EdsUsagePlan.FREE_TRIAL);
            //EdsUsagePlan freeUsage = usagePlanManager.getFreeTrialUsagePlanCompany(periodType, company);
            if (usagePlan == null) {
                usagePlan = usagePlanManager.getLastUsagePlan(company.getObjectID());
            }

            if (usagePlan != null) {
                SubscriptionInfo subscriptionInfo = new SubscriptionInfo();

                if (usagePlan.getPeriodType() != null) {
                    if (EdsUsagePlan.FREE_TRIAL.equalsIgnoreCase(usagePlan.getPeriodType().getCode())) {
                        subscriptionInfo.setSubscription_type("TRIAL");
                    } else {
                        subscriptionInfo.setSubscription_type("REGULAR");
                    }
                }
                if (usagePlan.getEndDate() != null) {
                    subscriptionInfo.setExpiration_date(longDateTimezoneFormat.format(usagePlan.getEndDate()));

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
                userTO.setSubscription_info(subscriptionInfo);
            }
            //End Of Subscription Info

            //Set Phone
            try {
                if (currentUser.getEmployee().getProfile() != null) {
                    EdsCrmContact contact = currentUser.getEmployee().getProfile().getContact();
                    if (contact != null && StringUtils.isNotBlank(contact.getPrimaryPhone())) {
                        List<EdsCountry> countryList = countryManager.list();
                        for (EdsCountry country : countryList) {
                            try {
                                if (StringUtils.isNotBlank(country.getTelCode()) && contact.getPrimaryPhone().startsWith(country.getTelCode())) {
                                    String telCode = country.getTelCode();
                                    String phone = contact.getPrimaryPhone().substring(telCode.length());
                                    PhoneTO phoneTO = new PhoneTO(telCode, phone);
                                    userTO.setPhone(phoneTO);
                                    break;
                                }
                            } catch (Exception e) {
                                log.error("", e);
                            }
                        }

                    }
                }
            } catch (Exception e) {
                log.error("", e);
                e.printStackTrace();
            }


            return successResponse(userTO);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * This api method is added to document app
     *
     * @return
     * @throws RestException
     */
    @Operation(summary = "Get User Details", description = "Retrieves the current user details")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have the current user details"),
            @ApiResponse(responseCode = "401", description = "Session expired")})
    @RequestMapping(value = "/user_details", method = RequestMethod.GET)
    public Object getUserDetails() throws RestException {
        ProfileItem userProfile;
        try {
            userProfile = hrmsServiceLocal.editProfile(userManager.getUser().getObjectID(), "FROM_HRMS_EMPLOYEE_VIEW", true);
            if (userProfile == null) {
                throw new RestException("Session expired", "Session expired", EXPIRED, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            log.error("", e);
            throw new RestException("Session expired", "Session expired", EXPIRED, HttpStatus.UNAUTHORIZED);
        }

        try {
            EdsUser user = userManager.getUser();
            EdsCompany edsCompany = user.getCompany();

            UserDetailsTO userDetailsTO = new UserDetailsTO();
            userDetailsTO.setUser_id(userProfile.getEmployeeId());
            userDetailsTO.setFull_name(userProfile.getName());
            userDetailsTO.setUser_email(userProfile.getEmail());
            userDetailsTO.setUser_company_id(edsCompany.getObjectID());
            userDetailsTO.setCompany_name(edsCompany.getName());

            String username = globalAuthJdbcSpringManager.getUsername(edsCompany.getObjectID(), user.getObjectID());
            if (StringUtils.isNotBlank(username)) {
                userDetailsTO.setUser_name(username);
            }

            ArrayList<PhoneEmailTO> phoneNumbers = new ArrayList<>();
            Map<Integer, ArrayList<String>> itemParamsAsMap = ContactListItem.getItemParamsAsMap(userProfile, Constants.CONTACT_PHONES);
            if (itemParamsAsMap.size() > 0) {
                for (Map.Entry<Integer, ArrayList<String>> entry : itemParamsAsMap.entrySet()) {
                    int relation = entry.getKey();
                    for (String value : entry.getValue()) {
                        if (StringUtils.isNotBlank(value)) {
                            value = value.replace("|", "-");
                            String phoneNumber;
                            String type;
                            if (entry.getKey() == Constants.CONTACT_PHONES) {
                                switch (relation) {
                                    case Constants.G_HOME -> {
                                        type = "Home";
                                        phoneNumber = value;
                                    }
                                    case Constants.G_WORK -> {
                                        type = "Work";
                                        phoneNumber = value;
                                    }
                                    case Constants.G_MOBILE -> {
                                        type = "Mobile";
                                        phoneNumber = value;
                                    }
                                    default -> {
                                        type = null;
                                        phoneNumber = null;
                                    }
                                }
                                if (phoneNumber != null) {
                                    PhoneEmailTO phoneEmailTO = new PhoneEmailTO();
                                    phoneEmailTO.setName(phoneNumber);
                                    phoneEmailTO.setType(type);
                                    phoneEmailTO.setPrimary(phoneNumber.equals(userProfile.getPrimaryPhone()));
                                    phoneNumbers.add(phoneEmailTO);
                                }
                            }
                        }
                    }
                }
            }
            userDetailsTO.setPhone_number(phoneNumbers);
            EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(user);
            if (userSettings != null) {
                userDetailsTO.setCompany_locale(userSettings.getInternationalization());
            }
            if (StringUtils.isNotBlank(user.getTimezone())) {
                userDetailsTO.setTimezone(user.getTimezone());
            }

            userDetailsTO.setOffice_phone(edsCompany.getPhone());

            AddressTO address = new AddressTO();
            address.setStreet_address_1(edsCompany.getAddress1() != null ? edsCompany.getAddress1() : "");
            address.setStreet_address_2(edsCompany.getBillAddress2() != null ? edsCompany.getBillAddress2() : "");
            address.setCity(edsCompany.getCity() != null ? edsCompany.getCity() : "");
            address.setRegion_name(edsCompany.getCountryRegion() != null ? edsCompany.getCountryRegion().getName() : "");
            address.setCountry_name((edsCompany.getCountryZone() != null && edsCompany.getCountryZone().getCountry() != null)
                    ? edsCompany.getCountryZone().getCountry().getName() : "");
            userDetailsTO.setAddress(address);

            EdsCompanyPayrollSettings companyWebsite = companyPayrollSettingsManager.getCompanySettingValue(Constants.WEBSITE);
            userDetailsTO.setWebsite(companyWebsite != null && companyWebsite.getValue() != null ? companyWebsite.getValue() : "");

            //Set Custom Fields
            if (CollectionUtils.isNotEmpty(userProfile.getCustomFields())) {
                userDetailsTO.setCustom_fields(getCustomFields(userProfile.getCustomFields()));
            }

            EdsCompanySettings companySettings = edsCompany.getCompanySettings();
            ArrayList<CompanyCustomFieldItem> companyCustomFieldItems = (ArrayList<CompanyCustomFieldItem>) CustomFieldsUtils.setRPCCustomFieldItems(companySettings.getCompanySettingsCustomFields(), commonService.getCompanyCustomFields(ViewName.CompanySettings));
            if (CollectionUtils.isNotEmpty(companyCustomFieldItems)) {
                if (CollectionUtils.isNotEmpty(userDetailsTO.getCustom_fields())) {
                    userDetailsTO.getCustom_fields().addAll(getCustomFields(companyCustomFieldItems));
                } else {
                    userDetailsTO.setCustom_fields(getCustomFields(companyCustomFieldItems));
                }
            }

            return successResponse(userDetailsTO);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage() == null ? e.toString() : e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Update User Profile", description = """
            Request to update the user profile. This is a multipart request, as well as for creating some applications, consisting of two parts:
            body - Json containing the information on the updated fields
            avatar - Image, which the user chose himself as an avatar (if the user did not choose an avatar, will not be sent)

            In response to the request, an updated user object comes with all the new data.""")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have user profile info")})
    @RequestMapping(value = "/user_profile", method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Object updateUserProfile(MultipartRequest multipartRequest, @RequestParam(name = "body") String jsonString) throws RestException {

        UserUpdateTO userUpdateTO;
        ObjectMapper mapper = new ObjectMapper();
        try {
            userUpdateTO = mapper.readValue(jsonString, UserUpdateTO.class);
        } catch (Exception e) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "JSON body format is wrong.", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        EdsUser user = userManager.getUser();
        ProfileItem userProfile;
        try {
            userProfile = hrmsServiceLocal.editProfile(user.getObjectID(), null, false);
            if (userProfile == null) {
                throw new RestException("Session expired", "Session expired", EXPIRED, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            log.error("", e);
            throw new RestException("Session expired", "Session expired", EXPIRED, HttpStatus.UNAUTHORIZED);
        }
        if (StringUtils.isNotBlank(userUpdateTO.getUser_name())) {
            userProfile.setFirstName(userUpdateTO.getUser_name());
            userProfile.setLastName(null);
            userProfile.setMiddleName(null);
        }
        if (StringUtils.isNotBlank(userUpdateTO.getEmail())) {
            if (!EMAIL_PATTERN.matcher(userUpdateTO.getEmail()).matches()) {
                throw new RestException("Invalid email address", "Invalid email address", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
            }

            HashMap<Integer, ArrayList<String>> emailParams = ContactListItem.getItemParamsAsMap(userProfile, Constants.CONTACT_EMAILS);

            String primaryEmail = null;
            if (StringUtils.isNotBlank(userProfile.getPrimaryEmail())) {
                primaryEmail = userProfile.getPrimaryEmail();
            }
            if (primaryEmail == null) {//if there is no employee primary email, set api provided email as WORK email
                ArrayList<String> workEmails = emailParams.get(Constants.G_WORK);
                workEmails.add(userUpdateTO.getEmail());
                userProfile.setWorkEmail(workEmails);
            } else {
                //this user has primary email, find the primary email type (WORK,HOME,OTHER) and update the email to api provided email
                // employee old primary email will be update
                for (HashMap.Entry<Integer, ArrayList<String>> entry : emailParams.entrySet()) {
                    if (entry != null) {
                        switch (entry.getKey()) {
                            case Constants.G_HOME -> {
                                ArrayList<String> homeEmails = entry.getValue();
                                for (int i = 0; i < homeEmails.size(); i++) {
                                    if (homeEmails.get(i).equals(primaryEmail)) {
                                        homeEmails.remove(primaryEmail);
                                        homeEmails.add(userUpdateTO.getEmail());
                                    }
                                }
                                userProfile.setHomeEmail(homeEmails);
                            }
                            case Constants.G_WORK -> {
                                ArrayList<String> workEmails = entry.getValue();
                                for (int i = 0; i < workEmails.size(); i++) {
                                    if (workEmails.get(i).equals(primaryEmail)) {
                                        workEmails.remove(primaryEmail);
                                        workEmails.add(userUpdateTO.getEmail());
                                    }
                                }
                                userProfile.setWorkEmail(workEmails);
                            }
                            case Constants.G_OTHER -> {
                                ArrayList<String> otherEmails = entry.getValue();
                                for (int i = 0; i < otherEmails.size(); i++) {
                                    if (otherEmails.get(i).equals(primaryEmail)) {
                                        otherEmails.remove(primaryEmail);
                                        otherEmails.add(userUpdateTO.getEmail());
                                    }
                                }
                                userProfile.setOtherEmail(otherEmails);
                            }
                        }
                    }
                }
            }
            userProfile.setPrimaryEmail(userUpdateTO.getEmail());

        }
        if (userUpdateTO.getPhone() != null && StringUtils.isNotBlank(userUpdateTO.getPhone().getCountry_code()) && StringUtils.isNotBlank(userUpdateTO.getPhone().getPhone_number())) {

            HashMap<Integer, ArrayList<String>> phoneParams = ContactListItem.getItemParamsAsMap(userProfile, Constants.CONTACT_PHONES);

            String primaryPhone = null;
            if (StringUtils.isNotBlank(userProfile.getPrimaryPhone())) {
                primaryPhone = userProfile.getPrimaryPhone();
            }
            if (primaryPhone == null) {//if there is no employee primary phone, set api provided phone as MOBILE email
                ArrayList<String> mobilePhones = phoneParams.get(Constants.G_MOBILE);
                mobilePhones.add(userUpdateTO.getPhone().toString());
                userProfile.setMobile(mobilePhones);
            } else {
                //this user has primary phone, find the primary phone type (WORK,WORK,MOBILE) and update the phone to api provided phone
                // employee old primary email will be update
                for (HashMap.Entry<Integer, ArrayList<String>> entry : phoneParams.entrySet()) {
                    if (entry != null) {
                        switch (entry.getKey()) {
                            case Constants.G_HOME -> {
                                ArrayList<String> homePhones = entry.getValue();
                                for (int i = 0; i < homePhones.size(); i++) {
                                    if (homePhones.get(i).equals(primaryPhone)) {
                                        homePhones.remove(primaryPhone);
                                        homePhones.add(userUpdateTO.getPhone().toString());
                                    }
                                }
                                userProfile.setHomePhone(homePhones);
                            }
                            case Constants.G_WORK -> {
                                ArrayList<String> workPhones = entry.getValue();
                                for (int i = 0; i < workPhones.size(); i++) {
                                    if (workPhones.get(i).equals(primaryPhone)) {
                                        workPhones.remove(primaryPhone);
                                        workPhones.add(userUpdateTO.getPhone().toString());
                                    }
                                }
                                userProfile.setWorkPhone(workPhones);
                            }
                            case Constants.G_MOBILE -> {
                                ArrayList<String> mobilePhones = entry.getValue();
                                for (int i = 0; i < mobilePhones.size(); i++) {
                                    if (mobilePhones.get(i).equals(primaryPhone)) {
                                        mobilePhones.remove(primaryPhone);
                                        mobilePhones.add(userUpdateTO.getPhone().toString());
                                    }
                                }
                                userProfile.setMobile(mobilePhones);
                            }
                        }
                    }
                }
            }

            userProfile.setPrimaryPhone(userUpdateTO.getPhone().toString());
        }

        Integer userId;
        try {
            userId = hrmsServiceLocal.updateProfile(userProfile);
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        try {
            if (multipartRequest != null && multipartRequest.getFileMap() != null && multipartRequest.getFileMap().size() > 0) {
                for (MultipartFile file : multipartRequest.getFileMap().values()) {
                    CreateDocumentCommand documentCommand = new CreateDocumentCommand();
                    documentCommand.setImgType(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1).toLowerCase(Locale.ENGLISH));
                    documentCommand.setCompanyID(user.getCompany().getObjectID());
                    documentCommand.setFolderName("static");
                    documentCommand.setNotdownloadable("YES");
                    WfmMultipartFile multipartFile = new WfmMultipartFile("", file);
                    documentCommand.addFile(multipartFile);
                    try {
                        String[] result = wfmCommandServiceLocal.createAttachmentHandler(documentCommand);
                        if (result != null && result.length > 0) {
                            commonServiceLocal.saveImageUrl(Integer.valueOf(result[0]), userId);
                        }
                    } catch (Throwable throwable) {
                        log.error(throwable.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return getUserProfile(userId);
    }

    @Operation(summary = "Update User Profile Image", description = """
            Request to update the user profile image. This is a multipart request, as well as for creating some applications, consisting of one part:
            avatar - Image, which the user chose himself as an avatar (if the user did not choose an avatar, will not be sent)

            In response to the request, an updated user object comes with all the new profile image url.""")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have user profile info")})
    @RequestMapping(value = "/user_profile_image", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Object updateCurrentUserProfileImage(MultipartRequest multipartRequest) throws RestException {
        EdsUser user = userManager.getUser();
        try {
            if (multipartRequest != null && multipartRequest.getFileMap() != null && multipartRequest.getFileMap().size() > 0) {
                for (MultipartFile file : multipartRequest.getFileMap().values()) {
                    CreateDocumentCommand documentCommand = new CreateDocumentCommand();
                    documentCommand.setImgType(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1).toLowerCase(Locale.ENGLISH));
                    documentCommand.setCompanyID(user.getCompany().getObjectID());
                    documentCommand.setFolderName("static");
                    documentCommand.setNotdownloadable("YES");
                    WfmMultipartFile multipartFile = new WfmMultipartFile("", file);
                    documentCommand.addFile(multipartFile);
                    try {
                        String[] result = wfmCommandServiceLocal.createAttachmentHandler(documentCommand);
                        if (result != null && result.length > 0) {
                            commonServiceLocal.saveImageUrl(Integer.valueOf(result[0]), user.getObjectID());
                        }
                    } catch (Throwable throwable) {
                        log.error(throwable.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            log.error("", e);
            throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return getUserProfile(user.getObjectID());
    }

    @Operation(summary = "Update User Profile Image", description = """
            Request to update the user profile image. This is a multipart request, as well as for creating some applications, consisting of one part:
            avatar - Image, which the user chose himself as an avatar (if the user did not choose an avatar, will not be sent)

            In response to the request, an updated user object comes with all the new profile image url.""")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have user profile info")})
    @RequestMapping(value = "/user_profile_image/{id}", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object updateUserProfileImage(@PathVariable("id") Integer id) throws RestException {
        EdsUser user = userManager.getUserByUserID(id);
        MyGovPassportResponseDto responseDto = getPassInfo(user.getEmployee().getProfile().getEmployeeCode(), new SimpleDateFormat("yyyy-MM-dd").format(user.getEmployee().getBirthDay()));
        if (responseDto != null && responseDto.getPhoto() != null) {
            try {
                MultipartFile file = new MockMultipartFile("Profile-image.jpg", "Profile-image.jpg", "application/octet-stream", Base64.getDecoder().decode(responseDto.getPhoto()));
                CreateDocumentCommand documentCommand = new CreateDocumentCommand();
                documentCommand.setImgType(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1).toLowerCase(Locale.ENGLISH));
                documentCommand.setCompanyID(user.getCompany().getObjectID());
                documentCommand.setFolderName("static");
                documentCommand.setNotdownloadable("YES");
                WfmMultipartFile multipartFile = new WfmMultipartFile("", file);
                documentCommand.addFile(multipartFile);
                try {
                    String[] result = wfmCommandServiceLocal.createAttachmentHandler(documentCommand);
                    if (result != null && result.length > 0) {
                        commonServiceLocal.saveImageUrl(Integer.valueOf(result[0]), user.getObjectID());
                    }
                } catch (Throwable throwable) {
                    log.error(throwable.getMessage());
                }
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return getUserProfile(user.getObjectID());
    }

    @Operation(summary = "Update User Profile Image", description = """
            Request to update the user profile image. This is a multipart request, as well as for creating some applications, consisting of one part:
            avatar - Image, which the user chose himself as an avatar (if the user did not choose an avatar, will not be sent)

            In response to the request, an updated user object comes with all the new profile image url.""")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have user profile info")})
    @RequestMapping(value = "/candidate_image/{id}", method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Object updateCandidateImage(@PathVariable("id") Integer id) throws RestException {
        EdsCrmContact candidate = candidateManager.getCandidateById(id);
        EdsUser user = userManager.getUser();
        MyGovPassportResponseDto responseDto = getPassInfo(candidate.getNumber(), new SimpleDateFormat("yyyy-MM-dd").format(candidate.getDateOfBirth()));
        if (responseDto != null && responseDto.getPhoto() != null) {
            try {
                MultipartFile file = new MockMultipartFile("Profile-image.jpg", "Profile-image.jpg", "application/octet-stream", Base64.getDecoder().decode(responseDto.getPhoto()));
                CreateDocumentCommand documentCommand = new CreateDocumentCommand();
                documentCommand.setImgType(file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1).toLowerCase(Locale.ENGLISH));
                documentCommand.setCompanyID(user.getCompany().getObjectID());
                documentCommand.setFolderName("static");
                documentCommand.setNotdownloadable("YES");
                WfmMultipartFile multipartFile = new WfmMultipartFile("", file);
                documentCommand.addFile(multipartFile);
                try {
                    String[] result = wfmCommandServiceLocal.createAttachmentHandler(documentCommand);
                    if (result != null && result.length > 0) {
                        commonServiceLocal.saveCrmContactImageUrl(Integer.valueOf(result[0]), candidate.getObjectID());
                    }
                } catch (Throwable throwable) {
                    log.error(throwable.getMessage());
                }
            } catch (Exception e) {
                log.error("", e);
                throw new RestException(GENERAL_ERROR_MESSAGE, e.getMessage(), SERVER_ERROR, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return "SUCCESS";
    }

    @Operation(summary = "Get Department List", description = "Get Department List.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of departments")})
    @RequestMapping(value = "/employees/departments", method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object departmentList(@RequestParam(value = "project_id", required = false) Integer project_id,
                                 @RequestParam(value = "employees_type", required = false) String employees_type) throws RestException {

        ArrayList<DepartmentListItemTO> departmentsList = new ArrayList<>();

        LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> assigneesMap;
        if (EmployeeTypeEnum.PROJECT_EMPLOYEES.name().equals(employees_type)) {
            assigneesMap = taskServiceLocal.getAssigneesWithTreeInfoLinkedHashMapWithParams(null, project_id, null, false);
        } else if (EmployeeTypeEnum.ONLY_AVAILABLE.name().equals(employees_type)) {
            assigneesMap = taskServiceLocal.getAssigneeListOnlyAvailableEmployees(null, project_id, null, null);
        } else {
            assigneesMap = taskServiceLocal.getAssigneesWithTreeInfoLinkedHashMap(null);
        }

        for (ArrayList<KpiTreeInfo> treeInfo : assigneesMap.values()) {
            DepartmentListItemTO department = new DepartmentListItemTO();
            department.setId(treeInfo.get(0).getDepartmentId());
            department.setName(treeInfo.get(0).getDepartmentName());
            department.setEmployees_count(treeInfo.size());
            departmentsList.add(department);
        }

        return successResponse(new ResponseListData<>(departmentsList));
    }

    @Operation(summary = "Employee Lookup", description = "Retrieves a list of employees.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of employees"),
            @ApiResponse(responseCode = "400", description = "Start point is required"),
            @ApiResponse(responseCode = "400", description = "Limit is required"),
            @ApiResponse(responseCode = "422", description = "Start point and limit can not be zero at the same time")
    })
    @RequestMapping(value = "/employee_lookup", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public Object employeeLookup(@RequestBody RequestListSearchData requestListSearchData) throws RestException {

        if (requestListSearchData.getStart() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Start point is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (requestListSearchData.getLimit() == null) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Limit is required", REQUIRED, HttpStatus.BAD_REQUEST);
        }
        if (requestListSearchData.getStart().equals(requestListSearchData.getLimit()) && requestListSearchData.getLimit() == 0) {
            throw new RestException(GENERAL_ERROR_MESSAGE, "Start point and limit can not be zero at the same time", INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        ArrayList<EmployeeTO> employeeTOS = new ArrayList<>();

        ListingFilterParameter fp = new ListingFilterParameter();
        fp.setStart(requestListSearchData.getStart());
        fp.setLimit(requestListSearchData.getLimit());
        fp.setSearchKey(requestListSearchData.getSearch_text());
        ListResult<EmployeeListItem> employees = employeeServiceLocal.getEmployeeList(fp);

        if (employees != null) {
            for (EmployeeListItem employeeItem : employees.getList()) {
                EmployeeLookUpTO employee = new EmployeeLookUpTO();
                employee.setId(employeeItem.getObjectID());
                employee.setName(employeeItem.getFullName());
                if (StringUtils.isNotBlank(employeeItem.getEmail())) {
                    employee.setEmail(employeeItem.getEmail());
                }
                employee.setDepartment(employeeItem.getDepartment());

                EdsUpload photo = userManager.get(employeeItem.getObjectID()).getPhoto();
                if (photo != null) {
                    employee.setAvatar(commonServiceLocal.getImageUrl(photo.getObjectID()));
                }
                employeeTOS.add(employee);
            }
        }

        EmployeeListTO result = new EmployeeListTO(employeeTOS);

        return successResponse(result);
    }

    @Operation(summary = "Search Employees", description = "Request to search employees by department.\n But if department_id is not set need to search by employees for whole company")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of employees"),
            @ApiResponse(responseCode = "400", description = "Start point is required")
    })
    @RequestMapping(value = "/employees", method = RequestMethod.GET)
    public Object searchEmployees(@RequestParam(value = "query", required = false) String query,
                                  @RequestParam(value = "limit", required = false) Integer limit,
                                  @RequestParam(value = "offset", required = false) Integer offset,
                                  @RequestParam(value = "department_id", required = false) Integer department_id,
                                  @RequestParam(value = "project_id", required = false) Integer project_id,
                                  @RequestParam(value = "employees_type", required = false) String employees_type) throws RestException {

        ArrayList<SearchEmployeeTO> result = new ArrayList<>();

        LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> assigneesMap;
        if (EmployeeTypeEnum.PROJECT_EMPLOYEES.name().equals(employees_type)) {
            assigneesMap = taskServiceLocal.getAssigneesWithTreeInfoLinkedHashMapWithParams(null, project_id, null, false);
        } else if (EmployeeTypeEnum.ONLY_AVAILABLE.name().equals(employees_type)) {
            assigneesMap = taskServiceLocal.getAssigneeListOnlyAvailableEmployees(null, project_id, null, null);
        } else if (project_id != null) {
            assigneesMap = taskServiceLocal.getAssigneesWithTreeInfoLinkedHashMapWithParams(null, project_id, null, false);
        } else {
            assigneesMap = taskServiceLocal.getAssigneesWithTreeInfoLinkedHashMap(null);
        }

        for (ArrayList<KpiTreeInfo> list : assigneesMap.values()) {
            for (KpiTreeInfo info : list) {
                SearchEmployeeTO employee = new SearchEmployeeTO();
                employee.setId(info.getId());
                employee.setName(info.getName());
                employee.setPhone(info.getPhone());
                employee.setEmail(info.getEmail());
                EdsEmployee edsEmployee = employeeManager.get(info.getEmployeeId());
                if (edsEmployee.getPhoto() != null) {
                    employee.setAvatar_image(commonServiceLocal.getImageUrl(edsEmployee.getPhoto().getObjectID()));
                }
                employee.setDepartment(new IdNameTO(info.getDepartmentId(), info.getDepartmentName()));
                employee.setPosition(new IdNameTO(info.getPositionId(), info.getPositionName()));
                result.add(employee);
            }
        }

        //Filter by department
        if (department_id != null && department_id > 0) {
            result = (ArrayList) result.stream().filter(employeeTO -> employeeTO.getDepartment().getId().equals(department_id)).collect(Collectors.toList());
        }

        //Filter by search query
        if (StringUtils.isNotBlank(query)) {
            result = (ArrayList<SearchEmployeeTO>) result.stream().filter(employeeTO -> employeeTO.getName().toLowerCase().contains(query.trim().toLowerCase())).collect(Collectors.toList());
        } else {
            result.sort(Comparator.comparing(IdNameTO::getName));
        }

        //Paging
        if (offset != null && limit != null) {
            result = ListUtils.getSublistSmart(result, offset, limit);
        }

        return successResponse(new ResponseListData<>(result));

    }

    @Operation(summary = "Get Permissions", description = "Retrieves permissions based on provided parameter")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of permissions"),
            @ApiResponse(responseCode = "401", description = "Session expired"),
            @ApiResponse(responseCode = "422", description = "Invalid permission group name")
    })
    @RequestMapping(value = "/permissions", method = RequestMethod.GET)
    public Object getPermissions(@RequestParam(value = "only_for", required = false) String permissionGroup) throws RestException {
        EdsUser edsUser = userManager.getUser();
        if (edsUser == null) {
            throw new RestException("Session expired", "Session expired", EXPIRED, HttpStatus.UNAUTHORIZED);
        }

        String level = edsUser.hasRole(EdsRole.ADMIN_CODE) ? PermissionLevelEnum.WRITE.getLevel() : PermissionLevelEnum.READ.getLevel();
        ArrayList<PermissionTO> permissions = new ArrayList<>();

        if (StringUtils.isNotBlank(permissionGroup)) {
            if (PermissionGroupEnum.getGroup(permissionGroup) == null) {
                throw new RestException(GENERAL_ERROR_MESSAGE, "Invalid permission group name: " + permissionGroup, INVALID, HttpStatus.UNPROCESSABLE_ENTITY);
            }
            ArrayList<String> codes;
            if (PermissionGroupEnum.FLOW_SETTINGS.getGroup().equalsIgnoreCase(permissionGroup)) {
                permissions = getFlowSettingPermissions(level);
            }
             /*else if (PermissionGroupEnum.SALES.getGroup().equalsIgnoreCase(permissionGroup)) {
                //Lead White
                codes = new ArrayList<>();
                codes.add(PermissionConstants.ADD_NEW_LEAD);
                codes.add(PermissionConstants.CRM_LEAD_EDIT);
                codes.add(PermissionConstants.CRM_LEAD_DELETE);
                if (permissionManager.hasPermission(codes, edsUser, true)) {
                    PermissionTO permission = new PermissionTO();
                    permission.setName(PermissionSubGroupEnum.LEADS.getGroup());
                    permission.setLevel(PermissionLevelEnum.WRITE.getLevel());
                    permissions.add(permission);
                } else if (permissionManager.hasPermission(codes, edsUser, true)) {
                    //Lead Read
                    codes = new ArrayList<>();
                    codes.add(PermissionConstants.CRM_LEADS_LIST);
                    codes.add(PermissionConstants.CRM_SEE_ALL_LEADS_LIST);

                    PermissionTO permission = new PermissionTO();
                    permission.setName(PermissionSubGroupEnum.LEADS.getGroup());
                    permission.setLevel(PermissionLevelEnum.READ.getLevel());
                    permissions.add(permission);
                }
                //Opportunity White
                codes = new ArrayList<>();
                codes.add(PermissionConstants.CRM_ADD_NEW_OPPORTUNITIES);
                codes.add(PermissionConstants.CRM_OPPORTUNITIES_ADD);
                codes.add(PermissionConstants.CRM_EDIT_OPPORTUNITIES);
                codes.add(PermissionConstants.CRM_OPPORTUNITIES_EDIT);
                codes.add(PermissionConstants.CRM_REMOVE_OPPORTUNITIES);
                if (permissionManager.hasPermission(codes, edsUser, true)) {
                    PermissionTO permission = new PermissionTO();
                    permission.setName(PermissionSubGroupEnum.OPPORTUNITIES.getGroup());
                    permission.setLevel(PermissionLevelEnum.WRITE.getLevel());
                    permissions.add(permission);
                } else if (permissionManager.hasPermission(codes, edsUser, true)) {
                    //Opportunity Read
                    codes = new ArrayList<>();
                    codes.add(PermissionConstants.CRM_OPPORTUNITIES_LIST);
                    codes.add(PermissionConstants.CRM_SEE_ALL_OPPORTUNITIES_LIST);

                    PermissionTO permission = new PermissionTO();
                    permission.setName(PermissionSubGroupEnum.OPPORTUNITIES.getGroup());
                    permission.setLevel(PermissionLevelEnum.READ.getLevel());
                    permissions.add(permission);
                }
                //Task Write
                codes = new ArrayList<>();
                codes.add(PermissionConstants.PM_TASKS_ADD);
                codes.add(PermissionConstants.PM_TASKS_ADD_MULTI);
                codes.add(PermissionConstants.PM_TASKS_EDIT);
                codes.add(PermissionConstants.PM_TASKS_REMOVE);
                if (permissionManager.hasPermission(codes, edsUser, true)) {
                    PermissionTO permission = new PermissionTO();
                    permission.setName(PermissionSubGroupEnum.TASKS.getGroup());
                    permission.setLevel(PermissionLevelEnum.WRITE.getLevel());
                    permissions.add(permission);
                } else if (permissionManager.hasPermission(codes, edsUser, true)) {
                    //Task Read
                    codes = new ArrayList<>();
                    codes.add(PermissionConstants.PM_TASKS_LIST);

                    PermissionTO permission = new PermissionTO();
                    permission.setName(PermissionSubGroupEnum.TASKS.getGroup());
                    permission.setLevel(PermissionLevelEnum.READ.getLevel());
                    permissions.add(permission);
                }
            }*/
        } else {
            permissions = getFlowSettingPermissions(level);
        }

        PermissionListTO permissionList = new PermissionListTO();
        permissionList.setPermissions(permissions);

        return successResponse(permissionList);
    }

    private ArrayList<PermissionTO> getFlowSettingPermissions(String level) {
        ArrayList<PermissionTO> permissions = new ArrayList<>();

        PermissionTO permission = new PermissionTO();
        permission.setName(PermissionSubGroupEnum.LEADS.getGroup());
        permission.setLevel(level);
        permissions.add(permission);

        permission = new PermissionTO();
        permission.setName(PermissionSubGroupEnum.OPPORTUNITIES.getGroup());
        permission.setLevel(level);
        permissions.add(permission);

        permission = new PermissionTO();
        permission.setName(PermissionSubGroupEnum.TASKS.getGroup());
        permission.setLevel(level);
        permissions.add(permission);

        return permissions;
    }

    @Operation(summary = "Menus", description = "Retrieves current user available menus(modules)")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have list of menus"),
            @ApiResponse(responseCode = "401", description = "Session expired")
    })
    @RequestMapping(value = "/menus", method = RequestMethod.GET)
    public Object getMenus() throws RestException {

        String fullHost = EdsContextParams.getFullHost();
        ArrayList<MenuTO> menuList = new ArrayList<>();

        if (ServerUtils.hasPermission(PermissionConstants.ACCOUNTING_MAIN_MENU)) {
            menuList.add(new MenuTO("Accounts", fullHost.concat(Constants.ACCOUNTING_URL)));
        }
        if (ServerUtils.hasPermission(PermissionConstants.CRM_MAIN_MENU)) {
            menuList.add(new MenuTO("Sales", fullHost.concat(Constants.CRM_URL)));
        }
        if (ServerUtils.hasPermission(PermissionConstants.HRMS_MAIN_MENU)) {
            menuList.add(new MenuTO("Humans", fullHost.concat(Constants.HRMS_URL)));
        }
        if (ServerUtils.hasPermission(PermissionConstants.PM_MAIN_MENU)) {
            menuList.add(new MenuTO("Projects", fullHost.concat(Constants.PM_URL)));
        }
        if (ServerUtils.hasPermission(PermissionConstants.REPORTING_MAIN_MENU)) {
            menuList.add(new MenuTO("Reports", fullHost.concat(Constants.REPORTING_URL)));
        }
        if (ServerUtils.hasPermission(PermissionConstants.PAYROLL_MAIN_MENU)) {
            menuList.add(new MenuTO("Payroll", fullHost.concat(Constants.PAYROLL_URL)));
        }
        if (ServerUtils.hasPermission(PermissionConstants.DOCUMENTS_MAIN_MENU)) {
            menuList.add(new MenuTO("Documents", fullHost.concat(Constants.DOCUMENTS_URL)));
        }
        if (ServerUtils.hasPermission(PermissionConstants.SETTINGS_MAIN_MENU)) {
            menuList.add(new MenuTO("Settings", fullHost.concat(Constants.SETTINGS_URL)));
        }

        menuList.add(new MenuTO("User Profile", fullHost.concat("Settings.html#profileSettingsHome|profile/").concat(userManager.getUser().getObjectID().toString())));


        return successResponse(new ResponseListData<>(menuList));
    }

    @Operation(summary = "Check Employee by pinfl or inn", description = "Request to Check Employee by pinfl or inn.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "\"Data\" field of response will have boolean showing whether employee exists or not")})
    @RequestMapping(value = "/check_employee", method = RequestMethod.GET)
    public Object checkEmployee(@RequestParam(value = "pinfl", required = false) String pinfl,
                                @RequestParam(value = "inn", required = false) String inn) throws RestException {
        if (pinfl == null && inn == null) {
            throw new RestException("Either pinfl or inn should be provided", "Either pinfl or inn should be provided", NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        EdsEmployee employee;
        if (pinfl != null) {
            employee = employeeManager.getEmployeeByNumber(pinfl);
        } else {
            employee = employeeManager.getEmployeeByInn(inn);
        }

        ResponseData response = new ResponseData();
        response.addProperty("key", employee != null);
        return successResponse(response);
    }

    private MyGovPassportResponseDto getPassInfo(String pinfl, String birthDay) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> request = new HashMap<>();
        request.put("pin", pinfl);
        request.put("lang_id", 1);
        request.put("birth_date", birthDay);
        request.put("document", "uz");
        request.put("is_photo", "Y");
        return restTemplate.postForObject("https://dev-api.agrobank.uz/egov/v1/people/info", new HttpEntity<>(request, httpHeaders), MyGovPassportResponseDto.class);
    }
}
