package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsClientContact;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.assessment.EdsAssessment;
import com.edatasite.workforce.core.domain.assessment.EdsEmployeeAssessment;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.server.db.EmployeeAssessmentManager;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
@Repository("employeeAssessmentManager")
public class EmployeeAssessmentManagerImpl extends BaseManager<EdsEmployeeAssessment> implements EmployeeAssessmentManager {

    public EmployeeAssessmentManagerImpl() {
        super(EdsEmployeeAssessment.class);
    }

    public List<EdsEmployeeAssessment> getReviewerAssessments(EdsEmployee employee) {
        Map<String, Object> map = new HashMap<>();
        map.put("user", employee);
        return findByNamedParams("select ea from EdsEmployeeAssessment ea where " + checkToAssessmentStatus(map) +
                " and (ea.assessment.reviewer=:user or ea.employee=:user or ea.collaborator=:user) order by ea.assessment.inititateDate desc", map);
    }

    public List<EdsEmployeeAssessment> getReviewerSimpleAssessments(EdsEmployee employee, boolean withInitiator) {
        Map<String, Object> map = new HashMap<>();
        map.put("assessmentcode", EdsAssessment.ASSESSMENT_SIMPLE);
        map.put("user", employee);
		if (withInitiator) {
			return findByNamedParams("select ea from EdsEmployeeAssessment ea where " + checkToAssessmentStatus(map) +
					" and (ea.assessment.assessmentType.code=:assessmentcode) and (ea.assessment.reviewer=:user or ea.assessment.initiator=:user or " +
					"ea.employee=:user or ea.collaborator=:user) and (ea.deleted is null or ea.deleted=false) " +
					"order by ea.assessment.inititateDate desc", map);
		}
        return findByNamedParams("select ea from EdsEmployeeAssessment ea where " + checkToAssessmentStatus(map) +
                " and (ea.assessment.assessmentType.code=:assessmentcode) and (ea.assessment.reviewer=:user or " +
                "ea.employee=:user or ea.collaborator=:user) and (ea.deleted is null or ea.deleted=false) " +
                "order by ea.assessment.inititateDate desc", map);
    }

    public List<EdsEmployeeAssessment> getReviewer360Assessments(EdsEmployee employee) {
        Map<String, Object> map = new HashMap<>();
        map.put("assessmentCode", EdsAssessment.ASSESSMENT_360);
        map.put("user", employee);
        return findByNamedParams("SELECT ea FROM EdsEmployeeAssessment ea WHERE " + checkToAssessmentStatus(map) +
                "AND (ea.assessment.reviewer=:user OR ea.employee=:user OR ea.collaborator=:user) AND " +
                "(ea.assessment.assessmentType.code =:assessmentCode) ORDER BY ea.assessment.inititateDate DESC", map);
    }

    public List<EdsEmployeeAssessment> getEmployeeAssessments(EdsEmployee employee) {
        Map<String, Object> map = new HashMap<>();
        map.put("employee", employee);
        return findByNamedParams("select ea from EdsEmployeeAssessment ea where " + checkToAssessmentStatus(map) +
                " and ea.employee=:employee or ea.collaborator=:employee order by ea.assessment.inititateDate desc", map);
    }

    public List<EdsEmployeeAssessment> getEmployeeSimpleAssessments(EdsEmployee employee, boolean withInitiator) {
        Map<String, Object> map = new HashMap<>();
        map.put("assessmentcode", EdsAssessment.ASSESSMENT_SIMPLE);
        map.put("employee", employee);
        if (withInitiator) {
			return findByNamedParams("select ea from EdsEmployeeAssessment ea where " + checkToAssessmentStatus(map) +
					" and (ea.assessment.assessmentType.code=:assessmentcode) and (ea.assessment.initiator=:employee or ea.employee=:employee " +
					"or ea.collaborator=:employee) and (ea.deleted is null or ea.deleted=false) order " +
					"by ea.assessment.inititateDate desc", map);
		}
		return findByNamedParams("select ea from EdsEmployeeAssessment ea where " + checkToAssessmentStatus(map) +
                " and (ea.assessment.assessmentType.code=:assessmentcode) and (ea.employee=:employee " +
                "or ea.collaborator=:employee) and (ea.deleted is null or ea.deleted=false) order " +
                "by ea.assessment.inititateDate desc", map);
    }

    public List<EdsEmployeeAssessment> getEmployee360Assessments(EdsEmployee employee) {
        Map<String, Object> map = new HashMap<>();
        map.put("assessmentCode", EdsAssessment.ASSESSMENT_360);
        map.put("employee", employee);
        return findByNamedParams("SELECT ea FROM EdsEmployeeAssessment ea WHERE " + checkToAssessmentStatus(map) +
                "AND (ea.assessment.assessmentType.code=:assessmentCode) AND (ea.employee=:employee OR ea.collaborator=:employee) " +
                "ORDER BY ea.assessment.inititateDate DESC", map);
    }

    public List<EdsEmployeeAssessment> getClientAssessments(EdsClientContact cc) {
        Map<String, Object> map = new HashMap<>();
        map.put("cc", cc);
        return findByNamedParams("select ea from EdsEmployeeAssessment ea where " + checkToAssessmentStatus(map) + " and ea.collaborator=:cc order by ea.assessment.inititateDate desc", map);

    }

    public List<EdsEmployeeAssessment> getClientSimpleAssessments(EdsClientContact cc) {
        Map<String, Object> map = new HashMap<>();
        map.put("assessmentcode", EdsAssessment.ASSESSMENT_SIMPLE);
        map.put("cc", cc);
        return findByNamedParams("select ea from EdsEmployeeAssessment ea where " + checkToAssessmentStatus(map) + " and (ea.assessment.assessmentType.code=:assessmentcode) and ea.collaborator=:cc order by ea.assessment.inititateDate desc", map);

    }

    public List<EdsEmployeeAssessment> getKeyEmployeeAssessment(EdsEmployee employee) {
        Map<String, Object> map = new HashMap<>();
        map.put("approved", Constants.APPROVED_BY_MANAGER);
        map.put("assessmenttype", EdsAssessment.ASSESSMENT_360);
        map.put("employee", employee);
        return findByNamedParams("select ea.keyEmployeeAssessment from EdsAssessment ea where ea.assessmentType.code=:assessmenttype " +
                "and ea.keyEmployeeAssessment.status.code=:approved and ea.keyEmployeeAssessment.employee=:employee order by ea.inititateDate desc", map);
    }

    private String checkToAssessmentStatus(Map<String, Object> params) {
        params.put("initiated", Constants.INITIATED);
        params.put("reviewed_by_employee", Constants.REVIEWED_BY_EMPLOYEE);
        params.put("reviewed_by_manager", Constants.REVIEWED_BY_MANAGER);
        params.put("rated", Constants.RATED);
        params.put("approved_by_manager", Constants.APPROVED_BY_MANAGER);
        params.put("approved", Constants.APPROVED);
        params.put("savedAsDraft", Constants.SAVED_AS_DRAFT);

        return " (ea.status.code=:initiated or ea.status.code=:reviewed_by_employee or ea.status.code=:reviewed_by_manager or ea.status.code=:rated or ea.status.code=:approved_by_manager or ea.status.code=:approved or ea.status.code=:savedAsDraft) ";
    }
}
