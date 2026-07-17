package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactTo;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.ReasonItem;
import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.enums.ColumnType;
import com.edatasite.workforce.gwt.core.client.enums.LeaveReasonType;
import com.edatasite.workforce.gwt.core.client.form.CustomizeFormItem;
import com.edatasite.workforce.gwt.core.client.form.DynamicSectionsRpc;
import com.edatasite.workforce.gwt.core.client.form.IntroductionPageRpc;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.AccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.accounting.BankAccountItem;
import com.edatasite.workforce.gwt.core.client.rpc.currency.CurrencyItem;
import com.edatasite.workforce.gwt.core.client.rpc.dashboard.InOutItem;
import com.edatasite.workforce.gwt.core.client.rpc.emailmessage.UserEmailItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.HelpDocumentItem;
import com.edatasite.workforce.gwt.core.client.rpc.form.LayoutRPC;
import com.edatasite.workforce.gwt.core.client.rpc.form.ModelForm;
import com.edatasite.workforce.gwt.core.client.rpc.googlecalendar.Appointment;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.project.WbsItem;
import com.edatasite.workforce.gwt.core.client.rpc.quickAddSettings.QuickAddColumnConfigs;
import com.edatasite.workforce.gwt.core.client.rpc.quickAddSettings.QuickAddSettingsForm;
import com.edatasite.workforce.gwt.core.client.rpc.resourceUtil.ExportToExcelItem;
import com.edatasite.workforce.gwt.core.client.rpc.resourceUtil.ProjectTaskItem;
import com.edatasite.workforce.gwt.core.client.rpc.resourceUtil.ResourceUtilItem;
import com.edatasite.workforce.gwt.core.client.rpc.resourceUtil.TaskItem;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.RunWebHookRequestItem;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowRule;
import com.edatasite.workforce.gwt.core.client.rpc.workflow.WorkflowWebHookListItem;
import com.edatasite.workforce.gwt.core.client.ui.ListLoadConfig;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomFormItemPdfTemplateList;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionObject;
import com.edatasite.workforce.gwt.core.client.ui.view.PaymentDeductionSelectItem;
import com.edatasite.workforce.gwt.core.client.ui.view.PayrollPdfTemplateList;
import com.edatasite.workforce.gwt.core.client.ui.view.PdfTemplateItemList;
import com.edatasite.workforce.gwt.core.client.ui.view.multiCashAdvance.MultiCashAdvanceItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * User: Hayot
 * Date: Nov 3, 2010
 * Time: 4:54:05 PM
 */
public interface AllInOneService extends RemoteService {

    ArrayList<RoleListItem> getCompanyRoles();

    ArrayList<SelectItem> getRoles();

    ArrayList<SelectItem> getRolesCheckAdmin();

    ArrayList<SelectItem> getAllRoles();

    LayoutRPC getFormData(String formID, String formType);

    SelectItem getLocationByDepartmentId(Integer departmentId);

    ModelForm getModelForm(String formID);

    ModelForm getModelGridForm(String formID);

    LinkedHashMap<String, HashMap<ColumnType, LinkedList<CustomizeFormItem>>> getCustomizeForm(String formID);

    LinkedHashMap<String, LinkedList<CustomizeFormItem>> getCustomizeGridForm(String formID);

    String getModelEntityName(String formID);

    LinkedHashMap<String, DynamicSectionsRpc> getCustomizeFormSections(String formID, boolean isFormCustomizeForm);

    ArrayList<CustomFormLocalization> getCFFLocalizations(String formID);

    void saveCFLItems(LinkedList<CustomFormLocalization> items);

    ApprovalListResult getApprovers(String formID, Integer objectID, boolean isLeaveRequestForm, Integer userID, boolean fromSettings, boolean fromApi, ListingFilterParameter filterParametrs);

    ModelForm getDefaultModelForm(String formID);

    ApprovalListResult getApprovers(String formID, Integer entityID, boolean isLeaveRequestForm, Integer userID, boolean fromSettings);

