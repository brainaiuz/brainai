package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeProfile;
import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.gwt.core.server.db.ProfileManager;
import org.springframework.stereotype.Repository;

@Repository("profileManager")
public class ProfileManagerImpl extends BaseManager<EdsEmployeeProfile> implements ProfileManager {

    public ProfileManagerImpl() {
        super(EdsEmployeeProfile.class);
    }

    public EdsEmployeeProfile getProfile() {
        EdsUser user = getUser();
        return getProfile(user.getObjectID());
    }

    public EdsEmployeeProfile getProfile(Integer employeeID) {
        return (EdsEmployeeProfile) findSingle("select e.profile from EdsEmployee e where e.objectID=?", employeeID);
    }

    public void deleteSalaryGradefromEmployeeProfile(Integer gradeId) {
        update("update EdsEmployeeProfile ep set ep.salaryGrade = null where ep.salaryGrade.objectID = ?", gradeId);
    }

    @Override
    public Integer getEmployeeLastIntNumber() {
        return (Integer) findSingle("select emp.intNumber from EdsEmployeeProfile emp where (emp.employee.deleted = false or emp.employee.deleted is null or emp.employee.accountStatus.code='RESIGNED_EMPLOYEE') and emp.intNumber is not null order by emp.intNumber desc ");
    }

    @Override
    public String getSavedNumberformat(Integer objectID) {
        System.out.println("------Profile ID " + objectID);
        return (String) findSingle("select emp.savedNumberFormula from EdsEmployeeProfile emp where emp.objectID =" + objectID);
    }

    @Override
    public Boolean isEmployeeCodeExists(String empCode, Integer objectID) {
        if (objectID != null) {
            return find("select emp from EdsEmployeeProfile emp where (emp.employee.deleted = false or emp.employee.deleted is null) and lower(emp.employeeCode) = lower(?) and emp.objectID != ?", empCode.trim(), objectID).size() > 0;
        } else {
            return find("select distinct emp from EdsEmployeeProfile emp where (emp.employee.deleted = false or emp.employee.deleted is null) and lower(emp.employeeCode) = lower(?)", empCode.trim()).size() > 0;
        }
    }

    @Override
    public Boolean isPassportNumberExists(String passportNumber, Integer objectID) {
        if (objectID != null) {
            return find("select emp from EdsEmployeeProfile emp where   lower(emp.passportNumber) = lower(?) and emp.objectID != ?", passportNumber.trim(), objectID).size() > 0;
        } else {
            return find("select distinct emp from EdsEmployeeProfile emp where (emp.employee.deleted = false or emp.employee.deleted is null) and lower(emp.passportNumber) = lower(?)", passportNumber.trim()).size() > 0;
        }
    }

    public void deleteEmployeeVisaExpirationReminder(Integer employeeProfileID) {
        if (employeeProfileID != null) {
            StringBuilder sql = new StringBuilder();
            sql.append("DELETE FROM ").append(getCompanyId()).append(".employeeProfileVisaExpirationReminder epVer \n");
            sql.append("WHERE epVer.employeeProfileID = ").append(employeeProfileID);
            updateNative(sql.toString());
        }
    }

    @Override
    public EdsEmployee getEmployeeByContactId(Integer contactID) {
        return (EdsEmployee) findSingle("SELECT emp.employee FROM EdsEmployeeProfile emp WHERE (emp.employee.deleted = false or emp.employee.deleted is null) AND emp.contact.objectID =" + contactID);
    }

    @Override
    public Integer getEmployeeByPinfl(String pinfl) {
        StringBuilder sql = new StringBuilder();
        sql.append("select em.id from ").append(getCompanyId()).append(".employeeprofile emp ")
                .append("join ").append(getCompanyId()).append(".employee em on emp.employeeId = em.id ")
                .append("join ").append(getCompanyId()).append(".myuser mu on em.id = mu.id ")
                .append("where (mu.deleted = false or mu.deleted is null) and emp.employeeCode='").append(pinfl).append("'");
        return (Integer) findNativeSingle(sql.toString());
    }
}