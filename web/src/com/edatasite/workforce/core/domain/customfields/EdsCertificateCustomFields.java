package com.edatasite.workforce.core.domain.customfields;

import com.edatasite.shared.db.EdsScope;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * User : Akhror on 28/10/2021
 */
@Entity
@Table(schema = EdsScope.PRIVATE_SCHEMA, name = "certificatecustomfields")
public class EdsCertificateCustomFields extends EdsCustomFields {
}
