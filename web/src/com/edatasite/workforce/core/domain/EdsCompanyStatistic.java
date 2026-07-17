package com.edatasite.workforce.core.domain;

import com.edatasite.shared.components.EncryptionHelper;
import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.lucene.Indexable;
import com.edatasite.workforce.gwt.backend.client.rpc.CompanyListItem;
import com.edatasite.workforce.gwt.core.client.ui.Constants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "companystatistic")
public class EdsCompanyStatistic extends EdsObject implements Indexable, Constants {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "indexed")
    private Boolean indexed;
    private Integer companyID;
    private String companyName;
    private String orgType;
    private Date registrationDate;
    private Integer accessCount;
    private Date firstAccessDate;
    private Date lastAccessDate;
    private long periodAccess = 0L;
    private Integer userCount;
    private Integer activeUsersCount;
    private Integer noAccessUsersCount;
    private Integer essUsersCount;
    private Integer projectCount;
    private Integer taskCount;
    private Integer timesheetCount;
    private Integer clientCount;
    private Integer supplierCount;
    private Integer leadCount;
    private Integer contactCount;
    private Integer crmtaskCount;
    private Integer eventCount;
    private Integer caseCount;
    private Integer invoiceCount;
    private Integer expenseCount;
    private Integer productCount;
    private Integer folderCount;
    private Integer fileCount;
    private String country;
    private String industry;
    private String contactPerson;
    private String email;
    private String adminEmail;
    private String phone;
    private String signedUpPage;
    private String companySignedUpFrom;
    private String host;
    private String adminName;
    private String affiliate;
    private String compaing;
    private String source;
    private Integer appraisalsCount;
    private Boolean activated;
    private String clientSignUpCompIP;
    private Date statisticUpdatedTime;
    private String medium;
    private String redirected;
    private String referrer;
    private String gclid;
    private Integer plannedActiveUsers;
    private Integer plannedEssUsers;
    private Integer plannedNoAccessUsers;
    private Date usagPlanStartDate;
    private Date usagPlanEndDate;
    private Float usagePlanUserRate;
    private String usagePlanPaymentType;
    private String usagePlanPaymentStatus;
    private Integer clientContactCount;
    private Integer currentUsagePlanId;
    private String adminPhone;

    public Date getStatisticUpdatedTime() {
        return statisticUpdatedTime;
    }

    public void setStatisticUpdatedTime(Date statisticUpdatedTime) {
        this.statisticUpdatedTime = statisticUpdatedTime;
    }

    public Integer getInvoiceCount() {
        return invoiceCount;
    }

    public void setInvoiceCount(Integer invoiceCount) {
        this.invoiceCount = invoiceCount;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }


    public Boolean getIndexed() {
        return indexed;
    }

    public void setIndexed(Boolean indexed) {
        this.indexed = indexed;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getOrgType() {
        return orgType;
    }

    public void setOrgType(String orgType) {
        this.orgType = orgType;
    }

    public Date getLastAccessDate() {
        return lastAccessDate;
    }

    public void setLastAccessDate(Date lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

    public long getPeriodAccess() {
        return periodAccess;
    }

    public void setPeriodAccess(long periodAccess) {
        this.periodAccess = periodAccess;
    }

    public void setPeriodAccess(Date from, Date to) {
        if (from != null && to != null) {
            setPeriodAccess((to.getTime() - from.getTime()) / (1000 * 60 * 60 * 24));
        }
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public Integer getAccessCount() {
        return accessCount;
    }

    public void setAccessCount(Integer accessCount) {
        this.accessCount = accessCount;
    }

    public Date getFirstAccessDate() {
        return firstAccessDate;
    }

    public void setFirstAccessDate(Date firstAccessDate) {
        this.firstAccessDate = firstAccessDate;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public Integer getActiveUsersCount() {
        return activeUsersCount;
    }

    public void setActiveUsersCount(Integer activeUsersCount) {
        this.activeUsersCount = activeUsersCount;
    }

    public Integer getNoAccessUsersCount() {
        return noAccessUsersCount;
    }

    public void setNoAccessUsersCount(Integer noAccessUsersCount) {
        this.noAccessUsersCount = noAccessUsersCount;
    }

    public Integer getEssUsersCount() {
        return essUsersCount;
    }

    public void setEssUsersCount(Integer essUsersCount) {
        this.essUsersCount = essUsersCount;
    }

    public Integer getProjectCount() {
        return projectCount;
    }

    public void setProjectCount(Integer projectCount) {
        this.projectCount = projectCount;
    }

    public Integer getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(Integer taskCount) {
        this.taskCount = taskCount;
    }

    public Integer getTimesheetCount() {
        return timesheetCount;
    }

    public void setTimesheetCount(Integer timesheetCount) {
        this.timesheetCount = timesheetCount;
    }

    public Integer getClientCount() {
        return clientCount;
    }

    public void setClientCount(Integer clientCount) {
        this.clientCount = clientCount;
    }

    public Integer getSupplierCount() {
        return supplierCount;
    }

    public void setSupplierCount(Integer supplierCount) {
        this.supplierCount = supplierCount;
    }

    public Integer getLeadCount() {
        return leadCount;
    }

    public void setLeadCount(Integer leadCount) {
        this.leadCount = leadCount;
    }

    public Integer getContactCount() {
        return contactCount;
    }

    public void setContactCount(Integer contactCount) {
        this.contactCount = contactCount;
    }

    public Integer getCrmtaskCount() {
        return crmtaskCount;
    }

    public void setCrmtaskCount(Integer crmtaskCount) {
        this.crmtaskCount = crmtaskCount;
    }

    public Integer getEventCount() {
        return eventCount;
    }

    public void setEventCount(Integer eventCount) {
        this.eventCount = eventCount;
    }

    public Integer getCaseCount() {
        return caseCount;
    }

    public void setCaseCount(Integer caseCount) {
        this.caseCount = caseCount;
    }

    public Integer getExpenseCount() {
        return expenseCount;
    }

    public void setExpenseCount(Integer expenseCount) {
        this.expenseCount = expenseCount;
    }

    public Integer getProductCount() {
        return productCount;
    }

    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }

    public Integer getFolderCount() {
        return folderCount;
    }

    public void setFolderCount(Integer folderCount) {
        this.folderCount = folderCount;
    }

    public Integer getFileCount() {
        return fileCount;
    }

    public void setFileCount(Integer fileCount) {
        this.fileCount = fileCount;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getContactPerson() {
        return contactPerson;
    }

    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSignedUpPage() {
        return signedUpPage;
    }

    public void setSignedUpPage(String signedUpPage) {
        this.signedUpPage = signedUpPage;
    }

    public String getCompanySignedUpFrom() {
        return companySignedUpFrom;
    }

    public void setCompanySignedUpFrom(String companySignedUpFrom) {
        this.companySignedUpFrom = companySignedUpFrom;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getClientSignUpCompIP() {
        return clientSignUpCompIP;
    }

    public void setClientSignUpCompIP(String clientSignUpCompIP) {
        this.clientSignUpCompIP = clientSignUpCompIP;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
    }

    public Integer getAppraisalsCount() {
        return appraisalsCount;
    }

    public void setAppraisalsCount(Integer appraisalsCount) {
        this.appraisalsCount = appraisalsCount;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAffiliate() {
        return affiliate;
    }

    public void setAffiliate(String affiliate) {
        this.affiliate = affiliate;
    }

    public String getCompaing() {
        return compaing;
    }

    public void setCompaing(String compaing) {
        this.compaing = compaing;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public CompanyListItem getRPC_CompanyListItem() {
        String shadowLoginlink = "shadowLogin?id=" + EncryptionHelper.encryptURL(getCompanyID().toString());
        CompanyListItem result = new CompanyListItem();
        result.setObjectID(getCompanyID());
        result.setCompanyLoginLink(shadowLoginlink);
        result.setCompanyID(getCompanyID());
        result.setCompanyName(getCompanyName() != null ? getCompanyName() : "");
        result.setOrgType(getOrgType() != null ? getOrgType() : "");
        result.setLastAccessDate(getLastAccessDate() != null ? new Date(getLastAccessDate().getTime()) : null);
        result.setRegistrationDate(getRegistrationDate() != null ? new Date(getRegistrationDate().getTime()) : null);
        result.setAccessCount(getAccessCount() != null ? getAccessCount().toString() : "0");
        result.setFirstAccessDate(getFirstAccessDate());
        result.setLastAccessDate(getLastAccessDate());
        result.setPeriodAccess(!"".equals(String.valueOf(getPeriodAccess())) ? getPeriodAccess() : 0);
        result.setUserCount(getUserCount() != null ? getUserCount() : Integer.valueOf(0));
        result.setActiveUsersCount(getActiveUsersCount() != null ? getActiveUsersCount().toString() : "0");
        result.setProjectCount(getProjectCount() != null ? getProjectCount().toString() : "0");
        result.setTaskCount(getTaskCount() != null ? getTaskCount().toString() : "0");
        result.setTimesheetCount(getTimesheetCount() != null ? getTimesheetCount() : Integer.valueOf(0));
        result.setClientsCount(getClientCount() != null ? getClientCount().toString() : "0");
        result.setSupplierCount(getSupplierCount() != null ? getSupplierCount() : Integer.valueOf(0));
        result.setLeadCount(getLeadCount() != null ? getLeadCount() : Integer.valueOf(0));
        result.setContactCount(getContactCount() != null ? getContactCount() : Integer.valueOf(0));
        result.setCrmtaskCount(getCrmtaskCount() != null ? getCrmtaskCount() : Integer.valueOf(0));
        result.setEventCount(getEventCount() != null ? getEventCount() : Integer.valueOf(0));
        result.setCaseCount(getCaseCount() != null ? getCaseCount() : Integer.valueOf(0));
        result.setInvoiceCount(getInvoiceCount() != null ? getInvoiceCount().toString() : "0");
        result.setExpenseCount(getExpenseCount() != null ? getExpenseCount() : Integer.valueOf(0));
        result.setProductCount(getProductCount() != null ? getProductCount() : Integer.valueOf(0));
        result.setFolderCount(getFolderCount() != null ? getFolderCount() : Integer.valueOf(0));
        result.setFileCount(getFileCount() != null ? getFileCount() : Integer.valueOf(0));
        result.setSignedUpPage(getSignedUpPage() != null ? getSignedUpPage() : "N/A");
        result.setCompanySignedUpFrom(getCompanySignedUpFrom() != null ? getCompanySignedUpFrom() : "N/A");
        result.setHostName(getHost() != null ? getHost() : "N/A");
        result.setCountry(getCountry() != null ? getCountry() : "");
        result.setIndustry(getIndustry() != null ? getIndustry() : "");
        result.setContactPerson(getContactPerson() != null ? getContactPerson() : "");
        result.setEmail(getEmail() != null ? getEmail() : "");
        result.setAdminEmail(getAdminEmail() != null ? getAdminEmail() : "");
        result.setPhone(getPhone() != null ? getPhone() : "");
        result.setAppraisalsCount(getAppraisalsCount() != null ? getAppraisalsCount().toString() : "0");
        result.setActivated(getActivated() != null ? getActivated() : false);
        result.setInvoiceCount(getInvoiceCount() != null ? getInvoiceCount().toString() : "");
        result.setCompanySigupCompIP(getClientSignUpCompIP() != null ? getClientSignUpCompIP() : "N/A");
        result.setAdminName(getAdminName() != null ? getAdminName() : "");
        result.setAffiliate(getAffiliate() != null ? getAffiliate() : "");
        result.setCompaing(getCompaing() != null ? getCompaing() : "");
        result.setSource(getSource() != null ? getSource() : "");
        result.setMedium(getMedium() != null ? getMedium() : "");
        result.setRedirected(getRedirected() != null ? getRedirected() : "");
        result.setReferrer(getReferrer() != null ? getReferrer() : "");
        result.setGclid(getGclid() != null ? getGclid() : "");
        result.setNoAccessUserCount(getNoAccessUsersCount() != null ? getNoAccessUsersCount() : 0);
        result.setEssUsersCount(getEssUsersCount() != null ? getEssUsersCount() : 0);
        result.setAdminPhone(getAdminPhone() != null ? getAdminPhone() : "");
        return result;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getMedium() {
        return medium;
    }

    public void setRedirected(String redirected) {
        this.redirected = redirected;
    }

    public String getRedirected() {
        return redirected;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setGclid(String gclid) {
        this.gclid = gclid;
    }

    public String getGclid() {
        return gclid;
    }

    public void setPlannedActiveUsers(Integer plannedActiveUsers) {
        this.plannedActiveUsers = plannedActiveUsers;
    }

    public Integer getPlannedActiveUsers() {
        return plannedActiveUsers;
    }

    public void setPlannedEssUsers(Integer plannedEssUsers) {
        this.plannedEssUsers = plannedEssUsers;
    }

    public Integer getPlannedEssUsers() {
        return plannedEssUsers;
    }

    public void setPlannedNoAccessUsers(Integer plannedNoAccessUsers) {
        this.plannedNoAccessUsers = plannedNoAccessUsers;
    }

    public Integer getPlannedNoAccessUsers() {
        return plannedNoAccessUsers;
    }

    public void setUsagPlanStartDate(Date usagPlanStartdate) {
        this.usagPlanStartDate = usagPlanStartdate;
    }

    public Date getUsagPlanStartDate() {
        return usagPlanStartDate;
    }

    public void setUsagPlanEndDate(Date usagPlanEndDate) {
        this.usagPlanEndDate = usagPlanEndDate;
    }

    public Date getUsagPlanEndDate() {
        return usagPlanEndDate;
    }

    public void setUsagePlanUserRate(Float usagePlanUserRate) {
        this.usagePlanUserRate = usagePlanUserRate;
    }

    public Float getUsagePlanUserRate() {
        return usagePlanUserRate;
    }

    public void setUsagePlanPaymentType(String usagePlanPaymentType) {
        this.usagePlanPaymentType = usagePlanPaymentType;
    }

    public String getUsagePlanPaymentType() {
        return usagePlanPaymentType;
    }

    public void setUsagePlanPaymentStatus(String usagePlanPaymentStatus) {
        this.usagePlanPaymentStatus = usagePlanPaymentStatus;
    }

    public String getUsagePlanPaymentStatus() {
        return usagePlanPaymentStatus;
    }

    public void setClientContactCount(Integer clientContactCount) {
        this.clientContactCount = clientContactCount;
    }

    public Integer getClientContactCount() {
        return clientContactCount;
    }

    public void setCurrentUsagePlanId(Integer currentUsagePlanId) {
        this.currentUsagePlanId = currentUsagePlanId;
    }

    public Integer getCurrentUsagePlanId() {
        return currentUsagePlanId;
    }

    public String getAdminPhone() {
        return adminPhone;
    }

    public void setAdminPhone(String adminPhone) {
        this.adminPhone = adminPhone;
    }
}
