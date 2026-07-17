package com.edatasite.workforce.core.domain.customfields;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User: Aziz
 * Date: 30-July-2012
 * Time: 17:10:38
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "goalcustomfields")
public class EdsGoalCustomFields extends EdsCustomFields {
}
