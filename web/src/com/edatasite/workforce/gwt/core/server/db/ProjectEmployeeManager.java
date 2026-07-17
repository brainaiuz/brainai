package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.*;
import com.edatasite.workforce.gwt.core.client.rpc.listingpanel.ListingFilterParameter;
import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: iskan
 * Date: Jan 13, 2008
 * Time: 10:07:22 PM
 * To change this template use File | Settings | File Templates.
 */

public interface ProjectEmployeeManager extends Manager<EdsProjectEmployee> {

    List<EdsProjectEmployee> findByEmployeeAndProject(EdsEmployee employee, EdsProject project);

    void deleteAndCreateProjectEmployee(EdsEmployeeDepartment employeeDepartment, EdsEmployeeDepartment newEmployeeDepartment);

    void deleteByEmployeedepartment(EdsEmployeeDepartment edsEmployeeDepartment);

    List<EdsEmployee> getEmployeesByProject(Integer projectId);

    ArrayList<Integer> getEmployeeIDsByProject(Integer projectId);

    List<EdsEmployee> getEmployeesByProject(ListingFilterParameter fp);

    List<EdsEmployee> getEmployeesByProjectWithDeletedEmployees(Integer projectId);

    List<EdsProject> getEmployeeProjects();

    Integer getProjectPlannedTime(Integer projectId, Integer type);

    void deleteProjectInPE(EdsProject project);

    EdsProjectEmployee getProjectEmployee(EdsEmployee employee, EdsProject edsProject);

    EdsProjectEmployee getProjectEmployee(EdsEmployee employee, EdsProject edsProject, Date contractStartDate);

    List<EdsProjectEmployee> getProjectEmployees(EdsProject project);

    List<EdsEmployee> getProjectEmployees2(EdsProject project);

    EdsProjectEmployee findProjectEmployeeByEmployeeName(Integer projectId, String firstName, String lastName);

    EdsProjectEmployee getEmployeeLastAssignedProject(Integer projectId, Integer employeeId);

    List<EdsProjectEmployee> getProjectEmployees(EdsEmployee employee);

    List<EdsProjectEmployee> getDeleteProjectEmployees(EdsProject project);

    List<EdsProjectEmployeeWageClientRateHistory> getProjectEmployeeWageClientRateHistory(Integer projectEmployeeId);

    List<ProjectMember> getProjectEmployeesInfo(Integer projectID);

    List<ProjectMember> getProjectEmployeesByContract(Integer employeeId, Date contractStart, Date contractEnd, Integer projectID);

    void updateProjectWageRates(Integer empID, Double wageRate, Double clientChargeRate, Date applyDate);

    void updateProjectEmployee(EdsEmployeeDepartment employeeTeam);

    HashMap<Integer, EdsProjectEmployee> getProjectEmployeesAsMap(EdsProject project);
}
