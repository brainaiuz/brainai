package com.edatasite.workforce.gwt.core.server.rpc;

import com.edatasite.workforce.gwt.backend.client.rpc.CompanyItem;
import com.edatasite.workforce.gwt.core.client.rpc.CompanySystemSettingsItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 * User: Admin
 * Date: 09.10.2008
 * Time: 17:58:09
 */
public class UserSignUPSessionID implements Serializable {

    CompanyItem[] companyList;
    private String signUp;
    private String sessionID;
    private Integer userId;
    private String fullName;
    private String flexfullName;
    private String initialName;
    private String companyName;
    private String email;
    private String cityname;
    private String countryname;
    private String companyCountryCode;
    private String taxName;

    //Setups
    private boolean setupUserProfile;
    private boolean accountingIsSetup;
    private boolean enableAccountingModule;
    private boolean salesIsSetup;

    private Integer accountingCalculationScale;
    private Integer accountingCustomQtyScale;
    private Integer accountingCustomPriceScale;
    private Integer accountingCustomExRateScale;
    private Integer accountingTaxRateScalse;
    private Integer accountingDiscountScale;
    private Integer accountingProgressinvoiceingAmountScale;
    private String transactionLockDate;

    private boolean isSalesLocked;
    private boolean isPurchasesLocked;
    private boolean isBankingLocked;
    private boolean isEmployeesLocked;
    private boolean isAttendanceLocked;
    private boolean isRecruitmentLocked;
    private boolean isPayslipsLocked;
    private boolean isCashAdvancesLocked;
    private boolean isAdditionalPaymentsLocked;
    private boolean isSupplier;
    private boolean pmIsSetup;
    private boolean isSetupSubProject = false;
    private boolean isSetupSubProjectTwoLevel = false;
    private boolean isLiveChatActive = false;
    private boolean isExpertChatActive = false;
    private boolean isMeetingMinutesActive = false;
    private boolean isBookingItemsActive = false;
    private String notShowingPages;
    private boolean workspaceWelcomePageEnable = false;
    private boolean enableWFTMoreMenuForMEM = true;
    private boolean enableWFTMoreMenuForADMIN = true;
    private boolean isClient = false;
    private boolean isEmployee = true;
    private List<String> modulePermissions;
    private boolean emailAccountSetup;

    public List<String> getModulePermissions() {
        if (modulePermissions == null) {
            modulePermissions = new ArrayList<>();
        }
        return modulePermissions;
    }

    public void setModulePermissions(List<String> modulePermissions) {
        this.modulePermissions = modulePermissions;
    }

    private boolean enableSalesBackend = false;
    private boolean enableSupportBackend = false;
    private boolean enableAdminBackend = false;
    private boolean enablePartnerAdminBackend = false;
    //    private boolean enablePDFBackend = false;
    private boolean enableDeveloperBackend = false;
    private boolean enableMonthlyTimesheet = false;
    private boolean enableWorkflowStatus = false;
    private boolean enableStoreFront = false;
    private boolean enableToShowSampleData;
    private boolean anyDataMissing;
    private boolean accountingSettingsEnabled;
    private boolean enableSwitchableLayout;
    private boolean accountingProductionEnabled;
    private boolean testCompany;

    //Gwt section first view
    private String invFirst;
    private String pmFirst;
    private String paFirst;
    private String avaFirst;


    ///For use in BaseGWTPagesController
    private boolean isCompanyActive;
    private boolean isAdmin;
    private boolean hasAccess = true;
    private boolean isClientContact;
    private String roles;

    private Integer companyId;

    private String userName;
    private String firstname;
    private String password;

    private String localeString;

    private String shortDateFormat;
    private String longDateFormat;

    private String googleAppDomain;

    private boolean googleMarketplaceUsersImportShow;

    private SelectItem[] roleItems;

    private UsagePlanItem usagePlan;

    private String companySignedUpFrom;

    private String imageUrl;

    private boolean messageCenterEnabled;

