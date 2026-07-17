package com.edatasite.workforce.core.domain.customfields;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by dilsh0d on 23.01.16.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "placementcustomfields")
public class EdsPlacementCustomFields extends EdsCustomFields {
}
