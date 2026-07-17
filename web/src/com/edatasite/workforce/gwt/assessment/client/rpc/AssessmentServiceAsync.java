package com.edatasite.workforce.gwt.assessment.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.ValidityPeriodItem;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.hrms.client.rpc.BonusDistributionItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.EligibleEmployeeItem;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;

import java.util.ArrayList;
import java.util.LinkedList;

public interface AssessmentServiceAsync {

    void getAssessments(AsyncCallback<AssessmentListData> async);

    void getAssessmentTemplates(Integer employeeId, AsyncCallback<SelectItem[]> async);

    void getReviewers(Integer employeeID, AsyncCallback<SelectItem[]> callback);

    void initiateAssessment(InitiatedAssessmentItem assessmentItem, AsyncCallback<InitiatedAssessmentItem> async);

    Request getAssessmentsList(ListingFilterParameter filterParameters,Integer employeeId, AsyncCallback<ListResult<AssessmentsListElem>> async);

    void getSkillAssessmentElemGroups(Integer employeeAssessmentId, AsyncCallback<SkillAssessmentElemsStruct> async);

    void getGoalAssessmentElemGroups(Integer employeeAssessmentId, AsyncCallback<SkillAssessmentElemsStruct> async);

    void assess(Integer employeeAssessmentId, SkillAssessmentElem[] skillElems, SkillAssessmentElem[] goalElems, String status, AsyncCallback<Void> async);

    void assess2(Integer employeeAssessmentId, SkillAssessmentElem[] skillElems, SkillAssessmentElem[] goalElems, String status, String generalComment, int competencyPercent, int goalPercent, AsyncCallback<Void> async);

    void getGroups(AsyncCallback<LinkedList<WfmTreeItem>> async);

    void getSkills(Integer group, AsyncCallback<LinkedList<WfmTreeItem>> async);

    void saveTemplate(Integer id, String name, BoolItem[] skills, ArrayList<SelectItem> departmnets,SelectItem owner, AsyncCallback<Void> async);

    void getCompetencyListAsTableItem(Integer employeeId, int type, AsyncCallback<TemplateItem> async);

    void getTemplate(Integer id, AsyncCallback<TemplateItem> async);

    Request getTemplates(ListingFilterParameter filterParameters, AsyncCallback<ListResult<TemplateListItem>> async);

    void getAssessmentSkillsComments(Integer assessmentId, AsyncCallback<AssessmentSkills> async);

    void getAssessmentGoalsComments(Integer assessmentId, AsyncCallback<AssessmentSkills> async);

    void managersFirstAppraisal(AsyncCallback<Boolean> async);

    void removeAssessmentSkillRating(Integer assessmentId, Integer skillId, boolean isCompetencyContainer, AsyncCallback<Void> async);

    void addAssessmentSkills(Integer employeeAssessmentId, BoolItem[] skills, AsyncCallback<SkillRatingItem[]> async);

    void sendReminderMessage(Integer keyEmployeeAssessmentId, String messageContent, AsyncCallback<Void> async);

    void getSkillGroupAsSelectItem(AsyncCallback<SelectItem[]> async);

    void getSkill(Integer objectId, AsyncCallback<SkillItem> async);

    void addSkill(SkillList skillList, AsyncCallback<LinkedList<WfmTreeItem>> async);

    Request getCompetencies(ListingFilterParameter filterParameters, AsyncCallback<ListResult<SkillItem>> async);

    void deleteCompetency(Integer competencyID, AsyncCallback<Void> callback);

    void deleteEmployeeCompetency(Integer employeeID, Integer competencyID, AsyncCallback<Void> callback);

    void addSkillGroup(SkillGroupItem skillGroupItem, AsyncCallback<Integer> async);

    void getCompanyDepartments(AsyncCallback<SelectItem[]> async);

    void getEmployeeByDepartment(Integer departmentId, Integer roleId, Integer appraisedEmplId, AsyncCallback<SelectItem[]> async);

    void getCompanyClientContacts(AsyncCallback<SelectItem[]> async);

    void deleteTemplate(Integer temlateID, AsyncCallback<Void> async);

    void deleteAssessment(Integer assessmentID, AsyncCallback<Void> callback);

    void sendAssessmentResultToEmployee(Integer employeeAssessmentId, Integer loggedUserId, AsyncCallback<Void> callback);

    void getAppraisalsSettings(AsyncCallback<AppraisalsSettingsItem> callback);

    void updateAppraisalsSettings(AppraisalsSettingsItem item, AsyncCallback<Void> callback);

    void getValidityPeriods(String periodType, AsyncCallback<ValidityPeriodItem[]> callback);

    void updateAppraisalsStatus(ArrayList<Integer> ids, String statusCode, AsyncCallback<Void> callback);

    void getBonusSettings(Integer periodId, AsyncCallback<BonusSettingsItem> callback);

    void saveBonusSettings(BonusSettingsItem item, AsyncCallback<Void> callback);

    void getValidityPeriodList(ListingFilterParameter filterParameters, AsyncCallback<ListResult<ValidityPeriodItem>> callback);

    void deletedValidityPeriodItem(ValidityPeriodItem item, AsyncCallback<Void> callback);

    void createValidityPeriodItem(ValidityPeriodItem item, AsyncCallback<Integer> callback);

    void updateDepartmentPeriodAppraisal(DepartmentPeriodAppraisalItem departmentPeriodAppraisalItem, AsyncCallback<Void> callback);

    void getDepartmentPeriodAppraisalItems(ListingFilterParameter fp, AsyncCallback<ListResult<DepartmentPeriodAppraisalItem>> asyncCallback);

    void getEmployeesByReviewerId(Integer reviewerId, AsyncCallback<SelectItem[]> callback);

    void getDepartmentPeriodAppraisalDataForPeriodAppraisal(Integer periodId, AsyncCallback<DepartmentPeriodAppraisalItem> callback);

    void getDepartmentPeriodAppraisalItem(Integer objectId, AsyncCallback<DepartmentPeriodAppraisalItem> callback);

    void saveBonusDistribution(BonusDistributionItem bonusDistributionItem, AsyncCallback<Void> callback);

    void updateEmployeeSalary(Integer employeeId, Double basicSalary, AsyncCallback<Void> callback);

    void getEligibleEmployeeList(ListingFilterParameter filterParametrs, AsyncCallback<ListResult<EligibleEmployeeItem>> callback);

    void getApprovedBonusDistributionItem(Integer objectId, AsyncCallback<BonusDistributionItem> callback);

    void returnLeftoverMoneyToCompany(Double remainingAmount, AsyncCallback<Void> callback);

    void recurrentlySendEmailToHR(Integer validityPeriodID, AsyncCallback<Void> callback);

     void getCompetencyGroupList(ListingFilterParameter filter, String sortField, boolean asc, AsyncCallback<ListResult<SkillGroupItem>> callback);

    void deleteCompetencyGroup(Integer comGroupId, AsyncCallback<Void> callback);

    void getValidityPeriod(Integer id, AsyncCallback<ValidityPeriodItem> async);

    void getDataFromShift(Integer shiftItemId, AsyncCallback<InitiatedAssessmentItem> async);


}
