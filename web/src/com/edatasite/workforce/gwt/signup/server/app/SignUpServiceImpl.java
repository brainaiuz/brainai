package com.edatasite.workforce.gwt.signup.server.app;

import com.edatasite.shared.components.PasswordGenerator;
import com.edatasite.shared.db.EdsDbException;
import com.edatasite.workforce.appContext.SpringPropertiesUtil;
import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCompanySystemSettings;
import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeDepartment;
import com.edatasite.workforce.core.domain.EdsProject;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRegion;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsTimeSlot;
import com.edatasite.workforce.core.domain.EdsTimeSlotItem;
import com.edatasite.workforce.core.domain.EdsUsagePlan;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.rbac.EdsGroup;
import com.edatasite.workforce.core.domain.rbac.EdsTrustee;
import com.edatasite.workforce.core.domain.security.EdsAllowedIPAddress;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.core.domain.settings.EdsGenericSettings;
import com.edatasite.workforce.core.tools.EdsSchemaUpdater;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.core.tools.GlobalAuthManager;
import com.edatasite.workforce.gwt.availability.client.rpc.AvailabilityService;
import com.edatasite.workforce.gwt.availability.server.app.AvailabilityServiceLocal;
import com.edatasite.workforce.gwt.backend.client.rpc.BackendService;
import com.edatasite.workforce.gwt.contact.server.app.ContactServiceLocal;
import com.edatasite.workforce.gwt.contactcategory.server.ContactCategoryServiceLocal;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.RegistrationTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.controllers.login.marketplace.DomainInfo;
import com.edatasite.workforce.gwt.core.server.db.ActivationLinkManager;
import com.edatasite.workforce.gwt.core.server.db.AddressManager;
import com.edatasite.workforce.gwt.core.server.db.AnnualLeaveAllowanceManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.CompanySystemSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.CountryManager;
import com.edatasite.workforce.gwt.core.server.db.CurrencyManager;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeDepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.JdbcSpringManager;
import com.edatasite.workforce.gwt.core.server.db.LeaveReasonManager;
import com.edatasite.workforce.gwt.core.server.db.LocaleManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RegionManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.SignupMessageManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSlotItemManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSlotManager;
import com.edatasite.workforce.gwt.core.server.db.TimeZoneManager;
import com.edatasite.workforce.gwt.core.server.db.UserEmailSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.GroupManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.TrusteeManager;
import com.edatasite.workforce.gwt.core.server.db.security.AllowedIPAddressManager;
import com.edatasite.workforce.gwt.core.server.db.security.SpamDomainManager;
import com.edatasite.workforce.gwt.core.server.db.settings.CompanySettingsManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.enums.TemplateSchema;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.ApiEmployeeProfilePicUploadEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.EmployeeEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.SystemFolderEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.customevents.ProjectCustomEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.documents.server.app.DocumentsServiceLocal;
import com.edatasite.workforce.gwt.employee.client.rpc.EmployeeService;
import com.edatasite.workforce.gwt.invoice.server.app.InvoiceServiceLocal;
import com.edatasite.workforce.gwt.modulesettings.client.ModuleService;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanItem;
import com.edatasite.workforce.gwt.myaccount.server.app.MyAccountServiceLocal;
import com.edatasite.workforce.gwt.newemployee.client.rpc.NewEmployee;
import com.edatasite.workforce.gwt.signup.client.rpc.CreatedCompany;
import com.edatasite.workforce.gwt.signup.client.rpc.NewCompany;
import com.edatasite.workforce.gwt.signup.client.rpc.SignUpService;
import com.edatasite.workforce.utils.EdsContextParams;
import com.edatasite.workforce.utils.redis.RedisClient;
import com.finnetlimited.reportservice.core.server.db.schema.ReportingManager;
import com.finnetlimited.reportservice.core.server.domain.schema.EdsReport;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service("signUpService")
public class SignUpServiceImpl implements SignUpService, SignUpServiceLocal, Constants {

    private static final Logger log = LoggerFactory.getLogger(SignUpServiceImpl.class);

    private final PasswordGenerator pg = new PasswordGenerator(6);
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private TimeZoneManager zoneManager;
    @Autowired
    private CountryManager countryManager;
    @Autowired
    private RegionManager regionManager;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private TimeSlotManager timeSlotManager;
    @Autowired
    private TimeSlotItemManager timeSlotItemManager;
    @Autowired
    private UserManager userManager;
    @Autowired
    private DepartmentManager departmentManager;
    @Autowired
    private CurrencyManager currencyManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private CommonService commonService;
    @Autowired
    private CompanySystemSettingsManager companySystemSettingsManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ContactServiceLocal contactServiceLocal;
    @Autowired
    private ContactCategoryServiceLocal contactCategoryServiceLocal;
    @Autowired
    private GlobalAuthManager globalAuthManager;
    @Autowired
    private CompanySettingsManager companySettingsManager;
    @Autowired
    private TrusteeManager trusteeManager;
    @Autowired
    private GroupManager groupManager;
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    private PlatformTransactionManager transactionManager;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private JdbcSpringManager jdbcSpringManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private AddressManager addressManager;
    @Autowired
    private LeaveReasonManager leaveReasonManager;
    @Autowired
    private AllowedIPAddressManager allowedIPAddressManager;
    @Autowired
    private SpamDomainManager spamDomainManager;
    @Autowired
    private DocumentsServiceLocal documentsServiceLocal;
    @Autowired
    private MyAccountServiceLocal myAccountServiceLocal;

    public static final String ENGLISH = "en";
    public static final String DUTCH = "nl";
    public static final String RUSSIAN = "ru";
    public static final String SPANISH = "es";
    public static final String TURKEY = "tr";
    public static final String PORTUGUESE = "pt";
    public static final String ARABIC = "ar";

    @Autowired
    private UserEmailSettingsManager userEmailSettingsManager;
    @Autowired
    private AnnualLeaveAllowanceManager annualLeaveAllowanceManager;
    @Autowired
    private AvailabilityService availabilityService;
    @Autowired
    private AvailabilityServiceLocal availabilityServiceLocal;
    @Autowired
    private ReportingManager reportingManager;
    @Autowired
    private BackendService backendService;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    private InvoiceServiceLocal invoiceServiceLocal;
    @Autowired
    private LocaleManager localeManager;
    @Autowired
    private EmployeeDepartmentManager employeeDepartmentManager;

    private MessageSource messageSource;

