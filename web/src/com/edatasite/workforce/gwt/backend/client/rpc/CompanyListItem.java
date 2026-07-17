package com.edatasite.workforce.gwt.backend.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.Date;

public class CompanyListItem implements IsSerializable {

    public static final String COMPANY_ID = "CompanyID";                //1
    public static final String COMPANY_NAME = "CompanyName";            //2
    public static final String ORGANIZATION_TYPE = "organizationType";
    public static final String COMPANY_STATUS = "CompanyStatus";            //2
    public static final String REGISTRATION_DATE = "RegistrationDate";  //3
    public static final String ACCESS_COUNT = "AccessCount";            //4
    public static final String FIRST_ACCESS_DATE = "FirstAccessDate";   //5
    public static final String LAST_ACCESS_DATE = "LastAccessDate";     //6
    public static final String PERIOD_ACCESS = "PeriodAccess";          //7
    public static final String USER_COUNT = "UserCount";                //8
    public static final String ACTIVE_USERS_COUNT = "ActiveUsersCount"; //9
    public static final String ACTIVE_USERS_COUNT_SUBS = "ActiveUsersCountSubs"; //9
    public static final String ESS_USERS_COUNT_SUBS = "ESSUsersCountSubs"; //9
    public static final String ESS_USERS_COUNT_ACTUAL = "ESSUsersCountActual"; //9
    public static final String PROJECT_COUNT = "ProjectCount";          //10
    public static final String TASK_COUNT = "TaskCount";                //11
    public static final String TIMESHEET_COUNT = "TimeSheetCount";      //12
    public static final String CLIENT_COUNT = "ClientCount";            //13
    public static final String DEPARTMENTCOUNT = "DepartmentCount";     //15
    public static final String APPRAISALSCOUNT = "AppraisalsCount";     //15
    public static final String SUPPLIER_COUNT = "SupplierCount";        //14
    public static final String LEAD_COUNT = "LeadCount";                //15
    public static final String PHONE = "Phone";                         //15
    public static final String EMPLOYEES = "Employees";                 //15

    public static final String CONTACT_COUNT = "ContactCount";          //16
    public static final String CRM_TASK_COUNT = "CrmTaskCount";         //17
    public static final String EVENT_COUNT = "EventCount";              //18
    public static final String CASE_COUNT = "CaseCount";                //19
    public static final String INVOICE_COUNT = "InvoiceCount";          //20
    public static final String EXPENSE_COUNT = "ExpenseCount";          //21

    public static final String PRODUCT_COUNT = "ProductCount";          //22
    public static final String FOLDER_COUNT = "FolderCount";            //23
    public static final String FILE_COUNT = "FileCount";                //24
    public static final String ASSESSMENT_COUNT = "AssessmentCount";    //25
    public static final String SIGNED_UP_FROM = "SignedUpFrom";         //26
    public static final String SUBSCRIPTION_TYPE = "SubscriptionType";  //27
    public static final String PAYMENT_STATUS = "PaymentStatus";        //28
    public static final String ACTION = "Action";                       //29
    public static final String ADMIN_EMAIL = "AdminEmail";              //30
    public static final String COUNTRY = "Country";                     //31
    public static final String EXPIRATION_DATE = "expirationDate ";    //32
    public static final String HOST_NAME = "hostName";                  //33
    public static final String ADMIN_NAME = "adminName";                  //34
    public static final String AFFILIATE = "affiliate";                  //35
    public static final String COMPAING = "compaing";                  //35
    public static final String SOURCE = "source";                  //36
    public static final String ADMIN_PHONE = "adminPhone";
    public static final String MEDIUM = "medium";
    public static final String REDIRECTED = "redirected";
    public static final String REFERRER = "referrer";
    public static final String GCLID = "gclid";
    public static final String NO_ACCESS_USER_COUNT = "noAccessUserCount";
    public static final String NO_ACCESS_USER_COUNT_SUBS = "noAccessUserCountSubs";

