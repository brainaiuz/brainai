package com.edatasite.workforce.core.domain.payrolluk;

import com.edatasite.shared.db.EdsScope;
import com.edatasite.workforce.core.domain.customfields.EdsCustomFields;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "apItemCustomField")
public class EdsAdditionalPaymentItemTableCF extends EdsCustomFields {
}
