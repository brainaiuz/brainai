package com.edatasite.workforce.gwt.hrms.client.rpc;

import com.edatasite.workforce.gwt.assessment.client.rpc.SkillItem;
import com.edatasite.workforce.gwt.availability.client.rpc.EmployeeAttendanceReport;
import com.edatasite.workforce.gwt.availability.client.rpc.EmployeeReport;
import com.edatasite.workforce.gwt.availability.client.rpc.UserFingerPrintDeviceItem;
import com.edatasite.workforce.gwt.contact.client.rpc.DependentItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.core.client.rpc.DepartmentGoalChartSettingsItem;
import com.edatasite.workforce.gwt.core.client.rpc.DepartmentGoalEmployeeHistoryItem;
import com.edatasite.workforce.gwt.core.client.rpc.EmployeeStepItem;
import com.edatasite.workforce.gwt.core.client.rpc.FileItem;
import com.edatasite.workforce.gwt.core.client.rpc.GoalAssigneeItem;
import com.edatasite.workforce.gwt.core.client.rpc.GoalItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryItem;
import com.edatasite.workforce.gwt.core.client.rpc.HistoryListItem;
import com.edatasite.workforce.gwt.core.client.rpc.NewsComment;
import com.edatasite.workforce.gwt.core.client.rpc.NumberData;
import com.edatasite.workforce.gwt.core.client.rpc.PositionAiRequest;
import com.edatasite.workforce.gwt.core.client.rpc.PositionAiResponse;
import com.edatasite.workforce.gwt.core.client.rpc.PositionItem;
import com.edatasite.workforce.gwt.core.client.rpc.RelationItem;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.historyNote.HistoryNote;
import com.edatasite.workforce.gwt.core.client.rpc.leaveRequest.LaborPeriodRequest;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectSingleItem;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.core.client.ui.view.CustomFormItemPdfTemplateList;
import com.edatasite.workforce.gwt.documents.client.rest.resource.FileResource;
import com.edatasite.workforce.gwt.profile.client.rpc.BenefitItem;
import com.edatasite.workforce.gwt.profile.client.rpc.HrReminderItem;
import com.edatasite.workforce.gwt.project.client.rpc.EditProject;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectListItem;
import com.edatasite.workforce.gwt.project.client.rpc.ProjectViewItem;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * User: Sherali
 * Date: Oct 14, 2009
 * Time: 6:56:13 PM
 */
public interface HrmsServiceAsync {

    Request getOnboardingStepsForListing(AsyncCallback<ArrayList<OnboardingItem>> callback);

    Request getDependentsList(ListingFilterParameter fp, AsyncCallback<ListResult<DependentItem>> callback);

    Request getOnboardingPeriodList(ListingFilterParameter fp, AsyncCallback<ListResult<OnboardingItem>> callback);

    Request getOnboardingStepdList(ListingFilterParameter fp, AsyncCallback<ListResult<OnboardingItem>> callback);

    void copyOnboardingSteps(ArrayList<Integer> stepIDs, Integer fromCompanyID, Integer toCompanyID, AsyncCallback<Integer> callback);

    void getOnboardingDataForReminders(RemindersDataRpc filterParameter, AsyncCallback<RemindersDataRpc> asyncCallback);

    Request getEmployeeStepList(ListingFilterParameter fp, AsyncCallback<ListResult<EmployeeStepItem>> callback);

    void saveEmployeeStep(EmployeeStepItem item, AsyncCallback<Integer> callback);

    void getEmployeeStep(Integer objectID, Integer stepID, AsyncCallback<EmployeeStepItem> callback);

    void deleteEmployeeStep(Integer objectID, AsyncCallback<Void> callback);

    void saveStepColumnChanges(EmployeeStepItem item, String columnCode, AsyncCallback<Void> callback);

    void saveDependent(DependentItem item, AsyncCallback<Void> callback);

    void onboardingPeriodDelete(Integer objectID, AsyncCallback<Void> callback);

    void onboardingStepDelete(Integer objectID, AsyncCallback<Void> callback);

    void getDependent(Integer objectId, AsyncCallback<DependentItem> callback);

    void editDependent(Integer objectId, AsyncCallback<DependentItem> callback);

    void editGoal(Integer objectId, String type, AsyncCallback<GoalItem> callback);

    void saveGoal(GoalItem item, AsyncCallback<Integer> callback);

    void deleteGoal(Integer goalId, String type, AsyncCallback<Void> callback);

