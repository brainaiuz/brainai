package com.edatasite.workforce.core.domain.customfields;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Normurod Buriev
 * Date: 10/28/11
 * Time: 4:33 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "invoicecustomfields")
public class EdsInvoiceCustomFields extends EdsCustomFields {
}