    @Autowired
    public void setMessageSource(@Qualifier("messageSource") MessageSource messageSource) {
        this.messageSource = messageSource;
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SelectItem[] getCountries() {
        return commonService.getCountries(true);
    }

    public ArrayList<SelectItem> getSupportedLocales() {
        ArrayList<SelectItem> locales = new ArrayList<>();
        locales.add(new SelectItem(0, WordUtils.capitalize(Locale.ENGLISH.getDisplayLanguage(Locale.ENGLISH)), Locale.ENGLISH.getLanguage()));
        locales.add(new SelectItem(1, "Dutch", DUTCH));
        locales.add(new SelectItem(2, WordUtils.capitalize(new Locale("ru").getDisplayLanguage(new Locale("ru"))), new Locale("ru").getLanguage()));
        locales.add(new SelectItem(3, WordUtils.capitalize(new Locale("es").getDisplayLanguage(new Locale("es"))), new Locale("es").getLanguage()));
        locales.add(new SelectItem(4, WordUtils.capitalize(new Locale("tr").getDisplayLanguage(new Locale("tr"))), new Locale("tr").getLanguage()));
        locales.add(new SelectItem(5, WordUtils.capitalize(new Locale("pt").getDisplayLanguage(new Locale("pt"))), new Locale("pt").getLanguage()));
        locales.add(new SelectItem(6, WordUtils.capitalize(new Locale("ar").getDisplayLanguage(new Locale("ar"))), new Locale("ar").getLanguage()));
        return locales;
    }


    @Autowired
    private SignupMessageManager signupMessageManager;

    @Autowired
    private ActivationLinkManager activationLinkManager;

    @Transactional
    public Integer getCompany() {
        EdsCompany comp = new EdsCompany();
        companyManager.create(comp);
        companyManager.flush();
        ServerSecurityContext.getInstance().setCompanyId(comp.getObjectID());
        return comp.getObjectID();
    }

    @Transactional
    public CreatedCompany createCompany(NewCompany company) {

        EdsCompany comp;
        if (company.getCompanyId() == null) {
            comp = new EdsCompany();
        } else {
            comp = companyManager.get(company.getCompanyId());
        }

        //mark it as not free now
        comp.setFree(false);
        comp.setIsSetUp(company.isSetUp());
        if (company.getName() != null && !"".equals(company.getName())) {
            comp.setName(StringEscapeUtils.unescapeHtml(company.getName()));
        } else {
            comp.setName(company.getName());
        }
        if (company.getAdminFName() != null && !"".equals(company.getAdminFName())) {
            company.setAdminFName(StringEscapeUtils.unescapeHtml(company.getAdminFName()));
        }
        if (company.getAdminLName() != null && !"".equals(company.getAdminLName())) {
            company.setAdminLName(StringEscapeUtils.unescapeHtml(company.getAdminLName()));
        }
        EdsCountry country = null;
        if (company.getCountryID() != null) {
            country = countryManager.get(company.getCountryID());
        } else if (company.getCountryCode() != null) {
            country = countryManager.getCountryByCode(company.getCountryCode().toUpperCase());
        }
        if (country == null) {
            country = countryManager.getCountryByCode("US");
        }

        if ((company.getPhone() != null) || !"".equals(company.getPhone())) {
            comp.setPhone(company.getCallCode() != null ? company.getCallCode() + company.getPhone() : company.getPhone());
        }
        if (company.getAdminEmail() != null && !"".equals(company.getAdminEmail())) {
            comp.setEmail(company.getAdminEmail());
        }
        if ("".equals(company.getSignedUpPage())) {
            company.setSignedUpPage(MY_WORKSPACE);
        }
        if (company.getSignedUpPage() != null) {
            if (!(PRM.equals(company.getSignedUpPage()) || PRM2.equals(company.getSignedUpPage()) || ACC.equals(company.getSignedUpPage()) || MY_WORKSPACE.equals(company.getSignedUpPage()))) {
                comp.setIsSetUp(true);
            }
            comp.setSignedUpPage(company.getSignedUpPage());
        }
        if (zoneManager.getCountryZones(country).size() > 0) {
            comp.setCountryZone(zoneManager.getCountryZones(country).get(0));
            if (company.getStateID() != null) {
                EdsRegion region = regionManager.get(company.getStateID());
                if (region != null && region.getCountry() != null && country.getObjectID().equals(region.getCountry().getObjectID())) {
                    comp.setCountryRegion(region);
                }
            }
        }
        if (country.getObjectID().equals(46)) {
            // If country = United States of America, as a default state
            //will be setted Washington id=55 (EdsRegion) to support weather in Workspace
            comp.setCountryRegion(regionManager.get(55));
        }
        comp.setRegistrationDate(comp.getCompanyDate());
        if (company.getWorkArea() != null) {
            EdsReference workarea = referenceManager.get(company.getWorkArea());
            comp.setWorkArea(workarea);
        }
        if (company.getClientSingUpIPAddress() != null) {
            comp.setSigupCompIP(company.getClientSingUpIPAddress());
        }

        companyManager.createOrUpdate(comp);

        EdsCurrency currency = null;
        if (company.getCurrencyID() != null) {
            currency = currencyManager.get(company.getCurrencyID());
        } else if (country.getCurrency() != null) {
            currency = country.getCurrency();
        } else {
            currency = currencyManager.getCurrency(CurrencyManager.USD);
        }

        if (currency != null) {
            EdsFinancialSettings financialSettings = new EdsFinancialSettings();
            financialSettings.setCurrency(currency);
            financialSettingsManager.createOrUpdate(financialSettings);
        }

        Locale locale = new Locale(company.getLocale());
        EdsTimeSlot timeSlot = saveDefaultTimeSlot(comp, locale);
        comp.setDefaultTimeSlot(timeSlot.getObjectID());
        String remoteAddr = company.getClientSingUpIPAddress();
        NewEmployee employeeListItem = new NewEmployee();
        employeeListItem.setFname(company.getAdminFName());
        employeeListItem.setLname(company.getAdminLName());
        employeeListItem.setEmail(company.getAdminEmail());
        employeeListItem.setWphone(company.getCallCode() != null ? company.getCallCode() + company.getPhone() : company.getPhone());
        employeeListItem.setCountry(company.getCountryID());
        employeeListItem.setPhotoURL(company.getAdminSocialImageUrl());
        employeeListItem.setRegistrationType(company.getRegistrationType());
        employeeListItem.setSocialUserName(company.getSocialUserName());
        employeeListItem.setActive(company.isReset());

        Integer employeeID = employeeService.createEmployee(employeeListItem, true);

        String password = userManager.findActiveAndNonFederateLoginUsers(company.getHost(), company.getAdminEmail());
        //if there is no active users for login data generate new password
        boolean userExists = false;
        if (password != null) {
            userExists = true;
        } else {
            password = company.getAdminPassword() != null ? company.getAdminPassword() : pg.generateAsString();
        }

        EdsReference activeStatus = referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_ACTIVE);
        EdsEmployee employee = employeeManager.get(employeeID);
        employee.setTimeSlot(timeSlot);
        if (userExists) {
            employee.setAccountStatus(activeStatus);
        }
        employeeManager.createOrUpdate(employee);

        //user gets registration notification with activation link
        // in the case of federated login notification won't sent


        roleManager.addRole(employee, EdsRole.DR);
        roleManager.addRole(employee, EdsRole.ADMIN);
        roleManager.addRole(employee, EdsRole.TL);
        roleManager.addRole(employee, EdsRole.MEM);

        //if registration type is social network, we use social network id as username. eg:FACEBOOK:685448769513763
        if (employee.getRegistrationType() != null && !RegistrationTypeEnum.EMAIL.getType().equalsIgnoreCase(employee.getRegistrationType().getType())) {
            employee.setUserName(company.getSocialUserName());
        } else {
            employee.setUserName(company.getAdminEmail());
        }
        employee.setEmail(company.getAdminEmail());
        employee.setPassword(password);

        if (!company.isReset()) {
            userManager.saveUserAuthenticationData(employee, comp.getObjectID(), !userExists, false);
        }

        employee.setRandom(ServerUtils.randomstring());

        availabilityServiceLocal.createOrUpdateLeaveAllowance(employeeID);

        comp.setCreator(employee.getObjectID());

        //User settings
        userEmailSettingsManager.getUserSettings(employee).setInternationalization(company.getLocale());
        //Create company settings
        EdsCompanySettings companySettings = userManager.getUser().getCompany().getCompanySettings();
        if (companySettings == null) {
            companySettings = new EdsCompanySettings();
            //Enable upload types by default (Google, Office365)
            companySettings.setEnableUploadTypes("true;true;true");
        }

        companySettings.setThemeForSystem(company.getTheme() != null ? company.getTheme() : EdsContextParams.getDefaultTheme());
        companySettingsManager.update(companySettings);

        //if from federated login
        if (company.getFromFederatedLogin()) {
            employee.setFromFederated(true);
            employee.setAccountStatus(activeStatus);
        }
        if (SIGNED_UP_FROM_IPHONE.equals(company.getCompanySignedUpFrom()) || SIGNED_UP_FROM_ANDROID.equals(company.getCompanySignedUpFrom()) ||
                //Activation link will not be sent if registration type is social
                (company.getRegistrationType() != null && !RegistrationTypeEnum.EMAIL.getType().equalsIgnoreCase(company.getRegistrationType().getType()))) {
            employee.setAccountStatus(activeStatus);
        }
        /*//If registered from mobile by email, user status should be active as social sign up to enter personal profile from mobile app.
        //But activation link should be sent anyway.
        if (RegistrationTypeEnum.EMAIL.getType().equalsIgnoreCase(company.getRegistrationType().getType())) {
            //Just in case, below two fields set to be true even they are true above
            includeActivationLink = true;
            sendRegistrationNotification = true;
            employee.setAccountStatus(referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_ACTIVE));
        }*/

        EdsDepartment department = new EdsDepartment();
        String defaultDepartmentName = messageSource.getMessage("createSignUpCompany.defaultDepartmentNameCompanyEmployees", null, "Company employees", locale);
        department.setName(defaultDepartmentName);
        department.setLeader(employee);
        department.setCreator(employee);
        department.setUpdater(employee);
        department.setStartDate(comp.getCompanyDate());
        departmentManager.create(department);
        EdsEmployeeDepartment empDept = new EdsEmployeeDepartment(department, employee);
        employeeDepartmentManager.create(empDept);
        employee.setEmployeeDepartmentId(empDept.getObjectID());
        employeeManager.update(employee);
        comp.setDefaultDepartment(department.getObjectID());
        // create default Project
        EdsProject project = new EdsProject();
        String defaultProjectName = messageSource.getMessage("createSignUpCompany.nonProjectRelatedHoursName", null, "Non-project related hours", locale);
        project.setName(defaultProjectName);
        String defaultProjectDescription = messageSource.getMessage("createSignUpCompany.defaultCompanyProjectDescription", null, "Default company project", locale);
        project.setDescription(defaultProjectDescription);
        project.setStartDate(comp.getCompanyDate());
        project.setManager(employee);
        project.setStatus(referenceManager.findReference(EdsProject.PROJECT_STATUS, EdsProject.ONGOING));
        project.setCompletedDate(null);
        project.setDeleted(false);
        project.setCreator(employee);
        projectManager.create(project);
        comp.setDefaultProject(project.getObjectID());
        Integer defaultProjectID = project.getObjectID();
        baseEventPostProcessor.registerEvent(ProjectCustomEventListenerImpl.TYPE, ProjectCustomEventListenerImpl.EVENT_PROJECT_ADD_TO_SOLR, project, employee);
        // create Crm Activities Project
        project = new EdsProject();
        String defaultCRMActivityProjectName = messageSource.getMessage("createSignUpCompany.crmActivitiesProjectName", null, "CRM Activities", locale);
        project.setName(defaultCRMActivityProjectName);
        String defaultCRMActivityProjectDescription = messageSource.getMessage("createSignUpCompany.crmActivitiesProjectDescription", null, "CRM Activities project", locale);
        project.setDescription(defaultCRMActivityProjectDescription);
        project.setStartDate(comp.getCompanyDate());
        project.setManager(employee);
        project.setStatus(referenceManager.findReference(EdsProject.PROJECT_STATUS, EdsProject.ONGOING));
        project.setCompletedDate(null);
        project.setDeleted(false);
        project.setCreator(employee);
        project.setCrmActivityProject(true);
        projectManager.create(project);
        baseEventPostProcessor.registerEvent(ProjectCustomEventListenerImpl.TYPE, ProjectCustomEventListenerImpl.EVENT_PROJECT_ADD_TO_SOLR, project, employee);
        initDefaultMembershis(employee);

        // add Employee default project
        Integer[] defaultProjectMember = {employee.getObjectID()};
        commonService.addMembers(defaultProjectID, defaultProjectMember);
        commonService.addMembers(project.getObjectID(), defaultProjectMember);
        comp.setActive(company.isActive());

        CreatedCompany cComp = new CreatedCompany();
        cComp.setAdminUserName(employee.getUserName());
        cComp.setAdminPwd(password);
        cComp.setAdminId(employee.getObjectID());
        cComp.setCompanyId(comp.getObjectID());
        cComp.setHasAccount(userExists);
        cComp.setCurrencyId(currency.getObjectID());
        comp.setHasChat(true);
        comp.setAnyDataMissing(company.isRedirectToSettings());

        contactCategoryServiceLocal.createSystemContactCategories(FROM_SIGNUP_CREATED);
        contactServiceLocal.updateContactsWithNoCategories();
        //Create ATTENDANCERAWDATA
        availabilityService.createAttendaceRawDataRecords(employee.getObjectID(), 0);
        //Create company system folders
        // TODO: create system folders task 2
        baseEventPostProcessor.registerEvent(SystemFolderEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, comp, employee);
        if (company.isReset()) {
            reindexSolrInSeparateThread(false, comp.getObjectID());
        } else {
            log.info("@Base event reindex employee core: {}", comp.getObjectID());
            //Employee add to solr
            baseEventPostProcessor.registerEvent(EmployeeEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, employee, userManager.getUser());
        }
        //Employee profile pic upload
        if (StringUtils.isNotBlank(employee.getSocialImageUrl())) {
            baseEventPostProcessor.registerEvent(ApiEmployeeProfilePicUploadEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, employee, userManager.getUser());
        }
        //ServerSecurityContext.getInstance().setCompanyID((String) null);

        if (!company.isReset()) {
            saveCompanySystemSettings(company, comp);
            saveCompanySettings(comp);

            //we're puting company host to cluster company for managing hostable things
            globalAuthJdbcSpringManager.registerCompanyHost(comp.getObjectID(), company.getHost());
            String serviceId = company.getServiceId() != null ? company.getServiceId() : SpringPropertiesUtil.getProperty("kpi.discovery.service-id");
            globalAuthJdbcSpringManager.registerCompanyService(comp.getObjectID(), serviceId);

            if (SIGNED_UP_FROM_IPHONE.equals(company.getCompanySignedUpFrom()) || SIGNED_UP_FROM_ANDROID.equals(company.getCompanySignedUpFrom())) {
                createFreeTrialUsagePlanForMobile(comp.getObjectID(), company.isUk(), company.getHost());
            } else if (SIGNED_UP_FROM_SUBSIDIARIES.equals(company.getCompanySignedUpFrom())) {
                createFreeTrialUsagePlan(comp.getObjectID(), UK.equals(country.getCode()), 4, company.getHost(), "");
            }

            if (SIGNED_UP_FROM_GOOGLE_MARKETPLACE.equals(company.getCompanySignedUpFrom())) {
                //This is logic that updates clustercompany
                globalAuthJdbcSpringManager.updateCompanyDomainInfo(comp.getObjectID(), company.getGoogleAppsDomain());

                String subjectTitle = "Marketplace Sign Up Notification";
                try {
                    messageManager.sendCompanyRegistrationNotificationOnlySupport(employee, comp, remoteAddr, subjectTitle);
                } catch (EdsDbException e) {
                    return null;
                }
            } else if (SIGNED_UP_FROM_IPHONE.equals(company.getCompanySignedUpFrom()) || SIGNED_UP_FROM_ANDROID.equals(company.getCompanySignedUpFrom())) {
                try {
                    final Map<String, Object> companyInfo = new HashMap<>();

                    companyInfo.put("companyID", comp.getObjectID());
                    companyInfo.put("companyName", comp.getName());
                    companyInfo.put("password", cComp.getAdminPwd());
                    signupMessageManager.sendFromMobileCompanyRegistrationNotification(employee, companyInfo, cComp.isHasAccount());
                } catch (EdsDbException e) {
                    return null;
                }
            } else if (SIGNED_UP_FROM_SUBSIDIARIES.equals(company.getCompanySignedUpFrom())) {
                try {
                    signupMessageManager.sendCompanyRegistrationNotificationToSystem(employee, comp, remoteAddr);
                } catch (EdsDbException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    signupMessageManager.sendCompanyRegistrationNotificationToSystem(employee, comp, remoteAddr);
                } catch (EdsDbException e) {
                    return null;
                }
            }
            //Create module codes from HostBasedModule
            if (company.getHost() != null && comp.getObjectID() != null) {
                HashSet<String> codes = moduleService.getHostBasedModule(company.getHost(), true);
                log.info("*************** Create new company modules, host name is " + company.getHost() + " codes count is " + codes.size() + " ******************");
                if (codes != null && !codes.isEmpty()) {
                    moduleService.save(comp.getObjectID(), codes, true);
                }
            }
        }
        return cComp;
    }

