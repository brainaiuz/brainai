package com.edatasite.workforce.gwt.core.server.controllers;

import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.accounting.EdsFinancialSettings;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxComponentData;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxData;
import com.edatasite.workforce.gwt.core.client.enums.ImportTypeEnum;
import com.edatasite.workforce.gwt.core.client.enums.TaxKeyEnum;
import com.edatasite.workforce.gwt.core.client.rpc.Address;
import com.edatasite.workforce.gwt.core.client.rpc.CommonService;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.DateUtil;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.AllInOneServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.CompanyService;
import com.edatasite.workforce.gwt.core.server.app.LoginServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.controllers.dto.AccountingSetupItem;
import com.edatasite.workforce.gwt.core.server.db.CountryManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.FinancialSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.rpc.UserSignUPSessionID;
import com.edatasite.workforce.gwt.employee.server.app.EmployeeServiceLocal;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFileService;
import com.edatasite.workforce.gwt.importfile.client.rpc.ReportDataImportItem;
import com.edatasite.workforce.gwt.modulesettings.client.ModuleService;
import com.edatasite.workforce.gwt.newemployee.client.rpc.NewEmployee;
import com.edatasite.workforce.gwt.reportingsystem.client.service.ReportingService;
import com.edatasite.workforce.utils.EdsContextParams;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import static com.edatasite.workforce.gwt.core.client.ui.Constants.DEFAULT_SECTION;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.GCC_COUNTRIES;
import static com.edatasite.workforce.gwt.core.client.ui.Constants.UK;
import static com.edatasite.workforce.gwt.core.client.ui.Constants._COMPANY_INDUSTRY;

@Controller
public class CompanyInitController {
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private CompanyService companyService;
    @Autowired
    private CommonService commonService;
    @Autowired
    @Qualifier("loginService")
    protected LoginServiceLocal loginServiceLocal;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private FinancialSettingsManager financialSettingsManager;
    @Autowired
    private AllInOneServiceLocal allInOneServiceLocal;
    @Autowired
    private EmployeeServiceLocal employeeServiceLocal;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private CountryManager countryManager;

    @Autowired
    private ImportFileService importFileService;
    @Autowired
    private ReportingService reportingService;

    @RequestMapping(value = {"/companyinit.html"}, method = RequestMethod.POST)
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ServerUtils.setUserSessionid(request);
        UserSignUPSessionID signedUser = loginServiceLocal.getSignedUser();
        String companyName = request.getParameter("name");
        String language = request.getParameter("language");

        Integer timeZoneID = null;
        String timezone = request.getParameter("timezone");

        if (timezone != null && timezone.matches("\\d+")) {
            timeZoneID = Integer.valueOf(timezone);
        }
        String orgType = request.getParameter("orgType");
        List<String> modules = Arrays.asList(request.getParameterValues("modules"));

        final HashSet<String> enabledModules = new HashSet<>();
        final HashSet<String> disabledModules = new HashSet<>();

        String section = DEFAULT_SECTION;
        String selectedApps = "";

        if (modules.contains("payroll")) {
            section = "Payroll";
            enabledModules.add(PermissionConstants.PAYROLL);
            selectedApps = selectedApps + "Payroll";
        } else {
            disabledModules.add(PermissionConstants.PAYROLL);
        }

        if (modules.contains("projects")) {
            section = "ProjectManagement";
            enabledModules.add(PermissionConstants.PM_MODULE);

            if (StringUtils.isNotBlank(selectedApps)) {
                selectedApps = selectedApps + ",";
            }
            selectedApps = selectedApps + "Projects";
        } else {
            disabledModules.add(PermissionConstants.PM_MODULE);
        }

        if (modules.contains("humans")) {
            section = "Hrms";
            enabledModules.add(PermissionConstants.HRMS_MODULE);

            if (StringUtils.isNotBlank(selectedApps)) {
                selectedApps = selectedApps + ",";
            }
            selectedApps = selectedApps + "Humans (HR)";
        } else {
            disabledModules.add(PermissionConstants.HRMS_MODULE);
        }

        if (modules.contains("sales")) {
            section = "Crm";
            enabledModules.add(PermissionConstants.CRM_MODULE);

            if (StringUtils.isNotBlank(selectedApps)) {
                selectedApps = selectedApps + ",";
            }
            selectedApps = selectedApps + "Sales(CRM)";
        } else {
            disabledModules.add(PermissionConstants.CRM_MODULE);
        }

