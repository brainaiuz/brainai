package com.edatasite.workforce.gwt.core.client.rpc;

import com.edatasite.workforce.gwt.contact.client.rpc.ContactTo;
import com.edatasite.workforce.gwt.contact.client.rpc.CrmAccountItem;
import com.edatasite.workforce.gwt.core.client.ReasonItem;
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
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

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
 * Time: 4:56:48 PM
 */
public interface AllInOneServiceAsync {

    void getCompanyRoles(AsyncCallback<ArrayList<RoleListItem>> callback);

    void getRoles(AsyncCallback<ArrayList<SelectItem>> async);

    void getAllRoles(AsyncCallback<ArrayList<SelectItem>> async);

    void getFormData(String formID, String formType, AsyncCallback<LayoutRPC> async);

    void getModelForm(String formID, AsyncCallback<ModelForm> async);

    void getModelGridForm(String formID, AsyncCallback<ModelForm> async);

    void getCustomizeForm(String formID, AsyncCallback<LinkedHashMap<String, HashMap<ColumnType, LinkedList<CustomizeFormItem>>>> async);

    void getCustomizeGridForm(String formID, AsyncCallback<LinkedHashMap<String, LinkedList<CustomizeFormItem>>> async);

    void getModelEntityName(String formID, AsyncCallback<String> async);

    void getCustomizeFormSections(String formID, boolean isFormCustomizeForm, AsyncCallback<LinkedHashMap<String, DynamicSectionsRpc>> async);

    void getCFFLocalizations(String formID, AsyncCallback<ArrayList<CustomFormLocalization>> async);

    void saveCFLItems(LinkedList<CustomFormLocalization> items, AsyncCallback<Void> async);

    void getDefaultModelForm(String formID, AsyncCallback<ModelForm> async);

    void getLookUpItems(ListingFilterParameter filterParametrs, Integer type, AsyncCallback<SelectItem[]> asyncCallback);

    void getLookUpItems(ListingFilterParameter filterParametrs, Integer type, String query, AsyncCallback<SelectItem[]> asyncCallback);

    void saveApprovers(String formID, String onboardingtype, Integer approveStatusID, Integer rejectStatusID, Integer startStatusID, ApprovalListResult listResult, AsyncCallback<Void> asyncCallback);

    void getApprovers(String formID, Integer entityID, boolean isLeaveRequestForm, Integer userID, boolean fromSettings, AsyncCallback<ApprovalListResult> asyncCallback);

    void getApprovers(String formID, Integer entityID, boolean isLeaveRequestForm, Integer userID, boolean fromSettings, boolean fromApi, ListingFilterParameter fp, AsyncCallback<ApprovalListResult> asyncCallback);

    void saveModelForm(ModelForm form, AsyncCallback<Integer> asyncCallback);

    void getSchemasAsSelectItem(ListingFilterParameter filterParameter, AsyncCallback<SelectItem[]> async);

    void getCompanyEmployeesForTree(AsyncCallback<ArrayList<TeamEmployees>> callback);

    void getCompanyAccountsForTree(String accountType, AsyncCallback<ArrayList<TeamEmployees>> callback);

    void getEmployeesAsSelectItem(ListLoadConfig listLoadConfig, ListingFilterParameter filterParametrs, AsyncCallback<SelectItem[]> asyncCallback);

    void getApproverEmployeesAsSelectItem(ListLoadConfig listLoadConfig, ListingFilterParameter filterParametrs, AsyncCallback<ArrayList<SelectItem>> asyncCallback);

    void getBankDetail(Integer employeeID, AsyncCallback<UserBankAccountData> callback);

    void getCrmAccountAsSelectItem(int type, ListingFilterParameter filterParametrs, AsyncCallback<ListResult<SelectItem>> asyncCallback);

    void getAsSelectItems(ListingFilterParameter filterParametrs, AsyncCallback<SelectItem[]> asyncCallback);

    void saveCrmNote(String entityType, Integer entityID, HistoryListItem note, AsyncCallback<Integer> async);

