package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsEmployee;
import com.edatasite.workforce.core.domain.EdsEmployeeProfile;

public interface ProfileManager extends Manager<EdsEmployeeProfile> {

    EdsEmployeeProfile getProfile();

    EdsEmployeeProfile getProfile(Integer employeeID);

    void deleteSalaryGradefromEmployeeProfile(Integer gradeId);

    Integer getEmployeeLastIntNumber();

    String getSavedNumberformat(Integer objectID);

    Boolean isEmployeeCodeExists(String empCode, Integer objectID);

    Boolean isPassportNumberExists(String passportNumber, Integer objectID);

    void deleteEmployeeVisaExpirationReminder(Integer employeeProfileID);

    EdsEmployee getEmployeeByContactId(Integer contactID);

    Integer getEmployeeByPinfl(String pinfl);

}