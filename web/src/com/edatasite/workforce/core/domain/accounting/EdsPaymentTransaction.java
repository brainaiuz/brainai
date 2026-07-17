package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.*;

/**
 * Created by IntelliJ IDEA.
 * User: Anvarbek
 * Date: 30.03.2009
 * Time: 18:09:45
 * To change this template use File | Settings | File Templates.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "paymentTransaction")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public class EdsPaymentTransaction extends EdsTransaction {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "targetTransactionid")
    private EdsTransaction targetTransaction;

    public EdsTransaction getTargetTransaction() {
        return targetTransaction;
    }

    public void setTargetTransaction(EdsTransaction targetTransaction) {
        this.targetTransaction = targetTransaction;
    }
}
