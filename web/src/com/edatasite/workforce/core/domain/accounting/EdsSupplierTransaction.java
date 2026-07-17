package com.edatasite.workforce.core.domain.accounting;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Sherzod
 * Date: Apr 14, 2011
 * Time: 12:10:03 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "supplierTransaction")
public class EdsSupplierTransaction extends EdsTransaction {

    public Integer getKeyId() {
        return getSupplier().getObjectID();
    }

    public String getKeyType() {
        return SUPPLIER_TRANSACTION;
    }
}
