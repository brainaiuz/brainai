package com.edatasite.workforce.core.domain;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: anvarbek
 * Date: Dec 13, 2007
 * Time: 5:00:47 PM
 * To change this template use File | Settings | File Templates.
 */
public class EdsDemoObject {

    private List<EdsProject> projects;
    private List<EdsDepartment> teams;
    private List<EdsEmployee> employees;

    public EdsDemoObject() {
        projects = new LinkedList<>();
        teams = new LinkedList<>();
        employees = new LinkedList<>();
    }

    public void addProject(EdsProject project) {
        projects.add(project);
    }

    public void addTeam(EdsDepartment team) {
        teams.add(team);
    }

    public void addEmployee(EdsEmployee employee) {
        employees.add(employee);
    }

    public List<EdsProject> getProjects() {
        return projects;

    }

    public List<EdsDepartment> getTeams() {
        return teams;
    }


    public List<EdsEmployee> getEmployees() {
        return employees;
    }


}
