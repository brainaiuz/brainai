package com.edatasite.workforce.gwt.myaccount.server.app;

import com.edatasite.shared.db.EdsDbException;
import com.edatasite.shared.mail.EdsMailer;
import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsCompanySystemSettings;
import com.edatasite.workforce.core.domain.EdsCountry;
import com.edatasite.workforce.core.domain.EdsMessage;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsSubscriptionHistory;
import com.edatasite.workforce.core.domain.EdsSubscriptionPayment;
import com.edatasite.workforce.core.domain.EdsUsagePlan;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsWorldPayHistory;
import com.edatasite.workforce.core.tools.GlobalAuthJdbcSpringManager;
import com.edatasite.workforce.gwt.core.client.enums.PaymentTypeEnum;
import com.edatasite.workforce.gwt.core.client.rpc.DateNonConvertable;
import com.edatasite.workforce.gwt.core.client.rpc.KpiPaymentRequestObject;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.CommonServiceLocal;
import com.edatasite.workforce.gwt.core.server.app.ListUtils;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.app.social.revolut.RevolutService;
import com.edatasite.workforce.gwt.core.server.app.social.revolut.dto.RevolutResponseDto;
import com.edatasite.workforce.gwt.core.server.db.CompanyEmailManager;
import com.edatasite.workforce.gwt.core.server.db.CompanyManager;
import com.edatasite.workforce.gwt.core.server.db.CompanySystemSettingsManager;
import com.edatasite.workforce.gwt.core.server.db.CountryManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.InvoiceManager;
import com.edatasite.workforce.gwt.core.server.db.MessageManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.SubscriptionHistoryManager;
import com.edatasite.workforce.gwt.core.server.db.SubscriptionPaymentManager;
import com.edatasite.workforce.gwt.core.server.db.UsagePlanManager;
import com.edatasite.workforce.gwt.core.server.db.WorldPayHistoryManager;
import com.edatasite.workforce.gwt.core.server.security.SecurityContext;
import com.edatasite.workforce.gwt.core.server.servlets.pdf.IPostPDFHandler;
import com.edatasite.workforce.gwt.invoice.client.rpc.SaveResult;
import com.edatasite.workforce.gwt.modulesettings.client.ModuleService;
import com.edatasite.workforce.gwt.myaccount.client.PricingUtils;
import com.edatasite.workforce.gwt.myaccount.client.rpc.MyAccountService;
import com.edatasite.workforce.gwt.myaccount.client.rpc.RequestQuoteItem;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanItem;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanPrice;
import com.edatasite.workforce.gwt.pricing.client.UserRateItem;
import com.edatasite.workforce.mail.EdsTemplateException;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.InvoiceDto;
import com.edatasite.workforce.rest.v3.release10.accounting.dto.LineItemDto;
import com.edatasite.workforce.rest.v3.release10.core.request.CustomFieldRequest;
import com.edatasite.workforce.rest.v3.release10.core.to.IdDTO;
import com.edatasite.workforce.rest.v3.release10.core.to.ItemDto;
import com.edatasite.workforce.utils.EdsContextParams;
import com.google.common.collect.Sets;
import com.google.gwt.user.server.rpc.security.ServerSecurityContext;
import com.stripe.Stripe;
import com.stripe.exception.ApiConnectionException;
import com.stripe.exception.ApiException;
import com.stripe.exception.AuthenticationException;
import com.stripe.exception.CardException;
import com.stripe.exception.InvalidRequestException;
import com.stripe.model.Charge;
import com.stripe.model.Customer;
import com.stripe.model.Invoice;
import com.stripe.model.InvoiceLineItem;
import com.stripe.model.InvoiceLineItemPeriod;
import com.stripe.model.Plan;
import com.stripe.model.Product;
import com.stripe.model.Subscription;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.WfmMessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * User: S11A
 * Date: Dec 5, 2008
 * Time: 10:45:26 AM
 */
@Transactional
@Service("myAccountService")
public class MyAccountServiceImpl implements MyAccountService, Constants, MyAccountServiceLocal {

    public static final DateFormat df = new SimpleDateFormat("HH:mm:ss MMM dd, yyyy z");//do not change
    private static final Logger log = LoggerFactory.getLogger(MyAccountServiceImpl.class);
    @Autowired
    private ModuleService moduleService;
    @Autowired
    private UsagePlanManager usagePlanManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private CountryManager countryManager;
    @Autowired
    private EmployeeManager employeeManager;
    @Autowired
    private SubscriptionHistoryManager subscriptionHistoryManager;
    @Autowired
    private CompanyManager companyManager;
    @Autowired
    private CompanyEmailManager companyEmailManager;
    @Autowired
    @Qualifier("commonService")
    private CommonServiceLocal commonServiceLocal;
    @Autowired
    @Qualifier("referenceWfmMessageSource")
    private WfmMessageSource referenceWfmMessageSource;
    @Autowired
    private SubscriptionPaymentManager subscriptionPaymentManager;
    @Autowired
    private WorldPayHistoryManager worldPayHistoryManager;
    @Autowired
    private MessageManager messageManager;
    @Autowired
    private CompanySystemSettingsManager companySystemSettingsManager;
    @Autowired
    private GlobalAuthJdbcSpringManager globalAuthJdbcSpringManager;
    @Autowired
    @Qualifier("kpiPaymentViewPdfHandler")
    private IPostPDFHandler kpiPaymentViewPdfHandler;
    private final RestTemplate restTemplate = new RestTemplate();
    @Autowired
    private RevolutService revolutService;
    @Autowired
    private InvoiceManager invoiceManager;

