package com.edatasite.workforce.gwt.backend.client.rpc;

import com.edatasite.workforce.gwt.accounting.client.rpc.AccountTypesByCategory;
import com.edatasite.workforce.gwt.accounting.client.rpc.AddAccountItem;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxData;
import com.edatasite.workforce.gwt.accounting.client.rpc.TaxList;
import com.edatasite.workforce.gwt.backend.client.exceptions.CustomException;
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
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;

public interface BackendServiceAsync {

    void saveLastUploadDetails(CompanyItem uploadDetails, AsyncCallback<Void> callback);

    Request getCompanies(Boolean isCount, ListingFilterParameter fp, AsyncCallback<CompanyList> async);

    void getOverallStatistics(AsyncCallback<Statistics> async);

    void getSignupsRate(String days, AsyncCallback<SignupsRate> async);

    void getCountryList(String days, AsyncCallback<CountryList> async);

    void getIndustryList(String days, AsyncCallback<IndustryList> async);

    void markasTestCompany(Integer companyID, AsyncCallback<Void> async);

    void getCompanyEmail(Integer companyID, AsyncCallback<String> async);

    void sendMessage(String to, String subject, String text, AsyncCallback<Void> async);

    void updateCompany(Integer companyID, AsyncCallback<Void> async);

    void resendActivationLink(Integer companyID, AsyncCallback<Void> async);

    void resendEmployeesActivationLink(Integer companyID, AsyncCallback<Void> async);

    void disableAccount(Integer companyID, AsyncCallback<Void> async);

    void getActivationLinkList(Integer companyID, AsyncCallback<ActivationLinkList> async);

    void removetestmails(String s, AsyncCallback<String[]> async);

    Request getSubscriptiontype(ListLoadConfig config, AsyncCallback<SubscriptionList> async);

    void editSubscription(EditSubscription subscription, AsyncCallback<Void> async);

    void deleteSubscription(Integer objectID, AsyncCallback<Void> async);

    void SubscriptiontypeById(Integer objectID, AsyncCallback<String[]> async);

    Request getPaypalReceiptsList(ListingFilterParameter fp, AsyncCallback<ListResult<PaypalReceiptsListItem>> async);

    Request getBugLists(ListingFilterParameter fp, ListLoadConfig config, AsyncCallback<BugList> async);

    Request getBugsPerEmployees(ListingFilterParameter fp, AsyncCallback<ListResult<BugsPerEmployeesListItem>> async);

    Request getBugsPerSections(ListingFilterParameter fp, AsyncCallback<ListResult<BugsPerEmployeesListItem>> async);

    Request getAccessLog(ListingFilterParameter fp, AsyncCallback<ListResult<AccessLogListItem>> async);

    Request getBackendManagementList(ListingFilterParameter fp, AsyncCallback<ListResult<BackendManagementListItem>> async);

    void saveBackendManagement(BackendManagementListItem managementListItem, AsyncCallback<Void> callback);

    void deleteBackendManagement(Integer objectID, AsyncCallback<Void> callback);

    void getBugPriority(AsyncCallback<SelectItem[]> async);

    void getBugStatus(AsyncCallback<SelectItem[]> async);

    void getBugLabel(AsyncCallback<SelectItem[]> callback);

    void getEmployees(AsyncCallback<SelectItem[]> async);

    void getCompanyEmployees(Integer companyID, AsyncCallback<SelectItem[]> callback);

    void getCompanyHosts(AsyncCallback<SelectItem[]> callback);

    void updateBugReport(String bugId, String bugPriority, String bugStatus, String bugLabel, Integer assignId, String assignName, String comment, AsyncCallback<Void> async);

    void setBugComment(String bugID, String comment, AsyncCallback<Void> async);

    void updateCompanyAsTest(TestCompanyItem[] item, AsyncCallback<String> async);

    void getAccountTypes(AsyncCallback<AccountTypesByCategory> async);

    void getCompanysVatList(AsyncCallback<TaxList> async);

    void createAccount(AddAccountItem accountItem, AsyncCallback<Void> async);

