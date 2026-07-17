package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsDepartment;
import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeDepartment;
import com.edatasite.workforce.core.domain.crm.EdsCrmContactItemParams;
import com.edatasite.workforce.gwt.core.server.db.EmployeeDepartmentManager;
import org.springframework.stereotype.Repository;

import javax.persistence.Query;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Repository("employeeDepartmentManager")
public class EmployeeDepartmentManagerImpl extends BaseManager<EdsEmployeeDepartment>
        implements EmployeeDepartmentManager {

    public EmployeeDepartmentManagerImpl() {
        super(EdsEmployeeDepartment.class);

    }

    public List<EdsEmployeeDepartment> list(EdsDepartment team) {
        return find("select distinct te from EdsEmployeeDepartment te"
                + " where te.department =?", team);
    }

    public List<EdsEmployeeDepartment> getEmployeeId(Integer id) {
        return find("select te from EdsEmployeeDepartment te where te.objectID=?", id);
    }

    public List<EdsEmployeeDepartment> getTeamEmployees(Integer teamId) {
        return find("select distinct te from EdsEmployeeDepartment te" +
                " where te.department.objectID=? and (te.deleted is null or te.deleted =?) and (te.employee.deleted is null or te.employee.deleted =?)", teamId, false, false);
    }

    public List<EdsEmployeeDepartment> getEmployeeDepartmentList(
            EdsEmployee employee) {
        return find("select te from EdsEmployeeDepartment te where te.employee=? and te.deleted<>true", employee);
    }

    public void deleteEmployeeDepartment(EdsEmployeeDepartment employeeDepartment) {
        update("update EdsEmployeeDepartment te set deleted = 'true' where te.employee=?", employeeDepartment.getEmployee());
    }

    @Override
    public void deleteEmployeeDepartment(EdsEmployeeDepartment employeeDepartment, Date endDate) {
        update("update EdsEmployeeDepartment te set deleted = 'true', enddate = ? where te.employee=? and deleted <> true", endDate, employeeDepartment.getEmployee());
    }

    public void deleteEmployeeInTeam(EdsEmployee employee) {
        update("update EdsEmployeeDepartment te set te.deleted = 'true' where te.employee.objectID=? and te.deleted<>true", employee.getObjectID());
    }

    @Override
    public EdsEmployeeDepartment getByEmployeeId(Integer id) {
        return (EdsEmployeeDepartment) findSingle("SELECT ed FROM EdsEmployeeDepartment ed WHERE ed.employee.objectID = ? AND ed.deleted = false", id);
    }

    @Override
    public List<EdsEmployee> getTeamEmployees2(Integer teamId) {
        return find("select distinct te.employee from EdsEmployeeDepartment te" +
                " where te.department.objectID=? and (te.deleted is null or te.deleted = false) and (te.employee.deleted is null or te.employee.deleted = false)", teamId);
    }

    @Override
    public EdsEmployeeDepartment getLastDepartment(EdsEmployeeDepartment department) {
        return (EdsEmployeeDepartment) findSingle("select te from EdsEmployeeDepartment te where te.employee.objectID = ? and te.endDate = ?", department.getEmployee().getObjectID(), department.getStartDate());
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<Object[]> getEmployeesForDepartments(Set<Integer> teamIds, String userLocale) {
        if (teamIds == null || teamIds.isEmpty()) {
            return Collections.emptyList();
        }

//userLocale = uz en ru ar
        String sql = """
                SELECT
                    te.teamId                         AS dep_id,
                    e.id                              AS emp_id,
                    (COALESCE(mu.firstname,'') || ' ' || COALESCE(mu.lastname,'')) AS full_name,
                    mu.email                          AS email,
                    c.primaryPhone                    AS phone,
                    tg_param.value                    AS tg_username,
                    mu.photoId                        AS photo_id,
                    COALESCE(CASE :userLocale
                                   WHEN 'uz' THEN rl.uzbek
                                   WHEN 'en' THEN rl.english
                                   WHEN 'ru' THEN rl.russian
                                   WHEN 'ar' THEN rl.arabic
                                  END,
                                        pname.name, pos.name)  AS position_name,
                    FALSE                             AS is_vacant
                FROM %1$s.teamEmployee te
                JOIN %1$s.employee e ON e.id = te.employeeId
                JOIN %1$s.myuser mu ON e.id = mu.id
                LEFT JOIN %1$s.position pos ON e.positionid = pos.id
                LEFT JOIN %1$s.reference pname ON pos.positionNameId = pname.id
                LEFT JOIN %1$s.reference muref ON muref.id = mu.accountstatusid
                LEFT JOIN %1$s.reference_locale rl ON rl.id = pname.localeid
                LEFT JOIN %1$s.employeeProfile ep
                     ON ep.id = e.profileId
                LEFT JOIN %1$s.crmContact c
                     ON c.id = ep.contact_id
                
                LEFT JOIN LATERAL (
                    SELECT cip.value
                    FROM %1$s.crmcontactitemparams cip
                    WHERE cip.contactid = c.id
                      AND cip.paramid   = :phoneParam
                      AND cip.relationid = :tgRel
                    ORDER BY cip.id
                    LIMIT 1
                ) tg_param ON TRUE
                
                WHERE te.teamId IN (:ids)
                  AND (te.isdeleted IS NULL OR te.isdeleted = FALSE)
                  AND (mu.deleted IS NOT TRUE and muref.code!='EMPLOYEE_STATUS_RESIGNED')
                
                UNION ALL
                
                SELECT
                    tv.department_id                    AS dep_id,
                    e.id                                AS emp_id,
                    (COALESCE(mu.firstname,'') || ' ' || COALESCE(mu.lastname,'')) AS full_name,
                    mu.email                            AS email,
                    c.primaryPhone                      AS phone,
                    tg_param.value                      AS tg_username,
                    mu.photoId                          AS photo_id,
                    COALESCE(CASE :userLocale
                                   WHEN 'uz' THEN rl.uzbek
                                   WHEN 'en' THEN rl.english
                                   WHEN 'ru' THEN rl.russian
                                   WHEN 'ar' THEN rl.arabic
                                  END,
                                        pname.name, pos.name)  AS position_name,
                    TRUE                                AS is_vacant
                FROM %1$s.team_vacants tv
                JOIN %1$s.employee e ON e.id = tv.vacant_id
                JOIN %1$s.myuser mu ON e.id = mu.id
                LEFT JOIN %1$s.position pos ON e.positionid = pos.id
                LEFT JOIN %1$s.reference pname ON pos.positionNameId = pname.id
                LEFT JOIN %1$s.reference muref ON muref.id = mu.accountstatusid
                LEFT JOIN %1$s.reference_locale rl ON rl.id = pname.localeid
                LEFT JOIN %1$s.employeeProfile ep
                     ON ep.id = e.profileId
                LEFT JOIN %1$s.crmContact c
                     ON c.id = ep.contact_id
                
                LEFT JOIN LATERAL (
                    SELECT cip.value
                    FROM %1$s.crmcontactitemparams cip
                    WHERE cip.contactid = c.id
                      AND cip.paramid   = :phoneParam
                      AND cip.relationid = :tgRel
                    ORDER BY cip.id
                    LIMIT 1
                ) tg_param ON TRUE
                
                WHERE tv.department_id IN (:ids)
                  AND (mu.deleted IS NOT TRUE and muref.code!='EMPLOYEE_STATUS_RESIGNED')
                """.formatted(getCompanyId());

        Query q = slaveEntityManager.createNativeQuery(sql);
        q.setParameter("ids", teamIds);
        q.setParameter("phoneParam", EdsCrmContactItemParams.PHONE);
        q.setParameter("tgRel", EdsCrmContactItemParams.TG_USERNAME);
        q.setParameter("userLocale",userLocale);

        return q.getResultList();
    }
}
