package com.edatasite.workforce.gwt.myaccount.client.rpc;

import com.edatasite.workforce.gwt.core.client.enums.PaymentTypeEnum;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

/**
 * User: Sherali
 * Date: 05.12.2008
 * Time: 13:07:36
 */
public class UsagePlanItem implements IsSerializable {

    public static final String SERVICE = "service";
    public static final String NAME = "name";
    public static final String START_DATE = "startDate";
    public static final String MODULES = "modules";
    public static final String END_DATE = "endDate";
    public static final String STATUS = "status";
    public static final String USERS = "users";
    public static final String STORAGE = "storage";
    public static final String ESS_USERS = "essUsers";
    public static final String NOACCESS_USERS = "noAccessUsers";
    public static final String CUSTOM = "custom"; //custom calculation
    public static final String ACTION = "ACTION";

    private Date startDate;
    private Date endDate;
    private Integer objectID;
    private String unique_guid;
    private String planType;
    private String[] planTypesMonthly;
    private Integer userCount;
    private Integer nonAccessUserCount;
    private Integer essUserCount;
    private Integer activeUserCount;
    private Integer activeNonAccessUserCount;
    private Integer activeEssUserCount;
    private Integer registeredUsersCount;
    private Integer storageCount;
    private float totalAmount;
    private float subTotalAmount;
    private float[] totalAmountsMonthly;
    private float[] subTotalAmountsMonthly;
    private float discount;
    private float[] discountsMonthly;
    private Integer companyID;
    private String companyName;
    private String service;
    private String status;
    private boolean paid;
    private float tax;
    private float[] taxMonthly;
    private int usageMonth;
    private int[] usageMonths;
    private boolean free;
    private float costDown;
    private String periodType;
    private String period;
    private boolean allService;
    private boolean isCurrSub;
    private boolean companyUk = false;
    private boolean paypalStatus;
    private boolean showUpgBt;
    private String hostName;
    private String modules;
    private String currency;
    private PaymentTypeEnum paymentType;

    private Integer supportPackage;

    private String supportPackageNAME;
    private float supportPackagePrice;
    private String categoryREAL;

    private String upgSupportPackageNAME;
    private float upgSupportPackagePrice;
    private String upgCategoryREAL;

    private Date expireDate;
    private String compName;

    private Integer upgSubHisId;
    private Integer upgUserCount;
    private Integer upgStorageCount;
    private boolean isUpgPayed = true;

    private boolean isCurrencyGBP = false;
    private int dayCount = 0;
    private int upgDayCount = 0;

    private boolean mobile = false;
    private Integer taskCount = 0;
    private Integer projectCount = 0;
    private Integer addedTaskCount = 0;
    private Integer addedProjectCount = 0;

    private Float userRate;

    private boolean projectModule;
    private boolean salesModule;
    private boolean humansModule;
    private boolean accountsModule;
    private boolean payrollModule;

    private boolean upgProjectModule;
    private boolean upgSalesModule;
    private boolean upgHumansModule;
    private boolean upgAccountsModule;
    private boolean upgPayrollModule;

    private AddOnsItem addOnsItem;
    private Double addonOnlineTraining;
    private Double addonInitialSetup;
    private Double addonExtraStorage;
    private Double addonCustomPDFTemplate;
    private Double addonDedicatedDeveloper;
    private Double addonDedicatedAccountManager;

    private Integer userId;
    private String userName;
    private String userEmail;
    private String periodConstant;

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public boolean isCurrSub() {
        return isCurrSub;
    }

    public void setCurrSub(boolean currSub) {
        isCurrSub = currSub;
    }

    public boolean isAllService() {
        return allService;
    }

