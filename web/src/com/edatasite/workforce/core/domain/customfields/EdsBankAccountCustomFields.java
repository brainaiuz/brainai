package com.edatasite.workforce.core.domain.customfields;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Dilshod on 4/3/2016.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "bankaccountcustomfields")
public class EdsBankAccountCustomFields extends EdsCustomFields {
}
