package com.edatasite.workforce.core.domain.customfields;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: Dilsh0d
 * Email: dilshod.toj@gmail.com
 * Date: 24-Nov-2010
 * Time: 15:21:14
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "projectcustomfields")
public class EdsProjectCustomFields extends EdsCustomFields {
}