    public void setAllService(boolean allService) {
        this.allService = allService;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getPlanType() {
        return planType;
    }

    public void setPlanType(String planType) {
        this.planType = planType;
    }

    public String[] getPlanTypesMonthly() {
        return planTypesMonthly;
    }

    public void setPlanTypesMonthly(String[] planTypesMonthly) {
        this.planTypesMonthly = planTypesMonthly;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public Integer getNonAccessUserCount() {
        return nonAccessUserCount;
    }

    public void setNonAccessUserCount(Integer nonAccessUserCount) {
        this.nonAccessUserCount = nonAccessUserCount;
    }

    public Integer getEssUserCount() {
        return essUserCount;
    }

    public void setEssUserCount(Integer essUserCount) {
        this.essUserCount = essUserCount;
    }

    public Integer getActiveUserCount() {
        return activeUserCount;
    }

    public void setActiveUserCount(Integer activeUserCount) {
        this.activeUserCount = activeUserCount;
    }

    public Integer getActiveNonAccessUserCount() {
        return activeNonAccessUserCount;
    }

    public void setActiveNonAccessUserCount(Integer activeNonAccessUserCount) {
        this.activeNonAccessUserCount = activeNonAccessUserCount;
    }

    public Integer getActiveEssUserCount() {
        return activeEssUserCount;
    }

    public void setActiveEssUserCount(Integer activeEssUserCount) {
        this.activeEssUserCount = activeEssUserCount;
    }

    public Integer getRegisteredUsersCount() {
        return registeredUsersCount;
    }

    public void setRegisteredUsersCount(Integer registeredUsersCount) {
        this.registeredUsersCount = registeredUsersCount;
    }

    public Integer getStorageCount() {
        return storageCount;
    }

    public void setStorageCount(Integer storageCount) {
        this.storageCount = storageCount;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public float getSubTotalAmount() {
        return subTotalAmount;
    }

    public void setSubTotalAmount(float subTotalAmount) {
        this.subTotalAmount = subTotalAmount;
    }

    public float[] getTotalAmountsMonthly() {
        return totalAmountsMonthly;
    }

    public void setTotalAmountsMonthly(float[] totalAmountsMonthly) {
        this.totalAmountsMonthly = totalAmountsMonthly;
    }

    public float[] getSubTotalAmountsMonthly() {
        return subTotalAmountsMonthly;
    }

    public void setSubTotalAmountsMonthly(float[] subTotalAmountsMonthly) {
        this.subTotalAmountsMonthly = subTotalAmountsMonthly;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float[] getDiscountsMonthly() {
        return discountsMonthly;
    }

    public void setDiscountsMonthly(float[] discountsMonthly) {
        this.discountsMonthly = discountsMonthly;
    }

    public float getTax() {
        return tax;
    }

    public void setTax(float tax) {
        this.tax = tax;
    }

    public float[] getTaxMonthly() {
        return taxMonthly;
    }

    public void setTaxMonthly(float[] taxMonthly) {
        this.taxMonthly = taxMonthly;
    }

    public int getUsageMonth() {
        return usageMonth;
    }

    public void setUsageMonth(int usageMonth) {
        this.usageMonth = usageMonth;
    }

    public int[] getUsageMonths() {
        return usageMonths;
    }

    public void setUsageMonths(int[] usageMonths) {
        this.usageMonths = usageMonths;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

    public float getCostDown() {
        return costDown;
    }

    public void setCostDown(float costDown) {
        this.costDown = costDown;
    }

    public String getPeriodType() {
        return periodType;
    }

    public void setPeriodType(String periodType) {
        this.periodType = periodType;
    }

    public boolean isCompanyUk() {
        return companyUk;
    }

    public void setCompanyUk(boolean companyUk) {
        this.companyUk = companyUk;
    }

    public boolean isPaypalStatus() {
        return paypalStatus;
    }

    public void setPaypalStatus(boolean paypalStatus) {
        this.paypalStatus = paypalStatus;
    }

    public Date getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(Date expireDate) {
        this.expireDate = expireDate;
    }

    public String getCompName() {
        return compName;
    }

    public void setCompName(String compName) {
        this.compName = compName;
    }

    public Integer getUpgUserCount() {
        return upgUserCount;
    }

    public void setUpgUserCount(Integer upgUserCount) {
        this.upgUserCount = upgUserCount;
    }

    public Integer getUpgStorageCount() {
        return upgStorageCount;
    }

    public void setUpgStorageCount(Integer upgStorageCount) {
        this.upgStorageCount = upgStorageCount;
    }

    public boolean isUpgPayed() {
        return isUpgPayed;
    }

    public void setUpgPayed(boolean upgPayed) {
        isUpgPayed = upgPayed;
    }

    public boolean isShowUpgBt() {
        return showUpgBt;
    }

    public void setShowUpgBt(boolean showUpgBt) {
        this.showUpgBt = showUpgBt;
    }

    public Integer getUpgSubHisId() {
        return upgSubHisId;
    }

    public void setUpgSubHisId(Integer upgSubHisId) {
        this.upgSubHisId = upgSubHisId;
    }

    public boolean isCurrencyGBP() {
        return isCurrencyGBP;
    }

    public void setCurrencyGBP(boolean currencyGBP) {
        isCurrencyGBP = currencyGBP;
    }

    public int getDayCount() {
        return dayCount;
    }

    public void setDayCount(int dayCount) {
        this.dayCount = dayCount;
    }

    public int getUpgDayCount() {
        return upgDayCount;
    }

    public void setUpgDayCount(int upgDayCount) {
        this.upgDayCount = upgDayCount;
    }

    public boolean isMobile() {
        return mobile;
    }

    public void setMobile(boolean mobile) {
        this.mobile = mobile;
    }

    public Integer getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(Integer taskCount) {
        this.taskCount = taskCount;
    }

    public Integer getProjectCount() {
        return projectCount;
    }

    public void setProjectCount(Integer projectCount) {
        this.projectCount = projectCount;
    }

    public Integer getAddedTaskCount() {
        return addedTaskCount;
    }

    public void setAddedTaskCount(Integer addedTaskCount) {
        this.addedTaskCount = addedTaskCount;
    }

    public Integer getAddedProjectCount() {
        return addedProjectCount;
    }

    public void setAddedProjectCount(Integer addedProjectCount) {
        this.addedProjectCount = addedProjectCount;
    }

    public Float getUserRate() {
        return userRate;
    }

    public void setUserRate(Float userRate) {
        this.userRate = userRate;
    }

    public String getHostName() {
        return this.hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public Integer getSupportPackage() {
        return supportPackage;
    }

    public void setSupportPackage(Integer supportPackage) {
        this.supportPackage = supportPackage;
    }

    public String getSupportPackageNAME() {
        return supportPackageNAME;
    }

    public void setSupportPackageNAME(String supportPackageNAME) {
        this.supportPackageNAME = supportPackageNAME;
    }

    public float getSupportPackagePrice() {
        return supportPackagePrice;
    }

    public void setSupportPackagePrice(float supportPackagePrice) {
        this.supportPackagePrice = supportPackagePrice;
    }

    public String getCategoryREAL() {
        return categoryREAL;
    }

    public void setCategoryREAL(String categoryREAL) {
        this.categoryREAL = categoryREAL;
    }

    public String getUpgSupportPackageNAME() {
        return upgSupportPackageNAME;
    }

    public void setUpgSupportPackageNAME(String upgSupportPackageNAME) {
        this.upgSupportPackageNAME = upgSupportPackageNAME;
    }

    public float getUpgSupportPackagePrice() {
        return upgSupportPackagePrice;
    }

    public void setUpgSupportPackagePrice(float upgSupportPackagePrice) {
        this.upgSupportPackagePrice = upgSupportPackagePrice;
    }

    public String getUpgCategoryREAL() {
        return upgCategoryREAL;
    }

    public void setUpgCategoryREAL(String upgCategoryREAL) {
        this.upgCategoryREAL = upgCategoryREAL;
    }

    public String getModules() {
        return modules;
    }

    public void setModules(String modules) {
        this.modules = modules;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public PaymentTypeEnum getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentTypeEnum paymentType) {
        this.paymentType = paymentType;
    }

    public boolean isProjectModule() {
        return projectModule;
    }

    public void setProjectModule(boolean projectModule) {
        this.projectModule = projectModule;
    }

    public boolean isSalesModule() {
        return salesModule;
    }

    public void setSalesModule(boolean salesModule) {
        this.salesModule = salesModule;
    }

    public boolean isHumansModule() {
        return humansModule;
    }

    public void setHumansModule(boolean humansModule) {
        this.humansModule = humansModule;
    }

    public boolean isAccountsModule() {
        return accountsModule;
    }

    public void setAccountsModule(boolean accountsModule) {
        this.accountsModule = accountsModule;
    }

    public boolean isPayrollModule() {
        return payrollModule;
    }

    public void setPayrollModule(boolean payrollModule) {
        this.payrollModule = payrollModule;
    }

    public boolean isUpgProjectModule() {
        return upgProjectModule;
    }

    public void setUpgProjectModule(boolean upgProjectModule) {
        this.upgProjectModule = upgProjectModule;
    }

    public boolean isUpgSalesModule() {
        return upgSalesModule;
    }

    public void setUpgSalesModule(boolean upgSalesModule) {
        this.upgSalesModule = upgSalesModule;
    }

    public boolean isUpgHumansModule() {
        return upgHumansModule;
    }

    public void setUpgHumansModule(boolean upgHumansModule) {
        this.upgHumansModule = upgHumansModule;
    }

    public boolean isUpgAccountsModule() {
        return upgAccountsModule;
    }

    public void setUpgAccountsModule(boolean upgAccountsModule) {
        this.upgAccountsModule = upgAccountsModule;
    }

    public boolean isUpgPayrollModule() {
        return upgPayrollModule;
    }

    public void setUpgPayrollModule(boolean upgPayrollModule) {
        this.upgPayrollModule = upgPayrollModule;
    }

    public Double getAddonOnlineTraining() {
        return addonOnlineTraining;
    }

    public void setAddonOnlineTraining(Double addonOnlineTraining) {
        this.addonOnlineTraining = addonOnlineTraining;
    }

    public Double getAddonInitialSetup() {
        return addonInitialSetup;
    }

    public void setAddonInitialSetup(Double addonInitialSetup) {
        this.addonInitialSetup = addonInitialSetup;
    }

    public Double getAddonExtraStorage() {
        return addonExtraStorage;
    }

    public void setAddonExtraStorage(Double addonExtraStorage) {
        this.addonExtraStorage = addonExtraStorage;
    }

    public Double getAddonCustomPDFTemplate() {
        return addonCustomPDFTemplate;
    }

    public void setAddonCustomPDFTemplate(Double addonCustomPDFTemplate) {
        this.addonCustomPDFTemplate = addonCustomPDFTemplate;
    }

    public Double getAddonDedicatedDeveloper() {
        return addonDedicatedDeveloper;
    }

    public void setAddonDedicatedDeveloper(Double addonDedicatedDeveloper) {
        this.addonDedicatedDeveloper = addonDedicatedDeveloper;
    }

    public Double getAddonDedicatedAccountManager() {
        return addonDedicatedAccountManager;
    }

    public void setAddonDedicatedAccountManager(Double addonDedicatedAccountManager) {
        this.addonDedicatedAccountManager = addonDedicatedAccountManager;
    }

    public String getUnique_guid() {
        return unique_guid;
    }

    public void setUnique_guid(String unique_guid) {
        this.unique_guid = unique_guid;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public AddOnsItem getAddOnsItem() {
        return addOnsItem;
    }

    public void setAddOnsItem(AddOnsItem addOnsItem) {
        this.addOnsItem = addOnsItem;
    }

    public String getPeriodConstant() {
        return periodConstant;
    }

    public void setPeriodConstant(String periodConstant) {
        this.periodConstant = periodConstant;
    }
}