        if (modules.contains("accounts")) {
            section = "Accounting";
            enabledModules.add(PermissionConstants.ACCOUNTING_MODULE);

            if (StringUtils.isNotBlank(selectedApps)) {
                selectedApps = selectedApps + ",";
            }
            selectedApps = selectedApps + "Accounts";
        } else {
            disabledModules.add(PermissionConstants.ACCOUNTING_MODULE);
        }

        if (!enabledModules.isEmpty()) {
            //free modules
            enabledModules.add(PermissionConstants.REPORTING);
            enabledModules.add(PermissionConstants.DOCUMENTS_CONTEXT);

            moduleService.save(signedUser.getCompanyId(), enabledModules, true);
        }

        if (!enabledModules.isEmpty()) {
            moduleService.save(signedUser.getCompanyId(), disabledModules, false);
        }

        //SAVE COMPANY SETTINGS
        companyService.saveCompanyInit(companyName, language, orgType, timeZoneID, selectedApps);

        return new ModelAndView("redirect:/" + section + ".html");
    }

    @RequestMapping(value = {"/accountinginit.html"}, method = RequestMethod.POST)
    public ModelAndView handleAccountingInit(
            @RequestParam(name = "conversion_date_month", required = false) Integer conversionDateMonthStr,
            @RequestParam(name = "conversion_date_year", required = false) String conversionDateYearStr,
            @RequestParam(name = "currency", required = false) String currencyCode,
            @RequestParam(name = "industry", required = false) Integer industryId,
            @RequestParam(name = "accounting_tool", required = false) String accounting_tool,
            @RequestParam(name = "tax_registered", required = false) String taxRegistered,
            @RequestParam(name = "tax_id_number", required = false) String taxIdNumber,
            @RequestParam(name = "tax_register_name", required = false) String tinName,
            @RequestParam(name = "tax_register_number", required = false) String tinNumber,
            @RequestParam(name = "tax_display_name", required = false) String taxDisplayName,
            @RequestParam(name = "enable_contract_outside", required = false) String enableContractOutside,
            @RequestParam(name = "tax_generation_date", required = false) String taxGenerationDate,
            @RequestParam(name = "vat_registered_on", required = false) String vatRegisteredOn,
            @RequestParam(name = "tax_name", required = false) List<String> taxNames,
            @RequestParam(name = "tax_percent", required = false) List<String> taxPercents,
            @RequestParam(name = "billing_addr_1", required = false) String address1,
            @RequestParam(name = "billing_addr_2", required = false) String address2,
            @RequestParam(name = "billing_addr_city", required = false) String city,
            @RequestParam(name = "billing_addr_country", required = false) Integer countryId,
            @RequestParam(name = "billing_addr_state", required = false) Integer stateId,
            @RequestParam(name = "billing_addr_zip", required = false) String zipCode,
            @RequestParam(name = "modules", required = false) List<String> modules,
            @RequestParam(name = "payment-gateway", required = false) String paymentGateway,
            @RequestParam(name = "paypal-account", required = false) String payPalMerchant,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ServerUtils.setUserSessionid(request);
        UserSignUPSessionID signedUser = loginServiceLocal.getSignedUser();
        Date conversionDate = null;
        conversionDate = new SimpleDateFormat("yyyy-MM").parse(conversionDateYearStr + "-" + conversionDateMonthStr);

        if (conversionDate != null) {
            conversionDate = ServerUtils.convertServerDateToUserDate(DateUtil.getMonthLastDate(conversionDate), signedUser.getTimeZone());
            Address billingAddress = new Address();
            billingAddress.setAddress(address1);
            billingAddress.setAddressb(address2);
            billingAddress.setCity(city);
            billingAddress.setZipCode(zipCode);

            EdsCountry country = countryManager.getCountryByCode(signedUser.getCompanyCountryCode());

            if (country != null) {
                billingAddress.setCountryId(country.getObjectID());
            }
            billingAddress.setStateId(stateId);
            ArrayList<TaxData> taxes = new ArrayList<>();

            if (Constants.VAT_COUNTRIES.contains(signedUser.getCompanyCountryCode())) {
                /**
                 * Tax permission type:
                 *
                 * permissionType = 1 means that the tax is not deleteable
                 * permissionType = 2 means that the tax is not editable
                 */

                if (GCC_COUNTRIES.contains(signedUser.getCompanyCountryCode())) {
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
                if (UK.equals(signedUser.getCompanyCountryCode())) {
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
                taxStandartRate.setComponents(new TaxComponentData[]{new TaxComponentData("Standard Rate", false, new BigDecimal(UK.equals(signedUser.getCompanyCountryCode()) ? 20 : 15))});
                taxes.add(taxStandartRate);

                TaxData taxZeroRate = new TaxData();
                taxZeroRate.setPermissionType(1);
                taxZeroRate.setTaxName("Zero Rate");
                taxZeroRate.setKey(TaxKeyEnum.ZERO_RATE);
                taxZeroRate.setComponents(new TaxComponentData[]{new TaxComponentData("Zero Rate", false, BigDecimal.ZERO)});
                taxes.add(taxZeroRate);
            } else if (taxNames != null && taxPercents != null) {
                for (int i = 0; i < taxNames.size(); i++) {

                    if (StringUtils.isNotBlank(taxNames.get(i)) && StringUtils.isNotBlank(taxPercents.get(i))) {
                        try {
                            TaxData taxData = new TaxData();
                            taxData.setTaxName(taxNames.get(i));
                            TaxComponentData[] taxComponentData = new TaxComponentData[1];
                            taxComponentData[0] = new TaxComponentData();
                            taxComponentData[0].setName(taxNames.get(i));
                            taxComponentData[0].setRate(new BigDecimal(taxPercents.get(i)));
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
                    .setCurrencyCode(currencyCode)
                    .setIndustryId(industryId)
                    .setAccountingTool(accounting_tool)
                    .setCompanyBillingAddress(billingAddress)
                    .setTaxIdNumber(taxIdNumber)
                    .setTaxDisplayName(taxDisplayName)
                    .setTaxes(taxes)
                    .setModules(modules != null ? modules : new ArrayList<>())
                    .setTaxRegistered("REGISTERED".equalsIgnoreCase(taxRegistered))
                    .setTaxIdNumber(taxIdNumber)
                    .setTaxDisplayName(taxDisplayName)
                    .setTaxRegisterName(tinName)
                    .setTaxRegisterNumber(tinNumber)
                    .setEnableContractOutsite("on".equalsIgnoreCase(enableContractOutside))
                    .setTaxGenerationDate(StringUtils.isNotBlank(taxGenerationDate) ? new SimpleDateFormat("MM/dd/yyyy").parse(taxGenerationDate) : null)
                    .setVatRegisteredOn(StringUtils.isNotBlank(vatRegisteredOn) ? new SimpleDateFormat("MM/dd/yyyy").parse(vatRegisteredOn) : null)
                    .setPaymentGateway(paymentGateway)
                    .setPayPalMerchant(payPalMerchant);
            companyService.saveCompanyAccountingSettings(setupItem);
        } else {
            ModelAndView initView = new ModelAndView("accountinginit").addObject("errormsg", "Modules must not be empty.");
            initView.addObject("industries", ServerUtils.getAsSelectItem(referenceManager.listReferences(_COMPANY_INDUSTRY), ServerUtils.REFERENCE));
            initView.addObject("states", commonService.getRegions());
            initView.addObject("fullname", signedUser.getFullName());
            initView.addObject("productname", EdsContextParams.getProductName());
            initView.addObject("tax_names", taxNames);
            initView.addObject("tax_percents", taxPercents);
            if (StringUtils.isNotBlank(conversionDateYearStr) && conversionDateMonthStr != null) {
                initView.addObject("conversion_date_month", conversionDateMonthStr);
                initView.addObject("conversion_date_year", conversionDateYearStr);
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
            if (industryId != null) {
                initView.addObject("industryid", industryId);
            }
            if (StringUtils.isNotBlank(accounting_tool)) {
                initView.addObject("accounting_tool", accounting_tool);
            }
            if (StringUtils.isNotBlank(address1)) {
                initView.addObject("address1", address1);
            }
            if (StringUtils.isNotBlank(address2)) {
                initView.addObject("address2", address2);
            }
            if (StringUtils.isNotBlank(city)) {
                initView.addObject("city", city);
            }
            if (countryId != null) {
                initView.addObject("countryid", countryId);
            }
            if (stateId != null) {
                initView.addObject("stateid", stateId);
            }
            if (StringUtils.isNotBlank(zipCode)) {
                initView.addObject("zip", zipCode);
            }
            return initView;
        }

        if (StringUtils.isNotBlank(paymentGateway)) {
            StringBuilder requestUrl = new StringBuilder("https://");
            requestUrl.append(request.getServerName());

            if ("stripe".equalsIgnoreCase(paymentGateway)) {
                if (StringUtils.isNotBlank(EdsContextParams.getStripePublicKey()) && EdsContextParams.getStripePublicKey().startsWith("pk_live_")) {
                    return new ModelAndView("redirect:https://connect.stripe.com/oauth/authorize?response_type=code&client_id=ca_EVfkOl28v40YPw8EPxaxnYSm7z8Os6oo&scope=read_write&redirect_uri=" + requestUrl + "/stripe-accounting-authorize");
                } else {
                    return new ModelAndView("redirect:https://connect.stripe.com/oauth/authorize?response_type=code&client_id=ca_EVfk8GBFFAo0x4BPqKgtpv43csnCmgBa&scope=read_write&redirect_uri=" + requestUrl + "/stripe-accounting-authorize");
                }

            }
        }

        return new ModelAndView("redirect:/Accounting.html");
    }

    @RequestMapping(value = {"/reportinginit.html"}, method = RequestMethod.POST)
    public ModelAndView handleImportReportDataInit(
            @RequestParam(name = "columnName", required = false) List<String> columnNames,
            @RequestParam(name = "onOfValue", required = false) List<String> onOfValue,
            @RequestParam(name = "columnType", required = false) List<String> columnTypes,
            @RequestParam(name = "fortmatType", required = false) List<String> fortmatTypes,
            @RequestParam(name = "importfileId", required = false) Integer importfileId,
            @RequestParam(name = "categoryId", required = false) Integer categoryId,
            @RequestParam(name = "cancel", required = false) String cancel) {

        if ("true".equals(cancel)) {
            return new ModelAndView("redirect:/Reporting.html#");
        }
        ReportDataImportItem importItem = new ReportDataImportItem();
        importItem.setObjectID(importfileId);
        importItem.setCategoryID(categoryId);

        SelectItem[] dynamicItems = new SelectItem[columnNames.size()];
        int stringValue = 1, numberValue = 1, dateValue = 1;
        for (int i = 0; i < columnNames.size(); i++) {
            dynamicItems[i] = new SelectItem();
            dynamicItems[i].setId(i);
            dynamicItems[i].setName(columnNames.get(i));
            String format = fortmatTypes.get(i);
            if (format != null) {
                dynamicItems[i].setSelectedId(Integer.valueOf(format));
                String columnDateFormat = null;
                String code;
                String referencecode;
                switch (format) {
                    case "2", "3" -> {
                        code = "number";
                        if (format.equals("3")) {
                            code = "money";
                        }
                        referencecode = "double_value" + numberValue;
                        numberValue++;
                    }
                    case "4", "5" -> {
                        code = "date";
                        columnDateFormat = "short";
                        if (format.equals("5")) {
                            columnDateFormat = "long";
                        }
                        referencecode = "date_value" + dateValue;
                        dateValue++;
                    }
                    default -> {
                        code = "string";
                        referencecode = "string_value" + stringValue;
                        stringValue++;
                    }
                }
                dynamicItems[i].setCode(code);
                dynamicItems[i].setParam(columnDateFormat);
                dynamicItems[i].setReferenceCode(referencecode);
                dynamicItems[i].setSelected(true);

            }
            if (onOfValue.get(i).equals("off")) {
                dynamicItems[i].setSelected(false);
            }

        }
        importItem.setItems(dynamicItems);

        ImportFile importFile = importItem.getImportFile();

        importFile.setDefaultSeparator(',');
        importFile.setHasHeader(true);
        importFile.setType(ImportTypeEnum.REPORT_DATA);

        String inProgress = importFileService.addImportToQueue(importFile);
        if (inProgress != null && !"".equals(inProgress)) {
            ModelAndView initView = new ModelAndView("reportinginit");
            initView.addObject("errormsg", "Import is already in progress. Please wait until the process is finished.");
            initView.addObject("reportcategoryid", categoryId);
            return initView;
        }
        Integer templateId = reportingService.createReportXmlTemplateFromFile(importFile);

        return new ModelAndView("redirect:/Reporting.html#reporting|stepControl/" + templateId + "/template");
    }


    @RequestMapping(value = {"/salesinit.html"}, method = RequestMethod.POST)
    public ModelAndView handleSalesInit(
            @RequestParam(name = "industry", required = false) Integer industryId,
            @RequestParam(name = "invited_user_name", required = false) List<String> invitedUserNames,
            @RequestParam(name = "invited_user_email", required = false) List<String> invitedUserEmails,
            @RequestParam(name = "invited_user_role", required = false) List<String> invitedUserRoles,
            @RequestParam(name = "comp_addr_1", required = false) String address1,
            @RequestParam(name = "comp_addr_2", required = false) String address2,
            @RequestParam(name = "comp_addr_city", required = false) String city,
            @RequestParam(name = "comp_addr_country", required = false) Integer countryId,
            @RequestParam(name = "comp_addr_state", required = false) Integer stateId,
            @RequestParam(name = "comp_addr_zip", required = false) String zipCode,
            HttpServletRequest request, HttpServletResponse response) throws Exception {

        ServerUtils.setUserSessionid(request);
        UserSignUPSessionID signedUser = loginServiceLocal.getSignedUser();

//        List<String> modules = Arrays.asList(request.getParameterValues("modules"));
//        if (industryId != null) {

        Address address = new Address();
        address.setAddress(address1);
        address.setAddressb(address2);
        address.setCity(city);
        address.setZipCode(zipCode);
        address.setCountryId(countryId);
        address.setStateId(stateId);

        //@TODO comment for now
        companyService.saveCompanyCrmSettings(industryId, address);

        //CREATE INVITED USERS as EMPLOYEES
        try {
            for (int i = 0; i < invitedUserEmails.size(); i++) {
                if (StringUtils.isNotBlank(invitedUserEmails.get(i)) && StringUtils.isNotBlank(invitedUserNames.get(i)) && Integer.valueOf(invitedUserRoles.get(i)) > 0) {
                    NewEmployee employee = new NewEmployee();
                    employee.setCreatedFrom("SALES SETUP");
                    if (invitedUserNames != null && invitedUserNames.size() == invitedUserEmails.size()) {
                        employee.setFname(invitedUserNames.get(i));
                    }
                    if (invitedUserRoles != null && invitedUserRoles.size() == invitedUserEmails.size()) {
                        employee.setRole(Integer.valueOf(invitedUserRoles.get(i)));
                    }
                    employee.setEmail(invitedUserEmails.get(i).trim());
                    employee.setIsFromMultiEmployee(true);

                    Integer employeeID = employeeServiceLocal.createEmployeeInternal(employee, null);

                    //Send Email
                    messageManager.sendEmployeeAddNotification(employeeManager.get(employeeID), messageManager.getUser());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        /*} else {
            ModelAndView initView = new ModelAndView("accountinginit").addObject("errormsg", "Industry must not be empty.");
            initView.addObject("industries", ServerUtils.getAsSelectItem(referenceManager.listReferences(_COMPANY_WORKAREA), ServerUtils.REFERENCE));
            initView.addObject("states", commonService.getRegions());
            initView.addObject("productname", EdsContextParams.getProductName());
            initView.addObject("names", invitedUserNames);
            initView.addObject("emails", invitedUserEmails);
//            initView.addObject("roles", invitedUserRoles);
            initView.addObject("roles", allInOneServiceLocal.getRoles() ); //coreService.getCompanyRoles()

            if (industryId != null) {
                initView.addObject("industryid", industryId);
            }
            if (StringUtils.isNotBlank(address1)) {
                initView.addObject("address1", address1);
            }
            if (StringUtils.isNotBlank(address2)) {
                initView.addObject("address2", address2);
            }
            if (StringUtils.isNotBlank(city)) {
                initView.addObject("city", city);
            }
            if (countryId != null) {
                initView.addObject("countryid", countryId);
            }
            if (stateId != null) {
                initView.addObject("stateid", stateId);
            }
            if (StringUtils.isNotBlank(zipCode)) {
                initView.addObject("zip", zipCode);
            }
            return initView;
        }*/

        return new ModelAndView("redirect:/Crm.html");
    }
}