    void getCountries(AsyncCallback<SelectItem[]> async);

    void saveTaxRate(TaxData data, AsyncCallback<Void> async);

    void isEmployeeFromWFT(AsyncCallback<Boolean> async);

    Request getValidUsers(ListingFilterParameter fp, AsyncCallback<ListResult<AccountManagementListItem>> async);

    void changeAccountStatus(Integer companyID, Integer userID, Boolean active, AsyncCallback<Void> async);

    void killUserSessions(Integer companyID, Integer userID, AsyncCallback<Void> async);

    void activateAndSendMessage(MessageItem item, Integer userId, AsyncCallback<Void> async);

    void isUserExist(String email, AsyncCallback<Boolean> async);

    Request getBlackList(ListingFilterParameter fp, ListLoadConfig config, AsyncCallback<SelectItem[]> async);

    void deleteBlackListById(Integer blackListId, AsyncCallback<Void> async);

    void saveBlackEmails(String[] emails, AsyncCallback<Void> async);

    void changeAccountPassword(AccountManagementListItem item, String password, AsyncCallback<Void> async);

    void changeAccountUserName(Integer userID, Integer companyID, Integer userCompanyID, String userName, String oldUsername, AsyncCallback<String> callback);

    Request getSubscriptions(ListingFilterParameter fp, AsyncCallback<ListResult<SubscriptionManagementItem>> async);

    void getCompanySubscriptionManagementItem(Integer companyID, AsyncCallback<SubscriptionManagementItem> callback);

    void getCurrentSubscriptionPlan(Integer companyId, AsyncCallback<UsagePlanItem> async);

    void extentSubscriptionPlanAndActivateCompany(UsagePlanItem usagePlanItem, AsyncCallback<Void> async);

    void reindexProject(Integer projectId, AsyncCallback<Void> async) throws CustomException;

    void clenupCompanyUsersMembership(Integer companyID, AsyncCallback<Void> async);

    void clenupUserMembership(Integer companyID, AsyncCallback<Void> async);

    void reindexCompanyTasks(SolrReindexRpc solrReindexRpc, AsyncCallback<Void> async);

    void stealContacts(String fileName, AsyncCallback<Void> async);

    void clenupUserMembershipGroupsIncosistency(Integer companyid, AsyncCallback<Void> async);

    void cleanDublicateTrustees(AsyncCallback<Void> async);

    Request getPlagins(ListingFilterParameter fp, AsyncCallback<ListResult<WFTPlaginListItem>> callback);

    void updatePlaginItem(WFTPlaginListItem plaginListItem, AsyncCallback<Void> callback);

    void getContactPrivelegiesItem(Integer companyID, AsyncCallback<ContactPrivelegiesItem> callback);

    void saveContactPrivelegies(boolean isPrivate, Integer companyID, AsyncCallback<Void> callback);

    void getUsagePlanListByCompany(Integer companyID, AsyncCallback<SimpleUsagePlanItem[]> callback);

    void getUsagePlanItem(Integer usagePlanID, AsyncCallback<SimpleUsagePlanItem> callback);

    void getItemsByParent(String parentCode, AsyncCallback<SelectItem[]> callback);

    void getLastUsagePlanEndDate(Integer companyId, AsyncCallback<Date> callback);

    void saveUsagePlan(SimpleUsagePlanItem simpleUsagePlanItem, AsyncCallback<Void> callback);

    void getMoreMenuItems(Integer companyID, AsyncCallback<MoreMenuUpdateItem> callback);

    void saveMoreMenuItems(SelectItem[] selectItems, Integer companyID, AsyncCallback<Void> callback);

    void saveEnableWFTMoreMenu(boolean forMembers, boolean forAdmin, Integer companyID, AsyncCallback<Void> callback);

    void getCompanyShownWFTFooterPDFs(Integer companyId, AsyncCallback<ContactPrivelegiesItem> callback);

    void saveCompanyIsShownWFTFooter(ContactPrivelegiesItem companyItem, AsyncCallback<Void> callback);