    void getCaseDescription(Integer caseID, boolean stripHtmls, AsyncCallback<String> callback);

    void deleteCrmAccount(ArrayList<Integer> objectIDs, boolean removeContactsAlso, AsyncCallback<ArrayList<Integer>> async);

    void getFirstLevelWorkstreams(Integer projectId, AsyncCallback<WbsItem[]> callback);

    void saveRelations(String relationType, Integer relationID, String relationName, ArrayList<RelationItem> selectedRelations, boolean indexToSolr, AsyncCallback<ArrayList<RelationItem>> asyncCallback);

    void saveRelations(String relationType, Integer relationID, String relationName, ArrayList<RelationItem> selectedRelations, boolean indexToSolr, boolean fromConvert, AsyncCallback<ArrayList<RelationItem>> asyncCallback);

    void getAccountingRelatedProjects(ListingFilterParameter filterParametrs, AsyncCallback<SelectItem[]> asyncCallback);

    void getPositionItems(Integer positionId, AsyncCallback<PositionsSelectItem> callback);

    void getAccountsForPayment(ListingFilterParameter filterParametrs, AsyncCallback<SelectItem[]> async);

    void deleteEmailFilter(Integer objectID, AsyncCallback<Void> async);

    void getInOutReport(Integer clientId, Integer projectId, Integer departmentId, Integer employeeId, Integer viewAsId,
                        String groupByName, Date t1, Date t2, boolean showDate, boolean showCheckIn, boolean showCheckOut,
                        boolean showActualIn, boolean showLeaveReq, boolean showLauchHour,
                        boolean showTimesheetHour, boolean showBudgetHour, boolean showMissingHours,
                        boolean showFinImpact, AsyncCallback<InOutItem[]> async);

    void getInOutReportItems(Integer employeeId, Date fromDate, Date toDate, AsyncCallback<InOutItem[]> async);

    void getRelationName(Integer relationID, String relationType, AsyncCallback<String> asyncCallback);

    void getAdditionalRelations(Integer relationID, String relationType, String relationName, Integer fromID, String fromType, String fromName, AsyncCallback<ArrayList<RelationItem>> asyncCallback);

    void copyCaseAttachments(Integer companyID, AsyncCallback callback);

    void getUserEmailItem(Integer emailSettingID, AsyncCallback<UserEmailItem> asyncCallback);

    void updateEmailRbacEntries(UserEmailItem userEmailItem, AsyncCallback<Void> asyncCallback);

    void getRelations(String relationType, ArrayList<Integer> relationIDs, AsyncCallback<HashMap<Integer, ArrayList<RelationItem>>> asyncCallback);

    void deleteNote(Integer noteID, String entityType, AsyncCallback<Void> asyncCallback);

    void getDepartmentForInvoice(AsyncCallback<SelectItem> async);

    void getNotes(Integer entityID, String entityType, AsyncCallback<ArrayList<HistoryListItem>> callback);

    void getReference(Integer objectID, AsyncCallback<ReferenceItem> asyncCallback);

    void getReferenceChildren(Integer parentID, AsyncCallback<ArrayList<ReferenceItem>> asyncCallback);

    void getReferenceChildren(String code, AsyncCallback<ArrayList<ReferenceItem>> asyncCallback);

    void saveReference(ReferenceItem item, ArrayList<ReferenceItem> children, boolean isChild, AsyncCallback<Integer> abstractAsyncCallback);

    void deleteReference(Integer objectId, AsyncCallback<Void> callback);

    void deleteReason(Integer objectId, AsyncCallback<Void> callback);

    void getAccountTreeLookUpItems(ListingFilterParameter fp, String treeLevel, AsyncCallback<SelectItem[]> asyncCallback);

    void getParentAccountsTreeList(Integer objectID, AsyncCallback<SelectItem[]> asyncCallback);

    void saveResourceUtilDailyEstimatedTime(Integer employeeID, Integer taskID, boolean isChangeTaskStartTime, boolean isChangeTaskEndTime, DateNonConvertable nonConvertable, Date dailyDate, Integer lastDailyEstimatedTime, AsyncCallback<Void> callback);

