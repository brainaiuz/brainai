package com.edatasite.workforce.gwt.hrms.client.rpc;

import com.edatasite.workforce.gwt.assessment.client.rpc.SkillItem;
import com.edatasite.workforce.gwt.availability.client.rpc.EmployeeAttendanceReport;
import com.edatasite.workforce.gwt.availability.client.rpc.EmployeeReport;
import com.edatasite.workforce.gwt.availability.client.rpc.UserFingerPrintDeviceItem;
import com.edatasite.workforce.gwt.contact.client.rpc.DependentItem;
import com.edatasite.workforce.gwt.contact.client.rpc.ProfileItem;
import com.edatasite.workforce.gwt.core.client.Exceptions.NumberExistingException;
import com.edatasite.workforce.gwt.core.client.Utils;
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
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

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
public interface HrmsService extends RemoteService {

    ArrayList<OnboardingItem> getOnboardingStepsForListing();

    ListResult<DependentItem> getDependentsList(ListingFilterParameter fp);

    ListResult<OnboardingItem> getOnboardingPeriodList(ListingFilterParameter fp);

    ListResult<OnboardingItem> getOnboardingStepdList(ListingFilterParameter fp);

    RemindersDataRpc getOnboardingDataForReminders(RemindersDataRpc rpc);

    ListResult<EmployeeStepItem> getEmployeeStepList(ListingFilterParameter fp);

    DependentItem getDependent(Integer objectId);

    void onboardingPeriodDelete(Integer objectID);

    void onboardingStepDelete(Integer objectID);

    void saveDependent(DependentItem item);

    DependentItem editDependent(Integer objectId);

    GoalItem editGoal(Integer objectId, String type);

    ArrayList<SelectItem> getCompanyGoals();

    Integer saveGoal(GoalItem item);

    Integer saveCompanyGoal(GoalItem item);

    GoalItem editCompanyGoal(Integer objectId);

    ListResult<GoalItem> getPersonalGoalList(ListingFilterParameter filterParametrs);

    ListResult<GoalItem> getDepartmentGoalList(ListingFilterParameter filterParametrs);

    ListResult<GoalItem> getCompanyGoalList(ListingFilterParameter filterParametrs);

    ListResult<GoalItem> getBusinGoalList(ListingFilterParameter filterParametrs);

    String getImageUrl(Integer id);

    ListResult<GoalItem> getOwnGoalList(ListingFilterParameter filterParameter);

    void saveGoalAssigneeItems(GoalAssigneeItem[] assigneeItems);

    ListResult<GoalItem> getProjectGoalList(ListingFilterParameter filterParametrs);

    PositionItem getPositionForEdit(Integer positionId,String type);

    OnboardingItem getOnboardingStep(Integer stepId);

    OnboardingItem getOnboardingPeriod(Integer positionId);


    GroupPlacementItem getCandidateItems(Integer candidateId);


    void saveOnboardingPeriod(OnboardingItem item);

    Integer saveOnboardingStep(OnboardingItem item);

    SelectItem[] getTeamsList();

    SelectItem[] getPositionsList();

    SelectItem[] getCountries();

    PositionItem getPositionParams();

    Integer savePosition(PositionItem position);


    void savePositionItems(ArrayList<PositionItem> positionItems);

    NumberData generatePositionNumber();

    Integer saveJobDescription(PositionItem positionItem);

    SelectItem[] getJobFamilies();

    Integer createJobFamily(SelectItem jfamily);

    SelectItem[] getLocationList();

    ListResult<PositionItem> getPositionList(ListingFilterParameter filterParametrs);

    void deletePosition(Integer posId);

    void deleteGoal(Integer objectId, String type);

    void deleteGroupGoals(Integer objectId);

    void deleteCompanyGoal(Integer objectId);

    void saveGoalEditCellValue(HrmsAPIItem apiItem);

    void saveCompanyGoalEditCellValue(HrmsAPIItem apiItem);