    void saveApprovers(String formID, String onboardingtype, Integer approveStatusID, Integer rejectStatusID, Integer startStatusID, ApprovalListResult listResult);

    SelectItem[] getLookUpItems(ListingFilterParameter filterParametrs, Integer type);

    SelectItem[] getLookUpItems(ListingFilterParameter filterParametrs, Integer type, String query);

    Integer saveModelForm(ModelForm form);

    SelectItem[] getSchemasAsSelectItem(ListingFilterParameter filterParameter);

    ArrayList<TeamEmployees> getCompanyEmployeesForTree();

    ArrayList<TeamEmployees> getCompanyAccountsForTree(String accountType);

    SelectItem[] getEmployeesAsSelectItem(ListLoadConfig listLoadConfig, ListingFilterParameter filterParametrs);

    ArrayList<SelectItem> getApproverEmployeesAsSelectItem(ListLoadConfig listLoadConfig, ListingFilterParameter filterParametrs);

    UserBankAccountData getBankDetail(Integer employeeID);

    ListResult<SelectItem> getCrmAccountAsSelectItem(int type, ListingFilterParameter filterParameter);

    SelectItem[] getAsSelectItems(ListingFilterParameter filterParametrs);

    Integer saveCrmNote(String entityType, Integer entityID, HistoryListItem note);

    String getCaseDescription(Integer caseID, boolean stripHtmls);

    ArrayList<Integer> deleteCrmAccount(ArrayList<Integer> objectId, boolean removeContactsAlso);

    //WBSSERVICE
    WbsItem[] getFirstLevelWorkstreams(Integer projectId);

    ArrayList<RelationItem> saveRelations(String relationType, Integer relationID, String relationName, ArrayList<RelationItem> selectedRelations, boolean indexToSolr);

    ArrayList<RelationItem> saveRelations(String relationType, Integer relationID, String relationName, ArrayList<RelationItem> selectedRelations, boolean indexToSolr, boolean fromConvert);

    SelectItem[] getAccountingRelatedProjects(ListingFilterParameter filterParametrs);

    SelectItem[] getAccountsForPayment(ListingFilterParameter filterParametrs);

    void deleteEmailFilter(Integer objectID);

    InOutItem[] getInOutReport(Integer clientId, Integer projectId, Integer departmentId, Integer employeeId, Integer viewAsId,
                               String groupByName, Date t1, Date t2, boolean showDate, boolean showCheckIn, boolean showCheckOut,
                               boolean showActualIn, boolean showLeaveReq, boolean showLauchHour,
                               boolean showTimesheetHour, boolean showBudgetHour, boolean showMissingHours, boolean showFinImpact);

    InOutItem[] getInOutReportItems(Integer employeeId, Date fromDate, Date toDate);

    String getRelationName(Integer relationID, String relationType);

    EmailTemplateItem getEventDetails(Integer eventID);

    ArrayList<RelationItem> getAdditionalRelations(Integer relationID, String relationType, String relationName, Integer fromID, String fromType, String fromName);

    UserEmailItem getUserEmailItem(Integer emailSettingID);

    void updateEmailRbacEntries(UserEmailItem userEmailItem);

    void copyCaseAttachments(Integer companyID);

    HashMap<Integer, ArrayList<RelationItem>> getRelations(String relationType, ArrayList<Integer> relationIDs);

    void deleteNote(Integer noteID, String entityType);

    ArrayList<HistoryListItem> getNotes(Integer entityID, String entityType);

    ReferenceItem getReference(Integer objectID);

    ReferenceItem getReferenceByCode(String code);

    ArrayList<ReferenceItem> getReferenceChildren(Integer parentID);

    ArrayList<ReferenceItem> getReferenceChildren(String parentCode);

    SelectItem getDepartmentForInvoice();

    Integer saveReference(ReferenceItem item, ArrayList<ReferenceItem> children, boolean isChild);

    void deleteReference(Integer objectId);

    void deleteReason(Integer objectId);

    SelectItem[] getAccountTreeLookUpItems(ListingFilterParameter fp, String treeLevel);

