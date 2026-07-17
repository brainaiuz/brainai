package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Farhod
 * Date: 04/04/12
 * Time: 11:45
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "annualleave",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"allowanceyear", "employeeID", "reason_code"})}
)
public class EdsAnnualLeaveAllowance extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "annualallowanceminutes", nullable = false, columnDefinition = "int4 default 0")
    private Integer annualAllowanceMinutes = 0;//allowed leave minutes per year

    @Column(name = "allowanceDays", columnDefinition = "Decimal(10,2) default 0.00")
    private Double allowanceDays = 0d;

    @Column(name = "allowanceyear", nullable = false, columnDefinition = "int4 default 0")
    private Integer allowanceYear = 0;

    @Column(name = "addprevious", nullable = false, columnDefinition = "boolean default false")
    private Boolean addPrevious = false;

    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeID")
    private EdsEmployee employee;

    @Column(name = "reason_code")
    private String reasonCode;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reason_code", referencedColumnName = "code", updatable = false, insertable = false)
    private EdsLeaveReason reason;

    @Column(name = "lastyearminutes", columnDefinition = "int4 default 0")
    private Integer lastYearMinutes = 0;

    @Column(name = "lastYearDay", columnDefinition = "Decimal(10,2) default 0.00")
    private Double lastYearDay = 0d;

    private Date effectiveDate;

    public Integer getObjectID() {
        return objectID;
    }

    public Integer getAnnualAllowanceMinutes() {
        if (annualAllowanceMinutes == null) {
            return 0;
        }
        return annualAllowanceMinutes;
    }

    public void setAnnualAllowanceMinutes(Integer annualAllowanceMinutes) {
        if (annualAllowanceMinutes == null) {
            annualAllowanceMinutes = 0;
        }
        this.annualAllowanceMinutes = annualAllowanceMinutes;
    }

    public Integer getAllowanceYear() {
        return allowanceYear;
    }

    public void setAllowanceYear(Integer allowanceYear) {
        this.allowanceYear = allowanceYear;
    }

    public Boolean getAddPrevious() {
        return addPrevious;
    }

    public void setAddPrevious(Boolean addPrevious) {
        this.addPrevious = addPrevious;
    }

    public EdsEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(EdsEmployee employee) {
        this.employee = employee;
    }

    public EdsLeaveReason getReason() {
        return reason;
    }

    public void setReason(EdsLeaveReason reason) {
        this.reason = reason;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    public Integer getLastYearMinutes() {
        return lastYearMinutes;
    }

    public void setLastYearMinutes(Integer lastYearMinutes) {
        this.lastYearMinutes = lastYearMinutes;
    }

    public Double getAllowanceDays() {
        if (allowanceDays == null) {
            allowanceDays = 0d;
        }
        return allowanceDays;
    }

    public void setAllowanceDays(Double allowanceDays) {
        this.allowanceDays = allowanceDays;
    }

    public Double getLastYearDay() {
        if (lastYearDay == null) {
            lastYearDay = 0d;
        }
        return lastYearDay;
    }

    public void setLastYearDay(Double lastYearDay) {
        this.lastYearDay = lastYearDay;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}