    void deleteGroupGoals(Integer groupId, AsyncCallback<Void> callback);

    void saveCompanyGoal(GoalItem item, AsyncCallback<Integer> callback);

    void editCompanyGoal(Integer objectId, AsyncCallback<GoalItem> callback);

    void deleteCompanyGoal(Integer objectId, AsyncCallback<Void> callback);

    void saveGoalEditCellValue(HrmsAPIItem apiItem, AsyncCallback<Void> callback);

    void saveCompanyGoalEditCellValue(HrmsAPIItem apiItem, AsyncCallback<Void> callback);

    void deleteDependent(Integer objectId, AsyncCallback<Void> callback);

    Request getPersonalGoalList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<GoalItem>> callback);

    Request getDepartmentGoalList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<GoalItem>> callback);

    Request getCompanyGoalList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<GoalItem>> callback);

    Request getImageUrl(Integer id, AsyncCallback<String> callback);

    void getOwnGoalList(ListingFilterParameter fp, AsyncCallback<ListResult<GoalItem>> callback);

    void saveGoalAssigneeItems(GoalAssigneeItem[] assigneeItems, AsyncCallback<Void> callback);

    Request getProjectGoalList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<GoalItem>> callback);

    void getPositionForEdit(Integer positionId, String type, AsyncCallback<PositionItem> callback);

    void getOnboardingStep(Integer stepId, AsyncCallback<OnboardingItem> callback);

    void saveOnboardingPeriod(OnboardingItem item, AsyncCallback<Void> callback);

    void saveOnboardingStep(OnboardingItem item, AsyncCallback<Integer> callback);

    void getOnboardingPeriod(Integer positionId, AsyncCallback<OnboardingItem> callback);

    void getTeamsList(AsyncCallback<SelectItem[]> async);

    void getPositionsList(AsyncCallback<SelectItem[]> async);

    void getCountries(AsyncCallback<SelectItem[]> async);

    void getPositionParams(AsyncCallback<PositionItem> async);

    void savePosition(PositionItem position, AsyncCallback<Integer> async);

    void savePositionItems(ArrayList<PositionItem> positionItems, AsyncCallback<Void> async);

    void saveJobDescription(PositionItem positionItem, AsyncCallback<Integer> callback);

    void getJobFamilies(AsyncCallback<SelectItem[]> async);

    void createJobFamily(SelectItem jfamily, AsyncCallback<Integer> async);

    void deletePosition(Integer posId, AsyncCallback<Void> callback);

    Request getBusinGoalList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<GoalItem>> callback);

    void getLocationList(AsyncCallback<SelectItem[]> callback);

    Request getPositionList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<PositionItem>> callback);

    void getOnboardingData(Integer userId, AsyncCallback<LinkedHashMap<OnboardingItem, ArrayList<OnboardingItem>>> asyncCallback);

    void saveOnboardingData(OnboardingItem stepItem, AsyncCallback<Void> asyncCallback);

    void getAttachments(Integer projectId, AsyncCallback<FileItem[]> async);

    void getGoalNoteComments(Integer noteID, AsyncCallback<NewsComment[]> callback);

    void getGoalNotes(Integer goalID, String goalTypeName, AsyncCallback<HistoryListItem[]> callback);

    void saveGoalNoteComments(NewsComment data, AsyncCallback<NewsComment> callback);

    //void deleteNote(Integer id, AsyncCallback<Void> callback);

    void getEmployees(ListingFilterParameter parameter, AsyncCallback<GoalAssigneeItem[]> callback);

    void getDepartmentAndPositionForRotation(Integer employeeId, AsyncCallback<RotationItem> callback);

    void getPerformanceNote(Integer int_objectID, AsyncCallback<PerformanceNoteItem> callback);

    void getPerformanceNoteList(ListingFilterParameter fp, AsyncCallback<ListResult<PerformanceNoteItem>> callback);

    void getCertificateList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<CertificateItem>> callback);

    void getCertificateData(Integer objectID, AsyncCallback<CertificateItem> asyncCallback);

    void getCertificateData(Integer objectId, String formType, Integer convertFormId, AsyncCallback<CertificateItem> callback);

    void getCertificateTypes(AsyncCallback<SelectItem[]> async);

    void getCertificateHTML(Integer employeeId, Integer certificateTypeID, ArrayList<FileResource> selectedDocuments, AsyncCallback<CertificateItem> asyncCallback);

    void saveCertificate(CertificateItem certificateItem, AsyncCallback<Integer> asyncCallback);

    void updateCertificateStatus(Integer objectId, String statusCode, String note, AsyncCallback<Void> callback);

    void deleteCertificate(Integer objectId, AsyncCallback<Boolean> asyncCallback);

    void getCertificateTypeData(Integer certificateTypeId, Integer employeeID, AsyncCallback<CertificateItem> asyncCallback);

    void checkCertificateTypeName(Integer objectId, String name, AsyncCallback<Boolean> callback);

    void saveCertificateType(CertificateItem item, AsyncCallback<Integer> asyncCallback);

    void getCertificateTypeList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<CertificateItem>> asyncCallback);

    void deleteCertificateType(Integer objectId, AsyncCallback<Boolean> callback);

    void getEmployeeLeaveTypes(Integer objectID, Integer selectedYear, AsyncCallback<ProfileItem> callback);

    void getEmployeeBenefitAllowance(Integer objectID, Integer year, AsyncCallback<ProfileItem> callback);

    void saveEmployeeBenefitAllowance(ProfileItem employeeItem, AsyncCallback<Integer> async);

    void saveEmployeeLeaveTypes(ProfileItem employee, Integer selectedYear, AsyncCallback<Integer> callback);

    void getBenefitList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<BenefitItem>> abstractAsyncCallback);

    void getBenefitData(Integer objectID, Boolean withDropdownValues, AsyncCallback<BenefitItem> abstractAsyncCallback);

    void deleteBenefit(Integer objectId, AsyncCallback<Integer> abstractAsyncCallback);

    void saveBenefit(BenefitItem benefitItem, AsyncCallback<Integer> asyncCallback);

    void saveHrReminderItems(ArrayList<HrReminderItem> reminderTOs, AsyncCallback<Void> callback);

    void getHrReminderItems(AsyncCallback<ListResult<HrReminderItem>> callback);


    void createReportXmlTemplate(String stepName, Integer stepId, AsyncCallback<Void> callback);

    void getOnboaringStepList(AsyncCallback<SelectItem[]> asyncCallback);

    void getOnboardingChartData(ListingFilterParameter fp, AsyncCallback<LinkedHashMap<String, Integer>> callback);

    void approveOrRejectEmployeeStep(Integer objectID, Integer stepID, boolean approve, AsyncCallback<Void> callback);

    void isEnableApprovers(String formID, AsyncCallback<Boolean> callback);

    void getFingerprintSetup(AsyncCallback<ArrayList<SelectItem>> callback);

    void getUserDeviceFingerPrint(AsyncCallback<HashMap<String, String>> callback);

    void getComapanyUsers(Integer userId, AsyncCallback<ArrayList<UserFingerPrintDeviceItem>> callback);

    void getUserFingerprintDevicesByDeviceId(String deviceId, AsyncCallback<ArrayList<UserFingerPrintDeviceItem>> callback);

    void saveUserFingerPrintDeviceData(HashMap<String, String> items, AsyncCallback<Void> callback);

    void companyIdForCertificatePermissions(AsyncCallback<Integer> callback);

    void getEmployeeUpdatesList(ListingFilterParameter fp, AsyncCallback<ListResult<HistoryItem>> async);

    void getAuditLogList(ListingFilterParameter fp, AsyncCallback<ListResult<HistoryItem>> async);

    void saveDependantEditCellValue(DependentItem rowValue, String columnCodeName, AsyncCallback<Boolean> asyncCallback);

    void saveOnboardingStepEditCellValue(OnboardingItem rowValue, String columnCodeName, AsyncCallback<Boolean> asyncCallback);

    void getPerformanceNoteItems(Integer employeeID, AsyncCallback<PerformanceNoteItem[]> callback);

    void deletePerformanceNote(Integer int_objectID, AsyncCallback<Void> callback);

    void savePerformanceNote(PerformanceNoteItem item, AsyncCallback<Integer> callback);

    void checkCompetencyList(Integer employeeId, int type, AsyncCallback<Boolean> callback);

    void getCompetencyList(Integer employeeId, int type, AsyncCallback<SkillItem[]> callback);

    void getOwnEmployeeGoalList(ListingFilterParameter filterParametrs, AsyncCallback<GoalItem[]> callback);

    void getAssassmentRatings(AsyncCallback<HashMap<Double, String>> callback);

    void getAssassmentGrades(AsyncCallback<HashMap<String, String[]>> callback);

    void getGroupGoalList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<GroupGoalITem>> callback);

    void createGroupGoals(GroupGoalITem groupGoalITem, AsyncCallback<Void> callback);

    void getGroupGoalData(Integer objectId, AsyncCallback<GroupGoalITem> callback);

    void getEmployeeAsSelectItem(Integer objectId, AsyncCallback<SelectItem> async);

    void getCompanyGoals(AsyncCallback<ArrayList<SelectItem>> async);

    void getDepartmentEmployees(AsyncCallback<SelectItem[]> async);

    void savePositionEditCellValue(PositionItem rowValue, String columnCodeName, AsyncCallback<Void> callback);

    void loadCertificateHistory(Integer objectId, AsyncCallback<List<HistoryNote>> callback);

    void createCertificateHistory(Integer certificateId, HistoryListItem hisItem, AsyncCallback<Integer> async);

    void deleteCertificateComment(Integer commentID, AsyncCallback<Void> callback);

    Request getShiftList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<ShiftItem>> async);

    Request getRotationList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<RotationItem>> async);

    Request getGroupPlacementList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<GroupPlacementItem>> async);

    void saveShiftItem(ShiftItem item, AsyncCallback<Void> async);

    void getShiftItem(Integer objectId, boolean fromSummary, AsyncCallback<ShiftItem> async);

    void getRotationItem(Integer objectId, boolean fromSummary, AsyncCallback<RotationItem> async);

    void getGroupPlacementItem(Integer objectId, boolean fromSummary, AsyncCallback<GroupPlacementItem> async);

    void getCandidateItems(Integer candidateId, AsyncCallback<GroupPlacementItem> async);

    void createRotation(RotationItem item, AsyncCallback<Void> async);

    void createGroupPlacement(GroupPlacementItem item, AsyncCallback<Void> async);

    void deleteShiftItem(Integer objectId, AsyncCallback<Void> async);

    void deleteRotationItem(Integer objectId, AsyncCallback<Void> async);

    void deleteGroupPlacementItem(Integer objectId, AsyncCallback<Void> async);

    void getShiftTeamsData(Integer shiftId, Set<Integer> ids, Integer lookUpType, Date period, boolean fromSummary, AsyncCallback<LinkedHashMap<Integer, List<ShiftTeamsItem>>> async);

    void getTeamsManagers(LinkedList<Integer> groupsId, AsyncCallback<ArrayList<SelectItem>> async);

    void getTeamsEmployeesInfo(AsyncCallback<HashMap<Integer, ArrayList<SelectItem>>> async);

    void deleteBrigada(Integer projectId, AsyncCallback<Void> async);

    void saveBrigadaEditCellValue(ProjectListItem rowValue, String columnCodeName, boolean changeTaskStatus, AsyncCallback<Void> callback);

    void getBrigadaList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<ProjectListItem>> callback);

    void getBrigadaForEdit(Integer projectId, Date date, Integer clientId, AsyncCallback<EditProject> async);

    void getBrigadaEmployeesForView(Integer projectID, AsyncCallback<KpiTreeInfo[]> async);

    void updateBrigada(EditProject project, AsyncCallback<Void> async);

    void saveBrigada(ProjectSingleItem item, AsyncCallback<Integer> async);

    void viewBrigada(Integer objectID, AsyncCallback<ProjectViewItem> async);

    void getAttendanceReportPDFTemplates(AsyncCallback<CustomFormItemPdfTemplateList> async);

    void getTerminalAttendancePDFTemplates(AsyncCallback<CustomFormItemPdfTemplateList> async);

    void generateShiftCode(AsyncCallback<NumberData> callback);

    void generateRotationNumber(AsyncCallback<NumberData> callback);

    void generateBrigadaCode(AsyncCallback<NumberData> callback);

    void getProjectEmployeesSelectItem(LinkedList<Integer> groupsId, Date period, Integer lookUpType, AsyncCallback<LinkedHashMap<String, List<EmployeeReport>>> async);


    void getDepartmentEmployeesSelectItem(ListingFilterParameter filterParameter, int monthMaxDay, AsyncCallback<EmployeeAttendanceReport> async);

    void updateApprove(ShiftItem shiftItem, AsyncCallback<Void> async);

    void updateRotationApprove(RotationItem item, AsyncCallback<Void> async);

    void updateGroupPlacementApprove(GroupPlacementItem item, AsyncCallback<Void> async);

    void getTeamsIdsForAttendanceLink(Integer shiftId, Integer type, AsyncCallback<String> async);

    void getEmployeeRelations(Integer empId, AsyncCallback<ArrayList<RelationItem>> async);

    void getSelectedTeamsByPeriodAndIds(String period, HashMap<Integer, ArrayList<String>> periodMap, ArrayList<Integer> teamsId, Integer shiftId, Integer shiftType, AsyncCallback<ArrayList<String>> async);

    void getLocationByEmployeeId(Integer employeeId, AsyncCallback<SelectItem> async);

    void getLocationByDepartmentId(Integer departmentId, AsyncCallback<SelectItem> async);

    void getPositionsSizeByNameForValidation(PositionItem item, AsyncCallback<Integer> async);

    void getGoal(Integer objectId, String type, AsyncCallback<GoalItem> async);

    void getPositionsByLocationId(Integer locationId, AsyncCallback<LinkedHashMap<SelectItem, ArrayList<PositionItem>>> async);

    void checkPositionAvailability(Integer positionId, AsyncCallback<Boolean> asyncCallback);

    void checkPositionAvailability(ArrayList<Integer> positionId, AsyncCallback<Boolean> asyncCallback);

    void generatePositionNumber(AsyncCallback<NumberData> async);

    void getPeriodList(Integer employeeID, AsyncCallback<ArrayList<LaborPeriodRequest>> async);

    void getLeaveAllowances(Integer employeeID, AsyncCallback<ArrayList<LaborPeriodRequest>> async);

    void loadLaborPeriodHistory(Integer periodID, AsyncCallback<List<HistoryNote>> async);

    void createlaborPeriodHistory(Integer periodId, HistoryListItem hisItem, AsyncCallback<Integer> async);

    void deleteLaborPeriodHistory(Integer periodHistoryID, AsyncCallback<Void> async);

    Request getSubscriptionList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<SubscriptionItem>> async);

    void deleteSubscriptionItem(Integer id, AsyncCallback<Void> async);

    Request getUsageList(ListingFilterParameter filterParameter, AsyncCallback<ListResult<SubscriptionUsageItem>> async);

    void deleteUsageItem(Integer id, AsyncCallback<Void> async);

    void deleteBackupEmployeeById(Integer backupId, AsyncCallback<Boolean> callback);

    void getBackupsEmployeeList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<BackupsEmployeeObject>> callback);

    void updateBackupsEmployeeItemsAndStatus(BackupsEmployeeObject objectData, AsyncCallback<Boolean> callback);

    void getBackupsEmployee(Integer objectId, AsyncCallback<BackupsEmployeeObject> callback);

    void generateBackupsEmployeeCode(AsyncCallback<NumberData> callback);

    void saveBackupsEmployee(BackupsEmployeeObject objectData, AsyncCallback<Integer> callback);

    void updateCandidateStatusOnApproval(Integer candidateId, String status, AsyncCallback<Void> async);

    void hireDateValidation(RotationItem rotationItem, AsyncCallback<String> async);

    void getEmployeeDataForRotation(Integer employeeId, AsyncCallback<RotationTableItem> async);

    void departmentGoalAvialableWeight(Integer weight, AsyncCallback<Integer> callback);

    void getDepartmentGoalLogData(Integer historyId, AsyncCallback<DepartmentGoalEmployeeHistoryItem> callback);

    void saveDepartmentGoalLogData(DepartmentGoalEmployeeHistoryItem item, AsyncCallback<Boolean> callback);

    void editDepartmentGoalLogData(DepartmentGoalEmployeeHistoryItem item, AsyncCallback<Boolean> callback);

    void deleteDepartmentGoalLogData(Integer id, AsyncCallback<Void> callback);


    void getDepartmentGoalChartSettings(Integer goalId, AsyncCallback<DepartmentGoalChartSettingsItem> callback);

    void saveDepartmentGoalChartSettings(Integer goalId, DepartmentGoalChartSettingsItem settings, AsyncCallback<Boolean> callback);

    void getDepartmentGoalEmployeeMetricHistory(ListingFilterParameter fp, AsyncCallback<ListResult<DepartmentGoalEmployeeHistoryItem>> callback);

    void getDepartmentGoalChartData(Integer goalId, AsyncCallback<List<DepartmentGoalEmployeeHistoryItem>> callback);

    void getDepartmentGoalAssignedEmployees(Integer departmentGoalId, AsyncCallback<SelectItem[]> callback);

    void fetchPositionAiData(PositionAiRequest positionAiRequest, AsyncCallback<PositionAiResponse> callback);
}
