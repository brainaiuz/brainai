package com.edatasite.workforce.gwt.core.client.rpc.resourceUtil;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ProjectTaskItem implements IsSerializable {

    private String project_description;   //Project name
    private Integer project_id;           //Project id
    private String project_name;          //Project name

    private int[] totalEstimatedTime;     //Daily project total time spent

    private int[] withHoliday_INT;        //Daily holiday time
    private int[] with_LR_INT;            //With LR time

	public ProjectTaskItem() {
	}

	public ProjectTaskItem(Integer project_id, String project_name, int maxMonthDay) {
		this.project_id = project_id;
		this.project_name = project_name;
		this.totalEstimatedTime = new int[maxMonthDay];
		this.withHoliday_INT = new int[maxMonthDay];
		this.with_LR_INT = new int[maxMonthDay];
	}

	public String getProject_description() {
		return project_description;
	}

	public void setProject_description(String project_description) {
		this.project_description = project_description;
	}

	public Integer getProject_id() {
		return project_id;
	}

	public void setProject_id(Integer project_id) {
		this.project_id = project_id;
	}

	public String getProject_name() {
		return project_name;
	}

	public void setProject_name(String project_name) {
		this.project_name = project_name;
	}

	public int[] getTotalEstimatedTime() {
		return totalEstimatedTime;
	}

	public void setTotalEstimatedTime(int[] totalEstimatedTime) {
		this.totalEstimatedTime = totalEstimatedTime;
	}

	public int[] getWithHoliday_INT() {
		return withHoliday_INT;
	}

	public void setWithHoliday_INT(int[] withHoliday_INT) {
		this.withHoliday_INT = withHoliday_INT;
	}

	public int[] getWith_LR_INT() {
		return with_LR_INT;
	}

	public void setWith_LR_INT(int[] with_LR_INT) {
		this.with_LR_INT = with_LR_INT;
	}
}
