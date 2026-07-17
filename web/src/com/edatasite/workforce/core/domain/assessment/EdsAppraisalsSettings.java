package com.edatasite.workforce.core.domain.assessment;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsRole;
import com.edatasite.workforce.gwt.assessment.client.rpc.AppraisalsSettingsItem;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

/**
 * User: Sher
 * Date: 7/24/12
 * Time: 4:18 PM
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "appraisalssettings")
public class EdsAppraisalsSettings extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "appraisalssettings_role",
            joinColumns = {@JoinColumn(name = "appraisalssettings_id")},
            inverseJoinColumns = {@JoinColumn(name = "reviewers_id")}
    )
    private Set<EdsRole> reviewers = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
   @JoinTable(schema = EdsScope.PRIVATE_SCHEMA, name = "appraisalssettings_score_types",
            joinColumns = {@JoinColumn(name = "appraisalssettings_id")},
            inverseJoinColumns = {@JoinColumn(name = "scoretypes_id")}
    )
    private Set<EdsApprasialScoreType> scoreTypes = new HashSet<>();

    @Column(columnDefinition = " boolean DEFAULT false")
    private boolean useCompetencies = true;
    @Column(columnDefinition = " boolean DEFAULT true")
    private boolean useGoals = true;
    @Column(columnDefinition = " boolean DEFAULT false")
    private boolean employeeRate = false;
    private double fromScale = 0;
    private double toScale = 7;
    private double stepSize = 1;

    private Double oldFromScale = 0d;
    private Double oldToScale = 7d;
    private Double oldStepSize = 1d;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Set<EdsRole> getReviewers() {
        return reviewers;
    }

    public void setReviewers(Set<EdsRole> reviewers) {
        this.reviewers = reviewers;
    }


    public Set<EdsApprasialScoreType> getScoreTypes() {
        return scoreTypes;
    }

    public void setScoreTypes(Set<EdsApprasialScoreType> scoreTypes) {
        this.scoreTypes = scoreTypes;
    }

    public boolean isUseCompetencies() {
        return useCompetencies;
    }

    public void setUseCompetencies(boolean useCompetencies) {
        this.useCompetencies = useCompetencies;
    }

    public boolean isUseGoals() {
        return useGoals;
    }

    public void setUseGoals(boolean useGoals) {
        this.useGoals = useGoals;
    }

    public double getFromScale() {
        return fromScale;
    }

    public void setFromScale(double fromScale) {
        this.fromScale = fromScale;
    }

    public double getToScale() {
        return toScale;
    }

    public void setToScale(double toScale) {
        this.toScale = toScale;
    }

    public double getStepSize() {
        return stepSize;
    }

    public void setStepSize(double stepSize) {
        this.stepSize = stepSize;
    }

    public Double getOldFromScale() {
        return oldFromScale;
    }

    public void setOldFromScale(Double oldFromScale) {
        this.oldFromScale = oldFromScale;
    }

    public Double getOldToScale() {
        return oldToScale;
    }

    public void setOldToScale(Double oldToScale) {
        this.oldToScale = oldToScale;
    }

    public Double getOldStepSize() {
        return oldStepSize;
    }

    public void setOldStepSize(Double oldStepSize) {
        this.oldStepSize = oldStepSize;
    }

    public boolean isEmployeeRate() {
        return employeeRate;
    }

    public void setEmployeeRate(boolean employeeRate) {
        this.employeeRate = employeeRate;
    }

    public AppraisalsSettingsItem getDTO() {
        AppraisalsSettingsItem item = new AppraisalsSettingsItem();

        for (EdsRole role : reviewers) {
            item.addReviewer(role.getCode());
        }
        item.setUseCompetencies(this.isUseCompetencies());
        item.setUseGoals(this.isUseGoals());
        item.setEmployeeRate(this.isEmployeeRate());
        item.setFromScale(this.getFromScale());
        item.setToScale(this.getToScale());
        item.setStepSize(this.getStepSize());
        return item;
    }
}