    void analyzeTaskSolrDbconsistence(AsyncCallback<Void> callback);

    void analyzeTaskSolrDbconsistence(Integer companyID, AsyncCallback<Void> callback);

    void analyzeChartOfAccountInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void analyzeLeavRequestInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void analyzeTaskDbSolrConsistency(AsyncCallback<Void> callback);

    void analyzeTaskDbSolrConsistency(Integer companyID, AsyncCallback<Void> callback);

    void getInconsistencyStatistic(Integer companyID, AsyncCallback<SolrDbInconsistencyList> callback);

    void deleteFixedInconsistencies(Integer companyID, AsyncCallback<Void> callback);

    void deleteFixedInconsistencesForAllCompanies(AsyncCallback<Void> callback);

    Request getSchemas(ListingFilterParameter filterParametrs, AsyncCallback<SchemaList> async);

    void removeCompany(Integer companyID, AsyncCallback<Boolean> async);

    void createSchemas(Integer count, AsyncCallback<Integer> async);

    void applyPatch(String schemaName, String excludeSchemas, String query, AsyncCallback<String> async);

    void getSchemasAsSelectItem(ListingFilterParameter filterParameter, AsyncCallback<SelectItem[]> async);

    void exportSchema(String schemaName, AsyncCallback<Boolean> async);

    void getSchemaFacetFilterData(FacetFilterRpc schemaFacet, AsyncCallback<FacetFilterRpc> callback);

    void indexCompanyLeads(SolrReindexRpc solrReindex, AsyncCallback<Void> async);

    void indexCompanyCandidates(SolrReindexRpc solrReindex, AsyncCallback<Void> async);

    void indexCompanyContacts(SolrReindexRpc solrReindex, AsyncCallback<Void> async);

    void indexCompanyCrmAccounts(SolrReindexRpc solrReindex, AsyncCallback<Void> async);

    void indexCompanyNews(SolrReindexRpc solrReindex, AsyncCallback<Void> async);

    void indexCompanyFolders(SolrReindexRpc solrReindex, AsyncCallback<Void> async);

    void indexCompanyFiles(SolrReindexRpc solrReindex, AsyncCallback<Void> async);

    void indexSaleInvoice(SolrReindexRpc solrReindex, AsyncCallback<Void> async);

    void indexSaleQuote(SolrReindexRpc solrReindex, AsyncCallback<Void> async);

    void indexPurchaseOrder(SolrReindexRpc solrReindex, AsyncCallback<Void> async);

    void indexOpportunities(SolrReindexRpc solrReindex, AsyncCallback<Void> async);

    void indexEvents(SolrReindexRpc solrReindex, AsyncCallback<Void> async);

    void indexProductsServices(SolrReindexRpc solrRenidex, AsyncCallback<Void> async);

    void indexCourseSchedule(SolrReindexRpc solrRenidex, AsyncCallback<Void> async);

    void indexEmployee(SolrReindexRpc solrRenidex, AsyncCallback<Void> async);

    void indexSinglePayrun(SolrReindexRpc solrRenidex, AsyncCallback<Void> async);

    void indexGroupPayrun(SolrReindexRpc solrRenidex, AsyncCallback<Void> async);

    void indexCashAdvance(SolrReindexRpc solrRenidex, AsyncCallback<Void> async);

    void indexAdditionalPayment(SolrReindexRpc solrRenidex, AsyncCallback<Void> async);

    void indexPurchaseInvoice(SolrReindexRpc solrReindex, AsyncCallback<Void> async);

    void indexExpenseReportClaims(SolrReindexRpc solrReindex, AsyncCallback<Void> async);

    void indexShippingData(SolrReindexRpc solrReindex, AsyncCallback<Void> async);

    void indexRFQ(SolrReindexRpc solrReindex, AsyncCallback<Void> async);

    void indexCompanyProjects(SolrReindexRpc solrRendex, AsyncCallback<Void> async);

    void indexChartOfAccount(SolrReindexRpc solrRenidex, AsyncCallback<Void> async);