    void saveShiftItem(ShiftItem item);

    void deleteDependent(Integer objectId);

    LinkedHashMap<OnboardingItem, ArrayList<OnboardingItem>> getOnboardingData(Integer userId);

    void saveOnboardingData(OnboardingItem stepItem);

    FileItem[] getAttachments(Integer employeeId);

    NewsComment[] getGoalNoteComments(Integer noteID);

    HistoryListItem[] getGoalNotes(Integer goalID, String goalTypeName);

    NewsComment saveGoalNoteComments(NewsComment data);

    GoalAssigneeItem[] getEmployees(ListingFilterParameter parameter);

    RotationItem getDepartmentAndPositionForRotation(Integer employeeId);

    PerformanceNoteItem getPerformanceNote(Integer int_objectID);

    ListResult<PerformanceNoteItem> getPerformanceNoteList(ListingFilterParameter fp);

    ListResult<CertificateItem> getCertificateList(ListingFilterParameter fp);

    CertificateItem getCertificateData(Integer certificateId);

    CertificateItem getCertificateData(Integer objectId, String formType, Integer convertFormId);

    SelectItem[] getCertificateTypes();

    CertificateItem getCertificateHTML(Integer employeeId, Integer certificateTypeID, ArrayList<FileResource> selectedDocuments);

    Integer saveCertificate(CertificateItem item);

    void updateCertificateStatus(Integer objectId, String statusCode, String note);

    boolean deleteCertificate(Integer objectId);

    CertificateItem getCertificateTypeData(Integer certificateTypeId, Integer employeeID);

    boolean checkCertificateTypeName(Integer objectId, String name);

    Integer saveCertificateType(CertificateItem item);

    ListResult<CertificateItem> getCertificateTypeList(ListingFilterParameter fp);

    boolean deleteCertificateType(Integer objectId);

    ProfileItem getEmployeeLeaveTypes(Integer objectId, Integer selectedYear);

    ProfileItem getEmployeeBenefitAllowance(Integer objectId, Integer year);

    Integer saveEmployeeBenefitAllowance(ProfileItem employeeItem);

    Integer saveEmployeeLeaveTypes(ProfileItem employee, Integer selectedYear);

    ListResult<BenefitItem> getBenefitList(ListingFilterParameter fp);

    BenefitItem getBenefitData(Integer objectID, Boolean withDropdownValues);

    Integer deleteBenefit(Integer objectId);

    Integer saveBenefit(BenefitItem benefitItem);

    int saveEmployeeStep(EmployeeStepItem item);

    EmployeeStepItem getEmployeeStep(Integer objectID, Integer stepID);

    void deleteEmployeeStep(Integer objectID);

    void saveHrReminderItems(ArrayList<HrReminderItem> reminderTOs);

    ListResult<HrReminderItem> getHrReminderItems();

    void saveStepColumnChanges(EmployeeStepItem item, String columnCode);

    void createReportXmlTemplate(String stepName, Integer stepId);

    SelectItem[] getOnboaringStepList();

    LinkedHashMap<String, Integer> getOnboardingChartData(ListingFilterParameter fp);

    Integer copyOnboardingSteps(ArrayList<Integer> stepIDs, Integer fromCompanyID, Integer toCompanyID);

    void approveOrRejectEmployeeStep(Integer objectID, Integer stepID, boolean approve);

    boolean isEnableApprovers(String formId);

    ArrayList<SelectItem> getFingerprintSetup();

    HashMap<String, String> getUserDeviceFingerPrint();

    ArrayList<UserFingerPrintDeviceItem> getComapanyUsers(Integer userId);

    ArrayList<UserFingerPrintDeviceItem> getUserFingerprintDevicesByDeviceId(String deviceId);

    void saveUserFingerPrintDeviceData(HashMap<String, String> items);

    Integer companyIdForCertificatePermissions();

