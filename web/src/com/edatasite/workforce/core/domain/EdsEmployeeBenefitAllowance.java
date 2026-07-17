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

/**
 * Created by Djuraev on 8/3/15.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "employeebenefit")
public class EdsEmployeeBenefitAllowance extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "allowance", nullable = false, columnDefinition = "Decimal(10,2) default 0.00")
    private Double allowance = 0.00;

    @Column(name = "allowanceyear", nullable = false, columnDefinition = "int4 default 0")
    private Integer allowanceYear = 0;

    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.LAZY)
    @JoinColumn(name = "employeeID")
    private EdsEmployee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "benefitID")
    private EdsBenefit benefit;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public Double getAllowance() {
        return allowance;
    }

    public void setAllowance(Double allowance) {
        this.allowance = allowance;
    }

    public Integer getAllowanceYear() {
        return allowanceYear;
    }

    public void setAllowanceYear(Integer allowanceYear) {
        this.allowanceYear = allowanceYear;
    }

    public EdsEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(EdsEmployee employee) {
        this.employee = employee;
    }

    public EdsBenefit getBenefit() {
        return benefit;
    }

    public void setBenefit(EdsBenefit benefit) {
        this.benefit = benefit;
    }
}
