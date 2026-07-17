package com.edatasite.workforce.core.domain.trainingcenter;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsLocation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 05.01.14
 * Time: 23:45
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "contractCoursePrice")
public class EdsContractCoursePrice extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    private EdsCourse course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "contract_id")
    private EdsTrainingContract contract;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "location_id")
    private EdsLocation location;

    @Column(name = "price", precision = 14, scale = 4)
    private BigDecimal price;

    @Column(name = "stopFee", precision = 14, scale = 4)
    private BigDecimal stopFee;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsCourse getCourse() {
        return course;
    }

    public void setCourse(EdsCourse course) {
        this.course = course;
    }

    public EdsTrainingContract getContract() {
        return contract;
    }

    public void setContract(EdsTrainingContract contract) {
        this.contract = contract;
    }

    public Boolean getDeleted() {
        return deleted == null ? Boolean.FALSE : deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public EdsLocation getLocation() {
        return location;
    }

    public void setLocation(EdsLocation location) {
        this.location = location;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getStopFee() {
        return stopFee;
    }

    public void setStopFee(BigDecimal stopFee) {
        this.stopFee = stopFee;
    }
}