    ListResult<HistoryItem> getEmployeeUpdatesList(ListingFilterParameter fp);

    ListResult<HistoryItem> getAuditLogList(ListingFilterParameter fp);

    boolean saveDependantEditCellValue(DependentItem rowValue, String columnCodeName);

    boolean saveOnboardingStepEditCellValue(OnboardingItem rowValue, String columnCodeName);

    PerformanceNoteItem[] getPerformanceNoteItems(Integer employeeID);

    void deletePerformanceNote(Integer int_objectID);

    Integer savePerformanceNote(PerformanceNoteItem item);

    Boolean checkCompetencyList(Integer employeeId, int type);

    SkillItem[] getCompetencyList(Integer employeeId, int type);

    GoalItem[] getOwnEmployeeGoalList(ListingFilterParameter filterParametrs);

    HashMap<Double, String> getAssassmentRatings();

    HashMap<String, String[]> getAssassmentGrades();

    ListResult<GroupGoalITem> getGroupGoalList(ListingFilterParameter filterParametrs);

    void createGroupGoals(GroupGoalITem groupGoalITem);

    GroupGoalITem getGroupGoalData(Integer objectId);

    SelectItem getEmployeeAsSelectItem(Integer objectId);

    SelectItem[] getDepartmentEmployees();

    void savePositionEditCellValue(PositionItem rowValue, String columnCodeName);

    List<HistoryNote> loadCertificateHistory(Integer objectId);

    Integer createCertificateHistory(Integer certificateId, HistoryListItem hisItem);

    void deleteCertificateComment(Integer commentID);

    ListResult<ShiftItem> getShiftList(ListingFilterParameter filterParameters);

    ListResult<RotationItem> getRotationList(ListingFilterParameter filterParameters);

    ListResult<GroupPlacementItem> getGroupPlacementList(ListingFilterParameter filterParameters);

    ShiftItem getShiftItem(Integer objectId, boolean fromSummary);

    RotationItem getRotationItem(Integer objectId, boolean fromSummary);

    GroupPlacementItem getGroupPlacementItem(Integer objectId, boolean fromSummary);

    void deleteRotationItem(Integer objectId);

    void deleteGroupPlacementItem(Integer objectId);

    void createRotation(RotationItem item);

    void createGroupPlacement(GroupPlacementItem item);

    LinkedHashMap<String, List<EmployeeReport>> getProjectEmployeesSelectItem(LinkedList<Integer> groupsId, Date period, Integer lookUpType);


    EmployeeAttendanceReport getDepartmentEmployeesSelectItem(ListingFilterParameter filterParameter, int monthMaxDay);

    LinkedHashMap<Integer, List<ShiftTeamsItem>> getShiftTeamsData(Integer shiftId, Set<Integer> groupsId, Integer lookUpType, Date period, boolean fromSummary);

    ArrayList<SelectItem> getTeamsManagers(LinkedList<Integer> groupsId);

    HashMap<Integer, ArrayList<SelectItem>> getTeamsEmployeesInfo();

    void deleteShiftItem(Integer objectId);

    void deleteBrigada(Integer brigadaId);

    void saveBrigadaEditCellValue(ProjectListItem rowValue, String columnCodeName, boolean changeTaskStatus);

    ListResult<ProjectListItem> getBrigadaList(ListingFilterParameter filterParameter);

    EditProject getBrigadaForEdit(Integer projectId, Date date, Integer clientID);

    KpiTreeInfo[] getBrigadaEmployeesForView(Integer projectID);

    void updateBrigada(EditProject project) throws NumberExistingException;

    Integer saveBrigada(ProjectSingleItem item) throws NumberExistingException;


    ProjectViewItem viewBrigada(Integer objectID);

    CustomFormItemPdfTemplateList getAttendanceReportPDFTemplates();

    CustomFormItemPdfTemplateList getTerminalAttendancePDFTemplates();

