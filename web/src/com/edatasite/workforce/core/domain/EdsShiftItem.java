package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.assessment.EdsEmployeeAssessment;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "shift_items")
public class EdsShiftItem extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;


    @Column(name = "groupId")
    private Integer groupId;

    @ManyToOne
    @JoinColumn(name = "shift_id")
    private EdsShift shift;

    @Column(name = "key")
    private String key;

    @ManyToOne
    @JoinColumn(name = "shift_settings_id")
    private EdsShiftSettings timeSlot;

    private Boolean deleted;

    private Integer row;

    @ManyToOne
    @JoinColumn(name = "employee_assessment_id")
    private EdsEmployeeAssessment employeeAssessment;


    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public EdsShift getShift() {
        return shift;
    }

    public void setShift(EdsShift shift) {
        this.shift = shift;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Integer getRow() {
        return row;
    }

    public void setRow(Integer row) {
        this.row = row;
    }

    public EdsShiftSettings getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(EdsShiftSettings timeSlot) {
        this.timeSlot = timeSlot;
    }

    public EdsEmployeeAssessment getEmployeeAssessment() {
        return employeeAssessment;
    }

    public void setEmployeeAssessment(EdsEmployeeAssessment employeeAssessment) {
        this.employeeAssessment = employeeAssessment;
    }
}
