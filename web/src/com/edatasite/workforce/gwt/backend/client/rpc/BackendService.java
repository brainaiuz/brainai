package com.edatasite.workforce.gwt.backend.client.rpc;

import com.edatasite.workforce.gwt.accounting.client.rpc.AccountTypesByCategory;
import com.edatasite.workforce.gwt.accounting.client.rpc.AddAccountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxData;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxList;
import com.edatasite.workforce.gwt.backend.client.exceptions.CustomException;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.GenericSettingsEnum;
import com.edatasite.workforce.gwt.core.client.rpc.ApiAccessToken;
import com.edatasite.workforce.gwt.core.client.rpc.DynamicLogin;
import com.edatasite.workforce.gwt.core.client.rpc.ReportingListItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.facet.FacetFilterRpc;
import com.edatasite.workforce.gwt.core.client.rpc.form.HelpDocumentItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.form.LocalizationItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LocalizationPermissionItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.solr.SolrReindexRpc;
import com.edatasite.workforce.gwt.core.client.rpc.website.CompanyDomain;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.importfile.client.rpc.ImportFile;
import com.edatasite.workforce.gwt.myaccount.client.rpc.UsagePlanItem;
import com.edatasite.workforce.gwt.profile.client.rpc.MessageItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;

public interface BackendService extends RemoteService {

    void saveLastUploadDetails(CompanyItem uploadDetails);

    CompanyList getCompanies(Boolean isCount, ListingFilterParameter fp);

    Statistics getOverallStatistics();

    SignupsRate getSignupsRate(String days);

    CountryList getCountryList(String days);

    IndustryList getIndustryList(String days);

    void markasTestCompany(Integer companyID);

    String getCompanyEmail(Integer companyID);

    void sendMessage(String to, String subject, String text);

    void updateCompany(Integer companyID);

    void resendActivationLink(Integer companyID);

    void resendEmployeesActivationLink(Integer companyID);

    void disableAccount(Integer companyID);

    void convertMarketplace(Integer companyID, String googleAppsDomain);

    String getCompanyDomain(Integer companyID);

    ActivationLinkList getActivationLinkList(Integer companyID);

    String[] removetestmails(String s);

    SubscriptionList getSubscriptiontype(ListLoadConfig config);

    void editSubscription(EditSubscription subscription);

    void deleteSubscription(Integer objectID);

    String[] SubscriptiontypeById(Integer objectID);

    ListResult<PaypalReceiptsListItem> getPaypalReceiptsList(ListingFilterParameter fp);

    BugList getBugLists(ListingFilterParameter fp, ListLoadConfig config);

    ListResult<BugsPerEmployeesListItem> getBugsPerEmployees(ListingFilterParameter fp);

    ListResult<BugsPerEmployeesListItem> getBugsPerSections(ListingFilterParameter fp);

    ListResult<AccessLogListItem> getAccessLog(ListingFilterParameter fp);

    ListResult<BackendManagementListItem> getBackendManagementList(ListingFilterParameter fp);

    ListResult<FingerPrintDeviceStatusHistoryListItem> getFingerPrintDeviceHistoryList(ListingFilterParameter fp);

    LinkedHashMap<String, String> getDeviceUniqueKeyListMap(ListingFilterParameter fp);

    void saveBackendManagement(BackendManagementListItem managementListItem);

    void deleteBackendManagement(Integer objectID);

    SelectItem[] getBugPriority();

    SelectItem[] getBugStatus();

    SelectItem[] getBugLabel();

    SelectItem[] getEmployees();

    SelectItem[] getCompanyEmployees(Integer companyID);

    SelectItem[] getCompanyHosts();

    void updateBugReport(String bugId, String bugPriority, String bugStatus, String bugLabel, Integer assignId, String assignName, String comment);

    void setBugComment(String bugID, String comment);

    String updateCompanyAsTest(TestCompanyItem[] item);

    AccountTypesByCategory getAccountTypes();

    TaxList getCompanysVatList();

    void createAccount(AddAccountItem accountItem);

    SelectItem[] getCountries();

    void saveTaxRate(TaxData data);

    boolean isEmployeeFromWFT();

    ListResult<AccountManagementListItem> getValidUsers(ListingFilterParameter fp);

    void changeAccountStatus(Integer companyID, Integer userID, Boolean active);

    void killUserSessions(Integer companyID, Integer userID);

