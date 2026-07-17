package com.edatasite.workforce.gwt.hrms.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.GoalItem;
import com.edatasite.workforce.gwt.core.client.rpc.PositionItem;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * Created by Ilxom Lutfullaev on 17.09.14.
 */
public class HrmsAPIItem implements IsSerializable {

	private PositionItem positionItem;
	private GoalItem goalItem;
	private String columnCodeName;
	private ArrayList<KpiTreeInfo> roles;
	private Boolean isSelected;

	public PositionItem getPositionItem() {
		return positionItem;
	}

	public void setPositionItem(PositionItem positionItem) {
		this.positionItem = positionItem;
	}

	public GoalItem getGoalItem() {
		return goalItem;
	}

	public void setGoalItem(GoalItem goalItem) {
		this.goalItem = goalItem;
	}

	public String getColumnCodeName() {
		return columnCodeName;
	}

	public void setColumnCodeName(String columnCodeName) {
		this.columnCodeName = columnCodeName;
	}

	public ArrayList<KpiTreeInfo> getRoles() {
		return roles;
	}

	public void setRole(ArrayList<KpiTreeInfo> roles) {
		this.roles = roles;
	}

	public Boolean getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(Boolean isSelected) {
		this.isSelected = isSelected;
	}
}