    void getResourceUtilization(ListingFilterParameter fp, AsyncCallback<ResourceUtilItem> callback);

    void getResourceUtilizationExcelData(ListingFilterParameter fp, String startDateString, String endDateString, int daysInMonth, AsyncCallback<ExportToExcelItem> callback);

    void getEmployeeProjectsResourceUtil(Integer start, String startDate, String endDate, ListingFilterParameter filterParameter, AsyncCallback<ProjectTaskItem[]> callback);

    void getEmployeeProjectTasksResourceUtil(String startDateString, String endDateString, Integer start, ListingFilterParameter filterParameter, AsyncCallback<TaskItem[]> callback);

    void generateCandidateNumber(Integer candidateID, AsyncCallback<NumberData> callback);

    void getLocations(AsyncCallback<SelectItem[]> callback);

    void generateAccountNumber(String accountType, AsyncCallback<String> async);

    void getWikiUrl(String code, AsyncCallback<HelpDocumentItem> callback);

    void getSignature(AsyncCallback<SignatureItem> callback);

    void getHelpDocumentBySectionView(String section, String view, AsyncCallback<ArrayList<HelpDocumentItem>> asyncCallback);

    void convertEmailTo(String emailID, String relationType, AsyncCallback<SelectItem> callback);

    void getEmployeesByPermessionCode(ListingFilterParameter filterParametrs, AsyncCallback<SelectItem[]> async);

    void deleteEvent(ArrayList<Integer> ids, AsyncCallback<ArrayList<Integer>> asyncCallback);

    void delete(String entity, ArrayList<Integer> objectIDs, AsyncCallback<ArrayList<Integer>> callback);

    void getJobTitles(AsyncCallback<SelectItem[]> callback);

    void getCountryList(ListingFilterParameter listingFilterParameter, AsyncCallback<SelectItem[]> async);

    void getEmployeesForLookUp(ListingFilterParameter filterParameter, AsyncCallback<SelectItem[]> async);

    void getPayrollDepartmentForLookUp(ListingFilterParameter filterParameter, AsyncCallback<SelectItem[]> async);

    void getPayrollLocationForLookUp(ListingFilterParameter filterParameter, AsyncCallback<SelectItem[]> async);

    void getPayrollSupervisorForLookUp(ListingFilterParameter filterParameter, AsyncCallback<SelectItem> async);

    void getEmployeesAsSelectItem(ListingFilterParameter filterParametrs, AsyncCallback<SelectItem[]> async);

    void getLocationsWithCodeAsSelectItem(ListingFilterParameter filterParametrs, AsyncCallback<SelectItem[]> async);

    void getCurrentUser(AsyncCallback<SelectItem> async);

    void getDepartmentsForLookUp(ListingFilterParameter filterParametrs, AsyncCallback<SelectItem[]> async);

    void gePositionsForLookUp(ListingFilterParameter filterParametrs, AsyncCallback<SelectItem[]> async);

    void getLocationByDepartmentId(Integer departmentId, AsyncCallback<SelectItem> async);


    void getPaymentMethodList(AsyncCallback<SelectItem[]> async);

    void getParentList(AsyncCallback<SelectItem[]> async);

    void getDeletableReferences(ListingFilterParameter fp, AsyncCallback<ListResult<ReferenceItem>> callback);

    void getEmployeeStepSatues(String type, AsyncCallback<SelectItem[]> asyncCallback);

    void getCurrencyAsSelectItems(AsyncCallback<CurrencyItem[]> asyncCallback);

    void getPositionListAsSelectItem(ListingFilterParameter filterParametrs, AsyncCallback<SelectItem[]> abstractAsyncCallback);

    void getCategoriesForLookUp(ListingFilterParameter filterParametrs, AsyncCallback<PaymentDeductionSelectItem[]> asyncCallback);

    void getCategoriesForBulkAdd(ListingFilterParameter filterParameter, AsyncCallback<BulkAddCategoriesItem> asyncCallback);

