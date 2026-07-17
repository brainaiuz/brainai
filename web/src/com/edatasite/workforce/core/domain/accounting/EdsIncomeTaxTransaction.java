package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 12/9/13
 * Time: 3:43 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "incometaxtransaction")
public class EdsIncomeTaxTransaction extends EdsTransaction {
    private String incomeTaxPeriodType;

    public String getIncomeTaxPeriodType() {
        return incomeTaxPeriodType;
    }

    public void setIncomeTaxPeriodType(String incomeTaxPeriodType) {
        this.incomeTaxPeriodType = incomeTaxPeriodType;
    }
}
