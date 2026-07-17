package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsCompany;
import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeDepartment;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.domain.EdsValidityPeriod;
import com.edatasite.workforce.core.domain.assessment.EdsAppraisalRate;
import com.edatasite.workforce.core.domain.assessment.EdsAppraisalsSettings;
import com.edatasite.workforce.core.domain.assessment.EdsApprasialScoreType;
import com.edatasite.workforce.core.domain.assessment.EdsAssessment;
import com.edatasite.workforce.core.domain.assessment.EdsEmployeeAssessment;
import com.edatasite.workforce.gwt.assessment.client.rpc.AssessmentsListElem;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.ui.Constants;
import com.edatasite.workforce.gwt.core.client.ui.PermissionConstants;
import com.edatasite.workforce.gwt.core.server.app.ServerUtils;
import com.edatasite.workforce.gwt.core.server.db.AssessmentManager;
import com.edatasite.workforce.gwt.core.server.db.EmployeeManager;
import com.edatasite.workforce.gwt.core.server.db.ReferenceManager;
import com.edatasite.workforce.gwt.core.server.db.RoleManager;
import com.edatasite.workforce.gwt.core.server.db.rbac.AssessmentIndexRbacManager;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.BaseEventsPostProcessor;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.AssessmentAppraisalUpdateChangeRateEventListenerImpl;
import com.edatasite.workforce.gwt.core.server.eventdispatcher.impl.BaseEventsPostProcessorImpl;
import com.edatasite.workforce.gwt.dashboard.client.rpc.PAReportItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository("assessmentManager")
public class AssessmentManagerImpl extends BaseManager<EdsAssessment> implements AssessmentManager {

    @Autowired
    EmployeeManager employeeManager;
    @Autowired
    private AssessmentIndexRbacManager assessmentIndexRbacManager;
    @Autowired
    private RoleManager roleManager;
    @Autowired
    private ReferenceManager referenceManager;
    @Autowired
    private BaseEventsPostProcessor baseEventPostProcessor;

    public AssessmentManagerImpl() {
        super(EdsAssessment.class);
    }

    public List<EdsEmployeeAssessment> getAssessments() {
        EdsUser user = getUser();
        return find("from EdsEmployeeAssessment ea " +
                "where ea.assessment.reviewer=? and (ea.deleted is null or ea.deleted<>true)", user);
    }

    @Override
    public List<EdsEmployeeDepartment> getUnassessedEmployeesByPeriodID(Integer periodID) {
        return find("SELECT tem FROM EdsEmployeeDepartment tem \n" +
                "left join tem.employee empl where tem.deleted is not true and empl.objectID not in " +
                "(SELECT ee.objectID FROM EdsEmployeeAssessment eas \n" +
                "left join eas.assessment ass \n" +
                "left join eas.employee ee \n" +
                "left join ass.validityPeriod vp \n where vp.objectID= " + periodID +
                " and eas.deleted is not true)");
    }

    @SuppressWarnings("unchecked")
    public List<EdsEmployeeAssessment> getAssessmentsByCompanyAndDate(Date sTime, Date eTime, EdsCompany company) {
        return find("select ea from EdsEmployeeAssessment ea where (ea.date between '" + sTime + "' and '" + eTime + "') and (ea.deleted is null or ea.deleted<>true)");
    }

    @SuppressWarnings("unchecked")
    @Deprecated
    public List<EdsCompany> getCompaniesUsedPAByDate(Date sTime, Date eTime) {
        return find("select distinct ea.employee.company from EdsEmployeeAssessment ea where ea.employee.company.objectID <> 1 and ea.date between '" + sTime + "' and '" + eTime + "') and (ea.deleted is null or ea.deleted<>true)");

    }

