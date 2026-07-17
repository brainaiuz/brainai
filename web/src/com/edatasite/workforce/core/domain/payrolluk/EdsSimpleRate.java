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
 * Time: 12:58 AM
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "simplerate")
@Inheritance(strategy = InheritanceType.JOINED)
public class EdsSimpleRate extends EdsObject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @Column(name = "percentage")
    private BigDecimal percentage;// 0.01 = 1%, 0.1 = 10%, 1 = 100%, etc...

    @Column(name = "fixed_amount")
    private BigDecimal fixedAmount;

    @Override
    public Integer getObjectID() {
        return objectID;
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
        categoryRate.setPercentage(getPercentage());
        categoryRate.setFixedAmount(getFixedAmount());
        return categoryRate;
    }
}
