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
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Ilxom Lutfullaev
 * Date: 29.03.11
 * Time: 14:26
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PUBLIC_SCHEMA, name = "recurrenceHistory")
public class EdsRecurrenceHistory extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recurrenceLogId")
    private EdsServerHistory serverHistory;

    private Integer recurrenceID;
    private Integer companyID;
    private String jobName;
    private String jobType;
    private String cronExpression;
    private Date normalFireTime;
    private Date lateFireTime;
    private Boolean isFired = false;

    @Override
    public Integer getObjectID() {
        return objectID;
    }

    public EdsServerHistory getServerHistory() {
        return serverHistory;
    }

    public void setServerHistory(EdsServerHistory serverHistory) {
        this.serverHistory = serverHistory;
    }

    public Integer getRecurrenceID() {
        return recurrenceID;
    }

    public void setRecurrenceID(Integer recurrenceID) {
        this.recurrenceID = recurrenceID;
    }

    public Integer getCompanyID() {
        return companyID;
    }

    public void setCompanyID(Integer companyID) {
        this.companyID = companyID;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Date getNormalFireTime() {
        return normalFireTime;
    }

    public void setNormalFireTime(Date normalFireTime) {
        this.normalFireTime = normalFireTime;
    }

    public Date getLateFireTime() {
        return lateFireTime;
    }

    public void setLateFireTime(Date fireTime) {
        this.lateFireTime = fireTime;
    }

    public Boolean getFired() {
        return isFired;
    }

    public void setFired(Boolean fired) {
        isFired = fired;
    }
}
