package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Apr 13, 2011
 * Time: 2:59:16 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "customerTransaction")
public class EdsCustomerTransaction extends EdsTransaction {

    public Integer getKeyId() {
        return getClient().getObjectID();
    }

    public String getKeyType() {
        return CUSTOMER_TRANSACTION;
    }
}