    private Integer objectID;
    private String companyLoginLink;
    private String companyName;
    private String orgType;
    private Integer companyID;
    private Date registrationDate;
    private String accessCount;
    private Date firstAccessDate;
    private Date lastAccessDate;
    private long periodAccess = 0;
    private Integer userCount;
    private Integer activeUserCount;
    private Integer noAccessUserCount;
    private String projectCount;
    private String taskCount;
    private Integer timesheetCount;
    private String clientsCount;
    private Integer supplierCount;
    private Integer leadCount;
    private Integer contactCount;
    private Integer crmtaskCount;
    private Integer eventCount;
    private Integer caseCount;
    private String invoiceCount;
    private Integer expenseCount;
    private Integer productCount;
    private Integer folderCount;
    private Integer fileCount;

    private String employeeCount;
    private String departmentCount;
    private String signedUpPage;
    private String companySignedUpFrom;

    private String country;
    private String industry;
    private String contactPerson;
    private String adminEmail;
    private String email;
    private String phone;

    private String overallUsersCount;
    private String activeUsersCount;
    private Integer essUsersCount;
    private Integer clientContactCount;
    private String inactiveUsersCount;

    private String issuesCount;
    private String tasksinProgressCount;
    private String tasksCompletedCount;
    private String totalTimeEntries;
    private String leaveRequestsCount;
    private String appraisalsCount;
    private Boolean activated;
    private String companySigupCompIP;
    private String usagePlanPaymentType;
    private String usagePlanPaymentStatus;
    private String periodStartDate;
    private String periodEndDate;

    private Date usagPlanStartDate;
    private Date usagPlanEndDate;
    private String hostName;
    private String adminName;
    private String affiliate;
    private String compaing;
    private String source;
    private String medium;
    private String redirected;
    private String referrer;
    private String gclid;
    private String promoCode;

    private String plannedActiveUsers;
    private String plannedEssUsers;
    private Integer plannedNoAccessUsers;
    private Double usagePlanUserRate;
    private Integer currentUsagePlan;
    private String adminPhone;

    public CompanyListItem() {
    }

    public CompanyListItem(String companyName, String phone, Date lastAccessDate, Date registrationDate,
                           Integer accessCount, Integer employeeCount, Integer projectCount, Integer taskCount,
                           Integer departmentCount, String country, String contactPerson, String email, Integer overallUsersCount,
                           Integer activeUsersCount, Integer essUsersCount, Integer clientContactCount, Integer clientsCount, Integer tasksinProgressCount, Integer tasksCompletedCount,
                           Boolean activated, String companySigupCompIP,
                           Integer activeUsersSubs, Integer eSSUsersSubs, Integer noAccessUsersSubs,
                           String periodType, String paymentStatus, Date periodStartDate,
                           Date periodEndDate) {

        this.companyName = companyName != null ? String.valueOf(companyName) : "";
        this.phone = phone != null ? String.valueOf(phone) : "";

        this.lastAccessDate = lastAccessDate;
        this.registrationDate = registrationDate;
        this.usagPlanStartDate = periodStartDate;
        this.usagPlanEndDate = periodEndDate;

        this.accessCount = accessCount != null ? String.valueOf(accessCount) : "0";
        this.employeeCount = employeeCount != null ? String.valueOf(employeeCount) : "0";
        this.projectCount = projectCount != null ? String.valueOf(projectCount) : "0";
        this.taskCount = taskCount != null ? String.valueOf(taskCount) : "0";
        this.departmentCount = departmentCount != null ? String.valueOf(departmentCount) : "0";

        this.country = country != null ? String.valueOf(country) : "";
        this.contactPerson = contactPerson != null ? String.valueOf(contactPerson) : "";
        this.email = email != null ? String.valueOf(email) : "";

        this.overallUsersCount = overallUsersCount != null ? String.valueOf(overallUsersCount) : "0";
        this.activeUsersCount = activeUsersCount != null ? String.valueOf(activeUsersCount) : "0";
        this.essUsersCount = essUsersCount != null ? essUsersCount : 0;
        this.clientContactCount = clientContactCount != null ? clientContactCount : 0;
        this.plannedEssUsers = eSSUsersSubs != null ? String.valueOf(eSSUsersSubs) : "0";
        this.plannedActiveUsers = activeUsersSubs != null ? String.valueOf(activeUsersSubs) : "0";
        this.plannedNoAccessUsers = noAccessUsersSubs != null ? noAccessUsersSubs : 0;
        this.clientsCount = clientsCount != null ? String.valueOf(clientsCount) : "0";
        this.tasksinProgressCount = tasksinProgressCount != null ? String.valueOf(tasksinProgressCount) : "0";
        this.tasksCompletedCount = tasksCompletedCount != null ? String.valueOf(tasksCompletedCount) : "0";

        this.activated = activated != null ? activated : false;
        this.companySigupCompIP = companySigupCompIP != null ? String.valueOf(companySigupCompIP) : "";
        this.usagePlanPaymentType = periodType != null ? String.valueOf(periodType) : "";
        this.usagePlanPaymentStatus = paymentStatus != null ? String.valueOf(paymentStatus) : "";
    }