    void activateAndSendMessage(MessageItem item, Integer userId);

    boolean isUserExist(String email);

    SelectItem[] getBlackList(ListingFilterParameter fp, ListLoadConfig config);

    void deleteBlackListById(Integer blackListId);

    void saveBlackEmails(String[] emails);

    void changeAccountPassword(AccountManagementListItem item, String password);

    String changeAccountUserName(Integer userID, Integer companyID, Integer userCompanyID, String userName, String oldUserName);

    ListResult<SubscriptionManagementItem> getSubscriptions(ListingFilterParameter fp);

    SubscriptionManagementItem getCompanySubscriptionManagementItem(Integer companyID);

    UsagePlanItem getCurrentSubscriptionPlan(Integer companyId);

    void extentSubscriptionPlanAndActivateCompany(UsagePlanItem usagePlanItem);

    void reindexProject(Integer projectId) throws CustomException;

    void indexCompanyLeads(SolrReindexRpc solrReindex);

    void indexCompanyCandidates(SolrReindexRpc solrReindex);

    void indexCompanyContacts(SolrReindexRpc solrReindex);

    void indexCompanyCrmAccounts(SolrReindexRpc solrReindex);

    void indexCompanyNews(SolrReindexRpc solrReindex);

    void indexCompanyFolders(SolrReindexRpc solrReindex);

    void indexCompanySystemFolders(SolrReindexRpc solrReindexRpc);

    void indexCompanyFiles(SolrReindexRpc solrReindex);

    void indexSaleInvoice(SolrReindexRpc solrReindex);

    void indexSaleQuote(SolrReindexRpc solrReindex);

    void indexPurchaseOrder(SolrReindexRpc solrReindex);

    void indexOpportunities(SolrReindexRpc solrReindex);

    void indexEvents(SolrReindexRpc solrReindex);

    void indexProductsServices(SolrReindexRpc solrRenidex);

    void indexCourseSchedule(SolrReindexRpc solrRenidex);

    void indexEmployee(SolrReindexRpc solrRenidex);

    void indexSinglePayrun(SolrReindexRpc solrRenidex);

    void indexGroupPayrun(SolrReindexRpc solrRenidex);

    void indexCashAdvance(SolrReindexRpc solrRenidex);

    void indexAdditionalPayment(SolrReindexRpc solrRenidex);

    void indexCompanyProjects(SolrReindexRpc solrRendex);

    void indexChartOfAccount(SolrReindexRpc solrReindex);

    void indexLeaveRequest(SolrReindexRpc solrReindex);

    void indexCustomFormItems(SolrReindexRpc solrReindex);

    void indexCertificates(SolrReindexRpc reindexRpc);

    void indexPositions(SolrReindexRpc reindexRpc);

    void indexDepartments(SolrReindexRpc reindexRpc);

    void clenupCompanyUsersMembership(Integer companyID);

    void clenupUserMembership(Integer companyID);

    void reindexCompanyTasks(SolrReindexRpc solrReindexRpc);

    void stealContacts(String fileName);

    void clenupUserMembershipGroupsIncosistency(Integer companyid);

    void cleanDublicateTrustees();

    ListResult<WFTPlaginListItem> getPlagins(ListingFilterParameter fp);

    void updatePlaginItem(WFTPlaginListItem plaginListItem);

    ContactPrivelegiesItem getContactPrivelegiesItem(Integer companyID);

    void saveContactPrivelegies(boolean isPrivate, Integer companyID);

    SimpleUsagePlanItem[] getUsagePlanListByCompany(Integer companyID);

    SimpleUsagePlanItem getUsagePlanItem(Integer usagePlanID);

    SelectItem[] getItemsByParent(String parentCode);

    Date getLastUsagePlanEndDate(Integer companyId);

    void saveUsagePlan(SimpleUsagePlanItem simpleUsagePlanItem);

    MoreMenuUpdateItem getMoreMenuItems(Integer companyID);

    void saveMoreMenuItems(SelectItem[] selectItems, Integer companyID);

    void saveEnableWFTMoreMenu(boolean forMembers, boolean forAdmin, Integer companyID);

    ContactPrivelegiesItem getCompanyShownWFTFooterPDFs(Integer companyId);

    void saveCompanyIsShownWFTFooter(ContactPrivelegiesItem companyItem);

    void analyzeTaskInconsistency(Integer compnayID);

    void analyzeTaskSolrDbconsistence();

