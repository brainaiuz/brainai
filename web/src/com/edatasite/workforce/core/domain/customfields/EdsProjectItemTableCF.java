package com.edatasite.workforce.core.domain.customfields;


import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "project_item_table_customfields")
public class EdsProjectItemTableCF extends EdsCustomFields {
}
