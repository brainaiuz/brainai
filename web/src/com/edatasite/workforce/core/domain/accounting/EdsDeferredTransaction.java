package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.enums.DeferredTransactionType;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

/**
 * Created by Normurod Buriev.
 * Date: 6/21/2021 7:58 PM
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "deferredTransaction")
public class EdsDeferredTransaction extends EdsTransaction {
    @Enumerated(EnumType.STRING)
    private DeferredTransactionType deferredType;

    private Integer deferredObjectId;

    public DeferredTransactionType getDeferredType() {
        return deferredType;
    }

    public void setDeferredType(DeferredTransactionType deferredType) {
        this.deferredType = deferredType;
    }

    public Integer getDeferredObjectId() {
        return deferredObjectId;
    }

    public void setDeferredObjectId(Integer deferredObjectId) {
        this.deferredObjectId = deferredObjectId;
    }

    @Override
    public Integer getKeyId() {
        return deferredObjectId;
    }

    @Override
    public String getKeyType() {
        return DEFERRED_TRANSACTION;
    }
}
