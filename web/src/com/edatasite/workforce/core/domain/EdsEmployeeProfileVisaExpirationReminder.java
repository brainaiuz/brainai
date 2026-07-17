package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

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
 * User: Ilhombek
 * Date: 3/7/13
 * Time: 6:06 PM
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "employeeProfileVisaExpirationReminder")
public class EdsEmployeeProfileVisaExpirationReminder extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeProfileID")
    private EdsEmployeeProfile employeeProfile;

    @Column(name = "minutes")
    private Integer minutes;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsEmployeeProfile getEmployeeProfile() {
        return employeeProfile;
    }

    public void setEmployeeProfile(EdsEmployeeProfile employeeProfile) {
        this.employeeProfile = employeeProfile;
    }

    public Integer getMinutes() {
        return minutes;
    }

    public void setMinutes(Integer minutes) {
        this.minutes = minutes;
    }
}