    /* CompanyListItem with full data for getting company statistics.
     * @see CompanyStatisticManagerImpl getCompanyStatisticList method
     */
    public CompanyListItem(Integer objectID, Integer companyID, String companyName, Date registrationDate, Integer accessCount, Date firstAccessDate, Date lastAccessDate,
                           long periodAccess, Integer userCount, Integer activeUsersCount, Integer clientContactCount, Integer essUsersCount, Integer projectCount, Integer taskCount, Integer timesheetCount,
                           Integer clientCount, Integer supplierCount, Integer leadCount, Integer contactCount, Integer crmtaskCount, Integer eventCount,
                           Integer caseCount, Integer invoiceCount, Integer expenseCount, Integer productCount, Integer folderCount, Integer fileCount,
                           String country, String industry, String contactPerson, String email, String adminEmail, String phone,
                           String signedUpPage, String companySignedUpFrom, String host, String adminName, String affiliate, String compaing, String source, String medium, String redirected, String referrer, String gclid,
                           Integer appraisalsCount, Boolean activated, String clientSignUpCompIP,
                           Integer activeUsersSubs, Integer eSSUsersSubs, Integer noAccessUsersSubs,
                           String periodType, String paymentStatus,
                           Date periodStartDate, Date periodEndDate, Float userRate, Integer noAccessUsersCount) {
        this.objectID = objectID;
        this.companyID = companyID;
        this.companyName = companyName;
        this.registrationDate = registrationDate;
        this.accessCount = accessCount != null ? String.valueOf(accessCount) : "0";
        this.firstAccessDate = firstAccessDate;
        this.lastAccessDate = lastAccessDate;
        this.periodAccess = periodAccess;
        this.userCount = userCount;
        this.activeUsersCount = activeUsersCount != null ? String.valueOf(activeUsersCount) : "0";
        this.clientContactCount = clientContactCount != null ? clientContactCount : 0;
        this.essUsersCount = essUsersCount != null ? essUsersCount : 0;
        this.plannedEssUsers = eSSUsersSubs != null ? String.valueOf(eSSUsersSubs) : "0";
        this.plannedActiveUsers = activeUsersSubs != null ? String.valueOf(activeUsersSubs) : "0";
        this.plannedNoAccessUsers = noAccessUsersSubs != null ? noAccessUsersSubs : 0;
        this.projectCount = projectCount != null ? String.valueOf(projectCount) : "0";
        this.taskCount = taskCount != null ? String.valueOf(taskCount) : "0";
        this.timesheetCount = timesheetCount;
        this.clientsCount = clientCount != null ? String.valueOf(clientCount) : "0";
        this.supplierCount = supplierCount;
        this.leadCount = leadCount;
        this.contactCount = contactCount;
        this.crmtaskCount = crmtaskCount;
        this.eventCount = eventCount;
        this.caseCount = caseCount;
        this.invoiceCount = invoiceCount != null ? String.valueOf(invoiceCount) : "0";
        this.expenseCount = expenseCount;
        this.productCount = productCount;
        this.folderCount = folderCount;
        this.fileCount = fileCount;
        this.signedUpPage = signedUpPage;
        this.companySignedUpFrom = companySignedUpFrom;
        this.country = country;
        this.industry = industry;
        this.contactPerson = contactPerson;
        this.adminEmail = adminEmail;
        this.email = email;
        this.phone = phone;
        this.appraisalsCount = appraisalsCount != null ? String.valueOf(appraisalsCount) : "0";
        this.activated = activated != null ? activated : false;
        this.companySigupCompIP = clientSignUpCompIP != null ? String.valueOf(clientSignUpCompIP) : "N/A";
        this.usagePlanPaymentType = periodType;
        this.usagePlanPaymentStatus = paymentStatus;
        this.usagPlanStartDate = periodStartDate;
        this.usagPlanEndDate = periodEndDate;
        this.hostName = host;
        this.adminName = adminName;
        this.affiliate = affiliate;
        this.compaing = compaing;
        this.source = source;
        this.medium = medium;
        this.redirected = redirected;
        this.referrer = referrer;
        this.gclid = gclid;
        this.usagePlanUserRate = userRate != null ? Double.valueOf(userRate) : null;
        this.noAccessUserCount = noAccessUsersCount;
    }