    NumberData generateShiftCode();

    NumberData generateRotationNumber();

    NumberData generateBrigadaCode();

    String getTeamsIdsForAttendanceLink(Integer shiftId, Integer lookUpType);

    ArrayList<String> getSelectedTeamsByPeriodAndIds(String period, HashMap<Integer, ArrayList<String>> periodMap, ArrayList<Integer> teamIds, Integer shiftId, Integer shiftType);

    void updateApprove(ShiftItem item);

    ArrayList<RelationItem> getEmployeeRelations(Integer empId);

    void updateRotationApprove(RotationItem item);

    void updateGroupPlacementApprove(GroupPlacementItem item);

    SelectItem getLocationByEmployeeId(Integer employeeId);

    SelectItem getLocationByDepartmentId(Integer departmentId);

    Integer getPositionsSizeByNameForValidation(PositionItem item);

    LinkedHashMap<SelectItem, ArrayList<PositionItem>> getPositionsByLocationId(Integer positionId);

    GoalItem getGoal(Integer objectId, String type);

    Boolean checkPositionAvailability(Integer positionId);

    Boolean checkPositionAvailability(ArrayList<Integer> positionId);

    ArrayList<LaborPeriodRequest> getPeriodList(Integer employeeID);

    ArrayList<LaborPeriodRequest> getLeaveAllowances(Integer employeeID);

    List<HistoryNote> loadLaborPeriodHistory(Integer periodID);

    Integer createlaborPeriodHistory(Integer periodId, HistoryListItem hisItem);

    void deleteLaborPeriodHistory(Integer periodHistoryID);

    ListResult<SubscriptionItem> getSubscriptionList(ListingFilterParameter filterParameter);

    void deleteSubscriptionItem(Integer id);

    ListResult<SubscriptionUsageItem> getUsageList(ListingFilterParameter filterParameter);

    void deleteUsageItem(Integer id);

    Boolean deleteBackupEmployeeById(Integer backupId);

    ListResult<BackupsEmployeeObject> getBackupsEmployeeList(ListingFilterParameter filterParametrs);

    Boolean updateBackupsEmployeeItemsAndStatus(BackupsEmployeeObject objectData);

    BackupsEmployeeObject getBackupsEmployee(Integer objectId);

    NumberData generateBackupsEmployeeCode();

    Integer saveBackupsEmployee(BackupsEmployeeObject objectData);

    void updateCandidateStatusOnApproval(Integer candidateId,String status);

    String hireDateValidation(RotationItem rotationItem);

    RotationTableItem getEmployeeDataForRotation(Integer employeeId);

    Integer departmentGoalAvialableWeight(Integer departmentId);

    DepartmentGoalEmployeeHistoryItem getDepartmentGoalLogData(Integer historyId);

    Boolean saveDepartmentGoalLogData(DepartmentGoalEmployeeHistoryItem item);

    Boolean editDepartmentGoalLogData(DepartmentGoalEmployeeHistoryItem item);

    void deleteDepartmentGoalLogData(Integer id);


    DepartmentGoalChartSettingsItem getDepartmentGoalChartSettings(Integer goalId);

    Boolean saveDepartmentGoalChartSettings(Integer goalId, DepartmentGoalChartSettingsItem settings);

    ListResult<DepartmentGoalEmployeeHistoryItem> getDepartmentGoalEmployeeMetricHistory(ListingFilterParameter filterParameter);

    List<DepartmentGoalEmployeeHistoryItem> getDepartmentGoalChartData(Integer goalId);

    SelectItem[] getDepartmentGoalAssignedEmployees(Integer departmentGoalId);

    PositionAiResponse fetchPositionAiData(PositionAiRequest positionAiRequest);



    class App {
        public static HrmsServiceAsync get() {
            ServiceDefTarget target = GWT.create(HrmsService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/hrms");
            return (HrmsServiceAsync) target;
        }
    }

}