    @Transactional
    public CreatedCompany createSampleCompany(NewCompany company, TemplateSchema templateSchema) {
        boolean isTestCompany = false;

        if (TemplateSchema.TEMPLATE.equals(templateSchema)) {
            isTestCompany = true;
        }

        EdsCompany comp;
        if (company.getCompanyId() == null) {
            comp = new EdsCompany();
        } else {
            comp = companyManager.get(company.getCompanyId());
        }

        comp.setFree(false);
        comp.setIsSetUp(company.isSetUp());
        comp.setTestCompany(isTestCompany);
        comp.setAccountingSetup(false);

        if (company.getName() != null && !"".equals(company.getName())) {
            comp.setName(StringEscapeUtils.unescapeHtml(company.getName()));
        } else {
            comp.setName(company.getName());
        }
        if (company.getAdminFName() != null && !"".equals(company.getAdminFName())) {
            company.setAdminFName(StringEscapeUtils.unescapeHtml(company.getAdminFName()));
        }
        if (company.getAdminLName() != null && !"".equals(company.getAdminLName())) {
            company.setAdminLName(StringEscapeUtils.unescapeHtml(company.getAdminLName()));
        }
        if (StringUtils.isNotBlank(company.getPromoCode())) {
            comp.setPromoCode(StringEscapeUtils.unescapeHtml(company.getPromoCode()));
        }
        //Country/State/Region
        EdsCountry country = null;
        if (company.getCountryCode() != null) {
            country = countryManager.getCountryByCode(company.getCountryCode().toUpperCase());
        } else if (company.getCountryID() != null) {
            country = countryManager.get(company.getCountryID());
        }

        if (country == null) {
            country = countryManager.getCountryByCode("US");
        }

        company.setCountryID(country.getObjectID());
        if (company.getPhone() != null && company.getPhone().contains(country.getTelCode())) {
            company.setPhone(company.getPhone().substring(country.getTelCode().length()));
        }

        if (zoneManager.getCountryZones(country).size() > 0) {
            comp.setCountryZone(zoneManager.getCountryZones(country).get(0));
            if (company.getStateID() != null) {
                EdsRegion region = regionManager.get(company.getStateID());
                if (region != null && region.getCountry() != null && country.getObjectID().equals(region.getCountry().getObjectID())) {
                    comp.setCountryRegion(region);
                }
            }
        }
        if (country.getObjectID().equals(46)) {
            // If country = United States of America, as a default state
            //will be setted Washington id=55 (EdsRegion) to support weather in Workspace
            comp.setCountryRegion(regionManager.get(55));
        }
        //Ends Country/State/Region

        if ((company.getPhone() != null) || !"".equals(company.getPhone())) {
            comp.setPhone(company.getCallCode() != null ? company.getCallCode() + company.getPhone() : company.getPhone());
        }
        if (company.getAdminEmail() != null && !"".equals(company.getAdminEmail())) {
            comp.setEmail(company.getAdminEmail());
        }
        if ("".equals(company.getSignedUpPage())) {
            company.setSignedUpPage(MY_WORKSPACE);
        }
        if (company.getSignedUpPage() != null) {
            if (!(PRM.equals(company.getSignedUpPage()) || PRM2.equals(company.getSignedUpPage()) || ACC.equals(company.getSignedUpPage()) || MY_WORKSPACE.equals(company.getSignedUpPage()))) {
                comp.setIsSetUp(true);
            }
            comp.setSignedUpPage(company.getSignedUpPage());
        }

        comp.setRegistrationDate(comp.getCompanyDate());
        if (company.getWorkArea() != null) {
            EdsReference workarea = referenceManager.get(company.getWorkArea());
            comp.setWorkArea(workarea);
        }
        if (company.getClientSingUpIPAddress() != null) {
            comp.setSigupCompIP(company.getClientSingUpIPAddress());
        }
        comp.setLocale(localeManager.getLocaleBylanguageCode(company.getLocale()) != null ? company.getLocale() : null);

        companyManager.createOrUpdate(comp);

        //Set Billing && Mailing Addresses
        if (comp.getBillingAddresses() == null || comp.getBillingAddresses().isEmpty()) {
            EdsAddress billingAddress = new EdsAddress();
            billingAddress.setEntity(comp);
            billingAddress.setRelationType(EdsAddress.BILLING_ADDRESS);
            billingAddress.setCountry(country);
            billingAddress.setState(comp.getCountryRegion());
            addressManager.create(billingAddress);

            List<EdsAddress> billingAddresses = new ArrayList<>();
            billingAddresses.add(billingAddress);
            comp.setBillingAddresses(billingAddresses);
        }


        if (comp.getMailingAddresses() == null || comp.getMailingAddresses().isEmpty()) {
            EdsAddress mailingAddress = new EdsAddress();
            mailingAddress.setEntity(comp);
            mailingAddress.setRelationType(EdsAddress.MAILING_ADDRESS);
            mailingAddress.setCountry(country);
            mailingAddress.setState(comp.getCountryRegion());
            addressManager.create(mailingAddress);

            List<EdsAddress> mailingAddresses = new ArrayList<>();
            mailingAddresses.add(mailingAddress);
            comp.setMailingAddresses(mailingAddresses);
        }


        EdsTimeSlot timeSlot = timeSlotManager.get(1);
        comp.setDefaultTimeSlot(timeSlot.getObjectID());
        String remoteAddr = company.getClientSingUpIPAddress();

        String password = userManager.findActiveAndNonFederateLoginUsers(company.getHost(), company.getAdminEmail());
        //if there is no active users for login data generate new password
        boolean userExists = false;
        if (password != null) {
            userExists = true;
        } else {
            password = company.getAdminPassword() != null ? company.getAdminPassword() : pg.generateAsString();
        }

        boolean isStarterOrPro = List.of(
                TemplateSchema.TEXTILE_FINDS_STARTER,
                TemplateSchema.TEXTILE_FINDS_PRO
        ).contains(templateSchema);

        boolean isStarterProPremium = List.of(
                TemplateSchema.TEXTILE_FINDS_STARTER,
                TemplateSchema.TEXTILE_FINDS_PRO,
                TemplateSchema.TEXTILE_FINDS_PREMIUM
        ).contains(templateSchema);

        EdsReference activeStatus = referenceManager.findReference(EMPLOYEE_STATUS, EMPLOYEE_STATUS_ACTIVE);
        EdsEmployee employee = isStarterOrPro ? employeeManager.get(3) : employeeManager.get(1);
        employee.setTimeSlot(timeSlot);
        if (userExists) {
            employee.setAccountStatus(activeStatus);
        }

        if (Boolean.TRUE.equals(employee.getDeleted())) {
            employee.setAccountStatus(activeStatus);
            employee.setDeleted(false);
        }

        if (isStarterProPremium) {
            Optional.ofNullable(employee.getCompany())
                    .ifPresent(c -> {
                        c.setAccountingSetup(true);
                        c.setSalesSetup(true);
                    });
        }

        if (isStarterOrPro) {
            employee.getRoles().stream()
                    .filter(Objects::nonNull)
                    .map(EdsRole::getRolePermissions)
                    .filter(Objects::nonNull)
                    .flatMap(Collection::stream)
                    .filter(p -> "PAYROLL_MAIN_MENU".equals(p.getPermissioncode())
                            || "PAYROLL_SETTINGS".equals(p.getPermissioncode()))
                    .forEach(p -> p.setPriviledgeCode("DENY"));
        }

        employeeManager.createOrUpdate(employee);

        //user gets registration notification with activation link
        boolean includeActivationLink = true;
        boolean sendRegistrationNotification = true;  // in the case of federated login notification won't sent

        if (!isStarterOrPro) {
            roleManager.addRole(employee, EdsRole.DR);
            roleManager.addRole(employee, EdsRole.ADMIN);
            roleManager.addRole(employee, EdsRole.TL);
            roleManager.addRole(employee, EdsRole.MEM);
        }

        //if registration type is social network, we use social network id as username. eg:FACEBOOK:685448769513763
        if (employee.getRegistrationType() != null && !RegistrationTypeEnum.EMAIL.getType().equalsIgnoreCase(employee.getRegistrationType().getType())) {
            employee.setUserName(company.getSocialUserName());
        } else {
            employee.setUserName(company.getAdminEmail());
        }
        employee.setEmail(company.getAdminEmail());
        employee.setPassword(password);

        employee.setFirstName(company.getAdminFName());
        employee.setLastName(company.getAdminLName());
        employee.setEmail(company.getAdminEmail());
        employee.getContact().setPrimaryPhone(company.getCallCode() != null ? company.getCallCode() + company.getPhone() : company.getPhone());
        employee.setRegistrationType(company.getRegistrationType());

        employee.getContact();

        userManager.saveUserAuthenticationData(employee, comp.getObjectID(), !userExists, false);

        employee.setRandom(ServerUtils.randomstring());
        comp.setCreator(employee.getObjectID());

        //User settings
        userEmailSettingsManager.getUserSettings(employee).setInternationalization(company.getLocale());

        //if from federated login
        if (company.getFromFederatedLogin()) {
            sendRegistrationNotification = false;
            employee.setFromFederated(true);
            employee.setAccountStatus(activeStatus);
        }
        if (SIGNED_UP_FROM_IPHONE.equals(company.getCompanySignedUpFrom()) || SIGNED_UP_FROM_ANDROID.equals(company.getCompanySignedUpFrom()) ||
                //Activation link will not be sent if registration type is social
                (company.getRegistrationType() != null && !RegistrationTypeEnum.EMAIL.getType().equalsIgnoreCase(company.getRegistrationType().getType()))) {
            includeActivationLink = false;
            sendRegistrationNotification = true;
            employee.setAccountStatus(activeStatus);
        }
        initDefaultMembershis(employee);

        comp.setDefaultDepartment(1);
        comp.setDefaultProject(1);

        // add Employee default project
        Integer[] defaultProjectMember = {employee.getObjectID()};
        commonService.addMembers(1, defaultProjectMember);
        commonService.addMembers(1, defaultProjectMember);
        comp.setActive(company.isActive());

        CreatedCompany cComp = new CreatedCompany();
        cComp.setAdminUserName(employee.getUserName());
        cComp.setAdminPwd(password);
        cComp.setAdminId(employee.getObjectID());
        cComp.setCompanyId(comp.getObjectID());
        cComp.setDatabase(SecurityContext.getInstance().getDatabase());
        cComp.setHasAccount(userExists);
        comp.setHasChat(true);
        comp.setAnyDataMissing(company.isRedirectToSettings());

        //Create company settings
        saveCompanySystemSettings(company, comp);
        saveCompanySettings(comp);
        //we're puting company host to cluster company for managing hostable things
        globalAuthJdbcSpringManager.registerCompanyHost(comp.getObjectID(), company.getHost());
        String serviceId = company.getServiceId() != null ? company.getServiceId() : SpringPropertiesUtil.getProperty("kpi.discovery.service-id");
        globalAuthJdbcSpringManager.registerCompanyService(comp.getObjectID(), serviceId);

        //Reset overdue invoice statuses
        invoiceServiceLocal.resetCompanySalesInvoicesStatuses(comp.getObjectID());
        invoiceServiceLocal.resetCompanyPurchaseInvoicesStatuses(comp.getObjectID());

        if (SIGNED_UP_FROM_IPHONE.equals(company.getCompanySignedUpFrom()) || SIGNED_UP_FROM_ANDROID.equals(company.getCompanySignedUpFrom())) {
            createFreeTrialUsagePlanForMobile(comp.getObjectID(), company.isUk(), company.getHost());
        } else if (SIGNED_UP_FROM_SUBSIDIARIES.equals(company.getCompanySignedUpFrom())) {
            createFreeTrialUsagePlan(comp.getObjectID(), UK.equals(country.getCode()), 4, company.getHost(), "");
        }

        company.setExistingUser(userExists);
        if (includeActivationLink) {
            company.setActivationKey(activationLinkManager.saveActivationLink(comp.getObjectID(), employee.getObjectID(), null));
        }

        //Create module codes from HostBasedModule
        if (company.getHost() != null && comp.getObjectID() != null) {
            HashSet<String> codes = moduleService.getHostBasedModule(company.getHost(), true);
            log.info("*************** Create new company modules, host name is " + company.getHost() + " codes count is " + codes.size() + " ******************");
            if (codes != null && codes.size() > 0) {
                moduleService.save(comp.getObjectID(), codes, true);
            }
        }

        if (UK.equals(country.getCode()) || AE.equals(country.getCode()) || SA.equals(country.getCode()) || OM.equals(country.getCode()) || QA.equals(country.getCode()) || BH.equals(country.getCode()) || KW.equals(country.getCode())) {
            genericSettingsManager.saveGenericSettings(comp.getObjectID(), GenericSettingsEnum.VAT_RETURN_ENABLE, EdsGenericSettings.YES);
        }
        //Reindex solr cores
        reindexSolrInSeparateThread(isTestCompany, company.getCompanyId());

        if (SIGNED_UP_FROM_GOOGLE_MARKETPLACE.equals(company.getCompanySignedUpFrom())) {
            //This is logic that updates clustercompany
            globalAuthJdbcSpringManager.updateCompanyDomainInfo(comp.getObjectID(), company.getGoogleAppsDomain());

            String subjectTitle = "Marketplace Sign Up Notification";
            try {
                messageManager.sendCompanyRegistrationNotificationOnlySupport(employee, comp, remoteAddr, subjectTitle);
            } catch (EdsDbException e) {
                return null;
            }
        } else if (SIGNED_UP_FROM_IPHONE.equals(company.getCompanySignedUpFrom()) || SIGNED_UP_FROM_ANDROID.equals(company.getCompanySignedUpFrom())) {
            try {
                final Map<String, Object> companyInfo = new HashMap<>();

                companyInfo.put("companyID", comp.getObjectID());
                companyInfo.put("companyName", comp.getName());
                companyInfo.put("password", cComp.getAdminPwd());
                signupMessageManager.sendFromMobileCompanyRegistrationNotification(employee, companyInfo, cComp.isHasAccount());
            } catch (EdsDbException e) {
                return null;
            }
        } else {
            try {
                signupMessageManager.sendCompanyRegistrationNotificationToSystem(employee, comp, remoteAddr);

                //send activation link to user
                if (company.isSendNotification()) {
                    sendCompanyRegistrationNotification(company);
                }
            } catch (EdsDbException e) {
                return null;
            }
        }
        return cComp;
    }

    public void reindexSolrInSeparateThread(boolean testCompany, Integer companyId) {
        CompletableFuture.runAsync(() -> {
            try {
                reindexAllCores(testCompany, companyId);
                log.info("Reindexing finished for company ID: {}", companyId);
            } catch (Exception e) {
                log.info("Error during Solr reindexing ({}): {}", companyId, e.getMessage());
            }
        });
    }

    private void reindexAllCores(boolean testCompany, Integer companyId) {
        log.info("@Reindexing sample date for company solr cores => {}", companyId);
        SecurityContext.getInstance().setDatabase(DATABASE_FREE);
        SolrReindexRpc solrReindexRpc = new SolrReindexRpc();
        solrReindexRpc.setCompanyId(companyId);
        backendService.indexAllCoresOfSelectedCompany(solrReindexRpc);
        log.info("@Reindexing sample date for company solr cores finished => {}", companyId);
        if (testCompany) {
            companyManager.updateTestCompany(companyId);
            log.info("@Make it test company: {}", companyId);
        }
    }

    @Transactional(propagation = Propagation.NEVER)
    public String preProcessSampleCompanyData(Integer companyId, TemplateSchema template) {
        ServerSecurityContext.getInstance().setDatabase(DATABASE_FREE);

        String schemaName;
        if (companyId == null) {
            Integer expectedID = companyManager.getMaxSchemaId() + 1;
            if (expectedID <= 23000) {
                expectedID = 23000 + 1;
            }
            schemaName = expectedID.toString();
        } else {
            schemaName = String.valueOf(companyId);
        }

        String slashedName = "\"" + schemaName + "\"";
        try {
            log.info("Moving schema: {}", schemaName);
            String templateSchema = jdbcSpringManager.getTemplateSchema(template.getPattern());
            jdbcTemplate.execute("ALTER SCHEMA " + templateSchema + " RENAME TO " + slashedName);
        } catch (Throwable t) {
            throw new RuntimeException("Error During Schema Clone with schema name - " + schemaName, t);
        }

        //schema update is already handled should be removed on commit
        try {
            EdsSchemaUpdater.setDbUrl(null);
            EdsSchemaUpdater.updateSchema(schemaName);
        } catch (Throwable t) {
            jdbcTemplate.update("DROP SCHEMA " + slashedName + " CASCADE");
            log.error(t.getMessage());
            throw new RuntimeException("Error During Schema Export with schema name - " + schemaName, t);
        }

        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);

        try {
            log.info("Starting to execute generateAccountNumberFunction with slashedName: {}", slashedName);
            jdbcTemplate.update(generateAccountNumberFunction(slashedName));
            log.info("Successfully executed generateAccountNumberFunction for slashedName: {}", slashedName);

            log.info("Starting manual schema update for slashedName: {}", slashedName);
            EdsSchemaUpdater.manualUpdateWhatWasNotUpdatedByCallingMethod(slashedName);
            log.info("Completed manual schema update for slashedName: {}", slashedName);

            log.info("Preparing to batch delete user sessions for schema: {}", slashedName);
            jdbcTemplate.batchUpdate("DELETE FROM " + slashedName + ".myUserSessionTrack", "DELETE FROM " + slashedName + ".myUserSession");
            log.info("Batch delete of user sessions completed for schema: {}", slashedName);

            log.info("Registering schema to global authentication: {}", schemaName);
            registerToGlobalAuth(schemaName);
            log.info("Schema successfully registered to global authentication: {}", schemaName);

            log.info("Committing transaction for schema: {}", schemaName);
            transactionManager.commit(status);
            log.info("Transaction committed successfully for schema: {}", schemaName);
        } catch (DataAccessException e) {
            log.error("DataAccessException occurred, rolling back transaction for schema: {}", schemaName, e);
            transactionManager.rollback(status);
            throw e;
        } catch (Exception e) {
            log.error("Exception occurred, rolling back transaction for schema: {}", schemaName, e);
            transactionManager.rollback(status);
            throw new RuntimeException(e);
        }