    public String getInvoiceCount() {
        return invoiceCount;
    }

    public void setInvoiceCount(String invoiceCount) {
        this.invoiceCount = invoiceCount;
    }

    public Boolean getActivated() {
        return activated;
    }

    public void setActivated(Boolean activated) {
        this.activated = activated;
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

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public Date getLastAccessDate() {
        return lastAccessDate;
    }

    public void setLastAccessDate(Date lastAccessDate) {
        this.lastAccessDate = lastAccessDate;
    }

    public Date getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(Date registrationDate) {
        this.registrationDate = registrationDate;
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

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAccessCount() {
        return accessCount;
    }

    public String getEmployeeCount() {
        return employeeCount;
    }

    public String getProjectCount() {
        return projectCount;
    }

    public String getTaskCount() {
        return taskCount;
    }

    public String getDepartmentCount() {
        return departmentCount;
    }

    public void setAccessCount(String accessCount) {
        this.accessCount = accessCount;
    }

    public void setEmployeeCount(String employeeCount) {
        this.employeeCount = employeeCount;
    }

    public void setProjectCount(String projectCount) {
        this.projectCount = projectCount;
    }

    public void setTaskCount(String taskCount) {
        this.taskCount = taskCount;
    }

    public void setDepartmentCount(String departmentCount) {
        this.departmentCount = departmentCount;
    }

    public String getOverallUsersCount() {
        return overallUsersCount;
    }

    public void setOverallUsersCount(String overallUsersCount) {
        this.overallUsersCount = overallUsersCount;
    }

    public String getActiveUsersCount() {
        return activeUsersCount;
    }

    public void setActiveUsersCount(String activeUsersCount) {
        this.activeUsersCount = activeUsersCount;
    }

    public Integer getEssUsersCount() {
        return essUsersCount;
    }

    public void setEssUsersCount(Integer essUsersCount) {
        this.essUsersCount = essUsersCount;
    }

    public String getPlannedActiveUsers() {
        return plannedActiveUsers;
    }

    public void setPlannedActiveUsers(String plannedActiveUsers) {
        this.plannedActiveUsers = plannedActiveUsers;
    }

    public String getInactiveUsersCount() {
        return inactiveUsersCount;
    }

    public void setInactiveUsersCount(String inactiveUsersCount) {
        this.inactiveUsersCount = inactiveUsersCount;
    }

    public String getClientsCount() {
        return clientsCount;
    }

    public void setClientsCount(String clientsCount) {
        this.clientsCount = clientsCount;
    }

    public String getIssuesCount() {
        return issuesCount;
    }

    public void setIssuesCount(String issuesCount) {
        this.issuesCount = issuesCount;
    }

    public String getTasksinProgressCount() {
        return tasksinProgressCount;
    }

    public void setTasksinProgressCount(String tasksinProgressCount) {
        this.tasksinProgressCount = tasksinProgressCount;
    }

    public String getLeaveRequestsCount() {
        return leaveRequestsCount;
    }

    public void setLeaveRequestsCount(String leaveRequestsCount) {
        this.leaveRequestsCount = leaveRequestsCount;
    }

    public String getAppraisalsCount() {
        return appraisalsCount;
    }

    public void setAppraisalsCount(String appraisalsCount) {
        this.appraisalsCount = appraisalsCount;
    }

    public String getTotalTimeEntries() {
        return totalTimeEntries;
    }

    public void setTotalTimeEntries(String totalTimeEntries) {
        this.totalTimeEntries = totalTimeEntries;
    }

    public String getTasksCompletedCount() {
        return tasksCompletedCount;
    }

    public void setTasksCompletedCount(String tasksCompletedCount) {
        this.tasksCompletedCount = tasksCompletedCount;
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getCompanyLoginLink() {
        return companyLoginLink;
    }

    public void setCompanyLoginLink(String companyLoginLink) {
        this.companyLoginLink = companyLoginLink;
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

    public String getCompanySigupCompIP() {
        return companySigupCompIP;
    }

    public void setCompanySigupCompIP(String companySigupCompIP) {
        this.companySigupCompIP = companySigupCompIP;
    }

    public String getUsagePlanPaymentType() {
        return usagePlanPaymentType;
    }

    public void setUsagePlanPaymentType(String usagePlanPaymentType) {
        this.usagePlanPaymentType = usagePlanPaymentType;
    }

    public String getUsagePlanPaymentStatus() {
        return usagePlanPaymentStatus;
    }

    public void setUsagePlanPaymentStatus(String usagePlanPaymentStatus) {
        this.usagePlanPaymentStatus = usagePlanPaymentStatus;
    }

    public String getPeriodStartDate() {
        return periodStartDate;
    }

    public void setPeriodStartDate(String periodStartDate) {
        this.periodStartDate = periodStartDate;
    }

    public String getPeriodEndDate() {
        return periodEndDate;
    }

    public void setPeriodEndDate(String periodEndDate) {
        this.periodEndDate = periodEndDate;
    }

    public Date getUsagPlanStartDate() {
        return usagPlanStartDate;
    }

    public void setUsagPlanStartDate(Date usagPlanStartDate) {
        this.usagPlanStartDate = usagPlanStartDate;
    }

    public Date getUsagPlanEndDate() {
        return usagPlanEndDate;
    }

    public void setUsagPlanEndDate(Date usagPlanEndDate) {
        this.usagPlanEndDate = usagPlanEndDate;
    }

    public Date getFirstAccessDate() {
        return firstAccessDate;
    }

    public void setFirstAccessDate(Date firstAccessDate) {
        this.firstAccessDate = firstAccessDate;
    }

    public long getPeriodAccess() {
        return periodAccess;
    }

    public void setPeriodAccess(long periodAccess) {
        this.periodAccess = periodAccess;
    }

    public Integer getUserCount() {
        return userCount;
    }

    public void setUserCount(Integer userCount) {
        this.userCount = userCount;
    }

    public Integer getActiveUserCount() {
        return activeUserCount;
    }

    public void setActiveUserCount(Integer activeUserCount) {
        this.activeUserCount = activeUserCount;
    }

    public Integer getTimesheetCount() {
        return timesheetCount;
    }

    public void setTimesheetCount(Integer timesheetCount) {
        this.timesheetCount = timesheetCount;
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

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
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

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String getRedirected() {
        return redirected;
    }

    public void setRedirected(String redirected) {
        this.redirected = redirected;
    }

    public String getReferrer() {
        return referrer;
    }

    public void setReferrer(String referrer) {
        this.referrer = referrer;
    }

    public String getGclid() {
        return gclid;
    }

    public Double getUsagePlanUserRate() {
        return usagePlanUserRate;
    }

    public void setUsagePlanUserRate(Double usagePlanUserRate) {
        this.usagePlanUserRate = usagePlanUserRate;
    }

    public void setGclid(String gclid) {
        this.gclid = gclid;
    }

    public Integer getNoAccessUserCount() {
        return noAccessUserCount;
    }

    public void setNoAccessUserCount(Integer noAccessUserCount) {
        this.noAccessUserCount = noAccessUserCount;
    }

    public String getPlannedEssUsers() {
        return plannedEssUsers;
    }

    public void setPlannedEssUsers(String plannedEssUsers) {
        this.plannedEssUsers = plannedEssUsers;
    }

    public Integer getPlannedNoAccessUsers() {
        return plannedNoAccessUsers;
    }

    public void setPlannedNoAccessUsers(Integer plannedNoAccessUsers) {
        this.plannedNoAccessUsers = plannedNoAccessUsers;
    }

    public Integer getCurrentUsagePlan() {
        return currentUsagePlan;
    }

    public void setCurrentUsagePlan(Integer currentUsagePlan) {
        this.currentUsagePlan = currentUsagePlan;
    }

    public boolean isActive() {
        return getActivated() != null && getActivated();
    }

    public Integer getClientContactCount() {
        return clientContactCount;
    }

    public void setClientContactCount(Integer clientContactCount) {
        this.clientContactCount = clientContactCount;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }

    public String getAdminPhone() {
        return adminPhone;
    }

    public void setAdminPhone(String adminPhone) {
        this.adminPhone = adminPhone;
    }
}