    void getEntityCustomFieldLookUpData(String query, ListingFilterParameter filterParameter, AsyncCallback<SelectItem[]> asyncCallback);

    void getCompanyPdfTemplates(String templateType, AsyncCallback<PayrollPdfTemplateList> templateList);

    void getCompanyPdfTemplatesWithFormId(String templateType, String formId, AsyncCallback<CustomFormItemPdfTemplateList> templateList);

    void getLanguagesWithLevels(AsyncCallback<SpokenLanguageTO> callback);

    void saveCustomizeForm(String formID, LinkedHashMap<String, HashMap<ColumnType, LinkedList<CustomizeFormItem>>> modelFields, HashMap<String, DynamicSectionsRpc> sectionsRpcMap, AsyncCallback<Void> asyncCallback);

    void saveCustomizeGridForm(String formID, LinkedHashMap<String, LinkedList<CustomizeFormItem>> modelFields, HashMap<String, DynamicSectionsRpc> sectionsRpcMap, AsyncCallback<Void> asyncCallback);

    void saveSectionOrder(String formID, LinkedList<DynamicSectionsRpc> sections, AsyncCallback<Void> asyncCallback);

    void saveCustomDynamicFormSection(DynamicSectionsRpc rpc, AsyncCallback<Integer> asyncCallback);

    void deletCustomSection(Integer id, AsyncCallback<Integer> asyncCallback);

    void generateReplyToReporterCaseItem(EntityToEmailTemplate emailTemplate, Integer autoResponseID, AsyncCallback<EmailTemplateItem> callback);

    void getProjectEmployeesAsSelectItem(ListingFilterParameter fp, AsyncCallback<SelectItem[]> callback);

    void saveCallLog(Appointment appointment, AsyncCallback<Integer> callback);

    void getCalendarItems(Integer employeeID, String reasonCode, DateNonConvertable displayFirstDay, DateNonConvertable displayLastDay, AsyncCallback<CalendarItems> async);

    void getReasonItems(ListingFilterParameter fp, AsyncCallback<ListResult<ReasonItem>> callback);

    void getReason(Integer objectID, AsyncCallback<ReasonItem> callback);

    void saveLeaveReason(ReasonItem item, AsyncCallback<Integer> asyncCallback);

    void getSelectOptions(Integer reasonID, AsyncCallback<HashMap<LeaveReasonType, ArrayList<SelectItem>>> asyncCallback);

    void getWorkflowActivitiesList(ListingFilterParameter fp, AsyncCallback<ListResult<WorkflowRule>> asyncCallback);

    void getRFQList(ListingFilterParameter lfp, AsyncCallback<SelectItem[]> async);

    void getCustomFieldLookUpData(ListingFilterParameter fp, CustomFieldLookUpTypeEnum typeEnum, AsyncCallback<SelectItem[]> async);

    void isDynamicForm(String formId, AsyncCallback<Boolean> async);

    void getProductDescription(Integer productId, AsyncCallback<String> async);

    void getAccountItemByRelation(RelationItem firstPhoneRelationItem, AsyncCallback<SelectItem> async);

    void getInvoiceTermsForLookUp(ListingFilterParameter filterParametrs, AsyncCallback<InvoiceTermsItem[]> callback);

    void getWarehousesForLookUp(ListingFilterParameter filterParameter, AsyncCallback<SelectItem[]> async);

    void getAccountsForInvoice(ListingFilterParameter fp, ArrayList<String> types, AsyncCallback<AccountItem[]> async);

    void getCompanyTaxesWithFilter(ListingFilterParameter filterParametrs, AsyncCallback<TaxItem[]> async);

    void getUnitMeasurements(ListingFilterParameter filterParametrs, AsyncCallback<SelectItem[]> async);

    void getReferenceLocaleByReferenceId(Integer referenceId, AsyncCallback<ReferenceLocale> asyncCallback);

    void saveReferenceLocale(Integer referenceId, ReferenceLocale referenceLocale, AsyncCallback<Void> asyncCallback);

