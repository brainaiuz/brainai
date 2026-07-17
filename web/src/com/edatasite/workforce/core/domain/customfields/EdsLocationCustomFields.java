package com.edatasite.workforce.core.domain.customfields;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User : Jamshid on 24/01/2022
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "locationcustomfields")
public class EdsLocationCustomFields extends EdsCustomFields {
}
