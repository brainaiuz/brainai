package com.edatasite.workforce.gwt.dashboard.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Created by IntelliJ IDEA.
 * User: Dilshod
 * Date: 20.08.2009
 * Time: 14:07:35
 * To change this template use File | Settings | File Templates.
 */
public class ProjectInvolment implements IsSerializable {

    private Double val;
    private String category;
    private Integer projectId;
    private String projectIds;
    private String values;
    private String categorys;
    private Integer departmentId;
    private Integer employeeId;

    public ProjectInvolment() {
    }

    public ProjectInvolment(String category, Double val, Integer projectId, String categorys, String values, String projectIds) {
        this.val = val;
        this.category = category;
        this.values = values;
        this.categorys = categorys;
        this.projectId = projectId;
        this.projectIds = projectIds;
    }

    public Double getVal() {
        return val;
    }

    public void setVal(Double val) {
        this.val = val;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(String projectIds) {
        this.projectIds = projectIds;
    }

    public String getValues() {
        return values;
    }

    public void setValues(String values) {
        this.values = values;
    }

    public String getCategorys() {
        return categorys;
    }

    public void setCategorys(String categorys) {
        this.categorys = categorys;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }
}
