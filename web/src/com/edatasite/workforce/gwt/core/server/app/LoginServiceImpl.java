package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.shared.db.EdsDbException;
import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.EdsActivationLink;
import com.edatasite.workforce.core.domain.EdsBackendManagement;
import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCompanyAttachment;
import com.edatasite.workforce.core.domain.EdsCompanySystemSettings;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeProfile;
import com.edatasite.workforce.core.domain.EdsGoogleCalendar;
import com.edatasite.workforce.core.domain.EdsHostBasedSetting;
import com.edatasite.workforce.core.domain.EdsLanding;
import com.edatasite.workforce.core.domain.EdsModule;
import com.edatasite.workforce.core.domain.EdsNumberingSettings;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsServerContacts;
import com.edatasite.workforce.core.domain.EdsSinxDocuments;
import com.edatasite.workforce.core.domain.EdsUsagePlan;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUserEmailSettings;
import com.edatasite.workforce.core.domain.EdsUserSession;
import com.edatasite.workforce.core.domain.EdsUserSessionTracker;
import com.edatasite.workforce.core.domain.EdsUserSettings;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.enums.DeviceTypeEnum;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.availability.client.rpc.TimeSlot;
import com.edatasite.workforce.gwt.core.client.CommandConstants;
import com.edatasite.workforce.gwt.core.client.Exceptions.IncorrectPasswordException;
import com.edatasite.workforce.gwt.core.client.Exceptions.UserNotFoundException;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.ActivateAccount;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.KeyValueStruct;
import com.edatasite.workforce.gwt.core.client.rpc.LoginService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.UserCompanyDTO;
import com.edatasite.workforce.gwt.core.client.rpc.website.CompanyDomain;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.locking.TransactionLockingCheckService;
import com.edatasite.workforce.gwt.core.server.controllers.login.BaseLoginController;
import com.edatasite.workforce.gwt.core.server.db.ActivationLinkManager;
import com.edatasite.workforce.gwt.core.server.db.BackendManagementManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyAttachmentManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.CompanySystemSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.GoogleCalendarManager;
import com.edatasite.workforce.gwt.core.server.db.GoogleContactsManager;
import com.edatasite.workforce.gwt.core.server.db.GoogleDocumentsManager;
import com.edatasite.workforce.gwt.core.server.db.GoogleManager;
import com.edatasite.workforce.gwt.core.server.db.HostBasedSettingManager;
import com.edatasite.workforce.gwt.core.server.db.LandingManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.ModuleManager;
import com.edatasite.workforce.gwt.core.server.db.NumberingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.PermissionManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.ServerUploadHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.UploadManager;
import com.edatasite.workforce.gwt.core.server.db.UsagePlanManager;
import com.edatasite.workforce.gwt.core.server.db.UserEmailSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.UserSessionManager;
import com.edatasite.workforce.gwt.core.server.db.UserSessionTrackerManager;
import com.edatasite.workforce.gwt.core.server.db.UserSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.ValidityPeriodManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.office365.services.Office365AuthService;
import com.edatasite.workforce.gwt.core.server.rpc.AuthDetails;
import com.edatasite.workforce.gwt.core.server.rpc.AuthInfoItem;
import com.edatasite.workforce.gwt.core.server.rpc.LoggingInUser;
import com.edatasite.workforce.gwt.core.server.rpc.ShadowAccount;
import com.edatasite.workforce.gwt.core.server.rpc.UserSignUPSessionID;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.localization.PdfLocalizationName;
import com.edatasite.workforce.gwt.messagecenter.client.rpc.MessageCenterService;
import com.edatasite.workforce.gwt.myaccount.client.rpc.MyAccountService;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanItem;
import com.edatasite.workforce.gwt.myaccount.server.app.MyAccountServiceLocal;
import com.edatasite.workforce.gwt.profile.client.rpc.AlternativeCalendarEnum;
import com.edatasite.workforce.gwt.signup.client.rpc.ActivationLink;
import com.edatasite.workforce.gwt.signup.client.rpc.NewCompany;
import com.edatasite.workforce.utils.EdsContextParams;
import com.edatasite.workforce.utils.redis.RedisClient;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import com.workforcetrack.mobile.rpc.client.MSelectItem;
import com.workforcetrack.mobile.rpc.login.MUserCompanyDTO;
import org.apache.commons.lang3.StringUtils;
import org.gwtwidgets.server.spring.ServletUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Created by IntelliJ IDEA. User: iskan Date: Dec 24, 2007 Time: 2:13:15 PM To
 * change this template use File | Settings | File Templates.
 */
@Transactional
@Service("loginService")
public class LoginServiceImpl implements LoginService, LoginServiceLocal, Constants {

    public static int READ = 1 << 0;
    private static final Logger log = LoggerFactory.getLogger(LoginServiceImpl.class);
    @Autowired
    protected GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private UserSessionManager userSessionManager;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private LandingManager landingManager;
    @Autowired
    private CompanyAttachmentManager companyAttachmentManager;
    @Autowired
    private GoogleDocumentsManager googleDocumentsManager;
    @Autowired
    private Office365AuthService office365AuthService;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private GoogleManager googleManager;
    @Autowired
    private UserSessionTrackerManager userSessionTrackerManager;
    @Autowired
    private GoogleContactsManager googleContactsManager;
    @Autowired
    private GoogleCalendarManager googleCalendarManager;
    @Autowired
    private StatusServiceLocal statusServiceLocal;
    @Autowired
    private CompanySystemSettingsManager companySystemSettingsManager;
    @Autowired
    private CommonService commonService;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private TransactionLockingCheckService transactionLockingCheckService;
    @Autowired
    private UserEmailSettingsManager userEmailSettingsManager;
    @Autowired
    private ServerUploadHistoryManager serverUploadHistoryManager;
    @Autowired
    @Qualifier("commonLocalizer")
    private WfmMessageSource commonLocalizer;
    @Autowired
    private NumberingSettingsManager numberingSettingsManager;
    @Autowired
    private BackendManagementManager backendManagementManager;
    @Autowired
    private UploadManager uploadManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private ModuleManager moduleManager;
    @Autowired
    private ValidityPeriodManager validityPeriodManager;
    @Autowired
    private MyAccountService myAccountService;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private HostBasedSettingManager hostBasedSettingManager;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private UserSettingsManager userSettingsManager;
    @Autowired
    private ActivationLinkManager activationLinkManager;
    @Autowired
    private UsagePlanManager usagePlanManager;
    @Autowired
    private PermissionManager permissionManager;
    @Autowired
    private MyAccountServiceLocal myAccountServiceLocal;
    @Autowired
    private MessageCenterService messageCenterService;

    public String login(String userName, String password, String userAgent, Integer companyID, String IPAddress) throws UserNotFoundException, IncorrectPasswordException {
        if ((userName == null || userName.isEmpty()) && (password == null || password.isEmpty())) {
            throw new UserNotFoundException("Incorrect username or password!");
        }
        if (companyID == null) {
            return "Error occured while registering user";
        }
        GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager = ApplicationContextProvider.applicationContext.getBean(GlobalAuthJdbcSpringManager.class);
        String databse = globalAuthJdbcSpringManager.getCompanyDatabaseName(companyID);
        ServerSecurityContext.getInstance().setCompanyId(companyID);
        ServerSecurityContext.getInstance().setDatabase(databse);

        String sessionID = databse + "$" + companyID + "$";
        ServerSecurityContext.getInstance().setSessionId(sessionID);
        return sessionID;
    }

