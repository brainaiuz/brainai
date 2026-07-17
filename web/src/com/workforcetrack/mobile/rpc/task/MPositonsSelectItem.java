package com.workforcetrack.mobile.rpc.task;

import com.edatasite.workforce.gwt.core.client.rpc.PositionsSelectItem;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by IntelliJ IDEA.
 * User: sancho
 * Date: 6/12/11
 * Time: 1:55 PM
 * To change this template use File | Settings | File Templates.
 */
@XmlRootElement
public class MPositonsSelectItem {

    private Integer objectID;
    private String name;
    private Integer departmentID;
    private String departmentName;
    private Integer employeeID;


    public MPositonsSelectItem() {
    }

    public MPositonsSelectItem(PositionsSelectItem positionsSelectItem) {
        if (positionsSelectItem != null) {
            this.objectID = positionsSelectItem.getId();
            this.name = positionsSelectItem.getName();
            this.departmentID = positionsSelectItem.getDepartmentId();
            this.departmentName = positionsSelectItem.getDepartmentName();
            this.employeeID = positionsSelectItem.getEmployeeId();
        }
    }


    public static Boolean convert(MPositonsSelectItem mPositonsSelectItem, PositionsSelectItem positionsSelectItem, boolean toPositionsSelectItem) {
        if (mPositonsSelectItem == null || positionsSelectItem == null) {
            return null;
        }

        try {
            if (toPositionsSelectItem) {
                positionsSelectItem.setId(mPositonsSelectItem.getObjectID());
                positionsSelectItem.setName(mPositonsSelectItem.getName());
                positionsSelectItem.setDepartmentId(mPositonsSelectItem.getDepartmentID());
                positionsSelectItem.setDepartmentName(mPositonsSelectItem.getDepartmentName());
                positionsSelectItem.setEmployeeId(mPositonsSelectItem.getEmployeeID());
            } else {
                mPositonsSelectItem.setObjectID(positionsSelectItem.getId());
                mPositonsSelectItem.setName(positionsSelectItem.getName());
                mPositonsSelectItem.setDepartmentID(positionsSelectItem.getDepartmentId());
                mPositonsSelectItem.setDepartmentName(positionsSelectItem.getDepartmentName());
                mPositonsSelectItem.setEmployeeID(positionsSelectItem.getEmployeeId());
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(Integer departmentID) {
        this.departmentID = departmentID;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Integer getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(Integer employeeID) {
        this.employeeID = employeeID;
    }
}
