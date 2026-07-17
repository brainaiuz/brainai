package com.edatasite.workforce.gwt.core.server.app;

import com.edatasite.shared.db.EdsDbException;
import com.edatasite.workforce.core.domain.EdsAddress;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsCurrency;
import com.edatasite.workforce.core.domain.EdsInvoicingSettings;
import com.edatasite.workforce.core.domain.EdsLocale;
import com.edatasite.workforce.core.domain.EdsRegion;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsUserSession;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;
import com.edatasite.workforce.core.domain.payrolluk.EdsCompanyPayrollSettings;
import com.edatasite.workforce.core.domain.settings.EdsGenericSettings;
import com.edatasite.workforce.core.solr.component.CrmAccountSolrComponent;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxComponentData;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxData;
import com.edatasite.workforce.gwt.accounting.server.app.AccountingServiceLocal;
import com.edatasite.workforce.gwt.backend.server.app.BackendServiceLocal;
import com.edatasite.workforce.gwt.client.client.rpc.ClientService;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.enums.TaxKeyEnum;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyData;
import com.edatasite.workforce.gwt.core.client.rpc.KeyValueStruct;
import com.edatasite.workforce.gwt.core.client.rpc.TaxItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.controllers.dto.AccountingSetupItem;
import com.edatasite.workforce.gwt.core.server.db.AddressManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.CountryManager;
import com.edatasite.workforce.gwt.core.server.db.CrmAccountManager;
import com.edatasite.workforce.gwt.core.server.db.CurrencyManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.InvoicingSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.LocaleManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.ModuleManager;
import com.edatasite.workforce.gwt.core.server.db.PropertManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RegionManager;
import com.edatasite.workforce.gwt.core.server.db.TimeZoneManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.db.UserSessionManager;
import com.edatasite.workforce.gwt.core.server.db.payroll.CompanyPayrollSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.SolrManager;
import com.edatasite.workforce.gwt.core.server.db.settings.GenericSettingsManager;
import com.edatasite.workforce.gwt.core.server.rabbitmq.service.RabbitMQService;
import com.edatasite.workforce.gwt.core.server.rpc.AuthDetails;
import com.edatasite.workforce.gwt.core.server.rpc.AuthInfoItem;
import com.edatasite.workforce.gwt.core.server.rpc.UserSignUPSessionID;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.zatca.ZatcaService;
import com.edatasite.workforce.gwt.core.server.zatca.service.errors.ZatcaException;
import com.edatasite.workforce.gwt.invoice.server.app.BaseInvoiceService;
import com.edatasite.workforce.gwt.modulesettings.client.ModuleService;
import com.edatasite.workforce.gwt.profile.server.app.ProfileServiceLocal;
import com.edatasite.workforce.gwt.signup.client.rpc.CreatedCompany;
import com.edatasite.workforce.gwt.signup.client.rpc.NewCompany;
import com.edatasite.workforce.gwt.signup.server.app.SignUpServiceLocal;
import com.edatasite.workforce.rest.v3.release10.auth.dto.AccountingInitTO;
import com.edatasite.workforce.rest.v3.release10.auth.dto.CompanyInitTO;
import com.edatasite.workforce.utils.EdsContextParams;
import com.edatasite.workforce.utils.redis.RedisClient;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

@Transactional
@Service("companyService")
public class CompanyServiceImpl implements CompanyService, PermissionConstants {

    private static final Logger log = LoggerFactory.getLogger(CompanyServiceImpl.class);

    @Autowired
    private UserManager userManager;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private ModuleManager moduleManager;
    @Autowired
    private PropertManager propertyManager;
    @Autowired
    private SolrManager solrManager;
    @Autowired
    private CurrencyManager currencyManager;
    @Autowired
    private UserSessionManager userSessionManager;
    @Autowired
    private LocaleManager localeManager;
    @Autowired
    private CrmAccountManager crmAccountManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private BackendServiceLocal backendServiceLocal;
    @Autowired
    private SignUpServiceLocal signupServiceLocal;
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private AccountingServiceLocal accountingServiceLocal;
    @Autowired
    private ProfileServiceLocal profileServiceLocal;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private RabbitMQService rabbitMQService;
    @Autowired
    private TimeZoneManager zoneManager;
    @Autowired
    private AddressManager addressManager;
    @Autowired
    private RegionManager regionManager;
    @Autowired
    private CountryManager countryManager;
    @Autowired
    private CommonService commonService;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private BaseInvoiceService baseInvoiceService;
    @Autowired
    private CompanyPayrollSettingsManager companyPayrollSettingsManager;
    @Autowired
    private InvoicingSettingsManager invoicingSettingsManager;
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    private GenericSettingsManager genericSettingsManager;
    @Autowired
    private ZatcaService zatcaService;
    @Autowired
    private CrmAccountSolrComponent crmAccountSolrComponent;
    @Autowired
    private LoginServiceLocal loginServiceLocal;
    @Autowired
    private ClientService clientService;

