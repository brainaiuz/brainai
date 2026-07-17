package com.edatasite.workforce.core.domain.customfields;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: Faxriddin Taslimov Date: 20:08:05
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "employeestepcustomfields")
public class EdsEmployeeStepCustomFields extends EdsCustomFields {
}
