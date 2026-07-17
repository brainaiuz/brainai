package com.edatasite.workforce.core.domain.customfields;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: Abror Abdukadirov
 * Date: 01.05.2017 19:48
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "sickrequestcustomfields")
public class EdsSickRequestCustomFields extends EdsCustomFields {
}
