package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsValidityPeriod;
import com.edatasite.workforce.core.domain.assessment.EdsAppraisalApproval;
import com.edatasite.workforce.core.domain.assessment.EdsAssessment;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;

import java.util.List;

/**
 * User: Sher
 * Date: 8/16/12
 * Time: 12:26 PM
 */
public interface AppraisalApprovalManager extends Manager<EdsAppraisalApproval> {

    List<EdsAppraisalApproval> list(ListingFilterParameter fp);

    Long size(ListingFilterParameter fp);

    EdsAppraisalApproval getAppraisalApprovalByDepartmentAndPeriod(EdsDepartment department, EdsValidityPeriod validityPeriod);

    List<EdsAssessment> getEmployeeAppraisalAssessmentsForPeriod(List<Integer> employeeIds, Integer validityPeriodId);

    String getRejectionReasonComment(Integer assessmentID);
}