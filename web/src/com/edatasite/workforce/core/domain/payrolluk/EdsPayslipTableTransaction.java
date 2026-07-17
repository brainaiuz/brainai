package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.accounting.EdsTransaction;

import javax.persistence.*;

/**
 * Created with IntelliJ IDEA.
 * User: Bunyod Xalilov
 * Date: 23.03.14
 * Time: 15:51
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "payslipTableTransaction")
public class EdsPayslipTableTransaction extends EdsTransaction {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payslip_table_id")
    private EdsPayslipTable payslipTable;

    public EdsPayslipTable getPayslipTable() {
        return payslipTable;
    }

    public void setPayslipTable(EdsPayslipTable payslipTable) {
        this.payslipTable = payslipTable;

        setCurrencyID(payslipTable.getCurrency() != null ? payslipTable.getCurrency().getObjectID() : null);
        setExchangeRate(payslipTable.getExchangeRate());
    }

    public Integer getKeyId() {
        return getPayslipTable().getObjectID();
    }

    public String getKeyType() {
        return PAYSLIP_TABLE_TRANSACTION;
    }
}