    SelectItem[] getParentAccountsTreeList(Integer objectID);

    void saveResourceUtilDailyEstimatedTime(Integer employeeID, Integer taskID, boolean isChangeTaskStartTime, boolean isChangeTaskEndTime, DateNonConvertable nonConvertable, Date dailyDate, Integer lastDailyEstimatedTime);

    ResourceUtilItem getResourceUtilization(ListingFilterParameter fp);

    ExportToExcelItem getResourceUtilizationExcelData(ListingFilterParameter fp, String startDateString, String endDateString, int daysInMonth);

    ProjectTaskItem[] getEmployeeProjectsResourceUtil(Integer start, String startDate, String endDate, ListingFilterParameter filterParameter);

    TaskItem[] getEmployeeProjectTasksResourceUtil(String startDateString, String endDateString, Integer start, ListingFilterParameter filterParameter);

    NumberData generateCandidateNumber(Integer candidateID);

    SelectItem[] getLocations();

    String generateAccountNumber(String accountType);

    HelpDocumentItem getWikiUrl(String code);

    SignatureItem getSignature();

    ArrayList<HelpDocumentItem> getHelpDocumentBySectionView(String section, String view);

    SelectItem convertEmailTo(String emailID, String relationType);

    SelectItem[] getEmployeesByPermessionCode(ListingFilterParameter filterParametrs);

    ArrayList<Integer> deleteEvent(ArrayList<Integer> ids);

    ArrayList<Integer> delete(String entity, ArrayList<Integer> objectIDs);

    SelectItem[] getJobTitles();

    SelectItem[] getCountryList(ListingFilterParameter listingFilterParameter);

    SelectItem[] getEmployeesForLookUp(ListingFilterParameter filterParameter);

    ArrayList<SelectItem> getPayrollBatchesForLookUp(ListingFilterParameter lfp);

    SelectItem[] getPayrollDepartmentForLookUp(ListingFilterParameter filterParameter);

    SelectItem[] getPayrollLocationForLookUp(ListingFilterParameter filterParameter);

    SelectItem getPayrollSupervisorForLookUp(ListingFilterParameter filterParameter);

    SelectItem[] getEmployeesAsSelectItem(ListingFilterParameter filterParametrs);

    SelectItem[] getLocationsWithCodeAsSelectItem(ListingFilterParameter filterParametrs);

    SelectItem getCurrentUser();

    SelectItem[] getDepartmentsForLookUp(ListingFilterParameter filterParametrs);

    SelectItem[] getDepartmentsByLocationAsSelectItem(Integer locationID);

    SelectItem[] gePositionsForLookUp(ListingFilterParameter filterParametrs);

    SelectItem[] getPaymentMethodList();

    SelectItem[] getParentList();

    ListResult<ReferenceItem> getDeletableReferences(ListingFilterParameter fp);

    SelectItem[] getEmployeeStepSatues(String formID);

    CurrencyItem[] getCurrencyAsSelectItems();

    SelectItem[] getPositionListAsSelectItem(ListingFilterParameter filterParametrs);

    PositionsSelectItem getPositionItems(Integer positionId);

    PaymentDeductionSelectItem[] getCategoriesForLookUp(ListingFilterParameter filterParametrs);

    BulkAddCategoriesItem getCategoriesForBulkAdd(ListingFilterParameter filterParameter);

    SelectItem[] getEntityCustomFieldLookUpData(String query, ListingFilterParameter filterParameter);

    PayrollPdfTemplateList getCompanyPdfTemplates(String templateType);

    CustomFormItemPdfTemplateList getCompanyPdfTemplatesWithFormId(String templateType, String formId);

    SpokenLanguageTO getLanguagesWithLevels();

    void saveCustomizeForm(String formID, LinkedHashMap<String, HashMap<ColumnType, LinkedList<CustomizeFormItem>>> modelFields, HashMap<String, DynamicSectionsRpc> sectionsRpcMap);

