package com.edatasite.workforce.core.domain.customfields;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Ilhom Lutfullaev on 20.10.2017.
 */

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "payrollcustomfields")
public class EdsPayrollCustomFields extends EdsCustomFields {
}
