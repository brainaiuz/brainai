package com.edatasite.workforce.gwt.assessment.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CustomFormLocalization;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.io.Serializable;

/**
 * DTO for Skill Group (used in GWT RPC communication).
 */
public class SkillGroupItem implements IsSerializable, Serializable {

    // Constants for column mapping (UI/DB alignment)
    public static final String ACTION = "action";
    public static final String COMPETENCY_GROUP_NAME = "competencyGroupName";
    public static final String COMPETENCY_GROUP_CODE = "competencyGroupCode";
    public static final String COMPETENCY_GROUP_PARENT_NAME = "competencyGroupParentName";

    private Integer id;
    private String name;
    private Integer parentId;
    private String parentName;
    private String code;
    private CustomFormLocalization localization;


    public SkillGroupItem() {
    }

     public SkillGroupItem(Integer id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    // ------------------------
    // Getters and Setters
    // ------------------------

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
        this.parentId = parentId;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public CustomFormLocalization getLocalization() {
        return localization;
    }

    public void setLocalization(CustomFormLocalization localization) {
        this.localization = localization;
    }

    @Override
    public String toString() {
        return "SkillGroupItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parentId=" + parentId +
                ", parentName='" + parentName + '\'' +
                ", code='" + code + '\'' +
                ", localization=" + localization +
                '}';
    }
}
