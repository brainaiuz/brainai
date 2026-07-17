package com.edatasite.workforce.gwt.core.server.db.impl;

import com.edatasite.workforce.core.domain.EdsEmployeeSkills;
import com.edatasite.workforce.gwt.core.server.db.EmployeeSkillsManager;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: HRS
 * Date: Feb 11, 2010
 * Time: 3:33:41 PM
 * To change this template use File | Settings | File Templates.
 */
@Repository("employeeSkillsManager")
public class EmployeeSkillsManagerImpl extends BaseManager<EdsEmployeeSkills> implements EmployeeSkillsManager {
    public EmployeeSkillsManagerImpl() {
        super(EdsEmployeeSkills.class);
    }

    public EdsEmployeeSkills getEmployeeSkill(Integer employeeID, Integer competencyID, EdsEmployeeSkills.Typer typer) {
        return (EdsEmployeeSkills) findSingle("SELECT emS FROM EdsEmployeeSkills  emS WHERE emS.employee.objectID = ? AND emS.skill.objectID = ? AND emS.deleted <> true AND emS.type = ?", employeeID, competencyID, typer);
    }

    @Override
    public EdsEmployeeSkills getEmployeeSkill(Integer employeeID, String skillName) {
        return (EdsEmployeeSkills) findSingle("SELECT emS FROM EdsEmployeeSkills  emS WHERE emS.employee.objectID = ? AND emS.skill.name = ? AND emS.deleted <> true", employeeID, skillName);
    }

    public void deleteEmployeeSkills(Integer employeeID, EdsEmployeeSkills.Typer typer) {
        if (employeeID != null) {
            List<EdsEmployeeSkills> employeeSkills = (List<EdsEmployeeSkills>) find("from EdsEmployeeSkills eSkills where eSkills.employee.objectID = ? and eSkills.deleted <> true and eSkills.type = ? ", employeeID, typer);
            if (employeeSkills.size() > 0) {
                for (EdsEmployeeSkills employeeSkill : employeeSkills) {
                    employeeSkill.setDeleted(true);
                    update(employeeSkill);
                }
            }
        }
    }
}