    public List<EdsAssessment> getAssessmentsByEmployee(EdsUser user, ListingFilterParameter fp) {
        boolean seeOwn = ServerUtils.hasPermission(PermissionConstants.HRMS_APPRAISALS_SEE_OWN);
        boolean seeAll = ServerUtils.hasPermission(PermissionConstants.HRMS_APPRAISALS_SEE_ALL);

        if (!seeAll && !seeOwn) {
            return new ArrayList<>();
        }

        StringBuilder hql = new StringBuilder("SELECT a FROM EdsAssessment a LEFT JOIN a.employeeAssessments ea WHERE (a.deleted IS NULL OR a.deleted = false) AND a.assessmentType.code = :assessmentTypeCode");
        Map<String, Object> params = new HashMap<>();
        params.put("assessmentTypeCode", EdsAssessment.ASSESSMENT_SIMPLE);

        if (!seeAll) {
            hql.append(" AND (ea.employee.objectID = :userId OR ea.collaborator.objectID = :userId OR a.reviewer.objectID = :userId OR a.initiator.objectID = :userId");
            params.put("userId", fp.getEmployeeId() != null ? fp.getEmployeeId() : user.getObjectID());

//            EdsEmployee employee = user.getEmployee();
//            if (employee != null) {
//                if (employee.getEmployeeDepartment() != null) {
//                    hql.append(" OR ea.employee.employeeDepartment.objectID = :departmentId ");
//                    params.put("departmentId", employee.getEmployeeDepartment().getObjectID());
//                }
//                if (employee.getLocation() != null) {
//                    hql.append(" OR ea.employee.location.objectID = :locationId ");
//                    params.put("locationId", employee.getLocation().getObjectID());
//                }
//            }
            hql.append(") ");
        }


        // filter
        if (fp.getEmployeeId() != null && fp.getEmployeeId() > 0) {
            hql.append(" AND ea.employee.objectID = :employeeId");
            params.put("employeeId", fp.getEmployeeId());
        }
        if (!ServerUtils.isNullOrEmpty(fp.getDepartmentIds())) {
            hql.append(" AND ea.employee.department.objectID = :departmentId");
            params.put("departmentId", fp.getDepartmentIds());
        }
        if (fp.getUserID() != null && fp.getUserID() > 0) {
            hql.append(" AND a.initiator.objectID = :initiatorId");
            params.put("initiatorId", fp.getUserID());
        }
        if (fp.getStatusID() != null && fp.getStatusID() > 0) {
            hql.append(" AND ea.status.objectID = :statusId");
            params.put("statusId", fp.getStatusID());
        }
        if (fp.getValidityPeriodId() != null && fp.getValidityPeriodId() > 0) {
            hql.append(" AND a.validityPeriod.objectID = :validityPeriodId");
            params.put("validityPeriodId", fp.getValidityPeriodId());
        }

        //searching
        if (fp.getSqlSearchKey() != null) {
            hql.append(" AND (LOWER(ea.employee.firstName) LIKE :searchKey OR LOWER(ea.employee.lastName) ");
            hql.append("LIKE :searchKey OR LOWER(a.initiator.firstName) LIKE :searchKey OR LOWER(a.initiator.lastName) ");
            hql.append("LIKE :searchKey OR LOWER(a.reviewer.firstName) LIKE :searchKey OR LOWER(a.reviewer.lastName) ");
            hql.append("LIKE :searchKey OR LOWER(ea.status.name) LIKE :searchKey OR LOWER(a.assessmentType.name) LIKE :searchKey) ");
            params.put("searchKey", fp.getSqlSearchKey());
        }

        if (fp.getSortField() == null) {
            fp.setSortField(AssessmentsListElem.INITIATION_DATE);
            if (fp.isAscending()) {
                fp.setAscending(false);
            }
        }

        String sortField = fp.getSortField() != null ? fp.getSortField() : AssessmentsListElem.INITIATION_DATE;
        String sortDirection = fp.isAscending() ? "ASC" : "DESC";

        String orderByClause = switch (sortField) {
            case AssessmentsListElem.INITIATION_DATE -> "a.inititateDate";
            case AssessmentsListElem.INITIATOR_NAME -> "a.initiator.firstName";
            case AssessmentsListElem.REVIEWER_NAME -> "a.reviewer.firstName";
            case AssessmentsListElem.ASSESSMENT_STATUS -> "ea.status.name";
            case AssessmentsListElem.VALIDITY_PERIOD -> "ea.assessment.validityPeriod.fromDate";
            default -> "ea.employee.firstName";
        };

        hql.append(" ORDER BY ").append(orderByClause).append(" ").append(sortDirection);

        return findIntervalByNamedParams(hql.toString(), fp.getStart(), fp.getLimit(),params);
    }

