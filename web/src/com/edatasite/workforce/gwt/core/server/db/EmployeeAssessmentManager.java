package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.assessment.EdsEmployeeAssessment;

import java.util.List;

public interface EmployeeAssessmentManager extends Manager<EdsEmployeeAssessment> {

    List<EdsEmployeeAssessment> getReviewerAssessments(EdsEmployee employee);

    List<EdsEmployeeAssessment> getReviewerSimpleAssessments(EdsEmployee employee, boolean withInitiator);

    List<EdsEmployeeAssessment> getEmployeeAssessments(EdsEmployee employee);

    List<EdsEmployeeAssessment> getEmployeeSimpleAssessments(EdsEmployee employee, boolean withInitiator);

    List<EdsEmployeeAssessment> getClientAssessments(EdsClientContact cc);

    List<EdsEmployeeAssessment> getClientSimpleAssessments(EdsClientContact cc);

    List<EdsEmployeeAssessment> getReviewer360Assessments(EdsEmployee employee);

    List<EdsEmployeeAssessment> getEmployee360Assessments(EdsEmployee employee);

    List<EdsEmployeeAssessment> getKeyEmployeeAssessment(EdsEmployee employee);
}
