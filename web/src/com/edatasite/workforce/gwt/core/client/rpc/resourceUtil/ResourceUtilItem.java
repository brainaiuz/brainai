package com.edatasite.workforce.gwt.core.client.rpc.resourceUtil;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created with IntelliJ IDEA.
 * User: Ilhombek
 * Date: 5/21/12
 * Time: 5:13 PM
 * To change this template use File | Settings | File Templates.
 */
public class ResourceUtilItem implements IsSerializable {

    private String department_description;                         //Department description
    private Integer department_id;                                 //Department id
    private String department_name;                                     //Department name
    private String employeeIds;                                     //Department name

    private EmployeeResourceUtilItem[] employeeResourceUtilItems;    //Employees

    private int[] month_holiday_INT;                                 //Monthly holidays

	public String getDepartment_description() {
		return department_description;
	}

	public void setDepartment_description(String department_description) {
		this.department_description = department_description;
	}

	public Integer getDepartment_id() {
		return department_id;
	}

	public void setDepartment_id(Integer department_id) {
		this.department_id = department_id;
	}

	public String getDepartment_name() {
		return department_name;
	}

	public void setDepartment_name(String department_name) {
		this.department_name = department_name;
	}

    public String getEmployeeIds() {
        return employeeIds;
    }

    public void setEmployeeIds(String employeeIds) {
        this.employeeIds = employeeIds;
    }

    public EmployeeResourceUtilItem[] getEmployeeResourceUtilItems() {
		return employeeResourceUtilItems;
	}

	public void setEmployeeResourceUtilItems(EmployeeResourceUtilItem[] employeeResourceUtilItems) {
		this.employeeResourceUtilItems = employeeResourceUtilItems;
	}

	public int[] getMonth_holiday_INT() {
		return month_holiday_INT;
	}

	public void setMonth_holiday_INT(int[] month_holiday_INT) {
		this.month_holiday_INT = month_holiday_INT;
	}
}
