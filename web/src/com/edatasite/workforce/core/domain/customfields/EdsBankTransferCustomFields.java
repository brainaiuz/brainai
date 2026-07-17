package com.edatasite.workforce.core.domain.customfields;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Omonullo on 3/1/2017.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "spendreceivemoneycustomfields")
public class EdsBankTransferCustomFields extends EdsCustomFields{
}
