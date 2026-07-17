package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsValidityPeriod;
import com.edatasite.workforce.core.domain.assessment.EdsAppraisalApproval;
import com.edatasite.workforce.core.domain.assessment.EdsAssessment;
import com.edatasite.workforce.gwt.assessment.client.rpc.DepartmentPeriodAppraisalItem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AppraisalApprovalManager;
import org.springframework.stereotype.Repository;

import java.util.*;

/**
 * User: Sher
 * Date: 8/16/12
 * Time: 12:27 PM
 */
@Repository("appraisalApprovalManager")
public class AppraisalApprovalManagerImpl extends BaseManager<EdsAppraisalApproval> implements AppraisalApprovalManager {

    public AppraisalApprovalManagerImpl() {
        super(EdsAppraisalApproval.class);
    }

    @Override
    public List<EdsAppraisalApproval> list(ListingFilterParameter fp) {
        if (fp.getStatusIDs() != null && fp.getStatusIDs().length > 0) {
            StringBuilder hql = new StringBuilder("from EdsAppraisalApproval where deleted<>true and status.objectID in(").append(ServerUtils.getAsCommoDelimited(Arrays.asList(fp.getStatusIDs()), "0")).append(")");
            return findInterval(hql.toString(), fp.getStart(), fp.getLimit());
        } else {
            return findInterval("from EdsAppraisalApproval where deleted<>true", fp.getStart(), fp.getLimit());
        }
    }

    @Override
    public Long size(ListingFilterParameter fp) {
        if (fp.getStatusCode() != null) {
            return (Long) findSingle("select count(*) from EdsAppraisalApproval where deleted<>true and status.code=?", fp.getStatusCode());
        } else {
            return (Long) findSingle("select count(*) from EdsAppraisalApproval where deleted<>true");
        }
    }

    @Override
    public EdsAppraisalApproval getAppraisalApprovalByDepartmentAndPeriod(EdsDepartment department, EdsValidityPeriod validityPeriod) {
        Map<String, Object> params = new HashMap<>();
        String sql = "select a from EdsAppraisalApproval a " +
                     "  where a.deleted<>true ";
        if (department != null) {
            sql += " and a.department.objectID=:departmentId ";
            params.put("departmentId", department.getObjectID());
        }
        if (validityPeriod != null) {
            sql += " and a.validityPeriod.objectID=:validityPeriodId ";
            params.put("validityPeriodId", validityPeriod.getObjectID());
        }
        return (EdsAppraisalApproval) findSingleByNamedParams(sql, params);
    }

    @Override
    public List<EdsAssessment> getEmployeeAppraisalAssessmentsForPeriod(List<Integer> employeeIds, Integer validityPeriodId) {
        if (employeeIds.isEmpty()) {
            return new ArrayList<>();
        }
        Map<String, Object> params = new HashMap<>();
        params.put("validityPeriodId", validityPeriodId);
        params.put("employeeIds", employeeIds);
        params.put("statusCode", DepartmentPeriodAppraisalItem.PERIOD_APPROVED);
        return findByNamedParams("select aa from EdsAppraisalApproval a, in(a.assessments) aa, " +
                "in(aa.employeeAssessments) ea where a.deleted<>true and a.status.code=:statusCode " +
                "and a.validityPeriod.objectID in (select v.id from EdsValidityPeriod v where ((v.fromDate between (select vv.fromDate from EdsValidityPeriod vv where vv.id = :validityPeriodId) and (select vv.toDate from EdsValidityPeriod vv where vv.id = :validityPeriodId)) or ((select vv.fromDate from EdsValidityPeriod vv where vv.id = :validityPeriodId) between v.fromDate and v.toDate))) and ea.employee.objectID in(:employeeIds)", params);
    }

    public String getRejectionReasonComment(Integer assessmentID) {
        StringBuilder sql = new StringBuilder();
        sql.append("select aa.rejectionReasonComment from ").append(getCompanyId()).append(".appraisal_approval aa\n ");
        sql.append("left outer join ").append(getCompanyId()).append(".appraisal_approval_assessment aaa on (aaa.appraisal_approval_id=aa.id)\n ");
        sql.append("where aaa.assessments_id in (").append(assessmentID != null ? assessmentID : 0).append(") ");
        return (String) findNativeSingle(sql.toString());
    }
}