    private String themeForSystem;
    private String languageForUser;
    private String latestUploadVersion;
    private String sessionLength;
    private Integer freeTrialDaysLeft;
    private boolean isPaidCompany;
    private String defaultCurrencyCODE;
    private boolean isAutomatic;
    private boolean isAutomaticApproval;
    private boolean isAutomaticWaitingForApproval;
    private boolean validateTaskStart;
    private boolean validateTaskEnd;
    private boolean validateMaximumHours;
    private boolean validateDayOff;
    private Integer maximumHours;
    private boolean validatePastTimesheet;
    private Integer pastTimesheetDays;
    private boolean validateFutureTimesheet;
    private Integer futureTimesheetDays;
    private String defaultCurrentEmployeeTimeSlotStartTIME;
    private String defaultCurrentEmployeeTimeSlotEndTIME;
    private boolean validateTimeslot;
    private boolean validateHoliday;
    private boolean validateLeaveRequest;
    private boolean timesheetCommentRequired;
    private Integer timesheetWeekStart;
    private Integer overallDatePickerWeekStart;
    private boolean showCompletedTasks;
    private boolean showToDoListTasks;
    private boolean showHourTypeDropdown;
    private boolean enableMultipleTimerInstances;
    private boolean saveTimerIntoTimesheetAutomatically;
    private boolean multiCompanySubsidiary;
    private boolean multiWarehouseEnabled;
    private boolean poIgnoreManagerApproval;
    private boolean isSuperUser;
    private String promotionalCode;
    private boolean enableTraningCenterView;
    private boolean enableSimplifiedReport;
    private boolean enableParentDepartment;

    private CompanySystemSettingsItem companySystemSettingsItem;
    private String timesheetDateFormat;
    private boolean validateTimesheetEstimate;
    private Integer alternativeCalendarId;
    private boolean doubleMessageEnable;
    private boolean multipleSalesPriceEnable;
    private boolean productTableCustomizationEnable;
    private boolean enableLogistics;
    private String tawkToSiteId;
    private String sideNavStyle;
    private String profileContent;
    private boolean vatRegistered;
    private TimeZone timeZone;

    private String vatAccountingBasis;

    public CompanySystemSettingsItem getCompanySystemSettingsItem() {
        if (companySystemSettingsItem == null) {
            companySystemSettingsItem = new CompanySystemSettingsItem();
        }
        return companySystemSettingsItem;
    }

    public void setCompanySystemSettingsItem(CompanySystemSettingsItem companySystemSettingsItem) {
        this.companySystemSettingsItem = companySystemSettingsItem;
    }

    public CompanyItem[] getCompanyList() {
        return companyList;
    }