        log.info("Cloning schema has beed finished: {}", schemaName);
        return schemaName;
    }

    private void initDefaultMembershis(EdsEmployee employee) {
        EdsTrustee userTrustee = trusteeManager.getTrustee(employee);
        List<EdsGroup> builtinGroups = groupManager.getCompanyDefaultGroups();
        for (EdsGroup gr : builtinGroups) {
            if (gr.getConstantName().equals(EdsGroup.ADMINISTRATORS)
                    || gr.getConstantName().equals(EdsGroup.DIRECTORS)
                    || gr.getConstantName().equals(EdsGroup.DEPARTMENT_LEADERS)
                    || gr.getConstantName().equals(EdsGroup.MEMBERS)) {
                employee.getMembershipGroups().add(gr);
                gr.getMembers().add(userTrustee);
            }
        }
    }

    private void saveCompanySettings(EdsCompany company) {
        EdsCompanySettings companySettings = new EdsCompanySettings();
        companySettingsManager.create(companySettings);
        if (companySettings.getObjectID() != null) {
            company.setCompanySettings(companySettings);
        }
    }

    private String[] initDefaultDataWithScript(String schema, HashSet<String> activeModules, TemplateSchema templateSchema) {
        ArrayList<String> queries = new ArrayList<>();

        String insertModelData = "INSERT INTO " + schema + ".model (active, formid, title, viewname, stepform) " +
                "                SELECT active, formid, title, viewname, stepform " +
                "                FROM " + templateSchema.getSlashedSchema() + ".model";
        queries.add(insertModelData);

        String insertCustomForSection = "INSERT INTO " + schema + ".customformsection (form_ID, section, sorder, active, custom, expanded) " +
                "                SELECT form_ID, section, sorder, active, custom, expanded " +
                "                FROM " + templateSchema.getSlashedSchema() + ".customformsection";
        queries.add(insertCustomForSection);

        String insertDyanmicFormColumns = "INSERT INTO " + schema + ".modelfield (label, form_ID, field_ID, widget, type, mandatory, systemmandatory, hide, isCustomField, fsection, noLabelFor, source, usableByWorkflow, disableUpdate, isEntityField, hideInCustomizeForm, isWorkflowAttribute, columnType, forder, customizabletable) " +
                "                SELECT label, form_ID, field_ID, widget, type, mandatory, systemmandatory, hide, isCustomField, fsection, noLabelFor, source, usableByWorkflow, disableUpdate, isEntityField, hideInCustomizeForm, isWorkflowAttribute, columnType, forder, customizabletable" +
                "                FROM " + templateSchema.getSlashedSchema() + ".modelfield";
        queries.add(insertDyanmicFormColumns);

        String referenceColorString = "INSERT INTO " + schema + ".referencecolor (id, name, hex) " +
                "SELECT id,name,hex FROM " + templateSchema.getSlashedSchema() + ".referencecolor; \n";
        queries.add(referenceColorString);

        String referenceLocale = "INSERT INTO " + schema + ".reference_locale (id, arabic, english, russian, uzbek) " +
                "SELECT tmp.id, tmp.arabic, tmp.english, tmp.russian, tmp.uzbek FROM " +
                "(SELECT * FROM " + templateSchema.getSlashedSchema() + ".reference_locale) AS tmp;";
        queries.add(referenceLocale);
        String referenceString = "INSERT INTO " + schema + ".reference (id, code, description, name, sorder, parentid, cssstyle, antonym, isRemovable, isSystemReference, deleted, shared, referencecolorid, localeid) " +
                "SELECT tmp.id,tmp.code,tmp.description,tmp.name,tmp.sorder,tmp.parentid,tmp.cssstyle, tmp.antonym, tmp.isRemovable, tmp.isSystemReference, tmp.deleted, tmp.shared, tmp.referencecolorid, tmp.localeid FROM " +
                "(SELECT * FROM " + templateSchema.getSlashedSchema() + ".reference) AS tmp;";
        queries.add(referenceString);

        String leaveReasonString = "INSERT INTO " + schema + ".leave_reason (code, name, description,isSystemReference, isActive, hasProrata,probationDays,leaveDays, shortname,attendanceLR, autoApprove, color, gender, unitType, typeOption, includeDayOffs, includeHolidays, deleted) " +
                "SELECT tmp.code, tmp.name, tmp.description,tmp.isSystemReference, tmp.isActive, tmp.hasProrata,tmp.probationDays,tmp.leaveDays, tmp.shortname,tmp.attendanceLR, tmp.autoApprove, tmp.color, tmp.gender, tmp.unitType, tmp.typeOption, tmp.includeDayOffs, tmp.includeHolidays, tmp.deleted FROM " +
                "(SELECT * FROM " + templateSchema.getSlashedSchema() + ".leave_reason) AS tmp;";
        queries.add(leaveReasonString);

        String reasonRelations = "INSERT INTO " + schema + ".leave_reason_relation (reason_code, relatedtype, relationid) " +
                "SELECT reason_code, relatedtype, relationid FROM " + templateSchema.getSlashedSchema() + ".leave_reason_relation;";
        queries.add(reasonRelations);


        String rolesString = "INSERT INTO " + schema + ".role " +
                "(id, name, code, description, issystem, isdeleted, isentityspecific, sorder, active, module_code ) " +
                "SELECT r.id, r.name, r.code, r.description, r.issystem, r.isdeleted, isentityspecific, r.sorder, r.active, r.module_code FROM " +
                "(SELECT * FROM " + templateSchema.getSlashedSchema() + ".role) AS r;";
        queries.add(rolesString);
        String rolesPermissionString = "INSERT INTO " + schema + ".rolepermission " +
                "(id, permissioncode, rolecode, access) " +
                "SELECT rp.id, rp.permissioncode, rp.rolecode, rp.access FROM " +
                "(SELECT * FROM " + templateSchema.getSlashedSchema() + ".rolepermission) AS rp;";
        queries.add(rolesPermissionString);

        String reportingPermissionString = "INSERT INTO " + schema + ".reportingpermission " +
                " (code,context,name,sorder,ismainmenu,parent,iscore,modulecode,isadvancedmode,companyid) " +
                " select code,context,name,sorder,ismainmenu,parent,iscore,modulecode,isadvancedmode,companyid " +
                " from " + templateSchema.getSlashedSchema() + ".reportingpermission on conflict do nothing;";
        queries.add(reportingPermissionString);

        String trusteeString = "INSERT INTO " + schema + ".trustee " +
                "(id,trusteeid,trusteetype) " +
                "SELECT tmp.id,tmp.trusteeid,tmp.trusteetype FROM " +
                "(SELECT * FROM " + templateSchema.getSlashedSchema() + ".trustee) AS tmp;";
        queries.add(trusteeString);
        String groupString = "INSERT INTO " + schema + ".trusteegroup  " +
                "(id,constantname,description,entrytype,name,owner_id) " +
                "SELECT tmp.id,tmp.constantname,tmp.description,tmp.entrytype,tmp.name,tmp.owner_id FROM " +
                "(SELECT * FROM " + templateSchema.getSlashedSchema() + ".trusteegroup) AS tmp;";
        queries.add(groupString);
        String relationshipString = "INSERT INTO " + schema + ".relationship  " +
                "(id,code,entityname,entrytype,name,rank,relationtype) " +
                "SELECT tmp.id,tmp.code,tmp.entityname,tmp.entrytype,tmp.name,tmp.rank,tmp.relationtype FROM " +
                "(SELECT * FROM " + templateSchema.getSlashedSchema() + ".relationship) AS tmp;";
        queries.add(relationshipString);

        String dropColumnSql = "ALTER TABLE " + schema + ".purchaseinvoice DROP COLUMN IF EXISTS reversecharge_applicable;";
        queries.add(dropColumnSql);

        String addColumnSql = "ALTER TABLE " + schema + ".purchaseinvoice ADD COLUMN reversecharge_applicable BOOLEAN DEFAULT FALSE;";
        queries.add(addColumnSql);

        String taskpermissionString = "INSERT INTO " + schema + ".taskpermission " +
                "(id,assigneeedit,assigneestatusedit,assigneeview,delete,edit,fullcontrol,permissionsedit,statusedit,timesheetentryadd,view) " +
                "SELECT tmp.id,tmp.assigneeedit,tmp.assigneestatusedit,tmp.assigneeview,tmp.delete,tmp.edit,tmp.fullcontrol,tmp.permissionsedit,tmp.statusedit,tmp.timesheetentryadd,tmp.view FROM " +
                "(SELECT * FROM " + templateSchema.getSlashedSchema() + ".taskpermission) AS tmp;";
        queries.add(taskpermissionString);
        String taskPolicyString = "INSERT INTO " + schema + ".taskpolicy  " +
                "(id,description,entrytype,trusteeid,permissionid,relationid) " +
                "SELECT tmp.id,tmp.description,tmp.entrytype,tmp.trusteeid,tmp.permissionid,tmp.relationid FROM " +
                "(SELECT * FROM " + templateSchema.getSlashedSchema() + ".taskpolicy) AS tmp;";
        queries.add(taskPolicyString);
        String folderPermissionString = "INSERT INTO " + schema + ".folderpermission " +
                "(id,modifyacl,read,version,write,delete) " +
                "SELECT tmp.id,tmp.modifyacl,tmp.read,tmp.version,tmp.write,tmp.delete FROM " +
                "(SELECT * FROM " + templateSchema.getSlashedSchema() + ".folderpermission ) AS tmp;";
        queries.add(folderPermissionString);
        String contactPermissionString = "INSERT INTO " + schema + ".contactpermission " +
                "(id,modifyacl,read,version,write,delete) " +
                "SELECT tmp.id,tmp.modifyacl,tmp.read,tmp.version,tmp.write,tmp.delete FROM " +
                "(SELECT * FROM " + templateSchema.getSlashedSchema() + ".contactpermission ) AS tmp;";
        queries.add(contactPermissionString);
        if (TemplateSchema.GYM.equals(templateSchema)) {
            String insertCustomFieldLocalization = "INSERT INTO " + schema + ".customformlocalization (id, arabicname, defaultname, deleted, englishname, form_id, russianname, section, type, uzbekname, parentid) " +
                    "SELECT cf.id, cf.arabicname, cf.defaultname, cf.deleted, cf.englishname, cf.form_id, cf.russianname, cf.section, cf.type, cf.uzbekname, cf.parentid FROM " +
                    "(SELECT * FROM " + templateSchema.getSlashedSchema() + ".customformlocalization) AS cf;";
            queries.add(insertCustomFieldLocalization);

            String insertCustomFieldSettings = "INSERT INTO " + schema + ".companycustomfieldssettings (id, active, addtab, aliasname, creationdate, issuperuser, modificationdate, clickable, columncode, columnwidth, custom_logic_value, datatype, deleted, disabled, entitycategoryalias, entitycategoryname, entityname, fieldname, isfacetable, isrequired, lookuptype, minchar, min_height, numberminvalue, predefinedvalues, predefinedvalueswithsorting, prefix, query, quizformscorevalues, relationfieldid, relationfieldvalues, relationship, scale, seeownpermission, showinfiltergrouping, showinlisting, uitype, useinpermission, customformlocalizationid, custom_logic_field_id, entitytypeid, referenceid) " +
                    "SELECT ccfs.id, ccfs.active, ccfs.addtab, ccfs.aliasname, ccfs.creationdate, ccfs.issuperuser, ccfs.modificationdate, ccfs.clickable, ccfs.columncode, ccfs.columnwidth, ccfs.custom_logic_value, ccfs.datatype, ccfs.deleted, ccfs.disabled, ccfs.entitycategoryalias, ccfs.entitycategoryname, ccfs.entityname, ccfs.fieldname, ccfs.isfacetable, ccfs.isrequired, ccfs.lookuptype, ccfs.minchar, ccfs.min_height, ccfs.numberminvalue, ccfs.predefinedvalues, ccfs.predefinedvalueswithsorting, ccfs.prefix, ccfs.query, ccfs.quizformscorevalues, ccfs.relationfieldid, ccfs.relationfieldvalues, ccfs.relationship, ccfs.scale, ccfs.seeownpermission, ccfs.showinfiltergrouping, ccfs.showinlisting, ccfs.uitype, ccfs.useinpermission, ccfs.customformlocalizationid, ccfs.custom_logic_field_id, ccfs.entitytypeid, ccfs.referenceid " +
                    "FROM (SELECT * FROM " + templateSchema.getSlashedSchema() + ".companycustomfieldssettings) AS ccfs;";
            queries.add(insertCustomFieldSettings);

            String q = "INSERT INTO " + schema + ".workflowrule " +
                    "(id, deleted, description, executioncriteria, executioncriteriaupdatefield, module, name, pattern, rulecriteria, active, recurrenceid, showinlist) " +
                    "SELECT tmp.id, tmp.deleted, tmp.description, tmp.executioncriteria, tmp.executioncriteriaupdatefield, tmp.module, tmp.name, tmp.pattern, tmp.rulecriteria, tmp.active, tmp.recurrenceid, tmp.showinlist FROM " +
                    "(SELECT * FROM " + templateSchema.getSlashedSchema() + ".workflowrule) AS tmp;";
            queries.add(q);
            q = "INSERT INTO " + schema + ".workflow_alerts " +
                    "(id, content, deleted, extrakeyvalues, recepient, replyto, subject, tobcc, ccemails, emailtemplateid, workflow, workflowactionstarttimeunit, workflowactionstarttime, workflowactionstarttimegranularity, isworkflowactiontimebased) " +
                    "SELECT tmp.id, tmp.content, tmp.deleted, tmp.extrakeyvalues, tmp.recepient, tmp.replyto, tmp.subject, tmp.tobcc, tmp.ccemails, tmp.emailtemplateid, tmp.workflow, tmp.workflowactionstarttimeunit, tmp.workflowactionstarttime, tmp.workflowactionstarttimegranularity, tmp.isworkflowactiontimebased FROM " +
                    "(SELECT * FROM " + templateSchema.getSlashedSchema() + ".workflow_alerts) AS tmp;";
            queries.add(q);
            q = "INSERT INTO " + schema + ".approvers " +
                    "(id,approvestatusid, approver_order, deleted, entity_id, entitytype, is_default, onapprovedaction, onrejectedaction, rejectstatusid, startstatusid, stepemployeetype, workflow, rejected_workflow, status) " +
                    "SELECT tmp.id,tmp.approvestatusid, tmp.approver_order, tmp.deleted, tmp.entity_id, tmp.entitytype, tmp.is_default, tmp.onapprovedaction, tmp.onrejectedaction, tmp.rejectstatusid, tmp.startstatusid, tmp.stepemployeetype, tmp.workflow, tmp.rejected_workflow, tmp.status FROM " +
                    "(SELECT * FROM " + templateSchema.getSlashedSchema() + ".approvers) AS tmp;";
            queries.add(q);
            q = "INSERT INTO " + schema + ".approver_roles " +
                    "(approver_id,role_id) " +
                    "SELECT tmp.approver_id,tmp.role_id FROM " +
                    "(SELECT * FROM " + templateSchema.getSlashedSchema() + ".approver_roles) AS tmp;";
            queries.add(q);


            q = "INSERT INTO " + schema + ".locationcustomfields (id, date_value1, date_value10, date_value11, date_value12, date_value13, date_value14, date_value15, date_value16, date_value17, date_value18, date_value19, date_value2, date_value20, date_value21, date_value22, date_value23, date_value24, date_value25, date_value26, date_value27, date_value28, date_value29, date_value3, date_value30, date_value31, date_value32, date_value33, date_value34, date_value35, date_value36, date_value37, date_value38, date_value39, date_value4, date_value40, date_value41, date_value42, date_value43, date_value44, date_value45, date_value46, date_value47, date_value48, date_value49, date_value5, date_value50, date_value6, date_value7, date_value8, date_value9, double_value1, double_value10, doublevalue100, double_value11, double_value12, double_value13, double_value14, double_value15, double_value16, double_value17, double_value18, double_value19, double_value2, double_value20, double_value21, double_value22, double_value23, double_value24, double_value25, double_value26, double_value27, double_value28, double_value29, double_value3, double_value30, double_value31, double_value32, double_value33, double_value34, double_value35, double_value36, double_value37, double_value38, double_value39, double_value4, double_value40, double_value41, double_value42, double_value43, double_value44, double_value45, double_value46, double_value47, double_value48, double_value49, double_value5, double_value50, double_value51, double_value52, double_value53, double_value54, double_value55, double_value56, double_value57, double_value58, double_value59, double_value6, double_value60, double_value61, double_value62, double_value63, double_value64, double_value65, double_value66, double_value67, double_value68, double_value69, double_value7, double_value70, double_value71, double_value72, double_value73, double_value74, double_value75, double_value76, double_value77, double_value78, double_value79, double_value8, double_value80, double_value81, double_value82, double_value83, double_value84, double_value85, double_value86, double_value87, double_value88, double_value89, double_value9, double_value90, double_value91, double_value92, double_value93, double_value94, double_value95, double_value96, double_value97, double_value98, double_value99, jsonentities, string_value1, string_value10, stringvalue100, stringvalue101, stringvalue102, stringvalue103, stringvalue104, stringvalue105, stringvalue106, stringvalue107, stringvalue108, stringvalue109, string_value11, stringvalue110, stringvalue111, stringvalue112, stringvalue113, stringvalue114, stringvalue115, stringvalue116, stringvalue117, stringvalue118, stringvalue119, string_value12, stringvalue120, stringvalue121, stringvalue122, stringvalue123, stringvalue124, stringvalue125, stringvalue126, stringvalue127, stringvalue128, stringvalue129, string_value13, stringvalue130, stringvalue131, stringvalue132, stringvalue133, stringvalue134, stringvalue135, stringvalue136, stringvalue137, stringvalue138, stringvalue139, string_value14, stringvalue140, stringvalue141, stringvalue142, stringvalue143, stringvalue144, stringvalue145, stringvalue146, stringvalue147, stringvalue148, stringvalue149, string_value15, stringvalue150, string_value16, string_value17, string_value18, string_value19, string_value2, string_value20, string_value21, string_value22, string_value23, string_value24, string_value25, string_value26, string_value27, string_value28, string_value29, string_value3, string_value30, string_value31, string_value32, string_value33, string_value34, string_value35, string_value36, string_value37, string_value38, string_value39, string_value4, string_value40, string_value41, string_value42, string_value43, string_value44, string_value45, string_value46, string_value47, string_value48, string_value49, string_value5, string_value50, string_value51, string_value52, string_value53, string_value54, string_value55, string_value56, string_value57, string_value58, string_value59, string_value6, string_value60, string_value61, string_value62, string_value63, string_value64, string_value65, string_value66, string_value67, string_value68, string_value69, string_value7, string_value70, string_value71, string_value72, string_value73, string_value74, string_value75, string_value76, string_value77, string_value78, string_value79, string_value8, string_value80, string_value81, string_value82, string_value83, string_value84, string_value85, string_value86, string_value87, string_value88, string_value89, string_value9, string_value90, string_value91, string_value92, string_value93, string_value94, string_value95, string_value96, string_value97, string_value98, string_value99) " +
                    "SELECT tmp.id, tmp.date_value1, tmp.date_value10, tmp.date_value11, tmp.date_value12, tmp.date_value13, tmp.date_value14, tmp.date_value15, tmp.date_value16, tmp.date_value17, tmp.date_value18, tmp.date_value19, tmp.date_value2, tmp.date_value20, tmp.date_value21, tmp.date_value22, tmp.date_value23, tmp.date_value24, tmp.date_value25, tmp.date_value26, tmp.date_value27, tmp.date_value28, tmp.date_value29, tmp.date_value3, tmp.date_value30, tmp.date_value31, tmp.date_value32, tmp.date_value33, tmp.date_value34, tmp.date_value35, tmp.date_value36, tmp.date_value37, tmp.date_value38, tmp.date_value39, tmp.date_value4, tmp.date_value40, tmp.date_value41, tmp.date_value42, tmp.date_value43, tmp.date_value44, tmp.date_value45, tmp.date_value46, tmp.date_value47, tmp.date_value48, tmp.date_value49, tmp.date_value5, tmp.date_value50, tmp.date_value6, tmp.date_value7, tmp.date_value8, tmp.date_value9, tmp.double_value1, tmp.double_value10, tmp.doublevalue100, tmp.double_value11, tmp.double_value12, tmp.double_value13, tmp.double_value14, tmp.double_value15, tmp.double_value16, tmp.double_value17, tmp.double_value18, tmp.double_value19, tmp.double_value2, tmp.double_value20, tmp.double_value21, tmp.double_value22, tmp.double_value23, tmp.double_value24, tmp.double_value25, tmp.double_value26, tmp.double_value27, tmp.double_value28, tmp.double_value29, tmp.double_value3, tmp.double_value30, tmp.double_value31, tmp.double_value32, tmp.double_value33, tmp.double_value34, tmp.double_value35, tmp.double_value36, tmp.double_value37, tmp.double_value38, tmp.double_value39, tmp.double_value4, tmp.double_value40, tmp.double_value41, tmp.double_value42, tmp.double_value43, tmp.double_value44, tmp.double_value45, tmp.double_value46, tmp.double_value47, tmp.double_value48, tmp.double_value49, tmp.double_value5, tmp.double_value50, tmp.double_value51, tmp.double_value52, tmp.double_value53, tmp.double_value54, tmp.double_value55, tmp.double_value56, tmp.double_value57, tmp.double_value58, tmp.double_value59, tmp.double_value6, tmp.double_value60, tmp.double_value61, tmp.double_value62, tmp.double_value63, tmp.double_value64, tmp.double_value65, tmp.double_value66, tmp.double_value67, tmp.double_value68, tmp.double_value69, tmp.double_value7, tmp.double_value70, tmp.double_value71, tmp.double_value72, tmp.double_value73, tmp.double_value74, tmp.double_value75, tmp.double_value76, tmp.double_value77, tmp.double_value78, tmp.double_value79, tmp.double_value8, tmp.double_value80, tmp.double_value81, tmp.double_value82, tmp.double_value83, tmp.double_value84, tmp.double_value85, tmp.double_value86, tmp.double_value87, tmp.double_value88, tmp.double_value89, tmp.double_value9, tmp.double_value90, tmp.double_value91, tmp.double_value92, tmp.double_value93, tmp.double_value94, tmp.double_value95, tmp.double_value96, tmp.double_value97, tmp.double_value98, tmp.double_value99, tmp.jsonentities, tmp.string_value1, tmp.string_value10, tmp.stringvalue100, tmp.stringvalue101, tmp.stringvalue102, tmp.stringvalue103, tmp.stringvalue104, tmp.stringvalue105, tmp.stringvalue106, tmp.stringvalue107, tmp.stringvalue108, tmp.stringvalue109, tmp.string_value11, tmp.stringvalue110, tmp.stringvalue111, tmp.stringvalue112, tmp.stringvalue113, tmp.stringvalue114, tmp.stringvalue115, tmp.stringvalue116, tmp.stringvalue117, tmp.stringvalue118, tmp.stringvalue119, tmp.string_value12, tmp.stringvalue120, tmp.stringvalue121, tmp.stringvalue122, tmp.stringvalue123, tmp.stringvalue124, tmp.stringvalue125, tmp.stringvalue126, tmp.stringvalue127, tmp.stringvalue128, tmp.stringvalue129, tmp.string_value13, tmp.stringvalue130, tmp.stringvalue131, tmp.stringvalue132, tmp.stringvalue133, tmp.stringvalue134, tmp.stringvalue135, tmp.stringvalue136, tmp.stringvalue137, tmp.stringvalue138, tmp.stringvalue139, tmp.string_value14, tmp.stringvalue140, tmp.stringvalue141, tmp.stringvalue142, tmp.stringvalue143, tmp.stringvalue144, tmp.stringvalue145, tmp.stringvalue146, tmp.stringvalue147, tmp.stringvalue148, tmp.stringvalue149, tmp.string_value15, tmp.stringvalue150, tmp.string_value16, tmp.string_value17, tmp.string_value18, tmp.string_value19, tmp.string_value2, tmp.string_value20, tmp.string_value21, tmp.string_value22, tmp.string_value23, tmp.string_value24, tmp.string_value25, tmp.string_value26, tmp.string_value27, tmp.string_value28, tmp.string_value29, tmp.string_value3, tmp.string_value30, tmp.string_value31, tmp.string_value32, tmp.string_value33, tmp.string_value34, tmp.string_value35, tmp.string_value36, tmp.string_value37, tmp.string_value38, tmp.string_value39, tmp.string_value4, tmp.string_value40, tmp.string_value41, tmp.string_value42, tmp.string_value43, tmp.string_value44, tmp.string_value45, tmp.string_value46, tmp.string_value47, tmp.string_value48, tmp.string_value49, tmp.string_value5, tmp.string_value50, tmp.string_value51, tmp.string_value52, tmp.string_value53, tmp.string_value54, tmp.string_value55, tmp.string_value56, tmp.string_value57, tmp.string_value58, tmp.string_value59, tmp.string_value6, tmp.string_value60, tmp.string_value61, tmp.string_value62, tmp.string_value63, tmp.string_value64, tmp.string_value65, tmp.string_value66, tmp.string_value67, tmp.string_value68, tmp.string_value69, tmp.string_value7, tmp.string_value70, tmp.string_value71, tmp.string_value72, tmp.string_value73, tmp.string_value74, tmp.string_value75, tmp.string_value76, tmp.string_value77, tmp.string_value78, tmp.string_value79, tmp.string_value8, tmp.string_value80, tmp.string_value81, tmp.string_value82, tmp.string_value83, tmp.string_value84, tmp.string_value85, tmp.string_value86, tmp.string_value87, tmp.string_value88, tmp.string_value89, tmp.string_value9, tmp.string_value90, tmp.string_value91, tmp.string_value92, tmp.string_value93, tmp.string_value94, tmp.string_value95, tmp.string_value96, tmp.string_value97, tmp.string_value98, tmp.string_value99 FROM " +
                    "(SELECT * FROM " + templateSchema.getSlashedSchema() + ".locationcustomfields) AS tmp;";
            queries.add(q);

            q = "INSERT INTO " + schema + ".location (id, city, code, deleted, email, fax, intnumber, name, ownersid, phone, timeslotid, zipcode, city_district, countryid, customfieldsid, localeid, parentid, stateid) " +
                    "SELECT tmp.id, tmp.city, tmp.code, tmp.deleted, tmp.email, tmp.fax, tmp.intnumber, tmp.name, tmp.ownersid, tmp.phone, tmp.timeslotid, tmp.zipcode, tmp.city_district, tmp.countryid, tmp.customfieldsid, tmp.localeid, tmp.parentid, tmp.stateid FROM " +
                    "(SELECT * FROM " + templateSchema.getSlashedSchema() + ".location) AS tmp;";
            queries.add(q);

            q = "INSERT INTO " + schema + ".productcategory (id, integrationid, lastchanges, version, active, code, deleted, description, description_localize, imageid, intnumber, lastupdatedate, magentoentityid, magentosyncdate, name, name_localize, sorder, prefix, price, customfields_id, parentid) " +
                    "SELECT tmp.id, tmp.integrationid, tmp.lastchanges, tmp.version, tmp.active, tmp.code, tmp.deleted, tmp.description, tmp.description_localize, tmp.imageid, tmp.intnumber, tmp.lastupdatedate, tmp.magentoentityid, tmp.magentosyncdate, tmp.name, tmp.name_localize, tmp.sorder, tmp.prefix, tmp.price, tmp.customfields_id, tmp.parentid FROM " +
                    "(SELECT * FROM " + templateSchema.getSlashedSchema() + ".productcategory) AS tmp;";
            queries.add(q);

        } else {
            String q = "INSERT INTO " + schema + ".workflowrule " +
                    "(id, deleted, description, executioncriteria, executioncriteriaupdatefield, module, name, pattern, rulecriteria, active, creator, recurrenceid, showinlist) " +
                    "SELECT tmp.id, tmp.deleted, tmp.description, tmp.executioncriteria, tmp.executioncriteriaupdatefield, tmp.module, tmp.name, tmp.pattern, tmp.rulecriteria, tmp.active, tmp.creator, tmp.recurrenceid, tmp.showinlist FROM " +
                    "(SELECT * FROM " + templateSchema.getSlashedSchema() + ".workflowrule) AS tmp;";
            queries.add(q);
            q = "INSERT INTO " + schema + ".workflow_alerts " +
                    "(id, content, deleted, extrakeyvalues, recepient, replyto, subject, tobcc, ccemails, emailtemplateid, workflow, workflowactionstarttimeunit, workflowactionstarttime, workflowactionstarttimegranularity, isworkflowactiontimebased) " +
                    "SELECT tmp.id, tmp.content, tmp.deleted, tmp.extrakeyvalues, tmp.recepient, tmp.replyto, tmp.subject, tmp.tobcc, tmp.ccemails, tmp.emailtemplateid, tmp.workflow, tmp.workflowactionstarttimeunit, tmp.workflowactionstarttime, tmp.workflowactionstarttimegranularity, tmp.isworkflowactiontimebased FROM " +
                    "(SELECT * FROM " + templateSchema.getSlashedSchema() + ".workflow_alerts) AS tmp;";
            queries.add(q);
            q = "INSERT INTO " + schema + ".approvers " +
                    "(id,approvestatusid, approver_order, deleted, entity_id, entitytype, is_default, onapprovedaction, onrejectedaction, rejectstatusid, startstatusid, stepemployeetype, exactapprover, workflow, rejected_workflow, status) " +
                    "SELECT tmp.id,tmp.approvestatusid, tmp.approver_order, tmp.deleted, tmp.entity_id, tmp.entitytype, tmp.is_default, tmp.onapprovedaction, tmp.onrejectedaction, tmp.rejectstatusid, tmp.startstatusid, tmp.stepemployeetype, tmp.exactapprover, tmp.workflow, tmp.rejected_workflow, tmp.status FROM " +
                    "(SELECT * FROM " + templateSchema.getSlashedSchema() + ".approvers) AS tmp;";
            queries.add(q);
            q = "INSERT INTO " + schema + ".approver_roles " +
                    "(approver_id,role_id) " +
                    "SELECT tmp.approver_id,tmp.role_id FROM " +
                    "(SELECT * FROM " + templateSchema.getSlashedSchema() + ".approver_roles) AS tmp;";
            queries.add(q);
            queries.add("INSERT INTO " + schema + ".companycustomfieldssettings (addtab, aliasname, createdby_id, creationdate, issuperuser, modificationdate, clickable, columncode, columnwidth, datatype, disabled, entitycategoryalias, entitycategoryname, entityname, fieldname, isfacetable, isrequired, lookuptype, predefinedvalues, predefinedvalueswithsorting, prefix, query, relationship, showinfiltergrouping, showinlisting, uitype, modifiedby_id, entitytypeid, referenceid, active) " +
                    "SELECT ccfs.addtab, ccfs.aliasname, ccfs.createdby_id, ccfs.creationdate, ccfs.issuperuser, ccfs.modificationdate, ccfs.clickable, ccfs.columncode, ccfs.columnwidth, ccfs.datatype, ccfs.disabled, ccfs.entitycategoryalias, ccfs.entitycategoryname, ccfs.entityname, ccfs.fieldname, ccfs.isfacetable, ccfs.isrequired, ccfs.lookuptype, ccfs.predefinedvalues, ccfs.predefinedvalueswithsorting, ccfs.prefix, ccfs.query, ccfs.relationship, ccfs.showinfiltergrouping, ccfs.showinlisting, ccfs.uitype, ccfs.modifiedby_id, ccfs.entitytypeid, ccfs.referenceid, ccfs.active " +
                    "FROM " + templateSchema.getSlashedSchema() + ".companycustomfieldssettings ccfs;");

        }
        String folderPolicyString = "INSERT INTO " + schema + ".folderpolicy " +
                "(id,description,entrytype,trusteeid,permissionid,relationid) " +
                "SELECT tmp.id,tmp.description,tmp.entrytype,tmp.trusteeid,tmp.permissionid,tmp.relationid FROM " +
                "(SELECT * FROM " + templateSchema.getSlashedSchema() + ".folderpolicy ) AS tmp;";
        queries.add(folderPolicyString);
        String contactPolicyString = "INSERT INTO " + schema + ".contactpolicy " +
                "(id,description,entrytype,trusteeid,permissionid,relationid) " +
                "SELECT tmp.id,tmp.description,tmp.entrytype,tmp.trusteeid,tmp.permissionid,tmp.relationid FROM " +
                "(SELECT * FROM " + templateSchema.getSlashedSchema() + ".contactpolicy ) AS tmp;";
        queries.add(contactPolicyString);

        String paymentMethodString = "INSERT INTO " + schema + ".paymentmethod " +
                "(id, name) " +
                "SELECT tmp.id, tmp.name FROM " +
                "(SELECT * FROM " + templateSchema.getSlashedSchema() + ".paymentmethod) AS tmp;";
        queries.add(paymentMethodString);
        String companyEmailNotification = "INSERT INTO " + schema + ".companyemailnotificationsettings " +
                "(id, category, description, isenabled, notificationname, rolegroupid) " +
                "SELECT cEmailNot.id, cEmailNot.category, cEmailNot.description, cEmailNot.isenabled, cEmailNot.notificationname, cEmailNot.rolegroupid FROM " +
                "(SELECT * FROM " + templateSchema.getSlashedSchema() + ".companyemailnotificationsettings) AS cEmailNot;";
        queries.add(companyEmailNotification);
        String emailTemplateString = "INSERT INTO " + schema + ".emailtemplate " +
                "(id, deleted, isdefault, messagehtml, name, code, locale, subject, categoryid, isCompanyEmailTemplate, fromUserId) " +
                "SELECT emailTemp.id, emailTemp.deleted, emailTemp.isdefault, emailTemp.messagehtml, emailTemp.name, emailTemp.code, emailTemp.locale," +
                "emailTemp.subject, emailTemp.categoryid, emailTemp.isCompanyEmailTemplate, emailTemp.fromUserId FROM " +
                "(SELECT * FROM " + templateSchema.getSlashedSchema() + ".emailtemplate) AS emailTemp;";
        queries.add(emailTemplateString);
        final String payrollCategories = "INSERT INTO " + schema + ".category(id, code, deductfromemployer, isadvancepayment, recurring, name, niable, pensionable, taxable,  type, arabic, forAll, iscashadvance) " +
                "select  cat.id, cat.code, cat.deductfromemployer, cat.isadvancepayment, cat.recurring, cat.name, cat.niable, cat.pensionable, cat.taxable,  cat.type, cat.arabic, cat.forAll, cat.iscashadvance " +
                "from " + templateSchema.getSlashedSchema() + ".category as cat ;";

        queries.add(payrollCategories);

        final String payrollPensionSchemas = "INSERT INTO " + schema + ".pensionscheme( deductfrom, deductiontype, deductionvalue, employerdeductiontype, employerdeductionvalue, name, nonlocaldeductionvalue, employernonlocaldeductionvalue, countrycode) " +
                "select ps.deductfrom, ps.deductiontype, ps.deductionvalue, ps.employerdeductiontype, ps.employerdeductionvalue, ps.name, ps.nonlocaldeductionvalue, ps.employernonlocaldeductionvalue, ps.countrycode " +
                "from " + templateSchema.getSlashedSchema() + ".pensionscheme as ps ;";
        queries.add(payrollPensionSchemas);

        String endOfServiceSettings = "INSERT INTO " + schema + ".eos_settings(id, countrycode, payFrom, fromallallowances) " +
                "select es.id, es.countrycode, es.payFrom, es.fromallallowances " +
                "from " + templateSchema.getSlashedSchema() + ".eos_settings es;";
        queries.add(endOfServiceSettings);

        String endOfServiceRules = "INSERT INTO " + schema + ".eos_rules(id, days, paymentaward, reasoncode, rule, settings_id, rulecode, ruletype) " +
                "select er.id, er.days, er.paymentaward, er.reasoncode, er.rule, er.settings_id, er.rulecode, er.ruletype " +
                "from " + templateSchema.getSlashedSchema() + ".eos_rules er;";
        queries.add(endOfServiceRules);

        String accountNumberSettings = "INSERT INTO " + schema + ".accountNumberSettings(id, accounttype_id, startNumber, endnumber) " +
                " select ans.id, ans.accounttype_id, ans.startnumber, ans.endnumber from " + templateSchema.getSlashedSchema() + ".accountNumberSettings as ans ";
        queries.add(accountNumberSettings);

        queries.add(generateAccountNumberFunction(schema));

        String reportingAddSystemFolder = "insert into " + schema + ".folders(name,type,domainName,companyid,createdate,showhide) " +
                "select 'System','System','#'," + schema.replace("\"", "") + ",now(),true";
        queries.add(reportingAddSystemFolder);

        String createDefaultGenericSettingsKey = "INSERT  INTO " + schema + ".genericsettings (key, value) " +
                "select key, value from " + templateSchema.getSlashedSchema() + ".genericsettings";
        queries.add(createDefaultGenericSettingsKey);

        String createDefaultCertificate = "INSERT  INTO " + schema + ".certificateofemploymenttype (name, defaulthtml, creationDate, creatirid, description, type, deleted) " +
                "select name, defaulthtml, creationDate, creatirid, description, (SELECT id from " + schema + ".reference  where code='CERTIFICATE_TEMPLATE_TYPE' limit 1), deleted from " + templateSchema.getSlashedSchema() + ".certificateofemploymenttype where deleted is not true";
        queries.add(createDefaultCertificate);

        StringBuilder insertPermissionContextData = new StringBuilder();
        insertPermissionContextData.append("insert into ").append(schema).append(".permission_context (permissioncode, contextcode) select permissioncode, contextcode from " + templateSchema.getSlashedSchema() + ".permission_context on conflict do nothing;");
        queries.add(insertPermissionContextData.toString());

        StringBuilder insertPdfReference = new StringBuilder();
        insertPdfReference.append("insert into ").append(schema).append(".pdfreference (id, code, name, deleted) select pr.id, pr.code, pr.name, pr.deleted from " + templateSchema.getSlashedSchema() + ".pdfreference pr;");
        queries.add(insertPdfReference.toString());

        queries.add(new StringBuilder().append("insert into ").append(schema).append(".module_dashboards (id, name, module, is_active, is_default, is_system, creationDate, updatedDate,deleted) select md.id, md.name, md.module, md.is_active, md.is_default, md.is_system, md.creationDate, md.updatedDate,md.deleted from " + templateSchema.getSlashedSchema() + ".module_dashboards md;").toString());

        queries.add(new StringBuilder().append("insert into ").append(schema).append(".default_components (id, componentName, componentCode, modules, report_code) select md.id, md.componentName, md.componentCode, md.modules, md.report_code from " + templateSchema.getSlashedSchema() + ".default_components md;").toString());

        queries.add(new StringBuilder().append("insert into ").append(schema).append(".dashboard_components (width, height, x, y, dashboard_id, component_id) select md.width, md.height, md.x, md.y, md.dashboard_id, md.component_id from " + templateSchema.getSlashedSchema() + ".dashboard_components md;").toString());

        queries.add(new StringBuilder().append("insert into ").append(schema).append(".dashboard_setup_configuration (title, description, state, type, dashboard_id) select sc.title, sc.description, sc.state, sc.type, sc.dashboard_id from " + templateSchema.getSlashedSchema() + ".dashboard_setup_configuration sc;").toString());

        queries.add(new StringBuilder().append("insert into ").append(schema).append(".dashboard_accesses (dashboard_id, role_id) select da.dashboard_id, da.role_id from " + templateSchema.getSlashedSchema() + ".dashboard_accesses da;").toString());

        queries.add(new StringBuilder().append("insert into ").append(schema).append(".mymodule (id, name, code, section, sorder, active) select mm.id, mm.name, mm.code, mm.section, mm.sorder, mm.active from " + templateSchema.getSlashedSchema() + ".mymodule mm;").toString());
        if (activeModules != null) {
            queries.add(new StringBuilder().append("update ").append(schema).append(".mymodule set active = true where code in ('").append(String.join("','", activeModules)).append("');").toString());
            queries.add(new StringBuilder().append("update ").append(schema).append(".mymodule set active = false where code not in ('").append(String.join("','", activeModules)).append("');").toString());
        }

        if (TemplateSchema.GYM.equals(templateSchema)) {
            String organizeModule = "INSERT INTO " + schema + ".property (id,objectName, defaultName, singular, plural, shortcut, moduleCode, isCustom, isActive, convertItems, lnameid, lpluralid, lshortid) " +
                    "SELECT tmp.id, tmp.objectName, tmp.defaultName, tmp.singular, tmp.plural, tmp.shortcut, tmp.moduleCode, tmp.isCustom, tmp.isActive, tmp.convertItems, tmp.lnameid, tmp.lpluralid, tmp.lshortid FROM " +
                    "(SELECT * FROM " + templateSchema.getSlashedSchema() + ".property) AS tmp;";
            queries.add(organizeModule);
        } else {
            String organizeModule = "INSERT INTO " + schema + ".property (id,objectName, defaultName, singular, plural, shortcut, moduleCode, isCustom, isActive, convertItems) " +
                    "SELECT tmp.id, tmp.objectName, tmp.defaultName, tmp.singular, tmp.plural, tmp.shortcut, tmp.moduleCode, tmp.isCustom, tmp.isActive, tmp.convertItems FROM " +
                    "(SELECT * FROM " + templateSchema.getSlashedSchema() + ".property) AS tmp;";
            queries.add(organizeModule);
        }

        String container = "INSERT INTO " + schema + ".container (id,code, defaultName, moduleCode, preparedView, sorder, changed) " +
                "SELECT con.id, con.code, con.defaultName, con.moduleCode, con.preparedView, con.sorder, con.changed FROM " +
                "(SELECT * FROM " + templateSchema.getSlashedSchema() + ".container) AS con;";
        queries.add(container);

        String containerItem = "INSERT INTO " + schema + ".container_item (id, sorder, moduleCode, isActive, moduleid, propertyid, containerid ) " +
                "SELECT coni.id, coni.sorder, coni.moduleCode, coni.isActive, coni.moduleid, coni.propertyid, coni.containerId FROM " +
                "(SELECT * FROM " + templateSchema.getSlashedSchema() + ".container_item) AS coni;";
        queries.add(containerItem);
        String formProperty = "INSERT INTO " + schema + ".form_property (form_id, settingsjsondata) " +
                "SELECT fp.form_id, fp.settingsjsondata FROM " +
                "(SELECT * FROM " + templateSchema.getSlashedSchema() + ".form_property) AS fp;";
        queries.add(formProperty);

        String itemTableSettings = "INSERT INTO " + schema + ".itemtable_settings (section, settingsJSONData) " +
                "SELECT fp.section, fp.settingsJSONData FROM " +
                "(SELECT * FROM " + templateSchema.getSlashedSchema() + ".itemtable_settings) AS fp;";
        queries.add(itemTableSettings);

        String addColumnToPdfTemplate = "ALTER TABLE " + schema + ".pdftemplate ADD COLUMN templatecode varchar(255);";
        queries.add(addColumnToPdfTemplate);

        String obyektivkaPdfTemplateRu = "INSERT INTO " + schema + ".pdftemplate (content, typeid, templatecode) " +
                "SELECT fp.content, fp.typeid, fp.templatecode FROM " +
                "(SELECT * FROM " + templateSchema.getSlashedSchema() + ".pdftemplate WHERE templatecode = 'OBYEKTIVKA_TEMPLATE_RU') AS fp;";
        queries.add(obyektivkaPdfTemplateRu);

        String obyektivkaPdfTemplateUz = "INSERT INTO " + schema + ".pdftemplate (content, typeid, templatecode) " +
                "SELECT fp.content, fp.typeid, fp.templatecode FROM " +
                "(SELECT * FROM " + templateSchema.getSlashedSchema() + ".pdftemplate WHERE templatecode = 'OBYEKTIVKA_TEMPLATE_UZ') AS fp;";
        queries.add(obyektivkaPdfTemplateUz);

        String obyektivkaCompanyPdfTemplateRu = "INSERT INTO " + schema + ".companypdftemplate (fontfamily, name, templateid) " +
                "SELECT fp.fontfamily, fp.name, (SELECT id FROM " + schema + ".pdftemplate where templatecode = 'OBYEKTIVKA_TEMPLATE_RU' limit 1) FROM " +
                "(SELECT * from " + templateSchema.getSlashedSchema() + ".companypdftemplate WHERE name = 'Объективка') AS fp;";
        queries.add(obyektivkaCompanyPdfTemplateRu);

        String obyektivkaCompanyPdfTemplateUz = "INSERT INTO " + schema + ".companypdftemplate (fontfamily, name, templateid) " +
                "SELECT fp.fontfamily, fp.name, (SELECT id FROM " + schema + ".pdftemplate where templatecode = 'OBYEKTIVKA_TEMPLATE_UZ' limit 1) FROM " +
                "(SELECT * from " + templateSchema.getSlashedSchema() + ".companypdftemplate WHERE name = 'Obyektiv') AS fp;";
        queries.add(obyektivkaCompanyPdfTemplateUz);

        String schemaVersion = "create table " + schema + ".schema_version AS select * from " + templateSchema.getSlashedSchema() + ".schema_version;";
        queries.add(schemaVersion);

        queries.add(new StringBuilder().append("insert into ").append(schema).append(".kanbanitemsettings (id, code, name, settingsjsondata) select kis.id, kis.code, kis.name, kis.settingsjsondata from " + templateSchema.getSlashedSchema() + ".kanbanitemsettings kis;").toString());
        queries.add(new StringBuilder().append("insert into ").append(schema).append(".quick_add_settings (form, settingsjsondata) select qas.form, qas.settingsjsondata from " + templateSchema.getSlashedSchema() + ".quick_add_settings qas;").toString());


        return queries.toArray(new String[]{});
    }

    private String generateAccountNumberFunction(String schema) {
        return "CREATE OR REPLACE FUNCTION " + schema + ".generateAccountNumber(startNumberRange integer, endNumberRange integer) " +
                "   RETURNS text AS " +
                "$BODY$ " +
                "   DECLARE accountNumber text; i integer; " +
                "   BEGIN " +
                "       FOR i in startNumberRange .. endNumberRange LOOP " +
                "           accountNumber = ''||i; " +
                "           IF NOT EXISTS (SELECT * FROM  " + schema + ".account WHERE accountcode = accountNumber) THEN " +
                "               return accountNumber; " +
                "           END IF; " +
                "       END LOOP; " +
                "       return null; " +
                "   END " +
                "$BODY$ " +
                "LANGUAGE plpgsql VOLATILE; ";
    }


    public String[] updateSequences(Map<String, Integer> sequences, String schema) {
        ArrayList<String> updates = new ArrayList<>();
        String trusteeSequenceUpd = "update updater set id = 1 where (SELECT setval('" + schema + ".trustee_id_seq', (SELECT max(id) FROM " + schema + ".trustee))) is not null;";//"ALTER SEQUENCE " + schema + ".trustee_id_seq RESTART " + sequences.get("trustee");
        updates.add(trusteeSequenceUpd);
        String trusteeGroupSequenceUpd = "update updater set id = 1 where (SELECT setval('" + schema + ".trusteegroup_id_seq', (SELECT max(id) FROM " + schema + ".trusteegroup))) is not null;";//"ALTER SEQUENCE " + schema + ".trusteegroup_id_seq RESTART " + sequences.get("trusteegroup");
        updates.add(trusteeGroupSequenceUpd);
        String relationshipSequenceUpd = "update updater set id = 1 where (SELECT setval('" + schema + ".relationship_id_seq', (SELECT max(id) FROM " + schema + ".relationship))) is not null;";//"ALTER SEQUENCE " + schema + ".relationship_id_seq RESTART " + sequences.get("relationship");
        updates.add(relationshipSequenceUpd);
        String taskpermissionSequenceUpd = "update updater set id = 1 where (SELECT setval('" + schema + ".taskpermission_id_seq', (SELECT max(id) FROM " + schema + ".taskpermission))) is not null;";//"ALTER SEQUENCE " + schema + ".taskpermission_id_seq RESTART " + sequences.get("taskpermission");
        updates.add(taskpermissionSequenceUpd);
        String taskpolicySequenceUpd = "update updater set id = 1 where (SELECT setval('" + schema + ".taskpolicy_id_seq', (SELECT max(id) FROM " + schema + ".taskpolicy))) is not null;";//"ALTER SEQUENCE " + schema + ".taskpolicy_id_seq RESTART " + sequences.get("taskpolicy");
        updates.add(taskpolicySequenceUpd);
        String folderpermissionSequenceUpd = "update updater set id = 1 where (SELECT setval('" + schema + ".folderpermission_id_seq', (SELECT max(id) FROM " + schema + ".folderpermission))) is not null;";//"ALTER SEQUENCE " + schema + ".folderpermission_id_seq RESTART " + sequences.get("folderpermission");
        updates.add(folderpermissionSequenceUpd);
        String folderpolicySequenceUpd = "update updater set id = 1 where (SELECT setval('" + schema + ".folderpolicy_id_seq', (SELECT max(id) FROM " + schema + ".folderpolicy))) is not null;";//"ALTER SEQUENCE " + schema + ".folderpolicy_id_seq RESTART " + sequences.get("folderpolicy");
        updates.add(folderpolicySequenceUpd);
        String contactpermissionSequenceUpd = "update updater set id = 1 where (SELECT setval('" + schema + ".contactpermission_id_seq', (SELECT max(id) FROM " + schema + ".contactpermission))) is not null;";//"ALTER SEQUENCE " + schema + ".contactpermission_id_seq RESTART " + sequences.get("contactpermission");
        updates.add(contactpermissionSequenceUpd);
        String contactpolicySequenceUpd = "update updater set id = 1 where (SELECT setval('" + schema + ".contactpolicy_id_seq', (SELECT max(id) FROM " + schema + ".contactpolicy))) is not null;";//"ALTER SEQUENCE " + schema + ".contactpolicy_id_seq RESTART " + sequences.get("contactpolicy");
        updates.add(contactpolicySequenceUpd);
        String paymentmethodSequeneUpd = "update updater set id = 1 where (SELECT setval('" + schema + ".paymentmethod_id_seq', (SELECT max(id) FROM " + schema + ".paymentmethod))) is not null;";//"ALTER SEQUENCE " + schema + ".paymentmethod_id_seq RESTART " + sequences.get("paymentmethod");
        updates.add(paymentmethodSequeneUpd);
        String companyEmailNotificationSequenceUpd = "update updater set id = 1 where (SELECT setval('" + schema + ".companyemailnotificationsettings_id_seq', (SELECT max(id) FROM " + schema + ".companyemailnotificationsettings))) is not null;";//"ALTER SEQUENCE " + schema + ".companyemailnotificationsettings_id_seq RESTART " + sequences.get("companyemailnotificationsettings");
        updates.add(companyEmailNotificationSequenceUpd);
        String categorySequenceUpd = "update updater set id = 1 where (SELECT setval('" + schema + ".category_id_seq', (SELECT max(id) FROM " + schema + ".category))) is not null;";//"ALTER SEQUENCE " + schema + ".category_id_seq RESTART " + sequences.get("category");
        updates.add(categorySequenceUpd);
        String roleSequenceUpd = "update updater set id = 1 where (SELECT setval('" + schema + ".role_id_seq', (SELECT max(id) FROM " + schema + ".role))) is not null;";//"ALTER SEQUENCE " + schema + ".role_id_seq RESTART " + sequences.get("role");
        updates.add(roleSequenceUpd);
        String rolePermissionSequenceUpd = "update updater set id = 1 where (SELECT setval('" + schema + ".rolepermission_id_seq', (SELECT max(id) FROM " + schema + ".rolepermission))) is not null;";
        updates.add(rolePermissionSequenceUpd);
        String approversSequenceUpd = "update updater set id = 1 where (SELECT setval('" + schema + ".approvers_id_seq', (SELECT max(id) FROM " + schema + ".approvers))) is not null;";//"ALTER SEQUENCE " + schema + ".approvers_id_seq RESTART " + sequences.get("approvers");
        updates.add(approversSequenceUpd);
        String dashboardSequenceUpd = "update updater set id = 1 where (SELECT setval('" + schema + ".module_dashboards_id_seq', (SELECT max(id) FROM " + schema + ".module_dashboards))) is not null;";//"ALTER SEQUENCE " + schema + ".module_dashboards_id_seq RESTART " + sequences.get("module_dashboards");
        updates.add(dashboardSequenceUpd);
        String dashboardComponentSequenceUpd = "update updater set id = 1 where (SELECT setval('" + schema + ".default_components_id_seq', (SELECT max(id) FROM " + schema + ".default_components))) is not null;";//"ALTER SEQUENCE " + schema + ".default_components_id_seq RESTART " + sequences.get("default_components");
        updates.add(dashboardComponentSequenceUpd);
        String referenceColorSequenceUpd = "update updater set id = 1 where (SELECT setval('" + schema + ".referencecolor_id_seq', (SELECT max(id) FROM " + schema + ".referencecolor))) is not null;";//"ALTER SEQUENCE " + schema + ".referencecolor_id_seq RESTART " + sequences.get("referencecolor");
        updates.add(referenceColorSequenceUpd);
        String referenceSequenceUpd = "update updater set id = 1 where (SELECT setval('" + schema + ".reference_id_seq', (SELECT max(id) FROM " + schema + ".reference))) is not null;";//"ALTER SEQUENCE " + schema + ".reference_id_seq RESTART " + sequences.get("reference");
        updates.add(referenceSequenceUpd);
        String referenceLocaleSequenceUpd = "update updater set id = 1 where (SELECT setval('" + schema + ".reference_locale_id_seq', (SELECT max(id) FROM " + schema + ".reference_locale))) is not null;";
        updates.add(referenceLocaleSequenceUpd);
        String emailTemplateSequenceUpd = "update updater set id = 1 where (SELECT setval('" + schema + ".emailtemplate_id_seq', (SELECT max(id) FROM " + schema + ".emailtemplate))) is not null;";
        updates.add(emailTemplateSequenceUpd);
        String myModuleSequenceUpd = "update updater set id = 1 where (SELECT setval('" + schema + ".mymodule_id_seq', (SELECT max(id) FROM " + schema + ".mymodule))) is not null;";
        updates.add(myModuleSequenceUpd);
        String containerSequenceUpd = "update updater set id = 1 where (SELECT setval('" + schema + ".container_id_seq', (SELECT max(id) FROM " + schema + ".container))) is not null;";
        updates.add(containerSequenceUpd);
        String containerItemSequenceUpd = "update updater set id = 1 where (SELECT setval('" + schema + ".container_item_id_seq', (SELECT max(id) FROM " + schema + ".container_item))) is not null;";
        updates.add(containerItemSequenceUpd);
        return updates.toArray(new String[]{});
    }

    private Map<String, Integer> retrieveSequenceInfo(String schema) {
        Map<String, Integer> sequenceMap = new HashMap<>();
        sequenceMap.put("trustee", 16);
        sequenceMap.put("trusteegroup", 16);
        sequenceMap.put("relationship", 17);
        sequenceMap.put("role", 21);
        sequenceMap.put("taskpermission", 6);
        sequenceMap.put("taskpolicy", 6);
        sequenceMap.put("folderpermission", 6);
        sequenceMap.put("folderpolicy", 6);
        sequenceMap.put("contactpermission", 4);
        sequenceMap.put("contactpolicy", 4);
        sequenceMap.put("paymentmethod", 6);
        sequenceMap.put("companyemailnotificationsettings", 178);
        sequenceMap.put("category", 58);

        return sequenceMap;
    }

    @Transactional
    public void saveCompanySystemSettings(NewCompany company, EdsCompany comp) {
        EdsCompanySystemSettings companySystemSettings = new EdsCompanySystemSettings();
        companySystemSettings.setCompany(comp);
        companySystemSettings.setCompanySignedUpFrom(company.getCompanySignedUpFrom());
        companySystemSettings.setAdminEmail(company.getAdminEmail());
        companySystemSettings.setHost(company.getHost());
        companySystemSettings.setParameter1(company.getValue1());
        companySystemSettings.setParameter2(company.getUtm_campaign() != null ? company.getUtm_campaign() : company.getValue2());
        companySystemSettings.setParameter3(company.getUtm_source() != null ? company.getUtm_source() : company.getValue3());
        companySystemSettings.setMedium(company.getUtm_medium());
        companySystemSettings.setRedirected(company.getRedirected());
        companySystemSettings.setReferrer(company.getReferrer());
        companySystemSettings.setGclid(company.getGclid());
        companySystemSettings.setParentIframeUrl(company.getParentIframeUrl());

        if (company.getGoogleAppsDomain() != null) { //for now, the user is comming from marketplace
            companySystemSettings.setShowPopups(true);
            companySystemSettings.setGoogleAppDomain(company.getGoogleAppsDomain());
            companySystemSettings.setGoogleAppSection(company.getGoogleAppsSection());
            companySystemSettings.setMarketplaceOauth2Enabled(true);
        }
        companySystemSettingsManager.create(companySystemSettings);
    }

    @Transactional
    public EdsTimeSlot saveDefaultTimeSlot(EdsCompany company, Locale locale) {
        int start = 570;
        int end = 1080;
        EdsTimeSlot timeSlot = new EdsTimeSlot();
        String defaultTimeSlotName = messageSource.getMessage("createSignUpCompany.defaultTimeSlotName", null, "Default timeslot", locale);
        timeSlot.setName(defaultTimeSlotName);
        String defaultTimeSlotDescription = messageSource.getMessage("createSignUpCompany.defaultTimeSlotDescription", null, "This is the Default timeslot", locale);
        timeSlot.setDescription(defaultTimeSlotDescription);
        timeSlotManager.create(timeSlot);

        for (int i = 1; i <= 7; i++) {
            EdsTimeSlotItem timeSlotItem = new EdsTimeSlotItem();
            if (i == 1 || i == 7) {
                timeSlotItem.setStartTime(0);
                timeSlotItem.setEndTime(0);
                timeSlotItem.setLunchStart(0);
                timeSlotItem.setLunchEnd(0);
                timeSlotItem.setCoffeeStart(0);
                timeSlotItem.setCoffeeEnd(0);
            } else {
                timeSlotItem.setStartTime(start);
                timeSlotItem.setEndTime(end);

                timeSlotItem.setLunchStart(0);
                timeSlotItem.setLunchEnd(0);
                timeSlotItem.setCoffeeStart(0);
                timeSlotItem.setCoffeeEnd(0);
            }
            timeSlotItem.setDay(i - 1);
            timeSlotItem.setTimeSlot(timeSlot);
            timeSlotItemManager.create(timeSlotItem);
        }
        return timeSlot;
    }

    @Transactional
    public void createFreeTrialUsagePlan(Integer companyId, boolean isCurrencyGBP, int usersCount, String hostName, String pricingPackageNAME) {
        float userRate = commonServiceLocal.getUserRatePerHOST(/*usersCount, */hostName, pricingPackageNAME).floatValue();
        ServerSecurityContext.getInstance().setCompanyId(companyId);
        UsagePlanItem fUsagePlanItem = new UsagePlanItem();
        fUsagePlanItem.setHostName(hostName);
        fUsagePlanItem.setDiscount(0);
        fUsagePlanItem.setPlanType(EdsUsagePlan.FREE_TRIAL);
        fUsagePlanItem.setStorageCount(10);
        fUsagePlanItem.setTotalAmount(0);
        fUsagePlanItem.setUserCount(usersCount);
        fUsagePlanItem.setService(ALL_SERVICES);
        fUsagePlanItem.setStatus(EdsUsagePlan.ACTIVE);
        fUsagePlanItem.setCompanyID(companyId);
        fUsagePlanItem.setCurrencyGBP(isCurrencyGBP);
        fUsagePlanItem.setMobile(false);
        fUsagePlanItem.setUserRate(userRate);
        fUsagePlanItem.setSupportPackagePrice(0);
        fUsagePlanItem.setAccountsModule(true);
        fUsagePlanItem.setSalesModule(true);
        fUsagePlanItem.setHumansModule(true);
        fUsagePlanItem.setPayrollModule(true);
        fUsagePlanItem.setProjectModule(true);
        commonServiceLocal.usagePlanSaveAndGetId(fUsagePlanItem);
    }

    @Override
    public void createOneYearUsagePlan(Integer companyId, String hostName) {
        float userRate = commonServiceLocal.getUserRatePerHOST(hostName, "").floatValue();
        ServerSecurityContext.getInstance().setCompanyId(companyId);
        UsagePlanItem planItem = new UsagePlanItem();
        planItem.setHostName(hostName);
        planItem.setDiscount(0);
        planItem.setPlanType(EdsUsagePlan.FREE_TRIAL);
        planItem.setStorageCount(10);
        planItem.setTotalAmount(0);
        planItem.setUserCount(10);
        planItem.setService(ALL_SERVICES);
        planItem.setStatus(EdsUsagePlan.ACTIVE);
        planItem.setCompanyID(companyId);
        planItem.setCurrencyGBP(false);
        planItem.setMobile(false);
        planItem.setUserRate(userRate);
        planItem.setSupportPackagePrice(0);
        planItem.setAccountsModule(true);
        planItem.setSalesModule(true);
        planItem.setHumansModule(true);
        planItem.setPayrollModule(true);
        planItem.setProjectModule(true);
        UsagePlanItem saved = commonServiceLocal.usagePlanSaveAndGetId(planItem);

        // End dateni 1 yilga set qilamiz
        Calendar cal = new GregorianCalendar();
        cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);
        saved.setExpireDate(cal.getTime());
        myAccountServiceLocal.updateCompanyLastUsagePlan(saved);
    }

    @Transactional
    public void createFreeTrialUsagePlanForMobile(Integer companyId, boolean isCurrencyGBP, String hostName) {
        int userCount = 1;
        float userRate = commonServiceLocal.getUserRatePerHOST(/*userCount, */hostName, "").floatValue();
        UsagePlanItem fUsagePlanItem = new UsagePlanItem();
        fUsagePlanItem.setDiscount(0);
        fUsagePlanItem.setPlanType(EdsUsagePlan.FREE_TRIAL);
        fUsagePlanItem.setStorageCount(10);
        fUsagePlanItem.setTotalAmount(0);
        fUsagePlanItem.setUserCount(userCount);
        fUsagePlanItem.setService(MOBILE_SERVICE);
        fUsagePlanItem.setStatus(EdsUsagePlan.ACTIVE);
        fUsagePlanItem.setCompanyID(companyId);
        fUsagePlanItem.setMobile(true);
        fUsagePlanItem.setCurrencyGBP(isCurrencyGBP);
        fUsagePlanItem.setProjectCount(5);
        fUsagePlanItem.setTaskCount(50);
        fUsagePlanItem.setUserRate(userRate);
        fUsagePlanItem.setSupportPackagePrice(0);
        commonServiceLocal.usagePlanSaveAndGetId(fUsagePlanItem);
    }

    @Transactional
    public DomainInfo findByGoogleAppDomain(String domainName, String googleAppDomain, String email) {
        return globalAuthJdbcSpringManager.findByGoogleAppDomain(domainName, googleAppDomain, email.toLowerCase());
    }

    @Transactional
    public void getParamsFromMarketPlace(StringBuffer stringBuffer, String param) {
        try {
            messageManager.sendParamsForGMPLogin(stringBuffer, param);
        } catch (EdsDbException e) {
            e.printStackTrace();
        }
    }

    /**
     * @return
     */
    @Transactional(propagation = Propagation.NEVER)
    public String preProcessCompanyData(Integer scheme, HashSet<String> activeModules) {
        ServerSecurityContext.getInstance().setDatabase(DATABASE_FREE);
        EdsCompany edsCompany = companyManager.get(scheme);
        String orgType = edsCompany.getOrgType();
        String schemaName;
        if (scheme == null) {
            Integer expectedID = companyManager.getMaxSchemaId() + 1;//(Integer) findSingle("select max(id) from EdsCompany") + 1;
            if (expectedID <= 23000) {
                expectedID = 23000 + 1;
            }
            schemaName = expectedID.toString();
        } else {
            schemaName = String.valueOf(scheme);
        }
        String slashedName = "\"" + schemaName + "\"";
        String dropSchema = "DROP SCHEMA " + slashedName + " CASCADE";
        String dbUserName = EdsSchemaUpdater.getDBUserName();
        jdbcTemplate.execute("CREATE SCHEMA " + slashedName + " AUTHORIZATION " + dbUserName + ";");

        try {
            EdsSchemaUpdater.setDbUrl(null);
            EdsSchemaUpdater.updateSchema(schemaName);
        } catch (Throwable t) {
            jdbcTemplate.update(dropSchema);
            throw new RuntimeException("Error During Schema Export with schema name - " + schemaName, t);
        }

        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
//            jdbcTemplate.batchUpdate(initDefaultDataWithScript(slashedName, activeModules));
            String[] queries = initDefaultDataWithScript(slashedName, activeModules, TemplateSchema.of(orgType));
            EdsSchemaUpdater.executeSqlStatements(Arrays.stream(queries).toList());
            EdsSchemaUpdater.manualUpdateWhatWasNotUpdatedByCallingMethod(slashedName);
            registerToGlobalAuth(schemaName);
            transactionManager.commit(status);

        } catch (DataAccessException e) {
            transactionManager.rollback(status);
            jdbcTemplate.update(dropSchema);
            throw e;
        } catch (Exception e) {
            transactionManager.rollback(status);
            jdbcTemplate.update(dropSchema);
            throw new RuntimeException(e);
        }
        TransactionDefinition def2 = new DefaultTransactionDefinition();
        TransactionStatus status2 = transactionManager.getTransaction(def2);
        try {
            jdbcTemplate.batchUpdate(updateSequences(retrieveSequenceInfo(slashedName), slashedName));
            transactionManager.commit(status2);

            importReportingData(schemaName);
        } catch (DataAccessException e) {
            transactionManager.rollback(status2);
            jdbcTemplate.update(dropSchema);
            throw e;
        }

        return schemaName;
    }

    private void importReportingData(String schemaName) {
        String currentDBUrl = ServerSecurityContext.getInstance().getDatabase();
        String currentCompanyID = ServerSecurityContext.getInstance().getCompanyId();
        TransactionDefinition def = new DefaultTransactionDefinition();
        TransactionStatus status = transactionManager.getTransaction(def);
        try {
            try {
                ServerSecurityContext.getInstance().setDatabase(DATABASE_FREE);
                ListingFilterParameter listingFilterParameter = new ListingFilterParameter();
                listingFilterParameter.setCompanyID(0);
                listingFilterParameter.setCompaines(new Integer[]{Integer.valueOf(schemaName)});
                ServerSecurityContext.getInstance().setCompanyId(0);
                List<EdsReport> edsReports = reportingManager.getReportListByCompany(0);
                Integer[] objectIDs = new Integer[edsReports.size()];
                int i = -1;
                for (EdsReport report : edsReports) {
                    objectIDs[++i] = report.getObjectID();
                }
                listingFilterParameter.setCategories(objectIDs);
                transactionManager.commit(status);
                backendService.exportSavedReports(listingFilterParameter);
                ServerSecurityContext.getInstance().setDatabase(currentDBUrl);
                ServerSecurityContext.getInstance().setCompanyId(currentCompanyID);
            } catch (Exception e) {
                transactionManager.rollback(status);
                ServerSecurityContext.getInstance().setDatabase(currentDBUrl);
                ServerSecurityContext.getInstance().setCompanyId(currentCompanyID);
            }
        } catch (Exception e) {
            ServerSecurityContext.getInstance().setDatabase(currentDBUrl);
            ServerSecurityContext.getInstance().setCompanyId(currentCompanyID);
        }
    }

    private void registerToGlobalAuth(String schemaName) {
//        String clusterType = SpringPropertiesUtil.getProperty("cluster.type");
        globalAuthManager.executeQuery(rs -> {
            while (rs.next()) {
            }
            return "";
        }, "SELECT insertClusterCompany(?,?);", DATABASE_FREE, Integer.valueOf(schemaName));
    }

    @Transactional
    public void sendCompanyRegistrationNotification(NewCompany company) throws EdsDbException {
        String adminEmail = company.getAdminEmail();
        String adminName = company.getAdminFName();
        String companyName = company.getName();
        String locale = company.getLocale();
        String activationLink = company.getActivationKey();
        boolean includeActivationLink = true;
        boolean existingUser = company.isExistingUser();

        signupMessageManager.sendCompanyRegistrationNotificationToUser(
                adminEmail,
                adminName,
                companyName,
                locale,
                activationLink,
                includeActivationLink,
                existingUser);
    }

    @Override
    public boolean validateForSecurity(NewCompany company) {
        String ipAddress = StringUtils.isNotBlank(company.getClientSingUpIPAddress()) ? company.getClientSingUpIPAddress().trim().split("[,]")[0].trim() : null;
        if (StringUtils.isBlank(ipAddress)) {
            return true;
        }

        EdsAllowedIPAddress allowedIP = allowedIPAddressManager.findByIpAddress(ipAddress);
        if (allowedIP != null) {
            return true;
        }

        String domainName = company.getAdminEmail().split("@")[1];
        if (StringUtils.isBlank(domainName)) {
            return false;
        }

        domainName = domainName.trim().toLowerCase();
        if (spamDomainManager.findByHost(domainName) != null) {
            return false;
        } else if (domainName.split("[.]").length > 2) {
            String mainDomain = domainName.substring(domainName.indexOf(".") + 1);
            if (spamDomainManager.findByHost(mainDomain) != null) {
                return false;
            }
        }

        SignUpIPAddress attackedIP = null;
        Long remainingLifeTime = null;
        if (StringUtils.isNotBlank(RedisClient.getKey(ipAddress))) {
            remainingLifeTime = RedisClient.getTtl(ipAddress);
            attackedIP = RedisClient.getKey(ipAddress, SignUpIPAddress.class);
            attackedIP.setAttemts(attackedIP.getAttemts() + 1);

            if (attackedIP.getAttemts() > SignUpIPAddress.MAX_ATTEMPTS) {
                return false;
            }
        }
        if (StringUtils.isNotBlank(ipAddress)) {
            attackedIP = attackedIP != null ? attackedIP : new SignUpIPAddress(ipAddress, 1);
            RedisClient.setKey(ipAddress, attackedIP, SignUpIPAddress.class, remainingLifeTime != null ? remainingLifeTime.intValue() : SignUpIPAddress.IP_LIFE_TIME);
        }
        return true;
    }
}
