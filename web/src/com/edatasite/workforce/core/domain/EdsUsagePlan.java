package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import org.hibernate.annotations.ForeignKey;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * User: Sherali
 * Date: 01.12.2008
 * Time: 13:53:36
 */
@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "usageplan")
public class EdsUsagePlan extends EdsObject {

    public static final String _PERIOD_TYPE = "_PERIOD_TYPE";
    public static final String FREE_TRIAL = "FREE_TRIAL";
    public static final String ONE_MONTH_0 = "ONE_MONTH_0";
    public static final String THREE_MONTH_15 = "THREE_MONTH_15";
    public static final String SIX_MONTH_20 = "SIX_MONTH_20";
    public static final String TWELVE_MONTH_TWENTY_30 = "TWELVE_MONTH_TWENTY_30";
    public static final String TWO_YEARS_45 = "TWO_YEARS_45";

    public static final String _SERVICE_TYPE = "_SERVICE_TYPE";
    public static final String ALL_SERVICE = "all_service";
    public static final String MOBILE_SERVICE = "mobile_service";
    public static final String PM = "pm";
    public static final String PA = "pa";
    public static final String EX = "exp";
    public static final String IN = "inv";
    public static final String AV = "ava";
    public static final String RE = "rep";
    public static final String PRM2 = "pm2";
    public static final String REP = "rep";

    public static final String _PAYMENT_STATUS = "_PAYMENT_STATUS";
    public static final String PENDING = "PENDING";
    public static final String ACTIVE = "ACTIVE";
    public static final String EXPIRED = "EXPIRED";

