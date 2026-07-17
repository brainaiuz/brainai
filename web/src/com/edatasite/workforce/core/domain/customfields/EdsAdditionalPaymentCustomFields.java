package com.edatasite.workforce.core.domain.customfields;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "additionalpaymentcustomfields")
public class EdsAdditionalPaymentCustomFields extends EdsCustomFields {

}
