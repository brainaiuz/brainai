package com.edatasite.workforce.core.domain.customfields;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: Ilhom
 * Date: 28.06.13
 * Time: 8:36
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "estimateOfContractCustomFields")
public class EdsEstimateOfContractCustomFields extends EdsCustomFields {
}