    public void setCompanyList(CompanyItem[] companyList) {
        this.companyList = companyList;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public UserSignUPSessionID() {
    }

    public UserSignUPSessionID(String sessionID, String signUp) {
        this.signUp = signUp;
        this.sessionID = sessionID;
    }

    public String getSignUp() {
        return signUp;
    }

    public void setSignUp(String signUp) {
        this.signUp = signUp;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public boolean getCompanyActive() {
        return isCompanyActive;
    }

    public void setCompanyActive(boolean companyActive) {
        isCompanyActive = companyActive;
    }

    public boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isClient() {
        return isClient;
    }

    public void setClient(boolean client) {
        isClient = client;
    }

    public boolean isEmployee() {
        return isEmployee;
    }

    public void setEmployee(boolean employee) {
        isEmployee = employee;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public boolean isHasAccess() {
        return hasAccess;
    }

    public void setHasAccess(boolean hasAccess) {
        this.hasAccess = hasAccess;
    }

    public boolean isClientContact() {
        return isClientContact;
    }

    public void setClientContact(boolean clientContact) {
        isClientContact = clientContact;
    }

    public String getFlexfullName() {
        return flexfullName;
    }

    public void setFlexfullName(String flexfullName) {
        this.flexfullName = flexfullName;
    }

    public boolean isSetupUserProfile() {
        return setupUserProfile;
    }

    public void setSetupUserProfile(boolean setupUserProfile) {
        this.setupUserProfile = setupUserProfile;
    }

    public boolean isAccountingIsSetup() {
        return accountingIsSetup;
    }

    public void setAccountingIsSetup(boolean accountingIsSetup) {
        this.accountingIsSetup = accountingIsSetup;
    }

    public boolean isEnableAccountingModule() {
        return enableAccountingModule;
    }

    public void setEnableAccountingModule(boolean enableAccountingModule) {
        this.enableAccountingModule = enableAccountingModule;
    }

    public boolean isSalesIsSetup() {
        return salesIsSetup;
    }

    public void setSalesIsSetup(boolean salesIsSetup) {
        this.salesIsSetup = salesIsSetup;
    }

    public Integer getAccountingCalculationScale() {
        return accountingCalculationScale;
    }

    public void setAccountingCalculationScale(Integer accountingCalculationScale) {
        this.accountingCalculationScale = accountingCalculationScale;
    }

    public Integer getAccountingCustomQtyScale() {
        return accountingCustomQtyScale;
    }

    public void setAccountingCustomQtyScale(Integer accountingCustomQtyScale) {
        this.accountingCustomQtyScale = accountingCustomQtyScale;
    }

    public Integer getAccountingCustomPriceScale() {
        return accountingCustomPriceScale;
    }

    public void setAccountingCustomPriceScale(Integer accountingCustomPriceScale) {
        this.accountingCustomPriceScale = accountingCustomPriceScale;
    }

    public Integer getAccountingCustomExRateScale() {
        return accountingCustomExRateScale;
    }

    public void setAccountingCustomExRateScale(Integer accountingCustomExRateScale) {
        this.accountingCustomExRateScale = accountingCustomExRateScale;
    }

    public String getTransactionLockDate() {
        return transactionLockDate;
    }

    public void setTransactionLockDate(String transactionLockDate) {
        this.transactionLockDate = transactionLockDate;
    }

    public boolean isSalesLocked() {
        return isSalesLocked;
    }

    public void setSalesLocked(boolean salesLocked) {
        isSalesLocked = salesLocked;
    }

    public boolean isPurchasesLocked() {
        return isPurchasesLocked;
    }

    public void setPurchasesLocked(boolean purchasesLocked) {
        isPurchasesLocked = purchasesLocked;
    }

    public boolean isBankingLocked() {
        return isBankingLocked;
    }

    public void setBankingLocked(boolean bankingLocked) {
        isBankingLocked = bankingLocked;
    }

    public boolean isEmployeesLocked() {
        return isEmployeesLocked;
    }

    public void setEmployeesLocked(boolean employeesLocked) {
        isEmployeesLocked = employeesLocked;
    }

    public boolean isAttendanceLocked() {
        return isAttendanceLocked;
    }

    public void setAttendanceLocked(boolean attendanceLocked) {
        isAttendanceLocked = attendanceLocked;
    }

    public boolean isRecruitmentLocked() {
        return isRecruitmentLocked;
    }

    public void setRecruitmentLocked(boolean recruitmentLocked) {
        isRecruitmentLocked = recruitmentLocked;
    }

    public boolean isPayslipsLocked() {
        return isPayslipsLocked;
    }

    public void setPayslipsLocked(boolean payslipsLocked) {
        isPayslipsLocked = payslipsLocked;
    }

    public boolean isCashAdvancesLocked() {
        return isCashAdvancesLocked;
    }

    public void setCashAdvancesLocked(boolean cashAdvancesLocked) {
        isCashAdvancesLocked = cashAdvancesLocked;
    }

    public boolean isAdditionalPaymentsLocked() {
        return isAdditionalPaymentsLocked;
    }

    public void setAdditionalPaymentsLocked(boolean additionalPaymentsLocked) {
        isAdditionalPaymentsLocked = additionalPaymentsLocked;
    }

    public boolean isPmIsSetup() {
        return pmIsSetup;
    }

    public void setPmIsSetup(boolean pmIsSetup) {
        this.pmIsSetup = pmIsSetup;
    }

    public boolean isSetupSubProject() {
        return isSetupSubProject;
    }

    public void setSetupSubProject(boolean setupSubProject) {
        isSetupSubProject = setupSubProject;
    }

    public boolean isSetupSubProjectTwoLevel() {
        return isSetupSubProjectTwoLevel;
    }

    public void setSetupSubProjectTwoLevel(boolean setupSubProjectTwoLevel) {
        isSetupSubProjectTwoLevel = setupSubProjectTwoLevel;
    }

    public String getInvFirst() {
        return invFirst;
    }

    public void setInvFirst(String invFirst) {
        this.invFirst = invFirst;
    }

    public String getPmFirst() {
        return pmFirst;
    }

    public void setPmFirst(String pmFirst) {
        this.pmFirst = pmFirst;
    }

    public String getPaFirst() {
        return paFirst;
    }

    public void setPaFirst(String paFirst) {
        this.paFirst = paFirst;
    }

    public String getAvaFirst() {
        return avaFirst;
    }

    public void setAvaFirst(String avaFirst) {
        this.avaFirst = avaFirst;
    }

    public String getLocaleString() {
        return localeString;
    }

    public void setLocaleString(String localeString) {
        this.localeString = localeString;
    }

    public boolean isLiveChatActive() {
        return isLiveChatActive;
    }

    public void setLiveChatActive(boolean liveChatActive) {
        isLiveChatActive = liveChatActive;
    }

    public boolean isExpertChatActive() {
        return isExpertChatActive;
    }

    public void setExpertChatActive(boolean expertChatActive) {
        isExpertChatActive = expertChatActive;
    }

    public boolean isMeetingMinutesActive() {
        return isMeetingMinutesActive;
    }

    public void setMeetingMinutesActive(boolean meetingMinutesActive) {
        isMeetingMinutesActive = meetingMinutesActive;
    }

    public boolean isBookingItemsActive() {
        return isBookingItemsActive;
    }

    public void setBookingItemsActive(boolean bookingItemsActive) {
        isBookingItemsActive = bookingItemsActive;
    }

    public String getNotShowingPages() {
        return notShowingPages;
    }

    public void setNotShowingPages(String notShowingPages) {
        this.notShowingPages = notShowingPages;
    }

    public boolean isWorkspaceWelcomePageEnable() {
        return workspaceWelcomePageEnable;
    }

    public void setWorkspaceWelcomePageEnable(boolean workspaceWelcomePageEnable) {
        this.workspaceWelcomePageEnable = workspaceWelcomePageEnable;
    }

    public boolean isEnableWFTMoreMenuForMEM() {
        return enableWFTMoreMenuForMEM;
    }

    public void setEnableWFTMoreMenuForMEM(boolean enableWFTMoreMenuForMEM) {
        this.enableWFTMoreMenuForMEM = enableWFTMoreMenuForMEM;
    }

    public boolean isEnableWFTMoreMenuForADMIN() {
        return enableWFTMoreMenuForADMIN;
    }

    public void setEnableWFTMoreMenuForADMIN(boolean enableWFTMoreMenuForADMIN) {
        this.enableWFTMoreMenuForADMIN = enableWFTMoreMenuForADMIN;
    }

    public boolean isEnableSalesBackend() {
        return enableSalesBackend;
    }

    public void setEnableSalesBackend(boolean enableSalesBackend) {
        this.enableSalesBackend = enableSalesBackend;
    }

    public boolean isEnableSupportBackend() {
        return enableSupportBackend;
    }

    public void setEnableSupportBackend(boolean enableSupportBackend) {
        this.enableSupportBackend = enableSupportBackend;
    }

    public boolean isEnableAdminBackend() {
        return enableAdminBackend;
    }

    public void setEnableAdminBackend(boolean enableAdminBackend) {
        this.enableAdminBackend = enableAdminBackend;
    }

    public boolean isEnablePartnerAdminBackend() {
        return enablePartnerAdminBackend;
    }

    public void setEnablePartnerAdminBackend(boolean enablePartnerAdminBackend) {
        this.enablePartnerAdminBackend = enablePartnerAdminBackend;
    }

    /*  public boolean isEnablePDFBackend() {
        return enablePDFBackend;
    }

    public void setEnablePDFBackend(boolean enablePDFBackend) {
        this.enablePDFBackend = enablePDFBackend;
    }*/

    public boolean isEnableDeveloperBackend() {
        return enableDeveloperBackend;
    }

    public void setEnableDeveloperBackend(boolean enableDeveloperBackend) {
        this.enableDeveloperBackend = enableDeveloperBackend;
    }

    public boolean isEnableMonthlyTimesheet() {
        return enableMonthlyTimesheet;
    }

    public void setEnableMonthlyTimesheet(boolean enableMonthlyTimesheet) {
        this.enableMonthlyTimesheet = enableMonthlyTimesheet;
    }

    public boolean isEnableWorkflowStatus() {
        return enableWorkflowStatus;
    }

    public void setEnableWorkflowStatus(boolean enableWorkflowStatus) {
        this.enableWorkflowStatus = enableWorkflowStatus;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getShortDateFormat() {
        return shortDateFormat;
    }

    public void setShortDateFormat(String shortDateFormat) {
        this.shortDateFormat = shortDateFormat;
    }

    public String getLongDateFormat() {
        return longDateFormat;
    }

    public void setLongDateFormat(String longDateFormat) {
        this.longDateFormat = longDateFormat;
    }

    public String getGoogleAppDomain() {
        return googleAppDomain;
    }

    public void setGoogleAppDomain(String googleAppDomain) {
        this.googleAppDomain = googleAppDomain;
    }

    public boolean isGoogleMarketplaceUsersImportShow() {
        return googleMarketplaceUsersImportShow;
    }

    public void setGoogleMarketplaceUsersImportShow(boolean googleMarketplaceUsersImportShow) {
        this.googleMarketplaceUsersImportShow = googleMarketplaceUsersImportShow;
    }

    public SelectItem[] getRoleItems() {
        return roleItems;
    }

    public void setRoleItems(SelectItem[] roleItems) {
        this.roleItems = roleItems;
    }

    public UsagePlanItem getUsagePlan() {
        return usagePlan;
    }

    public void setUsagePlan(UsagePlanItem usagePlan) {
        this.usagePlan = usagePlan;
    }

    public String getCompanySignedUpFrom() {
        return companySignedUpFrom;
    }

    public void setCompanySignedUpFrom(String companySignedUpFrom) {
        this.companySignedUpFrom = companySignedUpFrom;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isMessageCenterEnabled() {
        return messageCenterEnabled;
    }

    public void setMessageCenterEnabled(boolean messageCenterEnabled) {
        this.messageCenterEnabled = messageCenterEnabled;
    }

    public String getThemeForSystem() {
        return themeForSystem;
    }

    public void setThemeForSystem(String themeForSystem) {
        this.themeForSystem = themeForSystem;
    }

    public String getLanguageForUser() {
        return languageForUser;
    }

    public void setLanguageForUser(String languageForUser) {
        this.languageForUser = languageForUser;
    }

    public String getLatestUploadVersion() {
        return latestUploadVersion;
    }

    public void setLatestUploadVersion(String latestUploadVersion) {
        this.latestUploadVersion = latestUploadVersion;
    }

    public String getSessionLength() {
        return sessionLength;
    }

    public void setSessionLength(String sessionLength) {
        this.sessionLength = sessionLength;
    }

    public Integer getFreeTrialDaysLeft() {
        return freeTrialDaysLeft;
    }

    public void setFreeTrialDaysLeft(Integer freeTrialDaysLeft) {
        this.freeTrialDaysLeft = freeTrialDaysLeft;
    }

    public boolean isPaidCompany() {
        return isPaidCompany;
    }

    public void setPaidCompany(boolean paidCompany) {
        isPaidCompany = paidCompany;
    }

    public String getDefaultCurrencyCODE() {
        return defaultCurrencyCODE;
    }

    public void setDefaultCurrencyCODE(String defaultCurrencyCODE) {
        this.defaultCurrencyCODE = defaultCurrencyCODE;
    }

    public boolean isAutomatic() {
        return isAutomatic;
    }

    public void setAutomatic(boolean automatic) {
        isAutomatic = automatic;
    }

    public boolean isAutomaticApproval() {
        return isAutomaticApproval;
    }

    public void setAutomaticApproval(boolean automaticApproval) {
        isAutomaticApproval = automaticApproval;
    }

    public boolean isValidateTaskStart() {
        return validateTaskStart;
    }

    public void setValidateTaskStart(boolean validateTaskStart) {
        this.validateTaskStart = validateTaskStart;
    }

    public boolean isValidateTaskEnd() {
        return validateTaskEnd;
    }

    public void setValidateTaskEnd(boolean validateTaskEnd) {
        this.validateTaskEnd = validateTaskEnd;
    }

    public String getDefaultCurrentEmployeeTimeSlotStartTIME() {
        return defaultCurrentEmployeeTimeSlotStartTIME;
    }

    public void setDefaultCurrentEmployeeTimeSlotStartTIME(String defaultCurrentEmployeeTimeSlotStartTIME) {
        this.defaultCurrentEmployeeTimeSlotStartTIME = defaultCurrentEmployeeTimeSlotStartTIME;
    }

    public String getDefaultCurrentEmployeeTimeSlotEndTIME() {
        return defaultCurrentEmployeeTimeSlotEndTIME;
    }

    public void setDefaultCurrentEmployeeTimeSlotEndTIME(String defaultCurrentEmployeeTimeSlotEndTIME) {
        this.defaultCurrentEmployeeTimeSlotEndTIME = defaultCurrentEmployeeTimeSlotEndTIME;
    }

    public boolean isValidateTimeslot() {
        return validateTimeslot;
    }

    public void setValidateTimeslot(boolean validateTimeslot) {
        this.validateTimeslot = validateTimeslot;
    }

    public boolean isValidateHoliday() {
        return validateHoliday;
    }

    public void setValidateHoliday(boolean validateHoliday) {
        this.validateHoliday = validateHoliday;
    }

    public boolean isValidateLeaveRequest() {
        return validateLeaveRequest;
    }

    public void setValidateLeaveRequest(boolean validateLeaveRequest) {
        this.validateLeaveRequest = validateLeaveRequest;
    }

    public boolean isTimesheetCommentRequired() {
        return timesheetCommentRequired;
    }

    public void setTimesheetCommentRequired(boolean timesheetCommentRequired) {
        this.timesheetCommentRequired = timesheetCommentRequired;
    }

    public Integer getTimesheetWeekStart() {
        return timesheetWeekStart;
    }

    public void setTimesheetWeekStart(Integer timesheetWeekStart) {
        this.timesheetWeekStart = timesheetWeekStart;
    }

    public Integer getOverallDatePickerWeekStart() {
        return overallDatePickerWeekStart;
    }

    public void setOverallDatePickerWeekStart(Integer overallDatePickerWeekStart) {
        this.overallDatePickerWeekStart = overallDatePickerWeekStart;
    }

    public boolean isValidateMaximumHours() {
        return validateMaximumHours;
    }

    public void setValidateMaximumHours(boolean validateMaximumHours) {
        this.validateMaximumHours = validateMaximumHours;
    }

    public boolean isValidateDayOff() {
        return validateDayOff;
    }

    public void setValidateDayOff(boolean validateDayOff) {
        this.validateDayOff = validateDayOff;
    }

    public Integer getMaximumHours() {
        return maximumHours;
    }

    public void setMaximumHours(Integer maximumHours) {
        this.maximumHours = maximumHours;
    }

    public boolean isValidatePastTimesheet() {
        return validatePastTimesheet;
    }

    public void setValidatePastTimesheet(boolean validatePastTimesheet) {
        this.validatePastTimesheet = validatePastTimesheet;
    }

    public Integer getPastTimesheetDays() {
        return pastTimesheetDays;
    }

    public void setPastTimesheetDays(Integer pastTimesheetDays) {
        this.pastTimesheetDays = pastTimesheetDays;
    }

    public boolean isValidateFutureTimesheet() {
        return validateFutureTimesheet;
    }

    public void setValidateFutureTimesheet(boolean validateFutureTimesheet) {
        this.validateFutureTimesheet = validateFutureTimesheet;
    }

    public Integer getFutureTimesheetDays() {
        return futureTimesheetDays;
    }

    public void setFutureTimesheetDays(Integer futureTimesheetDays) {
        this.futureTimesheetDays = futureTimesheetDays;
    }

    public boolean isShowCompletedTasks() {
        return showCompletedTasks;
    }

    public void setShowCompletedTasks(boolean showCompletedTasks) {
        this.showCompletedTasks = showCompletedTasks;
    }

    public boolean isShowToDoListTasks() {
        return showToDoListTasks;
    }

    public void setShowToDoListTasks(boolean showToDoListTasks) {
        this.showToDoListTasks = showToDoListTasks;
    }

    public boolean isShowHourTypeDropdown() {
        return showHourTypeDropdown;
    }

    public void setShowHourTypeDropdown(boolean showHourTypeDropdown) {
        this.showHourTypeDropdown = showHourTypeDropdown;
    }

    public boolean isEnableMultipleTimerInstances() {
        return enableMultipleTimerInstances;
    }

    public void setEnableMultipleTimerInstances(boolean enableMultipleTimerInstances) {
        this.enableMultipleTimerInstances = enableMultipleTimerInstances;
    }

    public boolean isSaveTimerIntoTimesheetAutomatically() {
        return saveTimerIntoTimesheetAutomatically;
    }

    public void setSaveTimerIntoTimesheetAutomatically(boolean saveTimerIntoTimesheetAutomatically) {
        this.saveTimerIntoTimesheetAutomatically = saveTimerIntoTimesheetAutomatically;
    }

    public void setCityName(String city) {
        this.cityname = city;
    }

    public String getCityName() {
        return this.cityname;
    }

    public void setCountryName(String country) {
        this.countryname = country;
    }

    public String getCountryName() {
        return this.countryname;
    }

    public void setCompanyCountryCode(String companyCountryCode) {
        this.companyCountryCode = companyCountryCode;
    }

    public String getCompanyCountryCode() {
        return this.companyCountryCode;
    }

    public boolean isMultiCompanySubsidiary() {
        return multiCompanySubsidiary;
    }

    public void setMultiCompanySubsidiary(boolean multiCompanySubsidiary) {
        this.multiCompanySubsidiary = multiCompanySubsidiary;
    }

    public boolean isMultiWarehouseEnabled() {
        return multiWarehouseEnabled;
    }

    public void setMultiWarehouseEnabled(boolean multiWarehouseEnabled) {
        this.multiWarehouseEnabled = multiWarehouseEnabled;
    }

    public boolean isPoIgnoreManagerApproval() {
        return poIgnoreManagerApproval;
    }

    public void setPoIgnoreManagerApproval(boolean poIgnoreManagerApproval) {
        this.poIgnoreManagerApproval = poIgnoreManagerApproval;
    }

    public boolean isSupplier() {
        return isSupplier;
    }

    public void setSupplier(boolean supplier) {
        isSupplier = supplier;
    }

    public boolean isSuperUser() {
        return isSuperUser;
    }

    public void setSuperUser(boolean superUser) {
        isSuperUser = superUser;
    }

    public String getTaxName() {
        return taxName;
    }

    public void setTaxName(String taxName) {
        this.taxName = taxName;
    }

    public String getPromotionalCode() {
        return promotionalCode;
    }

    public void setPromotionalCode(String promotionalCode) {
        this.promotionalCode = promotionalCode;
    }

    public void setTimesheetDateFormat(String timesheetDateFormat) {
        this.timesheetDateFormat = timesheetDateFormat;
    }

    public String getTimesheetDateFormat() {
        return timesheetDateFormat;
    }

    public boolean isValidateTimesheetEstimate() {
        return validateTimesheetEstimate;
    }

    public void setValidateTimesheetEstimate(boolean validateTimesheetEstimate) {
        this.validateTimesheetEstimate = validateTimesheetEstimate;
    }

    public Integer getAlternativeCalendarId() {
        return alternativeCalendarId;
    }

    public void setAlternativeCalendarId(Integer alternativeCalendarId) {
        this.alternativeCalendarId = alternativeCalendarId;
    }

    public boolean isEnableTraningCenterView() {
        return enableTraningCenterView;
    }

    public void setEnableTraningCenterView(boolean enableTraningCenterView) {
        this.enableTraningCenterView = enableTraningCenterView;
    }

    public boolean isDoubleMessageEnable() {
        return doubleMessageEnable;
    }

    public void setDoubleMessageEnable(boolean doubleMessageEnable) {
        this.doubleMessageEnable = doubleMessageEnable;
    }

    public boolean isMultipleSalesPriceEnable() {
        return multipleSalesPriceEnable;
    }

    public void setMultipleSalesPriceEnable(boolean multipleSalesPriceEnable) {
        this.multipleSalesPriceEnable = multipleSalesPriceEnable;
    }

    public Integer getAccountingTaxRateScalse() {
        return accountingTaxRateScalse;
    }

    public void setAccountingTaxRateScalse(Integer accountingTaxRateScalse) {
        this.accountingTaxRateScalse = accountingTaxRateScalse;
    }

    public boolean isProductTableCustomizationEnable() {
        return productTableCustomizationEnable;
    }

    public void setProductTableCustomizationEnable(boolean productTableCustomizationEnable) {
        this.productTableCustomizationEnable = productTableCustomizationEnable;
    }

    public boolean isEnableParentDepartment() {
        return enableParentDepartment;
    }

    public void setEnableParentDepartment(boolean enableParentDepartment) {
        this.enableParentDepartment = enableParentDepartment;
    }

    public Integer getAccountingDiscountScale() {
        return accountingDiscountScale;
    }

    public void setAccountingDiscountScale(Integer accountingDiscountScale) {
        this.accountingDiscountScale = accountingDiscountScale;
    }

    public Integer getAccountingProgressinvoiceingAmountScale() {
        return this.accountingProgressinvoiceingAmountScale;
    }

    public void setAccountingProgressinvoiceingAmountScale(final Integer accountingProgressinvoiceingAmountScale) {
        this.accountingProgressinvoiceingAmountScale = accountingProgressinvoiceingAmountScale;
    }

    public boolean isEnableStoreFront() {
        return enableStoreFront;
    }

    public void setEnableStoreFront(boolean enableStoreFront) {
        this.enableStoreFront = enableStoreFront;
    }

    public boolean isEnableToShowSampleData() {
        return enableToShowSampleData;
    }

    public void setEnableToShowSampleData(boolean enableToShowSampleData) {
        this.enableToShowSampleData = enableToShowSampleData;
    }

    public boolean isEnableLogistics() {
        return enableLogistics;
    }

    public void setEnableLogistics(boolean enableLogistics) {
        this.enableLogistics = enableLogistics;
    }

    public boolean isAnyDataMissing() {
        return anyDataMissing;
    }

    public void setAnyDataMissing(boolean anyDataMissing) {
        this.anyDataMissing = anyDataMissing;
    }

    public String getTawkToSiteId() {
        return tawkToSiteId;
    }

    public void setTawkToSiteId(String tawkToSiteId) {
        this.tawkToSiteId = tawkToSiteId;
    }

    public boolean isAccountingSettingsEnabled() {
        return accountingSettingsEnabled;
    }

    public void setAccountingSettingsEnabled(boolean accountingSettingsEnabled) {
        this.accountingSettingsEnabled = accountingSettingsEnabled;
    }

    public boolean isAccountingProductionEnabled() {
        return accountingProductionEnabled;
    }

    public void setAccountingProductionEnabled(boolean accountingProductionEnabled) {
        this.accountingProductionEnabled = accountingProductionEnabled;
    }

    public String getInitialName() {
        return initialName;
    }

    public void setInitialName(String initialName) {
        this.initialName = initialName;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public boolean isEnableSwitchableLayout() {
        return enableSwitchableLayout;
    }

    public void setEnableSwitchableLayout(boolean enableSwitchableLayout) {
        this.enableSwitchableLayout = enableSwitchableLayout;
    }

    public boolean isTestCompany() {
        return testCompany;
    }

    public void setTestCompany(boolean testCompany) {
        this.testCompany = testCompany;
    }

    public String getSideNavStyle() {
        return sideNavStyle;
    }

    public void setSideNavStyle(String sideNavStyle) {
        this.sideNavStyle = sideNavStyle;
    }

    public String getProfileContent() {
        return profileContent;
    }

    public void setProfileContent(String profileContent) {
        this.profileContent = profileContent;
    }

    public boolean isVatRegistered() {
        return vatRegistered;
    }

    public void setVatRegistered(boolean vatRegistered) {
        this.vatRegistered = vatRegistered;
    }

    public boolean isAutomaticWaitingForApproval() {
        return isAutomaticWaitingForApproval;
    }

    public void setAutomaticWaitingForApproval(boolean automaticWaitingForApproval) {
        isAutomaticWaitingForApproval = automaticWaitingForApproval;
    }

    public TimeZone getTimeZone() {
        return this.timeZone;
    }

    public void setTimeZone(final TimeZone timeZone) {
        this.timeZone = timeZone;
    }

    public boolean isEmailAccountSetup() {
        return emailAccountSetup;
    }

    public void setEmailAccountSetup(boolean emailAccountSetup) {
        this.emailAccountSetup = emailAccountSetup;
    }

    public String getVatAccountingBasis() {
        return vatAccountingBasis;
    }

    public void setVatAccountingBasis(String vatAccountingBasis) {
        this.vatAccountingBasis = vatAccountingBasis;
    }
}
