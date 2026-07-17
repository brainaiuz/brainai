package com.edatasite.workforce.rest.base.to;

import com.edatasite.workforce.core.domain.EdsUser;
import com.edatasite.workforce.core.tools.StringUtil;
import com.edatasite.workforce.gwt.core.client.rpc.employee.EmployeeListItem;
import com.edatasite.workforce.gwt.core.client.ui.assigneetree.KpiTreeInfo;
import com.edatasite.workforce.gwt.newemployee.client.rpc.EmployeeViewItem;
import com.edatasite.workforce.rest.base.helpers.WrapUtils;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Umidbek on 16.02.2015.
 */
public class UserTO implements IsSerializable {
    private Integer id;
    private String email;
    private String name;
    private String firstName;
    private String lastName;
    private String middleName;
    private ArrayList<SelectItemTO> roles;
    private ArrayList<String> permissions;

    private SelectItemTO position;
    private SelectItemTO department;
    private String imageUrl;
    private Date hireDate;

    public UserTO() {
    }

    public UserTO(EdsUser item) {
        this.id = item.getObjectID();
        this.email = item.getEmail();
        this.name = item.getFullName();
        this.firstName = item.getFirstName();
        this.lastName = item.getLastName();
        this.middleName = item.getMiddleName();
    }

    public UserTO(EmployeeListItem item) {
        this.id = item.getObjectID();
        this.name = WrapUtils.getString(item.getFirstName());

        if (item.getPositionId() != null || !StringUtil.isEmpty(item.getPosition())) {
            this.position = new SelectItemTO(item.getPositionId(), item.getPosition());
        }

        if (item.getDepartmentId() != null || !StringUtil.isEmpty(item.getDepartment())) {
            this.department = new SelectItemTO(item.getDepartmentId(), item.getDepartment());
        }
    }

    public UserTO(EmployeeViewItem item) {
        this.id = item.getObjectID();
        this.firstName = WrapUtils.getString(item.getFirstName());
        this.lastName = WrapUtils.getString(item.getLastName());
        this.middleName = WrapUtils.getString(item.getMiddleName());
        this.name = (this.firstName + " " + this.lastName).trim();

        if (item.getPosition() != null) {
            this.position = new SelectItemTO(item.getPosition());
        }

        if (item.getDepartments() != null) {
            this.department = new SelectItemTO(item.getDepartments().getName());
        }
    }

    public UserTO(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public UserTO(KpiTreeInfo item) {
        this.name = item.getName();
        this.imageUrl = item.getImageUrl();

        if (item.getDepartmentId() != null || item.getDepartmentName() != null) {
            this.department = new SelectItemTO(item.getDepartmentId(), item.getDepartmentName());
        }

        if (item.getPositionId() != null || item.getPositionName() != null) {
            this.position = new SelectItemTO(item.getPositionId(), item.getPositionName());
        }
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public SelectItemTO getPosition() {
        return position;
    }

    public void setPosition(SelectItemTO position) {
        this.position = position;
    }

    public SelectItemTO getDepartment() {
        return department;
    }

    public void setDepartment(SelectItemTO department) {
        this.department = department;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ArrayList<SelectItemTO> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<SelectItemTO> roles) {
        this.roles = roles;
    }

    public ArrayList<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(ArrayList<String> permissions) {
        this.permissions = permissions;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }
}
