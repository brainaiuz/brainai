package com.edatasite.workforce.core.domain.documents;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * User: Faxriddin Taslimov  * Date: 08.02.2017
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "insurancedocument")
public class EdsInsuranceDocument extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "insureeName")
    private String insureeName;

    @Column(name = "insureeLastName")
    private String insureeLastName;

    @Column(name = "statusId")
    private Integer statusId;

    @Column(name = "insuranceCost")
    private String insuranceCost;

    @Column(name = "insurancePlan")
    private String insurancePlan;

    @Column(name = "insuranceCoverage")
    private String insuranceCoverage;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public String getInsureeName() {
        return insureeName;
    }

    public void setInsureeName(String insureeName) {
        this.insureeName = insureeName;
    }

    public String getInsureeLastName() {
        return insureeLastName;
    }

    public void setInsureeLastName(String insureeLastName) {
        this.insureeLastName = insureeLastName;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getInsuranceCost() {
        return insuranceCost;
    }

    public void setInsuranceCost(String insuranceCost) {
        this.insuranceCost = insuranceCost;
    }

    public String getInsurancePlan() {
        return insurancePlan;
    }

    public void setInsurancePlan(String insurancePlan) {
        this.insurancePlan = insurancePlan;
    }

    public String getInsuranceCoverage() {
        return insuranceCoverage;
    }

    public void setInsuranceCoverage(String insuranceCoverage) {
        this.insuranceCoverage = insuranceCoverage;
    }
}
