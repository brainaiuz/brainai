package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 5/11/11
 * Time: 12:42 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "depreciationTransaction")
public class EdsDepreciationTransaction extends EdsTransaction{
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "depreciationid")
    private EdsDepreciation depreciation;

    public EdsDepreciation getDepreciation() {
        return depreciation;
    }

    public void setDepreciation(EdsDepreciation depreciation) {
        this.depreciation = depreciation;
    }

    public Integer getKeyId() {
        return getDepreciation().getObjectID();
    }

    public String getKeyType() {
        return DEPRECIATION_TRANSACTION;
    }
}
