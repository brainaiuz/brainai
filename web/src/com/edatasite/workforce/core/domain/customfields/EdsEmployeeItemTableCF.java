package com.edatasite.workforce.core.domain.customfields;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.Table;


@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "employee_item_table_customfields")
public class EdsEmployeeItemTableCF extends EdsCustomFields {
}