    public static final Integer NO_ACCESS_USERS_COUNT = 0;
    public static final Integer ESS_USERS_COUNT = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "unique_guid")
    private String unique_guid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    private EdsCompany company;
    //    @Column(name = "startdate")
    private Date startDate;
    private Date endDate;
    @Column(name = "payment_StartDate")
    private Date payment_StartDate;
    @Column(name = "payment_EndDate")
    private Date payment_EndDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plantype")
    @ForeignKey(name = "none")
    private EdsReference serviceType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "periodtype")
    @ForeignKey(name = "none")
    private EdsReference periodType;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "paymentStatus")
    @ForeignKey(name = "none")
    private EdsReference status;
    private Integer users;
    private Integer noAccessUsers;
    private Integer essUsers;
    private Integer usersFree;
    private Integer storage;
    private Integer storageFree;
    private float totalAmount;
    private float discount;
    private float taxt;
    private float totalpayable;

    private String supportPackageNAME;
    private String categoryCODE;

    private Float userRate;

    @Column(name = "isPaid")
    private Boolean isPaid;

    @Column(name = "paypalStatus")
    private Boolean paypalStatus = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "subscriptionHistory")
    private EdsSubscriptionHistory subscriptionHistory;

    @Column(name = "isCurrencyGBP")
    private Boolean isCurrencyGBP;

    private Boolean isUKCompany = false;

    @Column(name = "deleted", columnDefinition = " boolean DEFAULT false")
    private Boolean deleted = false;

    @Column(name = "mobile", columnDefinition = " boolean DEFAULT false")
    private boolean mobile;

    private Integer taskCount = 0;
    private Integer projectCount = 0;

    //track usage plan expiration email notification send or not send
    private Boolean thirtyDays;    //30 days (1 month)
    private Boolean fourTeenDays;  //14 days (2 week)
    private Boolean sevenDays;     //7 days (1 week)
    private Boolean oneDays;       //1 day
    private Boolean ExpiredNotificationSent;       //0 day

    private Boolean accountsModule;
    private Boolean humanModule;
    private Boolean salesModule;
    private Boolean projectModule;
    private Boolean payrollModule;

    @Column(name = "addon_online_training")
    private Double addonOnlineTraining;
    @Column(name = "addon_initial_setup")
    private Double addonInitialSetup;
    @Column(name = "addon_extra_storage")
    private Double addonExtraStorage;
    @Column(name = "addon_custompdf")
    private Double addonCustomPDFTemplate;
    @Column(name = "addon_dedicated_dev")
    private Double addonDedicatedDeveloper;
    @Column(name = "addon_dedicated_accountmangr")
    private Double addonDedicatedAccountManager;

    public Date getPayment_StartDate() {
        return payment_StartDate;
    }

    public void setPayment_StartDate(Date payment_StartDate) {
        this.payment_StartDate = payment_StartDate;
    }

    public Date getPayment_EndDate() {
        return payment_EndDate;
    }

    public void setPayment_EndDate(Date payment_EndDate) {
        this.payment_EndDate = payment_EndDate;
    }

    @Column(name = "isUpgrade")
    private Boolean isUpgrade;

    @Column(name = "upgradePayable")
    private Float upgradePayable;

    @Column(name = "messageSended")
    private Boolean messageSended = false;

    private String modules;

    public Boolean getMessageSended() {
        return messageSended;
    }

    public void setMessageSended(Boolean messageSended) {
        this.messageSended = messageSended;
    }

    public Boolean getPaid() {
        return isPaid != null && isPaid;
    }

    public void setPaid(Boolean paid) {
        isPaid = paid;
    }

    public Boolean getUpgrade() {
        return isUpgrade;
    }

    public void setUpgrade(Boolean upgrade) {
        isUpgrade = upgrade;
    }

    public Float getUpgradePayable() {
        return upgradePayable;
    }

    public void setUpgradePayable(Float upgradePayable) {
        this.upgradePayable = upgradePayable;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsCompany getCompany() {
        return company;
    }

    public void setCompany(EdsCompany company) {
        this.company = company;
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

    public Integer getUsersFree() {
        return usersFree;
    }

    public void setUsersFree(Integer usersFree) {
        this.usersFree = usersFree;
    }

    public Integer getUsers() {
        return users;
    }

    public void setUsers(Integer users) {
        this.users = users;
    }

    public Integer getNoAccessUsers() {
        return noAccessUsers;
    }

    public void setNoAccessUsers(Integer noAccessUsers) {
        this.noAccessUsers = noAccessUsers;
    }

    public Integer getEssUsers() {
        if (essUsers == null) {
            return 0;
        }
        return essUsers;
    }

    public void setEssUsers(Integer essUsers) {
        this.essUsers = essUsers;
    }

    public Integer getStorage() {
        return storage;
    }

    public void setStorage(Integer storage) {
        this.storage = storage;
    }

    public Integer getStorageFree() {
        return storageFree;
    }

    public void setStorageFree(Integer storageFree) {
        this.storageFree = storageFree;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public float getTaxt() {
        return taxt;
    }

    public void setTaxt(float taxt) {
        this.taxt = taxt;
    }

    public float getTotalpayable() {
        return totalpayable;
    }

    public void setTotalpayable(float totalpayable) {
        this.totalpayable = totalpayable;
    }

    public String getSupportPackageNAME() {
        return supportPackageNAME;
    }

    public void setSupportPackageNAME(String supportPackageNAME) {
        this.supportPackageNAME = supportPackageNAME;
    }

    public String getCategoryCODE() {
        return categoryCODE;
    }

    public void setCategoryCODE(String categoryCODE) {
        this.categoryCODE = categoryCODE;
    }

    public Float getUserRate() {
        return userRate;
    }

    public void setUserRate(Float userRate) {
        this.userRate = userRate;
    }

    public EdsReference getServiceType() {
        return serviceType;
    }

    public void setServiceType(EdsReference serviceType) {
        this.serviceType = serviceType;
    }

    public EdsReference getPeriodType() {
        return periodType;
    }

    public void setPeriodType(EdsReference periodType) {
        this.periodType = periodType;
    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        this.status = status;
    }

    public Boolean isPaypalStatus() {
        return paypalStatus;
    }

    public void setPaypalStatus(Boolean paypalStatus) {
        this.paypalStatus = paypalStatus;
    }

    public EdsSubscriptionHistory getSubscriptionHistory() {
        return subscriptionHistory;
    }

    public void setSubscriptionHistory(EdsSubscriptionHistory subscriptionHistory) {
        this.subscriptionHistory = subscriptionHistory;
    }

    public Boolean isCurrencyGBP() {
        return isCurrencyGBP;
    }

    public void setCurrencyGBP(Boolean currencyGBP) {
        isCurrencyGBP = currencyGBP;
    }

    public Boolean isUKCompany() {
        return isUKCompany;
    }

    public void setUKCompany(Boolean UKCompany) {
        isUKCompany = UKCompany;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
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

    public Boolean getThirtyDays() {
        return thirtyDays != null ? thirtyDays : Boolean.FALSE;
    }

    public void setThirtyDays(Boolean thirtyDays) {
        this.thirtyDays = thirtyDays;
    }

    public Boolean getFourTeenDays() {
        return fourTeenDays != null ? fourTeenDays : Boolean.FALSE;
    }

    public void setFourTeenDays(Boolean fourTeenDays) {
        this.fourTeenDays = fourTeenDays;
    }

    public Boolean getSevenDays() {
        return sevenDays != null ? sevenDays : Boolean.FALSE;
    }

    public void setSevenDays(Boolean sevenDays) {
        this.sevenDays = sevenDays;
    }

    public Boolean getExpiredNotificationSent() {
        return ExpiredNotificationSent != null ? ExpiredNotificationSent : Boolean.FALSE;
    }

    public void setExpiredNotificationSent(Boolean accountExpiredSent) {
        this.ExpiredNotificationSent = accountExpiredSent;
    }

    public Boolean getOneDays() {
        return oneDays != null ? oneDays : Boolean.FALSE;
    }

    public void setOneDays(Boolean oneDays) {
        this.oneDays = oneDays;
    }

    public String getModules() {
        return modules;
    }

    public void setModules(String modules) {
        this.modules = modules;
    }

    public void setAccountsModule(Boolean accountsModule) {
        this.accountsModule = accountsModule;
    }

    public Boolean getAccountsModule() {
        return accountsModule;
    }

    public void setHumanModule(Boolean humanModule) {
        this.humanModule = humanModule;
    }

    public Boolean getHumanModule() {
        return humanModule;
    }

    public void setSalesModule(Boolean salesModule) {
        this.salesModule = salesModule;
    }

    public Boolean getSalesModule() {
        return salesModule;
    }

    public void setProjectModule(Boolean projectModule) {
        this.projectModule = projectModule;
    }

    public Boolean getProjectModule() {
        return projectModule;
    }

    public Boolean getPayrollModule() {
        return payrollModule;
    }

    public void setPayrollModule(Boolean payrollModule) {
        this.payrollModule = payrollModule;
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
}