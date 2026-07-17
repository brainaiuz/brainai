package com.edatasite.workforce.gwt.task.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Anvar Akramov
 * Date: 24.11.2008
 * Time: 23:12:17
 * To change this template use File | Settings | File Templates.
 */
public class WorkstreamAssigneeItem implements IsSerializable {

    private Integer id;
    private String name;
    private String position;
    private Integer time;
    private Integer actualSpentTime;
    private Float percent;
    private Integer employeeID;

    private Integer countOfTask;
	
	private int doubleEmployeeCount = 1;

    public WorkstreamAssigneeItem() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public Integer getTime() {
        return time;
    }

    public void setTime(Integer time) {
        this.time = time;
    }

    public Integer getActualSpentTime() {
        return actualSpentTime;
    }

    public void setActualSpentTime(Integer actualSpentTime) {
        this.actualSpentTime = actualSpentTime;
    }

    public Float getPercent() {
        return percent;
    }

    public void setPercent(Float percent) {
        this.percent = percent;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }

    public Integer getCountOfTask() {
        return countOfTask != null ? countOfTask : 0;
    }

    public void setCountOfTask(Integer countOfTask) {
        this.countOfTask = countOfTask;
    }

	public int getDoubleEmployeeCount() {
		return doubleEmployeeCount;
	}

	public void setDoubleEmployeeCount(int doubleEmployeeCount) {
		this.doubleEmployeeCount = doubleEmployeeCount;
	}
}