    @Override
    public String getMoreMenuSettings(String actionName) {
        return commonService.getMoreMenuSettings(actionName);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public MUserCompanyDTO getMobileUserSettings(MUserCompanyDTO mUserCompanyDTO) {
        mUserCompanyDTO.setUsagePlanItem(myAccountService.getCurrentUsagePlan());
        EdsUser user = userManager.getUser();
        for (EdsRole role : user.getRolesSorted()) {
            mUserCompanyDTO.getRoleItems().add(new MSelectItem(role.getObjectID(), role.getName()));
        }
        mUserCompanyDTO.setUserName(user.getFullName());
        mUserCompanyDTO.setKeyValueStructs(getUserSettings());

        return mUserCompanyDTO;
    }

    public String loginShadow(String userName, Integer companyId) throws UserNotFoundException,
            IncorrectPasswordException {
        EdsUser user;
        try {
            user = userManager.findUser(userName, companyId);

        } catch (DataAccessException dbex) {
            throw new UserNotFoundException(
                    "Exception obtaining user from database!");
        }
        if (user == null) {
            throw new UserNotFoundException("Invalid user name");
        }

        return null;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public LoggingInUser getLoggingUser(Integer userId) {
        EdsUser user = userManager.get(userId);
        if (user == null) {
            return null;
        }
        LoggingInUser lUser = new LoggingInUser();
        lUser.setCompanyActivated(user.getCompany().getActive());
        lUser.setActivated(EMPLOYEE_STATUS_ACTIVE.equals(user.getAccountStatus().getCode()));
        lUser.setCompanySetup(user.getCompany().getIsSetUp());
        lUser.setDeleted(user.getDeleted());
        lUser.setLogin(user.getUserName());
        lUser.setCompanyID(user.getCompany().getObjectID());
        lUser.setCompanyDataMissing(user.getCompany().getAnyDataMissing());
        lUser.setUserID(user.getObjectID());
        return lUser;
    }

    @Transactional
    public Boolean isActiveAccount(Integer id, Integer companyid) {
        EdsUser user = userManager.getUserByUserIdAndCompanyId(id, companyid);
        if (user != null) {
            return EMPLOYEE_STATUS_ACTIVE.equals(user.getAccountStatus().getCode()) || EMPLOYEE_STATUS_NO_ACCCESS.equals(user.getAccountStatus().getCode());
        }
        return null;
    }

    @Transactional
    public ActivateAccount getActiveAccount(Integer id, Integer companyid) {
        EdsUser user = userManager.getUserByUserIdAndCompanyId(id, companyid);
        if (user == null) {
            return null;
        }
        ActivateAccount account = new ActivateAccount();
        account.setActive(EMPLOYEE_STATUS_ACTIVE.equals(user.getAccountStatus().getCode()) || EMPLOYEE_STATUS_NO_ACCCESS.equals(user.getAccountStatus().getCode()));
        account.setLogin(user.getUserName());
        account.setRandom(user.getRandom());
        account.setCompanyId(companyid);
        return account;
    }

    // This data going to be stored on the client side
    //Do not delete (For mobile)
    @Transactional
    public ArrayList<KeyValueStruct> getUserSettings() {
        EdsUser user = companyManager.getUser();
        if (user == null) {
            log.error("companyManager.getUser() is NULL in LoginServiceImpl.getUserSettings() and sessionID is = {}", ServerSecurityContext.getInstance().getSessionId());
            return null;
        }

        EdsCompany company = user.getCompany();
        Boolean access = company.getActive();
        String accessAsString = FALSE;

        if (access == null) {
            access = true;
        }

        if (access) {
            accessAsString = TRUE;
        }

        EdsLanding landing = user.getLanding();
        if (landing == null) {
            user.setLanding(landing = new EdsLanding());
        }

        ArrayList<KeyValueStruct> resultKeyValueStruct = new ArrayList<>();
        resultKeyValueStruct.add(new KeyValueStruct(ROLES, user.getRolesAsIntegersString()));
        resultKeyValueStruct.add(new KeyValueStruct(ROLE_CODES, user.getRolesCodeAsString()));
        resultKeyValueStruct.add(new KeyValueStruct(ACCOUNTING_IS_SETUP, String.valueOf(user.getCompany().getAccountingSetup())));
        resultKeyValueStruct.add(new KeyValueStruct(PM_IS_SETUP, String.valueOf(user.getCompany().getIsSetUp())));
        resultKeyValueStruct.add(new KeyValueStruct(USER_FULLNAME, user.getName()));
        resultKeyValueStruct.add(new KeyValueStruct(USER_INITIALNAME, user.getInitialName()));
        resultKeyValueStruct.add(new KeyValueStruct(ACCESS_GRANTED, accessAsString));
        resultKeyValueStruct.add(new KeyValueStruct(USER_NAME, user.getUserName()));
        resultKeyValueStruct.add(new KeyValueStruct(FULL_NAME, user.getFullName()));
        resultKeyValueStruct.add(new KeyValueStruct(COMPANY_NAME, company.getName()));
        resultKeyValueStruct.add(new KeyValueStruct(COMPANY_ID, String.valueOf(company.getObjectID())));
        resultKeyValueStruct.add(new KeyValueStruct(INVOICE_FIRST_VIEW, landing.getInvoiceFirstView() == null ? LANDING_PAGE : landing.getInvoiceFirstView()));
        resultKeyValueStruct.add(new KeyValueStruct(PM_FIRST_VIEW, landing.getPMFirstView() == null ? LANDING_PAGE : landing.getPMFirstView()));
        resultKeyValueStruct.add(new KeyValueStruct(PA_FIRST_VIEW, landing.getPAFirstView() == null ? LANDING_PAGE : landing.getPAFirstView()));
        resultKeyValueStruct.add(new KeyValueStruct(USER_AVAILABILITY, statusServiceLocal.getUserStatus()));
        resultKeyValueStruct.add(new KeyValueStruct(USER_ID, user.getObjectID().toString()));
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        resultKeyValueStruct.add(new KeyValueStruct(BASE_CURRENCY, (financialSettings != null && financialSettings.getCurrency() != null) ?
                financialSettings.getCurrency().getSymbol() : "$"));

        return resultKeyValueStruct;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    @Override
    public MUserCompanyDTO getUserInfo(MUserCompanyDTO mUserCompanyDTO, boolean isForMobile) {
        EdsUser user = companyManager.getUser();
        if (user == null) {
            log.error("companyManager.getUser() is NULL in LoginServiceImpl.getUserSettings()and sessionID is = {}", ServerSecurityContext.getInstance().getSessionId());
            return null;
        }
        EdsCompany company = user.getCompany();
        for (EdsRole role : user.getRolesSorted()) {
            mUserCompanyDTO.getRoleItems().add(new MSelectItem(role.getObjectID(), role.getName()));
        }
        mUserCompanyDTO.setActive(company.getActive() != null ? company.getActive() : Boolean.TRUE);

        return mUserCompanyDTO;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Boolean isAdmin() {
        EdsUser user = userSessionManager.getUser();
        return user.hasRole(roleManager.get(EdsRole.ADMIN));
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ShadowAccount getShadowAccount(Integer companyID) {
        try {
            EdsCompany company = companyManager.get(companyID);
            if (company != null) {
                List<EdsEmployee> adminList = employeeManager.getAdministrators();
                EdsEmployee user = null;
                if (!adminList.isEmpty()) {
                    user = adminList.get(0);
                }
                if (user != null) {
                    ShadowAccount account = new ShadowAccount();
                    account.setLogin(user.getUserName());
                    account.setRandom(user.getRandom());
                    return account;
                }
            }
        } catch (Exception ex) {
            return null;
        }
        return null;
    }

    public void hideLandingPage(boolean checked, String fieldName) {
        if (checked) {
            landingManager.setFirstView(fieldName, HOME_PAGE);
        } else {
            landingManager.setFirstView(fieldName, LANDING_PAGE);
        }
    }

    @Transactional
    public String getCompanyLogoURL1() {
        return getCompanyLogoURL(CommandConstants.FOR_EMPLOYEES);
    }

    @Transactional
    public String getCompanyLogoURL(String logoType) {
        EdsUser user = userManager.getUser();
        if (user == null) {
            return EdsContextParams.getLogoImage();
        }
        EdsCompany company = user.getCompany();
        String url = companyAttachmentManager.getCompanyLogoUrl(company, logoType);
        if (url == null) {
            return EdsContextParams.getLogoImage();
        }
        return url;
    }

    @Transactional
    public SelectItem getCompanyLogo(String logoType) {
        EdsCompany company = employeeManager.getUser().getCompany();
        return companyAttachmentManager.getCompanyLogo(company, logoType);
    }

    @Transactional
    public String getCompanyLogoURL(EdsCompany company) {
        return companyAttachmentManager.getCompanyLogoUrl(company, CommandConstants.FOR_PDF);
    }

    public boolean resetCompanyOrPdfLogo(String logoType) {
        EdsCompany company = employeeManager.getUser().getCompany();
        EdsReference logo_type = referenceManager.findReference(CommandConstants._LOGO_TYPE, logoType);
        List<EdsCompanyAttachment> compAttachments = companyAttachmentManager.getCompanyAttachments(company, logo_type);
        for (EdsCompanyAttachment compAttach : compAttachments) {
            if (compAttach != null) {
                try {
                    EdsCompanyAttachment edsCompanyAttachment = companyAttachmentManager.get(compAttach.getObjectID());
                    if (edsCompanyAttachment != null) {
                        uploadManager.deleteFile(compAttach); // company logo or pdf logo
                        edsCompanyAttachment.setDeleted(true);
                    }
                } catch (Exception e) {
                    return false;
                }
            }
        }
        return true;
    }

    @Transactional
    public AccountItem getAccount() {
        EdsUser user = employeeManager.getUser();
        AccountItem account = new AccountItem();
        EdsCompany company = user.getCompany();
        if (user.isEmployee()) {
            EdsEmployee employee = (EdsEmployee) user;
            employee.setTimeSlot(user.getCompany().getDefaultTimeSlot());
            employeeManager.update(employee);

            account.setUserFullName(user.getFullName());

            if (company != null) {
                account.setCompanyId(company.getObjectID());
                account.setCompanyName(company.getName());
                if (company.getSignedUpPage() != null) {
                    account.setSignedUpPage(company.getSignedUpPage());
                }
            }
            account.setLogin(user.getUserName());
            String roles = "";
            if (!user.getRoles().isEmpty()) {
                for (EdsRole role : user.getRoles()) {

                    if (role.getObjectID().intValue() == EdsRole.DR.intValue()) {
                        if (!roles.isEmpty()) {
                            roles += ",";
                        }
                        roles += commonLocalizer.localize(PdfLocalizationName.director, role.getName());
                    }
                    if (role.getObjectID().intValue() == EdsRole.ADMIN
                            .intValue()) {
                        if (!roles.isEmpty()) {
                            roles += ",";
                        }
                        roles += commonLocalizer.localize(PdfLocalizationName.administrator, role.getName());
                    }
                    if (role.getObjectID().intValue() == EdsRole.PM.intValue()) {
                        if (!roles.isEmpty()) {
                            roles += ",";
                        }
                        roles += commonLocalizer.localize(PdfLocalizationName.projectManager, role.getName());
                    }
                    if (role.getObjectID().intValue() == EdsRole.TL.intValue()) {
                        if (roles.isEmpty()) {
                            roles += commonLocalizer.localize(PdfLocalizationName.departmentLeader, DEPARTMENT_LEADER_STRING);
                        }
                    }
                    if (role.getObjectID().intValue() == EdsRole.ACCOUNTANT.intValue()) {
                        if (!roles.isEmpty()) {
                            roles += ",";
                        }
                        roles += commonLocalizer.localize(PdfLocalizationName.accountantRole, role.getName());
                    }
                    if (role.getObjectID().intValue() == EdsRole.MEM.intValue()) {
                        if (roles.isEmpty() && user.getRoles().size() == 1) {
                            roles += commonLocalizer.localize(PdfLocalizationName.employee, EMPLOYEE_STRING);
                        }
                    }
                    account.setRole(roles);
                }

            }
        } else {
            if (user.getRoles() != null && !user.getRoles().isEmpty()) {
                for (EdsRole role : user.getRoles()) {
                    if (role.getObjectID().intValue() == EdsRole.GUEST.intValue()) {
                        account.setUserFullName(user.getFullName());
                        if (company != null) {
                            account.setCompanyName(company.getName());
                            if (company.getSignedUpPage() != null) {
                                account.setSignedUpPage(company.getSignedUpPage());
                            }
                        }
                        account.setLogin(user.getUserName());
                        account.setRole(role.getName());
                    }
                }

            }
            if (user instanceof EdsClientContact clientContact) {
                account.setCompanyName(clientContact.getCompany() != null ? clientContact.getCompany().getName() : "");
                account.setUserFullName(clientContact.getFirstName() + " " + clientContact.getLastName());
                account.setLogin(clientContact.getEmail());
                if (clientContact.hasRole(Constants.CLIENT_CODE) && clientContact.hasRole(Constants.SUPPLIER)) {
                    account.setRole("Client/Supplier");
                } else if (clientContact.hasRole(Constants.SUPPLIER)) {
                    account.setRole("Supplier");
                } else {
                    account.setRole("Client");
                }
            }
        }
        List<UserCompanyDTO> companyList = globalAuthJdbcSpringManager.getUserCompanyByEmail(null, user.getUserName());
        StringBuilder companyIds = new StringBuilder();
        for (int in = 0; in < companyList.size(); in++) {
            companyIds.append(companyList.get(in).getCompanyID());
            if (in < companyList.size() - 1) {
                companyIds.append(",");
            }
        }
        account.setAdvancedPassEnabled(globalAuthJdbcSpringManager.isEnabledAdvancedPassword(companyIds.toString()));
        return account;
    }

    public String[] getAdmin() {
        EdsUser user = userSessionManager.getUser();
        String[] admin = new String[2];
        admin[0] = user.getCompany().getCreator().getEmail();
        admin[1] = user.getCompany().getCreator().getFullName();
        return admin;
    }

    @Transactional
    public String[] updateAccount(AccountItem account) {
//        Integer compId = SecurityContext.getCompanyID();
        EdsUser user = employeeManager.getUser();
        user.setAccountStatus(referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_ACTIVE));
        user.setPassword(account.getPassword());
        userManager.saveUserAuthenticationData(user, user.getCompany().getObjectID());
        userManager.merge(user);
        userManager.flush();
        sessionService.expireMobileUserSessionsAcrossCompanies(user.getUserName());
//        SecurityContext.setCompanyID(compId);
        String[] st = new String[2];
        boolean isAdmin = user.hasRole(roleManager.get(EdsRole.ADMIN));
        if (isAdmin) {
            st[1] = "true";
        } else {
            st[1] = "false";
        }
        if (!user.getCompany().getName().equals("DEMO_COMPANY")) {
            try {
                messageManager.sendPasswordChangedNotification(user, true);
            } catch (EdsDbException ex) {
                ex.printStackTrace();
            }
        }
        st[0] = user.getCompany().getSignedUpPage();

        return st;

    }

    public String getAuthSubURL(String baseURL, String authType) {
        return googleManager.getAuthSubURL(authType, baseURL + "googleData");
    }

    @Transactional
    public Integer updateUserSessionTrack(String sessionID, String section, String params) {
        EdsUserSession uSession = userSessionManager.getUserSession(sessionID);
        EdsUserSessionTracker uSessionTracker = new EdsUserSessionTracker();
        uSessionTracker.setAccessTime(new Date());
        uSessionTracker.setSectionName(section);
        uSessionTracker.setParameters(params);
        uSessionTracker.setUserSession(uSession);
        userSessionTrackerManager.create(uSessionTracker);
        return uSessionTracker.getObjectID();
    }

    @Transactional
    public UserSignUPSessionID getSignedUser() {
        EdsUser user = (EdsUser) ServerSecurityContext.getInstance().getUser();
        if (user == null) {
            return null;
        }
        UserSignUPSessionID userData = new UserSignUPSessionID();
        userData.setEmployee(user.isEmployee());
        EdsCompany company = user.getCompany();
        EdsLanding landing = user.getLanding();

        EdsUserSession userSession = userSessionManager.getUserSession(ServerSecurityContext.getInstance().getSessionId());
        userData.setSuperUser(userSession.isSuperUser());
        if (company.getLiveDiscussionEnabled() != null) {
            userData.setLiveChatActive(company.getLiveDiscussionEnabled());
        }
        if (company.getExpertPanelEnabled() != null) {
            userData.setExpertChatActive(company.getExpertPanelEnabled());
        }
        if (user instanceof EdsClientContact) {
            userData.setClientContact(true);
            if (user.getClientContact().getAccess() != null) {
                if (user.getClientContact().getAccountStatus() != null && !user.getClientContact().getAccess()) {
                    userData.setHasAccess(false);
                }
            }
        }
        userData.setSessionID(ServerSecurityContext.getInstance().getSessionId());
        //user data
        userData.setAdmin(isAdmin());
        userData.setUserId(user.getObjectID());
        userData.setFullName(user.getName());
        userData.setFlexfullName(user.getFullName());
        userData.setFirstname(user.getFirstName());
        userData.setInitialName(user.getInitialName());
        userData.setUserName(user.getUserName());
        userData.setEmail(user.getEmail());
        userData.setPassword(user.getPassword());
        if (user.getLocation() != null) {
            userData.setCityName(user.getLocation().getCity());
            if (user.getLocation().getCountry() != null) {
                userData.setCountryName(user.getLocation().getCountry().getName());
            }
        }
        if (company.getCountryZone() != null) {
            userData.setCountryName(company.getCountryZone().getCountry().getName());
            userData.setCompanyCountryCode(company.getCountryZone().getCountry().getCode());
        }
        //company data
        userData.setCompanyName(user.getCompany().getName());
        userData.setRoles(user.getRolesAsIntegersString());
        userData.setClient(roleManager.hasRole(user, EdsRole.CLIENT));
        userData.setCompanyId(user.getCompany().getObjectID());
        EdsCompanySystemSettings settings = companySystemSettingsManager.findByCompanyID(user.getCompany().getObjectID());
        HashSet<String> module = moduleManager.getEnabledModuleCodesByCompany(user.getCompany().getObjectID());
        if (settings != null) {
            userData.setCompanySignedUpFrom(settings.getCompanySignedUpFrom());
            userData.setGoogleAppDomain(settings.getGoogleAppDomain());
            userData.setGoogleMarketplaceUsersImportShow(settings.isShowPopups() == null
                    ? true
                    : settings.isShowPopups());
            userData.setWorkspaceWelcomePageEnable(settings.getEnableWorkspaceWelcomePage() != null
                    ? settings.getEnableWorkspaceWelcomePage()
                    : false);//only for new sign upper's has show welcome page
            userData.setEnableWFTMoreMenuForMEM(settings.getEnableWFTMoreMenuForMEM() != null
                    ? settings.getEnableWFTMoreMenuForMEM()
                    : true);//only for members, has show wft more menu, DEFAULT shown
            userData.setEnableWFTMoreMenuForADMIN(settings.getEnableWFTMoreMenuForADMIN() != null
                    ? settings.getEnableWFTMoreMenuForADMIN()
                    : true);//for all users(ADMIN, DR, PM, HR, DR, MEM e.t.c), has show wft more menu, DEFAULT shown
            userData.setMeetingMinutesActive(module.contains(PermissionConstants.MEETING_MINUTES));
            userData.setBookingItemsActive(module.contains(PermissionConstants.BOOKING_ITEMS));
            userData.setCompanySystemSettingsItem(settings.getRPC());
            userData.setEnableMonthlyTimesheet(module.contains(PermissionConstants.MONTHLY_TIMESHEET));
            userData.setEnableStoreFront(module.contains(PermissionConstants.STOREFRONT));
            userData.setOverallDatePickerWeekStart(settings.getOverallDatePickerWeekStart() == null ? 2 : settings.getOverallDatePickerWeekStart());//2 is Monday
            userData.setProductTableCustomizationEnable(settings.getProductTableCustomizationEnabled() != null ? settings.getProductTableCustomizationEnabled() : false);
            if (settings.getAlternativeCalendarEnum() != null && !settings.getAlternativeCalendarEnum().equals(AlternativeCalendarEnum.NoAlternativeCalendar)) {
                userData.setAlternativeCalendarId(settings.getAlternativeCalendarEnum().getId());
            }
        }
        userData.setMultiCompanySubsidiary(user.getCompany().getParentCompanyId() != null);

        EdsBackendManagement backendManagement = backendManagementManager.getBackendManagement(user.getCompany().getObjectID(), user.getObjectID());
        if (backendManagement != null) {
            userData.setEnableSalesBackend(backendManagement.getEnableSalesBackend());
            userData.setEnableSupportBackend(backendManagement.getEnableSupportBackend());
            userData.setEnableAdminBackend(backendManagement.getEnableAdminBackend());
            userData.setEnablePartnerAdminBackend(backendManagement.getEnablePartnerAdminBackend());
//        userData.setEnablePDFBackend(backendManagement != null && backendManagement.getEnablePDFBackend());//for pdf backend
            userData.setEnableDeveloperBackend(backendManagement.getEnableDeveloperBackend());
            userData.setPromotionalCode(backendManagement.getPromotionalCode());
        }

        EdsNumberingSettings numberingSettings = numberingSettingsManager.getNumberingSetting();
        userData.setAutomatic(numberingSettings != null && numberingSettings.isAutomatic());
        userData.setAutomaticApproval(numberingSettings != null && numberingSettings.isAutomaticApproval());
        userData.setAutomaticWaitingForApproval(numberingSettings != null && numberingSettings.isWaitingForApproval());

        EdsUserEmailSettings userSettings = userEmailSettingsManager.getUserSettings(user);
        userData.setLanguageForUser(userSettings.getInternationalization());
        EdsUserSettings us = userSettingsManager.getUserSettingsValue("sidenavpos");
        if (us != null) {
            userData.setSideNavStyle(us.getValue());
        }
        userData.setProfileContent(getEmployeeProfileImageContent(user.getInitialName()));
        userData.setModulePermissions(permissionManager.getPermissions(
                Arrays.asList(new String[]{PermissionConstants.ACCOUNTING_MAIN_MENU,
                        PermissionConstants.CRM_MAIN_MENU,
                        PermissionConstants.HRMS_MAIN_MENU,
                        PermissionConstants.PM_MAIN_MENU,
                        PermissionConstants.PAYROLL_MAIN_MENU,
                        PermissionConstants.REPORTING_MAIN_MENU,
                        PermissionConstants.DOCUMENTS_MAIN_MENU,
                        PermissionConstants.LOGISTICS_MAIN_MENU}),
                user
        ));
        if (userSettings.isTimesheetrequired()) {
            userData.setValidateTaskStart(numberingSettings != null && numberingSettings.isValidateTaskStart());
            userData.setValidateTaskEnd(numberingSettings != null && numberingSettings.isValidateTaskEnd());
            userData.setValidateHoliday(numberingSettings != null && (numberingSettings.isValidateHoliday() != null && numberingSettings.isValidateHoliday()));
            userData.setValidateMaximumHours(numberingSettings != null && numberingSettings.isValidateMaximumHours());
            userData.setValidateFutureTimesheet(numberingSettings != null && numberingSettings.isValidateFutureTimesheet());
            userData.setValidateDayOff(numberingSettings != null && numberingSettings.isValidateDayOff());
            userData.setValidateTimesheetEstimate(numberingSettings != null && numberingSettings.isValidateTimesheetEstimate());
            userData.setValidatePastTimesheet(numberingSettings != null && numberingSettings.isValidatePastTimesheet());
            if (userData.isValidateMaximumHours()) {
                userData.setValidateTimeslot(numberingSettings != null && numberingSettings.isValidateTimeslot());
                if (!userData.isValidateTimeslot()) {
                    userData.setMaximumHours(numberingSettings == null ? 0 : numberingSettings.getMaximumHours());
                }
            }
        }
        if (userData.isValidatePastTimesheet()) {
            userData.setPastTimesheetDays(numberingSettings == null ? 1 : numberingSettings.getPastTimesheetDays());
        }
        if (userData.isValidateFutureTimesheet()) {
            userData.setFutureTimesheetDays(numberingSettings == null ? 1 : numberingSettings.getFutureTimesheetDays());
        }

        userData.setTimesheetCommentRequired(numberingSettings != null && numberingSettings.getTimesheetCommentRequired());
        userData.setValidateLeaveRequest(numberingSettings != null && (numberingSettings.isValidateLeaveRequest() != null && numberingSettings.isValidateLeaveRequest()));
        userData.setTimesheetWeekStart(numberingSettings == null ? 2 : numberingSettings.getTimesheetWeekStart());//2 is Monday
        userData.setShowCompletedTasks(numberingSettings == null ? false : numberingSettings.getShowCompletedTasks());
        userData.setShowToDoListTasks(numberingSettings == null ? true : numberingSettings.getShowToDoListTasks());//by default show such tasks
        userData.setShowHourTypeDropdown(numberingSettings == null ? false : numberingSettings.getShowTimesheetHourTypes());//by default do not show dropdown
        userData.setEnableMultipleTimerInstances(numberingSettings == null ? true : numberingSettings.getEnableMultipleTimerInstances());//by default enable multiple timers
        userData.setSaveTimerIntoTimesheetAutomatically(numberingSettings == null ? false : numberingSettings.getSaveTimerIntoTimesheetAutomatically());
        userData.setTimesheetDateFormat(numberingSettings == null ? "EEE, MM/d" : numberingSettings.getTimesheetDateFormat());

        UsagePlanItem usagePlanItem = myAccountService.getCurrentUsagePlan();
        if (!(usagePlanItem.isCurrSub() && (usagePlanItem.isFree() || usagePlanItem.isPaid()))) {
            company.setActive(false);
        }
        Boolean active = company.getActive();
        if (active == null) {
            active = true;
        }
        userData.setUsagePlan(usagePlanItem);
        userData.setCompanyActive(active);
        userData.setNotShowingPages(user.getCompany().getNotShowingPages());
        userData.setFreeTrialDaysLeft(commonService.getFreeTrialDaysLeft(usagePlanItem.isPaid()));
        userData.setPaidCompany(usagePlanItem.isPaid());
        userData.setDefaultCurrencyCODE(EdsContextParams.getCurrencyCODE());//default currency CODE;
        userData.setAnyDataMissing(company.getAnyDataMissing());
        userData.setAccountingIsSetup(company.getAccountingSetup());
        userData.setSalesIsSetup(Boolean.TRUE.equals(company.getSalesSetup()));

        boolean isPoIgnoreManagerApproval = false;
        String rolesAsString = genericSettingsManager.getValueByKey(GenericSettingsEnum.PO_IGNORE_MANAGER_APPROVAL_ROLES);
        Set<String> roleCodes = user.getRoleCODEs();
        if (rolesAsString != null && !rolesAsString.trim().isEmpty() && roleCodes != null && roleCodes.size() > 0) {
            String[] ignoredRoles = rolesAsString.split(",");
            for (String ignoredRole : ignoredRoles) {
                if (roleCodes.contains(ignoredRole)) {
                    isPoIgnoreManagerApproval = true;
                    break;
                }
            }
        }
        userData.setPoIgnoreManagerApproval(isPoIgnoreManagerApproval);

        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        if (financialSettings != null) {
            userData.setAccountingCalculationScale(financialSettings.getCalculationScale());
            userData.setAccountingCustomQtyScale(financialSettings.getProductQuantity());
            userData.setAccountingCustomPriceScale(financialSettings.getProductPriceScale());
            userData.setAccountingCustomExRateScale(financialSettings.getExchangeRateScale());
            userData.setAccountingTaxRateScalse(financialSettings.getTaxRateScale());
            userData.setAccountingDiscountScale(financialSettings.getDiscountScale());
            userData.setAccountingProgressinvoiceingAmountScale(financialSettings.getProgressInvoicingAmountScale());
            userData.setDoubleMessageEnable(financialSettings.getEnableDoubleMessage() != null ? financialSettings.getEnableDoubleMessage() : false);
            userData.setMultipleSalesPriceEnable(financialSettings.isEnableMultipleSalesPrice());
            userData.setVatRegistered(financialSettings.isVatRegistered());
            userData.setVatAccountingBasis(financialSettings.getVatAccountingBasis());

            if (financialSettings.isShowVatNumberInListings() && financialSettings.getCustomVatName() != null) {
                userData.setTaxName(financialSettings.getCustomVatName());
            } else {
                userData.setTaxName("");
            }
            /*if (financialSettings.getBlockBeforeDate() != null) {
                userData.setAccountingBeforeBlockDate(ServerUtils.getDateAsString(financialSettings.getBlockBeforeDate()));
            }*/
            userData.setMultiWarehouseEnabled(financialSettings.getEnableMultiWarehouse());
        } else {
            userData.setDoubleMessageEnable(false);
        }
        userData.setSupplier(user.hasRole(roleManager.getByCode(SUPPLIER)));

        DateNonConvertable lockDate = transactionLockingCheckService.getLockDate();
        if (lockDate != null) {
            userData.setTransactionLockDate(ServerUtils.getDateAsString(lockDate.getNonConvertedDate()));
            userData.setSalesLocked(transactionLockingCheckService.lockedForSales());
            userData.setPurchasesLocked(transactionLockingCheckService.lockedForPurchases());
            userData.setBankingLocked(transactionLockingCheckService.lockedForBanking());
            userData.setEmployeesLocked(transactionLockingCheckService.lockedForEmployees());
            userData.setAttendanceLocked(transactionLockingCheckService.lockedForAttendance());
            userData.setRecruitmentLocked(transactionLockingCheckService.lockedForRecruitment());
            userData.setPayslipsLocked(transactionLockingCheckService.lockedForPayslips());
            userData.setCashAdvancesLocked(transactionLockingCheckService.lockedForCashAdvances());
            userData.setAdditionalPaymentsLocked(transactionLockingCheckService.lockedForAdditionalPayments());
        } else if (financialSettings.getBlockBeforeDate() != null) {
            userData.setTransactionLockDate(ServerUtils.getDateAsString(financialSettings.getBlockBeforeDate()));
            userData.setSalesLocked(true);
            userData.setPurchasesLocked(true);
            userData.setBankingLocked(true);
        }

        userData.setPmIsSetup(company.getIsSetUp());

        if (company.getCompanySettings() != null) {
            userData.setLongDateFormat(company.getCompanySettings().getLongDateFormat());
            userData.setShortDateFormat(company.getCompanySettings().getShortDateFormat());
        } else {
            userData.setLongDateFormat("MMM dd, yyyy [HH:mm]");//MMM dd, yyyy [HH:mm] e.g. Jan 31, 2010 [08:30];
            userData.setShortDateFormat("MMM dd, yyyy");//MMM dd, yyyy e.g. Jan 31, 2010
        }

        //First views
        if (landing != null) {
            userData.setInvFirst(landing.getInvoiceFirstView());
            userData.setPmFirst(landing.getPMFirstView());
            userData.setPaFirst(landing.getPAFirstView());
            userData.setAvaFirst(landing.getAvailabilityFirstView());
        }

        EdsCompanySettings edsCompanySettings = user.getCompany().getCompanySettings();
        if (edsCompanySettings != null) {
            Boolean enabled = edsCompanySettings.isEnableMessageCenter() != null ? edsCompanySettings.isEnableMessageCenter() : false;
            userData.setMessageCenterEnabled(enabled);
            userData.setThemeForSystem(/*edsCompanySettings.getThemeForSystem() != null ? edsCompanySettings.getThemeForSystem() : */EdsContextParams.getDefaultTheme());
            if (edsCompanySettings.isSetupSubProject() != null) {// setup company subproject
                userData.setSetupSubProject(edsCompanySettings.isSetupSubProject());
            }
            if (edsCompanySettings.isSetupSubProject() != null) {// setup company subproject
                userData.setSetupSubProjectTwoLevel(edsCompanySettings.isSetupSubProjectTwoLevel());
            }
            userData.setAccountingSettingsEnabled(edsCompanySettings.isShowAccountingSettings());
        }
        if (user.isEmployee()) {
            EdsEmployee employee = (EdsEmployee) user;
            EdsEmployeeProfile profile = employee.getProfile();
            if (profile != null) {
                userData.setSetupUserProfile(profile.isSetupProfile() != null ? profile.isSetupProfile() : false);
            }
        }

        if (user.getLocale() != null) {
            userData.setLocaleString(user.getLocale().toString());
        } else if (user.getCompany().getLocale() != null) {
            userData.setLocaleString(user.getCompany().getLocale());
        } else {
            userData.setLocaleString(Locale.UK.toString());
        }

        String theLatestUploadVersion = serverUploadHistoryManager.getLatestUploadVersion();
        userData.setLatestUploadVersion(theLatestUploadVersion);

        if (settings != null) {
            userData.setSessionLength(settings.getSessionLength());
        }
        //current employee timeSlot start/end time
        TimeSlot currentEmployeeTimeSlot = commonServiceLocal.getCurrentEmployeeTimeSlot();
        String startHour = currentEmployeeTimeSlot.getStartHour() != null && !"00".equals(currentEmployeeTimeSlot.getStartHour()) ? currentEmployeeTimeSlot.getStartHour() : "09";
        String startMinute = currentEmployeeTimeSlot.getStartMin() != null && !"00".equals(currentEmployeeTimeSlot.getStartMin()) ? currentEmployeeTimeSlot.getStartMin() : "30";
        //
        String endHour = currentEmployeeTimeSlot.getEndHour() != null && !"00".equals(currentEmployeeTimeSlot.getEndHour()) ? currentEmployeeTimeSlot.getEndHour() : "18";
        String endMinute = currentEmployeeTimeSlot.getEndMin() != null && !"00".equals(currentEmployeeTimeSlot.getEndMin()) ? currentEmployeeTimeSlot.getEndMin() : "00";

        userData.setDefaultCurrentEmployeeTimeSlotStartTIME(startHour + ":" + startMinute);
        userData.setDefaultCurrentEmployeeTimeSlotEndTIME(endHour + ":" + endMinute);
        EdsModule trainingCenterEnabled = moduleManager.getModuleByCode(PermissionConstants.TRAINING_CENTER);
        userData.setEnableTraningCenterView(trainingCenterEnabled != null);
        EdsModule logisticsEnabled = moduleManager.getModuleByCode(PermissionConstants.LOGISTICS_MODULE);
        userData.setEnableLogistics(logisticsEnabled != null);
        EdsModule accountingEnabled = moduleManager.getModuleByCode(PermissionConstants.ACCOUNTING_MODULE);
        userData.setEnableAccountingModule(accountingEnabled != null);

        EdsModule productionEnabled = moduleManager.getModuleByCode(PermissionConstants.PRODUCTION);
        userData.setAccountingProductionEnabled(productionEnabled != null);

        EdsHostBasedSetting hostBasedSetting = hostBasedSettingManager.getByHostname(EdsContextParams.getHostname());
        if (hostBasedSetting != null && hostBasedSetting.getTawkToLink() != null) {
            userData.setTawkToSiteId(hostBasedSetting.getTawkToLink());
        }

        userData.setEnableSwitchableLayout(company.hasAccessToClassicUI() && StringUtils.isNotEmpty(globalAuthJdbcSpringManager.getClassicUIHost(company.getObjectID())));
        userData.setTestCompany(company.getTestCompany());
        userData.setTimeZone(user.getUserTimezone());
        userData.setEmailAccountSetup(messageCenterService.isEmailAccountSetup());

        return userData;
    }

    private String getEmployeeProfileImageContent(String initialName) {
        String result = "";
        /**
         * <span class="user-profile-img">
         *                                         <span class="user-profile-img__initials">TS</span>
         *                                     </span>
         * <span class="user-profile-img" style=" background-image:url('https://wfmtest.s3.amazonaws.com/c49913/u1/28480bb5-3bfc-45bf-9511-901f8f3c986a?AWSAccessKeyId=AKIAJJMKLWOMZUSCJLUQ&amp;Expires=1543128447&amp;Signature=Fra3guSxFv%2BMsnd8kZpDSIQyd4g%3D');">
         *                                         <img src="https://wfmtest.s3.amazonaws.com/c49913/u1/28480bb5-3bfc-45bf-9511-901f8f3c986a?AWSAccessKeyId=AKIAJJMKLWOMZUSCJLUQ&amp;Expires=1543128447&amp;Signature=Fra3guSxFv%2BMsnd8kZpDSIQyd4g%3D"/>
         *                                     </span>
         */
        SelectItem url = commonService.getEmployeeImageURL();
        if (url != null) {
            result = "<span class=\"user-profile-img\" style=\" background-image:url('" + url.getName() + "');\">\n" +
                    "<img src=\"" + url.getName() + "\"/>\n" +
                    "</span>";
        } else {
            result = "<span class=\"user-profile-img\"><span class=\"user-profile-img__initials\">" + initialName + "</span></span>";
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getRolesAsIntegersString() {
        EdsUser user = employeeManager.getUser();
        return user.getRolesAsIntegersString();
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Boolean isValid_User_For_Google_Gocs() {
        return googleDocumentsManager.validateUser();
    }

    @Transactional
    public ActivationLink getActivationLink(String key) {
        EdsActivationLink activationLink = activationLinkManager.getByKey(key);
        if (activationLink != null) {
            ActivationLink link = new ActivationLink();
            link.setObjectID(activationLink.getObjectID());
            link.setCompanyId(activationLink.getCompanyId());
            link.setUserId(activationLink.getUserId());
            link.setKey(activationLink.getKey());
            return link;
        }
        return null;
    }

    @Override
    @Transactional
    public void deleteActivationLink(Integer id) {
        final EdsActivationLink domain = this.activationLinkManager.get(id);

        if (domain == null || domain.isDeleted()) {
            return;
        }
        domain.setDeleted(true);
        domain.setUpdatedDate(new Date());
        activationLinkManager.update(domain);
    }

    public void registrGoogleServices(String userEmail, String accessToken) {
        if (accessToken == null || accessToken.trim().equals("")) {
            return;
        }
        EdsUser user = userManager.findUser(userEmail);
        EdsSinxDocuments document = googleDocumentsManager.getGoogleDocuments(user, true);
        if (document == null) {
            document = new EdsSinxDocuments();
        }
        document.setUser(user);
        document.setGoogleID(userEmail);
        document.setToken(accessToken);
        document.setActive(true);
        document.setAttempts(0);
        document.setReason(null);
        if (document.getObjectID() == null) {
            googleDocumentsManager.create(document);
        } else {
            googleDocumentsManager.update(document);
        }

        EdsServerContacts contacts = googleContactsManager.getGoogleContact(user, true);
        if (contacts == null) {
            contacts = new EdsServerContacts();
        }
        contacts.setUser(user);
        contacts.setToken(accessToken);
        contacts.setGoogleID(userEmail);
        contacts.setActive(true);
        contacts.setAttempts(0);
        contacts.setReason(null);
        if (document.getObjectID() == null) {
            googleContactsManager.create(contacts);
        } else {
            googleContactsManager.update(contacts);
        }

        EdsGoogleCalendar calendar = googleCalendarManager.getGoogleCalendar(user, true);
        if (calendar == null) {
            calendar = new EdsGoogleCalendar();
        }
        calendar.setUser(user);
        calendar.setToken(accessToken);
        calendar.setGoogleID(userEmail);
        calendar.setActive(true);
        calendar.setAttempts(0);
        calendar.setReason(null);
        if (calendar.getObjectID() == null) {
            googleCalendarManager.create(calendar);
        } else {
            googleCalendarManager.update(calendar);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public String getCompanyCurrencySymbol() {
        String symbol = "$";
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        EdsCurrency currency = null;
        if (financialSettings != null) {
            currency = financialSettings.getCurrency();
        }
        if (currency != null) {
            symbol = currency.getSymbol();
        }

        return symbol;
    }

    public void setTimeZone(String sessionId, String sessionTrackId, Integer minutes) {
        EdsUserSession userSession = userSessionManager.getUserSession(sessionId);
        if (minutes == null) {
            minutes = 0;
        }
        if (userSession == null || userSession.isSuperUser()) {
            return;
        }
        if (userSession.isShadow() == null) {
            userSession.setShadow(false);
        }
        EdsUser user = employeeManager.getUser();
        if (user == null) {
            user = userSession.getUser();
        }
        if (!userSession.isShadow() && user != null) {
            String timeZone;
            String gmt = "GMT-";
            timeZone = gmt + ServerUtils.timeSpentToString(minutes);
            if (minutes < 0) {
                gmt = "GMT+";
                minutes = (-1) * minutes;
                timeZone = gmt + ServerUtils.timeSpentToString(minutes);
            } else if (minutes == 0) {
                timeZone = "GMT";
            }

            user.setTimezone(timeZone);
        }

        try {
            if ((sessionTrackId != null) && !"null".equals(sessionTrackId)) {
                EdsUserSessionTracker userSessionTracker = userSessionTrackerManager.get(Integer.valueOf(sessionTrackId));
                userSessionTracker.setModuleLoadedTime(new Date());
                userSessionTrackerManager.update(userSessionTracker);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //Create default validity periods.
        if (validityPeriodManager.isFirstTime()) {
            validityPeriodManager.createDefaultValidityPeriods();
        }
    }

    @Transactional
    @Override
    public boolean sendForgotPasswordNotification(Integer userId, Map<Boolean, CompanyDomain> isKpi) throws EdsDbException {
        EdsUser user = userManager.get(userId);
        if (user != null && !user.getDeleted()) {
            try {
                messageManager.sendForgotPasswordNotification(user, isKpi);
                return true;
            } catch (EdsDbException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    @Override
    public ArrayList<Boolean> isValidUserOfficeAndGoogle(String storageType) {
        ArrayList<Boolean> settings = new ArrayList<>();
        settings.add(office365AuthService.isUserLinked(storageType));
        settings.add(office365AuthService.getUserAccessToken(EdsContextParams.getHost(), userManager.getUser(), storageType) != null);
        settings.add(googleCalendarManager.validateOfficeUser(userManager.getUser()));
        return settings;
    }

    public Boolean logout() {
        EdsUserSession userSession = userSessionManager.getUserLastAccessSession(userSessionManager.getUser());
        if (userSession != null) {
            userSession.setExpired(true);
            userSessionManager.update(userSession);
            return true;
        }
        return false;
    }

    @Override
    public LinkedHashMap<String, ArrayList<UserCompanyDTO>> getUserCompanyList() {
        HttpServletRequest request = ServletUtils.getRequest();

        Integer companyID = Integer.valueOf(ServerSecurityContext.getInstance().getCompanyId());
        boolean isSuperUser = userSessionManager.getUserSession(ServerSecurityContext.getInstance().getSessionId()).isSuperUser();
        ArrayList<UserCompanyDTO> companyDTOList = null;

        AuthInfoItem authInfoItem = RedisClient.getKey(ServerSecurityContext.getInstance().getSessionId(), AuthInfoItem.class);

        if (authInfoItem == null) {
            String username = globalAuthJdbcSpringManager.getUsername(companyID, userManager.getUser().getObjectID());
            companyDTOList = (ArrayList<UserCompanyDTO>) filterUserCompanyDTOList(globalAuthJdbcSpringManager.getAuthInfoByUsername(request.getServerName(), username));
        } else if (BaseLoginController.FROM_BASIC_LOGIN.equals(authInfoItem.getAuthType())) {
            companyDTOList = (ArrayList<UserCompanyDTO>) filterUserCompanyDTOList(globalAuthJdbcSpringManager.getAuthInfoByUsernameAndPassword(request.getServerName(), authInfoItem.getUsername(), authInfoItem.getPassword()));
        } else if (BaseLoginController.FROM_FEDERATED_LOGIN.equals(authInfoItem.getAuthType())) {

            if (StringUtils.isNotBlank(authInfoItem.getEmail())) {
                companyDTOList = (ArrayList<UserCompanyDTO>) filterUserCompanyDTOList(globalAuthJdbcSpringManager.getAuthInfoByUsername(request.getServerName(), authInfoItem.getEmail()));
            }
            if ((companyDTOList == null || companyDTOList.isEmpty()) && StringUtils.isNotBlank(authInfoItem.getSocialNetworkId())) {
                companyDTOList = (ArrayList<UserCompanyDTO>) filterUserCompanyDTOList(globalAuthJdbcSpringManager.getAuthInfoByUsername(request.getServerName(), authInfoItem.getSocialNetworkId()));
            }
        }

        LinkedHashMap<String, ArrayList<UserCompanyDTO>> map = new LinkedHashMap<>();
        if (companyDTOList == null || companyDTOList.isEmpty()) return map;

        for (UserCompanyDTO dto : companyDTOList) {
            boolean isCurrent = Objects.equals(dto.getCompanyID(), companyID);
            dto.setCurrent(isCurrent);
            if (!isCurrent) {
                dto.setClusterURL(
                        dto.getClusterURL()
                                + "&domain=" + dto.getSubdomainCompany()
                                + "&ACCOUNT_TYPE=" + (isSuperUser ? SUPER_USER : "")
                                + "&IS_MULTI_COMPANY=true"
                );
            }

            EdsCompany company = companyManager.get(dto.getCompanyID());

            NewCompany newCompany = new NewCompany();
            if (newCompany.getCompanyId() != null && company != null) {
                dto.setCompanyID(company.getObjectID());
            }

            String bucket;
            boolean notDeleted;

            if (company != null) {
                Boolean deletedFlag = company.isDeleted();
                notDeleted = (deletedFlag == null || !deletedFlag);

                EdsUsagePlan usagePlan = usagePlanManager.getCurrentUsagePlan(company);
                UsagePlanItem item = getUsagePlanItem(usagePlan, company.getObjectID());

                boolean paidPlan = usagePlan != null && Boolean.TRUE.equals(usagePlan.getPaid());
                boolean currSub = item != null && item.isCurrSub();
                boolean itemPaid = item != null && item.isPaid();
                boolean itemFree = item != null && item.isFree();

                if (paidPlan) {
                    bucket = "active";
                } else if (currSub && (itemFree || itemPaid)) {
                    bucket = "free";
                } else {
                    bucket = "expired";
                }
            } else {
                notDeleted = true;
                bucket = "expired";
            }

            dto.setStatus(bucket);
            if (notDeleted) {
                map.computeIfAbsent(bucket, k -> new ArrayList<>()).add(dto);
            }
        }

        return map;
    }

    public UsagePlanItem getUsagePlanItem(EdsUsagePlan usagePlan, Integer companyId) {
        UsagePlanItem result = new UsagePlanItem();
        if (usagePlan != null) {
            UsagePlanItem item = myAccountServiceLocal.getParametr(usagePlan);
            result.setFree(item.isFree());
            result.setPaid(usagePlan.getPaid());
            result.setCurrSub(true);
        } else {
//            EdsUsagePlan lastUsagePlan = usagePlanManager.getLastUsagePlan(companyId);
            result.setCurrSub(false);
            result.setPaid(false);
            result.setFree(true);
        }
        return result;
    }

    @Override
    @Transactional
    public void setUserDeviceTypeAndToken(Integer userID, String deviceType, String deviceToken) {
        if (userID == null || userID <= 0 || StringUtil.isEmpty(deviceType) || StringUtil.isEmpty(deviceToken)) {
            return;
        }
        EdsUser edsUser = userManager.get(userID);
        if (edsUser == null) {
            return;
        }
        edsUser.setMobileDeviceType(DeviceTypeEnum.valueOf(deviceType));
        edsUser.setDeviceToken(deviceToken);
        userManager.update(edsUser);
    }

    public String loginWithEmail(String email, String userAgent, String hostUrl) {
        try {
            List<UserCompanyDTO> companyDTOList = filterUserCompanyDTOList(globalAuthJdbcSpringManager.getAuthInfoByUsername(StringUtils.isNotBlank(hostUrl) ? hostUrl : HOST_LIVE, email));

            log.info("USER {} COMPANIES SIZE = {}", email, companyDTOList.size());

            if (!companyDTOList.isEmpty()) {//user has only one company
                AuthDetails authDetails = fillAuthDetails(userAgent, companyDTOList.get(0));
                log.info("Companyid= {}", authDetails.getCompanyID());
                log.info("Userid= {}", authDetails.getUserID());
                log.info("Database= {}", authDetails.getDatabase());
                //Obtaining session
                String sessionID = sessionService.obtainSession(authDetails);
                return sessionID;
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return null;
    }

    public UserCompanyDTO filterUserCompanyDTOList(UserCompanyDTO userCompanyDTO) {

        log.info("userCompanyDTO.getCompanyID()={}", userCompanyDTO.getCompanyID());
        log.info("userCompanyDTO.getClusterDbName()={}", userCompanyDTO.getClusterDbName());

        ServerSecurityContext.getInstance().setCompanyId(userCompanyDTO.getCompanyID());
        ServerSecurityContext.getInstance().setDatabase(userCompanyDTO.getClusterDbName());
        EdsCompany company = companyManager.get(userCompanyDTO.getCompanyID());
        log.info("userCompanyDTO.getUserID(): {}", userCompanyDTO.getUserID());
        EdsUser user = null;
        if (company != null) {
            try {
                user = userManager.get(userCompanyDTO.getUserID());
            } catch (Exception e) {
                //schema dosnt exists
            }
        }
        log.info("usernotnull={} company != null:{}", user != null, company != null);
        if (company != null && user != null && !user.getDeleted() && EMPLOYEE_STATUS_ACTIVE.equals(userManager.getUserStatus(user.getObjectID()))) {
            if (Constants.USER_TYPE_BMT_RESPONDENT.equals(user.getUserType())) { // is survey respondent
                return null;
            }
            if (user instanceof EdsClientContact) {
                if (user.getClientContact().getAccess() == null || (user.getClientContact().getAccess() != null && !user.getClientContact().getAccess())) {
                    log.debug("Client contact access is false for user {}", user.getObjectID());
                    return null;
                }
            }
            userCompanyDTO.setClusterURL(ServerUtils.getWebURL(userCompanyDTO));
            userCompanyDTO.setCompanyName(company.getName());
            String logo = getCompanyLogoURL(company);
            if (logo == null) {
                logo = "/no-logo.gif";
            }
            userCompanyDTO.setLogo(logo);
            userCompanyDTO.setFullName(user.getFullName());
            EdsCompanySystemSettings companySystemSettings = companySystemSettingsManager.findByCompanyID(userCompanyDTO.getCompanyID());
            if (companySystemSettings != null) {
                userCompanyDTO.setPasswordExpirationDayCount(companySystemSettings.getPasswordExpirationDayCount());
            }
            return userCompanyDTO;
        }

        return null;
    }

    public List<UserCompanyDTO> filterUserCompanyDTOList(List<UserCompanyDTO> urlList) {

        List<UserCompanyDTO> companyDTOList = new ArrayList<>();
        for (UserCompanyDTO userCompanyDTO : urlList) {

            Thread thread = new Thread(() -> {
                ServerSecurityContext.getInstance().setDatabase(userCompanyDTO.getClusterDbName());
                ServerSecurityContext.getInstance().setCompanyId(userCompanyDTO.getCompanyID());

                EdsCompany company = companyManager.get(userCompanyDTO.getCompanyID());
                EdsUser user = null;

                if (company != null) {
                    try {
                        user = userManager.get(userCompanyDTO.getUserID());
                    } catch (Exception e) {
                        //schema dosnt exists
                        log.error("User doesnt exist: companyid={}, userid={}", userCompanyDTO.getCompanyID(), userCompanyDTO.getUserID());
                    }
                }
                if (company != null && user != null && !user.getDeleted() && EMPLOYEE_STATUS_ACTIVE.equals(userManager.getUserStatus(user.getObjectID()))) {
                    if (Constants.USER_TYPE_BMT_RESPONDENT.equals(user.getUserType())) { // is survey respondent
                        return;
                    }
                    if (user instanceof EdsClientContact) {
                        if (user.getClientContact().getAccess() == null || (user.getClientContact().getAccess() != null && !user.getClientContact().getAccess())) {
                            return;
                        }
                    }
                    userCompanyDTO.setClusterURL(ServerUtils.getWebURL(userCompanyDTO));
                    userCompanyDTO.setCompanyName(company.getName());
                    String logo = getCompanyLogoURL(company);

                    if (logo == null) {
                        logo = "/no-logo.gif";
                    }
                    userCompanyDTO.setLogo(logo);
                    userCompanyDTO.setFullName(user.getFullName());
                    EdsCompanySystemSettings companySystemSettings = companySystemSettingsManager.findByCompanyID(userCompanyDTO.getCompanyID());

                    if (companySystemSettings != null) {
                        userCompanyDTO.setPasswordExpirationDayCount(companySystemSettings.getPasswordExpirationDayCount());
                    }
                    companyDTOList.add(userCompanyDTO);
                }
            });

            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return companyDTOList;
    }

    private AuthDetails fillAuthDetails(String userAgent, UserCompanyDTO userDetails) {
        AuthDetails authDetails = new AuthDetails();
        authDetails.setCompanyID(userDetails.getCompanyID());
        authDetails.setDatabase(userDetails.getClusterDbName());
        authDetails.setUserID(userDetails.getUserID());
        authDetails.setUserAgent(userAgent);
        return authDetails;
    }
}