    @Transactional(propagation = Propagation.NEVER)
    public String resetCompany() {
        NewCompany newCompany = new NewCompany();

        EdsUser user = userManager.getUser();
        EdsCompany company = user.getCompany();

        if (!company.getTestCompany()) {
            return null;
        }

        newCompany.setActive(company.getActive());
        newCompany.setCompanyId(company.getObjectID());
        newCompany.setSetUp(company.getSetUp());
        newCompany.setName(company.getName());
        newCompany.setAdminFName(user.getFirstName());
        newCompany.setAdminLName(user.getLastName());
        newCompany.setAdminEmail(user.getEmail());
        newCompany.setPhone(company.getPhone());
        newCompany.setHost(EdsContextParams.getHost());
        newCompany.setServiceId(globalAuthJdbcSpringManager.getCompanyServiceId(company.getObjectID()));
        newCompany.setLocale(company.getLocale() != null ? company.getLocale() : Locale.ENGLISH.getLanguage());
        newCompany.setCurrencyID(company.getCurrency() != null ? company.getCurrency().getObjectID() : null);
        newCompany.setRedirectToSettings(true);
        newCompany.setCountryID(company.getCountry() != null ? company.getCountry().getObjectID() : null);
        newCompany.setReset(true);

        //---------------------------------------------------------------------------------------

        //Set Company Industry
//        EdsReference industry = company.getWorkArea();
        KeyValueStruct industry = new KeyValueStruct();
        EdsCompanyPayrollSettings cps = companyPayrollSettingsManager.getCompanySettingValue(Constants.INDUSTRY_ID);
        if (cps != null) {
            industry.setKey(cps.getKey());
            industry.setValue(cps.getValue());
        }
        //Set Accounting Tool they used so far
        String accountingTool = company.getAccountingTool();
        //Set Billing && Mailing Addresses
        Address billingAddress = null;
        if (company.getBillingAddresses() != null && !company.getBillingAddresses().isEmpty()) {
            billingAddress = company.getBillingAddresses() != null ? company.getBillingAddresses().get(0).getRPC() : null;
            billingAddress.setPrimary(true);
        }


        //Invoice Settings Manager
        EdsInvoicingSettings invoiceSettings = invoicingSettingsManager.getInvoiceSettings(company);
        String companyStripeAccount = "";
        String companyPaypalAccount = "";

        if (invoiceSettings != null) {
            if (StringUtils.isNotBlank(invoiceSettings.getStripeUserId())) {
                companyStripeAccount = invoiceSettings.getStripeUserId();
            }
            if (StringUtils.isNotBlank(invoiceSettings.getPayPalAccount())) {
                companyPaypalAccount = invoiceSettings.getPayPalAccount();
            }
        }

        //Save Taxes
        TaxItem[] taxItems = accountingServiceLocal.getCompanyTaxesWithFilter(null);
        ArrayList<TaxData> taxDatas = new ArrayList<>();
        if (taxItems != null) {
            for (int i = 0; i < taxItems.length; i++) {
                TaxData t = baseInvoiceService.getTax(taxItems[i].getId());
                if (t != null) {
                    t.setObjectId(null);
                    taxDatas.add(t);
                }
            }
        }
        boolean accountingSetup = company.getAccountingSetup();

        //Set Conversion Date (Accounts start date)
        EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
        if (financialSettings == null) {
            financialSettings = new EdsFinancialSettings();
        }
        AccountingSetupItem setupItem = new AccountingSetupItem()
                .setConversionDate(financialSettings.getConversionDate())
                .setTaxIdNumber(financialSettings.getTaxIdNumber())
                .setTaxDisplayName(financialSettings.getTaxIdDisplayNumber())
                .setTaxRegistered(financialSettings.isVatRegistered())
                .setEnableContractOutsite(financialSettings.isEnableContractOutsite())
                .setVatRegisteredOn(financialSettings.getVatRegisteredOn())
                .setTaxGenerationDate(financialSettings.getTaxGenerationDate())
                .setTaxes(taxDatas);
        //-----------------------------------------------------------------------------------------

        HashSet<String> modules = moduleManager.getEnabledModuleCodes();
        EdsUserSession userSession = userSessionManager.getUserSession(ServerSecurityContext.getInstance().getSessionId());

        SecurityContext.getInstance().removeCompanyId();
        SecurityContext.getInstance().setDatabase(Constants.DATABASE_FREE);

        EdsLocale userLocale = user.getLocale();
        try {
            backendServiceLocal.removeSchema(company.getObjectID());
            backendServiceLocal.createSchemaByID(company.getObjectID(), modules);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        SecurityContext.getInstance().setDatabase(Constants.DATABASE_FREE);
        SecurityContext.getInstance().setCompanyId(newCompany.getCompanyId());

        log.info("@Remove sampe data from company has been started: {}", company.getObjectID());
        CreatedCompany createdCompany = signupServiceLocal.createCompany(newCompany);
        log.info("@New company has been created: {}", company.getObjectID());
        EdsUser createdCompanyAdmin = userManager.getUserByUserID(createdCompany.getAdminId());
        createdCompanyAdmin.setLocale(userLocale);
        userManager.update(createdCompanyAdmin);

        SecurityContext.getInstance().setStaticUserID(createdCompany.getAdminId());

        //new company modules
        moduleService.save(company.getObjectID(), modules, true);

        accountingServiceLocal.initDefaultAndCompanyAccounts(createdCompany.getCurrencyId());

        //Save Accounting settings (For Munir)
        accountingServiceLocal.resaveCompanyAccountingSettings(company.getObjectID(), industry, accountingTool, billingAddress,
                setupItem, accountingSetup, companyStripeAccount, companyPaypalAccount);

        //session track
        AuthDetails authDetails = new AuthDetails(createdCompany.getCompanyId(), createdCompany.getAdminId(), Constants.DATABASE_FREE);
        authDetails.setSessionID(userSession.getSessionID());
        authDetails.setIpAddress(userSession.getIPAddress());
        authDetails.setUserAgent(userSession.getUserAgent());
        String sessionId = sessionService.obtainSession(authDetails);

        {
            RedisClient.setKey(sessionId, new AuthInfoItem().buildForBasicLogin(newCompany.getAdminEmail(), newCompany.getAdminPassword(), newCompany.getServiceId()), AuthInfoItem.class);
        }
        log.info("@Remove sample date from company has been finished: {}", company.getObjectID());
        return sessionId;
    }


    public boolean saveCompanyInit(String companyName, String language, String orgType, Integer timeZoneID, String selectedApps) {
        EdsUser user = employeeManager.getUser();
        EdsCompany company = companyManager.getUser().getCompany();

        if (language != null) {
            profileServiceLocal.saveLanguageForUser(language, true);
            company.setLocale(localeManager.getLocaleBylanguageCode(language) != null ? language : null);
        }
        if (Constants.DEFAULT_COMPANY_NAME.equals(company.getName())) {
            EdsCrmAccount crmAccount = crmAccountManager.get(1);
            if (crmAccount != null) {
                crmAccount.setName(companyName);
                crmAccountManager.createOrUpdate(crmAccount);
                try {
                    crmAccountSolrComponent.index(crmAccount);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        company.setName(companyName);
        company.setOrgType(orgType);

        if (timeZoneID != null) {
            company.setCountryZone(zoneManager.getCountryZone(timeZoneID));
        }
        company.setAnyDataMissing(!(companyName != null && !"".equalsIgnoreCase(companyName) && language != null && !"".equalsIgnoreCase(language)));
        EdsCurrency currency = company.getCountry().getCurrency() != null ? company.getCountry().getCurrency() : currencyManager.getCurrency(CurrencyManager.USD);

        if (company.getTestCompany()) {
            company.setCurrency(currency);
        } else {
            EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();

            if (financialSettings == null) {
                financialSettings = new EdsFinancialSettings();
            }
            financialSettings.setCurrency(currency);
            financialSettingsManager.createOrUpdate(financialSettings);
        }
        companyManager.update(company);

        CompanyData cData = new CompanyData();
        cData.setName(companyName);
        EdsLocale locale = localeManager.getLocaleBylanguageCode(language != null ? language : "en");
        cData.setLocaleId(locale != null ? locale.getObjectID() : null);
        cData.setCurrencyId(currency != null ? currency.getObjectID() : null);
        cData.setCompanyId(company.getObjectID());
        cData.setSelectedApps(selectedApps);
        cData.setRating("Activated");
        cData.setWhatDoesYourOrgDo(orgType);
        cData.setPreventWorkflow(true); //We must not run workflow
        Integer leadSignUpCompany = EdsContextParams.getLeadSignUpCompany();

        if (leadSignUpCompany != null) {
            rabbitMQService.sendCompanySettingsUpdate(cData, leadSignUpCompany);
        }
        try {
            messageManager.sendPasswordChangedNotification(user, false);
        } catch (EdsDbException ex) {
            ex.printStackTrace();
        }

        return company.getAnyDataMissing();
    }

    public boolean saveCompanyAccountingSettings(AccountingSetupItem setupItem) {

        if (setupItem.getModules() != null) {
            EdsCompany company = companyManager.getUser().getCompany();

            //Set Company Industry
            if (setupItem.getIndustryId() != null) {
                company.setWorkArea(referenceManager.get(setupItem.getIndustryId()));
            }
            //Set Accounting Tool they used so far
            if (StringUtils.isNotBlank(setupItem.getAccountingTool())) {
                company.setAccountingTool(setupItem.getAccountingTool());
            }
            //Set company currency
            if (StringUtils.isNotBlank(setupItem.getCurrencyCode())) {
                company.setCurrency(currencyManager.getCurrency(setupItem.getCurrencyCode()));
            }
            Address companyBillingAddress = setupItem.getCompanyBillingAddress();
            //Set Billing && Mailing Addresses
            EdsAddress billingAddress = company.getBillingAddresses() == null || company.getBillingAddresses().isEmpty() ? new EdsAddress() : company.getBillingAddresses().get(0);
            billingAddress.setEntity(company);
            billingAddress.setRelationType(EdsAddress.BILLING_ADDRESS);
            billingAddress.setAddress(companyBillingAddress.getAddress());
            billingAddress.setAddressb(companyBillingAddress.getAddressb());
            billingAddress.setZipCode(companyBillingAddress.getZipCode());
            billingAddress.setCity(companyBillingAddress.getCity());
            billingAddress.setPrimary(true);

            if (companyBillingAddress.getCountryId() != null) {
                EdsCountry country = countryManager.get(companyBillingAddress.getCountryId());
                if (country != null) {
                    billingAddress.setCountry(country);
                } else {
                    billingAddress.setCountry(null);
                }
            } else {
                billingAddress.setCountry(null);
            }

            if (companyBillingAddress.getStateId() != null) {
                EdsRegion state = regionManager.get(companyBillingAddress.getStateId());
                if (state != null && state.getCountry().equals(billingAddress.getCountry())) {
                    billingAddress.setState(state);
                } else {
                    billingAddress.setState(null);
                }
            } else {
                billingAddress.setState(null);
            }

            if (company.getBillingAddresses() == null) {
                addressManager.create(billingAddress);
                List<EdsAddress> billingAddresses = new ArrayList<>();
                billingAddresses.add(billingAddress);
                company.setBillingAddresses(billingAddresses);
            } else {
                addressManager.update(billingAddress);
            }
            //Set Conversion Date (Accounts start date)
            EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();

            if (financialSettings == null) {
                financialSettings = new EdsFinancialSettings();
                financialSettings.setCurrency(company.getCurrency());
            }
            financialSettings.setConversionDate(setupItem.getConversionDate());
            financialSettings.setTaxIdNumber(setupItem.getTaxIdNumber());
            financialSettings.setTaxIdDisplayNumber(setupItem.getTaxDisplayName());
            financialSettings.setTinDisplayName(setupItem.getTaxRegisterName());
            financialSettings.setTinNumber(setupItem.getTaxRegisterNumber());
            financialSettings.setShowVatNumberInInvoices(true);
            financialSettings.setVatRegistered(setupItem.isTaxRegistered());
            financialSettings.setEnableContractOutsite(setupItem.isEnableContractOutsite());
            financialSettings.setVatRegisteredOn(setupItem.getVatRegisteredOn());
            financialSettings.setTaxGenerationDate(setupItem.getTaxGenerationDate());
            financialSettings.setShowVatReturnReport(true);
            financialSettingsManager.createOrUpdate(financialSettings);

            if (setupItem.isEnableContractOutsite()) {
                genericSettingsManager.saveGenericSettings(null, GenericSettingsEnum.ACCOUNTING_IS_REVERSE_CHARGE, EdsGenericSettings.YES);
            }
            //Save Taxes
            if (setupItem.getTaxes() != null) {
                setupItem.getTaxes().forEach(taxData -> accountingServiceLocal.saveTaxRate(taxData));
            }
            company.setAccountingSetup(true);
            companyManager.update(company);
            boolean isCreatedCSR = createCSRForSaudiaCompanies();
            if (!isCreatedCSR) {
                return false;
            }
            List<String> allModules = Arrays.asList(SALES_QUOTES, SALES_ORDERS, REQUEST_FOR_QUOTES, RECURRING_BILLS, RESERVATIONS, CONSIGNMENTS, PURCHASE_ORDERS, RECCURING_INVOICES,
                    INVENTORY_MANAGEMENT, FIXED_ASSESTS, CHECKS, REQUEST_FOR_PURCHASES);

            List<String> modules = setupItem.getModules();
            List<String> modulesToDisable = allModules.stream().filter(m -> !modules.contains(m)).toList();

            moduleService.save(company.getObjectID(), new HashSet<>(modulesToDisable), false);
            moduleService.save(company.getObjectID(), new HashSet<>(modules), true);
            moduleService.save(company.getObjectID(), new HashSet<>(Arrays.asList(SALES_INVOICING, SUPPLIER_CENTER, BANK_ACCOUNTS, MANUAL_TRANSACTIONS, ACCOUNTING_CHART_OF_ACCOUNTS, PURCHASE_INVOICING, PRODUCTS_SERVICES, EXPENSE_REPORTING, ACCOUNTING_CUSTOMER_CENTER)), true);

            if ("paypal".equalsIgnoreCase(setupItem.getPaymentGateway()) && StringUtils.isNotBlank(setupItem.getPayPalMerchant())) {
                EdsInvoicingSettings invoiceSettings = invoicingSettingsManager.getInvoiceSettings(company);
                if (invoiceSettings == null) {
                    invoiceSettings = new EdsInvoicingSettings();
                }
                invoiceSettings.setPayPalAccount(setupItem.getPayPalMerchant());
                if (invoiceSettings.getObjectID() != null) {
                    invoicingSettingsManager.update(invoiceSettings);
                } else {
                    invoicingSettingsManager.create(invoiceSettings);
                }

            }

            //SEND CHANGES to LEAD of KPI
            CompanyData companyData = new CompanyData();
            companyData.setCompanyId(company.getObjectID());
            companyData.setName(company.getName());
            companyData.setAccountingTool(setupItem.getAccountingTool());
            companyData.setIndustryId(setupItem.getIndustryId());
            companyData.setAddress(companyBillingAddress);
            Integer leadSignupCompany = EdsContextParams.getLeadSignUpCompany();

            if (leadSignupCompany != null) {
                rabbitMQService.sendCompanySettingsUpdate(companyData, leadSignupCompany);
            }

            return company.getAccountingSetup();
        } else {
            return false;
        }
    }

    private boolean createCSRForSaudiaCompanies() {
        EdsCompany company = companyManager.get(SecurityContext.getCompanyID());
        if ("SA".equals(company.getCountry().getCode())) {
            try {
                zatcaService.initSettings();
            } catch (ZatcaException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    public boolean saveCompanyCrmSettings(Integer industryId, Address companyBillingAddress) {
        EdsCompany company = companyManager.getUser().getCompany();
        //Set Company Industry
        if (industryId != null && industryId > 0) {
            company.setWorkArea(referenceManager.get(industryId));
        }
        //Set Billing && Mailing Addresses
        EdsAddress billingAddress = company.getBillingAddresses() == null || company.getBillingAddresses().isEmpty() ? new EdsAddress() : company.getBillingAddresses().get(0);
        billingAddress.setEntity(company);
        billingAddress.setRelationType(EdsAddress.BILLING_ADDRESS);
        billingAddress.setAddress(companyBillingAddress.getAddress());
        billingAddress.setAddressb(companyBillingAddress.getAddressb());
        billingAddress.setZipCode(companyBillingAddress.getZipCode());
        billingAddress.setCity(companyBillingAddress.getCity());
        billingAddress.setPrimary(true);

        if (companyBillingAddress.getCountryId() != null) {
            EdsCountry country = countryManager.get(companyBillingAddress.getCountryId());
            if (country != null) {
                billingAddress.setCountry(country);
            } else {
                billingAddress.setCountry(null);
            }
        } else {
            billingAddress.setCountry(null);
        }

        if (companyBillingAddress.getStateId() != null) {
            EdsRegion state = regionManager.get(companyBillingAddress.getStateId());
            if (state != null && state.getCountry().equals(billingAddress.getCountry())) {
                billingAddress.setState(state);
            } else {
                billingAddress.setState(null);
            }
        } else {
            billingAddress.setState(null);
        }


        if (company.getBillingAddresses() == null) {
            addressManager.create(billingAddress);
            List<EdsAddress> billingAddresses = new ArrayList<>();
            billingAddresses.add(billingAddress);
            company.setBillingAddresses(billingAddresses);
        } else {
            addressManager.update(billingAddress);
        }

        company.setSalesSetup(true);
        companyManager.update(company);

        //SEND CHANGES to LEAD of KPI
            /*CompanyData companyData = new CompanyData();
            companyData.setCompanyId(company.getObjectID());
            companyData.setName(company.getName());
            companyData.setIndustryId(industryId);
            companyData.setAddress(companyBillingAddress);
            Integer leadSignupCompany = EdsContextParams.getLeadSignUpCompany();
            if (leadSignupCompany != null) {
                activeMQService.sendCompanySettingsUpdate(companyData, leadSignupCompany);
            }*/

        return company.getSalesSetup();

    }

    public void gymCompanyInit(CompanyInitTO companyInitTO) {
        Integer companyID = SecurityContext.getCompanyID();

        Integer timeZoneID = Optional.ofNullable(companyInitTO.getTimezone())
                .filter(t -> t.matches("\\d+"))
                .map(Integer::valueOf)
                .orElse(null);

        final HashSet<String> enabledModules = new HashSet<>();
        final HashSet<String> disabledModules = new HashSet<>();

        String selectedApps = "";

        if (companyInitTO.getModules().contains("payroll")) {
            enabledModules.add(PermissionConstants.PAYROLL);
            selectedApps = selectedApps + "Payroll";
        } else {
            disabledModules.add(PermissionConstants.PAYROLL);
        }

        if (companyInitTO.getModules().contains("projects")) {
            enabledModules.add(PermissionConstants.PM_MODULE);

            if (StringUtils.isNotBlank(selectedApps)) {
                selectedApps = selectedApps + ",";
            }
            selectedApps = selectedApps + "Projects";
        } else {
            disabledModules.add(PermissionConstants.PM_MODULE);
        }

        if (companyInitTO.getModules().contains("humans")) {
            enabledModules.add(PermissionConstants.HRMS_MODULE);

            if (StringUtils.isNotBlank(selectedApps)) {
                selectedApps = selectedApps + ",";
            }
            selectedApps = selectedApps + "Humans (HR)";
        } else {
            disabledModules.add(PermissionConstants.HRMS_MODULE);
        }

        if (companyInitTO.getModules().contains("sales")) {
            enabledModules.add(PermissionConstants.CRM_MODULE);

            if (StringUtils.isNotBlank(selectedApps)) {
                selectedApps = selectedApps + ",";
            }
            selectedApps = selectedApps + "Sales(CRM)";
        } else {
            disabledModules.add(PermissionConstants.CRM_MODULE);
        }

        if (companyInitTO.getModules().contains("accounts")) {
            enabledModules.add(PermissionConstants.ACCOUNTING_MODULE);

            if (StringUtils.isNotBlank(selectedApps)) {
                selectedApps = selectedApps + ",";
            }
            selectedApps = selectedApps + "Accounts";
        } else {
            disabledModules.add(PermissionConstants.ACCOUNTING_MODULE);
        }

        if (!enabledModules.isEmpty()) {
            enabledModules.add(PermissionConstants.REPORTING);
            enabledModules.add(PermissionConstants.DOCUMENTS_CONTEXT);

            moduleService.save(companyID, enabledModules, true);
        }

        if (!enabledModules.isEmpty()) {
            moduleService.save(companyID, disabledModules, false);
        }

        saveCompanyInit(companyInitTO.getCompanyName(), companyInitTO.getLanguage(), companyInitTO.getOrgType(), timeZoneID, selectedApps);
    }

    public void gymAccountingInit(AccountingInitTO accountingInitTO) {
        UserSignUPSessionID signedUser = loginServiceLocal.getSignedUser();
        Object user = SecurityContext.getInstance().getUser();
        String date = accountingInitTO.getConversionDateYearStr() + "-" + accountingInitTO.getConversionDateMonthStr();
        Date conversionDate = parseDate(date, "yyyy-MM");

        if (conversionDate != null) {
            conversionDate = ServerUtils.convertServerDateToUserDate(DateUtil.getMonthLastDate(conversionDate), signedUser.getTimeZone());
            Address billingAddress = new Address();
            billingAddress.setAddress(accountingInitTO.getAddress1());
            billingAddress.setAddressb(accountingInitTO.getAddress2());
            billingAddress.setCity(accountingInitTO.getCity());
            billingAddress.setZipCode(accountingInitTO.getZipCode());

            EdsCountry country = countryManager.getCountryByCode(signedUser.getCompanyCountryCode());

            if (country != null) {
                billingAddress.setCountryId(country.getObjectID());
            }
            billingAddress.setStateId(accountingInitTO.getStateId());
            ArrayList<TaxData> taxes = new ArrayList<>();

            if (Constants.VAT_COUNTRIES.contains(signedUser.getCompanyCountryCode())) {
                /**
                 * Tax permission type:
                 *
                 * permissionType = 1 means that the tax is not deleteable
                 * permissionType = 2 means that the tax is not editable
                 */

                if (Constants.GCC_COUNTRIES.contains(signedUser.getCompanyCountryCode())) {
                    TaxData taxExempt = new TaxData();
                    taxExempt.setPermissionType(2);
                    taxExempt.setTaxName("Exempt");
                    taxExempt.setKey(TaxKeyEnum.EXEMPT);
                    taxExempt.setComponents(new TaxComponentData[]{new TaxComponentData("Exempt", false, BigDecimal.ZERO)});
                    taxes.add(taxExempt);

                    TaxData taxOutofScope = new TaxData();
                    taxOutofScope.setPermissionType(2);
                    taxOutofScope.setTaxName("Out of Scope");
                    taxOutofScope.setKey(TaxKeyEnum.OUT_OF_SCOPE);
                    taxOutofScope.setComponents(new TaxComponentData[]{new TaxComponentData("Out of Scope", false, BigDecimal.ZERO)});
                    taxes.add(taxOutofScope);

                    TaxData taxStandartRate = new TaxData();
                    taxStandartRate.setPermissionType(1);
                    taxStandartRate.setTaxName("Standard Rate");
                    taxStandartRate.setKey(TaxKeyEnum.STANDARD_RATE);
                    taxStandartRate.setComponents(new TaxComponentData[]{new TaxComponentData("Standard Rate", false, new BigDecimal(5))});
                    taxes.add(taxStandartRate);
                }
                if (Constants.UK.equals(signedUser.getCompanyCountryCode())) {
                    TaxData taxReducedRate = new TaxData();
                    taxReducedRate.setPermissionType(1);
                    taxReducedRate.setTaxName("Reduced Rate");
                    taxReducedRate.setKey(TaxKeyEnum.REDUCED_RATE);
                    taxReducedRate.setComponents(new TaxComponentData[]{new TaxComponentData("Standard Rate", false, new BigDecimal(5))});
                    taxes.add(taxReducedRate);
                }

                TaxData taxStandartRate = new TaxData();
                taxStandartRate.setPermissionType(1);
                taxStandartRate.setTaxName("Standard Rate");
                taxStandartRate.setKey(TaxKeyEnum.STANDARD_RATE);
                taxStandartRate.setComponents(new TaxComponentData[]{new TaxComponentData("Standard Rate", false, new BigDecimal(Constants.UK.equals(signedUser.getCompanyCountryCode()) ? 20 : 15))});
                taxes.add(taxStandartRate);

                TaxData taxZeroRate = new TaxData();
                taxZeroRate.setPermissionType(1);
                taxZeroRate.setTaxName("Zero Rate");
                taxZeroRate.setKey(TaxKeyEnum.ZERO_RATE);
                taxZeroRate.setComponents(new TaxComponentData[]{new TaxComponentData("Zero Rate", false, BigDecimal.ZERO)});
                taxes.add(taxZeroRate);
            } else if (accountingInitTO.getTaxNames() != null && accountingInitTO.getTaxPercents() != null) {
                for (int i = 0; i < accountingInitTO.getTaxNames().size(); i++) {

                    if (StringUtils.isNotBlank(accountingInitTO.getTaxNames().get(i)) && StringUtils.isNotBlank(accountingInitTO.getTaxPercents().get(i))) {
                        try {
                            TaxData taxData = new TaxData();
                            taxData.setTaxName(accountingInitTO.getTaxNames().get(i));
                            TaxComponentData[] taxComponentData = new TaxComponentData[1];
                            taxComponentData[0] = new TaxComponentData();
                            taxComponentData[0].setName(accountingInitTO.getTaxNames().get(i));
                            taxComponentData[0].setRate(new BigDecimal(accountingInitTO.getTaxPercents().get(i)));
                            taxData.setComponents(taxComponentData);
                            taxes.add(taxData);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            AccountingSetupItem setupItem = new AccountingSetupItem()
                    .setConversionDate(conversionDate)
                    .setCurrencyCode(accountingInitTO.getCurrencyCode())
                    .setIndustryId(accountingInitTO.getIndustryId())
                    .setAccountingTool(accountingInitTO.getAccountingTool())
                    .setCompanyBillingAddress(billingAddress)
                    .setTaxIdNumber(accountingInitTO.getTaxIdNumber())
                    .setTaxDisplayName(accountingInitTO.getTaxDisplayName())
                    .setTaxes(taxes)
                    .setModules(accountingInitTO.getModules() != null ? accountingInitTO.getModules() : new ArrayList<>())
                    .setTaxRegistered("REGISTERED".equalsIgnoreCase(accountingInitTO.getTaxRegistered()))
                    .setTaxIdNumber(accountingInitTO.getTaxIdNumber())
                    .setTaxDisplayName(accountingInitTO.getTaxDisplayName())
                    .setTaxRegisterName(accountingInitTO.getTinName())
                    .setTaxRegisterNumber(accountingInitTO.getTinNumber())
                    .setEnableContractOutsite("on".equalsIgnoreCase(accountingInitTO.getEnableContractOutside()))
                    .setTaxGenerationDate(parseDate(accountingInitTO.getTaxGenerationDate(), "MM/dd/yyyy"))
                    .setVatRegisteredOn(parseDate(accountingInitTO.getVatRegisteredOn(), "MM/dd/yyyy"))
                    .setPaymentGateway(accountingInitTO.getPaymentGateway())
                    .setPayPalMerchant(accountingInitTO.getPayPalMerchant());
            saveCompanyAccountingSettings(setupItem);
        } else {
            ModelAndView initView = new ModelAndView("accountinginit").addObject("errormsg", "Modules must not be empty.");
            initView.addObject("industries", ServerUtils.getAsSelectItem(referenceManager.listReferences(Constants._COMPANY_INDUSTRY), ServerUtils.REFERENCE));
            initView.addObject("states", clientService.getRegions());
            initView.addObject("fullname", signedUser.getFullName());
            initView.addObject("productname", EdsContextParams.getProductName());
            initView.addObject("tax_names", accountingInitTO.getTaxNames());
            initView.addObject("tax_percents", accountingInitTO.getTaxPercents());
            if (StringUtils.isNotBlank(accountingInitTO.getConversionDateYearStr()) && accountingInitTO.getConversionDateMonthStr() != null) {
                initView.addObject("conversion_date_month", accountingInitTO.getConversionDateMonthStr());
                initView.addObject("conversion_date_year", accountingInitTO.getConversionDateYearStr());
            } else {
                EdsFinancialSettings financialSettings = financialSettingsManager.getFinancialSettings();
                if (financialSettings != null && financialSettings.getConversionDate() != null) {
                    initView.addObject("conversion_date_month", financialSettings.getConversionDate().getMonth());
                    initView.addObject("conversion_date_year", new SimpleDateFormat("yyyy").format(financialSettings.getConversionDate()));
                } else {
                    initView.addObject("conversion_date_month", 0);
                    initView.addObject("conversion_date_year", new SimpleDateFormat("yyyy").format(new Date()));
                }
            }
            if (accountingInitTO.getIndustryId() != null) {
                initView.addObject("industryid", accountingInitTO.getIndustryId());
            }
            if (StringUtils.isNotBlank(accountingInitTO.getAccountingTool())) {
                initView.addObject("accounting_tool", accountingInitTO.getAccountingTool());
            }
            if (StringUtils.isNotBlank(accountingInitTO.getAddress1())) {
                initView.addObject("address1", accountingInitTO.getAddress1());
            }
            if (StringUtils.isNotBlank(accountingInitTO.getAddress2())) {
                initView.addObject("address2", accountingInitTO.getAddress2());
            }
            if (StringUtils.isNotBlank(accountingInitTO.getCity())) {
                initView.addObject("city", accountingInitTO.getCity());
            }
            if (accountingInitTO.getCountryId() != null) {
                initView.addObject("countryid", accountingInitTO.getCountryId());
            }
            if (accountingInitTO.getStateId() != null) {
                initView.addObject("stateid", accountingInitTO.getStateId());
            }
            if (StringUtils.isNotBlank(accountingInitTO.getZipCode())) {
                initView.addObject("zip", accountingInitTO.getZipCode());
            }
        }
    }

    private Date parseDate(String dateStr, String pattern) {
        if (StringUtils.isNotBlank(dateStr)) {
            try {
                return new SimpleDateFormat(pattern).parse(dateStr);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
