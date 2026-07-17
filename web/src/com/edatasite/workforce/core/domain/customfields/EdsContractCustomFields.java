package com.edatasite.workforce.core.domain.customfields;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by Omonullo Abdullaev on 8/4/2016.
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "contractcustomfields")
public class EdsContractCustomFields extends EdsCustomFields {
}
