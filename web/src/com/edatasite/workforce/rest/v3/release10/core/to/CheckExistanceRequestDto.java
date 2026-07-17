package com.edatasite.workforce.rest.v3.release10.core.to;

public class CheckExistanceRequestDto {
    private String formId;
    private Integer relationId;
    private String relationObjectKey;
    private String relationType;

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public Integer getRelationId() {
        return relationId;
    }

    public void setRelationId(Integer relationId) {
        this.relationId = relationId;
    }

    public String getRelationType() {
        return relationType;
    }

    public void setRelationType(String relationType) {
        this.relationType = relationType;
    }

    public String getRelationObjectKey() {
        return relationObjectKey;
    }

    public void setRelationObjectKey(String relationObjectKey) {
        this.relationObjectKey = relationObjectKey;
    }
}
