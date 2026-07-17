/**********************************************************************************************************************
 * LAST CHANGE                                                                                                        *
 * User: Hayot                                                                                                        *
 * Time: 2010/5/6 4:27:31                                                                                             *
 **********************************************************************************************************************/

package com.edatasite.workforce.gwt.profile.client.rpc;

import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.core.client.Utils;
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
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public interface ProfileService extends RemoteService {

    PermissionColumnsItem getRolesPermissions(boolean isSuperUser);

    EmailFilter getEmailFilter(String filterType, Integer objectID);

    Integer saveEmailFilter(EmailFilter item);

    ListResult<EmailFilter> getEmailFilters(ListingFilterParameter filterParametr);

    void updateProfile(ProfileItem editProfile);

    ListResult<WorkflowRule> listWorkflowRules(ListingFilterParameter fp);

    WorkflowRule editWorkflowRule(Integer objectID);

    Integer saveWorkflowRule(WorkflowRule item);

    ArrayList<Integer> deleteWorkflows(ArrayList<Integer> objectIds);

    SelectItem[] getCountries();

    SelectItem[] getRegions(Integer countryId);

    SelectItem[] getAccount(Integer imId);

    ProfileItem getProfile();

    SettingsData getCompanyInfoSwitchvox();

    void updateCompanyInfoSwitchvox(SettingsData data);

    void updateCompanyInfo(SettingsData data);

    Boolean deleteCurrentCompany();

    void updateSignUpCompanyInfo(SettingsData dataForSave);

    SelectItem[] getCountryTimezone(Integer countryID);

    SelectItem[] getMultipleCountryTimezones(ArrayList<Integer> countries);

    SettingsData getCompanySettings(boolean isAccountingGettingStarted);

    SelectItem[] getPayFrequencies();

    EmployerSettings getCompanyPayrollSettings();

    SelectItem[] getPdfFonts();

    CompanyOpportunitySettings getCompanyOpportunitySettings();

    SettingsData getInvoiceSettings();

    CredentialsItem getCredentials();

    Boolean saveCredentials(CredentialsItem items);

    String getImageUrl(Integer id);

    SelectItem[] getEmailTemplateCategories(Integer moduleID);

    ArrayList<String> getEmailTemplateCategoryFields(Integer categoryId);

    ArrayList<String> getEmailTemplateModuleAttributes(Integer moduleID);

    Integer createUpdateEmailTemplate(EmailTemplateItem templateItem);

    Integer[] createEmailTemplate(EmailTemplateItem[] templateItems);

    EmailTemplateItem getEmailTemplate(Integer objectId);

    ListResult<EmailTemplateItem> getEmailTemplateList(ListingFilterParameter fp);

    ListResult<SMSTemplateItem> getSMSTemplateList(ListingFilterParameter fp);

    String saveEMLTemplatesWithZIPFile(ListingFilterParameter fp);

    void deleteEmailTemplate(Integer objectId);

    String sendTestEmail(EmailTemplateItem templateItem);

    SelectItem[] getEmailTemplates(String templcatCategoryCode);

    void saveCustomFields(Integer companyID, CompanyCustomFieldItem items, boolean isItemTableField);

    void saveCustomFieldValidations(Integer companyID, CompanyCustomFieldItem item);

    HashMap<Integer, String[]> getExistingCustomFields(Integer companyID, String entityName, Integer relationship);

    HashMap<Integer, String[]> getExistingCustomFields(Integer companyID, String entityName, String entityCategoryName, Integer relationship, Integer objectID);

    void saveCompanyEmailNotifications(HashMap<String, HashSet<SelectItem>> selectedItems, boolean isApplyExistingUsers);

    void saveUserEmailNotifications(HashMap<String, HashSet<SelectItem>> newEventSelectItems);

    HashMap<String, HashSet<SelectItem>> getCompanyEmailNotificationSettings();

    HashMap<String, HashSet<SelectItem>> getUserEmailNotificationSettings();

    void sendTimeSheetReminder(Integer employeeID, Integer recurrenceID, Integer type, String when);

    void saveRecurrenceJob(RecurrenceJobItem item);

    RecurrenceJobItem getJob(Integer jobType, boolean defaultTimeSheetReminder);

    SelectItem[] getCurrentOrSomeUsers();

    SelectItem[] getUsers(boolean isEditForm);

    // Google Tokens
    boolean validateGoogleCalendarUser();

    void deleteGoogleCalendarToken();

    boolean validateGoogleContactUser();

    boolean validateGoogleAnalytics();

    void deleteGoogleContactToken(boolean isOffice);

    boolean validateGoogleDocumentUser();

    void deleteGoogleDocumentsToken();

    PMNumberingSettings getPMNumberingSettings();

    PMNumberingSettings getPayrollNumberingSettings();

    Integer savePMNumberingSettings(PMNumberingSettings settings, String view);

    Integer savePayrollNumberingSettings(PMNumberingSettings settings);

    String getWebContentByUrl(String url);

    ListResult<SmsSettings> getSmsSettingList(ListingFilterParameter fp);

    SmsSettings getSmsSetting(Integer objectID);

    void saveSmsSettings(SmsSettings smsSettings);

    void deleteSMSSettings(Integer objectID);

    void updateCompanyOpportunitySettings(CompanyOpportunitySettings opportunitySettings);

    ListResult<CompanyCustomFieldItem> getCustomFields(ListingFilterParameter filterParameter);

    SelectItem[] getCustomFieldsAsSelectItem(ListingFilterParameter fp);

    void saveCustomFormCustomFieldSettings(CompanyCustomFieldItem item);

    CompanyCustomFieldItem getCustomFieldData(Integer objectID, Integer companyID);

    void deleteCustomField(Integer objectID, Integer companyID);

    void deleteCustomField(Integer objectID, Integer companyID, String form_id);

    LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getEmployees();

    Boolean saveTimesheetRequired(ArrayList<KpiTreeInfo> selectedItems);

    String getProjectLastIntNumber();

    ConsolidationCompanyItem getConsolidationCompanyItems();

    KeyValueStruct saveConsolidationCompany(ConsolidationCompanySaveItem consolidationCompanySaveItem);

    ListResult<ConsolidationCompanyList> getSubsidiariesCompanyList(ListingFilterParameter filterParametrs);

    Boolean activeSubsidiarieCompany(ConsolidationCompanyList rowValue);

    SelectItem[] getCustomFieldDataByQuery(Integer companyID, String query);

    void deleteSignature(Integer objectID);

    ListResult<SignatureItem> getSignatureList(ListingFilterParameter fp);

    Integer saveSignature(SignatureItem signature);

    SignatureItem getSignature(Integer objectId);

    ArrayList<SelectItem> getRoles();

    ListResult<WorkflowAlert> listWorkflowAlerts(ListingFilterParameter filterParametr);

    ListResult<WorkflowSMSAlert> listWorkflowSMSAlerts(ListingFilterParameter filterParametr);

    ListResult<WorkflowTelegramAlert> listWorkflowTelegramAlerts(ListingFilterParameter filterParametr);

    WorkflowAlert editWorkflowAlert(Integer objectID, Integer workflowID);

    WorkflowSMSAlert getWorkflowSMSAlert(Integer objectID, Integer workflowID);

    WorkflowTelegramAlert getWorkflowTelegramAlert(Integer objectID, Integer workflowID);

    Integer deleteWorkflowTelegramAlert(Integer objectID);

    Integer saveWorkflowAlert(WorkflowAlert item);

    void saveWorkflowSMSAlert(WorkflowSMSAlert item);

    void saveWorkflowTelegramAlert(WorkflowTelegramAlert item);

    ListResult<WorkflowUpdateField> listWorkflowUpdateFields(ListingFilterParameter filterParametr);

    ListResult<WorkflowAction> listWorkflowActions(ListingFilterParameter filterParametr);

    Integer saveWorkflowUpdateField(WorkflowUpdateField item);

    Integer saveWorkflowAction(WorkflowAction item);

    WorkflowUpdateField editWorkflowUpdateField(Integer objectID, Integer workflowID);

    WorkflowAction editWorkflowAction(Integer objectID, Integer workflowID);

    ArrayList<ModelField> getModelFields(String formID);

    boolean deleteCertificateType(Integer objectId);

    SMSTemplateItem getSMSTemplateForWorkflow(Integer templateID);

    SMSTemplateItem getSMSTemplate(Integer templateID);

    void saveSMSTemplate(SMSTemplateItem templateItem);

    void deleteSMSTemplate(Integer objectID);

    EmployeeStepItem getWorkflowStep(Integer stepEmployeeID, Integer workflowID);

    void saveWorkflowStep(EmployeeStepItem step);

    void deleteWorkflowStep(Integer stepID);

    void deleteWorkflowSteps(ArrayList<Integer> stepIDs);

    ListResult<EmployeeStepItem> listWorkflowSteps(ListingFilterParameter filterParametr);

    SelectItem[] getStepStatuses(Integer stepID);

    WorkflowRule getWorkflowRuleForEvent(Integer workflowID);

    ListResult<WorkflowPush> getWorkflowPushList(ListingFilterParameter fp);

    WorkflowPush getWorkflowPush(Integer objectID, Integer workflowId);

    void saveWorkflowPush(WorkflowPush pushItem);

    void deleteWorkflowPush(Integer pushID);

    void deleteWorkflowPushes(ArrayList<Integer> ids);

    ListResult<WorkflowRule> getWorkflowActivitiesList(ListingFilterParameter fp);

    void stopUpcomingRecurrence(Integer recurrenceID);

    SelectItem[] getApproverModules(ListingFilterParameter fp);

    WorkflowEmployee getWorkflowEmployee(Integer objectID, Integer workflowID);

    void saveWorkflowEmployee(WorkflowEmployee item);

    ListResult<WorkflowEmployee> getWorkflowEmployeeList(ListingFilterParameter fp);

    void deleteWorkflowEmployees(ArrayList<Integer> ids);

    HashMap<String, Boolean> getEnableUploadTypes();

    RecurrenceJobItem getJob();

    void deleteOfficeToken(String storageType);

    boolean validateOffice365(String storageType);

    WorkflowInvoice getWorkflowInvoice(Integer objectID, Integer workflowID);

    void saveWorkflowInvoice(WorkflowInvoice item);

    ListResult<WorkflowInvoice> getWorkflowInvoiceList(ListingFilterParameter fp);

    void deleteWorkflowInvoice(Integer objectID);

    MagentoSettingsItem getMagentoSettings();

    Integer saveMagentoSettings(MagentoSettingsItem magentoSettings);

    Integer synchronizeWithMagentoCatalog();

    Integer resetMagentoSynchronization();

    void saveIntegrationSettins(IntegrationSettingsItem settingsItem);

    IntegrationSettingsItem getIntegrationSettings();

    SelectItem[] getEntityTypes();

    TestRPC saveLrSettingsItem(LRSettingsItem lrSettingsItem);

    LRSettingsItem getLrSettingsItem();

    ListResult<ImportLogItem> getImportLogs(ListingFilterParameter filterParameter);

    ListResult<MessageItem> getMessages(ListingFilterParameter filterParameter, boolean isWorkflowMessages);

    IntegrationItem getPaymentGatewayItem();

    IntegrationItem getIntegrationItem();

    void saveIntegrationItem(IntegrationItem item);

    ListResult<PropertyItem> getPropertyItems(ListingFilterParameter filterParameter);

    PropertyItem getPropertyItem(Integer objectID, String module);

    Integer saveProperty(PropertyItem item);

    int updatePropertyStatus(Integer id);

    Integer deleteForm(Integer formId);

    void resetFormProperty(PropertyItem item);

    LinkedHashMap<SelectItem, LinkedList<PropertyItem>> loadAllListingsByModule(String section);

    void saveModuleSettings(String section, LinkedHashMap<SelectItem, LinkedList<PropertyItem>> items);

    ListResult<EmployeeListItem> getAsteriskEmployeeList(Integer asteriskSettingId, ListingFilterParameter filterParametrs);

    AsteriskSettings getAsteriskSettings(Integer employeeId, Integer asteriskSettingsId);

    Integer saveEmployeeAsteriskSettings(AsteriskSettings item, Boolean active);

    SelectItem saveNewTab(String section, String sectionLabel);

    void deleteTab(Integer id, String section);

    void renameTabName(String value, Integer id);

    ListResult<RolePermissionHistoryItem> getPermissionLogHistoryList(ListingFilterParameter listingFilterParameter);

    SelectItem getModuleLocalizeData(String section);

    void renameModuleName(String value, String section);

    ListResult<WorkflowWebHookListItem> getWorkflowWebHooks(ListingFilterParameter fp);

    WorkflowWebHookItem getWorkflowWebHook(WebhookRequestItem item);

    void saveWorkflowWebHook(WorkflowWebHookItem item);

    ListResult<SelectItem> getWebHookResponses(ListingFilterParameter fp);

    ListResult<SelectItem> getWebHookResponsesByType(Integer typeId, String type);

    WorkflowTelegramAlert getWorkflowTelegramAlert();

    void saveRecruitmentIntegrationItem(RecruitmentIntegrationItem item);

    RecruitmentIntegrationItem getRecruitmentIntegrationItem();

    void retryWebhook(Integer id);

    PredefinedValueItem getPredefinedValueRoles(Integer customFieldId, String value);

    void savePredefinedValueRoles(PredefinedValueItem item);

    void savePayrollZone(SelectItem item);

    void deletePayrollZone(Integer id);

    SelectItem getPayrollZone(Integer id);

    ListResult<SelectItem> getMinimumWages(ListingFilterParameter fp);

    void saveMinimumWage(SelectItem item);

    void deleteMinimumWage(Integer id);

    SelectItem getMinimumWage(Integer id);

    ListResult<SelectItem> getWageRates(ListingFilterParameter fp);

    void saveWageRate(SelectItem item);

    void deleteWageRate(Integer id);

    SelectItem getWageRate(Integer id);

    ArrayList<UserCompanyDTO> getUserCompanies();

    void updateNameFormat(ArrayList<Integer> ids, String nameFormat);

    String updateDidoxCredentials(String inn, String password) throws Exception;

    String getDidoxInn();

    class App {
        public static ProfileServiceAsync get() {
            ServiceDefTarget target = GWT.create(ProfileService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/profile");
            return (ProfileServiceAsync) target;
        }
    }
}