    @Override
    public Long getAssessmentsByEmployeeTotal(EdsUser user, ListingFilterParameter fp) {
        boolean seeOwn = ServerUtils.hasPermission(PermissionConstants.HRMS_APPRAISALS_SEE_OWN);
        boolean seeAll = ServerUtils.hasPermission(PermissionConstants.HRMS_APPRAISALS_SEE_ALL);

        if (!seeAll && !seeOwn) {
            return 0L;
        }

        StringBuilder hql = new StringBuilder("SELECT count(a.id) FROM EdsAssessment a LEFT JOIN a.employeeAssessments ea WHERE (a.deleted IS NULL OR a.deleted = false) AND a.assessmentType.code = :assessmentTypeCode");
        Map<String, Object> params = new HashMap<>();
        params.put("assessmentTypeCode", EdsAssessment.ASSESSMENT_SIMPLE);

        if (!seeAll) {
            hql.append(" AND (ea.employee.objectID = :userId OR ea.collaborator.objectID = :userId OR a.reviewer.objectID = :userId OR a.initiator.objectID = :userId");
            params.put("userId", fp.getEmployeeId() != null ? fp.getEmployeeId() : user.getObjectID());

//            EdsEmployee employee = user.getEmployee();
//            if (employee != null) {
//                if (employee.getEmployeeDepartment() != null) {
//                    hql.append(" OR ea.employee.employeeDepartment.objectID = :departmentId ");
//                    params.put("departmentId", employee.getEmployeeDepartment().getObjectID());
//                }
//                if (employee.getLocation() != null) {
//                    hql.append(" OR ea.employee.location.objectID = :locationId ");
//                    params.put("locationId", employee.getLocation().getObjectID());
//                }
//            }
            hql.append(") ");
        }


        // filter
        if (fp.getEmployeeId() != null && fp.getEmployeeId() > 0) {
            hql.append(" AND ea.employee.objectID = :employeeId");
            params.put("employeeId", fp.getEmployeeId());
        }
        if (fp.getUserID() != null && fp.getUserID() > 0) {
            hql.append(" AND a.initiator.objectID = :initiatorId");
            params.put("initiatorId", fp.getUserID());
        }
        if (fp.getStatusID() != null && fp.getStatusID() > 0) {
            hql.append(" AND ea.status.objectID = :statusId");
            params.put("statusId", fp.getStatusID());
        }
        if (fp.getValidityPeriodId() != null && fp.getValidityPeriodId() > 0) {
            hql.append(" AND a.validityPeriod.objectID = :validityPeriodId");
            params.put("validityPeriodId", fp.getValidityPeriodId());
        }

        //searching
        if (fp.getSqlSearchKey() != null) {
            hql.append(" AND (LOWER(ea.employee.firstName) LIKE :searchKey OR LOWER(ea.employee.lastName) ");
            hql.append("LIKE :searchKey OR LOWER(a.initiator.firstName) LIKE :searchKey OR LOWER(a.initiator.lastName) ");
            hql.append("LIKE :searchKey OR LOWER(a.reviewer.firstName) LIKE :searchKey OR LOWER(a.reviewer.lastName) ");
            hql.append("LIKE :searchKey OR LOWER(ea.status.name) LIKE :searchKey OR LOWER(a.assessmentType.name) LIKE :searchKey) ");
            params.put("searchKey", fp.getSqlSearchKey());
        }

        Long result = (Long) findSingleByNamedParams(hql.toString(), params);
        return result != null ? result : 0L;
    }


