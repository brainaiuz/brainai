package com.edatasite.workforce.core.domain.trainingcenter;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsReference;
import com.edatasite.workforce.gwt.trainingcenter.client.rpc.passport.PassportData;

import javax.persistence.*;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: acer
 * Date: 13/06/14
 * Time: 19:52
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "passport")
public class EdsPassport extends EdsObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "deleted")
    private Boolean deleted = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "studentid")
    private EdsStudent student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id")
    private EdsReference status;

    private String level;
    private String type;
    private String number;
    private String numberString;
    private Date creationDate;


    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public EdsStudent getStudent() {
        return student;
    }

    public void setStudent(EdsStudent student) {
        this.student = student;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNumberString() {
        return numberString;
    }

    public void setNumberString(String numberString) {
        this.numberString = numberString;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public EdsReference getStatus() {
        return status;
    }

    public void setStatus(EdsReference status) {
        this.status = status;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public PassportData getRPC() {
        PassportData passport = new PassportData();
        passport.setObjectID(getObjectID());
        passport.setNumber(getNumber());
        passport.setNumberString(getNumberString());
        passport.setType(getType());
        passport.setStatusID(getStatus().getObjectID());
        passport.setStatus(getStatus().getName());
        passport.setLevel(getLevel());
        passport.setStudentID(getStudent().getObjectID());
        passport.setStudentName(getStudent().getFullName());
        passport.setStudent(getStudent().getFullName() + (getStudent().getCustomer() != null ? (" (" + getStudent().getCustomer().getName() + ")") : ""));
        passport.setCreationDate(getCreationDate());
        return passport;
    }
}