    void analyzeTaskSolrDbconsistence(Integer companyID);

    void analyzeChartOfAccountInconsistency(Integer companyID);

    void analyzeLeavRequestInconsistency(Integer companyID);

    void analyzeCustomFormInconsistency(Integer companyID);

    void analyzeShippingDataInconsistency(Integer companyID);

    void analyzeCertificatesInconsistency(Integer companyId);

    void analyzePositionsInconsistency(Integer companyId);

    void analyzeDepartmentsInconsistency(Integer companyId);

    void analyzeTaskDbSolrConsistency();

    void analyzeTaskDbSolrConsistency(Integer companyID);

    void analyzeContactInconsistencies(Integer companyID);

    void fixContactInconsistency(Integer companyID);

    void analyzeCrmAccountInconsistencies(Integer companyID);

    void fixCrmAccountInconsistency(Integer companyID);

    void fixChartOfAccountInconsistency(Integer companyID);

    void fixLeaveRequestInconsistency(Integer companyID);

    void fixCustomFormInconsistency(Integer companyID);

    void fixShippingDataInconsistency(Integer companyID);

    void fixCertificatesInconsistency(Integer companyId);

    void fixPositionsInconsistency(Integer companyId);

    void fixDepartmentsInconsistency(Integer companyId);

    void fixLeadInconsistency(Integer companyID);

    void fixCandidateInconsistency(Integer companyID);

    void analyzeLeadInconsistencies(Integer companyID);

    void analyzeCandidateInconsistencies(Integer companyID);

    void analyzeFileInconsistencies(Integer companyID);

    void analyzeNewsInconsistencies(Integer companyID);

    void fixFileIncosistencies(Integer companyID);

    void fixNewsIncosistencies(Integer companyID);

    void fixProjectIncosistencies(Integer companyID);

    void analyzeProjectInconsistencies(Integer companyID);

    /*    void indexCompanyNetwork(Integer companyId);*/

    /*  void analyzeNetworkInconsistencies(Integer companyID);*/

    /*    void fixNetworkIncosistencies(Integer companyID);*/

    void analyzeInvoiceInconsistency(Integer companyID);

    void analyzeQuoteInconsistency(Integer companyID);

    void analyzePurchaseOrderInconsistency(Integer companyID);

    void analyzeOpportunityInconsistency(Integer companyID);

    void analyzeEventInconsistency(Integer companyID);

    void analyzeProductsServicesInconsistency(Integer companyID);

    void analyzeCourseSchedulesInconsistency(Integer companyID);

    void analyzeCourseBookingInconsistency(Integer companyID);

    void analyzePurchaseInvoiceInconsistency(Integer companyID);

    void analyzeExpenseReportClaimsInconsistency(Integer companyID);

    void analyzeEmployeeInconsistency(Integer companyID);

    void analyzeSinglePayrunInconsistency(Integer companyID);

    void analyzeGroupPayrunInconsistency(Integer companyID);

    void analyzeCashAdvanceInconsistency(Integer companyID);

    void analyzeAdditionalPaymentInconsistency(Integer companyID);

    void fixInvoiceInconsistency(Integer companyID);

    void fixQuoteInconsistency(Integer companyID);

    void fixPurchaseOrderInconsistency(Integer companyID);

    void fixOpportunityInconsistency(Integer companyID);

    void fixEventInconsistency(Integer companyID);

    void fixProductsServicesInconsistency(Integer companyID);

    void fixCourseScheduleInconsistency(Integer companyID);

    void fixCourseBookingInconsistency(Integer companyID);

    void fixEmployeeInconsistency(Integer companyID);

    void fixSinglePayrunInconsistency(Integer companyID);

    void fixGroupPayrunInconsistency(Integer companyID);

    void fixCashAdvanceInconsistency(Integer companyID);

    void fixAdditionalPaymentInconsistency(Integer companyID);

    void fixPurchaseInvoiceInconsistency(Integer companyID);

    void fixExpenseReportClaimsInconsistency(Integer companyID);

    SolrDbInconsistencyList getInconsistencyStatistic(Integer companyID);

    SolrInconsistencyList getInconsistencyStatistic(Integer companyID, String entryType);

    void deleteFixedInconsistencies(Integer companyID);

    void deleteFixedInconsistencesForAllCompanies();

    SchemaList getSchemas(ListingFilterParameter filterParametrs);

    Boolean removeCompany(Integer companyID);

    Integer createSchemas(Integer count);

