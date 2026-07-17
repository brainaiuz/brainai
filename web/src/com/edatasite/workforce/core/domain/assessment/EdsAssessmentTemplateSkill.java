package com.edatasite.workforce.core.domain.assessment;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsSkill;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Iskandar
 * Date: 03-Aug-2007
 * Time: 17:59:31
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "assessmentTemplateSkill")
public class EdsAssessmentTemplateSkill extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assessmentTemplateId")
    private EdsAssessmentTemplate assessmentTemplate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "skillId")
    private EdsSkill skill;

    private String name;

    private Double weight = 1d;
    private Boolean showSlider = true;

    public EdsAssessmentTemplate getAssessmentTemplate() {
        return assessmentTemplate;
    }

    public void setAssessmentTemplate(EdsAssessmentTemplate assessmentTemplate) {
        this.assessmentTemplate = assessmentTemplate;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public Boolean getShowSlider() {
        return showSlider;
    }

    public void setShowSlider(Boolean showSlider) {
        this.showSlider = showSlider;
    }

    public EdsSkill getSkill() {
        return skill;
    }

    public void setSkill(EdsSkill skill) {
        this.skill = skill;
    }

}
