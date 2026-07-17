package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.core.domain.assessment.*;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.dashboard.client.rpc.PAReportItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface AssessmentManager extends Manager<EdsAssessment> {
    List<EdsEmployeeAssessment> getAssessments();

    List<EdsEmployeeDepartment> getUnassessedEmployeesByPeriodID(Integer periodID);

    List<EdsCompany> getCompaniesUsedPAByDate(Date sTime, Date eTime);

    List<EdsEmployeeAssessment> getAssessmentsByCompanyAndDate(Date sTime, Date eTime, EdsCompany company);

    List<EdsAssessment> getAssessmentsByEmployee(EdsUser user, ListingFilterParameter filterParametrs);

    Long getAssessmentsByEmployeeTotal(EdsUser user, ListingFilterParameter filterParametrs);

    List<EdsAssessment> get360AssessmentsByEmployee(EdsUser user);

    int getManagersAssessmentsCount(EdsUser user);

    List<PAReportItem> getPAReportList(EdsDepartment departmentFilter, EdsEmployee employeeFilter, Integer viewAsFilter,
                                       String groupByName, String type, Date fromDate, Date toDate);

    List<Object[]> getPADashboardReport(Integer departmentId, Date startDate, Date endDate, boolean isSelect360GapSelf);

    List<EdsAssessment> getCalendarAssessments(List<Integer> employeeIDs, Date start, Date end);

    List<EdsEmployee> getTeamEmployeeByRole(Integer departmentId, Integer roleId, Integer initiatorId, Integer appraisedEmplId);

    List<EdsAssessment> getCompanyAllAssessments(EdsCompany compnay);

    //    void indexEmployeeAssessmentToSolr(EdsEmployeeAssessment employeeAssessment);

    EdsAppraisalsSettings getAppraisalsSettings();

    EdsAppraisalsSettings getAppraisalsSettings(EdsCompany company);

    void createOrUpdateAppraisalsSettings(EdsAppraisalsSettings appraisalsSettings);

    void updateAppraisalsStatus(ArrayList<Integer> ids, String statusCode);

    Boolean isUsedValidityPeriod(EdsValidityPeriod validityPeriod);

    void updatePeriodAssessmentsByDepartment(List<Integer> employeeAssessmentIdList, EdsReference status);

    Set<EdsAssessment> getAssessmentsByIds(List<Integer> assessmentIds);

    List<EdsAppraisalRate> getAppraisalRates();

    List<EdsApprasialScoreType> getAppraisalScoreTypes();

    void createScoreTypes(EdsApprasialScoreType apprasialScoreType);

    void deleteScoreTypes();

}