    void saveCustomizeGridForm(String formID, LinkedHashMap<String, LinkedList<CustomizeFormItem>> modelFields, HashMap<String, DynamicSectionsRpc> sectionsRpcMap);

    void saveSectionOrder(String formID, LinkedList<DynamicSectionsRpc> sections);

    int saveCustomDynamicFormSection(DynamicSectionsRpc rpc);

    Integer deletCustomSection(Integer id);

    EmailTemplateItem generateReplyToReporterCaseItem(EntityToEmailTemplate emailTemplate, Integer autoResponseID);

    SelectItem[] getProjectEmployeesAsSelectItem(ListingFilterParameter fp);

    Integer saveCallLog(Appointment appointment);

    CalendarItems getCalendarItems(Integer employeeID, String reasonCode, DateNonConvertable displayFirstDay, DateNonConvertable displayLastDay);

    ListResult<ReasonItem> getReasonItems(ListingFilterParameter filterParameter);

    ArrayList<ReasonItem> getReasonItemsByReasonId(Integer reasonID);

    void deleteLeaveReasonHistoryById(Integer reasonID);

    ReasonItem getReason(Integer objectID);

    Integer saveLeaveReason(ReasonItem item);

    HashMap<LeaveReasonType, ArrayList<SelectItem>> getSelectOptions(Integer reasonId);

    ListResult<WorkflowRule> getWorkflowActivitiesList(ListingFilterParameter fp);

    SelectItem[] getRFQList(ListingFilterParameter lfp);

    SelectItem[] getRFPList(ListingFilterParameter lfp);

    SelectItem[] getCustomFieldLookUpData(ListingFilterParameter fp, CustomFieldLookUpTypeEnum typeEnum);

    Boolean isDynamicForm(String formId);

    String getProductDescription(Integer productId);

    SelectItem getAccountItemByRelation(RelationItem firstPhoneRelationItem);

    InvoiceTermsItem[] getInvoiceTermsForLookUp(ListingFilterParameter filterParametrs);

    SelectItem[] getWarehousesForLookUp(ListingFilterParameter filterParameter);

    AccountItem[] getAccountsForInvoice(ListingFilterParameter fp, ArrayList<String> types);

    TaxItem[] getCompanyTaxesWithFilter(ListingFilterParameter filterParametrs);

    SelectItem[] getUnitMeasurements(ListingFilterParameter filterParametrs);

    ReferenceLocale getReferenceLocaleByReferenceId(Integer referenceId);

    void saveReferenceLocale(Integer referenceId, ReferenceLocale referenceLocale);

    void saveReferencePermission(ReferenceItem item);

    BankAccountItem[] getBankAccountsForLookUp(ListingFilterParameter listingFilterParameter);

    Integer getTaxCalcTypeForInvoice();

    NumberData generateAccountNumberData(String accountType);

    LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>> getAssignesByType(ListingFilterParameter fp, String formType);

    SelectItem getEmployeeAsSelectItem(Integer objectId);

    PdfTemplateItemList getCompanyPdfTemplate(String type);

    SelectItem[] getVacanciesList(ListingFilterParameter fp);

    ArrayList<String> getScripts();

    ArrayList<SelectItem> getCompanyGoals();

    SelectItem[] getNewsCategories();

    ArrayList<SelectItem> getValidityPeriods(String periodType);

    SelectItem[] getReasons(Integer userId);

    SelectItem[] getReasons(Integer userId, boolean withDrafts);

    SelectItem[] getReasons(Integer userId, Integer year);

    SelectItem[] getBenefitTypes(ListingFilterParameter fp);

    Integer checkCustomFormQuota(String formId, boolean isUpdate);

    Boolean saveIntoductionPage(IntroductionPageRpc pageRpc);

    IntroductionPageRpc getIntoductionPageByParentFormId(String parentFormId);

    SelectItem[] getLocationList();

    SelectItem[] getParentIsNullProjects(Integer projectid);

    HashSet<SelectItem> getManagers();

    SelectItem[] getPrioritySelectItems();

    SelectItem[] getTeamsList();

    SelectItem[] getCertificateeTypes();

