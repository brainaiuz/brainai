package com.edatasite.workforce.gwt.core.server.db;

import com.edatasite.workforce.core.domain.EdsEmployeeSkills;

/**
 * Created by IntelliJ IDEA.
 * User: HRS
 * Date: Feb 11, 2010
 * Time: 3:32:28 PM
 * To change this template use File | Settings | File Templates.
 */
public interface EmployeeSkillsManager extends Manager<EdsEmployeeSkills> {

    EdsEmployeeSkills getEmployeeSkill(Integer employeeID, Integer competencyID, EdsEmployeeSkills.Typer typer);

    EdsEmployeeSkills getEmployeeSkill(Integer employeeID, String skillName);

    void deleteEmployeeSkills(Integer employeeID, EdsEmployeeSkills.Typer typer);
}