    String applyPatch(String schemaName, String excludeSchemas, String query);

    SelectItem[] getSchemasAsSelectItem(ListingFilterParameter filterParameter);

    Boolean exportSchema(String schemaName);

    FacetFilterRpc getSchemaFacetFilterData(FacetFilterRpc schemaFacet);

    String getInsertPublicData(Integer objectID);

    ListResult<PDFTemplatesListItem> getCompanyPDFTemplates(ListingFilterParameter filterParameter);

    ListResult<GenericSettingsRPC> getCompanyGenericSettings(ListingFilterParameter filterParameter);

    void enableDisableGenericSettings(Integer companyID, GenericSettingsEnum key, boolean enable);

//    SelectItem[] getPDFFonts();
//
//    SelectItem[] getPdfTemplateReferences();

    Integer saveCompanyPdfTemplate(PDFSettingsTransObject transObject);

    Integer saveAiPhantomPdfTemplate(PDFSettingsTransObject transObject);

    PDFSettingsTransObject getCompanyPDFSettings(Integer companyID, Integer companyPDFTemplateID);

    boolean deletePDFTemplate(Integer companyID, Integer companyPDFTemplateID);

    String saveInvoiceLogoSize(Integer width, Integer height, Integer companyID);

    String savePdfLogoSize(Integer width, Integer height, Integer companyID);

    String getCompanyLogoURL(Integer companyID, String logoType);

    String backupSchema(Integer companyID);

    void setCompanyUnderMaintenance(Integer companyID);

    Integer createSchemasSecond(Integer count, boolean backupZeroSchema) throws IOException;

    boolean updateReport(ReportingListItem reportListItem);

    RecurrenceLogList getRecurrenceHistory(ListingFilterParameter filterParameter);

    RecurrenceLogList getServerHistory(ListingFilterParameter filterParameter);

    RecurrenceLogList getRecurrenceJobItems(ListingFilterParameter filterParameter);

    void fixCrmCaseInconsistencies(Integer companyID);

    void indexCompanyCrmCase(SolrReindexRpc solrReindex);

    void analazyCrmCaseInconsistencies(Integer companyID);

    void copyUsagePlansToLoginDispatcher();

    void registerCompanyToLoginDispatcher(Integer companyId);

    boolean createClientGroupsToClientContactForCompany(Integer companyID);

    Boolean[] getChatActivities(Integer companyId);

    ListResult<BugListItem> getBugLists(ListingFilterParameter fp);

    void saveChatActivities(Integer companyId, boolean isActiveLiveChat, boolean isActiveExpertChat);


    SelectItem[] getPaypalStatus(ListingFilterParameter fp);

    CompanyListItem getCompany(Integer companyID);


    ListResult<SelectItem> getBlackLists(ListingFilterParameter fp);

    void indexPurchaseInvoice(SolrReindexRpc solrReindex);

    void indexExpenseReportClaims(SolrReindexRpc solrReindex);

    void indexShippingData(SolrReindexRpc solrReindex);

    void indexCourseBookings(SolrReindexRpc solrReindex);

    void registrationChatUsers(Integer id);

    String getCompanyStampURL(Integer companyID, String logoType);

    String saveStampLogoSize(Integer width, Integer height, Integer companyID);

    void enabledCompanyPdfStamper(Boolean enabled, Integer companyID);

    Boolean isPdfStamperEnabled(Integer companyID);

    void clearHostSettings();

    ListResult<ReportsListItem> getReportTemplateList(boolean isCustom, ListingFilterParameter filterParameter);

    ListResult<ReportsListItem> getReportsListCollectin(ListingFilterParameter filterParametrs);

    void runReport(HashSet<ReportsListItem> listItems, Integer companyid, Date testedDate);

    void runSingleReport(Integer id, ReportsListItem reportItem);

    ListResult<SolrMonitorRpc> getSolrMonitorStatistic(ListingFilterParameter filterParametrs);

    void deleteCompanyInSoreCore(String coreName, Integer companyId);

    ListResult<SelectItem> getSolrCoreByCompanyList(String coreName, ListingFilterParameter filterParametrs);

    void optimizeSolrCore(String coreName);

    Boolean saveAccessToken(ApiAccessToken apiAccessToken);

    ListResult<ApiAccessToken> getAccessTokenList(ListingFilterParameter fp);

    ApiAccessToken getAccessTokenByID(Integer objectID);

