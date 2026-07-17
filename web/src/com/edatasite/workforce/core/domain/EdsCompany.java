package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.appContext.ApplicationContextProvider;
import com.edatasite.workforce.core.domain.assessment.EdsAssessmentTemplate;
import com.edatasite.workforce.core.domain.lucene.Indexable;
import com.edatasite.workforce.core.domain.settings.EdsCompanySettings;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.app.StaticContextAccessor;
import com.edatasite.workforce.gwt.core.server.db.DepartmentManager;
import com.edatasite.workforce.gwt.core.server.db.ProjectManager;
import com.edatasite.workforce.gwt.core.server.db.TimeSlotManager;
import com.edatasite.workforce.gwt.core.server.db.UsagePlanManager;
import com.edatasite.workforce.gwt.core.server.db.UserManager;
import com.edatasite.workforce.gwt.core.server.domain.ObjectHistory;
import org.hibernate.annotations.ForeignKey;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.Where;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;


/**
 * Created by IntelliJ IDEA.
 * User: Slizer3D
 * Date: 22.03.2007
 * Time: 14:28:40
 * Software Team
 */

@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "company")
public class EdsCompany extends EdsObject implements Indexable, ObjectHistory, Constants {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //no need for generator, each time we
    @Column(name = "id")
    private Integer objectID;
    @Column(name = "name")
    private String name;
    @Column(name = "orgType")
    @Type(type = "text")
    private String orgType;
    @Column(name = "description")
    private String description;
    //This field has updated to Office phone
    @Column(name = "phone")
    private String phone;
    @Column(name = "mobilephone")
    private String mobilePhone;
    @Column(name = "promoCode")
    private String promoCode;
    @Column(name = "email")
    private String email;
    private Boolean isSetUp = false;
    private Integer paymentDue;
    private String faxNumber;
    private Boolean isAccountingSetup = false;
    private Boolean isSalesSetup = false;
    private Boolean isShowWorkforceLogoOnPDF = true;
    @Column(name = "pages", length = 5000)
    private String notShowingPages = "";
    private Boolean massMailEnabled = false;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "countryZoneId")
    @ForeignKey(name = "none")
    private EdsCountryZone countryZone;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "countryRegionId")
    @ForeignKey(name = "none")
    private EdsRegion countryRegion;
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE}, fetch = FetchType.LAZY)
    @JoinColumn(name = "workArea")
    @ForeignKey(name = "none")
    private EdsReference workArea;
    @Column(name = "timeSlot")
    private Integer defaultTimeSlot;
    @Column(name = "defaultDepartmentId")
    private Integer defaultDepartment;
    @Column(name = "defaultProjectId")
    private Integer defaultProject;
    @Column(name = "updaterid")
    private Integer updater;
    //server creation time
    @Column(name = "creationTime")
    private Date creationTime;
    @Column(name = "lastUpdateTime")
    private Date lastUpdateTime;
    @Column(name = "creatorid")
    private Integer creator;
    //client time zone registration date
    private Date registrationDate;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "entityID")
    @Where(clause = " entityType = '" + EdsAddress.ENTITY_TYPE_COMPANY + "' and relationType = 0 and (deleted = 'false' or deleted is null)")
    @ForeignKey(name = "none")
    private List<EdsAddress> billingAddresses = new ArrayList<>();
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "entityID")
    @Where(clause = " entityType = '" + EdsAddress.ENTITY_TYPE_COMPANY + "' and relationType = 1 and (deleted = 'false' or deleted is null)")
    @ForeignKey(name = "none")
    private List<EdsAddress> mailingAddresses = new ArrayList<>();
    //Mailing Address
    private Boolean sameAsBilling;
    @Transient
    private EdsAssessmentTemplate defaultTemplate;
    private Date lastWeeklyInvoiceDate;
    private Date lastMonthlyInvoiceDate;
    private Date lastQuarterlyInvoiceDate;
    @Column(name = "isDeleted")
    private Boolean deleted = false;
    private String signedUpPage = PRM;
    private String sigupCompIP;
    //@Column(name="testCompany")
    private Boolean testCompany = false;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id")
    @ForeignKey(name = "none")
    private EdsCurrency currency;
    private Boolean active = true;
    @Column(name = "hasaccess_to_classic_ui", columnDefinition = "boolean default true")
    private Boolean hasAccessToClassicUI = Boolean.FALSE;
    private Boolean anyDataMissing;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "companySettingsId")
    private EdsCompanySettings companySettings; //do not hold the refference but put companyId to companySettings table
    private Boolean hasChat = false;//it's temporary solution. Later you MUST delete this column. -> Ruslan Muhammadov
    private Boolean liveDiscussionEnabled = false;
    private Boolean expertPanelEnabled = false;
    @Column(name = "showCertificatePdfFooter", columnDefinition = "boolean default false")
    private Boolean showCertificatePdfFooter = false;
    private Integer parentCompanyId;
    @Column(name = "indexed")
    private Boolean indexed;
    @Column(nullable = false)
    private boolean isFree = true;
    @Column(name = "accounting_tool")
    private String accountingTool;
    @Column(name = "deletedtime")
    private Date deletedTime;
    @Column(name = "deletedby")
    private String deletedBy;
    @Column(name = "localecode")
    private String localeCode;
    @Column(name = "website")
    private String website;
    @Column(name = "hastelegrambot")
    private Boolean hasTelegramBot = false;

    public EdsCompany() {
    }

    public EdsCompany(Integer n) {
        setObjectID(n);
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        if (active != null && !active) {
            try {
                Calendar cal = Calendar.getInstance();
                cal.setTime(creationTime);//SHOULD BE DELETED AFTER FINDING THE SOLUTION TO COMPANY EXPIRED PROBLEM
                cal.add(Calendar.DATE, 2); //add 2 days to creation date
                if (new Date().before(cal.getTime())) {
                    System.out.println("!!!---Company inactivated before the expiration date, CID:" + objectID);
                    return;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.active = active;
    }

    public void setActiveForce(Boolean active) {
        this.active = active;
    }

    public Boolean hasAccessToClassicUI() {
        return hasAccessToClassicUI;
    }

    public void setAccessToClassicUI(Boolean hasAccessToClassicUI) {
        this.hasAccessToClassicUI = hasAccessToClassicUI;
    }

    public boolean getTestCompany() {
        return testCompany != null && testCompany;
    }

    public void setTestCompany(Boolean testCompany) {
        this.testCompany = testCompany;
    }

    public Date getCompanyDate() {
        return new Date();
    }

    public Date getCompanyDate(Date serverDate) {
        return serverDate;
    }

    public TimeZone getTimeZone() {
        return TimeZone.getTimeZone(countryZone.getZone().getZoneID());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddress1() {
        return getBillingAddress() != null ? getBillingAddress().getAddress() : null;
    }

    public String getBillAddress2() {
        return getBillingAddress() != null ? getBillingAddress().getAddressb() : null;
    }

    public String getAddress2() {
        return getMailingAddress() != null ? getMailingAddress().getAddress() : null;
    }

    public String getMailAddress2() {
        return getMailingAddress() != null ? getMailingAddress().getAddressb() : null;
    }

    public String getCity() {
        return getBillingAddress() != null ? getBillingAddress().getCity() : null;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public EdsReference getWorkArea() {
        return workArea;
    }

    public void setWorkArea(EdsReference workArea) {
        this.workArea = workArea;
    }

    public boolean getIsSetUp() {
        return isSetUp != null && isSetUp;
    }

    public void setIsSetUp(Boolean setUp) {
        isSetUp = setUp;
    }

    public EdsCountry getCountry() {
        return countryZone != null ? countryZone.getCountry() : null;
    }

    public EdsCountryZone getCountryZone() {
        return countryZone;
    }

    public void setCountryZone(EdsCountryZone countryZone) {
        this.countryZone = countryZone;
    }

    public EdsRegion getCountryRegion() {
        return countryRegion;
    }

    public void setCountryRegion(EdsRegion countryRegion) {
        this.countryRegion = countryRegion;
    }

    public EdsAssessmentTemplate getDefaultTemplate() {
        return defaultTemplate;
    }

    public void setDefaultTemplate(EdsAssessmentTemplate defaultTemplate) {
        this.defaultTemplate = defaultTemplate;
    }

    public Date getLastWeeklyInvoiceDate() {
        return lastWeeklyInvoiceDate;
    }

    public void setLastWeeklyInvoiceDate(Date lastWeeklyInvoiceDate) {
        this.lastWeeklyInvoiceDate = lastWeeklyInvoiceDate;
    }

    public Date getLastMonthlyInvoiceDate() {
        return lastMonthlyInvoiceDate;
    }

    public void setLastMonthlyInvoiceDate(Date lastMonthlyInvoiceDate) {
        this.lastMonthlyInvoiceDate = lastMonthlyInvoiceDate;
    }

    public Date getLastQuarterlyInvoiceDate() {
        return lastQuarterlyInvoiceDate;
    }

    public void setLastQuarterlyInvoiceDate(Date lastQuarterlyInvoiceDate) {
        this.lastQuarterlyInvoiceDate = lastQuarterlyInvoiceDate;
    }

    public Boolean isDeleted() {
        return deleted != null ? deleted : false;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Boolean getIndexed() {
        return indexed;
    }

    public void setIndexed(Boolean indexed) {
        this.indexed = indexed;
    }

    public String getSignedUpPage() {
        return signedUpPage;
    }

    public void setSignedUpPage(String signedUpPage) {
        this.signedUpPage = signedUpPage;
    }

    public Integer getPaymentDue() {
        return paymentDue;
    }

    public void setPaymentDue(Integer paymentDue) {
        this.paymentDue = paymentDue;
    }

    public String getPostCode() {
        return getBillingAddress() != null ? getBillingAddress().getZipCode() : null;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public Date getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public void setCreator(EdsUser value) {

    }

    public Date getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(Date lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    @Override
    public void setUpdater(EdsUser user) {

    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public List<EdsAddress> getBillingAddresses() {
        return billingAddresses;
    }

    public void setBillingAddresses(List<EdsAddress> billingAddresses) {
        this.billingAddresses = billingAddresses;
    }

    public List<EdsAddress> getMailingAddresses() {
        return mailingAddresses;
    }

    public void setMailingAddresses(List<EdsAddress> mailingAddresses) {
        this.mailingAddresses = mailingAddresses;
    }

    public int getEmployeeMaxCount(int employeeCount, EdsCompany company) {
        UsagePlanManager usagePlanManager = (UsagePlanManager) ApplicationContextProvider.applicationContext.getBean("usagePlanManager");
        List<EdsUsagePlan> usagePlans = usagePlanManager.getUsagePlansbyCompanyByCompanyId(company);
        EdsUsagePlan currentUsagePlan = null;
        for (EdsUsagePlan up : usagePlans) {
            if (up.getStartDate().getTime() <= (new Date()).getTime() && up.getEndDate().getTime() >= (new Date()).getTime()) {
                currentUsagePlan = up;
            }
        }
        if (currentUsagePlan != null) {
            if (currentUsagePlan.getUsers() > employeeCount) {
                return currentUsagePlan.getUsers() - employeeCount;
            }
        }
        return 0;
    }

    public int getUsegePlanMaxUsers(EdsCompany company) {
        UsagePlanManager usagePlanManager = (UsagePlanManager) ApplicationContextProvider.applicationContext.getBean("usagePlanManager");
        List<EdsUsagePlan> usagePlans = usagePlanManager.getUsagePlansbyCompanyByCompanyId(company);
        EdsUsagePlan currentUsagePlan = null;
        for (EdsUsagePlan up : usagePlans) {
            if (up.getStartDate().getTime() <= (new Date()).getTime() && up.getEndDate().getTime() >= (new Date()).getTime()) {
                currentUsagePlan = up;
            }
        }
        if (currentUsagePlan != null) {
            return currentUsagePlan.getUsers();
        }
        return 0;
    }

    public Integer[] getAllEmployeeMaxCount(int employeeCount, int noAccessEmployeeCount, int essUsersCount, EdsCompany company) {
        Integer[] userCount = new Integer[]{0, 0, 0};
        UsagePlanManager usagePlanManager = (UsagePlanManager) ApplicationContextProvider.applicationContext.getBean("usagePlanManager");
        List<EdsUsagePlan> usagePlans = usagePlanManager.getUsagePlansbyCompanyByCompanyId(company);
        EdsUsagePlan currentUsagePlan = null;
        for (EdsUsagePlan up : usagePlans) {
            if (up.getStartDate().getTime() <= (new Date()).getTime() && up.getEndDate().getTime() >= (new Date()).getTime()) {
                currentUsagePlan = up;
            }
        }
        if (currentUsagePlan != null) {
            if (currentUsagePlan.getUsers() > employeeCount) {
                userCount[ACTIVE] = currentUsagePlan.getUsers() - employeeCount;
            }
            if (currentUsagePlan.getNoAccessUsers() != null && currentUsagePlan.getNoAccessUsers() > noAccessEmployeeCount) {
                userCount[NO_ACCESS] = currentUsagePlan.getNoAccessUsers() - noAccessEmployeeCount;
            }
            if (currentUsagePlan.getEssUsers() != null && currentUsagePlan.getEssUsers() > essUsersCount) {
                userCount[ESS] = currentUsagePlan.getEssUsers() - essUsersCount;
            }
        }
        return userCount;
    }

    public Boolean getSameAsBilling() {
        return sameAsBilling;
    }

    public void setSameAsBilling(Boolean sameAsBilling) {
        this.sameAsBilling = sameAsBilling;
    }

    public String getMailingCity() {
        return getMailingAddress() != null ? getMailingAddress().getCity() : null;
    }

    public String getMailingCountryName() {
        return getMailingAddress() != null ? getMailingAddress().getCountryName() : null;
    }

    public EdsCountry getMailingCountry() {
        return getMailingAddress() != null ? getMailingAddress().getCountry() : null;
    }

    public EdsRegion getMailingCountryRegion() {
        return getMailingAddress() != null ? getMailingAddress().getState() : null;
    }

    public String getMailingPostCode() {
        return getMailingAddress() != null ? getMailingAddress().getZipCode() : null;
    }

    public boolean getAccountingSetup() {
        return isAccountingSetup != null && isAccountingSetup;
    }

    public void setAccountingSetup(Boolean accountingSetup) {
        isAccountingSetup = accountingSetup;
    }

    public boolean getSalesSetup() {
        return isSalesSetup != null && isSalesSetup;
    }

    public void setSalesSetup(Boolean salesSetup) {
        isSalesSetup = salesSetup;
    }

    public Boolean getShowWorkforceLogoOnPDF() {
        return isShowWorkforceLogoOnPDF;
    }

    public void setShowWorkforceLogoOnPDF(Boolean showWorkforceLogoOnPDF) {
        isShowWorkforceLogoOnPDF = showWorkforceLogoOnPDF;
    }

    public String getSigupCompIP() {
        return sigupCompIP;
    }

    public void setSigupCompIP(String sigupCompIP) {
        this.sigupCompIP = sigupCompIP;
    }

    public String getLocale() {
        return localeCode;
    }

    public void setLocale(String localeCode) {
        this.localeCode = localeCode;
    }

    public String getNotShowingPages() {
        return notShowingPages;
    }

    public void setNotShowingPages(String notShowingPages) {
        this.notShowingPages = notShowingPages;
    }

    public Boolean isHasChat() {
        return hasChat;
    }

    public void setHasChat(Boolean hasChat) {
        this.hasChat = hasChat;
    }

    public Boolean getLiveDiscussionEnabled() {
        return liveDiscussionEnabled;
    }

    public void setLiveDiscussionEnabled(Boolean liveDiscussionEnabled) {
        this.liveDiscussionEnabled = liveDiscussionEnabled;
    }

    public Boolean getExpertPanelEnabled() {
        return expertPanelEnabled;
    }

    public void setExpertPanelEnabled(Boolean expertPanelEnabled) {
        this.expertPanelEnabled = expertPanelEnabled;
    }

    public Boolean getMassMailEnabled() {
        return massMailEnabled != null ? massMailEnabled : Boolean.FALSE;
    }

    public void setMassMailEnabled(Boolean massMailEnabled) {
        this.massMailEnabled = massMailEnabled;
    }

    public EdsCompanySettings getCompanySettings() {
        return companySettings;
    }

    public void setCompanySettings(EdsCompanySettings companySettings) {
        this.companySettings = companySettings;
    }

    public Boolean getSetUp() {
        return isSetUp;
    }

    public void setSetUp(Boolean setUp) {
        isSetUp = setUp;
    }

    public EdsDepartment getDefaultDepartment() {
        DepartmentManager dm = (DepartmentManager) ApplicationContextProvider.applicationContext.getBean("departmentManager");
        return dm.get(defaultDepartment);
    }

    public void setDefaultDepartment(Integer defaultDepartment) {
        this.defaultDepartment = defaultDepartment;
    }

    public EdsProject getDefaultProject() {
        if (defaultProject != null) {
            return ((ProjectManager) ApplicationContextProvider.applicationContext.getBean("projectManager")).get(defaultProject);
        } else {
            return null;
        }
    }

    public void setDefaultProject(Integer defaultProject) {
        this.defaultProject = defaultProject;
    }

    public EdsUser getUpdater() {
        return StaticContextAccessor.getBean(UserManager.class).get(updater);
    }

    public void setUpdater(Integer updater) {
        this.updater = updater;
    }

    public EdsUser getCreator() {
        return StaticContextAccessor.getBean(UserManager.class).get(creator);
    }

    public void setCreator(Integer creator) {
        this.creator = creator;
    }

    public EdsTimeSlot getDefaultTimeSlot() {
        return StaticContextAccessor.getBean(TimeSlotManager.class).get(defaultTimeSlot);
    }

    public void setDefaultTimeSlot(Integer defaultTimeSlot) {
        this.defaultTimeSlot = defaultTimeSlot;
    }

    public boolean isFree() {
        return isFree;
    }

    public void setFree(boolean free) {
        isFree = free;
    }

    public boolean hasSchema(List<String> schemas) {
        return getObjectID() != null && schemas != null && schemas.size() > 0 && schemas.contains(getObjectID().toString());
    }

    public Integer getParentCompanyId() {
        return parentCompanyId;
    }

    public void setParentCompanyId(Integer parentId) {
        this.parentCompanyId = parentId;
    }

    public Boolean getShowCertificatePdfFooter() {
        return showCertificatePdfFooter;
    }

    public void setShowCertificatePdfFooter(Boolean showCertificatePdfFooter) {
        this.showCertificatePdfFooter = showCertificatePdfFooter;
    }

    public EdsAddress getBillingAddress() {
        return EdsAddress.getFirstAddress(this.getBillingAddresses(), true, true);
    }

    public EdsAddress getMailingAddress() {
        return EdsAddress.getFirstAddress(this.getMailingAddresses(), true, true);
    }

    public Boolean getAnyDataMissing() {
        return anyDataMissing != null && anyDataMissing;
    }

    public void setAnyDataMissing(Boolean anyDataMissing) {
        this.anyDataMissing = anyDataMissing;
    }

    public EdsCurrency getCurrency() {
        return currency;
    }

    public void setCurrency(EdsCurrency currency) {
        this.currency = currency;
    }

    public String getAccountingTool() {
        return accountingTool;
    }

    public void setAccountingTool(String accountingTool) {
        this.accountingTool = accountingTool;
    }

    public Date getDeletedTime() {
        return deletedTime;
    }

    public void setDeletedTime(Date deletedTime) {
        this.deletedTime = deletedTime;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public Boolean isHasTelegramBot() {
        return hasTelegramBot;
    }

    public void setHasTelegramBot(Boolean hasTelegramBot) {
        this.hasTelegramBot = hasTelegramBot;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }
}