    //    @Override
    public Long getReviewerSimpleAssessmentsTptal(EdsUser user, ListingFilterParameter fp) {
        Map<String, Object> pramas = new HashMap<>();
        pramas.put("assessmentcode", EdsAssessment.ASSESSMENT_SIMPLE);
        pramas.put("user", user);

        StringBuilder query = new StringBuilder("select count(ea) from EdsEmployeeAssessment ea where ");
        query.append(" (ea.assessment.assessmentType.code=:assessmentcode")
                .append(" and (ea.assessment.reviewer=:user or ea.assessment.initiator=:user))")
                .append(" and (ea.deleted is null or ea.deleted=false)")
                .append(" and (ea.deleted is null or ea.deleted = false)");
        Long result = (Long) findSingleByNamedParams(query.toString(), pramas);
        return result != null ? result : 0L;
    }

    public List<EdsAssessment> get360AssessmentsByEmployee(EdsUser user) {
        return find("select distinct a from EdsAssessment a, in(a.employeeAssessments) ea where (ea.employee =? or ea.collaborator=? or" +
                " a.reviewer =? or a.initiator =?)and(a.assessmentType.code=?) and (a.deleted is null or a.deleted<>true) order by a.inititateDate desc", user, user, user, user, EdsAssessment.ASSESSMENT_360);

    }

    public int getManagersAssessmentsCount(EdsUser user) {
        return ((Long) findSingle("select count(*) from EdsAssessment a where a.reviewer=? and (a.deleted is null or a.deleted<>true)", user)).intValue();
    }

    public List<PAReportItem> getPAReportList(EdsDepartment departmentFilter, EdsEmployee employeeFilter, Integer viewAsFilter,
                                              String groupByName, String type, Date fromDate, Date toDate) {
        EdsUser user = getUser();
        EdsEmployee employee = employeeManager.get(user.getObjectID());
        Map<String, Object> map = new HashMap<>();
        if (EdsRole.TL.equals(viewAsFilter) || EdsRole.PM.equals(viewAsFilter) || EdsRole.MEM.equals(viewAsFilter) || EdsRole.CLIENT.equals(viewAsFilter)) {
            map.put("user", employee);
        }
        if (departmentFilter != null) {
            map.put("departmentId", departmentFilter);
        }
        if (employeeFilter != null) {
            map.put("employeeId", employeeFilter);
        }
        map.put("type", type);
        map.put("fromDate", fromDate);
        map.put("toDate", toDate);
        StringBuilder sb = new StringBuilder();
        sb.append("select distinct new com.edatasite.workforce.gwt.dashboard.client.rpc.PAReportItem " +
                "(a.name,init.firstName,init.lastName,temp.name,a.overallRate, e.firstName, e.lastName,t.name,ea.date,");
        if ("Department".equals(groupByName)) {
            sb.append(" t.objectID");
        } else if ("Employee".equals(groupByName)) {
            sb.append(" e.objectID");
        } else {
            sb.append("0");
        }
        sb.append(") from EdsAssessment a" +

                "  left join a.initiator init" +
                "  left join a.template temp" +
                "  left join a.assessmentType tp" +
                "  left join a.keyEmployeeAssessment ea" +
                "  left join ea.employee e" +
                "  left join e.employeeDepartment ed" +
                "  left join ed.department t" +
                "  left join ed.projects pe" +
                "  left join pe.project p where" +
                "  tp.code=:type ");
        if (viewAsFilter != null) {
            if (EdsRole.TL.equals(viewAsFilter)) {
                sb.append(" and t.leader=:user");
            } else if (EdsRole.PM.equals(viewAsFilter)) {
                sb.append(" and (p.manager=:user or p.backupManager=:user");
                sb.append(" or p.backupManager2=:user or p.backupManager3=:user or p.backupManager4=:user");
                sb.append(" or p.backupManager5=:user or p.backupManager6=:user or p.backupManager7=:user");
                sb.append(" or p.backupManager8=:user or p.backupManager9=:user or p.backupManager10=:user)");
            } else if (EdsRole.MEM.equals(viewAsFilter)) {
                sb.append(" and e=:user");
            } else if (EdsRole.CLIENT.equals(viewAsFilter)) {
                sb.append(" and e=:user");
            }
        }
//        sb.append(" and tp.code=:type");
        if (departmentFilter != null) {
            sb.append(" and t=:departmentId");
        }
        if (employeeFilter != null) {
            sb.append(" and e=:employeeId");
        }
        sb.append(" and ea.date between :fromDate and :toDate");

        //ordering
        sb.append(" order by ea.date desc,e.firstName");
        return findByNamedParams(sb.toString(), map);

    }

