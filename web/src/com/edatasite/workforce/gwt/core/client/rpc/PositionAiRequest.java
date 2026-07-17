package com.edatasite.workforce.gwt.core.client.rpc;
import java.io.Serializable;

public class PositionAiRequest  implements Serializable{
    private Integer positionReferenceId;
    private Integer departmentReferenceId;

    public PositionAiRequest() {
    }

    public PositionAiRequest(Integer positionReferenceId, Integer departmentReferenceId) {
        this.positionReferenceId = positionReferenceId;
        this.departmentReferenceId = departmentReferenceId;
    }

    public Integer getPositionReferenceId() {
        return positionReferenceId;
    }

    public Integer getDepartmentReferenceId() {
        return departmentReferenceId;
    }

}