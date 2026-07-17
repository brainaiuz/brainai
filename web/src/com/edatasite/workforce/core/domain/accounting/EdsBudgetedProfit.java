package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 03.04.2009
 * Time: 14:54:34
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "budgetedProfit")
public class EdsBudgetedProfit extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }


    private Date date;

    @Column(precision = 25, scale = 5)
    private BigDecimal grossBudget;

    @Column(precision = 25, scale = 5)
    private BigDecimal netBudget;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public BigDecimal getGrossBudget() {
        return grossBudget;
    }

    public void setGrossBudget(BigDecimal grossBudget) {
        this.grossBudget = grossBudget;
    }

    public BigDecimal getNetBudget() {
        return netBudget;
    }

    public void setNetBudget(BigDecimal netBudget) {
        this.netBudget = netBudget;
    }
}