    SelectItem[] getDepartmentsEmployees();

    SelectItem[] getCategoriessAsSelectItem();

    SelectItem[] getBrandssAsSelectItem();

    String getDefaultPaginationName(String formId);

    ListResult<MultiCashAdvanceItem> getMultiCashAdvanceList(ListingFilterParameter filterParametrs);

    ArrayList<PaymentDeductionObject> getEmployeesForMultiCashAdvance(ListingFilterParameter filterParametrs);

    MultiCashAdvanceItem getMultiCashAdvanceData(Integer objectID);

    Boolean deleteMultiCashAdvance(Integer objectID);

    TestRPC saveMultiCashAdvance(MultiCashAdvanceItem multiCashAdvanceItem, boolean fromView);

    Integer createCFCommitBoxNote(HistoryListItem note, CompanyCustomFieldItem customFieldItem, Integer entityID);

    ArrayList<HistoryListItem> getCFCommitBoxNotes(CompanyCustomFieldItem customFieldItem, Integer formItemId);

    void removeCommitFromCFCommitBox(Integer noteId);

    void setFormItemIdToAllCommitsOfThisCFWidget(Integer formItemId, ArrayList<Integer> newNoteIds);

    LogHistoryItem[] getAllStatusHistories(Integer id);

    ListResult<HistoryItem> getCandidateUpdatesList(ListingFilterParameter fp);

    SelectItem[] getShiftsForLookUp(ListingFilterParameter filterParameter);


    SelectItem[] getImapHostAndSmptHost(ListingFilterParameter filterParameter);

    ListResult<GoalItem> getPersonalGoalList(ListingFilterParameter filterParametrs);

    ListResult<GoalItem> getDepartmentGoalList(ListingFilterParameter filterParametrs);

    ListResult<GoalItem> getBusinGoalList(ListingFilterParameter filterParametrs);

    ListResult<GoalItem> getRelatedGoalList(ListingFilterParameter filterParametrs, String type);

    ListResult<GoalItem> getProjectGoalList(ListingFilterParameter filterParametrs);

    void deleteGoal(Integer goalId, String type);

    void saveGoalEditCellValue(GoalItem rowValue, String columnCodeName);

    ArrayList<SelectItem> getReferenceRelatedItems(Integer referenceId, String code);

    QuickAddColumnConfigs[] getQuickAddColumns(QuickAddSettingsForm form);

    ListResult<WorkflowWebHookListItem> getWorkflowWebHooks(ListingFilterParameter fp);

    ListResult<WorkflowWebHookListItem> getPublicWebHooks(ListingFilterParameter fp);

    HashMap<String, Object> runFormWebhooks(RunWebHookRequestItem item);

    ArrayList<HashMap<String, Object>> runItemTableWebhooks(RunWebHookRequestItem item);

    ArrayList<SelectItem> getCustomFieldsForCustomLogic(String formId, String categoryName);

    SelectItem[] getLookUpItems(String type, String searchKey, String query);

    Boolean makeCallUsingSipuni(String phoneNumber);

    byte[] getSipuniAudio(String callId, String userId, String secretKey);

    ContactTo createContactFromCalls(String phoneNumber, String contactFullName);

    void updateStatusPlacement(Integer objectID, String status, String rejectionReason);

    String changeCandidateStatusByPlacmentId(String classEntity, Integer placementId, String parentCode, String statusCode, boolean fromPlacement, SelectItem columnLayoutData);

    List<SelectItem> getApproversHistoryNotes(String entityType);

    Boolean addDeletedApproverProcessHistory(String entityType, Integer id);

    SelectItem getDownloadAppLinks();

    Boolean isShowWiki();

    String shortenLink(String link, CrmAccountItem item);

    class App {
        public static AllInOneServiceAsync get() {
            ServiceDefTarget target = GWT.create(CoreGenericService.class);
            target.setServiceEntryPoint(Utils.getHostNameURL() + "rpc/allinone");
            return (AllInOneServiceAsync) target;
        }
    }
}
