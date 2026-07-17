package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.EdsUser;

import javax.persistence.*;

/**
 * Created by Sherzod on 6/19/2015.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "recurringbill")
public class EdsRecurringBill extends EdsBasePurchaseInvoice {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "senderId")
    private EdsUser sender;

    public EdsUser getSender() {
        return sender;
    }

    public void setSender(EdsUser sender) {
        this.sender = sender;
    }
}