    void saveReferencePermission(ReferenceItem item, AsyncCallback<Void> async);

    void getBankAccountsForLookUp(ListingFilterParameter listingFilterParameter, AsyncCallback<BankAccountItem[]> async);

    void getTaxCalcTypeForInvoice(AsyncCallback<Integer> asyncCallback);

    void getRFPList(ListingFilterParameter lfp, AsyncCallback<SelectItem[]> async);

    void getRolesCheckAdmin(AsyncCallback<ArrayList<SelectItem>> async);

    void generateAccountNumberData(String accountType, AsyncCallback<NumberData> callback);

    void getAssignesByType(ListingFilterParameter fp, String formType, AsyncCallback<LinkedHashMap<KpiTreeInfo, ArrayList<KpiTreeInfo>>> async);

    void getEmployeeAsSelectItem(Integer objectId, AsyncCallback<SelectItem> async);

    void getCompanyPdfTemplate(String type, AsyncCallback<PdfTemplateItemList> async);

    void getVacanciesList(ListingFilterParameter fp, AsyncCallback<SelectItem[]> async);

    void getScripts(AsyncCallback<ArrayList<String>> async);

    void getCompanyGoals(AsyncCallback<ArrayList<SelectItem>> async);

    void getNewsCategories(AsyncCallback<SelectItem[]> abstractAsyncCallback);

    void getValidityPeriods(String periodType, AsyncCallback<ArrayList<SelectItem>> async);

    void getReasons(Integer userId, AsyncCallback<SelectItem[]> async);

    void getReasons(Integer userId, Integer year, AsyncCallback<SelectItem[]> async);

    void getBenefitTypes(ListingFilterParameter filterParameter, AsyncCallback<SelectItem[]> async);

    void checkCustomFormQuota(String formId, boolean isUpdate, AsyncCallback<Integer> async);

    void saveIntoductionPage(IntroductionPageRpc pageRpc, AsyncCallback<Boolean> async);

    void getIntoductionPageByParentFormId(String parentFormId, AsyncCallback<IntroductionPageRpc> async);

    void getLocationList(AsyncCallback<SelectItem[]> async);

    void getParentIsNullProjects(Integer projectId, AsyncCallback<SelectItem[]> callback);

    void getManagers(AsyncCallback<HashSet<SelectItem>> abstractAsyncCallback);

    void getPrioritySelectItems(AsyncCallback<SelectItem[]> async);

    void getTeamsList(AsyncCallback<SelectItem[]> async);

    void getCertificateeTypes(AsyncCallback<SelectItem[]> async);

    void getDepartmentsEmployees(AsyncCallback<SelectItem[]> async);

    void getCategoriessAsSelectItem(AsyncCallback<SelectItem[]> async);

    void getBrandssAsSelectItem(AsyncCallback<SelectItem[]> callback);

    void getDefaultPaginationName(String formId, AsyncCallback<String> callback);

    void getPayrollBatchesForLookUp(ListingFilterParameter lfp, AsyncCallback<ArrayList<SelectItem>> async);

    void getEmployeesForMultiCashAdvance(ListingFilterParameter filterParametrs, AsyncCallback<ArrayList<PaymentDeductionObject>> async);

