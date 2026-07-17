package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * User: Faxriddin Taslimov : 08/07/15
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "hrremindertimeaction")
public class EdsHrReminderTimeAction extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "actiontype")
    private String actiontype;

    @Column(name = "actionNumber")
    private Integer actionNumber;

    @Column(name = "actionPeriod")
    private String actionPeriod;


    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public String getActiontype() {
        return actiontype;
    }

    public void setActiontype(String actiontype) {
        this.actiontype = actiontype;
    }

    public Integer getActionNumber() {
        return actionNumber;
    }

    public void setActionNumber(Integer actionNumber) {
        this.actionNumber = actionNumber;
    }

    public String getActionPeriod() {
        return actionPeriod;
    }

    public void setActionPeriod(String actionPeriod) {
        this.actionPeriod = actionPeriod;
    }
}