    @Override
    public Boolean sendRequestQuote(RequestQuoteItem requestQuoteItem) {
        EdsUser user = messageManager.getUser();
        EdsCompany company = user.getCompany();
        String subject = "NEW QUOTE REQUESTED";
        String to = "support@workforcetrack.com";
        String message = "<html>" +
                "<body>" +
                "Following user requested a quote for new subscription plan: <BR>" +
                "<BR>Company Name: " + company.getName() +
                "<BR>Company Email: " + company.getEmail() +
                "<BR>Company Phone: " + company.getPhone() +
                "<BR>Contact Person Name: " + user.getName() +
                "<BR>Contact Person Email: " + user.getEmail() +
                "<BR><BR><b>Current subscription plan:</b>" +
                "<BR>Subscription Period: " + requestQuoteItem.getCurrentSubscriptionPeriod() +
                "<BR>Users: " + requestQuoteItem.getCurrentUsersCount() +
                "<BR>Status: " + requestQuoteItem.getCurrentStatus() +
                "<BR>Total Amount: " + requestQuoteItem.getCurrentTotalAmount() +
                "<BR>Current Support package: " + requestQuoteItem.getCurrentSupportPackageNAME() +
                "<BR>Current Support package price: " + requestQuoteItem.getCurrentSupportPackagePrice() +
                "<BR><BR><b>Requested subscription plan:</b>" +
                "<BR>Subscription Period: " + requestQuoteItem.getRequestedSubscriptionPeriod() +
                "<BR>Users: " + requestQuoteItem.getRequestedUsersCount() +
                "<BR>Support package: " + requestQuoteItem.getRequestedSupportPackageNAME() +
                "<BR>Support package price: " + requestQuoteItem.getRequestedSupportPackagePrice() +
                "</body>" +
                "<html>";

        EdsMailer mailer;
        try {
            EdsMessage edsMessage = new EdsMessage();
            edsMessage.setSubject(subject);
            edsMessage.setText(message);
            edsMessage.setCompanyID(company.getObjectID());
            edsMessage.setTo(to);
            edsMessage.addBcc(companyEmailManager.getCompanyEmail(company.getObjectID()));
            mailer = EdsMailer.getNewInstance(edsMessage, null);
        } catch (Exception ex) {
            System.out.println("Cannot get instance of EdsMailer, exception: " + ex);
            return false;
        }
        mailer.send();
        System.out.println("Message sent[TO:" + to + ", SUBJECT:" + subject + "]");
        return true;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public UserRateItem getUserRateAndUpgUserRatePerHost(Integer userCount, Integer upgUserCount, String hostName) {
        UserRateItem userRateItem = getUserDiscount(userCount, hostName);
        userRateItem.setUserRateUpg(getUserRatePerHOST(upgUserCount, hostName));
        return userRateItem;
    }

    public Double getUserRatePerHOST(Integer userCount, String hostName) {
        hostName = hostName != null ? hostName : HOST_LIVE;
        BigDecimal userRatePerHOST = globalAuthJdbcSpringManager.getUserRatePerHOST(userCount, hostName);
        return userRatePerHOST.doubleValue();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public UserRateItem getUserDiscount(Integer userCount, String hostName) {
        hostName = hostName != null ? hostName : HOST_LIVE;
        UserRateItem userDiscountPerHOST = globalAuthJdbcSpringManager.getUserDiscountPerHOST(hostName, userCount);
        userDiscountPerHOST.setUserRate(getUserRatePerHOST(userCount, hostName));
        Integer maxPayableUserCount = globalAuthJdbcSpringManager.getMaxPayableUserCount(hostName);
        userDiscountPerHOST.setMaxPayableUserCount(maxPayableUserCount);
        return userDiscountPerHOST;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public UserRateItem getUserRateAndUpgUserRatePerHostT(String hostName, String pricingPackageNAME, String supportPackageNAME, String upgPricingPackageNAME, String upgSupportPackageNAME) {
        UserRateItem userRateItem = getUserT(hostName, pricingPackageNAME, supportPackageNAME, null, false);
        userRateItem.setUserRateUpg(getUserRatePerHOST(hostName, upgPricingPackageNAME));
        Double supportPricePerHOSTPerPackageUpg = getSupportPackagePricePerHostPerPackage(hostName, upgSupportPackageNAME);
        userRateItem.setSupportPackagePriceUpg(supportPricePerHOSTPerPackageUpg);
        userRateItem.setPricingPackageNAMEUpg(upgPricingPackageNAME);
        return userRateItem;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public UserRateItem getUserT(String hostName, String pricingPackageNAME, String supportPackageNAME, String modules, boolean pricingType) {
        hostName = hostName != null ? hostName : HOST_LIVE;
        UserRateItem t = globalAuthJdbcSpringManager.getUserCountsViaPrice(hostName, pricingPackageNAME, modules, pricingType);
        t.setMaxPayableUserCount(t.getUserCountMaxTwentyMonth());
        double price = 0d;
        if (!pricingType) {
            t.setDiscountOneMonth(0d);
            t.setDiscountThreeMonth(0d);
            t.setDiscountSixMonth(0d);
            t.setDiscountTwentyMonth(0d);
            t.setUserRate(t.getPricePerPackage());
        } else {
            for (int i = 0; i < t.getCount(); i++) {
                switch (i) {
                    case 0 -> price = 9.99;
                    case 1 -> price += 5.59;
                    case 2 -> price += 2.79;
                    case 3 -> price += 1.59;
                }
            }
            t.setUserRate(price);
        }
        Double supportPricePerHOSTPerPackage = getSupportPackagePricePerHostPerPackage(hostName, supportPackageNAME);
        t.setSupportPackagePrice(supportPricePerHOSTPerPackage);
        return t;
    }

    public Double getSupportPackagePricePerHostPerPackage(String hostName, String supportPackageNAME) {
        BigDecimal supportPricePerHOSTPerPackage = globalAuthJdbcSpringManager.getSupportPricePerPackage(hostName, supportPackageNAME);
        return supportPricePerHOSTPerPackage.doubleValue();
    }

    public Double getUserRatePerHOST(String hostName, String pricingPackageName) {
        hostName = hostName != null ? hostName : HOST_LIVE;
        BigDecimal userRatePerHOST = globalAuthJdbcSpringManager.getUserRatePerPackage(hostName, pricingPackageName);
        return userRatePerHOST.doubleValue();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ListResult<UsagePlanItem> getUsagePlans(ListingFilterParameter fp) {
        List<EdsUsagePlan> usagePlans = usagePlanManager.getUsagePlansbyCompany(fp);
        boolean paypalStatus = false;

        for (EdsUsagePlan usagePlan : usagePlans) {
            if (usagePlan.getPaid() && usagePlan.isPaypalStatus() != null && usagePlan.isPaypalStatus()) {
                paypalStatus = true;
            }
        }
        final int totalCount = usagePlans.size();

        if (fp.getLimit() != 0) {
            usagePlans = ListUtils.getSublist(usagePlans, fp.getStart(), fp.getLimit());
        }
        final ArrayList<UsagePlanItem> result = new ArrayList<>();
        for (EdsUsagePlan usagePlan : usagePlans) {
            try {
                UsagePlanItem tmp = new UsagePlanItem();
                tmp.setObjectID(usagePlan.getObjectID());
                tmp.setService(usagePlan.getServiceType() != null
                        ? referenceWfmMessageSource.localize(usagePlan.getServiceType().getCode(), usagePlan.getServiceType().getName())
                        : "");
                tmp.setStartDate(usagePlan.getStartDate());
                tmp.setEndDate(usagePlan.getEndDate());
                if (usagePlan.getEndDate() != null && usagePlan.getEndDate().before(new Date())) {
                    EdsReference expiredStatus = referenceManager.findReference(EdsUsagePlan._PAYMENT_STATUS, EdsUsagePlan.EXPIRED);
                    if (expiredStatus != null) {
                        tmp.setStatus(referenceWfmMessageSource.localize(expiredStatus.getCode(), expiredStatus.getName()));
                    } else {
                        tmp.setStatus("");
                    }
                } else {
                    tmp.setStatus(usagePlan.getStatus() != null
                            ? referenceWfmMessageSource.localize(usagePlan.getStatus().getCode(), usagePlan.getStatus().getName())
                            : "");
                }
                tmp.setUserCount(Optional.ofNullable(usagePlan.getUsers()).orElse(0));
                tmp.setEssUserCount(Optional.ofNullable(usagePlan.getEssUsers()).orElse(0));
                tmp.setNonAccessUserCount(Optional.ofNullable(usagePlan.getNoAccessUsers()).orElse(0));
                tmp.setStorageCount(Optional.ofNullable(usagePlan.getStorage()).orElse(0));
                tmp.setDiscount(usagePlan.getDiscount());
                tmp.setTax(usagePlan.getTaxt());
                tmp.setTotalAmount(usagePlan.getTotalAmount());
                tmp.setPaid(usagePlan.getPaid());
                UsagePlanItem item = getParametr(usagePlan);
                tmp.setFree(item.isFree());
                tmp.setUsageMonth(item.getUsageMonth());
                tmp.setCostDown(item.getCostDown());
                tmp.setPeriodType(item.getPeriodType());
                tmp.setPaypalStatus(paypalStatus);
                tmp.setCategoryREAL(usagePlan.getCategoryCODE());
                tmp.setAccountsModule(Optional.ofNullable(usagePlan.getAccountsModule()).orElse(false));
                tmp.setProjectModule(Optional.ofNullable(usagePlan.getProjectModule()).orElse(false));
                tmp.setHumansModule(Optional.ofNullable(usagePlan.getHumanModule()).orElse(false));
                tmp.setSalesModule(Optional.ofNullable(usagePlan.getSalesModule()).orElse(false));
                tmp.setPayrollModule(Optional.ofNullable(usagePlan.getPayrollModule()).orElse(false));
                result.add(tmp);
            } catch (Exception e) {
                log.error("", e);
            }
        }
        return new ListResult<>(result, totalCount);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public UsagePlanItem getParametr(EdsUsagePlan usagePlan) {
        UsagePlanItem item = new UsagePlanItem();
        if (usagePlan.getPeriodType().equals(referenceManager.findReference(EdsUsagePlan._PERIOD_TYPE, EdsUsagePlan.FREE_TRIAL))) {
            item.setFree(true);
            item.setUsageMonth(0);
            item.setCostDown(0);
            item.setPeriodType("Free Trial");
        } else {
            item.setFree(false);
            if (usagePlan.getPeriodType().equals(referenceManager.findReference(EdsUsagePlan._PERIOD_TYPE, EdsUsagePlan.ONE_MONTH_0))) {
                item.setUsageMonth(1);
                item.setCostDown(0);
                item.setPeriodType(" 1 Month");
            } else if (usagePlan.getPeriodType().equals(referenceManager.findReference(EdsUsagePlan._PERIOD_TYPE, EdsUsagePlan.THREE_MONTH_15))) {
                item.setUsageMonth(3);
                item.setCostDown(10);
                item.setPeriodType(" 3 Month");
            } else if (usagePlan.getPeriodType().equals(referenceManager.findReference(EdsUsagePlan._PERIOD_TYPE, EdsUsagePlan.SIX_MONTH_20))) {
                item.setUsageMonth(6);
                item.setCostDown(10);
                item.setPeriodType(" 6 Month");
            } else if (usagePlan.getPeriodType().equals(referenceManager.findReference(EdsUsagePlan._PERIOD_TYPE, EdsUsagePlan.TWELVE_MONTH_TWENTY_30))) {
                item.setUsageMonth(12);
                item.setCostDown(20);
                item.setPeriodType(" 1 Year");
            } else if (usagePlan.getPeriodType().equals(referenceManager.findReference(EdsUsagePlan._PERIOD_TYPE, EdsUsagePlan.TWO_YEARS_45))) {
                item.setUsageMonth(24);
                item.setCostDown(45);
                item.setPeriodType(" 2 Years");
            }
        }

        return item;
    }

    public void updateCompanyLastUsagePlan(UsagePlanItem item) {
        EdsUsagePlan usagePlan = usagePlanManager.get(item.getObjectID());
        Calendar calendar = new GregorianCalendar();
        if (usagePlan != null) {
            if (item.getExpireDate() != null) {
                calendar.setTime(item.getExpireDate());
                usagePlan.setEndDate(item.getExpireDate());
            }

            if (item.getUserCount() != null) {
                usagePlan.setUsers(item.getUserCount());
            }

            usagePlan.setStatus(referenceManager.findReference(EdsUsagePlan._PAYMENT_STATUS, EdsUsagePlan.ACTIVE));
            usagePlanManager.update(usagePlan);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public UsagePlanItem getCompanyLastUsagePlan(Integer companyID) {
        UsagePlanItem usagePlanItem = new UsagePlanItem();
        EdsUsagePlan usagePlan = usagePlanManager.getCurrentUsagePlan(companyManager.get(companyID));

        if (usagePlan == null) {
            usagePlan = usagePlanManager.getLastUsagePlan(companyID);
        }

        if (usagePlan != null) {
            usagePlanItem.setExpireDate(usagePlan.getEndDate());
            usagePlanItem.setStartDate(usagePlan.getStartDate());
            usagePlanItem.setUserCount(usagePlan.getUsers());
            usagePlanItem.setCompName(usagePlan.getCompany().getName());
            usagePlanItem.setObjectID(usagePlan.getObjectID());
            usagePlanItem.setUnique_guid(usagePlan.getUnique_guid());
        }
        return usagePlanItem;
    }

    @Override
    public UsagePlanItem getUsagePlan(Integer int_usagePlanID) {
        List<EdsUsagePlan> usagePlans = usagePlanManager.getUsagePlansbyCompany(new ListingFilterParameter());
        boolean paypalStatus = false;
        for (EdsUsagePlan usagePlan : usagePlans) {
            if (usagePlan.getPaid() && usagePlan.isPaypalStatus() != null && usagePlan.isPaypalStatus()) {
                paypalStatus = true;
            }
        }
        EdsUsagePlan usagePlan = usagePlanManager.getCompanyUsagePlan(int_usagePlanID);
        if (usagePlan != null) {
            UsagePlanItem tmp = new UsagePlanItem();
            tmp.setObjectID(usagePlan.getObjectID());
            tmp.setService(usagePlan.getServiceType() != null
                    ? referenceWfmMessageSource.localize(usagePlan.getServiceType().getCode(), usagePlan.getServiceType().getName())
                    : "");
            tmp.setStartDate(usagePlan.getStartDate());
            tmp.setEndDate(usagePlan.getEndDate());
            tmp.setStatus(usagePlan.getStatus() != null
                    ? referenceWfmMessageSource.localize(usagePlan.getStatus().getCode(), usagePlan.getStatus().getName())
                    : "");
            tmp.setUserCount(usagePlan.getUsers() != null ? usagePlan.getUsers() : Integer.valueOf(0));
            tmp.setStorageCount(usagePlan.getStorage() != null ? usagePlan.getStorage() : Integer.valueOf(0));
            tmp.setDiscount(usagePlan.getDiscount());
            tmp.setTax(usagePlan.getTaxt());
            tmp.setTotalAmount(usagePlan.getTotalAmount());
            tmp.setPaid(usagePlan.getPaid());
            UsagePlanItem item = getParametr(usagePlan);
            tmp.setFree(item.isFree());
            tmp.setUsageMonth(item.getUsageMonth());
            tmp.setCostDown(item.getCostDown());
            tmp.setPeriodType(item.getPeriodType());
            tmp.setPaypalStatus(paypalStatus);
            return tmp;
        }
        return null;
    }


    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public UsagePlanItem getUsagePlanItem(Integer id) {//SUBSCRIPTION_ADD
        EdsUsagePlan usagePlan = usagePlanManager.get(id);
        return getUsagePlanItem(usagePlan);
    }

    /*
      Send email for statistics when user is visited to the pricing order confirm page
     */
    @Override
    public void sendEmailUserVisitToPage(KpiPaymentRequestObject kpiRequestObject) {
        try {
            ByteArrayOutputStream pdfStream = kpiPaymentViewPdfHandler.getPDFStream(kpiRequestObject);
            if (pdfStream != null) {
                messageManager.sendEmailUserVisitToPage(pdfStream);
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Override
    @Transactional
    public Boolean enableOrDisableFreeTrialModule(String module, boolean enable) {
        EdsUser user = usagePlanManager.getUser();
        EdsUsagePlan usagePlan = usagePlanManager.getCurrentUsagePlan(user.getCompany());
        if (usagePlan!=null && usagePlan.getPeriodType().equals(referenceManager.findReference(EdsUsagePlan._PERIOD_TYPE, EdsUsagePlan.FREE_TRIAL))) {
            if("accounts".equalsIgnoreCase(module)) {
                usagePlan.setAccountsModule(enable);
            } else if("sales".equalsIgnoreCase(module)) {
                usagePlan.setSalesModule(enable);
            } else if("humans".equalsIgnoreCase(module)) {
                usagePlan.setHumanModule(enable);
            } else if("projects".equalsIgnoreCase(module)) {
                usagePlan.setProjectModule(enable);
            } else if("payroll".equalsIgnoreCase(module)) {
                usagePlan.setPayrollModule(enable);
            }
            usagePlanManager.update(usagePlan);
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }

    @Override
    public String getRedirectUrlForRevolut(UsagePlanItem usagePlanItem, UsagePlanPrice prices, String currency, String description) {

        InvoiceDto dto = new InvoiceDto();
        dto.setBankAccount(new IdDTO(47));
        dto.setCurrencyCode(currency);
        ItemDto itemDto = new ItemDto();
        itemDto.setName(companyManager.getCompany(SecurityContext.getCompanyID()).getName());
        dto.setCustomer(itemDto);
        dto.setDate(new Date());
        dto.setDueDate(usagePlanItem.getExpireDate());
        dto.setDueDateType("DUE_DATE");
        ArrayList<CustomFieldRequest> cfs = new ArrayList<>();
        CustomFieldRequest cf = new CustomFieldRequest();
        cf.setAlias("Module");
        cf.setValue(usagePlanItem.getModules());
        cfs.add(cf);
        dto.setCustomFields(cfs);
        dto.setReference(String.valueOf(SecurityContext.getCompanyID()));

        String period = "";
        Integer month = 1;
        Double discount = 0d;
        if (PP_BY_YEAR.equals(usagePlanItem.getPeriodConstant())) {
            period = "Annual";
            month = 12;
            discount = 20d;
        } else if (PP_BY_HALF_YEAR.equals(usagePlanItem.getPeriodConstant())) {
            period = "Semi-Annualy";
            month = 6;
            discount = 15d;
        } else if (PP_BY_QUARTER.equals(usagePlanItem.getPeriodConstant())) {
            period = "Quarterly";
            month = 3;
            discount = 10d;
        } else {
            period = "Monthly";
        }
        //Item Table starts
        List<LineItemDto> lineItemDtoList = new ArrayList<>();
        LineItemDto row = new LineItemDto();
        ItemDto fullUsers = new ItemDto(6, "Renewal Subscription", "PD0006");
        row.setProduct(fullUsers);
        row.setQuantity(BigDecimal.valueOf(usagePlanItem.getUserCount()));
        row.setDescription(period + " kpi.com subscription");
        row.setDiscount(BigDecimal.valueOf(discount));
        row.setUnitPrice(BigDecimal.valueOf(prices.getFullUsersDiscountedPrice() * month));
        row.setTaxItem(null);
        lineItemDtoList.add(row);

        if (usagePlanItem.getEssUserCount() > 0) {
            LineItemDto row2 = new LineItemDto();
            ItemDto nonUsers = new ItemDto(10, "Renewal Kpi.com subscription (ESS)", "PD0010");
            row2.setProduct(nonUsers);
            row2.setQuantity(BigDecimal.valueOf(usagePlanItem.getEssUserCount()));
            row2.setDescription(period + " kpi.com subscription");
            row2.setDiscount(BigDecimal.valueOf(0));
            row2.setUnitPrice(BigDecimal.valueOf(prices.getEssUsersPrice()));
            row2.setTaxItem(null);
            lineItemDtoList.add(row2);
        }

        if (usagePlanItem.getNonAccessUserCount() > 0) {
            LineItemDto row1 = new LineItemDto();
            ItemDto essUsers = new ItemDto(11, "Renewal Kpi.com subscription (Non-User)", "PD0011");
            row1.setProduct(essUsers);
            row1.setQuantity(BigDecimal.valueOf(usagePlanItem.getNonAccessUserCount()));
            row1.setDescription(period + " kpi.com subscription");
            row1.setDiscount(BigDecimal.valueOf(0));
            row1.setUnitPrice(BigDecimal.valueOf(prices.getNonUsersPrice()));
            row1.setTaxItem(null);
            lineItemDtoList.add(row1);
        }

        double addOnAmounts = 0;

        if (usagePlanItem.getAddonOnlineTraining() != null) {
            LineItemDto row2 = new LineItemDto();
            ItemDto traning = new ItemDto(47, "Online Training", "PD0014");
            row2.setProduct(traning);
            row2.setQuantity(BigDecimal.valueOf(1));
            row2.setUnitPrice(BigDecimal.valueOf(usagePlanItem.getAddonOnlineTraining()));
            lineItemDtoList.add(row2);
            addOnAmounts += usagePlanItem.getAddonOnlineTraining();
        }

        if (usagePlanItem.getAddonInitialSetup() != null) {
            LineItemDto row2 = new LineItemDto();
            ItemDto initial = new ItemDto(3, "Initial Set Up Package", "PD0003");
            row2.setProduct(initial);
            row2.setQuantity(BigDecimal.valueOf(1));
            row2.setUnitPrice(BigDecimal.valueOf(usagePlanItem.getAddonInitialSetup()));
            lineItemDtoList.add(row2);
            addOnAmounts += usagePlanItem.getAddonInitialSetup();

        }

        if (usagePlanItem.getAddonCustomPDFTemplate() != null) {
            LineItemDto row2 = new LineItemDto();
            ItemDto initial = new ItemDto(7, "Custom PDF template", "PD0007");
            row2.setProduct(initial);
            row2.setQuantity(BigDecimal.valueOf(1));
            row2.setUnitPrice(BigDecimal.valueOf(usagePlanItem.getAddonCustomPDFTemplate()));
            lineItemDtoList.add(row2);
            addOnAmounts += usagePlanItem.getAddonCustomPDFTemplate();
        }


        dto.setItems(lineItemDtoList);
        dto.setTaxCalcType("NO_TAX");
        //Item Table ends


        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();


        int result = (int) ((usagePlanItem.getTotalAmount() + addOnAmounts) * 100);
        RevolutResponseDto order = revolutService.createOrder(result, currency, true, description);

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE);
        headers.set("x-auth", "FREE$65159$561A03392D4B9B40");
        headers.set("accessToken", "7085a4c7-b736-46f4-ac9a-256c1c5e4559");

        HttpEntity<InvoiceDto> httpEntity = new HttpEntity<>(dto, headers);
        ResponseEntity<SaveResult> stringResponseEntity = restTemplate.postForEntity("http://localhost:8080/services/api/v3/sales/invoice", httpEntity, SaveResult.class);
        invoiceManager.insertRevolutUrl(stringResponseEntity.getBody().getId(), order.getCheckout_url(), order.getId(), 65159);

        return order.getCheckout_url();
    }


    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public UsagePlanItem getCurrentUsagePlan() {
        EdsUser user = usagePlanManager.getUser();
        EdsUsagePlan usagePlan = usagePlanManager.getCurrentUsagePlan(user.getCompany());
        return getUsagePlanItem(usagePlan);
    }

    private UsagePlanItem getUsagePlanItem(EdsUsagePlan usagePlan) {
        String period = "";
        EdsUser user = usagePlanManager.getUser();
        UsagePlanItem cupViewItem = new UsagePlanItem();
        cupViewItem.setCompanyUk(getCompanyIsUK());
        if (usagePlan != null) {
            cupViewItem.setCompanyID(usagePlan.getCompany().getObjectID());
            if (usagePlan.getSubscriptionHistory() != null && !usagePlan.getSubscriptionHistory().getPaid()) {
                final EdsSubscriptionHistory subscriptionHistory = usagePlan.getSubscriptionHistory();

                if (subscriptionHistory != null) {
                    cupViewItem.setUpgSubHisId(subscriptionHistory.getObjectID());
                    cupViewItem.setUpgPayed(subscriptionHistory.getPaid());
                    cupViewItem.setUpgUserCount(subscriptionHistory.getUsers());
                    cupViewItem.setUpgStorageCount(subscriptionHistory.getStorage());
                    cupViewItem.setShowUpgBt(true);//@TODO previously it was set to false
                    cupViewItem.setUpgAccountsModule(Optional.ofNullable(subscriptionHistory.getAccountsModule()).orElse(false));
                    cupViewItem.setUpgSalesModule(Optional.ofNullable(subscriptionHistory.getSalesModule()).orElse(false));
                    cupViewItem.setUpgProjectModule(Optional.ofNullable(subscriptionHistory.getProjectModule()).orElse(false));
                    cupViewItem.setUpgHumansModule(Optional.ofNullable(subscriptionHistory.getHumanModule()).orElse(false));
                    cupViewItem.setUpgPayrollModule(Optional.ofNullable(subscriptionHistory.getPayrollModule()).orElse(false));

                    int dayCount = getDayCount(usagePlan.getStartDate(), usagePlan.getEndDate());
                    int upgDayCount = this.getDayCount(new Date(), usagePlan.getEndDate());
                    cupViewItem.setDayCount(dayCount);
                    cupViewItem.setUpgDayCount(upgDayCount);
                    cupViewItem.setUpgCategoryREAL(subscriptionHistory.getCategoryCODE());
                    if (subscriptionHistory.getSupportPackageNAME() != null && !"".equals(subscriptionHistory.getSupportPackageNAME())) {
                        cupViewItem.setUpgSupportPackageNAME(subscriptionHistory.getSupportPackageNAME());
                        Double upgSupportPackagePricePerHostPerPackage = commonServiceLocal.getSupportPackagePricePerHostPerPackage(EdsContextParams.getHostname(), subscriptionHistory.getSupportPackageNAME());
                        cupViewItem.setUpgSupportPackagePrice(upgSupportPackagePricePerHostPerPackage.floatValue());
                    }
                }
            } else {
                cupViewItem.setShowUpgBt(true);
                cupViewItem.setUpgPayed(true);
            }
            cupViewItem.setObjectID(usagePlan.getObjectID());

            //Set GUID if empty
            if (StringUtils.isBlank(usagePlan.getUnique_guid())) {
                usagePlan.setUnique_guid(UUID.randomUUID().toString());
                usagePlanManager.update(usagePlan);
            }
            cupViewItem.setUnique_guid(usagePlan.getUnique_guid());

            if (usagePlan.getServiceType() != null) {
                cupViewItem.setService(usagePlan.getServiceType().getName() != null
                        ? usagePlan.getServiceType().getName()
                        : "");
            }
            if (usagePlan.getStartDate() != null && usagePlan.getEndDate() != null) {
                period = ServerUtils.shortDateFormat(user.getUserDate(usagePlan.getStartDate()), user) + " - " + ServerUtils.shortDateFormat(user.getUserDate(usagePlan.getEndDate()), user);
                cupViewItem.setStartDate(usagePlan.getStartDate());
                cupViewItem.setEndDate(usagePlan.getEndDate());
            }
            cupViewItem.setPeriod(period);
            cupViewItem.setStatus(usagePlan.getStatus() != null ? usagePlan.getStatus().getName() : "");
            cupViewItem.setUserCount(Optional.ofNullable(usagePlan.getUsers()).orElse(0));
            cupViewItem.setEssUserCount(Optional.ofNullable(usagePlan.getEssUsers()).orElse(0));
            cupViewItem.setNonAccessUserCount(Optional.ofNullable(usagePlan.getNoAccessUsers()).orElse(0));
            cupViewItem.setStorageCount(Optional.ofNullable(usagePlan.getStorage()).orElse(0));
            cupViewItem.setDiscount(usagePlan.getDiscount());
            cupViewItem.setTax(usagePlan.getTaxt());
            cupViewItem.setTotalAmount(usagePlan.getTotalAmount());
//            cupViewItem.setTotalAmount( ServerUtils.round(usagePlan.getTotalAmount(), 2));

            UsagePlanItem item = getParametr(usagePlan);
            cupViewItem.setFree(item.isFree());
            cupViewItem.setUsageMonth(item.getUsageMonth());
            cupViewItem.setCostDown(item.getCostDown());
            cupViewItem.setPeriodType(item.getPeriodType());
            cupViewItem.setCompanyUk(Optional.ofNullable(usagePlan.isUKCompany()).orElse(false));
            cupViewItem.setModules(usagePlan.getModules());
            cupViewItem.setAccountsModule(Optional.ofNullable(usagePlan.getAccountsModule()).orElse(false));
            cupViewItem.setHumansModule(Optional.ofNullable(usagePlan.getHumanModule()).orElse(false));
            cupViewItem.setSalesModule(Optional.ofNullable(usagePlan.getSalesModule()).orElse(false));
            cupViewItem.setProjectModule(Optional.ofNullable(usagePlan.getProjectModule()).orElse(false));
            cupViewItem.setPayrollModule(Optional.ofNullable(usagePlan.getPayrollModule()).orElse(false));
            cupViewItem.setAddonOnlineTraining(usagePlan.getAddonOnlineTraining());
            cupViewItem.setAddonInitialSetup(usagePlan.getAddonInitialSetup());
            cupViewItem.setAddonExtraStorage(usagePlan.getAddonExtraStorage());
            cupViewItem.setAddonCustomPDFTemplate(usagePlan.getAddonCustomPDFTemplate());
            cupViewItem.setAddonDedicatedDeveloper(usagePlan.getAddonDedicatedDeveloper());
            cupViewItem.setAddonDedicatedAccountManager(usagePlan.getAddonDedicatedAccountManager());

            final EdsReference serviceType = referenceManager.findReference(EdsUsagePlan._SERVICE_TYPE, EdsUsagePlan.ALL_SERVICE);

            if (serviceType != null && serviceType.equals(usagePlan.getServiceType())) {
                cupViewItem.setAllService(true);
            } else {
                cupViewItem.setAllService(false);
            }
            cupViewItem.setPaid(usagePlan.getPaid());
            cupViewItem.setCurrSub(true);
            if (usagePlan.isCurrencyGBP() != null && usagePlan.isCurrencyGBP()) {
                cupViewItem.setCurrencyGBP(usagePlan.isCurrencyGBP());
            }
            cupViewItem.setMobile(usagePlan.isMobile());
            cupViewItem.setProjectCount(usagePlan.getProjectCount());
            cupViewItem.setTaskCount(usagePlan.getTaskCount());
            cupViewItem.setUserRate(usagePlan.getUserRate());
            cupViewItem.setCategoryREAL(usagePlan.getCategoryCODE());
            if (usagePlan.getSupportPackageNAME() != null && !"".equals(usagePlan.getSupportPackageNAME())) {
                cupViewItem.setSupportPackageNAME(usagePlan.getSupportPackageNAME());
                Double supportPackagePricePerHostPerPackage = commonServiceLocal.getSupportPackagePricePerHostPerPackage(EdsContextParams.getHostname(), usagePlan.getSupportPackageNAME());
                cupViewItem.setSupportPackagePrice(supportPackagePricePerHostPerPackage.floatValue());
            }
        } else {
            EdsUsagePlan lastUsagePlan = usagePlanManager.getLastUsagePlan(user.getCompany().getObjectID());
            if (lastUsagePlan != null) {
                UsagePlanItem lastUsagePlanItem = getParametr(lastUsagePlan);
                cupViewItem.setPeriodType(lastUsagePlanItem.getPeriodType());
                cupViewItem.setUnique_guid(lastUsagePlan.getUnique_guid());

                long activeFullUsersCount = employeeManager.getEmployeesCountByCompany(true, true, true);
                long activeEssUsersCount = employeeManager.getEssUsersCount(true);
                long activeNonAccessUsersCount = employeeManager.getNoAccessEmployeesCountByCompany();

                cupViewItem.setUserCount(lastUsagePlan.getUsers() != null ? lastUsagePlan.getUsers() : 0);
                cupViewItem.setEssUserCount(lastUsagePlan.getEssUsers() != null ? lastUsagePlan.getEssUsers() : 0);
                cupViewItem.setNonAccessUserCount(lastUsagePlan.getNoAccessUsers() != null ? lastUsagePlan.getNoAccessUsers() : 0);

                cupViewItem.setActiveUserCount((int) activeFullUsersCount);
                cupViewItem.setActiveEssUserCount((int) activeEssUsersCount);
                cupViewItem.setActiveNonAccessUserCount((int) activeNonAccessUsersCount);

                cupViewItem.setModules(lastUsagePlan.getModules());
                cupViewItem.setAccountsModule(Optional.ofNullable(lastUsagePlan.getAccountsModule()).orElse(false));
                cupViewItem.setHumansModule(Optional.ofNullable(lastUsagePlan.getHumanModule()).orElse(false));
                cupViewItem.setSalesModule(Optional.ofNullable(lastUsagePlan.getSalesModule()).orElse(false));
                cupViewItem.setProjectModule(Optional.ofNullable(lastUsagePlan.getProjectModule()).orElse(false));
                cupViewItem.setPayrollModule(Optional.ofNullable(lastUsagePlan.getPayrollModule()).orElse(false));
            } else {
                cupViewItem.setUserCount(0);
                cupViewItem.setEssUserCount(0);
                cupViewItem.setNonAccessUserCount(0);

                cupViewItem.setActiveUserCount(0);
                cupViewItem.setActiveEssUserCount(0);
                cupViewItem.setActiveNonAccessUserCount(0);
            }
            cupViewItem.setCurrSub(false);
            cupViewItem.setService("");
            cupViewItem.setPeriod("");
            cupViewItem.setStatus("");
//            cupViewItem.setUserCount(0);
            cupViewItem.setStorageCount(0);
            cupViewItem.setDiscount(0);
            cupViewItem.setTax(0);
            cupViewItem.setTotalAmount(0);
            cupViewItem.setPaid(false);
            cupViewItem.setFree(true);
            cupViewItem.setUsageMonth(0);
            cupViewItem.setCostDown(0);
            cupViewItem.setUpgPayed(true);
            cupViewItem.setProjectCount(0);
            cupViewItem.setTaskCount(0);
            cupViewItem.setUserRate(0.0f);
        }
        Long companyUsers = employeeManager.getEmployeesCountByCompany(false, false, false);
        Long currentRegisteredUsersCount = companyUsers != null ? companyUsers : 0;
        cupViewItem.setRegisteredUsersCount(currentRegisteredUsersCount.intValue());
        return cupViewItem;
    }

    @Override
    public UsagePlanItem saveUsagePlan(UsagePlanItem usagePlan) {
        final EdsUser user = usagePlanManager.getUser();

        if (usagePlan == null || user == null) {
            return null;
        }
        usagePlan.setCompanyID(user.getCompany().getObjectID());
        return this.usagePlanSaveAndGet(usagePlan);
    }

    private Customer createStripeCustomer(String stripeCheckoutToken) {
        try {
            //Get STRIPE secret key from database
            //Stripe.apiKey = "sk_test_LjJzzD0OQE9RydPRgocD5oQf";
            Stripe.apiKey = EdsContextParams.getStripeSecretKey();

            // Create a Customer:
            Map<String, Object> customerParams = new HashMap<String, Object>();
            if (StringUtils.isNotBlank(usagePlanManager.getUser().getEmail())) {
                customerParams.put("email", usagePlanManager.getUser().getEmail());
            }
            HashMap<String, Object> customerMetadata = new HashMap<>();
            customerMetadata.put("company_id", SecurityContext.getCompanyID());
            customerParams.put("source", stripeCheckoutToken);
            customerParams.put("metadata", customerMetadata);
            return Customer.create(customerParams);
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    private Product createStripeProduct(UsagePlanItem us, int moduleCount, HashMap<String, Object> genericMetadata) {
        try {
            //Get STRIPE secret key from database
            //Stripe.apiKey = "sk_test_LjJzzD0OQE9RydPRgocD5oQf";
            Stripe.apiKey = EdsContextParams.getStripeSecretKey();

            Map<String, Object> kpiProductParams = new HashMap<String, Object>();
            kpiProductParams.put("name", "Full Users " + us.getUserCount() + (us.getEssUserCount() != null && us.getEssUserCount() > 0 ? ", ESS Users " + us.getEssUserCount() : "")
                    + (us.getNonAccessUserCount() != null && us.getNonAccessUserCount() > 0 ? ", Non Users " + us.getNonAccessUserCount() : "")
                    + " / " + moduleCount + " Apps");
            kpiProductParams.put("type", "service");
            kpiProductParams.put("metadata", genericMetadata);
            return Product.create(kpiProductParams);
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    private Plan createStripePlan(UsagePlanItem us, UsagePlanPrice usagePlanPrice, Product kpiProduct, String currencyValue, HashMap<String, Object> genericMetadata) {
        try {
            //Get STRIPE secret key from database
            //Stripe.apiKey = "sk_test_LjJzzD0OQE9RydPRgocD5oQf";
            Stripe.apiKey = EdsContextParams.getStripeSecretKey();

            Map<String, Object> kpiPlanParams = new HashMap<String, Object>();
            kpiPlanParams.put("nickname", us.getUsageMonth() < 12 ? "Monthly KPI.com Subscription" : "Annual KPI.com Subscription");
            kpiPlanParams.put("product", kpiProduct.getId());
//            kpiPlanParams.put("amount", Double.valueOf((usagePlanPrice.getTotalAmount() + usagePlanPrice.getPrevTotalAmount()) * 100d).intValue());
            kpiPlanParams.put("amount", Double.valueOf(usagePlanPrice.getTotalSubscription() * 100d).intValue());
            kpiPlanParams.put("currency", currencyValue);
            kpiPlanParams.put("interval", us.getUsageMonth() < 12 ? "month" : "year");
            kpiPlanParams.put("usage_type", "licensed");
            kpiPlanParams.put("metadata", genericMetadata);
            return Plan.create(kpiPlanParams);
        } catch (Exception e) {
            log.error("", e);
            return null;
        }
    }

    @Transactional
    public Boolean chargeForSubscriptionPaymentWithStripeNew(String subscriptionOperation, UsagePlanItem us, Integer subscriptionHistoryID,
                                                             String stripeCheckoutToken, String currencyValue, double totalAddOns, UsagePlanPrice usagePlanPriceDataToServer) {

        String description = "KPI.com";
        //StringBuilder msgBody = new StringBuilder("<html><body>");
        //msgBody.append("<p>description=").append(description).append("</p>");
        //msgBody.append("<p>amount=").append(us.getTotalAmount()).append("</p>");
        //msgBody.append("<p>currency=").append(currencyValue).append("</p>");

        //StringBuilder msgSubject = new StringBuilder("Stripe Payment Notification:");
        UsagePlanPrice usagePlanPrice = null;
        boolean sendOneTimePaymentNotification = false;
        boolean result = false;

        UsagePlanItem currentUsagePlan = getCurrentUsagePlan();
        //Charge with STRIPE
        try {
            String unique_guid = null;
            if (StringUtils.isBlank(us.getUnique_guid())) {
                unique_guid = UUID.randomUUID().toString();
                us.setUnique_guid(unique_guid);
            }
            //Get STRIPE secret key from database
            //Stripe.apiKey = "sk_test_LjJzzD0OQE9RydPRgocD5oQf";
            Stripe.apiKey = EdsContextParams.getStripeSecretKey();

            //if (StringUtils.isNotBlank(usagePlanManager.getUser().getFullName())) {
            //    msgSubject.append(usagePlanManager.getUser().getFullName());
            //}
            int moduleCount = 0;
            if (us.isAccountsModule()) moduleCount++;
            if (us.isPayrollModule()) moduleCount++;
            if (us.isProjectModule()) moduleCount++;
            if (us.isHumansModule()) moduleCount++;
            if (us.isSalesModule()) moduleCount++;


            usagePlanPrice = PricingUtils.getTotalPrice(us.getUserCount(), us.getEssUserCount(), moduleCount,
                    us.getNonAccessUserCount(), totalAddOns,
                    currentUsagePlan, us.getUsageMonth() == 1, null);
//            usagePlanPrice = usagePlanPriceDataToServer;

            //Create Stripe Customer
            Customer customer = createStripeCustomer(stripeCheckoutToken);

            if (customer != null) {
                Date paymentDate = new DateNonConvertable(new Date()).getNonConvertedDate();
                double onetimeAmount = usagePlanPrice.getAddonPrice();
                //Create Generic metadata which we will attach to all Stripe calls
                HashMap<String, Object> genericMetadata = new HashMap<>();
                genericMetadata.put("company_id", SecurityContext.getCompanyID());
                genericMetadata.put("usageplan_id", us.getUnique_guid());
                genericMetadata.put("user_id", employeeManager.getUser().getObjectID());
                genericMetadata.put("user_name", employeeManager.getUser().getName());
                genericMetadata.put("user_email", employeeManager.getUser().getEmail());

                //Create Stripe Subscription
                Subscription stripeSubscription = null;

                if (usagePlanPrice.getTotalAmount() > 0d) {

                    //Create Stripe Product
                    Product kpiProduct = createStripeProduct(us, moduleCount, genericMetadata);
                    //Create Stripe Plan
                    Plan kpiPlan = createStripePlan(us, usagePlanPrice, kpiProduct, currencyValue, genericMetadata);

                    //Create Stripe Subscription
                    Map<String, Object> kpiPlanItem = new HashMap<String, Object>();
                    kpiPlanItem.put("plan", kpiPlan.getId());
                    kpiPlanItem.put("quantity", 1);

                    Map<String, Object> plans = new HashMap<String, Object>();
                    plans.put("0", kpiPlanItem);


                    Map<String, Object> stripeSubscriptionParams = new HashMap<String, Object>();
                    EdsSubscriptionPayment payment = subscriptionPaymentManager.getByUsageplanUID(us.getUnique_guid());
                    if (payment != null && currentUsagePlan.isPaid()/*payment != null && StringUtils.isNotBlank(payment.getApiSubscrId())*/ && SUBSCRIPTION_UPG.equals(subscriptionOperation) /*&& !payment.getApiSubscrId().equalsIgnoreCase(stripeSubscription.getId())*/) {
                        stripeSubscription = Subscription.retrieve(payment.getApiSubscrId());
                        if (stripeSubscription != null) {
                            onetimeAmount += usagePlanPrice.getTotalAmount();//During the upgrade we charge for subscription difference immidiatly
                            Map<String, Object> item = new HashMap<>();
                            item.put("id", stripeSubscription.getSubscriptionItems().getData().get(0).getId());
                            item.put("plan", kpiPlan.getId());
                            Map<String, Object> items = new HashMap<>();
                            items.put("0", item);
                            Map<String, Object> params = new HashMap<>();
                            params.put("items", items);
                            // Set proration date to this moment:
//                    long prorationDate = System.currentTimeMillis() / 1000L;
//                    params.put("proration_date", prorationDate);
                            params.put("prorate", false);

                            /*stripeSubscription = */
                            stripeSubscription.update(params);
                        }
                    }

                    if (stripeSubscription == null) {
                        stripeSubscriptionParams.put("customer", customer.getId());
                        stripeSubscriptionParams.put("items", plans);
                        stripeSubscriptionParams.put("metadata", genericMetadata);
                        /*stripeSubscription =*/
                        Subscription.create(stripeSubscriptionParams);
                    }
                }
                // Charge the Customer instead of the card:
                //Charge for Addons + plan difference if its upgrade (onetime payment)
                Charge charge = null;
                if (onetimeAmount > 0) {
                    Map<String, Object> chargeParams = new HashMap<>();
                    chargeParams.put("amount", (new BigDecimal(onetimeAmount * 100d).setScale(2, RoundingMode.HALF_UP).intValue()));
                    chargeParams.put("currency", currencyValue);
                    chargeParams.put("description", description);
                    chargeParams.put("customer", customer.getId());
                    try {
                        charge = Charge.create(chargeParams);
                        sendOneTimePaymentNotification = true;
                    } catch (AuthenticationException | InvalidRequestException | CardException | ApiException | ApiConnectionException e) {
                        e.printStackTrace();
                    }
                }


                //msgBody.append("<p>NOTIFICATION_VALIDATION = VERIFIED<p>");

                EdsSubscriptionPayment newSubscriptionPayment = null;

                if (charge != null && StringUtils.isNotBlank(charge.getStatus()) && charge.getStatus().toLowerCase().contains("succeeded")) {
                    newSubscriptionPayment = new EdsSubscriptionPayment();
                    newSubscriptionPayment.setPaymentType(PaymentTypeEnum.STRIPE);
                    newSubscriptionPayment.setStripeCustomerId(customer.getId());
                    //if (charge != null) {
                    newSubscriptionPayment.setStripeChargeId(charge.getId());
                    newSubscriptionPayment.setPayment_status(charge.getStatus());
                    if (stripeSubscription != null) {
                        newSubscriptionPayment.setApiSubscrId(stripeSubscription.getId());
                        newSubscriptionPayment.setSubscriptionPaymentStatus(stripeSubscription.getStatus());
                    }
                    /*(us.getTotalAmount() * 100d)*/
                    newSubscriptionPayment.setAmount3(String.valueOf(charge.getAmount()));
                    newSubscriptionPayment.setPayment_date(df.format(paymentDate));
                    newSubscriptionPayment.setMc_currency(currencyValue);
                    //}
                }

                if (SUBSCRIPTION_ADD.equals(subscriptionOperation)) {
                    //Subscription ADD
                    //Get UsagePlan to update (Customer paying for this UsagePlan)
                    EdsUsagePlan usagePlan = usagePlanManager.get(us.getObjectID());
                    if (StringUtils.isBlank(usagePlan.getUnique_guid()) && StringUtils.isNotBlank(unique_guid)) {
                        usagePlan.setUnique_guid(unique_guid);
                    }

                    //Set UsagePlan details
                    usagePlan.setPayment_StartDate(paymentDate);
                    Calendar cal = getCalendar(paymentDate, usagePlan.getPeriodType().getCode());
                    usagePlan.setEndDate(cal.getTime());
                    usagePlan.setPayment_EndDate(cal.getTime());
                    usagePlan.setEndDate(usagePlan.getPayment_EndDate());

                    usagePlan.setStatus(referenceManager.findReference(EdsUsagePlan._PAYMENT_STATUS, EdsUsagePlan.ACTIVE));
//                usagePlan.setPaid(true);
                    usagePlan.getCompany().setActive(true);

                    //Expire FREE usage plan
                    final EdsReference periodType = referenceManager.findReference(EdsUsagePlan._PERIOD_TYPE, EdsUsagePlan.FREE_TRIAL);
                    final EdsUsagePlan freeUsage = usagePlanManager.getFreeTrialUsagePlanCompany(periodType, usagePlan.getCompany());
                    if (freeUsage != null) {
                        freeUsage.setMessageSended(true);
                        freeUsage.setStatus(referenceManager.findReference(EdsUsagePlan._PAYMENT_STATUS, EdsUsagePlan.EXPIRED));
                        freeUsage.setEndDate(new DateNonConvertable(new Date()).getNonConvertedDate());
                        usagePlanManager.update(freeUsage);
                    }
                    //Set StartDate of UsagePlan
                    usagePlan.setStartDate(new DateNonConvertable(new Date()).getNonConvertedDate());
                    //Update UsagePlan
                    usagePlanManager.update(usagePlan);
                    //Set UsagePlan
                    if (newSubscriptionPayment != null) {
                        newSubscriptionPayment.setSubsId(us.getObjectID());
                        newSubscriptionPayment.setUsageplan_guid(usagePlan.getUnique_guid());
                    }
                    us.setCompanyName(usagePlan.getCompany().getName());
                    us.setCompanyID(usagePlan.getCompany().getObjectID());

                    //msgBody = msgBody.append("<p>Company name:").append(usagePlan.getCompany().getName())
                    //        .append("( ").append(usagePlan.getCompany().getObjectID()).append(")")
                    //        .append("; </p>");

                } else if (SUBSCRIPTION_UPG.equals(subscriptionOperation)) {

                    //Subscription UPGRADE
                    final EdsSubscriptionHistory subscriptionHistory = subscriptionHistoryManager.get(subscriptionHistoryID);
                    final EdsUsagePlan usagePlanHist = subscriptionHistory.getUsagePlan();
                    final EdsCompanySystemSettings companySystemSettings = companySystemSettingsManager.findByCompanyID(usagePlanHist.getCompany().getObjectID());

                    if (StringUtils.isBlank(usagePlanHist.getUnique_guid()) && StringUtils.isNotBlank(unique_guid)) {
                        usagePlanHist.setUnique_guid(unique_guid);
                    }

                    if (companySystemSettings != null) {
                        companySystemSettings.setPayPalRecurring(Boolean.TRUE);
                    }

                    subscriptionHistory.setPaid(true);
//                  usagePlanHist.setPaid(true);
                    usagePlanHist.getCompany().setActive(true);

                    //Set UsagePlan
                    if (newSubscriptionPayment != null) {
                        newSubscriptionPayment.setUsageplan_guid(usagePlanHist.getUnique_guid());
                    }
                    if (us.getUsageMonth() == 1) {
                        usagePlanHist.setPeriodType(referenceManager.findReference(EdsUsagePlan._PERIOD_TYPE, EdsUsagePlan.ONE_MONTH_0));
                    } else {
                        usagePlanHist.setPeriodType(referenceManager.findReference(EdsUsagePlan._PERIOD_TYPE, EdsUsagePlan.TWELVE_MONTH_TWENTY_30));
                    }

                    usagePlanHist.setTotalAmount(subscriptionHistory.getTotalAmount() /*+ usagePlanHist.getTotalAmount()*/);
                    usagePlanHist.setUsers(subscriptionHistory.getUsers());
                    usagePlanHist.setEssUsers(subscriptionHistory.getEssUsers());
                    usagePlanHist.setNoAccessUsers(subscriptionHistory.getNoAccessUsers());
                    usagePlanHist.setStorage(subscriptionHistory.getStorage());
                    usagePlanHist.setDiscount(subscriptionHistory.getDiscount());
                    usagePlanHist.setPayment_EndDate(subscriptionHistory.getPayment_EndDate());
                    usagePlanHist.setSubscriptionHistory(subscriptionHistory);
                    usagePlanHist.setCategoryCODE(subscriptionHistory.getCategoryCODE());
                    usagePlanHist.setSupportPackageNAME(subscriptionHistory.getSupportPackageNAME());
                    usagePlanHist.setUKCompany(Optional.ofNullable(subscriptionHistory.isUKCompany()).orElse(false));
                    //Set Choosen apps
                    usagePlanHist.setAccountsModule(subscriptionHistory.getAccountsModule());
                    usagePlanHist.setProjectModule(subscriptionHistory.getProjectModule());
                    usagePlanHist.setSalesModule(subscriptionHistory.getSalesModule());
                    usagePlanHist.setHumanModule(subscriptionHistory.getHumanModule());
                    usagePlanHist.setPayrollModule(subscriptionHistory.getPayrollModule());

                    //Set addonprices
                    usagePlanHist.setAddonOnlineTraining(subscriptionHistory.getAddonOnlineTraining() + (usagePlanHist.getAddonOnlineTraining() != null ? usagePlanHist.getAddonOnlineTraining() : 0));
                    usagePlanHist.setAddonInitialSetup(subscriptionHistory.getAddonInitialSetup() + (usagePlanHist.getAddonInitialSetup() != null ? usagePlanHist.getAddonInitialSetup() : 0));
                    usagePlanHist.setAddonExtraStorage(subscriptionHistory.getAddonExtraStorage() + (usagePlanHist.getAddonExtraStorage() != null ? usagePlanHist.getAddonExtraStorage() : 0));
                    usagePlanHist.setAddonCustomPDFTemplate(subscriptionHistory.getAddonCustomPDFTemplate() + (usagePlanHist.getAddonCustomPDFTemplate() != null ? usagePlanHist.getAddonCustomPDFTemplate() : 0));
                    usagePlanHist.setAddonDedicatedDeveloper(subscriptionHistory.getAddonDedicatedDeveloper() + (usagePlanHist.getAddonDedicatedDeveloper() != null ? usagePlanHist.getAddonDedicatedDeveloper() : 0));
                    usagePlanHist.setAddonDedicatedAccountManager(subscriptionHistory.getAddonDedicatedAccountManager() + (usagePlanHist.getAddonDedicatedAccountManager() != null ? usagePlanHist.getAddonDedicatedAccountManager() : 0));

                    this.enableDisableModule(usagePlanHist);

                    if (usagePlanHist.getSubscriptionHistory() != null
                            && usagePlanHist.getSubscriptionHistory().getObjectID().equals(subscriptionHistory.getObjectID())) {

                        /*usagePlanHist.setTotalAmount(subscriptionHistory.getTotalAmount());
                        usagePlanHist.setUsers(subscriptionHistory.getUsers());
                        usagePlanHist.setStorage(subscriptionHistory.getStorage());
                        usagePlanHist.setDiscount(subscriptionHistory.getDiscount());
                        usagePlanHist.setPayment_EndDate(subscriptionHistory.getPayment_EndDate());
                        usagePlanHist.setSubscriptionHistory(subscriptionHistory);
                        usagePlanHist.setCategoryCODE(subscriptionHistory.getCategoryCODE());
                        usagePlanHist.setSupportPackageNAME(subscriptionHistory.getSupportPackageNAME());
                        usagePlanHist.setUKCompany(Optional.ofNullable(subscriptionHistory.isUKCompany()).orElse(false));
                        //Set Choosen apps
                        usagePlanHist.setAccountsModule(subscriptionHistory.getAccountsModule());
                        usagePlanHist.setProjectModule(subscriptionHistory.getProjectModule());
                        usagePlanHist.setSalesModule(subscriptionHistory.getSalesModule());
                        usagePlanHist.setHumanModule(subscriptionHistory.getHumanModule());
                        usagePlanHist.setPayrollModule(subscriptionHistory.getPayrollModule());

                        this.enableDisableModule(usagePlanHist);*/
                    } else {
                        usagePlanHist.setSubscriptionHistory(subscriptionHistory);
                        subscriptionHistory.setPayment_StartDate(paymentDate);
                        Calendar calendar = getCalendar(paymentDate, usagePlanHist.getPeriodType().getCode());
                        subscriptionHistory.setPayment_EndDate(calendar.getTime());

                        /*if (SUBSCR_CANCEL.equals(newSubscriptionPayment.getTxn_type())) {
                            usagePlanHist.setPaid(false);
                            usagePlanHist.getCompany().setActive(false);
                            companySystemSettings.setPayPalRecurring(Boolean.FALSE);
                        }*/
                    }

                    //msgBody = msgBody.append("<p>Company name:").append(usagePlanHist.getCompany().getName())
                    //        .append("( ").append(usagePlanHist.getCompany().getObjectID()).append("); Subscription  modify complete</p>");

                    usagePlanManager.update(usagePlanHist);
                    us.setCompanyName(usagePlanHist.getCompany().getName());
                    us.setCompanyID(usagePlanHist.getCompany().getObjectID());
                }

                //Save Payment Information
                if (newSubscriptionPayment != null) {
                    subscriptionPaymentManager.create(newSubscriptionPayment);
                }
                //When charge was failed.
                //    msgBody.append("<p>NOTIFICATION_VALIDATION = FAILED<p>");



            /*if (charge != null) {
                log.info("Addons paid: " + charge.getStatus());
            }
            if (stripeSubscription != null) {
                log.info("Total Reccuring paid: " + stripeSubscription.getStatus());
            }*/
                // YOUR CODE: Save the customer ID and other info in a database for later.

                // YOUR CODE (LATER): When it's time to charge the customer again, retrieve the customer ID.
            /*Map<String, Object> chargeParams = new HashMap<String, Object>();
            chargeParams.put("amount", 1500); // $15.00 this time
            chargeParams.put("currency", "usd");
            chargeParams.put("customer", customerId);
            Charge charge = Charge.create(chargeParams);*/
                result = true;
            }
        } catch (Exception e) {
            log.error("", e);

        }

        if (usagePlanPrice != null && sendOneTimePaymentNotification) {
            us.setUserId(usagePlanManager.getUser().getObjectID());
            us.setUserName(usagePlanManager.getUser().getName());
            us.setUserEmail(usagePlanManager.getUser().getEmail());
            us.setPaymentType(PaymentTypeEnum.STRIPE);
            us.setCurrency("USD");

            int prevModuleCount = 0;
            if (currentUsagePlan.isAccountsModule()) prevModuleCount++;
            if (currentUsagePlan.isHumansModule()) prevModuleCount++;
            if (currentUsagePlan.isProjectModule()) prevModuleCount++;
            if (currentUsagePlan.isSalesModule()) prevModuleCount++;
            if (currentUsagePlan.isPayrollModule()) prevModuleCount++;

            UsagePlanPrice prevUsagePlanPrice = currentUsagePlan.isPaid() ? PricingUtils.getTotalPrice(currentUsagePlan.getUserCount(), currentUsagePlan.getEssUserCount(), prevModuleCount,
                    currentUsagePlan.getNonAccessUserCount(), 0, currentUsagePlan, currentUsagePlan.getUsageMonth() != 12, null) : null;

            //SEND ONE TIME CHARGE PAYMENT NOTIFICATION
            sendStripeOneTimeChargePaymentNotification(subscriptionOperation, us, usagePlanPrice, currentUsagePlan, prevUsagePlanPrice);
        }
        //msgBody.append("<p>HOST =").append(EdsContextParams.getHostname()).append("</p>");
        //msgBody.append("</body></html>");

        //Send Notification Email
        /*try {
            String to = "support@kpi.com,sales@kpi.com," + EdsContextParams.getSupportEmail();
            messageManager.sendMessage(to, msgSubject.toString(), msgBody.toString(), null, false, null, null, null);
        } catch (EdsDbException e) {
            log.error("", e);
        }*/
        if (!result) {
            EdsUser user = employeeManager.getUser();
            try {
                messageManager.sendInsufficientFundsNotification(user);
            } catch (EdsTemplateException e) {
                e.printStackTrace();
            }
            log.info("Email Sent");
        }
        return result;
    }

    /*
      Send stripe one time charge payment notification
     */
    private void sendStripeOneTimeChargePaymentNotification(String subscriptionOperation, UsagePlanItem us, UsagePlanPrice usagePlanPrice, UsagePlanItem prevUsagePlan, UsagePlanPrice prevUsagePlanPrice) {
        try {
            messageManager.sendStripeOneTimeChargePaymentNotification(subscriptionOperation, us, usagePlanPrice, prevUsagePlan, prevUsagePlanPrice);
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Override
    public Boolean chargeForSubscriptionPaymentWithStripe(String subscriptionOperation, UsagePlanItem us, Integer subscriptionHistoryID,
                                                          String stripeCheckoutToken, String currencyValue, String description) {


        StringBuilder msgBody = new StringBuilder("<html><body>");
        msgBody.append("<p>description=").append(description).append("</p>");
        msgBody.append("<p>amount=").append(us.getTotalAmount()).append("</p>");
        msgBody.append("<p>currency=").append(currencyValue).append("</p>");

        StringBuilder msgSubject = new StringBuilder("Stripe Payment Notification:");

        boolean result = false;
        //Charge with STRIPE
        try {
            //Get STRIPE secret key from database
            //Stripe.apiKey = "sk_test_LjJzzD0OQE9RydPRgocD5oQf";
            Stripe.apiKey = EdsContextParams.getStripeSecretKey();

            if (StringUtils.isNotBlank(usagePlanManager.getUser().getFullName())) {
                msgSubject.append(usagePlanManager.getUser().getFullName());
            }

            // Create a Customer:
            Map<String, Object> customerParams = new HashMap<String, Object>();
            if (StringUtils.isNotBlank(usagePlanManager.getUser().getEmail())) {
                customerParams.put("email", usagePlanManager.getUser().getEmail());
            }
            customerParams.put("source", stripeCheckoutToken);
            Customer customer = Customer.create(customerParams);

            // Charge the Customer instead of the card:
            Map<String, Object> chargeParams = new HashMap<String, Object>();
            chargeParams.put("amount", Double.valueOf(us.getTotalAmount() * 100d).intValue());
            chargeParams.put("currency", currencyValue);
            chargeParams.put("description", description);
            chargeParams.put("customer", customer.getId());
            Charge charge = Charge.create(chargeParams);

            if (charge != null && StringUtils.isNotBlank(charge.getStatus()) && charge.getStatus().toLowerCase().contains("succeeded")) {
                msgBody.append("<p>NOTIFICATION_VALIDATION = VERIFIED<p>");

                Date paymentDate = new DateNonConvertable(new Date()).getNonConvertedDate();

                EdsSubscriptionPayment newSubscriptionPayment = new EdsSubscriptionPayment();
                newSubscriptionPayment.setPaymentType(PaymentTypeEnum.STRIPE);
                newSubscriptionPayment.setStripeCustomerId(customer.getId());
                newSubscriptionPayment.setStripeChargeId(charge.getId());
                newSubscriptionPayment.setAmount3(String.valueOf(us.getTotalAmount() * 100d));
                newSubscriptionPayment.setPayment_date(df.format(paymentDate));
                newSubscriptionPayment.setPayment_status(charge.getStatus());
                newSubscriptionPayment.setMc_currency(currencyValue);

                if (SUBSCRIPTION_ADD.equals(subscriptionOperation)) {
                    //Subscription ADD

                    //Get UsagePlan to update (Customer paying for this UsagePlan)
                    EdsUsagePlan usagePlan = usagePlanManager.get(us.getObjectID());

                    //Set UsagePlan details
                    usagePlan.setPayment_StartDate(paymentDate);
                    Calendar cal = getCalendar(paymentDate, usagePlan.getPeriodType().getCode());
                    usagePlan.setEndDate(cal.getTime());
                    usagePlan.setPayment_EndDate(cal.getTime());
                    usagePlan.setEndDate(usagePlan.getPayment_EndDate());

                    usagePlan.setStatus(referenceManager.findReference(EdsUsagePlan._PAYMENT_STATUS, EdsUsagePlan.ACTIVE));
                    usagePlan.setPaid(true);
                    usagePlan.getCompany().setActive(true);

                    //Expire FREE usage plan
                    final EdsReference periodType = referenceManager.findReference(EdsUsagePlan._PERIOD_TYPE, EdsUsagePlan.FREE_TRIAL);
                    final EdsUsagePlan freeUsage = usagePlanManager.getFreeTrialUsagePlanCompany(periodType, usagePlan.getCompany());
                    if (freeUsage != null) {
                        freeUsage.setMessageSended(true);
                        freeUsage.setStatus(referenceManager.findReference(EdsUsagePlan._PAYMENT_STATUS, EdsUsagePlan.EXPIRED));
                        freeUsage.setEndDate(new DateNonConvertable(new Date()).getNonConvertedDate());
                        usagePlanManager.update(freeUsage);
                    }
                    //Set StartDate of UsagePlan
                    usagePlan.setStartDate(new DateNonConvertable(new Date()).getNonConvertedDate());
                    //Update UsagePlan
                    usagePlanManager.update(usagePlan);
                    //Set UsagePlan
                    newSubscriptionPayment.setUsageplan_guid(usagePlan.getUnique_guid());
                    newSubscriptionPayment.setSubsId(us.getObjectID());

                    msgBody = msgBody.append("<p>Company name:").append(usagePlan.getCompany().getName())
                            .append("( ").append(usagePlan.getCompany().getObjectID()).append(")")
                            .append("; </p>");

                } else if (SUBSCRIPTION_UPG.equals(subscriptionOperation)) {

                    //Subscription UPGRADE
                    final EdsSubscriptionHistory subscriptionHistory = subscriptionHistoryManager.get(subscriptionHistoryID);
                    final EdsUsagePlan usagePlan = subscriptionHistory.getUsagePlan();
                    final EdsCompanySystemSettings companySystemSettings = companySystemSettingsManager.findByCompanyID(usagePlan.getCompany().getObjectID());

                    if (companySystemSettings != null) {
                        companySystemSettings.setPayPalRecurring(Boolean.TRUE);
                    }
                    if (usagePlan.getSubscriptionHistory() != null
                            && usagePlan.getSubscriptionHistory().getObjectID().equals(subscriptionHistory.getObjectID())) {
                        //Set UsagePlan
                        newSubscriptionPayment.setUsageplan_guid(usagePlan.getUnique_guid());

                        subscriptionHistory.setPaid(true);
                        usagePlan.setPaid(true);
                        usagePlan.getCompany().setActive(true);

                        if (usagePlan.getTotalAmount() > 0) {
                            usagePlan.setTotalAmount(usagePlan.getTotalAmount() + subscriptionHistory.getTotalAmount());
                        }
                        usagePlan.setUsers(subscriptionHistory.getUsers());
                        usagePlan.setStorage(subscriptionHistory.getStorage());
                        usagePlan.setDiscount(subscriptionHistory.getDiscount());
                        usagePlan.setPayment_EndDate(subscriptionHistory.getPayment_EndDate());
                        usagePlan.setSubscriptionHistory(subscriptionHistory);
                        usagePlan.setCategoryCODE(subscriptionHistory.getCategoryCODE());
                        usagePlan.setSupportPackageNAME(subscriptionHistory.getSupportPackageNAME());
                        usagePlan.setUKCompany(Optional.ofNullable(subscriptionHistory.isUKCompany()).orElse(false));
                        usagePlan.setAccountsModule(subscriptionHistory.getAccountsModule());
                        usagePlan.setProjectModule(subscriptionHistory.getProjectModule());
                        usagePlan.setSalesModule(subscriptionHistory.getSalesModule());
                        usagePlan.setHumanModule(subscriptionHistory.getHumanModule());

                        this.enableDisableModule(usagePlan);
                    } else {
                        usagePlan.setSubscriptionHistory(subscriptionHistory);
                        subscriptionHistory.setPayment_StartDate(paymentDate);
                        Calendar calendar = getCalendar(paymentDate, usagePlan.getPeriodType().getCode());
                        subscriptionHistory.setPayment_EndDate(calendar.getTime());

                        /*if (SUBSCR_CANCEL.equals(newSubscriptionPayment.getTxn_type())) {
                            usagePlan.setPaid(false);
                            usagePlan.getCompany().setActive(false);
                            companySystemSettings.setPayPalRecurring(Boolean.FALSE);
                        }*/
                    }

                    msgBody = msgBody.append("<p>Company name:").append(usagePlan.getCompany().getName())
                            .append("( ").append(usagePlan.getCompany().getObjectID()).append(")")
                            .append("; Subscription  modify complete</p>");

                    usagePlanManager.update(usagePlan);

                } else if (SUBSCRIPTION_SF.equals(subscriptionOperation)) {
                    //Storefront subscription
                    //@TODO To be done if necessary but right now its not used anymore as Iskandar told me (Anvar Akramov)
                }
                //Save Payment Information
                subscriptionPaymentManager.create(newSubscriptionPayment);
            } else {
                //When charge was failed.
                msgBody.append("<p>NOTIFICATION_VALIDATION = FAILED<p>");
            }


            log.info(charge.getStatus());
            // YOUR CODE: Save the customer ID and other info in a database for later.

            // YOUR CODE (LATER): When it's time to charge the customer again, retrieve the customer ID.
            /*Map<String, Object> chargeParams = new HashMap<String, Object>();
            chargeParams.put("amount", 1500); // $15.00 this time
            chargeParams.put("currency", "usd");
            chargeParams.put("customer", customerId);
            Charge charge = Charge.create(chargeParams);*/
            result = true;
        } catch (Exception e) {
            log.error("", e);

        }
        msgBody.append("<p>HOST =").append(EdsContextParams.getHostname()).append("</p>");
        msgBody.append("</body></html>");

        //Send Notification Email
        try {
            String to = "support@kpi.com," + EdsContextParams.getSupportEmail();
            messageManager.sendMessage(to, msgSubject.toString(), msgBody.toString(), null, false, null, null, null);
        } catch (EdsDbException e) {
            log.error("", e);
        }

        return result;
    }

    @Override
    public UsagePlanItem usagePlanSaveAndGet(UsagePlanItem usagePlanItem) {
        final EdsUsagePlan edsUsagePlan = new EdsUsagePlan();
        edsUsagePlan.setUnique_guid(UUID.randomUUID().toString());
        final EdsCompany company = companyManager.get(usagePlanItem.getCompanyID());
        final EdsReference reference = referenceManager.findReference(EdsUsagePlan._PAYMENT_STATUS, EdsUsagePlan.PENDING);

        if (company == null || reference == null) {
            return usagePlanItem;
        }
        final List<EdsUsagePlan> usagePlans = usagePlanManager.getPendingUsagePlans(reference, company);

        for (EdsUsagePlan pendingUp : usagePlans) {
            pendingUp.setDeleted(true);
        }
        final EdsReference periodType = referenceManager.findReference(EdsUsagePlan._PERIOD_TYPE, usagePlanItem.getPlanType());

        edsUsagePlan.setPeriodType(periodType);
        edsUsagePlan.setCompany(company);
        edsUsagePlan.setDiscount(usagePlanItem.getDiscount());
        edsUsagePlan.setPaid(false);
        final Calendar cal = new GregorianCalendar();
        final EdsUsagePlan lastUsagPlan = usagePlanManager.getLastUsagePlan(usagePlanItem.getCompanyID());

        if (lastUsagPlan != null) {
            if (lastUsagPlan.getEndDate() != null && !FREE_TRIAL.equalsIgnoreCase(lastUsagPlan.getPeriodType().getCode())) {
                edsUsagePlan.setStartDate(lastUsagPlan.getEndDate());
                cal.setTime(lastUsagPlan.getEndDate());
            } else {
                edsUsagePlan.setStartDate(cal.getTime());
            }
        } else {
            edsUsagePlan.setStartDate(cal.getTime());
        }
        if (EdsUsagePlan.ONE_MONTH_0.equals(usagePlanItem.getPlanType())) {
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
        } else if (EdsUsagePlan.THREE_MONTH_15.equals(usagePlanItem.getPlanType())) {
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 3);
        } else if (EdsUsagePlan.SIX_MONTH_20.equals(usagePlanItem.getPlanType())) {
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 6);
        } else if (EdsUsagePlan.TWELVE_MONTH_TWENTY_30.equals(usagePlanItem.getPlanType())) {
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);
        } else if (EdsUsagePlan.TWO_YEARS_45.equals(usagePlanItem.getPlanType())) {
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 2);
        } else if (EdsUsagePlan.FREE_TRIAL.equals(usagePlanItem.getPlanType())) {
            if (!usagePlanItem.isMobile()) {
                cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + EdsContextParams.getFreeTrialDays(usagePlanItem.getHostName()));
            } else {
                cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) + 14);
            }
        }
        edsUsagePlan.setEndDate(cal.getTime());
        edsUsagePlan.setStorage(usagePlanItem.getStorageCount());
        edsUsagePlan.setStorageFree(10);
        edsUsagePlan.setTotalAmount(usagePlanItem.getTotalAmount());
        edsUsagePlan.setCurrencyGBP(usagePlanItem.isCurrencyGBP());
        edsUsagePlan.setUKCompany(usagePlanItem.isCompanyUk());
        edsUsagePlan.setMobile(usagePlanItem.isMobile());
        edsUsagePlan.setProjectCount(usagePlanItem.getProjectCount());
        edsUsagePlan.setTaskCount(usagePlanItem.getTaskCount());
        //Set Modules
        edsUsagePlan.setAccountsModule(usagePlanItem.isAccountsModule());
        edsUsagePlan.setHumanModule(usagePlanItem.isHumansModule());
        edsUsagePlan.setSalesModule(usagePlanItem.isSalesModule());
        edsUsagePlan.setProjectModule(usagePlanItem.isProjectModule());
        edsUsagePlan.setPayrollModule(usagePlanItem.isPayrollModule());
        //Set addonprices
        edsUsagePlan.setAddonOnlineTraining(usagePlanItem.getAddonOnlineTraining());
        edsUsagePlan.setAddonInitialSetup(usagePlanItem.getAddonInitialSetup());
        edsUsagePlan.setAddonExtraStorage(usagePlanItem.getAddonExtraStorage());
        edsUsagePlan.setAddonCustomPDFTemplate(usagePlanItem.getAddonCustomPDFTemplate());
        edsUsagePlan.setAddonDedicatedDeveloper(usagePlanItem.getAddonDedicatedDeveloper());
        edsUsagePlan.setAddonDedicatedAccountManager(usagePlanItem.getAddonDedicatedAccountManager());

        final EdsReference serviceType = referenceManager.findReference(EdsUsagePlan._SERVICE_TYPE, usagePlanItem.getService());

        edsUsagePlan.setServiceType(serviceType);
        if ((EdsUsagePlan.ACTIVE).equals(usagePlanItem.getStatus())) {
            edsUsagePlan.setStatus(referenceManager.findReference(EdsUsagePlan._PAYMENT_STATUS, EdsUsagePlan.ACTIVE));
        } else {
            edsUsagePlan.setStatus(reference);
        }
        edsUsagePlan.setUsers(usagePlanItem.getUserCount());
        edsUsagePlan.setNoAccessUsers(Optional.ofNullable(usagePlanItem.getNonAccessUserCount()).orElse(EdsUsagePlan.NO_ACCESS_USERS_COUNT));
        edsUsagePlan.setEssUsers(Optional.ofNullable(usagePlanItem.getEssUserCount()).orElse(EdsUsagePlan.ESS_USERS_COUNT));
        edsUsagePlan.setUserRate(usagePlanItem.getUserRate());

        if (usagePlanItem.getCategoryREAL() != null && !"".equals(usagePlanItem.getCategoryREAL())) {
            edsUsagePlan.setCategoryCODE(usagePlanItem.getCategoryREAL());
        }
        if (!ServerUtils.isNullOrEmpty(usagePlanItem.getSupportPackageNAME())) {
            edsUsagePlan.setSupportPackageNAME(usagePlanItem.getSupportPackageNAME());
        }
        if (usagePlanItem.getModules() != null) {
            edsUsagePlan.setModules(usagePlanItem.getModules());
        }
        usagePlanManager.create(edsUsagePlan);
        usagePlanItem.setObjectID(edsUsagePlan.getObjectID());
        final List<EdsUsagePlan> paidUsagePlans = usagePlanManager.getPaidUsagePlan(usagePlanItem.getCompanyID());

        usagePlanItem.setPaid(!paidUsagePlans.isEmpty());

        this.enableDisableModule(edsUsagePlan);

        return usagePlanItem;
    }

    private void enableDisableModule(EdsUsagePlan usagePlanItem) {
        if (usagePlanItem == null || ServerUtils.isNullOrEmpty(usagePlanItem.getCategoryCODE())) {
            return;
        }
        final HashSet<String> enabledModules = Sets.newHashSet();
        final HashSet<String> disabledModules = Sets.newHashSet();

        //enable all modules for non custom plans
        if (!Constants.PP_CUSTOM.equals(usagePlanItem.getCategoryCODE())) {
            enabledModules.add(PermissionConstants.ACCOUNTING_MODULE);
            enabledModules.add(PermissionConstants.PAYROLL);
            enabledModules.add(PermissionConstants.CRM_MODULE);
            enabledModules.add(PermissionConstants.HRMS_MODULE);
            enabledModules.add(PermissionConstants.PM_MODULE);
        } else {
            if (Optional.ofNullable(usagePlanItem.getAccountsModule()).orElse(false)) {
                enabledModules.add(PermissionConstants.ACCOUNTING_MODULE);
            } else {
                disabledModules.add(PermissionConstants.ACCOUNTING_MODULE);
            }
            if (Optional.ofNullable(usagePlanItem.getPayrollModule()).orElse(false)) {
                enabledModules.add(PermissionConstants.PAYROLL);
            } else {
                disabledModules.add(PermissionConstants.PAYROLL);
            }
            if (Optional.ofNullable(usagePlanItem.getSalesModule()).orElse(false)) {
                enabledModules.add(PermissionConstants.CRM_MODULE);
            } else {
                disabledModules.add(PermissionConstants.CRM_MODULE);
            }
            if (Optional.ofNullable(usagePlanItem.getHumanModule()).orElse(false)) {
                enabledModules.add(PermissionConstants.HRMS_MODULE);
            } else {
                disabledModules.add(PermissionConstants.HRMS_MODULE);
            }
            if (Optional.ofNullable(usagePlanItem.getProjectModule()).orElse(false)) {
                enabledModules.add(PermissionConstants.PM_MODULE);
            } else {
                disabledModules.add(PermissionConstants.PM_MODULE);
            }
        }
        if (!enabledModules.isEmpty()) {
            moduleService.save(usagePlanItem.getCompany().getObjectID(), enabledModules, true);
        }
        if (!disabledModules.isEmpty()) {
            moduleService.save(usagePlanItem.getCompany().getObjectID(), disabledModules, false);
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public boolean getCompanyIsUK() {
        EdsUser user = usagePlanManager.getUser();
        EdsCountry c = countryManager.getCountryByCode(UK);
        return user.getCompany().getCountryZone().getCountry().equals(c);
    }

    @Override
    public Integer createSubscriptionHistory(UsagePlanItem us) {
        final EdsSubscriptionHistory subHis = new EdsSubscriptionHistory();

        subHis.setDiscount(us.getDiscount());
        subHis.setPaid(false);
        subHis.setUKCompany(us.isCompanyUk());
        subHis.setStorage(us.getStorageCount());
        subHis.setTotalAmount(us.getSubTotalAmount());
        subHis.setUsers(us.getUserCount());
        subHis.setEssUsers(us.getEssUserCount());
        subHis.setNoAccessUsers(us.getNonAccessUserCount());
        subHis.setCategoryCODE(us.getCategoryREAL());
        subHis.setSupportPackageNAME(us.getSupportPackageNAME());
        //Set Modules
        subHis.setAccountsModule(us.isAccountsModule());
        subHis.setHumanModule(us.isHumansModule());
        subHis.setSalesModule(us.isSalesModule());
        subHis.setProjectModule(us.isProjectModule());
        subHis.setPayrollModule(us.isPayrollModule());
        //Set addonprices
        subHis.setAddonOnlineTraining(us.getAddonOnlineTraining());
        subHis.setAddonInitialSetup(us.getAddonInitialSetup());
        subHis.setAddonExtraStorage(us.getAddonExtraStorage());
        subHis.setAddonCustomPDFTemplate(us.getAddonCustomPDFTemplate());
        subHis.setAddonDedicatedDeveloper(us.getAddonDedicatedDeveloper());
        subHis.setAddonDedicatedAccountManager(us.getAddonDedicatedAccountManager());

        final EdsUsagePlan usp = usagePlanManager.get(us.getObjectID());

        if (usp != null) {
            subHis.setUsagePlan(usp);
        }
        subscriptionHistoryManager.create(subHis);

        if (usp != null) {
            usp.setUpgrade(true);
            //we must usageplan and set subscription history for paypal type of payments
            usp.setSubscriptionHistory(subHis);
            if (StringUtils.isBlank(usp.getUnique_guid())) {
                usp.setUnique_guid(UUID.randomUUID().toString());
            }
            usagePlanManager.update(usp);
        }
        return subHis.getObjectID();
    }

    @Override
    public Boolean deleteSubscriptionHistory(UsagePlanItem us) {
        if(us!=null && us.getObjectID()!=null) {
            EdsUsagePlan usagePlan = usagePlanManager.get(us.getObjectID());
            if(usagePlan!=null && usagePlan.getSubscriptionHistory()!=null) {
                EdsSubscriptionHistory subscriptionHistory = usagePlan.getSubscriptionHistory();
                usagePlan.setSubscriptionHistory(null);
                subscriptionHistory.setUsagePlan(null);
                usagePlanManager.update(usagePlan);

                subscriptionHistoryManager.delete(subscriptionHistory);
                return Boolean.TRUE;
            }
        }
        return Boolean.FALSE;
    }

    @Override
    @Transactional
    public String payFromPaypal(EdsSubscriptionPayment newSubscriptionPayment, String mes) throws ParseException {
        String customType = newSubscriptionPayment.getCustomType();
        Integer subsId = newSubscriptionPayment.getSubsId();
        Integer storefrontId = newSubscriptionPayment.getStorefrontId();
        EdsUsagePlan usagePlan = null;

        if (SUBSCRIPTION_ADD.equals(customType) && SUBSCR_MODIFY.equals(newSubscriptionPayment.getTxn_type())) {

            usagePlan = usagePlanManager.get(subsId);

            if (usagePlan != null && usagePlan.getCompany() != null) {
                ServerSecurityContext.getInstance().setCompanyId(usagePlan.getCompany().getObjectID());
            }
            if (usagePlan != null && StringUtils.isBlank(usagePlan.getUnique_guid())) {
                usagePlan.setUnique_guid(UUID.randomUUID().toString());
            }
            if (usagePlan != null) {
                newSubscriptionPayment.setUsageplan_guid(usagePlan.getUnique_guid());
            }
            final EdsSubscriptionHistory subsHis = subscriptionHistoryManager.getLastSubscriptionHistory(usagePlan);
            final EdsCompanySystemSettings companySystemSettings = companySystemSettingsManager.findByCompanyID(usagePlan.getCompany().getObjectID());

            companySystemSettings.setPayPalRecurring(Boolean.TRUE);
            if (subsHis != null && "2".equals(newSubscriptionPayment.getItem_number())) {
                mes = mes + "<p>Company name:" + usagePlan.getCompany().getName() + "( " + usagePlan.getCompany().getObjectID() + ")" + "; Subscription  modify complete</p>";
                usagePlan.setSubscriptionHistory(subsHis);
                if (newSubscriptionPayment.getPayment_date() != null) {
                    subsHis.setPayment_StartDate(df.parse(newSubscriptionPayment.getPayment_date()));

                    Calendar cal = this.getCalendar(newSubscriptionPayment.getPayment_date(), usagePlan.getPeriodType().getCode());

                    subsHis.setPayment_EndDate(cal.getTime());
                }
                if (SUBSCR_CANCEL.equals(newSubscriptionPayment.getTxn_type())) {
                    usagePlan.setPaid(false);
                    //Commenting below line because if you press cancel button on paypal site company becomes inactive
                    //usagePlan.getCompany().setActive(false);
                    companySystemSettings.setPayPalRecurring(Boolean.FALSE);
                }
                subscriptionHistoryManager.update(subsHis);
            }
            usagePlanManager.update(usagePlan);
            if (subsHis != null &&
                    subsHis.getUsers().equals(usagePlan.getUsers()) &&
                    subsHis.getStorage().equals(usagePlan.getStorage())) {

                if (usagePlan.getSubscriptionHistory() != null &&
                        usagePlan.getSubscriptionHistory().getObjectID().equals(subsHis.getObjectID())) {

                    newSubscriptionPayment.setUsageplan_guid(usagePlan.getUnique_guid());

                    subsHis.setPaid(true);

                    usagePlan.setPaid(true);
                    usagePlan.setTotalAmount(subsHis.getTotalAmount());
                    usagePlan.setUsers(subsHis.getUsers());
                    usagePlan.setEssUsers(subsHis.getEssUsers());
                    usagePlan.setNoAccessUsers(subsHis.getNoAccessUsers());
                    usagePlan.setStorage(subsHis.getStorage());
                    usagePlan.setDiscount(subsHis.getDiscount());
                    usagePlan.setPayment_EndDate(subsHis.getPayment_EndDate());
                    usagePlan.setSubscriptionHistory(subsHis);
                    usagePlan.setCategoryCODE(subsHis.getCategoryCODE());
                    usagePlan.setSupportPackageNAME(subsHis.getSupportPackageNAME());
                    usagePlan.setUKCompany(subsHis.isUKCompany() != null ? subsHis.isUKCompany() : false);
                    usagePlan.setAccountsModule(subsHis.getAccountsModule());
                    usagePlan.setProjectModule(subsHis.getProjectModule());
                    usagePlan.setSalesModule(subsHis.getSalesModule());
                    usagePlan.setHumanModule(subsHis.getHumanModule());
                    usagePlanManager.update(usagePlan);
                    final EdsCompany company = usagePlan.getCompany();

                    if (company != null) {
                        company.setActive(true);
                        companyManager.update(company);
                    }
                }
            }
        } else if (SUBSCRIPTION_ADD.equals(customType) &&
                (SUBSCR_PAYMENT.equals(newSubscriptionPayment.getTxn_type()) || SUBSCR_SIGNUP.equals(newSubscriptionPayment.getTxn_type()))) { //FOR ADD NEW
            usagePlan = usagePlanManager.get(subsId);
            if (usagePlan == null) {
                log.info("|||||||||||||||||||||||||||||>> UPS! subsId " + subsId + " not found.");
                return mes;
            }
            if (StringUtils.isBlank(usagePlan.getUnique_guid())) {
                usagePlan.setUnique_guid(UUID.randomUUID().toString());
            }
            ServerSecurityContext.getInstance().setCompanyId(usagePlan.getCompany().getObjectID());
            if (newSubscriptionPayment.getPayment_date() != null) {
                usagePlan.setPayment_StartDate(df.parse(newSubscriptionPayment.getPayment_date()));
                Calendar cal = getCalendar(newSubscriptionPayment.getPayment_date(), usagePlan.getPeriodType().getCode());
                usagePlan.setEndDate(cal.getTime());
                usagePlan.setPayment_EndDate(cal.getTime());
                usagePlan.setEndDate(usagePlan.getPayment_EndDate());
            }
            usagePlan.setStatus(referenceManager.findReference(EdsUsagePlan._PAYMENT_STATUS, EdsUsagePlan.ACTIVE));
            usagePlan.setPaid(true);
            usagePlan.getCompany().setActive(true);
            final EdsReference periodType = referenceManager.findReference(EdsUsagePlan._PERIOD_TYPE, EdsUsagePlan.FREE_TRIAL);
            final EdsUsagePlan freeUsage = usagePlanManager.getFreeTrialUsagePlanCompany(periodType, usagePlan.getCompany());

            if (freeUsage != null) {
                freeUsage.setMessageSended(true);
                freeUsage.setStatus(referenceManager.findReference(EdsUsagePlan._PAYMENT_STATUS, EdsUsagePlan.EXPIRED));
                freeUsage.setEndDate(new Date());
                usagePlanManager.update(freeUsage);
            }
            usagePlan.setStartDate(new Date());
            newSubscriptionPayment.setUsageplan_guid(usagePlan.getUnique_guid());
            usagePlanManager.update(usagePlan);
            this.enableDisableModule(usagePlan);
        } else if (SUBSCRIPTION_UPG.equals(customType) &&
                (SUBSCR_MODIFY.equals(newSubscriptionPayment.getTxn_type()) ||
                        SUBSCR_PAYMENT.equals(newSubscriptionPayment.getTxn_type()) ||
                        SUBSCR_SIGNUP.equals(newSubscriptionPayment.getTxn_type()))) {
            final EdsSubscriptionHistory subscriptionHistory = subscriptionHistoryManager.get(subsId);
            usagePlan = subscriptionHistory.getUsagePlan();
            final EdsCompanySystemSettings companySystemSettings = companySystemSettingsManager.findByCompanyID(usagePlan.getCompany().getObjectID());

            if (usagePlan != null) {
                ServerSecurityContext.getInstance().setCompanyId(usagePlan.getCompany().getObjectID());
            }
            if (usagePlan != null && StringUtils.isBlank(usagePlan.getUnique_guid())) {
                usagePlan.setUnique_guid(UUID.randomUUID().toString());
            }
            if (companySystemSettings != null) {
                companySystemSettings.setPayPalRecurring(Boolean.TRUE);
            }
            if (usagePlan.getSubscriptionHistory() != null && usagePlan.getSubscriptionHistory().getObjectID().equals(subscriptionHistory.getObjectID())) {

                newSubscriptionPayment.setUsageplan_guid(usagePlan.getUnique_guid());

                subscriptionHistory.setPaid(true);
                usagePlan.setPaid(true);
                usagePlan.getCompany().setActive(true);

                usagePlan.setTotalAmount(subscriptionHistory.getTotalAmount());
                usagePlan.setUsers(subscriptionHistory.getUsers());
                usagePlan.setEssUsers(subscriptionHistory.getEssUsers());
                usagePlan.setNoAccessUsers(subscriptionHistory.getNoAccessUsers());
                usagePlan.setStorage(subscriptionHistory.getStorage());
                usagePlan.setDiscount(subscriptionHistory.getDiscount());
                usagePlan.setPayment_EndDate(subscriptionHistory.getPayment_EndDate());
                usagePlan.setSubscriptionHistory(subscriptionHistory);
                usagePlan.setCategoryCODE(subscriptionHistory.getCategoryCODE());
                usagePlan.setSupportPackageNAME(subscriptionHistory.getSupportPackageNAME());
                usagePlan.setUKCompany(Optional.ofNullable(subscriptionHistory.isUKCompany()).orElse(false));
                usagePlan.setAccountsModule(subscriptionHistory.getAccountsModule());
                usagePlan.setProjectModule(subscriptionHistory.getProjectModule());
                usagePlan.setSalesModule(subscriptionHistory.getSalesModule());
                usagePlan.setHumanModule(subscriptionHistory.getHumanModule());
                usagePlanManager.update(usagePlan);

                this.enableDisableModule(usagePlan);
            } else {
                mes = mes + "<p>Company name:" + usagePlan.getCompany().getName() +
                        "( " + usagePlan.getCompany().getObjectID() + ")" +
                        "; Subscription  modify complete</p>";

                usagePlan.setSubscriptionHistory(subscriptionHistory);
                if (newSubscriptionPayment.getPayment_date() != null) {
                    subscriptionHistory.setPayment_StartDate(df.parse(newSubscriptionPayment.getPayment_date()));
                    Calendar cal = getCalendar(newSubscriptionPayment.getPayment_date(), usagePlan.getPeriodType().getCode());
                    subscriptionHistory.setPayment_EndDate(cal.getTime());
                }
                if (SUBSCR_CANCEL.equals(newSubscriptionPayment.getTxn_type())) {
                    usagePlan.setPaid(false);
                    //Commenting below line because if you press cancel button on paypal site company becomes inactive
                    //usagePlan.getCompany().setActive(false);
                    companySystemSettings.setPayPalRecurring(Boolean.FALSE);
                }
            }
        }
        subscriptionPaymentManager.create(newSubscriptionPayment);

        //Send new Notification Email Template
        if (usagePlan != null && SUBSCR_PAYMENT.equals(newSubscriptionPayment.getTxn_type())) {
            HashMap<String, String> metadata = new HashMap<>();
            metadata.put("usageplan_id", usagePlan.getUnique_guid());
            metadata.put("user_id", newSubscriptionPayment.getReceiver_id());
            if(StringUtils.isNotBlank(newSubscriptionPayment.getFirst_name())) {
                metadata.put("user_name", newSubscriptionPayment.getFirst_name());
            }
//            metadata.put("user_email", newSubscriptionPayment.getReceiver_email());
            metadata.put("user_email", newSubscriptionPayment.getPayer_email());
            log.info("PAYPAL NOTIFICATION WITH TEMPLATE BEING SEND for usageplan_id:" + usagePlan.getUnique_guid());
            sendPaidStripeWebhookNotification(metadata, PaymentTypeEnum.PAYPAL);
        }
        return mes;
    }

    @Override
    @Transactional
    public void stripeSubscriptionInvoicePaid(Invoice invoice) {
        InvoiceLineItem invoiceLineItem = invoice.getLines().getData().get(0);
        if (invoiceLineItem != null && invoiceLineItem.getMetadata() != null) {
            EdsUsagePlan usagePlan = usagePlanManager.getUsagePlanByUID(invoiceLineItem.getMetadata().get("usageplan_id"));
            if (usagePlan != null) {
                if (StringUtils.isBlank(usagePlan.getUnique_guid())) {
                    usagePlan.setUnique_guid(UUID.randomUUID().toString());
                }
                InvoiceLineItemPeriod invoicePeriod = invoiceLineItem.getPeriod();
                /*log.info("period.start: " + new Date(invoicePeriod.getStart()*1000));
                log.info("period.end: " + new Date(invoicePeriod.getEnd()*1000));*/
                //Subscription ADD
                usagePlan.setStatus(referenceManager.findReference(EdsUsagePlan._PAYMENT_STATUS, EdsUsagePlan.ACTIVE));
                usagePlan.getCompany().setActive(true);
                //Expire FREE usage plan
                final EdsReference periodType = referenceManager.findReference(EdsUsagePlan._PERIOD_TYPE, EdsUsagePlan.FREE_TRIAL);
                final EdsUsagePlan freeUsage = usagePlanManager.getFreeTrialUsagePlanCompany(periodType, usagePlan.getCompany());
                if (freeUsage != null) {
                    freeUsage.setMessageSended(true);
                    freeUsage.setStatus(referenceManager.findReference(EdsUsagePlan._PAYMENT_STATUS, EdsUsagePlan.EXPIRED));
                    freeUsage.setEndDate(new DateNonConvertable(new Date()).getNonConvertedDate());
                    usagePlanManager.update(freeUsage);
                }
                usagePlan.setPaid(true);
                usagePlan.setEndDate(new Date(invoicePeriod.getEnd() * 1000));
                usagePlanManager.update(usagePlan);

                EdsSubscriptionPayment newSubscriptionPayment = new EdsSubscriptionPayment();
                newSubscriptionPayment.setPaymentType(PaymentTypeEnum.STRIPE);
                newSubscriptionPayment.setStripeCustomerId(invoice.getCustomer());
                newSubscriptionPayment.setApiSubscrId(invoice.getSubscription());
                newSubscriptionPayment.setSubscriptionPaymentStatus("active");
                newSubscriptionPayment.setAmount3(String.valueOf(invoice.getTotal()));
                newSubscriptionPayment.setPayment_date(df.format(new Date(invoice.getDate() * 100L)));
                newSubscriptionPayment.setMc_currency(invoice.getCurrency());
                newSubscriptionPayment.setUsageplan_guid(usagePlan.getUnique_guid());
                newSubscriptionPayment.setUsageplan_guid(invoiceLineItem.getMetadata().get("usageplan_id"));
                subscriptionPaymentManager.create(newSubscriptionPayment);
            } else {
                log.info("Usageplan with GUID {} not found !!!", invoiceLineItem.getMetadata().get("usageplan_id"));
            }
        }
    }

    @Override
    @Transactional
    public void sendPaidStripeWebhookNotification(Map<String, String> invoiceLineItem, PaymentTypeEnum paymentTypeEnum) {
        try {
            EdsUsagePlan usagePlan = usagePlanManager.getUsagePlanByUID(invoiceLineItem.get("usageplan_id"));
            if (usagePlan != null) {
                log.info("Usage Plan is not null. ".concat(usagePlan.getName()));
                UsagePlanItem usagePlanItem = getParametr(usagePlan);
                usagePlanItem.setPaymentType(paymentTypeEnum);
                usagePlanItem.setAccountsModule(usagePlan.getAccountsModule() == null ? false : usagePlan.getAccountsModule());
                usagePlanItem.setSalesModule(usagePlan.getSalesModule() == null ? false : usagePlan.getSalesModule());
                usagePlanItem.setHumansModule(usagePlan.getHumanModule() == null ? false : usagePlan.getHumanModule());
                usagePlanItem.setProjectModule(usagePlan.getProjectModule() == null ? false : usagePlan.getProjectModule());
                usagePlanItem.setPayrollModule(usagePlan.getPayrollModule() == null ? false : usagePlan.getPayrollModule());
                if (usagePlan.getCompany() != null) {
                    usagePlanItem.setCompanyName(usagePlan.getCompany().getName());
                    usagePlanItem.setCompanyID(usagePlan.getCompany().getObjectID());
                }
                usagePlanItem.setCurrency("USD");
                usagePlanItem.setUserCount(Optional.ofNullable(usagePlan.getUsers()).orElse(0));
                usagePlanItem.setEssUserCount(Optional.ofNullable(usagePlan.getEssUsers()).orElse(0));
                usagePlanItem.setNonAccessUserCount(Optional.ofNullable(usagePlan.getNoAccessUsers()).orElse(0));

                //Get Email params form Stripe meta data
                if (StringUtils.isNotBlank(invoiceLineItem.get("user_id"))) {
                    try {
                        usagePlanItem.setUserId(Integer.valueOf(invoiceLineItem.get("user_id")));
                    } catch (NumberFormatException ignored) {
                    }
                }
                usagePlanItem.setUserName(invoiceLineItem.get("user_name"));
                usagePlanItem.setUserEmail(invoiceLineItem.get("user_email"));

                int moduleCount = 0;
                if (usagePlanItem.isAccountsModule()) moduleCount++;
                if (usagePlanItem.isPayrollModule()) moduleCount++;
                if (usagePlanItem.isProjectModule()) moduleCount++;
                if (usagePlanItem.isHumansModule()) moduleCount++;
                if (usagePlanItem.isSalesModule()) moduleCount++;

                UsagePlanPrice usagePlanPrice = PricingUtils.getTotalPrice(usagePlanItem.getUserCount(), usagePlanItem.getEssUserCount(), moduleCount,
                        usagePlanItem.getNonAccessUserCount(), 0d,
                        usagePlanItem, usagePlanItem.getUsageMonth() == 1, null);

                messageManager.sendStripeWebhookPaymentNotification(usagePlanItem, usagePlanPrice);
            }
        } catch (Exception e) {
            log.error("", e);
        }
    }

    @Override
    @Transactional
    public String payFromWorld(EdsWorldPayHistory domain, String mes) throws ParseException {
        System.out.println("-----------------------------------------------payFromWorld-----------------------------------------------");
        String customType = domain.getCustomType();
        Integer subsId = domain.getSubsId();
        Integer storefrontId = domain.getStorefrontId();

        if (SUBSCRIPTION_ADD.equals(customType) && WORLDPAY_TRANSACTION_SUCCESSFULL.equals(domain.getTransStatus())) {
            System.out.println("----------------------------------------Add stage----------------------------------------");
            EdsUsagePlan us = usagePlanManager.get(subsId);
            System.out.println("Usage plan id:" + us.getObjectID());
            if (us == null) {
                log.info("|||||||||||||||||||||||||||||>> UPS! subsId " + subsId + " not found.");
                return mes;
            }
            ServerSecurityContext.getInstance().setCompanyId(us.getCompany().getObjectID());
            us.setPayment_StartDate(new Date());
            Calendar cal = getCalendar(new Date(), us.getPeriodType().getCode());
            us.setEndDate(cal.getTime());
            us.setPayment_EndDate(cal.getTime());

            us.setStatus(referenceManager.findReference(EdsUsagePlan._PAYMENT_STATUS, EdsUsagePlan.ACTIVE));
            us.setPaid(true);
            us.getCompany().setActive(true);

            EdsUsagePlan freeUsage = usagePlanManager.getFreeTrialUsagePlanCompany(referenceManager.findReference(EdsUsagePlan._PERIOD_TYPE, EdsUsagePlan.FREE_TRIAL), us.getCompany());
            if (freeUsage != null) {
                freeUsage.setMessageSended(true);
                freeUsage.setStatus(referenceManager.findReference(EdsUsagePlan._PAYMENT_STATUS, EdsUsagePlan.EXPIRED));
                freeUsage.setEndDate(new Date());
                usagePlanManager.update(freeUsage);
            }
            us.setStartDate(new Date());
            domain.setUsagepPlan(us);
            usagePlanManager.update(us);
            System.out.println("------------------------------End saved usage plan for \"SUBSCRIPTION_ADD\"------------------------------");
        } else if (SUBSCRIPTION_UPG.equals(customType) && WORLDPAY_TRANSACTION_SUCCESSFULL.equals(domain.getTransStatus())) {
            System.out.println("-----------------------------------------------Upgrade stage");
            EdsSubscriptionHistory subsHis = subscriptionHistoryManager.get(subsId);
            EdsUsagePlan us = subsHis.getUsagePlan();
            if (us != null) {
                ServerSecurityContext.getInstance().setCompanyId(us.getCompany().getObjectID());
            }
            EdsCompanySystemSettings companySystemSettings = companySystemSettingsManager.findByCompanyID(us.getCompany().getObjectID());
            companySystemSettings.setPayPalRecurring(Boolean.TRUE);
            if (subsHis != null && us != null) {
                System.out.println("-----------------------------------------------Updating subscription");
                domain.setUsagepPlan(us);
                subsHis.setPaid(true);
                us.setPaid(true);
                us.getCompany().setActive(true);

                us.setTotalAmount(subsHis.getTotalAmount());
                us.setUsers(subsHis.getUsers());
                us.setStorage(subsHis.getStorage());
                us.setDiscount(subsHis.getDiscount());
                us.setPayment_EndDate(subsHis.getPayment_EndDate());
                us.setSubscriptionHistory(subsHis);
                us.setCategoryCODE(subsHis.getCategoryCODE());
                us.setSupportPackageNAME(subsHis.getSupportPackageNAME());
                us.setUKCompany(subsHis.isUKCompany() != null ? subsHis.isUKCompany() : false);
                usagePlanManager.update(us);
                System.out.println("-----------------------------------------------End of Updating subscription");
            } else {
                mes = mes + "<p>Company name:" + us.getCompany().getName() + "( " + us.getCompany().getObjectID() + ")" + "; Subscription  modify complete</p>";
                us.setSubscriptionHistory(subsHis);
                subsHis.setPayment_StartDate(new Date());
                if (domain.getPayment_date() != null) {
                    subsHis.setPayment_StartDate(df.parse(domain.getPayment_date()));
                    Calendar cal = getCalendar(domain.getPayment_date(), us.getPeriodType().getCode());
                    subsHis.setPayment_EndDate(cal.getTime());
                } else {
                    subsHis.setPayment_StartDate(new Date());
                    Calendar cal = getCalendar(new Date(), us.getPeriodType().getCode());
                    subsHis.setPayment_EndDate(cal.getTime());
                }
                if (domain.getFuturePayStatusChange() != null && (MERCHANT_CANCELLED.equals(domain.getFuturePayStatusChange()) ||
                        CUSTOMER_CANCELLED.equals(domain.getFuturePayStatusChange()))) {
                    us.setPaid(false);
                    us.getCompany().setActive(false);
                    companySystemSettings.setPayPalRecurring(Boolean.FALSE);
                }
            }
        }

        worldPayHistoryManager.create(domain);
        System.out.println("-----------------------------------------------End of MyAccountServiceImpl method");
        return mes;
    }

    @Transactional
    @Override
    public void sendPayPalNotification(String mes, String subject) {
        messageManager.sendPayPalNotification(mes, subject);
    }

    @Transactional
    @Override
    public void sendWorldPayNotification(String mes, String subject) {
        messageManager.sendWorldPayNotification(mes, subject);
    }

    private Calendar getCalendar(String payment_date, String planType) throws ParseException {
        Calendar cal = new GregorianCalendar();
        cal.setTime(df.parse(payment_date));
        if (EdsUsagePlan.ONE_MONTH_0.equals(planType)) {
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
        } else if (EdsUsagePlan.THREE_MONTH_15.equals(planType)) {
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 3);
        } else if (EdsUsagePlan.SIX_MONTH_20.equals(planType)) {
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 6);
        } else if (EdsUsagePlan.TWELVE_MONTH_TWENTY_30.equals(planType)) {
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);
        } else if (EdsUsagePlan.TWO_YEARS_45.equals(planType)) {
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 2);
        }
        return cal;
    }

    private Calendar getCalendar(Date paymentDate, String planType) throws ParseException {
        Calendar cal = new GregorianCalendar();
        cal.setTime(paymentDate);
        if (EdsUsagePlan.ONE_MONTH_0.equals(planType)) {
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 1);
        } else if (EdsUsagePlan.THREE_MONTH_15.equals(planType)) {
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 3);
        } else if (EdsUsagePlan.SIX_MONTH_20.equals(planType)) {
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) + 6);
        } else if (EdsUsagePlan.TWELVE_MONTH_TWENTY_30.equals(planType)) {
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);
        } else if (EdsUsagePlan.TWO_YEARS_45.equals(planType)) {
            cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 2);
        }
        return cal;
    }

    private int getDayCount(Date startDate, Date endDate) {
        Calendar cal1 = new GregorianCalendar();
        cal1.setTime(startDate);
        Calendar cal2 = new GregorianCalendar();
        cal2.setTime(endDate);
        cal1.set(Calendar.HOUR, 0);
        cal1.set(Calendar.MINUTE, 0);
        cal1.set(Calendar.SECOND, 0);
        cal1.set(Calendar.MILLISECOND, 0);
        cal2.set(Calendar.HOUR, 0);
        cal2.set(Calendar.MINUTE, 0);
        cal2.set(Calendar.SECOND, 0);
        cal2.set(Calendar.MILLISECOND, 0);
        return (360 * (cal2.get(Calendar.YEAR) - cal1.get(Calendar.YEAR)) + 30 * (cal2.get(Calendar.MONTH) -
                cal1.get(Calendar.MONTH)) + cal2.get(Calendar.DAY_OF_MONTH) - cal1.get(Calendar.DAY_OF_MONTH));
    }
}
