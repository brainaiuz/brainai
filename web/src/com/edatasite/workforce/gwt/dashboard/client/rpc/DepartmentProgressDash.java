package com.edatasite.workforce.gwt.dashboard.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 10.08.2009
 * Time: 20:28:09
 * To change this template use File | Settings | File Templates.
 */
public class DepartmentProgressDash implements IsSerializable {

    private Integer id;
    private String teamName;
    private String leader;
    private Integer mem;
    private String projects;
    private String tasks;
    private String issues;
    private String startDate;
    private String dueDate;
    private String emplName;
    private String position;
    private Integer proIvol;
    private Integer leave;

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getLeader() {
        return leader;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public Integer getMem() {
        return mem;
    }

    public void setMem(Integer mem) {
        this.mem = mem;
    }

    public String getProjects() {
        return projects;
    }

    public void setProjects(String projects) {
        this.projects = projects;
    }

    public String getTasks() {
        return tasks;
    }

    public void setTasks(String tasks) {
        this.tasks = tasks;
    }

    public String getIssues() {
        return issues;
    }

    public void setIssues(String issues) {
        this.issues = issues;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getEmplName() {
        return emplName;
    }

    public void setEmplName(String emplName) {
        this.emplName = emplName;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getProIvol() {
        return proIvol;
    }

    public void setProIvol(Integer proIvol) {
        this.proIvol = proIvol;
    }

    public Integer getLeave() {
        return leave;
    }

    public void setLeave(Integer leave) {
        this.leave = leave;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
