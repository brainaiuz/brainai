package com.edatasite.workforce.gwt.assessment.client.rpc;

import com.edatasite.workforce.gwt.core.client.Utils;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.edatasite.workforce.gwt.core.client.rpc.ValidityPeriodItem;
import com.edatasite.workforce.gwt.core.client.rpc.WfmTreeItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListResult;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.documents.client.exceptions.InsufficientPermissionsException;
import com.edatasite.workforce.gwt.documents.client.exceptions.ObjectNotFoundException;
import com.edatasite.workforce.gwt.hrms.client.rpc.BonusDistributionItem;
import com.edatasite.workforce.gwt.hrms.client.rpc.EligibleEmployeeItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.ServiceDefTarget;

import java.util.ArrayList;
import java.util.LinkedList;

public interface AssessmentService extends RemoteService {

    AssessmentListData getAssessments();

    SelectItem[] getAssessmentTemplates(Integer employeeId);

    SelectItem[] getReviewers(Integer employeeID);

    InitiatedAssessmentItem initiateAssessment(InitiatedAssessmentItem assessmentItem);

    ListResult<AssessmentsListElem> getAssessmentsList(ListingFilterParameter filterParameters,Integer employeeId);

    SkillAssessmentElemsStruct getSkillAssessmentElemGroups(Integer employeeAssessmentId);

    SkillAssessmentElemsStruct getGoalAssessmentElemGroups(Integer employeeAssessmentId);

    void assess(Integer employeeAssessmentId, SkillAssessmentElem[] skillElems, SkillAssessmentElem[] goalElems, String status);

    void assess2(Integer employeeAssessmentId, SkillAssessmentElem[] skillElems, SkillAssessmentElem[] goalElems, String status, String generalComment, int competencyPercent, int goalPercent);

    LinkedList<WfmTreeItem> getGroups();

    LinkedList<WfmTreeItem> getSkills(Integer group);

    void saveTemplate(Integer id, String name, BoolItem[] skills, ArrayList<SelectItem> departmnets,SelectItem owner);

    TemplateItem getCompetencyListAsTableItem(Integer employeeId, int type);

    TemplateItem getTemplate(Integer id);

    ListResult<TemplateListItem> getTemplates(ListingFilterParameter filterParameters);

    AssessmentSkills getAssessmentSkillsComments(Integer assessmentId);

    AssessmentSkills getAssessmentGoalsComments(Integer assessmentId);

    Boolean managersFirstAppraisal();

    void removeAssessmentSkillRating(Integer assessmentId, Integer skillId, boolean isCompetencyContainer);

    SkillRatingItem[] addAssessmentSkills(Integer employeeAssessmentId, BoolItem[] skills);

    void sendReminderMessage(Integer keyEmployeeAssessmentId, String messageContent);

    SelectItem[] getSkillGroupAsSelectItem();

    SkillItem getSkill(Integer objectId);

    LinkedList<WfmTreeItem> addSkill(SkillList skillList);

    ListResult<SkillItem> getCompetencies(ListingFilterParameter filterParameters);

    void deleteCompetency(Integer competencyID);

    void deleteEmployeeCompetency(Integer employeeID, Integer competencyID);

    Integer addSkillGroup(SkillGroupItem skillGroupItem);

    SelectItem[] getCompanyDepartments();

    SelectItem[] getEmployeeByDepartment(Integer departmentId, Integer roleId, Integer appraisedEmplId);

    SelectItem[] getCompanyClientContacts();

    void deleteTemplate(Integer temlateID);

    void deleteAssessment(Integer assessmentID);

    void sendAssessmentResultToEmployee(Integer employeeAssessmentId, Integer loggedUserId);

    AppraisalsSettingsItem getAppraisalsSettings();

    void updateAppraisalsSettings(AppraisalsSettingsItem item);

    ValidityPeriodItem[] getValidityPeriods(String periodType);

    ValidityPeriodItem getValidityPeriod(Integer id);

    void updateAppraisalsStatus(ArrayList<Integer> ids, String statusCode);

    BonusSettingsItem getBonusSettings(Integer periodId);

    void saveBonusSettings(BonusSettingsItem item);

    ListResult<ValidityPeriodItem> getValidityPeriodList(ListingFilterParameter filterParameters);

    void deletedValidityPeriodItem(ValidityPeriodItem item) throws InsufficientPermissionsException;

    Integer createValidityPeriodItem(ValidityPeriodItem item) throws InsufficientPermissionsException;

    void updateDepartmentPeriodAppraisal(DepartmentPeriodAppraisalItem departmentPeriodAppraisalItem) throws InsufficientPermissionsException;

    ListResult<DepartmentPeriodAppraisalItem> getDepartmentPeriodAppraisalItems(ListingFilterParameter fp) throws ObjectNotFoundException;

    SelectItem[] getEmployeesByReviewerId(Integer reviewerId);

    DepartmentPeriodAppraisalItem getDepartmentPeriodAppraisalDataForPeriodAppraisal(Integer periodId) throws ObjectNotFoundException;

    DepartmentPeriodAppraisalItem getDepartmentPeriodAppraisalItem(Integer objectId) throws ObjectNotFoundException;

    void saveBonusDistribution(BonusDistributionItem bonusDistributionItem);

    void updateEmployeeSalary(Integer employeeId, Double basicSalary);

    ListResult<EligibleEmployeeItem> getEligibleEmployeeList(ListingFilterParameter filterParametrs);

    BonusDistributionItem getApprovedBonusDistributionItem(Integer objectId);

    void returnLeftoverMoneyToCompany(Double remainingAmount);

    InitiatedAssessmentItem getDataFromShift(Integer shiftItemId);

    void recurrentlySendEmailToHR(Integer validityPeriodID) throws InsufficientPermissionsException;

    /**
     * Updated method to include sorting options
     * @param filterParameter filter and paging info
     * @param sortColumn name of column to sort by
     * @param ascending sort order
     * @param callback async callback to return results
     */
    ListResult<SkillGroupItem> getCompetencyGroupList(ListingFilterParameter filterParameter, String sortColumn, boolean ascending);

    void deleteCompetencyGroup(Integer id);

    class App {
        public static AssessmentServiceAsync get() {
            ServiceDefTarget target = GWT.create(AssessmentService.class);
            target.setServiceEntryPoint(Utils.getRpcBaseUrl() + "/assessment");
            return (AssessmentServiceAsync) target;
        }
    }
}
