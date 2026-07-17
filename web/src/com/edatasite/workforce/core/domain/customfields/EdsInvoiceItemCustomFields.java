package com.edatasite.workforce.core.domain.customfields;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Normurod on 3/25/2017.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "invoiceitemcustomfields")
public class EdsInvoiceItemCustomFields extends EdsCustomFields {
}
