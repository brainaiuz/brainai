package com.edatasite.workforce.gwt.assessment.client.rpc;

import com.edatasite.workforce.gwt.core.client.rpc.CustomFormLocalization;
import com.edatasite.workforce.gwt.core.client.rpc.SelectItem;
import com.google.gwt.user.client.rpc.IsSerializable;

public class SkillItem implements IsSerializable {

    public static final String ACTION = "action";
    public static final String COMPETENCY_GROUP_NAME = "competencyGroupName";
    public static final String COMPETENCY_NAME = "competencyName";
    public static final String COMPETENCY_DESCRIPTION = "competencyDescription";

    private Integer id;
    private Integer groupId;
    private String groupName;
    private String name;
    private String description;
    private Integer companyId;
    private String companyName;
    private Double weight;
    private Double givenScore = 0.d;
    private SelectItem[] skillGroups;
    private CustomFormLocalization skillNameLocalization;
    private CustomFormLocalization skillDescriptionLoc;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Integer companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Double getGivenScore() {
        return givenScore;
    }

    public void setGivenScore(Double givenScore) {
        this.givenScore = givenScore;
    }

    public SelectItem[] getSkillGroups() {
        return skillGroups;
    }

    public void setSkillGroups(SelectItem[] skillGroups) {
        this.skillGroups = skillGroups;
    }

    public CustomFormLocalization getSkillNameLocalization() {
        return skillNameLocalization;
    }

    public void setSkillNameLocalization(CustomFormLocalization skillNameLocalization) {
        this.skillNameLocalization = skillNameLocalization;
    }

    public CustomFormLocalization getSkillDescriptionLoc() {
        return skillDescriptionLoc;
    }

    public void setSkillDescriptionLoc(CustomFormLocalization skillDescriptionLoc) {
        this.skillDescriptionLoc = skillDescriptionLoc;
    }
}
