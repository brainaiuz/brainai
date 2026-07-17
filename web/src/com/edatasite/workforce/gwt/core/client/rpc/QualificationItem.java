package com.edatasite.workforce.gwt.core.client.rpc;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * User: Sher
 * Date: 12/4/12 7:20 PM
 */
public class QualificationItem implements IsSerializable {

    private Integer objectId;
    private String departmentName;
    private String qualificationName;

    public QualificationItem() {
    }

    public QualificationItem(Integer objectId, String departmentName, String qualificationName) {
        this.objectId = objectId;
        this.departmentName = departmentName;
        this.qualificationName = qualificationName;
    }

    public Integer getObjectId() {
        return objectId;
    }

    public void setObjectId(Integer objectId) {
        this.objectId = objectId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getQualificationName() {
        return qualificationName;
    }

    public void setQualificationName(String qualificationName) {
        this.qualificationName = qualificationName;
    }
}
