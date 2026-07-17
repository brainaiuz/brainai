package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.accounting.EdsTransaction;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Mar 12, 2011
 * Time: 12:58:06 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "payslipTransaction")
public class EdsPayslipTransaction extends EdsTransaction{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payslipid")
    private P11 payslip;

    public P11 getPayslip() {
        return payslip;
    }

    public void setPayslip(P11 payslip) {
        this.payslip = payslip;
    }

    public Integer getKeyId() {
        return getPayslip().getObjectID();
    }

    public String getKeyType() {
        return PAYSLIP_TRANSACTION;
    }
}
