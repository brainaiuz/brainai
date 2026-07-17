package com.edatasite.workforce.core.domain;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.customfields.EdsCustomFields;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: Sanjar
 * Date: Apr 20, 2011
 * Time: 5:12:36 PM
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "itemcustomfields")
public class EdsItemCustomFields extends EdsCustomFields {
}
