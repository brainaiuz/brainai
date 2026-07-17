package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "vatreturn_transaction")
public class EdsVatReturnTransaction extends EdsTransaction {

    @Column(name = "vatreturn_id")
    private Integer vatReturnId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vatreturn_id", insertable = false, updatable = false)
    private EdsVatReturn vatReturn;

    public Integer getVatReturnId() {
        return vatReturnId;
    }

    public void setVatReturnId(Integer vatReturnId) {
        this.vatReturnId = vatReturnId;
    }

    public EdsVatReturn getVatReturn() {
        return vatReturn;
    }

    public void setVatReturn(EdsVatReturn vatReturn) {
        this.vatReturn = vatReturn;
    }

    @Override
    public Integer getKeyId() {
        return vatReturnId;
    }
}