    public List<Object[]> getPADashboardReport(Integer departmentId, Date startDate, Date endDate, boolean isSelect360GapSelf) {
        String companyId = getCompanyId();
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String from = format.format(startDate);
        String to = format.format(endDate);
        StringBuffer sql = null;
        sql = new StringBuffer();

        sql.append(" select t.* from ");
        sql.append(" (select");
        sql.append(" m.id as mid,a.assessmentType as rid,");
        sql.append(" ea.date as date,m.firstName ||' ' || m.lastName,a.overallRate ");
        if (isSelect360GapSelf) {
            sql.append(" ,a.employeeSelfRatesAverage ");
        }

        sql.append(" from " + companyId + ".assessment a  ");

        sql.append(" left outer join " + companyId + ".employeeAssessment ea on (ea.id=a.keyEmployeeAssessmentId) ");
        sql.append(" left outer join " + companyId + ".reference rf on (rf.id=ea.statusId) ");
        sql.append(" left outer join " + companyId + ".employee e on (e.id=ea.employeeId) ");
        sql.append(" left outer join " + companyId + ".myuser m on (m.id=e.id) ");
        sql.append(" left outer join " + companyId + ".teamEmployee te on (te.id=e.employeeDepartmentId) ");
        sql.append(" left outer join " + companyId + ".team t on (t.id=te.teamId) ");

        sql.append(" where 1=1");
        if (isSelect360GapSelf) {
            sql.append(" and a.assessmentType=208");
        }  // Select only 360 assessments

        sql.append(" and rf.code='" + Constants.APPROVED + "'");
        sql.append(" and t.id=" + departmentId);
        sql.append(" and ea.date is not null ");
        sql.append(" and ea.deleted is not true ");
        sql.append(" and ea.date between to_date('" + from + "','yyyy-mm-dd') and to_date('" + to + "','yyyy-mm-dd')");
        sql.append(" order by m.firstName ||' ' || m.lastName");
        sql.append(" ) t");
        sql.append(" where t.date = (");
        sql.append(" select max(ea.date)");
        sql.append(" from " + companyId + ".assessment a ");
        sql.append(" left outer join " + companyId + ".employeeAssessment ea on (ea.id=a.keyEmployeeAssessmentId) ");
        sql.append(" left outer join " + companyId + ".myuser m on (m.id=ea.employeeId) ");
        sql.append(" where ea.date is not null  ");
        sql.append(" and ea.deleted is not true and ea.date between to_date('" + from + "','yyyy-mm-dd') and to_date('" + to + "','yyyy-mm-dd')");
        sql.append("  and mid = m.id and rid = a.assessmentType  ");
        sql.append("  ) ");
        return findNative(sql.toString());
    }

    public List<EdsAssessment> getCalendarAssessments(List<Integer> employeeIDs, Date start, Date end) {
        Map params = new HashMap();
        params.put("employeeIDs", employeeIDs);
        params.put("start", start);
        params.put("end", end);
        return findByNamedParams("select distinct a from EdsAssessment a, in (a.employeeAssessments) ea where" +
                " (ea.employee.objectID in (:employeeIDs) or ea.collaborator.objectID in (:employeeIDs) or a.reviewer.objectID in (:employeeIDs) or a.initiator.objectID in (:employeeIDs))" +
                " and (a.inititateDate<=:end and a.inititateDate>=:start) and (a.deleted is null or a.deleted<>true) order by a.inititateDate desc", params);
    }