    String exportSavedReports(ListingFilterParameter filterParameter);

    ListResult<SelectItem> getWorkspaceList(ListingFilterParameter filterParametrs);

    void activeDraggableWorkspace(ListingFilterParameter filterParametrs);

    String getLink(Integer exceltemplateId);

    String indexAllCoresOfSelectedCompany(SolrReindexRpc solrReindex);

    ListResult<LayoutRPC> getCustomForms(ListingFilterParameter filterParameter);

    Integer saveCustomForm(Integer companyID, LayoutRPC formRpc);

    LayoutRPC getCustomForm(Integer companyID, Integer customFormID);

    void applyToMultiDBReportTemplate(Integer[] iDs, ArrayList<SelectItem> selectedItems);

    Integer saveHelpDocument(HelpDocumentItem item);

    ListResult<HelpDocumentItem> getHelpDocumentList(ListingFilterParameter filterParameter);

    HelpDocumentItem getHelpDocuments(Integer objectID);

    Boolean deleteHelpDocument(Integer objectID);

    Boolean isExistHelpDocument(Integer objectID, String form, String block);

    ListResult<LocalizationItem> getLocalizations(ListingFilterParameter filter, String code, String untranslatedField);

    LocalizationItem getLocalization(Integer id);

    Boolean saveLocalization(LocalizationItem item);

    ListResult<LocalizationPermissionItem> getLocalizationPermission();

    Boolean saveLocalizationPermission(String str, Boolean permission);

    LocalizationPermissionItem getCompanyLocalizationPermissions();

    SelectItem[] getPropertyItems();

    String backupCompanyDocuments(Integer companyID);

    SelectItem[] getCompanyActiveUsers(Integer companyID);

    ListResult<CompanyListItem> getCompanyStatisticList(ListingFilterParameter filterParameter);

    Integer updateCompaniesStatistic();

    BackendManagementListItem getBackendManagement(Integer objectID);

    void setPermissionForSavedReports(ListingFilterParameter filterParametrs);

    String synchronizationReporting(Integer companyId);

    void changeProjectPercents(Integer companyId, boolean toReset);

    void removeCompnayID(Integer companyId);

    void indexVacancy(SolrReindexRpc solrReindexRpc);

    void fixVacancyInconsistency(Integer companyID);

    void analyzeVacancyInconsistency(Integer companyID);

    void indexEmployeeStep(SolrReindexRpc solrReindexRpc);

    void fixEmployeeStepInconsistency(Integer companyID);

    void analyzeEmployeeStepInconsistency(Integer companyID);

    ArrayList<CompanyDomain> getFingerprintSetup(Integer companyID);

    ArrayList<String> saveFingerPrintSetup(Integer companyID, ArrayList<CompanyDomain> setupTOs);

    LinkedHashMap<String, String> getExpiringCompanyRatio(ListingFilterParameter fp);

    LinkedHashMap<String, String> getNotLoggingCompanyRatio();

    void localizationUpdateDataBase();

    void localizationUpdateResource();

    void createAttendaceRawDataRecords(ListingFilterParameter fp);

    String startTansferCompanyFile(Integer companyId, SelectItem importType);

    void importLocalizationPropertyToDB(ImportFile file);

    Integer createTemplateSchema(Integer count);

    PDFSettingsTransObject getCustomFormItemList(Integer companyId, String module);

    void indexRFQ(SolrReindexRpc solrReindex);

    void analyzeRFQInconsistency(Integer companyID);

    void fixRFQInconsistency(Integer companyID);

    ListResult<DynamicLogin> getDynamicLoginList(ListingFilterParameter filterParameter);

    ListResult<DynamicLogin> getWhiteLabelList(ListingFilterParameter filterParameter);

    DynamicLogin getDynamicLoginItem(String hostname);

    String saveDynamicLogin(DynamicLogin item);

    void saveWhiteLabelItems(DynamicLogin item);

    DynamicLogin getWhiteLabelItem(String hostname);

    SelectItem[] getHosts();

    void runSchemaUpdate(String args[]);

    class App {
        public static BackendServiceAsync get() {
            ServiceDefTarget target = GWT.create(BackendService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/backend");
            return (BackendServiceAsync) target;
        }
    }

    class Reporting {
        public static BackendServiceAsync get() {
            ServiceDefTarget target = GWT.create(BackendService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/backendReporting");
            return (BackendServiceAsync) target;
        }
    }

}
