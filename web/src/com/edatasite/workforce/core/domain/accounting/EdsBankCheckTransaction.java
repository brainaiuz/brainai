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
 * Date: 5/15/12
 * Time: 7:45 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "bankCheckTransaction")
public class EdsBankCheckTransaction extends EdsTransaction {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bankcheckid")
    private EdsBankCheck bankCheck;

    public EdsBankCheck getBankCheck() {
        return bankCheck;
    }

    public void setBankCheck(EdsBankCheck bankCheck) {
        this.bankCheck = bankCheck;
    }

    @Override
    public Integer getKeyId() {
        return bankCheck.getObjectID();
    }

    @Override
    public String getKeyType() {
        return BANK_CHECK_TRANSACTION;
    }
}
