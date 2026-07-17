package com.edatasite.workforce.core.domain.customfields;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User : Bekhruz on 11/12/2021
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "accountcustomfields")
public class EdsAccountCustomFields extends EdsCustomFields {
}
