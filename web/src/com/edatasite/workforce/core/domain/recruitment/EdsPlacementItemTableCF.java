package com.edatasite.workforce.core.domain.recruitment;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.customfields.EdsCustomFields;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "placementItemCustomField")
public class EdsPlacementItemTableCF extends EdsCustomFields {
}
