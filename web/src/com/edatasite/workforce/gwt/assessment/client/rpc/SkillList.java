package com.edatasite.workforce.gwt.assessment.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Admin
 * Date: 18.07.2009
 * Time: 19:26:57
 * To change this template use File | Settings | File Templates.
 */
public class SkillList implements IsSerializable {

	private Integer employeeID;
	private Integer skilGroupId;
    private SkillItem[] skillItems;

	public Integer getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(Integer employeeID) {
		this.employeeID = employeeID;
	}

	public Integer getSkilGroupId() {
        return skilGroupId;
    }

    public void setSkilGroupId(Integer skilGroupId) {
        this.skilGroupId = skilGroupId;
    }

    public SkillItem[] getSkillItems() {
        return skillItems;
    }

    public void setSkillItems(SkillItem[] skillItems) {
        this.skillItems = skillItems;
    }
}