    void getMultiCashAdvanceList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<MultiCashAdvanceItem>> listResultAsyncCallback);

    void getMultiCashAdvanceData(Integer objectID, AsyncCallback<MultiCashAdvanceItem> async);

    void saveMultiCashAdvance(MultiCashAdvanceItem multiCashAdvanceItem, boolean fromView, AsyncCallback<TestRPC> async);

    void deleteMultiCashAdvance(Integer objectID, AsyncCallback<Boolean> async);

    void createCFCommitBoxNote(HistoryListItem note, CompanyCustomFieldItem customFieldItem, Integer entityID, AsyncCallback<Integer> async);

    void getCFCommitBoxNotes(CompanyCustomFieldItem customFieldItem, Integer formItemId, AsyncCallback<ArrayList<HistoryListItem>> async);

    void removeCommitFromCFCommitBox(Integer noteId, AsyncCallback<Void> async);

    void getReasonItemsByReasonId(Integer reasonID, AsyncCallback<ArrayList<ReasonItem>> async);

    void deleteLeaveReasonHistoryById(Integer reasonID, AsyncCallback<Void> async);

    void setFormItemIdToAllCommitsOfThisCFWidget(Integer formItemId, ArrayList<Integer> newNoteIds, AsyncCallback<Void> async);

    void getAllStatusHistories(Integer id, AsyncCallback<LogHistoryItem[]> callback);

    void getCandidateUpdatesList(ListingFilterParameter fp, AsyncCallback<ListResult<HistoryItem>> async);

    void getReasons(Integer userId, boolean withDrafts, AsyncCallback<SelectItem[]> async);

    void getShiftsForLookUp(ListingFilterParameter filterParameter, AsyncCallback<SelectItem[]> async);

    void getImapHostAndSmptHost(ListingFilterParameter filterParameter, AsyncCallback<SelectItem[]> async);

    Request getPersonalGoalList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<GoalItem>> callback);

    Request getDepartmentGoalList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<GoalItem>> callback);

    Request getBusinGoalList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<GoalItem>> callback);

    Request getProjectGoalList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<GoalItem>> callback);

    void deleteGoal(Integer goalId, String type, AsyncCallback<Void> callback);

    void saveGoalEditCellValue(GoalItem rowValue, String columnCodeName, AsyncCallback<Void> callback);

    void getReferenceRelatedItems(Integer referenceId, String code, AsyncCallback<ArrayList<SelectItem>> async);

    void getQuickAddColumns(QuickAddSettingsForm form, AsyncCallback<QuickAddColumnConfigs[]> callback);

    void getDepartmentsByLocationAsSelectItem(Integer locationID, AsyncCallback<SelectItem[]> async);

    void getWorkflowWebHooks(ListingFilterParameter fp, AsyncCallback<ListResult<WorkflowWebHookListItem>> async);

    void getPublicWebHooks(ListingFilterParameter fp, AsyncCallback<ListResult<WorkflowWebHookListItem>> async);

    void runFormWebhooks(RunWebHookRequestItem item, AsyncCallback<HashMap<String, Object>> async);

    void runItemTableWebhooks(RunWebHookRequestItem item, AsyncCallback<ArrayList<HashMap<String, Object>>> async);

    void getEventDetails(Integer eventID, AsyncCallback<EmailTemplateItem> async);

    void getCustomFieldsForCustomLogic(String formId, String categoryName, AsyncCallback<ArrayList<SelectItem>> async);


    void makeCallUsingSipuni(String phoneNumber, AsyncCallback<Boolean> async);

    void getSipuniAudio(String callId, String userId, String secretKey, AsyncCallback<byte[]> async);

    void getRelatedGoalList(ListingFilterParameter filterParameter, String type, AsyncCallback<ListResult<GoalItem>> asyncCallback);

    void getLookUpItems(String type, String searchKey, String query, AsyncCallback<SelectItem[]> async);

    void createContactFromCalls(String phoneNumber, String contactFullName, AsyncCallback<ContactTo> async);

    void updateStatusPlacement(Integer objectID, String status, String rejectionReason, AsyncCallback<Void> async);


    void changeCandidateStatusByPlacmentId(String classEntity, Integer placementId, String parentCode, String statusCode, boolean fromPlacement, SelectItem columnLayoutData, AsyncCallback<String> abstractAsyncCallback);

    void getApproversHistoryNotes(String entityType, AsyncCallback<List<SelectItem>> callback);

    void addDeletedApproverProcessHistory(String entityType, Integer id, AsyncCallback<Boolean> callback);

    void getDownloadAppLinks(AsyncCallback<SelectItem> callback);

    void isShowWiki(AsyncCallback<Boolean> callback);

    void shortenLink(String link, CrmAccountItem id, AsyncCallback<String> callback);

    void getReferenceByCode(String code, AsyncCallback<ReferenceItem> async);
}
