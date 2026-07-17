package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.accounting.EdsTransaction;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 1/27/15
 * Time: 6:28 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "payrunTransaction")
public class EdsSinglePayrunTransaction extends EdsTransaction {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payrun_id")
    private EdsPayslipTableItem payrun;

    public EdsPayslipTableItem getPayrun() {
        return payrun;
    }

    public void setPayrun(EdsPayslipTableItem payrun) {
        this.payrun = payrun;

        setCurrencyID(payrun.getCurrency() != null ? payrun.getCurrency().getObjectID() : null);
        setExchangeRate(payrun.getExchangeRate());
    }

    public Integer getKeyId() {
        return getPayrun().getObjectID();
    }

    public String getKeyType() {
        return SINGLE_PAYRUN_TRANSACTION;
    }

}
