package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.gwt.core.client.rpc.payroll.CategoryRate;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Ruslan Muhammadov
 * Date: 2011-07-19
 * Time: 1:00 AM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "multirangerate")
public class EdsMultiRangeRate extends EdsObject {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formula_id")
    private EdsFormula formula;

    @Column(name = "from_rate")
    private BigDecimal from;// From - beginning of the rating interval.

    @Column(name = "to_rate")
    private BigDecimal to;// To - end(limit) of the rating interval.

    @Column(name = "percentage")
    private BigDecimal percentage;// 0.01 = 1%, 0.1 = 10%, 1 = 100%, etc...

    @Column(name = "fixed_amount")
    private BigDecimal fixedAmount;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsFormula getFormula() {
        return formula;
    }

    public void setFormula(EdsFormula formula) {
        this.formula = formula;
    }

    public BigDecimal getFrom() {
        return from;
    }

    public void setFrom(BigDecimal from) {
        this.from = from;
    }

    public BigDecimal getTo() {
        return to;
    }

    public void setTo(BigDecimal to) {
        this.to = to;
    }

    public BigDecimal getPercentage() {
        return percentage;
    }

    public void setPercentage(BigDecimal percentage) {
        this.percentage = percentage;
    }

    public BigDecimal getFixedAmount() {
        return fixedAmount;
    }

    public void setFixedAmount(BigDecimal fixedAmount) {
        this.fixedAmount = fixedAmount;
    }

    public CategoryRate getAsRPC() {
        CategoryRate categoryRate = new CategoryRate();
        categoryRate.setFrom(getFrom());
        categoryRate.setTo(getTo());
        categoryRate.setFixedAmount(getFixedAmount());
        categoryRate.setPercentage(getPercentage());
        return categoryRate;
    }
}