    public List<EdsEmployee> getTeamEmployeeByRole(Integer departmentId, Integer roleId, Integer initiatorId, Integer appraisedEmplId) {
        return find(" select e from EdsEmployee e " +
                " left join e.employeeDepartment de " +
                " join e.roles r " +
                " where e.id<>? and e.id<>? and  de.deleted<>true and de.department.objectID=? and r.id= ?" +
                " order by e.firstName, e.lastName, e.middleName", initiatorId, appraisedEmplId, departmentId, roleId);
    }

    public List<EdsAssessment> getCompanyAllAssessments(EdsCompany company) {
        Integer companyID = company.getObjectID();
        String companyId = "\"" + companyID + "\"";
        return findNative("select distinct a.* from " + companyId + ".assessment a" +
                " where a.deleted is null or a.deleted <> true", EdsAssessment.class);
    }

    @Override
    public EdsAppraisalsSettings getAppraisalsSettings() {
        return (EdsAppraisalsSettings) findSingle("from EdsAppraisalsSettings");
    }

    public EdsAppraisalsSettings getAppraisalsSettings(EdsCompany company) {
        Integer companyID = company.getObjectID();
        String companyId = "\"" + companyID + "\"";
        return (EdsAppraisalsSettings) findNativeSingle("select ass.* from " + companyId + ".appraisalssettings ass", EdsAppraisalsSettings.class);
    }

    @Override
    public void createOrUpdateAppraisalsSettings(EdsAppraisalsSettings appraisalsSettings) {
        if (appraisalsSettings.getObjectID() == null) {
            persist(appraisalsSettings);
        } else {
            jpaTemplate.merge(appraisalsSettings);
            //calculate existing appraisals for this changed rate
            baseEventPostProcessor.registerEvent(AssessmentAppraisalUpdateChangeRateEventListenerImpl.TYPE, BaseEventsPostProcessorImpl.EVENT_TYPE_ADD, appraisalsSettings, getUser());
        }
    }

    @Override
    public void updateAppraisalsStatus(ArrayList<Integer> ids, String statusCode) {
        EdsReference reference = referenceManager.findReferenceByCode(statusCode);
        updateNative("update " + getCompanyId() + ".employeeAssessment set statusId = " + reference.getObjectID() + " WHERE assessmentId in (" + ServerUtils.getAsCommoDelimited(ids, "0") + ")");
    }

    @Override
    public Boolean isUsedValidityPeriod(EdsValidityPeriod validityPeriod) {
        Long count = (Long) findSingle("select count(g.objectID) from EdsAssessment g where g.deleted<>true and validityPeriod=?", validityPeriod);
        return count > 0;
    }

    @Override
    public void updatePeriodAssessmentsByDepartment(List<Integer> employeeAssessmentIdList, EdsReference status) {
        Map params = new HashMap();
        params.put("objectIds", employeeAssessmentIdList);
        params.put("status", status);
        updateByNamedParams("update EdsEmployeeAssessment ea set ea.status=:status where ea.objectID in (:objectIds)", params);
    }

    @Override
    public Set<EdsAssessment> getAssessmentsByIds(List<Integer> assessmentIds) {
        Map params = new HashMap();
        params.put("assessmentIds", assessmentIds);
        List<EdsAssessment> assessments = findByNamedParams("select a from EdsAssessment a where (a.deleted is null or a.deleted<>true) and a.objectID in (:assessmentIds)", params);
        return new HashSet<>(assessments);
    }

    @Override
    public List<EdsAppraisalRate> getAppraisalRates() {
        return find("from EdsAppraisalRate");
    }

    @Override
    public List<EdsApprasialScoreType> getAppraisalScoreTypes() {
        return find("from EdsApprasialScoreType");
    }

    @Override
    public void createScoreTypes(EdsApprasialScoreType apprasialScoreType) {
        persist(apprasialScoreType);
    }

    @Override
    public void deleteScoreTypes() {
        update("DELETE FROM EdsApprasialScoreType ");
    }
}