    void indexLeaveRequest(SolrReindexRpc solrRenidex, AsyncCallback<Void> async);

    void indexCustomFormItems(SolrReindexRpc solrRenidex, AsyncCallback<Void> async);

    void getInconsistencyStatistic(Integer companyID, String entryType, AsyncCallback<SolrInconsistencyList> async);

    void analyzeTaskInconsistency(Integer compnayID, AsyncCallback<Void> async);

    void fixContactInconsistency(Integer companyID, AsyncCallback<Void> async);

    void fixCrmAccountInconsistency(Integer companyID, AsyncCallback<Void> async);

    void fixLeadInconsistency(Integer companyID, AsyncCallback<Void> async);

    void fixCandidateInconsistency(Integer companyID, AsyncCallback<Void> async);

    void analyzeLeadInconsistencies(Integer companyID, AsyncCallback<Void> async);

    void analyzeCandidateInconsistencies(Integer companyID, AsyncCallback<Void> async);

    void analyzeContactInconsistencies(Integer companyID, AsyncCallback<Void> async);

    void analyzeCrmAccountInconsistencies(Integer companyID, AsyncCallback<Void> async);

    void fixFileIncosistencies(Integer companyID, AsyncCallback<Void> async);

    void analyzeFileInconsistencies(Integer companyID, AsyncCallback<Void> async);

    void analyzeNewsInconsistencies(Integer companyID, AsyncCallback<Void> async);

    void fixNewsIncosistencies(Integer companyID, AsyncCallback<Void> async);

    /*void indexCompanyNetwork(Integer companyId, AsyncCallback<Void> async);*/

    void fixProjectIncosistencies(Integer companyID, AsyncCallback<Void> callbackFixInconsistencies);

    void analyzeProjectInconsistencies(Integer companyID, AsyncCallback<Void> callbackAnalyzeInconsistencies);

    void analyzeInvoiceInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void analyzeQuoteInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void analyzePurchaseOrderInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void analyzeOpportunityInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void analyzeEventInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void analyzeProductsServicesInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void analyzeCourseSchedulesInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void analyzeCourseBookingInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void analyzePurchaseInvoiceInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void analyzeExpenseReportClaimsInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void analyzeEmployeeInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void analyzeSinglePayrunInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void analyzeGroupPayrunInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void analyzeCashAdvanceInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void analyzeAdditionalPaymentInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void fixInvoiceInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void fixQuoteInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void fixPurchaseOrderInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void fixOpportunityInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void fixEventInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void fixProductsServicesInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void fixCourseScheduleInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void fixCourseBookingInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void fixEmployeeInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void fixSinglePayrunInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void fixGroupPayrunInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void fixCashAdvanceInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void fixAdditionalPaymentInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void fixPurchaseInvoiceInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void fixExpenseReportClaimsInconsistency(Integer companyID, AsyncCallback<Void> callback);

    /* void analyzeNetworkInconsistencies(Integer companyID, AsyncCallback<Void> async);*/

    /*void fixNetworkIncosistencies(Integer companyID, AsyncCallback<Void> async);*/

    void fixChartOfAccountInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void fixLeaveRequestInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void fixCustomFormInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void fixShippingDataInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void fixRFQInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void getDynamicLoginList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<DynamicLogin>> callback);

    void getWhiteLabelList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<DynamicLogin>> callback);

    void getInsertPublicData(Integer objectID, AsyncCallback<String> callback);

    Request getCompanyPDFTemplates(ListingFilterParameter filterParameter, AsyncCallback<ListResult<PDFTemplatesListItem>> callback);

    void getCompanyGenericSettings(ListingFilterParameter filterParameter, AsyncCallback<ListResult<GenericSettingsRPC>> callback);

    void enableDisableGenericSettings(Integer companyID, GenericSettingsEnum key, boolean enable, AsyncCallback<Void> callback);

