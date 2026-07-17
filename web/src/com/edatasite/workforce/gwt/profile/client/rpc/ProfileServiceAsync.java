/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/5/6 4:27:31                                                                                             *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.profile.client.rpc;

import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.core.client.rpc.CompanyCustomFieldItem;
import com.edatasite.workforce.gwt.core.client.rpc.EmailTemplateItem;
import com.edatasite.workforce.gwt.core.client.rpc.EmployeeStepItem;
import com.edatasite.workforce.gwt.core.client.rpc.KeyValueStruct;
import com.edatasite.workforce.gwt.core.client.rpc.LRSettingsItem;
import com.edatasite.workforce.gwt.core.client.rpc.PredefinedValueItem;
import com.edatasite.workforce.gwt.core.client.rpc.PropertyItem;
import com.edatasite.workforce.gwt.core.client.rpc.RecurrenceJobItem;
import com.edatasite.workforce.gwt.core.client.rpc.RolePermissionHistoryItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.SignatureItem;
import com.edatasite.workforce.gwt.core.client.rpc.TestRPC;
import com.edatasite.workforce.gwt.core.client.rpc.UserCompanyDTO;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelField;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.EmployerSettings;
import com.edatasite.workforce.gwt.core.client.rpc.sms.SmsSettings;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WebhookRequestItem;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowRule;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowWebHookItem;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowWebHookListItem;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.communication.AsteriskSettings;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowAction;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowAlert;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowEmployee;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowInvoice;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowPush;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowSMSAlert;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowTelegramAlert;
import com.edatasite.workforce.gwt.profile.client.rpc.workflow.WorkflowUpdateField;
import com.edatasite.workforce.gwt.submodule.paymentdeduction.client.SettingsData;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public interface ProfileServiceAsync {

    void getRolesPermissions(boolean isSuperUser, AsyncCallback<PermissionColumnsItem> async);

    void getEmailFilter(String filterType, Integer objectID, AsyncCallback<EmailFilter> async);

    void saveEmailFilter(EmailFilter item, AsyncCallback<Integer> async);

    void getEmailFilters(ListingFilterParameter filterParametr, AsyncCallback<ListResult<EmailFilter>> async);

    void updateProfile(ProfileItem editProfile, AsyncCallback callback);

    void getCountries(AsyncCallback<SelectItem[]> callback);

    void getRegions(Integer countryId, AsyncCallback<SelectItem[]> callback);

    void getAccount(Integer imId, AsyncCallback<SelectItem[]> callback);

    void getProfile(AsyncCallback<ProfileItem> callback);

    void getCompanySettings(boolean isAccountingGettingStarted, AsyncCallback<SettingsData> callback);

    void getCompanyInfoSwitchvox(AsyncCallback<SettingsData> callback);

    void updateCompanyInfoSwitchvox(SettingsData data, AsyncCallback callback);

    void updateCompanyInfo(SettingsData dataForSave, AsyncCallback callback);

    void deleteCurrentCompany(AsyncCallback<Boolean> callback);

    void updateSignUpCompanyInfo(SettingsData dataForSave, AsyncCallback callback);

    void getCountryTimezone(Integer id, AsyncCallback<SelectItem[]> callback);

    void getMultipleCountryTimezones(ArrayList<Integer> countries, AsyncCallback<SelectItem[]> callback);

    void getInvoiceSettings(AsyncCallback<SettingsData> callback);

    void getCredentials(AsyncCallback<CredentialsItem> callback);

    void saveCredentials(CredentialsItem items, AsyncCallback<Boolean> callback);

    void getImageUrl(Integer id, AsyncCallback<String> callback);

    void getEmailTemplateCategories(Integer moduleID, AsyncCallback<SelectItem[]> callback);

    void getEmailTemplateCategoryFields(Integer categoryId, AsyncCallback<ArrayList<String>> callback);

    void getEmailTemplateModuleAttributes(Integer moduleID, AsyncCallback<ArrayList<String>> callback);

    void createUpdateEmailTemplate(EmailTemplateItem templateItem, AsyncCallback<Integer> callback);

    void createEmailTemplate(EmailTemplateItem[] templateItems, AsyncCallback<Integer[]> callback);

    void getEmailTemplate(Integer objectId, AsyncCallback<EmailTemplateItem> callback);

    Request getEmailTemplateList(ListingFilterParameter fp, AsyncCallback<ListResult<EmailTemplateItem>> callback);

    Request getSMSTemplateList(ListingFilterParameter fp, AsyncCallback<ListResult<SMSTemplateItem>> callback);

    void saveEMLTemplatesWithZIPFile(ListingFilterParameter fp, AsyncCallback<String> callback);

    void deleteEmailTemplate(Integer objectId, AsyncCallback<Void> callback);

    void sendTestEmail(EmailTemplateItem templateItem, AsyncCallback<String> callback);

    void getEmailTemplates(String templcatCategoryCode, AsyncCallback<SelectItem[]> callback);

    void saveCustomFields(Integer companyID, CompanyCustomFieldItem items, boolean isItemTableField, AsyncCallback<Void> callback);

    void saveCustomFieldValidations(Integer companyID, CompanyCustomFieldItem item, AsyncCallback<Void> callback);

    void getExistingCustomFields(Integer companyID, String entityName, Integer relationship, AsyncCallback<HashMap<Integer, String[]>> callback);

    void saveCompanyEmailNotifications(HashMap<String, HashSet<SelectItem>> selectedItems, boolean isApplyExistingUsers, AsyncCallback<Void> callback);

    void saveUserEmailNotifications(HashMap<String, HashSet<SelectItem>> newEventSelectItems, AsyncCallback<Void> callback);

    void getCompanyEmailNotificationSettings(AsyncCallback<HashMap<String, HashSet<SelectItem>>> callback);

    void getUserEmailNotificationSettings(AsyncCallback<HashMap<String, HashSet<SelectItem>>> callback);

    void sendTimeSheetReminder(Integer employeeID, Integer recurrenceID, Integer type, String when, AsyncCallback<Void> callback);

    void saveRecurrenceJob(RecurrenceJobItem item, AsyncCallback<Void> callback);

    void getJob(Integer jobType, boolean defaultTimeSheetReminder, AsyncCallback<RecurrenceJobItem> callback);

    void getCurrentOrSomeUsers(AsyncCallback<SelectItem[]> callback);

    void getUsers(boolean isEditForm, AsyncCallback<SelectItem[]> callback);

    void validateGoogleCalendarUser(AsyncCallback<Boolean> async);

    void deleteGoogleCalendarToken(AsyncCallback async);

    void validateGoogleContactUser(AsyncCallback<Boolean> async);

    void validateGoogleAnalytics(AsyncCallback<Boolean> async);

    void deleteGoogleContactToken(boolean isOffice, AsyncCallback async);

    void validateGoogleDocumentUser(AsyncCallback<Boolean> async);

    void deleteGoogleDocumentsToken(AsyncCallback async);

    void getPMNumberingSettings(AsyncCallback<PMNumberingSettings> callback);

    void getPayrollNumberingSettings(AsyncCallback<PMNumberingSettings> callback);

    void savePMNumberingSettings(PMNumberingSettings settings, String view, AsyncCallback<Integer> callback);

    void savePayrollNumberingSettings(PMNumberingSettings settings, AsyncCallback<Integer> callback);

    void getCompanyOpportunitySettings(AsyncCallback<CompanyOpportunitySettings> async);

    void updateCompanyOpportunitySettings(CompanyOpportunitySettings opportunitySettings, AsyncCallback<Void> async);

    void getPayFrequencies(AsyncCallback<SelectItem[]> async);

    void getCompanyPayrollSettings(AsyncCallback<EmployerSettings> async);

    void getPdfFonts(AsyncCallback<SelectItem[]> async);

    void getWebContentByUrl(String url, AsyncCallback<String> asyncCallback);

    Request getSmsSettingList(ListingFilterParameter fp, AsyncCallback<ListResult<SmsSettings>> asyncCallback);

    void getSmsSetting(Integer objectID, AsyncCallback<SmsSettings> asyncCallback);

    void saveSmsSettings(SmsSettings smsSettings, AsyncCallback asyncCallback);

    void deleteSMSSettings(Integer objectID, AsyncCallback asyncCallback);

    Request getCustomFields(ListingFilterParameter filterParameter, AsyncCallback<ListResult<CompanyCustomFieldItem>> async);

    void getCustomFieldsAsSelectItem(ListingFilterParameter fp, AsyncCallback<SelectItem[]> async);

    void saveCustomFormCustomFieldSettings(CompanyCustomFieldItem item, AsyncCallback<Void> async);

    void getCustomFieldData(Integer objectID, Integer companyID, AsyncCallback<CompanyCustomFieldItem> async);

    void getExistingCustomFields(Integer companyID, String entityName, String entityCategoryName, Integer relationship, Integer objectID, AsyncCallback<HashMap<Integer, String[]>> async);

    void deleteCustomField(Integer objectID, Integer companyID, AsyncCallback<Void> async);

    void deleteCustomField(Integer objectID, Integer companyID, String form_id, AsyncCallback<Void> async);

    void getEmployees(AsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>> async);

    void saveTimesheetRequired(ArrayList<KpiTreeInfo> selectedItems, AsyncCallback<Boolean> async);

    void getProjectLastIntNumber(AsyncCallback<String> abstractAsyncCallback);

    void getConsolidationCompanyItems(AsyncCallback<ConsolidationCompanyItem> asyncCallback);

    void saveConsolidationCompany(ConsolidationCompanySaveItem consolidationCompanySaveItem, AsyncCallback<KeyValueStruct> asyncCallback);

    void getSubsidiariesCompanyList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<ConsolidationCompanyList>> asyncCallback);

    void activeSubsidiarieCompany(ConsolidationCompanyList rowValue, AsyncCallback<Boolean> asyncCallback);

    void getCustomFieldDataByQuery(Integer companyID, String query, AsyncCallback<SelectItem[]> callback);

    void deleteSignature(Integer objectID, AsyncCallback<Void> asyncCallback);

    Request getSignatureList(ListingFilterParameter fp, AsyncCallback<ListResult<SignatureItem>> asyncCallback);

    void saveSignature(SignatureItem signature, AsyncCallback<Integer> abstractAsyncCallback);

    void getSignature(Integer objectID, AsyncCallback<SignatureItem> abstractAsyncCallback);

    void getRoles(AsyncCallback<ArrayList<SelectItem>> callback);

    Request listWorkflowRules(ListingFilterParameter fp, AsyncCallback<ListResult<WorkflowRule>> callback);

    void editWorkflowRule(Integer objectID, AsyncCallback<WorkflowRule> callback);

    void saveWorkflowRule(WorkflowRule item, AsyncCallback<Integer> callback);

    void deleteWorkflows(ArrayList<Integer> objectId, AsyncCallback<ArrayList<Integer>> callback);

    void listWorkflowAlerts(ListingFilterParameter filterParametr, AsyncCallback<ListResult<WorkflowAlert>> asyncCallback);

    void listWorkflowSMSAlerts(ListingFilterParameter filterParametr, AsyncCallback<ListResult<WorkflowSMSAlert>> asyncCallback);

    void listWorkflowTelegramAlerts(ListingFilterParameter filterParametr, AsyncCallback<ListResult<WorkflowTelegramAlert>> asyncCallback);

    void editWorkflowAlert(Integer objectID, Integer workflowID, AsyncCallback<WorkflowAlert> abstractAsyncCallback);

    void getWorkflowSMSAlert(Integer objectID, Integer workflowID, AsyncCallback<WorkflowSMSAlert> abstractAsyncCallback);

    void getWorkflowTelegramAlert(Integer objectID, Integer workflowID, AsyncCallback<WorkflowTelegramAlert> abstractAsyncCallback);

    void deleteWorkflowTelegramAlert(Integer objectID, AsyncCallback<Integer> abstractAsyncCallback);

    void saveWorkflowAlert(WorkflowAlert item, AsyncCallback<Integer> abstractAsyncCallback);

    void saveWorkflowSMSAlert(WorkflowSMSAlert item, AsyncCallback<Void> abstractAsyncCallback);

    void saveWorkflowTelegramAlert(WorkflowTelegramAlert item, AsyncCallback<Void> abstractAsyncCallback);

    void listWorkflowUpdateFields(ListingFilterParameter filterParametr, AsyncCallback<ListResult<WorkflowUpdateField>> asyncCallback);

    void listWorkflowActions(ListingFilterParameter filterParametr, AsyncCallback<ListResult<WorkflowAction>> async);

    void saveWorkflowUpdateField(WorkflowUpdateField item, AsyncCallback<Integer> abstractAsyncCallback);

    void saveWorkflowAction(WorkflowAction item, AsyncCallback<Integer> abstractAsyncCallback);

    void editWorkflowUpdateField(Integer objectID, Integer workflowID, AsyncCallback<WorkflowUpdateField> abstractAsyncCallback);

    void editWorkflowAction(Integer objectID, Integer workflowID, AsyncCallback<WorkflowAction> abstractAsyncCallback);

    void getModelFields(String formID, AsyncCallback<ArrayList<ModelField>> abstractAsyncCallback);

    void deleteCertificateType(Integer objectId, AsyncCallback<Boolean> callback);

    void getSMSTemplateForWorkflow(Integer templateID, AsyncCallback<SMSTemplateItem> asyncCallback);

    void getSMSTemplate(Integer templateID, AsyncCallback<SMSTemplateItem> asyncCallback);

    void saveSMSTemplate(SMSTemplateItem templateItem, AsyncCallback<Void> asyncCallback);

    void deleteSMSTemplate(Integer objectID, AsyncCallback<Void> asyncCallback);

    void getWorkflowStep(Integer stepEmployeeID, Integer workflowID, AsyncCallback<EmployeeStepItem> asyncCallback);

    void getStepStatuses(Integer stepID, AsyncCallback<SelectItem[]> asyncCallback);

    void saveWorkflowStep(EmployeeStepItem step, AsyncCallback<Void> asyncCallback);

    void deleteWorkflowStep(Integer stepID, AsyncCallback<Void> asyncCallback);

    void deleteWorkflowSteps(ArrayList<Integer> stepIDs, AsyncCallback<Void> callback);

    void listWorkflowSteps(ListingFilterParameter filterParametr, AsyncCallback<ListResult<EmployeeStepItem>> asyncCallback);

    void getWorkflowRuleForEvent(Integer workflowID, AsyncCallback<WorkflowRule> asyncCallback);

    Request getWorkflowPushList(ListingFilterParameter fp, AsyncCallback<ListResult<WorkflowPush>> asyncCallback);

    void getWorkflowPush(Integer objectID, Integer workflowId, AsyncCallback<WorkflowPush> asyncCallback);

    void saveWorkflowPush(WorkflowPush pushItem, AsyncCallback<Void> asyncCallback);

    void deleteWorkflowPush(Integer pushID, AsyncCallback<Void> asyncCallback);

    void deleteWorkflowPushes(ArrayList<Integer> ids, AsyncCallback<Void> asyncCallback);

    Request getWorkflowActivitiesList(ListingFilterParameter fp, AsyncCallback<ListResult<WorkflowRule>> asyncCallback);

    void stopUpcomingRecurrence(Integer recurrenceID, AsyncCallback<Void> asyncCallback);

    void getApproverModules(ListingFilterParameter filterParameter, AsyncCallback<SelectItem[]> callback);

    Request getWorkflowEmployee(Integer objectID, Integer workflowID, AsyncCallback<WorkflowEmployee> callback);

    void saveWorkflowEmployee(WorkflowEmployee item, AsyncCallback<Void> callback);

    void getWorkflowEmployeeList(ListingFilterParameter fp, AsyncCallback<ListResult<WorkflowEmployee>> asyncCallback);

    void deleteWorkflowEmployees(ArrayList<Integer> ids, AsyncCallback<Void> asyncCallback);

    Request getWorkflowInvoice(Integer objectID, Integer workflowID, AsyncCallback<WorkflowInvoice> callback);

    void saveWorkflowInvoice(WorkflowInvoice item, AsyncCallback<Void> callback);

    void getWorkflowInvoiceList(ListingFilterParameter fp, AsyncCallback<ListResult<WorkflowInvoice>> asyncCallback);

    void deleteWorkflowInvoice(Integer objectID, AsyncCallback<Void> asyncCallback);

    void getEnableUploadTypes(AsyncCallback<HashMap<String, Boolean>> callback);

    void getJob(AsyncCallback<RecurrenceJobItem> callback);

    void deleteOfficeToken(String storageType, AsyncCallback callback);

    void validateOffice365(String storageType, AsyncCallback<Boolean> callback);

    void getMagentoSettings(AsyncCallback<MagentoSettingsItem> callback);

    void saveMagentoSettings(MagentoSettingsItem magentoSettings, AsyncCallback<Integer> callback);

    void synchronizeWithMagentoCatalog(AsyncCallback<Integer> callback);

    void resetMagentoSynchronization(AsyncCallback<Integer> callback);

    void saveIntegrationSettins(IntegrationSettingsItem settingsItem, AsyncCallback<Void> asyncCallback);

    void getIntegrationSettings(AsyncCallback<IntegrationSettingsItem> asyncCallback);

    void getEntityTypes(AsyncCallback<SelectItem[]> callback);

    void saveLrSettingsItem(LRSettingsItem lrSettingsItem, AsyncCallback<TestRPC> callback);

    void getLrSettingsItem(AsyncCallback<LRSettingsItem> callback);

    void getImportLogs(ListingFilterParameter filterParameter, AsyncCallback<ListResult<ImportLogItem>> callback);

    void getMessages(ListingFilterParameter filterParameter, boolean isWorkflowMessages, AsyncCallback<ListResult<MessageItem>> callback);

    void getPaymentGatewayItem(AsyncCallback<IntegrationItem> callback);

    void getIntegrationItem(AsyncCallback<IntegrationItem> callback);

    void saveIntegrationItem(IntegrationItem item, AsyncCallback<Void> callback);

    void getPropertyItems(ListingFilterParameter filterParameter, AsyncCallback<ListResult<PropertyItem>> asyncCallback);

    void getPropertyItem(Integer objectID, String module, AsyncCallback<PropertyItem> asyncCallback);

    void saveProperty(PropertyItem item, AsyncCallback<Integer> asyncCallback);

    void updatePropertyStatus(Integer id, AsyncCallback<Integer> asyncCallback);

    void deleteForm(Integer id, AsyncCallback<Integer> callback);

    void loadAllListingsByModule(String section, AsyncCallback<LinkedHashMap<SelectItem, LinkedList<PropertyItem>>> callback);

    void saveModuleSettings(String section, LinkedHashMap<SelectItem, LinkedList<PropertyItem>> items, AsyncCallback<Void> callback);

    Request getAsteriskEmployeeList(Integer asteriskSettingId, ListingFilterParameter filterParametrs, AsyncCallback<ListResult<EmployeeListItem>> async);

    void getAsteriskSettings(Integer employeeId, Integer asteriskSettingsId, AsyncCallback<AsteriskSettings> asteriskSettingsAsyncCallback);

    void saveEmployeeAsteriskSettings(AsteriskSettings item, Boolean active, AsyncCallback<Integer> integerAsyncCallback);

    void saveNewTab(String section, String sectionLabel, AsyncCallback<SelectItem> async);

    void deleteTab(Integer id, String section, AsyncCallback<Void> async);

    void renameTabName(String value, Integer id, AsyncCallback<Void> async);

    void resetFormProperty(PropertyItem item, AsyncCallback<Void> async);

    void getPermissionLogHistoryList(ListingFilterParameter listingFilterParameter, AsyncCallback<ListResult<RolePermissionHistoryItem>> listResultAsyncCallback);

    void getModuleLocalizeData(String section, AsyncCallback<SelectItem> async);

    void renameModuleName(String value, String section, AsyncCallback<Void> async);

    void getWorkflowWebHooks(ListingFilterParameter fp, AsyncCallback<ListResult<WorkflowWebHookListItem>> async);

    void getWorkflowWebHook(WebhookRequestItem item, AsyncCallback<WorkflowWebHookItem> abstractAsyncCallback);

    void saveWorkflowWebHook(WorkflowWebHookItem item, AsyncCallback<Void> async);

    void getWebHookResponses(ListingFilterParameter fp, AsyncCallback<ListResult<SelectItem>> async);

    void getWorkflowTelegramAlert(AsyncCallback<WorkflowTelegramAlert> workflowTelegramAlertAsyncCallback);

    void getWebHookResponsesByType(Integer typeId, String type, AsyncCallback<ListResult<SelectItem>> async);

    void saveRecruitmentIntegrationItem(RecruitmentIntegrationItem item, AsyncCallback<Void> async);

    void getRecruitmentIntegrationItem(AsyncCallback<RecruitmentIntegrationItem> async);

    void retryWebhook(Integer id, AsyncCallback<Void> async);

    void getPredefinedValueRoles(Integer customFieldId, String value, AsyncCallback<PredefinedValueItem> async);

    void savePredefinedValueRoles(PredefinedValueItem item, AsyncCallback<Void> async);

    void savePayrollZone(SelectItem item, AsyncCallback<Void> async);

    void deletePayrollZone(Integer id, AsyncCallback<Void> async);

    void getPayrollZone(Integer id, AsyncCallback<SelectItem> async);

    void getMinimumWages(ListingFilterParameter fp, AsyncCallback<ListResult<SelectItem>> async);

    void saveMinimumWage(SelectItem item, AsyncCallback<Void> async);

    void deleteMinimumWage(Integer id, AsyncCallback<Void> async);

    void getMinimumWage(Integer id, AsyncCallback<SelectItem> async);

    void getWageRates(ListingFilterParameter fp, AsyncCallback<ListResult<SelectItem>> async);

    void saveWageRate(SelectItem item, AsyncCallback<Void> async);

    void deleteWageRate(Integer id, AsyncCallback<Void> async);

    void getWageRate(Integer id, AsyncCallback<SelectItem> async);

    void getUserCompanies( AsyncCallback<ArrayList<UserCompanyDTO>> async);

    void updateNameFormat(ArrayList<Integer> ids, String format, AsyncCallback<Void> async);

    void updateDidoxCredentials(String inn, String password, AsyncCallback<String> async);

    void getDidoxInn(AsyncCallback<String> async);
}
