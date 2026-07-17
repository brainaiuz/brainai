package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsObject;
import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.crm.EdsCrmAccount;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: 11/3/11
 * Time: 4:42 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "prepaymentbalance")
public class EdsProjectPrepaymentBalance extends EdsObject{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer objectID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "crmAccountID")
    private EdsCrmAccount crmAccount;

    @Column(name = "balance", precision = 25, scale = 5)
    private BigDecimal balance;

    public Integer getObjectID() {
        return objectID;
    }

    public void setObjectID(Integer objectID) {
        this.objectID = objectID;
    }

    public EdsCrmAccount getCrmAccount() {
        return crmAccount;
    }

    public void setCrmAccount(EdsCrmAccount crmAccount) {
        this.crmAccount = crmAccount;
    }

    public BigDecimal getBalance() {
        return balance != null ? balance : BigDecimal.ZERO;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