//    void getPDFFonts(AsyncCallback<SelectItem[]> async);
//
//    void getPdfTemplateReferences(AsyncCallback<SelectItem[]> async);

    void saveCompanyPdfTemplate(PDFSettingsTransObject transObject, AsyncCallback<Integer> async);

    void saveAiPhantomPdfTemplate(PDFSettingsTransObject transObject, AsyncCallback<Integer> async);

    void getCompanyPDFSettings(Integer companyID, Integer companyPDFTemplateID, AsyncCallback<PDFSettingsTransObject> async);

    void deletePDFTemplate(Integer companyID, Integer companyPDFTemplateID, AsyncCallback<Boolean> callback);

    void saveInvoiceLogoSize(Integer width, Integer height, Integer companyID, AsyncCallback<String> callback);

    void getCompanyLogoURL(Integer companyID, String logoType, AsyncCallback<String> async);

    void backupSchema(Integer companyID, AsyncCallback<String> callback);

    void createSchemasSecond(Integer count, boolean backupZeroSchema, AsyncCallback<Integer> callback);

    void savePdfLogoSize(Integer width, Integer height, Integer companyID, AsyncCallback<String> async);

    void updateReport(ReportingListItem reportListItem, AsyncCallback<Boolean> callback);

    Request getRecurrenceHistory(ListingFilterParameter filterParametrs, AsyncCallback<RecurrenceLogList> callback);

    Request getServerHistory(ListingFilterParameter filterParametrs, AsyncCallback<RecurrenceLogList> callback);

    Request getRecurrenceJobItems(ListingFilterParameter filterParametrs, AsyncCallback<RecurrenceLogList> callback);

    void fixCrmCaseInconsistencies(Integer companyID, AsyncCallback<Void> callbackFixInconsistencies);

    void indexCompanyCrmCase(SolrReindexRpc solrReindex, AsyncCallback<Void> callbackIndex);

    void analazyCrmCaseInconsistencies(Integer companyID, AsyncCallback<Void> callbackAnalyzeInconsistencies);

    void copyUsagePlansToLoginDispatcher(AsyncCallback<Void> async);

    void registerCompanyToLoginDispatcher(Integer companyId, AsyncCallback<Void> callback);

    void createClientGroupsToClientContactForCompany(Integer companyID, AsyncCallback<Boolean> callback);

    void convertMarketplace(Integer objectID, String googleAppsDomain, AsyncCallback<Void> asyncCallback);

    void getCompanyDomain(Integer objectID, AsyncCallback<String> asyncCallback);

    void getChatActivities(Integer companyId, AsyncCallback<Boolean[]> asyncCallback);

    void saveChatActivities(Integer companyId, boolean isActiveLiveChat, boolean isActiveExpertChat, AsyncCallback<Void> asyncCallback);


    void getBugLists(ListingFilterParameter fp, AsyncCallback<ListResult<BugListItem>> async);

    Request getBlackLists(ListingFilterParameter fp, AsyncCallback<ListResult<SelectItem>> async);

    void getPaypalStatus(ListingFilterParameter listingFilterParameter, AsyncCallback<SelectItem[]> callback);

    void getCompany(Integer companyID, AsyncCallback<CompanyListItem> asyncCallback);

    void registrationChatUsers(Integer id, AsyncCallback asyncCallback);

    void getCompanyStampURL(Integer companyID, String logoType, AsyncCallback<String> async);

    void saveStampLogoSize(Integer width, Integer height, Integer companyID, AsyncCallback<String> async);

    void enabledCompanyPdfStamper(Boolean enabled, Integer companyID, AsyncCallback<Void> async);

    void isPdfStamperEnabled(Integer companyID, AsyncCallback<Boolean> async);

    void clearHostSettings(AsyncCallback<Void> async);

    void getReportsListCollectin(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<ReportsListItem>> failure);

    void runReport(HashSet<ReportsListItem> listItems, Integer companyid, Date testedDate, AsyncCallback callback);

    void runSingleReport(Integer companyID, ReportsListItem reportItem, AsyncCallback callback);

    void setCompanyUnderMaintenance(Integer companyID, AsyncCallback<Void> async);

    void getReportTemplateList(boolean isCustom, ListingFilterParameter filterParameter, AsyncCallback<ListResult<ReportsListItem>> async);

    void getSolrMonitorStatistic(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<SolrMonitorRpc>> async);

    void deleteCompanyInSoreCore(String coreName, Integer companyId, AsyncCallback<Void> abstractAsyncCallback);

    void getSolrCoreByCompanyList(String coreName, ListingFilterParameter filterParametrs, AsyncCallback<ListResult<SelectItem>> async);

    void optimizeSolrCore(String coreName, AsyncCallback<Void> async);

    void saveAccessToken(ApiAccessToken apiAccessToken, AsyncCallback<Boolean> callback);

    Request getAccessTokenList(ListingFilterParameter fp, AsyncCallback<ListResult<ApiAccessToken>> callback);

    Request getAccessTokenByID(Integer objectID, AsyncCallback<ApiAccessToken> callback);

    void exportSavedReports(ListingFilterParameter filterParameter, AsyncCallback<String> asyncCallback);

    void indexCourseBookings(SolrReindexRpc solrReindex, AsyncCallback<Void> async);

    void getWorkspaceList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<SelectItem>> asyncCallback);

    void activeDraggableWorkspace(ListingFilterParameter filterParametrs, AsyncCallback<Void> asyncCallback);

    void getLink(Integer exceltemplateId, AsyncCallback<String> asyncCallback);

    void indexCompanySystemFolders(SolrReindexRpc solrReindexRpc, AsyncCallback<Void> callback);

    void indexAllCoresOfSelectedCompany(SolrReindexRpc solrReindex, AsyncCallback<String> async);

    Request getCustomForms(ListingFilterParameter filterParameter, AsyncCallback<ListResult<LayoutRPC>> asyncCallback);

    void saveCustomForm(Integer companyID, LayoutRPC formRpc, AsyncCallback<Integer> asyncCallback);

    void getCustomForm(Integer companyID, Integer customFormID, AsyncCallback<LayoutRPC> asyncCallback);

    void applyToMultiDBReportTemplate(Integer[] iDs, ArrayList<SelectItem> selectedItems, AsyncCallback<Void> asyncCallback);

    void saveHelpDocument(HelpDocumentItem item, AsyncCallback<Integer> asyncCallback);

    Request getHelpDocumentList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<HelpDocumentItem>> asyncCallback);

    void getHelpDocuments(Integer objectID, AsyncCallback<HelpDocumentItem> asyncCallback);

    void deleteHelpDocument(Integer objectID, AsyncCallback<Boolean> asyncCallback);

    void isExistHelpDocument(Integer objectID, String form, String block, AsyncCallback<Boolean> asyncCallback);

    void getLocalizations(ListingFilterParameter filter, String code, String untranslatedField, AsyncCallback<ListResult<LocalizationItem>> asyncCallback);

    void getLocalization(Integer id, AsyncCallback<LocalizationItem> asyncCallback);

    void saveLocalization(LocalizationItem item, AsyncCallback<Boolean> asyncCallback);

    void getLocalizationPermission(AsyncCallback<ListResult<LocalizationPermissionItem>> asyncCallback);

    void saveLocalizationPermission(String str, Boolean permission, AsyncCallback<Boolean> asyncCallback);

    void getCompanyLocalizationPermissions(AsyncCallback<LocalizationPermissionItem> asyncCallback);

    void getPropertyItems(AsyncCallback<SelectItem[]> asyncCallback);

    void backupCompanyDocuments(Integer companyID, AsyncCallback<String> asyncCallback);

    void getCompanyActiveUsers(Integer companyID, AsyncCallback<SelectItem[]> abstractAsyncCallback);

    void getCompanyStatisticList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<CompanyListItem>> asyncCallback);

    void updateCompaniesStatistic(AsyncCallback<Integer> async);

    void getBackendManagement(Integer objectID, AsyncCallback<BackendManagementListItem> asyncCallback);

    void setPermissionForSavedReports(ListingFilterParameter filterParametrs, AsyncCallback<Void> asyncCallback);

    void synchronizationReporting(Integer companyId, AsyncCallback<String> callback);

    void changeProjectPercents(Integer companyId, boolean toReset, AsyncCallback<Void> callback);

    void removeCompnayID(Integer copamyid, AsyncCallback<Void> callback);

    void indexVacancy(SolrReindexRpc solrReindexRpc, AsyncCallback<Void> callback);

    void fixVacancyInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void analyzeVacancyInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void indexEmployeeStep(SolrReindexRpc solrReindexRpc, AsyncCallback<Void> callback);

    void fixEmployeeStepInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void analyzeEmployeeStepInconsistency(Integer companyID, AsyncCallback<Void> callback);

    void getFingerprintSetup(Integer companyID, AsyncCallback<ArrayList<CompanyDomain>> callback);

    void saveFingerPrintSetup(Integer companyID, ArrayList<CompanyDomain> setupTOs, AsyncCallback<ArrayList<String>> callback);

    void getExpiringCompanyRatio(ListingFilterParameter fp, AsyncCallback<LinkedHashMap<String, String>> asyncCallback);

    void getNotLoggingCompanyRatio(AsyncCallback<LinkedHashMap<String, String>> asyncCallback);

    void localizationUpdateDataBase(AsyncCallback<Void> callback);

    void localizationUpdateResource(AsyncCallback<Void> callback);

    void createAttendaceRawDataRecords(ListingFilterParameter fp, AsyncCallback<Void> callback);

    void startTansferCompanyFile(Integer companyId, SelectItem importType, AsyncCallback<String> ac);

    void importLocalizationPropertyToDB(ImportFile file, AsyncCallback<Void> callback);

    void getFingerPrintDeviceHistoryList(ListingFilterParameter fp, AsyncCallback<ListResult<FingerPrintDeviceStatusHistoryListItem>> async);

    void getDeviceUniqueKeyListMap(ListingFilterParameter fp, AsyncCallback<LinkedHashMap<String, String>> async);

    void analyzeCustomFormInconsistency(Integer companyID, AsyncCallback<Void> callbackcallbackAnalyzeInconsistencies);

    void analyzeShippingDataInconsistency(Integer companyID, AsyncCallback<Void> callbackcallbackAnalyzeInconsistencies);

    void analyzeRFQInconsistency(Integer companyID, AsyncCallback<Void> callbackcallbackAnalyzeInconsistencies);

    void createTemplateSchema(Integer count, AsyncCallback<Integer> callback);

    void getCustomFormItemList(Integer companyId, String module, AsyncCallback<PDFSettingsTransObject> async);

    void indexCertificates(SolrReindexRpc reindexRpc, AsyncCallback<Void> async);

    void indexPositions(SolrReindexRpc reindexRpc, AsyncCallback<Void> async);

    void indexDepartments(SolrReindexRpc reindexRpc, AsyncCallback<Void> async);

    void analyzeCertificatesInconsistency(Integer companyId, AsyncCallback<Void> async);

    void analyzePositionsInconsistency(Integer companyId, AsyncCallback<Void> async);

    void fixCertificatesInconsistency(Integer companyId, AsyncCallback<Void> async);

    void fixPositionsInconsistency(Integer companyId, AsyncCallback<Void> async);

    void fixDepartmentsInconsistency(Integer companyId, AsyncCallback<Void> async);

    void getDynamicLoginItem(String hostName, AsyncCallback<DynamicLogin> async);

    void saveDynamicLogin(DynamicLogin item, AsyncCallback<String> async);

    void saveWhiteLabelItems(DynamicLogin item, AsyncCallback<Void> async);

    void getHosts(AsyncCallback<SelectItem[]> asyncCallback);

    void analyzeDepartmentsInconsistency(Integer companyId, AsyncCallback<Void> async);

    void getWhiteLabelItem(String hostName, AsyncCallback<DynamicLogin> async);

    void runSchemaUpdate(String args[], AsyncCallback<Void> async);
}
