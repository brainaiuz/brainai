package com.edatasite.workforce.core.domain.customfields;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: Abror Abdukadirov
 * Date: 04.11.2017 15:25
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "rfqcustomfields")
public class EdsRFQCustomFields extends EdsCustomFields {
}
