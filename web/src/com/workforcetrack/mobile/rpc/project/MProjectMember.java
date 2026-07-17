package com.workforcetrack.mobile.rpc.project;

import com.edatasite.workforce.gwt.core.client.rpc.project.ProjectMember;

import java.text.ParseException;
import java.util.Map;

/**
 * User: sancho
 * Date: 5/31/11
 * Time: 4:28 PM
 */
public class MProjectMember {

    private Integer objectID;
    private Integer projectEmployeeID;
    private Integer departmentID;
    private String name;
    private String teamName;
    private Double wageRate;
    private String posititon;
    private Double clientChargeRate;
    private Float workloadPercentage;
    private Integer defaulDepartmentID;
    private Boolean check;
    private Integer permission;
    private Boolean isDeleted;


    public MProjectMember() {
    }

    //FOR API
    public MProjectMember(Map<String, Object> map) throws ParseException, ClassCastException, NumberFormatException {
        if (map != null && !map.isEmpty()) {
            this.objectID = (Integer) map.get("objectID");
            if (map.get("clientChargeRate") != null) {
                if (map.get("clientChargeRate") instanceof Float) {
                    Float f = (Float) map.get("clientChargeRate");
                    this.clientChargeRate = Double.valueOf(f.toString());
                } else {
                    this.clientChargeRate = (Double) map.get("clientChargeRate");
                }
            }
            if (map.get("wageRate") != null) {
                if (map.get("wageRate") instanceof Float) {
                    Float f = (Float) map.get("wageRate");
                    this.wageRate = Double.valueOf(f.toString());
                } else {
                    this.wageRate = (Double) map.get("wageRate");
                }
            }
            if (map.get("workloadPercentage") != null) {
                this.workloadPercentage = (Float) map.get("workloadPercentage");
            }
            this.departmentID = (Integer) map.get("departmentID");
            this.name = (String) map.get("name");

        }
    }

    public MProjectMember(ProjectMember projectMember) {

        if (projectMember != null) {
            this.objectID = projectMember.getId();
            this.projectEmployeeID = projectMember.getProjectEmployeeId();
            this.departmentID = projectMember.getDepartmentId();
            this.name = projectMember.getName();
            this.teamName = projectMember.getTeamName();
            this.wageRate = projectMember.getWageRate();
            this.posititon = projectMember.getPosititon();
            this.clientChargeRate = projectMember.getClientChargeRate();
            this.workloadPercentage = projectMember.getWorkloadPercentage();
            this.defaulDepartmentID = projectMember.getDefaulDepartmentId();
            this.check = projectMember.isCheck();
            this.permission = projectMember.getPermission();
            this.isDeleted = projectMember.isDeleted();
        }
    }

    public static boolean convert(ProjectMember projectMember, MProjectMember mProjectMember, boolean fromProjectMember) {
        if (projectMember == null || mProjectMember == null) {
            return false;
        }

        try {
            if (fromProjectMember) {
                mProjectMember.setObjectID(projectMember.getId());
                mProjectMember.setProjectEmployeeID(projectMember.getProjectEmployeeId());
                mProjectMember.setDepartmentID(projectMember.getDefaulDepartmentId());
                mProjectMember.setName(projectMember.getName());
                mProjectMember.setTeamName(projectMember.getTeamName());
                mProjectMember.setWageRate(projectMember.getWageRate());
                mProjectMember.setPosititon(projectMember.getPosititon());
                mProjectMember.setClientChargeRate(projectMember.getClientChargeRate());
                mProjectMember.setWorkloadPercentage(projectMember.getWorkloadPercentage());
                mProjectMember.setDefaulDepartmentID(projectMember.getDefaulDepartmentId());
                mProjectMember.setCheck(projectMember.isCheck());
                mProjectMember.setPermission(projectMember.getPermission());
                mProjectMember.setDeleted(projectMember.isDeleted());
            } else {
                projectMember.setId(mProjectMember.getObjectID());
                projectMember.setProjectEmployeeId(mProjectMember.getProjectEmployeeID());
                projectMember.setDepartmentId(mProjectMember.getDefaulDepartmentID());
                projectMember.setName(mProjectMember.getName());
                projectMember.setTeamName(mProjectMember.getTeamName());
                projectMember.setWageRate(mProjectMember.getWageRate());
                projectMember.setPosititon(mProjectMember.getPosititon());
                projectMember.setClientChargeRate(mProjectMember.getClientChargeRate());
                projectMember.setWorkloadPercentage(mProjectMember.getWorkloadPercentage());
                projectMember.setDefaulDepartmentId(mProjectMember.getDefaulDepartmentID());
                projectMember.setCheck(mProjectMember.getCheck() != null ? mProjectMember.getCheck() : false);
                projectMember.setPermission(mProjectMember.getPermission() != null ? mProjectMember.getPermission() : 0);
                projectMember.setDeleted(mProjectMember.getDeleted() != null ? mProjectMember.getDeleted() : false);
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

    public Integer getProjectEmployeeID() {
        return projectEmployeeID;
    }

    public void setProjectEmployeeID(Integer projectEmployeeID) {
        this.projectEmployeeID = projectEmployeeID;
    }

    public Integer getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(Integer departmentID) {
        this.departmentID = departmentID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Double getWageRate() {
        return wageRate;
    }

    public void setWageRate(Double wageRate) {
        this.wageRate = wageRate;
    }

    public String getPosititon() {
        return posititon;
    }

    public void setPosititon(String posititon) {
        this.posititon = posititon;
    }

    public Double getClientChargeRate() {
        return clientChargeRate;
    }

    public void setClientChargeRate(Double clientChargeRate) {
        this.clientChargeRate = clientChargeRate;
    }

    public Float getWorkloadPercentage() {
        return workloadPercentage;
    }

    public void setWorkloadPercentage(Float workloadPercentage) {
        this.workloadPercentage = workloadPercentage;
    }

    public Integer getDefaulDepartmentID() {
        return defaulDepartmentID;
    }

    public void setDefaulDepartmentID(Integer defaulDepartmentID) {
        this.defaulDepartmentID = defaulDepartmentID;
    }

    public Boolean getCheck() {
        return check;
    }

    public void setCheck(Boolean check) {
        this.check = check;
    }

    public Integer getPermission() {
        return permission;
    }

    public void setPermission(Integer permission) {
        this.permission = permission;
    }

    public Boolean getDeleted() {
        return isDeleted;
    }

    public void setDeleted(Boolean deleted) {
        isDeleted = deleted;
    }
}
